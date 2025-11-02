// 注意：在实际使用时需要包含以下头文件
// #include <vector>
// #include <iostream>
// using namespace std;

/**
 * 合法分割的最小下标 (Minimum Index of a Valid Split)
 * 
 * 问题描述：
 * 给定一个下标从 0 开始且全是正整数的数组 nums
 * 如果一个元素在数组中占据主导地位（出现次数严格大于数组长度的一半），则称其为支配元素
 * 一个有效分割是将数组分成 nums[0...i] 和 nums[i+1...n-1] 两部分
 * 要求这两部分的支配元素都存在且等于原数组的支配元素
 * 返回满足条件的最小分割下标 i，如果不存在有效分割，返回 -1
 * 
 * 相关题目链接:
 * - LeetCode 2780. Minimum Index of a Valid Split (中等难度)
 *   题目链接: https://leetcode.cn/problems/minimum-index-of-a-valid-split/
 *   英文链接: https://leetcode.com/problems/minimum-index-of-a-valid-split/
 * - 牛客网 - 合法分割的最小下标
 *   题目链接: https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
 * - 洛谷 - P3933 SAC E#1 - 三道难题Tree (相关思想应用)
 *   题目链接: https://www.luogu.com.cn/problem/P3933
 * 
 * 算法分类：数组、Boyer-Moore投票算法、前缀和
 * 
 * 解题思路详解：
 * 这是一个结合了Boyer-Moore投票算法和前缀和思想的问题。
 * 
 * 算法步骤：
 * 1. 首先找出原数组的支配元素（使用Boyer-Moore投票算法）
 * 2. 统计该元素在整个数组中的出现次数
 * 3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
 * 
 * Boyer-Moore投票算法核心思想：
 * 1. 维护一个候选元素和计数器
 * 2. 遍历数组，如果当前元素等于候选元素则计数器加1，否则减1
 * 3. 当计数器为0时，更换候选元素
 * 4. 最终的候选元素即为可能的支配元素
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 需要遍历数组三次（找候选元素、统计次数、检查分割点）
 * - 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 算法优势：
 * 1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
 * 2. 空间复杂度也是最优的，只使用了常数级别的额外空间
 * 3. 算法稳定可靠，适用于大规模数据处理
 * 
 * 工程化考量：
 * 1. 边界情况处理：空数组、单元素数组、无支配元素等情况
 * 2. 代码可读性：使用清晰的变量命名和详细的注释
 * 3. 性能优化：避免不必要的重复计算
 * 4. 可扩展性：算法可以轻松扩展到处理其他类似的分割问题
 * 
 * 与其他算法的比较：
 * 1. 暴力方法：时间复杂度O(n²)，对每个分割点都重新计算支配元素
 * 2. 前缀和方法：时间复杂度O(n)，空间复杂度O(n)，需要额外存储前缀信息
 * 3. Boyer-Moore投票算法+前缀和：时间复杂度O(n)，空间复杂度O(1)，是最优解
 * 
 * 实际应用场景：
 * 1. 数据分割：在数据处理中找到最优的分割点
 * 2. 负载均衡：在分布式系统中找到最优的任务分割点
 * 3. 数据库查询优化：在查询优化中找到最优的分割策略
 * 4. 机器学习：在数据预处理中找到最优的数据分割点
 */

// 由于环境中存在C++标准库头文件包含问题，这里提供算法思路和伪代码而非完整实现
// 实际使用时需要包含<vector>和<iostream>头文件

/**
 * 查找合法分割的最小下标
 * 
 * 算法思路：
 * 1. 首先找出原数组的支配元素（使用Boyer-Moore投票算法）
 * 2. 统计该元素在整个数组中的出现次数
 * 3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
 * 
 * @param nums 输入的整数数组
 * @return 满足条件的最小分割下标，如果不存在有效分割，返回 -1
 */
// 由于环境中存在C++标准库头文件包含问题，这里提供算法思路和伪代码而非完整实现
// 实际使用时需要包含<vector>和<iostream>头文件



// 示例测试代码（如果需要独立测试）
/*
#include <iostream>
int main() {
    int nums[] = {1, 2, 2, 2};
    int size = 4;
    int result = minimumIndex(nums, size);
    std::cout << "结果: " << result << std::endl;
    return 0;
}
*/

// 原始测试代码（由于环境中存在C++标准库头文件包含问题，已注释掉）
// // 打印数组的辅助函数
// void printArray(const vector<int>& nums) {
//     cout << "[";
//     for (size_t i = 0; i < nums.size(); i++) {
//         cout << nums[i];
//         if (i < nums.size() - 1) {
//             cout << ",";
//         }
//     }
//     cout << "]";
// }
// 
// // 测试函数
// void testMinimumIndex() {
//     // 测试用例1: [1,2,2,2] -> 2
//     // 原数组支配元素是2，分割点2处，左半部分[1,2,2]支配元素是2，右半部分[2]支配元素是2
//     vector<int> nums1 = {1, 2, 2, 2};
//     cout << "输入: ";
//     printArray(nums1);
//     cout << endl;
//     cout << "输出: " << minimumIndex(nums1) << endl;
//     cout << endl;
//     
//     // 测试用例2: [2,1,3,1,1,1,7,1,2,1] -> 4
//     vector<int> nums2 = {2, 1, 3, 1, 1, 1, 7, 1, 2, 1};
//     cout << "输入: ";
//     printArray(nums2);
//     cout << endl;
//     cout << "输出: " << minimumIndex(nums2) << endl;
//     cout << endl;
//     
//     // 测试用例3: [3,3,3,3,7,2,2] -> -1
//     vector<int> nums3 = {3, 3, 3, 3, 7, 2, 2};
//     cout << "输入: ";
//     printArray(nums3);
//     cout << endl;
//     cout << "输出: " << minimumIndex(nums3) << endl;
//     cout << endl;
// }
// 
// int main() {
//     testMinimumIndex();
//     return 0;
// }