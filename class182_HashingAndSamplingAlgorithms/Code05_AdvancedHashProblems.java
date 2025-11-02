package class107;

import java.util.*;

/**
 * 高级哈希算法题目集合
 * 
 * 本文件包含各大算法平台的高级哈希相关题目，包括：
 * - 滚动哈希 (Rabin-Karp算法)
 * - 布隆过滤器 (Bloom Filter)
 * - 一致性哈希 (Consistent Hashing)
 * - 完美哈希 (Perfect Hashing)
 * - 分布式哈希表 (DHT)
 * 
 * 这些算法在分布式系统、大数据处理、网络安全等领域有重要应用
 */

public class Code05_AdvancedHashProblems {
    
    /**
     * 滚动哈希算法实现 (Rabin-Karp算法)
     * 应用场景：字符串匹配、子串查找、重复检测等
     * 
     * 算法原理：
     * 1. 使用多项式哈希函数计算字符串的哈希值
     * 2. 通过滑动窗口实现O(1)时间复杂度的哈希值更新
     * 3. 使用大质数取模防止整数溢出
     * 
     * 时间复杂度：O(n + m)，其中n是文本长度，m是模式长度
     * 空间复杂度：O(1)
     */
    public static class RollingHashSolution {
        private static final int BASE = 256; // 字符集大小
        private static final int MOD = 1000000007; // 大质数
        
        /**
         * Rabin-Karp字符串匹配算法
         * 
         * @param text 文本字符串
         * @param pattern 模式字符串
         * @return 模式在文本中出现的起始位置列表
         */
        public List<Integer> rabinKarp(String text, String pattern) {
            List<Integer> result = new ArrayList<>();
            
            if (text == null || pattern == null || pattern.length() > text.length()) {
                return result;
            }
            
            int n = text.length();
            int m = pattern.length();
            
            // 计算BASE^(m-1) mod MOD
            long highestPower = 1;
            for (int i = 0; i < m - 1; i++) {
                highestPower = (highestPower * BASE) % MOD;
            }
            
            // 计算模式和文本前m个字符的哈希值
            long patternHash = 0;
            long textHash = 0;
            
            for (int i = 0; i < m; i++) {
                patternHash = (patternHash * BASE + pattern.charAt(i)) % MOD;
                textHash = (textHash * BASE + text.charAt(i)) % MOD;
            }
            
            // 滑动窗口匹配
            for (int i = 0; i <= n - m; i++) {
                // 哈希值匹配时，进行精确比较（防止哈希冲突）
                if (patternHash == textHash) {
                    boolean match = true;
                    for (int j = 0; j < m; j++) {
                        if (text.charAt(i + j) != pattern.charAt(j)) {
                            match = false;
                            break;
                        }
                    }
                    if (match) {
                        result.add(i);
                    }
                }
                
                // 更新下一个窗口的哈希值
                if (i < n - m) {
                    textHash = (textHash - text.charAt(i) * highestPower) % MOD;
                    textHash = (textHash * BASE + text.charAt(i + m)) % MOD;
                    
                    // 处理负数
                    if (textHash < 0) {
                        textHash += MOD;
                    }
                }
            }
            
            return result;
        }
        
        /**
         * 计算字符串的所有不同子串数量（使用滚动哈希）
         * 
         * @param s 输入字符串
         * @return 不同子串的数量
         */
        public int countDistinctSubstrings(String s) {
            if (s == null || s.length() == 0) {
                return 0;
            }
            
            Set<Long> hashSet = new HashSet<>();
            int n = s.length();
            
            for (int i = 0; i < n; i++) {
                long hash = 0;
                for (int j = i; j < n; j++) {
                    hash = (hash * BASE + s.charAt(j)) % MOD;
                    hashSet.add(hash);
                }
            }
            
            return hashSet.size();
        }
        
