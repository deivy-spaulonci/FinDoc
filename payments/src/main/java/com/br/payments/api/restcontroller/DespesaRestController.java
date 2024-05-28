package com.br.payments.api.restcontroller;

import com.br.personaladm.api.config.Messages;
import com.br.personaladm.api.dto.DespesaDTO;
import com.br.personaladm.api.filter.DespesaFilter;
import com.br.personaladm.api.mapper.DespesaMapper;
import com.br.personaladm.business.service.DespesaService;
import com.br.personaladm.domain.model.Despesa;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/despesa")
public class DespesaRestController {

    @Autowired
    private DespesaService despesaService;

//    @Autowired
//    private  ImportacaoDespesaService importacaoDespesaService;
//
//    @Value("classpath:despesas.csv")
//    Resource resource;

    private static final DespesaMapper despesaMapper = DespesaMapper.INSTANCE;

    @GetMapping()
    public List<DespesaDTO> get(@ModelAttribute DespesaFilter despesaFilter, Sort sort){
        return despesaMapper.toDtoList(despesaService.searchByFilterOrder(despesaFilter, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> despesaFindById(@PathVariable("id") Long id){
        Optional<Despesa> despesa = despesaService.findById(id);
        return despesa.<ResponseEntity<Object>>map(value -> ResponseEntity.ok(despesaMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Messages.getMessage("retorno.despesa.vazio")));
    }

    @GetMapping("/page")
    public Page<DespesaDTO> getPage(@ModelAttribute DespesaFilter despesaFilter, Pageable pageable){
        return despesaService.getPageByFilters(despesaFilter, pageable).map(despesaMapper::toDTO);
    }

    @GetMapping("/valorTotal")
    public BigDecimal get(@ModelAttribute DespesaFilter despesaFilter){
        return despesaService.getValorTotalDespesa(despesaFilter);
    }

    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DespesaDTO> create(@RequestBody @Valid DespesaDTO despesaDTO,
                                           UriComponentsBuilder uriBuilder){
        Despesa despesa = despesaService.save(despesaMapper.toModel(despesaDTO));
        URI uri = uriBuilder.path("/{id}").buildAndExpand(despesaDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(despesaMapper.toDTO(despesa));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid DespesaDTO despesaDTO){
        Optional<Despesa> despesaOptional = despesaService.findById(despesaDTO.getId());
        if(despesaOptional.isPresent()){
            Despesa despesa = despesaService.update(despesaMapper.toModel(despesaDTO));
            return ResponseEntity.ok(despesaMapper.toDTO(despesa));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Messages.getMessage("retorno.despesa.vazio"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Despesa> despesa = despesaService.findById(id);
        if(despesa.isPresent()){
            despesaService.deleteById(despesa.get().getId());
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Messages.getMessage("retorno.despesa.vazio"));
    }

//    @GetMapping("/despesaPorTipo")
//    public ResponseEntity<List<DespesaPorTipoDTO>> getDespesaPorTipo(){
//        List<DespesaPorTipoDTO> lista = new ArrayList<>();
//
//        List resultado = despesaService.getDespesaPorTipo();
//
//        if(resultado.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        for(Object item : resultado){
//            DespesaPorTipoDTO despesaPorTipoDTO = new DespesaPorTipoDTO();
//            var subitem = (Object[]) item;
//            TipoDespesa tipoDespesa = TipoDespesa.forValues(subitem[0].toString());
//            despesaPorTipoDTO.setTipoDespesa(tipoDespesa);
//            despesaPorTipoDTO.setValor(new BigDecimal(subitem[1].toString()));
//            lista.add(despesaPorTipoDTO);
//        }
//
//        return ResponseEntity.ok(lista);
//    }
//    @GetMapping(value = "/validacaoImportarcaoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> validacaoImportacaoDespesa(){
//        List<String> lista = importacaoDespesaService.validacaoImportacaoDespesa();
//        Gson gson = new Gson();
//        if(Objects.isNull(lista) || lista.isEmpty())
//            return ResponseEntity.ok(gson.toJson("Validação da importação de despesas ok!"));
//        else{
//            String jsonArray = gson.toJson(lista);
//            return ResponseEntity.ok(jsonArray);
//        }
//    }
//    @GetMapping(value = "/salvarImportarcaoDespesa", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<?> salvarImportacaoDespesa(){
//        int salvos = importacaoDespesaService.importarDespesas();
//        Gson gson = new Gson();
//        return ResponseEntity.ok(gson.toJson("Quantidade de despesas salvas - " + salvos));
//    }
//
//    @GetMapping("/importarDespesa")
//    public ResponseEntity<?> importDespesaFromFile(){
//
//        String resposta = null;
//        try {
//            resposta = despesaService.importFromFile(resource.getFile());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.ok(resposta);
//    }
}
