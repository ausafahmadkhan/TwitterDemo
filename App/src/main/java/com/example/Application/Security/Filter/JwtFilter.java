package com.example.Application.Security.Filter;

import com.example.Application.Service.JwtTokenService;
import com.example.Application.Service.UserService;
import com.example.Contracts.Requests.SearchRequest;
import com.example.Contracts.Responses.UserResponse;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException
    {
        String bearerToken = httpServletRequest.getHeader("Authorization");
        String token = jwtTokenService.getToken(bearerToken);

        if (null != token) {
            try {
                Claims claims = jwtTokenService.validateToken(token);

                if (null == claims) {
                    System.err.println("Bad credentials!");
                } else {
                    String username = claims.get("username").toString();
                    SearchRequest searchRequest = new SearchRequest();
                    searchRequest.setName(username);
                    UserResponse userResponse = userService.fetchUser(searchRequest);
                    List<SimpleGrantedAuthority> authorities = userResponse.getRoles()
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.getVal()))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userResponse.getName(), null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } catch (Exception e) {
                System.err.println("User not present.");
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
