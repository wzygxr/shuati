package class036;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树最大宽度计算
 * 
 * 核心算法思想：
 * - 使用层序遍历（BFS）为每个节点分配一个虚拟索引
 * - 对于完全二叉树，假设根节点索引为1，则左子节点索引为2*i，右子节点索引为2*i+1
 * - 每层的宽度等于最后一个节点索引减去第一个节点索引再加1
 * - 记录所有层中的最大宽度
 * 
 * 关键优化点：
 * - 索引溢出问题：对于深层树，直接使用2*i的索引方式会导致整数溢出
 * - 优化方案：每层重新编号，减去当前层第一个节点的索引，确保索引不会溢出
 * - 实现效率：使用数组实现队列比使用标准库队列性能更好
 * 
 * 边界情况处理：
 * - 空树：返回0
 * - 单节点树：返回1
 * - 只有一侧子树的树：需要正确处理中间的null节点
 * 
 * 时间复杂度分析：
 * - O(N)，其中N是二叉树中的节点总数，每个节点只访问一次
 * 
 * 空间复杂度分析：
 * - O(N)，队列最多存储树中最宽的一层的所有节点
 * 
 * 工程化考量：
 * - 使用long类型存储索引，避免整数溢出
 * - 在C++中需要手动管理内存，避免内存泄漏
 * - 在Java中使用数组实现队列可以提升性能
 * - 在Python中可以使用动态扩展的列表
 * 
 * 相关题目（穷尽各大平台）：
 * 1. LeetCode 662. Maximum Width of Binary Tree - 原题
 *    链接：https://leetcode.cn/problems/maximum-width-of-binary-tree/
 * 2. LeetCode 116. Populating Next Right Pointers in Each Node - 层序遍历应用
 *    链接：https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
 * 3. LeetCode 117. Populating Next Right Pointers in Each Node II - 通用二叉树层序遍历
 *    链接：https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/
 * 4. LeetCode 102. Binary Tree Level Order Traversal - 基础层序遍历
 *    链接：https://leetcode.cn/problems/binary-tree-level-order-traversal/
 * 5. LeetCode 515. Find Largest Value in Each Tree Row - 层序遍历求最大值
 *    链接：https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
 * 6. LintCode 97. Maximum Depth of Binary Tree - 层序遍历求深度
 *    链接：https://www.lintcode.com/problem/97/
 * 7. HackerRank Tree Level Order Traversal - 标准层序遍历
 *    链接：https://www.hackerrank.com/challenges/tree-level-order-traversal
 * 8. CodeChef TREE2 - 树的宽度相关问题
 *    链接：https://www.codechef.com/problems/TREE2
 * 9. POJ 1661 Help Jimmy - 树结构处理
 *    链接：http://poj.org/problem?id=1661
 * 10. HDU 1026 Ignatius and the Princess I - BFS应用
 *    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1026
 * 11. AcWing 847. 图中点的层次 - BFS层序遍历
 *    链接：https://www.acwing.com/problem/content/849/
 * 12. 剑指Offer 32 - I. 从上到下打印二叉树 - 层序遍历基础
 *    链接：https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-lcof/
 * 13. 牛客NC15 求二叉树的层序遍历 - 层序遍历应用
 *    链接：https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3
 */
