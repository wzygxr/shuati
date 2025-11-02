/**
 * 布隆过滤器与一致性哈希算法实现 - C++版本
 * 
 * 本文件包含高级哈希算法的实现，包括：
 * - 布隆过滤器 (Bloom Filter)
 * - 计数布隆过滤器 (Counting Bloom Filter)
 * - 一致性哈希 (Consistent Hashing)
 * - 虚拟节点技术 (Virtual Nodes)
 * - 分布式哈希表 (Distributed Hash Table)
 * 
 * 这些算法在大数据、分布式系统、缓存系统等领域有重要应用
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <set>
#include <algorithm>
#include <random>
#include <cmath>
#include <mutex>
#include <shared_mutex>
#include <chrono>
#include <functional>
#include <stdexcept>
#include <utility>
#include <exception>
#include <cstdlib>
#include <ctime>

using namespace std;

/**
 * 布隆过滤器实现
 * 应用场景：缓存系统、垃圾邮件过滤、URL去重
 * 
 * 算法原理：
 * 1. 使用多个哈希函数将元素映射到位数组的不同位置
 * 2. 查询时检查所有对应位置是否都为1
 * 3. 存在假阳性（可能误判存在），但不存在假阴性
 * 
 * 时间复杂度：O(k)，k为哈希函数数量
 * 空间复杂度：O(m)，m为位数组大小
 */
class BloomFilter {
private:
    vector<bool> bitArray;
    int size;
    int hashFunctions;
    mt19937 rng;
    
public:
    BloomFilter(int expectedElements, double falsePositiveRate) {
        // 计算最优位数组大小和哈希函数数量
        size = optimalSize(expectedElements, falsePositiveRate);
        hashFunctions = optimalHashFunctions(expectedElements, size);
        bitArray.resize(size, false);
        rng.seed(random_device{}());
    }
    
    /**
     * 计算最优位数组大小
     */
    int optimalSize(int n, double p) {
        return ceil(-(n * log(p)) / pow(log(2), 2));
    }
    
    /**
     * 计算最优哈希函数数量
     */
    int optimalHashFunctions(int n, int m) {
        return ceil((m / (double) n) * log(2));
    }
    
