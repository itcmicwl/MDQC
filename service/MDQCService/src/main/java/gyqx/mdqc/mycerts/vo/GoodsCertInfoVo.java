package gyqx.mdqc.mycerts.vo;

import gyqx.mdqc.po.BasRegistrationInfo;
import gyqx.mdqc.po.ProvRegistrationInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author liangwu
 * @Date 18-8-22 上午10:48
 */
@Data
public class GoodsCertInfoVo extends GoodsInfoVo {
    private Date expdtEndDate;
    private boolean fromJDE;
    List<BasRegistrationInfo> basCertList;
    List<ProvRegistrationInfo> provCertList;
}
