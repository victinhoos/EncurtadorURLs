package victor.encurtadourl.app.services;

import org.hashids.Hashids;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import victor.encurtadourl.app.models.EncurtadorURLModel;
import victor.encurtadourl.app.repositores.EncurtadorURLRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class EncurtadorURLService {

    @Autowired
    private EncurtadorURLRepository repository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String CHAVE_RANKING = "top100_urls";
    private static final String PREFIXO_HLL_VISITANTES = "visitantes_unicos:";

    private final Hashids hashids;

    public EncurtadorURLService() {
        this.hashids = new Hashids(
                "minha-chave-secreta-super-segura",
                5,
                "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
        );
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


    public List<EncurtadorURLModel> listarTodasUrls() {
        return repository.findAll();
    }

    public void registrarCliqueNoRanking(String encurtador) {
        redisTemplate.opsForZSet().incrementScore(CHAVE_RANKING, encurtador, 1);
    }

    public Set<ZSetOperations.TypedTuple<String>> obterTop100() {
        return redisTemplate.opsForZSet().reverseRangeWithScores(CHAVE_RANKING, 0, 99);
    }

    public void registrarVisitanteUnico(String encurtador, String ipUsuario) {
        String chaveRedis = PREFIXO_HLL_VISITANTES + encurtador;
        redisTemplate.opsForHyperLogLog().add(chaveRedis, ipUsuario);
    }

    public long obterTotalVisitantesUnicos(String encurtador) {
        String chaveRedis = PREFIXO_HLL_VISITANTES + encurtador;
        Long total = redisTemplate.opsForHyperLogLog().size(chaveRedis);
        return total != null ? total : 0L;
    }
}