package combis.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank @Size(max = 50)
    private String pseudo;

    @NotBlank @Size(max = 80)
    private String nom;

    @NotBlank @Size(max = 80)
    private String prenom;

    @NotNull @Min(0)
    private Integer age;

    @NotBlank @Size(max = 255)
    private String adresse;

    @NotBlank @Size(min = 6)
    private String password;
}
