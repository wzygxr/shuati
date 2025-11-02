package class008_AdvancedAlgorithmsAndDataStructures.doubly_circular_linked_list_problems;

import java.util.*;

/**
 * LeetCode 460. LFU缓存 (LFU Cache)
 * 
 * 题目来源：https://leetcode.cn/problems/lfu-cache/
 * 
 * 题目描述：
 * 请你为最不经常使用（LFU）缓存算法设计并实现数据结构。
 * 实现 LFUCache 类：
 * - LFUCache(int capacity) - 用数据结构的容量 capacity 初始化对象
 * - int get(int key) - 如果键存在于缓存中，则获取键的值，否则返回 -1
 * - void put(int key, int value) - 如果键已存在，则变更其值；如果键不存在，请插入键值对
 * 
 * 当缓存达到其容量时，则应该在插入新项之前，使最不经常使用的项无效。
 * 在此问题中，当存在平局（即两个或更多个键具有相同使用频率）时，应该去除最久未使用的键。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 双向循环链表：维护不同频率的节点列表
 * 2. 哈希表：快速定位键值对和频率列表
 * 3. 双哈希表：一个存储键值对，一个存储频率到节点列表的映射
 * 
 * 使用双向循环链表的方法：
 * 1. 维护一个频率到双向循环链表的映射
 * 2. 每个链表存储具有相同频率的键值对节点
 * 3. 维护一个键到节点的映射用于快速访问
 * 4. 记录最小频率用于快速找到需要删除的节点
 * 
 * 时间复杂度：
 * - get操作：O(1)
 * - put操作：O(1)
 * - 空间复杂度：O(capacity)
 * 
 * 应用场景：
 * 1. 缓存系统：LFU缓存策略实现
 * 2. 操作系统：页面置换算法
 * 3. 数据库：查询结果缓存
 * 
 * 相关题目：
 * 1. LeetCode 146. LRU缓存机制
 * 2. LeetCode 1756. 设计最近使用（MRU）栈
 * 3. LeetCode 1429. 第一个唯一数字
 */
public class LeetCode_460_LFUCache {
    
