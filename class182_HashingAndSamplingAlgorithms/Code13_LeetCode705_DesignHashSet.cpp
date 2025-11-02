/**
 * LeetCode 705. 设计哈希集合 - C++版本
 * 
 * 题目来源：https://leetcode.com/problems/design-hashset/
 * 题目描述：不使用任何内建的哈希表库设计一个哈希集合（HashSet）。
 * 
 * 算法思路：
 * 1. 使用链地址法解决哈希冲突
 * 2. 选择合适的哈希函数和桶大小
 * 3. 实现动态扩容机制
 * 4. 处理边界情况和异常
 * 
 * 时间复杂度：平均O(1)，最坏O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 动态扩容策略
 * - 哈希函数设计
 * - 内存管理优化
 * - 异常安全处理
 */

#include <iostream>
#include <vector>
#include <list>
#include <algorithm>
#include <chrono>
#include <stdexcept>
#include <cmath>

using namespace std;

/**
 * 哈希集合实现类
 */
class MyHashSet {
private:
    // 默认初始容量
    static const int DEFAULT_CAPACITY = 16;
    // 负载因子阈值
    static const double LOAD_FACTOR;
    
    // 桶数组，每个桶是一个链表
    vector<list<int>> buckets;
    // 当前元素数量
    int size;
    // 当前容量
    int capacity;
    
public:
    /**
     * 构造函数：使用默认容量初始化哈希集合
     */
    MyHashSet() : MyHashSet(DEFAULT_CAPACITY) {}
    
