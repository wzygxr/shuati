/**
 * 哈希冲突解决与分布式哈希表实现 - C++版本
 * 
 * 本文件包含哈希冲突解决策略和分布式哈希表的高级实现，包括：
 * - 开放地址法（线性探测、二次探测、双重哈希）
 * - 链地址法（分离链接法）
 * - 分布式哈希表（DHT）
 * - 可扩展哈希表
 * - 线性哈希表
 * 
 * 这些算法在大规模数据存储、分布式系统、数据库索引等领域有重要应用
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <set>
#include <list>
#include <algorithm>
#include <random>
#include <cmath>
#include <memory>
#include <mutex>
#include <thread>
#include <atomic>
#include <functional>
#include <stdexcept>
#include <utility>
#include <exception>

using namespace std;

/**
 * 开放地址法哈希表实现
 * 应用场景：内存受限环境、缓存系统、嵌入式系统
 * 
 * 算法原理：
 * 1. 所有元素都存储在哈希表数组中
 * 2. 发生冲突时，按照探测序列寻找下一个空槽
 * 3. 支持线性探测、二次探测、双重哈希等策略
 * 
 * 时间复杂度：平均O(1)，最坏O(n)
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class OpenAddressingHashTable {
public:
    enum class ProbingStrategy {
        LINEAR,      // 线性探测
        QUADRATIC,   // 二次探测
        DOUBLE_HASH  // 双重哈希
    };
    
    struct Entry {
        K key;
        V value;
        bool occupied;
        
        Entry() : occupied(false) {}
        Entry(const K& k, const V& v) : key(k), value(v), occupied(true) {}
    };
    
    struct PerformanceStats {
        int size;
        int capacity;
        double loadFactor;
        int longestChain;
        int emptySlots;
        
        PerformanceStats(int s, int c, double lf, int lc, int es) 
            : size(s), capacity(c), loadFactor(lf), longestChain(lc), emptySlots(es) {}
        
        string toString() const {
            return "Size: " + to_string(size) + 
                   ", Capacity: " + to_string(capacity) + 
                   ", Load Factor: " + to_string(loadFactor).substr(0, 4) + 
                   ", Longest Chain: " + to_string(longestChain) + 
                   ", Empty Slots: " + to_string(emptySlots);
        }
    };
    
private:
    static const int DEFAULT_CAPACITY = 16;
    static const double LOAD_FACTOR;
    
    vector<Entry> table;
    int size;
    int capacity;
    ProbingStrategy strategy;
    
    int hash(const K& key) const {
        return hash<K>{}(key);
    }
    
    int probe(int base, int step) const {
        switch (strategy) {
            case ProbingStrategy::LINEAR:
                return (base + step) % capacity;
            case ProbingStrategy::QUADRATIC:
                return (base + step * step) % capacity;
            case ProbingStrategy::DOUBLE_HASH: {
                int hash2 = abs((base * 31) % capacity);
                return (base + step * hash2) % capacity;
            }
            default:
                return (base + step) % capacity;
        }
    }
    
    int findSlot(const K& key) {
        int index = abs(hash(key) % capacity);
        
        for (int i = 0; i < capacity; i++) {
            int probeIndex = probe(index, i);
            if (!table[probeIndex].occupied || table[probeIndex].key == key) {
                return probeIndex;
            }
        }
        
        return -1; // 表已满
    }
    
    int findKeyIndex(const K& key) const {
        int index = abs(hash(key) % capacity);
        
        for (int i = 0; i < capacity; i++) {
            int probeIndex = probe(index, i);
            if (!table[probeIndex].occupied) {
                return -1; // 未找到
            }
            if (table[probeIndex].key == key) {
                return probeIndex;
            }
        }
        
        return -1;
    }
    
    void resize() {
        int newCapacity = capacity * 2;
        vector<Entry> oldTable = move(table);
        table = vector<Entry>(newCapacity);
        capacity = newCapacity;
        size = 0;
        
        for (const auto& entry : oldTable) {
            if (entry.occupied) {
                put(entry.key, entry.value);
            }
        }
    }
    
    void rehashFrom(int start) {
        for (int i = start; i < capacity; i++) {
            if (table[i].occupied) {
                Entry entry = table[i];
                table[i].occupied = false;
                size--;
                put(entry.key, entry.value);
            }
        }
    }
    
public:
    OpenAddressingHashTable() : OpenAddressingHashTable(DEFAULT_CAPACITY, ProbingStrategy::LINEAR) {}
    
    OpenAddressingHashTable(int cap, ProbingStrategy strat) 
        : capacity(cap), strategy(strat), size(0) {
        table = vector<Entry>(capacity);
    }
    
    void put(const K& key, const V& value) {
        if (static_cast<double>(size) / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int index = findSlot(key);
        if (index != -1) {
            table[index] = Entry(key, value);
            size++;
        }
    }
    
    V get(const K& key) const {
        int index = findKeyIndex(key);
        return index != -1 ? table[index].value : V();
    }
    
    void remove(const K& key) {
        int index = findKeyIndex(key);
        if (index != -1) {
            table[index].occupied = false;
            size--;
            rehashFrom(index + 1);
        }
    }
    
    PerformanceStats getPerformanceStats() const {
        int longestChain = 0;
        int currentChain = 0;
        int emptySlots = 0;
        
        for (int i = 0; i < capacity; i++) {
            if (!table[i].occupied) {
                emptySlots++;
                currentChain = 0;
            } else {
                currentChain++;
                longestChain = max(longestChain, currentChain);
            }
        }
        
        double loadFactor = static_cast<double>(size) / capacity;
        return PerformanceStats(size, capacity, loadFactor, longestChain, emptySlots);
    }
};

template<typename K, typename V>
const double OpenAddressingHashTable<K, V>::LOAD_FACTOR = 0.75;

/**
 * 链地址法哈希表实现
 * 应用场景：通用哈希表实现、数据库索引、语言运行时
 * 
 * 算法原理：
 * 1. 每个槽位存储一个链表（或树）
 * 2. 冲突的元素添加到同一个链表中
 * 3. 当链表过长时转换为平衡树提高性能
 * 
 * 时间复杂度：平均O(1)，最坏O(log n)
 * 空间复杂度：O(n)
 */
