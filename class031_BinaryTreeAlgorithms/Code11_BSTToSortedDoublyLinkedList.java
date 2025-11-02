package class037;

// LeetCode 426. Convert Binary Search Tree to Sorted Doubly Linked List
// 题目链接: https://leetcode.com/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
// 题目描述: 将一个二叉搜索树就地转换为已排序的循环双向链表。
// 对于双向循环链表，左右子指针分别作为前驱和后继指针。
//
// 解题思路:
// 1. 利用BST的中序遍历特性，可以按升序访问所有节点
// 2. 在中序遍历过程中，维护前一个访问的节点(prev)
// 3. 将当前节点与前一个节点连接起来
// 4. 遍历完成后，将首尾节点连接形成循环链表
//
// 时间复杂度: O(n) - n为树中节点的数量，每个节点访问一次
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是将BST转换为排序双向链表的标准解法

public class Code11_BSTToSortedDoublyLinkedList {
    
    // 二叉树节点定义
    static class Node {
        public int val;
        public Node left;
        public Node right;
        
        public Node() {}
        
        public Node(int val) {
            this.val = val;
        }
        
        public Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    // 全局变量用于跟踪前一个节点和头节点
    private Node prev = null;
    private Node head = null;
    
    /**
     * 将二叉搜索树转换为排序的循环双向链表
     * 
     * @param root 二叉搜索树的根节点
     * @return 排序的循环双向链表的头节点
     */
    public Node treeToDoublyList(Node root) {
        // 边界条件：空树
        if (root == null) {
            return null;
        }
        
        // 重置全局变量
        prev = null;
        head = null;
        
        // 中序遍历构建双向链表
        inorderTraversal(root);
        
        // 连接首尾节点形成循环链表
        if (head != null && prev != null) {
            prev.right = head;
            head.left = prev;
        }
        
        return head;
    }
    
    /**
     * 中序遍历并构建双向链表
     * 
     * @param node 当前节点
     */
    private void inorderTraversal(Node node) {
        // 基础情况：空节点
        if (node == null) {
            return;
        }
        
        // 递归处理左子树
        inorderTraversal(node.left);
        
        // 处理当前节点
        if (prev == null) {
            // 第一个节点，设置为头节点
            head = node;
        } else {
            // 将当前节点与前一个节点连接
            prev.right = node;
            node.left = prev;
        }
        
        // 更新前一个节点
        prev = node;
        
        // 递归处理右子树
        inorderTraversal(node.right);
    }
    
    // 测试用例
    public static void main(String[] args) {
        Code11_BSTToSortedDoublyLinkedList solution = new Code11_BSTToSortedDoublyLinkedList();
        
        // 测试用例1:
        // 构建BST:
        //       4
        //      / \
        //     2   5
        //    / \
        //   1   3
        Node root1 = new Node(4);
        root1.left = new Node(2);
        root1.right = new Node(5);
        root1.left.left = new Node(1);
        root1.left.right = new Node(3);
        
        Node result1 = solution.treeToDoublyList(root1);
        
        // 遍历循环双向链表验证结果
        System.out.print("测试用例1结果: ");
        if (result1 != null) {
            Node current = result1;
            do {
                System.out.print(current.val + " ");
                current = current.right;
            } while (current != result1);
        }
        System.out.println(); // 应该输出: 1 2 3 4 5
        
        // 测试用例2: 空树
        Node root2 = null;
        Node result2 = solution.treeToDoublyList(root2);
        System.out.println("测试用例2结果: " + result2); // 应该输出: null
        
        // 测试用例3: 只有根节点
        Node root3 = new Node(1);
        Node result3 = solution.treeToDoublyList(root3);
        System.out.print("测试用例3结果: ");
        if (result3 != null) {
            Node current = result3;
            do {
                System.out.print(current.val + " ");
                current = current.right;
            } while (current != result3);
        }
        System.out.println(); // 应该输出: 1
    }
}