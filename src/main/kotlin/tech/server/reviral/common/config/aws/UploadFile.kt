package tech.server.reviral.common.config.aws

/**
 *packageName    : tech.server.reviral.common.config.aws
 * fileName       : UploadFile
 * author         : joy58
 * date           : 2025-02-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-03        joy58       최초 생성
 */
data class UploadFile(
    val originalFilename: String,
    val uploadFilename: String,
    val imageUrl: String
)
