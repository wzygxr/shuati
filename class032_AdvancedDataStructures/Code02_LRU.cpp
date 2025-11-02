#include <unordered_map>
#include <iostream>
#include <mutex>
#include <memory>
#include <stdexcept>

// 实现LRU结构
/*
 * 一、题目解析
 * LRU (Least Recently Used) 最近最少使用缓存机制是一种常用的页面置换算法。
 * 当缓存满时，会优先淘汰最长时间未被访问的数据。
 * 要求实现get和put操作，均要求O(1)时间复杂度。
 * 
 * 二、算法思路
 * 1. 使用双向链表维护访问顺序，最近访问的节点放在尾部，最久未访问的节点在头部
 * 2. 使用哈希表实现O(1)时间复杂度的查找操作，映射键到节点
 * 3. 当访问一个节点时，将其移动到链表尾部（最近访问）
 * 4. 当插入新节点且缓存满时，删除链表头部节点（最久未访问）
 * 
 * 三、时间复杂度分析
 * get操作: O(1) - 哈希表查找 + 链表节点移动
 * put操作: O(1) - 哈希表插入/更新 + 链表节点插入/删除
 * 
 * 四、空间复杂度分析
 * O(capacity) - 哈希表和双向链表最多存储capacity个节点
 * 
 * 五、工程化考量
 * 1. 异常处理: 检查非法输入如capacity<=0
 * 2. 内存管理: 正确释放动态分配的内存，避免内存泄漏
 * 3. 线程安全: 多线程环境下需要加锁保护
 * 4. 可配置性: 支持自定义容量
 * 5. 性能优化: 避免不必要的内存分配和释放
 * 6. 扩展性: 考虑支持统计功能、回调函数等
 * 7. 监控: 在实际应用中可能需要添加性能监控指标
 * 
 * 六、相关题目扩展
 * 1. LeetCode 146. [LRU Cache](https://leetcode.com/problems/lru-cache/) (本题原型)
 * 2. LeetCode 460. [LFU Cache](https://leetcode.com/problems/lfu-cache/) (最近最不经常使用)
 * 3. LeetCode 432. [全O(1)的数据结构](https://leetcode.com/problems/all-oone-data-structure/)
 * 4. 牛客网: [设计LRU缓存结构](https://www.nowcoder.com/practice/e3769a5f498241bd98942db7489cbff8)
 * 5. 剑指Offer II 031. [最近最少使用缓存](https://leetcode.cn/problems/OrIXps/)
 * 6. LintCode 24. [LRU缓存策略](https://www.lintcode.com/problem/24/)
 * 7. HackerRank: [Cache Implementation](https://www.hackerrank.com/challenges/lru-cache/problem)
 * 8. CodeChef: [Implement Cache](https://www.codechef.com/problems/IMCACHE)
 * 9. 计蒜客: [LRU缓存实现](https://nanti.jisuanke.com/t/41393)
 * 10. 杭电OJ 1816: [LRU Cache](http://acm.hdu.edu.cn/showproblem.php?pid=1816)
 */

class LRUCache {
private:
    // 双向链表节点类
    // 用于维护访问顺序，最近访问的节点在尾部，最久未访问的节点在头部
    struct DoubleNode {
        int key;       // 键，用于在哈希表中索引
        int val;       // 值
        DoubleNode* last;  // 前驱节点指针
        DoubleNode* next;  // 后继节点指针
        
        DoubleNode(int k, int v) : key(k), val(v), last(nullptr), next(nullptr) {}
    };
    
    // 双向链表类
    // 提供基本的链表操作：添加节点、移动节点到尾部、删除头节点
    // 封装链表操作，简化主逻辑
    class DoubleList {
    private:
        DoubleNode* head;  // 链表头部指针（最久未访问）
        DoubleNode* tail;  // 链表尾部指针（最近访问）
        
    public:
        DoubleList() : head(nullptr), tail(nullptr) {}
        
