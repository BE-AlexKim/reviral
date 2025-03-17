package tech.server.reviral.view.home.controller

import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import tech.server.reviral.view.home.service.IndexService

/**
 *packageName    : tech.server.reviral.view.home
 * fileName       : HomeController
 * author         : joy58
 * date           : 2025-02-14
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2025-02-14        joy58       최초 생성
 */
@Controller
class IndexController constructor(
    private val indexService: IndexService
) {

//    @GetMapping("/")
//    fun index(
//        model: Model,
//        @RequestParam(defaultValue = "0") page: Int,
//        @RequestParam(defaultValue = "20") size: Int
//    ): String {
//        val pageable = PageRequest.of(page,size)
////        val campaignList = indexService.findAll(pageable)
//
////        model.addAttribute("campaignList", campaignList)
//
//        return "index"
//    }

    @GetMapping("/campaign/register")
    fun campaignRegister(): String {
        return "campaignRegister"
    }
}