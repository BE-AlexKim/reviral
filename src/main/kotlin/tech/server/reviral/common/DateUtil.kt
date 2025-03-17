package tech.server.reviral.common

import tech.server.reviral.config.response.exception.BasicException
import tech.server.reviral.config.response.exception.CampaignException
import tech.server.reviral.config.response.exception.enums.CampaignError
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 *packageName    : tech.server.reviral.common.util
 * fileName       : DateUtil
 * author         : joy58
 * date           : 2025-02-12
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-12        joy58       최초 생성
 */
open class DateUtil {

    /**
     * 캠페인 시작일시 정보 조회
     * @param startDate: 시작일
     * @param endDate: 종료일
     * @param isNotWorkDay: 주말 포함 여부(true:미포함, false:포함)
     * @return 진행날짜 리스트
     */
    fun getWorkDays(startDate: LocalDate, endDate: LocalDate, isNotWorkDay: Boolean): List<LocalDate> {

        val endDays = if ( isNotWorkDay ) {
            adjustToNearestWeekDay(endDate)
        }else {
            endDate
        }

        val daysToAdd = getLocalDateBetween(startDate,endDays, isNotWorkDay)

        return getWorkDay(startDate,daysToAdd, isNotWorkDay)
    }

    /**
     * 시작일과 종료일의 날짜 차이값
     * @param startDate: 시작일
     * @param endDate: 종료일
     * @param isNotWorkDay: 주말 포함 여부(true:미포함, false:포함)
     * @return 날짜의 차이값
     */
    @Throws(BasicException::class)
    private fun getLocalDateBetween(startDate: LocalDate, endDate: LocalDate, isNotWorkDay: Boolean): Long {

        if (startDate.isAfter(endDate)) {
            throw CampaignException(CampaignError.START_DATE_SET)
        }

        return if (isNotWorkDay) {
            var date = startDate
            var workDays = 0L

            while (!date.isAfter(endDate)) { // Iterate through dates
                if (date.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
                    workDays++
                }
                date = date.plusDays(1) // Increment day
            }
            workDays
        } else {
            ChronoUnit.DAYS.between(startDate, endDate) // Include weekends
        }
    }

    /**
     * 시작일과 종료일 날짜
     * @param startDate: 시작일
     * @param targetWorkDays: 날짜의 차이값
     * @param isNotWorkDay: 주말 포함 여부(true:미포함, false:포함)
     * @return 진행날짜 리스트
     */
    private fun getWorkDay(startDate: LocalDate, targetWorkDays: Long, isNotWorkDay: Boolean): List<LocalDate> {
        val dayList = mutableListOf<LocalDate>()
        var date = startDate
        var workDayCount = 0L

        if ( isNotWorkDay ) {
            while (workDayCount < targetWorkDays) {
                if (date.dayOfWeek !in listOf(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY)) {
                    dayList.add(date)
                    workDayCount++
                }
                date = date.plusDays(1) // 하루 증가
            }
        }else {
            (workDayCount..targetWorkDays).forEach { i ->
                dayList.add(date.plusDays(i))
            }
        }
        return dayList
    }

    /**
     * 진행 날짜가 주말인지 아닌지에 따라 가장 가까운 영업일 값을 리턴
     * @param date: 조회하려는 날짜
     * @return 주말이 아닌 최근 날짜값
     */
    fun adjustToNearestWeekDay(date: LocalDate): LocalDate {
        return when ( date.dayOfWeek ) {
            DayOfWeek.SATURDAY -> date.plusDays(2)
            DayOfWeek.SUNDAY -> date.plusDays(1)
            else -> date
        }
    }

    fun adjustToBackNearestWeekDay(date: LocalDate): LocalDate {
        return when ( date.dayOfWeek ) {
            DayOfWeek.SUNDAY -> date.minusDays(2)
            DayOfWeek.SATURDAY -> date.minusDays(1)
            else -> date
        }
    }

}