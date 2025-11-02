package class100;

import java.util.*;

/**
 * 子树匹配算法及其应用题目集合
 * 
 * 本类实现了子树匹配的核心算法，并提供了多个相关题目的解决方案
 * 子树匹配是二叉树操作中的经典问题，主要应用于树形结构的比较、搜索等场景
 * 
 * 核心思想：
 * 1. 暴力递归法：遍历每个节点，检查以该节点为根的子树是否与目标子树相同
 * 2. 序列化+KMP算法：将树序列化为字符串，使用KMP算法查找子序列
 * 
 * 应用场景：
 * - 树形结构相似度比较
 * - XML/JSON文档片段匹配
 * - 代码结构分析
 * - 模式识别中的树形结构匹配
 */
// 另一棵树的子树 (LeetCode 572)
// 给你两棵二叉树root和subRoot
// 检验root中是否包含和subRoot具有相同结构和节点值的子树
// 如果存在，返回true
// 否则，返回false
// 测试链接 : https://leetcode.cn/problems/subtree-of-another-tree/
public class Code02_SubtreeOfAnotherTree {

	// 不要提交这个类
	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode() {}

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
	 * 方法1：暴力递归
	 * 算法思路：
	 * 1. 遍历树t1的每个节点
	 * 2. 对于每个节点，检查以该节点为根的子树是否与t2相同
	 * 3. 如果相同，返回true
	 * 4. 如果遍历完所有节点都没有找到匹配的子树，返回false
	 *
	 * 时间复杂度：O(n * m)，其中n是t1的节点数，m是t2的节点数
	 * 空间复杂度：O(max(n, m))，递归调用栈的深度
	 *
	 * @param t1 主树
	 * @param t2 子树
	 * @return 如果t1包含t2返回true，否则返回false
	 */
	public static boolean isSubtree(TreeNode t1, TreeNode t2) {
		if (t1 != null && t2 != null) {
			return same(t1, t2) || isSubtree(t1.left, t2) || isSubtree(t1.right, t2);
		}
		return t2 == null;
	}

