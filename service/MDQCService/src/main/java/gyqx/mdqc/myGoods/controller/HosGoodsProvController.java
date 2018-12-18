package gyqx.mdqc.myGoods.controller;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.utils.UserOnlineStateUtils;
import common.web.AjaxResult;
import common.web.BaseController;
import gyqx.mdqc.myGoods.service.HosGoodsProvService;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.spdhdi.myInfos.vo.ProvHosInfoVo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-11-6 下午4:24
 */
@RestController
@RequestMapping("/hosGoodsProv")
public class HosGoodsProvController extends BaseController {
    @Resource
    private HosGoodsProvService service;
    @Resource
    private UserOnlineStateUtils userOnline;

    /**
     * 医院查询供应商列表信息
     */
    @GetMapping(value = "hosProvInfoVoList")
    @ResponseBody
    public AjaxResult<List<Map<String, String>>> getHosProvInfoVoList() {
        AjaxResult<List<Map<String, String>>> result = new AjaxResult<>();
        result.setData(service.getHosProvInfoVoList());
        return result;
    }

    @PostMapping(value = "getPHGoodsList")
    @ResponseBody
    public AjaxResult<QueryResult<ProvGoods4HosVo>> getPHGoodsList(@RequestBody QueryInfo<ProvGoods4HosVo> queryInfo) {
        AjaxResult<QueryResult<ProvGoods4HosVo>> result = new AjaxResult<>();
        queryInfo.getQueryObject().setHosId(userOnline.getCurrent().getCorpId());
        QueryResult<ProvGoods4HosVo> lstGoods = service.getProGoods4HosList(queryInfo);
        result.setData(lstGoods);
        return result;
    }
}
