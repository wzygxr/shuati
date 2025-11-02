package class175.森林;

import java.util.*;

/**
 * 森林（Forest）实现类
 * 森林是由多棵不相交的树组成的数据结构
 * 
 * 常见应用场景：
 * 1. 多棵独立树的集合管理
 * 2. 并查集（Union-Find）的基础
 * 3. 数据库中的多树索引
 * 4. 社交网络中的多个独立社区
 * 5. 并行计算中的任务调度树
 * 
 * 相关算法题目：
 * - LeetCode 684. 冗余连接 https://leetcode.cn/problems/redundant-connection/
 * - LeetCode 685. 冗余连接 II https://leetcode.cn/problems/redundant-connection-ii/
 * - LeetCode 1258. 近义词句子 https://leetcode.cn/problems/synonymous-sentences/
 * - LeetCode 959. 由斜杠划分区域 https://leetcode.cn/problems/regions-cut-by-slashes/
 * - LintCode 1179. 连通分量 https://www.lintcode.com/problem/1179/
 * - 洛谷 P3367 【模板】并查集 https://www.luogu.com.cn/problem/P3367
 * - 牛客 NC233 合并二叉树 https://www.nowcoder.com/practice/a5e8156e81224147bd749c560909299a
 */

class TreeNode {
    public int val;
    public List<TreeNode> children;

    /**
     * 构造函数
     * @param val 节点值
     */
    public TreeNode(int val) {
        this.val = val;
        this.children = new ArrayList<>();
    }

    /**
     * 添加子节点
     * @param child 子节点
     */
    public void addChild(TreeNode child) {
        this.children.add(child);
    }
}

public class Forest {
    private List<TreeNode> trees; // 森林中的所有树

    /**
     * 构造函数
     */
    public Forest() {
        this.trees = new ArrayList<>();
    }

    /**
     * 添加一棵树到森林
     * @param root 树的根节点
     */
    public void addTree(TreeNode root) {
        if (root != null) {
            trees.add(root);
        }
    }

    /**
     * 计算森林中树的数量
     * @return 树的数量
     */
    public int getTreeCount() {
        return trees.size();
    }

    /**
     * 获取森林中的所有树
     * @return 树的列表
     */
    public List<TreeNode> getTrees() {
        return new ArrayList<>(trees);
    }

    /**
     * 计算森林中节点的总数
     * @return 节点总数
     */
    public int getTotalNodeCount() {
        int count = 0;
        for (TreeNode root : trees) {
            count += countNodes(root);
        }
        return count;
    }

    /**
     * 计算单棵树的节点数量（递归辅助函数）
     * @param root 树的根节点
     * @return 节点数量
     */
    private int countNodes(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int count = 1; // 当前节点
        for (TreeNode child : root.children) {
            count += countNodes(child);
        }
        return count;
    }

    /**
     * 计算森林中所有树的高度之和
     * @return 高度之和
     */
    public int getTotalHeight() {
        int totalHeight = 0;
        for (TreeNode root : trees) {
            totalHeight += getTreeHeight(root);
        }
        return totalHeight;
    }

    /**
     * 计算单棵树的高度（递归辅助函数）
     * @param root 树的根节点
     * @return 树的高度
     */
    private int getTreeHeight(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int maxChildHeight = 0;
        for (TreeNode child : root.children) {
            maxChildHeight = Math.max(maxChildHeight, getTreeHeight(child));
        }
        return maxChildHeight + 1;
    }

