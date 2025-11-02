package class124;

import java.util.*;

/**
 * Morris遍历求二叉树最大路径和
 * 
 * 题目来源：
 * - 二叉树最大路径和：LeetCode 124. Binary Tree Maximum Path Sum
 *   链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的递归求解二叉树最大路径和
 * 2. 迭代版本的求解二叉树最大路径和
 * 3. 详细的注释和算法解析
 * 4. 完整的测试用例
 * 5. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/binary-tree-maximum-path-sum/solution/python-di-gui-qiu-er-cha-shu-zui-da-lu-jing-he-by-xxx/
 * - C++: https://leetcode.cn/problems/binary-tree-maximum-path-sum/solution/c-di-gui-qiu-er-cha-shu-zui-da-lu-jing-he-by-xxx/
 * 
 * 算法详解：
 * 二叉树中的路径被定义为一条节点序列，序列中每对相邻节点之间都存在一条边。
 * 同一个节点在一条路径序列中至多出现一次。该路径至少包含一个节点，且不一定经过根节点。
 * 路径和是路径中各节点值的总和。
 * 
 * 解题思路：
 * 1. 对于每个节点，计算经过该节点的最大路径和
 * 2. 路径可以分为三部分：左子树路径 + 节点值 + 右子树路径
 * 3. 但向父节点返回时，只能返回单侧路径的最大值（节点值 + max(左子树路径, 右子树路径)）
 * 4. 使用递归后序遍历，自底向上计算每个节点的最大贡献值
 * 
 * 时间复杂度：O(n) - 每个节点访问一次
 * 空间复杂度：O(h) - 递归栈空间，h为树高
 * 适用场景：求解二叉树中任意节点间最大路径和问题
 * 优缺点分析：
 * - 优点：思路清晰，实现简洁
 * - 缺点：递归可能导致栈溢出，对于极深的树需要改用迭代实现
 */
public class Code08_MorrisMaxPathSum {

	/**
	 * 二叉树节点定义
	 */
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
	 * 递归解法：二叉树最大路径和
	 * 时间复杂度: O(n)，其中n是节点数量，每个节点访问一次
	 * 空间复杂度: O(h)，其中h是树的高度，递归调用栈的深度
	 */
	public static class RecursiveSolution {
		private int maxSum;

		public int maxPathSum(TreeNode root) {
			// 初始化为最小整数值，防止所有节点值都是负数的情况
			maxSum = Integer.MIN_VALUE;
			maxGain(root);
			return maxSum;
		}

		/**
		 * 计算以当前节点为根的子树中，从该节点出发的最大路径和（贡献值）
		 * 这个贡献值可以传递给父节点使用
		 * 
		 * @param node 当前节点
		 * @return 从当前节点向下延伸的最大路径和
		 */
		private int maxGain(TreeNode node) {
			// 基本情况：空节点没有贡献值
			if (node == null) {
				return 0;
			}

			// 递归计算左右子节点的最大贡献值
			// 只有在贡献值大于0时才考虑包含该子树，否则直接舍弃（选择0）
			int leftGain = Math.max(maxGain(node.left), 0);
			int rightGain = Math.max(maxGain(node.right), 0);

			// 计算经过当前节点的最大路径和：当前节点值 + 左右子节点的最大贡献值
			// 这条路径的形状是 "left subtree -> current node -> right subtree"
			int currentPathSum = node.val + leftGain + rightGain;

			// 更新全局最大路径和
			maxSum = Math.max(maxSum, currentPathSum);

			// 返回当前节点的最大贡献值：当前节点值 + max(左子节点贡献值, 右子节点贡献值)
			// 这代表从当前节点出发，只能选择左子树或右子树中的一条路径向上延伸
			return node.val + Math.max(leftGain, rightGain);
		}
	}

