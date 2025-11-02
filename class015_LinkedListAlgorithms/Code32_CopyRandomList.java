package class034;

import java.util.HashMap;
import java.util.Map;

// 复杂链表的复制 - 剑指Offer
// 测试链接: https://leetcode.cn/problems/copy-list-with-random-pointer/
public class Code32_CopyRandomList {

    // 提交时不要提交这个类
    public static class Node {
        public int val;
        public Node next;
        public Node random;
        
        public Node() {
            this.val = 0;
            this.next = null;
            this.random = null;
        }
        
        public Node(int val) {
            this.val = val;
            this.next = null;
            this.random = null;
        }
        
        public Node(int val, Node next, Node random) {
            this.val = val;
            this.next = next;
            this.random = random;
        }
    }

    // 提交如下的方法 - 哈希表法
    public static Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        
        // 使用哈希表存储原节点和新节点的映射关系
        Map<Node, Node> map = new HashMap<>();
        
        // 第一次遍历：创建所有新节点
        Node curr = head;
        while (curr != null) {
            map.put(curr, new Node(curr.val));
            curr = curr.next;
        }
        
        // 第二次遍历：连接next和random指针
        curr = head;
        while (curr != null) {
            Node newNode = map.get(curr);
            newNode.next = map.get(curr.next); // 处理next指针
            newNode.random = map.get(curr.random); // 处理random指针
            curr = curr.next;
        }
        
        // 返回新链表的头节点
        return map.get(head);
    }
    
    // 方法2：原地复制法（不需要额外空间）
    public static Node copyRandomListInPlace(Node head) {
        if (head == null) {
            return null;
        }
        
        // 第一步：在每个原节点后插入对应的新节点
        Node curr = head;
        while (curr != null) {
            Node newNode = new Node(curr.val);
            newNode.next = curr.next;
            curr.next = newNode;
            curr = newNode.next;
        }
        
        // 第二步：处理random指针
        curr = head;
        while (curr != null) {
            if (curr.random != null) {
                curr.next.random = curr.random.next; // 新节点的random指向原节点random的新节点
            }
            curr = curr.next.next; // 跳过新节点，移动到下一个原节点
        }
        
        // 第三步：分离两个链表
        curr = head;
        Node newHead = head.next;
        Node newCurr = newHead;
        
        while (curr != null) {
            curr.next = curr.next.next; // 恢复原链表
            if (newCurr.next != null) {
                newCurr.next = newCurr.next.next; // 构建新链表
            }
            curr = curr.next;
            newCurr = newCurr.next;
        }
        
        return newHead;
    }
    
    /*
     * 题目扩展：剑指Offer - 复杂链表的复制
     * 来源：剑指Offer、LeetCode 138
     * 
     * 题目描述：
     * 请实现 copyRandomList 函数，复制一个复杂链表。在复杂链表中，每个节点除了有一个 next 指针指向下一个节点，
     * 还有一个 random 指针指向链表中的任意节点或者 null。
     * 
     * 解题思路（哈希表法）：
     * 1. 使用哈希表记录原节点和新节点的对应关系
     * 2. 第一次遍历：创建所有新节点，并存储映射关系
     * 3. 第二次遍历：根据映射关系连接next和random指针
     * 
     * 时间复杂度：O(n) - 需要遍历链表两次
     * 空间复杂度：O(n) - 需要哈希表存储映射关系
     * 
     * 解题思路（原地复制法）：
     * 1. 第一次遍历：在每个原节点后插入对应的新节点
     * 2. 第二次遍历：处理新节点的random指针
     * 3. 第三次遍历：分离两个链表，恢复原链表，构建新链表
     * 
     * 时间复杂度：O(n) - 需要遍历链表三次
     * 空间复杂度：O(1) - 不需要额外空间（除了新链表节点）
     * 
     * 最优解：两种方法各有优劣，哈希表法更直观，原地复制法空间效率更高
     * 
     * 工程化考量：
     * 1. 边界情况处理：空链表
     * 2. 异常处理：确保random指针可能为null的情况
     * 3. 代码可读性：哈希表法更易理解，原地复制法需要更仔细的指针操作
     * 4. 性能优化：原地复制法空间效率更高
     * 
     * 与机器学习等领域的联系：
     * 1. 在图神经网络中，节点复制是基础操作
     * 2. 在数据结构序列化中，类似的深拷贝操作很常见
     * 
     * 语言特性差异：
     * Java: 使用HashMap存储映射关系
     * C++: 可以使用unordered_map或直接操作指针
     * Python: 可以使用字典或利用语言特性简化深拷贝
     * 
     * 极端输入场景：
     * 1. 空链表：返回null
     * 2. 只有一个节点的链表
     * 3. 链表中存在循环引用
     * 4. 大量节点且random指针随机分布
     */
    
    // 辅助方法：构建复杂链表用于测试
    public static Node buildComplexList(int[][] nodes) {
        if (nodes == null || nodes.length == 0) {
            return null;
        }
        
        // 创建所有节点
        Node[] nodeArray = new Node[nodes.length];
        for (int i = 0; i < nodes.length; i++) {
            nodeArray[i] = new Node(nodes[i][0]);
        }
        
        // 连接next和random指针
        for (int i = 0; i < nodes.length; i++) {
            if (i < nodes.length - 1) {
                nodeArray[i].next = nodeArray[i + 1];
            }
            int randomIndex = nodes[i][1];
            if (randomIndex != -1) {
                nodeArray[i].random = nodeArray[randomIndex];
            }
        }
        
        return nodeArray[0];
    }
    
    // 辅助方法：打印链表
    public static String printList(Node head) {
        StringBuilder sb = new StringBuilder();
        Map<Node, Integer> nodeToIndex = new HashMap<>();
        Node curr = head;
        int index = 0;
        
        // 记录每个节点的索引
        while (curr != null) {
            nodeToIndex.put(curr, index);
            curr = curr.next;
            index++;
        }
        
        // 打印链表
        curr = head;
        while (curr != null) {
            sb.append("[val=").append(curr.val);
            if (curr.random != null) {
                sb.append(", random->").append(nodeToIndex.get(curr.random));
            } else {
                sb.append(", random->null");
            }
            sb.append("]");
            if (curr.next != null) {
                sb.append(" -> ");
            }
            curr = curr.next;
        }
        
        return sb.toString();
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例: [[7,null],[13,0],[11,4],[10,2],[1,0]]
        int[][] nodes1 = { {7, -1}, {13, 0}, {11, 4}, {10, 2}, {1, 0} };
        Node head1 = buildComplexList(nodes1);
        System.out.println("原始链表1: " + printList(head1));
        
        // 测试哈希表方法
        Node result1 = copyRandomList(head1);
        System.out.println("复制后的链表1: " + printList(result1));
        
        // 测试用例: [[1,1],[2,1]]
        int[][] nodes2 = { {1, 1}, {2, 1} };
        Node head2 = buildComplexList(nodes2);
        System.out.println("\n原始链表2: " + printList(head2));
        
        // 测试原地复制方法
        Node result2 = copyRandomListInPlace(head2);
        System.out.println("复制后的链表2: " + printList(result2));
        System.out.println("复制后原始链表2: " + printList(head2)); // 验证原链表未被破坏
        
        // 测试用例: [[3,null],[3,0],[3,null]]
        int[][] nodes3 = { {3, -1}, {3, 0}, {3, -1} };
        Node head3 = buildComplexList(nodes3);
        System.out.println("\n原始链表3: " + printList(head3));
        Node result3 = copyRandomList(head3);
        System.out.println("复制后的链表3: " + printList(result3));
    }
}