package class047;

import java.util.Scanner;

/**
 * POJ 3468. A Simple Problem with Integers
 * 
 * 题目描述:
 * 给定一个长度为 N 的数列 A，以及 M 条指令，每条指令可能是以下两种之一：
 * 1. "C a b c"：表示给 [a, b] 区间中的每一个数加上 c。
 * 2. "Q a b"：表示询问 [a, b] 区间中所有数的和。
 * 
 * 示例:
 * 输入:
 * 10 5
 * 1 2 3 4 5 6 7 8 9 10
 * Q 4 4
 * Q 1 10
 * Q 2 4
 * C 3 6 3
 * Q 2 4
 * 
 * 输出:
 * 4
 * 55
 * 9
 * 15
 * 
 * 题目链接: http://poj.org/problem?id=3468
 * 
 * 解题思路:
 * 使用线段树或树状数组来支持区间更新和区间查询。
 * 这里使用差分数组结合树状数组的方法：
 * 1. 维护两个树状数组：一个用于记录区间加法的累积影响，另一个用于记录区间加法的次数
 * 2. 区间更新时，使用差分思想在树状数组上进行标记
 * 3. 区间查询时，通过两个树状数组的组合计算得到区间和
 * 
 * 时间复杂度: O((N+M)logN) - 每次操作的时间复杂度为O(logN)
 * 空间复杂度: O(N) - 需要存储树状数组
 * 
 * 这是最优解之一，线段树和树状数组都是解决此类问题的标准方法。
 */
public class Code21_POJ3468 {

    static class FenwickTree {
        long[] tree;
        int n;
        
        public FenwickTree(int size) {
            this.n = size;
            this.tree = new long[n + 1];
        }
        
        // 单点更新
        public void update(int index, long delta) {
            while (index <= n) {
                tree[index] += delta;
                index += index & -index;
            }
        }
        
        // 前缀和查询
        public long query(int index) {
            long sum = 0;
            while (index > 0) {
                sum += tree[index];
                index -= index & -index;
            }
            return sum;
        }
        
        // 区间和查询
        public long rangeQuery(int l, int r) {
            return query(r) - query(l - 1);
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        long[] arr = new long[n + 1];
        FenwickTree bit1 = new FenwickTree(n); // 用于记录区间加法的累积影响
        FenwickTree bit2 = new FenwickTree(n); // 用于记录区间加法的次数
        
        // 读取初始数组
        for (int i = 1; i <= n; i++) {
            arr[i] = scanner.nextLong();
        }
        
        // 构建初始前缀和
        long[] prefix = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            prefix[i] = prefix[i - 1] + arr[i];
        }
        
        // 处理指令
        for (int i = 0; i < m; i++) {
            String op = scanner.next();
            if (op.equals("C")) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                long c = scanner.nextLong();
                
                // 区间更新: 使用差分思想
                bit1.update(a, c);
                bit1.update(b + 1, -c);
                bit2.update(a, c * (a - 1));
                bit2.update(b + 1, -c * b);
            } else if (op.equals("Q")) {
                int a = scanner.nextInt();
                int b = scanner.nextInt();
                
                // 区间查询: 使用两个树状数组组合计算
                long sum1 = prefix[b] + bit1.query(b) * b - bit2.query(b);
                long sum2 = prefix[a - 1] + bit1.query(a - 1) * (a - 1) - bit2.query(a - 1);
                System.out.println(sum1 - sum2);
            }
        }
        
        scanner.close();
    }
    
    /**
     * 算法原理详解:
     * 对于区间 [a, b] 加上 c 的操作，我们可以使用差分思想：
     * 设 d[i] 表示第 i 个位置的增量，那么：
     * - 在位置 a 加上 c
     * - 在位置 b+1 减去 c
     * 
     * 但是这样只能支持单点查询，为了支持区间查询，我们需要维护两个树状数组：
     * bit1: 记录差分数组 d[i]
     * bit2: 记录 i*d[i] 的前缀和
     * 
     * 区间和公式推导:
     * sum[1..x] = Σ(i=1 to x) arr[i] + Σ(i=1 to x) d[i] * (x - i + 1)
     *           = prefix[x] + x * Σ(i=1 to x) d[i] - Σ(i=1 to x) (i-1) * d[i]
     * 
     * 因此，区间 [a, b] 的和 = sum[1..b] - sum[1..a-1]
     */
    
    /**
     * 工程化考量:
     * 1. 大数处理: 使用long类型防止整数溢出
     * 2. 输入优化: 使用Scanner进行快速输入
     * 3. 边界检查: 确保索引不越界
     * 4. 性能优化: 使用树状数组降低时间复杂度
     */
    
    /**
     * 时间复杂度分析:
     * - 构建前缀和: O(n)
     * - 每次更新操作: O(log n)
     * - 每次查询操作: O(log n)
     * 总时间复杂度: O((n + m) log n)
     * 
     * 空间复杂度分析:
     * - 原始数组: O(n)
     * - 前缀和数组: O(n)
     * - 两个树状数组: O(n)
     * 总空间复杂度: O(n)
     */
    
    /**
     * 测试用例设计:
     * 1. 小规模测试: 验证基本功能
     * 2. 边界测试: 测试n=1, m=1的情况
     * 3. 性能测试: 大规模数据测试算法效率
     * 4. 正确性测试: 对比暴力解法的结果
     */
}