package class161;

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
// Java实现参考：Code_CF165D_BeardGraph.java（当前文件）
// Python实现参考：Code_CF165D_BeardGraph.py
// C++实现参考：Code_CF165D_BeardGraph.cpp

import java.io.*;
import java.util.*;

public class Code_CF165D_BeardGraph {
    public static int MAXN = 100010;
    public static int n, m;
    
    // 邻接表存储树
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
    public static int[] edge_id = new int[MAXN << 1]; // 边的编号
    public static int cnt = 0;
    
    // 树链剖分相关数组
    public static int[] fa = new int[MAXN];     // 父节点
    public static int[] dep = new int[MAXN];    // 深度
    public static int[] siz = new int[MAXN];    // 子树大小
    public static int[] son = new int[MAXN];    // 重儿子
    public static int[] top = new int[MAXN];    // 所在重链的顶部节点
    public static int[] dfn = new int[MAXN];    // dfs序
    public static int[] rnk = new int[MAXN];    // dfs序对应的节点
    public static int time = 0;                 // dfs时间戳
    
    // 边的颜色状态：1表示白色，0表示黑色
    public static int[] edge_color = new int[MAXN];
    
    // 边到节点的映射（将边权转移到深度更深的节点上）
    public static int[] edge_to_node = new int[MAXN];
    
    // 线段树相关数组
    public static int[] sum = new int[MAXN << 2];      // 白色边的数量
    public static boolean[] has_white = new boolean[MAXN << 2]; // 是否存在白色边
    
    // 添加边
    public static void addEdge(int u, int v, int id) {
        next[++cnt] = head[u];
        to[cnt] = v;
        edge_id[cnt] = id;
        head[u] = cnt;
    }
    
    // 第一次DFS：计算深度、父节点、子树大小、重儿子
    public static void dfs1(int u, int father) {
        fa[u] = father;
        dep[u] = dep[father] + 1;
        siz[u] = 1;
        
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v != father) {
                dfs1(v, u);
                siz[u] += siz[v];
                // 更新重儿子
                if (son[u] == 0 || siz[v] > siz[son[u]]) {
                    son[u] = v;
                }
            }
        }
    }
    
    // 第二次DFS：计算重链顶部节点、dfs序
    public static void dfs2(int u, int tp) {
        top[u] = tp;
        dfn[u] = ++time;
        rnk[time] = u;
        
        if (son[u] != 0) {
            dfs2(son[u], tp); // 优先遍历重儿子
        }
        
        for (int i = head[u]; i != 0; i = next[i]) {
            int v = to[i];
            if (v != fa[u] && v != son[u]) {
                dfs2(v, v); // 轻儿子作为新重链的顶部
            }
        }
    }
    
    // 线段树向上更新
    public static void pushUp(int rt) {
        sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        has_white[rt] = has_white[rt << 1] || has_white[rt << 1 | 1];
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        if (l == r) {
            // 叶子节点不需要特殊处理，初始值为0
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 单点更新
    public static void update(int pos, int val, int l, int r, int rt) {
        if (l == r) {
            sum[rt] = val;
            has_white[rt] = (val > 0);
            return;
        }
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            update(pos, val, l, mid, rt << 1);
        } else {
            update(pos, val, mid + 1, r, rt << 1 | 1);
        }
        pushUp(rt);
    }
    
    // 区间查询和
    public static int querySum(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        int ans = 0;
        if (L <= mid) ans += querySum(L, R, l, mid, rt << 1);
        if (R > mid) ans += querySum(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    // 区间查询是否存在白色边
    public static boolean queryHasWhite(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return has_white[rt];
        }
        int mid = (l + r) >> 1;
        boolean ans = false;
        if (L <= mid) ans = ans || queryHasWhite(L, R, l, mid, rt << 1);
        if (R > mid) ans = ans || queryHasWhite(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    // 翻转边的颜色
    public static void flipEdge(int edgeId) {
        edge_color[edgeId] = 1 - edge_color[edgeId];
        // 更新线段树中对应节点的值
        int node = edge_to_node[edgeId];
        update(dfn[node], edge_color[edgeId], 1, time, 1);
    }
    
    // 查询路径上是否存在白色边
    public static boolean pathHasWhite(int x, int y) {
        boolean ans = false;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            ans = ans || queryHasWhite(dfn[top[x]], dfn[x], 1, time, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        if (x != y) { // 排除LCA节点本身
            ans = ans || queryHasWhite(dfn[x] + 1, dfn[y], 1, time, 1);
        }
        return ans;
    }
    
    // 查询路径上白色边的数量
    public static int pathWhiteCount(int x, int y) {
        int ans = 0;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            ans += querySum(dfn[top[x]], dfn[x], 1, time, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        if (x != y) { // 排除LCA节点本身
            ans += querySum(dfn[x] + 1, dfn[y], 1, time, 1);
        }
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        // 读入边信息
        int[] u = new int[n];
        int[] v = new int[n];
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            u[i] = Integer.parseInt(parts[0]);
            v[i] = Integer.parseInt(parts[1]);
            addEdge(u[i], v[i], i);
            addEdge(v[i], u[i], i);
        }
        
        // 初始化所有边为白色
        Arrays.fill(edge_color, 1);
        
        // 树链剖分，以节点1为根
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 建立边到节点的映射（将边权转移到深度更深的节点上）
        for (int i = 1; i < n; i++) {
            int node = (dep[u[i]] > dep[v[i]]) ? u[i] : v[i];
            edge_to_node[i] = node;
        }
        
        // 构建线段树
        build(1, n, 1);
        
        // 初始化线段树中的边权值
        for (int i = 1; i < n; i++) {
            update(dfn[edge_to_node[i]], 1, 1, n, 1);
        }
        
        m = Integer.parseInt(br.readLine());
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