package gyqx.mdqc.mycerts.service;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.mdqc.mycerts.vo.BasRegistrationInfoVo;
import gyqx.mdqc.mycerts.vo.IdNameVo;
import gyqx.mdqc.mycerts.vo.ProvBasGoodsInfoVo;
import gyqx.mdqc.mycerts.vo.ProvRegistrationVo;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by cjzyw on 2018/8/27.
 */
public interface IProvRegistrationService {
    QueryResult<ProvRegistrationVo> listProvCertByPage(QueryInfo<ProvRegistrationVo> queryInfo) throws Exception;

    List<IdNameVo> listDictValues(String dictName) throws Exception;

    List<IdNameVo> listMfrsForProId(String provId) throws Exception;

    List<ProvRegistrationVo> getProvCertById(String id,String mfrsId) throws Exception;

    List<ProvBasGoodsInfoVo> getMfrsProduct(String mfrsId, String input) throws Exception;

    boolean modyCert(ProvRegistrationVo provRegistrationVo) throws Exception;

    Integer deleteProvCert(String id, Integer version) throws Exception;

    boolean provReplaceCert(ProvRegistrationVo provRegistrationVo) throws SQLException;

    List<ProvRegistrationVo> getBasCert(String mfrsId,String certificateCode) throws Exception;

    boolean addProvCert(ProvRegistrationVo provRegistrationVo) throws SQLException;


}
