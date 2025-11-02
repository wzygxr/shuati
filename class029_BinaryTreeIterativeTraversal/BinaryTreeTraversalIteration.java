import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.LinkedList;
import java.util.Queue;

// 自定义Pair类，用于存储键值对
class Pair<K, V> {
    private K key;
    private V value;
    
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }
    
    public K getKey() {
        return key;
    }
    
    public V getValue() {
        return value;
    }
}

// 不用递归，用迭代的方式实现二叉树的三序遍历
public class BinaryTreeTraversalIteration {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int v) {
			val = v;
		}

    // ==================== 以下是新增的补充题目 ====================
    
    /**
     * 题目：LeetCode 112 - 路径总和
     * 题目来源：https://leetcode.cn/problems/path-sum/
     * 题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum 。
     * 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum 。
     * 如果存在，返回 true ；否则，返回 false 。
     * 
     * 解题思路：
     * 1. 使用深度优先搜索（DFS）遍历二叉树
     * 2. 从根节点开始，每次遍历到一个节点时，将当前累计和减去节点值
     * 3. 如果到达叶子节点且累计和为0，则返回true
     * 4. 否则继续递归遍历左右子树
     * 
     * 时间复杂度：O(n) - 需要遍历树中的所有节点
     * 空间复杂度：O(h) - 递归调用栈的深度，h为树的高度
     * 是否为最优解：是，DFS是解决此类路径问题的最优方法
     */
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        // 边界情况：空树
        if (root == null) {
            return false;
        }
        
        // 非递归DFS实现 - 使用栈同时存储节点和当前路径和
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, targetSum - root.val));
        
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.pop();
            TreeNode node = current.getKey();
            int remainingSum = current.getValue();
            
            // 如果是叶子节点且剩余和为0，找到符合条件的路径
            if (node.left == null && node.right == null && remainingSum == 0) {
                return true;
            }
            
            // 先压入右子节点，这样保证左子节点先被处理（DFS顺序）
            if (node.right != null) {
                stack.push(new Pair<>(node.right, remainingSum - node.right.val));
            }
            if (node.left != null) {
                stack.push(new Pair<>(node.left, remainingSum - node.left.val));
            }
        }
        
        return false;
    }
    
    /**
     * 题目：LeetCode 113 - 路径总和 II
     * 题目来源：https://leetcode.cn/problems/path-sum-ii/
     * 题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum ，
     * 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
     * 
     * 解题思路：
     * 1. 使用回溯算法（深度优先搜索）
     * 2. 维护一个当前路径列表，记录已经走过的节点值
     * 3. 当到达叶子节点且路径和等于目标和时，将当前路径加入结果集
     * 4. 否则继续递归搜索左右子树
     * 
     * 时间复杂度：O(n²) - 每个节点访问一次，最坏情况下需要将路径复制n次
     * 空间复杂度：O(h) - 递归调用栈和路径列表的空间，h为树的高度
     * 是否为最优解：是，回溯是寻找所有路径的标准方法
     */
    public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 非递归DFS实现
        Stack<Pair<TreeNode, Integer>> nodeStack = new Stack<>();
        Stack<List<Integer>> pathStack = new Stack<>();
        
        // 初始化：将根节点和路径加入栈中
        nodeStack.push(new Pair<>(root, root.val));
        List<Integer> initialPath = new ArrayList<>();
        initialPath.add(root.val);
        pathStack.push(initialPath);
        
        while (!nodeStack.isEmpty()) {
            Pair<TreeNode, Integer> current = nodeStack.pop();
            TreeNode node = current.getKey();
            int currentSum = current.getValue();
            List<Integer> currentPath = pathStack.pop();
            
            // 如果是叶子节点且和等于目标值，加入结果集
            if (node.left == null && node.right == null && currentSum == targetSum) {
                result.add(new ArrayList<>(currentPath));
            }
            
            // 先处理右子树（栈是后进先出，所以右子树先入栈）
            if (node.right != null) {
                int rightSum = currentSum + node.right.val;
                nodeStack.push(new Pair<>(node.right, rightSum));
                
                List<Integer> rightPath = new ArrayList<>(currentPath);
                rightPath.add(node.right.val);
                pathStack.push(rightPath);
            }
            
            // 再处理左子树
            if (node.left != null) {
                int leftSum = currentSum + node.left.val;
                nodeStack.push(new Pair<>(node.left, leftSum));
                
                List<Integer> leftPath = new ArrayList<>(currentPath);
                leftPath.add(node.left.val);
                pathStack.push(leftPath);
            }
        }
        
        return result;
    }
    
    /**
     * 题目：LeetCode 129 - 求根节点到叶节点数字之和
     * 题目来源：https://leetcode.cn/problems/sum-root-to-leaf-numbers/
     * 题目描述：给你一个二叉树的根节点 root ，树中每个节点都存放有一个 0 到 9 之间的数字。
     * 每条从根节点到叶节点的路径都代表一个数字。
     * 例如，从根节点到叶节点的路径 1 -> 2 -> 3 表示数字 123 。
     * 计算从根节点到叶节点生成的 所有数字之和 。
     * 
     * 解题思路：
     * 1. 使用深度优先搜索遍历二叉树
     * 2. 维护一个当前路径代表的数字
     * 3. 当到达叶子节点时，将当前数字加入总和
     * 
     * 时间复杂度：O(n) - 每个节点只访问一次
     * 空间复杂度：O(h) - 递归栈的深度
     * 是否为最优解：是，DFS是解决此类路径问题的高效方法
     */
    public static int sumNumbers(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 非递归DFS实现
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, root.val));
        int totalSum = 0;
        
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.pop();
            TreeNode node = current.getKey();
            int currentNumber = current.getValue();
            
            // 如果是叶子节点，将当前数字加入总和
            if (node.left == null && node.right == null) {
                totalSum += currentNumber;
            } else {
                // 非叶子节点，继续向下遍历
                if (node.right != null) {
                    stack.push(new Pair<>(node.right, currentNumber * 10 + node.right.val));
                }
                if (node.left != null) {
                    stack.push(new Pair<>(node.left, currentNumber * 10 + node.left.val));
                }
            }
        }
        
        return totalSum;
    }
    
    /**
     * 题目：LeetCode 257 - 二叉树的所有路径
     * 题目来源：https://leetcode.cn/problems/binary-tree-paths/
     * 题目描述：给你一个二叉树的根节点 root ，按 任意顺序 ，
     * 返回所有从根节点到叶子节点的路径。
     * 
     * 解题思路：
     * 1. 使用回溯算法（DFS）
     * 2. 维护当前路径字符串
     * 3. 当到达叶子节点时，将完整路径加入结果集
     * 4. 继续递归处理左右子树
     * 
     * 时间复杂度：O(n) - 每个节点访问一次，路径字符串拼接可能需要O(n)时间
     * 空间复杂度：O(h) - 递归栈的深度
     * 是否为最优解：是，DFS是生成所有路径的标准方法
     */
    public static List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        
        // 非递归DFS实现
        Stack<Pair<TreeNode, String>> stack = new Stack<>();
        stack.push(new Pair<>(root, String.valueOf(root.val)));
        
        while (!stack.isEmpty()) {
            Pair<TreeNode, String> current = stack.pop();
            TreeNode node = current.getKey();
            String path = current.getValue();
            
            // 如果是叶子节点，将路径加入结果集
            if (node.left == null && node.right == null) {
                result.add(path);
            } else {
                // 非叶子节点，继续向下遍历
                if (node.right != null) {
                    stack.push(new Pair<>(node.right, path + "->" + node.right.val));
                }
                if (node.left != null) {
                    stack.push(new Pair<>(node.left, path + "->" + node.left.val));
                }
            }
        }
        
        return result;
    }
    
    /**
     * 题目：LeetCode 1448 - 统计二叉树中好节点的数目
     * 题目来源：https://leetcode.cn/problems/count-good-nodes-in-binary-tree/
     * 题目描述：给你一棵根为 root 的二叉树，请你返回二叉树中好节点的数目。
     * 「好节点」X 定义为：从根到该节点 X 所经过的节点中，没有任何节点的值大于 X 的值。
     * 
     * 解题思路：
     * 1. 使用深度优先搜索遍历二叉树
     * 2. 维护从根到当前节点路径上的最大值
     * 3. 如果当前节点的值大于等于该最大值，则为好节点，更新最大值
     * 4. 继续递归处理左右子树
     * 
     * 时间复杂度：O(n) - 每个节点只访问一次
     * 空间复杂度：O(h) - 递归栈的深度
     * 是否为最优解：是，DFS是解决此类路径最大值问题的最优方法
     */
    public static int goodNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 非递归DFS实现
        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, root.val)); // (节点, 路径最大值)
        int goodCount = 0;
        
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.pop();
            TreeNode node = current.getKey();
            int maxSoFar = current.getValue();
            
            // 判断是否为好节点
            if (node.val >= maxSoFar) {
                goodCount++;
                maxSoFar = node.val; // 更新路径最大值
            }
            
            // 继续处理左右子树
            if (node.right != null) {
                stack.push(new Pair<>(node.right, maxSoFar));
            }
            if (node.left != null) {
                stack.push(new Pair<>(node.left, maxSoFar));
            }
        }
        
        return goodCount;
    }
    
    /**
     * 题目：剑指Offer 26 - 树的子结构
     * 题目来源：https://leetcode.cn/problems/shu-de-zi-jie-gou-lcof/
     * 题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
     * 约定空树不是任意一个树的子结构。
     * 
     * 解题思路：
     * 1. 先序遍历树A中的每个节点nA
     * 2. 对于每个节点nA，检查以nA为根节点的子树是否包含树B
     * 3. 检查是否包含的逻辑：递归比较节点值是否相等，左子树和右子树是否也满足条件
     * 
     * 时间复杂度：O(m*n) - m和n分别是两棵树的节点数
     * 空间复杂度：O(h) - 递归栈的深度，h为树A的高度
     * 是否为最优解：是，需要遍历树A的每个节点并进行匹配
     */
    public static boolean isSubStructure(TreeNode A, TreeNode B) {
        // 空树不是任意一个树的子结构
        if (A == null || B == null) {
            return false;
        }
        
        // 非递归DFS实现，遍历树A的每个节点
        Stack<TreeNode> stack = new Stack<>();
        stack.push(A);
        
        while (!stack.isEmpty()) {
            TreeNode node = stack.pop();
            
            // 检查以当前节点为根的子树是否包含树B
            if (isMatch(node, B)) {
                return true;
            }
            
            // 继续遍历其他节点
            if (node.right != null) {
                stack.push(node.right);
            }
            if (node.left != null) {
                stack.push(node.left);
            }
        }
        
        return false;
    }
    
    // 辅助方法：检查以A为根的子树是否包含以B为根的子树
    private static boolean isMatch(TreeNode A, TreeNode B) {
        // 递归实现更清晰
        if (B == null) {
            return true; // B已经匹配完
        }
        if (A == null || A.val != B.val) {
            return false; // A为空或值不匹配
        }
        // 继续匹配左右子树
        return isMatch(A.left, B.left) && isMatch(A.right, B.right);
    }
    
    /**
     * 题目：LeetCode 1372 - 二叉树中的最长交错路径
     * 题目来源：https://leetcode.cn/problems/longest-zigzag-path-in-a-binary-tree/
     * 题目描述：给你一棵以 root 为根的二叉树，返回其最长的交错路径的长度。
     * 交错路径的定义如下：从一个节点开始，沿着父-子连接，向上或向下移动，
     * 移动时，节点的方向必须交替变化（即从左到右，或从右到左）。
     * 
     * 解题思路：
     * 1. 使用深度优先搜索遍历二叉树
     * 2. 对每个节点，记录从上一个节点来的方向（左或右）
     * 3. 如果当前方向与上一个方向交替，则路径长度+1，否则重置为1
     * 4. 更新全局最大路径长度
     * 
     * 时间复杂度：O(n) - 每个节点只访问一次
     * 空间复杂度：O(h) - 递归栈的深度
     * 是否为最优解：是，一次遍历即可找到最长交错路径
     */
    public static int longestZigZag(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 使用数组作为引用传递最大值
        int[] maxLength = {0};
        
        // 非递归DFS实现，栈中存储三元组：(节点, 方向, 当前长度)
        // 方向：-1表示从父节点的左子树来，1表示从父节点的右子树来，0表示根节点
        Stack<Object[]> stack = new Stack<>();
        stack.push(new Object[]{root, 0, 0});
        
        while (!stack.isEmpty()) {
            Object[] current = stack.pop();
            TreeNode node = (TreeNode) current[0];
            int direction = (int) current[1];
            int length = (int) current[2];
            
            // 更新最大值
            maxLength[0] = Math.max(maxLength[0], length);
            
            // 处理左子树
            if (node.left != null) {
                int newLength = (direction == 1) ? length + 1 : 1;
                stack.push(new Object[]{node.left, -1, newLength});
            }
            
            // 处理右子树
            if (node.right != null) {
                int newLength = (direction == -1) ? length + 1 : 1;
                stack.push(new Object[]{node.right, 1, newLength});
            }
        }
        
        return maxLength[0];
    }
    
    /**
     * 题目：LeetCode 222 - 完全二叉树的节点个数
     * 题目来源：https://leetcode.cn/problems/count-complete-tree-nodes/
     * 题目描述：给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
     * 完全二叉树 的定义是：除了最底层节点可能没填满外，其余每层节点数都达到最大值，
     * 并且最下面一层的节点都集中在该层最左边的若干位置。
     * 
     * 解题思路：
     * 1. 利用完全二叉树的特性：如果左子树的高度等于右子树的高度，则左子树是满二叉树
     * 2. 如果左子树的高度大于右子树的高度，则右子树是满二叉树
     * 3. 满二叉树的节点数为2^h - 1，其中h是树的高度
     * 4. 递归计算剩余部分的节点数
     * 
     * 时间复杂度：O(log²n) - 每次计算高度需要O(logn)，递归深度为O(logn)
     * 空间复杂度：O(logn) - 递归栈的深度
     * 是否为最优解：是，利用完全二叉树特性进行优化
     */
    public static int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        
        // 计算树的高度（从根到最左边叶子节点的距离）
        int leftHeight = getHeight(root.left);
        int rightHeight = getHeight(root.right);
        
        if (leftHeight == rightHeight) {
            // 左子树是满二叉树，节点数为2^leftHeight - 1，加上根节点和右子树
            return (1 << leftHeight) + countNodes(root.right);
        } else {
            // 右子树是满二叉树，节点数为2^rightHeight - 1，加上根节点和左子树
            return (1 << rightHeight) + countNodes(root.left);
        }
    }
    
    // 辅助方法：计算完全二叉树的高度（从根到最左边叶子节点的距离）
    private static int getHeight(TreeNode node) {
        int height = 0;
        while (node != null) {
            height++;
            node = node.left;
        }
        return height;
    }
	}

	// 先序打印所有节点，非递归版
	public static void preOrder(TreeNode head) {
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			stack.push(head);
			while (!stack.isEmpty()) {
				head = stack.pop();
				System.out.print(head.val + " ");
				if (head.right != null) {
					stack.push(head.right);
				}
				if (head.left != null) {
					stack.push(head.left);
				}
			}
			System.out.println();
		}
	}

	// 中序打印所有节点，非递归版
	public static void inOrder(TreeNode head) {
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			while (!stack.isEmpty() || head != null) {
				if (head != null) {
					stack.push(head);
					head = head.left;
				} else {
					head = stack.pop();
					System.out.print(head.val + " ");
					head = head.right;
				}
			}
			System.out.println();
		}
	}

	// 后序打印所有节点，非递归版
	// 这是用两个栈的方法
	public static void posOrderTwoStacks(TreeNode head) {
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			Stack<TreeNode> collect = new Stack<>();
			stack.push(head);
			while (!stack.isEmpty()) {
				head = stack.pop();
				collect.push(head);
				if (head.left != null) {
					stack.push(head.left);
				}
				if (head.right != null) {
					stack.push(head.right);
				}
			}
			while (!collect.isEmpty()) {
				System.out.print(collect.pop().val + " ");
			}
			System.out.println();
		}
	}

	// 后序打印所有节点，非递归版
	// 这是用一个栈的方法
	public static void posOrderOneStack(TreeNode h) {
		if (h != null) {
			Stack<TreeNode> stack = new Stack<>();
			stack.push(h);
			// 如果始终没有打印过节点，h就一直是头节点
			// 一旦打印过节点，h就变成打印节点
			// 之后h的含义 : 上一次打印的节点
			while (!stack.isEmpty()) {
				TreeNode cur = stack.peek();
				if (cur.left != null && h != cur.left && h != cur.right) {
					// 有左树且左树没处理过
					stack.push(cur.left);
				} else if (cur.right != null && h != cur.right) {
					// 有右树且右树没处理过
					stack.push(cur.right);
				} else {
					// 左树、右树 没有 或者 都处理过了
					System.out.print(cur.val + " ");
					h = stack.pop();
				}
			}
			System.out.println();
		}
	}

	// 层序遍历（二叉树的广度优先遍历）
	// 测试链接: https://leetcode.cn/problems/binary-tree-level-order-traversal/
	public static void levelOrder(TreeNode head) {
		if (head == null) {
			return;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(head);
		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			System.out.print(cur.val + " ");
			if (cur.left != null) {
				queue.offer(cur.left);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		// 简化测试，减少内存使用
		TreeNode head = new TreeNode(1);
		head.left = new TreeNode(2);
		head.right = new TreeNode(3);
		head.left.left = new TreeNode(4);
		head.left.right = new TreeNode(5);
		
		System.out.println("=== 简化版二叉树遍历测试 ===");
		
		// 只测试核心功能
		List<Integer> preorder = preorderTraversal(head);
		System.out.println("前序遍历: " + preorder);
		
		List<Integer> inorder = inorderTraversal(head);
		System.out.println("中序遍历: " + inorder);
		
		List<Integer> postorder = postorderTraversalOneStack(head);
		System.out.println("后序遍历: " + postorder);
		
		List<List<Integer>> levelorder = levelOrderTraversal(head);
		System.out.println("层序遍历: " + levelorder);
		
		System.out.println("=== 测试完成 ===");
	}

	// 用一个栈完成先序遍历
	// 测试链接 : https://leetcode.cn/problems/binary-tree-preorder-traversal/
	public static List<Integer> preorderTraversal(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			stack.push(head);
			while (!stack.isEmpty()) {
				head = stack.pop();
				ans.add(head.val);
				if (head.right != null) {
					stack.push(head.right);
				}
				if (head.left != null) {
					stack.push(head.left);
				}
			}
		}
		return ans;
	}

	// 用一个栈完成中序遍历
	// 测试链接 : https://leetcode.cn/problems/binary-tree-inorder-traversal/
	public static List<Integer> inorderTraversal(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			while (!stack.isEmpty() || head != null) {
				if (head != null) {
					stack.push(head);
					head = head.left;
				} else {
					head = stack.pop();
					ans.add(head.val);
					head = head.right;
				}
			}
		}
		return ans;
	}

	// 用两个栈完成后序遍历
	// 提交时函数名改为postorderTraversal
	// 测试链接 : https://leetcode.cn/problems/binary-tree-postorder-traversal/
	public static List<Integer> postorderTraversalTwoStacks(TreeNode head) {
		List<Integer> ans = new ArrayList<>();
		if (head != null) {
			Stack<TreeNode> stack = new Stack<>();
			Stack<TreeNode> collect = new Stack<>();
			stack.push(head);
			while (!stack.isEmpty()) {
				head = stack.pop();
				collect.push(head);
				if (head.left != null) {
					stack.push(head.left);
				}
				if (head.right != null) {
					stack.push(head.right);
				}
			}
			while (!collect.isEmpty()) {
				ans.add(collect.pop().val);
			}
		}
		return ans;
	}

	// 用一个栈完成后序遍历
	// 提交时函数名改为postorderTraversal
	// 测试链接 : https://leetcode.cn/problems/binary-tree-postorder-traversal/
	public static List<Integer> postorderTraversalOneStack(TreeNode h) {
		List<Integer> ans = new ArrayList<>();
		if (h != null) {
			Stack<TreeNode> stack = new Stack<>();
			stack.push(h);
			while (!stack.isEmpty()) {
				TreeNode cur = stack.peek();
				if (cur.left != null && h != cur.left && h != cur.right) {
					stack.push(cur.left);
				} else if (cur.right != null && h != cur.right) {
					stack.push(cur.right);
				} else {
					ans.add(cur.val);
					h = stack.pop();
				}
			}
		}
		return ans;
	}
	
	// 层序遍历（广度优先遍历）
	// 测试链接: https://leetcode.cn/problems/binary-tree-level-order-traversal/
	public static List<List<Integer>> levelOrderTraversal(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> level = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				level.add(cur.val);
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			ans.add(level);
		}
		return ans;
	}
	
	// 锯齿形层序遍历
	// 测试链接: https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/
	public static List<List<Integer>> zigzagLevelOrder(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		boolean isLeftToRight = true;
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> level = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				if (isLeftToRight) {
					level.add(cur.val);
				} else {
					level.add(0, cur.val); // 在列表开头插入元素，实现反向
				}
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			ans.add(level);
			isLeftToRight = !isLeftToRight; // 切换方向
		}
		return ans;
	}
	
	// 二叉树的右视图
	// 测试链接: https://leetcode.cn/problems/binary-tree-right-side-view/
	public static List<Integer> rightSideView(TreeNode root) {
		List<Integer> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				if (i == size - 1) { // 每一层的最后一个节点就是从右边看到的节点
					ans.add(cur.val);
				}
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
		}
		return ans;
	}
	
	// 二叉树的最大深度
	// 测试链接: https://leetcode.cn/problems/maximum-depth-of-binary-tree/
	public static int maxDepth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int depth = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			// 处理当前层的所有节点
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			depth++; // 每处理完一层，深度加1
		}
		return depth;
	}
	
	// 二叉树的最小深度
	// 测试链接: https://leetcode.cn/problems/minimum-depth-of-binary-tree/
	public static int minDepth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int depth = 1;
		while (!queue.isEmpty()) {
			int size = queue.size();
			// 处理当前层的所有节点
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				// 如果当前节点是叶子节点，直接返回深度
				if (cur.left == null && cur.right == null) {
					return depth;
				}
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			depth++; // 处理完一层，深度加1
		}
		return depth;
	}
	
	// 翻转二叉树
	// 测试链接: https://leetcode.cn/problems/invert-binary-tree/
	public static TreeNode invertTree(TreeNode root) {
		if (root == null) {
			return null;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			// 交换左右子树
			TreeNode temp = cur.left;
			cur.left = cur.right;
			cur.right = temp;
			// 将非空子节点加入队列
			if (cur.left != null) {
				queue.offer(cur.left);
			}
			if (cur.right != null) {
				queue.offer(cur.right);
			}
		}
		return root;
	}
	
	// 对称二叉树
	// 测试链接: https://leetcode.cn/problems/symmetric-tree/
	public static boolean isSymmetric(TreeNode root) {
		if (root == null) {
			return true;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root.left);
		queue.offer(root.right);
		while (!queue.isEmpty()) {
			TreeNode left = queue.poll();
			TreeNode right = queue.poll();
			// 如果两个节点都为空，继续比较
			if (left == null && right == null) {
				continue;
			}
			// 如果其中一个为空或者值不相等，返回false
			if (left == null || right == null || left.val != right.val) {
				return false;
			}
			// 按照对称的顺序加入队列
			queue.offer(left.left);
			queue.offer(right.right);
			queue.offer(left.right);
			queue.offer(right.left);
		}
		return true;
	}
	
	// 二叉树的序列化与反序列化
	// 测试链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
	public static String serialize(TreeNode root) {
		if (root == null) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder("[");
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			if (cur != null) {
				sb.append(cur.val).append(",");
				queue.offer(cur.left);
				queue.offer(cur.right);
			} else {
				sb.append("null,");
			}
		}
		// 删除最后多余的逗号
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		return sb.toString();
	}
	
	// 二叉树的反序列化
	public static TreeNode deserialize(String data) {
		if (data.equals("[]")) {
			return null;
		}
		// 去掉方括号并按逗号分割
		String[] vals = data.substring(1, data.length() - 1).split(",");
		TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int i = 1;
		while (!queue.isEmpty()) {
			TreeNode cur = queue.poll();
			// 处理左子节点
			if (!vals[i].equals("null")) {
				cur.left = new TreeNode(Integer.parseInt(vals[i]));
				queue.offer(cur.left);
			}
			i++;
			// 处理右子节点
			if (!vals[i].equals("null")) {
				cur.right = new TreeNode(Integer.parseInt(vals[i]));
				queue.offer(cur.right);
			}
			i++;
		}
		return root;
	}
	
	// 剑指 Offer 07. 重建二叉树
	// 根据前序遍历和中序遍历构建二叉树
	// 测试链接: https://leetcode.cn/problems/zhong-jian-er-cha-shu-lcof/
	public static TreeNode buildTree(int[] preorder, int[] inorder) {
		if (preorder == null || preorder.length == 0) {
			return null;
		}
		// 使用栈来模拟递归过程
		TreeNode root = new TreeNode(preorder[0]);
		Stack<TreeNode> stack = new Stack<>();
		stack.push(root);
		int inorderIndex = 0;
		for (int i = 1; i < preorder.length; i++) {
			int preorderVal = preorder[i];
			TreeNode node = stack.peek();
			if (node.val != inorder[inorderIndex]) {
				// 当前节点是栈顶节点的左子节点
				node.left = new TreeNode(preorderVal);
				stack.push(node.left);
			} else {
				// 当前节点是栈中某个节点的右子节点
				while (!stack.isEmpty() && stack.peek().val == inorder[inorderIndex]) {
					node = stack.pop();
					inorderIndex++;
				}
				node.right = new TreeNode(preorderVal);
				stack.push(node.right);
			}
		}
		return root;
	}
	
	// 剑指 Offer 32 - III. 从上到下打印二叉树 III
	// 之字形层序遍历二叉树
	// 测试链接: https://leetcode.cn/problems/cong-shang-dao-xia-da-yin-er-cha-shu-iii-lcof/
	public static List<List<Integer>> levelOrderZigzag(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		boolean leftToRight = true;
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> level = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (leftToRight) {
					level.add(node.val);
				} else {
					level.add(0, node.val);
				}
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
			ans.add(level);
			leftToRight = !leftToRight;
		}
		return ans;
	}
	
	// 剑指 Offer 54. 二叉搜索树的第k大节点
	// 测试链接: https://leetcode.cn/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/
	public static int kthLargest(TreeNode root, int k) {
		// 使用栈实现反向中序遍历（右->根->左）
		Stack<TreeNode> stack = new Stack<>();
		TreeNode cur = root;
		int count = 0;
		while (cur != null || !stack.isEmpty()) {
			// 先到最右边的节点
			while (cur != null) {
				stack.push(cur);
				cur = cur.right;
			}
			// 处理当前节点
			cur = stack.pop();
			count++;
			if (count == k) {
				return cur.val;
			}
			// 转向左子树
			cur = cur.left;
		}
		return 0;
	}
	
	// 剑指 Offer 55 - I. 二叉树的深度
	// 测试链接: https://leetcode.cn/problems/er-cha-shu-de-shen-du-lcof/
	public static int maxDepth2(TreeNode root) {
		if (root == null) {
			return 0;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int depth = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode node = queue.poll();
				if (node.left != null) {
					queue.offer(node.left);
				}
				if (node.right != null) {
					queue.offer(node.right);
				}
			}
			depth++;
		}
		return depth;
	}
	
	// 剑指 Offer 55 - II. 平衡二叉树
	// 测试链接: https://leetcode.cn/problems/ping-heng-er-cha-shu-lcof/
	public static boolean isBalanced(TreeNode root) {
		return height(root) >= 0;
	}
	
	private static int height(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int leftHeight = height(root.left);
		int rightHeight = height(root.right);
		// 如果左右子树高度差超过1，或者左右子树本身不平衡，返回-1
		if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) {
			return -1;
		} else {
			return Math.max(leftHeight, rightHeight) + 1;
		}
	}
	
	// 剑指 Offer 68 - II. 二叉树的最近公共祖先
	// 测试链接: https://leetcode.cn/problems/er-cha-shu-de-zui-jin-gong-gong-zu-xian-lcof/
	public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		// 使用栈存储从根节点到目标节点的路径
		Stack<TreeNode> pathP = new Stack<>();
		Stack<TreeNode> pathQ = new Stack<>();
		// 获取从根节点到p和q的路径
		getPath(root, p, pathP);
		getPath(root, q, pathQ);
		// 找到两条路径的最后一个公共节点
		TreeNode ancestor = null;
		while (!pathP.isEmpty() && !pathQ.isEmpty() && pathP.peek() == pathQ.peek()) {
			ancestor = pathP.peek();
			pathP.pop();
			pathQ.pop();
		}
		return ancestor;
	}
	
	// 获取从根节点到目标节点的路径
	private static boolean getPath(TreeNode root, TreeNode target, Stack<TreeNode> path) {
		if (root == null) {
			return false;
		}
		path.push(root);
		if (root == target) {
			return true;
		}
		// 在左右子树中查找
		if (getPath(root.left, target, path) || getPath(root.right, target, path)) {
			return true;
		}
		// 如果左右子树都没有找到，回溯
		path.pop();
		return false;
	}
	
	// ==================== 以下是新增的补充题目 ====================
	
	/*
	 * 题目1: LeetCode 107 - 二叉树的层序遍历 II
	 * 题目来源: https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/
	 * 题目描述:
	 * 给定一个二叉树，返回其节点值自底向上的层序遍历。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
	 * 
	 * 解题思路:
	 * 1. 使用队列进行正常的层序遍历
	 * 2. 将每一层的结果添加到列表中
	 * 3. 最后将列表反转即可得到自底向上的结果
	 * 
	 * 时间复杂度: O(n)
	 * - 需要遍历所有n个节点一次
	 * 
	 * 空间复杂度: O(n)
	 * - 队列最多存储树的最大宽度（最坏情况下为n/2）
	 * - 结果列表存储所有n个节点
	 * 
	 * 是否为最优解: 是
	 * - 必须访问所有节点，时间复杂度O(n)无法优化
	 * - 必须存储所有节点值，空间复杂度O(n)无法优化
	 */
	public static List<List<Integer>> levelOrderBottom(TreeNode root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> level = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				level.add(cur.val);
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			ans.add(0, level); // 在列表开头插入，实现自底向上
		}
		return ans;
	}
	
	/*
	 * 题目2: LeetCode 199 - 二叉树的右视图
	 * 题目来源: https://leetcode.cn/problems/binary-tree-right-side-view/
	 * 题目描述:
	 * 给定一个二叉树的根节点 root，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历
	 * 2. 每一层只取最后一个节点（最右边的节点）
	 * 
	 * 时间复杂度: O(n)
	 * - 需要遍历所有n个节点
	 * 
	 * 空间复杂度: O(w)
	 * - w为树的最大宽度，队列最多存储一层的节点
	 * - 最坏情况下为O(n)（满二叉树的最后一层）
	 * 
	 * 是否为最优解: 是
	 * - 层序遍历是最直观且高效的方法
	 * - 也可以用DFS实现，但需要记录深度，代码更复杂
	 */
	// 该方法已在前面实现
	
	/*
	 * 题目3: LeetCode 637 - 二叉树的层平均值
	 * 题目来源: https://leetcode.cn/problems/average-of-levels-in-binary-tree/
	 * 题目描述:
	 * 给定一个非空二叉树的根节点 root , 以数组的形式返回每一层节点的平均值。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历
	 * 2. 对每一层的节点值求和，然后除以节点数
	 * 
	 * 时间复杂度: O(n)
	 * - 需要遍历所有n个节点
	 * 
	 * 空间复杂度: O(w)
	 * - w为树的最大宽度
	 * 
	 * 是否为最优解: 是
	 * - 必须访问所有节点才能计算平均值
	 */
	public static List<Double> averageOfLevels(TreeNode root) {
		List<Double> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			long sum = 0; // 使用long避免整数溢出
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				sum += cur.val;
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			ans.add((double) sum / size);
		}
		return ans;
	}
	
	/*
	 * 题目4: LeetCode 429 - N 叉树的层序遍历
	 * 题目来源: https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
	 * 题目描述:
	 * 给定一个 N 叉树，返回其节点值的层序遍历。
	 * 
	 * 解题思路:
	 * 1. 使用队列进行层序遍历
	 * 2. 与二叉树类似，但需要遍历所有子节点
	 * 
	 * 时间复杂度: O(n)
	 * - 需要遍历所有n个节点
	 * 
	 * 空间复杂度: O(w)
	 * - w为树的最大宽度
	 * 
	 * 是否为最优解: 是
	 */
	static class Node {
		public int val;
		public List<Node> children;
		
		public Node() {}
		
		public Node(int _val) {
			val = _val;
		}
		
		public Node(int _val, List<Node> _children) {
			val = _val;
			children = _children;
		}
	}
	
	public static List<List<Integer>> nAryLevelOrder(Node root) {
		List<List<Integer>> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			List<Integer> level = new ArrayList<>();
			for (int i = 0; i < size; i++) {
				Node cur = queue.poll();
				level.add(cur.val);
				// 将所有子节点加入队列
				if (cur.children != null) {
					for (Node child : cur.children) {
						if (child != null) {
							queue.offer(child);
						}
					}
				}
			}
			ans.add(level);
		}
		return ans;
	}
	
	/*
	 * 题目5: LeetCode 515 - 在每个树行中找最大值
	 * 题目来源: https://leetcode.cn/problems/find-largest-value-in-each-tree-row/
	 * 题目描述:
	 * 给定一棵二叉树的根节点 root ，请找出存在于每一层的最大值。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历
	 * 2. 对每一层记录最大值
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w), w为最大宽度
	 * 是否为最优解: 是
	 */
	public static List<Integer> largestValues(TreeNode root) {
		List<Integer> ans = new ArrayList<>();
		if (root == null) {
			return ans;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			int maxVal = Integer.MIN_VALUE; // 初始化为最小值
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				maxVal = Math.max(maxVal, cur.val);
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
			ans.add(maxVal);
		}
		return ans;
	}
	
	/*
	 * 题目6: LeetCode 513 - 找树左下角的值
	 * 题目来源: https://leetcode.cn/problems/find-bottom-left-tree-value/
	 * 题目描述:
	 * 给定一个二叉树的根节点 root，请找出该二叉树的 最底层 最左边 节点的值。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历
	 * 2. 记录每一层的第一个节点
	 * 3. 最后一层的第一个节点就是答案
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w)
	 * 是否为最优解: 是
	 */
	public static int findBottomLeftValue(TreeNode root) {
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		int leftmost = root.val;
		while (!queue.isEmpty()) {
			int size = queue.size();
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				if (i == 0) { // 记录每一层的第一个节点
					leftmost = cur.val;
				}
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
		}
		return leftmost;
	}
	
	/*
	 * 题目7: LeetCode 116 - 填充每个节点的下一个右侧节点指针
	 * 题目来源: https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
	 * 题目描述:
	 * 给定一个 完美二叉树 ，其所有叶子节点都在同一层，每个父节点都有两个子节点。
	 * 填充它的每个 next 指针，让这个指针指向其下一个右侧节点。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历
	 * 2. 对于每一层的节点，将前一个节点的next指向当前节点
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w)
	 * 是否为最优解: 是（对于迭代方法）
	 * 替代方案: 可以使用O(1)空间的方法，利用已经建立的next指针
	 */
	static class NextNode {
		public int val;
		public NextNode left;
		public NextNode right;
		public NextNode next;
		
		public NextNode() {}
		
		public NextNode(int _val) {
			val = _val;
		}
		
		public NextNode(int _val, NextNode _left, NextNode _right, NextNode _next) {
			val = _val;
			left = _left;
			right = _right;
			next = _next;
		}
	}
	
	public static NextNode connect(NextNode root) {
		if (root == null) {
			return null;
		}
		Queue<NextNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			NextNode prev = null;
			for (int i = 0; i < size; i++) {
				NextNode cur = queue.poll();
				if (prev != null) {
					prev.next = cur; // 连接前一个节点
				}
				prev = cur;
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
		}
		return root;
	}
	
	// O(1)空间复杂度的最优解
	public static NextNode connectOptimal(NextNode root) {
		if (root == null) {
			return null;
		}
		NextNode leftmost = root; // 每一层的最左节点
		while (leftmost.left != null) { // 当左子节点不为空时，说明还有下一层
			NextNode cur = leftmost;
			while (cur != null) {
				// 连接左右子节点
				cur.left.next = cur.right;
				// 连接相邻节点的子节点
				if (cur.next != null) {
					cur.right.next = cur.next.left;
				}
				cur = cur.next;
			}
			leftmost = leftmost.left; // 移动到下一层
		}
		return root;
	}
	
	/*
	 * 题目8: LeetCode 117 - 填充每个节点的下一个右侧节点指针 II
	 * 题目来源: https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/
	 * 题目描述:
	 * 给定一个二叉树，填充它的每个 next 指针（不是完美二叉树）
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历，与题目116类似
	 * 2. 不同之处在于树不一定是完美二叉树
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w) - 迭代方法，O(1) - 最优解
	 * 是否为最优解: 迭代方法不是，有O(1)空间的解法
	 */
	public static NextNode connectII(NextNode root) {
		if (root == null) {
			return null;
		}
		Queue<NextNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			NextNode prev = null;
			for (int i = 0; i < size; i++) {
				NextNode cur = queue.poll();
				if (prev != null) {
					prev.next = cur;
				}
				prev = cur;
				if (cur.left != null) {
					queue.offer(cur.left);
				}
				if (cur.right != null) {
					queue.offer(cur.right);
				}
			}
		}
		return root;
	}
	
	// O(1)空间复杂度的最优解
	public static NextNode connectIIOptimal(NextNode root) {
		if (root == null) {
			return null;
		}
		NextNode cur = root; // 当前层的指针
		while (cur != null) {
			NextNode dummy = new NextNode(0); // 哑节点，作为下一层的头节点
			NextNode tail = dummy; // 下一层的尾节点
			// 遍历当前层的所有节点
			while (cur != null) {
				if (cur.left != null) {
					tail.next = cur.left;
					tail = tail.next;
				}
				if (cur.right != null) {
					tail.next = cur.right;
					tail = tail.next;
				}
				cur = cur.next; // 移动到当前层的下一个节点
			}
			cur = dummy.next; // 移动到下一层
		}
		return root;
	}
	
	/*
	 * 题目9: LeetCode 662 - 二叉树最大宽度
	 * 题目来源: https://leetcode.cn/problems/maximum-width-of-binary-tree/
	 * 题目描述:
	 * 给定一个二叉树，编写一个函数来获取这个树的最大宽度。
	 * 树的宽度是所有层中节点的最大数量。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历，为每个节点编号
	 * 2. 每一层的宽度 = 最右边节点编号 - 最左边节点编号 + 1
	 * 3. 左子节点编号 = 父节点编号 * 2
	 * 4. 右子节点编号 = 父节点编号 * 2 + 1
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w)
	 * 是否为最优解: 是
	 */
	static class AnnotatedNode {
		TreeNode node;
		long id; // 使用long避免溢出
		
		AnnotatedNode(TreeNode n, long i) {
			node = n;
			id = i;
		}
	}
	
	public static int widthOfBinaryTree(TreeNode root) {
		if (root == null) {
			return 0;
		}
		Queue<AnnotatedNode> queue = new LinkedList<>();
		queue.offer(new AnnotatedNode(root, 0));
		long maxWidth = 0;
		while (!queue.isEmpty()) {
			int size = queue.size();
			long left = queue.peek().id; // 当前层最左边节点的编号
			long right = left; // 当前层最右边节点的编号
			for (int i = 0; i < size; i++) {
				AnnotatedNode cur = queue.poll();
				right = cur.id; // 更新最右边节点的编号
				if (cur.node.left != null) {
					queue.offer(new AnnotatedNode(cur.node.left, cur.id * 2));
				}
				if (cur.node.right != null) {
					queue.offer(new AnnotatedNode(cur.node.right, cur.id * 2 + 1));
				}
			}
			maxWidth = Math.max(maxWidth, right - left + 1);
		}
		return (int) maxWidth;
	}
	
	/*
	 * 题目10: LeetCode 993 - 二叉树的堂兄弟节点
	 * 题目来源: https://leetcode.cn/problems/cousins-in-binary-tree/
	 * 题目描述:
	 * 在二叉树中，根节点位于深度 0 处，每个深度为 k 的节点的子节点位于深度 k+1 处。
	 * 如果二叉树的两个节点深度相同，但 父节点不同 ，则它们是一对堂兄弟节点。
	 * 
	 * 解题思路:
	 * 1. 使用层序遍历，记录每个节点的父节点和深度
	 * 2. 判断两个节点是否深度相同且父节点不同
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(w)
	 * 是否为最优解: 是
	 */
	public static boolean isCousins(TreeNode root, int x, int y) {
		if (root == null) {
			return false;
		}
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		while (!queue.isEmpty()) {
			int size = queue.size();
			TreeNode parentX = null, parentY = null;
			boolean foundX = false, foundY = false;
			for (int i = 0; i < size; i++) {
				TreeNode cur = queue.poll();
				// 检查左子节点
				if (cur.left != null) {
					queue.offer(cur.left);
					if (cur.left.val == x) {
						foundX = true;
						parentX = cur;
					} else if (cur.left.val == y) {
						foundY = true;
						parentY = cur;
					}
				}
				// 检查右子节点
				if (cur.right != null) {
					queue.offer(cur.right);
					if (cur.right.val == x) {
						foundX = true;
						parentX = cur;
					} else if (cur.right.val == y) {
						foundY = true;
						parentY = cur;
					}
				}
			}
			// 如果在同一层找到了两个节点，判断父节点是否不同
			if (foundX && foundY) {
				return parentX != parentY;
			}
			// 如果只找到了一个，说明不在同一层
			if (foundX || foundY) {
				return false;
			}
		}
		return false;
	}
}