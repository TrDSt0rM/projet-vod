package combis.services.impl;

import combis.dtos.Authresponse;
import combis.dtos.LoginRequest;
import combis.dtos.RegisterRequest;
import combis.dtos.user;
import combis.entities.User;
import combis.mappers.UserMapper;
import combis.repositories.AuthRepo;
import combis.services.AuthService;
import combis.utils.PasswordUtil;
import combis.utils.TokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional
public class AuthServiceIMPL implements AuthService {

    private final AuthRepo authRepo;
    private final UserMapper userMapper;

    public AuthServiceIMPL(AuthRepo authRepo, UserMapper userMapper) {
        this.authRepo = authRepo;
        this.userMapper = userMapper;
    }

    @Override
    public user register(RegisterRequest dto) {
        // 1) Vérifier unicité du pseudo
        if (authRepo.existsByPseudo(dto.getPseudo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pseudo déjà utilisé");
        }

        // 2) DTO -> Entity
        User entity = userMapper.toEntity(dto);

        // 3) Hash du mot de passe avant insertion en BDD
        entity.setPasswordHash(PasswordUtil.hashPassword(dto.getPassword()));

        // 4) Save BDD
        User saved = authRepo.save(entity);

        // 5) Entity -> DTO (sans password)
        return userMapper.toDto(saved);
    }

    @Override
    public Authresponse login(LoginRequest dto) {
        // 1) Chercher utilisateur par pseudo
        User user = authRepo.findByPseudo(dto.getPseudo())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides"));

        // 2) Vérifier mot de passe
        if (!PasswordUtil.matches(dto.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Identifiants invalides");
        }

        // 3) Générer token
        String token = TokenUtil.generateToken(user.getId(), user.getPseudo(), user.getRole());

        // 4) Réponse
        Authresponse res = new Authresponse();
        res.setToken(token);
        res.setUser(userMapper.toDto(user));
        return res;
    }

    @Override
    public user me(String token) {
        // token doit être "valide" (souvent header Authorization)
        Long userId = TokenUtil.verifyAndGetUserId(token);
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Token invalide ou expiré");
        }

        User user = authRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utilisateur introuvable"));

        return userMapper.toDto(user);
    }
}