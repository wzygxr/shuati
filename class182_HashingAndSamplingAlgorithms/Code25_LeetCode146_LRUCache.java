package class107_HashingAndSamplingAlgorithms;

import java.util.*;

/**
 * LeetCode 146. LRU缓存机制 (LRU Cache)
 * 题目链接：https://leetcode.com/problems/lru-cache/
 * 
 * 题目描述：
 * 运用你所掌握的数据结构，设计和实现一个 LRU (最近最少使用) 缓存机制。
 * 实现 LRUCache 类：
 * - LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * - int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
 * - void put(int key, int value) 如果关键字已经存在，则变更其数据值；
 *   如果关键字不存在，则插入该组「关键字-值」。
 *   当缓存容量达到上限时，它应该在写入新数据之前删除最久未使用的数据值。
 * 
 * 算法思路：
 * 1. 使用哈希表存储键到链表节点的映射，实现O(1)查找
 * 2. 使用双向链表维护访问顺序，实现O(1)插入和删除
 * 3. 当访问或更新节点时，将其移动到链表头部（表示最近使用）
 * 4. 当缓存满时，删除链表尾部节点（表示最久未使用）
 * 
 * 时间复杂度：
 * - get: O(1)
 * - put: O(1)
 * 
 * 空间复杂度：O(capacity)
 * 
 * 工程化考量：
 * 1. 线程安全：在多线程环境下需要加锁保护
 * 2. 内存优化：使用虚拟头尾节点简化链表操作
 * 3. 异常处理：处理非法容量和键值
 * 4. 边界情况：处理空缓存、满缓存等场景
 */
public class Code25_LeetCode146_LRUCache {
    
    // 链表节点类
    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private final int capacity;
    private final Map<Integer, Node> cache;
    private final Node head;  // 虚拟头节点
    private final Node tail;  // 虚拟尾节点
    
    /**
     * 构造函数
     * @param capacity 缓存容量
     * @throws IllegalArgumentException 如果容量小于等于0
     */
    public Code25_LeetCode146_LRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        
        this.capacity = capacity;
        this.cache = new HashMap<>();
        
        // 初始化虚拟头尾节点
        this.head = new Node(0, 0);
        this.tail = new Node(0, 0);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    
    /**
     * 获取键对应的值
     * @param key 键
     * @return 键对应的值，如果不存在则返回-1
     */
    public int get(int key) {
        Node node = cache.get(key);
        if (node == null) {
            return -1;
        }
        
        // 将节点移动到头部（表示最近使用）
        moveToHead(node);
        return node.value;
    }
    
    /**
     * 插入或更新键值对
     * @param key 键
     * @param value 值
     */
    public void put(int key, int value) {
        Node node = cache.get(key);
        
        if (node == null) {
            // 创建新节点
            Node newNode = new Node(key, value);
            
            // 检查缓存是否已满
            if (cache.size() >= capacity) {
                // 删除最久未使用的节点（尾部节点）
                Node tailNode = removeTail();
                cache.remove(tailNode.key);
            }
            
            // 添加新节点到头部
            addToHead(newNode);
            cache.put(key, newNode);
        } else {
            // 更新现有节点的值
            node.value = value;
            // 将节点移动到头部（表示最近使用）
            moveToHead(node);
        }
    }
    
