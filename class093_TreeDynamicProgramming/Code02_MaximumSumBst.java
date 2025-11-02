package class078;

// 二叉搜索子树的最大键值和
// 给你一棵以 root 为根的二叉树
// 请你返回 任意 二叉搜索子树的最大键值和
// 二叉搜索树的定义如下：
// 任意节点的左子树中的键值都 小于 此节点的键值
// 任意节点的右子树中的键值都 大于 此节点的键值
// 任意节点的左子树和右子树都是二叉搜索树
// 测试链接 : https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
//
// 相关题目链接:
// 1. LeetCode 1373. 二叉搜索子树的最大键值和 - https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
// 2. LeetCode 333. 最大BST子树 - https://leetcode.cn/problems/largest-bst-subtree/
// 3. LeetCode 98. 验证二叉搜索树 - https://leetcode.cn/problems/validate-binary-search-tree/
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
//    - 该子树中所有节点值的和
//    - 该子树是否为BST
//    - 以该节点为根的子树中BST的最大键值和
// 3. 递归处理左右子树，综合计算当前节点的信息
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算BST最大键值和的标准方法
public class Code02_MaximumSumBst {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static int maxSumBST(TreeNode root) {
		return f(root).maxBstSum;
	}

	public static class Info {
		// 为什么这里的max和min是int类型？
		// 因为题目的数据量规定，
		// 节点值在[-4 * 10^4，4 * 10^4]范围
		// 所以int类型的最小值和最大值就够用了
		// 不需要用long类型
		
		// 以当前节点为根的子树中的最大值
		public int max;
		// 以当前节点为根的子树中的最小值
		public int min;
		// 该子树中所有节点值的和
		public int sum;
		// 该子树是否为BST
		public boolean isBst;
		// 以该节点为根的子树中BST的最大键值和
		public int maxBstSum;

		public Info(int a, int b, int c, boolean d, int e) {
			max = a;
			min = b;
			sum = c;
			isBst = d;
			maxBstSum = e;
		}
	}

	public static Info f(TreeNode x) {
		// 基本情况：空节点
		if (x == null) {
			// 空树也是BST，节点数为0，和为0
			// 最大值设为Integer.MIN_VALUE，最小值设为Integer.MAX_VALUE
			// 这样在比较时不会影响父节点的判断
			return new Info(Integer.MIN_VALUE, Integer.MAX_VALUE, 0, true, 0);
		}
		
		// 递归处理左右子树
		Info infol = f(x.left);
		Info infor = f(x.right);
		
		// 计算当前子树的信息
		// 当前子树的最大值 = max(当前节点值, 左子树最大值, 右子树最大值)
		int max = Math.max(x.val, Math.max(infol.max, infor.max));
		// 当前子树的最小值 = min(当前节点值, 左子树最小值, 右子树最小值)
		int min = Math.min(x.val, Math.min(infol.min, infor.min));
		// 当前子树所有节点值的和 = 左子树节点值和 + 右子树节点值和 + 当前节点值
		int sum = infol.sum + infor.sum + x.val;
		
		// 判断当前子树是否为BST
		// 条件：左右子树都是BST，且左子树最大值 < 当前节点值 < 右子树最小值
		boolean isBst = infol.isBst && infor.isBst && infol.max < x.val && x.val < infor.min;
		
		// 计算当前子树中BST的最大键值和
		int maxBstSum = Math.max(infol.maxBstSum, infor.maxBstSum);
		if (isBst) {
			// 如果当前子树是BST，则更新最大键值和
			maxBstSum = Math.max(maxBstSum, sum);
		}
		
		// 返回当前节点的信息
		return new Info(max, min, sum, isBst, maxBstSum);
	}

}