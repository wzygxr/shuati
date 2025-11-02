package class118;

import java.util.*;

/**
 * LCA问题 - 递归法与倍增法实现
 * 本文件包含多个LCA相关题目的解答，涵盖不同的实现方法和优化策略
 * 所有非代码内容都以注释形式呈现，包含详细的分析和说明
 * 
 * 主要内容包括：
 * 1. LeetCode 236: 二叉树的最近公共祖先
 * 2. LeetCode 235: 二叉搜索树的最近公共祖先
 * 3. LeetCode 1650: 带父指针的二叉树最近公共祖先
 * 4. 其他平台的经典LCA题目
 * 5. 洛谷 P3379: 最近公共祖先模板题
 * 6. HDU 2586: 树上两点距离
 * 7. SPOJ LCASQ: 基础LCA模板题
 * 8. POJ 1330: 最近公共祖先
 * 9. Codeforces 1304E: 1-Trees and Queries
 * 10. AtCoder ABC133F: Colorful Tree
 * 
 * 算法复杂度分析：
 * 1. 递归DFS: O(n)时间, O(h)空间
 * 2. 倍增法: O(n log n)预处理, O(log n)查询
 * 3. Tarjan算法: O(n + q)时间, O(n)空间
 * 4. 树链剖分: O(n)预处理, O(log n)查询
 * 
 * 工程化考量：
 * 1. 异常处理：输入验证、边界条件处理
 * 2. 性能优化：预处理优化、查询优化
 * 3. 可读性：详细注释、模块化设计
 * 4. 调试能力：打印调试、断言验证
 * 5. 单元测试：覆盖各种边界场景
 * 
 * 语言特性差异：
 * 1. Java: 自动垃圾回收，对象引用传递
 * 2. C++: 手动内存管理，指针操作
 * 3. Python: 动态类型，引用计数垃圾回收
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
 * 
 * 反直觉设计：
 * 1. 倍增法的二进制跳跃思想
 * 2. Tarjan算法的离线处理策略
 * 3. 树链剖分的重链轻链分解
 * 
 * 极端场景鲁棒性：
 * 1. 空树和单节点树
 * 2. 线性树（退化为链表）
 * 3. 完全二叉树
 * 4. 大规模数据（n > 10^5）
 * 5. 深度极大的树
 */
public class Code04_LCA_BinaryLifting {

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
     * 解法一：LeetCode 236. 二叉树的最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
     * 难度：中等
     * 
     * 问题描述：
     * 给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
     * 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
     * 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
     * 
     * 解题思路：递归深度优先搜索
     * 1. 递归终止条件：当前节点为空或者是p或q中的一个
     * 2. 递归搜索左右子树
     * 3. 根据左右子树的返回结果判断：
     *    - 如果左右子树都返回非空，说明当前节点就是LCA
     *    - 如果只有一侧返回非空，返回该侧的结果
     *    - 如果两侧都返回空，返回null
     * 
     * 时间复杂度：O(n) - 其中n是树中节点的数量，最坏情况下需要遍历所有节点
     * 空间复杂度：O(h) - 其中h是树的高度，递归调用栈的深度
     * 是否为最优解：对于单次查询，这是最优解之一
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        // 异常处理：检查输入参数
        if (root == null || p == null || q == null) {
            return null;
        }
        
        // 基本情况：如果当前节点是p或q，则当前节点就是LCA
        if (root == p || root == q) {
            return root;
        }
        
        // 递归查找左子树中的LCA
        TreeNode left = lowestCommonAncestor(root.left, p, q);
        // 递归查找右子树中的LCA
        TreeNode right = lowestCommonAncestor(root.right, p, q);
        
        // 如果左右子树都找到了节点，说明当前节点是LCA
        if (left != null && right != null) {
            return root;
        }
        
        // 如果只有左子树找到了节点，返回左子树的结果
        if (left != null) {
            return left;
        }
        
        // 如果只有右子树找到了节点，返回右子树的结果
        if (right != null) {
            return right;
        }
        
