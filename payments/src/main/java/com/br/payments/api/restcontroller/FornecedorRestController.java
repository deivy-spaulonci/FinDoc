package com.br.payments.api.restcontroller;

import com.br.payments.api.dto.FornecedorDTO;
import com.br.payments.api.mapper.FornecedorMapper;
import com.br.payments.bussiness.services.FornecedorService;
import com.br.payments.domain.model.Fornecedor;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/fornecedor")
public class FornecedorRestController {

    @Autowired
    private FornecedorService fornecedorService;

    private static final FornecedorMapper fornecedorMapper = FornecedorMapper.INSTANCE;

    @GetMapping()
    public List<FornecedorDTO> get(){
        return fornecedorMapper.toDtoList(fornecedorService.findAll());
    }
    @GetMapping("/page")
    public Page<FornecedorDTO> getPage(Pageable pageable){
        return fornecedorService.findAll(pageable).map(fornecedorMapper::toDTO);
    }
    @GetMapping("/busca")
    public List<FornecedorDTO> get(@PathParam("textobusca") String textobusca){
        return fornecedorMapper.toDtoList(fornecedorService.listFornecedorByNomeOrRazaoOrCNPJ(textobusca));
    }
    @GetMapping("/busca/page")
    public Page<FornecedorDTO> getPage(@PathParam("textobusca") String textobusca, Pageable pageable){
        return fornecedorService.getPageByNomeOrRazaoOrCNPJ(textobusca, pageable).map(fornecedorMapper::toDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> contaFindById(@PathVariable("id") Long id){
        Optional<Fornecedor> fornecedor = fornecedorService.findById(id);
        return fornecedor.<ResponseEntity<Object>>map(value -> ResponseEntity.ok(fornecedorMapper.toDTO(value)))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Nenhum fornecedor econtrado!"));
    }
    @PostMapping
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FornecedorDTO> create(@RequestBody @Valid FornecedorDTO fornecedorDTO,
                                           UriComponentsBuilder uriBuilder){
        Fornecedor fornecedor = fornecedorService.save(fornecedorMapper.toModel(fornecedorDTO));
        URI uri = uriBuilder.path("/{id}").buildAndExpand(fornecedorDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(fornecedorMapper.toDTO(fornecedor));
    }
    @PutMapping
    @Transactional
    public ResponseEntity<?> update(@RequestBody @Valid FornecedorDTO fornecedorDTO){
        Optional<Fornecedor> fornecedorOptional = fornecedorService.findById(fornecedorDTO.getId());
        if(fornecedorOptional.isPresent()){
            Fornecedor fornecedor = fornecedorService.update(fornecedorMapper.toModel(fornecedorDTO));
            return ResponseEntity.ok(fornecedorMapper.toDTO(fornecedor));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Nenhum fornecedor econtrado!");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable Long id){
        return ResponseEntity.ok("Não ha permissao de exclusao do fornecedor!");
    }

    @GetMapping(value = "/consultacnpj", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<String> consultaCNPJ(@RequestParam(name = "cnpj") String cnpj){

        Optional<Fornecedor> fornecedorbusca = fornecedorService.findFornecedorByCNPJ(cnpj);
        if(fornecedorbusca.isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("CNPJ já cadastrado!");
        }else{
            String URL_API = "https://www.receitaws.com.br/v1/cnpj/"+cnpj;
            HttpURLConnection con = null;

            try {
                URL url = null;
                url = new URL(URL_API);
                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.connect();
                return ResponseEntity.ok(getJson(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ResponseEntity.status(HttpStatus.CONFLICT).body("conflito na requisição da receita!");
    }

    public static String getJson(URL url) {
        if (url == null)
            throw new RuntimeException("URL é null");

        String html = null;
        StringBuilder sB = new StringBuilder();
        try (BufferedReader bR = new BufferedReader(new InputStreamReader(url.openStream()))) {
            while ((html = bR.readLine()) != null)
                sB.append(html);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sB.toString();
    }
}
