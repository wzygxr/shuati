package class175.随机化与复杂度分析;

import java.util.ArrayList;
import java.util.List;

/**
 * 二项堆（Binomial Heap）实现
 * 算法思想：二项堆是一组二项树的集合，每个二项树满足堆性质
 * 支持高效的合并操作，是可并堆的一种重要实现
 * 
 * 摊还分析与势能分析：
 * - 势能函数：选择为堆中树的数量
 * - 合并操作的摊还时间复杂度：O(log n)
 * - 插入操作的摊还时间复杂度：O(1)
 * - 提取最小操作的摊还时间复杂度：O(log n)
 * 
 * 相关题目：
 * 1. LeetCode 23. 合并K个排序链表 - https://leetcode-cn.com/problems/merge-k-sorted-lists/
 * 2. LeetCode 1046. 最后一块石头的重量 - https://leetcode-cn.com/problems/last-stone-weight/
 * 3. CodeChef - CHEFBM - https://www.codechef.com/problems/CHEFBM
 * 4. AtCoder - C - Min Difference - https://atcoder.jp/contests/abc129/tasks/abc129_c
 */
public class BinomialHeap {
    // 二项堆的根节点列表
    private Node head;
    // 最小节点引用
    private Node minNode;
    // 节点数量
    private int size;

    /**
     * 二项树节点定义
     */
    private static class Node {
        int key; // 节点值
        int degree; // 节点的度
        Node parent; // 父节点
        Node child; // 第一个子节点
        Node sibling; // 下一个兄弟节点

        public Node(int key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.sibling = null;
        }
    }

    /**
     * 构造空的二项堆
     */
    public BinomialHeap() {
        this.head = null;
        this.minNode = null;
        this.size = 0;
    }

