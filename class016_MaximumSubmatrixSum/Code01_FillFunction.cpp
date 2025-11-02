// ==================================================================================
// 题目1：子矩阵的最大累加和（填函数风格 - C++版本）
// ==================================================================================
// 题目来源：牛客网 (NowCoder)
// 题目链接：https://www.nowcoder.com/practice/840eee05dccd4ffd8f9433ce8085946b
// 难度等级：中等
// 
// ==================================================================================
// C++实现特点
// ==================================================================================
// 1. 使用vector<vector<int>>代替二维数组，更安全
// 2. 使用INT_MIN（定义在<climits>）代替Java的Integer.MIN_VALUE
// 3. 使用std::max代替Math.max
// 4. C++没有内置数组越界检查，需要自己小心
// 
// ==================================================================================
// C++编译命令
// ==================================================================================
// g++ -std=c++11 -O2 Code01_FillFunction.cpp -o solution
// ./solution
//
// 或使用在线评测系统自动编译
//
// ==================================================================================

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>  // INT_MIN, INT_MAX
#include <set>      // 用于LeetCode 363
#include <map>      // 用于LeetCode 1074
#include <unordered_map> // 用于Codeforces 977F
#include <string>   // 用于字符串操作
#include <sstream>  // 用于字符串流
#include <functional> // 用于greater
#include <queue>    // 用于优先队列
#include <stack>    // 用于栈操作
using namespace std;

class Solution {
public:
    // ==================================================================================
    // 主要函数：子矩阵最大累加和（对外接口）
    // ==================================================================================
    // 这是填函数风格的对外接口，在线评测平台会调用此函数
    //
    // 参数：
    // - mat: 输入矩阵
    // - n: 矩阵行数（在这个版本中，n也表示列数，即方阵）
    //
    // 返回值：
    // - 最大子矩阵和
    //
    // 注意：C++版本中，我们使用vector<vector<int>>以获得更好的安全性
    int sumOfSubMatrix(vector<vector<int>>& mat, int n) {
        if (mat.empty() || mat[0].empty()) return 0;
        
        int rows = mat.size();
        int cols = mat[0].size();
        int maxSum = INT_MIN;
        
        // 枚举所有可能的上下边界
        for (int top = 0; top < rows; top++) {
            vector<int> colSum(cols, 0); // 存储每列在[top, bottom]范围内的和
            
            for (int bottom = top; bottom < rows; bottom++) {
                // 更新列和
                for (int col = 0; col < cols; col++) {
                    colSum[col] += mat[bottom][col];
                }
                
                // 对列和数组应用Kadane算法
                int currentSum = 0;
                for (int col = 0; col < cols; col++) {
                    currentSum = max(colSum[col], currentSum + colSum[col]);
                    maxSum = max(maxSum, currentSum);
                }
            }
        }
        
        return maxSum;
    }
    
    // ==================================================================================
    // 题目2：最大子数组乘积（LeetCode 152）
    // ==================================================================================
    // 题目链接：https://leetcode.com/problems/maximum-product-subarray/
    // 难度：中等
    //
    // 题目描述：
    // 给你一个整数数组 nums，请你找出数组中乘积最大的连续子数组（该子数组中至少包含一个数字），
    // 并返回该子数组所对应的乘积。
    //
    // 示例：
    // 输入：[2,3,-2,4]
    // 输出：6
    // 解释：[2,3] 有最大乘积 6
    //
    // ==================================================================================
    // 核心算法：动态规划（维护最大值和最小值）
    // ==================================================================================
    // 【算法思想】
    // 由于存在负数，最大值可能变成最小值，最小值可能变成最大值
    // 因此需要同时维护当前的最大值和最小值
    //
    // 【时间复杂度】O(n)
    // - 只需一次遍历数组
    //
    // 【空间复杂度】O(1)
    // - 只使用常数个变量
    //
    // 【是否为最优解】是的！
    // - 这是解决最大子数组乘积问题的最优解法
    int maxProduct(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int maxProd = nums[0];
        int minProd = nums[0];
        int result = nums[0];
        
        for (int i = 1; i < nums.size(); i++) {
            // 如果当前数是负数，交换最大值和最小值
            if (nums[i] < 0) {
                swap(maxProd, minProd);
            }
            
            // 更新最大值和最小值
            maxProd = max(nums[i], maxProd * nums[i]);
            minProd = min(nums[i], minProd * nums[i]);
            
            // 更新结果
            result = max(result, maxProd);
        }
        
        return result;
    }
    
