package class109;

import java.util.Arrays;

/**
 * 最长递增子序列
 * 给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
 * 子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
 * 
 * 示例 1：
 * 输入：nums = [10,9,2,5,3,7,101,18]
 * 输出：4
 * 解释：最长递增子序列是 [2,3,7,101]，因此长度为 4 。
 * 
 * 示例 2：
 * 输入：nums = [0,1,0,3,2,3]
 * 输出：4
 * 
 * 示例 3：
 * 输入：nums = [7,7,7,7,7,7,7]
 * 输出：1
 * 
 * 提示：
 * 1 <= nums.length <= 2500
 * -10^4 <= nums[i] <= 10^4
 * 
 * 进阶：
 * 你能将算法的时间复杂度降低到 O(n log(n)) 吗?
 * 
 * 解题思路：
 * 1. 使用树状数组优化动态规划解法
 * 2. 离散化处理数值，将数值映射到1~m的范围内
 * 3. 树状数组维护以数值i结尾的最长递增子序列的长度
 * 4. 遍历数组，对每个元素：
 *    - 查询小于当前元素的数值中，最长递增子序列的长度
 *    - 更新当前元素对应的树状数组
 * 
 * 时间复杂度分析：
 * - 离散化排序：O(n log n)
 * - 遍历数组，每次操作树状数组：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 需要额外数组存储原始数据、排序数据和树状数组：O(n)
 * - 所以总空间复杂度为O(n)
 * 
 * 测试链接: https://leetcode.cn/problems/longest-increasing-subsequence/
 */
public class Code07_LIS_BIT {
    
    // 最大数组长度
    public static int MAXN = 2501;
    
    // 排序数组，用于离散化
    public static int[] sort = new int[MAXN];
    
    // 树状数组，维护以数值i结尾的最长递增子序列的长度
    public static int[] tree = new int[MAXN];
    
    // 离散化后数组长度
    public static int m;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 查询结尾数值<=i的最长递增子序列的长度
     * 
     * @param i 查询的结束位置
     * @return 最长递增子序列的长度
     */
    public static int query(int i) {
        int maxLen = 0;
        while (i > 0) {
            maxLen = Math.max(maxLen, tree[i]);
            i -= lowbit(i);
        }
        return maxLen;
    }
    
    /**
     * 更新以数值i结尾的最长递增子序列的长度
     * 
     * @param i 数值
     * @param len 最长递增子序列长度
     */
    public static void add(int i, int len) {
        while (i <= m) {
            tree[i] = Math.max(tree[i], len);
            i += lowbit(i);
        }
    }
    
    /**
     * 计算最长递增子序列的长度
     * 
     * @param nums 输入数组
     * @return 最长递增子序列的长度
     */
    public static int lengthOfLIS(int[] nums) {
        int n = nums.length;
        // 离散化处理
        for (int i = 1; i <= n; i++) {
            sort[i] = nums[i - 1];
        }
        Arrays.sort(sort, 1, n + 1);
        m = 1;
        for (int i = 2; i <= n; i++) {
            // 去重
            if (sort[m] != sort[i]) {
                sort[++m] = sort[i];
            }
        }
        
        // 将原数组元素替换为离散化后的排名
        Arrays.fill(tree, 1, m + 1, 0);
        int maxLen = 0;
        for (int num : nums) {
            int i = rank(num);
            // 查询以数值<i结尾的最长递增子序列的长度
            int curLen = query(i - 1) + 1;
            maxLen = Math.max(maxLen, curLen);
            // 更新以数值i结尾的最长递增子序列的长度
            add(i, curLen);
        }
        return maxLen;
    }
    
    /**
     * 给定原始值v，返回其在离散化数组中的排名（即在排序数组中的位置）
     * 
     * @param v 原始值
     * @return 排名值(排序部分1~m中的下标)
     */
    public static int rank(int v) {
        int l = 1, r = m, mid;
        int ans = 0;
        while (l <= r) {
            mid = (l + r) / 2;
            if (sort[mid] >= v) {
                ans = mid;
                r = mid - 1;
            } else {
                l = mid + 1;
            }
        }
        return ans;
    }
    
    // ==================== 补充题目：LeetCode 307. 区域和检索 - 数组可修改 ====================
    /**
     * LeetCode 307. 区域和检索 - 数组可修改
     * 链接：https://leetcode.cn/problems/range-sum-query-mutable/
     * 题目：给你一个数组 nums ，请你完成两类查询。
     * 1. 其中一类查询要求 更新 数组 nums 下标对应的值
     * 2. 另一类查询要求返回数组 nums 中索引 left 和索引 right 之间（包含）的 nums 元素的 和 ，其中 left <= right
     * 
     * 解题思路：
     * 使用树状数组实现单点更新和区间查询
     */
    static class NumArray {
        private FenwickTree bit;
        private int[] nums;
        
