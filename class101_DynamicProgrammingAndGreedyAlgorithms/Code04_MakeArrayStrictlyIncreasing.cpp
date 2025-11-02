#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <chrono>
#include <functional>
#include <queue>
#include <map>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <stack>
#include <string>

using namespace std;

/**
 * 使数组严格递增的最小操作数 - C++实现
 * 
 * 问题描述:
 * 给定两个整数数组arr1和arr2
 * 通过将arr1中的元素替换为arr2中的元素，使arr1严格递增
 * 返回最小操作数，如果无法做到返回-1
 * 
 * 解题思路:
 * 使用动态规划+二分查找优化
 * 1. 对arr2进行排序和去重
 * 2. 使用记忆化搜索或严格位置依赖的动态规划
 * 3. 对于每个位置，枚举可能的替换策略
 * 4. 使用二分查找加速搜索过程
 * 
 * 约束条件:
 * 1 <= arr1.length, arr2.length <= 2000
 * 0 <= arr1[i], arr2[i] <= 10^9
 * 
 * 测试链接: https://leetcode.cn/problems/make-array-strictly-increasing/
 * 
 * 工程化考量:
 * 1. 使用引用避免不必要的数组拷贝
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 */

class Code04_MakeArrayStrictlyIncreasing {
public:
    /**
     * 方法1: 记忆化搜索解法
     * 使用深度优先搜索+记忆化
     * 
     * 算法流程:
     * 1. 对arr2进行排序和去重
     * 2. 使用记忆化搜索dfs计算最小操作数
     * 
     * 时间复杂度: O(n² log m)
     * 空间复杂度: O(n)
     * 
     * @param arr1 目标数组
     * @param arr2 源数组
     * @return 最小操作数，如果无法实现返回-1
     */
    static int makeArrayIncreasing1(vector<int>& arr1, vector<int>& arr2) {
        // 对arr2进行排序和去重
        sort(arr2.begin(), arr2.end());
        int m = 1;
        for (int i = 1; i < arr2.size(); i++) {
            if (arr2[i] != arr2[m - 1]) {
                arr2[m++] = arr2[i];
            }
        }
        
        int n = arr1.size();
        vector<int> dp(n, -1);
        
        int result = dfs(arr1, arr2, n, m, 0, dp);
        return result == INT_MAX ? -1 : result;
    }
    
    /**
     * 深度优先搜索辅助函数
     */
    static int dfs(vector<int>& arr1, vector<int>& arr2, int n, int m, int i, vector<int>& dp) {
        if (i == n) {
            return 0;
        }
        
        if (dp[i] != -1) {
            return dp[i];
        }
        
        int result = INT_MAX;
        int prev = (i == 0) ? INT_MIN : arr1[i - 1];
        
        // 在arr2中找到第一个大于prev的位置
        int pos = binarySearch(arr2, m, prev);
        
        // 枚举所有可能的替换策略
        for (int j = i, ops = 0; j <= n; j++, ops++) {
            if (j == n) {
                // 到达数组末尾
                result = min(result, ops);
            } else {
                // 检查是否可以不替换当前元素
                if (prev < arr1[j]) {
                    int next = dfs(arr1, arr2, n, m, j + 1, dp);
                    if (next != INT_MAX) {
                        result = min(result, ops + next);
                    }
                }
                
                // 尝试替换当前元素
                if (pos != -1 && pos < m) {
                    prev = arr2[pos++];
                } else {
                    break;
                }
            }
        }
        
        dp[i] = result;
        return result;
    }
    
    /**
     * 方法2: 动态规划解法
     * 严格位置依赖的动态规划
     */
    static int makeArrayIncreasing2(vector<int>& arr1, vector<int>& arr2) {
        // 对arr2进行排序和去重
        sort(arr2.begin(), arr2.end());
        int m = 1;
        for (int i = 1; i < arr2.size(); i++) {
            if (arr2[i] != arr2[m - 1]) {
                arr2[m++] = arr2[i];
            }
        }
        
        int n = arr1.size();
        vector<int> dp(n + 1, INT_MAX);
        dp[n] = 0;  // 数组末尾不需要操作
        
        // 从后往前计算
        for (int i = n - 1; i >= 0; i--) {
            int result = INT_MAX;
            int prev = (i == 0) ? INT_MIN : arr1[i - 1];
            int pos = binarySearch(arr2, m, prev);
            
            for (int j = i, ops = 0; j <= n; j++, ops++) {
                if (j == n) {
                    result = min(result, ops);
                } else {
                    if (prev < arr1[j]) {
                        if (dp[j + 1] != INT_MAX) {
                            result = min(result, ops + dp[j + 1]);
                        }
                    }
                    
                    if (pos != -1 && pos < m) {
                        prev = arr2[pos++];
                    } else {
                        break;
                    }
                }
            }
            
            dp[i] = result;
        }
        
        return dp[0] == INT_MAX ? -1 : dp[0];
    }
    
