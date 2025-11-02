package class161;

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
// Java实现参考：Code_LuoguP3178_TreeOperations.java（当前文件）
// Python实现参考：Code_LuoguP3178_TreeOperations.py
// C++实现参考：Code_LuoguP3178_TreeOperations.cpp

import java.io.*;
import java.util.*;

public class Code_LuoguP3178_TreeOperations {
    public static int MAXN = 100010;
    public static int n, m;
    public static long[] arr = new long[MAXN]; // 节点权值
    
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
    public static long[] sum = new long[MAXN << 2];    // 区间和
    public static long[] addTag = new long[MAXN << 2]; // 懒标记
    
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
    }
    
    // 线段树懒标记下传
    public static void pushDown(int rt, int ln, int rn) {
        if (addTag[rt] != 0) {
            // 下传懒标记
            addTag[rt << 1] += addTag[rt];
            addTag[rt << 1 | 1] += addTag[rt];
            // 更新区间和
            sum[rt << 1] += addTag[rt] * ln;
            sum[rt << 1 | 1] += addTag[rt] * rn;
            // 清除当前节点的懒标记
            addTag[rt] = 0;
        }
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        addTag[rt] = 0;
        if (l == r) {
            sum[rt] = arr[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间加法
    public static void update(int L, int R, long val, int l, int r, int rt) {
        if (L <= l && r <= R) {
            sum[rt] += val * (r - l + 1);
            addTag[rt] += val;
            return;
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        if (L <= mid) update(L, R, val, l, mid, rt << 1);
        if (R > mid) update(L, R, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间查询
    public static long query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        long ans = 0;
        if (L <= mid) ans += query(L, R, l, mid, rt << 1);
        if (R > mid) ans += query(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    // 路径点权和查询（从节点x到根节点1）
    public static long pathSumToRoot(int x) {
        long ans = 0;
        while (top[x] != 1) { // 当不在以1为根的重链上时
            ans += query(dfn[top[x]], dfn[x], 1, n, 1);
            x = fa[top[x]];
        }
        // 处理到根节点路径上的剩余部分
        ans += query(dfn[1], dfn[x], 1, n, 1);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读入节点初始权值
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(parts[i - 1]);
        }
        
        // 读入边信息
        for (int i = 1; i < n; i++) {
            parts = br.readLine().split(" ");
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
            parts = br.readLine().split(" ");
            int op = Integer.parseInt(parts[0]);
            if (op == 1) {
                // 操作1：把某个节点x的点权增加a
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                update(dfn[x], dfn[x], a, 1, n, 1);
            } else if (op == 2) {
                // 操作2：把某个节点x为根的子树中所有点的点权都增加a
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                update(dfn[x], dfn[x] + siz[x] - 1, a, 1, n, 1);
            } else {
                // 操作3：询问某个节点x到根的路径中所有点的点权和
                int x = Integer.parseInt(parts[1]);
                out.println(pathSumToRoot(x));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}            if (op == 1) {
                // 操作1：把某个节点x的点权增加a
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                update(dfn[x], dfn[x], a, 1, n, 1);
            } else if (op == 2) {
                // 操作2：把某个节点x为根的子树中所有点的点权都增加a
                int x = Integer.parseInt(parts[1]);
                long a = Long.parseLong(parts[2]);
                update(dfn[x], dfn[x] + siz[x] - 1, a, 1, n, 1);
            } else {
                // 操作3：询问某个节点x到根的路径中所有点的点权和
                int x = Integer.parseInt(parts[1]);
                out.println(pathSumToRoot(x));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}