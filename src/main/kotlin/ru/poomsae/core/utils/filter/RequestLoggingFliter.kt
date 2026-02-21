package ru.poomsae.core

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class RequestLoggingFilter : OncePerRequestFilter() {
  private val log = LoggerFactory.getLogger(this::class.java)

  override fun doFilterInternal(
      request: HttpServletRequest,
      response: HttpServletResponse,
      filterChain: FilterChain,
  ) {
    val start = System.currentTimeMillis()
    filterChain.doFilter(request, response)
    val duration = System.currentTimeMillis() - start

    log.info("${request.method} ${request.requestURI} -> ${response.status} (${duration}ms)")
  }
}
