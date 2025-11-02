package class131;

import java.util.*;

/**
 * 洛谷 P4556 (Vani有约会) 雨天的尾巴 / 【模板】线段树合并
 * 题目链接: https://www.luogu.com.cn/problem/P4556
 * 题目描述: 
 * 给定一棵有n个节点的树，初始时每个节点的值为0。有m次操作，每次操作将路径x到y上的所有节点都加上一袋z种救济粮。
 * 最后每个节点需要输出它拥有的最多的救济粮种类，如果有多个，输出编号最小的。
 * 
 * 解题思路:
 * 1. 使用树上差分+线段树合并
 * 2. 对每个节点建立动态开点线段树，维护各救济粮的数量
 * 3. 使用LCA找到路径的最近公共祖先，进行差分操作
 * 4. 在DFS回溯时合并子树的线段树，统计每个节点的答案
 * 
 * 时间复杂度分析:
 * - 预处理LCA: O(n log n)
 * - 线段树操作: O(m log n)
 * - 线段树合并: O(n log n)
 * 空间复杂度: O(n log n) 动态开点线段树
 */
public class Code17_SegmentTreeMerge {
    
    // 线段树节点定义
    static class Node {
        int left, right; // 左右子节点索引
        int val;        // 节点值（救济粮数量）
        int maxVal;     // 最大值
        int maxType;    // 最大值对应的救济粮类型
        
        public Node() {
            this.left = 0;
            this.right = 0;
            this.val = 0;
            this.maxVal = 0;
            this.maxType = 0;
        }
    }
    
    private static final int MAXN = 100010;
    private static List<Integer>[] graph;    // 树的邻接表
    private static Node[] tree;             // 线段树节点数组
    private static int[] root;              // 每个节点对应的线段树根节点
    private static int[] ans;               // 每个节点的答案
    private static int cnt;                 // 动态开点计数器
    private static int maxType;             // 最大救济粮类型
    
    // LCA相关
    private static int[][] up;              // 祖先表
    private static int[] depth;             // 深度数组
    private static int LOG;                 // log2(n)向上取整
    
    @SuppressWarnings("unchecked")
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        
        // 初始化树
        graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        
        for (int i = 1; i < n; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            graph[u].add(v);
            graph[v].add(u);
        }
        
        // 预处理LCA
        LOG = 0;
        while ((1 << LOG) <= n) LOG++;
        up = new int[LOG][n + 1];
        depth = new int[n + 1];
        Arrays.fill(depth, -1);
        dfsLCA(1, 0);
        
        // 初始化线段树相关数组
        int size = MAXN * 40; // 动态开点空间估计
        tree = new Node[size];
        for (int i = 0; i < size; i++) {
            tree[i] = new Node();
        }
        root = new int[n + 1];
        ans = new int[n + 1];
        cnt = 1;
        maxType = 0;
        
        // 处理操作
        while (m-- > 0) {
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            int z = scanner.nextInt();
            maxType = Math.max(maxType, z);
            
            int lca = getLCA(x, y);
            int parentLCA = up[0][lca];
            
            // 树上差分
            update(root[x], 1, maxType, z, 1);
            update(root[y], 1, maxType, z, 1);
            update(root[lca], 1, maxType, z, -1);
            if (parentLCA != 0) {
                update(root[parentLCA], 1, maxType, z, -1);
            }
        }
        
        // DFS合并子树线段树
        dfsMerge(1, 0);
        
        // 输出答案
        for (int i = 1; i <= n; i++) {
            System.out.println(ans[i]);
        }
        
        scanner.close();
    }
    
    // 线段树更新操作
    private static void update(int root, int l, int r, int pos, int val) {
        if (l == r) {
            tree[root].val += val;
            tree[root].maxVal = tree[root].val;
            tree[root].maxType = l;
            return;
        }
        
        int mid = (l + r) >> 1;
        if (pos <= mid) {
            if (tree[root].left == 0) tree[root].left = cnt++;
            update(tree[root].left, l, mid, pos, val);
        } else {
            if (tree[root].right == 0) tree[root].right = cnt++;
            update(tree[root].right, mid + 1, r, pos, val);
        }
        
        pushUp(root);
    }
    
    // 线段树合并操作
    private static int merge(int a, int b, int l, int r) {
        if (a == 0) return b;
        if (b == 0) return a;
        
        if (l == r) {
            tree[a].val += tree[b].val;
            tree[a].maxVal = tree[a].val;
            tree[a].maxType = l;
            return a;
        }
        
        int mid = (l + r) >> 1;
        tree[a].left = merge(tree[a].left, tree[b].left, l, mid);
        tree[a].right = merge(tree[a].right, tree[b].right, mid + 1, r);
        
        pushUp(a);
        return a;
    }
    
    // 更新节点信息
    private static void pushUp(int root) {
        tree[root].maxVal = tree[root].val;
        tree[root].maxType = 0;
        
        // 检查左子树
        if (tree[root].left != 0) {
            if (tree[tree[root].left].maxVal > tree[root].maxVal ||
                (tree[tree[root].left].maxVal == tree[root].maxVal && tree[tree[root].left].maxType < tree[root].maxType)) {
                tree[root].maxVal = tree[tree[root].left].maxVal;
                tree[root].maxType = tree[tree[root].left].maxType;
            }
        }
        
        // 检查右子树
        if (tree[root].right != 0) {
            if (tree[tree[root].right].maxVal > tree[root].maxVal ||
                (tree[tree[root].right].maxVal == tree[root].maxVal && tree[tree[root].right].maxType < tree[root].maxType)) {
                tree[root].maxVal = tree[tree[root].right].maxVal;
                tree[root].maxType = tree[tree[root].right].maxType;
            }
        }
    }
    
    // LCA预处理
    private static void dfsLCA(int u, int parent) {
        depth[u] = depth[parent] + 1;
        up[0][u] = parent;
        
        for (int k = 1; k < LOG; k++) {
            up[k][u] = up[k-1][up[k-1][u]];
        }
        
        for (int v : graph[u]) {
            if (v != parent) {
                dfsLCA(v, u);
            }
        }
    }
    
    // 获取LCA
    private static int getLCA(int u, int v) {
        if (depth[u] < depth[v]) {
            int temp = u;
            u = v;
            v = temp;
        }
        
        // 将u提升到v的深度
        for (int k = LOG - 1; k >= 0; k--) {
            if (depth[u] - (1 << k) >= depth[v]) {
                u = up[k][u];
            }
        }
        
        if (u == v) return u;
        
        for (int k = LOG - 1; k >= 0; k--) {
            if (up[k][u] != up[k][v]) {
                u = up[k][u];
                v = up[k][v];
            }
        }
        
        return up[0][u];
    }
    
    // DFS合并子树并统计答案
    private static void dfsMerge(int u, int parent) {
        for (int v : graph[u]) {
            if (v != parent) {
                dfsMerge(v, u);
                root[u] = merge(root[u], root[v], 1, maxType);
            }
        }
        
        // 记录该节点的答案
        ans[u] = tree[root[u]].maxType;
    }
}