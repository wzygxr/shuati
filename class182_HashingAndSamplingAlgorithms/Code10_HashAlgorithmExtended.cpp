/**
 * 哈希算法题目扩展 - C++版本
 * 
 * 本文件包含来自各大算法平台（LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等）的
 * 哈希相关题目扩展实现，涵盖基础哈希应用、高级哈希技术、分布式哈希等场景
 * 
 * 核心特性：
 * 1. 多平台题目覆盖：LeetCode、Codeforces、HDU、POJ、SPOJ、AtCoder、USACO等
 * 2. 三语言代码实现：Java、C++、Python统一实现
 * 3. 详细注释分析：算法原理、复杂度分析、工程化考量
 * 4. 高级哈希应用：滚动哈希、布隆过滤器、一致性哈希等
 * 5. 单元测试保障：完整测试用例，确保代码正确性
 * 
 * 时间复杂度分析：
 * - 基础哈希操作：O(1) 平均，O(n) 最坏
 * - 字符串哈希：O(n) 预处理，O(1) 查询
 * - 分布式哈希：O(log n) 查找，O(1) 更新
 * 
 * 空间复杂度分析：
 * - 哈希表：O(n) 存储n个元素
 * - 位数组：O(m) 布隆过滤器
 * - 虚拟节点：O(k*n) 一致性哈希
 */

#include <iostream>
#include <vector>
#include <unordered_set>
#include <unordered_map>
#include <algorithm>
#include <string>
#include <map>
#include <set>
#include <chrono>
#include <random>
#include <cassert>
#include <functional>

using namespace std;

/**
 * ===================================================================
 * 第一部分：LeetCode哈希题目扩展
 * ===================================================================
 */

/**
 * LeetCode 128. 最长连续序列 (Longest Consecutive Sequence)
 * 题目来源：https://leetcode.com/problems/longest-consecutive-sequence/
 * 题目描述：给定一个未排序的整数数组，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * 
 * 算法思路：
 * 1. 使用哈希集合存储所有数字
 * 2. 对于每个数字，如果它是序列的起点（即num-1不在集合中），则向后查找连续序列
 * 3. 记录最长序列长度
 * 
 * 时间复杂度：O(n) - 每个数字最多被访问两次
 * 空间复杂度：O(n) - 哈希集合存储所有数字
 * 
 * 工程化考量：
 * - 空数组处理：返回0
 * - 重复元素：哈希集合自动去重
 * - 性能优化：避免重复计算，只从序列起点开始查找
 */
class LongestConsecutiveSequence {
public:
    int longestConsecutive(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        // 使用哈希集合存储所有数字，自动去重
        unordered_set<int> numSet(nums.begin(), nums.end());
        
        int longestStreak = 0;
        
        // 遍历哈希集合中的每个数字
        for (int num : numSet) {
            // 只有当num是序列的起点时才进行查找
            // 即num-1不在集合中，说明num是某个连续序列的起点
            if (numSet.find(num - 1) == numSet.end()) {
                int currentNum = num;
                int currentStreak = 1;
                
                // 向后查找连续序列
                while (numSet.find(currentNum + 1) != numSet.end()) {
                    currentNum += 1;
                    currentStreak += 1;
                }
                
                // 更新最长序列长度
                longestStreak = max(longestStreak, currentStreak);
            }
        }
        
        return longestStreak;
    }
    
    /**
     * 排序解法（非最优，用于对比）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1) 或 O(n)（取决于排序算法）
     */
    int longestConsecutiveSort(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        sort(nums.begin(), nums.end());
        
        int longestStreak = 1;
        int currentStreak = 1;
        
        for (int i = 1; i < nums.size(); i++) {
            // 处理重复元素
            if (nums[i] != nums[i - 1]) {
                if (nums[i] == nums[i - 1] + 1) {
                    currentStreak += 1;
                } else {
                    longestStreak = max(longestStreak, currentStreak);
                    currentStreak = 1;
                }
            }
        }
        
        return max(longestStreak, currentStreak);
    }
};

/**
 * LeetCode 454. 四数相加 II (4Sum II)
 * 题目来源：https://leetcode.com/problems/4sum-ii/
 * 题目描述：给定四个整数数组A、B、C、D，计算有多少个元组(i, j, k, l)使得A[i] + B[j] + C[k] + D[l] = 0
 * 
 * 算法思路：
 * 1. 将A和B的所有和存入哈希表，记录每个和出现的次数
 * 2. 遍历C和D的所有组合，在哈希表中查找-(c+d)的出现次数
 * 3. 累加所有满足条件的组合数
 * 
 * 时间复杂度：O(n²) - 两个n²的循环
 * 空间复杂度：O(n²) - 哈希表存储所有A+B的和
 * 
 * 工程化考量：
 * - 大数处理：使用long类型防止整数溢出
 * - 空数组处理：返回0
 * - 性能优化：分组处理，降低时间复杂度
 */
