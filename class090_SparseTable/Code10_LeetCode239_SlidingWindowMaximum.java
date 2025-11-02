package class117;

/**
 * LeetCode 239. 滑动窗口最大值 - Sparse Table应用
 * 题目链接：https://leetcode.com/problems/sliding-window-maximum/
 * 
 * 【题目描述】
 * 给定一个整数数组nums和一个整数k，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。
 * 你只可以看到在滑动窗口内的k个数字。滑动窗口每次只向右移动一位。
 * 返回滑动窗口中的最大值。
 * 
 * 【示例】
 * 输入：nums = [1,3,-1,-3,5,3,6,7], k = 3
 * 输出：[3,3,5,5,6,7]
 * 
 * 【算法核心思想】
 * 使用Sparse Table预处理区间最大值，然后对每个滑动窗口进行O(1)查询。
 * 这种方法特别适合k值较大且需要高效查询的场景。
 * 
 * 【核心原理】
 * 1. 预处理：构建Sparse Table存储所有长度为2的幂次的区间最大值
 * 2. 查询：对于每个滑动窗口[l, r]，使用ST表在O(1)时间内查询最大值
 * 3. 滑动窗口：窗口从左到右滑动，每次移动一个位置
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n log n) - 构建Sparse Table
 * - 查询：O(n) - 每个窗口一次O(1)查询，共n-k+1个窗口
 * - 总时间复杂度：O(n log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table：O(n log n)
 * - 结果数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【应用场景】
 * 1. 实时数据流中的滑动窗口统计
 * 2. 股票价格分析中的移动窗口最大值
 * 3. 网络流量监控中的峰值检测
 * 4. 图像处理中的滑动窗口滤波
 * 5. 时间序列数据分析
 * 6. 传感器数据实时处理
 * 7. 金融风险监控系统
 * 
 * 【工程化考量】
 * 1. 异常处理：处理k>n或k<=0的边界情况
 * 2. 性能优化：对于小k值，可以使用双端队列更高效
 * 3. 内存管理：大数组时注意内存使用
 * 4. 可扩展性：封装为可复用的滑动窗口统计组件
 * 
 * 【测试用例设计】
 * 1. 常规测试：正常数组和k值
 * 2. 边界测试：k=1, k=n, k>n
 * 3. 极端测试：大数组，重复元素
 * 4. 性能测试：n=10^5级别的数据规模
 */
import java.util.*;

public class Code10_LeetCode239_SlidingWindowMaximum {
    
    /**
     * 使用Sparse Table解决滑动窗口最大值问题
     * @param nums 输入数组
     * @param k 滑动窗口大小
     * @return 每个滑动窗口的最大值数组
     */
    public int[] maxSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0 || k <= 0) {
            return new int[0];
        }
        
        int n = nums.length;
        if (k > n) {
            k = n; // 处理k大于n的情况
        }
        
        // 结果数组大小为n-k+1
        int[] result = new int[n - k + 1];
        
        // 构建Sparse Table
        SparseTable st = new SparseTable(nums);
        
        // 对每个滑动窗口查询最大值
        for (int i = 0; i <= n - k; i++) {
            result[i] = st.query(i, i + k - 1);
        }
        
        return result;
    }
    
    /**
     * Sparse Table实现类
     * 支持区间最大值查询
     */
    static class SparseTable {
        private int[][] st; // ST表，st[i][j]表示从i开始长度为2^j的区间最大值
        private int[] logTable; // 预处理log2值，避免重复计算
        
        public SparseTable(int[] arr) {
            int n = arr.length;
            // 计算最大层数
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            st = new int[n][maxLog];
            logTable = new int[n + 1];
            
            // 预处理log2值
            preprocessLog(n);
            
            // 初始化第一层（长度为1的区间）
            for (int i = 0; i < n; i++) {
                st[i][0] = arr[i];
            }
            
            // 动态规划构建ST表
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) - 1 < n; i++) {
                    st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
        
        /**
         * 预处理log2值，用于快速计算区间长度对应的幂次
         */
        private void preprocessLog(int n) {
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 查询区间[l, r]的最大值
         * @param l 区间左端点（包含）
         * @param r 区间右端点（包含）
         * @return 区间最大值
         */
        public int query(int l, int r) {
            if (l > r) {
                throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
            }
            
            int len = r - l + 1;
            int k = logTable[len];
            
            // 使用两个重叠区间覆盖整个查询区间
            return Math.max(st[l][k], st[r - (1 << k) + 1][k]);
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        Code10_LeetCode239_SlidingWindowMaximum solution = new Code10_LeetCode239_SlidingWindowMaximum();
        
        // 测试用例1：常规测试
        int[] nums1 = {1, 3, -1, -3, 5, 3, 6, 7};
        int k1 = 3;
        int[] result1 = solution.maxSlidingWindow(nums1, k1);
        System.out.println("测试用例1结果: " + Arrays.toString(result1));
        System.out.println("期望结果: [3, 3, 5, 5, 6, 7]");
        
        // 测试用例2：边界测试 - k=1
        int[] nums2 = {1, 2, 3, 4, 5};
        int k2 = 1;
        int[] result2 = solution.maxSlidingWindow(nums2, k2);
        System.out.println("测试用例2结果: " + Arrays.toString(result2));
        
        // 测试用例3：边界测试 - k等于数组长度
        int[] nums3 = {5, 4, 3, 2, 1};
        int k3 = 5;
        int[] result3 = solution.maxSlidingWindow(nums3, k3);
        System.out.println("测试用例3结果: " + Arrays.toString(result3));
        
        // 测试用例4：极端测试 - 大数组
        int[] nums4 = new int[1000];
        Arrays.fill(nums4, 1);
        int k4 = 100;
        int[] result4 = solution.maxSlidingWindow(nums4, k4);
        System.out.println("测试用例4结果长度: " + result4.length);
        
        // 性能测试
        long startTime = System.currentTimeMillis();
        int[] largeNums = new int[100000];
        Arrays.fill(largeNums, 1);
        int[] largeResult = solution.maxSlidingWindow(largeNums, 1000);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试耗时: " + (endTime - startTime) + "ms");
        
        System.out.println("所有测试用例执行完成！");
    }
}