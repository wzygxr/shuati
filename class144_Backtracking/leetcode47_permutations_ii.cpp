/**
 * LeetCode 47. 全排列 II
 * 
 * 题目描述：
 * 给定一个可包含重复数字的序列 nums ，按任意顺序返回所有不重复的全排列。
 * 
 * 示例：
 * 输入：nums = [1,1,2]
 * 输出：
 * [[1,1,2],
 *  [1,2,1],
 *  [2,1,1]]
 * 
 * 输入：nums = [1,2,3]
 * 输出：[[1,2,3],[1,3,2],[2,1,3],[2,3,1],[3,1,2],[3,2,1]]
 * 
 * 提示：
 * 1 <= nums.length <= 8
 * -10 <= nums[i] <= 10
 * 
 * 链接：https://leetcode.cn/problems/permutations-ii/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的排列
 * 2. 先对数组进行排序，使相同元素相邻
 * 3. 对于每个位置，尝试放置每个未使用的数字
 * 4. 通过递归和回溯生成所有满足条件的排列
 * 5. 使用布尔数组标记数字是否已被使用
 * 6. 通过剪枝避免生成重复的排列
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：使用布尔数组避免重复使用数字
 * 2. 最优性剪枝：当已选择的数字个数等于数组长度时终止
 * 3. 约束传播：一旦某个数字被使用，立即标记为已使用
 * 4. 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择
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
#include <map>
using namespace std;

class LeetCode47_PermutationsII {
public:
    /**
     * 生成数组的所有不重复全排列
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的全排列
     */
    vector<vector<int>> permuteUnique(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        // 排序使相同元素相邻，便于剪枝
        sort(nums.begin(), nums.end());
        
        vector<int> path;
        vector<bool> used(nums.size(), false);
        backtrack(nums, path, used, result);
        return result;
    }
    
private:
    /**
     * 回溯函数生成不重复排列
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
            
            // 重复剪枝：对于相同元素，只允许第一个未使用的元素被选择
            // 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过当前元素
            if (i > 0 && nums[i] == nums[i - 1] && !used[i - 1]) {
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
     * 解法二：使用计数法处理重复元素
     * 统计每个元素的出现次数，然后基于计数生成排列
     * 
     * @param nums 输入数组（可能包含重复元素）
     * @return 所有不重复的全排列
     */
    vector<vector<int>> permuteUnique2(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        // 统计每个元素的出现次数
        map<int, int> counts;
        for (int num : nums) {
            counts[num]++;
        }
        
        vector<int> path;
        backtrack2(counts, nums.size(), path, result);
        return result;
    }
    
private:
    /**
     * 基于计数的回溯函数
     * 
     * @param counts 每个元素的出现次数
     * @param remaining 剩余需要选择的元素个数
     * @param path 当前路径
     * @param result 结果列表
     */
    void backtrack2(map<int, int>& counts, int remaining, vector<int>& path, vector<vector<int>>& result) {
        // 终止条件：已选择所有数字
        if (remaining == 0) {
            result.push_back(path);
            return;
        }
        
        // 尝试每个可用的数字
        for (auto& pair : counts) {
            int num = pair.first;
            int count = pair.second;
            
            // 如果当前数字还有剩余
            if (count > 0) {
                // 选择当前数字
                path.push_back(num);
                counts[num]--;
                
                // 递归处理剩余位置
                backtrack2(counts, remaining - 1, path, result);
                
                // 回溯：撤销选择
                path.pop_back();
                counts[num]++;
            }
        }
    }
};

// 测试方法
int main() {
    LeetCode47_PermutationsII solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 1, 2};
    vector<vector<int>> result1 = solution.permuteUnique(nums1);
    printf("输入: nums = [1,1,2]\n");
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
    vector<int> nums2 = {1, 2, 3};
    vector<vector<int>> result2 = solution.permuteUnique(nums2);
    printf("\n输入: nums = [1,2,3]\n");
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
    vector<int> nums3 = {2, 2, 1, 1};
    vector<vector<int>> result3 = solution.permuteUnique(nums3);
    printf("\n输入: nums = [2,2,1,1]\n");
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
    vector<vector<int>> result4 = solution.permuteUnique2(nums1);
    printf("输入: nums = [1,1,2]\n");
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