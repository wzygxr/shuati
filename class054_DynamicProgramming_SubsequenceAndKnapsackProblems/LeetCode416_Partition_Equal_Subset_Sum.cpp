#include <iostream>
#include <vector>
#include <numeric>
#include <algorithm>
#include <chrono>
#include <random>
#include <bitset>

using namespace std;

/**
 * LeetCode 416. 分割等和子集
 * 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 * 测试链接：https://leetcode.cn/problems/partition-equal-subset-sum/
 * 
 * 算法详解：
 * 将问题转化为0-1背包问题，使用动态规划求解。
 * 
 * 时间复杂度：O(n * target)，其中target = sum/2
 * 空间复杂度：O(target)（优化版本）
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组有效性
 * 2. 边界处理：总和为奇数的情况直接返回false
 * 3. 性能优化：使用空间优化技术
 * 4. 代码质量：清晰的变量命名和注释
 */

class Solution {
public:
    /**
     * 基础动态规划解法
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(n * target)
     */
    static bool canPartition(const vector<int>& nums) {
        if (nums.empty()) {
            return false;
        }
        
        int n = nums.size();
        int sum = accumulate(nums.begin(), nums.end(), 0);
        
        // 总和为奇数，不可能分割
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        // 检查最大元素
        int maxNum = *max_element(nums.begin(), nums.end());
        if (maxNum > target) {
            return false;
        }
        
        // 创建dp表
        vector<vector<bool>> dp(n + 1, vector<bool>(target + 1, false));
        dp[0][0] = true; // 前0个元素和为0总是可能
        
        for (int i = 1; i <= n; i++) {
            int num = nums[i - 1];
            for (int j = 0; j <= target; j++) {
                if (j < num) {
                    dp[i][j] = dp[i - 1][j];
                } else {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - num];
                }
            }
        }
        
        return dp[n][target];
    }
    
    /**
     * 空间优化版本（使用一维数组）
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(target)
     */
    static bool canPartitionOptimized(const vector<int>& nums) {
        if (nums.empty()) {
            return false;
        }
        
        int sum = accumulate(nums.begin(), nums.end(), 0);
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        int maxNum = *max_element(nums.begin(), nums.end());
        if (maxNum > target) {
            return false;
        }
        
        vector<bool> dp(target + 1, false);
        dp[0] = true;
        
        for (int num : nums) {
            // 从后往前更新，避免重复使用
            for (int j = target; j >= num; j--) {
                dp[j] = dp[j] || dp[j - num];
            }
            
            // 提前终止
            if (dp[target]) {
                return true;
            }
        }
        
        return dp[target];
    }
    
    /**
     * 位运算优化版本
     * 时间复杂度：O(n * target)
     * 空间复杂度：O(target/32) ≈ O(target)
     */
    static bool canPartitionBitMask(const vector<int>& nums) {
        if (nums.empty()) {
            return false;
        }
        
        int sum = accumulate(nums.begin(), nums.end(), 0);
        
        if (sum % 2 != 0) {
            return false;
        }
        
        int target = sum / 2;
        
        int maxNum = *max_element(nums.begin(), nums.end());
        if (maxNum > target) {
            return false;
        }
        
        // 使用bitset（如果target不大）或者vector<bool>
        // 这里使用unsigned long long数组模拟位掩码
        int size = (target + 63) / 64; // 计算需要的unsigned long long数量
        vector<unsigned long long> bitset(size, 0);
        bitset[0] = 1ULL; // 和为0可达
        
        for (int num : nums) {
            // 创建新的位掩码
            vector<unsigned long long> newBitset = bitset;
            
            // 更新位掩码
            for (int i = 0; i < size; i++) {
                if (bitset[i] != 0) {
                    int shift = num;
                    int newIndex = i + (shift / 64);
                    shift %= 64;
                    
                    if (newIndex < size) {
                        newBitset[newIndex] |= (bitset[i] << shift);
                        if (shift > 0 && newIndex + 1 < size) {
                            newBitset[newIndex + 1] |= (bitset[i] >> (64 - shift));
                        }
                    }
                }
            }
            
            bitset = newBitset;
            
            // 检查target是否可达
            int targetIndex = target / 64;
            int targetBit = target % 64;
            if (targetIndex < size && (bitset[targetIndex] & (1ULL << targetBit))) {
                return true;
            }
        }
        
        return false;
    }
};

/**
 * 测试辅助函数
 */
