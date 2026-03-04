package combis.mappers;

import combis.dtos.RegisterRequest;
import combis.dtos.user;
import combis.entities.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest dto) {
        User u = new User();
        u.setPseudo(dto.getPseudo());
        u.setNom(dto.getNom());
        u.setPrenom(dto.getPrenom());
        u.setAge(dto.getAge());
        u.setAdresse(dto.getAdresse());
        return u;
    }

    public user toDto(User u) {
        user dto = new user();
        dto.setId(u.getId());
        dto.setPseudo(u.getPseudo());
        dto.setNom(u.getNom());
        dto.setPrenom(u.getPrenom());
        dto.setAge(u.getAge());
        dto.setAdresse(u.getAdresse());
        return dto;
    }
}