    /**
     * 将节点添加到头部
     * @param node 节点
     */
    private void addToHead(Node node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    /**
     * 删除节点
     * @param node 节点
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    /**
     * 将节点移动到头部
     * @param node 节点
     */
    private void moveToHead(Node node) {
        removeNode(node);
        addToHead(node);
    }
    
    /**
     * 删除尾部节点
     * @return 被删除的节点
     */
    private Node removeTail() {
        Node lastNode = tail.prev;
        removeNode(lastNode);
        return lastNode;
    }
    
    /**
     * 获取缓存统计信息
     * @return 统计信息映射
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("capacity", capacity);
        stats.put("size", cache.size());
        stats.put("usage", (double) cache.size() / capacity);
        return stats;
    }
    
    /**
     * 性能测试类
     */
    public static class PerformanceTest {
        
        /**
         * 测试LRU缓存的性能
         * @param lruCache LRU缓存实例
         * @param operationCount 操作数量
         */
        public static void testLRUPerformance(Code25_LeetCode146_LRUCache lruCache, 
                                              int operationCount) {
            System.out.println("=== LRU缓存性能测试 ===");
            System.out.printf("操作数量: %d\n", operationCount);
            
            Random random = new Random(42);
            
            // 测试插入性能
            long startTime = System.nanoTime();
            
            for (int i = 0; i < operationCount / 2; i++) {
                int key = random.nextInt(operationCount);
                lruCache.put(key, key * 2);
            }
            
            long putTime = System.nanoTime() - startTime;
            
            // 测试查询性能
            startTime = System.nanoTime();
            
            int hitCount = 0;
            int missCount = 0;
            
            for (int i = 0; i < operationCount / 2; i++) {
                int key = random.nextInt(operationCount);
                int value = lruCache.get(key);
                if (value != -1) {
                    hitCount++;
                } else {
                    missCount++;
                }
            }
            
            long getTime = System.nanoTime() - startTime;
            
            System.out.printf("插入平均时间: %.2f ns\n", (double) putTime / (operationCount / 2));
            System.out.printf("查询平均时间: %.2f ns\n", (double) getTime / (operationCount / 2));
            System.out.printf("缓存命中率: %.2f%%\n", (double) hitCount / (hitCount + missCount) * 100);
            
            // 显示统计信息
            System.out.println("缓存统计信息: " + lruCache.getStatistics());
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        System.out.println("=== LRU缓存机制测试 ===\n");
        
        // 基本功能测试
        System.out.println("1. 基本功能测试:");
        
        Code25_LeetCode146_LRUCache lruCache = new Code25_LeetCode146_LRUCache(2);
        
        // 插入键值对
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        System.out.println("插入 (1,1) 和 (2,2)");
        
        // 查询键1
        int value = lruCache.get(1);
        System.out.println("查询键1: " + value);
        
        // 插入键3，缓存满，应删除键2
        lruCache.put(3, 3);
        System.out.println("插入 (3,3)，缓存满，删除最久未使用的键2");
        
        // 查询键2，应返回-1
        value = lruCache.get(2);
        System.out.println("查询键2: " + value);
        
        // 插入键4，缓存满，应删除键1
        lruCache.put(4, 4);
        System.out.println("插入 (4,4)，缓存满，删除最久未使用的键1");
        
        // 查询键1，应返回-1
        value = lruCache.get(1);
        System.out.println("查询键1: " + value);
        
        // 查询键3和键4
        value = lruCache.get(3);
        System.out.println("查询键3: " + value);
        value = lruCache.get(4);
        System.out.println("查询键4: " + value);
        
        // 更新键4的值
        lruCache.put(4, 40);
        System.out.println("更新键4的值为40");
        value = lruCache.get(4);
        System.out.println("查询键4: " + value);
        
        // 复杂场景测试
        System.out.println("\n2. 复杂场景测试:");
        
        Code25_LeetCode146_LRUCache lruCache2 = new Code25_LeetCode146_LRUCache(3);
        
        // 插入多个键值对
        for (int i = 1; i <= 5; i++) {
            lruCache2.put(i, i * 10);
        }
        
        // 查询所有键
        for (int i = 1; i <= 5; i++) {
            int val = lruCache2.get(i);
            System.out.printf("查询键%d: %d\n", i, val);
        }
        
        // 性能测试
        System.out.println("\n3. 性能测试:");
        Code25_LeetCode146_LRUCache lruCache3 = new Code25_LeetCode146_LRUCache(100);
        PerformanceTest.testLRUPerformance(lruCache3, 10000);
        
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度:");
        System.out.println("- get: O(1)");
        System.out.println("- put: O(1)");
        System.out.println("空间复杂度: O(capacity)");
        
        System.out.println("\n=== 工程化应用场景 ===");
        System.out.println("1. Web浏览器缓存: 存储最近访问的网页内容");
        System.out.println("2. 数据库查询缓存: 缓存热点查询结果");
        System.out.println("3. 操作系统页面置换: 管理内存页面");
        System.out.println("4. CDN缓存策略: 管理边缘节点内容");
        
        System.out.println("\n=== 设计要点 ===");
        System.out.println("1. 数据结构选择: 哈希表 + 双向链表实现O(1)操作");
        System.out.println("2. 虚拟节点: 使用虚拟头尾节点简化链表操作");
        System.out.println("3. 访问顺序维护: 每次访问都将节点移到链表头部");
        System.out.println("4. 容量控制: 缓存满时删除链表尾部节点");
    }
}