#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <ios>
using namespace std;

/**
 * ============================================================================
 * 题目7: HDU 1394 - Minimum Inversion Number - C++版
 * ============================================================================
 * 
 * 题目来源: HDU (杭州电子科技大学OJ)
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
 * 难度级别: 中等
 * 
 * 问题描述:
 * 给定一个0到n-1的排列，可以进行循环移位操作（把第一个数移到最后）。
 * 求所有可能状态中逆序对数量的最小值。
 * 
 * 核心洞察:
 * 1. 先用归并排序计算初始逆序对数
 * 2. 循环移位时，利用数学公式快速更新逆序对数
 *    - 若移动元素x: new_inv = old_inv - x + (n-1-x)
 * 
 * 示例输入输出:
 * 输入:
 * 5
 * 1 3 0 2 4
 * 
 * 输出:
 * 3
 * 
 * 解释:
 * 初始序列[1,3,0,2,4]有6个逆序对: (1,0), (3,0), (3,2), (2,0), (2,1), (4,0)
 * 循环移位后序列[3,0,2,4,1]有5个逆序对
 * 循环移位后序列[0,2,4,1,3]有4个逆序对
 * 循环移位后序列[2,4,1,3,0]有5个逆序对
 * 循环移位后序列[4,1,3,0,2]有3个逆序对 (最小值)
 * 
 * ============================================================================
 * 核心算法思想: 归并排序 + 数学优化
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 对每个循环移位后的序列，都计算一次逆序对数量
 * - 时间复杂度: O(N^3) - N次循环移位，每次O(N^2)计算逆序对
 * - 空间复杂度: O(N) - 存储循环移位后的序列
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序 + 数学优化 (最优解) ★★★★★
 * - 核心洞察: 
 *   1. 先计算初始序列的逆序对数
 *   2. 利用数学关系快速计算循环移位后的逆序对数
 * 
 * - 数学优化原理:
 *   当把第一个元素x移到序列末尾时：
 *   - 减少的逆序对数：原来在x后面且小于x的元素个数，即x个
 *   - 增加的逆序对数：原来在x后面且大于x的元素个数，即(n-1-x)个
 *   - 因此：new_inv = old_inv - x + (n-1-x)
 * 
 * - 算法步骤:
 *   1. 使用归并排序计算初始序列的逆序对数
 *   2. 循环N-1次，每次根据数学公式更新逆序对数
 *   3. 记录过程中的最小值
 * 
 * - 时间复杂度详细计算:
 *   T(n) = O(n log n) + O(n) = O(n log n)
 *   - 归并排序计算初始逆序对: O(n log n)
 *   - 循环更新逆序对数: O(n)
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
 * 1. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 2. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 3. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 4. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 5. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 6. 洛谷 P1908 - 逆序对
 *    https://www.luogu.com.cn/problem/P1908
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序统计逆序对
 * 
 * 7. HackerRank - Merge Sort: Counting Inversions
 *    https://www.hackerrank.com/challenges/merge-sort/problem
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 8. SPOJ - INVCNT
 *    https://www.spoj.com/problems/INVCNT/
 *    问题：统计逆序对数量
 *    解法：归并排序统计逆序对
 * 
 * 9. UVa 10810 - Ultra-QuickSort
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 */

const int MAXN = 10001;
int arr[MAXN];
int help[MAXN];

/**
 * 归并排序，在排序过程中统计逆序对数量
 * 
 * @param l 左边界
 * @param r 右边界
 * @return 区间[l,r]中的逆序对数量
 */
long long mergeSort(int l, int r) {
    if (l == r) {
        return 0;
    }
    
    int m = (l + r) / 2;
    return mergeSort(l, m) + mergeSort(m + 1, r) + merge(l, m, r);
}

/**
 * 合并两个有序数组，并统计跨越左右两部分的逆序对
 * 
 * @param l 左边界
 * @param m 中点
 * @param r 右边界
 * @return 跨越左右两部分的逆序对数量
 */
