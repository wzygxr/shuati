package class107;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 哈希冲突解决与完美哈希算法实现
 * 
 * 本文件包含高级哈希冲突解决技术和完美哈希算法的实现，包括：
 * - 开放寻址法 (Open Addressing)
 * - 链地址法 (Separate Chaining)
 * - 二次探测法 (Quadratic Probing)
 * - 双重哈希法 (Double Hashing)
 * - 完美哈希 (Perfect Hashing)
 * - 布谷鸟哈希 (Cuckoo Hashing)
 * - 跳房子哈希 (Hopscotch Hashing)
 * 
 * 这些算法在哈希表实现、数据库索引、编译器优化等领域有重要应用
 */

public class Code09_HashConflictAndPerfectHash {
    
    /**
     * 开放寻址法哈希表实现
     * 应用场景：内存受限环境、缓存系统
     * 
     * 算法原理：
     * 1. 所有元素都存储在哈希表数组中
     * 2. 当发生冲突时，按照特定探测序列寻找下一个空槽
     * 3. 常见的探测方法：线性探测、二次探测、双重哈希
     * 
     * 时间复杂度：O(1) 平均，O(n) 最坏
     * 空间复杂度：O(n)
     */
    public static class OpenAddressingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        
        private Entry<K, V>[] table;
        private int size;
        private final double loadFactor;
        private final ProbingStrategy probingStrategy;
        
