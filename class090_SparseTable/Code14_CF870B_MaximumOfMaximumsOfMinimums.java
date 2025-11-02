package class117;

/**
 * Maximum of Maximums of Minimums - Codeforces 870B
 * 
 * 【题目大意】
 * 给定一个长度为n的数组和一个整数k，要求将数组分成恰好k个连续的非空子数组，
 * 每个子数组的值为该子数组中所有元素的最小值，
 * 求所有子数组的值的最大值的最大可能值。
 * 
 * 【解题思路】
 * 这是一个贪心问题，可以通过分析不同k值的情况来解决：
 * 1. 当k=1时，只能分成一段，答案就是整个数组的最小值
 * 2. 当k>=3时，可以将最大值单独分为一段，其余元素分为另外两段，答案就是数组的最大值
 * 3. 当k=2时，需要枚举所有可能的分割点，找到使两段最小值的最大值最大的分割方案
 * 
 * 但也可以使用Sparse Table来预处理区间最小值，然后通过枚举分割点来求解。
 * 
 * 【时间复杂度分析】
 * - 预处理Sparse Table: O(n log n)
 * - 枚举分割点查询: O(n)
 * - 总时间复杂度: O(n log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组: O(n log n)
 * - 其他辅助数组: O(n)
 * - 总空间复杂度: O(n log n)
 * 
 * 【算法核心思想】
 * 使用Sparse Table预处理区间最小值，然后通过贪心策略或枚举分割点来找到最优解。
 * 
 * 【应用场景】
 * 1. 数组分割优化问题
 * 2. 区间最值查询问题
 * 3. 贪心算法与数据结构结合的问题
 */

import java.io.*;
import java.util.*;

public class Code14_CF870B_MaximumOfMaximumsOfMinimums {
    static final int MAXN = 100005;
    static final int LIMIT = 20;
    
    // 输入参数
    static int n, k;
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
        k = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 特殊情况处理
        if (k == 1) {
            // 只能分成一段，答案是整个数组的最小值
            int minVal = arr[1];
            for (int i = 2; i <= n; i++) {
                minVal = Math.min(minVal, arr[i]);
            }
            out.println(minVal);
        } else if (k >= 3) {
            // 可以将最大元素单独分为一段，答案是数组的最大值
            int maxVal = arr[1];
            for (int i = 2; i <= n; i++) {
                maxVal = Math.max(maxVal, arr[i]);
            }
            out.println(maxVal);
        } else {
            // k == 2，需要找到最优的分割点
            // 预处理log2数组和Sparse Table
            precomputeLog();
            buildSparseTable();
            
            int result = Integer.MIN_VALUE;
            
            // 枚举分割点，第一段为[1, i]，第二段为[i+1, n]
            for (int i = 1; i < n; i++) {
                int min1 = queryMin(1, i);      // 第一段的最小值
                int min2 = queryMin(i + 1, n);  // 第二段的最小值
                int maxMin = Math.max(min1, min2); // 两段最小值的最大值
                result = Math.max(result, maxMin);
            }
            
            out.println(result);
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
     * 1. 对于特殊情况直接处理，避免不必要的计算
     * 2. 使用Sparse Table预处理区间最值，实现O(1)查询
     * 3. 位运算优化幂运算和除法操作
     * 4. 1-based索引设计，简化区间计算
     * 5. 预处理log2数组避免重复计算
     * 
     * 【常见错误点】
     * 1. 数组索引越界：注意Sparse Table的边界条件
     * 2. 特殊情况处理：k=1和k>=3的情况需要特殊处理
     * 3. 区间长度计算：确保r-l+1计算正确
     * 4. 输入输出效率：大数据量时使用BufferedReader和PrintWriter
     * 
     * 【工程化考量】
     * 1. 代码模块化：将Sparse Table构建和查询封装为独立方法
     * 2. 异常处理：添加输入验证和边界检查
     * 3. 可扩展性：设计支持不同查询类型的Sparse Table框架
     * 4. 性能监控：对于大规模数据，可以添加性能统计
     */
}