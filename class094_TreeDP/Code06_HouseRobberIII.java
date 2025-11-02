package class079;

// 337. 打家劫舍 III
// 测试链接 : https://leetcode.cn/problems/house-robber-iii/
public class Code06_HouseRobberIII {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	// 时间复杂度: O(n) n为树中节点的数量，需要遍历所有节点
	// 空间复杂度: O(h) h为树的高度，递归调用栈的深度
	// 是否为最优解: 是，这是解决树形DP问题的标准方法
	public static int rob(TreeNode root) {
		// 调用递归函数，返回包含两个值的数组
		// 第一个值表示不抢劫当前节点时的最大收益
		// 第二个值表示抢劫当前节点时的最大收益
		int[] result = robHelper(root);
		// 返回两种情况的最大值
		return Math.max(result[0], result[1]);
	}

	// 递归函数返回一个长度为2的数组
	// result[0] 表示不抢劫当前节点时的最大收益
	// result[1] 表示抢劫当前节点时的最大收益
	private static int[] robHelper(TreeNode node) {
		// 基础情况：如果节点为空，返回[0, 0]
		if (node == null) {
			return new int[]{0, 0};
		}

		// 递归计算左右子树的结果
		int[] left = robHelper(node.left);
		int[] right = robHelper(node.right);

		// 计算当前节点的两种情况
		// 1. 不抢劫当前节点：左右子树可以自由选择是否抢劫
		int notRob = Math.max(left[0], left[1]) + Math.max(right[0], right[1]);
		
		// 2. 抢劫当前节点：左右子节点都不能抢劫
		int doRob = node.val + left[0] + right[0];

		// 返回结果
		return new int[]{notRob, doRob};
	}

	// 补充题目1: 1372. 二叉树中的最长交错路径
	// 题目链接: https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
	// 题目描述: 给定一棵二叉树，找到最长的交错路径的长度。
	// 交错路径定义为：从根节点到任意叶子节点，路径上的节点交替经过左子节点和右子节点。
	public static int longestZigZag(TreeNode root) {
		int[] maxLength = new int[1];
		if (root == null) {
			return 0;
		}
		longestZigZagHelper(root.left, 1, true, maxLength); // 从左子节点开始，方向为左
		longestZigZagHelper(root.right, 1, false, maxLength); // 从右子节点开始，方向为右
		return maxLength[0];
	}

	private static void longestZigZagHelper(TreeNode node, int length, boolean isLeft, int[] maxLength) {
		if (node == null) {
			return;
		}
		maxLength[0] = Math.max(maxLength[0], length);
		
		if (isLeft) {
			// 如果当前是左子节点，下一步应该走右子节点
			longestZigZagHelper(node.right, length + 1, false, maxLength);
			// 也可以重新开始计算
			longestZigZagHelper(node.left, 1, true, maxLength);
		} else {
			// 如果当前是右子节点，下一步应该走左子节点
			longestZigZagHelper(node.left, length + 1, true, maxLength);
			// 也可以重新开始计算
			longestZigZagHelper(node.right, 1, false, maxLength);
		}
	}

	// 补充题目2: 549. 二叉树中最长的连续序列
	// 题目链接: https://leetcode.cn/problems/binary-tree-longest-consecutive-sequence-ii/
	// 题目描述: 给定一棵二叉树，找出最长连续序列路径的长度。这个路径可以是升序也可以是降序。
	public static int longestConsecutive2(TreeNode root) {
		int[] maxLength = new int[1];
		longestConsecutive2Helper(root, maxLength);
		return maxLength[0];
	}

