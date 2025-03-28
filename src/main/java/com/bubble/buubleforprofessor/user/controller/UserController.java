package com.bubble.buubleforprofessor.user.controller;

import com.bubble.buubleforprofessor.user.dto.JoinRequestDto;
import com.bubble.buubleforprofessor.user.dto.LoginRequestDto;
import com.bubble.buubleforprofessor.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/user")
    public String mainPage(@AuthenticationPrincipal UserDetails userDetails) {


        return "user controller - " + userDetails.getUsername() + " - " + userDetails.getPassword() + " - " + userDetails.getAuthorities();
    }

    @PostMapping("/join")
    public String join(JoinRequestDto joinRequestDto) {

        return userService.createUser(joinRequestDto);
    }
}
