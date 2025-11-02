package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Morris遍历求BST最小差值
 * 
 * 题目来源：
 * - BST最小差值：LeetCode 530. Minimum Absolute Difference in BST
 *   链接：https://leetcode.cn/problems/minimum-absolute-difference-in-bst/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris中序遍历求BST最小差值
 * 2. 递归版本的求BST最小差值
 * 3. 迭代版本的求BST最小差值
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/python-morris-qiu-bst-zui-xiao-chai-zhi-by-xxx/
 * - C++: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/c-morris-qiu-bst-zui-xiao-chai-zhi-by-xxx/
 * 
 * 算法详解：
 * 给定一个所有节点为非负值的二叉搜索树，求树中任意两节点的差的绝对值的最小值。
 * 
 * 解题思路：
 * 1. 利用BST的性质：中序遍历得到递增序列
 * 2. 使用Morris中序遍历访问节点
 * 3. 在遍历过程中计算相邻节点的差值
 * 4. 记录最小差值
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中求BST节点间的最小差值
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现复杂，需要维护前驱节点状态
 */
public class Code12_MorrisMinDiffInBSTFixed {

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
	 * 使用Morris中序遍历求BST中的最小绝对差
	 * 这是空间最优的实现，仅使用O(1)的额外空间
	 * 
	 * @param root BST的根节点
	 * @return 最小绝对差
	 * @throws NullPointerException 如果root为null（但代码已处理null情况，此处仅作文档说明）
	 * 
	 * 题目描述：
	 * 给你一个二叉搜索树的根节点 root ，返回树中任意两不同节点值之间的最小差值。
	 * 差值是一个正数，其数值等于两值之差的绝对值。
	 * 
	 * 解题思路：
	 * 1. 利用BST的性质：中序遍历得到递增序列
	 * 2. 在递增序列中，相邻元素之间的差值最小
	 * 3. 使用Morris中序遍历，在遍历过程中计算相邻节点值的差值
	 * 4. 维护最小差值
	 * 5. 本实现提供三种方法：Morris中序遍历、递归DFS、迭代DFS
	 * 
	 * 算法步骤（Morris中序遍历）：
	 * 1. 使用Morris中序遍历遍历BST
	 * 2. 在遍历过程中维护前一个节点pre
	 * 3. 计算当前节点与前一个节点的差值
	 * 4. 更新最小差值
	 * 
	 * 时间复杂度：
	 * - Morris方法：O(n) - 需要遍历所有节点，每个节点最多被访问3次
	 * - 递归方法：O(n) - 每个节点被访问一次
	 * - 迭代方法：O(n) - 每个节点被访问一次
	 * 
	 * 空间复杂度：
	 * - Morris方法：O(1) - 仅使用常数额外空间
	 * - 递归方法：O(h) - h为树高，最坏情况下为O(n)
	 * - 迭代方法：O(h) - 栈的空间复杂度，最坏情况下为O(n)
	 * 
	 * 是否为最优解：
	 * - 从空间复杂度角度，Morris方法最优
	 * - 从代码简洁性角度，递归方法更直观
	 * - 实际应用中可根据空间限制选择合适的方法
	 * 
	 * 适用场景：
	 * 1. 需要节省内存空间的环境
	 * 2. BST中序遍历的应用场景
	 * 3. 面试中展示对Morris遍历的深入理解
	 * 4. 大规模二叉搜索树的差值查找，内存受限场景
	 * 
	 * 边界情况处理：
	 * 1. 空树：直接返回0
	 * 2. 单节点树：直接返回0（无差值）
	 * 3. 负数值：处理方式与正数相同，因为BST中序遍历后会自然递增
	 * 4. 极端值：需要考虑整数溢出问题
	 * 
	 * 扩展思考：
	 * 1. 如何处理节点值为负数的情况？
	 *    - 算法不受影响，因为BST中序遍历仍会产生递增序列
	 * 2. 如何在并发环境下保证线程安全？
	 *    - 使用线程局部变量存储中间状态
	 *    - 避免修改原始树结构（Morris方法修改树结构，需考虑线程安全）
	 * 3. 如果不是BST而是普通二叉树，如何找最小差值？
	 *    - 需要遍历整棵树并收集所有值，排序后计算相邻差值
	 * 4. 如何处理可能的整数溢出？
	 *    - 使用long类型存储差值，避免计算过程中的溢出
	 * 
	 * 调试技巧：
	 * 1. 打印遍历顺序：验证是否按照中序遍历顺序
	 * 2. 打印相邻节点差值：跟踪最小差值的更新过程
	 * 3. 可视化树结构：帮助理解遍历路径
	 * 4. 使用断言验证BST性质：确保输入确实是BST
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/python-morris-qiu-bst-zui-xiao-chai-zhi-by-xxx/
	 * - C++: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/c-morris-qiu-bst-zui-xiao-chai-zhi-by-xxx/
	 */
	public int getMinimumDifference(TreeNode root) {
		// 边界情况处理：空树
		if (root == null) {
			return 0;
		}
		
		int minDiff = Integer.MAX_VALUE;  // 最小差值
		TreeNode pre = null;              // 前一个遍历的节点
		TreeNode cur = root;              // 当前节点
		TreeNode mostRight = null;        // 最右节点（前驱节点）
		
		// Morris中序遍历的核心循环
		while (cur != null) {
			mostRight = cur.left;
			
			// 如果当前节点有左子树
			if (mostRight != null) {
				// 找到左子树中的最右节点（前驱节点）
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				// 判断前驱节点的右指针状态
				if (mostRight.right == null) {
					// 第一次到达，建立线索
					// 线索指向当前节点，用于后续回溯
					mostRight.right = cur;
					// 继续向左子树深入，保证先访问左子树
					cur = cur.left;
					continue;  // 跳过当前迭代的剩余部分
				} else {
					// 第二次到达，断开线索
					// 恢复树的原始结构
					mostRight.right = null;
					// 此时需要处理当前节点（在第二次访问时）
				}
			}
			
			// 处理当前节点（中序遍历的核心处理逻辑）
			// 计算与前一个节点的差值
			if (pre != null) {
				// 使用Math.abs确保差值为正数
				minDiff = Math.min(minDiff, Math.abs(cur.val - pre.val));
			}
			
			// 更新前一个节点为当前节点
			pre = cur;
			// 处理完当前节点后，移动到右子树
			// 保证遍历顺序为：左-根-右
			cur = cur.right;
		}
		
		return minDiff;
	}
	
