package class161;

// 洛谷P2486[SDOI2011]染色
// 题目来源：洛谷P2486 [SDOI2011]染色
// 题目链接：https://www.luogu.com.cn/problem/P2486
// 
// 题目描述：
// 给定一棵n个节点的无根树，共有m个操作，操作分为两种：
// 1. 将节点a到节点b的路径上的所有点（包括a和b）都染成颜色c。
// 2. 询问节点a到节点b的路径上的颜色段数量。
// 颜色段的定义是极长的连续相同颜色被认为是一段。例如112221由三段组成：11、222、1。
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间颜色段数，需要记录区间左右端点颜色
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的颜色段数和左右端点颜色
// 3. 对于染色操作：更新路径上所有节点的颜色
// 4. 对于查询操作：计算路径上的颜色段数，注意路径连接处颜色相同的合并
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
// 1. 洛谷P2486 [SDOI2011]染色（本题）：https://www.luogu.com.cn/problem/P2486
// 2. 洛谷P2146 [NOI2015]软件包管理器：https://www.luogu.com.cn/problem/P2146
// 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Code_LuoguP2486_Coloring.java（当前文件）
// Python实现参考：Code_LuoguP2486_Coloring.py
// C++实现参考：Code_LuoguP2486_Coloring.cpp

import java.io.*;
import java.util.*;

public class Code_LuoguP2486_Coloring {
    public static int MAXN = 100010;
    public static int n, m;
    public static int[] color = new int[MAXN]; // 节点颜色
    
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
    public static int[] sum = new int[MAXN << 2];    // 区间颜色段数
    public static int[] leftColor = new int[MAXN << 2]; // 区间左端点颜色
    public static int[] rightColor = new int[MAXN << 2]; // 区间右端点颜色
    public static int[] setColor = new int[MAXN << 2]; // 懒标记（-1表示无标记）
    
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
        // 更新左右端点颜色
        leftColor[rt] = leftColor[rt << 1];
        rightColor[rt] = rightColor[rt << 1 | 1];
        
        // 更新颜色段数
        sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        // 如果左子区间的右端点颜色等于右子区间的左端点颜色，则颜色段数减1
        if (rightColor[rt << 1] == leftColor[rt << 1 | 1]) {
            sum[rt]--;
        }
    }
    
    // 线段树懒标记下传
    public static void pushDown(int rt, int ln, int rn) {
        if (setColor[rt] != -1) {
            // 下传懒标记
            setColor[rt << 1] = setColor[rt];
            setColor[rt << 1 | 1] = setColor[rt];
            // 更新左右端点颜色
            leftColor[rt << 1] = setColor[rt];
            rightColor[rt << 1] = setColor[rt];
            leftColor[rt << 1 | 1] = setColor[rt];
            rightColor[rt << 1 | 1] = setColor[rt];
            // 更新颜色段数
            sum[rt << 1] = 1;
            sum[rt << 1 | 1] = 1;
            // 清除当前节点的懒标记
            setColor[rt] = -1;
        }
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        setColor[rt] = -1; // -1表示无标记
        if (l == r) {
            sum[rt] = 1;
            leftColor[rt] = rightColor[rt] = color[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间染色
    public static void update(int L, int R, int val, int l, int r, int rt) {
        if (L <= l && r <= R) {
            sum[rt] = 1;
            leftColor[rt] = rightColor[rt] = val;
            setColor[rt] = val;
            return;
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        if (L <= mid) update(L, R, val, l, mid, rt << 1);
        if (R > mid) update(L, R, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间查询
    public static int[] query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return new int[]{sum[rt], leftColor[rt], rightColor[rt]};
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        
        if (R <= mid) return query(L, R, l, mid, rt << 1);
        if (L > mid) return query(L, R, mid + 1, r, rt << 1 | 1);
        
        int[] leftResult = query(L, R, l, mid, rt << 1);
        int[] rightResult = query(L, R, mid + 1, r, rt << 1 | 1);
        
        int[] result = new int[3];
        result[0] = leftResult[0] + rightResult[0];
        if (leftResult[2] == rightResult[1]) {
            result[0]--;
        }
        result[1] = leftResult[1];
        result[2] = rightResult[2];
        return result;
    }
    
    // 路径染色
    public static void pathColor(int x, int y, int c) {
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            update(dfn[top[x]], dfn[x], c, 1, n, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        update(dfn[x], dfn[y], c, 1, n, 1);
    }
    
    // 路径颜色段数查询
    public static int pathColorCount(int x, int y) {
        int ans = 0;
        int lastColor = -1; // 上一次查询的右端点颜色
        
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            int[] result = query(dfn[top[x]], dfn[x], 1, n, 1);
            ans += result[0];
            // 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
            if (lastColor == result[2]) {
                ans--;
            }
            lastColor = result[1]; // 更新为当前查询的左端点颜色
            x = fa[top[x]];
        }
        
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        int[] result = query(dfn[x], dfn[y], 1, n, 1);
        ans += result[0];
        // 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
        if (lastColor == result[2]) {
            ans--;
        }
        
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读入节点初始颜色
        parts = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            color[i] = Integer.parseInt(parts[i - 1]);
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
            if (parts[0].equals("C")) {
                // 操作1：将节点a到节点b的路径上的所有点都染成颜色c
                int a = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);
                int c = Integer.parseInt(parts[3]);
                pathColor(a, b, c);
            } else {
                // 操作2：询问节点a到节点b的路径上的颜色段数量
                int a = Integer.parseInt(parts[1]);
                int b = Integer.parseInt(parts[2]);
                out.println(pathColorCount(a, b));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}