package gyqx.mdqc.myGoods.service.impl;

import com.google.common.collect.Lists;
import common.base.PinYin;
import common.db.SimpleDao;
import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.transform.Tx;
import common.utils.PageUtils;
import common.utils.UtilsContext;
import gyqx.mdqc.myGoods.dao.HosGoodsInfoDao;
import gyqx.mdqc.myGoods.service.IHosGoodsInfoService;
import gyqx.mdqc.myGoods.vo.HosGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.mdqc.myGoods.vo.ProvHosInfoVo;
import gyqx.mdqc.myGoods.vo.ProvIdAndNameVo;
import gyqx.mdqc.po.HosGoodsInfo;
import gyqx.mdqc.po.ProvHosInfo;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class HosGoodsInfoService implements IHosGoodsInfoService {
    @Resource
    private HosGoodsInfoDao dao;
    @Resource
    private SimpleDao simpleDao;
    @Resource
    private UtilsContext utilsContext;

    @Override
    public void deleteHosGood(HosGoodsInfo good) throws Exception {
        dao.deleteByPKey(good);
    }

    @Override
    public int multDelProvGoods(List<HosGoodsInfo> goods) throws Exception {
        int i = 0;
        for (HosGoodsInfo hg : goods) {
            dao.deleteByPKey(hg);
            i = i + 1;
        }
        return i;
    }

    public HosGoodsInfo add(HosGoodsInfoVo hosGoodsInfoVo) throws Exception {
        HosGoodsInfo hosGoodsInfo = new HosGoodsInfo();
        Tx.transform(hosGoodsInfoVo).to(hosGoodsInfo);
        String id = utilsContext.getSysAtomUtil().newValue("hos_goods_info_id");
        hosGoodsInfo.setShortPinyin(PinYin.getShortPinyin(hosGoodsInfo.getGoodsName()));
        hosGoodsInfo.setId(id);
        hosGoodsInfo.setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        hosGoodsInfo.setFillDate(new Date());
        hosGoodsInfo.setLastUpdateDatetime(new Date());
        hosGoodsInfo.setVersion(0);
        hosGoodsInfo.setFlag("1");
        hosGoodsInfo.setUid(utilsContext.getUserStateUtils().getCurrent().getUserId());
        return dao.insertAndGet(hosGoodsInfo);
    }


    public HosGoodsInfo update(HosGoodsInfoVo hosGoodsInfoVo) throws Exception {
        HosGoodsInfo hosGoodsInfo = new HosGoodsInfo();
        Tx.transform(hosGoodsInfoVo).to(hosGoodsInfo);
        return dao.updateAndGet(hosGoodsInfo);
    }

    @Override
    public List<ProvIdAndNameVo> list(HosGoodsInfoVo bean) throws Exception {
        //根据医院id查询当前所有医院的供应商
        //bean.setHosId("h0001");
        bean.setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        return dao.getProvList(bean);
    }

    public QueryResult<HosGoodsInfoVo> listByPage(QueryInfo<HosGoodsInfoVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.list(queryInfo.getQueryObject()));
    }

    public List<String> addHosGoodS(List<HosGoodsInfo> goods) throws Exception {
        List<String> msg = Lists.newArrayList();
        for (HosGoodsInfo good : goods) {
            if (simpleDao.checkExists(HosGoodsInfo.class, "spdGoodsCode", good.getSpdGoodsCode()) > 0) {
                msg.add("商品 " + good.getGoodsName() + " 已存在，无需导入。");
                continue;
            }
            good.setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
            good.setShortPinyin(PinYin.getShortPinyin(good.getGoodsName()));
            String id = good.getHosId() + utilsContext.getSysAtomUtil().newValue("hos_goods_info_id");
            good.setPrice(good.getPrice() != null ? good.getPrice() : new BigDecimal(0));
            good.setUniqueCodeStrategy(good.getUniqueCodeStrategy() != null ? good.getUniqueCodeStrategy() : "3");
            good.setTaxRate(good.getTaxRate() != null ? good.getTaxRate() : new BigDecimal(0.17));
            good.setId(id);
            good.setFillDate(new Date());
            good.setVersion(0);
            good.setUid(utilsContext.getUserStateUtils().getCurrent().getUserId());
            good.setFlag("1");
            if (StringUtils.isEmpty(good.getPurMode())) {
                good.setPurMode("10");
            }
            simpleDao.insert(good);
        }
        List<String> provIdList = goods.stream().map(HosGoodsInfo::getProvId).distinct().collect(Collectors.toList());
        String hosId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        Date now = new Date();
        provIdList.forEach(provId -> {
            if (!dao.existsProvHosInfo(provId, hosId)) {
                ProvHosInfo provHosInfo = new ProvHosInfo();
                provHosInfo.setId(utilsContext.getSysAtomUtil().newValue("prov_hos_info_id"));
                provHosInfo.setHosId(hosId);
                provHosInfo.setProvId(provId);
                provHosInfo.setStatus("2"); // 状态值为什么？
                provHosInfo.setUxid(utilsContext.getUserStateUtils().getCurrent().getUserId());
                provHosInfo.setFillDate(now);
                provHosInfo.setLastUpdateDatetime(now);
                provHosInfo.setVersion(0);
                dao.insertProvHosInfo(provHosInfo);
            }
        });
        if (msg.size() == 0) {
            msg.add("全部成功导入。");
        }
        return msg;
    }

    public HosGoodsInfo convertBasGood2HosGood(BasGoodsInfoVo basGood, HosGoodsInfo hosGood) {
        hosGood.setCode(null);//医院商品编码，需要医院自行维护
        hosGood.setGoodsName(basGood.getGoodsName());
        hosGood.setGoodsGg(basGood.getGoodsGg());
        hosGood.setMade(basGood.getMade());
        hosGood.setMfrsId(basGood.getMfrsId());
        hosGood.setUnit(basGood.getUnit());
        hosGood.setBrand(basGood.getBrand());
        hosGood.setPackeage(basGood.getPackeage());
        hosGood.setMfrsCode(basGood.getMfrsCode());
        hosGood.setKindCode(basGood.getKindCode());
        hosGood.setKind68code(basGood.getKind68code());
        hosGood.setSpdGoodsCode(basGood.getCode()); //产品基础信息的code对应
        hosGood.setLbsx(basGood.getLbsx());
        hosGood.setRemark(basGood.getRemark());
        return hosGood;
    }

    //供货目录表转换成医院信息表
    public HosGoodsInfo convertPHGood2HosGood(ProvGoods4HosVo phGoodVo, HosGoodsInfo hosGood) {
        hosGood.setCode(null);//医院商品编码，需要医院自行维护
        hosGood.setGoodsName(phGoodVo.getProvGoodsName());
        hosGood.setGoodsGg(phGoodVo.getProvGoodsGg());
        hosGood.setMade(phGoodVo.getProvGoodsMade());
        hosGood.setMfrsId(phGoodVo.getProvGoodsMfrsId());
        hosGood.setUnit(phGoodVo.getProvGoodsUnit());
        hosGood.setPackeage(phGoodVo.getProvGoodsPackeage());
        hosGood.setBrand(phGoodVo.getBrand());
        //	hosGood.setMasterCode(hosGood.getMasterCode());
        hosGood.setMfrsCode(phGoodVo.getProvGoodsMfrsCode());
        hosGood.setKindCode(phGoodVo.getProvGoodsKindCode());
        hosGood.setKind68code(phGoodVo.getProvGoodsKind68code());
        //	hosGood.setErpCode(basGood.getErpCode());
        hosGood.setSpdGoodsCode(StringUtils.substringAfterLast(phGoodVo.getProvGoodsSpdGoodsCode(), "-")); //产品基础信息的code对应
        hosGood.setLbsx(phGoodVo.getProvGoodsLbsx());
        hosGood.setProvId(phGoodVo.getProvId());
        hosGood.setHosMfrsName(phGoodVo.getProvGoodsMfrsName());
        return hosGood;
    }

    public QueryResult<ProvGoods4HosVo> getProGoods4HosList(QueryInfo<ProvGoods4HosVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.getProGoods4HosList(queryInfo.getQueryObject()));
    }

    @Override
    public QueryResult<ProvHosInfoVo> getHosProvInfoVoList(QueryInfo<ProvHosInfoVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        //设置医院id
        queryInfo.getQueryObject().setHosId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        //queryInfo.getQueryObject().setHosId("h0001");
        return PageUtils.endPage(dao.getHosProvInfoVoList(queryInfo.getQueryObject()));
    }
}


