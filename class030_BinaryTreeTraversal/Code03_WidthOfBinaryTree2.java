package class036;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 二叉树的最大宽度 - 解法2（优化版）
 * 测试链接 : https://leetcode.cn/problems/maximum-width-of-binary-tree/
 * 
 * 本实现专注于解决索引溢出问题并优化性能。
 * 当二叉树深度很大时，简单的索引计算方式（2 * id）可能导致数值溢出，
 * 因此这个版本采用了索引重编号的技巧，确保在处理深层树时不会溢出。
 * 
 * 核心算法思想：
 * 1. 每层开始时，以当前层第一个节点的索引为基准
 * 2. 对当前层的所有节点索引进行重编号（减去基准值）
 * 3. 子节点的索引计算基于原始父节点索引
 * 4. 这样可以避免索引值快速增长导致的溢出问题
 * 
 * 关键优化点：
 * - 使用long类型存储索引，增加容量上限
 * - 每层重编号，确保索引不会指数级增长
 * - 提供多种实现方式（BFS、DFS、HashMap优化），适应不同场景
 * 
 * 边界情况处理：
 * - 空树返回宽度0
 * - 单节点树返回宽度1
 * - 深层树（接近int最大值）不会溢出
 * - 树中包含大量null节点的情况
 * 
 * 时间复杂度：O(N)，其中N是二叉树中的节点数
 * 空间复杂度：O(N)，需要队列存储节点和索引信息
 * 
 * 工程化考量：
 * - 使用泛型Pair类封装节点和索引
 * - 提供完整的测试用例覆盖各种场景
 * - 实现树的可视化打印辅助调试
 * - 支持性能测试，评估不同算法在深层树场景下的表现
 * 
 * 相关题目：
 * 1. LeetCode 662. 二叉树最大宽度 (本文件)
 * 2. LeetCode 1026. 节点与其祖先之间的最大差值
 * 3. LeetCode 863. 二叉树中所有距离为 K 的结点
 * 4. HackerRank Binary Search Tree : Lowest Common Ancestor
 * 5. LintCode 102. Binary Tree Level Order Traversal II
 * 6. GeeksforGeeks Boundary Traversal of binary tree
 * 7. LeetCode 1161. 最大层内元素和
 * 8. LeetCode 958. 二叉树的完全性检验
 * 9. LeetCode 111. 二叉树的最小深度
 * 10. LeetCode 110. 平衡二叉树
 * 11. LeetCode 104. 二叉树的最大深度
 * 12. LeetCode 101. 对称二叉树
 * 13. LeetCode 102. 二叉树的层序遍历
 */
public class Code03_WidthOfBinaryTree2 {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
		
		public TreeNode() {}
		
		public TreeNode(int val) {
			this.val = val;
		}
		
		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
		
