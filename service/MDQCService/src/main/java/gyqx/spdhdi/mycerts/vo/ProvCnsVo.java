
package gyqx.spdhdi.mycerts.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.Size;
import java.util.Date;

@JsonIgnoreProperties
public class ProvCnsVo {
    /**
     * ID
     */
    @Size(max = 36)
    private String id;
    /**
     * 供应商ID
     */
    @Size(max = 36)
    private String provId;
    /**
     * 医院ID
     */
    @Size(max = 36)
    private String hosId;
    private String hosName;
    private String provName;
    /**
     * 承诺书附件路径
     */
    @Size(max = 256)
    private String filePathCns;
    /**
     * 有效开始日期
     */
    private java.util.Date expdtBeginDate;
    /**
     * 有效结束日期
     */
    private java.util.Date expdtEndDate;
    /**
     * 数据版本
     */
    private int version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProvId() {
        return provId;
    }

    public void setProvId(String provId) {
        this.provId = provId;
    }

    public String getHosId() {
        return hosId;
    }

    public void setHosId(String hosId) {
        this.hosId = hosId;
    }

    public String getHosName() {
        return hosName;
    }

    public void setHosName(String hosName) {
        this.hosName = hosName;
    }

    public String getFilePathCns() {
        return filePathCns;
    }

    public void setFilePathCns(String filePathCns) {
        this.filePathCns = filePathCns;
    }

    public Date getExpdtBeginDate() {
        return expdtBeginDate;
    }

    public void setExpdtBeginDate(Date expdtBeginDate) {
        this.expdtBeginDate = expdtBeginDate;
    }

    public Date getExpdtEndDate() {
        return expdtEndDate;
    }

    public void setExpdtEndDate(Date expdtEndDate) {
        this.expdtEndDate = expdtEndDate;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getProvName() {
        return provName;
    }

    public void setProvName(String provName) {
        this.provName = provName;
    }
}