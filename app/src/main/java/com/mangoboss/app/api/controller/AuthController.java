package com.mangoboss.app.api.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;

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
import com.mangoboss.app.dto.ReissueTokenDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
	private final AuthFacade authFacade;

	@PatchMapping("/reissue-token")
	public ResponseEntity<LoginResponse> reissueAccessToken(@RequestBody ReissueTokenDto refreshToken) {
		LoginResponse token = authFacade.reissueAccessToken(refreshToken);
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

	@PostMapping("/login/kakao")
	public ResponseEntity<LoginResponse> getAccessToken(@RequestBody LoginRequest loginRequest) throws GeneralSecurityException, IOException {
		LoginResponse loginResponse = authFacade.socialLogin(loginRequest);
		return ResponseEntity.status(HttpStatus.OK).body(loginResponse);
	}
}
