package victor.encurtadourl.app.repositores;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import victor.encurtadourl.app.models.EncurtadorURLModel;

import javax.print.DocFlavor;

@Repository
public interface EncurtadorURLRepository extends MongoRepository<EncurtadorURLModel, String> {
}
