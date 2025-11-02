package class175."n叉树"; // 注意：在实际编译时需要移除引号

import java.util.*;

/**
 * N叉树（N-ary Tree）实现类
 * N叉树是一种树数据结构，其中每个节点可以有0个或多个子节点
 * 
 * 常见应用场景：
 * 1. 组织结构图
 * 2. 文件系统目录结构
 * 3. XML/HTML文档解析
 * 4. 计算机网络路由
 * 5. 游戏开发中的场景树
 * 
 * 相关算法题目：
 * - LeetCode 589. N叉树的前序遍历 https://leetcode.cn/problems/n-ary-tree-preorder-traversal/
 * - LeetCode 590. N叉树的后序遍历 https://leetcode.cn/problems/n-ary-tree-postorder-traversal/
 * - LeetCode 429. N叉树的层序遍历 https://leetcode.cn/problems/n-ary-tree-level-order-traversal/
 * - LeetCode 559. N叉树的最大深度 https://leetcode.cn/problems/maximum-depth-of-n-ary-tree/
 * - LeetCode 1104. 二叉树寻路 https://leetcode.cn/problems/path-in-zigzag-labelled-binary-tree/
 * - LeetCode 1490. 克隆N叉树 https://leetcode.cn/problems/clone-n-ary-tree/
 * - LintCode 1522. N叉树的直径 https://www.lintcode.com/problem/1522/
 * - HackerRank N-ary Tree Level Order Traversal https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
 * - 洛谷 P5598 【XR-4】文本编辑器 https://www.luogu.com.cn/problem/P5598
 */
class NaryTreeNode {
    public int val;
    public List<NaryTreeNode> children;

    /**
     * 构造函数
     * @param val 节点值
     */
    public NaryTreeNode(int val) {
        this.val = val;
        this.children = new ArrayList<>();
    }

    /**
     * 添加子节点
     * @param child 子节点
     */
    public void addChild(NaryTreeNode child) {
        this.children.add(child);
    }
}

public class NaryTree {
    /**
     * 前序遍历：根节点 -> 子节点（从左到右）
     * @param root N叉树的根节点
     * @return 前序遍历的结果列表
     */
    public List<Integer> preorderTraversal(NaryTreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        preorderHelper(root, result);
        return result;
    }

    private void preorderHelper(NaryTreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        // 先访问根节点
        result.add(node.val);
        // 再递归访问所有子节点
        for (NaryTreeNode child : node.children) {
            preorderHelper(child, result);
        }
    }

    /**
     * 前序遍历的非递归实现
     * @param root N叉树的根节点
     * @return 前序遍历的结果列表
     */
    public List<Integer> preorderTraversalIterative(NaryTreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Stack<NaryTreeNode> stack = new Stack<>();
        stack.push(root);

        while (!stack.isEmpty()) {
            NaryTreeNode node = stack.pop();
            result.add(node.val);
            // 注意：这里需要逆序压入子节点，以保证出栈顺序是从左到右
            for (int i = node.children.size() - 1; i >= 0; i--) {
                stack.push(node.children.get(i));
            }
        }

        return result;
    }

