#include <iostream>
#include <vector>
#include <bitset>

using namespace std;

/**
 * 汉明距离总和
 * 测试链接：https://leetcode.cn/problems/total-hamming-distance/
 * 
 * 题目描述：
 * 两个整数的 汉明距离 指的是这两个数字的二进制数对应位不同的数量。
 * 给你一个整数数组 nums，请你计算并返回 nums 中任意两个数之间汉明距离的总和。
 * 
 * 解题思路：
 * 1. 暴力法：双重循环计算所有组合（会超时）
 * 2. 位运算法：逐位计算贡献值
 * 3. 数学优化：利用组合数学优化计算
 * 
 * 时间复杂度分析：
 * - 暴力法：O(n²)，会超时
 * - 位运算法：O(n * 32)，32位整数
 * - 数学优化：O(n * 32)
 * 
 * 空间复杂度分析：
 * - 所有方法：O(1)，只使用常数空间
 */
class Solution {
public:
    /**
     * 方法1：暴力法（不推荐，会超时）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(1)
     */
    int totalHammingDistance1(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                total += bitset<32>(nums[i] ^ nums[j]).count();
            }
        }
        
        return total;
    }
    
    /**
     * 方法2：位运算法（推荐）
     * 核心思想：逐位计算每个位的贡献
     * 对于每个位，统计有多少个数的该位是1（设为count）
     * 那么该位的贡献就是 count * (n - count)
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    int totalHammingDistance2(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        
        // 遍历32位（整数最多32位）
        for (int i = 0; i < 32; i++) {
            int countOnes = 0;
            
            // 统计当前位为1的数的个数
            for (int num : nums) {
                countOnes += (num >> i) & 1;
            }
            
            // 当前位的贡献：countOnes * (n - countOnes)
            total += countOnes * (n - countOnes);
        }
        
        return total;
    }
    
    /**
     * 方法3：数学优化版
     * 使用更高效的位运算技巧
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    int totalHammingDistance3(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        
        for (int i = 0; i < 32; i++) {
            int mask = 1 << i;
            int count = 0;
            
            for (int num : nums) {
                if ((num & mask) != 0) {
                    count++;
                }
            }
            
            total += count * (n - count);
        }
        
        return total;
    }
    
    /**
     * 方法4：使用GCC内置函数优化
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(1)
     */
    int totalHammingDistance4(vector<int>& nums) {
        int total = 0;
        int n = nums.size();
        
        for (int bitPos = 0; bitPos < 32; bitPos++) {
            int ones = 0;
            
            for (int num : nums) {
                // 检查特定位是否为1
                if (((num >> bitPos) & 1) == 1) {
                    ones++;
                }
            }
            
            // 当前位的汉明距离贡献
            total += ones * (n - ones);
        }
        
        return total;
    }
    
    /**
     * 方法5：分组统计法
     * 将数字按位分组统计
     * 时间复杂度：O(n * 32)
     * 空间复杂度：O(32)
     */
    int totalHammingDistance5(vector<int>& nums) {
        if (nums.empty()) {
            return 0;
        }
        
        int total = 0;
        int n = nums.size();
        
        // 创建32个桶，每个桶统计对应位的1的个数
        vector<int> bitCounts(32, 0);
        
        for (int num : nums) {
            for (int i = 0; i < 32; i++) {
                if ((num & (1 << i)) != 0) {
                    bitCounts[i]++;
                }
            }
        }
        
        // 计算总汉明距离
        for (int count : bitCounts) {
            total += count * (n - count);
        }
        
        return total;
    }
};

/**
 * 测试函数
 */
int main() {
    Solution solution;
    
    // 测试用例1：基础情况
    vector<int> nums1 = {4, 14, 2};
    int result1 = solution.totalHammingDistance2(nums1);
    cout << "测试用例1 - 输入: [4, 14, 2]" << endl;
    cout << "结果: " << result1 << " (预期: 6)" << endl;
    
    // 测试用例2：两个相同数字
    vector<int> nums2 = {4, 4};
    int result2 = solution.totalHammingDistance2(nums2);
    cout << "测试用例2 - 输入: [4, 4]" << endl;
    cout << "结果: " << result2 << " (预期: 0)" << endl;
    
    // 测试用例3：三个不同数字
    vector<int> nums3 = {1, 2, 3};
    int result3 = solution.totalHammingDistance2(nums3);
    cout << "测试用例3 - 输入: [1, 2, 3]" << endl;
    cout << "结果: " << result3 << " (预期: 4)" << endl;
    
    // 测试用例4：边界情况（单个元素）
    vector<int> nums4 = {5};
    int result4 = solution.totalHammingDistance2(nums4);
    cout << "测试用例4 - 输入: [5]" << endl;
    cout << "结果: " << result4 << " (预期: 0)" << endl;
    
    // 性能测试
    vector<int> largeNums(1000);
    for (int i = 0; i < 1000; i++) {
        largeNums[i] = i; // 0到999的序列
    }
    
    auto start = chrono::high_resolution_clock::now();
    int result5 = solution.totalHammingDistance2(largeNums);
    auto end = chrono::high_resolution_clock::now();
    auto duration = chrono::duration_cast<chrono::microseconds>(end - start);
    cout << "性能测试 - 输入长度: " << largeNums.size() << endl;
    cout << "结果: " << result5 << endl;
    cout << "耗时: " << duration.count() << "微秒" << endl;
    
    // 复杂度分析
    cout << "\n=== 复杂度分析 ===" << endl;
    cout << "方法1 - 暴力法:" << endl;
    cout << "  时间复杂度: O(n²) - 会超时" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法2 - 位运算法:" << endl;
    cout << "  时间复杂度: O(n * 32)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法3 - 数学优化版:" << endl;
    cout << "  时间复杂度: O(n * 32)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法4 - GCC内置函数法:" << endl;
    cout << "  时间复杂度: O(n * 32)" << endl;
    cout << "  空间复杂度: O(1)" << endl;
    
    cout << "方法5 - 分组统计法:" << endl;
    cout << "  时间复杂度: O(n * 32)" << endl;
    cout << "  空间复杂度: O(32)" << endl;
    
    // 工程化考量
    cout << "\n=== 工程化考量 ===" << endl;
    cout << "1. 算法选择：方法2是最优选择" << endl;
    cout << "2. 性能优化：避免O(n²)的暴力解法" << endl;
    cout << "3. 边界处理：处理空数组和单元素数组" << endl;
    cout << "4. 可读性：清晰的数学公式解释" << endl;
    
    // 算法技巧总结
    cout << "\n=== 算法技巧总结 ===" << endl;
    cout << "1. 组合数学：C(k,2) = k*(k-1)/2 的变形应用" << endl;
    cout << "2. 位运算：逐位统计1的个数" << endl;
    cout << "3. 贡献值计算：每个位的独立贡献可以分开计算" << endl;
    cout << "4. 数学优化：利用对称性减少计算量" << endl;
    
    // 数学原理说明
    cout << "\n=== 数学原理说明 ===" << endl;
    cout << "对于每个位位置：" << endl;
    cout << "  设该位为1的数字有k个，为0的数字有m个" << endl;
    cout << "  那么该位产生的汉明距离贡献为：k * m" << endl;
    cout << "  因为每个1和每个0的组合都会产生1的贡献" << endl;
    cout << "  总贡献 = Σ(每个位的k * m)" << endl;
    
    return 0;
}