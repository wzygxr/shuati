package class008_AdvancedAlgorithmsAndDataStructures.unrolled_linked_list_problems;

import java.util.*;

/**
 * LeetCode 641. 设计循环双端队列 (Design Circular Deque)
 * 
 * 题目来源：https://leetcode.cn/problems/design-circular-deque/
 * 
 * 题目描述：
 * 设计实现双端队列。实现 MyCircularDeque 类:
 * - MyCircularDeque(int k)：构造函数,双端队列最大为 k。
 * - boolean insertFront(int value)：将一个元素添加到双端队列头部。如果操作成功返回 true，否则返回 false。
 * - boolean insertLast(int value)：将一个元素添加到双端队列尾部。如果操作成功返回 true，否则返回 false。
 * - boolean deleteFront()：从双端队列头部删除一个元素。如果操作成功返回 true，否则返回 false。
 * - boolean deleteLast()：从双端队列尾部删除一个元素。如果操作成功返回 true，否则返回 false。
 * - int getFront()：从双端队列头部获得一个元素。如果双端队列为空，返回 -1。
 * - int getRear()：获得双端队列的最后一个元素。如果双端队列为空，返回 -1。
 * - boolean isEmpty()：若双端队列为空，则返回 true，否则返回 false。
 * - boolean isFull()：若双端队列满了，则返回 true，否则返回 false。
 * 
 * 算法思路：
 * 循环双端队列可以通过以下方式实现：
 * 1. 数组实现：使用固定大小的数组和头尾指针
 * 2. 块状链表：使用块状链表（Unrolled Linked List）实现，每个块存储多个元素
 * 3. 双向链表：使用双向链表实现
 * 
 * 时间复杂度（块状链表实现）：
 * - insertFront/insertLast：O(1) 均摊
 * - deleteFront/deleteLast：O(1) 均摊
 * - getFront/getRear：O(1)
 * - isEmpty/isFull：O(1)
 * - 空间复杂度：O(k)
 * 
 * 应用场景：
 * 1. 操作系统：任务调度队列
 * 2. 网络编程：数据包缓冲队列
 * 3. 数据库：查询结果缓冲
 * 4. 游戏开发：事件队列
 * 
 * 相关题目：
 * 1. LeetCode 707. 设计链表
 * 2. LeetCode 239. 滑动窗口最大值
 * 3. LeetCode 286. 墙与门
 */
public class LeetCode_641_DesignCircularDeque {
    
    /**
     * 块状链表节点类
     */
    static class Block {
        int capacity;      // 块的最大容量
        int[] array;       // 块内的数组
        int size;          // 当前块中元素的数量
        Block next;        // 指向下一个块
        Block prev;        // 指向上一个块
        
        Block(int capacity) {
            this.capacity = capacity;
            this.array = new int[capacity];
            this.size = 0;
            this.next = null;
            this.prev = null;
        }
        
        boolean isFull() {
            return size == capacity;
        }
        
        boolean isEmpty() {
            return size == 0;
        }
        
        int size() {
            return size;
        }
        
        int capacity() {
            return capacity;
        }
    }
    
    /**
     * 块状链表实现的循环双端队列
     */
    static class UnrolledCircularDeque {
        private int blockCapacity;  // 块的最大容量
        private Block head;         // 头块指针
        private Block tail;         // 尾块指针
        private int size;           // 队列元素总数
        private int capacity;       // 队列最大容量
        
        /**
         * 构造函数
         * @param capacity 队列最大容量
         */
        public UnrolledCircularDeque(int capacity) {
            // 根据总容量计算块容量，这里选择sqrt(capacity)作为块容量
            this.blockCapacity = Math.max(2, (int) Math.sqrt(capacity));
            this.capacity = capacity;
            this.size = 0;
            
            // 初始化空队列
            this.head = null;
            this.tail = null;
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public boolean isFull() {
            return size == capacity;
        }
        
        public int size() {
            return size;
        }
        
        /**
         * 在队列头部插入元素
         * 时间复杂度：O(1) 均摊
         */
        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            
            if (isEmpty()) {
                // 空队列，创建第一个块
                head = new Block(blockCapacity);
                tail = head;
                head.array[0] = value;
                head.size = 1;
            } else {
                // 检查头块是否已满
                if (head.size == head.capacity) {
                    // 头块已满，创建新块作为新的头块
                    Block newBlock = new Block(blockCapacity);
                    newBlock.array[0] = value;
                    newBlock.size = 1;
                    
                    // 连接新块
                    newBlock.next = head;
                    head.prev = newBlock;
                    head = newBlock;
                } else {
                    // 头块未满，直接在头部插入
                    // 将头块中的元素向后移动一位
                    for (int i = head.size; i > 0; i--) {
                        head.array[i] = head.array[i - 1];
                    }
                    head.array[0] = value;
                    head.size++;
                }
            }
            
            size++;
            return true;
        }
        