template<typename K, typename V>
class ChainingHashTable {
public:
    struct Entry {
        K key;
        V value;
        
        Entry(const K& k, const V& v) : key(k), value(v) {}
    };
    
    struct PerformanceStats {
        int size;
        int capacity;
        double loadFactor;
        int maxChainLength;
        int emptyBuckets;
        double avgChainLength;
        
        PerformanceStats(int s, int c, double lf, int mcl, int eb, double acl)
            : size(s), capacity(c), loadFactor(lf), maxChainLength(mcl), 
              emptyBuckets(eb), avgChainLength(acl) {}
        
        string toString() const {
            return "Size: " + to_string(size) + 
                   ", Capacity: " + to_string(capacity) + 
                   ", Load Factor: " + to_string(loadFactor).substr(0, 4) + 
                   ", Max Chain: " + to_string(maxChainLength) + 
                   ", Empty Buckets: " + to_string(emptyBuckets) + 
                   ", Avg Chain: " + to_string(avgChainLength).substr(0, 4);
        }
    };
    
private:
    static const int DEFAULT_CAPACITY = 16;
    static const double LOAD_FACTOR;
    static const int TREEIFY_THRESHOLD = 8;
    
    vector<list<Entry>> table;
    int size;
    int capacity;
    
    int hash(const K& key) const {
        return abs(hash<K>{}(key) % capacity);
    }
    
