package yapp.be.apiapplication.auth.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import yapp.be.apiapplication.auth.service.model.TokenRefreshRequestDto
import yapp.be.apiapplication.auth.service.model.TokenRefreshResponseDto
import yapp.be.apiapplication.system.exception.ApiExceptionType
import yapp.be.apiapplication.system.security.JwtTokenProvider
import yapp.be.apiapplication.system.security.SecurityTokenType
import yapp.be.apiapplication.system.security.properties.JwtConfigProperties
import yapp.be.domain.port.inbound.CheckTokenUseCase
import yapp.be.domain.port.inbound.DeleteTokenUseCase
import yapp.be.domain.port.inbound.SaveTokenUseCase
import yapp.be.exceptions.CustomException
import yapp.be.model.enums.volunteerevent.Role
import yapp.be.model.vo.Email
import java.time.Duration

@Service
class TokenRefreshApplicationService(
    private val checkTokenUseCase: CheckTokenUseCase,
    private val jwtTokenProvider: JwtTokenProvider,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val deleteTokenUseCase: DeleteTokenUseCase,
    private val jwtConfigProperties: JwtConfigProperties,
) {
    @Transactional
    fun refresh(reqDto: TokenRefreshRequestDto): TokenRefreshResponseDto {
        val isValidRefreshToken = checkTokenUseCase.checkToken(
            accessToken = reqDto.accessToken,
            refreshToken = reqDto.refreshToken,
        )
        if (!isValidRefreshToken) {
            throw CustomException(ApiExceptionType.INVALID_TOKEN, "올바르지 않은 토큰입니다.")
        }

        val claim = jwtTokenProvider.parseClaims(reqDto.refreshToken, SecurityTokenType.REFRESH) ?: throw CustomException(ApiExceptionType.UNAUTHORIZED_EXCEPTION, "손상된 토큰입니다.")
        val role = claim["role"] as? String ?: throw CustomException(ApiExceptionType.UNAUTHENTICATED_EXCEPTION, "Cannot Find Role")

        deleteTokenUseCase.deleteToken(reqDto.accessToken)

        val accessToken = jwtTokenProvider.generateToken(
            id = claim["id"].toString().toLong(),
            email = Email(claim["email"].toString()),
            role = Role.valueOf(role),
            securityTokenType = SecurityTokenType.ACCESS
        )
        saveTokenUseCase.saveToken(
            accessToken = accessToken,
            refreshToken = reqDto.refreshToken,
            expire = Duration.ofMillis(jwtConfigProperties.refresh.expire),
        )

        return TokenRefreshResponseDto(
            accessToken = accessToken,
            refreshToken = reqDto.refreshToken,
        )
    }
}
