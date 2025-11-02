package class132;

import java.util.*;

/**
 * LeetCode 327. 区间和的个数 (Count of Range Sum)
 * 
 * 题目描述：
 * 给定一个整数数组 nums，以及两个整数 lower 和 upper。
 * 返回区间和的值在区间 [lower, upper] 之间的区间个数（包含等于）。
 * 
 * 解题思路：
 * 使用树状数组（Fenwick Tree）+ 离散化来高效统计满足条件的区间和个数。
 * 核心思想：
 * 1. 计算前缀和数组 prefixSum
 * 2. 对于每个前缀和 prefixSum[j]，需要统计有多少个 i < j 满足：
 *    lower <= prefixSum[j] - prefixSum[i] <= upper
 *    即：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
 * 3. 使用树状数组维护前缀和的出现次数
 * 4. 通过离散化处理大数值范围问题
 * 
 * 时间复杂度分析：
 * - 前缀和计算：O(n)
 * - 离散化处理：O(n log n)
 * - 树状数组操作：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 前缀和数组：O(n)
 * - 离散化数组：O(n)
 * - 树状数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 边界条件处理：处理空数组、lower > upper 等情况
 * 2. 数值溢出处理：使用long类型避免整数溢出
 * 3. 离散化优化：使用TreeSet去重并排序
 * 4. 异常处理：检查输入参数合法性
 * 
 * 算法技巧：
 * - 离散化：将大范围的数值映射到小范围的索引
 * - 树状数组：高效统计前缀和的出现次数
 * - 容斥原理：通过区间查询统计满足条件的个数
 * 
 * 适用场景：
 * - 需要统计满足特定条件的区间和个数
 * - 数值范围较大，需要离散化处理
 * - 对时间复杂度要求较高的场景
 * 
 * 测试用例：
 * 输入：nums = [-2,5,-1], lower = -2, upper = 2
 * 输出：3
 * 解释：三个区间和满足条件：[0,0], [2,2], [0,2]
 */
public class Code22_CountOfRangeSum {
    
    /**
     * 计算满足条件的区间和个数
     * 
     * @param nums 输入数组
     * @param lower 区间下界
     * @param upper 区间上界
     * @return 满足条件的区间个数
     * 
     * 算法步骤：
     * 1. 计算前缀和数组
     * 2. 离散化处理所有可能的前缀和值
     * 3. 使用树状数组统计前缀和出现次数
     * 4. 遍历前缀和数组，统计满足条件的区间个数
     */
    public int countRangeSum(int[] nums, int lower, int upper) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        
        if (lower > upper) {
            return 0;
        }
        
        int n = nums.length;
        
        // 1. 计算前缀和数组（使用long避免溢出）
        long[] prefixSum = new long[n + 1];
        for (int i = 0; i < n; i++) {
            prefixSum[i + 1] = prefixSum[i] + nums[i];
        }
        
        // 2. 离散化处理：收集所有需要离散化的值
        TreeSet<Long> set = new TreeSet<>();
        for (long sum : prefixSum) {
            set.add(sum);
            set.add(sum - lower);
            set.add(sum - upper);
        }
        
        // 构建离散化映射
        Map<Long, Integer> map = new HashMap<>();
        int idx = 1;
        for (long num : set) {
            map.put(num, idx++);
        }
        
        // 3. 使用树状数组统计前缀和出现次数
        FenwickTree tree = new FenwickTree(map.size());
        int count = 0;
        
        // 从右向左遍历前缀和数组
        for (int i = 0; i <= n; i++) {
            long currentSum = prefixSum[i];
            long leftBound = currentSum - upper;
            long rightBound = currentSum - lower;
            
            // 查询满足条件的区间和个数
            int leftIdx = map.get(leftBound);
            int rightIdx = map.get(rightBound);
            
            count += tree.rangeQuery(leftIdx, rightIdx);
            
            // 更新当前前缀和的出现次数
            int currentIdx = map.get(currentSum);
            tree.update(currentIdx, 1);
        }
        
        return count;
    }
    
    /**
     * 树状数组（Fenwick Tree）实现
     * 用于高效统计前缀和的出现次数
     */
    private static class FenwickTree {
        private int[] tree;
        private int size;
        
        /**
         * 构造函数
         * 
         * @param size 树状数组大小
         */
        public FenwickTree(int size) {
            this.size = size;
            this.tree = new int[size + 1];
        }
        
        /**
         * 更新操作：在指定位置增加一个值
         * 
         * @param index 位置索引
         * @param delta 增加值
         */
        public void update(int index, int delta) {
            while (index <= size) {
                tree[index] += delta;
                index += lowbit(index);
            }
        }
        
        /**
         * 查询前缀和：从1到index的和
         * 
         * @param index 位置索引
         * @return 前缀和
         */
        public int query(int index) {
            int sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= lowbit(index);
            }
            return sum;
        }
        
        /**
         * 区间查询：从left到right的和
         * 
         * @param left 左边界
         * @param right 右边界
         * @return 区间和
         */
        public int rangeQuery(int left, int right) {
            if (left > right) return 0;
            return query(right) - query(left - 1);
        }
        
        /**
         * 计算lowbit（最低位的1）
         * 
         * @param x 输入数字
         * @return lowbit值
         */
        private int lowbit(int x) {
            return x & -x;
        }
    }
    
    /**
     * 测试方法：验证算法正确性
     * 
     * 测试用例设计：
     * 1. 正常情况测试
     * 2. 边界情况测试
     * 3. 空数组测试
     * 4. 大数值测试
     */
    public static void main(String[] args) {
        Code22_CountOfRangeSum solution = new Code22_CountOfRangeSum();
        
        // 测试用例1：正常情况
        int[] nums1 = {-2, 5, -1};
        int lower1 = -2, upper1 = 2;
        int result1 = solution.countRangeSum(nums1, lower1, upper1);
        System.out.println("测试用例1结果：" + result1 + " (期望：3)");
        
        // 测试用例2：边界情况
        int[] nums2 = {0};
        int lower2 = 0, upper2 = 0;
        int result2 = solution.countRangeSum(nums2, lower2, upper2);
        System.out.println("测试用例2结果：" + result2 + " (期望：1)");
        
        // 测试用例3：空数组
        int[] nums3 = {};
        int result3 = solution.countRangeSum(nums3, 0, 0);
        System.out.println("测试用例3结果：" + result3 + " (期望：0)");
        
        // 测试用例4：大数值
        int[] nums4 = {2147483647, -2147483648, -1, 0};
        int lower4 = -1, upper4 = 0;
        int result4 = solution.countRangeSum(nums4, lower4, upper4);
        System.out.println("测试用例4结果：" + result4 + " (期望：4)");
        
        System.out.println("所有测试用例执行完成！");
    }
}