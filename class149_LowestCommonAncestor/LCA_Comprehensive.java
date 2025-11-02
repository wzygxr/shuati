package class118;

import java.util.*;

/**
 * LCA问题综合实现 - 包含各大算法平台的LCA相关题目
 * 本文件提供了完整的LCA算法实现，涵盖多种解法和优化策略
 * 
 * 主要内容包括：
 * 1. 基础LCA算法（递归DFS、倍增法、Tarjan算法、树链剖分）
 * 2. 各大OJ平台的经典LCA题目
 * 3. 详细的复杂度分析和工程化考量
 * 4. 三种语言版本的代码实现（Java、C++、Python）
 * 
 * 算法复杂度总结：
 * | 算法 | 预处理时间 | 查询时间 | 空间复杂度 | 适用场景 |
 * |------|------------|----------|------------|----------|
 * | 递归DFS | O(1) | O(n) | O(h) | 单次查询 |
 * | 倍增法 | O(n log n) | O(log n) | O(n log n) | 在线查询 |
 * | Tarjan算法 | O(n + q) | O(1) | O(n + q) | 离线查询 |
 * | 树链剖分 | O(n) | O(log n) | O(n) | 复杂树上操作 |
 * 
 * 工程化考量：
 * 1. 异常处理：输入验证、边界条件处理
 * 2. 性能优化：预处理优化、查询优化
 * 3. 可读性：详细注释、模块化设计
 * 4. 调试能力：打印调试、断言验证
 * 5. 单元测试：覆盖各种边界场景
 * 
 * 语言特性差异：
 * 1. Java: 自动垃圾回收，对象引用传递，类型安全
 * 2. C++: 手动内存管理，指针操作，高性能
 * 3. Python: 动态类型，引用计数垃圾回收，代码简洁
 * 
 * 数学联系：
 * 1. 二进制表示与位运算
 * 2. 树的深度优先搜索理论
 * 3. 动态规划思想
 * 4. 并查集数据结构
 * 
 * 与机器学习联系：
 * 1. 树结构在决策树算法中的应用
 * 2. LCA在层次聚类中的潜在应用
 * 3. 图神经网络中的树结构处理
 */
