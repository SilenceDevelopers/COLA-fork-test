package com.alibaba.demo.aviatorFunction;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Component
public class DateInRangeFunction extends AbstractFunction {

    @Override
    public String getName() {
        return "dateInRange";
    }

    @Override
    public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2, AviatorObject arg3) {
        // 参数：当前时间、开始时间、结束时间
        long now = FunctionUtils.getNumberValue(arg1, env).longValue();
        long start = FunctionUtils.getNumberValue(arg2, env).longValue();
        long end = FunctionUtils.getNumberValue(arg3, env).longValue();
        return AviatorBoolean.valueOf(now >= start && now <= end);
    }

    public static void main(String[] args) {
        // 注册自定义函数
        AviatorEvaluator.addFunction(new MultiplyFunction());
        // 方式1
        System.out.println(AviatorEvaluator.execute("multiply(12.23, -2.3)"));
        // 方式2
        Map<String, Object> params = new HashMap<>();
        params.put("a", 12.23);
        params.put("b", -2.3);
        System.out.println(AviatorEvaluator.execute("multiply(a, b)", params));
    }

    static class MultiplyFunction extends AbstractFunction {
        @Override
        public AviatorObject call(Map<String, Object> env, AviatorObject arg1, AviatorObject arg2) {

            double num1 = FunctionUtils.getNumberValue(arg1, env).doubleValue();
            double num2 = FunctionUtils.getNumberValue(arg2, env).doubleValue();
            BigDecimal value = new BigDecimal(num1).multiply(new BigDecimal(num2)).setScale(2, RoundingMode.HALF_UP);
            return AviatorDecimal.valueOf(value);
        }

        @Override
        public String getName() {
            return "multiply";
        }

    }
}
