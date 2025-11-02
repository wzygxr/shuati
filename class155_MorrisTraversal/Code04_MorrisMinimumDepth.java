package class124;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Morris遍历求二叉树最小高度
 * 测试链接 : https://leetcode.cn/problems/minimum-depth-of-binary-tree/
 * LeetCode 111. Minimum Depth of Binary Tree
 * 
 * 题目来源：
 * - 二叉树最小深度：LeetCode 111. Minimum Depth of Binary Tree
 *   链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/
 * 
 * Morris遍历是一种空间复杂度为O(1)的二叉树遍历算法，通过临时修改树的结构（利用叶子节点的空闲指针）
 * 来避免使用栈或递归调用栈所需的额外空间。算法的核心思想是将树转换为一个线索二叉树。
 * 
 * 本实现包含：
 * 1. Java语言的Morris遍历计算最小深度
 * 2. 递归版本的计算最小深度
 * 3. 迭代版本的计算最小深度（BFS）
 * 4. 详细的注释和算法解析
 * 5. 完整的测试用例
 * 6. C++和Python语言的完整实现
 * 
 * 三种语言实现链接：
 * - Java: 当前文件
 * - Python: https://leetcode.cn/problems/minimum-depth-of-binary-tree/solution/python-morris-qiu-er-cha-shu-zui-xiao-gao-du-by-xxx/
 * - C++: https://leetcode.cn/problems/minimum-depth-of-binary-tree/solution/c-morris-qiu-er-cha-shu-zui-xiao-gao-du-by-xxx/
 * 
 * 算法详解：
 * 利用Morris中序遍历计算二叉树的最小深度，通过记录遍历过程中的层数来确定叶子节点的深度
 * 1. 在Morris遍历过程中维护当前节点所在的层数
 * 2. 当第二次访问节点时，检查其左子树的最右节点是否为叶子节点
 * 3. 最后检查整棵树的最右节点是否为叶子节点
 * 4. 返回所有叶子节点深度中的最小值
 * 
 * 时间复杂度：O(n)，空间复杂度：O(1)
 * 适用场景：内存受限环境中计算大规模二叉树的最小深度
 * 优缺点分析：
 * - 优点：空间复杂度最优，适用于内存极度受限的环境
 * - 缺点：实现复杂，需要精确维护层数信息
 */
public class Code04_MorrisMinimumDepth {

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
	 * 使用Morris遍历计算二叉树的最小深度
	 * 时间复杂度: O(n)，空间复杂度: O(1)
	 * 
	 * @param head 二叉树的根节点
	 * @return 二叉树的最小深度（从根节点到最近叶子节点的最短路径上的节点数量）
	 */
	public static int minDepth(TreeNode head) {
		// 处理空树情况
		if (head == null) {
			return 0;
		}
		
		TreeNode cur = head;      // 当前节点
		TreeNode mostRight = null; // 当前节点左子树的最右节点
		int preLevel = 0;         // 上一个节点所在的层数
		int rightLen;             // 树的右边界长度
		int ans = Integer.MAX_VALUE; // 记录最小深度
		
		// Morris中序遍历主循环
		while (cur != null) {
			mostRight = cur.left;
			
			// 如果当前节点有左子树
			if (mostRight != null) {
				// 计算当前节点左子树的右边界长度
				rightLen = 1;
				while (mostRight.right != null && mostRight.right != cur) {
					rightLen++;
					mostRight = mostRight.right;
				}
				
				// 第一次访问当前节点
				if (mostRight.right == null) {
					preLevel++;
					mostRight.right = cur; // 建立线索化连接
					cur = cur.left;       // 移动到左子树
					continue;
				} else {
					// 第二次访问当前节点
					// 检查是否是叶子节点
					if (mostRight.left == null) {
						ans = Math.min(ans, preLevel);
					}
					preLevel -= rightLen; // 回溯层数
					mostRight.right = null; // 恢复树的结构
				}
			} else {
				// 没有左子树，直接增加层数
				preLevel++;
			}
			
			// 移动到右子树
			cur = cur.right;
		}
		
		// 处理特殊情况：整棵树的最右节点可能是叶子节点
		rightLen = 1;
		cur = head;
		while (cur.right != null) {
			rightLen++;
			cur = cur.right;
		}
		
		// 整棵树的最右节点是叶节点才纳入统计
		if (cur.left == null) {
			ans = Math.min(ans, rightLen);
		}
		
		return ans;
	}
	
