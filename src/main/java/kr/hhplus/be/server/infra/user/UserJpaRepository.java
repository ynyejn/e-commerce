package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths="point")
    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> findByIdWithPoint(@Param("id") Long id);
}
