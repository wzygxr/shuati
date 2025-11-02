package class078;

// 在二叉树中分配硬币
// 给你一个有 n 个结点的二叉树的根结点 root
// 其中树中每个结点 node 都对应有 node.val 枚硬币
// 整棵树上一共有 n 枚硬币
// 在一次移动中，我们可以选择两个相邻的结点，然后将一枚硬币从其中一个结点移动到另一个结点
// 移动可以是从父结点到子结点，或者从子结点移动到父结点
// 返回使每个结点上 只有 一枚硬币所需的 最少 移动次数
// 测试链接 : https://leetcode.cn/problems/distribute-coins-in-binary-tree/
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 以该节点为根的子树中的节点数
//    - 以该节点为根的子树中的硬币数
//    - 使该子树每个节点都有一枚硬币所需的最少移动次数
// 3. 递归处理左右子树，综合计算当前节点的信息
// 4. 对于每个节点，需要从子树中移出或移入硬币，移动次数等于左右子树需要移出或移入的硬币数的绝对值之和
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点
// 空间复杂度: O(h) - h为树的高度，递归调用栈的深度
// 是否为最优解: 是，这是计算分配硬币最少移动次数的标准方法
public class Code04_DistributeCoins {

	// 不要提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
	}

	// 提交如下的方法
	public static int distributeCoins(TreeNode root) {
		return f(root).move;
	}

	public static class Info {
		// 以该节点为根的子树中的节点数
		public int cnt;
		// 以该节点为根的子树中的硬币数
		public int sum;
		// 使该子树每个节点都有一枚硬币所需的最少移动次数
		public int move;

		public Info(int a, int b, int c) {
			cnt = a;
			sum = b;
			move = c;
		}
	}

	public static Info f(TreeNode x) {
		// 基本情况：空节点
		if (x == null) {
			// 空树节点数为0，硬币数为0，移动次数为0
			return new Info(0, 0, 0);
		}
		
		// 递归处理左右子树
		Info infol = f(x.left);
		Info infor = f(x.right);
		
		// 计算当前子树的信息
		// 当前子树节点数 = 左子树节点数 + 右子树节点数 + 1
		int cnts = infol.cnt + infor.cnt + 1;
		// 当前子树硬币数 = 左子树硬币数 + 右子树硬币数 + 当前节点硬币数
		int sums = infol.sum + infor.sum + x.val;
		// 当前子树移动次数 = 左子树移动次数 + 右子树移动次数 + 
		//                  从左子树移出/移入硬币的次数 + 从右子树移出/移入硬币的次数
		// 需要移出/移入的硬币数 = |子树节点数 - 子树硬币数|
		int moves = infol.move + infor.move + Math.abs(infol.cnt - infol.sum) + Math.abs(infor.cnt - infor.sum);
		
		// 返回当前节点的信息
		return new Info(cnts, sums, moves);
	}

}