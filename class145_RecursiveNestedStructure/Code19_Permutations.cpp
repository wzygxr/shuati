// LeetCode 46. Permutations
// 全排列
// 题目来源：https://leetcode.cn/problems/permutations/

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 问题描述：
 * 给定一个不含重复数字的数组 nums，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 解题思路：
 * 使用回溯算法，通过递归生成所有可能的排列组合
 * 1. 选择：从当前可用的数字中选择一个加入当前排列
 * 2. 约束：已经选择过的数字不能再次选择
 * 3. 目标：生成长度等于原数组长度的排列
 * 
 * 时间复杂度：O(N * N!)，其中N是数组的长度，N!是排列的总数，每个排列需要O(N)的时间复制
 * 空间复杂度：O(N)，递归栈的深度最多为N，另外需要O(N)的空间来标记已使用的数字
 */

class Solution {
public:
    /**
     * 使用used数组的回溯算法求解全排列
     * @param nums 输入的不含重复数字的数组
     * @return 所有可能的全排列
     */
    vector<vector<int>> permute(vector<int>& nums) {
        vector<vector<int>> result;
        if (nums.empty()) {
            return result;
        }
        
        vector<int> currentPermutation; // 当前正在构建的排列
        vector<bool> used(nums.size(), false); // 标记数组元素是否已被使用
        
        // 开始回溯
        backtrack(nums, currentPermutation, used, result);
        
        return result;
    }
    
    /**
     * 回溯函数
     * @param nums 输入数组
     * @param currentPermutation 当前正在构建的排列
     * @param used 标记数组元素是否已被使用
     * @param result 存储所有全排列的结果集
     */
    void backtrack(vector<int>& nums, vector<int>& currentPermutation, 
                   vector<bool>& used, vector<vector<int>>& result) {
        // 终止条件：当前排列的长度等于原数组长度，说明找到了一个完整的排列
        if (currentPermutation.size() == nums.size()) {
            // 将当前排列的副本加入结果集
            result.push_back(currentPermutation);
            return;
        }
        
        // 尝试选择每个未使用的数字
        for (int i = 0; i < nums.size(); ++i) {
            // 如果当前数字已经被使用，跳过
            if (used[i]) {
                continue;
            }
            
            // 选择当前数字
            currentPermutation.push_back(nums[i]);
            used[i] = true;
            
            // 递归到下一层，构建剩余的排列
            backtrack(nums, currentPermutation, used, result);
            
            // 回溯：撤销选择
            used[i] = false;
            currentPermutation.pop_back();
        }
    }
    
    /**
     * 不使用used数组的回溯方法（通过交换元素实现）
     * 这种方法更节省空间，但会修改原数组
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    vector<vector<int>> permuteBySwapping(vector<int>& nums) {
        vector<vector<int>> result;
        if (nums.empty()) {
            return result;
        }
        
        // 开始回溯（通过交换元素实现）
        backtrackBySwapping(nums, 0, result);
        
        return result;
    }
    
    /**
     * 通过交换元素实现回溯的辅助函数
     * @param nums 当前的数字列表
     * @param start 开始交换的位置
     * @param result 存储所有全排列的结果集
     */
    void backtrackBySwapping(vector<int>& nums, int start, vector<vector<int>>& result) {
        // 终止条件：当start等于nums.size()时，说明已经确定了一个排列
        if (start == nums.size()) {
            result.push_back(nums);
            return;
        }
        
        // 从start位置开始，尝试将每个位置的元素与start位置交换
        for (int i = start; i < nums.size(); ++i) {
            // 交换元素
            swap(nums[start], nums[i]);
            
            // 递归到下一个位置
            backtrackBySwapping(nums, start + 1, result);
            
            // 回溯：撤销交换
            swap(nums[start], nums[i]);
        }
    }
};

// 释放动态分配的内存（用于测试和避免内存泄漏）
void releaseMemory(vector<vector<int>>& result) {
    // 在C++中，vector会自动管理内存，但如果有复杂的嵌套结构，可能需要显式清理
    // 这里简单地clear所有内部vector
    for (auto& vec : result) {
        vec.clear();
    }
    result.clear();
}

