package gyqx.mdqc.myGoods.service.impl;

import common.base.PinYin;
import common.db.SimpleDao;
import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.transform.Tx;
import common.utils.PageUtils;
import common.utils.UtilsContext;
import gyqx.mdqc.myGoods.dao.ProvGoodsInfoDao;
import gyqx.mdqc.myGoods.service.IProvGoodsInfoService;
import gyqx.mdqc.myGoods.vo.BasGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.BasMfrsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoodsInfoVo;
import gyqx.mdqc.po.ProvGoodsInfo;
import gyqx.spdhdi.po.BasGoodsInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class ProvGoodsInfoService implements IProvGoodsInfoService {

    @Resource
    private ProvGoodsInfoDao dao;
    @Resource
    private SimpleDao simpleDao;
    @Resource
    private UtilsContext utilsContext;

    @Override
    public void deleteProvGood(ProvGoodsInfo good) throws Exception {
        //改为逻辑删除 ，并判断 供货目录中是否有该商品，如有不允许删除
//        if (simpleDao.checkExists(ProvGoodsHos.class, "id,flag", good.getId(), "1") > 0) {
//            ValidateException.Throw("goodsName", "您删除的" + good.getGoodsName() + "在供货目录中存在，不允许删除！", good.getGoodsName());
//        }
        good.setFlag("0");
        good.setLastUpdateDatetime(new Date());
        simpleDao.updateNotNullFields(good);
//        dao.deleteByPKey(good);
    }

    public int multDelProvGoods(List<ProvGoodsInfo> goods) throws Exception {
        //改为逻辑删除 ，并判断 供货目录中是否有该商品，如有不允许删除
//        int i = 0;
        for (ProvGoodsInfo pg : goods) {
//			if (simpleDao.checkExists(ProvGoodsHos.class, "id,flag", pg.getId(),"1") > 0) {
//				//	ValidateException.Throw("goodsName", "您删除的"+pg.getGoodsName()+ "在供货目录中存在，不允许删除！", pg.getGoodsName());
//				System.out.println("供货目录中是否有该商品，不允许删除!");
//				i=i+1;
//			}
//			else{
            pg.setFlag("0");
            pg.setLastUpdateDatetime(new Date());
            simpleDao.updateNotNullFields(pg);
//			}
/*            pg.setFlag("0");
            dao.deleteByPKey(pg);
            i = i + 1;*/
        }
        return goods.size();
    }

    public ProvGoodsInfo add(ProvGoodsInfoVo provGoodsInfoVo) throws Exception {
        ProvGoodsInfo provGoodsInfo = new ProvGoodsInfo();
        Tx.transform(provGoodsInfoVo).to(provGoodsInfo);

        String id = utilsContext.getSysAtomUtil().newValue("prov_goods_info_id");
        provGoodsInfo.setProvId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        provGoodsInfo.setShortPinyin(PinYin.getShortPinyin(provGoodsInfo.getGoodsName()));
        provGoodsInfo.setId(id);
        provGoodsInfo.setFillDate(new Date());
        provGoodsInfo.setLastUpdateDatetime(new Date());
        provGoodsInfo.setVersion(0);
        provGoodsInfo.setFlag("1");
        provGoodsInfo.setUid(utilsContext.getUserStateUtils().getCurrent().getUserId());

        String sql = "select * from bas_goods_info where goods_name=? and goods_gg=? and made=? and mfrs_id=? and flag='1'";
        Object[] params = new Object[]{provGoodsInfo.getGoodsName(), provGoodsInfo.getGoodsGg(), provGoodsInfo.getMade(), provGoodsInfo.getMfrsId()};
        List<BasGoodsInfo> goodsList = simpleDao.queryForList(sql, BasGoodsInfo.class, params);
        if (CollectionUtils.isNotEmpty(goodsList)) {
            provGoodsInfo.setSpdGoodsCode(goodsList.get(0).getId());
        } else {
            BasGoodsInfo basGoodsInfo = new BasGoodsInfo();
            BeanUtils.copyProperties(basGoodsInfo, provGoodsInfo);
            String bId = utilsContext.getSysAtomUtil().newValue("bGoods_id");
            basGoodsInfo.setId(bId);
            basGoodsInfo.setCode(bId.substring(bId.lastIndexOf("-") + 1));
            simpleDao.insert(basGoodsInfo);
            provGoodsInfo.setSpdGoodsCode(bId);
        }
        return dao.insertAndGet(provGoodsInfo);
    }


    public ProvGoodsInfo update(ProvGoodsInfoVo provGoodsInfoVo) throws Exception {
        ProvGoodsInfo provGoodsInfo = new ProvGoodsInfo();
        Tx.transform(provGoodsInfoVo).to(provGoodsInfo);
        return dao.updateAndGet(provGoodsInfo);
    }

    @Override
    public QueryResult<ProvGoodsInfoVo> listByPage(QueryInfo<ProvGoodsInfoVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.list(queryInfo.getQueryObject()));
    }

    @Override
    public QueryResult<BasGoodsInfoVo> listBasGoods(QueryInfo<BasGoodsInfoVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.getBasGoodsInfoList(queryInfo.getQueryObject()));
    }

    @Override
    public QueryResult<BasMfrsInfoVo> getBasMfrsInfoList(QueryInfo<BasMfrsInfoVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.getBasMfrsInfoList(queryInfo.getQueryObject()));
    }

    @Override
    public void addProvGoodS(List<ProvGoodsInfo> goods) throws Exception {
        for (ProvGoodsInfo good : goods) {
            String id = utilsContext.getSysAtomUtil().newValue("prov_goods_info_id");
            good.setProvId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
            good.setShortPinyin(PinYin.getShortPinyin(good.getGoodsName()));
            good.setId(id);
            good.setFillDate(new Date());
            good.setFlag("1");
            good.setVersion(0);
            good.setUid(utilsContext.getUserStateUtils().getCurrent().getUserId());
            simpleDao.insert(good);
        }
    }

    @Override
    public ProvGoodsInfo convertBasGood2ProvGood(BasGoodsInfoVo basGoodVo, ProvGoodsInfo provGood) throws Exception {
        provGood.setFillDate(new Date());
        provGood.setFlag("1");
        provGood.setGoodsGg(basGoodVo.getGoodsGg());
        provGood.setGoodsName(basGoodVo.getGoodsName());
        provGood.setKind68code(basGoodVo.getKind68code());
        provGood.setKindCode(basGoodVo.getKindCode());
        //provGood.setLastUpdateDatetime(lastUpdateDatetime);
        provGood.setLbsx(basGoodVo.getLbsx());
        provGood.setMade(basGoodVo.getMade());
        provGood.setMasterCode(basGoodVo.getMasterCode());
        provGood.setMfrsCode(basGoodVo.getMfrsCode());
        provGood.setMfrsId(basGoodVo.getMfrsId());
        provGood.setPackeage(basGoodVo.getPackeage());
        //provGood.setProvId(); 从当前用户身份中获取
        provGood.setRemark(basGoodVo.getRemark());
        //provGood.setShortPinyin(shortPinyin);
        provGood.setSpdGoodsCode(basGoodVo.getCode());
        //provGood.setUid(uid);
        provGood.setUnit(basGoodVo.getUnit());
        provGood.setBrand(basGoodVo.getBrand());
        //provGood.setVersion(version);
        return provGood;
    }

}


