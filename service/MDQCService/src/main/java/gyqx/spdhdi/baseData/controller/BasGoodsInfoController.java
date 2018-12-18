package gyqx.spdhdi.baseData.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.web.AjaxResult;
import common.web.BaseController;
import gyqx.spdhdi.baseData.service.IBasGoodsInfoService;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import gyqx.spdhdi.mycerts.vo.JxsqsGoodsVo;

@Controller
@RequestMapping(value = "bas/goods")
public class BasGoodsInfoController extends BaseController {
	@Resource
	private IBasGoodsInfoService basGoodsInfoService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping(value = "getGoodsInfos")
	@ResponseBody
	public AjaxResult getGoodsInfos(HttpServletRequest request, HttpServletResponse response,
			@RequestBody QueryInfo<JxsqsGoodsVo> queryInfo) throws Exception {
		QueryResult<BasGoodsInfoVo> queryResult = basGoodsInfoService.listBasGoods(queryInfo);
		AjaxResult result = new AjaxResult();
		result.setData(queryResult);
		return result;
	}

}
