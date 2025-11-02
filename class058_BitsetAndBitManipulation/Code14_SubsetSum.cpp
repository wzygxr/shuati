#include <iostream>
#include <vector>
#include <numeric>
#include <bitset>
#include <algorithm>
#include <stdexcept>
#include <chrono>
#include <cstdlib>  // 添加rand()函数支持
#include <ctime>    // 添加时间种子支持

using namespace std;
using namespace std::chrono;

/**
 * LeetCode 416 Partition Equal Subset Sum - 子集和问题
 * 题目链接: https://leetcode.com/problems/partition-equal-subset-sum/
 * 题目描述: 给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等
 * 
 * 解题思路:
 * 方法1: 回溯法 - 暴力搜索所有子集，时间复杂度高
 * 方法2: 动态规划（0-1背包问题） - 使用二维DP数组
 * 方法3: 动态规划优化 - 使用一维DP数组，空间优化
 * 方法4: Bitset优化 - 使用位运算加速DP
 * 
 * 时间复杂度分析:
 * 方法1: O(2^N) - 指数级，不可行
 * 方法2: O(N * sum) - 伪多项式时间
 * 方法3: O(N * sum) - 空间优化版本
 * 方法4: O(N * sum/64) - 使用bitset优化常数因子
 * 
 * 空间复杂度分析:
 * 方法1: O(N) - 递归栈空间
 * 方法2: O(N * sum) - 二维DP数组
 * 方法3: O(sum) - 一维DP数组
 * 方法4: O(sum/64) - bitset空间
 * 
 * 工程化考量:
 * 1. 输入验证: 检查数组是否为空，元素是否为正整数
 * 2. 边界处理: 处理总和为奇数的情况（直接返回false）
 * 3. 性能优化: 根据数据规模选择最优算法
 * 4. 内存管理: 使用bitset优化大内存消耗
 */

