package com.oliver.cloud.controller;

import com.oliver.cloud.entity.OperationResult;
import com.oliver.cloud.feign.TestIdFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
/**
 * @Author: cpeter
 * @Desc: ID生成控制器类
 * @Date: cretead in 2019/10/17 18:07
 * @Last Modified: by
 * @return value
 */
public class IDGenController {

	@Autowired
	private TestIdFeign testIdFeign;

	/**
	 * @Author: cpeter
	 * @Desc: 获取本地Leaf-Id
	 * @Date: cretead in 2019/10/18 10:18
	 * @Last Modified: by
	 * @return value
	 */
	@RequestMapping(value = "/getLeafId", method = RequestMethod.GET)
	public OperationResult getLeafId(){
		return OperationResult.buildConfirmResult("success");
	}

	/**
	 * @Author: cpeter
	 * @Desc: 获取远程的leaf-id
	 * @return
	 */
	@RequestMapping(value = "/getFeign")
	public OperationResult getFeign(){
		System.out.println(testIdFeign.getId());
		return OperationResult.buildSuccessResult();
	}
}
