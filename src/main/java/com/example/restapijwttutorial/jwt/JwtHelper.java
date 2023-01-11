package com.example.restapijwttutorial.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.restapijwttutorial.document.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Component
@Log4j2
public class JwtHelper {

   static final String issuer = "MyApp";

//   @Value("${accessTokenExpirationMinutes}")
   @Value("#{${accessTokenExpirationMinutes}*180*60*1000}")
   private int accessTokenExpirationMinutes;
//   @Value("${refreshTokenExpirationMinutes}")
//   @Value("#{30*24*60*60*1000}")
   private long refreshTokenExpirationMinutes;

   private Algorithm accessTokenAlgorithm;
   private Algorithm refreshTokenAlgorithm;
   private JWTVerifier accessTokenVerifier;
   private JWTVerifier refreshTokenVerifier;

   public JwtHelper(@Value("${accessTokenSecret}") String accessTokenSecret, @Value("${refreshTokenSecret}") String refreshTokenSecret, @Value("${refreshTokenExpirationDays}") int refreshTokenExpirationDays) {
      refreshTokenExpirationMinutes = (long) refreshTokenExpirationDays * 24 * 60 * 60 * 1000;

      accessTokenAlgorithm = Algorithm.HMAC256(accessTokenSecret);
      refreshTokenAlgorithm = Algorithm.HMAC256(refreshTokenSecret);

      accessTokenVerifier = JWT.require(accessTokenAlgorithm).withIssuer(issuer).build();
      refreshTokenVerifier = JWT.require(refreshTokenAlgorithm).withIssuer(issuer).build();
   }

   public String generateAccessToken(User user) {
      return JWT.create().withIssuer(issuer)
              .withSubject(user.getId().toString())
              .withIssuedAt(new Date())
              .withExpiresAt(new Date(new Date().getTime() + accessTokenExpirationMinutes))
              .sign(accessTokenAlgorithm);
   }

   public String generateRefreshToken(User user, String tokenId) {
      return JWT.create().withIssuer(issuer)
              .withSubject(user.getId().toString())
              .withClaim("tokenId", tokenId)
              .withIssuedAt(new Date())
              .withExpiresAt(new Date(new Date().getTime() + refreshTokenExpirationMinutes))
              .sign(refreshTokenAlgorithm);
   }

   private Optional<DecodedJWT> decodeAccessToken(String token ) {
      try {
         return Optional.of(accessTokenVerifier.verify(token));
      } catch (JWTVerificationException e) {
         log.error("invalid access token", e);
      }
      return Optional.empty();
   }

   private Optional<DecodedJWT> decodeRefreshToken(String token ) {
      try {
         return Optional.of(refreshTokenVerifier.verify(token));
      } catch (JWTVerificationException e) {
         log.error("invalid refresh token", e);
      }
      return Optional.empty();
   }

   public boolean validateAccessToken(String token) {
      return decodeAccessToken(token).isPresent();
   }

   public boolean validateRefreshToken(String token) {
      return decodeRefreshToken(token).isPresent();
   }

   public String getUserIdFromAccessToken(String token) {
      return decodeAccessToken(token).get().getSubject();
   }

   public String getUserIdFromRefreshToken(String token) {
      return decodeRefreshToken(token).get().getSubject();
   }

   public String getTokenIdFromRefreshToken (String token) {
      return decodeRefreshToken(token).get().getClaim("tokenId").asString();
   }

}