class SubsetSumSolver {
public:
    /**
     * 方法1: 回溯法（仅用于教学，实际不可行）
     */
    static bool canPartitionBacktrack(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        return backtrack(nums, 0, 0, target);
    }
    
private:
    static bool backtrack(const vector<int>& nums, int index, int currentSum, int target) {
        if (currentSum == target) return true;
        if (currentSum > target || index >= nums.size()) return false;
        
        // 选择当前元素
        if (backtrack(nums, index + 1, currentSum + nums[index], target)) {
            return true;
        }
        
        // 不选择当前元素
        return backtrack(nums, index + 1, currentSum, target);
    }
    
public:
    /**
     * 方法2: 动态规划（二维DP）
     */
    static bool canPartitionDP2D(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.size();
        
        vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
        
        // 初始化：和为0的子集总是存在
        for (int i = 0; i <= n; i++) {
            dp[i][0] = true;
        }
        
        // 动态规划填表
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= target; j++) {
                // 不选当前元素
                dp[i][j] = dp[i - 1][j];
                
                // 选当前元素
                if (j >= nums[i - 1]) {
                    dp[i][j] = dp[i][j] || dp[i - 1][j - nums[i - 1]];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 方法3: 动态规划优化（一维DP）
     */
    static bool canPartitionDP1D(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.size();
        
        vector<bool> dp(target + 1, false);
        dp[0] = true;
        
        for (int i = 0; i < n; i++) {
            // 从后往前遍历避免重复计算
            for (int j = target; j >= nums[i]; j--) {
                dp[j] = dp[j] || dp[j - nums[i]];
            }
            
            // 提前终止
            if (dp[target]) return true;
        }
        
        return dp[target];
    }
    
    /**
     * 方法4: Bitset优化（最优解）
     * 使用C++的bitset进行优化
     */
    static bool canPartitionBitset(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        
        // 使用bitset，每个bit代表一个和是否可达
        bitset<10001> bitSet;  // 假设target最大为10000
        bitSet[0] = 1;  // 和为0可达
        
        for (int num : nums) {
            // 将bitset左移num位，然后与原来的bitset取或
            bitSet |= (bitSet << num);
            
            // 提前终止
            if (bitSet[target]) return true;
        }
        
        return bitSet[target];
    }
    
    /**
     * 方法5: 动态bitset（适用于大target）
     * 使用vector<bool>模拟动态bitset
     */
    static bool canPartitionDynamicBitset(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        
        vector<bool> dp(target + 1, false);
        dp[0] = true;
        
        for (int num : nums) {
            // 从后往前更新
            for (int j = target; j >= num; j--) {
                if (dp[j - num]) {
                    dp[j] = true;
                }
            }
            
            if (dp[target]) return true;
        }
        
        return dp[target];
    }
    
    /**
     * 优化版本：根据数据规模选择算法
     */
    static bool canPartitionOptimized(const vector<int>& nums) {
        if (nums.empty()) return false;
        
        int totalSum = accumulate(nums.begin(), nums.end(), 0);
        if (totalSum % 2 != 0) return false;
        
        int target = totalSum / 2;
        int n = nums.size();
        
        // 根据数据规模选择算法
        if (n <= 20) {
            return canPartitionBacktrack(nums);
        } else if (target <= 10000) {
            return canPartitionDP1D(nums);
        } else {
            return canPartitionDynamicBitset(nums);
        }
    }
    
    /**
     * 工程化改进版本：完整的异常处理
     */
    static bool canPartitionWithValidation(const vector<int>& nums) {
        try {
            // 输入验证
            if (nums.empty()) {
                return false;
            }
            
            // 检查元素是否为正整数
            for (int num : nums) {
                if (num <= 0) {
                    throw invalid_argument("All elements must be positive integers");
                }
            }
            
            return canPartitionOptimized(nums);
            
        } catch (const exception& e) {
            cerr << "Error in canPartition: " << e.what() << endl;
            return false;
        }
    }
};

/**
 * 性能测试工具类
 */
class PerformanceTester {
public:
    static void runTests() {
        cout << "=== LeetCode 416 Partition Equal Subset Sum - 单元测试 ===" << endl;
        
        // 测试用例
        vector<pair<vector<int>, bool>> testCases = {
            {{1, 5, 11, 5}, true},    // 可以平分
            {{1, 2, 3, 5}, false},    // 不能平分（总和为奇数）
            {{}, false}               // 空数组
        };
        
        for (const auto& testCase : testCases) {
            const auto& nums = testCase.first;
            bool expected = testCase.second;
            bool result = SubsetSumSolver::canPartitionOptimized(nums);
            
            cout << "测试: ";
            for (int num : nums) cout << num << " ";
            cout << ", 期望=" << (expected ? "true" : "false")
                 << ", 实际=" << (result ? "true" : "false")
                 << ", " << (result == expected ? "通过" : "失败") << endl;
        }
        
        // 方法一致性测试
        cout << "\n=== 方法一致性测试 ===" << endl;
        vector<int> testNums = {1, 5, 10, 6};
        
        bool r1 = SubsetSumSolver::canPartitionDP2D(testNums);
        bool r2 = SubsetSumSolver::canPartitionDP1D(testNums);
        bool r3 = SubsetSumSolver::canPartitionBitset(testNums);
        bool r4 = SubsetSumSolver::canPartitionOptimized(testNums);
        
        cout << "二维DP: " << (r1 ? "true" : "false") << endl;
        cout << "一维DP: " << (r2 ? "true" : "false") << endl;
        cout << "Bitset: " << (r3 ? "true" : "false") << endl;
        cout << "优化法: " << (r4 ? "true" : "false") << endl;
        cout << "所有方法结果一致: " << (r1 == r2 && r2 == r3 && r3 == r4 ? "是" : "否") << endl;
    }
    
    static void performanceTest() {
        cout << "\n=== 性能测试 ===" << endl;
        
        // 生成测试数据
        vector<vector<int>> testCases = {
            generateRandomArray(20, 100),    // 小规模
            generateRandomArray(100, 100),   // 中等规模
            generateRandomArray(200, 100)    // 较大规模
        };
        
        for (int i = 0; i < testCases.size(); i++) {
            const auto& nums = testCases[i];
            cout << "测试用例 " << i + 1 << ": 数组长度=" << nums.size() << endl;
            
            // 测试一维DP
            auto start = high_resolution_clock::now();
            bool result1 = SubsetSumSolver::canPartitionDP1D(nums);
            auto time1 = duration_cast<nanoseconds>(high_resolution_clock::now() - start);
            
            // 测试Bitset优化
            start = high_resolution_clock::now();
            bool result2 = SubsetSumSolver::canPartitionBitset(nums);
            auto time2 = duration_cast<nanoseconds>(high_resolution_clock::now() - start);
            
            cout << "  一维DP: " << time1.count() << " ns, 结果: " << (result1 ? "true" : "false") << endl;
            cout << "  Bitset: " << time2.count() << " ns, 结果: " << (result2 ? "true" : "false") << endl;
            
            if (time1 > 0) {
                double ratio = static_cast<double>(time1) / time2;
                cout << "  Bitset比一维DP快: " << ratio << "倍" << endl;
            }
            cout << endl;
        }
    }
    
private:
    static vector<int> generateRandomArray(int size, int maxValue) {
        vector<int> arr(size);
        for (int i = 0; i < size; i++) {
            arr[i] = rand() % maxValue + 1;
        }
        return arr;
    }
};

/**
 * 复杂度分析
 */
void complexityAnalysis() {
    cout << "=== 复杂度分析 ===" << endl;
    cout << "方法1（回溯法）:" << endl;
    cout << "  时间复杂度: O(2^N)" << endl;
    cout << "  空间复杂度: O(N)" << endl;
    cout << "  适用场景: N <= 20" << endl;
    
    cout << "\n方法2（二维DP）:" << endl;
    cout << "  时间复杂度: O(N * sum)" << endl;
    cout << "  空间复杂度: O(N * sum)" << endl;
    cout << "  适用场景: 小规模数据" << endl;
    
    cout << "\n方法3（一维DP）:" << endl;
    cout << "  时间复杂度: O(N * sum)" << endl;
    cout << "  空间复杂度: O(sum)" << endl;
    cout << "  适用场景: 中等规模数据" << endl;
    
    cout << "\n方法4（Bitset优化）:" << endl;
    cout << "  时间复杂度: O(N * sum/64)" << endl;
    cout << "  空间复杂度: O(sum/64)" << endl;
    cout << "  适用场景: 大规模数据" << endl;
    
    cout << "\n工程化建议:" << endl;
    cout << "1. 使用bitset可以获得常数级别的性能优化" << endl;
    cout << "2. 注意C++ bitset的大小需要在编译时确定" << endl;
    cout << "3. 对于动态大小的bitset，使用vector<bool>" << endl;
}

int main() {
    cout << "LeetCode 416 Partition Equal Subset Sum - 子集和问题" << endl;
    cout << "判断是否可以将数组分割成两个和相等的子集" << endl;
    
    // 运行单元测试
    PerformanceTester::runTests();
    
    // 运行性能测试
    PerformanceTester::performanceTest();
    
    // 复杂度分析
    complexityAnalysis();
    
    // 示例使用
    cout << "\n=== 示例使用 ===" << endl;
    vector<vector<int>> examples = {
        {1, 5, 11, 5},
        {1, 2, 3, 5},
        {1, 2, 3, 4, 5, 6, 7}
    };
    
    for (const auto& nums : examples) {
        bool result = SubsetSumSolver::canPartitionWithValidation(nums);
        cout << "数组: ";
        for (int num : nums) cout << num << " ";
        cout << " -> 是否可以平分: " << (result ? "true" : "false") << endl;
    }
    
    return 0;
}