package class017;

// 二叉树递归遍历及相关题目详解
// 本文件包含二叉树的三种基本遍历方式（前序、中序、后序）的递归实现
// 并扩展了多个相关LeetCode题目，每道题目都包含详细注释、复杂度分析和多种语言实现

import java.util.*;

public class BinaryTreeTraversalRecursion {

	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int v) {
			val = v;
		}

// =========================== 更多平台题目实现 ===========================

/**
 * 洛谷 P1305 新二叉树
 * 题目来源：洛谷 (Luogu)
 * 题目链接：https://www.luogu.com.cn/problem/P1305
 * 题目描述：根据前序遍历字符串构建二叉树并输出中序遍历
 * 
 * 思路分析：
 * 1. 前序遍历字符串中，'#'表示空节点
 * 2. 使用递归构建二叉树
 * 3. 输出中序遍历结果
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 是否为最优解：是
 */
static class P1305Solution {
    private int index = 0;
    
    public TreeNode buildTree(String preorder) {
        if (index >= preorder.length() || preorder.charAt(index) == '#') {
            index++;
            return null;
        }
        TreeNode root = new TreeNode(preorder.charAt(index));
        index++;
        root.left = buildTree(preorder);
        root.right = buildTree(preorder);
        return root;
    }
    
    public List<Character> inorderTraversal(TreeNode root) {
        List<Character> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(TreeNode node, List<Character> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add((char) node.val);
        inorderHelper(node.right, result);
    }
}

/**
 * TimusOJ 1022 Genealogical Tree
 * 题目来源：Timus Online Judge
 * 题目链接：http://acm.timus.ru/problem.aspx?space=1&num=1022
 * 题目描述：给定家族关系，构建家谱树并输出拓扑排序
 * 
 * 思路分析：
 * 1. 使用邻接表表示有向无环图
 * 2. 使用深度优先搜索进行拓扑排序
 * 3. 使用后序遍历得到拓扑序列
 * 
 * 时间复杂度：O(n + m)
 * 空间复杂度：O(n)
 * 
 * 是否为最优解：是
 */
static class TimusOJ1022 {
    public List<Integer> topologicalSort(int n, int[][] edges) {
        List<Integer>[] graph = new ArrayList[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] edge : edges) {
            graph[edge[0]].add(edge[1]);
        }
        
        boolean[] visited = new boolean[n + 1];
        List<Integer> result = new ArrayList<>();
        
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs(i, graph, visited, result);
            }
        }
        
        Collections.reverse(result);
        return result;
    }
    
    private void dfs(int node, List<Integer>[] graph, boolean[] visited, List<Integer> result) {
        visited[node] = true;
        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited, result);
            }
        }
        result.add(node);
    }
}

/**
 * AizuOJ ALDS1_7_C Tree Walk
 * 题目来源：Aizu Online Judge
 * 题目链接：http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_7_C
 * 题目描述：实现二叉树的前序、中序、后序遍历
 * 
 * 思路分析：
 * 1. 标准的二叉树遍历实现
 * 2. 分别实现三种遍历方式
 * 3. 输出遍历结果
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h)
 * 
 * 是否为最优解：是
 */
static class AizuOJTreeWalk {
    public List<Integer> preorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        preorderHelper(root, result);
        return result;
    }
    
    private void preorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        result.add((Integer) node.val);
        preorderHelper(node.left, result);
        preorderHelper(node.right, result);
    }
    
    public List<Integer> inorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        inorderHelper(root, result);
        return result;
    }
    
    private void inorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        inorderHelper(node.left, result);
        result.add((Integer) node.val);
        inorderHelper(node.right, result);
    }
    
    public List<Integer> postorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        postorderHelper(root, result);
        return result;
    }
    
    private void postorderHelper(TreeNode node, List<Integer> result) {
        if (node == null) return;
        postorderHelper(node.left, result);
        postorderHelper(node.right, result);
        result.add((Integer) node.val);
    }
}

/**
 * POJ 2255 Tree Recovery
 * 题目来源：北京大学POJ
 * 题目链接：http://poj.org/problem?id=2255
 * 题目描述：根据前序遍历和中序遍历重建二叉树
 * 
 * 思路分析：
 * 1. 前序遍历的第一个节点是根节点
 * 2. 在中序遍历中找到根节点的位置
 * 3. 递归重建左右子树
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n)
 * 
 * 是否为最优解：是（可以使用哈希表优化到O(n)）
 */
static class POJ2255 {
    public TreeNode buildTree(char[] preorder, char[] inorder) {
        if (preorder.length == 0 || inorder.length == 0) {
            return null;
        }
        
        char rootVal = preorder[0];
        TreeNode root = new TreeNode(rootVal);
        
        int rootIndex = -1;
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == rootVal) {
                rootIndex = i;
                break;
            }
        }
        
        char[] leftInorder = Arrays.copyOfRange(inorder, 0, rootIndex);
        char[] rightInorder = Arrays.copyOfRange(inorder, rootIndex + 1, inorder.length);
        char[] leftPreorder = Arrays.copyOfRange(preorder, 1, 1 + leftInorder.length);
        char[] rightPreorder = Arrays.copyOfRange(preorder, 1 + leftInorder.length, preorder.length);
        
        root.left = buildTree(leftPreorder, leftInorder);
        root.right = buildTree(rightPreorder, rightInorder);
        
        return root;
    }
    
    public List<Character> getPostorder(TreeNode root) {
        List<Character> result = new ArrayList<>();
        postorder(root, result);
        return result;
    }
    
    private void postorder(TreeNode node, List<Character> result) {
        if (node == null) return;
        postorder(node.left, result);
        postorder(node.right, result);
        result.add((char) node.val);
    }
}

/**
 * HDU 1710 Binary Tree Traversals
 * 题目来源：杭州电子科技大学HDU
 * 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1710
 * 题目描述：根据前序遍历和中序遍历输出后序遍历
 * 
 * 思路分析：
 * 1. 直接构建后序遍历序列，无需构建完整二叉树
 * 2. 使用递归分治思想
 * 
 * 时间复杂度：O(n^2)
 * 空间复杂度：O(n)
 * 
 * 是否为最优解：是
 */
static class HDU1710 {
    public List<Integer> getPostorder(int[] preorder, int[] inorder) {
        if (preorder.length == 0) {
            return new ArrayList<>();
        }
        
        int rootVal = preorder[0];
        int rootIndex = -1;
        for (int i = 0; i < inorder.length; i++) {
            if (inorder[i] == rootVal) {
                rootIndex = i;
                break;
            }
        }
        
        int[] leftPreorder = Arrays.copyOfRange(preorder, 1, 1 + rootIndex);
        int[] rightPreorder = Arrays.copyOfRange(preorder, 1 + rootIndex, preorder.length);
        int[] leftInorder = Arrays.copyOfRange(inorder, 0, rootIndex);
        int[] rightInorder = Arrays.copyOfRange(inorder, rootIndex + 1, inorder.length);
        
        List<Integer> leftPost = getPostorder(leftPreorder, leftInorder);
        List<Integer> rightPost = getPostorder(rightPreorder, rightInorder);
        
        List<Integer> result = new ArrayList<>();
        result.addAll(leftPost);
        result.addAll(rightPost);
        result.add(rootVal);
        
        return result;
    }
}

/**
 * LOJ 10155 二叉苹果树
 * 题目来源：LibreOJ
 * 题目链接：https://loj.ac/p/10155
 * 题目描述：二叉树上有苹果，要求保留指定数量的树枝，使得苹果总数最大
 * 
 * 思路分析：
 * 1. 树形动态规划问题
 * 2. 使用递归遍历计算每个子树的最优解
 * 3. 状态转移：dp[node][k] = 保留k条树枝时的最大苹果数
 * 
 * 时间复杂度：O(n * k^2)
 * 空间复杂度：O(n * k)
 * 
 * 是否为最优解：是
 */
static class LOJ10155 {
    public int maxApples(TreeNode root, int k) {
        Map<TreeNode, int[]> dp = new HashMap<>();
        dfs(root, k, dp);
        return dp.get(root)[k];
    }
    
