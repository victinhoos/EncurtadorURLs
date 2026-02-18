package victor.encurtadourl.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.encurtadourl.app.dtos.EncurtadorURLDTOs;
import victor.encurtadourl.app.models.EncurtadorURLModel;
import victor.encurtadourl.app.services.EncurtadorURLService;

import java.net.URI;
import java.util.List;

@RestController
public class EncurtadorURLController {

    @Autowired
    private EncurtadorURLService service;

    @PostMapping("/encurtar")
    public ResponseEntity<EncurtadorURLModel> encurtarUrl(@RequestBody EncurtadorURLDTOs dto) {

        EncurtadorURLModel urlCriada = service.criarUrlEncurtada(dto.url());

        return ResponseEntity.status(HttpStatus.CREATED).body(urlCriada);
    }

    @GetMapping("/urls")
    public ResponseEntity<List<EncurtadorURLModel>> getAllUrls() {

        List<EncurtadorURLModel> listaDeUrls = service.listarTodasUrls();
        return ResponseEntity.ok(listaDeUrls);
    }

    @GetMapping("/{codigoCurto}")
    public ResponseEntity<Void> redirecionarParaOriginal(@PathVariable String codigoCurto) {

        EncurtadorURLModel urlModel = service.buscarUrlOriginal(codigoCurto);

        if (urlModel == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlModel.getUrl()))
                .build();
    }
}