        /**
         * 查找最长重复子串（使用滚动哈希和二分搜索）
         * 
         * @param s 输入字符串
         * @return 最长重复子串
         */
        public String longestRepeatingSubstring(String s) {
            if (s == null || s.length() == 0) {
                return "";
            }
            
            int n = s.length();
            int left = 1, right = n - 1;
            String result = "";
            
            while (left <= right) {
                int mid = left + (right - left) / 2;
                String found = findRepeatingSubstring(s, mid);
                
                if (!found.isEmpty()) {
                    result = found;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            return result;
        }
        
        private String findRepeatingSubstring(String s, int length) {
            Map<Long, List<Integer>> hashMap = new HashMap<>();
            long hash = 0;
            long power = 1;
            
            // 计算BASE^(length-1) mod MOD
            for (int i = 0; i < length - 1; i++) {
                power = (power * BASE) % MOD;
            }
            
            // 计算第一个窗口的哈希值
            for (int i = 0; i < length; i++) {
                hash = (hash * BASE + s.charAt(i)) % MOD;
            }
            
            hashMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(0);
            
            // 滑动窗口
            for (int i = 1; i <= s.length() - length; i++) {
                hash = (hash - s.charAt(i - 1) * power) % MOD;
                hash = (hash * BASE + s.charAt(i + length - 1)) % MOD;
                
                if (hash < 0) {
                    hash += MOD;
                }
                
                if (hashMap.containsKey(hash)) {
                    String current = s.substring(i, i + length);
                    for (int start : hashMap.get(hash)) {
                        if (s.substring(start, start + length).equals(current)) {
                            return current;
                        }
                    }
                }
                
                hashMap.computeIfAbsent(hash, k -> new ArrayList<>()).add(i);
            }
            
            return "";
        }
    }
    
    /**
     * 布隆过滤器实现
     * 应用场景：大规模数据去重、缓存穿透防护、垃圾邮件过滤等
     * 
     * 算法原理：
     * 1. 使用k个哈希函数将元素映射到位数组的k个位置
     * 2. 查询时检查所有k个位置是否都为1
     * 3. 存在假阳性（false positive），但不存在假阴性（false negative）
     * 
     * 时间复杂度：O(k)
     * 空间复杂度：O(m)，其中m是位数组大小
     */
    public static class BloomFilter {
        private final int size; // 位数组大小
        private final int hashCount; // 哈希函数数量
        private final BitSet bitSet; // 位数组
        
        /**
         * 构造函数
         * 
         * @param expectedElements 预期元素数量
         * @param falsePositiveRate 期望的假阳性率
         */
        public BloomFilter(int expectedElements, double falsePositiveRate) {
            this.size = calculateSize(expectedElements, falsePositiveRate);
            this.hashCount = calculateHashCount(expectedElements, size);
            this.bitSet = new BitSet(size);
        }
        
        /**
         * 计算位数组大小
         */
        private int calculateSize(int n, double p) {
            if (p == 0) {
                p = Double.MIN_VALUE;
            }
            return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
        }
        
        /**
         * 计算哈希函数数量
         */
        private int calculateHashCount(int n, int m) {
            return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
        }
        
        /**
         * 添加元素
         */
        public void add(String element) {
            for (int i = 0; i < hashCount; i++) {
                int hash = hash(element, i);
                bitSet.set(Math.abs(hash % size));
            }
        }
        
        /**
         * 检查元素是否存在
         */
        public boolean contains(String element) {
            for (int i = 0; i < hashCount; i++) {
                int hash = hash(element, i);
                if (!bitSet.get(Math.abs(hash % size))) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 哈希函数（使用不同的种子生成不同的哈希值）
         */
        private int hash(String element, int seed) {
            int hash = 0;
            for (char c : element.toCharArray()) {
                hash = seed * hash + c;
            }
            return hash;
        }
        
        /**
         * 获取当前假阳性率的估计值
         */
        public double estimateFalsePositiveRate(int insertedElements) {
            return Math.pow(1 - Math.exp(-hashCount * (double) insertedElements / size), hashCount);
        }
        
        /**
         * 获取位数组的使用率
         */
        public double getUsageRate() {
            return (double) bitSet.cardinality() / size;
        }
    }
    
    /**
     * 一致性哈希算法实现
     * 应用场景：分布式缓存、负载均衡、分布式存储等
     * 
     * 算法原理：
     * 1. 将节点和键都映射到哈希环上
     * 2. 每个键顺时针找到的第一个节点就是其归属节点
     * 3. 节点增减时，只有少量数据需要重新分配
     * 
     * 时间复杂度：O(log n) 查找，其中n是虚拟节点数量
     * 空间复杂度：O(n)
     */
    public static class ConsistentHashing {
        private final TreeMap<Integer, String> circle; // 哈希环
        private final int virtualNodeCount; // 每个物理节点的虚拟节点数量
        
        public ConsistentHashing(int virtualNodeCount) {
            this.circle = new TreeMap<>();
            this.virtualNodeCount = virtualNodeCount;
        }
        
        /**
         * 添加节点
         */
        public void addNode(String node) {
            for (int i = 0; i < virtualNodeCount; i++) {
                String virtualNode = node + "#" + i;
                int hash = hash(virtualNode);
                circle.put(hash, node);
            }
        }
        
        /**
         * 移除节点
         */
        public void removeNode(String node) {
            for (int i = 0; i < virtualNodeCount; i++) {
                String virtualNode = node + "#" + i;
                int hash = hash(virtualNode);
                circle.remove(hash);
            }
        }
        
        /**
         * 获取键对应的节点
         */
        public String getNode(String key) {
            if (circle.isEmpty()) {
                return null;
            }
            
            int hash = hash(key);
            Map.Entry<Integer, String> entry = circle.ceilingEntry(hash);
            
            if (entry == null) {
                // 环回，返回第一个节点
                entry = circle.firstEntry();
            }
            
            return entry.getValue();
        }
        
        /**
         * 获取所有节点
         */
        public Set<String> getAllNodes() {
            return new HashSet<>(circle.values());
        }
        
        /**
         * 计算数据分布的不平衡度
         */
        public double calculateImbalance(Map<String, Integer> keyDistribution) {
            if (keyDistribution.isEmpty()) {
                return 0.0;
            }
            
            double average = keyDistribution.values().stream()
                    .mapToInt(Integer::intValue).average().orElse(0.0);
            
            double variance = keyDistribution.values().stream()
                    .mapToDouble(count -> Math.pow(count - average, 2))
                    .average().orElse(0.0);
            
            return Math.sqrt(variance) / average; // 变异系数
        }
        
        /**
         * 哈希函数
         */
        private int hash(String key) {
            return Math.abs(key.hashCode());
        }
    }
    
    /**
     * 完美哈希实现（两级哈希表）
     * 应用场景：静态数据集、编译器符号表、字典等
     * 
     * 算法原理：
     * 1. 第一级哈希将元素分组
     * 2. 第二级为每个组创建无冲突的哈希表
     * 3. 保证O(1)查找时间，无哈希冲突
     * 
     * 时间复杂度：O(1) 查找
     * 空间复杂度：O(n)
     */
    public static class PerfectHashTable {
        private final int size; // 第一级哈希表大小
        private final HashFunction[] secondLevel; // 第二级哈希表
        private final Object[][] tables; // 存储数据的表
        
        public PerfectHashTable(Set<String> keys) {
            this.size = keys.size();
            this.secondLevel = new HashFunction[size];
            this.tables = new Object[size][];
            
            buildPerfectHash(keys);
        }
        
        /**
         * 构建完美哈希表
         */
        private void buildPerfectHash(Set<String> keys) {
            // 第一级：将键分组
            Map<Integer, List<String>> groups = new HashMap<>();
            
            for (String key : keys) {
                int hash = firstLevelHash(key);
                groups.computeIfAbsent(hash, k -> new ArrayList<>()).add(key);
            }
            
            // 第二级：为每个组构建无冲突哈希表
            for (Map.Entry<Integer, List<String>> entry : groups.entrySet()) {
                int groupIndex = entry.getKey();
                List<String> groupKeys = entry.getValue();
                
                // 为这个组找到无冲突的哈希函数
                HashFunction hashFunc = findPerfectHashFunction(groupKeys);
                secondLevel[groupIndex] = hashFunc;
                
                // 创建第二级哈希表
                tables[groupIndex] = new Object[hashFunc.tableSize];
                for (String key : groupKeys) {
                    int pos = hashFunc.hash(key);
                    tables[groupIndex][pos] = key;
                }
            }
        }
        
        /**
         * 查找键
         */
        public boolean contains(String key) {
            int firstLevel = firstLevelHash(key);
            if (secondLevel[firstLevel] == null) {
                return false;
            }
            
            int secondLevelPos = secondLevel[firstLevel].hash(key);
            return key.equals(tables[firstLevel][secondLevelPos]);
        }
        
        /**
         * 第一级哈希函数
         */
        private int firstLevelHash(String key) {
            return Math.abs(key.hashCode()) % size;
        }
        
        /**
         * 为给定的键集合找到无冲突的哈希函数
         */
        private HashFunction findPerfectHashFunction(List<String> keys) {
            if (keys.isEmpty()) {
                return new HashFunction(0, 0, 0);
            }
            
            int tableSize = keys.size() * keys.size(); // 平方空间保证无冲突
            
            // 尝试不同的哈希参数直到找到无冲突的
            Random random = new Random();
            while (true) {
                int a = random.nextInt(1000) + 1;
                int b = random.nextInt(1000) + 1;
                
                Set<Integer> positions = new HashSet<>();
                boolean collision = false;
                
                for (String key : keys) {
                    int pos = (a * key.hashCode() + b) % tableSize;
                    if (positions.contains(pos)) {
                        collision = true;
                        break;
                    }
                    positions.add(pos);
                }
                
                if (!collision) {
                    return new HashFunction(a, b, tableSize);
                }
            }
        }
        
        /**
         * 哈希函数类
         */
        private static class HashFunction {
            final int a, b, tableSize;
            
            HashFunction(int a, int b, int tableSize) {
                this.a = a;
                this.b = b;
                this.tableSize = tableSize;
            }
            
            int hash(String key) {
                return Math.abs(a * key.hashCode() + b) % tableSize;
            }
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 高级哈希算法测试 ===\n");
        
        // 测试滚动哈希
        System.out.println("1. 滚动哈希算法测试:");
        RollingHashSolution rollingHash = new RollingHashSolution();
        
        String text = "abracadabra";
        String pattern = "cad";
        List<Integer> positions = rollingHash.rabinKarp(text, pattern);
        System.out.println("文本: \"" + text + "\", 模式: \"" + pattern + "\"");
        System.out.println("匹配位置: " + positions);
        
        int distinctCount = rollingHash.countDistinctSubstrings("abc");
        System.out.println("字符串'abc'的不同子串数量: " + distinctCount);
        
        String repeating = rollingHash.longestRepeatingSubstring("banana");
        System.out.println("字符串'banana'的最长重复子串: \"" + repeating + "\"");
        
        // 测试布隆过滤器
        System.out.println("\n2. 布隆过滤器测试:");
        BloomFilter bloomFilter = new BloomFilter(1000, 0.01);
        
        bloomFilter.add("hello");
        bloomFilter.add("world");
        bloomFilter.add("test");
        
        System.out.println("包含'hello': " + bloomFilter.contains("hello"));
        System.out.println("包含'unknown': " + bloomFilter.contains("unknown"));
        System.out.println("使用率: " + String.format("%.2f%%", bloomFilter.getUsageRate() * 100));
        
        // 测试一致性哈希
        System.out.println("\n3. 一致性哈希测试:");
        ConsistentHashing consistentHashing = new ConsistentHashing(100);
        
        consistentHashing.addNode("node1");
        consistentHashing.addNode("node2");
        consistentHashing.addNode("node3");
        
        Map<String, Integer> distribution = new HashMap<>();
        for (int i = 0; i < 1000; i++) {
            String key = "key" + i;
            String node = consistentHashing.getNode(key);
            distribution.put(node, distribution.getOrDefault(node, 0) + 1);
        }
        
        System.out.println("数据分布: " + distribution);
        System.out.println("不平衡度: " + consistentHashing.calculateImbalance(distribution));
        
        // 测试完美哈希
        System.out.println("\n4. 完美哈希测试:");
        Set<String> keys = Set.of("apple", "banana", "cherry", "date", "elderberry");
        PerfectHashTable perfectHash = new PerfectHashTable(keys);
        
        for (String key : keys) {
            System.out.println("包含'" + key + "': " + perfectHash.contains(key));
        }
        System.out.println("包含'unknown': " + perfectHash.contains("unknown"));
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. 滚动哈希: O(n+m) 时间, O(1) 空间");
        System.out.println("2. 布隆过滤器: O(k) 时间, O(m) 空间");
        System.out.println("3. 一致性哈希: O(log n) 时间, O(n) 空间");
        System.out.println("4. 完美哈希: O(1) 时间, O(n) 空间");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 滚动哈希: 字符串匹配、重复检测、版本控制");
        System.out.println("2. 布隆过滤器: 缓存系统、垃圾邮件过滤、爬虫去重");
        System.out.println("3. 一致性哈希: 分布式缓存、负载均衡、分布式存储");
        System.out.println("4. 完美哈希: 编译器符号表、静态字典、配置文件");
    }
}