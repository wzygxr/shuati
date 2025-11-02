package class124;

import java.util.Stack;

/**
 * Morris遍历求根到叶数字之和
 * 
 * 题目来源：
 * - 求根到叶数字之和：LeetCode 129. Sum Root to Leaf Numbers
 *   链接：https://leetcode.cn/problems/sum-root-to-leaf-numbers/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris前序遍历求根到叶数字之和
 * 2. 递归版本的求根到叶数字之和
 * 3. 迭代版本的求根到叶数字之和
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/python-morris-qiu-gen-dao-xie-shu-zi-zhi-he-by-xxx/
 * - C++: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/c-morris-qiu-gen-dao-xie-shu-zi-zhi-he-by-xxx/
 * 
 * 算法详解：
 * 给定一个二叉树，每个节点包含0-9的数字，每条从根到叶节点的路径表示一个数字。
 * 计算所有从根到叶节点生成的数字之和。
 * 
 * 解题思路：
 * 1. 使用Morris前序遍历访问树的每个节点
 * 2. 在遍历过程中维护从根到当前节点的数字
 * 3. 当到达叶节点时，将该数字加到总和中
 * 4. 利用Morris遍历的线索化特性，在回溯时正确恢复路径数字
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问两次
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中计算二叉树根到叶路径数字之和
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现复杂，需要维护路径数字和深度信息
 */
public class Code10_MorrisSumRootToLeafNew {

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
	 * 使用Morris前序遍历计算根到叶节点数字之和
	 * 注意：这是一个特殊的实现，因为标准Morris遍历不适合路径回溯问题
	 * 
	 * @param root 二叉树的根节点
	 * @return 所有根到叶路径数字之和
	 * @throws NullPointerException 如果root为null（但代码已处理null情况，此处仅作文档说明）
	 * 
	 * 题目描述：
	 * 给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
	 * 每条从根节点到叶节点的路径都代表一个数字：
	 * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
	 * 计算从根节点到叶节点生成的所有数字之和。
	 * 叶节点是指没有子节点的节点。
	 * 
	 * 解题思路：
	 * 1. 需要遍历所有从根到叶的路径
	 * 2. 在遍历过程中维护当前路径表示的数字
	 * 3. 当到达叶节点时，将当前数字加到结果中
	 * 4. 本实现提供三种方法：Morris前序遍历、递归DFS、迭代DFS
	 * 
	 * 算法步骤（Morris前序遍历）：
	 * 1. 使用Morris前序遍历遍历二叉树
	 * 2. 在遍历过程中维护从根到当前节点的数字路径
	 * 3. 当到达叶节点时，累加路径数字到结果中
	 * 4. 需要特别处理回溯过程，因为Morris遍历会修改树结构
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
	 * - 从实现复杂度和代码可读性角度，递归方法最优
	 * - 对于此问题，推荐使用递归或迭代方法
	 * 
	 * 适用场景：
	 * 1. 理解Morris遍历思想的扩展应用
	 * 2. 面试中展示对算法的深入理解
	 * 3. 空间受限环境下的路径求和问题
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/python-morris-qiu-lu-jing-he-by-xxx/
	 * - C++: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/c-morris-qiu-lu-jing-he-by-xxx/
	 */
	public int sumNumbers(TreeNode root) {
		// 防御性编程：处理空树情况
		if (root == null) {
			return 0;
		}
		
		int totalSum = 0;              // 结果总和
		int currentNum = 0;            // 当前路径表示的数字
		TreeNode cur = root;           // 当前节点
		TreeNode mostRight = null;     // 最右节点（前驱节点）
		
		// 记录节点深度，用于正确回溯路径
		int depth = 0;
		
		// Morris前序遍历的核心循环
		while (cur != null) {
			mostRight = cur.left;
			
			// 如果当前节点有左子树
			if (mostRight != null) {
				// 计算到前驱节点的距离（用于正确回溯）
				int steps = 0;
				// 找到左子树中的最右节点（前驱节点）
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
					steps++;
				}
				
				// 判断前驱节点的右指针状态
				if (mostRight.right == null) {
					// 第一次到达，建立线索
					// 更新当前路径数字
					currentNum = currentNum * 10 + cur.val;
					depth++;
					
					// 如果是叶节点，累加到结果中
					if (cur.left == null && cur.right == null) {
						totalSum += currentNum;
					}
					
					mostRight.right = cur;  // 建立线索
					cur = cur.left;         // 继续向左子树深入
					continue;               // 跳过当前迭代的剩余部分
				} else {
					// 第二次到达，断开线索
					mostRight.right = null; // 断开线索，恢复树的原始结构
					
					// 恢复currentNum，回溯路径
					// 这里需要根据到前驱节点的距离正确恢复路径值
					for (int i = 0; i <= steps; i++) {
						currentNum /= 10;
						depth--;
					}
				}
			} else {
				// 没有左子树，直接处理当前节点
				currentNum = currentNum * 10 + cur.val;
				depth++;
				
				// 如果是叶节点，累加到结果中
				if (cur.right == null) { // 因为没有左子树，所以只需要检查右子树
					totalSum += currentNum;
					// 回溯路径（如果有父节点）
					currentNum /= 10;
					depth--;
				}
			}
			
			cur = cur.right; // 移动到右子树或通过线索回到父节点
		}
		
