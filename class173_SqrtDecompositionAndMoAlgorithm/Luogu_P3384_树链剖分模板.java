/**
 * 洛谷 P3384 【模板】轻重链剖分 - Java实现
 * 
 * 题目描述：
 * 给定一棵包含 n 个节点的树，每个节点有一个初始权值。
 * 支持以下操作：
 * 1. 将树从 x 到 y 节点路径上所有节点的权值加上 z
 * 2. 求树从 x 到 y 节点路径上所有节点的权值和
 * 3. 将以 x 为根节点的子树内所有节点权值加上 z
 * 4. 求以 x 为根节点的子树内所有节点权值和
 * 
 * 输入格式：
 * 第一行包含 4 个正整数 n, m, r, p，分别表示树的节点个数、操作个数、根节点序号和取模数
 * 第二行包含 n 个非负整数，表示每个节点的初始权值
 * 接下来 n-1 行每行包含两个整数 x, y，表示点 x 和点 y 之间有一条边
 * 接下来 m 行每行包含若干个正整数，每行表示一个操作
 * 
 * 输出格式：
 * 对于每个操作 2 和 4，输出一行一个整数表示答案
 * 
 * 时间复杂度：
 * - 预处理：O(n)
 * - 路径查询/更新：O(log²n)
 * - 子树查询/更新：O(log n)
 * 
 * 空间复杂度：O(n)
 * 
 * 题目链接：https://www.luogu.com.cn/problem/P3384
 */

import java.util.*;
import java.io.*;

public class Luogu_P3384_树链剖分模板 {
    static int n, m, r, p;
    static int[] w; // 节点初始权值
    static List<Integer>[] tree; // 树的邻接表
    
    // 树链剖分相关数组
    static int[] parent, depth, size, heavy, head, pos;
    static int curPos;
    
    // 线段树相关
    static int[] segTree, lazy, arr;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        r = Integer.parseInt(st.nextToken());
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
        
        // 初始化树链剖分
        initHLD();
        
        // 处理操作
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            
            if (op == 1) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int z = Integer.parseInt(st.nextToken());
                updatePath(x, y, z);
            } else if (op == 2) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                int res = queryPath(x, y);
                System.out.println(res % p);
            } else if (op == 3) {
                int x = Integer.parseInt(st.nextToken());
                int z = Integer.parseInt(st.nextToken());
                updateSubtree(x, z);
            } else if (op == 4) {
                int x = Integer.parseInt(st.nextToken());
                int res = querySubtree(x);
                System.out.println(res % p);
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
        lazy = new int[4 * n];
        arr = new int[n + 1];
        
        Arrays.fill(heavy, -1);
        Arrays.fill(head, -1);
        
        dfs1(r, 0);
        dfs2(r, r);
        
        // 将初始权值设置到线段树中
        for (int i = 1; i <= n; i++) {
            arr[pos[i]] = w[i] % p;
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
        segTree[idx] = (segTree[2 * idx] + segTree[2 * idx + 1]) % p;
    }
    
    static void updatePath(int u, int v, int val) {
        val %= p;
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
    
    static int queryPath(int u, int v) {
        int res = 0;
        while (head[u] != head[v]) {
            if (depth[head[u]] < depth[head[v]]) {
                int temp = u;
                u = v;
                v = temp;
            }
            res = (res + querySegTree(1, 0, n - 1, pos[head[u]], pos[u])) % p;
            u = parent[head[u]];
        }
        if (depth[u] > depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        res = (res + querySegTree(1, 0, n - 1, pos[u], pos[v])) % p;
        return res;
    }
    
    static void updateSubtree(int u, int val) {
        val %= p;
        updateSegTree(1, 0, n - 1, pos[u], pos[u] + size[u] - 1, val);
    }
    
    static int querySubtree(int u) {
        return querySegTree(1, 0, n - 1, pos[u], pos[u] + size[u] - 1) % p;
    }
    
    static void updateSegTree(int idx, int segL, int segR, int l, int r, int val) {
        if (lazy[idx] != 0) {
            segTree[idx] = (int)((segTree[idx] + (long)lazy[idx] * (segR - segL + 1)) % p);
            if (segL != segR) {
                lazy[2 * idx] = (lazy[2 * idx] + lazy[idx]) % p;
                lazy[2 * idx + 1] = (lazy[2 * idx + 1] + lazy[idx]) % p;
            }
            lazy[idx] = 0;
        }
        if (l > segR || r < segL) return;
        if (l <= segL && segR <= r) {
            segTree[idx] = (int)((segTree[idx] + (long)val * (segR - segL + 1)) % p);
            if (segL != segR) {
                lazy[2 * idx] = (lazy[2 * idx] + val) % p;
                lazy[2 * idx + 1] = (lazy[2 * idx + 1] + val) % p;
            }
            return;
        }
        int mid = (segL + segR) / 2;
        updateSegTree(2 * idx, segL, mid, l, r, val);
        updateSegTree(2 * idx + 1, mid + 1, segR, l, r, val);
        segTree[idx] = (segTree[2 * idx] + segTree[2 * idx + 1]) % p;
    }
    
    static int querySegTree(int idx, int segL, int segR, int l, int r) {
        if (lazy[idx] != 0) {
            segTree[idx] = (int)((segTree[idx] + (long)lazy[idx] * (segR - segL + 1)) % p);
            if (segL != segR) {
                lazy[2 * idx] = (lazy[2 * idx] + lazy[idx]) % p;
                lazy[2 * idx + 1] = (lazy[2 * idx + 1] + lazy[idx]) % p;
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
        return (leftRes + rightRes) % p;
    }
}