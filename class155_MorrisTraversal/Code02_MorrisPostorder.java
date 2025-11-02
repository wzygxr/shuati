package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Morris遍历实现后序遍历
 * 
 * 题目来源：
 * - 后序遍历：LeetCode 145. Binary Tree Postorder Traversal
 *   链接：https://leetcode.cn/problems/binary-tree-postorder-traversal/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris后序遍历
 * 2. 递归版本的后序遍历
 * 3. 迭代版本的后序遍历
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-morris-hou-xu-bian-li-by-xxx/
 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-morris-hou-xu-bian-li-by-xxx/
 * 
 * 算法详解：
 * Morris后序遍历相对复杂，因为后序遍历的顺序是左->右->根，而线索化的过程是按照中序遍历的顺序进行的
 * 核心技巧是在第二次访问节点时，先收集其左子树的右边界，最后再收集整棵树的右边界
 * 1. 线索化过程与中序遍历类似
 * 2. 在第二次访问节点时，收集左子树的右边界（逆序）
 * 3. 最后收集整棵树的右边界（逆序）
 * 4. 通过翻转右边界链表来实现逆序收集
 * 
 * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
 * 空间复杂度：O(1) - 不考虑返回值的空间占用
 * 适用场景：内存受限环境、需要后序遍历的大规模二叉树
 * 优缺点分析：
 * - 优点：空间复杂度最优，适用于内存极度受限的环境
 * - 缺点：实现最为复杂，需要多次翻转链表，常数因子较大
 */
public class Code02_MorrisPostorder {

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
	 * 后序遍历二叉树 - 主方法
	 * 题目描述：给定一个二叉树，返回它的后序遍历结果
	 * LeetCode 145: Binary Tree Postorder Traversal
	 * 
	 * 解题思路：
	 * - 后序遍历顺序：左 -> 右 -> 根
	 * - Morris后序遍历利用右指针空闲空间构建线索，实现O(1)空间复杂度
	 * - 核心技巧是在第二次访问节点时，先收集其左子树的右边界，最后再收集整棵树的右边界
	 * 
	 * @param head 二叉树的根节点
	 * @return 后序遍历的节点值列表
	 * 
	 * 时间复杂度：O(n) - 每个节点最多被访问3次，总时间线性
	 * 空间复杂度：O(1) - 不考虑返回值的空间占用
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-morris-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-morris-hou-xu-bian-li-by-xxx/
	 */
	public static List<Integer> postorderTraversal(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		// 处理空树情况
		if (head == null) {
			return ans;
		}
		morrisPostorder(head, ans);
		return ans;
	}

