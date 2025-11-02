/**
 * SPOJ QTREE2 - Query on a tree II - Java实现
 * 
 * 题目描述：
 * 给定一棵树，每条边有一个权值。支持以下操作：
 * 1. DIST a b：查询a到b路径上的边权和
 * 2. KTH a b k：查询a到b路径上的第k个节点
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - DIST查询：O(log²n)
 * - KTH查询：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://www.spoj.com/problems/QTREE2/
 */

import java.util.*;
import java.io.*;

public class SPOJ_QTREE2_QueryOnTree2 {
    static int n;
    static List<Edge>[] tree;
    
    // 树链剖分相关数组
    static int[] parent, depth, size, heavy, head, pos, dist;
    static int curPos;
    
    // 线段树相关
    static int[] segTree;
    static int[] arr;
    
    static class Edge {
        int v, w;
        Edge(int v, int w) {
            this.v = v;
            this.w = w;
        }
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        int t = Integer.parseInt(br.readLine());
        while (t-- > 0) {
            n = Integer.parseInt(br.readLine());
            
            tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                tree[i] = new ArrayList<>();
            }
            
            for (int i = 1; i < n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                int w = Integer.parseInt(st.nextToken());
                
                tree[u].add(new Edge(v, w));
                tree[v].add(new Edge(u, w));
            }
            
            initHLD();
            
            while (true) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                String op = st.nextToken();
                if (op.equals("DONE")) break;
                
                if (op.equals("DIST")) {
                    int a = Integer.parseInt(st.nextToken());
                    int b = Integer.parseInt(st.nextToken());
                    pw.println(queryDist(a, b));
                } else if (op.equals("KTH")) {
                    int a = Integer.parseInt(st.nextToken());
                    int b = Integer.parseInt(st.nextToken());
                    int k = Integer.parseInt(st.nextToken());
                    pw.println(queryKth(a, b, k));
                }
            }
        }
        
        pw.flush();
    }
    
    static void initHLD() {
        parent = new int[n + 1];
        depth = new int[n + 1];
        size = new int[n + 1];
        heavy = new int[n + 1];
        head = new int[n + 1];
        pos = new int[n + 1];
        dist = new int[n + 1];
        curPos = 0;
        
        segTree = new int[4 * n];
        arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        dfs1(1, 0, 0);
        dfs2(1, 1);
        
        buildSegTree(1, 0, n - 1);
    }
    
    static void dfs1(int u, int p, int d) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        dist[u] = d;
        size[u] = 1;
        
        int maxSize = 0;
        for (Edge e : tree[u]) {
            int v = e.v;
            if (v == p) continue;
            dfs1(v, u, d + e.w);
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
        
        for (Edge e : tree[u]) {
            int v = e.v;
            if (v == parent[u] || v == heavy[u]) continue;
            dfs2(v, v);
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
        segTree[idx] = segTree[2 * idx] + segTree[2 * idx + 1];
    }
    
    static int queryDist(int u, int v) {
        int res = 0;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            res += querySegTree(1, 0, n - 1, pos[head[u]], pos[u]);
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        if (u != v) {
            res += querySegTree(1, 0, n - 1, pos[u] + 1, pos[v]);
        }
        return res;
    }
    
    static int queryKth(int u, int v, int k) {
        int lca = getLCA(u, v);
        int dist1 = depth[u] - depth[lca] + 1;
        
        if (k <= dist1) {
            // 第k个节点在u到lca的路径上
            return findKthOnPath(u, lca, k);
        } else {
            // 第k个节点在lca到v的路径上
            int dist2 = depth[v] - depth[lca];
            int kFromV = dist2 - (k - dist1) + 1;
            return findKthOnPath(v, lca, kFromV);
        }
    }
    
    static int getLCA(int u, int v) {
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                v = parent[head[v]];
            } else {
                u = parent[head[u]];
            }
        }
        return depth[u] < depth[v] ? u : v;
    }
    
    static int findKthOnPath(int u, int ancestor, int k) {
        while (true) {
            if (head[u] == head[ancestor]) {
                // 在同一条重链上
                int posU = pos[u];
                int posAncestor = pos[ancestor];
                int distOnChain = posU - posAncestor + 1;
                
                if (k <= distOnChain) {
                    // 第k个节点在当前链上
                    return getNodeByPos(posAncestor + k - 1);
                } else {
                    k -= distOnChain;
                    u = parent[head[u]];
                }
            } else {
                int distOnChain = depth[u] - depth[head[u]] + 1;
                if (k <= distOnChain) {
                    // 第k个节点在当前链上
                    return getNodeByPos(pos[u] - k + 1);
                } else {
                    k -= distOnChain;
                    u = parent[head[u]];
                }
            }
        }
    }
    
    static int getNodeByPos(int pos) {
        // 通过位置找到对应的节点（需要维护反向映射）
        for (int i = 1; i <= n; i++) {
            if (this.pos[i] == pos) {
                return i;
            }
        }
        return -1;
    }
    
    static int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (l > segR || r < segL) return 0;
        if (l <= segL && segR <= r) {
            return segTree[idx];
        }
        int mid = (segL + segR) / 2;
        int leftRes = querySegTree(2 * idx, segL, mid, l, r);
        int rightRes = querySegTree(2 * idx + 1, mid + 1, segR, l, r);
        return leftRes + rightRes;
    }
}