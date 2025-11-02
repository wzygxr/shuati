package class155;

import java.io.*;
import java.util.*;

/**
 * SPOJ QTREE - Query on a tree
 * 题目链接：https://www.spoj.com/problems/QTREE/
 * 
 * 问题描述：
 * 给定一棵N个节点的树，每条边都有一个权值。
 * 有两种操作：
 * 1. CHANGE i ti: 将第i条边的权值改为ti
 * 2. QUERY a b: 询问从节点a到节点b路径上边权的最大值
 * 
 * 解题思路：
 * 使用树链剖分（Tree Chain Decomposition）+ 线段树（Segment Tree）
 * 
 * 树链剖分核心思想：
 * 1. 第一次DFS：计算每个节点的深度、子树大小、重儿子
 * 2. 第二次DFS：进行链剖分，给节点重新编号，确定每条链的顶端
 * 3. 使用线段树维护重链上的边权信息
 * 
 * 树链剖分关键概念：
 * - 重儿子：子树大小最大的子节点
 * - 轻儿子：除重儿子外的其他子节点
 * - 重边：父节点与重儿子之间的边
 * - 轻边：父节点与轻儿子之间的边
 * - 重链：由重边连接形成的链
 * 
 * 时间复杂度分析：
 * - 预处理: O(N)
 * - 修改操作: O(log N)
 * - 查询操作: O(log² N)
 * - 总体: O(N + M * log² N)
 * 
 * 空间复杂度分析:
 * - 存储树结构: O(N)
 * - 线段树: O(N)
 * - 其他辅助数组: O(N)
 * - 总体: O(N)
 * 
 * 相关题目：
 * - Java实现：QTREE_Java.java
 * - Python实现：QTREE_Python.py
 */
public class QTREE_Java {
    
    static final int MAXN = 10010;
    
    // 链式前向星存图
    static class Edge {
        int to, next, weight, id;
        Edge(int to, int next, int weight, int id) {
            this.to = to;
            this.next = next;
            this.weight = weight;
            this.id = id;
        }
    }
    
    static Edge[] edges = new Edge[MAXN * 2];
    static int[] head = new int[MAXN];
    static int edgeCount = 0;
    
    // 树链剖分相关数组
    static int[] size = new int[MAXN];    // 子树大小
    static int[] depth = new int[MAXN];   // 节点深度
    static int[] father = new int[MAXN];  // 父节点
    static int[] son = new int[MAXN];     // 重儿子
    static int[] top = new int[MAXN];     // 所在链的顶端节点
    static int[] pos = new int[MAXN];     // 线段树中位置
    static int[] edgeId = new int[MAXN];  // 边的映射
    static int posCount = 0;
    
    // 线段树
    static int[] segTree = new int[MAXN * 4];
    static int[] lazy = new int[MAXN * 4];
    
    /**
     * 添加边
     * @param u 起始节点
     * @param v 终止节点
     * @param w 边的权重
     * @param id 边的编号
     */
    static void addEdge(int u, int v, int w, int id) {
        edges[edgeCount] = new Edge(v, head[u], w, id);
        head[u] = edgeCount++;
        edges[edgeCount] = new Edge(u, head[v], w, id);
        head[v] = edgeCount++;
    }
    
    /**
     * 第一次DFS：计算深度、子树大小、重儿子
     * @param u 当前节点
     * @param pre 父节点
     * @param dep 当前深度
     */
    static void dfs1(int u, int pre, int dep) {
        father[u] = pre;
        depth[u] = dep;
        size[u] = 1;
        
        for (int i = head[u]; i != -1; i = edges[i].next) {
            int v = edges[i].to;
            if (v == pre) continue;
            
            edgeId[v] = edges[i].id;
            dfs1(v, u, dep + 1);
            size[u] += size[v];
            
            // 更新重儿子
            if (size[v] > size[son[u]]) {
                son[u] = v;
            }
        }
    }
    
    /**
     * 第二次DFS：链剖分，重新编号
     * @param u 当前节点
     * @param tp 当前链的顶端节点
     */
    static void dfs2(int u, int tp) {
        top[u] = tp;
        pos[u] = ++posCount;
        
        if (son[u] != 0) {
            dfs2(son[u], tp);  // 优先遍历重儿子
        }
        
        for (int i = head[u]; i != -1; i = edges[i].next) {
            int v = edges[i].to;
            if (v == father[u] || v == son[u]) continue;
            dfs2(v, v);  // 轻儿子作为新链的顶端
        }
    }
    
    /**
     * 线段树向上更新
     * @param rt 当前节点在线段树中的位置
     */
    static void pushUp(int rt) {
        segTree[rt] = Math.max(segTree[rt << 1], segTree[rt << 1 | 1]);
    }
    
