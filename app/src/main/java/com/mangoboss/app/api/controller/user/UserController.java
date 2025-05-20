package com.mangoboss.app.api.controller.user;

import com.mangoboss.app.dto.auth.response.JwtResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.user.UserFacade;
import com.mangoboss.app.common.security.CustomUserDetails;
import com.mangoboss.app.dto.user.response.UserInfoResponse;
import com.mangoboss.app.dto.auth.requeset.SignUpRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserFacade userFacade;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me")
    public UserInfoResponse getUserInfo(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userFacade.getUserInfo(userDetails);
    }

    @PreAuthorize("hasRole('UNASSIGNED')")
    @PostMapping("/sign-up")
    public JwtResponse signUp(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @RequestBody @Valid SignUpRequest request) {
        final Long userId = userDetails.getUserId();
        return userFacade.signUp(userId, request);
    }
}
