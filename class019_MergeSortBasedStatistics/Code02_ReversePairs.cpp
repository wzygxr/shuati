#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <ios>
using namespace std;;;

// 翻转对问题，c++版
// 测试链接 : https://leetcode.com/problems/reverse-pairs/

/**
 * ============================================================================
 * 题目2: 翻转对 (Reverse Pairs)
 * ============================================================================
 * 
 * 题目来源: LeetCode 493
 * 题目链接: https://leetcode.cn/problems/reverse-pairs/
 * 难度级别: 困难
 * 
 * 问题描述:
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] ，我们就将 (i,j) 称作一个翻转对。
 * 你需要返回数组中的翻转对的数量。
 * 
 * 示例输入输出:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 解释:
 * - (1,4): 3 > 2*1
 * - (3,4): 3 > 2*1
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 解释:
 * - (1,4): 4 > 2*1
 * - (2,4): 3 > 2*1
 * - (3,4): 5 > 2*1
 * 
 * ============================================================================
 * 核心算法思想: 归并排序+双指针统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 双重循环检查每一对 (i,j) 是否满足条件
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 利用归并排序过程统计跨越左右两个子数组的翻转对
 *   - 先统计左半部分内部的翻转对
 *   - 再统计右半部分内部的翻转对
 *   - 最后统计跨越左右两部分的翻转对（关键步骤）
 * 
 * - 统计跨区间翻转对的优化方法:
 *   - 在合并前，对每个左区间元素nums[i]，找到右区间中满足 nums[i] > 2*nums[j] 的最小j
 *   - 利用双指针技巧：由于左右子数组已排序，可以线性扫描而不需要嵌套循环
 *   - 这一步的时间复杂度为O(n)而非O(n²)
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 统计三种类型的翻转对
 *   3. 合并: 将两个有序子数组合并
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层合并与统计: O(n)
 * 
 * - 空间复杂度详细计算:
 *   S(n) = O(n) + O(log n)
 *   - O(n): 辅助数组help
 *   - O(log n): 递归调用栈
 *   总计: O(n)
 * 
 * - 是否最优解: ★ 是 ★
 *   理由: 基于比较的算法下界为O(n log n)，本算法已达到最优
 * 
 * ============================================================================
 * 相关题目列表 (同类算法)
 * ============================================================================
 * 1. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 2. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 3. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 4. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 5. HackerRank - Merge Sort: Counting Inversions
 *    https://www.hackerrank.com/challenges/merge-sort/problem
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 6. HDU 4911 - Inversion
 *    http://acm.hdu.edu.cn/showproblem.php?pid=4911
 *    问题：统计数组中满足i<j且a[i]>a[j]的对的数量
 *    解法：归并排序统计逆序对
 * 
 * 7. 洛谷 P1908 - 逆序对
 *    https://www.luogu.com.cn/problem/P1908
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序统计逆序对
 * 
 * 8. SPOJ - INVCNT
 *    https://www.spoj.com/problems/INVCNT/
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 9. CodeChef - COUPON2
 *    https://www.codechef.com/problems/COUPON2
 *    问题：涉及逆序对概念的应用问题
 *    解法：归并排序统计逆序对
 * 
 * 10. AtCoder - D - Grid Repainting 2
 *     https://atcoder.jp/contests/abc129/tasks/abc129_d
 *     问题：涉及统计满足特定条件的元素对
 *     解法：类似归并排序的分治统计方法
 * 
 * 11. Codeforces - B. George and Round
 *     https://codeforces.com/contest/387/problem/B
 *     问题：需要统计满足条件的元素对
 *     解法：双指针+排序
 * 
 * 12. USACO - Sorting a Three-Valued Sequence
 *     https://train.usaco.org/usacoprob2?a=2bT6XmB9E6P&S=sots
 *     问题：涉及逆序对概念
 *     解法：归并排序统计逆序对
 * 
 * 13. 牛客网 - 数组中的逆序对
 *     https://www.nowcoder.com/practice/96bd6684e04a44eb80e6a68efc0ec6c5
 *     问题：统计数组中逆序对的总数
 *     解法：归并排序统计逆序对
 * 
 * 14. 杭电OJ - 5876
 *     http://acm.hdu.edu.cn/showproblem.php?pid=5876
 *     问题：涉及统计满足特定条件的元素对
 *     解法：类似归并排序的分治统计方法
 * 
 * 15. AizuOJ - ALDS1_5_D
 *     https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_5_D
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 16. MarsCode - Merge Sort Count
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 17. 计蒜客 - 逆序对计数
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 18. Codeforces - E. Inversions After Shuffle
 *     问题：涉及逆序对的排列问题
 *     解法：归并排序统计逆序对
 * 
 * 19. SPOJ - PSHTTR
 *     问题：涉及逆序对的应用问题
 *     解法：归并排序或树状数组
 * 
 * 20. UVa OJ - 10810
 *     https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 * 
 * C++语言特性注意事项:
 * 1. 整数溢出问题: 计算 nums[i] > 2*nums[j] 时，2*nums[j] 可能溢出，需要使用 long long 类型
 * 2. 内存管理: 使用vector代替手动内存分配，避免内存泄漏
 * 3. 递归深度: 对于大型数组，可能需要增加栈大小或考虑非递归实现
 */