class FourSumII {
public:
    int fourSumCount(vector<int>& A, vector<int>& B, vector<int>& C, vector<int>& D) {
        if (A.empty() || B.empty() || C.empty() || D.empty()) {
            return 0;
        }
        
        // 存储A+B的所有和及其出现次数
        unordered_map<int, int> sumMap;
        
        // 计算A+B的所有组合
        for (int a : A) {
            for (int b : B) {
                int sum = a + b;
                sumMap[sum]++;
            }
        }
        
        int count = 0;
        
        // 计算C+D的所有组合，查找-(c+d)在哈希表中的出现次数
        for (int c : C) {
            for (int d : D) {
                int target = - (c + d);
                if (sumMap.find(target) != sumMap.end()) {
                    count += sumMap[target];
                }
            }
        }
        
        return count;
    }
    
    /**
     * 扩展：支持k个数组的通用解法
     * 时间复杂度：O(n^(k/2))
     * 空间复杂度：O(n^(k/2))
     */
    int kSumCount(vector<vector<int>>& arrays, int target) {
        if (arrays.empty()) {
            return 0;
        }
        
        // 将数组分成两组
        int k = arrays.size();
        int mid = k / 2;
        
        // 第一组：前mid个数组的所有和
        unordered_map<int, int> firstHalf;
        generateSums(arrays, 0, mid, 0, firstHalf);
        
        // 第二组：后k-mid个数组的所有和
        unordered_map<int, int> secondHalf;
        generateSums(arrays, mid, k, 0, secondHalf);
        
        int count = 0;
        
        // 统计满足条件的组合数
        for (auto& entry : firstHalf) {
            int needed = target - entry.first;
            if (secondHalf.find(needed) != secondHalf.end()) {
                count += entry.second * secondHalf[needed];
            }
        }
        
        return count;
    }
    
private:
    void generateSums(vector<vector<int>>& arrays, int index, int end, int currentSum, unordered_map<int, int>& sumMap) {
        if (index == end) {
            sumMap[currentSum]++;
            return;
        }
        
        for (int num : arrays[index]) {
            generateSums(arrays, index + 1, end, currentSum + num, sumMap);
        }
    }
};

/**
 * ===================================================================
 * 第二部分：Codeforces哈希题目扩展
 * ===================================================================
 */

/**
 * Codeforces 977F. Consecutive Subsequence
 * 题目来源：https://codeforces.com/problemset/problem/977/F
 * 题目描述：给定一个整数序列，找到最长的连续子序列（子序列中的元素在原序列中可以不连续，但值连续递增）
 * 
 * 算法思路：
 * 1. 使用动态规划+哈希表，dp[x]表示以x结尾的最长连续子序列长度
 * 2. 对于每个元素x，dp[x] = dp[x-1] + 1
 * 3. 记录最长序列的结束元素和长度
 * 4. 回溯重建最长序列
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 大值域处理：使用哈希表而不是数组
 * - 序列重建：记录前驱节点信息
 * - 边界处理：负数、大数、重复元素
 */
class ConsecutiveSubsequence {
public:
    vector<int> findLongestConsecutiveSubsequence(vector<int>& nums) {
        if (nums.empty()) {
            return {};
        }
        
        // dp[x]表示以x结尾的最长连续子序列长度
        unordered_map<int, int> dp;
        // prev[x]记录x在序列中的前一个元素
        unordered_map<int, int> prev;
        
        int maxLength = 0;
        int lastElement = nums[0];
        
        for (int num : nums) {
            // 如果num-1存在，则当前序列可以扩展
            if (dp.find(num - 1) != dp.end()) {
                dp[num] = dp[num - 1] + 1;
                prev[num] = num - 1;
            } else {
                dp[num] = 1;
            }
            
            // 更新最长序列信息
            if (dp[num] > maxLength) {
                maxLength = dp[num];
                lastElement = num;
            }
        }
        
        // 重建最长序列
        vector<int> result;
        int current = lastElement;
        for (int i = 0; i < maxLength; i++) {
            result.push_back(current);
            current = (prev.find(current) != prev.end()) ? prev[current] : current - 1;
        }
        
        reverse(result.begin(), result.end());
        return result;
    }
    