    /**
     * 检查堆是否为空
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * 获取堆中的最小元素
     */
    public int findMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return minNode.key;
    }

    /**
     * 插入新元素
     * 摊还时间复杂度：O(1)
     */
    public void insert(int key) {
        BinomialHeap tempHeap = new BinomialHeap();
        Node newNode = new Node(key);
        tempHeap.head = newNode;
        tempHeap.minNode = newNode;
        tempHeap.size = 1;
        
        // 合并当前堆和只有一个节点的临时堆
        merge(tempHeap);
    }

    /**
     * 提取并返回堆中的最小元素
     * 摊还时间复杂度：O(log n)
     */
    public int extractMin() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        // 找到包含最小节点的树，并从根列表中移除
        Node prevMin = null;
        Node curr = head;
        Node min = minNode;
        
        // 查找最小节点的前一个节点
        if (head != min) {
            while (curr != null && curr.sibling != min) {
                curr = curr.sibling;
            }
            prevMin = curr;
        }
        
        // 从根列表中移除最小节点
        if (prevMin == null) {
            head = min.sibling;
        } else {
            prevMin.sibling = min.sibling;
        }
        
        // 将最小节点的子树添加到一个新的堆中
        BinomialHeap childHeap = new BinomialHeap();
        if (min.child != null) {
            Node child = min.child;
            // 反转子树列表，并设置父节点为null
            Node next;
            Node prev = null;
            while (child != null) {
                next = child.sibling;
                child.sibling = prev;
                child.parent = null;
                prev = child;
                child = next;
            }
            childHeap.head = prev;
            // 重新计算新堆的最小节点
            childHeap.updateMinNode();
        }
        
        // 合并原堆（已移除最小节点）和子堆
        if (head != null) {
            merge(childHeap);
        } else {
            head = childHeap.head;
            minNode = childHeap.minNode;
        }
        
        size--;
        return min.key;
    }

    /**
     * 合并两个二项堆
     * 摊还时间复杂度：O(log n)
     */
    public void merge(BinomialHeap otherHeap) {
        if (otherHeap == null || otherHeap.isEmpty()) {
            return;
        }
        if (this.isEmpty()) {
            this.head = otherHeap.head;
            this.minNode = otherHeap.minNode;
            this.size = otherHeap.size;
            return;
        }

        // 合并两个堆的根列表（按度数排序）
        Node newHead = mergeRootLists(this.head, otherHeap.head);
        this.head = null;
        this.minNode = null;
        
        if (newHead == null) {
            return;
        }

        // 合并相同度数的树
        Node prev = null;
        Node curr = newHead;
        Node next = curr.sibling;
        
        while (next != null) {
            // 如果当前树和下一棵树度数不同，或者下下棵树和当前树度数相同，则移动指针
            if (curr.degree != next.degree || (next.sibling != null && next.sibling.degree == curr.degree)) {
                prev = curr;
                curr = next;
            } else {
                // 合并度数相同的树
                if (curr.key <= next.key) {
                    // curr成为父节点
                    curr.sibling = next.sibling;
                    linkTrees(next, curr);
                } else {
                    // next成为父节点
                    if (prev == null) {
                        newHead = next;
                    } else {
                        prev.sibling = next;
                    }
                    linkTrees(curr, next);
                    curr = next;
                }
            }
            next = curr.sibling;
        }
        
        this.head = newHead;
        // 更新最小节点和大小
        updateMinNode();
        this.size += otherHeap.size;
    }

    /**
     * 合并两个有序的根列表（按度数递增排序）
     */
    private Node mergeRootLists(Node h1, Node h2) {
        if (h1 == null) return h2;
        if (h2 == null) return h1;
        
        Node head;
        // 选择度数较小的作为新的头节点
        if (h1.degree <= h2.degree) {
            head = h1;
            h1 = h1.sibling;
        } else {
            head = h2;
            h2 = h2.sibling;
        }
        
        Node current = head;
        // 合并剩余节点
        while (h1 != null && h2 != null) {
            if (h1.degree <= h2.degree) {
                current.sibling = h1;
                h1 = h1.sibling;
            } else {
                current.sibling = h2;
                h2 = h2.sibling;
            }
            current = current.sibling;
        }
        
        // 连接剩余的节点
        if (h1 != null) {
            current.sibling = h1;
        } else {
            current.sibling = h2;
        }
        
        return head;
    }

    /**
     * 将child树链接到parent树下
     */
    private void linkTrees(Node child, Node parent) {
        child.parent = parent;
        child.sibling = parent.child;
        parent.child = child;
        parent.degree++;
    }

    /**
     * 更新最小节点引用
     */
    private void updateMinNode() {
        Node min = null;
        Node current = head;
        
        while (current != null) {
            if (min == null || current.key < min.key) {
                min = current;
            }
            current = current.sibling;
        }
        
        this.minNode = min;
    }

    /**
     * 获取堆的大小
     */
    public int size() {
        return size;
    }

    /**
     * 打印堆的结构（用于调试）
     */
    public void printHeap() {
        System.out.println("Binomial Heap Structure:");
        if (isEmpty()) {
            System.out.println("Empty heap");
            return;
        }
        printNode(head, 0);
    }

    private void printNode(Node node, int level) {
        if (node == null) return;
        
        // 打印当前节点
        for (int i = 0; i < level; i++) {
            System.out.print("  ");
        }
        System.out.println("Key: " + node.key + ", Degree: " + node.degree);
        
        // 打印子节点
        if (node.child != null) {
            printNode(node.child, level + 1);
        }
        
        // 打印兄弟节点
        printNode(node.sibling, level);
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        BinomialHeap heap = new BinomialHeap();
        
        // 测试插入操作
        System.out.println("插入元素: 10, 20, 5, 15, 30");
        heap.insert(10);
        heap.insert(20);
        heap.insert(5);
        heap.insert(15);
        heap.insert(30);
        
        System.out.println("堆的大小: " + heap.size());
        System.out.println("最小元素: " + heap.findMin());
        
        // 打印堆结构
        heap.printHeap();
        
        // 测试合并操作
        BinomialHeap heap2 = new BinomialHeap();
        heap2.insert(8);
        heap2.insert(12);
        heap2.insert(2);
        
        System.out.println("\n合并另一个堆（元素: 8, 12, 2）");
        heap.merge(heap2);
        
        System.out.println("合并后堆的大小: " + heap.size());
        System.out.println("合并后最小元素: " + heap.findMin());
        
        // 打印堆结构
        heap.printHeap();
        
        // 测试提取最小操作
        System.out.println("\n提取最小元素: " + heap.extractMin());
        System.out.println("提取后最小元素: " + heap.findMin());
        
        System.out.println("\n提取最小元素: " + heap.extractMin());
        System.out.println("提取后最小元素: " + heap.findMin());
        
        // 打印最终堆结构
        heap.printHeap();
    }
}