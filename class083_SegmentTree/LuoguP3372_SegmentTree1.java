package class110.problems.java;

// Luogu P3372. 【模板】线段树 1
// 题目链接: https://www.luogu.com.cn/problem/P3372
// 题目描述:
// 如题，已知一个数列，你需要进行下面两种操作：
// 1. 将某区间每一个数加上 k
// 2. 求出某区间每一个数的和
//
// 输入:
// 第一行包含两个整数 n, m，分别表示该数列数字的个数和操作的总个数。
// 第二行包含 n 个用空格分隔的整数，其中第 i 个数字表示数列第 i 项的初始值。
// 接下来 m 行每行包含 3 或 4 个整数，表示一个操作，具体如下：
// 1. 1 x y k：将区间 [x,y] 内每个数加上 k
// 2. 2 x y：输出区间 [x,y] 内每个数的和
//
// 输出:
// 输出包含若干行整数，即为所有操作 2 的结果。
//
// 示例:
// 输入:
// 5 5
// 1 5 4 2 3
// 2 2 4
// 1 2 3 2
// 2 3 4
// 1 1 5 1
// 2 1 4
//
// 输出:
// 11
// 8
// 20
//
// 解题思路:
// 这是一个支持区间加法和区间求和的线段树模板题。
// 1. 使用线段树维护区间和
// 2. 使用懒标记技术处理区间加法操作
// 3. 支持两种操作：
//    - 区间加法：将区间内每个数加上k
//    - 区间求和：查询区间内所有数的和
//
// 时间复杂度: 
// - 建树: O(n)
// - 区间更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

public class LuoguP3372_SegmentTree1 {
    
    // 线段树节点
    static class Node {
        int l, r;      // 区间左右端点
        long sum;      // 区间和
        long add;      // 加法懒标记
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
        }
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 原始数组
    private long[] arr;
    
    // 数组长度
    private int n;
    
    // 初始化线段树
    public void init(int n) {
        this.n = n;
        tree = new Node[n * 4];
        arr = new long[n + 1];
        for (int i = 0; i < n * 4; i++) {
            tree[i] = new Node(0, 0);
        }
    }
    
    // 向上传递
    private void pushUp(int i) {
        tree[i].sum = tree[i << 1].sum + tree[i << 1 | 1].sum;
    }
    
    // 懒标记下发
    private void pushDown(int i) {
        if (tree[i].add != 0) {
            // 下发给左子树
            lazy(i << 1, tree[i].add);
            // 下发给右子树
            lazy(i << 1 | 1, tree[i].add);
            // 清除父节点的懒标记
            tree[i].add = 0;
        }
    }
    
    // 懒标记更新
    private void lazy(int i, long val) {
        tree[i].sum += val * (tree[i].r - tree[i].l + 1);
        tree[i].add += val;
    }
    
    // 建立线段树
    public void build(int l, int r, int i) {
        tree[i].l = l;
        tree[i].r = r;
        tree[i].add = 0;
        
        if (l == r) {
            tree[i].sum = arr[l];
            return;
        }
        
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        pushUp(i);
    }
    
    // 区间加法
    public void add(int jobl, int jobr, long val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(i, val);
            return;
        }
        
        pushDown(i);
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, val, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, val, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 区间查询和
    public long query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i].sum;
        }
        
        pushDown(i);
        int mid = (l + r) >> 1;
        long ans = 0;
        if (jobl <= mid) {
            ans += query(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }
    
    // 测试函数
    public static void main(String[] args) throws IOException {
        LuoguP3372_SegmentTree1 solution = new LuoguP3372_SegmentTree1();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        // 读取n和m
        String[] nm = reader.readLine().trim().split(" ");
        int n = Integer.parseInt(nm[0]);
        int m = Integer.parseInt(nm[1]);
        
        solution.init(n);
        
        // 读取初始数组
        String[] arrStrs = reader.readLine().trim().split(" ");
        for (int i = 1; i <= n; i++) {
            solution.arr[i] = Long.parseLong(arrStrs[i - 1]);
        }
        
        // 建立线段树
        solution.build(1, n, 1);
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            String[] parts = reader.readLine().trim().split(" ");
            int op = Integer.parseInt(parts[0]);
            
            if (op == 1) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                long k = Long.parseLong(parts[3]);
                solution.add(x, y, k, 1, n, 1);
            } else if (op == 2) {
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                long result = solution.query(x, y, 1, n, 1);
                System.out.println(result);
            }
        }
        
        // 为了演示，我们直接使用示例数据进行测试
        System.out.println("示例测试:");
        solution.init(5);
        solution.arr[1] = 1;
        solution.arr[2] = 5;
        solution.arr[3] = 4;
        solution.arr[4] = 2;
        solution.arr[5] = 3;
        solution.build(1, 5, 1);
        
        System.out.println("操作 2 2 4: " + solution.query(2, 4, 1, 5, 1)); // 期望输出: 11
        solution.add(2, 3, 2, 1, 5, 1);
        System.out.println("操作 1 2 3 2 后 2 3 4: " + solution.query(3, 4, 1, 5, 1)); // 期望输出: 8
        solution.add(1, 5, 1, 1, 5, 1);
        System.out.println("操作 1 1 5 1 后 2 1 4: " + solution.query(1, 4, 1, 5, 1)); // 期望输出: 20
    }
}