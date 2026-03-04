package combis.controllers;

import combis.dtos.DogDto;
import combis.services.impl.DogServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dogs")
public class DogController {

	private final DogServiceImpl dogService;
//ici est
	public DogController(DogServiceImpl dogService) {
		this.dogService = dogService;
	}

	@GetMapping
	public List<DogDto> getDogs() {
		return dogService.getAllDogs();
	}

	@GetMapping("/{id}")
	public DogDto getDog(@PathVariable Long id) {
		return dogService.getDogById(id);
	}

	@PostMapping
	public DogDto createDog(@Valid @RequestBody DogDto dto) {
		return dogService.saveDog(dto);
	}

	@DeleteMapping("/{id}")
	public void deleteDog(@PathVariable Long id) {
		dogService.deleteDog(id);
	}
}
