<template>
  <el-row>
    <el-col :span="24">
        <el-input v-model="filePath" placeholder="文件路径"></el-input>
        <el-button type="success" @click="addShuiyin">加水印</el-button>
    </el-col>
  </el-row>
</template>
<script>
export default {
    data() {
        return {
            filePath:'',
        }
    },
    methods: {
        addShuiyin(){
            var param={
                picPath:this.filePath
            };
            this.axios.post(`/platformService/sys/upload/addPicShuiYin`,param).then(res=>{
                this.$message.success(res.data.data);
            },err=>{
                this.$message.error("出错啦");
            })
        }
    },
    mounted() {

    }
}
</script>
<style>

</style>

