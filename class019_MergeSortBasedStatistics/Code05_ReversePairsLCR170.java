package class022;

import java.util.*;

/**
 * ============================================================================
 * 题目5: 剑指 Offer 51 - 数组中的逆序对 (LCR 170)
 * ============================================================================
 * 
 * 题目来源: 剑指Offer / LCR 170
 * 题目链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 * 难度级别: 困难
 * 
 * 问题描述:
 * 在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
 * 输入一个数组，求出这个数组中的逆序对的总数。
 * 
 * 示例输入输出:
 * 输入: [7,5,6,4]
 * 输出: 5
 * 解释: 逆序对有(7,5), (7,6), (7,4), (5,4), (6,4)
 * 
 * ============================================================================
 * 核心算法思想: 归并排序分治统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 双重循环检查每一对 (i,j) 是否满足 i<j 且 nums[i] > nums[j]
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 利用归并排序过程统计跨越左右两个子数组的逆序对
 *   - 先统计左半部分内部的逆序对
 *   - 再统计右半部分内部的逆序对
 *   - 最后统计跨越左右两部分的逆序对（关键步骤）
 * 
 * - 统计跨区间逆序对的优化方法:
 *   - 在合并前，对每个左区间元素nums[i]，找到右区间中满足 nums[i] > nums[j] 的元素个数
 *   - 利用双指针技巧：由于左右子数组已排序，可以线性扫描而不需要嵌套循环
 *   - 这一步的时间复杂度为O(n)而非O(n²)
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 统计三种类型的逆序对
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
 * 1. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 2. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 3. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 4. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 5. HDU 1394 - Minimum Inversion Number
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    问题：将数组循环左移，求所有可能排列中的最小逆序对数量
 *    解法：归并排序+逆序对性质分析
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
 * 9. CodeChef - INVCNT
 *    https://www.codechef.com/problems/INVCNT
 *    问题：统计逆序对数量
 *    解法：归并排序或树状数组
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 */

public class Code05_ReversePairsLCR170 {
    
    public static int MAXN = 50001;
    public static int[] help = new int[MAXN];
    
    /**
     * 计算数组中逆序对的数量
     * 
     * @param nums 输入数组
     * @return 逆序对的数量
     */
    public static int reversePairs(int[] nums) {
        if (nums == null || nums.length < 2) {
            return 0;
        }
        return mergeSort(nums, 0, nums.length - 1);
    }
    
    /**
     * 归并排序，在排序过程中统计逆序对数量
     * 
     * @param arr 输入数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]中的逆序对数量
     */
    public static int mergeSort(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        
        int m = (l + r) / 2;
        return mergeSort(arr, l, m) + mergeSort(arr, m + 1, r) + merge(arr, l, m, r);
    }
    
    /**
     * 合并两个有序数组，并统计跨越左右两部分的逆序对
     * 
     * @param arr 输入数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 跨越左右两部分的逆序对数量
     */
    public static int merge(int[] arr, int l, int m, int r) {
        // 统计逆序对数量
        int ans = 0;
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
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        int[] test1 = {7, 5, 6, 4};
        System.out.println("输入: " + Arrays.toString(test1));
        System.out.println("输出: " + reversePairs(test1)); // 预期输出: 5
        
        // 测试用例2: 空数组
        int[] test2 = {};
        System.out.println("输入: " + Arrays.toString(test2));
        System.out.println("输出: " + reversePairs(test2)); // 预期输出: 0
        
        // 测试用例3: 单元素数组
        int[] test3 = {1};
        System.out.println("输入: " + Arrays.toString(test3));
        System.out.println("输出: " + reversePairs(test3)); // 预期输出: 0
        
        // 测试用例4: 升序数组
        int[] test4 = {1, 2, 3, 4};
        System.out.println("输入: " + Arrays.toString(test4));
        System.out.println("输出: " + reversePairs(test4)); // 预期输出: 0
        
        // 测试用例5: 降序数组
        int[] test5 = {4, 3, 2, 1};
        System.out.println("输入: " + Arrays.toString(test5));
        System.out.println("输出: " + reversePairs(test5)); // 预期输出: 6
        
        // 测试用例6: 重复元素
        int[] test6 = {2, 2, 2, 2};
        System.out.println("输入: " + Arrays.toString(test6));
        System.out.println("输出: " + reversePairs(test6)); // 预期输出: 0
        
        // 测试用例7: 包含负数
        int[] test7 = {-1, -2, 3, -4};
        System.out.println("输入: " + Arrays.toString(test7));
        System.out.println("输出: " + reversePairs(test7)); // 预期输出: 4
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 数据类型溢出问题：
     *    - 逆序对数量可能超过int范围，但题目约束范围内不会溢出
     *    - 对于极端情况，可以使用long类型存储结果
     * 
     * 2. 内存管理：
     *    - 使用静态数组help避免频繁创建对象
     *    - 静态数组在类加载时初始化，在类卸载时销毁
     *    - 注意线程安全问题，多线程环境下可能需要额外同步
     * 
     * 3. 递归深度控制：
     *    - Java默认递归深度限制约为1000-2000层
     *    - 对于n=50000的数据规模，递归深度约为log2(50000)≈16层，完全足够
     * 
     * 4. 输入输出优化：
     *    - 对于大规模数据输入，可以使用BufferedReader和StreamTokenizer优化
     *    - 对于输出，可以使用PrintWriter缓冲输出
     * 
     * 5. 位运算优化：
     *    - 可以使用(l + r) >> 1代替(l + r) / 2提高运算效率
     *    - 注意当l和r都很大时，(l + r)可能导致溢出，应改为l + ((r - l) >> 1)
     * 
     * 6. 异常处理：
     *    - 可以添加对null输入的检查
     *    - 对于极大数组，可以添加数组长度检查
     * 
     * 7. 性能优化：
     *    - 对于小规模子数组（如长度<10），可以使用插入排序代替归并排序
     *    - 可以添加判断条件，当arr[m] <= arr[m+1]时，子数组已有序，跳过合并
     * 
     * 8. 代码优化建议：
     *    - 减少对象创建和垃圾回收压力
     *    - 使用基本数据类型而非包装类（避免自动装箱/拆箱开销）
     *    - 合理设置MAXN常量，预留适当空间
     */
}