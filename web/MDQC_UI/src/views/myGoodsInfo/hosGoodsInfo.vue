<template>
  <div id="hos-goods-info" class="full-height">
    <el-row style="height: 100%" v-show="visible.hosgoodsedit">
      <el-row class="tool-bar">
        <el-input v-model="filter.goodsName" class="input-box" placeholder="输入商品名称或规格"></el-input>
        <el-button @click="getHosGoodsList(1)" icon="el-icon-search">搜索</el-button>
        <!--<el-button type="primary" @click="handleHosImport(null)">批量导入</el-button>-->
        <!--<el-button @click="addHosGoodEdit(null)" icon="el-icon-plus">新增产品</el-button>-->
        <el-button type="primary" @click="goodsImportByph()" >从供货目录导入</el-button>
        <el-button @click="multHosDel()" type="danger" icon="el-icon-minus">批量删除</el-button>
      </el-row>
      <template>
        <el-table ref="multipleTable" :data="hosGoods" row-key="id" @selection-change="handleSelectionChange"
                  highlight-current-row v-loading="tLoading"
                  style="width: 100%;" border fit class="hosgoods-tableheight" height="outer">
          <el-table-column type="selection" :reserve-selection="true" width="35"></el-table-column>
          <el-table-column prop="goodsName" header-align="center" label="商品名称" sortable></el-table-column>
          <el-table-column prop="goodsGg" header-align="center" label="规格型号" width="120px"></el-table-column>
          <!-- <el-table-column prop="price" header-align="center" label="单价" width="80px"></el-table-column> -->
          <el-table-column prop="unit" header-align="center" label="单位" width="40px"></el-table-column>
          <el-table-column prop="made" header-align="center" label="产地" width="50px"></el-table-column>
          <el-table-column prop="mfrsName" header-align="center" label="生产厂商" sortable></el-table-column>
          <el-table-column prop="cname" header-align="center" label="供应商" sortable></el-table-column>
          <!--<el-table-column prop="masterCode" header-align="center" label="统一码" width="100px"> </el-table-column>-->
          <el-table-column prop="brand" header-align="center" label="品牌" width="80px"></el-table-column>
          <el-table-column label="操作" width="120" header-align="center" fixed="right">
            <template slot-scope="scope">
              <el-button-group>
                <el-button type="primary" size="mini" @click="addHosGoodEdit(scope.row)" icon="el-icon-edit-outline">编辑
                </el-button>
                <el-button type="danger" size="mini" @click="hosDelGood(scope.row)" icon="el-icon-delete">删除</el-button>
                <!--<el-button type="success" size="mini" @click="examine(scope.row)" icon="el-icon-view">查看</el-button>-->
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
        <!--分页工具条-->
        <el-col :span="24" class="toolbar">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page"
                         :page-sizes="[50, 100, 200, 400]" :page-size="pageSize"
                         layout="total, sizes, prev, pager, next"
                         :total="total" style="float:right;" small>
          </el-pagination>
        </el-col>
      </template>
      <!--在院产品编辑(新增)界面-->
      <el-dialog :title="modelTitle" close="EditOnClose" :visible.sync="editFormVisible" :close-on-click-modal="false">
        <el-form :model="hosGood" label-width="100px" ref="editForm" class="el-form-item-nomsg">
          <el-row>
            <el-col>
              <el-form-item label="产品名称" prop="goodsName">
                <el-input v-model="hosGood.goodsName" placeholder="请输入商品名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="拼音码">
                <el-input v-model="hosGood.shortPinyin" placeholder="请输入拼音码"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="规格型号" prop="goodsGg">
                <el-input v-model="hosGood.goodsGg" placeholder="请输入规格型号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <!-- <el-col :span="12">
              <el-form-item label="单价" prop="price">
                <el-input v-model="hosGood.price" placeholder="请输入商品单价"></el-input>
              </el-form-item>
            </el-col> -->
            <el-col :span="12">
              <el-form-item label="商品编号" prop="code">
                <el-input v-model="hosGood.code" placeholder="请输入商品编号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="生产厂商" prop="mfrsId">
                <el-autocomplete style="width:100%"
                                 class="inline-input"
                                 v-model="selectedName"
                                 :fetch-suggestions="querySearch"
                                 placeholder="请输入内容"
                                 value-key="mfrsName"
                                 size="small"
                                 @select="handleSelect"
                >
                </el-autocomplete>
              </el-form-item>
            </el-col>

            <el-col :span="12">
              <el-form-item label="供应商" prop="provId">
                <el-autocomplete style="width:100%"
                                 class="inline-input"
                                 v-model="selectedProvName"
                                 :fetch-suggestions="queryProSearch"
                                 placeholder="请输入内容"
                                 value-key="cname"
                                 size="small"
                                 @select="handleProSelect"
                >
                </el-autocomplete>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="产地" prop="made">
                <el-input v-model="hosGood.made" placeholder="请输入产地"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位" prop="unit">
                <el-input placeholder="请输入单位" v-model="hosGood.unit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="品牌" prop="brand">
                <el-input placeholder="请输入品牌" v-model="hosGood.brand"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="包装" prop="packeage">
                <el-input placeholder="请输入包装" v-model.number="hosGood.packeage"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="统一码">
                <el-input placeholder="请输入统一码" v-model="hosGood.masterCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="厂家出厂编码">
                <el-input placeholder="请输入厂家出厂编码" v-model="hosGood.mfrsCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="分类" prop="kind68code">
                <el-select v-model="kind68code" filterable placeholder="请选择"
                           style="width:100%">
                  <el-option
                    v-for="item in dict68s"
                    :key="item.val"
                    :label="item.ename"
                    :value="item.val">
                    <span style="float: left">{{ item.ename }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ item.val }}</span>
                  </el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="ERP编码">
                <el-input placeholder="请输入erpCode" v-model="hosGood.erpCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label=" 备注信息">
                <el-input placeholder="请输入备注信息" v-model="hosGood.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click.native="closeDia">取消</el-button>
          <el-button type="primary" @click.native="editDqSubmit" :loading="editLoading">提交</el-button>
        </div>
      </el-dialog>
    </el-row>
    <el-row style="height: 100%" v-show="visible.hosgoodsprov">
      <basGoodsforHos ref="basGoodsforHos" @close="closebasGoodsforHos"></basGoodsforHos>
    </el-row>

    <el-dialog title="从供货目录导入数据" close="onClose" :visible.sync="goodImportByphVisible" :close-on-click-modal="false" width="85%">
      <el-form :inline="true" label-width="80px">
        <el-form-item label="商品名称">
          <el-input v-model="basGoodsFilters.goodsName" placeholder="输入商品名称或规格" ></el-input>
        </el-form-item>
        <el-form-item label="供应商">
          <el-select v-model="basGoodsFilters.provId" filterable clearable placeholder="请选择" >
            <el-option
              v-for="item in provList"
              :key="item.PROVID"
              :label="item.PROVNAME"
              :value="item.PROVID">
            </el-option>
          </el-select>
        </el-form-item>

        <el-switch :width="80"
                   v-model="basGoodsFilters.isAll"
                   active-value="1" inactive-value="0"
                   active-color="#13ce66" active-text="全部" inactive-text="未导入" inactive-color="#ff4949">
        </el-switch>
        <el-button type="primary" @click="getPHGoodsList(1)" >查询</el-button>
      </el-form>
      <template>
        <el-table
          ref="multipleTable" :data="phGoods" border tooltip-effect="dark" row-key="id"
          style="width: 100%" @selection-change="handleSelectionChange" :loading="qLoading">
          <el-table-column type="selection" :reserve-selection="true" width="50" align="center" > </el-table-column>

          <el-table-column prop="provGoodsName" label="商品名称" width="200"></el-table-column>
          <el-table-column prop="provGoodsCode" label="商品编号" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="provName" label="经销商名" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="provGoodsMfrsName" label="生产厂家" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="provGoodsMade" label="产地" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="provGoodsGg" label="规格" show-overflow-tooltip> </el-table-column>
          <el-table-column prop="provGoodsUnit" label="单位" show-overflow-tooltip> </el-table-column>

        </el-table>
        <el-pagination @size-change="handlePHGoodsSizeChange" @current-change="handlePHGoodsCurrentChange" :current-page="phGoodsPage" :page-sizes="[10, 100, 200, 400]" :page-size="phGoodsPageSize" layout="total, sizes, prev, pager, next"
                       :total="phGoodsTotal" style="float:right;" small>
        </el-pagination>
        <div style="margin-top: 20px">

          <el-button @click="toggleSelection()">取消选择</el-button>
          <el-button @click="goodImprotByphSubmit()">确定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import basGoodsforHos from "./basGoodsforHos";

