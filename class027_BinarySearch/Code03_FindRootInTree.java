package class189;

import java.util.*;

/**
 * 在树中查找根节点的算法实现
 * 
 * 核心思想：
 * 1. 通过交互式查询来确定树的根节点
 * 2. 利用树的性质：根节点是唯一没有父节点的节点
 * 3. 通过查询节点间的父子关系来确定根节点
 * 
 * 应用场景：
 * 1. 交互式图论问题
 * 2. 树结构重建
 * 3. 网络拓扑发现
 * 
 * 工程化考量：
 * 1. 查询次数优化
 * 2. 异常处理
 * 3. 边界条件处理
 * 4. 时间复杂度优化
 */
public class Code03_FindRootInTree {
    
    /**
     * 树节点类
     */
    static class TreeNode {
        int val;
        List<TreeNode> children;
        
        TreeNode(int val) {
            this.val = val;
            this.children = new ArrayList<>();
        }
    }
    
    /**
     * 模拟交互式查询接口
     * 在实际应用中，这可能是一个网络请求或用户输入
     */
    static class InteractiveQuery {
        private TreeNode root;
        private Map<Integer, TreeNode> nodeMap;
        private int queryCount;
        
        InteractiveQuery(TreeNode root) {
            this.root = root;
            this.nodeMap = new HashMap<>();
            this.queryCount = 0;
            buildNodeMap(root);
        }
        
        /**
         * 构建节点映射表
         */
        private void buildNodeMap(TreeNode node) {
            if (node == null) return;
            nodeMap.put(node.val, node);
            for (TreeNode child : node.children) {
                buildNodeMap(child);
            }
        }
        
        /**
         * 查询节点u是否是节点v的父节点
         * 
         * @param u 父节点候选
         * @param v 子节点候选
         * @return true如果u是v的父节点，否则false
         */
        public boolean isParent(int u, int v) {
            queryCount++;
            TreeNode nodeU = nodeMap.get(u);
            TreeNode nodeV = nodeMap.get(v);
            
            if (nodeU == null || nodeV == null) {
                return false;
            }
            
            // 检查u是否是v的直接父节点
            for (TreeNode child : nodeU.children) {
                if (child.val == v) {
                    return true;
                }
            }
            return false;
        }
        
        /**
         * 获取查询次数
         */
        public int getQueryCount() {
            return queryCount;
        }
        
        /**
         * 重置查询次数
         */
        public void resetQueryCount() {
            queryCount = 0;
        }
    }
    
    /**
     * 通过二分查找策略找到根节点
     * 
     * @param n 节点数量
     * @param query 查询接口
     * @return 根节点的值
     * 
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(1)
     */
    public static int findRootBinarySearch(int n, InteractiveQuery query) {
        // 根节点是唯一没有父节点的节点
        // 我们可以通过查询每个节点是否有父节点来找到根节点
        
        int left = 1;
        int right = n;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            // 检查mid是否有父节点
            boolean hasParent = false;
            for (int i = 1; i <= n; i++) {
                if (i != mid && query.isParent(i, mid)) {
                    hasParent = true;
                    break;
                }
            }
            
            if (!hasParent) {
                // 找到根节点
                return mid;
            } else {
                // 继续查找
                if (mid < n) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return -1;  // 未找到根节点
    }
    
    /**
     * 优化的查找根节点算法
     * 
     * @param n 节点数量
     * @param query 查询接口
     * @return 根节点的值
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int findRootOptimized(int n, InteractiveQuery query) {
        // 根节点是唯一没有父节点的节点
        // 我们可以通过查询每个节点是否有父节点来找到根节点
        
        for (int i = 1; i <= n; i++) {
            boolean hasParent = false;
            for (int j = 1; j <= n; j++) {
                if (i != j && query.isParent(j, i)) {
                    hasParent = true;
                    break;
                }
            }
            
            if (!hasParent) {
                // 找到根节点
                return i;
            }
        }
        
        return -1;  // 未找到根节点
    }
    
    /**
     * 自适应查找根节点算法
     * 
     * @param n 节点数量
     * @param query 查询接口
     * @return 根节点的值
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     */
    public static int findRootAdaptive(int n, InteractiveQuery query) {
        // 使用启发式策略优化查询顺序
        // 优先查询度数较低的节点
        
        // 首先统计每个节点的度数（作为启发式信息）
        int[] degree = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= n; j++) {
                if (i != j && query.isParent(i, j)) {
                    degree[i]++;  // i是父节点
                    degree[j]++;  // j是子节点
                }
            }
        }
        
        // 按度数排序节点
        List<Integer> nodes = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            nodes.add(i);
        }
        
        // 根据度数排序（度数低的优先）
        nodes.sort((a, b) -> Integer.compare(degree[a], degree[b]));
        
        // 按排序后的顺序查找根节点
        for (int node : nodes) {
            boolean hasParent = false;
            for (int j = 1; j <= n; j++) {
                if (node != j && query.isParent(j, node)) {
                    hasParent = true;
                    break;
                }
            }
            
            if (!hasParent) {
                // 找到根节点
                return node;
            }
        }
        
        return -1;  // 未找到根节点
    }
    
    /**
     * 构建测试树
     * 
     *       1
     *      / \
     *     2   3
     *    /   / \
     *   4   5   6
     *      /
     *     7
     */
    public static TreeNode buildTestTree() {
        TreeNode root = new TreeNode(1);
        TreeNode node2 = new TreeNode(2);
        TreeNode node3 = new TreeNode(3);
        TreeNode node4 = new TreeNode(4);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        TreeNode node7 = new TreeNode(7);
        
        root.children.add(node2);
        root.children.add(node3);
        node2.children.add(node4);
        node3.children.add(node5);
        node3.children.add(node6);
        node5.children.add(node7);
        
        return root;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 构建测试树
        TreeNode root = buildTestTree();
        InteractiveQuery query = new InteractiveQuery(root);
        
        int n = 7;  // 节点数量
        
        System.out.println("测试树结构：");
        System.out.println("       1");
        System.out.println("      / \\");
        System.out.println("     2   3");
        System.out.println("    /   / \\");
        System.out.println("   4   5   6");
        System.out.println("      /");
        System.out.println("     7");
        System.out.println();
        
        // 测试二分查找方法
        query.resetQueryCount();
        int root1 = findRootBinarySearch(n, query);
        System.out.println("二分查找方法找到的根节点：" + root1);
        System.out.println("查询次数：" + query.getQueryCount());
        System.out.println();
        
        // 测试优化方法
        query.resetQueryCount();
        int root2 = findRootOptimized(n, query);
        System.out.println("优化方法找到的根节点：" + root2);
        System.out.println("查询次数：" + query.getQueryCount());
        System.out.println();
        
        // 测试自适应方法
        query.resetQueryCount();
        int root3 = findRootAdaptive(n, query);
        System.out.println("自适应方法找到的根节点：" + root3);
        System.out.println("查询次数：" + query.getQueryCount());
    }
}