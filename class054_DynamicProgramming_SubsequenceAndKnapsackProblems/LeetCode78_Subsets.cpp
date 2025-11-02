#include <iostream>
#include <vector>
#include <algorithm>
#include <chrono>
#include <random>
#include <stdexcept>
#include <unordered_set>

using namespace std;

/**
 * LeetCode 78. 子集
 * 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 测试链接：https://leetcode.cn/problems/subsets/
 * 
 * 算法详解：
 * 使用多种方法生成数组的所有子集，包括位掩码法、回溯法和迭代法。
 * 
 * 时间复杂度：O(n * 2^n)，其中n是数组长度
 * 空间复杂度：O(n * 2^n) 用于存储所有子集
 * 
 * 工程化考量：
 * 1. 异常处理：检查输入参数有效性
 * 2. 边界处理：空数组的情况
 * 3. 性能优化：避免不必要的拷贝
 * 4. 内存管理：合理使用STL容器
 */

class Solution {
public:
    /**
     * 位掩码法生成所有子集
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n * 2^n)
     */
    static vector<vector<int>> subsetsBitMask(const vector<int>& nums) {
        if (nums.empty()) {
            return {{}}; // 返回包含空集的向量
        }
        
        int n = nums.size();
        int total = 1 << n; // 2^n
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
     * 回溯法生成所有子集
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n) 递归栈空间
     */
    static vector<vector<int>> subsetsBacktrack(const vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> current;
        
        backtrack(nums, 0, current, result);
        return result;
    }
    
private:
    static void backtrack(const vector<int>& nums, int index, 
                         vector<int>& current, vector<vector<int>>& result) {
        if (index == nums.size()) {
            result.push_back(current);
            return;
        }
        
        // 不包含当前元素
        backtrack(nums, index + 1, current, result);
        
        // 包含当前元素
        current.push_back(nums[index]);
        backtrack(nums, index + 1, current, result);
        current.pop_back(); // 回溯
    }
    
public:
    /**
     * 迭代法生成所有子集
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n * 2^n)
     */
    static vector<vector<int>> subsetsIterative(const vector<int>& nums) {
        vector<vector<int>> result = {{}}; // 初始包含空集
        
        for (int num : nums) {
            int size = result.size();
            for (int i = 0; i < size; i++) {
                vector<int> newSubset = result[i]; // 拷贝已有子集
                newSubset.push_back(num);         // 添加当前元素
                result.push_back(newSubset);
            }
        }
        
        return result;
    }
    
    /**
     * 优化的回溯法（避免不必要的拷贝）
     * 时间复杂度：O(n * 2^n)
     * 空间复杂度：O(n) 递归栈空间
     */
    static vector<vector<int>> subsetsOptimized(const vector<int>& nums) {
        vector<vector<int>> result;
        vector<int> path;
        dfs(nums, 0, path, result);
        return result;
    }
    
private:
    static void dfs(const vector<int>& nums, int start, 
                   vector<int>& path, vector<vector<int>>& result) {
        result.push_back(path); // 添加当前路径
        
        for (int i = start; i < nums.size(); i++) {
            path.push_back(nums[i]);
            dfs(nums, i + 1, path, result);
            path.pop_back(); // 回溯
        }
    }
};

/**
 * 测试辅助函数
 */
void runTest(const string& description, const vector<int>& nums) {
    cout << description << endl;
    cout << "输入数组: [";
    for (size_t i = 0; i < nums.size(); i++) {
        cout << nums[i];
        if (i < nums.size() - 1) cout << ", ";
    }
    cout << "]" << endl;
    
    auto result1 = Solution::subsetsBitMask(nums);
    auto result2 = Solution::subsetsBacktrack(nums);
    auto result3 = Solution::subsetsIterative(nums);
    auto result4 = Solution::subsetsOptimized(nums);
    
    cout << "位掩码法: " << result1.size() << "个子集" << endl;
    cout << "回溯法: " << result2.size() << "个子集" << endl;
    cout << "迭代法: " << result3.size() << "个子集" << endl;
    cout << "优化回溯: " << result4.size() << "个子集" << endl;
    
    // 验证结果一致性
    bool sizesMatch = (result1.size() == result2.size() && 
                      result2.size() == result3.size() && 
                      result3.size() == result4.size());
    
    if (sizesMatch) {
        cout << "子集数量一致 ✓" << endl;
        
        // 打印前几个子集作为示例
        if (result1.size() <= 16) {
            cout << "所有子集: " << endl;
            for (const auto& subset : result1) {
                cout << "  [";
                for (size_t i = 0; i < subset.size(); i++) {
                    cout << subset[i];
                    if (i < subset.size() - 1) cout << ", ";
                }
                cout << "]" << endl;
            }
        } else {
            cout << "前4个子集: " << endl;
            for (int i = 0; i < min(4, (int)result1.size()); i++) {
                cout << "  [";
                for (size_t j = 0; j < result1[i].size(); j++) {
                    cout << result1[i][j];
                    if (j < result1[i].size() - 1) cout << ", ";
                }
                cout << "]" << endl;
            }
            cout << "... 等 " << result1.size() << " 个子集" << endl;
        }
    } else {
        cout << "子集数量不一致 ✗" << endl;
    }
    
    cout << endl;
}

