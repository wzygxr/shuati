// 可持久化线段树分裂模板

import java.io.*;
import java.util.*;

/**
 * 可持久化线段树分裂 - 支持历史版本查询
 * 
 * 问题描述：
 * 在线段树分裂的基础上，支持历史版本查询和回溯
 * 
 * 核心算法：可持久化线段树 + 线段树分裂
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */

public class Code32_PersistentSegmentTreeSplit {
    static class FastIO {
        BufferedReader br;
        StringTokenizer st;
        PrintWriter out;
        
        public FastIO() {
            br = new BufferedReader(new InputStreamReader(System.in));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));
        }
        
        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }
        
        int nextInt() { return Integer.parseInt(next()); }
        long nextLong() { return Long.parseLong(next()); }
        
        void println(Object obj) { out.println(obj); }
        void close() { out.close(); }
    }
    
    static final int MAXN = 100010;
    static final int MAXM = 20000000;
    
    // 可持久化线段树节点
    static class PersistentNode {
        int l, r;
        long sum;
        int version; // 版本号
        
        PersistentNode() {
            l = r = -1;
            sum = 0;
            version = 0;
        }
    }
    
    static PersistentNode[] tree = new PersistentNode[MAXM];
    static int cnt = 0;
    static int currentVersion = 0;
    static int[] roots = new int[MAXN]; // 各版本的根节点
    
    static {
        for (int i = 0; i < MAXM; i++) {
            tree[i] = new PersistentNode();
        }
    }
    
    // 创建新节点
    static int newNode(int version) {
        if (cnt >= MAXM) {
            System.gc();
            return -1;
        }
        tree[cnt].l = tree[cnt].r = -1;
        tree[cnt].sum = 0;
        tree[cnt].version = version;
        return cnt++;
    }
    
    // 复制节点（可持久化核心）
    static int copyNode(int src, int version) {
        if (src == -1) return -1;
        
        int newIdx = newNode(version);
        tree[newIdx].l = tree[src].l;
        tree[newIdx].r = tree[src].r;
        tree[newIdx].sum = tree[src].sum;
        
        return newIdx;
    }
    
    // 构建初始线段树
    static int buildTree(int l, int r, long[] arr, int version) {
        int rt = newNode(version);
        
        if (l == r) {
            tree[rt].sum = arr[l];
            return rt;
        }
        
        int mid = (l + r) >> 1;
        
        tree[rt].l = buildTree(l, mid, arr, version);
        tree[rt].r = buildTree(mid + 1, r, arr, version);
        
        tree[rt].sum = tree[tree[rt].l].sum + tree[tree[rt].r].sum;
        
        return rt;
    }
    
    // 可持久化线段树分裂
    static int[] persistentSplit(int rt, int l, int r, int pos, int version) {
        if (rt == -1) return new int[]{-1, -1};
        
        // 创建新版本的节点
        int newRt = copyNode(rt, version);
        
        if (l == r) {
            // 叶子节点分裂
            int splitNode = newNode(version);
            tree[splitNode].sum = tree[newRt].sum;
            tree[newRt].sum = 0;
            
            return new int[]{newRt, splitNode};
        }
        
        int mid = (l + r) >> 1;
        
        if (pos <= mid) {
            // 分裂左子树
            int[] leftSplit = persistentSplit(tree[newRt].l, l, mid, pos, version);
            tree[newRt].l = leftSplit[0];
            
            int splitRoot = newNode(version);
            tree[splitRoot].l = leftSplit[1];
            tree[splitRoot].r = tree[newRt].r;
            
            // 更新和
            tree[newRt].sum = (tree[newRt].l != -1 ? tree[tree[newRt].l].sum : 0) + 
                            (tree[newRt].r != -1 ? tree[tree[newRt].r].sum : 0);
            tree[splitRoot].sum = (tree[splitRoot].l != -1 ? tree[tree[splitRoot].l].sum : 0) + 
                                (tree[splitRoot].r != -1 ? tree[tree[splitRoot].r].sum : 0);
            
            return new int[]{newRt, splitRoot};
        } else {
            // 分裂右子树
            int[] rightSplit = persistentSplit(tree[newRt].r, mid + 1, r, pos, version);
            tree[newRt].r = rightSplit[0];
            
            int splitRoot = newNode(version);
            tree[splitRoot].l = tree[newRt].l;
            tree[splitRoot].r = rightSplit[1];
            
            // 更新和
            tree[newRt].sum = (tree[newRt].l != -1 ? tree[tree[newRt].l].sum : 0) + 
                            (tree[newRt].r != -1 ? tree[tree[newRt].r].sum : 0);
            tree[splitRoot].sum = (tree[splitRoot].l != -1 ? tree[tree[splitRoot].l].sum : 0) + 
                                (tree[splitRoot].r != -1 ? tree[tree[splitRoot].r].sum : 0);
            
            return new int[]{newRt, splitRoot};
        }
    }
    
    // 可持久化线段树合并
    static int persistentMerge(int u, int v, int l, int r, int version) {
        if (u == -1) return copyNode(v, version);
        if (v == -1) return copyNode(u, version);
        
        int newU = copyNode(u, version);
        
        if (l == r) {
            tree[newU].sum += tree[v].sum;
            return newU;
        }
        
        int mid = (l + r) >> 1;
        
        tree[newU].l = persistentMerge(tree[newU].l, tree[v].l, l, mid, version);
        tree[newU].r = persistentMerge(tree[newU].r, tree[v].r, mid + 1, r, version);
        
        tree[newU].sum = (tree[newU].l != -1 ? tree[tree[newU].l].sum : 0) + 
                        (tree[newU].r != -1 ? tree[tree[newU].r].sum : 0);
        
        return newU;
    }
    
    // 查询历史版本
    static long queryVersion(int version, int l, int r, int ql, int qr) {
        return query(roots[version], 1, MAXN - 1, ql, qr);
    }
    
    // 区间查询
    static long query(int rt, int l, int r, int ql, int qr) {
        if (rt == -1 || ql > r || qr < l) return 0;
        
        if (ql <= l && r <= qr) {
            return tree[rt].sum;
        }
        
        int mid = (l + r) >> 1;
        long res = 0;
        
        if (ql <= mid && tree[rt].l != -1) {
            res += query(tree[rt].l, l, mid, ql, qr);
        }
        
        if (qr > mid && tree[rt].r != -1) {
            res += query(tree[rt].r, mid + 1, r, ql, qr);
        }
        
        return res;
    }
    
    // 保存当前版本
    static void saveVersion(int version) {
        roots[version] = copyNode(roots[version - 1], version);
    }
    
    public static void main(String[] args) {
        FastIO io = new FastIO();
        
        int n = io.nextInt();
        int q = io.nextInt();
        
        long[] arr = new long[n + 1];
        for (int i = 1; i <= n; i++) {
            arr[i] = io.nextLong();
        }
        
        // 构建初始版本
        currentVersion = 1;
        roots[1] = buildTree(1, n, arr, 1);
        
        while (q-- > 0) {
            int type = io.nextInt();
            
            switch (type) {
                case 1: // 创建新版本并分裂
                    int version = io.nextInt();
                    int pos = io.nextInt();
                    
                    currentVersion++;
                    int[] splitResult = persistentSplit(roots[version], 1, n, pos, currentVersion);
                    roots[currentVersion] = splitResult[0];
                    
                    io.println("Split completed. New version: " + currentVersion);
                    break;
                    
                case 2: // 创建新版本并合并
                    int ver1 = io.nextInt();
                    int ver2 = io.nextInt();
                    
                    currentVersion++;
                    roots[currentVersion] = persistentMerge(roots[ver1], roots[ver2], 1, n, currentVersion);
                    
                    io.println("Merge completed. New version: " + currentVersion);
                    break;
                    
                case 3: // 查询历史版本
                    int queryVer = io.nextInt();
                    int l = io.nextInt(), r = io.nextInt();
                    long ans = queryVersion(queryVer, 1, n, l, r);
                    io.println(ans);
                    break;
                    
                case 4: // 保存当前版本
                    currentVersion++;
                    saveVersion(currentVersion);
                    io.println("Version saved: " + currentVersion);
                    break;
            }
        }
        
        io.close();
    }
}

/*
 * 算法详解：
 * 
 * 1. 可持久化技术：
 *    - 每次修改创建新节点，保留历史版本
 *    - 通过版本号管理不同时间点的状态
 *    - 支持历史版本查询和回溯
 * 
 * 2. 线段树分裂：
 *    - 支持动态区间分离操作
 *    - 每个版本独立维护线段树状态
 *    - 高效处理序列分裂合并
 * 
 * 3. 核心优化：
 *    - 节点复制：实现可持久化
 *    - 版本管理：支持多版本共存
 *    - 内存优化：共享未修改的子树
 * 
 * 4. 时间复杂度：
 *    - 每次操作O(log n)
 *    - 空间复杂度O(q log n)
 * 
 * 5. 应用场景：
 *    - 历史版本查询
 *    - 操作回滚
 *    - 时间旅行查询
 * 
 * 6. 扩展方向：
 *    - 支持更多复杂操作
 *    - 分布式版本管理
 *    - 增量备份和恢复
 */