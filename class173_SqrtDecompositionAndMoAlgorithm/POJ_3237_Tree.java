/**
 * POJ 3237 - Tree - Java实现
 * 
 * 题目描述：
 * 给定一棵树，每条边有一个权值。支持以下操作：
 * 1. CHANGE i v：将第i条边的权值改为v
 * 2. NEGATE a b：将a到b路径上所有边的权值取反
 * 3. QUERY a b：查询a到b路径上边的最大权值
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 路径更新：O(log²n)
 * - 路径查询：O(log²n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：http://poj.org/problem?id=3237
 */

import java.util.*;
import java.io.*;

public class POJ_3237_Tree {
    static int n;
    static List<Edge>[] tree;
    static Edge[] edges;
    
    // 树链剖分相关数组
    static int[] parent, depth, size, heavy, head, pos;
    static int curPos;
    
    // 线段树相关（支持取反操作）
    static int[] segMax, segMin, lazy;
    static int[] arr;
    
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
        PrintWriter pw = new PrintWriter(System.out);
        
        int t = Integer.parseInt(br.readLine());
        while (t-- > 0) {
            n = Integer.parseInt(br.readLine());
            
            tree = new ArrayList[n + 1];
            edges = new Edge[n];
            for (int i = 0; i <= n; i++) {
                tree[i] = new ArrayList<>();
            }
            
            for (int i = 1; i < n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                int w = Integer.parseInt(st.nextToken());
                
                edges[i] = new Edge(u, v, w, i);
                tree[u].add(new Edge(u, v, w, i));
                tree[v].add(new Edge(v, u, w, i));
            }
            
            initHLD();
            
            while (true) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                String op = st.nextToken();
                if (op.equals("DONE")) break;
                
                if (op.equals("CHANGE")) {
                    int i = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    changeEdge(i, v);
                } else if (op.equals("NEGATE")) {
                    int a = Integer.parseInt(st.nextToken());
                    int b = Integer.parseInt(st.nextToken());
                    negatePath(a, b);
                } else if (op.equals("QUERY")) {
                    int a = Integer.parseInt(st.nextToken());
                    int b = Integer.parseInt(st.nextToken());
                    pw.println(queryPathMax(a, b));
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
        curPos = 0;
        
        segMax = new int[4 * n];
        segMin = new int[4 * n];
        lazy = new int[4 * n]; // 0: 无操作, 1: 需要取反
        arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 将边权设置到线段树中
        for (int i = 1; i < n; i++) {
            Edge e = edges[i];
            int node = (depth[e.u] > depth[e.v]) ? e.u : e.v;
            arr[pos[node]] = e.w;
        }
        buildSegTree(1, 0, n - 1);
    }
    
    static void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        
        int maxSize = 0;
        for (Edge e : tree[u]) {
            int v = e.v;
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
        
        for (Edge e : tree[u]) {
            int v = e.v;
            if (v == parent[u] || v == heavy[u]) continue;
            dfs2(v, v);
        }
    }
    
    static void buildSegTree(int idx, int l, int r) {
        if (l == r) {
            segMax[idx] = arr[l];
            segMin[idx] = arr[l];
            return;
        }
        int mid = (l + r) / 2;
        buildSegTree(2 * idx, l, mid);
        buildSegTree(2 * idx + 1, mid + 1, r);
        segMax[idx] = Math.max(segMax[2 * idx], segMax[2 * idx + 1]);
        segMin[idx] = Math.min(segMin[2 * idx], segMin[2 * idx + 1]);
    }
    
    static void changeEdge(int edgeId, int newVal) {
        Edge e = edges[edgeId];
        int node = (depth[e.u] > depth[e.v]) ? e.u : e.v;
        updateSegTree(1, 0, n - 1, pos[node], pos[node], newVal);
    }
    
    static void negatePath(int u, int v) {
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            negateSegTree(1, 0, n - 1, pos[head[u]], pos[u]);
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        if (u != v) {
            negateSegTree(1, 0, n - 1, pos[u] + 1, pos[v]);
        }
    }
    
    static int queryPathMax(int u, int v) {
        int maxVal = Integer.MIN_VALUE;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            maxVal = Math.max(maxVal, querySegTreeMax(1, 0, n - 1, pos[head[u]], pos[u]));
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        if (u != v) {
            maxVal = Math.max(maxVal, querySegTreeMax(1, 0, n - 1, pos[u] + 1, pos[v]));
        }
        return maxVal;
    }
    
    static void updateSegTree(int idx, int segL, int segR, int l, int r, int val) {
        pushDown(idx, segL, segR);
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            segMax[idx] = val;
            segMin[idx] = val;
            return;
        }
        int mid = (segL + segR) / 2;
        updateSegTree(2 * idx, segL, mid, l, r, val);
        updateSegTree(2 * idx + 1, mid + 1, segR, l, r, val);
        segMax[idx] = Math.max(segMax[2 * idx], segMax[2 * idx + 1]);
        segMin[idx] = Math.min(segMin[2 * idx], segMin[2 * idx + 1]);
    }
    
    static void negateSegTree(int idx, int segL, int segR, int l, int r) {
        pushDown(idx, segL, segR);
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            // 取反操作：最大值变最小值取负，最小值变最大值取负
            int tempMax = segMax[idx];
            int tempMin = segMin[idx];
            segMax[idx] = -tempMin;
            segMin[idx] = -tempMax;
            if (segL != segR) {
                lazy[2 * idx] ^= 1;
                lazy[2 * idx + 1] ^= 1;
            }
            return;
        }
        int mid = (segL + segR) / 2;
        negateSegTree(2 * idx, segL, mid, l, r);
        negateSegTree(2 * idx + 1, mid + 1, segR, l, r);
        segMax[idx] = Math.max(segMax[2 * idx], segMax[2 * idx + 1]);
        segMin[idx] = Math.min(segMin[2 * idx], segMin[2 * idx + 1]);
    }
    
    static int querySegTreeMax(int idx, int segL, int segR, int l, int r) {
        pushDown(idx, segL, segR);
        if (l > segR || r < segL) return Integer.MIN_VALUE;
        if (l <= segL && segR <= r) {
            return segMax[idx];
        }
        int mid = (segL + segR) / 2;
        int leftRes = querySegTreeMax(2 * idx, segL, mid, l, r);
        int rightRes = querySegTreeMax(2 * idx + 1, mid + 1, segR, l, r);
        return Math.max(leftRes, rightRes);
    }
    
    static void pushDown(int idx, int l, int r) {
        if (lazy[idx] != 0) {
            if (lazy[idx] == 1) {
                int tempMax = segMax[idx];
                int tempMin = segMin[idx];
                segMax[idx] = -tempMin;
                segMin[idx] = -tempMax;
            }
            if (l != r) {
                lazy[2 * idx] ^= lazy[idx];
                lazy[2 * idx + 1] ^= lazy[idx];
            }
            lazy[idx] = 0;
        }
    }
}