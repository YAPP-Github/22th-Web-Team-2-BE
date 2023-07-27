package yapp.be.domain.port.inbound.model

import yapp.be.model.enums.event.EventType

data class CreateEventCommand(
    val recordId: String,
    val json: String,
    val type: EventType,
)
