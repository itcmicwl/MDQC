package gyqx.spdhdi.baseData.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import gyqx.spdhdi.baseData.dao.mapper.IBasGoodsInfoMapper;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import gyqx.spdhdi.mycerts.vo.JxsqsGoodsVo;

@Repository
public class BasGoodsInfoDao {
	@Resource
	private IBasGoodsInfoMapper mapper;
	public List<BasGoodsInfoVo> getBasGoodsInfoList(JxsqsGoodsVo good) throws Exception {
		return mapper.getBasGoodsInfoList(good);
	}
}
