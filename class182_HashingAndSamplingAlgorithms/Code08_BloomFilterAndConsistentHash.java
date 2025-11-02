package class107;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 布隆过滤器与一致性哈希算法实现
 * 
 * 本文件包含高级哈希算法的实现，包括：
 * - 布隆过滤器 (Bloom Filter)
 * - 计数布隆过滤器 (Counting Bloom Filter)
 * - 一致性哈希 (Consistent Hashing)
 * - 虚拟节点技术 (Virtual Nodes)
 * - 分布式哈希表 (Distributed Hash Table)
 * 
 * 这些算法在大数据、分布式系统、缓存系统等领域有重要应用
 */

public class Code08_BloomFilterAndConsistentHash {
    
    /**
     * 布隆过滤器实现
     * 应用场景：缓存系统、垃圾邮件过滤、URL去重
     * 
     * 算法原理：
     * 1. 使用多个哈希函数将元素映射到位数组的不同位置
     * 2. 查询时检查所有对应位置是否都为1
     * 3. 存在假阳性（可能误判存在），但不存在假阴性
     * 
     * 时间复杂度：O(k)，k为哈希函数数量
     * 空间复杂度：O(m)，m为位数组大小
     */
    public static class BloomFilter {
        private final boolean[] bitArray;
        private final int size;
        private final int hashFunctions;
        private final Random random;
        
        public BloomFilter(int expectedElements, double falsePositiveRate) {
            // 计算最优位数组大小和哈希函数数量
            this.size = optimalSize(expectedElements, falsePositiveRate);
            this.hashFunctions = optimalHashFunctions(expectedElements, size);
            this.bitArray = new boolean[size];
            this.random = new Random();
        }
        
        /**
         * 计算最优位数组大小
         */
        private int optimalSize(int n, double p) {
            return (int) Math.ceil(-(n * Math.log(p)) / Math.pow(Math.log(2), 2));
        }
        
        /**
         * 计算最优哈希函数数量
         */
        private int optimalHashFunctions(int n, int m) {
            return (int) Math.ceil((m / (double) n) * Math.log(2));
        }
        
        /**
         * 添加元素
         */
        public void add(String element) {
            for (int i = 0; i < hashFunctions; i++) {
                int hash = hash(element, i);
                bitArray[hash % size] = true;
            }
        }
        
        /**
         * 检查元素是否存在
         */
        public boolean contains(String element) {
            for (int i = 0; i < hashFunctions; i++) {
                int hash = hash(element, i);
                if (!bitArray[hash % size]) {
                    return false;
                }
            }
            return true;
        }
        
        /**
         * 哈希函数
         */
        private int hash(String element, int seed) {
            random.setSeed(seed);
            int hash = element.hashCode();
            hash ^= random.nextInt();
            return Math.abs(hash);
        }
        
        /**
         * 获取假阳性概率
         */
        public double getFalsePositiveProbability(int insertedElements) {
            return Math.pow(1 - Math.exp(-hashFunctions * insertedElements / (double) size), hashFunctions);
        }
        
        /**
         * 获取位数组使用率
         */
        public double getBitUsage() {
            int used = 0;
            for (boolean bit : bitArray) {
                if (bit) used++;
            }
            return used / (double) size;
        }
    }
    
    /**
     * 计数布隆过滤器实现
     * 应用场景：需要支持删除操作的布隆过滤器变种
     * 
     * 算法原理：
     * 1. 使用计数器数组代替位数组
     * 2. 添加时递增计数器，删除时递减计数器
     * 3. 查询时检查所有对应位置的计数器是否大于0
     * 
     * 时间复杂度：O(k)
     * 空间复杂度：O(m * log2(maxCount))
     */
    public static class CountingBloomFilter {
        private final int[] counterArray;
        private final int size;
        private final int hashFunctions;
        private final Random random;
        private int elementCount;
        
        public CountingBloomFilter(int expectedElements, double falsePositiveRate) {
            this.size = optimalSize(expectedElements, falsePositiveRate);
            this.hashFunctions = optimalHashFunctions(expectedElements, size);
            this.counterArray = new int[size];
            this.random = new Random();
            this.elementCount = 0;
        }
        
        private int optimalSize(int n, double p) {
            return (int) Math.ceil(-(n * Math.log(p)) / Math.pow(Math.log(2), 2));
        }
        
        private int optimalHashFunctions(int n, int m) {
            return (int) Math.ceil((m / (double) n) * Math.log(2));
        }
        
        /**
         * 添加元素
         */
        public void add(String element) {
            for (int i = 0; i < hashFunctions; i++) {
                int hash = hash(element, i);
                counterArray[hash % size]++;
            }
            elementCount++;
        }
        
