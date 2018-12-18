package gyqx.mdqc.mycerts.dao.mapper;

import gyqx.mdqc.mycerts.vo.ExpiredCert;
import gyqx.mdqc.mycerts.vo.HomeStatVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @Author liangwu
 * @Date 18-8-28 上午9:59
 */
public interface HomeStatMapper {
    @Select("select count(distinct(hgi.id)) AS goods_count, count(distinct(hgi.prov_id)) AS prov_count, count(distinct(bgi.mfrs_id)) AS mfrs_count, count(distinct(bregi.id)) AS cert_count from hos_goods_info hgi left outer join bas_goods_info bgi on hgi.SPD_GOODS_CODE = bgi.CODE and bgi.flag = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.GOODS_ID left outer join BAS_REGISTRATION_INFO bregi on brg.CERTIFICATE_ID = bregi.id and bregi.IS_OLD_INFO = '0' where hgi.hos_id = #{hosId} and hgi.flag = '1'")
    HomeStatVo getHosHomeStatistics(@Param("hosId") String hosId);

    @Select("select count(id) from prov_frwts where hos_id = #{hosId}")
    long getHosFrwtsCount(@Param("hosId") String hosId);

    @Select("select count(id) from prov_jxsqs_hos where hos_id = #{hosId}")
    long getHosSqsCount(@Param("hosId") String hosId);

    @Select("select count(id) from prov_frwts where hos_id = #{hosId} and expdt_end_date < #{date}")
    long getHosFrwtsExpiredCount(@Param("hosId") String hosId, @Param("date") Date date);

    @Select("select count(distinct(pj.id)) from prov_jxsqs_hos pjh left outer join prov_jxsqs pj on pjh.sqs_id = pj.id where pjh.hos_id = #{hosId} and pj.expdt_end_date < #{date}")
    long getHosJxsqsExpiredCount(@Param("hosId") String hosId, @Param("date") Date date);

    @Select("select count(distinct(bri.id)) from hos_goods_info hgi left outer join bas_goods_info bgi on hgi.SPD_GOODS_CODE = bgi.code and bgi.flag = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.GOODS_ID left outer join BAS_REGISTRATION_INFO bri on brg.CERTIFICATE_ID = bri.id and bri.is_old_info = '0' where hgi.hos_id = #{hosId} and bri.EXPDT_END_DATE < #{date} and hgi.flag = '1'")
    long getHosCertExpiredCount(@Param("hosId") String hosId, @Param("date") Date date);

    @Select("select distinct bri.product_name, bri.certificate_code, bri.expdt_end_date from hos_goods_info hgi left outer join bas_goods_info bgi on hgi.SPD_GOODS_CODE = bgi.code and bgi.flag = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.GOODS_ID left outer join BAS_REGISTRATION_INFO bri on brg.CERTIFICATE_ID = bri.id and bri.is_old_info = '0' where hgi.hos_id = #{hosId} and bri.EXPDT_END_DATE < #{date} and hgi.flag = '1' and bri.id is not null")
    List<ExpiredCert> listHosExpiredCert(@Param("hosId") String hosId, @Param("date") Date date);

    @Select("select count(distinct(pgi.id)) AS goods_count,count(distinct(bri.id)) AS cert_count,count(distinct(bgi.mfrs_id)) AS mfrs_count from prov_goods_info pgi left outer join bas_goods_info bgi on pgi.SPD_GOODS_CODE = bgi.id and bgi.FLAG = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.goods_id left outer join BAS_REGISTRATION_INFO bri on brg.CERTIFICATE_ID = bri.id and bri.IS_OLD_INFO = '0' where pgi.prov_id = #{corpId}")
    HomeStatVo getProvHomeStatistics(@Param("corpId") String corpId);

    @Select("select count(id) from prov_frwts where prov_id = #{corpId}")
    long getProvFrwtsCount(@Param("corpId") String corpId);

    @Select("select count(id) from prov_jxsqs where prov_id = #{corpId}")
    long getProvJxsqsCount(@Param("corpId") String corpId);

    @Select("select count(id) from prov_cns where prov_id = #{corpId}")
    long getProvCnsCount(@Param("corpId") String corpId);

    @Select("select count(id) from prov_frwts where prov_id = #{corpId} and expdt_end_date < #{date}")
    long getProvFrwtsExpiredCount(@Param("corpId") String corpId, @Param("date") Date date);

    @Select("select count(id) from prov_jxsqs where prov_id = #{corpId} and expdt_end_date < #{date}")
    long getProvJxsqsExpiredCount(@Param("corpId") String corpId, @Param("date") Date date);

    @Select("select count(distinct(bri.id)) from prov_goods_info pgi left outer join bas_goods_info bgi on pgi.SPD_GOODS_CODE = bgi.id and bgi.flag = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.goods_id left outer join BAS_REGISTRATION_INFO bri on brg.CERTIFICATE_ID = bri.id and bri.IS_OLD_INFO = '0' where pgi.PROV_ID = #{corpId} and bri.EXPDT_END_DATE < #{date} and pgi.FLAG = '1'")
    long getProvCertExpiredCount(@Param("corpId") String corpId, @Param("date") Date date);

    @Select("select distinct bri.product_name, bri.certificate_code, bri.expdt_end_date from prov_goods_info pgi left outer join bas_goods_info bgi on pgi.SPD_GOODS_CODE = bgi.id and bgi.flag = '1' left outer join BAS_REGIST_GOODS brg on bgi.id = brg.goods_id left outer join BAS_REGISTRATION_INFO bri on brg.CERTIFICATE_ID = bri.id and bri.IS_OLD_INFO = '0' where pgi.PROV_ID = #{corpId} and bri.EXPDT_END_DATE < #{date} and pgi.FLAG = '1' and bri.id is not null")
    List<ExpiredCert> listProvExpiredCert(@Param("corpId") String corpId, @Param("date") Date date);
}