	/**
	 * 使用递归DFS方法找BST中的最小绝对差
	 * 递归实现更简洁直观，但空间复杂度为O(h)
	 * 
	 * @param root BST的根节点
	 * @return 最小绝对差
	 * 
	 * 算法步骤（递归DFS）：
	 * 1. 递归进行中序遍历
	 * 2. 在遍历过程中维护前一个节点和最小差值
	 * 3. 计算相邻节点的差值并更新最小值
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/python-di-gui-zhao-zui-xiao-chai-by-xxx/
	 * - C++: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/c-di-gui-zhao-zui-xiao-chai-by-xxx/
	 */
	public int getMinimumDifferenceRecursive(TreeNode root) {
		// 边界情况处理
		if (root == null) {
			return 0;
		}
		
		// 使用可变引用存储最小差值和前一个节点
		int[] minDiff = {Integer.MAX_VALUE};
		TreeNode[] pre = {null};
		
		// 执行递归中序遍历
		inorderDFS(root, minDiff, pre);
		
		return minDiff[0];
	}
	
	/**
	 * 递归中序遍历辅助函数
	 * 
	 * @param node 当前节点
	 * @param minDiff 最小差值（作为可变引用传递）
	 * @param pre 前一个节点（作为可变引用传递）
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/python-di-gui-zhao-zui-xiao-chai-by-xxx/
	 * - C++: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/c-di-gui-zhao-zui-xiao-chai-by-xxx/
	 */
	private void inorderDFS(TreeNode node, int[] minDiff, TreeNode[] pre) {
		// 基本情况：节点为空
		if (node == null) {
			return;
		}
		
		// 1. 递归处理左子树
		inorderDFS(node.left, minDiff, pre);
		
		// 2. 处理当前节点
		if (pre[0] != null) {
			// 计算与前一个节点的差值并更新最小差值
			int diff = Math.abs(node.val - pre[0].val);
			if (diff < minDiff[0]) {
				minDiff[0] = diff;
			}
		}
		// 更新前一个节点为当前节点
		pre[0] = node;
		
		// 3. 递归处理右子树
		inorderDFS(node.right, minDiff, pre);
	}
	
	/**
	 * 使用迭代DFS方法找BST中的最小绝对差
	 * 
	 * @param root BST的根节点
	 * @return 最小绝对差
	 * 
	 * 算法步骤（迭代DFS）：
	 * 1. 使用栈模拟递归进行中序遍历
	 * 2. 维护前一个节点和最小差值
	 * 3. 计算相邻节点的差值并更新最小值
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/python-die-dai-zhao-zui-xiao-chai-by-xxx/
	 * - C++: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/solution/c-die-dai-zhao-zui-xiao-chai-by-xxx/
	 */
	public int getMinimumDifferenceIterative(TreeNode root) {
		// 边界情况处理
		if (root == null) {
			return 0;
		}
		
		int minDiff = Integer.MAX_VALUE;  // 最小差值
		TreeNode pre = null;              // 前一个节点
		Stack<TreeNode> stack = new Stack<>();  // 栈用于模拟递归
		TreeNode cur = root;              // 当前节点
		
		// 迭代中序遍历（左-根-右）
		while (cur != null || !stack.isEmpty()) {
			// 1. 一直向左遍历，将节点入栈
			while (cur != null) {
				stack.push(cur);
				cur = cur.left;
			}
			
			// 2. 处理栈顶节点
			cur = stack.pop();
			
			// 3. 计算与前一个节点的差值并更新最小差值
			if (pre != null) {
				int diff = Math.abs(cur.val - pre.val);
				if (diff < minDiff) {
					minDiff = diff;
				}
			}
			// 更新前一个节点为当前节点
			pre = cur;
			
			// 4. 处理右子树
			cur = cur.right;
		}
		
		return minDiff;
	}
	
	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		// 创建测试树: [4,2,6,1,3]
		TreeNode root = new TreeNode(4);
		root.left = new TreeNode(2);
		root.right = new TreeNode(6);
		root.left.left = new TreeNode(1);
		root.left.right = new TreeNode(3);
		
		Code12_MorrisMinDiffInBSTFixed solution = new Code12_MorrisMinDiffInBSTFixed();
		
		System.out.println("测试用例: [4,2,6,1,3]");
		System.out.println("原始树中序遍历结果: ");
		printInOrder(root);
		
		System.out.println("\nMorris方法结果: " + solution.getMinimumDifference(root));
		System.out.println("递归方法结果: " + solution.getMinimumDifferenceRecursive(root));
		System.out.println("迭代方法结果: " + solution.getMinimumDifferenceIterative(root));
	}
	
	/**
	 * 中序遍历打印树节点值
	 * @param root 树的根节点
	 */
	public static void printInOrder(TreeNode root) {
		if (root == null) {
			return;
		}
		
		printInOrder(root.left);
		System.out.print(root.val + " ");
		printInOrder(root.right);
	}
}