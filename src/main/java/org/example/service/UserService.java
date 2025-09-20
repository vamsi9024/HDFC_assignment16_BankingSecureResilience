package org.example.service;

import org.example.model.Account;
import org.example.model.User;
import org.example.config.JwtUtil;
import org.example.repository.AccountRepository;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final double INITIAL_BALANCE = 5000;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public void registerUser(String username,String password){
        userRepository.save(new User(null,username,passwordEncoder.encode(password)));
        accountRepository.save(new Account(null,username,INITIAL_BALANCE));
    }

    public String login(String username, String password) {
        User user=userRepository.findByUsername(username)
                .orElseThrow(()->new RuntimeException("No User Found with username "+username));
        if(passwordEncoder.matches(password,user.getPassword())){
            return jwtUtil.generateToken(username);
        }
        else{
            throw new RuntimeException("Invalid Credentails");
        }
    }
}
