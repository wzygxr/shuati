package class078;

// 最大BST子树
// 给定一个二叉树，找到其中最大的二叉搜索树（BST）子树，并返回该子树的大小
// 其中，最大指的是子树节点数最多的
// 二叉搜索树（BST）中的所有节点都具备以下属性：
// 左子树的值小于其父（根）节点的值
// 右子树的值大于其父（根）节点的值
// 注意：子树必须包含其所有后代
// 测试链接 : https://leetcode.cn/problems/largest-bst-subtree/
//
// 相关题目链接:
// 1. LeetCode 333. 最大BST子树 - https://leetcode.cn/problems/largest-bst-subtree/
// 2. LeetCode 98. 验证二叉搜索树 - https://leetcode.cn/problems/validate-binary-search-tree/
// 3. LeetCode 1373. 二叉搜索子树的最大键值和 - https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
// 4. 洛谷 P1352 没有上司的舞会 - https://www.luogu.com.cn/problem/P1352
// 5. HDU 1520 Anniversary party - http://acm.hdu.edu.cn/showproblem.php?pid=1520
// 6. POJ 3342 Party at Hali-Bula - http://poj.org/problem?id=3342
// 7. Codeforces 1083C Max Mex - https://codeforces.com/problemset/problem/1083/C
// 8. AtCoder ABC163F path pass i - https://atcoder.jp/contests/abc163/tasks/abc163_f
// 9. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树中的最大值
//    - 以该节点为根的子树中的最小值
//    - 该子树是否为BST
//    - 该子树中最大BST的节点数
// 3. 递归处理左右子树，综合计算当前节点的信息
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算最大BST子树的标准方法
public class Code01_LargestBstSubtree {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static int largestBSTSubtree(TreeNode root) {
		return f(root).maxBstSize;
	}

	// 用于存储递归过程中的信息
	public static class Info {
		// 以当前节点为根的子树中的最大值
		public long max;
		// 以当前节点为根的子树中的最小值
		public long min;
		// 该子树是否为BST
		public boolean isBst;
		// 该子树中最大BST的节点数
		public int maxBstSize;

		public Info(long a, long b, boolean c, int d) {
			max = a;
			min = b;
			isBst = c;
			maxBstSize = d;
		}
	}

	// 递归处理每个节点
	public static Info f(TreeNode x) {
		// 基本情况：空节点
		if (x == null) {
			// 空树也是BST，节点数为0
			// 最大值设为Long.MIN_VALUE，最小值设为Long.MAX_VALUE
			// 这样在比较时不会影响父节点的判断
			return new Info(Long.MIN_VALUE, Long.MAX_VALUE, true, 0);
		}
		
		// 递归处理左右子树
		Info infol = f(x.left);
		Info infor = f(x.right);
		
		// 计算当前子树的信息
		// 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
		long max = Math.max(x.val, Math.max(infol.max, infor.max));
		// 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
		long min = Math.min(x.val, Math.min(infol.min, infor.min));
		
		// 判断当前子树是否为BST
		// 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
		boolean isBst = infol.isBst && infor.isBst && infol.max < x.val && x.val < infor.min;
		
		// 计算当前子树中最大BST的节点数
		int maxBSTSize;
		if (isBst) {
			// 如果当前子树是BST，则最大BST节点数 = 左子树节点数 + 右子树节点数 + 1
			maxBSTSize = infol.maxBstSize + infor.maxBstSize + 1;
		} else {
			// 如果当前子树不是BST，则最大BST节点数 = max(左子树最大BST节点数, 右子树最大BST节点数)
			maxBSTSize = Math.max(infol.maxBstSize, infor.maxBstSize);
		}
		
		// 返回当前节点的信息
		return new Info(max, min, isBst, maxBSTSize);
	}

}