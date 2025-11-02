package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * LeetCode 706. 设计哈希映射
 * 题目链接：https://leetcode.com/problems/design-hashmap/
 * 
 * 题目描述：
 * 不使用任何内建的哈希表库设计一个哈希映射（HashMap）。
 * 实现 MyHashMap 类：
 * - MyHashMap() 用空映射初始化对象
 * - void put(int key, int value) 向 HashMap 插入一个键值对 (key, value) 。如果 key 已经存在于映射中，则更新其对应的值 value 。
 * - int get(int key) 返回特定的 key 所映射的 value ；如果映射中不包含 key 的映射，返回 -1 。
 * - void remove(key) 如果映射中存在 key 的映射，则移除 key 和它所对应的 value 。
 * 
 * 示例：
 * 输入：
 * ["MyHashMap", "put", "put", "get", "get", "put", "get", "remove", "get"]
 * [[], [1, 1], [2, 2], [1], [3], [2, 1], [2], [2], [2]]
 * 输出：
 * [null, null, null, 1, -1, null, 1, null, -1]
 * 
 * 约束条件：
 * 0 <= key, value <= 10^6
 * 最多调用 10^4 次 put、get 和 remove 方法
 * 
 * 算法思路：
 * 使用链地址法实现哈希表，创建一个固定大小的数组，每个数组元素是一个链表。
 * 每个链表节点存储键值对，当发生哈希冲突时，将节点添加到对应位置的链表中。
 * 
 * 时间复杂度：
 * - put: O(n/b)，其中n是元素个数，b是桶数
 * - get: O(n/b)
 * - remove: O(n/b)
 * 
 * 空间复杂度：O(n)，存储所有键值对
 * 
 * 工程化考量：
 * 1. 边界情况处理：空映射、重复键值、不存在的键
 * 2. 内存管理：动态分配链表节点
 * 3. 性能优化：选择合适的桶数量
 * 4. 异常处理：输入验证和错误恢复
 */
public class Code20_LeetCode706_DesignHashMap {
    
    static class MyHashMap {
        private static final int BASE = 10000; // 桶的数量
        private LinkedList<Pair>[] data; // 桶数组，每个桶是一个链表
        
        /** Initialize your data structure here. */
        public MyHashMap() {
            data = new LinkedList[BASE];
            for (int i = 0; i < BASE; ++i) {
                data[i] = new LinkedList<Pair>();
            }
        }
        
        /** value will always be non-negative. */
        public void put(int key, int value) {
            int h = hash(key);
            Iterator<Pair> iterator = data[h].iterator();
            while (iterator.hasNext()) {
                Pair pair = iterator.next();
                if (pair.getKey() == key) {
                    pair.setValue(value);
                    return;
                }
            }
            data[h].offerLast(new Pair(key, value));
        }
        
        /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
        public int get(int key) {
            int h = hash(key);
            Iterator<Pair> iterator = data[h].iterator();
            while (iterator.hasNext()) {
                Pair pair = iterator.next();
                if (pair.getKey() == key) {
                    return pair.getValue();
                }
            }
            return -1;
        }
        
        /** Removes the mapping of the specified value key if this map contains a mapping for the key */
        public void remove(int key) {
            int h = hash(key);
            Iterator<Pair> iterator = data[h].iterator();
            while (iterator.hasNext()) {
                Pair pair = iterator.next();
                if (pair.getKey() == key) {
                    iterator.remove();
                    return;
                }
            }
        }
        
        private static int hash(int key) {
            return key % BASE;
        }
        
        private class Pair {
            private int key;
            private int value;
            
            public Pair(int key, int value) {
                this.key = key;
                this.value = value;
            }
            
            public int getKey() {
                return key;
            }
            
            public int getValue() {
                return value;
            }
            
            public void setValue(int value) {
                this.value = value;
            }
        }
    }
    