	/**
	 * Morris后序遍历的核心实现
	 * 
	 * 算法步骤：
	 * 1. 当前节点cur初始化为根节点
	 * 2. 当cur不为空时：
	 *    a. 如果cur有左子树：
	 *       i. 找到左子树的最右节点mostRight
	 *       ii. 如果mostRight的right指针为空：第一次到达，建立线索，cur左移
	 *       iii. 如果mostRight的right指针指向cur：第二次到达，断开线索，收集左子树右边界，cur右移
	 *    b. 如果cur没有左子树：cur直接右移
	 * 3. 最后收集整棵树的右边界
	 * 
	 * @param head 根节点
	 * @param ans 结果列表
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-morris-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-morris-hou-xu-bian-li-by-xxx/
	 */
	public static void morrisPostorder(TreeNode head, List<Integer> ans) {
		TreeNode cur = head;
		TreeNode mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) { // cur有左子树
				// 找到左子树的最右节点
				// 注意：左子树最右节点的右指针可能为空，也可能指向cur（已建立的线索）
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				// 判断左子树最右节点的右指针状态
				if (mostRight.right == null) { // 第一次到达当前节点
					// 建立线索：左子树最右节点指向当前节点
					mostRight.right = cur;
					// 继续处理左子树
					cur = cur.left;
					continue;
				} else { // 第二次到达当前节点
					// 断开线索，恢复树的原始结构
					mostRight.right = null;
					// 收集当前节点左子树的右边界（逆序）
					collect(cur.left, ans);
				}
			}
			// 没有左子树或已处理完左子树，继续处理右子树
			cur = cur.right;
		}
		// 最后收集整棵树的右边界（逆序）
		collect(head, ans);
	}

	/**
	 * 收集以head为头的子树的右边界（逆序）
	 * 
	 * 实现思路：
	 * 1. 翻转右边界链表（类似单链表翻转）
	 * 2. 遍历翻转后的链表，收集节点值
	 * 3. 再次翻转链表，恢复原始结构
	 * 
	 * @param head 子树的根节点
	 * @param ans 结果列表
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-morris-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-morris-hou-xu-bian-li-by-xxx/
	 */
	public static void collect(TreeNode head, List<Integer> ans) {
		// 翻转右边界，返回新的头节点（原尾节点）
		TreeNode tail = reverse(head);
		// 遍历翻转后的右边界，收集节点值
		TreeNode cur = tail;
		while (cur != null) {
			ans.add(cur.val);
			cur = cur.right;
		}
		// 恢复原始的右边界结构
		reverse(tail);
	}

	/**
	 * 翻转链表（仅操作right指针）
	 * 
	 * 实现思路：类似单链表的翻转算法
	 * 
	 * @param from 链表的头节点
	 * @return 翻转后的链表头节点（原尾节点）
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-morris-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-morris-hou-xu-bian-li-by-xxx/
	 */
	public static TreeNode reverse(TreeNode from) {
		TreeNode pre = null;
		TreeNode next = null;
		while (from != null) {
			next = from.right; // 保存下一个节点
			from.right = pre;  // 翻转指针
			pre = from;        // pre前进
			from = next;       // 当前节点前进
		}
		return pre; // 返回新的头节点
	}

	/**
	 * 使用递归实现后序遍历
	 * 
	 * @param root 二叉树的根节点
	 * @return 后序遍历的节点值列表
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h) - h为树高，最坏O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-di-gui-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-di-gui-hou-xu-bian-li-by-xxx/
	 */
	public static List<Integer> postorderTraversalRecursive(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		recursiveHelper(root, result);
		return result;
	}

	/**
	 * 递归辅助方法
	 * 
	 * @param node 当前节点
	 * @param result 结果列表
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-di-gui-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-di-gui-hou-xu-bian-li-by-xxx/
	 */
	private static void recursiveHelper(TreeNode node, List<Integer> result) {
		if (node == null) {
			return;
		}
		// 左 -> 右 -> 根
		recursiveHelper(node.left, result);
		recursiveHelper(node.right, result);
		result.add(node.val);
	}

	/**
	 * 使用栈实现迭代后序遍历
	 * 
	 * 实现思路：使用一个栈记录访问路径，使用一个指针记录上一次访问的节点
	 * 
	 * @param root 二叉树的根节点
	 * @return 后序遍历的节点值列表
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(h) - h为树高，最坏O(n)
	 * 
	 * 三种语言实现链接：
	 * - Java: 当前方法
	 * - Python: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/python-die-dai-hou-xu-bian-li-by-xxx/
	 * - C++: https://leetcode.cn/problems/binary-tree-postorder-traversal/solution/c-die-dai-hou-xu-bian-li-by-xxx/
	 */
	public static List<Integer> postorderTraversalIterative(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		if (root == null) {
			return result;
		}
		
		Stack<TreeNode> stack = new Stack<>();
		TreeNode current = root;
		TreeNode lastVisited = null;
		
		while (!stack.isEmpty() || current != null) {
			// 将所有左子节点入栈
			while (current != null) {
				stack.push(current);
				current = current.left;
			}
			
			// 查看栈顶节点，但不弹出
			TreeNode peekNode = stack.peek();
			
			// 如果右子树存在且未被访问过，则处理右子树
			if (peekNode.right != null && lastVisited != peekNode.right) {
				current = peekNode.right;
			} else {
				// 否则处理当前节点（访问）
				result.add(peekNode.val);
				lastVisited = stack.pop();
			}
		}
		
		return result;
	}
	
	/**
     * 测试方法
     */
    public static void main(String[] args) {
        // 创建测试树
        //       1
        //      / \
        //     2   3
        //    / \
        //   4   5
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        
        System.out.println("后序遍历测试:");
        System.out.println("Morris方法: " + postorderTraversal(root));
        System.out.println("递归方法: " + postorderTraversalRecursive(root));
        System.out.println("迭代方法: " + postorderTraversalIterative(root));
    }
}
