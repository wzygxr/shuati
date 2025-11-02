package class182;

// More Powerful P3899
// 测试链接 : https://www.luogu.com.cn/problem/P3899
// 线段树合并解法
//
// 题目来源：湖南集训
// 题目大意：树上DP问题，需要维护子树信息
// 解法：线段树合并 + 树形DP
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code10_MorePowerful {
    
    static final int MAXN = 300005;
    
    static int n, q;
    static List<Integer>[] G = new ArrayList[MAXN];
    static long[] ans = new long[MAXN];
    static int[] size = new int[MAXN]; // 子树大小
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static long[] sum = new long[MAXN*20];
    static long[] cnt = new long[MAXN*20];
    static int segCnt = 0;
    
    // 计算子树大小
    static void dfs1(int u, int father) {
        size[u] = 1;
        for (int v : G[u]) {
            if (v != father) {
                dfs1(v, u);
                size[u] += size[v];
            }
        }
    }
    
    // 动态开点线段树插入
    static void insert(int rt, int l, int r, int x, long val) {
        if (l == r) {
            sum[rt] += x * val;
            cnt[rt] += val;
            return;
        }
        int mid = (l + r) >> 1;
        if (x <= mid) {
            if (lc[rt] == 0) lc[rt] = ++segCnt;
            insert(lc[rt], l, mid, x, val);
        } else {
            if (rc[rt] == 0) rc[rt] = ++segCnt;
            insert(rc[rt], mid+1, r, x, val);
        }
        sum[rt] = sum[lc[rt]] + sum[rc[rt]];
        cnt[rt] = cnt[lc[rt]] + cnt[rc[rt]];
    }
    
    // 线段树合并
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            sum[x] += sum[y];
            cnt[x] += cnt[y];
            return x;
        }
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        sum[x] = sum[lc[x]] + sum[rc[x]];
        cnt[x] = cnt[lc[x]] + cnt[rc[x]];
        return x;
    }
    
    // 查询前k大的和
    static long query(int rt, int l, int r, long k) {
        if (k <= 0) return 0;
        if (l == r) {
            return Math.min(k, cnt[rt]) * l;
        }
        int mid = (l + r) >> 1;
        if (cnt[rc[rt]] >= k) {
            return query(rc[rt], mid+1, r, k);
        } else {
            return sum[rc[rt]] + query(lc[rt], l, mid, k - cnt[rc[rt]]);
        }
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
        if (root[u] == 0) root[u] = ++segCnt;
        insert(root[u], 1, n, size[u], 1);
        
        // 记录答案
        ans[u] = query(root[u], 1, n, Math.min(size[u], (long)n));
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
        
        // 读取树结构
        for (int i = 1; i < n; i++) {
            parts = reader.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            G[u].add(v);
            G[v].add(u);
        }
        
        // 计算子树大小
        dfs1(1, 0);
        
        // DFS处理线段树合并
        dfs2(1, 0);
        
        // 处理查询
        for (int i = 0; i < q; i++) {
            int x = Integer.parseInt(reader.readLine());
            writer.println(ans[x]);
        }
        
        writer.flush();
        writer.close();
    }
}