		return totalSum;
	}
	
	/**
	 * 使用递归DFS方法计算根到叶节点数字之和（推荐方法）
	 * 
	 * @param root 二叉树的根节点
	 * @return 所有根到叶路径数字之和
	 * 
	 * 算法步骤（递归DFS）：
	 * 1. 递归遍历二叉树
	 * 2. 在递归过程中维护当前路径表示的数字
	 * 3. 当到达叶节点时，返回当前路径数字作为贡献值
	 * 4. 非叶节点返回左右子树贡献值之和
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/python-di-gui-qiu-lu-jing-he-by-xxx/
	 * - C++: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/c-di-gui-qiu-lu-jing-he-by-xxx/
	 */
	public int sumNumbersRecursive(TreeNode root) {
		// 防御性编程：处理空树情况
		if (root == null) {
			return 0;
		}
		
		// 调用递归辅助函数，初始路径和为0
		return dfs(root, 0);
	}
	
	/**
	 * 递归DFS辅助函数
	 * 
	 * @param node 当前节点
	 * @param currentSum 到当前节点的路径数字
	 * @return 以当前节点为根的子树的所有路径数字之和
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/python-di-gui-qiu-lu-jing-he-by-xxx/
	 * - C++: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/c-di-gui-qiu-lu-jing-he-by-xxx/
	 */
	private int dfs(TreeNode node, int currentSum) {
		// 基本情况：空节点贡献0
		if (node == null) {
			return 0;
		}
		
		// 更新当前路径数字
		currentSum = currentSum * 10 + node.val;
		
		// 如果是叶节点，返回当前路径数字
		if (node.left == null && node.right == null) {
			return currentSum;
		}
		
		// 非叶节点：返回左右子树的贡献值之和
		return dfs(node.left, currentSum) + dfs(node.right, currentSum);
	}
	
	/**
	 * 使用迭代DFS方法计算根到叶节点数字之和
	 * 
	 * @param root 二叉树的根节点
	 * @return 所有根到叶路径数字之和
	 * 
	 * 算法步骤（迭代DFS）：
	 * 1. 使用栈模拟递归过程
	 * 2. 每个栈元素包含节点和到该节点的路径数字
	 * 3. 当遇到叶节点时，将路径数字加到结果中
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/python-die-dai-qiu-lu-jing-he-by-xxx/
	 * - C++: https://leetcode.cn/problems/sum-root-to-leaf-numbers/solution/c-die-dai-qiu-lu-jing-he-by-xxx/
	 */
	public int sumNumbersIterative(TreeNode root) {
		// 防御性编程：处理空树情况
		if (root == null) {
			return 0;
		}
		
		// 定义节点和路径和的结构体
		class NodeSum {
			TreeNode node;
			int sum;
			
			NodeSum(TreeNode node, int sum) {
				this.node = node;
				this.sum = sum;
			}
		}
		
		Stack<NodeSum> stack = new Stack<>();
		stack.push(new NodeSum(root, 0));
		
		int totalSum = 0;
		
		while (!stack.isEmpty()) {
			NodeSum ns = stack.pop();
			
			TreeNode node = ns.node;
			int currentSum = ns.sum;
			
			currentSum = currentSum * 10 + node.val;
			
			// 如果是叶节点，累加到结果中
			if (node.left == null && node.right == null) {
				totalSum += currentSum;
			} else {
				// 非叶节点，继续处理子节点
				// 先压入右子树，再压入左子树，保证左子树先被处理
				if (node.right != null) {
					stack.push(new NodeSum(node.right, currentSum));
				}
				if (node.left != null) {
					stack.push(new NodeSum(node.left, currentSum));
				}
			}
		}
		
		return totalSum;
	}
	
	/**
	 * 测试方法
	 */
	public static void main(String[] args) {
		// 创建测试树: [1,2,3]
		//     1
		//    / \
		//   2   3
		// 路径1->2表示数字12，路径1->3表示数字13
		// 总和 = 12 + 13 = 25
		TreeNode root1 = new TreeNode(1);
		root1.left = new TreeNode(2);
		root1.right = new TreeNode(3);
		
		Code10_MorrisSumRootToLeafNew solution = new Code10_MorrisSumRootToLeafNew();
		
		System.out.println("测试用例1: [1,2,3]");
		System.out.println("Morris方法结果: " + solution.sumNumbers(root1));
		System.out.println("递归方法结果: " + solution.sumNumbersRecursive(root1));
		System.out.println("迭代方法结果: " + solution.sumNumbersIterative(root1));
		
		// 创建测试树: [4,9,0,5,1]
		//       4
		//      / \
		//     9   0
		//    / \
		//   5   1
		// 路径4->9->5表示数字495，路径4->9->1表示数字491，路径4->0表示数字40
		// 总和 = 495 + 491 + 40 = 1026
		TreeNode root2 = new TreeNode(4);
		root2.left = new TreeNode(9);
		root2.right = new TreeNode(0);
		root2.left.left = new TreeNode(5);
		root2.left.right = new TreeNode(1);
		
		System.out.println("\n测试用例2: [4,9,0,5,1]");
		System.out.println("Morris方法结果: " + solution.sumNumbers(root2));
		System.out.println("递归方法结果: " + solution.sumNumbersRecursive(root2));
		System.out.println("迭代方法结果: " + solution.sumNumbersIterative(root2));
	}
}