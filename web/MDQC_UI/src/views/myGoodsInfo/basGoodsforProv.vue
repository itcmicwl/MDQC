<template>
  <el-row id="prov-basPro-view" :gutter="10" style="height:100%">
    <el-col :span="24" style="height: 100%">
      <el-col :span="24" class="toolbar">
        <el-input v-model="basGoodsFilters.goodsName" placeholder="输入商品名称或规格" class="input-box"></el-input>
        <el-button type="primary" @click="getBasGoodsList(1)">查询</el-button>
      </el-col>
      <template>
        <el-table
          ref="multipleTableImport" :data="basGoods" border tooltip-effect="dark" row-key="id"
          style="width: 100%" @selection-change="handleSelectionChange" :loading="qtLoading"
          class="provgoods-tableheight" height="outer">
          <el-table-column type="selection" :reserve-selection="true" width="55"></el-table-column>
          <el-table-column prop="goodsName" label="商品名称" width="200"></el-table-column>
          <el-table-column prop="code" label="商品编号" show-overflow-tooltip></el-table-column>
          <el-table-column prop="mfrsName" label="生产厂家" show-overflow-tooltip></el-table-column>
          <el-table-column prop="goodsGg" label="规格" show-overflow-tooltip></el-table-column>
          <el-table-column prop="unit" label="单位" show-overflow-tooltip></el-table-column>
        </el-table>
      </template>
      <el-col :span="24" class="toolbar" style="margin-top: 3px">
        <el-col :span="6" style="margin-top:5px; padding-bottom: 0px;">
          <el-button @click="toggleSelection()">取消选择</el-button>
          <el-button @click="goodImprotSubmit()" v-loading="confirmVis">确定</el-button>
          <el-button :plain="true" type="danger" @click="goBack">返回</el-button>
        </el-col>
        <el-col :span="18">
          <el-pagination @size-change="basHandleSizeChange" @current-change="basHandleCurrentChange"
                         :current-page="basPage" :page-sizes="[50, 100, 200, 400]" :page-size="basPageSize"
                         layout="total, sizes, prev, pager, next"
                         :total="basTotal" style="float:right;" small>
          </el-pagination>
        </el-col>
      </el-col><!--
      <el-col :span="24" class="toolbar" style="margin-top:5px; padding-bottom: 0px;text-align: center;">
        <el-button @click="toggleSelection()">取消选择</el-button>
        <el-button @click="goodImprotSubmit()">确定</el-button>
      </el-col>-->
    </el-col>
  </el-row>
</template>

<script>
export default {
  data() {
    return {
      confirmVis: false,
      qtLoading: false,
      basPage: 1,
      basPageSize: 200,
      basTotal: 0,
      basGoods: [],
      multipleSelection: [],
      basGoodsFilters: {
        goodsName: ""
      }
    };
  },
  methods: {
    //获取平台基础商品列表
    getBasGoodsList: function(pIndex) {
      this.page = pIndex;
      var param = {
        orderBy: "",
        queryObject: { goodsName: this.basGoodsFilters.goodsName },
        page: this.basPage,
        rows: this.basPageSize
      };
      this.qtLoading = true;
      this.axios
        .post("/mdqcService/base/provGoodsInfo/getGoodsInfos", param)
        .then(
          res => {
            this.qtLoading = false;
            if (res.data.code == "0") {
              this.basGoods = res.data.data.data;
              this.basTotal = res.data.data.total;
              this.basGoodsFilters.goodsName = "";
            } else {
              this.$message.error(res.data.msg);
            }
          },
          err => {
            this.qtLoading = false;
          }
        );
    },
    basHandleSizeChange(val) {
      this.basPageSize = val;
      this.getBasGoodsList(this.basPage);
    },
    basHandleCurrentChange(val) {
      this.basPage = val;
      this.getBasGoodsList(this.basPage);
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
      this.confirmVis = true;
      this.axios
        .post(
          "/mdqcService/base/provGoodsInfo/goodImprotFromBasGoods",
          this.multipleSelection
        )
        .then(
          res => {
            this.confirmVis = true;
            if (res.data.code == "0") {
              this.goodImportVisible = false;
              this.$message({ message: "导入成功！", type: "success" });
              this.goBack();
            } else {
              this.$message.error(res.data.msg);
            }
            this.$refs.multipleTableImport.clearSelection();
          },
          err => {
            this.confirmVis = false;
          }
        );
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    goBack() {
      this.basGoods = [];
      this.$emit("close");
    }
  }
};
</script>

<style scoped>
.provgoods-tableheight {
  height: calc(100% - 65px);
}

.input-box {
  padding: 0 10px;
  width: 200px;
}
</style>
