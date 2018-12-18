<template>
  <el-row style="height:100%;">
    <searchJxsqs ref="searchDia" @onSelectJxsqs="onSelectJxsqs"></searchJxsqs>    
    <el-row v-if="jxsqsLst.length == 0" style="height:100%;">
      <el-col :span="24"><el-button type="warning" class="big-btn" icon="el-icon-plus" circle @click="showSearchDia(0)"></el-button></el-col>
    </el-row>
    <el-row v-else style="height:100%;">
      <el-row>
        <el-col :span="2" v-if="!(jxsqsLst[0].chiefKind==3)" class="el-arrow add-btn"><el-button plain icon="el-icon-plus" circle @click="showSearchDia(-1)"></el-button></el-col>
        <template v-for="(o, index) in jxsqsLst">
          <el-col :span="2" v-if="index>0" class="el-arrow"><i class="fa fa-arrow-circle-right"></i></el-col>
          <el-col :span="6" style="height:300px">
            <el-card :body-style="{ padding: '0px' }" style="height:100%">
              <div slot="header">
                <span :title="o.provName">{{o.provName | subStr}}</span>
                <el-tag type="success" style="margin-left:10px">{{o.provKind | FprovKind}}</el-tag>                
                <el-button style="float: right; padding: 3px 0" type="text" @click="removeRlation(index)">移除</el-button>
              </div>
              <div style="padding: 5px;">
                <el-form label-width="65px" label-position='left'>
                  <el-col :span="24">
                      <el-form-item label="授权单位:">
                          <span>{{ o.chiefName }}</span>
                          <el-tag type="success" style="margin-left:10px">{{o.chiefKind | FprovKind}}</el-tag>
                           <a :href="'/upload/' + o.filePath" target="_blank" title="点击查看授权书"><i class="fa fa-file-pdf-o doc-view"></i></a>
                      </el-form-item>
                  </el-col>
                  <el-col :span="8">
                      <el-form-item label="长期有效:">
                          <span>{{ o.isValiding==0?'否':'是' }}</span>
                      </el-form-item>
                  </el-col>
                  <el-col :span="16">
                      <el-form-item label="起止时间:">
                          <span>{{ o.expdtBeginDate | dateFormat }}/{{o.expdtEndDate | dateFormat}}</span>
                      </el-form-item>
                  </el-col>
              </el-form>
                <el-table :ref='"sqlTbl"+index' :data="o.lstDetail" style="width: 100%;height:190px;" height="outer" @selection-change="selectRows(index)">
                    <el-table-column type="selection" width="35" :selectable='canSltGoods'></el-table-column>
                    <el-table-column  prop="goodsName" label="产品名称"></el-table-column>
                    <el-table-column  prop="goodsGg" label="产品规格"></el-table-column>
                </el-table>
              </div>
            </el-card>
          </el-col>
        </template>
        <el-col :span="2" class="el-arrow"><i class="fa fa-arrow-circle-right"></i></el-col>
        <el-col :span="6">
          <el-card :body-style="{ padding: '0px' }" style="height:100%">
              <div slot="header"><span>适用医院</span></div>
              <div style="padding: 5px;">
                <el-autocomplete :fetch-suggestions="querySearchAsync" placeholder="请输入内容"  @select="sltHosChange"></el-autocomplete>
                  <el-table :data="hosLst" style="width: 100%;height:218px;margin-top:10px;" height="outer">
                    <el-table-column  prop="cname" label="医院名称"></el-table-column>
                    <el-table-column  prop="linkman" label="联系人" width="80"></el-table-column>
                    <el-table-column  prop="contactWay" label="联系电话" width="80"></el-table-column>
                    <el-table-column label="操作" width="45">
                      <template slot-scope="scope">
                        <el-button type="danger" icon="el-icon-delete" @click="delHos(scope.$index, scope.row)"></el-button>                        
                      </template>
                    </el-table-column>
                </el-table>
              </div>
            </el-card>
        </el-col>   
      </el-row>
      <el-row>
        <el-col :span="24" style="margin-top: 22px;text-align: center">
            <el-button type="warning" size="small" @click="cancel" icon="el-icon-close">取消</el-button>
            <el-button type="success" size="small" @click="submitRelation"  icon="el-icon-check">提交</el-button>
          </el-col>
      </el-row>
    </el-row>
  </el-row>