// 打印排列结果
void printPermutations(const vector<vector<int>>& result) {
    cout << "[" << endl;
    for (size_t i = 0; i < result.size(); ++i) {
        cout << "  [";
        for (size_t j = 0; j < result[i].size(); ++j) {
            cout << result[i][j];
            if (j < result[i].size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
        if (i < result.size() - 1) {
            cout << ",";
        }
        cout << endl;
    }
    cout << "]" << endl;
}

// 主函数，用于测试
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    cout << "测试用例1 - 使用used数组:" << endl;
    vector<vector<int>> result1 = solution.permute(nums1);
    printPermutations(result1);
    
    cout << "\n测试用例1 - 通过交换元素:" << endl;
    vector<int> nums1_copy = {1, 2, 3}; // 创建副本以避免影响原数组
    vector<vector<int>> result1BySwapping = solution.permuteBySwapping(nums1_copy);
    printPermutations(result1BySwapping);
    
    // 测试用例2
    vector<int> nums2 = {0, 1};
    cout << "\n测试用例2 - 使用used数组:" << endl;
    vector<vector<int>> result2 = solution.permute(nums2);
    printPermutations(result2);
    
    // 测试用例3
    vector<int> nums3 = {1};
    cout << "\n测试用例3 - 使用used数组:" << endl;
    vector<vector<int>> result3 = solution.permute(nums3);
    printPermutations(result3);
    
    // 清理内存（虽然vector会在作用域结束时自动释放，但显式清理是好习惯）
    releaseMemory(result1);
    releaseMemory(result1BySwapping);
    releaseMemory(result2);
    releaseMemory(result3);
    
    return 0;
}

/**
 * 性能分析：
 * 
 * 1. 使用used数组的方法：
 *    - 时间复杂度：O(N * N!)，其中N是数组长度
 *    - 空间复杂度：O(N)，用于存储used数组、递归栈和当前排列
 *    - 优点：逻辑清晰，不修改原数组
 *    - 缺点：需要额外的used数组空间
 * 
 * 2. 通过交换元素的方法：
 *    - 时间复杂度：O(N * N!)，其中N是数组长度
 *    - 空间复杂度：O(N)，主要是递归栈的空间
 *    - 优点：节省了used数组的空间，空间效率更高
 *    - 缺点：修改了原数组（在测试代码中我们创建了副本，所以原数组没被修改）
 * 
 * C++语言特性利用：
 * 1. 使用vector容器管理动态大小的数组，避免了手动内存管理的复杂性
 * 2. 利用引用传递参数，避免了不必要的数据拷贝
 * 3. 使用标准库算法（如swap）提高代码可读性和效率
 * 4. 使用const引用传递不需要修改的参数，提高效率
 * 
 * 算法优化思路：
 * 1. 剪枝优化：虽然对于标准的全排列问题没有太多剪枝空间，但在实际应用中可以根据具体条件进行剪枝
 * 2. 记忆化搜索：在某些特定变体问题中，可以使用记忆化技术避免重复计算
 * 3. 迭代实现：可以将递归实现转换为迭代实现，避免深层递归可能导致的栈溢出
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中需要注意对象的生命周期和内存管理，避免内存泄漏
 * 2. 异常处理：在实际应用中应该添加异常处理机制
 * 3. 线程安全：如果在多线程环境中使用，需要确保线程安全
 * 4. 性能监控：对于大规模数据，可以添加性能监控代码
 * 5. 扩展性：可以扩展此算法以处理包含重复元素的数组
 * 6. 模板化：可以使用C++模板编写更通用的算法
 * 
 * 回溯算法框架总结：
 * 回溯算法是一种通过探索所有可能的候选解来找出所有解的算法。如果候选解被确认不是一个解（或者至少不是最后一个解），回溯算法会通过在上一步进行一些变化来舍弃该解，即回溯并且尝试另一种可能。
 * 
 * 回溯算法通常用于解决以下类型的问题：
 * 1. 排列问题：如本题的全排列
 * 2. 组合问题：如从n个元素中选择k个元素的所有组合
 * 3. 子集问题：找出给定集合的所有子集
 * 4. 棋盘问题：如N皇后、数独等
 * 5. 路径问题：如找出从起点到终点的所有路径
 * 
 * 回溯算法的基本框架可以表示为：
 * 
 * void backtrack(路径, 选择列表):
 *     if 满足结束条件:
 *         将路径加入结果集
 *         return
 *     
 *     for 选择 in 选择列表:
 *         做选择
 *         backtrack(路径, 选择列表)
 *         撤销选择
 */