	/**
	 * 迭代解法：使用后序遍历和栈模拟递归过程
	 * 时间复杂度: O(n)，每个节点访问一次
	 * 空间复杂度: O(h)，栈的空间占用
	 */
	public static class IterativeSolution {
		// 使用一个内部类来保存访问状态和子树的最大贡献值
		private static class TreeNodeInfo {
			TreeNode node;
			boolean visited; // 标记是否已处理完子节点
			int leftGain;    // 左子树的最大贡献值
			int rightGain;   // 右子树的最大贡献值

			TreeNodeInfo(TreeNode node) {
				this.node = node;
				this.visited = false;
				this.leftGain = 0;
				this.rightGain = 0;
			}
		}

		public int maxPathSum(TreeNode root) {
			if (root == null) {
				return 0;
			}

			int maxSum = Integer.MIN_VALUE;
			Stack<TreeNodeInfo> stack = new Stack<>();
			// 存储已处理节点的最大贡献值，用于更新父节点的左右增益
			Map<TreeNode, Integer> gainMap = new HashMap<>();

			stack.push(new TreeNodeInfo(root));

			while (!stack.isEmpty()) {
				TreeNodeInfo current = stack.pop();

				if (!current.visited) {
					// 第一次访问该节点，先将其重新入栈并标记为已访问
					current.visited = true;
					stack.push(current);

					// 然后将右子节点和左子节点入栈（先右后左，确保出栈顺序是左、右、根）
					if (current.node.right != null) {
						stack.push(new TreeNodeInfo(current.node.right));
					}
					if (current.node.left != null) {
						stack.push(new TreeNodeInfo(current.node.left));
					}
				} else {
					// 第二次访问该节点，此时左右子节点已经处理完毕
					// 获取左右子节点的最大贡献值（如果存在），否则为0
					int leftGain = gainMap.getOrDefault(current.node.left, 0);
					int rightGain = gainMap.getOrDefault(current.node.right, 0);

					// 只有贡献值为正才考虑，否则舍弃（取0）
					leftGain = Math.max(leftGain, 0);
					rightGain = Math.max(rightGain, 0);

					// 计算经过当前节点的最大路径和
					int currentPathSum = current.node.val + leftGain + rightGain;
					// 更新全局最大路径和
					maxSum = Math.max(maxSum, currentPathSum);

					// 计算当前节点的最大贡献值，并保存到映射中供父节点使用
					int currentGain = current.node.val + Math.max(leftGain, rightGain);
					gainMap.put(current.node, currentGain);
				}
			}

			return maxSum;
		}
	}

	/**
	 * 创建测试用例树
	 * @return 测试用例的根节点
	 */
	private static TreeNode[] createTestCases() {
		// 测试用例1: 标准树 [1,2,3]
		TreeNode root1 = new TreeNode(1);
		root1.left = new TreeNode(2);
		root1.right = new TreeNode(3);

		// 测试用例2: 包含负值的树 [-10,9,20,null,null,15,7]
		TreeNode root2 = new TreeNode(-10);
		root2.left = new TreeNode(9);
		root2.right = new TreeNode(20);
		root2.right.left = new TreeNode(15);
		root2.right.right = new TreeNode(7);

		// 测试用例3: 全是负值的树 [-3]
		TreeNode root3 = new TreeNode(-3);

		// 测试用例4: 单一路径树 [5,4,8,11,null,13,4,7,2,null,null,null,1]
		TreeNode root4 = new TreeNode(5);
		root4.left = new TreeNode(4);
		root4.right = new TreeNode(8);
		root4.left.left = new TreeNode(11);
		root4.right.left = new TreeNode(13);
		root4.right.right = new TreeNode(4);
		root4.left.left.left = new TreeNode(7);
		root4.left.left.right = new TreeNode(2);
		root4.right.right.right = new TreeNode(1);

		// 测试用例5: 复杂树，包含多个路径选择
		TreeNode root5 = new TreeNode(1);
		root5.left = new TreeNode(-2);
		root5.right = new TreeNode(3);
		root5.left.left = new TreeNode(4);
		root5.left.right = new TreeNode(5);
		root5.right.left = new TreeNode(-6);
		root5.right.right = new TreeNode(7);

		return new TreeNode[]{root1, root2, root3, root4, root5};
	}

