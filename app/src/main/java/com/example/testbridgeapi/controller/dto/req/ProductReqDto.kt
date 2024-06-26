package dto.req

import com.example.testbridgeapi.core.domain.Product

data class ProductReqDto(
    val name: String,
    val price: Int,
    val stock: Int,
) {
    fun toDomain(id: Long): Product = Product(
        id = id,
        name = name,
        price = price,
        stock = stock,
    )
}
