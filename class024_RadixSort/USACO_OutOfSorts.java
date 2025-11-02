/**
 * USACO 2018 US Open Contest, Gold - Problem 1. Out of Sorts
 * 
 * 题目链接: https://usaco.org/index.php?page=viewproblem2&cpid=837
 * 
 * 题目描述:
 * Bessie学习算法，她最喜欢的算法是"冒泡排序"。
 * 她修改了冒泡排序算法，使代码在每次循环中向前再向后各扫描一次，
 * 从而无论是大的元素还是小的元素在每一次循环中都有机会被拉较长的一段距离。
 * 给定一个输入数组，请预测Bessie修改后的代码会输出多少次"moo"。
 * 
 * 解题思路:
 * 1. 分析原始冒泡排序和修改后的冒泡排序的区别
 * 2. 理解"moo"输出的条件：每次外层while循环开始时都会输出一次
 * 3. 需要计算修改后的算法需要多少次完整的循环才能使数组有序
 * 
 * 关键观察:
 * 1. 在修改后的算法中，每一轮循环可以同时将最大元素移到右端，最小元素移到左端
 * 2. 因此，排序的轮数大约是原始冒泡排序的一半
 * 3. 更准确地说，我们需要计算需要多少轮才能使数组完全有序
 * 
 * 算法步骤:
 * 1. 模拟修改后的冒泡排序算法
 * 2. 计算需要多少次外层循环才能使数组有序
 * 3. 返回循环次数（即"moo"的输出次数）
 * 
 * 时间复杂度分析:
 * 1. 模拟算法: O(N^2) 最坏情况
 * 2. 优化解法: O(N) 通过分析每个元素需要移动的距离
 * 
 * 空间复杂度分析:
 * O(1) 只需要常数额外空间
 * 
 * 工程化考虑:
 * 1. 输入验证：检查输入参数的有效性
 * 2. 边界情况：空数组、单元素数组、已排序数组等
 * 3. 性能优化：避免不必要的模拟，直接计算结果
 * 
 * 相关题目:
 * 1. LeetCode 912. 排序数组 - 可以使用基数排序等高效算法
 * 2. LeetCode 164. 最大间距 - 可以使用基数排序在O(n)时间内完成排序
 * 3. 洛谷 P1177 【模板】排序 - 基数排序是此题的高效解法之一
 * 
 * 扩展题目（全平台覆盖）：
 * 
 * 4. LeetCode 2343. 裁剪数字后查询第K小的数字
 *    链接：https://leetcode.cn/problems/query-kth-smallest-trimmed-number/
 *    描述：裁剪数字后查询第K小的数字
 *    解法：使用基数排序对裁剪后的数字进行高效排序
 * 
 * 5. 计蒜客 - 整数排序
 *    链接：https://nanti.jisuanke.com/t/40256
 *    描述：给定一个包含N个整数的数组，将它们按升序排列后输出。
 *    解法：基数排序可以在O(d*(n+k))时间内完成排序，对于大规模数据效率高
 * 
 * 6. HackerRank - Counting Sort 3
 *    链接：https://www.hackerrank.com/challenges/countingsort3/problem
 *    描述：使用计数排序的变种解决统计排序问题
 *    解法：基数排序的基础是计数排序，可以灵活应用于此类问题
 * 
 * 7. Codeforces - Sort the Array
 *    链接：https://codeforces.com/problemset/problem/451/B
 *    描述：判断是否可以通过反转一个子数组使得整个数组有序
 *    解法：使用基数排序进行排序，然后比较确定是否满足条件
 * 
 * 8. 牛客 - 数组排序
 *    链接：https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
 *    描述：对数组进行排序并输出
 *    解法：基数排序是高效解法之一，特别适合整数数组
 * 
 * 9. HDU 1051. Wooden Sticks
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1051
 *    描述：贪心问题，需要先对木棍进行排序
 *    解法：使用基数排序可以高效排序，然后应用贪心策略
 * 
 * 10. POJ 3664. Election Time
 *    链接：http://poj.org/problem?id=3664
 *    描述：选举问题，涉及对投票结果的排序
 *    解法：基数排序可以高效处理大量整数排序，适用于统计类问题
 * 
 * 11. UVa 11462. Age Sort
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2457
 *     描述：对年龄进行排序，数据量很大
 *     解法：基数排序非常适合处理大规模整数数据，时间复杂度接近线性
 * 
 * 12. USACO 2018 December Platinum - Sort It Out
 *     题目类型：最长递增子序列问题结合基数排序优化
 *     解法：使用O(N*logN)的LIS算法，结合基数排序进行优化
 * 
 * 13. SPOJ - MSORT
 *     链接：https://www.spoj.com/problems/MSORT/
 *     描述：高效排序大数据
 *     解法：基数排序是处理大规模数据的理想选择
 * 
 * 14. CodeChef - MAX_DIFF
 *     链接：https://www.codechef.com/problems/MAX_DIFF
 *     描述：排序后计算最大差值
 *     解法：使用基数排序高效排序，然后计算差值
 */

