package class037;

import java.util.*;

// LeetCode 297. Serialize and Deserialize Binary Tree
// 题目链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
// 题目描述: 设计一个算法来序列化和反序列化二叉树。
// 序列化是将一个数据结构或者对象转换为连续的比特位的过程，进而可以将转换后的数据存储在一个文件或内存中，
// 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
//
// 解题思路:
// 1. 前序遍历序列化：使用特殊字符表示空节点
// 2. 递归反序列化：根据前序遍历顺序重建二叉树
// 3. 使用队列辅助反序列化：更直观的迭代方法
//
// 时间复杂度: 
//   - 序列化: O(n) - 每个节点访问一次
//   - 反序列化: O(n) - 每个节点处理一次
// 空间复杂度: O(n) - 需要存储序列化字符串或使用递归栈
// 是否为最优解: 是，这是序列化二叉树的标准方法

// 补充题目:
// 1. LeetCode 449. Serialize and Deserialize BST - BST的序列化与反序列化
// 2. LeetCode 428. Serialize and Deserialize N-ary Tree - N叉树的序列化
// 3. 剑指Offer 37. 序列化二叉树 - 与LeetCode 297相同

public class SerializeDeserializeBinaryTree {

    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int x) {
            val = x;
        }
    }

    // 序列化分隔符
    private static final String SEPARATOR = ",";
    private static final String NULL_NODE = "null";

    // 方法1: 前序遍历序列化（递归）
    // 核心思想: 使用前序遍历，空节点用"null"表示
    public String serializePreorder(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }
    
    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NULL_NODE).append(SEPARATOR);
            return;
        }
        
        // 前序遍历：根->左->右
        sb.append(node.val).append(SEPARATOR);
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }

    // 方法2: 层序遍历序列化（BFS）
    // 核心思想: 使用队列进行层序遍历，更符合直观理解
    public String serializeLevelOrder(TreeNode root) {
        if (root == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            
            if (node == null) {
                sb.append(NULL_NODE).append(SEPARATOR);
                continue;
            }
            
            sb.append(node.val).append(SEPARATOR);
            queue.offer(node.left);
            queue.offer(node.right);
        }
        
        return sb.toString();
    }

    // 反序列化方法（前序遍历版本）
    public TreeNode deserializePreorder(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        String[] nodes = data.split(SEPARATOR);
        Queue<String> queue = new LinkedList<>(Arrays.asList(nodes));
        return deserializeHelper(queue);
    }
    
    private TreeNode deserializeHelper(Queue<String> queue) {
        if (queue.isEmpty()) {
            return null;
        }
        
        String val = queue.poll();
        if (val.equals(NULL_NODE)) {
            return null;
        }
        
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeHelper(queue);
        node.right = deserializeHelper(queue);
        
        return node;
    }

    // 反序列化方法（层序遍历版本）
    public TreeNode deserializeLevelOrder(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        String[] nodes = data.split(SEPARATOR);
        if (nodes.length == 0) {
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int index = 1;
        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode node = queue.poll();
            
            // 处理左子节点
            if (index < nodes.length && !nodes[index].equals(NULL_NODE)) {
                node.left = new TreeNode(Integer.parseInt(nodes[index]));
                queue.offer(node.left);
            }
            index++;
            
            // 处理右子节点
            if (index < nodes.length && !nodes[index].equals(NULL_NODE)) {
                node.right = new TreeNode(Integer.parseInt(nodes[index]));
                queue.offer(node.right);
            }
            index++;
        }
        
        return root;
    }

    // 提交如下的方法（使用层序遍历版本，更直观）
    public String serialize(TreeNode root) {
        return serializeLevelOrder(root);
    }
    
    public TreeNode deserialize(String data) {
        return deserializeLevelOrder(data);
    }

    // 测试用例
    public static void main(String[] args) {
        SerializeDeserializeBinaryTree codec = new SerializeDeserializeBinaryTree();

        // 测试用例1:
        //       1
        //      / \
        //     2   3
        //        / \
        //       4   5
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.right.left = new TreeNode(4);
        root1.right.right = new TreeNode(5);
        
        // 序列化
        String serialized = codec.serialize(root1);
        System.out.println("序列化结果: " + serialized);
        // 应该输出类似: "1,2,3,null,null,4,5,null,null,null,null,"
        
        // 反序列化
        TreeNode deserialized = codec.deserialize(serialized);
        System.out.println("反序列化验证:");
        printLevelOrder(deserialized); // 应该输出与原树相同的结构

        // 测试用例2: 空树
        TreeNode root2 = null;
        String serialized2 = codec.serialize(root2);
        System.out.println("空树序列化: " + serialized2);
        TreeNode deserialized2 = codec.deserialize(serialized2);
        System.out.println("空树反序列化验证: " + (deserialized2 == null));

        // 测试用例3: 单节点树
        TreeNode root3 = new TreeNode(1);
        String serialized3 = codec.serialize(root3);
        System.out.println("单节点树序列化: " + serialized3);
        TreeNode deserialized3 = codec.deserialize(serialized3);
        System.out.println("单节点树反序列化验证: " + (deserialized3.val == 1));

        // 补充题目测试: BST的序列化与反序列化
        System.out.println("\n=== 补充题目测试: BST序列化与反序列化 ===");
        TreeNode bstRoot = new TreeNode(2);
        bstRoot.left = new TreeNode(1);
        bstRoot.right = new TreeNode(3);
        
        String bstSerialized = serializeBST(bstRoot);
        System.out.println("BST序列化: " + bstSerialized);
        TreeNode bstDeserialized = deserializeBST(bstSerialized);
        System.out.println("BST反序列化验证: " + isValidBST(bstDeserialized));
    }
    
    // 辅助方法：层序遍历打印二叉树（用于验证）
    private static void printLevelOrder(TreeNode root) {
        if (root == null) {
            System.out.println("空树");
            return;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> level = new ArrayList<>();
            
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(String.valueOf(node.val));
                
                if (node.left != null) {
                    queue.offer(node.left);
                }
                if (node.right != null) {
                    queue.offer(node.right);
                }
            }
            
            System.out.println(String.join(" ", level));
        }
    }
    
    // 补充题目: LeetCode 449. Serialize and Deserialize BST
    // BST的序列化可以利用BST的性质进行优化
    public static String serializeBST(TreeNode root) {
        if (root == null) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder();
        serializeBSTHelper(root, sb);
        return sb.toString();
    }
    
    private static void serializeBSTHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            return;
        }
        
        sb.append(node.val).append(SEPARATOR);
        serializeBSTHelper(node.left, sb);
        serializeBSTHelper(node.right, sb);
    }
    
    public static TreeNode deserializeBST(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        String[] nodes = data.split(SEPARATOR);
        Queue<Integer> queue = new LinkedList<>();
        for (String node : nodes) {
            queue.offer(Integer.parseInt(node));
        }
        
        return deserializeBSTHelper(queue, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
    private static TreeNode deserializeBSTHelper(Queue<Integer> queue, int lower, int upper) {
        if (queue.isEmpty()) {
            return null;
        }
        
        int val = queue.peek();
        if (val < lower || val > upper) {
            return null;
        }
        
        queue.poll();
        TreeNode node = new TreeNode(val);
        node.left = deserializeBSTHelper(queue, lower, val);
        node.right = deserializeBSTHelper(queue, val, upper);
        
        return node;
    }
    
    // 验证BST是否有效
    private static boolean isValidBST(TreeNode root) {
        return isValidBSTHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
    }
    
    private static boolean isValidBSTHelper(TreeNode node, long lower, long upper) {
        if (node == null) {
            return true;
        }
        
        if (node.val <= lower || node.val >= upper) {
            return false;
        }
        
        return isValidBSTHelper(node.left, lower, node.val) && 
               isValidBSTHelper(node.right, node.val, upper);
    }
}