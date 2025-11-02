package class078;

// 二叉树的直径
// 给你一棵二叉树的根节点，返回该树的直径
// 二叉树的 直径 是指树中任意两个节点之间最长路径的长度
// 这条路径可能经过也可能不经过根节点 root
// 两节点之间路径的 长度 由它们之间边数表示
// 测试链接 : https://leetcode.cn/problems/diameter-of-binary-tree/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树的最大深度（高度）
//    - 以该节点为根的子树的直径
// 3. 递归处理左右子树，综合计算当前节点的信息
// 4. 对于每个节点，经过该节点的最长路径 = 左子树的最大深度 + 右子树的最大深度
//    整个树的直径 = max(左子树直径, 右子树直径, 经过当前节点的最长路径)
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算二叉树直径的标准方法
public class Code03_DiameterOfBinaryTree {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static int diameterOfBinaryTree(TreeNode root) {
		return f(root).diameter;
	}

	public static class Info {
		// 以当前节点为根的子树的直径
		public int diameter;
		// 以当前节点为根的子树的最大深度（高度）
		public int height;

		public Info(int a, int b) {
			diameter = a;
			height = b;
		}

	}

	public static Info f(TreeNode x) {
		// 基本情况：空节点
		if (x == null) {
			// 空树的直径为0，高度为0
			return new Info(0, 0);
		}
		
		// 递归处理左右子树
		Info leftInfo = f(x.left);
		Info rightInfo = f(x.right);
		
		// 计算当前子树的信息
		// 当前子树的高度 = max(左子树高度, 右子树高度) + 1
		int height = Math.max(leftInfo.height, rightInfo.height) + 1;
		
		// 当前子树的直径 = max(左子树直径, 右子树直径, 经过当前节点的最长路径)
		// 经过当前节点的最长路径 = 左子树高度 + 右子树高度
		int diameter = Math.max(leftInfo.diameter, rightInfo.diameter);
		diameter = Math.max(diameter, leftInfo.height + rightInfo.height);
		
		// 返回当前节点的信息
		return new Info(diameter, height);
	}

}