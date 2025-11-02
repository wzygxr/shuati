package class158;

// Problem: 洛谷P3919 - 【模板】可持久化线段树1（可持久化数组）
// Link: https://www.luogu.com.cn/problem/P3919
// Description: 维护一个长度为N的数组，支持在某个历史版本上修改某一个位置上的值，以及访问某个历史版本上的某一位置的值
// Solution: 使用可持久化线段树实现可持久化数组
// Time Complexity: O(logn) for each operation
// Space Complexity: O(n + mlogn) where m is the number of operations

import java.io.*;
import java.util.*;

public class Luogu_P3919_Java {
    static final int MAXN = 1000005;
    static int[] a = new int[MAXN];           // 初始数组
    static int n, m;
    
    // 主席树节点
    static class Node {
        int l, r, val;
        Node(int l, int r, int val) {
            this.l = l;
            this.r = r;
            this.val = val;
        }
    }
    
    static Node[] T = new Node[40 * MAXN];    // 主席树节点数组
    static int[] root = new int[MAXN];        // 每个版本的根节点
    static int cnt = 0;                       // 节点计数器
    
    // 创建新节点
    static int createNode(int l, int r, int val) {
        T[++cnt] = new Node(l, r, val);
        return cnt;
    }
    
    // 构建初始线段树
    static int build(int l, int r) {
        int now = createNode(0, 0, 0);
        
        if (l == r) {
            T[now].val = a[l];
            return now;
        }
        
        int mid = (l + r) >> 1;
        T[now].l = build(l, mid);
        T[now].r = build(mid + 1, r);
        return now;
    }
    
    // 在主席树中修改位置p的值为x
    static int update(int pre, int l, int r, int p, int x) {
        int now = createNode(0, 0, 0);
        
        if (l == r) {
            T[now].val = x;
            return now;
        }
        
        int mid = (l + r) >> 1;
        if (p <= mid) {
            T[now].l = update(T[pre].l, l, mid, p, x);
            T[now].r = T[pre].r;
        } else {
            T[now].l = T[pre].l;
            T[now].r = update(T[pre].r, mid + 1, r, p, x);
        }
        return now;
    }
    
    // 在主席树中查询位置p的值
    static int query(int u, int l, int r, int p) {
        if (l == r) {
            return T[u].val;
        }
        
        int mid = (l + r) >> 1;
        if (p <= mid) {
            return query(T[u].l, l, mid, p);
        } else {
            return query(T[u].r, mid + 1, r, p);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        
        String[] line = br.readLine().split(" ");
        n = Integer.parseInt(line[0]);
        m = Integer.parseInt(line[1]);
        
        line = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            a[i] = Integer.parseInt(line[i - 1]);
        }
        
        // 构建初始版本
        root[0] = build(1, n);
        
        // 处理操作
        for (int i = 1; i <= m; i++) {
            line = br.readLine().split(" ");
            int v = Integer.parseInt(line[0]);
            
            if (line[1].equals("1")) {
                // 修改操作
                int p = Integer.parseInt(line[2]);
                int x = Integer.parseInt(line[3]);
                root[i] = update(root[v], 1, n, p, x);
            } else {
                // 查询操作
                int p = Integer.parseInt(line[2]);
                out.println(query(root[v], 1, n, p));
                root[i] = root[v]; // 生成一样的版本
            }
        }
        
        out.close();
    }
}