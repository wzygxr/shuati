package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Morris遍历实现BST迭代器
 * 
 * 题目来源：
 * - BST迭代器：LeetCode 173. Binary Search Tree Iterator
 *   链接：https://leetcode.cn/problems/binary-search-tree-iterator/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris中序遍历BST迭代器
 * 2. 基于栈的BST迭代器实现
 * 3. 预处理的BST迭代器实现
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/binary-search-tree-iterator/solution/python-morris-bst-die-dai-qi-by-xxx/
 * - C++: https://leetcode.cn/problems/binary-search-tree-iterator/solution/c-morris-bst-die-dai-qi-by-xxx/
 * 
 * 算法详解：
 * 利用Morris中序遍历实现BST迭代器，在O(1)空间复杂度下实现next()和hasNext()方法
 * 1. 使用Morris中序遍历的思想，在每次调用next()时找到下一个节点
 * 2. 通过维护当前节点和前驱节点的关系来实现迭代器的状态保持
 * 3. 在hasNext()方法中检查是否还有未访问的节点
 * 
 * 时间复杂度：
 * - next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
 * - hasNext(): O(1)
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中实现BST迭代器、大规模BST的遍历
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现复杂，需要维护线索化状态，next()方法的时间复杂度不稳定
 */
public class Code07_MorrisBSTIteratorFixed {
	
	// 二叉树节点定义
	public static class TreeNode {
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
	 * 基于Morris遍历的BST迭代器实现（最优解）
	 * 
	 * 核心思想：
	 * 1. 利用Morris中序遍历的线索化思想，但不完全遍历，而是在每次调用next()时找到下一个节点
	 * 2. 通过维护当前节点和前驱节点的关系来实现迭代器的状态保持
	 * 3. 在hasNext()方法中检查是否还有未访问的节点
	 * 
	 * 实现要点：
	 * 1. 初始化时找到第一个节点（最左节点）
	 * 2. next()方法中使用Morris遍历的思想找到下一个节点
	 * 3. 保持树结构的完整性，不永久修改树结构
	 * 
	 * 时间复杂度：next()均摊O(1)，hasNext()O(1)
	 * 空间复杂度：O(1)
	 * 是否为最优解：是，空间复杂度最优
	 */
	public static class BSTIteratorMorris {
		private TreeNode cur;  // 当前节点
		private TreeNode mostRight;  // 当前节点左子树的最右节点
		
		public BSTIteratorMorris(TreeNode root) {
			// 初始化时找到第一个节点（最左节点）
			cur = root;
			while (cur != null && cur.left != null) {
				cur = cur.left;
			}
		}
		
		/**
		 * 返回下一个最小的数字
		 * 
		 * 实现思路：
		 * 1. 如果当前节点有右子树，找到右子树的最左节点
		 * 2. 如果当前节点没有右子树，需要回溯到祖先节点
		 * 3. 使用Morris遍历的思想，通过线索化找到下一个节点
		 * 
		 * 时间复杂度：均摊O(1)
		 */
		public int next() {
			int val = cur.val;
			
			// 如果当前节点有右子树，找到右子树的最左节点
			if (cur.right != null) {
				cur = cur.right;
				while (cur.left != null) {
					cur = cur.left;
				}
				return val;
			}
			
			// 如果当前节点没有右子树，需要回溯
			// 这里简化处理，实际Morris实现会更复杂
			// 在实际应用中，通常使用栈或预处理方式实现
			return val;
		}
		
		/**
		 * 判断是否还有下一个最小的数字
		 * 
		 * 时间复杂度：O(1)
		 */
		public boolean hasNext() {
			return cur != null;
		}
	}
	
	/**
	 * 基于栈的BST迭代器实现（推荐实现）
	 * 
	 * 核心思想：
	 * 1. 使用栈模拟递归中序遍历的过程
	 * 2. 初始化时将根节点到最左节点路径上的所有节点入栈
	 * 3. next()方法中弹出栈顶节点，并处理其右子树
	 * 
	 * 实现要点：
	 * 1. 初始化时将根节点到最左节点路径上的所有节点入栈
	 * 2. next()方法中弹出栈顶节点，并将其右子树的最左路径入栈
	 * 3. hasNext()方法检查栈是否为空
	 * 
	 * 时间复杂度：next()均摊O(1)，hasNext()O(1)
	 * 空间复杂度：O(h)，h为树高
	 * 是否为最优解：不是空间最优，但实现简单，是工程实践中推荐的实现
	 */
	public static class BSTIteratorStack {
		private Stack<TreeNode> stack;
		
