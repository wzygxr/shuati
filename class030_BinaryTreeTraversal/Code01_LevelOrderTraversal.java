package class036;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树的层序遍历
 * 测试链接 : https://leetcode.cn/problems/binary-tree-level-order-traversal/
 * 
 * 层序遍历（Breadth-First Search, BFS）是二叉树的一种基本遍历方式，从根节点开始，逐层向下，同一层从左到右访问所有节点。
 * 相比于深度优先遍历（DFS），层序遍历更适合处理需要按层处理的问题，如分层收集节点值、计算每层的统计信息等。
 * 
 * 核心算法思想：
 * 1. 使用队列数据结构存储待访问的节点
 * 2. 每次从队列头部取出节点进行处理
 * 3. 将该节点的左右子节点加入队列尾部
 * 4. 通过记录每层的节点数量，可以精确分层收集结果
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(N)，其中N是二叉树中的节点数，每个节点只被访问一次
 * - 空间复杂度：O(W)，其中W是二叉树的最大宽度，最坏情况下（完全二叉树的最底层）为O(N/2)≈O(N)
 * 
 * 关键优化点：
 * 1. 优化版实现通过记录每层的节点数量，避免了使用额外的哈希表存储层级信息
 * 2. 使用数组实现队列可以进一步提升性能（本代码中的优化实现）
 * 3. 针对大数据量，可以动态调整MAXN或使用动态扩容的数据结构
 * 
 * 边界情况处理：
 * 1. 空树：直接返回空列表
 * 2. 单节点树：返回只包含一个列表的列表
 * 3. 斜树（链状结构）：仍然能正确处理，但空间复杂度退化为O(1)
 * 
 * 相关题目：
 * 1. LeetCode 102. 二叉树的层序遍历 (本文件) - https://leetcode.cn/problems/binary-tree-level-order-traversal/
 * 2. LeetCode 107. 二叉树的层序遍历 II - https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
 * 3. LeetCode 103. 二叉树的锯齿形层序遍历 - https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
 * 4. LeetCode 637. 二叉树的层平均值 - https://leetcode.cn/problems/average-of-levels-in-binary-tree/
 * 5. LeetCode 515. 在每个树行中找最大值 - https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
 * 6. LeetCode 429. N叉树的层序遍历 - https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
 * 7. LeetCode 116. 填充每个节点的下一个右侧节点指针 - https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
 * 8. LeetCode 117. 填充每个节点的下一个右侧节点指针 II - https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/
 * 9. LeetCode 199. 二叉树的右视图 - https://leetcode.cn/problems/binary-tree-right-side-view/
 * 10. LintCode 69. 二叉树的层次遍历 - https://www.lintcode.com/problem/69/
 * 11. HackerRank Tree: Level Order Traversal - https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
 * 12. UVA 122. Trees on the Level - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=58
 * 13. POJ 3278. Catch That Cow - https://poj.org/problem?id=3278 (BFS应用)
 * 14. Codeforces 1335B. Construct the String - https://codeforces.com/problemset/problem/1335/B
 * 15. AcWing 102. 最佳牛围栏 - https://www.acwing.com/problem/content/104/
 * 16. 牛客 NC15. 求二叉树的层序遍历 - https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3
 * 17. 剑指 Offer 32 - I. 从上到下打印二叉树 - https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof/
 * 18. 剑指 Offer 32 - II. 从上到下打印二叉树 II - https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-ii-lcof/
 * 
 * 工程化考量：
 * 1. 异常处理：在实际应用中，应考虑空指针异常和数据溢出问题
 * 2. 可配置性：MAXN参数可以根据实际需求调整或使用动态扩容机制
 * 3. 线程安全：多线程环境下需要额外的同步机制
 * 4. 内存管理：对于大树，需要注意内存使用情况，避免OOM
 */
public class Code01_LevelOrderTraversal {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;
		
		public TreeNode() {}
		public TreeNode(int val) { this.val = val; }
		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
		
