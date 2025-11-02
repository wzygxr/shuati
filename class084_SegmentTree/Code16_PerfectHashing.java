package class111;

// 完美哈希算法实现 (Java版本)
// 题目来源: 数据库索引、编译器符号表、静态字典查找
// 应用场景: 静态数据集的高效查找、编译时符号表、数据库索引优化
// 题目描述: 实现完美哈希算法，为静态数据集构建无冲突的哈希函数
// 
// 解题思路:
// 1. 使用两级哈希结构：第一级哈希将元素分配到桶中，第二级为每个桶构建完美哈希
// 2. 第一级使用通用哈希函数，第二级为每个桶构建自定义哈希函数
// 3. 通过调整哈希参数确保每个桶内无冲突
// 
// 时间复杂度分析:
// - 构建时间: O(n^2) 最坏情况，但实际中通常为 O(n)
// - 查找时间: O(1) 最坏情况
// 
// 空间复杂度: O(n)
// 
// 工程化考量:
// 1. 静态数据集: 完美哈希适用于数据不变化的场景
// 2. 构建成本: 构建过程较复杂，但查询效率极高
// 3. 内存优化: 使用紧凑的数据结构存储哈希参数
// 4. 适用场景: 适合编译时符号表、静态字典等

import java.util.*;

public class Code16_PerfectHashing {
    
    // 第一级哈希函数参数
    private int a1, b1, p1, m1;
    
    // 第二级哈希函数参数数组
    private int[] a2, b2, p2, m2;
    
    // 第二级哈希表数组
    private String[][] secondLevelTables;
    
    // 数据集大小
    private int n;
    
    /**
     * 构造函数 - 为给定的静态数据集构建完美哈希
     * @param keys 静态数据集（不能包含重复元素）
     * @throws IllegalArgumentException 如果数据集为空或包含重复元素
     */
    public Code16_PerfectHashing(String[] keys) {
        if (keys == null || keys.length == 0) {
            throw new IllegalArgumentException("数据集不能为空");
        }
        
        this.n = keys.length;
        
        // 检查重复元素
        Set<String> keySet = new HashSet<>(Arrays.asList(keys));
        if (keySet.size() != n) {
            throw new IllegalArgumentException("数据集包含重复元素");
        }
        
        // 初始化第一级哈希参数
        initializeFirstLevel();
        
        // 构建完美哈希结构
        buildPerfectHash(keys);
    }
    
    /**
     * 初始化第一级哈希参数
     */
    private void initializeFirstLevel() {
        Random random = new Random(42);
        
        // 选择足够大的质数
        p1 = nextPrime(n * n);
        m1 = n; // 第一级桶数量等于元素数量
        
        a1 = random.nextInt(p1 - 1) + 1; // a ∈ [1, p-1]
        b1 = random.nextInt(p1);           // b ∈ [0, p-1]
    }
    
    /**
     * 构建完美哈希结构
     * @param keys 静态数据集
     */
    private void buildPerfectHash(String[] keys) {
        // 第一级哈希：将元素分配到桶中
        List<List<String>> buckets = new ArrayList<>(m1);
        for (int i = 0; i < m1; i++) {
            buckets.add(new ArrayList<>());
        }
        
        // 分配元素到桶中
        for (String key : keys) {
            int bucketIndex = firstLevelHash(key);
            buckets.get(bucketIndex).add(key);
        }
        
        // 初始化第二级结构
        a2 = new int[m1];
        b2 = new int[m1];
        p2 = new int[m1];
        m2 = new int[m1];
        secondLevelTables = new String[m1][];
        
        Random random = new Random(42);
        
        // 为每个桶构建第二级完美哈希
        for (int i = 0; i < m1; i++) {
            List<String> bucket = buckets.get(i);
            int bucketSize = bucket.size();
            
            if (bucketSize == 0) {
                // 空桶
                m2[i] = 0;
                secondLevelTables[i] = new String[0];
                continue;
            }
            
            // 计算第二级哈希表大小：桶大小的平方（确保无冲突）
            m2[i] = bucketSize * bucketSize;
            
            // 选择质数
            p2[i] = nextPrime(m2[i]);
            
            boolean collisionFree = false;
            int attempts = 0;
            
            // 尝试不同的哈希参数直到无冲突
            while (!collisionFree && attempts < 100) {
                a2[i] = random.nextInt(p2[i] - 1) + 1;
                b2[i] = random.nextInt(p2[i]);
                
                String[] table = new String[m2[i]];
                collisionFree = true;
                
                // 测试当前参数是否会产生冲突
                for (String key : bucket) {
                    int hash = secondLevelHash(key, i);
                    if (table[hash] != null) {
                        // 发生冲突，重新选择参数
                        collisionFree = false;
                        break;
                    }
                    table[hash] = key;
                }
                
                if (collisionFree) {
                    // 无冲突，保存结果
                    secondLevelTables[i] = table;
                }
                
                attempts++;
            }
            
            if (!collisionFree) {
                throw new RuntimeException("无法为桶 " + i + " 构建无冲突的完美哈希");
            }
        }
        
        System.out.println("完美哈希构建完成，数据集大小: " + n);
    }
    