    /**
     * 优化版本：使用更好的哈希函数和动态扩容
     */
    static class MyHashMapOptimized {
        private static final int DEFAULT_CAPACITY = 16;
        private static final double LOAD_FACTOR = 0.75;
        
        private LinkedList<Entry>[] buckets;
        private int size;
        private int capacity;
        
        public MyHashMapOptimized() {
            this(DEFAULT_CAPACITY);
        }
        
        public MyHashMapOptimized(int initialCapacity) {
            if (initialCapacity <= 0) {
                throw new IllegalArgumentException("初始容量必须大于0");
            }
            this.capacity = initialCapacity;
            this.buckets = new LinkedList[capacity];
            this.size = 0;
            
            for (int i = 0; i < capacity; i++) {
                buckets[i] = new LinkedList<>();
            }
        }
        
        private int hash(int key) {
            return Math.abs(Integer.hashCode(key)) % capacity;
        }
        
        public void put(int key, int value) {
            // 检查是否需要扩容
            if ((double) size / capacity >= LOAD_FACTOR) {
                resize();
            }
            
            int index = hash(key);
            LinkedList<Entry> bucket = buckets[index];
            
            // 检查键是否已存在
            for (Entry entry : bucket) {
                if (entry.key == key) {
                    entry.value = value;
                    return;
                }
            }
            
            // 添加新键值对
            bucket.add(new Entry(key, value));
            size++;
        }
        
        public int get(int key) {
            int index = hash(key);
            LinkedList<Entry> bucket = buckets[index];
            
            for (Entry entry : bucket) {
                if (entry.key == key) {
                    return entry.value;
                }
            }
            
            return -1;
        }
        
        public void remove(int key) {
            int index = hash(key);
            LinkedList<Entry> bucket = buckets[index];
            
            Iterator<Entry> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                Entry entry = iterator.next();
                if (entry.key == key) {
                    iterator.remove();
                    size--;
                    return;
                }
            }
        }
        
        private void resize() {
            int newCapacity = capacity * 2;
            LinkedList<Entry>[] newBuckets = new LinkedList[newCapacity];
            
            // 初始化新桶
            for (int i = 0; i < newCapacity; i++) {
                newBuckets[i] = new LinkedList<>();
            }
            
            // 重新哈希所有元素
            for (LinkedList<Entry> bucket : buckets) {
                for (Entry entry : bucket) {
                    int newIndex = Math.abs(Integer.hashCode(entry.key)) % newCapacity;
                    newBuckets[newIndex].add(entry);
                }
            }
            
            // 更新容量和桶数组
            this.capacity = newCapacity;
            this.buckets = newBuckets;
        }
        
