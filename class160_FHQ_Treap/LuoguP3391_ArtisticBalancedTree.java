package class152;

// 洛谷 P3391 文艺平衡树 - Java实现
// 使用FHQ-Treap（无旋Treap）解决区间翻转问题
// 题目链接: https://www.luogu.com.cn/problem/P3391
// 题目描述: 维护一个序列，支持区间反转操作，并输出最终数组
// 
// 解题思路:
// 使用FHQ-Treap维护序列，通过按大小分裂和合并操作结合懒标记实现区间翻转
// 实现O(log n)的区间反转操作复杂度

import java.util.Random;
import java.util.Scanner;

public class LuoguP3391_ArtisticBalancedTree {
    // FHQ-Treap节点定义
    private static class Node {
        int key;        // 键值
        int priority;   // 随机优先级
        int size;       // 子树大小
        boolean reversed; // 反转标记（懒标记）
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int k, int prio) {
            key = k;
            priority = prio;
            size = 1;
            reversed = false;
            left = right = null;
        }
    }
    
    private Node root;       // 根节点
    private Random random;   // 随机数生成器
    
    public LuoguP3391_ArtisticBalancedTree() {
        root = null;
        random = new Random();
    }
    
    // 更新节点的子树大小
    private void updateSize(Node node) {
        if (node != null) {
            int leftSize = node.left != null ? node.left.size : 0;
            int rightSize = node.right != null ? node.right.size : 0;
            node.size = leftSize + rightSize + 1;
        }
    }
    
    // 下传懒标记
    private void pushDown(Node node) {
        if (node != null && node.reversed) {
            // 交换左右子树
            Node temp = node.left;
            node.left = node.right;
            node.right = temp;
            
            // 标记子节点为待反转
            if (node.left != null) {
                node.left.reversed = !node.left.reversed;
            }
            if (node.right != null) {
                node.right.reversed = !node.right.reversed;
            }
            
            // 清除当前节点的反转标记
            node.reversed = false;
        }
    }
    
    // 按照大小分裂（第k大）
    private Node[] splitBySize(Node root, int k) {
        if (root == null) {
            return new Node[]{null, null};
        }
        
        // 先下传懒标记
        pushDown(root);
        
        int leftSize = root.left != null ? root.left.size : 0;
        
        if (leftSize + 1 <= k) {
            // 当前节点及其左子树属于左部分，递归分裂右子树
            Node[] rightSplit = splitBySize(root.right, k - leftSize - 1);
            root.right = rightSplit[0];
            updateSize(root);
            return new Node[]{root, rightSplit[1]};
        } else {
            // 当前节点及其右子树属于右部分，递归分裂左子树
            Node[] leftSplit = splitBySize(root.left, k);
            root.left = leftSplit[1];
            updateSize(root);
            return new Node[]{leftSplit[0], root};
        }
    }
    
    // 合并操作
    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;
        
        // 先下传懒标记
        pushDown(left);
        pushDown(right);
        
        if (left.priority >= right.priority) {
            left.right = merge(left.right, right);
            updateSize(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            updateSize(right);
            return right;
        }
    }
    
    // 插入节点
    public void insert(int key) {
        root = merge(root, new Node(key, random.nextInt()));
    }
    
    // 区间反转操作 [l, r]
    public void reverse(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node[] split1 = splitBySize(root, r);
        Node[] split2 = splitBySize(split1[0], l - 1);
        
        // 对中间部分打反转标记
        if (split2[1] != null) {
            split2[1].reversed = !split2[1].reversed;
        }
        
        // 合并回去
        root = merge(merge(split2[0], split2[1]), split1[1]);
    }
    
    // 中序遍历输出树
    public void inorderTraversal(Node node, StringBuilder sb) {
        if (node == null) return;
        
        // 先下传懒标记
        pushDown(node);
        
        inorderTraversal(node.left, sb);
        sb.append(node.key).append(" ");
        inorderTraversal(node.right, sb);
    }
    
    // 输出整棵树
    public String toString() {
        StringBuilder sb = new StringBuilder();
        inorderTraversal(root, sb);
        return sb.toString().trim();
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // 序列长度
        int m = scanner.nextInt(); // 操作次数
        
        LuoguP3391_ArtisticBalancedTree tree = new LuoguP3391_ArtisticBalancedTree();
        
        // 初始化树，插入1~n的序列
        for (int i = 1; i <= n; i++) {
            // 这里使用插入方式构建树，更直观但效率不是最优的
            // 更优的方式是构建平衡的Treap
            Node newNode = new Node(i, tree.random.nextInt());
            tree.root = tree.merge(tree.root, newNode);
        }
        
        // 处理每个反转操作
        for (int i = 0; i < m; i++) {
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            tree.reverse(l, r);
        }
        
        // 输出结果
        System.out.println(tree.toString());
        
        scanner.close();
    }
}

/**
 * 【时间复杂度分析】
 * - 构建树：O(n log n)
 * - 每次反转操作：O(log n)
 * - 中序遍历：O(n)
 * 总时间复杂度：O(n log n + m log n)
 * 
 * 【空间复杂度分析】
 * - O(n)，存储n个节点
 * 
 * 【优化说明】
 * 1. 使用懒标记优化区间反转操作，避免每次都需要遍历区间内的所有节点
 * 2. 按照大小分裂，便于区间操作
 * 3. 在下传懒标记时交换左右子树，实现高效的区间反转
 * 
 * 【测试用例】
 * 输入：
 * 6 3
 * 1 3
 * 1 4
 * 1 6
 * 输出：
 * 6 5 3 4 2 1
 * 
 * 【Java语言特性考虑】
 * 1. 使用StringBuilder高效拼接输出结果
 * 2. 使用递归实现中序遍历，代码简洁
 * 3. 注意懒标记的正确处理，避免遗漏
 */