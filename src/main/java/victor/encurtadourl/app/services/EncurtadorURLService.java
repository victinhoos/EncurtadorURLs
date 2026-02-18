package victor.encurtadourl.app.services;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import victor.encurtadourl.app.controller.EncurtadorURLController;
import victor.encurtadourl.app.models.EncurtadorURLModel;
import victor.encurtadourl.app.repositores.EncurtadorURLRepository;

import java.time.LocalDate; // <-- CORRIGIDO PARA LOCALDATE
import java.util.List;

@Service
public class EncurtadorURLService {

    @Autowired
    private EncurtadorURLRepository repository;

    private final Hashids hashids;

    public EncurtadorURLService() {
        this.hashids = new Hashids(
                "minha-chave-secreta-super-segura",
                5,
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        );
    }


    public List<EncurtadorURLModel> listarTodasUrls(){
        return repository.findAll();
    }

    public EncurtadorURLModel criarUrlEncurtada(String url) {

        long idUnico = System.currentTimeMillis();
        String encurtadorGerado = hashids.encode(idUnico);

        EncurtadorURLModel novaUrl = new EncurtadorURLModel();
        novaUrl.setUrl(url);
        novaUrl.setEncurtador(encurtadorGerado);
        novaUrl.setData(LocalDate.now()); 

        return repository.save(novaUrl);
    }

    public EncurtadorURLModel buscarUrlOriginal(String encurtador) {
        return repository.findByEncurtador(encurtador);
    }
}