		@Override
		public String toString() {
			return String.valueOf(val);
		}
	}

	/**
	 * 方法1：使用HashMap记录每个节点的层级 - 普通BFS实现
	 * 时间复杂度：O(N)，N为节点数，每个节点只被访问一次
	 * 空间复杂度：O(N)，需要队列和哈希表存储所有节点及其层级信息
	 * 优点：
	 * - 逻辑直观，易于理解
	 * - 不需要预先知道每层的节点数量
	 * 缺点：
	 * - 使用HashMap增加了额外的空间开销
	 * - 哈希表的访问有一定的常数时间开销
	 * 适用场景：
	 * - 树结构复杂，需要显式记录节点层级信息
	 * - 算法教学和解释场景
	 */
	public static List<List<Integer>> levelOrder1(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			Queue<TreeNode> queue = new LinkedList<>();
			HashMap<TreeNode, Integer> levels = new HashMap<>();
			queue.add(root);
			levels.put(root, 0);
			while (!queue.isEmpty()) {
				TreeNode cur = queue.poll();
				int level = levels.get(cur);
				// 如果当前层级还没有对应的列表，创建一个新的
				if (ans.size() == level) {
					ans.add(new ArrayList<>());
				}
				// 将当前节点的值添加到对应层级的列表中
				ans.get(level).add(cur.val);
				// 处理左子节点
				if (cur.left != null) {
					queue.add(cur.left);
					levels.put(cur.left, level + 1);
				}
				// 处理右子节点
				if (cur.right != null) {
					queue.add(cur.right);
					levels.put(cur.right, level + 1);
				}
			}
		}
		return ans;
	}

	// 如果测试数据量变大了就修改这个值
	// 注意：在工程实践中，可能需要动态扩容或使用更灵活的数据结构
	public static int MAXN = 2001;

	// 使用数组模拟队列，提高访问效率
	public static TreeNode[] queue = new TreeNode[MAXN];

	// 队列的左右指针
	public static int l, r;

	/**
	 * 方法2：每次处理一层的优化BFS实现 - 推荐解法
	 * 时间复杂度：O(N)，N为节点数，每个节点只被访问一次
	 * 空间复杂度：O(W)，W为树的最大宽度，最坏情况为O(N/2)≈O(N)
	 * 优点：
	 * 1. 不需要额外的HashMap记录层级信息，节省空间
	 * 2. 内存效率更高，使用数组实现队列进一步提升性能
	 * 3. 更符合层序遍历的直观理解，逻辑清晰
	 * 4. 常数因子更小，实际运行速度更快
	 * 核心思想：
	 * - 通过记录每层开始时的队列大小，确保每次处理的都是同一层的所有节点
	 * - 每处理完一层，收集该层的所有节点值，然后再处理下一层
	 * 优化点：
	 * - 使用数组模拟队列而非LinkedList，减少链表节点的创建开销
	 */
	public static List<List<Integer>> levelOrder2(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			while (l < r) { // 队列里还有东西
				int size = r - l; // 当前层的节点数量
				ArrayList<Integer> list = new ArrayList<Integer>();
				// 处理当前层的所有节点
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					list.add(cur.val);
					// 将子节点加入队列，供下一层处理
					if (cur.left != null) {
						queue[r++] = cur.left;
					}
					if (cur.right != null) {
						queue[r++] = cur.right;
					}
				}
				// 将当前层的结果添加到最终答案中
				ans.add(list);
			}
		}
		return ans;
	}

	/**
	 * LeetCode 107. 二叉树的层序遍历 II
	 * 测试链接: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
	 * 描述: 自底向上的层序遍历，从叶子节点层开始向上访问，与常规层序遍历顺序相反
	 * 时间复杂度: O(N)，其中N为节点数，额外的反转操作是O(L)，L为层数，L≤N
	 * 空间复杂度: O(N)，存储所有节点值和队列
	 * 实现思路：
	 * 1. 先执行常规的层序遍历，从根到叶收集每层节点
	 * 2. 最后反转结果列表，得到从叶到根的顺序
	 * 注意事项：
	 * - 反转操作只影响结果的顺序，不影响遍历过程
	 * - 对于非常深的树，反转操作的时间开销可以忽略不计
	 */
	public static List<List<Integer>> levelOrderBottom(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			while (l < r) {
				int size = r - l;
				ArrayList<Integer> list = new ArrayList<>();
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					list.add(cur.val);
					if (cur.left != null) queue[r++] = cur.left;
					if (cur.right != null) queue[r++] = cur.right;
				}
				ans.add(list);
			}
			// 反转结果，得到自底向上的遍历
			for (int i = 0, j = ans.size() - 1; i < j; i++, j--) {
				List<Integer> temp = ans.get(i);
				ans.set(i, ans.get(j));
				ans.set(j, temp);
			}
		}
		return ans;
	}

	/**
	 * LeetCode 637. 二叉树的层平均值
	 * 测试链接: https://leetcode.cn/problems/average-of-levels-in-binary-tree/
	 * 描述: 计算二叉树每一层节点的平均值
	 * 时间复杂度: O(N)，每个节点只被访问一次
	 * 空间复杂度: O(W)，W为树的最大宽度
	 * 实现细节：
	 * 1. 使用double类型存储总和，避免整数除法精度问题
	 * 2. 每层单独计算总和，不影响其他层的处理
	 * 边界考虑：
	 * - 对于每层只有一个节点的情况，平均值就是该节点的值
	 * - 对于空树，直接返回空列表
	 */
	public static List<Double> averageOfLevels(TreeNode root) {
		List<Double> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			while (l < r) {
				int size = r - l;
				double sum = 0;
				// 计算当前层的总和
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					sum += cur.val;
					if (cur.left != null) queue[r++] = cur.left;
					if (cur.right != null) queue[r++] = cur.right;
				}
				// 计算平均值并添加到结果中
				ans.add(sum / size);
			}
		}
		return ans;
	}

	/**
	 * LeetCode 515. 在每个树行中找最大值
	 * 测试链接: https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
	 * 描述: 找出二叉树每一层中的最大值
	 * 时间复杂度: O(N)，每个节点只被访问一次
	 * 空间复杂度: O(W)，W为树的最大宽度
	 * 实现思路：
	 * 1. 初始化每层的最大值为Integer.MIN_VALUE
	 * 2. 遍历当前层的所有节点，更新最大值
	 * 3. 每层遍历结束后，将最大值加入结果列表
	 * 优化点：
	 * - 可以提前终止遍历，如果当前层只有一个节点，则该节点值即为最大值
	 */
	public static List<Integer> largestValues(TreeNode root) {
		List<Integer> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			while (l < r) {
				int size = r - l;
				int max = Integer.MIN_VALUE;
				// 找出当前层的最大值
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					max = Math.max(max, cur.val);
					if (cur.left != null) queue[r++] = cur.left;
					if (cur.right != null) queue[r++] = cur.right;
				}
				ans.add(max);
			}
		}
		return ans;
	}

	/**
	 * 辅助方法：根据数组生成测试树
	 * 数组格式：层序遍历，null表示空节点
	 * 例如：[3,9,20,null,null,15,7] 表示示例中的树
	 */
	public static TreeNode generateTree(Integer[] arr) {
		if (arr == null || arr.length == 0 || arr[0] == null) {
			return null;
		}
		
		TreeNode root = new TreeNode(arr[0]);
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int i = 1;
		
		while (!queue.isEmpty() && i < arr.length) {
			TreeNode current = queue.poll();
			
			// 添加左子节点
			if (i < arr.length && arr[i] != null) {
				current.left = new TreeNode(arr[i]);
				queue.offer(current.left);
			}
			i++;
			
			// 添加右子节点
			if (i < arr.length && arr[i] != null) {
				current.right = new TreeNode(arr[i]);
				queue.offer(current.right);
			}
			i++;
		}
		
		return root;
	}
	
	/**
	 * 辅助方法：打印树的结构（层序方式）
	 * 用于可视化测试树，便于调试
	 */
	public static void printTree(TreeNode root) {
		if (root == null) {
			System.out.println("Empty Tree");
			return;
		}
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		List<List<String>> levels = new ArrayList<>();
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<String> level = new ArrayList<>();
			boolean hasNonNull = false;
			
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (node != null) {
					level.add(String.valueOf(node.val));
					queue.offer(node.left);
					queue.offer(node.right);
					if (node.left != null || node.right != null) {
						hasNonNull = true;
					}
				} else {
					level.add("null");
					queue.offer(null);
					queue.offer(null);
				}
			}
			
			if (!hasNonNull && level.stream().allMatch("null"::equals)) {
				break;
			}
			levels.add(level);
		}
		
		// 打印每层
		for (List<String> level : levels) {
			System.out.println(level);
		}
	}
	
	/**
	 * 主方法用于测试
	 * 包含多种测试用例，覆盖常见场景和边界情况
	 */
	public static void main(String[] args) {
		// 测试用例1：标准二叉树
		System.out.println("========== 测试用例1：标准二叉树 ==========");
		Integer[] arr1 = {3, 9, 20, null, null, 15, 7};
		TreeNode root1 = generateTree(arr1);
		System.out.println("树结构：");
		printTree(root1);
		
		System.out.println("\n方法1 层序遍历:");
		for (List<Integer> level : levelOrder1(root1)) {
			System.out.println(level);
		}
		
		System.out.println("\n方法2 层序遍历 (优化版):");
		for (List<Integer> level : levelOrder2(root1)) {
			System.out.println(level);
		}
		
		System.out.println("\n自底向上层序遍历:");
		for (List<Integer> level : levelOrderBottom(root1)) {
			System.out.println(level);
		}
		
		System.out.println("\n层平均值:");
		System.out.println(averageOfLevels(root1));
		
		System.out.println("\n每层最大值:");
		System.out.println(largestValues(root1));
		
		// 测试用例2：空树
		System.out.println("\n\n========== 测试用例2：空树 ==========");
		TreeNode root2 = null;
		System.out.println("层序遍历:");
		System.out.println(levelOrder2(root2));
		
		// 测试用例3：单节点树
		System.out.println("\n\n========== 测试用例3：单节点树 ==========");
		TreeNode root3 = new TreeNode(1);
		System.out.println("层序遍历:");
		System.out.println(levelOrder2(root3));
		
		// 测试用例4：斜树（链状结构）
		System.out.println("\n\n========== 测试用例4：斜树 ==========");
		Integer[] arr4 = {1, 2, null, 3, null, 4, null};
		TreeNode root4 = generateTree(arr4);
		System.out.println("树结构：");
		printTree(root4);
		System.out.println("层序遍历:");
		System.out.println(levelOrder2(root4));
		
		// 测试用例5：完全二叉树
		System.out.println("\n\n========== 测试用例5：完全二叉树 ==========");
		Integer[] arr5 = {1, 2, 3, 4, 5, 6, 7};
		TreeNode root5 = generateTree(arr5);
		System.out.println("树结构：");
		printTree(root5);
		System.out.println("层序遍历:");
		System.out.println(levelOrder2(root5));
		
		// 性能测试说明
		System.out.println("\n\n========== 性能对比说明 ==========");
		System.out.println("1. 方法1（HashMap记录层级）：逻辑清晰，但有额外的哈希表开销");
		System.out.println("2. 方法2（数组队列优化版）：性能更好，内存效率更高，推荐在实际应用中使用");
		System.out.println("3. 对于大数据量，可能需要调整MAXN参数或使用动态扩容的队列实现");
	}
}

