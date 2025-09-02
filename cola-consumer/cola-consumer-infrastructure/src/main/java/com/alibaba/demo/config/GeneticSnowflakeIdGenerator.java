package com.alibaba.demo.config;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 基于基因法的雪花ID生成器
 * 结构：0 | 41位时间戳 | 6位工作节点ID | 8位基因码 | 8位序列号
 */
public class GeneticSnowflakeIdGenerator {
    // 起始时间戳（2020-01-01 00:00:00）
    private final long epoch = 1577808000000L;

    // 各部分位数
    private final long workerIdBits = 6L;
    private final long geneBits = 8L;
    private final long sequenceBits = 8L;

    // 最大值
    private final long maxWorkerId = ~(-1L << workerIdBits);
    private final long maxGene = ~(-1L << geneBits);
    private final long maxSequence = ~(-1L << sequenceBits);

    // 移位偏移量
    private final long workerIdShift = sequenceBits + geneBits;
    private final long timestampShift = sequenceBits + geneBits + workerIdBits;
    private final long geneShift = sequenceBits;

    // 工作节点ID
    private final long workerId;

    // 序列号
    private AtomicLong sequence = new AtomicLong(0L);

    // 上次生成ID的时间戳
    private long lastTimestamp = -1L;

    public GeneticSnowflakeIdGenerator(long workerId) {
        if (workerId < 0 || workerId > maxWorkerId) {
            throw new IllegalArgumentException(
                    String.format("Worker ID must be between 0 and %d", maxWorkerId));
        }
        this.workerId = workerId;
    }

    /**
     * 生成带有基因的ID
     * @param geneSource 基因来源（如userId）
     * @return 64位长整型ID
     */
    public synchronized long nextId(long geneSource) {
        long timestamp = timeGen();

        // 计算基因码
        long gene = geneSource & maxGene;

        // 处理时钟回拨
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards. Refusing to generate id for %d milliseconds",
                            lastTimestamp - timestamp));
        }

        // 同一毫秒内生成ID
        if (lastTimestamp == timestamp) {
            sequence.set((sequence.get() + 1) & maxSequence);
            if (sequence.get() == 0) {
                // 序列号用完，等待下一毫秒
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 新毫秒重置序列号
            sequence.set(0L);
        }

        lastTimestamp = timestamp;

        // 组合各部分生成ID
        return ((timestamp - epoch) << timestampShift)
                | (workerId << workerIdShift)
                | (gene << geneShift)
                | sequence.get();
    }

    /**
     * 从ID中提取基因码
     * @param id 生成的ID
     * @return 基因码
     */
    public long extractGene(long id) {
        return (id >> geneShift) & maxGene;
    }

    /**
     * 从ID中提取时间戳
     * @param id 生成的ID
     * @return 时间戳
     */
    public long extractTimestamp(long id) {
        return ((id >> timestampShift) & ~(-1L << 41L)) + epoch;
    }

    /**
     * 从ID中提取工作节点ID
     * @param id 生成的ID
     * @return 工作节点ID
     */
    public long extractWorkerId(long id) {
        return (id >> workerIdShift) & maxWorkerId;
    }

    /**
     * 等待下一毫秒
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前时间戳
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}
