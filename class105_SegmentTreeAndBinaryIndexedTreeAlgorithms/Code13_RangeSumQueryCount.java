// LeetCode 327. 区间和的个数
// 给定一个整数数组 nums 以及两个整数 lower 和 upper，求区间 [lower, upper] 内的区间和的个数。
// 测试链接: https://leetcode.cn/problems/count-of-range-sum/

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 区间和的个数 - 树状数组解法
 * 
 * 解题思路:
 * 1. 题目要求计算数组中区间和位于 [lower, upper] 范围内的子数组个数
 * 2. 利用前缀和思想：区间和 sum(i,j) = prefix[j+1] - prefix[i]
 * 3. 问题转化为：对于每个 j，计算有多少个 i < j 满足 lower <= prefix[j] - prefix[i] <= upper
 * 4. 进一步转化为：对于每个 j，统计 prefix[i] 的范围为 [prefix[j] - upper, prefix[j] - lower] 的 i 的个数
 * 5. 使用树状数组可以高效地统计这个范围查询
 * 6. 由于前缀和可能很大，需要进行离散化处理
 * 
 * 时间复杂度分析:
 * - 计算前缀和: O(n)
 * - 离散化: O(n log n)
 * - 构建和操作树状数组: O(n log n)
 * - 总时间复杂度: O(n log n)
 * 
 * 空间复杂度分析:
 * - 前缀和数组: O(n)
 * - 离散化数组和映射: O(n)
 * - 树状数组: O(n)
 * - 总空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 离散化处理：由于前缀和可能超出整数范围，使用long类型存储，并进行离散化
 * 2. 边界条件处理：处理空数组、单元素数组等特殊情况
 * 3. 数据类型溢出：使用long类型避免溢出
 * 4. 异常输入检查：验证输入的有效性
 * 5. 代码可读性：使用清晰的变量命名和详细的注释
 */
public class Code13_RangeSumQueryCount {
    /**
     * 树状数组类
     * 用于高效计算前缀和和单点更新
     */
    private static class FenwickTree {
        private int[] tree; // 树状数组
        private int size;   // 数组大小

