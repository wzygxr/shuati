// LeetCode 78. Subsets
// 子集
// 题目来源：https://leetcode.cn/problems/subsets/

#include <iostream>
#include <vector>
using namespace std;

/**
 * 问题描述：
 * 给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 解题思路：
 * 使用回溯算法，通过递归生成所有可能的子集
 * 1. 选择：对于每个元素，可以选择包含或不包含在子集中
 * 2. 约束：元素互不相同，不能重复选择同一个元素
 * 3. 目标：生成所有可能的子集（包括空集和原数组本身）
 * 
 * 时间复杂度：O(N * 2^N)，其中N是数组的长度，2^N是子集的总数，每个子集需要O(N)的时间复制
 * 空间复杂度：O(N)，递归栈的深度最多为N
 */

class Solution {
public:
    /**
     * 使用回溯算法生成所有子集
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsets(vector<int>& nums) {
        vector<vector<int>> result;
        if (nums.empty()) {
            // 至少返回空集
            result.push_back({});
            return result;
        }
        
        vector<int> current_subset; // 存储当前子集
        
        // 开始回溯
        backtrack(nums, 0, current_subset, result);
        
        return result;
    }
    
    /**
     * 回溯函数
     * @param nums 输入数组
     * @param start 当前考虑元素的起始索引
     * @param current_subset 当前正在构建的子集
     * @param result 存储所有子集的结果集
     */
    void backtrack(vector<int>& nums, int start, vector<int>& current_subset, 
                  vector<vector<int>>& result) {
        // 每次进入函数，都将当前子集的副本加入结果集
        result.push_back(current_subset);
        
        // 遍历从start开始的所有元素，决定是否将其加入子集
        for (int i = start; i < nums.size(); ++i) {
            // 选择当前元素，将其加入子集
            current_subset.push_back(nums[i]);
            
            // 递归到下一层，考虑下一个位置的元素
            backtrack(nums, i + 1, current_subset, result);
            
            // 回溯：撤销选择，移除刚刚添加的元素
            current_subset.pop_back();
        }
    }
    