        /**
         * 删除元素
         */
        public boolean remove(String element) {
            if (!contains(element)) {
                return false;
            }
            
            for (int i = 0; i < hashFunctions; i++) {
                int hash = hash(element, i);
                counterArray[hash % size]--;
            }
            elementCount--;
            return true;
        }
        
        /**
         * 检查元素是否存在
         */
        public boolean contains(String element) {
            for (int i = 0; i < hashFunctions; i++) {
                int hash = hash(element, i);
                if (counterArray[hash % size] <= 0) {
                    return false;
                }
            }
            return true;
        }
        
        private int hash(String element, int seed) {
            random.setSeed(seed);
            int hash = element.hashCode();
            hash ^= random.nextInt();
            return Math.abs(hash);
        }
        
        /**
         * 获取元素数量估计
         */
        public int getEstimatedSize() {
            return elementCount;
        }
        
        /**
         * 获取最大计数器值
         */
        public int getMaxCounterValue() {
            int max = 0;
            for (int count : counterArray) {
                if (count > max) max = count;
            }
            return max;
        }
    }
    
    /**
     * 一致性哈希算法实现
     * 应用场景：分布式缓存、负载均衡、分布式存储
     * 
     * 算法原理：
     * 1. 将哈希空间组织成环状结构
     * 2. 节点和数据都映射到环上的位置
     * 3. 数据存储在顺时针方向的下一个节点上
     * 4. 使用虚拟节点实现负载均衡
     * 
     * 时间复杂度：O(log n) 查找，O(1) 添加/删除节点
     * 空间复杂度：O(n + v)，n为节点数，v为虚拟节点数
     */
    public static class ConsistentHash {
        private final TreeMap<Integer, String> circle;
        private final Map<String, List<Integer>> virtualNodes;
        private final int virtualNodeCount;
        private final int hashSpace;
        
        public ConsistentHash(int virtualNodeCount, int hashSpace) {
            this.circle = new TreeMap<>();
            this.virtualNodes = new HashMap<>();
            this.virtualNodeCount = virtualNodeCount;
            this.hashSpace = hashSpace;
        }
        
        /**
         * 添加节点
         */
        public void addNode(String node) {
            List<Integer> nodeHashes = new ArrayList<>();
            
            for (int i = 0; i < virtualNodeCount; i++) {
                String virtualNode = node + "#" + i;
                int hash = hash(virtualNode) % hashSpace;
                circle.put(hash, node);
                nodeHashes.add(hash);
            }
            
            virtualNodes.put(node, nodeHashes);
        }
        
        /**
         * 删除节点
         */
        public void removeNode(String node) {
            List<Integer> nodeHashes = virtualNodes.remove(node);
            if (nodeHashes != null) {
                for (int hash : nodeHashes) {
                    circle.remove(hash);
                }
            }
        }
        
        /**
         * 获取数据应该存储的节点
         */
        public String getNode(String key) {
            if (circle.isEmpty()) {
                return null;
            }
            
            int hash = hash(key) % hashSpace;
            Map.Entry<Integer, String> entry = circle.ceilingEntry(hash);
            
            if (entry == null) {
                // 回到环的开头
                entry = circle.firstEntry();
            }
            
            return entry.getValue();
        }
        
        /**
         * 获取所有节点
         */
        public Set<String> getNodes() {
            return virtualNodes.keySet();
        }
        
        /**
         * 获取节点负载分布
         */
        public Map<String, Integer> getLoadDistribution() {
            Map<String, Integer> distribution = new HashMap<>();
            
            for (String node : virtualNodes.keySet()) {
                distribution.put(node, virtualNodes.get(node).size());
            }
            
            return distribution;
        }
        
        /**
         * 哈希函数
         */
        private int hash(String key) {
            return Math.abs(key.hashCode());
        }
        
        /**
         * 数据迁移分析（当节点变化时）
         */
        public Map<String, Set<String>> analyzeDataMigration(Set<String> keys, String newNode) {
            Map<String, Set<String>> migration = new HashMap<>();
            
            // 添加新节点前的映射
            Map<String, String> oldMapping = new HashMap<>();
            for (String key : keys) {
                oldMapping.put(key, getNode(key));
            }
            
            // 添加新节点
            addNode(newNode);
            
            // 分析迁移数据
            for (String key : keys) {
                String newNodeForKey = getNode(key);
                String oldNodeForKey = oldMapping.get(key);
                
                if (!newNodeForKey.equals(oldNodeForKey)) {
                    migration.computeIfAbsent(oldNodeForKey, k -> new HashSet<>()).add(key);
                }
            }
            
            // 恢复原状
            removeNode(newNode);
            
            return migration;
        }
    }
    