</template>
<script>
import {comKind} from '@/common/js/constance'
import searchJxsqs from './searchJxsqs'
  export default {
    data() {
      return {
        jxsqsFilter:{
          sqsLastlevelId:'',      //查询时过滤掉上级授权书为下级授权书的父级ID
          provId:'',
          chiefId:'',
          goodsId:'',
          goodsName:'',
          goodsIds:[],
        },
        jxsqsLst:[],
        hosLst:[]
      }
    },
    filters:{
      FprovKind:(value)=>{
        return comKind[value.toString()];
      }
    },
    components:{searchJxsqs},
    methods: {
      canSltGoods(){
        return false;
      },      
       querySearchAsync(queryString, cb) {
         var param={cname:queryString, kind:1};
        this.axios.post('/mdqcService/bas/company/companysList',param).then(res=>{
          if(res.data.code == 0){
            res.data.data.forEach(item=>item.value = item.cname);
            cb(res.data.data);
          }
        });
      },      
      sltHosChange(hosInfo){
        if(this.hosLst.filter(o=>o.id == hosInfo.id).length==0){
          this.hosLst.push(hosInfo);
        }
      },
      onSelectJxsqs(data){
        this.jxsqsLst.unshift(data);
        this.tableSelectRender();
      },
      removeRlation(index){
        this.jxsqsLst.splice(0,index+1);
        this.tableSelectRender();
      },
      tableSelectRender(){
        var _this = this;
        this.$nextTick(function(){
          for(var i=0;i<this.jxsqsLst.length;i++){
            if(Array.isArray(this.jxsqsLst[i].sltedGoods)){
               this.jxsqsLst[i].sltedGoods.forEach(item=>{
                _this.$refs['sqlTbl'+i][0].toggleRowSelection(item,true);
              });
            }
          }          
        });
      },
      showSearchDia(flag){
        var currentSqs = null;
        this.jxsqsFilter.provId ='';
        this.jxsqsFilter.chiefId='';
        this.jxsqsFilter.sqsLastlevelId='';
        this.jxsqsFilter.goodsIds=[];
        if(flag === 0){
          this.jxsqsFilter.provId = this.user.corpId;
        }else if(flag ==-1){
          currentSqs =  this.jxsqsLst[0];
          this.jxsqsFilter.provId = currentSqs.chiefId; 
          this.jxsqsFilter.goodsIds = currentSqs.sltedGoodsIds;
          this.jxsqsFilter.sqsLastlevelId=currentSqs.sqsLastlevelId;
          if(!Array.isArray(this.jxsqsFilter.goodsIds) || this.jxsqsFilter.goodsIds.length == 0){
            this.$message.error('无授权产品，请先选择！');
            return;
          }
        }else{
          return;
        }    
        this.$refs.searchDia.showAndSearch(this.jxsqsFilter);
      },
      selectRows(index){
        this.jxsqsLst[index].sltedGoodsIds = this.$refs['sqlTbl'+index][0].selection.map(o=>o.goodsId);
      },
      delHos(index,row){
        this.hosLst.splice(index, 1);
      },
      submitRelation(){
        var param={
          lstSqsRelation:this.getSaveRelationInfo(),
          lstSqsHos:this.getSaveHosInfo()
        };
        var _this = this;
        this.axios.post('/mdqcService/provJxsqs/saveJxsqsRelation',param).then(res=>{
          if(res.data.code==0){
            _this.$alert('操作成功！', '成功', {type:'success',callback:function(){_this.cancel();}}); 
          }else{
            _this.$error("操作失败!");
          }
        },err=>{
          _this.$error("操作失败!");
        });
      },
      getSaveHosInfo(){
        var _this = this;
        var res = [];
        if(Array.isArray(this.hosLst) && Array.isArray(this.jxsqsLst) && this.jxsqsLst.length > 0){
          var sqs = this.jxsqsLst[this.jxsqsLst.length - 1];
          this.hosLst.forEach(item=>{
            var hosInfo={
              sqsId:sqs.id,
              provId:sqs.provId,
              hosId:item.id,
              uxid:_this.user.userId
            }
            res.push(hosInfo);
          });
        }
        return res;
      },
      getSaveRelationInfo(){
        var _this = this;
        var res = [];
        if(Array.isArray(this.jxsqsLst) && this.jxsqsLst.length > 1){
          for(var i =1;i<this.jxsqsLst.length;i++){
            var sltedGoods = this.jxsqsLst[i].sltedGoods;
            var lastSltedGoods = this.jxsqsLst[i-1].sltedGoods;
            if(Array.isArray(sltedGoods) && sltedGoods.length>0){
              sltedGoods.forEach(item=>{
                if(Array.isArray(lastSltedGoods) && lastSltedGoods.length>0){
                  if(lastSltedGoods.filter(o=>o.goodsId == item.goodsId).length>0){
                      var reInfo ={id:item.id,sqsId:item.sqsId,goodsId:item.goodsId,sqsLastlevelId:_this.jxsqsLst[i-1].id,version:item.version};
                      res.push(reInfo);
                  }
                }               
              });
            }
          }
        }
        return res;
      },
      cancel(){
        this.$confirm('将移除所有操作, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.jxsqsLst=[];
          this.hosLst=[];
        });        
      }
    },
    mounted() {
     this.jxsqsFilter.goodsId = this.$route.query.goodsId;
      this.jxsqsFilter.goodsId = this.$route.query.goodsName;
    }
  };

</script>
<style>
.big-col{
  text-align: center; padding: 92px 0px;
}
.el-arrow{
  height: 100%; text-align: center; padding: 92px 0px;
}
.el-arrow i{
  font-size:100px;color: #a9a9a9;
}
.el-arrow .el-button i{
  color: #a9a9a9;
  font-size:75px;
}
.el-form-item--mini.el-form-item, .el-form-item--small.el-form-item{
  margin-bottom: 0;
}
.time {
    font-size: 12px;
    color: #999;
  }  
  .big-btn{
    height:200px;
    width:200px;
    position: absolute;
    top: 30%;
    left:40%
  }
  .big-btn i{
    font-size:150px;
  }
  .doc-view{
    font-size:17px; 
    float:right;
    margin-top: 4px;
    margin-right:15px;
    color:#5bb64d;
  }
</style>
