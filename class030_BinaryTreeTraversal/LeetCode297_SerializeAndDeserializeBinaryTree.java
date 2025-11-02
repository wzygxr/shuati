package class036;

import java.util.*;

// LeetCode 297. 二叉树的序列化与反序列化
// 题目链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
// 题目大意: 设计一个算法来序列化和反序列化二叉树。
// 序列化是将一个数据结构或者对象转换为连续的比特位，进而可以将转换后的数据存储在一个文件或内存缓冲区中，
// 并且在需要的时候可以恢复成原来的数据结构或对象。

public class LeetCode297_SerializeAndDeserializeBinaryTree {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    /**
     * 方法: 使用层序遍历进行序列化和反序列化
     * 思路:
     * 1. 序列化: 使用BFS层序遍历，将每个节点的值转换为字符串，空节点用"null"表示
     * 2. 反序列化: 将序列化的字符串解析为节点值列表，然后使用BFS方式重建树
     * 时间复杂度: O(n) - n是树中节点的数量
     * 空间复杂度: O(n) - 存储序列化字符串和重建树所需的队列空间
     */
    
    /**
     * 序列化二叉树
     * @param root 二叉树的根节点
     * @return 序列化后的字符串
     */
    public static String serialize(TreeNode root) {
        if (root == null) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        boolean first = true;
        
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            
            if (!first) {
                sb.append(",");
            }
            first = false;
            
            if (node == null) {
                sb.append("null");
            } else {
                sb.append(node.val);
                queue.offer(node.left);
                queue.offer(node.right);
            }
        }
        
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * 反序列化二叉树
     * @param data 序列化后的字符串
     * @return 二叉树的根节点
     */
    public static TreeNode deserialize(String data) {
        if (data == null || data.equals("[]")) {
            return null;
        }
        
        // 解析字符串为节点值列表
        String[] values = data.substring(1, data.length() - 1).split(",");
        if (values.length == 0 || values[0].equals("null")) {
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.parseInt(values[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int i = 1;
        
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            
            // 处理左子节点
            if (!values[i].equals("null")) {
                node.left = new TreeNode(Integer.parseInt(values[i]));
                queue.offer(node.left);
            }
            i++;
            
            // 处理右子节点
            if (i < values.length && !values[i].equals("null")) {
                node.right = new TreeNode(Integer.parseInt(values[i]));
                queue.offer(node.right);
            }
            i++;
        }
        
        return root;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试二叉树:
        //       1
        //      / \
        //     2   3
        //        / \
        //       4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        
        // 序列化
        String serialized = serialize(root);
        System.out.println("序列化结果: " + serialized);
        
        // 反序列化
        TreeNode deserialized = deserialize(serialized);
        System.out.println("反序列化结果: " + serialize(deserialized));
    }
}