        /**
         * 在队列尾部插入元素
         * 时间复杂度：O(1) 均摊
         */
        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            
            if (isEmpty()) {
                // 空队列，创建第一个块
                head = new Block(blockCapacity);
                tail = head;
                head.array[0] = value;
                head.size = 1;
            } else {
                // 检查尾块是否已满
                if (tail.size == tail.capacity) {
                    // 尾块已满，创建新块作为新的尾块
                    Block newBlock = new Block(blockCapacity);
                    newBlock.array[0] = value;
                    newBlock.size = 1;
                    
                    // 连接新块
                    newBlock.prev = tail;
                    tail.next = newBlock;
                    tail = newBlock;
                } else {
                    // 尾块未满，直接在尾部插入
                    tail.array[tail.size] = value;
                    tail.size++;
                }
            }
            
            size++;
            return true;
        }
        
        /**
         * 删除队列头部元素
         * 时间复杂度：O(1) 均摊
         */
        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            
            // 删除头块的第一个元素
            for (int i = 0; i < head.size - 1; i++) {
                head.array[i] = head.array[i + 1];
            }
            head.size--;
            
            // 如果头块变空，删除头块
            if (head.size == 0) {
                if (head == tail) {
                    // 队列变空
                    head = null;
                    tail = null;
                } else {
                    // 移动头指针
                    head = head.next;
                    head.prev = null;
                }
            }
            
