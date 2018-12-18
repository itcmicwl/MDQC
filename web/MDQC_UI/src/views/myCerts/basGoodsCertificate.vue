<template>
  <div id="bas-goods-certificate" class="full-height overflow">
    <transition name="el-fade-in-linear">
      <div v-show="!bShowForm" style="height: 100%;">
        <el-row class="tool-bar">
          <el-input v-model="filter.input" class="input-box" placeholder="请输入名称或注册证号"></el-input>
          <el-select v-model="filter.kind" placeholder="请选择证件类别" clearable>
            <el-option v-for="item in certKinds" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
          <el-select v-if="!bIsMfrs" v-model="filter.mfrsId" :loading="mfrsSearchLoading" :remote-method="querySearchMfrsAsync" placeholder="请搜索生产厂家" clearable filterable remote>
            <el-option v-for="item in mfrsArray" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
          <el-button @click="getCertsInfo" icon="el-icon-search">搜索</el-button>
          <el-button @click="initForm" icon="el-icon-plus">新增注册证</el-button>
        </el-row>
        <el-row class="table-class">
          <el-table :data="certsData" :row-class-name="tableRowClassName" style="height: 100%;" v-loading="tableLoading" height="css" border fit>
            <el-table-column type="index" width="40" align="center" label="序号">
            </el-table-column>
            <el-table-column prop="productName" label="注册证产品名称" width="180">
            </el-table-column>
            <el-table-column prop="mfrsName" label="生产厂家" width="180">
            </el-table-column>
            <el-table-column prop="certificateCode" label="注册证编号">
            </el-table-column>
            <el-table-column :formatter="formatterRegKind" prop="regKind" label="证件类型" width="110">
            </el-table-column>
            <el-table-column :formatter="formatterExpdtDate" prop="expdtDate" label="效期" width="150">
            </el-table-column>
            <el-table-column :formatter="formatterIsOldInfo" prop="isOldInfo" label="历史证件" align="center" width="60">
            </el-table-column>
            <el-table-column label="操作" width="160" fixed="right" align="center">
              <template slot-scope="scope">
                <el-button-group>
                  <el-button type="primary" size="mini" @click="editCert(scope.row, true)">编辑</el-button>
                  <el-button type="info" size="mini" @click="editCert(scope.row, true, true)">换证</el-button>
                  <el-button @click="deleteCert(scope.row)" type="danger" size="mini">删除</el-button>
                  <el-button type="success" @click="editCert(scope.row, false)" size="mini">查看</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
        <el-row>
          <el-pagination :current-page="pageInfo.page" :page-size="pageInfo.size" :page-sizes="[30, 50, 100]" :total="pageInfo.total" @current-change="handlePageChange" @size-change="handlePageSizeChange" layout="total, sizes, prev, pager, next" style="float:right;" small>
          </el-pagination>
        </el-row>
      </div>
    </transition>

    <transition name="el-fade-in-linear">
      <div v-show="bShowForm">
        <el-form ref="certForm" :model="certFormData" :rules="validateRules" class="el-form-item-nomsg" label-width="130px" label-suffix="：">
          <el-row>
            <el-col :span="12">
              <el-form-item label="厂家名称" prop="mfrsId">
                <el-select :disabled="!bEditMode || bReplaceCert" v-model="certFormData.mfrsId" :disable="bIsMfrs" placeholder="请搜索生产厂家" :loading="mfrsSearchLoading" :remote-method="querySearchMfrsAsync" clearable filterable remote class="width">
                  <el-option v-for="item in mfrsArray" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="(bReplaceCert?'新':'')+'证件类型'" prop="regKind">
                <el-select :disabled="!bEditMode" v-model="certFormData.regKind" placeholder="请选择证件类别" class="width">
                  <el-option v-for="item in certKinds" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item :label="(bReplaceCert?'新':'')+'注册证产品名称'" prop="productName">
                <el-input :disabled="!bEditMode" v-model="certFormData.productName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="(bReplaceCert?'新':'')+'证件编号'" prop="certificateCode">
                <el-input :disabled="!bEditMode" v-model="certFormData.certificateCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item :label="(bReplaceCert?'新':'')+'证件有效期'">
                <el-date-picker :disabled="!bEditMode" v-model="datePickerData" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" unlink-panels style="width:250px;">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item :label="(bReplaceCert?'新':'')+'证件附件'" prop="file">
            <el-upload :disabled="!bEditMode" :file-list="certFileList" ref="certUpload" :action="UPLOAD_URL"
                       :before-upload="beforePicUpload" :headers="userInfo"
                       :multiple="false" :on-success="handlePicUploadSuccess"
                       :on-error="handlePicUploadError" :on-preview="handlePicPreview"
                       :on-remove="handlePicRemove" class="upload"
                       accept=".bmp,.jpg,.png,.pdf" list-type="picture">
              <el-button :disabled="!bEditMode" type="primary" round icon="el-icon-upload" size="small">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传jpg/png/bmp/pdf文件，且不超过5Mb。点击图片文字预览证照</div>
            </el-upload>
          </el-form-item>
          <el-form-item :label="(bReplaceCert?'新':'')+'关联产品'" prop="products">
            <div style="margin-top:5px;">
              <el-button :disabled="!bEditMode || !(certFormData.mfrsId&&certFormData.productName)" type="primary" icon="el-icon-plus" round @click="showMfrsProductDataDlg">增加关联产品</el-button>
              <el-table :data="formProducts" v-loading="prodLoading" border fit>
                <el-table-column type="index" width="50" label="序号">
                </el-table-column>
                <el-table-column prop="goodsName" label="产品名称">
                </el-table-column>
                <el-table-column prop="goodsGg" label="规格">
                </el-table-column>
                <el-table-column label="操作" width="80" fixed="right" align="center">
                  <template slot-scope="scope">
                    <el-button :disabled="!bEditMode" type="danger" icon="el-icon-delete" size="mini" @click="delProd(scope)">删除</el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-form-item>
          <el-form-item>
            <div align="right">
              <el-button :disabled="!bEditMode" type="primary" :loading="btnLoading" @click="submitForm">提交</el-button>
              <el-button @click="closeForm()">取消</el-button>
              <!-- <el-button @click="resetForm('certForm')">重置</el-button> -->
            </div>
          </el-form-item>
        </el-form>
        <el-dialog title="增加关联产品" :visible.sync="bShowDlg">
          <el-input v-model="dlgFilter.input" class="input-box" placeholder="请输入产品名称或规格"></el-input>
          <el-button @click="getMfrsProduct" icon="el-icon-search">搜索</el-button>
          <el-table style="margin-top:10px;" :data="mfrsProductData" v-loading="mfrsProductLoading" stripe row-key="id" ref="mfrsProductTable">
            <el-table-column type="selection" reserve-selection width="55"></el-table-column>
            <el-table-column property="goodsName" label="产品名称"></el-table-column>
            <el-table-column property="goodsGg" label="规格"></el-table-column>
          </el-table>
          <div align="right">
            <el-button type="primary" @click="submitProd('mfrsProductTable')">确定</el-button>
            <el-button @click="closeDlg('mfrsProductTable')">取消</el-button>
            <el-button @click="resetDlgTable('mfrsProductTable')">取消选择</el-button>
          </div>
        </el-dialog>
      </div>
    </transition>
  </div>
