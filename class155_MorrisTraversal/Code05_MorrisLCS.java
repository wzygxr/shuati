package class124;

import java.util.*;

/**
 * Morris遍历求两个节点的最低公共祖先
 * 测试链接 : https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
 * LeetCode 236. Lowest Common Ancestor of a Binary Tree
 * 
 * 题目来源：
 * - 最低公共祖先：LeetCode 236. Lowest Common Ancestor of a Binary Tree
 *   链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris遍历求最低公共祖先
 * 2. 递归版本的求最低公共祖先
 * 3. 迭代版本的求最低公共祖先（使用父指针）
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/solution/python-morris-qiu-liang-ge-jie-dian-de-zui-di-gong-by-xxx/
 * - C++: https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/solution/c-morris-qiu-liang-ge-jie-dian-de-zui-di-gong-by-xxx/
 * 
 * 算法详解：
 * 利用Morris遍历求二叉树中两个节点的最低公共祖先（LCA）
 * 1. 首先检查特殊情况：一个节点是否是另一个节点的祖先
 * 2. 使用Morris先序遍历找到第一个遇到的目标节点
 * 3. 使用Morris中序遍历寻找LCA：
 *    - 在第二次访问节点时，检查left是否在当前节点左子树的右边界上
 *    - 如果是，则检查left的右子树中是否包含另一个目标节点
 *    - 如果找到，则left就是LCA
 * 4. 如果遍历结束后仍未找到LCA，则最后一个left就是答案
 * 
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 适用场景：内存受限环境中求大规模二叉树中两个节点的LCA
 * 优缺点分析：
 * - 优点：空间复杂度最优，适用于内存极度受限的环境
 * - 缺点：实现最为复杂，需要结合Morris先序和中序遍历，逻辑复杂
 */
public class Code05_MorrisLCS {

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
	 * 使用Morris遍历求二叉树中两个节点的最低公共祖先
	 * 时间复杂度: O(n)，空间复杂度: O(1)
	 * 
	 * @param head 二叉树的根节点
	 * @param o1 第一个目标节点
	 * @param o2 第二个目标节点
	 * @return o1和o2的最低公共祖先节点
	 */
	public static TreeNode lowestCommonAncestor(TreeNode head, TreeNode o1, TreeNode o2) {
		// 情况1：如果其中一个节点是另一个节点的祖先，直接返回祖先节点
		// 检查o1是否是o2的祖先
		if (preOrder(o1.left, o1, o2) != null || preOrder(o1.right, o1, o2) != null) {
			return o1;
		}
		// 检查o2是否是o1的祖先
		if (preOrder(o2.left, o1, o2) != null || preOrder(o2.right, o1, o2) != null) {
			return o2;
		}
		
		// 情况2：两个节点不在同一路径上
		// 先找到第一个遇到的目标节点（o1或o2）
		TreeNode left = preOrder(head, o1, o2);
		
		// 使用Morris中序遍历来寻找LCA
		TreeNode cur = head;
		TreeNode mostRight = null;
		TreeNode lca = null;
		
		while (cur != null) {
			mostRight = cur.left;
			
			if (mostRight != null) {
				// 找到左子树的最右节点
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				if (mostRight.right == null) {
					// 第一次访问当前节点，建立线索化连接
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次访问当前节点
					// 恢复树的结构
					mostRight.right = null;
					
					if (lca == null) {
						// 检查left是否在cur左树的右边界上
						if (rightCheck(cur.left, left)) {
							// 检查left的右子树中是否包含另一个目标节点
							if (preOrder(left.right, o1, o2) != null) {
								lca = left; // 找到LCA
							}
							left = cur;
							// 注意：此时检查的是left而不是cur
							// 因为cur右树上的某些右指针可能还没有恢复回来
							// 需要等右指针完全恢复后检查才不会出错
						}
					}
				}
			}
			cur = cur.right;
		}
		
		// 如果morris遍历结束了还没有收集到答案
		// 此时最后一个left一定是答案
		return lca != null ? lca : left;
	}