void runTest(const string& description, const vector<int>& nums, bool expected) {
    cout << description << endl;
    cout << "输入数组: [";
    for (size_t i = 0; i < nums.size(); i++) {
        cout << nums[i];
        if (i < nums.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    cout << "期望结果: " << (expected ? "true" : "false") << endl;
    
    bool result1 = Solution::canPartition(nums);
    bool result2 = Solution::canPartitionOptimized(nums);
    bool result3 = Solution::canPartitionBitMask(nums);
    
    cout << "基础DP: " << (result1 ? "true" : "false") 
         << " " << (result1 == expected ? "✓" : "✗") << endl;
    cout << "优化DP: " << (result2 ? "true" : "false") 
         << " " << (result2 == expected ? "✓" : "✗") << endl;
    cout << "位运算: " << (result3 ? "true" : "false") 
         << " " << (result3 == expected ? "✓" : "✗") << endl;
    
    if (result1 == result2 && result2 == result3 && result1 == expected) {
        cout << "测试通过 ✓" << endl;
    } else {
        cout << "测试失败 ✗" << endl;
    }
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据
    const int n = 100;
    vector<int> nums(n);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(1, 100);
    
    int sum = 0;
    for (int i = 0; i < n; i++) {
        nums[i] = dis(gen);
        sum += nums[i];
    }
    
    // 确保总和为偶数
    if (sum % 2 != 0) {
        nums[0]++;
        sum++;
    }
    
    cout << "测试数据规模: " << n << "个元素" << endl;
    cout << "目标总和: " << (sum / 2) << endl;
    
    // 测试优化DP算法
    auto start = chrono::high_resolution_clock::now();
    bool result1 = Solution::canPartitionOptimized(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化DP算法:" << endl;
    cout << "  结果: " << (result1 ? "true" : "false") << endl;
    cout << "  耗时: " << duration1.count() << " 毫秒" << endl;
    
    // 测试位运算算法
    start = chrono::high_resolution_clock::now();
    bool result2 = Solution::canPartitionBitMask(nums);
    end = chrono::high_resolution_clock::now();
    auto duration2 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "位运算算法:" << endl;
    cout << "  结果: " << (result2 ? "true" : "false") << endl;
    cout << "  耗时: " << duration2.count() << " 毫秒" << endl;
    
    // 验证结果一致性
    if (result1 == result2) {
        cout << "结果一致性验证: 通过 ✓" << endl;
    } else {
        cout << "结果一致性验证: 失败 ✗" << endl;
    }
    cout << endl;
}

int main() {
    cout << "=== LeetCode 416 分割等和子集测试 ===" << endl << endl;
    
    // 测试用例1：基本功能测试
    runTest("测试用例1 - 基本功能", {1, 5, 11, 5}, true);
    
    // 测试用例2：LeetCode官方示例
    runTest("测试用例2 - 官方示例", {1, 2, 3, 5}, false);
    
    // 测试用例3：总和为奇数
    runTest("测试用例3 - 总和奇数", {1, 2, 3, 4, 5}, false);
    
    // 测试用例4：单个元素
    runTest("测试用例4 - 单个元素", {1}, false);
    
    // 测试用例5：空数组
    runTest("测试用例5 - 空数组", {}, false);
    
    // 测试用例6：两个相同元素
    runTest("测试用例6 - 两个相同", {2, 2}, true);
    
    // 性能测试
    performanceTest();
    
    cout << "所有测试完成！" << endl;
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 基础动态规划：
 * - 时间：计算总和O(n) + 填充dp表O(n * target) → O(n * target)
 * - 空间：二维vector大小n×target → O(n * target)
 * 
 * 空间优化版本：
 * - 时间：O(n * target)
 * - 空间：一维vector大小target → O(target)
 * 
 * 位运算版本：
 * - 时间：O(n * target)
 * - 空间：位掩码数组大小target/64 → O(target)
 * 
 * C++特性说明：
 * 1. 使用STL算法简化代码（accumulate, max_element）
 * 2. 使用vector<bool>进行空间优化
 * 3. 使用chrono库进行精确性能测试
 * 4. RAII机制自动管理资源
 * 
 * 工程化建议：
 * 1. 对于生产环境使用空间优化版本
 * 2. 添加异常处理确保程序健壮性
 * 3. 编写单元测试覆盖各种边界情况
 * 4. 使用性能分析工具优化关键路径
 */