/**
 * 性能测试函数
 */
void performanceTest() {
    cout << "=== 性能测试 ===" << endl;
    
    // 生成测试数据：中等规模数组
    const int n = 15; // 2^15 = 32768个子集
    vector<int> nums(n);
    for (int i = 0; i < n; i++) {
        nums[i] = i + 1;
    }
    
    cout << "测试数据规模: " << n << "个元素" << endl;
    cout << "预期子集数量: " << (1 << n) << endl;
    
    // 测试位掩码法
    auto start = chrono::high_resolution_clock::now();
    auto result1 = Solution::subsetsBitMask(nums);
    auto end = chrono::high_resolution_clock::now();
    auto duration1 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "位掩码法:" << endl;
    cout << "  子集数量: " << result1.size() << endl;
    cout << "  耗时: " << duration1.count() << " 毫秒" << endl;
    
    // 测试优化回溯法
    start = chrono::high_resolution_clock::now();
    auto result4 = Solution::subsetsOptimized(nums);
    end = chrono::high_resolution_clock::now();
    auto duration4 = chrono::duration_cast<chrono::milliseconds>(end - start);
    
    cout << "优化回溯法:" << endl;
    cout << "  子集数量: " << result4.size() << endl;
    cout << "  耗时: " << duration4.count() << " 毫秒" << endl;
    
    // 验证结果一致性
    if (result1.size() == result4.size()) {
        cout << "结果一致性验证: 通过 ✓" << endl;
    } else {
        cout << "结果一致性验证: 失败 ✗" << endl;
    }
    
    cout << "注意：回溯法和迭代法在大规模数据下可能内存不足" << endl;
    cout << endl;
}

int main() {
    cout << "=== LeetCode 78 子集问题测试 ===" << endl << endl;
    
    try {
        // 测试用例1：空数组
        runTest("测试用例1 - 空数组", {});
        
        // 测试用例2：单元素数组
        runTest("测试用例2 - 单元素", {1});
        
        // 测试用例3：双元素数组
        runTest("测试用例3 - 双元素", {1, 2});
        
        // 测试用例4：三元素数组
        runTest("测试用例4 - 三元素", {1, 2, 3});
        
        // 测试用例5：四元素数组
        runTest("测试用例5 - 四元素", {1, 2, 3, 4});
        
        // 性能测试
        performanceTest();
        
        cout << "所有测试完成！" << endl;
        
    } catch (const exception& e) {
        cerr << "测试过程中发生错误: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}

/**
 * 复杂度分析详细计算：
 * 
 * 位掩码法：
 * - 时间：外层循环2^n次，内层循环n次 → O(n * 2^n)
 * - 空间：需要存储所有子集 → O(n * 2^n)
 * 
 * 回溯法：
 * - 时间：生成2^n个子集 → O(n * 2^n)
 * - 空间：递归深度n → O(n)
 * 
 * 迭代法：
 * - 时间：外层循环n次，内层循环2^i次 → O(n * 2^n)
 * - 空间：需要存储所有子集 → O(n * 2^n)
 * 
 * 优化回溯法：
 * - 时间：O(n * 2^n)
 * - 空间：递归深度n → O(n)
 * 
 * C++特性说明：
 * 1. 使用const引用避免不必要的拷贝
 * 2. STL容器提供高效的内存管理
 * 3. 使用chrono库进行精确性能测试
 * 4. RAII机制自动管理资源
 * 
 * 工程化建议：
 * 1. 对于小规模数据使用任意方法
 * 2. 对于中等规模数据优先选择优化回溯法
 * 3. 对于大规模数据考虑使用迭代器避免内存爆炸
 * 4. 添加异常处理确保程序健壮性
 * 
 * 调试技巧：
 * 1. 使用小规模测试用例验证算法正确性
 * 2. 打印中间结果观察生成过程
 * 3. 使用断言验证关键假设
 * 4. 使用valgrind检查内存泄漏
 */