package class161;

import java.io.*;
import java.util.Arrays;

// Codeforces 165D Beard Graph
// 题目来源：Codeforces 165D Beard Graph
// 题目链接：https://codeforces.com/problemset/problem/165/D
//
// 题目描述：
// 给定一棵n个节点的树，节点编号从1到n。
// 初始时树上所有边都是白色的。
// 现在有三种操作：
// 1. 0 i : 将第i条边的颜色翻转（白变黑，黑变白）
// 2. 1 a b : 询问从节点a到节点b的路径上是否存在白色的边，如果存在则输出1，否则输出0
// 3. 2 a b : 询问从节点a到节点b的路径上有多少条白色边
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 边权转点权：将每条边的权值下放到深度更深的节点上
// 3. 线段树：维护区间和与区间是否存在白色边
// 4. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 将边权转移到节点上（每条边的权值赋给深度更深的节点）
// 3. 使用线段树维护每个区间的白色边数量和是否存在白色边
// 4. 对于翻转操作：更新对应节点的边颜色状态
// 5. 对于查询操作：计算路径上的白色边数量或是否存在白色边
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
// 1. Codeforces 165D Beard Graph（本题）：https://codeforces.com/problemset/problem/165/D
// 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 5. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Codeforces165D_BeardGraph.java（当前文件）
// Python实现参考：Code_CF165D_BeardGraph.py
// C++实现参考：Code_CF165D_BeardGraph.cpp

public class Codeforces165D_BeardGraph {
    public static int MAXN = 100001;
    
    // 图相关
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int[] edgeId = new int[MAXN << 1]; // 边的编号
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
    
    // 边的颜色状态：true表示白色，false表示黑色
    public static boolean[] edgeColor = new boolean[MAXN];
    
    // 线段树相关
    public static int[] sum = new int[MAXN << 2]; // 白色边的数量
    public static boolean[] hasWhite = new boolean[MAXN << 2]; // 是否存在白色边
    
    // 边到节点的映射
    public static int[] edgeToNode = new int[MAXN];
    
    public static void addEdge(int u, int v, int id) {
        next[++cnt] = head[u];
        to[cnt] = v;
        edgeId[cnt] = id;
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
        hasWhite[i] = hasWhite[i << 1] || hasWhite[i << 1 | 1];
    }
    
    // 构建线段树
    public static void build(int l, int r, int i) {
        if (l == r) {
            // 叶子节点不需要特殊处理，初始值为0
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
            sum[i] = jobv;
            hasWhite[i] = (jobv > 0);
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
    
    // 区间查询是否存在白色边
    public static boolean queryHasWhite(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return hasWhite[i];
        }
        int mid = (l + r) >> 1;
        boolean ans = false;
        if (jobl <= mid) ans = ans || queryHasWhite(jobl, jobr, l, mid, i << 1);
        if (jobr > mid) ans = ans || queryHasWhite(jobl, jobr, mid + 1, r, i << 1 | 1);
        return ans;
    }
    
    // 翻转边的颜色
    public static void flipEdge(int edgeId) {
        edgeColor[edgeId] = !edgeColor[edgeId];
        // 更新线段树中对应节点的值
        int node = edgeToNode[edgeId];
        update(dfn[node], edgeColor[edgeId] ? 1 : 0, 1, cntd, 1);
    }
    
    // 查询路径上是否存在白色边
    public static boolean pathHasWhite(int x, int y) {
        boolean ans = false;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp;
            }
            ans = ans || queryHasWhite(dfn[top[x]], dfn[x], 1, cntd, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp;
        }
        if (x != y) { // 排除LCA节点本身
            ans = ans || queryHasWhite(dfn[x] + 1, dfn[y], 1, cntd, 1);
        }
        return ans;
    }
    
    // 查询路径上白色边的数量
    public static int pathWhiteCount(int x, int y) {
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
        if (x != y) { // 排除LCA节点本身
            ans += querySum(dfn[x] + 1, dfn[y], 1, cntd, 1);
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        int n = Integer.parseInt(br.readLine());
        
        // 读取边信息
        int[] u = new int[n+1];
        int[] v = new int[n+1];
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            u[i] = Integer.parseInt(parts[0]);
            v[i] = Integer.parseInt(parts[1]);
            addEdge(u[i], v[i], i);
            addEdge(v[i], u[i], i);
        }
        
        // 初始化所有边为白色
        Arrays.fill(edgeColor, true);
        
        // 树链剖分，以节点1为根
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 建立边到节点的映射（将边权转移到深度更深的节点上）
        for (int i = 1; i < n; i++) {
            int node = (dep[u[i]] > dep[v[i]]) ? u[i] : v[i];
            edgeToNode[i] = node;
        }
        
        // 构建线段树
        build(1, n, 1);
        
        // 初始化线段树中的边权值
        for (int i = 1; i < n; i++) {
            update(dfn[edgeToNode[i]], 1, 1, cntd, 1);
        }
        
        int m = Integer.parseInt(br.readLine());
        for (int i = 0; i < m; i++) {
            String[] parts = br.readLine().split(" ");
            int op = Integer.parseInt(parts[0]);
            
            if (op == 0) {
                int edgeId = Integer.parseInt(parts[1]);
                // 翻转边的颜色
                flipEdge(edgeId);
            } else if (op == 1) {
                int a = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);
                out.println(pathHasWhite(a, b) ? 1 : 0);
            } else { // op == 2
                int a = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);
                out.println(pathWhiteCount(a, b));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}