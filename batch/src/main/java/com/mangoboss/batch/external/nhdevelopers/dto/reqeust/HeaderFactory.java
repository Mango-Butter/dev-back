package com.mangoboss.batch.external.nhdevelopers.dto.reqeust;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

@Component
@RequiredArgsConstructor
public class HeaderFactory {
    private final ApiProperties props;

    public CommonPartHeaderRequest create(final ApiName apiName, final String apiSvcCd, final Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        String tsymd = now.format(ofPattern("yyyyMMdd"));
        String trtm = now.format(ofPattern("HHmmss"));
        String isTuno = trtm + now.getNano();

        return CommonPartHeaderRequest.builder()
                .ApiNm(apiName.getName())
                .Tsymd(tsymd)
                .Trtm(trtm)
                .Iscd(props.getIscd())
                .FintechApsno(props.getFintechApsno())
                .ApiSvcCd(apiSvcCd)
                .IsTuno(isTuno)
                .AccessToken(props.getAccessToken())
                .build();
    }
}