            size--;
            return true;
        }
        
        /**
         * 删除队列尾部元素
         * 时间复杂度：O(1) 均摊
         */
        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            
            // 删除尾块的最后一个元素
            tail.size--;
            
            // 如果尾块变空，删除尾块
            if (tail.size == 0) {
                if (head == tail) {
                    // 队列变空
                    head = null;
                    tail = null;
                } else {
                    // 移动尾指针
                    tail = tail.prev;
                    tail.next = null;
                }
            }
            
            size--;
            return true;
        }
        
        /**
         * 获取队列头部元素
         * 时间复杂度：O(1)
         */
        public int getFront() {
            if (isEmpty()) {
                return -1;
            }
            return head.array[0];
        }
        
        /**
         * 获取队列尾部元素
         * 时间复杂度：O(1)
         */
        public int getRear() {
            if (isEmpty()) {
                return -1;
            }
            return tail.array[tail.size - 1];
        }
    }
    
    private UnrolledCircularDeque deque;
    
    /**
     * 构造函数
     * @param k 双端队列的最大容量
     */
    public LeetCode_641_DesignCircularDeque(int k) {
        this.deque = new UnrolledCircularDeque(k);
    }
    
    /**
     * 在队列头部插入元素
     * @param value 要插入的值
     * @return 如果操作成功返回true，否则返回false
     */
    public boolean insertFront(int value) {
        return deque.insertFront(value);
    }
    
    /**
     * 在队列尾部插入元素
     * @param value 要插入的值
     * @return 如果操作成功返回true，否则返回false
     */
    public boolean insertLast(int value) {
        return deque.insertLast(value);
    }
    
    /**
     * 删除队列头部元素
     * @return 如果操作成功返回true，否则返回false
     */
    public boolean deleteFront() {
        return deque.deleteFront();
    }
    
    /**
     * 删除队列尾部元素
     * @return 如果操作成功返回true，否则返回false
     */
    public boolean deleteLast() {
        return deque.deleteLast();
    }
    
    /**
     * 获取队列头部元素
     * @return 队列头部元素，如果队列为空返回-1
     */
    public int getFront() {
        return deque.getFront();
    }
    
    /**
     * 获取队列尾部元素
     * @return 队列尾部元素，如果队列为空返回-1
     */
    public int getRear() {
        return deque.getRear();
    }
    
    /**
     * 检查队列是否为空
     * @return 如果队列为空返回true，否则返回false
     */
    public boolean isEmpty() {
        return deque.isEmpty();
    }
    
    /**
     * 检查队列是否已满
     * @return 如果队列已满返回true，否则返回false
     */
    public boolean isFull() {
        return deque.isFull();
    }
    
    /**
     * 方法2：使用数组实现的循环双端队列（用于对比）
     */
    static class MyCircularDequeArray {
        private int[] deque;
        private int front;
        private int rear;
        private int size;
        private int capacity;
        
        public MyCircularDequeArray(int k) {
            this.capacity = k;
            this.deque = new int[k];
            this.front = 0;
            this.rear = 0;
            this.size = 0;
        }
        
        public boolean insertFront(int value) {
            if (isFull()) {
                return false;
            }
            
            front = (front - 1 + capacity) % capacity;
            deque[front] = value;
            size++;
            return true;
        }
        
        public boolean insertLast(int value) {
            if (isFull()) {
                return false;
            }
            
            deque[rear] = value;
            rear = (rear + 1) % capacity;
            size++;
            return true;
        }
        
        public boolean deleteFront() {
            if (isEmpty()) {
                return false;
            }
            
            front = (front + 1) % capacity;
            size--;
            return true;
        }
        
        public boolean deleteLast() {
            if (isEmpty()) {
                return false;
            }
            
            rear = (rear - 1 + capacity) % capacity;
            size--;
            return true;
        }
        
        public int getFront() {
            if (isEmpty()) {
                return -1;
            }
            return deque[front];
        }
        
        public int getRear() {
            if (isEmpty()) {
                return -1;
            }
            return deque[(rear - 1 + capacity) % capacity];
        }
        
        public boolean isEmpty() {
            return size == 0;
        }
        
        public boolean isFull() {
            return size == capacity;
        }
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 641. 设计循环双端队列 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        LeetCode_641_DesignCircularDeque deque1 = new LeetCode_641_DesignCircularDeque(3);
        System.out.println("insertLast(1): " + deque1.insertLast(1)); // 返回 true
        System.out.println("insertLast(2): " + deque1.insertLast(2)); // 返回 true
        System.out.println("insertFront(3): " + deque1.insertFront(3)); // 返回 true
        System.out.println("insertFront(4): " + deque1.insertFront(4)); // 返回 false（队列已满）
        System.out.println("getRear(): " + deque1.getRear()); // 返回 2
        System.out.println("isFull(): " + deque1.isFull()); // 返回 true
        System.out.println("deleteLast(): " + deque1.deleteLast()); // 返回 true
        System.out.println("insertFront(4): " + deque1.insertFront(4)); // 返回 true
        System.out.println("getFront(): " + deque1.getFront()); // 返回 3
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        LeetCode_641_DesignCircularDeque deque2 = new LeetCode_641_DesignCircularDeque(4);
        System.out.println("insertFront(9): " + deque2.insertFront(9)); // 返回 true
        System.out.println("deleteLast(): " + deque2.deleteLast()); // 返回 true
        System.out.println("getRear(): " + deque2.getRear()); // 返回 -1
        System.out.println("getFront(): " + deque2.getFront()); // 返回 -1
        System.out.println("getFront(): " + deque2.getFront()); // 返回 -1
        System.out.println("deleteFront(): " + deque2.deleteFront()); // 返回 false
        System.out.println("insertFront(6): " + deque2.insertFront(6)); // 返回 true
        System.out.println("insertLast(5): " + deque2.insertLast(5)); // 返回 true
        System.out.println("insertFront(9): " + deque2.insertFront(9)); // 返回 true
        System.out.println("getFront(): " + deque2.getFront()); // 返回 9
        System.out.println("insertFront(6): " + deque2.insertFront(6)); // 返回 true
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        int dequeCapacity = 10000;
        int operationCount = 100000;
        Random random = new Random(42);
        
        // 测试块状链表实现
        LeetCode_641_DesignCircularDeque deque3 = new LeetCode_641_DesignCircularDeque(dequeCapacity);
        long startTime = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            int operation = random.nextInt(6);
            int value = random.nextInt(1000);
            
            switch (operation) {
                case 0:
                    deque3.insertFront(value);
                    break;
                case 1:
                    deque3.insertLast(value);
                    break;
                case 2:
                    deque3.deleteFront();
                    break;
                case 3:
                    deque3.deleteLast();
                    break;
                case 4:
                    deque3.getFront();
                    break;
                case 5:
                    deque3.getRear();
                    break;
            }
        }
        long endTime = System.nanoTime();
        System.out.println("块状链表实现处理" + operationCount + "次操作时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
        
        // 测试数组实现
        MyCircularDequeArray deque4 = new MyCircularDequeArray(dequeCapacity);
        startTime = System.nanoTime();
        for (int i = 0; i < operationCount; i++) {
            int operation = random.nextInt(6);
            int value = random.nextInt(1000);
            
            switch (operation) {
                case 0:
                    deque4.insertFront(value);
                    break;
                case 1:
                    deque4.insertLast(value);
                    break;
                case 2:
                    deque4.deleteFront();
                    break;
                case 3:
                    deque4.deleteLast();
                    break;
                case 4:
                    deque4.getFront();
                    break;
                case 5:
                    deque4.getRear();
                    break;
            }
        }
        endTime = System.nanoTime();
        System.out.println("数组实现处理" + operationCount + "次操作时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}