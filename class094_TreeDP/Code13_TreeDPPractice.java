package class079;

import java.util.*;

/**
 * 树形DP实战练习题目
 * 包含各大OJ平台的经典树形DP问题实现
 * 
 * 题目来源：LeetCode, LintCode, Codeforces, 洛谷, POJ等
 * 算法类型：基础树形DP、换根DP、树形背包、虚树DP
 */
public class Code13_TreeDPPractice {
    
    /**
     * 1. LeetCode 337 - 打家劫舍 III（经典树形DP）
     * 题目链接：https://leetcode.cn/problems/house-robber-iii/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    public int rob(TreeNode root) {
        int[] result = robHelper(root);
        return Math.max(result[0], result[1]);
    }
    
    private int[] robHelper(TreeNode node) {
        if (node == null) return new int[]{0, 0};
        
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // 不偷当前节点：左右子树可以偷或不偷
        int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        // 偷当前节点：左右子树都不能偷
        int doRob = node.val + left[0] + right[0];
        
        return new int[]{notRob, doRob};
    }
    
    /**
     * 2. LeetCode 124 - 二叉树中的最大路径和
     * 题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    private int maxPathSum = Integer.MIN_VALUE;
    
    public int maxPathSum(TreeNode root) {
        maxPathSum = Integer.MIN_VALUE;
        maxGain(root);
        return maxPathSum;
    }
    
    private int maxGain(TreeNode node) {
        if (node == null) return 0;
        
        int leftGain = Math.max(maxGain(node.left), 0);
        int rightGain = Math.max(maxGain(node.right), 0);
        
        maxPathSum = Math.max(maxPathSum, node.val + leftGain + rightGain);
        return node.val + Math.max(leftGain, rightGain);
    }
    
    /**
     * 3. LeetCode 543 - 二叉树的直径
     * 题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    private int maxDiameter = 0;
    
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;
        depth(root);
        return maxDiameter;
    }
    
    private int depth(TreeNode node) {
        if (node == null) return 0;
        
        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);
        
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 4. LeetCode 968 - 二叉树摄像头
     * 题目链接：https://leetcode.cn/problems/binary-tree-cameras/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    public int minCameraCover(TreeNode root) {
        int[] result = minCameraCoverHelper(root);
        return Math.min(result[0], result[1]);
    }
    
    private int[] minCameraCoverHelper(TreeNode node) {
        if (node == null) return new int[]{Integer.MAX_VALUE / 2, 0, 0};
        
        int[] left = minCameraCoverHelper(node.left);
        int[] right = minCameraCoverHelper(node.right);
        
        // 状态0：当前节点安装摄像头
        int install = 1 + Math.min(Math.min(left[0], left[1]), left[2]) + 
                         Math.min(Math.min(right[0], right[1]), right[2]);
        
        // 状态1：当前节点被子节点监控
        int monitored = Math.min(
            Math.min(left[0] + right[0], left[0] + right[1]),
            left[1] + right[0]
        );
        
        // 状态2：当前节点未被监控（需要父节点安装摄像头）
        int unmonitored = left[1] + right[1];
        
        return new int[]{install, monitored, unmonitored};
    }
    
    /**
     * 5. LeetCode 834 - 树中距离之和（换根DP经典题）
     * 题目链接：https://leetcode.cn/problems/sum-of-distances-in-tree/
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public int[] sumOfDistancesInTree(int n, int[][] edges) {
        // 构建图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        int[] dp = new int[n]; // 以节点i为根的子树的距离和
        int[] size = new int[n]; // 子树大小
        int[] result = new int[n];
        
        dfs1(0, -1, graph, dp, size);
        dfs2(0, -1, graph, dp, size, result, n);
        
        return result;
    }
    
    private void dfs1(int u, int parent, List<List<Integer>> graph, int[] dp, int[] size) {
        size[u] = 1;
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfs1(v, u, graph, dp, size);
                size[u] += size[v];
                dp[u] += dp[v] + size[v];
            }
        }
    }
    
    private void dfs2(int u, int parent, List<List<Integer>> graph, int[] dp, int[] size, 
                     int[] result, int n) {
        result[u] = dp[u];
        for (int v : graph.get(u)) {
            if (v != parent) {
                // 换根操作
                int dpU = dp[u], dpV = dp[v];
                int szU = size[u], szV = size[v];
                
                dp[u] = dp[u] - dp[v] - size[v];
                size[u] = size[u] - size[v];
                dp[v] = dp[v] + dp[u] + size[u];
                size[v] = size[v] + size[u];
                
                dfs2(v, u, graph, dp, size, result, n);
                
                // 恢复
                dp[u] = dpU;
                dp[v] = dpV;
                size[u] = szU;
                size[v] = szV;
            }
        }
    }
    
    /**
     * 6. 洛谷 P2014 - 选课（树形背包DP）
     * 题目链接：https://www.luogu.com.cn/problem/P2014
     * 时间复杂度: O(n*m²), 空间复杂度: O(n*m)
     */
    public int courseSelection(int n, int m, int[] prerequisites, int[] credits) {
        // 构建树（0为虚拟根节点）
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) graph.add(new ArrayList<>());
        