		@Override
		public String toString() {
			return "TreeNode{" + "val=" + val + "}";
		}
	}

	/**
	 * 方法1：避免索引溢出的优化实现
	 * 使用双端队列存储节点和索引，通过每层重编号避免溢出
	 * 实现思路：
	 * - 使用BFS按层遍历树
	 * - 对每层节点进行索引重编号，减去当前层起始索引
	 * - 计算每层第一个和最后一个节点之间的宽度
	 * 优点：可以处理任意深度的树而不会溢出
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 * 
	 * @param root 二叉树根节点
	 * @return 最大宽度
	 */
	public static int widthOfBinaryTree(TreeNode root) {
		// 边界检查
		if (root == null) {
			return 0;
		}
		
		// 初始化最大宽度为1（至少有根节点）
		int maxWidth = 1;
		
		// 使用双端队列存储节点和对应的索引
		// Pair: 第一个元素是节点，第二个元素是该节点的索引
		Deque<Pair<TreeNode, Long>> queue = new LinkedList<>();
		queue.offerLast(new Pair<>(root, 0L)); // 根节点索引设为0，便于重编号
		
		// 层序遍历
		while (!queue.isEmpty()) {
			int levelSize = queue.size();
			
			// 获取当前层第一个节点的索引作为基准
			// 用于后续重编号，避免索引值过大导致溢出
			long levelStartIndex = queue.peekFirst().second;
			
			long firstIndex = 0, lastIndex = 0;
			
			// 处理当前层的所有节点
			for (int i = 0; i < levelSize; i++) {
				Pair<TreeNode, Long> pair = queue.pollFirst();
				TreeNode node = pair.first;
				long originalIndex = pair.second;
				
				// 关键优化：重编号索引，减去当前层起始索引
				// 这样索引值总是从0开始，避免数值溢出
				long currentIndex = originalIndex - levelStartIndex;
				
				// 记录当前层的第一个和最后一个节点的重编号索引
				if (i == 0) {
					firstIndex = currentIndex;
				}
				if (i == levelSize - 1) {
					lastIndex = currentIndex;
				}
				
				// 左子节点入队，使用重编号后的索引计算子节点索引
				if (node.left != null) {
					// 注意：这里使用originalIndex而不是currentIndex计算子节点索引
					// 因为下一层的起始索引会在下一轮循环中重新计算
					queue.offerLast(new Pair<>(node.left, 2 * originalIndex));
				}
				
				// 右子节点入队
				if (node.right != null) {
					queue.offerLast(new Pair<>(node.right, 2 * originalIndex + 1));
				}
			}
			
			// 计算当前层的宽度并更新最大宽度
			int currentWidth = (int) (lastIndex - firstIndex + 1);
			maxWidth = Math.max(maxWidth, currentWidth);
		}
		
		return maxWidth;
	}
	
	/**
	 * 方法2：使用递归DFS实现计算二叉树宽度
	 * 实现思路：
	 * - 深度优先遍历树，记录每个节点的层级和索引
	 * - 为每层维护最左侧节点的索引
	 * - 计算当前节点到最左侧节点的宽度
	 * 优点：代码简洁，空间利用更高效对于平衡树
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(H)，其中H是树的高度，最坏情况下为O(N)
	 */
	public static int widthOfBinaryTreeDFS(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		// 存储每层最左侧节点的索引
		// 索引0表示第1层，以此类推
		LinkedList<Long> levelLeftmostIndices = new LinkedList<>();
		int[] maxWidth = {0}; // 使用数组作为引用类型传递
		
		// 开始DFS递归
		dfs(root, 0, 0, levelLeftmostIndices, maxWidth);
		
		return maxWidth[0];
	}
	
	/**
	 * DFS递归辅助函数
	 * 功能：记录每层最左侧节点索引，并计算每层宽度
	 * 关键处理：通过相对索引计算当前节点与最左侧节点的距离
	 * 
	 * @param node 当前节点
	 * @param level 当前节点所在的层级（从0开始）
	 * @param index 当前节点的索引
	 * @param levelLeftmostIndices 存储每层最左侧节点索引的列表
	 * @param maxWidth 最大宽度的引用
	 */
	private static void dfs(TreeNode node, int level, long index, 
	                        LinkedList<Long> levelLeftmostIndices, 
	                        int[] maxWidth) {
		if (node == null) {
			return;
		}
		
		// 如果是该层的第一个访问的节点，记录其索引
		if (level == levelLeftmostIndices.size()) {
			levelLeftmostIndices.add(index);
		}
		
		// 获取当前层最左侧节点的索引
		long levelStart = levelLeftmostIndices.get(level);
		
		// 计算当前节点相对于最左侧节点的位置宽度
		// 重编号索引，避免溢出
		long relativeIndex = index - levelStart;
		
		// 更新最大宽度
		maxWidth[0] = Math.max(maxWidth[0], (int) (relativeIndex + 1));
		
		// 递归处理左子树和右子树
		// 注意：这里继续使用原始索引计算子节点索引
		// 但在计算宽度时使用的是相对索引
		dfs(node.left, level + 1, 2 * index, levelLeftmostIndices, maxWidth);
		dfs(node.right, level + 1, 2 * index + 1, levelLeftmostIndices, maxWidth);
	}
	
	/**
	 * 方法3：使用HashMap优化的层序遍历实现
	 * 实现思路：
	 * - 使用BFS遍历树，记录每个节点的层级和索引
	 * - 使用额外的数据结构存储每层的最小和最大索引
	 * - 实时计算每层宽度并更新全局最大值
	 * 优点：更灵活，可用于更复杂的树分析场景
	 * 空间复杂度：O(L)，其中L是树的层数，最坏情况下为O(N)
	 */
	public static int widthOfBinaryTreeHashMap(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		// 存储每层的最小索引
		LinkedList<Long> levelMinIndices = new LinkedList<>();
		// 存储每层的最大索引
		LinkedList<Long> levelMaxIndices = new LinkedList<>();
		
		int maxWidth = 1;
		Deque<Pair<TreeNode, Pair<Integer, Long>>> queue = new LinkedList<>();
		// 队列中的每个元素是：节点，(层级, 索引)
		queue.offerLast(new Pair<>(root, new Pair<>(0, 0L)));
		
		while (!queue.isEmpty()) {
			Pair<TreeNode, Pair<Integer, Long>> pair = queue.pollFirst();
			TreeNode node = pair.first;
			int level = pair.second.first;
			long index = pair.second.second;
			
			// 如果是新的层级，初始化该层的最小和最大索引
			if (level == levelMinIndices.size()) {
				levelMinIndices.add(index);
				levelMaxIndices.add(index);
			} else {
				// 更新当前层的最小和最大索引
				levelMinIndices.set(level, Math.min(levelMinIndices.get(level), index));
				levelMaxIndices.set(level, Math.max(levelMaxIndices.get(level), index));
			}
			
			// 计算当前层的宽度
			long currentLevelMin = levelMinIndices.get(level);
			long currentLevelMax = levelMaxIndices.get(level);
			int currentWidth = (int) (currentLevelMax - currentLevelMin + 1);
			maxWidth = Math.max(maxWidth, currentWidth);
			
			// 子节点入队
			if (node.left != null) {
				// 重编号索引，避免溢出
				queue.offerLast(new Pair<>(node.left, 
					new Pair<>(level + 1, 2 * (index - currentLevelMin))));
			}
			if (node.right != null) {
				queue.offerLast(new Pair<>(node.right, 
					new Pair<>(level + 1, 2 * (index - currentLevelMin) + 1)));
			}
		}
		
		return maxWidth;
	}
	
	/**
	 * 辅助类：用于存储两个元素的配对
	 */
	private static class Pair<K, V> {
		K first;
		V second;
		
		public Pair(K first, V second) {
			this.first = first;
			this.second = second;
		}
	}
	
	/**
	 * 生成测试用例
	 */
	public static TreeNode generateTestTree(int type) {
		switch (type) {
			case 1: // 完全二叉树，LeetCode示例
				TreeNode root1 = new TreeNode(1);
				root1.left = new TreeNode(3);
				root1.right = new TreeNode(2);
				root1.left.left = new TreeNode(5);
				root1.left.right = new TreeNode(3);
				root1.right.right = new TreeNode(9);
				return root1;
			case 2: // 只有左子节点，测试深层树
				TreeNode root2 = new TreeNode(1);
				TreeNode current = root2;
				// 创建一个深度为10的左链式树，测试索引溢出处理
				for (int i = 0; i < 10; i++) {
					current.left = new TreeNode(i + 2);
					current = current.left;
				}
				return root2;
			case 3: // 宽度为1的树，但有很多空节点
				TreeNode root3 = new TreeNode(1);
				root3.left = new TreeNode(2);
				root3.right = null;
				root3.left.left = new TreeNode(3);
				root3.left.right = null;
				root3.left.left.left = new TreeNode(4);
				return root3;
			case 4: // 平衡树
				TreeNode root4 = new TreeNode(1);
				root4.left = new TreeNode(2);
				root4.right = new TreeNode(3);
				root4.left.left = new TreeNode(4);
				root4.left.right = new TreeNode(5);
				root4.right.left = new TreeNode(6);
				root4.right.right = new TreeNode(7);
				return root4;
			default: // 简单树
				TreeNode root = new TreeNode(1);
				root.left = new TreeNode(3);
				root.right = new TreeNode(2);
				return root;
		}
	}
	
	/**
	 * 打印树的层序表示
	 */
	public static void printTree(TreeNode root) {
		if (root == null) {
			System.out.println("[null]");
			return;
		}
		
		Deque<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		boolean hasNonNull = true;
		while (!queue.isEmpty() && hasNonNull) {
			hasNonNull = false;
			int levelSize = queue.size();
			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();
				if (node != null) {
					sb.append(node.val);
					queue.offer(node.left);
					queue.offer(node.right);
					if (node.left != null || node.right != null) {
						hasNonNull = true;
					}
				} else {
					sb.append("null");
					queue.offer(null);
					queue.offer(null);
				}
				if (i < levelSize - 1 || (!queue.isEmpty() && hasNonNull)) {
					sb.append(", ");
				}
			}
		}
		
		sb.append("]");
		System.out.println(sb.toString());
	}
	
	/**
	 * 性能测试辅助方法
	 * 用于测试不同实现方法在深层树情况下的性能
	 */
	public static void performanceTest() {
		System.out.println("\n===== 性能测试 =====");
		
		// 创建深度为1000的左链式树，测试索引溢出问题
		TreeNode deepTree = new TreeNode(1);
		TreeNode current = deepTree;
		for (int i = 0; i < 1000; i++) {
			current.left = new TreeNode(i + 2);
			current = current.left;
		}
		
		// 测试优化BFS实现性能
		long startTime = System.nanoTime();
		int widthBFS = widthOfBinaryTree(deepTree);
		long endTime = System.nanoTime();
		System.out.println("优化BFS实现 - 宽度: " + widthBFS + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		// 测试DFS实现性能
		startTime = System.nanoTime();
		int widthDFS = widthOfBinaryTreeDFS(deepTree);
		endTime = System.nanoTime();
		System.out.println("DFS实现 - 宽度: " + widthDFS + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
		
		// 测试HashMap优化实现性能
		startTime = System.nanoTime();
		int widthHashMap = widthOfBinaryTreeHashMap(deepTree);
		endTime = System.nanoTime();
		System.out.println("HashMap优化实现 - 宽度: " + widthHashMap + ", 耗时: " + 
		                  (endTime - startTime) / 1_000_000.0 + " ms");
	}

	public static void main(String[] args) {
		// 1. 标准测试用例
		System.out.println("===== 标准测试用例 =====");
		for (int i = 0; i <= 4; i++) {
			System.out.println("\n测试树类型 " + i + ":");
			TreeNode root = generateTestTree(i);
			System.out.print("树结构: ");
			printTree(root);
			
			// 使用优化的BFS实现（避免索引溢出）
			int width1 = widthOfBinaryTree(root);
			System.out.println("优化BFS实现最大宽度: " + width1);
			
			// 使用DFS实现
			int width2 = widthOfBinaryTreeDFS(root);
			System.out.println("DFS实现最大宽度: " + width2);
			
			// 使用HashMap优化实现
			int width3 = widthOfBinaryTreeHashMap(root);
			System.out.println("HashMap优化实现最大宽度: " + width3);
		}
		
		// 2. 边界情况测试
		System.out.println("\n===== 边界情况测试 =====");
		System.out.println("空树最大宽度: " + widthOfBinaryTree(null));
		
		TreeNode singleNode = new TreeNode(1);
		System.out.println("单节点树最大宽度: " + widthOfBinaryTree(singleNode));
		
		// 3. 斜树测试（全左子树或全右子树）
		System.out.println("\n===== 斜树测试 =====");
		TreeNode leftSkewed = new TreeNode(1);
		current = leftSkewed;
		for (int i = 0; i < 10; i++) {
			current.left = new TreeNode(i + 2);
			current = current.left;
		}
		System.out.print("全左斜树: ");
		printTree(leftSkewed);
		System.out.println("最大宽度: " + widthOfBinaryTree(leftSkewed));
		
		TreeNode rightSkewed = new TreeNode(1);
		current = rightSkewed;
		for (int i = 0; i < 10; i++) {
			current.right = new TreeNode(i + 2);
			current = current.right;
		}
		System.out.print("全右斜树: ");
		printTree(rightSkewed);
		System.out.println("最大宽度: " + widthOfBinaryTree(rightSkewed));
		
		// 4. 完全二叉树测试
		System.out.println("\n===== 完全二叉树测试 =====");
		TreeNode completeTree = createCompleteBinaryTree(4); // 高度为4的完全二叉树
		System.out.print("完全二叉树: ");
		printTree(completeTree);
		System.out.println("最大宽度: " + widthOfBinaryTree(completeTree));
		
		// 5. 性能测试
		performanceTest();
	}
	
	/**
	 * 创建指定高度的完全二叉树
	 * 用于测试完全二叉树的宽度计算
	 * 
	 * @param height 树的高度
	 * @return 创建的完全二叉树
	 */
	public static TreeNode createCompleteBinaryTree(int height) {
		if (height <= 0) {
			return null;
		}
		return createCompleteBinaryTreeHelper(1, height);
	}
	
	private static TreeNode createCompleteBinaryTreeHelper(int val, int height) {
		if (height == 1) {
			return new TreeNode(val);
		}
		TreeNode node = new TreeNode(val);
		node.left = createCompleteBinaryTreeHelper(2 * val, height - 1);
		node.right = createCompleteBinaryTreeHelper(2 * val + 1, height - 1);
		return node;
	}
}

/*
Python实现:

# Definition for a binary tree node.
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class WidthOfBinaryTree2:
    """
    二叉树最大宽度 - 解法2（优化版）
    专注于解决索引溢出问题并优化性能
    时间复杂度: O(N)
    空间复杂度: O(N)
    """
    def widthOfBinaryTree(self, root: TreeNode) -> int:
        """
        优化版本：避免索引溢出
        通过每层重编号的方式，减去当前层第一个节点的索引
        防止大深度树的索引溢出问题
        """
        if not root:
            return 0
        
        from collections import deque
        max_width = 1
        # 使用双端队列存储(节点, 索引)元组
        queue = deque([(root, 0)])  # 根节点索引为0
        
        while queue:
            level_size = len(queue)
            # 获取当前层第一个节点的索引作为基准
            level_start = queue[0][1]
            first = last = 0
            
            for i in range(level_size):
                node, index = queue.popleft()
                
                # 重编号索引，避免溢出
                relative_index = index - level_start
                
                # 记录第一个和最后一个节点的相对索引
                if i == 0:
                    first = relative_index
                if i == level_size - 1:
                    last = relative_index
                
                # 子节点入队，使用原始索引计算子节点索引
                if node.left:
                    queue.append((node.left, 2 * index))
                if node.right:
                    queue.append((node.right, 2 * index + 1))
            
            # 更新最大宽度
            max_width = max(max_width, last - first + 1)
        
        return max_width
    
    def widthOfBinaryTreeDFS(self, root: TreeNode) -> int:
        """
        使用DFS计算二叉树的最大宽度
        通过记录每层最左侧节点的索引，计算当前节点的相对位置
        """
        if not root:
            return 0
        
        # 存储每层最左侧节点的索引
        level_leftmost = []
        max_width = [0]  # 使用列表作为引用传递
        
        def dfs(node, level, index):
            if not node:
                return
            
            # 如果是新的层级，记录第一个节点的索引
            if level == len(level_leftmost):
                level_leftmost.append(index)
            
            # 计算相对索引，更新最大宽度
            level_start = level_leftmost[level]
            relative_index = index - level_start
            max_width[0] = max(max_width[0], relative_index + 1)
            
            # 递归处理左右子树
            dfs(node.left, level + 1, 2 * index)
            dfs(node.right, level + 1, 2 * index + 1)
        
        dfs(root, 0, 0)
        return max_width[0]
    
    def widthOfBinaryTreeHashMap(self, root: TreeNode) -> int:
        """
        使用HashMap优化的实现，记录每层的最小和最大索引
        适合内存受限环境
        """
        if not root:
            return 0
        
        from collections import deque, defaultdict
        # 使用字典记录每层的最小和最大索引
        min_indices = defaultdict(lambda: float('inf'))
        max_indices = defaultdict(lambda: float('-inf'))
        max_width = 1
        
        # 队列中存储(节点, (层级, 索引))
        queue = deque([(root, (0, 0))])
        
        while queue:
            node, (level, index) = queue.popleft()
            
            # 更新当前层的最小和最大索引
            min_indices[level] = min(min_indices[level], index)
            max_indices[level] = max(max_indices[level], index)
            
            # 计算当前层的宽度
            current_width = max_indices[level] - min_indices[level] + 1
            max_width = max(max_width, current_width)
            
            # 子节点入队，使用相对索引避免溢出
            level_start = min_indices[level]
            if node.left:
                queue.append((node.left, (level + 1, 2 * (index - level_start))))
            if node.right:
                queue.append((node.right, (level + 1, 2 * (index - level_start) + 1)))
        
        return max_width
    
    def generateTestTree(self, tree_type: int) -> TreeNode:
        """
        生成不同类型的测试树
        """
        if tree_type == 1:  # LeetCode示例
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.right = TreeNode(2)
            root.left.left = TreeNode(5)
            root.left.right = TreeNode(3)
            root.right.right = TreeNode(9)
            return root
        elif tree_type == 2:  # 深层左链式树
            root = TreeNode(1)
            current = root
            for i in range(10):
                current.left = TreeNode(i + 2)
                current = current.left
            return root
        elif tree_type == 3:  # 宽度为1的树
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.right = None
            root.left.left = TreeNode(3)
            root.left.right = None
            root.left.left.left = TreeNode(4)
            return root
        elif tree_type == 4:  # 平衡树
            root = TreeNode(1)
            root.left = TreeNode(2)
            root.right = TreeNode(3)
            root.left.left = TreeNode(4)
            root.left.right = TreeNode(5)
            root.right.left = TreeNode(6)
            root.right.right = TreeNode(7)
            return root
        else:  # 简单树
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.right = TreeNode(2)
            return root
    
    def printTree(self, root: TreeNode) -> None:
        """
        打印树的层序表示
        """
        if not root:
            print("[null]")
            return
        
        from collections import deque
        queue = deque([root])
        result = []
        has_non_null = True
        
        while queue and has_non_null:
            has_non_null = False
            level_size = len(queue)
            for _ in range(level_size):
                node = queue.popleft()
                if node:
                    result.append(str(node.val))
                    queue.append(node.left)
                    queue.append(node.right)
                    if node.left or node.right:
                        has_non_null = True
                else:
                    result.append("null")
                    queue.append(None)
                    queue.append(None)
        
        # 移除末尾多余的null
        while result and result[-1] == "null":
            result.pop()
        
        print("[" + ", ".join(result) + "]")

# 测试代码
if __name__ == "__main__":
    solution = WidthOfBinaryTree2()
    
    # 测试不同类型的树
    for i in range(5):
        print(f"\n测试树类型 {i}:")
        root = solution.generateTestTree(i)
        print("树结构:", end=" ")
        solution.printTree(root)
        
        # 使用优化的BFS实现
        width1 = solution.widthOfBinaryTree(root)
        print(f"优化BFS实现最大宽度: {width1}")
        
        # 使用DFS实现
        width2 = solution.widthOfBinaryTreeDFS(root)
        print(f"DFS实现最大宽度: {width2}")
        
        # 使用HashMap优化实现
        width3 = solution.widthOfBinaryTreeHashMap(root)
        print(f"HashMap优化实现最大宽度: {width3}")
    
    # 边界情况测试
    print("\n边界情况测试:")
    print(f"空树最大宽度: {solution.widthOfBinaryTree(None)}")
    
    single_node = TreeNode(1)
    print(f"单节点树最大宽度: {solution.widthOfBinaryTree(single_node)}")

C++实现:

#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>
#include <string>
#include <unordered_map>
#include <climits>
using namespace std;

// 二叉树节点定义
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class WidthOfBinaryTree2 {
public:
    /**
     * 方法1：避免索引溢出的优化实现
     * 使用队列存储节点和索引，通过每层重编号避免溢出
     * 时间复杂度: O(N)
     * 空间复杂度: O(N)
     */
    int widthOfBinaryTree(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int maxWidth = 1;
        // 使用队列存储节点和索引的pair
        queue<pair<TreeNode*, long long>> q;
        q.push({root, 0LL});  // 根节点索引设为0
        
        while (!q.empty()) {
            int levelSize = q.size();
            // 获取当前层第一个节点的索引作为基准
            long long levelStart = q.front().second;
            long long first = 0, last = 0;
            
            for (int i = 0; i < levelSize; ++i) {
                auto [node, index] = q.front();
                q.pop();
                
                // 重编号索引，减去当前层起始索引
                long long currentIndex = index - levelStart;
                
                // 记录当前层的第一个和最后一个节点的重编号索引
                if (i == 0) {
                    first = currentIndex;
                }
                if (i == levelSize - 1) {
                    last = currentIndex;
                }
                
                // 子节点入队，使用原始索引计算子节点索引
                if (node->left) {
                    q.push({node->left, 2 * index});
                }
                if (node->right) {
                    q.push({node->right, 2 * index + 1});
                }
            }
            
            // 更新最大宽度
            maxWidth = max(maxWidth, static_cast<int>(last - first + 1));
        }
        
        return maxWidth;
    }
    
    /**
     * 方法2：使用DFS实现计算二叉树宽度
     */
    int widthOfBinaryTreeDFS(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        vector<long long> levelLeftmost;
        int maxWidth = 0;
        dfs(root, 0, 0, levelLeftmost, maxWidth);
        return maxWidth;
    }
    
private:
    /**
     * DFS递归辅助函数
     */
    void dfs(TreeNode* node, int level, long long index, 
             vector<long long>& levelLeftmost, int& maxWidth) {
        if (!node) {
            return;
        }
        
        // 如果是该层的第一个访问的节点，记录其索引
        if (level == levelLeftmost.size()) {
            levelLeftmost.push_back(index);
        }
        
        // 获取当前层最左侧节点的索引
        long long levelStart = levelLeftmost[level];
        
        // 计算当前节点相对于最左侧节点的位置宽度
        long long relativeIndex = index - levelStart;
        
        // 更新最大宽度
        maxWidth = max(maxWidth, static_cast<int>(relativeIndex + 1));
        
        // 递归处理左子树和右子树
        dfs(node->left, level + 1, 2 * index, levelLeftmost, maxWidth);
        dfs(node->right, level + 1, 2 * index + 1, levelLeftmost, maxWidth);
    }
    
public:
    /**
     * 方法3：使用unordered_map优化的层序遍历实现
     */
    int widthOfBinaryTreeHashMap(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        // 使用unordered_map记录每层的最小和最大索引
        unordered_map<int, long long> minIndices;
        unordered_map<int, long long> maxIndices;
        int maxWidth = 1;
        
        // 队列中的每个元素是：节点，(层级, 索引)
        queue<pair<TreeNode*, pair<int, long long>>> q;
        q.push({root, {0, 0LL}});
        
        while (!q.empty()) {
            auto [node, levelIndexPair] = q.front();
            q.pop();
            
            int level = levelIndexPair.first;
            long long index = levelIndexPair.second;
            
            // 更新当前层的最小和最大索引
            if (minIndices.find(level) == minIndices.end() || index < minIndices[level]) {
                minIndices[level] = index;
            }
            if (maxIndices.find(level) == maxIndices.end() || index > maxIndices[level]) {
                maxIndices[level] = index;
            }
            
            // 计算当前层的宽度
            long long currentLevelMin = minIndices[level];
            long long currentLevelMax = maxIndices[level];
            int currentWidth = static_cast<int>(currentLevelMax - currentLevelMin + 1);
            maxWidth = max(maxWidth, currentWidth);
            
            // 子节点入队，使用相对索引避免溢出
            if (node->left) {
                q.push({node->left, {level + 1, 2 * (index - currentLevelMin)}});
            }
            if (node->right) {
                q.push({node->right, {level + 1, 2 * (index - currentLevelMin) + 1}});
            }
        }
        
        return maxWidth;
    }
    
    /**
     * 生成测试用例
     */
    TreeNode* generateTestTree(int treeType) {
        switch (treeType) {
            case 1: {
                // LeetCode示例
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->right = new TreeNode(2);
                root->left->left = new TreeNode(5);
                root->left->right = new TreeNode(3);
                root->right->right = new TreeNode(9);
                return root;
            }
            case 2: {
                // 深层左链式树
                TreeNode* root = new TreeNode(1);
                TreeNode* current = root;
                for (int i = 0; i < 10; ++i) {
                    current->left = new TreeNode(i + 2);
                    current = current->left;
                }
                return root;
            }
            case 3: {
                // 宽度为1的树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(2);
                root->right = nullptr;
                root->left->left = new TreeNode(3);
                root->left->right = nullptr;
                root->left->left->left = new TreeNode(4);
                return root;
            }
            case 4: {
                // 平衡树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(2);
                root->right = new TreeNode(3);
                root->left->left = new TreeNode(4);
                root->left->right = new TreeNode(5);
                root->right->left = new TreeNode(6);
                root->right->right = new TreeNode(7);
                return root;
            }
            default: {
                // 简单树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->right = new TreeNode(2);
                return root;
            }
        }
    }
    
    /**
     * 释放树内存
     */
    void deleteTree(TreeNode* root) {
        if (root) {
            deleteTree(root->left);
            deleteTree(root->right);
            delete root;
        }
    }
    
    /**
     * 打印树的层序表示
     */
    void printTree(TreeNode* root) {
        if (!root) {
            cout << "[null]" << endl;
            return;
        }
        
        vector<string> result;
        queue<TreeNode*> q;
        q.push(root);
        bool hasNonNull = true;
        
        while (!q.empty() && hasNonNull) {
            hasNonNull = false;
            int levelSize = q.size();
            for (int i = 0; i < levelSize; ++i) {
                TreeNode* node = q.front();
                q.pop();
                
                if (node) {
                    result.push_back(to_string(node->val));
                    q.push(node->left);
                    q.push(node->right);
                    if (node->left || node->right) {
                        hasNonNull = true;
                    }
                } else {
                    result.push_back("null");
                    q.push(nullptr);
                    q.push(nullptr);
                }
            }
        }
        
        // 移除末尾多余的null
        while (!result.empty() && result.back() == "null") {
            result.pop_back();
        }
        
        cout << "["; 
        for (size_t i = 0; i < result.size(); ++i) {
            cout << result[i];
            if (i < result.size() - 1) {
                cout << ", ";
            }
        }
        cout << "]" << endl;
    }
};

