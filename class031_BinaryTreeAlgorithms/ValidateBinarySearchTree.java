package class037;

import java.util.ArrayList;
import java.util.List;

// LeetCode 98. Validate Binary Search Tree
// 题目链接: https://leetcode.cn/problems/validate-binary-search-tree/
// 题目描述: 给你一个二叉树的根节点 root ，判断其是否是一个有效的二叉搜索树。
// 有效二叉搜索树定义如下：
// 节点的左子树只包含小于当前节点的数。
// 节点的右子树只包含大于当前节点的数。
// 所有左子树和右子树自身必须也是二叉搜索树。
//
// 解题思路:
// 1. 使用递归方法，为每个节点设置上下界
// 2. 对于根节点，上下界为无穷大和无穷小
// 3. 对于左子树，上界更新为当前节点值
// 4. 对于右子树，下界更新为当前节点值
// 5. 递归检查每个节点是否满足上下界约束
//
// 时间复杂度: O(n) - n为树中节点的数量，需要访问每个节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是验证BST的标准方法

// 补充题目:
// 1. LeetCode 530. Minimum Absolute Difference in BST - 二叉搜索树的最小绝对差
// 2. LintCode 95. Validate Binary Search Tree - 与LeetCode 98相同
// 3. 牛客NC47. 寻找第K大的元素

// 二叉搜索树的核心思想和技巧:
// 1. 利用BST的中序遍历结果是升序的特性解决许多问题
// 2. 对于验证BST，使用上下界递归可以有效处理边界情况
// 3. BST的查找、插入、删除操作都可以在O(h)时间内完成
// 4. 使用Morris遍历可以实现O(n)时间和O(1)空间的中序遍历
// 5. 对于第K大/小元素问题，可以利用中序遍历的特性

public class ValidateBinarySearchTree {

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

