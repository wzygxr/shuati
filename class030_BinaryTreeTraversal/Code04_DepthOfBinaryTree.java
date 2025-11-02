package class036;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * 二叉树深度相关算法实现
 * 包括：
 * 1. 最大深度计算（递归和非递归实现）
 * 2. 最小深度计算（递归和非递归实现）
 * 3. 平衡二叉树判断
 * 4. 树的高度计算
 * 5. 二叉树直径计算
 * 
 * 核心算法思想：
 * - 最大深度：根节点到最远叶子节点的最长路径上的节点数
 * - 最小深度：根节点到最近叶子节点的最短路径上的节点数
 * - 平衡二叉树：任意节点的左右子树高度差不超过1
 * - 二叉树直径：树中任意两个节点之间最长路径的长度
 * 
 * 时间复杂度分析：
 * - 递归DFS解法：O(N)，其中N是树中的节点数，每个节点只访问一次
 * - 迭代BFS解法：O(N)
 * - 迭代DFS解法：O(N)
 * 
 * 空间复杂度分析：
 * - 递归DFS：O(H)，H为树的高度，最坏情况下（斜树）为O(N)
 * - 迭代BFS：O(W)，W为树的最大宽度，最坏情况下为O(N)
 * - 迭代DFS：O(H)
 * 
 * 工程化考量：
 * - 针对不同应用场景选择合适的实现方法
 * - 对于完全二叉树，BFS可能更高效
 * - 对于不平衡的树，DFS可能更节省空间
 * - 递归实现简洁但可能有栈溢出风险
 * - 非递归实现更健壮，适合深层树
 * 
 * 算法优化要点：
 * - 最小深度计算中，BFS可以在找到第一个叶子节点时立即返回，提高效率
 * - 平衡二叉树判断采用后序遍历，实现剪枝优化
 * - 直径计算与高度计算结合，避免重复计算
 * 
 * 跨语言实现差异：
 * - Java：使用递归、队列、栈等标准数据结构
 * - Python：代码更简洁，使用元组存储节点和深度
 * - C++：需要手动管理内存，提供deleteTree方法释放资源
 * 
 * 相关题目：
 * 1. LeetCode 104. 二叉树的最大深度 - https://leetcode.cn/problems/maximum-depth-of-binary-tree/
 * 2. LeetCode 111. 二叉树的最小深度 - https://leetcode.cn/problems/minimum-depth-of-binary-tree/
 * 3. LeetCode 110. 平衡二叉树 - https://leetcode.cn/problems/balanced-binary-tree/
 * 4. LeetCode 543. 二叉树的直径 - https://leetcode.cn/problems/diameter-of-binary-tree/
 * 5. LintCode 97. 二叉树的最大深度 - https://www.lintcode.com/problem/97
 * 6. HackerRank Tree: Height of a Binary Tree - https://www.hackerrank.com/challenges/tree-height-of-a-binary-tree
 * 7. UVA 12455 - Bars
 * 8. CodeChef - TREE2
 * 9. LeetCode 563. 二叉树的坡度 - https://leetcode.cn/problems/binary-tree-tilt/
 * 10. LeetCode 124. 二叉树中的最大路径和 - https://leetcode.cn/problems/binary-tree-maximum-path-sum/
 * 11. LeetCode 687. 最长同值路径 - https://leetcode.cn/problems/longest-univalue-path/
 * 12. LeetCode 958. 二叉树的完全性检验 - https://leetcode.cn/problems/check-completeness-of-a-binary-tree/
 * 13. LeetCode 1302. 层数最深叶子节点的和 - https://leetcode.cn/problems/deepest-leaves-sum/
 * 14. HackerRank Balanced Brackets - https://www.hackerrank.com/challenges/balanced-brackets
 * 15. GeeksforGeeks Check if a binary tree is height balanced - https://www.geeksforgeeks.org/how-to-determine-if-a-binary-tree-is-balanced/
 * 16. Codeforces 779B - Weird Journey - https://codeforces.com/problemset/problem/779/B
 * 17. AtCoder ABC129D - Lamp - https://atcoder.jp/contests/abc129/tasks/abc129_d
 * 18. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
 * 19. UVa 10942 - Is This the Easiest Problem? - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1883
 * 20. USACO 2017 January Contest, Bronze - Problem 3. Cow Tipping - http://www.usaco.org/index.php?page=viewproblem2&cpid=688
 * 21. 牛客网 NC137 表达式求值 - https://www.nowcoder.com/practice/c215ba61c8b1443b996351df929dc4d4
 * 22. 洛谷 P1028 数的计算 - https://www.luogu.com.cn/problem/P1028
 * 23. 杭电OJ 1115 - Lifting the Stone - http://acm.hdu.edu.cn/showproblem.php?pid=1115
 * 24. AizuOJ ALDS1_7_C - Tree Walk - https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_C
 */
public class Code04_DepthOfBinaryTree {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode() {}

		public TreeNode(int val) {
			this.val = val;
		}

		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
		
