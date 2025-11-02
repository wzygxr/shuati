package class120;

// 968. 监控二叉树
// 给定一个二叉树，我们在树的节点上安装摄像头。
// 节点上的每个摄影头都可以监视其父对象、自身及其直接子对象。
// 计算监控树的所有节点所需的最小摄像头数量。
// 测试链接 : https://leetcode.cn/problems/binary-tree-cameras/
// 提交以下的code，提交时请把类名改成"Solution"，可以直接通过
// 时间复杂度：O(n)，空间复杂度：O(n)

public class Code29_LeetCode968 {

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

    // 定义三种状态：
    // 0: 该节点无覆盖
    // 1: 该节点有摄像头
    // 2: 该节点有覆盖
    private int result = 0;

    public int minCameraCover(TreeNode root) {
        // 对根节点的状态进行检验，防止根节点是无覆盖状态
        if (traversal(root) == 0) {
            result++;
        }
        return result;
    }

    private int traversal(TreeNode cur) {
        // 空节点，该节点有覆盖
        if (cur == null) {
            return 2;
        }

        int left = traversal(cur.left);   // 左
        int right = traversal(cur.right); // 右

        // 情况1：左右节点都有覆盖
        if (left == 2 && right == 2) {
            return 0;
        }

        // 情况2：左右节点至少有一个无覆盖
        if (left == 0 || right == 0) {
            result++;
            return 1;
        }

        // 情况3：左右节点至少有一个有摄像头
        if (left == 1 || right == 1) {
            return 2;
        }

        // 不会走到这里
        return -1;
    }

    // 方法二：更清晰的树形DP实现
    public int minCameraCover2(TreeNode root) {
        int[] result = dfs(root);
        // 根节点需要额外考虑：如果根节点未被监控，需要加一个摄像头
        return Math.min(result[1], result[2]) + (result[0] == Integer.MAX_VALUE ? 1 : 0);
    }

    // 返回一个长度为3的数组
    // dp[0]: 当前节点未被监控，但子节点都被监控的最小摄像头数
    // dp[1]: 当前节点被监控，但当前节点没有摄像头的最小摄像头数
    // dp[2]: 当前节点有摄像头的最小摄像头数
    private int[] dfs(TreeNode node) {
        if (node == null) {
            return new int[]{0, 0, Integer.MAX_VALUE / 2}; // 避免整数溢出
        }

        int[] left = dfs(node.left);
        int[] right = dfs(node.right);

        // 当前节点未被监控，但子节点都被监控
        int dp0 = left[1] + right[1];

        // 当前节点被监控，但当前节点没有摄像头
        // 子节点至少有一个有摄像头
        int dp1 = Math.min(left[2] + Math.min(right[1], right[2]), 
                          right[2] + Math.min(left[1], left[2]));

        // 当前节点有摄像头
        int dp2 = 1 + Math.min(left[0], Math.min(left[1], left[2])) + 
                     Math.min(right[0], Math.min(right[1], right[2]));

        return new int[]{dp0, dp1, dp2};
    }

    // 测试方法
    public static void main(String[] args) {
        Code29_LeetCode968 solution = new Code29_LeetCode968();
        
        // 测试用例1: [0,0,null,0,0]
        TreeNode root1 = new TreeNode(0);
        root1.left = new TreeNode(0);
        root1.left.left = new TreeNode(0);
        root1.left.right = new TreeNode(0);
        
        System.out.println("测试用例1结果: " + solution.minCameraCover(root1)); // 期望输出: 1
        
        // 测试用例2: [0,0,null,0,null,0,null,null,0]
        TreeNode root2 = new TreeNode(0);
        root2.left = new TreeNode(0);
        root2.left.left = new TreeNode(0);
        root2.left.left.left = new TreeNode(0);
        root2.left.left.left.right = new TreeNode(0);
        
        System.out.println("测试用例2结果: " + solution.minCameraCover(root2)); // 期望输出: 2
        
        // 测试用例3: 单个节点
        TreeNode root3 = new TreeNode(0);
        System.out.println("测试用例3结果: " + solution.minCameraCover(root3)); // 期望输出: 1
        
        // 测试用例4: 空树
        System.out.println("测试用例4结果: " + solution.minCameraCover(null)); // 期望输出: 0
    }
}

/*
算法思路与树的重心联系：
本题虽然不是直接求树的重心，但体现了树形DP的深度应用：
1. 需要遍历整棵树，处理每个节点的状态
2. 状态转移依赖于子节点的状态
3. 利用了树的结构特性进行最优决策

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 使用了常数级别的额外空间存储状态

工程化考量：
1. 异常处理：处理空树和单节点情况
2. 性能优化：避免重复计算，使用状态转移
3. 可读性：使用清晰的变量命名和状态定义
4. 边界处理：处理根节点的特殊情况

与监控系统联系：
本题可以应用于实际的监控系统设计，如：
1. 智能家居的摄像头布局优化
2. 安防系统的监控点选择
3. 网络监控节点的部署

调试技巧：
1. 使用小规模树结构验证状态转移的正确性
2. 打印每个节点的状态值进行调试
3. 特别注意叶子节点的状态处理

面试要点：
1. 能够解释三种状态的含义和转移逻辑
2. 能够处理边界情况和特殊输入
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法思想应用到其他树形DP问题中

反直觉但关键的设计：
1. 空节点返回状态2（有覆盖）而不是状态0（无覆盖）
2. 根节点需要特殊处理，防止无覆盖状态
3. 状态转移方程的设计需要仔细考虑所有可能情况
*/
