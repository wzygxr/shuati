package class117;

/**
 * SPOJ FREQUENT - 区间频繁值查询 - Sparse Table应用
 * 题目链接：https://www.spoj.com/problems/FREQUENT/
 * 
 * 【题目描述】
 * 给定一个非降序数组，多次查询区间内出现次数最多的数的出现次数。
 * 由于数组是非降序的，相同的数字会连续出现，这大大简化了问题。
 * 
 * 【示例】
 * 输入：
 * 10 3
 * -1 -1 1 1 1 1 3 10 10 10
 * 0 1
 * 0 5
 * 5 9
 * 
 * 输出：
 * 1
 * 2
 * 4
 * 
 * 【算法核心思想】
 * 结合游程编码和Sparse Table解决区间频繁值查询问题。
 * 由于数组是非降序的，可以将连续的相同数字压缩为游程，然后使用Sparse Table查询游程长度的最大值。
 * 
 * 【核心原理】
 * 1. 游程编码：将连续的相同数字压缩为(值, 开始位置, 结束位置, 长度)
 * 2. 预处理：对于每个游程，记录其信息
 * 3. Sparse Table：预处理游程长度的最大值
 * 4. 查询处理：根据查询区间与游程的关系，分三种情况处理
 * 
 * 【时间复杂度分析】
 * - 预处理：O(n) - 游程编码 + O(m log m) - Sparse Table构建（m为游程数量）
 * - 查询：O(1) - 每次查询最多3次ST表查询
 * - 总时间复杂度：O(n + m log m + q)
 * 
 * 【空间复杂度分析】
 * - 游程数组：O(n)
 * - Sparse Table：O(m log m)
 * - 总空间复杂度：O(n + m log m)
 * 
 * 【应用场景】
 * 1. 数据压缩中的频率统计
 * 2. 时间序列数据分析
 * 3. 日志分析中的模式识别
 * 4. 基因组序列分析
 * 5. 图像处理中的连续区域检测
 * 6. 网络流量分析
 * 7. 传感器数据异常检测
 * 
 * 【工程化考量】
 * 1. 异常处理：处理空数组、无效查询等边界情况
 * 2. 性能优化：对于小规模数据可以使用更简单的方法
 * 3. 内存管理：大数组时注意内存使用
 * 4. 可扩展性：支持动态数据更新（需要重新构建ST表）
 * 5. 测试覆盖：覆盖各种边界情况和特殊输入
 */
import java.util.*;

public class Code11_SPOJFREQUENT {
    
    /**
     * 游程编码类
     */
    static class Run {
        int value;      // 游程的值
        int start;      // 游程开始位置
        int end;        // 游程结束位置
        int length;     // 游程长度
        
        Run(int value, int start, int end) {
            this.value = value;
            this.start = start;
            this.end = end;
            this.length = end - start + 1;
        }
        
        @Override
        public String toString() {
            return "Run{value=" + value + ", start=" + start + ", end=" + end + ", length=" + length + "}";
        }
    }
    
    /**
     * 频繁值查询解决方案
     */
    static class FrequentQuerySolver {
        private int[] arr;           // 原始数组
        private List<Run> runs;      // 游程列表
        private SparseTable st;     // Sparse Table用于查询游程长度最大值
        private int[] runIndex;      // 每个位置对应的游程索引
        
        public FrequentQuerySolver(int[] arr) {
            this.arr = arr;
            this.runs = new ArrayList<>();
            this.runIndex = new int[arr.length];
            
            // 执行游程编码
            runLengthEncoding();
            
            // 构建Sparse Table
            buildSparseTable();
        }
        
        /**
         * 游程编码：将连续的相同数字压缩为游程
         */
        private void runLengthEncoding() {
            if (arr.length == 0) return;
            
            int currentValue = arr[0];
            int start = 0;
            
            for (int i = 1; i < arr.length; i++) {
                if (arr[i] != currentValue) {
                    // 结束当前游程
                    runs.add(new Run(currentValue, start, i - 1));
                    // 填充runIndex
                    for (int j = start; j < i; j++) {
                        runIndex[j] = runs.size() - 1;
                    }
                    // 开始新游程
                    currentValue = arr[i];
                    start = i;
                }
            }
            
            // 处理最后一个游程
            runs.add(new Run(currentValue, start, arr.length - 1));
            for (int j = start; j < arr.length; j++) {
                runIndex[j] = runs.size() - 1;
            }
        }
        