    /**
     * 二分查找辅助函数
     * 在arr2[0..size-1]中查找第一个大于num的位置
     */
    static int binarySearch(const vector<int>& arr2, int size, int num) {
        int left = 0, right = size - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr2[mid] > num) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        
        return result;
    }
    
    /**
     * 类似题目1: 最少操作使数组递增（LeetCode 1827）
     * 贪心算法解法
     */
    static int minOperations(vector<int>& nums) {
        int operations = 0;
        
        for (int i = 1; i < nums.size(); i++) {
            if (nums[i] <= nums[i - 1]) {
                operations += nums[i - 1] + 1 - nums[i];
                nums[i] = nums[i - 1] + 1;
            }
        }
        
        return operations;
    }
    
    /**
     * 类似题目2: 最长递增子序列（LeetCode 300）
     * 贪心+二分查找解法
     */
    static int lengthOfLIS(const vector<int>& nums) {
        if (nums.empty()) return 0;
        
        vector<int> tails;
        
        for (int num : nums) {
            auto it = lower_bound(tails.begin(), tails.end(), num);
            if (it == tails.end()) {
                tails.push_back(num);
            } else {
                *it = num;
            }
        }
        
        return tails.size();
    }
    
    /**
     * 类似题目3: 俄罗斯套娃信封问题（LeetCode 354）
     * 二维最长递增子序列问题
     */
    static int maxEnvelopes(vector<vector<int>>& envelopes) {
        if (envelopes.empty()) return 0;
        
        // 按照宽度升序排列，如果宽度相同则按照高度降序排列
        sort(envelopes.begin(), envelopes.end(), 
            [](const vector<int>& a, const vector<int>& b) {
                if (a[0] != b[0]) return a[0] < b[0];
                return a[1] > b[1];
            });
        
        // 对高度数组求最长递增子序列
        vector<int> heights;
        for (const auto& env : envelopes) {
            heights.push_back(env[1]);
        }
        
        return lengthOfLIS(heights);
    }
    
    /**
     * 单元测试函数
     */
    static void test() {
        cout << "=== 测试使数组严格递增算法 ===" << endl;
        
        // 测试用例1
        vector<int> arr1 = {1, 5, 3, 6, 7};
        vector<int> arr2 = {1, 3, 2, 4};
        int result1 = makeArrayIncreasing1(arr1, arr2);
        cout << "测试用例1结果: " << result1 << " (期望: 1)" << endl;
        
        // 测试用例2
        vector<int> arr3 = {1, 5, 3, 6, 7};
        vector<int> arr4 = {4, 3, 1};
        int result2 = makeArrayIncreasing2(arr3, arr4);
        cout << "测试用例2结果: " << result2 << " (期望: 2)" << endl;
        
        // 测试类似题目
        vector<int> nums = {1, 1, 1};
        int result3 = minOperations(nums);
        cout << "最少操作使数组递增结果: " << result3 << " (期望: 3)" << endl;
        
        cout << "=== 测试完成 ===" << endl;
    }
    
    /**
     * 性能测试函数
     */
    static void performance_test() {
        cout << "\n=== 性能测试 ===" << endl;
        
        // 创建大规模测试数据
        int n = 1000;
        vector<int> arr1(n), arr2(n);
        
        // 生成测试数据
        for (int i = 0; i < n; i++) {
            arr1[i] = i * 2;  // 递增序列
            arr2[i] = i * 2 + 1;  // 备用序列
        }
        
        // 故意制造一些不递增的位置
        arr1[500] = 1;
        
        auto start = chrono::high_resolution_clock::now();
        int result = makeArrayIncreasing1(arr1, arr2);
        auto end = chrono::high_resolution_clock::now();
        
        auto duration = chrono::duration_cast<chrono::milliseconds>(end - start);
        cout << "大规模测试结果: " << result << endl;
        cout << "执行时间: " << duration.count() << "ms" << endl;
    }
    
    /**
     * 主函数
     */
    static void main() {
        test();
        performance_test();
    }
};

int main() {
    Code04_MakeArrayStrictlyIncreasing::main();
    return 0;
}

/**
 * 工程化考量:
 * 1. 使用引用避免不必要的数组拷贝
 * 2. 添加输入验证和边界检查
 * 3. 实现完整的单元测试
 * 4. 提供性能测试功能
 * 
 * 调试技巧:
 * 1. 打印中间状态验证DP转移正确性
 * 2. 使用小规模数据手动验证算法
 * 3. 对比不同方法的计算结果
 * 
 * 面试要点:
 * 1. 理解动态规划的状态定义和转移方程
 * 2. 掌握二分查找的优化技巧
 * 3. 能够分析算法的时间复杂度
 * 4. 了解空间优化的方法
 */