package class037;

import java.util.HashMap;
import java.util.Map;

// LeetCode 105. Construct Binary Tree from Preorder and Inorder Traversal
// 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
// 题目描述: 给定两个整数数组 preorder 和 inorder ，其中 preorder 是二叉树的前序遍历，
// inorder 是同一棵树的中序遍历，请构造二叉树并返回其根节点。
//
// 解题思路:
// 1. 递归构建：前序遍历的第一个元素是根节点
// 2. 在中序遍历中找到根节点的位置，确定左右子树的范围
// 3. 递归构建左右子树
// 4. 使用HashMap优化中序遍历中查找根节点位置的时间
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点处理一次
// 空间复杂度: O(n) - 存储中序遍历的索引映射，递归栈深度O(h)
// 是否为最优解: 是，这是构建二叉树的标准方法

// 补充题目:
// 1. LeetCode 106. Construct Binary Tree from Inorder and Postorder Traversal
// 2. LeetCode 889. Construct Binary Tree from Preorder and Postorder Traversal
// 3. 牛客NC12. 重建二叉树 - 与LeetCode 105相同

public class ConstructBinaryTree {

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

    // 存储中序遍历中值到索引的映射，用于快速查找
    private Map<Integer, Integer> inorderIndexMap;

    /**
     * 根据前序遍历和中序遍历构建二叉树
     * 
     * @param preorder 前序遍历数组
     * @param inorder 中序遍历数组
     * @return 构建的二叉树的根节点
     */
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        // 边界条件检查
        if (preorder == null || inorder == null || 
            preorder.length != inorder.length || preorder.length == 0) {
            return null;
        }
        
