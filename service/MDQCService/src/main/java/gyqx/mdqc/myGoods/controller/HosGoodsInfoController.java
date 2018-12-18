package gyqx.mdqc.myGoods.controller;

import javax.annotation.Resource;
import javax.validation.Valid;
import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.transform.Tx;
import common.utils.UtilsContext;
import gyqx.mdqc.myGoods.service.IHosGoodsInfoService;
import gyqx.mdqc.myGoods.vo.HosGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.mdqc.myGoods.vo.ProvHosInfoVo;
import gyqx.mdqc.myGoods.vo.ProvIdAndNameVo;
import gyqx.mdqc.po.HosGoodsInfo;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import common.web.AjaxResult;
import common.web.BaseController;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "base/hosGoodsInfo")
public class HosGoodsInfoController extends BaseController 
{
	@Resource
	private IHosGoodsInfoService hosGoodsInfoService;
	@Resource
	private UtilsContext utilsContext;

	@RequestMapping(value = "deleteHosGoodsInfo")
	@ResponseBody
	public AjaxResult delGoodsInfo(@RequestBody @Valid HosGoodsInfoVo hosGood, Errors error) throws Exception {
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		HosGoodsInfo hosGoodsInfo = new HosGoodsInfo();
		Tx.transform(hosGood).to(hosGoodsInfo);
		hosGoodsInfoService.deleteHosGood(hosGoodsInfo);
		return result;
	}

	@RequestMapping(value = "multDel")
	@ResponseBody
	public AjaxResult multDelGoodsInfo(@RequestBody ArrayList<HosGoodsInfoVo> hosGoods) throws Exception {
		AjaxResult result = new AjaxResult();
		List<HosGoodsInfo> goods = new ArrayList<>();
		for(HosGoodsInfoVo hsv:hosGoods ){
			HosGoodsInfo hp = new HosGoodsInfo();
			Tx.transform(hsv).to(hp);
			goods.add(hp);
		}
		int re =hosGoodsInfoService.multDelProvGoods(goods);
		if(re>0){
			result.setCode(re);
			String msg= "数据删除成功！";
			result.setMsg("您选择的"+hosGoods.size()+"条记录中有"+re+"条"+msg);
		}
		return result;
	}

	@RequestMapping(value = "add")
	@ResponseBody
	public AjaxResult add(@RequestBody @Valid HosGoodsInfoVo hosGoodsInfoVo , Errors error  )  throws  Exception
	{
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		HosGoodsInfo ret = hosGoodsInfoService.add(hosGoodsInfoVo);
		result.setData(ret);
		return result;
	}

	@RequestMapping(value = "update")
	@ResponseBody
	public AjaxResult update(@RequestBody @Valid HosGoodsInfoVo hosGoodsInfoVo ,Errors error  )  throws  Exception
	{
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		HosGoodsInfo ret = hosGoodsInfoService.update(hosGoodsInfoVo);
		result.setData(ret);
		return result;
	}

	@RequestMapping(value = "listByPage")
	@ResponseBody
	public AjaxResult listByPage(@RequestBody @Valid QueryInfo<HosGoodsInfoVo> queryInfo , Errors error  )  throws  Exception
	{
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		QueryResult<HosGoodsInfoVo> ret = hosGoodsInfoService.listByPage(queryInfo);
		result.setData(ret);
		return result;
	}

	@RequestMapping(value = "goodImprotFromBasGoods")
	@ResponseBody
	public AjaxResult goodImprotFromBasGoods(@RequestBody ArrayList<BasGoodsInfoVo> basGoods) throws Exception {
		AjaxResult result = new AjaxResult();
		List<HosGoodsInfo> hosGoods = new ArrayList<>();
		for(BasGoodsInfoVo basGoodVo:basGoods){
			HosGoodsInfo hosGood = new HosGoodsInfo();
			hosGood = hosGoodsInfoService.convertBasGood2HosGood(basGoodVo, hosGood);
			hosGoods.add(hosGood);
		}
		hosGoodsInfoService.addHosGoodS(hosGoods);
		return result;
	}

	@RequestMapping(value = "goodImprotFromPHGoods")
	@ResponseBody
	public AjaxResult goodImprotFromPHGoods(@RequestBody ArrayList<ProvGoods4HosVo> phGoodsVos)
			throws Exception {
		AjaxResult<List<String>> result = new AjaxResult<>();
		List<HosGoodsInfo> hosGoods = new ArrayList<>();
		for(ProvGoods4HosVo phGoodVo:phGoodsVos){
			HosGoodsInfo hosGood = new HosGoodsInfo();
			hosGood = hosGoodsInfoService.convertPHGood2HosGood(phGoodVo, hosGood);
			hosGoods.add(hosGood);
		}
		result.setData(hosGoodsInfoService.addHosGoodS(hosGoods));
		return result;
	}

	@RequestMapping(value = "getPHGoodsList")    //获取供货列表供批量导入
	@ResponseBody
	public AjaxResult<QueryResult<ProvGoods4HosVo>> getPHGoodsList(@RequestBody QueryInfo<ProvGoods4HosVo> queryInfo)
			throws Exception {
		AjaxResult<QueryResult<ProvGoods4HosVo>> result = new AjaxResult<QueryResult<ProvGoods4HosVo>>();
		queryInfo.getQueryObject().setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
		//queryInfo.getQueryObject().setHosId("h0001");
		QueryResult<ProvGoods4HosVo> lstGoods = hosGoodsInfoService.getProGoods4HosList(queryInfo);
		result.setData(lstGoods);
		return result;
	}

	@RequestMapping(value = "basProvInfoList")
	@ResponseBody
	public AjaxResult listProvInfo(@RequestBody @Valid HosGoodsInfoVo hosGoodsInfoVo , Errors error  )  throws  Exception
	{
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		List<ProvIdAndNameVo> ret = hosGoodsInfoService.list(hosGoodsInfoVo);
		result.setData(ret);
		return result;
	}

	/**
	 * 医院查询供应商列表信息
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "hosProvInfoVoList")
	@ResponseBody
	public AjaxResult<QueryResult<ProvHosInfoVo>> getHosProvInfoVoList(@RequestBody QueryInfo<ProvHosInfoVo> queryInfo)
			throws Exception {
		AjaxResult<QueryResult<ProvHosInfoVo>> result = new AjaxResult<QueryResult<ProvHosInfoVo>>();
		QueryResult<ProvHosInfoVo> lstProvHosInfoVoList = hosGoodsInfoService.getHosProvInfoVoList(queryInfo);
		result.setData(lstProvHosInfoVoList);
		return result;
	}
}
