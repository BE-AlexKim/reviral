package tech.server.reviral.common.config.docs.bussiness

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.bussiness
 * fileName       : DeleteCampaignDetailExplain
 * author         : joy58
 * date           : 2025-02-13
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-13        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 상세정보 삭제 요청",
    description = "해당 캠페인의 상세정보를 삭제합니다."
)
@ApiResponses(
    ApiResponse(
        responseCode = "200",
        description = "OK",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "통신 성공",
                        value = """
                            {
                              "isDeleted": true
                            }
                        """
                    )
                ]
            )
        ]
    ),ApiResponse(
        responseCode = "400",
        description = "BAD_REQUEST",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 상세정보 오류",
                        description = "캠페인 상세정보가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0004",
                                "message": "해당 캠페인이 존재하지 않습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            ),Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "등록 인원 존재 오류",
                        description = "등록한 인원이 존재합니다. 삭제할 수 없습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0024",
                                "message": "등록한 인원이 존재합니다. 삭제할 수 없습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            ),Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "삭제 오류",
                        description = "진행 또는 완료된 캠페인 삭제 오류",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0023",
                                "message": "진행중이거나 완료된 캠페인은 삭제할 수 없습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    ),ApiResponse(
        responseCode = "500",
        description = "INTERNAL_SERVER_ERROR",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "",
                        description = "",
                        value = """
                            {
                                "status": 500,
                                "code": "ME0001",
                                "message": "서버 오류",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class DeleteCampaignDetailExplain()
