package class155;

import java.io.*;
import java.util.*;

/**
 * SPOJ RMQSQ - Range Minimum Query
 * 题目链接：https://www.spoj.com/problems/RMQSQ/
 * 
 * 问题描述：
 * 给定一个长度为N的数组，进行M次查询，每次查询区间[L,R]内的最小值。
 * 
 * 解题思路：
 * 使用Sparse Table（稀疏表）算法，也叫ST算法。
 * 这是一种基于动态规划的预处理算法，可以在O(N log N)时间内预处理，
 * 然后在O(1)时间内回答每次查询。
 * 
 * 核心思想：
 * 1. 预处理：用dp[i][j]表示从索引i开始，长度为2^j的区间内的最小值
 * 2. 状态转移方程：dp[i][j] = min(dp[i][j-1], dp[i+2^(j-1)][j-1])
 * 3. 查询：对于区间[L,R]，计算区间长度len=R-L+1，找到k使得2^k <= len
 *    答案为min(dp[L][k], dp[R-2^k+1][k])
 * 
 * 时间复杂度分析：
 * - 预处理: O(N log N)
 * - 查询: O(1)
 * - 总体: O(N log N + M)
 * 
 * 空间复杂度分析:
 * - 存储dp表: O(N log N)
 * 
 * 相关题目：
 * - Java实现：RMQSQ_Java.java
 * - Python实现：RMQSQ_Python.py
 */
public class RMQSQ_Java {
    
    static int MAXN = 100005;
    static int MAXLOG = 20;
    
    // Sparse Table数组
    static int[][] st = new int[MAXN][MAXLOG];
    static int[] logTable = new int[MAXN];  // 预处理log值
    
    /**
     * 预处理log表
     * logTable[i]表示floor(log2(i))
     */
    static void precomputeLog() {
        logTable[1] = 0;
        for (int i = 2; i < MAXN; i++) {
            logTable[i] = logTable[i >> 1] + 1;
        }
    }
    
    /**
     * 构建Sparse Table
     * @param arr 输入数组
     * @param n 数组长度
     */
    static void buildSparseTable(int[] arr, int n) {
        // 初始化长度为1的区间
        for (int i = 0; i < n; i++) {
            st[i][0] = arr[i];
        }
        
        // 动态规划填表
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                st[i][j] = Math.min(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
            }
        }
    }
    
    /**
     * 查询区间[L,R]内的最小值
     * @param L 区间左端点（从0开始）
     * @param R 区间右端点（从0开始）
     * @return 区间最小值
     */
    static int query(int L, int R) {
        int len = R - L + 1;
        int k = logTable[len];  // 找到最大的k使得2^k <= len
        return Math.min(st[L][k], st[R - (1 << k) + 1][k]);
    }
    
    /**
     * 主函数
     * 输入格式：
     * 第一行包含一个整数N，表示数组长度
     * 第二行包含N个整数，表示数组元素
     * 第三行包含一个整数M，表示查询次数
     * 接下来M行，每行包含两个整数L和R，表示查询区间
     * 输出格式：
     * 对于每个查询，输出区间[L,R]内的最小值
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // 预处理log表
        precomputeLog();
        
        // 读取数组长度
        int n = Integer.parseInt(reader.readLine().trim());
        
        // 读取数组元素
        String[] elements = reader.readLine().trim().split("\\s+");
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = Integer.parseInt(elements[i]);
        }
        
        // 构建Sparse Table
        buildSparseTable(arr, n);
        
        // 读取查询次数
        int q = Integer.parseInt(reader.readLine().trim());
        
        // 处理每次查询
        for (int i = 0; i < q; i++) {
            String[] queryRange = reader.readLine().trim().split("\\s+");
            int L = Integer.parseInt(queryRange[0]);
            int R = Integer.parseInt(queryRange[1]);
            
            // 输出查询结果
            System.out.println(query(L, R));
        }
    }
}