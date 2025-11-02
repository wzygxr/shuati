import java.io.*;
import java.util.*;

/**
 * Code27: P4556 RainyTail Advanced (线段树合并 + 树上差分 + 权值线段树)
 * 题目：给定一棵树，每条边有颜色，多次询问路径(u,v)上出现次数最多的颜色
 * 时间复杂度：O((n+q)logn)
 */
public class Code27_P4556_RainyTail_Advanced {
    
    static class Edge {
        int to, color;
        Edge(int to, int color) {
            this.to = to;
            this.color = color;
        }
    }
    
    static class Query {
        int u, v, lca, ansColor, ansCount;
        Query(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }
    
    static class SegmentTreeNode {
        int l, r;
        int maxColor, maxCount;
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int l, int r) {
            this.l = l;
            this.r = r;
            this.maxColor = 0;
            this.maxCount = 0;
        }
    }
    
    static int n, m, maxColor = 100000;
    static List<Edge>[] graph;
    static List<Query> queries;
    static int[] depth;
    static int[][] parent;
    static SegmentTreeNode[] roots;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();
        
        for (int i = 1; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            int c = Integer.parseInt(st.nextToken());
            graph[u].add(new Edge(v, c));
            graph[v].add(new Edge(u, c));
        }
        
        // 预处理LCA
        depth = new int[n + 1];
        parent = new int[n + 1][20];
        roots = new SegmentTreeNode[n + 1];
        
        dfsLCA(1, 0);
        
        queries = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            Query q = new Query(u, v);
            q.lca = getLCA(u, v);
            queries.add(q);
        }
        
        // 线段树合并处理
        dfsMerge(1, 0);
        
        // 输出答案
        for (Query q : queries) {
            pw.println(q.ansColor + " " + q.ansCount);
        }
        
        pw.flush();
        pw.close();
    }
    
    static void dfsLCA(int u, int p) {
        depth[u] = depth[p] + 1;
        parent[u][0] = p;
        for (int i = 1; i < 20; i++) {
            parent[u][i] = parent[parent[u][i - 1]][i - 1];
        }
        for (Edge e : graph[u]) {
            if (e.to != p) {
                dfsLCA(e.to, u);
            }
        }
    }
    
    static int getLCA(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        for (int i = 19; i >= 0; i--) {
            if (depth[u] - (1 << i) >= depth[v]) {
                u = parent[u][i];
            }
        }
        
        if (u == v) return u;
        
        for (int i = 19; i >= 0; i--) {
            if (parent[u][i] != parent[v][i]) {
                u = parent[u][i];
                v = parent[v][i];
            }
        }
        return parent[u][0];
    }
    
    static void update(SegmentTreeNode node, int color, int delta) {
        if (node.l == node.r) {
            // 叶子节点
            if (node.maxCount < delta) {
                node.maxCount = delta;
                node.maxColor = color;
            } else if (node.maxCount == delta && color < node.maxColor) {
                node.maxColor = color;
            }
            return;
        }
        
        int mid = (node.l + node.r) >> 1;
        if (color <= mid) {
            if (node.left == null) node.left = new SegmentTreeNode(node.l, mid);
            update(node.left, color, delta);
        } else {
            if (node.right == null) node.right = new SegmentTreeNode(mid + 1, node.r);
            update(node.right, color, delta);
        }
        
        // 更新最大值
        node.maxCount = 0;
        node.maxColor = 0;
        if (node.left != null) {
            if (node.left.maxCount > node.maxCount) {
                node.maxCount = node.left.maxCount;
                node.maxColor = node.left.maxColor;
            }
        }
        if (node.right != null) {
            if (node.right.maxCount > node.maxCount) {
                node.maxCount = node.right.maxCount;
                node.maxColor = node.right.maxColor;
            } else if (node.right.maxCount == node.maxCount && node.right.maxColor < node.maxColor) {
                node.maxColor = node.right.maxColor;
            }
        }
    }
    
    static SegmentTreeNode merge(SegmentTreeNode a, SegmentTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        if (a.l == a.r) {
            // 叶子节点直接合并
            a.maxCount += b.maxCount;
            return a;
        }
        
        a.left = merge(a.left, b.left);
        a.right = merge(a.right, b.right);
        
        // 更新最大值
        a.maxCount = 0;
        a.maxColor = 0;
        if (a.left != null) {
            if (a.left.maxCount > a.maxCount) {
                a.maxCount = a.left.maxCount;
                a.maxColor = a.left.maxColor;
            }
        }
        if (a.right != null) {
            if (a.right.maxCount > a.maxCount) {
                a.maxCount = a.right.maxCount;
                a.maxColor = a.right.maxColor;
            } else if (a.right.maxCount == a.maxCount && a.right.maxColor < a.maxColor) {
                a.maxColor = a.right.maxColor;
            }
        }
        
        return a;
    }
    
    static void dfsMerge(int u, int p) {
        roots[u] = new SegmentTreeNode(1, maxColor);
        
        for (Edge e : graph[u]) {
            if (e.to == p) continue;
            
            dfsMerge(e.to, u);
            
            // 在子节点线段树中添加当前边的颜色
            update(roots[e.to], e.color, 1);
            
            // 合并子节点线段树到当前节点
            roots[u] = merge(roots[u], roots[e.to]);
        }
        
        // 处理查询
        for (Query q : queries) {
            if (q.lca == u) {
                // 查询路径上的最大值
                SegmentTreeNode node = roots[u];
                if (node != null && node.maxCount > 0) {
                    q.ansColor = node.maxColor;
                    q.ansCount = node.maxCount;
                } else {
                    q.ansColor = 0;
                    q.ansCount = 0;
                }
            }
        }
    }
}