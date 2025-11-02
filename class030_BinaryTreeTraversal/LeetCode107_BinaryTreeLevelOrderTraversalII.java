package class036;

import java.util.*;

// LeetCode 107. 二叉树的层序遍历 II
// 题目链接: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
// 题目大意: 给你二叉树的根节点 root ，返回其节点值 自底向上的层序遍历 。（即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）

public class LeetCode107_BinaryTreeLevelOrderTraversalII {
    
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
     * 方法1: 先正常层序遍历，再反转结果
     * 思路:
     * 1. 使用队列进行正常的层序遍历，从上到下收集每层节点值
     * 2. 遍历完成后，将结果列表反转，得到自底向上的遍历结果
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次，反转操作是O(L)，L为层数
     * 空间复杂度: O(n) - 存储队列和结果
     */
    public static List<List<Integer>> levelOrderBottom1(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> level = new ArrayList<>();
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode cur = queue.poll();
                    level.add(cur.val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                
                // 将当前层的结果添加到最终答案中
                ans.add(level);
            }
            
            // 反转结果，得到自底向上的遍历
            Collections.reverse(ans);
        }
        return ans;
    }
    
    /**
     * 方法2: 使用栈存储中间结果
     * 思路:
     * 1. 使用队列进行正常的层序遍历
     * 2. 使用栈存储每层的结果
     * 3. 遍历完成后，从栈中弹出结果，得到自底向上的遍历结果
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列、栈和结果
     */
    public static List<List<Integer>> levelOrderBottom2(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            Stack<List<Integer>> stack = new Stack<>();
            queue.offer(root);
            
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> level = new ArrayList<>();
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode cur = queue.poll();
                    level.add(cur.val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                
                // 将当前层的结果压入栈中
                stack.push(level);
            }
            
            // 从栈中弹出结果，得到自底向上的遍历
            while (!stack.isEmpty()) {
                ans.add(stack.pop());
            }
        }
        return ans;
    }
    
    /**
     * 方法3: 在遍历过程中直接在列表开头插入
     * 思路:
     * 1. 使用队列进行正常的层序遍历
     * 2. 每层遍历完成后，将结果插入到结果列表的开头
     * 3. 这样最终结果就是自底向上的遍历
     * 时间复杂度: O(n) - n是节点数量，每个节点访问一次
     * 空间复杂度: O(n) - 存储队列和结果
     * 注意: 在列表开头插入元素的时间复杂度是O(L)，L为当前列表长度，总体时间复杂度仍为O(n)
     */
    public static List<List<Integer>> levelOrderBottom3(TreeNode root) {
        List<List<Integer>> ans = new ArrayList<>();
        if (root != null) {
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            
            while (!queue.isEmpty()) {
                int size = queue.size();
                List<Integer> level = new ArrayList<>();
                
                // 处理当前层的所有节点
                for (int i = 0; i < size; i++) {
                    TreeNode cur = queue.poll();
                    level.add(cur.val);
                    
                    // 将子节点加入队列，供下一层处理
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                
                // 将当前层的结果插入到结果列表的开头
                ans.add(0, level);
            }
        }
        return ans;
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
        System.out.println(levelOrderBottom1(root1));
        
        System.out.println("方法2结果:");
        System.out.println(levelOrderBottom2(root1));
        
        System.out.println("方法3结果:");
        System.out.println(levelOrderBottom3(root1));
        
        // 测试用例2: [1]
        TreeNode root2 = new TreeNode(1);
        System.out.println("单节点树结果:");
        System.out.println(levelOrderBottom1(root2));
        
        // 测试用例3: []
        TreeNode root3 = null;
        System.out.println("空树结果:");
        System.out.println(levelOrderBottom1(root3));
    }
}