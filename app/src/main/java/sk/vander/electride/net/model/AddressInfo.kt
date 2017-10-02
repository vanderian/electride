package sk.vander.electride.net.model

import com.google.gson.annotations.SerializedName

data class AddressInfo(
    @SerializedName("ID") val id: Int,
    @SerializedName("Title") val title: String,
    @SerializedName("AddressLine1") val address: String,
    @SerializedName("Town") val town: String,
    @SerializedName("Latitude") val latitude: Double,
    @SerializedName("Longitude") val longitude: Double
)