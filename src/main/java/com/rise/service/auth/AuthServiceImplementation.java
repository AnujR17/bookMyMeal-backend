package com.rise.service.auth;

import com.MEALPROJECT.MEAL_PROJECT1.dtos.SignupRequest;
import com.MEALPROJECT.MEAL_PROJECT1.dtos.UserDto;
import com.MEALPROJECT.MEAL_PROJECT1.entities.User;
import com.MEALPROJECT.MEAL_PROJECT1.enums.UserRole;
import com.MEALPROJECT.MEAL_PROJECT1.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImplementation implements AuthService {


    private final UserRepository userRepository;




    public AuthServiceImplementation(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @PostConstruct
    public void createRegisterAccount(){
        User RegisterAccount = userRepository.findByUserRole(UserRole.ADMIN);
        if(RegisterAccount ==null )
        {
            User user = new User();
            user.setName("Devanshi");
            user.setEmail("Devanshi@gmail.com");
            user.setPassword(new BCryptPasswordEncoder().encode("123"));
            user.setUserRole(UserRole.ADMIN);
            userRepository.save(user);


        }

    }

    @Override
    public UserDto createUser(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER);
        User createdUser = userRepository.save(user);
        UserDto createdUserDto = new UserDto();
        createdUserDto.setId(createdUser.getId());
        createdUserDto.setName(createdUser.getName());
        createdUserDto.setEmail(createdUser.getEmail());
        createdUserDto.setUserRole(createdUser.getUserRole());
        return createdUserDto;
    }



}
