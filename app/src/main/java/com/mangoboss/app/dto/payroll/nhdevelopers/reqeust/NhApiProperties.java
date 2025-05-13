package com.mangoboss.app.dto.payroll.nhdevelopers.reqeust;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "external.nh")
@Component
@Getter
@Setter
public class NhApiProperties {
    private String iscd;
    private String fintechApsno;
    private String apiSvcCd;
    private String accessToken;
}

