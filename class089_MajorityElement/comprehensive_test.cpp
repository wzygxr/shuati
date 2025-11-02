// 注意：在实际使用时需要包含以下头文件
// #include <vector>
// #include <iostream>
// #include <algorithm>
// #include <map>
// #include <random>
// using namespace std;

/**
 * 水王数相关算法综合测试
 * 包含所有主要的水王数相关算法实现和测试用例
 * 
 * 本文件综合测试了以下算法：
 * 1. 基础水王数问题 (出现次数大于n/2) - Boyer-Moore投票算法
 * 2. 多数元素 II (出现次数大于n/3) - 扩展Boyer-Moore投票算法
 * 3. 合法分割的最小下标 - Boyer-Moore投票算法+前缀和
 * 4. 出现次数大于n/k的数 - 通用Boyer-Moore投票算法
 * 5. 子数组中占绝大多数的元素 - 随机化+二分查找
 * 
 * 相关题目链接:
 * - LeetCode 169. Majority Element (基础水王数)
 *   题目链接: https://leetcode.cn/problems/majority-element/
 * - LeetCode 229. Majority Element II (多数元素 II)
 *   题目链接: https://leetcode.cn/problems/majority-element-ii/
 * - LeetCode 2780. Minimum Index of a Valid Split (合法分割的最小下标)
 *   题目链接: https://leetcode.cn/problems/minimum-index-of-a-valid-split/
 * - LeetCode 1157. Online Majority Element In Subarray (子数组中占绝大多数的元素)
 *   题目链接: https://leetcode.cn/problems/online-majority-element-in-subarray/
 * - 牛客网 - 水王数相关题目
 *   题目链接: https://www.nowcoder.com/practice/5f3c9f8d4ba44525b3eb961de1910611
 * - 洛谷 - P2367 【模板】多数元素 II
 *   题目链接: https://www.luogu.com.cn/problem/P2367
 * 
 * 算法分类：数组、哈希表、计数、分治、Boyer-Moore投票算法、随机化算法、二分查找
 * 
 * 算法复杂度对比：
 * 1. 基础水王数问题：时间复杂度O(n)，空间复杂度O(1)
 * 2. 多数元素 II：时间复杂度O(n)，空间复杂度O(1)
 * 3. 合法分割的最小下标：时间复杂度O(n)，空间复杂度O(1)
 * 4. 出现次数大于n/k的数：时间复杂度O(n*k)，空间复杂度O(k)
 * 5. 子数组中占绝大多数的元素：初始化O(n)，查询期望O(logn)，空间复杂度O(n)
 */

// 由于环境中存在C++标准库头文件包含问题，这里提供算法思路和伪代码而非完整实现
// 实际使用时需要包含<vector>、<iostream>、<algorithm>、<map>、<random>等头文件

/**
 * 基础水王数问题 (出现次数大于n/2)
 * 使用Boyer-Moore投票算法
 * 
 * 算法思路：
 * 1. 维护一个候选元素和计数器
 * 2. 遍历数组，如果当前元素等于候选元素则计数器加1，否则减1
 * 3. 当计数器为0时，更换候选元素
 * 4. 最终的候选元素即为可能的水王数
 * 5. 验证候选元素是否真的是水王数
 * 
 * 时间复杂度：O(n) - 需要遍历数组两次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 */
// int findMajorityElement(vector<int>& nums) {
//     // Boyer-Moore投票算法第一阶段：找出候选元素
//     int candidate = 0;
//     int count = 0;
//     
//     for (int num : nums) {
//         if (count == 0) {
//             candidate = num;
//             count = 1;
//         } else if (num == candidate) {
//             count++;
//         } else {
//             count--;
//         }
//     }
//     
//     // 验证候选元素是否真的是水王数
//     count = 0;
//     for (int num : nums) {
//         if (num == candidate) {
//             count++;
//         }
//     }
//     
//     return count > nums.size() / 2 ? candidate : -1;
// }