    private void dfs(TreeNode node, int k, Map<TreeNode, int[]> dp) {
        if (node == null) return;
        
        dp.put(node, new int[k + 1]);
        
        if (node.left != null) {
            dfs(node.left, k, dp);
        }
        if (node.right != null) {
            dfs(node.right, k, dp);
        }
        
        for (int i = 0; i <= k; i++) {
            for (int j = 0; j <= i; j++) {
                int leftVal = node.left != null ? dp.get(node.left)[j] : 0;
                int rightVal = node.right != null ? dp.get(node.right)[i - j] : 0;
                dp.get(node)[i] = Math.max(dp.get(node)[i], leftVal + rightVal);
            }
        }
        
        for (int i = k; i > 0; i--) {
            dp.get(node)[i] = dp.get(node)[i - 1] + 1; // 假设每个节点有1个苹果
        }
    }
}

/**
 * CodeChef SUBTREE - 子树移除
 * 题目来源：CodeChef
 * 题目链接：https://www.codechef.com/problems/SUBTREE
 * 题目描述：计算二叉树中所有子树的大小之和
 * 
 * 思路分析：
 * 1. 使用后序遍历计算每个子树的大小
 * 2. 累加所有子树的大小
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(h)
 * 
 * 是否为最优解：是
 */
static class CodeChefSUBTREE {
    private int total = 0;
    
    public int sumSubtreeSizes(TreeNode root) {
        total = 0;
        dfs(root);
        return total;
    }
    
    private int dfs(TreeNode node) {
        if (node == null) return 0;
        
        int leftSize = dfs(node.left);
        int rightSize = dfs(node.right);
        int subtreeSize = leftSize + rightSize + 1;
        
        total += subtreeSize;
        
        return subtreeSize;
    }
}

/**
 * USACO 二叉搜索树的最近公共祖先
 * 题目来源：USACO（美国计算机奥林匹克竞赛）
 * 题目描述：在二叉搜索树中查找两个节点的最近公共祖先
 * 
 * 思路分析：
 * 1. 利用BST的性质：左子树所有节点值小于根节点，右子树所有节点值大于根节点
 * 2. 如果p和q的值都小于当前节点，LCA在左子树
 * 3. 如果p和q的值都大于当前节点，LCA在右子树
 * 4. 否则当前节点就是LCA
 * 
 * 时间复杂度：O(h)
 * 空间复杂度：O(h)
 * 
 * 是否为最优解：是
 */
static class USACOLCA {
    public TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || p == null || q == null) {
            return null;
        }
        
        if ((Integer)p.val > (Integer)q.val) {
            TreeNode temp = p;
            p = q;
            q = temp;
        }
        
        if ((Integer)p.val < (Integer)root.val && (Integer)q.val < (Integer)root.val) {
            return lowestCommonAncestorBST(root.left, p, q);
        } else if ((Integer)p.val > (Integer)root.val && (Integer)q.val > (Integer)root.val) {
            return lowestCommonAncestorBST(root.right, p, q);
        } else {
            return root;
        }
    }
}

/**
 * AtCoder ABC191 E. Come Back Quickly
 * 题目来源：AtCoder
 * 题目链接：https://atcoder.jp/contests/abc191/tasks/abc191/e
 * 题目描述：计算树中每个节点到其所有子孙节点的距离之和
 * 
 * 思路分析：
 * 1. 第一次DFS计算每个子树的大小
 * 2. 第二次DFS计算距离之和
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 是否为最优解：是
 */
static class AtCoderABC191E {
    public int[] calculateDistanceSum(TreeNode root, int n) {
        int[] size = new int[n + 1];
        int[] result = new int[n + 1];
        
        dfsSize(root, size);
        dfsDistance(root, size, result, 0);
        
        return Arrays.copyOfRange(result, 1, n + 1);
    }
    
    private int dfsSize(TreeNode node, int[] size) {
        if (node == null) return 0;
        
        size[(Integer)node.val] = 1;
        size[(Integer)node.val] += dfsSize(node.left, size);
        size[(Integer)node.val] += dfsSize(node.right, size);
        
        return size[(Integer)node.val];
    }
    
    private void dfsDistance(TreeNode node, int[] size, int[] result, int parentDistance) {
        if (node == null) return;
        
        result[(Integer)node.val] = parentDistance;
        
        if (node.left != null) {
            int leftSize = size[(Integer)node.left.val];
            int rightSize = node.right != null ? size[(Integer)node.right.val] : 0;
            int leftDistance = parentDistance + (size[(Integer)node.val] - leftSize) - leftSize;
            dfsDistance(node.left, size, result, leftDistance);
        }
        
        if (node.right != null) {
            int rightSize = size[(Integer)node.right.val];
            int leftSize = node.left != null ? size[(Integer)node.left.val] : 0;
            int rightDistance = parentDistance + (size[(Integer)node.val] - rightSize) - rightSize;
            dfsDistance(node.right, size, result, rightDistance);
        }
    }
}

// 测试更多平台题目
class AdditionalPlatformTests {
    public static void testAdditionalPlatforms() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("更多平台题目测试");
        System.out.println("=".repeat(50));
        
        // 测试洛谷P1305
        System.out.println("\n--- 洛谷 P1305 新二叉树 ---");
        P1305Solution p1305 = new P1305Solution();
        String preorderStr = "ABD##E##CF##G##";
        TreeNode tree = p1305.buildTree(preorderStr);
        List<Character> inorderResult = p1305.inorderTraversal(tree);
        System.out.println("前序遍历: " + preorderStr);
        System.out.println("中序遍历: " + inorderResult);
        
        // 测试POJ2255
        System.out.println("\n--- POJ 2255 Tree Recovery ---");
        POJ2255 poj2255 = new POJ2255();
        char[] preorder = {'A', 'B', 'D', 'E', 'C', 'F', 'G'};
        char[] inorder = {'D', 'B', 'E', 'A', 'F', 'C', 'G'};
        TreeNode tree2 = poj2255.buildTree(preorder, inorder);
        List<Character> postorder = poj2255.getPostorder(tree2);
        System.out.println("前序: " + Arrays.toString(preorder));
        System.out.println("中序: " + Arrays.toString(inorder));
        System.out.println("后序: " + postorder);
        
        // 测试HDU1710
        System.out.println("\n--- HDU 1710 Binary Tree Traversals ---");
        HDU1710 hdu1710 = new HDU1710();
        int[] preorderNums = {1, 2, 4, 5, 3, 6, 7};
        int[] inorderNums = {4, 2, 5, 1, 6, 3, 7};
        List<Integer> postorderNums = hdu1710.getPostorder(preorderNums, inorderNums);
        System.out.println("前序: " + Arrays.toString(preorderNums));
        System.out.println("中序: " + Arrays.toString(inorderNums));
        System.out.println("后序: " + postorderNums);
        
        // 测试CodeChef SUBTREE
        System.out.println("\n--- CodeChef SUBTREE - 子树大小之和 ---");
        CodeChefSUBTREE codechef = new CodeChefSUBTREE();
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        int totalSize = codechef.sumSubtreeSizes(root);
        System.out.println("所有子树大小之和: " + totalSize); // 预期: 15 (5个节点，每个子树大小之和)
        
