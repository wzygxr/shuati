package class034;

import java.util.HashMap;
import java.util.Map;

// LRU缓存设计（使用双向链表）
// 测试链接：https://leetcode.cn/problems/lru-cache/
public class Code45_LRUCacheDesign {

    /**
     * LRU缓存实现
     * 使用双向链表 + 哈希表实现O(1)时间复杂度的get和put操作
     * 
     * 设计思路：
     * 1. 使用双向链表维护访问顺序，最近访问的节点在链表头部
     * 2. 使用哈希表实现O(1)的键值查找
     * 3. 当缓存达到容量时，淘汰链表尾部的节点
     * 
     * 时间复杂度：
     * - get操作：O(1)
     * - put操作：O(1)
     * 
     * 空间复杂度：O(capacity)
     * 是否最优解：是
     */
    public static class LRUCache {
        
        // 双向链表节点
        class DLinkedNode {
            int key;
            int value;
            DLinkedNode prev;
            DLinkedNode next;
            
            public DLinkedNode() {}
            public DLinkedNode(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }
        
        private Map<Integer, DLinkedNode> cache;
        private int capacity;
        private int size;
        private DLinkedNode head; // 虚拟头节点
        private DLinkedNode tail; // 虚拟尾节点
        
        public LRUCache(int capacity) {
            this.capacity = capacity;
            this.size = 0;
            this.cache = new HashMap<>();
            
            // 初始化虚拟头尾节点
            this.head = new DLinkedNode();
            this.tail = new DLinkedNode();
            head.next = tail;
            tail.prev = head;
        }
        
        /**
         * 获取缓存值
         * @param key 键
         * @return 值，如果不存在返回-1
         */
        public int get(int key) {
            DLinkedNode node = cache.get(key);
            if (node == null) {
                return -1;
            }
            
            // 将访问的节点移动到头部
            moveToHead(node);
            return node.value;
        }
        
        /**
         * 插入缓存值
         * @param key 键
         * @param value 值
         */
        public void put(int key, int value) {
            DLinkedNode node = cache.get(key);
            
            if (node == null) {
                // 键不存在，创建新节点
                DLinkedNode newNode = new DLinkedNode(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
                size++;
                
                if (size > capacity) {
                    // 超出容量，删除尾部节点
                    DLinkedNode tailNode = removeTail();
                    cache.remove(tailNode.key);
                    size--;
                }
            } else {
                // 键已存在，更新值并移动到头部
                node.value = value;
                moveToHead(node);
            }
        }
        
        /**
         * 添加节点到头部
         * @param node 要添加的节点
         */
        private void addToHead(DLinkedNode node) {
            node.prev = head;
            node.next = head.next;
            head.next.prev = node;
            head.next = node;
        }
        
        /**
         * 删除节点
         * @param node 要删除的节点
         */
        private void removeNode(DLinkedNode node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        
        /**
         * 移动节点到头部
         * @param node 要移动的节点
         */
        private void moveToHead(DLinkedNode node) {
            removeNode(node);
            addToHead(node);
        }
        
        /**
         * 删除尾部节点
         * @return 被删除的节点
         */
        private DLinkedNode removeTail() {
            DLinkedNode tailNode = tail.prev;
            removeNode(tailNode);
            return tailNode;
        }
    }
    
    /*
     * 题目扩展：LeetCode 146. LRU缓存
     * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
     * 
     * 题目描述：
     * 设计并实现一个满足 LRU (最近最少使用) 缓存约束的数据结构。
     * 
     * 实现 LRUCache 类：
     * - LRUCache(int capacity) 以正整数作为容量 capacity 初始化 LRU 缓存
     * - int get(int key) 如果关键字 key 存在于缓存中，则返回关键字的值，否则返回 -1
     * - void put(int key, int value) 如果关键字 key 已经存在，则变更其数据值 value ；
     *   如果不存在，则向缓存中插入该组 key-value 。如果插入操作导致关键字数量超过 capacity ，
     *   则应该逐出最久未使用的关键字。
     * 
     * 函数 get 和 put 必须以 O(1) 的平均时间复杂度运行。
     * 
     * 解题思路：
     * 1. 使用双向链表维护访问顺序
     * 2. 使用哈希表实现快速查找
     * 3. 维护虚拟头尾节点简化边界处理
     * 
     * 时间复杂度：
     * - get操作：O(1)
     * - put操作：O(1)
     * 
     * 空间复杂度：O(capacity)
     * 是否最优解：是
     * 
     * 工程化考量：
     * 1. 线程安全：在多线程环境下需要加锁
     * 2. 内存管理：合理设置缓存容量
     * 3. 异常处理：处理非法输入
     * 
     * 与机器学习等领域的联系：
     * 1. 在缓存系统中，LRU是常用淘汰策略
     * 2. 在数据库系统中，用于页面置换
     * 3. 在操作系统中，用于内存页面管理
     * 
     * 语言特性差异：
     * Java: 使用LinkedHashMap可以简化实现
     * C++: 使用list和unordered_map组合
     * Python: 使用OrderedDict简化实现
     * 
     * 极端输入场景：
     * 1. 容量为0
     * 2. 大量重复操作
     * 3. 键值对数量很大
     * 4. 并发访问
     * 
     * 反直觉但关键的设计：
     * 1. 虚拟头尾节点：简化边界处理
     * 2. 双向链表：支持O(1)的节点删除
     * 3. 哈希表：支持O(1)的查找
     * 
     * 工程选择依据：
     * 1. 性能要求：O(1)时间复杂度
     * 2. 内存效率：合理使用数据结构
     * 3. 代码可维护性：清晰的代码结构
     * 
     * 异常防御：
     * 1. 容量校验
     * 2. 空指针检查
     * 3. 并发控制
     * 
     * 单元测试要点：
     * 1. 测试基本操作
     * 2. 测试容量淘汰
     * 3. 测试边界情况
     * 4. 测试性能
     * 
     * 性能优化策略：
     * 1. 使用合适的数据结构
     * 2. 避免不必要的操作
     * 3. 优化内存使用
     * 
     * 算法安全与业务适配：
     * 1. 避免内存泄漏
     * 2. 处理并发冲突
     * 3. 合理设置超时策略
     * 
     * 与标准库实现的对比：
     * 1. Java的LinkedHashMap已经实现了LRU功能
     * 2. 自定义实现更灵活，可以定制淘汰策略
     * 3. 标准库实现通常更稳定
     * 
     * 笔试解题效率：
     * 1. 模板化：掌握LRU实现的通用模板
     * 2. 数据结构选择：理解双向链表和哈希表的组合
     * 3. 边界处理：熟练处理各种边界情况
     * 
     * 面试深度表达：
     * 1. 解释设计思路：为什么选择双向链表+哈希表
     * 2. 分析复杂度：时间和空间复杂度分析
     * 3. 讨论变种：LFU等其他缓存策略
     * 4. 实际应用：LRU在工程中的应用场景
     */
}