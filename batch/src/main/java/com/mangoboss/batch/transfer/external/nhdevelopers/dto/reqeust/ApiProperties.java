package com.mangoboss.batch.transfer.external.nhdevelopers.dto.reqeust;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "external.nh")
@Component
@Getter
@Setter
public class ApiProperties {
    private String iscd;
    private String fintechApsno;
    private String accessToken;
}
