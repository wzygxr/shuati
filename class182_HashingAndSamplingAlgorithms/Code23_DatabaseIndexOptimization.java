package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * 数据库索引优化 - 完美哈希应用
 * 题目链接：无特定链接，这是一个工程化应用场景
 * 
 * 题目描述：
 * 在数据库系统中，索引是提高查询性能的关键技术。对于静态数据集（如配置表、字典表），
 * 可以使用完美哈希技术构建最优的索引结构，实现O(1)时间复杂度的查找操作。
 * 
 * 算法核心思想：
 * 1. 使用两级哈希结构：第一级哈希将记录分配到桶中，第二级为每个桶构建完美哈希
 * 2. 第一级使用通用哈希函数，第二级为每个桶构建自定义哈希函数
 * 3. 通过调整哈希参数确保每个桶内无冲突
 * 4. 优化存储结构，减少内存占用
 * 
 * 时间复杂度：O(1) 查找
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 静态数据集：完美哈希适用于数据不频繁变化的场景
 * 2. 构建成本：构建过程较复杂，但查询效率极高
 * 3. 内存优化：使用紧凑的数据结构存储哈希参数
 * 4. 适用场景：适合配置表、字典表等静态数据的索引
 */
public class Code23_DatabaseIndexOptimization {
    
    // 数据库记录类
    public static class Record {
        private final int id;
        private final String key;
        private final Map<String, Object> fields;
        
        public Record(int id, String key, Map<String, Object> fields) {
            this.id = id;
            this.key = key;
            this.fields = new HashMap<>(fields);
        }
        
        public int getId() { return id; }
        public String getKey() { return key; }
        public Object getField(String fieldName) { return fields.get(fieldName); }
        public Set<String> getFieldNames() { return fields.keySet(); }
        
        @Override
        public String toString() {
            return "Record{id=" + id + ", key='" + key + "', fields=" + fields + "}";
        }
    }
    
    // 完美哈希索引实现
    public static class PerfectHashIndex {
        // 第一级哈希函数参数
        private int a1, b1, p1, m1;
        
        // 第二级哈希函数参数数组
        private int[] a2, b2, p2, m2;
        
        // 第二级哈希表数组
        private Record[][] secondLevelTables;
        
        // 数据集大小
        private int n;
        
        // 键到记录的映射
        private Map<String, Record> keyToRecord;
        
        /**
         * 构造函数 - 为给定的静态数据集构建完美哈希索引
         * @param records 静态数据集（不能包含重复键）
         * @throws IllegalArgumentException 如果数据集为空或包含重复键
         */
        public PerfectHashIndex(List<Record> records) {
            if (records == null || records.isEmpty()) {
                throw new IllegalArgumentException("数据集不能为空");
            }
            
            this.n = records.size();
            this.keyToRecord = new HashMap<>();
            
            // 检查重复键
            Set<String> keySet = new HashSet<>();
            for (Record record : records) {
                if (!keySet.add(record.getKey())) {
                    throw new IllegalArgumentException("数据集包含重复键: " + record.getKey());
                }
                keyToRecord.put(record.getKey(), record);
            }
            
            // 初始化第一级哈希参数
            initializeFirstLevel();
            
            // 构建完美哈希索引结构
            buildPerfectHash(records);
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
            b1 = random.nextInt(p1);         // b ∈ [0, p-1]
        }
        
