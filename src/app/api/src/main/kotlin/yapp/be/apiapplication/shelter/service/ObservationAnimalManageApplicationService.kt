package yapp.be.apiapplication.shelter.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yapp.be.apiapplication.shelter.service.model.AddObservationAnimalRequestDto
import yapp.be.apiapplication.shelter.service.model.AddObservationAnimalResponseDto
import yapp.be.apiapplication.shelter.service.model.DeleteObservationAnimalResponseDto
import yapp.be.apiapplication.shelter.service.model.EditObservationAnimalRequestDto
import yapp.be.apiapplication.shelter.service.model.EditObservationAnimalResponseDto
import yapp.be.apiapplication.shelter.service.model.GetShelterUserObservationAnimalResponseDto
import yapp.be.domain.port.inbound.observationanimal.AddObservationAnimalUseCase
import yapp.be.domain.port.inbound.observationanimal.DeleteObservationAnimalUseCase
import yapp.be.domain.port.inbound.observationanimal.EditObservationAnimalUseCase
import yapp.be.domain.port.inbound.observationanimal.GetObservationAnimalUseCase
import yapp.be.domain.port.inbound.shelteruser.GetShelterUserUseCase
import yapp.be.model.support.PagedResult

@Service
class ObservationAnimalManageApplicationService(
    private val getShelterUserUseCase: GetShelterUserUseCase,
    private val getObservationAnimalUseCase: GetObservationAnimalUseCase,
    private val addObservationAnimalUseCase: AddObservationAnimalUseCase,
    private val editObservationAnimalUseCase: EditObservationAnimalUseCase,
    private val deleteObservationAnimalUseCase: DeleteObservationAnimalUseCase
) {

    @Transactional(readOnly = true)
    fun getShelterObservationAnimals(shelterUserId: Long, page: Int): PagedResult<GetShelterUserObservationAnimalResponseDto> {
        val shelterUser = getShelterUserUseCase.getShelterUserById(shelterUserId)

        val observationAnimals = getObservationAnimalUseCase.getAllObservationAnimalsByShelterId(
            shelterId = shelterUser.shelterId,
            page = page
        )

        return PagedResult(
            pageNumber = observationAnimals.pageNumber,
            pageSize = observationAnimals.pageSize,
            content = observationAnimals.content.map { observationAnimal ->
                GetShelterUserObservationAnimalResponseDto(
                    id = observationAnimal.id,
                    shelterId = observationAnimal.shelterId,
                    name = observationAnimal.name,
                    age = observationAnimal.age,
                    gender = observationAnimal.gender,
                    profileImageUrl = observationAnimal.profileImageUrl,
                    specialNote = observationAnimal.specialNote,
                    breed = observationAnimal.breed
                )
            }.toList()
        )
    }

    @Transactional(readOnly = true)
    fun getObservationAnimal(observationAnimalId: Long): GetShelterUserObservationAnimalResponseDto {
        val observationAnimal = getObservationAnimalUseCase.getObservationAnimalById(observationAnimalId)

        return GetShelterUserObservationAnimalResponseDto(
            id = observationAnimal.id,
            shelterId = observationAnimal.shelterId,
            name = observationAnimal.name,
            age = observationAnimal.age,
            gender = observationAnimal.gender,
            profileImageUrl = observationAnimal.profileImageUrl,
            specialNote = observationAnimal.specialNote,
            breed = observationAnimal.breed

        )
    }

    @Transactional
    fun addObservationAnimal(shelterUserId: Long, reqDto: AddObservationAnimalRequestDto): AddObservationAnimalResponseDto {
        val shelterUser = getShelterUserUseCase.getShelterUserById(shelterUserId)
        val observationAnimal = addObservationAnimalUseCase.addObservationAnimal(
            shelterId = shelterUser.shelterId,
            name = reqDto.name,
            age = reqDto.age,
            specialNote = reqDto.specialNote,
            gender = reqDto.gender,
            breed = reqDto.breed,
            images = reqDto.images
        )
        return AddObservationAnimalResponseDto(
            observationAnimalId = observationAnimal.id
        )
    }

    @Transactional
    fun editObservationAnimal(shelterUserId: Long, observationAnimalId: Long, reqDto: EditObservationAnimalRequestDto): EditObservationAnimalResponseDto {
        val shelterUser = getShelterUserUseCase.getShelterUserById(shelterUserId)
        val observationAnimal = getObservationAnimalUseCase.getObservationAnimalByIdAndShelterId(
            observationAnimalId = observationAnimalId,
            shelterId = shelterUser.shelterId
        )

        val updatedObservationAnimal = editObservationAnimalUseCase.editObservationAnimal(
            observationAnimalId = observationAnimal.id,
            shelterId = shelterUser.shelterId,
            name = reqDto.name,
            age = reqDto.age,
            specialNote = reqDto.specialNote,
            gender = reqDto.gender,
            breed = reqDto.breed,
            images = reqDto.images
        )

        return EditObservationAnimalResponseDto(
            observationAnimalId = updatedObservationAnimal.id
        )
    }

    @Transactional
    fun deleteObservationAnimal(
        observationAnimalId: Long,
        shelterUserId: Long
    ): DeleteObservationAnimalResponseDto {
        val shelterUser = getShelterUserUseCase.getShelterUserById(shelterUserId)
        val observationAnimal = deleteObservationAnimalUseCase.deleteObservationAnimal(
            shelterId = shelterUser.shelterId,
            observationAnimalId = observationAnimalId
        )

        return DeleteObservationAnimalResponseDto(observationAnimal.id)
    }
}
