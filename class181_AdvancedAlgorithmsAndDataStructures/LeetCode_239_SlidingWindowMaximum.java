package class008_AdvancedAlgorithmsAndDataStructures.sparse_table_problems;

import java.util.*;

/**
 * LeetCode 239. 滑动窗口最大值 (Sliding Window Maximum)
 * 
 * 题目来源：https://leetcode.cn/problems/sliding-window-maximum/
 * 
 * 题目描述：
 * 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 稀疏表：预处理范围最大值查询
 * 2. 双端队列：维护单调递减队列
 * 3. 分块：将数组分块预处理
 * 
 * 使用稀疏表的方法：
 * 1. 预处理稀疏表用于范围最大值查询
 * 2. 对于每个滑动窗口，使用稀疏表查询最大值
 * 
 * 时间复杂度：
 * - 稀疏表预处理：O(n log n)
 * - 查询：O(1)
 * - 总时间复杂度：O(n log n)
 * - 双端队列：O(n)
 * - 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 信号处理：滑动窗口数据分析
 * 2. 金融：股票价格极值分析
 * 3. 图像处理：区域特征提取
 * 
 * 相关题目：
 * 1. LeetCode 2444. 统计定界子数组的数目
 * 2. LeetCode 315. 计算右侧小于当前元素的个数
 * 3. LeetCode 1584. 连接所有点的最小费用
 */
public class LeetCode_239_SlidingWindowMaximum {
    
    /**
     * 稀疏表类，用于范围最大值查询
     */
    static class SparseTable {
        private int[] data;
        private int[][] stMax;  // 用于范围最大值查询的稀疏表
        private int[] logTable;
        private int n;
        
        /**
         * 构造函数
         * @param data 输入数组
         */
        public SparseTable(int[] data) {
            if (data == null || data.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            this.data = data.clone();
            this.n = data.length;
            
            // 预计算log表
            precomputeLogTable();
            
            // 构建稀疏表
            buildSparseTable();
        }
        
        /**
         * 预计算log2值表
         */
        private void precomputeLogTable() {
            logTable = new int[n + 1];
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 构建稀疏表
         */
        private void buildSparseTable() {
            int k = logTable[n] + 1;
            
            // 初始化稀疏表
            stMax = new int[k][n];
            
            // 初始化k=0的情况（长度为1的区间）
            for (int i = 0; i < n; i++) {
                stMax[0][i] = data[i];
            }
            
            // 动态规划构建其他k值
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) <= n; i++) {
                    int prevLen = 1 << (j - 1);
                    // 范围最大值查询
                    stMax[j][i] = Math.max(stMax[j-1][i], stMax[j-1][i + prevLen]);
                }
            }
        }
        
