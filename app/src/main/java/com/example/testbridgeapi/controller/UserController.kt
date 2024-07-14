import com.example.testbridgeapi.controller.dto.req.UserReqDto
import com.example.testbridgeapi.controller.dto.res.UserResDto
import com.example.testbridgeapi.controller.dto.res.toResDto
import com.example.testbridgeapi.core.service.UserService
import dto.res.ApiCommonResponse
import io.github.shiniseong.bridgeApi.annotation.method.*
import io.github.shiniseong.bridgeApi.annotation.param.*


class UserController(private val userService: UserService) {
    @Get("/:id")
    fun getUserById(@PathVariable("id") id: Long): ApiCommonResponse<UserResDto> {
        val user = userService.getUserById(id)
        return ApiCommonResponse(
            status = 0,
            message = "success",
            data = user.toResDto(),
        )
    }

    @Post("")
    fun createUser(@JsonBody newUser: UserReqDto): ApiCommonResponse<UserResDto> {
        val createdUser = userService.createUser(newUser.toDomain())
        return ApiCommonResponse(
            status = 0,
            message = "success",
            data = createdUser.toResDto(),
        )
    }

    @Patch("/:id/user-type")
    fun updateUserAge(@PathVariable("id") id: Long, @JsonBody userReq: UserReqDto): ApiCommonResponse<UserResDto> {
        val updatedUser = userService.updateUserType(id, userReq.toDomain())
        return ApiCommonResponse(
            status = 0,
            message = "success",
            data = updatedUser.toResDto()
        )
    }

    @Delete("/:id")
    fun deleteUser(@PathVariable("id") id: Long): ApiCommonResponse<Unit> {
        userService.deleteUser(id)
        return ApiCommonResponse(
            status = 0,
            message = "success",
            data = Unit,
        )
    }

    @Get("all")
    fun getAllUsers(@Query("order") order: String): ApiCommonResponse<List<UserResDto>> {
        val users = userService.getAllUsers(order)
        return ApiCommonResponse(
            status = 0,
            message = "success",
            data = users.map { it.toResDto() }
        )
    }

    @Get("test/exception/service")
    fun testExceptionService(): ApiCommonResponse<Unit> {
        userService.testExceptionService()
        return ApiCommonResponse(status = 0, message = "success", data = Unit)
    }

    @Get("test/exception/general")
    fun testExceptionGeneral(): ApiCommonResponse<Unit> {
        userService.testExceptionGeneral()
        return ApiCommonResponse(status = 0, message = "success", data = Unit)
    }

    @Get("test/time-out")
    fun testTimeOut(): ApiCommonResponse<Unit> {
        // time measurement
        val start = System.currentTimeMillis()
        for (i in 1..10000) {
            println(i)
        }
        val end = System.currentTimeMillis()
        println("time: ${end - start}")
        return ApiCommonResponse(status = 0, message = "success", data = Unit)
    }
}