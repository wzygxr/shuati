package class120;

// 543. 二叉树的直径
// 题目来源：LeetCode 543 https://leetcode.cn/problems/diameter-of-binary-tree/
// 题目描述：给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
// 这条路径可能穿过也可能不穿过根结点。
// 算法思想：利用深度优先搜索计算每个节点的高度，同时更新最长路径长度（直径）
// 与树的重心的关系：树的直径与树的重心有密切关系，直径必然经过树的重心
// 解题思路：
// 1. 对于每个节点，计算经过该节点的最长路径长度（左子树深度+右子树深度）
// 2. 在计算深度的过程中，同时更新全局最大值（直径）
// 3. 返回整棵树的直径
// 时间复杂度：O(n)，每个节点访问一次
// 空间复杂度：O(h)，h为树高，最坏情况下为O(n)，用于递归栈
// 提交说明：提交时请把类名改成"Solution"，可以直接通过

public class Code33_LeetCode543 {

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

    // 记录二叉树的最大直径（全局变量）
    private int maxDiameter = 0;

    /**
     * 计算二叉树的直径（方法一：使用全局变量）
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    public int diameterOfBinaryTree(TreeNode root) {
        // 边界情况：空树的直径为0
        if (root == null) {
            return 0;
        }
        
        // 重置最大直径为0
        maxDiameter = 0;
        // 通过深度优先搜索计算深度并更新直径
        depth(root);
        // 返回计算得到的最大直径
        return maxDiameter;
    }

    /**
     * 计算树的深度，同时更新直径
     * 核心思想：对于每个节点，经过该节点的最长路径长度等于左子树深度+右子树深度
     * @param node 当前节点
     * @return 以node为根的子树的最大深度
     */
    private int depth(TreeNode node) {
        // 基础情况：空节点的深度为0
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的深度
        // leftDepth表示以node.left为根的子树的最大深度
        int leftDepth = depth(node.left);
        // rightDepth表示以node.right为根的子树的最大深度
        int rightDepth = depth(node.right);
        
        // 更新直径：经过当前节点的最长路径为左子树深度+右子树深度
        // 这是因为从左子树的最深叶子节点经过当前节点到右子树的最深叶子节点的路径长度
        // 就是左子树深度+右子树深度
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        // 等于左右子树的最大深度加1（当前节点）
        return Math.max(leftDepth, rightDepth) + 1;
    }