#include <iostream>
#include <vector>
using namespace std;

/**
 * 翻转对详解:
 * 
 * 问题描述:
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i, j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 示例:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 
 * 解法思路:
 * 1. 暴力解法: 双重循环遍历所有可能的(i,j)对，检查是否满足条件，时间复杂度O(N^2)
 * 2. 归并排序思想: 利用归并排序分治的思想
 *    - 分: 将数组不断二分，直到只有一个元素
 *    - 治: 统计左半部分、右半部分内部的翻转对数量，再统计跨越两部分的翻转对数量
 *    - 合: 将两部分合并排序
 *    
 *    关键点在于统计跨越两部分的翻转对时，由于两部分各自已经有序，可以使用双指针技巧优化:
 *    对于左半部分的每个元素nums[i]，找出右半部分满足nums[i] > 2*nums[j]的元素个数
 * 
 * 时间复杂度: O(N * logN) - 归并排序的时间复杂度
 * 空间复杂度: O(N) - 辅助数组的空间复杂度
 * 
 * 相关题目:
 * 1. LeetCode 315. 计算右侧小于当前元素的个数
 * 2. LeetCode 327. 区间和的个数
 * 3. 剑指Offer 51. 数组中的逆序对
 * 4. 牛客网 - 计算数组的小和
 */

const int MAXN = 50001;
int help[MAXN];

// 提交以下代码到LeetCode
/*
#include <vector>
using namespace std;

int merge(vector<int>& nums, int l, int m, int r) {
    // 统计翻转对数量
    int ans = 0;
    int j = m + 1;
    for (int i = l; i <= m; i++) {
        // 找到右半部分中第一个不满足 nums[i] > 2*nums[j] 的位置
        while (j <= r && (long long)nums[i] > 2LL * nums[j]) {
            j++;
        }
        // j之前的元素都满足条件
        ans += j - m - 1;
    }
    
    // 正常合并两个有序数组
    int i = l;
    int a = l, b = m + 1;
    while (a <= m && b <= r) {
        if (nums[a] <= nums[b]) {
            help[i++] = nums[a++];
        } else {
            help[i++] = nums[b++];
        }
    }
    while (a <= m) {
        help[i++] = nums[a++];
    }
    while (b <= r) {
        help[i++] = nums[b++];
    }
    for (i = l; i <= r; i++) {
        nums[i] = help[i];
    }
    
    return ans;
}

int mergeSort(vector<int>& nums, int l, int r) {
    if (l == r) {
        return 0;
    }
    
    int m = (l + r) / 2;
    return mergeSort(nums, l, m) + mergeSort(nums, m + 1, r) + merge(nums, l, m, r);
}

int reversePairs(vector<int>& nums) {
    if (nums.empty()) {
        return 0;
    }
    
    // 创建副本避免修改原数组
    vector<int> numsCopy = nums;
    return mergeSort(numsCopy, 0, numsCopy.size() - 1);
}
*/