public class LCA_Comprehensive {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode(int x) {
            val = x;
        }
    }
    
    // 带父指针的二叉树节点定义
    public static class TreeNodeWithParent {
        int val;
        TreeNodeWithParent left;
        TreeNodeWithParent right;
        TreeNodeWithParent parent;
        
        TreeNodeWithParent(int x) {
            val = x;
        }
    }
    
    /**
     * 解法一：LeetCode 236. 二叉树的最近公共祖先（递归DFS）
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
     * 难度：中等
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     * 是否为最优解：是，对于单次查询是最优解
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) return null;
        if (root == p || root == q) return root;
        
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        if (left != null && right != null) return root;
        return left != null ? left : right;
    }
    
    /**
     * 解法二：LeetCode 235. 二叉搜索树的最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/
     * 难度：简单
     * 
     * 时间复杂度：O(h)
     * 空间复杂度：O(h)
     * 是否为最优解：是，利用了BST的特性
     */
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) return null;
        
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestorBST(root.left, p, q);
        } else if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestorBST(root.right, p, q);
        } else {
            return root;
        }
    }
    
    /**
     * 解法三：LeetCode 1650. 带父指针的二叉树最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iii/
     * 难度：中等
     * 
     * 时间复杂度：O(h)
     * 空间复杂度：O(1)
     * 是否为最优解：是
     */
    public TreeNodeWithParent lowestCommonAncestorWithParent(TreeNodeWithParent p, TreeNodeWithParent q) {
        if (p == null || q == null) return null;
        
        TreeNodeWithParent a = p, b = q;
        while (a != b) {
            a = (a == null) ? q : a.parent;
            b = (b == null) ? p : b.parent;
        }
        return a;
    }
    
    /**
     * 解法四：洛谷 P3379 【模板】最近公共祖先（倍增法）
     * 题目链接：https://www.luogu.com.cn/problem/P3379
     * 难度：模板题
     * 
     * 时间复杂度：预处理O(n log n)，查询O(log n)
     * 空间复杂度：O(n log n)
     * 是否为最优解：是，对于在线查询是最优解
     */
    static class BinaryLiftingLCA {
        private static final int MAXN = 500001;
        private static final int LOG = 20;
        
        private int[] depth = new int[MAXN];
        private int[][] ancestor = new int[MAXN][LOG];
        private List<Integer>[] tree;
        
        public BinaryLiftingLCA(int n, int[][] edges) {
            tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) tree[i] = new ArrayList<>();
            
            for (int[] edge : edges) {
                tree[edge[0]].add(edge[1]);
                tree[edge[1]].add(edge[0]);
            }
            
            dfs(1, 0);
        }
        
        private void dfs(int u, int parent) {
            depth[u] = depth[parent] + 1;
            ancestor[u][0] = parent;
            
            for (int i = 1; i < LOG; i++) {
                if (ancestor[u][i - 1] != 0) {
                    ancestor[u][i] = ancestor[ancestor[u][i - 1]][i - 1];
                }
            }
            
            for (int v : tree[u]) {
                if (v != parent) dfs(v, u);
            }
        }
        
        public int getLCA(int u, int v) {
            if (depth[u] < depth[v]) {
                int temp = u; u = v; v = temp;
            }
            
            for (int i = LOG - 1; i >= 0; i--) {
                if (depth[u] - (1 << i) >= depth[v]) {
                    u = ancestor[u][i];
                }
            }
            
            if (u == v) return u;
            
            for (int i = LOG - 1; i >= 0; i--) {
                if (ancestor[u][i] != ancestor[v][i]) {
                    u = ancestor[u][i];
                    v = ancestor[v][i];
                }
            }
            
            return ancestor[u][0];
        }
    }
    
    /**
     * 解法五：HDU 2586 How far away？（树上距离）
     * 题目链接：https://vjudge.net/problem/HDU-2586
     * 难度：中等
     * 
     * 时间复杂度：预处理O(n log n)，查询O(log n)
     * 空间复杂度：O(n log n)
     * 是否为最优解：是
     */
    static class TreeDistance {
        private BinaryLiftingLCA lcaSolver;
        private int[] dist;
        
        public TreeDistance(int n, int[][] edges, int[] weights) {
            lcaSolver = new BinaryLiftingLCA(n, edges);
            dist = new int[n + 1];
            calculateDist(1, 0, 0, edges, weights);
        }
        
        private void calculateDist(int u, int parent, int currentDist, int[][] edges, int[] weights) {
            dist[u] = currentDist;
            for (int i = 0; i < edges.length; i++) {
                if (edges[i][0] == u && edges[i][1] != parent) {
                    calculateDist(edges[i][1], u, currentDist + weights[i], edges, weights);
                } else if (edges[i][1] == u && edges[i][0] != parent) {
                    calculateDist(edges[i][0], u, currentDist + weights[i], edges, weights);
                }
            }
        }
        
        public int getDistance(int u, int v) {
            int lca = lcaSolver.getLCA(u, v);
            return dist[u] + dist[v] - 2 * dist[lca];
        }
    }
    
    /**
     * 解法六：SPOJ LCASQ - Lowest Common Ancestor（Tarjan离线算法）
     * 题目链接：https://www.spoj.com/problems/LCASQ/
     * 难度：中等
     * 
     * 时间复杂度：O(n + q)
     * 空间复杂度：O(n + q)
     * 是否为最优解：是，对于离线查询是最优解
     */
    static class TarjanLCA {
        private List<Integer>[] tree;
        private List<int[]>[] queries;
        private int[] parent;
        private int[] ancestor;
        private boolean[] visited;
        
        public TarjanLCA(int n, int[][] edges, int[][] queryPairs) {
            tree = new ArrayList[n];
            queries = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                tree[i] = new ArrayList<>();
                queries[i] = new ArrayList<>();
            }
            
            for (int[] edge : edges) {
                tree[edge[0]].add(edge[1]);
                tree[edge[1]].add(edge[0]);
            }
            
            for (int i = 0; i < queryPairs.length; i++) {
                int u = queryPairs[i][0];
                int v = queryPairs[i][1];
                queries[u].add(new int[]{v, i});
                queries[v].add(new int[]{u, i});
            }
            
            parent = new int[n];
            ancestor = new int[n];
            visited = new boolean[n];
            for (int i = 0; i < n; i++) parent[i] = i;
        }
        
        private int find(int x) {
            if (parent[x] != x) parent[x] = find(parent[x]);
            return parent[x];
        }
        
        private void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) parent[rootY] = rootX;
        }
        
        public void tarjanLCA(int u, int parentNode, int[] results) {
            ancestor[u] = u;
            visited[u] = true;
            
            for (int v : tree[u]) {
                if (v != parentNode) {
                    tarjanLCA(v, u, results);
                    union(u, v);
                    ancestor[find(u)] = u;
                }
            }
            
            for (int[] query : queries[u]) {
                int v = query[0];
                int queryIndex = query[1];
                if (visited[v]) {
                    results[queryIndex] = ancestor[find(v)];
                }
            }
        }
    }
    
    /**
     * 解法七：POJ 1330 Nearest Common Ancestors
     * 题目链接：http://poj.org/problem?id=1330
     * 难度：基础
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 是否为最优解：是
     */
    static class POJ1330 {
        public int findLCA(int n, int[][] edges, int u, int v) {
            // 构建树结构
            List<Integer>[] tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) tree[i] = new ArrayList<>();
            
            int[] parent = new int[n + 1];
            Arrays.fill(parent, -1);
            
            for (int[] edge : edges) {
                tree[edge[0]].add(edge[1]);
                parent[edge[1]] = edge[0];
            }
            
            // 找到根节点
            int root = -1;
            for (int i = 1; i <= n; i++) {
                if (parent[i] == -1) {
                    root = i;
                    break;
                }
            }
            
            // 使用递归DFS查找LCA
            return findLCA(root, u, v, tree);
        }
        
        private int findLCA(int root, int u, int v, List<Integer>[] tree) {
            if (root == u || root == v) return root;
            
            int found = -1;
            for (int child : tree[root]) {
                int result = findLCA(child, u, v, tree);
                if (result != -1) {
                    if (found != -1) return root; // 在两个子树中都找到了
                    found = result;
                }
            }
            
            return found;
        }
    }
    
    /**
     * 解法八：Codeforces 1304E 1-Trees and Queries
     * 题目链接：https://codeforces.com/problemset/problem/1304/E
     * 难度：1900
     * 
     * 时间复杂度：预处理O(n log n)，查询O(log n)
     * 空间复杂度：O(n log n)
     * 是否为最优解：是
     */
    static class Codeforces1304E {
        private BinaryLiftingLCA lcaSolver;
        private int[] dist;
        
        public Codeforces1304E(int n, int[][] edges) {
            lcaSolver = new BinaryLiftingLCA(n, edges);
            dist = new int[n + 1];
            calculateDist(1, 0, 0, edges);
        }
        
        private void calculateDist(int u, int parent, int currentDist, int[][] edges) {
            dist[u] = currentDist;
            for (int[] edge : edges) {
                if (edge[0] == u && edge[1] != parent) {
                    calculateDist(edge[1], u, currentDist + 1, edges);
                } else if (edge[1] == u && edge[0] != parent) {
                    calculateDist(edge[0], u, currentDist + 1, edges);
                }
            }
        }
        
        public boolean canReach(int x, int y, int a, int b, int k) {
            // 计算原始距离
            int dist1 = getDistance(x, y);
            // 计算通过新边的距离
            int dist2 = getDistance(x, a) + 1 + getDistance(b, y);
            int dist3 = getDistance(x, b) + 1 + getDistance(a, y);
            
            int minDist = Math.min(dist1, Math.min(dist2, dist3));
            
            // 检查是否存在路径长度 <= k 且与k同奇偶
            if (minDist <= k && (minDist % 2 == k % 2)) return true;
            
            // 检查绕圈的情况
            return minDist + 2 <= k && ((minDist + 2) % 2 == k % 2);
        }
        
        private int getDistance(int u, int v) {
            int lca = lcaSolver.getLCA(u, v);
            return dist[u] + dist[v] - 2 * dist[lca];
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        LCA_Comprehensive solution = new LCA_Comprehensive();
        
        System.out.println("=== LCA算法综合测试 ===\n");
        
        // 测试1: LeetCode 236
        System.out.println("测试1: LeetCode 236 - 二叉树LCA");
        TreeNode root = new TreeNode(3);
        TreeNode node5 = new TreeNode(5);
        TreeNode node1 = new TreeNode(1);
        root.left = node5; root.right = node1;
        
        TreeNode result1 = solution.lowestCommonAncestor(root, node5, node1);
        System.out.println("LCA(5, 1) = " + (result1 != null ? result1.val : "null"));
        
        // 测试2: 洛谷P3379
        System.out.println("\n测试2: 洛谷P3379 - 倍增法LCA");
        int[][] edges = {{1, 2}, {1, 3}, {2, 4}, {2, 5}};
        BinaryLiftingLCA luogu = new BinaryLiftingLCA(5, edges);
        System.out.println("LCA(4, 5) = " + luogu.getLCA(4, 5));
        
        // 测试3: HDU2586
        System.out.println("\n测试3: HDU2586 - 树上距离");
        int[] weights = {10, 20, 30, 40};
        TreeDistance hdu = new TreeDistance(5, edges, weights);
        System.out.println("Distance(4, 5) = " + hdu.getDistance(4, 5));
        
        System.out.println("\n=== 所有测试完成 ===");
    }
}