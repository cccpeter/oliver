package com.oliver.cloud.feign;

import com.oliver.cloud.entity.OperationResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author: cpeter
 * @Desc: 测试接入feign
 * @Date: cretead in 2019/10/18 10:10
 * @Last Modified: by
 * @return value
 */
@FeignClient(name = "leaf-Id-Get",path="/getLeafId")
public interface TestIdFeign {
//    @FeignClient("HelloServer")
//    interface HelloClient {
        @RequestMapping(value = "/getLeafId", method = RequestMethod.GET)
        OperationResult getId();
//    }
}
//@FeignClient(name = "test-service", path = "/test")
//public interface TestService {
//    @RequestMapping(value = "/echo", method = RequestMethod.GET)
//    TestModel echo(@RequestParam("parameter") String parameter);
//}