    /**
     * 添加元素
     */
    void add(const string& element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = hashFunction(element, i);
            bitArray[hash % size] = true;
        }
    }
    
    /**
     * 检查元素是否存在
     */
    bool contains(const string& element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = hashFunction(element, i);
            if (!bitArray[hash % size]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 哈希函数
     */
    int hashFunction(const string& element, int seed) {
        rng.seed(seed);
        hash<string> hasher;
        size_t hash = hasher(element);
        hash ^= rng();
        return abs(static_cast<int>(hash));
    }
    
    /**
     * 获取假阳性概率
     */
    double getFalsePositiveProbability(int insertedElements) {
        return pow(1 - exp(-hashFunctions * insertedElements / (double) size), hashFunctions);
    }
    
    /**
     * 获取位数组使用率
     */
    double getBitUsage() {
        int used = 0;
        for (bool bit : bitArray) {
            if (bit) used++;
        }
        return used / (double) size;
    }
    
    /**
     * 获取统计信息
     */
    void printStats() {
        cout << "布隆过滤器统计:" << endl;
        cout << "  位数组大小: " << size << endl;
        cout << "  哈希函数数量: " << hashFunctions << endl;
        cout << "  位使用率: " << getBitUsage() * 100 << "%" << endl;
    }
};

/**
 * 计数布隆过滤器实现
 * 应用场景：需要支持删除操作的布隆过滤器变种
 * 
 * 算法原理：
 * 1. 使用计数器数组代替位数组
 * 2. 添加时递增计数器，删除时递减计数器
 * 3. 查询时检查所有对应位置的计数器是否大于0
 * 
 * 时间复杂度：O(k)
 * 空间复杂度：O(m * log2(maxCount))
 */
class CountingBloomFilter {
private:
    vector<int> counterArray;
    int size;
    int hashFunctions;
    mt19937 rng;
    int elementCount;
    
public:
    CountingBloomFilter(int expectedElements, double falsePositiveRate) {
        size = optimalSize(expectedElements, falsePositiveRate);
        hashFunctions = optimalHashFunctions(expectedElements, size);
        counterArray.resize(size, 0);
        rng.seed(random_device{}());
        elementCount = 0;
    }
    
    int optimalSize(int n, double p) {
        return ceil(-(n * log(p)) / pow(log(2), 2));
    }
    
    int optimalHashFunctions(int n, int m) {
        return ceil((m / (double) n) * log(2));
    }
    
    /**
     * 添加元素
     */
    void add(const string& element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = hashFunction(element, i);
            counterArray[hash % size]++;
        }
        elementCount++;
    }
    
    /**
     * 删除元素
     */
    bool remove(const string& element) {
        if (!contains(element)) {
            return false;
        }
        
        for (int i = 0; i < hashFunctions; i++) {
            int hash = hashFunction(element, i);
            counterArray[hash % size]--;
        }
        elementCount--;
        return true;
    }
    
    /**
     * 检查元素是否存在
     */
    bool contains(const string& element) {
        for (int i = 0; i < hashFunctions; i++) {
            int hash = hashFunction(element, i);
            if (counterArray[hash % size] <= 0) {
                return false;
            }
        }
        return true;
    }
    
    int hashFunction(const string& element, int seed) {
        rng.seed(seed);
        hash<string> hasher;
        size_t hash = hasher(element);
        hash ^= rng();
        return abs(static_cast<int>(hash));
    }
    
    /**
     * 获取元素数量估计
     */
    int getEstimatedSize() {
        return elementCount;
    }
    
    /**
     * 获取最大计数器值
     */
    int getMaxCounterValue() {
        int max = 0;
        for (int count : counterArray) {
            if (count > max) max = count;
        }
        return max;
    }
    
    /**
     * 获取统计信息
     */
    void printStats() {
        cout << "计数布隆过滤器统计:" << endl;
        cout << "  计数器数组大小: " << size << endl;
        cout << "  哈希函数数量: " << hashFunctions << endl;
        cout << "  估计元素数量: " << elementCount << endl;
        cout << "  最大计数器值: " << getMaxCounterValue() << endl;
    }
};

/**
 * 一致性哈希算法实现
 * 应用场景：分布式缓存、负载均衡、分布式存储
 * 
 * 算法原理：
 * 1. 将哈希空间组织成环状结构
 * 2. 节点和数据都映射到环上的位置
 * 3. 数据存储在顺时针方向的下一个节点上
 * 4. 使用虚拟节点实现负载均衡
 * 
 * 时间复杂度：O(log n) 查找，O(1) 添加/删除节点
 * 空间复杂度：O(n + v)，n为节点数，v为虚拟节点数
 */
class ConsistentHash {
private:
    map<int, string> circle;
    unordered_map<string, vector<int>> virtualNodes;
    int virtualNodeCount;
    int hashSpace;
    
public:
    ConsistentHash(int vNodeCount, int hSpace) 
        : virtualNodeCount(vNodeCount), hashSpace(hSpace) {}
    
    /**
     * 添加节点
     */
    void addNode(const string& node) {
        vector<int> nodeHashes;
        
        for (int i = 0; i < virtualNodeCount; i++) {
            string virtualNode = node + "#" + to_string(i);
            int hash = hashFunction(virtualNode) % hashSpace;
            circle[hash] = node;
            nodeHashes.push_back(hash);
        }
        
        virtualNodes[node] = nodeHashes;
    }
    
    /**
     * 删除节点
     */
    void removeNode(const string& node) {
        auto it = virtualNodes.find(node);
        if (it != virtualNodes.end()) {
            for (int hash : it->second) {
                circle.erase(hash);
            }
            virtualNodes.erase(it);
        }
    }
    
