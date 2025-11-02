#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * LeetCode 1707. 与数组中元素的最大异或值
 * 题目链接：https://leetcode.com/problems/maximum-xor-with-an-element-from-array/
 * 
 * 解题思路：
 * 这道题是上一题的扩展，需要处理带约束条件的查询。我们可以采用离线处理的方法：
 * 1. 将nums数组排序
 * 2. 将queries数组排序，并记录原始索引
 * 3. 按照mi从小到大的顺序处理查询，将nums中不超过mi的元素插入字典树
 * 4. 对于每个查询，在字典树中查询最大异或值
 * 
 * 时间复杂度：O(n log n + q log q + (n + q) * 32)，其中n是数组长度，q是查询数量
 * 空间复杂度：O(n * 32 + q)
 */
class Solution {
private:
    // 字典树节点结构定义
    struct TrieNode {
        TrieNode* children[2]; // 0和1两个子节点
        
        // 构造函数，初始化子节点为nullptr
        TrieNode() {
            children[0] = children[1] = nullptr;
        }
        
        // 析构函数，递归释放子节点内存
        ~TrieNode() {
            if (children[0]) delete children[0];
            if (children[1]) delete children[1];
        }
    };
    
    TrieNode* root;
    const int HIGH_BIT = 30;
    
    /**
     * 将数字插入字典树
     * @param num 要插入的数字
     */
    void insert(int num) {
        TrieNode* node = root;
        // 从最高位开始处理每一位
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1; // 提取当前位
            if (!node->children[bit]) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
        }
    }
    
    /**
     * 查询与给定数字异或的最大值
     * @param num 给定数字
     * @return 最大异或值
     */
    int queryMaxXor(int num) {
        TrieNode* node = root;
        int maxXor = 0;
        
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit; // 希望找到相反的位以获得最大异或值
            
            if (node->children[desiredBit]) {
                // 当前位可以取到1，增加异或和
                maxXor |= (1 << i);
                node = node->children[desiredBit];
            } else {
                // 只能取相同的位
                node = node->children[bit];
            }
        }
        
        return maxXor;
    }
    
public:
    Solution() {
        root = nullptr;
    }
    
    ~Solution() {
        if (root) delete root;
    }
    
    /**
     * 处理每个查询，返回每个查询的最大异或值
     * @param nums 输入数组
     * @param queries 查询数组，每个查询包含[xi, mi]
     * @return 每个查询的最大异或值数组
     */
    vector<int> maximizeXor(vector<int>& nums, vector<vector<int>>& queries) {
        // 边界情况处理
        if (nums.empty() || queries.empty()) {
            return {};
        }
        
        // 创建字典树
        root = new TrieNode();
        int n = nums.size();
        int q = queries.size();
        vector<int> result(q);
        
        // 对nums数组排序
        sort(nums.begin(), nums.end());
        
        // 对queries进行排序，并记录原始索引
        vector<tuple<int, int, int>> sortedQueries;
        for (int i = 0; i < q; ++i) {
            sortedQueries.emplace_back(queries[i][1], queries[i][0], i);
        }
        sort(sortedQueries.begin(), sortedQueries.end());
        
        int numIndex = 0;
        for (auto& query : sortedQueries) {
            int mi = get<0>(query);
            int xi = get<1>(query);
            int originalIndex = get<2>(query);
            
            // 将nums中不超过mi的元素插入字典树
            while (numIndex < n && nums[numIndex] <= mi) {
                insert(nums[numIndex]);
                numIndex++;
            }
            
            // 如果字典树为空，说明没有符合条件的元素
            if (numIndex == 0) {
                result[originalIndex] = -1;
            } else {
                result[originalIndex] = queryMaxXor(xi);
            }
        }
        
        return result;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {0, 1, 2, 3, 4};
    vector<vector<int>> queries1 = {{3, 1}, {1, 3}, {5, 6}};
    vector<int> result1 = solution.maximizeXor(nums1, queries1);
    cout << "测试用例1结果: [";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl; // 预期输出: [3, 3, 7]
    
    // 测试用例2
    vector<int> nums2 = {5, 2, 4, 6, 6, 3};
    vector<vector<int>> queries2 = {{12, 4}, {8, 1}, {6, 3}};
    vector<int> result2 = solution.maximizeXor(nums2, queries2);
    cout << "测试用例2结果: [";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl; // 预期输出: [15, -1, 5]
    
    return 0;
}