package yapp.be.storage.jpa.model

import jakarta.persistence.Embeddable


@Embeddable
data class Address(
    private val city: String,
    private val borough: String,
    private val town: String,
    private val complexName: String,
    private val block: String,
    private val unit: String,
    private val roadName: String,
    private val mainBuildingNumber: Int,
    private val subBuildingNumber: Int,
    private val buildingName: String
)