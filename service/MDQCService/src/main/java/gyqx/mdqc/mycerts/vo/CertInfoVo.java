package gyqx.mdqc.mycerts.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.StringJoiner;

/**
 * @Author liangwu
 * @Date 18-9-27 上午11:46
 */
@Data
@AllArgsConstructor
public class CertInfoVo {
    private String name;
    private String certName;
    private String certCode;
    private String filePath;

    @Override
    public String toString() {
        return new StringJoiner("-", " ", "")
                .add(name)
                .add(certName)
                .add(certCode)
                .toString();
    }
}
