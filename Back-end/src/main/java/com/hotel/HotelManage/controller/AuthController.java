package com.hotel.HotelManage.controller;

import com.hotel.HotelManage.dto.LoginRequest;
import com.hotel.HotelManage.dto.Response;
import com.hotel.HotelManage.entity.User;
import com.hotel.HotelManage.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService UserService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody User user) {
        Response response = UserService.register(user);
        return ResponseEntity.status(response.getStatusCode())
                             .body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
        Response response = UserService.login(loginRequest);
        return ResponseEntity.status(response.getStatusCode())
                .body(response);

    }
}