    /**
     * 获取数据应该存储的节点
     */
    string getNode(const string& key) {
        if (circle.empty()) {
            return "";
        }
        
        int hash = hashFunction(key) % hashSpace;
        auto it = circle.lower_bound(hash);
        
        if (it == circle.end()) {
            // 回到环的开头
            it = circle.begin();
        }
        
        return it->second;
    }
    
    /**
     * 获取所有节点
     */
    unordered_set<string> getNodes() {
        unordered_set<string> nodes;
        for (const auto& pair : virtualNodes) {
            nodes.insert(pair.first);
        }
        return nodes;
    }
    
    /**
     * 获取节点负载分布
     */
    unordered_map<string, int> getLoadDistribution() {
        unordered_map<string, int> distribution;
        
        for (const auto& pair : virtualNodes) {
            distribution[pair.first] = pair.second.size();
        }
        
        return distribution;
    }
    
    /**
     * 哈希函数
     */
    int hashFunction(const string& key) {
        hash<string> hasher;
        return abs(static_cast<int>(hasher(key)));
    }
    
    /**
     * 数据迁移分析（当节点变化时）
     */
    unordered_map<string, unordered_set<string>> analyzeDataMigration(
        const unordered_set<string>& keys, const string& newNode) {
        
        unordered_map<string, unordered_set<string>> migration;
        
        // 添加新节点前的映射
        unordered_map<string, string> oldMapping;
        for (const string& key : keys) {
            oldMapping[key] = getNode(key);
        }
        
        // 添加新节点
        addNode(newNode);
        
        // 分析迁移数据
        for (const string& key : keys) {
            string newNodeForKey = getNode(key);
            string oldNodeForKey = oldMapping[key];
            
            if (newNodeForKey != oldNodeForKey) {
                migration[oldNodeForKey].insert(key);
            }
        }
        
        // 恢复原状
        removeNode(newNode);
        
        return migration;
    }
    
    /**
     * 打印环状态
     */
    void printRing() {
        cout << "一致性哈希环状态:" << endl;
        for (const auto& entry : circle) {
            cout << "  哈希位置: " << entry.first << " -> 节点: " << entry.second << endl;
        }
    }
};

/**
 * 分布式哈希表实现
 * 应用场景：P2P网络、分布式存储系统
 * 
 * 算法原理：
 * 1. 使用一致性哈希进行节点定位
 * 2. 支持数据的存储、检索和删除
 * 3. 处理节点加入和离开的数据迁移
 * 
 * 时间复杂度：O(log n) 查找
 * 空间复杂度：O(n)
 */
class DistributedHashTable {
private:
    ConsistentHash consistentHash;
    unordered_map<string, unordered_map<string, string>> nodeData;
    shared_mutex mutex;
    
public:
    DistributedHashTable(int virtualNodeCount, int hashSpace)
        : consistentHash(virtualNodeCount, hashSpace) {}
    
    /**
     * 添加节点
     */
    void addNode(const string& node) {
        unique_lock<shared_mutex> lock(mutex);
        consistentHash.addNode(node);
        nodeData[node] = unordered_map<string, string>();
    }
    
    /**
     * 删除节点
     */
    void removeNode(const string& node) {
        unique_lock<shared_mutex> lock(mutex);
        
        // 迁移数据到其他节点
        auto dataToMigrate = nodeData[node];
        for (const auto& entry : dataToMigrate) {
            const string& key = entry.first;
            const string& value = entry.second;
            string newNode = consistentHash.getNode(key);
            if (!newNode.empty() && newNode != node) {
                nodeData[newNode][key] = value;
            }
        }
        
        consistentHash.removeNode(node);
        nodeData.erase(node);
    }
    
    /**
     * 存储数据
     */
    void put(const string& key, const string& value) {
        shared_lock<shared_mutex> lock(mutex);
        string node = consistentHash.getNode(key);
        if (!node.empty()) {
            nodeData[node][key] = value;
        }
    }
    
