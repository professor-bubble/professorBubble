package com.bubble.buubleforprofessor.aspect;

import com.bubble.buubleforprofessor.global.config.CustomException;
import com.bubble.buubleforprofessor.global.config.ErrorCode;
import com.bubble.buubleforprofessor.user.dto.CustomUserDetails;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Aspect
@Component
public class UserControllerAspect {

    @Pointcut("execution(* com.bubble.buubleforprofessor.user.controller.UserController.*(..))")
    public void userControllerMethods() {

    }

    @Before(value = "userControllerMethods() && args(userId, ..)")
    public void validateUser(UUID userId)
    {
        CustomUserDetails userDetails= (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(!userId.toString().equals(userDetails.getUserId()))
        {
            throw new CustomException(ErrorCode.USER_MISMATCH);
        }
    }
}

