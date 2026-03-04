package combis.utils;

import combis.entities.User;
import combis.repositories.AuthRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuthHelper {

    public static Long extractUserIdFromBearer(String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token manquant");
        }

        String token = authorization.substring(7).trim();

        Long userId = TokenUtil.verifyAndGetUserId(token);

        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide ou expiré");
        }

        return userId;
    }

    public static User requireUser(String authorization, AuthRepo repo) {
        Long userId = extractUserIdFromBearer(authorization);

        return repo.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur introuvable"));
    }

    public static void requireAdmin(User user) {
        if (!"ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Accès admin requis");
        }
    }
}