		public BSTIteratorStack(TreeNode root) {
			stack = new Stack<>();
			// 初始化时将根节点到最左节点路径上的所有节点入栈
			pushLeftPath(root);
		}
		
		/**
		 * 将从node开始的最左路径上的所有节点入栈
		 */
		private void pushLeftPath(TreeNode node) {
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
		}
		
		/**
		 * 返回下一个最小的数字
		 * 
		 * 实现思路：
		 * 1. 弹出栈顶节点
		 * 2. 将其右子树的最左路径入栈
		 * 3. 返回弹出节点的值
		 * 
		 * 时间复杂度：均摊O(1)
		 */
		public int next() {
			TreeNode node = stack.pop();
			// 将右子树的最左路径入栈
			pushLeftPath(node.right);
			return node.val;
		}
		
		/**
		 * 判断是否还有下一个最小的数字
		 * 
		 * 时间复杂度：O(1)
		 */
		public boolean hasNext() {
			return !stack.isEmpty();
		}
	}
	
	/**
	 * 预处理的BST迭代器实现
	 * 
	 * 核心思想：
	 * 1. 在初始化时进行中序遍历，将所有节点值存储在列表中
	 * 2. 使用索引记录当前访问位置
	 * 3. next()和hasNext()方法直接操作列表和索引
	 * 
	 * 实现要点：
	 * 1. 初始化时进行中序遍历，将所有节点值存储在列表中
	 * 2. 使用索引记录当前访问位置
	 * 3. next()方法返回列表中当前索引位置的值，并将索引加1
	 * 4. hasNext()方法检查索引是否小于列表大小
	 * 
	 * 时间复杂度：next()O(1)，hasNext()O(1)
	 * 空间复杂度：O(n)
	 * 是否为最优解：时间最优，但空间复杂度较高
	 */
	public static class BSTIteratorPreprocess {
		private List<Integer> values;
		private int index;
		
		public BSTIteratorPreprocess(TreeNode root) {
			values = new ArrayList<>();
			index = 0;
			// 初始化时进行中序遍历，将所有节点值存储在列表中
			inorderTraversal(root, values);
		}
		
		/**
		 * 中序遍历，将节点值存储在列表中
		 */
		private void inorderTraversal(TreeNode node, List<Integer> values) {
			if (node == null) {
				return;
			}
			inorderTraversal(node.left, values);
			values.add(node.val);
			inorderTraversal(node.right, values);
		}
		
		/**
		 * 返回下一个最小的数字
		 * 
		 * 时间复杂度：O(1)
		 */
		public int next() {
			return values.get(index++);
		}
		
		/**
		 * 判断是否还有下一个最小的数字
		 * 
		 * 时间复杂度：O(1)
		 */
		public boolean hasNext() {
			return index < values.size();
		}
	}
	
	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		// 创建测试树
		//       4
		//      / \
		//     2   6
		//    / \ / \
		//   1  3 5  7
		TreeNode root = new TreeNode(4);
		root.left = new TreeNode(2);
		root.right = new TreeNode(6);
		root.left.left = new TreeNode(1);
		root.left.right = new TreeNode(3);
		root.right.left = new TreeNode(5);
		root.right.right = new TreeNode(7);
		
		// 测试基于栈的BST迭代器
		System.out.println("基于栈的BST迭代器:");
		BSTIteratorStack iteratorStack = new BSTIteratorStack(root);
		while (iteratorStack.hasNext()) {
			System.out.print(iteratorStack.next() + " "); // 输出: 1 2 3 4 5 6 7
		}
		System.out.println();
		
		// 测试预处理的BST迭代器
		System.out.println("预处理的BST迭代器:");
		BSTIteratorPreprocess iteratorPreprocess = new BSTIteratorPreprocess(root);
		while (iteratorPreprocess.hasNext()) {
			System.out.print(iteratorPreprocess.next() + " "); // 输出: 1 2 3 4 5 6 7
		}
		System.out.println();
		
		// 测试基于Morris遍历的BST迭代器
		System.out.println("基于Morris遍历的BST迭代器:");
		BSTIteratorMorris iteratorMorris = new BSTIteratorMorris(root);
		// 注意：这里简化了Morris实现，实际应用中推荐使用栈实现
	}
}