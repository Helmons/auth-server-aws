package br.pucpr.authserver.users

import br.pucpr.authserver.roles.Role
import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.jetbrains.annotations.NotNull

@Entity
@Table(name = "tblUser")
class User(
    @Id @GeneratedValue
    var id: Long? = null,
    @Column(unique = true, nullable = false)
    var phone: String = "",
    @Column(unique = true, nullable = true)
    var uuid: String? = null,
    var name: String = "",
    var email: String = "",
    var password: String = "",

    @ManyToMany
    val roles: MutableSet<Role> = mutableSetOf()
)