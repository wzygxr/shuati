package class109;

import java.util.Arrays;

/**
 * 翻转对问题
 * 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们就将 (i,j) 称作一个重要翻转对。
 * 你需要返回给定数组中的重要翻转对的数量。
 * 
 * 示例 1:
 * 输入: [1,3,2,3,1]
 * 输出: 2
 * 
 * 示例 2:
 * 输入: [2,4,3,5,1]
 * 输出: 3
 * 
 * 提示：
 * 1 <= nums.length <= 5 * 10^4
 * -2^31 <= nums[i] <= 2^31 - 1
 * 
 * 解题思路：
 * 使用归并排序的思想，在归并的过程中统计翻转对的数量。
 * 对于区间 [l, r]，我们将其分为 [l, mid] 和 [mid + 1, r]，先统计左右子区间内的翻转对数量，
 * 然后统计跨越左右子区间的翻转对数量，最后进行归并排序。
 * 
 * 时间复杂度分析：
 * - 归并排序的时间复杂度为 O(n log n)
 * - 每一层递归中，统计翻转对的时间为 O(n)
 * - 总时间复杂度为 O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外的辅助数组存储临时数据，空间复杂度为 O(n)
 * - 递归调用栈的深度为 O(log n)
 * - 总空间复杂度为 O(n)
 * 
 * LeetCode 493. 翻转对
 * 链接：https://leetcode.cn/problems/reverse-pairs/
 */
public class Code06_ReversePairs {
    
    // 最大数组长度
    public static int MAXN = 50001;
    
    // 辅助数组，用于归并过程
    public static long[] help = new long[MAXN];
    
    /**
     * 计算重要翻转对的数量
     * 
     * @param nums 输入数组
     * @return 重要翻转对的数量
     */
    public static int reversePairs(int[] nums) {
        // 处理空数组或只有一个元素的情况
        if (nums == null || nums.length < 2) {
            return 0;
        }
        
        // 转换为long类型，防止溢出
        long[] arr = new long[nums.length];
        for (int i = 0; i < nums.length; i++) {
            arr[i] = nums[i];
        }
        
        // 调用归并排序并统计翻转对
        return f(arr, 0, arr.length - 1);
    }
    
    /**
     * 归并排序并统计区间[l, r]内的翻转对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param r 右边界
     * @return 区间内的翻转对数量
     */
    private static int f(long[] arr, int l, int r) {
        // 递归终止条件：只有一个元素时，没有翻转对
        if (l == r) {
            return 0;
        }
        
        // 计算中间位置
        int m = l + ((r - l) >> 1);
        
        // 统计左半部分、右半部分以及跨越中间的翻转对数量
        return f(arr, l, m) + f(arr, m + 1, r) + merge(arr, l, m, r);
    }
    
    /**
     * 合并两个有序数组，并统计跨越中间的翻转对数量
     * 
     * @param arr 数组
     * @param l 左边界
     * @param m 中间位置
     * @param r 右边界
     * @return 跨越中间的翻转对数量
     */
    private static int merge(long[] arr, int l, int m, int r) {
        // 统计翻转对数量
        int ans = 0;
        // 计算满足 arr[i] > 2 * arr[j] 的对数
        for (int i = l, j = m + 1; i <= m; i++) {
            // 对于每个i，找到最大的j使得 arr[i] > 2 * arr[j]
            while (j <= r && arr[i] > 2 * arr[j]) {
                j++;
            }
            // j - (m + 1) 就是满足条件的j的数量
            ans += j - (m + 1);
        }
        
        // 归并排序
        int i = l;
        int p1 = l;
        int p2 = m + 1;
        
        while (p1 <= m && p2 <= r) {
            help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
        }
        
        while (p1 <= m) {
            help[i++] = arr[p1++];
        }
        
        while (p2 <= r) {
            help[i++] = arr[p2++];
        }
        
        // 将辅助数组中的元素复制回原数组
        for (i = l; i <= r; i++) {
            arr[i] = help[i];
        }
        
        return ans;
    }
    
    // ==================== 补充题目：LeetCode 315. 计算右侧小于当前元素的个数 ====================
    /**
     * LeetCode 315. 计算右侧小于当前元素的个数
     * 链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
     * 题目：给定一个整数数组 nums，按要求返回一个新数组 counts。
     * 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
     * 
     * 解题思路：
     * 1. 离散化处理数组元素
     * 2. 使用树状数组从右向左扫描，统计比当前元素小的元素个数
     */
    public static int[] countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }
        
        int n = nums.length;
        int[] result = new int[n];
        
        // 离散化处理
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);
        for (int i = 0; i < n; i++) {
            nums[i] = Arrays.binarySearch(sorted, nums[i]) + 1; // 映射到1~n
        }
        
        // 树状数组实现
        FenwickTree bit = new FenwickTree(n);
        
        // 从右向左扫描
        for (int i = n - 1; i >= 0; i--) {
            // 查询比当前元素小的个数（即查询[1, nums[i]-1]的和）
            result[i] = bit.query(nums[i] - 1);
            // 更新树状数组，当前元素出现次数+1
            bit.update(nums[i], 1);
        }
        
        return result;
    }
    
    // 树状数组实现
    static class FenwickTree {
        private int[] tree;
        private int n;
        
        public FenwickTree(int size) {
            this.n = size;
            this.tree = new int[size + 1];
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int index, int delta) {
            while (index <= n) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
    }
    
    // ==================== 补充题目：LeetCode 327. 区间和的个数 ====================
    /**
     * LeetCode 327. 区间和的个数
     * 链接：https://leetcode.cn/problems/count-of-range-sum/
     * 题目：给定一个整数数组 nums 以及两个整数 lower 和 upper 。
     * 求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
     * 区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。
     * 
     * 解题思路：
     * 1. 计算前缀和数组
     * 2. 使用归并排序的思想，在归并过程中统计满足条件的区间和数量
     */
    public static int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        int n = nums.length;
        long[] prefixSum = new long[n + 1];
        
        // 计算前缀和
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 使用归并排序的方法统计符合条件的区间和
        return mergeSort(prefixSum, 0, n, lower, upper);
    }
    
    private static int mergeSort(long[] arr, int left, int right, int lower, int upper) {
        if (left >= right) {
            return 0;
        }
        
        int mid = left + (right - left) / 2;
        int count = mergeSort(arr, left, mid, lower, upper) + 
                    mergeSort(arr, mid + 1, right, lower, upper);
        
        // 统计跨越中间的符合条件的区间和
        int i = left;
        int L = mid + 1; // 第一个大于等于 (arr[i] + lower) 的位置
        int R = mid + 1; // 第一个大于 (arr[i] + upper) 的位置
        
        while (i <= mid) {
            // 找到L和R的位置
            while (L <= right && arr[L] - arr[i] < lower) {
                L++;
            }
            while (R <= right && arr[R] - arr[i] <= upper) {
                R++;
            }
            count += R - L;
            i++;
        }
        
        // 归并两个有序数组
        merge(arr, left, mid, right);
        
        return count;
    }
    
    private static void merge(long[] arr, int left, int mid, int right) {
        long[] temp = new long[right - left + 1];
        int i = left;
        int j = mid + 1;
        int k = 0;
        
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }
        
        while (i <= mid) {
            temp[k++] = arr[i++];
        }
        
        while (j <= right) {
            temp[k++] = arr[j++];
        }
        
        for (k = 0; k < temp.length; k++) {
            arr[left + k] = temp[k];
        }
    }
}