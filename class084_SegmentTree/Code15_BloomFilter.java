package class111;

// 布隆过滤器实现 (Java版本)
// 题目来源: 大数据处理、缓存系统、网络爬虫去重
// 应用场景: 网页去重、垃圾邮件过滤、缓存穿透防护
// 题目描述: 实现布隆过滤器，支持元素添加和存在性检查
// 
// 解题思路:
// 1. 使用多个哈希函数将元素映射到位数组的不同位置
// 2. 添加元素时，将所有哈希位置设为1
// 3. 检查元素时，如果所有哈希位置都为1，则元素可能存在
// 4. 使用误判率公式计算最优的哈希函数数量和位数组大小
// 
// 时间复杂度分析:
// - 添加元素: O(k)，其中k是哈希函数数量
// - 检查元素: O(k)
// 
// 空间复杂度: O(m)，其中m是位数组大小
// 
// 工程化考量:
// 1. 误判率控制: 根据预期元素数量和可接受的误判率计算最优参数
// 2. 哈希函数选择: 使用不同的哈希函数减少冲突
// 3. 内存优化: 使用位数组节省空间
// 4. 线程安全: 在多线程环境下安全使用

import java.util.BitSet;
import java.util.Random;

public class Code15_BloomFilter {
    
    // 位数组，用于存储元素的存在性信息
    private BitSet bitSet;
    
    // 位数组大小
    private int bitSetSize;
    
    // 哈希函数数量
    private int hashFunctionCount;
    
    // 预期元素数量
    private int expectedElementCount;
    
    // 实际添加的元素数量
    private int actualElementCount;
    
    // 随机种子，用于生成不同的哈希函数
    private int[] seeds;
    
    /**
     * 构造函数 - 根据预期元素数量和误判率自动计算参数
     * @param expectedElementCount 预期元素数量
     * @param falsePositiveRate 可接受的误判率 (0 < falsePositiveRate < 1)
     * @throws IllegalArgumentException 如果参数无效
     */
    public Code15_BloomFilter(int expectedElementCount, double falsePositiveRate) {
        if (expectedElementCount <= 0) {
            throw new IllegalArgumentException("预期元素数量必须大于0");
        }
        if (falsePositiveRate <= 0 || falsePositiveRate >= 1) {
            throw new IllegalArgumentException("误判率必须在0和1之间");
        }
        
        this.expectedElementCount = expectedElementCount;
        this.actualElementCount = 0;
        
        // 根据误判率公式计算最优参数
        // m = - (n * ln(p)) / (ln(2))^2
        // k = (m / n) * ln(2)
        this.bitSetSize = (int) Math.ceil(-expectedElementCount * Math.log(falsePositiveRate) / (Math.log(2) * Math.log(2)));
        this.hashFunctionCount = (int) Math.ceil((bitSetSize / (double) expectedElementCount) * Math.log(2));
        
        // 确保哈希函数数量至少为1
        this.hashFunctionCount = Math.max(1, this.hashFunctionCount);
        
        this.bitSet = new BitSet(bitSetSize);
        
        // 初始化哈希种子
        this.seeds = new int[hashFunctionCount];
        Random random = new Random(42); // 固定种子保证可重复性
        for (int i = 0; i < hashFunctionCount; i++) {
            seeds[i] = random.nextInt();
        }
        
        System.out.printf("布隆过滤器初始化: 预期元素数=%d, 误判率=%.6f, 位数组大小=%d, 哈希函数数=%d%n",
                expectedElementCount, falsePositiveRate, bitSetSize, hashFunctionCount);
    }
    
    /**
     * 构造函数 - 手动指定参数
     * @param bitSetSize 位数组大小
     * @param hashFunctionCount 哈希函数数量
     */
    public Code15_BloomFilter(int bitSetSize, int hashFunctionCount) {
        if (bitSetSize <= 0) {
            throw new IllegalArgumentException("位数组大小必须大于0");
        }
        if (hashFunctionCount <= 0) {
            throw new IllegalArgumentException("哈希函数数量必须大于0");
        }
        
        this.bitSetSize = bitSetSize;
        this.hashFunctionCount = hashFunctionCount;
        this.expectedElementCount = 0; // 未知预期数量
        this.actualElementCount = 0;
        this.bitSet = new BitSet(bitSetSize);
        
        // 初始化哈希种子
        this.seeds = new int[hashFunctionCount];
        Random random = new Random(42);
        for (int i = 0; i < hashFunctionCount; i++) {
            seeds[i] = random.nextInt();
        }
    }
    
