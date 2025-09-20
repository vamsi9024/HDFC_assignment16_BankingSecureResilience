package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserService userService;
    private final Logger log = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String,String> request){
        userService.registerUser(request.get("username"),request.get("password"));
        log.info("User registered: {}", request.get("username"));
        return new ResponseEntity<>("New User Registered...",HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String,String> request){
        String token=userService.login(request.get("username"),request.get("password"));
        log.info("Login success: {}",request.get("username"));
        return new ResponseEntity<>(token,HttpStatus.OK);
    }
}
