package pe.dcs.registry.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import pe.dcs.registry.security.service.UserDetailsServiceImpl;

import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private static final Logger filterLogger = LogManager.getLogger(JwtTokenFilter.class);

    final JwtProvider jwtProvider;

    final UserDetailsServiceImpl userDetailsService;

    public JwtTokenFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req,
                                    @NonNull HttpServletResponse res,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = parseJwt(req);

            if (jwt != null && jwtProvider.validateJWT(jwt)) {

                String usernameUsuario = jwtProvider.getUsernameFromToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(usernameUsuario);
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            filterLogger.error("No se puede enviar la autenticación del usuario:", e);
        }

        filterChain.doFilter(req, res);
    }

    private String parseJwt(HttpServletRequest request) {

        String header = request.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {

            return header.substring(7);
        }
        return null;
    }
}
