package tech.server.reviral.common.config.response.exception.message

/**
 *packageName    : tech.server.reviral.common.config.response.exception.message
 * fileName       : CampaignExceptionMessage
 * author         : joy58
 * date           : 2024-12-03
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2024-12-03        joy58       최초 생성
 */
object CampaignExceptionMessage {
    const val CAMPAIGN_DO_NOT_CANCEL = "해당 캠페인을 취소할 수 없습니다."
    const val CAMPAIGN_REVIEW = "해당 캠페인의 리뷰를 이미 작성했거나, 작성할 수 없습니다."
    const val CAMPAIGN_ORDER_NO = "상품 주문번호를 이미 작성했거나, 작성할 수 없습니다."
    const val REGISTER_CAMPAIGN_NOT_EXIST = "해당 캠페인은 참여중이지 않습니다."
    const val SUB_OPTION_IS_NOT_CONTAIN_OPTION = "해당 옵션에 맞지 않는 하위 옵션값입니다."
    const val SUB_OPTION_IS_NOT_EMPTY = "하위 옵션을 선택해주세요."
    const val CAMPAIGN_NOT_COMPLETED = "해당 캠페인을 완료 후, 신청해 주세요."
    const val CAMPAIGN_IS_NOT_EXIST = "해당 캠페인이 존재하지 않습니다."
    const val OPTION_IS_NOT_EMPTY = "옵션을 선택해주세요."
    const val START_DATE_SET = "캠페인 시작일과 종료일은 시작일이 이 더 빨라야합니다."
    const val DEFAULT = "알 수 없는 오류"
}