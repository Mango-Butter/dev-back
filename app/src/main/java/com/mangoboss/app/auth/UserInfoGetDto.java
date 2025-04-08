package com.mangoboss.app.auth;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoGetDto {
    private String id;
    private Long kakaoId;
    private String email;
    @JsonProperty("verified_email")
    private boolean verifiedEmail;
    private String name;
    @JsonProperty("given_name")
    private String givenName;
    @JsonProperty("family_name")
    private String familyName;
    private LocalDate birthday;
    private String picture;
    private String locale;
    private String hd;
    private String phone;
}
