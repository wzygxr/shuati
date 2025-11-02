/**
 * Codeforces 343D - Water Tree - Java实现
 * 
 * 题目描述：
 * 给定一棵树，支持以下操作：
 * 1. 将节点v及其所有祖先节点填满水
 * 2. 将节点v及其子树清空水
 * 3. 查询节点v是否有水
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 操作1：O(log²n)
 * - 操作2：O(log n)
 * - 操作3：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://codeforces.com/problemset/problem/343/D
 */

import java.util.*;
import java.io.*;

public class Codeforces_343D_WaterTree {
    static int n, m;
    static List<Integer>[] tree;
    
    // 树链剖分相关数组
    static int[] parent, depth, size, heavy, head, pos, inTime, outTime;
    static int curPos, timer;
    
    // 线段树用于记录填水时间
    static int[] segTree, lazy;
    static int[] fillTime; // 记录每个节点最后一次被填水的时间
    static int[] emptyTime; // 记录每个节点最后一次被清空的时间
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        n = Integer.parseInt(br.readLine());
        tree = new ArrayList[n + 1];
        for (int i = 0; i <= n; i++) {
            tree[i] = new ArrayList<>();
        }
        
        for (int i = 1; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            tree[u].add(v);
            tree[v].add(u);
        }
        
        initHLD();
        
        m = Integer.parseInt(br.readLine());
        for (int i = 1; i <= m; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            
            if (op == 1) {
                // 填满节点v及其所有祖先
                fillWater(v, i);
            } else if (op == 2) {
                // 清空节点v及其子树
                emptyWater(v, i);
            } else {
                // 查询节点v是否有水
                pw.println(queryWater(v) ? "1" : "0");
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
        inTime = new int[n + 1];
        outTime = new int[n + 1];
        curPos = 0;
        timer = 0;
        
        segTree = new int[4 * n];
        lazy = new int[4 * n];
        fillTime = new int[n + 1];
        emptyTime = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        dfs1(1, 0);
        dfs2(1, 1);
        
        buildSegTree(1, 0, n - 1);
    }
    
    static void dfs1(int u, int p) {
        parent[u] = p;
        depth[u] = depth[p] + 1;
        size[u] = 1;
        inTime[u] = timer++;
        
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
        outTime[u] = timer - 1;
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
            segTree[idx] = 0;
            return;
        }
        int mid = (l + r) / 2;
        buildSegTree(2 * idx, l, mid);
        buildSegTree(2 * idx + 1, mid + 1, r);
        segTree[idx] = Math.max(segTree[2 * idx], segTree[2 * idx + 1]);
    }
    
    static void fillWater(int v, int time) {
        // 填满节点v及其所有祖先
        while (v != 0) {
            updateSegTree(1, 0, n - 1, pos[v], pos[v], time);
            v = parent[v];
        }
    }
    
    static void emptyWater(int v, int time) {
        // 清空节点v及其子树
        emptyTime[v] = time;
        updateSubtree(v, 0);
    }
    
    static boolean queryWater(int v) {
        // 查询节点v是否有水
        int lastFill = queryPathMax(v);
        int lastEmpty = emptyTime[v];
        
        // 如果最后一次填水时间晚于最后一次清空时间，则有水
        return lastFill > lastEmpty;
    }
    
    static int queryPathMax(int v) {
        int maxTime = 0;
        while (v != 0) {
            maxTime = Math.max(maxTime, querySegTree(1, 0, n - 1, pos[v], pos[v]));
            v = parent[v];
        }
        return maxTime;
    }
    
    static void updateSegTree(int idx, int segL, int segR, int l, int r, int val) {
        if (lazy[idx] != 0) {
            segTree[idx] = Math.max(segTree[idx], lazy[idx]);
            if (segL != segR) {
                lazy[2 * idx] = Math.max(lazy[2 * idx], lazy[idx]);
                lazy[2 * idx + 1] = Math.max(lazy[2 * idx + 1], lazy[idx]);
            }
            lazy[idx] = 0;
        }
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            segTree[idx] = Math.max(segTree[idx], val);
            if (segL != segR) {
                lazy[2 * idx] = Math.max(lazy[2 * idx], val);
                lazy[2 * idx + 1] = Math.max(lazy[2 * idx + 1], val);
            }
            return;
        }
        int mid = (segL + segR) / 2;
        updateSegTree(2 * idx, segL, mid, l, r, val);
        updateSegTree(2 * idx + 1, mid + 1, segR, l, r, val);
        segTree[idx] = Math.max(segTree[2 * idx], segTree[2 * idx + 1]);
    }
    
    static void updateSubtree(int u, int val) {
        updateSegTree(1, 0, n - 1, pos[u], pos[u] + size[u] - 1, val);
    }
    
    static int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (lazy[idx] != 0) {
            segTree[idx] = Math.max(segTree[idx], lazy[idx]);
            if (segL != segR) {
                lazy[2 * idx] = Math.max(lazy[2 * idx], lazy[idx]);
                lazy[2 * idx + 1] = Math.max(lazy[2 * idx + 1], lazy[idx]);
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
        return Math.max(leftRes, rightRes);
    }
}