    /**
     * 在森林中查找值为target的节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回null
     */
    public TreeNode findNode(int target) {
        for (TreeNode root : trees) {
            TreeNode found = findNodeInTree(root, target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 在单棵树中查找节点（递归辅助函数）
     * @param root 树的根节点
     * @param target 目标值
     * @return 找到的节点，如果不存在返回null
     */
    private TreeNode findNodeInTree(TreeNode root, int target) {
        if (root == null) {
            return null;
        }
        if (root.val == target) {
            return root;
        }
        for (TreeNode child : root.children) {
            TreeNode found = findNodeInTree(child, target);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 合并两棵树（将tree2合并到tree1）
     * @param tree1 第一棵树的根节点
     * @param tree2 第二棵树的根节点
     * @return 合并后的树的根节点
     */
    public TreeNode mergeTrees(TreeNode tree1, TreeNode tree2) {
        if (tree1 == null) {
            return tree2;
        }
        if (tree2 == null) {
            return tree1;
        }

        // 将tree2作为tree1的一个子节点
        tree1.addChild(tree2);
        
        // 从森林中移除tree2
        trees.remove(tree2);
        
        return tree1;
    }

    /**
     * 将树从森林中移除
     * @param root 要移除的树的根节点
     * @return 如果移除成功返回true，否则返回false
     */
    public boolean removeTree(TreeNode root) {
        return trees.remove(root);
    }

    /**
     * 森林的层序遍历
     * @return 层序遍历的结果列表
     */
    public List<List<Integer>> levelOrderTraversal() {
        List<List<Integer>> result = new ArrayList<>();
        for (TreeNode root : trees) {
            List<List<Integer>> treeLevelOrder = levelOrderTraversalTree(root);
            result.addAll(treeLevelOrder);
        }
        return result;
    }

    /**
     * 单棵树的层序遍历（辅助函数）
     * @param root 树的根节点
     * @return 层序遍历的结果列表
     */
    private List<List<Integer>> levelOrderTraversalTree(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) {
            return result;
        }

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                currentLevel.add(node.val);
                for (TreeNode child : node.children) {
                    queue.offer(child);
                }
            }

            result.add(currentLevel);
        }

        return result;
    }

    /**
     * 打印森林的结构
     */
    public void printForest() {
        System.out.println("森林包含 " + trees.size() + " 棵树：");
        for (int i = 0; i < trees.size(); i++) {
            System.out.println("\n树 " + (i + 1) + ":");
            printTree(trees.get(i), 0);
        }
    }

    /**
     * 打印单棵树的结构（辅助函数）
     * @param node 当前节点
     * @param level 当前节点的层级
     */
    private void printTree(TreeNode node, int level) {
        if (node == null) {
            return;
        }
        // 打印缩进
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println(node.val);
        // 递归打印子节点
        for (TreeNode child : node.children) {
            printTree(child, level + 1);
        }
    }

    /**
     * 将森林转换为并查集（基于父指针）
     * @return 并查集数组，其中parent[i]表示节点i的父节点
     */
    public Map<Integer, Integer> toUnionFind() {
        Map<Integer, Integer> parentMap = new HashMap<>();
        for (TreeNode root : trees) {
            buildUnionFind(root, -1, parentMap);
        }
        return parentMap;
    }

    /**
     * 构建并查集（递归辅助函数）
     * @param node 当前节点
     * @param parent 父节点值
     * @param parentMap 父节点映射
     */
    private void buildUnionFind(TreeNode node, int parent, Map<Integer, Integer> parentMap) {
        if (node == null) {
            return;
        }
        parentMap.put(node.val, parent);
        for (TreeNode child : node.children) {
            buildUnionFind(child, node.val, parentMap);
        }
    }

    /**
     * 从并查集构建森林
     * @param parentMap 并查集父节点映射
     * @return 构建的森林
     */
    public static Forest fromUnionFind(Map<Integer, Integer> parentMap) {
        Forest forest = new Forest();
        Map<Integer, TreeNode> nodeMap = new HashMap<>();
        List<Integer> roots = new ArrayList<>();

        // 创建所有节点并找出根节点
        for (Map.Entry<Integer, Integer> entry : parentMap.entrySet()) {
            int nodeVal = entry.getKey();
            int parentVal = entry.getValue();
            
            // 创建节点（如果不存在）
            nodeMap.putIfAbsent(nodeVal, new TreeNode(nodeVal));
            
            // 如果是根节点（父节点为-1）
            if (parentVal == -1) {
                roots.add(nodeVal);
            } else {
                // 创建父节点（如果不存在）
                nodeMap.putIfAbsent(parentVal, new TreeNode(parentVal));
                // 建立父子关系
                nodeMap.get(parentVal).addChild(nodeMap.get(nodeVal));
            }
        }

        // 将所有根节点添加到森林
        for (int rootVal : roots) {
            forest.addTree(nodeMap.get(rootVal));
        }

        return forest;
    }

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        // 创建森林示例
        Forest forest = new Forest();

        // 创建第一棵树
        //      1
        //     / \
        //    2   3
        //   /
        //  4
        TreeNode tree1 = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        tree1.addChild(node2);
        tree1.addChild(node3);
        node2.addChild(node4);

        // 创建第二棵树
        //      5
        //     / \
        //    6   7
        TreeNode tree2 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        tree2.addChild(node6);
        tree2.addChild(node7);

        // 创建第三棵树
        //      8
        TreeNode tree3 = new TreeNode(8);

        // 添加树到森林
        forest.addTree(tree1);
        forest.addTree(tree2);
        forest.addTree(tree3);

        // 打印森林
        System.out.println("初始森林：");
        forest.printForest();

        // 测试树的数量
        System.out.println("\n森林中树的数量: " + forest.getTreeCount());

        // 测试节点总数
        System.out.println("森林中节点总数: " + forest.getTotalNodeCount());

        // 测试总高度
        System.out.println("森林中所有树的高度之和: " + forest.getTotalHeight());

        // 测试查找节点
        TreeNode found = forest.findNode(6);
        System.out.println("查找节点6: " + (found != null ? "找到" : "未找到"));

        // 测试合并树
        System.out.println("\n合并第一棵树和第二棵树后：");
        forest.mergeTrees(tree1, tree2);
        forest.printForest();

        // 测试移除树
        System.out.println("\n移除第三棵树后：");
        forest.removeTree(tree3);
        forest.printForest();

        // 测试层序遍历
        System.out.println("\n森林的层序遍历：");
        List<List<Integer>> levelOrder = forest.levelOrderTraversal();
        for (List<Integer> level : levelOrder) {
            System.out.println(level);
        }

        // 测试并查集转换
        System.out.println("\n转换为并查集：");
        Map<Integer, Integer> unionFind = forest.toUnionFind();
        for (Map.Entry<Integer, Integer> entry : unionFind.entrySet()) {
            System.out.println("节点 " + entry.getKey() + " 的父节点: " + entry.getValue());
        }

        // 测试从并查集构建森林
        System.out.println("\n从并查集构建森林：");
        Forest rebuiltForest = Forest.fromUnionFind(unionFind);
        rebuiltForest.printForest();
    }
}