package com.alibaba.demo.web;

import com.alibaba.demo.gateway.impl.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/seckill")
public class SeckillController {

    @Autowired
    private SeckillService seckillService;

    /**
     * 获取秒杀令牌接口
     */
    @PostMapping("/token")
    public String getToken(@RequestParam Long userId, @RequestParam Long productId) {
        return seckillService.tryGetSeckillToken(userId, productId);
    }

    /**
     * 秒杀接口
     */
    @PostMapping("/do")
    public String doSeckill(@RequestParam String token) {
        boolean success = seckillService.doSeckill(token);
        if (success) {
            return "秒杀成功";
        } else {
            return "秒杀失败，令牌无效或已使用";
        }
    }
}
