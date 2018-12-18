package gyqx.mdqc.mycerts.controller;


import javax.annotation.Resource;
import javax.validation.Valid;

import common.db.query.QueryInfo;
import common.db.query.QueryResult;
import common.web.AjaxResult;
import common.web.BaseController;
import gyqx.mdqc.mycerts.service.IProvRegistrationService;
import gyqx.mdqc.mycerts.vo.BasRegistrationInfoVo;
import gyqx.mdqc.mycerts.vo.IdNameVo;
import gyqx.mdqc.mycerts.vo.ProvBasGoodsInfoVo;
import gyqx.mdqc.mycerts.vo.ProvRegistrationVo;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping(value = "prov/provRegistration")
public class ProvRegistrationController extends BaseController
{
	@Resource
	private IProvRegistrationService provRegistrationService;

	@RequestMapping(value = "listByPage")
	@ResponseBody
	public AjaxResult listByPage(@RequestBody @Valid QueryInfo<ProvRegistrationVo> queryInfo , Errors error  )  throws  Exception
	{
		AjaxResult result = new AjaxResult();
		filterErrors(error);
		QueryResult<ProvRegistrationVo> ret = provRegistrationService.listProvCertByPage(queryInfo);
		result.setData(ret);
		return result;
	}

	@GetMapping("/getProvCertById/{id}/{mfrsId}")
	public AjaxResult getProvCertById(@PathVariable("id") String id,
									  @PathVariable("mfrsId") String mfrsId) throws Exception {
		AjaxResult<ProvRegistrationVo> result = new AjaxResult<>();
		List<ProvRegistrationVo> list = provRegistrationService.getProvCertById(id,mfrsId);
		if(list!=null&&list.size()>0){
			result.setData(list.get(0));
		}else {
			throw new Exception("此id对应的注册证信息不存在！");
		}
		return result;
	}

	@GetMapping("/getDict/{dictName}")
	public AjaxResult getDict(@PathVariable("dictName") String dictName) throws Exception {
		AjaxResult<List<IdNameVo>> result = new AjaxResult<>();
		result.setData(provRegistrationService.listDictValues(dictName));
		return result;
	}

	@GetMapping("/getMfrs/{provId}")
	public AjaxResult getMfrs(@PathVariable("provId") String provId) throws Exception {
		AjaxResult<List<IdNameVo>> result = new AjaxResult<>();
		result.setData(provRegistrationService.listMfrsForProId(provId));
		return result;
	}
	@GetMapping("/getMfrsProduct/{mfrsId}")
	public AjaxResult getMfrsProduct(@PathVariable("mfrsId") String mfrsId,
									 @RequestParam(value = "input", required = false) String input) throws Exception {
		AjaxResult<List<ProvBasGoodsInfoVo>> result = new AjaxResult<>();
		result.setData(provRegistrationService.getMfrsProduct(mfrsId, input));
		return result;
	}

	@PostMapping("/provModCert")
	public AjaxResult modyCert(@RequestBody ProvRegistrationVo provRegistrationVo) throws Exception {
		AjaxResult<Boolean> result = new AjaxResult<>();
		result.setData(provRegistrationService.modyCert(provRegistrationVo));
		return result;
	}

	@DeleteMapping("/delReg/{id}/{version}")
	public AjaxResult deleteProvCert(@PathVariable("id") String id,
								 @PathVariable("version") Integer version) throws Exception {
		AjaxResult<Integer> result = new AjaxResult<>();
		result.setData(provRegistrationService.deleteProvCert(id, version));
		return result;
	}

	@PostMapping("/provReplaceCert")
	public AjaxResult provReplaceCert(@RequestBody ProvRegistrationVo provRegistrationVo) throws SQLException {
		AjaxResult<Boolean> result = new AjaxResult<>();
		result.setData(provRegistrationService.provReplaceCert(provRegistrationVo));
		return result;
	}

	@GetMapping("/getBasResg/{mfrsId}/{certificateCode}")
	public AjaxResult  getBasCert(@PathVariable("mfrsId") String mfrsId,
								  @PathVariable("certificateCode") String certificateCode) throws Exception{
		AjaxResult<ProvRegistrationVo> result = new AjaxResult<>();
		List<ProvRegistrationVo> list = provRegistrationService.getBasCert(mfrsId,certificateCode);
		if(list!=null&&list.size()>0){
			result.setData(list.get(0));
		}else {
			throw new Exception("未查询到该注册证信息！");
		}
		return result;
	}

	@PostMapping("/provAddCert")
	public AjaxResult addProvCert(@RequestBody ProvRegistrationVo provRegistrationVo) throws SQLException {
		AjaxResult<Boolean> result = new AjaxResult<>();
		result.setData(provRegistrationService.addProvCert(provRegistrationVo));
		return result;
	}
}