    /**
     * 第一级哈希函数
     * @param key 键
     * @return 桶索引
     */
    private int firstLevelHash(String key) {
        long hash = 0;
        for (char c : key.toCharArray()) {
            hash = (hash * 31 + c) % p1;
        }
        hash = (a1 * hash + b1) % p1;
        return (int) (hash % m1);
    }
    
    /**
     * 第二级哈希函数
     * @param key 键
     * @param bucketIndex 桶索引
     * @return 第二级哈希表中的位置
     */
    private int secondLevelHash(String key, int bucketIndex) {
        long hash = 0;
        for (char c : key.toCharArray()) {
            hash = (hash * 31 + c) % p2[bucketIndex];
        }
        hash = (a2[bucketIndex] * hash + b2[bucketIndex]) % p2[bucketIndex];
        return (int) (hash % m2[bucketIndex]);
    }
    
    /**
     * 查找元素是否存在于完美哈希表中
     * @param key 要查找的键
     * @return true如果存在，false如果不存在
     */
    public boolean contains(String key) {
        if (key == null) {
            return false;
        }
        
        // 第一级哈希确定桶
        int bucketIndex = firstLevelHash(key);
        
        // 检查桶是否为空
        if (m2[bucketIndex] == 0) {
            return false;
        }
        
        // 第二级哈希定位元素
        int position = secondLevelHash(key, bucketIndex);
        
        // 检查该位置是否存储了目标元素
        return key.equals(secondLevelTables[bucketIndex][position]);
    }
    
    /**
     * 查找下一个质数
     * @param n 起始数字
     * @return 大于等于n的最小质数
     */
    private int nextPrime(int n) {
        if (n <= 2) return 2;
        if (n % 2 == 0) n++;
        
        while (!isPrime(n)) {
            n += 2;
        }
        return n;
    }
    
