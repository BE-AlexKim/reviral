package tech.server.reviral.common.config.aws

import com.amazonaws.SdkClientException
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.AmazonS3Exception
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import tech.server.reviral.common.config.response.exception.BasicException
import tech.server.reviral.common.config.response.exception.enums.BasicError
import java.io.IOException
import java.util.*


/**
 *packageName    : tech.server.reviral.common.config.aws
 * fileName       : S3Service
 * author         : joy58
 * date           : 2025-01-07
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-07        joy58       최초 생성
 */
@Service
class AmazonS3Service constructor(
    private val amazonS3: AmazonS3
){
    @Value("\${cloud.aws.s3.bucket}")
    private lateinit var bucket: String

    private val log = KotlinLogging.logger {}

    /**
     * S3 파일 업로드
     * @param file: MultipartFile
     * @return fileUrl: String
     * @exception BasicException
     */
    @Transactional
    @Throws(BasicException::class)
    fun uploadMultipartFile(file: MultipartFile, path: String): UploadFile {

        val maxSize = 5 * 1024 * 1024

        if ( file.size > maxSize ) throw BasicException(BasicError.FILE_SIZE_FULL)

        val filename = generatedRandomFilename(file.originalFilename ?: "")
        log.info { "Amazon S3 업로드 파일 명 : $filename" }

        val metadata = ObjectMetadata()
        metadata.contentLength = file.size
        metadata.contentType = file.contentType

        try {
            val putObjectRequest = PutObjectRequest(bucket, "$path/$filename", file.inputStream, metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead) // 퍼블릭 읽기 권한 추가

            amazonS3.putObject(putObjectRequest)

        } catch (e: AmazonS3Exception) {
            log.error { "파일 업로드 S3 관련 오류 발생: ${e.message}" }
            throw BasicException(BasicError.FILE_UPLOAD_FAIL)
        } catch (e: SdkClientException) {
            log.error {"Amazon SDK 관련 오류 발생 : ${e.message}"}
            throw BasicException(BasicError.FILE_UPLOAD_FAIL)
        } catch (e: IOException) {
            log.error {"IO 관련 오류 발생:  + ${e.message}" }
            throw BasicException(BasicError.FILE_UPLOAD_FAIL)
        }

        val url = amazonS3.getUrl(bucket, "${path}/${filename}").toString()

        return UploadFile(
            originalFilename = file.originalFilename ?: filename,
            uploadFilename = filename,
            imageUrl = url,
        )
    }

    private fun generatedRandomFilename(originalFilename: String): String {
        val extension = isFileExtension(originalFilename)
        return "${UUID.randomUUID()}.$extension"
    }

    /**
     * 파일 확장자 유효성 체크
     * @param originalFilename: String
     * @return fileExtension: String
     * @exception BasicException
     */
    @Throws(BasicException::class)
    private fun isFileExtension(originalFilename: String): String {
        val fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).lowercase()
        val allowedExtensions = arrayListOf("jpg","png","gif","jpeg","tiff")

        if ( !allowedExtensions.contains(fileExtension) ) {
            throw BasicException(BasicError.FILE_IMG_EXTENSION)
        }else {
            return fileExtension
        }
    }
}