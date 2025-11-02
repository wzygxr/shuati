package class037;

// LeetCode 114. Flatten Binary Tree to Linked List
// 题目链接: https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/
// 题目描述: 给你二叉树的根结点 root ，请你将它展开为一个单链表：
// - 展开后的单链表应该同样使用 TreeNode ，其中 right 子指针指向链表中下一个结点，而左子指针始终为 null 。
// - 展开后的单链表应该与二叉树先序遍历顺序相同。
//
// 解题思路:
// 1. 递归方法：后序遍历，先处理左右子树，再将左子树插入到根节点和右子树之间
// 2. 迭代方法：使用前序遍历，将节点按顺序连接
// 3. Morris遍历：O(1)空间复杂度的解法
//
// 时间复杂度: O(n) - n为树中节点的数量
// 空间复杂度: 
//   - 递归: O(h) - h为树的高度
//   - 迭代: O(n) - 需要存储前序遍历结果
//   - Morris: O(1) - 只使用常数空间
// 是否为最优解: Morris遍历是最优解，空间复杂度O(1)

// 补充题目:
// 1. LeetCode 144. Binary Tree Preorder Traversal - 二叉树的前序遍历
// 2. LeetCode 94. Binary Tree Inorder Traversal - 二叉树的中序遍历
// 3. 牛客NC5. 二叉树根节点到叶子节点的所有路径和

public class FlattenBinaryTree {

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

    // 方法1: 递归解法（后序遍历）
    // 核心思想: 先递归处理左右子树，然后将左子树插入到根节点和右子树之间
    public void flattenRecursive(TreeNode root) {
        if (root == null) {
            return;
        }
        
        // 递归处理左右子树
        flattenRecursive(root.left);
        flattenRecursive(root.right);
        
        // 保存右子树
        TreeNode right = root.right;
        
        // 将左子树移到右子树位置
        root.right = root.left;
        root.left = null;
        
        // 找到当前右子树（原左子树）的最后一个节点
        TreeNode current = root;
        while (current.right != null) {
            current = current.right;
        }
        
        // 将原右子树接到末尾
        current.right = right;
    }

    // 方法2: 迭代解法（前序遍历）
    // 核心思想: 使用栈进行前序遍历，按顺序连接节点
    public void flattenIterative(TreeNode root) {
        if (root == null) {
            return;
        }
        
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        stack.push(root);
        TreeNode prev = null;
        
        while (!stack.isEmpty()) {
            TreeNode current = stack.pop();
            
            if (prev != null) {
                prev.right = current;
                prev.left = null;
            }
            
            // 先右后左入栈，保证出栈顺序是前序遍历
            if (current.right != null) {
                stack.push(current.right);
            }
            if (current.left != null) {
                stack.push(current.left);
            }
            
            prev = current;
        }
    }

    // 方法3: Morris遍历（最优解，O(1)空间）
    // 核心思想: 利用线索二叉树的思想，在遍历过程中建立连接
    public void flattenMorris(TreeNode root) {
        TreeNode current = root;
        
        while (current != null) {
            if (current.left != null) {
                // 找到当前节点左子树的最右节点（前驱节点）
                TreeNode predecessor = current.left;
                while (predecessor.right != null) {
                    predecessor = predecessor.right;
                }
                
                // 将当前节点的右子树接到前驱节点的右边
                predecessor.right = current.right;
                // 将左子树移到右子树位置
                current.right = current.left;
                current.left = null;
            }
            
            // 移动到下一个节点
            current = current.right;
        }
    }

    // 提交如下的方法（使用Morris遍历，最优解）
    public void flatten(TreeNode root) {
        flattenMorris(root);
    }

    // 辅助方法：打印展开后的链表（用于测试）
    public void printFlattenedTree(TreeNode root) {
        TreeNode current = root;
        while (current != null) {
            System.out.print(current.val + " ");
            current = current.right;
        }
        System.out.println();
    }

    // 测试用例
    public static void main(String[] args) {
        FlattenBinaryTree solution = new FlattenBinaryTree();

        // 测试用例1:
        //       1
        //      / \
        //     2   5
        //    / \   \
        //   3   4   6
        // 展开后: 1->2->3->4->5->6
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(5);
        root1.left.left = new TreeNode(3);
        root1.left.right = new TreeNode(4);
        root1.right.right = new TreeNode(6);
        
        System.out.println("原始树前序遍历:");
        printPreorder(root1); // 应该输出: 1 2 3 4 5 6
        
        solution.flatten(root1);
        System.out.println("展开后链表:");
        solution.printFlattenedTree(root1); // 应该输出: 1 2 3 4 5 6

        // 测试用例2: 空树
        TreeNode root2 = null;
        solution.flatten(root2);
        System.out.println("空树展开结果:");
        solution.printFlattenedTree(root2); // 应该输出空行

        // 测试用例3: 只有左子树的树
        //       1
        //      /
        //     2
        //    /
        //   3
        TreeNode root3 = new TreeNode(1);
        root3.left = new TreeNode(2);
        root3.left.left = new TreeNode(3);
        
        solution.flatten(root3);
        System.out.println("只有左子树展开结果:");
        solution.printFlattenedTree(root3); // 应该输出: 1 2 3

        // 补充题目测试: 二叉树的前序遍历
        System.out.println("\n=== 补充题目测试: 二叉树的前序遍历 ===");
        TreeNode root4 = new TreeNode(1);
        root4.left = new TreeNode(2);
        root4.right = new TreeNode(3);
        root4.left.left = new TreeNode(4);
        root4.left.right = new TreeNode(5);
        
        java.util.List<Integer> preorder = preorderTraversal(root4);
        System.out.println("前序遍历结果: " + preorder); // 应该输出: [1, 2, 4, 5, 3]
    }
    
    // 辅助方法：前序遍历（递归）
    private static void printPreorder(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.print(root.val + " ");
        printPreorder(root.left);
        printPreorder(root.right);
    }
    
    // 补充题目: LeetCode 144. Binary Tree Preorder Traversal
    // 题目链接: https://leetcode.cn/problems/binary-tree-preorder-traversal/
    public static java.util.List<Integer> preorderTraversal(TreeNode root) {
        java.util.List<Integer> result = new java.util.ArrayList<>();
        if (root == null) {
            return result;
        }
        
        java.util.Stack<TreeNode> stack = new java.util.Stack<>();
        stack.push(root);
        
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            result.add(node.val);
            
            // 先右后左入栈，保证出栈顺序是根->左->右
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        
        return result;
    }
}