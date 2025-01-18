package br.pucpr.authserver.users.requests

data class PhoneLoginRequest(
    val phone: String,
    val uuid: String
)