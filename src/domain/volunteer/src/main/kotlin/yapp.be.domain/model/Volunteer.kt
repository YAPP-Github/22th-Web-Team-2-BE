package yapp.be.domain.model

import yapp.be.enum.OAuthType
import yapp.be.enum.Role
import yapp.be.model.Email

data class Volunteer(
    val id: Long = 0,
    val email: Email,
    val nickname: String,
    val phone: String,
    val role: Role = Role.VOLUNTEER,
    val oAuthType: OAuthType = OAuthType.KAKAO,
    val oAuthAccessToken: String? = null,
    val oAuthRefreshToken: String? = null,
    val isDeleted: Boolean = false,
)