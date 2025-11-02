package class117;

/**
 * CodeChef MSTICK - 区间最值查询 - Sparse Table应用
 * 题目链接：https://www.codechef.com/problems/MSTICK
 * 
 * 【题目描述】
 * 给定一个数组，多次查询区间内的最大值和最小值，然后计算它们的差值。
 * 这是一个经典的RMQ（Range Minimum/Maximum Query）问题。
 * 
 * 【示例】
 * 输入：
 * 5
 * 1 2 3 4 5
 * 3
 * 0 4
 * 1 3
 * 2 4
 * 
 * 输出：
 * 4
 * 2
 * 2
 * 
 * 【算法核心思想】
 * 使用Sparse Table同时预处理区间最大值和最小值，实现O(1)查询。
 * 对于每个查询，分别查询最大值和最小值，然后计算它们的差值。
 * 
 * 【核心原理】
 * 1. 构建两个Sparse Table：一个用于最大值，一个用于最小值
 * 2. 预处理log数组，避免重复计算
 * 3. 查询时使用两个重叠区间覆盖整个查询区间
 * 4. 计算最大值和最小值的差值作为结果
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n log n) - 构建两个ST表
 * - 查询：O(1) - 每次查询两次ST表查询
 * - 总时间复杂度：O(n log n + q)
 * 
 * 【空间复杂度分析】
 * - 最大值ST表：O(n log n)
 * - 最小值ST表：O(n log n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【应用场景】
 * 1. 数据统计分析中的极差计算
 * 2. 股票价格波动分析
 * 3. 传感器数据质量监控
 * 4. 图像处理中的对比度分析
 * 5. 网络流量峰值检测
 * 6. 温度变化范围监控
 * 7. 金融风险评估
 * 
 * 【工程化考量】
 * 1. 异常处理：处理无效查询范围
 * 2. 性能优化：预处理log值避免重复计算
 * 3. 内存管理：大数组时注意内存使用
 * 4. 可扩展性：支持动态数据更新（需要重新构建ST表）
 * 5. 测试覆盖：覆盖各种边界情况和特殊输入
 */
import java.util.*;

public class Code12_CodeChefMSTICK {
    
    /**
     * 区间最值查询解决方案类
     */
    static class RangeMinMaxQuery {
        private int[] arr;               // 原始数组
        private SparseTable maxSt;       // 最大值Sparse Table
        private SparseTable minSt;       // 最小值Sparse Table
        
        public RangeMinMaxQuery(int[] arr) {
            this.arr = arr;
            this.maxSt = new SparseTable(arr, true);  // 最大值查询
            this.minSt = new SparseTable(arr, false); // 最小值查询
        }
        
        /**
         * 查询区间[l, r]的最大值和最小值的差值
         */
        public int queryDifference(int l, int r) {
            if (l < 0 || r >= arr.length || l > r) {
                throw new IllegalArgumentException("Invalid query range: [" + l + ", " + r + "]");
            }
            
            int maxVal = maxSt.query(l, r);
            int minVal = minSt.query(l, r);
            
            return maxVal - minVal;
        }
        
        /**
         * 分别查询最大值和最小值
         */
        public int[] queryMinMax(int l, int r) {
            if (l < 0 || r >= arr.length || l > r) {
                throw new IllegalArgumentException("Invalid query range: [" + l + ", " + r + "]");
            }
            
            return new int[]{minSt.query(l, r), maxSt.query(l, r)};
        }
    }
    
    /**
     * 通用的Sparse Table实现类
     * 支持最大值和最小值查询
     */
    static class SparseTable {
        private int[][] st;             // ST表
        private int[] logTable;         // 预处理log2值
        private boolean isMaxQuery;     // true表示最大值查询，false表示最小值查询
        
