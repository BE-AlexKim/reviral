package tech.server.reviral.common.config.docs.campaign

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.enums.ParameterIn
import io.swagger.v3.oas.annotations.enums.ParameterStyle
import io.swagger.v3.oas.annotations.extensions.Extension
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.ExampleObject
import io.swagger.v3.oas.annotations.media.SchemaProperty
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.MediaType
import java.lang.annotation.Inherited

/**
 *packageName    : tech.server.reviral.common.config.docs.campaign
 * fileName       : UploadReviewExplain
 * author         : joy58
 * date           : 2025-01-08
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-08        joy58       최초 생성
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Inherited
@Operation(
    summary = "캠페인 후기 작성 이미지 업로드 API",
    description = "캠페인의 후기를 작성합니다."
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
                                "status": 200,
                                "data": {
                                    "isSave": true
                                },
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    ), ApiResponse(
        responseCode = "400",
        description = "BAD_REQUEST",
        content = [
            Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = [
                    ExampleObject(
                        name = "캠페인 리뷰 작성 오류",
                        description = "해당 캠페인의 리뷰를 이미 작성했거나, 작성할 수 없습니다.",
                        value = """
                            {
                              "status": 400,
                              "code": "CP0010",
                              "message": "해당 캠페인의 리뷰를 이미 작성했거나, 작성할 수 없습니다.",
                              "timestamp": "2024-12-05 12:52:50"
                            }
                        """
                    ),ExampleObject(
                        name = "캠페인 참여 오류",
                        description = "해당 캠페인 참여 일련번호가 존재하지 않습니다.",
                        value = """
                            {
                                "status": 400,
                                "code": "CP0008",
                                "message": "해당 캠페인은 참여중이지 않습니다.",
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
                        name = "서버 오류",
                        description = "서버가 정상적으로 작동하지 않습니다.",
                        value = """
                            {
                                "status": 500,
                                "code": "ME0001",
                                "message": "서버 오류",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    ),ExampleObject(
                        name = "파일 업로드 실패",
                        description = "파일이 정상적으로 업로드 되지 않았습니다.",
                        value = """
                            {
                                "status": 500,
                                "code": "BE0018",
                                "message": "파일 업로드에 실패하였습니다.",
                                "timestamp": "2024-11-27 23:59:59"
                            }
                        """
                    )
                ]
            )
        ]
    )
)
annotation class UploadReviewExplain
