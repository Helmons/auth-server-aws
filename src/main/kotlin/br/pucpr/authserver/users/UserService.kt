package br.pucpr.authserver.users

import br.pucpr.authserver.security.Jwt
import br.pucpr.authserver.users.confirm.Confirmation
import br.pucpr.authserver.users.confirm.ConfirmationRepository
import br.pucpr.authserver.users.requests.ConfirmationRequest
import br.pucpr.authserver.users.requests.UserRequest
import br.pucpr.authserver.users.responses.LoginResponse
import br.pucpr.authserver.users.responses.UserResponse
import br.pucpr.authserver.users.results.LoginResult
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class UserService(
    val userRepository: UserRepository,
    val confirmationRepository: ConfirmationRepository,
    val smsService: SmsService,
    val jwt: Jwt
) {
    fun loginByPhone(phone: String, uuid: String): LoginResult {
        val user = userRepository.findByPhone(phone)
        return if (user == null || user.uuid != uuid) {
            val code = smsService.sendConfirmationCode(phone)
            val confirmation = confirmationRepository.findByPhoneAndUuid(phone, uuid)
                ?: Confirmation(phone = phone, uuid = uuid, code = code)
            confirmation.code = code
            confirmation.expiration = LocalDateTime.now().plusMinutes(5)
            confirmationRepository.save(confirmation)
            LoginResult(LoginStatus.SMS_SENT)
        } else {
            LoginResult(LoginStatus.SUCCESS, LoginResponse(jwt.createToken(user), UserResponse(user)))
        }
    }

    fun confirmUser(request: ConfirmationRequest): User? {
        val confirmation = confirmationRepository.findByPhoneAndUuid(request.phone, request.uuid)
        if (confirmation == null || confirmation.code != request.code || confirmation.expiration.isBefore(LocalDateTime.now())) {
            return null
        }
        var user = userRepository.findByPhone(request.phone)
        if (user == null) {
            user = User(phone = request.phone, uuid = request.uuid) // Novo usuário
        } else {
            user.uuid = request.uuid // Atualizar UUID de usuário existente
        }
        userRepository.save(user)
        confirmationRepository.delete(confirmation) // Remover confirmação usada
        return user
    }

    fun updateUser(id: Long, updatedUser: User): User? {
        val existingUser = userRepository.findByIdOrNull(id)
            ?: return null

        existingUser.name = updatedUser.name
        existingUser.email = updatedUser.email
        existingUser.password = updatedUser.password

        return userRepository.save(existingUser)
    }
}