    /**
     * 构建线段树
     * @param l 区间左端点
     * @param r 区间右端点
     * @param rt 当前节点在线段树中的位置
     */
    static void build(int l, int r, int rt) {
        lazy[rt] = 0;
        if (l == r) {
            segTree[rt] = 0;
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    /**
     * 线段树单点更新
     * @param p 要更新的位置
     * @param val 新的值
     * @param l 区间左端点
     * @param r 区间右端点
     * @param rt 当前节点在线段树中的位置
     */
    static void update(int p, int val, int l, int r, int rt) {
        if (l == r) {
            segTree[rt] = val;
            return;
        }
        int mid = (l + r) >> 1;
        if (p <= mid) update(p, val, l, mid, rt << 1);
        else update(p, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    /**
     * 线段树区间查询最大值
     * @param L 查询区间左端点
     * @param R 查询区间右端点
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param rt 当前节点在线段树中的位置
     * @return 区间最大值
     */
    static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return segTree[rt];
        }
        int mid = (l + r) >> 1;
        int ans = Integer.MIN_VALUE;
        if (L <= mid) ans = Math.max(ans, query(L, R, l, mid, rt << 1));
        if (R > mid) ans = Math.max(ans, query(L, R, mid + 1, r, rt << 1 | 1));
        return ans;
    }
    
    /**
     * 查询树上两点间路径的最大边权
     * @param u 起始节点
     * @param v 终止节点
     * @return 路径上的最大边权
     */
    static int queryPath(int u, int v) {
        int ans = Integer.MIN_VALUE;
        
        // 两个点向上跳，直到在一个链上
        while (top[u] != top[v]) {
            if (depth[top[u]] < depth[top[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            // 查询u到top[u]的路径最大值
            ans = Math.max(ans, query(pos[top[u]], pos[u], 1, posCount, 1));
            u = father[top[u]];
        }
        
        // 在同一条链上
        if (u == v) return ans;
        
        // 保证u是深度更深的节点
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 查询u的子节点到v的路径最大值
        ans = Math.max(ans, query(pos[son[u]], pos[v], 1, posCount, 1));
        return ans;
    }
    
    /**
     * 主函数
     * 输入格式：
     * 第一行包含一个整数T，表示测试用例数量
     * 对于每个测试用例：
     *   第一行包含一个整数N，表示节点数量
     *   接下来N-1行，每行包含三个整数a、b、c，表示节点a和b之间有一条权值为c的边
     *   接下来若干行，每行包含一个操作：
     *     - CHANGE i ti: 将第i条边的权值改为ti
     *     - QUERY a b: 询问从节点a到节点b路径上边权的最大值
     *     - DONE: 结束当前测试用例
     * 输出格式：
     * 对于每个QUERY操作，输出路径上的最大边权
     */
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        
        int testCases = Integer.parseInt(reader.readLine().trim());
        
        for (int t = 0; t < testCases; t++) {
            int n = Integer.parseInt(reader.readLine().trim());
            
            // 初始化
            Arrays.fill(head, -1);
            edgeCount = 0;
            posCount = 0;
            Arrays.fill(son, 0);
            
            // 存储边信息
            int[] from = new int[n];
            int[] to = new int[n];
            int[] weight = new int[n];
            
            // 读取边信息
            for (int i = 1; i < n; i++) {
                String[] edgeInfo = reader.readLine().trim().split("\\s+");
                from[i] = Integer.parseInt(edgeInfo[0]);
                to[i] = Integer.parseInt(edgeInfo[1]);
                weight[i] = Integer.parseInt(edgeInfo[2]);
                addEdge(from[i], to[i], weight[i], i);
            }
            
            // 树链剖分
            dfs1(1, 0, 0);
            dfs2(1, 1);
            
            // 构建线段树
            build(1, n, 1);
            
            // 初始化线段树
            for (int i = 1; i < n; i++) {
                // 将边权赋给深度更深的节点
                int u = from[i], v = to[i];
                if (depth[u] > depth[v]) {
                    update(pos[u], weight[i], 1, n, 1);
                } else {
                    update(pos[v], weight[i], 1, n, 1);
                }
            }
            
            // 处理操作
            String line;
            while (!(line = reader.readLine().trim()).equals("DONE")) {
                String[] operation = line.split("\\s+");
                
                if (operation[0].equals("QUERY")) {
                    int u = Integer.parseInt(operation[1]);
                    int v = Integer.parseInt(operation[2]);
                    System.out.println(queryPath(u, v));
                } else if (operation[0].equals("CHANGE")) {
                    int edgeIdx = Integer.parseInt(operation[1]);
                    int newWeight = Integer.parseInt(operation[2]);
                    
                    // 更新边权
                    int u = from[edgeIdx], v = to[edgeIdx];
                    if (depth[u] > depth[v]) {
                        update(pos[u], newWeight, 1, n, 1);
                    } else {
                        update(pos[v], newWeight, 1, n, 1);
                    }
                }
            }
        }
    }
}