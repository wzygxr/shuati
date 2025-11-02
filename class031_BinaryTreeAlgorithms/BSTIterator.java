package class037;

import java.util.Stack;

// LeetCode 173. Binary Search Tree Iterator
// 题目链接: https://leetcode.cn/problems/binary-search-tree-iterator/
// 题目描述: 实现一个二叉搜索树迭代器类BSTIterator ，表示一个按中序遍历二叉搜索树（BST）的迭代器：
// - BSTIterator(TreeNode root) 初始化 BSTIterator 类的一个对象。BST 的根节点 root 会作为构造函数的一部分给出。
// - boolean hasNext() 如果向指针右侧遍历存在数字，则返回 true ；否则返回 false 。
// - int next()将指针向右移动，然后返回指针处的数字。
//
// 解题思路:
// 1. 使用栈模拟中序遍历的递归过程
// 2. 初始化时将根节点及其所有左子节点入栈
// 3. next()方法弹出栈顶节点，处理其右子树
// 4. hasNext()方法检查栈是否为空
//
// 时间复杂度: 
//   - 构造函数: O(h) - h为树的高度
//   - next(): 平均O(1)，最坏O(h)
//   - hasNext(): O(1)
// 空间复杂度: O(h) - 栈中最多存储h个节点
// 是否为最优解: 是，这是BST迭代器的标准实现

// 补充题目:
// 1. LeetCode 94. Binary Tree Inorder Traversal - 二叉树的中序遍历
// 2. LeetCode 230. Kth Smallest Element in a BST - BST中第K小的元素
// 3. LintCode 86. Binary Search Tree Iterator - 与LeetCode 173相同

public class BSTIterator {

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

    private Stack<TreeNode> stack;

    /**
     * BSTIterator构造函数
     * 初始化时将根节点及其所有左子节点入栈
     * 
     * @param root 二叉搜索树的根节点
     */
    public BSTIterator(TreeNode root) {
        stack = new Stack<>();
        // 将根节点及其所有左子节点入栈
        pushAllLeft(root);
    }
    
    /**
     * 将节点及其所有左子节点入栈
     * 用于模拟中序遍历的左子树递归过程
     * 
     * @param node 当前节点
     */
    private void pushAllLeft(TreeNode node) {
        while (node != null) {
            stack.push(node);
            node = node.left;
        }
    }
    
    /**
     * 返回中序遍历序列的下一个元素
     * 
     * @return 下一个节点的值
     */
    public int next() {
        // 弹出栈顶节点（当前最小的节点）
        TreeNode node = stack.pop();
        // 如果该节点有右子树，将右子树及其所有左子节点入栈
        if (node.right != null) {
            pushAllLeft(node.right);
        }
        return node.val;
    }
    
    /**
     * 检查是否还有下一个元素
     * 
     * @return 如果还有下一个元素返回true，否则返回false
     */
    public boolean hasNext() {
        return !stack.isEmpty();
    }

    // 测试用例
    public static void main(String[] args) {
        // 构造测试BST:
        //       7
        //      / \
        //     3   15
        //        /  \
        //       9    20
        TreeNode root = new TreeNode(7);
        root.left = new TreeNode(3);
        root.right = new TreeNode(15);
        root.right.left = new TreeNode(9);
        root.right.right = new TreeNode(20);

        // 创建BST迭代器
        BSTIterator iterator = new BSTIterator(root);
        
        System.out.println("BST中序遍历结果:");
        // 应该按顺序输出: 3, 7, 9, 15, 20
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println(); // 换行

        // 测试用例2: 空树
        System.out.println("\n=== 测试用例2: 空树 ===");
        BSTIterator emptyIterator = new BSTIterator(null);
        System.out.println("空树hasNext: " + emptyIterator.hasNext()); // 应该输出false

        // 测试用例3: 单节点树
        System.out.println("\n=== 测试用例3: 单节点树 ===");
        TreeNode singleNode = new TreeNode(1);
        BSTIterator singleIterator = new BSTIterator(singleNode);
        System.out.println("单节点树遍历:");
        while (singleIterator.hasNext()) {
            System.out.print(singleIterator.next() + " "); // 应该输出1
        }
        System.out.println();

        // 补充题目测试: LeetCode 230 - BST中第K小的元素
        System.out.println("\n=== 补充题目测试: BST中第K小的元素 ===");
        int k = 3;
        int kthSmallest = kthSmallest(root, k);
        System.out.println("第" + k + "小的元素: " + kthSmallest); // 应该输出9
    }
    
    // 补充题目: LeetCode 230. Kth Smallest Element in a BST
    // 题目链接: https://leetcode.cn/problems/kth-smallest-element-in-a-bst/
    // 使用BST迭代器思路找到第K小的元素
    public static int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode current = root;
        int count = 0;
        
        while (current != null || !stack.isEmpty()) {
            // 将当前节点及其所有左子节点入栈
            while (current != null) {
                stack.push(current);
                current = current.left;
            }
            
            // 弹出栈顶节点（当前最小的节点）
            current = stack.pop();
            count++;
            
            // 如果找到第K小的元素，返回
            if (count == k) {
                return current.val;
            }
            
            // 处理右子树
            current = current.right;
        }
        
        return -1; // 理论上不会执行到这里
    }
    
    // 方法2: 递归实现中序遍历找到第K小的元素
    private static int result;
    private static int count;
    
    public static int kthSmallestRecursive(TreeNode root, int k) {
        count = 0;
        result = -1;
        inorderTraverse(root, k);
        return result;
    }
    
    private static void inorderTraverse(TreeNode node, int k) {
        if (node == null || count >= k) {
            return;
        }
        
        // 中序遍历：先左子树
        inorderTraverse(node.left, k);
        
        // 处理当前节点
        count++;
        if (count == k) {
            result = node.val;
            return;
        }
        
        // 中序遍历：后右子树
        inorderTraverse(node.right, k);
    }
}