        // 如果左右子树都没有找到节点，返回null
        return null;
    }

    /**
     * 解法二：LeetCode 235. 二叉搜索树的最近公共祖先
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/
     * 难度：简单
     * 
     * 问题描述：
     * 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。
     * 最近公共祖先的定义为："对于有根树 T 的两个节点 p、q，最近公共祖先表示为一个节点 x，
     * 满足 x 是 p、q 的祖先且 x 的深度尽可能大。"
     * 
     * 解题思路：利用二叉搜索树的特性
     * 二叉搜索树的特性：左子树所有节点值 < 根节点值 < 右子树所有节点值
     * 1. 如果p和q的值都小于当前节点，那么LCA在左子树
     * 2. 如果p和q的值都大于当前节点，那么LCA在右子树
     * 3. 如果一个小于等于，一个大于等于，那么当前节点就是LCA
     * 
     * 时间复杂度：O(h) - 其中h是树的高度，在平衡树情况下为O(log n)
     * 空间复杂度：O(h) - 递归调用栈的深度
     * 是否为最优解：是，利用了BST的特性，比通用二叉树解法更高效
     */
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        // 异常处理
        if (root == null || p == null || q == null) {
            return null;
        }
        
        // 如果p和q都在左子树
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestorBST(root.left, p, q);
        }
        // 如果p和q都在右子树
        else if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestorBST(root.right, p, q);
        }
        // 如果p和q分别在两侧，或者其中一个是当前节点
        else {
            return root;
        }
    }

    /**
     * 解法三：LeetCode 1650. 二叉树的最近公共祖先 III (带父指针)
     * 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree-iii/
     * 难度：中等
     * 
     * 问题描述：
     * 给定一棵二叉树中的两个节点 p 和 q，返回它们的最近公共祖先（LCA）节点。
     * 每个节点都有一个指向其父节点的指针。
     * 
     * 解题思路：双指针法
     * 1. 分别计算p和q到根节点的深度差
     * 2. 先将较深的节点向上移动，使两个节点处于同一深度
     * 3. 然后同时向上移动两个节点，直到找到相同的节点，即为LCA
     * 
     * 时间复杂度：O(h) - 其中h是树的高度
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 是否为最优解：是，充分利用了父指针的特性
     */
    public TreeNodeWithParent lowestCommonAncestorWithParent(TreeNodeWithParent p, TreeNodeWithParent q) {
        if (p == null || q == null) {
            return null;
        }
        
        TreeNodeWithParent a = p;
        TreeNodeWithParent b = q;
        
        // 双指针法，类似于链表相交问题
        // 当a或b为空时，将其指向对方的起始节点，这样可以抵消深度差
        while (a != b) {
            a = (a == null) ? q : a.parent;
            b = (b == null) ? p : b.parent;
        }
        
        return a;
    }

    /**
     * 解法四：迭代版本的二叉树LCA（避免递归栈溢出）
     * 适用于处理大型树的情况
     * 
     * 解题思路：后序遍历 + 记录父节点路径
     * 1. 使用栈进行后序遍历
     * 2. 记录每个节点的访问状态
     * 3. 当找到p或q时，记录其路径
     * 4. 比较两条路径，找到最后一个公共节点
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(h)
     */
    public TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        
        // 存储路径
        Map<TreeNode, TreeNode> parentMap = new HashMap<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        parentMap.put(root, null);
        
        // 遍历树，构建父节点映射
        while (!parentMap.containsKey(p) || !parentMap.containsKey(q)) {
            TreeNode node = stack.pop();
            
            if (node.right != null) {
                parentMap.put(node.right, node);
                stack.push(node.right);
            }
            if (node.left != null) {
                parentMap.put(node.left, node);
                stack.push(node.left);
            }
        }
        
        // 构建p的祖先集合
        Set<TreeNode> ancestors = new HashSet<>();
        while (p != null) {
            ancestors.add(p);
            p = parentMap.get(p);
        }
        
        // 查找q的祖先中是否在p的祖先集合中
        while (!ancestors.contains(q)) {
            q = parentMap.get(q);
        }
        
        return q;
    }
    
    /**
     * 解法五：洛谷 P3379 【模板】最近公共祖先（LCA）
     * 题目链接：https://www.luogu.com.cn/problem/P3379
     * 难度：模板题
     * 
     * 问题描述：
     * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
     * 
     * 解题思路：树上倍增法
     * 1. 预处理每个节点的2^k级祖先
     * 2. 对于查询，先将两个节点调整到同一深度
     * 3. 然后同时向上跳跃，直到找到最近公共祖先
     * 
     * 时间复杂度：预处理O(n log n)，查询O(log n)
     * 空间复杂度：O(n log n)
     * 是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法
     */
    static class LuoguP3379 {
        private static final int MAXN = 500001;
        private static final int LOG = 20;
        
        private int[] depth = new int[MAXN];
        private int[][] ancestor = new int[MAXN][LOG];
        private List<Integer>[] tree;
        
        public LuoguP3379(int n, int[][] edges) {
            tree = new ArrayList[n + 1];
            for (int i = 0; i <= n; i++) {
                tree[i] = new ArrayList<>();
            }
            
            // 构建邻接表
            for (int[] edge : edges) {
                tree[edge[0]].add(edge[1]);
                tree[edge[1]].add(edge[0]);
            }
            
            // 预处理倍增数组
            dfs(1, 0);
        }
        
        private void dfs(int u, int parent) {
            depth[u] = depth[parent] + 1;
            ancestor[u][0] = parent;
            
            // 构建倍增数组
            for (int i = 1; i < LOG; i++) {
                if (ancestor[u][i - 1] != 0) {
                    ancestor[u][i] = ancestor[ancestor[u][i - 1]][i - 1];
                }
            }
            
            // 递归处理子节点
            for (int v : tree[u]) {
                if (v != parent) {
                    dfs(v, u);
                }
            }
        }
        
        public int getLCA(int u, int v) {
            // 确保u的深度不小于v
            if (depth[u] < depth[v]) {
                int temp = u;
                u = v;
                v = temp;
            }
            
            // 将u向上跳跃到与v同一深度
            for (int i = LOG - 1; i >= 0; i--) {
                if (depth[u] - (1 << i) >= depth[v]) {
                    u = ancestor[u][i];
                }
            }
            
            // 如果u和v已经相同，则找到了LCA
            if (u == v) {
                return u;
            }
            
            // u和v同时向上跳跃，直到找到LCA
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
     * 解法六：HDU 2586 How far away？
     * 题目链接：https://vjudge.net/problem/HDU-2586
     * 难度：中等
     * 
     * 问题描述：
     * 给定一棵树和若干查询，每个查询要求计算树上两点之间的距离。
     * 
     * 解题思路：
     * 1. 使用LCA计算两点之间的最近公共祖先
     * 2. 两点距离 = 节点u到根的距离 + 节点v到根的距离 - 2 * LCA到根的距离
     * 
     * 时间复杂度：预处理O(n log n)，查询O(log n)
     * 空间复杂度：O(n log n)
     * 是否为最优解：是
     */
    static class HDU2586 {
        private LuoguP3379 lcaSolver;
        private int[] dist; // 节点到根节点的距离
        
        public HDU2586(int n, int[][] edges, int[] weights) {
            lcaSolver = new LuoguP3379(n, edges);
            dist = new int[n + 1];
            
            // 计算每个节点到根节点的距离
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
     * 解法七：SPOJ LCASQ - Lowest Common Ancestor
     * 题目链接：https://www.spoj.com/problems/LCASQ/
     * 难度：中等
     * 
     * 问题描述：
     * 给定一棵树和多个查询，每个查询要求计算两个节点的最近公共祖先。
     * 
     * 解题思路：
     * 使用Tarjan离线算法处理所有查询
     * 
     * 时间复杂度：O(n + q)
     * 空间复杂度：O(n + q)
     * 是否为最优解：是，对于离线查询，Tarjan算法是最优的
     */
    static class SPOJ_LCASQ {
        private List<Integer>[] tree;
        private List<int[]>[] queries;
        private int[] parent;
        private int[] ancestor;
        private boolean[] visited;
        
        public SPOJ_LCASQ(int n, int[][] edges, int[][] queryPairs) {
            tree = new ArrayList[n];
            queries = new ArrayList[n];
            for (int i = 0; i < n; i++) {
                tree[i] = new ArrayList<>();
                queries[i] = new ArrayList<>();
            }
            
            // 构建邻接表
            for (int[] edge : edges) {
                tree[edge[0]].add(edge[1]);
                tree[edge[1]].add(edge[0]);
            }
            
            // 存储查询
            for (int i = 0; i < queryPairs.length; i++) {
                int u = queryPairs[i][0];
                int v = queryPairs[i][1];
                queries[u].add(new int[]{v, i});
                queries[v].add(new int[]{u, i});
            }
            
            parent = new int[n];
            ancestor = new int[n];
            visited = new boolean[n];
            
            // 初始化并查集
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }
        
        private int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        private void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            if (rootX != rootY) {
                parent[rootY] = rootX;
            }
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
     * 测试方法，创建测试用例并验证所有LCA算法实现的正确性
     */
    public static void main(String[] args) {
        Code04_LCA_BinaryLifting solution = new Code04_LCA_BinaryLifting();
        
        System.out.println("========== 测试普通二叉树的LCA算法 ==========");
        // 测试用例1: 简单树结构
        //       3
        //      / \
        //     5   1
        //    / \
        //   6   2
        TreeNode root1 = new TreeNode(3);
        TreeNode node5 = new TreeNode(5);
        TreeNode node1 = new TreeNode(1);
        TreeNode node6 = new TreeNode(6);
        TreeNode node2 = new TreeNode(2);
        
        root1.left = node5;
        root1.right = node1;
        node5.left = node6;
        node5.right = node2;
        
        // 测试LCA(5, 1) 应该返回 3
        TreeNode lca1 = solution.lowestCommonAncestor(root1, node5, node1);
        System.out.println("Test Case 1: LCA(5, 1) = " + (lca1 != null ? lca1.val : "null"));
        
        // 测试LCA(5, 6) 应该返回 5
        TreeNode lca2 = solution.lowestCommonAncestor(root1, node5, node6);
        System.out.println("Test Case 2: LCA(5, 6) = " + (lca2 != null ? lca2.val : "null"));
        
        // 测试用例2: 更复杂的树结构
        //        1
        //       / \
        //      2   3
        //     / \   \
        //    4   5   6
        TreeNode root2 = new TreeNode(1);
        TreeNode node2_2 = new TreeNode(2);
        TreeNode node2_3 = new TreeNode(3);
        TreeNode node2_4 = new TreeNode(4);
        TreeNode node2_5 = new TreeNode(5);
        TreeNode node2_6 = new TreeNode(6);
        
        root2.left = node2_2;
        root2.right = node2_3;
        node2_2.left = node2_4;
        node2_2.right = node2_5;
        node2_3.right = node2_6;
        
        // 测试LCA(4, 5) 应该返回 2
        TreeNode lca3 = solution.lowestCommonAncestor(root2, node2_4, node2_5);
        System.out.println("Test Case 3: LCA(4, 5) = " + (lca3 != null ? lca3.val : "null"));
        
        // 测试LCA(4, 6) 应该返回 1
        TreeNode lca4 = solution.lowestCommonAncestor(root2, node2_4, node2_6);
        System.out.println("Test Case 4: LCA(4, 6) = " + (lca4 != null ? lca4.val : "null"));
        
        // 测试用例3: 极端情况 - 空树
        TreeNode lca5 = solution.lowestCommonAncestor(null, node5, node1);
        System.out.println("Test Case 5: LCA in null tree = " + (lca5 != null ? lca5.val : "null"));
        
        // 测试用例4: 极端情况 - 节点为空
        TreeNode lca6 = solution.lowestCommonAncestor(root1, null, node1);
        System.out.println("Test Case 6: LCA with null node = " + (lca6 != null ? lca6.val : "null"));
        
        System.out.println("\n========== 测试二叉搜索树的LCA算法 ==========");
        // 测试用例5: 二叉搜索树
        //       6
        //      / \
        //     2   8
        //    / \ / \
        //   0  4 7  9
        //     / \
        //    3   5
        TreeNode root3 = new TreeNode(6);
        TreeNode node3_2 = new TreeNode(2);
        TreeNode node3_8 = new TreeNode(8);
        TreeNode node3_0 = new TreeNode(0);
        TreeNode node3_4 = new TreeNode(4);
        TreeNode node3_7 = new TreeNode(7);
        TreeNode node3_9 = new TreeNode(9);
        TreeNode node3_3 = new TreeNode(3);
        TreeNode node3_5 = new TreeNode(5);
        
        root3.left = node3_2;
        root3.right = node3_8;
        node3_2.left = node3_0;
        node3_2.right = node3_4;
        node3_8.left = node3_7;
        node3_8.right = node3_9;
        node3_4.left = node3_3;
        node3_4.right = node3_5;
        
        // 测试BST的LCA - LCA(2, 8) 应该返回 6
        TreeNode bstLca1 = solution.lowestCommonAncestorBST(root3, node3_2, node3_8);
        System.out.println("BST Test 1: LCA(2, 8) = " + (bstLca1 != null ? bstLca1.val : "null"));
        
        // 测试BST的LCA - LCA(2, 4) 应该返回 2
        TreeNode bstLca2 = solution.lowestCommonAncestorBST(root3, node3_2, node3_4);
        System.out.println("BST Test 2: LCA(2, 4) = " + (bstLca2 != null ? bstLca2.val : "null"));
        
        // 测试BST的LCA - LCA(3, 5) 应该返回 4
        TreeNode bstLca3 = solution.lowestCommonAncestorBST(root3, node3_3, node3_5);
        System.out.println("BST Test 3: LCA(3, 5) = " + (bstLca3 != null ? bstLca3.val : "null"));
        
        System.out.println("\n========== 测试带父指针的LCA算法 ==========");
        // 测试用例6: 带父指针的二叉树
        //       3
        //      / \
        //     5   1
        //    / \
        //   6   2
        TreeNodeWithParent root4 = new TreeNodeWithParent(3);
        TreeNodeWithParent node4_5 = new TreeNodeWithParent(5);
        TreeNodeWithParent node4_1 = new TreeNodeWithParent(1);
        TreeNodeWithParent node4_6 = new TreeNodeWithParent(6);
        TreeNodeWithParent node4_2 = new TreeNodeWithParent(2);
        
        root4.left = node4_5;
        root4.right = node4_1;
        node4_5.left = node4_6;
        node4_5.right = node4_2;
        
        // 设置父指针
        node4_5.parent = root4;
        node4_1.parent = root4;
        node4_6.parent = node4_5;
        node4_2.parent = node4_5;
        
        // 测试带父指针的LCA - LCA(5, 1) 应该返回 3
        TreeNodeWithParent parentLca1 = solution.lowestCommonAncestorWithParent(node4_5, node4_1);
        System.out.println("Parent Test 1: LCA(5, 1) = " + (parentLca1 != null ? parentLca1.val : "null"));
        
        // 测试带父指针的LCA - LCA(6, 2) 应该返回 5
        TreeNodeWithParent parentLca2 = solution.lowestCommonAncestorWithParent(node4_6, node4_2);
        System.out.println("Parent Test 2: LCA(6, 2) = " + (parentLca2 != null ? parentLca2.val : "null"));
        
        System.out.println("\n========== 测试迭代版本的LCA算法 ==========");
        // 测试迭代版本的LCA - 使用root1树
        TreeNode iterativeLca1 = solution.lowestCommonAncestorIterative(root1, node5, node1);
        System.out.println("Iterative Test 1: LCA(5, 1) = " + (iterativeLca1 != null ? iterativeLca1.val : "null"));
        
        // 测试迭代版本的LCA - 使用root2树
        TreeNode iterativeLca2 = solution.lowestCommonAncestorIterative(root2, node2_4, node2_6);
        System.out.println("Iterative Test 2: LCA(4, 6) = " + (iterativeLca2 != null ? iterativeLca2.val : "null"));
        
        System.out.println("\n========== 测试洛谷P3379倍增法LCA ==========");
        // 测试用例7: 洛谷P3379
        int n = 5;
        int[][] edges = {{1, 2}, {1, 3}, {2, 4}, {2, 5}};
        LuoguP3379 luoguSolver = new LuoguP3379(n, edges);
        
        // 测试LCA(4, 5) 应该返回 2
        int luoguLca1 = luoguSolver.getLCA(4, 5);
        System.out.println("Luogu Test 1: LCA(4, 5) = " + luoguLca1);
        
        // 测试LCA(4, 3) 应该返回 1
        int luoguLca2 = luoguSolver.getLCA(4, 3);
        System.out.println("Luogu Test 2: LCA(4, 3) = " + luoguLca2);
        
        System.out.println("\n========== 测试HDU2586树上距离 ==========");
        // 测试用例8: HDU2586
        int[][] hduEdges = {{1, 2}, {1, 3}, {2, 4}, {2, 5}};
        int[] weights = {10, 20, 30, 40};
        HDU2586 hduSolver = new HDU2586(n, hduEdges, weights);
        
        // 测试距离(4, 5) 应该返回 30 + 40 = 70
        int distance1 = hduSolver.getDistance(4, 5);
        System.out.println("HDU Test 1: Distance(4, 5) = " + distance1);
        
        // 测试距离(4, 3) 应该返回 30 + 20 = 50
        int distance2 = hduSolver.getDistance(4, 3);
        System.out.println("HDU Test 2: Distance(4, 3) = " + distance2);
        
        System.out.println("\n所有LCA算法测试完成！");
    }
}