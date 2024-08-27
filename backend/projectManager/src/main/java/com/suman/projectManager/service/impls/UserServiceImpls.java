package com.suman.projectManager.service.impls;

import com.suman.projectManager.config.jwt.JwtTokenUtils;
import com.suman.projectManager.entity.User;
import com.suman.projectManager.exception.EntityNotFoundException;
import com.suman.projectManager.repository.UserRepository;
import com.suman.projectManager.service.interfac.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpls implements UserService {

    private final UserRepository userRepository;
    @Override
    public User findByUserId(Long userId) {
      Optional<User> user=  userRepository.findById(userId);
        if (user.isEmpty()){
            throw new EntityNotFoundException("No user found");
        }
        return user.get();
    }

    @Override
    public User findByEmail(String email) {

        System.out.println("Starting to find user");
        Optional<User> user=userRepository.findByEmailId(email);
        if (user.isEmpty()){
            System.out.println("No user");
            throw new EntityNotFoundException("No user found");
        }
        return user.get();
    }

    @Override
    public User updateUserProjectSize(User user, int number) {
        user.setProjectSize(user.getProjectSize()+number);
        return userRepository.save(user);
    }
}
