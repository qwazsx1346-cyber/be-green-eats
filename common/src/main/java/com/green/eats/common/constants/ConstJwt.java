package com.green.eats.common.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="constants.jwt")
//constants밑에 jwt밑에 아래 얘네들이 있다는뜻. yaml에서 데이터 가져옴
public record ConstJwt(String issuer
      , String bearerFormat
      , String claimKey
      , String secretKey
      , String accessTokenCookieName
      , String accessTokenCookiePath
      , int accessTokenCookieValiditySeconds
      , long accessTokenValidityMilliseconds
      , String refreshTokenCookieName
      , String refreshTokenCookiePath
      , int refreshTokenCookieValiditySeconds
      , long refreshTokenValidityMilliseconds) {}
