/**
 * SPOJ QTREE - Query on a tree - Java实现
 * 
 * 题目描述：
 * 给定一棵树，每条边有一个权值。支持两种操作：
 * 1. CHANGE i ti：将第i条边的权值改为ti
 * 2. QUERY a b：询问a到b路径上的最大边权
 * 
 * 输入格式：
 * 第一行：T（测试用例数量）
 * 每个测试用例：
 * - 第一行：n（节点数量）
 * - 接下来n-1行：每行三个整数a, b, c，表示a和b之间有一条边，权值为c
 * - 接下来若干行操作，直到遇到"DONE"结束
 * 
 * 输出格式：
 * 对于每个QUERY操作，输出路径上的最大边权
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 查询/更新：O(log²n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://www.spoj.com/problems/QTREE/
 */

import java.util.*;
import java.io.*;

public class SPOJ_QTREE_QueryOnTree {
    static int n;
    static List<Edge>[] tree;
    static Edge[] edges; // 存储所有边
    
    // 树链剖分相关
    static int[] parent, depth, size, heavy, head, pos;
    static int curPos;
    
    // 线段树相关（维护最大值）
    static int[] segTree;
    static int[] arr; // 存储边权（映射到节点）
    
    static class Edge {
        int u, v, w, id;
        Edge(int u, int v, int w, int id) {
            this.u = u;
            this.v = v;
            this.w = w;
            this.id = id;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int T = Integer.parseInt(br.readLine().trim());
        
        while (T-- > 0) {
            n = Integer.parseInt(br.readLine().trim());
            
            tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                tree[i] = new ArrayList<>();
            }
            
            edges = new Edge[n]; // 边从1到n-1编号
            
            // 读取边
            for (int i = 1; i < n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                int w = Integer.parseInt(st.nextToken());
                
                Edge edge = new Edge(u, v, w, i);
                edges[i] = edge;
                tree[u].add(edge);
                tree[v].add(edge);
            }
            
            // 初始化树链剖分
            initHLD();
            
            // 处理操作
            while (true) {
                String line = br.readLine().trim();
                if (line.equals("DONE")) break;
                
                StringTokenizer st = new StringTokenizer(line);
                String op = st.nextToken();
                
                if (op.equals("QUERY")) {
                    int a = Integer.parseInt(st.nextToken());
                    int b = Integer.parseInt(st.nextToken());
                    int res = queryPath(a, b);
                    System.out.println(res);
                } else if (op.equals("CHANGE")) {
                    int i = Integer.parseInt(st.nextToken());
                    int ti = Integer.parseInt(st.nextToken());
                    updateEdge(i, ti);
                }
            }
        }
    }
    
    static void initHLD() {
        parent = new int[n + 1];
        depth = new int[n + 1];
        size = new int[n + 1];
        heavy = new int[n + 1];
        head = new int[n + 1];
        pos = new int[n + 1];
        curPos = 0;
        
        segTree = new int[4 * n];
        arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        // 以1为根节点
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 将边权映射到节点
        mapEdgesToNodes();
        buildSegTree(1, 0, n - 1);
    }
    
    static void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        
        int maxSize = 0;
        for (Edge edge : tree[u]) {
            int v = (edge.u == u) ? edge.v : edge.u;
            if (v == p) continue;
            dfs1(v, u);
            size[u] += size[v];
            if (size[v] > maxSize) {
                maxSize = size[v];
                heavy[u] = v;
            }
        }
    }
    
    static void dfs2(int u, int h) {
        head[u] = h;
        pos[u] = curPos++;
        
        if (heavy[u] != -1) {
            dfs2(heavy[u], h);
        }
        
        for (Edge edge : tree[u]) {
            int v = (edge.u == u) ? edge.v : edge.u;
            if (v == parent[u] || v == heavy[u]) continue;
            dfs2(v, v);
        }
    }
    
    static void mapEdgesToNodes() {
        // 将边权映射到深度较大的节点
        for (int i = 1; i < n; i++) {
            Edge edge = edges[i];
            int u = edge.u, v = edge.v;
            // 将边权赋给深度较大的节点
            if (depth[u] > depth[v]) {
                arr[pos[u]] = edge.w;
            } else {
                arr[pos[v]] = edge.w;
            }
        }
    }
    
    static void buildSegTree(int idx, int l, int r) {
        if (l == r) {
            segTree[idx] = arr[l];
            return;
        }
        int mid = (l + r) / 2;
        buildSegTree(2 * idx, l, mid);
        buildSegTree(2 * idx + 1, mid + 1, r);
        segTree[idx] = Math.max(segTree[2 * idx], segTree[2 * idx + 1]);
    }
    
    static void updateEdge(int edgeId, int newVal) {
        Edge edge = edges[edgeId];
        int u = edge.u, v = edge.v;
        
        // 找到深度较大的节点
        int nodeToUpdate;
        if (depth[u] > depth[v]) {
            nodeToUpdate = u;
        } else {
            nodeToUpdate = v;
        }
        
        // 更新线段树
        updateSegTree(1, 0, n - 1, pos[nodeToUpdate], newVal);
    }
    
    static int queryPath(int u, int v) {
        int res = 0;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            res = Math.max(res, querySegTree(1, 0, n - 1, pos[head[u]], pos[u]));
            u = parent[head[u]];
        }
        if (u == v) return res; // 如果u和v相同，直接返回
        
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        // 注意：这里查询的是u的儿子到v的路径（因为边权映射在深度较大的节点）
        res = Math.max(res, querySegTree(1, 0, n - 1, pos[u] + 1, pos[v]));
        return res;
    }
    
    static void updateSegTree(int idx, int segL, int segR, int pos, int val) {
        if (segL == segR) {
            segTree[idx] = val;
            arr[pos] = val;
            return;
        }
        int mid = (segL + segR) / 2;
        if (pos <= mid) {
            updateSegTree(2 * idx, segL, mid, pos, val);
        } else {
            updateSegTree(2 * idx + 1, mid + 1, segR, pos, val);
        }
        segTree[idx] = Math.max(segTree[2 * idx], segTree[2 * idx + 1]);
    }
    
    static int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (l > segR || r < segL) return 0;
        if (l <= segL && segR <= r) {
            return segTree[idx];
        }
        int mid = (segL + segR) / 2;
        int leftRes = querySegTree(2 * idx, segL, mid, l, r);
        int rightRes = querySegTree(2 * idx + 1, mid + 1, segR, l, r);
        return Math.max(leftRes, rightRes);
    }
}