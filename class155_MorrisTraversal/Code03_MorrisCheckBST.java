package class124;

import java.util.Stack;

/**
 * Morris遍历判断搜索二叉树
 * 
 * 题目来源：
 * - 验证BST：LeetCode 98. Validate Binary Search Tree
 *   链接：https://leetcode.cn/problems/validate-binary-search-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris中序遍历验证BST
 * 2. 递归版本的验证BST
 * 3. 迭代版本的验证BST
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-morris-yan-zheng-bst-by-xxx/
 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-morris-yan-zheng-bst-by-xxx/
 * 
 * 算法详解：
 * 利用BST的中序遍历结果应该是严格递增的特性，通过Morris中序遍历在O(1)空间复杂度下验证BST
 * 1. 使用Morris中序遍历访问每个节点
 * 2. 在遍历过程中检查当前节点值是否大于前一个遍历的节点值
 * 3. 如果发现违反BST性质的情况，立即返回false
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问2次
 * 空间复杂度：O(1) - 不使用额外空间
 * 适用场景：内存受限环境中验证大规模BST、在线算法验证BST
 * 优缺点分析：
 * - 优点：空间复杂度最优，适合内存受限环境
 * - 缺点：实现相对复杂，需要维护前驱节点指针
 */
public class Code03_MorrisCheckBST {

