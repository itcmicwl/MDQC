
package gyqx.spdhdi.myInfos.vo;

import java.util.List;

import gyqx.spdhdi.po.BasCompanyInfo;

/**
 * 供应商/医院/生产厂商/厂供一体信息Vo
 */
public class CompanyInfoVo extends BasCompanyInfo {

	private static final long serialVersionUID = 1L;

	private List<CompanyCertificateVo> companyCertificateVos;

	public List<CompanyCertificateVo> getCompanyCertificateVos() {
		return companyCertificateVos;
	}

	public void setCompanyCertificateVos(List<CompanyCertificateVo> companyCertificateVos) {
		this.companyCertificateVos = companyCertificateVos;
	}

}