package class120;

// 687. 最长同值路径
// 给定一个二叉树的根节点 root ，返回树中最长路径的长度，这个路径中的每个节点具有相同值。
// 这条路径可以经过也可以不经过根节点。
// 两个节点之间的路径长度由它们之间的边数表示。
// 测试链接 : https://leetcode.cn/problems/longest-univalue-path/
// 提交以下的code，提交时请把类名改成"Solution"，可以直接通过
// 时间复杂度：O(n)，空间复杂度：O(n)

public class Code30_LeetCode687 {

    // 树节点定义
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

    private int maxLength = 0;

    public int longestUnivaluePath(TreeNode root) {
        if (root == null) {
            return 0;
        }
        dfs(root);
        return maxLength;
    }

    // 返回以当前节点为起点的最长同值路径长度
    private int dfs(TreeNode node) {
        if (node == null) {
            return 0;
        }

        // 递归计算左右子树的最长同值路径长度
        int left = dfs(node.left);
        int right = dfs(node.right);

        // 当前节点与左子节点值相同，则可以延伸左路径
        int leftPath = 0;
        if (node.left != null && node.left.val == node.val) {
            leftPath = left + 1;
        }

        // 当前节点与右子节点值相同，则可以延伸右路径
        int rightPath = 0;
        if (node.right != null && node.right.val == node.val) {
            rightPath = right + 1;
        }

        // 更新全局最大值：当前节点连接左右路径
        maxLength = Math.max(maxLength, leftPath + rightPath);

        // 返回以当前节点为起点的最长同值路径长度
        return Math.max(leftPath, rightPath);
    }

    // 方法二：更详细的实现，便于理解
    public int longestUnivaluePath2(TreeNode root) {
        if (root == null) return 0;
        
        int[] result = new int[1]; // 使用数组传递引用，避免使用成员变量
        dfs2(root, result);
        return result[0];
    }

    private int dfs2(TreeNode node, int[] max) {
        if (node == null) return 0;
        
        int left = dfs2(node.left, max);
        int right = dfs2(node.right, max);
        
        // 计算以当前节点为根的最长路径
        int currentMax = 0;
        
        // 如果左子节点值与当前节点相同，可以连接左路径
        if (node.left != null && node.left.val == node.val) {
            left = left + 1;
        } else {
            left = 0; // 值不同，不能连接
        }
        
        // 如果右子节点值与当前节点相同，可以连接右路径
        if (node.right != null && node.right.val == node.val) {
            right = right + 1;
        } else {
            right = 0; // 值不同，不能连接
        }
        
        // 更新全局最大值
        max[0] = Math.max(max[0], left + right);
        
        // 返回以当前节点为起点的最长路径
        return Math.max(left, right);
    }

    // 测试方法
    public static void main(String[] args) {
        Code30_LeetCode687 solution = new Code30_LeetCode687();
        
        // 测试用例1: [5,4,5,1,1,5]
        TreeNode root1 = new TreeNode(5);
        root1.left = new TreeNode(4);
        root1.right = new TreeNode(5);
        root1.left.left = new TreeNode(1);
        root1.left.right = new TreeNode(1);
        root1.right.right = new TreeNode(5);
        
        System.out.println("测试用例1结果: " + solution.longestUnivaluePath(root1)); // 期望输出: 2
        
        // 测试用例2: [1,4,5,4,4,5]
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(4);
        root2.right = new TreeNode(5);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(4);
        root2.right.right = new TreeNode(5);
        
        System.out.println("测试用例2结果: " + solution.longestUnivaluePath(root2)); // 期望输出: 2
        
        // 测试用例3: 单个节点
        TreeNode root3 = new TreeNode(1);
        System.out.println("测试用例3结果: " + solution.longestUnivaluePath(root3)); // 期望输出: 0
        
        // 测试用例4: 空树
        System.out.println("测试用例4结果: " + solution.longestUnivaluePath(null)); // 期望输出: 0
        
        // 测试用例5: [1,1,1,1,1,1,1]
        TreeNode root5 = new TreeNode(1);
        root5.left = new TreeNode(1);
        root5.right = new TreeNode(1);
        root5.left.left = new TreeNode(1);
        root5.left.right = new TreeNode(1);
        root5.right.left = new TreeNode(1);
        root5.right.right = new TreeNode(1);
        
        System.out.println("测试用例5结果: " + solution.longestUnivaluePath(root5)); // 期望输出: 4
    }
}

/*
算法思路与树的重心联系：
本题虽然不是直接求树的重心，但体现了树形遍历和路径计算的思想：
1. 需要遍历整棵树，计算每个节点的相关信息
2. 路径计算需要考虑节点值的连续性
3. 利用了树的结构特性进行最优路径搜索

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 使用了常数级别的额外空间

工程化考量：
1. 异常处理：处理空树和单节点情况
2. 性能优化：避免重复计算，使用一次DFS遍历
3. 可读性：使用清晰的变量命名和注释
4. 边界处理：处理节点值不同的情况

与网络路由联系：
本题可以应用于网络路由中的最长连续路径查找：
1. 网络拓扑中的最长稳定路径
2. 通信链路的连续性检测
3. 数据传输路径的优化选择

调试技巧：
1. 使用小规模树结构验证路径计算正确性
2. 打印每个节点的左右路径长度进行调试
3. 特别注意叶子节点的路径计算

面试要点：
1. 能够解释路径长度的定义（边数而非节点数）
2. 能够处理节点值不同的情况
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法思想应用到其他树形路径问题中

关键设计细节：
1. 路径长度由边数表示，不是节点数
2. 路径可以经过根节点，也可以不经过
3. 需要同时考虑左右子树的路径连接
4. 全局最大值需要在递归过程中不断更新

反直觉但关键的设计：
1. 返回值是以当前节点为起点的最长路径，而不是以当前节点为根的最长路径
2. 全局最大值是通过左右路径相加得到的，而不是单独的最大值
3. 节点值不同时需要重置路径长度为0
*/
