package class124;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.TimeUnit;

// Morris遍历恢复二叉搜索树
// 
// 题目来源：
// - 恢复BST：LeetCode 99. Recover Binary Search Tree
//   链接：https://leetcode.cn/problems/recover-binary-search-tree/
// 
// Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
// 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
// 
// 本实现包含：
// 1. Java语言的Morris中序遍历恢复BST
// 2. 递归版本的恢复BST
// 3. 迭代版本的恢复BST
// 4. 详细的注释和算法解析
// 5. 完整的测试用例
// 6. C++和Python语言的完整实现
// 
// 三种语言实现链接：
// - Java: 当前文件
// - Python: https://leetcode.cn/problems/recover-binary-search-tree/solution/python-morris-hui-fu-bst-by-xxx/
// - C++: https://leetcode.cn/problems/recover-binary-search-tree/solution/c-morris-hui-fu-bst-by-xxx/
// 
// 算法详解：
// 利用BST的中序遍历结果应该是严格递增的特性，通过Morris中序遍历找到被错误交换的两个节点并恢复
// 1. 使用Morris中序遍历访问BST
// 2. 在遍历过程中找到违反BST性质的节点对
// 3. 记录第一对和最后一对违反BST性质的节点
// 4. 交换这两个节点的值，恢复BST
// 
// 时间复杂度：O(n) - 每个节点最多被访问两次
// 空间复杂度：O(1) - 不使用额外空间
// 适用场景：内存受限环境中恢复大规模BST、在线算法恢复BST
// 优缺点分析：
// - 优点：空间复杂度最优，适合内存受限环境
// - 缺点：实现相对复杂，需要准确识别被错误交换的节点
*/
public class Code06_MorrisRecoverBST {

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
	 * 使用Morris遍历恢复二叉搜索树（最优解）
	 * 
	 * 题目描述：
	 * 给你二叉搜索树的根节点 root ，该树中的恰好两个节点被错误地交换。
	 * 请在不改变其结构的情况下，恢复这棵树。
	 * 
	 * 解题思路：
	 * 1. 使用Morris中序遍历获取节点序列
	 * 2. 在遍历过程中找到被错误交换的两个节点
	 * 3. 交换这两个节点的值
	 * 
	 * 核心思想：
	 * 在正确的BST中，中序遍历应该是严格递增的序列。
	 * 当两个节点被错误交换后，中序遍历序列中会出现1-2个逆序对：
	 * - 如果相邻节点交换：会出现一个逆序对 (first=pre, second=cur)
	 * - 如果不相邻节点交换：会出现两个逆序对 (first=第一个逆序对的pre, second=第二个逆序对的cur)
	 * 
	 * 时间复杂度：O(n) - 每个节点最多被访问3次（创建线索、访问节点、删除线索）
	 * 空间复杂度：O(1) - 仅使用常数额外空间
	 * 是否为最优解：是，Morris遍历是解决此问题的最优方法，空间复杂度优于传统方法
	 * 
	 * @param root 二叉搜索树的根节点
	 */
	public void recoverTree(TreeNode root) {
		// 边界情况处理：空树或单节点树无需恢复
		if (root == null) {
			return;
		}
		
		TreeNode first = null;   // 第一个错误节点
		TreeNode second = null;  // 第二个错误节点
		TreeNode pre = null;     // 前一个遍历的节点
		
		TreeNode cur = root;
		TreeNode mostRight = null;
		
		// Morris中序遍历
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				// 找到cur左子树的最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				if (mostRight.right == null) {
					// 第一次到达，建立线索
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					// 第二次到达，断开线索
					mostRight.right = null;
				}
			}
			
			// 调试信息：打印当前访问的节点
			// System.out.println("Visiting node: " + cur.val + ", pre node: " + (pre == null ? "null" : pre.val));
			
			// 检查是否有逆序对
			if (pre != null && pre.val > cur.val) {
				// 第一次发现逆序对时，pre是第一个错误节点
				if (first == null) {
					first = pre;
					// 调试信息：找到第一个逆序对
					// System.out.println("Found first pair: pre=" + pre.val + ", cur=" + cur.val);
				}
				// 每次发现逆序对时，cur都可能是第二个错误节点
				// 如果只有一个逆序对，这是正确的
				// 如果有两个逆序对，会被后面的覆盖
				second = cur;
				// 调试信息：更新second节点
				// System.out.println("Updated second node to: " + cur.val);
			}
			
			pre = cur;
			cur = cur.right;
		}
		
		// 交换两个错误节点的值
		if (first != null && second != null) {
			// 调试信息：交换前的值
			// System.out.println("Swapping nodes: " + first.val + " and " + second.val);
			int temp = first.val;
			first.val = second.val;
			second.val = temp;
		}
	}

	/**
	 * 递归版本的恢复二叉搜索树方法
	 * 
	 * 思路：使用递归进行中序遍历，找出逆序对
	 * 优点：实现简单，代码清晰
	 * 缺点：空间复杂度O(h)，h为树高，最坏情况下为O(n)
	 * 
	 * 时间复杂度：O(n) - 需要遍历所有节点
	 * 空间复杂度：O(h) - 递归栈空间
	 * 
	 * @param root 二叉搜索树的根节点
	 */
	public void recoverTreeRecursive(TreeNode root) {
		TreeNode[] nodes = new TreeNode[3]; // [first, second, pre]
		nodes[0] = null; // first
		nodes[1] = null; // second
		nodes[2] = null; // pre
		
		inorderRecursive(root, nodes);
		
		// 交换两个错误节点的值
		if (nodes[0] != null && nodes[1] != null) {
			int temp = nodes[0].val;
			nodes[0].val = nodes[1].val;
			nodes[1].val = temp;
		}
	}
	
	/**
	 * 递归中序遍历辅助方法
	 */
	private void inorderRecursive(TreeNode node, TreeNode[] nodes) {
		if (node == null) {
			return;
		}
		
		inorderRecursive(node.left, nodes);
		
		// 检查逆序对
		if (nodes[2] != null && nodes[2].val > node.val) {
			if (nodes[0] == null) {
				nodes[0] = nodes[2];
			}
			nodes[1] = node;
		}
		nodes[2] = node;
		
		inorderRecursive(node.right, nodes);
	}

	/**
	 * 迭代版本的恢复二叉搜索树方法
	 * 
	 * 思路：使用栈进行迭代中序遍历，找出逆序对
	 * 优点：避免了递归栈溢出的风险
	 * 缺点：空间复杂度O(h)，h为树高
	 * 
	 * 时间复杂度：O(n) - 需要遍历所有节点
	 * 空间复杂度：O(h) - 栈空间
	 * 
	 * @param root 二叉搜索树的根节点
	 */
	public void recoverTreeIterative(TreeNode root) {
		if (root == null) {
			return;
		}
		
		TreeNode first = null;
		TreeNode second = null;
		TreeNode pre = null;
		Stack<TreeNode> stack = new Stack<>();
		TreeNode cur = root;
		
		// 迭代中序遍历
		while (cur != null || !stack.isEmpty()) {
			// 遍历左子树，入栈
			while (cur != null) {
				stack.push(cur);
				cur = cur.left;
			}
			
			// 处理当前节点
			cur = stack.pop();
			
			// 检查逆序对
			if (pre != null && pre.val > cur.val) {
				if (first == null) {
					first = pre;
				}
				second = cur;
			}
			
			pre = cur;
			cur = cur.right;
		}
		
		// 交换两个错误节点的值
		if (first != null && second != null) {
			int temp = first.val;
			first.val = second.val;
			second.val = temp;
		}
	}
	
	/**
	 * 辅助方法：打印树的中序遍历序列
	 * 用于验证树是否被正确恢复
	 */
	public List<Integer> inorderTraversal(TreeNode root) {
		List<Integer> result = new ArrayList<>();
		if (root == null) {
			return result;
		}
		
		// 使用Morris中序遍历进行打印
		TreeNode cur = root;
		TreeNode mostRight = null;
		
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			
			result.add(cur.val);
			cur = cur.right;
		}
		
		return result;
	}

	/**
	 * 测试方法
	 */
	/**
	 * 创建测试树
	 * @param values 层序遍历的值数组，null表示空节点
	 * @return 构造的树的根节点
	 */
	private TreeNode createTree(Integer[] values) {
		if (values == null || values.length == 0) {
			return null;
		}
		
		TreeNode[] nodes = new TreeNode[values.length];
		for (int i = 0; i < values.length; i++) {
			if (values[i] != null) {
				nodes[i] = new TreeNode(values[i]);
			}
		}
		
		for (int i = 0; i < values.length; i++) {
			if (nodes[i] != null) {
				int leftIndex = 2 * i + 1;
				if (leftIndex < values.length) {
					nodes[i].left = nodes[leftIndex];
				}
				
				int rightIndex = 2 * i + 2;
				if (rightIndex < values.length) {
					nodes[i].right = nodes[rightIndex];
				}
			}
		}
		
		return nodes[0];
	}
	
	/**
	 * 验证BST是否有效
	 * @param root 树的根节点
	 * @return 是否是有效的BST
	 */
	private boolean isValidBST(TreeNode root) {
		List<Integer> values = new ArrayList<>();
		inorderCollect(root, values);
		
		for (int i = 1; i < values.size(); i++) {
			if (values.get(i - 1) >= values.get(i)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 中序遍历收集值
	 */
	private void inorderCollect(TreeNode root, List<Integer> values) {
		if (root == null) {
			return;
		}
		inorderCollect(root.left, values);
		values.add(root.val);
		inorderCollect(root.right, values);
	}
	
	/**
	 * 创建随机打乱的BST（用于性能测试）
	 */
	private TreeNode createRandomBST(int size) {
		if (size <= 0) {
			return null;
		}
		
		// 创建一个有序数组
		int[] values = new int[size];
		for (int i = 0; i < size; i++) {
			values[i] = i + 1;
		}
		
		// 构建平衡BST
		return buildBST(values, 0, values.length - 1);
	}
	
	/**
	 * 构建平衡BST
	 */
	private TreeNode buildBST(int[] values, int start, int end) {
		if (start > end) {
			return null;
		}
		
		int mid = start + (end - start) / 2;
		TreeNode root = new TreeNode(values[mid]);
		root.left = buildBST(values, start, mid - 1);
		root.right = buildBST(values, mid + 1, end);
		return root;
	}
	
	/**
	 * 随机交换两个节点的值
	 */
	private void swapTwoRandomNodes(TreeNode root) {
		if (root == null) {
			return;
		}
		
		// 收集所有节点
		List<TreeNode> nodes = new ArrayList<>();
		collectAllNodes(root, nodes);
		
		if (nodes.size() < 2) {
			return;
		}
		
		// 随机选择两个不同的节点
		Random random = new Random();
		int i = random.nextInt(nodes.size());
		int j = random.nextInt(nodes.size());
		while (i == j) {
			j = random.nextInt(nodes.size());
		}
		
		// 交换值
		int temp = nodes.get(i).val;
		nodes.get(i).val = nodes.get(j).val;
		nodes.get(j).val = temp;
	}
	
	/**
	 * 收集所有节点
	 */
	private void collectAllNodes(TreeNode root, List<TreeNode> nodes) {
		if (root == null) {
			return;
		}
		nodes.add(root);
		collectAllNodes(root.left, nodes);
		collectAllNodes(root.right, nodes);
	}
	
	/**
	 * 性能测试
	 */
	private void performanceTest(int treeSize, int iterations) {
		System.out.println("\n=== 性能测试 ===");
		System.out.println("树大小: " + treeSize);
		System.out.println("迭代次数: " + iterations);
		
		Code06_MorrisRecoverBST solution = new Code06_MorrisRecoverBST();
		
		// Morris遍历性能测试
		long morrisTotalTime = 0;
		for (int i = 0; i < iterations; i++) {
			TreeNode root = createRandomBST(treeSize);
			swapTwoRandomNodes(root);
			
			long startTime = System.nanoTime();
			solution.recoverTree(root);
			long endTime = System.nanoTime();
			
			morrisTotalTime += (endTime - startTime);
			
			// 验证结果正确性
			assert isValidBST(root) : "Morris遍历恢复失败！";
		}
		System.out.println("Morris遍历平均耗时: " + TimeUnit.NANOSECONDS.toMicros(morrisTotalTime / iterations) + " μs");
		
		// 递归性能测试
		long recursiveTotalTime = 0;
		for (int i = 0; i < iterations; i++) {
			TreeNode root = createRandomBST(treeSize);
			swapTwoRandomNodes(root);
			
			long startTime = System.nanoTime();
			solution.recoverTreeRecursive(root);
			long endTime = System.nanoTime();
			
			recursiveTotalTime += (endTime - startTime);
			
			// 验证结果正确性
			assert isValidBST(root) : "递归恢复失败！";
		}
		System.out.println("递归平均耗时: " + TimeUnit.NANOSECONDS.toMicros(recursiveTotalTime / iterations) + " μs");
		
		// 迭代性能测试
		long iterativeTotalTime = 0;
		for (int i = 0; i < iterations; i++) {
			TreeNode root = createRandomBST(treeSize);
			swapTwoRandomNodes(root);
			
			long startTime = System.nanoTime();
			solution.recoverTreeIterative(root);
			long endTime = System.nanoTime();
			
			iterativeTotalTime += (endTime - startTime);
			
			// 验证结果正确性
			assert isValidBST(root) : "迭代恢复失败！";
		}
		System.out.println("迭代平均耗时: " + TimeUnit.NANOSECONDS.toMicros(iterativeTotalTime / iterations) + " μs");
	}
	
	/**
	 * 运行所有测试用例
	 */
	public static void runAllTests() {
		Code06_MorrisRecoverBST solution = new Code06_MorrisRecoverBST();
		
		// 测试用例1：相邻节点交换
		testCase1(solution);
		
		// 测试用例2：不相邻节点交换
		testCase2(solution);
		
		// 测试用例3：空树
		testCase3(solution);
		
		// 测试用例4：单节点树
		testCase4(solution);
		
		// 测试用例5：更深的树，不相邻节点交换
		testCase5(solution);
		
		// 测试用例6：两个相同值的节点（特殊情况）
		testCase6(solution);
	}
	
	// 测试用例详情
	private static void testCase1(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例1：相邻节点交换 ===");
		Integer[] values = {1, 3, null, null, 2};
		TreeNode root = solution.createTree(values);
		
		System.out.println("恢复前中序遍历: " + solution.inorderTraversal(root));
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	private static void testCase2(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例2：不相邻节点交换 ===");
		Integer[] values = {3, 1, 4, null, null, 2};
		TreeNode root = solution.createTree(values);
		
		System.out.println("恢复前中序遍历: " + solution.inorderTraversal(root));
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	private static void testCase3(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例3：空树 ===");
		TreeNode root = null;
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	private static void testCase4(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例4：单节点树 ===");
		TreeNode root = new TreeNode(5);
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	private static void testCase5(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例5：更深的树，不相邻节点交换 ===");
		Integer[] values = {4, 2, 6, 1, 5, 3, 7};
		TreeNode root = solution.createTree(values);
		
		System.out.println("恢复前中序遍历: " + solution.inorderTraversal(root));
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	private static void testCase6(Code06_MorrisRecoverBST solution) {
		System.out.println("\n=== 测试用例6：两个相同值的节点 ===");
		TreeNode root = new TreeNode(2);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		
		System.out.println("恢复前中序遍历: " + solution.inorderTraversal(root));
		solution.recoverTree(root);
		System.out.println("恢复后中序遍历: " + solution.inorderTraversal(root));
		System.out.println("是否有效BST: " + solution.isValidBST(root));
	}
	
	public static void main(String[] args) {
		// 运行所有测试用例
		runAllTests();
		
		// 运行性能测试
		Code06_MorrisRecoverBST solution = new Code06_MorrisRecoverBST();
		
		// 小型树性能测试
		solution.performanceTest(100, 1000);
		
		// 中型树性能测试
		solution.performanceTest(1000, 100);
	}


	// 以下是C++完整实现代码
	/*
	// C++实现
	#include <iostream>
	#include <vector>
	#include <stack>
	using namespace std;

	struct TreeNode {
	    int val;
	    TreeNode *left;
	    TreeNode *right;
	    TreeNode() : val(0), left(nullptr), right(nullptr) {}
	    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
	    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
	};

	class Solution {
	public:
	    // Morris遍历恢复BST（最优解）
	    void recoverTree(TreeNode* root) {
	        if (!root) return;
	        
	        TreeNode* first = nullptr;
	        TreeNode* second = nullptr;
	        TreeNode* pre = nullptr;
	        TreeNode* cur = root;
	        TreeNode* mostRight = nullptr;
	        
	        while (cur) {
	            mostRight = cur->left;
	            if (mostRight) {
	                while (mostRight->right && mostRight->right != cur) {
	                    mostRight = mostRight->right;
	                }
	                
	                if (!mostRight->right) {
	                    mostRight->right = cur;
	                    cur = cur->left;
	                    continue;
	                } else {
	                    mostRight->right = nullptr;
	                }
	            }
	            
	            // 检查逆序对
	            if (pre && pre->val > cur->val) {
	                if (!first) {
	                    first = pre;
	                }
	                second = cur;
	            }
	            
	            pre = cur;
	            cur = cur->right;
	        }
	        
	        // 交换节点值
	        if (first && second) {
	            swap(first->val, second->val);
	        }
	    }
	    
	    // 递归版本
	    void recoverTreeRecursive(TreeNode* root) {
	        TreeNode* first = nullptr;
	        TreeNode* second = nullptr;
	        TreeNode* pre = nullptr;
	        
	        inorderRecursive(root, first, second, pre);
	        
	        if (first && second) {
	            swap(first->val, second->val);
	        }
	    }
	    
	    void inorderRecursive(TreeNode* node, TreeNode*& first, TreeNode*& second, TreeNode*& pre) {
	        if (!node) return;
	        
	        inorderRecursive(node->left, first, second, pre);
	        
	        if (pre && pre->val > node->val) {
	            if (!first) {
	                first = pre;
	            }
	            second = node;
	        }
	        pre = node;
	        
	        inorderRecursive(node->right, first, second, pre);
	    }
	    
	    // 迭代版本
	    void recoverTreeIterative(TreeNode* root) {
	        if (!root) return;
	        
	        TreeNode* first = nullptr;
	        TreeNode* second = nullptr;
	        TreeNode* pre = nullptr;
	        stack<TreeNode*> stk;
	        TreeNode* cur = root;
	        
	        while (cur || !stk.empty()) {
	            while (cur) {
	                stk.push(cur);
	                cur = cur->left;
	            }
	            
	            cur = stk.top();
	            stk.pop();
	            
	            if (pre && pre->val > cur->val) {
	                if (!first) {
	                    first = pre;
	                }
	                second = cur;
	            }
	            
	            pre = cur;
	            cur = cur->right;
	        }
	        
	        if (first && second) {
	            swap(first->val, second->val);
	        }
	    }
	    
	    // 辅助方法：中序遍历
	    vector<int> inorderTraversal(TreeNode* root) {
	        vector<int> result;
	        if (!root) return result;
	        
	        TreeNode* cur = root;
	        TreeNode* mostRight = nullptr;
	        
	        while (cur) {
	            mostRight = cur->left;
	            if (mostRight) {
	                while (mostRight->right && mostRight->right != cur) {
	                    mostRight = mostRight->right;
	                }
	                
	                if (!mostRight->right) {
	                    mostRight->right = cur;
	                    cur = cur->left;
	                    continue;
	                } else {
	                    mostRight->right = nullptr;
	                }
	            }
	            
	            result.push_back(cur->val);
	            cur = cur->right;
	        }
	        
	        return result;
	    }
	};
	*/

	// 以下是Python完整实现代码
	/*
	# Python实现
	# Definition for a binary tree node.
	# class TreeNode:
	#     def __init__(self, val=0, left=None, right=None):
	#         self.val = val
	#         self.left = left
	#         self.right = right
	
	class Solution:
	    def recoverTree(self, root):
	        """使用Morris遍历恢复二叉搜索树（最优解）"""
	        if not root:
	            return
	        
	        first = None  # 第一个错误节点
	        second = None  # 第二个错误节点
	        pre = None     # 前一个遍历的节点
	        cur = root
	        
	        while cur:
	            if cur.left:
	                # 找到cur左子树的最右节点
	                most_right = cur.left
	                while most_right.right and most_right.right != cur:
	                    most_right = most_right.right
	                
	                if not most_right.right:
	                    # 第一次到达，建立线索
	                    most_right.right = cur
	                    cur = cur.left
	                    continue
	                else:
	                    # 第二次到达，断开线索
	                    most_right.right = None
	            
	            # 检查是否有逆序对
	            if pre and pre.val > cur.val:
	                # 第一次发现逆序对时，pre是第一个错误节点
	                if not first:
	                    first = pre
	                # 每次发现逆序对时，cur都可能是第二个错误节点
	                second = cur
	            
	            pre = cur
	            cur = cur.right
	        
	        # 交换两个错误节点的值
	        if first and second:
	            first.val, second.val = second.val, first.val
	    
	    def recoverTreeRecursive(self, root):
	        """递归版本的恢复二叉搜索树方法"""
	        first = [None]  # 使用列表作为可变引用
	        second = [None]
	        pre = [None]
	        
	        def inorder(node):
	            if not node:
	                return
	            
	            inorder(node.left)
	            
	            # 检查逆序对
	            if pre[0] and pre[0].val > node.val:
	                if not first[0]:
	                    first[0] = pre[0]
	                second[0] = node
	            pre[0] = node
	            
	            inorder(node.right)
	        
	        inorder(root)
	        
	        # 交换两个错误节点的值
	        if first[0] and second[0]:
	            first[0].val, second[0].val = second[0].val, first[0].val
	    
	    def recoverTreeIterative(self, root):
	        """迭代版本的恢复二叉搜索树方法"""
	        if not root:
	            return
	        
	        first = None
	        second = None
	        pre = None
	        stack = []
	        cur = root
	        
	        # 迭代中序遍历
	        while cur or stack:
	            # 遍历左子树，入栈
	            while cur:
	                stack.append(cur)
	                cur = cur.left
	            
	            # 处理当前节点
	            cur = stack.pop()
	            
	            # 检查逆序对
	            if pre and pre.val > cur.val:
	                if not first:
	                    first = pre
	                second = cur
	            
	            pre = cur
	            cur = cur.right
	        
	        # 交换两个错误节点的值
	        if first and second:
	            first.val, second.val = second.val, first.val
	    
	    def inorderTraversal(self, root):
	        """辅助方法：返回树的中序遍历序列"""
	        result = []
	        if not root:
	            return result
	        
	        cur = root
	        
	        while cur:
	            if cur.left:
	                # 找到cur左子树的最右节点
	                most_right = cur.left
	                while most_right.right and most_right.right != cur:
	                    most_right = most_right.right
	                
	                if not most_right.right:
	                    # 第一次到达，建立线索
	                    most_right.right = cur
	                    cur = cur.left
	                    continue
	                else:
	                    # 第二次到达，断开线索
	                    most_right.right = None
	            
	            result.append(cur.val)
	            cur = cur.right
	        
	        return result
	*/

	/**
	 * 算法深度解析与工程实践
	 * 
	 * 【复杂度分析对比】
	 * | 方法 | 时间复杂度 | 空间复杂度 | 平均时间(小型树) | 平均时间(中型树) |
	 * |------|------------|------------|------------------|------------------|
	 * | Morris | O(n) | O(1) | 最快~中等 | 中等~较慢 |
	 * | 递归 | O(n) | O(h) | 中等 | 最快 |
	 * | 迭代 | O(n) | O(h) | 最慢 | 中等 |
	 * 
	 * 【Morris遍历核心原理】
	 * Morris遍历是一种空间效率极高的树遍历算法，通过临时修改树的结构（线索化）来实现O(1)空间复杂度。
	 * 关键思想：
	 * 1. 对于每个节点，找到其前驱节点（左子树的最右节点）
	 * 2. 第一次访问时，建立从前驱节点到当前节点的线索
	 * 3. 第二次访问时，断开线索并处理当前节点
	 * 4. 这种方式避免了使用栈或递归调用栈
	 * 
	 * 【错误节点定位策略】
	 * 恢复BST的关键在于理解错误交换对中序遍历序列的影响：
	 * 1. 如果交换相邻节点，中序序列中会出现1个逆序对
	 * 2. 如果交换不相邻节点，中序序列中会出现2个逆序对
	 * 3. 算法会记录第一个逆序对的前一个节点作为first，最后一个逆序对的当前节点作为second
	 * 4. 这种策略能够正确处理所有可能的交换情况
	 * 
	 * 【工程实践建议】
	 * 1. 内存受限环境（如嵌入式系统）：Morris遍历是最佳选择
	 * 2. 开发速度优先：递归实现最简洁，易于理解和维护
	 * 3. 大数据量处理：迭代实现避免了递归栈溢出风险
	 * 4. 性能优化：
	 *    - 对于大部分应用场景，递归和迭代实现已足够高效
	 *    - 只有在严格的内存限制下，Morris遍历的空间优势才会显著体现
	 * 5. 代码健壮性：
	 *    - 确保处理所有边界情况：空树、单节点树、只有左子树或右子树的树
	 *    - 添加适当的验证机制确保恢复后树的有效性
	 * 
	 * 【常见陷阱与注意事项】
	 * 1. Morris遍历必须正确恢复树的结构，否则会导致后续操作出错
	 * 2. 在寻找前驱节点时，必须检查mostRight.right != cur，防止形成环
	 * 3. 错误节点的选择规则容易混淆，需要仔细理解逆序对的处理逻辑
	 * 4. 递归实现可能导致栈溢出，对于非常深的树需要改用迭代版本
	 * 5. 处理包含重复值的BST时需要特别注意，标准BST不允许重复值
	 * 
	 * 【扩展应用】
	 * Morris遍历不仅可以用于恢复BST，还可以应用于：
	 * 1. 树的序列化与反序列化
	 * 2. 树的路径和问题
	 * 3. 树的节点删除操作
	 * 4. 二叉树的镜像操作
	 * 5. 树的层序遍历变种
	 * 
	 * 【跨语言实现差异】
	 * 1. Java：需要使用数组或包装类传递可变引用
	 * 2. C++：可以直接使用指针引用（&）传递可变引用
	 * 3. Python：可以使用列表或可变对象传递可变引用
	 * 4. 不同语言的栈实现和内存管理机制会影响各方法的实际性能表现
	 */
}