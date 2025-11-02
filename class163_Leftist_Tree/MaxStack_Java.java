package class155;

import java.util.*;

/**
 * LeetCode 716. Max Stack（最大栈）
 * 
 * 题目链接: https://leetcode.com/problems/max-stack/
 * 
 * 题目描述：设计一个最大栈，支持push、pop、top、peekMax和popMax操作。
 * 操作说明：
 * - push(x) -- 将元素x压入栈中
 * - pop() -- 移除栈顶元素并返回该元素
 * - top() -- 返回栈顶元素
 * - peekMax() -- 返回栈中最大元素
 * - popMax() -- 返回栈中最大元素，并将其删除
 * 
 * 解题思路：使用左偏树实现最大栈
 * 
 * 时间复杂度：所有操作均为O(log n)
 * 空间复杂度：O(n)
 */
public class MaxStack_Java {
    
    // 定义链表节点，用于存储栈中的元素
    private static class Node {
        int value;
        Node prev;
        Node next;
        boolean deleted; // 标记节点是否被删除
        
        public Node(int value) {
            this.value = value;
            this.deleted = false;
        }
    }
    
    // 定义左偏树节点
    private static class LeftistTreeNode {
        int value;
        Node stackNode; // 指向栈中的对应节点
        int dist;
        LeftistTreeNode left;
        LeftistTreeNode right;
        
        public LeftistTreeNode(int value, Node stackNode) {
            this.value = value;
            this.stackNode = stackNode;
            this.dist = 0;
            this.left = null;
            this.right = null;
        }
    }
    
    // 合并两个左偏树
    private LeftistTreeNode merge(LeftistTreeNode a, LeftistTreeNode b) {
        if (a == null) return b;
        if (b == null) return a;
        
        // 维护大根堆性质（最大值优先）
        if (a.value < b.value) {
            LeftistTreeNode temp = a;
            a = b;
            b = temp;
        }
        
        // 递归合并右子树
        a.right = merge(a.right, b);
        
        // 维护左偏性质
        if (a.left == null || (a.right != null && a.left.dist < a.right.dist)) {
            LeftistTreeNode temp = a.left;
            a.left = a.right;
            a.right = temp;
        }
        
        // 更新距离
        a.dist = (a.right == null) ? 0 : a.right.dist + 1;
        return a;
    }
    
    // 栈的头节点和尾节点
    private Node head; // 栈底
    private Node tail; // 栈顶
    
    // 大根堆的根节点
    private LeftistTreeNode maxHeapRoot;
    
    public MaxStack_Java() {
        // 初始化双向链表的头尾哨兵节点
        head = new Node(Integer.MIN_VALUE);
        tail = new Node(Integer.MAX_VALUE);
        head.next = tail;
        tail.prev = head;
        
        maxHeapRoot = null;
    }
    
    // 将元素x压入栈中
    public void push(int x) {
        // 创建新的栈节点
        Node newNode = new Node(x);
        
        // 将新节点插入到链表尾部（栈顶）
        newNode.next = tail;
        newNode.prev = tail.prev;
        tail.prev.next = newNode;
        tail.prev = newNode;
        
        // 将新节点加入大根堆
        maxHeapRoot = merge(maxHeapRoot, new LeftistTreeNode(x, newNode));
    }
    
    // 移除栈顶元素并返回该元素
    public int pop() {
        // 确保栈不为空
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        
        // 获取栈顶节点
        Node topNode = tail.prev;
        
        // 标记为已删除
        topNode.deleted = true;
        
        // 从链表中移除
        topNode.prev.next = topNode.next;
        topNode.next.prev = topNode.prev;
        
        return topNode.value;
    }
    
    // 返回栈顶元素
    public int top() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return tail.prev.value;
    }
    
    // 返回栈中最大元素
    public int peekMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        
        // 清理堆中已删除的节点
        while (maxHeapRoot != null && maxHeapRoot.stackNode.deleted) {
            maxHeapRoot = merge(maxHeapRoot.left, maxHeapRoot.right);
        }
        
        return maxHeapRoot.value;
    }
    
    // 返回栈中最大元素，并将其删除
    public int popMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        
        // 清理堆中已删除的节点
        while (maxHeapRoot != null && maxHeapRoot.stackNode.deleted) {
            maxHeapRoot = merge(maxHeapRoot.left, maxHeapRoot.right);
        }
        
        // 获取最大值节点
        LeftistTreeNode maxNode = maxHeapRoot;
        int maxValue = maxNode.value;
        
        // 从堆中删除最大值节点
        maxHeapRoot = merge(maxHeapRoot.left, maxHeapRoot.right);
        
        // 从栈中删除对应的节点
        Node stackNode = maxNode.stackNode;
        stackNode.deleted = true;
        stackNode.prev.next = stackNode.next;
        stackNode.next.prev = stackNode.prev;
        
        return maxValue;
    }
    
    // 检查栈是否为空
    public boolean isEmpty() {
        return head.next == tail;
    }
    
    // 测试主函数
    public static void main(String[] args) {
        MaxStack_Java maxStack = new MaxStack_Java();
        maxStack.push(5);
        maxStack.push(1);
        maxStack.push(5);
        
        System.out.println("top: " + maxStack.top());        // 输出 5
        System.out.println("popMax: " + maxStack.popMax());  // 输出 5
        System.out.println("top: " + maxStack.top());        // 输出 1
        System.out.println("peekMax: " + maxStack.peekMax()); // 输出 5
        System.out.println("pop: " + maxStack.pop());        // 输出 1
        System.out.println("top: " + maxStack.top());        // 输出 5
    }
}