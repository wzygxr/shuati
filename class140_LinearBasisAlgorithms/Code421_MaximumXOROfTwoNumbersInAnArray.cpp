#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * LeetCode 421. 数组中两个数的最大异或值
 * 题目链接：https://leetcode.com/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 解题思路：
 * 这道题可以用字典树（前缀树）来实现线性基的功能。我们将每个数的二进制表示从最高位到最低位插入字典树中，
 * 然后对于每个数，在字典树中查找能够与其产生最大异或值的另一个数。
 * 
 * 时间复杂度：O(n * 32)，其中n是数组长度，32是整数的二进制位数
 * 空间复杂度：O(n * 32)
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
    
    TrieNode* root; // 字典树根节点
    const int HIGH_BIT = 30; // 整数的最高位是第30位（假设是32位整数）
    
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
    int query(int num) {
        TrieNode* node = root;
        int xorSum = 0;
        
        for (int i = HIGH_BIT; i >= 0; --i) {
            int bit = (num >> i) & 1;
            int desiredBit = 1 - bit; // 希望找到相反的位以获得最大异或值
            
            if (node->children[desiredBit]) {
                // 当前位可以取到1，增加异或和
                xorSum |= (1 << i);
                node = node->children[desiredBit];
            } else {
                // 只能取相同的位
                node = node->children[bit];
            }
        }
        
        return xorSum;
    }
    
public:
    Solution() {
        root = nullptr;
    }
    
    ~Solution() {
        if (root) delete root;
    }
    
    /**
     * 找到数组中两个数的最大异或值
     * @param nums 输入数组
     * @return 最大异或值
     */
    int findMaximumXOR(vector<int>& nums) {
        // 边界情况处理
        if (nums.size() <= 1) {
            return 0;
        }
        
        // 创建字典树
        root = new TrieNode();
        int maxXor = 0;
        
        // 插入第一个数
        insert(nums[0]);
        
        // 对于每个数，先查询最大异或值，再插入到字典树中
        for (int i = 1; i < nums.size(); ++i) {
            maxXor = max(maxXor, query(nums[i]));
            insert(nums[i]);
        }
        
        return maxXor;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {3, 10, 5, 25, 2, 8};
    cout << "测试用例1结果: " << solution.findMaximumXOR(nums1) << endl; // 预期输出: 28
    
    // 测试用例2
    vector<int> nums2 = {14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70};
    cout << "测试用例2结果: " << solution.findMaximumXOR(nums2) << endl; // 预期输出: 127
    
    return 0;
}