    /**
     * 使用位运算生成所有子集
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsetsByBitmask(vector<int>& nums) {
        vector<vector<int>> result;
        if (nums.empty()) {
            result.push_back({});
            return result;
        }
        
        int n = nums.size();
        // 总共有2^n个子集
        int total_subsets = 1 << n; // 相当于2^n
        
        // 遍历从0到2^n-1的所有数字，每个数字代表一个子集的位掩码
        for (int mask = 0; mask < total_subsets; ++mask) {
            vector<int> subset;
            
            // 检查每一位是否为1，如果为1则将对应的元素加入子集
            for (int i = 0; i < n; ++i) {
                // 检查mask的第i位是否为1
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * 使用增量法迭代构建子集
     * @param nums 输入的整数数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsetsIterative(vector<int>& nums) {
        vector<vector<int>> result = {{}}; // 初始时加入空集
        
        if (nums.empty()) {
            return result;
        }
        
        // 对于每个元素，将其添加到现有所有子集中，生成新的子集
        for (int num : nums) {
            int size = result.size();
            // 遍历当前所有子集
            for (int i = 0; i < size; ++i) {
                // 创建新子集，基于现有子集
                vector<int> new_subset = result[i];
                // 添加当前元素
                new_subset.push_back(num);
                // 将新子集加入结果集
                result.push_back(new_subset);
            }
        }
        
        return result;
    }
};

// 释放动态分配的内存（用于测试和避免内存泄漏）
void releaseMemory(vector<vector<int>>& result) {
    for (auto& subset : result) {
        subset.clear();
    }
    result.clear();
}

// 打印所有子集
void printSubsets(const vector<vector<int>>& subsets) {
    cout << "[";
    for (size_t i = 0; i < subsets.size(); ++i) {
        cout << "[";
        for (size_t j = 0; j < subsets[i].size(); ++j) {
            cout << subsets[i][j];
            if (j < subsets[i].size() - 1) {
                cout << ", ";
            }
        }
        cout << "]";
        if (i < subsets.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 主函数，用于测试
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    cout << "测试用例1 - 回溯算法:" << endl;
    vector<vector<int>> result1 = solution.subsets(nums1);
    printSubsets(result1);
    
    cout << "\n测试用例1 - 位运算:" << endl;
    vector<vector<int>> result1ByBitmask = solution.subsetsByBitmask(nums1);
    printSubsets(result1ByBitmask);
    
    cout << "\n测试用例1 - 迭代增量法:" << endl;
    vector<vector<int>> result1Iterative = solution.subsetsIterative(nums1);
    printSubsets(result1Iterative);
    
    // 测试用例2
    vector<int> nums2 = {0};
    cout << "\n测试用例2 - 回溯算法:" << endl;
    vector<vector<int>> result2 = solution.subsets(nums2);
    printSubsets(result2);
    
    // 清理内存
    releaseMemory(result1);
    releaseMemory(result1ByBitmask);
    releaseMemory(result1Iterative);
    releaseMemory(result2);
    
    return 0;
}

/**
 * 性能分析：
 * 
 * 1. 回溯算法：
 *    - 时间复杂度：O(N * 2^N)，其中N是数组长度，2^N是子集的总数，每个子集需要O(N)的时间复制
 *    - 空间复杂度：O(N)，递归栈的深度最多为N，以及存储当前子集的空间
 *    - 优点：逻辑清晰，容易理解和实现
 *    - 缺点：递归可能导致栈溢出（对于非常大的数组）
 * 
 * 2. 位运算解法：
 *    - 时间复杂度：O(N * 2^N)，需要遍历2^N个掩码，每个掩码需要O(N)的时间生成子集
 *    - 空间复杂度：O(N)，主要是存储结果的空间（不考虑输出）
 *    - 优点：代码简洁，对于小规模问题效率较高
 *    - 缺点：当N较大时（如超过30），2^N会超出整型范围；对于非常大的N不适用
 * 
 * 3. 迭代增量法：
 *    - 时间复杂度：O(N * 2^N)，需要处理2^N个子集，每个子集可能需要O(N)的复制操作
 *    - 空间复杂度：O(N * 2^N)，存储所有子集
 *    - 优点：避免了递归可能导致的栈溢出问题
 *    - 缺点：空间复杂度较高
 * 
 * C++语言特性利用：
 * 1. 使用vector容器管理动态大小的数组，避免了手动内存管理的复杂性
 * 2. 利用引用传递参数，避免了不必要的数据拷贝
 * 3. 使用C++11的初始化列表简化代码
 * 4. 使用范围for循环（C++11特性）简化迭代操作
 * 
 * 算法优化思路：
 * 1. 剪枝优化：在特定应用场景中，可以根据条件进行剪枝，提前停止某些分支的搜索
 * 2. 内存优化：对于非常大的数组，可以考虑使用迭代解法避免递归栈溢出
 * 3. 并行计算：对于大规模数据，可以考虑使用C++的并行库进行并行计算
 * 
 * 工程化考量：
 * 1. 内存管理：在C++中需要注意对象的生命周期和内存管理，避免内存泄漏
 * 2. 异常处理：在实际应用中应该添加异常处理机制
 * 3. 线程安全：如果在多线程环境中使用，需要确保线程安全
 * 4. 性能监控：对于大规模数据，可以添加性能监控代码
 * 5. 扩展性：可以扩展此算法以处理包含重复元素的数组（如LeetCode 90题）
 * 6. 模板化：可以使用C++模板编写更通用的算法
 * 
 * 子集生成算法的理论基础：
 * 一个包含n个不同元素的集合，其子集数量为2^n，这是因为对于每个元素，我们有两种选择：包含或不包含。
 * 这种选择的组合自然形成了所有可能的子集。
 * 
 * 实际应用中的注意事项：
 * 1. 当n很大时（如n > 20），子集的数量会变得非常庞大（超过1百万），可能导致内存溢出
 * 2. 在实际应用中，通常只需要关注满足特定条件的子集，这时可以添加剪枝条件
 * 3. 对于包含重复元素的数组，需要额外的去重逻辑
 */