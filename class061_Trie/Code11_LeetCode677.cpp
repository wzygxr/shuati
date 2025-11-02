// LeetCode 677. 键值映射 - C++实现
// 
// 题目描述：
// 实现一个 MapSum 类，支持 insert 和 sum 操作。
// 
// 测试链接：https://leetcode.cn/problems/map-sum-pairs/
// 
// 算法思路：
// 1. 使用前缀树存储键值对，每个节点存储经过该节点的所有键的值之和
// 2. 插入操作：更新键对应的值，并更新路径上所有节点的和
// 3. 求和操作：查找前缀对应的节点，返回该节点的和值
// 
// 时间复杂度分析：
// - 插入操作：O(L)，其中L是键的长度
// - 求和操作：O(L)，其中L是前缀的长度
// 
// 空间复杂度分析：
// - 前缀树空间：O(N*L)，其中N是键的数量，L是平均键长度
// - 总体空间复杂度：O(N*L)
// 
// 是否最优解：是
// 理由：使用前缀树可以高效处理前缀相关的键值操作
// 
// 工程化考虑：
// 1. 异常处理：处理空键和非法值
// 2. 边界情况：键为空或前缀为空的情况
// 3. 极端输入：大量键或长键的情况
// 4. 内存管理：合理管理前缀树内存
// 
// 语言特性差异：
// C++：使用指针实现前缀树，性能高且节省空间
// Java：使用数组实现，更安全但空间固定
// Python：使用字典实现，代码更简洁
// 
// 调试技巧：
// 1. 验证插入和求和操作的正确性
// 2. 测试重复插入相同键的情况
// 3. 单元测试覆盖各种边界条件

#include <iostream>
#include <vector>
#include <memory>
#include <string>
#include <unordered_map>
#include <cassert>

using namespace std;

/**
 * 前缀树节点类
 */
class TrieNode {
public:
    unique_ptr<TrieNode> children[26]; // 26个小写字母
    int value; // 经过该节点的所有键的值之和
    
    TrieNode() : value(0) {
        for (int i = 0; i < 26; i++) {
            children[i] = nullptr;
        }
    }
};

/**
 * MapSum 类实现
 */
class MapSum {
private:
    unique_ptr<TrieNode> root;
    unordered_map<string, int> keyValueMap; // 存储键的原始值，用于更新操作
    
public:
    /**
     * 构造函数
     */
    MapSum() : root(make_unique<TrieNode>()) {}
    
    /**
     * 插入键值对
     */
    void insert(const string& key, int val) {
        if (key.empty()) {
            return;
        }
        
        // 计算值的差异
        int delta = val;
        if (keyValueMap.find(key) != keyValueMap.end()) {
            delta = val - keyValueMap[key];
        }
        
        // 更新键值映射
        keyValueMap[key] = val;
        
        // 更新前缀树
        TrieNode* node = root.get();
        for (char c : key) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                continue; // 跳过非小写字母
            }
            
            if (node->children[index] == nullptr) {
                node->children[index] = make_unique<TrieNode>();
            }
            node = node->children[index].get();
            node->value += delta;
        }
    }
    
    /**
     * 计算以指定前缀开头的所有键的值之和
     */
    int sum(const string& prefix) {
        if (prefix.empty()) {
            return 0;
        }
        
        TrieNode* node = root.get();
        for (char c : prefix) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                return 0;
            }
            
            if (node->children[index] == nullptr) {
                return 0;
            }
            node = node->children[index].get();
        }
        
        return node->value;
    }
};

/**
 * 优化版本：使用静态数组实现，性能更高
 */
class MapSumOptimized {
private:
    static const int MAXN = 100000;
    static int tree[MAXN][26];
    static int values[MAXN];
    static int cnt;
    unordered_map<string, int> keyValueMap;
    
public:
    MapSumOptimized() {
        cnt = 1;
        // 初始化数组（实际项目中可能需要更复杂的初始化）
        for (int i = 0; i < MAXN; i++) {
            for (int j = 0; j < 26; j++) {
                tree[i][j] = 0;
            }
            values[i] = 0;
        }
    }
    
