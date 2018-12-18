package gyqx.mdqc.myGoods.service;


import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.mdqc.myGoods.vo.HosGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.mdqc.myGoods.vo.ProvHosInfoVo;
import gyqx.mdqc.myGoods.vo.ProvIdAndNameVo;
import gyqx.mdqc.po.HosGoodsInfo;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;

import java.util.List;

public interface IHosGoodsInfoService 
{
	void deleteHosGood(HosGoodsInfo good) throws Exception;
	int multDelProvGoods(List<HosGoodsInfo> goods) throws Exception;
	HosGoodsInfo add(HosGoodsInfoVo hosGoodsInfoVo) throws Exception ;
	HosGoodsInfo update(HosGoodsInfoVo hosGoodsInfoVo) throws Exception;
	List<ProvIdAndNameVo> list(HosGoodsInfoVo bean) throws Exception;
	QueryResult<HosGoodsInfoVo> listByPage(QueryInfo<HosGoodsInfoVo> queryInfo) throws Exception;
	List<String> addHosGoodS(List<HosGoodsInfo> goods) throws Exception;
	HosGoodsInfo convertBasGood2HosGood(BasGoodsInfoVo basGood, HosGoodsInfo hosGood);
	HosGoodsInfo convertPHGood2HosGood(ProvGoods4HosVo phGoodVo, HosGoodsInfo hosGood);
	QueryResult<ProvGoods4HosVo> getProGoods4HosList(QueryInfo<ProvGoods4HosVo> queryInfo )throws Exception;
	QueryResult<ProvHosInfoVo> getHosProvInfoVoList(QueryInfo<ProvHosInfoVo> queryInfo) throws Exception;
}


