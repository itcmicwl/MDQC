<template>
  <div id="prov-goods-info" class="full-height">
    <el-row style="height: 100%" v-show="visible.provgoodsedit">
      <el-row class="tool-bar">
        <el-input v-model="filters.goodsName" class="input-box" placeholder="输入商品名称或规格"></el-input>
        <el-button @click="getProvGoodsList()" icon="el-icon-search">搜索</el-button>
        <el-button type="primary" @click="handleImport(null)">批量导入</el-button>
        <el-button @click="addGoodEdit(null)" icon="el-icon-plus">新增产品</el-button>
        <el-button @click="multDel()" type="danger" icon="el-icon-minus">批量删除</el-button>
      </el-row>
      <template>
        <el-table ref="multipleTable" :data="provGoods" row-key="id" @selection-change="handleSelectionChange"
                  highlight-current-row v-loading="loading"
                  style="width: 100%;" border fit class="provgoods-tableheight" height="outer">
          <el-table-column type="selection" :reserve-selection="true" width="35"></el-table-column>
          <el-table-column prop="goodsName" header-align="center" label="商品名称" sortable></el-table-column>
          <el-table-column prop="goodsGg" header-align="center" label="规格型号" width="180px"></el-table-column>
          <el-table-column prop="erpCode" header-align="center" label="ERP编码" width="90px"></el-table-column>
          <el-table-column prop="unit" header-align="center" label="单位" width="40px"></el-table-column>
          <el-table-column prop="made" header-align="center" label="产地" width="50px"></el-table-column>
          <el-table-column prop="mfrsName" header-align="center" label="生产厂商" sortable></el-table-column>
          <!--<el-table-column prop="masterCode" header-align="center" label="统一码" width="100px"> </el-table-column>-->
          <el-table-column prop="certificateCode" label="注册证号" sortable></el-table-column>
          <el-table-column prop="brand" header-align="center" label="品牌" width="80px"></el-table-column>
          <el-table-column label="操作" width="180" header-align="center" fixed="right">
            <template slot-scope="scope">
              <el-button-group>
                <el-button type="primary" size="mini" @click="addGoodEdit(scope.row)" icon="el-icon-edit-outline">编辑
                </el-button>
                <el-button type="danger" size="mini" @click="provDelGood(scope.row)" icon="el-icon-delete">删除</el-button>
                <el-button type="success" size="mini" @click="examine(scope.row)" icon="el-icon-view">查看</el-button>
              </el-button-group>
            </template>
          </el-table-column>
        </el-table>
        <!--分页工具条-->
        <el-col :span="24" class="toolbar">
          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page"
                         :page-sizes="[50, 100, 200, 400]" :page-size="pageSize" layout="total, sizes, prev, pager, next"
                         :total="total" style="float:right;" small>
          </el-pagination>
        </el-col>
      </template>
      <!--编辑(新增)界面-->
      <el-dialog :title="modelTitle" close="EditOnClose" :visible.sync="editFormVisible" :close-on-click-modal="false">
        <el-form :model="provGood" label-width="100px" ref="editForm" class="el-form-item-nomsg">
          <el-row>
            <el-col>
              <el-form-item label="产品名称" prop="goodsName">
                <el-input v-model="provGood.goodsName" placeholder="请输入商品名称"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <!-- <el-col :span="8">
              <el-form-item label="拼音码">
                <el-input v-model="provGood.shortPinyin" placeholder="请输入拼音码"></el-input>
              </el-form-item>
            </el-col> -->
            <el-col :span="16">
              <el-form-item label="规格型号" prop="goodsGg">
                <el-input v-model="provGood.goodsGg" placeholder="请输入规格型号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="商品编号" prop="code">
                <el-input v-model="provGood.code" placeholder="请输入商品编号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="生产厂商" prop="mfrsId">
                <el-autocomplete style="width:100%"
                                 class="inline-input"
                                 v-model="selectedName"
                                 :fetch-suggestions="querySearch"
                                 placeholder="请输入内容"
                                 value-key="mfrsName"
                                 size="small"
                                 @select="handleSelect">
                </el-autocomplete>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="产地" prop="made">
                <el-input v-model="provGood.made" placeholder="请输入产地"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位" prop="unit">
                <el-input placeholder="请输入单位" v-model="provGood.unit"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="品牌" prop="brand">
                <el-input placeholder="请输入品牌" v-model="provGood.brand"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="包装数量" prop="packeage">
                <el-input placeholder="请输入包装数量" v-model.number="provGood.packeage"></el-input>
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
              <el-form-item label="厂家出厂编码">
                <el-input placeholder="请输入厂家出厂编码" v-model="provGood.mfrsCode"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <!-- <el-col :span="12">
              <el-form-item label="统一码">
                <el-input placeholder="请输入统一码" v-model="provGood.masterCode"></el-input>
              </el-form-item>
            </el-col> -->
            <!-- <el-col :span="12">
              <el-form-item label="ERP编码">
                <el-input placeholder="请输入erpCode" v-model="provGood.erpCode"></el-input>
              </el-form-item>
            </el-col> -->
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label=" 备注信息">
                <el-input placeholder="请输入备注信息" v-model="provGood.remark"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click.native="closeDia">取消</el-button>
          <el-button type="primary" @click.native="editDqSubmit" :loading="editLoading">提交</el-button>
        </div>
      </el-dialog>
      <!--查看界面-->
      <el-dialog :title="modelTitle" close="OnClose" :visible.sync="examineFormVisible" :close-on-click-modal="false">
        <el-form :model="provGood" label-width="100px" ref="examineForm" class="el-form-item-nomsg">
          <el-row>
            <el-col>
              <el-form-item label="产品名称" prop="goodsName">
                <el-input v-model="provGood.goodsName" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="拼音码">
                <el-input v-model="provGood.shortPinyin" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="规格型号" prop="goodsGg">
                <el-input v-model="provGood.goodsGg" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="8">
              <el-form-item label="商品编号" prop="code">
                <el-input v-model="provGood.code" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="16">
              <el-form-item label="生产厂商" prop="mfrsId">
                <el-input v-model="selectedName" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="产地" prop="made">
                <el-input v-model="provGood.made" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="单位" prop="unit">
                <el-input v-model="provGood.unit" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="品牌" prop="brand">
                <el-input v-model="provGood.brand" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="包装" prop="packeage">
                <el-input v-model.number="provGood.packeage" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="统一码">
                <el-input v-model="provGood.masterCode" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="厂家出厂编码">
                <el-input v-model="provGood.mfrsCode" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="分类">
                <el-input v-model="kind68code" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="ERP编码">
                <el-input v-model="provGood.erpCode" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col>
              <el-form-item label=" 备注信息">
                <el-input v-model="provGood.remark" :disabled="examineFormVisible"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>

        <div slot="footer" class="dialog-footer">
          <el-button @click.native="closeExamDia">取消</el-button>
        </div>
      </el-dialog>
    </el-row>

    <el-row style="height: calc(100% - 30px)" v-show="visible.provgoodsprov">
      <basGoodsforProv ref="basGoodsforProv" @close="closebasGoodsforProv"></basGoodsforProv>
    </el-row>

  </div>
