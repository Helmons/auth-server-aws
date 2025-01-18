package br.pucpr.authserver.users

import br.pucpr.authserver.users.requests.ConfirmationRequest
import br.pucpr.authserver.users.requests.LoginRequest
import br.pucpr.authserver.users.requests.PhoneLoginRequest
import br.pucpr.authserver.users.requests.UserRequest
import br.pucpr.authserver.users.responses.UserResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {
    @PostMapping("/login")
    fun loginByPhone(@Valid @RequestBody login: PhoneLoginRequest): ResponseEntity<Any> {
        val result = userService.loginByPhone(login.phone, login.uuid)
        return when (result.status) {
            LoginStatus.SUCCESS -> ResponseEntity.ok(result.response)
            LoginStatus.SMS_SENT -> ResponseEntity.status(HttpStatus.ACCEPTED).body("SMS sent for confirmation")
            else -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
    }

    @PostMapping("/confirm")
    fun confirmUser(@Valid @RequestBody confirmation: ConfirmationRequest): ResponseEntity<UserResponse> {
        val user = userService.confirmUser(confirmation)
        return if (user != null) ResponseEntity.ok(UserResponse(user))
        else ResponseEntity.status(HttpStatus.NOT_FOUND).build()
    }

    @PutMapping("/{id}")
    fun updateUser(
        @PathVariable id: Long,
        @RequestBody @Valid updatedUser: UserRequest
    ): ResponseEntity<UserResponse> {
        val user = userService.updateUser(id, updatedUser.toUser())
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok(UserResponse(user))
    }
}