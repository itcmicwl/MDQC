<template>
  <div id="mfrs" style="height: 100%;">
    <!--工具条-->
    <el-col :span="24" class="toolbar" style="padding-bottom: 0px;">
      <el-form :inline="true" label-width="88px" class="queryFormClass">
        <el-form-item label="厂商名称">
          <el-input v-model="filters.cname" style="height: 28px;" placeholder="厂商名称" clearable></el-input>
        </el-form-item>
        <el-form-item label="状态" label-width="50px">
          <el-radio-group v-model="filters.flag" @change="flagChange" size="mini">
            <el-radio-button label="1" plain>启用</el-radio-button>
            <el-radio-button label="0" plain>停用</el-radio-button>
            <el-radio-button label="" plain>全部</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" v-on:click="getProvInfoList(1)" plain style="width: 80px;height:28px;"  icon="el-icon-search">搜索</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="success" icon="el-icon-plus" plain style="height: 28px" @click="provAdd(null)" >新增厂商</el-button>
        </el-form-item>
      </el-form>
    </el-col>
    <!--列表-->
    <template>
      <el-table :data="provs" highlight-current-row v-loading="loading" border style="width: 100%;" fit
                class="mfrs-tableheight" height="outer">
        <el-table-column type="index" label="序号" width="50" align="center">
        </el-table-column>
        <el-table-column prop="cname" label="厂商名称">
        </el-table-column>
        <el-table-column prop="mfrsKind" width="60" :formatter="formateMfrsKind" label="类型" align="center">
        </el-table-column>
        <el-table-column prop="linkman" width="90" label="联系人" header-align="center">
        </el-table-column>
        <el-table-column prop="legal" width="90" label="法人代表" header-align="center">
        </el-table-column>
        <el-table-column prop="contactWay" label="联系方式" width="100" header-align="center">
        </el-table-column>
        <!--<el-table-column prop="flag" label="状态" :formatter="formateStatus" width="60" align="center">-->
        <!--</el-table-column>-->
        <el-table-column prop="flag" label="状态" align="center" width="55"
                         :formatter="formateStatus"
                         filter-placement="bottom-end">
          <template slot-scope="scope">
            <el-tag :type="scope.row.flag === '0' ? 'danger' : 'success'" close-transition>{{scope.row.flag==='0'?'停用':'启用'}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="scope">
            <el-button-group>
              <el-button type="primary" size="mini" @click="mfrsEdit(scope.row)"  icon="el-icon-edit-outline">编辑信息</el-button>
              <!--<el-button type="info" size="mini" @click="getCompanyInfo(scope.row)"  icon="el-icon-picture">证照信息</el-button>-->
              <span v-if="scope.row.flag=='1'">
                <el-button type="danger" size="mini" @click="closeProv(scope.row)" plain icon="el-icon-remove">停用</el-button>
              </span>
              <span v-else>
                <el-button type="success" size="mini" @click="openProv(scope.row)" plain icon="el-icon-circle-check">启用</el-button>
              </span>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <!--工具条-->
      <el-col :span="24" class="toolbar">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="page"
                       :page-sizes="[30, 100, 200, 400]" :page-size="pageSize" layout="total, sizes, prev, pager, next"
                       :total="total" style="float:right;" small>
        </el-pagination>
      </el-col>
    </template>
  </div>
</template>
<script>
  import { fileServer,PROV_CERT_TYPE } from '../../common/js/constance';
  export default {
    data() {
      return {
        imgBasePath: '',
        uploadUrl:'',
        filters: {
          cname: '',
          flag:'1'
        },
        provs: [],
        total: 0,
        page: 1,
        pageSize: 30,
        loading: false,
        certKinds: [],
        unitKinds: []
      }
    },
    methods: {
      mfrsEdit:function (row) {
        this.$router.push({ path: 'mfrsEdit', query: {companyId: row.id,editDisable: '1',companyName:this.filters.cname}});
      },
      provAdd:function () {
        this.$router.push({ path: 'mfrsEdit', query: {editDisable: '1'}});
      },
      flagChange:function () {
        this.getProvInfoList(this.page);
      },
      //获取列表
      getProvInfoList: function (pIndex) {
        this.page = pIndex;
        var param = {
          orderBy: "",
          queryObject: {cname: this.filters.cname,flag:this.filters.flag,kind: 3},
          page: this.page,
          rows: this.pageSize
        };
        this.loading = true;
        this.axios.post('/mdqcService/custProvManage/provInfo/getProvInfoList', param).then(res => {
          this.loading = false;
          if (res.data.code == "0") {
            this.provs = res.data.data.data;
            this.total = res.data.data.total;
            //  this.page = res.data.data.pageNum;
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.loading = false;
        });
      },
      formateStatus:function (d) {
        if(d.flag == '1'){
          return '启用';
        }
        return '停用';
      },
      formateMfrsKind:function (d) {
        if(d.mfrsKind == '1'){
          return '境内';
        }
        return '境外';
      },
      handleCurrentChange(val) {
        this.page = val;
        this.getProvInfoList(this.page);
      },
      handleSizeChange(val) {
        this.pageSize = val;
        this.getProvInfoList(this.page);
      },
      closeProv(provInfo) {
        this.$confirm('确认停用这条记录吗?', '提示', {}).then(() => {
          this.loading = true;
          this.axios.post("/mdqcService/bas/company/delCompany", provInfo).then(res => {
            this.loading = false;
            if (res.data.code == "0") {
              this.loading = false;
              this.$message({message: '操作成功！', type: 'success'});
            } else {
              this.$message.error(res.data.msg);
            }
            this.getProvInfoList(this.page);
          }, err => {
            this.loading = false;
          });
        }).catch(() => {
        });
      },
      openProv(provInfo) {
        this.$confirm('确认启用这条记录吗?', '提示', {}).then(() => {
          this.loading = true;
          this.axios.post("/mdqcService/bas/company/openCompany", provInfo).then(res => {
            this.loading = false;
            if (res.data.code == "0") {
              this.loading = false;
              this.$message({message: '操作成功！', type: 'success'});
            } else {
              this.$message.error(res.data.msg);
            }
            this.getProvInfoList(this.page);
          }, err => {
            this.loading = false;
          });
        }).catch(() => {
        });
      },
      //字典获取单位类型
      getUnitKinds: function () {
        this.loading = true;
        this.axios.post('/platformService/sys/dict/getDictValueByDictEname', {dictName: 'JGLX', val: 2}).then(res => {

          this.loading = false;
          if (res.data.code == "0") {
            this.unitKinds = res.data.data;
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.loading = false;
        });
      },
      getZZLX: function () {   //证照类型 字典
        this.loading = true;
        this.axios.post('/platformService/sys/dict/getDictValueByDictEname', { dictName: 'ZZLX' }).then(res => {
          this.loading = false;
          if (res.data.code == "0") {
            this.certKinds = res.data.data;
          }
          else {
            this.$message.error(res.data.msg);
          }
        }, err => {
          this.loading = false;
        });
      }
    },
    mounted() {
      this.getZZLX();
      var tempObj = this.$route.query;
      this.filters.cname = tempObj.companyName;
      this.getProvInfoList(1);
      this.uploadUrl =fileServer.upload +"baseData";
      this.imgBasePath = fileServer.imgBasePath;
    }
  };

</script>
<style>
  #mfrs .mfrs-tableheight {
    height: calc(100% - 90px);
  }
  #mfrs .el-radio-button__orig-radio:checked+.el-radio-button__inner{
    color: #ffffff;
    background: #535c63;
    border-color: #676082;
  }
  #mfrs .el-radio-button--mini .el-radio-button__inner {
    height: 28px;
    width: 38px;
    padding: 7px 7px 6px;
    font-size: 12px;
    font-size: 12px;
    border-radius: 0px;
  }
  #mfrs .el-radio-button:first-child .el-radio-button__inner {
    border-left: 1px solid #dcdfe6;
    border-radius: 4px 0 0 4px;
    -webkit-box-shadow: none!important;
    box-shadow: none!important;
  }
  #mfrs .el-radio-button:last-child .el-radio-button__inner {
    border-radius: 0px 4px 4px 0px;
    -webkit-box-shadow: none!important;
    box-shadow: none!important;
  }
  #mfrs .el-input--mini .el-input__inner{
    height: 28px;
  }
  .queryFormClass{
    padding-top: 3px;
    height: 30px;
    /*box-shadow: 0px 0px 30px #0c629a inset;*/
    box-shadow: 0px 0px 3px 0px #a9a1a1 inset;
    border-radius: 4px;
    background-color: #f9f9f9;
  }
  #mfrs .el-form--inline .el-form-item {
    margin-right: 0px;
  }
  #mfrs .toolbar {
    margin-bottom: 1px;
    margin-top: -3px;
  }
</style>