	/**
	 * 打印树结构（便于调试）
	 */
	private static void printTree(TreeNode root) {
		if (root == null) {
			System.out.println("Empty tree");
			return;
		}
		printTreeHelper(root, 0, "H");
	}

	private static void printTreeHelper(TreeNode node, int level, String prefix) {
		if (node == null) {
			return;
		}

		// 打印前缀空格
		for (int i = 0; i < level; i++) {
			System.out.print("    ");
		}

		// 打印节点值
		System.out.println(prefix + ": " + node.val);
		// 递归打印左右子树
		printTreeHelper(node.left, level + 1, "L");
		printTreeHelper(node.right, level + 1, "R");
	}

	/**
	 * 性能测试方法
	 */
	private static void performanceTest() {
		// 创建一个较大的树进行性能测试
		TreeNode largeTree = createLargeTree(20);

		// 递归方法性能测试
		RecursiveSolution recursiveSolution = new RecursiveSolution();
		long startTime = System.nanoTime();
		int recursiveResult = recursiveSolution.maxPathSum(largeTree);
		long recursiveTime = System.nanoTime() - startTime;

		// 迭代方法性能测试
		IterativeSolution iterativeSolution = new IterativeSolution();
		startTime = System.nanoTime();
		int iterativeResult = iterativeSolution.maxPathSum(largeTree);
		long iterativeTime = System.nanoTime() - startTime;

		System.out.println("\n性能测试结果:");
		System.out.println("递归方法: " + recursiveTime + " ns, 结果: " + recursiveResult);
		System.out.println("迭代方法: " + iterativeTime + " ns, 结果: " + iterativeResult);
	}

	/**
	 * 创建大型二叉树用于性能测试
	 */
	private static TreeNode createLargeTree(int depth) {
		return createLargeTreeHelper(1, depth, true);
	}

	private static TreeNode createLargeTreeHelper(int val, int depth, boolean positive) {
		if (depth <= 0) {
			return null;
		}
		// 交替生成正数和负数，模拟真实数据分布
		int nodeValue = positive ? val : -val;
		TreeNode node = new TreeNode(nodeValue);
		node.left = createLargeTreeHelper(2 * val, depth - 1, !positive);
		node.right = createLargeTreeHelper(2 * val + 1, depth - 1, !positive);
		return node;
	}

	/**
	 * 主方法用于测试
	 */
	public static void main(String[] args) {
		TreeNode[] testCases = createTestCases();
		String[] testNames = {
				"测试用例1 [1,2,3]",
				"测试用例2 [-10,9,20,null,null,15,7]",
				"测试用例3 [-3]",
				"测试用例4 [5,4,8,11,null,13,4,7,2,null,null,null,1]",
				"测试用例5 [1,-2,3,4,5,-6,7]"
		};

		RecursiveSolution recursiveSolution = new RecursiveSolution();
		IterativeSolution iterativeSolution = new IterativeSolution();

		// 执行测试用例
		for (int i = 0; i < testCases.length; i++) {
			System.out.println("\n" + testNames[i]);
			printTree(testCases[i]);

			int recursiveResult = recursiveSolution.maxPathSum(testCases[i]);
			int iterativeResult = iterativeSolution.maxPathSum(testCases[i]);

			System.out.println("递归方法结果: " + recursiveResult);
			System.out.println("迭代方法结果: " + iterativeResult);
		}

		// 运行性能测试
		performanceTest();
	}