	/**
	 * 二叉树节点定义
	 */
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
	 * 验证二叉搜索树 - 主方法（Morris遍历实现）
	 * 题目描述：给定一个二叉树，判断其是否是一个有效的二叉搜索树（BST）
	 * LeetCode 98: Validate Binary Search Tree
	 * 
	 * 解题思路：
	 * - 利用Morris中序遍历，保证O(1)空间复杂度
	 * - BST的中序遍历结果应该是严格递增的
	 * - 在遍历过程中检查当前节点值是否大于前一个遍历的节点值
	 * 
	 * @param head 二叉树的根节点
	 * @return 如果是有效的二叉搜索树返回true，否则返回false
	 * 
	 * 时间复杂度：O(n) - 每个节点最多被访问2次
	 * 空间复杂度：O(1) - 不使用额外空间
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-morris-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-morris-yan-zheng-bst-by-xxx/
	 */
	public static boolean isValidBST(TreeNode head) {
		TreeNode cur = head;
		TreeNode mostRight = null;
		// 前一个遍历的节点，用于比较值的大小
		TreeNode pre = null;
		boolean ans = true;
		
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				// 找到左子树的最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				// 第一次到达当前节点，建立线索
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					// 第二次到达当前节点，断开线索
					mostRight.right = null;
				}
			}
			
			// 检查是否满足BST性质：当前节点值必须严格大于前一个节点值
			// 注意：这里只在第二次访问节点（或只有右子树的节点）时进行比较
			if (pre != null && pre.val >= cur.val) {
				ans = false;
			}
			// 更新pre指针为当前节点
			pre = cur;
			// 继续处理右子树
			cur = cur.right;
		}
		
		return ans;
	}

	/**
	 * 使用递归实现验证二叉搜索树
	 * 采用范围验证法：每个节点必须满足 lower < val < upper
	 * 
	 * @param root 二叉树的根节点
	 * @return 如果是有效的二叉搜索树返回true，否则返回false
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h) - h为树高，最坏O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-di-gui-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-di-gui-yan-zheng-bst-by-xxx/
	 */
	public static boolean isValidBSTRecursive(TreeNode root) {
		// 使用Long.MIN_VALUE和Long.MAX_VALUE来处理边界情况
		return recursiveHelper(root, Long.MIN_VALUE, Long.MAX_VALUE);
	}

	/**
	 * 递归辅助方法 - 范围验证法
	 * 
	 * @param node 当前节点
	 * @param lower 当前节点值的下界（不包含）
	 * @param upper 当前节点值的上界（不包含）
	 * @return 如果当前子树是有效的BST返回true
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-di-gui-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-di-gui-yan-zheng-bst-by-xxx/
	 */
	private static boolean recursiveHelper(TreeNode node, long lower, long upper) {
		// 空节点视为有效的BST
		if (node == null) {
			return true;
		}
		
		// 检查当前节点值是否在有效范围内
		if (node.val <= lower || node.val >= upper) {
			return false;
		}
		
		// 递归验证左子树（上界更新为当前节点值）和右子树（下界更新为当前节点值）
		return recursiveHelper(node.left, lower, node.val) && 
		       recursiveHelper(node.right, node.val, upper);
	}

	/**
	 * 使用中序遍历递归实现验证二叉搜索树
	 * 
	 * @param root 二叉树的根节点
	 * @return 如果是有效的二叉搜索树返回true，否则返回false
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-zhong-xu-bian-li-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-zhong-xu-bian-li-yan-zheng-bst-by-xxx/
	 */
	public static boolean isValidBSTInorderRecursive(TreeNode root) {
		// 使用包装类型Long而不是long，以便初始值可以为null
		return inorderRecursiveHelper(root, new Long[] { null });
	}

	/**
	 * 中序遍历递归辅助方法
	 * 
	 * @param node 当前节点
	 * @param prev 前一个访问节点的值（通过数组传递，实现引用传递）
	 * @return 如果中序遍历序列严格递增返回true
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-zhong-xu-bian-li-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-zhong-xu-bian-li-yan-zheng-bst-by-xxx/
	 */
	private static boolean inorderRecursiveHelper(TreeNode node, Long[] prev) {
		if (node == null) {
			return true;
		}
		
		// 先验证左子树
		if (!inorderRecursiveHelper(node.left, prev)) {
			return false;
		}
		
		// 验证当前节点值是否大于前一个节点值
		if (prev[0] != null && node.val <= prev[0]) {
			return false;
		}
		
		// 更新prev为当前节点值
		prev[0] = (long)node.val;
		
		// 验证右子树
		return inorderRecursiveHelper(node.right, prev);
	}

	/**
	 * 使用栈实现迭代中序遍历验证二叉搜索树
	 * 
	 * @param root 二叉树的根节点
	 * @return 如果是有效的二叉搜索树返回true，否则返回false
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h) - h为树高，最坏O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/validate-binary-search-tree/solution/python-die-dai-yan-zheng-bst-by-xxx/
	 * - C++: https://leetcode.cn/problems/validate-binary-search-tree/solution/c-die-dai-yan-zheng-bst-by-xxx/
	 */
	public static boolean isValidBSTIterative(TreeNode root) {
		if (root == null) {
			return true;
		}
		
		Stack<TreeNode> stack = new Stack<>();
		TreeNode cur = root;
		// 前一个访问节点的值
		Long prevVal = null;
		
		// 迭代中序遍历：左-根-右
		while (!stack.isEmpty() || cur != null) {
			// 将所有左子节点入栈
			while (cur != null) {
				stack.push(cur);
				cur = cur.left;
			}
			
			// 访问栈顶节点
			cur = stack.pop();
			
			// 检查是否满足BST性质
			if (prevVal != null && cur.val <= prevVal) {
				return false;
			}
			
			// 更新prevVal为当前节点值
			prevVal = (long) cur.val;
			
			// 处理右子树
			cur = cur.right;
		}
		
		return true;
	}
	
	/**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建有效的BST
        //       5
        //      / \
        //     3   8
        //    / \ / \
        //   2  4 7  9
        TreeNode validBST = new TreeNode(5);
        validBST.left = new TreeNode(3);
        validBST.right = new TreeNode(8);
        validBST.left.left = new TreeNode(2);
        validBST.left.right = new TreeNode(4);
        validBST.right.left = new TreeNode(7);
        validBST.right.right = new TreeNode(9);
        
        // 创建无效的BST
        //       5
        //      / \
        //     3   8
        //    / \ / \
        //   2  6 7  9  (6 > 5，违反BST性质)
        TreeNode invalidBST = new TreeNode(5);
        invalidBST.left = new TreeNode(3);
        invalidBST.right = new TreeNode(8);
        invalidBST.left.left = new TreeNode(2);
        invalidBST.left.right = new TreeNode(6); // 这里违反了BST性质
        invalidBST.right.left = new TreeNode(7);
        invalidBST.right.right = new TreeNode(9);
        
        System.out.println("验证BST测试:");
        System.out.println("有效BST - Morris方法: " + isValidBST(validBST));
        System.out.println("有效BST - 递归方法: " + isValidBSTRecursive(validBST));
        System.out.println("有效BST - 迭代方法: " + isValidBSTIterative(validBST));
        
        System.out.println("无效BST - Morris方法: " + isValidBST(invalidBST));
        System.out.println("无效BST - 递归方法: " + isValidBSTRecursive(invalidBST));
        System.out.println("无效BST - 迭代方法: " + isValidBSTIterative(invalidBST));
    }
}
