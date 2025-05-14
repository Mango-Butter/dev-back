package com.mangoboss.app.external.nhdevelopers.dto.reqeust;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;
import static java.time.format.DateTimeFormatter.*;

@Component
@RequiredArgsConstructor
public class NhHeaderFactory {
    private final NhApiProperties props;

    public NhCommonPartHeaderRequest create(final ApiName apiName, final Clock clock) {
        LocalDateTime now = LocalDateTime.now(clock);
        String tsymd = now.format(ofPattern("yyyyMMdd"));
        String trtm = now.format(ofPattern("HHmmss"));
        String isTuno = trtm + now.getNano();

        return NhCommonPartHeaderRequest.builder()
                .ApiNm(apiName.getName())
                .Tsymd(tsymd)
                .Trtm(trtm)
                .Iscd(props.getIscd())
                .FintechApsno(props.getFintechApsno())
                .ApiSvcCd(props.getApiSvcCd())
                .IsTuno(isTuno)
                .AccessToken(props.getAccessToken())
                .build();
    }
}
