package class108;

import java.io.*;
import java.util.*;

/**
 * 洛谷 P3368 【模板】树状数组 2
 * 题目链接: https://www.luogu.com.cn/problem/P3368
 * 
 * 题目描述:
 * 给定一个数列，需要进行下面两种操作：
 * 1. 将某区间加上一个值
 * 2. 求出某一个数的值
 * 
 * 输入格式:
 * 第一行包含两个正整数 n, m，分别表示该数列数字的个数和总操作的次数。
 * 第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
 * 接下来 m 行每行包含 3 或 4 个整数，表示一个操作：
 * - 如果是 1 l r k：表示将区间 [l,r] 加上 k
 * - 如果是 2 x：表示求出第 x 项的值
 * 
 * 输出格式:
 * 对于每个 2 操作，输出一行一个整数表示答案。
 * 
 * 样例输入:
 * 5 5
 * 1 5 4 2 3
 * 1 2 4 2
 * 2 3
 * 1 1 5 -1
 * 1 3 5 7
 * 2 4
 * 
 * 样例输出:
 * 7
 * 9
 * 
 * 解题思路:
 * 使用树状数组实现区间修改和单点查询，采用差分数组的思想
 * 差分数组的性质：原数组的区间修改等价于差分数组的单点修改
 * 原数组的单点查询等价于差分数组的前缀和查询
 * 时间复杂度：
 * - 区间修改: O(log n)
 * - 单点查询: O(log n)
 * 空间复杂度: O(n)
 */

public class P3368_BitTree2 {
    // 树状数组最大容量
    public static int MAXN = 500001;
    
    // 树状数组，维护差分数组的前缀和
    public static long[] tree = new long[MAXN];
    
    // 数组长度和操作次数
    public static int n, m;
    
    /**
     * lowbit函数：获取数字的二进制表示中最右边的1所代表的数值
     * 例如：x=6(110) 返回2(010)，x=12(1100) 返回4(0100)
     * 
     * @param i 输入数字
     * @return 最低位的1所代表的数值
     */
    public static int lowbit(int i) {
        return i & -i;
    }
    
    /**
     * 单点增加操作：在位置i上增加v
     * 
     * @param i 位置（从1开始）
     * @param v 增加的值
     */
    public static void add(int i, long v) {
        // 从位置i开始，沿着父节点路径向上更新所有相关的节点
        while (i <= n) {
            tree[i] += v;
            // 移动到父节点
            i += lowbit(i);
        }
    }
    
    /**
     * 查询前缀和：计算从位置1到位置i的所有元素之和
     * 
     * @param i 查询的结束位置
     * @return 前缀和
     */
    public static long sum(int i) {
        long ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
    /**
     * 区间增加操作：在区间[l,r]上每个元素都增加v
     * 利用差分数组的思想：
     * 在差分数组的第l个位置加上v，在第r+1个位置减去v
     * 
     * @param l 区间起始位置
     * @param r 区间结束位置
     * @param v 增加的值
     */
    public static void rangeAdd(int l, int r, long v) {
        add(l, v);
        add(r + 1, -v);
    }
    
    /**
     * 主函数：处理输入输出和调用相关操作
     */
    public static void main(String[] args) throws IOException {
        // 使用高效的IO处理方式
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取数组长度n和操作次数m
        in.nextToken();
        n = (int) in.nval;
        in.nextToken();
        m = (int) in.nval;
        
        // 读取初始数组并构建差分数组
        long pre = 0;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            long cur = (long) in.nval;
            // 构建差分数组：差分数组第i位 = 原数组第i位 - 原数组第i-1位
            add(i, cur - pre);
            pre = cur;
        }
        
        // 处理m次操作
        for (int i = 1, a, b, c; i <= m; i++) {
            in.nextToken(); 
            a = (int) in.nval;
            
            if (a == 1) {
                // 操作1：区间[l,r]增加k
                in.nextToken(); 
                b = (int) in.nval;
                in.nextToken(); 
                c = (int) in.nval;
                in.nextToken();
                long k = (long) in.nval;
                rangeAdd(b, c, k);
            } else {
                // 操作2：查询位置b的值（即差分数组的前缀和）
                in.nextToken(); 
                b = (int) in.nval;
                out.println(sum(b));
            }
        }
        
        // 刷新输出缓冲区并关闭IO流
        out.flush();
        out.close();
        br.close();
    }
}