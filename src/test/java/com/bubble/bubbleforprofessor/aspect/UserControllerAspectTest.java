package com.bubble.bubbleforprofessor.aspect;

import com.bubble.bubbleforprofessor.global.config.CustomException;
import com.bubble.bubbleforprofessor.global.config.ErrorCode;
import com.bubble.bubbleforprofessor.user.dto.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserControllerAspectTest {

    private UserControllerAspect userControllerAspect;

    @BeforeEach
    void setUp() {
        userControllerAspect = new UserControllerAspect();
    }

    @Test
    void testValidateUser_ValidUserId() {
        // Mock CustomUserDetails
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(UUID.randomUUID().toString());

        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Set SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Call the method to test
        UUID userId = UUID.fromString(userDetails.getUserId());
        userControllerAspect.validateUser(userId);

        // Verify interactions
        verify(securityContext).getAuthentication();
        verify(authentication).getPrincipal();
    }
    @Test
    void testValidateUser_InValidUserId() {
        // Mock CustomUserDetails
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(UUID.randomUUID().toString());

        // Mock Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        // Mock SecurityContext
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Set SecurityContextHolder
        SecurityContextHolder.setContext(securityContext);

        // Call the method to test
        UUID invalidUserId = UUID.randomUUID();

        CustomException thrown = assertThrows(CustomException.class, () -> {
            userControllerAspect.validateUser(invalidUserId);
        });

        // Optionally, assert that the exception's error code is as expected
        assertEquals(ErrorCode.USER_MISMATCH, thrown.getErrorCode());
    }
}