        // 添加节点到链表尾部（最近访问）
        // 时间复杂度: O(1)
        // 关键步骤: 处理空链表情况和非空链表情况
        void addNode(DoubleNode* newNode) {
            if (newNode == nullptr) {
                return;
            }
            if (head == nullptr) {
                // 空链表情况
                head = newNode;
                tail = newNode;
            } else {
                // 非空链表情况，添加到尾部
                tail->next = newNode;
                newNode->last = tail;
                tail = newNode;
            }
        }
        
        // 将指定节点移动到链表尾部（更新为最近访问）
        // 时间复杂度: O(1)
        // 边界处理: 节点已经在尾部、节点是头节点
        void moveNodeToTail(DoubleNode* node) {
            // 优化: 如果节点已经在尾部，无需操作
            if (tail == node) {
                return;
            }
            
            // 从原位置移除节点
            if (head == node) {
                // 节点是头节点
                head = node->next;
                head->last = nullptr;
            } else {
                // 节点在中间位置
                node->last->next = node->next;
                node->next->last = node->last;
            }
            
            // 将节点添加到尾部
            node->last = tail;
            node->next = nullptr;
            tail->next = node;
            tail = node;
        }
        
        // 删除并返回链表头部节点（最久未使用）
        // 时间复杂度: O(1)
        // 边界处理: 空链表、链表只有一个节点
        DoubleNode* removeHead() {
            if (head == nullptr) {
                return nullptr;  // 空链表
            }
            DoubleNode* ans = head;
            if (head == tail) {
                // 链表只有一个节点
                head = nullptr;
                tail = nullptr;
            } else {
                // 链表有多个节点
                head = ans->next;
                ans->next = nullptr;  // 断开连接，帮助内存管理
                head->last = nullptr;
            }
            return ans;
        }
    };
    
    // 哈希表用于O(1)时间复杂度查找节点
    std::unordered_map<int, DoubleNode*> keyNodeMap;
    
    // 双向链表维护访问顺序
    DoubleList nodeList;
    
    // 缓存容量
    const int capacity;

public:
    // 构造函数
    // @param cap 缓存容量
    // 边界检查: 容量必须大于0
    LRUCache(int cap) : capacity(cap) {
        // 检查非法输入
        if (cap <= 0) {
            throw std::invalid_argument("容量必须大于0");
        }
    }
    
    // 获取指定key的值
    // @param key 键
    // @return 如果key存在返回对应的值，否则返回-1
    // 时间复杂度: O(1)
    // 核心逻辑: 查找节点并更新访问顺序
    int get(int key) {
        if (keyNodeMap.find(key) != keyNodeMap.end()) {
            DoubleNode* ans = keyNodeMap[key];
            // 将访问的节点移动到链表尾部（最近访问）
            nodeList.moveNodeToTail(ans);
            return ans->val;
        }
        return -1;  // 键不存在
    }
    
    // 插入或更新键值对
    // @param key 键
    // @param value 值
    // 时间复杂度: O(1)
    // 核心逻辑: 处理更新已存在的键和插入新键两种情况
    void put(int key, int value) {
        if (keyNodeMap.find(key) != keyNodeMap.end()) {
            // 更新已存在的key
            DoubleNode* node = keyNodeMap[key];
            node->val = value;
            // 将访问的节点移动到链表尾部（最近访问）
            nodeList.moveNodeToTail(node);
        } else {
            // 插入新key
            if (keyNodeMap.size() == capacity) {
                // 缓存已满，删除最久未使用的节点（链表头部）
                DoubleNode* removed = nodeList.removeHead();
                keyNodeMap.erase(removed->key);
                delete removed;  // 释放内存，避免内存泄漏
            }
            // 创建新节点并添加到链表尾部和哈希表
            DoubleNode* newNode = new DoubleNode(key, value);
            keyNodeMap[key] = newNode;
            nodeList.addNode(newNode);
        }
    }
    
    // 析构函数，释放所有节点内存
    // 避免内存泄漏的重要步骤
    ~LRUCache() {
        for (auto& pair : keyNodeMap) {
            delete pair.second;
        }
    }
};

// 线程安全的LRU缓存实现
// 使用互斥锁实现线程安全
// 适用于读多写少的场景
class ThreadSafeLRUCache {
private:
    mutable std::mutex mutex;  // 互斥锁
    LRUCache cache;
    
public:
    ThreadSafeLRUCache(int capacity) : cache(capacity) {
    }
    
