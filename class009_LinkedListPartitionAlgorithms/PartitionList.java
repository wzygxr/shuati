/**
 * 链表分隔问题 - 最优解实现与深度解析
 * 
 * <p><b>题目来源</b>：LeetCode 86. Partition List
 * <p><b>题目链接</b>：https://leetcode.cn/problems/partition-list/
 * 
 * <h3>题目描述</h3>
 * 给你一个链表的头节点 head 和一个特定值 x，请你对链表进行分隔，
 * 使得所有小于 x 的节点都出现在大于或等于 x 的节点之前。
 * 你应当保留两个分区中每个节点的初始相对位置。
 * 
 * <h3>示例</h3>
 * 输入：head = [1,4,3,2,5,2], x = 3
 * 输出：[1,2,2,4,3,5]
 * 
 * 输入：head = [2,1], x = 2
 * 输出：[1,2]
 * 
 * <h3>算法本质</h3>
 * 链表分隔问题本质上是一个<b>分类与合并问题</b>，通过虚拟头节点技术和精确的指针操作，
 * 将一个线性结构按照条件划分为两个子结构，同时保持元素的相对顺序不变。
 * 这种思想在计算机科学中广泛应用，从算法设计到系统架构都有体现。
 * 
 * <h3>解题思路</h3>
 * 1. <b>双链表法（推荐最优解）</b>：使用两个链表分别存储小于x和大于等于x的节点，最后连接
 * 2. <b>原地操作法</b>：在原链表中移动节点，保持相对顺序
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li>时间复杂度：O(n) - 只需遍历链表一次
 *   <li>空间复杂度：O(1) - 只使用常数级别额外空间
 * </ul>
 * 
 * <h3>与机器学习/深度学习的联系</h3>
 * <ul>
 *   <li><b>数据预处理</b>：特征离散化和数据清洗中常用的分区策略类似
 *   <li><b>小批量梯度下降</b>：将大规模数据分批处理的思想一致
 *   <li><b>注意力机制</b>：选择性地连接重要信息，类似于链表按条件连接节点
 *   <li><b>序列处理</b>：在NLP中对序列元素进行分类并保持顺序
 * </ul>
 * 
 * <h3>扩展应用</h3>
 * 此算法在实际工程中的应用包括：
 * <ul>
 *   <li>数据流过滤与路由
 *   <li>任务调度与优先级队列实现
 *   <li>内存管理中的块分配策略
 *   <li>分布式系统中的数据分片
 * </ul>
 */
