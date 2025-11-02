// 一致性哈希算法实现 (C++版本)
// 题目来源: 分布式系统设计面试题
// 应用场景: 负载均衡、分布式缓存、分布式存储系统
// 题目描述: 实现一致性哈希算法，支持节点的动态增删和虚拟节点技术
// 
// 解题思路:
// 1. 使用哈希环存储节点和虚拟节点
// 2. 使用虚拟节点技术解决数据分布不均问题
// 3. 支持节点的动态添加和删除
// 4. 实现高效的数据查找和节点定位
// 
// 时间复杂度分析:
// - 添加节点: O(k)，其中k是虚拟节点数量
// - 删除节点: O(k)
// - 查找节点: O(log n)，其中n是节点总数
// 
// 空间复杂度: O(n * k)，其中n是物理节点数，k是每个节点的虚拟节点数
// 
// 工程化考量:
// 1. 异常处理: 验证节点和数据的有效性
// 2. 性能优化: 使用TreeMap实现高效的区间查找
// 3. 负载均衡: 虚拟节点技术确保数据均匀分布
// 4. 容错性: 支持节点的动态增删，最小化数据迁移

#include <iostream>
#include <map>
#include <set>
#include <vector>
#include <string>
#include <functional>
#include <random>
#include <algorithm>
#include <stdexcept>
#include <chrono>

using namespace std;

class ConsistentHashing {
private:
    // 哈希环，存储虚拟节点到物理节点的映射
    map<int, string> hashRing;
    
    // 物理节点集合
    set<string> physicalNodes;
    
    // 每个物理节点的虚拟节点数量
    int virtualNodeCount;
    
    /**
     * 哈希函数 - 使用FNV-1a算法
     * @param str 输入字符串
     * @return 哈希值
     */
    int hash(const string& str) {
        const int FNV_OFFSET_BASIS = 0x811C9DC5;
        const int FNV_PRIME = 0x01000193;
        
        int hash = FNV_OFFSET_BASIS;
        for (char c : str) {
            hash ^= (c & 0xff);
            hash *= FNV_PRIME;
        }
        
        // 确保哈希值为正数
        return hash & 0x7fffffff;
    }

public:
    /**
     * 构造函数
     * @param virtualNodeCount 每个物理节点的虚拟节点数量
     */
    ConsistentHashing(int virtualNodeCount = 3) : virtualNodeCount(virtualNodeCount) {
        if (virtualNodeCount <= 0) {
            throw invalid_argument("虚拟节点数量必须大于0");
        }
    }
    
    /**
     * 添加物理节点
     * @param node 物理节点名称
     * @throws invalid_argument 如果节点名为空或已存在
     */
    void addNode(const string& node) {
        if (node.empty()) {
            throw invalid_argument("节点名不能为空");
        }
        if (physicalNodes.find(node) != physicalNodes.end()) {
            throw invalid_argument("节点 " + node + " 已存在");
        }
        
        physicalNodes.insert(node);
        
        // 为物理节点创建虚拟节点
        for (int i = 0; i < virtualNodeCount; i++) {
            string virtualNode = node + "#" + to_string(i);
            int nodeHash = hash(virtualNode);
            hashRing[nodeHash] = node;
        }
        
        cout << "添加节点: " << node << "，虚拟节点数: " << virtualNodeCount << endl;
    }
    
    /**
     * 删除物理节点
     * @param node 物理节点名称
     * @throws invalid_argument 如果节点不存在
     */
    void removeNode(const string& node) {
        if (physicalNodes.find(node) == physicalNodes.end()) {
            throw invalid_argument("节点 " + node + " 不存在");
        }
        
        physicalNodes.erase(node);
        
        // 删除该物理节点的所有虚拟节点
        auto it = hashRing.begin();
        while (it != hashRing.end()) {
            if (it->second == node) {
                it = hashRing.erase(it);
            } else {
                ++it;
            }
        }
        
        cout << "删除节点: " << node << endl;
    }
    
    /**
     * 根据键查找对应的物理节点
     * @param key 数据键
     * @return 负责该键的物理节点
     * @throws invalid_argument 如果键为空或哈希环为空
     */
    string getNode(const string& key) {
        if (key.empty()) {
            throw invalid_argument("键不能为空");
        }
        if (hashRing.empty()) {
            throw invalid_argument("哈希环为空，请先添加节点");
        }
        
        int keyHash = hash(key);
        
        // 在哈希环上顺时针查找第一个大于等于该哈希值的节点
        auto it = hashRing.lower_bound(keyHash);
        
        // 如果没找到，则返回环上的第一个节点（环状结构）
        if (it == hashRing.end()) {
            it = hashRing.begin();
        }
        
        return it->second;
    }
    
    /**
     * 获取哈希环的状态信息
     * @return 哈希环状态字符串
     */
    string getStatus() const {
        string result;
        result += "一致性哈希环状态:\n";
        result += "物理节点数: " + to_string(physicalNodes.size()) + "\n";
        result += "虚拟节点数: " + to_string(hashRing.size()) + "\n";
        
        // 物理节点列表
        result += "物理节点列表: ";
        for (const auto& node : physicalNodes) {
            result += node + " ";
        }
        result += "\n";
        
        // 统计每个物理节点的虚拟节点分布
        map<string, int> nodeDistribution;
        for (const auto& entry : hashRing) {
            nodeDistribution[entry.second]++;
        }
        
        result += "虚拟节点分布: ";
        for (const auto& entry : nodeDistribution) {
            result += entry.first + ":" + to_string(entry.second) + " ";
        }
        result += "\n";
        
        return result;
    }
    
