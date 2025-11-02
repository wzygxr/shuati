#include <iostream>
#include <string>
#include <map>
#include <set>
#include <functional>
#include <openssl/md5.h>
#include <chrono>

using namespace std;

/**
 * 一致性哈希算法实现（C++版本）
 * 
 * 题目来源：分布式系统设计面试题
 * 应用场景：负载均衡、分布式缓存、分布式存储系统
 * 
 * 核心思想：
 * 1. 将哈希空间组织成一个虚拟的圆环（0 ~ 2^32-1）
 * 2. 服务器节点通过哈希函数映射到环上
 * 3. 数据通过哈希函数映射到环上，顺时针找到最近的服务器节点
 * 4. 虚拟节点技术解决数据分布不均问题
 * 
 * 时间复杂度：
 * - 添加节点：O(k) k为虚拟节点数
 * - 删除节点：O(k)
 * - 查找节点：O(log n) n为环上节点总数
 * 
 * 空间复杂度：O(n*k) n为物理节点数，k为虚拟节点数
 * 
 * 工程化考量：
 * 1. 虚拟节点解决数据倾斜问题
 * 2. 支持节点的动态增删
 * 3. 数据迁移最小化
 * 4. 容错性和可扩展性
 */
class ConsistentHashing {
private:
    // 哈希环，存储虚拟节点到物理节点的映射
    map<int, string> ring;
    
    // 虚拟节点数量
    int virtualNodes;
    
    // 物理节点集合
    set<string> physicalNodes;
    
public:
    ConsistentHashing(int vNodes) : virtualNodes(vNodes) {}
    
    /**
     * 添加物理节点
     * @param node 物理节点标识
     */
    void addNode(const string& node) {
        if (physicalNodes.find(node) != physicalNodes.end()) {
            return; // 节点已存在
        }
        
        physicalNodes.insert(node);
        
        // 为每个物理节点创建虚拟节点
        for (int i = 0; i < virtualNodes; i++) {
            string virtualNode = node + "#" + to_string(i);
            int hash = getHash(virtualNode);
            ring[hash] = node;
        }
    }
    
    /**
     * 删除物理节点
     * @param node 物理节点标识
     */
    void removeNode(const string& node) {
        if (physicalNodes.find(node) == physicalNodes.end()) {
            return; // 节点不存在
        }
        
        physicalNodes.erase(node);
        
        // 删除该节点的所有虚拟节点
        for (int i = 0; i < virtualNodes; i++) {
            string virtualNode = node + "#" + to_string(i);
            int hash = getHash(virtualNode);
            ring.erase(hash);
        }
    }
    
    /**
     * 根据key查找对应的物理节点
     * @param key 数据key
     * @return 物理节点标识
     */
    string getNode(const string& key) {
        if (ring.empty()) {
            return "";
        }
        
        int hash = getHash(key);
        
        // 在环上查找大于等于该hash的第一个节点
        auto it = ring.lower_bound(hash);
        
        // 如果没找到，则返回环的第一个节点（环形结构）
        if (it == ring.end()) {
            it = ring.begin();
        }
        
        return it->second;
    }
    
    /**
     * 哈希函数：使用MD5哈希然后取模
     * @param key 输入字符串
     * @return 哈希值（0 ~ 2^32-1）
     */
    int getHash(const string& key) {
        unsigned char digest[MD5_DIGEST_LENGTH];
        MD5((const unsigned char*)key.c_str(), key.length(), digest);
        
        // 取前4个字节作为哈希值
        int hash = 0;
        for (int i = 0; i < 4; i++) {
            hash = (hash << 8) | digest[i];
        }
        return hash & 0x7FFFFFFF; // 确保为正数
    }
    
    /**
     * 获取环上节点分布情况（用于调试）
     */
    void printRing() {
        cout << "一致性哈希环状态：" << endl;
        for (const auto& entry : ring) {
            cout << "位置 " << entry.first << " -> " << entry.second << endl;
        }
    }
    
    /**
     * 获取物理节点数量
     */
    int getPhysicalNodeCount() {
        return physicalNodes.size();
    }
    
    /**
     * 获取虚拟节点数量
     */
    int getVirtualNodeCount() {
        return ring.size();
    }
};

/**
 * 测试函数
 */
int main() {
    // 创建一致性哈希环，每个物理节点有3个虚拟节点
    ConsistentHashing ch(3);
    
    // 添加物理节点
    ch.addNode("Server-A");
    ch.addNode("Server-B");
    ch.addNode("Server-C");
    
    // 测试数据分布
    string testKeys[] = {"user1", "user2", "user3", "data1", "data2", "data3"};
    
    cout << "=== 初始节点分布测试 ===" << endl;
    for (const auto& key : testKeys) {
        string node = ch.getNode(key);
        cout << "Key: " << key << " -> Node: " << node << endl;
    }
    
    // 测试节点删除
    cout << "\n=== 删除Server-B后测试 ===" << endl;
    ch.removeNode("Server-B");
    
    for (const auto& key : testKeys) {
        string node = ch.getNode(key);
        cout << "Key: " << key << " -> Node: " << node << endl;
    }
    
    // 测试节点添加
    cout << "\n=== 添加Server-D后测试 ===" << endl;
    ch.addNode("Server-D");
    
    for (const auto& key : testKeys) {
        string node = ch.getNode(key);
        cout << "Key: " << key << " -> Node: " << node << endl;
    }
    
    // 性能测试
    cout << "\n=== 性能测试 ===" << endl;
    auto startTime = chrono::high_resolution_clock::now();
    
    for (int i = 0; i < 10000; i++) {
        ch.getNode("test" + to_string(i));
    }
    
    auto endTime = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
    cout << "10000次查找耗时: " << duration.count() << "ms" << endl;
    
    // 打印环状态（调试用）
    ch.printRing();
    
    cout << "物理节点数量: " << ch.getPhysicalNodeCount() << endl;
    cout << "虚拟节点数量: " << ch.getVirtualNodeCount() << endl;
    
    return 0;
}