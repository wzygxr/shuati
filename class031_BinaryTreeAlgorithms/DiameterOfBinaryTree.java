package class037;

// LeetCode 543. Diameter of Binary Tree
// 题目链接: https://leetcode.cn/problems/diameter-of-binary-tree/
// 题目描述: 给定一棵二叉树，你需要计算它的直径长度。
// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，直径等于左子树高度 + 右子树高度
// 3. 在递归计算高度的过程中，同时更新最大直径
// 4. 注意：直径不一定经过根节点，需要在所有节点中寻找最大值
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算二叉树直径的标准方法

// 补充题目:
// 1. LeetCode 124. Binary Tree Maximum Path Sum - 二叉树中的最大路径和
// 2. LeetCode 687. Longest Univalue Path - 最长同值路径
// 3. 牛客NC6. 二叉树中的最大路径和 - 与LeetCode 124相同

public class DiameterOfBinaryTree {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode() {}

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    // 全局变量，记录最大直径
    private int maxDiameter;

    // 提交如下的方法
    public int diameterOfBinaryTree(TreeNode root) {
        maxDiameter = 0;
        height(root);
        return maxDiameter;
    }
    
    /**
     * 计算二叉树的高度，同时更新最大直径
     * 
     * @param node 当前节点
     * @return 以当前节点为根的子树的高度
     */
    private int height(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的高度
        int leftHeight = height(node.left);
        int rightHeight = height(node.right);
        
        // 更新最大直径：当前节点的直径 = 左子树高度 + 右子树高度
        maxDiameter = Math.max(maxDiameter, leftHeight + rightHeight);
        
        // 返回当前节点的高度：左右子树高度的最大值 + 1
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // 方法2: 使用数组返回多个值（更工程化的写法）
    public int diameterOfBinaryTree2(TreeNode root) {
        int[] result = new int[1]; // result[0]存储最大直径
        height2(root, result);
        return result[0];
    }
    
    private int height2(TreeNode node, int[] result) {
        if (node == null) {
            return 0;
        }
        
        int leftHeight = height2(node.left, result);
        int rightHeight = height2(node.right, result);
        
        // 更新最大直径
        result[0] = Math.max(result[0], leftHeight + rightHeight);
        
        return Math.max(leftHeight, rightHeight) + 1;
    }

    // 测试用例
    public static void main(String[] args) {
        DiameterOfBinaryTree solution = new DiameterOfBinaryTree();

        // 测试用例1:
        //       1
        //      / \
        //     2   3
        //    / \     
        //   4   5    
        // 直径路径: 4->2->1->3 或 5->2->1->3，长度为3
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.left.left = new TreeNode(4);
        root1.left.right = new TreeNode(5);
        
        int result1 = solution.diameterOfBinaryTree(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出3

        // 测试用例2: 直径不经过根节点
        //       1
        //      / \
        //     2   3
        //    / \     
        //   4   5    
        //  /     \
        // 6       7
        // 直径路径: 6->4->2->5->7，长度为4
        TreeNode root2 = new TreeNode(1);
        root2.left = new TreeNode(2);
        root2.right = new TreeNode(3);
        root2.left.left = new TreeNode(4);
        root2.left.right = new TreeNode(5);
        root2.left.left.left = new TreeNode(6);
        root2.left.right.right = new TreeNode(7);
        
        int result2 = solution.diameterOfBinaryTree(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出4

        // 测试用例3: 单节点树
        TreeNode root3 = new TreeNode(1);
        int result3 = solution.diameterOfBinaryTree(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出0

        // 测试用例4: 空树
        TreeNode root4 = null;
        int result4 = solution.diameterOfBinaryTree(root4);
        System.out.println("测试用例4结果: " + result4); // 应该输出0

        // 测试用例5: 退化为链表的树
        //       1
        //        \
        //         2
        //          \
        //           3
        // 直径路径: 1->2->3，长度为2
        TreeNode root5 = new TreeNode(1);
        root5.right = new TreeNode(2);
        root5.right.right = new TreeNode(3);
        
        int result5 = solution.diameterOfBinaryTree(root5);
        System.out.println("测试用例5结果: " + result5); // 应该输出2

        // 补充题目测试: LeetCode 687 - 最长同值路径
        System.out.println("\n=== 补充题目测试: 最长同值路径 ===");
        //       5
        //      / \
        //     4   5
        //    / \   \
        //   1   1   5
        TreeNode root6 = new TreeNode(5);
        root6.left = new TreeNode(4);
        root6.right = new TreeNode(5);
        root6.left.left = new TreeNode(1);
        root6.left.right = new TreeNode(1);
        root6.right.right = new TreeNode(5);
        
        int longestUnivalue = longestUnivaluePath(root6);
        System.out.println("最长同值路径: " + longestUnivalue); // 应该输出2
    }
    
    // 补充题目: LeetCode 687. Longest Univalue Path
    // 题目链接: https://leetcode.cn/problems/longest-univalue-path/
    // 计算二叉树中最长的同值路径长度
    private static int maxUnivaluePath = 0;
    
    public static int longestUnivaluePath(TreeNode root) {
        maxUnivaluePath = 0;
        univaluePathLength(root);
        return maxUnivaluePath;
    }
    
    private static int univaluePathLength(TreeNode node) {
        if (node == null) {
            return 0;
        }
        
        int leftLength = univaluePathLength(node.left);
        int rightLength = univaluePathLength(node.right);
        
        // 计算左右子树的同值路径长度
        int leftUnivalue = 0, rightUnivalue = 0;
        
        if (node.left != null && node.left.val == node.val) {
            leftUnivalue = leftLength + 1;
        }
        
        if (node.right != null && node.right.val == node.val) {
            rightUnivalue = rightLength + 1;
        }
        
        // 更新最大同值路径长度
        maxUnivaluePath = Math.max(maxUnivaluePath, leftUnivalue + rightUnivalue);
        
        // 返回以当前节点为起点的最长同值路径长度
        return Math.max(leftUnivalue, rightUnivalue);
    }
}