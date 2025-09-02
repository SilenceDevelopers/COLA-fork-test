package com.alibaba.demo.config;

import cn.hutool.core.lang.generator.SnowflakeGenerator;

public class GeneticShardingExample {
    public static void main(String[] args) {
        // 1. 改用安全的ID生成器（工作节点ID为1）
        SafeGeneticSnowflakeIdGenerator idGenerator = new SafeGeneticSnowflakeIdGenerator(1);

        // 初始化分片路由器（16个库，每个库16张表）
        ShardingRouter router = new ShardingRouter(null, null);

        // 模拟用户ID - 使用雪花算法生成，模拟真实场景
        long[] userIds = generateSnowflakeUserIds(1000); // 生成1000个模拟用户ID

        // 2. 使用散列函数计算用户基因码，确保与ID生成器使用相同的算法
        System.out.println("===== 安全基因法测试 =====");

        // 测试单个用户
        long userId = userIds[0];
        System.out.println("测试用户ID: " + userId);

        // 为用户生成订单ID
        long orderId = idGenerator.nextId(userId);
        System.out.println("生成的订单ID: " + orderId);

        // 从订单ID中提取基因码
        long extractedGene = idGenerator.extractGene(orderId);
        System.out.println("从订单ID提取的基因码: " + extractedGene);

        // 3. 使用相同的散列函数计算用户基因码
        long userGene = SafeGeneticSnowflakeIdGenerator.hashGene(userId);
        System.out.println("用户基因码(散列后): " + userGene);

        // 验证基因码是否匹配
        System.out.println("基因码匹配: " + (extractedGene == userGene));

        // 通过订单ID计算分片
        int[] shardByOrderId = router.calculateShardByOrderId(orderId, idGenerator);
        String physicalTableByOrder = router.getPhysicalTableName(shardByOrderId, "orders");
        System.out.println("订单ID路由到: " + physicalTableByOrder);

        // 4. 修改路由器的calculateShardByUserId方法，使用相同的散列函数
        int[] shardByUserId = router.calculateShardByUserId(userId, idGenerator);
        String physicalTableByUser = router.getPhysicalTableName(shardByUserId, "orders");
        System.out.println("用户ID路由到: " + physicalTableByUser);

        // 验证路由一致性
        System.out.println("路由一致性: " +
                (shardByOrderId[0] == shardByUserId[0] &&
                        shardByOrderId[1] == shardByUserId[1]));

        // 5. 测试数据分布均匀性
        System.out.println("\n===== 测试数据分布均匀性 =====");
        testDataDistribution(userIds, idGenerator, router);
    }

    /**
     * 生成模拟的雪花算法用户ID
     */
    private static long[] generateSnowflakeUserIds(int count) {
        SnowflakeGenerator snowflakeGenerator = new SnowflakeGenerator();
        long[] userIds = new long[count];
        // 简单模拟 - 实际中这些ID应该来自用户服务
        for (int i = 0; i < count; i++) {
            // 使用时间戳+随机数模拟雪花ID
            userIds[i] = snowflakeGenerator.next();
        }
        return userIds;
    }

    /**
     * 测试数据分布是否均匀
     */
    private static void testDataDistribution(long[] userIds,
                                             SafeGeneticSnowflakeIdGenerator idGenerator,
                                             ShardingRouter router) {
        // 创建一个数组来统计每个分片的数据量
        int[] shardCounts = new int[256]; // 16*16=256个分片

        // 为每个用户生成一个订单，并统计分片分布
        for (long userId : userIds) {
            long orderId = idGenerator.nextId(userId);
            int[] shardInfo = router.calculateShardByOrderId(orderId, idGenerator);

            // 计算全局分片编号
            int globalShardId = shardInfo[0] * 16 + shardInfo[1];
            shardCounts[globalShardId]++;
        }

        // 计算统计信息
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int sum = 0;

        for (int count : shardCounts) {
            if (count < min) min = count;
            if (count > max) max = count;
            sum += count;
        }

        double avg = (double) sum / shardCounts.length;

        // 计算标准差
        double variance = 0;
        for (int count : shardCounts) {
            variance += Math.pow(count - avg, 2);
        }
        double stdDev = Math.sqrt(variance / shardCounts.length);

        System.out.println("总订单数: " + sum);
        System.out.println("分片数量: " + shardCounts.length);
        System.out.println("最小分片订单数: " + min);
        System.out.println("最大分片订单数: " + max);
        System.out.println("平均分片订单数: " + String.format("%.2f", avg));
        System.out.println("标准差: " + String.format("%.2f", stdDev));
        System.out.println("最大/最小比例: " + String.format("%.2f", (double)max/min));

        // 输出分布情况（可选）
        System.out.println("\n前20个分片的订单分布:");
        for (int i = 0; i < 20; i++) {
            System.out.println("分片" + i + ": " + shardCounts[i] + "个订单");
        }
    }
}
