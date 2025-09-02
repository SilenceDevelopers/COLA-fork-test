package com.alibaba.demo.config;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 分库分表路由管理器
 */
@Component
@Slf4j
@Getter
public class ShardingRouter {
    // 分库数量
    private int databaseCount;

    // 分表数量
    private int tableCount;

    // 总分片数
    private int totalShards;

    private final SafeGeneticSnowflakeIdGenerator idGenerator;

    @Autowired
    public ShardingRouter(ShardingConfigProperties configProperties, SafeGeneticSnowflakeIdGenerator idGenerator) {
        this.databaseCount = configProperties.getDatabaseCount();
        this.tableCount = configProperties.getTableCount();
        this.totalShards = databaseCount * tableCount;
        this.idGenerator = idGenerator;
        log.info("ShardingRouter initialized with {} databases and {} tables per database ({} total shards)", databaseCount, tableCount, totalShards);
    }

    /**
     * 动态更新分片配置（可用于运行时调整）
     */
    public void updateShardingConfig(int newDatabaseCount, int newTableCount) {
        this.databaseCount = newDatabaseCount;
        this.tableCount = newTableCount;
        this.totalShards = newDatabaseCount * newTableCount;
        log.info("Sharding configuration updated to {} databases and {} tables per database ({} total shards)", databaseCount, tableCount, totalShards);
    }

    /**
     * 根据基因码计算分片信息
     *
     * @param gene 基因码
     * @return 分片信息 [数据库索引, 表索引]
     */
    public int[] calculateShard(long gene) {
        // 计算全局分片编号
        int shardNum = (int) (gene % totalShards);

        // 计算数据库和表索引
        int dbIndex = shardNum / tableCount;
        int tableIndex = shardNum % tableCount;

        return new int[]{dbIndex, tableIndex};
    }

    /**
     * 根据订单ID计算分片信息
     *
     * @param orderId     订单ID
     * @param idGenerator ID生成器
     * @return 分片信息 [数据库索引, 表索引]
     */
    public int[] calculateShardByOrderId(long orderId, GeneticSnowflakeIdGenerator idGenerator) {
        long gene = idGenerator.extractGene(orderId);
        return calculateShard(gene);
    }

    /**
     * 根据用户ID计算分片信息
     *
     * @param userId 用户ID
     * @return 分片信息 [数据库索引, 表索引]
     */
    public int[] calculateShardByUserId(long userId, SafeGeneticSnowflakeIdGenerator idGenerator) {
        // 使用与ID生成器相同的散列算法计算基因码
        long gene = idGenerator.hashGene(userId);
        return calculateShard(gene);
    }

    /**
     * 生成实际的数据源名称和表名
     *
     * @param shardInfo      分片信息
     * @param logicTableName 逻辑表名
     * @return 物理表名 "dbX.tableY"
     */
    public String getPhysicalTableName(int[] shardInfo, String logicTableName) {
        return String.format("db%d.%s_%d",
                shardInfo[0], logicTableName, shardInfo[1]);
    }
}
