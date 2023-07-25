package yapp.be.apiapplication.shelter.service.model

data class GetShelterMyProfileResponseDto(
    val name: String,
    val historyStat: ShelterVolunteerEventHistoryStatInfo
)

data class ShelterVolunteerEventHistoryStatInfo(
    val done: Int,
    val inProgress: Int
)