/*
Python实现:

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class LevelOrderTraversal:
    # 方法1：使用HashMap记录层级
    def levelOrder1(self, root: TreeNode) -> list[list[int]]:
        ans = []
        if root:
            from collections import deque, defaultdict
            queue = deque([root])
            levels = defaultdict(int)
            levels[root] = 0
            while queue:
                cur = queue.popleft()
                level = levels[cur]
                if len(ans) == level:
                    ans.append([])
                ans[level].append(cur.val)
                if cur.left:
                    queue.append(cur.left)
                    levels[cur.left] = level + 1
                if cur.right:
                    queue.append(cur.right)
                    levels[cur.right] = level + 1
        return ans
    
    # 方法2：每次处理一层的优化BFS实现
    def levelOrder2(self, root: TreeNode) -> list[list[int]]:
        ans = []
        if root:
            from collections import deque
            queue = deque([root])
            while queue:
                size = len(queue)
                level = []
                for _ in range(size):
                    cur = queue.popleft()
                    level.append(cur.val)
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                ans.append(level)
        return ans
    
    # LeetCode 107: 自底向上层序遍历
    def levelOrderBottom(self, root: TreeNode) -> list[list[int]]:
        ans = []
        if root:
            from collections import deque
            queue = deque([root])
            while queue:
                size = len(queue)
                level = []
                for _ in range(size):
                    cur = queue.popleft()
                    level.append(cur.val)
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                ans.append(level)
            # 反转结果列表
            ans.reverse()
        return ans
    
    # LeetCode 637: 层平均值
    def averageOfLevels(self, root: TreeNode) -> list[float]:
        ans = []
        if root:
            from collections import deque
            queue = deque([root])
            while queue:
                size = len(queue)
                level_sum = 0
                for _ in range(size):
                    cur = queue.popleft()
                    level_sum += cur.val
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                ans.append(level_sum / size)
        return ans
    
    # LeetCode 515: 每层最大值
    def largestValues(self, root: TreeNode) -> list[int]:
        ans = []
        if root:
            from collections import deque
            queue = deque([root])
            while queue:
                size = len(queue)
                max_val = float('-inf')
                for _ in range(size):
                    cur = queue.popleft()
                    max_val = max(max_val, cur.val)
                    if cur.left:
                        queue.append(cur.left)
                    if cur.right:
                        queue.append(cur.right)
                ans.append(max_val)
        return ans

# 测试代码
if __name__ == "__main__":
    #       3
    #      / \
    #     9  20
    #       /  \
    #      15   7
    root = TreeNode(3)
    root.left = TreeNode(9)
    root.right = TreeNode(20)
    root.right.left = TreeNode(15)
    root.right.right = TreeNode(7)
    
    solution = LevelOrderTraversal()
    print("普通层序遍历:")
    print(solution.levelOrder2(root))
    
    print("\n自底向上层序遍历:")
    print(solution.levelOrderBottom(root))
    
    print("\n层平均值:")
    print(solution.averageOfLevels(root))
    
    print("\n每层最大值:")
    print(solution.largestValues(root))

C++实现:

#include <iostream>
#include <vector>
#include <queue>
#include <unordered_map>
#include <algorithm>
#include <climits>
using namespace std;

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class LevelOrderTraversal {
public:
    // 方法1：使用unordered_map记录层级
    vector<vector<int>> levelOrder1(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            unordered_map<TreeNode*, int> levels;
            q.push(root);
            levels[root] = 0;
            while (!q.empty()) {
                TreeNode* cur = q.front();
                q.pop();
                int level = levels[cur];
                if (ans.size() == level) {
                    ans.push_back({});
                }
                ans[level].push_back(cur->val);
                if (cur->left) {
                    q.push(cur->left);
                    levels[cur->left] = level + 1;
                }
                if (cur->right) {
                    q.push(cur->right);
                    levels[cur->right] = level + 1;
                }
            }
        }
        return ans;
    }
    
    // 方法2：每次处理一层的优化BFS实现
    vector<vector<int>> levelOrder2(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            while (!q.empty()) {
                int size = q.size();
                vector<int> level;
                for (int i = 0; i < size; ++i) {
                    TreeNode* cur = q.front();
                    q.pop();
                    level.push_back(cur->val);
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                ans.push_back(level);
            }
        }
        return ans;
    }
    
    // LeetCode 107: 自底向上层序遍历
    vector<vector<int>> levelOrderBottom(TreeNode* root) {
        vector<vector<int>> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            while (!q.empty()) {
                int size = q.size();
                vector<int> level;
                for (int i = 0; i < size; ++i) {
                    TreeNode* cur = q.front();
                    q.pop();
                    level.push_back(cur->val);
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                ans.push_back(level);
            }
            // 反转结果
            reverse(ans.begin(), ans.end());
        }
        return ans;
    }
    
    // LeetCode 637: 层平均值
    vector<double> averageOfLevels(TreeNode* root) {
        vector<double> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            while (!q.empty()) {
                int size = q.size();
                double sum = 0;
                for (int i = 0; i < size; ++i) {
                    TreeNode* cur = q.front();
                    q.pop();
                    sum += cur->val;
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                ans.push_back(sum / size);
            }
        }
        return ans;
    }
    
    // LeetCode 515: 每层最大值
    vector<int> largestValues(TreeNode* root) {
        vector<int> ans;
        if (root) {
            queue<TreeNode*> q;
            q.push(root);
            while (!q.empty()) {
                int size = q.size();
                int max_val = INT_MIN;
                for (int i = 0; i < size; ++i) {
                    TreeNode* cur = q.front();
                    q.pop();
                    max_val = max(max_val, cur->val);
                    if (cur->left) q.push(cur->left);
                    if (cur->right) q.push(cur->right);
                }
                ans.push_back(max_val);
            }
        }
        return ans;
    }
    
    // 辅助函数：打印二维向量
    void printVector(const vector<vector<int>>& vec) {
        for (const auto& v : vec) {
            cout << "[";
            for (size_t i = 0; i < v.size(); ++i) {
                cout << v[i];
                if (i < v.size() - 1) cout << ", ";
            }
            cout << "]" << endl;
        }
    }
    
    // 辅助函数：打印一维向量
    template<typename T>
    void printVector(const vector<T>& vec) {
        cout << "[";
        for (size_t i = 0; i < vec.size(); ++i) {
            cout << vec[i];
            if (i < vec.size() - 1) cout << ", ";
        }
        cout << "]" << endl;
    }
};

// 测试代码
int main() {
    //       3
    //      / \
    //     9  20
    //       /  \
    //      15   7
    TreeNode* root = new TreeNode(3);
    root->left = new TreeNode(9);
    root->right = new TreeNode(20);
    root->right->left = new TreeNode(15);
    root->right->right = new TreeNode(7);
    
    LevelOrderTraversal solution;
    
    cout << "普通层序遍历:" << endl;
    solution.printVector(solution.levelOrder2(root));
    
    cout << "\n自底向上层序遍历:" << endl;
    solution.printVector(solution.levelOrderBottom(root));
    
    cout << "\n层平均值:" << endl;
    solution.printVector(solution.averageOfLevels(root));
    
    cout << "\n每层最大值:" << endl;
    solution.printVector(solution.largestValues(root));
    
    // 释放内存
    delete root->right->right;
    delete root->right->left;
    delete root->right;
    delete root->left;
    delete root;
    
    return 0;
}
*/
