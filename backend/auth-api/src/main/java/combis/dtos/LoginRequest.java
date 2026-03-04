package combis.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank
    private String pseudo;

    @NotBlank
    private String password;
}
