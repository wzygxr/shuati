/**
 * HDU 3966 - Aragorn's Story - Java实现
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个初始权值。支持以下操作：
 * 1. I u v k：将节点u到v路径上所有节点权值增加k
 * 2. D u v k：将节点u到v路径上所有节点权值减少k
 * 3. Q u：查询节点u的权值
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 路径更新：O(log²n)
 * - 单点查询：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3966
 */

import java.util.*;
import java.io.*;

public class HDU_3966_AragornsStory {
    static int n, m, p;
    static int[] w; // 节点初始权值
    static List<Integer>[] tree;
    
    // 树链剖分相关数组
    static int[] parent, depth, size, heavy, head, pos;
    static int curPos;
    
    // 线段树相关
    static int[] segTree, lazy;
    static int[] arr;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            n = Integer.parseInt(st.nextToken());
            m = Integer.parseInt(st.nextToken());
            p = Integer.parseInt(st.nextToken());
            
            w = new int[n + 1];
            st = new StringTokenizer(br.readLine());
            for (int i = 1; i <= n; i++) {
                w[i] = Integer.parseInt(st.nextToken());
            }
            
            tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                tree[i] = new ArrayList<>();
            }
            
            for (int i = 1; i < n; i++) {
                st = new StringTokenizer(br.readLine());
                int u = Integer.parseInt(st.nextToken());
                int v = Integer.parseInt(st.nextToken());
                tree[u].add(v);
                tree[v].add(u);
            }
            
            initHLD();
            
            for (int i = 0; i < p; i++) {
                st = new StringTokenizer(br.readLine());
                String op = st.nextToken();
                
                if (op.equals("I")) {
                    int u = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    int k = Integer.parseInt(st.nextToken());
                    updatePath(u, v, k);
                } else if (op.equals("D")) {
                    int u = Integer.parseInt(st.nextToken());
                    int v = Integer.parseInt(st.nextToken());
                    int k = Integer.parseInt(st.nextToken());
                    updatePath(u, v, -k);
                } else if (op.equals("Q")) {
                    int u = Integer.parseInt(st.nextToken());
                    pw.println(queryPoint(u));
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
        
        segTree = new int[4 * n];
        lazy = new int[4 * n];
        arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        dfs1(1, 0);
        dfs2(1, 1);
        
        // 将初始权值设置到线段树中
        for (int i = 1; i <= n; i++) {
            arr[pos[i]] = w[i];
        }
        buildSegTree(1, 0, n - 1);
    }
    
    static void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        
        int maxSize = 0;
        for (int v : tree[u]) {
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
        
        for (int v : tree[u]) {
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
    
    static void updatePath(int u, int v, int val) {
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            updateSegTree(1, 0, n - 1, pos[head[u]], pos[u], val);
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        updateSegTree(1, 0, n - 1, pos[u], pos[v], val);
    }
    
    static int queryPoint(int u) {
        return querySegTree(1, 0, n - 1, pos[u], pos[u]);
    }
    
    static void updateSegTree(int idx, int segL, int segR, int l, int r, int val) {
        if (lazy[idx] != 0) {
            segTree[idx] += lazy[idx] * (segR - segL + 1);
            if (segL != segR) {
                lazy[2 * idx] += lazy[idx];
                lazy[2 * idx + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            segTree[idx] += val * (segR - segL + 1);
            if (segL != segR) {
                lazy[2 * idx] += val;
                lazy[2 * idx + 1] += val;
            }
            return;
        }
        int mid = (segL + segR) / 2;
        updateSegTree(2 * idx, segL, mid, l, r, val);
        updateSegTree(2 * idx + 1, mid + 1, segR, l, r, val);
        segTree[idx] = segTree[2 * idx] + segTree[2 * idx + 1];
    }
    
    static int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (lazy[idx] != 0) {
            segTree[idx] += lazy[idx] * (segR - segL + 1);
            if (segL != segR) {
                lazy[2 * idx] += lazy[idx];
                lazy[2 * idx + 1] += lazy[idx];
            }
            lazy[idx] = 0;
        }
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