<template>
	<el-row class="layOutHeader">
		<el-col :span="18" class="logo">
			<el-col :span="8">
				<div class="sys-title">
					<router-link to="/" class="logo-a">
						<img src="@/assets/images/logo1_1.png"  class="logo-img"/>
						<span class="sys-name">{{appConfig.appName}}</span>
					</router-link>
				</div>
			</el-col>
			<!-- <el-col :span="6">
				<img :src="getLogoPath()" v-if="user.corpLogo && imgNotError" class="corpLogo" @error="imgError"/>
			</el-col> -->
			<el-col :span="16" class="div_nav">
				<ul class="nav">
					<li class="nav-item" v-for="item in getHomeNavs" v-if="!item.hidden" :key="item.path">
						<router-link :to="item.path" class="a-router">{{item.name}}</router-link>
					</li>
				</ul>
			</el-col>				
		</el-col>
		<el-col :span="4"  v-if="user">
			<div class="d-msg">
					<router-link to="/user/usersetting">{{user.ename}}</router-link>						
					<message style="float:right;margin-left:10px;"></message>
			</div>
		</el-col>
		<el-col :span="2" class="userinfo" v-if="user">	
			<el-dropdown trigger="click">
				<span class="el-dropdown-link userinfo-inner"><img src="../assets/user.png"/></span>						
				<el-dropdown-menu slot="dropdown">					
					<el-dropdown-item divided @click.native="userSetting">个人设置</el-dropdown-item>
					<el-dropdown-item divided v-if="user.userId=='0000'" @click.native="testPage">test</el-dropdown-item>
					<el-dropdown-item divided @click.native="cleanCache">清空缓存</el-dropdown-item>
					<el-dropdown-item divided @click.native="logout">退出登录</el-dropdown-item>
				</el-dropdown-menu>
			</el-dropdown>
		</el-col>
		<el-col :span="4" class="userinfo" v-else>
			<a><router-link to="/login">您当前未登陆，请登陆</router-link></a>
		</el-col>		
	</el-row>
</template>

<script>
import { mapGetters, mapActions } from 'vuex';
import axios from 'axios';
import { appConfig } from '../config/appConfigs';
import {loginOut} from '@/common/js/loginUtil';
import message from "@/components/message";
export default {
	name: 'home',
	data() {
		return {
			sysName: 'VUEADMIN',
			imgNotError:true
		}
	},
	components: { message },
	computed: {
		...mapGetters(["getHomeNavs","getSpdMsg","getLoginFlag"])
	},	
	methods: {
		...mapActions([
			'clearCache','setSpdMsg'
		]),		
		getLogoPath(){
			if(this.user.corpLogo){
				return "/upload/" + this.user.corpLogo;
			}else{

				//return "../assets/tjlog.jpg"
			}
		},
		imgError(){
			this.imgNotError = false;
		},
		onSubmit() {
		},
		handleopen() {
		},
		handleclose() {
		},
		handleselect: function (a, b) {
		},
		cleanCache: function () {
			loginOut();
			window.location.reload();
		},
		//退出登录
		logout: function () {
			var _this = this;
			this.$confirm('确认退出吗?', '提示', {
			}).then(() => {
				_this.cleanCache();
			}).catch(() => {});
		},	
		userSetting() {
			this.$router.push("/user/usersetting");
		},
		testPage(){
			this.$router.push("/user/test");
		}
	},
	mounted() {	
				
	}	
}
</script>

<style lang="scss">
@import '~gycolor';

.layOutHeader {
	height: 40px;
	color: #898989;				
	background: url(../assets/images/top.jpg) no-repeat;
	-moz-background-size:100% 100%;  
    background-size:100% 100%;  
	background-position:200px;
	.logo {
		height: 40px;
		font-size: 20px;
		.sys-title{
			height: 100%;
			width: 100%;
			padding:5px;
			//background: linear-gradient(45deg, #fff, #dcf0ef) !important;
			//color: #fff;
			.logo-a{
				color:$--text-color;
				text-decoration:none;
				.logo-img{
					width: 30px;
					height: 30px;
				}	
				.sys-name{
					margin-left: 5px;
					font-size:24px;
					font-weight:500;
					font-family:'PingHeiText';
				}
			}			
		}							
		img {
			float: left;
		}
		.corpLogo{
				//width: 100%;
				height: 40px;
				margin-left: 200px;
		}					
	}
	.d-msg{
		padding-right: 10px;
		line-height:40px;
		float:right;					
			a{
				text-decoration:none;
				color:$--text-color;
			}						
	}
	.userinfo {
		height: 40px;
		.userinfo-inner {
			cursor: pointer;
			img {
				width: 22px;
				height:22px;
				border-radius: 22px;
				margin: 10px 0px 10px 10px;
				float: right;
			}						
		}
	}
	
	.div_nav{
			float: right;
	}
	.nav {
		float: right;
		height: 100%;
		line-height: 40px;
		background: transparent;
		padding: 0;
		margin: 0;
		.nav-item {
			margin: 0;
			float: left;
			list-style: none;
			position: relative;
			cursor: pointer; //margin-left: 20px;
			.a-router {
				text-decoration: none;
				//color: #9FA8B1;
				//color: #fff;
				display: block;
				padding: 0 10px;
				opacity: .9;
				font-size: 14px;
			}
			.a-router:hover{
					font-weight: 600;
			}
			.router-link-active {
				color:$--color-primary;
				font-weight: 700;
			}
			.router-link-active:before {
				content: "";
				display: block;
				position: absolute;
				bottom: 0;
				left: 0;
				width: 100%;
				height: 4px;
				background-color:$--color-primary;
			}
		}
	}
}
.el-dropdown-item,
.el-dropdown-menu__item {
	line-height: 30px;
}

.el-dropdown-menu__item--divided {
	margin-top: 0px;
}

.msg_color{ 
	color:#e6a23c;//$--color-primary;
}
.p-msg{
	padding:0px;
	.el-popover__title{
		padding:10px 0px 0px 10px;
	}
	.lst_notice{
		margin: 0px;
		max-height: 300px;
		overflow-y: auto;
		dt{
				border-top: 1px solid #ccc;
				padding: 15px 5px 15px 10px;
				cursor: pointer;
			.notice_title{
				font-size: 13px;
				font-weight: 400;
				color: #999;
			}
			.notice_date{
				font-size: 12px;
				float: right;
				margin: 0 0 0 5px;
				color:#ccc;
			}
		}
		dt:hover{
			background-color: rgb(240, 248, 255);
		}	
	}
	.msgInfo{
		margin: 0px;
		max-height: 300px;
		overflow-y: auto;
		dt{
				height:20px;			
    		padding: 10px 5px 2px 10px;
			.notice_title{
				-webkit-line-clamp: 1;  
				overflow: hidden;  
				display: -webkit-box;  
				-webkit-box-orient: vertical;  
				white-space: normal;  
				font-size: 13px;
				font-weight: 600;
				color: #737070;
			}			
		}
		dd.dd_content{
			margin: 0px;
    	padding: 10px 5px 10px 22px;
			border-top: 1px solid #ccc;
			.notice_content{
				font-size: 12px;
				font-weight: 400;
				color: #6f6b6b;
			}
			.notice_date{
				font-size: 12px;
				float: right;
				margin: 0 0 0 5px;
				color:#ccc;
			}
		}
		dd.dd_foot{
			margin: 0px;
    	padding: 5px 5px 5px 300px;
			border-top: 1px solid #ccc;			
		}
	}
	i.back{
		cursor: pointer;
	}

}
</style>