public class PartitionList {
    /**
     * 运行所有测试用例的主方法
     */
    public static void runTestCases() {
        System.out.println("=== Partition List Test ===");
        System.out.println("Algorithm: Classification and Merge Pattern");
        
        // Test Case 1: Standard case - mixed elements
        System.out.println("Test Case 1: Standard Case");
        int[] arr1 = {1, 4, 3, 2, 5, 2};
        ListNode head1 = ListNode.createList(arr1);
        
        System.out.print("Original List: ");
        printList(head1);
        
        ListNode result1 = partition(head1, 3);
        System.out.print("After Partition: ");
        printList(result1);
        
        verifyPartitionResult(result1, 3);
        
        // Test Case 2: Two nodes need swap
        System.out.println("Test Case 2: Two Nodes");
        int[] arr2 = {2, 1};
        ListNode head2 = ListNode.createList(arr2);
        
        System.out.print("Original List: ");
        printList(head2);
        
        ListNode result3 = partition(head2, 2);
        System.out.print("After Partition: ");
        printList(result3);
        
        // Test Case 3: Empty list
        System.out.println("Test Case 3: Empty List");
        ListNode head3 = null;
        
        System.out.print("Original List: ");
        printList(head3);
        
        ListNode result4 = partition(head3, 1);
        System.out.print("After Partition: ");
        printList(result4);
        
        // Test Case 4: Single node
        System.out.println("Test Case 4: Single Node");
        int[] arr4 = {5};
        ListNode head4 = ListNode.createList(arr4);
        
        System.out.print("Original List: ");
        printList(head4);
        
        ListNode result5 = partition(head4, 3);
        System.out.print("After Partition (x=3): ");
        printList(result5);
        
        // Test Case 5: All nodes less than x
        System.out.println("Test Case 5: All Nodes < x");
        int[] arr5 = {1, 2, 3};
        ListNode head5 = ListNode.createList(arr5);
        
        System.out.print("Original List: ");
        printList(head5);
        
        ListNode result6 = partition(head5, 4);
        System.out.print("After Partition (x=4): ");
        printList(result6);
        
        // Test Case 6: All nodes >= x
        System.out.println("Test Case 6: All Nodes >= x");
        int[] arr6 = {5, 6, 7};
        ListNode head6 = ListNode.createList(arr6);
        
        System.out.print("Original List: ");
        printList(head6);
        
        ListNode result7 = partition(head6, 4);
        System.out.print("After Partition (x=4): ");
        printList(result7);
        
        // Test Case 7: Sorted list
        System.out.println("Test Case 7: Sorted List");
        int[] arr7 = {1, 2, 3, 4, 5};
        ListNode head7 = ListNode.createList(arr7);
        
        System.out.print("Original List: ");
        printList(head7);
        
        ListNode result8 = partition(head7, 3);
        System.out.print("After Partition (x=3): ");
        printList(result8);
        
        // Test Case 8: Reverse sorted list
        System.out.println("Test Case 8: Reverse Sorted List");
        int[] arr8 = {5, 4, 3, 2, 1};
        ListNode head8 = ListNode.createList(arr8);
        
        System.out.print("Original List: ");
        printList(head8);
        
        ListNode result9 = partition(head8, 3);
        System.out.print("After Partition (x=3): ");
        printList(result9);
        
        // Extended Problems
        System.out.println("Extended Problems");
        
        // Extended Test 1: LeetCode 328 - Odd Even Linked List
        System.out.println("Extended Test 1: LeetCode 328");
        int[] arr9 = {1, 2, 3, 4, 5};
        ListNode head9 = ListNode.createList(arr9);
        System.out.print("Original List: ");
        printList(head9);
        ListNode result10 = oddEvenList(head9);
        System.out.print("After Odd-Even: ");
        printList(result10);
        
        // Extended Test 2: LeetCode 725 - Split Linked List in Parts
        System.out.println("Extended Test 2: LeetCode 725");
        int[] arr11 = {1, 2, 3};
        ListNode head11 = ListNode.createList(arr11);
        System.out.print("Original List: ");
        printList(head11);
        ListNode[] parts1 = splitListToParts(head11, 5);
        System.out.println("Split into 5 parts:");
        for (int i = 0; i < parts1.length; i++) {
            System.out.print("Part " + (i + 1) + ": ");
            printList(parts1[i]);
        }
        
        // Extended Test 3: LeetCode 2095 - Delete Middle Node
        System.out.println("Extended Test 3: LeetCode 2095");
        int[] arr13 = {1, 3, 4, 7, 1, 2, 6};
        ListNode head13 = ListNode.createList(arr13);
        System.out.print("Original List: ");
        printList(head13);
        ListNode result12 = deleteMiddle(head13);
        System.out.print("After Delete Middle: ");
        printList(result12);
        
        System.out.println("All Tests Completed");
    }



    /**
     * 链表节点类定义
     * 在LeetCode提交时，此类由系统提供，不需要提交
     */
    public static class ListNode {
        public int val;         // 节点值
        public ListNode next;   // 指向下一节点的引用

        /**
         * 构造函数 - 创建单个节点
         * @param val 节点值
         */
        public ListNode(int val) {
            this.val = val;
        }

        /**
         * 构造函数 - 创建节点并指定后继节点
         * @param val 节点值
         * @param next 后继节点
         */
        public ListNode(int val, ListNode next) {
            this.val = val;
            this.next = next;
        }
        
        /**
         * 从数组创建链表的静态方法（用于测试）
         * @param arr 整数数组
         * @return 创建的链表头节点
         */
        public static ListNode createList(int[] arr) {
            if (arr == null || arr.length == 0) {
                return null;
            }
            
            ListNode head = new ListNode(arr[0]);
            ListNode current = head;
            
            for (int i = 1; i < arr.length; i++) {
                current.next = new ListNode(arr[i]);
                current = current.next;
            }
            
            return head;
        }
    }