        /**
         * 构建完美哈希索引结构
         * @param records 静态数据集
         */
        private void buildPerfectHash(List<Record> records) {
            // 第一级哈希：将记录分配到桶中
            List<List<Record>> buckets = new ArrayList<>(m1);
            for (int i = 0; i < m1; i++) {
                buckets.add(new ArrayList<>());
            }
            
            // 分配记录到桶中
            for (Record record : records) {
                int bucketIndex = firstLevelHash(record.getKey());
                buckets.get(bucketIndex).add(record);
            }
            
            // 初始化第二级结构
            a2 = new int[m1];
            b2 = new int[m1];
            p2 = new int[m1];
            m2 = new int[m1];
            secondLevelTables = new Record[m1][];
            
            Random random = new Random(42);
            
            // 为每个桶构建第二级完美哈希
            for (int i = 0; i < m1; i++) {
                List<Record> bucket = buckets.get(i);
                int bucketSize = bucket.size();
                
                if (bucketSize == 0) {
                    // 空桶
                    m2[i] = 0;
                    secondLevelTables[i] = new Record[0];
                    continue;
                }
                
                // 计算第二级哈希表大小：桶大小的平方（确保无冲突）
                m2[i] = bucketSize * bucketSize;
                
                // 选择质数
                p2[i] = nextPrime(m2[i]);
                
                boolean collisionFree = false;
                int attempts = 0;
                
                // 尝试不同的哈希参数直到无冲突
                while (!collisionFree && attempts < 1000) {  // 增加尝试次数
                    a2[i] = random.nextInt(p2[i] - 1) + 1;
                    b2[i] = random.nextInt(p2[i]);
                    
                    Record[] table = new Record[m2[i]];
                    collisionFree = true;
                    
                    // 测试当前参数是否会产生冲突
                    for (Record record : bucket) {
                        int hash = secondLevelHash(record.getKey(), i);
                        if (table[hash] != null) {
                            // 发生冲突，重新选择参数
                            collisionFree = false;
                            break;
                        }
                        table[hash] = record;
                    }
                    
                    if (collisionFree) {
                        // 无冲突，保存结果
                        secondLevelTables[i] = table;
                    }
                    
                    attempts++;
                }
                
                if (!collisionFree) {
                    // 如果仍然无法构建无冲突的哈希，使用线性探测作为备选方案
                    Record[] table = new Record[m2[i]];
                    for (Record record : bucket) {
                        int hash = secondLevelHash(record.getKey(), i);
                        // 线性探测找到空位置
                        while (table[hash] != null) {
                            hash = (hash + 1) % m2[i];
                        }
                        table[hash] = record;
                    }
                    secondLevelTables[i] = table;
                    System.out.println("警告: 桶 " + i + " 使用线性探测解决冲突");
                }
            }
            
            System.out.println("完美哈希索引构建完成，数据集大小: " + n);
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
         * 根据键查找记录
         * @param key 要查找的键
         * @return 记录对象，如果不存在则返回null
         */
        public Record findByKey(String key) {
            if (key == null) {
                return null;
            }
            
            // 第一级哈希确定桶
            int bucketIndex = firstLevelHash(key);
            
            // 检查桶是否为空
            if (m2[bucketIndex] == 0) {
                return null;
            }
            
            // 第二级哈希定位记录
            int position = secondLevelHash(key, bucketIndex);
            
            // 检查该位置是否存储了目标记录
            Record record = secondLevelTables[bucketIndex][position];
            if (record != null && key.equals(record.getKey())) {
                return record;
            }
            
            return null;
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
         * 获取索引的状态信息
         * @return 状态信息字符串
         */
        public String getStatus() {
            StringBuilder sb = new StringBuilder();
            sb.append("完美哈希索引状态:\n");
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
    }
    
    /**
     * 性能测试类
     */
    public static class PerformanceTest {
        /**
         * 测试完美哈希索引的性能
         * @param index 完美哈希索引
         * @param testKeys 测试键列表
         */
        public static void testPerfectHashIndex(PerfectHashIndex index, List<String> testKeys) {
            System.out.println("=== 完美哈希索引性能测试 ===");
            System.out.println("测试键数量: " + testKeys.size());
            
            // 测试查找性能
            long startTime = System.nanoTime();
            
            int found = 0;
            int notFound = 0;
            
            for (String key : testKeys) {
                Record record = index.findByKey(key);
                if (record != null) {
                    found++;
                } else {
                    notFound++;
                }
            }
            
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            System.out.println("测试结果:");
            System.out.println("找到记录: " + found);
            System.out.println("未找到记录: " + notFound);
            System.out.println("总查找时间: " + duration + " 纳秒");
            System.out.println("平均查找时间: " + (double) duration / testKeys.size() + " 纳秒/次");
            System.out.println("查找吞吐量: " + String.format("%.2f", (double) testKeys.size() / (duration / 1e9)) + " 次/秒");
        }
        
        /**
         * 与HashMap性能对比
         * @param records 记录列表
         * @param testKeys 测试键列表
         */
        public static void compareWithHashMap(List<Record> records, List<String> testKeys) {
            System.out.println("\n=== 与HashMap性能对比 ===");
            
            // 构建HashMap索引
            Map<String, Record> hashMapIndex = new HashMap<>();
            for (Record record : records) {
                hashMapIndex.put(record.getKey(), record);
            }
            
            // 测试HashMap查找性能
            long startTime = System.nanoTime();
            
            int found = 0;
            int notFound = 0;
            
            for (String key : testKeys) {
                Record record = hashMapIndex.get(key);
                if (record != null) {
                    found++;
                } else {
                    notFound++;
                }
            }
            
            long endTime = System.nanoTime();
            long duration = endTime - startTime;
            
            System.out.println("HashMap测试结果:");
            System.out.println("找到记录: " + found);
            System.out.println("未找到记录: " + notFound);
            System.out.println("总查找时间: " + duration + " 纳秒");
            System.out.println("平均查找时间: " + (double) duration / testKeys.size() + " 纳秒/次");
            System.out.println("查找吞吐量: " + String.format("%.2f", (double) testKeys.size() / (duration / 1e9)) + " 次/秒");
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 数据库索引优化 - 完美哈希应用测试 ===\n");
        
        // 创建测试数据
        List<Record> records = new ArrayList<>();
        
        // 创建国家信息记录
        Map<String, Object> chinaFields = new HashMap<>();
        chinaFields.put("name", "中国");
        chinaFields.put("capital", "北京");
        chinaFields.put("population", 1400000000L);
        chinaFields.put("area", 9600000);
        records.add(new Record(1, "CN", chinaFields));
        
        Map<String, Object> usaFields = new HashMap<>();
        usaFields.put("name", "美国");
        usaFields.put("capital", "华盛顿");
        usaFields.put("population", 330000000L);
        usaFields.put("area", 9800000);
        records.add(new Record(2, "US", usaFields));
        
        Map<String, Object> japanFields = new HashMap<>();
        japanFields.put("name", "日本");
        japanFields.put("capital", "东京");
        japanFields.put("population", 126000000L);
        japanFields.put("area", 378000);
        records.add(new Record(3, "JP", japanFields));
        
        Map<String, Object> germanyFields = new HashMap<>();
        germanyFields.put("name", "德国");
        germanyFields.put("capital", "柏林");
        germanyFields.put("population", 83000000L);
        germanyFields.put("area", 357000);
        records.add(new Record(4, "DE", germanyFields));
        
        Map<String, Object> franceFields = new HashMap<>();
        franceFields.put("name", "法国");
        franceFields.put("capital", "巴黎");
        franceFields.put("population", 67000000L);
        franceFields.put("area", 644000);
        records.add(new Record(5, "FR", franceFields));
        
        Map<String, Object> ukFields = new HashMap<>();
        ukFields.put("name", "英国");
        ukFields.put("capital", "伦敦");
        ukFields.put("population", 67000000L);
        ukFields.put("area", 242000);
        records.add(new Record(6, "GB", ukFields));
        
        Map<String, Object> canadaFields = new HashMap<>();
        canadaFields.put("name", "加拿大");
        canadaFields.put("capital", "渥太华");
        canadaFields.put("population", 38000000L);
        canadaFields.put("area", 10000000);
        records.add(new Record(7, "CA", canadaFields));
        
        Map<String, Object> australiaFields = new HashMap<>();
        australiaFields.put("name", "澳大利亚");
        australiaFields.put("capital", "堪培拉");
        australiaFields.put("population", 25000000L);
        australiaFields.put("area", 7692000);
        records.add(new Record(8, "AU", australiaFields));
        
        Map<String, Object> brazilFields = new HashMap<>();
        brazilFields.put("name", "巴西");
        brazilFields.put("capital", "巴西利亚");
        brazilFields.put("population", 213000000L);
        brazilFields.put("area", 8515000);
        records.add(new Record(9, "BR", brazilFields));
        
        Map<String, Object> indiaFields = new HashMap<>();
        indiaFields.put("name", "印度");
        indiaFields.put("capital", "新德里");
        indiaFields.put("population", 1380000000L);
        indiaFields.put("area", 3287000);
        records.add(new Record(10, "IN", indiaFields));
        
        // 构建完美哈希索引
        PerfectHashIndex index = new PerfectHashIndex(records);
        
        // 显示索引状态
        System.out.println(index.getStatus());
        
        // 测试查找功能
        System.out.println("\n=== 查找功能测试 ===");
        String[] testKeys = {"CN", "US", "JP", "XX", "YY"};
        for (String key : testKeys) {
            Record record = index.findByKey(key);
            if (record != null) {
                System.out.println("找到记录: " + record);
            } else {
                System.out.println("未找到键: " + key);
            }
        }
        
        // 性能测试
        List<String> testKeyList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            testKeyList.add("CN"); // 重复查找同一个键
        }
        
        PerformanceTest.testPerfectHashIndex(index, testKeyList);
        PerformanceTest.compareWithHashMap(records, testKeyList);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度:");
        System.out.println("- 构建索引: O(n^2) 最坏情况，实际中通常为 O(n)");
        System.out.println("- 查找记录: O(1) 最坏情况");
        System.out.println("空间复杂度: O(n)");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 配置表索引：系统配置、参数设置等静态数据");
        System.out.println("2. 字典表索引：国家、地区、货币等字典数据");
        System.out.println("3. 权限表索引：用户角色、权限映射等相对静态数据");
        System.out.println("4. 编译器符号表：变量、函数名等编译时确定的数据");
        
        System.out.println("\n=== 选择策略指南 ===");
        System.out.println("1. 数据静态性：仅适用于数据不频繁变化的场景");
        System.out.println("2. 查询频繁性：适用于查询远多于更新的场景");
        System.out.println("3. 内存敏感性：完美哈希通常比HashMap占用更少内存");
        System.out.println("4. 构建成本：构建过程较复杂，但查询性能极优");
    }
}