    /**
     * 分布式哈希表实现
     * 应用场景：P2P网络、分布式存储系统
     * 
     * 算法原理：
     * 1. 使用一致性哈希进行节点定位
     * 2. 支持数据的存储、检索和删除
     * 3. 处理节点加入和离开的数据迁移
     * 
     * 时间复杂度：O(log n) 查找
     * 空间复杂度：O(n)
     */
    public static class DistributedHashTable {
        private final ConsistentHash consistentHash;
        private final Map<String, Map<String, Object>> nodeData;
        private final ReentrantReadWriteLock lock;
        
        public DistributedHashTable(int virtualNodeCount, int hashSpace) {
            this.consistentHash = new ConsistentHash(virtualNodeCount, hashSpace);
            this.nodeData = new HashMap<>();
            this.lock = new ReentrantReadWriteLock();
        }
        
        /**
         * 添加节点
         */
        public void addNode(String node) {
            lock.writeLock().lock();
            try {
                consistentHash.addNode(node);
                nodeData.put(node, new HashMap<>());
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 删除节点
         */
        public void removeNode(String node) {
            lock.writeLock().lock();
            try {
                // 迁移数据到其他节点
                Map<String, Object> dataToMigrate = nodeData.get(node);
                if (dataToMigrate != null) {
                    for (Map.Entry<String, Object> entry : dataToMigrate.entrySet()) {
                        String key = entry.getKey();
                        Object value = entry.getValue();
                        String newNode = consistentHash.getNode(key);
                        if (newNode != null && !newNode.equals(node)) {
                            nodeData.get(newNode).put(key, value);
                        }
                    }
                }
                
                consistentHash.removeNode(node);
                nodeData.remove(node);
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        /**
         * 存储数据
         */
        public void put(String key, Object value) {
            lock.readLock().lock();
            try {
                String node = consistentHash.getNode(key);
                if (node != null) {
                    nodeData.get(node).put(key, value);
                }
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 检索数据
         */
        public Object get(String key) {
            lock.readLock().lock();
            try {
                String node = consistentHash.getNode(key);
                if (node != null) {
                    return nodeData.get(node).get(key);
                }
                return null;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 删除数据
         */
        public boolean remove(String key) {
            lock.readLock().lock();
            try {
                String node = consistentHash.getNode(key);
                if (node != null) {
                    return nodeData.get(node).remove(key) != null;
                }
                return false;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 获取系统状态
         */
        public Map<String, Object> getSystemStatus() {
            lock.readLock().lock();
            try {
                Map<String, Object> status = new HashMap<>();
                status.put("nodes", consistentHash.getNodes());
                status.put("loadDistribution", consistentHash.getLoadDistribution());
                
                Map<String, Integer> dataSize = new HashMap<>();
                for (String node : nodeData.keySet()) {
                    dataSize.put(node, nodeData.get(node).size());
                }
                status.put("dataSize", dataSize);
                
                return status;
            } finally {
                lock.readLock().unlock();
            }
        }
        
        /**
         * 数据备份策略
         */
        public void replicateData(String key, int replicationFactor) {
            lock.writeLock().lock();
            try {
                String primaryNode = consistentHash.getNode(key);
                Object value = nodeData.get(primaryNode).get(key);
                
                if (value != null) {
                    // 在后续节点上创建副本
                    Set<String> replicaNodes = new HashSet<>();
                    int hash = hash(key) % consistentHash.hashSpace;
                    
                    // 找到后续的replicationFactor个节点
                    NavigableMap<Integer, String> tailMap = consistentHash.circle.tailMap(hash, false);
                    Iterator<Map.Entry<Integer, String>> iterator = tailMap.entrySet().iterator();
                    
                    while (replicaNodes.size() < replicationFactor && iterator.hasNext()) {
                        Map.Entry<Integer, String> entry = iterator.next();
                        if (!entry.getValue().equals(primaryNode)) {
                            replicaNodes.add(entry.getValue());
                        }
                    }
                    
                    // 如果不够，从头开始找
                    if (replicaNodes.size() < replicationFactor) {
                        iterator = consistentHash.circle.entrySet().iterator();
                        while (replicaNodes.size() < replicationFactor && iterator.hasNext()) {
                            Map.Entry<Integer, String> entry = iterator.next();
                            if (!entry.getValue().equals(primaryNode) && !replicaNodes.contains(entry.getValue())) {
                                replicaNodes.add(entry.getValue());
                            }
                        }
                    }
                    
                    // 存储副本
                    for (String replicaNode : replicaNodes) {
                        nodeData.get(replicaNode).put(key + "_replica", value);
                    }
                }
            } finally {
                lock.writeLock().unlock();
            }
        }
        
        private int hash(String key) {
            return Math.abs(key.hashCode());
        }
    }
    
    /**
     * 性能测试和分析工具
     */
    public static class PerformanceAnalyzer {
        
        /**
         * 测试布隆过滤器性能
         */
        public static void testBloomFilter(int elementCount, double falsePositiveRate) {
            System.out.println("=== 布隆过滤器性能测试 ===");
            
            BloomFilter bf = new BloomFilter(elementCount, falsePositiveRate);
            
            // 添加元素
            long startTime = System.nanoTime();
            for (int i = 0; i < elementCount; i++) {
                bf.add("element" + i);
            }
            long addTime = System.nanoTime() - startTime;
            
            // 查询元素
            startTime = System.nanoTime();
            for (int i = 0; i < elementCount; i++) {
                bf.contains("element" + i);
            }
            long queryTime = System.nanoTime() - startTime;
            
            // 测试假阳性
            int falsePositives = 0;
            int testCount = 10000;
            for (int i = elementCount; i < elementCount + testCount; i++) {
                if (bf.contains("element" + i)) {
                    falsePositives++;
                }
            }
            
            System.out.printf("元素数量: %,d\n", elementCount);
            System.out.printf("目标假阳性率: %.4f%%\n", falsePositiveRate * 100);
            System.out.printf("实际假阳性率: %.4f%%\n", (falsePositives / (double) testCount) * 100);
            System.out.printf("位数组使用率: %.2f%%\n", bf.getBitUsage() * 100);
            System.out.printf("添加时间: %,d ns\n", addTime / elementCount);
            System.out.printf("查询时间: %,d ns\n", queryTime / elementCount);
        }
        
        /**
         * 测试一致性哈希负载均衡
         */
        public static void testConsistentHashLoadBalance(int nodeCount, int virtualNodeCount, int keyCount) {
            System.out.println("\n=== 一致性哈希负载均衡测试 ===");
            
            ConsistentHash ch = new ConsistentHash(virtualNodeCount, 1000000);
            
            // 添加节点
            for (int i = 0; i < nodeCount; i++) {
                ch.addNode("node" + i);
            }
            
            // 模拟数据分布
            Map<String, Integer> keyDistribution = new HashMap<>();
            for (int i = 0; i < keyCount; i++) {
                String node = ch.getNode("key" + i);
                keyDistribution.put(node, keyDistribution.getOrDefault(node, 0) + 1);
            }
            
            // 分析负载均衡
            int minLoad = Integer.MAX_VALUE;
            int maxLoad = Integer.MIN_VALUE;
            int totalLoad = 0;
            
            for (int load : keyDistribution.values()) {
                minLoad = Math.min(minLoad, load);
                maxLoad = Math.max(maxLoad, load);
                totalLoad += load;
            }
            
            double avgLoad = totalLoad / (double) nodeCount;
            double imbalance = (maxLoad - minLoad) / avgLoad * 100;
            
            System.out.printf("节点数量: %d\n", nodeCount);
            System.out.printf("虚拟节点数量: %d\n", virtualNodeCount);
            System.out.printf("数据总量: %,d\n", keyCount);
            System.out.printf("最小负载: %,d\n", minLoad);
            System.out.printf("最大负载: %,d\n", maxLoad);
            System.out.printf("平均负载: %.1f\n", avgLoad);
            System.out.printf("负载不均衡度: %.2f%%\n", imbalance);
            
            // 显示详细分布
            System.out.println("\n详细负载分布:");
            for (Map.Entry<String, Integer> entry : keyDistribution.entrySet()) {
                System.out.printf("  %s: %,d 数据 (%.1f%%)\n", 
                    entry.getKey(), entry.getValue(), 
                    entry.getValue() / (double) keyCount * 100);
            }
        }
        
        /**
         * 测试节点变化的数据迁移
         */
        public static void testDataMigration(int initialNodes, int keyCount) {
            System.out.println("\n=== 数据迁移测试 ===");
            
            ConsistentHash ch = new ConsistentHash(100, 1000000);
            
            // 初始节点
            for (int i = 0; i < initialNodes; i++) {
                ch.addNode("node" + i);
            }
            
            // 生成测试键
            Set<String> keys = new HashSet<>();
            for (int i = 0; i < keyCount; i++) {
                keys.add("key" + i);
            }
            
            // 添加新节点前的映射
            Map<String, String> oldMapping = new HashMap<>();
            for (String key : keys) {
                oldMapping.put(key, ch.getNode(key));
            }
            
            // 添加新节点
            ch.addNode("newNode");
            
            // 分析迁移
            int migrated = 0;
            Map<String, Integer> migrationByNode = new HashMap<>();
            
            for (String key : keys) {
                String newNode = ch.getNode(key);
                String oldNode = oldMapping.get(key);
                
                if (!newNode.equals(oldNode)) {
                    migrated++;
                    migrationByNode.put(oldNode, migrationByNode.getOrDefault(oldNode, 0) + 1);
                }
            }
            
            System.out.printf("初始节点数: %d\n", initialNodes);
            System.out.printf("数据总量: %,d\n", keyCount);
            System.out.printf("迁移数据量: %,d (%.2f%%)\n", migrated, (migrated / (double) keyCount) * 100);
            
            System.out.println("\n各节点迁移情况:");
            for (Map.Entry<String, Integer> entry : migrationByNode.entrySet()) {
                System.out.printf("  %s: %,d 数据迁移\n", entry.getKey(), entry.getValue());
            }
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 布隆过滤器与一致性哈希算法测试 ===\n");
        
        // 测试布隆过滤器
        System.out.println("1. 布隆过滤器测试:");
        BloomFilter bf = new BloomFilter(10000, 0.01);
        bf.add("hello");
        bf.add("world");
        System.out.println("包含 'hello': " + bf.contains("hello"));
        System.out.println("包含 'test': " + bf.contains("test"));
        System.out.println("假阳性概率: " + bf.getFalsePositiveProbability(2));
        
        // 测试计数布隆过滤器
        System.out.println("\n2. 计数布隆过滤器测试:");
        CountingBloomFilter cbf = new CountingBloomFilter(10000, 0.01);
        cbf.add("apple");
        cbf.add("banana");
        cbf.add("apple"); // 重复添加
        System.out.println("包含 'apple': " + cbf.contains("apple"));
        cbf.remove("apple");
        System.out.println("删除后包含 'apple': " + cbf.contains("apple"));
        
        // 测试一致性哈希
        System.out.println("\n3. 一致性哈希测试:");
        ConsistentHash ch = new ConsistentHash(100, 1000000);
        ch.addNode("node1");
        ch.addNode("node2");
        ch.addNode("node3");
        
        System.out.println("'key1' 分配到: " + ch.getNode("key1"));
        System.out.println("'key2' 分配到: " + ch.getNode("key2"));
        System.out.println("负载分布: " + ch.getLoadDistribution());
        
        // 测试分布式哈希表
        System.out.println("\n4. 分布式哈希表测试:");
        DistributedHashTable dht = new DistributedHashTable(100, 1000000);
        dht.addNode("nodeA");
        dht.addNode("nodeB");
        dht.put("user1", "Alice");
        dht.put("user2", "Bob");
        
        System.out.println("user1: " + dht.get("user1"));
        System.out.println("系统状态: " + dht.getSystemStatus());
        
        // 性能测试
        System.out.println("\n5. 性能测试:");
        PerformanceAnalyzer.testBloomFilter(100000, 0.01);
        PerformanceAnalyzer.testConsistentHashLoadBalance(5, 100, 10000);
        PerformanceAnalyzer.testDataMigration(5, 10000);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. 布隆过滤器: O(k)时间，O(m)空间，k为哈希函数数，m为位数组大小");
        System.out.println("2. 计数布隆过滤器: O(k)时间，O(m*log(maxCount))空间");
        System.out.println("3. 一致性哈希: O(log n)查找，O(1)添加/删除，O(n+v)空间");
        System.out.println("4. 分布式哈希表: O(log n)操作，O(n)空间");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 缓存系统: Redis、Memcached使用布隆过滤器进行键存在性检查");
        System.out.println("2. 分布式存储: Cassandra、DynamoDB使用一致性哈希进行数据分片");
        System.out.println("3. 负载均衡: Nginx、HAProxy使用一致性哈希进行请求路由");
        System.out.println("4. 垃圾邮件过滤: 使用布隆过滤器快速判断邮件是否垃圾邮件");
        
        System.out.println("\n=== 系统设计考量 ===");
        System.out.println("1. 容错性: 处理节点故障和数据恢复");
        System.out.println("2. 可扩展性: 支持动态添加和删除节点");
        System.out.println("3. 一致性: 保证数据在不同副本间的一致性");
        System.out.println("4. 性能: 优化哈希函数选择和参数配置");
    }
}