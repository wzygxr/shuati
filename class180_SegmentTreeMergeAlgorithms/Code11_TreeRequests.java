package class182;

// Tree Requests CF570D
// 测试链接 : https://codeforces.com/problemset/problem/570/D
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：树上字符串查询问题，判断子树中节点字符能否重排成回文串
// 解法：线段树合并 + 位运算 + DFS序
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

import java.io.*;
import java.util.*;

public class Code11_TreeRequests {
    
    static final int MAXN = 500005;
    
    static int n, m;
    static List<Integer>[] G = new ArrayList[MAXN];
    static char[] val = new char[MAXN];
    static boolean[] ans = new boolean[MAXN];
    
    // 查询信息
    static int[] queryV = new int[MAXN];
    static int[] queryH = new int[MAXN];
    static List<Integer>[] queries = new ArrayList[MAXN];
    
    // 线段树合并相关
    static int[] root = new int[MAXN];
    static int[] lc = new int[MAXN*20];
    static int[] rc = new int[MAXN*20];
    static int[] cnt = new int[MAXN*20]; // 记录每个字符的出现次数
    static int segCnt = 0;
    
    // DFS序相关
    static int[] dfn = new int[MAXN];
    static int[] end = new int[MAXN];
    static int dfnCnt = 0;
    
    // 动态开点线段树插入
    static void insert(int rt, int l, int r, int x, int val) {
        if (l == r) {
            cnt[rt] ^= (1 << val); // 异或操作，出现偶数次为0，奇数次为1
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
        cnt[rt] = cnt[lc[rt]] ^ cnt[rc[rt]]; // 合并左右子树信息
    }
    
    // 线段树合并
    static int merge(int x, int y, int l, int r) {
        if (x == 0 || y == 0) return x + y;
        if (l == r) {
            cnt[x] ^= cnt[y];
            return x;
        }
        int mid = (l + r) >> 1;
        lc[x] = merge(lc[x], lc[y], l, mid);
        rc[x] = merge(rc[x], rc[y], mid+1, r);
        cnt[x] = cnt[lc[x]] ^ cnt[rc[x]];
        return x;
    }
    
    // 查询子树中字符出现次数的奇偶性
    static int query(int rt, int l, int r, int x, int y) {
        if (x <= l && r <= y) {
            return cnt[rt];
        }
        int mid = (l + r) >> 1;
        int res = 0;
        if (x <= mid && lc[rt] != 0) {
            res ^= query(lc[rt], l, mid, x, y);
        }
        if (y > mid && rc[rt] != 0) {
            res ^= query(rc[rt], mid+1, r, x, y);
        }
        return res;
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
        if (root[u] == 0) root[u] = ++segCnt;
        insert(root[u], 1, n, dfn[u], val[u] - 'a');
        
        // 处理当前节点的查询
        for (int i = 0; i < queries[u].size(); i++) {
            int id = queries[u].get(i);
            int h = queryH[id];
            // 查询深度为h的节点中字符出现次数的奇偶性
            int res = query(root[u], 1, n, 1, n);
            // 判断是否可以重排为回文串
            // 回文串的条件是最多只有一个字符出现奇数次
            ans[id] = (res & (res - 1)) == 0; // 判断是否为2的幂次或0
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = reader.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            G[i] = new ArrayList<>();
            queries[i] = new ArrayList<>();
        }
        
        // 读取树结构
        parts = reader.readLine().split(" ");
        for (int i = 2; i <= n; i++) {
            int p = Integer.parseInt(parts[i-2]);
            G[p].add(i);
            G[i].add(p);
        }
        
        // 读取节点字符
        String s = reader.readLine();
        for (int i = 1; i <= n; i++) {
            val[i] = s.charAt(i-1);
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            parts = reader.readLine().split(" ");
            queryV[i] = Integer.parseInt(parts[0]);
            queryH[i] = Integer.parseInt(parts[1]);
            queries[queryV[i]].add(i);
        }
        
        // DFS序处理
        dfs1(1, 0);
        
        // DFS处理线段树合并
        dfs2(1, 0);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            writer.println(ans[i] ? "Yes" : "No");
        }
        
        writer.flush();
        writer.close();
    }
}