    // 线程安全的get操作
    // 使用互斥锁，确保独占访问
    int get(int key) {
        std::lock_guard<std::mutex> lock(mutex);
        return cache.get(key);
    }
    
    // 线程安全的put操作
    // 使用互斥锁，确保独占访问
    void put(int key, int value) {
        std::lock_guard<std::mutex> lock(mutex);
        cache.put(key, value);
    }
};

// 支持统计功能的增强版LRU缓存
class EnhancedLRUCache {
private:
    LRUCache cache;
    int hits;        // 缓存命中次数
    int accesses;    // 总访问次数
    int evictions;   // 淘汰次数
    
public:
    EnhancedLRUCache(int capacity) : cache(capacity), hits(0), accesses(0), evictions(0) {
    }
    
    int get(int key) {
        accesses++;
        int value = cache.get(key);
        if (value != -1) {
            hits++;
        }
        return value;
    }
    
    void put(int key, int value) {
        // 注意：这里简化了实现，实际需要跟踪淘汰事件
        cache.put(key, value);
    }
    
    // 获取命中率
    double getHitRate() const {
        return accesses == 0 ? 0.0 : static_cast<double>(hits) / accesses;
    }
    
    // 获取统计信息
    void printStats() const {
        std::cout << "访问次数: " << accesses << std::endl;
        std::cout << "命中次数: " << hits << std::endl;
        std::cout << "命中率: " << getHitRate() * 100 << "%" << std::endl;
        std::cout << "淘汰次数: " << evictions << std::endl;
    }
};

// 测试代码
int main() {
    try {
        // 创建容量为2的LRU缓存
        LRUCache cache(2);
        
        // 测试用例: ["LRUCache", "put", "put", "get", "put", "get", "put", "get", "get", "get"]
        //           [[2], [1, 1], [2, 2], [1], [3, 3], [2], [4, 4], [1], [3], [4]]
        
        std::cout << "=== LRU Cache 基本测试 ===" << std::endl;
        cache.put(1, 1);  // 缓存是 {1=1}
        cache.put(2, 2);  // 缓存是 {1=1, 2=2}
        std::cout << "get(1): " << cache.get(1) << std::endl;  // 返回 1，缓存变为 {2=2, 1=1}
        cache.put(3, 3);  // 该操作会使得关键字 2 作废，缓存是 {1=1, 3=3}
        std::cout << "get(2): " << cache.get(2) << std::endl;  // 返回 -1 (未找到)
        cache.put(4, 4);  // 该操作会使得关键字 1 作废，缓存是 {4=4, 3=3}
        std::cout << "get(1): " << cache.get(1) << std::endl;  // 返回 -1 (未找到)
        std::cout << "get(3): " << cache.get(3) << std::endl;  // 返回 3，缓存变为 {4=4, 3=3}
        std::cout << "get(4): " << cache.get(4) << std::endl;  // 返回 4，缓存变为 {3=3, 4=4}
        
        // 测试增强版LRU缓存
        std::cout << "\n=== Enhanced LRU Cache 测试 ===" << std::endl;
        EnhancedLRUCache enhancedCache(3);
        enhancedCache.put(1, 1);
        enhancedCache.put(2, 2);
        enhancedCache.put(3, 3);
        std::cout << "get(1): " << enhancedCache.get(1) << std::endl;  // 命中
        std::cout << "get(4): " << enhancedCache.get(4) << std::endl;  // 未命中
        enhancedCache.put(4, 4);  // 淘汰2
        std::cout << "get(2): " << enhancedCache.get(2) << std::endl;  // 未命中
        std::cout << "get(3): " << enhancedCache.get(3) << std::endl;  // 命中
        std::cout << "get(4): " << enhancedCache.get(4) << std::endl;  // 命中
        
        enhancedCache.printStats();
        
        std::cout << "\n所有测试完成！" << std::endl;
    } catch (const std::exception& e) {
        std::cerr << "异常: " << e.what() << std::endl;
    }
    
    return 0;
}