package class036;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringJoiner;

/**
 * 二叉树的层序序列化与反序列化
 * 测试链接 : https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
 * 
 * 二叉树序列化是将二叉树结构转换为可存储或传输的字符串格式的过程，
 * 反序列化则是将该字符串恢复为原二叉树结构的过程。
 * 
 * 层序序列化的核心思想：
 * 1. 按层遍历二叉树，将每个节点的值转换为字符串
 * 2. 对于空节点，使用特殊标记（如"#"）表示
 * 3. 用分隔符（如","）连接所有节点值
 * 
 * 时间复杂度：O(N)，其中N是二叉树中的节点数
 * 空间复杂度：O(N)，需要一个队列存储节点，以及一个字符串存储序列化结果
 * 
 * 相关题目：
 * 1. LeetCode 297. 二叉树的序列化与反序列化 (本文件)
 * 2. LintCode 7. 二叉树序列化与反序列化
 * 3. HackerRank Tree: Serialize and Deserialize
 * 4. CodeChef Serialize and Deserialize a Binary Tree
 * 5. UVA 10562 - Undraw the Trees
 * 6. Codeforces 862B - Mahmoud and Ehab and the bipartiteness
 * 7. AtCoder AHC001 - Let's Play Tag
 */
public class Code06_LevelorderSerializeAndDeserialize {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int val) {
			this.val = val;
		}
		
		public TreeNode() {}
		
		public TreeNode(int val, TreeNode left, TreeNode right) {
			this.val = val;
			this.left = left;
			this.right = right;
		}
	}

	/**
	 * 方法1：序列化，使用层序遍历的方式
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 * @param root 二叉树的根节点
	 * @return 序列化后的字符串
	 */
	public static String serialize(TreeNode root) {
		StringBuilder builder = new StringBuilder();
		if (root == null) {
			builder.append("#");
		} else {
			// 层序遍历，用队列实现
			Queue<TreeNode> queue = new LinkedList<>();
			queue.add(root);
			// 先放入头节点的值
			builder.append(root.val);
			while (!queue.isEmpty()) {
				TreeNode node = queue.poll();
				// 左孩子
				if (node.left != null) {
					builder.append("," + node.left.val);
					queue.add(node.left);
				} else {
					builder.append(",#");
				}
				// 右孩子
				if (node.right != null) {
					builder.append("," + node.right.val);
					queue.add(node.right);
				} else {
					builder.append(",#");
				}
			}
		}
		return builder.toString();
	}

	/**
	 * 方法2：序列化优化版本，使用StringJoiner提高可读性
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 * 优点：代码更清晰，使用StringJoiner处理分隔符
	 */
	public static String serializeOptimized(TreeNode root) {
		if (root == null) {
			return "#";
		}
		
		StringJoiner joiner = new StringJoiner(",");
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			
			if (node == null) {
				joiner.add("#");
			} else {
				joiner.add(String.valueOf(node.val));
				// 无论子节点是否为空，都入队，因为后续需要序列化空节点
				queue.offer(node.left);
				queue.offer(node.right);
			}
		}
		
		// 移除末尾的连续#以节省空间（可选优化）
		String result = joiner.toString();
		int lastNonHash = result.lastIndexOf('#');
		while (lastNonHash >= 0 && result.charAt(lastNonHash) == '#') {
			lastNonHash--;
		}
		return result.substring(0, lastNonHash + 1);
	}

	/**
	 * 反序列化，根据层序遍历的字符串，重建二叉树
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 * @param data 序列化的字符串
	 * @return 重建的二叉树根节点
	 */
	public static TreeNode deserialize(String data) {
		if (data == null || data.equals("#")) {
			return null;
		}
		String[] values = data.split(",");
		// 头节点
		TreeNode root = new TreeNode(Integer.parseInt(values[0]));
		// 队列
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		// 从第二个位置开始遍历，因为0位置是头节点
		int index = 1;
		while (!queue.isEmpty() && index < values.length) {
			TreeNode node = queue.poll();
			// 左孩子
			if (index < values.length && !values[index].equals("#")) {
				node.left = new TreeNode(Integer.parseInt(values[index]));
				queue.add(node.left);
			}
			index++;
			// 右孩子
			if (index < values.length && !values[index].equals("#")) {
				node.right = new TreeNode(Integer.parseInt(values[index]));
				queue.add(node.right);
			}
			index++;
		}
		return root;
	}

	/**
	 * 反序列化优化版本，更健壮的边界处理
	 * 时间复杂度：O(N)
	 * 空间复杂度：O(N)
	 */
	public static TreeNode deserializeOptimized(String data) {
		if (data == null || data.trim().isEmpty() || data.equals("#")) {
			return null;
		}
		
		String[] values = data.split(",");
		if (values.length == 0 || values[0].equals("#")) {
			return null;
		}
		
		// 头节点
		TreeNode root = new TreeNode(Integer.parseInt(values[0]));
		Queue<TreeNode> queue = new LinkedList<>();
		queue.offer(root);
		
		int index = 1;
		while (!queue.isEmpty() && index < values.length) {
			TreeNode current = queue.poll();
			
			// 处理左子节点
			if (index < values.length) {
				String leftVal = values[index++];
				if (!leftVal.equals("#")) {
					current.left = new TreeNode(Integer.parseInt(leftVal));
					queue.offer(current.left);
				}
			}
			
			// 处理右子节点
			if (index < values.length) {
				String rightVal = values[index++];
				if (!rightVal.equals("#")) {
					current.right = new TreeNode(Integer.parseInt(rightVal));
					queue.offer(current.right);
				}
			}
		}
		
		return root;
	}

	/**
	 * 用于测试的函数，打印层序遍历
	 */
	public static String levelOrder(TreeNode root) {
		if (root == null) {
			return "#";
		}
		StringBuilder builder = new StringBuilder();
		Queue<TreeNode> queue = new LinkedList<>();
		queue.add(root);
		builder.append(root.val);
		while (!queue.isEmpty()) {
			TreeNode node = queue.poll();
			if (node.left != null) {
				builder.append("," + node.left.val);
				queue.add(node.left);
			} else {
				builder.append(",#");
			}
			if (node.right != null) {
				builder.append("," + node.right.val);
				queue.add(node.right);
			} else {
				builder.append(",#");
			}
		}
		return builder.toString();
	}

	/**
	 * 验证序列化和反序列化是否正确
	 */
	public static boolean isValidSerialization(TreeNode original) {
		String serialized = serialize(original);
		TreeNode deserialized = deserialize(serialized);
		String reserialized = serialize(deserialized);
		return serialized.equals(reserialized);
	}

	/**
	 * 生成用于测试的二叉树
	 */
	public static TreeNode generateTestTree(int type) {
		switch (type) {
			case 1: // 完全二叉树
				TreeNode root1 = new TreeNode(1);
				root1.left = new TreeNode(2);
				root1.right = new TreeNode(3);
				root1.left.left = new TreeNode(4);
				root1.left.right = new TreeNode(5);
				root1.right.left = new TreeNode(6);
				root1.right.right = new TreeNode(7);
				return root1;
			case 2: // 不平衡树
				TreeNode root2 = new TreeNode(1);
				root2.left = new TreeNode(2);
				root2.left.left = new TreeNode(3);
				root2.left.left.left = new TreeNode(4);
				return root2;
			case 3: // 只有右子树
				TreeNode root3 = new TreeNode(1);
				root3.right = new TreeNode(2);
				root3.right.right = new TreeNode(3);
				root3.right.right.right = new TreeNode(4);
				return root3;
			default: // 普通树
				TreeNode root = new TreeNode(1);
				root.left = new TreeNode(2);
				root.right = new TreeNode(3);
				root.right.left = new TreeNode(4);
				root.right.right = new TreeNode(5);
				return root;
		}
	}

	// 提交这个类
	public class Codec {
		public String serialize(TreeNode root) {
			return Code06_LevelorderSerializeAndDeserialize.serialize(root);
		}

		public TreeNode deserialize(String data) {
			return Code06_LevelorderSerializeAndDeserialize.deserialize(data);
		}
	}

	public static void main(String[] args) {
		// 测试多种树结构
		for (int i = 0; i <= 3; i++) {
			System.out.println("\n测试树类型 " + i + ":");
			TreeNode root = generateTestTree(i);
			
			// 序列化测试
			String serialized = serialize(root);
			System.out.println("常规序列化结果: " + serialized);
			
			// 优化序列化测试
			String serializedOpt = serializeOptimized(root);
			System.out.println("优化序列化结果: " + serializedOpt);
			
			// 反序列化测试
			TreeNode deserialized = deserialize(serialized);
			String check = levelOrder(deserialized);
			System.out.println("反序列化后层序遍历: " + check);
			
			// 验证序列化和反序列化的正确性
			boolean isValid = isValidSerialization(root);
			System.out.println("序列化和反序列化正确性: " + isValid);
		}
		
		// 边界情况测试
		System.out.println("\n边界情况测试:");
		// 空节点
		System.out.println("空节点序列化: " + serialize(null));
		System.out.println("空字符串反序列化: " + levelOrder(deserialize("#")));
		
		// 单节点
		TreeNode singleNode = new TreeNode(42);
		System.out.println("单节点序列化: " + serialize(singleNode));
		System.out.println("单节点反序列化: " + levelOrder(deserialize(serialize(singleNode))));
		
		// 测试优化版本的反序列化
		String optimized = serializeOptimized(generateTestTree(0));
		TreeNode optimizedDeserialized = deserializeOptimized(optimized);
		System.out.println("优化版本反序列化: " + levelOrder(optimizedDeserialized));
	}
}
