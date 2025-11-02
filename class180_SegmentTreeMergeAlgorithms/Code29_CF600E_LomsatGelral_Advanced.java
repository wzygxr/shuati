import java.io.*;
import java.util.*;

/**
 * Code29: CF600E Lomsat Gelral Advanced (线段树合并 + 树上启发式合并优化)
 * 题目：给定一棵树，每个节点有颜色，求每个子树中出现次数最多的颜色之和
 * 使用线段树合并优化，支持动态维护最大值和求和
 * 时间复杂度：O(nlogn)
 */
public class Code29_CF600E_LomsatGelral_Advanced {
    
    static class SegmentTreeNode {
        int l, r;
        long maxCount; // 最大出现次数
        long sumColors; // 出现次数等于最大次数的颜色编号之和
        SegmentTreeNode left, right;
        
        SegmentTreeNode(int l, int r) {
            this.l = l;
            this.r = r;
            this.maxCount = 0;
            this.sumColors = 0;
        }
    }
    
    static int n, maxColor;
    static int[] colors;
    static List<Integer>[] graph;
    static SegmentTreeNode[] roots;
    static long[] ans;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter pw = new PrintWriter(System.out);
        
        n = Integer.parseInt(br.readLine());
        colors = new int[n + 1];
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        maxColor = 0;
        for (int i = 1; i <= n; i++) {
            colors[i] = Integer.parseInt(st.nextToken());
            maxColor = Math.max(maxColor, colors[i]);
        }
        
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) graph[i] = new ArrayList<>();
        
        for (int i = 1; i < n; i++) {
            st = new StringTokenizer(br.readLine());
            int u = Integer.parseInt(st.nextToken());
            int v = Integer.parseInt(st.nextToken());
            graph[u].add(v);
            graph[v].add(u);
        }
        
        roots = new SegmentTreeNode[n + 1];
        ans = new long[n + 1];
        
        // DFS线段树合并
        dfs(1, 0);
        
        for (int i = 1; i <= n; i++) {
            pw.print(ans[i] + " ");
        }
        pw.println();
        
        pw.flush();
        pw.close();
    }
    
    static void dfs(int u, int parent) {
        // 创建当前节点的线段树
        roots[u] = new SegmentTreeNode(1, maxColor);
        update(roots[u], colors[u], 1);
        
        for (int v : graph[u]) {
            if (v == parent) continue;
            
            dfs(v, u);
            
            // 合并子节点线段树到当前节点
            roots[u] = merge(roots[u], roots[v]);
        }
        
        // 记录答案：当前子树中出现次数最多的颜色之和
        ans[u] = roots[u].sumColors;
    }
    
    static void update(SegmentTreeNode node, int color, int delta) {
        if (node.l == node.r) {
            // 叶子节点
            node.maxCount += delta;
            node.sumColors = color;
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
        
        // 合并左右子树信息
        updateNodeInfo(node);
    }
    
    static SegmentTreeNode merge(SegmentTreeNode a, SegmentTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        if (a.l == a.r) {
            // 叶子节点直接合并
            a.maxCount += b.maxCount;
            a.sumColors = a.l; // 颜色编号
            return a;
        }
        
        a.left = merge(a.left, b.left);
        a.right = merge(a.right, b.right);
        
        // 更新节点信息
        updateNodeInfo(a);
        
        return a;
    }
    
    static void updateNodeInfo(SegmentTreeNode node) {
        // 重置节点信息
        node.maxCount = 0;
        node.sumColors = 0;
        
        // 处理左子树
        if (node.left != null) {
            if (node.left.maxCount > node.maxCount) {
                node.maxCount = node.left.maxCount;
                node.sumColors = node.left.sumColors;
            } else if (node.left.maxCount == node.maxCount) {
                node.sumColors += node.left.sumColors;
            }
        }
        
        // 处理右子树
        if (node.right != null) {
            if (node.right.maxCount > node.maxCount) {
                node.maxCount = node.right.maxCount;
                node.sumColors = node.right.sumColors;
            } else if (node.right.maxCount == node.maxCount) {
                node.sumColors += node.right.sumColors;
            }
        }
    }
    
    // 优化版本：使用启发式合并（小树合并到大树）
    static void dfsOptimized(int u, int parent) {
        // 创建当前节点的线段树
        roots[u] = new SegmentTreeNode(1, maxColor);
        update(roots[u], colors[u], 1);
        
        int heavyChild = -1;
        int maxSize = -1;
        
        // 找到重儿子
        for (int v : graph[u]) {
            if (v == parent) continue;
            
            // 递归处理子节点
            dfsOptimized(v, u);
            
            // 统计子树大小
            int size = getTreeSize(roots[v]);
            if (size > maxSize) {
                maxSize = size;
                heavyChild = v;
            }
        }
        
        if (heavyChild != -1) {
            // 先合并重儿子
            roots[u] = merge(roots[u], roots[heavyChild]);
            
            // 再合并其他轻儿子
            for (int v : graph[u]) {
                if (v == parent || v == heavyChild) continue;
                roots[u] = merge(roots[u], roots[v]);
            }
        }
        
        // 记录答案
        ans[u] = roots[u].sumColors;
    }
    
    static int getTreeSize(SegmentTreeNode node) {
        if (node == null) return 0;
        if (node.l == node.r) return 1;
        return getTreeSize(node.left) + getTreeSize(node.right);
    }
}