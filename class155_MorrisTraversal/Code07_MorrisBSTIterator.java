package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

// Morris遍历实现BST迭代器
// 
// 题目来源：
// - BST迭代器：LeetCode 173. Binary Search Tree Iterator
//   链接：https://leetcode.cn/problems/binary-search-tree-iterator/
// 
// Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
// 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
// 
// 本实现包含：
// 1. Java语言的Morris中序遍历BST迭代器
// 2. 基于栈的BST迭代器实现
// 3. 预处理的BST迭代器实现
// 4. 详细的注释和算法解析
// 5. 完整的测试用例
// 6. C++和Python语言的完整实现
// 
// 三种语言实现链接：
// - Java: 当前文件
// - Python: https://leetcode.cn/problems/binary-search-tree-iterator/solution/python-morris-bst-die-dai-qi-by-xxx/
// - C++: https://leetcode.cn/problems/binary-search-tree-iterator/solution/c-morris-bst-die-dai-qi-by-xxx/
// 
// 算法详解：
// 利用Morris中序遍历实现BST迭代器，在O(1)空间复杂度下实现next()和hasNext()方法
// 1. 使用Morris中序遍历的思想，在每次调用next()时找到下一个节点
// 2. 通过维护当前节点和前驱节点的关系来实现迭代器的状态保持
// 3. 在hasNext()方法中检查是否还有未访问的节点
// 
// 时间复杂度：
// - next(): 均摊O(1) - 虽然单次调用可能需要O(n)时间，但n次调用的总时间复杂度为O(n)
// - hasNext(): O(1)
// 空间复杂度：O(1) - 不使用额外空间
// 适用场景：内存受限环境中实现BST迭代器、大规模BST的遍历
// 优缺点分析：
// - 优点：空间复杂度最优，适合内存受限环境
// - 缺点：实现复杂，需要维护线索化状态，next()方法的时间复杂度不稳定
*/
public class Code07_MorrisBSTIterator {

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

	/**
	 * 使用Morris遍历实现BST迭代器
	 * 
	 * 题目描述：
	 * 实现一个二叉搜索树迭代器类BSTIterator，表示一个按中序遍历二叉搜索树（BST）的迭代器：
	 * - BSTIterator(TreeNode root)：初始化BSTIterator类的一个对象。BST的根节点root会作为构造函数的一部分给出。
	 * - boolean hasNext()：如果向指针右侧遍历存在数字，则返回true；否则返回false。
	 * - int next()：将指针向右移动，然后返回指针处的数字。
	 * 
	 * 注意：指针初始化为一个不存在于BST中的数字，所以对next()的首次调用将返回BST中的最小元素。
	 * 
	 * 解题思路：
	 * 1. 使用Morris中序遍历的思想，但需要支持暂停和恢复
	 * 2. 维护当前节点和前驱节点的引用，以便在调用next时继续遍历
	 * 3. 在hasNext方法中预先判断是否还有下一个节点
	 * 
	 * 时间复杂度：
	 * - next(): 均摊O(1) - 虽然单次操作可能需要O(n)，但n个节点的总操作时间是O(n)，因此均摊为O(1)
	 * - hasNext(): O(1) - 仅需检查当前节点是否为空
	 * - 总体: O(n)遍历n个节点
	 * 
	 * 空间复杂度：O(1) - 仅使用常数额外空间，不需要栈或递归调用栈
	 * 是否为最优解：是，相比传统方法节省了空间
	 */

	public static class BSTIteratorMorris {
		private TreeNode cur;        // 当前节点
		private TreeNode mostRight;  // 最右节点（前驱节点）

		/**
		 * 构造函数，初始化迭代器
		 * @param root 二叉搜索树的根节点
		 */
		public BSTIteratorMorris(TreeNode root) {
			cur = root;
			mostRight = null;
		}

		/**
		 * 返回下一个最小的数
		 * 
		 * 算法思路：
		 * 1. 执行Morris中序遍历的核心逻辑
		 * 2. 当需要返回节点值时暂停并返回
		 * 3. 保持状态以便下次调用时继续
		 * 
		 * @return 下一个中序遍历的节点值
		 * @throws NoSuchElementException 当没有更多元素时抛出
		 */
		public int next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements available in BST");
			}

			int val = 0;
			boolean found = false;

