// package class108;

import java.io.*;
import java.util.*;

/**
 * 洛谷 P3374 【模板】树状数组 1
 * 题目链接: https://www.luogu.com.cn/problem/P3374
 * 
 * 题目描述:
 * 给定一个数列，需要进行下面两种操作：
 * 1. 将某一个数加上一个值
 * 2. 求出某区间内所有数的和
 * 
 * 输入格式:
 * 第一行包含两个正整数 n, m，分别表示该数列数字的个数和总操作的次数。
 * 第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
 * 接下来 m 行每行包含 3 个整数，表示一个操作：
 * - 如果是 1 x k：表示将第 x 个数加上 k
 * - 如果是 2 x y：表示求出第 x 到第 y 项的和
 * 
 * 输出格式:
 * 对于每个 2 操作，输出一行一个整数表示答案。
 * 
 * 样例输入:
 * 5 5
 * 1 5 4 2 3
 * 1 1 3
 * 2 2 4
 * 1 2 4
 * 2 1 5
 * 2 2 4
 * 
 * 样例输出:
 * 11
 * 18
 * 16
 * 
 * 解题思路:
 * 使用树状数组（Binary Indexed Tree/Fenwick Tree）实现单点修改和区间查询
 * 时间复杂度：
 * - 单点修改: O(log n)
 * - 区间查询: O(log n)
 * 空间复杂度: O(n)
 */

public class P3374_BitTree1 {
    // 树状数组最大容量
    public static int MAXN = 500001;
    
    // 树状数组，存储前缀和信息
    public static int[] tree = new int[MAXN];
    
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
    public static void add(int i, int v) {
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
    public static int sum(int i) {
        int ans = 0;
        // 从位置i开始，沿着子节点路径向下累加
        while (i > 0) {
            ans += tree[i];
            // 移动到前一个相关区间
            i -= lowbit(i);
        }
        return ans;
    }
    
    /**
     * 区间查询：计算从位置l到位置r的所有元素之和
     * 利用前缀和的性质：[l,r]的和 = [1,r]的和 - [1,l-1]的和
     * 
     * @param l 区间起始位置
     * @param r 区间结束位置
     * @return 区间和
     */
    public static int range(int l, int r) {
        return sum(r) - sum(l - 1);
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
        
        // 读取初始数组并构建树状数组
        for (int i = 1, v; i <= n; i++) {
            in.nextToken();
            v = (int) in.nval;
            // 初始构建相当于在每个位置上增加初始值
            add(i, v);
        }
        
        // 处理m次操作
        for (int i = 1, a, b, c; i <= m; i++) {
            in.nextToken(); 
            a = (int) in.nval;
            in.nextToken(); 
            b = (int) in.nval;
            in.nextToken(); 
            c = (int) in.nval;
            
            if (a == 1) {
                // 操作1：在位置b上增加c
                add(b, c);
            } else {
                // 操作2：查询区间[b,c]的和
                out.println(range(b, c));
            }
        }
        
        // 刷新输出缓冲区并关闭IO流
        out.flush();
        out.close();
        br.close();
    }
}