/**
 * LeetCode 46. 全排列
 * 
 * 题目描述：
 * 给定一个不含重复数字的数组 nums ，返回其所有可能的全排列。你可以按任意顺序返回答案。
 * 
 * 示例：
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 输入：nums = [0,1]
 * 输出：[[0,1],[1,0]]
 * 
 * 输入：nums = [1]
 * 输出：[[1]]
 * 
 * 提示：
 * 1 <= nums.length <= 6
 * -10 <= nums[i] <= 10
 * nums 中的所有整数互不相同
 * 
 * 链接：https://leetcode.cn/problems/permutations/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的排列
 * 2. 对于每个位置，尝试放置每个未使用的数字
 * 3. 通过递归和回溯生成所有满足条件的排列
 * 4. 使用布尔数组标记数字是否已被使用
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：使用布尔数组避免重复使用数字
 * 2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
 * 3. 约束传播：一旦某个数字被使用，立即标记为已使用
 * 
 * 时间复杂度：O(n! * n)，其中n是数组长度，共有n!种排列，每种排列需要O(n)时间复制
 * 空间复杂度：O(n)，递归栈深度和存储路径的空间
 * 
 * 工程化考量：
 * 1. 边界处理：处理空数组和单元素数组的特殊情况
 * 2. 参数验证：验证输入参数的有效性
 * 3. 性能优化：通过剪枝减少不必要的计算
 * 4. 内存管理：合理使用数据结构减少内存占用
 * 5. 可读性：添加详细注释和变量命名
 * 6. 异常处理：处理可能的异常情况
 * 7. 模块化设计：将核心逻辑封装成独立方法
 * 8. 可维护性：添加详细注释和文档说明
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class LeetCode46_Permutations {
public:
    /**
     * 生成数组的所有全排列
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    vector<vector<int>> permute(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        vector<int> path;
        vector<bool> used(nums.size(), false);
        backtrack(nums, path, used, result);
        return result;
    }
    
private:
    /**
     * 回溯函数生成排列
     * 
     * @param nums 输入数组
     * @param path 当前路径
     * @param used 标记数字是否已被使用的数组
     * @param result 结果列表
     */
    void backtrack(vector<int>& nums, vector<int>& path, vector<bool>& used, vector<vector<int>>& result) {
        // 终止条件：已选择所有数字
        if (path.size() == nums.size()) {
            result.push_back(path);
            return;
        }
        
        // 尝试每个未使用的数字
        for (int i = 0; i < nums.size(); i++) {
            // 可行性剪枝：如果数字已被使用，跳过
            if (used[i]) {
                continue;
            }
            
            // 选择当前数字
            path.push_back(nums[i]);
            used[i] = true;
            
            // 递归处理下一个位置
            backtrack(nums, path, used, result);
            
            // 回溯：撤销选择
            path.pop_back();
            used[i] = false;
        }
    }
    
public:
    /**
     * 解法二：交换元素法生成排列
     * 通过交换数组元素生成所有排列，避免使用额外的标记数组
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    vector<vector<int>> permute2(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        backtrack2(nums, 0, result);
        return result;
    }
    
private:
    /**
     * 通过交换元素生成排列的回溯函数
     * 
     * @param nums 数字列表
     * @param start 当前处理的位置
     * @param result 结果列表
     */
    void backtrack2(vector<int>& nums, int start, vector<vector<int>>& result) {
        // 终止条件：已处理完所有位置
        if (start == nums.size()) {
            result.push_back(nums);
            return;
        }
        
        // 尝试将每个后续元素交换到当前位置
        for (int i = start; i < nums.size(); i++) {
            // 交换元素
            swap(nums[start], nums[i]);
            
            // 递归处理下一个位置
            backtrack2(nums, start + 1, result);
            
            // 回溯：恢复交换前的状态
            swap(nums[start], nums[i]);
        }
    }
    
public:
    /**
     * 解法三：使用STL的next_permutation函数
     * 先对数组排序，然后使用STL函数生成所有排列
     * 
     * @param nums 输入数组
     * @return 所有可能的全排列
     */
    vector<vector<int>> permute3(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        // 排序以确保从最小排列开始
        sort(nums.begin(), nums.end());
        
        // 添加第一个排列
        result.push_back(nums);
        
        // 生成所有后续排列
        while (next_permutation(nums.begin(), nums.end())) {
            result.push_back(nums);
        }
        
        return result;
    }
};

// 测试方法
int main() {
    LeetCode46_Permutations solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    vector<vector<int>> result1 = solution.permute(nums1);
    printf("输入: nums = [1,2,3]\n");
    printf("输出: [");
    for (int i = 0; i < result1.size(); i++) {
        printf("[");
        for (int j = 0; j < result1[i].size(); j++) {
            printf("%d", result1[i][j]);
            if (j < result1[i].size() - 1) printf(",");
        }
        printf("]");
        if (i < result1.size() - 1) printf(",");
    }
    printf("]\n");
    
    // 测试用例2
    vector<int> nums2 = {0, 1};
    vector<vector<int>> result2 = solution.permute(nums2);
    printf("\n输入: nums = [0,1]\n");
    printf("输出: [");
    for (int i = 0; i < result2.size(); i++) {
        printf("[");
        for (int j = 0; j < result2[i].size(); j++) {
            printf("%d", result2[i][j]);
            if (j < result2[i].size() - 1) printf(",");
        }
        printf("]");
        if (i < result2.size() - 1) printf(",");
    }
    printf("]\n");
    
    // 测试用例3
    vector<int> nums3 = {1};
    vector<vector<int>> result3 = solution.permute(nums3);
    printf("\n输入: nums = [1]\n");
    printf("输出: [");
    for (int i = 0; i < result3.size(); i++) {
        printf("[");
        for (int j = 0; j < result3[i].size(); j++) {
            printf("%d", result3[i][j]);
            if (j < result3[i].size() - 1) printf(",");
        }
        printf("]");
        if (i < result3.size() - 1) printf(",");
    }
    printf("]\n");
    
    // 测试解法二
    printf("\n=== 解法二测试 ===\n");
    vector<vector<int>> result4 = solution.permute2(nums1);
    printf("输入: nums = [1,2,3]\n");
    printf("输出: [");
    for (int i = 0; i < result4.size(); i++) {
        printf("[");
        for (int j = 0; j < result4[i].size(); j++) {
            printf("%d", result4[i][j]);
            if (j < result4[i].size() - 1) printf(",");
        }
        printf("]");
        if (i < result4.size() - 1) printf(",");
    }
    printf("]\n");
    
    return 0;
}