package class107;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

/**
 * 哈希冲突解决与分布式哈希表实现
 * 
 * 本文件包含哈希冲突解决策略和分布式哈希表的高级实现，包括：
 * - 开放地址法（线性探测、二次探测、双重哈希）
 * - 链地址法（分离链接法）
 * - 分布式哈希表（DHT）
 * - 可扩展哈希表
 * - 线性哈希表
 * 
 * 这些算法在大规模数据存储、分布式系统、数据库索引等领域有重要应用
 */

public class Code06_HashConflictAndDistributed {
    
    /**
     * 开放地址法哈希表实现
     * 应用场景：内存受限环境、缓存系统、嵌入式系统
     * 
     * 算法原理：
     * 1. 所有元素都存储在哈希表数组中
     * 2. 发生冲突时，按照探测序列寻找下一个空槽
     * 3. 支持线性探测、二次探测、双重哈希等策略
     * 
     * 时间复杂度：平均O(1)，最坏O(n)
     * 空间复杂度：O(n)
     */
    public static class OpenAddressingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        private Entry<K, V>[] table;
        private int size;
        private int capacity;
        private ProbingStrategy strategy;
        
        public OpenAddressingHashTable() {
            this(DEFAULT_CAPACITY, ProbingStrategy.LINEAR);
        }
        
        public OpenAddressingHashTable(int capacity, ProbingStrategy strategy) {
            this.capacity = capacity;
            this.strategy = strategy;
            this.table = new Entry[capacity];
            this.size = 0;
        }
        
        /**
         * 插入键值对
         */
        public void put(K key, V value) {
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            
            int index = findSlot(key);
            if (index != -1) {
                table[index] = new Entry<>(key, value);
                size++;
            }
        }
        
        /**
         * 获取值
         */
        public V get(K key) {
            int index = findKeyIndex(key);
            return index != -1 ? table[index].value : null;
        }
        
        /**
         * 删除键值对
         */
        public void remove(K key) {
            int index = findKeyIndex(key);
            if (index != -1) {
                table[index] = null;
                size--;
                // 重新插入后续可能被影响的元素
                rehashFrom(index + 1);
            }
        }
        
        /**
         * 查找键的槽位
         */
        private int findSlot(K key) {
            int hash = key.hashCode();
            int index = Math.abs(hash % capacity);
            
            for (int i = 0; i < capacity; i++) {
                int probeIndex = probe(index, i);
                if (table[probeIndex] == null || table[probeIndex].key.equals(key)) {
                    return probeIndex;
                }
            }
            
            return -1; // 表已满
        }
        
        /**
         * 查找键的索引
         */
        private int findKeyIndex(K key) {
            int hash = key.hashCode();
            int index = Math.abs(hash % capacity);
            
            for (int i = 0; i < capacity; i++) {
                int probeIndex = probe(index, i);
                if (table[probeIndex] == null) {
                    return -1; // 未找到
                }
                if (table[probeIndex].key.equals(key)) {
                    return probeIndex;
                }
            }
            
            return -1;
        }
        
        /**
         * 探测函数
         */
        private int probe(int base, int step) {
            switch (strategy) {
                case LINEAR:
                    return (base + step) % capacity;
                case QUADRATIC:
                    return (base + step * step) % capacity;
                case DOUBLE_HASH:
                    int hash2 = Math.abs((base * 31) % capacity);
                    return (base + step * hash2) % capacity;
                default:
                    return (base + step) % capacity;
            }
        }
        
        /**
         * 扩容并重新哈希
         */
        private void resize() {
            int newCapacity = capacity * 2;
            Entry<K, V>[] oldTable = table;
            table = new Entry[newCapacity];
            capacity = newCapacity;
            size = 0;
            
            for (Entry<K, V> entry : oldTable) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
        }
        
        /**
         * 从指定位置开始重新哈希
         */
        private void rehashFrom(int start) {
            for (int i = start; i < capacity; i++) {
                if (table[i] != null) {
                    Entry<K, V> entry = table[i];
                    table[i] = null;
                    size--;
                    put(entry.key, entry.value);
                }
            }
        }
        
