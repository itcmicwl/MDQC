
package gyqx.platform.base.vo;

import java.util.List;

import gyqx.spdhdi.po.CollectorProvCertificate;
import gyqx.spdhdi.po.ProvCertificateImage;

/**
 * 我的供应商证件信息Vo
 */
public class CollectorProvCertificateVo extends CollectorProvCertificate {

	private List<ProvCertificateImage> provCertificateImage;

	public List<ProvCertificateImage> getProvCertificateImage() {
		return provCertificateImage;
	}

	public void setProvCertificateImage(List<ProvCertificateImage> provCertificateImage) {
		this.provCertificateImage = provCertificateImage;
	}

}