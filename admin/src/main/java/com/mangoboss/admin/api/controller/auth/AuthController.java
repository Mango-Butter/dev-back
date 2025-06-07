package com.mangoboss.admin.api.controller.auth;

import com.mangoboss.admin.api.facade.auth.AuthFacade;
import com.mangoboss.admin.dto.auth.requeset.LoginRequest;
import com.mangoboss.admin.dto.auth.requeset.RefreshTokenRequest;
import com.mangoboss.admin.dto.auth.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
