package class122;

/**
 * LeetCode 235. 二叉搜索树的最近公共祖先（LCA）
 * 
 * 题目来源：LeetCode
 * 题目链接：https://leetcode.com/problems/lowest-common-ancestor-of-a-binary-search-tree/
 * 
 * 题目描述：
 * 给定一个二叉搜索树，找到两个指定节点的最近公共祖先。
 * 最近公共祖先的定义：对于有根树T的两个节点p、q，最近公共祖先表示为一个节点x，
 * 满足x是p、q的祖先且x的深度尽可能大。
 * 
 * 算法原理：利用二叉搜索树的性质
 * 二叉搜索树具有重要性质：左子树所有节点值 < 根节点值 < 右子树所有节点值。
 * 利用这个性质可以高效地找到LCA。
 * 
 * 解题思路：
 * 1. 从根节点开始遍历
 * 2. 如果p和q的值都小于当前节点值，则LCA在左子树
 * 3. 如果p和q的值都大于当前节点值，则LCA在右子树
 * 4. 否则当前节点就是LCA（包括以下情况：p和q分别在左右子树，或其中一个节点就是当前节点）
 * 
 * 时间复杂度分析：
 * - 迭代版本：O(h)，其中h为树的高度
 * - 递归版本：O(h)，其中h为树的高度
 * 
 * 空间复杂度分析：
 * - 迭代版本：O(1)
 * - 递归版本：O(h)，递归调用栈空间
 * 
 * 工程化考量：
 * 1. 提供迭代和递归两种实现，满足不同场景需求
 * 2. 迭代版本空间效率更高，避免栈溢出风险
 * 3. 利用BST性质，相比普通二叉树LCA算法更高效
 * 
 * 最优解分析：
 * 本解法充分利用了二叉搜索树的性质，是解决此类问题的最优解。
 * 相比于普通二叉树的LCA算法O(N)时间复杂度，本解法将时间复杂度优化到O(h)。
 */

// 二叉树节点定义
class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x) { val = x; }
}

public class Code14_LeetCode235 {
    
    /**
     * 二叉搜索树的最近公共祖先（迭代版本）
     * 
     * @param root 根节点
     * @param p 节点p
     * @param q 节点q
     * @return 最近公共祖先
     * 
     * 算法原理：
     * 利用二叉搜索树的性质进行迭代查找
     * 1. 如果p和q都小于当前节点，则LCA在左子树
     * 2. 如果p和q都大于当前节点，则LCA在右子树
     * 3. 否则当前节点就是LCA
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        TreeNode current = root;
        
        // 迭代查找LCA
        while (current != null) {
            // 如果p和q的值都小于当前节点值，说明LCA在左子树
            if (p.val < current.val && q.val < current.val) {
                current = current.left;
            } 
            // 如果p和q的值都大于当前节点值，说明LCA在右子树
            else if (p.val > current.val && q.val > current.val) {
                current = current.right;
            } 
            // 否则当前节点就是LCA，包括以下情况：
            // 1. p和q分别在当前节点的左右子树
            // 2. p或q其中一个就是当前节点
            // 3. p和q都是当前节点（理论上不会出现）
            else {
                return current;
            }
        }
        
        return null; // 理论上不会执行到这里
    }
    
    /**
     * 二叉搜索树的最近公共祖先（递归版本）
     * 
     * @param root 根节点
     * @param p 节点p
     * @param q 节点q
     * @return 最近公共祖先
     * 
     * 算法原理：
     * 利用二叉搜索树的性质进行递归查找
     * 1. 如果p和q都小于当前节点，则LCA在左子树
     * 2. 如果p和q都大于当前节点，则LCA在右子树
     * 3. 否则当前节点就是LCA
     */
    public TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
        // 基本情况：如果节点为空，返回null
        
        // 如果p和q的值都小于当前节点值，说明LCA在左子树
        if (p.val < root.val && q.val < root.val) {
            return lowestCommonAncestorRecursive(root.left, p, q);
        }
        
        // 如果p和q的值都大于当前节点值，说明LCA在右子树
        if (p.val > root.val && q.val > root.val) {
            return lowestCommonAncestorRecursive(root.right, p, q);
        }
        
        // 否则当前节点就是LCA
        return root;
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 构建测试二叉搜索树
        //       6
        //      / \
        //     2   8
        //    / \ / \
        //   0  4 7  9
        //     / \
        //    3   5
        
        TreeNode root = new TreeNode(6);
        root.left = new TreeNode(2);
        root.right = new TreeNode(8);
        root.left.left = new TreeNode(0);
        root.left.right = new TreeNode(4);
        root.right.left = new TreeNode(7);
        root.right.right = new TreeNode(9);
        root.left.right.left = new TreeNode(3);
        root.left.right.right = new TreeNode(5);
        
        Code14_LeetCode235 solution = new Code14_LeetCode235();
        
        // 测试用例1：节点2和8的LCA
        TreeNode p1 = root.left;  // 节点2
        TreeNode q1 = root.right;  // 节点8
        TreeNode lca1 = solution.lowestCommonAncestor(root, p1, q1);
        System.out.println("节点2和8的LCA: " + lca1.val); // 期望: 6
        
        // 测试用例2：节点2和4的LCA
        TreeNode p2 = root.left;      // 节点2
        TreeNode q2 = root.left.right; // 节点4
        TreeNode lca2 = solution.lowestCommonAncestor(root, p2, q2);
        System.out.println("节点2和4的LCA: " + lca2.val); // 期望: 2
        
        // 测试用例3：节点3和5的LCA
        TreeNode p3 = root.left.right.left;  // 节点3
        TreeNode q3 = root.left.right.right;  // 节点5
        TreeNode lca3 = solution.lowestCommonAncestor(root, p3, q3);
        System.out.println("节点3和5的LCA: " + lca3.val); // 期望: 4
        
        // 测试用例4：节点0和5的LCA
        TreeNode p4 = root.left.left;        // 节点0
        TreeNode q4 = root.left.right.right; // 节点5
        TreeNode lca4 = solution.lowestCommonAncestor(root, p4, q4);
        System.out.println("节点0和5的LCA: " + lca4.val); // 期望: 2
    }
}