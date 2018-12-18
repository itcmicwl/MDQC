package gyqx.mdqc.myGoods.service.impl;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.utils.PageUtils;
import common.utils.UserOnlineStateUtils;
import gyqx.mdqc.myGoods.dao.HosGoodsProvDao;
import gyqx.mdqc.myGoods.service.HosGoodsProvService;
import gyqx.mdqc.myGoods.vo.ProvGoods4HosVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author liangwu
 * @Date 18-11-6 下午4:27
 */
@Service
public class HosGoodsProvServiceImple implements HosGoodsProvService {
    @Resource
    private UserOnlineStateUtils userOnlineStateUtils;

    @Resource
    private HosGoodsProvDao hosGoodsProvDao;

    @Override
    public List<Map<String, String>> getHosProvInfoVoList() {
        return hosGoodsProvDao.getHosProvInfoVoList();
    }

    public QueryResult<ProvGoods4HosVo> getProGoods4HosList(QueryInfo<ProvGoods4HosVo> queryInfo ) {
        PageUtils.startPage(queryInfo);
        return PageUtils.endPage(hosGoodsProvDao.getProGoods4HosList(queryInfo.getQueryObject()));
    }
}
