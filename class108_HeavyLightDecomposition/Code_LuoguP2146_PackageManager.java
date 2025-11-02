package class161;

// 洛谷P2146[NOI2015]软件包管理器
// 题目来源：洛谷P2146 [NOI2015]软件包管理器
// 题目链接：https://www.luogu.com.cn/problem/P2146
// 
// 题目描述：
// 你决定设计你自己的软件包管理器。不可避免地，你要解决软件包之间的依赖问题。
// 如果软件包a依赖软件包b，那么安装软件包a以前，必须先安装软件包b。
// 同时，如果想要卸载软件包b，则必须卸载软件包a。
// 现在你已经获得了所有的软件包之间的依赖关系。而且，由于你之前的工作，
// 除0号软件包以外，在你的管理器当中的软件包都会依赖一个且仅一个软件包，
// 而0号软件包不依赖任何一个软件包。且依赖关系不存在环。
// 
// 两种操作：
// install x：安装x号软件包
// uninstall x：卸载x号软件包
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 将依赖关系看作树结构，0号软件包为根节点
// 2. install操作：将节点x到根节点路径上的所有未安装节点安装
// 3. uninstall操作：将以节点x为根的子树中所有已安装节点卸载
// 4. 用线段树维护区间状态（0表示未安装，1表示已安装）
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的安装状态
// 3. 对于安装操作：更新从节点到根节点路径上所有未安装的节点为已安装
// 4. 对于卸载操作：更新以该节点为根的子树中所有已安装的节点为未安装
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
// 1. 洛谷P2146 [NOI2015]软件包管理器（本题）：https://www.luogu.com.cn/problem/P2146
// 2. 洛谷P3979 遥远的国度：https://www.luogu.com.cn/problem/P3979
// 3. Codeforces 916E Jamie and Tree：https://codeforces.com/problemset/problem/916/E
// 4. HackerEarth Tree Queries：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/approximate/tree-query/
//
// Java实现参考：Code_LuoguP2146_PackageManager.java（当前文件）
// Python实现参考：Code_LuoguP2146_PackageManager.py
// C++实现参考：Code_LuoguP2146_PackageManager.cpp

import java.io.*;
import java.util.*;

public class Code_LuoguP2146_PackageManager {
    public static int MAXN = 100010;
    public static int n, q;
    public static int[] parent = new int[MAXN]; // 父节点
    
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
    public static int[] sum = new int[MAXN << 2];    // 区间和（已安装软件包数量）
    public static int[] setTag = new int[MAXN << 2]; // 懒标记（-1表示无标记，0表示未安装，1表示已安装）
    
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
        if (setTag[rt] != -1) {
            // 下传懒标记
            setTag[rt << 1] = setTag[rt];
            setTag[rt << 1 | 1] = setTag[rt];
            // 更新区间和
            sum[rt << 1] = setTag[rt] * ln;
            sum[rt << 1 | 1] = setTag[rt] * rn;
            // 清除当前节点的懒标记
            setTag[rt] = -1;
        }
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        setTag[rt] = -1; // -1表示无标记
        if (l == r) {
            sum[rt] = 0; // 初始状态都是未安装
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间设置
    public static void update(int L, int R, int val, int l, int r, int rt) {
        if (L <= l && r <= R) {
            sum[rt] = val * (r - l + 1);
            setTag[rt] = val;
            return;
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        if (L <= mid) update(L, R, val, l, mid, rt << 1);
        if (R > mid) update(L, R, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间查询
    public static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        int ans = 0;
        if (L <= mid) ans += query(L, R, l, mid, rt << 1);
        if (R > mid) ans += query(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    // 安装软件包：将节点x到根节点路径上的所有未安装节点安装
    public static int install(int x) {
        int installedCount = 0;
        while (top[x] != 0) { // 当不在以0为根的重链上时
            int currentCount = query(dfn[top[x]], dfn[x], 1, n, 1);
            int totalCount = dfn[x] - dfn[top[x]] + 1;
            installedCount += totalCount - currentCount;
            update(dfn[top[x]], dfn[x], 1, 1, n, 1);
            x = fa[top[x]];
        }
        // 处理到根节点路径上的剩余部分
        int currentCount = query(dfn[0], dfn[x], 1, n, 1);
        int totalCount = dfn[x] - dfn[0] + 1;
        installedCount += totalCount - currentCount;
        update(dfn[0], dfn[x], 1, 1, n, 1);
        return installedCount;
    }
    
    // 卸载软件包：将以节点x为根的子树中所有已安装节点卸载
    public static int uninstall(int x) {
        int currentCount = query(dfn[x], dfn[x] + siz[x] - 1, 1, n, 1);
        update(dfn[x], dfn[x] + siz[x] - 1, 0, 1, n, 1);
        return currentCount;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        // 读入依赖关系
        for (int i = 1; i < n; i++) {
            parent[i] = Integer.parseInt(br.readLine());
            addEdge(parent[i], i);
            addEdge(i, parent[i]);
        }
        
        // 树链剖分
        dfs1(0, -1);
        dfs2(0, 0);
        
        // 构建线段树
        build(1, n, 1);
        
        // 处理操作
        q = Integer.parseInt(br.readLine());
        for (int i = 0; i < q; i++) {
            String[] parts = br.readLine().split(" ");
            if (parts[0].equals("install")) {
                int x = Integer.parseInt(parts[1]);
                out.println(install(x));
            } else { // uninstall
                int x = Integer.parseInt(parts[1]);
                out.println(uninstall(x));
            }
        }
        
        out.flush();
        out.close();
        br.close();
    }
}