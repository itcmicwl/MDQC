package gyqx.mdqc.mycerts.controller;

import common.web.AjaxResult;
import common.web.BaseController;
import gyqx.mdqc.mycerts.dao.mapper.HomeStatMapper;
import gyqx.mdqc.mycerts.service.HomeStatService;
import gyqx.mdqc.mycerts.vo.HomeStatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-8-28 上午9:57
 */
@RestController
@RequestMapping("/homestat")
public class HomeStatisticsController extends BaseController {
    @Autowired
    private HomeStatService service;

    @GetMapping("/getStat")
    AjaxResult getHomeStat(){
        AjaxResult<HomeStatVo> result = new AjaxResult<>();
        result.setData(service.getHomeStat());
        return result;
    }
}
