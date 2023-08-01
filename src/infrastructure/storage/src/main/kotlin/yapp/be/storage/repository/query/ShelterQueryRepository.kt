package yapp.be.storage.repository.query

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import yapp.be.domain.model.BankAccount
import yapp.be.domain.model.Shelter
import yapp.be.domain.model.ShelterBookMark
import yapp.be.domain.model.ShelterParkingInfo
import yapp.be.domain.model.dto.ShelterDto
import yapp.be.domain.port.outbound.shelter.ShelterBookMarkQueryHandler
import yapp.be.domain.port.outbound.shelter.ShelterQueryHandler
import yapp.be.exceptions.CustomException
import yapp.be.storage.config.exceptions.StorageExceptionType
import yapp.be.storage.jpa.common.model.toDomainModel
import yapp.be.storage.jpa.shelter.model.mappers.toDomainModel
import yapp.be.storage.jpa.shelter.repository.ShelterBookMarkJpaRepository
import yapp.be.storage.jpa.shelter.repository.ShelterJpaRepository

@Component
@Transactional(readOnly = true)
class ShelterQueryRepository(
    private val shelterJpaRepository: ShelterJpaRepository,
    private val shelterBookMarkJpaRepository: ShelterBookMarkJpaRepository
) : ShelterQueryHandler, ShelterBookMarkQueryHandler {

    override fun findById(id: Long): Shelter {
        val shelterEntity = shelterJpaRepository.findByIdOrNull(id) ?: throw CustomException(
            type = StorageExceptionType.ENTITY_NOT_FOUND,
            message = "보호소를 찾을 수 없습니다."
        )
        return shelterEntity.toDomainModel()
    }

    override fun findInfoByIdAndVolunteerId(id: Long, volunteerId: Long): ShelterDto {
        val shelterWithBookMarkInfo = shelterJpaRepository
            .findWithBookMarkByIdAndVolunteerId(
                id = id,
                volunteerId = volunteerId
            ) ?: throw CustomException(
            type = StorageExceptionType.ENTITY_NOT_FOUND,
            message = "보호소를 찾을 수 없습니다."
        )

        return ShelterDto(
            id = shelterWithBookMarkInfo.id,
            name = shelterWithBookMarkInfo.name,
            description = shelterWithBookMarkInfo.description,
            phoneNumber = shelterWithBookMarkInfo.phoneNum,
            notice = shelterWithBookMarkInfo.notice,
            profileImageUrl = shelterWithBookMarkInfo.profileImageUrl,
            bankAccount = run {
                if (shelterWithBookMarkInfo.bankName.isNullOrEmpty() || shelterWithBookMarkInfo.bankAccountNum.isNullOrEmpty()) null
                else BankAccount(name = shelterWithBookMarkInfo.bankName, accountNumber = shelterWithBookMarkInfo.bankAccountNum)
            },
            address = shelterWithBookMarkInfo.address.toDomainModel(),
            parkingInfo = run {
                if (shelterWithBookMarkInfo.parkingEnabled == null || shelterWithBookMarkInfo.parkingNotice.isNullOrEmpty()) null
                else ShelterParkingInfo(parkingEnabled = shelterWithBookMarkInfo.parkingEnabled, parkingNotice = shelterWithBookMarkInfo.parkingNotice)
            },
            bookMarked = shelterWithBookMarkInfo.bookMarked

        )
    }

    override fun findInfoById(id: Long): ShelterDto {
        val shelter = shelterJpaRepository.findByIdOrNull(id)?.toDomainModel()
            ?: throw CustomException(
                type = StorageExceptionType.ENTITY_NOT_FOUND,
                message = "보호소를 찾을 수 없습니다."
            )

        return ShelterDto(
            id = shelter.id,
            name = shelter.name,
            description = shelter.description,
            phoneNumber = shelter.phoneNumber,
            notice = shelter.notice,
            profileImageUrl = shelter.profileImageUrl,
            bankAccount = shelter.bankAccount,
            address = shelter.address,
            parkingInfo = shelter.parkingInfo,
            bookMarked = false
        )
    }
    override fun existByName(name: String): Boolean {
        return shelterJpaRepository.findByName(name) != null
    }

    override fun getAllBookMarkedShelterByVolunteerId(volunteerId: Long): List<Shelter> {
        return shelterJpaRepository.findAllBookMarkedShelterByVolunteerId(volunteerId)
            .map { it.toDomainModel() }
    }

    override fun getShelterIdAndVolunteerId(shelterId: Long, volunteerId: Long): ShelterBookMark? {
        val shelterBookMarkEntity = shelterBookMarkJpaRepository.findByShelterIdAndVolunteerId(
            shelterId = shelterId,
            volunteerId = volunteerId
        )

        return shelterBookMarkEntity?.toDomainModel()
    }
}