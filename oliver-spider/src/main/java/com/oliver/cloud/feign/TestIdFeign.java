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
@FeignClient(name = "Leaf-ID")
public interface TestIdFeign {
        /**
         * @Author: Oliver
         * @Desc: desc
         * @Date: cretead in 2020/5/12 10:27
         * @Last Modified: by
         * @return value
         */
        @RequestMapping(value = "/getLeafId", method = RequestMethod.GET)
        String getId();
}
