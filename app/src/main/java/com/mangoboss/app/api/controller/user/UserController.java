package com.mangoboss.app.api.controller.user;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangoboss.app.api.facade.user.UserFacade;
import com.mangoboss.app.common.exception.CustomUserDetails;
import com.mangoboss.app.dto.UserInfoResponse;
import com.mangoboss.app.dto.auth.requeset.SignUpRequest;

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
	public void signUp(@RequestBody SignUpRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails) {
		final Long userId = userDetails.getUserId();
		userFacade.signUp(userId, request);
	}
}
