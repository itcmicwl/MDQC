package gyqx.mdqc.mycerts.dao;

import gyqx.mdqc.mycerts.Constance;
import gyqx.mdqc.mycerts.dao.mapper.HomeStatMapper;
import gyqx.mdqc.mycerts.vo.HomeStatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author liangwu
 * @Date 18-8-28 上午9:59
 */
@Repository
public class HomeStatDao {
    @Autowired
    private HomeStatMapper mapper;

    public HomeStatVo getHomeStat(String corpId, String kind) {
        HomeStatVo result;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 3);
        switch (kind) {
            case Constance.PROV:
            case Constance.MFRS_PROV:
                result = mapper.getProvHomeStatistics(corpId);
                result.setJxsqsCount(mapper.getProvJxsqsCount((corpId)));
                result.setFrwtsCount(mapper.getProvFrwtsCount(corpId));
                result.setProvCount(mapper.getProvCnsCount(corpId));
                result.setFrwtsExpiredCount(mapper.getProvFrwtsExpiredCount(corpId, now));
                result.setCertExpiredCount(mapper.getProvCertExpiredCount(corpId, now));
                result.setJxsqsExpiredCount(mapper.getProvJxsqsExpiredCount(corpId, now));
                result.setExpiredCertList(mapper.listProvExpiredCert(corpId, calendar.getTime()));
                break;
            case Constance.HOS:
                result = mapper.getHosHomeStatistics(corpId);
                result.setFrwtsCount(mapper.getHosFrwtsCount(corpId));
                result.setJxsqsCount(mapper.getHosSqsCount(corpId));
                result.setFrwtsExpiredCount(mapper.getHosFrwtsExpiredCount(corpId, now));
                result.setCertExpiredCount(mapper.getHosCertExpiredCount(corpId, now));
                result.setJxsqsExpiredCount(mapper.getHosJxsqsExpiredCount(corpId, now));
                result.setExpiredCertList(mapper.listHosExpiredCert(corpId, calendar.getTime()));
                break;
            default:
                throw new IllegalArgumentException("非法查询");
        }
        return result;
    }
}
