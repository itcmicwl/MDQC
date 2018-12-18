package gyqx.mdqc.mycerts.dao;

import common.utils.UtilsContext;
import gyqx.mdqc.mycerts.dao.mapper.IProvRegistrationMapper;
import gyqx.mdqc.mycerts.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cjzyw on 2018/8/27.
 */
@Repository
public class ProvRegistrationDao {
    @Autowired
    private IProvRegistrationMapper mapper;
    @Autowired
    private UtilsContext utilsContext;

    public List<ProvRegistrationVo> listProvCert(ProvRegistrationVo provRegistrationVo) {
        provRegistrationVo.setProvId(utilsContext.getUserStateUtils().getCurrent().getCorpId());
        //provRegistrationVo.setProvId("p007a");
        return mapper.listProvCert(provRegistrationVo);
    }

    public List<IdNameVo> listDictValues(String dictName) {
        return mapper.listDictValues(dictName);
    }

    public List<IdNameVo> listMfrsForProId(String provId) {
        //provId = "p007a";
        return mapper.listMfrsForProId(provId);
    }

    public List<ProvRegistrationVo> getProvCertById(String id,String mfrsId){
        String provId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        //String provId = "p007a";
        return mapper.getProvCertById(id,mfrsId,provId);
    }

    public List<ProvBasGoodsInfoVo> getMfrsProduct(String mfrsId, String input) {
        String provId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        //String provId = "p007a";
        return mapper.getMfrsProduct(mfrsId, input,provId);
    }

    public boolean updateCert(ProvRegistrationVo provRegistrationVo, String operId) {
        int imgRowAddNum = 0;
        if (provRegistrationVo.getFileListAdd().size() > 0) {
            imgRowAddNum = mapper.insertCertImg(provRegistrationVo.getFileListAdd(), operId);
        }
        int goodsRowAddNum = 0;
        if (provRegistrationVo.getProductsAdd().size() > 0) {
            goodsRowAddNum = mapper.insertCertGoods(provRegistrationVo.getProductsAdd(), operId);
        }
        int imgRowDelNum = 0;
        if (provRegistrationVo.getFileListDel().size() > 0) {
            imgRowDelNum = mapper.delCertImg(provRegistrationVo.getFileListDel());
        }
        int goodsRowDelNum = 0;
        if (provRegistrationVo.getProductsDel().size() > 0) {
            goodsRowDelNum = mapper.delCertGoods(provRegistrationVo.getProductsDel());
        }
        return imgRowAddNum == provRegistrationVo.getFileListAdd().size() &&
                imgRowDelNum == provRegistrationVo.getFileListDel().size() &&
                goodsRowAddNum == provRegistrationVo.getProductsAdd().size() &&
                goodsRowDelNum == provRegistrationVo.getProductsDel().size();
    }

    public Integer deleteProvCert(String id, Integer version) {
        mapper.deleteProvCertGoods(id);
        mapper.deleteProvCertImg(id);
        return mapper.deleteProvCert(id, version);
    }

    public int changeGoodsCertificateCode(String certId, String operId) {
        return mapper.changeGoodsCertificateCode(certId, operId);
    }

    public int changeProvCertToOld(ProvRegistrationVo provRegistrationVo, String operId) {
        return mapper.changeProvCertToOld(provRegistrationVo, operId);
    }
    public boolean addProvCert(ProvRegistrationVo provRegistrationVo, String operId) {
        int certRowNum = mapper.insertProvCert(provRegistrationVo, operId);
        int imgRowNum = 0;
        if (provRegistrationVo.getFileListAdd().size() > 0) {
            imgRowNum = mapper.insertCertImg(provRegistrationVo.getFileListAdd(), operId);
        }
        int goodsRowNum = 0;
        if (provRegistrationVo.getProductsAdd().size() > 0) {
            goodsRowNum = mapper.insertCertGoods(provRegistrationVo.getProductsAdd(), operId);
        }
        return certRowNum > 0 &&
                imgRowNum == provRegistrationVo.getFileListAdd().size() &&
                goodsRowNum == provRegistrationVo.getProductsAdd().size();
    }

    public boolean addProvCertforAdd(ProvRegistrationVo provRegistrationVo, String operId) {
        int certRowNum = mapper.insertProvCert(provRegistrationVo, operId);
        int imgRowNum = 0;
        if (provRegistrationVo.getListImages()!=null&&provRegistrationVo.getListImages().size() > 0) {
            imgRowNum = mapper.insertCertImg(provRegistrationVo.getListImages(), operId);
        }
        int goodsRowNum = 0;
        if (provRegistrationVo.getListGoods().size() > 0) {
            goodsRowNum = mapper.insertCertGoods(provRegistrationVo.getListGoods(), operId);
        }

        //新增注册证两种情况
        int imgRowNum1 = 0;
        if (provRegistrationVo.getFileListAdd().size() > 0) {
            imgRowNum1 = mapper.insertCertImg(provRegistrationVo.getFileListAdd(), operId);
        }
        int goodsRowNum1 = 0;
        if (provRegistrationVo.getProductsAdd().size() > 0) {
            goodsRowNum1 = mapper.insertCertGoods(provRegistrationVo.getProductsAdd(), operId);
        }
        return certRowNum > 0 &&
                imgRowNum == (provRegistrationVo.getListImages()==null?0:provRegistrationVo.getListImages().size()) &&
                goodsRowNum == provRegistrationVo.getListGoods().size()&&
                imgRowNum1 == provRegistrationVo.getFileListAdd().size()&&
                goodsRowNum1 == provRegistrationVo.getProductsAdd().size();
    }
    public  List<ProvRegistrationVo> getBasCert(String mfrsId,String certificateCode){
        return mapper.getBasCert(mfrsId,certificateCode);
    }
}
