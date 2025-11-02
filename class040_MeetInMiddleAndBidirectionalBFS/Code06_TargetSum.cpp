// 目标和
// 给你一个非负整数数组 nums 和一个整数 target 。
// 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，可以构造一个表达式 。
// 返回可以通过上述方法构造的、运算结果等于 target 的不同表达式的数目。
// 测试链接 : https://leetcode.cn/problems/target-sum/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过哈希表统计不同表达式的数目
// 时间复杂度：O(2^(n/2))
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查数组是否为空
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用unordered_map进行计数统计，使用递归计算子集和

#include <iostream>
#include <vector>
#include <unordered_map>
using namespace std;

/**
 * 递归生成指定范围内所有可能的子集和及其出现次数
 * @param nums 输入数组
 * @param start 起始索引
 * @param end 结束索引
 * @param currentSum 当前累积和
 * @param sums 存储结果的Map，key为和，value为出现次数
 */
void generateSubsetSums(vector<int>& nums, int start, int end, int currentSum, unordered_map<int, int>& sums) {
    // 递归终止条件
    if (start == end) {
        sums[currentSum]++;
        return;
    }
    
    // 不选择当前元素（相当于添加 '-' 号）
    generateSubsetSums(nums, start + 1, end, currentSum - nums[start], sums);
    
    // 选择当前元素（相当于添加 '+' 号）
    generateSubsetSums(nums, start + 1, end, currentSum + nums[start], sums);
}

/**
 * 计算可以通过添加 '+' 或 '-' 构造的、运算结果等于 target 的不同表达式的数目
 * @param nums 输入数组
 * @param target 目标值
 * @return 不同表达式的数目
 */
int findTargetSumWays(vector<int>& nums, int target) {
    // 边界条件检查
    if (nums.empty()) {
        return 0;
    }
    
    int n = nums.size();
    
    // 使用折半搜索，将数组分为两半
    unordered_map<int, int> leftSums;
    unordered_map<int, int> rightSums;
    
    // 计算左半部分所有可能的子集和及其出现次数
    generateSubsetSums(nums, 0, n / 2, 0, leftSums);
    
    // 计算右半部分所有可能的子集和及其出现次数
    generateSubsetSums(nums, n / 2, n, 0, rightSums);
    
    // 统计满足条件的表达式数目
    int count = 0;
    for (auto& leftEntry : leftSums) {
        int leftSum = leftEntry.first;
        int leftCount = leftEntry.second;
        
        // 查找右半部分是否存在和为(target - leftSum)的子集
        int rightCount = rightSums[target - leftSum];
        count += leftCount * rightCount;
    }
    
    return count;
}

// 测试方法
int main() {
    // 测试用例1
    vector<int> nums1 = {1, 1, 1, 1, 1};
    int target1 = 3;
    cout << "测试用例1:" << endl;
    cout << "nums = [1, 1, 1, 1, 1], target = 3" << endl;
    cout << "期望输出: 5" << endl;
    cout << "实际输出: " << findTargetSumWays(nums1, target1) << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {1};
    int target2 = 1;
    cout << "测试用例2:" << endl;
    cout << "nums = [1], target = 1" << endl;
    cout << "期望输出: 1" << endl;
    cout << "实际输出: " << findTargetSumWays(nums2, target2) << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 0};
    int target3 = 1;
    cout << "测试用例3:" << endl;
    cout << "nums = [1, 0], target = 1" << endl;
    cout << "期望输出: 2" << endl;
    cout << "实际输出: " << findTargetSumWays(nums3, target3) << endl;
    
    return 0;
}