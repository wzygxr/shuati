/**
 * LeetCode 78. 子集
 * 
 * 题目描述：
 * 给你一个整数数组 nums ，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
 * 解集不能包含重复的子集。你可以按任意顺序返回解集。
 * 
 * 示例：
 * 输入：nums = [1,2,3]
 * 输出：[[],[1],[2],[1,2],[3],[1,3],[2,3],[1,2,3]]
 * 
 * 输入：nums = [0]
 * 输出：[[],[0]]
 * 
 * 提示：
 * 1 <= nums.length <= 10
 * -10 <= nums[i] <= 10
 * nums 中的所有元素互不相同
 * 
 * 链接：https://leetcode.cn/problems/subsets/
 * 
 * 算法思路：
 * 1. 使用回溯算法生成所有可能的子集
 * 2. 对于每个元素，有两种选择：选择或不选择
 * 3. 通过递归和回溯生成所有满足条件的子集
 * 4. 每一步递归都将当前路径加入结果集
 * 
 * 剪枝策略：
 * 1. 可行性剪枝：通过起始索引避免重复选择元素
 * 2. 最优性剪枝：当已选择的元素个数等于数组长度时终止
 * 3. 约束传播：一旦某个元素被选择，后续只能选择后面的元素
 * 
 * 时间复杂度：O(n * 2^n)，其中n是数组长度，共有2^n个子集，每个子集需要O(n)时间复制
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
using namespace std;

class LeetCode78_Subsets {
public:
    /**
     * 生成数组的所有子集
     * 
     * @param nums 输入数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsets(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        vector<int> path;
        backtrack(nums, 0, path, result);
        return result;
    }
    
private:
    /**
     * 回溯函数生成子集
     * 
     * @param nums 输入数组
     * @param start 当前起始索引
     * @param path 当前路径
     * @param result 结果列表
     */
    void backtrack(vector<int>& nums, int start, vector<int>& path, vector<vector<int>>& result) {
        // 每一步都添加到结果中（空集也在其中）
        result.push_back(path);
        
        // 从start开始遍历，避免重复
        for (int i = start; i < nums.size(); i++) {
            // 选择当前元素
            path.push_back(nums[i]);
            
            // 递归处理下一个元素
            backtrack(nums, i + 1, path, result);
            
            // 回溯：撤销选择
            path.pop_back();
        }
    }
    
public:
    /**
     * 解法二：使用位运算枚举所有可能的子集
     * 每个元素有两种状态：选择(1)或不选择(0)，共2^n种组合
     * 
     * @param nums 输入数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsets2(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        int n = nums.size();
        
        // 枚举所有可能的子集（0到2^n-1）
        for (int mask = 0; mask < (1 << n); mask++) {
            vector<int> subset;
            
            // 根据位掩码构造子集
            for (int i = 0; i < n; i++) {
                // 检查第i位是否为1
                if (mask & (1 << i)) {
                    subset.push_back(nums[i]);
                }
            }
            
            result.push_back(subset);
        }
        
        return result;
    }
    
    /**
     * 解法三：使用迭代法生成所有子集
     * 逐个添加元素，每次添加新元素时，将该元素添加到已有的所有子集中
     * 
     * @param nums 输入数组
     * @return 所有可能的子集
     */
    vector<vector<int>> subsets3(vector<int>& nums) {
        vector<vector<int>> result;
        
        // 边界条件检查
        if (nums.empty()) {
            return result;
        }
        
        // 初始化空集
        result.push_back(vector<int>());
        
        // 逐个添加元素
        for (int num : nums) {
            int size = result.size();
            
            // 将当前元素添加到已有的所有子集中
            for (int i = 0; i < size; i++) {
                vector<int> newSubset = result[i];
                newSubset.push_back(num);
                result.push_back(newSubset);
            }
        }
        
        return result;
    }
};

// 测试方法
int main() {
    LeetCode78_Subsets solution;
    
    // 测试用例1
    vector<int> nums1 = {1, 2, 3};
    vector<vector<int>> result1 = solution.subsets(nums1);
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
    vector<int> nums2 = {0};
    vector<vector<int>> result2 = solution.subsets(nums2);
    printf("\n输入: nums = [0]\n");
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
    
    // 测试解法二
    printf("\n=== 解法二测试 ===\n");
    vector<vector<int>> result3 = solution.subsets2(nums1);
    printf("输入: nums = [1,2,3]\n");
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
    
    // 测试解法三
    printf("\n=== 解法三测试 ===\n");
    vector<vector<int>> result4 = solution.subsets3(nums1);
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