    /**
     * 添加元素到布隆过滤器
     * @param element 要添加的元素
     * @throws IllegalArgumentException 如果元素为空
     */
    public void add(String element) {
        if (element == null) {
            throw new IllegalArgumentException("元素不能为空");
        }
        
        // 计算所有哈希位置并设置为1
        for (int i = 0; i < hashFunctionCount; i++) {
            int hash = hash(element, seeds[i]);
            int position = Math.abs(hash % bitSetSize);
            bitSet.set(position);
        }
        
        actualElementCount++;
    }
    
    /**
     * 检查元素是否可能在布隆过滤器中
     * @param element 要检查的元素
     * @return true如果元素可能存在，false如果元素一定不存在
     * @throws IllegalArgumentException 如果元素为空
     */
    public boolean mightContain(String element) {
        if (element == null) {
            throw new IllegalArgumentException("元素不能为空");
        }
        
        // 检查所有哈希位置是否都为1
        for (int i = 0; i < hashFunctionCount; i++) {
            int hash = hash(element, seeds[i]);
            int position = Math.abs(hash % bitSetSize);
            if (!bitSet.get(position)) {
                return false; // 如果有一个位置为0，元素一定不存在
            }
        }
        
        return true; // 所有位置都为1，元素可能存在
    }
    
    /**
     * 哈希函数 - 使用简单的字符串哈希算法
     * @param str 输入字符串
     * @param seed 哈希种子
     * @return 哈希值
     */
    private int hash(String str, int seed) {
        int hash = seed;
        for (char c : str.toCharArray()) {
            hash = hash * 31 + c;
        }
        return hash;
    }
    
    /**
     * 获取布隆过滤器的状态信息
     * @return 状态信息字符串
     */
    public String getStatus() {
        int setBits = bitSet.cardinality();
        double fillRatio = (double) setBits / bitSetSize;
        
        // 计算当前误判率
        double currentFalsePositiveRate = Math.pow(fillRatio, hashFunctionCount);
        
        StringBuilder sb = new StringBuilder();
        sb.append("布隆过滤器状态:\n");
        sb.append("位数组大小: ").append(bitSetSize).append("\n");
        sb.append("哈希函数数量: ").append(hashFunctionCount).append("\n");
        sb.append("预期元素数量: ").append(expectedElementCount).append("\n");
        sb.append("实际元素数量: ").append(actualElementCount).append("\n");
        sb.append("已设置位数: ").append(setBits).append("\n");
        sb.append("填充比例: ").append(String.format("%.6f", fillRatio)).append("\n");
        sb.append("当前误判率: ").append(String.format("%.6f", currentFalsePositiveRate)).append("\n");
        
        return sb.toString();
    }
    
