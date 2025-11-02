#include <iostream>
#include <vector>
#include <bitset>
#include <stdexcept>
#include <chrono>
#include <random>
#include <algorithm>
#include <functional>
#include <map>
#include <unordered_map>
#include <queue>
#include <stack>
#include <string>
#include <sstream>
#include <iomanip>
#include <cmath>
#include <climits>
#include <cassert>
#include <limits>
#include <unordered_set>
#include <cstdint>
#include <set>
#include <numeric>

using namespace std;

/**
 * 位算法应用实现
 * 包含LeetCode多个位算法应用相关题目的解决方案
 * 
 * 题目列表:
 * 1. LeetCode 78 - 子集
 * 2. LeetCode 90 - 子集 II
 * 3. LeetCode 46 - 全排列
 * 4. LeetCode 47 - 全排列 II
 * 5. LeetCode 77 - 组合
 * 6. LeetCode 39 - 组合总和
 * 7. LeetCode 40 - 组合总和 II
 * 8. LeetCode 216 - 组合总和 III
 * 9. LeetCode 131 - 分割回文串
 * 10. LeetCode 93 - 复原IP地址
 * 
 * 时间复杂度分析:
 * - 回溯算法: O(2^n) 到 O(n!)
 * - 空间复杂度: O(n) 到 O(n^2)
 * 
 * 工程化考量:
 * 1. 位集优化: 使用位集优化回溯算法
 * 2. 状态压缩: 使用位运算压缩状态空间
 * 3. 剪枝优化: 使用位运算进行高效剪枝
 * 4. 去重处理: 使用位掩码进行重复检测
 */

