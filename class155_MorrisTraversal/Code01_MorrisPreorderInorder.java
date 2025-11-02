package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;

/**
 * Morris遍历实现先序和中序遍历
 * 
 * 题目来源：
 * - 先序遍历：LeetCode 144. Binary Tree Preorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-preorder-traversal/
 * - 中序遍历：LeetCode 94. Binary Tree Inorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-inorder-traversal/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris先序和中序遍历
 * 2. 递归版本的先序和中序遍历
 * 3. 迭代版本的先序和中序遍历
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例（常规树、空树、单节点树、链表结构树等）
 * 6. 性能测试和算法对比
 * 7. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-morrisxian-xu-bian-li-by-xxx/
 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-morrisxian-xu-bian-li-by-xxx/
 * 
 * 算法详解：
 * Morris遍历的核心思想是利用二叉树中大量空闲的空指针来存储遍历所需的路径信息，从而避免使用栈或递归调用栈所需的额外空间
 * 1. 线索化：对于每个有左子树的节点，将其左子树的最右节点的右指针指向该节点本身，形成一个临时的线索
 * 2. 两次访问：第一次访问节点时建立线索，第二次访问节点时删除线索并处理右子树
 * 3. 还原树结构：每次访问完节点后，都会恢复树的原始结构，不影响后续操作
 * 
 * 时间复杂度：O(n)，虽然每个节点可能被访问两次，但总体操作次数仍是线性的
 * 空间复杂度：O(1)，只使用了常数级别的额外空间
 * 适用场景：内存受限环境、嵌入式系统、超大二叉树遍历
 * 优缺点分析：
 * - 优点：空间复杂度最优，不依赖栈或递归调用栈
 * - 缺点：实现复杂，修改树结构，不适合并发环境
 */
public class Code01_MorrisPreorderInorder {

	// 二叉树节点定义
	public class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;
		TreeNode() {}
		TreeNode(int val) { this.val = val; }
		TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	/**
	 * 基础Morris遍历框架
	 * 
	 * 算法步骤：
	 * 1. 初始化当前节点cur为根节点
	 * 2. 当cur不为null时：
	 *    a. 如果cur没有左子树，cur移动到其右子树
	 *    b. 如果cur有左子树：
	 *       i. 找到cur左子树的最右节点mostRight
	 *       ii. 如果mostRight的right指针为null（第一次访问cur）：
	 *           - 将mostRight的right指向cur
	 *           - cur移动到其左子树
	 *       iii. 如果mostRight的right指针指向cur（第二次访问cur）：
	 *           - 将mostRight的right恢复为null
	 *           - cur移动到其右子树
	 *
	 * 时间复杂度：O(n)，虽然每个节点可能被访问两次，但总体操作次数仍是线性的
	 * 空间复杂度：O(1)，只使用了常数级别的额外空间
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-morrisxian-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-morrisxian-xu-bian-li-by-xxx/
	 */
	public static void morris(TreeNode head) {
		// 防御性编程：处理空树情况
		if (head == null) {
			return;
		}
		
		TreeNode cur = head;
		TreeNode mostRight = null;
		while (cur != null) {
			mostRight = cur.left; // 尝试访问左子树
			if (mostRight != null) { // cur有左树
				// 找到左树最右节点
				// 注意：需要判断right指针是否指向cur本身，避免死循环
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				// 判断左树最右节点的右指针状态
				if (mostRight.right == null) { // 第一次到达cur节点
					mostRight.right = cur; // 建立线索，记录回来的路径
					cur = cur.left; // 继续遍历左子树
					continue; // 跳过后续步骤，不处理右子树
				} else { // 第二次到达cur节点
					mostRight.right = null; // 恢复树的原始结构
				}
			}
			// 没有左子树或者已经处理完左子树（第二次访问）
			cur = cur.right;
		}
	}

	/**
	 * Morris遍历实现先序遍历
	 * 
	 * 先序遍历顺序：根-左-右
	 * 在Morris遍历中的实现：
	 * - 第一次访问节点时就收集值（适合先序遍历）
	 * - 如果节点没有左子树，则在第一次访问时直接收集
	 *
	 * 测试链接 : https://leetcode.cn/problems/binary-tree-preorder-traversal/
	 * 提交preorderTraversal方法，可以直接通过
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-morrisxian-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-morrisxian-xu-bian-li-by-xxx/
	 */
	public static List<Integer> preorderTraversal(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		// 防御性编程：处理空树情况
		if (head == null) {
			return ans;
		}
		morrisPreorder(head, ans);
		return ans;
	}

