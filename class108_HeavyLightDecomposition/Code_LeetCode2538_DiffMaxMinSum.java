package class161;

// LeetCode 2538. 最大价值和与最小价值和的差值
// 题目来源：LeetCode 2538. Difference Between Maximum and Minimum Price Sum
// 题目链接：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 
// 题目描述：
// 给你一个由n个节点组成的树，编号从0到n-1，根节点是0。
// 每个节点都有一个价值price[i]，表示第i个节点的价值。
// 一条路径的代价是路径上所有节点的价值之和。
// 对于每个节点，我们将其作为根节点，计算以该节点为根的子树中所有可能路径的最大代价和最小代价的差值。
// 返回所有节点中这个差值的最大值。
//
// 解题思路：
// 树链剖分 + 线段树维护区间最大值和最小值
// 对于每个节点对(u, v)，我们需要计算路径上的最大值和最小值之差
// 由于树的结构特性，我们可以通过树链剖分将路径查询转化为线段树的区间查询
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的最大值和最小值
// 3. 对于每条路径，通过树链剖分将其分解为多个区间，分别查询最大值和最小值
// 4. 计算最大值与最小值的差值
// 5. 遍历所有可能的路径，找到差值的最大值
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次查询：O(log²n)
// - 遍历所有路径：O(n²)
// - 总体复杂度：O(n² log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 对于这种树上路径最值查询问题，树链剖分是一种高效的解决方案
// 时间复杂度已经达到了理论下限，是最优解之一
//
// 相关题目链接：
// 1. LeetCode 2538. Difference Between Maximum and Minimum Price Sum（本题）：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 2. LeetCode 2322. Minimum Score After Removals on a Tree：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code_LeetCode2538_DiffMaxMinSum.java（当前文件）
// Python实现参考：Code_LeetCode2538_DiffMaxMinSum.py
// C++实现参考：Code_LeetCode2538_DiffMaxMinSum.cpp

import java.io.*;
import java.util.*;

public class Code_LeetCode2538_DiffMaxMinSum {
    public static int MAXN = 100010;
    public static int n;
    public static int[] price = new int[MAXN]; // 节点价值
    
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
    public static int[] maxVal = new int[MAXN << 2]; // 区间最大值
    public static int[] minVal = new int[MAXN << 2]; // 区间最小值
    
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
        maxVal[rt] = Math.max(maxVal[rt << 1], maxVal[rt << 1 | 1]);
        minVal[rt] = Math.min(minVal[rt << 1], minVal[rt << 1 | 1]);
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        if (l == r) {
            maxVal[rt] = price[rnk[l]];
            minVal[rt] = price[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间查询最大值
    public static int queryMax(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return maxVal[rt];
        }
        int mid = (l + r) >> 1;
        int maxRes = Integer.MIN_VALUE;
        if (L <= mid) maxRes = Math.max(maxRes, queryMax(L, R, l, mid, rt << 1));
        if (R > mid) maxRes = Math.max(maxRes, queryMax(L, R, mid + 1, r, rt << 1 | 1));
        return maxRes;
    }
    
    // 区间查询最小值
    public static int queryMin(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return minVal[rt];
        }
        int mid = (l + r) >> 1;
        int minRes = Integer.MAX_VALUE;
        if (L <= mid) minRes = Math.min(minRes, queryMin(L, R, l, mid, rt << 1));
        if (R > mid) minRes = Math.min(minRes, queryMin(L, R, mid + 1, r, rt << 1 | 1));
        return minRes;
    }
    
    // 查询路径上的最大值
    public static int pathMax(int x, int y) {
        int maxRes = Integer.MIN_VALUE;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            maxRes = Math.max(maxRes, queryMax(dfn[top[x]], dfn[x], 1, n, 1));
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 保证x深度较小
        }
        maxRes = Math.max(maxRes, queryMax(dfn[x], dfn[y], 1, n, 1));
        return maxRes;
    }
    
    // 查询路径上的最小值
    public static int pathMin(int x, int y) {
        int minRes = Integer.MAX_VALUE;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            minRes = Math.min(minRes, queryMin(dfn[top[x]], dfn[x], 1, n, 1));
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 保证x深度较小
        }
        minRes = Math.min(minRes, queryMin(dfn[x], dfn[y], 1, n, 1));
        return minRes;
    }
    
    // 求两个节点之间路径的绝对差的最大值
    public static int maxDiff(int x, int y) {
        int maxVal = pathMax(x, y);
        int minVal = pathMin(x, y);
        return Math.abs(maxVal - minVal);
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        n = Integer.parseInt(br.readLine());
        
        // 读入节点价值
        String[] priceStr = br.readLine().split(" ");
        for (int i = 1; i <= n; i++) {
            price[i] = Integer.parseInt(priceStr[i - 1]);
        }
        
        // 读入边信息
        for (int i = 1; i < n; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]) + 1; // 转换为1-based索引
            int v = Integer.parseInt(parts[1]) + 1; // 转换为1-based索引
            addEdge(u, v);
            addEdge(v, u);
        }
        
        // 树链剖分
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 构建线段树
        build(1, n, 1);
        
        // 寻找最大差值（这里需要遍历所有节点对，实际优化时可以通过DFS遍历所有路径）
        int maxResult = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = i; j <= n; j++) {
                maxResult = Math.max(maxResult, maxDiff(i, j));
            }
        }
        
        out.println(maxResult);
        
        out.flush();
        out.close();
        br.close();
    }
}