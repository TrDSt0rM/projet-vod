package combis.services.impl;

import combis.dtos.DogDto;
import combis.entities.Dog;
import combis.mappers.DogMapper;
import combis.repositories.DogRepository;
import combis.services.DogService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DogServiceImpl implements DogService {

	private final DogRepository dogRepository;
	private final DogMapper dogMapper;

	public DogServiceImpl(DogRepository dogRepository, DogMapper dogMapper) {
		this.dogRepository = dogRepository;
		this.dogMapper = dogMapper;
	}

	@Override
	public List<DogDto> getAllDogs() {
		return dogRepository.findAll().stream().map(dogMapper::toDto).toList();
	}

	@Override
	public DogDto getDogById(Long id) {
		Dog dog = dogRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Dog introuvable: " + id));
		return dogMapper.toDto(dog);
	}

	@Override
	public DogDto saveDog(DogDto dto) {
		Dog toSave = dogMapper.toEntity(dto);
		Dog saved = dogRepository.save(toSave);
		return dogMapper.toDto(saved);
	}

	@Override
	public void deleteDog(Long id) {
		if (!dogRepository.existsById(id)) {
			throw new EntityNotFoundException("Dog introuvable: " + id);
		}
		dogRepository.deleteById(id);
	}
}
