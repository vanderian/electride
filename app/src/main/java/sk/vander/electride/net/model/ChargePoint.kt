package sk.vander.electride.net.model

import com.google.gson.annotations.SerializedName

data class ChargePoint(
    @SerializedName("ID") val id: Int,
    @SerializedName("AddressInfo") val addressInfo: AddressInfo
)