    /**
     * 解法1：双链表法（推荐最优解）
     * 
     * <h4>核心思想</h4>
     * 1. 创建两个虚拟头节点，分别用于收集小于x和大于等于x的节点
     * 2. 遍历原链表，根据节点值将节点连接到对应的链表中
     * 3. 连接两个链表并返回结果
     * 
     * <h4>算法设计模式</h4>
     * - <b>虚拟节点模式</b>：使用虚拟头节点消除边界情况处理，简化代码
     * - <b>双指针追踪模式</b>：分别维护两个链表的尾指针，实现O(1)时间的节点添加
     * - <b>分类与合并模式</b>：先分类处理，再合并结果
     * 
     * <h4>实现要点</h4>
     * - <b>提前保存下一个节点</b>：确保在修改当前节点的next指针后不会丢失链表后续部分
     * - <b>断开原连接</b>：避免形成环引用
     * - <b>指针更新顺序</b>：遵循"先连接，后更新指针"的原则
     * 
     * <h4>此解法的优势</h4>
     * - 逻辑清晰，易于理解和实现
     * - 边界条件处理简单，不容易出错
     * - 满足O(n)时间复杂度和O(1)空间复杂度要求
     * - 代码可读性高，易于维护
     * 
     * <h4>复杂度分析</h4>
     * <ul>
     *   <li>时间复杂度：O(n) - 只需要遍历原链表一次，每个节点执行常数次操作
     *   <li>空间复杂度：O(1) - 只使用常数个额外指针变量，不使用额外数据结构
     * </ul>
     * 
     * <h4>工程化考量</h4>
     * - <b>健壮性</b>：对空链表进行处理
     * - <b>防错设计</b>：断开节点原连接防止循环引用
     * - <b>代码可读性</b>：清晰的变量命名和详细注释
     * 
     * @param head 链表头节点
     * @param x 分隔值
     * @return 分隔后的链表头节点
     * @throws NullPointerException 当head为空时返回null（已处理）
     */
    public static ListNode partition(ListNode head, int x) {
        // 【异常处理】空链表直接返回null
        if (head == null) {
            return null;
        }
        
        // 创建两个虚拟头节点，分别用于存储小于x和大于等于x的节点
        // 使用虚拟头节点可以避免处理头节点为空的边界情况
        ListNode leftDummy = new ListNode(0);
        ListNode rightDummy = new ListNode(0);
        
        // 两个链表的尾指针，用于高效添加节点
        ListNode leftTail = leftDummy;
        ListNode rightTail = rightDummy;
        
        // 遍历原链表
        while (head != null) {
            // 【关键点】提前保存下一个节点，避免在操作当前节点时丢失链表后续部分
            ListNode next = head.next;
            
            // 【重要】断开当前节点与原链表的连接，防止形成环
            head.next = null;
            
            // 根据节点值将节点连接到对应的链表中
            if (head.val < x) {
                // 小于x的节点连接到左侧链表
                leftTail.next = head;
                leftTail = head;  // 更新左侧链表尾指针
            } else {
                // 大于等于x的节点连接到右侧链表
                rightTail.next = head;
                rightTail = head;  // 更新右侧链表尾指针
            }
            
            // 移动到下一个节点
            head = next;
        }
        
        // 【关键点】连接两个链表：将左侧链表的尾部连接到右侧链表的头部
        leftTail.next = rightDummy.next;
        
        // 返回结果链表的头节点（左侧链表的第一个有效节点）
        return leftDummy.next;
    }

