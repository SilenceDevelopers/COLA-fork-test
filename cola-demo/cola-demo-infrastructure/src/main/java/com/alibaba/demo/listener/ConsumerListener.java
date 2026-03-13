package com.alibaba.demo.listener;

import com.googlecode.aviator.AviatorEvaluator;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
//@RocketMQMessageListener(consumerGroup = "consumer_test", topic = "consumer_topic")
public class ConsumerListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("消费者收到消息：" + new String(message.getBody()));
    }

    public static void main(String[] args) {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("算术表达式【1+1】： " + AviatorEvaluator.execute("1+1"));
        System.out.println("逻辑表达式【1==1】： " + AviatorEvaluator.execute("1==1"));
        System.out.println("三元表达式【1==1 ? '对' : '错'】： " + AviatorEvaluator.execute("1==1 ? '对' : '错'"));

        System.out.println("正则表达式： " +  AviatorEvaluator.execute("'killme@2008gmail.com'=~/([\\w0-8]+@\\w+[\\.\\w+]+)/ ? $1:'unknow'"));
        System.out.println("-----------------------------------------------------------------");

        System.out.println("函数调用【6的3次方】： " + AviatorEvaluator.execute("math.pow(6,3)"));
        System.out.println("求字符串长度： " +       AviatorEvaluator.execute("string.length('hello')"));
        System.out.println("判断字符串是否包含字符串： " +     AviatorEvaluator.execute("string.contains('hello','h')"));
        System.out.println("是否以子串开头： " +     AviatorEvaluator.execute("math.pow(-3,2)"));
        System.out.println("求n次方： " +        AviatorEvaluator.execute("math.sqrt(14.0)"));
        System.out.println("正弦函数： " +      AviatorEvaluator.execute("math.sin(20)"));

        System.out.println("-----------------------------------------------------------------");


        Map env = new HashMap(16);
        env.put("yourname","aviator");
        String result3 = (String)AviatorEvaluator.execute(" 'hello ' + yourname ", env);
        System.out.println("变量和字符串相加:"+ result3);

        System.out.println("-----------------------------------------------------------------");
    }
}
