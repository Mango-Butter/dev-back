package com.mangoboss.app.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<TokenReissueResponse> reissueAccessToken(@RequestBody ReissueAccessTokenRequest reissueAccessTokenRequest) {
		TokenReissueResponse tokenReissueResponse = authFacade.reissueAccessToken(reissueAccessTokenRequest);
		return ResponseEntity.ok(tokenReissueResponse);
	}

	@PostMapping("/login/kakao")
	public ResponseEntity<LoginResponse> socialLogin(@RequestBody LoginRequest loginRequest) {
		LoginResponse loginResponse = authFacade.socialLogin(loginRequest);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
	}
}
