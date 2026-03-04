package combis.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DogDto {

	private Long id;

	@NotBlank(message = "Le nom est obligatoire")
	private String name;

	@NotBlank(message = "La race est obligatoire")
	private String race;

	@NotNull(message = "La date de naissance est obligatoire")
	private LocalDate birthDate;
}
