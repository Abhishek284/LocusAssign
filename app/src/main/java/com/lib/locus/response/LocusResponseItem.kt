import com.google.gson.annotations.SerializedName

data class LocusResponseItem(
    @SerializedName("type")
    val type: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("dataMap")
    val dataMap: DataMap?
)