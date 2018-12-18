package gyqx.mdqc.myGoods.service;


import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.mdqc.myGoods.vo.BasGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.BasMfrsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoodsInfoVo;
import gyqx.mdqc.po.ProvGoodsInfo;

import java.util.List;

public interface IProvGoodsInfoService
{
	void deleteProvGood(ProvGoodsInfo good) throws Exception;
	int multDelProvGoods(List<ProvGoodsInfo> goods) throws Exception;
	ProvGoodsInfo add(ProvGoodsInfoVo provGoodsInfoVo) throws Exception ;
	ProvGoodsInfo update(ProvGoodsInfoVo provGoodsInfoVo) throws Exception;
	QueryResult<ProvGoodsInfoVo> listByPage(QueryInfo<ProvGoodsInfoVo> queryInfo) throws Exception;
	QueryResult<BasGoodsInfoVo> listBasGoods(QueryInfo<BasGoodsInfoVo> queryInfo) throws Exception;
	QueryResult<BasMfrsInfoVo> getBasMfrsInfoList(QueryInfo<BasMfrsInfoVo> queryInfo) throws Exception;
	ProvGoodsInfo convertBasGood2ProvGood(BasGoodsInfoVo basGoodVo, ProvGoodsInfo provGood) throws Exception;
	void addProvGoodS(List<ProvGoodsInfo> goods) throws Exception;
}


