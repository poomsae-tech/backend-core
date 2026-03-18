package ru.poomsae.core.driver.http

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ru.poomsae.core.domain.AuthenticatedUser

@RestController
class MessageController {

    @GetMapping("/api/me")
    fun me(@AuthenticationPrincipal jwt: Jwt): Map<String, Any?> {
        val user = AuthenticatedUser(jwt)
        return mapOf(
            "userId" to user.userId,
            "username" to user.username,
            "email" to user.email,
            "roles" to user.roles,
            "orgId" to user.orgId,
            "judgeType" to user.judgeType,
        )
    }
}

