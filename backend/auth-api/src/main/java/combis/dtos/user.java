package combis.dtos;

import lombok.Data;

@Data
public class user {
    private Long id;
    private String pseudo;
    private String nom;
    private String prenom;
    private Integer age;
    private String adresse;
    private String role = "USER";
}