    /**
     * 解法2：原地操作法
     * 
     * 核心思想：
     * 1. 使用一个指针遍历链表
     * 2. 遇到小于x的节点就将其移动到前面
     * 3. 保持相对顺序不变
     * 
     * 这种方法虽然也是O(n)时间复杂度和O(1)空间复杂度，
     * 但实现更复杂，且容易在指针操作中出错
     * 
     * @param head 链表头节点
     * @param x 分隔值
     * @return 分隔后的链表头节点
     */
    public static ListNode partition2(ListNode head, int x) {
        // 【异常处理】空链表直接返回null
        if (head == null) {
            return null;
        }
        
        // 创建虚拟头节点，简化边界处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 找到第一个大于等于x的节点的前驱节点
        // 这个节点将作为小于x的节点插入位置的前驱
        ListNode prev = dummy;
        while (prev.next != null && prev.next.val < x) {
            prev = prev.next;
        }
        
        // 当前节点指针，用于遍历链表
        ListNode curr = prev;
        
        // 遍历链表剩余部分
        while (curr.next != null) {
            // 如果下一个节点小于x，则需要将其移动到前面
            if (curr.next.val < x) {
                // 【指针操作】取出要移动的节点
                ListNode moveNode = curr.next;
                
                // 从当前位置断开
                curr.next = moveNode.next;
                
                // 插入到prev后面
                moveNode.next = prev.next;
                prev.next = moveNode;
                
                // 更新prev指针，为下一次插入做准备
                prev = moveNode;
            } else {
                // 否则继续向后移动
                curr = curr.next;
            }
        }
        
        return dummy.next;
    }

    /**
     * 打印链表的辅助方法
     * @param head 链表头节点
     */
    public static void printList(ListNode head) {
        // 边界检查
        if (head == null) {
            System.out.println("空链表");
            return;
        }
        
        ListNode current = head;
        while (current != null) {
            System.out.print(current.val);
            if (current.next != null) {
                System.out.print(" -> ");
            }
            current = current.next;
        }
        System.out.println();
    }
    
    /**
     * 验证链表分隔结果是否正确
     * 
     * <h4>验证规则</h4>
     * 1. 所有小于x的节点必须出现在大于等于x的节点之前
     * 2. 必须保持节点的相对顺序
     * 3. 不能出现循环引用
     * 
     * <h4>实现思路</h4>
     * - 使用状态标志跟踪是否已经遇到大于等于x的节点
     * - 遍历链表检查是否违反分区规则
     * - 同时检查是否存在循环（通过记录访问过的节点数量限制）
     * 
     * @param head 分隔后的链表头节点
     * @param x 分隔值
     * @return 验证是否通过
     */
    public static boolean verifyPartitionResult(ListNode head, int x) {
        if (head == null) {
            System.out.println("验证结果：通过（空链表）");
            return true;
        }
        
        boolean passedX = false; // 标志是否已经遇到大于等于x的节点
        ListNode current = head;
        int nodeCount = 0; // 用于检测循环引用
        int maxNodes = 1000; // 链表最大节点数限制，防止无限循环
        
        while (current != null && nodeCount < maxNodes) {
            // 检查分区规则：如果已经遇到过大于等于x的节点，则后续节点都不能小于x
            if (passedX && current.val < x) {
                System.out.println("验证结果：失败！违反分区规则 - 大于等于x的节点后出现小于x的节点");
                return false;
            }
            
            // 如果当前节点大于等于x，则设置passedX标志为true
            if (current.val >= x) {
                passedX = true;
            }
            
            current = current.next;
            nodeCount++;
        }
        
        // 检查是否存在循环引用
        if (nodeCount >= maxNodes) {
            System.out.println("验证结果：失败！检测到可能的循环引用");
            return false;
        }
        
        System.out.println("验证结果：通过 - 分区规则正确遵守");
        return true;
    }
    
    // ========== 扩展题目1：LeetCode 328. Odd Even Linked List（链表奇偶重排）==========
    /**
     * LeetCode 328. Odd Even Linked List
     * 题目链接：https://leetcode.cn/problems/odd-even-linked-list/
     * 
     * 题目描述：
     * 给定单链表的头节点head，将所有索引为奇数的节点和索引为偶数的节点分别组合在一起，然后返回重新排序的列表。
     * 第一个节点的索引被认为是奇数，第二个节点的索引为偶数，以此类推。
     * 请注意，偶数组和奇数组内部的相对顺序应该与输入时保持一致。
     * 
     * 示例：
     * 输入: head = [1,2,3,4,5]
     * 输出: [1,3,5,2,4]
     * 
     * 输入: head = [2,1,3,5,6,4,7]
     * 输出: [2,3,6,7,1,5,4]
     * 
     * 解题思路：
     * 使用链表分隔的双指针思想，将奇数索引节点和偶数索引节点分别连接成两个链表，最后合并。
     * 
     * 时间复杂度：O(n) - 遍历链表一次
     * 空间复杂度：O(1) - 只使用常数额外空间
     * 
     * 是否最优解：是，时间和空间复杂度都已达到最优
     */
    public static ListNode oddEvenList(ListNode head) {
        // 边界检查：空链表或单节点链表直接返回
        if (head == null || head.next == null) {
            return head;
        }
        
        // odd指向奇数索引节点的尾部，even指向偶数索引节点的尾部
        ListNode odd = head;
        ListNode even = head.next;
        ListNode evenHead = even;  // 保存偶数链表的头节点用于最后连接
        
        // 遍历链表，交替处理奇数和偶数节点
        // 循环条件：偶数节点存在且其后继节点存在（因为偶数节点总是在奇数节点后面）
        while (even != null && even.next != null) {
            // 将下一个奇数节点连接到odd后面
            odd.next = even.next;
            odd = odd.next;
            
            // 将下一个偶数节点连接到even后面
            even.next = odd.next;
            even = even.next;
        }
        
        // 将偶数链表连接到奇数链表尾部
        odd.next = evenHead;
        
        return head;
    }

