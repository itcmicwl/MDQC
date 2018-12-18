package gyqx.mdqc.myGoods.dao.mapper;

import gyqx.mdqc.myGoods.vo.HosGoodsInfoVo;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.mdqc.myGoods.vo.ProvHosInfoVo;
import gyqx.mdqc.myGoods.vo.ProvIdAndNameVo;
import gyqx.mdqc.po.HosGoodsInfo;
import gyqx.mdqc.po.ProvHosInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface IHosGoodsInfoMapper {
    List<HosGoodsInfoVo> list(HosGoodsInfoVo queryInfo);

    List<ProvIdAndNameVo> getProvList(HosGoodsInfoVo queryInfo);

    int deleteByPKey(HosGoodsInfo bean);

    List<ProvGoods4HosVo> getProGoods4HosList(ProvGoods4HosVo v);

    List<ProvHosInfoVo> getHosProvInfoVoList(ProvHosInfoVo basMfrsInfo);

    @Select("SELECT COUNT(0) FROM prov_hos_info WHERE prov_id = #{provId} AND hos_id = #{hosId}")
    int countProvHosInfo(@Param("provId") String provId, @Param("hosId") String hosId);

    int insertProvHosInfo(ProvHosInfo provHosInfo);
}

