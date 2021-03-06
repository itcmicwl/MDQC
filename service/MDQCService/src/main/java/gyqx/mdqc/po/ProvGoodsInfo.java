
package gyqx.mdqc.po;

import java.util.*;
import common.db.IBean;
import common.db.meta.Title;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.*;
/**
 * 供应商产品信息
*/
@Table(name="prov_goods_info")
public class ProvGoodsInfo implements IBean
{
	/**
	 * ID
	*/
	@Id
	@Size(max=36)
	@Column(name="id")
	@Title("ID")
	private String id ;
	/**
	 * 供应商ID
	*/
	@Size(max=36)
	@JoinColumn(name="prov_id",table="bas_company_info",referencedColumnName="id")
	@Title("供应商ID")
	private String provId ;
	/**
	 * 编号
	*/
	@Size(max=36)
	@Column(name="code")
	@Title("编号")
	private String code ;
	/**
	 * 名称
	*/
	@Size(max=500)
	@Column(name="goods_name")
	@Title("名称")
	private String goodsName ;
	/**
	 * 拼音码
	*/
	@Size(max=72)
	@Column(name="short_pinyin")
	@Title("拼音码")
	private String shortPinyin ;
	/**
	 * 规格型号
	*/
	@Size(max=500)
	@Column(name="goods_gg")
	@Title("规格型号")
	private String goodsGg ;
	/**
	 * 产地
	*/
	@Size(max=36)
	@Column(name="made")
	@Title("产地")
	private String made ;
	/**
	 * 生产厂商
	*/
	@Size(max=36)
	@JoinColumn(name="mfrs_id",table="bas_mfrs_info",referencedColumnName="id")
	@Title("生产厂商")
	private String mfrsId ;
	/**
	 * 单位
	*/
	@Size(max=36)
	@Column(name="unit")
	@Title("单位")
	private String unit ;
	/**
	 * 包装
	*/
	@Column(name="packeage")
	@Title("包装")
	private java.math.BigDecimal packeage ;
	/**
	 * 统一码
	*/
	@Size(max=128)
	@Column(name="master_code")
	@Title("统一码")
	private String masterCode ;
	/**
	 * 厂家出厂编码
	*/
	@Size(max=128)
	@Column(name="mfrs_code")
	@Title("厂家出厂编码")
	private String mfrsCode ;
	/**
	 * 类别
	*/
	@Size(max=36)
	@Column(name="kind_code")
	@Title("类别")
	private String kindCode ;
	/**
	 * 68分类
	 * 
	 * 68分类
	*/
	@Size(max=36)
	@Column(name="kind_68code")
	@Title("68分类")
	private String kind68code ;
	/**
	 * erp编码
	*/
	@Size(max=36)
	@Column(name="erp_code")
	@Title("erp编码")
	private String erpCode ;
	/**
	 * 阳光采购平台编码
	*/
	@Size(max=36)
	@Column(name="hit_code")
	@Title("阳光采购平台编码")
	private String hitCode ;
	/**
	 * 中标价格
	*/
	@Column(name="hit_price")
	@Title("中标价格")
	private java.math.BigDecimal hitPrice ;
	/**
	 * 平台商品编码
	*/
	@Size(max=128)
	@Column(name="spd_goods_code")
	@Title("平台商品编码")
	private String spdGoodsCode ;
	/**
	 * 编码2
	*/
	@Size(max=128)
	@Column(name="field_code2")
	@Title("编码2")
	private String fieldCode2 ;
	/**
	 * 编码3
	*/
	@Size(max=128)
	@Column(name="field_code3")
	@Title("编码3")
	private String fieldCode3 ;
	/**
	 * 编码4
	*/
	@Size(max=128)
	@Column(name="field_code4")
	@Title("编码4")
	private String fieldCode4 ;
	/**
	 * 注册证号
	*/
	@Size(max=500)
	@Column(name="certificate_code")
	@Title("注册证号")
	private String certificateCode ;
	/**
	 * 类别属性
	 * 
	 * 类别属性
	*/
	@Size(max=36)
	@Column(name="lbsx")
	@Title("类别属性")
	private String lbsx ;
	/**
	 * 备注信息
	*/
	@Size(max=128)
	@Column(name="remark")
	@Title("备注信息")
	private String remark ;
	/**
	 * 标志
	*/
	@Size(max=1)
	@Column(name="flag")
	@Title("标志")
	private String flag ;
	/**
	 * 品牌
	*/
	@Size(max=128)
	@Column(name="brand")
	@Title("品牌")
	private String brand ;
	/**
	 * 储运条件
	*/
	@Size(max=36)
	@Column(name="st_condition")
	@Title("储运条件")
	private String stCondition ;
	/**
	 * 操作员ID
	 * 新增或修改的操作员ID
	*/
	@Size(max=36)
	@Column(name="uxid")
	@Title("操作员ID")
	private String uxid ;
	/**
	 * 建档时间
	*/
	@Column(name="fill_date")
	@Title("建档时间")
	private Date fillDate ;
	/**
	 * 最后更新时间
	*/
	@Column(name="last_update_datetime")
	@Title("最后更新时间")
	private Date lastUpdateDatetime ;
	/**
	 * 数据版本
	*/
	@Column(name="version")
	@Title("数据版本")
	private int version ;

