package com.example.restapijwttutorial.security;

import com.example.restapijwttutorial.document.User;
import com.example.restapijwttutorial.jwt.JwtHelper;
import com.example.restapijwttutorial.services.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;


@Log4j2
public class AccessTokenFilter extends OncePerRequestFilter {

   @Autowired
   private JwtHelper jwtHelper;

   @Autowired
   private UserService userService;

   @Override
   protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
      try {
         Optional<String> accessToken = parseAccessToken(request);
         if (accessToken.isPresent() && jwtHelper.validateAccessToken(accessToken.get())) {
            Long userId = Long.parseLong(jwtHelper.getUserIdFromAccessToken(accessToken.get()));
            User user = userService.findById(userId);
            UsernamePasswordAuthenticationToken upat = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            upat.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(upat);

         }
      } catch (Exception e) {
         log.error("cannot set authentication", e);
      }

      filterChain.doFilter(request,response);
   }

   private Optional<String> parseAccessToken (HttpServletRequest httpServletRequest) {
      String authHeader = httpServletRequest.getHeader("Authorization");
      if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
         return Optional.of(authHeader.replace("Bearer ", ""));
      }
      return Optional.empty();
   }
}
