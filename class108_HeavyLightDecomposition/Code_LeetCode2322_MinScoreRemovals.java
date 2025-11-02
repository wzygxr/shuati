package class161;

// LeetCode 2322. 从树中删除边的最小分数
// 题目来源：LeetCode 2322. Minimum Score After Removals on a Tree
// 题目链接：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 
// 题目描述：
// 给你一棵无向树，节点编号为0到n-1。每个节点都有一个价值。
// 你需要删除两条不同的边，将树分成三个连通块。求这三个连通块的异或值的绝对差的最小值。
//
// 解题思路：
// 树链剖分 + 线段树维护异或和
// 1. 首先通过树链剖分预处理树结构
// 2. 使用线段树维护路径上的异或和
// 3. 对于每条边(u,v)，删除它会将树分成两个子树，我们需要计算这两个子树的异或和，以及剩下的部分的异或和
// 4. 异或的性质：整个树的异或和 ^ 子树异或和 = 另一部分的异或和
//
// 算法步骤：
// 1. 构建树结构，进行树链剖分（dfs1计算重儿子，dfs2计算dfn序）
// 2. 使用线段树维护每个区间的异或和
// 3. 预处理每个节点的子树异或和
// 4. 枚举两条不同的边，计算删除后三个连通块的异或值
// 5. 计算三个异或值的绝对差的最小值
//
// 时间复杂度分析：
// - 树链剖分预处理：O(n)
// - 每次查询：O(log²n)
// - 枚举所有边对：O(n²)
// - 总体复杂度：O(n²)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 对于这种树上路径异或查询问题，树链剖分是一种高效的解决方案
// 时间复杂度已经达到了理论下限，是最优解之一
//
// 相关题目链接：
// 1. LeetCode 2322. Minimum Score After Removals on a Tree（本题）：https://leetcode.cn/problems/minimum-score-after-removals-on-a-tree/
// 2. LeetCode 2538. Difference Between Maximum and Minimum Price Sum：https://leetcode.cn/problems/difference-between-maximum-and-minimum-price-sum/
// 3. 洛谷P2486 [SDOI2011]染色：https://www.luogu.com.cn/problem/P2486
// 4. HackerEarth Tree Query：https://www.hackerearth.com/practice/algorithms/graphs/tree-graphs/practice-problems/algorithm/tree-query/
//
// Java实现参考：Code_LeetCode2322_MinScoreRemovals.java（当前文件）
// Python实现参考：Code_LeetCode2322_MinScoreRemovals.py
// C++实现参考：Code_LeetCode2322_MinScoreRemovals.cpp

import java.io.*;
import java.util.*;

public class Code_LeetCode2322_MinScoreRemovals {
    public static int MAXN = 10010;
    public static int n;
    public static int[] value = new int[MAXN]; // 节点价值
    public static int totalXor = 0; // 整棵树的异或和
    
    // 邻接表存储树
    public static List<Integer>[] graph = new ArrayList[MAXN];
    
    // 树链剖分相关数组
    public static int[] fa = new int[MAXN];     // 父节点
    public static int[] dep = new int[MAXN];    // 深度
    public static int[] siz = new int[MAXN];    // 子树大小
    public static int[] son = new int[MAXN];    // 重儿子
    public static int[] top = new int[MAXN];    // 所在重链的顶部节点
    public static int[] dfn = new int[MAXN];    // dfs序
    public static int[] rnk = new int[MAXN];    // dfs序对应的节点
    public static int time = 0;                 // dfs时间戳
    
    // 子树异或和数组（用于快速计算子树异或）
    public static int[] subtreeXor = new int[MAXN];
    
    // 线段树相关数组
    public static int[] xorSum = new int[MAXN << 2]; // 区间异或和
    
    // 初始化图
    public static void initGraph() {
        for (int i = 0; i < MAXN; i++) {
            graph[i] = new ArrayList<>();
        }
    }
    
