package com.mangoboss.app.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.UserFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.UserInfoResponse;

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
}
