package combis.services;

import combis.dtos.Authresponse;
import combis.dtos.LoginRequest;
import combis.dtos.RegisterRequest;
import combis.dtos.user;

public interface AuthService {
    user register(RegisterRequest dto);
    Authresponse login(LoginRequest dto);
    user me(String token);
}
