package yapp.be.domain.port.inbound

import java.time.LocalDateTime
import yapp.be.domain.model.VolunteerEvent
import yapp.be.domain.model.dto.DetailVolunteerEventDto
import yapp.be.domain.model.dto.ShelterVolunteerEventStatDto
import yapp.be.domain.model.dto.SimpleVolunteerEventDto

interface GetVolunteerEventUseCase {

    fun getShelterVolunteerEventStat(
        shelterId: Long
    ): ShelterVolunteerEventStatDto

    fun getVolunteerEvent(
        shelterId: Long,
        volunteerEventId: Long
    ): VolunteerEvent

    fun getMemberDetailVolunteerEventInfo(
        shelterId: Long,
        volunteerId: Long,
        volunteerEventId: Long
    ): DetailVolunteerEventDto

    fun getNonMemberDetailVolunteerEventInfo(
        shelterId: Long,
        volunteerEventId: Long
    ): DetailVolunteerEventDto

    fun getShelterUserDetailVolunteerEventInfo(
        shelterId: Long,
        volunteerEventId: Long
    ): DetailVolunteerEventDto

    fun getShelterUserVolunteerEventsByDateRange(
        shelterId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<SimpleVolunteerEventDto>
    fun getMemberVolunteerEventsByDateRange(
        shelterId: Long,
        volunteerId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<SimpleVolunteerEventDto>
    fun getNonMemberVolunteerEventsByDateRange(
        shelterId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<SimpleVolunteerEventDto>
}