		@Override
		public String toString() {
			return "TreeNode{" + "val=" + val + "}";
		}
	}

	/**
	 * 计算二叉树的最大深度 - 递归DFS解法
	 * 实现思路：
	 * - 基本情况：空节点深度为0
	 * - 递归情况：树的最大深度 = max(左子树深度, 右子树深度) + 1
	 * - 采用分治思想，将大问题分解为子问题
	 * 
	 * 优点：代码简洁易懂，实现高效
	 * 缺点：对于极深的树可能导致栈溢出
	 * 
	 * 时间复杂度：O(N)，每个节点只访问一次
	 * 空间复杂度：O(H)，H为树的高度，最坏情况（斜树）为O(N)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最大深度
	 */
	// 测试链接 : https://leetcode.cn/problems/maximum-depth-of-binary-tree/
	public static int maxDepth(TreeNode root) {
		// 基本情况：空节点深度为0
		return root == null ? 0 : Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
	}

	/**
	 * 计算二叉树的最大深度 - 迭代BFS解法
	 * 实现思路：
	 * - 使用队列进行层序遍历
	 * - 记录每层的节点数量，每处理完一层，深度加1
	 * - 适用于需要按层处理的场景
	 * 
	 * 优点：避免递归栈溢出问题，适用于深层树
	 * 缺点：空间复杂度可能高于DFS，因为需要存储整层的节点
	 * 
	 * 时间复杂度：O(N)，每个节点只访问一次
	 * 空间复杂度：O(W)，W为树的最大宽度，最坏情况为O(N)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最大深度
	 */
	public static int maxDepthBFS(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int depth = 0;
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);

		while (!queue.isEmpty()) {
			int levelSize = queue.size(); // 当前层的节点数
			
			// 处理当前层的所有节点
			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();
				
				// 将子节点加入队列
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
			
			// 每处理完一层，深度加1
			depth++;
		}

		return depth;
	}

	/**
	 * 计算二叉树的最大深度 - 迭代DFS解法
	 * 实现思路：
	 * - 使用栈模拟递归过程
	 * - 每个栈元素存储节点和对应的深度
	 * - 先压入右子节点，再压入左子节点，确保左子树先被处理
	 * 
	 * 优点：避免递归栈溢出问题，适用于深层树
	 * 缺点：代码复杂度略高
	 * 
	 * 时间复杂度：O(N)，每个节点只访问一次
	 * 空间复杂度：O(H)，H为树的高度，最坏情况为O(N)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最大深度
	 */
	public static int maxDepthDFS(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int maxDepth = 0;
		// 使用栈存储节点和对应的深度
		Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
		stack.push(new Pair<>(root, 1));

		while (!stack.isEmpty()) {
			Pair<TreeNode, Integer> pair = stack.pop();
			TreeNode node = pair.first;
			int depth = pair.second;

			// 更新最大深度
			maxDepth = Math.max(maxDepth, depth);

			// 先压右子节点，再压左子节点，确保左子节点先被处理
			if (node.right != null) {
				stack.push(new Pair<>(node.right, depth + 1));
			}
			if (node.left != null) {
				stack.push(new Pair<>(node.left, depth + 1));
			}
		}

		return maxDepth;
	}

	/**
	 * 计算二叉树的最小深度 - 递归DFS解法
	 * 实现思路：
	 * - 基本情况：空节点深度为0
	 * - 叶子节点深度为1
	 * - 对于非叶子节点，递归计算左右子树的最小深度
	 * - 注意：需要处理只有一侧子树的情况
	 * 
	 * 关键点：最小深度是从根节点到最近叶子节点的最短路径上的节点数量
	 * 叶子节点定义：没有子节点的节点
	 * 
	 * 时间复杂度：O(N)，每个节点只访问一次
	 * 空间复杂度：O(H)，H为树的高度，最坏情况为O(N)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	// 测试链接 : https://leetcode.cn/problems/minimum-depth-of-binary-tree/
	public int minDepth(TreeNode root) {
		if (root == null) {
			// 当前的树是空树
			return 0;
		}
		if (root.left == null && root.right == null) {
			// 当前root是叶节点
			return 1;
		}
		int ldeep = Integer.MAX_VALUE;
		int rdeep = Integer.MAX_VALUE;
		if (root.left != null) {
			ldeep = minDepth(root.left);
		}
		if (root.right != null) {
			rdeep = minDepth(root.right);
		}
		return Math.min(ldeep, rdeep) + 1;
	}

	/**
	 * 计算二叉树的最小深度 - 优化递归解法
	 * 实现思路：
	 * - 基本情况：空节点深度为0
	 * - 优化处理单侧子树情况：如果左子树为空，返回右子树深度+1；反之亦然
	 * - 当左右子树都不为空时，取较小深度+1
	 * 
	 * 优点：代码更简洁，逻辑更清晰
	 * 缺点：同样存在递归栈溢出风险
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	public static int minDepthRecursive(TreeNode root) {
		if (root == null) {
			return 0;
		}

		// 如果左子树为空，则返回右子树的最小深度+1
		if (root.left == null) {
			return minDepthRecursive(root.right) + 1;
		}

		// 如果右子树为空，则返回左子树的最小深度+1
		if (root.right == null) {
			return minDepthRecursive(root.left) + 1;
		}

		// 如果左右子树都不为空，取较小值+1
		return Math.min(minDepthRecursive(root.left), minDepthRecursive(root.right)) + 1;
	}

	/**
	 * 计算二叉树的最小深度 - 迭代BFS解法（最优解）
	 * 实现思路：
	 * - 使用队列进行层序遍历
	 * - 记录当前深度，从1开始递增
	 * - 一旦找到叶子节点，立即返回当前深度，无需遍历整棵树
	 * 
	 * 为什么是最优解：
	 * - BFS保证最先找到的叶子节点一定是最近的叶子节点
	 * - 一旦找到目标可以立即返回，不会不必要地遍历深层节点
	 * - 对于不平衡的树，效率远高于DFS实现
	 * 
	 * 时间复杂度：O(N)，最坏情况下访问所有节点，但平均情况表现更好
	 * 空间复杂度：O(W)，W为树的最大宽度
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	public static int minDepthBFS(TreeNode root) {
		if (root == null) {
			return 0;
		}

		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int depth = 0;

		while (!queue.isEmpty()) {
			depth++;
			int levelSize = queue.size();

			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();

				// 如果是叶子节点，直接返回当前深度
				if (node.left == null && node.right == null) {
					return depth;
				}

				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
		}

		return depth; // 不会到达这里
	}

	/**
	 * 计算二叉树的最小深度 - 迭代DFS解法
	 * 实现思路：
	 * - 使用栈模拟递归过程
	 * - 每个栈元素存储节点和对应的深度
	 * - 遇到叶子节点时更新最小深度
	 * - 需要遍历完所有可能的路径才能确定最小深度
	 * 
	 * 优点：避免递归栈溢出问题
	 * 缺点：对于大多数情况，效率低于BFS实现，因为可能需要遍历整棵树
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	public static int minDepthDFS(TreeNode root) {
		if (root == null) {
			return 0;
		}

		int minDepth = Integer.MAX_VALUE;
		Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
		stack.push(new Pair<>(root, 1));

		while (!stack.isEmpty()) {
			Pair<TreeNode, Integer> pair = stack.pop();
			TreeNode node = pair.first;
			int depth = pair.second;

			// 如果是叶子节点，更新最小深度
			if (node.left == null && node.right == null) {
				minDepth = Math.min(minDepth, depth);
			}

			// 继续遍历子节点
			if (node.right != null) {
				stack.push(new Pair<>(node.right, depth + 1));
			}
			if (node.left != null) {
				stack.push(new Pair<>(node.left, depth + 1));
			}
		}

		return minDepth;
	}

	/**
	 * 判断二叉树是否是平衡二叉树
	 * 实现思路：
	 * - 平衡二叉树定义：任意节点的左右子树高度差不超过1
	 * - 使用后序遍历策略，自底向上计算高度
	 * - 如果在计算过程中发现不平衡，可以提前返回，实现剪枝
	 * 
	 * 优化点：
	 * - 一旦发现不平衡，立即返回-1，避免不必要的计算
	 * - 同时计算高度和判断平衡性，避免多次遍历
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @return 如果是平衡二叉树返回true，否则返回false
	 */
	public static boolean isBalanced(TreeNode root) {
		return height(root) != -1;
	}

	/**
	 * 辅助方法：计算树的高度，如果不是平衡树则返回-1
	 * 实现思路：
	 * - 递归计算左右子树的高度
	 * - 在递归过程中检查平衡性
	 * - 如果左子树或右子树不平衡，直接返回-1
	 * - 如果当前节点不平衡（左右子树高度差>1），返回-1
	 * - 否则返回树的高度
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @return 树的高度，如果不是平衡树则返回-1
	 */
	private static int height(TreeNode root) {
		if (root == null) {
			return 0;
		}

		// 递归计算左右子树的高度
		int leftHeight = height(root.left);
		if (leftHeight == -1) {
			return -1; // 左子树不平衡
		}

		int rightHeight = height(root.right);
		if (rightHeight == -1) {
			return -1; // 右子树不平衡
		}

		// 检查当前节点是否平衡
		if (Math.abs(leftHeight - rightHeight) > 1) {
			return -1; // 不平衡
		}

		return Math.max(leftHeight, rightHeight) + 1;
	}

	/**
	 * 计算二叉树的直径（最长路径长度）
	 * 实现思路：
	 * - 直径定义：树中任意两个节点之间的最长路径的长度
	 * - 最长路径可能经过根节点，也可能不经过根节点
	 * - 使用递归计算每个子树的高度，并在递归过程中更新直径
	 * - 对于每个节点，其作为路径最高点的路径长度 = 左子树高度 + 右子树高度
	 * 
	 * 关键点：
	 * - 直径不一定要经过根节点
	 * - 需要在遍历过程中记录全局最大值
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的直径
	 */
	public static int diameterOfBinaryTree(TreeNode root) {
		int[] diameter = new int[1]; // 使用数组保存直径，便于在递归中修改
		calculateDiameter(root, diameter);
		return diameter[0];
	}

	/**
	 * 辅助方法：计算树的高度并更新直径
	 * 实现思路：
	 * - 递归计算左右子树的高度
	 * - 对于当前节点，计算可能的最长路径：左子树高度 + 右子树高度
	 * - 更新全局直径最大值
	 * - 返回当前子树的高度
	 * 
	 * 使用数组保存直径的原因：
	 * - 数组在Java中是引用类型，可以在递归过程中修改其内容
	 * - 相比使用成员变量，这种方式更封装，不影响类的状态
	 * 
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)
	 * 
	 * @param root 二叉树的根节点
	 * @param diameter 保存直径的数组
	 * @return 当前子树的高度
	 */
	private static int calculateDiameter(TreeNode root, int[] diameter) {
		if (root == null) {
			return 0;
		}

		int leftHeight = calculateDiameter(root.left, diameter);
		int rightHeight = calculateDiameter(root.right, diameter);

		// 更新直径：左子树高度 + 右子树高度
		diameter[0] = Math.max(diameter[0], leftHeight + rightHeight);

		return Math.max(leftHeight, rightHeight) + 1;
	}

	/**
	 * 辅助类：用于存储节点和对应的值（如深度）
	 * 泛型设计提高了代码的复用性
	 */
	private static class Pair<K, V> {
		K first;
		V second;

		public Pair(K first, V second) {
			this.first = first;
			this.second = second;
		}
	}

	/**
	 * 生成测试用例
	 * 用于生成不同类型的树以测试各种算法
	 * 
	 * @param type 树类型：
	 * - 0: 普通二叉树
	 * - 1: 完全二叉树
	 * - 2: 左倾斜树
	 * - 3: 右倾斜树
	 * - 4: 单节点树
	 * @return 测试用的二叉树
	 */
	public static TreeNode generateTestTree(int type) {
		switch (type) {
			case 1: // 完全二叉树
				//      1
				//    /   \
				//   2     3
				//  / \   / \
				// 4   5 6   7
				TreeNode root1 = new TreeNode(1);
				root1.left = new TreeNode(2);
				root1.right = new TreeNode(3);
				root1.left.left = new TreeNode(4);
				root1.left.right = new TreeNode(5);
				root1.right.left = new TreeNode(6);
				root1.right.right = new TreeNode(7);
				return root1;
			case 2: // 左倾斜树（用于测试最小深度）
				//    1
				//   /
				//  2
				// /
				// 3
				TreeNode root2 = new TreeNode(1);
				root2.left = new TreeNode(2);
				root2.left.left = new TreeNode(3);
				return root2;
			case 3: // 右倾斜树（用于测试最小深度）
				// 1
				//  \
				//   2
				//    \
				//     3
				TreeNode root3 = new TreeNode(1);
				root3.right = new TreeNode(2);
				root3.right.right = new TreeNode(3);
				return root3;
			case 4: // 单节点树
				return new TreeNode(1);
			default: // 普通二叉树
				//      1
				//    /   \
				//   2     3
				//  /       \
				// 4         5
				//           \
				//            6
				TreeNode root = new TreeNode(1);
				root.left = new TreeNode(2);
				root.right = new TreeNode(3);
				root.left.left = new TreeNode(4);
				root.right.right = new TreeNode(5);
				root.right.right.right = new TreeNode(6);
				return root;
		}
	}

	/**
	 * 性能测试方法
	 * 用于比较不同算法在深层树情况下的性能表现
	 */
	public static void performanceTest() {
		System.out.println("\n===== 性能测试 =====");
		
		// 创建一个深度为1000的左链式树
		TreeNode deepTree = new TreeNode(1);
		TreeNode current = deepTree;
		for (int i = 0; i < 1000; i++) {
			current.left = new TreeNode(i + 2);
			current = current.left;
		}
		
		// 测试最大深度算法性能
		System.out.println("\n最大深度算法性能测试 (深层树):");
		
		long startTime = System.nanoTime();
		int maxDepth1 = maxDepth(deepTree);
		long endTime = System.nanoTime();
		System.out.println("递归DFS - 结果: " + maxDepth1 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		startTime = System.nanoTime();
		int maxDepth2 = maxDepthBFS(deepTree);
		endTime = System.nanoTime();
		System.out.println("迭代BFS - 结果: " + maxDepth2 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		startTime = System.nanoTime();
		int maxDepth3 = maxDepthDFS(deepTree);
		endTime = System.nanoTime();
		System.out.println("迭代DFS - 结果: " + maxDepth3 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		// 测试最小深度算法性能
		System.out.println("\n最小深度算法性能测试 (深层树):");
		
		startTime = System.nanoTime();
		int minDepth1 = minDepthRecursive(deepTree);
		endTime = System.nanoTime();
		System.out.println("优化递归 - 结果: " + minDepth1 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		startTime = System.nanoTime();
		int minDepth2 = minDepthBFS(deepTree);
		endTime = System.nanoTime();
		System.out.println("迭代BFS - 结果: " + minDepth2 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		startTime = System.nanoTime();
		int minDepth3 = minDepthDFS(deepTree);
		endTime = System.nanoTime();
		System.out.println("迭代DFS - 结果: " + minDepth3 + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
	}
	
	/**
	 * 打印树结构辅助方法
	 * 用于调试和可视化树的结构
	 */
	public static void printTree(TreeNode root) {
		if (root == null) {
			System.out.println("[]");
			return;
		}
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		StringBuilder sb = new StringBuilder("[");
		
		while (!queue.isEmpty()) {
			int levelSize = queue.size();
			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();
				if (node != null) {
					sb.append(node.val);
					queue.offer(node.left);
					queue.offer(node.right);
				} else {
					sb.append("null");
				}
				if (i < levelSize - 1) {
					sb.append(", ");
				}
			}
			// 检查队列是否全为null
			boolean allNull = true;
			for (TreeNode node : queue) {
				if (node != null) {
					allNull = false;
					break;
				}
			}
			if (!allNull) {
				sb.append(", ");
			}
		}
		
		// 移除末尾的null值
		int end = sb.length() - 1;
		while (end >= 0 && (sb.charAt(end) == 'l' || sb.charAt(end) == 'n' || 
		                  sb.charAt(end) == ' ' || sb.charAt(end) == ',')) {
			end--;
		}
		sb.setLength(end + 1);
		sb.append("]");
		System.out.println(sb.toString());
	}
	
	/**
	 * 主方法：测试所有深度相关算法
	 * 提供完整的测试覆盖，包括不同类型的树和边界情况
	 */
	public static void main(String[] args) {
		System.out.println("二叉树深度相关算法测试");
		System.out.println("====================\n");

		// 1. 标准测试用例 - 不同类型的树
		System.out.println("===== 标准测试用例 =====");
		for (int treeType = 0; treeType <= 4; treeType++) {
			System.out.println("\n测试树类型 " + treeType + ":");
			TreeNode root = generateTestTree(treeType);
			System.out.print("树结构: ");
			printTree(root);

			// 最大深度测试
			int maxDepth1 = maxDepth(root);
			int maxDepth2 = maxDepthBFS(root);
			int maxDepth3 = maxDepthDFS(root);
			System.out.println("最大深度 (递归DFS): " + maxDepth1);
			System.out.println("最大深度 (迭代BFS): " + maxDepth2);
			System.out.println("最大深度 (迭代DFS): " + maxDepth3);

			// 最小深度测试
			Code04_DepthOfBinaryTree instance = new Code04_DepthOfBinaryTree();
			int minDepth1 = instance.minDepth(root);
			int minDepth2 = minDepthRecursive(root);
			int minDepth3 = minDepthBFS(root);
			int minDepth4 = minDepthDFS(root);
			System.out.println("最小深度 (原递归): " + minDepth1);
			System.out.println("最小深度 (优化递归): " + minDepth2);
			System.out.println("最小深度 (迭代BFS): " + minDepth3);
			System.out.println("最小深度 (迭代DFS): " + minDepth4);

			// 平衡树测试
			boolean isBalanced = isBalanced(root);
			System.out.println("是否是平衡二叉树: " + isBalanced);

			// 直径测试
			int diameter = diameterOfBinaryTree(root);
			System.out.println("二叉树的直径: " + diameter);
		}

		// 2. 边界情况测试
		System.out.println("\n===== 边界情况测试 =====");
		TreeNode nullRoot = null;
		System.out.println("空树最大深度: " + maxDepth(nullRoot));
		System.out.println("空树最小深度: " + minDepthRecursive(nullRoot));
		System.out.println("空树是否平衡: " + isBalanced(nullRoot));
		System.out.println("空树直径: " + diameterOfBinaryTree(nullRoot));
		
		// 3. 单节点树测试
		System.out.println("\n===== 单节点树测试 =====");
		TreeNode singleNode = new TreeNode(5);
		System.out.print("树结构: ");
		printTree(singleNode);
		System.out.println("最大深度: " + maxDepth(singleNode));
		System.out.println("最小深度: " + minDepthRecursive(singleNode));
		System.out.println("是否平衡: " + isBalanced(singleNode));
		System.out.println("直径: " + diameterOfBinaryTree(singleNode));
		
		// 4. 深层树测试
		System.out.println("\n===== 深层树测试 =====");
		TreeNode deepTree = new TreeNode(1);
		TreeNode current = deepTree;
		for (int i = 0; i < 100; i++) {
			current.left = new TreeNode(i + 2);
			current = current.left;
		}
		System.out.println("101层左链式树:");
		System.out.println("最大深度: " + maxDepth(deepTree));
		System.out.println("最小深度: " + minDepthRecursive(deepTree));
		System.out.println("是否平衡: " + isBalanced(deepTree));
		System.out.println("直径: " + diameterOfBinaryTree(deepTree));
		
		// 5. 性能测试
		performanceTest();
	}
}

/*
Python实现:

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class BinaryTreeDepth:
    """
    二叉树深度相关算法实现类
    包含最大深度、最小深度、平衡树判断、树直径计算等功能
    """
    
    def max_depth_recursive(self, root):
        """
        计算二叉树的最大深度 - 递归DFS解法
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        if not root:
            return 0
        return max(self.max_depth_recursive(root.left), self.max_depth_recursive(root.right)) + 1
    
    def max_depth_bfs(self, root):
        """
        计算二叉树的最大深度 - 迭代BFS解法
        时间复杂度: O(N)
        空间复杂度: O(W)
        """
        if not root:
            return 0
        
        from collections import deque
        queue = deque([root])
        depth = 0
        
        while queue:
            level_size = len(queue)
            for _ in range(level_size):
                node = queue.popleft()
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            depth += 1
        
        return depth
    
    def max_depth_dfs(self, root):
        """
        计算二叉树的最大深度 - 迭代DFS解法
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        if not root:
            return 0
        
        max_depth = 0
        stack = [(root, 1)]
        
        while stack:
            node, depth = stack.pop()
            max_depth = max(max_depth, depth)
            
            if node.right:
                stack.append((node.right, depth + 1))
            if node.left:
                stack.append((node.left, depth + 1))
        
        return max_depth
    
    def min_depth_recursive(self, root):
        """
        计算二叉树的最小深度 - 递归DFS解法
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        if not root:
            return 0
        
        if not root.left and not root.right:
            return 1
        
        min_depth = float('inf')
        if root.left:
            min_depth = min(min_depth, self.min_depth_recursive(root.left))
        if root.right:
            min_depth = min(min_depth, self.min_depth_recursive(root.right))
        
        return min_depth + 1
    
    def min_depth_optimized(self, root):
        """
        计算二叉树的最小深度 - 优化递归解法
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        if not root:
            return 0
        
        # 如果左子树为空，则返回右子树的最小深度+1
        if not root.left:
            return self.min_depth_optimized(root.right) + 1
        
        # 如果右子树为空，则返回左子树的最小深度+1
        if not root.right:
            return self.min_depth_optimized(root.left) + 1
        
        # 如果左右子树都不为空，取较小值+1
        return min(self.min_depth_optimized(root.left), self.min_depth_optimized(root.right)) + 1
    
    def min_depth_bfs(self, root):
        """
        计算二叉树的最小深度 - 迭代BFS解法（最优解）
        时间复杂度: O(N)
        空间复杂度: O(W)
        """
        if not root:
            return 0
        
        from collections import deque
        queue = deque([root])
        depth = 0
        
        while queue:
            depth += 1
            level_size = len(queue)
            
            for _ in range(level_size):
                node = queue.popleft()
                
                # 如果是叶子节点，直接返回当前深度
                if not node.left and not node.right:
                    return depth
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
        
        return depth
    
    def min_depth_dfs(self, root):
        """
        计算二叉树的最小深度 - 迭代DFS解法
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        if not root:
            return 0
        
        min_depth = float('inf')
        stack = [(root, 1)]
        
        while stack:
            node, depth = stack.pop()
            
            # 如果是叶子节点，更新最小深度
            if not node.left and not node.right:
                min_depth = min(min_depth, depth)
            
            if node.right:
                stack.append((node.right, depth + 1))
            if node.left:
                stack.append((node.left, depth + 1))
        
        return min_depth
    
    def is_balanced(self, root):
        """
        判断二叉树是否是平衡二叉树
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        def height(node):
            if not node:
                return 0
            
            left_height = height(node.left)
            if left_height == -1:
                return -1  # 左子树不平衡
            
            right_height = height(node.right)
            if right_height == -1:
                return -1  # 右子树不平衡
            
            # 检查当前节点是否平衡
            if abs(left_height - right_height) > 1:
                return -1  # 不平衡
            
            return max(left_height, right_height) + 1
        
        return height(root) != -1
    
    def diameter_of_binary_tree(self, root):
        """
        计算二叉树的直径（最长路径长度）
        时间复杂度: O(N)
        空间复杂度: O(H)
        """
        diameter = [0]  # 使用列表保存直径，便于在递归中修改
        
        def calculate_diameter(node):
            if not node:
                return 0
            
            left_height = calculate_diameter(node.left)
            right_height = calculate_diameter(node.right)
            
            # 更新直径
            diameter[0] = max(diameter[0], left_height + right_height)
            
            return max(left_height, right_height) + 1
        
        calculate_diameter(root)
        return diameter[0]
    
    def generate_test_tree(self, tree_type):
        """
        生成测试用例
        """
        if tree_type == 1:  # 完全二叉树
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.right = TreeNode(3)
            root.left.left = TreeNode(4)
            root.left.right = TreeNode(5)
            root.right.left = TreeNode(6)
            root.right.right = TreeNode(7)
            return root
        elif tree_type == 2:  # 左倾斜树
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.left.left = TreeNode(3)
            return root
        elif tree_type == 3:  # 右倾斜树
            root = TreeNode(1)
            root.right = TreeNode(2)
            root.right.right = TreeNode(3)
            return root
        elif tree_type == 4:  # 单节点树
            return TreeNode(1)
        else:  # 普通二叉树
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.right = TreeNode(3)
            root.left.left = TreeNode(4)
            root.right.right = TreeNode(5)
            root.right.right.right = TreeNode(6)
            return root

# 测试代码
if __name__ == "__main__":
    depth_calculator = BinaryTreeDepth()
    print("二叉树深度相关算法测试")
    print("====================\n")
    
    # 测试不同类型的树
    for tree_type in range(5):
        print(f"测试树类型 {tree_type}:")
        root = depth_calculator.generate_test_tree(tree_type)
        
        # 最大深度测试
        max_depth1 = depth_calculator.max_depth_recursive(root)
        max_depth2 = depth_calculator.max_depth_bfs(root)
        max_depth3 = depth_calculator.max_depth_dfs(root)
        print(f"最大深度 (递归DFS): {max_depth1}")
        print(f"最大深度 (迭代BFS): {max_depth2}")
        print(f"最大深度 (迭代DFS): {max_depth3}")
        
        # 最小深度测试
        min_depth1 = depth_calculator.min_depth_recursive(root)
        min_depth2 = depth_calculator.min_depth_optimized(root)
        min_depth3 = depth_calculator.min_depth_bfs(root)
        min_depth4 = depth_calculator.min_depth_dfs(root)
        print(f"最小深度 (原递归): {min_depth1}")
        print(f"最小深度 (优化递归): {min_depth2}")
        print(f"最小深度 (迭代BFS): {min_depth3}")
        print(f"最小深度 (迭代DFS): {min_depth4}")
        
        # 平衡树测试
        is_balanced = depth_calculator.is_balanced(root)
        print(f"是否是平衡二叉树: {is_balanced}")
        
        # 直径测试
        diameter = depth_calculator.diameter_of_binary_tree(root)
        print(f"二叉树的直径: {diameter}")
        
        print()
    
    # 边界情况测试
    print("边界情况测试:")
    
    print(f"空树最大深度: {depth_calculator.max_depth_recursive(None)}")
    print(f"空树最小深度: {depth_calculator.min_depth_optimized(None)}")
    print(f"空树是否平衡: {depth_calculator.is_balanced(None)}")
    print(f"空树直径: {depth_calculator.diameter_of_binary_tree(None)}")

C++实现:

#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <algorithm>
#include <climits>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class BinaryTreeDepth {
public:
    /**
     * 二叉树深度相关算法实现类
     */
    
    // 计算二叉树的最大深度 - 递归DFS解法
    int maxDepthRecursive(TreeNode* root) {
        if (!root) {
            return 0;
        }
        return max(maxDepthRecursive(root->left), maxDepthRecursive(root->right)) + 1;
    }
    
    // 计算二叉树的最大深度 - 迭代BFS解法
    int maxDepthBFS(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        queue<TreeNode*> q;
        q.push(root);
        int depth = 0;
        
        while (!q.empty()) {
            int levelSize = q.size();
            for (int i = 0; i < levelSize; ++i) {
                TreeNode* node = q.front();
                q.pop();
                
                if (node->left) {
                    q.push(node->left);
                }
                if (node->right) {
                    q.push(node->right);
                }
            }
            depth++;
        }
        
        return depth;
    }
    
    // 计算二叉树的最大深度 - 迭代DFS解法
    int maxDepthDFS(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int maxDepth = 0;
        stack<pair<TreeNode*, int>> s;
        s.push({root, 1});
        
        while (!s.empty()) {
            auto [node, depth] = s.top();
            s.pop();
            
            maxDepth = max(maxDepth, depth);
            
            if (node->right) {
                s.push({node->right, depth + 1});
            }
            if (node->left) {
                s.push({node->left, depth + 1});
            }
        }
        
        return maxDepth;
    }
    
    // 计算二叉树的最小深度 - 递归DFS解法
    int minDepthRecursive(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        if (!root->left && !root->right) {
            return 1;
        }
        
        int minDepth = INT_MAX;
        if (root->left) {
            minDepth = min(minDepth, minDepthRecursive(root->left));
        }
        if (root->right) {
            minDepth = min(minDepth, minDepthRecursive(root->right));
        }
        
        return minDepth + 1;
    }
    
    // 计算二叉树的最小深度 - 优化递归解法
    int minDepthOptimized(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        // 如果左子树为空，则返回右子树的最小深度+1
        if (!root->left) {
            return minDepthOptimized(root->right) + 1;
        }
        
        // 如果右子树为空，则返回左子树的最小深度+1
        if (!root->right) {
            return minDepthOptimized(root->left) + 1;
        }
        
        // 如果左右子树都不为空，取较小值+1
        return min(minDepthOptimized(root->left), minDepthOptimized(root->right)) + 1;
    }
    
    // 计算二叉树的最小深度 - 迭代BFS解法（最优解）
    int minDepthBFS(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        queue<TreeNode*> q;
        q.push(root);
        int depth = 0;
        
        while (!q.empty()) {
            depth++;
            int levelSize = q.size();
            
            for (int i = 0; i < levelSize; ++i) {
                TreeNode* node = q.front();
                q.pop();
                
                // 如果是叶子节点，直接返回当前深度
                if (!node->left && !node->right) {
                    return depth;
                }
                
                if (node->left) {
                    q.push(node->left);
                }
                if (node->right) {
                    q.push(node->right);
                }
            }
        }
        
        return depth;
    }
    
    // 计算二叉树的最小深度 - 迭代DFS解法
    int minDepthDFS(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int minDepth = INT_MAX;
        stack<pair<TreeNode*, int>> s;
        s.push({root, 1});
        
        while (!s.empty()) {
            auto [node, depth] = s.top();
            s.pop();
            
            // 如果是叶子节点，更新最小深度
            if (!node->left && !node->right) {
                minDepth = min(minDepth, depth);
            }
            
            if (node->right) {
                s.push({node->right, depth + 1});
            }
            if (node->left) {
                s.push({node->left, depth + 1});
            }
        }
        
        return minDepth;
    }
    
    // 判断二叉树是否是平衡二叉树
    bool isBalanced(TreeNode* root) {
        return height(root) != -1;
    }
    
    // 辅助方法：计算树的高度，如果不是平衡树则返回-1
    int height(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int leftHeight = height(root->left);
        if (leftHeight == -1) {
            return -1; // 左子树不平衡
        }
        
        int rightHeight = height(root->right);
        if (rightHeight == -1) {
            return -1; // 右子树不平衡
        }
        
        // 检查当前节点是否平衡
        if (abs(leftHeight - rightHeight) > 1) {
            return -1; // 不平衡
        }
        
        return max(leftHeight, rightHeight) + 1;
    }
    
    // 计算二叉树的直径（最长路径长度）
    int diameterOfBinaryTree(TreeNode* root) {
        int diameter = 0;
        calculateDiameter(root, diameter);
        return diameter;
    }
    
    // 辅助方法：计算树的高度并更新直径
    int calculateDiameter(TreeNode* root, int& diameter) {
        if (!root) {
            return 0;
        }
        
        int leftHeight = calculateDiameter(root->left, diameter);
        int rightHeight = calculateDiameter(root->right, diameter);
        
        // 更新直径
        diameter = max(diameter, leftHeight + rightHeight);
        
        return max(leftHeight, rightHeight) + 1;
    }
    
    // 生成测试用例
    TreeNode* generateTestTree(int treeType) {
        if (treeType == 1) {  // 完全二叉树
            TreeNode* root = new TreeNode(1);
            root->left = new TreeNode(2);
            root->right = new TreeNode(3);
            root->left->left = new TreeNode(4);
            root->left->right = new TreeNode(5);
            root->right->left = new TreeNode(6);
            root->right->right = new TreeNode(7);
            return root;
        } else if (treeType == 2) {  // 左倾斜树
            TreeNode* root = new TreeNode(1);
            root->left = new TreeNode(2);
            root->left->left = new TreeNode(3);
            return root;
        } else if (treeType == 3) {  // 右倾斜树
            TreeNode* root = new TreeNode(1);
            root->right = new TreeNode(2);
            root->right->right = new TreeNode(3);
            return root;
        } else if (treeType == 4) {  // 单节点树
            return new TreeNode(1);
        } else {  // 普通二叉树
            TreeNode* root = new TreeNode(1);
            root->left = new TreeNode(2);
            root->right = new TreeNode(3);
            root->left->left = new TreeNode(4);
            root->right->right = new TreeNode(5);
            root->right->right->right = new TreeNode(6);
            return root;
        }
    }
    
    // 释放树内存
    void deleteTree(TreeNode* root) {
        if (root) {
            deleteTree(root->left);
            deleteTree(root->right);
            delete root;
        }
    }
};

// 测试代码
int main() {
    BinaryTreeDepth depthCalculator;
    cout << "二叉树深度相关算法测试" << endl;
    cout << "====================\n" << endl;
    
    // 测试不同类型的树
    for (int treeType = 0; treeType < 5; ++treeType) {
        cout << "测试树类型 " << treeType << ":" << endl;
        TreeNode* root = depthCalculator.generateTestTree(treeType);
        
        // 最大深度测试
        int maxDepth1 = depthCalculator.maxDepthRecursive(root);
        int maxDepth2 = depthCalculator.maxDepthBFS(root);
        int maxDepth3 = depthCalculator.maxDepthDFS(root);
        cout << "最大深度 (递归DFS): " << maxDepth1 << endl;
        cout << "最大深度 (迭代BFS): " << maxDepth2 << endl;
        cout << "最大深度 (迭代DFS): " << maxDepth3 << endl;
        
        // 最小深度测试
        int minDepth1 = depthCalculator.minDepthRecursive(root);
        int minDepth2 = depthCalculator.minDepthOptimized(root);
        int minDepth3 = depthCalculator.minDepthBFS(root);
        int minDepth4 = depthCalculator.minDepthDFS(root);
        cout << "最小深度 (原递归): " << minDepth1 << endl;
        cout << "最小深度 (优化递归): " << minDepth2 << endl;
        cout << "最小深度 (迭代BFS): " << minDepth3 << endl;
        cout << "最小深度 (迭代DFS): " << minDepth4 << endl;
        
        // 平衡树测试
        bool isBalanced = depthCalculator.isBalanced(root);
        cout << "是否是平衡二叉树: " << (isBalanced ? "true" : "false") << endl;
        
        // 直径测试
        int diameter = depthCalculator.diameterOfBinaryTree(root);
        cout << "二叉树的直径: " << diameter << endl;
        
        cout << endl;
        
        // 释放内存
        depthCalculator.deleteTree(root);
    }
    
    // 边界情况测试
    cout << "边界情况测试:" << endl;
    
    cout << "空树最大深度: " << depthCalculator.maxDepthRecursive(nullptr) << endl;
    cout << "空树最小深度: " << depthCalculator.minDepthOptimized(nullptr) << endl;
    cout << "空树是否平衡: " << (depthCalculator.isBalanced(nullptr) ? "true" : "false") << endl;
    cout << "空树直径: " << depthCalculator.diameterOfBinaryTree(nullptr) << endl;
    
    return 0;
}
*/