    // ========== 扩展题目2：LeetCode 725. Split Linked List in Parts（分隔链表）==========
    /**
     * LeetCode 725. Split Linked List in Parts
     * 题目链接：https://leetcode.cn/problems/split-linked-list-in-parts/
     * 
     * 题目描述：
     * 给你一个头结点为head的单链表和一个整数k，请你设计一个算法将链表分隔为k个连续的部分。
     * 每部分的长度应该尽可能的相等：任意两部分的长度差距不能超过1。这可能会导致有些部分为null。
     * 这k个部分应该按照在链表中出现的顺序排列，并且排在前面的部分的长度应该大于或等于排在后面的长度。
     * 返回一个由上述k部分组成的数组。
     * 
     * 示例：
     * 输入：head = [1,2,3], k = 5
     * 输出：[[1],[2],[3],[],[]]
     * 
     * 输入：head = [1,2,3,4,5,6,7,8,9,10], k = 3
     * 输出：[[1,2,3,4],[5,6,7],[8,9,10]]
     * 
     * 解题思路：
     * 1. 先计算链表总长度n
     * 2. 计算每部分的基本长度：n/k，以及需要多分配节点的部分数：n%k
     * 3. 遍历链表，按计算的长度分隔
     * 
     * 时间复杂度：O(n+k) - n为链表长度，k为分隔数
     * 空间复杂度：O(k) - 返回数组的空间
     * 
     * 是否最优解：是
     */
    public static ListNode[] splitListToParts(ListNode head, int k) {
        // 计算链表长度
        int length = 0;
        ListNode curr = head;
        while (curr != null) {
            length++;
            curr = curr.next;
        }
        
        // 计算每部分的基本大小和需要额外节点的部分数
        int partSize = length / k;  // 每部分的基本大小
        int remainder = length % k;  // 前remainder个部分需要多一个节点
        
        // 结果数组
        ListNode[] result = new ListNode[k];
        curr = head;
        
        for (int i = 0; i < k && curr != null; i++) {
            result[i] = curr;  // 当前部分的头节点
            
            // 当前部分的大小：基本大小 + (如果在前remainder个则+1)
            int currentPartSize = partSize + (i < remainder ? 1 : 0);
            
            // 移动到当前部分的最后一个节点
            for (int j = 1; j < currentPartSize; j++) {
                curr = curr.next;
            }
            
            // 断开当前部分与下一部分的连接
            ListNode next = curr.next;
            curr.next = null;
            curr = next;
        }
        
        return result;
    }

