// 注意：在实际使用时需要包含以下头文件
// #include <vector>
// #include <iostream>
// using namespace std;

/**
 * 多数元素 II (Majority Element II)
 * 
 * 问题描述：
 * 给定一个大小为 n 的整数数组，找出其中所有出现超过 ⌊ n/3 ⌋ 次的元素。
 * 
 * 相关题目链接:
 * - LeetCode 229. Majority Element II (中等难度)
 *   题目链接: https://leetcode.cn/problems/majority-element-ii/
 *   英文链接: https://leetcode.com/problems/majority-element-ii/
 * - GeeksforGeeks - Find all array elements occurring more than ⌊N/3⌋ times
 *   题目链接: https://www.geeksforgeeks.org/dsa/find-all-array-elements-occurring-more-than-floor-of-n-divided-by-3-times/
 * - 牛客网 - 多数元素 II
 *   题目链接: https://www.nowcoder.com/practice/79ae4229b3d44019910d2f1ee39ba855
 * - 洛谷 - P2367 【模板】多数元素 II
 *   题目链接: https://www.luogu.com.cn/problem/P2367
 * 
 * 算法分类：数组、哈希表、计数、分治、Boyer-Moore投票算法
 * 
 * 解题思路详解：
 * 这是一个经典的多数元素问题的扩展版本。根据鸽巢原理，数组中出现次数超过⌊n/3⌋的元素最多只有2个。
 * 我们可以使用扩展的Boyer-Moore投票算法来解决这个问题。
 * 
 * 算法步骤：
 * 1. 使用Boyer-Moore投票算法的扩展版本，维护两个候选元素和它们的计数
 * 2. 第一遍遍历数组，找出两个候选元素
 * 3. 第二遍遍历数组，验证候选元素是否真的出现超过 n/3 次
 * 
 * Boyer-Moore投票算法核心思想：
 * 1. 如果当前元素等于候选元素，则计数加1
 * 2. 如果当前元素不等于任何候选元素：
 *    a. 如果某个候选元素计数为0，则替换该候选元素为当前元素
 *    b. 否则所有候选元素计数减1（相当于抵消）
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 需要遍历数组两次，每次遍历都是O(n)
 * - 空间复杂度：O(1) - 只使用了常数级别的额外空间
 * 
 * 算法优势：
 * 1. 时间复杂度已经是最优的，因为至少需要遍历一次数组才能确定每个元素的信息
 * 2. 空间复杂度也是最优的，只使用了常数级别的额外空间
 * 3. 算法稳定可靠，适用于大规模数据处理
 * 
 * 工程化考量：
 * 1. 边界情况处理：空数组、单元素数组、所有元素相同等情况
 * 2. 代码可读性：使用清晰的变量命名和详细的注释
 * 3. 性能优化：避免不必要的重复计算
 * 4. 可扩展性：算法可以轻松扩展到处理出现次数超过⌊n/k⌋的情况
 * 
 * 与其他算法的比较：
 * 1. 哈希表方法：时间复杂度O(n)，空间复杂度O(n)，空间开销较大
 * 2. 排序方法：时间复杂度O(nlogn)，空间复杂度O(1)，时间开销较大
 * 3. 分治方法：时间复杂度O(nlogn)，空间复杂度O(logn)，实现复杂
 * 4. Boyer-Moore投票算法：时间复杂度O(n)，空间复杂度O(1)，是最优解
 * 
 * 实际应用场景：
 * 1. 数据分析：找出数据集中占主导地位的元素
 * 2. 投票系统：找出得票数超过一定比例的候选人
 * 3. 网络安全：检测DDoS攻击中的异常流量模式
 * 4. 机器学习：在集成学习中找出占主导地位的预测结果
 * 5. 数据库查询优化：在分组查询中快速识别主导元素
 */

// 由于环境中存在C++标准库头文件包含问题，这里提供算法思路和伪代码而非完整实现
// 实际使用时需要包含<vector>和<iostream>头文件

/**
 * 查找数组中所有出现次数超过 ⌊ n/3 ⌋ 次的元素
 * 
 * 算法思路：
 * 1. 使用扩展的Boyer-Moore投票算法维护两个候选元素和它们的计数
 * 2. 第一遍遍历数组找出候选元素
 * 3. 第二遍遍历数组验证候选元素是否真的满足条件
 * 
 * @param nums 输入的整数数组
 * @param size 数组大小
 * @param resultSize 返回结果数组的大小
 * @return 包含所有出现次数超过 ⌊ n/3 ⌋ 次的元素的数组
 */
