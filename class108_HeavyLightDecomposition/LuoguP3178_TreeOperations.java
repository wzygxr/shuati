package class161;

import java.io.*;
import java.util.*;

// 洛谷P3178[HAOI2015]树上操作
// 题目来源：洛谷P3178 [HAOI2015]树上操作
// 题目链接：https://www.luogu.com.cn/problem/P3178
//
// 题目描述：
// 有一棵点数为N的树，以点1为根，且树有点权。然后有M个操作，分为三种：
// 操作1：把某个节点x的点权增加a。
// 操作2：把某个节点x为根的子树中所有点的点权都增加a。
// 操作3：询问某个节点x到根的路径中所有点的点权和。
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和，支持区间修改和区间查询
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和，支持区间加法操作
// 3. 对于单点加法操作：更新节点权值
// 4. 对于子树加法操作：更新子树对应的连续区间
// 5. 对于路径查询操作：计算从节点到根节点路径上的权值和
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次操作：O(log²n)
// - 总体复杂度：O(m log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。
//
// 相关题目链接：
// 1. 洛谷P3178 [HAOI2015]树上操作（本题）：https://www.luogu.com.cn/problem/P3178
// 2. 洛谷P2590 [ZJOI2008]树的统计：https://www.luogu.com.cn/problem/P2590
// 3. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：LuoguP3178_TreeOperations.java（当前文件）
// Python实现参考：Code_LuoguP3178_TreeOperations.py
// C++实现参考：Code_LuoguP3178_TreeOperations.cpp

public class LuoguP3178_TreeOperations {
    public static int MAXN = 100001;
    
    // 图相关
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int cnt = 0;
    
    // 树链剖分相关
    public static int[] fa = new int[MAXN];
    public static int[] dep = new int[MAXN];
    public static int[] siz = new int[MAXN];
    public static int[] son = new int[MAXN];
    public static int[] top = new int[MAXN];
    public static int[] dfn = new int[MAXN];
    public static int[] rnk = new int[MAXN];
    public static int cntd = 0;
    
    // 节点权值
    public static long[] val = new long[MAXN];
    
    // 线段树相关
    public static long[] sum = new long[MAXN << 2];
    public static long[] addTag = new long[MAXN << 2];
    
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt;
    }
    
    // 第一次dfs，计算树链剖分所需信息
    public static void dfs1(int u, int f) {
        fa[u] = f;
        dep[u] = dep[f] + 1;
        siz[u] = 1;
        
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != f) {
                dfs1(v, u);
                siz[u] += siz[v];
                if (son[u] == 0 || siz[son[u]] < siz[v]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 第二次dfs，计算重链剖分
    public static void dfs2(int u, int t) {
        top[u] = t;
        dfn[u] = ++cntd;
        rnk[cntd] = u;
        
        if (son[u] == 0) return;
        dfs2(son[u], t);
        
        for (int e = head[u], v; e != 0; e = next[e]) {
            v = to[e];
            if (v != fa[u] && v != son[u]) {
                dfs2(v, v);
            }
        }
    }
    
    // 线段树操作
    public static void up(int i) {
        sum[i] = sum[i << 1] + sum[i << 1 | 1];
    }
    
    public static void lazy(int i, long v, int n) {
        sum[i] += v * n;
        addTag[i] += v;
    }
    
    public static void down(int i, int ln, int rn) {
        if (addTag[i] != 0) {
            lazy(i << 1, addTag[i], ln);
            lazy(i << 1 | 1, addTag[i], rn);
            addTag[i] = 0;
        }
    }
    
    // 构建线段树
    public static void build(int l, int r, int i) {
        if (l == r) {
            sum[i] = val[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i);
    }
    
    // 区间加法
    public static void add(int jobl, int jobr, long jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(i, jobv, r - l + 1);
            return;
        }
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) add(jobl, jobr, jobv, l, mid, i << 1);
        if (jobr > mid) add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        up(i);
    }
    
    // 区间查询
    public static long query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        long ans = 0;
        if (jobl <= mid) ans += query(jobl, jobr, l, mid, i << 1);
        if (jobr > mid) ans += query(jobl, jobr, mid + 1, r, i << 1 | 1);
        return ans;
    }
    
    // 单点加法：将节点x的权值加上v
    public static void nodeAdd(int x, long v) {
        add(dfn[x], dfn[x], v, 1, cntd, 1);
    }
    
    // 子树加法：将以节点x为根的子树上的所有节点的权值加上v
    public static void subtreeAdd(int x, long v) {
        add(dfn[x], dfn[x] + siz[x] - 1, v, 1, cntd, 1);
    }
    
    // 路径查询：查询从节点x到节点y的路径上的所有节点的权值和
    public static long pathSum(int x, int y) {
        long ans = 0;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp;
            }
            ans += query(dfn[top[x]], dfn[x], 1, cntd, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp;
        }
        ans += query(dfn[x], dfn[y], 1, cntd, 1);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        int m = Integer.parseInt(br.readLine());
        
        // 读取节点初始权值
        String[] vals = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            val[i] = Long.parseLong(vals[i - 1]);
        }
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 树链剖分
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 构建线段树
        build(1, n, 1);
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            String[] parts = br.readLine().split(" ");
            int op = Integer.parseInt(parts[0]);
            
            if (op == 1) {
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                nodeAdd(x, a);
            } else if (op == 2) {
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                subtreeAdd(x, a);
            } else { // op == 3
                int x = Integer.parseInt(parts[1]);
                int y = Integer.parseInt(parts[2]);
                out.println(pathSum(x, y));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}