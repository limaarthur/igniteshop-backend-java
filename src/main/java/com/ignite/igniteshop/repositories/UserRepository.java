package com.ignite.igniteshop.repositories;

import com.ignite.igniteshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
