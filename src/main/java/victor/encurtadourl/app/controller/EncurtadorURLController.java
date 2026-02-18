package victor.encurtadourl.app.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import victor.encurtadourl.app.dtos.EncurtadorURLDTOs;
import victor.encurtadourl.app.models.EncurtadorURLModel;
import victor.encurtadourl.app.services.EncurtadorURLService;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
public class EncurtadorURLController {

    @Autowired
    private EncurtadorURLService service;

    @PostMapping("/encurtar")
    public ResponseEntity<EncurtadorURLModel> encurtarUrl(@Valid @RequestBody EncurtadorURLDTOs dto) {
        EncurtadorURLModel urlCriada = service.criarUrlEncurtada(dto.url());
        return ResponseEntity.status(HttpStatus.CREATED).body(urlCriada);
    }


    @GetMapping("/urls")
    public ResponseEntity<List<EncurtadorURLModel>> getAllUrls() {
        List<EncurtadorURLModel> listaDeUrls = service.listarTodasUrls();
        return ResponseEntity.ok(listaDeUrls);
    }

    @GetMapping("/{codigoCurto}")
    public ResponseEntity<Void> redirecionarParaOriginal(
            @PathVariable String codigoCurto,
            HttpServletRequest request) {

        EncurtadorURLModel urlModel = service.buscarUrlOriginal(codigoCurto);

        if (urlModel == null) {
            return ResponseEntity.notFound().build();
        }

        String ipUsuario = request.getHeader("X-Forwarded-For");
        if (ipUsuario == null || ipUsuario.isEmpty()) {
            ipUsuario = request.getRemoteAddr();
        }

        service.registrarCliqueNoRanking(codigoCurto);
        service.registrarVisitanteUnico(codigoCurto, ipUsuario);

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(urlModel.getUrl()))
                .build();
    }

    @GetMapping("/ranking")
    public ResponseEntity<Set<ZSetOperations.TypedTuple<String>>> verRankingTop100() {
        return ResponseEntity.ok(service.obterTop100());
    }

    @GetMapping("/{codigoCurto}/estatisticas")
    public ResponseEntity<Map<String, Object>> verEstatisticasLink(@PathVariable String codigoCurto) {

        long visitantesUnicos = service.obterTotalVisitantesUnicos(codigoCurto);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("encurtador", codigoCurto);
        resposta.put("visitantes_unicos", visitantesUnicos);

        return ResponseEntity.ok(resposta);
    }
}