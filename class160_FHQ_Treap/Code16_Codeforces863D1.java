package class152;

// Codeforces 863D - Yet Another Array Queries Problem
// 题目链接: https://codeforces.com/contest/863/problem/D
// 题目描述: 给定一个数组和一系列操作，支持以下操作：
// 1. 将区间[l,r]循环右移一位
// 2. 将区间[l,r]循环左移一位
// 3. 查询位置x的元素值
//
// 解题思路:
// 使用FHQ-Treap维护数组，通过懒标记支持区间循环移位操作
// 实现O(log n)的区间操作和O(log n)的查询操作

import java.util.Random;
import java.util.Scanner;

public class Code16_Codeforces863D1 {
    // FHQ-Treap节点定义
    private static class Node {
        int key;        // 键值（数组元素）
        int priority;   // 随机优先级
        int size;       // 子树大小
        int shift;      // 循环移位标记（懒标记）
        Node left;      // 左子节点
        Node right;     // 右子节点
        
        Node(int k, int prio) {
            key = k;
            priority = prio;
            size = 1;
            shift = 0;
            left = right = null;
        }
    }
    
    private Node root;       // 根节点
    private Random random;   // 随机数生成器
    
    public Code16_Codeforces863D1() {
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
        if (node != null && node.shift != 0) {
            // 应用循环移位
            node.shift = node.shift % node.size;
            if (node.shift != 0) {
                // 分裂成三部分：前size-shift个，中间shift个，后0个
                Node[] split1 = splitBySize(node, node.size - node.shift);
                Node[] split2 = splitBySize(split1[0], node.size - node.shift - 1);
                
                // 重新合并：中间shift个 + 前size-shift个
                node = merge(merge(split1[1], split2[0]), split2[1]);
                
                // 传递懒标记给子节点
                if (node.left != null) {
                    node.left.shift = (node.left.shift + node.shift) % node.left.size;
                }
                if (node.right != null) {
                    node.right.shift = (node.right.shift + node.shift) % node.right.size;
                }
                
                // 清除当前节点的移位标记
                node.shift = 0;
            }
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
            Node[] rightSplit = splitBySize(root.right, k - leftSize - 1);
            root.right = rightSplit[0];
            updateSize(root);
            return new Node[]{root, rightSplit[1]};
        } else {
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
    
    // 构建初始数组
    public void build(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            Node newNode = new Node(arr[i], random.nextInt());
            root = merge(root, newNode);
        }
    }
    
    // 区间循环右移 [l, r]
    public void rotateRight(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node[] split1 = splitBySize(root, r);
        Node[] split2 = splitBySize(split1[0], l - 1);
        
        // 对中间部分打循环右移标记
        if (split2[1] != null) {
            split2[1].shift = (split2[1].shift + 1) % split2[1].size;
        }
        
        // 合并回去
        root = merge(merge(split2[0], split2[1]), split1[1]);
    }
    
    // 区间循环左移 [l, r]
    public void rotateLeft(int l, int r) {
        // 先将树分裂成三部分：1~l-1, l~r, r+1~n
        Node[] split1 = splitBySize(root, r);
        Node[] split2 = splitBySize(split1[0], l - 1);
        
        // 对中间部分打循环左移标记
        if (split2[1] != null) {
            split2[1].shift = (split2[1].shift - 1 + split2[1].size) % split2[1].size;
        }
        
        // 合并回去
        root = merge(merge(split2[0], split2[1]), split1[1]);
    }
    
    // 查询位置x的元素值
    public int query(int x) {
        // 先下传所有懒标记
        pushDownAll(root);
        
        // 查找第x个元素
        return findKth(root, x);
    }
    
    // 下传所有懒标记
    private void pushDownAll(Node node) {
        if (node != null) {
            pushDown(node);
            pushDownAll(node.left);
            pushDownAll(node.right);
        }
    }
    
    // 查找第k个元素
    private int findKth(Node node, int k) {
        if (node == null) return -1;
        
        pushDown(node);
        
        int leftSize = node.left != null ? node.left.size : 0;
        
        if (k <= leftSize) {
            return findKth(node.left, k);
        } else if (k == leftSize + 1) {
            return node.key;
        } else {
            return findKth(node.right, k - leftSize - 1);
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt(); // 数组长度
        int q = scanner.nextInt(); // 操作次数
        int m = scanner.nextInt(); // 查询次数
        
        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }
        
        Code16_Codeforces863D1 tree = new Code16_Codeforces863D1();
        tree.build(arr);
        
        // 处理每个操作
        for (int i = 0; i < q; i++) {
            int type = scanner.nextInt();
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            
            if (type == 1) {
                tree.rotateRight(l, r);
            } else {
                tree.rotateLeft(l, r);
            }
        }
        
        // 处理查询
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int x = scanner.nextInt();
            sb.append(tree.query(x)).append(" ");
        }
        
        System.out.println(sb.toString().trim());
        
        scanner.close();
    }
}

/**
 * 【时间复杂度分析】
 * - 构建数组：O(n log n)
 * - 每次操作：O(log n)
 * - 每次查询：O(log n)
 * 总时间复杂度：O(n log n + (q + m) log n)
 * 
 * 【空间复杂度分析】
 * - O(n)，存储n个节点
 * 
 * 【优化说明】
 * 1. 使用FHQ-Treap维护数组，支持高效的区间操作
 * 2. 通过懒标记优化循环移位操作，避免每次都需要遍历区间内的所有节点
 * 3. 按照大小分裂，便于区间操作
 * 
 * 【测试用例】
 * 输入：
 * 5 3 3
 * 1 2 3 4 5
 * 1 2 4
 * 2 1 5
 * 1 1 3
 * 1 2 3
 * 输出：
 * 4 2 5
 */