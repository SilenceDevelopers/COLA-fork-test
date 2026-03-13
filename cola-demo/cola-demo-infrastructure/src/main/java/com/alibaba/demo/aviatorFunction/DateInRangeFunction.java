package com.alibaba.demo.aviatorFunction;

import com.googlecode.aviator.runtime.function.AbstractFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorBoolean;
import com.googlecode.aviator.runtime.type.AviatorObject;
import org.springframework.stereotype.Component;

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
}
