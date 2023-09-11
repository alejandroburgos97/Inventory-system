package com.nexossoftware.inventorysystem.service.impl;

import com.nexossoftware.inventorysystem.entity.User;
import com.nexossoftware.inventorysystem.payload.ProductDto;
import com.nexossoftware.inventorysystem.repository.UserRepository;
import com.nexossoftware.inventorysystem.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
