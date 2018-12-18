package gyqx.mdqc.mycerts.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author liangwu
 * @Date 18-8-28 上午10:14
 */
@Data
public class HomeStatVo {
    private long goodsCount;
    // 供应商查询时为承诺书数量
    private long provCount;
    private long mfrsCount;
    private long certCount;
    private long frwtsCount;
    private long jxsqsCount;

    private long certExpiredCount;
    private long jxsqsExpiredCount;
    private long frwtsExpiredCount;

    private List<ExpiredCert> expiredCertList;
}
