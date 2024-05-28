package com.br.payments.bussiness.services;

import com.br.personaladm.api.filter.ContaFilter;
import com.br.personaladm.business.exception.ContaNotFoundException;
import com.br.personaladm.domain.model.Conta;
import com.br.personaladm.domain.model.parametro.ChavePasta;
import com.br.personaladm.domain.model.parametro.SulFixoTipoArquivo;
import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoConta;
import com.br.personaladm.domain.repository.ContaRepository;
import com.br.personaladm.domain.repository.FormaPagamentoRepository;
import com.br.personaladm.domain.repository.ParametroRepository;
import com.br.personaladm.domain.repository.TipoContaRepository;
import com.br.personaladm.domain.specs.ContaEspecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@Log4j2
@Service
public class ContaService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ContaRepository contaRepository;
    @Autowired
    private ParametroRepository parametroRepository;
    @Autowired
    private FileService fileService;
    @Autowired
    private TipoContaRepository tipoContaRepository;
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;

    /**
     * salva nova conta
     * @param conta
     * @return Conta
     */
    public Conta salvarConta(Conta conta){
        Conta contaSalva = contaRepository.save(conta);
        conta.setDataLancamento(LocalDateTime.now());

        if(Objects.isNull(conta.getTitulo()) || conta.getTitulo().isEmpty())
            contaSalva.setTitulo(criaNomeArquivoConta(conta, "", SulFixoTipoArquivo.TB));
        if(Objects.isNull(conta.getComprovante()) || conta.getComprovante().isEmpty())
            contaSalva.setComprovante(criaNomeArquivoConta(conta, "", SulFixoTipoArquivo.CO));

        return Optional.ofNullable(contaRepository.save(conta))
                .orElseThrow(()-> new RuntimeException("Erro ao salvar a conta!"));
    }

    /**
     * busca conta por id
     * @param id
     * @return Optional<Conta>
     */
    public Conta findContaPorId(Long id){
        return Optional.ofNullable(contaRepository.findById(id)).get().orElseThrow(ContaNotFoundException::new);
    }

    /**
     * apaga conta por id
     * @param id
     */
    public boolean deleteContaPorId(Long id){
        Optional<Conta> conta = Optional.of(findContaPorId(id));
        if(conta.isPresent()){
            excluirArquivoTitulo(conta.get());
            excluirArquivoComprovante(conta.get());
            contaRepository.delete(conta.get());
        }
        return contaRepository.findById(id) == null;
    }

    /**
     * busca contas paginadas e filtradas
     * @param contaFilter
     * @param pageable
     * @return Page<Conta>
     */
    public Page<Conta> findAllContas(ContaFilter contaFilter, Pageable pageable){
        var contaSpecification = new ContaEspecification(contaFilter);
        return contaRepository.findAll(contaSpecification, pageable);
    }

    /**
     * busca contas filtradas e ordenadas
     * @param contaFilter
     * @param sort
     * @return List<Contas>
     */
    public List<Conta> findAllContas(ContaFilter contaFilter, Sort sort){
        var contaSpecification = new ContaEspecification(contaFilter);
        return contaRepository.findAll(contaSpecification, sort);
    }

    public List<Conta> findAll(){
        return contaRepository.findAll(Sort.by("vencimento").descending());
    }

    /**
     *  somatoria do campo valor com filtros
     * @param contaFilter
     * @return BigDecimal
     */
    public BigDecimal getValorTotalConta(ContaFilter contaFilter){
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Object> query = cb.createQuery(Object.class);
        final Root<Conta> root = query.from(Conta.class);
        var contaSpecification = new ContaEspecification(contaFilter);
        query.multiselect(cb.sum(root.get("valor")));
        query.where(contaSpecification.toPredicate(root, query, cb));
        Query qry = em.createQuery(query);
        return (BigDecimal) qry.getSingleResult();
    }

    /**
     * consulta contas por tipo id e nome
     * @return List
     */
    public List getContasPorTipo(){
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<Object> query = cb.createQuery(Object.class);
        final Root<Conta> root = query.from(Conta.class);

        query.multiselect(
                root.get("tipoConta").get("id"),
                root.get("tipoConta").get("nome"),
                cb.sum(root.get("valor")));
        query.groupBy(
                root.get("tipoConta").get("id"),
                root.get("tipoConta").get("nome"));

        Query qry = em.createQuery(query);
        return qry.getResultList();
    }

    /**
     * exclui o arquivo do titulo/boleto pela conta
     * @param conta
     * @return Boolean
     */
    public Boolean excluirArquivoTitulo(Conta conta){
        try {
            fileService.excluirArquivo(FilenameUtils.getBaseName(conta.getTitulo())+".zip", ChavePasta.PASTA_TITULO);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * exclui o arquivo do comprovante pela conta
     * @param conta
     * @return Boolean
     */
    public Boolean excluirArquivoComprovante(Conta conta){
        try {
            fileService.excluirArquivo(conta.getComprovante(), ChavePasta.PASTA_COMPROVANTE);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
    /**
     * crio num novo nome padrao dos sistema com a extensao que recebo
     * crio o aquivo compactado sem essa extencao e como zip
     * atualizo o conta com o nome do arquivo do comprovante
     * @param conta
     * @param multipartFile
     */
    public void uploadArquivoComprovante(Conta conta, MultipartFile multipartFile){
        String novoNomeComExtencao = criaNomeArquivoConta(conta,
                FilenameUtils.getExtension(multipartFile.getOriginalFilename()),
                SulFixoTipoArquivo.CO);
        fileService.createFileCompacated(multipartFile, ChavePasta.PASTA_COMPROVANTE, novoNomeComExtencao);

        conta.setComprovante(novoNomeComExtencao);
        salvarConta(conta);
    }
    /**
     * crio num novo nome padrao dos sistema com a extensao que recebo
     * crio o aquivo compactado sem essa extencao e como zip
     * atualizo o conta com o nome do arquivo do titulo/boleto
     * @param conta
     * @param multipartFile
     */
    public void uploadArquivoTitulo(Conta conta, MultipartFile multipartFile){
        String novoNomeComExtencao = criaNomeArquivoConta(conta,
                FilenameUtils.getExtension(multipartFile.getOriginalFilename()),
                SulFixoTipoArquivo.TB);
        fileService.createFileCompacated(multipartFile, ChavePasta.PASTA_TITULO, novoNomeComExtencao);

        conta.setTitulo(novoNomeComExtencao);
        salvarConta(conta);
    }

    /**
     * trago o arquivo de titulo referente a conta
     * @param idConta
     * @return Resource
     */
    public Resource downloadArquivoTitulo(Long idConta){
        Conta conta = findContaPorId(idConta);
        return fileService.getArquivo(conta.getTitulo(), ChavePasta.PASTA_TITULO);
    }

    /**
     * trago o arquivo do comprovante referente a conta
     * @param idConta
     * @return Resource
     */
    public Resource downloadArquivoComprovante(Long idConta){
        Conta conta = findContaPorId(idConta);
        return fileService.getArquivo(conta.getComprovante(), ChavePasta.PASTA_COMPROVANTE);
    }

    /**
     * crio um nome padrao para o arquivo
     * @param conta
     * @return String;
     */
    public String criaNomeArquivoConta(Conta conta,
                                         String extensao,
                                         SulFixoTipoArquivo sulFixoTipoArquvio){
        String novoNome="";
        novoNome = conta.getVencimento().getYear()+"";
        novoNome += "_"+String.format("%02d", conta.getVencimento().getMonth().getValue());
        novoNome += "_"+String.format("%06d", conta.getId());
        novoNome += "_"+String.format("%06d", conta.getTipoConta().getId());
        novoNome += sulFixoTipoArquvio.name();
        if(Objects.nonNull(extensao) && !extensao.trim().isEmpty())
            novoNome += "."+extensao.toLowerCase();

        return novoNome;
    }

    public void uploadCSV(MultipartFile file){
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();
            List<Conta> contas  = new ArrayList<>();

            for (CSVRecord csvRecord : csvRecords) {
                TipoConta tipoConta = new TipoConta().builder()
                        .id(Long.parseLong(csvRecord.get("tipoConta")))
                        .build();
                tipoConta = tipoContaRepository.findById(tipoConta.getId()).get();

                Conta conta = new Conta();
                conta.setTipoConta(tipoConta);
                conta.setCodigoBarra(csvRecord.get("codigobarras"));

                DateTimeFormatter f = new DateTimeFormatterBuilder().parseCaseInsensitive().append(DateTimeFormatter.ofPattern("ddMMyyyy")).toFormatter();
                LocalDate emissao = LocalDate.parse(csvRecord.get("emissao"), f);
                LocalDate vencimento = LocalDate.parse(csvRecord.get("vencimento"), f);

                conta.setEmissao(emissao);
                conta.setVencimento(vencimento);

                conta.setParcela(Integer.parseInt(csvRecord.get("parcela")));
                conta.setTotalParcelas(Integer.parseInt(csvRecord.get("totalParcela")));
                conta.setValor(new BigDecimal(csvRecord.get("valor")));

                if(!csvRecord.get("formaPagamento").isEmpty()){
                    FormaPagamento formaPagamento = new FormaPagamento().builder()
                            .id(Long.parseLong(csvRecord.get("formaPagamento")))
                            .build();
                    formaPagamento = formaPagamentoRepository.getReferenceById(formaPagamento.getId());
                    conta.setFormaPagamento(formaPagamento);
                }

                if(!csvRecord.get("dataPagamento").isEmpty()){
                    LocalDate dataPagmento = LocalDate.parse(csvRecord.get("dataPagamento"), f);
                    conta.setDataPagamento(dataPagmento);
                }

                contas.add(conta);

            }
            for (Conta conta : contas) {
                salvarConta(conta);
            }


        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }



}