	/**
	 * 以head为头的树进行Morris先序遍历，返回o1和o2中先被找到的节点
	 * 如果都找不到则返回null
	 * 
	 * @param head 起始节点
	 * @param o1 第一个目标节点
	 * @param o2 第二个目标节点
	 * @return 先找到的目标节点或null
	 */
	public static TreeNode preOrder(TreeNode head, TreeNode o1, TreeNode o2) {
		TreeNode cur = head;
		TreeNode mostRight = null;
		TreeNode ans = null;
		
		while (cur != null) {
			mostRight = cur.left;
			
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				
				if (mostRight.right == null) {
					// 第一次访问当前节点，检查是否是目标节点
					if (ans == null && (cur == o1 || cur == o2)) {
						ans = cur;
					}
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					// 第二次访问，恢复树结构
					mostRight.right = null;
				}
			} else {
				// 没有左子树，直接检查是否是目标节点
				if (ans == null && (cur == o1 || cur == o2)) {
					ans = cur;
				}
			}
			cur = cur.right;
		}
		
		return ans;
	}

	/**
	 * 从head节点开始遍历右边界，检查是否存在target节点
	 * 
	 * @param head 起始节点
	 * @param target 目标节点
	 * @return 是否在右边界上找到target
	 */
	public static boolean rightCheck(TreeNode head, TreeNode target) {
		while (head != null) {
			if (head == target) {
				return true;
			}
			head = head.right;
		}
		return false;
	}
	
	/**
	 * 递归方法求最低公共祖先
	 * 时间复杂度: O(n)，空间复杂度: O(h)，h为树高
	 * 
	 * @param root 二叉树的根节点
	 * @param p 第一个目标节点
	 * @param q 第二个目标节点
	 * @return p和q的最低公共祖先节点
	 */
	public static TreeNode lowestCommonAncestorRecursive(TreeNode root, TreeNode p, TreeNode q) {
		// 基本情况：如果根为空，或者根就是p或q，则返回根
		if (root == null || root == p || root == q) {
			return root;
		}
		
		// 递归搜索左子树
		TreeNode left = lowestCommonAncestorRecursive(root.left, p, q);
		// 递归搜索右子树
		TreeNode right = lowestCommonAncestorRecursive(root.right, p, q);
		
		// 如果左右子树各找到一个目标节点，则当前节点就是LCA
		if (left != null && right != null) {
			return root;
		}
		
		// 如果只有左子树或右子树找到了目标节点，则返回找到的那个节点
		return left != null ? left : right;
	}
	
	/**
	 * 迭代方法求最低公共祖先（使用父指针）
	 * 时间复杂度: O(n)，空间复杂度: O(n)
	 * 
	 * @param root 二叉树的根节点
	 * @param p 第一个目标节点
	 * @param q 第二个目标节点
	 * @return p和q的最低公共祖先节点
	 */
	public static TreeNode lowestCommonAncestorIterative(TreeNode root, TreeNode p, TreeNode q) {
		// 使用HashMap记录每个节点的父节点
		Map<TreeNode, TreeNode> parentMap = new HashMap<>();
		// 使用Queue进行广度优先搜索
		Queue<TreeNode> queue = new LinkedList<>();
		
		// 根节点没有父节点
		parentMap.put(root, null);
		queue.offer(root);
		
		// 当两个目标节点都找到父节点映射后停止BFS
		while (!parentMap.containsKey(p) || !parentMap.containsKey(q)) {
			TreeNode node = queue.poll();
			
			// 将左子节点加入队列并记录父节点
			if (node.left != null) {
				parentMap.put(node.left, node);
				queue.offer(node.left);
			}
			
			// 将右子节点加入队列并记录父节点
			if (node.right != null) {
				parentMap.put(node.right, node);
				queue.offer(node.right);
			}
		}
		
		// 收集p节点到根节点的路径
		Set<TreeNode> pPath = new HashSet<>();
		while (p != null) {
			pPath.add(p);
			p = parentMap.get(p);
		}
		
		// 沿着q节点向上查找，第一个在p路径中的节点就是LCA
		while (q != null) {
			if (pPath.contains(q)) {
				return q;
			}
			q = parentMap.get(q);
		}
		
		return null; // 理论上不应该到达这里
	}
	
	/**
	 * 创建测试用的二叉树并返回特定节点
	 *        3
	 *       / \
	 *      5   1
	 *     / \/ \
	 *    6  2  0  8
	 *      / \
	 *     7   4
	 */
	private static TreeNode[] createTestTree() {
		TreeNode root = new TreeNode(3);
		TreeNode node5 = new TreeNode(5);
		TreeNode node1 = new TreeNode(1);
		TreeNode node6 = new TreeNode(6);
		TreeNode node2 = new TreeNode(2);
		TreeNode node0 = new TreeNode(0);
		TreeNode node8 = new TreeNode(8);
		TreeNode node7 = new TreeNode(7);
		TreeNode node4 = new TreeNode(4);
		
		root.left = node5;
		root.right = node1;
		node5.left = node6;
		node5.right = node2;
		node1.left = node0;
		node1.right = node8;
		node2.left = node7;
		node2.right = node4;
		
		// 返回根节点和测试用的目标节点
		return new TreeNode[]{root, node5, node1, node6, node4, node7, node8};
	}
	
	/**
	 * 打印树的结构（便于调试）
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
		
		for (int i = 0; i < level; i++) {
			System.out.print("    ");
		}
		
		System.out.println(prefix + ": " + node.val);
		printTreeHelper(node.left, level + 1, "L");
		printTreeHelper(node.right, level + 1, "R");
	}
	
	/**
	 * 性能测试方法
	 */
	private static void performanceTest() {
		// 创建一个较大的二叉树进行性能测试
		TreeNode largeTree = createLargeTree(15); // 创建深度为15的完全二叉树
		
		// 找出最深层的两个节点作为目标节点
		TreeNode p = findDeepestNode(largeTree, 1);
		TreeNode q = findDeepestNode(largeTree, 2);
		
		// 测试Morris方法
		long startTime = System.nanoTime();
		TreeNode morrisResult = lowestCommonAncestor(largeTree, p, q);
		long morrisTime = System.nanoTime() - startTime;
		
		// 测试递归方法
		startTime = System.nanoTime();
		TreeNode recursiveResult = lowestCommonAncestorRecursive(largeTree, p, q);
		long recursiveTime = System.nanoTime() - startTime;
		
		// 测试迭代方法
		startTime = System.nanoTime();
		TreeNode iterativeResult = lowestCommonAncestorIterative(largeTree, p, q);
		long iterativeTime = System.nanoTime() - startTime;
		
		System.out.println("\n性能测试结果:");
		System.out.println("Morris方法: " + morrisTime + " ns, LCA值: " + (morrisResult != null ? morrisResult.val : "null"));
		System.out.println("递归方法: " + recursiveTime + " ns, LCA值: " + (recursiveResult != null ? recursiveResult.val : "null"));
		System.out.println("迭代方法: " + iterativeTime + " ns, LCA值: " + (iterativeResult != null ? iterativeResult.val : "null"));
	}
	
	/**
	 * 创建一个大型完全二叉树用于性能测试
	 */
	private static TreeNode createLargeTree(int depth) {
		if (depth <= 0) {
			return null;
		}
		return createLargeTreeHelper(1, depth);
	}
	
	private static TreeNode createLargeTreeHelper(int val, int depth) {
		if (depth <= 0) {
			return null;
		}
		TreeNode node = new TreeNode(val);
		node.left = createLargeTreeHelper(2 * val, depth - 1);
		node.right = createLargeTreeHelper(2 * val + 1, depth - 1);
		return node;
	}
	
	/**
	 * 查找最深层的第n个节点
	 */
	private static TreeNode findDeepestNode(TreeNode root, int n) {
		if (root == null) {
			return null;
		}
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		List<TreeNode> deepestNodes = new ArrayList<>();
		
		// 层序遍历找到最深层的所有节点
		while (!queue.isEmpty()) {
			int levelSize = queue.size();
			deepestNodes.clear();
			
			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();
				deepestNodes.add(node);
				
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
		}
		
		// 返回第n个最深节点（如果存在）
		return deepestNodes.size() >= n ? deepestNodes.get(n - 1) : deepestNodes.get(0);
	}
	
	/**
	 * 主方法用于测试
	 */
	public static void main(String[] args) {
		TreeNode[] nodes = createTestTree();
		TreeNode root = nodes[0];
		TreeNode node5 = nodes[1];
		TreeNode node1 = nodes[2];
		TreeNode node6 = nodes[3];
		TreeNode node4 = nodes[4];
		TreeNode node7 = nodes[5];
		TreeNode node8 = nodes[6];
		
		System.out.println("测试树结构:");
		printTree(root);
		System.out.println();
		
		// 测试用例1: LCA(5, 1) 应该返回 3
		System.out.println("测试用例1 - LCA(5, 1):");
		TreeNode lca1Morris = lowestCommonAncestor(root, node5, node1);
		TreeNode lca1Recursive = lowestCommonAncestorRecursive(root, node5, node1);
		TreeNode lca1Iterative = lowestCommonAncestorIterative(root, node5, node1);
		System.out.println("Morris结果: " + (lca1Morris != null ? lca1Morris.val : "null"));
		System.out.println("递归结果: " + (lca1Recursive != null ? lca1Recursive.val : "null"));
		System.out.println("迭代结果: " + (lca1Iterative != null ? lca1Iterative.val : "null"));
		System.out.println();
		
		// 测试用例2: LCA(5, 4) 应该返回 5
		System.out.println("测试用例2 - LCA(5, 4):");
		TreeNode lca2Morris = lowestCommonAncestor(root, node5, node4);
		TreeNode lca2Recursive = lowestCommonAncestorRecursive(root, node5, node4);
		TreeNode lca2Iterative = lowestCommonAncestorIterative(root, node5, node4);
		System.out.println("Morris结果: " + (lca2Morris != null ? lca2Morris.val : "null"));
		System.out.println("递归结果: " + (lca2Recursive != null ? lca2Recursive.val : "null"));
		System.out.println("迭代结果: " + (lca2Iterative != null ? lca2Iterative.val : "null"));
		System.out.println();
		
		// 测试用例3: LCA(6, 4) 应该返回 5
		System.out.println("测试用例3 - LCA(6, 4):");
		TreeNode lca3Morris = lowestCommonAncestor(root, node6, node4);
		TreeNode lca3Recursive = lowestCommonAncestorRecursive(root, node6, node4);
		TreeNode lca3Iterative = lowestCommonAncestorIterative(root, node6, node4);
		System.out.println("Morris结果: " + (lca3Morris != null ? lca3Morris.val : "null"));
		System.out.println("递归结果: " + (lca3Recursive != null ? lca3Recursive.val : "null"));
		System.out.println("迭代结果: " + (lca3Iterative != null ? lca3Iterative.val : "null"));
		System.out.println();
		
		// 测试用例4: LCA(7, 8) 应该返回 3
		System.out.println("测试用例4 - LCA(7, 8):");
		TreeNode lca4Morris = lowestCommonAncestor(root, node7, node8);
		TreeNode lca4Recursive = lowestCommonAncestorRecursive(root, node7, node8);
		TreeNode lca4Iterative = lowestCommonAncestorIterative(root, node7, node8);
		System.out.println("Morris结果: " + (lca4Morris != null ? lca4Morris.val : "null"));
		System.out.println("递归结果: " + (lca4Recursive != null ? lca4Recursive.val : "null"));
		System.out.println("迭代结果: " + (lca4Iterative != null ? lca4Iterative.val : "null"));
		
		// 性能测试
		performanceTest();
	}
	
	/**
	 * 算法分析与总结：
	 * 
	 * 1. Morris遍历方法：
	 *    - 时间复杂度：O(n)，每个节点最多被访问两次
	 *    - 空间复杂度：O(1)，只使用常数额外空间
	 *    - 优点：空间效率极高，适用于内存受限环境
	 *    - 缺点：实现复杂，难以理解和维护，需要临时修改树结构
	 *    - 关键思路：利用Morris先序遍历找到第一个目标节点，再利用Morris中序遍历
	 *      查找左右子树中是否包含另一个目标节点，通过右边界检查确定LCA位置
	 * 
	 * 2. 递归方法：
	 *    - 时间复杂度：O(n)，访问每个节点一次
	 *    - 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 *    - 优点：实现简洁优雅，逻辑清晰
	 *    - 缺点：对于不平衡树可能导致栈溢出
	 *    - 关键思路：后序遍历，自底向上查找，当一个节点的左右子树分别包含p和q时，
	 *      该节点即为LCA；当一个节点等于p或q时，直接返回该节点
	 * 
	 * 3. 迭代方法（父指针法）：
	 *    - 时间复杂度：O(n)
	 *    - 空间复杂度：O(n)，需要存储父指针映射和路径集合
	 *    - 优点：避免递归可能的栈溢出问题
	 *    - 缺点：需要额外空间存储父指针信息
	 *    - 关键思路：使用BFS建立每个节点到父节点的映射，然后找出p到根的路径，
	 *      再从q向上查找，第一个在p路径中的节点即为LCA
	 * 
	 * 4. 适用场景选择：
	 *    - 内存受限环境：Morris方法最佳
	 *    - 代码简洁性和可维护性要求：递归方法最佳
	 *    - 处理大规模不平衡树：迭代方法可能更安全
	 *    - 工程实践中，递归方法通常是首选，因为它简单易懂且在大多数情况下表现良好
	 */
}

