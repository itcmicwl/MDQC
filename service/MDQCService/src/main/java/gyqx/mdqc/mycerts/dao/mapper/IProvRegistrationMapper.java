package gyqx.mdqc.mycerts.dao.mapper;

import gyqx.mdqc.mycerts.vo.*;
import gyqx.mdqc.po.ProvRegistrationImage;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * Created by cjzyw on 2018/8/27.
 */
public interface IProvRegistrationMapper {
    List<ProvRegistrationVo> listProvCert(ProvRegistrationVo qBean);

    @Select("SELECT val AS id, ename AS name FROM sys_dict_value WHERE dict_id = #{dictName}")
    List<IdNameVo> listDictValues(@Param("dictName") String dictName);
    List<IdNameVo> listMfrsForProId(@Param("provId")String provId);
    List<ProvRegistrationVo> getProvCertById(@Param("id")String id,
                                             @Param("mfrsId")String mfrsId,
                                             @Param("provId")String provId);
    List<ProvRegistrationVo> getBasCert(@Param("mfrsId")String mfrsId,
                                        @Param("certificateCode")String certificateCode);
    List<ProvBasGoodsInfoVo> getMfrsProduct(@Param("mfrsId") String mfrsId,
                                            @Param("input") String input,
                                            @Param("provId")String provId);
    int insertCertImg(@Param("vo") List<ProvRegistrationImage> fileList,
                      @Param("operId") String operId);
    int insertCertGoods(@Param("vo") List<ProvRegistGoodsVo> basGoodsInfoVoList,
                        @Param("operId") String operId);
    int delCertImg(@Param("vo") List<ProvRegistrationImage> fileListDel);

    int delCertGoods(@Param("vo") List<ProvRegistGoodsVo> productsDel);

    @Delete("DELETE FROM prov_registration_info WHERE id = #{id} AND version = #{version}")
    Integer deleteProvCert(@Param("id") String id, @Param("version") Integer version);

    @Delete("DELETE FROM prov_regist_goods WHERE certificate_id = #{id}")
    Integer deleteProvCertGoods(@Param("id") String id);

    @Delete("DELETE FROM prov_registration_image WHERE certificate_id = #{id}")
    Integer deleteProvCertImg(@Param("id") String id);

    @Update("UPDATE prov_registration_info SET is_old_info = '1', uxid = #{operId}, version = version + 1, last_update_datetime = sysdate " +
            "WHERE id = #{vo.id} AND mfrs_id = #{vo.mfrsId} AND version = #{vo.version} AND prov_id = #{vo.provId}AND is_old_info = '0'")
    int changeProvCertToOld(@Param("vo") ProvRegistrationVo provRegistrationVo, @Param("operId") String operId);

    int changeGoodsCertificateCode(@Param("certId") String certId,
                                   @Param("operId") String operId);

    int insertProvCert(@Param("vo") ProvRegistrationVo provRegistrationVo,
                   @Param("operId") String operId);

}