</template>
<script>
import moment from "moment";
import cloneDeep from "lodash/cloneDeep";
import debounce from "throttle-debounce/debounce";
export default {
  data() {
    return {
      validateRules: {
        mfrsId: [{ required: true, message: "请选择厂家", trigger: "change" }],
        regKind: [
          { required: true, message: "请选择证件类型", trigger: "change" }
        ],
        productName: [
          { required: true, message: "请输入注册证产品名称", trigger: "blur" }
        ],
        certificateCode: [
          { required: true, message: "请输入注册证编号", trigger: "blur" }
        ]
      },
      BASE_URL: "/mdqcService/BasRegistration",
      UPLOAD_URL: "/platformService/sys/upload/file/certs/",
      ATTACHMENT_URL: "/upload/",
      bEditMode: false,
      bShowForm: false,
      bShowDlg: false,
      bIsMfrs: false,
      bReplaceCert: false,
      bAddMode: false,
      filter: {
        input: "",
        kind: "",
        mfrsId: ""
      },
      pageInfo: {
        page: 1,
        size: 30,
        total: 0
      },
      dlgFilter: {
        input: ""
      },
      tableLoading: true,
      prodLoading: false,
      mfrsSearchLoading: false,
      mfrsProductLoading: false,
      btnLoading: false,
      certKinds: [],
      mfrsArray: [],

      certsData: [],
      mfrsProductData: [],

      certFormData: {},
      allowedFileTypes: [
        "image/bmp",
        "image/jpeg",
        "image/png",
        "application/pdf"
      ],
      certFileList: [],
      formProducts: [],
      datePickerData: []
    };
  },
  computed: {
    userInfo() {
      return { "X-US": this.user.xus };
    }
  },
  mounted() {
    this.verifyMfrs()
      .then(res => this.getCertsInfo())
      .catch(err => {
        this.$message.error(err);
      });
    this.getDict("ZCZLX");
  },
  methods: {
    handlePageSizeChange(val) {
      this.pageInfo.size = val;
      this.getCertsInfo();
    },
    handlePageChange(val) {
      this.pageInfo.page = val;
      this.getCertsInfo();
    },
    formatterIsOldInfo(row, col, cellValue) {
      return cellValue === "1" ? "是" : "否";
    },
    formatterExpdtDate(row, col, cellValue) {
      let begin = moment(row.expdtBeginDate).format("YYYY-MM-DD");
      let end = moment(row.expdtEndDate).format("YYYY-MM-DD");
      return begin + "至" + end;
    },
    formatterRegKind(row, col, cellValue) {
      let regKind = this.certKinds.find(item => item.id === cellValue);
      return regKind ? regKind.name : "";
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.isOldInfo === "1") {
        return "warning-old";
      }
      const expdtEndDate = moment(row.expdtEndDate);
      const now = moment();
      if (expdtEndDate.isBefore(now)) {
        return "warning-red";
      } else if (expdtEndDate.isBefore(now.add(1, "month"))) {
        return "warning-row";
      }
      return "";
    },
    getDict(name) {
      this.axios
        .get(this.BASE_URL + "/getDict/" + name)
        .then(res => {
          if (res.data.code === 0) {
            this.certKinds = res.data.data;
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    verifyMfrs() {
      return this.axios
        .get(this.BASE_URL + "/verifyMfrs")
        .then(res => {
          if (res.data.code === 0) {
            if (res.data.data === true) {
              this.bIsMfrs = true;
              this.filter.mfrsId = this.user.corpId;
            }
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    getCertsInfo() {
      let params = {
        input: this.filter.input,
        kind: this.filter.kind,
        mfrsId: this.filter.mfrsId,
        page: this.pageInfo.page,
        size: this.pageInfo.size
      };
      this.tableLoading = true;
      this.axios
        .get(this.BASE_URL + "/listCerts", { params })
        .then(res => {
          if (res.data.code === 0) {
            this.certsData = res.data.data.data;
            this.pageInfo.total = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error(err);
        })
        .finally(_ => {
          this.tableLoading = false;
        });
    },
    querySearchMfrsAsync: debounce(800, function(queryString, callback) {
      if (queryString && queryString.length > 2) {
        this.mfrsSearchLoading = true;
        this.axios
          .get(this.BASE_URL + "/getMfrs/" + queryString)
          .then(res => {
            if (res.data.code === 0) {
              this.mfrsArray = res.data.data;
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(err => {
            this.$message.error(err);
          })
          .finally(_ => {
            this.mfrsSearchLoading = false;
          });
      } else {
        this.$message.error("搜索字符必须大于等于3个");
      }
    }),
    deleteCert(row) {
      this.$confirm("是否确认删除此注册证信息?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.axios
            .delete(this.BASE_URL + "/del/" + row.id + "/" + row.version)
            .then(res => {
              if (res.data.code === 0) {
                if (res.data.data > 0) {
                  this.$message.info("删除注册证成功");
                  this.getCertsInfo();
                }
              } else {
                this.$message.error(res.data.msg);
              }
            })
            .catch(err => {
              this.$message.error(err);
            });
        })
        .catch(() => {});
    },
    // resetForm(formName) {
    //   this.$refs[formName].resetFields();
    // },
    closeForm() {
      this.certFormData = this.newCertData();
      this.bShowForm = false;
    },
    beforePicUpload(file) {
      if (!this.allowedFileTypes.includes(file.type)) {
        this.$message.error(
          "文件格式不符合要求，请按要求上传" +
            this.allowedFileTypes.join(",") +
            "文件！"
        );
        return false;
      }
      if (file.size / 1024 / 1024 > 10) {
        this.$message.error("上传图片大小不能超过10M！");
        return false;
      }
    },
    handlePicUploadSuccess(res, file, fileList) {
      file.filePath =
        res.data[0].path + "/" + res.data[0].id + "." + res.data[0].ext;
    },
    handlePicUploadError(err, file, fileList) {},
    handlePicPreview(file) {
      window.open(this.ATTACHMENT_URL + file.filePath, "_blank");
    },
    handlePicRemove(file, fileList) {},
    newCertData() {
      this.certFileList.splice(0);
      const now = moment().format("YYYY-MM-DD");
      return {
        id: "",
        regKind: "0",
        productName: "",
        certificateCode: "",
        mfrsId: this.bIsMfrs ? this.user.corpId : "",
        expdtBeginDate: now,
        expdtEndDate: now,
        fileList: [],
        products: [],
        isOldInfo: 0,
        version: 0
      };
    },
    initForm() {
      if (this.bIsMfrs) {
        this.mfrsArray = [{ id: this.user.corpId, name: this.user.corpName }];
      }
      this.$refs.certForm.resetFields();
      this.certFormData = this.newCertData();
      this.datePickerData = [
        this.certFormData.expdtBeginDate,
        this.certFormData.expdtEndDate
      ];
      this.certFileList.splice(0);
      this.formProducts.splice(0);
      this.bEditMode = true;
      this.bAddMode = true;
      this.bReplaceCert = false;
      this.bShowForm = true;
    },
    showMfrsProductDataDlg() {
      this.dlgFilter.input = this.certFormData.productName;
      this.bShowDlg = true;
      this.getMfrsProduct();
    },
    getMfrsProduct() {
      this.axios
        .get(this.BASE_URL + "/getMfrsProduct/" + this.certFormData.mfrsId, {
          params: {
            input: this.dlgFilter.input
          }
        })
        .then(res => {
          if (res.data.code === 0) {
            this.mfrsProductData = res.data.data.filter(
              item => !this.formProducts.some(o => o.goodsId === item.goodsId)
            );
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    closeDlg(tableName) {
      this.mfrsProductData.splice(0);
      this.$refs[tableName].clearSelection();
      this.bShowDlg = false;
    },
    resetDlgTable(tableName) {
      this.$refs[tableName].clearSelection();
    },
    submitProd(tableName) {
      this.formProducts.push(...cloneDeep(this.$refs[tableName].selection));
      this.closeDlg(tableName);
    },
    delProd(data) {
      this.$confirm("是否确认取消此关联产品?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.formProducts.splice(data.$index, 1);
        })
        .catch(() => {});
    },
    submitForm() {
      // 新增、编辑证书
      this.$refs.certForm.validate(valid => {
        if (valid) {
          this.btnLoading = true;
          const actionURL = this.bReplaceCert
            ? "/replaceCert"
            : this.bEditMode && this.bAddMode
              ? "/addCert"
              : "/modCert";
          this.axios
            .post(
              this.BASE_URL + actionURL,
              this.transData(this.certFormData, this.bReplaceCert)
            )
            .then(res => {
              if (res.data.code === 0) {
                if (res.data.data) {
                  this.$message.success("更新数据成功");
                  this.bShowForm = false;
                  this.getCertsInfo();
                } else {
                  this.$message.error("更新数据失败，新重新刷新页面提交数据");
                }
              } else {
                this.$message.error(res.data.msg);
              }
            })
            .catch(err => {
              this.$message.error(err);
            })
            .finally(() => {
              this.btnLoading = false;
            });
        } else {
          return false;
        }
      });
    },
    transData(certFormDataOrig, replaceCert = false) {
      const certFormData = cloneDeep(certFormDataOrig);
      const fileListOrig = cloneDeep(certFormData.fileList);
      const fileList = cloneDeep(
        this.$refs.certUpload.uploadFiles.filter(item => item.id)
      );
      const fileListAdd = cloneDeep(
        this.$refs.certUpload.uploadFiles.filter(item => item.response)
      );
      const fileListDel = fileListOrig.filter(
        item => !fileList.some(o => o.id === item.id)
      );
      const productsOrig = cloneDeep(certFormData.products);
      const products = cloneDeep(this.formProducts);
      const productsDel = productsOrig.filter(
        item => !products.some(o => o.goodsId === item.goodsId)
      );
      const productsAdd = products.filter(
        item => !productsOrig.some(o => o.goodsId === item.goodsId)
      );
      certFormData.fileListAdd = this.transFilePath(fileListAdd);
      certFormData.fileListDel = fileListDel;
      certFormData.productsAdd = productsAdd;
      certFormData.productsDel = productsDel;
      certFormData.expdtBeginDate = this.datePickerData[0] || null;
      certFormData.expdtEndDate = this.datePickerData[1] || null;
      if (!replaceCert) {
        delete certFormData.products;
        delete certFormData.fileList;
      }
      return certFormData;
    },
    transFilePath(data) {
      return cloneDeep(data).map(item => {
        const file = item.response.data[0];
        return {
          id: "",
          certificateId: "",
          filePath: file.path + "/" + file.id + "." + file.ext,
          version: 0
        };
      });
    },
    editCert(row, editMode = false, replace = false) {
      this.bAddMode = false;
      this.tableLoading = true;
      this.axios
        .get(
          this.BASE_URL +
            "/getCert/" +
            row.id +
            "/" +
            row.version +
            "/" +
            row.mfrsId
        )
        .then(res => {
          if (res.data.code === 0) {
            this.certFormData = res.data.data;
            this.formProducts = cloneDeep(this.certFormData.products);
            this.certFileList = cloneDeep(this.certFormData.fileList);
            this.certFileList.forEach((item, index) => {
              item.name = "证照" + (index + 1);
              item.url = this.ATTACHMENT_URL + item.filePath;
            });
            this.mfrsArray = [
              {
                id: this.certFormData.mfrsId,
                name: this.certFormData.mfrsName
              }
            ];
            this.datePickerData = [
              this.certFormData.expdtBeginDate,
              this.certFormData.expdtEndDate
            ];
            this.bEditMode = editMode;
            this.bReplaceCert = replace;
            this.bShowForm = true;
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error(err);
        })
        .finally(_ => {
          this.tableLoading = false;
        });
    }
  }
};
</script>
<style scoped>
.tool-bar {
  padding-top: 3px;
  height: 30px;
  box-shadow: 0px 0px 3px 0px #a9a1a1 inset;
  border-radius: 4px;
  background-color: #f9f9f9;
  margin-bottom: 5px;
}
.input-box {
  padding: 0 10px;
  width: 200px;
}
.full-height {
  height: 100%;
}
.table-class {
  width: 100%;
  height: calc(100% - 85px);
}
.upload {
  padding-top: 5px;
}
.el-upload__tip {
  font-size: 12px;
  color: #606266;
  margin-top: 2px;
}
.overflow {
  overflow-y: auto;
}
.width {
  width: 100%;
}
</style>
<style>
#bas-goods-certificate .el-table .warning-row {
  /*background: antiquewhite;*/
  color: #E6A23C;
}
#bas-goods-certificate .el-table .warning-red {
  /*background: lightpink;*/
  color: #F56C6C;
}
#bas-goods-certificate .el-table .warning-old {
  /*background: lightgrey;*/
  color: #c5c5cd;
}
#bas-goods-certificate .el-checkbox__label {
  font-size: 13px;
}
#bas-goods-certificate .el-upload-list {
  display: flex;
  flex-flow: row wrap;
}
#bas-goods-certificate .el-upload-list--picture .el-upload-list__item {
  width: 250px;
  margin: 5px 5px 0 0;
}
#bas-goods-certificate .el-form-item-nomsg .el-form-item {
  margin-bottom: 10px;
}
#bas-goods-certificate .el-form-item__error {
  position: relative;
}
</style>
