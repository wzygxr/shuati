package class022;

import java.io.*;
import java.util.*;

/**
 * ============================================================================
 * 题目6: POJ 2299 - Ultra-QuickSort
 * ============================================================================
 * 
 * 题目来源: POJ (Peking Online Judge)
 * 题目链接: http://poj.org/problem?id=2299
 * 难度级别: 中等
 * 
 * 问题描述:
 * 在这个问题中，您必须分析一个特定的排序算法。该算法通过交换相邻元素对序列进行排序。
 * 给定一个序列，求出使用该算法进行排序所需的最少交换次数。
 * 
 * 核心洞察:
 * 最少交换相邻元素的次数等于逆序对的数量。
 * 
 * 示例输入输出:
 * 输入:
 * 5
 * 9 1 0 5 4
 * 3
 * 1 2 3
 * 0
 * 
 * 输出:
 * 6
 * 0
 * 
 * 解释:
 * 对于序列[9,1,0,5,4]，逆序对有：
 * (9,1), (9,0), (9,5), (9,4), (1,0), (5,4) 共6对
 * 
 * ============================================================================
 * 核心算法思想: 归并排序统计逆序对
 * ============================================================================
 * 
 * 方法1: 冒泡排序模拟 (不推荐)
 * - 思路: 直接模拟冒泡排序过程，统计交换次数
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 最少交换相邻元素的次数等于逆序对的数量
 * - 证明: 每次交换相邻元素可以消除一个逆序对，且这是唯一消除逆序对的方式
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 统计三种类型的逆序对
 *   3. 合并: 将两个有序子数组合并
 * 
 * - 统计跨区间逆序对的优化方法:
 *   - 在合并前，对每个左区间元素arr[i]，找到右区间中满足 arr[i] > arr[j] 的元素个数
 *   - 利用双指针技巧：由于左右子数组已排序，可以线性扫描而不需要嵌套循环
 *   - 这一步的时间复杂度为O(n)而非O(n²)
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
 * 1. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 2. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 3. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 4. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
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
 * 9. UVa 10810 - Ultra-QuickSort
 *    https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 */

public class Code06_UltraQuickSort {
    
    public static int MAXN = 500001;
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];
    
    /**
     * 计算数组中逆序对的数量（即最少交换次数）
     * 
     * @param n 数组长度
     * @return 逆序对的数量
     */
    public static long mergeSort(int n) {
        if (n < 2) {
            return 0;
        }
        return mergeSort(0, n - 1);
    }
    
    /**
     * 归并排序，在排序过程中统计逆序对数量
     * 
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]中的逆序对数量
     */
    public static long mergeSort(int l, int r) {
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
    public static long merge(int l, int m, int r) {
        // 统计逆序对数量
        long ans = 0;
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
     * 主方法，处理POJ格式的输入输出
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            if (n == 0) {
                break;
            }
            
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            
            out.println(mergeSort(n));
        }
        
        out.flush();
        out.close();
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 数据类型溢出问题：
     *    - 逆序对数量可能超过int范围，必须使用long类型存储结果
     *    - 对于n=500000的数据规模，逆序对数量最大约为n*(n-1)/2 ≈ 1.25×10^11，超过int范围
     * 
     * 2. 输入输出效率：
     *    - 使用StreamTokenizer和BufferedReader提高输入效率
     *    - 使用PrintWriter缓冲输出提高输出效率
     *    - 对于大规模数据输入输出，这种优化是必要的
     * 
     * 3. 内存管理：
     *    - 使用静态数组避免频繁创建对象
     *    - MAXN设为500001，足够处理题目要求的最大数据规模
     *    - 静态数组在类加载时初始化，在类卸载时销毁
     * 
     * 4. 递归深度控制：
     *    - Java默认递归深度限制约为1000-2000层
     *    - 对于n=500000的数据规模，递归深度约为log2(500000)≈19层，完全足够
     * 
     * 5. 位运算优化：
     *    - 可以使用(l + r) >> 1代替(l + r) / 2提高运算效率
     *    - 注意当l和r都很大时，(l + r)可能导致溢出，应改为l + ((r - l) >> 1)
     * 
     * 6. 异常处理：
     *    - 添加IOException处理，处理输入输出异常
     *    - 可以添加对极大数组的检查
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