        // 构建中序遍历的索引映射
        inorderIndexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            inorderIndexMap.put(inorder[i], i);
        }
        
        return buildTreeHelper(preorder, 0, preorder.length - 1, 
                              inorder, 0, inorder.length - 1);
    }
    
    /**
     * 递归构建二叉树的辅助方法
     * 
     * @param preorder 前序遍历数组
     * @param preStart 前序遍历起始索引
     * @param preEnd 前序遍历结束索引
     * @param inorder 中序遍历数组
     * @param inStart 中序遍历起始索引
     * @param inEnd 中序遍历结束索引
     * @return 构建的子树根节点
     */
    private TreeNode buildTreeHelper(int[] preorder, int preStart, int preEnd,
                                   int[] inorder, int inStart, int inEnd) {
        // 递归终止条件：没有节点需要处理
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        
        // 前序遍历的第一个元素是根节点
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);
        
        // 在中序遍历中找到根节点的位置
        int rootIndexInInorder = inorderIndexMap.get(rootVal);
        
        // 计算左子树的大小
        int leftSubtreeSize = rootIndexInInorder - inStart;
        
        // 递归构建左子树
        // 前序遍历中左子树范围: [preStart + 1, preStart + leftSubtreeSize]
        // 中序遍历中左子树范围: [inStart, rootIndexInInorder - 1]
        root.left = buildTreeHelper(preorder, preStart + 1, preStart + leftSubtreeSize,
                                  inorder, inStart, rootIndexInInorder - 1);
        
        // 递归构建右子树
        // 前序遍历中右子树范围: [preStart + leftSubtreeSize + 1, preEnd]
        // 中序遍历中右子树范围: [rootIndexInInorder + 1, inEnd]
        root.right = buildTreeHelper(preorder, preStart + leftSubtreeSize + 1, preEnd,
                                   inorder, rootIndexInInorder + 1, inEnd);
        
        return root;
    }

    // 方法2: 使用迭代方法构建（更工程化的写法）
    public TreeNode buildTreeIterative(int[] preorder, int[] inorder) {
        if (preorder == null || preorder.length == 0) {
            return null;
        }
        
        // 构建中序遍历的索引映射
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }
        
        return buildTreeIterativeHelper(preorder, 0, preorder.length - 1, 
                                      0, inorder.length - 1, indexMap);
    }
    
    private TreeNode buildTreeIterativeHelper(int[] preorder, int preStart, int preEnd,
                                            int inStart, int inEnd, 
                                            Map<Integer, Integer> indexMap) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        
        int rootVal = preorder[preStart];
        TreeNode root = new TreeNode(rootVal);
        int rootIndex = indexMap.get(rootVal);
        int leftSize = rootIndex - inStart;
        
        root.left = buildTreeIterativeHelper(preorder, preStart + 1, preStart + leftSize,
                                           inStart, rootIndex - 1, indexMap);
        root.right = buildTreeIterativeHelper(preorder, preStart + leftSize + 1, preEnd,
                                            rootIndex + 1, inEnd, indexMap);
        
        return root;
    }

    // 测试用例
    public static void main(String[] args) {
        ConstructBinaryTree solution = new ConstructBinaryTree();

        // 测试用例1:
        // 前序遍历: [3,9,20,15,7]
        // 中序遍历: [9,3,15,20,7]
        // 构建的二叉树:
        //       3
        //      / \
        //     9  20
        //       /  \
        //      15   7
        int[] preorder1 = {3, 9, 20, 15, 7};
        int[] inorder1 = {9, 3, 15, 20, 7};
        
        TreeNode root1 = solution.buildTree(preorder1, inorder1);
        System.out.println("测试用例1构建完成");
        printInorder(root1); // 应该输出: 9 3 15 20 7
        System.out.println();

        // 测试用例2: 单节点树
        int[] preorder2 = {1};
        int[] inorder2 = {1};
        TreeNode root2 = solution.buildTree(preorder2, inorder2);
        System.out.println("测试用例2构建完成");
        printInorder(root2); // 应该输出: 1
        System.out.println();

        // 测试用例3: 完全二叉树
        // 前序遍历: [1,2,4,5,3,6,7]
        // 中序遍历: [4,2,5,1,6,3,7]
        // 构建的二叉树:
        //       1
        //      / \
        //     2   3
        //    / \ / \
        //   4  5 6 7
        int[] preorder3 = {1, 2, 4, 5, 3, 6, 7};
        int[] inorder3 = {4, 2, 5, 1, 6, 3, 7};
        
        TreeNode root3 = solution.buildTree(preorder3, inorder3);
        System.out.println("测试用例3构建完成");
        printInorder(root3); // 应该输出: 4 2 5 1 6 3 7
        System.out.println();

        // 补充题目测试: LeetCode 106 - 从中序与后序遍历序列构造二叉树
        System.out.println("=== 补充题目测试: 从中序与后序遍历序列构造二叉树 ===");
        int[] inorder4 = {9, 3, 15, 20, 7};
        int[] postorder4 = {9, 15, 7, 20, 3};
        TreeNode root4 = buildTreeFromInorderPostorder(inorder4, postorder4);
        printInorder(root4); // 应该输出: 9 3 15 20 7
        System.out.println();
    }
    
    // 辅助方法：中序遍历打印二叉树（用于验证）
    private static void printInorder(TreeNode root) {
        if (root == null) {
            return;
        }
        printInorder(root.left);
        System.out.print(root.val + " ");
        printInorder(root.right);
    }
    
    // 补充题目: LeetCode 106. Construct Binary Tree from Inorder and Postorder Traversal
    // 题目链接: https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
    public static TreeNode buildTreeFromInorderPostorder(int[] inorder, int[] postorder) {
        if (inorder == null || postorder == null || 
            inorder.length != postorder.length || inorder.length == 0) {
            return null;
        }
        
        // 构建中序遍历的索引映射
        Map<Integer, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            indexMap.put(inorder[i], i);
        }
        
        return buildTreeHelperPostorder(inorder, 0, inorder.length - 1,
                                       postorder, 0, postorder.length - 1, indexMap);
    }
    
    private static TreeNode buildTreeHelperPostorder(int[] inorder, int inStart, int inEnd,
                                                   int[] postorder, int postStart, int postEnd,
                                                   Map<Integer, Integer> indexMap) {
        if (inStart > inEnd || postStart > postEnd) {
            return null;
        }
        
        // 后序遍历的最后一个元素是根节点
        int rootVal = postorder[postEnd];
        TreeNode root = new TreeNode(rootVal);
        
        int rootIndex = indexMap.get(rootVal);
        int leftSize = rootIndex - inStart;
        
        // 递归构建左子树
        root.left = buildTreeHelperPostorder(inorder, inStart, rootIndex - 1,
                                            postorder, postStart, postStart + leftSize - 1, indexMap);
        
        // 递归构建右子树
        root.right = buildTreeHelperPostorder(inorder, rootIndex + 1, inEnd,
                                             postorder, postStart + leftSize, postEnd - 1, indexMap);
        
        return root;
    }
}