    /**
     * 检索数据
     */
    string get(const string& key) {
        shared_lock<shared_mutex> lock(mutex);
        string node = consistentHash.getNode(key);
        if (!node.empty()) {
            auto it = nodeData[node].find(key);
            if (it != nodeData[node].end()) {
                return it->second;
            }
        }
        return "";
    }
    
    /**
     * 删除数据
     */
    bool remove(const string& key) {
        shared_lock<shared_mutex> lock(mutex);
        string node = consistentHash.getNode(key);
        if (!node.empty()) {
            return nodeData[node].erase(key) > 0;
        }
        return false;
    }
    
    /**
     * 获取系统状态
     */
    unordered_map<string, unordered_map<string, int>> getSystemStatus() {
        shared_lock<shared_mutex> lock(mutex);
        unordered_map<string, unordered_map<string, int>> status;
        
        // 节点信息
        unordered_map<string, int> nodeInfo;
        for (const auto& node : consistentHash.getNodes()) {
            nodeInfo[node] = nodeData[node].size();
        }
        status["nodeDataSize"] = nodeInfo;
        
        // 负载分布
        status["loadDistribution"] = consistentHash.getLoadDistribution();
        
        return status;
    }
    
    /**
     * 数据备份策略
     */
    void replicateData(const string& key, int replicationFactor) {
        unique_lock<shared_mutex> lock(mutex);
        string primaryNode = consistentHash.getNode(key);
        
        if (nodeData[primaryNode].find(key) != nodeData[primaryNode].end()) {
            string value = nodeData[primaryNode][key];
            
            // 在后续节点上创建副本
            unordered_set<string> replicaNodes;
            int hash = consistentHash.hashFunction(key) % consistentHash.hashSpace;
            
            // 找到后续的replicationFactor个节点
            auto it = consistentHash.circle.lower_bound(hash);
            if (it != consistentHash.circle.end()) {
                ++it; // 跳过主节点
            }
            
            while (replicaNodes.size() < replicationFactor && it != consistentHash.circle.end()) {
                if (it->second != primaryNode) {
                    replicaNodes.insert(it->second);
                }
                ++it;
            }
            
            // 如果不够，从头开始找
            if (replicaNodes.size() < replicationFactor) {
                it = consistentHash.circle.begin();
                while (replicaNodes.size() < replicationFactor && it != consistentHash.circle.end()) {
                    if (it->second != primaryNode && 
                        replicaNodes.find(it->second) == replicaNodes.end()) {
                        replicaNodes.insert(it->second);
                    }
                    ++it;
                }
            }
            
            // 存储副本
            for (const string& replicaNode : replicaNodes) {
                nodeData[replicaNode][key + "_replica"] = value;
            }
        }
    }
};

/**
 * 性能测试和分析工具
 */
class PerformanceAnalyzer {
public:
    /**
     * 测试布隆过滤器性能
     */
    static void testBloomFilter(int elementCount, double falsePositiveRate) {
        cout << "=== 布隆过滤器性能测试 ===" << endl;
        
        BloomFilter bf(elementCount, falsePositiveRate);
        
        // 添加元素
        auto startTime = chrono::high_resolution_clock::now();
        for (int i = 0; i < elementCount; i++) {
            bf.add("element" + to_string(i));
        }
        auto addTime = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - startTime);
        
        // 查询元素
        startTime = chrono::high_resolution_clock::now();
        for (int i = 0; i < elementCount; i++) {
            bf.contains("element" + to_string(i));
        }
        auto queryTime = chrono::duration_cast<chrono::nanoseconds>(
            chrono::high_resolution_clock::now() - startTime);
        
        // 测试假阳性
        int falsePositives = 0;
        int testCount = 10000;
        for (int i = elementCount; i < elementCount + testCount; i++) {
            if (bf.contains("element" + to_string(i))) {
                falsePositives++;
            }
        }
        
        cout << "元素数量: " << elementCount << endl;
        cout << "目标假阳性率: " << falsePositiveRate * 100 << "%" << endl;
        cout << "实际假阳性率: " << (falsePositives / (double) testCount) * 100 << "%" << endl;
        cout << "添加时间: " << addTime.count() / elementCount << " ns/元素" << endl;
        cout << "查询时间: " << queryTime.count() / elementCount << " ns/元素" << endl;
        