/*
C++版本实现参考：

#include <iostream>
#include <unordered_map>
#include <unordered_set>
#include <queue>
#include <vector>
using namespace std;

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
};

// Morris先序遍历寻找o1或o2
TreeNode* preOrder(TreeNode* head, TreeNode* o1, TreeNode* o2) {
    TreeNode* cur = head;
    TreeNode* mostRight = nullptr;
    TreeNode* ans = nullptr;
    
    while (cur != nullptr) {
        mostRight = cur->left;
        if (mostRight != nullptr) {
            while (mostRight->right != nullptr && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) {
                if (ans == nullptr && (cur == o1 || cur == o2)) {
                    ans = cur;
                }
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else {
                mostRight->right = nullptr;
            }
        } else {
            if (ans == nullptr && (cur == o1 || cur == o2)) {
                ans = cur;
            }
        }
        cur = cur->right;
    }
    
    return ans;
}

// 检查target是否在head的右边界上
bool rightCheck(TreeNode* head, TreeNode* target) {
    while (head != nullptr) {
        if (head == target) {
            return true;
        }
        head = head->right;
    }
    return false;
}

// Morris遍历求最低公共祖先
TreeNode* lowestCommonAncestorMorris(TreeNode* head, TreeNode* o1, TreeNode* o2) {
    // 检查是否一个节点是另一个的祖先
    if (preOrder(o1->left, o1, o2) != nullptr || preOrder(o1->right, o1, o2) != nullptr) {
        return o1;
    }
    if (preOrder(o2->left, o1, o2) != nullptr || preOrder(o2->right, o1, o2) != nullptr) {
        return o2;
    }
    
    TreeNode* left = preOrder(head, o1, o2);
    TreeNode* cur = head;
    TreeNode* mostRight = nullptr;
    TreeNode* lca = nullptr;
    
    while (cur != nullptr) {
        mostRight = cur->left;
        if (mostRight != nullptr) {
            while (mostRight->right != nullptr && mostRight->right != cur) {
                mostRight = mostRight->right;
            }
            
            if (mostRight->right == nullptr) {
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else {
                mostRight->right = nullptr;
                if (lca == nullptr) {
                    if (rightCheck(cur->left, left)) {
                        if (preOrder(left->right, o1, o2) != nullptr) {
                            lca = left;
                        }
                        left = cur;
                    }
                }
            }
        }
        cur = cur->right;
    }
    
    return lca != nullptr ? lca : left;
}

// 递归求最低公共祖先
TreeNode* lowestCommonAncestorRecursive(TreeNode* root, TreeNode* p, TreeNode* q) {
    if (root == nullptr || root == p || root == q) {
        return root;
    }
    
    TreeNode* left = lowestCommonAncestorRecursive(root->left, p, q);
    TreeNode* right = lowestCommonAncestorRecursive(root->right, p, q);
    
    if (left != nullptr && right != nullptr) {
        return root;
    }
    
    return left != nullptr ? left : right;
}

// 迭代求最低公共祖先（使用父指针）
TreeNode* lowestCommonAncestorIterative(TreeNode* root, TreeNode* p, TreeNode* q) {
    unordered_map<TreeNode*, TreeNode*> parentMap;
    queue<TreeNode*> qNodes;
    
    parentMap[root] = nullptr;
    qNodes.push(root);
    
    while (parentMap.find(p) == parentMap.end() || parentMap.find(q) == parentMap.end()) {
        TreeNode* node = qNodes.front();
        qNodes.pop();
        
        if (node->left != nullptr) {
            parentMap[node->left] = node;
            qNodes.push(node->left);
        }
        if (node->right != nullptr) {
            parentMap[node->right] = node;
            qNodes.push(node->right);
        }
    }
    
    unordered_set<TreeNode*> pPath;
    while (p != nullptr) {
        pPath.insert(p);
        p = parentMap[p];
    }
    
    while (q != nullptr) {
        if (pPath.find(q) != pPath.end()) {
            return q;
        }
        q = parentMap[q];
    }
    
    return nullptr;
}

// 创建测试树
vector<TreeNode*> createTestTree() {
    TreeNode* root = new TreeNode(3);
    TreeNode* node5 = new TreeNode(5);
    TreeNode* node1 = new TreeNode(1);
    TreeNode* node6 = new TreeNode(6);
    TreeNode* node2 = new TreeNode(2);
    TreeNode* node0 = new TreeNode(0);
    TreeNode* node8 = new TreeNode(8);
    TreeNode* node7 = new TreeNode(7);
    TreeNode* node4 = new TreeNode(4);
    
    root->left = node5;
    root->right = node1;
    node5->left = node6;
    node5->right = node2;
    node1->left = node0;
    node1->right = node8;
    node2->left = node7;
    node2->right = node4;
    
    return {root, node5, node1, node6, node4, node7, node8};
}

// 打印树结构
void printTreeHelper(TreeNode* node, int level, const string& prefix) {
    if (node == nullptr) {
        return;
    }
    
    for (int i = 0; i < level; i++) {
        cout << "    ";
    }
    
    cout << prefix << ": " << node->val << endl;
    printTreeHelper(node->left, level + 1, "L");
    printTreeHelper(node->right, level + 1, "R");
}

void printTree(TreeNode* root) {
    if (root == nullptr) {
        cout << "Empty tree" << endl;
        return;
    }
    printTreeHelper(root, 0, "H");
}

// 释放树内存
void deleteTree(TreeNode* root) {
    if (root == nullptr) {
        return;
    }
    deleteTree(root->left);
    deleteTree(root->right);
    delete root;
}

int main() {
    vector<TreeNode*> nodes = createTestTree();
    TreeNode* root = nodes[0];
    TreeNode* node5 = nodes[1];
    TreeNode* node1 = nodes[2];
    TreeNode* node6 = nodes[3];
    TreeNode* node4 = nodes[4];
    TreeNode* node7 = nodes[5];
    TreeNode* node8 = nodes[6];
    
    cout << "Test Tree Structure:" << endl;
    printTree(root);
    cout << endl;
    
    // 测试LCA(5, 1)
    TreeNode* lca1 = lowestCommonAncestorMorris(root, node5, node1);
    cout << "LCA(5, 1) (Morris): " << (lca1 != nullptr ? to_string(lca1->val) : "null") << endl;
    
    lca1 = lowestCommonAncestorRecursive(root, node5, node1);
    cout << "LCA(5, 1) (Recursive): " << (lca1 != nullptr ? to_string(lca1->val) : "null") << endl;
    
    lca1 = lowestCommonAncestorIterative(root, node5, node1);
    cout << "LCA(5, 1) (Iterative): " << (lca1 != nullptr ? to_string(lca1->val) : "null") << endl;
    
    // 清理内存
    deleteTree(root);
    
    return 0;
}

Python版本实现参考：

class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

def pre_order(head, o1, o2):
    """Morris先序遍历寻找o1或o2"""
    cur = head
    most_right = None
    ans = None
    
    while cur:
        most_right = cur.left
        if most_right:
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if not most_right.right:
                if ans is None and (cur == o1 or cur == o2):
                    ans = cur
                most_right.right = cur
                cur = cur.left
                continue
            else:
                most_right.right = None
        else:
            if ans is None and (cur == o1 or cur == o2):
                ans = cur
        cur = cur.right
    
    return ans

def right_check(head, target):
    """检查target是否在head的右边界上"""
    while head:
        if head == target:
            return True
        head = head.right
    return False

def lowest_common_ancestor_morris(head, o1, o2):
    """使用Morris遍历求最低公共祖先"""
    # 检查是否一个节点是另一个的祖先
    if pre_order(o1.left, o1, o2) or pre_order(o1.right, o1, o2):
        return o1
    if pre_order(o2.left, o1, o2) or pre_order(o2.right, o1, o2):
        return o2
    
    left = pre_order(head, o1, o2)
    cur = head
    most_right = None
    lca = None
    
    while cur:
        most_right = cur.left
        if most_right:
            while most_right.right and most_right.right != cur:
                most_right = most_right.right
            
            if not most_right.right:
                most_right.right = cur
                cur = cur.left
                continue
            else:
                most_right.right = None
                if lca is None:
                    if right_check(cur.left, left):
                        if pre_order(left.right, o1, o2):
                            lca = left
                        left = cur
        cur = cur.right
    
    return lca if lca is not None else left

def lowest_common_ancestor_recursive(root, p, q):
    """递归求最低公共祖先"""
    if root is None or root == p or root == q:
        return root
    
    left = lowest_common_ancestor_recursive(root.left, p, q)
    right = lowest_common_ancestor_recursive(root.right, p, q)
    
    if left is not None and right is not None:
        return root
    
    return left if left is not None else right

def lowest_common_ancestor_iterative(root, p, q):
    """迭代求最低公共祖先（使用父指针）"""
    parent_map = {root: None}
    queue = [root]
    
    # 构建父指针映射
    while p not in parent_map or q not in parent_map:
        node = queue.pop(0)
        
        if node.left:
            parent_map[node.left] = node
            queue.append(node.left)
        if node.right:
            parent_map[node.right] = node
            queue.append(node.right)
    
    # 收集p到根的路径
    p_path = set()
    while p:
        p_path.add(p)
        p = parent_map[p]
    
    # 从q向上查找LCA
    while q:
        if q in p_path:
            return q
        q = parent_map[q]
    
    return None

def create_test_tree():
    """创建测试树并返回相关节点"""
    root = TreeNode(3)
    node5 = TreeNode(5)
    node1 = TreeNode(1)
    node6 = TreeNode(6)
    node2 = TreeNode(2)
    node0 = TreeNode(0)
    node8 = TreeNode(8)
    node7 = TreeNode(7)
    node4 = TreeNode(4)
    
    root.left = node5
    root.right = node1
    node5.left = node6
    node5.right = node2
    node1.left = node0
    node1.right = node8
    node2.left = node7
    node2.right = node4
    
    return root, node5, node1, node6, node4, node7, node8

def print_tree_helper(node, level, prefix):
    """打印树结构辅助函数"""
    if node is None:
        return
    
    print("    " * level + f"{prefix}: {node.val}")
    print_tree_helper(node.left, level + 1, "L")
    print_tree_helper(node.right, level + 1, "R")

def print_tree(root):
    """打印树结构"""
    if root is None:
        print("Empty tree")
        return
    print_tree_helper(root, 0, "H")

def test():
    """测试函数"""
    root, node5, node1, node6, node4, node7, node8 = create_test_tree()
    
    print("Test Tree Structure:")
    print_tree(root)
    print()
    
    # 测试LCA(5, 1)
    lca = lowest_common_ancestor_morris(root, node5, node1)
    print(f"LCA(5, 1) (Morris): {lca.val if lca else 'None'}")
    
    lca = lowest_common_ancestor_recursive(root, node5, node1)
    print(f"LCA(5, 1) (Recursive): {lca.val if lca else 'None'}")
    
    lca = lowest_common_ancestor_iterative(root, node5, node1)
    print(f"LCA(5, 1) (Iterative): {lca.val if lca else 'None'}")

if __name__ == "__main__":
    test()
*/