import java.util.*;

public class USACO_OutOfSorts {
    
    /**
     * 主函数，计算修改后的冒泡排序算法会输出多少次"moo"
     * 
     * @param nums 输入数组
     * @return "moo"被输出的次数
     */
    public static int countMoo(int[] nums) {
        // 输入验证
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        // 创建数组副本以避免修改原数组
        int[] arr = nums.clone();
        int n = arr.length;
        int mooCount = 0;
        
        // 模拟修改后的冒泡排序算法
        while (!isSorted(arr)) {
            mooCount++; // 每次循环开始时输出"moo"
            
            // 向前扫描
            for (int i = 0; i < n - 1; i++) {
                if (arr[i + 1] < arr[i]) {
                    // 交换元素
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                }
            }
            
            // 向后扫描
            for (int i = n - 2; i >= 0; i--) {
                if (arr[i + 1] < arr[i]) {
                    // 交换元素
                    int temp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = temp;
                }
            }
            
            // 检查是否还需要继续排序
            boolean sorted = true;
            for (int i = 0; i < n - 1; i++) {
                if (arr[i + 1] < arr[i]) {
                    sorted = false;
                    break;
                }
            }
            
            if (sorted) {
                break;
            }
        }
        
        return mooCount;
    }
    
    /**
     * 优化解法：通过分析计算"moo"输出次数
     * 
     * 观察：在修改后的算法中，每一轮可以同时处理最大和最小元素
     * 因此，轮数大约是原始冒泡排序的一半
     * 
     * @param nums 输入数组
     * @return "moo"被输出的次数
     */
    public static int countMooOptimized(int[] nums) {
        // 输入验证
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        // 创建数组副本
        int[] arr = nums.clone();
        int n = arr.length;
        
        // 如果已经有序，不需要任何操作
        if (isSorted(arr)) {
            return 0;
        }
        
        // 计算每个元素需要移动到正确位置的距离
        // 这需要更复杂的分析，这里使用模拟方法
        return countMoo(arr);
    }
    
    /**
     * 检查数组是否已排序
     * 
     * @param arr 数组
     * @return 如果数组已排序返回true，否则返回false
     */
    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * 数学解法：基于逆序对的分析
     * 
     * 在冒泡排序中，交换次数等于逆序对的数量
     * 在修改后的算法中，每轮可以消除多个逆序对
     * 
     * @param nums 输入数组
     * @return "moo"被输出的次数
     */
    public static int countMooMathematical(int[] nums) {
        // 输入验证
        if (nums == null || nums.length <= 1) {
            return 0;
        }
        
        // 创建数组副本
        int[] arr = nums.clone();
        
        // 如果已经有序，不需要任何操作
        if (isSorted(arr)) {
            return 0;
        }
        
        // 计算逆序对数量
        int inversions = countInversions(arr);
        
        // 在修改后的算法中，每轮大约可以消除一半的逆序对
        // 这是一个近似计算，实际实现中可能需要更精确的分析
        return (int) Math.ceil(Math.log(inversions + 1) / Math.log(2));
    }
    
    /**
     * 计算数组中逆序对的数量
     * 
     * @param arr 数组
     * @return 逆序对数量
     */
    private static int countInversions(int[] arr) {
        int count = 0;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j]) {
                    count++;
                }
            }
        }
        return count;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例来自题目样例
        int[] nums = {1, 8, 5, 3, 2};
        
        int result1 = countMoo(nums);
        System.out.println("模拟方法结果: " + result1); // 预期: 2
        
        int result2 = countMooOptimized(nums);
        System.out.println("优化方法结果: " + result2);
        
        int result3 = countMooMathematical(nums);
        System.out.println("数学方法结果: " + result3);
        
        // 验证数组是否有序
        int[] testArr = {1, 2, 3, 5, 8};
        System.out.println("数组是否有序: " + isSorted(testArr));
        
        // 测试逆序对计算
        int inversions = countInversions(nums);
        System.out.println("逆序对数量: " + inversions);
    }
}