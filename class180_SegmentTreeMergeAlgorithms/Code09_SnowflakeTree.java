package class182;

// Snowflake Tree P5384
// 测试链接 : https://www.luogu.com.cn/problem/P5384
// 线段树合并解法
//
// 题目来源：Cnoi2019
// 题目大意：树上路径查询问题，需要维护路径信息
// 解法：线段树合并 + DFS序 + 区间更新
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code09_SnowflakeTree {
    
    static final int MAXN = 1000005;
    static final long MOD = 998244353;
    
    static int n, q;
    static List<Integer>[] G = new ArrayList[MAXN];
    static int[] val = new int[MAXN];
    static long[] ans = new long[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static long[] sum = new long[MAXN*20];
    static long[] addTag = new long[MAXN*20];
    static int cnt = 0;
    
    // DFS序相关
    static int[] dfn = new int[MAXN];
    static int[] end = new int[MAXN];
    static int dfnCnt = 0;
    
    // 更新父节点信息
    static void pushUp(int rt) {
        sum[rt] = (sum[lc[rt]] + sum[rc[rt]]) % MOD;
    }
    
    // 下放标记
    static void pushDown(int rt, int l, int r) {
        if (addTag[rt] != 0) {
            int mid = (l + r) >> 1;
            if (lc[rt] == 0) lc[rt] = ++cnt;
            if (rc[rt] == 0) rc[rt] = ++cnt;
            addTag[lc[rt]] = (addTag[lc[rt]] + addTag[rt]) % MOD;
            addTag[rc[rt]] = (addTag[rc[rt]] + addTag[rt]) % MOD;
            sum[lc[rt]] = (sum[lc[rt]] + addTag[rt] * (mid - l + 1)) % MOD;
            sum[rc[rt]] = (sum[rc[rt]] + addTag[rt] * (r - mid)) % MOD;
            addTag[rt] = 0;
        }
    }
    
    // 区间加法
    static void update(int rt, int l, int r, int x, int y, long val) {
        if (x <= l && r <= y) {
            addTag[rt] = (addTag[rt] + val) % MOD;
            sum[rt] = (sum[rt] + val * (r - l + 1)) % MOD;
            return;
        }
        pushDown(rt, l, r);
        int mid = (l + r) >> 1;
        if (x <= mid) {
            if (lc[rt] == 0) lc[rt] = ++cnt;
            update(lc[rt], l, mid, x, y, val);
        }
        if (y > mid) {
            if (rc[rt] == 0) rc[rt] = ++cnt;
            update(rc[rt], mid+1, r, x, y, val);
        }
        pushUp(rt);
    }
    
    // 查询区间和
    static long query(int rt, int l, int r, int x, int y) {
        if (x <= l && r <= y) {
            return sum[rt];
        }
        pushDown(rt, l, r);
        int mid = (l + r) >> 1;
        long res = 0;
        if (x <= mid && lc[rt] != 0) {
            res = (res + query(lc[rt], l, mid, x, y)) % MOD;
        }
        if (y > mid && rc[rt] != 0) {
            res = (res + query(rc[rt], mid+1, r, x, y)) % MOD;
        }
        return res;
    }
    
    // 线段树合并
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            sum[x] = (sum[x] + sum[y]) % MOD;
            return x;
        }
        pushDown(x, l, r);
        pushDown(y, l, r);
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        pushUp(x);
        return x;
    }
    
    // DFS序处理
    static void dfs1(int u, int father) {
        dfn[u] = ++dfnCnt;
        for (int v : G[u]) {
            if (v != father) {
                dfs1(v, u);
            }
        }
        end[u] = dfnCnt;
    }
    
    // DFS处理线段树合并
    static void dfs2(int u, int father) {
        // 先处理所有子节点
        for (int v : G[u]) {
            if (v != father) {
                dfs2(v, u);
                // 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n);
            }
        }
        
        // 插入当前节点的信息
        if (root[u] == 0) root[u] = ++cnt;
        update(root[u], 1, n, dfn[u], dfn[u], val[u]);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = reader.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        q = Integer.parseInt(parts[1]);
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
        }
        
        // 读取节点权值
        parts = reader.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            val[i] = Integer.parseInt(parts[i-1]);
        }
        
        // 读取树结构
        for (int i = 1; i < n; i++) {
            parts = reader.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            G[u].add(v);
            G[v].add(u);
        }
        
        // DFS序处理
        dfs1(1, 0);
        
        // DFS处理线段树合并
        dfs2(1, 0);
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            parts = reader.readLine().split(" ");
            int op = Integer.parseInt(parts[0]);
            if (op == 1) {
                int u = Integer.parseInt(parts[1]);
                int x = Integer.parseInt(parts[2]);
                // 在u的子树中所有节点权值加上x
                update(root[1], 1, n, dfn[u], end[u], x);
            } else {
                int u = Integer.parseInt(parts[1]);
                // 查询u子树的权值和
                long res = query(root[1], 1, n, dfn[u], end[u]);
                writer.println(res);
            }
        }
        
        writer.flush();
        writer.close();
    }
}