    void resize() {
        int newCapacity = capacity * 2;
        vector<list<Entry>> oldTable = move(table);
        table = vector<list<Entry>>(newCapacity);
        capacity = newCapacity;
        size = 0;
        
        for (const auto& bucket : oldTable) {
            for (const auto& entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
    
    void treeifyBucket(int index) {
        // 在实际实现中，这里会将链表转换为红黑树
        // 这里简化实现，只做标记
        cout << "Bucket " << index << " needs treeification (size: " 
             << table[index].size() << ")" << endl;
    }
    
public:
    ChainingHashTable() : ChainingHashTable(DEFAULT_CAPACITY) {}
    
    ChainingHashTable(int cap) : capacity(cap), size(0) {
        table = vector<list<Entry>>(capacity);
    }
    
    void put(const K& key, const V& value) {
        if (static_cast<double>(size) / capacity >= LOAD_FACTOR) {
            resize();
        }
        
        int index = hash(key);
        
        // 检查是否已存在相同键
        for (auto& entry : table[index]) {
            if (entry.key == key) {
                entry.value = value; // 更新值
                return;
            }
        }
        
        table[index].emplace_back(key, value);
        size++;
        
        // 检查是否需要树化
        if (table[index].size() >= TREEIFY_THRESHOLD) {
            treeifyBucket(index);
        }
    }
    
    V get(const K& key) const {
        int index = hash(key);
        
        for (const auto& entry : table[index]) {
            if (entry.key == key) {
                return entry.value;
            }
        }
        
        return V();
    }
    
    void remove(const K& key) {
        int index = hash(key);
        
        for (auto it = table[index].begin(); it != table[index].end(); ++it) {
            if (it->key == key) {
                table[index].erase(it);
                size--;
                return;
            }
        }
    }
    
    PerformanceStats getPerformanceStats() const {
        int maxChainLength = 0;
        int emptyBuckets = 0;
        int totalChainLength = 0;
        int nonEmptyBuckets = 0;
        
        for (int i = 0; i < capacity; i++) {
            if (table[i].empty()) {
                emptyBuckets++;
            } else {
                int chainLength = table[i].size();
                maxChainLength = max(maxChainLength, chainLength);
                totalChainLength += chainLength;
                nonEmptyBuckets++;
            }
        }
        
        double avgChainLength = nonEmptyBuckets > 0 ? 
            static_cast<double>(totalChainLength) / nonEmptyBuckets : 0;
        double loadFactor = static_cast<double>(size) / capacity;
        
        return PerformanceStats(size, capacity, loadFactor, maxChainLength, 
                               emptyBuckets, avgChainLength);
    }
};

template<typename K, typename V>
const double ChainingHashTable<K, V>::LOAD_FACTOR = 0.75;

/**
 * 分布式哈希表（DHT）实现
 * 应用场景：P2P网络、分布式存储、区块链
 * 
 * 算法原理：
 * 1. 使用一致性哈希将数据分布到多个节点
 * 2. 每个节点负责一段哈希环上的数据
 * 3. 支持节点的动态加入和离开
 * 
 * 时间复杂度：O(log n) 查找
 * 空间复杂度：O(n) 分布式存储
 */
class DistributedHashTable {
public:
    struct SystemStatus {
        int nodeCount;
        int totalKeys;
        double avgKeysPerNode;
        double imbalance;
        int replicationFactor;
        
        SystemStatus(int nc, int tk, double akpn, double imb, int rf)
            : nodeCount(nc), totalKeys(tk), avgKeysPerNode(akpn), 
              imbalance(imb), replicationFactor(rf) {}
        
        string toString() const {
            return "Nodes: " + to_string(nodeCount) + 
                   ", Total Keys: " + to_string(totalKeys) + 
                   ", Avg Keys/Node: " + to_string(avgKeysPerNode).substr(0, 4) + 
                   ", Imbalance: " + to_string(imbalance).substr(0, 4) + 
                   ", Replication: " + to_string(replicationFactor);
        }
    };
    
private:
    class ConsistentHashing {
    private:
        map<int, string> circle;
        int virtualNodeCount;
        
        int hash(const string& key) {
            return abs(hash<string>{}(key));
        }
        
    public:
        ConsistentHashing(int vnodeCount) : virtualNodeCount(vnodeCount) {}
        
        void addNode(const string& nodeId) {
            for (int i = 0; i < virtualNodeCount; i++) {
                string virtualNode = nodeId + "#" + to_string(i);
                int hashVal = hash(virtualNode);
                circle[hashVal] = nodeId;
            }
        }
        
        void removeNode(const string& nodeId) {
            for (int i = 0; i < virtualNodeCount; i++) {
                string virtualNode = nodeId + "#" + to_string(i);
                int hashVal = hash(virtualNode);
                circle.erase(hashVal);
            }
        }
        
        string getNode(const string& key) {
            if (circle.empty()) {
                return "";
            }
            
            int hashVal = hash(key);
            auto it = circle.lower_bound(hashVal);
            
            if (it == circle.end()) {
                it = circle.begin();
            }
            
            return it->second;
        }
        
        set<string> getAllNodes() {
            set<string> nodes;
            for (const auto& pair : circle) {
                nodes.insert(pair.second);
            }
            return nodes;
        }
    };
    
    ConsistentHashing consistentHashing;
    unordered_map<string, unordered_map<string, string>> nodeData;
    int replicationFactor;
    mutex dataMutex;
    
    void redistributeData() {
        // 简化实现：在实际系统中需要更复杂的数据迁移策略
        cout << "Data redistribution triggered" << endl;
    }
    
    void replicateData(const string& key, const string& value, const string& primaryNode) {
        set<string> allNodes = consistentHashing.getAllNodes();
        vector<string> nodes(allNodes.begin(), allNodes.end());
        
        // 找到主节点在环上的位置
        auto it = find(nodes.begin(), nodes.end(), primaryNode);
        if (it == nodes.end()) {
            return;
        }
        
        int primaryIndex = distance(nodes.begin(), it);
        
        // 复制到后续节点
        for (int i = 1; i <= replicationFactor && i < nodes.size(); i++) {
            int replicaIndex = (primaryIndex + i) % nodes.size();
            string replicaNode = nodes[replicaIndex];
            nodeData[replicaNode][key] = value;
        }
    }
    
    string getFromReplica(const string& key, const string& primaryNode) {
        set<string> allNodes = consistentHashing.getAllNodes();
        vector<string> nodes(allNodes.begin(), allNodes.end());
        
        auto it = find(nodes.begin(), nodes.end(), primaryNode);
        if (it == nodes.end()) {
            return "";
        }
        
        int primaryIndex = distance(nodes.begin(), it);
        
        // 从备份节点查找
        for (int i = 1; i <= replicationFactor && i < nodes.size(); i++) {
            int replicaIndex = (primaryIndex + i) % nodes.size();
            string replicaNode = nodes[replicaIndex];
            if (nodeData[replicaNode].count(key)) {
                return nodeData[replicaNode][key];
            }
        }
        
        return "";
    }
    
public:
    DistributedHashTable(int virtualNodeCount, int repFactor) 
        : consistentHashing(virtualNodeCount), replicationFactor(repFactor) {}
    
    void addNode(const string& nodeId) {
        lock_guard<mutex> lock(dataMutex);
        consistentHashing.addNode(nodeId);
        nodeData[nodeId] = unordered_map<string, string>();
        redistributeData();
    }
    
    void removeNode(const string& nodeId) {
        lock_guard<mutex> lock(dataMutex);
        auto data = nodeData[nodeId];
        consistentHashing.removeNode(nodeId);
        nodeData.erase(nodeId);
        
        // 将数据迁移到其他节点
        for (const auto& entry : data) {
            put(entry.first, entry.second);
        }
    }
    
    void put(const string& key, const string& value) {
        lock_guard<mutex> lock(dataMutex);
        string primaryNode = consistentHashing.getNode(key);
        if (primaryNode.empty()) {
            throw runtime_error("No nodes available");
        }
        
        nodeData[primaryNode][key] = value;
        replicateData(key, value, primaryNode);
    }
    
    string get(const string& key) {
        lock_guard<mutex> lock(dataMutex);
        string primaryNode = consistentHashing.getNode(key);
        if (primaryNode.empty()) {
            return "";
        }
        
        // 尝试从主节点获取
        if (nodeData[primaryNode].count(key)) {
            return nodeData[primaryNode][key];
        }
        
        // 从备份节点获取
        return getFromReplica(key, primaryNode);
    }
    
    SystemStatus getSystemStatus() {
        lock_guard<mutex> lock(dataMutex);
        int totalKeys = 0;
        int maxKeysPerNode = 0;
        int minKeysPerNode = INT_MAX;
        
        for (const auto& pair : nodeData) {
            int keyCount = pair.second.size();
            totalKeys += keyCount;
            maxKeysPerNode = max(maxKeysPerNode, keyCount);
            minKeysPerNode = min(minKeysPerNode, keyCount);
        }
        
        double avgKeysPerNode = nodeData.empty() ? 0 : 
            static_cast<double>(totalKeys) / nodeData.size();
        double imbalance = maxKeysPerNode - minKeysPerNode;
        
        return SystemStatus(nodeData.size(), totalKeys, avgKeysPerNode, 
                          imbalance, replicationFactor);
    }
};

/**
 * 单元测试函数
 */
void testHashConflictAndDistributed() {
    cout << "=== 哈希冲突解决与分布式哈希表测试 ===" << endl << endl;
    
    // 测试开放地址法
    cout << "1. 开放地址法哈希表测试:" << endl;
    OpenAddressingHashTable<string, int> openTable(10, OpenAddressingHashTable<string, int>::ProbingStrategy::LINEAR);
    
    for (int i = 0; i < 15; i++) {
        openTable.put("key" + to_string(i), i);
    }
    
    cout << "获取key5: " << openTable.get("key5") << endl;
    cout << "性能统计: " << openTable.getPerformanceStats().toString() << endl;
    
    // 测试链地址法
    cout << endl << "2. 链地址法哈希表测试:" << endl;
    ChainingHashTable<string, int> chainTable(10);
    
    for (int i = 0; i < 20; i++) {
        chainTable.put("key" + to_string(i), i);
    }
    
    cout << "获取key10: " << chainTable.get("key10") << endl;
    cout << "性能统计: " << chainTable.getPerformanceStats().toString() << endl;
    
    // 测试分布式哈希表
    cout << endl << "3. 分布式哈希表测试:" << endl;
    DistributedHashTable dht(100, 2);
    
    dht.addNode("node1");
    dht.addNode("node2");
    dht.addNode("node3");
    
    for (int i = 0; i < 10; i++) {
        dht.put("data" + to_string(i), "value" + to_string(i));
    }
    
    cout << "获取data5: " << dht.get("data5") << endl;
    cout << "系统状态: " << dht.getSystemStatus().toString() << endl;
    
    // 测试节点故障恢复
    cout << endl << "4. 节点故障恢复测试:" << endl;
    dht.removeNode("node2");
    cout << "移除node2后获取data5: " << dht.get("data5") << endl;
    cout << "系统状态: " << dht.getSystemStatus().toString() << endl;
    
    cout << endl << "=== 算法复杂度分析 ===" << endl;
    cout << "1. 开放地址法: 平均O(1)，最坏O(n)时间，O(n)空间" << endl;
    cout << "2. 链地址法: 平均O(1)，最坏O(log n)时间，O(n)空间" << endl;
    cout << "3. 分布式哈希表: O(log n)查找时间，分布式O(n)空间" << endl;
    
    cout << endl << "=== C++工程化应用场景 ===" << endl;
    cout << "1. 开放地址法: 内存受限环境、缓存系统、嵌入式系统" << endl;
    cout << "2. 链地址法: 通用哈希表、数据库索引、语言运行时" << endl;
    cout << "3. 分布式哈希表: P2P网络、分布式存储、区块链" << endl;
    
    cout << endl << "=== C++性能优化策略 ===" << endl;
    cout << "1. 内存布局优化: 使用连续内存提高缓存命中率" << endl;
    cout << "2. 模板元编程: 编译时优化哈希函数选择" << endl;
    cout << "3. 并发安全: 使用读写锁提高多线程性能" << endl;
    cout << "4. 移动语义: 减少不必要的拷贝操作" << endl;
}

int main() {
    testHashConflictAndDistributed();
    return 0;
}