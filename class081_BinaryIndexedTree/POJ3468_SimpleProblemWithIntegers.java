package class108;

import java.io.*;
import java.util.*;

/**
 * POJ 3468 A Simple Problem with Integers
 * 题目链接: http://poj.org/problem?id=3468
 * 
 * 题目描述:
 * 给定一个长度为 N 的数列 A，需要处理如下两种操作：
 * 1. "C a b c"：将区间 [a, b] 中的每个数都加上 c
 * 2. "Q a b"：求区间 [a, b] 中所有数的和
 * 
 * 解题思路:
 * 使用树状数组实现区间更新、区间查询。
 * 这是树状数组的一个高级应用，需要使用差分的思想来处理区间更新。
 * 
 * 设原数组为 A，差分数组为 D，其中 D[1] = A[1]，D[i] = A[i] - A[i-1] (i > 1)。
 * 
 * 区间更新 [l, r] 增加 v：
 * - D[l] += v
 * - D[r+1] -= v
 * 
 * 区间查询 [1, n] 的和：
 * - 设 sumD[i] = D[1] + D[2] + ... + D[i]
 * - 原数组前缀和 sumA[n] = (n+1) * sumD[n] - (D[1]*1 + D[2]*2 + ... + D[n]*n)
 * 
 * 因此我们需要维护两个树状数组：
 * 1. tree1: 维护差分数组 D
 * 2. tree2: 维护 i*D[i]
 * 
 * 时间复杂度：
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 */

public class POJ3468_SimpleProblemWithIntegers {
    private long[] tree1;  // 维护差分数组 D
    private long[] tree2;  // 维护 i*D[i]
    private int n;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    private int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 树状数组初始化
     * 
     * @param n 数组长度
     */
    public POJ3468_SimpleProblemWithIntegers(int n) {
        this.n = n;
        this.tree1 = new long[n + 1];
        this.tree2 = new long[n + 1];
    }
    
    /**
     * 在 tree1 和 tree2 中的 position 位置增加 value
     * 
     * @param position 位置（从1开始）
     * @param value 增加的值
     */
    private void add(int position, long value) {
        // 更新 tree1
        for (int i = position; i <= n; i += lowbit(i)) {
            tree1[i] += value;
        }
        
        // 更新 tree2
        for (int i = position; i <= n; i += lowbit(i)) {
            tree2[i] += position * value;
        }
    }
    
    /**
     * 查询 tree1 的前缀和 [1, position]
     * 
     * @param position 查询位置
     * @return 前缀和
     */
    private long sum1(int position) {
        long ans = 0;
        for (int i = position; i > 0; i -= lowbit(i)) {
            ans += tree1[i];
        }
        return ans;
    }
    
    /**
     * 查询 tree2 的前缀和 [1, position]
     * 
     * @param position 查询位置
     * @return 前缀和
     */
    private long sum2(int position) {
        long ans = 0;
        for (int i = position; i > 0; i -= lowbit(i)) {
            ans += tree2[i];
        }
        return ans;
    }
    
    /**
     * 区间更新：将区间 [l, r] 中的每个数都加上 value
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @param value 增加的值
     */
    public void update(int l, int r, long value) {
        add(l, value);
        add(r + 1, -value);
    }
    
    /**
     * 区间查询：求区间 [1, position] 的前缀和
     * 
     * @param position 查询位置
     * @return 前缀和
     */
    public long prefixSum(int position) {
        return (position + 1) * sum1(position) - sum2(position);
    }
    
    /**
     * 区间查询：求区间 [l, r] 的和
     * 
     * @param l 区间左端点
     * @param r 区间右端点
     * @return 区间和
     */
    public long rangeSum(int l, int r) {
        if (l == 1) {
            return prefixSum(r);
        }
        return prefixSum(r) - prefixSum(l - 1);
    }
    
    /**
     * 主函数：处理输入输出和调用相关操作
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的IO处理方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度和操作数量
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int q = (int) in.nval;
        
        // 初始化树状数组
        POJ3468_SimpleProblemWithIntegers solution = new POJ3468_SimpleProblemWithIntegers(n);
        
        // 读取初始数组并构建差分数组
        long[] a = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            a[i] = (long) in.nval;
        }
        
        // 通过单点更新的方式构建初始差分数组
        for (int i = 1; i <= n; i++) {
            solution.update(i, i, a[i] - a[i - 1]);
        }
        
        // 处理操作
        for (int i = 0; i < q; i++) {
            in.nextToken();
            String op = in.sval;
            
            if (op.equals("C")) {
                // 区间更新操作
                in.nextToken(); int l = (int) in.nval;
                in.nextToken(); int r = (int) in.nval;
                in.nextToken(); long c = (long) in.nval;
                solution.update(l, r, c);
            } else {
                // 区间查询操作
                in.nextToken(); int l = (int) in.nval;
                in.nextToken(); int r = (int) in.nval;
                out.println(solution.rangeSum(l, r));
            }
        }
        
        // 刷新输出缓冲区并关闭IO流
        out.flush();
        out.close();
        br.close();
    }
}