	/**
	 * 算法深度解析与总结：
	 * 
	 * 1. 问题本质：
	 *    这是一个典型的树形动态规划问题，需要从子树向上聚合信息。
	 *    对于每个节点，我们需要知道两条关键信息：
	 *    - 经过该节点的最大路径和（用于更新全局最大值）
	 *    - 从该节点出发向下延伸的最大路径和（用于提供给父节点计算）
	 * 
	 * 2. 递归解法核心思想：
	 *    - 自底向上遍历树，为每个节点计算其最大贡献值
	 *    - 对于空节点，贡献值为0
	 *    - 对于非空节点，左/右子树的贡献值如果为负，则可以选择不包含该子树（取0）
	 *    - 经过当前节点的最大路径和 = 当前节点值 + 左子树最大贡献值 + 右子树最大贡献值
	 *    - 当前节点的最大贡献值 = 当前节点值 + max(左子树最大贡献值, 右子树最大贡献值)
	 * 
	 * 3. 迭代解法核心思想：
	 *    - 使用栈模拟递归的后序遍历过程
	 *    - 使用HashMap存储每个节点的最大贡献值
	 *    - 通过标记位来区分是首次访问节点还是第二次访问（此时子节点已处理完毕）
	 *    - 第二次访问时，根据子节点的贡献值计算当前节点的贡献值和路径和
	 * 
	 * 4. 边界条件处理：
	 *    - 空树：根据题意，路径至少包含一个节点，所以实际应用中不会有空树
	 *    - 只有负节点：需要确保能够选择单个节点作为路径，即使其值为负
	 *    - 子树贡献为负：此时应选择不包含该子树，即取贡献值为0
	 * 
	 * 5. 为什么Morris遍历不适用于此问题：
	 *    - 信息聚合：需要从子树向上传递贡献值，Morris遍历的线索化会干扰这一过程
	 *    - 非线性路径：问题中的路径可以包含左右分支，而Morris是线性遍历
	 *    - 状态维护：在Morris遍历中维护子树的最大贡献值会非常复杂且容易出错
	 * 
	 * 6. 算法复杂度分析：
	 *    - 时间复杂度：O(n)，每个节点只访问一次
	 *    - 空间复杂度：
	 *      - 递归：O(h)，h为树高，最坏情况下为O(n)
	 *      - 迭代：O(h)，栈空间和映射空间，最坏情况下为O(n)
	 * 
	 * 7. 优化考虑：
	 *    - 如果树很深，递归可能导致栈溢出，此时应使用迭代版本
	 *    - 对于非常大的树，可以考虑使用分治策略进行并行计算
	 * 
	 * 8. 实际应用场景：
	 *    - 网络路由中的最大流量计算
	 *    - 电路设计中的信号传输路径优化
	 *    - 金融分析中的投资组合优化
	 *    - 物流系统中的最优路径规划
	 */
}