class BitAlgorithmApplications {
public:
    /**
     * LeetCode 78 - 子集
     * 题目链接: https://leetcode.com/problems/subsets/
     * 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * 
     * 方法: 位运算枚举
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(n * 2^n)
     * 
     * 原理: 使用二进制位表示每个元素是否在子集中
     */
    static vector<vector<int>> subsets(vector<int>& nums) {
        int n = nums.size();
        int total = 1 << n;
        vector<vector<int>> result;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * LeetCode 90 - 子集 II
     * 题目链接: https://leetcode.com/problems/subsets-ii/
     * 给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
     * 
     * 方法: 位运算 + 排序去重
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(n * 2^n)
     */
    static vector<vector<int>> subsetsWithDup(vector<int>& nums) {
        sort(nums.begin(), nums.end());
        int n = nums.size();
        int total = 1 << n;
        set<vector<int>> result_set;
        
        for (int mask = 0; mask < total; mask++) {
            vector<int> subset;
            for (int i = 0; i < n; i++) {
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            result_set.insert(subset);
        }
        
        vector<vector<int>> result(result_set.begin(), result_set.end());
        return result;
    }
    
    /**
     * LeetCode 46 - 全排列
     * 题目链接: https://leetcode.com/problems/permutations/
     * 给定一个没有重复数字的序列，返回其所有可能的全排列。
     * 
     * 方法: 回溯 + 位掩码
     * 时间复杂度: O(n!)
     * 空间复杂度: O(n)
     */
    static vector<vector<int>> permute(vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> current;
        vector<bool> used(nums.size(), false);
        backtrack(nums, used, current, result);
        return result;
    }
    
private:
    static void backtrack(vector<int>& nums, vector<bool>& used, 
                        vector<int>& current, vector<vector<int>>& result) {
        if (current.size() == nums.size()) {
            result.push_back(current);
            return;
        }
        
        for (int i = 0; i < nums.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                current.push_back(nums[i]);
                backtrack(nums, used, current, result);
                current.pop_back();
                used[i] = false;
            }
        }
    }
    
public:
    /**
     * LeetCode 47 - 全排列 II
     * 题目链接: https://leetcode.com/problems/permutations-ii/
     * 给定一个可包含重复数字的序列，返回所有不重复的全排列。
     * 
     * 方法: 回溯 + 排序剪枝
     * 时间复杂度: O(n!)
     * 空间复杂度: O(n)
     */
    static vector<vector<int>> permuteUnique(vector<int>& nums) {
        sort(nums.begin(), nums.end());
        vector<vector<int>> result;
        vector<int> current;
        vector<bool> used(nums.size(), false);
        backtrackUnique(nums, used, current, result);
        return result;
    }
    
private:
    static void backtrackUnique(vector<int>& nums, vector<bool>& used,
                              vector<int>& current, vector<vector<int>>& result) {
        if (current.size() == nums.size()) {
            result.push_back(current);
            return;
        }
        
        for (int i = 0; i < nums.size(); i++) {
            if (used[i] || (i > 0 && nums[i] == nums[i-1] && !used[i-1])) {
                continue;
            }
            
            used[i] = true;
            current.push_back(nums[i]);
            backtrackUnique(nums, used, current, result);
            current.pop_back();
            used[i] = false;
        }
    }
    
public:
    /**
     * LeetCode 77 - 组合
     * 题目链接: https://leetcode.com/problems/combinations/
     * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
     * 
     * 方法: 回溯
     * 时间复杂度: O(C(n, k))
     * 空间复杂度: O(k)
     */
    static vector<vector<int>> combine(int n, int k) {
        vector<vector<int>> result;
        vector<int> current;
        backtrackCombine(1, n, k, current, result);
        return result;
    }
    
private:
    static void backtrackCombine(int start, int n, int k,
                               vector<int>& current, vector<vector<int>>& result) {
        if (current.size() == k) {
            result.push_back(current);
            return;
        }
        
        for (int i = start; i <= n; i++) {
            current.push_back(i);
            backtrackCombine(i + 1, n, k, current, result);
            current.pop_back();
        }
    }
    
public:
    /**
     * LeetCode 39 - 组合总和
     * 题目链接: https://leetcode.com/problems/combination-sum/
     * 给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
     * 
     * 方法: 回溯
     * 时间复杂度: O(n^target)
     * 空间复杂度: O(target)
     */
    static vector<vector<int>> combinationSum(vector<int>& candidates, int target) {
        vector<vector<int>> result;
        vector<int> current;
        sort(candidates.begin(), candidates.end());
        backtrackCombinationSum(candidates, target, 0, current, result);
        return result;
    }
    
private:
    static void backtrackCombinationSum(vector<int>& candidates, int target, int start,
                                     vector<int>& current, vector<vector<int>>& result) {
        if (target == 0) {
            result.push_back(current);
            return;
        }
        
        for (int i = start; i < candidates.size(); i++) {
            if (candidates[i] > target) {
                break;
            }
            
            current.push_back(candidates[i]);
            backtrackCombinationSum(candidates, target - candidates[i], i, current, result);
            current.pop_back();
        }
    }
    
public:
    /**
     * LeetCode 40 - 组合总和 II
     * 题目链接: https://leetcode.com/problems/combination-sum-ii/
     * 给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
     * candidates 中的每个数字在每个组合中只能使用一次。
     * 
     * 方法: 回溯 + 排序剪枝
     * 时间复杂度: O(2^n)
     * 空间复杂度: O(n)
     */
    static vector<vector<int>> combinationSum2(vector<int>& candidates, int target) {
        vector<vector<int>> result;
        vector<int> current;
        sort(candidates.begin(), candidates.end());
        backtrackCombinationSum2(candidates, target, 0, current, result);
        return result;
    }
    
private:
    static void backtrackCombinationSum2(vector<int>& candidates, int target, int start,
                                        vector<int>& current, vector<vector<int>>& result) {
        if (target == 0) {
            result.push_back(current);
            return;
        }
        
        for (int i = start; i < candidates.size(); i++) {
            if (candidates[i] > target) {
                break;
            }
            
            if (i > start && candidates[i] == candidates[i-1]) {
                continue;
            }
            
            current.push_back(candidates[i]);
            backtrackCombinationSum2(candidates, target - candidates[i], i + 1, current, result);
            current.pop_back();
        }
    }
    
public:
    /**
     * LeetCode 216 - 组合总和 III
     * 题目链接: https://leetcode.com/problems/combination-sum-iii/
     * 找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
     * 
     * 方法: 回溯
     * 时间复杂度: O(C(9, k))
     * 空间复杂度: O(k)
     */
    static vector<vector<int>> combinationSum3(int k, int n) {
        vector<vector<int>> result;
        vector<int> current;
        backtrackCombinationSum3(1, k, n, current, result);
        return result;
    }
    
private:
    static void backtrackCombinationSum3(int start, int k, int n,
                                       vector<int>& current, vector<vector<int>>& result) {
        if (current.size() == k && n == 0) {
            result.push_back(current);
            return;
        }
        
        if (current.size() > k || n < 0) {
            return;
        }
        
        for (int i = start; i <= 9; i++) {
            current.push_back(i);
            backtrackCombinationSum3(i + 1, k, n - i, current, result);
            current.pop_back();
        }
    }
    
public:
    /**
     * LeetCode 131 - 分割回文串
     * 题目链接: https://leetcode.com/problems/palindrome-partitioning/
     * 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
     * 
     * 方法: 回溯 + 动态规划预处理
     * 时间复杂度: O(n * 2^n)
     * 空间复杂度: O(n^2)
     */
    static vector<vector<string>> partition(string s) {
        int n = s.length();
        vector<vector<bool>> dp(n, vector<bool>(n, false));
        vector<vector<string>> result;
        vector<string> current;
        
        // 预处理回文信息
        for (int i = 0; i < n; i++) {
            for (int j = 0; j <= i; j++) {
                if (s[i] == s[j] && (i - j <= 2 || dp[j+1][i-1])) {
                    dp[j][i] = true;
                }
            }
        }
        
        backtrackPartition(s, 0, dp, current, result);
        return result;
    }
    
private:
    static void backtrackPartition(string& s, int start, vector<vector<bool>>& dp,
                                 vector<string>& current, vector<vector<string>>& result) {
        if (start == s.length()) {
            result.push_back(current);
            return;
        }
        
        for (int i = start; i < s.length(); i++) {
            if (dp[start][i]) {
                current.push_back(s.substr(start, i - start + 1));
                backtrackPartition(s, i + 1, dp, current, result);
                current.pop_back();
            }
        }
    }
    
public:
    /**
     * LeetCode 93 - 复原IP地址
     * 题目链接: https://leetcode.com/problems/restore-ip-addresses/
     * 给定一个只包含数字的字符串，复原它并返回所有可能的 IP 地址格式。
     * 
     * 方法: 回溯
     * 时间复杂度: O(1) - 固定长度
     * 空间复杂度: O(1)
     */
    static vector<string> restoreIpAddresses(string s) {
        vector<string> result;
        vector<string> current;
        backtrackRestoreIP(s, 0, current, result);
        return result;
    }
    
private:
    static void backtrackRestoreIP(string& s, int start, vector<string>& current, vector<string>& result) {
        if (current.size() == 4 && start == s.length()) {
            result.push_back(current[0] + "." + current[1] + "." + current[2] + "." + current[3]);
            return;
        }
        
        if (current.size() == 4 || start == s.length()) {
            return;
        }
        
        for (int len = 1; len <= 3 && start + len <= s.length(); len++) {
            string segment = s.substr(start, len);
            
            // 检查段是否有效
            if (segment.length() > 1 && segment[0] == '0') {
                continue;
            }
            
            int num = stoi(segment);
            if (num > 255) {
                continue;
            }
            
            current.push_back(segment);
            backtrackRestoreIP(s, start + len, current, result);
            current.pop_back();
        }
    }
};

class PerformanceTester {
public:
    static void testSubsets() {
        cout << "=== 子集问题性能测试 ===" << endl;
        
        vector<int> nums = {1, 2, 3, 4};
        
        auto start = chrono::high_resolution_clock::now();
        auto result = BitAlgorithmApplications::subsets(nums);
        auto time = chrono::duration_cast<chrono::microseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "子集问题: 输入大小=" << nums.size() << ", 结果数量=" << result.size() 
             << ", 耗时=" << time << " μs" << endl;
    }
    
