package class008_AdvancedAlgorithmsAndDataStructures.doubly_circular_linked_list_problems;

import java.util.*;

/**
 * LeetCode 716. Max Stack
 * 
 * 题目描述：
 * 设计一个最大栈数据结构，支持栈的基本操作，并支持在常数时间内检索到栈中的最大元素。
 * 实现 MaxStack 类：
 * - MaxStack() 初始化栈对象
 * - void push(int x) 将元素 x 压入栈中
 * - int pop() 删除栈顶元素并返回
 * - int top() 获取栈顶元素
 * - int peekMax() 获取栈中的最大元素
 * - int popMax() 删除栈中的最大元素并返回
 * 
 * 解题思路：
 * 使用双向循环链表实现栈，并维护一个指向最大元素的指针。
 * 为了支持 popMax 操作，我们需要能够快速找到最大元素并删除它。
 * 
 * 时间复杂度：
 * - push(), pop(), top(), peekMax(): O(1)
 * - popMax(): O(n)
 * 空间复杂度：O(n)
 */
public class LeetCode_716_MaxStack {
    
    // 栈节点
    static class Node {
        int value;
        Node prev, next;
        
        Node(int value) {
            this.value = value;
        }
    }
    
    static class MaxStack {
        private Node head; // 双向循环链表的虚拟头节点
        private Node maxNode; // 指向最大元素的指针
        
        public MaxStack() {
            // 创建双向循环链表的虚拟头节点
            head = new Node(0);
            head.prev = head;
            head.next = head;
            maxNode = null;
        }
        
        public void push(int x) {
            // 创建新节点
            Node newNode = new Node(x);
            
            // 将新节点插入到链表头部
            newNode.next = head.next;
            newNode.prev = head;
            head.next.prev = newNode;
            head.next = newNode;
            
            // 更新最大节点指针
            if (maxNode == null || x >= maxNode.value) {
                maxNode = newNode;
            }
        }
        
        public int pop() {
            if (head.next == head) {
                throw new RuntimeException("Stack is empty");
            }
            
            // 获取栈顶节点
            Node topNode = head.next;
            
            // 从链表中删除栈顶节点
            head.next = topNode.next;
            topNode.next.prev = head;
            
            // 如果删除的是最大节点，需要重新查找最大节点
            if (topNode == maxNode) {
                findNewMax();
            }
            
            return topNode.value;
        }
        
        public int top() {
            if (head.next == head) {
                throw new RuntimeException("Stack is empty");
            }
            return head.next.value;
        }
        
        public int peekMax() {
            if (maxNode == null) {
                throw new RuntimeException("Stack is empty");
            }
            return maxNode.value;
        }
        
        public int popMax() {
            if (maxNode == null) {
                throw new RuntimeException("Stack is empty");
            }
            
            // 获取最大节点的值
            int maxValue = maxNode.value;
            
            // 从链表中删除最大节点
            maxNode.prev.next = maxNode.next;
            maxNode.next.prev = maxNode.prev;
            
            // 重新查找最大节点
            findNewMax();
            
            return maxValue;
        }
        
        // 重新查找最大节点
        private void findNewMax() {
            maxNode = null;
            Node current = head.next;
            
            // 遍历链表找到最大节点
            while (current != head) {
                if (maxNode == null || current.value >= maxNode.value) {
                    maxNode = current;
                }
                current = current.next;
            }
        }
        
        // 辅助方法：打印栈内容（用于调试）
        public void printStack() {
            System.out.print("Stack: [");
            Node current = head.next;
            boolean first = true;
            while (current != head) {
                if (!first) {
                    System.out.print(", ");
                }
                System.out.print(current.value);
                first = false;
                current = current.next;
            }
            System.out.println("]");
            System.out.println("Max: " + (maxNode != null ? maxNode.value : "null"));
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        System.out.println("测试用例1:");
        MaxStack stack = new MaxStack();
        
        stack.push(5);
        stack.push(1);
        stack.push(5);
        
        System.out.println("top() = " + stack.top()); // 5
        System.out.println("pop() = " + stack.pop()); // 5
        System.out.println("top() = " + stack.top()); // 1
        System.out.println("peekMax() = " + stack.peekMax()); // 5
        System.out.println("popMax() = " + stack.popMax()); // 5
        System.out.println("top() = " + stack.top()); // 1
        System.out.println("peekMax() = " + stack.peekMax()); // 1
        System.out.println("pop() = " + stack.pop()); // 1
        System.out.println("pop() = " + stack.pop()); // 5
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        MaxStack stack2 = new MaxStack();
        
        stack2.push(1);
        stack2.push(2);
        stack2.push(3);
        
        System.out.println("peekMax() = " + stack2.peekMax()); // 3
        System.out.println("popMax() = " + stack2.popMax()); // 3
        System.out.println("peekMax() = " + stack2.peekMax()); // 2
        System.out.println("popMax() = " + stack2.popMax()); // 2
        System.out.println("peekMax() = " + stack2.peekMax()); // 1
        System.out.println();
        
        // 测试用例3
        System.out.println("测试用例3:");
        MaxStack stack3 = new MaxStack();
        
        stack3.push(5);
        stack3.push(1);
        stack3.push(3);
        stack3.push(7);
        stack3.push(2);
        
        System.out.println("peekMax() = " + stack3.peekMax()); // 7
        System.out.println("popMax() = " + stack3.popMax()); // 7
        System.out.println("peekMax() = " + stack3.peekMax()); // 5
        System.out.println("pop() = " + stack3.pop()); // 2
        System.out.println("peekMax() = " + stack3.peekMax()); // 5
    }
}