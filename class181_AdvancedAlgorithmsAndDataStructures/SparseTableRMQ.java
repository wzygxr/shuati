package class185.sparse_table_problems;

import java.util.*;

/**
 * 稀疏表（Sparse Table）实现 - RMQ问题解决方案 (Java版本)
 * 
 * 算法思路：
 * 稀疏表是一种用于解决范围查询问题的数据结构，特别适用于：
 * 1. 范围最小值查询（RMQ）
 * 2. 范围最大值查询
 * 3. 其他满足结合律和幂等性的操作
 * 
 * 稀疏表通过预处理，在O(n log n)时间内构建一个二维数组，
 * 然后在O(1)时间内回答任何范围查询。
 * 
 * 时间复杂度：
 * - 预处理：O(n log n)
 * - 查询：O(1)
 * 空间复杂度：O(n log n)
 * 
 * 应用场景：
 * 1. 数据库：范围查询优化
 * 2. 图像处理：区域统计信息计算
 * 3. 金融：时间序列分析中的极值查询
 * 4. 算法竞赛：优化动态规划中的范围查询
 * 
 * 相关题目：
 * 1. LeetCode 2444. 统计定界子数组的数目
 * 2. POJ 3264 Balanced Lineup
 * 3. SPOJ RMQSQ - Range Minimum Query
 */
public class SparseTableRMQ {
    private int[] data;
    private int[][] stMin;  // 用于范围最小值查询的稀疏表
    private int[][] stMax;  // 用于范围最大值查询的稀疏表
    private int[] logTable;
    private int n;
    
    /**
     * 构造函数
     * @param data 输入数组
     */
    public SparseTableRMQ(int[] data) {
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
        stMin = new int[k][n];
        stMax = new int[k][n];
        
        // 初始化k=0的情况（长度为1的区间）
        for (int i = 0; i < n; i++) {
            stMin[0][i] = data[i];
            stMax[0][i] = data[i];
        }
        
        // 动态规划构建其他k值
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                int prevLen = 1 << (j - 1);
                // 范围最小值查询
                stMin[j][i] = Math.min(stMin[j-1][i], stMin[j-1][i + prevLen]);
                // 范围最大值查询
                stMax[j][i] = Math.max(stMax[j-1][i], stMax[j-1][i + prevLen]);
            }
        }
    }
    
    /**
     * 范围最小值查询
     * 时间复杂度：O(1)
     * @param left 左边界（包含）
     * @param right 右边界（包含）
     * @return 区间内的最小值
     */
    public int queryMin(int left, int right) {
        if (left < 0 || right >= n || left > right) {
            throw new IllegalArgumentException("查询范围无效");
        }
        
        int length = right - left + 1;
        int k = logTable[length];
        
        return Math.min(stMin[k][left], stMin[k][right - (1 << k) + 1]);
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
    
    /**
     * 批量处理范围查询
     * @param queries 查询数组，每个查询是[left, right]形式的数组
     * @param isMinQuery 是否是最小值查询，否则是最大值查询
     * @return 查询结果数组
     */
    public int[] batchQuery(int[][] queries, boolean isMinQuery) {
        if (queries == null) {
            throw new IllegalArgumentException("查询数组不能为空");
        }
        
        int[] results = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int[] query = queries[i];
            if (query == null || query.length != 2) {
                throw new IllegalArgumentException("查询格式无效");
            }
            
            if (isMinQuery) {
                results[i] = queryMin(query[0], query[1]);
            } else {
                results[i] = queryMax(query[0], query[1]);
            }
        }
        
        return results;
    }
    
    /**
     * 测试稀疏表
     */
    public static void main(String[] args) {
        System.out.println("=== 测试稀疏表 ===");
        
        int[] data = {1, 3, 5, 7, 9, 11, 13, 15, 17};
        
        SparseTableRMQ st = new SparseTableRMQ(data);
        
        // 测试最小值查询
        System.out.println("测试最小值查询:");
        System.out.println("区间[1, 5]的最小值: " + st.queryMin(1, 5));  // 应该是3
        System.out.println("区间[0, 8]的最小值: " + st.queryMin(0, 8));  // 应该是1
        System.out.println("区间[4, 7]的最小值: " + st.queryMin(4, 7));  // 应该是9
        
        // 测试最大值查询
        System.out.println("\n测试最大值查询:");
        System.out.println("区间[1, 5]的最大值: " + st.queryMax(1, 5));  // 应该是11
        System.out.println("区间[0, 8]的最大值: " + st.queryMax(0, 8));  // 应该是17
        System.out.println("区间[4, 7]的最大值: " + st.queryMax(4, 7));  // 应该是15
        
        // 测试批量查询
        System.out.println("\n测试批量查询:");
        int[][] queries = {
            {0, 2}, {1, 5}, {3, 7}, {2, 8}
        };
        
        int[] minResults = st.batchQuery(queries, true);
        System.out.println("批量最小值查询结果: " + Arrays.toString(minResults));
        
        int[] maxResults = st.batchQuery(queries, false);
        System.out.println("批量最大值查询结果: " + Arrays.toString(maxResults));
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        Random random = new Random(42);
        
        // 生成大数据集
        int n = 100000;
        int[] largeData = new int[n];
        for (int i = 0; i < n; i++) {
            largeData[i] = random.nextInt(1000000) + 1;
        }
        
        long startTime = System.nanoTime();
        SparseTableRMQ largeST = new SparseTableRMQ(largeData);
        long buildTime = System.nanoTime() - startTime;
        
        // 执行大量查询
        int numQueries = 100000;
        int[][] largeQueries = new int[numQueries][2];
        for (int i = 0; i < numQueries; i++) {
            // 生成有效的查询范围
            int left = random.nextInt(n - 1);
            int right = left + random.nextInt(Math.min(1000, n - left - 1)) + 1;
            largeQueries[i][0] = left;
            largeQueries[i][1] = right;
        }
        
        startTime = System.nanoTime();
        for (int i = 0; i < numQueries; i++) {
            largeST.queryMin(largeQueries[i][0], largeQueries[i][1]);
        }
        long queryTime = System.nanoTime() - startTime;
        
        System.out.println("构建100000个元素的稀疏表时间: " + buildTime / 1_000_000.0 + " ms");
        System.out.println("执行100000次查询时间: " + queryTime / 1_000_000.0 + " ms");
        System.out.println("平均每次查询时间: " + queryTime / 1_000.0 / numQueries + " μs");
    }
}