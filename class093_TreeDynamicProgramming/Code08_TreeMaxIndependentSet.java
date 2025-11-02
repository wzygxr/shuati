package class078;

// 树的最大独立集 (Tree Maximum Independent Set)
// 题目描述:
// 对于一棵有N个结点的无根树，选出尽量多的结点，使得任何两个结点均不相邻
// 这是一个经典的树形动态规划问题

import java.util.ArrayList;

public class Code08_TreeMaxIndependentSet {
    // 树的最大节点数 - 可根据实际情况调整
    public static int MAXN = 6001;
    
    // 树的邻接表表示 - 使用ArrayList存储邻接节点，便于遍历
    public static ArrayList<Integer>[] tree = new ArrayList[MAXN];
    
    // dp[i][0] 表示节点i不被选中时，以i为根的子树的最大独立集大小
    // dp[i][1] 表示节点i被选中时，以i为根的子树的最大独立集大小
    public static int[][] dp = new int[MAXN][2];
    
    // 节点权重（对于没有权重的版本，可以都设为1）
    public static int[] weight = new int[MAXN];
    
    // 标记是否有父节点（用于找根节点）
    public static boolean[] hasParent = new boolean[MAXN];
    
    static {
        // 静态初始化邻接表
        for (int i = 0; i < MAXN; i++) {
            tree[i] = new ArrayList<>();
        }
    }
    
    // 构建树结构
    public static void buildTree(int n) {
        // 参数校验
        if (n <= 0) {
            throw new IllegalArgumentException("节点数量必须为正整数");
        }
        if (n >= MAXN) {
            throw new IllegalArgumentException("节点数量超过最大限制：" + MAXN);
        }
        
        // 清空邻接表
        for (int i = 1; i <= n; i++) {
            tree[i].clear();
        }
        // 初始化数组
        java.util.Arrays.fill(hasParent, false);
    }
    
    // 添加无向边（适用于一般树结构）
    public static void addEdge(int u, int v) {
        // 参数校验
        if (u <= 0 || v <= 0 || u >= MAXN || v >= MAXN) {
            throw new IllegalArgumentException("节点编号无效：" + u + ", " + v);
        }
        
        tree[u].add(v);
        tree[v].add(u);
    }
    
    // 设置父子关系（适用于有根树，如公司组织结构）
    public static void setParent(int parent, int child) {
        // 参数校验
        if (parent <= 0 || child <= 0 || parent >= MAXN || child >= MAXN) {
            throw new IllegalArgumentException("节点编号无效：" + parent + ", " + child);
        }
        
        tree[parent].add(child);
        hasParent[child] = true;
    }
    
    // 设置节点权重
    public static void setWeight(int node, int w) {
        if (node <= 0 || node >= MAXN) {
            throw new IllegalArgumentException("节点编号无效：" + node);
        }
        weight[node] = w;
    }
    
    // 树形DP主函数 - 适用于有根树（如通过setParent构建的树）
    public static int maxIndependentSet(int n) {
        // 参数校验
        if (n <= 0) {
            return 0; // 空树的最大独立集大小为0
        }
        
        // 找到根节点（没有父节点的节点）
        int root = 1;
        boolean foundRoot = false;
        for (int i = 1; i <= n; i++) {
            if (!hasParent[i]) {
                root = i;
                foundRoot = true;
                break;
            }
        }
        
        if (!foundRoot) {
            throw new IllegalStateException("无法找到根节点，树结构可能存在环");
        }
        
        // 初始化DP数组
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 0;
            dp[i][1] = 0;
        }
        
        // 执行树形DP
        dfs(root, -1);
        
        // 返回根节点选或不选的最大值
        return Math.max(dp[root][0], dp[root][1]);
    }
    
    // 树形DP主函数 - 适用于无根树（通过addEdge构建的树）
    public static int maxIndependentSetUndirected(int n) {
        // 参数校验
        if (n <= 0) {
            return 0; // 空树的最大独立集大小为0
        }
        
        // 初始化DP数组
        for (int i = 1; i <= n; i++) {
            dp[i][0] = 0;
            dp[i][1] = 0;
        }
        
        // 对于无根树，任意选择一个节点作为根（这里选择节点1）
        int root = 1;
        dfs(root, -1);
        
        // 返回根节点选或不选的最大值
        return Math.max(dp[root][0], dp[root][1]);
    }
    
    // 深度优先搜索 + 动态规划
    private static void dfs(int u, int parent) {
        // 初始化当前节点的DP值
        // 当前节点不被选中时，初始值为0
        dp[u][0] = 0;
        // 当前节点被选中时，初始值为其权重
        dp[u][1] = weight[u];
        
        // 遍历当前节点的所有相邻节点
        for (int v : tree[u]) {
            // 避免回到父节点（防止重复访问）
            if (v != parent) {
                // 递归处理子节点
                dfs(v, u);
                
                // 更新当前节点的DP值
                // 当前节点不被选中：可以选择子节点选或不选的最大值之和
                dp[u][0] += Math.max(dp[v][0], dp[v][1]);
                // 当前节点被选中：子节点都不能选，只能取子节点不被选中的情况
                dp[u][1] += dp[v][0];
            }
        }
    }
    
    // 【打家劫舍III - LeetCode 337】二叉树版本的最大独立集
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
    
    public static int rob(TreeNode root) {
        // 边界条件：空树
        if (root == null) {
            return 0;
        }
        
        int[] result = robHelper(root);
        // 返回不抢劫根节点和抢劫根节点的最大值
        return Math.max(result[0], result[1]);
    }
    
    // 辅助函数：返回 [不抢劫当前节点的最大金额, 抢劫当前节点的最大金额]
    private static int[] robHelper(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        
        // 递归处理左右子树
        int[] left = robHelper(node.left);
        int[] right = robHelper(node.right);
        
        // 不抢劫当前节点：左右子树可以抢也可以不抢，取最大值
        int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
        // 抢劫当前节点：左右子树都不能抢
        int doRob = node.val + left[0] + right[0];
        
        return new int[]{notRob, doRob};
    }
}