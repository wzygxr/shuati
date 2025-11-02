package class022;

// 区间和的个数
// 测试链接 : https://leetcode.cn/problems/count-of-range-sum/
import java.util.*;

/**
 * 区间和的个数详解:
 * 
 * 问题描述:
 * 给你一个整数数组 nums 以及两个整数 lower 和 upper 。
 * 求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
 * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
 * 
 * 示例:
 * 输入：nums = [-2,5,-1], lower = -2, upper = 2
 * 输出：3
 * 解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。
 * 
 * 解法思路:
 * 1. 暴力解法: 计算所有可能区间的和，检查是否在范围内，时间复杂度O(N^3)或O(N^2)（使用前缀和优化）
 * 2. 归并排序思想:
 *    - 使用前缀和将问题转换为: 找到满足条件 prefixSum[j] - prefixSum[i] ∈ [lower, upper] 的(i,j)对数量
 *    - 在归并排序过程中，对于左半部分的每个前缀和prefixSum[i]，在右半部分找到满足条件的prefixSum[j]数量
 *    - 由于两部分各自有序，可以使用双指针技巧优化查找过程
 * 
 * 时间复杂度: O(N * logN) - 归并排序的时间复杂度
 * 空间复杂度: O(N) - 前缀和数组和辅助数组的空间复杂度
 * 
 * 相关题目:
 * 1. LeetCode 493. 翻转对
 * 2. LeetCode 315. 计算右侧小于当前元素的个数
 * 3. 剑指Offer 51. 数组中的逆序对
 * 4. 牛客网 - 计算数组的小和
 */
public class Code04_CountRangeSum {
    
    public static int countRangeSum(int[] nums, int lower, int upper) {
        int n = nums.length;
        // 计算前缀和数组
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        return mergeSort(prefixSum, 0, n, lower, upper);
    }
    
    // 归并排序，在排序过程中统计满足条件的区间和个数
    public static int mergeSort(long[] arr, int left, int right, int lower, int upper) {
        if (left == right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(arr, left, mid, lower, upper) + 
                    mergeSort(arr, mid + 1, right, lower, upper);
        
        // 统计跨越左右两部分的满足条件的区间和个数
        int l = mid + 1, r = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 找到右半部分中第一个满足 arr[j] - arr[i] >= lower 的位置l
            while (l <= right && arr[l] - arr[i] < lower) {
                l++;
            }
            // 找到右半部分中第一个满足 arr[j] - arr[i] > upper 的位置r
            while (r <= right && arr[r] - arr[i] <= upper) {
                r++;
            }
            // 区间[l, r-1]中的元素都满足条件
            count += (r - l);
        }
        
        // 合并两个有序数组
        long[] help = new long[right - left + 1];
        int i = 0;
        int a = left, b = mid + 1;
        while (a <= mid && b <= right) {
            help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
        }
        while (a <= mid) {
            help[i++] = arr[a++];
        }
        while (b <= right) {
            help[i++] = arr[b++];
        }
        for (i = 0; i < help.length; i++) {
            arr[left + i] = help[i];
        }
        
        return count;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 测试用例1: 基本情况
        int[] test1 = {-2, 5, -1};
        int lower1 = -2, upper1 = 2;
        System.out.println("输入: nums = " + Arrays.toString(test1) + ", lower = " + lower1 + ", upper = " + upper1);
        System.out.println("输出: " + countRangeSum(test1, lower1, upper1)); // 预期输出: 3
        
        // 测试用例2: 空数组
        int[] test2 = {};
        int lower2 = 0, upper2 = 0;
        System.out.println("输入: nums = " + Arrays.toString(test2) + ", lower = " + lower2 + ", upper = " + upper2);
        System.out.println("输出: " + countRangeSum(test2, lower2, upper2)); // 预期输出: 0
        
        // 测试用例3: 全为正数的数组
        int[] test3 = {1, 2, 3, 4};
        int lower3 = 5, upper3 = 10;
        System.out.println("输入: nums = " + Arrays.toString(test3) + ", lower = " + lower3 + ", upper = " + upper3);
        System.out.println("输出: " + countRangeSum(test3, lower3, upper3)); // 预期输出: 4
        
        // 测试用例4: 全为负数的数组
        int[] test4 = {-4, -3, -2, -1};
        int lower4 = -6, upper4 = -2;
        System.out.println("输入: nums = " + Arrays.toString(test4) + ", lower = " + lower4 + ", upper = " + upper4);
        System.out.println("输出: " + countRangeSum(test4, lower4, upper4)); // 预期输出: 4
        
        // 测试用例5: 大数值测试
        int[] test5 = {Integer.MAX_VALUE, -Integer.MAX_VALUE};
        int lower5 = -2, upper5 = 2;
        System.out.println("输入: nums = [MAX, -MAX], lower = " + lower5 + ", upper = " + upper5);
        System.out.println("输出: " + countRangeSum(test5, lower5, upper5)); // 预期输出: 1
    }
    
