#include <iostream>
#include <vector>
#include <list>
#include <utility>
#include <iterator>
#include <cmath>

using namespace std;

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
 */

class MyHashMap {
private:
    static const int BASE = 10000; // 桶的数量
    vector<list<pair<int, int>>> data; // 桶数组，每个桶是一个链表
    
    // 哈希函数
    int hash(int key) {
        return key % BASE;
    }
    
public:
    /** Initialize your data structure here. */
    MyHashMap() : data(BASE) {}
    
    /** value will always be non-negative. */
    void put(int key, int value) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); it++) {
            if (it->first == key) {
                it->second = value;
                return;
            }
        }
        data[h].push_back(make_pair(key, value));
    }
    
    /** Returns the value to which the specified key is mapped, or -1 if this map contains no mapping for the key */
    int get(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); it++) {
            if (it->first == key) {
                return it->second;
            }
        }
        return -1;
    }
    
    /** Removes the mapping of the specified value key if this map contains a mapping for the key */
    void remove(int key) {
        int h = hash(key);
        for (auto it = data[h].begin(); it != data[h].end(); it++) {
            if (it->first == key) {
                data[h].erase(it);
                return;
            }
        }
    }
};

/**
 * 优化版本：使用更好的哈希函数和动态扩容
 */
class MyHashMapOptimized {
private:
    static const int DEFAULT_CAPACITY = 16;
    static constexpr double LOAD_FACTOR = 0.75;
    
    vector<list<pair<int, int>>> buckets;
    int size;
    int capacity;
    
    // 哈希函数
    int hash(int key) {
        return abs(key) % capacity;
    }
    
    // 动态扩容
    void resize() {
        int newCapacity = capacity * 2;
        vector<list<pair<int, int>>> newBuckets(newCapacity);
        
        // 重新哈希所有元素
        for (const auto& bucket : buckets) {
            for (const auto& entry : bucket) {
                int newIndex = abs(entry.first) % newCapacity;
                newBuckets[newIndex].push_back(entry);
            }
        }
        
        // 更新容量和桶数组
        capacity = newCapacity;
        buckets = std::move(newBuckets);
    }
    
public:
    MyHashMapOptimized() : MyHashMapOptimized(DEFAULT_CAPACITY) {}
    
    MyHashMapOptimized(int initialCapacity) : size(0), capacity(initialCapacity), buckets(initialCapacity) {}
    
    void put(int key, int value) {
        // 检查是否需要扩容
        if ((double) size / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        auto& bucket = buckets[index];
        
        // 检查键是否已存在
        for (auto& entry : bucket) {
            if (entry.first == key) {
                entry.second = value;
                return;
            }
        }
        
        // 添加新键值对
        bucket.push_back(make_pair(key, value));
        size++;
    }
    
    int get(int key) {
        int index = hash(key);
        auto& bucket = buckets[index];
        
        for (const auto& entry : bucket) {
            if (entry.first == key) {
                return entry.second;
            }
        }
        
        return -1;
    }
    
    void remove(int key) {
        int index = hash(key);
        auto& bucket = buckets[index];
        
        for (auto it = bucket.begin(); it != bucket.end(); it++) {
            if (it->first == key) {
                bucket.erase(it);
                size--;
                return;
            }
        }
    }
    
    int getSize() {
        return size;
    }
    
    bool isEmpty() {
        return size == 0;
    }
    
    void clear() {
        for (auto& bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }
};

/**
 * 测试函数
 */
void testBasicVersion() {
    cout << "--- 基础版本测试 ---" << endl;
    MyHashMap hashMap;
    
    // 测试put和get操作
    hashMap.put(1, 1);
    hashMap.put(2, 2);
    cout << "get(1): " << hashMap.get(1) << endl; // 期望: 1
    cout << "get(3): " << hashMap.get(3) << endl; // 期望: -1
    
    // 测试更新操作
    hashMap.put(2, 1);
    cout << "get(2): " << hashMap.get(2) << endl; // 期望: 1
    
    // 测试删除操作
    hashMap.remove(2);
    cout << "get(2): " << hashMap.get(2) << endl; // 期望: -1
    
    // 测试边界值
    hashMap.put(1000000, 1000000);
    cout << "get(1000000): " << hashMap.get(1000000) << endl; // 期望: 1000000
    hashMap.remove(1000000);
    cout << "get(1000000): " << hashMap.get(1000000) << endl; // 期望: -1
}

void testOptimizedVersion() {
    cout << "\n--- 优化版本测试 ---" << endl;
    MyHashMapOptimized hashMap;
    
    // 测试基本操作
    hashMap.put(1, 10);
    hashMap.put(2, 20);
    hashMap.put(3, 30);
    cout << "大小: " << hashMap.getSize() << endl; // 期望: 3
    cout << "get(2): " << hashMap.get(2) << endl; // 期望: 20
    
    // 测试更新操作
    hashMap.put(2, 25);
    cout << "更新后get(2): " << hashMap.get(2) << endl; // 期望: 25
    
    // 测试删除操作
    hashMap.remove(2);
    cout << "删除后get(2): " << hashMap.get(2) << endl; // 期望: -1
    cout << "删除后大小: " << hashMap.getSize() << endl; // 期望: 2
    
    // 测试清空操作
    hashMap.clear();
    cout << "清空后大小: " << hashMap.getSize() << endl; // 期望: 0
    cout << "清空后是否为空: " << (hashMap.isEmpty() ? "true" : "false") << endl; // 期望: true
}

int main() {
    cout << "=== 测试 LeetCode 706. 设计哈希映射 ===" << endl;
    
    // 基础版本测试
    testBasicVersion();
    
    // 优化版本测试
    testOptimizedVersion();
    
    return 0;
}