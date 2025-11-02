package class078;

import java.util.HashMap;

// 路径总和 III
// 给定一个二叉树的根节点 root ，和一个整数 targetSum
// 求该二叉树里节点值之和等于 targetSum 的 路径 的数目
// 路径 不需要从根节点开始，也不需要在叶子节点结束
// 但是路径方向必须是向下的（只能从父节点到子节点）
// 测试链接 : https://leetcode.cn/problems/path-sum-iii/
//
// 解题思路:
// 1. 使用前缀和 + 哈希表的方法
// 2. 在深度优先搜索过程中，维护从根节点到当前节点路径上的节点值之和（前缀和）
// 3. 对于当前节点，查找之前路径上是否存在前缀和为 (当前前缀和 - targetSum) 的节点
//    如果存在，则说明存在一条从该节点到当前节点的路径，其和为 targetSum
// 4. 使用哈希表记录每个前缀和出现的次数
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度，哈希表最多存储h个元素
// 是否为最优解: 是，这是计算路径总和III的标准方法
public class Code07_PathSumIII {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static int pathSum(TreeNode root, int sum) {
		// 初始化前缀和哈希表，前缀和为0出现1次
		HashMap<Long, Integer> presum = new HashMap<>();
		presum.put(0L, 1);
		ans = 0;
		f(root, sum, 0, presum);
		return ans;
	}

	public static int ans;

	// sum : 从头节点出发，来到x的时候，上方累加和是多少
	// 路径必须以x作为结尾，路径累加和是target的路径数量，累加到全局变量ans上
	public static void f(TreeNode x, int target, long sum, HashMap<Long, Integer> presum) {
		if (x != null) {
			// 从头节点出发一路走到x的整体累加和
			sum += x.val;
			
			// 查找之前路径上是否存在前缀和为 (sum - target) 的节点
			// 如果存在，则说明存在一条从该节点到当前节点的路径，其和为target
			ans += presum.getOrDefault(sum - target, 0);
			
			// 更新前缀和哈希表
			presum.put(sum, presum.getOrDefault(sum, 0) + 1);
			
			// 递归处理左右子树
			f(x.left, target, sum, presum);
			f(x.right, target, sum, presum);
			
			// 回溯，恢复前缀和哈希表状态
			presum.put(sum, presum.get(sum) - 1);
		}
	}

}