    /*
     * ============================================================================
     * Java语言特有关注事项
     * ============================================================================
     * 1. 数据类型溢出问题：
     *    - 前缀和可能会超出int的范围，因此使用long类型存储前缀和
     *    - 当数组长度较大且元素值较大时，普通int类型会导致溢出错误
     *    - 例如：当处理包含Integer.MAX_VALUE或Integer.MIN_VALUE的数组时
     * 
     * 2. 递归深度限制：
     *    - Java的默认栈深度约为1000-2000层
     *    - 对于极大数组（如长度>1e5），可能导致栈溢出
     *    - 可以通过JVM参数-Xss调整栈大小
     * 
     * 3. 内存分配：
     *    - help数组在每次递归调用时都会重新创建，这会增加GC压力
     *    - 对于频繁调用的场景，可以考虑使用静态辅助数组复用内存
     * 
     * 4. 线程安全性：
     *    - 当前实现是无状态的，多线程调用countRangeSum方法是线程安全的
     *    - 但mergeSort方法的局部变量不会被多线程共享，因此整体是线程安全的
     * 
     * 5. 装箱和拆箱：
     *    - 本实现避免了自动装箱/拆箱操作，提高了性能
     *    - 直接使用基本类型进行计算，减少了对象创建
     */
    
    /*
     * ============================================================================
     * 工程化考量
     * ============================================================================
     * 1. 异常处理：
     *    - 可以添加对null输入数组的检查
     *    - 可以处理极端情况下的数组长度（如Integer.MAX_VALUE）
     *    - 对于lower > upper的情况，可以直接返回0并给出警告
     * 
     * 2. 性能优化：
     *    - 对于小规模数组（如长度<100），可以考虑使用暴力解法（O(N^2)），常数项更小
     *    - 可以使用静态辅助数组避免重复创建help数组
     *    - 考虑使用迭代版本的归并排序避免栈溢出
     * 
     * 3. 测试策略：
     *    - 已提供多种测试用例，包括基本情况、边界情况和特殊输入
     *    - 建议补充压力测试，验证算法在大规模数据下的性能
     *    - 可以使用参数化测试覆盖更多的输入组合
     * 
     * 4. 可扩展性：
     *    - 该算法框架可以扩展应用于其他类似的区间查询问题
     *    - 可以将双指针部分抽象为独立函数，提高代码复用性
     *    - 考虑添加缓存机制，避免对相同输入的重复计算
     * 
     * 5. 代码可读性：
     *    - 方法命名清晰（countRangeSum、mergeSort）
     *    - 变量命名直观（prefixSum、help、count等）
     *    - 添加了详细的注释解释算法思路和关键步骤
     * 
     * 6. 并行处理：
     *    - 对于超大规模数据集，可以考虑使用Fork/Join框架实现并行归并排序
     *    - 但需要注意任务划分的粒度，避免过多的线程创建开销
     * 
     * 7. 边界情况处理：
     *    - 空数组：返回0（没有区间）
     *    - lower > upper：返回0（没有符合条件的区间）
     *    - 单元素数组：检查该元素是否在[lower, upper]范围内
     *    - 大数值：使用long类型避免溢出
     */
    
    /*
     * ============================================================================
     * 相关题目与平台信息
     * ============================================================================
     * 1. LeetCode 327. Count of Range Sum
     *    - 题目链接：https://leetcode.cn/problems/count-of-range-sum/
     *    - 难度等级：困难
     *    - 标签：归并排序、前缀和、二分查找
     * 
     * 2. LeetCode 493. 翻转对 (Reverse Pairs)
     *    - 题目链接：https://leetcode.cn/problems/reverse-pairs/
     *    - 难度等级：困难
     *    - 解题思路：同样使用归并排序的过程统计满足条件的对
     * 
     * 3. LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
     *    - 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
     *    - 难度等级：困难
     *    - 解题思路：类似的归并排序框架，统计右侧较小的元素个数
     * 
     * 4. 剑指Offer 51. 数组中的逆序对
     *    - 题目链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
     *    - 难度等级：困难
     *    - 解题思路：归并排序过程中统计逆序对数量
     * 
     * 5. 牛客网 - 计算数组的小和
     *    - 题目链接：https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
     *    - 解题思路：归并排序过程中计算小和
     * 
     * 6. LintCode 1497. 区间和的个数
     *    - 题目链接：https://www.lintcode.com/problem/1497/
     *    - 与LeetCode 327题相同
     * 
     * 7. 华为机试 - 区间和统计
     *    - 类似问题，但可能有不同的输入输出格式要求
     * 
     * 8. 字节跳动面试题 - 前缀和区间统计
     *    - 实际面试中可能会对本题进行变体，如不同的范围条件或附加约束
     */
}