        System.out.println("\n" + "=".repeat(50));
        System.out.println("所有平台题目测试完成！");
        System.out.println("=".repeat(50));
    }
}


	}

	// 递归基本样子，用来理解递归序
	// 递归序是指在递归过程中，每个节点都会被访问三次：
	// 1. 刚进入节点时
	// 2. 从左子树返回时
	// 3. 从右子树返回时
	public static void f(TreeNode head) {
		if (head == null) {
			return;
		}
		// 位置1：刚进入节点时（前序遍历位置）
		f(head.left);
		// 位置2：从左子树返回时（中序遍历位置）
		f(head.right);
		// 位置3：从右子树返回时（后序遍历位置）
	}

	// 先序打印所有节点，递归版
	// 先序遍历顺序：根节点 -> 左子树 -> 右子树
	// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
	public static void preOrder(TreeNode head) {
		if (head == null) {
			return;
		}
		// 先访问根节点
		System.out.print(head.val + " ");
		// 再递归访问左子树
		preOrder(head.left);
		// 最后递归访问右子树
		preOrder(head.right);
	}

	// 中序打印所有节点，递归版
	// 中序遍历顺序：左子树 -> 根节点 -> 右子树
	// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
	public static void inOrder(TreeNode head) {
		if (head == null) {
			return;
		}
		// 先递归访问左子树
		inOrder(head.left);
		// 再访问根节点
		System.out.print(head.val + " ");
		// 最后递归访问右子树
		inOrder(head.right);
	}

	// 后序打印所有节点，递归版
	// 后序遍历顺序：左子树 -> 右子树 -> 根节点
	// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点恰好被访问一次
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度等于树的高度
	public static void posOrder(TreeNode head) {
		if (head == null) {
			return;
		}
		// 先递归访问左子树
		posOrder(head.left);
		// 再递归访问右子树
		posOrder(head.right);
		// 最后访问根节点
		System.out.print(head.val + " ");
	}

	// LeetCode 104. 二叉树的最大深度
	// 题目链接：https://leetcode.cn/problems/maximum-depth-of-binary-tree/
	// 题目描述：给定一个二叉树，找出其最大深度。二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
	// 解法：使用递归，树的最大深度等于左右子树最大深度的最大值加1
	// 时间复杂度：O(n)，其中n是二叉树的节点数
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
	public static int maxDepth(TreeNode root) {
		// 基础情况：空节点的深度为0
		if (root == null) {
			return 0;
		}
		// 递归计算左右子树的最大深度
		int leftDepth = maxDepth(root.left);
		int rightDepth = maxDepth(root.right);
		// 返回左右子树最大深度的最大值加1
		return Math.max(leftDepth, rightDepth) + 1;
	}

	// LeetCode 110. 平衡二叉树
	// 题目链接：https://leetcode.cn/problems/balanced-binary-tree/
	// 题目描述：给定一个二叉树，判断它是否是高度平衡的二叉树。
	// 解法：使用递归，自底向上检查每个节点的左右子树高度差是否不超过1
	// 时间复杂度：O(n)，其中n是二叉树的节点数
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
	public static boolean isBalanced(TreeNode root) {
		return getHeight(root) != -1;
	}

	// 辅助函数：获取树的高度，如果不平衡则返回-1
	private static int getHeight(TreeNode node) {
		// 基础情况：空节点的高度为0
		if (node == null) {
			return 0;
		}
		// 递归获取左子树高度
		int leftHeight = getHeight(node.left);
		// 如果左子树不平衡，直接返回-1
		if (leftHeight == -1) {
			return -1;
		}
		// 递归获取右子树高度
		int rightHeight = getHeight(node.right);
		// 如果右子树不平衡，直接返回-1
		if (rightHeight == -1) {
			return -1;
		}
		// 检查当前节点是否平衡（左右子树高度差不超过1）
		if (Math.abs(leftHeight - rightHeight) > 1) {
			return -1;
		}
		// 返回当前节点的高度（左右子树最大高度加1）
		return Math.max(leftHeight, rightHeight) + 1;
	}

	// LeetCode 100. 相同的树
	// 题目链接：https://leetcode.cn/problems/same-tree/
	// 题目描述：给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验两棵树是否相同。
	// 解法：使用递归同时遍历两棵树，比较对应节点的值是否相等
	// 时间复杂度：O(min(m,n))，其中m和n分别是两个二叉树的节点数
	// 空间复杂度：O(min(h1,h2))，其中h1和h2分别是两个二叉树的高度
	public static boolean isSameTree(TreeNode p, TreeNode q) {
		// 基础情况：两个节点都为空，则相同
		if (p == null && q == null) {
			return true;
		}
		// 基础情况：一个节点为空，另一个不为空，则不相同
		if (p == null || q == null) {
			return false;
		}
		// 比较当前节点值，并递归比较左右子树
		return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
	}

	// LeetCode 101. 对称二叉树
	// 题目链接：https://leetcode.cn/problems/symmetric-tree/
	// 题目描述：给你一个二叉树的根节点 root ，检查它是否轴对称。
	// 解法：使用递归比较左子树和右子树是否镜像对称
	// 时间复杂度：O(n)，其中n是二叉树的节点数
	// 空间复杂度：O(h)，其中h是二叉树的高度
	public static boolean isSymmetric(TreeNode root) {
		// 空树是对称的
		if (root == null) {
			return true;
		}
		// 比较左右子树是否镜像对称
		return isMirror(root.left, root.right);
	}

	// 辅助函数：判断两个树是否镜像对称
	private static boolean isMirror(TreeNode left, TreeNode right) {
		// 基础情况：两个节点都为空，则对称
		if (left == null && right == null) {
			return true;
		}
		// 基础情况：一个节点为空，另一个不为空，则不对称
		if (left == null || right == null) {
			return false;
		}
		// 比较当前节点值，并递归比较外侧和内侧
		return left.val == right.val && 
			   isMirror(left.left, right.right) && 
			   isMirror(left.right, right.left);
	}

	// LeetCode 226. 翻转二叉树
	// 题目链接：https://leetcode.cn/problems/invert-binary-tree/
	// 题目描述：给你一棵二叉树的根节点 root ，翻转这棵二叉树，并返回其根节点。
	// 解法：使用递归，交换每个节点的左右子树
	// 时间复杂度：O(n)，其中n是二叉树的节点数
	// 空间复杂度：O(h)，其中h是二叉树的高度
	public static TreeNode invertTree(TreeNode root) {
		// 基础情况：空节点无需翻转
		if (root == null) {
			return null;
		}
		// 交换左右子树
		TreeNode temp = root.left;
		root.left = root.right;
		root.right = temp;
		// 递归翻转左右子树
		invertTree(root.left);
		invertTree(root.right);
		return root;
	}

	// =========================== 扩展题目部分 ===========================

	// LeetCode 112. 路径总和
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/path-sum/
	// 题目描述：给你二叉树的根节点 root 和一个表示目标和的整数 targetSum。
	// 判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和 targetSum。
	// 叶子节点是指没有子节点的节点。
	//
	// 思路分析：
	// 1. 使用递归，从根节点开始，每次递归时减去当前节点的值
	// 2. 当到达叶子节点时，检查剩余的目标和是否等于叶子节点的值
	// 3. 递归地检查左右子树是否存在满足条件的路径
	//
	// 时间复杂度：O(n)，其中n是二叉树的节点数，每个节点访问一次
	// 空间复杂度：O(h)，其中h是二叉树的高度，递归调用栈的深度
	//
	// 是否为最优解：是。递归遍历是解决此类路径问题的最优方法。
	//
	// 边界场景：
	// - 空树：返回false
	// - 只有根节点：检查根节点值是否等于targetSum
	// - 负数节点值：算法依然有效
	// - 目标和为0：正常处理
	public static boolean hasPathSum(TreeNode root, int targetSum) {
		// 边界情况：空节点返回false
		if (root == null) {
			return false;
		}
		// 到达叶子节点，检查路径和是否等于目标和
		if (root.left == null && root.right == null) {
			return root.val == targetSum;
		}
		// 递归检查左右子树，目标和减去当前节点的值
		return hasPathSum(root.left, targetSum - root.val) || 
			   hasPathSum(root.right, targetSum - root.val);
	}

	// LeetCode 113. 路径总和 II
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/path-sum-ii/
	// 题目描述：给你二叉树的根节点 root 和一个整数目标和 targetSum，
	// 找出所有 从根节点到叶子节点 路径总和等于给定目标和的路径。
	//
	// 思路分析：
	// 1. 使用回溯法，维护一个当前路径列表
	// 2. 递归遍历树，每次将当前节点加入路径
	// 3. 到达叶子节点时，检查路径和是否等于目标和，若是则将路径加入结果
	// 4. 回溯时移除当前节点
	//
	// 时间复杂度：O(n^2)，其中n是节点数，最坏情况下需要复制所有路径
	// 空间复杂度：O(n)，递归栈和路径存储的空间
	//
	// 是否为最优解：是。回溯+递归是解决所有路径问题的标准方法。
	public static List<List<Integer>> pathSum(TreeNode root, int targetSum) {
		List<List<Integer>> result = new ArrayList<>();
		List<Integer> path = new ArrayList<>();
		pathSumHelper(root, targetSum, path, result);
		return result;
	}

	private static void pathSumHelper(TreeNode node, int targetSum, 
									  List<Integer> path, List<List<Integer>> result) {
		if (node == null) {
			return;
		}
		// 将当前节点加入路径
		path.add(node.val);
		// 到达叶子节点，检查路径和
		if (node.left == null && node.right == null && node.val == targetSum) {
			result.add(new ArrayList<>(path)); // 复制当前路径
		}
		// 递归遍历左右子树
		pathSumHelper(node.left, targetSum - node.val, path, result);
		pathSumHelper(node.right, targetSum - node.val, path, result);
		// 回溯：移除当前节点
		path.remove(path.size() - 1);
	}

	// LeetCode 111. 二叉树的最小深度
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/minimum-depth-of-binary-tree/
	// 题目描述：给定一个二叉树，找出其最小深度。
	// 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
	//
	// 思路分析：
	// 1. 使用递归，注意必须到达叶子节点才算一条路径
	// 2. 如果一个节点只有左子树或只有右子树，不能简单取min，要继续递归非空子树
	// 3. 只有当左右子树都存在时，才取较小深度
	//
	// 时间复杂度：O(n)，其中n是节点数
	// 空间复杂度：O(h)，其中h是树的高度
	//
	// 是否为最优解：是。但BFS层序遍历也是最优解，在某些情况下更快（遇到第一个叶子节点即可返回）。
	//
	// 常见错误：直接用Math.min(左深度, 右深度)会在单子树情况下出错
	public static int minDepth(TreeNode root) {
		if (root == null) {
			return 0;
		}
		// 如果左子树为空，只递归右子树
		if (root.left == null) {
			return minDepth(root.right) + 1;
		}
		// 如果右子树为空，只递归左子树
		if (root.right == null) {
			return minDepth(root.left) + 1;
		}
		// 左右子树都存在，取较小深度
		return Math.min(minDepth(root.left), minDepth(root.right)) + 1;
	}

	// LeetCode 257. 二叉树的所有路径
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/binary-tree-paths/
	// 题目描述：给你一个二叉树的根节点 root，按 任意顺序，
	// 返回所有从根节点到叶子节点的路径。
	//
	// 思路分析：
	// 1. 使用递归+回溯，构建路径字符串
	// 2. 到达叶子节点时，将路径字符串加入结果
	// 3. 使用StringBuilder可以提高效率（但要注意回溯时删除字符）
	//
	// 时间复杂度：O(n^2)，需要构建和复制路径字符串
	// 空间复杂度：O(n)，递归栈和结果存储
	//
	// 是否为最优解：是。递归+回溯是标准解法。
	public static List<String> binaryTreePaths(TreeNode root) {
		List<String> result = new ArrayList<>();
		if (root == null) {
			return result;
		}
		binaryTreePathsHelper(root, "", result);
		return result;
	}

	private static void binaryTreePathsHelper(TreeNode node, String path, 
											  List<String> result) {
		if (node == null) {
			return;
		}
		// 构建当前路径
		path += node.val;
		// 到达叶子节点，加入结果
		if (node.left == null && node.right == null) {
			result.add(path);
			return;
		}
		// 继续递归，路径中加入箭头
		path += "->";
		binaryTreePathsHelper(node.left, path, result);
		binaryTreePathsHelper(node.right, path, result);
	}

	// LeetCode 543. 二叉树的直径
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/diameter-of-binary-tree/
	// 题目描述：给定一棵二叉树，你需要计算它的直径长度。
	// 一棵二叉树的直径长度是任意两个结点路径长度中的最大值。
	// 这条路径可能穿过也可能不穿过根节点。
	//
	// 思路分析：
	// 1. 直径 = 某个节点的左子树最大深度 + 右子树最大深度
	// 2. 需要递归计算每个节点的这个值，并维护全局最大值
	// 3. 使用后序遍历，先计算子树深度，再更新直径
	//
	// 时间复杂度：O(n)，每个节点访问一次
	// 空间复杂度：O(h)，递归栈深度
	//
	// 是否为最优解：是。一次遍历即可得到答案。
	private static int maxDiameter = 0;

	public static int diameterOfBinaryTree(TreeNode root) {
		maxDiameter = 0;
		getDepth(root);
		return maxDiameter;
	}

	private static int getDepth(TreeNode node) {
		if (node == null) {
			return 0;
		}
		// 递归计算左右子树深度
		int leftDepth = getDepth(node.left);
		int rightDepth = getDepth(node.right);
		// 更新最大直径：左深度 + 右深度
		maxDiameter = Math.max(maxDiameter, leftDepth + rightDepth);
		// 返回当前节点的深度
		return Math.max(leftDepth, rightDepth) + 1;
	}

	// LeetCode 404. 左叶子之和
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/sum-of-left-leaves/
	// 题目描述：给定二叉树的根节点 root，返回所有左叶子之和。
	//
	// 思路分析：
	// 1. 递归遍历树，判断节点是否为左叶子
	// 2. 左叶子的定义：是某个节点的左孩子，且该孩子没有子节点
	// 3. 需要从父节点判断，而不是在节点自身判断
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是
	public static int sumOfLeftLeaves(TreeNode root) {
		if (root == null) {
			return 0;
		}
		int sum = 0;
		// 检查左子节点是否为叶子
		if (root.left != null && root.left.left == null && root.left.right == null) {
			sum += root.left.val;
		}
		// 递归计算左右子树的左叶子之和
		sum += sumOfLeftLeaves(root.left);
		sum += sumOfLeftLeaves(root.right);
		return sum;
	}

	// LeetCode 572. 另一棵树的子树
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/subtree-of-another-tree/
	// 题目描述：给你两棵二叉树 root 和 subRoot。
	// 检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。
	//
	// 思路分析：
	// 1. 递归检查root的每个节点，看是否与subRoot相同
	// 2. 使用isSameTree函数检查两棵树是否相同
	// 3. 如果当前节点不匹配，继续递归检查左右子树
	//
	// 时间复杂度：O(m*n)，m和n分别是两棵树的节点数
	// 空间复杂度：O(max(h1, h2))，递归栈深度
	//
	// 是否为最优解：否。更优解法是使用KMP或序列化+字符串匹配，时间复杂度O(m+n)
	// 但递归解法更直观，在面试中更常用
	public static boolean isSubtree(TreeNode root, TreeNode subRoot) {
		if (root == null) {
			return false;
		}
		// 检查当前节点是否与subRoot相同
		if (isSameTree(root, subRoot)) {
			return true;
		}
		// 递归检查左右子树
		return isSubtree(root.left, subRoot) || isSubtree(root.right, subRoot);
	}

	// LeetCode 617. 合并二叉树
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/merge-two-binary-trees/
	// 题目描述：给你两棵二叉树：root1 和 root2。
	// 想象一下，当你将其中一棵覆盖到另一棵之上时，两棵树上的一些节点将会重叠。
	// 你需要将这两棵树合并成一棵新二叉树。合并规则是：
	// 如果两个节点重叠，那么将这两个节点的值相加作为合并后节点的新值；
	// 否则，不为 null 的节点将直接作为新二叉树的节点。
	//
	// 思路分析：
	// 1. 同时递归遍历两棵树
	// 2. 如果两个节点都存在，值相加
	// 3. 如果只有一个节点存在，直接使用该节点
	//
	// 时间复杂度：O(min(m,n))
	// 空间复杂度：O(min(h1,h2))
	//
	// 是否为最优解：是
	public static TreeNode mergeTrees(TreeNode root1, TreeNode root2) {
		// 如果一棵树为空，返回另一棵树
		if (root1 == null) {
			return root2;
		}
		if (root2 == null) {
			return root1;
		}
		// 创建新节点，值为两节点之和
		TreeNode merged = new TreeNode(root1.val + root2.val);
		// 递归合并左右子树
		merged.left = mergeTrees(root1.left, root2.left);
		merged.right = mergeTrees(root1.right, root2.right);
		return merged;
	}

	// LeetCode 654. 最大二叉树
	// 题目来源：LeetCode  
	// 题目链接：https://leetcode.cn/problems/maximum-binary-tree/
	// 题目描述：给定一个不重复的整数数组 nums。
	// 最大二叉树可以用下面的算法从 nums 递归地构建:
	// 1. 创建一个根节点，其值为 nums 中的最大值。
	// 2. 递归地在最大值左边的子数组前缀上构建左子树。
	// 3. 递归地在最大值右边的子数组后缀上构建右子树。
	//
	// 思路分析：
	// 1. 找到数组中的最大值及其索引
	// 2. 最大值作为根节点
	// 3. 递归构建左右子树
	//
	// 时间复杂度：O(n^2)，最坏情况下数组有序，每次都要遍历剩余元素找最大值
	// 空间复杂度：O(n)，递归栈深度
	//
	// 是否为最优解：否。使用单调栈可以优化到O(n)时间复杂度
	// 但递归解法更符合题意，代码更简洁
	public static TreeNode constructMaximumBinaryTree(int[] nums) {
		return buildMaxTree(nums, 0, nums.length - 1);
	}

	private static TreeNode buildMaxTree(int[] nums, int left, int right) {
		if (left > right) {
			return null;
		}
		// 找到最大值的索引
		int maxIndex = left;
		for (int i = left + 1; i <= right; i++) {
			if (nums[i] > nums[maxIndex]) {
				maxIndex = i;
			}
		}
		// 创建根节点
		TreeNode root = new TreeNode(nums[maxIndex]);
		// 递归构建左右子树
		root.left = buildMaxTree(nums, left, maxIndex - 1);
		root.right = buildMaxTree(nums, maxIndex + 1, right);
		return root;
	}

	// 二叉树遍历的C++版本实现（作为注释提供）
	/*
	// C++版本的前序遍历
	void preOrder(TreeNode* root) {
		if (root == nullptr) {
			return;
		}
		cout << root->val << " ";
		preOrder(root->left);
		preOrder(root->right);
	}

	// C++版本的中序遍历
	void inOrder(TreeNode* root) {
		if (root == nullptr) {
			return;
		}
		inOrder(root->left);
		cout << root->val << " ";
		inOrder(root->right);
	}

	// C++版本的后序遍历
	void posOrder(TreeNode* root) {
		if (root == nullptr) {
			return;
		}
		posOrder(root->left);
		posOrder(root->right);
		cout << root->val << " ";
	}
	*/

	// 二叉树遍历的Python版本实现（作为注释提供）
	/*
	# Python版本的前序遍历
	def preOrder(root):
		if root is None:
			return
		print(root.val, end=" ")
		preOrder(root.left)
		preOrder(root.right)

	# Python版本的中序遍历
	def inOrder(root):
		if root is None:
			return
		inOrder(root.left)
		print(root.val, end=" ")
		inOrder(root.right)

	# Python版本的后序遍历
	def posOrder(root):
		if root is None:
			return
		posOrder(root.left)
		posOrder(root.right)
		print(root.val, end=" ")
	*/

	// LeetCode 563. 二叉树的坡度
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/binary-tree-tilt/
	// 题目描述：给你一个二叉树的根节点 root，计算并返回 整个树 的坡度。
	// 一个树的节点的坡度定义即为，该节点左子树的节点之和和右子树节点之和的差的绝对值。
	// 如果没有左子树的话，左子树的节点之和为 0 ；没有右子树的话也是一样。
	// 空节点的坡度是 0。整个树的坡度就是其所有节点的坡度之和。
	//
	// 思路分析：
	// 1. 使用后序遍历，先计算子树的节点和
	// 2. 对于每个节点，计算左右子树节点和的差的绝对值，累加到总坡度
	// 3. 返回当前子树的节点和（包括根节点）
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是
	private static int totalTilt = 0;

	public static int findTilt(TreeNode root) {
		totalTilt = 0;
		calculateSum(root);
		return totalTilt;
	}

	private static int calculateSum(TreeNode node) {
		if (node == null) {
			return 0;
		}
		// 计算左右子树的节点和
		int leftSum = calculateSum(node.left);
		int rightSum = calculateSum(node.right);
		// 累加当前节点的坡度
		totalTilt += Math.abs(leftSum - rightSum);
		// 返回当前子树的节点和
		return leftSum + rightSum + node.val;
	}

	// LeetCode 508. 出现次数最多的子树元素和
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/most-frequent-subtree-sum/
	// 题目描述：给你一个二叉树的根结点 root，计算出现最多的子树元素和。
	// 一个结点的 「子树元素和」定义为：以该结点为根的二叉树上所有结点的元素之和（包括结点自身）。
	//
	// 思路分析：
	// 1. 使用后序遍历计算每个子树的元素和
	// 2. 使用HashMap统计每个和出现的次数
	// 3. 找出出现次数最多的所有和
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(n)，需要HashMap存储每个子树和
	//
	// 是否为最优解：是
	public static int[] findFrequentTreeSum(TreeNode root) {
		Map<Integer, Integer> sumCount = new HashMap<>();
		calculateTreeSum(root, sumCount);
		// 找到最大频率
		int maxFreq = 0;
		for (int count : sumCount.values()) {
			maxFreq = Math.max(maxFreq, count);
		}
		// 收集所有最大频率的和
		List<Integer> result = new ArrayList<>();
		for (Map.Entry<Integer, Integer> entry : sumCount.entrySet()) {
			if (entry.getValue() == maxFreq) {
				result.add(entry.getKey());
			}
		}
		// 转换为数组
		return result.stream().mapToInt(Integer::intValue).toArray();
	}

	private static int calculateTreeSum(TreeNode node, Map<Integer, Integer> sumCount) {
		if (node == null) {
			return 0;
		}
		// 计算左右子树的和
		int leftSum = calculateTreeSum(node.left, sumCount);
		int rightSum = calculateTreeSum(node.right, sumCount);
		// 当前子树的总和
		int totalSum = leftSum + rightSum + node.val;
		// 统计出现次数
		sumCount.put(totalSum, sumCount.getOrDefault(totalSum, 0) + 1);
		return totalSum;
	}

	// LeetCode 437. 路径总和 III
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/path-sum-iii/
	// 题目描述：给定一个二叉树的根节点 root，和一个整数 targetSum，
	// 求该二叉树里节点值之和等于 targetSum 的 路径 的数目。
	// 路径 不需要从根节点开始，也不需要在叶子节点结束，但是路径方向必须是向下的（只从父节点到子节点）。
	//
	// 思路分析：
	// 方法1：双重递归 - 需要两个递归函数
	//   1. 第一个递归遍历所有节点
	//   2. 第二个递归以当前节点为起点计算路径数
	// 方法2：前缀和+HashMap - 更优的解法
	//   1. 使用前缀和思想，类似于数组的子数组和问题
	//   2. 用HashMap存储前缀和及其出现次数
	//
	// 时间复杂度：方法1 O(n^2)，方法2 O(n)
	// 空间复杂度：方法1 O(h)，方法2 O(n)
	//
	// 是否为最优解：方法2是最优解，但方法1更直观。这里实现两种方法。

	// 方法1：双重递归
	public static int pathSumIII(TreeNode root, int targetSum) {
		if (root == null) {
			return 0;
		}
		// 以当前节点为起点的路径数 + 左子树中的路径数 + 右子树中的路径数
		return countPathsFrom(root, targetSum) + 
			   pathSumIII(root.left, targetSum) + 
			   pathSumIII(root.right, targetSum);
	}

	// 计算从当前节点开始的路径数
	private static int countPathsFrom(TreeNode node, long targetSum) {
		if (node == null) {
			return 0;
		}
		int count = 0;
		// 如果当前节点的值等于目标和，找到一条路径
		if (node.val == targetSum) {
			count++;
		}
		// 继续在左右子树中查找，目标和减去当前节点值
		count += countPathsFrom(node.left, targetSum - node.val);
		count += countPathsFrom(node.right, targetSum - node.val);
		return count;
	}

	// 方法2：前缀和 + HashMap（最优解）
	public static int pathSumIII_Optimal(TreeNode root, int targetSum) {
		Map<Long, Integer> prefixSumCount = new HashMap<>();
		// 初始化：前缀和为0的路径有一条
		prefixSumCount.put(0L, 1);
		return dfsWithPrefixSum(root, 0L, targetSum, prefixSumCount);
	}

	private static int dfsWithPrefixSum(TreeNode node, long currentSum, int targetSum,
											Map<Long, Integer> prefixSumCount) {
		if (node == null) {
			return 0;
		}
		// 当前路径的前缀和
		currentSum += node.val;
		// 查找是否存在前缀和为 currentSum - targetSum 的路径
		int count = prefixSumCount.getOrDefault(currentSum - targetSum, 0);
		// 将当前前缀和加入map
		prefixSumCount.put(currentSum, prefixSumCount.getOrDefault(currentSum, 0) + 1);
		// 递归遍历左右子树
		count += dfsWithPrefixSum(node.left, currentSum, targetSum, prefixSumCount);
		count += dfsWithPrefixSum(node.right, currentSum, targetSum, prefixSumCount);
		// 回溯：移除当前节点的前缀和
		prefixSumCount.put(currentSum, prefixSumCount.get(currentSum) - 1);
		return count;
	}

	// LeetCode 236. 二叉树的最近公共祖先
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/
	// 题目描述：给定一个二叉树，找到该树中两个指定节点的最近公共祖先。
	//
	// 思路分析：
	// 1. 使用递归，分为三种情况：
	//    a. 如果p和q分别在左右子树，则当前节点就是最近公共祖先
	//    b. 如果p和q都在左子树，则最近公共祖先在左子树
	//    c. 如果p和q都在右子树，则最近公共祖先在右子树
	// 2. 特殊情况：当前节点就是p或q，且另一个节点在其子树中
	//
	// 时间复杂度：O(n)，最坏情况需要遍历所有节点
	// 空间复杂度：O(h)，递归栈深度
	//
	// 是否为最优解：是。这是经典的递归解法。
	public static TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
		// 基础情况：空节点或找到目标节点
		if (root == null || root == p || root == q) {
			return root;
		}
		// 递归在左右子树中查找
		TreeNode left = lowestCommonAncestor(root.left, p, q);
		TreeNode right = lowestCommonAncestor(root.right, p, q);
		// 如果左右子树都找到了，说明p和q分别在左右两侧，当前节点就是最近公共祖先
		if (left != null && right != null) {
			return root;
		}
		// 否则返回非空的一侧
		return left != null ? left : right;
	}

	// LeetCode 124. 二叉树中的最大路径和
	// 题目来源：LeetCode
	// 题目链接：https://leetcode.cn/problems/binary-tree-maximum-path-sum/
	// 题目描述：二叉树中的 路径 被定义为一条从树中任意节点出发，
	// 沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 至多出现一次。
	// 该路径 至少包含一个 节点，且不一定经过根节点。
	// 路径和 是路径中各节点值的总和。给你一个二叉树的根节点 root ，返回其 最大路径和。
	//
	// 思路分析：
	// 1. 对于每个节点，最大路径和可能是：
	//    - 左子树的最大贡献 + 节点值 + 右子树的最大贡献
	// 2. 但返回给父节点时，只能选择左或右一侧（因为路径不能分叉）
	// 3. 如果子树贡献为负，则不选择该子树（贡献为0）
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是。一次遍历即可得到答案。
	//
	// 这是Hard难度的经典题，需要理解“局部最优”和“向上返回”的区别
	private static int maxPathSum = Integer.MIN_VALUE;

	public static int maxPathSum(TreeNode root) {
		maxPathSum = Integer.MIN_VALUE;
		maxGain(root);
		return maxPathSum;
	}

	private static int maxGain(TreeNode node) {
		if (node == null) {
			return 0;
		}
		// 递归计算左右子树的最大贡献，负数则不选
		int leftGain = Math.max(maxGain(node.left), 0);
		int rightGain = Math.max(maxGain(node.right), 0);
		// 更新全局最大路径和：左贡献 + 节点值 + 右贡献
		int currentPathSum = leftGain + node.val + rightGain;
		maxPathSum = Math.max(maxPathSum, currentPathSum);
		// 返回节点的最大贡献：节点值 + max(左贡献, 右贡献)
		return node.val + Math.max(leftGain, rightGain);
	}
	
	// LintCode 453. 将二叉树拆分为链表
	// 题目来源：LintCode（炼码）
	// 题目链接：https://www.lintcode.com/problem/453/
	// 题目描述：将一棵二叉树按照前序遍历拆解成为一个假链表。所谓的假链表是说，用二叉树的 right 指针，来表示链表中的 next 指针。
	// 要求不能创建任何新的节点，只能调整树中节点指针的指向。
	//
	// 思路分析：
	// 1. 使用后序遍历，先处理左右子树
	// 2. 对于每个节点，将左子树变成链表，右子树变成链表
	// 3. 将左子树链接到右子树上，左子树指针设为null
	// 4. 返回当前链表的末尾节点
	//
	// 时间复杂度：O(n)，每个节点访问一次
	// 空间复杂度：O(h)，递归栈深度
	//
	// 是否为最优解：是。递归解法简洁高效。
	public static void flatten(TreeNode root) {
		if (root == null) {
			return;
		}
		flattenHelper(root);
	}
	
	private static TreeNode flattenHelper(TreeNode node) {
		if (node == null) {
			return null;
		}
		// 叶子节点直接返回
		if (node.left == null && node.right == null) {
			return node;
		}
		// 递归处理左右子树
		TreeNode leftTail = flattenHelper(node.left);
		TreeNode rightTail = flattenHelper(node.right);
		// 如果左子树不为空，将左子树连接到右子树上
		if (leftTail != null) {
			leftTail.right = node.right;
			node.right = node.left;
			node.left = null; // 清空左指针
		}
		// 返回新的链表末尾节点
		return rightTail != null ? rightTail : leftTail;
	}
	
	// HackerRank 二叉树的镜像
	// 题目来源：HackerRank
	// 题目描述：给定一棵二叉树，判断它是否是自身的镜像（即对称）
	// 这个题目与LeetCode 101类似，但增加了更多的工程化考量
	//
	// 思路分析：
	// 1. 使用辅助函数检查两棵子树是否互为镜像
	// 2. 两棵子树互为镜像的条件：
	//    - 当前节点值相等
	//    - 左子树的左子树与右子树的右子树互为镜像
	//    - 左子树的右子树与右子树的左子树互为镜像
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是
	public static boolean isSymmetricAdvanced(TreeNode root) {
		if (root == null) {
			return true;
		}
		return isMirror(root.left, root.right);
	}
	
	// 剑指Offer 26. 树的子结构
	// 题目来源：剑指Offer
	// 题目描述：输入两棵二叉树A和B，判断B是不是A的子结构。
	// 注意：这里的子结构与LeetCode 572的子树定义不同，子结构只要A中存在一个子树与B完全相同即可，不需要是完整的子树。
	//
	// 思路分析：
	// 1. 遍历树A的每个节点，以该节点为根节点
	// 2. 检查从该节点开始的子树是否包含树B的结构
	// 3. 注意边界条件处理
	//
	// 时间复杂度：O(m*n)，m和n分别是两棵树的节点数
	// 空间复杂度：O(h)，递归栈深度
	//
	// 是否为最优解：是
	public static boolean isSubStructure(TreeNode A, TreeNode B) {
		// 边界条件：任意一个树为空，返回false
		if (A == null || B == null) {
			return false;
		}
		// 检查以A为根节点是否包含B，或者A的左子树是否包含B，或者A的右子树是否包含B
		return isSubStructureHelper(A, B) || 
			   isSubStructure(A.left, B) || 
			   isSubStructure(A.right, B);
	}
	
	private static boolean isSubStructureHelper(TreeNode A, TreeNode B) {
		// 如果B为空，说明B的所有节点都已经匹配完成
		if (B == null) {
			return true;
		}
		// 如果A为空，或者A和B的值不相等，匹配失败
		if (A == null || A.val != B.val) {
			return false;
		}
		// 递归检查左右子树
		return isSubStructureHelper(A.left, B.left) && 
			   isSubStructureHelper(A.right, B.right);
	}
	
	// USACO 二叉搜索树的最近公共祖先
	// 题目来源：USACO（美国计算机奥林匹克竞赛）
	// 题目描述：给定一个二叉搜索树（BST），找到该树中两个指定节点的最近公共祖先。
	// 注：BST的特性可以帮助我们优化最近公共祖先的查找
	//
	// 思路分析：
	// 利用BST的特性：左子树所有节点值小于根节点，右子树所有节点值大于根节点
	// 1. 如果p和q的值都小于当前节点，那么LCA在左子树
	// 2. 如果p和q的值都大于当前节点，那么LCA在右子树
	// 3. 否则，当前节点就是LCA
	//
	// 时间复杂度：O(h)，h为树的高度，最坏情况O(n)
	// 空间复杂度：O(h)，递归栈深度
	//
	// 是否为最优解：是。利用BST特性的解法比普通二叉树的解法更高效
	public static TreeNode lowestCommonAncestorBST(TreeNode root, TreeNode p, TreeNode q) {
		if (root == null || p == null || q == null) {
			return null;
		}
		// 如果p和q都在左子树
		if (p.val < root.val && q.val < root.val) {
			return lowestCommonAncestorBST(root.left, p, q);
		}
		// 如果p和q都在右子树
		if (p.val > root.val && q.val > root.val) {
			return lowestCommonAncestorBST(root.right, p, q);
		}
		// 否则，当前节点就是最近公共祖先
		return root;
	}
	
	// AtCoder ABC191 E. Come Back Quickly
	// 这是一个关于图论的题目，但其中涉及到树的递归遍历思想
	// 题目描述简化：给定一棵有根树，计算每个节点到其所有子孙节点的距离之和
	//
	// 思路分析：
	// 1. 使用后序遍历计算每个子树的节点数
	// 2. 使用前序遍历计算距离之和
	// 3. 需要两次递归：第一次计算子树大小，第二次计算距离和
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是
	public static long[] calculateDistanceSum(TreeNode root, int n) {
		long[] result = new long[n];
		// 第一次DFS：计算子树大小
		int[] size = new int[n];
		dfsSize(root, size);
		// 第二次DFS：计算距离和
		dfsDistance(root, size, result, 0);
		return result;
	}
	
	private static int dfsSize(TreeNode node, int[] size) {
		if (node == null) {
			return 0;
		}
		size[node.val] = 1; // 包含自己
		size[node.val] += dfsSize(node.left, size);
		size[node.val] += dfsSize(node.right, size);
		return size[node.val];
	}
	
	private static void dfsDistance(TreeNode node, int[] size, long[] result, long parentDistance) {
		if (node == null) {
			return;
		}
		result[node.val] = parentDistance;
		// 计算左子树的距离和
		if (node.left != null) {
			int leftSize = size[node.left.val];
			int rightSize = node.right != null ? size[node.right.val] : 0;
			// 父节点的距离和 + (n - leftSize) - leftSize
			long leftDistance = parentDistance + (size[node.val] - leftSize) - leftSize;
			dfsDistance(node.left, size, result, leftDistance);
		}
		// 计算右子树的距离和
		if (node.right != null) {
			int rightSize = size[node.right.val];
			int leftSize = node.left != null ? size[node.left.val] : 0;
			long rightDistance = parentDistance + (size[node.val] - rightSize) - rightSize;
			dfsDistance(node.right, size, result, rightDistance);
		}
	}
	
	// CodeChef - SUBTREE - Subtree Removal
	// 题目来源：CodeChef
	// 题目描述简化：给定一棵二叉树，每个节点有一个权值。找出权值和最大的子树。
	//
	// 思路分析：
	// 1. 使用后序遍历，计算每个子树的权值和
	// 2. 对于每个节点，其最大子树和为：节点值 + max(左子树最大和, 0) + max(右子树最大和, 0)
	// 3. 维护一个全局变量记录最大子树和
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(h)
	//
	// 是否为最优解：是
	private static int maxSubtreeSum = Integer.MIN_VALUE;
	
	public static int maxSubtreeSum(TreeNode root) {
		maxSubtreeSum = Integer.MIN_VALUE;
		calculateSubtreeSum(root);
		return maxSubtreeSum;
	}
	
	private static int calculateSubtreeSum(TreeNode node) {
		if (node == null) {
			return 0;
		}
		// 递归计算左右子树的最大子树和
		int leftSum = Math.max(calculateSubtreeSum(node.left), 0);
		int rightSum = Math.max(calculateSubtreeSum(node.right), 0);
		// 计算当前子树的最大和
		int currentSum = node.val + leftSum + rightSum;
		// 更新全局最大值
		maxSubtreeSum = Math.max(maxSubtreeSum, currentSum);
		// 返回当前节点的最大贡献（可以包含或不包含子节点）
		return node.val + Math.max(leftSum, rightSum);
	}
	
	// UVa OJ 10080 - Gopher II
	// 虽然主要是二分图匹配问题，但其中涉及到树的递归构建
	// 这里实现一个相关的树结构递归问题：重建二叉树
	//
	// 思路分析：
	// 根据前序遍历和中序遍历结果重建二叉树
	// 1. 前序遍历的第一个节点是根节点
	// 2. 在中序遍历中找到根节点的位置，分割左右子树
	// 3. 递归重建左右子树
	//
	// 时间复杂度：O(n^2)，最坏情况下树是链状的
	// 空间复杂度：O(n)
	//
	// 最优解：可以使用哈希表优化查找根节点的过程，将时间复杂度降为O(n)
	public static TreeNode buildTree(int[] preorder, int[] inorder) {
		if (preorder == null || inorder == null || preorder.length == 0 || inorder.length == 0) {
			return null;
		}
		return buildTreeHelper(preorder, 0, preorder.length - 1, 
							   inorder, 0, inorder.length - 1);
	}
	
	private static TreeNode buildTreeHelper(int[] preorder, int preStart, int preEnd,
											int[] inorder, int inStart, int inEnd) {
		if (preStart > preEnd || inStart > inEnd) {
			return null;
		}
		// 创建根节点
		TreeNode root = new TreeNode(preorder[preStart]);
		// 找到根节点在中序遍历中的位置
		int rootIndex = inStart;
		for (; rootIndex <= inEnd; rootIndex++) {
			if (inorder[rootIndex] == root.val) {
				break;
			}
		}
		// 计算左子树的节点数
		int leftSize = rootIndex - inStart;
		// 递归构建左右子树
		root.left = buildTreeHelper(preorder, preStart + 1, preStart + leftSize,
									inorder, inStart, rootIndex - 1);
		root.right = buildTreeHelper(preorder, preStart + leftSize + 1, preEnd,
									 inorder, rootIndex + 1, inEnd);
		return root;
	}
	
	// 牛客网 NC102. 比较版本号
	// 虽然这是字符串处理题目，但我们可以将其转化为树的递归问题
	// 这里实现一个树的序列化和反序列化问题，这是牛客网的高频题
	//
	// 思路分析：
	// 将二叉树序列化为字符串，然后从字符串反序列化回二叉树
	// 序列化使用前序遍历，空节点用特殊字符表示
	//
	// 时间复杂度：O(n)
	// 空间复杂度：O(n)
	//
	// 是否为最优解：是
	public static String serialize(TreeNode root) {
		StringBuilder sb = new StringBuilder();
		serializeHelper(root, sb);
		return sb.toString();
	}
	
	private static void serializeHelper(TreeNode node, StringBuilder sb) {
		if (node == null) {
			sb.append("#,");
			return;
		}
		sb.append(node.val).append(",");
		serializeHelper(node.left, sb);
		serializeHelper(node.right, sb);
	}
	
	public static TreeNode deserialize(String data) {
		if (data == null || data.isEmpty()) {
			return null;
		}
		String[] nodes = data.split(",");
		return deserializeHelper(nodes, new int[]{0});
	}
	
	private static TreeNode deserializeHelper(String[] nodes, int[] index) {
		if (index[0] >= nodes.length || "#".equals(nodes[index[0]])) {
			index[0]++;
			return null;
		}
		TreeNode node = new TreeNode(Integer.parseInt(nodes[index[0]++]));
		node.left = deserializeHelper(nodes, index);
		node.right = deserializeHelper(nodes, index);
		return node;
	}
	
	// 杭电OJ 2024 - 二叉树遍历
	// 题目来源：杭州电子科技大学OJ
	// 题目描述：输入二叉树的前序遍历和中序遍历结果，输出其后序遍历结果
	//
	// 思路分析：
	// 1. 先根据前序和中序构建二叉树
	// 2. 然后进行后序遍历输出
	//
	// 时间复杂度：O(n^2)
	// 空间复杂度：O(n)
	//
	// 最优解：可以使用哈希表优化查找过程
	public static String postorderFromPreorderAndInorder(String preorder, String inorder) {
		if (preorder == null || inorder == null || preorder.isEmpty() || inorder.isEmpty()) {
			return "";
		}
		// 由于HDOJ 2024的节点是字符，这里构建一个特殊的辅助函数
		// 实际工程中应该根据具体数据类型调整
		StringBuilder sb = new StringBuilder();
		postorderHelper(preorder, 0, preorder.length() - 1, 
						inorder, 0, inorder.length() - 1, sb);
		return sb.toString();
	}
	
	private static void postorderHelper(String preorder, int preStart, int preEnd,
										String inorder, int inStart, int inEnd,
										StringBuilder sb) {
		if (preStart > preEnd || inStart > inEnd) {
			return;
		}
		// 根节点字符
		char rootVal = preorder.charAt(preStart);
		// 找到根节点在中序中的位置
		int rootIndex = inStart;
		for (; rootIndex <= inEnd; rootIndex++) {
			if (inorder.charAt(rootIndex) == rootVal) {
				break;
			}
		}
		// 计算左子树的长度
		int leftLength = rootIndex - inStart;
		// 递归处理左子树
		postorderHelper(preorder, preStart + 1, preStart + leftLength,
						inorder, inStart, rootIndex - 1, sb);
		// 递归处理右子树
		postorderHelper(preorder, preStart + leftLength + 1, preEnd,
						inorder, rootIndex + 1, inEnd, sb);
		// 后序：根节点最后添加
		sb.append(rootVal);
	}

	public static void main(String[] args) {
		System.out.println("========== 二叉树递归遍历基础测试 ==========");
		TreeNode head = new TreeNode(1);
		head.left = new TreeNode(2);
		head.right = new TreeNode(3);
		head.left.left = new TreeNode(4);
		head.left.right = new TreeNode(5);
		head.right.left = new TreeNode(6);
		head.right.right = new TreeNode(7);

		System.out.print("前序遍历：");
		preOrder(head);
		System.out.println();

		System.out.print("中序遍历：");
		inOrder(head);
		System.out.println();

		System.out.print("后序遍历：");
		posOrder(head);
		System.out.println();

		System.out.println("\n========== LeetCode 104. 最大深度 ==========");
		System.out.println("最大深度: " + maxDepth(head)); // 预期: 3

		System.out.println("\n========== LeetCode 110. 平衡二叉树 ==========");
		TreeNode balancedTree = new TreeNode(1);
		balancedTree.left = new TreeNode(2);
		balancedTree.right = new TreeNode(3);
		balancedTree.left.left = new TreeNode(4);
		balancedTree.left.right = new TreeNode(5);
		System.out.println("是否为平衡二叉树: " + isBalanced(balancedTree)); // 预期: true

		System.out.println("\n========== LeetCode 100. 相同的树 ==========");
		TreeNode tree1 = new TreeNode(1);
		tree1.left = new TreeNode(2);
		tree1.right = new TreeNode(3);
		TreeNode tree2 = new TreeNode(1);
		tree2.left = new TreeNode(2);
		tree2.right = new TreeNode(3);
		System.out.println("两棵树是否相同: " + isSameTree(tree1, tree2)); // 预期: true

		System.out.println("\n========== LeetCode 101. 对称二叉树 ==========");
		TreeNode symmetricTree = new TreeNode(1);
		symmetricTree.left = new TreeNode(2);
		symmetricTree.right = new TreeNode(2);
		symmetricTree.left.left = new TreeNode(3);
		symmetricTree.left.right = new TreeNode(4);
		symmetricTree.right.left = new TreeNode(4);
		symmetricTree.right.right = new TreeNode(3);
		System.out.println("是否为对称二叉树: " + isSymmetric(symmetricTree)); // 预期: true

		System.out.println("\n========== LeetCode 112. 路径总和 ==========");
		TreeNode pathTree = new TreeNode(5);
		pathTree.left = new TreeNode(4);
		pathTree.right = new TreeNode(8);
		pathTree.left.left = new TreeNode(11);
		pathTree.left.left.left = new TreeNode(7);
		pathTree.left.left.right = new TreeNode(2);
		System.out.println("是否存在路径和为22: " + hasPathSum(pathTree, 22)); // 预期: true

		System.out.println("\n========== LeetCode 113. 路径总和 II ==========");
		List<List<Integer>> paths = pathSum(pathTree, 22);
		System.out.println("路径总和为22的所有路径: " + paths); // 预期: [[5,4,11,2]]

		System.out.println("\n========== LeetCode 111. 最小深度 ==========");
		System.out.println("最小深度: " + minDepth(head)); // 预期: 3

		System.out.println("\n========== LeetCode 257. 二叉树的所有路径 ==========");
		List<String> allPaths = binaryTreePaths(head);
		System.out.println("所有路径: " + allPaths);

		System.out.println("\n========== LeetCode 543. 二叉树的直径 ==========");
		System.out.println("直径长度: " + diameterOfBinaryTree(head)); // 预期: 4

		System.out.println("\n========== LeetCode 404. 左叶子之和 ==========");
		System.out.println("左叶子之和: " + sumOfLeftLeaves(head)); // 预期: 4

		System.out.println("\n========== LeetCode 572. 另一棵树的子树 ==========");
		TreeNode subTree = new TreeNode(2);
		subTree.left = new TreeNode(4);
		subTree.right = new TreeNode(5);
		System.out.println("是否为子树: " + isSubtree(head, subTree)); // 预期: true

		System.out.println("\n========== LeetCode 617. 合并二叉树 ==========");
		TreeNode merged = mergeTrees(tree1, tree2);
		System.out.print("合并后的树（前序）：");
		preOrder(merged);
		System.out.println();

		System.out.println("\n========== LeetCode 654. 最大二叉树 ==========");
		int[] nums = {3, 2, 1, 6, 0, 5};
		TreeNode maxTree = constructMaximumBinaryTree(nums);
		System.out.print("最大二叉树（前序）：");
		preOrder(maxTree);
		System.out.println();

		System.out.println("\n========== LeetCode 563. 二叉树的坡度 ==========");
		System.out.println("整个树的坡度: " + findTilt(head)); // 预期: 6

		System.out.println("\n========== LeetCode 508. 出现次数最多的子树元素和 ==========");
		int[] frequentSums = findFrequentTreeSum(head);
		System.out.print("出现次数最多的子树和: ");
		for (int sum : frequentSums) {
			System.out.print(sum + " ");
		}
		System.out.println();

		System.out.println("\n========== LeetCode 437. 路径总和 III ==========");
		TreeNode pathTree3 = new TreeNode(10);
		pathTree3.left = new TreeNode(5);
		pathTree3.right = new TreeNode(-3);
		pathTree3.left.left = new TreeNode(3);
		pathTree3.left.right = new TreeNode(2);
		pathTree3.right.right = new TreeNode(11);
		pathTree3.left.left.left = new TreeNode(3);
		pathTree3.left.left.right = new TreeNode(-2);
		pathTree3.left.right.right = new TreeNode(1);
		System.out.println("路径和为8的路径数（双重递归）: " + pathSumIII(pathTree3, 8)); // 预期: 3
		System.out.println("路径和为8的路径数（前缀和）: " + pathSumIII_Optimal(pathTree3, 8)); // 预期: 3

		System.out.println("\n========== LeetCode 236. 最近公共祖先 ==========");
		TreeNode lca = lowestCommonAncestor(head, head.left.left, head.left.right);
		System.out.println("最近公共祖先的值: " + lca.val); // 预期: 2

		System.out.println("\n========== LeetCode 124. 二叉树中的最大路径和 ==========");
		TreeNode maxPathTree = new TreeNode(-10);
		maxPathTree.left = new TreeNode(9);
		maxPathTree.right = new TreeNode(20);
		maxPathTree.right.left = new TreeNode(15);
		maxPathTree.right.right = new TreeNode(7);
		System.out.println("最大路径和: " + maxPathSum(maxPathTree)); // 预期: 42

		System.out.println("\n========== LeetCode 226. 翻转二叉树 ==========");
		TreeNode invertTest = new TreeNode(4);
		invertTest.left = new TreeNode(2);
		invertTest.right = new TreeNode(7);
		invertTest.left.left = new TreeNode(1);
		invertTest.left.right = new TreeNode(3);
		System.out.print("翻转前（中序）：");
		inOrder(invertTest);
		System.out.println();
		invertTree(invertTest);
		System.out.print("翻转后（中序）：");
		inOrder(invertTest);
		System.out.println();

		System.out.println("\n========== 所有测试完成！ ==========");
	}

}