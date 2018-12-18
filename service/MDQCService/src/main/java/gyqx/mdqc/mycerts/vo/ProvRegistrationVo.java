package gyqx.mdqc.mycerts.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import gyqx.mdqc.po.ProvRegistrationImage;
import gyqx.mdqc.po.ProvRegistrationInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cjzyw on 2018/8/27.
 */
public class ProvRegistrationVo extends ProvRegistrationInfo implements Serializable{
    //生产厂家名称
    private String mfrsName;
    private String searchName;
    private List<ProvRegistrationImage> listImages;
    private List<ProvRegistGoodsVo> listGoods;

    private List<ProvRegistGoodsVo> productsAdd;
    private List<ProvRegistGoodsVo> productsDel;
    private List<ProvRegistrationImage> fileListAdd;
    private List<ProvRegistrationImage> fileListDel;


    public String getMfrsName() {
        return mfrsName;
    }

    public void setMfrsName(String mfrsName) {
        this.mfrsName = mfrsName;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public List<ProvRegistrationImage> getListImages() {
        return listImages;
    }

    public void setListImages(List<ProvRegistrationImage> listImages) {
        this.listImages = listImages;
    }

    public List<ProvRegistGoodsVo> getListGoods() {
        return listGoods;
    }

    public void setListGoods(List<ProvRegistGoodsVo> listGoods) {
        this.listGoods = listGoods;
    }
    @JsonIgnore
    public List<ProvRegistGoodsVo> getProductsAdd() {
        return productsAdd;
    }
    @JsonProperty
    public void setProductsAdd(List<ProvRegistGoodsVo> productsAdd) {
        this.productsAdd = productsAdd;
    }
    @JsonIgnore
    public List<ProvRegistGoodsVo> getProductsDel() {
        return productsDel;
    }
    @JsonProperty
    public void setProductsDel(List<ProvRegistGoodsVo> productsDel) {
        this.productsDel = productsDel;
    }
    @JsonIgnore
    public List<ProvRegistrationImage> getFileListAdd() {
        return fileListAdd;
    }
    @JsonProperty
    public void setFileListAdd(List<ProvRegistrationImage> fileListAdd) {
        this.fileListAdd = fileListAdd;
    }
    @JsonIgnore
    public List<ProvRegistrationImage> getFileListDel() {
        return fileListDel;
    }
    @JsonProperty
    public void setFileListDel(List<ProvRegistrationImage> fileListDel) {
        this.fileListDel = fileListDel;
    }
}
