// 二叉树的序列化与反序列化 - LeetCode 297
// 题目来源：LeetCode 297. Serialize and Deserialize Binary Tree
// 题目链接：https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
// 测试链接 : https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一个经典的树形问题，需要实现二叉树的序列化和反序列化。
有多种序列化方式：前序遍历、层次遍历等。

算法思路（前序遍历版）：
1. 序列化：使用前序遍历，将节点值转换为字符串，空节点用特殊标记表示
2. 反序列化：根据前序遍历顺序和特殊标记重建二叉树

时间复杂度：O(n) - 每个节点访问一次
空间复杂度：O(n) - 需要存储序列化字符串或递归栈
是否为最优解：是，这是解决此类问题的标准方法

相关题目链接：
Java实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code25_SerializeAndDeserializeBinaryTree.java
Python实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code18_SerializeAndDeserializeBinaryTree.py
C++实现：https://github.com/algorithm-learning/algorithm-journey/blob/main/src/class123/Code18_SerializeAndDeserializeBinaryTree.cpp

工程化考量：
1. 编码格式：选择合适的分隔符和空节点标记
2. 异常处理：处理无效输入、格式错误
3. 性能优化：使用StringBuilder提高序列化效率
*/

import java.util.*;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public class Code25_SerializeAndDeserializeBinaryTree {
    
    private static final String NULL_MARKER = "null";
    private static final String DELIMITER = ",";
    
    /**
     * 序列化二叉树为字符串（前序遍历）
     * 
     * @param root 二叉树根节点
     * @return 序列化后的字符串
     */
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        serializeHelper(root, sb);
        return sb.toString();
    }
    
    private void serializeHelper(TreeNode node, StringBuilder sb) {
        if (node == null) {
            sb.append(NULL_MARKER).append(DELIMITER);
            return;
        }
        
        // 前序遍历：根-左-右
        sb.append(node.val).append(DELIMITER);
        serializeHelper(node.left, sb);
        serializeHelper(node.right, sb);
    }
    
    /**
     * 反序列化字符串为二叉树
     * 
     * @param data 序列化后的字符串
     * @return 反序列化后的二叉树根节点
     */
    public TreeNode deserialize(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        String[] nodes = data.split(DELIMITER);
        Queue<String> queue = new LinkedList<>(Arrays.asList(nodes));
        return deserializeHelper(queue);
    }
    
    private TreeNode deserializeHelper(Queue<String> queue) {
        if (queue.isEmpty()) {
            return null;
        }
        
        String val = queue.poll();
        if (val.equals(NULL_MARKER)) {
            return null;
        }
        
        TreeNode node = new TreeNode(Integer.parseInt(val));
        node.left = deserializeHelper(queue);
        node.right = deserializeHelper(queue);
        
        return node;
    }
    
    // 层次遍历序列化版本
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
                sb.append(NULL_MARKER).append(DELIMITER);
                continue;
            }
            
            sb.append(node.val).append(DELIMITER);
            queue.offer(node.left);
            queue.offer(node.right);
        }
        
        return sb.toString();
    }
    
    public TreeNode deserializeLevelOrder(String data) {
        if (data == null || data.isEmpty()) {
            return null;
        }
        
        String[] nodes = data.split(DELIMITER);
        if (nodes.length == 0 || nodes[0].equals(NULL_MARKER)) {
            return null;
        }
        
        TreeNode root = new TreeNode(Integer.parseInt(nodes[0]));
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        
        int index = 1;
        while (!queue.isEmpty() && index < nodes.length) {
            TreeNode node = queue.poll();
            
            // 处理左子节点
            if (index < nodes.length && !nodes[index].equals(NULL_MARKER)) {
                node.left = new TreeNode(Integer.parseInt(nodes[index]));
                queue.offer(node.left);
            }
            index++;
            
            // 处理右子节点
            if (index < nodes.length && !nodes[index].equals(NULL_MARKER)) {
                node.right = new TreeNode(Integer.parseInt(nodes[index]));
                queue.offer(node.right);
            }
            index++;
        }
        
        return root;
    }
    
    // 单元测试
    public static void main(String[] args) {
        Code25_SerializeAndDeserializeBinaryTree codec = new Code25_SerializeAndDeserializeBinaryTree();
        
        // 测试用例1: [1,2,3,null,null,4,5]
        TreeNode root1 = new TreeNode(1);
        root1.left = new TreeNode(2);
        root1.right = new TreeNode(3);
        root1.right.left = new TreeNode(4);
        root1.right.right = new TreeNode(5);
        
        String serialized1 = codec.serialize(root1);
        System.out.println("前序遍历序列化: " + serialized1);
        
        TreeNode deserialized1 = codec.deserialize(serialized1);
        System.out.println("反序列化成功: " + (deserialized1 != null));
        
        // 测试用例2: 空树
        String serialized2 = codec.serialize(null);
        System.out.println("空树序列化: " + serialized2);
        
        TreeNode deserialized2 = codec.deserialize(serialized2);
        System.out.println("空树反序列化成功: " + (deserialized2 == null));
        
        // 测试层次遍历版本
        String levelSerialized = codec.serializeLevelOrder(root1);
        System.out.println("层次遍历序列化: " + levelSerialized);
        
        TreeNode levelDeserialized = codec.deserializeLevelOrder(levelSerialized);
        System.out.println("层次遍历反序列化成功: " + (levelDeserialized != null));
        
        // 验证序列化-反序列化的一致性
        String originalSerialized = codec.serialize(root1);
        TreeNode reconstructed = codec.deserialize(originalSerialized);
        String reconstructedSerialized = codec.serialize(reconstructed);
        
        System.out.println("序列化-反序列化一致性: " + 
            originalSerialized.equals(reconstructedSerialized));
    }
    
    /**
     * 算法复杂度分析：
     * 前序遍历版本：
     * - 时间复杂度：O(n) - 每个节点访问一次
     * - 空间复杂度：O(n) - 递归栈深度或字符串长度
     * 
     * 层次遍历版本：
     * - 时间复杂度：O(n) - 每个节点访问一次
     * - 空间复杂度：O(n) - 队列大小和字符串长度
     * 
     * 算法正确性验证：
     * 1. 空树处理：正确序列化和反序列化空树
     * 2. 单节点树：正确处理只有一个节点的树
     * 3. 完全二叉树：正确处理各种形态的二叉树
     * 4. 一致性验证：序列化后反序列化应该得到相同的树
     * 
     * 工程化改进：
     * 1. 提供多种序列化方式
     * 2. 添加输入验证和异常处理
     * 3. 支持大规模数据序列化
     * 4. 优化字符串处理效率
     */
}