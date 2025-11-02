/**
 * 高级哈希算法题目集合 - C++版本
 * 
 * 本文件包含各大算法平台的高级哈希相关题目，包括：
 * - 滚动哈希 (Rabin-Karp算法)
 * - 布隆过滤器 (Bloom Filter)
 * - 一致性哈希 (Consistent Hashing)
 * - 完美哈希 (Perfect Hashing)
 * - 分布式哈希表 (DHT)
 * 
 * 这些算法在分布式系统、大数据处理、网络安全等领域有重要应用
 */

#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <unordered_set>
#include <map>
#include <set>
#include <bitset>
#include <cmath>
#include <random>
#include <algorithm>
#include <functional>
#include <stdexcept>
#include <utility>
#include <exception>

using namespace std;

/**
 * 滚动哈希算法实现 (Rabin-Karp算法)
 * 应用场景：字符串匹配、子串查找、重复检测等
 * 
 * 算法原理：
 * 1. 使用多项式哈希函数计算字符串的哈希值
 * 2. 通过滑动窗口实现O(1)时间复杂度的哈希值更新
 * 3. 使用大质数取模防止整数溢出
 * 
 * 时间复杂度：O(n + m)，其中n是文本长度，m是模式长度
 * 空间复杂度：O(1)
 */
class RollingHashSolution {
private:
    static const int BASE = 256; // 字符集大小
    static const int MOD = 1000000007; // 大质数
    
public:
    /**
     * Rabin-Karp字符串匹配算法
     */
    vector<int> rabinKarp(const string& text, const string& pattern) {
        vector<int> result;
        
        if (text.empty() || pattern.empty() || pattern.length() > text.length()) {
            return result;
        }
        
        int n = text.length();
        int m = pattern.length();
        
        // 计算BASE^(m-1) mod MOD
        long long highestPower = 1;
        for (int i = 0; i < m - 1; i++) {
            highestPower = (highestPower * BASE) % MOD;
        }
        
        // 计算模式和文本前m个字符的哈希值
        long long patternHash = 0;
        long long textHash = 0;
        
        for (int i = 0; i < m; i++) {
            patternHash = (patternHash * BASE + pattern[i]) % MOD;
            textHash = (textHash * BASE + text[i]) % MOD;
        }
        
        // 滑动窗口匹配
        for (int i = 0; i <= n - m; i++) {
            // 哈希值匹配时，进行精确比较（防止哈希冲突）
            if (patternHash == textHash) {
                bool match = true;
                for (int j = 0; j < m; j++) {
                    if (text[i + j] != pattern[j]) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    result.push_back(i);
                }
            }
            
            // 更新下一个窗口的哈希值
            if (i < n - m) {
                textHash = (textHash - text[i] * highestPower) % MOD;
                textHash = (textHash * BASE + text[i + m]) % MOD;
                
                // 处理负数
                if (textHash < 0) {
                    textHash += MOD;
                }
            }
        }
        
        return result;
    }
    
    /**
     * 计算字符串的所有不同子串数量（使用滚动哈希）
     */
    int countDistinctSubstrings(const string& s) {
        if (s.empty()) {
            return 0;
        }
        
        unordered_set<long long> hashSet;
        int n = s.length();
        
        for (int i = 0; i < n; i++) {
            long long hash = 0;
            for (int j = i; j < n; j++) {
                hash = (hash * BASE + s[j]) % MOD;
                hashSet.insert(hash);
            }
        }
        
        return hashSet.size();
    }
    
