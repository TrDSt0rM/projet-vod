package combis.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table(name = "dogs")
@Data
public class Dog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(nullable = false, length = 80)
	private String race;

	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;
}
