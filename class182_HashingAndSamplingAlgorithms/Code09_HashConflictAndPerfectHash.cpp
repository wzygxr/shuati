/**
 * 哈希冲突解决与完美哈希算法实现 - C++版本
 * 
 * 本文件包含高级哈希冲突解决技术和完美哈希算法的C++实现，包括：
 * - 开放寻址法 (Open Addressing)
 * - 链地址法 (Separate Chaining)
 * - 二次探测法 (Quadratic Probing)
 * - 双重哈希法 (Double Hashing)
 * - 完美哈希 (Perfect Hashing)
 * - 布谷鸟哈希 (Cuckoo Hashing)
 * - 跳房子哈希 (Hopscotch Hashing)
 * 
 * 这些算法在哈希表实现、数据库索引、编译器优化等领域有重要应用
 */

#include <iostream>
#include <vector>
#include <functional>
#include <string>
#include <cmath>
#include <random>
#include <algorithm>
#include <memory>
#include <stdexcept>
#include <utility>

using namespace std;

/**
 * 开放寻址法哈希表实现
 * 应用场景：内存受限环境、缓存系统
 * 
 * 算法原理：
 * 1. 所有元素都存储在哈希表数组中
 * 2. 当发生冲突时，按照特定探测序列寻找下一个空槽
 * 3. 常见的探测方法：线性探测、二次探测、双重哈希
 * 
 * 时间复杂度：O(1) 平均，O(n) 最坏
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class OpenAddressingHashTable {
private:
    static const int DEFAULT_CAPACITY = 16;
    static constexpr double DEFAULT_LOAD_FACTOR = 0.75;
    
    struct Entry {
        K key;
        V value;
        bool occupied;
        
        Entry() : occupied(false) {}
        Entry(const K& k, const V& v) : key(k), value(v), occupied(true) {}
    };
    
    vector<Entry> table;
    int size;
    double loadFactor;
    
    // 哈希函数
    size_t hash(const K& key) const {
        return hash<K>{}(key);
    }
    
    // 线性探测
    size_t linearProbe(size_t index, int i) const {
        return (index + i) % table.size();
    }
    
    // 二次探测
    size_t quadraticProbe(size_t index, int i) const {
        return (index + i * i) % table.size();
    }
    
    // 双重哈希
    size_t doubleHash(size_t index, int i) const {
        size_t hash2 = hash<K>{}(index) * 31 + 17;
        return (index + i * hash2) % table.size();
    }
    
    // 查找键的索引位置
    int findKeyIndex(const K& key) const {
        size_t index = hash(key) % table.size();
        
        for (int i = 0; i < table.size(); i++) {
            size_t probeIndex = linearProbe(index, i);
            
            if (!table[probeIndex].occupied) {
                return -1; // 未找到
            }
            
            if (table[probeIndex].key == key) {
                return probeIndex;
            }
        }
        
        return -1;
    }
    
    // 查找空槽位置
    int findSlot(const K& key) {
        size_t index = hash(key) % table.size();
        
        for (int i = 0; i < table.size(); i++) {
            size_t probeIndex = linearProbe(index, i);
            
            if (!table[probeIndex].occupied || table[probeIndex].key == key) {
                return probeIndex;
            }
        }
        
        return -1; // 哈希表已满
    }
    
    // 扩容
    void resize() {
        vector<Entry> oldTable = table;
        table.clear();
        table.resize(oldTable.size() * 2);
        size = 0;
        
        for (const auto& entry : oldTable) {
            if (entry.occupied) {
                put(entry.key, entry.value);
            }
        }
    }
    
public:
    OpenAddressingHashTable() : OpenAddressingHashTable(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR) {}
    
    OpenAddressingHashTable(int capacity, double loadFactor) 
        : table(capacity), size(0), loadFactor(loadFactor) {}
    
    // 插入键值对
    void put(const K& key, const V& value) {
        if ((double)size / table.size() >= loadFactor) {
            resize();
        }
        
        int index = findSlot(key);
        if (index != -1) {
            if (!table[index].occupied) {
                size++;
            }
            table[index] = Entry(key, value);
        }
    }
    
    // 获取值
    V get(const K& key) const {
        int index = findKeyIndex(key);
        return index != -1 ? table[index].value : V();
    }
    
    // 删除键值对
    bool remove(const K& key) {
        int index = findKeyIndex(key);
        if (index != -1) {
            table[index].occupied = false;
            size--;
            return true;
        }
        return false;
    }
    
    // 检查是否包含键
    bool contains(const K& key) const {
        return findKeyIndex(key) != -1;
    }
    
    // 获取大小
    int getSize() const { return size; }
    
    // 获取容量
    int getCapacity() const { return table.size(); }
};

// 静态成员初始化
template<typename K, typename V>
const double OpenAddressingHashTable<K, V>::DEFAULT_LOAD_FACTOR = 0.75;

/**
 * 链地址法哈希表实现
 * 应用场景：通用哈希表实现、数据库索引
 * 
 * 算法原理：
 * 1. 每个哈希桶使用链表存储冲突的元素
 * 2. 插入时计算哈希值，将元素添加到对应链表中
 * 3. 查找时遍历对应链表
 * 
 * 时间复杂度：O(1) 平均，O(n) 最坏
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class SeparateChainingHashTable {
private:
    static const int DEFAULT_CAPACITY = 16;
    static constexpr double DEFAULT_LOAD_FACTOR = 0.75;
    
    struct Node {
        K key;
        V value;
        shared_ptr<Node> next;
        
        Node(const K& k, const V& v) : key(k), value(v), next(nullptr) {}
    };
    
    vector<shared_ptr<Node>> table;
    int size;
    double loadFactor;
    
    // 哈希函数
    size_t hash(const K& key) const {
        return hash<K>{}(key);
    }
    
    // 扩容
    void resize() {
        vector<shared_ptr<Node>> oldTable = table;
        table.clear();
        table.resize(oldTable.size() * 2);
        size = 0;
        
        for (const auto& bucket : oldTable) {
            shared_ptr<Node> current = bucket;
            while (current) {
                put(current->key, current->value);
                current = current->next;
            }
        }
    }
    
public:
    SeparateChainingHashTable() : SeparateChainingHashTable(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR) {}
    
    SeparateChainingHashTable(int capacity, double loadFactor) 
        : table(capacity), size(0), loadFactor(loadFactor) {}
    
    // 插入键值对
    void put(const K& key, const V& value) {
        if ((double)size / table.size() >= loadFactor) {
            resize();
        }
        
        size_t index = hash(key) % table.size();
        shared_ptr<Node> current = table[index];
        
        // 检查是否已存在
        while (current) {
            if (current->key == key) {
                current->value = value;
                return;
            }
            current = current->next;
        }
        
        // 插入新节点
        shared_ptr<Node> newNode = make_shared<Node>(key, value);
        newNode->next = table[index];
        table[index] = newNode;
        size++;
    }
    
    // 获取值
    V get(const K& key) const {
        size_t index = hash(key) % table.size();
        shared_ptr<Node> current = table[index];
        
        while (current) {
            if (current->key == key) {
                return current->value;
            }
            current = current->next;
        }
        
        return V();
    }
    
    // 删除键值对
    bool remove(const K& key) {
        size_t index = hash(key) % table.size();
        shared_ptr<Node> current = table[index];
        shared_ptr<Node> prev = nullptr;
        
        while (current) {
            if (current->key == key) {
                if (prev) {
                    prev->next = current->next;
                } else {
                    table[index] = current->next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current->next;
        }
        
        return false;
    }
    
    // 检查是否包含键
    bool contains(const K& key) const {
        size_t index = hash(key) % table.size();
        shared_ptr<Node> current = table[index];
        
        while (current) {
            if (current->key == key) {
                return true;
            }
            current = current->next;
        }
        
        return false;
    }
    
    // 获取大小
    int getSize() const { return size; }
    
    // 获取容量
    int getCapacity() const { return table.size(); }
};

// 静态成员初始化
template<typename K, typename V>
const double SeparateChainingHashTable<K, V>::DEFAULT_LOAD_FACTOR = 0.75;

/**
 * 布谷鸟哈希实现
 * 应用场景：高性能哈希表、缓存系统
 * 
 * 算法原理：
 * 1. 使用两个哈希函数和两个哈希表
 * 2. 插入时检查两个位置，如果都已被占用，则踢出其中一个元素
 * 3. 被踢出的元素重新插入到另一个哈希表中
 * 4. 重复此过程直到所有元素都找到位置或达到最大迭代次数
 * 
 * 时间复杂度：O(1) 平均，O(n) 最坏
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class CuckooHashTable {
private:
    static const int DEFAULT_CAPACITY = 16;
    static const int MAX_ITERATIONS = 100;
    
    struct Entry {
        K key;
        V value;
        bool occupied;
        
        Entry() : occupied(false) {}
        Entry(const K& k, const V& v) : key(k), value(v), occupied(true) {}
    };
    
    vector<Entry> table1, table2;
    int size;
    
    // 第一个哈希函数
    size_t hash1(const K& key) const {
        return hash<K>{}(key);
    }
    
    // 第二个哈希函数
    size_t hash2(const K& key) const {
        return hash<K>{}(key) * 31 + 17;
    }
    
    // 重新哈希所有元素
    void rehash() {
        vector<Entry> oldTable1 = table1;
        vector<Entry> oldTable2 = table2;
        
        table1.clear();
        table2.clear();
        table1.resize(oldTable1.size() * 2);
        table2.resize(oldTable2.size() * 2);
        size = 0;
        
        for (const auto& entry : oldTable1) {
            if (entry.occupied) {
                put(entry.key, entry.value);
            }
        }
        
        for (const auto& entry : oldTable2) {
            if (entry.occupied) {
                put(entry.key, entry.value);
            }
        }
    }
    
public:
    CuckooHashTable() : CuckooHashTable(DEFAULT_CAPACITY) {}
    
    CuckooHashTable(int capacity) 
        : table1(capacity), table2(capacity), size(0) {}
    
    // 插入键值对
    bool put(const K& key, const V& value) {
        if (contains(key)) {
            // 更新现有值
            size_t index1 = hash1(key) % table1.size();
            if (table1[index1].occupied && table1[index1].key == key) {
                table1[index1].value = value;
                return true;
            }
            
            size_t index2 = hash2(key) % table2.size();
            if (table2[index2].occupied && table2[index2].key == key) {
                table2[index2].value = value;
                return true;
            }
        }
        
        // 插入新值
        Entry newEntry(key, value);
        Entry current = newEntry;
        
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            // 尝试插入第一个表
            size_t index1 = hash1(current.key) % table1.size();
            if (!table1[index1].occupied) {
                table1[index1] = current;
                size++;
                return true;
            }
            
            // 交换并尝试第二个表
            swap(current, table1[index1]);
            
            size_t index2 = hash2(current.key) % table2.size();
            if (!table2[index2].occupied) {
                table2[index2] = current;
                size++;
                return true;
            }
            
            // 交换并继续
            swap(current, table2[index2]);
        }
        
        // 达到最大迭代次数，需要重新哈希
        rehash();
        return put(key, value);
    }
    
    // 获取值
    V get(const K& key) const {
        size_t index1 = hash1(key) % table1.size();
        if (table1[index1].occupied && table1[index1].key == key) {
            return table1[index1].value;
        }
        
        size_t index2 = hash2(key) % table2.size();
        if (table2[index2].occupied && table2[index2].key == key) {
            return table2[index2].value;
        }
        
        return V();
    }
    
    // 删除键值对
    bool remove(const K& key) {
        size_t index1 = hash1(key) % table1.size();
        if (table1[index1].occupied && table1[index1].key == key) {
            table1[index1].occupied = false;
            size--;
            return true;
        }
        
        size_t index2 = hash2(key) % table2.size();
        if (table2[index2].occupied && table2[index2].key == key) {
            table2[index2].occupied = false;
            size--;
            return true;
        }
        
        return false;
    }
    
    // 检查是否包含键
    bool contains(const K& key) const {
        size_t index1 = hash1(key) % table1.size();
        if (table1[index1].occupied && table1[index1].key == key) {
            return true;
        }
        
        size_t index2 = hash2(key) % table2.size();
        if (table2[index2].occupied && table2[index2].key == key) {
            return true;
        }
        
        return false;
    }
    
    // 获取大小
    int getSize() const { return size; }
    
    // 获取容量
    int getCapacity() const { return table1.size() + table2.size(); }
};

/**
 * 完美哈希实现
 * 应用场景：静态数据集、编译器符号表、字典实现
 * 
 * 算法原理：
 * 1. 使用两级哈希表结构
 * 2. 第一级哈希将元素分组到桶中
 * 3. 第二级为每个桶创建无冲突的哈希表
 * 4. 通过调整哈希函数参数确保无冲突
 * 
 * 时间复杂度：O(1) 查找
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class PerfectHashTable {
private:
    static const int DEFAULT_BUCKETS = 16;
    
    struct Bucket {
        vector<pair<K, V>> entries;
        vector<int> hashTable;
        int a, b; // 哈希函数参数
        int size;
        
        Bucket() : a(1), b(0), size(0) {}
    };
    
    vector<Bucket> buckets;
    int totalSize;
    
    // 第一级哈希函数
    size_t primaryHash(const K& key) const {
        return hash<K>{}(key) % buckets.size();
    }
    
    // 第二级哈希函数
    size_t secondaryHash(const K& key, int a, int b, int size) const {
        return (a * hash<K>{}(key) + b) % size;
    }
    
    // 构建无冲突哈希表
    bool buildHashTable(Bucket& bucket) {
        if (bucket.entries.empty()) {
            bucket.hashTable.resize(1);
            return true;
        }
        
        int n = bucket.entries.size();
        bucket.hashTable.resize(n * n); // 平方空间确保无冲突
        
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(1, 1000);
        
        for (int attempt = 0; attempt < 100; attempt++) {
            bucket.a = dis(gen);
            bucket.b = dis(gen);
            
            fill(bucket.hashTable.begin(), bucket.hashTable.end(), -1);
            bool collision = false;
            
            for (const auto& entry : bucket.entries) {
                size_t index = secondaryHash(entry.first, bucket.a, bucket.b, bucket.hashTable.size());
                
                if (bucket.hashTable[index] != -1) {
                    collision = true;
                    break;
                }
                
                bucket.hashTable[index] = &entry - &bucket.entries[0];
            }
            
            if (!collision) {
                return true;
            }
        }
        
        return false;
    }
    
public:
    PerfectHashTable() : PerfectHashTable(DEFAULT_BUCKETS) {}
    
    PerfectHashTable(int numBuckets) : buckets(numBuckets), totalSize(0) {}
    
    // 构建完美哈希表
    bool build(const vector<pair<K, V>>& data) {
        // 第一级：将数据分配到桶中
        for (const auto& entry : data) {
            size_t bucketIndex = primaryHash(entry.first);
            buckets[bucketIndex].entries.push_back(entry);
        }
        
        // 第二级：为每个桶构建无冲突哈希表
        for (auto& bucket : buckets) {
            if (!buildHashTable(bucket)) {
                return false;
            }
            bucket.size = bucket.entries.size();
            totalSize += bucket.size;
        }
        
        return true;
    }
    
    // 查找值
    V get(const K& key) const {
        size_t bucketIndex = primaryHash(key);
        const Bucket& bucket = buckets[bucketIndex];
        
        if (bucket.hashTable.empty()) {
            return V();
        }
        
        size_t index = secondaryHash(key, bucket.a, bucket.b, bucket.hashTable.size());
        int entryIndex = bucket.hashTable[index];
        
        if (entryIndex != -1 && bucket.entries[entryIndex].first == key) {
            return bucket.entries[entryIndex].second;
        }
        
        return V();
    }
    
    // 检查是否包含键
    bool contains(const K& key) const {
        size_t bucketIndex = primaryHash(key);
        const Bucket& bucket = buckets[bucketIndex];
        
        if (bucket.hashTable.empty()) {
            return false;
        }
        
        size_t index = secondaryHash(key, bucket.a, bucket.b, bucket.hashTable.size());
        int entryIndex = bucket.hashTable[index];
        
        return entryIndex != -1 && bucket.entries[entryIndex].first == key;
    }
    
    // 获取总大小
    int getSize() const { return totalSize; }
    
    // 获取桶数量
    int getBucketCount() const { return buckets.size(); }
};

/**
 * 测试函数
 */
