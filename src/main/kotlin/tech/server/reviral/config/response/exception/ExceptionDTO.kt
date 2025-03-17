package tech.server.reviral.config.response.exception

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.common.config.response.exception
 * fileName       : ExceptionDTO
 * author         : joy58
 * date           : 2024-11-27
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-27        joy58       최초 생성
 */
data class ExceptionDTO(
    val status: Int,
    val code: String,
    val message: String?,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
)