        /**
         * 范围最大值查询
         * 时间复杂度：O(1)
         * @param left 左边界（包含）
         * @param right 右边界（包含）
         * @return 区间内的最大值
         */
        public int queryMax(int left, int right) {
            if (left < 0 || right >= n || left > right) {
                throw new IllegalArgumentException("查询范围无效");
            }
            
            int length = right - left + 1;
            int k = logTable[length];
            
            return Math.max(stMax[k][left], stMax[k][right - (1 << k) + 1]);
        }
    }
    
    /**
     * 方法1：使用稀疏表的解法
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n log n)
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 滑动窗口最大值数组
     */
    public static int[] maxSlidingWindowSparseTable(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) return new int[0];
        if (k == 1) return nums.clone();
        
        // 构建稀疏表
        SparseTable st = new SparseTable(nums);
        
        // 计算结果数组大小
        int resultSize = n - k + 1;
        int[] result = new int[resultSize];
        
        // 对于每个滑动窗口，查询最大值
        for (int i = 0; i < resultSize; i++) {
            result[i] = st.queryMax(i, i + k - 1);
        }
        
        return result;
    }
    
    /**
     * 方法2：双端队列解法（最优解）
     * 时间复杂度：O(n)
     * 空间复杂度：O(k)
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 滑动窗口最大值数组
     */
    public static int[] maxSlidingWindowDeque(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) return new int[0];
        if (k == 1) return nums.clone();
        
        // 双端队列，存储数组索引
        Deque<Integer> deque = new LinkedList<>();
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i < n; i++) {
            // 移除队列中超出窗口范围的索引
            while (!deque.isEmpty() && deque.peekFirst() < i - k + 1) {
                deque.pollFirst();
            }
            
            // 移除队列中所有小于当前元素的索引（维护单调递减）
            while (!deque.isEmpty() && nums[deque.peekLast()] < nums[i]) {
                deque.pollLast();
            }
            
            // 添加当前索引
            deque.offerLast(i);
            
            // 如果窗口已形成，记录最大值
            if (i >= k - 1) {
                result[i - k + 1] = nums[deque.peekFirst()];
            }
        }
        
        return result;
    }
    
    /**
     * 方法3：分块预处理解法
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 滑动窗口最大值数组
     */
    public static int[] maxSlidingWindowBlock(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) return new int[0];
        if (k == 1) return nums.clone();
        
        // 分块大小
        int blockSize = k;
        int blocks = (n + blockSize - 1) / blockSize;
        
        // 预处理前缀最大值和后缀最大值
        int[] prefixMax = new int[n];
        int[] suffixMax = new int[n];
        
        // 计算前缀最大值
        for (int i = 0; i < n; i++) {
            if (i % blockSize == 0) {
                prefixMax[i] = nums[i];
            } else {
                prefixMax[i] = Math.max(prefixMax[i - 1], nums[i]);
            }
        }
        
        // 计算后缀最大值
        for (int i = n - 1; i >= 0; i--) {
            if (i == n - 1 || (i + 1) % blockSize == 0) {
                suffixMax[i] = nums[i];
            } else {
                suffixMax[i] = Math.max(suffixMax[i + 1], nums[i]);
            }
        }
        
        // 计算结果
        int[] result = new int[n - k + 1];
        for (int i = 0; i <= n - k; i++) {
            int blockStart = i / blockSize;
            int blockEnd = (i + k - 1) / blockSize;
            
            if (blockStart == blockEnd) {
                // 窗口在一个块内
                result[i] = suffixMax[i];
            } else {
                // 窗口跨越多个块
                int max1 = suffixMax[i]; // 块内前缀最大值
                int max2 = prefixMax[i + k - 1]; // 块内后缀最大值
                result[i] = Math.max(max1, max2);
            }
        }
        
        return result;
    }
    
    /**
     * 方法4：暴力解法（用于对比）
     * 时间复杂度：O(nk)
     * 空间复杂度：O(1)
     * @param nums 输入数组
     * @param k 窗口大小
     * @return 滑动窗口最大值数组
     */
    public static int[] maxSlidingWindowBruteForce(int[] nums, int k) {
        int n = nums.length;
        if (n == 0 || k == 0) return new int[0];
        
        int[] result = new int[n - k + 1];
        
        for (int i = 0; i <= n - k; i++) {
            int max = nums[i];
            for (int j = 1; j < k; j++) {
                max = Math.max(max, nums[i + j]);
            }
            result[i] = max;
        }
        
        return result;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 239. 滑动窗口最大值 ===");
        
        // 测试用例1
        int[] nums1 = {1,3,-1,-3,5,3,6,7};
        int k1 = 3;
        System.out.println("测试用例1:");
        System.out.println("数组: [1,3,-1,-3,5,3,6,7], k: " + k1);
        System.out.println("稀疏表解法结果: " + Arrays.toString(maxSlidingWindowSparseTable(nums1, k1)));
        System.out.println("双端队列解法结果: " + Arrays.toString(maxSlidingWindowDeque(nums1, k1)));
        System.out.println("分块解法结果: " + Arrays.toString(maxSlidingWindowBlock(nums1, k1)));
        System.out.println("暴力解法结果: " + Arrays.toString(maxSlidingWindowBruteForce(nums1, k1)));
        System.out.println("期望结果: [3,3,5,5,6,7]");
        System.out.println();
        
        // 测试用例2
        int[] nums2 = {1};
        int k2 = 1;
        System.out.println("测试用例2:");
        System.out.println("数组: [1], k: " + k2);
        System.out.println("稀疏表解法结果: " + Arrays.toString(maxSlidingWindowSparseTable(nums2, k2)));
        System.out.println("双端队列解法结果: " + Arrays.toString(maxSlidingWindowDeque(nums2, k2)));
        System.out.println("分块解法结果: " + Arrays.toString(maxSlidingWindowBlock(nums2, k2)));
        System.out.println("暴力解法结果: " + Arrays.toString(maxSlidingWindowBruteForce(nums2, k2)));
        System.out.println("期望结果: [1]");
        System.out.println();
        
        // 测试用例3
        int[] nums3 = {1,-1};
        int k3 = 1;
        System.out.println("测试用例3:");
        System.out.println("数组: [1,-1], k: " + k3);
        System.out.println("稀疏表解法结果: " + Arrays.toString(maxSlidingWindowSparseTable(nums3, k3)));
        System.out.println("双端队列解法结果: " + Arrays.toString(maxSlidingWindowDeque(nums3, k3)));
        System.out.println("分块解法结果: " + Arrays.toString(maxSlidingWindowBlock(nums3, k3)));
        System.out.println("暴力解法结果: " + Arrays.toString(maxSlidingWindowBruteForce(nums3, k3)));
        System.out.println("期望结果: [1,-1]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int arraySize = 100000;
        int[] nums = new int[arraySize];
        for (int i = 0; i < arraySize; i++) {
            nums[i] = random.nextInt(10000) - 5000; // 范围[-5000, 4999]
        }
        int k = 1000;
        
        long startTime = System.nanoTime();
        int[] result1 = maxSlidingWindowSparseTable(nums, k);
        long endTime = System.nanoTime();
        System.out.println("稀疏表解法处理" + arraySize + "个元素,k=" + k + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        int[] result2 = maxSlidingWindowDeque(nums, k);
        endTime = System.nanoTime();
        System.out.println("双端队列解法处理" + arraySize + "个元素,k=" + k + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        int[] result3 = maxSlidingWindowBlock(nums, k);
        endTime = System.nanoTime();
        System.out.println("分块解法处理" + arraySize + "个元素,k=" + k + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        startTime = System.nanoTime();
        int[] result4 = maxSlidingWindowBruteForce(nums, k);
        endTime = System.nanoTime();
        System.out.println("暴力解法处理" + arraySize + "个元素,k=" + k + "时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 验证结果一致性
        System.out.println("结果一致性检查: " + 
            (Arrays.equals(result1, result2) && Arrays.equals(result2, result3) && Arrays.equals(result3, result4) ? 
            "通过" : "失败"));
    }
}