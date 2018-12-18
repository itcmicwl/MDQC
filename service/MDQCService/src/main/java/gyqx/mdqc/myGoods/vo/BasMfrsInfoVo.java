package gyqx.mdqc.myGoods.vo;

import gyqx.mdqc.po.BasMfrsInfo;

import java.io.Serializable;

/**
 * Created by cjzyw on 2018/8/22.
 */
public class BasMfrsInfoVo extends BasMfrsInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private String id;

    /**
     * 厂家名称
     */
    private String mfrsName;
    /**
     * 操作员名称
     */
    private String ename;
    /**
     * 操作员名称
     */
    private String cname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMfrsName() {
        return mfrsName;
    }

    public void setMfrsName(String mfrsName) {
        this.mfrsName = mfrsName;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "BasMfrsInfoVo [id=" + id + ", mfrsName=" + mfrsName + ", ename=" + ename + ", cname=" + cname + "]";
    }
}
