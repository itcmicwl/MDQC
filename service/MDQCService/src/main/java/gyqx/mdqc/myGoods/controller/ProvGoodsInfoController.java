package gyqx.mdqc.myGoods.controller;


import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.transform.Tx;
import common.web.AjaxResult;
import common.web.BaseController;
import gyqx.mdqc.myGoods.service.IProvGoodsInfoService;
import gyqx.mdqc.myGoods.vo.BasGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.BasMfrsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoodsInfoVo;
import gyqx.mdqc.po.ProvGoodsInfo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "base/provGoodsInfo")
public class ProvGoodsInfoController extends BaseController {
    @Resource
    private IProvGoodsInfoService provGoodsInfoService;

    @RequestMapping(value = "add")
    @ResponseBody
    public AjaxResult add(@RequestBody @Valid ProvGoodsInfoVo provGoodsInfoVo, Errors error) throws Exception {
        AjaxResult result = new AjaxResult();
        filterErrors(error);
        ProvGoodsInfo ret = provGoodsInfoService.add(provGoodsInfoVo);
        result.setData(ret);
        return result;
    }

    @RequestMapping(value = "update")
    @ResponseBody
    public AjaxResult update(@RequestBody @Valid ProvGoodsInfoVo provGoodsInfoVo, Errors error) throws Exception {
        AjaxResult result = new AjaxResult();
        filterErrors(error);
        ProvGoodsInfo ret = provGoodsInfoService.update(provGoodsInfoVo);
        result.setData(ret);
        return result;
    }

    @RequestMapping(value = "deleteGoodsInfo")
    @ResponseBody
    public AjaxResult delGoodsInfo(@RequestBody @Valid ProvGoodsInfoVo provgood, Errors error) throws Exception {
        AjaxResult result = new AjaxResult();
        filterErrors(error);
        ProvGoodsInfo provGoodsInfo = new ProvGoodsInfo();
        Tx.transform(provgood).to(provGoodsInfo);
        provGoodsInfoService.deleteProvGood(provGoodsInfo);
        return result;
    }

    @RequestMapping(value = "multDel")
    @ResponseBody
    public AjaxResult multDelGoodsInfo(@RequestBody ArrayList<ProvGoodsInfoVo> provGoods) throws Exception {
        AjaxResult result = new AjaxResult();
        List<ProvGoodsInfo> goods = new ArrayList<>();
        for (ProvGoodsInfoVo pgv : provGoods) {
            ProvGoodsInfo pg = new ProvGoodsInfo();
            Tx.transform(pgv).to(pg);
            goods.add(pg);
        }
        int re = provGoodsInfoService.multDelProvGoods(goods);
        if (re > 0) {
            result.setCode(re);
            String msg = "数据删除成功！";
            result.setMsg("您选择的" + provGoods.size() + "条记录中有" + re + "条" + msg);
        }
        return result;
    }

    @RequestMapping(value = "listByPage")
    @ResponseBody
    public AjaxResult listByPage(@RequestBody @Valid QueryInfo<ProvGoodsInfoVo> queryInfo, Errors error) throws Exception {
        AjaxResult result = new AjaxResult();
        filterErrors(error);
        QueryResult<ProvGoodsInfoVo> ret = provGoodsInfoService.listByPage(queryInfo);
        result.setData(ret);
        return result;
    }

    @RequestMapping(value = "getGoodsInfos")
    @ResponseBody
    public AjaxResult getGoodsInfos(HttpServletRequest request, HttpServletResponse response,
                                    @RequestBody QueryInfo<BasGoodsInfoVo> queryInfo, Errors error) throws Exception {
        AjaxResult result = new AjaxResult();
        filterErrors(error);
        QueryResult<BasGoodsInfoVo> queryResult = provGoodsInfoService.listBasGoods(queryInfo);
        result.setData(queryResult);
        return result;
    }

    @RequestMapping(value = "basMfrsInfoList")
    @ResponseBody
    public AjaxResult<QueryResult<BasMfrsInfoVo>> getBasMfrsInfoList(@RequestBody QueryInfo<BasMfrsInfoVo> queryInfo) throws Exception {
        AjaxResult<QueryResult<BasMfrsInfoVo>> result = new AjaxResult<QueryResult<BasMfrsInfoVo>>();
        QueryResult<BasMfrsInfoVo> lstSysProject = provGoodsInfoService.getBasMfrsInfoList(queryInfo);
        result.setData(lstSysProject);
        return result;
    }

    @RequestMapping(value = "goodImprotFromBasGoods")
    @ResponseBody
    public AjaxResult goodImprotFromBasGoods(@RequestBody ArrayList<BasGoodsInfoVo> basGoods) throws Exception {
        AjaxResult result = new AjaxResult();
        List<ProvGoodsInfo> provGoods = new ArrayList<>();
        for (BasGoodsInfoVo basGoodVo : basGoods) {
            ProvGoodsInfo provGood = new ProvGoodsInfo();
            provGood = provGoodsInfoService.convertBasGood2ProvGood(basGoodVo, provGood);
            provGoods.add(provGood);
        }
        provGoodsInfoService.addProvGoodS(provGoods);
        return result;
    }
}