	/**
	 * Morris先序遍历的核心实现
	 * @param head 根节点
	 * @param ans 结果集合
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-morrisxian-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-morrisxian-xu-bian-li-by-xxx/
	 */
	public static void morrisPreorder(TreeNode head, List<Integer> ans) {
		TreeNode cur = head;
		TreeNode mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) { // cur有左树
				// 找到左树最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				// 判断左树最右节点的右指针状态
				if (mostRight.right == null) { // 第一次到达
					// 先序遍历：第一次访问时收集节点值
					ans.add(cur.val);
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次到达
					mostRight.right = null;
					// 第二次访问时不收集，因为先序遍历已经在第一次访问时收集了
				}
			} else { // cur无左树
				// 无左子树时，只有一次访问机会，直接收集
				ans.add(cur.val);
			}
			cur = cur.right;
		}
	}

	/**
	 * Morris遍历实现中序遍历
	 * 
	 * 中序遍历顺序：左-根-右
	 * 在Morris遍历中的实现：
	 * - 第二次访问节点时收集值（适合中序遍历）
	 * - 如果节点没有左子树，则在访问时直接收集
	 *
	 * 测试链接 : https://leetcode.cn/problems/binary-tree-inorder-traversal/
	 * 提交inorderTraversal方法，可以直接通过
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/python-morriszhong-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/c-morriszhong-xu-bian-li-by-xxx/
	 */
	public static List<Integer> inorderTraversal(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		// 防御性编程：处理空树情况
		if (head == null) {
			return ans;
		}
		morrisInorder(head, ans);
		return ans;
	}

	/**
	 * Morris中序遍历的核心实现
	 * @param head 根节点
	 * @param ans 结果集合
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/python-morriszhong-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/c-morriszhong-xu-bian-li-by-xxx/
	 */
	public static void morrisInorder(TreeNode head, List<Integer> ans) {
		TreeNode cur = head;
		TreeNode mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) { // cur有左树
				// 找到左树最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				// 判断左树最右节点的右指针状态
				if (mostRight.right == null) { // 第一次到达
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次到达
					mostRight.right = null;
					// 中序遍历：第二次访问时收集节点值
					ans.add(cur.val);
				}
			} else { // cur无左树
				// 无左子树时，只有一次访问机会，直接收集
				ans.add(cur.val);
			}
			cur = cur.right;
		}
	}

	/**
	 * 使用递归实现先序遍历
	 * 时间复杂度：O(n)，每个节点访问一次
	 * 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-digui-xian-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-digui-xian-xu-bian-li-by-xxx/
	 */
	public static List<Integer> recursivePreorder(TreeNode head) {
		List<Integer> result = new ArrayList<>();
		preorderHelper(head, result);
		return result;
	}

	private static void preorderHelper(TreeNode node, List<Integer> result) {
		if (node == null) {
			return;
		}
		// 先序：根-左-右
		result.add(node.val);
		preorderHelper(node.left, result);
		preorderHelper(node.right, result);
	}

	/**
	 * 使用迭代实现先序遍历
	 * 使用栈模拟递归过程
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/python-die-dai-xian-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-preorder-traversal/solution/c-die-dai-xian-xu-bian-li-by-xxx/
	 */
	public static List<Integer> iterativePreorder(TreeNode head) {
		List<Integer> result = new ArrayList<>();
		if (head == null) {
			return result;
		}
		Stack<TreeNode> stack = new Stack<>();
		stack.push(head);
		
		while (!stack.isEmpty()) {
			TreeNode node = stack.pop();
			result.add(node.val);
			// 注意：先压右子节点，再压左子节点，保证弹出顺序是先序
			if (node.right != null) {
				stack.push(node.right);
			}
			if (node.left != null) {
				stack.push(node.left);
			}
		}
		return result;
	}

	/**
	 * 使用递归实现中序遍历
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/python-digui-zhong-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/c-digui-zhong-xu-bian-li-by-xxx/
	 */
	public static List<Integer> recursiveInorder(TreeNode head) {
		List<Integer> result = new ArrayList<>();
		inorderHelper(head, result);
		return result;
	}

	private static void inorderHelper(TreeNode node, List<Integer> result) {
		if (node == null) {
			return;
		}
		// 中序：左-根-右
		inorderHelper(node.left, result);
		result.add(node.val);
		inorderHelper(node.right, result);
	}

	/**
	 * 使用迭代实现中序遍历
	 * 使用栈模拟递归过程
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/python-die-dai-zhong-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-inorder-traversal/solution/c-die-dai-zhong-xu-bian-li-by-xxx/
	 */
	public static List<Integer> iterativeInorder(TreeNode head) {
		List<Integer> result = new ArrayList<>();
		if (head == null) {
			return result;
		}
		Stack<TreeNode> stack = new Stack<>();
		TreeNode current = head;
		
		while (current != null || !stack.isEmpty()) {
			// 一直遍历到最左节点
			while (current != null) {
				stack.push(current);
				current = current.left;
			}
			// 弹出并访问
			current = stack.pop();
			result.add(current.val);
			// 处理右子树
			current = current.right;
		}
		return result;
	}

	/**
	 * 打印树结构，用于调试
	 * 中序遍历打印，展示树的结构
	 */
	public static void printTree(TreeNode root) {
		System.out.println("Tree structure (inorder):");
		printTreeHelper(root, 0);
		System.out.println();
	}

	private static void printTreeHelper(TreeNode node, int level) {
		if (node == null) {
			return;
		}
		printTreeHelper(node.right, level + 1);
		for (int i = 0; i < level; i++) {
			System.out.print("    ");
		}
		System.out.println(node.val);
		printTreeHelper(node.left, level + 1);
	}

	/**
	 * 创建测试树的辅助方法
	 * @return 测试用的二叉树
	 */
	public static TreeNode createTestTree(Code01_MorrisPreorderInorder obj) {
		// 创建示例树：
		//       1
		//      / \
		//     2   3
		//    / \ / \
		//   4  5 6  7
		TreeNode root = obj.new TreeNode(1);
		root.left = obj.new TreeNode(2);
		root.right = obj.new TreeNode(3);
		root.left.left = obj.new TreeNode(4);
		root.left.right = obj.new TreeNode(5);
		root.right.left = obj.new TreeNode(6);
		root.right.right = obj.new TreeNode(7);
		return root;
	}

	/**
	 * 创建左偏树（所有节点只有左子树）
	 * @return 左偏树
	 */
	public static TreeNode createLeftSkewedTree(Code01_MorrisPreorderInorder obj) {
		TreeNode root = obj.new TreeNode(1);
		TreeNode current = root;
		for (int i = 2; i <= 5; i++) {
			current.left = obj.new TreeNode(i);
			current = current.left;
		}
		return root;
	}

	/**
	 * 创建右偏树（所有节点只有右子树）
	 * @return 右偏树
	 */
	public static TreeNode createRightSkewedTree(Code01_MorrisPreorderInorder obj) {
		TreeNode root = obj.new TreeNode(1);
		TreeNode current = root;
		for (int i = 2; i <= 5; i++) {
			current.right = obj.new TreeNode(i);
			current = current.right;
		}
		return root;
	}

	/**
	 * 创建完全二叉树
	 * @return 完全二叉树
	 */
	public static TreeNode createCompleteBinaryTree(Code01_MorrisPreorderInorder obj) {
		// 创建完全二叉树：
		//        1
		//       / \
		//      2   3
		//     / \ /
		//    4  5 6
		TreeNode root = obj.new TreeNode(1);
		root.left = obj.new TreeNode(2);
		root.right = obj.new TreeNode(3);
		root.left.left = obj.new TreeNode(4);
		root.left.right = obj.new TreeNode(5);
		root.right.left = obj.new TreeNode(6);
		return root;
	}

	/**
	 * 性能测试方法
	 * 比较不同遍历方法的性能
	 */
	public static void performanceTest(Code01_MorrisPreorderInorder obj, TreeNode root, int iterations) {
		System.out.println("\n===== 性能测试 =====");
		System.out.println("迭代次数: " + iterations);
		
		// Morris先序遍历性能测试
		long startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			preorderTraversal(root);
		}
		long endTime = System.nanoTime();
		System.out.println("Morris先序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
		
		// 递归先序遍历性能测试
		startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			recursivePreorder(root);
		}
		endTime = System.nanoTime();
		System.out.println("递归先序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
		
		// 迭代先序遍历性能测试
		startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			iterativePreorder(root);
		}
		endTime = System.nanoTime();
		System.out.println("迭代先序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
		
		// Morris中序遍历性能测试
		startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			inorderTraversal(root);
		}
		endTime = System.nanoTime();
		System.out.println("Morris中序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
		
		// 递归中序遍历性能测试
		startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			recursiveInorder(root);
		}
		endTime = System.nanoTime();
		System.out.println("递归中序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
		
		// 迭代中序遍历性能测试
		startTime = System.nanoTime();
		for (int i = 0; i < iterations; i++) {
			iterativeInorder(root);
		}
		endTime = System.nanoTime();
		System.out.println("迭代中序遍历: " + (endTime - startTime) / 1_000_000 + " ms");
	}

	/**
	 * 验证遍历结果正确性
	 */
	public static void validateResults(List<Integer> morrisResult, List<Integer> recursiveResult, List<Integer> iterativeResult, String traversalType) {
		boolean allEqual = morrisResult.equals(recursiveResult) && recursiveResult.equals(iterativeResult);
		System.out.println(traversalType + " 结果验证: " + (allEqual ? "通过" : "失败"));
		if (!allEqual) {
			System.out.println("  Morris结果: " + morrisResult);
			System.out.println("  递归结果: " + recursiveResult);
			System.out.println("  迭代结果: " + iterativeResult);
		}
	}

	/**
	 * 主方法，用于测试各种遍历方式
	 */
	public static void main(String[] args) {
		Code01_MorrisPreorderInorder obj = new Code01_MorrisPreorderInorder();
		
		System.out.println("===== 1. 标准二叉树测试 =====");
		TreeNode standardTree = createTestTree(obj);
		printTree(standardTree);
		
		// 测试先序遍历
		System.out.println("Preorder Traversal:");
		List<Integer> morrisPreorder = preorderTraversal(standardTree);
		List<Integer> recursivePreorder = recursivePreorder(standardTree);
		List<Integer> iterativePreorder = iterativePreorder(standardTree);
		System.out.println("Morris: " + morrisPreorder);
		System.out.println("Recursive: " + recursivePreorder);
		System.out.println("Iterative: " + iterativePreorder);
		validateResults(morrisPreorder, recursivePreorder, iterativePreorder, "先序遍历");

		// 测试中序遍历
		System.out.println("\nInorder Traversal:");
		List<Integer> morrisInorder = inorderTraversal(standardTree);
		List<Integer> recursiveInorder = recursiveInorder(standardTree);
		List<Integer> iterativeInorder = iterativeInorder(standardTree);
		System.out.println("Morris: " + morrisInorder);
		System.out.println("Recursive: " + recursiveInorder);
		System.out.println("Iterative: " + iterativeInorder);
		validateResults(morrisInorder, recursiveInorder, iterativeInorder, "中序遍历");
		
		// 测试边界情况
		System.out.println("\n===== 2. 边界情况测试 =====");
		
		// 空树测试
		System.out.println("\nEmpty Tree Test:");
		System.out.println("Preorder: " + preorderTraversal(null));
		System.out.println("Inorder: " + inorderTraversal(null));

		// 单节点树测试
		System.out.println("\nSingle Node Tree Test:");
		TreeNode singleNode = obj.new TreeNode(42);
		System.out.println("Preorder: " + preorderTraversal(singleNode));
		System.out.println("Inorder: " + inorderTraversal(singleNode));
		
		// 特殊树结构测试
		System.out.println("\n===== 3. 特殊树结构测试 =====");
		
		// 右偏树（链表结构）测试
		System.out.println("\nRight Skewed Tree Test:");
		TreeNode rightSkewed = createRightSkewedTree(obj);
		printTree(rightSkewed);
		System.out.println("Preorder: " + preorderTraversal(rightSkewed));
		System.out.println("Inorder: " + inorderTraversal(rightSkewed));
		
		// 左偏树测试
		System.out.println("\nLeft Skewed Tree Test:");
		TreeNode leftSkewed = createLeftSkewedTree(obj);
		printTree(leftSkewed);
		System.out.println("Preorder: " + preorderTraversal(leftSkewed));
		System.out.println("Inorder: " + inorderTraversal(leftSkewed));
		
		// 完全二叉树测试
		System.out.println("\nComplete Binary Tree Test:");
		TreeNode completeTree = createCompleteBinaryTree(obj);
		printTree(completeTree);
		System.out.println("Preorder: " + preorderTraversal(completeTree));
		System.out.println("Inorder: " + inorderTraversal(completeTree));
		
		// 性能测试 - 创建一个较大的树
		System.out.println("\n===== 4. 大数据量测试 =====");
		// 创建一个深度为10的二叉树用于性能测试
		TreeNode largeTree = createLargeTree(obj, 10);
		System.out.println("Created large tree with depth 10");
		
		// 性能测试 - 执行10000次迭代
		performanceTest(obj, largeTree, 10000);
		
		// 算法总结
		System.out.println("\n===== 5. 算法总结 =====");
		System.out.println("Morris遍历的核心优势是空间复杂度O(1)，适合内存受限环境。");
		System.out.println("递归和迭代方法虽然空间复杂度为O(h)，但实现简单直观。");
		System.out.println("在实际应用中，应根据具体场景选择合适的遍历方法。");
	}

	/**
	 * 创建一个指定深度的二叉树，用于性能测试
	 */
	public static TreeNode createLargeTree(Code01_MorrisPreorderInorder obj, int depth) {
		return createLargeTreeHelper(obj, 1, depth);
	}

	private static TreeNode createLargeTreeHelper(Code01_MorrisPreorderInorder obj, int value, int depth) {
		if (depth <= 0) {
			return null;
		}
		TreeNode node = obj.new TreeNode(value);
		node.left = createLargeTreeHelper(obj, value * 2, depth - 1);
		node.right = createLargeTreeHelper(obj, value * 2 + 1, depth - 1);
		return node;
	}
}

