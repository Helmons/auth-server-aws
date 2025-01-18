package br.pucpr.authserver.users.results

import br.pucpr.authserver.users.LoginStatus
import br.pucpr.authserver.users.responses.LoginResponse

data class LoginResult(
    val status: LoginStatus,
    val response: LoginResponse? = null
)