public class Code03_WidthOfBinaryTree1 {

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
			return "TreeNode{val=" + val + "}";
		}
	}

	/**
	 * 方法1：使用数组实现的队列进行层序遍历
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 * 优点：数组实现的队列比LinkedList效率更高，常数更小
	 * 缺点：需要预先定义最大容量
	 */
	// 如果测试数据量变大了就修改这个值
	public static int MAXN = 3001;

	// 节点队列
	public static TreeNode[] nq = new TreeNode[MAXN];

	// 索引队列，存储每个节点的虚拟索引
	public static long[] iq = new long[MAXN];

	// 队列的左右指针
	public static int l, r;

	/**
	 * 计算二叉树的最大宽度 - 数组队列实现
	 * 
	 * 实现思路：
	 * - 使用两个数组分别存储节点和对应的索引
	 * - 对于每个节点，其左子节点索引为2*parentIndex，右子节点索引为2*parentIndex+1
	 * - 每层处理时，计算当前层的宽度（最后一个节点索引 - 第一个节点索引 + 1）
	 * 
	 * 优点：
	 * - 性能优于使用标准库队列，避免了对象创建的开销
	 * - 数组访问速度快
	 * 
	 * 缺点：
	 * - 需要预先定义最大容量
	 * - 对于深层树可能存在索引溢出问题
	 */
	public static int widthOfBinaryTree(TreeNode root) {
		// 边界检查
		if (root == null) {
			return 0;
		}
		
		int ans = 1;  // 最小宽度为1（只有根节点）
		l = r = 0;    // 初始化队列指针
		
		// 根节点入队，索引为1
		nq[r] = root;
		iq[r++] = 1;
		
		// 层序遍历
		while (l < r) {
			int size = r - l;  // 当前层的节点数
			
			// 计算当前层的宽度（最后一个节点索引 - 第一个节点索引 + 1）
			ans = Math.max(ans, (int) (iq[r - 1] - iq[l] + 1));
			
			// 处理当前层的所有节点
			for (int i = 0; i < size; i++) {
				TreeNode node = nq[l];
				long id = iq[l++];  // 当前节点的索引
				
				// 左子节点索引为 2 * id
				if (node.left != null) {
					nq[r] = node.left;
					iq[r++] = id * 2;
				}
				
				// 右子节点索引为 2 * id + 1
				if (node.right != null) {
					nq[r] = node.right;
					iq[r++] = id * 2 + 1;
				}
			}
		}
		
		return ans;
	}
	
	/**
	 * 计算二叉树的最大宽度 - 标准库队列实现
	 * 
	 * 实现思路：
	 * - 使用Java标准库的Queue和自定义Pair类存储节点和索引
	 * - Pair类封装了节点和其对应的索引
	 * - 层序遍历过程中，记录每层的第一个和最后一个节点的索引
	 * 
	 * 优点：
	 * - 实现简单，代码可读性好
	 * - 队列自动调整大小，无需预先定义容量
	 * 
	 * 缺点：
	 * - 每个节点需要创建Pair对象，有一定的内存开销
	 * - 性能略低于数组实现
	 */
	public static int widthOfBinaryTreeWithQueue(TreeNode root) {
		if (root == null) {
			return 0;
		}
		
		int maxWidth = 0;
		// 使用队列存储节点和其索引
		Queue<Pair<TreeNode, Long>> queue = new LinkedList<>();
		queue.offer(new Pair<>(root, 1L));  // 根节点索引为1
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			long firstIndex = -1;
			long lastIndex = -1;
			
			for (int i = 0; i < size; i++) {
				Pair<TreeNode, Long> pair = queue.poll();
				TreeNode node = pair.first;
				long index = pair.second;
				
				// 记录当前层的第一个和最后一个节点索引
				if (i == 0) {
					firstIndex = index;
				}
				if (i == size - 1) {
					lastIndex = index;
				}
				
				// 子节点入队
				if (node.left != null) {
					queue.offer(new Pair<>(node.left, 2 * index));
				}
				if (node.right != null) {
					queue.offer(new Pair<>(node.right, 2 * index + 1));
				}
			}
			
			// 更新最大宽度
			maxWidth = Math.max(maxWidth, (int) (lastIndex - firstIndex + 1));
		}
		
		return maxWidth;
	}
	
	/**
	 * 节点和索引的配对类
	 * 用于在层序遍历过程中同时跟踪节点和其对应的虚拟索引
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
	 * 
	 * @param type 测试树类型：
	 *             0: LeetCode示例
	 *             1: 完全二叉树
	 *             2: 只有左子树
	 *             3: 只有右子树
	 *             4: 空节点较多的树
	 * @return 生成的测试树
	 */
	public static TreeNode generateTestTree(int type) {
		switch (type) {
			case 1: // 完全二叉树
				TreeNode root1 = new TreeNode(1);
				root1.left = new TreeNode(3);
				root1.right = new TreeNode(2);
				root1.left.left = new TreeNode(5);
				root1.left.right = new TreeNode(3);
				root1.right.right = new TreeNode(9);
				return root1;
			case 2: // 只有左子树
				TreeNode root2 = new TreeNode(1);
				root2.left = new TreeNode(3);
				root2.left.left = new TreeNode(5);
				root2.left.left.left = new TreeNode(7);
				return root2;
			case 3: // 只有右子树
				TreeNode root3 = new TreeNode(1);
				root3.right = new TreeNode(3);
				root3.right.right = new TreeNode(5);
				root3.right.right.right = new TreeNode(7);
				return root3;
			case 4: // 空节点较多的树
				TreeNode root4 = new TreeNode(1);
				root4.left = new TreeNode(3);
				root4.right = new TreeNode(2);
				root4.left.left = new TreeNode(5);
				root4.left.right = null;
				root4.right.left = null;
				root4.right.right = new TreeNode(9);
				return root4;
			default: // LeetCode示例
				TreeNode root = new TreeNode(1);
				root.left = new TreeNode(3);
				root.right = new TreeNode(2);
				root.left.left = new TreeNode(5);
				root.left.right = new TreeNode(3);
				root.right.right = new TreeNode(9);
				return root;
		}
	}
	
	/**
	 * 打印树的结构（简化版）
	 * 
	 * 打印格式：[1, 3, 2, 5, 3, null, 9]
	 * 按照层序遍历的顺序，null表示不存在的节点
	 * 移除末尾多余的null，使输出更简洁
	 */
	public static void printTree(TreeNode root) {
		if (root == null) {
			System.out.println("[null]");
			return;
		}
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (node != null) {
					sb.append(node.val);
					queue.offer(node.left);
					queue.offer(node.right);
				} else {
					sb.append("null");
				}
				if (!queue.isEmpty() || i < size - 1) {
					sb.append(", ");
				}
			}
		}
		
		// 移除末尾多余的null
		int lastValidIndex = sb.length() - 1;
		while (lastValidIndex >= 0 && 
		       (sb.charAt(lastValidIndex) == 'n' || 
		        sb.charAt(lastValidIndex) == 'u' || 
		        sb.charAt(lastValidIndex) == 'l' || 
		        sb.charAt(lastValidIndex) == ',' || 
		        sb.charAt(lastValidIndex) == ' ')) {
			lastValidIndex--;
		}
		
		sb.replace(lastValidIndex + 1, sb.length(), "");
		sb.append("]");
		System.out.println(sb.toString());
	}
	
	public static void main(String[] args) {
		// 测试不同类型的树
		for (int i = 0; i <= 4; i++) {
			System.out.println("\n测试树类型 " + i + ":");
			TreeNode root = generateTestTree(i);
			System.out.print("树结构: ");
			printTree(root);
			
			// 使用数组队列实现
			int width1 = widthOfBinaryTree(root);
			System.out.println("数组队列实现最大宽度: " + width1);
			
			// 使用标准库队列实现
			int width2 = widthOfBinaryTreeWithQueue(root);
			System.out.println("标准库队列实现最大宽度: " + width2);
		}
		
		// 边界情况测试
		System.out.println("\n边界情况测试:");
		
		// 空树
		System.out.println("空树最大宽度: " + widthOfBinaryTree(null));
		
		// 单节点
		TreeNode singleNode = new TreeNode(1);
		System.out.println("单节点树最大宽度: " + widthOfBinaryTree(singleNode));
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

class WidthOfBinaryTree1:
    """
    二叉树最大宽度 - 解法1
    使用数组实现队列进行层序遍历，效率更高
    时间复杂度: O(N)
    空间复杂度: O(N)
    """
    def widthOfBinaryTree(self, root: TreeNode) -> int:
        """
        计算二叉树的最大宽度
        使用数组实现的队列
        """
        if not root:
            return 0
        
        # 预先定义数组大小（Python中可以动态扩展）
        MAXN = 3001
        nq = [None] * MAXN  # 节点队列
        iq = [0] * MAXN     # 索引队列
        l, r = 0, 0
        
        nq[r] = root
        iq[r] = 1
        r += 1
        
        max_width = 1
        
        while l < r:
            size = r - l
            # 计算当前层的宽度
            current_width = iq[r - 1] - iq[l] + 1
            if current_width > max_width:
                max_width = current_width
            
            # 处理当前层的所有节点
            for _ in range(size):
                node = nq[l]
                index = iq[l]
                l += 1
                
                # 左子节点入队
                if node.left:
                    nq[r] = node.left
                    iq[r] = index * 2
                    r += 1
                
                # 右子节点入队
                if node.right:
                    nq[r] = node.right
                    iq[r] = index * 2 + 1
                    r += 1
            
            # 如果队列已满，扩展队列（Python特有处理）
            if r >= MAXN - 10:
                MAXN *= 2
                new_nq = [None] * MAXN
                new_iq = [0] * MAXN
                new_nq[:r] = nq[:r]
                new_iq[:r] = iq[:r]
                nq, iq = new_nq, new_iq
        
        return max_width
    
    def widthOfBinaryTreeWithQueue(self, root: TreeNode) -> int:
        """
        计算二叉树的最大宽度
        使用Python标准库的deque实现
        时间复杂度: O(N)
        空间复杂度: O(N)
        """
        if not root:
            return 0
        
        from collections import deque
        max_width = 0
        # 使用双端队列存储(节点, 索引)元组
        queue = deque()
        queue.append((root, 1))  # 根节点索引为1
        
        while queue:
            size = len(queue)
            first_index = None
            last_index = None
            
            for i in range(size):
                node, index = queue.popleft()
                
                # 记录当前层的第一个和最后一个节点索引
                if i == 0:
                    first_index = index
                if i == size - 1:
                    last_index = index
                
                # 子节点入队
                if node.left:
                    queue.append((node.left, 2 * index))
                if node.right:
                    queue.append((node.right, 2 * index + 1))
            
            # 更新最大宽度
            current_width = last_index - first_index + 1
            if current_width > max_width:
                max_width = current_width
        
        return max_width
    
    def widthOfBinaryTreeOptimized(self, root: TreeNode) -> int:
        """
        优化版本：避免索引溢出
        通过每层重新编号的方式，减去当前层第一个节点的索引
        防止大深度树的索引溢出问题
        """
        if not root:
            return 0
        
        from collections import deque
        max_width = 0
        queue = deque([(root, 0)])  # 根节点索引为0
        
        while queue:
            size = len(queue)
            level_start = queue[0][1]  # 当前层第一个节点的索引
            first = last = 0
            
            for i in range(size):
                node, index = queue.popleft()
                # 重新编号，避免溢出
                index -= level_start
                
                if i == 0:
                    first = index
                if i == size - 1:
                    last = index
                
                # 子节点入队，使用新的索引计算方式
                if node.left:
                    queue.append((node.left, index * 2))
                if node.right:
                    queue.append((node.right, index * 2 + 1))
            
            max_width = max(max_width, last - first + 1)
        
        return max_width
    
    def generateTestTree(self, tree_type: int) -> TreeNode:
        """
        生成不同类型的测试树
        """
        if tree_type == 1:  # 完全二叉树
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.right = TreeNode(2)
            root.left.left = TreeNode(5)
            root.left.right = TreeNode(3)
            root.right.right = TreeNode(9)
            return root
        elif tree_type == 2:  # 只有左子树
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.left.left = TreeNode(5)
            root.left.left.left = TreeNode(7)
            return root
        elif tree_type == 3:  # 只有右子树
            root = TreeNode(1)
            root.right = TreeNode(3)
            root.right.right = TreeNode(5)
            root.right.right.right = TreeNode(7)
            return root
        elif tree_type == 4:  # 空节点较多的树
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.right = TreeNode(2)
            root.left.left = TreeNode(5)
            root.left.right = None
            root.right.left = None
            root.right.right = TreeNode(9)
            return root
        else:  # LeetCode示例
            root = TreeNode(1)
            root.left = TreeNode(3)
            root.right = TreeNode(2)
            root.left.left = TreeNode(5)
            root.left.right = TreeNode(3)
            root.right.right = TreeNode(9)
            return root
    
    def printTree(self, root: TreeNode) -> None:
        """
        打印树的结构（简化版）
        """
        if not root:
            print("[null]")
            return
        
        from collections import deque
        queue = deque([root])
        result = []
        
        while queue:
            node = queue.popleft()
            if node:
                result.append(str(node.val))
                queue.append(node.left)
                queue.append(node.right)
            else:
                result.append("null")
        
        # 移除末尾多余的null
        while result and result[-1] == "null":
            result.pop()
        
        print("[" + ", ".join(result) + "]")

# 测试代码
if __name__ == "__main__":
    solution = WidthOfBinaryTree1()
    
    # 测试不同类型的树
    for i in range(5):
        print(f"\n测试树类型 {i}:")
        root = solution.generateTestTree(i)
        print("树结构:", end=" ")
        solution.printTree(root)
        
        # 使用数组队列实现
        width1 = solution.widthOfBinaryTree(root)
        print(f"数组队列实现最大宽度: {width1}")
        
        # 使用标准库队列实现
        width2 = solution.widthOfBinaryTreeWithQueue(root)
        print(f"标准库队列实现最大宽度: {width2}")
        
        # 使用优化版本
        width3 = solution.widthOfBinaryTreeOptimized(root)
        print(f"优化版本最大宽度: {width3}")
    
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
#include <sstream>
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

class WidthOfBinaryTree1 {
public:
    /**
     * 方法1：使用数组实现的队列进行层序遍历
     * 时间复杂度: O(N)
     * 空间复杂度: O(N)
     */
    // 可以根据需要调整最大容量
    static const int MAXN = 3001;
    
    /**
     * 计算二叉树的最大宽度
     * @param root 二叉树根节点
     * @return 最大宽度
     */
    int widthOfBinaryTree(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        // 定义数组队列
        TreeNode* nq[MAXN] = {nullptr};
        long long iq[MAXN] = {0};
        int l = 0, r = 0;
        
        // 根节点入队
        nq[r] = root;
        iq[r++] = 1;
        
        int maxWidth = 1;
        
        while (l < r) {
            int size = r - l;
            // 计算当前层的宽度
            long long currentWidth = iq[r - 1] - iq[l] + 1;
            if (currentWidth > maxWidth) {
                maxWidth = static_cast<int>(currentWidth);
            }
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; ++i) {
                TreeNode* node = nq[l];
                long long index = iq[l];
                l++;
                
                // 左子节点入队
                if (node->left) {
                    nq[r] = node->left;
                    iq[r] = index * 2;
                    r++;
                }
                
                // 右子节点入队
                if (node->right) {
                    nq[r] = node->right;
                    iq[r] = index * 2 + 1;
                    r++;
                }
            }
        }
        
        return maxWidth;
    }
    
    /**
     * 方法2：使用STL的queue实现
     * 时间复杂度: O(N)
     * 空间复杂度: O(N)
     */
    int widthOfBinaryTreeWithQueue(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int maxWidth = 0;
        // 使用队列存储节点和索引的pair
        queue<pair<TreeNode*, long long>> q;
        q.push({root, 1LL});  // 根节点索引为1
        
        while (!q.empty()) {
            int size = q.size();
            long long firstIndex = -1;
            long long lastIndex = -1;
            
            for (int i = 0; i < size; ++i) {
                auto [node, index] = q.front();
                q.pop();
                
                // 记录当前层的第一个和最后一个节点索引
                if (i == 0) {
                    firstIndex = index;
                }
                if (i == size - 1) {
                    lastIndex = index;
                }
                
                // 子节点入队
                if (node->left) {
                    q.push({node->left, 2 * index});
                }
                if (node->right) {
                    q.push({node->right, 2 * index + 1});
                }
            }
            
            // 更新最大宽度
            long long currentWidth = lastIndex - firstIndex + 1;
            if (currentWidth > maxWidth) {
                maxWidth = static_cast<int>(currentWidth);
            }
        }
        
        return maxWidth;
    }
    
    /**
     * 方法3：优化版本，避免索引溢出
     * 通过每层重新编号的方式
     */
    int widthOfBinaryTreeOptimized(TreeNode* root) {
        if (!root) {
            return 0;
        }
        
        int maxWidth = 0;
        queue<pair<TreeNode*, long long>> q;
        q.push({root, 0LL});  // 根节点索引为0
        
        while (!q.empty()) {
            int size = q.size();
            long long levelStart = q.front().second;  // 当前层第一个节点的索引
            long long first = 0, last = 0;
            
            for (int i = 0; i < size; ++i) {
                auto [node, index] = q.front();
                q.pop();
                
                // 重新编号，避免溢出
                index -= levelStart;
                
                if (i == 0) {
                    first = index;
                }
                if (i == size - 1) {
                    last = index;
                }
                
                // 子节点入队，使用新的索引计算方式
                if (node->left) {
                    q.push({node->left, index * 2});
                }
                if (node->right) {
                    q.push({node->right, index * 2 + 1});
                }
            }
            
            maxWidth = max(maxWidth, static_cast<int>(last - first + 1));
        }
        
        return maxWidth;
    }
    
    /**
     * 生成测试用例
     */
    TreeNode* generateTestTree(int treeType) {
        switch (treeType) {
            case 1: {
                // 完全二叉树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->right = new TreeNode(2);
                root->left->left = new TreeNode(5);
                root->left->right = new TreeNode(3);
                root->right->right = new TreeNode(9);
                return root;
            }
            case 2: {
                // 只有左子树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->left->left = new TreeNode(5);
                root->left->left->left = new TreeNode(7);
                return root;
            }
            case 3: {
                // 只有右子树
                TreeNode* root = new TreeNode(1);
                root->right = new TreeNode(3);
                root->right->right = new TreeNode(5);
                root->right->right->right = new TreeNode(7);
                return root;
            }
            case 4: {
                // 空节点较多的树
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->right = new TreeNode(2);
                root->left->left = new TreeNode(5);
                root->left->right = nullptr;
                root->right->left = nullptr;
                root->right->right = new TreeNode(9);
                return root;
            }
            default: {
                // LeetCode示例
                TreeNode* root = new TreeNode(1);
                root->left = new TreeNode(3);
                root->right = new TreeNode(2);
                root->left->left = new TreeNode(5);
                root->left->right = new TreeNode(3);
                root->right->right = new TreeNode(9);
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
     * 打印树的结构
     */
    void printTree(TreeNode* root) {
        if (!root) {
            cout << "[null]" << endl;
            return;
        }
        
        vector<string> result;
        queue<TreeNode*> q;
        q.push(root);
        
        while (!q.empty()) {
            TreeNode* node = q.front();
            q.pop();
            
            if (node) {
                result.push_back(to_string(node->val));
                q.push(node->left);
                q.push(node->right);
            } else {
                result.push_back("null");
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
    WidthOfBinaryTree1 solution;
    
    // 测试不同类型的树
    for (int i = 0; i < 5; ++i) {
        cout << "\n测试树类型 " << i << ":" << endl;
        TreeNode* root = solution.generateTestTree(i);
        cout << "树结构: ";
        solution.printTree(root);
        
        // 使用数组队列实现
        int width1 = solution.widthOfBinaryTree(root);
        cout << "数组队列实现最大宽度: " << width1 << endl;
        
        // 使用STL队列实现
        int width2 = solution.widthOfBinaryTreeWithQueue(root);
        cout << "STL队列实现最大宽度: " << width2 << endl;
        
        // 使用优化版本
        int width3 = solution.widthOfBinaryTreeOptimized(root);
        cout << "优化版本最大宽度: " << width3 << endl;
        
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
