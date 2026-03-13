package com.alibaba.demo.web;

import com.alibaba.cola.dto.MultiResponse;
import com.alibaba.cola.dto.Response;
import com.alibaba.demo.api.CustomerServiceI;
import com.alibaba.demo.dto.CustomerAddCmd;
import com.alibaba.demo.dto.CustomerListByNameQry;
import com.alibaba.demo.dto.data.CustomerDTO;
import com.google.common.collect.Maps;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CustomerController {

    @Autowired
    private CustomerServiceI customerService;

    @Autowired
    private AviatorEvaluatorInstance aviatorEvaluator;

    @GetMapping(value = "/helloworld")
    public String helloWorld() {
        return "Hello, welcome to COLA world!";
    }

    @GetMapping(value = "/customer")
    public MultiResponse<CustomerDTO> listCustomerByName(@RequestParam(required = false) String name) {
        CustomerListByNameQry customerListByNameQry = new CustomerListByNameQry();
        customerListByNameQry.setName(name);
        return customerService.listByName(customerListByNameQry);
    }

    @PostMapping(value = "/customer")
    public Response addCustomer(@RequestBody CustomerAddCmd customerAddCmd) {
        return customerService.addCustomer(customerAddCmd);
    }

    @PostMapping(value = "/aviatorTest")
    public String aviatorTest() {
        String expression = "dateInRange(currentTime, startTime, endTime)";
        long current = System.currentTimeMillis();
        long startTime = System.currentTimeMillis() - 11235345;
        long endTime = System.currentTimeMillis() + 324335;
        Map<String, Object> map = Maps.newHashMap();
        map.put("currentTime", current);
        map.put("startTime", startTime);
        map.put("endTime", endTime);
        Expression exp = aviatorEvaluator.compile(expression);
        return exp.execute(map).toString();
    }
}
