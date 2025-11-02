package class079;

// 124. 二叉树中的最大路径和
// 测试链接 : https://leetcode.cn/problems/binary-tree-maximum-path-sum/
public class Code08_MaximumPathSum {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	// 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
	// 是否为最优解: 是，这是计算二叉树最大路径和的标准方法
	public static int maxPathSum(TreeNode root) {
		maxSum = Integer.MIN_VALUE;
		maxGain(root);
		return maxSum;
	}

	// 全局变量，记录最大路径和
	private static int maxSum;

	// 计算以node为根的子树能向父节点提供的最大路径和
	private static int maxGain(TreeNode node) {
		// 基础情况：空节点贡献0
		if (node == null) {
			return 0;
		}

		// 递归计算左右子树能提供的最大路径和
		// 只有当贡献值大于0时才选择
		int leftGain = Math.max(maxGain(node.left), 0);
		int rightGain = Math.max(maxGain(node.right), 0);

		// 计算以当前节点为最高节点的路径的最大路径和
		int currentMax = node.val + leftGain + rightGain;

		// 更新全局最大值
		maxSum = Math.max(maxSum, currentMax);

		// 返回当前节点能向父节点提供的最大路径和
		return node.val + Math.max(leftGain, rightGain);
	}

	// 补充题目1: 437. 路径总和 III
	// 题目链接: https://leetcode.cn/problems/path-sum-iii/
	// 题目描述: 给定一个二叉树的根节点 root 和一个整数 targetSum ，求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
	// 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只能从父节点到子节点）。
	// 时间复杂度: O(n^2) 最坏情况下，对于每个节点都需要遍历其路径
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
	public static int pathSumIII(TreeNode root, int targetSum) {
		// 使用前缀和 + 哈希表的优化方法
		java.util.HashMap<Long, Integer> prefixSum = new java.util.HashMap<>();
		prefixSum.put(0L, 1); // 前缀和为0的路径有1条（空路径）
		return dfsPathSum(root, 0L, targetSum, prefixSum);
	}

	private static int dfsPathSum(TreeNode node, long currentSum, int target, java.util.HashMap<Long, Integer> prefixSum) {
		if (node == null) {
			return 0;
		}

		// 更新当前路径和
		currentSum += node.val;
		// 计算有多少条路径以当前节点结束，路径和为target
		int count = prefixSum.getOrDefault(currentSum - target, 0);
		// 将当前路径和加入前缀和哈希表
		prefixSum.put(currentSum, prefixSum.getOrDefault(currentSum, 0) + 1);

		// 递归处理左右子树
		count += dfsPathSum(node.left, currentSum, target, prefixSum);
		count += dfsPathSum(node.right, currentSum, target, prefixSum);

		// 回溯，移除当前路径和
		prefixSum.put(currentSum, prefixSum.get(currentSum) - 1);
		if (prefixSum.get(currentSum) == 0) {
			prefixSum.remove(currentSum);
		}

		return count;
	}

	// 补充题目2: 112. 路径总和
	// 题目链接: https://leetcode.cn/problems/path-sum/
	// 题目描述: 给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
	// 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
	// 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
	public static boolean hasPathSum(TreeNode root, int targetSum) {
		if (root == null) {
			return false;
		}

		// 如果是叶子节点，直接判断当前节点值是否等于目标和
		if (root.left == null && root.right == null) {
			return root.val == targetSum;
		}

		// 递归检查左右子树
		return hasPathSum(root.left, targetSum - root.val) || hasPathSum(root.right, targetSum - root.val);
	}

	// 补充题目3: 113. 路径总和 II
	// 题目链接: https://leetcode.cn/problems/path-sum-ii/
	// 题目描述: 给你二叉树的根节点 root 和一个整数目标和 targetSum ，
	// 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
	// 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
	public static java.util.List<java.util.List<Integer>> pathSumII(TreeNode root, int targetSum) {
		java.util.List<java.util.List<Integer>> result = new java.util.ArrayList<>();
		java.util.List<Integer> currentPath = new java.util.ArrayList<>();
		dfsPathSumII(root, targetSum, currentPath, result);
		return result;
	}

	private static void dfsPathSumII(TreeNode node, int remainingSum, java.util.List<Integer> currentPath, java.util.List<java.util.List<Integer>> result) {
		if (node == null) {
			return;
		}

		// 将当前节点加入路径
		currentPath.add(node.val);

		// 如果是叶子节点且路径和等于目标值，将路径加入结果
		if (node.left == null && node.right == null && remainingSum == node.val) {
			result.add(new java.util.ArrayList<>(currentPath));
		}

		// 递归处理左右子树
		dfsPathSumII(node.left, remainingSum - node.val, currentPath, result);
		dfsPathSumII(node.right, remainingSum - node.val, currentPath, result);

		// 回溯，移除当前节点
		currentPath.remove(currentPath.size() - 1);
	}

	// 补充题目4: 257. 二叉树的所有路径
	// 题目链接: https://leetcode.cn/problems/binary-tree-paths/
	// 题目描述: 给你一个二叉树的根节点 root ，按 任意顺序 ，返回所有从根节点到叶子节点的路径。
	// 时间复杂度: O(n^2) 最坏情况下，需要存储O(n)条路径，每条路径有O(n)个节点
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度，加上存储路径的O(n)空间
	public static java.util.List<String> binaryTreePaths(TreeNode root) {
		java.util.List<String> result = new java.util.ArrayList<>();
		if (root != null) {
			buildPaths(root, "", result);
		}
		return result;
	}

	private static void buildPaths(TreeNode node, String currentPath, java.util.List<String> result) {
		// 将当前节点加入路径
		if (currentPath.isEmpty()) {
			currentPath = String.valueOf(node.val);
		} else {
			currentPath += "->" + node.val;
		}

		// 如果是叶子节点，将路径加入结果
		if (node.left == null && node.right == null) {
			result.add(currentPath);
			return;
		}

		// 递归处理左右子树
		if (node.left != null) {
			buildPaths(node.left, currentPath, result);
		}
		if (node.right != null) {
			buildPaths(node.right, currentPath, result);
		}
	}
}