    void insert(const string& key, int val) {
        if (key.empty()) {
            return;
        }
        
        int delta = val;
        if (keyValueMap.find(key) != keyValueMap.end()) {
            delta = val - keyValueMap[key];
        }
        
        keyValueMap[key] = val;
        
        int cur = 1;
        for (char c : key) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                continue;
            }
            
            if (tree[cur][index] == 0) {
                tree[cur][index] = ++cnt;
            }
            cur = tree[cur][index];
            values[cur] += delta;
        }
    }
    
    int sum(const string& prefix) {
        if (prefix.empty()) {
            return 0;
        }
        
        int cur = 1;
        for (char c : prefix) {
            int index = c - 'a';
            if (index < 0 || index >= 26) {
                return 0;
            }
            
            if (tree[cur][index] == 0) {
                return 0;
            }
            cur = tree[cur][index];
        }
        
        return values[cur];
    }
};

// 静态成员变量定义
int MapSumOptimized::tree[MapSumOptimized::MAXN][26];
int MapSumOptimized::values[MapSumOptimized::MAXN];
int MapSumOptimized::cnt = 0;

/**
 * 单元测试函数
 */
void testMapSum() {
    // 测试用例1：基础功能测试
    MapSum mapSum;
    mapSum.insert("apple", 3);
    assert(mapSum.sum("ap") == 3);
    
    mapSum.insert("app", 2);
    assert(mapSum.sum("ap") == 5);
    
    // 测试用例2：更新操作测试
    mapSum.insert("apple", 5);
    assert(mapSum.sum("ap") == 7);
    
    // 测试用例3：前缀不存在测试
    assert(mapSum.sum("banana") == 0);
    
    // 测试用例4：优化版本测试
    MapSumOptimized optimized;
    optimized.insert("test", 100);
    assert(optimized.sum("te") == 100);
    
    cout << "所有单元测试通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    MapSum mapSum;
    MapSumOptimized optimized;
    
    int n = 10000;
    vector<string> keys;
    vector<int> values;
    
    // 生成测试数据
    for (int i = 0; i < n; i++) {
        keys.push_back("key" + to_string(i));
        values.push_back(i % 1000);
    }
    
    // 标准版本性能测试
    auto start = chrono::high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        mapSum.insert(keys[i], values[i]);
    }
    auto insertTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        mapSum.sum("key");
    }
    auto sumTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    // 优化版本性能测试
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        optimized.insert(keys[i], values[i]);
    }
    auto optimizedInsertTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    start = chrono::high_resolution_clock::now();
    for (int i = 0; i < n; i++) {
        optimized.sum("key");
    }
    auto optimizedSumTime = chrono::duration_cast<chrono::milliseconds>(
        chrono::high_resolution_clock::now() - start);
    
    cout << "标准版本 - 插入" << n << "个键耗时: " << insertTime.count() << "ms" << endl;
    cout << "标准版本 - 求和" << n << "次耗时: " << sumTime.count() << "ms" << endl;
    cout << "优化版本 - 插入" << n << "个键耗时: " << optimizedInsertTime.count() << "ms" << endl;
    cout << "优化版本 - 求和" << n << "次耗时: " << optimizedSumTime.count() << "ms" << endl;
}

/**
 * 边界情况测试函数
 */
void edgeCaseTest() {
    MapSum mapSum;
    
    // 测试空键
    mapSum.insert("", 100);
    assert(mapSum.sum("") == 0);
    
    // 测试空前缀
    mapSum.insert("test", 50);
    assert(mapSum.sum("") == 50);
    
    // 测试特殊字符
    mapSum.insert("test-key", 25);  // 包含连字符
    mapSum.insert("test key", 30);  // 包含空格
    
    // 测试极端数值
    mapSum.insert("large", 1000000000);
    mapSum.insert("negative", -100);
    assert(mapSum.sum("large") == 1000000000);
    assert(mapSum.sum("negative") == -100);
    
    cout << "边界情况测试通过！" << endl;
}

int main() {
    // 运行单元测试
    testMapSum();
    
    // 运行边界情况测试
    edgeCaseTest();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}