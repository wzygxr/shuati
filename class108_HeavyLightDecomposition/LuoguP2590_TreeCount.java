package class161;

import java.io.*;
import java.util.*;

// 洛谷P2590[ZJOI2008]树的统计
// 题目来源：洛谷P2590 [ZJOI2008]树的统计
// 题目链接：https://www.luogu.com.cn/problem/P2590
//
// 题目描述：
// 一棵树上有n个节点，编号分别为1到n，每个节点都有一个权值w。
// 我们将以下面的形式来要求你对这棵树完成一些操作：
// I. CHANGE u t : 把结点u的权值改为t。
// II. QMAX u v: 询问从点u到点v的路径上的节点的最大权值。
// III. QSUM u v: 询问从点u到点v的路径上的节点的权值和。
// 注意：从点u到点v的路径上的节点包括u和v本身。
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间和与区间最大值
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的权值和与最大值
// 3. 对于修改操作：更新节点权值
// 4. 对于查询操作：计算路径上的权值和或最大值
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
// 1. 洛谷P2590 [ZJOI2008]树的统计（本题）：https://www.luogu.com.cn/problem/P2590
// 2. 洛谷P3178 [HAOI2015]树上操作：https://www.luogu.com.cn/problem/P3178
// 3. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：LuoguP2590_TreeCount.java（当前文件）
// Python实现参考：Code_LuoguP2590_TreeCount.py
// C++实现参考：Code_LuoguP2590_TreeCount.cpp

public class LuoguP2590_TreeCount {
    public static int MAXN = 30001;
    
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
    public static int[] val = new int[MAXN];
    
    // 线段树相关
    public static int[] sum = new int[MAXN << 2];
    public static int[] max = new int[MAXN << 2];
    
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
        max[i] = Math.max(max[i << 1], max[i << 1 | 1]);
    }
    
    // 构建线段树
    public static void build(int l, int r, int i) {
        if (l == r) {
            sum[i] = max[i] = val[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i);
    }
    
    // 单点更新
    public static void update(int jobx, int jobv, int l, int r, int i) {
        if (l == r) {
            sum[i] = max[i] = jobv;
            return;
        }
        int mid = (l + r) >> 1;
        if (jobx <= mid) {
            update(jobx, jobv, l, mid, i << 1);
        } else {
            update(jobx, jobv, mid + 1, r, i << 1 | 1);
        }
        up(i);
    }
    
    // 区间查询和
    public static int querySum(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        int ans = 0;
        if (jobl <= mid) ans += querySum(jobl, jobr, l, mid, i << 1);
        if (jobr > mid) ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
        return ans;
    }
    
    // 区间查询最大值
    public static int queryMax(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return max[i];
        }
        int mid = (l + r) >> 1;
        int ans = Integer.MIN_VALUE;
        if (jobl <= mid) ans = Math.max(ans, queryMax(jobl, jobr, l, mid, i << 1));
        if (jobr > mid) ans = Math.max(ans, queryMax(jobl, jobr, mid + 1, r, i << 1 | 1));
        return ans;
    }
    
    // 查询路径上的节点权值和
    public static int pathSum(int x, int y) {
        int ans = 0;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp;
            }
            ans += querySum(dfn[top[x]], dfn[x], 1, cntd, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp;
        }
        ans += querySum(dfn[x], dfn[y], 1, cntd, 1);
        return ans;
    }
    
    // 查询路径上的节点最大权值
    public static int pathMax(int x, int y) {
        int ans = Integer.MIN_VALUE;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp;
            }
            ans = Math.max(ans, queryMax(dfn[top[x]], dfn[x], 1, cntd, 1));
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp;
        }
        ans = Math.max(ans, queryMax(dfn[x], dfn[y], 1, cntd, 1));
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        
        // 读取边信息
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 读取节点权值
        String[] vals = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            val[i] = Integer.parseInt(vals[i - 1]);
        }
        
        // 树链剖分
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 构建线段树
        build(1, n, 1);
        
        int q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            String[] parts = br.readLine().split(" ");
            String op = parts[0];
            
            if (op.equals("CHANGE")) {
                int u = Integer.parseInt(parts[1]);
                int t = Integer.parseInt(parts[2]);
                update(dfn[u], t, 1, n, 1);
            } else if (op.equals("QMAX")) {
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                out.println(pathMax(u, v));
            } else { // QSUM
                int u = Integer.parseInt(parts[1]);
                int v = Integer.parseInt(parts[2]);
                out.println(pathSum(u, v));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}