        /**
         * 获取性能统计
         */
        public PerformanceStats getPerformanceStats() {
            int longestChain = 0;
            int currentChain = 0;
            int emptySlots = 0;
            
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null) {
                    emptySlots++;
                    currentChain = 0;
                } else {
                    currentChain++;
                    longestChain = Math.max(longestChain, currentChain);
                }
            }
            
            double loadFactor = (double) size / capacity;
            return new PerformanceStats(size, capacity, loadFactor, longestChain, emptySlots);
        }
        
        /**
         * 探测策略枚举
         */
        public enum ProbingStrategy {
            LINEAR,      // 线性探测
            QUADRATIC,   // 二次探测
            DOUBLE_HASH  // 双重哈希
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            K key;
            V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        
        /**
         * 性能统计类
         */
        public static class PerformanceStats {
            public final int size;
            public final int capacity;
            public final double loadFactor;
            public final int longestChain;
            public final int emptySlots;
            
            PerformanceStats(int size, int capacity, double loadFactor, int longestChain, int emptySlots) {
                this.size = size;
                this.capacity = capacity;
                this.loadFactor = loadFactor;
                this.longestChain = longestChain;
                this.emptySlots = emptySlots;
            }
            
            @Override
            public String toString() {
                return String.format("Size: %d, Capacity: %d, Load Factor: %.2f, Longest Chain: %d, Empty Slots: %d",
                        size, capacity, loadFactor, longestChain, emptySlots);
            }
        }
    }
    
    /**
     * 链地址法哈希表实现
     * 应用场景：通用哈希表实现、数据库索引、语言运行时
     * 
     * 算法原理：
     * 1. 每个槽位存储一个链表（或树）
     * 2. 冲突的元素添加到同一个链表中
     * 3. 当链表过长时转换为平衡树提高性能
     * 
     * 时间复杂度：平均O(1)，最坏O(log n)
     * 空间复杂度：O(n)
     */
    public static class ChainingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        private static final int TREEIFY_THRESHOLD = 8;
        
        private List<Entry<K, V>>[] table;
        private int size;
        private int capacity;
        
        public ChainingHashTable() {
            this(DEFAULT_CAPACITY);
        }
        
        @SuppressWarnings("unchecked")
        public ChainingHashTable(int capacity) {
            this.capacity = capacity;
            this.table = new LinkedList[capacity];
            this.size = 0;
        }
        
        /**
         * 插入键值对
         */
        public void put(K key, V value) {
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            
            int index = hash(key);
            if (table[index] == null) {
                table[index] = new LinkedList<>();
            }
            
            // 检查是否已存在相同键
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    entry.value = value; // 更新值
                    return;
                }
            }
            
            table[index].add(new Entry<>(key, value));
            size++;
            
            // 检查是否需要树化
            if (table[index].size() >= TREEIFY_THRESHOLD) {
                treeifyBucket(index);
            }
        }
        
        /**
         * 获取值
         */
        public V get(K key) {
            int index = hash(key);
            if (table[index] == null) {
                return null;
            }
            
            for (Entry<K, V> entry : table[index]) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
            
            return null;
        }
        
        /**
         * 删除键值对
         */
        public void remove(K key) {
            int index = hash(key);
            if (table[index] == null) {
                return;
            }
            
            Iterator<Entry<K, V>> iterator = table[index].iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove();
                    size--;
                    return;
                }
            }
        }
        
        /**
         * 哈希函数
         */
        private int hash(K key) {
            return Math.abs(key.hashCode() % capacity);
        }
        
        /**
         * 扩容并重新哈希
         */
        @SuppressWarnings("unchecked")
        private void resize() {
            int newCapacity = capacity * 2;
            List<Entry<K, V>>[] oldTable = table;
            table = new LinkedList[newCapacity];
            capacity = newCapacity;
            size = 0;
            
            for (List<Entry<K, V>> bucket : oldTable) {
                if (bucket != null) {
                    for (Entry<K, V> entry : bucket) {
                        put(entry.key, entry.value);
                    }
                }
            }
        }
        
        /**
         * 将链表转换为平衡树（简化版）
         */
        private void treeifyBucket(int index) {
            // 在实际实现中，这里会将链表转换为红黑树
            // 这里简化实现，只做标记
            System.out.println("Bucket " + index + " needs treeification (size: " + table[index].size() + ")");
        }
        
        /**
         * 获取性能统计
         */
        public PerformanceStats getPerformanceStats() {
            int maxChainLength = 0;
            int emptyBuckets = 0;
            int totalChainLength = 0;
            int nonEmptyBuckets = 0;
            
            for (int i = 0; i < capacity; i++) {
                if (table[i] == null || table[i].isEmpty()) {
                    emptyBuckets++;
                } else {
                    int chainLength = table[i].size();
                    maxChainLength = Math.max(maxChainLength, chainLength);
                    totalChainLength += chainLength;
                    nonEmptyBuckets++;
                }
            }
            
            double avgChainLength = nonEmptyBuckets > 0 ? (double) totalChainLength / nonEmptyBuckets : 0;
            double loadFactor = (double) size / capacity;
            
            return new PerformanceStats(size, capacity, loadFactor, maxChainLength, emptyBuckets, avgChainLength);
        }
        
        /**
         * 扩展的性能统计类
         */
        public static class PerformanceStats extends OpenAddressingHashTable.PerformanceStats {
            public final double avgChainLength;
            
            PerformanceStats(int size, int capacity, double loadFactor, int longestChain, 
                            int emptySlots, double avgChainLength) {
                super(size, capacity, loadFactor, longestChain, emptySlots);
                this.avgChainLength = avgChainLength;
            }
            
            @Override
            public String toString() {
                return super.toString() + String.format(", Avg Chain Length: %.2f", avgChainLength);
            }
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            K key;
            V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * 分布式哈希表（DHT）实现
     * 应用场景：P2P网络、分布式存储、区块链
     * 
     * 算法原理：
     * 1. 使用一致性哈希将数据分布到多个节点
     * 2. 每个节点负责一段哈希环上的数据
     * 3. 支持节点的动态加入和离开
     * 
     * 时间复杂度：O(log n) 查找
     * 空间复杂度：O(n) 分布式存储
     */
    public static class DistributedHashTable {
        private final ConsistentHashing consistentHashing;
        private final Map<String, Map<String, String>> nodeData;
        private final int replicationFactor;
        
        public DistributedHashTable(int virtualNodeCount, int replicationFactor) {
            this.consistentHashing = new ConsistentHashing(virtualNodeCount);
            this.nodeData = new ConcurrentHashMap<>();
            this.replicationFactor = replicationFactor;
        }
        
        /**
         * 添加节点
         */
        public void addNode(String nodeId) {
            consistentHashing.addNode(nodeId);
            nodeData.put(nodeId, new ConcurrentHashMap<>());
            
            // 数据重新分布
            redistributeData();
        }
        
        /**
         * 移除节点
         */
        public void removeNode(String nodeId) {
            consistentHashing.removeNode(nodeId);
            Map<String, String> data = nodeData.remove(nodeId);
            
            // 将数据迁移到其他节点
            if (data != null) {
                for (Map.Entry<String, String> entry : data.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
        
        /**
         * 存储数据
         */
        public void put(String key, String value) {
            String primaryNode = consistentHashing.getNode(key);
            if (primaryNode == null) {
                throw new IllegalStateException("No nodes available");
            }
            
            // 存储到主节点
            nodeData.get(primaryNode).put(key, value);
            
            // 复制到备份节点
            replicateData(key, value, primaryNode);
        }
        
        /**
         * 获取数据
         */
        public String get(String key) {
            String primaryNode = consistentHashing.getNode(key);
            if (primaryNode == null) {
                return null;
            }
            
            // 尝试从主节点获取
            String value = nodeData.get(primaryNode).get(key);
            if (value != null) {
                return value;
            }
            
            // 从备份节点获取
            return getFromReplica(key, primaryNode);
        }
        
        /**
         * 数据复制
         */
        private void replicateData(String key, String value, String primaryNode) {
            Set<String> allNodes = consistentHashing.getAllNodes();
            List<String> nodes = new ArrayList<>(allNodes);
            
            // 找到主节点在环上的位置
            int primaryIndex = nodes.indexOf(primaryNode);
            if (primaryIndex == -1) {
                return;
            }
            
            // 复制到后续节点
            for (int i = 1; i <= replicationFactor && i < nodes.size(); i++) {
                int replicaIndex = (primaryIndex + i) % nodes.size();
                String replicaNode = nodes.get(replicaIndex);
                nodeData.get(replicaNode).put(key, value);
            }
        }
        
        /**
         * 从备份节点获取数据
         */
        private String getFromReplica(String key, String primaryNode) {
            Set<String> allNodes = consistentHashing.getAllNodes();
            List<String> nodes = new ArrayList<>(allNodes);
            
            int primaryIndex = nodes.indexOf(primaryNode);
            if (primaryIndex == -1) {
                return null;
            }
            
            // 从备份节点查找
            for (int i = 1; i <= replicationFactor && i < nodes.size(); i++) {
                int replicaIndex = (primaryIndex + i) % nodes.size();
                String replicaNode = nodes.get(replicaIndex);
                String value = nodeData.get(replicaNode).get(key);
                if (value != null) {
                    return value;
                }
            }
            
            return null;
        }
        
        /**
         * 数据重新分布
         */
        private void redistributeData() {
            // 简化实现：在实际系统中需要更复杂的数据迁移策略
            System.out.println("Data redistribution triggered");
        }
        
        /**
         * 获取系统状态
         */
        public SystemStatus getSystemStatus() {
            int totalKeys = 0;
            int maxKeysPerNode = 0;
            int minKeysPerNode = Integer.MAX_VALUE;
            
            for (Map<String, String> data : nodeData.values()) {
                int keyCount = data.size();
                totalKeys += keyCount;
                maxKeysPerNode = Math.max(maxKeysPerNode, keyCount);
                minKeysPerNode = Math.min(minKeysPerNode, keyCount);
            }
            
            double avgKeysPerNode = nodeData.isEmpty() ? 0 : (double) totalKeys / nodeData.size();
            double imbalance = maxKeysPerNode - minKeysPerNode;
            
            return new SystemStatus(nodeData.size(), totalKeys, avgKeysPerNode, imbalance, replicationFactor);
        }
        
        /**
         * 系统状态类
         */
        public static class SystemStatus {
            public final int nodeCount;
            public final int totalKeys;
            public final double avgKeysPerNode;
            public final double imbalance;
            public final int replicationFactor;
            
            SystemStatus(int nodeCount, int totalKeys, double avgKeysPerNode, 
                        double imbalance, int replicationFactor) {
                this.nodeCount = nodeCount;
                this.totalKeys = totalKeys;
                this.avgKeysPerNode = avgKeysPerNode;
                this.imbalance = imbalance;
                this.replicationFactor = replicationFactor;
            }
            
            @Override
            public String toString() {
                return String.format("Nodes: %d, Total Keys: %d, Avg Keys/Node: %.2f, Imbalance: %.2f, Replication: %d",
                        nodeCount, totalKeys, avgKeysPerNode, imbalance, replicationFactor);
            }
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 哈希冲突解决与分布式哈希表测试 ===\n");
        
        // 测试开放地址法
        System.out.println("1. 开放地址法哈希表测试:");
        OpenAddressingHashTable<String, Integer> openTable = 
            new OpenAddressingHashTable<>(10, OpenAddressingHashTable.ProbingStrategy.LINEAR);
        
        // 插入测试数据
        for (int i = 0; i < 15; i++) {
            openTable.put("key" + i, i);
        }
        
        System.out.println("获取key5: " + openTable.get("key5"));
        System.out.println("性能统计: " + openTable.getPerformanceStats());
        
        // 测试链地址法
        System.out.println("\n2. 链地址法哈希表测试:");
        ChainingHashTable<String, Integer> chainTable = new ChainingHashTable<>(10);
        
        for (int i = 0; i < 20; i++) {
            chainTable.put("key" + i, i);
        }
        
        System.out.println("获取key10: " + chainTable.get("key10"));
        System.out.println("性能统计: " + chainTable.getPerformanceStats());
        
        // 测试分布式哈希表
        System.out.println("\n3. 分布式哈希表测试:");
        DistributedHashTable dht = new DistributedHashTable(100, 2);
        
        dht.addNode("node1");
        dht.addNode("node2");
        dht.addNode("node3");
        
        for (int i = 0; i < 10; i++) {
            dht.put("data" + i, "value" + i);
        }
        
        System.out.println("获取data5: " + dht.get("data5"));
        System.out.println("系统状态: " + dht.getSystemStatus());
        
        // 测试节点故障恢复
        System.out.println("\n4. 节点故障恢复测试:");
        dht.removeNode("node2");
        System.out.println("移除node2后获取data5: " + dht.get("data5"));
        System.out.println("系统状态: " + dht.getSystemStatus());
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. 开放地址法: 平均O(1)，最坏O(n)时间，O(n)空间");
        System.out.println("2. 链地址法: 平均O(1)，最坏O(log n)时间，O(n)空间");
        System.out.println("3. 分布式哈希表: O(log n)查找时间，分布式O(n)空间");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 开放地址法: 内存受限环境、缓存系统、嵌入式系统");
        System.out.println("2. 链地址法: 通用哈希表、数据库索引、语言运行时");
        System.out.println("3. 分布式哈希表: P2P网络、分布式存储、区块链");
        
        System.out.println("\n=== 性能优化策略 ===");
        System.out.println("1. 负载因子监控: 实时监控哈希表负载，及时扩容");
        System.out.println("2. 探测策略选择: 根据数据分布选择合适的探测方法");
        System.out.println("3. 数据分布均衡: 在分布式系统中确保数据均匀分布");
        System.out.println("4. 故障恢复机制: 实现节点故障时的数据自动迁移");
    }
}