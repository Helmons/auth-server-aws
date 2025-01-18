package br.pucpr.authserver.users.confirm

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tblConfirmation")
class Confirmation(
    @Id @GeneratedValue
    var id: Long? = null,
    @Column(nullable = false)
    var phone: String = "",
    @Column(nullable = false)
    var uuid: String = "",
    @Column(nullable = false)
    var code: String = "",
    @Column(nullable = false)
    var expiration: LocalDateTime = LocalDateTime.now().plusMinutes(5)
)