	public String getId ()
	{
		return id ;
	}
	
	public void setId (String id )
	{
		this.id = id;
	}
	public String getProvId ()
	{
		return provId ;
	}
	
	public void setProvId (String provId )
	{
		this.provId = provId;
	}
	public String getCode ()
	{
		return code ;
	}
	
	public void setCode (String code )
	{
		this.code = code;
	}
	public String getGoodsName ()
	{
		return goodsName ;
	}
	
	public void setGoodsName (String goodsName )
	{
		this.goodsName = goodsName;
	}
	public String getShortPinyin ()
	{
		return shortPinyin ;
	}
	
	public void setShortPinyin (String shortPinyin )
	{
		this.shortPinyin = shortPinyin;
	}
	public String getGoodsGg ()
	{
		return goodsGg ;
	}
	
	public void setGoodsGg (String goodsGg )
	{
		this.goodsGg = goodsGg;
	}
	public String getMade ()
	{
		return made ;
	}
	
	public void setMade (String made )
	{
		this.made = made;
	}
	public String getMfrsId ()
	{
		return mfrsId ;
	}
	
	public void setMfrsId (String mfrsId )
	{
		this.mfrsId = mfrsId;
	}
	public String getUnit ()
	{
		return unit ;
	}
	
	public void setUnit (String unit )
	{
		this.unit = unit;
	}
	public java.math.BigDecimal getPackeage ()
	{
		return packeage ;
	}
	
	public void setPackeage (java.math.BigDecimal packeage )
	{
		this.packeage = packeage;
	}
	public String getMasterCode ()
	{
		return masterCode ;
	}
	
	public void setMasterCode (String masterCode )
	{
		this.masterCode = masterCode;
	}
	public String getMfrsCode ()
	{
		return mfrsCode ;
	}
	
	public void setMfrsCode (String mfrsCode )
	{
		this.mfrsCode = mfrsCode;
	}
	public String getKindCode ()
	{
		return kindCode ;
	}
	
	public void setKindCode (String kindCode )
	{
		this.kindCode = kindCode;
	}
	public String getKind68code ()
	{
		return kind68code ;
	}
	
	public void setKind68code (String kind68code )
	{
		this.kind68code = kind68code;
	}
	public String getErpCode ()
	{
		return erpCode ;
	}
	
	public void setErpCode (String erpCode )
	{
		this.erpCode = erpCode;
	}
	public String getHitCode ()
	{
		return hitCode ;
	}
	
	public void setHitCode (String hitCode )
	{
		this.hitCode = hitCode;
	}
	public java.math.BigDecimal getHitPrice ()
	{
		return hitPrice ;
	}
	
