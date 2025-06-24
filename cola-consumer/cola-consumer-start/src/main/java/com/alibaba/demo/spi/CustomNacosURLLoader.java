package com.alibaba.demo.spi;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.apache.shardingsphere.infra.url.spi.ShardingSphereURLLoader;

import java.util.Properties;

public class CustomNacosURLLoader implements ShardingSphereURLLoader {

    /**
     * 定义jdbc:shardingsphere:后的类型为nacos:
     */
    private static final String NACOS_TYPE = "nacos:";
    private String str = "org.apache.shardingsphere.infra.url.spi.ShardingSphereURLLoader";

    /**
     * 接收nacos:后的参数sharding.yaml?serverAddr=${nacos.service-address}&namespace=${nacos.namespace}&group=${nacos.group}&username=${nacos.username}&password=${nacos.password}
     *
     * @param configurationSubject configuration dataId
     * @param queryProps           url参数，已经解析成为Properties
     * @return
     */
    @Override
    @SneakyThrows
    public String load(String configurationSubject, Properties queryProps) {
        ConfigService configService = NacosFactory.createConfigService(queryProps);
        String dataId = configurationSubject;
        if (dataId.contains(".")){
            dataId = dataId.substring(0,dataId.indexOf("."));
        }
        //获取nacos配置
        String config = configService.getConfig(dataId, queryProps.getProperty(Constants.GROUP, Constants.DEFAULT_GROUP), 500);
        Preconditions.checkArgument(config != null, "Nacos config [" + dataId + "] is Empty.");
        return config;
    }

    @Override
    public Object getType() {
        return NACOS_TYPE;
    }
}