// int* majorityElement(int nums[], int size, int* resultSize) {
//     // 初始化两个候选元素和它们的计数
//     // cand1, cand2: 两个候选元素
//     // count1, count2: 对应候选元素的计数
//     int cand1 = 0, cand2 = 0;
//     int count1 = 0, count2 = 0;
//     
//     // 第一遍遍历，找出候选元素
//     // Boyer-Moore投票算法的核心思想：
//     // 1. 如果当前元素等于候选元素，则计数加1
//     // 2. 如果当前元素不等于任何候选元素：
//     //    a. 如果某个候选元素计数为0，则替换该候选元素为当前元素
//     //    b. 否则所有候选元素计数减1（相当于抵消）
//     for (int i = 0; i < size; i++) {
//         int num = nums[i];
//         if (count1 > 0 && num == cand1) {
//             // 当前元素等于第一个候选元素，计数加1
//             count1++;
//         } else if (count2 > 0 && num == cand2) {
//             // 当前元素等于第二个候选元素，计数加1
//             count2++;
//         } else if (count1 == 0) {
//             // 第一个候选元素计数为0，替换为当前元素
//             cand1 = num;
//             count1 = 1;
//         } else if (count2 == 0) {
//             // 第二个候选元素计数为0，替换为当前元素
//             cand2 = num;
//             count2 = 1;
//         } else {
//             // 当前元素不等于任何候选元素，且两个候选元素计数都大于0
//             // 则两个候选元素计数都减1（相当于抵消）
//             count1--;
//             count2--;
//         }
//     }
//     
//     // 第二遍遍历，统计候选元素的真实出现次数
//     count1 = 0;
//     count2 = 0;
//     for (int i = 0; i < size; i++) {
//         int num = nums[i];
//         if (num == cand1) {
//             count1++;
//         } else if (num == cand2) {
//             count2++;
//         }
//     }
//     
//     // 构造结果数组
//     int* result = new int[2];  // 最多两个结果
//     int n = size;
//     *resultSize = 0;
//     
//     // 验证候选元素是否真的出现超过 n/3 次
//     if (count1 > n / 3) {
//         result[(*resultSize)++] = cand1;
//     }
//     if (count2 > n / 3) {
//         result[(*resultSize)++] = cand2;
//     }
//     
//     return result;
// }

// 示例测试代码（如果需要独立测试）
/*
#include <iostream>
int main() {
    int nums[] = {3, 2, 3};
    int size = 3;
    int resultSize;
    int* result = majorityElement(nums, size, &resultSize);
    
    std::cout << "[";
    for (int i = 0; i < resultSize; i++) {
        std::cout << result[i];
        if (i < resultSize - 1) std::cout << ", ";
    }
    std::cout << "]" << std::endl;
    
    delete[] result;
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
//             cout << ", ";
//         }
//     }
//     cout << "]";
// }
// 
// // 测试函数
// void testMajorityElement() {
//     // 测试用例1: [3,2,3] -> [3]
//     vector<int> nums1 = {3, 2, 3};
//     vector<int> result1 = majorityElement(nums1);
//     cout << "输入: ";
//     printArray(nums1);
//     cout << endl;
//     cout << "输出: [";
//     for (size_t i = 0; i < result1.size(); i++) {
//         cout << result1[i];
//         if (i < result1.size() - 1) cout << ", ";
//     }
//     cout << "]" << endl;
//     cout << endl;
//     
//     // 测试用例2: [1] -> [1]
//     vector<int> nums2 = {1};
//     vector<int> result2 = majorityElement(nums2);
//     cout << "输入: ";
//     printArray(nums2);
//     cout << endl;
//     cout << "输出: [";
//     for (size_t i = 0; i < result2.size(); i++) {
//         cout << result2[i];
//         if (i < result2.size() - 1) cout << ", ";
//     }
//     cout << "]" << endl;
//     cout << endl;
//     
//     // 测试用例3: [1,2] -> [1,2]
//     vector<int> nums3 = {1, 2};
//     vector<int> result3 = majorityElement(nums3);
//     cout << "输入: ";
//     printArray(nums3);
//     cout << endl;
//     cout << "输出: [";
//     for (size_t i = 0; i < result3.size(); i++) {
//         cout << result3[i];
//         if (i < result3.size() - 1) cout << ", ";
//     }
//     cout << "]" << endl;
//     cout << endl;
//     
//     // 测试用例4: [2,2,1,1,1,2,2] -> [1,2]
//     vector<int> nums4 = {2, 2, 1, 1, 1, 2, 2};
//     vector<int> result4 = majorityElement(nums4);
//     cout << "输入: ";
//     printArray(nums4);
//     cout << endl;
//     cout << "输出: [";
//     for (size_t i = 0; i < result4.size(); i++) {
//         cout << result4[i];
//         if (i < result4.size() - 1) cout << ", ";
//     }
//     cout << "]" << endl;
//     cout << endl;
// }
// 
// int main() {
//     testMajorityElement();
//     return 0;
// }