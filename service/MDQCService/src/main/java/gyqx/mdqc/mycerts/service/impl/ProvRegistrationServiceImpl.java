package gyqx.mdqc.mycerts.service.impl;

import common.db.SimpleDao;
import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.transform.Tx;
import common.utils.PageUtils;
import common.utils.UtilsContext;
import gyqx.mdqc.mycerts.dao.ProvRegistrationDao;
import gyqx.mdqc.mycerts.service.IProvRegistrationService;
import gyqx.mdqc.mycerts.vo.BasRegistrationInfoVo;
import gyqx.mdqc.mycerts.vo.IdNameVo;
import gyqx.mdqc.mycerts.vo.ProvBasGoodsInfoVo;
import gyqx.mdqc.mycerts.vo.ProvRegistrationVo;
import gyqx.mdqc.po.BasRegistrationInfo;
import gyqx.mdqc.po.ProvRegistrationInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by cjzyw on 2018/8/27.
 */
@Service
public class ProvRegistrationServiceImpl implements IProvRegistrationService{
    @Resource
    private ProvRegistrationDao dao;
    @Resource
    private SimpleDao simpleDao;
    @Resource
    private UtilsContext utilsContext;

    @Override
    public List<ProvRegistrationVo> getProvCertById(String id,String mfrsId) {
        return dao.getProvCertById(id,mfrsId);
    }

    @Override
    public List<ProvBasGoodsInfoVo> getMfrsProduct(String mfrsId, String input) {
        return dao.getMfrsProduct(mfrsId, input);
    }

    @Override
    public QueryResult<ProvRegistrationVo> listProvCertByPage(QueryInfo<ProvRegistrationVo> queryInfo) throws Exception {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(dao.listProvCert(queryInfo.getQueryObject()));
    }

    @Override
    public List<IdNameVo> listDictValues(String dictName) {
        return dao.listDictValues(dictName);
    }

    @Override
    public List<IdNameVo> listMfrsForProId(String provId) {
        return dao.listMfrsForProId(provId);
    }

    @Override
    public boolean modyCert(ProvRegistrationVo provRegistrationVo) throws Exception {
        ProvRegistrationInfo provRegistrationInfo = new ProvRegistrationInfo();
        Tx.transform(provRegistrationVo).to(provRegistrationInfo);
        provRegistrationInfo.setLastUpdateDatetime(new Date());
        int certResult = simpleDao.updateNotNullFields(provRegistrationInfo);
        transforAdd(provRegistrationVo);
        String operId = utilsContext.getUserStateUtils().getCurrent().getUserId();
        boolean subResult = dao.updateCert(provRegistrationVo, operId);
        dao.changeGoodsCertificateCode(provRegistrationVo.getId(), operId);
        boolean result = certResult > 0 && subResult;
        if (!result) {
            throw new SQLException("更新注册证失败");
        }
        return result;
    }
    private void transforAdd(ProvRegistrationVo provRegistrationVo) {
        provRegistrationVo.getFileListAdd().forEach(file -> {
            if(file.getProvId()==null){
                file.setProvId(provRegistrationVo.getProvId());
            }
            file.setCertificateId(provRegistrationVo.getId());
            file.setProvId(provRegistrationVo.getProvId());
            file.setId(UUID.randomUUID().toString().replace("-", ""));
        });

        provRegistrationVo.getProductsAdd().forEach(reg -> {
            if(reg.getProvId()==null){
                reg.setProvId(provRegistrationVo.getProvId());
            }
            reg.setId(UUID.randomUUID().toString().replace("-", ""));
            reg.setProvId(provRegistrationVo.getProvId());
            reg.setCertificateId(provRegistrationVo.getId());
            reg.setGoodsId(reg.getGoodsId());
        });
    }

    @Override
    public boolean provReplaceCert(ProvRegistrationVo provRegistrationVo) throws SQLException {
        String operId = utilsContext.getUserStateUtils().getCurrent().getUserId();
        int certNum = dao.changeProvCertToOld(provRegistrationVo, operId);
        if (certNum != 1) {
            throw new SQLException("更新数据失败");
        }
        provRegistrationVo.setId(UUID.randomUUID().toString().replace("-",""));
        provRegistrationVo.getProductsAdd().addAll(provRegistrationVo.getListGoods());
        if (provRegistrationVo.getProductsDel().size() > 0) {
            provRegistrationVo.getProductsAdd().removeIf(basGoodsInfoVo -> provRegistrationVo.getProductsDel().stream().anyMatch(item -> item.getGoodsId().equals(basGoodsInfoVo.getGoodsId())));
        }
        provRegistrationVo.getFileListAdd().addAll(provRegistrationVo.getListImages());
        if (provRegistrationVo.getFileListDel().size() > 0) {
            provRegistrationVo.getFileListAdd().removeIf(imgVo -> provRegistrationVo.getFileListDel().stream().anyMatch(item -> item.getId().equals(imgVo.getId())));
        }
        transforAdd(provRegistrationVo);
        boolean result = dao.addProvCert(provRegistrationVo, operId);
        dao.changeGoodsCertificateCode(provRegistrationVo.getId(), operId);
        if (!result) {
            throw new SQLException("更新注册证失败");
        }
        return result;
    }

    @Override
    public boolean addProvCert(ProvRegistrationVo provRegistrationVo) throws SQLException {
        provRegistrationVo.setId(UUID.randomUUID().toString().replace("-",""));
        provRegistrationVo.setProvId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        transforAdd(provRegistrationVo);
        if(provRegistrationVo.getListGoods()!=null){
            provRegistrationVo.getListGoods().forEach(goods->{
                goods.setId(UUID.randomUUID().toString().replace("-",""));
                goods.setCertificateId(provRegistrationVo.getId());
                goods.setProvId(provRegistrationVo.getProvId());
            });
        }
        if(provRegistrationVo.getListImages()!=null){
            provRegistrationVo.getListImages().forEach(img->{
                img.setId(UUID.randomUUID().toString().replace("-",""));
                img.setCertificateId(provRegistrationVo.getId());
                img.setProvId(provRegistrationVo.getProvId());
            });
        }
        String operId = utilsContext.getUserStateUtils().getCurrent().getUserId();
        boolean result = dao.addProvCertforAdd(provRegistrationVo, operId);
        dao.changeGoodsCertificateCode(provRegistrationVo.getId(), operId);
        if (!result) {
            throw new SQLException("新增注册证失败");
        }
        return result;
    }

    @Override
    public List<ProvRegistrationVo> getBasCert(String mfrsId, String certificateCode) throws Exception {
        return dao.getBasCert(mfrsId,certificateCode);
    }

    @Override
    public Integer deleteProvCert(String id, Integer version) {
        return dao.deleteProvCert(id, version);
    }
}
