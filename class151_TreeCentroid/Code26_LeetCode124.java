// LeetCode 124. 二叉树中的最大路径和
// 题目描述：路径被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。路径和是路径中各节点值的总和。
// 算法思想：利用深度优先搜索计算每个节点的最大贡献值，同时更新全局最大路径和
// 测试链接：https://leetcode.com/problems/binary-tree-maximum-path-sum/
// 时间复杂度：O(n)
// 空间复杂度：O(h)，h为树高，最坏情况下为O(n)

public class Code26_LeetCode124 {
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
    
    private int maxSum;
    
    public int maxPathSum(TreeNode root) {
        """
        计算二叉树中的最大路径和
        :param root: 二叉树的根节点
        :return: 最大路径和
        """
        // 初始化最大路径和为根节点的值，考虑到可能有负数的情况
        maxSum = Integer.MIN_VALUE;
        maxPathSumHelper(root);
        return maxSum;
    }
    
    private int maxPathSumHelper(TreeNode node) {
        """
        计算从当前节点开始的最大路径和，并更新全局最大路径和
        :param node: 当前节点
        :return: 以当前节点为起点的最大路径和
        """
        if (node == null) {
            return 0;
        }
        
        // 递归计算左右子树的最大贡献值（如果贡献值为负，则取0，即不选择该子树）
        int leftGain = Math.max(maxPathSumHelper(node.left), 0);
        int rightGain = Math.max(maxPathSumHelper(node.right), 0);
        
        // 更新全局最大路径和：当前节点值 + 左子树最大贡献值 + 右子树最大贡献值
        maxSum = Math.max(maxSum, node.val + leftGain + rightGain);
        
        // 返回当前节点的最大贡献值：节点值 + 左右子树中较大的贡献值
        return node.val + Math.max(leftGain, rightGain);
    }
    
    public TreeNode buildTree(Integer[] nums, int index) {
        """
        根据数组构建二叉树
        :param nums: 数组，null表示空节点
        :param index: 当前索引
        :return: 构建好的树节点
        """
        if (index >= nums.length || nums[index] == null) {
            return null;
        }
        
        TreeNode node = new TreeNode(nums[index]);
        node.left = buildTree(nums, 2 * index + 1);
        node.right = buildTree(nums, 2 * index + 2);
        
        return node;
    }
    
    public void printTree(TreeNode root) {
        """
        打印树的结构（用于调试）
        :param root: 二叉树的根节点
        """
        if (root == null) {
            System.out.print("null ");
            return;
        }
        
        System.out.print(root.val + " ");
        printTree(root.left);
        printTree(root.right);
    }
    
    public void test() {
        """
        测试方法
        """
        // 测试用例1
        Integer[] nums1 = {1, 2, 3};
        TreeNode root1 = buildTree(nums1, 0);
        int result1 = maxPathSum(root1);
        System.out.println("测试用例1结果: " + result1); // 期望输出: 6
        
        // 测试用例2
        Integer[] nums2 = {-10, 9, 20, null, null, 15, 7};
        TreeNode root2 = buildTree(nums2, 0);
        int result2 = maxPathSum(root2);
        System.out.println("测试用例2结果: " + result2); // 期望输出: 42
        
        // 测试用例3 - 单节点树
        Integer[] nums3 = {1};
        TreeNode root3 = buildTree(nums3, 0);
        int result3 = maxPathSum(root3);
        System.out.println("测试用例3结果: " + result3); // 期望输出: 1
        
        // 测试用例4 - 全负数节点
        Integer[] nums4 = {-3};
        TreeNode root4 = buildTree(nums4, 0);
        int result4 = maxPathSum(root4);
        System.out.println("测试用例4结果: " + result4); // 期望输出: -3
        
        // 测试用例5 - 混合正负数节点
        Integer[] nums5 = {2, -1, -2};
        TreeNode root5 = buildTree(nums5, 0);
        int result5 = maxPathSum(root5);
        System.out.println("测试用例5结果: " + result5); // 期望输出: 2
    }
    
    public static void main(String[] args) {
        Code26_LeetCode124 solution = new Code26_LeetCode124();
        solution.test();
    }
}

/*
注意：
1. 树的最大路径和问题与树重心的思想有相似之处，都是通过深度优先搜索来计算子树的属性
2. 树重心寻找的是使最大子树大小最小的节点，而最大路径和寻找的是路径和最大的路径
3. 两种算法都需要在递归过程中维护全局最优解
4. 最大路径和问题中，我们需要考虑每个节点作为路径转折点的情况，即当前节点的值加上左右子树的最大贡献值
5. 时间复杂度分析：每个节点只被访问一次，因此时间复杂度为O(n)
6. 空间复杂度分析：递归调用栈的深度为树的高度，最坏情况下为O(n)
7. 异常情况处理：代码处理了空树和只有负数节点的情况
8. 算法优化：当子树的贡献值为负时，我们选择不包含该子树，以获得更大的路径和
9. 边界情况处理：初始最大路径和设置为Integer.MIN_VALUE，避免了全负数情况的错误
*/