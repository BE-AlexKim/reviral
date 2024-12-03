package tech.server.reviral.api.campaign.controller

import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.tags.Tags
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import tech.server.reviral.api.campaign.service.CampaignService
import tech.server.reviral.common.config.response.success.WrapResponseEntity

/**
 *packageName    : tech.server.reviral.api.campaign.controller
 * fileName       : CampaignController
 * author         : joy58
 * date           : 2024-11-19
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-11-19        joy58       최초 생성
 */
@RestController
@RequestMapping("/api/v1/campaign")
@Tag(name = "캠페인", description = "캠페인 정보 API")
class CampaignController constructor(
    private val campaignService: CampaignService
){

    /**
     * 캠페인 정보 다건 조회
     */
    @GetMapping
    fun getCampaigns():ResponseEntity<WrapResponseEntity<Boolean>> {
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "hello", true)
    }

    /**
     * 캠페인 정보 단건 조회
     */
    @GetMapping("/{campaignId}")
    fun getCampaign(@PathVariable campaignId: Long): ResponseEntity<WrapResponseEntity<Boolean>> {
        return WrapResponseEntity.toResponseEntity(HttpStatus.OK, "hello", true)
    }

    /**
     * 캠페인 정보 저장
     */
    @PostMapping("/save")
    fun save(@RequestBody hello: String): ResponseEntity<String> {
        return ResponseEntity.ok("OK")
    }

    /**
     * 캠페인 정보 단건 수정
     */
    @PutMapping("/{campaignId}")
    fun updateCampaign(@PathVariable campaignId: Long) {

    }

}