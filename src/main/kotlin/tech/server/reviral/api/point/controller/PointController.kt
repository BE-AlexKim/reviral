package tech.server.reviral.api.point.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tech.server.reviral.api.point.model.dto.ExchangePointRequestDTO
import tech.server.reviral.api.point.model.dto.PointAttributeResponseDTO
import tech.server.reviral.api.point.model.dto.PointExchangeResponseDTO
import tech.server.reviral.api.point.service.PointService
import tech.server.reviral.common.config.docs.point.ExchangePointExplain
import tech.server.reviral.common.config.docs.point.PointAttributeExplain
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
    @PostMapping("/exchange")
    @ExchangePointExplain
    fun setExchangePoint(@RequestBody request: ExchangePointRequestDTO): ResponseEntity<WrapResponseEntity<Boolean>> {
        val isExchange = pointService.setExchangePoint(request)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "isExchange", isExchange)
    }

    @GetMapping("/{userId}")
    @PointAttributeExplain
    fun getPointAttribute(@PathVariable userId: Long): ResponseEntity<WrapResponseEntity<PointAttributeResponseDTO>> {
        val pointAttribute = pointService.getPointAttributes(userId)
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK,"pointAttributes", pointAttribute)
    }

}