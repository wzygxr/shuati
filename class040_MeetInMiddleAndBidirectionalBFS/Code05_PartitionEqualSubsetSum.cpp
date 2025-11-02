// 分割等和子集
// 给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
// 测试链接 : https://leetcode.cn/problems/partition-equal-subset-sum/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法解决，将数组分为两半分别计算所有可能的和，
// 然后通过哈希表查找是否存在两个子集的和相等
// 时间复杂度：O(2^(n/2))
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查数组是否为空或长度小于2
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用unordered_set进行快速查找，使用递归计算子集和

#include <iostream>
#include <vector>
#include <unordered_set>
#include <algorithm>
using namespace std;

/**
 * 递归生成指定范围内所有可能的子集和
 * @param nums 输入数组
 * @param start 起始索引
 * @param end 结束索引
 * @param currentSum 当前累积和
 * @param sums 存储结果的集合
 */
void generateSubsetSums(vector<int>& nums, int start, int end, int currentSum, unordered_set<int>& sums) {
    // 递归终止条件
    if (start == end) {
        sums.insert(currentSum);
        return;
    }
    
    // 不选择当前元素
    generateSubsetSums(nums, start + 1, end, currentSum, sums);
    
    // 选择当前元素
    generateSubsetSums(nums, start + 1, end, currentSum + nums[start], sums);
}

/**
 * 判断是否可以将数组分割成两个子集，使得两个子集的元素和相等
 * @param nums 输入数组
 * @return 如果可以分割返回true，否则返回false
 */
bool canPartition(vector<int>& nums) {
    // 边界条件检查
    if (nums.empty() || nums.size() < 2) {
        return false;
    }
    
    // 计算数组总和
    int sum = 0;
    for (int num : nums) {
        sum += num;
    }
    
    // 如果总和是奇数，无法分割成两个相等的子集
    if (sum % 2 != 0) {
        return false;
    }
    
    int target = sum / 2;
    
    // 如果最大元素大于目标值，无法分割
    int maxNum = *max_element(nums.begin(), nums.end());
    if (maxNum > target) {
        return false;
    }
    
    int n = nums.size();
    
    // 使用折半搜索，将数组分为两半
    unordered_set<int> leftSums;
    unordered_set<int> rightSums;
    
    // 计算左半部分所有可能的子集和
    generateSubsetSums(nums, 0, n / 2, 0, leftSums);
    
    // 计算右半部分所有可能的子集和
    generateSubsetSums(nums, n / 2, n, 0, rightSums);
    
    // 检查是否存在两个子集的和等于目标值
    for (int leftSum : leftSums) {
        // 如果左半部分的某个子集和正好等于目标值
        if (leftSum == target) {
            return true;
        }
        
        // 如果右半部分存在一个子集，其和等于目标值减去左半部分的子集和
        if (rightSums.count(target - leftSum)) {
            return true;
        }
    }
    
    return false;
}

// 测试方法
int main() {
    // 测试用例1
    vector<int> nums1 = {1, 5, 11, 5};
    cout << "测试用例1:" << endl;
    cout << "nums = [1, 5, 11, 5]" << endl;
    cout << "期望输出: true" << endl;
    cout << "实际输出: " << (canPartition(nums1) ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例2
    vector<int> nums2 = {1, 2, 3, 5};
    cout << "测试用例2:" << endl;
    cout << "nums = [1, 2, 3, 5]" << endl;
    cout << "期望输出: false" << endl;
    cout << "实际输出: " << (canPartition(nums2) ? "true" : "false") << endl;
    cout << endl;
    
    // 测试用例3
    vector<int> nums3 = {1, 2, 5};
    cout << "测试用例3:" << endl;
    cout << "nums = [1, 2, 5]" << endl;
    cout << "期望输出: false" << endl;
    cout << "实际输出: " << (canPartition(nums3) ? "true" : "false") << endl;
    
    return 0;
}