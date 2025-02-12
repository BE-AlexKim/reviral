package tech.server.reviral.api.bussiness.model.enums

/**
 *packageName    : tech.server.reviral.api.bussiness.model.enums
 * fileName       : ImageCheck
 * author         : joy58
 * date           : 2025-01-31
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-01-31        joy58       최초 생성
 */
enum class ImageCheck(private val desc: String) {
    review("리뷰 이미지 확인"), modify("리뷰 이미지 재요청"),order("주문 이미지 확인"),reorder("주문이미지 재요청");
}