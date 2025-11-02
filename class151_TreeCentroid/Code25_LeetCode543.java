package class120;

// LeetCode 543. 二叉树的直径
// 题目来源：LeetCode 543 https://leetcode.com/problems/diameter-of-binary-tree/
// 题目描述：给定一棵二叉树，计算它的直径长度。直径是指树中任意两个节点之间最长路径的长度。
// 算法思想：利用深度优先搜索计算每个节点的高度，同时更新最长路径长度（直径）
// 与树的重心的关系：树的直径与树的重心有密切关系，直径必然经过树的重心
// 解题思路：
// 1. 对于每个节点，计算经过该节点的最长路径长度（左子树深度+右子树深度）
// 2. 在计算深度的过程中，同时更新全局最大值（直径）
// 3. 返回整棵树的直径
// 时间复杂度：O(n)，每个节点访问一次
// 空间复杂度：O(h)，h为树高，最坏情况下为O(n)，用于递归栈

public class Code25_LeetCode543 {
    // 二叉树节点定义
    public static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        
        TreeNode() {}
        
        TreeNode(int val) {
            this.val = val;
        }
        
        TreeNode(int val, TreeNode left, TreeNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }
    
    // 记录二叉树的直径（全局最大值）
    private int diameter;
    
    /**
     * 计算二叉树的直径
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    public int diameterOfBinaryTree(TreeNode root) {
        diameter = 0; // 初始化直径为0
        depth(root); // 计算深度并更新直径
        return diameter;
    }
    
    /**
     * 计算节点的深度，并在过程中更新直径
     * 核心思想：对于每个节点，经过该节点的最长路径长度等于左子树深度+右子树深度
     * @param node 当前节点
     * @return 以node为根的子树的最大深度
     */
    private int depth(TreeNode node) {
        // 基础情况：空节点的深度为0
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的深度
        // leftDepth表示以node.left为根的子树的最大深度
        int leftDepth = depth(node.left);
        // rightDepth表示以node.right为根的子树的最大深度
        int rightDepth = depth(node.right);
        
        // 更新直径：经过当前节点的最长路径为左子树深度+右子树深度
        // 这是因为从左子树的最深叶子节点经过当前节点到右子树的最深叶子节点的路径长度
        // 就是左子树深度+右子树深度
        diameter = Math.max(diameter, leftDepth + rightDepth);
        
        // 返回以当前节点为根的子树的最大深度
        // 等于左右子树的最大深度加1（当前节点）
        return Math.max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 根据数组构建二叉树（层序遍历方式）
     * @param nums 数组，null表示空节点
     * @param index 当前索引
     * @return 构建好的树节点
     */
    public TreeNode buildTree(Integer[] nums, int index) {
        // 边界条件：索引超出数组范围或当前元素为null
        if (index >= nums.length || nums[index] == null) {
            return null;
        }
        
        // 创建当前节点
        TreeNode node = new TreeNode(nums[index]);
        // 递归构建左子树（在数组中的索引为2*index+1）
        node.left = buildTree(nums, 2 * index + 1);
        // 递归构建右子树（在数组中的索引为2*index+2）
        node.right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    /**
     * 打印树的结构（用于调试）
     * @param root 二叉树的根节点
     */
    public void printTree(TreeNode root) {
        // 空节点打印null
        if (root == null) {
            System.out.print("null ");
            return;
        }
        
        // 打印当前节点值
        System.out.print(root.val + " ");
        // 递归打印左子树
        printTree(root.left);
        // 递归打印右子树
        printTree(root.right);
    }
    
    /**
     * 测试方法
     */
    public void test() {
        // 测试用例1: [1,2,3,4,5]
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        // 直径为3（路径4->2->1->3）
        Integer[] nums1 = {1, 2, 3, 4, 5};
        TreeNode root1 = buildTree(nums1, 0);
        int result1 = diameterOfBinaryTree(root1);
        System.out.println("测试用例1结果: " + result1); // 期望输出: 3
        
        // 测试用例2: [1,2]
        //   1
        //  /
        // 2
        // 直径为1（路径1->2）
        Integer[] nums2 = {1, 2};
        TreeNode root2 = buildTree(nums2, 0);
        int result2 = diameterOfBinaryTree(root2);
        System.out.println("测试用例2结果: " + result2); // 期望输出: 1
        
        // 测试用例3: 空树
        // 直径为0
        TreeNode root3 = null;
        int result3 = diameterOfBinaryTree(root3);
        System.out.println("测试用例3结果: " + result3); // 期望输出: 0
        
        // 测试用例4: 单节点树 [1]
        // 1
        // 直径为0
        Integer[] nums4 = {1};
        TreeNode root4 = buildTree(nums4, 0);
        int result4 = diameterOfBinaryTree(root4);
        System.out.println("测试用例4结果: " + result4); // 期望输出: 0
        
        // 测试用例5: 不平衡树 [1,null,3,null,null,null,5]
        // 1
        //  \
        //   3
        //    \
        //     5
        // 直径为2（路径1->3->5）
        Integer[] nums5 = {1, null, 3, null, null, null, 5};
        TreeNode root5 = buildTree(nums5, 0);
        int result5 = diameterOfBinaryTree(root5);
        System.out.println("测试用例5结果: " + result5); // 期望输出: 2
    }
    
    public static void main(String[] args) {
        Code25_LeetCode543 solution = new Code25_LeetCode543();
        solution.test();
    }
}

/*
注意：
1. 树的直径与树的重心有密切关系：树的直径必然经过树的重心
2. 对于树的直径问题，可以采用与树重心相似的深度优先搜索方法来解决
3. 两种算法都利用了树形结构的特性，通过计算子树的属性来获得全局最优解
4. 树的直径计算中，我们需要记录每个节点的左右子树深度之和的最大值，这与树重心寻找最大子树的过程类似
5. 时间复杂度分析：每个节点只被访问一次，因此时间复杂度为O(n)
6. 空间复杂度分析：递归调用栈的深度为树的高度，最坏情况下为O(n)
7. 异常情况处理：代码处理了空树和单节点树的情况
8. 算法优化：可以通过一次深度优先搜索同时计算子树深度和更新直径，避免了重复计算
*/