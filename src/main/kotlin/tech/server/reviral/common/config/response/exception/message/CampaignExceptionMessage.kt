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
    const val CAMPAIGN_EXIST_JOIN_PEOPLE = "등록한 인원이 존재합니다. 삭제할 수 없습니다."
    const val CAMPAIGN_DO_NOT_DELETE = "진행중이거나 완료된 캠페인은 삭제할 수 없습니다."
    const val CAMPAIGN_STATUS_PROGRESS = "진행중인 캠페인에는 참여 하실 수 없습니다."
    const val CAMPAIGN_MODIFY_IMPOSSIBLE = "캠페인이 시작되었다면, 수정이 불가능합니다."
    const val CAMPAIGN_ENROLL_NOT_MATCH = "캠페인 참여자와 일치하지 않습니다."
    const val CAMPAIGN_STATUS_NOT_WAIT = "캠페인이 대기중인 상태만 변경가능합니다."
    const val CAMPAIGN_STATUS_WAIT = "모집 예정인 캠페인입니다. 다음에 신청해주세요."
    const val CAMPAIGN_CP_ID_NULL = "쿠팡 아이디를 등록해주세요."
    const val CAMPAIGN_NV_ID_NULL = "네이버 아이디를 등록해주세요."
    const val CAMPAIGN_CANCEL_JOIN = "캠페인을 취소한 당일날은 신청이 불가능합니다."
    const val CAMPAIGN_IS_NOT_DUPLICATED = "해당 캠페인은 중복으로 진행이 불가능합니다."
    const val CAMPAIGN_START_NOT_YET = "해당 캠페인은 아직 다시할 수 없습니다."
    const val CAMPAIGN_FULL_RECRUIT = "해당 캠페인 모집인원이 마감되었습니다."
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