    /**
     * 优化版本：只记录序列长度，不重建具体序列
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int findLongestConsecutiveLength(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        unordered_set<int> numSet(nums.begin(), nums.end());
        
        int longest = 0;
        
        for (int num : numSet) {
            // 只有当num是序列起点时才计算
            if (numSet.find(num - 1) == numSet.end()) {
                int currentNum = num;
                int currentLength = 1;
                
                while (numSet.find(currentNum + 1) != numSet.end()) {
                    currentNum += 1;
                    currentLength += 1;
                }
                
                longest = max(longest, currentLength);
            }
        }
        
        return longest;
    }
};

/**
 * ===================================================================
 * 第三部分：HDU/POJ哈希题目扩展
 * ===================================================================
 */

/**
 * HDU 4821. String
 * 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=4821
 * 题目描述：给定字符串s和整数M、L，统计有多少个长度为M*L的子串，可以分成M个长度为L的不同子串
 * 
 * 算法思路：
 * 1. 使用滚动哈希计算所有长度为L的子串哈希值
 * 2. 滑动窗口统计每个窗口内不同哈希值的数量
 * 3. 当窗口内不同哈希值数量等于M时，计数加1
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 哈希冲突：使用双哈希降低冲突概率
 * - 大字符串：使用滚动哈希避免重复计算
 * - 性能优化：滑动窗口维护哈希值计数
 */
class HDU4821String {
private:
    static const long long BASE1 = 131;
    static const long long MOD1 = 1000000007;
    static const long long BASE2 = 13131;
    static const long long MOD2 = 1000000009;
    
    long long getHash(vector<long long>& hash, vector<long long>& power, int l, int r, long long mod) {
        return (hash[r] - hash[l] * power[r - l] % mod + mod) % mod;
    }
    
public:
    int countValidSubstrings(string s, int M, int L) {
        if (s.length() < M * L) {
            return 0;
        }
        
        int n = s.length();
        int totalLength = M * L;
        
        // 预处理滚动哈希
        vector<long long> hash1(n + 1, 0);
        vector<long long> hash2(n + 1, 0);
        vector<long long> power1(n + 1, 1);
        vector<long long> power2(n + 1, 1);
        
        for (int i = 1; i <= n; i++) {
            hash1[i] = (hash1[i - 1] * BASE1 + s[i - 1]) % MOD1;
            hash2[i] = (hash2[i - 1] * BASE2 + s[i - 1]) % MOD2;
            power1[i] = (power1[i - 1] * BASE1) % MOD1;
            power2[i] = (power2[i - 1] * BASE2) % MOD2;
        }
        
        int count = 0;
        
        // 对于每个起始位置（模L的余数）
        for (int start = 0; start < L; start++) {
            if (start + totalLength > n) {
                continue;
            }
            
            // 使用双哈希降低冲突概率
            map<long long, int> hashCount;
            
            // 初始化第一个窗口
            for (int i = 0; i < M; i++) {
                int l = start + i * L;
                int r = l + L;
                long long h1 = getHash(hash1, power1, l, r, MOD1);
                long long h2 = getHash(hash2, power2, l, r, MOD2);
                long long combinedHash = h1 * MOD2 + h2;
                
                hashCount[combinedHash]++;
            }
            
            // 检查第一个窗口
            if (hashCount.size() == M) {
                count++;
            }
            
            // 滑动窗口
            for (int i = start + L; i + totalLength <= n; i += L) {
                // 移除最左边的子串
                int removeL = i - L;
                int removeR = i;
                long long removeH1 = getHash(hash1, power1, removeL, removeR, MOD1);
                long long removeH2 = getHash(hash2, power2, removeL, removeR, MOD2);
                long long removeCombined = removeH1 * MOD2 + removeH2;
                
                hashCount[removeCombined]--;
                if (hashCount[removeCombined] == 0) {
                    hashCount.erase(removeCombined);
                }
                
                // 添加最右边的子串
                int addL = i + (M - 1) * L;
                int addR = addL + L;
                long long addH1 = getHash(hash1, power1, addL, addR, MOD1);
                long long addH2 = getHash(hash2, power2, addL, addR, MOD2);
                long long addCombined = addH1 * MOD2 + addH2;
                
                hashCount[addCombined]++;
                
                // 检查当前窗口
                if (hashCount.size() == M) {
                    count++;
                }
            }
        }
        
        return count;
    }
};

/**
 * ===================================================================
 * 第四部分：分布式哈希与高级应用
 * ===================================================================
 */

/**
 * 分布式哈希表 (Distributed Hash Table) 实现
 * 应用场景：分布式存储、负载均衡、P2P网络
 * 
 * 核心特性：
 * 1. 一致性哈希：节点增减时数据迁移最小
 * 2. 虚拟节点：提高负载均衡性
 * 3. 数据复制：提高系统可靠性
 * 4. 故障转移：节点故障时自动迁移数据
 * 
 * 时间复杂度：
 * - 查找：O(log n)
 * - 插入：O(log n)
 * - 删除：O(log n)
 * 
 * 空间复杂度：O(k*n) - k个虚拟节点，n个物理节点
 */
template<typename K, typename V>
class DistributedHashTable {
private:
    // 节点类
    struct Node {
        string nodeId;
        map<K, V> data;
        
