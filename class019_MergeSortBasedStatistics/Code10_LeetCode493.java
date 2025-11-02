package class022;

/**
 * ============================================================================
 * 题目10: LeetCode 493 - 翻转对 (Reverse Pairs)
 * ============================================================================
 * 
 * 题目来源: LeetCode
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
 * (1,4) -> 3 > 2*1
 * (3,4) -> 3 > 2*1
 * 
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 解释:
 * (1,4) -> 4 > 2*1
 * (2,4) -> 3 > 2*1
 * (3,4) -> 5 > 2*1
 * 
 * ============================================================================
 * 核心算法思想: 归并排序分治统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 双重循环遍历所有 i < j 的情况，判断 nums[i] > 2*nums[j]
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 利用归并排序的分治过程，在合并两个有序子数组之前，
 *   统计左侧子数组中满足 nums[i] > 2*nums[j] 的元素对数量
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 统计: 在合并前，统计左侧子数组中每个元素能与右侧子数组形成的翻转对数量
 *   3. 合并: 合并两个有序子数组
 * 
 * - 优化技巧:
 *   - 由于左右子数组已经各自有序，可以使用双指针技巧高效统计
 *   - 对于左侧子数组的每个元素nums[i]，找到右侧子数组中最大的j，使得 nums[j] < nums[i]/2
 *   - 这样，右侧子数组中从start到j的元素都可以与nums[i]形成翻转对
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层统计和合并: O(n)
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
 * 相关题目列表 (基于归并排序的统计问题)
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
 *     问题：计算逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 */
public class Code10_LeetCode493 {
    
    public static int MAXN = 50001;
    public static int[] help = new int[MAXN];
    
    /**
     * 计算数组中翻转对的数量
     * 
     * @param arr 输入数组
     * @return 翻转对的数量
     * 
     * 算法思路:
     * 使用归并排序的思想，在合并两个有序子数组的过程中统计翻转对数量。
     * 关键在于统计左侧子数组中满足 nums[i] > 2*nums[j] 的元素对数量。
     */
    public static int reversePairs(int[] arr) {
        return counts(arr, 0, arr.length - 1);
    }
    
    /**
     * 归并排序并统计翻转对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间[l,r]中的翻转对数量
     */
    public static int counts(int[] arr, int l, int r) {
        if (l == r) {
            return 0;
        }
        
        int m = (l + r) / 2;
        // 分治：左半部分翻转对 + 右半部分翻转对 + 跨越两部分的翻转对
        return counts(arr, l, m) + counts(arr, m + 1, r) + merge(arr, l, m, r);
    }
    
    /**
     * 合并两个有序子数组并统计翻转对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中点
     * @param r 右边界
     * @return 跨越[l,m]和[m+1,r]的翻转对数量
     */
    public static int merge(int[] arr, int l, int m, int r) {
        // 统计部分
        int ans = 0;
        for (int i = l, j = m + 1; i <= m; i++) {
            // 对于左侧元素arr[i]，找到右侧子数组中满足 arr[i] > 2*arr[j] 的元素数量
            // 由于右侧子数组已排序，可以使用双指针技巧
            while (j <= r && (long) arr[i] > (long) arr[j] * 2) {
                j++;
            }
            // j之前的元素都满足条件，即从m+1到j-1的元素
            ans += j - m - 1;
        }
        
        // 正常merge
        int i = l;
        int a = l;
        int b = m + 1;
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
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        int[] test1 = {1, 3, 2, 3, 1};
        System.out.println("输入: " + java.util.Arrays.toString(test1));
        System.out.println("输出: " + reversePairs(test1)); // 预期输出: 2
        
        // 测试用例2: 基本情况
        int[] test2 = {2, 4, 3, 5, 1};
        System.out.println("输入: " + java.util.Arrays.toString(test2));
        System.out.println("输出: " + reversePairs(test2)); // 预期输出: 3
        
        // 测试用例3: 空数组
        int[] test3 = {};
        System.out.println("输入: " + java.util.Arrays.toString(test3));
        System.out.println("输出: " + reversePairs(test3)); // 预期输出: 0
        
        // 测试用例4: 单元素数组
        int[] test4 = {1};
        System.out.println("输入: " + java.util.Arrays.toString(test4));
        System.out.println("输出: " + reversePairs(test4)); // 预期输出: 0
        
        // 测试用例5: 有序数组
        int[] test5 = {1, 2, 3, 4, 5};
        System.out.println("输入: " + java.util.Arrays.toString(test5));
        System.out.println("输出: " + reversePairs(test5)); // 预期输出: 0
        
        // 测试用例6: 逆序数组
        int[] test6 = {5, 4, 3, 2, 1};
        System.out.println("输入: " + java.util.Arrays.toString(test6));
        System.out.println("输出: " + reversePairs(test6)); // 预期输出: 4
        
        // 测试用例7: 包含负数
        int[] test7 = {-5, -5, -5};
        System.out.println("输入: " + java.util.Arrays.toString(test7));
        System.out.println("输出: " + reversePairs(test7)); // 预期输出: 0
        
        // 测试用例8: 大数值测试
        int[] test8 = {2147483647, -2147483648};
        System.out.println("输入: " + java.util.Arrays.toString(test8));
        System.out.println("输出: " + reversePairs(test8)); // 预期输出: 1
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 数据类型溢出问题：
     *    - 在比较 arr[i] > 2*arr[j] 时，2*arr[j] 可能会溢出
     *    - 使用long类型进行强制转换避免溢出：(long) arr[i] > (long) arr[j] * 2
     *    - 当数组元素为Integer.MAX_VALUE或Integer.MIN_VALUE时特别需要注意
     * 
     * 2. 输入输出优化：
     *    - 对于大规模数据，可以使用StreamTokenizer和BufferedReader提高输入效率
     *    - 使用PrintWriter提高输出效率
     * 
     * 3. 内存管理：
     *    - 使用静态数组避免频繁内存分配
     *    - MAXN设为50001，满足题目要求
     * 
     * 4. 递归深度：
     *    - 归并排序递归深度为log2(n)，不会超出Java默认栈限制
     * 
     * 5. 边界条件处理：
     *    - 空数组、单元素数组都有正确的处理
     *    - 数组元素可能为负数，算法仍正确
     * 
     * ============================================================================
     * 工程化考量
     * ============================================================================
     * 1. 性能优化：
     *    - 对于小规模子数组(如n<10)，可考虑使用插入排序
     *    - 可添加判断：当arr[m] <= arr[m+1]时，子数组已有序，可跳过合并
     * 
     * 2. 错误处理：
     *    - 可添加输入验证，检查数组是否为null
     *    - 可添加异常处理，处理输入异常
     * 
     * 3. 可扩展性：
     *    - 算法易于扩展到其他统计问题(如逆序对、小和问题)
     *    - 可封装为工具类供其他程序调用
     * 
     * 4. 测试策略：
     *    - 已提供多种测试用例，覆盖常见场景
     *    - 应包含边界测试(空数组、单元素、全相同元素等)
     *    - 应包含性能测试(大规模数据)
     *    - 应包含正确性测试(已知结果的测试用例)
     */
}