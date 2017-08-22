package com.hason.repository;

import com.hason.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Hason on 2017/8/22.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
