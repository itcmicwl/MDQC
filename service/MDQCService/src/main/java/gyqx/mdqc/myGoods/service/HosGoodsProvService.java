package gyqx.mdqc.myGoods.service;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;

import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-11-6 下午4:26
 */
public interface HosGoodsProvService {
    List<Map<String, String>> getHosProvInfoVoList();

    QueryResult<ProvGoods4HosVo> getProGoods4HosList(QueryInfo<ProvGoods4HosVo> queryInfo);
}