    /**
     * 负载均衡测试
     * 模拟大量数据分布，检查负载均衡性
     * @param dataCount 测试数据数量
     */
    void loadBalanceTest(int dataCount) {
        if (physicalNodes.empty()) {
            cout << "请先添加节点再进行负载均衡测试" << endl;
            return;
        }
        
        map<string, int> distribution;
        
        // 初始化分布统计
        for (const auto& node : physicalNodes) {
            distribution[node] = 0;
        }
        
        // 模拟数据分布
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(0, 1000000);
        
        for (int i = 0; i < dataCount; i++) {
            string key = "key" + to_string(dis(gen));
            string node = getNode(key);
            distribution[node]++;
        }
        
        // 计算负载均衡指标
        int total = dataCount;
        double average = (double)total / physicalNodes.size();
        double variance = 0.0;
        
        cout << "负载均衡测试结果 (数据量: " << dataCount << "):" << endl;
        for (const auto& entry : distribution) {
            double deviation = abs(entry.second - average);
            variance += deviation * deviation;
            cout << "节点 " << entry.first << ": " << entry.second 
                 << " 数据 (" << (double)entry.second / total * 100 << "%)" << endl;
        }
        
        double stdDev = sqrt(variance / physicalNodes.size());
        cout << "标准差: " << stdDev << ", 相对标准差: " << stdDev / average * 100 << "%" << endl;
    }
    
    /**
     * 获取物理节点数量
     * @return 物理节点数量
     */
    int getPhysicalNodeCount() const {
        return physicalNodes.size();
    }
    
    /**
     * 获取虚拟节点数量
     * @return 虚拟节点数量
     */
    int getVirtualNodeCount() const {
        return hashRing.size();
    }
};

/**
 * 单元测试函数
 */
void testConsistentHashing() {
    cout << "=== 一致性哈希算法单元测试 ===" << endl;
    
    // 测试1: 基本功能测试
    cout << "测试1: 基本功能测试" << endl;
    ConsistentHashing ch(3);
    
    // 添加节点
    ch.addNode("Node-A");
    ch.addNode("Node-B");
    ch.addNode("Node-C");
    
    cout << ch.getStatus() << endl;
    
    // 测试数据分布
    map<string, int> testDistribution;
    vector<string> testKeys = {"user1", "user2", "user3", "data1", "data2", "file1"};
    
    for (const auto& key : testKeys) {
        string node = ch.getNode(key);
        testDistribution[node]++;
        cout << "键 '" << key << "' 分配到节点: " << node << endl;
    }
    
    cout << "测试数据分布: ";
    for (const auto& entry : testDistribution) {
        cout << entry.first << ":" << entry.second << " ";
    }
    cout << endl;
    
    // 测试2: 负载均衡测试
    cout << "\n测试2: 负载均衡测试" << endl;
    ch.loadBalanceTest(1000);
    
    // 测试3: 节点删除测试
    cout << "\n测试3: 节点删除测试" << endl;
    ch.removeNode("Node-B");
    cout << ch.getStatus() << endl;
    
    // 重新测试数据分布
    testDistribution.clear();
    for (const auto& key : testKeys) {
        string node = ch.getNode(key);
        testDistribution[node]++;
        cout << "键 '" << key << "' 重新分配到节点: " << node << endl;
    }
    
    cout << "删除节点后数据分布: ";
    for (const auto& entry : testDistribution) {
        cout << entry.first << ":" << entry.second << " ";
    }
    cout << endl;
    
    // 测试4: 异常处理测试
    cout << "\n测试4: 异常处理测试" << endl;
    try {
        ch.addNode(""); // 空节点名
        cout << "异常处理测试失败" << endl;
    } catch (const invalid_argument& e) {
        cout << "异常处理测试通过: " << e.what() << endl;
    }
    
    cout << "=== 单元测试完成 ===" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 一致性哈希算法性能测试 ===" << endl;
    
    ConsistentHashing ch(5);
    
    // 添加多个节点
    for (int i = 1; i <= 10; i++) {
        ch.addNode("Server-" + to_string(i));
    }
    
    // 测试查找性能
    int testCount = 100000;
    
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 1000000);
    
    auto start = chrono::high_resolution_clock::now();
    
    for (int i = 0; i < testCount; i++) {
        string key = "key" + to_string(dis(gen));
        ch.getNode(key);
    }
    
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "性能测试结果:" << endl;
    cout << "节点数量: " << ch.getPhysicalNodeCount() << endl;
    cout << "虚拟节点数量: " << ch.getVirtualNodeCount() << endl;
    cout << "查找次数: " << testCount << endl;
    cout << "总查找时间: " << duration.count() << " 毫秒" << endl;
    cout << "平均查找时间: " << (double)duration.count() / testCount << " 毫秒/次" << endl;
    cout << "查找吞吐量: " << (double)testCount / (duration.count() / 1000.0) << " 次/秒" << endl;
    
    cout << "=== 性能测试完成 ===" << endl;
}

int main() {
    // 运行单元测试
    testConsistentHashing();
    
    // 运行性能测试
    performanceTest();
    
    // 演示示例
    cout << "=== 一致性哈希算法演示 ===" << endl;
    
    ConsistentHashing demo(5);
    
    // 添加节点
    demo.addNode("Server-1");
    demo.addNode("Server-2");
    demo.addNode("Server-3");
    
    cout << demo.getStatus() << endl;
    
    // 演示数据分布
    vector<string> demoKeys = {"user:1001", "order:2001", "product:3001", "cache:4001"};
    for (const auto& key : demoKeys) {
        cout << "数据 '" << key << "' 分配到: " << demo.getNode(key) << endl;
    }
    
    // 负载均衡测试
    demo.loadBalanceTest(10000);
    
    return 0;
}