	/**
	 * 递归方法计算二叉树的最小深度
	 * 时间复杂度: O(n)，空间复杂度: O(h)，h为树高
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	public static int minDepthRecursive(TreeNode root) {
		// 处理空树情况
		if (root == null) {
			return 0;
		}
		
		// 叶子节点
		if (root.left == null && root.right == null) {
			return 1;
		}
		
		// 只有右子树
		if (root.left == null) {
			return 1 + minDepthRecursive(root.right);
		}
		
		// 只有左子树
		if (root.right == null) {
			return 1 + minDepthRecursive(root.left);
		}
		
		// 左右子树都存在，取较小值
		return 1 + Math.min(minDepthRecursive(root.left), minDepthRecursive(root.right));
	}
	
	/**
	 * 迭代方法计算二叉树的最小深度（BFS）
	 * 使用广度优先搜索可以在找到第一个叶子节点时立即返回，提高效率
	 * 时间复杂度: O(n)，空间复杂度: O(n)
	 * 
	 * @param root 二叉树的根节点
	 * @return 二叉树的最小深度
	 */
	public static int minDepthIterative(TreeNode root) {
		// 处理空树情况
		if (root == null) {
			return 0;
		}
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int depth = 0;
		
		while (!queue.isEmpty()) {
			depth++;
			int levelSize = queue.size();
			
			// 遍历当前层的所有节点
			for (int i = 0; i < levelSize; i++) {
				TreeNode node = queue.poll();
				
				// 如果是叶子节点，直接返回当前深度
				if (node.left == null && node.right == null) {
					return depth;
				}
				
				// 将非空子节点加入队列
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
		}
		
		return depth;
	}
	
	/**
	 * 创建一个测试用的二叉树
	 *        3
	 *       / \
	 *      9  20
	 *        /  \
	 *       15   7
	 */
	private static TreeNode createTestTree1() {
		TreeNode root = new TreeNode(3);
		root.left = new TreeNode(9);
		root.right = new TreeNode(20);
		root.right.left = new TreeNode(15);
		root.right.right = new TreeNode(7);
		return root;
	}
	
	/**
	 * 创建另一个测试用的二叉树
	 *    2
	 *     \
	 *      3
	 *       \
	 *        4
	 *         \
	 *          5
	 *           \
	 *            6
	 */
	private static TreeNode createTestTree2() {
		TreeNode root = new TreeNode(2);
		root.right = new TreeNode(3);
		root.right.right = new TreeNode(4);
		root.right.right.right = new TreeNode(5);
		root.right.right.right.right = new TreeNode(6);
		return root;
	}
	
	/**
	 * 创建单节点二叉树
	 */
	private static TreeNode createTestTree3() {
		return new TreeNode(1);
	}
	
	/**
	 * 创建完全二叉树测试用例
	 *        1
	 *       / \
	 *      2   3
	 *     / \
	 *    4   5
	 */
	private static TreeNode createTestTree4() {
		TreeNode root = new TreeNode(1);
		root.left = new TreeNode(2);
		root.right = new TreeNode(3);
		root.left.left = new TreeNode(4);
		root.left.right = new TreeNode(5);
		return root;
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
		
		// 测试Morris方法
		long startTime = System.nanoTime();
		int morrisResult = minDepth(largeTree);
		long morrisTime = System.nanoTime() - startTime;
		
		// 测试递归方法
		startTime = System.nanoTime();
		int recursiveResult = minDepthRecursive(largeTree);
		long recursiveTime = System.nanoTime() - startTime;
		
		// 测试迭代方法
		startTime = System.nanoTime();
		int iterativeResult = minDepthIterative(largeTree);
		long iterativeTime = System.nanoTime() - startTime;
		
		System.out.println("\n性能测试结果:");
		System.out.println("Morris方法: " + morrisTime + " ns, 结果: " + morrisResult);
		System.out.println("递归方法: " + recursiveTime + " ns, 结果: " + recursiveResult);
		System.out.println("迭代方法: " + iterativeTime + " ns, 结果: " + iterativeResult);
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
	 * 主方法用于测试
	 */
	public static void main(String[] args) {
		// 测试用例1
		TreeNode tree1 = createTestTree1();
		System.out.println("测试用例1:");
		printTree(tree1);
		System.out.println("Morris最小深度: " + minDepth(tree1));
		System.out.println("递归最小深度: " + minDepthRecursive(tree1));
		System.out.println("迭代最小深度: " + minDepthIterative(tree1));
		System.out.println();
		
		// 测试用例2
		TreeNode tree2 = createTestTree2();
		System.out.println("测试用例2:");
		printTree(tree2);
		System.out.println("Morris最小深度: " + minDepth(tree2));
		System.out.println("递归最小深度: " + minDepthRecursive(tree2));
		System.out.println("迭代最小深度: " + minDepthIterative(tree2));
		System.out.println();
		
		// 测试用例3
		TreeNode tree3 = createTestTree3();
		System.out.println("测试用例3:");
		printTree(tree3);
		System.out.println("Morris最小深度: " + minDepth(tree3));
		System.out.println("递归最小深度: " + minDepthRecursive(tree3));
		System.out.println("迭代最小深度: " + minDepthIterative(tree3));
		System.out.println();
		
		// 测试用例4
		TreeNode tree4 = createTestTree4();
		System.out.println("测试用例4:");
		printTree(tree4);
		System.out.println("Morris最小深度: " + minDepth(tree4));
		System.out.println("递归最小深度: " + minDepthRecursive(tree4));
		System.out.println("迭代最小深度: " + minDepthIterative(tree4));
		
		// 空树测试
		System.out.println("\n空树测试:");
		System.out.println("Morris最小深度: " + minDepth(null));
		System.out.println("递归最小深度: " + minDepthRecursive(null));
		System.out.println("迭代最小深度: " + minDepthIterative(null));
		
		// 性能测试
		performanceTest();
	}
	
	/**
	 * 算法分析与总结：
	 * 
	 * 1. Morris遍历方法：
	 *    - 时间复杂度：O(n)，每个节点最多被访问两次
	 *    - 空间复杂度：O(1)，只使用常数额外空间
	 *    - 优点：空间效率高，适用于内存受限环境
	 *    - 缺点：实现复杂，难以理解，需要修改树结构（临时）
	 * 
	 * 2. 递归方法：
	 *    - 时间复杂度：O(n)，访问每个节点一次
	 *    - 空间复杂度：O(h)，h为树高，最坏情况O(n)
	 *    - 优点：实现简洁，易于理解
	 *    - 缺点：对于不平衡树可能导致栈溢出
	 * 
	 * 3. 迭代BFS方法：
	 *    - 时间复杂度：O(n)，在最坏情况下访问所有节点
	 *    - 空间复杂度：O(w)，w为最大宽度，最坏情况O(n)
	 *    - 优点：可以在找到第一个叶子节点时立即返回，对于宽树可能比递归更快
	 *    - 缺点：需要使用队列存储节点
	 * 
	 * 4. 适用场景选择：
	 *    - 内存受限环境：Morris方法最佳
	 *    - 代码简洁性要求：递归方法最佳
	 *    - 平衡树或需要快速找到浅层叶子节点：BFS迭代方法最佳
	 */
}

/*
C++版本实现参考：

#include <iostream>
#include <queue>
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

// Morris遍历计算最小深度
int minDepthMorris(TreeNode* head) {
    if (!head) return 0;
    
    TreeNode* cur = head;
    TreeNode* mostRight = nullptr;
    int preLevel = 0;
    int rightLen;
    int ans = INT_MAX;
    
    while (cur) {
        mostRight = cur->left;
        if (mostRight) {
            rightLen = 1;
            while (mostRight->right && mostRight->right != cur) {
                rightLen++;
                mostRight = mostRight->right;
            }
            
            if (!mostRight->right) {
                preLevel++;
                mostRight->right = cur;
                cur = cur->left;
                continue;
            } else {
                if (!mostRight->left) {
                    ans = min(ans, preLevel);
                }
                preLevel -= rightLen;
                mostRight->right = nullptr;
            }
        } else {
            preLevel++;
        }
        cur = cur->right;
    }
    
    // 处理最右节点
    rightLen = 1;
    cur = head;
    while (cur->right) {
        rightLen++;
        cur = cur->right;
    }
    
    if (!cur->left) {
        ans = min(ans, rightLen);
    }
    
    return ans;
}

// 递归方法计算最小深度
int minDepthRecursive(TreeNode* root) {
    if (!root) return 0;
    
    if (!root->left && !root->right) return 1;
    
    if (!root->left) return 1 + minDepthRecursive(root->right);
    if (!root->right) return 1 + minDepthRecursive(root->left);
    
    return 1 + min(minDepthRecursive(root->left), minDepthRecursive(root->right));
}

// BFS迭代方法计算最小深度
int minDepthIterative(TreeNode* root) {
    if (!root) return 0;
    
    queue<TreeNode*> q;
    q.push(root);
    int depth = 0;
    
    while (!q.empty()) {
        depth++;
        int levelSize = q.size();
        
        for (int i = 0; i < levelSize; i++) {
            TreeNode* node = q.front();
            q.pop();
            
            if (!node->left && !node->right) return depth;
            
            if (node->left) q.push(node->left);
            if (node->right) q.push(node->right);
        }
    }
    
    return depth;
}

// 测试函数
int main() {
    // 创建测试树1
    TreeNode* tree1 = new TreeNode(3);
    tree1->left = new TreeNode(9);
    tree1->right = new TreeNode(20);
    tree1->right->left = new TreeNode(15);
    tree1->right->right = new TreeNode(7);
    
    cout << "Tree 1 min depth (Morris): " << minDepthMorris(tree1) << endl;
    cout << "Tree 1 min depth (Recursive): " << minDepthRecursive(tree1) << endl;
    cout << "Tree 1 min depth (Iterative): " << minDepthIterative(tree1) << endl;
    
    // 清理内存
    // 此处省略内存清理代码
    
    return 0;
}

Python版本实现参考：

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

def min_depth_morris(head):
    if not head:
        return 0
    
    cur = head
    most_right = None
    pre_level = 0
    right_len = 0
    ans = float('inf')
    
    while cur:
        most_right = cur.left
        if most_right:
            right_len = 1
            while most_right.right and most_right.right != cur:
                right_len += 1
                most_right = most_right.right
            
            if not most_right.right:
                pre_level += 1
                most_right.right = cur
                cur = cur.left
                continue
            else:
                if not most_right.left:
                    ans = min(ans, pre_level)
                pre_level -= right_len
                most_right.right = None
        else:
            pre_level += 1
        cur = cur.right
    
    # 处理最右节点
    right_len = 1
    cur = head
    while cur.right:
        right_len += 1
        cur = cur.right
    
    if not cur.left:
        ans = min(ans, right_len)
    
    return ans

def min_depth_recursive(root):
    if not root:
        return 0
    
    if not root.left and not root.right:
        return 1
    
    if not root.left:
        return 1 + min_depth_recursive(root.right)
    if not root.right:
        return 1 + min_depth_recursive(root.left)
    
    return 1 + min(min_depth_recursive(root.left), min_depth_recursive(root.right))

def min_depth_iterative(root):
    if not root:
        return 0
    
    from collections import deque
    queue = deque([root])
    depth = 0
    
    while queue:
        depth += 1
        level_size = len(queue)
        
        for _ in range(level_size):
            node = queue.popleft()
            
            if not node.left and not node.right:
                return depth
            
            if node.left:
                queue.append(node.left)
            if node.right:
                queue.append(node.right)
    
    return depth

# 测试代码
def test():
    # 创建测试树1
    tree1 = TreeNode(3)
    tree1.left = TreeNode(9)
    tree1.right = TreeNode(20)
    tree1.right.left = TreeNode(15)
    tree1.right.right = TreeNode(7)
    
    print(f"Tree 1 min depth (Morris): {min_depth_morris(tree1)}")
    print(f"Tree 1 min depth (Recursive): {min_depth_recursive(tree1)}")
    print(f"Tree 1 min depth (Iterative): {min_depth_iterative(tree1)}")

if __name__ == "__main__":
    test()
*/
