package gyqx.mdqc.myGoods.dao.mapper;

import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;

import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-11-6 下午4:29
 */
public interface HosGoodsProvMapper {
    List<Map<String, String>> getHosProvInfoVoList();

    List<ProvGoods4HosVo> getProGoods4HosList(ProvGoods4HosVo v);
}
