package gyqx.mdqc.myGoods.vo;

import gyqx.mdqc.po.HosGoodsInfo;

import java.io.Serializable;

/**
 * Created by cjzyw on 2018/8/23.
 */
public class HosGoodsInfoVo extends HosGoodsInfo implements Serializable{
    //供应商名称
    private String cname;
    //生产厂商名称
    private String mfrsName;

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getMfrsName() {
        return mfrsName;
    }

    public void setMfrsName(String mfrsName) {
        this.mfrsName = mfrsName;
    }
}
