package class022;

import java.io.*;

/**
 * ============================================================================
 * 题目11: POJ 2299 - Ultra-QuickSort
 * ============================================================================
 * 
 * 题目来源: POJ
 * 题目链接: http://poj.org/problem?id=2299
 * 难度级别: 中等
 * 
 * 问题描述:
 * 在这个问题中，您必须分析某些特定的排序算法的性能。该算法工作过程如下：
 * 1. 检查输入序列是否已经排序。
 * 2. 如果序列已经排序，则算法终止。
 * 3. 如果序列未排序，则交换输入序列中两个相邻的元素，然后返回步骤1。
 * 
 * 你的任务是计算将输入序列排序所需的最少交换次数。
 * 
 * 输入格式:
 * 输入包含若干测试用例。
 * 每个测试用例的第一行包含一个正整数n，n<=500000，表示序列的长度。
 * 下一行包含n个不同的整数a1,...,an，(0<=ai<=999,999,999)。
 * 输入以n=0的一行结束，该行不应被处理。
 * 
 * 输出格式:
 * 对于每个测试用例，输出一行包含一个整数，表示将序列排序所需的最少交换次数。
 * 
 * 示例输入输出:
 * 输入:
 * 5
 * 9 1 0 5 4
 * 3
 * 1 2 3
 * 0
 * 输出:
 * 6
 * 0
 * 
 * 解释:
 * 对于第一个测试用例[9,1,0,5,4]，需要6次交换才能排序：
 * 1. 交换9和1得到[1,9,0,5,4]
 * 2. 交换9和0得到[1,0,9,5,4]
 * 3. 交换9和5得到[1,0,5,9,4]
 * 4. 交换9和4得到[1,0,5,4,9]
 * 5. 交换5和4得到[1,0,4,5,9]
 * 6. 交换1和0得到[0,1,4,5,9]
 * 
 * 但实际上，最少交换次数等于逆序对的数量。
 * 
 * ============================================================================
 * 核心算法思想: 归并排序统计逆序对
 * ============================================================================
 * 
 * 关键洞察:
 * 将序列排序所需的最少相邻交换次数等于序列中逆序对的数量。
 * 
 * 证明思路:
 * 1. 每次相邻交换只会减少一个逆序对
 * 2. 当序列有序时，逆序对数量为0
 * 3. 因此，最少交换次数等于初始逆序对数量
 * 
 * 算法:
 * 使用归并排序统计逆序对数量。
 * 
 * 时间复杂度: O(n log n)
 * 空间复杂度: O(n)
 * 
 * ============================================================================
 * 相关题目列表
 * ============================================================================
 * 1. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 2. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 3. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 4. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
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
 * 10. UVa 10810 - Ultra-QuickSort
 *     https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *     问题：与POJ 2299相同
 *     解法：归并排序统计逆序对
 */
public class Code11_POJ2299 {
    
    public static int MAXN = 500001;
    public static int[] arr = new int[MAXN];
    public static int[] help = new int[MAXN];
    
    /**
     * 计算数组中逆序对的数量
     * 
     * @param n 数组长度
     * @return 逆序对的数量
     * 
     * 算法思路:
     * 使用归并排序的思想，在合并两个有序子数组的过程中统计逆序对数量。
     * 当从左侧子数组选取元素时，右侧子数组中已处理的元素都小于该元素，
     * 因此这些元素与当前元素构成逆序对。
     */
    public static long countInversions(int n) {
        return mergeSort(0, n - 1);
    }
    
    /**
     * 归并排序并统计逆序对数量
     * 
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]中的逆序对数量
     */
    public static long mergeSort(int l, int r) {
        if (l == r) {
            return 0;
        }
        
        int m = l + (r - l) / 2;
        // 分治：左半部分逆序对 + 右半部分逆序对 + 跨越两部分的逆序对
        return mergeSort(l, m) + mergeSort(m + 1, r) + merge(l, m, r);
    }
    
    /**
     * 合并两个有序子数组并统计逆序对数量
     * 
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 跨越[l,m]和[m+1,r]的逆序对数量
     */
    public static long merge(int l, int m, int r) {
        long count = 0;
        int i = l;      // help数组的当前位置
        int a = l;      // 左侧数组指针
        int b = m + 1;  // 右侧数组指针
        
        // 合并过程，同时统计逆序对
        while (a <= m && b <= r) {
            if (arr[a] <= arr[b]) {
                // 左侧元素小于等于右侧元素
                // 右侧数组中已处理的元素(b - (m+1))个都小于arr[a]，构成逆序对
                count += (b - m - 1);
                help[i++] = arr[a++];
            } else {
                // 右侧元素小于左侧元素
                help[i++] = arr[b++];
            }
        }
        
        // 处理左侧剩余元素
        while (a <= m) {
            // 左侧剩余元素与右侧所有元素都构成逆序对
            count += (b - m - 1);
            help[i++] = arr[a++];
        }
        
        // 处理右侧剩余元素
        while (b <= r) {
            help[i++] = arr[b++];
        }
        
        // 将help数组拷贝回原数组
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
        
        return count;
    }
    
    /**
     * 主函数 - 处理输入输出
     * 
     * 输入处理优化:
     * 使用BufferedReader和StreamTokenizer提高输入效率
     * 对于大规模数据(500000个元素)，这种优化非常必要
     */
    public static void main(String[] args) throws IOException {
        // 使用高效IO处理
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        while (true) {
            // 读取数组长度
            in.nextToken();
            int n = (int) in.nval;
            
            // 输入以n=0结束
            if (n == 0) {
                break;
            }
            
            // 读取数组元素
            for (int i = 0; i < n; i++) {
                in.nextToken();
                arr[i] = (int) in.nval;
            }
            
            // 计算并输出逆序对数量（即最少交换次数）
            out.println(countInversions(n));
        }
        
        out.flush();
        out.close();
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 数据类型溢出问题：
     *    - 逆序对数量可能超过int范围，使用long类型存储结果
     *    - 当n=500000时，最坏情况下逆序对数量可达n*(n-1)/2≈1.25*10^11，超出int范围
     * 
     * 2. 输入输出优化：
     *    - 使用StreamTokenizer和BufferedReader提高输入效率
     *    - 使用PrintWriter提高输出效率
     *    - 对于大规模数据，Scanner类效率较低
     * 
     * 3. 内存管理：
     *    - 使用静态数组避免频繁内存分配
     *    - MAXN设为500001，满足题目要求
     * 
     * 4. 递归深度：
     *    - 归并排序递归深度为log2(500000)≈19层，不会超出Java默认栈限制
     * 
     * 5. 边界条件处理：
     *    - 空数组、单元素数组都有正确的处理
     *    - 数组元素可能为0，算法仍正确
     * 
     * ============================================================================
     * 工程化考量
     * ============================================================================
     * 1. 性能优化：
     *    - 对于小规模子数组(如n<10)，可考虑使用插入排序
     *    - 可添加判断：当arr[m] <= arr[m+1]时，子数组已有序，可跳过合并
     * 
     * 2. 错误处理：
     *    - 可添加输入验证，检查n是否在合法范围内
     *    - 可添加异常处理，处理输入格式错误
     * 
     * 3. 可扩展性：
     *    - 算法易于扩展到其他统计问题(如翻转对、小和问题)
     *    - 可封装为工具类供其他程序调用
     * 
     * 4. 测试策略：
     *    - 应包含边界测试(空数组、单元素、全相同元素等)
     *    - 应包含性能测试(大规模数据)
     *    - 应包含正确性测试(已知结果的测试用例)
     */
}