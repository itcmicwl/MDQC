package gyqx.mdqc.myGoods.dao;


import java.util.List;
import javax.annotation.Resource;

import common.utils.UtilsContext;
import gyqx.mdqc.myGoods.dao.mapper.IHosGoodsInfoMapper;
import gyqx.mdqc.myGoods.vo.HosGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.mdqc.myGoods.vo.ProvHosInfoVo;
import gyqx.mdqc.myGoods.vo.ProvIdAndNameVo;
import gyqx.mdqc.po.HosGoodsInfo;
import gyqx.mdqc.po.ProvHosInfo;
import org.springframework.stereotype.Repository;
import common.db.SimpleDao;

@Repository
public class HosGoodsInfoDao 
{
	@Resource
	private SimpleDao dao;
	@Resource
	private UtilsContext utilsContext;
	@Resource
	private IHosGoodsInfoMapper mapper;

	public HosGoodsInfo insertAndGet(HosGoodsInfo hosGoodsInfo) throws Exception
	{
		return dao.insertAndGet(hosGoodsInfo);
	}

	public HosGoodsInfo updateAndGet(HosGoodsInfo hosGoodsInfo) throws Exception {
		hosGoodsInfo.setFlag("1");
		return dao.updateAndGet(hosGoodsInfo);
	}

	public int deleteByPKey(HosGoodsInfo bean){
		return mapper.deleteByPKey(bean);
	}

	public List<HosGoodsInfoVo> list(HosGoodsInfoVo qbean) throws Exception{
		qbean.setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
		return mapper.list(qbean);
	}

	public List<ProvIdAndNameVo> getProvList(HosGoodsInfoVo queryInfo){
		return mapper.getProvList(queryInfo);
	}

	public List<ProvGoods4HosVo> getProGoods4HosList(ProvGoods4HosVo v){
		return mapper.getProGoods4HosList(v);
	}
	public List<ProvHosInfoVo> getHosProvInfoVoList(ProvHosInfoVo provHosInfoVo) {
		return mapper.getHosProvInfoVoList(provHosInfoVo);
	}

	public boolean existsProvHosInfo(String provId, String hosId){
	    return mapper.countProvHosInfo(provId, hosId) > 0;
    }

    public int insertProvHosInfo(ProvHosInfo provHosInfo) {
        return mapper.insertProvHosInfo(provHosInfo);
    }
}


