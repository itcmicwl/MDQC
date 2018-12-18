<template>
  <div id="home-index" style="height:100%">
    <el-row type="flex" justify="space-between" sytle="height:270px">
      <el-col :span="17">
        <el-row type="flex" justify="space-between">
          <el-col :span="7" class="boxs l-seagreen">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.goodsCount }}</h3>
              <p>商品</p>
            </div>
            <i class="fa fa-medkit fa-5x" aria-hidden="true" title="商品数量"></i>
          </el-col>
          <el-col :span="7" class="boxs l-blue">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.certCount }}</h3>
              <p>注册证</p>
            </div>
            <i class="fa fa-address-book fa-5x" aria-hidden="true" title="注册证数量"></i>
          </el-col>
          <el-col :span="7" class="boxs l-parpl">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.provCount }}</h3>
              <p>{{ bIsHos === true ? '供应商' : '承诺书' }}</p>
            </div>
            <i class="fa fa-shopping-cart fa-5x" aria-hidden="true" :title="(bIsHos === true ? '供应商' : '承诺书') + '数量'"></i>
          </el-col>
        </el-row>
        <el-row type="flex" justify="space-between">
          <el-col :span="7" class="boxs l-blush">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.mfrsCount }}</h3>
              <p>厂家</p>
            </div>
            <i class="fa fa-industry fa-5x" aria-hidden="true" title="厂家数量"></i>
          </el-col>
          <el-col :span="7" class="boxs l-amber">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.jxsqsCount }}</h3>
              <p>经销授权书</p>
            </div>
            <i class="fa fa-file-text fa-5x" aria-hidden="true" title="经销授权书数量"></i>
          </el-col>
          <el-col :span="7" class="boxs l-yellow">
            <div class="boxs-body">
              <h3 class="mt-0">{{ data.frwtsCount }}</h3>
              <p>法人委托书</p>
            </div>
            <i class="fa fa-clipboard fa-5x" aria-hidden="true" title="法人委托书数量"></i>
          </el-col>
        </el-row>
      </el-col>
      <el-col :span="6">
        <el-card><userwidget></userwidget></el-card>
      </el-col>
    </el-row>
    <el-row :gutter="10" class="table-height">
      <el-col :span="6" style="height: 100%">
        <p class="table-title">
          已过期注册证
        </p>
        <el-table :data="expiredCerts" style="height: 100%" height="css">
          <el-table-column prop="productName" label="注册证名称"></el-table-column>
          <el-table-column prop="certificateCode" label="注册证编号"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="6" style="height: 100%">
        <p class="table-title">
          两周内过期注册证
        </p>
        <el-table :data="twoWeeksExpiredCerts" style="height: 100%" height="css">
          <el-table-column prop="productName" label="注册证名称"></el-table-column>
          <el-table-column prop="certificateCode" label="注册证编号"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="6" style="height: 100%">
        <p class="table-title">
          一个月内过期注册证
        </p>
        <el-table :data="oneMonthExpiredCerts" style="height: 100%" height="css">
          <el-table-column prop="productName" label="注册证名称"></el-table-column>
          <el-table-column prop="certificateCode" label="注册证编号"></el-table-column>
        </el-table>
      </el-col>
      <el-col :span="6" style="height: 100%">
        <p class="table-title">
          三个月内过期注册证
        </p>
        <el-table :data="threeMonthsExpiredCerts" style="height: 100%" height="css">
          <el-table-column prop="productName" label="注册证名称"></el-table-column>
          <el-table-column prop="certificateCode" label="注册证编号"></el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import moment from "moment";
import userwidget from "./index/com/userWidget";
import { companyKind } from "../common/js/constance";
export default {
  components: {
    userwidget
  },
  data() {
    return {
      BASE_URL: "/mdqcService/homestat",
      data: {},
      bIsHos: false
    };
  },
  computed: {
    expiredCerts() {
      const expiringDate = moment();
      if (this.data.expiredCertList)
        return this.data.expiredCertList.filter(item =>
          moment(item.expdtEndDate).isBefore(expiringDate)
        );
    },
    twoWeeksExpiredCerts() {
      const staringDate = moment();
      const expiringDate = moment().add(2, "weeks");
      if (this.data.expiredCertList)
        return this.data.expiredCertList.filter(item =>
          moment(item.expdtEndDate).isBetween(staringDate, expiringDate)
        );
    },
    oneMonthExpiredCerts() {
      const staringDate = moment().add(2, "weeks");
      const expiringDate = moment().add(1, "months");
      if (this.data.expiredCertList)
        return this.data.expiredCertList.filter(item =>
          moment(item.expdtEndDate).isBetween(staringDate, expiringDate)
        );
    },
    threeMonthsExpiredCerts() {
      const staringDate = moment().add(1, "months");
      const expiringDate = moment().add(3, "months");
      if (this.data.expiredCertList)
        return this.data.expiredCertList.filter(item =>
          moment(item.expdtEndDate).isBetween(staringDate, expiringDate)
        );
    }
  },
  mounted() {
    this.bIsHos = this.user.corpKind === companyKind.HOS;
    this.getHomeStat();
  },
  methods: {
    getHomeStat() {
      this.axios
        .get(this.BASE_URL + "/getStat")
        .then(res => {
          if (res.data.code === 0) {
            this.data = res.data.data;
          } else {
            this.$message.error(res.data.msg);
          }
        })
        .catch(err => {
          this.$message.error("获取数据失败。");
        });
    }
  }
};
</script>

<style scoped>
.boxs {
  position: relative;
  color: #4d585f;
  background-color: white;
  filter: alpha(opacity=100);
  opacity: 1;
  -webkit-transition: opacity 0.25s ease-out;
  -moz-transition: opacity 0.25s ease-out;
  transition: opacity 0.25s ease-out;
  border-radius: 10px;
  height: 100px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: 10px 0;
}
.l-blue {
  background: linear-gradient(45deg, #72c2ff, #86f0ff) !important;
  color: #fff !important;
}
.l-parpl {
  background: linear-gradient(45deg, #a890d3, #edbae7) !important;
  color: #fff !important;
}
.l-seagreen {
  background: linear-gradient(45deg, #8ed8ec, #85f7b5) !important;
  color: #fff !important;
}
.l-amber {
  background: linear-gradient(45deg, #fda582, #f7cf68) !important;
  color: #fff !important;
}
.l-blush {
  background: linear-gradient(45deg, #dd5e89, #f7bb97) !important;
  color: #fff !important;
}
.l-yellow {
  background: linear-gradient(45deg, #d4c601, #fffd94) !important;
  color: #fff !important;
}
.boxs .boxs-header,
.boxs .boxs-widget,
.boxs .boxs-body,
.boxs .boxs-footer {
  position: relative;
  padding: 15px 0px 15px 15px;
  float: left;
}
.boxs .boxs-body {
  margin-left: 5%;
  /* cursor: pointer; */
  padding: 20px 0px 0px 0px;
}
.fa {
  float: right;
  margin-right: 10%;
  line-height: 100px;
}
.mt-0 {
  margin-top: 0 !important;
  font-size: 24px;
  margin-bottom: 12px;
}
p {
  margin: 0 0 10px;
}
.table-title {
  text-align: center;
}
.table-height {
  height: calc(100% - 300px);
}
</style>
