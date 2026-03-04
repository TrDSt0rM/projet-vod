package combis.services;

import combis.dtos.DogDto;

import java.util.List;

public interface DogService {
    List<DogDto> getAllDogs();
    DogDto getDogById(Long id);
    DogDto saveDog(DogDto dto);
    void deleteDog(Long id);
}