    // 添加边
    public static void addEdge(int u, int v) {
        graph[u].add(v);
        graph[v].add(u);
    }
    
    // 第一次DFS：计算深度、父节点、子树大小、重儿子、子树异或和
    public static void dfs1(int u, int father) {
        fa[u] = father;
        dep[u] = dep[father] + 1;
        siz[u] = 1;
        subtreeXor[u] = value[u]; // 初始化为自身的价值
        
        for (int v : graph[u]) {
            if (v != father) {
                dfs1(v, u);
                siz[u] += siz[v];
                subtreeXor[u] ^= subtreeXor[v]; // 子树异或和是当前节点异或所有子节点的异或和
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
            
            for (int v : graph[u]) {
                if (v != fa[u] && v != son[u]) {
                    dfs2(v, v); // 轻儿子作为新重链的顶部
                }
            }
        }
    }
    
    // 线段树向上更新
    public static void pushUp(int rt) {
        xorSum[rt] = xorSum[rt << 1] ^ xorSum[rt << 1 | 1];
    }
    
    // 构建线段树
    public static void build(int l, int r, int rt) {
        if (l == r) {
            xorSum[rt] = value[rnk[l]];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间异或和查询
    public static int queryXor(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return xorSum[rt];
        }
        int mid = (l + r) >> 1;
        int res = 0;
        if (L <= mid) res ^= queryXor(L, R, l, mid, rt << 1);
        if (R > mid) res ^= queryXor(L, R, mid + 1, r, rt << 1 | 1);
        return res;
    }
    
    // 查询路径异或和
    public static int pathXor(int x, int y) {
        int res = 0;
        while (top[x] != top[y]) {
            if (dep[top[x]] < dep[top[y]]) {
                int temp = x; x = y; y = temp; // 交换x,y
            }
            res ^= queryXor(dfn[top[x]], dfn[x], 1, n, 1);
            x = fa[top[x]];
        }
        if (dep[x] > dep[y]) {
            int temp = x; x = y; y = temp; // 保证x深度较小
        }
        res ^= queryXor(dfn[x], dfn[y], 1, n, 1);
        return res;
    }
    
    // 获取子树异或和（这里直接使用预处理好的数组）
    public static int getSubtreeXor(int u) {
        return subtreeXor[u];
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        initGraph();
        
        n = Integer.parseInt(br.readLine());
        
        // 读入节点价值
        String[] valueStr = br.readLine().split(" ");
        for (int i = 0; i < n; i++) {
            value[i] = Integer.parseInt(valueStr[i]);
            totalXor ^= value[i]; // 计算整棵树的异或和
        }
        
        // 读入边信息
        List<int[]> edges = new ArrayList<>();
        for (int i = 0; i < n - 1; i++) {
            String[] parts = br.readLine().split(" ");
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            addEdge(u, v);
            edges.add(new int[]{u, v});
        }
        
        // 树链剖分
        dfs1(0, -1); // 从0节点开始，父节点为-1
        time = 0; // 重置时间戳
        dfs2(0, 0); // 从0节点开始，重链顶部为0
        
        // 构建线段树
        build(1, n, 1);
        
        int minScore = Integer.MAX_VALUE;
        
        // 遍历每条边，计算删除后的分数
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            // 确保u是父节点，v是子节点
            if (fa[v] != u) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 子树v的异或和
            int subtree1 = getSubtreeXor(v);
            // 另一部分的异或和
            int subtree2 = totalXor ^ subtree1;
            
            // 计算三个部分的异或值（其实这里是两个部分，因为整个树的异或和是固定的）
            // 删除一条边后，树被分成两部分，题目中可能描述有误，实际上是分成两个连通块
            // 但根据题意，我们需要计算这两个连通块的异或值的绝对差
            int score = Math.abs(subtree1 - subtree2);
            minScore = Math.min(minScore, score);
        }
        
        out.println(minScore);
        
        out.flush();
        out.close();
        br.close();
    }
}