        bf.printStats();
    }
    
    /**
     * 测试一致性哈希负载均衡
     */
    static void testConsistentHashLoadBalance(int nodeCount, int virtualNodeCount, int keyCount) {
        cout << "\n=== 一致性哈希负载均衡测试 ===" << endl;
        
        ConsistentHash ch(virtualNodeCount, 1000000);
        
        // 添加节点
        for (int i = 0; i < nodeCount; i++) {
            ch.addNode("node" + to_string(i));
        }
        
        // 模拟数据分布
        unordered_map<string, int> keyDistribution;
        for (int i = 0; i < keyCount; i++) {
            string node = ch.getNode("key" + to_string(i));
            keyDistribution[node]++;
        }
        
        // 分析负载均衡
        int minLoad = INT_MAX;
        int maxLoad = INT_MIN;
        int totalLoad = 0;
        
        for (const auto& pair : keyDistribution) {
            minLoad = min(minLoad, pair.second);
            maxLoad = max(maxLoad, pair.second);
            totalLoad += pair.second;
        }
        
        double avgLoad = totalLoad / (double) nodeCount;
        double imbalance = (maxLoad - minLoad) / avgLoad * 100;
        
        cout << "节点数量: " << nodeCount << endl;
        cout << "虚拟节点数量: " << virtualNodeCount << endl;
        cout << "数据总量: " << keyCount << endl;
        cout << "最小负载: " << minLoad << endl;
        cout << "最大负载: " << maxLoad << endl;
        cout << "平均负载: " << avgLoad << endl;
        cout << "负载不均衡度: " << imbalance << "%" << endl;
        
        // 显示详细分布
        cout << "\n详细负载分布:" << endl;
        for (const auto& pair : keyDistribution) {
            cout << "  " << pair.first << ": " << pair.second 
                 << " 数据 (" << pair.second / (double) keyCount * 100 << "%)" << endl;
        }
    }
    
    /**
     * 测试节点变化的数据迁移
     */
    static void testDataMigration(int initialNodes, int keyCount) {
        cout << "\n=== 数据迁移测试 ===" << endl;
        
        ConsistentHash ch(100, 1000000);
        
        // 初始节点
        for (int i = 0; i < initialNodes; i++) {
            ch.addNode("node" + to_string(i));
        }
        
        // 生成测试键
        unordered_set<string> keys;
        for (int i = 0; i < keyCount; i++) {
            keys.insert("key" + to_string(i));
        }
        
        // 添加新节点前的映射
        unordered_map<string, string> oldMapping;
        for (const string& key : keys) {
            oldMapping[key] = ch.getNode(key);
        }
        
        // 添加新节点
        ch.addNode("newNode");
        
        // 分析迁移
        int migrated = 0;
        unordered_map<string, int> migrationByNode;
        
        for (const string& key : keys) {
            string newNode = ch.getNode(key);
            string oldNode = oldMapping[key];
            
            if (newNode != oldNode) {
                migrated++;
                migrationByNode[oldNode]++;
            }
        }
        
        cout << "初始节点数: " << initialNodes << endl;
        cout << "数据总量: " << keyCount << endl;
        cout << "迁移数据量: " << migrated << " (" 
             << (migrated / (double) keyCount) * 100 << "%)" << endl;
        
        cout << "\n各节点迁移情况:" << endl;
        for (const auto& pair : migrationByNode) {
            cout << "  " << pair.first << ": " << pair.second << " 数据迁移" << endl;
        }
    }
};

/**
 * 单元测试函数
 */