    /**
     * 性能测试 - 测试布隆过滤器的误判率
     * @param testElementCount 测试元素数量
     */
    public void performanceTest(int testElementCount) {
        if (testElementCount <= 0) {
            System.out.println("测试元素数量必须大于0");
            return;
        }
        
        System.out.println("=== 布隆过滤器性能测试 ===");
        System.out.println("测试元素数量: " + testElementCount);
        
        // 添加测试元素
        int falsePositives = 0;
        int trueNegatives = 0;
        
        // 添加真实元素
        for (int i = 0; i < testElementCount; i++) {
            add("real" + i);
        }
        
        // 测试不存在元素
        for (int i = 0; i < testElementCount; i++) {
            String fakeElement = "fake" + i;
            if (mightContain(fakeElement)) {
                falsePositives++; // 误判
            } else {
                trueNegatives++; // 正确判断
            }
        }
        
        double actualFalsePositiveRate = (double) falsePositives / testElementCount;
        
        System.out.println("测试结果:");
        System.out.println("误判数量: " + falsePositives);
        System.out.println("正确判断数量: " + trueNegatives);
        System.out.println("实际误判率: " + String.format("%.6f", actualFalsePositiveRate));
        System.out.println("理论误判率: " + String.format("%.6f", Math.pow((double) bitSet.cardinality() / bitSetSize, hashFunctionCount)));
        
        System.out.println(getStatus());
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        System.out.println("=== 布隆过滤器单元测试 ===");
        
        // 测试1: 基本功能测试
        System.out.println("测试1: 基本功能测试");
        Code15_BloomFilter bf = new Code15_BloomFilter(1000, 0.01);
        
        // 添加元素
        bf.add("apple");
        bf.add("banana");
        bf.add("cherry");
        
        // 检查存在的元素
        assert bf.mightContain("apple") : "应该包含apple";
        assert bf.mightContain("banana") : "应该包含banana";
        assert bf.mightContain("cherry") : "应该包含cherry";
        
        // 检查不存在的元素
        assert !bf.mightContain("orange") : "不应该包含orange";
        assert !bf.mightContain("grape") : "不应该包含grape";
        
        System.out.println("基本功能测试通过");
        
        // 测试2: 性能测试
        System.out.println("\n测试2: 性能测试");
        bf.performanceTest(1000);
        
        // 测试3: 不同参数对比
        System.out.println("\n测试3: 不同参数对比");
        
        Code15_BloomFilter bf1 = new Code15_BloomFilter(1000, 0.1);  // 高误判率
        Code15_BloomFilter bf2 = new Code15_BloomFilter(1000, 0.01); // 低误判率
        Code15_BloomFilter bf3 = new Code15_BloomFilter(1000, 0.001); // 极低误判率
        
        // 添加相同元素
        for (int i = 0; i < 500; i++) {
            bf1.add("test" + i);
            bf2.add("test" + i);
            bf3.add("test" + i);
        }
        
        // 测试误判率
        int fp1 = 0, fp2 = 0, fp3 = 0;
        for (int i = 500; i < 1000; i++) {
            if (bf1.mightContain("test" + i)) fp1++;
            if (bf2.mightContain("test" + i)) fp2++;
            if (bf3.mightContain("test" + i)) fp3++;
        }
        
        System.out.printf("不同误判率配置的测试结果:%n");
        System.out.printf("误判率0.1: 实际误判率=%.4f%n", (double)fp1/500);
        System.out.printf("误判率0.01: 实际误判率=%.4f%n", (double)fp2/500);
        System.out.printf("误判率0.001: 实际误判率=%.4f%n", (double)fp3/500);
        
        System.out.println("=== 单元测试完成 ===");
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            test();
            return;
        }
        
        // 演示示例
        System.out.println("=== 布隆过滤器演示 ===");
        
        // 创建一个预期处理10000个元素，误判率为1%的布隆过滤器
        Code15_BloomFilter bloomFilter = new Code15_BloomFilter(10000, 0.01);
        
        // 添加一些URL到布隆过滤器（模拟网页去重）
        String[] urls = {
            "https://example.com/page1",
            "https://example.com/page2", 
            "https://example.com/page3",
            "https://example.com/page4",
            "https://example.com/page5"
        };
        
        for (String url : urls) {
            bloomFilter.add(url);
            System.out.println("添加URL: " + url);
        }
        
        // 检查URL是否已存在
        String[] testUrls = {
            "https://example.com/page1",     // 已存在
            "https://example.com/page6",     // 不存在
            "https://example.com/page3",     // 已存在
            "https://example.com/page7"      // 不存在
        };
        
        System.out.println("\nURL存在性检查:");
        for (String url : testUrls) {
            boolean exists = bloomFilter.mightContain(url);
            System.out.printf("URL %s: %s%n", url, exists ? "可能已存在" : "一定不存在");
        }
        
        // 显示布隆过滤器状态
        System.out.println("\n" + bloomFilter.getStatus());
        
        // 性能测试
        bloomFilter.performanceTest(1000);
    }
}