    /**
     * 查找最长重复子串（使用滚动哈希和二分搜索）
     */
    string longestRepeatingSubstring(const string& s) {
        if (s.empty()) {
            return "";
        }
        
        int n = s.length();
        int left = 1, right = n - 1;
        string result = "";
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            string found = findRepeatingSubstring(s, mid);
            
            if (!found.empty()) {
                result = found;
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        
        return result;
    }
    
private:
    string findRepeatingSubstring(const string& s, int length) {
        unordered_map<long long, vector<int>> hashMap;
        long long hash = 0;
        long long power = 1;
        
        // 计算BASE^(length-1) mod MOD
        for (int i = 0; i < length - 1; i++) {
            power = (power * BASE) % MOD;
        }
        
        // 计算第一个窗口的哈希值
        for (int i = 0; i < length; i++) {
            hash = (hash * BASE + s[i]) % MOD;
        }
        
        hashMap[hash].push_back(0);
        
        // 滑动窗口
        for (int i = 1; i <= (int)s.length() - length; i++) {
            hash = (hash - s[i - 1] * power) % MOD;
            hash = (hash * BASE + s[i + length - 1]) % MOD;
            
            if (hash < 0) {
                hash += MOD;
            }
            
            if (hashMap.find(hash) != hashMap.end()) {
                string current = s.substr(i, length);
                for (int start : hashMap[hash]) {
                    if (s.substr(start, length) == current) {
                        return current;
                    }
                }
            }
            
            hashMap[hash].push_back(i);
        }
        
        return "";
    }
};

/**
 * 布隆过滤器实现
 * 应用场景：大规模数据去重、缓存穿透防护、垃圾邮件过滤等
 * 
 * 算法原理：
 * 1. 使用k个哈希函数将元素映射到位数组的k个位置
 * 2. 查询时检查所有k个位置是否都为1
 * 3. 存在假阳性（false positive），但不存在假阴性（false negative）
 * 
 * 时间复杂度：O(k)
 * 空间复杂度：O(m)，其中m是位数组大小
 */
class BloomFilter {
private:
    int size; // 位数组大小
    int hashCount; // 哈希函数数量
    vector<bool> bitArray; // 位数组
    
    /**
     * 计算位数组大小
     */
    int calculateSize(int n, double p) {
        if (p == 0) {
            p = numeric_limits<double>::min();
        }
        return (int)(-n * log(p) / (log(2) * log(2)));
    }
    
    /**
     * 计算哈希函数数量
     */
    int calculateHashCount(int n, int m) {
        return max(1, (int)round((double)m / n * log(2)));
    }
    
    /**
     * 哈希函数（使用不同的种子生成不同的哈希值）
     */
    int hash(const string& element, int seed) {
        int hash = 0;
        for (char c : element) {
            hash = seed * hash + c;
        }
        return hash;
    }
    
public:
    /**
     * 构造函数
     */
    BloomFilter(int expectedElements, double falsePositiveRate) {
        size = calculateSize(expectedElements, falsePositiveRate);
        hashCount = calculateHashCount(expectedElements, size);
        bitArray.resize(size, false);
    }
    
    /**
     * 添加元素
     */
    void add(const string& element) {
        for (int i = 0; i < hashCount; i++) {
            int hashVal = hash(element, i);
            int index = abs(hashVal % size);
            bitArray[index] = true;
        }
    }
    