/**
 * 多数元素 II (出现次数大于n/3)
 * 使用扩展的Boyer-Moore投票算法
 * 
 * 算法思路：
 * 1. 维护两个候选元素和它们的计数器
 * 2. 遍历数组，根据规则更新候选元素和计数器
 * 3. 验证候选元素是否真的满足条件
 * 
 * 时间复杂度：O(n) - 需要遍历数组两次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 */
// vector<int> findMajorityElementsII(vector<int>& nums) {
//     // 初始化两个候选元素和它们的计数
//     int cand1 = 0, cand2 = 0;
//     int count1 = 0, count2 = 0;
//     
//     // 第一遍遍历，找出候选元素
//     for (int num : nums) {
//         if (count1 > 0 && num == cand1) {
//             count1++;
//         } else if (count2 > 0 && num == cand2) {
//             count2++;
//         } else if (count1 == 0) {
//             cand1 = num;
//             count1 = 1;
//         } else if (count2 == 0) {
//             cand2 = num;
//             count2 = 1;
//         } else {
//             count1--;
//             count2--;
//         }
//     }
//     
//     // 第二遍遍历，统计候选元素的真实出现次数
//     count1 = 0;
//     count2 = 0;
//     for (int num : nums) {
//         if (num == cand1) {
//             count1++;
//         } else if (num == cand2) {
//             count2++;
//         }
//     }
//     
//     // 构造结果列表
//     vector<int> result;
//     int n = nums.size();
//     if (count1 > n / 3) {
//         result.push_back(cand1);
//     }
//     if (count2 > n / 3) {
//         result.push_back(cand2);
//     }
//     
//     return result;
// }

/**
 * 合法分割的最小下标
 * 使用Boyer-Moore投票算法+前缀和
 * 
 * 算法思路：
 * 1. 找出原数组的支配元素
 * 2. 统计该元素在整个数组中的出现次数
 * 3. 遍历所有可能的分割点，检查分割后的两部分是否都满足支配元素条件
 * 
 * 时间复杂度：O(n) - 需要遍历数组三次
 * 空间复杂度：O(1) - 只使用了常数级别的额外空间
 */
// int findMinimumIndexValidSplit(vector<int>& nums) {
//     // 第一步：使用Boyer-Moore投票算法找出候选元素
//     int candidate = 0;
//     int count = 0;
//     
//     // 投票阶段：找出可能的支配元素
//     for (int num : nums) {
//         if (count == 0) {
//             candidate = num;
//             count = 1;
//         } else if (num == candidate) {
//             count++;
//         } else {
//             count--;
//         }
//     }
//     
//     // 第二步：统计候选元素在整个数组中的出现次数
//     count = 0;
//     for (int num : nums) {
//         if (num == candidate) {
//             count++;
//         }
//     }
//     
//     // 第三步：遍历所有可能的分割点，检查是否满足条件
//     int n = nums.size();
//     int leftCount = 0; // 左半部分中候选元素的出现次数
//     
//     // 遍历所有可能的分割点 i (0 <= i < n-1)
//     for (int i = 0; i < n - 1; i++) {
//         // 更新左半部分中候选元素的出现次数
//         if (nums[i] == candidate) {
//             leftCount++;
//         }
//         
//         // 计算右半部分中候选元素的出现次数
//         int rightCount = count - leftCount;
//         
//         // 检查左半部分是否满足支配元素条件
//         bool leftValid = leftCount * 2 > (i + 1);
//         
//         // 检查右半部分是否满足支配元素条件
//         bool rightValid = rightCount * 2 > (n - i - 1);
//         
//         // 如果两部分都满足条件，则找到了有效分割点
//         if (leftValid && rightValid) {
//             return i;
//         }
//     }
//     
//     // 不存在有效分割点
//     return -1;
// }

/**
 * 出现次数大于n/k的数
 * 使用通用的Boyer-Moore投票算法
 * 
 * 算法思路：
 * 1. 维护k-1个候选元素和它们的计数器
 * 2. 遍历数组，根据规则更新候选元素和计数器
 * 3. 验证候选元素是否真的满足条件
 * 
 * 时间复杂度：O(n*k) - 需要遍历数组两次，每次遍历都需要检查k-1个候选元素
 * 空间复杂度：O(k) - 需要存储k-1个候选元素和它们的计数
 */
