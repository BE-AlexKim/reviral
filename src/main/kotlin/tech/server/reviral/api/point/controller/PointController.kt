package tech.server.reviral.api.point.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.point.model.dto.ExchangePointRequestDTO
import tech.server.reviral.api.point.service.PointService
import tech.server.reviral.common.config.docs.point.ExchangePointExplain
import tech.server.reviral.common.config.response.success.WrapResponseEntity

/**
 *packageName    : tech.server.reviral.api.point.controller
 * fileName       : PointController
 * author         : joy58
 * date           : 2024-12-30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-30        joy58       최초 생성
 */
@Tag(name = "포인트", description = "포인트 API")
@RestController
@RequestMapping("/api/v1/point")
class PointController constructor(
    private val pointService: PointService
){
    @ExchangePointExplain
    @PostMapping("/exchange")
    fun setExchangePoint(request: ExchangePointRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isExchange = pointService.setExchangePoint(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isExchange", isExchange)
    }
}