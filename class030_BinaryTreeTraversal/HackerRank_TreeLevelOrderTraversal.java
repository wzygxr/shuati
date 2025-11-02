package class036;

import java.util.*;

// HackerRank Tree: Level Order Traversal
// 题目链接: https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
// 题目大意: 给你一个二叉树的根节点，按照层序遍历的方式打印所有节点的值，从左到右，一层一层地打印。

class Node {
    Node left;
    Node right;
    int data;
    
    Node(int data) {
        this.data = data;
        left = null;
        right = null;
    }
}

class HackerRank_TreeLevelOrderTraversal {
    
    /**
     * 层序遍历实现
     * 思路:
     * 1. 使用队列进行层序遍历
     * 2. 从根节点开始，将节点加入队列
     * 3. 当队列不为空时，取出队首节点并打印其值
     * 4. 将该节点的左右子节点（如果存在）加入队列
     * 5. 重复步骤3-4直到队列为空
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
     */
    public static void levelOrder(Node root) {
        if (root == null) {
            return;
        }
        
        // 使用队列存储待访问的节点
        Queue<Node> queue = new LinkedList<>();
        queue.offer(root);
        
        // 当队列不为空时继续遍历
        while (!queue.isEmpty()) {
            // 取出队首节点
            Node current = queue.poll();
            
            // 打印当前节点的值
            System.out.print(current.data + " ");
            
            // 将左右子节点加入队列（如果存在）
            if (current.left != null) {
                queue.offer(current.left);
            }
            if (current.right != null) {
                queue.offer(current.right);
            }
        }
    }
    
    // 以下代码是HackerRank提供的测试框架，无需修改
    public static Node insert(Node root, int data) {
        if(root == null) {
            return new Node(data);
        } else {
            Node cur;
            if(data <= root.data) {
                cur = insert(root.left, data);
                root.left = cur;
            } else {
                cur = insert(root.right, data);
                root.right = cur;
            }
            return root;
        }
    }
    
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        int t = scan.nextInt();
        Node root = null;
        while(t-- > 0) {
            int data = scan.nextInt();
            root = insert(root, data);
        }
        scan.close();
        levelOrder(root);
    }
}