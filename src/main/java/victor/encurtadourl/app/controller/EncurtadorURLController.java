package victor.encurtadourl.app.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import victor.encurtadourl.app.dtos.EncurtadorURLDTOs;
import victor.encurtadourl.app.models.EncurtadorURLModel;
import victor.encurtadourl.app.repositores.EncurtadorURLRepository;

import java.time.LocalDate;

@RestController
public class EncurtadorURLController {

    @Autowired
    private EncurtadorURLRepository repository;

    @PostMapping("/url")
    public ResponseEntity<EncurtadorURLModel> saveEncurtadorURL(@RequestBody @Valid EncurtadorURLDTOs dto) {

        var encurtador = new EncurtadorURLModel();
        BeanUtils.copyProperties(dto, encurtador);
        encurtador.setData(LocalDate.now());
        EncurtadorURLModel urlSalva = repository.save(encurtador);

        return ResponseEntity.status(HttpStatus.CREATED).body(urlSalva);
    }
}