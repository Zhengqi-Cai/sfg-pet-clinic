package guru.framework.sfgpetclinic.services.map;

import guru.framework.sfgpetclinic.model.Owner;
import guru.framework.sfgpetclinic.model.Pet;
import guru.framework.sfgpetclinic.services.OwnerService;
import guru.framework.sfgpetclinic.services.PetService;
import guru.framework.sfgpetclinic.services.PetTypeService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Profile({"default","map"})
public class OwnerServiceMap extends abstractMapService<Owner,Long> implements OwnerService {

    private final PetTypeService petTypeService;
    private final PetService petService;

    public OwnerServiceMap(PetTypeService petTypeService, PetService petService) {
        this.petTypeService = petTypeService;
        this.petService = petService;
    }

    @Override
    public Owner findByLastName(String lastName) {

        return super.findAll()
                .stream()
                .filter(owner -> owner.getLastName()
                        .equalsIgnoreCase(lastName))
                .findFirst()
                .orElse(null);
    }


    @Override
    public Owner save(Owner object) {
        if(object!=null){
            if (object.getPets()!=null){
                for (Pet pet : object.getPets()) {
                    if (pet.getPetType()!=null){
                        if (pet.getPetType().getId()==null){
                            pet.setPetType(petTypeService.save(pet.getPetType()));
                        }
                    }else{
                        throw new RuntimeException("Pet Type is required");
                    }

                    if (pet.getId()==null){
                        Pet savedPet=petService.save(pet);
                        pet.setId(savedPet.getId());
                    }
                }
            }
            return super.save(object);
        }else{
            return null;
        }
    }

    @Override
    public List<Owner> findAllByLastNameLike(String lastName) {
        return null;
    }
}
