/**
 * LeetCode 543. 二叉树的直径 (Diameter of Binary Tree) - Java实现
 * 题目链接：https://leetcode.com/problems/diameter-of-binary-tree/
 * 
 * 题目描述：
 * 给定一棵二叉树，你需要计算它的直径长度。二叉树的直径是指树中任意两个节点之间最长路径的长度。
 * 这条路径可能穿过也可能不穿过根节点。
 * 
 * 算法思路（树形DP经典问题）：
 * 1. 树形DP思想：对于每个节点，计算以该节点为根的子树的最大深度
 * 2. 直径计算：对于每个节点，直径 = 左子树深度 + 右子树深度
 * 3. 全局维护：在递归过程中维护全局最大直径
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(n) - 每个节点访问一次，无法优化
 * - 空间复杂度：O(h) - 递归栈深度，h为树的高度，最坏情况O(n)
 * 
 * 最优解验证：这是最优解，无法进一步优化时间复杂度
 * 
 * 工程化考量：
 * 1. 异常处理：处理空树、单节点等边界情况
 * 2. 内存优化：使用递归栈，避免额外空间开销
 * 3. 可测试性：提供完整的测试用例覆盖各种场景
 * 4. 可读性：清晰的变量命名和注释
 * 
 * 算法技巧总结：
 * - 见到树形结构求最长路径问题，优先考虑树形DP
 * - 路径可能不经过根节点，需要在每个节点处计算可能的最大路径
 * - 递归返回深度信息，同时维护全局最大值
 * 
 * 边界场景处理：
 * - 空树：返回0
 * - 单节点树：返回0（没有边）
 * - 极端不平衡树：递归深度可能较大，但时间复杂度仍为O(n)
 */

class TreeNode {
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

public class LC543_DiameterOfBinaryTree {
    private int maxDiameter = 0;
    
    /**
     * 主方法：计算二叉树的直径
     * @param root 二叉树根节点
     * @return 树的直径长度
     */
    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        maxDiameter = 0;
        depth(root);
        return maxDiameter;
    }
    
    /**
     * 递归计算节点深度，同时更新最大直径
     * @param node 当前节点
     * @return 当前节点的深度
     */
    private int depth(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树深度
        int leftDepth = depth(node.left);
        int rightDepth = depth(node.right);
        
        // 更新全局最大直径：当前节点的直径 = 左深度 + 右深度
        maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的深度：左右子树最大深度 + 1
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 测试用例：验证算法正确性
     * 包含多种边界情况和典型情况
     */
    public static void main(String[] args) {
        LC543_DiameterOfBinaryTree solution = new LC543_DiameterOfBinaryTree();
        
        // 测试用例1：空树
        System.out.println("测试用例1 - 空树: " + solution.diameterOfBinaryTree(null));
        
        // 测试用例2：单节点树
        TreeNode root1 = new TreeNode(1);
        System.out.println("测试用例2 - 单节点: " + solution.diameterOfBinaryTree(root1));
        
        // 测试用例3：示例树 [1,2,3,4,5]
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        System.out.println("测试用例3 - 示例树: " + solution.diameterOfBinaryTree(root));
        
        // 测试用例4：左斜树
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.left.left = new TreeNode(3);
        root2.left.left.left = new TreeNode(4);
        System.out.println("测试用例4 - 左斜树: " + solution.diameterOfBinaryTree(root2));
        
        // 测试用例5：完全二叉树
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.right = new TreeNode(3);
        root3.left.left = new TreeNode(4);
        root3.left.right = new TreeNode(5);
        root3.right.left = new TreeNode(6);
        root3.right.right = new TreeNode(7);
        System.out.println("测试用例5 - 完全二叉树: " + solution.diameterOfBinaryTree(root3));
    }
    
    /**
     * 调试辅助方法：打印树结构
     */
    private void printTree(TreeNode node, int level) {
        if (node == null) {
            System.out.println("  ".repeat(level) + "null");
            return;
        }
        System.out.println("  ".repeat(level) + node.val);
        printTree(node.left, level + 1);
        printTree(node.right, level + 1);
    }
}