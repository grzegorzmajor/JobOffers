package ovh.major.joboffers.domain.loginandregister;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends MongoRepository<User, String> {

    Optional<User> findByName(String name);
}