/*
C++版本实现：

#include <iostream>
#include <vector>
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

// Morris先序遍历
vector<int> morrisPreorderTraversal(TreeNode* root) {
    vector<int> result;
    if (!root) return result;
    
    TreeNode* cur = root;
    TreeNode* mostRight = nullptr;
    
    while (cur != nullptr) {
        mostRight = cur->left;
        if (mostRight != nullptr) {
            // 找到左子树的最右节点
            while (mostRight->right != nullptr && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) { // 第一次访问
                result.push_back(cur->val);
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else { // 第二次访问
                mostRight->right = nullptr;
            }
        } else { // 没有左子树
            result.push_back(cur->val);
        }
        cur = cur->right;
    }
    
    return result;
}

// Morris中序遍历
vector<int> morrisInorderTraversal(TreeNode* root) {
    vector<int> result;
    if (!root) return result;
    
    TreeNode* cur = root;
    TreeNode* mostRight = nullptr;
    
    while (cur != nullptr) {
        mostRight = cur->left;
        if (mostRight != nullptr) {
            // 找到左子树的最右节点
            while (mostRight->right != nullptr && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) { // 第一次访问
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else { // 第二次访问
                mostRight->right = nullptr;
                result.push_back(cur->val);
            }
        } else { // 没有左子树
            result.push_back(cur->val);
        }
        cur = cur->right;
    }
    
    return result;
}

// 递归先序遍历
void recursivePreorderHelper(TreeNode* node, vector<int>& result) {
    if (!node) return;
    result.push_back(node->val);
    recursivePreorderHelper(node->left, result);
    recursivePreorderHelper(node->right, result);
}

vector<int> recursivePreorderTraversal(TreeNode* root) {
    vector<int> result;
    recursivePreorderHelper(root, result);
    return result;
}

// 递归中序遍历
void recursiveInorderHelper(TreeNode* node, vector<int>& result) {
    if (!node) return;
    recursiveInorderHelper(node->left, result);
    result.push_back(node->val);
    recursiveInorderHelper(node->right, result);
}

vector<int> recursiveInorderTraversal(TreeNode* root) {
    vector<int> result;
    recursiveInorderHelper(root, result);
    return result;
}

// 迭代先序遍历
vector<int> iterativePreorderTraversal(TreeNode* root) {
    vector<int> result;
    if (!root) return result;
    
    vector<TreeNode*> stack;
    stack.push_back(root);
    
    while (!stack.empty()) {
        TreeNode* node = stack.back();
        stack.pop_back();
        result.push_back(node->val);
        
        // 注意先压右子节点，再压左子节点
        if (node->right) stack.push_back(node->right);
        if (node->left) stack.push_back(node->left);
    }
    
    return result;
}

// 迭代中序遍历
vector<int> iterativeInorderTraversal(TreeNode* root) {
    vector<int> result;
    if (!root) return result;
    
    vector<TreeNode*> stack;
    TreeNode* current = root;
    
    while (current || !stack.empty()) {
        // 一直遍历到最左节点
        while (current) {
            stack.push_back(current);
            current = current->left;
        }
        
        current = stack.back();
        stack.pop_back();
        result.push_back(current->val);
        current = current->right;
    }
    
    return result;
}

// 主函数用于测试
int main() {
    // 创建示例树
    TreeNode* root = new TreeNode(1);
    root->left = new TreeNode(2);
    root->right = new TreeNode(3);
    root->left->left = new TreeNode(4);
    root->left->right = new TreeNode(5);
    root->right->left = new TreeNode(6);
    root->right->right = new TreeNode(7);
    
    cout << "Preorder Traversal:" << endl;
    vector<int> preorder = morrisPreorderTraversal(root);
    for (int val : preorder) cout << val << " ";
    cout << endl;
    
    cout << "Inorder Traversal:" << endl;
    vector<int> inorder = morrisInorderTraversal(root);
    for (int val : inorder) cout << val << " ";
    cout << endl;
    
    // 释放内存
    // 注意：实际应用中应该实现一个递归删除函数来释放树的所有节点
    // 这里简化处理
    
    return 0;
}

Python版本实现：

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# Morris先序遍历
def morris_preorder_traversal(root):
    result = []
    if not root:
        return result
    
    cur = root
    while cur:
        if cur.left:
            # 找到左子树的最右节点
            most_right = cur.left
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if not most_right.right:  # 第一次访问
                result.append(cur.val)
                most_right.right = cur
                cur = cur.left
                continue
            else:  # 第二次访问
                most_right.right = None
        else:  # 没有左子树
            result.append(cur.val)
        cur = cur.right
    
    return result

# Morris中序遍历
def morris_inorder_traversal(root):
    result = []
    if not root:
        return result
    
    cur = root
    while cur:
        if cur.left:
            # 找到左子树的最右节点
            most_right = cur.left
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if not most_right.right:  # 第一次访问
                most_right.right = cur
                cur = cur.left
                continue
            else:  # 第二次访问
                most_right.right = None
                result.append(cur.val)
        else:  # 没有左子树
            result.append(cur.val)
        cur = cur.right
    
    return result

# 递归先序遍历
def recursive_preorder_traversal(root):
    result = []
    
    def preorder_helper(node):
        if not node:
            return
        result.append(node.val)
        preorder_helper(node.left)
        preorder_helper(node.right)
    
    preorder_helper(root)
    return result

# 递归中序遍历
def recursive_inorder_traversal(root):
    result = []
    
    def inorder_helper(node):
        if not node:
            return
        inorder_helper(node.left)
        result.append(node.val)
        inorder_helper(node.right)
    
    inorder_helper(root)
    return result

# 迭代先序遍历
def iterative_preorder_traversal(root):
    result = []
    if not root:
        return result
    
    stack = [root]
    while stack:
        node = stack.pop()
        result.append(node.val)
        # 注意先压右子节点，再压左子节点
        if node.right:
            stack.append(node.right)
        if node.left:
            stack.append(node.left)
    
    return result

# 迭代中序遍历
def iterative_inorder_traversal(root):
    result = []
    if not root:
        return result
    
    stack = []
    current = root
    
    while current or stack:
        # 一直遍历到最左节点
        while current:
            stack.append(current)
            current = current.left
        
        current = stack.pop()
        result.append(current.val)
        current = current.right
    
    return result

# 打印树结构的辅助函数
def print_tree(root):
    def _print_tree(node, level):
        if not node:
            return
        _print_tree(node.right, level + 1)
        print('    ' * level + str(node.val))
        _print_tree(node.left, level + 1)
    
    print("Tree structure (inorder):")
    _print_tree(root, 0)
    print()

# 测试函数
if __name__ == "__main__":
    # 创建示例树
    #       1
    #      / \
    #     2   3
    #    / \ / \
    #   4  5 6  7
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.left.left = TreeNode(4)
    root.left.right = TreeNode(5)
    root.right.left = TreeNode(6)
    root.right.right = TreeNode(7)
    
    # 打印树结构
    print_tree(root)
    
    # 测试先序遍历
    print("Preorder Traversal:")
    print("Morris: ", morris_preorder_traversal(root))
    print("Recursive: ", recursive_preorder_traversal(root))
    print("Iterative: ", iterative_preorder_traversal(root))
    
    # 测试中序遍历
    print("\nInorder Traversal:")
    print("Morris: ", morris_inorder_traversal(root))
    print("Recursive: ", recursive_inorder_traversal(root))
    print("Iterative: ", iterative_inorder_traversal(root))
    
    # 测试边界情况
    print("\nEmpty Tree Test:")
    print("Preorder: ", morris_preorder_traversal(None))
    print("Inorder: ", morris_inorder_traversal(None))
    
    print("\nSingle Node Tree Test:")
    single_node = TreeNode(42)
    print("Preorder: ", morris_preorder_traversal(single_node))
    print("Inorder: ", morris_inorder_traversal(single_node))

# 算法深度解析与工程实践

## 1. 复杂度分析

| 遍历方法 | 时间复杂度 | 空间复杂度 | 优点 | 缺点 |
|---------|-----------|-----------|------|------|
| Morris遍历 | O(n) | O(1) | 常数空间，不依赖栈 | 实现复杂，修改树结构 |
| 递归遍历 | O(n) | O(h) | 实现简单，代码优雅 | 可能栈溢出，额外空间 |
| 迭代遍历 | O(n) | O(h) | 避免递归栈溢出 | 实现稍复杂，额外空间 |

* h为树高，平衡树时h=log(n)，最坏情况（链表）h=n

## 2. Morris遍历的核心原理深度解析

Morris遍历算法的核心思想是**利用二叉树中大量空闲的空指针来存储遍历所需的路径信息**，从而避免使用栈。具体来说：

1. **线索化**：对于每个有左子树的节点，将其左子树的最右节点的右指针指向该节点本身，形成一个临时的线索
2. **两次访问**：
   - 第一次访问节点时建立线索
   - 第二次访问节点时删除线索并处理右子树
3. **还原树结构**：每次访问完节点后，都会恢复树的原始结构，不影响后续操作

Morris遍历的巧妙之处在于它只使用了两个指针（cur和mostRight），在O(n)时间内完成遍历，同时保持O(1)的空间复杂度。

## 3. 不同遍历顺序的Morris实现对比

### 先序遍历 vs 中序遍历

**关键区别**：收集节点值的时机不同
- **先序遍历**：在第一次访问节点时收集值（根-左-右）
- **中序遍历**：在第二次访问节点时收集值（左-根-右）

这一区别直接体现了两种遍历顺序的本质差异。

## 4. 工程实践建议

### 选择合适的遍历方法

1. **内存敏感场景**：优先选择Morris遍历
   - 嵌入式系统
   - 内存受限的服务器环境
   - 处理超大二叉树

2. **一般应用场景**：优先选择递归或迭代方法
   - 实现简单，易于维护
   - 代码可读性好
   - 多线程环境更安全

3. **特殊情况考虑**：
   - 对于极深的树，递归可能导致栈溢出，此时应选择迭代方法
   - 对于频繁调用的场景，递归的函数调用开销可能较大

### 代码优化建议

1. **Morris遍历优化**：
   - 确保在任何情况下都恢复树结构
   - 添加异常处理，防止树结构被意外修改

2. **递归优化**：
   - 对于特别深的树，考虑使用尾递归优化（如支持的语言）
   - 可考虑手动限制递归深度

3. **迭代优化**：
   - 使用Deque代替Stack类，性能更好
   - 考虑使用更高效的数据结构减少操作开销

## 5. 常见陷阱与注意事项

1. **死循环风险**：在Morris遍历中，如果mostRight指针判断条件不完整，可能导致死循环
2. **树结构破坏**：如果在遍历过程中抛出异常，可能导致树结构未被正确还原
3. **线程安全问题**：Morris遍历修改树结构，不适合并发环境
4. **性能误区**：虽然Morris遍历空间复杂度最优，但常数因子较大，在某些情况下实际性能可能不如递归或迭代方法

## 6. 总结

Morris遍历是一种优雅而高效的算法设计，展示了如何通过深入理解数据结构特性来优化算法性能。虽然在一般应用中可能不常使用，但其设计思想和空间优化策略值得学习和借鉴。

在实际工作中，应根据具体需求、数据规模和运行环境，选择最合适的遍历方法，平衡实现复杂度、运行性能和内存使用。
*/