			// 执行Morris遍历直到找到要返回的节点
			while (cur != null && !found) {
				mostRight = cur.left;
				if (mostRight != null) {
					// 找到cur左子树的最右节点（即cur的前驱节点）
					while (mostRight.right != null && mostRight.right != cur) {
						mostRight = mostRight.right;
					}

					if (mostRight.right == null) {
						// 第一次到达，建立线索（前驱节点指向当前节点）
						mostRight.right = cur;
						cur = cur.left;
						continue;
					} else {
						// 第二次到达，断开线索并访问节点
						mostRight.right = null;
						val = cur.val;
						found = true;
					}
				} else {
					// 没有左子树，直接访问节点
					val = cur.val;
					found = true;
				}
				cur = cur.right;
			}

			return val;
		}

		/**
		 * 判断是否还有下一个节点
		 * 
		 * 算法思路：
		 * 1. 检查当前节点是否为空
		 * 2. 如果不为空，则还有节点可以遍历
		 * 
		 * @return 如果还有下一个节点返回true，否则返回false
		 */
		public boolean hasNext() {
			return cur != null;
		}
	}

	/**
	 * 使用栈的迭代方法实现BST迭代器
	 * 这是传统解法，空间复杂度为O(h)，其中h是树的高度
	 */
	public static class BSTIteratorStack {
		private Stack<TreeNode> stack;
		private TreeNode cur;

		/**
		 * 构造函数，初始化迭代器
		 * @param root 二叉搜索树的根节点
		 */
		public BSTIteratorStack(TreeNode root) {
			stack = new Stack<>();
			cur = root;
			// 初始化时将根节点及其所有左子节点入栈
			pushLeftBranch(cur);
		}

		/**
		 * 将当前节点及其所有左子节点入栈
		 * @param node 当前节点
		 */
		private void pushLeftBranch(TreeNode node) {
			while (node != null) {
				stack.push(node);
				node = node.left;
			}
		}

		/**
		 * 返回下一个最小的数
		 * @return 下一个中序遍历的节点值
		 */
		public int next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements available");
			}
			TreeNode node = stack.pop();
			// 处理右子树
			if (node.right != null) {
				pushLeftBranch(node.right);
			}
			return node.val;
		}

		/**
		 * 判断是否还有下一个节点
		 * @return 如果还有下一个节点返回true，否则返回false
		 */
		public boolean hasNext() {
			return !stack.isEmpty();
		}
	}

	/**
	 * 使用预处理数组的方法实现BST迭代器
	 * 预先存储所有中序遍历的结果
	 */
	public static class BSTIteratorPreprocess {
		private List<Integer> values;
		private int index;

		/**
		 * 构造函数，初始化迭代器
		 * @param root 二叉搜索树的根节点
		 */
		public BSTIteratorPreprocess(TreeNode root) {
			values = new ArrayList<>();
			index = 0;
			// 预先进行中序遍历并存储结果
			inorderTraversal(root);
		}

		/**
		 * 递归中序遍历
		 * @param node 当前节点
		 */
		private void inorderTraversal(TreeNode node) {
			if (node == null) {
				return;
			}
			inorderTraversal(node.left);
			values.add(node.val);
			inorderTraversal(node.right);
		}

		/**
		 * 返回下一个最小的数
		 * @return 下一个中序遍历的节点值
		 */
		public int next() {
			if (!hasNext()) {
				throw new NoSuchElementException("No more elements available");
			}
			return values.get(index++);
		}

		/**
		 * 判断是否还有下一个节点
		 * @return 如果还有下一个节点返回true，否则返回false
		 */
		public boolean hasNext() {
			return index < values.size();
		}
	}

	/**
	 * 辅助方法：打印树的中序遍历结果
	 * 用于调试和验证迭代器的正确性
	 * @param root 树的根节点
	 * @return 中序遍历的结果字符串
	 */
	public static String printInorder(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		inorderHelper(root, result);
		return result.toString();
	}

	/**
	 * 递归中序遍历辅助方法
	 * @param node 当前节点
	 * @param result 存储结果的列表
	 */
	private static void inorderHelper(TreeNode node, List<Integer> result) {
		if (node == null) return;
		inorderHelper(node.left, result);
		result.add(node.val);
		inorderHelper(node.right, result);
	}

	/**
	 * 测试方法
	 * 测试各种情况下迭代器的正确性
	 */
	public static void main(String[] args) {
		// 测试用例1: 标准二叉搜索树
		//     4
		//    / \
		//   2   6
		//  / \ / \
		// 1  3 5  7
		TreeNode root1 = new TreeNode(4);
		root1.left = new TreeNode(2);
		root1.right = new TreeNode(6);
		root1.left.left = new TreeNode(1);
		root1.left.right = new TreeNode(3);
		root1.right.left = new TreeNode(5);
		root1.right.right = new TreeNode(7);

		// 测试Morris迭代器
		System.out.println("=== 测试Morris迭代器 ===");
		BSTIteratorMorris iterator1 = new BSTIteratorMorris(root1);
		List<Integer> result1 = new ArrayList<>();
		while (iterator1.hasNext()) {
			result1.add(iterator1.next());
		}
		System.out.println("Morris迭代器结果: " + result1);
		System.out.println("期望中序结果: " + printInorder(root1));

		// 测试用例2: 空树
		TreeNode root2 = null;
		System.out.println("\n=== 测试空树 ===");
		BSTIteratorMorris iterator2 = new BSTIteratorMorris(root2);
		System.out.println("空树hasNext(): " + iterator2.hasNext());

		// 测试用例3: 单节点树
		TreeNode root3 = new TreeNode(1);
		System.out.println("\n=== 测试单节点树 ===");
		BSTIteratorMorris iterator3 = new BSTIteratorMorris(root3);
		System.out.println("单节点树第一个next(): " + iterator3.next());
		System.out.println("next()后hasNext(): " + iterator3.hasNext());

		// 测试用例4: 只有左子树的树
		//    4
		//   /
		//  2
		// /
		// 1
		TreeNode root4 = new TreeNode(4);
		root4.left = new TreeNode(2);
		root4.left.left = new TreeNode(1);
		System.out.println("\n=== 测试只有左子树的树 ===");
		BSTIteratorMorris iterator4 = new BSTIteratorMorris(root4);
		List<Integer> result4 = new ArrayList<>();
		while (iterator4.hasNext()) {
			result4.add(iterator4.next());
		}
		System.out.println("只有左子树的树结果: " + result4);

		// 测试用例5: 只有右子树的树
		// 1
		//  \n		//   2
		//    \
		//     3
		TreeNode root5 = new TreeNode(1);
		root5.right = new TreeNode(2);
		root5.right.right = new TreeNode(3);
		System.out.println("\n=== 测试只有右子树的树 ===");
		BSTIteratorMorris iterator5 = new BSTIteratorMorris(root5);
		List<Integer> result5 = new ArrayList<>();
		while (iterator5.hasNext()) {
			result5.add(iterator5.next());
		}
		System.out.println("只有右子树的树结果: " + result5);

		// 对比不同实现的性能（对于大的测试用例）
		System.out.println("\n=== 对比不同实现 ===");
		TreeNode largeTree = createLargeBST(1000);
		
		// 测试Morris迭代器性能
		long start = System.currentTimeMillis();
		BSTIteratorMorris morrisIter = new BSTIteratorMorris(largeTree);
		while (morrisIter.hasNext()) {
			morrisIter.next();
		}
		long morrisTime = System.currentTimeMillis() - start;
		System.out.println("Morris迭代器遍历1000节点耗时: " + morrisTime + "ms");

		// 测试栈迭代器性能
		start = System.currentTimeMillis();
		BSTIteratorStack stackIter = new BSTIteratorStack(largeTree);
		while (stackIter.hasNext()) {
			stackIter.next();
		}
		long stackTime = System.currentTimeMillis() - start;
		System.out.println("栈迭代器遍历1000节点耗时: " + stackTime + "ms");

		// 测试预处理迭代器性能
		start = System.currentTimeMillis();
		BSTIteratorPreprocess preprocessIter = new BSTIteratorPreprocess(largeTree);
		while (preprocessIter.hasNext()) {
			preprocessIter.next();
		}
		long preprocessTime = System.currentTimeMillis() - start;
		System.out.println("预处理迭代器遍历1000节点耗时: " + preprocessTime + "ms");
	}

	/**
	 * 创建一个大型的平衡二叉搜索树用于性能测试
	 * @param size 节点数量
	 * @return 平衡BST的根节点
	 */
	public static TreeNode createLargeBST(int size) {
		return createBST(1, size);
	}

	/**
	 * 递归创建平衡BST
	 * @param start 起始值
	 * @param end 结束值
	 * @return 平衡BST的根节点
	 */
	private static TreeNode createBST(int start, int end) {
		if (start > end) return null;
		int mid = start + (end - start) / 2;
		TreeNode root = new TreeNode(mid);
		root.left = createBST(start, mid - 1);
		root.right = createBST(mid + 1, end);
		return root;
	}

	/**
	 * 完整的Python实现
	 * 
	 * # Definition for a binary tree node
	 * class TreeNode:
	 *     def __init__(self, val=0, left=None, right=None):
	 *         self.val = val
	 *         self.left = left
	 *         self.right = right
	 * 
	 * class BSTIterator:
	 *     """
	 *     使用Morris中序遍历实现的二叉搜索树迭代器
	 *     时间复杂度: next()均摊O(1)，hasNext()O(1)
	 *     空间复杂度: O(1)
	 *     """
	 *     
	 *     def __init__(self, root):
		 *         """
		 *         初始化迭代器
		 *         
		 *         Args:
		 *             root: 二叉搜索树的根节点
		 *         """
	 *         self.cur = root
	 *         self.mostRight = None
	 *     
	 *     def next(self):
		 *         """
		 *         返回二叉搜索树中的下一个最小的数
		 *         
		 *         Returns:
		 *             int: 下一个中序遍历的节点值
		 *             
		 *         Raises:
		 *             StopIteration: 当没有更多元素时抛出
		 *         """
	 *         if not self.hasNext():
	 *             raise StopIteration("No more elements available in BST")
	 *         
	 *         val = 0
	 *         found = False
	 *         
	 *         # 执行Morris遍历直到找到要返回的节点
	 *         while self.cur and not found:
	 *             self.mostRight = self.cur.left
	 *             if self.mostRight:
	 *                 # 找到当前节点左子树的最右节点
	 *                 while self.mostRight.right and self.mostRight.right != self.cur:
	 *                     self.mostRight = self.mostRight.right
	 *                 
	 *                 if not self.mostRight.right:
	 *                     # 第一次到达，建立线索
	 *                     self.mostRight.right = self.cur
	 *                     self.cur = self.cur.left
	 *                     continue
	 *                 else:
	 *                     # 第二次到达，断开线索并访问节点
	 *                     self.mostRight.right = None
	 *                     val = self.cur.val
	 *                     found = True
	 *             else:
	 *                 # 没有左子树，直接访问节点
	 *                 val = self.cur.val
	 *                 found = True
	 *                 
	 *             self.cur = self.cur.right
	 *         
	 *         return val
	 *     
	 *     def hasNext(self):
		 *         """
		 *         判断是否还有下一个节点
		 *         
		 *         Returns:
		 *             bool: 如果还有下一个节点返回True，否则返回False
		 *         """
	 *         return self.cur is not None
	 * 
	 * # 示例用法
	 * # root = TreeNode(4)
	 * # root.left = TreeNode(2)
	 * # root.right = TreeNode(6)
	 * # root.left.left = TreeNode(1)
	 * # root.left.right = TreeNode(3)
	 * # root.right.left = TreeNode(5)
	 * # root.right.right = TreeNode(7)
	 * # 
	 * # iterator = BSTIterator(root)
	 * # while iterator.hasNext():
	 * #     print(iterator.next())  # 输出: 1 2 3 4 5 6 7
	 */

	/*
	 * 完整的C++实现
	 * 
	 * C++实现代码（注释形式）：
	 * // Definition for a binary tree node
	 * struct TreeNode {
	 *     int val;
	 *     TreeNode *left;
	 *     TreeNode *right;
	 *     TreeNode() : val(0), left(nullptr), right(nullptr) {}
	 *     TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
	 *     TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
	 * };
	 * 
	 * class BSTIterator {
	 * private:
	 *     TreeNode* cur;        // 当前节点
	 *     TreeNode* mostRight;  // 最右节点（前驱节点）
	 *     
	 * public:
	 *     // 初始化迭代器
	 *     // @param root 二叉搜索树的根节点
	 *     BSTIterator(TreeNode* root) : cur(root), mostRight(nullptr) {}
	 *     
	 *     // 返回下一个最小的数
	 *     // @return 下一个中序遍历的节点值
	 *     // @throws std::out_of_range 当没有更多元素时抛出
	 *     int next() {
	 *         if (!hasNext()) {
	 *             throw std::out_of_range("No more elements available in BST");
	 *         }
	 *         
	 *         int val = 0;
	 *         bool found = false;
	 *         
	 *         // 执行Morris遍历直到找到要返回的节点
	 *         while (cur && !found) {
	 *             mostRight = cur->left;
	 *             if (mostRight) {
	 *                 // 找到当前节点左子树的最右节点
	 *                 while (mostRight->right && mostRight->right != cur) {
	 *                     mostRight = mostRight->right;
	 *                 }
	 *                 
	 *                 if (!mostRight->right) {
	 *                     // 第一次到达，建立线索
	 *                     mostRight->right = cur;
	 *                     cur = cur->left;
	 *                     continue;
	 *                 } else {
	 *                     // 第二次到达，断开线索并访问节点
	 *                     mostRight->right = nullptr;
	 *                     val = cur->val;
	 *                     found = true;
	 *                 }
	 *             } else {
	 *                 // 没有左子树，直接访问节点
	 *                 val = cur->val;
	 *                 found = true;
	 *             }
	 *             cur = cur->right;
	 *         }
	 *         
	 *         return val;
	 *     }
	 *     
	 *     // 判断是否还有下一个节点
	 *     // @return 如果还有下一个节点返回true，否则返回false
	 *     bool hasNext() {
	 *         return cur != nullptr;
	 *     }
	 * };
	 * 
	 * // 示例用法
	 * // TreeNode* root = new TreeNode(4);
	 * // root->left = new TreeNode(2);
	 * // root->right = new TreeNode(6);
	 * // root->left->left = new TreeNode(1);
	 * // root->left->right = new TreeNode(3);
	 * // root->right->left = new TreeNode(5);
	 * // root->right->right = new TreeNode(7);
	 * // 
	 * // BSTIterator* iterator = new BSTIterator(root);
	 * // while (iterator->hasNext()) {
	 * //     cout << iterator->next() << " "; // 输出: 1 2 3 4 5 6 7
	 * // }
	 * // delete iterator;
	 * // // 注意: 实际应用中需要释放树节点内存
	 */

	/**
	 * 算法核心思想与深度解析：
	 * 1. Morris遍历的本质是利用二叉树中的空闲指针来实现O(1)空间的遍历
	 * 2. 关键技术点是找到当前节点的前驱节点，并建立临时连接以便回溯
	 * 3. 在迭代器实现中，需要在每次调用next()时恢复之前的遍历状态
	 * 
	 * 复杂度分析：
	 * - 时间复杂度：
	 *   * Morris迭代器：next()均摊O(1)，因为虽然单次操作可能需要O(n)，但n个节点的总操作时间是O(n)
	 *   * 栈迭代器：next()最坏O(h)，h为树高，平均O(1)
	 *   * 预处理迭代器：next()O(1)，但初始化需要O(n)时间
	 * 
	 * - 空间复杂度：
	 *   * Morris迭代器：O(1)，仅使用常数额外空间
	 *   * 栈迭代器：O(h)，h为树高，最坏O(n)
	 *   * 预处理迭代器：O(n)，需要存储所有节点值
	 * 
	 * 调试技巧：
	 * 1. 打印中间状态：在Morris遍历过程中打印cur和mostRight的值，可以观察遍历流程
	 * 2. 手动模拟：对于复杂树，手动模拟Morris遍历步骤，验证线索建立和断开的正确性
	 * 3. 边界测试：测试空树、单节点树、偏斜树等特殊情况
	 * 
	 * 工程化考量：
	 * 1. 异常处理：在没有更多元素时抛出适当的异常
	 * 2. 资源管理：C++实现中注意内存泄漏问题
	 * 3. 线程安全：当前实现不是线程安全的，如需线程安全需要加锁
	 * 4. 性能优化：对于大规模数据，Morris迭代器在空间效率上有明显优势
	 * 5. 接口设计：遵循迭代器设计模式，提供直观易用的接口
	 * 
	 * 扩展应用：
	 * 1. 反向迭代器：可以通过Morris反向中序遍历实现（右-根-左）
	 * 2. 前序迭代器：修改Morris遍历中访问节点的时机即可实现
	 * 3. 迭代器适配器：可以基于此实现更多高级功能，如范围查询等
	 * 
	 * 与机器学习的联系：
	 * 在处理树结构数据（如决策树、树状图卷积网络）时，Morris遍历可以高效地遍历树节点而不消耗大量内存，
	 * 对于处理大规模树结构数据集有重要意义。
	 */
}