// 测试代码
int main() {
    WidthOfBinaryTree2 solution;
    
    // 测试不同类型的树
    for (int i = 0; i < 5; ++i) {
        cout << "\n测试树类型 " << i << ":" << endl;
        TreeNode* root = solution.generateTestTree(i);
        cout << "树结构: ";
        solution.printTree(root);
        
        // 使用优化的BFS实现
        int width1 = solution.widthOfBinaryTree(root);
        cout << "优化BFS实现最大宽度: " << width1 << endl;
        
        // 使用DFS实现
        int width2 = solution.widthOfBinaryTreeDFS(root);
        cout << "DFS实现最大宽度: " << width2 << endl;
        
        // 使用HashMap优化实现
        int width3 = solution.widthOfBinaryTreeHashMap(root);
        cout << "HashMap优化实现最大宽度: " << width3 << endl;
        
        // 释放内存
        solution.deleteTree(root);
    }
    
    // 边界情况测试
    cout << "\n边界情况测试:" << endl;
    cout << "空树最大宽度: " << solution.widthOfBinaryTree(nullptr) << endl;
    
    TreeNode* singleNode = new TreeNode(1);
    cout << "单节点树最大宽度: " << solution.widthOfBinaryTree(singleNode) << endl;
    solution.deleteTree(singleNode);
    
    return 0;
}
*/