package gyqx.mdqc.mycerts.service.impl;

import common.utils.UtilsContext;
import gyqx.mdqc.mycerts.dao.HomeStatDao;
import gyqx.mdqc.mycerts.dao.mapper.ViewJxsqsMapper;
import gyqx.mdqc.mycerts.service.HomeStatService;
import gyqx.mdqc.mycerts.service.ViewJxsqsService;
import gyqx.mdqc.mycerts.vo.HomeStatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author liangwu
 * @Date 18-8-28 上午9:59
 */
@Service
public class HomeStatcServiceImpl implements HomeStatService {
    @Autowired
    private HomeStatDao dao;
    @Autowired
    private UtilsContext utilsContext;
    @Autowired
    private ViewJxsqsService viewJxsqsService;

    @Override
    public HomeStatVo getHomeStat() {
        String corpId = utilsContext.getUserStateUtils().getCurrent().getCorpId();
        String kind = viewJxsqsService.getCorpKind(corpId);
        return dao.getHomeStat(corpId, kind);
    }
}