    /**
     * 计算二叉树的直径（方法二：使用数组传递引用）
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    public int diameterOfBinaryTree2(TreeNode root) {
        // 边界情况：空树的直径为0
        if (root == null) return 0;
        
        // 使用数组传递引用，存储最大直径
        // 数组的第一个元素存储最大直径
        int[] result = new int[1];
        // 通过深度优先搜索计算深度并更新直径
        getDepth(root, result);
        // 返回计算得到的最大直径
        return result[0];
    }

    /**
     * 计算树的深度，同时更新直径（方法二的辅助函数）
     * @param node 当前节点
     * @param maxDiameter 存储最大直径的数组
     * @return 以node为根的子树的最大深度
     */
    private int getDepth(TreeNode node, int[] maxDiameter) {
        // 基础情况：空节点的深度为0
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的深度
        int leftDepth = getDepth(node.left, maxDiameter);
        int rightDepth = getDepth(node.right, maxDiameter);
        
        // 更新最大直径：经过当前节点的最长路径为左子树深度+右子树深度
        maxDiameter[0] = Math.max(maxDiameter[0], leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        return Math.max(leftDepth, rightDepth) + 1;
    }

    /**
     * 计算二叉树的直径（方法三：使用自定义类返回多个值）
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    public int diameterOfBinaryTree3(TreeNode root) {
        // 边界情况：空树的直径为0
        if (root == null) return 0;
        
        // 通过计算获取树的信息（深度和直径）
        TreeInfo info = calculateDiameter(root);
        // 返回计算得到的直径
        return info.diameter;
    }

    /**
     * 自定义类存储深度和直径信息
     * 用于方法三中同时返回深度和直径信息
     */
    private static class TreeInfo {
        int depth;      // 树的深度
        int diameter;    // 树的直径
        
        TreeInfo(int depth, int diameter) {
            this.depth = depth;
            this.diameter = diameter;
        }
    }

    /**
     * 计算树的深度和直径（方法三的辅助函数）
     * @param node 当前节点
     * @return 包含深度和直径信息的TreeInfo对象
     */
    private TreeInfo calculateDiameter(TreeNode node) {
        // 基础情况：空节点的深度为0，直径为0
        if (node == null) {
            return new TreeInfo(0, 0);
        }
        
        // 递归计算左右子树的信息
        TreeInfo leftInfo = calculateDiameter(node.left);
        TreeInfo rightInfo = calculateDiameter(node.right);
        
        // 计算当前节点的深度：左右子树的最大深度加1
        int currentDepth = Math.max(leftInfo.depth, rightInfo.depth) + 1;
        
        // 计算当前节点的直径：
        // 取左子树直径、右子树直径、经过当前节点的直径（左子树深度+右子树深度）的最大值
        int currentDiameter = Math.max(
            Math.max(leftInfo.diameter, rightInfo.diameter),  // 左右子树的直径
            leftInfo.depth + rightInfo.depth                  // 经过当前节点的直径
        );
        
        // 返回当前节点的信息
        return new TreeInfo(currentDepth, currentDiameter);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        Code33_LeetCode543 solution = new Code33_LeetCode543();
        
        // 测试用例1: [1,2,3,4,5]
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        // 直径为3（路径4->2->1->3）
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        System.out.println("测试用例1结果: " + solution.diameterOfBinaryTree(root1)); // 期望输出: 3
        
        // 测试用例2: [1,2]
        //   1
        //  /
        // 2
        // 直径为1（路径1->2）
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        
        System.out.println("测试用例2结果: " + solution.diameterOfBinaryTree(root2)); // 期望输出: 1
        
        // 测试用例3: 单个节点
        // 1
        // 直径为0
        TreeNode root3 = new TreeNode(1);
        System.out.println("测试用例3结果: " + solution.diameterOfBinaryTree(root3)); // 期望输出: 0
        
        // 测试用例4: 空树
        // 直径为0
        System.out.println("测试用例4结果: " + solution.diameterOfBinaryTree(null)); // 期望输出: 0
        
        // 测试用例5: 复杂结构
        //       1
        //      / \
        //     2   3
        //    / \   \
        //   4   5   6
        //  / \
        // 7   8
        // 直径为5（路径7->4->2->1->3->6）
        TreeNode root5 = new TreeNode(1);
        root5.left = new TreeNode(2);
        root5.right = new TreeNode(3);
        root5.left.left = new TreeNode(4);
        root5.left.right = new TreeNode(5);
        root5.right.right = new TreeNode(6);
        root5.left.left.left = new TreeNode(7);
        root5.left.left.right = new TreeNode(8);
        
        System.out.println("测试用例5结果: " + solution.diameterOfBinaryTree(root5)); // 期望输出: 5
    }
}

/*
算法思路与树的重心联系：
本题与树的重心密切相关，因为：
1. 树的直径的两个端点通常与重心有特定关系
2. 计算直径的方法可以用于寻找重心
3. 树形遍历的思想在两者中都得到应用

时间复杂度分析：
- 每个节点只被访问一次，时间复杂度为O(n)

空间复杂度分析：
- 递归栈深度为树的高度，最坏情况下为O(n)
- 使用了常数级别的额外空间

工程化考量：
1. 异常处理：处理空树和单节点情况
2. 性能优化：避免重复计算，使用一次DFS遍历
3. 可读性：提供多种实现方式便于理解
4. 边界处理：直径定义为边数而不是节点数

关键设计细节：
1. 直径定义为边数，不是节点数
2. 直径可能不经过根节点
3. 需要同时计算深度和直径
4. 使用后序遍历（左右根）的顺序

调试技巧：
1. 使用小规模树结构验证算法正确性
2. 打印每个节点的深度和直径进行调试
3. 特别注意叶子节点的处理

面试要点：
1. 能够解释直径的定义（边数而非节点数）
2. 能够处理直径不经过根节点的情况
3. 能够分析算法的时间复杂度和空间复杂度
4. 能够将算法思想应用到其他树形问题中

反直觉但关键的设计：
1. 直径不一定经过根节点
2. 单个节点的直径是0而不是1
3. 深度计算和直径更新需要同时进行

与网络拓扑联系：
本题可以应用于网络拓扑分析：
1. 网络延迟分析：直径代表最大延迟
2. 通信路径优化：寻找最优通信路径
3. 分布式系统：节点间通信距离计算

性能优化：
1. 使用一次DFS遍历完成所有计算
2. 避免重复计算子树信息
3. 使用引用传递减少对象创建
*/