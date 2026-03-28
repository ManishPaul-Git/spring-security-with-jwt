package com.manish.securitysample.controller;



import com.manish.securitysample.entity.AuthRequest;
import com.manish.securitysample.entity.UserInfo;
import com.manish.securitysample.service.JwtService;
import com.manish.securitysample.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserInfoService service;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }



    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }



    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getName(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getName());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    // role based APIs - only accessible to users with ROLE_USER authority
    @GetMapping("/user/userProfile")
    public String getUserProfile() {
        return service.getUserProfile();
    }

    // role based APIs - only accessible to users with ROLE_ADMIN authority
    @GetMapping("/userTest")
    public String getUserTest() {
        return "successfully accessed userTest endpoint - only for ROLE_ADMIN";
    }

    // role based APIs - only accessible to users with ROLE_ADMIN authority
    @GetMapping("/admin/userDetail")
    public String getUserDetail() {
        return service.getUserDetail();
    }
}
