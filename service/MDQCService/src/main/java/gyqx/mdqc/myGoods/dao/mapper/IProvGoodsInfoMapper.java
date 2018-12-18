package gyqx.mdqc.myGoods.dao.mapper;

import gyqx.mdqc.myGoods.vo.BasGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.BasMfrsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoodsInfoVo;
import gyqx.mdqc.po.ProvGoodsInfo;

import java.util.List;


public interface IProvGoodsInfoMapper {
	int update(ProvGoodsInfo bean);
	int insert(ProvGoodsInfo bean);
	List<ProvGoodsInfoVo> list(ProvGoodsInfoVo queryInfo);
	List<BasMfrsInfoVo> getBasMfrsInfoList(BasMfrsInfoVo basMfrsInfo);
	int deleteByPKey(ProvGoodsInfo bean);
	List<BasGoodsInfoVo> getBasGoodsInfoList(BasGoodsInfoVo bean);
}