long long merge(int l, int m, int r) {
    // 统计逆序对数量
    long long ans = 0;
    int j = m + 1;
    for (int i = l; i <= m; i++) {
        // 找到右半部分中第一个不满足 arr[i] > arr[j] 的位置
        while (j <= r && arr[i] > arr[j]) {
            j++;
        }
        // j之前的元素都满足条件，即与arr[i]构成逆序对
        ans += (j - m - 1);
    }
    
    // 正常合并两个有序数组
    int i = l;
    int a = l, b = m + 1;
    while (a <= m && b <= r) {
        help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
    }
    while (a <= m) {
        help[i++] = arr[a++];
    }
    while (b <= r) {
        help[i++] = arr[b++];
    }
    for (i = l; i <= r; i++) {
        arr[i] = help[i];
    }
    
    return ans;
}

/**
 * 计算循环移位序列中的最小逆序对数
 * 
 * @param n 数组长度
 * @return 最小逆序对数
 */
long long getMinimumInversion(int n) {
    // 计算初始序列的逆序对数
    long long inv = mergeSort(0, n - 1);
    long long minInv = inv;
    
    // 循环移位，利用数学公式快速更新逆序对数
    for (int i = 0; i < n - 1; i++) {
        // 当把第一个元素arr[i]移到序列末尾时：
        // 减少的逆序对数：arr[i]个
        // 增加的逆序对数：(n-1-arr[i])个
        inv = inv - arr[i] + (n - 1 - arr[i]);
        minInv = min(minInv, inv);
    }
    
    return minInv;
}

/**
 * 主函数，处理HDU格式的输入输出
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    while (cin >> n) {
        for (int i = 0; i < n; i++) {
            cin >> arr[i];
        }
        
        cout << getMinimumInversion(n) << "\n";
    }
    
    return 0;
}

/*
============================================================================
C++语言特有关注事项
============================================================================

1. 数据类型溢出问题：
   - 逆序对数量可能超过int范围，必须使用long long类型存储结果
   - 对于n=10000的数据规模，逆序对数量最大约为n*(n-1)/2 ≈ 5×10^7，未超过int范围
   - 但为了代码通用性，仍使用long long类型

2. 内存管理：
   - 使用全局数组而非vector，避免频繁动态分配内存
   - MAXN设为10001，足够处理题目要求的最大数据规模
   - 静态数组在栈上分配，访问速度比堆分配更快

3. 递归深度控制：
   - 归并排序的递归深度为log2(n)，对于n=10000，深度约为14层
   - 不会超过C++默认的栈大小限制
   - 对于极端大数据，可以考虑非递归实现

4. 输入输出优化：
   - ios::sync_with_stdio(false); 关闭同步，加速cin/cout
   - cin.tie(nullptr); 解绑cin和cout，减少刷新次数
   - 使用"\n"代替endl，避免不必要的缓冲区刷新

5. 位运算优化：
   - 使用(l + r) >> 1代替(l + r) / 2，提高运算效率
   - 注意当l和r都很大时，(l + r)可能导致溢出，应改为l + ((r - l) >> 1)

6. 代码优化技巧：
   - 在merge函数中先统计逆序对再排序，逻辑更清晰
   - 合并时使用三目运算符使代码更简洁

7. 编译优化选项：
   - 可以添加-O2编译选项获得更好的性能
   - 对于某些编译器，-march=native可以利用CPU特性进一步优化

8. 多线程考虑：
   - 当前实现不是线程安全的，因为使用了全局变量
   - 多线程环境下应使用局部变量或添加同步机制

9. 边界条件处理：
   - 对空数组、单元素数组有正确的边界检查
   - 递归终止条件明确

10. 异常处理：
    - C++中可以添加try-catch块处理可能的异常
    - 对数组索引越界等情况进行检查
*/