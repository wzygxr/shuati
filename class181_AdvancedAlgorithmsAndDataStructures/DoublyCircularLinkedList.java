package class185.doubly_circular_linked_list_problems;

import java.util.*;

/**
 * 双向循环链表实现 (Java版本)
 * 
 * 算法思路：
 * 双向循环链表是一种线性数据结构，每个节点都有指向前驱和后继节点的指针，
 * 并且尾节点指向头节点，形成一个环。
 * 
 * 应用场景：
 * 1. 操作系统：内存管理和进程调度
 * 2. 浏览器：历史记录和标签页管理
 * 3. 音乐播放器：播放列表管理
 * 4. 游戏开发：对象管理
 * 
 * 时间复杂度：
 * - 插入操作：
 *   - 在头部/尾部插入：O(1)
 *   - 在指定位置插入：O(n)
 * - 删除操作：
 *   - 删除头部/尾部：O(1)
 *   - 删除指定位置：O(n)
 *   - 按值删除：O(n)
 * - 查找操作：O(n)
 * - 遍历操作：O(n)
 * - 其他操作：
 *   - 反转：O(n)
 *   - 旋转：O(n)
 *   - 清空：O(n)
 * 
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * 1. LeetCode LCR 155. 将二叉搜索树转化为排序的双向链表
 * 2. LeetCode 426. 将二叉搜索树转化为排序的双向链表
 */
class Node {
    int data;
    Node prev;
    Node next;
    
    Node(int data) {
        this.data = data;
        this.prev = this;
        this.next = this;
    }
}

public class DoublyCircularLinkedList {
    private Node head;
    private int size;
    
    public DoublyCircularLinkedList() {
        this.head = null;
        this.size = 0;
    }
    
