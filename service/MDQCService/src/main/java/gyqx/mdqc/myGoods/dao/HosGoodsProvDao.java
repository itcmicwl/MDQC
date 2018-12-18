package gyqx.mdqc.myGoods.dao;

import gyqx.mdqc.myGoods.dao.mapper.HosGoodsProvMapper;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import gyqx.spdhdi.myInfos.vo.ProvHosInfoVo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-11-6 下午4:28
 */
@Repository
public class HosGoodsProvDao {
    @Resource
    private HosGoodsProvMapper mapper;

    public List<Map<String, String>> getHosProvInfoVoList() {
        return mapper.getHosProvInfoVoList();
    }

    public List<ProvGoods4HosVo> getProGoods4HosList(ProvGoods4HosVo v) {
        return mapper.getProGoods4HosList(v);
    }
}
