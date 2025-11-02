// LeetCode 104. Maximum Depth of Binary Tree
// 二叉树的最大深度
// 题目来源：https://leetcode.cn/problems/maximum-depth-of-binary-tree/

import java.util.LinkedList;
import java.util.Queue;

/**
 * 问题描述：
 * 给定一个二叉树，找出其最大深度。
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 * 
 * 解题思路：
 * 1. 递归方法：使用深度优先搜索（DFS），计算左右子树的最大深度，取较大值加1
 * 2. 迭代方法：使用广度优先搜索（BFS），逐层处理节点，记录层数
 * 
 * 时间复杂度：O(N)，其中N是树中的节点数，每个节点只被访问一次
 * 空间复杂度：
 *   - 递归：最坏情况下O(N)（树为链状），平均O(log N)（平衡树）
 *   - 迭代：O(N)（队列最多存储树的最宽层的所有节点）
 */

// 二叉树节点定义
class TreeNode {
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

public class Code17_MaximumDepthOfBinaryTree {
    /**
     * 递归方法计算二叉树的最大深度
     * @param root 二叉树的根节点
     * @return 二叉树的最大深度
     */
    public int maxDepthRecursive(TreeNode root) {
        // 基本情况：空节点深度为0
        if (root == null) {
            return 0;
        }
        
        // 递归计算左子树的最大深度
        int leftDepth = maxDepthRecursive(root.left);
        // 递归计算右子树的最大深度
        int rightDepth = maxDepthRecursive(root.right);
        
        // 当前树的最大深度 = max(左子树最大深度, 右子树最大深度) + 1
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 迭代方法计算二叉树的最大深度（使用BFS）
     * @param root 二叉树的根节点
     * @return 二叉树的最大深度
     */
    public int maxDepthIterative(TreeNode root) {
        // 基本情况：空树深度为0
        if (root == null) {
            return 0;
        }
        
        // 使用队列进行广度优先搜索
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        int depth = 0;
        
        // 逐层处理节点
        while (!queue.isEmpty()) {
            // 当前层的节点数量
            int levelSize = queue.size();
            
            // 处理当前层的所有节点
            for (int i = 0; i < levelSize; i++) {
                TreeNode current = queue.poll();
                
                // 将下一层的节点加入队列
                if (current.left != null) {
                    queue.offer(current.left);
                }
                if (current.right != null) {
                    queue.offer(current.right);
                }
            }
            
            // 处理完一层，深度加1
            depth++;
        }
        
        return depth;
    }
    
    /**
     * 测试主函数
     */
    public static void main(String[] args) {
        Code17_MaximumDepthOfBinaryTree solution = new Code17_MaximumDepthOfBinaryTree();
        
        // 构建测试用例1：[3,9,20,null,null,15,7]
        //      3
        //     / \
        //    9  20
        //      /  \
        //     15   7
        TreeNode root1 = new TreeNode(3);
        root1.left = new TreeNode(9);
        root1.right = new TreeNode(20);
        root1.right.left = new TreeNode(15);
        root1.right.right = new TreeNode(7);
        
        // 测试递归方法
        System.out.println("递归方法 - 测试用例1的最大深度: " + solution.maxDepthRecursive(root1));
        // 测试迭代方法
        System.out.println("迭代方法 - 测试用例1的最大深度: " + solution.maxDepthIterative(root1));
        
        // 构建测试用例2：[1,null,2]
        //    1
        //     \
        //      2
        TreeNode root2 = new TreeNode(1);
        root2.right = new TreeNode(2);
        
        System.out.println("递归方法 - 测试用例2的最大深度: " + solution.maxDepthRecursive(root2));
        System.out.println("迭代方法 - 测试用例2的最大深度: " + solution.maxDepthIterative(root2));
        
        // 测试空树
        TreeNode root3 = null;
        System.out.println("递归方法 - 空树的最大深度: " + solution.maxDepthRecursive(root3));
        System.out.println("迭代方法 - 空树的最大深度: " + solution.maxDepthIterative(root3));
        
        // 测试单节点树
        TreeNode root4 = new TreeNode(1);
        System.out.println("递归方法 - 单节点树的最大深度: " + solution.maxDepthRecursive(root4));
        System.out.println("迭代方法 - 单节点树的最大深度: " + solution.maxDepthIterative(root4));
    }
    
    /**
     * 性能分析：
     * 
     * 1. 递归实现：
     *    - 时间复杂度：O(N)，每个节点都会被访问一次
     *    - 空间复杂度：
     *      - 最好情况：O(log N)，对于完全平衡的二叉树
     *      - 最坏情况：O(N)，对于链状树（每个节点只有一个子节点）
     *      - 平均情况：O(log N)
     *    - 优点：代码简洁，易于理解
     *    - 缺点：对于非常深的树可能导致栈溢出
     * 
     * 2. 迭代实现：
     *    - 时间复杂度：O(N)，每个节点都会被访问一次
     *    - 空间复杂度：O(N)，队列在最坏情况下存储树的最宽层的所有节点
     *    - 优点：避免了递归调用栈溢出的风险
     *    - 缺点：代码相对复杂
     * 
     * 工程化考量：
     * 1. 对于中小型树，递归实现更加优雅和直观
     * 2. 对于大型树或深度不确定的树，应该优先考虑迭代实现以避免栈溢出
     * 3. 在实际应用中，可以根据输入数据的特性选择合适的实现方式
     * 4. 可以添加日志记录功能，以便在复杂场景中调试
     * 5. 可以考虑使用非递归的DFS（使用显式栈），在某些情况下可能比BFS更节省空间
     * 
     * 递归优化：
     * 1. 虽然Java不自动进行尾递归优化，但这个问题不适合尾递归优化
     * 2. 在某些语言中（如Scala），可以通过尾递归优化避免栈溢出
     */
}