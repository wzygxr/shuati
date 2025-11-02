package class117;

/**
 * Static Range Minimum Queries - CSES 1647
 * 
 * 【题目大意】
 * 给定一个长度为n的整数数组，需要处理q个查询，每个查询给定一个范围[a,b]，
 * 要求回答该范围内元素的最小值。
 * 
 * 【解题思路】
 * 这是经典的RMQ（Range Minimum Query）问题，可以使用Sparse Table来解决。
 * Sparse Table通过预处理所有长度为2的幂次的区间最小值，
 * 实现O(n log n)预处理，O(1)查询的时间复杂度。
 * 
 * 【时间复杂度分析】
 * - 预处理Sparse Table: O(n log n)
 * - 单次查询: O(1)
 * - 总时间复杂度: O(n log n + q)
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组: O(n log n)
 * - 其他辅助数组: O(n)
 * - 总空间复杂度: O(n log n)
 * 
 * 【算法核心思想】
 * 1. 预处理阶段：使用动态规划构建Sparse Table
 * 2. 查询阶段：将任意区间分解为两个重叠的预处理区间，取最小值
 * 
 * 【应用场景】
 * 1. 大数据分析中的快速区间统计
 * 2. 信号处理中的特征提取
 * 3. 游戏开发中的范围检测
 * 4. 网络流量监控中的异常检测
 */

import java.io.*;
import java.util.*;

public class Code15_CSES1647_StaticRangeMinimumQueries {
    static final int MAXN = 200005;
    static final int LIMIT = 20;
    
    // 输入参数
    static int n, q;
    static int[] arr = new int[MAXN];
    
    // Sparse Table数组，st[i][j]表示从位置i开始，长度为2^j的区间的最小值
    static int[][] st = new int[MAXN][LIMIT];
    
    // log数组，log2[i]表示不超过i的最大的2的幂次
    static int[] log2 = new int[MAXN];
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取输入
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        q = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 预处理log2数组和Sparse Table
        precomputeLog();
        buildSparseTable();
        
        // 处理每个查询
        for (int i = 0; i < q; i++) {
            line = br.readLine().split(" ");
            int a = Integer.parseInt(line[0]);
            int b = Integer.parseInt(line[1]);
            // 注意：题目输入是1-based，但转换为内部处理时需要保持一致
            out.println(queryMin(a, b));
        }
        
        out.flush();
        out.close();
        br.close();
    }
    
    /**
     * 预处理log2数组，用于快速查询区间长度对应的2的幂次
     */
    static void precomputeLog() {
        log2[1] = 0;
        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i >> 1] + 1;
        }
    }
    
    /**
     * 构建Sparse Table，预处理区间最小值
     */
    static void buildSparseTable() {
        // 初始化Sparse Table的第一层（j=0）
        for (int i = 1; i <= n; i++) {
            st[i][0] = arr[i];
        }
        
        // 动态规划构建Sparse Table
        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 1; i + (1 << j) - 1 <= n; i++) {
                st[i][j] = Math.min(
                    st[i][j - 1],
                    st[i + (1 << (j - 1))][j - 1]
                );
            }
        }
    }
    
    /**
     * 查询区间[l,r]的最小值
     * 
     * @param l 区间左端点（包含，1-based）
     * @param r 区间右端点（包含，1-based）
     * @return 区间最小值
     */
    static int queryMin(int l, int r) {
        int k = log2[r - l + 1];
        return Math.min(
            st[l][k],
            st[r - (1 << k) + 1][k]
        );
    }
    
    /**
     * 【算法优化技巧】
     * 1. 预处理log2数组避免重复计算
     * 2. 使用位运算优化幂运算和除法操作
     * 3. 1-based索引设计，简化区间计算
     * 4. 高效IO处理，使用BufferedReader和PrintWriter
     * 
     * 【常见错误点】
     * 1. 数组索引越界：注意Sparse Table的边界条件
     * 2. 区间长度计算：确保r-l+1计算正确
     * 3. 查询区间转换：注意输入的1-based索引处理
     * 
     * 【工程化考量】
     * 1. 代码模块化：将Sparse Table构建和查询封装为独立方法
     * 2. 异常处理：添加输入验证和边界检查
     * 3. 可扩展性：设计支持不同查询类型的Sparse Table框架
     * 4. 性能监控：对于大规模数据，可以添加性能统计
     */
}