    /**
     * 构造函数：使用指定容量初始化哈希集合
     * 
     * @param initialCapacity 初始容量
     */
    MyHashSet(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw invalid_argument("初始容量必须大于0");
        }
        capacity = initialCapacity;
        size = 0;
        buckets.resize(capacity);
    }
    
    /**
     * 哈希函数：计算元素的哈希值
     * 
     * @param key 元素值
     * @return 哈希值（桶索引）
     */
    int hash(int key) const {
        // 使用标准库的hash函数并取模
        return hash<int>{}(key) % capacity;
    }
    
    /**
     * 添加元素到哈希集合
     * 
     * @param key 要添加的元素
     */
    void add(int key) {
        // 检查是否需要扩容
        if (static_cast<double>(size) / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        list<int>& bucket = buckets[index];
        
        // 检查元素是否已存在
        if (find(bucket.begin(), bucket.end(), key) == bucket.end()) {
            bucket.push_back(key);
            size++;
        }
    }
    
    /**
     * 从哈希集合中移除元素
     * 
     * @param key 要移除的元素
     */
    void remove(int key) {
        int index = hash(key);
        list<int>& bucket = buckets[index];
        
        auto it = find(bucket.begin(), bucket.end(), key);
        if (it != bucket.end()) {
            bucket.erase(it);
            size--;
        }
    }
    
    /**
     * 检查哈希集合是否包含指定元素
     * 
     * @param key 要检查的元素
     * @return 如果包含返回true，否则返回false
     */
    bool contains(int key) const {
        int index = hash(key);
        const list<int>& bucket = buckets[index];
        return find(bucket.begin(), bucket.end(), key) != bucket.end();
    }
    
    /**
     * 获取哈希集合的大小
     * 
     * @return 元素数量
     */
    int getSize() const {
        return size;
    }
    
    /**
     * 检查哈希集合是否为空
     * 
     * @return 如果为空返回true，否则返回false
     */
    bool isEmpty() const {
        return size == 0;
    }
    
    /**
     * 清空哈希集合
     */
    void clear() {
        for (auto& bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }
    
private:
    /**
     * 动态扩容：当负载因子超过阈值时扩容
     */
    void resize() {
        int newCapacity = capacity * 2;
        vector<list<int>> newBuckets(newCapacity);
        
        // 重新哈希所有元素
        for (const auto& bucket : buckets) {
            for (int key : bucket) {
                int newIndex = hash<int>{}(key) % newCapacity;
                newBuckets[newIndex].push_back(key);
            }
        }
        
        // 更新容量和桶数组
        capacity = newCapacity;
        buckets = move(newBuckets);
    }
};

// 静态成员初始化
const double MyHashSet::LOAD_FACTOR = 0.75;

/**
 * 优化版本：使用更好的哈希函数和更高效的数据结构
 */
class MyHashSetOptimized {
private:
    static const int DEFAULT_CAPACITY = 16;
    static const double LOAD_FACTOR;
    
    // 使用vector代替list，提高缓存局部性
    vector<vector<int>> buckets;
    int size;
    int capacity;
    
public:
    MyHashSetOptimized() : MyHashSetOptimized(DEFAULT_CAPACITY) {}
    
    MyHashSetOptimized(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw invalid_argument("初始容量必须大于0");
        }
        capacity = initialCapacity;
        size = 0;
        buckets.resize(capacity);
    }
    
    /**
     * 优化的哈希函数：使用乘法哈希法
     */
    int hash(int key) const {
        // 使用黄金分割率的倒数作为乘数
        const double A = (sqrt(5) - 1) / 2;
        double fractionalPart = fmod(key * A, 1.0);
        return static_cast<int>(fractionalPart * capacity);
    }
    
    void add(int key) {
        if (static_cast<double>(size) / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        vector<int>& bucket = buckets[index];
        
        // 使用二分查找检查元素是否存在（桶内元素有序）
        auto it = lower_bound(bucket.begin(), bucket.end(), key);
        if (it == bucket.end() || *it != key) {
            // 插入到正确位置保持有序
            bucket.insert(it, key);
            size++;
        }
    }
    
    void remove(int key) {
        int index = hash(key);
        vector<int>& bucket = buckets[index];
        
        auto it = lower_bound(bucket.begin(), bucket.end(), key);
        if (it != bucket.end() && *it == key) {
            bucket.erase(it);
            size--;
        }
    }
    
    bool contains(int key) const {
        int index = hash(key);
        const vector<int>& bucket = buckets[index];
        auto it = lower_bound(bucket.begin(), bucket.end(), key);
        return it != bucket.end() && *it == key;
    }
    
    int getSize() const {
        return size;
    }
    
    bool isEmpty() const {
        return size == 0;
    }
    
    void clear() {
        for (auto& bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }
    
private:
    void resize() {
        int newCapacity = capacity * 2;
        vector<vector<int>> newBuckets(newCapacity);
        
        for (const auto& bucket : buckets) {
            for (int key : bucket) {
                int newIndex = hash(key);
                vector<int>& newBucket = newBuckets[newIndex];
                auto it = lower_bound(newBucket.begin(), newBucket.end(), key);
                if (it == newBucket.end() || *it != key) {
                    newBucket.insert(it, key);
                }
            }
        }
        
        capacity = newCapacity;
        buckets = move(newBuckets);
    }
};

// 静态成员初始化
const double MyHashSetOptimized::LOAD_FACTOR = 0.75;

/**
 * 测试函数
 */
void testBasicVersion() {
    cout << "=== 基础版本测试 ===" << endl;
    MyHashSet hashSet;
    
    // 基本操作测试
    hashSet.add(1);
    hashSet.add(2);
    hashSet.add(3);
    
    cout << "添加1,2,3后大小: " << hashSet.getSize() << endl;
    cout << "包含2: " << hashSet.contains(2) << endl;
    cout << "包含4: " << hashSet.contains(4) << endl;
    
    hashSet.remove(2);
    cout << "删除2后包含2: " << hashSet.contains(2) << endl;
    
    // 重复添加测试
    hashSet.add(1);
    cout << "重复添加1后大小: " << hashSet.getSize() << endl;
    
    hashSet.clear();
    cout << "清空后大小: " << hashSet.getSize() << endl;
    cout << "是否为空: " << hashSet.isEmpty() << endl;
}

void testOptimizedVersion() {
    cout << "=== 优化版本测试 ===" << endl;
    MyHashSetOptimized hashSet;
    
    // 基本操作测试
    hashSet.add(10);
    hashSet.add(20);
    hashSet.add(30);
    hashSet.add(5);
    hashSet.add(15);
    
    cout << "添加多个元素后大小: " << hashSet.getSize() << endl;
    cout << "包含15: " << hashSet.contains(15) << endl;
    cout << "包含25: " << hashSet.contains(25) << endl;
    
    hashSet.remove(20);
    cout << "删除20后包含20: " << hashSet.contains(20) << endl;
    
    // 测试有序性
    hashSet.add(8);
    hashSet.add(18);
    hashSet.add(28);
    cout << "添加无序元素后操作正常" << endl;
}

void performanceTest() {
    cout << "=== 性能对比测试 ===" << endl;
    int testSize = 10000;
    
    // 基础版本性能测试
    auto startTime = chrono::high_resolution_clock::now();
    MyHashSet basicSet;
    for (int i = 0; i < testSize; i++) {
        basicSet.add(i);
    }
    for (int i = 0; i < testSize; i++) {
        basicSet.contains(i);
    }
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "基础版本耗时: " << duration.count() << "ms" << endl;
    
    // 优化版本性能测试
    startTime = chrono::high_resolution_clock::now();
    MyHashSetOptimized optimizedSet;
    for (int i = 0; i < testSize; i++) {
        optimizedSet.add(i);
    }
    for (int i = 0; i < testSize; i++) {
        optimizedSet.contains(i);
    }
    endTime = chrono::high_resolution_clock::now();
    duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "优化版本耗时: " << duration.count() << "ms" << endl;
}

void edgeCaseTest() {
    cout << "=== 边界情况测试 ===" << endl;
    
    // 边界值测试
    MyHashSet hashSet(1); // 最小容量
    
    // 大量重复操作
    for (int i = 0; i < 100; i++) {
        hashSet.add(1);
    }
    cout << "重复添加100次1后大小: " << hashSet.getSize() << endl;
    
    // 删除不存在的元素
    hashSet.remove(999);
    cout << "删除不存在的元素后大小: " << hashSet.getSize() << endl;
    
    // 空集合操作
    hashSet.clear();
    cout << "清空后包含1: " << hashSet.contains(1) << endl;
    cout << "清空后是否为空: " << hashSet.isEmpty() << endl;
    
    // 负数和零测试
    hashSet.add(-1);
    hashSet.add(0);
    hashSet.add(INT_MIN);
    hashSet.add(INT_MAX);
    cout << "添加边界值后大小: " << hashSet.getSize() << endl;
    cout << "包含INT_MIN: " << hashSet.contains(INT_MIN) << endl;
    cout << "包含INT_MAX: " << hashSet.contains(INT_MAX) << endl;
}

int main() {
    try {
        testBasicVersion();
        testOptimizedVersion();
        performanceTest();
        edgeCaseTest();
    } catch (const exception& e) {
        cerr << "错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * 复杂度分析：
 * 
 * 时间复杂度：
 * - 添加操作(add): 平均O(1)，最坏O(n)（当所有元素哈希到同一个桶时）
 * - 删除操作(remove): 平均O(1)，最坏O(n)
 * - 查询操作(contains): 平均O(1)，最坏O(n)
 * - 扩容操作(resize): O(n)
 * 
 * 空间复杂度：
 * - 总空间：O(n + m)，其中n是元素数量，m是桶的数量
 * - 每个桶需要额外空间存储链表节点
 * 
 * 算法优化点：
 * 1. 链地址法：简单有效，易于实现
 * 2. 动态扩容：避免哈希冲突过多
 * 3. 负载因子控制：平衡空间和时间效率
 * 4. 优化版本使用有序桶：提高查找效率
 * 
 * 边界情况处理：
 * - 空集合操作
 * - 重复元素添加
 * - 删除不存在的元素
 * - 边界值（最小/最大整数）
 * - 初始容量验证
 * 
 * 工程化考量：
 * - 参数可配置性（容量、负载因子）
 * - 异常处理
 * - 内存管理
 * - 性能监控
 * 
 * C++特定优化：
 * - 使用标准库的hash函数
 * - 使用move语义提高性能
 * - 使用const引用避免拷贝
 * - 使用异常安全的设计
 * 
 * 实际应用场景：
 * 1. 数据库索引：快速查找记录
 * 2. 缓存系统：存储热点数据
 * 3. 编译器：符号表管理
 * 4. 网络路由：快速查找路由表
 */