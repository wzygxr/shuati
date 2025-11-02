package class182;

// Blood Cousins Return CF246E
// 测试链接 : https://codeforces.com/problemset/problem/246/E
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：Blood Cousins的加强版，查询第k代子孙的不同名字数量
// 解法：线段树合并 + DFS序 + Set合并
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code12_BloodCousinsReturn {
    
    static final int MAXN = 100005;
    
    static int n, m;
    static List<Integer>[] G = new ArrayList[MAXN];
    static String[] name = new String[MAXN];
    static int[] ans = new int[MAXN];
    
    // 查询信息
    static int[] queryV = new int[MAXN];
    static int[] queryK = new int[MAXN];
    static List<Integer>[] queries = new ArrayList[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static Set<String>[] names = new HashSet[MAXN*20];
    static int segCnt = 0;
    
    // 动态开点线段树插入
    static void insert(int rt, int l, int r, int x, String val) {
        if (names[rt] == null) names[rt] = new HashSet<>();
        if (l == r) {
            names[rt].add(val);
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
    }
    
    // 线段树合并
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            if (names[x] == null) names[x] = new HashSet<>();
            if (names[y] != null) names[x].addAll(names[y]);
            return x;
        }
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        // 合并左右子树信息
        if (names[x] == null) names[x] = new HashSet<>();
        if (names[lc[x]] != null) names[x].addAll(names[lc[x]]);
        if (names[rc[x]] != null) names[x].addAll(names[rc[x]]);
        return x;
    }
    
    // 查询第k代子孙的不同名字数量
    static int query(int rt, int l, int r, int k) {
        if (l == r) {
            return names[rt] != null ? names[rt].size() : 0;
        }
        int mid = (l + r) >> 1;
        if (k <= mid) {
            return lc[rt] != 0 ? query(lc[rt], l, mid, k) : 0;
        } else {
            return rc[rt] != 0 ? query(rc[rt], mid+1, r, k) : 0;
        }
    }
    
    // DFS处理线段树合并
    static void dfs(int u, int father, int depth) {
        // 先处理所有子节点
        for (int v : G[u]) {
            if (v != father) {
                dfs(v, u, depth + 1);
                // 合并子节点的信息到当前节点
                root[u] = merge(root[u], root[v], 1, n);
            }
        }
        
        // 插入当前节点的信息
        if (root[u] == 0) root[u] = ++segCnt;
        insert(root[u], 1, n, depth, name[u]);
        
        // 处理当前节点的查询
        for (int i = 0; i < queries[u].size(); i++) {
            int id = queries[u].get(i);
            int k = queryK[id];
            ans[id] = query(root[u], 1, n, depth + k);
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(reader.readLine());
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
            queries[i] = new ArrayList<>();
        }
        
        // 读取节点信息
        for (int i = 1; i <= n; i++) {
            String[] parts = reader.readLine().split(" ");
            name[i] = parts[0];
            int p = Integer.parseInt(parts[1]);
            if (p != 0) {
                G[p].add(i);
                G[i].add(p);
            }
        }
        
        // 读取查询
        m = Integer.parseInt(reader.readLine());
        for (int i = 1; i <= m; i++) {
            String[] parts = reader.readLine().split(" ");
            queryV[i] = Integer.parseInt(parts[0]);
            queryK[i] = Integer.parseInt(parts[1]);
            queries[queryV[i]].add(i);
        }
        
        // DFS处理线段树合并
        dfs(1, 0, 1);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            writer.println(ans[i]);
        }
        
        writer.flush();
        writer.close();
    }
}