    public boolean isEmpty() {
        return head == null;
    }
    
    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }
    
    private Node getNodeAt(int index) {
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("索引超出范围");
        }
        
        // 优化：根据索引位置选择从头还是从尾开始遍历
        if (index <= size / 2) {
            // 从头开始遍历
            Node current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            // 从尾开始遍历（尾部是head.prev）
            Node current = head.prev;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }
    
    public void insertAtHead(int value) {
        Node newNode = new Node(value);
        
        if (isEmpty()) {
            head = newNode;
        } else {
            Node tail = head.prev;
            
            newNode.prev = tail;
            tail.next = newNode;
            
            newNode.next = head;
            head.prev = newNode;
            
            head = newNode;
        }
        
        size++;
    }
    
    public void insertAtTail(int value) {
        Node newNode = new Node(value);
        
        if (isEmpty()) {
            head = newNode;
        } else {
            Node tail = head.prev;
            
            tail.next = newNode;
            newNode.prev = tail;
            
            newNode.next = head;
            head.prev = newNode;
        }
        
        size++;
    }
    
    public void insertAtPosition(int index, int value) {
        if (index == 0) {
            insertAtHead(value);
            return;
        }
        
        if (index == size) {
            insertAtTail(value);
            return;
        }
        
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("索引超出范围");
        }
        
        Node prevNode = getNodeAt(index - 1);
        Node nextNode = prevNode.next;
        
        Node newNode = new Node(value);
        
        newNode.prev = prevNode;
        newNode.next = nextNode;
        prevNode.next = newNode;
        nextNode.prev = newNode;
        
        size++;
    }
    
    public int deleteHead() {
        if (isEmpty()) {
            throw new RuntimeException("无法从空链表删除");
        }
        
        Node oldHead = head;
        int value = oldHead.data;
        
        if (size == 1) {
            head = null;
        } else {
            Node tail = head.prev;
            Node newHead = head.next;
            
            tail.next = newHead;
            newHead.prev = tail;
            
            head = newHead;
        }
        
        size--;
        return value;
    }
    
    public int deleteTail() {
        if (isEmpty()) {
            throw new RuntimeException("无法从空链表删除");
        }
        
        Node tail = head.prev;
        int value = tail.data;
        
        if (size == 1) {
            head = null;
        } else {
            Node newTail = tail.prev;
            
            newTail.next = head;
            head.prev = newTail;
        }
        
        size--;
        return value;
    }
    
    public int deleteAtPosition(int index) {
        if (isEmpty()) {
            throw new RuntimeException("无法从空链表删除");
        }
        
        if (!isValidIndex(index)) {
            throw new IndexOutOfBoundsException("索引超出范围");
        }
        
        if (index == 0) {
            return deleteHead();
        }
        
        if (index == size - 1) {
            return deleteTail();
        }
        
        Node nodeToDelete = getNodeAt(index);
        int value = nodeToDelete.data;
        
        Node prevNode = nodeToDelete.prev;
        Node nextNode = nodeToDelete.next;
        
        prevNode.next = nextNode;
        nextNode.prev = prevNode;
        
        size--;
        return value;
    }
    
    public boolean deleteByValue(int value) {
        if (isEmpty()) {
            return false;
        }
        
        if (head.data == value) {
            deleteHead();
            return true;
        }
        
        Node current = head.next;
        while (current != head) {
            if (current.data == value) {
                Node prevNode = current.prev;
                Node nextNode = current.next;
                
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
                
                size--;
                return true;
            }
            current = current.next;
        }
        
        return false;
    }
    
    public List<Integer> traverseForward() {
        List<Integer> result = new ArrayList<>();
        if (isEmpty()) {
            return result;
        }
        
        Node current = head;
        do {
            result.add(current.data);
            current = current.next;
        } while (current != head);
        
        return result;
    }
    
    public List<Integer> traverseBackward() {
        List<Integer> result = new ArrayList<>();
        if (isEmpty()) {
            return result;
        }
        
        Node current = head.prev;
        do {
            result.add(current.data);
            current = current.prev;
        } while (current != head.prev);
        
        return result;
    }
    
    public int search(int value) {
        if (isEmpty()) {
            return -1;
        }
        
        Node current = head;
        int index = 0;
        do {
            if (current.data == value) {
                return index;
            }
            current = current.next;
            index++;
        } while (current != head);
        
        return -1;
    }
    
    public int get(int index) {
        Node node = getNodeAt(index);
        return node.data;
    }
    
    public void set(int index, int value) {
        Node node = getNodeAt(index);
        node.data = value;
    }
    
    public int getSize() {
        return size;
    }
    
    public void clear() {
        head = null;
        size = 0;
    }
    
    public void reverse() {
        if (isEmpty() || size == 1) {
            return;
        }
        
        Node current = head;
        do {
            Node temp = current.prev;
            current.prev = current.next;
            current.next = temp;
            
            current = current.prev;
        } while (current != head);
        
        head = head.prev;
    }
    
    public void rotate(int k) {
        if (isEmpty() || size == 1 || k % size == 0) {
            return;
        }
        
        k = k % size;
        if (k < 0) {
            k += size;
        }
        
        if (k > 0) {
            Node newHead = head;
            for (int i = 0; i < size - k; i++) {
                newHead = newHead.next;
            }
            
            head = newHead;
        }
    }
    
    public void printList() {
        if (isEmpty()) {
            System.out.println("List is empty");
            return;
        }
        
        Node current = head;
        System.out.print("List: ");
        do {
            System.out.print(current.data);
            if (current.next != head) {
                System.out.print(" <-> ");
            }
            current = current.next;
        } while (current != head);
        System.out.println(" (circular)");
    }
    
    public static void main(String[] args) {
        System.out.println("=== 测试双向循环链表 ===");
        
        DoublyCircularLinkedList list = new DoublyCircularLinkedList();
        
        // 测试插入操作
        System.out.println("\n1. 测试插入操作:");
        System.out.println("插入10, 20, 30, 40, 50");
        list.insertAtTail(10);
        list.insertAtTail(20);
        list.insertAtTail(30);
        list.insertAtTail(40);
        list.insertAtTail(50);
        list.printList();
        System.out.println("List size: " + list.getSize());
        
        System.out.println("\n在头部插入5:");
        list.insertAtHead(5);
        list.printList();
        
        System.out.println("\n在位置3插入25:");
        list.insertAtPosition(3, 25);
        list.printList();
        
        // 测试遍历操作
        System.out.println("\n2. 测试遍历操作:");
        System.out.println("正向遍历: " + list.traverseForward());
        System.out.println("反向遍历: " + list.traverseBackward());
        
        // 测试查找和访问操作
        System.out.println("\n3. 测试查找和访问操作:");
        int value = 25;
        int index = list.search(value);
        System.out.println("查找值 " + value + ": 索引 = " + index);
        
        index = 3;
        value = list.get(index);
        System.out.println("索引 " + index + " 的值 = " + value);
        
        System.out.println("设置索引2的值为15:");
        list.set(2, 15);
        list.printList();
        
        // 测试删除操作
        System.out.println("\n4. 测试删除操作:");
        System.out.println("删除头部元素:");
        value = list.deleteHead();
        System.out.println("删除的值 = " + value);
        list.printList();
        
        System.out.println("删除尾部元素:");
        value = list.deleteTail();
        System.out.println("删除的值 = " + value);
        list.printList();
        
        System.out.println("删除索引2的元素:");
        value = list.deleteAtPosition(2);
        System.out.println("删除的值 = " + value);
        list.printList();
        
        System.out.println("删除值20:");
        boolean deleted = list.deleteByValue(20);
        System.out.println("删除 " + (deleted ? "成功" : "失败"));
        list.printList();
        
        // 测试反转操作
        System.out.println("\n5. 测试反转操作:");
        list.reverse();
        System.out.println("反转后:");
        list.printList();
        
        // 测试旋转操作
        System.out.println("\n6. 测试旋转操作:");
        System.out.println("向右旋转1步:");
        list.rotate(1);
        list.printList();
        
        System.out.println("向左旋转2步:");
        list.rotate(-2);
        list.printList();
        
        // 测试边界情况
        System.out.println("\n7. 测试边界情况:");
        System.out.println("清空链表:");
        list.clear();
        list.printList();
        System.out.println("List size: " + list.getSize());
        
        System.out.println("空链表插入元素:");
        list.insertAtTail(100);
        list.printList();
        
        System.out.println("单节点链表删除:");
        value = list.deleteHead();
        System.out.println("删除的值 = " + value);
        list.printList();
        
        // 性能测试
        System.out.println("\n=== 性能测试 ===");
        
        DoublyCircularLinkedList largeList = new DoublyCircularLinkedList();
        
        long startTime = System.nanoTime();
        for (int i = 0; i < 10000; i++) {
            largeList.insertAtTail(i);
        }
        long insertTime = System.nanoTime() - startTime;
        
        System.out.println("插入10000个元素时间: " + insertTime / 1_000_000.0 + " ms");
        System.out.println("链表大小: " + largeList.getSize());
        
        startTime = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            largeList.get(i * 10);
        }
        long accessTime = System.nanoTime() - startTime;
        
        System.out.println("1000次随机访问时间: " + accessTime / 1_000_000.0 + " ms");
    }
}