        public NumArray(int[] nums) {
            this.nums = nums;
            bit = new FenwickTree(nums.length);
            // 初始化树状数组
            for (int i = 0; i < nums.length; i++) {
                bit.update(i + 1, nums[i]); // 树状数组从1开始
            }
        }
        
        public void update(int index, int val) {
            // 计算差值
            int delta = val - nums[index];
            nums[index] = val;
            // 更新树状数组
            bit.update(index + 1, delta);
        }
        
        public int sumRange(int left, int right) {
            // 区间和 = 前缀和(right+1) - 前缀和(left)
            return bit.query(right + 1) - bit.query(left);
        }
        
        // 树状数组实现
        class FenwickTree {
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
    }
    
    // ==================== 补充题目：LeetCode 673. 最长递增子序列的个数 ====================
    /**
     * LeetCode 673. 最长递增子序列的个数
     * 链接：https://leetcode.cn/problems/number-of-longest-increasing-subsequence/
     * 题目：给定一个未排序的整数数组，找到最长递增子序列的个数。
     * 
     * 解题思路：
     * 使用两个树状数组：一个维护长度，一个维护路径数
     */
    public static int findNumberOfLIS(int[] nums) {
        int n = nums.length;
        if (n == 0) {
            return 0;
        }
        
        // 离散化处理
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);
        // 去重
        int m = 1;
        for (int i = 1; i < n; i++) {
            if (sorted[i] != sorted[i-1]) {
                sorted[m++] = sorted[i];
            }
        }
        
        // 离散化映射
        for (int i = 0; i < n; i++) {
            nums[i] = binarySearch(sorted, 0, m-1, nums[i]) + 1;
        }
        
        // 使用两个树状数组
        FenwickTreeForLength lenTree = new FenwickTreeForLength(m);
        FenwickTreeForCount cntTree = new FenwickTreeForCount(m);
        
        for (int num : nums) {
            // 查询比当前数小的最大长度
            int maxLen = lenTree.query(num - 1);
            // 查询对应的路径数
            int count = maxLen > 0 ? cntTree.query(num - 1) : 1;
            
            // 更新长度和路径数
            if (maxLen + 1 > lenTree.query(num)) {
                lenTree.update(num, maxLen + 1);
                cntTree.update(num, count);
            } else if (maxLen + 1 == lenTree.query(num)) {
                cntTree.update(num, cntTree.query(num) + count);
            }
        }
        
        // 获取最大长度
        int maxLength = lenTree.query(m);
        // 统计所有达到最大长度的路径数
        int result = 0;
        for (int i = 1; i <= m; i++) {
            if (lenTree.query(i) == maxLength) {
                result += cntTree.query(i);
            }
        }
        
        return result;
    }
    
    // 用于维护最长长度的树状数组
    static class FenwickTreeForLength {
        private int[] tree;
        private int n;
        
        public FenwickTreeForLength(int size) {
            this.n = size;
            this.tree = new int[size + 1];
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int index, int value) {
            while (index <= n) {
                tree[index] = Math.max(tree[index], value);
                index += lowbit(index);
            }
        }
        
        public int query(int index) {
            int max = 0;
            while (index > 0) {
                max = Math.max(max, tree[index]);
                index -= lowbit(index);
            }
            return max;
        }
    }
    
    // 用于维护路径数的树状数组
    static class FenwickTreeForCount {
        private int[] tree;
        private int n;
        
        public FenwickTreeForCount(int size) {
            this.n = size;
            this.tree = new int[size + 1];
        }
        
        private int lowbit(int x) {
            return x & (-x);
        }
        
        public void update(int index, int value) {
            while (index <= n) {
                tree[index] += value;
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
    
    // 二分查找辅助方法
    private static int binarySearch(int[] arr, int l, int r, int target) {
        while (l <= r) {
            int mid = l + (r - l) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                l = mid + 1;
            } else {
                r = mid - 1;
            }
        }
        return -1;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        int[] nums1 = {10, 9, 2, 5, 3, 7, 101, 18};
        System.out.println("输入: [10,9,2,5,3,7,101,18]");
        System.out.println("输出: " + lengthOfLIS(nums1));
        System.out.println("期望: 4\n");
        
        // 测试用例2
        int[] nums2 = {0, 1, 0, 3, 2, 3};
        System.out.println("输入: [0,1,0,3,2,3]");
        System.out.println("输出: " + lengthOfLIS(nums2));
        System.out.println("期望: 4\n");
        
        // 测试用例3
        int[] nums3 = {7, 7, 7, 7, 7, 7, 7};
        System.out.println("输入: [7,7,7,7,7,7,7]");
        System.out.println("输出: " + lengthOfLIS(nums3));
        System.out.println("期望: 1");
    }
}