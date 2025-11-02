package class182;

// Dominant Indices CF1009F
// 测试链接 : https://codeforces.com/problemset/problem/1009/F
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：对于每个节点，求其子树中深度最大的节点的深度
// 解法：线段树合并 + 树形DP
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code08_DominantIndices {
    
    static final int MAXN = 1000005;
    
    static int n;
    static List<Integer>[] G = new ArrayList[MAXN];
    static int[] ans = new int[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static int[] sum = new int[MAXN*20];
    static int[] maxDepth = new int[MAXN*20]; // 记录最大深度及其出现次数
    static int[] maxCount = new int[MAXN*20];
    static int cnt = 0;
    
    // 动态开点线段树插入
    static void insert(int rt, int l, int r, int x, int val) {
        if (l == r) {
            sum[rt] += val;
            maxDepth[rt] = l;
            maxCount[rt] = sum[rt];
            return;
        }
        int mid = (l + r) >> 1;
        if (x <= mid) {
            if (lc[rt] == 0) lc[rt] = ++cnt;
            insert(lc[rt], l, mid, x, val);
        } else {
            if (rc[rt] == 0) rc[rt] = ++cnt;
            insert(rc[rt], mid+1, r, x, val);
        }
        // 合并左右子树信息
        sum[rt] = sum[lc[rt]] + sum[rc[rt]];
        if (maxCount[lc[rt]] > maxCount[rc[rt]]) {
            maxDepth[rt] = maxDepth[lc[rt]];
            maxCount[rt] = maxCount[lc[rt]];
        } else if (maxCount[lc[rt]] < maxCount[rc[rt]]) {
            maxDepth[rt] = maxDepth[rc[rt]];
            maxCount[rt] = maxCount[rc[rt]];
        } else {
            maxDepth[rt] = Math.min(maxDepth[lc[rt]], maxDepth[rc[rt]]);
            maxCount[rt] = maxCount[lc[rt]];
        }
    }
    
    // 线段树合并
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            sum[x] += sum[y];
            maxDepth[x] = l;
            maxCount[x] = sum[x];
            return x;
        }
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        // 合并左右子树信息
        sum[x] = sum[lc[x]] + sum[rc[x]];
        if (maxCount[lc[x]] > maxCount[rc[x]]) {
            maxDepth[x] = maxDepth[lc[x]];
            maxCount[x] = maxCount[lc[x]];
        } else if (maxCount[lc[x]] < maxCount[rc[x]]) {
            maxDepth[x] = maxDepth[rc[x]];
            maxCount[x] = maxCount[rc[x]];
        } else {
            maxDepth[x] = Math.min(maxDepth[lc[x]], maxDepth[rc[x]]);
            maxCount[x] = maxCount[lc[x]];
        }
        return x;
    }
    
    // DFS处理线段树合并
    static void dfs(int u, int father) {
        // 先处理所有子节点
        for (int v : G[u]) {
            if (v != father) {
                dfs(v, u);
                // 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n);
            }
        }
        
        // 插入当前节点的深度信息（深度为1）
        if (root[u] == 0) root[u] = ++cnt;
        insert(root[u], 1, n, 1, 1);
        
        // 记录答案
        ans[u] = maxDepth[root[u]] - 1; // 减去1得到相对于当前节点的深度
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(reader.readLine());
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
        }
        
        // 读取树结构
        for (int i = 1; i < n; i++) {
            String[] parts = reader.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            G[u].add(v);
            G[v].add(u);
        }
        
        // DFS处理
        dfs(1, 0);
        
        // 输出结果
        for (int i = 1; i <= n; i++) {
            writer.println(ans[i]);
        }
        
        writer.flush();
        writer.close();
    }
}