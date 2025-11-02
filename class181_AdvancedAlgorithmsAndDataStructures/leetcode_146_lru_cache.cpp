#include <iostream>
#include <unordered_map>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 146. LRU缓存机制 (LRU Cache) - C++版本
 * 
 * 解题思路：
 * 使用双向循环链表 + 哈希表实现LRU缓存
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(capacity)
 */
class LRUCache {
private:
    // 双向链表节点
    struct DLinkedNode {
        int key;
        int value;
        DLinkedNode* prev;
        DLinkedNode* next;
        
        DLinkedNode() : key(0), value(0), prev(nullptr), next(nullptr) {}
        DLinkedNode(int k, int v) : key(k), value(v), prev(nullptr), next(nullptr) {}
    };
    
    unordered_map<int, DLinkedNode*> cache;
    int size;
    int capacity;
    DLinkedNode* head;
    DLinkedNode* tail;
    
    void addToHead(DLinkedNode* node) {
        node->prev = head;
        node->next = head->next;
        head->next->prev = node;
        head->next = node;
    }
    
    void removeNode(DLinkedNode* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }
    
    void moveToHead(DLinkedNode* node) {
        removeNode(node);
        addToHead(node);
    }
    
    DLinkedNode* removeTail() {
        DLinkedNode* node = tail->prev;
        removeNode(node);
        return node;
    }
    
public:
    LRUCache(int capacity) : size(0), capacity(capacity) {
        // 使用伪头部和伪尾部节点
        head = new DLinkedNode();
        tail = new DLinkedNode();
        head->next = tail;
        tail->prev = head;
    }
    
    int get(int key) {
        if (!cache.count(key)) {
            return -1;
        }
        
        // 如果key存在，先通过哈希表定位，再移到头部
        DLinkedNode* node = cache[key];
        moveToHead(node);
        return node->value;
    }
    
    void put(int key, int value) {
        if (!cache.count(key)) {
            // 如果key不存在，创建一个新的节点
            DLinkedNode* node = new DLinkedNode(key, value);
            
            // 添加进哈希表
            cache[key] = node;
            
            // 添加至双向链表的头部
            addToHead(node);
            size++;
            
            if (size > capacity) {
                // 如果超出容量，删除双向链表的尾部节点
                DLinkedNode* removed = removeTail();
                
                // 删除哈希表中对应的项
                cache.erase(removed->key);
                
                // 防止内存泄漏
                delete removed;
                size--;
            }
        } else {
            // 如果key存在，先通过哈希表定位，再修改value，并移到头部
            DLinkedNode* node = cache[key];
            node->value = value;
            moveToHead(node);
        }
    }
    
    ~LRUCache() {
        // 清理内存
        DLinkedNode* current = head->next;
        while (current != tail) {
            DLinkedNode* temp = current;
            current = current->next;
            delete temp;
        }
        delete head;
        delete tail;
    }
};

/**
 * 测试LRU缓存实现
 */
void testLRUCache() {
    cout << "=== LeetCode 146. LRU缓存机制 (C++版本) ===" << endl;
    
    // 测试用例1
    cout << "测试用例1:" << endl;
    LRUCache lruCache(2);
    
    lruCache.put(1, 1);
    lruCache.put(2, 2);
    cout << "put(1,1), put(2,2)" << endl;
    
    int result1 = lruCache.get(1);
    cout << "get(1) = " << result1 << " (期望: 1)" << endl;
    
    lruCache.put(3, 3);
    cout << "put(3,3)" << endl;
    
    int result2 = lruCache.get(2);
    cout << "get(2) = " << result2 << " (期望: -1)" << endl;
    
    lruCache.put(4, 4);
    cout << "put(4,4)" << endl;
    
    int result3 = lruCache.get(1);
    cout << "get(1) = " << result3 << " (期望: -1)" << endl;
    
    int result4 = lruCache.get(3);
    cout << "get(3) = " << result4 << " (期望: 3)" << endl;
    
    int result5 = lruCache.get(4);
    cout << "get(4) = " << result5 << " (期望: 4)" << endl;
    cout << endl;
    
    // 性能测试
    cout << "=== 性能测试 ===" << endl;
    LRUCache performanceCache(1000);
    default_random_engine generator(42);
    uniform_int_distribution<int> distribution(0, 2000);
    
    auto startTime = chrono::high_resolution_clock::now();
    
    // 执行10000次操作
    for (int i = 0; i < 10000; i++) {
        int operation = distribution(generator) % 3;
        int key = distribution(generator);
        
        if (operation == 0) {
            // get操作
            performanceCache.get(key);
        } else {
            // put操作
            int value = distribution(generator) % 10000;
            performanceCache.put(key, value);
        }
    }
    
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(endTime - startTime);
    
    cout << "10000次操作完成" << endl;
    cout << "运行时间: " << duration.count() / 1000.0 << " ms" << endl;
    
    // C++语言特性考量
    cout << "\n=== C++语言特性考量 ===" << endl;
    cout << "1. 内存管理：使用析构函数清理内存" << endl;
    cout << "2. 智能指针：可以使用unique_ptr管理节点内存" << endl;
    cout << "3. 模板化：可以模板化支持任意类型" << endl;
    cout << "4. 异常安全：确保异常情况下的资源释放" << endl;
}

int main() {
    testLRUCache();
    return 0;
}