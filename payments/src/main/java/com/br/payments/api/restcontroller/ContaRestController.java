package com.br.payments.api.restcontroller;


import com.br.personaladm.api.dto.ContaDTO;
import com.br.personaladm.api.filter.ContaFilter;
import com.br.personaladm.api.mapper.ContaMapper;
import com.br.personaladm.business.service.ContaService;
import com.br.personaladm.business.service.TipoContaService;
import com.br.personaladm.business.service.generic.GenericServiceTipo;
import com.br.personaladm.domain.model.Conta;
import com.br.personaladm.domain.model.tipo.FormaPagamento;
import com.br.personaladm.domain.model.tipo.TipoConta;
import com.br.personaladm.domain.repository.ParametroRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

//@Tag(name = "Contas", description = "contas APIs")
@RestController
@RequestMapping("/api/v1/conta")
public class ContaRestController {

    @Autowired
    private ContaService contaService;

    @Autowired
    private ParametroRepository parametroRepository;

    private static final String MSG_CONTA_NOT_FOUND = "Conta não econtrada!";

    private static final ContaMapper contaMapper = ContaMapper.INSTANCE;
    @Autowired
    private TipoContaService tipoContaService;

    /**
     * listagem de contas por filtragem e tipo de ordenacao
     * @param contaFilter
     * @param sort
     * @return List<ContaDTO>
     */
    @Operation(
            summary = "Retorna lista de contas por filtro e ordenada",
            description = "Retorna lista de contas por objeto de filtro pre-definido, sem paginação e ordenada",
            tags = { "contas", "get" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Conta.class), mediaType = "application/json") }),
        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
    })
    @GetMapping()
    public List<ContaDTO> get(@ModelAttribute ContaFilter contaFilter, Sort sort){
        return contaMapper.toDtoList(contaService.findAllContas(contaFilter, sort));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> contaFindById(@PathVariable("id") Long id){
        Optional<Conta> conta = Optional.of(contaService.findContaPorId(id));
        return conta.<ResponseEntity<Object>>map(value -> ok(contaMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MSG_CONTA_NOT_FOUND));
    }

    @GetMapping("/page")
    public Page<ContaDTO> getPage(@ModelAttribute ContaFilter contaFilter, Pageable pageable){
        return contaService.findAllContas(contaFilter, pageable).map(contaMapper::toDTO);
    }

    @GetMapping("/valorTotal")
    public BigDecimal get(@ModelAttribute ContaFilter contaFilter){
        return contaService.getValorTotalConta(contaFilter);
    }
    @GetMapping("/downloadComprovante/{idConta}")
    public ResponseEntity<?> downloadComprovante(@PathVariable("idConta") Long idConta) {
        Resource resource = contaService.downloadArquivoComprovante(idConta);
        if (resource == null)
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
    @GetMapping("/downloadTituloBoleto/{idConta}")
    public ResponseEntity<?> downloadTitulo(@PathVariable("idConta") Long idConta) {
        Resource resource = contaService.downloadArquivoTitulo(idConta);
        if (resource == null)
            return new ResponseEntity<>("File not found", HttpStatus.NOT_FOUND);

        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() + "\"";

        return ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);
    }
    @PostMapping("/uploadTituloBoleto/{idConta}")
    public ResponseEntity<?> uploadTituloBoleto(@RequestParam("file") MultipartFile file,
                                          @PathVariable("idConta") String idConta){
        Optional<Conta> conta = Optional.of(contaService.findContaPorId(Long.parseLong(idConta)));
        if(conta.isPresent()){
            contaService.uploadArquivoComprovante(conta.get(),file);
            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MSG_CONTA_NOT_FOUND);
    }
    @PostMapping("/uploadComprovante/{idConta}")
    public ResponseEntity<?> uploadComprovante(@RequestParam("file") MultipartFile file,
                                          @PathVariable("idConta") String idConta){
        Optional<Conta> conta = Optional.of(contaService.findContaPorId(Long.parseLong(idConta)));
        if(conta.isPresent()){
            contaService.uploadArquivoComprovante(conta.get(),file);
            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MSG_CONTA_NOT_FOUND);
    }
    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody ContaDTO contaDTO,
                                    UriComponentsBuilder uriBuilder){
        Conta conta = contaService.salvarConta(contaMapper.toModel(contaDTO));
        URI uri = uriBuilder.path("/{id}").buildAndExpand(contaDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(contaMapper.toDTO(conta));
    }

    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid ContaDTO contaDTO){
        Optional<Conta> contaOptional =  Optional.of(contaService.findContaPorId(contaDTO.getId()));
        if(contaOptional.isPresent()){
            Conta conta = contaService.salvarConta(contaMapper.toModel(contaDTO));
            return ok(contaMapper.toDTO(conta));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(MSG_CONTA_NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        Optional<Conta> conta =  Optional.of(contaService.findContaPorId(id));
        if(conta.isPresent()){
            this.contaService.deleteContaPorId(conta.get().getId());
            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MSG_CONTA_NOT_FOUND);
    }
    @GetMapping("/deleteFileTitulo/{idConta}")
    public ResponseEntity<?> excluirArquivoTipo(@PathVariable("idConta") Long idConta){
        Optional<Conta> conta =  Optional.of(contaService.findContaPorId(idConta));
        if(conta.isPresent()){
            contaService.excluirArquivoTitulo(conta.get());
            conta.get().setTitulo(null);
            contaService.salvarConta(conta.get());
            return ok().build();
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Erro arquivo de titulo não foi excluído!");
    }

    @PostMapping("/uploadCSV")
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> uploadCSV(@RequestParam("file") MultipartFile file){

        contaService.uploadCSV(file);
        return ok().build();

    }

//    @GetMapping("/contaPorTipo")
//    public ResponseEntity<List<ContaPorTipoDTO>> getContaPorTipo(){
//        List<ContaPorTipoDTO> lista = new ArrayList<>();
//
//        List resultado = contaService.getContasPorTipo();
//
//        if(resultado.isEmpty()){
//            return ResponseEntity.notFound().build();
//        }
//        for(Object item : resultado){
//            ContaPorTipoDTO contaPorTipoDTO = new ContaPorTipoDTO();
//            var subitem = (Object[]) item;
//            TipoConta tipoConta = new TipoConta();
//            tipoConta.setId(Long.parseLong(subitem[0].toString()));
//            tipoConta.setNome(subitem[1].toString());
//            contaPorTipoDTO.setTipoConta(tipoConta);
//            contaPorTipoDTO.setValor(new BigDecimal(subitem[2].toString()));
//
//            lista.add(contaPorTipoDTO);
//        }
//
//        return ResponseEntity.ok(lista);
//    }
}
