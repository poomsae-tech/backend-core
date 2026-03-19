package ru.poomsae.core.domain

import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

data class AuthenticatedUser(val jwt: Jwt) {
    val userId: String get() = jwt.subject
    val username: String get() = jwt.getClaimAsString("preferred_username") ?: ""
    val email: String get() = jwt.getClaimAsString("email") ?: ""
    val orgId: String? get() = jwt.getClaimAsString("org_id")
    val roles: List<String> get() = jwt.getClaimAsStringList("roles") ?: emptyList()
    val judgeType: String? get() = jwt.getClaimAsString("judge_type")

    fun hasRole(role: String): Boolean = roles.contains(role)
    fun isJudge(): Boolean = hasRole("judge")
    fun isSuperAdmin(): Boolean = hasRole("superadmin")
}

fun JwtAuthenticationToken.toAuthenticatedUser(): AuthenticatedUser = AuthenticatedUser(this.token as Jwt)
