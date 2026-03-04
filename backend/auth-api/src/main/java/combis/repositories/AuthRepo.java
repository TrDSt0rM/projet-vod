package combis.repositories;

import combis.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepo extends JpaRepository<User, Long> {
    Optional<User> findByPseudo(String pseudo);
    boolean existsByPseudo(String pseudo);
}