    /**
     * 后序遍历：子节点（从左到右）-> 根节点
     * @param root N叉树的根节点
     * @return 后序遍历的结果列表
     */
    public List<Integer> postorderTraversal(NaryTreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) {
            return result;
        }
        postorderHelper(root, result);
        return result;
    }

    private void postorderHelper(NaryTreeNode node, List<Integer> result) {
        if (node == null) {
            return;
        }
        // 先递归访问所有子节点
        for (NaryTreeNode child : node.children) {
            postorderHelper(child, result);
        }
        // 再访问根节点
        result.add(node.val);
    }

    /**
     * 层序遍历（广度优先遍历）
     * @param root N叉树的根节点
     * @return 层序遍历的结果列表，每个子列表代表一层
     */
    public List<List<Integer>> levelOrderTraversal(NaryTreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<NaryTreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                NaryTreeNode node = queue.poll();
                currentLevel.add(node.val);
                // 将所有子节点加入队列
                for (NaryTreeNode child : node.children) {
                    queue.offer(child);
                }
            }

            result.add(currentLevel);
        }

        return result;
    }

    /**
     * 计算N叉树的最大深度
     * @param root N叉树的根节点
     * @return 最大深度
     */
    public int maxDepth(NaryTreeNode root) {
        if (root == null) {
            return 0;
        }
        int maxChildDepth = 0;
        for (NaryTreeNode child : root.children) {
            maxChildDepth = Math.max(maxChildDepth, maxDepth(child));
        }
        return maxChildDepth + 1;
    }

    /**
     * 计算N叉树的节点总数
     * @param root N叉树的根节点
     * @return 节点总数
     */
    public int countNodes(NaryTreeNode root) {
        if (root == null) {
            return 0;
        }
        int count = 1; // 当前节点
        for (NaryTreeNode child : root.children) {
            count += countNodes(child);
        }
        return count;
    }

    /**
     * 克隆一棵N叉树
     * @param root 原N叉树的根节点
     * @return 克隆后的N叉树的根节点
     */
    public NaryTreeNode cloneTree(NaryTreeNode root) {
        if (root == null) {
            return null;
        }
        
        NaryTreeNode clonedRoot = new NaryTreeNode(root.val);
        for (NaryTreeNode child : root.children) {
            clonedRoot.addChild(cloneTree(child));
        }
        
        return clonedRoot;
    }

    /**
     * 查找值为target的节点
     * @param root N叉树的根节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回null
     */
    public NaryTreeNode findNode(NaryTreeNode root, int target) {
        if (root == null) {
            return null;
        }
        if (root.val == target) {
            return root;
        }
        for (NaryTreeNode child : root.children) {
            NaryTreeNode found = findNode(child, target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 打印N叉树的结构
     * @param root N叉树的根节点
     */
    public void printTree(NaryTreeNode root) {
        printTreeHelper(root, 0);
    }

    private void printTreeHelper(NaryTreeNode node, int level) {
        if (node == null) {
            return;
        }
        // 打印缩进
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.val);
        // 递归打印子节点
        for (NaryTreeNode child : node.children) {
            printTreeHelper(child, level + 1);
        }
    }

    /**
     * 从父节点数组构建N叉树
     * @param parent 父节点数组，parent[i]表示节点i的父节点
     * @return 构建的N叉树的根节点
     */
    public NaryTreeNode buildTreeFromParentArray(int[] parent) {
        if (parent == null || parent.length == 0) {
            return null;
        }

        Map<Integer, NaryTreeNode> nodes = new HashMap<>();
        NaryTreeNode root = null;

        for (int i = 0; i < parent.length; i++) {
            // 创建当前节点
            nodes.putIfAbsent(i, new NaryTreeNode(i));
            NaryTreeNode current = nodes.get(i);

            if (parent[i] == -1) {
                // 根节点
                root = current;
            } else {
                // 创建父节点（如果不存在）
                nodes.putIfAbsent(parent[i], new NaryTreeNode(parent[i]));
                NaryTreeNode parentNode = nodes.get(parent[i]);
                // 将当前节点添加为父节点的子节点
                parentNode.addChild(current);
            }
        }

        return root;
    }

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 创建N叉树示例
        //       1
        //     / | \
        //    2  3  4
        //   / \   / \
        //  5   6 7   8
        
        NaryTreeNode root = new NaryTreeNode(1);
        NaryTreeNode node2 = new NaryTreeNode(2);
        NaryTreeNode node3 = new NaryTreeNode(3);
        NaryTreeNode node4 = new NaryTreeNode(4);
        NaryTreeNode node5 = new NaryTreeNode(5);
        NaryTreeNode node6 = new NaryTreeNode(6);
        NaryTreeNode node7 = new NaryTreeNode(7);
        NaryTreeNode node8 = new NaryTreeNode(8);

        root.addChild(node2);
        root.addChild(node3);
        root.addChild(node4);
        node2.addChild(node5);
        node2.addChild(node6);
        node4.addChild(node7);
        node4.addChild(node8);

        NaryTree tree = new NaryTree();

        // 测试前序遍历
        System.out.println("前序遍历（递归）:");
        List<Integer> preorder = tree.preorderTraversal(root);
        System.out.println(preorder);

        System.out.println("前序遍历（非递归）:");
        List<Integer> preorderIter = tree.preorderTraversalIterative(root);
        System.out.println(preorderIter);

        // 测试后序遍历
        System.out.println("后序遍历:");
        List<Integer> postorder = tree.postorderTraversal(root);
        System.out.println(postorder);

        // 测试层序遍历
        System.out.println("层序遍历:");
        List<List<Integer>> levelOrder = tree.levelOrderTraversal(root);
        for (List<Integer> level : levelOrder) {
            System.out.println(level);
        }

        // 测试最大深度
        System.out.println("最大深度: " + tree.maxDepth(root));

        // 测试节点总数
        System.out.println("节点总数: " + tree.countNodes(root));

        // 测试克隆树
        NaryTreeNode cloned = tree.cloneTree(root);
        System.out.println("克隆树前序遍历:");
        List<Integer> clonedPreorder = tree.preorderTraversal(cloned);
        System.out.println(clonedPreorder);

        // 测试查找节点
        NaryTreeNode found = tree.findNode(root, 6);
        System.out.println("查找节点6: " + (found != null ? "找到" : "未找到"));

        // 测试打印树
        System.out.println("树的结构:");
        tree.printTree(root);

        // 测试从父节点数组构建树
        int[] parentArray = {-1, 0, 0, 0, 1, 1, 3, 3};
        NaryTreeNode builtTree = tree.buildTreeFromParentArray(parentArray);
        System.out.println("从父节点数组构建的树结构:");
        tree.printTree(builtTree);
    }
}