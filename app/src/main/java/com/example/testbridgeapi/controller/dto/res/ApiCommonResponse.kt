package dto.res

data class ApiCommonResponse<T>(
    val status: Int,
    val message: String,
    val data: T,
)