var Child = {
  template: '<div><p v-for="item in errMsg" v-html="item"></p></div>',
  props: ["errMsg", "vGoOn"]
};
export default {
  components: { basGoodsforHos },
  data() {
    return {
      BASE_URL: "/mdqcService/hosGoodsProv/",
      filter: {
        goodsName: ""
      },
      visible: {
        hosgoodsedit: true,
        hosgoodsprov: false
      },
      loading: false,
      qLoading: false,
      editLoading: false,
      editFormVisible: false,
      examineFormVisible: false,
      goodHosImportVisible: false,
      tLoading: false,
      total: 0,
      page: 1,
      pageSize: 50,
      modelTitle: "",
      kind68code: "",
      hosGoods: [],
      hosGood: {},
      multipleSelection: [],
      mfrs: [], //生产厂家
      prov: [], //供应商
      dict68s: [], //68分类字典
      selectedId: "", //选中的厂家Id
      selectedProvId: "", //选中的供应商Id
      selectedName: "", //选中的厂家name
      selectedProvName: "", //选中的供应商name

      goodImportByphVisible: false,
      basGoodsFilters: { goodsName: "", provId: "", isAll: "0" },
      provList: [],
      phGoodsTotal: 0,
      phGoodsPage: 1,
      phGoodsPageSize: 10,
      phGoods: []
    };
  },
  mounted() {
    this.getHosGoodsList(1);
    //this.getDqMfrs();
    //this.getProvList();
    //this.getDqDict68s();
  },
  methods: {
    //医院产品信息
    getHosGoodsList: function(pIndex) {
      this.page = pIndex;
      var param = {
        orderBy: "",
        page: this.page,
        rows: this.pageSize,
        queryObject: { goodsName: this.filter.goodsName }
      };
      this.tLoading = true;
      this.axios
        .post("/mdqcService/base/hosGoodsInfo/listByPage", param)
        .then(res => {
          this.tLoading = false;
          if (res.data.code == "0") {
            this.hosGoods = res.data.data.data;
            this.total = res.data.data.total;
          } else {
            this.handleError(res.data.msg);
          }
        });
    },
    addHosGoodEdit(item) {
      if (item) {
        let hosGood = Object.assign({}, item);
        this.hosGood = hosGood;
        this.kind68code = this.hosGood.kind68code;
        this.selectedId = item.mfrsId;
        this.selectedProvId = item.provId;
        this.selectedName = item.mfrsName;
        this.selectedProvName = item.cname;
        this.modelTitle = "修改商品";
      } else {
        this.selectedId = "";
        this.selectedProvId = "";
        this.selectedName = "";
        this.selectedProvName = "";
        this.hosGood = {};
        this.kind68code = "";
        this.modelTitle = "添加商品";
      }
      this.getDqMfrs();
      this.getDqDict68s();
      this.getProvList();
      this.editFormVisible = true;
    },
    multHosDel() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error("至少选择一条数据！");
        return;
      }
      this.$confirm("确认删除选中记录吗?", "提示", {})
        .then(() => {
          this.tLoading = true;
          this.axios
            .post(
              "/mdqcService/base/hosGoodsInfo/multDel",
              this.multipleSelection
            )
            .then(
              res => {
                this.tLoading = false;
                if (res.data.code != "0") {
                  this.$message({ message: res.data.msg, type: "success" });
                } else {
                  this.$message.error(res.data.msg);
                }
                this.getHosGoodsList(this.page);
                this.$refs.multipleTable.clearSelection();
              },
              err => {
                this.tLoading = false;
              }
            );
        })
        .catch(() => {});
    },
    hosDelGood(hosGood) {
      this.$confirm("确认删除这条记录吗?", "提示", {})
        .then(() => {
          this.tLoading = true;
          this.axios
            .post("/mdqcService/base/hosGoodsInfo/deleteHosGoodsInfo", hosGood)
            .then(
              res => {
                this.tLoading = false;
                if (res.data.code == "0") {
                  this.$message({ message: "操作成功！", type: "success" });
                  this.getHosGoodsList(this.page);
                } else {
                  this.$message.error(res.data.msg);
                }
              },
              err => {
                this.tLoading = false;
              }
            );
        })
        .catch(() => {});
    },
    /* 输入框自动补全**/
    querySearch(queryString, cb) {
      var restaurants = this.mfrs;
      var results = queryString
        ? restaurants.filter(this.createFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createFilter(queryString) {
      return restaurant => {
        return restaurant.mfrsName.indexOf(queryString) >= 0;
      };
    },
    handleSelect(item) {
      this.selectedId = item.id;
      this.selectedName = item.mfrsName;
      this.mfrsId = item.id;
    },
    /* 输入框自动补全**/
    queryProSearch(queryString, cb) {
      var restaurants = this.prov;
      var results = queryString
        ? restaurants.filter(this.createProFilter(queryString))
        : restaurants;
      // 调用 callback 返回建议列表的数据
      cb(results);
    },
    createProFilter(queryString) {
      return restaurant => {
        return restaurant.cname.indexOf(queryString) >= 0;
      };
    },
    handleProSelect(item) {
      this.selectedProvId = item.provId;
      this.selectedProvName = item.cname;
      this.provId = item.provId;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getHosGoodsList(this.page);
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getHosGoodsList(this.page);
    },
    closeDia() {
      this.editFormVisible = false;
    },
    /*     closeExamDia(){
             this.examineFormVisible =false;
           },*/
    checkDqHosSubmit(hosGood) {
      const h = this.$createElement;
      var errMsg = [];
      if (!hosGood.goodsName) {
        errMsg.push(`<font color='red'>商品名称</font>不能为空`);
      }
      if (!hosGood.code) {
        errMsg.push(`<font color='red'>商品编码</font>不能为空`);
      }
      if (!hosGood.goodsGg) {
        errMsg.push(`<font color='red'>商品规格</font>不能为空`);
      }
      if (!hosGood.unit) {
        errMsg.push(`<font color='red'>计量单位</font>不能为空`);
      }
      if (!hosGood.mfrsId) {
        errMsg.push(`请选择<font color='red'>生产厂商</font>`);
      }
      if (!hosGood.provId) {
        errMsg.push(`请选择<font color='red'>供应商</font>`);
      }
      if (!hosGood.made) {
        errMsg.push(`请输入<font color='red'>产地</font>`);
      }
      if (!hosGood.packeage) {
        errMsg.push(`<font color='red'>包装数量</font>不能为空`);
      } else if (Number(hosGood.packeage) <= 0) {
        errMsg.push(`<font color='red'>包装数量</font>必须为数字且大于零`);
      }
      if (errMsg.length > 0) {
        this.$msgbox({
          type: "error",
          title: "验证出错",
          message: h(Child, { props: { errMsg: errMsg } })
        });
        return false;
      }
      return errMsg.length == 0;
    },
    editDqSubmit() {
      this.hosGood.mfrsName = this.selectedName;
      this.hosGood.cname = this.selectedProvName;
      this.hosGood.mfrsId = this.selectedId;
      this.hosGood.provId = this.selectedProvId;
      this.hosGood.kind68code = this.kind68code;
      var paramsHos = this.hosGood;
      if (this.checkDqHosSubmit(paramsHos)) {
        this.editLoading = true;
        let serviceUrl = this.hosGood.id
          ? "/mdqcService/base/hosGoodsInfo/update"
          : "/mdqcService/base/hosGoodsInfo/add";
        this.axios.post(serviceUrl, this.hosGood).then(
          res => {
            this.editLoading = false;
            if (res.data.code == "0") {
              this.$message({ message: "操作成功！", type: "success" });
              this.editFormVisible = false;
              this.getHosGoodsList(this.page);
            } else {
              this.$message.error("操作失败！");
            }
          },
          err => {
            this.editLoading = false;
          }
        );
      }
    },
    getDqMfrs: function() {
      this.tLoading = true;
      this.axios
        .post("/mdqcService/base/provGoodsInfo/basMfrsInfoList", {})
        .then(
          res => {
            this.tLoading = false;
            if (res.data.code == "0") {
              let ms = res.data.data.data;
              ms.forEach(m => {
                m.mfrsId = m.id;
                this.mfrs.push(m);
              });
              if (this.mfrs) {
                this.mfr = this.mfrs[0];
              }
            } else {
              this.$message.error(res.data.msg);
            }
          },
          err => {
            this.tLoading = false;
          }
        );
    },
    getProvList: function() {
      this.axios
        .post("/mdqcService/base/hosGoodsInfo/basProvInfoList", {})
        .then(
          res => {
            if (res.data.code == "0") {
              this.prov = res.data.data;
            } else {
              this.$message.error(res.data.msg);
            }
          },
          err => {}
        );
    },
    getDqDict68s: function() {
      this.axios
        .post("/platformService/sys/dict/getDictValueByDictEname", {
          dictName: "68FL"
        })
        .then(
          res => {
            if (res.data.code == "0") {
              this.dict68s = res.data.data;
            } else {
              this.handleError(res.data.msg);
            }
          },
          err => {}
        );
    },
    handleHosImport: function() {
      this.visible.hosgoodsprov = true;
      this.visible.hosgoodsedit = false;
      //this.getProvList();
      this.$refs.basGoodsforHos.getInitList();
    },
    closebasGoodsforHos() {
      this.visible.hosgoodsprov = false;
      this.visible.hosgoodsedit = true;
      this.getHosGoodsList(1);
    },
    handleError(err) {
      return new Promise((resolve, reject) => {
        this.$msgbox({ title: "错误", type: "error", message: err })
          .then(action => {
            resolve(action);
          })
          .catch(err => {
            reject(err);
          });
      });
    },
    goodsImportByph() {
      this.$refs.multipleTable.clearSelection();
      this.goodImportByphVisible = true;
      this.getUnitListsByHos();
      this.getPHGoodsList(1);
    },
    getUnitListsByHos() {
      //获取经销商
      this.tLoading = true;

      this.axios.get(this.BASE_URL + "hosProvInfoVoList").then(
        res => {
          this.tLoading = false;
          if (res.data.code == "0") {
            this.provList = res.data.data;
          } else {
            this.$message.error(res.data.msg);
          }
        },
        err => {
          this.tLoading = false;
        }
      );
    },
    handlePHGoodsCurrentChange(val) {
      this.phGoodsPage = val;
      this.getPHGoodsList(this.phGoodsPage);
    },
    handlePHGoodsSizeChange(val) {
      this.getPHBasGoodsList(this.phGoodsPage);
    },
    getPHGoodsList(pIndex) {
      //获取供货列表供批量导入
      this.phGoodsPage = pIndex;
      let param = {
        orderBy: "",
        queryObject: {
          provGoodsName: this.basGoodsFilters.goodsName,
          provId: this.basGoodsFilters.provId,
          isAll: this.basGoodsFilters.isAll
        },
        page: this.phGoodsPage,
        rows: this.phGoodsPageSize
      };
      this.qLoading = true;
      this.axios.post(this.BASE_URL + "getPHGoodsList", param).then(
        res => {
          this.qLoading = false;
          if (res.data.code == "0") {
            this.phGoods = res.data.data.data;
            this.phGoodsTotal = res.data.data.total;
          } else {
            this.$message.error(res.data.msg);
          }
        },
        err => {
          this.qLoading = false;
        }
      );
    },
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },
    goodImprotByphSubmit() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error("至少选择一条数据！");
        return;
      }

      this.axios
        .post(
          "/mdqcService/base/hosGoodsInfo/goodImprotFromPHGoods",
          this.multipleSelection
        )
        .then(
          res => {
            this.loading = false;
            if (res.data.code == "0") {
              this.goodImportByphVisible = false;
              const h = this.$createElement;
              const msg = [];
              res.data.data.forEach(item => {
                msg.push(h("p", null, item));
              });
              this.$message({
                message: h("p", null, msg)
              });
              this.getHosGoodsList(1);
            } else {
              this.$message.error(res.data.msg);
            }
            this.$refs.multipleTable.clearSelection();
          },
          err => {
            this.loading = false;
          }
        );
    }
  }
};
</script>

<style>
.overflow {
  overflow-y: auto;
}

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

.hosgoods-tableheight {
  height: calc(100% - 85px);
}
</style>