    /**
     * 判断是否为质数
     * @param n 数字
     * @return true如果是质数
     */
    private boolean isPrime(int n) {
        if (n <= 1) return false;
        if (n <= 3) return true;
        if (n % 2 == 0 || n % 3 == 0) return false;
        
        for (int i = 5; i * i <= n; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取完美哈希表的状态信息
     * @return 状态信息字符串
     */
    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("完美哈希表状态:\n");
        sb.append("数据集大小: ").append(n).append("\n");
        sb.append("第一级桶数量: ").append(m1).append("\n");
        sb.append("第一级哈希参数: a1=").append(a1).append(", b1=").append(b1).append(", p1=").append(p1).append("\n");
        
        // 统计桶分布
        int emptyBuckets = 0;
        int maxBucketSize = 0;
        long totalSecondLevelSize = 0;
        
        for (int i = 0; i < m1; i++) {
            if (m2[i] == 0) {
                emptyBuckets++;
            } else {
                int bucketSize = (int) Math.sqrt(m2[i]);
                maxBucketSize = Math.max(maxBucketSize, bucketSize);
                totalSecondLevelSize += m2[i];
            }
        }
        
        sb.append("空桶数量: ").append(emptyBuckets).append("\n");
        sb.append("最大桶大小: ").append(maxBucketSize).append("\n");
        sb.append("第二级总空间: ").append(totalSecondLevelSize).append("\n");
        sb.append("空间利用率: ").append(String.format("%.2f%%", (double) n / totalSecondLevelSize * 100)).append("\n");
        
        return sb.toString();
    }
    
    /**
     * 性能测试 - 测试查找性能
     * @param testKeys 测试键数组
     */
    public void performanceTest(String[] testKeys) {
        if (testKeys == null || testKeys.length == 0) {
            System.out.println("测试键数组不能为空");
            return;
        }
        
        System.out.println("=== 完美哈希性能测试 ===");
        System.out.println("测试键数量: " + testKeys.length);
        
        long startTime = System.nanoTime();
        
        int found = 0;
        int notFound = 0;
        
        for (String key : testKeys) {
            if (contains(key)) {
                found++;
            } else {
                notFound++;
            }
        }
        
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        
        System.out.println("测试结果:");
        System.out.println("找到元素: " + found);
        System.out.println("未找到元素: " + notFound);
        System.out.println("总查找时间: " + duration + " 纳秒");
        System.out.println("平均查找时间: " + (double) duration / testKeys.length + " 纳秒/次");
        System.out.println("查找吞吐量: " + String.format("%.2f", (double) testKeys.length / (duration / 1e9)) + " 次/秒");
    }
    
    /**
     * 单元测试方法
     */
    public static void test() {
        System.out.println("=== 完美哈希单元测试 ===");
        
        // 测试1: 基本功能测试
        System.out.println("测试1: 基本功能测试");
        String[] keys = {"apple", "banana", "cherry", "date", "elderberry", "fig", "grape"};
        Code16_PerfectHashing ph = new Code16_PerfectHashing(keys);
        
        // 测试存在的元素
        for (String key : keys) {
            assert ph.contains(key) : "应该包含 " + key;
        }
        
        // 测试不存在的元素
        assert !ph.contains("honeydew") : "不应该包含 honeydew";
        assert !ph.contains("kiwi") : "不应该包含 kiwi";
        
        System.out.println("基本功能测试通过");
        
        // 测试2: 性能测试
        System.out.println("\n测试2: 性能测试");
        ph.performanceTest(keys);
        
        // 测试3: 大规模数据测试
        System.out.println("\n测试3: 大规模数据测试");
        String[] largeKeys = new String[1000];
        for (int i = 0; i < 1000; i++) {
            largeKeys[i] = "key" + i;
        }
        
        Code16_PerfectHashing largePH = new Code16_PerfectHashing(largeKeys);
        largePH.performanceTest(largeKeys);
        
        System.out.println(largePH.getStatus());
        
        System.out.println("=== 单元测试完成 ===");
    }
    
    public static void main(String[] args) {
        if (args.length > 0 && "test".equals(args[0])) {
            test();
            return;
        }
        
        // 演示示例
        System.out.println("=== 完美哈希演示 ===");
        
        // 创建一个编程语言关键字的完美哈希表
        String[] programmingKeywords = {
            "if", "else", "for", "while", "do", "switch", "case", "break",
            "continue", "return", "class", "interface", "extends", "implements",
            "public", "private", "protected", "static", "final", "abstract",
            "void", "int", "long", "float", "double", "boolean", "char", "String"
        };
        
        Code16_PerfectHashing keywordTable = new Code16_PerfectHashing(programmingKeywords);
        
        System.out.println("构建编程语言关键字完美哈希表");
        System.out.println(keywordTable.getStatus());
        
        // 测试关键字查找
        String[] testKeywords = {"if", "for", "class", "xyz", "abc", "static"};
        
        System.out.println("\n关键字查找测试:");
        for (String keyword : testKeywords) {
            boolean exists = keywordTable.contains(keyword);
            System.out.printf("关键字 '%s': %s%n", keyword, exists ? "存在" : "不存在");
        }
        
        // 性能测试
        keywordTable.performanceTest(programmingKeywords);
    }
}