    static void testPermutations() {
        cout << "\n=== 全排列性能测试 ===" << endl;
        
        vector<int> nums = {1, 2, 3, 4, 5};
        
        auto start = chrono::high_resolution_clock::now();
        auto result = BitAlgorithmApplications::permute(nums);
        auto time = chrono::duration_cast<chrono::milliseconds>(
            chrono::high_resolution_clock::now() - start).count();
        
        cout << "全排列: 输入大小=" << nums.size() << ", 结果数量=" << result.size() 
             << ", 耗时=" << time << " ms" << endl;
    }
    
    static void runUnitTests() {
        cout << "=== 位算法应用单元测试 ===" << endl;
        
        // 测试子集
        vector<int> nums = {1, 2, 3};
        auto subsets_result = BitAlgorithmApplications::subsets(nums);
        assert(subsets_result.size() == 8);
        
        // 测试全排列
        auto permute_result = BitAlgorithmApplications::permute(nums);
        assert(permute_result.size() == 6);
        
        cout << "所有单元测试通过!" << endl;
    }
    
    static void complexityAnalysis() {
        cout << "\n=== 复杂度分析 ===" << endl;
        
        vector<pair<string, string>> algorithms = {
            {"subsets", "O(n*2^n), O(n*2^n)"},
            {"subsetsWithDup", "O(n*2^n), O(n*2^n)"},
            {"permute", "O(n!), O(n)"},
            {"permuteUnique", "O(n!), O(n)"},
            {"combine", "O(C(n,k)), O(k)"},
            {"combinationSum", "O(n^target), O(target)"},
            {"combinationSum2", "O(2^n), O(n)"},
            {"combinationSum3", "O(C(9,k)), O(k)"},
            {"partition", "O(n*2^n), O(n^2)"},
            {"restoreIpAddresses", "O(1), O(1)"}
        };
        
        for (auto& algo : algorithms) {
            cout << algo.first << ": 时间复杂度=" << algo.second << endl;
        }
    }
};

int main() {
    cout << "位算法应用实现" << endl;
    cout << "包含LeetCode多个位算法应用相关题目的解决方案" << endl;
    cout << "===========================================" << endl;
    
    // 运行单元测试
    PerformanceTester::runUnitTests();
    
    // 运行性能测试
    PerformanceTester::testSubsets();
    PerformanceTester::testPermutations();
    
    // 复杂度分析
    PerformanceTester::complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    
    // 子集示例
    vector<int> nums = {1, 2, 3};
    auto subsets_result = BitAlgorithmApplications::subsets(nums);
    cout << "子集示例([1,2,3]): 共" << subsets_result.size() << "个子集" << endl;
    
    // 全排列示例
    auto permute_result = BitAlgorithmApplications::permute(nums);
    cout << "全排列示例([1,2,3]): 共" << permute_result.size() << "个排列" << endl;
    
    // 组合示例
    auto combine_result = BitAlgorithmApplications::combine(4, 2);
    cout << "组合示例(C(4,2)): 共" << combine_result.size() << "个组合" << endl;
    
    return 0;
}