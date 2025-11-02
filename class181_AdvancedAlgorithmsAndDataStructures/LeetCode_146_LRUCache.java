package class185.doubly_circular_linked_list_problems;

import java.util.*;

/**
 * LeetCode 146. LRU缓存机制 (LRU Cache)
 * 
 * 题目来源：https://leetcode.cn/problems/lru-cache/
 * 
 * 题目描述：
 * 请你设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
 * 实现 LRUCache 类：
 * - LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
 * - int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
 * - void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value；
 *   如果不存在，则向缓存中插入该组 key-value。如果插入操作导致关键字数量超过 capacity，
 *   则应该逐出最久未使用的关键字
 * 
 * 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。
 * 
 * 示例：
 * 输入：
 * ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
 * [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
 * 输出：
 * [null, null, null, 1, null, -1, null, -1, 3, 4]
 * 
 * 解释：
 * LRUCache lRUCache = new LRUCache(2);
 * lRUCache.put(1, 1); // 缓存是 {1=1}
 * lRUCache.put(2, 2); // 缓存是 {1=1, 2=2}
 * lRUCache.get(1);    // 返回 1
 * lRUCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
 * lRUCache.get(2);    // 返回 -1 (未找到)
 * lRUCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
 * lRUCache.get(1);    // 返回 -1 (未找到)
 * lRUCache.get(3);    // 返回 3
 * lRUCache.get(4);    // 返回 4
 * 
 * 提示：
 * 1 <= capacity <= 3000
 * 0 <= key <= 10000
 * 0 <= value <= 10^5
 * 最多调用 2 * 10^5 次 get 和 put
 * 
 * 解题思路：
 * 使用双向循环链表 + 哈希表实现LRU缓存。核心思想是：
 * 1. 使用双向循环链表维护访问顺序，最近访问的节点在链表头部
 * 2. 使用哈希表实现O(1)的查找操作
 * 3. 当缓存满时，删除链表尾部的节点（最久未使用）
 * 
 * 时间复杂度：O(1) 对于get和put操作
 * 空间复杂度：O(capacity)
 * 
 * 相关题目：
 * - LeetCode 460. LFU缓存
 * - LeetCode 432. 全O(1)的数据结构
 */
public class LeetCode_146_LRUCache {
    
    // 双向链表节点
    class DLinkedNode {
        int key;
        int value;
        DLinkedNode prev;
        DLinkedNode next;
        
        DLinkedNode() {}
        
        DLinkedNode(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private Map<Integer, DLinkedNode> cache;
    private int size;
    private int capacity;
    private DLinkedNode head, tail;
    
    public LeetCode_146_LRUCache(int capacity) {
        this.cache = new HashMap<>();
        this.size = 0;
        this.capacity = capacity;
        
        // 使用伪头部和伪尾部节点
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head.next = tail;
        tail.prev = head;
    }
    
    public int get(int key) {
        DLinkedNode node = cache.get(key);
        if (node == null) {
            return -1;
        }
        
        // 如果key存在，先通过哈希表定位，再移到头部
        moveToHead(node);
        return node.value;
    }
    
    public void put(int key, int value) {
        DLinkedNode node = cache.get(key);
        
        if (node == null) {
            // 如果key不存在，创建一个新的节点
            DLinkedNode newNode = new DLinkedNode(key, value);
            
            // 添加进哈希表
            cache.put(key, newNode);
            
            // 添加至双向链表的头部
            addToHead(newNode);
            size++;
            
            if (size > capacity) {
                // 如果超出容量，删除双向链表的尾部节点
                DLinkedNode tail = removeTail();
                
                // 删除哈希表中对应的项
                cache.remove(tail.key);
                size--;
            }
        } else {
            // 如果key存在，先通过哈希表定位，再修改value，并移到头部
            node.value = value;
            moveToHead(node);
        }
    }
    
    private void addToHead(DLinkedNode node) {
        node.prev = head;
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(DLinkedNode node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    private void moveToHead(DLinkedNode node) {
        removeNode(node);
        addToHead(node);
    }
    
    private DLinkedNode removeTail() {
        DLinkedNode res = tail.prev;
        removeNode(res);
        return res;
    }
    
    /**
     * 测试LRU缓存实现
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 146. LRU缓存机制 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        LeetCode_146_LRUCache lruCache = new LeetCode_146_LRUCache(2);
        
        lruCache.put(1, 1);
        lruCache.put(2, 2);
        System.out.println("put(1,1), put(2,2)");
        
        int result1 = lruCache.get(1);
        System.out.println("get(1) = " + result1 + " (期望: 1)");
        
        lruCache.put(3, 3);
        System.out.println("put(3,3)");
        
        int result2 = lruCache.get(2);
        System.out.println("get(2) = " + result2 + " (期望: -1)");
        
        lruCache.put(4, 4);
        System.out.println("put(4,4)");
        
        int result3 = lruCache.get(1);
        System.out.println("get(1) = " + result3 + " (期望: -1)");
        
        int result4 = lruCache.get(3);
        System.out.println("get(3) = " + result4 + " (期望: 3)");
        
        int result5 = lruCache.get(4);
        System.out.println("get(4) = " + result5 + " (期望: 4)");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        LeetCode_146_LRUCache performanceCache = new LeetCode_146_LRUCache(1000);
        Random random = new Random(42);
        
        long startTime = System.nanoTime();
        
        // 执行10000次操作
        for (int i = 0; i < 10000; i++) {
            int operation = random.nextInt(3);
            int key = random.nextInt(2000);
            
            if (operation == 0) {
                // get操作
                performanceCache.get(key);
            } else {
                // put操作
                int value = random.nextInt(10000);
                performanceCache.put(key, value);
            }
        }
        
        long endTime = System.nanoTime();
        
        System.out.println("10000次操作完成");
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 工程化考量
        System.out.println("\n=== 工程化考量 ===");
        System.out.println("1. 线程安全：在多线程环境下需要加锁");
        System.out.println("2. 内存管理：合理设置缓存容量");
        System.out.println("3. 性能优化：使用伪头部和伪尾部简化边界处理");
        System.out.println("4. 异常处理：处理非法输入和边界情况");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("时间复杂度: O(1)");
        System.out.println("  - get操作: O(1)");
        System.out.println("  - put操作: O(1)");
        System.out.println("空间复杂度: O(capacity)");
        System.out.println("  - 哈希表: O(capacity)");
        System.out.println("  - 双向链表: O(capacity)");
    }
}