// vector<int> findMoreThanNK(vector<int>& nums, int k) {
//     // 初始化候选元素数组 [值, 计数]
//     vector<vector<int>> candidates(k - 1, vector<int>(2, 0));
//     
//     // 更新候选元素的辅助函数
//     auto updateCandidates = [&](int num) {
//         // 检查是否已存在
//         for (int i = 0; i < k - 1; i++) {
//             if (candidates[i][0] == num && candidates[i][1] > 0) {
//                 candidates[i][1]++;
//                 return;
//             }
//         }
//         
//         // 检查是否有空位
//         for (int i = 0; i < k - 1; i++) {
//             if (candidates[i][1] == 0) {
//                 candidates[i][0] = num;
//                 candidates[i][1] = 1;
//                 return;
//             }
//         }
//         
//         // 所有位置都被占用，计数都减1
//         for (int i = 0; i < k - 1; i++) {
//             if (candidates[i][1] > 0) {
//                 candidates[i][1]--;
//             }
//         }
//     };
//     
//     // 更新候选元素
//     for (int num : nums) {
//         updateCandidates(num);
//     }
//     
//     // 验证候选元素
//     vector<int> result;
//     int n = nums.size();
//     for (int i = 0; i < k - 1; i++) {
//         if (candidates[i][1] > 0) {
//             int candidate = candidates[i][0];
//             int count = 0;
//             for (int num : nums) {
//                 if (candidate == num) {
//                     count++;
//                 }
//             }
//             if (count > n / k) {
//                 result.push_back(candidate);
//             }
//         }
//     }
//     
//     return result;
// }

/**
 * 子数组中占绝大多数的元素（随机化方法）
 * 使用随机化+二分查找
 * 
 * 算法思路：
 * 1. 使用随机化方法：由于多数元素在子数组中出现次数超过阈值，随机选择索引有很大概率选到多数元素
 * 2. 预处理每个元素出现的所有位置，使用二分查找快速统计某个元素在区间内的出现次数
 * 3. 为了提高准确率，可以多次随机选择并验证
 * 
 * 时间复杂度：初始化O(n)，查询期望O(logn)
 * 空间复杂度：O(n)
 */
// class MajorityChecker {
// private:
//     vector<int> arr;
//     map<int, vector<int>> positions;
//     default_random_engine generator;
//     
// public:
//     MajorityChecker(vector<int>& arr) {
//         this->arr = arr;
//         // 预处理：记录每个元素出现的所有位置
//         for (int i = 0; i < arr.size(); i++) {
//             positions[arr[i]].push_back(i);
//         }
//     }
//     
//     int query(int left, int right, int threshold) {
//         // 随机化方法：随机选择区间内的元素进行验证
//         // 由于多数元素出现次数超过threshold，随机选择命中多数元素的概率较高
//         uniform_int_distribution<int> distribution(left, right);
//         
//         for (int i = 0; i < 20; i++) {  // 尝试20次，可以调整次数以平衡准确率和性能
//             // 随机选择区间内的一个位置
//             int random_index = distribution(generator);
//             int candidate = arr[random_index];
//             
//             // 使用二分查找计算该候选元素在区间[left, right]内的出现次数
//             vector<int>& pos = positions[candidate];
//             // 找到第一个大于等于left的位置
//             int left_bound = lower_bound(pos.begin(), pos.end(), left) - pos.begin();
//             // 找到第一个大于right的位置
//             int right_bound = upper_bound(pos.begin(), pos.end(), right) - pos.begin();
//             // 计算区间内出现次数
//             int count = right_bound - left_bound;
//             
//             // 如果出现次数达到阈值，返回该元素
//             if (count >= threshold) {
//                 return candidate;
//             }
//         }
//         
//         // 未找到满足条件的元素
//         return -1;
//     }
// };

// 打印数组的辅助函数
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