        /**
         * 构造函数
         * @param size 树状数组大小
         */
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1]; // 树状数组下标从1开始
        }

        /**
         * lowbit操作，获取x的二进制表示中最低位的1所代表的值
         * @param x 输入整数
         * @return x & (-x)
         */
        private int lowbit(int x) {
            return x & (-x);
        }

        /**
         * 在指定位置增加delta
         * @param index 索引位置（从1开始）
         * @param delta 增加的值
         */
        public void update(int index, int delta) {
            // 沿树状数组向上更新所有相关节点
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }

        /**
         * 查询前缀和[1, index]
         * @param index 查询的右边界（从1开始）
         * @return 前缀和
         */
        public int query(int index) {
            int sum = 0;
            // 沿树状数组向下累加所有相关节点的值
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }

        /**
         * 查询区间和[left, right]
         * @param left 区间左边界（从1开始）
         * @param right 区间右边界（从1开始）
         * @return 区间和
         */
        public int queryRange(int left, int right) {
            if (left > right) {
                return 0;
            }
            return query(right) - query(left - 1);
        }
    }

    /**
     * 计算区间和的个数
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        // 边界条件检查
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long[] prefixSums = new long[n + 1]; // 前缀和数组

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        // 离散化处理
        TreeSet<Long> valuesSet = new TreeSet<>();
        // 将所有可能需要查询的值加入集合
        for (long sum : prefixSums) {
            valuesSet.add(sum);
            valuesSet.add(sum - lower);
            valuesSet.add(sum - upper);
        }

        // 建立值到索引的映射
        Map<Long, Integer> valueToIndex = new HashMap<>();
        int index = 1; // 索引从1开始
        for (long value : valuesSet) {
            valueToIndex.put(value, index++);
        }

        // 创建树状数组
        FenwickTree fenwickTree = new FenwickTree(valuesSet.size());
        int count = 0;

        // 从前向后遍历前缀和数组
        for (long prefixSum : prefixSums) {
            // 查询满足条件的前缀和的数量：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
            int leftIndex = valueToIndex.get(prefixSum - upper);
            int rightIndex = valueToIndex.get(prefixSum - lower);
            count += fenwickTree.queryRange(leftIndex, rightIndex);

            // 将当前前缀和加入树状数组
            int currentIndex = valueToIndex.get(prefixSum);
            fenwickTree.update(currentIndex, 1);
        }

        return count;
    }

    /**
     * 暴力解法（仅供比较，时间复杂度较高）
     * 时间复杂度: O(n²)
     * 空间复杂度: O(n)
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    public static int countRangeSumBruteForce(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long[] prefixSums = new long[n + 1];

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        int count = 0;
        // 暴力枚举所有可能的子数组
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= n; j++) {
                long rangeSum = prefixSums[j] - prefixSums[i];
                if (rangeSum >= lower && rangeSum <= upper) {
                    count++;
                }
            }
        }

        return count;
    }

    /**
     * 归并排序解法（另一种O(n log n)的解法）
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    public static int countRangeSumMergeSort(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        long[] prefixSums = new long[n + 1];

        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSums[i + 1] = prefixSums[i] + nums[i];
        }

        // 归并排序过程中统计满足条件的子数组个数
        return mergeSortAndCount(prefixSums, 0, n, lower, upper);
    }

    /**
     * 归并排序并统计满足条件的子数组个数
     * @param prefixSums 前缀和数组
     * @param left 左边界
     * @param right 右边界
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的子数组个数
     */
    private static int mergeSortAndCount(long[] prefixSums, int left, int right, int lower, int upper) {
        if (left >= right) {
            return 0;
        }

        int mid = left + (right - left) / 2;
        // 递归处理左右两部分
        int count = mergeSortAndCount(prefixSums, left, mid, lower, upper) +
                   mergeSortAndCount(prefixSums, mid + 1, right, lower, upper);

        // 统计满足条件的子数组个数
        int j = mid + 1, k = mid + 1;
        for (int i = left; i <= mid; i++) {
            // 找到最小的j，使得prefixSums[j] - prefixSums[i] >= lower
            while (j <= right && prefixSums[j] - prefixSums[i] < lower) {
                j++;
            }
            // 找到最大的k，使得prefixSums[k] - prefixSums[i] <= upper
            while (k <= right && prefixSums[k] - prefixSums[i] <= upper) {
                k++;
            }
            // 区间[j, k-1]内的所有前缀和都满足条件
            count += k - j;
        }

        // 合并两个有序数组
        merge(prefixSums, left, mid, right);

        return count;
    }

    /**
     * 合并两个有序数组
     * @param prefixSums 前缀和数组
     * @param left 左边界
     * @param mid 中间点
     * @param right 右边界
     */
    private static void merge(long[] prefixSums, int left, int mid, int right) {
        long[] temp = new long[right - left + 1];
        int i = left, j = mid + 1, k = 0;

        // 合并两个有序数组
        while (i <= mid && j <= right) {
            if (prefixSums[i] <= prefixSums[j]) {
                temp[k++] = prefixSums[i++];
            } else {
                temp[k++] = prefixSums[j++];
            }
        }

        // 处理剩余元素
        while (i <= mid) {
            temp[k++] = prefixSums[i++];
        }

        while (j <= right) {
            temp[k++] = prefixSums[j++];
        }

        // 将临时数组复制回原数组
        System.arraycopy(temp, 0, prefixSums, left, temp.length);
    }

    /**
     * 打印数组的辅助方法
     * @param arr 输入数组
     */
    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2;
        int upper1 = 2;
        System.out.println("测试用例1:");
        System.out.print("输入数组: ");
        printArray(nums1);
        System.out.println("lower: " + lower1 + ", upper: " + upper1);
        System.out.println("树状数组解法结果: " + countRangeSum(nums1, lower1, upper1));  // 期望输出: 3
        System.out.println("归并排序解法结果: " + countRangeSumMergeSort(nums1, lower1, upper1));  // 期望输出: 3
        System.out.println("暴力解法结果: " + countRangeSumBruteForce(nums1, lower1, upper1));  // 期望输出: 3

        // 测试用例2
        int[] nums2 = {0};
        int lower2 = 0;
        int upper2 = 0;
        System.out.println("\n测试用例2:");
        System.out.print("输入数组: ");
        printArray(nums2);
        System.out.println("lower: " + lower2 + ", upper: " + upper2);
        System.out.println("树状数组解法结果: " + countRangeSum(nums2, lower2, upper2));  // 期望输出: 1

        // 测试用例3 - 空数组
        int[] nums3 = {};
        int lower3 = 0;
        int upper3 = 0;
        System.out.println("\n测试用例3:");
        System.out.print("输入数组: ");
        printArray(nums3);
        System.out.println("lower: " + lower3 + ", upper: " + upper3);
        System.out.println("树状数组解法结果: " + countRangeSum(nums3, lower3, upper3));  // 期望输出: 0

        // 测试用例4 - 大规模数据
        int size = 1000;
        int[] nums4 = new int[size];
        for (int i = 0; i < size; i++) {
            nums4[i] = (i % 3 == 0) ? -1 : (i % 3 == 1) ? 0 : 1;
        }
        int lower4 = -2;
        int upper4 = 2;
        System.out.println("\n测试用例4 (大规模数据):");
        System.out.println("数组长度: " + size);
        System.out.println("lower: " + lower4 + ", upper: " + upper4);

        // 测量树状数组解法的时间
        long startTime1 = System.currentTimeMillis();
        int result1 = countRangeSum(nums4, lower4, upper4);
        long endTime1 = System.currentTimeMillis();
        System.out.println("树状数组解法结果: " + result1);
        System.out.println("树状数组解法耗时: " + (endTime1 - startTime1) + "ms");

        // 测量归并排序解法的时间
        long startTime2 = System.currentTimeMillis();
        int result2 = countRangeSumMergeSort(nums4, lower4, upper4);
        long endTime2 = System.currentTimeMillis();
        System.out.println("归并排序解法结果: " + result2);
        System.out.println("归并排序解法耗时: " + (endTime2 - startTime2) + "ms");

        // 验证两种方法结果是否一致
        System.out.println("两种方法结果一致: " + (result1 == result2));

        // 对比暴力解法（仅在小规模数据上测试）
        if (size <= 1000) {
            int smallSize = Math.min(size, 300);  // 限制暴力解法的数组大小，避免超时
            int[] smallNums = Arrays.copyOf(nums4, smallSize);

            long startTime3 = System.currentTimeMillis();
            int result3 = countRangeSumBruteForce(smallNums, lower4, upper4);
            long endTime3 = System.currentTimeMillis();
            System.out.println("暴力解法(前" + smallSize + "个元素)结果: " + result3);
            System.out.println("暴力解法耗时: " + (endTime3 - startTime3) + "ms");

            // 验证暴力解法与树状数组解法在前smallSize个元素上是否一致
            int result4 = countRangeSum(smallNums, lower4, upper4);
            System.out.println("暴力解法与树状数组解法结果一致: " + (result3 == result4));
        }
    }
}