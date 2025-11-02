import java.util.BitSet;
import java.util.Random;

/**
 * 布隆过滤器实现（Java版本）
 * 
 * 题目来源：大数据处理、缓存系统、网络爬虫去重
 * 应用场景：网页去重、垃圾邮件过滤、缓存穿透防护
 * 
 * 核心思想：
 * 1. 使用多个哈希函数将元素映射到位数组的不同位置
 * 2. 插入元素时，将所有对应位置设为1
 * 3. 查询元素时，检查所有对应位置是否都为1
 * 4. 存在一定的误判率（假阳性），但不会漏判（假阴性）
 * 
 * 时间复杂度：
 * - 插入：O(k) k为哈希函数数量
 * - 查询：O(k)
 * 
 * 空间复杂度：O(m) m为位数组大小
 * 
 * 工程化考量：
 * 1. 误判率控制：通过调整位数组大小和哈希函数数量
 * 2. 哈希函数选择：独立且分布均匀的哈希函数
 * 3. 内存优化：使用位数组节省空间
 * 4. 并发安全：多线程环境下的线程安全
 */
public class Code15_BloomFilter {
    
    // 位数组
    private BitSet bitSet;
    
    // 位数组大小
    private int size;
    
    // 哈希函数数量
    private int hashCount;
    
    // 随机种子，用于生成不同的哈希函数
    private int[] seeds;
    
    /**
     * 构造函数
     * 
     * @param expectedInsertions 预期插入元素数量
     * @param falsePositiveRate 期望的误判率
     */
    public Code15_BloomFilter(int expectedInsertions, double falsePositiveRate) {
        if (expectedInsertions <= 0) {
            throw new IllegalArgumentException("预期插入数量必须大于0");
        }
        if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
            throw new IllegalArgumentException("误判率必须在0和1之间");
        }
        
        // 计算最优的位数组大小和哈希函数数量
        this.size = optimalBitArraySize(expectedInsertions, falsePositiveRate);
        this.hashCount = optimalHashFunctionCount(expectedInsertions, size);
        
        this.bitSet = new BitSet(size);
        this.seeds = generateSeeds(hashCount);
        
        System.out.println("布隆过滤器初始化：");
        System.out.println("预期插入数量：" + expectedInsertions);
        System.out.println("期望误判率：" + falsePositiveRate);
        System.out.println("位数组大小：" + size);
        System.out.println("哈希函数数量：" + hashCount);
    }
    
    /**
     * 计算最优的位数组大小
     * 公式：m = - (n * ln(p)) / (ln(2))^2
     */
    private int optimalBitArraySize(int n, double p) {
        return (int) Math.ceil(-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }
    
    /**
     * 计算最优的哈希函数数量
     * 公式：k = (m/n) * ln(2)
     */
    private int optimalHashFunctionCount(int n, int m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }
    
    /**
     * 生成随机种子
     */
    private int[] generateSeeds(int count) {
        Random random = new Random(42); // 固定种子保证可重复性
        int[] seeds = new int[count];
        for (int i = 0; i < count; i++) {
            seeds[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return seeds;
    }
    
    /**
     * 哈希函数：使用MurmurHash变种
     */
    private int hash(String element, int seed) {
        int hash = seed;
        for (int i = 0; i < element.length(); i++) {
            hash = hash * 31 + element.charAt(i);
            hash ^= hash >>> 16;
        }
        return Math.abs(hash % size);
    }
    
    /**
     * 添加元素
     */
    public void add(String element) {
        for (int i = 0; i < hashCount; i++) {
            int position = hash(element, seeds[i]);
            bitSet.set(position);
        }
    }
    
    /**
     * 检查元素是否存在
     * 
     * @return true: 可能存在（可能有误判）
     *         false: 一定不存在
     */
    public boolean mightContain(String element) {
        for (int i = 0; i < hashCount; i++) {
            int position = hash(element, seeds[i]);
            if (!bitSet.get(position)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取位数组使用率
     */
    public double getUsageRate() {
        int usedBits = bitSet.cardinality();
        return (double) usedBits / size;
    }
    
    /**
     * 清空布隆过滤器
     */
    public void clear() {
        bitSet.clear();
    }
    
    /**
     * 获取布隆过滤器统计信息
     */
    public void printStats() {
        System.out.println("布隆过滤器统计信息：");
        System.out.println("位数组大小：" + size);
        System.out.println("哈希函数数量：" + hashCount);
        System.out.println("已使用位数：" + bitSet.cardinality());
        System.out.println("使用率：" + String.format("%.2f%%", getUsageRate() * 100));
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建布隆过滤器：预期插入10000个元素，误判率0.01
        Code15_BloomFilter bloomFilter = new Code15_BloomFilter(10000, 0.01);
        
        // 测试数据
        String[] testData = {
            "https://example.com/page1",
            "https://example.com/page2", 
            "https://example.com/page3",
            "https://example.com/page4",
            "https://example.com/page5"
        };
        
        // 添加测试数据
        System.out.println("\n=== 添加测试数据 ===");
        for (String url : testData) {
            bloomFilter.add(url);
            System.out.println("添加：" + url);
        }
        
        // 测试存在性检查
        System.out.println("\n=== 存在性检查 ===");
        for (String url : testData) {
            boolean exists = bloomFilter.mightContain(url);
            System.out.println("检查 " + url + " : " + (exists ? "可能存在" : "不存在"));
        }
        
        // 测试不存在的数据
        System.out.println("\n=== 测试不存在的数据 ===");
        String[] nonExistentData = {
            "https://example.com/page999",
            "https://google.com/search",
            "https://github.com/project"
        };
        
        for (String url : nonExistentData) {
            boolean exists = bloomFilter.mightContain(url);
            System.out.println("检查 " + url + " : " + (exists ? "可能存在（误判）" : "不存在"));
        }
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 100000; i++) {
            bloomFilter.mightContain("test" + i);
        }
        
        long endTime = System.currentTimeMillis();
        System.out.println("100000次查询耗时：" + (endTime - startTime) + "ms");
        
        // 打印统计信息
        bloomFilter.printStats();
        
        // 误判率测试
        System.out.println("\n=== 误判率测试 ===");
        int falsePositives = 0;
        int testCount = 10000;
        
        for (int i = 0; i < testCount; i++) {
            String testStr = "new_data_" + i;
            if (bloomFilter.mightContain(testStr)) {
                falsePositives++;
            }
        }
        
        double actualFalsePositiveRate = (double) falsePositives / testCount;
        System.out.println("测试数量：" + testCount);
        System.out.println("误判数量：" + falsePositives);
        System.out.println("实际误判率：" + String.format("%.4f", actualFalsePositiveRate));
    }
}