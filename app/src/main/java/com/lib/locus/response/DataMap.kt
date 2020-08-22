
import com.google.gson.annotations.SerializedName

data class DataMap(
    @SerializedName("options")
    val options: List<String>?
)