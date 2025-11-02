package class036;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 二叉树的锯齿形层序遍历
 * 测试链接 : https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
 * 
 * 锯齿形层序遍历是层序遍历的一个变种，要求按照锯齿形顺序遍历二叉树：
 * - 第一层（根节点）从左到右
 * - 第二层从右到左
 * - 第三层从左到右
 * - 以此类推，交替方向
 * 
 * 核心算法思想：
 * 1. 基于标准层序遍历，增加方向控制逻辑
 * 2. 可以通过以下几种方式实现方向交替：
 *    - 每一层收集节点后，根据层号决定是否反转结果
 *    - 使用双端队列，根据当前方向决定添加到队列的头部还是尾部
 *    - 使用两个栈交替存储不同层的节点，实现方向切换
 * 
 * 时间复杂度分析：
 * - 时间复杂度：O(N)，其中N是二叉树中的节点数，每个节点只被访问一次
 * - 空间复杂度：O(W)，其中W是二叉树的最大宽度，最坏情况下为O(N/2)≈O(N)
 * 
 * 关键优化点：
 * 1. 使用双端队列可以避免额外的反转操作，提高性能
 * 2. 使用数组实现队列可以进一步提升内存效率
 * 3. 使用两个栈的方法在某些情况下可能更直观
 * 
 * 边界情况处理：
 * 1. 空树：直接返回空列表
 * 2. 单节点树：返回只包含一个列表的列表
 * 3. 斜树（链状结构）：仍然能正确处理锯齿形遍历
 * 
 * 相关题目：
 * 1. LeetCode 103. 二叉树的锯齿形层序遍历 (本文件) - https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
 * 2. LintCode 71. 二叉树的锯齿形层次遍历 - https://www.lintcode.com/problem/71/
 * 3. HackerRank Tree: Zig Zag Level Order Traversal - https://www.hackerrank.com/challenges/tree-zigzag-level-order-traversal/problem
 * 4. CodeChef Zigzag Tree Traversal - https://www.codechef.com/problems/ZIGZAGT
 * 5. GeeksforGeeks ZigZag Tree Traversal - https://www.geeksforgeeks.org/zigzag-tree-traversal/
 * 6. POJ 3278. Catch That Cow - https://poj.org/problem?id=3278 (类似BFS方向问题)
 * 7. Codeforces 1335B. Construct the String - https://codeforces.com/problemset/problem/1335/B
 * 8. AcWing 102. 最佳牛围栏 - https://www.acwing.com/problem/content/104/
 * 9. 牛客 NC15. 求二叉树的层序遍历 - https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3
 * 10. 剑指 Offer 32 - III. 从上到下打印二叉树 III - https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof/
 * 
 * 工程化考量：
 * 1. 异常处理：需要考虑空指针异常和数据溢出问题
 * 2. 可配置性：MAXN参数可以根据实际需求调整
 * 3. 线程安全：多线程环境下需要额外的同步机制
 * 4. 内存管理：对于大数据量的树，需要注意内存使用情况
 * 
 * 算法变体与扩展：
 * 1. 可以调整起始方向（从左到右或从右到左）
 * 2. 可以每k层切换一次方向，而不是每层切换
 * 3. 在图像处理中可应用于边缘检测和特征提取
 * 4. 在网络路由算法中有类似的分层方向搜索策略
 */
public class Code02_ZigzagLevelOrderTraversal {

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

	// 如果测试数据量变大了就修改这个值
	// 注意：在工程实践中，可能需要动态扩容或使用更灵活的数据结构
	public static int MAXN = 2001;

	// 使用数组模拟队列，提高访问效率
	public static TreeNode[] queue = new TreeNode[MAXN];

	// 队列的左右指针
	public static int l, r;

