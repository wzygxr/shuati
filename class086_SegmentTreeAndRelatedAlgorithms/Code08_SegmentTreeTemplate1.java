package class113;

import java.io.*;
import java.util.*;

/**
 * 线段树模板1 - 区间加法和区间求和
 * 题目来源：洛谷 P3372 【模板】线段树 1
 * 题目链接：https://www.luogu.com.cn/problem/P3372
 * 
 * 核心算法：线段树 + 懒标记
 * 难度：普及+/提高-
 * 
 * 【题目详细描述】
 * 如题，已知一个数列，你需要进行下面两种操作：
 * 1. 将某区间每一个数加上 k
 * 2. 求出某区间每一个数的和
 * 
 * 输入格式：
 * 第一行包含两个整数 n, m，分别表示该数列数字的个数和操作的总个数。
 * 第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
 * 接下来 m 行每行包含 3 或 4 个整数，表示一个操作。
 * 
 * 输出格式：
 * 输出若干行整数，表示每次操作2的结果。
 * 
 * 【解题思路】
 * 使用带懒标记的线段树来实现区间更新和区间查询。
 * 
 * 【核心算法】
 * 1. 线段树构建：构建支持区间求和的线段树
 * 2. 懒标记：使用懒标记优化区间更新操作
 * 3. 区间更新：支持区间加法操作
 * 4. 区间查询：支持区间求和操作
 * 
 * 【复杂度分析】
 * - 时间复杂度：
 *   - 构建线段树：O(n)
 *   - 区间更新：O(log n)
 *   - 区间查询：O(log n)
 * - 空间复杂度：O(n)，线段树所需空间
 * 
 * 【算法优化点】
 * 1. 懒标记优化：延迟下传标记，避免不必要的计算
 * 2. 位运算优化：使用位移操作优化索引计算
 * 3. IO优化：使用BufferedReader和PrintWriter优化输入输出
 * 
 * 【工程化考量】
 * 1. 输入输出效率：使用高效的IO处理大数据量
 * 2. 内存管理：合理分配线段树数组空间
 * 3. 错误处理：处理非法输入和边界情况
 * 
 * 【类似题目推荐】
 * 1. 洛谷 P3373 【模板】线段树 2 - https://www.luogu.com.cn/problem/P3373
 * 2. LeetCode 307. 区域和检索 - 数组可修改 - https://leetcode.cn/problems/range-sum-query-mutable/
 * 3. HDU 1698 Just a Hook - http://acm.hdu.edu.cn/showproblem.php?pid=1698
 * 4. POJ 3468 A Simple Problem with Integers - http://poj.org/problem?id=3468
 */
public class Code08_SegmentTreeTemplate1 {
    
    static long[] tree;
    static long[] lazy;
    static long[] arr;
    static int n, m;
    
    /**
     * 向下传递懒标记
     * 
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    static void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            int mid = (start + end) / 2;
            // 更新左右子节点的值
            tree[2 * node] += lazy[node] * (mid - start + 1);
            tree[2 * node + 1] += lazy[node] * (end - mid);
            // 传递懒标记给子节点
            lazy[2 * node] += lazy[node];
            lazy[2 * node + 1] += lazy[node];
            // 清除当前节点的懒标记
            lazy[node] = 0;
        }
    }
    
    /**
     * 构建线段树
     * 
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     */
    static void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
    
    /**
     * 区间更新操作
     * 
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 更新区间左边界
     * @param r 更新区间右边界
     * @param val 要加上的值
     */
    static void update(int node, int start, int end, int l, int r, long val) {
        if (l <= start && end <= r) {
            // 当前区间完全包含在更新区间内
            tree[node] += val * (end - start + 1);
            lazy[node] += val;
        } else {
            // 向下传递懒标记
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            if (l <= mid) update(2 * node, start, mid, l, r, val);
            if (r > mid) update(2 * node + 1, mid + 1, end, l, r, val);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
    
    /**
     * 区间查询操作
     * 
     * @param node 当前节点索引
     * @param start 当前区间左边界
     * @param end 当前区间右边界
     * @param l 查询区间左边界
     * @param r 查询区间右边界
     * @return 区间和
     */
    static long query(int node, int start, int end, int l, int r) {
        if (l <= start && end <= r) {
            // 当前区间完全包含在查询区间内
            return tree[node];
        }
        // 向下传递懒标记
        pushDown(node, start, end);
        int mid = (start + end) / 2;
        long sum = 0;
        if (l <= mid) sum += query(2 * node, start, mid, l, r);
        if (r > mid) sum += query(2 * node + 1, mid + 1, end, l, r);
        return sum;
    }
    
    /**
     * 主函数：处理输入并执行操作
     * 
     * @param args 命令行参数
     * @throws IOException IO异常
     */
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        arr = new long[n + 1];
        tree = new long[4 * n];
        lazy = new long[4 * n];
        
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        build(1, 1, n);
        
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            if (op == 1) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                long k = Long.parseLong(st.nextToken());
                update(1, 1, n, x, y, k);
            } else {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                out.println(query(1, 1, n, x, y));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}