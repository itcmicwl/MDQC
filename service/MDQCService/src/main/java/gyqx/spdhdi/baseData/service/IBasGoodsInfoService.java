package gyqx.spdhdi.baseData.service;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import gyqx.spdhdi.mycerts.vo.JxsqsGoodsVo;

public interface IBasGoodsInfoService {
	
	public QueryResult<BasGoodsInfoVo> listBasGoods(QueryInfo<JxsqsGoodsVo> queryInfo) throws Exception;

}
