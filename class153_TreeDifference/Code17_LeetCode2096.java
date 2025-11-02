package class122;

import java.util.*;

/**
 * LeetCode 2096. 从二叉树一个节点到另一个节点的方向
 * 
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.cn/problems/step-by-step-directions-from-a-binary-tree-node-to-another/
 * 
 * 题目描述：
 * 给定一棵二叉树，找到从起点节点到目标节点的路径方向。
 * 路径方向用字符串表示，其中'L'表示向左移动，'R'表示向右移动，'U'表示向上移动到父节点。
 * 
 * 算法原理：LCA + 路径重建
 * 这是一个结合了LCA和路径重建的二叉树问题。
 * 
 * 解题思路：
 * 1. 首先找到起点节点和目标节点的最近公共祖先(LCA)
 * 2. 然后分别找到从LCA到起点和从LCA到目标节点的路径
 * 3. 起点到LCA的路径全部转换为'U'（向上移动）
 * 4. LCA到目标节点的路径保持不变
 * 5. 将两段路径拼接即为最终结果
 * 
 * 时间复杂度分析：
 * - 找LCA：O(N)
 * - 找路径：O(N)
 * 总时间复杂度：O(N)
 * 
 * 空间复杂度分析：
 * - 递归栈空间：O(H)，其中H是树的高度
 * - 路径字符串：O(N)
 * 总空间复杂度：O(N)
 * 
 * 工程化考量：
 * 1. 使用StringBuilder提高字符串拼接效率
 * 2. 递归实现简洁明了，但需注意栈溢出问题
 * 3. 路径查找采用回溯法，确保找到正确的路径
 * 
 * 最优解分析：
 * 本解法是解决此类问题的最优解，时间复杂度为O(N)。
 * 相比于分别找到两个节点的路径再合并的方法，本解法通过LCA避免了重复遍历。
 */
public class Code17_LeetCode2096 {
    
    /**
     * 二叉树节点定义
     */
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }
    
    static class Solution {
        /**
         * 获取从起点节点到目标节点的方向路径
         * 
         * @param root 二叉树根节点
         * @param startValue 起点节点值
         * @param destValue 目标节点值
         * @return 从起点到目标的方向路径字符串
         */
        public String getDirections(TreeNode root, int startValue, int destValue) {
            // 找到起点和目标节点的LCA
            TreeNode lca = findLCA(root, startValue, destValue);
            
            // 从LCA到起点的路径（全部是向上移动'U'）
            StringBuilder startToLCA = new StringBuilder();
            findPath(lca, startValue, startToLCA);
            
            // 从LCA到目标的路径
            StringBuilder lcaToDest = new StringBuilder();
            findPath(lca, destValue, lcaToDest);
            
            // 起点到LCA的路径全部替换为'U'
            String upPath = "U".repeat(startToLCA.length());
            
            // 拼接两段路径并返回
            return upPath + lcaToDest.toString();
        }
        
        /**
         * 找到两个节点的最近公共祖先(LCA)
         * 
         * @param root 当前节点
         * @param p 第一个节点值
         * @param q 第二个节点值
         * @return 两个节点的LCA
         */
        private TreeNode findLCA(TreeNode root, int p, int q) {
            // 基本情况：节点为空或找到目标节点
            if (root == null || root.val == p || root.val == q) {
                return root;
            }
            
            // 在左子树中查找
            TreeNode left = findLCA(root.left, p, q);
            // 在右子树中查找
            TreeNode right = findLCA(root.right, p, q);
            
            // 如果左右子树都找到了目标节点，则当前节点就是LCA
            if (left != null && right != null) {
                return root;
            }
            
            // 返回非空的子树结果
            return left != null ? left : right;
        }
        
        /**
         * 查找从node节点到target节点的路径
         * 
         * @param node 当前节点
         * @param target 目标节点值
         * @param path 路径字符串
         * @return 是否找到路径
         */
        private boolean findPath(TreeNode node, int target, StringBuilder path) {
            // 基本情况：节点为空
            if (node == null) return false;
            
            // 找到目标节点
            if (node.val == target) return true;
            
            // 尝试左子树
            path.append('L');
            if (findPath(node.left, target, path)) {
                return true;
            }
            // 回溯：删除最后添加的字符
            path.deleteCharAt(path.length() - 1);
            
            // 尝试右子树
            path.append('R');
            if (findPath(node.right, target, path)) {
                return true;
            }
            // 回溯：删除最后添加的字符
            path.deleteCharAt(path.length() - 1);
            
            return false;
        }
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        TreeNode root1 = new TreeNode(5);
        root1.left = new TreeNode(1);
        root1.right = new TreeNode(2);
        root1.left.left = new TreeNode(3);
        root1.right.left = new TreeNode(6);
        root1.right.right = new TreeNode(4);
        
        System.out.println(solution.getDirections(root1, 3, 6)); // 输出: "UURL"
        
        // 测试用例2
        TreeNode root2 = new TreeNode(2);
        root2.left = new TreeNode(1);
        
        System.out.println(solution.getDirections(root2, 2, 1)); // 输出: "L"
    }
}