	/**
	 * 方法1：使用数组队列实现的锯齿形层序遍历 - 推荐解法
	 * 时间复杂度：O(N)，每个节点只被访问一次
	 * 空间复杂度：O(W)，W为树的最大宽度
	 * 优点：
	 * 1. 不需要额外的集合类，内存效率高
	 * 2. 可以直接按照指定顺序收集节点值，避免了额外的反转操作
	 * 3. 数组实现的队列比LinkedList具有更好的缓存局部性
	 * 核心思想：
	 * - 使用两个循环：第一个循环按照当前方向收集节点值
	 * - 第二个循环将子节点按顺序加入队列，保持正确的层次结构
	 * - 通过reverse标志位控制每层的遍历方向
	 */
	public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root != null) {
			l = r = 0;
			queue[r++] = root;
			// false 代表从左往右，true 代表从右往左
			boolean reverse = false; 
			while (l < r) {
				int size = r - l; // 当前层的节点数量
				ArrayList<Integer> list = new ArrayList<Integer>();
				
				// 第一遍循环：根据当前遍历方向收集节点值
				// reverse == false: 从左往右，顺序遍历队列中的当前层节点
				// reverse == true: 从右往左，逆序遍历队列中的当前层节点
				for (int i = reverse ? r - 1 : l, j = reverse ? -1 : 1, k = 0; k < size; i += j, k++) {
					TreeNode cur = queue[i];
					list.add(cur.val);
				}
				
				// 第二遍循环：将当前层所有节点的子节点按顺序加入队列
				for (int i = 0; i < size; i++) {
					TreeNode cur = queue[l++];
					if (cur.left != null) {
						queue[r++] = cur.left;
					}
					if (cur.right != null) {
						queue[r++] = cur.right;
					}
				}
				
				ans.add(list);
				reverse = !reverse; // 反转下一层的遍历方向
			}
		}
		return ans;
	}

	/**
	 * 方法2：使用LinkedList实现的锯齿形层序遍历
	 * 时间复杂度：O(N)，每个节点只被访问一次，但反转操作会增加一些常数时间
	 * 空间复杂度：O(N)
	 * 优点：
	 * 1. 代码更加简洁易懂，逻辑清晰
	 * 2. 使用标准库的Queue接口，实现更加规范
	 * 缺点：
	 * 1. 在偶数层需要额外的反转操作，增加了时间常数
	 * 2. LinkedList的节点开销比数组大
	 * 实现思路：
	 * - 先进行标准的层序遍历，收集每层的节点值
	 * - 根据当前层数决定是否反转结果列表
	 * - 适合代码可读性要求高于极致性能的场景
	 */
	public static List<List<Integer>> zigzagLevelOrder2(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) return ans;
		
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		boolean reverse = false; // 控制遍历方向
		
		while (!queue.isEmpty()) {
			int size = queue.size();
			ArrayList<Integer> level = new ArrayList<>();
			
			// 处理当前层的所有节点
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				level.add(node.val);
				
				// 将子节点加入队列
				if (node.left != null) queue.offer(node.left);
				if (node.right != null) queue.offer(node.right);
			}
			
			// 如果需要反转，反转当前层的结果
			if (reverse) {
				Collections.reverse(level);
			}
			
			ans.add(level);
			reverse = !reverse; // 切换方向
		}
		
		return ans;
	}

	/**
	 * 辅助方法：创建测试用的二叉树
	 * 根据数组创建二叉树，null表示空节点
	 * 数组格式：层序遍历顺序，如[3,9,20,null,null,15,7]
	 */
	public static TreeNode createTree(Integer[] nums) {
		if (nums == null || nums.length == 0) return null;
		
		TreeNode root = new TreeNode(nums[0]);
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		
		int index = 1;
		while (!queue.isEmpty() && index < nums.length) {
			TreeNode node = queue.poll();
			
			// 添加左子节点
			if (index < nums.length && nums[index] != null) {
				node.left = new TreeNode(nums[index]);
				queue.offer(node.left);
			}
			index++;
			
			// 添加右子节点
			if (index < nums.length && nums[index] != null) {
				node.right = new TreeNode(nums[index]);
				queue.offer(node.right);
			}
			index++;
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
		// 测试用例1: [3,9,20,null,null,15,7]
		//       3
		//      / \
		//     9  20
		//       /  \
		//      15   7
		System.out.println("========== 测试用例1：标准二叉树 ==========");
		TreeNode root1 = createTree(new Integer[]{3, 9, 20, null, null, 15, 7});
		System.out.println("树结构：");
		printTree(root1);
		
		System.out.println("\n方法1 锯齿形遍历 (数组实现):");
		for (List<Integer> level : zigzagLevelOrder(root1)) {
			System.out.println(level);
		}
		
		System.out.println("\n方法2 锯齿形遍历 (LinkedList实现):");
		for (List<Integer> level : zigzagLevelOrder2(root1)) {
			System.out.println(level);
		}
		
		// 测试用例2: [1] - 单节点树
		System.out.println("\n\n========== 测试用例2：单节点树 ==========");
		TreeNode root2 = createTree(new Integer[]{1});
		System.out.println("树结构：");
		printTree(root2);
		System.out.println("\n锯齿形遍历:");
		for (List<Integer> level : zigzagLevelOrder(root2)) {
			System.out.println(level);
		}
		
		// 测试用例3: [] - 空树
		System.out.println("\n\n========== 测试用例3：空树 ==========");
		TreeNode root3 = null;
		System.out.println("树结构：");
		printTree(root3);
		System.out.println("\n锯齿形遍历:");
		System.out.println(zigzagLevelOrder(root3));
		
		// 测试用例4: [1,2,3,4,5,6,7] - 完全二叉树
		System.out.println("\n\n========== 测试用例4：完全二叉树 ==========");
		//       1
		//      / \
		//     2   3
		//    / \ / \
		//   4  5 6  7
		TreeNode root4 = createTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
		System.out.println("树结构：");
		printTree(root4);
		System.out.println("\n锯齿形遍历:");
		for (List<Integer> level : zigzagLevelOrder(root4)) {
			System.out.println(level);
		}
		
		// 测试用例5: [1,2,null,3,null,4,null] - 斜树（链状结构）
		System.out.println("\n\n========== 测试用例5：斜树 ==========");
		TreeNode root5 = createTree(new Integer[]{1, 2, null, 3, null, 4, null});
		System.out.println("树结构：");
		printTree(root5);
		System.out.println("\n锯齿形遍历:");
		for (List<Integer> level : zigzagLevelOrder(root5)) {
			System.out.println(level);
		}
		
		// 性能对比分析
		System.out.println("\n\n========== 性能对比分析 ==========");
		System.out.println("1. 方法1（数组队列）：内存效率更高，常数时间更小，适合大数据量");
		System.out.println("2. 方法2（LinkedList+反转）：代码更简洁，但有额外的反转开销");
		System.out.println("3. 空间复杂度：两种方法均为O(N)，但方法1的内存使用更高效");
		System.out.println("4. 实际应用中，对于大型树，方法1通常表现更好");
	}
}

