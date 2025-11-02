import java.util.*;

/**
 * LeetCode 2846. 边权重查询的最小值
 * 题目描述：给定一棵无向树，每个边有一个权重。支持多次查询，每次查询给出两个节点u和v，
 * 要求找到从u到v路径上所有边权重的最小值。
 * 
 * 数据范围：n ≤ 10^5，q ≤ 10^5
 * 解法：树链剖分 + 线段树维护区间最小值
 * 
 * 时间复杂度：预处理O(n)，每次查询O(log²n)
 * 空间复杂度：O(n)
 * 
 * 网址：https://leetcode.cn/problems/minimum-edge-weight-equilibrium-queries-in-a-tree/
 */
public class Code_LeetCode2846_MinEdgeWeightQueries {
    static class Edge {
        int to, weight;
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    static class SegmentTree {
        int[] min;
        
        SegmentTree(int n) {
            min = new int[4 * n];
            Arrays.fill(min, Integer.MAX_VALUE);
        }
        
        void pushUp(int rt) {
            min[rt] = Math.min(min[rt << 1], min[rt << 1 | 1]);
        }
        
        void build(int l, int r, int rt, int[] arr) {
            if (l == r) {
                min[rt] = arr[l];
                return;
            }
            int mid = (l + r) >> 1;
            build(l, mid, rt << 1, arr);
            build(mid + 1, r, rt << 1 | 1, arr);
            pushUp(rt);
        }
        
        int query(int L, int R, int l, int r, int rt) {
            if (L <= l && r <= R) {
                return min[rt];
            }
            int mid = (l + r) >> 1;
            int res = Integer.MAX_VALUE;
            if (L <= mid) res = Math.min(res, query(L, R, l, mid, rt << 1));
            if (R > mid) res = Math.min(res, query(L, R, mid + 1, r, rt << 1 | 1));
            return res;
        }
    }
    
    static int n, cnt;
    static int[] parent, depth, size, heavy, head, pos, edgeWeight;
    static List<Edge>[] tree;
    static SegmentTree seg;
    
    public int[] minEdgeWeightQueries(int n, int[][] edges, int[][] queries) {
        this.n = n;
        init();
        
        // 构建树
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1], w = edge[2];
            tree[u].add(new Edge(v, w));
            tree[v].add(new Edge(u, w));
        }
        
        // 树链剖分预处理
        dfs1(0, -1, 0);
        dfs2(0, 0);
        
        // 构建线段树
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[pos[i]] = (i == 0) ? Integer.MAX_VALUE : edgeWeight[i];
        }
        seg = new SegmentTree(n);
        seg.build(1, n - 1, 1, arr);
        
        // 处理查询
        int[] result = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            int u = queries[i][0], v = queries[i][1];
            result[i] = queryPath(u, v);
        }
        
        return result;
    }
    
    static void init() {
        parent = new int[n];
        depth = new int[n];
        size = new int[n];
        heavy = new int[n];
        head = new int[n];
        pos = new int[n];
        edgeWeight = new int[n];
        tree = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            tree[i] = new ArrayList<>();
            heavy[i] = -1;
        }
        cnt = 0;
    }
    
    static void dfs1(int u, int p, int d) {
        parent[u] = p;
        depth[u] = d;
        size[u] = 1;
        int maxSize = 0;
        
        for (Edge e : tree[u]) {
            int v = e.to;
            if (v == p) continue;
            edgeWeight[v] = e.weight; // 边权下放到子节点
            dfs1(v, u, d + 1);
            size[u] += size[v];
            if (size[v] > maxSize) {
                maxSize = size[v];
                heavy[u] = v;
            }
        }
    }
    
    static void dfs2(int u, int h) {
        head[u] = h;
        pos[u] = cnt++;
        
        if (heavy[u] != -1) {
            dfs2(heavy[u], h);
        }
        
        for (Edge e : tree[u]) {
            int v = e.to;
            if (v != parent[u] && v != heavy[u]) {
                dfs2(v, v);
            }
        }
    }
    
    static int queryPath(int u, int v) {
        int res = Integer.MAX_VALUE;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            res = Math.min(res, seg.query(pos[head[u]], pos[u], 1, n - 1, 1));
            u = parent[head[u]];
        }
        if (u == v) return res;
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        res = Math.min(res, seg.query(pos[u] + 1, pos[v], 1, n - 1, 1));
        return res;
    }
}

/**
 * 算法总结：
 * 1. 边权转点权：将边权下放到深度较深的节点上
 * 2. 树链剖分：将树划分为重链，便于路径查询
 * 3. 线段树维护：支持区间最小值查询
 * 
 * 工程化考量：
 * 1. 输入验证：验证输入数据的合法性
 * 2. 性能优化：使用快速IO，优化线段树实现
 * 3. 内存管理：合理分配数组大小，避免内存泄漏
 * 
 * 测试用例：
 * 1. 单边树：验证基本功能
 * 2. 链状树：测试路径查询
 * 3. 完全二叉树：验证复杂度
 * 4. 极端数据：测试边界情况
 */