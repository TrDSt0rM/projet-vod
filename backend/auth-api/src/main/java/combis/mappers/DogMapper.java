package combis.mappers;

import combis.dtos.DogDto;
import combis.entities.Dog;
import org.springframework.stereotype.Component;

@Component
public class DogMapper {

    public DogDto toDto(Dog dog) {
        DogDto dto = new DogDto();
        dto.setId(dog.getId());
        dto.setName(dog.getName());
        dto.setRace(dog.getRace());
        dto.setBirthDate(dog.getBirthDate());
        return dto;
    }

    public Dog toEntity(DogDto dto) {
        Dog dog = new Dog();
        dog.setId(dto.getId());
        dog.setName(dto.getName());
        dog.setRace(dto.getRace());
        dog.setBirthDate(dto.getBirthDate());
        return dog;
    }
}
