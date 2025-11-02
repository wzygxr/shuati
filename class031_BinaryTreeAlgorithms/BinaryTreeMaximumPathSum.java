package class037;

// LeetCode 124. Binary Tree Maximum Path Sum
// 题目链接: https://leetcode.cn/problems/binary-tree-maximum-path-sum/
// 二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
// 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
// 路径和是路径中各节点值的总和。
// 给你一个二叉树的根节点 root ，返回其最大路径和。
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们计算两个值：
//    - 以该节点为起点向下的最大路径和（可以用于连接父节点）
//    - 经过该节点的最大路径和（可以作为最终答案）
// 3. 递归处理左右子树，综合计算当前节点的信息
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算二叉树最大路径和的标准方法

public class BinaryTreeMaximumPathSum {

	// 不提交这个类
	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode() {
		}

		TreeNode(int val) {
			this.val = val;
		}

		TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	// 用于存储递归过程中的信息
	private static class Info {
		// 以当前节点为起点向下的最大路径和（可以连接父节点）
		public int maxPathSumFromRoot;
		// 以当前节点为根的子树中的最大路径和（可以作为最终答案）
		public int maxPathSumInSubtree;

		public Info(int maxPathSumFromRoot, int maxPathSumInSubtree) {
			this.maxPathSumFromRoot = maxPathSumFromRoot;
			this.maxPathSumInSubtree = maxPathSumInSubtree;
		}
	}

	// 全局变量，记录最大路径和
	private int globalMax = Integer.MIN_VALUE;

	// 提交如下的方法
	public int maxPathSum(TreeNode root) {
		globalMax = Integer.MIN_VALUE;
		Info info = process(root);
		return info.maxPathSumInSubtree;
	}

	// 递归处理以node为根的子树
	private Info process(TreeNode node) {
		if (node == null) {
			return new Info(0, Integer.MIN_VALUE);
		}

		// 递归处理左右子树
		Info leftInfo = process(node.left);
		Info rightInfo = process(node.right);

		// 计算以当前节点为起点向下的最大路径和
		// 可以选择不走子树（路径和为0），或者走左子树或右子树
		int maxPathSumFromRoot = Math.max(0, 
				Math.max(leftInfo.maxPathSumFromRoot, rightInfo.maxPathSumFromRoot)) + node.val;

		// 计算以当前节点为根的子树中的最大路径和
		// 可能的情况：
		// 1. 只包含当前节点
		// 2. 当前节点 + 左子树向下的最大路径
		// 3. 当前节点 + 右子树向下的最大路径
		// 4. 左子树向下的最大路径 + 当前节点 + 右子树向下的最大路径
		int maxPathSumInSubtree = node.val;
		if (leftInfo.maxPathSumFromRoot > 0) {
			maxPathSumInSubtree += leftInfo.maxPathSumFromRoot;
		}
		if (rightInfo.maxPathSumFromRoot > 0) {
			maxPathSumInSubtree += rightInfo.maxPathSumFromRoot;
		}
		
		// 还需要考虑左右子树内部的最大路径和
		if (leftInfo.maxPathSumInSubtree != Integer.MIN_VALUE) {
			maxPathSumInSubtree = Math.max(maxPathSumInSubtree, leftInfo.maxPathSumInSubtree);
		}
		if (rightInfo.maxPathSumInSubtree != Integer.MIN_VALUE) {
			maxPathSumInSubtree = Math.max(maxPathSumInSubtree, rightInfo.maxPathSumInSubtree);
		}

		return new Info(maxPathSumFromRoot, maxPathSumInSubtree);
	}

	// 测试用例
	public static void main(String[] args) {
		BinaryTreeMaximumPathSum solution = new BinaryTreeMaximumPathSum();

		// 测试用例1:
		//       1
		//      / \
		//     2   3
		TreeNode root1 = new TreeNode(1);
		root1.left = new TreeNode(2);
		root1.right = new TreeNode(3);
		int result1 = solution.maxPathSum(root1);
		System.out.println("测试用例1结果: " + result1); // 应该输出6 (2->1->3)

		// 测试用例2:
		//      -10
		//      /  \
		//     9   20
		//        /  \
		//       15   7
		TreeNode root2 = new TreeNode(-10);
		root2.left = new TreeNode(9);
		root2.right = new TreeNode(20);
		root2.right.left = new TreeNode(15);
		root2.right.right = new TreeNode(7);
		int result2 = solution.maxPathSum(root2);
		System.out.println("测试用例2结果: " + result2); // 应该输出42 (15->20->7)

		// 测试用例3:
		//       5
		//      / \
		//    -3   4
		//    / \   \
		//   1   4  -2
		TreeNode root3 = new TreeNode(5);
		root3.left = new TreeNode(-3);
		root3.right = new TreeNode(4);
		root3.left.left = new TreeNode(1);
		root3.left.right = new TreeNode(4);
		root3.right.right = new TreeNode(-2);
		int result3 = solution.maxPathSum(root3);
		System.out.println("测试用例3结果: " + result3); // 应该输出10 (1->(-3)->5->4->(-2))
	}
}