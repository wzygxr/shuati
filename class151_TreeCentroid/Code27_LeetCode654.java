// LeetCode 654. 最大二叉树
// 题目描述：给定一个不含重复元素的整数数组nums。一个以此数组构建的最大二叉树定义如下：
// 1. 二叉树的根是数组中的最大元素
// 2. 左子树是通过数组中最大值左边部分构造出的最大二叉树
// 3. 右子树是通过数组中最大值右边部分构造出的最大二叉树
// 算法思想：递归地在数组中找到最大值作为根节点，然后分别构建左右子树
// 测试链接：https://leetcode.com/problems/maximum-binary-tree/
// 时间复杂度：O(n²)，最坏情况下数组有序
// 空间复杂度：O(n)

public class Code27_LeetCode654 {
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
    
    public TreeNode constructMaximumBinaryTree(int[] nums) {
        """
        根据数组构造最大二叉树
        :param nums: 整数数组，不含重复元素
        :return: 构造好的最大二叉树的根节点
        """
        if (nums == null || nums.length == 0) {
            return null;
        }
        return buildTree(nums, 0, nums.length - 1);
    }
    
    private TreeNode buildTree(int[] nums, int left, int right) {
        """
        递归地构建最大二叉树
        :param nums: 整数数组
        :param left: 当前区间的左边界
        :param right: 当前区间的右边界
        :return: 构建好的子树的根节点
        """
        if (left > right) {
            return null;
        }
        
        // 找到当前区间内的最大值及其索引（作为树的重心）
        int maxIndex = left;
        for (int i = left + 1; i <= right; i++) {
            if (nums[i] > nums[maxIndex]) {
                maxIndex = i;
            }
        }
        
        // 创建根节点（最大值节点）
        TreeNode root = new TreeNode(nums[maxIndex]);
        
        // 递归构建左右子树
        root.left = buildTree(nums, left, maxIndex - 1);
        root.right = buildTree(nums, maxIndex + 1, right);
        
        return root;
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
        int[] nums1 = {3, 2, 1, 6, 0, 5};
        TreeNode root1 = constructMaximumBinaryTree(nums1);
        System.out.print("测试用例1结果: ");
        printTree(root1);
        System.out.println();
        // 期望输出: 6 3 null 2 null 1 null null 5 0 null null
        
        // 测试用例2
        int[] nums2 = {3, 2, 1};
        TreeNode root2 = constructMaximumBinaryTree(nums2);
        System.out.print("测试用例2结果: ");
        printTree(root2);
        System.out.println();
        // 期望输出: 3 null 2 null 1 null null
        
        // 测试用例3 - 单元素数组
        int[] nums3 = {5};
        TreeNode root3 = constructMaximumBinaryTree(nums3);
        System.out.print("测试用例3结果: ");
        printTree(root3);
        System.out.println();
        // 期望输出: 5 null null
        
        // 测试用例4 - 递减数组
        int[] nums4 = {5, 4, 3, 2, 1};
        TreeNode root4 = constructMaximumBinaryTree(nums4);
        System.out.print("测试用例4结果: ");
        printTree(root4);
        System.out.println();
        // 期望输出: 5 null 4 null 3 null 2 null 1 null null
        
        // 测试用例5 - 递增数组
        int[] nums5 = {1, 2, 3, 4, 5};
        TreeNode root5 = constructMaximumBinaryTree(nums5);
        System.out.print("测试用例5结果: ");
        printTree(root5);
        System.out.println();
        // 期望输出: 5 4 3 2 1 null null null null null null
    }
    
    public static void main(String[] args) {
        Code27_LeetCode654 solution = new Code27_LeetCode654();
        solution.test();
    }
}

/*
注意：
1. 最大二叉树的构建过程与树重心的选择有相似之处：都需要找到一个节点作为根，使得其左子树和右子树满足某种特性
2. 树重心是使最大子树大小最小的节点，而最大二叉树是选择当前区间的最大值作为根节点
3. 两种算法都采用了分治法的思想，将问题分解为子问题并递归求解
4. 时间复杂度分析：在最坏情况下（如递增或递减数组），每次都要遍历整个区间，因此时间复杂度为O(n²)
5. 空间复杂度分析：递归调用栈的深度为O(n)，因此空间复杂度为O(n)
6. 算法优化：可以使用单调栈将时间复杂度优化到O(n)，但会增加实现的复杂度
7. 异常情况处理：代码处理了空数组和单元素数组的情况
8. 该问题的核心思想是选择当前区间的最大值作为根节点，这与树重心思想中的"平衡"概念有关
9. 在树的构建过程中，我们每次都选择一个节点（最大值）作为根，然后递归构建左右子树，这与树重心分解树的过程类似
*/