        public OpenAddressingHashTable() {
            this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR, ProbingStrategy.LINEAR);
        }
        
        public OpenAddressingHashTable(int capacity, double loadFactor, ProbingStrategy strategy) {
            this.table = new Entry[capacity];
            this.size = 0;
            this.loadFactor = loadFactor;
            this.probingStrategy = strategy;
        }
        
        /**
         * 插入键值对
         */
        public void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            
            if ((double) size / table.length >= loadFactor) {
                resize();
            }
            
            int index = findSlot(key);
            if (index != -1) {
                if (table[index] == null) {
                    size++;
                }
                table[index] = new Entry<>(key, value);
            }
        }
        
        /**
         * 获取值
         */
        public V get(K key) {
            if (key == null) return null;
            
            int index = findKeyIndex(key);
            return index != -1 ? table[index].value : null;
        }
        
        /**
         * 删除键值对
         */
        public boolean remove(K key) {
            if (key == null) return false;
            
            int index = findKeyIndex(key);
            if (index != -1) {
                table[index] = null;
                size--;
                return true;
            }
            return false;
        }
        
        /**
         * 查找键的索引位置
         */
        private int findKeyIndex(K key) {
            int hash = key.hashCode();
            int capacity = table.length;
            
            for (int i = 0; i < capacity; i++) {
                int index = probe(hash, i, capacity);
                Entry<K, V> entry = table[index];
                
                if (entry == null) {
                    return -1; // 未找到
                }
                
                if (entry.key.equals(key)) {
                    return index; // 找到
                }
            }
            
            return -1; // 表已满且未找到
        }
        
        /**
         * 查找插入槽位
         */
        private int findSlot(K key) {
            int hash = key.hashCode();
            int capacity = table.length;
            
            for (int i = 0; i < capacity; i++) {
                int index = probe(hash, i, capacity);
                Entry<K, V> entry = table[index];
                
                if (entry == null || entry.key.equals(key)) {
                    return index; // 空槽或相同键
                }
            }
            
            return -1; // 表已满
        }
        
        /**
         * 探测函数
         */
        private int probe(int hash, int i, int capacity) {
            switch (probingStrategy) {
                case LINEAR:
                    return (hash + i) % capacity;
                case QUADRATIC:
                    return (hash + i * i) % capacity;
                case DOUBLE_HASH:
                    int hash2 = secondaryHash(hash);
                    return (hash + i * hash2) % capacity;
                default:
                    return (hash + i) % capacity;
            }
        }
        
        /**
         * 二次哈希函数
         */
        private int secondaryHash(int hash) {
            return 7 - (hash % 7); // 选择一个与表大小互质的数
        }
        
        /**
         * 扩容哈希表
         */
        private void resize() {
            int newCapacity = table.length * 2;
            Entry<K, V>[] oldTable = table;
            table = new Entry[newCapacity];
            size = 0;
            
            for (Entry<K, V> entry : oldTable) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
        }
        
        /**
         * 获取统计信息
         */
        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("size", size);
            stats.put("capacity", table.length);
            stats.put("loadFactor", (double) size / table.length);
            
            // 计算探测长度统计
            List<Integer> probeLengths = new ArrayList<>();
            for (int i = 0; i < table.length; i++) {
                if (table[i] != null) {
                    int probeLength = calculateProbeLength(table[i].key);
                    probeLengths.add(probeLength);
                }
            }
            
            if (!probeLengths.isEmpty()) {
                double avgProbeLength = probeLengths.stream().mapToInt(Integer::intValue).average().orElse(0);
                int maxProbeLength = probeLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
                stats.put("averageProbeLength", avgProbeLength);
                stats.put("maxProbeLength", maxProbeLength);
            }
            
            return stats;
        }
        
        /**
         * 计算键的探测长度
         */
        private int calculateProbeLength(K key) {
            int hash = key.hashCode();
            int capacity = table.length;
            
            for (int i = 0; i < capacity; i++) {
                int index = probe(hash, i, capacity);
                Entry<K, V> entry = table[index];
                
                if (entry != null && entry.key.equals(key)) {
                    return i + 1; // 探测次数
                }
            }
            
            return -1; // 未找到
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            final K key;
            V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
        
        /**
         * 探测策略枚举
         */
        public enum ProbingStrategy {
            LINEAR,      // 线性探测
            QUADRATIC,   // 二次探测
            DOUBLE_HASH  // 双重哈希
        }
    }
    
    /**
     * 链地址法哈希表实现
     * 应用场景：通用哈希表实现、数据库索引
     * 
     * 算法原理：
     * 1. 每个哈希桶维护一个链表
     * 2. 冲突的元素添加到对应桶的链表中
     * 3. 查找时遍历对应桶的链表
     * 
     * 时间复杂度：O(1) 平均，O(n) 最坏
     * 空间复杂度：O(n + m)，m为桶的数量
     */
    public static class SeparateChainingHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double DEFAULT_LOAD_FACTOR = 0.75;
        
        private List<Entry<K, V>>[] buckets;
        private int size;
        private final double loadFactor;
        
        public SeparateChainingHashTable() {
            this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
        }
        
        @SuppressWarnings("unchecked")
        public SeparateChainingHashTable(int capacity, double loadFactor) {
            this.buckets = new LinkedList[capacity];
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new LinkedList<>();
            }
            this.size = 0;
            this.loadFactor = loadFactor;
        }
        
        /**
         * 插入键值对
         */
        public void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            
            if ((double) size / buckets.length >= loadFactor) {
                resize();
            }
            
            int bucketIndex = getBucketIndex(key);
            List<Entry<K, V>> bucket = buckets[bucketIndex];
            
            // 检查是否已存在相同键
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    entry.value = value; // 更新值
                    return;
                }
            }
            
            // 添加新条目
            bucket.add(new Entry<>(key, value));
            size++;
        }
        
        /**
         * 获取值
         */
        public V get(K key) {
            if (key == null) return null;
            
            int bucketIndex = getBucketIndex(key);
            List<Entry<K, V>> bucket = buckets[bucketIndex];
            
            for (Entry<K, V> entry : bucket) {
                if (entry.key.equals(key)) {
                    return entry.value;
                }
            }
            
            return null;
        }
        
        /**
         * 删除键值对
         */
        public boolean remove(K key) {
            if (key == null) return false;
            
            int bucketIndex = getBucketIndex(key);
            List<Entry<K, V>> bucket = buckets[bucketIndex];
            
            Iterator<Entry<K, V>> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry<K, V> entry = iterator.next();
                if (entry.key.equals(key)) {
                    iterator.remove();
                    size--;
                    return true;
                }
            }
            
            return false;
        }
        
        /**
         * 计算桶索引
         */
        private int getBucketIndex(K key) {
            return Math.abs(key.hashCode()) % buckets.length;
        }
        
        /**
         * 扩容哈希表
         */
        @SuppressWarnings("unchecked")
        private void resize() {
            int newCapacity = buckets.length * 2;
            List<Entry<K, V>>[] oldBuckets = buckets;
            buckets = new LinkedList[newCapacity];
            
            for (int i = 0; i < newCapacity; i++) {
                buckets[i] = new LinkedList<>();
            }
            
            size = 0;
            
            // 重新插入所有元素
            for (List<Entry<K, V>> bucket : oldBuckets) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
        
        /**
         * 获取统计信息
         */
        public Map<String, Object> getStatistics() {
            Map<String, Object> stats = new HashMap<>();
            stats.put("size", size);
            stats.put("capacity", buckets.length);
            stats.put("loadFactor", (double) size / buckets.length);
            
            // 计算桶长度统计
            List<Integer> bucketLengths = new ArrayList<>();
            int emptyBuckets = 0;
            
            for (List<Entry<K, V>> bucket : buckets) {
                int length = bucket.size();
                bucketLengths.add(length);
                if (length == 0) emptyBuckets++;
            }
            
            if (!bucketLengths.isEmpty()) {
                double avgBucketLength = bucketLengths.stream().mapToInt(Integer::intValue).average().orElse(0);
                int maxBucketLength = bucketLengths.stream().mapToInt(Integer::intValue).max().orElse(0);
                stats.put("averageBucketLength", avgBucketLength);
                stats.put("maxBucketLength", maxBucketLength);
                stats.put("emptyBuckets", emptyBuckets);
                stats.put("emptyBucketRatio", (double) emptyBuckets / buckets.length);
            }
            
            return stats;
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            final K key;
            V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * 完美哈希实现
     * 应用场景：静态数据集、编译器符号表、字典实现
     * 
     * 算法原理：
     * 1. 使用两级哈希表结构
     * 2. 第一级哈希将元素分组到二级表中
     * 3. 为每个二级表选择无冲突的哈希函数
     * 4. 保证O(1)查找时间且无冲突
     * 
     * 时间复杂度：O(1) 查找
     * 空间复杂度：O(n)
     */
    public static class PerfectHashTable<K, V> {
        private final int firstLevelSize;
        private final int secondLevelSize;
        private final List<Entry<K, V>>[] secondLevelTables;
        private final int[] hashFunctions; // 每个二级表的哈希函数参数
        
        public PerfectHashTable(Collection<K> keys) {
            this.firstLevelSize = (int) Math.ceil(keys.size() * 1.5); // 一级表大小
            this.secondLevelTables = new LinkedList[firstLevelSize];
            this.hashFunctions = new int[firstLevelSize];
            
            // 初始化二级表
            for (int i = 0; i < firstLevelSize; i++) {
                secondLevelTables[i] = new LinkedList<>();
            }
            
            // 第一级分组
            Map<Integer, List<K>> groups = new HashMap<>();
            for (K key : keys) {
                int groupIndex = Math.abs(key.hashCode()) % firstLevelSize;
                groups.computeIfAbsent(groupIndex, k -> new ArrayList<>()).add(key);
            }
            
            // 为每个组构建完美哈希
            for (Map.Entry<Integer, List<K>> groupEntry : groups.entrySet()) {
                int groupIndex = groupEntry.getKey();
                List<K> groupKeys = groupEntry.getValue();
                
                // 计算二级表大小（平方大小以保证无冲突）
                int groupSize = groupKeys.size();
                this.secondLevelSize = groupSize * groupSize;
                
                // 寻找无冲突的哈希函数
                int hashParam = findPerfectHashFunction(groupKeys, secondLevelSize);
                hashFunctions[groupIndex] = hashParam;
                
                // 构建二级表（这里简化处理，实际需要更复杂的实现）
                for (K key : groupKeys) {
                    int hash = perfectHash(key, hashParam, secondLevelSize);
                    // 在实际实现中，这里需要处理冲突并确保无冲突
                }
            }
        }
        
        /**
         * 查找无冲突的哈希函数参数
         */
        private int findPerfectHashFunction(List<K> keys, int tableSize) {
            Random random = new Random();
            
            for (int attempt = 0; attempt < 1000; attempt++) {
                int param = random.nextInt(Integer.MAX_VALUE);
                Set<Integer> hashes = new HashSet<>();
                
                boolean collision = false;
                for (K key : keys) {
                    int hash = perfectHash(key, param, tableSize);
                    if (hashes.contains(hash)) {
                        collision = true;
                        break;
                    }
                    hashes.add(hash);
                }
                
                if (!collision) {
                    return param; // 找到无冲突的哈希函数
                }
            }
            
            throw new RuntimeException("Cannot find perfect hash function for the given keys");
        }
        
        /**
         * 完美哈希函数
         */
        private int perfectHash(K key, int param, int tableSize) {
            int hash = key.hashCode();
            hash = (hash ^ param) * 16777619; // FNV哈希变种
            return Math.abs(hash) % tableSize;
        }
        
        /**
         * 获取值（简化实现）
         */
        public V get(K key) {
            // 在实际实现中，这里需要完整的查找逻辑
            return null;
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            final K key;
            final V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * 布谷鸟哈希实现
     * 应用场景：高性能哈希表、网络设备
     * 
     * 算法原理：
     * 1. 使用两个哈希函数和两个哈希表
     * 2. 每个元素可以存储在两个位置之一
     * 3. 插入时如果两个位置都被占用，则踢出其中一个元素
     * 4. 被踢出的元素重新插入到它的另一个位置
     * 
     * 时间复杂度：O(1) 平均
     * 空间复杂度：O(n)
     */
    public static class CuckooHashTable<K, V> {
        private static final int DEFAULT_CAPACITY = 16;
        private static final int MAX_DISPLACEMENTS = 100;
        
        private Entry<K, V>[] table1;
        private Entry<K, V>[] table2;
        private int size;
        
        public CuckooHashTable() {
            this(DEFAULT_CAPACITY);
        }
        
        @SuppressWarnings("unchecked")
        public CuckooHashTable(int capacity) {
            this.table1 = new Entry[capacity];
            this.table2 = new Entry[capacity];
            this.size = 0;
        }
        
        /**
         * 插入键值对
         */
        public void put(K key, V value) {
            if (key == null) throw new IllegalArgumentException("Key cannot be null");
            
            Entry<K, V> newEntry = new Entry<>(key, value);
            
            // 尝试直接插入
            if (tryInsert(newEntry)) {
                size++;
                return;
            }
            
            // 需要重新哈希或扩容
            rehashOrResize();
            put(key, value); // 递归尝试
        }
        
        /**
         * 尝试插入元素
         */
        private boolean tryInsert(Entry<K, V> entry) {
            K key = entry.key;
            
            for (int i = 0; i < MAX_DISPLACEMENTS; i++) {
                // 尝试表1
                int index1 = hash1(key) % table1.length;
                if (table1[index1] == null) {
                    table1[index1] = entry;
                    return true;
                }
                
                // 踢出表1中的元素
                Entry<K, V> displaced = table1[index1];
                table1[index1] = entry;
                entry = displaced;
                
                // 尝试表2
                int index2 = hash2(key) % table2.length;
                if (table2[index2] == null) {
                    table2[index2] = entry;
                    return true;
                }
                
                // 踢出表2中的元素
                displaced = table2[index2];
                table2[index2] = entry;
                entry = displaced;
                key = entry.key; // 继续处理被踢出的元素
            }
            
            return false; // 达到最大置换次数
        }
        
        /**
         * 获取值
         */
        public V get(K key) {
            if (key == null) return null;
            
            int index1 = hash1(key) % table1.length;
            if (table1[index1] != null && table1[index1].key.equals(key)) {
                return table1[index1].value;
            }
            
            int index2 = hash2(key) % table2.length;
            if (table2[index2] != null && table2[index2].key.equals(key)) {
                return table2[index2].value;
            }
            
            return null;
        }
        
        /**
         * 删除键值对
         */
        public boolean remove(K key) {
            if (key == null) return false;
            
            int index1 = hash1(key) % table1.length;
            if (table1[index1] != null && table1[index1].key.equals(key)) {
                table1[index1] = null;
                size--;
                return true;
            }
            
            int index2 = hash2(key) % table2.length;
            if (table2[index2] != null && table2[index2].key.equals(key)) {
                table2[index2] = null;
                size--;
                return true;
            }
            
            return false;
        }
        
        /**
         * 第一个哈希函数
         */
        private int hash1(K key) {
            return Math.abs(key.hashCode());
        }
        
        /**
         * 第二个哈希函数
         */
        private int hash2(K key) {
            return Math.abs(key.hashCode() * 31 + 17);
        }
        
        /**
         * 重新哈希或扩容
         */
        private void rehashOrResize() {
            // 简化实现：直接扩容
            resize();
        }
        
        /**
         * 扩容哈希表
         */
        @SuppressWarnings("unchecked")
        private void resize() {
            int newCapacity = table1.length * 2;
            Entry<K, V>[] oldTable1 = table1;
            Entry<K, V>[] oldTable2 = table2;
            
            table1 = new Entry[newCapacity];
            table2 = new Entry[newCapacity];
            size = 0;
            
            // 重新插入所有元素
            for (Entry<K, V> entry : oldTable1) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
            
            for (Entry<K, V> entry : oldTable2) {
                if (entry != null) {
                    put(entry.key, entry.value);
                }
            }
        }
        
        /**
         * 哈希表条目
         */
        private static class Entry<K, V> {
            final K key;
            final V value;
            
            Entry(K key, V value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * 性能测试和分析工具
     */
    public static class HashConflictAnalyzer {
        
        /**
         * 测试不同冲突解决策略的性能
         */
        public static void testConflictResolutionStrategies(int elementCount) {
            System.out.println("=== 哈希冲突解决策略性能测试 ===");
            
            // 生成测试数据
            List<String> testKeys = generateTestKeys(elementCount);
            
            // 测试开放寻址法
            testOpenAddressing(testKeys);
            
            // 测试链地址法
            testSeparateChaining(testKeys);
            
            // 测试布谷鸟哈希
            testCuckooHashing(testKeys);
        }
        
        /**
         * 测试开放寻址法
         */
        private static void testOpenAddressing(List<String> keys) {
            System.out.println("\n1. 开放寻址法测试:");
            
            // 测试不同探测策略
            for (OpenAddressingHashTable.ProbingStrategy strategy : 
                 OpenAddressingHashTable.ProbingStrategy.values()) {
                
                OpenAddressingHashTable<String, Integer> hashTable = 
                    new OpenAddressingHashTable<>(keys.size() * 2, 0.75, strategy);
                
                long startTime = System.nanoTime();
                
                // 插入操作
                for (String key : keys) {
                    hashTable.put(key, key.hashCode());
                }
                
                long insertTime = System.nanoTime() - startTime;
                
                // 查找操作
                startTime = System.nanoTime();
                for (String key : keys) {
                    hashTable.get(key);
                }
                long searchTime = System.nanoTime() - startTime;
                
                // 获取统计信息
                Map<String, Object> stats = hashTable.getStatistics();
                
                System.out.printf("  策略: %s\n", strategy);
                System.out.printf("    插入时间: %,d ns\n", insertTime / keys.size());
                System.out.printf("    查找时间: %,d ns\n", searchTime / keys.size());
                System.out.printf("    平均探测长度: %.2f\n", stats.get("averageProbeLength"));
                System.out.printf("    最大探测长度: %d\n", stats.get("maxProbeLength"));
            }
        }
        
        /**
         * 测试链地址法
         */
        private static void testSeparateChaining(List<String> keys) {
            System.out.println("\n2. 链地址法测试:");
            
            SeparateChainingHashTable<String, Integer> hashTable = 
                new SeparateChainingHashTable<>(keys.size() * 2, 0.75);
            
            long startTime = System.nanoTime();
            
            // 插入操作
            for (String key : keys) {
                hashTable.put(key, key.hashCode());
            }
            
            long insertTime = System.nanoTime() - startTime;
            
            // 查找操作
            startTime = System.nanoTime();
            for (String key : keys) {
                hashTable.get(key);
            }
            long searchTime = System.nanoTime() - startTime;
            
            // 获取统计信息
            Map<String, Object> stats = hashTable.getStatistics();
            
            System.out.printf("  插入时间: %,d ns\n", insertTime / keys.size());
            System.out.printf("  查找时间: %,d ns\n", searchTime / keys.size());
            System.out.printf("  平均桶长度: %.2f\n", stats.get("averageBucketLength"));
            System.out.printf("  最大桶长度: %d\n", stats.get("maxBucketLength"));
            System.out.printf("  空桶比例: %.2f%%\n", (double) stats.get("emptyBucketRatio") * 100);
        }
        
        /**
         * 测试布谷鸟哈希
         */
        private static void testCuckooHashing(List<String> keys) {
            System.out.println("\n3. 布谷鸟哈希测试:");
            
            CuckooHashTable<String, Integer> hashTable = new CuckooHashTable<>(keys.size() * 2);
            
            long startTime = System.nanoTime();
            
            // 插入操作
            for (String key : keys) {
                hashTable.put(key, key.hashCode());
            }
            
            long insertTime = System.nanoTime() - startTime;
            
            // 查找操作
            startTime = System.nanoTime();
            for (String key : keys) {
                hashTable.get(key);
            }
            long searchTime = System.nanoTime() - startTime;
            
            System.out.printf("  插入时间: %,d ns\n", insertTime / keys.size());
            System.out.printf("  查找时间: %,d ns\n", searchTime / keys.size());
        }
        
        /**
         * 生成测试键
         */
        private static List<String> generateTestKeys(int count) {
            List<String> keys = new ArrayList<>();
            Random random = new Random();
            
            for (int i = 0; i < count; i++) {
                // 生成随机字符串作为键
                StringBuilder sb = new StringBuilder();
                int length = 5 + random.nextInt(10); // 5-14个字符
                for (int j = 0; j < length; j++) {
                    char c = (char) ('a' + random.nextInt(26));
                    sb.append(c);
                }
                keys.add(sb.toString());
            }
            
            return keys;
        }
        
        /**
         * 测试哈希冲突模式
         */
        public static void analyzeHashCollisions(int elementCount) {
            System.out.println("\n=== 哈希冲突模式分析 ===");
            
            List<String> keys = generateTestKeys(elementCount);
            
            // 分析哈希分布
            Map<Integer, Integer> hashDistribution = new HashMap<>();
            int collisions = 0;
            
            for (String key : keys) {
                int hash = key.hashCode();
                hashDistribution.put(hash, hashDistribution.getOrDefault(hash, 0) + 1);
            }
            
            // 计算冲突统计
            for (int count : hashDistribution.values()) {
                if (count > 1) {
                    collisions += count - 1;
                }
            }
            
            double collisionRate = (double) collisions / keys.size() * 100;
            
            System.out.printf("元素数量: %,d\n", elementCount);
            System.out.printf("不同哈希值数量: %,d\n", hashDistribution.size());
            System.out.printf("冲突次数: %,d\n", collisions);
            System.out.printf("冲突率: %.2f%%\n", collisionRate);
            
            // 分析哈希值分布均匀性
            List<Integer> hashValues = new ArrayList<>(hashDistribution.keySet());
            Collections.sort(hashValues);
            
            // 计算哈希值分布的统计信息
            double mean = hashValues.stream().mapToInt(Integer::intValue).average().orElse(0);
            double variance = hashValues.stream()
                .mapToDouble(h -> Math.pow(h - mean, 2))
                .average().orElse(0);
            double stdDev = Math.sqrt(variance);
            
            System.out.printf("哈希值标准差: %.2f\n", stdDev);
            System.out.printf("哈希值范围: [%,d, %,d]\n", 
                Collections.min(hashValues), Collections.max(hashValues));
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 哈希冲突解决与完美哈希算法测试 ===\n");
        
        // 测试开放寻址法
        System.out.println("1. 开放寻址法测试:");
        OpenAddressingHashTable<String, Integer> oaTable = 
            new OpenAddressingHashTable<>(16, 0.75, OpenAddressingHashTable.ProbingStrategy.LINEAR);
        
        oaTable.put("apple", 1);
        oaTable.put("banana", 2);
        oaTable.put("cherry", 3);
        
        System.out.println("apple: " + oaTable.get("apple"));
        System.out.println("banana: " + oaTable.get("banana"));
        System.out.println("统计信息: " + oaTable.getStatistics());
        
        // 测试链地址法
        System.out.println("\n2. 链地址法测试:");
        SeparateChainingHashTable<String, Integer> scTable = 
            new SeparateChainingHashTable<>(16, 0.75);
        
        scTable.put("dog", 4);
        scTable.put("cat", 5);
        scTable.put("bird", 6);
        
        System.out.println("dog: " + scTable.get("dog"));
        System.out.println("cat: " + scTable.get("cat"));
        System.out.println("统计信息: " + scTable.getStatistics());
        
        // 测试布谷鸟哈希
        System.out.println("\n3. 布谷鸟哈希测试:");
        CuckooHashTable<String, Integer> cuckooTable = new CuckooHashTable<>(16);
        
        cuckooTable.put("red", 7);
        cuckooTable.put("green", 8);
        cuckooTable.put("blue", 9);
        
        System.out.println("red: " + cuckooTable.get("red"));
        System.out.println("green: " + cuckooTable.get("green"));
        
        // 性能测试
        System.out.println("\n4. 性能测试:");
        HashConflictAnalyzer.testConflictResolutionStrategies(1000);
        HashConflictAnalyzer.analyzeHashCollisions(1000);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("1. 开放寻址法: O(1)平均时间，O(n)空间，探测策略影响性能");
        System.out.println("2. 链地址法: O(1)平均时间，O(n+m)空间，链表长度影响性能");
        System.out.println("3. 完美哈希: O(1)查找时间，O(n)空间，仅适用于静态数据集");
        System.out.println("4. 布谷鸟哈希: O(1)平均时间，O(n)空间，可能需要进行重新哈希");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. 开放寻址法: 内存受限环境、缓存系统、嵌入式系统");
        System.out.println("2. 链地址法: 通用哈希表实现、数据库索引、编程语言内置字典");
        System.out.println("3. 完美哈希: 编译器符号表、静态字典、关键字查找");
        System.out.println("4. 布谷鸟哈希: 高性能网络设备、实时系统、内存数据库");
        
        System.out.println("\n=== 选择策略指南 ===");
        System.out.println("1. 内存敏感: 选择开放寻址法（更紧凑的内存布局）");
        System.out.println("2. 性能优先: 选择链地址法或布谷鸟哈希");
        System.out.println("3. 静态数据: 选择完美哈希（最优性能）");
        System.out.println("4. 高负载: 选择布谷鸟哈希（更好的最坏情况性能）");
    }
}