    /**
     * 双向链表节点
     */
    static class Node {
        int key, value, freq;
        Node prev, next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
            this.freq = 1;
        }
    }
    
    /**
     * 双向循环链表
     */
    static class DoublyCircularLinkedList {
        Node head; // 虚拟头节点
        
        DoublyCircularLinkedList() {
            head = new Node(0, 0);
            head.prev = head;
            head.next = head;
        }
        
        /**
         * 在头部添加节点
         */
        void addFirst(Node node) {
            node.next = head.next;
            node.prev = head;
            head.next.prev = node;
            head.next = node;
        }
        
        /**
         * 删除指定节点
         */
        void remove(Node node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        /**
         * 删除尾部节点（最久未使用的节点）
         */
        Node removeLast() {
            if (isEmpty()) {
                return null;
            }
            Node last = head.prev;
            remove(last);
            return last;
        }
        
        /**
         * 检查链表是否为空
         */
        boolean isEmpty() {
            return head.next == head;
        }
    }
    
    private int capacity;
    private int size;
    private int minFreq; // 记录最小频率
    private Map<Integer, Node> keyToNode; // 键到节点的映射
    private Map<Integer, DoublyCircularLinkedList> freqToList; // 频率到双向链表的映射
    
    /**
     * 构造函数
     * @param capacity 缓存容量
     */
    public LeetCode_460_LFUCache(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.minFreq = 0;
        this.keyToNode = new HashMap<>();
        this.freqToList = new HashMap<>();
    }
    
    /**
     * 获取缓存值
     * 时间复杂度：O(1)
     * @param key 键
     * @return 值，如果不存在返回-1
     */
    public int get(int key) {
        if (!keyToNode.containsKey(key)) {
            return -1;
        }
        
        Node node = keyToNode.get(key);
        // 更新节点频率
        updateNode(node);
        return node.value;
    }
    
    /**
     * 添加缓存值
     * 时间复杂度：O(1)
     * @param key 键
     * @param value 值
     */
    public void put(int key, int value) {
        if (capacity == 0) {
            return;
        }
        
        if (keyToNode.containsKey(key)) {
            // 如果键已存在，更新值并增加频率
            Node node = keyToNode.get(key);
            node.value = value;
            updateNode(node);
        } else {
            // 如果键不存在
            if (size == capacity) {
                // 如果缓存已满，删除最不经常使用的节点
                DoublyCircularLinkedList minFreqList = freqToList.get(minFreq);
                Node deletedNode = minFreqList.removeLast();
                keyToNode.remove(deletedNode.key);
                size--;
            }
            
            // 添加新节点
            Node newNode = new Node(key, value);
            keyToNode.put(key, newNode);
            freqToList.computeIfAbsent(1, k -> new DoublyCircularLinkedList()).addFirst(newNode);
            minFreq = 1; // 新节点的频率为1，所以最小频率更新为1
            size++;
        }
    }
    
    /**
     * 更新节点频率
     * @param node 节点
     */
    private void updateNode(Node node) {
        // 从当前频率的链表中删除节点
        DoublyCircularLinkedList oldList = freqToList.get(node.freq);
        oldList.remove(node);
        
        // 如果当前频率等于最小频率且该频率的链表为空，更新最小频率
        if (node.freq == minFreq && oldList.isEmpty()) {
            minFreq++;
        }
        
        // 增加节点频率
        node.freq++;
        
        // 将节点添加到新频率的链表中
        freqToList.computeIfAbsent(node.freq, k -> new DoublyCircularLinkedList()).addFirst(node);
    }
    
    /**
     * 方法2：使用TreeSet优化的实现
     * 时间复杂度：O(log n)
     * 空间复杂度：O(n)
     */
    static class LFUCacheTreeSet {
        private int capacity;
        private int timestamp;
        private Map<Integer, int[]> keyToValue; // [value, freq, timestamp]
        private TreeSet<int[]> freqTimeSet; // [freq, timestamp, key]
        private Map<Integer, int[]> keyToEntry; // 键到TreeSet条目的映射
        
        public LFUCacheTreeSet(int capacity) {
            this.capacity = capacity;
            this.timestamp = 0;
            this.keyToValue = new HashMap<>();
            this.freqTimeSet = new TreeSet<>((a, b) -> {
                if (a[0] != b[0]) return Integer.compare(a[0], b[0]); // 按频率排序
                return Integer.compare(a[1], b[1]); // 按时间戳排序
            });
            this.keyToEntry = new HashMap<>();
        }
        
        public int get(int key) {
            if (!keyToValue.containsKey(key)) {
                return -1;
            }
            
            int[] valueEntry = keyToValue.get(key);
            int value = valueEntry[0];
            int freq = valueEntry[1];
            int time = valueEntry[2];
            
            // 删除旧条目
            int[] oldEntry = keyToEntry.get(key);
            freqTimeSet.remove(oldEntry);
            
            // 更新频率和时间戳
            timestamp++;
            int[] newEntry = {freq + 1, timestamp, key};
            freqTimeSet.add(newEntry);
            keyToEntry.put(key, newEntry);
            keyToValue.put(key, new int[]{value, freq + 1, timestamp});
            
            return value;
        }
        
        public void put(int key, int value) {
            if (capacity == 0) return;
            
            if (keyToValue.containsKey(key)) {
                // 更新现有键
                int[] valueEntry = keyToValue.get(key);
                int freq = valueEntry[1];
                int time = valueEntry[2];
                
                // 删除旧条目
                int[] oldEntry = keyToEntry.get(key);
                freqTimeSet.remove(oldEntry);
                
                // 更新频率和时间戳
                timestamp++;
                int[] newEntry = {freq + 1, timestamp, key};
                freqTimeSet.add(newEntry);
                keyToEntry.put(key, newEntry);
                keyToValue.put(key, new int[]{value, freq + 1, timestamp});
            } else {
                // 添加新键
                if (keyToValue.size() == capacity) {
                    // 删除最不经常使用的键
                    int[] toRemove = freqTimeSet.first();
                    freqTimeSet.remove(toRemove);
                    keyToValue.remove(toRemove[2]);
                    keyToEntry.remove(toRemove[2]);
                }
                
                timestamp++;
                int[] newEntry = {1, timestamp, key};
                freqTimeSet.add(newEntry);
                keyToEntry.put(key, newEntry);
                keyToValue.put(key, new int[]{value, 1, timestamp});
            }
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 460. LFU缓存 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        LeetCode_460_LFUCache lfuCache = new LeetCode_460_LFUCache(2);
        
        lfuCache.put(1, 1);
        lfuCache.put(2, 2);
        System.out.println("put(1,1), put(2,2)");
        
        int result1 = lfuCache.get(1);
        System.out.println("get(1) = " + result1 + " (期望: 1)");
        
        lfuCache.put(3, 3);
        System.out.println("put(3,3)");
        
        int result2 = lfuCache.get(2);
        System.out.println("get(2) = " + result2 + " (期望: -1)");
        
        int result3 = lfuCache.get(3);
        System.out.println("get(3) = " + result3 + " (期望: 3)");
        
        lfuCache.put(4, 4);
        System.out.println("put(4,4)");
        
        int result4 = lfuCache.get(1);
        System.out.println("get(1) = " + result4 + " (期望: -1)");
        
        int result5 = lfuCache.get(3);
        System.out.println("get(3) = " + result5 + " (期望: 3)");
        
        int result6 = lfuCache.get(4);
        System.out.println("get(4) = " + result6 + " (期望: 4)");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        LeetCode_460_LFUCache perfCache = new LeetCode_460_LFUCache(1000);
        Random random = new Random(42);
        
        long startTime = System.nanoTime();
        
        // 执行10000次操作
        for (int i = 0; i < 10000; i++) {
            int operation = random.nextInt(3);
            int key = random.nextInt(2000);
            
            if (operation == 0) {
                // get操作
                perfCache.get(key);
            } else {
                // put操作
                int value = random.nextInt(10000);
                perfCache.put(key, value);
            }
        }
        
        long endTime = System.nanoTime();
        System.out.println("10000次操作完成");
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 算法复杂度分析
        System.out.println("\n=== 算法复杂度分析 ===");
        System.out.println("双向循环链表实现:");
        System.out.println("  时间复杂度:");
        System.out.println("    get操作: O(1)");
        System.out.println("    put操作: O(1)");
        System.out.println("  空间复杂度: O(capacity)");
        System.out.println("TreeSet实现:");
        System.out.println("  时间复杂度:");
        System.out.println("    get操作: O(log n)");
        System.out.println("    put操作: O(log n)");
        System.out.println("  空间复杂度: O(n)");
    }
}