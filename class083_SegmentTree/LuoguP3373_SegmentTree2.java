package class110.problems.java;

// Luogu P3373. 【模板】线段树 2
// 题目链接: https://www.luogu.com.cn/problem/P3373
// 题目描述:
// 如题，已知一个数列a，你需要进行下面三种操作：
// 1. 将某区间每一个数乘上x
// 2. 将某区间每一个数加上x
// 3. 求出某区间每一个数的和
//
// 输入:
// 第一行包含三个整数n, m, p，分别表示该数列数字的个数、操作的总个数和模数。
// 第二行包含n个用空格分隔的整数，其中第i个数字表示数列第i项的初始值。
// 接下来m行每行包含若干个整数，表示一个操作，操作有以下三种：
// 1. 1 l r x：将区间[l,r]内每个数乘上x
// 2. 2 l r x：将区间[l,r]内每个数加上x
// 3. 3 l r：求区间[l,r]内每个数的和对p取模的值
//
// 输出:
// 对于每个操作3，输出一行包含一个整数，表示区间和对p取模的值。
//
// 示例:
// 输入:
// 5 5 38
// 1 2 3 4 5
// 1 1 5 2
// 2 1 5 1
// 3 1 5
// 2 1 5 2
// 3 1 5
//
// 输出:
// 32
// 36
//
// 解题思路:
// 这是一个支持区间乘法、区间加法和区间求和的线段树模板题。
// 需要同时维护两个懒标记：乘法标记和加法标记。
// 1. 乘法标记优先级高于加法标记
// 2. 下发标记时，先下发乘法标记，再下发加法标记
// 3. 更新标记时，需要考虑标记的优先级和组合
//
// 时间复杂度: 
// - 建树: O(n)
// - 区间更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

import java.util.*;
import java.io.*;

public class LuoguP3373_SegmentTree2 {
    // 线段树节点
    static class Node {
        int l, r;      // 区间左右端点
        long sum;      // 区间和
        long mul;      // 乘法懒标记
        long add;      // 加法懒标记
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
        }
        
        public Node() {}
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 原始数组
    private long[] arr;
    
    // 模数
    private long p;
    
    // 数组长度
    private int n;
    
    // 初始化线段树
    public void init(int n, long p) {
        this.n = n;
        this.p = p;
        tree = new Node[n * 4];
        arr = new long[n + 1];
        for (int i = 0; i < n * 4; i++) {
            tree[i] = new Node();
        }
    }
    
    // 向上传递
    private void pushUp(int i) {
        tree[i].sum = (tree[i << 1].sum + tree[i << 1 | 1].sum) % p;
    }
    
    // 懒标记下发
    private void pushDown(int i) {
        if (tree[i].mul != 1 || tree[i].add != 0) {
            long mul = tree[i].mul;
            long add = tree[i].add;
            
            // 下发给左子树
            lazy(i << 1, mul, add);
            
            // 下发给右子树
            lazy(i << 1 | 1, mul, add);
            
            // 清除父节点的懒标记
            tree[i].mul = 1;
            tree[i].add = 0;
        }
    }
    
    // 懒标记更新
    private void lazy(int i, long mul, long add) {
        // 更新区间和
        tree[i].sum = (tree[i].sum * mul % p + add * (tree[i].r - tree[i].l + 1) % p) % p;
        
        // 更新乘法标记
        tree[i].mul = tree[i].mul * mul % p;
        
        // 更新加法标记
        tree[i].add = (tree[i].add * mul % p + add) % p;
    }
    
    // 建立线段树
    public void build(int l, int r, int i) {
        tree[i].l = l;
        tree[i].r = r;
        tree[i].mul = 1;
        tree[i].add = 0;
        
        if (l == r) {
            tree[i].sum = arr[l] % p;
            return;
        }
        
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        pushUp(i);
    }
    
    // 区间乘法
    public void multiply(int jobl, int jobr, long val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(i, val, 0);
            return;
        }
        
        pushDown(i);
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            multiply(jobl, jobr, val, l, mid, i << 1);
        }
        if (jobr > mid) {
            multiply(jobl, jobr, val, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 区间加法
    public void add(int jobl, int jobr, long val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(i, 1, val);
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
            ans = (ans + query(jobl, jobr, l, mid, i << 1)) % p;
        }
        if (jobr > mid) {
            ans = (ans + query(jobl, jobr, mid + 1, r, i << 1 | 1)) % p;
        }
        return ans;
    }
    
    // 测试函数
    public static void main(String[] args) throws IOException {
        LuoguP3373_SegmentTree2 solution = new LuoguP3373_SegmentTree2();
        
        // 示例测试
        int n = 5, m = 5;
        long p = 38;
        
        solution.init(n, p);
        solution.arr[1] = 1;
        solution.arr[2] = 2;
        solution.arr[3] = 3;
        solution.arr[4] = 4;
        solution.arr[5] = 5;
        
        solution.build(1, n, 1);
        
        System.out.println("初始数组: [1, 2, 3, 4, 5]");
        
        // 操作1: 1 1 5 2 (将区间[1,5]内每个数乘上2)
        solution.multiply(1, 5, 2, 1, n, 1);
        System.out.println("操作 1 1 5 2 后");
        
        // 操作2: 2 1 5 1 (将区间[1,5]内每个数加上1)
        solution.add(1, 5, 1, 1, n, 1);
        System.out.println("操作 2 1 5 1 后");
        
        // 操作3: 3 1 5 (求区间[1,5]内每个数的和)
        long result1 = solution.query(1, 5, 1, n, 1);
        System.out.println("操作 3 1 5 结果: " + result1); // 应该是32
        
        // 操作4: 2 1 5 2 (将区间[1,5]内每个数加上2)
        solution.add(1, 5, 2, 1, n, 1);
        System.out.println("操作 2 1 5 2 后");
        
        // 操作5: 3 1 5 (求区间[1,5]内每个数的和)
        long result2 = solution.query(1, 5, 1, n, 1);
        System.out.println("操作 3 1 5 结果: " + result2); // 应该是36
    }
}