        public int size() {
            return size;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public void clear() {
            for (LinkedList<Entry> bucket : buckets) {
                bucket.clear();
            }
            size = 0;
        }
        
        private class Entry {
            int key;
            int value;
            
            Entry(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 706. 设计哈希映射 ===");
        
        // 基础版本测试
        testBasicVersion();
        
        // 优化版本测试
        testOptimizedVersion();
        
        // 性能对比测试
        performanceTest();
        
        // 边界情况测试
        edgeCaseTest();
    }
    
    private static void testBasicVersion() {
        System.out.println("--- 基础版本测试 ---");
        MyHashMap hashMap = new MyHashMap();
        
        // 测试put和get操作
        hashMap.put(1, 1);
        hashMap.put(2, 2);
        System.out.println("get(1): " + hashMap.get(1)); // 期望: 1
        System.out.println("get(3): " + hashMap.get(3)); // 期望: -1
        
        // 测试更新操作
        hashMap.put(2, 1);
        System.out.println("get(2): " + hashMap.get(2)); // 期望: 1
        
        // 测试删除操作
        hashMap.remove(2);
        System.out.println("get(2): " + hashMap.get(2)); // 期望: -1
        
        // 测试边界值
        hashMap.put(1000000, 1000000);
        System.out.println("get(1000000): " + hashMap.get(1000000)); // 期望: 1000000
        hashMap.remove(1000000);
        System.out.println("get(1000000): " + hashMap.get(1000000)); // 期望: -1
    }
    
    private static void testOptimizedVersion() {
        System.out.println("\n--- 优化版本测试 ---");
        MyHashMapOptimized hashMap = new MyHashMapOptimized();
        
        // 测试基本操作
        hashMap.put(1, 10);
        hashMap.put(2, 20);
        hashMap.put(3, 30);
        System.out.println("大小: " + hashMap.size()); // 期望: 3
        System.out.println("get(2): " + hashMap.get(2)); // 期望: 20
        
        // 测试更新操作
        hashMap.put(2, 25);
        System.out.println("更新后get(2): " + hashMap.get(2)); // 期望: 25
        
        // 测试删除操作
        hashMap.remove(2);
        System.out.println("删除后get(2): " + hashMap.get(2)); // 期望: -1
        System.out.println("删除后大小: " + hashMap.size()); // 期望: 2
        
        // 测试清空操作
        hashMap.clear();
        System.out.println("清空后大小: " + hashMap.size()); // 期望: 0
        System.out.println("清空后是否为空: " + hashMap.isEmpty()); // 期望: true
    }
    
    private static void performanceTest() {
        System.out.println("\n--- 性能对比测试 ---");
        int testSize = 10000;
        
        // 基础版本性能测试
        long startTime = System.currentTimeMillis();
        MyHashMap basicMap = new MyHashMap();
        for (int i = 0; i < testSize; i++) {
            basicMap.put(i, i * 2);
        }
        for (int i = 0; i < testSize; i++) {
            basicMap.get(i);
        }
        long basicTime = System.currentTimeMillis() - startTime;
        
        // 优化版本性能测试
        startTime = System.currentTimeMillis();
        MyHashMapOptimized optimizedMap = new MyHashMapOptimized();
        for (int i = 0; i < testSize; i++) {
            optimizedMap.put(i, i * 2);
        }
        for (int i = 0; i < testSize; i++) {
            optimizedMap.get(i);
        }
        long optimizedTime = System.currentTimeMillis() - startTime;
        
        // Java内置HashMap性能测试
        startTime = System.currentTimeMillis();
        Map<Integer, Integer> javaMap = new HashMap<>();
        for (int i = 0; i < testSize; i++) {
            javaMap.put(i, i * 2);
        }
        for (int i = 0; i < testSize; i++) {
            javaMap.get(i);
        }
        long javaTime = System.currentTimeMillis() - startTime;
        
        System.out.println("基础版本耗时: " + basicTime + "ms");
        System.out.println("优化版本耗时: " + optimizedTime + "ms");
        System.out.println("Java内置HashMap耗时: " + javaTime + "ms");
    }
    
    private static void edgeCaseTest() {
        System.out.println("\n--- 边界情况测试 ---");
        MyHashMapOptimized hashMap = new MyHashMapOptimized(1); // 最小容量
        
        // 测试大量重复操作
        for (int i = 0; i < 100; i++) {
            hashMap.put(1, i);
        }
        System.out.println("重复put 100次后get(1): " + hashMap.get(1)); // 期望: 99
        System.out.println("大小: " + hashMap.size()); // 期望: 1
        
        // 测试删除不存在的键
        hashMap.remove(999);
        System.out.println("删除不存在的键后大小: " + hashMap.size()); // 期望: 1
        
        // 测试负数键
        hashMap.put(-1, -10);
        hashMap.put(-2, -20);
        System.out.println("get(-1): " + hashMap.get(-1)); // 期望: -10
        System.out.println("get(-2): " + hashMap.get(-2)); // 期望: -20
        
        // 测试零键
        hashMap.put(0, 0);
        System.out.println("get(0): " + hashMap.get(0)); // 期望: 0
    }
}