        /**
         * 构建Sparse Table用于查询游程长度最大值
         */
        private void buildSparseTable() {
            int m = runs.size();
            int[] lengths = new int[m];
            for (int i = 0; i < m; i++) {
                lengths[i] = runs.get(i).length;
            }
            st = new SparseTable(lengths);
        }
        
        /**
         * 查询区间[l, r]内出现次数最多的数的出现次数
         */
        public int query(int l, int r) {
            if (l > r || l < 0 || r >= arr.length) {
                throw new IllegalArgumentException("Invalid query range: [" + l + ", " + r + "]");
            }
            
            int leftRunIndex = runIndex[l];
            int rightRunIndex = runIndex[r];
            
            // 情况1：查询区间完全在一个游程内
            if (leftRunIndex == rightRunIndex) {
                return r - l + 1;
            }
            
            // 情况2：查询区间跨越多个游程
            int maxFreq = 0;
            
            // 处理左边界游程
            Run leftRun = runs.get(leftRunIndex);
            maxFreq = Math.max(maxFreq, leftRun.end - l + 1);
            
            // 处理右边界游程
            Run rightRun = runs.get(rightRunIndex);
            maxFreq = Math.max(maxFreq, r - rightRun.start + 1);
            
            // 处理中间游程（如果存在）
            if (rightRunIndex - leftRunIndex > 1) {
                maxFreq = Math.max(maxFreq, st.query(leftRunIndex + 1, rightRunIndex - 1));
            }
            
            return maxFreq;
        }
    }
    
    /**
     * Sparse Table实现类（最大值查询）
     */
    static class SparseTable {
        private int[][] st;
        private int[] logTable;
        
        public SparseTable(int[] arr) {
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
                    st[i][j] = Math.max(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
                }
            }
        }
        
        private void preprocessLog(int n) {
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        public int query(int l, int r) {
            if (l > r) return 0;
            int len = r - l + 1;
            int k = logTable[len];
            return Math.max(st[l][k], st[r - (1 << k) + 1][k]);
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        // 测试用例1：SPOJ示例
        int[] arr1 = {-1, -1, 1, 1, 1, 1, 3, 10, 10, 10};
        FrequentQuerySolver solver1 = new FrequentQuerySolver(arr1);
        
        System.out.println("测试用例1 - SPOJ示例:");
        System.out.println("查询[0,1]: " + solver1.query(0, 1) + " (期望: 1)");
        System.out.println("查询[0,5]: " + solver1.query(0, 5) + " (期望: 2)");
        System.out.println("查询[5,9]: " + solver1.query(5, 9) + " (期望: 4)");
        
        // 测试用例2：所有元素相同
        int[] arr2 = {5, 5, 5, 5, 5};
        FrequentQuerySolver solver2 = new FrequentQuerySolver(arr2);
        System.out.println("\n测试用例2 - 所有元素相同:");
        System.out.println("查询[0,4]: " + solver2.query(0, 4) + " (期望: 5)");
        
        // 测试用例3：每个元素都不同
        int[] arr3 = {1, 2, 3, 4, 5};
        FrequentQuerySolver solver3 = new FrequentQuerySolver(arr3);
        System.out.println("\n测试用例3 - 每个元素都不同:");
        System.out.println("查询[0,4]: " + solver3.query(0, 4) + " (期望: 1)");
        
        // 测试用例4：混合情况
        int[] arr4 = {1, 1, 2, 2, 2, 3, 3, 3, 3, 4};
        FrequentQuerySolver solver4 = new FrequentQuerySolver(arr4);
        System.out.println("\n测试用例4 - 混合情况:");
        System.out.println("查询[0,9]: " + solver4.query(0, 9) + " (期望: 4)");
        System.out.println("查询[2,6]: " + solver4.query(2, 6) + " (期望: 3)");
        
        // 性能测试
        int[] largeArr = new int[100000];
        Random random = new Random();
        int current = 0;
        for (int i = 0; i < largeArr.length; i++) {
            if (random.nextDouble() < 0.1) { // 10%概率改变值
                current = random.nextInt(100);
            }
            largeArr[i] = current;
        }
        
        FrequentQuerySolver largeSolver = new FrequentQuerySolver(largeArr);
        
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            int l = random.nextInt(largeArr.length - 100);
            int r = l + random.nextInt(100);
            largeSolver.query(l, r);
        }
        long endTime = System.currentTimeMillis();
        
        System.out.println("\n性能测试: 1000次查询耗时 " + (endTime - startTime) + "ms");
        
        System.out.println("\n所有测试用例执行完成！");
    }
}