package class184;

import java.util.*;
import java.io.*;

/**
 * SPOJ - RMQSQ - Range Minimum Query 解决方案
 * 
 * 题目链接: https://www.spoj.com/problems/RMQSQ/
 * 题目描述: 经典的范围最小值查询问题
 * 解题思路: 使用稀疏表实现O(1)查询
 * 
 * 时间复杂度: 
 * - 预处理: O(n log n)
 * - 查询: O(1)
 * 空间复杂度: O(n log n)
 */
public class SPOJ_RMQSQ {
    
    /**
     * 稀疏表实现类
     */
    static class SparseTable {
        private int[][] st;      // 稀疏表数组
        private int[] logTable;  // 预计算的log值表
        private int[] data;      // 原始数据
        
        /**
         * 构造函数
         * @param data 输入数组
         */
        public SparseTable(int[] data) {
            if (data == null || data.length == 0) {
                throw new IllegalArgumentException("输入数组不能为空");
            }
            
            this.data = data;
            int n = data.length;
            
            // 计算log表
            precomputeLogTable(n);
            
            // 计算稀疏表
            int k = logTable[n] + 1;
            st = new int[n][k];
            
            // 初始化第一列（区间长度为1）
            for (int i = 0; i < n; i++) {
                st[i][0] = data[i];
            }
            
            // 填充其他列
            for (int j = 1; j < k; j++) {
                for (int i = 0; i <= n - (1 << j); i++) {
                    st[i][j] = Math.min(st[i][j-1], st[i + (1 << (j-1))][j-1]);
                }
            }
        }
        
        /**
         * 预计算log2值表
         */
        private void precomputeLogTable(int n) {
            logTable = new int[n + 1];
            logTable[1] = 0;
            for (int i = 2; i <= n; i++) {
                logTable[i] = logTable[i / 2] + 1;
            }
        }
        
        /**
         * 区间最小值查询
         * 时间复杂度：O(1)
         * @param l 左边界（包含）
         * @param r 右边界（包含）
         * @return 区间内的最小值
         */
        public int query(int l, int r) {
            if (l < 0 || r >= data.length || l > r) {
                throw new IllegalArgumentException("查询范围无效");
            }
            
            int length = r - l + 1;
            int k = logTable[length];
            
            return Math.min(st[l][k], st[r - (1 << k) + 1][k]);
        }
    }
    
    /**
     * 主函数 - 处理SPOJ RMQSQ问题
     */
    public static void main(String[] args) throws IOException {
        // 注意：在实际的SPOJ环境中，需要使用BufferedReader和PrintWriter
        // 这里为了简化测试，使用Scanner
        Scanner scanner = new Scanner(System.in);
        
        // 读取数组大小
        int n = scanner.nextInt();
        
        // 读取数组元素
        int[] data = new int[n];
        for (int i = 0; i < n; i++) {
            data[i] = scanner.nextInt();
        }
        
        // 构建稀疏表
        SparseTable sparseTable = new SparseTable(data);
        
        // 读取查询数量
        int q = scanner.nextInt();
        
        // 处理每个查询
        for (int i = 0; i < q; i++) {
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            System.out.println(sparseTable.query(l, r));
        }
        
        scanner.close();
    }
    
    /**
     * 测试方法
     */
    public static void testSparseTable() {
        System.out.println("=== 测试稀疏表 ===");
        
        int[] data = {1, 3, 5, 7, 9, 11, 13, 15, 17};
        
        // 创建稀疏表
        SparseTable sparseTable = new SparseTable(data);
        
        // 测试查询
        System.out.println("数组: " + Arrays.toString(data));
        System.out.println("区间[1, 5]的最小值: " + sparseTable.query(1, 5));  // 应该是3
        System.out.println("区间[0, 8]的最小值: " + sparseTable.query(0, 8));  // 应该是1
        System.out.println("区间[4, 7]的最小值: " + sparseTable.query(4, 7));  // 应该是9
        System.out.println("区间[2, 2]的最小值: " + sparseTable.query(2, 2));  // 应该是5
        System.out.println("区间[6, 8]的最小值: " + sparseTable.query(6, 8));  // 应该是13
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 生成大数据集
        int n = 100000;
        int[] largeData = new int[n];
        Random random = new Random(42); // 固定种子以确保可重复性
        for (int i = 0; i < n; i++) {
            largeData[i] = random.nextInt(1000000);
        }
        
        // 构建稀疏表
        long startTime = System.currentTimeMillis();
        SparseTable largeST = new SparseTable(largeData);
        long buildTime = System.currentTimeMillis() - startTime;
        
        // 执行大量查询
        int numQueries = 100000;
        startTime = System.currentTimeMillis();
        Random queryRandom = new Random(123); // 不同的种子
        for (int i = 0; i < numQueries; i++) {
            // 生成有效的查询范围
            int left = queryRandom.nextInt(n);
            int right = queryRandom.nextInt(n);
            if (left > right) {
                int temp = left;
                left = right;
                right = temp;
            }
            largeST.query(left, right);
        }
        long queryTime = System.currentTimeMillis() - startTime;
        
        System.out.println("构建100000个元素的稀疏表时间: " + buildTime + " ms");
        System.out.println("执行100000次查询时间: " + queryTime + " ms");
        System.out.println("平均每次查询时间: " + (double)queryTime / numQueries * 1000 + " μs");
    }
}