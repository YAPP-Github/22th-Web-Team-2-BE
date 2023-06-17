package yapp.be.domain.port.inbound

import yapp.be.domain.port.inbound.model.CreateUserCommand

interface CreateUserUseCase {
    fun create(command: CreateUserCommand): Boolean
}