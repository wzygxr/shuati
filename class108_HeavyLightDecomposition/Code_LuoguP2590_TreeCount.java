package class161;

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
// Java实现参考：Code_LuoguP2590_TreeCount.java（当前文件）
// Python实现参考：Code_LuoguP2590_TreeCount.py
// C++实现参考：Code_LuoguP2590_TreeCount.cpp

import java.io.*;
import java.util.*;

public class Code_LuoguP2590_TreeCount {
    public static int MAXN = 30010;
    public static int n, q;
    public static int[] arr = new int[MAXN]; // 节点权值
    
    // 邻接表存储树
    public static int[] head = new int[MAXN];
    public static int[] next = new int[MAXN << 1];
    public static int[] to = new int[MAXN << 1];
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
    
    // 线段树相关数组
    public static int[] sum = new int[MAXN << 2];    // 区间和
    public static int[] max = new int[MAXN << 2];    // 区间最大值
    
    // 添加边
    public static void addEdge(int u, int v) {
        next[++cnt] = head[u];
        to[cnt] = v;
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
        max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        if (l == r) {
            sum[rt] = max[rt] = arr[rnk[l]];
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
            sum[rt] = max[rt] = val;
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
    
    // 区间求和
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
    
    // 区间求最大值
    public static int queryMax(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return max[rt];
        }
        int mid = (l + r) >> 1;
        int ans = Integer.MIN_VALUE;
        if (L <= mid) ans = Math.max(ans, queryMax(L, R, l, mid, rt << 1));
        if (R > mid) ans = Math.max(ans, queryMax(L, R, mid + 1, r, rt << 1 | 1));
        return ans;
    }
    
    // 路径点权和查询
    public static int pathSum(int x, int y) {
        int ans = 0;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            ans += querySum(dfn[top[x]], dfn[x], 1, n, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 保证x深度较小
        }
        ans += querySum(dfn[x], dfn[y], 1, n, 1);
        return ans;
    }
    
    // 路径点权最大值查询
    public static int pathMax(int x, int y) {
        int ans = Integer.MIN_VALUE;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            ans = Math.max(ans, queryMax(dfn[top[x]], dfn[x], 1, n, 1));
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 保证x深度较小
        }
        ans = Math.max(ans, queryMax(dfn[x], dfn[y], 1, n, 1));
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        // 读入边信息
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 读入节点权值
        String[] vals = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(vals[i - 1]);
        }
        
        // 树链剖分
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 构建线段树
        build(1, n, 1);
        
        // 处理操作
        q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            String[] parts = br.readLine().split(" ");
            if (parts[0].equals("CHANGE")) {
                int u = Integer.parseInt(parts[1]);
                int t = Integer.parseInt(parts[2]);
                update(dfn[u], t, 1, n, 1);
                arr[u] = t; // 更新原数组
            } else if (parts[0].equals("QMAX")) {
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