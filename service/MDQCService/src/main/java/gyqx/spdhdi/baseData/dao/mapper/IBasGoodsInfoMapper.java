package gyqx.spdhdi.baseData.dao.mapper;

import java.util.List;

import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import gyqx.spdhdi.mycerts.vo.JxsqsGoodsVo;

public interface IBasGoodsInfoMapper {
	List<BasGoodsInfoVo> getBasGoodsInfoList(JxsqsGoodsVo com);
}