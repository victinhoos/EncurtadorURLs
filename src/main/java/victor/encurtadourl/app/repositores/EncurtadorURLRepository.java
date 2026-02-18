package victor.encurtadourl.app.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import victor.encurtadourl.app.models.EncurtadorURLModel;

@Repository
public interface EncurtadorURLRepository extends MongoRepository<EncurtadorURLModel, String> {
    EncurtadorURLModel findByEncurtador(String encurtador);
}