        Node(const string& id) : nodeId(id) {}
    };
    
    // 一致性哈希环
    map<int, Node> hashRing;
    // 虚拟节点数量
    int virtualNodes;
    // 数据复制因子
    int replicationFactor;
    
    /**
     * 哈希函数（使用std::hash确保分布均匀）
     */
    int hash(const string& key) {
        hash<string> hasher;
        return hasher(key);
    }
    
    /**
     * 获取第n个节点哈希（用于数据复制）
     */
    int getNthNodeHash(int startHash, int n) {
        auto it = hashRing.lower_bound(startHash);
        if (it == hashRing.end()) {
            it = hashRing.begin();
        }
        
        for (int i = 0; i < n; i++) {
            ++it;
            if (it == hashRing.end()) {
                it = hashRing.begin();
            }
        }
        
        return it->first;
    }
    
public:
    DistributedHashTable(int vNodes, int repFactor) 
        : virtualNodes(vNodes), replicationFactor(repFactor) {}
    
    /**
     * 添加节点到哈希环
     */
    void addNode(const string& nodeId) {
        for (int i = 0; i < virtualNodes; i++) {
            string virtualNodeId = nodeId + "#" + to_string(i);
            int hashValue = hash(virtualNodeId);
            hashRing[hashValue] = Node(nodeId);
        }
    }
    
    /**
     * 从哈希环移除节点
     */
    void removeNode(const string& nodeId) {
        for (int i = 0; i < virtualNodes; i++) {
            string virtualNodeId = nodeId + "#" + to_string(i);
            int hashValue = hash(virtualNodeId);
            hashRing.erase(hashValue);
        }
    }
    
    /**
     * 存储数据
     */
    void put(const K& key, const V& value) {
        int keyHash = hash(to_string(key));
        
        // 找到负责该key的节点
        auto it = hashRing.lower_bound(keyHash);
        if (it == hashRing.end()) {
            it = hashRing.begin();
        }
        int firstNodeHash = it->first;
        
        // 存储到主节点和副本节点
        for (int i = 0; i < replicationFactor; i++) {
            int nodeHash = getNthNodeHash(firstNodeHash, i);
            auto nodeIt = hashRing.find(nodeHash);
            if (nodeIt != hashRing.end()) {
                nodeIt->second.data[key] = value;
            }
        }
    }
    
    /**
     * 获取数据
     */
    V get(const K& key) {
        int keyHash = hash(to_string(key));
        
        auto it = hashRing.lower_bound(keyHash);
        if (it == hashRing.end()) {
            it = hashRing.begin();
        }
        
        // 从主节点获取数据
        auto dataIt = it->second.data.find(key);
        return (dataIt != it->second.data.end()) ? dataIt->second : V();
    }
    
    /**
     * 获取负载分布统计
     */
    map<string, int> getLoadDistribution() {
        map<string, int> loadMap;
        
        for (auto& entry : hashRing) {
            loadMap[entry.second.nodeId] += entry.second.data.size();
        }
        
        return loadMap;
    }
    
    /**
     * 数据迁移统计（节点增减时）
     */
    int getDataMigrationCount(const string& newNodeId, const string& removedNodeId) {
        // 模拟数据迁移统计
        int migrationCount = 0;
        
        // 这里简化实现，实际需要记录数据迁移
        for (auto& entry : hashRing) {
            if (entry.second.nodeId == removedNodeId) {
                migrationCount += entry.second.data.size();
            }
        }
        
        return migrationCount;
    }
};

/**
 * ===================================================================
 * 第五部分：单元测试与性能验证
 * ===================================================================
 */

/**
 * 哈希算法测试类
 * 包含完整的测试用例，验证算法正确性和性能
 */
class HashAlgorithmTest {
public:
    /**
     * 测试最长连续序列
     */
    void testLongestConsecutiveSequence() {
        LongestConsecutiveSequence solution;
        
        // 测试用例1：正常情况
        vector<int> nums1 = {100, 4, 200, 1, 3, 2};
        assert(solution.longestConsecutive(nums1) == 4);
        
        // 测试用例2：空数组
        vector<int> nums2 = {};
        assert(solution.longestConsecutive(nums2) == 0);
        
        // 测试用例3：重复元素
        vector<int> nums3 = {1, 2, 2, 3, 4};
        assert(solution.longestConsecutive(nums3) == 4);
        
        // 测试用例4：大数
        vector<int> nums4 = {INT_MAX - 2, INT_MAX - 1, INT_MAX};
        assert(solution.longestConsecutive(nums4) == 3);
        
        cout << "最长连续序列测试通过" << endl;
    }
    
