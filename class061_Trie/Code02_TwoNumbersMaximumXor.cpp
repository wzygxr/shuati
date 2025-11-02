// LeetCode 421. 数组中两个数的最大异或值 - C++实现
// 题目描述：给定一个整数数组，返回数组中两个数的最大异或值。
// 测试链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 最大异或值问题 - 使用前缀树优化
 * 
 * 算法思路：
 * 1. 将每个整数转换为二进制表示（最高位在前）
 * 2. 构建前缀树，每个节点表示一个二进制位
 * 3. 对于每个数，在前缀树中寻找能与其产生最大异或值的路径
 * 4. 最大异或值即为所求结果
 * 
 * 时间复杂度分析：
 * - 构建前缀树：O(N*32)，其中N是数组长度，32是整数的位数
 * - 查询操作：O(N*32)，对每个数进行一次查询
 * - 总体时间复杂度：O(N*32) = O(N)
 * 
 * 空间复杂度分析：
 * - 前缀树空间：O(N*32)，最坏情况下存储所有二进制位
 * - 总体空间复杂度：O(N)
 * 
 * 是否最优解：是
 * 理由：前缀树方法的时间复杂度为O(N)，优于暴力解法的O(N²)
 * 
 * 工程化考虑：
 * 1. 位运算优化：使用位运算提高效率
 * 2. 边界情况处理：处理数组长度为0或1的情况
 * 3. 内存管理：合理管理前缀树节点内存
 */

class TrieNode {
public:
    // 子节点：0和1
    TrieNode* children[2];
    
    /**
     * 初始化前缀树节点
     */
    TrieNode() {
        children[0] = nullptr;
        children[1] = nullptr;
    }
    
    /**
     * 析构函数，释放子节点内存
     */
    ~TrieNode() {
        if (children[0]) delete children[0];
        if (children[1]) delete children[1];
    }
};

class Trie {
private:
    TrieNode* root;
    
public:
    /**
     * 初始化前缀树
     */
    Trie() {
        root = new TrieNode();
    }
    
    /**
     * 析构函数
     */
    ~Trie() {
        delete root;
    }
    
    /**
     * 插入一个整数到前缀树中
     * 
     * @param num 待插入的整数
     */
    void insert(int num) {
        TrieNode* node = root;
        // 从最高位到最低位插入
        for (int i = 30; i >= 0; i--) {  // 假设是31位整数（不包括符号位）
            int bit = (num >> i) & 1;
            if (!node->children[bit]) {
                node->children[bit] = new TrieNode();
            }
            node = node->children[bit];
        }
    }
    
    /**
     * 查找与给定整数产生最大异或值的数
     * 
     * @param num 给定的整数
     * @return 最大异或值
     */
    int findMaxXOR(int num) {
        TrieNode* node = root;
        int maxXOR = 0;
        
        for (int i = 30; i >= 0; i--) {
            int currentBit = (num >> i) & 1;
            int desiredBit = 1 - currentBit;  // 希望找到相反的位以获得最大异或
            
            if (node->children[desiredBit]) {
                // 如果存在相反的位，选择该路径，并设置结果的对应位为1
                maxXOR |= (1 << i);
                node = node->children[desiredBit];
            } else {
                // 否则只能选择相同的位
                node = node->children[currentBit];
            }
        }
        
        return maxXOR;
    }
};

/**
 * 计算数组中两个数的最大异或值
 * 
 * @param nums 整数数组
 * @return 最大异或值
 */
int findMaximumXOR(vector<int>& nums) {
    if (nums.size() <= 1) return 0;
    
    Trie trie;
    // 先插入第一个数
    trie.insert(nums[0]);
    
    int maxResult = 0;
    // 对于每个后续的数，查找最大异或值并更新结果
    for (int i = 1; i < nums.size(); i++) {
        maxResult = max(maxResult, trie.findMaxXOR(nums[i]));
        trie.insert(nums[i]);
    }
    
    return maxResult;
}

// 测试代码
int main() {
    // 测试用例1
    vector<int> nums1 = {3, 10, 5, 25, 2, 8};
    cout << "测试用例1结果: " << findMaximumXOR(nums1) << endl;  // 应该输出28 (25 XOR 3)
    
    // 测试用例2
    vector<int> nums2 = {14, 70, 53, 83, 49, 91, 36, 80, 92, 51, 66, 70};
    cout << "测试用例2结果: " << findMaximumXOR(nums2) << endl;  // 应该输出127
    
    // 测试用例3：边界情况
    vector<int> nums3 = {0};
    cout << "测试用例3结果: " << findMaximumXOR(nums3) << endl;  // 应该输出0
    
    vector<int> nums4 = {1, 2, 3, 4, 5, 6, 7, 8};
    cout << "测试用例4结果: " << findMaximumXOR(nums4) << endl;  // 应该输出15 (7 XOR 8)
    
    return 0;
}