/*
Python实现:

class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class ZigzagLevelOrderTraversal:
    # 方法1：使用deque实现的锯齿形层序遍历
    def zigzagLevelOrder1(self, root: TreeNode) -> list[list[int]]:
        """
        使用双端队列实现锯齿形层序遍历
        时间复杂度: O(N)
        空间复杂度: O(N)
        """
        result = []
        if not root:
            return result
        
        from collections import deque
        queue = deque([root])
        reverse = False  # 控制遍历方向
        
        while queue:
            size = len(queue)
            level = deque()
            
            # 处理当前层的所有节点
            for _ in range(size):
                node = queue.popleft()
                
                # 根据当前方向决定添加到level的左侧还是右侧
                if reverse:
                    level.appendleft(node.val)
                else:
                    level.append(node.val)
                
                # 将子节点加入队列
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            result.append(list(level))
            reverse = not reverse  # 切换方向
        
        return result
    
    # 方法2：使用常规BFS + 反转偶数层
    def zigzagLevelOrder2(self, root: TreeNode) -> list[list[int]]:
        """
        使用常规层序遍历，然后反转偶数层
        时间复杂度: O(N)
        空间复杂度: O(N)
        """
        result = []
        if not root:
            return result
        
        from collections import deque
        queue = deque([root])
        level_num = 0  # 记录当前层数
        
        while queue:
            size = len(queue)
            level = []
            
            for _ in range(size):
                node = queue.popleft()
                level.append(node.val)
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            # 偶数层（从0开始计数）保持原样，奇数层反转
            if level_num % 2 == 1:
                level.reverse()
            
            result.append(level)
            level_num += 1
        
        return result
    
    # 方法3：使用两个栈实现
    def zigzagLevelOrder3(self, root: TreeNode) -> list[list[int]]:
        """
        使用两个栈交替存储不同层的节点
        时间复杂度: O(N)
        空间复杂度: O(N)
        """
        result = []
        if not root:
            return result
        
        # 使用两个栈，分别存储奇数层和偶数层的节点
        stack1 = [root]  # 存储奇数层节点，从左到右
        stack2 = []      # 存储偶数层节点，从右到左
        
        while stack1 or stack2:
            # 处理奇数层
            if stack1:
                level = []
                while stack1:
                    node = stack1.pop()
                    level.append(node.val)
                    # 先左子节点，后右子节点
                    if node.left:
                        stack2.append(node.left)
                    if node.right:
                        stack2.append(node.right)
                result.append(level)
            
            # 处理偶数层
            elif stack2:
                level = []
                while stack2:
                    node = stack2.pop()
                    level.append(node.val)
                    # 先右子节点，后左子节点
                    if node.right:
                        stack1.append(node.right)
                    if node.left:
                        stack1.append(node.left)
                result.append(level)
        
        return result
    
    # 辅助方法：创建二叉树
    def createTree(self, nums: list) -> TreeNode:
        if not nums:
            return None
        
        root = TreeNode(nums[0])
        from collections import deque
        queue = deque([root])
        
        index = 1
        while queue and index < len(nums):
            node = queue.popleft()
            
            # 添加左子节点
            if index < len(nums) and nums[index] is not None:
                node.left = TreeNode(nums[index])
                queue.append(node.left)
            index += 1
            
            # 添加右子节点
            if index < len(nums) and nums[index] is not None:
                node.right = TreeNode(nums[index])
                queue.append(node.right)
            index += 1
        
        return root

# 测试代码
if __name__ == "__main__":
    solution = ZigzagLevelOrderTraversal()
    
    # 测试用例1: [3,9,20,None,None,15,7]
    root1 = solution.createTree([3, 9, 20, None, None, 15, 7])
    print("测试用例1 (双端队列实现):")
    print(solution.zigzagLevelOrder1(root1))
    
    # 测试用例2: [1]
    root2 = solution.createTree([1])
    print("\n测试用例2:")
    print(solution.zigzagLevelOrder1(root2))
    
    # 测试用例3: []
    root3 = solution.createTree([])
    print("\n测试用例3:")
    print(solution.zigzagLevelOrder1(root3))
    
    # 测试用例4: [1,2,3,4,5,6,7]
    root4 = solution.createTree([1, 2, 3, 4, 5, 6, 7])
    print("\n测试用例4 (三种实现对比):")
    print("方法1:", solution.zigzagLevelOrder1(root4))
    print("方法2:", solution.zigzagLevelOrder2(root4))
    print("方法3:", solution.zigzagLevelOrder3(root4))

C++实现:

#include <iostream>
#include <vector>
#include <queue>
#include <stack>
#include <algorithm>
#include <deque>
using namespace std;

struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

class ZigzagLevelOrderTraversal {
public:
    // 方法1：使用双端队列实现的锯齿形层序遍历
    vector<vector<int>> zigzagLevelOrder1(TreeNode* root) {
        vector<vector<int>> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        bool reverse = false; // 控制遍历方向
        
        while (!q.empty()) {
            int size = q.size();
            deque<int> level;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* node = q.front();
                q.pop();
                
                // 根据当前方向决定添加到level的左侧还是右侧
                if (reverse) {
                    level.push_front(node->val);
                } else {
                    level.push_back(node->val);
                }
                
                if (node->left) q.push(node->left);
                if (node->right) q.push(node->right);
            }
            
            // 将双端队列转换为vector并添加到结果中
            result.push_back(vector<int>(level.begin(), level.end()));
            reverse = !reverse; // 切换方向
        }
        
        return result;
    }
    
    // 方法2：使用常规BFS + 反转偶数层
    vector<vector<int>> zigzagLevelOrder2(TreeNode* root) {
        vector<vector<int>> result;
        if (!root) return result;
        
        queue<TreeNode*> q;
        q.push(root);
        int levelNum = 0; // 记录当前层数
        
        while (!q.empty()) {
            int size = q.size();
            vector<int> level;
            
            for (int i = 0; i < size; ++i) {
                TreeNode* node = q.front();
                q.pop();
                level.push_back(node->val);
                
                if (node->left) q.push(node->left);
                if (node->right) q.push(node->right);
            }
            
            // 奇数层(从0开始计数)需要反转
            if (levelNum % 2 == 1) {
                reverse(level.begin(), level.end());
            }
            
            result.push_back(level);
            levelNum++;
        }
        
        return result;
    }
    
    // 方法3：使用两个栈实现
    vector<vector<int>> zigzagLevelOrder3(TreeNode* root) {
        vector<vector<int>> result;
        if (!root) return result;
        
        stack<TreeNode*> stack1; // 存储奇数层节点
        stack<TreeNode*> stack2; // 存储偶数层节点
        stack1.push(root);
        
        while (!stack1.empty() || !stack2.empty()) {
            // 处理奇数层
            if (!stack1.empty()) {
                vector<int> level;
                while (!stack1.empty()) {
                    TreeNode* node = stack1.top();
                    stack1.pop();
                    level.push_back(node->val);
                    
                    // 先左后右，这样出栈顺序是右左
                    if (node->left) stack2.push(node->left);
                    if (node->right) stack2.push(node->right);
                }
                result.push_back(level);
            }
            // 处理偶数层
            else if (!stack2.empty()) {
                vector<int> level;
                while (!stack2.empty()) {
                    TreeNode* node = stack2.top();
                    stack2.pop();
                    level.push_back(node->val);
                    
                    // 先右后左，这样出栈顺序是左右
                    if (node->right) stack1.push(node->right);
                    if (node->left) stack1.push(node->left);
                }
                result.push_back(level);
            }
        }
        
        return result;
    }
    
    // 辅助方法：创建二叉树
    TreeNode* createTree(const vector<int*>& nums) {
        if (nums.empty() || !nums[0]) return nullptr;
        
        TreeNode* root = new TreeNode(*nums[0]);
        queue<TreeNode*> q;
        q.push(root);
        
        int index = 1;
        while (!q.empty() && index < nums.size()) {
            TreeNode* node = q.front();
            q.pop();
            
            // 添加左子节点
            if (index < nums.size() && nums[index]) {
                node->left = new TreeNode(*nums[index]);
                q.push(node->left);
            }
            index++;
            
            // 添加右子节点
            if (index < nums.size() && nums[index]) {
                node->right = new TreeNode(*nums[index]);
                q.push(node->right);
            }
            index++;
        }
        
        return root;
    }
    
    // 辅助方法：释放树内存
    void deleteTree(TreeNode* root) {
        if (root) {
            deleteTree(root->left);
            deleteTree(root->right);
            delete root;
        }
    }
    
    // 辅助方法：打印结果
    void printResult(const vector<vector<int>>& result) {
        cout << "[" << endl;
        for (const auto& level : result) {
            cout << "  [";
            for (size_t i = 0; i < level.size(); ++i) {
                cout << level[i];
                if (i < level.size() - 1) cout << ", ";
            }
            cout << "]" << endl;
        }
        cout << "]" << endl;
    }
};

// 测试代码
int main() {
    ZigzagLevelOrderTraversal solution;
    
    // 测试用例1: [3,9,20,null,null,15,7]
    vector<int*> nums1 = {new int(3), new int(9), new int(20), nullptr, nullptr, new int(15), new int(7)};
    TreeNode* root1 = solution.createTree(nums1);
    cout << "测试用例1 (双端队列实现):" << endl;
    solution.printResult(solution.zigzagLevelOrder1(root1));
    
    // 测试用例2: [1]
    vector<int*> nums2 = {new int(1)};
    TreeNode* root2 = solution.createTree(nums2);
    cout << "\n测试用例2:" << endl;
    solution.printResult(solution.zigzagLevelOrder1(root2));
    
    // 测试用例3: []
    vector<int*> nums3 = {};
    TreeNode* root3 = solution.createTree(nums3);
    cout << "\n测试用例3:" << endl;
    solution.printResult(solution.zigzagLevelOrder1(root3));
    
    // 测试用例4: [1,2,3,4,5,6,7]
    vector<int*> nums4 = {new int(1), new int(2), new int(3), new int(4), new int(5), new int(6), new int(7)};
    TreeNode* root4 = solution.createTree(nums4);
    cout << "\n测试用例4 (三种实现对比):" << endl;
    cout << "方法1:" << endl;
    solution.printResult(solution.zigzagLevelOrder1(root4));
    cout << "\n方法2:" << endl;
    solution.printResult(solution.zigzagLevelOrder2(root4));
    cout << "\n方法3:" << endl;
    solution.printResult(solution.zigzagLevelOrder3(root4));
    
    // 释放内存
    solution.deleteTree(root1);
    solution.deleteTree(root2);
    solution.deleteTree(root3);
    solution.deleteTree(root4);
    
    // 释放nums中的int指针
    for (auto p : nums1) if (p) delete p;
    for (auto p : nums2) if (p) delete p;
    for (auto p : nums4) if (p) delete p;
    
    return 0;
}
*/
