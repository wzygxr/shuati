#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <random>
#include <chrono>

using namespace std;

/**
 * LeetCode 334. 递增的三元子序列
 * 给你一个整数数组 nums ，判断这个数组中是否存在长度为 3 的递增子序列。
 * 如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k ，使得 nums[i] < nums[j] < nums[k] ，返回 true ；否则，返回 false 。
 * 测试链接：https://leetcode.cn/problems/increasing-triplet-subsequence/
 * 
 * 算法详解：
 * 使用贪心思想判断是否存在递增三元组，时间复杂度O(n)，空间复杂度O(1)。
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入数组有效性
 * 2. 边界处理：数组长度小于3的情况
 * 3. 性能优化：提前终止遍历
 * 4. 代码质量：清晰的变量命名和注释
 */

class Solution {
public:
    /**
     * 贪心算法解法（最优解）
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * 算法思想：
     * 维护两个变量first和second，分别表示当前找到的最小值和次小值。
     * 遍历数组时，如果当前数比second大，说明存在递增三元组。
     */
    static bool increasingTriplet(const vector<int>& nums) {
        // 异常处理：检查输入数组是否有效
        if (nums.size() < 3) {
            return false;
        }
        
        int first = INT_MAX;    // 当前最小值
        int second = INT_MAX;   // 当前次小值（比first大）
        
        for (int num : nums) {
            if (num <= first) {
                // 更新最小值
                first = num;
            } else if (num <= second) {
                // 更新次小值
                second = num;
            } else {
                // 找到比second大的数，存在递增三元组
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 动态规划解法（通用但效率较低）
     * 时间复杂度：O(n²)
     * 空间复杂度：O(n)
     * 
     * 可以扩展到判断任意长度的递增子序列
     */
    static bool increasingTripletDP(const vector<int>& nums) {
        if (nums.size() < 3) {
            return false;
        }
        
        int n = nums.size();
        vector<int> dp(n, 1);  // dp[i]表示以nums[i]结尾的最长递增子序列长度
        
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (nums[j] < nums[i]) {
                    dp[i] = max(dp[i], dp[j] + 1);
                    if (dp[i] >= 3) {
                        return true;  // 提前终止
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * 二分查找解法（LIS思想）
     * 时间复杂度：O(n log k)，其中k≤3 → O(n)
     * 空间复杂度：O(k) → O(1)
     */
    static bool increasingTripletBinarySearch(const vector<int>& nums) {
        if (nums.size() < 3) {
            return false;
        }
        
        vector<int> tails(3, INT_MAX);  // tails[i]表示长度为i+1的递增子序列的最小末尾
        int len = 0;  // 当前最长递增子序列长度
        
        for (int num : nums) {
            // 二分查找找到num在tails中的位置
            int left = 0, right = len;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (tails[mid] < num) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            
            tails[left] = num;
            if (left == len) {
                len++;
                if (len >= 3) {
                    return true;
                }
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
    
    bool result1 = Solution::increasingTriplet(nums);
    bool result2 = Solution::increasingTripletDP(nums);
    bool result3 = Solution::increasingTripletBinarySearch(nums);
    
    cout << "贪心算法: " << (result1 ? "true" : "false") 
         << " " << (result1 == expected ? "✓" : "✗") << endl;
    cout << "动态规划: " << (result2 ? "true" : "false") 
         << " " << (result2 == expected ? "✓" : "✗") << endl;
    cout << "二分查找: " << (result3 ? "true" : "false") 
         << " " << (result3 == expected ? "✓" : "✗") << endl;
    cout << "期望结果: " << (expected ? "true" : "false") << endl;
    
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
    
    // 生成大规模测试数据
    const int n = 10000;
    vector<int> nums(n);
    random_device rd;
    mt19937 gen(rd());
    uniform_int_distribution<> dis(1, 1000000);
    
    for (int i = 0; i < n; i++) {
        nums[i] = dis(gen);
    }
    
    // 测试贪心算法
    auto start = chrono::high_resolution_clock::now();
    bool result1 = Solution::increasingTriplet(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "贪心算法:" << endl;
    cout << "  结果: " << (result1 ? "true" : "false") << endl;
    cout << "  耗时: " << duration1.count() << " 微秒" << endl;
    
    // 测试二分查找算法
    start = chrono::high_resolution_clock::now();
    bool result3 = Solution::increasingTripletBinarySearch(nums);
    end = chrono::high_resolution_clock::now();
    auto duration3 = chrono::duration_cast<chrono::microseconds>(end - start);
    
    cout << "二分查找:" << endl;
    cout << "  结果: " << (result3 ? "true" : "false") << endl;
    cout << "  耗时: " << duration3.count() << " 微秒" << endl;
    
    // 验证结果一致性
    if (result1 == result3) {
        cout << "结果一致性验证: 通过 ✓" << endl;
    } else {
        cout << "结果一致性验证: 失败 ✗" << endl;
    }
    cout << endl;
}

int main() {
    cout << "=== LeetCode 334 递增的三元子序列测试 ===" << endl << endl;
    
    // 测试用例1：存在递增三元组
    runTest("测试用例1 - 严格递增", {1, 2, 3, 4, 5}, true);
    
    // 测试用例2：不存在递增三元组
    runTest("测试用例2 - 严格递减", {5, 4, 3, 2, 1}, false);
    
    // 测试用例3：存在递增三元组（非连续）
    runTest("测试用例3 - 非连续递增", {2, 1, 5, 0, 4, 6}, true);
    
    // 测试用例4：边界情况
    runTest("测试用例4 - 长度小于3", {1, 2}, false);
    
    // 测试用例5：包含重复元素
    runTest("测试用例5 - 全部重复", {1, 1, 1, 1, 1}, false);
    
    // 测试用例6：复杂情况
    runTest("测试用例6 - 复杂情况", {5, 1, 6, 2, 7, 3, 8}, true);
    
    // 测试用例7：刚好存在三元组
    runTest("测试用例7 - 刚好存在", {1, 2, 0, 3}, true);
    
    // 性能测试
    performanceTest();
    
    cout << "所有测试完成！" << endl;
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 贪心算法（最优解）：
 * - 时间：单次遍历数组O(n)，每次操作O(1) → O(n)
 * - 空间：使用常数个变量 → O(1)
 * 
 * 动态规划：
 * - 时间：双重循环O(n²)，最坏情况需要比较所有元素对
 * - 空间：dp数组大小n → O(n)
 * 
 * 二分查找：
 * - 时间：遍历数组O(n)，每次二分查找O(log k)其中k≤3 → O(n)
 * - 空间：tails数组大小3 → O(1)
 * 
 * C++特性说明：
 * 1. 使用vector容器动态管理数组
 * 2. 使用const引用避免不必要的拷贝
 * 3. 使用STL算法简化代码
 * 4. 使用chrono库进行精确性能测试
 * 
 * 工程化建议：
 * 1. 对于大规模数据优先选择贪心算法
 * 2. 如果需要找到具体三元组可以使用动态规划
 * 3. 二分查找方法在理论上有优势但实际中贪心更简洁
 * 4. 添加单元测试覆盖各种边界情况
 */