package raviolz.understory_back.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import raviolz.understory_back.entities.User;
import raviolz.understory_back.services.UserService;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final UserService userService;

    public TokenFilter(TokenTools tokenTools, UserService userService) {
        this.tokenTools = tokenTools;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token mancante o formato non corretto.");
            return;
        }

        try {
            String accessToken = authHeader.replace("Bearer ", "");

            tokenTools.verifyToken(accessToken);

            UUID userId = tokenTools.extractIdFromToken(accessToken);

            User authenticatedUser = userService.findById(userId);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticatedUser,
                    null,
                    authenticatedUser.getAuthorities()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception ex) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token non valido oppure utente non autorizzato.");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath())
                || new AntPathMatcher().match("/error", request.getServletPath())
                || "OPTIONS".equalsIgnoreCase(request.getMethod());
    }
}