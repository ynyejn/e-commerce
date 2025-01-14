package kr.hhplus.be.server.infra.user;


import kr.hhplus.be.server.domain.user.IUserRepository;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long id) {
        return userJpaRepository.findById(id);
    }

    @Override
    public Optional<User> findByIdWithPoint(Long id) {
        return userJpaRepository.findByIdWithPoint(id);

    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
