package kr.hhplus.be.server.domain.user.repository;


import kr.hhplus.be.server.domain.user.entity.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> findById(Long aLong);

    User save(User user);
}
