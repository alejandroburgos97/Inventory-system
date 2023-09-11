package com.nexossoftware.inventorysystem.repository;

import com.nexossoftware.inventorysystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByName(String userEntered);

}