    // ========== 扩展题目3：LeetCode 2095. Delete the Middle Node of a Linked List（删除链表中间节点）==========
    /**
     * LeetCode 2095. Delete the Middle Node of a Linked List
     * 题目链接：https://leetcode.cn/problems/delete-the-middle-node-of-a-linked-list/
     * 
     * 题目描述：
     * 给你一个链表的头节点head。删除链表的中间节点，并返回修改后的链表的头节点head。
     * 长度为n链表的中间节点是从头数起第⌊n/2⌋个节点（下标从0开始）
     * 
     * 示例：
     * 输入：head = [1,3,4,7,1,2,6]
     * 输出：[1,3,4,1,2,6]
     * 
     * 输入：head = [1,2,3,4]
     * 输出：[1,2,4]
     * 
     * 解题思路：
     * 使用快慢指针，快指针每次走两步，慢指针每次走一步，当快指针到达末尾时，慢指针指向中间节点。
     * 需要额外维护一个prev指针指向慢指针的前驱节点，用于删除中间节点。
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(1)
     * 
     * 是否最优解：是
     */
    public static ListNode deleteMiddle(ListNode head) {
        // 边界情况：空链表或单节点链表
        if (head == null || head.next == null) {
            return null;
        }
        
        // 使用虚拟头节点简化操作
        ListNode dummy = new ListNode(0, head);
        ListNode slow = dummy;
        ListNode fast = head;
        
        // 快慢指针移动，当fast到达末尾时，slow的next就是中间节点
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 删除中间节点
        slow.next = slow.next.next;
        
        return dummy.next;
    }

