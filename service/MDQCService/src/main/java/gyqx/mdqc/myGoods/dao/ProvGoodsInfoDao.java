package gyqx.mdqc.myGoods.dao;


import common.db.SimpleDao;
import common.utils.UtilsContext;
import gyqx.mdqc.myGoods.dao.mapper.IProvGoodsInfoMapper;
import gyqx.mdqc.myGoods.vo.BasGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.BasMfrsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoodsInfoVo;
import gyqx.mdqc.po.ProvGoodsInfo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class ProvGoodsInfoDao {
    @Resource
    private SimpleDao dao;
    @Resource
    private UtilsContext utilsContext;
    @Resource
    private IProvGoodsInfoMapper mapper;

    public ProvGoodsInfo insertAndGet(ProvGoodsInfo provGoodsInfo) throws Exception {
        dao.insert(provGoodsInfo);
        return provGoodsInfo;
    }

    public ProvGoodsInfo updateAndGet(ProvGoodsInfo provGoodsInfo) throws Exception {
        provGoodsInfo.setFlag("1");
        return dao.updateAndGet(provGoodsInfo);
    }

    public int deleteByPKey(ProvGoodsInfo bean) {
        return mapper.deleteByPKey(bean);
    }

    public List<ProvGoodsInfoVo> list(ProvGoodsInfoVo qbean) throws Exception {
        qbean.setProvId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        return mapper.list(qbean);
    }

    public List<BasMfrsInfoVo> getBasMfrsInfoList(BasMfrsInfoVo basMfrsInfo) {
        return mapper.getBasMfrsInfoList(basMfrsInfo);
    }

    public List<BasGoodsInfoVo> getBasGoodsInfoList(BasGoodsInfoVo good) throws Exception {
        return mapper.getBasGoodsInfoList(good);
    }
}


