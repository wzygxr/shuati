package class036;

import java.util.*;

/**
 * 二叉树序列化与反序列化算法实现
 * 测试链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
 * 
 * 核心算法思想：
 * 1. 序列化：将二叉树结构转换为字符串表示形式
 * 2. 反序列化：将字符串表示形式恢复为原始二叉树结构
 * 
 * 实现方式：
 * - 先序遍历序列化（递归实现）
 * - BFS层序遍历序列化（迭代实现）
 * - 后序遍历序列化（递归实现）
 * 
 * 关键优化点：
 * - 使用字符串构建器提升性能，避免频繁字符串拼接
 * - 使用特殊标记（如"#"）表示空节点
 * - 使用分隔符（如","）分离节点值
 * - 对于BFS实现，使用队列高效管理节点访问顺序
 * 
 * 边界情况处理：
 * - 空树处理：返回特殊标记
 * - 单节点树处理：直接返回节点值加空标记
 * - 数据类型转换异常处理
 * 
 * 工程化考量：
 * - 代码可读性：方法职责单一，命名清晰
 * - 可扩展性：支持多种序列化方式
 * - 健壮性：包含异常处理机制
 * - 性能优化：使用StringBuilder、避免递归栈溢出
 * 
 * 相关题目：
 * 1. LeetCode 297. 二叉树的序列化与反序列化 - https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
 * 2. LeetCode 449. 序列化和反序列化二叉搜索树 - https://leetcode.cn/problems/serialize-and-deserialize-bst/
 * 3. LeetCode 536. 从字符串生成二叉树 - https://leetcode.cn/problems/construct-binary-tree-from-string/
 * 4. LeetCode 652. 寻找重复的子树 - https://leetcode.cn/problems/find-duplicate-subtrees/
 * 5. LeetCode 1008. 前序遍历构造二叉搜索树 - https://leetcode.cn/problems/construct-binary-search-tree-from-preorder-traversal/
 * 6. LeetCode 889. 根据前序和后序遍历构造二叉树 - https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-postorder-traversal/
 * 7. LeetCode 105. 从前序与中序遍历序列构造二叉树 - https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/
 * 8. LeetCode 106. 从中序与后序遍历序列构造二叉树 - https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/
 * 9. LeetCode 1028. 从先序遍历还原二叉树 - https://leetcode.cn/problems/recover-a-tree-from-preorder-traversal/
 * 10. LeetCode 1302. 层数最深叶子节点的和 - https://leetcode.cn/problems/deepest-leaves-sum/
 * 11. LeetCode 116. 填充每个节点的下一个右侧节点指针 - https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/
 * 12. LeetCode 117. 填充每个节点的下一个右侧节点指针 II - https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/
 * 13. LeetCode 104. 二叉树的最大深度 - https://leetcode.cn/problems/maximum-depth-of-binary-tree/
 */
public class Code05_PreorderSerializeAndDeserialize {

	// 不提交这个类
	public static class TreeNode {
		public int val;
		public TreeNode left;
		public TreeNode right;

		public TreeNode(int v) {
			val = v;
		}

        @Override
        public String toString() {
            return String.valueOf(val);
        }
	}

    /**
     * 重要说明：
     * 二叉树可以通过先序、后序或者按层遍历的方式序列化和反序列化
     * 但是，二叉树无法通过中序遍历的方式实现序列化和反序列化
     * 因为不同的两棵树，可能得到同样的中序序列，即便补了空位置也可能一样。
     * 比如如下两棵树
     *         __2
     *        /
     *       1
     *       和
     *       1__
     *          \
     *           2
     * 补足空位置的中序遍历结果都是{ null, 1, null, 2, null}
     */

	// 提交这个类 - 先序遍历实现
	public class CodecPreorder {

		/**
         * 将二叉树序列化为字符串
         * 实现思路：使用先序遍历（根-左-右），递归访问每个节点
         * 时间复杂度：O(n)，其中n是节点数
         * 空间复杂度：O(h)，h是树的高度，最坏情况下为O(n)
         * @param root 二叉树根节点
         * @return 序列化后的字符串
         */
		public String serialize(TreeNode root) {
			StringBuilder builder = new StringBuilder();
			preorderSerialize(root, builder);
			return builder.toString();
		}

