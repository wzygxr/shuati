package class034;

// 扁平化多级双向链表 - LeetCode 430
// 测试链接: https://leetcode.cn/problems/flatten-a-multilevel-doubly-linked-list/
public class Code25_FlattenAMultilevelDoublyLinkedList {

    // 提交时不要提交这个类
    public static class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;
        
        public Node() {}
        
        public Node(int val) {
            this.val = val;
        }
        
        public Node(int val, Node prev, Node next, Node child) {
            this.val = val;
            this.prev = prev;
            this.next = next;
            this.child = child;
        }
    }

    // 提交如下的方法
    public static Node flatten(Node head) {
        if (head == null) {
            return null;
        }
        
        // 使用深度优先搜索的递归方式
        flattenDFS(head);
        return head;
    }
    
    // 辅助递归方法，返回扁平化后的最后一个节点
    private static Node flattenDFS(Node node) {
        Node curr = node;
        Node last = node;
        
        while (curr != null) {
            Node next = curr.next;
            
            // 如果有子节点，先处理子节点
            if (curr.child != null) {
                Node childLast = flattenDFS(curr.child);
                
                // 连接当前节点和子链表的头
                curr.next = curr.child;
                curr.child.prev = curr;
                curr.child = null; // 清空child指针
                
                // 连接子链表的尾和原来的next节点
                if (next != null) {
                    childLast.next = next;
                    next.prev = childLast;
                }
                
                last = childLast;
            } else {
                last = curr;
            }
            
            curr = next;
        }
        
        return last;
    }
    
    /*
     * 题目扩展：LeetCode 430. 扁平化多级双向链表
     * 来源：LeetCode、LintCode
     * 
     * 题目描述：
     * 你会得到一个双链表，其中包含的节点可能有下一个节点（next指针）、
     * 前一个节点（prev指针），有的节点还有一个子链表（child指针），子链表的结构与原链表相同。
     * 请将其扁平化，以便所有节点都出现在单级双链表中。
     * 
     * 解题思路：
     * 1. 使用深度优先搜索的思想，递归处理每个子链表
     * 2. 对于每个节点，如果有子节点：
     *    a. 递归扁平化子链表
     *    b. 将当前节点与子链表头连接
     *    c. 将子链表尾与当前节点的下一节点连接
     *    d. 清空child指针
     * 3. 返回处理后的链表尾部节点，用于上层连接
     * 
     * 时间复杂度：O(n) - 每个节点只被访问一次
     * 空间复杂度：O(h) - h为子链表的最大深度，最坏情况下为O(n)
     * 是否最优解：是，递归实现清晰高效
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表、无child节点的链表
     * 2. 异常处理：确保指针操作的安全性
     * 3. 代码可读性：递归函数职责清晰
     * 
     * 与机器学习等领域的联系：
     * 1. 在树状数据结构处理中，类似的扁平化操作常用于特征处理
     * 2. 类似于HTML DOM树的扁平化处理
     * 
     * 语言特性差异：
     * Java: 自动垃圾回收，无需手动释放内存
     * C++: 需要注意内存管理，避免内存泄漏
     * Python: 实现方式类似，但语法更简洁
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 无child节点：返回原链表
     * 3. 只有child节点，无next节点的链表
     * 4. 多层嵌套的复杂链表结构
     */
    
    // 辅助方法：构建多级链表（简化版）
    public static Node buildMultilevelList() {
        // 构建一个简单的测试用例
        // 1 <-> 2 <-> 3 <-> 4
        //      |
        //      v
        //      5 <-> 6
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        Node node5 = new Node(5);
        Node node6 = new Node(6);
        
        // 连接主链表
        node1.next = node2;
        node2.prev = node1;
        node2.next = node3;
        node3.prev = node2;
        node3.next = node4;
        node4.prev = node3;
        
        // 添加子链表
        node2.child = node5;
        node5.next = node6;
        node6.prev = node5;
        
        return node1;
    }
    
    // 辅助方法：打印扁平化后的链表
    public static String printList(Node head) {
        StringBuilder sb = new StringBuilder();
        while (head != null) {
            sb.append(head.val);
            if (head.next != null) {
                sb.append(" <-> ");
            }
            head = head.next;
        }
        return sb.toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        Node head = buildMultilevelList();
        System.out.println("扁平化前的链表结构（简化表示）：");
        System.out.println("1 <-> 2 <-> 3 <-> 4");
        System.out.println("      |");
        System.out.println("      v");
        System.out.println("      5 <-> 6");
        
        Node result = flatten(head);
        System.out.println("\n扁平化后的链表：");
        System.out.println(printList(result));
    }
}