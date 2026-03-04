package combis.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_pseudo", columnNames = {"pseudo"})
        }
)
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String pseudo;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(nullable = false, length = 80)
    private String prenom;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = false, length = 255)
    private String adresse;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Column(nullable = false, length = 20)
    private String role = "USER";
}
