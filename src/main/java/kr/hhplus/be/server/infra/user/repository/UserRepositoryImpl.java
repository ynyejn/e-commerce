package kr.hhplus.be.server.infra.user.repository;


import kr.hhplus.be.server.domain.user.entity.User;
import kr.hhplus.be.server.domain.user.repository.IUserRepository;
import kr.hhplus.be.server.infra.user.jpa.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(Long aLong) {
        return userJpaRepository.findById(aLong);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user);
    }
}
