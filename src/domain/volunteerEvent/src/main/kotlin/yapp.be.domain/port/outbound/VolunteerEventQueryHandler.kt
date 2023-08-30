package yapp.be.domain.port.outbound

import java.time.LocalDateTime
import yapp.be.domain.model.VolunteerEvent
import yapp.be.domain.model.VolunteerEventJoinQueue
import yapp.be.domain.model.VolunteerEventWaitingQueue
import yapp.be.domain.model.dto.DetailVolunteerEventDto
import yapp.be.domain.model.dto.ReminderVolunteerEventDto
import yapp.be.domain.model.dto.SimpleVolunteerEventDto
import java.time.LocalDate

interface VolunteerEventQueryHandler {

    fun findByIdAndShelterId(
        id: Long,
        shelterId: Long
    ): VolunteerEvent

    fun findDetailVolunteerEventInfoByIdAndShelterId(
        id: Long,
        shelterId: Long
    ): DetailVolunteerEventDto

    fun findAllVolunteerEventByDayBefore(
        date: LocalDate
    ): List<ReminderVolunteerEventDto>

    fun findDetailVolunteerEventInfoByIdAndShelterIdAndVolunteerId(
        id: Long,
        volunteerId: Long,
        shelterId: Long
    ): DetailVolunteerEventDto
    fun findAllSimpleVolunteerEventInfosByShelterIdAndDateRange(
        shelterId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<SimpleVolunteerEventDto>
    fun findAllSimpleVolunteerEventInfosWithMyParticipationStatusByShelterIdAndVolunteerIdAndDateRange(
        shelterId: Long,
        volunteerId: Long,
        from: LocalDateTime,
        to: LocalDateTime
    ): List<SimpleVolunteerEventDto>

    fun findVolunteerEventJoinQueueByVolunteerIdAndVolunteerEventId(
        volunteerId: Long,
        volunteerEventId: Long
    ): VolunteerEventJoinQueue?

    fun findVolunteerEventWaitingQueueByVolunteerIdAndVolunteerEventId(
        volunteerId: Long,
        volunteerEventId: Long
    ): VolunteerEventWaitingQueue?
}
