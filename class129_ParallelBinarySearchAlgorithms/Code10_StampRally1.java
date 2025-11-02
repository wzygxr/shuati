package class169;

// AGC002D Stamp Rally - Java实现
// 题目来源：https://atcoder.jp/contests/agc002/tasks/agc002_d
// 题目描述：并查集相关的二分答案问题
// 时间复杂度：O(Q * logM * α(N))
// 空间复杂度：O(N + M + Q)

import java.io.*;
import java.util.*;

public class Code10_StampRally1 {
    public static int MAXN = 100001;
    public static int MAXM = 100001;
    public static int MAXQ = 100001;
    public static int n, m, q;
    
    // 边信息
    public static int[] u = new int[MAXM];
    public static int[] v = new int[MAXM];
    
    // 查询信息
    public static int[] x = new int[MAXQ];
    public static int[] y = new int[MAXQ];
    public static int[] z = new int[MAXQ];
    public static int[] qid = new int[MAXQ];
    
    // 并查集
    public static int[] parent = new int[MAXN];
    public static int[] size = new int[MAXN];
    
    // 整体二分
    public static int[] lset = new int[MAXQ];
    public static int[] rset = new int[MAXQ];
    
    // 查询的答案
    public static int[] ans = new int[MAXQ];
    
    // 并查集操作
    public static void init(int n) {
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }
    
    public static int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }
    
    public static void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        if (rootX != rootY) {
            parent[rootX] = rootY;
            size[rootY] += size[rootX];
        }
    }
    
    // 检查使用前mid条边是否能满足查询id的要求
    public static boolean check(int id, int mid) {
        // 重建并查集
        init(n);
        // 加入前mid条边
        for (int i = 1; i <= mid; i++) {
            union(u[i], v[i]);
        }
        
        // 检查查询id是否满足要求
        int rootX = find(x[id]);
        int rootY = find(y[id]);
        
        if (rootX == rootY) {
            // x和y在同一个连通分量中
            return size[rootX] >= z[id];
        } else {
            // x和y在不同的连通分量中
            return size[rootX] + size[rootY] >= z[id];
        }
    }
    
    public static void compute(int ql, int qr, int vl, int vr) {
        if (ql > qr) {
            return;
        }
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = vl;
            }
        } else {
            int mid = (vl + vr) >> 1;
            int lsiz = 0, rsiz = 0;
            for (int i = ql; i <= qr; i++) {
                int id = qid[i];
                if (check(id, mid)) {
                    // 说明使用前mid条边就能满足要求
                    lset[++lsiz] = id;
                } else {
                    // 说明需要更多的边
                    rset[++rsiz] = id;
                }
            }
            
            // 重新排列查询顺序
            for (int i = 1; i <= lsiz; i++) {
                qid[ql + i - 1] = lset[i];
            }
            for (int i = 1; i <= rsiz; i++) {
                qid[ql + lsiz + i - 1] = rset[i];
            }
            
            // 递归处理左右两部分
            compute(ql, ql + lsiz - 1, vl, mid);
            compute(ql + lsiz, qr, mid + 1, vr);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] params = br.readLine().split(" ");
        n = Integer.parseInt(params[0]);
        m = Integer.parseInt(params[1]);
        
        // 读取边信息
        for (int i = 1; i <= m; i++) {
            params = br.readLine().split(" ");
            u[i] = Integer.parseInt(params[0]);
            v[i] = Integer.parseInt(params[1]);
        }
        
        q = Integer.parseInt(br.readLine());
        
        // 读取查询信息
        for (int i = 1; i <= q; i++) {
            params = br.readLine().split(" ");
            x[i] = Integer.parseInt(params[0]);
            y[i] = Integer.parseInt(params[1]);
            z[i] = Integer.parseInt(params[2]);
            qid[i] = i;
        }
        
        // 整体二分求解
        compute(1, q, 0, m);
        
        // 输出结果
        for (int i = 1; i <= q; i++) {
            out.println(ans[i]);
        }
        
        out.flush();
        out.close();
        br.close();
    }
}