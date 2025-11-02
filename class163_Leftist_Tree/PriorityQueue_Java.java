package class155;

import java.util.*;

/**
 * AizuOJ ALDS1_9_C Priority Queue（优先级队列）
 * 题目链接：https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/9/ALDS1_9_C
 * 
 * 题目描述：
 * 实现一个最大优先级队列，支持以下操作：
 * 1. insert(x) - 插入元素x
 * 2. extract - 提取并删除最大元素
 * 3. end - 结束程序
 * 
 * 解题思路：
 * 使用左偏树（Leftist Tree）实现最大堆，可以高效支持合并和删除最大元素操作。
 * 左偏树是一种可合并堆，具有良好的时间复杂度特性。
 * 
 * 算法特点：
 * 1. 左偏树是一种二叉树，满足堆性质（父节点值大于等于子节点值）
 * 2. 满足左偏性质：左子树的距离大于等于右子树的距离
 * 3. 距离定义：从节点到最近的空节点的路径长度
 * 
 * 时间复杂度：
 * - 插入元素：O(log n)
 * - 提取最大元素：O(log n)
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - Java实现：PriorityQueue_Java.java
 * - Python实现：PriorityQueue_Python.py
 * - C++实现：PriorityQueue_Cpp.cpp
 */
public class PriorityQueue_Java {
    
    // 左偏树节点类（最大堆）
    static class LeftistTreeNode {
        int value;    // 节点值
        int dist;     // 距离（空路径长度）
        LeftistTreeNode left;
        LeftistTreeNode right;
        
        /**
         * 构造函数
         * @param value 节点值
         */
        public LeftistTreeNode(int value) {
            this.value = value;
            this.dist = 0;  // 叶子节点距离为0
            this.left = null;
            this.right = null;
        }
    }
    
    /**
     * 合并两个左偏树（最大堆）
     * @param a 第一棵左偏树的根节点
     * @param b 第二棵左偏树的根节点
     * @return 合并后的左偏树根节点
     */
    private static LeftistTreeNode merge(LeftistTreeNode a, LeftistTreeNode b) {
        // 处理空树情况
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护最大堆性质：确保a的根节点值大于等于b的根节点值
        if (a.value < b.value) {
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
    
    /**
     * 获取最大值（堆顶）
     * @param root 左偏树根节点
     * @return 堆顶元素的值
     * @throws IllegalStateException 如果堆为空
     */
    private static int getMax(LeftistTreeNode root) {
        if (root == null) {
            throw new IllegalStateException("Priority queue is empty");
        }
        return root.value;
    }
    
    /**
     * 删除最大值（堆顶）
     * @param root 左偏树根节点
     * @return 删除堆顶元素后的新根节点
     * @throws IllegalStateException 如果堆为空
     */
    private static LeftistTreeNode deleteMax(LeftistTreeNode root) {
        if (root == null) {
            throw new IllegalStateException("Priority queue is empty");
        }
        // 合并左右子树作为新的根节点
        LeftistTreeNode newRoot = merge(root.left, root.right);
        return newRoot;
    }
    
    /**
     * 插入元素
     * @param root 左偏树根节点
     * @param value 要插入的元素值
     * @return 插入元素后的新根节点
     */
    private static LeftistTreeNode insert(LeftistTreeNode root, int value) {
        // 创建新节点
        LeftistTreeNode newNode = new LeftistTreeNode(value);
        // 合并原树与新节点
        return merge(root, newNode);
    }
    
    /**
     * 主函数，处理输入命令并执行相应操作
     * 输入格式：
     * 多行输入，每行包含一个命令：
     * - insert x：插入元素x
     * - extract：提取并删除最大元素
     * - end：结束程序
     * 输出格式：
     * 对于每个extract命令，输出提取的最大元素
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LeftistTreeNode root = null;  // 左偏树根节点，初始为空
        
        // 循环处理命令
        while (true) {
            String command = scanner.next();
            
            if (command.equals("insert")) {
                // 插入元素
                int value = scanner.nextInt();
                root = insert(root, value);
            } else if (command.equals("extract")) {
                // 提取最大元素
                int maxValue = getMax(root);
                System.out.println(maxValue);
                root = deleteMax(root);
            } else if (command.equals("end")) {
                // 结束程序
                break;
            }
        }
        
        scanner.close();
    }
}