    /**
     * 主测试方法 - 包含全面的测试用例和验证策略
     * 
     * <h4>测试策略</h4>
     * 1. <b>功能验证</b>：确保算法正确实现了分隔功能
     * 2. <b>边界测试</b>：测试特殊输入情况
     * 3. <b>极端情况测试</b>：测试性能和正确性边界
     * 4. <b>多解法对比</b>：验证不同实现方法的正确性
     * 5. <b>结果验证</b>：确保所有分隔后的链表满足条件
     * 
     * <h4>测试覆盖范围</h4>
     * - 标准情况
     * - 边界情况（空链表、单节点链表）
     * - 特殊情况（全小于/大于x的链表）
     * - 已排序/逆序链表
     * - 重复值链表
     */
    public static void main(String[] args) {
        System.out.println("=== 链表分隔问题测试 ===");
        System.out.println("算法本质：分类与合并模式，使用虚拟头节点技术");
        
        // 【测试用例1】标准情况 - 混合大小的元素分布
        // 输入：[1,4,3,2,5,2], x = 3
        // 预期输出: [1,2,2,4,3,5]
        // 验证点：1. 小于x的节点在前 2. 大于等于x的节点在后 3. 相对顺序保持不变
        System.out.println("\n【测试用例1】标准情况 - 混合元素分布");
        System.out.println("测试目的：验证基本功能正确性，确保相对顺序保持不变");
        int[] arr1 = {1, 4, 3, 2, 5, 2};
        ListNode head1 = ListNode.createList(arr1);
        
        System.out.print("原链表: ");
        printList(head1);
        
        ListNode result1 = partition(head1, 3);
        System.out.print("分隔后 (双链表法): ");
        printList(result1);
        
        // 验证结果正确性
        verifyPartitionResult(result1, 3);
        
        // 重新构建测试用例，测试解法2
        head1 = ListNode.createList(arr1);
        ListNode result2 = partition2(head1, 3);
        System.out.print("分隔后 (原地操作法): ");
        printList(result2);
        
        // 验证结果正确性
        verifyPartitionResult(result2, 3);
        
        // 测试用例2: 两个节点，需要交换
        // 输入：[2,1], x = 2
        // 预期输出: [1,2]
        System.out.println("\n测试用例2: 两个节点需要交换");
        int[] arr2 = {2, 1};
        ListNode head2 = ListNode.createList(arr2);
        
        System.out.print("原链表: ");
        printList(head2);
        
        ListNode result3 = partition(head2, 2);
        System.out.print("分隔后: ");
        printList(result3);
        
        // 测试用例3: 空链表
        System.out.println("\n测试用例3: 空链表");
        ListNode head3 = null;
        
        System.out.print("原链表: ");
        printList(head3);
        
        ListNode result4 = partition(head3, 1);
        System.out.print("分隔后: ");
        printList(result4);
        
        // 测试用例4: 单节点链表
        System.out.println("\n测试用例4: 单节点链表");
        int[] arr4 = {5};
        ListNode head4 = ListNode.createList(arr4);
        
        System.out.print("原链表: ");
        printList(head4);
        
        ListNode result5 = partition(head4, 3);
        System.out.print("分隔后 (x=3): ");
        printList(result5);
        
        // 测试用例5: 所有节点值都小于x
        System.out.println("\n测试用例5: 所有节点值都小于x");
        int[] arr5 = {1, 2, 3};
        ListNode head5 = ListNode.createList(arr5);
        
        System.out.print("原链表: ");
        printList(head5);
        
        ListNode result6 = partition(head5, 4);
        System.out.print("分隔后 (x=4): ");
        printList(result6);
        
        // 测试用例6: 所有节点值都大于等于x
        System.out.println("\n测试用例6: 所有节点值都大于等于x");
        int[] arr6 = {5, 6, 7};
        ListNode head6 = ListNode.createList(arr6);
        
        System.out.print("原链表: ");
        printList(head6);
        
        ListNode result7 = partition(head6, 4);
        System.out.print("分隔后 (x=4): ");
        printList(result7);
        
        // 测试用例7: 已排序的链表
        System.out.println("\n测试用例7: 已排序的链表");
        int[] arr7 = {1, 2, 3, 4, 5};
        ListNode head7 = ListNode.createList(arr7);
        
        System.out.print("原链表: ");
        printList(head7);
        
        ListNode result8 = partition(head7, 3);
        System.out.print("分隔后 (x=3): ");
        printList(result8);
        
        // 测试用例8: 逆序的链表
        System.out.println("\n测试用例8: 逆序的链表");
        int[] arr8 = {5, 4, 3, 2, 1};
        ListNode head8 = ListNode.createList(arr8);
        
        System.out.print("原链表: ");
        printList(head8);
        
        ListNode result9 = partition(head8, 3);
        System.out.print("分隔后 (x=3): ");
        printList(result9);
        
        // ========== 扩展题目测试 ==========
        System.out.println("\n========== 扩展题目测试 ==========");
        
        // 测试1: LeetCode 328 - 链表奇偶重排
        System.out.println("\n【扩展测试1】LeetCode 328 - Odd Even Linked List");
        int[] arr9 = {1, 2, 3, 4, 5};
        ListNode head9 = ListNode.createList(arr9);
        System.out.print("原链表: ");
        printList(head9);
        ListNode result10 = oddEvenList(head9);
        System.out.print("奇偶重排后: ");
        printList(result10);
        
        int[] arr10 = {2, 1, 3, 5, 6, 4, 7};
        ListNode head10 = ListNode.createList(arr10);
        System.out.print("\n原链表: ");
        printList(head10);
        ListNode result11 = oddEvenList(head10);
        System.out.print("奇偶重排后: ");
        printList(result11);
        
        // 测试2: LeetCode 725 - 分隔链表为多部分
        System.out.println("\n【扩展测试2】LeetCode 725 - Split Linked List in Parts");
        int[] arr11 = {1, 2, 3};
        ListNode head11 = ListNode.createList(arr11);
        System.out.print("原链表: ");
        printList(head11);
        ListNode[] parts1 = splitListToParts(head11, 5);
        System.out.println("分隔为5部分:");
        for (int i = 0; i < parts1.length; i++) {
            System.out.print("部分" + (i + 1) + ": ");
            printList(parts1[i]);
        }
        
        int[] arr12 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        ListNode head12 = ListNode.createList(arr12);
        System.out.print("\n原链表: ");
        printList(head12);
        ListNode[] parts2 = splitListToParts(head12, 3);
        System.out.println("分隔为3部分:");
        for (int i = 0; i < parts2.length; i++) {
            System.out.print("部分" + (i + 1) + ": ");
            printList(parts2[i]);
        }
        
        // 测试3: LeetCode 2095 - 删除链表中间节点
        System.out.println("\n【扩展测试3】LeetCode 2095 - Delete Middle Node");
        int[] arr13 = {1, 3, 4, 7, 1, 2, 6};
        ListNode head13 = ListNode.createList(arr13);
        System.out.print("原链表: ");
        printList(head13);
        ListNode result12 = deleteMiddle(head13);
        System.out.print("删除中间节点后: ");
        printList(result12);
        
        int[] arr14 = {1, 2, 3, 4};
        ListNode head14 = ListNode.createList(arr14);
        System.out.print("\n原链表: ");
        printList(head14);
        ListNode result13 = deleteMiddle(head14);
        System.out.print("删除中间节点后: ");
        printList(result13);
        
        System.out.println("\n========== 所有测试完成 ==========");
    }
}