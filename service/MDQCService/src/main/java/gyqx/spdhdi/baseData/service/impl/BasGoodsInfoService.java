package gyqx.spdhdi.baseData.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import gyqx.spdhdi.baseData.dao.BasGoodsInfoDao;
import gyqx.spdhdi.baseData.service.IBasGoodsInfoService;
import gyqx.spdhdi.baseData.vo.BasGoodsInfoVo;
import gyqx.spdhdi.mycerts.vo.JxsqsGoodsVo;
@Service
public class BasGoodsInfoService implements IBasGoodsInfoService {
	@Resource
	private BasGoodsInfoDao basGoodsDao;
	
	@Override
	public QueryResult<BasGoodsInfoVo> listBasGoods(QueryInfo<JxsqsGoodsVo> queryInfo) throws Exception {
		 QueryResult<BasGoodsInfoVo> re = new  QueryResult<BasGoodsInfoVo>();
			PageHelper.startPage(queryInfo.getPage(), queryInfo.getRows());
			List<BasGoodsInfoVo> dataList = basGoodsDao.getBasGoodsInfoList(queryInfo.getQueryObject());
			PageInfo<BasGoodsInfoVo> sysConfigPage = new PageInfo<BasGoodsInfoVo>(dataList);
			re.setData(sysConfigPage.getList());
			re.setPageNum(sysConfigPage.getPageNum());
			re.setPageSize(sysConfigPage.getPageSize());
			re.setTotal(sysConfigPage.getTotal());
		   	return re; 
	}
}
