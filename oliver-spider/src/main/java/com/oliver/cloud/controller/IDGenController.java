package com.oliver.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.oliver.cloud.entity.OperationResult;
import com.oliver.cloud.feign.TestIdFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

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
	 * @Desc: 获取Leaf-Id
	 * @Date: cretead in 2019/10/18 10:18
	 * @Last Modified: by
	 * @return value
	 */
	@RequestMapping(value = "/getLeafId", method = RequestMethod.GET)
	public String getLeafId(){
		String result = testIdFeign.getId();
		HashMap<String,String> jsonMap = JSON.parseObject(result,HashMap.class);
		return result;
	}

//	给你一个整数数组 nums ，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），并返回该子数组所对应的乘积。
//
// 
//
//示例 1:
//
//输入: [2,3,-2,4]
//输出: 6
//解释: 子数组 [2,3] 有最大乘积 6。
//示例 2:
//
//输入: [-2,0,-1]
//输出: 0
//解释: 结果不能为 2, 因为 [-2,-1] 不是子数组。
//总产量/拼版所有的拼版 = 一个拼版就有一个产品出来
//
	public static void main(String[] args){

	}
}