void testBloomFilterAndConsistentHash() {
    cout << "=== 布隆过滤器与一致性哈希算法测试 ===" << endl << endl;
    
    // 测试布隆过滤器
    cout << "1. 布隆过滤器测试:" << endl;
    BloomFilter bf(10000, 0.01);
    bf.add("hello");
    bf.add("world");
    cout << "包含 'hello': " << (bf.contains("hello") ? "是" : "否") << endl;
    cout << "包含 'test': " << (bf.contains("test") ? "是" : "否") << endl;
    cout << "假阳性概率: " << bf.getFalsePositiveProbability(2) << endl;
    
    // 测试计数布隆过滤器
    cout << "\n2. 计数布隆过滤器测试:" << endl;
    CountingBloomFilter cbf(10000, 0.01);
    cbf.add("apple");
    cbf.add("banana");
    cbf.add("apple"); // 重复添加
    cout << "包含 'apple': " << (cbf.contains("apple") ? "是" : "否") << endl;
    cbf.remove("apple");
    cout << "删除后包含 'apple': " << (cbf.contains("apple") ? "是" : "否") << endl;
    
    // 测试一致性哈希
    cout << "\n3. 一致性哈希测试:" << endl;
    ConsistentHash ch(100, 1000000);
    ch.addNode("node1");
    ch.addNode("node2");
    ch.addNode("node3");
    
    cout << "'key1' 分配到: " << ch.getNode("key1") << endl;
    cout << "'key2' 分配到: " << ch.getNode("key2") << endl;
    
    auto distribution = ch.getLoadDistribution();
    cout << "负载分布: " << endl;
    for (const auto& pair : distribution) {
        cout << "  " << pair.first << ": " << pair.second << " 虚拟节点" << endl;
    }
    
    // 测试分布式哈希表
    cout << "\n4. 分布式哈希表测试:" << endl;
    DistributedHashTable dht(100, 1000000);
    dht.addNode("nodeA");
    dht.addNode("nodeB");
    dht.put("user1", "Alice");
    dht.put("user2", "Bob");
    
    cout << "user1: " << dht.get("user1") << endl;
    
    auto status = dht.getSystemStatus();
    cout << "系统状态: " << endl;
    for (const auto& category : status) {
        cout << "  " << category.first << ":" << endl;
        for (const auto& entry : category.second) {
            cout << "    " << entry.first << ": " << entry.second << endl;
        }
    }
    
    // 性能测试
    cout << "\n5. 性能测试:" << endl;
    PerformanceAnalyzer::testBloomFilter(100000, 0.01);
    PerformanceAnalyzer::testConsistentHashLoadBalance(5, 100, 10000);
    PerformanceAnalyzer::testDataMigration(5, 10000);
    
    cout << "\n=== C++算法复杂度分析 ===" << endl;
    cout << "1. 布隆过滤器: O(k)时间，O(m)空间，k为哈希函数数，m为位数组大小" << endl;
    cout << "2. 计数布隆过滤器: O(k)时间，O(m*log(maxCount))空间" << endl;
    cout << "3. 一致性哈希: O(log n)查找，O(1)添加/删除，O(n+v)空间" << endl;
    cout << "4. 分布式哈希表: O(log n)操作，O(n)空间" << endl;
    
    cout << "\n=== C++工程化应用场景 ===" << endl;
    cout << "1. 缓存系统: Redis、Memcached使用布隆过滤器进行键存在性检查" << endl;
    cout << "2. 分布式存储: Cassandra、DynamoDB使用一致性哈希进行数据分片" << endl;
    cout << "3. 负载均衡: Nginx、HAProxy使用一致性哈希进行请求路由" << endl;
    cout << "4. 垃圾邮件过滤: 使用布隆过滤器快速判断邮件是否垃圾邮件" << endl;
    
    cout << "\n=== C++性能优化策略 ===" << endl;
    cout << "1. 内存布局优化: 使用连续内存提高缓存命中率" << endl;
    cout << "2. 模板元编程: 编译时优化哈希函数选择" << endl;
    cout << "3. SIMD指令: 使用向量化指令加速哈希计算" << endl;
    cout << "4. 预计算优化: 提前计算常用哈希值减少运行时开销" << endl;
}

int main() {
    testBloomFilterAndConsistentHash();
    return 0;
}