    // ==================================================================================
    // 题目3：环形数组的最大子数组和（LeetCode 918）
    // ==================================================================================
    // 题目链接：https://leetcode.com/problems/maximum-sum-circular-subarray/
    // 难度：中等
    //
    // 题目描述：
    // 给定一个由整数数组 A 表示的环形数组 C，求 C 的非空子数组的最大可能和。
    // 环形数组意味着数组的末端将会与开头相连呈环状。
    //
    // 示例：
    // 输入：[5,-3,5]
    // 输出：10
    // 解释：子数组 [5,5] 有最大和 5 + 5 = 10
    //
    // ==================================================================================
    // 核心算法：Kadane算法 + 环形数组技巧
    // ==================================================================================
    // 【算法思想】
    // 环形数组的最大子数组和有两种情况：
    // 1. 正常情况：最大子数组在数组中间（使用普通Kadane算法）
    // 2. 环形情况：最大子数组跨越数组首尾（总和 - 最小子数组和）
    //
    // 【时间复杂度】O(n)
    // - 需要两次遍历数组
    //
    // 【空间复杂度】O(1)
    // - 只使用常数个变量
    //
    // 【是否为最优解】是的！
    // - 这是解决环形数组最大子数组和问题的最优解法
    int maxSubarraySumCircular(vector<int>& nums) {
        if (nums.empty()) return 0;
        
        int total = 0;
        int maxSum = INT_MIN;
        int minSum = INT_MAX;
        int currentMax = 0;
        int currentMin = 0;
        
        for (int num : nums) {
            total += num;
            
            // 普通Kadane算法求最大子数组和
            currentMax = max(num, currentMax + num);
            maxSum = max(maxSum, currentMax);
            
            // 求最小子数组和（用于环形情况）
            currentMin = min(num, currentMin + num);
            minSum = min(minSum, currentMin);
        }
        
        // 如果所有数都是负数，返回最大单个元素
        if (maxSum < 0) return maxSum;
        
        // 返回两种情况的最大值
        return max(maxSum, total - minSum);
    }
    
