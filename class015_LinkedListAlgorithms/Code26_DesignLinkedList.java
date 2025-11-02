package class034;

// 设计链表 - LeetCode 707
// 测试链接: https://leetcode.cn/problems/design-linked-list/
public class Code26_DesignLinkedList {

    // 单链表节点类
    private static class ListNode {
        int val;
        ListNode next;
        
        public ListNode() {}
        
        public ListNode(int val) {
            this.val = val;
        }
    }
    
    // 提交如下的类
    public static class MyLinkedList {
        private ListNode head; // 头节点
        private int size;      // 链表大小
        
        /** 初始化链表 */
        public MyLinkedList() {
            head = null;
            size = 0;
        }
        
        /** 获取链表中第 index 个节点的值。如果索引无效，则返回 -1。 */
        public int get(int index) {
            if (index < 0 || index >= size) {
                return -1; // 索引无效
            }
            
            ListNode curr = head;
            for (int i = 0; i < index; i++) {
                curr = curr.next;
            }
            return curr.val;
        }
        
        /** 在链表的第一个元素之前添加一个值为 val 的节点。插入后，新节点将成为链表的第一个节点。 */
        public void addAtHead(int val) {
            ListNode newNode = new ListNode(val);
            newNode.next = head;
            head = newNode;
            size++;
        }
        
        /** 将值为 val 的节点追加到链表的最后一个元素。 */
        public void addAtTail(int val) {
            ListNode newNode = new ListNode(val);
            
            if (head == null) {
                // 空链表的情况
                head = newNode;
            } else {
                // 找到最后一个节点
                ListNode curr = head;
                while (curr.next != null) {
                    curr = curr.next;
                }
                curr.next = newNode;
            }
            
            size++;
        }
        
        /** 在链表中的第 index 个节点之前添加值为 val 的节点。
         * 如果 index 等于链表的长度，则该节点将附加到链表的末尾。
         * 如果 index 大于链表长度，则不会插入节点。
         * 如果 index 小于0，则在头部插入节点。
         */
        public void addAtIndex(int index, int val) {
            // 处理特殊情况
            if (index > size) {
                return; // 不插入
            }
            
            if (index <= 0) {
                addAtHead(val); // 在头部插入
                return;
            }
            
            if (index == size) {
                addAtTail(val); // 在尾部插入
                return;
            }
            
            // 找到插入位置的前一个节点
            ListNode prev = head;
            for (int i = 0; i < index - 1; i++) {
                prev = prev.next;
            }
            
            // 插入新节点
            ListNode newNode = new ListNode(val);
            newNode.next = prev.next;
            prev.next = newNode;
            size++;
        }
        
        /** 如果索引 index 有效，则删除链表中的第 index 个节点。 */
        public void deleteAtIndex(int index) {
            if (index < 0 || index >= size || head == null) {
                return; // 索引无效或链表为空
            }
            
            if (index == 0) {
                // 删除头节点
                head = head.next;
            } else {
                // 找到删除位置的前一个节点
                ListNode prev = head;
                for (int i = 0; i < index - 1; i++) {
                    prev = prev.next;
                }
                prev.next = prev.next.next;
            }
            
            size--;
        }
    }
    
    /*
     * 题目扩展：LeetCode 707. 设计链表
     * 来源：LeetCode、LintCode、牛客网
     * 
     * 题目描述：
     * 设计链表的实现。您可以选择使用单链表或双链表。
     * 单链表中的节点应该具有两个属性：val 和 next。val 是当前节点的值，
     * next 是指向下一个节点的指针/引用。如果要使用双向链表，
     * 则还需要一个属性 prev 以指示链表中的上一个节点。假设链表中的所有节点都是 0-indexed 的。
     * 
     * 解题思路：
     * 1. 实现单链表的基本操作：
     *    a. 获取指定位置的节点值
     *    b. 在头部添加节点
     *    c. 在尾部添加节点
     *    d. 在指定位置添加节点
     *    e. 删除指定位置的节点
     * 2. 使用虚拟头节点可以简化代码，但这里为了清晰展示基本操作，直接处理头节点
     * 
     * 时间复杂度：
     * - get(index): O(n)
     * - addAtHead(val): O(1)
     * - addAtTail(val): O(n)
     * - addAtIndex(index, val): O(n)
     * - deleteAtIndex(index): O(n)
     * 空间复杂度：O(1) - 除了存储节点的空间外，只使用常数额外空间
     * 
     * 是否最优解：对于单链表实现，这是最优解。如果使用双链表和哈希表等数据结构可以优化某些操作的时间复杂度，但会增加空间复杂度。
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、索引越界等
     * 2. 异常处理：输入参数校验
     * 3. 代码可读性：方法命名清晰，职责单一
     * 4. 可扩展性：可以轻松扩展为双向链表
     * 
     * 与机器学习等领域的联系：
     * 1. 在数据结构层面，链表是许多高级数据结构的基础
     * 2. 在流数据处理中，链表常用于动态维护数据流
     * 
     * 语言特性差异：
     * Java: 使用类封装链表操作，自动垃圾回收
     * C++: 需要手动管理内存，注意内存泄漏
     * Python: 可以使用更灵活的方式实现，但效率可能较低
     * 
     * 极端输入场景：
     * 1. 频繁在头部插入/删除
     * 2. 频繁在尾部插入/删除
     * 3. 频繁访问中间节点
     * 4. 空链表操作
     */
    
    // 测试方法
    public static void main(String[] args) {
        MyLinkedList linkedList = new MyLinkedList();
        
        // 测试基本操作
        linkedList.addAtHead(1);
        linkedList.addAtTail(3);
        linkedList.addAtIndex(1, 2);   // 链表变为 1->2->3
        System.out.println("get(1): " + linkedList.get(1));    // 返回 2
        linkedList.deleteAtIndex(1);   // 现在链表是 1->3
        System.out.println("get(1): " + linkedList.get(1));    // 返回 3
        
        // 测试边界情况
        MyLinkedList linkedList2 = new MyLinkedList();
        linkedList2.addAtHead(7);
        linkedList2.addAtHead(2);
        linkedList2.addAtHead(1);
        linkedList2.addAtIndex(3, 0);  // 链表变为 1->2->7->0
        linkedList2.deleteAtIndex(2);  // 链表变为 1->2->0
        linkedList2.addAtHead(6);
        linkedList2.addAtTail(4);
        System.out.println("get(4): " + linkedList2.get(4));    // 返回 4
    }
}