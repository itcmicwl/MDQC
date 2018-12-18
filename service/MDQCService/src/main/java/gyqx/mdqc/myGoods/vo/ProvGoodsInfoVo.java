
package gyqx.mdqc.myGoods.vo;
import gyqx.mdqc.po.ProvGoodsInfo;
import java.io.Serializable;


public class ProvGoodsInfoVo extends ProvGoodsInfo implements Serializable{
	private String mfrsName;

	public String getMfrsName() {
		return mfrsName;
	}

	public void setMfrsName(String mfrsName) {
		this.mfrsName = mfrsName;
	}

	@Override
	public String toString() {
		return super.toString();
	}
}