    /**
     * 检查元素是否存在
     */
    bool contains(const string& element) {
        for (int i = 0; i < hashCount; i++) {
            int hashVal = hash(element, i);
            int index = abs(hashVal % size);
            if (!bitArray[index]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 获取当前假阳性率的估计值
     */
    double estimateFalsePositiveRate(int insertedElements) {
        return pow(1 - exp(-hashCount * (double)insertedElements / size), hashCount);
    }
    
    /**
     * 获取位数组的使用率
     */
    double getUsageRate() {
        int used = count(bitArray.begin(), bitArray.end(), true);
        return (double)used / size;
    }
};

/**
 * 一致性哈希算法实现
 * 应用场景：分布式缓存、负载均衡、分布式存储等
 * 
 * 算法原理：
 * 1. 将节点和键都映射到哈希环上
 * 2. 每个键顺时针找到的第一个节点就是其归属节点
 * 3. 节点增减时，只有少量数据需要重新分配
 * 
 * 时间复杂度：O(log n) 查找，其中n是虚拟节点数量
 * 空间复杂度：O(n)
 */
class ConsistentHashing {
private:
    map<int, string> circle; // 哈希环
    int virtualNodeCount; // 每个物理节点的虚拟节点数量
    
    /**
     * 哈希函数
     */
    int hash(const string& key) {
        return abs(hash<string>{}(key));
    }
    
public:
    ConsistentHashing(int vnodeCount) : virtualNodeCount(vnodeCount) {}
    
    /**
     * 添加节点
     */
    void addNode(const string& node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            string virtualNode = node + "#" + to_string(i);
            int hashVal = hash(virtualNode);
            circle[hashVal] = node;
        }
    }
    
    /**
     * 移除节点
     */
    void removeNode(const string& node) {
        for (int i = 0; i < virtualNodeCount; i++) {
            string virtualNode = node + "#" + to_string(i);
            int hashVal = hash(virtualNode);
            circle.erase(hashVal);
        }
    }
    
    /**
     * 获取键对应的节点
     */
    string getNode(const string& key) {
        if (circle.empty()) {
            return "";
        }
        
        int hashVal = hash(key);
        auto it = circle.lower_bound(hashVal);
        
        if (it == circle.end()) {
            // 环回，返回第一个节点
            it = circle.begin();
        }
        
        return it->second;
    }
    
    /**
     * 获取所有节点
     */
    set<string> getAllNodes() {
        set<string> nodes;
        for (const auto& pair : circle) {
            nodes.insert(pair.second);
        }
        return nodes;
    }
    
    /**
     * 计算数据分布的不平衡度
     */
    double calculateImbalance(const map<string, int>& keyDistribution) {
        if (keyDistribution.empty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (const auto& pair : keyDistribution) {
            sum += pair.second;
        }
        double average = sum / keyDistribution.size();
        
        double variance = 0.0;
        for (const auto& pair : keyDistribution) {
            variance += pow(pair.second - average, 2);
        }
        variance /= keyDistribution.size();
        
        return sqrt(variance) / average; // 变异系数
    }
};

/**
 * 完美哈希实现（两级哈希表）
 * 应用场景：静态数据集、编译器符号表、字典等
 * 
 * 算法原理：
 * 1. 第一级哈希将元素分组
 * 2. 第二级为每个组创建无冲突的哈希表
 * 3. 保证O(1)查找时间，无哈希冲突
 * 
 * 时间复杂度：O(1) 查找
 * 空间复杂度：O(n)
 */
class PerfectHashTable {
private:
    struct HashFunction {
        int a, b, tableSize;
        
        HashFunction(int a_val, int b_val, int size) : a(a_val), b(b_val), tableSize(size) {}
        
        int hash(const string& key) {
            return abs(a * hash<string>{}(key) + b) % tableSize;
        }
    };
    
    int size; // 第一级哈希表大小
    vector<HashFunction> secondLevel; // 第二级哈希函数
    vector<vector<string>> tables; // 存储数据的表
    
    /**
     * 第一级哈希函数
     */
    int firstLevelHash(const string& key) {
        return abs(hash<string>{}(key)) % size;
    }
    
    /**
     * 为给定的键集合找到无冲突的哈希函数
     */
    HashFunction findPerfectHashFunction(const vector<string>& keys) {
        if (keys.empty()) {
            return HashFunction(0, 0, 0);
        }
        
        int tableSize = keys.size() * keys.size(); // 平方空间保证无冲突
        
        // 尝试不同的哈希参数直到找到无冲突的
        random_device rd;
        mt19937 gen(rd());
        uniform_int_distribution<> dis(1, 1000);
        
        while (true) {
            int a = dis(gen);
            int b = dis(gen);
            
            unordered_set<int> positions;
            bool collision = false;
            
            for (const string& key : keys) {
                int pos = abs(a * hash<string>{}(key) + b) % tableSize;
                if (positions.find(pos) != positions.end()) {
                    collision = true;
                    break;
                }
                positions.insert(pos);
            }
            
            if (!collision) {
                return HashFunction(a, b, tableSize);
            }
        }
    }
    
public:
    /**
     * 构造函数
     */
    PerfectHashTable(const set<string>& keys) {
        size = keys.size();
        secondLevel.resize(size);
        tables.resize(size);
        
        buildPerfectHash(keys);
    }
    
    /**
     * 构建完美哈希表
     */
    void buildPerfectHash(const set<string>& keys) {
        // 第一级：将键分组
        unordered_map<int, vector<string>> groups;
        
        for (const string& key : keys) {
            int hashVal = firstLevelHash(key);
            groups[hashVal].push_back(key);
        }
        
        // 第二级：为每个组构建无冲突哈希表
        for (auto& pair : groups) {
            int groupIndex = pair.first;
            vector<string>& groupKeys = pair.second;
            
            // 为这个组找到无冲突的哈希函数
            HashFunction hashFunc = findPerfectHashFunction(groupKeys);
            secondLevel[groupIndex] = hashFunc;
            
            // 创建第二级哈希表
            tables[groupIndex].resize(hashFunc.tableSize, "");
            for (const string& key : groupKeys) {
                int pos = hashFunc.hash(key);
                tables[groupIndex][pos] = key;
            }
        }
    }
    
    /**
     * 查找键
     */
    bool contains(const string& key) {
        int firstLevel = firstLevelHash(key);
        if (secondLevel[firstLevel].tableSize == 0) {
            return false;
        }
        
        int secondLevelPos = secondLevel[firstLevel].hash(key);
        return key == tables[firstLevel][secondLevelPos];
    }
};

/**
 * 单元测试函数
 */
void testAdvancedHashProblems() {
    cout << "=== 高级哈希算法测试 ===" << endl << endl;
    
    // 测试滚动哈希
    cout << "1. 滚动哈希算法测试:" << endl;
    RollingHashSolution rollingHash;
    
    string text = "abracadabra";
    string pattern = "cad";
    vector<int> positions = rollingHash.rabinKarp(text, pattern);
    cout << "文本: \"" << text << "\", 模式: \"" << pattern << "\"" << endl;
    cout << "匹配位置: ";
    for (int pos : positions) {
        cout << pos << " ";
    }
    cout << endl;
    
    int distinctCount = rollingHash.countDistinctSubstrings("abc");
    cout << "字符串'abc'的不同子串数量: " << distinctCount << endl;
    
    string repeating = rollingHash.longestRepeatingSubstring("banana");
    cout << "字符串'banana'的最长重复子串: \"" << repeating << "\"" << endl;
    
    // 测试布隆过滤器
    cout << endl << "2. 布隆过滤器测试:" << endl;
    BloomFilter bloomFilter(1000, 0.01);
    
    bloomFilter.add("hello");
    bloomFilter.add("world");
    bloomFilter.add("test");
    
    cout << "包含'hello': " << (bloomFilter.contains("hello") ? "true" : "false") << endl;
    cout << "包含'unknown': " << (bloomFilter.contains("unknown") ? "true" : "false") << endl;
    cout << "使用率: " << bloomFilter.getUsageRate() * 100 << "%" << endl;
    
    // 测试一致性哈希
    cout << endl << "3. 一致性哈希测试:" << endl;
    ConsistentHashing consistentHashing(100);
    
    consistentHashing.addNode("node1");
    consistentHashing.addNode("node2");
    consistentHashing.addNode("node3");
    
    map<string, int> distribution;
    for (int i = 0; i < 1000; i++) {
        string key = "key" + to_string(i);
        string node = consistentHashing.getNode(key);
        distribution[node]++;
    }
    
    cout << "数据分布: ";
    for (const auto& pair : distribution) {
        cout << pair.first << ": " << pair.second << " ";
    }
    cout << endl;
    cout << "不平衡度: " << consistentHashing.calculateImbalance(distribution) << endl;
    
    // 测试完美哈希
    cout << endl << "4. 完美哈希测试:" << endl;
    set<string> keys = {"apple", "banana", "cherry", "date", "elderberry"};
    PerfectHashTable perfectHash(keys);
    
    for (const string& key : keys) {
        cout << "包含'" << key << "': " << (perfectHash.contains(key) ? "true" : "false") << endl;
    }
    cout << "包含'unknown': " << (perfectHash.contains("unknown") ? "true" : "false") << endl;
    
    cout << endl << "=== 算法复杂度分析 ===" << endl;
    cout << "1. 滚动哈希: O(n+m) 时间, O(1) 空间" << endl;
    cout << "2. 布隆过滤器: O(k) 时间, O(m) 空间" << endl;
    cout << "3. 一致性哈希: O(log n) 时间, O(n) 空间" << endl;
    cout << "4. 完美哈希: O(1) 时间, O(n) 空间" << endl;
    
    cout << endl << "=== C++工程化应用场景 ===" << endl;
    cout << "1. 滚动哈希: 字符串匹配、重复检测、版本控制" << endl;
    cout << "2. 布隆过滤器: 缓存系统、垃圾邮件过滤、爬虫去重" << endl;
    cout << "3. 一致性哈希: 分布式缓存、负载均衡、分布式存储" << endl;
    cout << "4. 完美哈希: 编译器符号表、静态字典、配置文件" << endl;
}

int main() {
    testAdvancedHashProblems();
    return 0;
}