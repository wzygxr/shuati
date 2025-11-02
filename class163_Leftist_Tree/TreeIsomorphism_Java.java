package class155;

import java.util.*;

/**
 * 牛客 NC20828 [HNOI2004] 树的同构
 * 题目链接：https://ac.nowcoder.com/acm/problem/20828
 * 
 * 题目描述：
 * 给定两棵有根树，判断它们是否同构。同构的定义是：两棵树可以通过若干次交换子节点的顺序得到彼此。
 * 
 * 解题思路：
 * 使用左偏树维护哈希值，进行树的同构判断。
 * 通过递归计算每棵树的哈希值，同构的树会得到相同的哈希值。
 * 
 * 算法步骤：
 * 1. 对于每棵树，递归计算每个节点的哈希值
 * 2. 使用左偏树对子节点的哈希值进行排序
 * 3. 将排序后的子节点哈希值合并成当前节点的哈希值
 * 4. 比较所有树的哈希值，相同哈希值的树是同构的
 * 
 * 时间复杂度：O(N log N)，其中N是节点总数
 * 空间复杂度：O(N)
 * 
 * 相关题目：
 * - Java实现：TreeIsomorphism_Java.java
 * - Python实现：TreeIsomorphism_Python.py
 * - C++实现：TreeIsomorphism_Cpp.cpp
 */
public class TreeIsomorphism_Java {
    
    // 左偏树节点类
    static class LeftistTreeNode {
        long hash; // 哈希值
        int dist;  // 距离（空路径长度）
        LeftistTreeNode left, right;
        
        /**
         * 构造函数
         * @param hash 哈希值
         */
        public LeftistTreeNode(long hash) {
            this.hash = hash;
            this.dist = 0;
            this.left = this.right = null;
        }
    }
    
    /**
     * 合并两个左偏树
     * @param a 第一棵左偏树的根节点
     * @param b 第二棵左偏树的根节点
     * @return 合并后的左偏树根节点
     */
    private static LeftistTreeNode merge(LeftistTreeNode a, LeftistTreeNode b) {
        // 处理空树情况
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护大根堆性质：确保a的根节点哈希值大于等于b的根节点哈希值
        if (a.hash < b.hash) {
            LeftistTreeNode temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并a的右子树与b
        a.right = merge(a.right, b);
        
        // 维护左偏性质：左子树的距离应大于等于右子树的距离
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    // 树节点类
    static class TreeNode {
        int id;
        List<TreeNode> children;
        
        /**
         * 构造函数
         * @param id 节点ID
         */
        public TreeNode(int id) {
            this.id = id;
            this.children = new ArrayList<>();
        }
    }
    
    /**
     * 计算树的哈希值
     * @param root 树的根节点
     * @return 树的哈希值
     */
    private static long computeHash(TreeNode root) {
        // 空节点的哈希值为0
        if (root == null) return 0;
        
        // 使用左偏树维护子节点的哈希值
        LeftistTreeNode heap = null;
        for (TreeNode child : root.children) {
            long childHash = computeHash(child);
            heap = merge(heap, new LeftistTreeNode(childHash));
        }
        
        // 计算当前节点的哈希值，结合子节点的哈希值
        long hash = 1; // 初始哈希值
        
        // 通过左偏树排序子节点的哈希值，确保同构的树得到相同的哈希值
        while (heap != null) {
            hash = hash * 1000003 + heap.hash; // 使用大质数作为基数
            // 删除根节点，继续处理下一个子节点
            heap = merge(heap.left, heap.right);
        }
        
        return hash;
    }
    
    /**
     * 构建树
     * @param scanner 输入扫描器
     * @param n 节点数量
     * @return 树的根节点
     */
    private static TreeNode buildTree(Scanner scanner, int n) {
        // 创建所有节点
        TreeNode[] nodes = new TreeNode[n + 1];
        int rootId = -1;
        
        for (int i = 1; i <= n; i++) {
            nodes[i] = new TreeNode(i);
        }
        
        // 根据输入建立父子关系
        for (int i = 1; i <= n; i++) {
            int parent = scanner.nextInt();
            if (parent == 0) {
                // 父节点为0表示当前节点是根节点
                rootId = i;
            } else {
                // 建立父子关系
                nodes[parent].children.add(nodes[i]);
            }
        }
        
        return nodes[rootId];
    }
    
    /**
     * 主函数
     * 输入格式：
     * 第一行包含一个整数t，表示测试用例数量
     * 对于每个测试用例：
     *   第一行包含一个整数n，表示节点数量
     *   接下来n行，第i行包含一个整数，表示节点i的父节点（0表示根节点）
     * 输出格式：
     * 对于每个测试用例，输出与当前树同构的最小编号
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int t = scanner.nextInt(); // 测试用例数量
        
        // 使用哈希表存储哈希值到树编号的映射
        Map<Long, List<Integer>> hashToTrees = new HashMap<>();
        
        // 处理每个测试用例
        for (int i = 1; i <= t; i++) {
            int n = scanner.nextInt();
            TreeNode root = buildTree(scanner, n);
            long hash = computeHash(root);
            
            // 将具有相同哈希值的树分组
            hashToTrees.putIfAbsent(hash, new ArrayList<>());
            hashToTrees.get(hash).add(i);
        }
        
        // 输出同构的最小树编号
        for (int i = 1; i <= t; i++) {
            boolean found = false;
            for (List<Integer> group : hashToTrees.values()) {
                if (group.contains(i)) {
                    System.out.print(group.get(0));
                    if (i < t) System.out.print(" ");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.print(i);
                if (i < t) System.out.print(" ");
            }
        }
        
        scanner.close();
    }
}