/*
C++版本实现：

#include <iostream>
#include <vector>
#include <stack>
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

// 递归解法
class RecursiveSolution {
private:
    int maxSum;
    
    int maxGain(TreeNode* node) {
        if (!node) return 0;
        
        // 递归计算左右子节点的最大贡献值
        int leftGain = max(maxGain(node->left), 0);
        int rightGain = max(maxGain(node->right), 0);
        
        // 计算经过当前节点的最大路径和
        int currentPathSum = node->val + leftGain + rightGain;
        
        // 更新全局最大路径和
        maxSum = max(maxSum, currentPathSum);
        
        // 返回当前节点的最大贡献值
        return node->val + max(leftGain, rightGain);
    }
    
public:
    int maxPathSum(TreeNode* root) {
        maxSum = INT_MIN;
        maxGain(root);
        return maxSum;
    }
};

// 迭代解法
class IterativeSolution {
private:
    struct TreeNodeInfo {
        TreeNode* node;
        bool visited;
        
        TreeNodeInfo(TreeNode* n) : node(n), visited(false) {}
    };
    
public:
    int maxPathSum(TreeNode* root) {
        if (!root) return 0;
        
        int maxSum = INT_MIN;
        stack<TreeNodeInfo> stk;
        unordered_map<TreeNode*, int> gainMap;
        
        stk.push(TreeNodeInfo(root));
        
        while (!stk.empty()) {
            TreeNodeInfo current = stk.top();
            stk.pop();
            
            if (!current.visited) {
                // 第一次访问，重新入栈并标记为已访问
                current.visited = true;
                stk.push(current);
                
                // 先右后左入栈，确保处理顺序是左、右、根
                if (current.node->right) {
                    stk.push(TreeNodeInfo(current.node->right));
                }
                if (current.node->left) {
                    stk.push(TreeNodeInfo(current.node->left));
                }
            } else {
                // 第二次访问，计算贡献值
                int leftGain = 0;
                int rightGain = 0;
                
                if (current.node->left && gainMap.find(current.node->left) != gainMap.end()) {
                    leftGain = max(gainMap[current.node->left], 0);
                }
                if (current.node->right && gainMap.find(current.node->right) != gainMap.end()) {
                    rightGain = max(gainMap[current.node->right], 0);
                }
                
                // 计算经过当前节点的最大路径和
                int currentPathSum = current.node->val + leftGain + rightGain;
                maxSum = max(maxSum, currentPathSum);
                
                // 计算当前节点的贡献值并存储
                int currentGain = current.node->val + max(leftGain, rightGain);
                gainMap[current.node] = currentGain;
            }
        }
        
        return maxSum;
    }
};

// 创建测试用例
vector<TreeNode*> createTestCases() {
    vector<TreeNode*> testCases;
    
    // 测试用例1: [1,2,3]
    TreeNode* root1 = new TreeNode(1);
    root1->left = new TreeNode(2);
    root1->right = new TreeNode(3);
    testCases.push_back(root1);
    
    // 测试用例2: [-10,9,20,null,null,15,7]
    TreeNode* root2 = new TreeNode(-10);
    root2->left = new TreeNode(9);
    root2->right = new TreeNode(20);
    root2->right->left = new TreeNode(15);
    root2->right->right = new TreeNode(7);
    testCases.push_back(root2);
    
    // 测试用例3: [-3]
    TreeNode* root3 = new TreeNode(-3);
    testCases.push_back(root3);
    
    // 测试用例4: [5,4,8,11,null,13,4,7,2,null,null,null,1]
    TreeNode* root4 = new TreeNode(5);
    root4->left = new TreeNode(4);
    root4->right = new TreeNode(8);
    root4->left->left = new TreeNode(11);
    root4->right->left = new TreeNode(13);
    root4->right->right = new TreeNode(4);
    root4->left->left->left = new TreeNode(7);
    root4->left->left->right = new TreeNode(2);
    root4->right->right->right = new TreeNode(1);
    testCases.push_back(root4);
    
    return testCases;
}

// 释放树内存
void deleteTree(TreeNode* root) {
    if (!root) return;
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

// 打印树结构
void printTreeHelper(TreeNode* node, int level, const string& prefix) {
    if (!node) return;
    
    for (int i = 0; i < level; ++i) {
        cout << "    ";
    }
    
    cout << prefix << ": " << node->val << endl;
    printTreeHelper(node->left, level + 1, "L");
    printTreeHelper(node->right, level + 1, "R");
}

void printTree(TreeNode* root) {
    if (!root) {
        cout << "Empty tree" << endl;
        return;
    }
    printTreeHelper(root, 0, "H");
}

int main() {
    vector<TreeNode*> testCases = createTestCases();
    vector<string> testNames = {
        "测试用例1 [1,2,3]",
        "测试用例2 [-10,9,20,null,null,15,7]",
        "测试用例3 [-3]",
        "测试用例4 [5,4,8,11,null,13,4,7,2,null,null,null,1]"
    };
    
    RecursiveSolution recursiveSolution;
    IterativeSolution iterativeSolution;
    
    for (size_t i = 0; i < testCases.size(); ++i) {
        cout << "\n" << testNames[i] << endl;
        printTree(testCases[i]);
        
        int recursiveResult = recursiveSolution.maxPathSum(testCases[i]);
        int iterativeResult = iterativeSolution.maxPathSum(testCases[i]);
        
        cout << "递归方法结果: " << recursiveResult << endl;
        cout << "迭代方法结果: " << iterativeResult << endl;
        
        // 清理内存
        deleteTree(testCases[i]);
    }
    
    return 0;
}

Python版本实现：

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

# 递归解法
class RecursiveSolution:
    def __init__(self):
        self.max_sum = float('-inf')
    
    def maxPathSum(self, root):
        self.max_sum = float('-inf')
        self._max_gain(root)
        return self.max_sum
    
    def _max_gain(self, node):
        if not node:
            return 0
        
        # 递归计算左右子节点的最大贡献值
        left_gain = max(self._max_gain(node.left), 0)
        right_gain = max(self._max_gain(node.right), 0)
        
        # 计算经过当前节点的最大路径和
        current_path_sum = node.val + left_gain + right_gain
        
        # 更新全局最大路径和
        self.max_sum = max(self.max_sum, current_path_sum)
        
        # 返回当前节点的最大贡献值
        return node.val + max(left_gain, right_gain)

# 迭代解法
class IterativeSolution:
    def maxPathSum(self, root):
        if not root:
            return 0
        
        max_sum = float('-inf')
        stack = [(root, False)]
        gain_map = {}
        
        while stack:
            node, visited = stack.pop()
            
            if not visited:
                # 第一次访问，重新入栈并标记为已访问
                stack.append((node, True))
                # 先右后左入栈，确保处理顺序是左、右、根
                if node.right:
                    stack.append((node.right, False))
                if node.left:
                    stack.append((node.left, False))
            else:
                # 第二次访问，计算贡献值
                left_gain = max(gain_map.get(node.left, 0), 0)
                right_gain = max(gain_map.get(node.right, 0), 0)
                
                # 计算经过当前节点的最大路径和
                current_path_sum = node.val + left_gain + right_gain
                max_sum = max(max_sum, current_path_sum)
                
                # 计算当前节点的贡献值并存储
                current_gain = node.val + max(left_gain, right_gain)
                gain_map[node] = current_gain
        
        return max_sum

# 创建测试用例
def create_test_cases():
    # 测试用例1: [1,2,3]
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    
    # 测试用例2: [-10,9,20,null,null,15,7]
    root2 = TreeNode(-10)
    root2.left = TreeNode(9)
    root2.right = TreeNode(20)
    root2.right.left = TreeNode(15)
    root2.right.right = TreeNode(7)
    
    # 测试用例3: [-3]
    root3 = TreeNode(-3)
    
    # 测试用例4: [5,4,8,11,null,13,4,7,2,null,null,null,1]
    root4 = TreeNode(5)
    root4.left = TreeNode(4)
    root4.right = TreeNode(8)
    root4.left.left = TreeNode(11)
    root4.right.left = TreeNode(13)
    root4.right.right = TreeNode(4)
    root4.left.left.left = TreeNode(7)
    root4.left.left.right = TreeNode(2)
    root4.right.right.right = TreeNode(1)
    
    return [root1, root2, root3, root4]

# 打印树结构
def print_tree_helper(node, level, prefix):
    if not node:
        return
    
    print("    " * level + f"{prefix}: {node.val}")
    print_tree_helper(node.left, level + 1, "L")
    print_tree_helper(node.right, level + 1, "R")

def print_tree(root):
    if not root:
        print("Empty tree")
        return
    print_tree_helper(root, 0, "H")

# 主函数用于测试
if __name__ == "__main__":
    test_cases = create_test_cases()
    test_names = [
        "测试用例1 [1,2,3]",
        "测试用例2 [-10,9,20,null,null,15,7]",
        "测试用例3 [-3]",
        "测试用例4 [5,4,8,11,null,13,4,7,2,null,null,null,1]"
    ]
    
    recursive_solution = RecursiveSolution()
    iterative_solution = IterativeSolution()
    
    for i, root in enumerate(test_cases):
        print(f"\n{test_names[i]}")
        print_tree(root)
        
        recursive_result = recursive_solution.maxPathSum(root)
        iterative_result = iterative_solution.maxPathSum(root)
        
        print(f"递归方法结果: {recursive_result}")
        print(f"迭代方法结果: {iterative_result}")
*/