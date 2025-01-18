package br.pucpr.authserver.users.confirm

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConfirmationRepository : JpaRepository<Confirmation, Long> {
    fun findByPhoneAndUuid(phone: String, uuid: String): Confirmation?
}