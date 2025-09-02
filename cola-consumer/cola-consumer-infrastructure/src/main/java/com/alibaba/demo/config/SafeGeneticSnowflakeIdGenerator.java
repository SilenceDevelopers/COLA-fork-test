package com.alibaba.demo.config;

/**
 * 使用散列函数的安全基因法
 */
public class SafeGeneticSnowflakeIdGenerator extends GeneticSnowflakeIdGenerator {

    public SafeGeneticSnowflakeIdGenerator(long workerId) {
        super(workerId);
    }

    /**
     * 重写nextId方法，使用散列函数计算基因码
     * @param geneSource 基因来源（如userId）
     * @return 64位长整型ID
     */
    @Override
    public synchronized long nextId(long geneSource) {
        // 使用散列函数计算基因码，确保均匀分布
        long safeGene = hashGene(geneSource);
        return super.nextId(safeGene); // 调用父类方法，但传入的是散列后的安全基因码
    }

    /**
     * 使用散列函数计算基因码，避免数据倾斜
     * @param sourceId 原始ID（如userId）
     * @return 均匀分布的基因码
     */
    public static long hashGene(long sourceId) {
        // 选择一种高效且分布均匀的散列函数
        // 这里以64位MurmurHash变种为例（实际使用时可能需要引入相关库或自己实现）

        // 简易但有效的替代方案：使用CRC32
        java.util.zip.CRC32 crc32 = new java.util.zip.CRC32();
        // 将long转换为byte数组进行散列
        byte[] bytes = longToBytes(sourceId);
        crc32.update(bytes);
        long hashValue = crc32.getValue();

        // 取散列值的低8位作为基因码
        return hashValue & 0xFF;
    }

    private static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte)(l & 0xFF);
            l >>= 8;
        }
        return result;
    }
}
