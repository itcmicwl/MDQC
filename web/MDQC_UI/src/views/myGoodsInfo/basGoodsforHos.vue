<template>
  <el-row id="prov-basPro-view" :gutter="10" style="height: 100%">
    <el-col :span="24">
      <el-col :span="24" class="toolbar">
        <el-form :inline="true" label-width="80px">
          <el-form-item label="商品名称">
            <el-input v-model="basGoodsFilters.goodsName" placeholder="输入商品名称或规格"></el-input>
          </el-form-item>
          <el-form-item label="供应商">
            <el-select v-model="basGoodsFilters.provId" filterable clearable placeholder="请选择">
              <el-option
                v-for="item in provList"
                :key="item.provId"
                :label="item.provName"
                :value="item.provId">
              </el-option>
            </el-select>
          </el-form-item>

          <el-switch :width="80"
                     v-model="basGoodsFilters.isAll"
                     active-value="1" inactive-value="0"
                     active-color="#13ce66" active-text="全部" inactive-text="未导入" inactive-color="#ff4949">
          </el-switch>
          <el-button type="primary" v-on:click="getPHGoodsList(1)">查询</el-button>
        </el-form>
      </el-col>
      <template>
        <el-table
          ref="multipleTable" :data="phGoods" border tooltip-effect="dark" row-key='index'
          style="width: 100%" @selection-change="handleSelectionChange" :loading="qLoading" class="hosgoods-tableheight"
          max-height="450">
          <el-table-column type="selection" :reserve-selection="true" width="50" align="center"></el-table-column>
          <el-table-column prop="provGoodsName" label="商品名称" width="200"></el-table-column>
          <el-table-column prop="provGoodsCode" label="商品编号" show-overflow-tooltip></el-table-column>
          <el-table-column prop="provName" label="经销商名" show-overflow-tooltip></el-table-column>
          <el-table-column prop="provGoodsMfrsName" label="生产厂家" show-overflow-tooltip></el-table-column>
          <el-table-column prop="provGoodsMade" label="产地" show-overflow-tooltip></el-table-column>
          <el-table-column prop="provGoodsGg" label="规格" show-overflow-tooltip></el-table-column>
          <el-table-column prop="provGoodsUnit" label="单位" show-overflow-tooltip></el-table-column>
        </el-table>
      </template>
      <el-col :span="24" class="toolbar" style="margin-top: 10px">
        <el-col :span="6" style="margin-top:5px; padding-bottom: 0px;">
          <!--<el-button @click="goodHosImprotSubmit()">确定</el-button>-->
          <el-button @click="toggleSelection()">取消选择</el-button>
          <el-button @click="goodImprotByphSubmit()"v-loading="confirmVis">确定</el-button>
          <el-button :plain="true" type="danger" @click="goBack">返回</el-button>
        </el-col>
        <el-col :span="18">
          <!--<el-pagination @size-change="basHandleSizeChange" @current-change="basHandleCurrentChange"
                         :current-page="basPage" :page-sizes="[10, 100, 200, 400]" :page-size="basPageSize"
                         layout="total, sizes, prev, pager, next"
                         :total="basTotal" style="float:right;" small>
          </el-pagination>-->
          <el-pagination @size-change="handlePHGoodsSizeChange" @current-change="handlePHGoodsCurrentChange"
                         :current-page="phGoodsPage" :page-sizes="[50, 100, 200, 400]" :page-size="phGoodsPageSize"
                         layout="total, sizes, prev, pager, next"
                         :total="phGoodsTotal" style="float:right;" small>
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
        qLoading: false,
        confirmVis:false,
        basPage: 1,
        basPageSize: 20,
        basTotal: 0,
        basGoods: [],
        phGoods: [],
        phGoodsTotal: 0,
        phGoodsPage: 1,
        phGoodsPageSize: 100,
        provList: [],
        multipleSelection: [],
        basGoodsFilters: {
          goodsName: '',
          provId: '',
          isAll: '1',
        },
      }
    },
    methods: {
      //获取平台基础商品列表
      getBasGoodsList: function (pIndex) {
        this.page = pIndex;
        var param = {
          orderBy: "",
          queryObject: {goodsName: this.basGoodsFilters.goodsName},
          page: this.basPage,
          rows: this.basPageSize
        };
        this.qLoading = true;
        this.axios.post('/mdqcService/base/provGoodsInfo/getGoodsInfos', param).then(res => {
          this.qLoading = false;
          if (res.data.code == "0") {
            this.basGoods = res.data.data.data;
            this.basTotal = res.data.data.total;
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.qLoading = false;
        });
      },
      handlePHGoodsSizeChange(val) {
        this.phGoodsPageSize = val;
        this.getPHGoodsList(this.phGoodsPage);
      },
      handlePHGoodsCurrentChange(val) {
        this.phGoodsPage = val;
        this.getPHGoodsList(this.phGoodsPage);
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
      goodImprotByphSubmit: function () {
        if (this.multipleSelection.length <= 0) {
          this.$message.error("至少选择一条数据！");
          return;
        }
        this.confirmVis = true;
        this.axios.post('/mdqcService/base/hosGoodsInfo/goodImprotFromPHGoods', this.multipleSelection).then(res => {
          this.confirmVis = false;
          if (res.data.code == "0") {
            this.$message({message: '导入成功！', type: 'success'});
            this.goBack();
          }
          else {
            this.$message.error(res.data.msg);
          }
          this.$refs.multipleTable.clearSelection();

        }, err => {
          this.confirmVis = false;
        });
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },
      getPHGoodsList: function (pIndex) { //获取供货列表供批量导入
        this.phGoodsPage = pIndex;
        let param = {
          orderBy: "", queryObject: {
            provGoodsName: this.basGoodsFilters.goodsName,
            provId: this.basGoodsFilters.provId,
            isAll: this.basGoodsFilters.isAll
          },
          page: this.phGoodsPage,
          rows: this.phGoodsPageSize
        };
        this.qLoading = true;
        this.axios.post('/mdqcService/base/hosGoodsInfo/getPHGoodsList', param).then(res => {
          this.qLoading = false;
          if (res.data.code == "0") {
            this.phGoods = res.data.data.data;
            this.phGoodsTotal = res.data.data.total;
            this.basGoodsFilters.goodsName='';
            this.basGoodsFilters.provId='';
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.qLoading = false;
        });
      },
      getUnitListsByHos: function () {  //获取经销商
        //this.qLoading = true;
        let param = {orderBy: "", queryObject: {}, page: 0, rows: 0};
        this.axios.post('/mdqcService/base/hosGoodsInfo/hosProvInfoVoList', param).then(res => {
          //this.qLoading = false;
          if (res.data.code == "0") {
            this.provList = res.data.data.data;
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          //this.qLoading = false;
        });
      },
      getInitList() {
        this.getUnitListsByHos();
        this.getPHGoodsList(1);
      },
      goBack() {
        this.phGoods=[];
        this.$emit("close");
      },
    },
  }
</script>

<style>
  #prov-basPro-view .hosgoods-tableheight {
    height: 450px;
  }
  .full-height {
    height: 100%;
  }

</style>