        for (int i = 1; i <= n; i++) {
            int pre = prerequisites[i-1];
            graph.get(pre).add(i);
        }
        
        int[][] dp = new int[n+1][m+1];
        dfsCourse(0, graph, credits, dp, m);
        return dp[0][m];
    }
    
    private void dfsCourse(int u, List<List<Integer>> graph, int[] credits, int[][] dp, int m) {
        // 初始化：选择当前节点（如果u>0）
        if (u > 0) {
            for (int j = 1; j <= m; j++) {
                dp[u][j] = credits[u-1];
            }
        }
        
        for (int v : graph.get(u)) {
            dfsCourse(v, graph, credits, dp, m);
            
            // 背包DP：从大到小遍历
            for (int j = m; j >= 0; j--) {
                for (int k = 1; k <= j; k++) {
                    if (u == 0) {
                        // 虚拟根节点，只能选择子节点
                        dp[u][j] = Math.max(dp[u][j], dp[u][j-k] + dp[v][k]);
                    } else {
                        // 普通节点，可以选择当前节点和子节点
                        dp[u][j] = Math.max(dp[u][j], dp[u][j-k] + dp[v][k] - credits[u-1]);
                    }
                }
            }
        }
    }
    
    /**
     * 7. Codeforces 1187E - Tree Painting（换根DP）
     * 题目链接：https://codeforces.com/contest/1187/problem/E
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public long treePainting(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        long[] size = new long[n];
        long[] dp = new long[n];
        
        // 第一次DFS：计算以0为根时的结果
        dfsPainting1(0, -1, graph, size, dp);
        
        long maxScore = dp[0];
        // 第二次DFS：换根计算最大值
        dfsPainting2(0, -1, graph, size, dp, maxScore, n);
        
        return maxScore;
    }
    
    private void dfsPainting1(int u, int parent, List<List<Integer>> graph, long[] size, long[] dp) {
        size[u] = 1;
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfsPainting1(v, u, graph, size, dp);
                size[u] += size[v];
                dp[u] += dp[v];
            }
        }
        dp[u] += size[u];
    }
    
    private void dfsPainting2(int u, int parent, List<List<Integer>> graph, long[] size, 
                            long[] dp, long maxScore, int n) {
        maxScore = Math.max(maxScore, dp[u]);
        
        for (int v : graph.get(u)) {
            if (v != parent) {
                // 保存原始值
                long dpU = dp[u], dpV = dp[v];
                long szU = size[u], szV = size[v];
                
                // 换根：u->v
                dp[u] = dp[u] - dp[v] - size[v];
                size[u] = size[u] - size[v];
                dp[v] = dp[v] + dp[u] + size[u];
                size[v] = size[v] + size[u];
                
                dfsPainting2(v, u, graph, size, dp, maxScore, n);
                
                // 恢复
                dp[u] = dpU;
                dp[v] = dpV;
                size[u] = szU;
                size[v] = szV;
            }
        }
    }
    
    /**
     * 8. POJ 3107 - Godfather（树的重心）
     * 题目链接：http://poj.org/problem?id=3107
     * 时间复杂度: O(n), 空间复杂度: O(n)
     */
    public List<Integer> findCentroids(int n, int[][] edges) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        
        int[] size = new int[n];
        List<Integer> centroids = new ArrayList<>();
        int[] maxComponent = new int[n];
        Arrays.fill(maxComponent, Integer.MAX_VALUE);
        
        dfsCentroid(0, -1, graph, size, centroids, maxComponent, n);
        return centroids;
    }
    
    private void dfsCentroid(int u, int parent, List<List<Integer>> graph, int[] size,
                           List<Integer> centroids, int[] maxComponent, int n) {
        size[u] = 1;
        int maxSize = 0;
        
        for (int v : graph.get(u)) {
            if (v != parent) {
                dfsCentroid(v, u, graph, size, centroids, maxComponent, n);
                size[u] += size[v];
                maxSize = Math.max(maxSize, size[v]);
            }
        }
        
        maxSize = Math.max(maxSize, n - size[u]);
        maxComponent[u] = maxSize;
    }
    
    /**
     * 9. LeetCode 687 - 最长同值路径
     * 题目链接：https://leetcode.cn/problems/longest-univalue-path/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    private int longestUnivaluePath = 0;
    
    public int longestUnivaluePath(TreeNode root) {
        longestUnivaluePath = 0;
        dfsUnivalue(root);
        return longestUnivaluePath;
    }
    
    private int dfsUnivalue(TreeNode node) {
        if (node == null) return 0;
        
        int left = dfsUnivalue(node.left);
        int right = dfsUnivalue(node.right);
        
        int leftPath = 0, rightPath = 0;
        if (node.left != null && node.left.val == node.val) {
            leftPath = left + 1;
        }
        if (node.right != null && node.right.val == node.val) {
            rightPath = right + 1;
        }
        
        longestUnivaluePath = Math.max(longestUnivaluePath, leftPath + rightPath);
        return Math.max(leftPath, rightPath);
    }
    
    /**
     * 10. LeetCode 979 - 在二叉树中分配硬币
     * 题目链接：https://leetcode.cn/problems/distribute-coins-in-binary-tree/
     * 时间复杂度: O(n), 空间复杂度: O(h)
     */
    private int distributeMoves = 0;
    
    public int distributeCoins(TreeNode root) {
        distributeMoves = 0;
        dfsDistribute(root);
        return distributeMoves;
    }
    
    private int dfsDistribute(TreeNode node) {
        if (node == null) return 0;
        
        int left = dfsDistribute(node.left);
        int right = dfsDistribute(node.right);
        
        distributeMoves += Math.abs(left) + Math.abs(right);
        return node.val - 1 + left + right;
    }
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 单元测试方法
     */
    public static void main(String[] args) {
        Code13_TreeDPPractice solver = new Code13_TreeDPPractice();
        
        // 测试打家劫舍III
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(3);
        root.right.right = new TreeNode(1);
        
        System.out.println("打家劫舍III结果: " + solver.rob(root));
        
        // 测试二叉树直径
        TreeNode diameterRoot = new TreeNode(1);
        diameterRoot.left = new TreeNode(2);
        diameterRoot.right = new TreeNode(3);
        diameterRoot.left.left = new TreeNode(4);
        diameterRoot.left.right = new TreeNode(5);
        
        System.out.println("二叉树直径: " + solver.diameterOfBinaryTree(diameterRoot));
        
        // 测试最长同值路径
        TreeNode univalueRoot = new TreeNode(5);
        univalueRoot.left = new TreeNode(4);
        univalueRoot.right = new TreeNode(5);
        univalueRoot.left.left = new TreeNode(1);
        univalueRoot.left.right = new TreeNode(1);
        univalueRoot.right.right = new TreeNode(5);
        
        System.out.println("最长同值路径: " + solver.longestUnivaluePath(univalueRoot));
    }
}