    // ==================================================================================
    // 题目4：和为目标值的子矩阵数量（LeetCode 1074）
    // ==================================================================================
    // 题目链接：https://leetcode.com/problems/number-of-submatrices-that-sum-to-target/
    // 难度：困难
    //
    // 题目描述：
    // 给出矩阵 matrix 和目标值 target，返回元素总和等于目标值的非空子矩阵的数量。
    //
    // 示例：
    // 输入：matrix = [[0,1,0],[1,1,1],[0,1,0]], target = 0
    // 输出：4
    // 解释：四个只含 0 的 1x1 子矩阵。
    //
    // ==================================================================================
    // 核心算法：前缀和 + 哈希表
    // ==================================================================================
    // 【算法思想】
    // 1. 枚举所有可能的上下边界
    // 2. 计算每列在上下边界范围内的前缀和
    // 3. 使用哈希表记录前缀和出现的次数
    // 4. 对于每个右边界，查找满足条件的左边界
    //
    // 【时间复杂度】O(m² * n)
    // - m是行数，n是列数
    //
    // 【空间复杂度】O(n)
    // - 需要哈希表存储前缀和
    //
    // 【是否为最优解】是的！
    // - 这是解决子矩阵和等于目标值问题的最优解法
    int numSubmatrixSumTarget(vector<vector<int>>& matrix, int target) {
        if (matrix.empty() || matrix[0].empty()) return 0;
        
        int rows = matrix.size();
        int cols = matrix[0].size();
        int count = 0;
        
        // 枚举上边界
        for (int top = 0; top < rows; top++) {
            vector<int> colSum(cols, 0);
            
            // 枚举下边界
            for (int bottom = top; bottom < rows; bottom++) {
                // 更新列和
                for (int col = 0; col < cols; col++) {
                    colSum[col] += matrix[bottom][col];
                }
                
                // 使用哈希表记录前缀和
                unordered_map<int, int> prefixSumCount;
                prefixSumCount[0] = 1; // 前缀和为0出现1次
                int currentSum = 0;
                
                // 遍历所有列（相当于一维数组）
                for (int col = 0; col < cols; col++) {
                    currentSum += colSum[col];
                    
                    // 查找满足 currentSum - prefixSum = target 的前缀和
                    if (prefixSumCount.find(currentSum - target) != prefixSumCount.end()) {
                        count += prefixSumCount[currentSum - target];
                    }
                    
                    // 更新前缀和出现次数
                    prefixSumCount[currentSum]++;
                }
            }
        }
        
        return count;
    }
    
    // ==================================================================================
    // 题目5：最大权矩形（洛谷 P1719）
    // ==================================================================================
    // 题目链接：https://www.luogu.com.cn/problem/P1719
    // 难度：普及/提高-
    //
    // 题目描述：
    // 给定一个N*N的矩阵，求其最大子矩阵和。
    //
    // 示例：
    // 输入：
    // 4
    // 0 -2 -7 0
    // 9 2 -6 2
    // -4 1 -4 1
    // -1 8 0 -2
    // 输出：15
    //
    // ==================================================================================
    // 核心算法：二维压缩 + Kadane算法
    // ==================================================================================
    // 【算法思想】
    // 与题目1相同，都是最大子矩阵和问题
    //
    // 【时间复杂度】O(n³)
    // - 需要三重循环
    //
    // 【空间复杂度】O(n)
    // - 需要存储列和数组
    //
    // 【是否为最优解】是的！
    // - 对于最大子矩阵和问题，这是最优解法
    int maxWeightRectangle(vector<vector<int>>& matrix) {
        return sumOfSubMatrix(matrix, matrix.size());
    }
};

// ==================================================================================
// 测试主函数
// ==================================================================================
int main() {
    Solution solution;
    
    cout << "开始测试最大子矩阵和相关算法..." << endl;
    
    // 测试1：最大子矩阵和
    vector<vector<int>> matrix1 = {{1, 2, 3}, {-4, 5, -6}, {7, 8, 9}};
    int result1 = solution.sumOfSubMatrix(matrix1, 3);
    cout << "测试1 - 最大子矩阵和: " << result1 << " (期望: 27)" << endl;
    
    // 测试2：最大子数组乘积
    vector<int> nums2 = {2, 3, -2, 4};
    int result2 = solution.maxProduct(nums2);
    cout << "测试2 - 最大子数组乘积: " << result2 << " (期望: 6)" << endl;
    
    // 测试3：环形数组最大子数组和
    vector<int> nums3 = {5, -3, 5};
    int result3 = solution.maxSubarraySumCircular(nums3);
    cout << "测试3 - 环形数组最大子数组和: " << result3 << " (期望: 10)" << endl;
    
    // 测试4：和为目标值的子矩阵数量
    vector<vector<int>> matrix4 = {{0, 1, 0}, {1, 1, 1}, {0, 1, 0}};
    int result4 = solution.numSubmatrixSumTarget(matrix4, 0);
    cout << "测试4 - 和为目标值的子矩阵数量: " << result4 << " (期望: 4)" << endl;
    
    cout << "所有测试完成！" << endl;
    
    return 0;
}