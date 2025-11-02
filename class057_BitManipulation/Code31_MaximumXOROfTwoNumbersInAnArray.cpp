/**
 * 数组中两个数的最大异或值
 * 测试链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，返回 nums[i] XOR nums[j] 的最大运算结果，其中 0 ≤ i ≤ j < nums.length 。
 * 
 * 解题思路：
 * 1. 暴力法：双重循环计算所有异或值
 * 2. 字典树法：使用Trie树存储二进制表示
 * 3. 哈希集合法：利用异或性质进行优化
 * 4. 位运算技巧：逐位确定最大值
 * 
 * 时间复杂度：O(n) - 使用字典树或哈希集合
 * 空间复杂度：O(n) - 需要存储字典树或哈希集合
 */
#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>
using namespace std;

class Code31_MaximumXOROfTwoNumbersInAnArray {
public:
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    int findMaximumXOR1(vector<int>& nums) {
        int max_val = 0;
        int n = nums.size();
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                max_val = max(max_val, nums[i] ^ nums[j]);
            }
        }
        return max_val;
    }
    
    /**
     * 方法2：字典树法（最优解）
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    class TrieNode {
    public:
        TrieNode* children[2];  // 0和1两个分支
        
        TrieNode() {
            children[0] = nullptr;
            children[1] = nullptr;
        }
        
        ~TrieNode() {
            if (children[0]) delete children[0];
            if (children[1]) delete children[1];
        }
    };
    
    int findMaximumXOR2(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        // 创建字典树根节点
        TrieNode* root = new TrieNode();
        
        // 构建字典树
        for (int num : nums) {
            TrieNode* node = root;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                if (node->children[bit] == nullptr) {
                    node->children[bit] = new TrieNode();
                }
                node = node->children[bit];
            }
        }
        
        int max_val = 0;
        // 对每个数字，在字典树中寻找最大异或值
        for (int num : nums) {
            TrieNode* node = root;
            int current_max = 0;
            for (int i = 31; i >= 0; i--) {
                int bit = (num >> i) & 1;
                int desired_bit = 1 - bit;  // 希望找到相反的位
                
                if (node->children[desired_bit] != nullptr) {
                    current_max |= (1 << i);
                    node = node->children[desired_bit];
                } else {
                    node = node->children[bit];
                }
            }
            max_val = max(max_val, current_max);
        }
        
        delete root;  // 释放内存
        return max_val;
    }
    
    /**
     * 方法3：哈希集合法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int findMaximumXOR3(vector<int>& nums) {
        int max_val = 0, mask = 0;
        
        for (int i = 31; i >= 0; i--) {
            mask |= (1 << i);
            unordered_set<int> prefix_set;
            
            // 提取前缀
            for (int num : nums) {
                prefix_set.insert(num & mask);
            }
            
            // 尝试设置当前位为1
            int temp = max_val | (1 << i);
            for (int prefix : prefix_set) {
                if (prefix_set.find(temp ^ prefix) != prefix_set.end()) {
                    max_val = temp;
                    break;
                }
            }
        }
        
        return max_val;
    }
    
    /**
     * 方法4：位运算优化版
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     */
    int findMaximumXOR4(vector<int>& nums) {
        int max_result = 0;
        int mask = 0;
        
        // 从最高位开始尝试
        for (int i = 31; i >= 0; i--) {
            // 设置掩码，只保留前i位
            mask = mask | (1 << i);
            
            unordered_set<int> left_bits;
            for (int num : nums) {
                left_bits.insert(num & mask);
            }
            
            // 尝试设置当前位为1
            int greedy_try = max_result | (1 << i);
            for (int left_bit : left_bits) {
                // 如果存在两个数使得它们的异或等于greedy_try
                if (left_bits.find(greedy_try ^ left_bit) != left_bits.end()) {
                    max_result = greedy_try;
                    break;
                }
            }
        }
        
        return max_result;
    }
    
    /**
     * 测试方法
     */
    static void test() {
        Code31_MaximumXOROfTwoNumbersInAnArray solution;
        
        // 测试用例1：正常情况
        vector<int> nums1 = {3, 10, 5, 25, 2, 8};
        int result1 = solution.findMaximumXOR1(nums1);
        int result2 = solution.findMaximumXOR2(nums1);
        int result3 = solution.findMaximumXOR3(nums1);
        int result4 = solution.findMaximumXOR4(nums1);
        cout << "测试用例1 - 输入: [3, 10, 5, 25, 2, 8]" << endl;
        cout << "方法1结果: " << result1 << " (预期: 28)" << endl;
        cout << "方法2结果: " << result2 << " (预期: 28)" << endl;
        cout << "方法3结果: " << result3 << " (预期: 28)" << endl;
        cout << "方法4结果: " << result4 << " (预期: 28)" << endl;
        
        // 测试用例2：边界情况（两个元素）
        vector<int> nums2 = {1, 2};
        int result5 = solution.findMaximumXOR2(nums2);
        cout << "测试用例2 - 输入: [1, 2]" << endl;
        cout << "方法2结果: " << result5 << " (预期: 3)" << endl;
        
        // 测试用例3：边界情况（一个元素）
        vector<int> nums3 = {5};
        int result6 = solution.findMaximumXOR2(nums3);
        cout << "测试用例3 - 输入: [5]" << endl;
        cout << "方法2结果: " << result6 << " (预期: 0)" << endl;
        
        // 复杂度分析
        cout << "\n=== 复杂度分析 ===" << endl;
        cout << "方法1 - 暴力法:" << endl;
        cout << "  时间复杂度: O(n²)" << endl;
        cout << "  空间复杂度: O(1)" << endl;
        
        cout << "方法2 - 字典树法:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        cout << "方法3 - 哈希集合法:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        cout << "方法4 - 位运算优化版:" << endl;
        cout << "  时间复杂度: O(n)" << endl;
        cout << "  空间复杂度: O(n)" << endl;
        
        // 工程化考量
        cout << "\n=== 工程化考量 ===" << endl;
        cout << "1. 方法选择：" << endl;
        cout << "   - 实际工程：方法2（字典树法）最优" << endl;
        cout << "   - 面试场景：方法2（字典树法）最优" << endl;
        cout << "2. 性能优化：避免暴力法，使用高效数据结构" << endl;
        cout << "3. 边界处理：处理空数组和单元素数组" << endl;
        cout << "4. 内存管理：C++需要手动释放字典树内存" << endl;
        
        // C++特性考量
        cout << "\n=== C++特性考量 ===" << endl;
        cout << "1. 内存管理：使用析构函数释放字典树" << endl;
        cout << "2. 标准库：使用unordered_set提高查找效率" << endl;
        cout << "3. 引用传递：使用vector引用避免拷贝" << endl;
        cout << "4. 智能指针：实际工程中可使用智能指针" << endl;
        
        // 算法技巧总结
        cout << "\n=== 算法技巧总结 ===" << endl;
        cout << "1. 字典树应用：高效处理二进制前缀" << endl;
        cout << "2. 贪心策略：从高位到低位确定最大值" << endl;
        cout << "3. 哈希集合：利用集合特性快速查找" << endl;
        cout << "4. 位掩码：逐位构建最大异或值" << endl;
    }
};

int main() {
    Code31_MaximumXOROfTwoNumbersInAnArray::test();
    return 0;
}