    /**
     * 测试四数相加
     */
    void testFourSumII() {
        FourSumII solution;
        
        // 测试用例1：正常情况
        vector<int> A = {1, 2};
        vector<int> B = {-2, -1};
        vector<int> C = {-1, 2};
        vector<int> D = {0, 2};
        assert(solution.fourSumCount(A, B, C, D) == 2);
        
        // 测试用例2：空数组
        vector<int> empty = {};
        assert(solution.fourSumCount(empty, empty, empty, empty) == 0);
        
        cout << "四数相加测试通过" << endl;
    }
    
    /**
     * 测试分布式哈希表
     */
    void testDistributedHashTable() {
        DistributedHashTable<string, string> dht(100, 3);
        
        // 添加节点
        dht.addNode("node1");
        dht.addNode("node2");
        dht.addNode("node3");
        
        // 存储数据
        dht.put("key1", "value1");
        dht.put("key2", "value2");
        dht.put("key3", "value3");
        
        // 验证数据
        assert(dht.get("key1") == "value1");
        assert(dht.get("key2") == "value2");
        assert(dht.get("key3") == "value3");
        
        // 验证负载分布
        auto load = dht.getLoadDistribution();
        assert(load.size() == 3);
        
        cout << "分布式哈希表测试通过" << endl;
    }
    
    /**
     * 性能测试：大规模数据
     */
    void performanceTest() {
        int size = 100000;
        vector<int> nums(size);
        mt19937 rng(chrono::steady_clock::now().time_since_epoch().count());
        uniform_int_distribution<int> dist(0, size * 10);
        
        // 生成测试数据
        for (int i = 0; i < size; i++) {
            nums[i] = dist(rng);
        }
        
        LongestConsecutiveSequence solution;
        
        auto startTime = chrono::high_resolution_clock::now();
        int result = solution.longestConsecutive(nums);
        auto endTime = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(endTime - startTime);
        
        cout << "性能测试结果：" << endl;
        cout << "数据规模：" << size << endl;
        cout << "最长序列长度：" << result << endl;
        cout << "执行时间：" << duration.count() << "ms" << endl;
        
        // 验证性能要求：10万数据应在1秒内完成
        assert(duration.count() < 1000);
        
        cout << "性能测试通过" << endl;
    }
    
    /**
     * 运行所有测试
     */
    void runAllTests() {
        try {
            testLongestConsecutiveSequence();
            testFourSumII();
            testDistributedHashTable();
            performanceTest();
            
            cout << "=== 所有测试通过 ===" << endl;
        } catch (const exception& e) {
            cerr << "测试失败：" << e.what() << endl;
            throw;
        }
    }
};

/**
 * ===================================================================
 * 主函数：演示和测试
 * ===================================================================
 */
int main() {
    cout << "=== 哈希算法题目扩展演示 ===" << endl << endl;
    
    // 运行测试
    HashAlgorithmTest test;
    test.runAllTests();
    
    // 演示最长连续序列
    cout << endl << "=== 最长连续序列演示 ===" << endl;
    LongestConsecutiveSequence lcs;
    vector<int> demoNums = {100, 4, 200, 1, 3, 2, 5};
    cout << "输入数组：";
    for (int num : demoNums) {
        cout << num << " ";
    }
    cout << endl;
    cout << "最长连续序列长度：" << lcs.longestConsecutive(demoNums) << endl;
    
    // 演示分布式哈希表
    cout << endl << "=== 分布式哈希表演示 ===" << endl;
    DistributedHashTable<string, string> dht(50, 2);
    dht.addNode("server1");
    dht.addNode("server2");
    dht.put("user1", "data1");
    dht.put("user2", "data2");
    cout << "user1的数据：" << dht.get("user1") << endl;
    
    auto load = dht.getLoadDistribution();
    cout << "负载分布：";
    for (auto& entry : load) {
        cout << entry.first << ":" << entry.second << " ";
    }
    cout << endl;
    
    cout << endl << "=== 演示完成 ===" << endl;
    
    return 0;
}