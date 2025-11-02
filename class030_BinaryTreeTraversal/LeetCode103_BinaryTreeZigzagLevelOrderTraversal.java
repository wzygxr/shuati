package class036;

import java.util.*;

// LeetCode 103. 二叉树的锯齿形层序遍历
// 题目链接: https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
// 题目大意: 给你二叉树的根节点 root ，返回其节点值的 锯齿形层序遍历 。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）

public class LeetCode103_BinaryTreeZigzagLevelOrderTraversal {
    
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode() {}
        TreeNode(int val) { this.val = val; }
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    /**
     * 方法1: 使用双端队列实现锯齿形层序遍历
     * 思路:
     * 1. 使用一个布尔变量记录当前层的遍历方向（从左到右或从右到左）
     * 2. 对于每一层，根据遍历方向决定是从队列头部取节点还是从尾部取节点
     * 3. 添加子节点时也根据方向决定是添加到头部还是尾部
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列和结果
     */
    public static List<List<Integer>> zigzagLevelOrder1(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root != null) {
            // 使用双端队列存储节点
            Deque<TreeNode> deque = new LinkedList<>();
            deque.offerFirst(root);
            // true表示从左到右，false表示从右到左
            boolean leftToRight = true;
            
            while (!deque.isEmpty()) {
                int size = deque.size();
                List<Integer> level = new ArrayList<>();
                
                if (leftToRight) {
                    // 从左到右遍历：从头部取节点，子节点添加到尾部
                    for (int i = 0; i < size; i++) {
                        TreeNode node = deque.pollFirst();
                        level.add(node.val);
                        // 先添加左子节点，再添加右子节点
                        if (node.left != null) deque.offerLast(node.left);
                        if (node.right != null) deque.offerLast(node.right);
                    }
                } else {
                    // 从右到左遍历：从尾部取节点，子节点添加到头部
                    for (int i = 0; i < size; i++) {
                        TreeNode node = deque.pollLast();
                        level.add(node.val);
                        // 先添加右子节点，再添加左子节点
                        if (node.right != null) deque.offerFirst(node.right);
                        if (node.left != null) deque.offerFirst(node.left);
                    }
                }
                
                ans.add(level);
                // 切换方向
                leftToRight = !leftToRight;
            }
        }
        return ans;
    }
    
    /**
     * 方法2: 使用普通队列实现锯齿形层序遍历
     * 思路:
     * 1. 使用普通队列进行层序遍历
     * 2. 使用一个布尔变量记录当前层是否需要反转
     * 3. 对于需要反转的层，在添加到结果之前进行反转
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列和结果
     */
    public static List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            // true表示从左到右，false表示从右到左
            boolean leftToRight = true;
            
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> level = new ArrayList<>();
                
                // 正常的层序遍历
                for (int i = 0; i < size; i++) {
                    TreeNode node = queue.poll();
                    level.add(node.val);
                    if (node.left != null) queue.offer(node.left);
                    if (node.right != null) queue.offer(node.right);
                }
                
                // 如果当前层需要从右到左，则反转列表
                if (!leftToRight) {
                    Collections.reverse(level);
                }
                
                ans.add(level);
                // 切换方向
                leftToRight = !leftToRight;
            }
        }
        return ans;
    }
    
    /**
     * 方法3: 使用递归实现锯齿形层序遍历
     * 思路:
     * 1. 使用递归进行深度优先遍历
     * 2. 根据层数的奇偶性决定节点值的添加方向
     * 3. 奇数层从右到左，偶数层从左到右
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(h) - h是树的高度，递归调用栈的深度
     */
    public static List<List<Integer>> zigzagLevelOrder3(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        dfs(root, 0, ans);
        return ans;
    }
    
    /**
     * 递归辅助函数
     * @param node 当前节点
     * @param level 当前层数
     * @param ans 结果列表
     */
    private static void dfs(TreeNode node, int level, List<List<Integer>> ans) {
        if (node == null) {
            return;
        }
        
        // 如果当前层还没有对应的列表，创建一个新的
        if (ans.size() <= level) {
            ans.add(new ArrayList<>());
        }
        
        // 根据层数的奇偶性决定添加方向
        if (level % 2 == 0) {
            // 偶数层：从左到右，添加到列表末尾
            ans.get(level).add(node.val);
        } else {
            // 奇数层：从右到左，添加到列表开头
            ans.get(level).add(0, node.val);
        }
        
        // 递归处理左右子树
        dfs(node.left, level + 1, ans);
        dfs(node.right, level + 1, ans);
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1: [3,9,20,null,null,15,7]
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        System.out.println("方法1结果:");
        System.out.println(zigzagLevelOrder1(root1));
        
        System.out.println("方法2结果:");
        System.out.println(zigzagLevelOrder2(root1));
        
        System.out.println("方法3结果:");
        System.out.println(zigzagLevelOrder3(root1));
        
        // 测试用例2: [1]
        TreeNode root2 = new TreeNode(1);
        System.out.println("单节点树结果:");
        System.out.println(zigzagLevelOrder1(root2));
        
        // 测试用例3: []
        TreeNode root3 = null;
        System.out.println("空树结果:");
        System.out.println(zigzagLevelOrder1(root3));
    }
}