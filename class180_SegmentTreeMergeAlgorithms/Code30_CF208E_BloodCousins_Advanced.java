import java.io.*;
import java.util.*;

/**
 * Code30: CF208E Blood Cousins Advanced (线段树合并 + 树上k级祖先查询)
 * 题目：给定一棵树，多次询问节点v的p级祖先有多少个p级侄子（与v深度相同的节点）
 * 使用线段树合并维护深度信息，支持快速查询
 * 时间复杂度：O((n+q)logn)
 */
public class Code30_CF208E_BloodCousins_Advanced {
    
    static class Query {
        int v, p, ans;
        Query(int v, int p) {
            this.v = v;
            this.p = p;
        }
    }
    
    static class SegmentTreeNode {
        int l, r;
        int count;
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int l, int r) {
            this.l = l;
            this.r = r;
            this.count = 0;
        }
    }
    
    static int n, maxDepth;
    static List<Integer>[] graph;
    static int[] depth, parent;
    static int[][] ancestor;
    static SegmentTreeNode[] depthTrees;
    static List<Query>[] queries;
    static int[] ans;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        n = Integer.parseInt(br.readLine());
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();
        
        depth = new int[n + 1];
        parent = new int[n + 1];
        ancestor = new int[n + 1][20];
        
        // 读取树结构
        for (int i = 1; i <= n; i++) {
            int p = Integer.parseInt(br.readLine());
            if (p == 0) {
                parent[i] = 0;
            } else {
                parent[i] = p;
                graph[p].add(i);
                graph[i].add(p);
            }
        }
        
        // 预处理深度和祖先
        maxDepth = 0;
        for (int i = 1; i <= n; i++) {
            if (parent[i] == 0) {
                dfsDepth(i, 0);
            }
        }
        
        // 预处理二进制倍增
        for (int i = 1; i <= n; i++) {
            ancestor[i][0] = parent[i];
            for (int j = 1; j < 20; j++) {
                if (ancestor[i][j - 1] != 0) {
                    ancestor[i][j] = ancestor[ancestor[i][j - 1]][j - 1];
                }
            }
        }
        
        int q = Integer.parseInt(br.readLine());
        queries = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) queries[i] = new ArrayList<>();
        ans = new int[q];
        
        for (int i = 0; i < q; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());
            int v = Integer.parseInt(st.nextToken());
            int p = Integer.parseInt(st.nextToken());
            Query query = new Query(v, p);
            
            // 找到v的p级祖先
            int kthAncestor = getKthAncestor(v, p);
            if (kthAncestor != 0) {
                queries[kthAncestor].add(query);
            }
            ans[i] = query.ans;
        }
        
        // 线段树合并处理
        depthTrees = new SegmentTreeNode[n + 1];
        for (int i = 1; i <= n; i++) {
            if (parent[i] == 0) {
                dfsMerge(i, 0);
            }
        }
        
        // 输出答案
        for (int i = 0; i < q; i++) {
            pw.print(ans[i] + " ");
        }
        pw.println();
        
        pw.flush();
        pw.close();
    }
    
    static void dfsDepth(int u, int p) {
        depth[u] = depth[p] + 1;
        maxDepth = Math.max(maxDepth, depth[u]);
        parent[u] = p;
        
        for (int v : graph[u]) {
            if (v != p) {
                dfsDepth(v, u);
            }
        }
    }
    
    static int getKthAncestor(int u, int k) {
        if (k > depth[u]) return 0;
        
        int targetDepth = depth[u] - k;
        for (int i = 19; i >= 0; i--) {
            if (depth[u] - (1 << i) >= targetDepth) {
                u = ancestor[u][i];
            }
        }
        return u;
    }
    
    static void dfsMerge(int u, int p) {
        // 创建当前节点的深度线段树
        depthTrees[u] = new SegmentTreeNode(1, maxDepth);
        updateDepth(depthTrees[u], depth[u], 1);
        
        for (int v : graph[u]) {
            if (v == p) continue;
            
            dfsMerge(v, u);
            
            // 合并子节点线段树
            depthTrees[u] = merge(depthTrees[u], depthTrees[v]);
        }
        
        // 处理当前节点的查询
        for (Query query : queries[u]) {
            int targetDepth = depth[query.v]; // v的深度
            query.ans = queryDepth(depthTrees[u], targetDepth) - 1; // 减去v自己
        }
    }
    
    static void updateDepth(SegmentTreeNode node, int d, int delta) {
        if (node.l == node.r) {
            node.count += delta;
            return;
        }
        
        int mid = (node.l + node.r) >> 1;
        if (d <= mid) {
            if (node.left == null) node.left = new SegmentTreeNode(node.l, mid);
            updateDepth(node.left, d, delta);
        } else {
            if (node.right == null) node.right = new SegmentTreeNode(mid + 1, node.r);
            updateDepth(node.right, d, delta);
        }
        
        node.count = (node.left != null ? node.left.count : 0) + 
                    (node.right != null ? node.right.count : 0);
    }
    
    static SegmentTreeNode merge(SegmentTreeNode a, SegmentTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        if (a.l == a.r) {
            a.count += b.count;
            return a;
        }
        
        a.left = merge(a.left, b.left);
        a.right = merge(a.right, b.right);
        a.count = (a.left != null ? a.left.count : 0) + 
                 (a.right != null ? a.right.count : 0);
        
        return a;
    }
    
    static int queryDepth(SegmentTreeNode node, int d) {
        if (node == null) return 0;
        if (d < node.l || d > node.r) return 0;
        if (node.l == node.r) return node.count;
        
        int mid = (node.l + node.r) >> 1;
        if (d <= mid) {
            return queryDepth(node.left, d);
        } else {
            return queryDepth(node.right, d);
        }
    }
    
    // 优化版本：使用树上启发式合并
    static void dfsOptimized(int u, int p, boolean keep) {
        int heavyChild = -1;
        int maxSize = -1;
        
        // 找到重儿子
        for (int v : graph[u]) {
            if (v == p) continue;
            int size = getSubtreeSize(v, u);
            if (size > maxSize) {
                maxSize = size;
                heavyChild = v;
            }
        }
        
        // 先递归处理轻儿子
        for (int v : graph[u]) {
            if (v == p || v == heavyChild) continue;
            dfsOptimized(v, u, false);
        }
        
        // 处理重儿子
        if (heavyChild != -1) {
            dfsOptimized(heavyChild, u, true);
            depthTrees[u] = depthTrees[heavyChild];
        } else {
            depthTrees[u] = new SegmentTreeNode(1, maxDepth);
        }
        
        // 添加当前节点
        updateDepth(depthTrees[u], depth[u], 1);
        
        // 合并轻儿子
        for (int v : graph[u]) {
            if (v == p || v == heavyChild) continue;
            mergeSubtree(u, v);
        }
        
        // 处理查询
        for (Query query : queries[u]) {
            int targetDepth = depth[query.v];
            query.ans = queryDepth(depthTrees[u], targetDepth) - 1;
        }
        
        if (!keep) {
            // 如果不保留，清空线段树
            clearTree(depthTrees[u]);
        }
    }
    
    static int getSubtreeSize(int u, int p) {
        int size = 1;
        for (int v : graph[u]) {
            if (v != p) {
                size += getSubtreeSize(v, u);
            }
        }
        return size;
    }
    
    static void mergeSubtree(int u, int v) {
        depthTrees[u] = merge(depthTrees[u], depthTrees[v]);
    }
    
    static void clearTree(SegmentTreeNode node) {
        if (node == null) return;
        if (node.left != null) clearTree(node.left);
        if (node.right != null) clearTree(node.right);
        node.count = 0;
    }
}