package tech.server.reviral.common.config.response.success

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import java.time.LocalDateTime

/**
 *packageName    : tech.server.reviral.common.config.response.success
 * fileName       : ResponseEntity
 * author         : joy58
 * date           : 2024-11-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-20        joy58       최초 생성
 */
class WrapResponseEntity<T>(
    val status: Int = 200,
    val data: MutableMap<String,T>,
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    val timestamp: LocalDateTime = LocalDateTime.now()
) {

    companion object {
        fun <T> toResponseEntity( status: HttpStatus, key: String, data: T ): ResponseEntity<WrapResponseEntity<T>> {
            return ResponseEntity.status(status)
                .body(WrapResponseEntity(
                    status = status.value(),
                    data = mutableMapOf(key to data))
                )
        }
    }

}