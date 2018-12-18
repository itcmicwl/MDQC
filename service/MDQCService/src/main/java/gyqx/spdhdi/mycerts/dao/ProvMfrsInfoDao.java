package gyqx.spdhdi.mycerts.dao;

import common.db.query.QueryResult;
import common.transform.Tx;
import common.utils.PageUtils;
import common.utils.UtilsContext;
import gyqx.mdqc.mycerts.Constance;
import gyqx.spdhdi.mycerts.dao.mapper.ProvMfrsInfoMapper;
import gyqx.spdhdi.mycerts.vo.BasMfrsInfoMyCertsVo;
import gyqx.spdhdi.mycerts.vo.ProvMfrsCertificateImageVo;
import gyqx.spdhdi.mycerts.vo.ProvMfrsCertificateVo;
import gyqx.spdhdi.mycerts.vo.ProvMfrsInfoVo;
import gyqx.spdhdi.po.ProvMfrsCertificate;
import gyqx.spdhdi.po.ProvMfrsCertificateImage;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by liangwu on 17-6-19.
 */
@Repository
public class ProvMfrsInfoDao {
    @Resource
    private ProvMfrsInfoMapper mapper;

    @Resource
    private UtilsContext utilsContext;

    public List<ProvMfrsInfoVo> getMfrsInfo(String provId, int searchType, String searchText, int pageNum, int pageSize) {
        return mapper.getMfrsInfo(provId, searchType, searchText, pageNum, pageSize);
    }

    public List<BasMfrsInfoMyCertsVo> getBasMfrs(String mfrsName, String provId, int pageNum, int pageSize) {
        return mapper.getBasMfrs(mfrsName, provId, pageNum, pageSize);
    }

    public Set<ProvMfrsCertificate> insertCertList(Collection<ProvMfrsCertificateVo> provMfrsCertificateVos, String pId, String mfrsId, String uId) throws Exception {
        Set<ProvMfrsCertificate> certList = new HashSet<>();
        for (ProvMfrsCertificateVo provMfrsCertificateVo : provMfrsCertificateVos) {
            ProvMfrsCertificate provMfrsCertificate = new ProvMfrsCertificate();
            Tx.transform(provMfrsCertificateVo).to(provMfrsCertificate);
            String certId = utilsContext.getSysAtomUtil().newValue("prov_mfrs_cert_id");
            provMfrsCertificate.setId(certId);
            certList.add(provMfrsCertificate);

            if (provMfrsCertificateVo.getImageList().size() > 0)
                insertImageList(provMfrsCertificateVo.getImageList(), certId, uId);
        }
        if (certList.size() > 0)
            mapper.insertCertList(certList, pId, mfrsId, uId);
        return certList;
    }

    public Set<ProvMfrsCertificateImage> insertImageList(Collection<ProvMfrsCertificateImageVo> provMfrsCertificateImageVos, String certId, String uId) throws Exception {
        Set<ProvMfrsCertificateImage> imageList = new HashSet<>();
        for (ProvMfrsCertificateImageVo provMfrsCertificateImageVo : provMfrsCertificateImageVos) {
            ProvMfrsCertificateImage provMfrsCertificateImage = new ProvMfrsCertificateImage();
            Tx.transform(provMfrsCertificateImageVo).to(provMfrsCertificateImage);
            provMfrsCertificateImage.setId(utilsContext.getSysAtomUtil().newValue("prov_mfrs_cert_image_id"));
            imageList.add(provMfrsCertificateImage);
        }
        mapper.insertImageList(imageList, certId, uId);
        return imageList;
    }

    public List<ProvMfrsCertificateVo> getCertList(String id) {
        return mapper.getCertList(id);
    }

    public void delMfrsCertImg(String certId) {
        mapper.delMfrsCertImg(certId);
    }
}