void testHashConflictAndPerfectHash() {
    cout << "=== 哈希冲突解决与完美哈希算法测试 ===" << endl;
    
    // 测试开放寻址法
    cout << "\n1. 开放寻址法测试:" << endl;
    OpenAddressingHashTable<string, int> openTable;
    openTable.put("apple", 1);
    openTable.put("banana", 2);
    openTable.put("cherry", 3);
    
    cout << "apple: " << openTable.get("apple") << endl;
    cout << "banana: " << openTable.get("banana") << endl;
    cout << "cherry: " << openTable.get("cherry") << endl;
    cout << "size: " << openTable.getSize() << endl;
    
    // 测试链地址法
    cout << "\n2. 链地址法测试:" << endl;
    SeparateChainingHashTable<string, int> chainTable;
    chainTable.put("apple", 1);
    chainTable.put("banana", 2);
    chainTable.put("cherry", 3);
    
    cout << "apple: " << chainTable.get("apple") << endl;
    cout << "banana: " << chainTable.get("banana") << endl;
    cout << "cherry: " << chainTable.get("cherry") << endl;
    cout << "size: " << chainTable.getSize() << endl;
    
    // 测试布谷鸟哈希
    cout << "\n3. 布谷鸟哈希测试:" << endl;
    CuckooHashTable<string, int> cuckooTable;
    cuckooTable.put("apple", 1);
    cuckooTable.put("banana", 2);
    cuckooTable.put("cherry", 3);
    
    cout << "apple: " << cuckooTable.get("apple") << endl;
    cout << "banana: " << cuckooTable.get("banana") << endl;
    cout << "cherry: " << cuckooTable.get("cherry") << endl;
    cout << "size: " << cuckooTable.getSize() << endl;
    
    // 测试完美哈希
    cout << "\n4. 完美哈希测试:" << endl;
    PerfectHashTable<string, int> perfectTable;
    vector<pair<string, int>> data = {
        {"apple", 1}, {"banana", 2}, {"cherry", 3}
    };
    
    if (perfectTable.build(data)) {
        cout << "完美哈希构建成功" << endl;
        cout << "apple: " << perfectTable.get("apple") << endl;
        cout << "banana: " << perfectTable.get("banana") << endl;
        cout << "cherry: " << perfectTable.get("cherry") << endl;
        cout << "size: " << perfectTable.getSize() << endl;
    } else {
        cout << "完美哈希构建失败" << endl;
    }
    
    cout << "\n=== 测试完成 ===" << endl;
}

int main() {
    testHashConflictAndPerfectHash();
    return 0;
}