	// 返回一个长度为2的数组，第一个元素是从该节点开始的最长递增序列长度，第二个元素是最长递减序列长度
	private static int[] longestConsecutive2Helper(TreeNode node, int[] maxLength) {
		if (node == null) {
			return new int[]{0, 0};
		}

		int inc = 1; // 递增序列长度，初始为1（包含自己）
		int dec = 1; // 递减序列长度，初始为1（包含自己）

		if (node.left != null) {
			int[] left = longestConsecutive2Helper(node.left, maxLength);
			if (node.val == node.left.val + 1) {
				// 当前节点比左子节点大1，递减序列
				dec = left[1] + 1;
			} else if (node.val == node.left.val - 1) {
				// 当前节点比左子节点小1，递增序列
				inc = left[0] + 1;
			}
		}

		if (node.right != null) {
			int[] right = longestConsecutive2Helper(node.right, maxLength);
			if (node.val == node.right.val + 1) {
				// 当前节点比右子节点大1，递减序列
				dec = Math.max(dec, right[1] + 1);
			} else if (node.val == node.right.val - 1) {
				// 当前节点比右子节点小1，递增序列
				inc = Math.max(inc, right[0] + 1);
			}
		}

		// 更新全局最长长度：可以是从该节点开始的递增或递减序列，或者经过该节点的序列（inc + dec - 1）
		maxLength[0] = Math.max(maxLength[0], inc + dec - 1);
		
		return new int[]{inc, dec};
	}

	// 补充题目3: 1457. 二叉树中的伪回文路径
	// 题目链接: https://leetcode.cn/problems/pseudo-palindromic-paths-in-a-binary-tree/
	// 题目描述: 给一棵二叉树，统计从根到叶子节点的所有路径中，伪回文路径的数量。
	// 伪回文路径定义为：路径上的节点值可以重新排列形成一个回文串。
	public static int pseudoPalindromicPaths(TreeNode root) {
		int[] count = new int[10]; // 存储每个数字出现的次数
		return pseudoPalindromicPathsHelper(root, count);
	}

	private static int pseudoPalindromicPathsHelper(TreeNode node, int[] count) {
		if (node == null) {
			return 0;
		}

		// 增加当前节点值的计数
		count[node.val]++;

		int result = 0;
		if (node.left == null && node.right == null) {
			// 叶子节点，检查是否是伪回文路径
			if (isPseudoPalindrome(count)) {
				result = 1;
			} else {
				result = 0;
			}
		} else {
			// 非叶子节点，继续递归
			result = pseudoPalindromicPathsHelper(node.left, count) + 
					   pseudoPalindromicPathsHelper(node.right, count);
		}

		// 回溯，减少当前节点值的计数
		count[node.val]--;

		return result;
	}

	private static boolean isPseudoPalindrome(int[] count) {
		int oddCount = 0;
		for (int i = 0; i < 10; i++) {
			if (count[i] % 2 != 0) {
				oddCount++;
				// 伪回文最多只能有一个奇数次数
				if (oddCount > 1) {
					return false;
				}
			}
		}
		return true;
	}

	// 补充题目4: 2246. 相邻字符不同的最长路径
	// 题目链接: https://leetcode.cn/problems/longest-path-with-different-adjacent-characters/
	// 题目描述: 给一棵树，每个节点有一个字符，找到最长的路径，使得路径上相邻节点的字符不同。
	public static int longestPath(int[] parent, String s) {
		int n = parent.length;
		// 构建邻接表
		java.util.List<java.util.List<Integer>> adj = new java.util.ArrayList<>();
		for (int i = 0; i < n; i++) {
			adj.add(new java.util.ArrayList<>());
		}
		for (int i = 1; i < n; i++) {
			adj.get(parent[i]).add(i);
			adj.get(i).add(parent[i]); // 无向树
		}

		int[] maxLength = new int[1];
		longestPathHelper(0, -1, adj, s, maxLength);
		return maxLength[0];
	}

	private static int longestPathHelper(int node, int parent, java.util.List<java.util.List<Integer>> adj, String s, int[] maxLength) {
		int firstMax = 0, secondMax = 0;
		
		for (int neighbor : adj.get(node)) {
			if (neighbor == parent) continue;
			
			int currentLength = longestPathHelper(neighbor, node, adj, s, maxLength);
			
			// 如果相邻节点字符不同，才能继续路径
			if (s.charAt(neighbor) != s.charAt(node)) {
				if (currentLength > firstMax) {
					secondMax = firstMax;
					firstMax = currentLength;
				} else if (currentLength > secondMax) {
					secondMax = currentLength;
				}
			}
		}
		
		// 更新全局最长路径：可能是通过当前节点的两条最长路径之和
		maxLength[0] = Math.max(maxLength[0], firstMax + secondMax + 1);
		
		// 返回从当前节点开始的最长路径长度
		return firstMax + 1;
	}
}