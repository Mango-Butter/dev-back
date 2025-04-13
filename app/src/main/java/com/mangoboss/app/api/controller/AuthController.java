package com.mangoboss.app.api.controller;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.AuthFacade;
import com.mangoboss.app.dto.LoginRequest;
import com.mangoboss.app.dto.LoginResponse;
import com.mangoboss.app.dto.ReissueAccessTokenRequest;
import com.mangoboss.app.dto.TokenReissueResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
	private final AuthFacade authFacade;

	@PatchMapping("/reissue-token")
	public TokenReissueResponse reissueAccessToken(@RequestBody ReissueAccessTokenRequest reissueAccessTokenRequest) {
		return authFacade.reissueAccessToken(reissueAccessTokenRequest);
	}

	@PostMapping("/login/kakao")
	public LoginResponse socialLogin(@RequestBody LoginRequest loginRequest) {
		return authFacade.socialLogin(loginRequest);
	}
}