	public void setHitPrice (java.math.BigDecimal hitPrice )
	{
		this.hitPrice = hitPrice;
	}
	public String getSpdGoodsCode ()
	{
		return spdGoodsCode ;
	}
	
	public void setSpdGoodsCode (String spdGoodsCode )
	{
		this.spdGoodsCode = spdGoodsCode;
	}
	public String getFieldCode2 ()
	{
		return fieldCode2 ;
	}
	
	public void setFieldCode2 (String fieldCode2 )
	{
		this.fieldCode2 = fieldCode2;
	}
	public String getFieldCode3 ()
	{
		return fieldCode3 ;
	}
	
	public void setFieldCode3 (String fieldCode3 )
	{
		this.fieldCode3 = fieldCode3;
	}
	public String getFieldCode4 ()
	{
		return fieldCode4 ;
	}
	
	public void setFieldCode4 (String fieldCode4 )
	{
		this.fieldCode4 = fieldCode4;
	}
	public String getCertificateCode ()
	{
		return certificateCode ;
	}
	
	public void setCertificateCode (String certificateCode )
	{
		this.certificateCode = certificateCode;
	}
	public String getLbsx ()
	{
		return lbsx ;
	}
	
	public void setLbsx (String lbsx )
	{
		this.lbsx = lbsx;
	}
	public String getRemark ()
	{
		return remark ;
	}
	
	public void setRemark (String remark )
	{
		this.remark = remark;
	}
	public String getFlag ()
	{
		return flag ;
	}
	
	public void setFlag (String flag )
	{
		this.flag = flag;
	}
	public String getBrand ()
	{
		return brand ;
	}
	
	public void setBrand (String brand )
	{
		this.brand = brand;
	}
	public String getStCondition ()
	{
		return stCondition ;
	}
	
	public void setStCondition (String stCondition )
	{
		this.stCondition = stCondition;
	}
	public String getUxid ()
	{
		return uxid ;
	}
	
	public void setUxid (String uxid )
	{
		this.uxid = uxid;
	}
	public Date getFillDate ()
	{
		return fillDate ;
	}
	
	public void setFillDate (Date fillDate )
	{
		this.fillDate = fillDate;
	}
	public Date getLastUpdateDatetime ()
	{
		return lastUpdateDatetime ;
	}
	
	public void setLastUpdateDatetime (Date lastUpdateDatetime )
	{
		this.lastUpdateDatetime = lastUpdateDatetime;
	}
	public int getVersion ()
	{
		return version ;
	}
	
	public void setVersion (int version )
	{
		this.version = version;
	}

	public String getUid ()
	{
		return uxid ;
	}
	
	public void setUid (String uxid )
	{
		this.uxid = uxid;
	}

	@Override
	public String toString() {
		return "ProvGoodsInfo [" +" id=" +  id   +", provId=" +  provId   +", code=" +  code   +", goodsName=" +  goodsName   +", shortPinyin=" +  shortPinyin   +", goodsGg=" +  goodsGg   +", made=" +  made   +", mfrsId=" +  mfrsId   +", unit=" +  unit   +", packeage=" +  packeage   +", masterCode=" +  masterCode   +", mfrsCode=" +  mfrsCode   +", kindCode=" +  kindCode   +", kind68code=" +  kind68code   +", erpCode=" +  erpCode   +", hitCode=" +  hitCode   +", hitPrice=" +  hitPrice   +", spdGoodsCode=" +  spdGoodsCode   +", fieldCode2=" +  fieldCode2   +", fieldCode3=" +  fieldCode3   +", fieldCode4=" +  fieldCode4   +", certificateCode=" +  certificateCode   +", lbsx=" +  lbsx   +", remark=" +  remark   +", flag=" +  flag   +", brand=" +  brand   +", stCondition=" +  stCondition   +", uxid=" +  uxid   +", fillDate=" +  fillDate   +", lastUpdateDatetime=" +  lastUpdateDatetime   +", version=" +  version   
			 + "]";
	}

}