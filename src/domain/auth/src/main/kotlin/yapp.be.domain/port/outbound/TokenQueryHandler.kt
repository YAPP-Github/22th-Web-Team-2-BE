package yapp.be.domain.port.outbound

interface TokenQueryHandler {
    fun getTokensByAuthToken(authToken: String): String?
    fun getTokenByAccessToken(accessToken: String): String?
    fun checkToken(accessToken: String, refreshToken: String): Boolean
}
