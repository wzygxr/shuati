package class184;

import java.util.*;

/**
 * LeetCode 146. LRU Cache 解决方案
 * 
 * 题目链接: https://leetcode.com/problems/lru-cache/
 * 题目描述: 实现最近最少使用缓存
 * 解题思路: 使用双向循环链表和哈希表
 * 
 * 时间复杂度: 
 * - get操作: O(1)
 * - put操作: O(1)
 * 空间复杂度: O(capacity)
 */
public class LeetCode_LRUCache {
    
    /**
     * 双向链表节点类
     */
    static class Node {
        int key;
        int value;
        Node prev;
        Node next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.prev = this;
            this.next = this;
        }
    }
    
    /**
     * 双向循环链表类
     */
    static class DoublyCircularLinkedList {
        private Node head;  // 头节点（最久未使用的节点）
        private int size;   // 链表大小
        
        DoublyCircularLinkedList() {
            this.head = null;
            this.size = 0;
        }
        
        /**
         * 在链表尾部插入节点（表示最近使用）
         */
        void insertAtTail(Node node) {
            if (head == null) {
                // 空链表
                head = node;
            } else {
                // 非空链表，插入到尾部
                Node tail = head.prev;
                
                node.next = head;
                node.prev = tail;
                
                tail.next = node;
                head.prev = node;
            }
            size++;
        }
        
        /**
         * 删除指定节点
         */
        void deleteNode(Node node) {
            if (node == null || head == null) {
                return;
            }
            
            if (size == 1) {
                // 链表只有一个节点
                head = null;
            } else {
                // 链表有多个节点
                node.prev.next = node.next;
                node.next.prev = node.prev;
                
                // 如果删除的是头节点，更新头节点
                if (node == head) {
                    head = node.next;
                }
            }
            size--;
        }
        
        /**
         * 删除头节点（最久未使用的节点）
         * @return 被删除的节点，如果链表为空返回null
         */
        Node deleteHead() {
            if (head == null) {
                return null;
            }
            
            Node oldHead = head;
            
            if (size == 1) {
                // 链表只有一个节点
                head = null;
            } else {
                // 链表有多个节点
                Node tail = head.prev;
                Node newHead = head.next;
                
                tail.next = newHead;
                newHead.prev = tail;
                
                head = newHead;
            }
            size--;
            return oldHead;
        }
        
        /**
         * 将节点移动到尾部（表示最近使用）
         */
        void moveToTail(Node node) {
            // 先删除节点
            deleteNode(node);
            // 再插入到尾部
            insertAtTail(node);
        }
        
        /**
         * 获取链表大小
         */
        int size() {
            return size;
        }
        
        /**
         * 检查链表是否为空
         */
        boolean isEmpty() {
            return size == 0;
        }
    }
    
    private int capacity;                    // 缓存容量
    private DoublyCircularLinkedList list;   // 双向循环链表
    private Map<Integer, Node> map;          // 哈希表，用于O(1)查找
    
    /**
     * 构造函数
     * @param capacity 缓存容量
     */
    public LeetCode_LRUCache(int capacity) {
        this.capacity = capacity;
        this.list = new DoublyCircularLinkedList();
        this.map = new HashMap<>();
    }
    
    /**
     * 获取键对应的值
     * @param key 键
     * @return 值，如果键不存在返回-1
     */
    public int get(int key) {
        Node node = map.get(key);
        if (node == null) {
            return -1;  // 键不存在
        }
        
        // 将节点移动到链表尾部（表示最近使用）
        list.moveToTail(node);
        
        return node.value;
    }
    
    /**
     * 插入或更新键值对
     * @param key 键
     * @param value 值
     */
    public void put(int key, int value) {
        Node node = map.get(key);
        
        if (node == null) {
            // 键不存在，需要插入新节点
            Node newNode = new Node(key, value);
            
            // 检查是否需要淘汰最久未使用的节点
            if (list.size() >= capacity) {
                Node oldNode = list.deleteHead();
                if (oldNode != null) {
                    map.remove(oldNode.key);
                }
            }
            
            // 插入新节点
            list.insertAtTail(newNode);
            map.put(key, newNode);
        } else {
            // 键已存在，更新值并移动到尾部
            node.value = value;
            list.moveToTail(node);
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        // 测试用例1
        System.out.println("=== 测试用例1 ===");
        LeetCode_LRUCache lruCache = new LeetCode_LRUCache(2);
        
        lruCache.put(1, 1); // 缓存是 {1=1}
        lruCache.put(2, 2); // 缓存是 {1=1, 2=2}
        System.out.println("get(1) = " + lruCache.get(1)); // 返回 1
        lruCache.put(3, 3); // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        System.out.println("get(2) = " + lruCache.get(2)); // 返回 -1 (未找到)
        lruCache.put(4, 4); // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        System.out.println("get(1) = " + lruCache.get(1)); // 返回 -1 (未找到)
        System.out.println("get(3) = " + lruCache.get(3)); // 返回 3
        System.out.println("get(4) = " + lruCache.get(4)); // 返回 4
        
        // 测试用例2
        System.out.println("\n=== 测试用例2 ===");
        LeetCode_LRUCache lruCache2 = new LeetCode_LRUCache(1);
        
        lruCache2.put(2, 1); // 缓存是 {2=1}
        System.out.println("get(2) = " + lruCache2.get(2)); // 返回 1
        lruCache2.put(3, 2); // 该操作会使得关键字 2 作废，缓存是 {3=2}
        System.out.println("get(2) = " + lruCache2.get(2)); // 返回 -1 (未找到)
        System.out.println("get(3) = " + lruCache2.get(3)); // 返回 2
        
        // 测试用例3：更新已存在的键
        System.out.println("\n=== 测试用例3：更新已存在的键 ===");
        LeetCode_LRUCache lruCache3 = new LeetCode_LRUCache(2);
        
        lruCache3.put(2, 1); // 缓存是 {2=1}
        lruCache3.put(1, 1); // 缓存是 {2=1, 1=1}
        lruCache3.put(2, 3); // 更新键2的值，缓存是 {1=1, 2=3}
        lruCache3.put(4, 1); // 该操作会使得关键字 1 作废，缓存是 {2=3, 4=1}
        System.out.println("get(1) = " + lruCache3.get(1)); // 返回 -1 (未找到)
        System.out.println("get(2) = " + lruCache3.get(2)); // 返回 3
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        int capacity = 1000;
        int numOperations = 100000;
        LeetCode_LRUCache lruCache = new LeetCode_LRUCache(capacity);
        Random random = new Random(42); // 固定种子以确保可重复性
        
        long startTime = System.currentTimeMillis();
        
        // 执行随机的get和put操作
        for (int i = 0; i < numOperations; i++) {
            if (random.nextBoolean()) {
                // 执行get操作
                int key = random.nextInt(capacity * 2); // 一半的键在缓存中，一半不在
                lruCache.get(key);
            } else {
                // 执行put操作
                int key = random.nextInt(capacity * 2);
                int value = random.nextInt(1000);
                lruCache.put(key, value);
            }
        }
        
        long endTime = System.currentTimeMillis();
        
        System.out.println("执行 " + numOperations + " 次操作耗时: " + (endTime - startTime) + " ms");
        System.out.println("平均每次操作耗时: " + (double)(endTime - startTime) / numOperations * 1000 + " μs");
    }
}