<template>
  <div id="prov_registration_info" class="full-height overflow">
    <transition name="el-fade-in-linear">
      <div v-show="!visible.provListPage" style="height: 100%;">
        <el-row class="tool-bar">
          <el-input v-model="filter.input" class="input-box" placeholder="请输入名称或注册证号"></el-input>
          <el-select v-model="filter.kind" placeholder="请选择证件类别" clearable>
            <el-option v-for="item in provCertKinds" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
          <el-select v-if="!visible.bIsMfrs" v-model="filter.mfrsId" :loading="mfrsSearchLoading"
                     :remote-method="querySearchMfrs" placeholder="请搜索生产厂家" filterable>
            <el-option v-for="item in provMfrsArray" :key="item.id" :label="item.name" :value="item.id">
            </el-option>
          </el-select>
          <el-button @click="getProvCertsInfo()" icon="el-icon-search">搜索</el-button>
          <el-button @click="addInitForm" icon="el-icon-plus">新增注册证</el-button>
          <el-button @click="importRegExcel()" icon="el-icon-plus">从Excel导入</el-button>
          <!--<el-button @click="importRegExcel()" type="success" plain icon="el-icon-plus">从Excel导入</el-button>-->
        </el-row>
        <el-row class="table-class">
          <el-table :data="provCertsData" :row-class-name="tableRowClassName" style="height: 100%;" v-loading="loading"
                    height="css" border fit>
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
                  <el-button @click="deleteProvCert(scope.row)" type="danger" size="mini">删除</el-button>
                  <el-button type="success" @click="editCert(scope.row, false)" size="mini">查看</el-button>
                </el-button-group>
              </template>
            </el-table-column>
          </el-table>
        </el-row>
        <el-row>
          <el-pagination :current-page="page" :page-size="pageSize" :page-sizes="[20, 50, 100,200]" :total="total"
                         @current-change="handlePageChange" @size-change="handlePageSizeChange"
                         layout="total, sizes, prev, pager, next" style="float:right;" small>
          </el-pagination>
        </el-row>
      </div>
    </transition>
    <!--Excel导入界面-->
    <el-dialog title="从Excel中导入商品证照模板" close="onClose" :visible.sync="importExcelVisible" :show-close='true' :close-on-press-escape='true' :close-on-click-modal="true">
      <el-col :span="3" style="line-height: 24px;max-width: 66px;">
        合作医院
      </el-col>
      <el-col :span="21">
        <el-select v-model="hosId" placeholder="请选择医院" clearable @change="hosSelect">
          <el-option v-for="item in hosInfos" :key="item.hosId" :label="item.hosName" :value="item.hosId">
          </el-option>
        </el-select>
      </el-col>
      <el-col :span="24" class="uploadClass">
        <template>
          <div>
            <el-upload
              class="upload-demo"
              :disabled="uploadState"
              :action= "uploadURL"
              :on-remove="handleRemove"
              :on-success="handleFileSuccess"
              :file-list="regfileList"
              list-type="text">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">只能上传一个Excel文件</div>
            </el-upload>
          </div>
        </template>
      </el-col>

      <div slot="footer" class="dialog-footer">
        <el-button @click.native="importExcelVisible = false;uploadState=false">取消</el-button>
        <el-button type="primary" @click.native="importExcelSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <transition name="el-fade-in-linear">
      <div v-show="visible.provListPage" id="prov_registration_edit">
        <el-form ref="provCertForm" :model="provCertFormData" class="el-form-item-nomsg" label-width="130px"
                 label-suffix="：">
          <el-row class="row-top">
            <el-col :span="12">
              <el-form-item label="厂家名称" prop="mfrsId">
                <el-select :disabled="!provManual.edit || provManual.replace" v-model="provCertFormData.mfrsId" :disable="bIsMfrs"
                           placeholder="请搜索生产厂家" :loading="mfrsSearchLoading" :remote-method="querySearchMfrsAsync"
                           clearable filterable remote class="width">
                  <el-option v-for="item in provMfrsArrayforEdit" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="(provManual.replace?'新':'')+'证件类型'" prop="regKind">
                <el-select :disabled="!provManual.edit" v-model="provCertFormData.regKind" placeholder="请选择证件类别" class="width">
                  <el-option v-for="item in provCertKinds" :key="item.id" :label="item.name" :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-top">
            <el-col :span="12">
              <el-form-item :label="(provManual.replace?'新':'')+'注册证产品名称'" prop="productName">
                <el-input :disabled="!provManual.edit" v-model="provCertFormData.productName"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="(provManual.replace?'新':'')+'证件编号'" prop="certificateCode">
                <el-input :disabled="!provManual.edit" v-model="provCertFormData.certificateCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row >
          <el-row class="row-top">
            <el-col :span="12">
              <el-form-item :label="(provManual.replace?'新':'')+'证件有效期'">
                <el-date-picker :disabled="!provManual.edit" v-model="datePickerData" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" unlink-panels style="width:250px;">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="1" :offset="11">
              <el-button v-show="provManual.add" type="primary" :loading="serBtnLoading" @click="searchBasResg">查询</el-button>
            </el-col>
          </el-row >

          <el-row class="row-top">
            <el-col :span="24">
              <el-form-item :label="(provManual.replace?'新':'')+'证件附件'" prop="file">
                <el-upload :disabled="!provManual.edit" :file-list="listImages" ref="certUpload" :action="UPLOAD_URL"
                           :before-upload="beforePicUpload" :headers="userInfo"
                           :multiple="false" :on-success="handlePicUploadSuccess"
                           :on-error="handlePicUploadError" :on-preview="handlePicPreview"
                           :on-remove="handlePicRemove" class="upload"
                           accept=".bmp,.jpg,.png,.pdf" list-type="picture">
                  <el-button :disabled="!provManual.edit" type="primary" round icon="el-icon-upload" size="small">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png/bmp/pdf文件，且不超过5Mb。点击图片文字预览证照</div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row class="row-top">
            <el-col :span="12">
              <el-form-item :label="(provManual.replace?'新':'')+'关联产品'" prop="products">
                  <el-button :disabled="!provManual.edit || !(provCertFormData.mfrsId&&provCertFormData.productName)" type="primary" icon="el-icon-plus" round @click="showMfrsProductDataDlg">增加关联产品</el-button>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item>
                <el-table :data="listGoods" v-loading="prodLoading" border fit height="180px">
                  <el-table-column type="index" width="50" label="序号">
                  </el-table-column>
                  <el-table-column prop="goodsName" label="产品名称">
                  </el-table-column>
                  <el-table-column prop="goodsGg" label="规格">
                  </el-table-column>
                  <el-table-column prop="unit" label="单位">
                  </el-table-column>
                  <el-table-column label="操作" width="80" fixed="right" align="center">
                    <template slot-scope="scope">
                      <el-button :disabled="!provManual.edit" type="danger" icon="el-icon-delete" size="mini" @click="delProvd(scope)">删除</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row class="row-top">
            <el-col :span="5" :offset="19">
              <el-form-item>
                  <el-button :disabled="!provManual.edit" type="primary" :loading="btnLoading" @click="provSubmitForm">提交</el-button>
                  <el-button @click="closeForm()">取消</el-button>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <!--增加该生产厂商关联的产品信息-->
        <el-dialog title="增加关联产品" :visible.sync="pShowDlg" :close-on-click-modal="false" close="EditOnClose">
          <el-input v-model="filter.dlgInput" class="input-box" placeholder="请输入产品名称或规格"></el-input>
          <el-button @click="getMfrsProduct" icon="el-icon-search">搜索</el-button>
          <el-table style="margin-top:10px;" :data="mfrsProductData" v-loading="mfrsProductLoading" stripe row-key="id" ref="mfrsProductTable" height="300px">
            <el-table-column type="selection" reserve-selection width="55"></el-table-column>
            <el-table-column property="goodsName" label="产品名称"></el-table-column>
            <el-table-column property="goodsGg" label="规格"></el-table-column>
            <el-table-column property="unit" label="单位"></el-table-column>
          </el-table>
          <div align="right">
            <el-button type="primary" @click.native="submitProvd('mfrsProductTable')">确定</el-button>
            <el-button @click="closeDialg('mfrsProductTable')">取消</el-button>
            <el-button @click="resetDialgTable('mfrsProductTable')">取消选择</el-button>
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
  var Child = {
    template:
      '<div><p v-for="item in errMsg" v-html="item"></p></div>',
    props: ["errMsg", "vGoOn"]
  };
  export default {
    data() {
      return {
        importExcelVisible:false,
        regfileList:[],
        fileMap:{id:'',ext:'',name:'',path:''},
        editLoading:false,
        hosId:'',
        hosInfos:[],
        uploadState:true,
        uploadURL: '/platformService/sys/upload/file/certs/',
        visible: {
          provListPage: false,
          bIsMfrs: false,
        },
        filter: {
          dlgInput:'',
          input: '',
          kind: '',
          mfrsId: '',
        },
        provManual: {
          add: false,
          edit: false,
          replace: false,
        },
        pShowDlg:false,
        bIsMfrs:false,
        loading: false,
        prodLoading:false,
        //新增注册证提交按钮
        btnLoading:false,
        serBtnLoading:false,
        mfrsSearchLoading: false,
        mfrsProductLoading:false,
        UPLOAD_URL: "/platformService/sys/upload/file/certs/",
        ATTACHMENT_URL: "/upload/",
        allowedFileTypes: [
          "image/bmp",
          "image/jpeg",
          "image/png",
          "application/pdf"
        ],
        provCertKinds: [],
        provCertsData: [],
        provCertFormData: {},
        //注册证品种表
        listGoods:[],
        //注册证文件表
        listImages:[],
        total: 0,
        page: 1,
        pageSize: 50,
        provMfrsArray: [],
        provMfrsArrayforEdit: [],
        datePickerData: [],
        mfrsProductData:[],
      }
    },
    computed: {
      userInfo() {
        return { "X-US": this.user.xus };
      }
    },
    methods: {
      hosSelect:function (d) {
        this.hosId = d;
        if(d !=''){
          this.uploadState = false;
        }else{
          this.uploadState = true;
        }
      },
      // 获取我的医院列表
      getProvHosInfoVoList: function () {
        var param = {
          orderBy: "",
          queryObject: {
          }
        };
        this.axios.post('/mdqcService/myInfo/provHosInfo/provHosInfoVoList', param).then(res => {
          this.hosInfos = res.data.data.data;
        }, err => {
        });
      },
      importExcelSubmit:function(){
        this.uploadState=false;
        this.editLoading = true;
        this.fileMap['hosId'] = this.hosId;
        this.axios.post('/mdqcService/provRegInfo/importRegCerts', this.fileMap).then(res => {
          this.editLoading = false;
          if (res.data.code == "0") {
            this.$message({message: '操作成功！', type: 'success'});
          }else{
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.editLoading = false;
          this.importExcelVisible=false;
        });
      },
      importRegExcel:function(){
        this.getProvHosInfoVoList();
        this.regfileList=[];
        this.importExcelVisible=true;
      },
      handleRemove(file, regfileList) {
        this.uploadState=false;
      },
      handleFileSuccess(response, file, regfileList){
        var ob  = response.data[0];
        this.fileMap=ob;
        this.BaseGoodsTempUrl = 'upload/'+ob['path']+'.'+ob['ext'];
        this.uploadState=true;
      },
      searchBasResg(){
        if(this.checkAddForm(this.provCertFormData)){
          this.axios
            .get("/mdqcService/prov/provRegistration/getBasResg/" + this.provCertFormData.mfrsId +"/"+this.provCertFormData.certificateCode).then(res=>{
              if(res.data.code===0){
                this.provCertFormData = res.data.data;
                this.listGoods = cloneDeep(this.provCertFormData.listGoods);
                this.listImages = cloneDeep(this.provCertFormData.listImages);
                this.listImages.forEach((item, index) => {
                  item.name = "证照" + (index + 1);
                  item.url = this.ATTACHMENT_URL + item.filePath;
                });
                this.datePickerData = [
                  this.provCertFormData.expdtBeginDate,
                  this.provCertFormData.expdtEndDate
                ];
              }else {
                //this.$message.info("平台无此注册证信息，请手动录入！");
                this.$confirm("平台无此注册证信息，请手动录入！", "提示", {
                  confirmButtonText: "确定",
                  cancelButtonText: "取消",
                  type: "warning"
                })
              }
            }).catch(err=>{
              this.$message.error(err);
          })
        }
      },
      deleteProvCert(row){
        this.$confirm("是否确认删除此注册证信息?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.axios
              .delete("/mdqcService/prov/provRegistration/delReg/" + row.id + "/" + row.version)
              .then(res => {
                if (res.data.code === 0) {
                  if (res.data.data > 0) {
                    this.$message.info("删除注册证成功");
                    this.getProvCertsInfo();
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
      querySearchMfrsAsync: debounce(800, function(queryString, callback) {
        if (queryString && queryString.length > 2) {
          this.mfrsSearchLoading = true;
          this.axios
            .get("/mdqcService/BasRegistration/getMfrs/" + queryString)
            .then(res => {
              if (res.data.code === 0) {
                this.provMfrsArrayforEdit = res.data.data;
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
      resetDialgTable(tableName){
        this.$refs[tableName].clearSelection();
      },
      closeDialg(tableName){
        this.mfrsProductData.splice(0);
        this.$refs[tableName].clearSelection();
        this.pShowDlg = false;
      },
      submitProvd(tableName){
        this.listGoods.push(...cloneDeep(this.$refs[tableName].selection));
        this.closeDialg(tableName);
      },
      getMfrsProduct(){
        this.axios
          .get("/mdqcService/prov/provRegistration/getMfrsProduct/" + this.provCertFormData.mfrsId, {
            params: {
              input: this.filter.dlgInput
            }
          })
          .then(res => {
            if (res.data.code === 0) {
              this.mfrsProductData = res.data.data.filter(
                item => !this.listGoods.some(o => o.goodsId === item.goodsId)
              );
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(err => {
            this.$message.error(err);
          });
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
     transData(provCertFormDataForm, replaceCert = false) {
        const certFormData = cloneDeep(provCertFormDataForm);
        const fileListForm = cloneDeep(certFormData.listImages);
        //先取出新的附件
        const fileList = cloneDeep(
          this.$refs.certUpload.uploadFiles.filter(item => item.id)
        );
        //比较是否有新的附件
        const fileListAdd = cloneDeep(
          this.$refs.certUpload.uploadFiles.filter(item => item.response)
        );
        //找到需要删除的附件
        const fileListDel = fileListForm?fileListForm.filter(
          item => !fileList.some(o => o.id === item.id)
        ):[];
        //表单提交的产品信息
        const listGoodsForm = cloneDeep(certFormData.listGoods);
        //关联之后的产品信息
        const products = cloneDeep(this.listGoods);
        const productsDel = listGoodsForm.filter(
          item => !products.some(o => o.goodsId === item.goodsId)
        );
        const productsAdd = products?products.filter(
          item => !listGoodsForm.some(o => o.goodsId === item.goodsId)
        ):[];
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
      checkAddForm(provCertFormData){
        const h = this.$createElement;
        var errMsg = [];
        if (!provCertFormData.mfrsId) {
          errMsg.push(`<font color='red'>厂家名称</font>不能为空`);
        }
        if (!provCertFormData.certificateCode) {
          errMsg.push(`<font color='red'>注册证编号</font>不能为空`);
        }
        if (errMsg.length > 0) {
          this.$msgbox({type: "error", title: '验证出错', message: h(Child, {props: {errMsg: errMsg}})});
          return false;
        }
        return errMsg.length == 0;
      },
      checkFormSubmit(provCertFormData) {
        const h = this.$createElement;
        var errMsg = [];
        if (!provCertFormData.mfrsId) {
          errMsg.push(`<font color='red'>厂家名称</font>不能为空`);
        }
        if (!provCertFormData.regKind) {
          errMsg.push(`<font color='red'>证件类型</font>不能为空`);
        }
        if (!provCertFormData.productName) {
          errMsg.push(`<font color='red'>注册证产品名称</font>不能为空`);
        }
        if (!provCertFormData.certificateCode) {
          errMsg.push(`<font color='red'>注册证编号</font>不能为空`);
        }
        if(this.$refs.certUpload.uploadFiles.length==0){
          errMsg.push(`请上传<font color='red'>证件附件</font>`);
        }
        if (errMsg.length > 0) {
          this.$msgbox({type: "error", title: '验证出错', message: h(Child, {props: {errMsg: errMsg}})});
          return false;
        }
        return errMsg.length == 0;
      },
      provSubmitForm() {
          if (this.checkFormSubmit(this.provCertFormData)) {
            this.btnLoading = true;
            var actionURL='';
            if(!this.provManual.replace&&!this.provManual.add&&this.provManual.edit){
              actionURL = "/provModCert"
            }else if(this.provManual.replace&&!this.provManual.add&&this.provManual.edit){
              actionURL = "/provReplaceCert"
            }else {
              actionURL = "/provAddCert"
            }
           /* const actionURL = this.provManual.replace
              ? "/provReplaceCert"
              : this.provManual.edit && this.provManual.add
                ? "/provAddCert"
                : "/provModCert";*/
            this.axios
              .post(
                "/mdqcService/prov/provRegistration" + actionURL,
                 this.transData(this.provCertFormData,this.provManual.replace)
              )
              .then(res => {
                if (res.data.code === 0) {
                  if (res.data.data) {
                    this.$message.success("更新数据成功");
                    this.visible.provListPage = false;
                    this.getProvCertsInfo();
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
      },
      closeForm(){
        for(var key in this.provCertFormData){
          delete this.provCertFormData[key];
        }
        this.listGoods=[];
        this.listImages=[];
        this.visible.provListPage = false;
      },
      resetDlgTable(){},
      closeDlg(){},
      submitProd(){},
      delProvd(data){
        this.$confirm("是否确认取消此关联产品?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.listGoods.splice(data.$index, 1);
          })
          .catch(() => {});
      },
      showMfrsProductDataDlg() {
        this.filter.dlgInput = this.provCertFormData.productName;
        this.pShowDlg = true;
        this.getMfrsProduct();
      },
      handlePicRemove(file, fileList) {},
      handlePicUploadError(err, file, fileList) {},
      handlePicPreview(file) {
        window.open(this.ATTACHMENT_URL + file.filePath, "_blank");
      },
      handlePicUploadSuccess(res, file, fileList) {
        file.filePath =
          res.data[0].path + "/" + res.data[0].id + "." + res.data[0].ext;
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
      editCert(row, editMode = false, replace = false) {
        this.provManual.add = false;
        this.loading = true;
        this.axios
          .get(
            "/mdqcService/prov/provRegistration/getProvCertById/" + row.id + "/" + row.mfrsId
          )
          .then(res => {
            if (res.data.code === 0) {
              this.loading = false;
              this.provCertFormData = res.data.data;
              this.listGoods = cloneDeep(this.provCertFormData.listGoods);
              this.listImages = cloneDeep(this.provCertFormData.listImages);
              this.listImages.forEach((item, index) => {
                item.name = "证照" + (index + 1);
                item.url = this.ATTACHMENT_URL + item.filePath;
              });
              this.provMfrsArrayforEdit = [
                {
                  id: this.provCertFormData.mfrsId,
                  name: this.provCertFormData.mfrsName
                }
              ];
              this.datePickerData = [
                this.provCertFormData.expdtBeginDate,
                this.provCertFormData.expdtEndDate
              ];
              this.provManual.edit = editMode;
              this.provManual.replace = replace;
              this.visible.provListPage = true;
            } else {
              this.loading = false;
              this.$message.error(res.data.msg);
            }
          })
          .catch(err => {
            this.loading = false
            this.$message.error(err);
          })
      },
      tableRowClassName({row, rowIndex}) {
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
      handlePageSizeChange(val) {
        this.pageSize = val;
        this.getProvCertsInfo();
      },
      handlePageChange(val) {
        this.page = val;
        this.getProvCertsInfo();
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
        let regKind = this.provCertKinds.find(item => item.id === cellValue);
        return regKind ? regKind.name : "";
      },
      newProCertData() {
        this.listImages.splice(0);
        const now = moment().format("YYYY-MM-DD");
        return {
          id: "",
          regKind: "0",
          productName: "",
          mfrsId:'',
          certificateCode: "",
          provId: this.user.corpId,
          expdtBeginDate: now,
          expdtEndDate: now,
          fileList: [],
          listGoods: [],
          isOldInfo: 0,
          version: 0
        };
      },
      addInitForm() {
        this.$refs.provCertForm.resetFields();
        this.provCertFormData = this.newProCertData();
        this.datePickerData = [
          this.provCertFormData.expdtBeginDate,
          this.provCertFormData.expdtEndDate
        ];
        //this.provMfrsArrayforEdit = this.provMfrsArray;
        this.listImages.splice(0);
        this.listGoods.splice(0);
        this.provManual.edit = true;
        this.provManual.add = true;
        this.provManual.replace = false;
        this.visible.provListPage = true;
      },
      getProvCertsInfo: function () {
        //this.page = pIndex;
        var param = {
          orderBy: "",
          page: this.page,
          rows: this.pageSize,
          queryObject: {
            searchName: this.filter.input,
            regKind: this.filter.kind,
            mfrsId: this.filter.mfrsId,
          },
        };
        this.loading = true;
        this.axios.post('/mdqcService/prov/provRegistration/listByPage', param).then(res => {
          this.loading = false;
          if (res.data.code == "0") {
            this.provCertsData = res.data.data.data;
            this.total = res.data.data.total;
          } else {
            this.handleError(res.data.msg);
          }
        });
      },
      querySearchMfrs() {

      },
      getDict(name) {
        this.axios
          .get("/mdqcService/prov/provRegistration/getDict/" + name)
          .then(res => {
            if (res.data.code === 0) {
              this.provCertKinds = res.data.data;
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(err => {
            this.$message.error(err);
          });
      },
      getMfrsByProvId(provId) {
        this.axios
          .get("/mdqcService/prov/provRegistration/getMfrs/" + provId)
          .then(res => {
            if (res.data.code === 0) {
              this.provMfrsArray = res.data.data;
            } else {
              this.$message.error(res.data.msg);
            }
          })
          .catch(err => {
            this.$message.error(err);
          });
      },
      handleError(err) {
        return new Promise((resolve, reject) => {
          this.$msgbox({title: "错误", type: "error", message: err})
            .then(action => {
              resolve(action);
            })
            .catch(err => {
              reject(err);
            });
        });
      },
    },
    mounted() {
      this.getDict("ZCZLX");
      this.getMfrsByProvId(this.user.corpId);
      this.getProvCertsInfo();
    },
  };
</script>
<style>
  .tool-bar {
    padding-top: 3px;
    height: 30px;
    box-shadow: 0px 0px 3px 0px #a9a1a1 inset;
    border-radius: 4px;
    background-color: #f9f9f9;
    margin-bottom: 5px;
  }

  .full-height {
    height: 100%;
  }

  .upload {
    padding-top: 5px;
  }
  .input-box {
    padding: 0 10px;
    width: 200px;
  }

  .table-class {
    width: 100%;
    height: calc(100% - 85px);
  }

  .el-upload__tip {
    font-size: 12px;
    color: #606266;
    margin-top: 2px;
  }
  .width {
    width: 100%;
  }
  .overflow {
    overflow-y: auto;
  }
  #prov_registration_edit .row-top{
    margin-top: 5px;
  }
  #prov_registration_info .el-table .warning-old {
    /*color: #E6A23C;*/
    color: #c5c5cd;
  }

  #prov_registration_info .el-table .warning-red {
    color: #F56C6C;
  }

  #prov_registration_info .el-table .warning-row {
    color: #E6A23C;
  }

  #prov_registration_info .el-upload-list {
    display: flex;
    flex-flow: row wrap;
  }
  #prov_registration_info .el-upload-list--picture .el-upload-list__item {
    width: 260px;
    margin: 5px 5px 0 0;
  }
  #prov_registration_info .el-form-item__error {
    position: relative;
  }
  .uploadClass{
    margin-bottom: 5px;
    margin-top: 5px;
    padding: 5px;
    border-radius: 4px;
    border: 1px solid #dcdfe6;
  }
</style>