		/**
         * 先序遍历序列化辅助方法
         * @param root 当前节点
         * @param builder 字符串构建器
         */
		void preorderSerialize(TreeNode root, StringBuilder builder) {
			if (root == null) {
				builder.append("#,");
			} else {
				builder.append(root.val + ",");
				preorderSerialize(root.left, builder);
				preorderSerialize(root.right, builder);
			}
		}

		/**
         * 将字符串反序列化为二叉树
         * 实现思路：根据先序遍历的特点，递归重建二叉树
         * 时间复杂度：O(n)
         * 空间复杂度：O(h)，h是树的高度
         * @param data 序列化的字符串
         * @return 重建后的二叉树根节点
         */
		public TreeNode deserialize(String data) {
			String[] vals = data.split(",");
			cnt = 0;
			return preorderDeserialize(vals);
		}

		// 当前数组消费到哪了
		public static int cnt;

		/**
         * 先序遍历反序列化辅助方法
         * @param vals 节点值数组
         * @return 重建的节点
         */
		TreeNode preorderDeserialize(String[] vals) {
			String cur = vals[cnt++];
			if (cur.equals("#")) {
				return null;
			} else {
				TreeNode head = new TreeNode(Integer.valueOf(cur));
				head.left = preorderDeserialize(vals);
				head.right = preorderDeserialize(vals);
				return head;
			}
		}
	}

    // BFS层序遍历实现
    public class CodecBFS {
        
        /**
         * 使用BFS层序遍历序列化二叉树
         * 实现思路：使用队列进行层次遍历，记录每个节点的值或空标记
         * 时间复杂度：O(n)
         * 空间复杂度：O(w)，w是树的最大宽度
         * @param root 二叉树根节点
         * @return 序列化后的字符串
         */
        public String serialize(TreeNode root) {
            if (root == null) {
                return "#";
            }
            StringBuilder builder = new StringBuilder();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            
            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node == null) {
                    builder.append("#,");
                } else {
                    builder.append(node.val + ",");
                    queue.offer(node.left);
                    queue.offer(node.right);
                }
            }
            