// 打印列表的辅助函数
// void printList(const vector<int>& nums) {
//     cout << "[";
//     for (size_t i = 0; i < nums.size(); i++) {
//         cout << nums[i];
//         if (i < nums.size() - 1) {
//             cout << ", ";
//         }
//     }
//     cout << "]";
// }

// 主测试函数
// int main() {
//     cout << "=== 水王数相关算法综合测试 ===" << endl << endl;
//     
//     // 测试用例1: 基础水王数问题
//     cout << "1. 基础水王数问题 (出现次数大于n/2):" << endl;
//     vector<int> nums1 = {3, 2, 3};
//     cout << "输入: ";
//     printArray(nums1);
//     cout << endl;
//     cout << "输出: " << findMajorityElement(nums1) << endl;
//     cout << endl;
//     
//     vector<int> nums2 = {2, 2, 1, 1, 1, 2, 2};
//     cout << "输入: ";
//     printArray(nums2);
//     cout << endl;
//     cout << "输出: " << findMajorityElement(nums2) << endl;
//     cout << endl;
//     
//     // 测试用例2: 多数元素 II
//     cout << "2. 多数元素 II (出现次数大于n/3):" << endl;
//     vector<int> nums3 = {3, 2, 3};
//     cout << "输入: ";
//     printArray(nums3);
//     cout << endl;
//     cout << "输出: ";
//     vector<int> result1 = findMajorityElementsII(nums3);
//     printList(result1);
//     cout << endl << endl;
//     
//     vector<int> nums4 = {1};
//     cout << "输入: ";
//     printArray(nums4);
//     cout << endl;
//     cout << "输出: ";
//     vector<int> result2 = findMajorityElementsII(nums4);
//     printList(result2);
//     cout << endl << endl;
//     
//     // 测试用例3: 合法分割的最小下标
//     cout << "3. 合法分割的最小下标:" << endl;
//     vector<int> nums5 = {1, 2, 2, 2};
//     cout << "输入: ";
//     printArray(nums5);
//     cout << endl;
//     cout << "输出: " << findMinimumIndexValidSplit(nums5) << endl;
//     cout << endl;
//     
//     vector<int> nums6 = {2, 1, 3, 1, 1, 1, 7, 1, 2, 1};
//     cout << "输入: ";
//     printArray(nums6);
//     cout << endl;
//     cout << "输出: " << findMinimumIndexValidSplit(nums6) << endl;
//     cout << endl;
//     
//     // 测试用例4: 出现次数大于n/k的数
//     cout << "4. 出现次数大于n/k的数 (k=3):" << endl;
//     vector<int> nums7 = {3, 2, 3};
//     cout << "输入: ";
//     printArray(nums7);
//     cout << endl;
//     cout << "输出: ";
//     vector<int> result3 = findMoreThanNK(nums7, 3);
//     printList(result3);
//     cout << endl << endl;
//     
//     vector<int> nums8 = {1, 1, 1, 2, 2, 3, 3, 3, 3, 3, 3};
//     cout << "输入: ";
//     printArray(nums8);
//     cout << endl;
//     cout << "输出: ";
//     vector<int> result4 = findMoreThanNK(nums8, 3);
//     printList(result4);
//     cout << endl << endl;
//     
//     // 测试用例5: 子数组中占绝大多数的元素
//     cout << "5. 子数组中占绝大多数的元素:" << endl;
//     vector<int> arr = {1, 1, 2, 2, 1, 1};
//     MajorityChecker checker(arr);
//     cout << "数组: ";
//     printArray(arr);
//     cout << endl;
//     cout << "query(0,5,4): " << checker.query(0, 5, 4) << endl;  // 应该返回 1
//     cout << "query(0,3,3): " << checker.query(0, 3, 3) << endl;  // 应该返回 -1
//     cout << "query(2,3,2): " << checker.query(2, 3, 2) << endl;  // 应该返回 2
//     cout << endl;
//     
//     cout << "=== 测试完成 ===" << endl;
//     
//     return 0;
// }