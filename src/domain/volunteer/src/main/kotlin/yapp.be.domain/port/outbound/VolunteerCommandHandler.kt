package yapp.be.domain.port.outbound

import yapp.be.domain.model.Volunteer

interface VolunteerCommandHandler {
    fun save(volunteer: Volunteer): Volunteer

    fun update(volunteer: Volunteer): Volunteer
}
