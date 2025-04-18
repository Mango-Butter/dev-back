package com.mangoboss.app.api.controller.auth;

import com.mangoboss.app.dto.auth.response.JwtResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.auth.AuthFacade;
import com.mangoboss.app.dto.auth.requeset.LoginRequest;
import com.mangoboss.app.dto.auth.requeset.RefreshTokenRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthFacade authFacade;

    @PostMapping("/reissue-token")
    public JwtResponse reissueAccessToken(@RequestBody RefreshTokenRequest reissueAccessTokenRequest) {
        return authFacade.reissueAccessToken(reissueAccessTokenRequest);
    }

    @PostMapping("/login/kakao")
    public JwtResponse socialLogin(@RequestBody LoginRequest loginRequest) {
        return authFacade.socialLogin(loginRequest);
    }
}