	/**
	 * 判断两棵树是否完全相同
	 * 算法思路：
	 * 1. 如果两个节点都为null，返回true
	 * 2. 如果一个节点为null，另一个不为null，返回false
	 * 3. 如果两个节点值不相等，返回false
	 * 4. 递归比较左右子树
	 *
	 * @param a 树a的节点
	 * @param b 树b的节点
	 * @return 如果两棵树相同返回true，否则返回false
	 */
	public static boolean same(TreeNode a, TreeNode b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null && b != null) {
			return a.val == b.val && same(a.left, b.left) && same(a.right, b.right);
		}
		return false;
	}

	/**
	 * 方法2：二叉树先序序列化 + KMP算法匹配
	 * 算法思路：
	 * 1. 将两棵树进行先序序列化
	 * 2. 使用KMP算法在t1的序列化结果中查找t2的序列化结果
	 * 3. 如果能找到，说明t1包含t2作为子树
	 *
	 * 时间复杂度：O(n + m)，其中n是t1的节点数，m是t2的节点数
	 * 空间复杂度：O(n + m)，用于存储序列化结果
	 *
	 * @param t1 主树
	 * @param t2 子树
	 * @return 如果t1包含t2返回true，否则返回false
	 */
	public static boolean isSubtree2(TreeNode t1, TreeNode t2) {
		if (t1 != null && t2 != null) {
			ArrayList<String> s1 = new ArrayList<>();
			ArrayList<String> s2 = new ArrayList<>();
			serial(t1, s1);
			serial(t2, s2);
			return kmp(s1, s2) != -1;
		}
		return t2 == null;
	}

	/**
	 * 二叉树先序序列化
	 * 算法思路：
	 * 1. 如果节点为null，添加null到序列中
	 * 2. 如果节点不为null，添加节点值到序列中
	 * 3. 递归序列化左右子树
	 *
	 * @param head 树的根节点
	 * @param path 序列化结果存储的列表
	 */
	public static void serial(TreeNode head, ArrayList<String> path) {
		if (head == null) {
			path.add(null);
		} else {
			path.add(String.valueOf(head.val));
			serial(head.left, path);
			serial(head.right, path);
		}
	}

	/**
	 * KMP算法在序列中查找子序列
	 * 算法思路：
	 * 1. 构建模式串s2的next数组
	 * 2. 使用双指针在s1中查找s2
	 * 3. 匹配成功返回起始位置，失败返回-1
	 *
	 * @param s1 文本串序列
	 * @param s2 模式串序列
	 * @return 匹配的起始位置，如果不存在返回-1
	 */
	public static int kmp(ArrayList<String> s1, ArrayList<String> s2) {
		int n = s1.size(), m = s2.size(), x = 0, y = 0;
		int[] next = nextArray(s2, m);
		while (x < n && y < m) {
			if (isEqual(s1.get(x), s2.get(y))) {
				x++;
				y++;
			} else if (y == 0) {
				x++;
			} else {
				y = next[y];
			}
		}
		return y == m ? x - y : -1;
	}

	/**
	 * 构建next数组
	 * @param s 模式串序列
	 * @param m 模式串长度
	 * @return next数组
	 */
	public static int[] nextArray(ArrayList<String> s, int m) {
		if (m == 1) {
			return new int[] { -1 };
		}
		int[] next = new int[m];
		next[0] = -1;
		next[1] = 0;
		int i = 2, cn = 0;
		while (i < next.length) {
			if (isEqual(s.get(i - 1), s.get(cn))) {
				next[i++] = ++cn;
			} else if (cn > 0) {
				cn = next[cn];
			} else {
				next[i++] = 0;
			}
		}
		return next;
	}

	/**
	 * 比对两个字符串是否相等
	 * a和b可能为null
	 * @param a 字符串a
	 * @param b 字符串b
	 * @return 如果相等返回true，否则返回false
	 */
	public static boolean isEqual(String a, String b) {
		if (a == null && b == null) {
			return true;
		}
		if (a != null && b != null) {
			return a.equals(b);
		}
		return false;
	}

	/**
	 * 测试用例和使用示例
	 */
	/**
	 * 测试LeetCode 572: 另一棵树的子树
	 * 验证暴力递归和KMP+序列化两种解法
	 */
	public static void testSubtreeOfAnotherTree() {
		System.out.println("========== 测试 LeetCode 572: 另一棵树的子树 ==========");
		
		// 构建测试用例1: t1包含t2
		// t1:
		//     3
		//    / \
		//   4   5
		//  / \
		// 1   2
		//
		// t2:
		//   4
		//  / \
		// 1   2
		TreeNode t1_root1 = new TreeNode(3);
		t1_root1.left = new TreeNode(4);
		t1_root1.right = new TreeNode(5);
		t1_root1.left.left = new TreeNode(1);
		t1_root1.left.right = new TreeNode(2);

		TreeNode t2_root1 = new TreeNode(4);
		t2_root1.left = new TreeNode(1);
		t2_root1.right = new TreeNode(2);

		boolean result1_method1 = isSubtree(t1_root1, t2_root1);
		boolean result1_method2 = isSubtree2(t1_root1, t2_root1);

		System.out.println("测试用例1:");
		System.out.println("方法1结果: " + result1_method1 + "，期望输出: true");
		System.out.println("方法2结果: " + result1_method2 + "，期望输出: true");
		System.out.println();

		// 构建测试用例2: t1不包含t2
		// t1:
		//     3
		//    / \
		//   4   5
		//  / \
		// 1   2
		//    /
		//   0
		//
		// t2:
		//   4
		//  / \
		// 1   2
		TreeNode t1_root2 = new TreeNode(3);
		t1_root2.left = new TreeNode(4);
		t1_root2.right = new TreeNode(5);
		t1_root2.left.left = new TreeNode(1);
		t1_root2.left.right = new TreeNode(2);
		t1_root2.left.right.left = new TreeNode(0);

		TreeNode t2_root2 = new TreeNode(4);
		t2_root2.left = new TreeNode(1);
		t2_root2.right = new TreeNode(2);

		boolean result2_method1 = isSubtree(t1_root2, t2_root2);
		boolean result2_method2 = isSubtree2(t1_root2, t2_root2);

		System.out.println("测试用例2:");
		System.out.println("方法1结果: " + result2_method1 + "，期望输出: false");
		System.out.println("方法2结果: " + result2_method2 + "，期望输出: false");
		System.out.println();

		// 测试用例3: t2为空树
		boolean result3_method1 = isSubtree(t1_root1, null);
		boolean result3_method2 = isSubtree2(t1_root1, null);

		System.out.println("测试用例3 (t2为空):");
		System.out.println("方法1结果: " + result3_method1 + "，期望输出: true");
		System.out.println("方法2结果: " + result3_method2 + "，期望输出: true");
		System.out.println();

		// 测试用例4: t1为空树，t2非空
		boolean result4_method1 = isSubtree(null, t2_root1);
		boolean result4_method2 = isSubtree2(null, t2_root1);

		System.out.println("测试用例4 (t1为空，t2非空):");
		System.out.println("方法1结果: " + result4_method1 + "，期望输出: false");
		System.out.println("方法2结果: " + result4_method2 + "，期望输出: false");
		System.out.println();
	}

	/**
	 * LeetCode 652: 寻找重复的子树
	 * 题目描述：给定一棵二叉树，返回所有重复的子树
	 * 对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可
	 * 两棵树重复是指它们具有相同的结构以及相同的结点值
	 * 测试链接: https://leetcode.cn/problems/find-duplicate-subtrees/
	 * 
	 * 算法思路：
	 * 1. 使用后序遍历序列化每个子树
	 * 2. 使用哈希表记录每个序列化结果出现的次数
	 * 3. 当某个序列化结果出现次数为2时，将对应子树的根节点加入结果集
	 * 
	 * 时间复杂度：O(n²)，其中n是树的节点数，每个节点可能需要O(n)时间序列化
	 * 空间复杂度：O(n²)，存储所有子树的序列化结果
	 * 
	 * @param root 二叉树的根节点
	 * @return 重复子树的根节点列表
	 */
	public static List<TreeNode> findDuplicateSubtrees(TreeNode root) {
		List<TreeNode> result = new ArrayList<>();
		Map<String, Integer> countMap = new HashMap<>();
		serializeAndCount(root, countMap, result);
		return result;
	}

	/**
	 * 序列化子树并计数
	 * @param node 当前节点
	 * @param countMap 序列化结果计数表
	 * @param result 重复子树根节点列表
	 * @return 当前子树的序列化字符串
	 */
	private static String serializeAndCount(TreeNode node, Map<String, Integer> countMap, List<TreeNode> result) {
		if (node == null) {
			return "#";
		}
		
		// 后序遍历序列化
		String left = serializeAndCount(node.left, countMap, result);
		String right = serializeAndCount(node.right, countMap, result);
		String serial = node.val + "," + left + "," + right;
		
		// 计数并收集结果
		countMap.put(serial, countMap.getOrDefault(serial, 0) + 1);
		if (countMap.get(serial) == 2) {
			result.add(node);
		}
		
		return serial;
	}

	/**
	 * 测试LeetCode 652: 寻找重复的子树
	 */
	public static void testFindDuplicateSubtrees() {
		System.out.println("========== 测试 LeetCode 652: 寻找重复的子树 ==========");
		
		// 构建测试用例
		//     1
		//    / \
		//   2   3
		//  /   / \
		// 4   2   4
		//    /
		//   4
		TreeNode root = new TreeNode(1);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		root.left.left = new TreeNode(4);
		root.right.left = new TreeNode(2);
		root.right.right = new TreeNode(4);
		root.right.left.left = new TreeNode(4);
		
		List<TreeNode> result = findDuplicateSubtrees(root);
		System.out.println("重复子树数量: " + result.size()); // 期望输出: 2
		System.out.println("重复子树根节点值: ");
		for (TreeNode node : result) {
			System.out.print(node.val + " "); // 期望输出: 2 4 或 4 2
		}
		System.out.println("\n");
	}

	/**
	 * LeetCode 1367: 二叉树中的链表
	 * 题目描述：给定一棵二叉树，判断它是否包含一个子树，其结构与给定的链表完全相同
	 * 链表中的节点值应与二叉树中的对应节点值完全匹配
	 * 测试链接: https://leetcode.cn/problems/linked-list-in-binary-tree/
	 * 
	 * 算法思路：
	 * 1. 遍历二叉树的每个节点
	 * 2. 对于每个节点，尝试匹配链表
	 * 3. 使用DFS递归匹配
	 * 
	 * 时间复杂度：O(n*m)，其中n是树的节点数，m是链表长度
	 * 空间复杂度：O(max(h, m))，h是树的高度，m是链表长度
	 * 
	 * @param head 链表头节点
	 * @param root 二叉树根节点
	 * @return 是否存在匹配
	 */
	public static boolean isSubPath(ListNode head, TreeNode root) {
		if (head == null) {
			return true;
		}
		if (root == null) {
			return false;
		}
		
		// 检查当前节点是否能开始匹配，或者在左子树、右子树中寻找匹配
		return dfsMatch(head, root) || isSubPath(head, root.left) || isSubPath(head, root.right);
	}

	/**
	 * DFS递归匹配链表和子树
	 * @param head 链表当前节点
	 * @param root 二叉树当前节点
	 * @return 是否匹配
	 */
	private static boolean dfsMatch(ListNode head, TreeNode root) {
		if (head == null) {
			return true; // 链表匹配完成
		}
		if (root == null) {
			return false; // 树遍历完但链表未匹配完
		}
		if (head.val != root.val) {
			return false; // 当前节点值不匹配
		}
		
		// 递归匹配下一个节点
		return dfsMatch(head.next, root.left) || dfsMatch(head.next, root.right);
	}

	/**
	 * 链表节点类
	 * 用于LeetCode 1367题
	 */
	public static class ListNode {
		int val;
		ListNode next;
		ListNode() {}
		ListNode(int val) { this.val = val; }
		ListNode(int val, ListNode next) { this.val = val; this.next = next; }
	}

	/**
	 * 测试LeetCode 1367: 二叉树中的链表
	 */
	public static void testIsSubPath() {
		System.out.println("========== 测试 LeetCode 1367: 二叉树中的链表 ==========");
		
		// 构建测试用例1: 匹配
		// 链表: 4->2->8
		// 二叉树:
		//      1
		//     / \
		//    4   4
		//     \   \
		//      2   2
		//       \   \
		//        8   6
		//             \
		//              8
		ListNode head1 = new ListNode(4);
		head1.next = new ListNode(2);
		head1.next.next = new ListNode(8);
		
		TreeNode root1 = new TreeNode(1);
		root1.left = new TreeNode(4);
		root1.right = new TreeNode(4);
		root1.left.right = new TreeNode(2);
		root1.right.right = new TreeNode(2);
		root1.left.right.right = new TreeNode(8);
		root1.right.right.right = new TreeNode(6);
		root1.right.right.right.right = new TreeNode(8);
		
		boolean result1 = isSubPath(head1, root1);
		System.out.println("测试用例1结果: " + result1 + "，期望输出: true");
		
		// 测试用例2: 匹配
		// 链表: 1->4->2->6->8
		// 在二叉树中存在路径: 1(根)->4(右子树)->2(右子树)->6(右子树)->8(右子树)
		ListNode head2 = new ListNode(1);
		head2.next = new ListNode(4);
		head2.next.next = new ListNode(2);
		head2.next.next.next = new ListNode(6);
		head2.next.next.next.next = new ListNode(8);
		
		boolean result2 = isSubPath(head2, root1);
		System.out.println("测试用例2结果: " + result2 + "，期望输出: true");
		System.out.println();
	}

	/**
	 * LeetCode 951: 翻转等价二叉树
	 * 题目描述：判断两棵二叉树是否是翻转等价的
	 * 翻转等价的定义是：通过交换任意节点的左右子树若干次，可以使两棵树变得完全相同
	 * 测试链接: https://leetcode.cn/problems/flip-equivalent-binary-trees/
	 * 
	 * 算法思路：
	 * 1. 如果两个节点都为空，返回true
	 * 2. 如果一个为空另一个不为空，或节点值不同，返回false
	 * 3. 递归判断：要么不翻转直接匹配左右子树，要么翻转后匹配
	 * 
	 * 时间复杂度：O(min(n, m))，其中n和m是两棵树的节点数
	 * 空间复杂度：O(min(h1, h2))，h1和h2是两棵树的高度
	 * 
	 * @param root1 第一棵树的根节点
	 * @param root2 第二棵树的根节点
	 * @return 是否翻转等价
	 */
	public static boolean flipEquiv(TreeNode root1, TreeNode root2) {
		if (root1 == null && root2 == null) {
			return true;
		}
		if (root1 == null || root2 == null || root1.val != root2.val) {
			return false;
		}
		
		// 不翻转的情况 或 翻转的情况
		return (flipEquiv(root1.left, root2.left) && flipEquiv(root1.right, root2.right)) || 
		       (flipEquiv(root1.left, root2.right) && flipEquiv(root1.right, root2.left));
	}

	/**
	 * 测试LeetCode 951: 翻转等价二叉树
	 */
	public static void testFlipEquiv() {
		System.out.println("========== 测试 LeetCode 951: 翻转等价二叉树 ==========");
		
		// 测试用例1: 翻转等价
		// 树1:
		//      1
		//     / \
		//    2   3
		//   / \   \
		//  4   5   6
		//     / \
		//    7   8
		//
		// 树2 (翻转后等价):
		//      1
		//     / \
		//    3   2
		//   /   / \
		//  6   5   4
		//     / \
		//    8   7
		TreeNode root1 = new TreeNode(1);
		root1.left = new TreeNode(2);
		root1.right = new TreeNode(3);
		root1.left.left = new TreeNode(4);
		root1.left.right = new TreeNode(5);
		root1.right.right = new TreeNode(6);
		root1.left.right.left = new TreeNode(7);
		root1.left.right.right = new TreeNode(8);
		
		TreeNode root2 = new TreeNode(1);
		root2.left = new TreeNode(3);
		root2.right = new TreeNode(2);
		root2.left.left = new TreeNode(6);
		root2.right.left = new TreeNode(5);
		root2.right.right = new TreeNode(4);
		root2.right.left.left = new TreeNode(8);
		root2.right.left.right = new TreeNode(7);
		
		boolean result1 = flipEquiv(root1, root2);
		System.out.println("测试用例1结果: " + result1 + "，期望输出: true");
		
		// 测试用例2: 不等价
		TreeNode root3 = new TreeNode(1);
		root3.left = new TreeNode(2);
		root3.left.left = new TreeNode(3);
		
		TreeNode root4 = new TreeNode(1);
		root4.left = new TreeNode(3);
		root4.right = new TreeNode(2);
		
		boolean result2 = flipEquiv(root3, root4);
		System.out.println("测试用例2结果: " + result2 + "，期望输出: false");
		System.out.println();
	}

	/**
	 * 主方法，运行所有测试
	 */
	public static void main(String[] args) {
		// 运行所有测试用例
		testSubtreeOfAnotherTree();
		testFindDuplicateSubtrees();
		testIsSubPath();
		testFlipEquiv();
	}

}