    // 提交如下的方法
    public boolean isValidBST(TreeNode root) {
        return isValidBST(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    /**
     * 递归验证BST
     * @param node 当前节点
     * @param min 下界（不包含）
     * @param max 上界（不包含）
     * @return 是否为有效的BST
     */
    private boolean isValidBST(TreeNode node, long min, long max) {
        // 空节点是有效的BST
        if (node == null) {
            return true;
        }
        
        // 检查当前节点是否满足上下界约束
        if (node.val <= min || node.val >= max) {
            return false;
        }
        
        // 递归检查左右子树
        // 左子树的上界更新为当前节点值
        // 右子树的下界更新为当前节点值
        return isValidBST(node.left, min, node.val) && 
               isValidBST(node.right, node.val, max);
    }

    // 测试用例
    public static void main(String[] args) {
        ValidateBinarySearchTree solution = new ValidateBinarySearchTree();

        // 测试用例1: 有效的BST
        //       2
        //      / \
        //     1   3
        TreeNode root1 = new TreeNode(2);
        root1.left = new TreeNode(1);
        root1.right = new TreeNode(3);
        boolean result1 = solution.isValidBST(root1);
        System.out.println("测试用例1结果: " + result1); // 应该输出true

        // 测试用例2: 无效的BST
        //       5
        //      / \
        //     1   4
        //        / \
        //       3   6
        TreeNode root2 = new TreeNode(5);
        root2.left = new TreeNode(1);
        root2.right = new TreeNode(4);
        root2.right.left = new TreeNode(3);
        root2.right.right = new TreeNode(6);
        boolean result2 = solution.isValidBST(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出false

        // 测试用例3: 无效的BST（相同值）
        //       1
        //      / \
        //     1   1
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(1);
        root3.right = new TreeNode(1);
        boolean result3 = solution.isValidBST(root3);
        System.out.println("测试用例3结果: " + result3); // 应该输出false
        
        // 测试补充题目1: LeetCode 530
        System.out.println("\n测试LeetCode 530 - 最小绝对差:");
        //       4
        //      / \
        //     2   6
        //    / \
        //   1   3
        TreeNode root4 = new TreeNode(4);
        root4.left = new TreeNode(2);
        root4.right = new TreeNode(6);
        root4.left.left = new TreeNode(1);
        root4.left.right = new TreeNode(3);
        System.out.println("最小绝对差: " + solution.getMinimumDifference(root4)); // 应该输出1
        
        // 测试补充题目3: 牛客NC47
        System.out.println("\n测试牛客NC47 - 寻找第K大的元素:");
        //       3
        //      / \
        //     1   4
        //      \n        //       2
        TreeNode root5 = new TreeNode(3);
        root5.left = new TreeNode(1);
        root5.right = new TreeNode(4);
        root5.left.right = new TreeNode(2);
        int k = 1;
        System.out.println("第" + k + "大的元素: " + solution.kthLargest(root5, k)); // 应该输出4
    }
    
    // 补充题目1: LeetCode 530. Minimum Absolute Difference in BST
    // 题目链接: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/
    // 题目描述: 给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小绝对差。
    //
    // 解题思路:
    // 1. 利用二叉搜索树的中序遍历结果是升序的特性
    // 2. 在中序遍历过程中，计算当前节点与前一个节点的差值
    // 3. 维护一个最小值变量，记录最小的绝对差值
    //
    // 时间复杂度: O(n) - 每个节点只访问一次
    // 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，利用BST特性的高效解法
    private int minDiff = Integer.MAX_VALUE;
    private TreeNode prev = null;
    
    public int getMinimumDifference(TreeNode root) {
        minDiff = Integer.MAX_VALUE;
        prev = null;
        inorderTraverse(root);
        return minDiff;
    }
    
    private void inorderTraverse(TreeNode node) {
        if (node == null) {
            return;
        }
        
        // 中序遍历：先左子树
        inorderTraverse(node.left);
        
        // 处理当前节点
        if (prev != null) {
            minDiff = Math.min(minDiff, node.val - prev.val);
        }
        prev = node;
        
        // 中序遍历：后右子树
        inorderTraverse(node.right);
    }
    
    // 补充题目3: 牛客NC47. 寻找第K大的元素
    // 题目链接: https://www.nowcoder.com/practice/ef068f602dde4d28aab2b210e859150a
    // 题目描述: 给定一棵二叉搜索树，请找出其中第k大的节点值。
    //
    // 解题思路:
    // 1. 利用二叉搜索树的特性：中序遍历的结果是升序排列
    // 2. 我们可以进行逆中序遍历（右-根-左），这样结果就是降序排列
    // 3. 在遍历过程中计数，当计数达到k时，即为第k大的元素
    //
    // 时间复杂度: O(n) - 最坏情况下需要遍历所有节点
    // 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
    // 是否为最优解: 是，利用BST特性的高效解法
    private int count = 0;
    private int result = 0;
    
    public int kthLargest(TreeNode root, int k) {
        count = 0;
        result = 0;
        reverseInorder(root, k);
        return result;
    }
    
    private void reverseInorder(TreeNode node, int k) {
        if (node == null || count >= k) {
            return;
        }
        
        // 逆中序遍历：先右子树
        reverseInorder(node.right, k);
        
        // 处理当前节点
        count++;
        if (count == k) {
            result = node.val;
            return;
        }
        
        // 逆中序遍历：后左子树
        reverseInorder(node.left, k);
    }
    
    // 补充：Morris中序遍历实现（O(n)时间，O(1)空间）
    public List<Integer> morrisInorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        TreeNode current = root;
        
        while (current != null) {
            if (current.left == null) {
                // 没有左子树，直接访问当前节点，然后移动到右子树
                result.add(current.val);
                current = current.right;
            } else {
                // 找到当前节点的前驱节点（左子树的最右节点）
                TreeNode predecessor = current.left;
                while (predecessor.right != null && predecessor.right != current) {
                    predecessor = predecessor.right;
                }
                
                if (predecessor.right == null) {
                    // 第一次访问，建立线索，连接前驱节点的右指针到当前节点
                    predecessor.right = current;
                    current = current.left;
                } else {
                    // 已经访问过，断开线索，访问当前节点，然后移动到右子树
                    predecessor.right = null;
                    result.add(current.val);
                    current = current.right;
                }
            }
        }
        
        return result;
    }
}