</template>

<script>
import basGoodsforProv from "./basGoodsforProv";
var Child = {
  template: '<div><p v-for="item in errMsg" v-html="item"></p></div>',
  props: ["errMsg", "vGoOn"]
};
export default {
  components: { basGoodsforProv },
  data() {
    return {
      visible: {
        provgoodsedit: true,
        provgoodsprov: false
      },
      filters: {
        goodsName: ""
      },
      basGoodsFilters: {
        goodsName: ""
      },
      modelTitle: "",
      editFormVisible: false,
      examineFormVisible: false,
      goodImportVisible: false,
      editLoading: false,
      kind68code: "",
      loading: false,
      qLoading: false,
      total: 0,
      page: 1,
      pageSize: 50,
      basPage: 1,
      basPageSize: 20,
      basTotal: 0,
      multipleSelection: [],
      provGoods: [],
      basGoods: [],
      provGood: {},
      mfrs: [], //生产厂家
      dict68s: [], //68分类字典
      selectedId: "", //选中的厂家Id
      selectedName: "" //选中的厂家name
    };
  },
  mounted() {
    this.getProvGoodsList();
    //this.getDqMfrs();
    //this.getDqDict68s();
  },
  methods: {
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
    //查询供应商产品信息
    getProvGoodsList: function() {
      //this.page = pIndex;
      var param = {
        orderBy: "",
        page: this.page,
        rows: this.pageSize,
        queryObject: { goodsName: this.filters.goodsName }
      };
      this.loading = true;
      this.axios
        .post("/mdqcService/base/provGoodsInfo/listByPage", param)
        .then(res => {
          this.loading = false;
          if (res.data.code == "0") {
            this.provGoods = res.data.data.data;
            this.total = res.data.data.total;
          } else {
            this.handleError(res.data.msg);
          }
        });
    },
    //新增
    addGoodEdit(item) {
      if (item) {
        let provGood = Object.assign({}, item);
        this.provGood = provGood;
        this.kind68code = this.provGood.kind68code;
        this.selectedId = item.mfrsId;
        this.selectedName = item.mfrsName;
        this.modelTitle = "修改商品";
      } else {
        this.selectedId = "";
        this.selectedName = "";
        this.provGood = {};
        this.kind68code = "";
        this.modelTitle = "添加商品";
      }
      this.getDqMfrs();
      this.getDqDict68s();
      this.editFormVisible = true;
    },
    examine(item) {
      let provGood = Object.assign({}, item);
      this.provGood = provGood;
      this.kind68code = this.provGood.kind68code;
      this.selectedId = item.mfrsId;
      this.selectedName = item.mfrsName;
      this.modelTitle = "查看商品";
      this.examineFormVisible = true;
    },
    getDqMfrs: function() {
      //this.loading = true;
      this.axios
        .post("/mdqcService/base/provGoodsInfo/basMfrsInfoList", {})
        .then(
          res => {
            //this.loading = false;
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
            this.loading = false;
          }
        );
    },
    getDqDict68s: function() {
      //this.loading = true;
      this.axios
        .post("/platformService/sys/dict/getDictValueByDictEname", {
          dictName: "68FL"
        })
        .then(
          res => {
            //this.loading = false;
            if (res.data.code == "0") {
              this.dict68s = res.data.data;
            } else {
              this.handleError(res.data.msg);
            }
          },
          err => {
            this.loading = false;
          }
        );
    },
    closeDia() {
      this.editFormVisible = false;
    },
    closeExamDia() {
      this.examineFormVisible = false;
    },
    EditOnClose() {},
    checkDqProvSubmit(provGood) {
      const h = this.$createElement;
      var errMsg = [];
      if (!provGood.goodsName) {
        errMsg.push(`<font color='red'>商品名称</font>不能为空`);
      }
      if (!provGood.code) {
        errMsg.push(`<font color='red'>商品编码</font>不能为空`);
      }
      if (!provGood.goodsGg) {
        errMsg.push(`<font color='red'>商品规格</font>不能为空`);
      }
      if (!provGood.unit) {
        errMsg.push(`<font color='red'>计量单位</font>不能为空`);
      }
      if (!provGood.mfrsId) {
        errMsg.push(`请选择<font color='red'>生产厂商</font>`);
      }
      if (!provGood.made) {
        errMsg.push(`请输入<font color='red'>产地</font>`);
      }
      if (!provGood.packeage) {
        errMsg.push(`<font color='red'>包装数量</font>不能为空`);
      } else if (Number(provGood.packeage) <= 0) {
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
      this.provGood.mfrsName = this.selectedName;
      this.provGood.mfrsId = this.selectedId;
      this.provGood.kind68code = this.kind68code;
      var paramsProv = this.provGood;
      if (this.checkDqProvSubmit(paramsProv)) {
        this.editLoading = true;
        let serviceUrl = this.provGood.id
          ? "/mdqcService/base/provGoodsInfo/update"
          : "/mdqcService/base/provGoodsInfo/add";
        this.axios.post(serviceUrl, this.provGood).then(
          res => {
            this.editLoading = false;
            if (res.data.code == "0") {
              this.$message({ message: "操作成功！", type: "success" });
              this.editFormVisible = false;
              this.getProvGoodsList();
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
    //批量删除
    multDel() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error("至少选择一条数据！");
        return;
      }
      this.$confirm("确认删除选中记录吗?", "提示", {})
        .then(() => {
          this.loading = true;
          this.axios
            .post(
              "/mdqcService/base/provGoodsInfo/multDel",
              this.multipleSelection
            )
            .then(
              res => {
                this.loading = false;
                if (res.data.code != "0") {
                  this.loading = false;
                  this.$message({ message: res.data.msg, type: "success" });
                } else {
                  this.$message.error(res.data.msg);
                }
                this.getProvGoodsList();
                this.$refs.multipleTable.clearSelection();
              },
              err => {
                this.loading = false;
              }
            );
        })
        .catch(() => {});
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    provDelGood(provGood) {
      this.$confirm("确认删除这条记录吗?", "提示", {})
        .then(() => {
          this.loading = true;
          this.axios
            .post("/mdqcService/base/provGoodsInfo/deleteGoodsInfo", provGood)
            .then(
              res => {
                this.loading = false;
                if (res.data.code == "0") {
                  this.loading = false;
                  this.$message({ message: "操作成功！", type: "success" });
                  this.getProvGoodsList();
                } else {
                  this.$message.error(res.data.msg);
                }
              },
              err => {
                this.loading = false;
              }
            );
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getProvGoodsList();
    },
    basHandleSizeChange(val) {
      this.basPageSize = val;
      this.getBasGoodsList(this.basPage);
    },
    handleCurrentChange(val) {
      this.page = val;
      this.getProvGoodsList();
    },
    basHandleCurrentChange(val) {
      this.basPage = val;
      this.getBasGoodsList(this.basPage);
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
    handleImport: function() {
      this.visible.provgoodsedit = false;
      this.visible.provgoodsprov = true;
      this.$refs.basGoodsforProv.getBasGoodsList(1);
    },
    closebasGoodsforProv() {
      this.visible.provgoodsedit = true;
      this.visible.provgoodsprov = false;
      this.getProvGoodsList();
    },
    //获取平台基础商品列表
    getBasGoodsList: function(pIndex) {
      this.page = pIndex;
      var param = {
        orderBy: "",
        queryObject: { goodsName: this.basGoodsFilters.goodsName },
        page: this.basPage,
        rows: this.basPageSize
      };
      this.qLoading = true;
      this.axios
        .post("/mdqcService/base/provGoodsInfo/getGoodsInfos", param)
        .then(
          res => {
            this.qLoading = false;
            if (res.data.code == "0") {
              this.basGoods = res.data.data.data;
              this.basTotal = res.data.data.total;
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
          this.$refs.multipleTableImport.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTableImport.clearSelection();
      }
    },
    goodImprotSubmit: function() {
      if (this.multipleSelection.length <= 0) {
        this.$message.error("至少选择一条数据！");
        return;
      }

      this.axios
        .post(
          "/mdqcService/base/provGoodsInfo/goodImprotFromBasGoods",
          this.multipleSelection
        )
        .then(
          res => {
            this.loading = false;
            if (res.data.code == "0") {
              this.goodImportVisible = false;
              this.getProvGoodsList();
            } else {
              this.$message.error(res.data.msg);
            }
            this.$refs.multipleTableImport.clearSelection();
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

.full-height {
  height: 100%;
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

.provgoods-tableheight {
  height: calc(100% - 85px);
}
</style>