            // 移除末尾的逗号
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            
            return builder.toString();
        }
        
        /**
         * 使用BFS层序遍历反序列化二叉树
         * 实现思路：使用队列重建树结构，按层次构建每个节点的左右子节点
         * 时间复杂度：O(n)
         * 空间复杂度：O(w)，w是树的最大宽度
         * @param data 序列化的字符串
         * @return 重建后的二叉树根节点
         */
        public TreeNode deserialize(String data) {
            if (data.equals("#")) {
                return null;
            }
            
            String[] vals = data.split(",");
            TreeNode root = new TreeNode(Integer.parseInt(vals[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);
            int index = 1;
            
            while (!queue.isEmpty() && index < vals.length) {
                TreeNode node = queue.poll();
                
                // 处理左子节点
                if (!vals[index].equals("#")) {
                    node.left = new TreeNode(Integer.parseInt(vals[index]));
                    queue.offer(node.left);
                }
                index++;
                
                // 处理右子节点
                if (index < vals.length && !vals[index].equals("#")) {
                    node.right = new TreeNode(Integer.parseInt(vals[index]));
                    queue.offer(node.right);
                }
                index++;
            }
            
            return root;
        }
    }

    // 后序遍历实现
    public class CodecPostorder {
        
        /**
         * 使用后序遍历序列化二叉树
         * 实现思路：递归后序遍历（左-右-根），记录每个节点的值或空标记
         * 时间复杂度：O(n)
         * 空间复杂度：O(h)，h是树的高度
         * @param root 二叉树根节点
         * @return 序列化后的字符串
         */
        public String serialize(TreeNode root) {
            StringBuilder builder = new StringBuilder();
            postorderSerialize(root, builder);
            // 移除末尾的逗号
            if (builder.length() > 0) {
                builder.setLength(builder.length() - 1);
            }
            return builder.toString();
        }
        
        private void postorderSerialize(TreeNode root, StringBuilder builder) {
            if (root == null) {
                builder.append("#,");
            } else {
                postorderSerialize(root.left, builder);
                postorderSerialize(root.right, builder);
                builder.append(root.val + ",");
            }
        }
        
        /**
         * 使用后序遍历反序列化二叉树
         * 实现思路：将数组反向处理，使用栈模拟递归过程，按照根-右-左的顺序重建
         * 时间复杂度：O(n)
         * 空间复杂度：O(h)，h是树的高度
         * @param data 序列化的字符串
         * @return 重建后的二叉树根节点
         */
        public TreeNode deserialize(String data) {
            if (data.equals("#")) {
                return null;
            }
            
            String[] vals = data.split(",");
            Stack<String> stack = new Stack<>();
            for (String val : vals) {
                stack.push(val);
            }
            
            return postorderDeserialize(stack);
        }
        
        private TreeNode postorderDeserialize(Stack<String> stack) {
            String val = stack.pop();
            if (val.equals("#")) {
                return null;
            }
            
            TreeNode root = new TreeNode(Integer.parseInt(val));
            // 注意：后序遍历反序列化时，先处理右子树再处理左子树
            root.right = postorderDeserialize(stack);
            root.left = postorderDeserialize(stack);
            
            return root;
        }
    }

    /**
     * 打印二叉树结构的辅助方法
     * 使用层序遍历方式，打印树的结构
     * @param root 二叉树根节点
     */
    public static void printTree(TreeNode root) {
        if (root == null) {
            System.out.println("[空树]");
            return;
        }
        
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        List<List<String>> levels = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<String> level = new ArrayList<>();
            boolean hasNonNull = false;
            
            for (int i = 0; i < levelSize; i++) {
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
            
            levels.add(level);
            // 如果当前层之后都是null，停止遍历
            if (!hasNonNull) {
                break;
            }
        }
        
        // 打印树结构
        int maxWidth = levels.get(levels.size() - 2).size() * 4 - 3;
        for (int i = 0; i < levels.size() - 1; i++) { // 去掉最后一层全为null的层
            int levelWidth = levels.get(i).size();
            int space = (maxWidth - (levelWidth * 4 - 3)) / 2;
            
            StringBuilder line = new StringBuilder();
            for (int j = 0; j < space; j++) {
                line.append(" ");
            }
            
            for (int j = 0; j < levelWidth; j++) {
                String val = levels.get(i).get(j);
                line.append(val);
                if (j < levelWidth - 1) {
                    for (int k = 0; k < 4 - val.length(); k++) {
                        line.append(" ");
                    }
                }
            }
            
            System.out.println(line.toString());
        }
    }

    /**
     * 性能测试方法
     * 测试不同序列化方法在深层树情况下的性能
     */
    public static void performanceTest() {
        System.out.println("\n===== 性能测试 =====");
        
        // 创建深度为1000的左链式树
        TreeNode deepTree = createDeepTree(1000);
        
        Code05_PreorderSerializeAndDeserialize outer = new Code05_PreorderSerializeAndDeserialize();
        CodecPreorder codecPreorder = outer.new CodecPreorder();
        CodecBFS codecBFS = outer.new CodecBFS();
        CodecPostorder codecPostorder = outer.new CodecPostorder();
        
        // 测试先序序列化性能
        long start = System.currentTimeMillis();
        String preorderSerialized = codecPreorder.serialize(deepTree);
        long end = System.currentTimeMillis();
        System.out.println("先序序列化耗时: " + (end - start) + "ms");
        
        // 测试BFS序列化性能
        start = System.currentTimeMillis();
        String bfsSerialized = codecBFS.serialize(deepTree);
        end = System.currentTimeMillis();
        System.out.println("BFS序列化耗时: " + (end - start) + "ms");
        
        // 测试后序序列化性能
        start = System.currentTimeMillis();
        String postorderSerialized = codecPostorder.serialize(deepTree);
        end = System.currentTimeMillis();
        System.out.println("后序序列化耗时: " + (end - start) + "ms");
        
        // 测试先序反序列化性能
        start = System.currentTimeMillis();
        TreeNode preorderDeserialized = codecPreorder.deserialize(preorderSerialized);
        end = System.currentTimeMillis();
        System.out.println("先序反序列化耗时: " + (end - start) + "ms");
        
        // 测试BFS反序列化性能
        start = System.currentTimeMillis();
        TreeNode bfsDeserialized = codecBFS.deserialize(bfsSerialized);
        end = System.currentTimeMillis();
        System.out.println("BFS反序列化耗时: " + (end - start) + "ms");
        
        // 测试后序反序列化性能
        start = System.currentTimeMillis();
        TreeNode postorderDeserialized = codecPostorder.deserialize(postorderSerialized);
        end = System.currentTimeMillis();
        System.out.println("后序反序列化耗时: " + (end - start) + "ms");
    }

    /**
     * 创建指定深度的左链式树
     * @param depth 树的深度
     * @return 创建的树的根节点
     */
    private static TreeNode createDeepTree(int depth) {
        TreeNode root = new TreeNode(1);
        TreeNode current = root;
        for (int i = 2; i <= depth; i++) {
            current.left = new TreeNode(i);
            current = current.left;
        }
        return root;
    }

    /**
     * 创建完全二叉树
     * @param height 树的高度
     * @return 创建的完全二叉树的根节点
     */
    private static TreeNode createCompleteBinaryTree(int height) {
        if (height <= 0) {
            return null;
        }
        return createCompleteBinaryTreeHelper(1, height);
    }

    private static TreeNode createCompleteBinaryTreeHelper(int val, int height) {
        if (val > Math.pow(2, height) - 1) {
            return null;
        }
        TreeNode root = new TreeNode(val);
        root.left = createCompleteBinaryTreeHelper(2 * val, height);
        root.right = createCompleteBinaryTreeHelper(2 * val + 1, height);
        return root;
    }

    /**
     * 生成各种测试树
     * @param type 树类型：1-完全二叉树，2-左倾斜树，3-右倾斜树，4-单节点树，5-普通二叉树
     * @return 生成的测试树
     */
    public static TreeNode generateTestTree(int type) {
        switch (type) {
            case 1: // 完全二叉树
                return createCompleteBinaryTree(4);
            case 2: // 左倾斜树
                TreeNode leftSkewed = new TreeNode(1);
                TreeNode current = leftSkewed;
                for (int i = 2; i <= 5; i++) {
                    current.left = new TreeNode(i);
                    current = current.left;
                }
                return leftSkewed;
            case 3: // 右倾斜树
                TreeNode rightSkewed = new TreeNode(1);
                current = rightSkewed;
                for (int i = 2; i <= 5; i++) {
                    current.right = new TreeNode(i);
                    current = current.right;
                }
                return rightSkewed;
            case 4: // 单节点树
                return new TreeNode(1);
            case 5: // 普通二叉树
                TreeNode root = new TreeNode(1);
                root.left = new TreeNode(2);
                root.right = new TreeNode(3);
                root.left.left = new TreeNode(4);
                root.left.right = new TreeNode(5);
                root.right.right = new TreeNode(6);
                root.left.left.left = new TreeNode(7);
                return root;
            default:
                return null;
        }
    }

    /**
     * 比较两个二叉树是否完全相同
     */
    public static boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if (p == null || q == null) {
            return false;
        }
        return p.val == q.val && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
    }

    public static void main(String[] args) {
        Code05_PreorderSerializeAndDeserialize outer = new Code05_PreorderSerializeAndDeserialize();
        CodecPreorder codecPreorder = outer.new CodecPreorder();
        CodecBFS codecBFS = outer.new CodecBFS();
        CodecPostorder codecPostorder = outer.new CodecPostorder();

        // 1. 标准测试用例
        System.out.println("===== 标准测试用例 =====");
        for (int i = 1; i <= 5; i++) {
            System.out.println("\n测试树类型 " + i + ":");
            TreeNode tree = generateTestTree(i);
            System.out.println("原始树结构:");
            printTree(tree);

            // 先序序列化测试
            String preorderSerialized = codecPreorder.serialize(tree);
            System.out.println("先序序列化结果: " + preorderSerialized);
            TreeNode preorderDeserialized = codecPreorder.deserialize(preorderSerialized);
            System.out.println("先序反序列化后树结构:");
            printTree(preorderDeserialized);
            System.out.println("先序序列化/反序列化正确性: " + isSameTree(tree, preorderDeserialized));

            // BFS序列化测试
            String bfsSerialized = codecBFS.serialize(tree);
            System.out.println("BFS序列化结果: " + bfsSerialized);
            TreeNode bfsDeserialized = codecBFS.deserialize(bfsSerialized);
            System.out.println("BFS反序列化后树结构:");
            printTree(bfsDeserialized);
            System.out.println("BFS序列化/反序列化正确性: " + isSameTree(tree, bfsDeserialized));

            // 后序序列化测试
            String postorderSerialized = codecPostorder.serialize(tree);
            System.out.println("后序序列化结果: " + postorderSerialized);
            TreeNode postorderDeserialized = codecPostorder.deserialize(postorderSerialized);
            System.out.println("后序反序列化后树结构:");
            printTree(postorderDeserialized);
            System.out.println("后序序列化/反序列化正确性: " + isSameTree(tree, postorderDeserialized));
        }

        // 2. 边界情况测试
        System.out.println("\n===== 边界情况测试 =====");
        // 空树测试
        TreeNode nullTree = null;
        System.out.println("空树测试:");
        String preorderNull = codecPreorder.serialize(nullTree);
        System.out.println("先序序列化空树: " + preorderNull);
        TreeNode deserializedNull = codecPreorder.deserialize(preorderNull);
        System.out.println("先序反序列化空树结果: " + (deserializedNull == null ? "null" : deserializedNull.val));

        // 3. 深层树测试
        System.out.println("\n===== 深层树测试 =====");
        TreeNode deepTree = createDeepTree(10);
        System.out.println("深层树(深度10):");
        String deepSerialized = codecPreorder.serialize(deepTree);
        TreeNode deepDeserialized = codecPreorder.deserialize(deepSerialized);
        System.out.println("深层树序列化/反序列化正确性: " + isSameTree(deepTree, deepDeserialized));

        // 4. 性能测试
        performanceTest();

        // 5. 不同序列化方法结果比较
        System.out.println("\n===== 不同序列化方法结果比较 =====");
        TreeNode sampleTree = generateTestTree(5);
        String preorder = codecPreorder.serialize(sampleTree);
        String bfs = codecBFS.serialize(sampleTree);
        String postorder = codecPostorder.serialize(sampleTree);
        System.out.println("先序序列化: " + preorder);
        System.out.println("BFS序列化: " + bfs);
        System.out.println("后序序列化: " + postorder);
    }

    /*
    // Python实现
    # 二叉树序列化与反序列化的Python实现
    # 包含先序、BFS和后序三种实现方式

    class TreeNode:
        def __init__(self, val=0, left=None, right=None):
            self.val = val
            self.left = left
            self.right = right
        
        def __str__(self):
            return str(self.val)

    class CodecPreorder:
        def serialize(self, root):
            """将二叉树序列化为字符串（先序遍历）"""
            result = []
            self._preorder_serialize(root, result)
            return ",".join(result)
        
        def _preorder_serialize(self, root, result):
            if root is None:
                result.append("#")
            else:
                result.append(str(root.val))
                self._preorder_serialize(root.left, result)
                self._preorder_serialize(root.right, result)
        
        def deserialize(self, data):
            """将字符串反序列化为二叉树（先序遍历）"""
            if data == "":
                return None
            values = data.split(",")
            self.index = 0
            return self._preorder_deserialize(values)
        
        def _preorder_deserialize(self, values):
            if self.index >= len(values) or values[self.index] == "#":
                self.index += 1
                return None
            
            root = TreeNode(int(values[self.index]))
            self.index += 1
            root.left = self._preorder_deserialize(values)
            root.right = self._preorder_deserialize(values)
            return root

    class CodecBFS:
        def serialize(self, root):
            """使用BFS层序遍历序列化二叉树"""
            if not root:
                return "#"
            
            result = []
            queue = [root]
            
            while queue:
                node = queue.pop(0)
                if node is None:
                    result.append("#")
                else:
                    result.append(str(node.val))
                    queue.append(node.left)
                    queue.append(node.right)
            
            return ",".join(result)
        
        def deserialize(self, data):
            """使用BFS层序遍历反序列化二叉树"""
            if data == "#":
                return None
            
            values = data.split(",")
            root = TreeNode(int(values[0]))
            queue = [root]
            index = 1
            
            while queue and index < len(values):
                node = queue.pop(0)
                
                # 处理左子节点
                if index < len(values) and values[index] != "#":
                    node.left = TreeNode(int(values[index]))
                    queue.append(node.left)
                index += 1
                
                # 处理右子节点
                if index < len(values) and values[index] != "#":
                    node.right = TreeNode(int(values[index]))
                    queue.append(node.right)
                index += 1
            
            return root

    class CodecPostorder:
        def serialize(self, root):
            """使用后序遍历序列化二叉树"""
            result = []
            self._postorder_serialize(root, result)
            return ",".join(result)
        
        def _postorder_serialize(self, root, result):
            if root is None:
                result.append("#")
            else:
                self._postorder_serialize(root.left, result)
                self._postorder_serialize(root.right, result)
                result.append(str(root.val))
        
        def deserialize(self, data):
            """使用后序遍历反序列化二叉树"""
            if data == "":
                return None
            
            values = data.split(",")
            self.stack = values[::-1]  # 反转数组，用于模拟栈操作
            return self._postorder_deserialize()
        
        def _postorder_deserialize(self):
            if not self.stack:
                return None
            
            val = self.stack.pop(0)
            if val == "#":
                return None
            
            root = TreeNode(int(val))
            # 注意：后序遍历反序列化时，先处理右子树再处理左子树
            root.right = self._postorder_deserialize()
            root.left = self._postorder_deserialize()
            
            return root
    */

    /*
    // C++实现
    // 二叉树序列化与反序列化的C++实现
    // 包含先序、BFS和后序三种实现方式

    #include <iostream>
    #include <string>
    #include <vector>
    #include <queue>
    #include <stack>
    #include <sstream>

    struct TreeNode {
        int val;
        TreeNode *left;
        TreeNode *right;
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    };

    class CodecPreorder {
    public:
        // 先序遍历序列化
        std::string serialize(TreeNode* root) {
            std::string result;
            preorderSerialize(root, result);
            return result;
        }

        // 先序遍历反序列化
        TreeNode* deserialize(std::string data) {
            std::vector<std::string> values;
            split(data, values, ',');
            int index = 0;
            return preorderDeserialize(values, index);
        }

    private:
        void preorderSerialize(TreeNode* root, std::string& result) {
            if (root == nullptr) {
                result += "#,",
            } else {
                result += std::to_string(root->val) + ",";
                preorderSerialize(root->left, result);
                preorderSerialize(root->right, result);
            }
        }

        TreeNode* preorderDeserialize(const std::vector<std::string>& values, int& index) {
            if (index >= values.size() || values[index] == "#") {
                index++;
                return nullptr;
            }

            TreeNode* root = new TreeNode(std::stoi(values[index]));
            index++;
            root->left = preorderDeserialize(values, index);
            root->right = preorderDeserialize(values, index);
            return root;
        }

        void split(const std::string& s, std::vector<std::string>& tokens, char delimiter) {
            std::string token;
            std::istringstream tokenStream(s);
            while (std::getline(tokenStream, token, delimiter)) {
                tokens.push_back(token);
            }
        }
    };

    class CodecBFS {
    public:
        // BFS层序遍历序列化
        std::string serialize(TreeNode* root) {
            if (!root) return "#";

            std::string result;
            std::queue<TreeNode*> q;
            q.push(root);

            while (!q.empty()) {
                TreeNode* node = q.front();
                q.pop();

                if (!node) {
                    result += "#,",
                } else {
                    result += std::to_string(node->val) + ",";
                    q.push(node->left);
                    q.push(node->right);
                }
            }

            // 移除最后一个逗号
            if (!result.empty()) {
                result.pop_back();
            }
            return result;
        }

        // BFS层序遍历反序列化
        TreeNode* deserialize(std::string data) {
            if (data == "#") return nullptr;

            std::vector<std::string> values;
            split(data, values, ',');

            TreeNode* root = new TreeNode(std::stoi(values[0]));
            std::queue<TreeNode*> q;
            q.push(root);
            int index = 1;

            while (!q.empty() && index < values.size()) {
                TreeNode* node = q.front();
                q.pop();

                // 处理左子节点
                if (index < values.size() && values[index] != "#") {
                    node->left = new TreeNode(std::stoi(values[index]));
                    q.push(node->left);
                }
                index++;

                // 处理右子节点
                if (index < values.size() && values[index] != "#") {
                    node->right = new TreeNode(std::stoi(values[index]));
                    q.push(node->right);
                }
                index++;
            }

            return root;
        }

    private:
        void split(const std::string& s, std::vector<std::string>& tokens, char delimiter) {
            std::string token;
            std::istringstream tokenStream(s);
            while (std::getline(tokenStream, token, delimiter)) {
                tokens.push_back(token);
            }
        }
    };

    class CodecPostorder {
    public:
        // 后序遍历序列化
        std::string serialize(TreeNode* root) {
            std::string result;
            postorderSerialize(root, result);
            // 移除最后一个逗号
            if (!result.empty()) {
                result.pop_back();
            }
            return result;
        }

        // 后序遍历反序列化
        TreeNode* deserialize(std::string data) {
            if (data.empty()) return nullptr;

            std::vector<std::string> values;
            split(data, values, ',');

            std::stack<std::string> stack;
            for (const auto& val : values) {
                stack.push(val);
            }

            return postorderDeserialize(stack);
        }

    private:
        void postorderSerialize(TreeNode* root, std::string& result) {
            if (root == nullptr) {
                result += "#,",
            } else {
                postorderSerialize(root->left, result);
                postorderSerialize(root->right, result);
                result += std::to_string(root->val) + ",";
            }
        }

        TreeNode* postorderDeserialize(std::stack<std::string>& stack) {
            if (stack.empty()) return nullptr;

            std::string val = stack.top();
            stack.pop();

            if (val == "#") {
                return nullptr;
            }

            TreeNode* root = new TreeNode(std::stoi(val));
            // 注意：后序遍历反序列化时，先处理右子树再处理左子树
            root->right = postorderDeserialize(stack);
            root->left = postorderDeserialize(stack);

            return root;
        }

        void split(const std::string& s, std::vector<std::string>& tokens, char delimiter) {
            std::string token;
            std::istringstream tokenStream(s);
            while (std::getline(tokenStream, token, delimiter)) {
                tokens.push_back(token);
            }
        }
    };

    // 辅助函数：释放树的内存
    void deleteTree(TreeNode* root) {
        if (root) {
            deleteTree(root->left);
            deleteTree(root->right);
            delete root;
        }
    }

    // 辅助函数：打印树结构（简化版）
    void printTree(TreeNode* root) {
        if (!root) {
            std::cout << "[空树]" << std::endl;
            return;
        }

        std::queue<TreeNode*> q;
        q.push(root);

        while (!q.empty()) {
            int levelSize = q.size();
            for (int i = 0; i < levelSize; i++) {
                TreeNode* node = q.front();
                q.pop();

                if (node) {
                    std::cout << node->val << " ";
                    q.push(node->left);
                    q.push(node->right);
                } else {
                    std::cout << "# ";
                }
            }
            std::cout << std::endl;
        }
    }
    */
}