        public SparseTable(int[] arr, boolean isMaxQuery) {
            this.isMaxQuery = isMaxQuery;
            int n = arr.length;
            if (n == 0) return;
            
            int maxLog = (int) (Math.log(n) / Math.log(2)) + 1;
            st = new int[n][maxLog];
            logTable = new int[n + 1];
            
            preprocessLog(n);
            
            // 初始化第一层
            for (int i = 0; i < n; i++) {
                st[i][0] = arr[i];
            }
            
            // 动态规划构建ST表
            for (int j = 1; (1 << j) <= n; j++) {
                for (int i = 0; i + (1 << j) - 1 < n; i++) {
                    if (isMaxQuery) {
                        st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                    } else {
                        st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                    }
                }
            }
        }
        
        /**
         * 预处理log2值
         */
        private void preprocessLog(int n) {
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 查询区间[l, r]的最值
         */
        public int query(int l, int r) {
            if (l > r) {
                throw new IllegalArgumentException("Invalid range: [" + l + ", " + r + "]");
            }
            
            int len = r - l + 1;
            int k = logTable[len];
            
            if (isMaxQuery) {
                return Math.max(st[l][k], st[r - (1 << k) + 1][k]);
            } else {
                return Math.min(st[l][k], st[r - (1 << k) + 1][k]);
            }
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：CodeChef示例
        int[] arr1 = {1, 2, 3, 4, 5};
        RangeMinMaxQuery solver1 = new RangeMinMaxQuery(arr1);
        
        System.out.println("测试用例1 - CodeChef示例:");
        System.out.println("查询[0,4]: " + solver1.queryDifference(0, 4) + " (期望: 4)");
        System.out.println("查询[1,3]: " + solver1.queryDifference(1, 3) + " (期望: 2)");
        System.out.println("查询[2,4]: " + solver1.queryDifference(2, 4) + " (期望: 2)");
        
        // 测试用例2：所有元素相同
        int[] arr2 = {5, 5, 5, 5, 5};
        RangeMinMaxQuery solver2 = new RangeMinMaxQuery(arr2);
        System.out.println("\n测试用例2 - 所有元素相同:");
        System.out.println("查询[0,4]: " + solver2.queryDifference(0, 4) + " (期望: 0)");
        
        // 测试用例3：递减序列
        int[] arr3 = {5, 4, 3, 2, 1};
        RangeMinMaxQuery solver3 = new RangeMinMaxQuery(arr3);
        System.out.println("\n测试用例3 - 递减序列:");
        System.out.println("查询[0,4]: " + solver3.queryDifference(0, 4) + " (期望: 4)");
        
        // 测试用例4：随机数组
        int[] arr4 = {3, 7, 1, 9, 4, 6, 2, 8, 5};
        RangeMinMaxQuery solver4 = new RangeMinMaxQuery(arr4);
        System.out.println("\n测试用例4 - 随机数组:");
        System.out.println("查询[0,8]: " + solver4.queryDifference(0, 8) + " (期望: 8)");
        System.out.println("查询[2,6]: " + solver4.queryDifference(2, 6) + " (期望: 8)");
        
        // 分别查询最小值和最大值
        int[] minMax = solver4.queryMinMax(2, 6);
        System.out.println("区间[2,6]的最小值: " + minMax[0] + ", 最大值: " + minMax[1]);
        
        // 性能测试
        int[] largeArr = new int[100000];
        Random random = new Random();
        for (int i = 0; i < largeArr.length; i++) {
            largeArr[i] = random.nextInt(1000000);
        }
        
        RangeMinMaxQuery largeSolver = new RangeMinMaxQuery(largeArr);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int l = random.nextInt(largeArr.length - 100);
            int r = l + random.nextInt(100);
            largeSolver.queryDifference(l, r);
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n性能测试: 1000次查询耗时 " + (endTime - startTime) + "ms");
        
        // 边界测试
        System.out.println("\n边界测试:");
        try {
            solver1.queryDifference(-1, 4);
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试1通过: " + e.getMessage());
        }
        
        try {
            solver1.queryDifference(3, 2);
        } catch (IllegalArgumentException e) {
            System.out.println("边界测试2通过: " + e.getMessage());
        }
        
        System.out.println("\n所有测试用例执行完成！");
    }
}