package com.mypay.identity.configuration;

import com.mypay.identity.repositories.InvalidatedTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.signKey}")
    private String SIGNER_KEY;

    private final InvalidatedTokenRepository _invalidatedTokenRepository;

    public JwtAuthenticationFilter(InvalidatedTokenRepository invalidatedTokenRepository) {
        _invalidatedTokenRepository = invalidatedTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        // 1. In ra xem request có mang theo Header không
        System.out.println("=== [JWT Filter] API đang gọi: " + request.getRequestURI());
        System.out.println("=== [JWT Filter] Authorization Header: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

                if (signedJWT.verify(verifier)) {
                    String jwtId = signedJWT.getJWTClaimsSet().getJWTID();
                    if (_invalidatedTokenRepository.existsById(jwtId)) {
                        System.out.println("=== [JWT Filter] Token này đã bị thu hồi (Đã đăng xuất trước đó)!");
                        // Dừng chuỗi filter, không cho phép đi tiếp vào Controller
                        filterChain.doFilter(request, response);
                        return;
                    }
                    String username = signedJWT.getJWTClaimsSet().getSubject();
                    String accountId = signedJWT.getJWTClaimsSet().getStringClaim("accountId");
                    System.out.println("=== [JWT Filter] Token HỢP LỆ của user: " + username);

                    String rolesStr = (String) signedJWT.getJWTClaimsSet().getClaim("roles");
                    List<SimpleGrantedAuthority> authorities = Arrays.stream(rolesStr.split(" "))
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(accountId, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("=== [JWT Filter] Cảnh báo: Chữ ký Token KHÔNG ĐÚNG!");
                }
            } catch (ParseException e) {
                System.out.println("=== [JWT Filter] Lỗi Parse: Chuỗi Token bị sai định dạng! (Có thể do Bruno truyền lỗi)");
            } catch (JOSEException e) {
                System.out.println("=== [JWT Filter] Lỗi JOSE: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("=== [JWT Filter] Lỗi không xác định: " + e.getMessage());
            }
        } else {
            System.out.println("=== [JWT Filter] Không tìm thấy Bearer Token trong request này.");
        }

        filterChain.doFilter(request, response);
    }
}