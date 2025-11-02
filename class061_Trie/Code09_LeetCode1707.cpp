// LeetCode 1707. 与数组中元素的最大异或值 - C++实现
// 
// 题目描述：
// 给定一个数组和查询数组，每个查询包含x和m，找出数组中满足num <= m的元素与x的最大异或值。
// 
// 测试链接：https://leetcode.cn/problems/maximum-xor-with-an-element-from-array/
// 
// 算法思路：
// 1. 离线查询 + 前缀树：将查询和数组排序，按顺序插入前缀树并回答查询
// 2. 构建二进制前缀树，支持最大异或值查询
// 3. 使用离线处理技巧，避免重复构建前缀树
// 
// 时间复杂度分析：
// - 排序：O(N log N + Q log Q)，其中N是数组长度，Q是查询数量
// - 前缀树操作：O((N + Q) * 32)，32是整数的位数
// - 总体时间复杂度：O(N log N + Q log Q + (N + Q) * 32)
// 
// 空间复杂度分析：
// - 前缀树空间：O(N * 32)
// - 排序空间：O(N + Q)
// - 总体空间复杂度：O(N * 32 + Q)
// 
// 是否最优解：是
// 理由：离线查询+前缀树是最优解法，避免了重复构建前缀树
// 
// 工程化考虑：
// 1. 异常处理：处理空数组和非法查询
// 2. 边界情况：数组为空或查询为空的情况
// 3. 极端输入：大量查询或大数值的情况
// 4. 内存管理：合理管理前缀树内存
// 
// 语言特性差异：
// C++：使用指针实现前缀树，性能高且节省空间
// Java：使用数组实现，更安全但空间固定
// Python：使用字典实现，代码更简洁
// 
// 调试技巧：
// 1. 打印中间结果验证排序和查询处理
// 2. 使用小规模测试数据验证算法正确性
// 3. 单元测试覆盖各种边界条件

#include <iostream>
#include <vector>
#include <algorithm>
#include <memory>
#include <cassert>
#include <chrono>
#include <random>

using namespace std;

/**
 * 查询结构体，用于存储查询信息和索引
 */
struct Query {
    int x;          // 查询值
    int m;          // 最大值限制
    int index;      // 原始索引
    
    Query(int x_val, int m_val, int idx) : x(x_val), m(m_val), index(idx) {}
};

/**
 * 二进制前缀树节点类
 */
class TrieNode {
public:
    unique_ptr<TrieNode> children[2]; // 0和1两个子节点
    bool is_end;
    
    TrieNode() : is_end(false) {
        children[0] = nullptr;
        children[1] = nullptr;
    }
};

/**
 * 二进制前缀树类
 */
class BinaryTrie {
private:
    unique_ptr<TrieNode> root;
    
public:
    BinaryTrie() : root(make_unique<TrieNode>()) {}
    
    /**
     * 向前缀树中插入数字
     */
    void insert(int num) {
        TrieNode* node = root.get();
        for (int i = 31; i >= 0; i--) {
            int bit = (num >> i) & 1;
            if (node->children[bit] == nullptr) {
                node->children[bit] = make_unique<TrieNode>();
            }
            node = node->children[bit].get();
        }
        node->is_end = true;
    }
    
    /**
     * 查询与x的最大异或值
     */
    int maxXor(int x) {
        if (root->children[0] == nullptr && root->children[1] == nullptr) {
            return -1; // 空树
        }
        
        TrieNode* node = root.get();
        int result = 0;
        
        for (int i = 31; i >= 0; i--) {
            int bit = (x >> i) & 1;
            int opposite = 1 - bit;
            
            if (node->children[opposite] != nullptr) {
                result |= (1 << i);
                node = node->children[opposite].get();
            } else {
                node = node->children[bit].get();
            }
        }
        
        return result;
    }
};

/**
 * 主函数：计算每个查询的最大异或值
 */
vector<int> maximizeXor(vector<int>& nums, vector<vector<int>>& queries) {
    int n = nums.size();
    int q = queries.size();
    
    // 对数组排序
    sort(nums.begin(), nums.end());
    
    // 创建查询数组，按m值排序
    vector<Query> queryArr;
    for (int i = 0; i < q; i++) {
        queryArr.emplace_back(queries[i][0], queries[i][1], i);
    }
    
    // 按m值排序查询
    sort(queryArr.begin(), queryArr.end(), 
         [](const Query& a, const Query& b) { return a.m < b.m; });
    
    // 初始化前缀树
    BinaryTrie trie;
    vector<int> result(q, -1);
    int idx = 0;
    
    // 离线处理查询
    for (const auto& query : queryArr) {
        // 将数组中<=m的元素插入前缀树
        while (idx < n && nums[idx] <= query.m) {
            trie.insert(nums[idx]);
            idx++;
        }
        
        // 查询最大异或值
        result[query.index] = trie.maxXor(query.x);
    }
    
    return result;
}

/**
 * 单元测试函数
 */
void testMaximizeXor() {
    // 测试用例1：基础测试
    vector<int> nums1 = {0, 1, 2, 3, 4};
    vector<vector<int>> queries1 = {{3, 1}, {1, 3}, {5, 6}};
    vector<int> result1 = maximizeXor(nums1, queries1);
    vector<int> expected1 = {3, 3, 7};
    assert(result1 == expected1);
    
    // 测试用例2：空数组
    vector<int> nums2 = {};
    vector<vector<int>> queries2 = {{1, 1}};
    vector<int> result2 = maximizeXor(nums2, queries2);
    vector<int> expected2 = {-1};
    assert(result2 == expected2);
    
    // 测试用例3：单个元素
    vector<int> nums3 = {5};
    vector<vector<int>> queries3 = {{1, 10}, {10, 1}};
    vector<int> result3 = maximizeXor(nums3, queries3);
    vector<int> expected3 = {5, -1};
    assert(result3 == expected3);
    
    cout << "所有单元测试通过！" << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    // 生成大规模测试数据
    int n = 100000;
    int q = 100000;
    vector<int> nums(n);
    vector<vector<int>> queries(q, vector<int>(2));
    
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(0, 1000000000);
    
    for (int i = 0; i < n; i++) {
        nums[i] = dis(gen);
    }
    
    for (int i = 0; i < q; i++) {
        queries[i][0] = dis(gen);
        queries[i][1] = dis(gen);
    }
    
    auto start = chrono::high_resolution_clock::now();
    vector<int> result = maximizeXor(nums, queries);
    auto end = chrono::high_resolution_clock::now();
    
    auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
    cout << "大规模测试耗时: " << duration.count() << "ms" << endl;
    cout << "处理了 " << n << " 个数字和 " << q << " 个查询" << endl;
}

int main() {
    // 运行单元测试
    testMaximizeXor();
    
    // 运行性能测试
    performanceTest();
    
    return 0;
}