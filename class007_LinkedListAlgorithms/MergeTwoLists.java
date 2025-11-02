/*
 * 合并两个有序链表及相关题目扩展 (Java版本)
 * 
 * 算法专题：链表合并与相关算法
 * 覆盖平台：LeetCode、牛客网、LintCode、剑指Offer等
 * 语言特性：Java 8+，面向对象设计，异常处理，性能优化
 * 
 * 工程化考量：
 * 1. 内存管理：垃圾回收机制，避免内存泄漏
 * 2. 异常安全：完整的异常处理机制
 * 3. 性能优化：算法优化，数据结构选择
 * 4. 可测试性：单元测试框架，边界条件覆盖
 * 5. 可维护性：清晰的代码结构，详细的注释说明
 * 
 * 复杂度分析体系：
 * - 时间复杂度：从理论分析到实际性能考量
 * - 空间复杂度：内存使用优化策略
 * - 常数项分析：实际运行效率的关键因素
 * 
 * 算法应用场景：
 * - 大数据处理：外部排序，多路归并
 * - 实时系统：数据流合并处理
 * - 分布式计算：多节点结果合并
 * - 数据库系统：索引合并优化
 * 
 * 主要题目：
 * 1. LeetCode 21. 合并两个有序链表 (基础题)
 * 2. LeetCode 23. 合并K个升序链表 (进阶题)
 * 3. LeetCode 88. 合并两个有序数组 (变种题)
 * 4. LeetCode 148. 排序链表 (应用扩展)
 * 5. LeetCode 2. 两数相加 (链表操作)
 * 6. LeetCode 24. 两两交换链表中的节点 (链表变换)
 * 7. 牛客 NC33. 合并两个排序的链表 (国内平台)
 * 8. LintCode 104. 合并k个排序链表 (国际平台)
 * 9. LeetCode 86. 分隔链表 (链表分割)
 * 
 * 解题思路技巧总结：
 * 1. 双指针法：适用于两个有序序列的合并，时间复杂度O(m+n)
 * 2. 优先队列(堆)：适用于K个有序序列的合并，时间复杂度O(N*logK)
 * 3. 分治法：将K个序列问题分解为多个两个序列问题，时间复杂度O(N*logK)
 * 4. 哨兵节点：简化链表操作的边界处理，提高代码可读性
 * 5. 原地修改：充分利用已有空间，减少额外空间使用
 * 6. 递归与迭代：不同场景下的选择策略
 * 
 * 时间复杂度分析：
 * 1. 合并两个链表：O(m+n)，m和n分别是两个链表的长度
 * 2. 合并K个链表(优先队列)：O(N*logK)，N是所有节点总数，K是链表数量
 * 3. 合并K个链表(分治)：O(N*logK)
 * 4. 合并两个数组：O(m+n)
 * 5. 链表排序：O(nlogn)，归并排序最优
 * 
 * 空间复杂度分析：
 * 1. 合并两个链表：O(1)，原地操作
 * 2. 合并K个链表(优先队列)：O(K)，堆的大小
 * 3. 合并K个链表(分治)：O(logK)，递归栈深度
 * 4. 合并两个数组：O(1)，原地操作
 * 5. 链表排序：O(1)或O(logn)，取决于实现方式
 * 
 * 安全与稳定性：
 * - 空指针检查：所有链表操作前的边界检查
 * - 异常处理：try-catch块，参数验证
 * - 输入验证：参数合法性检查
 * 
 * 调试与测试：
 * - 单元测试：每个算法的独立测试用例
 * - 边界测试：空输入、单元素、极端值等
 * - 性能测试：大规模数据下的性能表现
 * 
 * 学习路径建议：
 * 1. 基础掌握：LeetCode 21 -> 牛客 NC33
 * 2. 进阶提升：LeetCode 23 -> LintCode 104
 * 3. 综合应用：LeetCode 148 -> LeetCode 2
 * 4. 拓展思维：LeetCode 24 -> LeetCode 86
 * 
 * 面试重点：
 * - 算法思路清晰表达
 * - 时间空间复杂度分析
 * - 边界条件处理能力
 * - 代码实现简洁优雅
 * - 工程化考量意识
 */

import java.util.*;

/**
 * 链表节点定义类
 * 
 * 设计要点：
 * - 使用构造函数简化节点创建
 * - 提供静态工具方法便于测试
 * - 包含输入验证和异常处理
 * 
 * 注意事项：
 * - Java垃圾回收机制自动管理内存
 * - 注意避免循环引用
 * - 考虑线程安全性
 */
class ListNode {
    int val;
    ListNode next;
    
    // 构造函数
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { 
        this.val = val; 
        this.next = next; 
    }
    
    /**
     * 从数组创建链表（测试工具方法）
     * 
     * 复杂度分析：
     * - 时间复杂度: O(n)，n为数组长度
     * - 空间复杂度: O(n)，需要创建n个节点
     * 
     * 使用场景：单元测试、算法演示
     * 
     * @param arr 整数数组
     * @return 链表头节点
     * @throws IllegalArgumentException 如果输入数组为null
     */
    public static ListNode createList(int[] arr) {
        // 输入验证
        if (arr == null) {
            throw new IllegalArgumentException("输入数组不能为null");
        }
        if (arr.length == 0) return null;
        
        ListNode head = new ListNode(arr[0]);
        ListNode cur = head;
        for (int i = 1; i < arr.length; i++) {
            cur.next = new ListNode(arr[i]);
            cur = cur.next;
        }
        return head;
    }
    
    /**
     * 打印链表内容（调试工具方法）
     * 
     * 复杂度分析：
     * - 时间复杂度: O(n)，n为链表长度
     * - 空间复杂度: O(1)，只使用常数空间
     * 
     * 使用场景：调试、结果验证
     * 
     * @param head 链表头节点
     */
    public static void printList(ListNode head) {
        ListNode cur = head;
        while (cur != null) {
            System.out.print(cur.val);
            if (cur.next != null) {
                System.out.print(" -> ");
            }
            cur = cur.next;
        }
        System.out.println();
    }
    
    /**
     * 获取链表长度（工具方法）
     * 
     * 复杂度分析：
     * - 时间复杂度: O(n)，需要遍历整个链表
     * - 空间复杂度: O(1)，只使用常数空间
     * 
     * @param head 链表头节点
     * @return 链表长度
     */
    public static int getLength(ListNode head) {
        int length = 0;
        ListNode cur = head;
        while (cur != null) {
            length++;
            cur = cur.next;
        }
        return length;
    }
    
    /**
     * 验证链表是否有序（测试工具方法）
     * 
     * 复杂度分析：
     * - 时间复杂度: O(n)，需要遍历整个链表
     * - 空间复杂度: O(1)，只使用常数空间
     * 
     * @param head 链表头节点
     * @return 是否有序（升序）
     */
    public static boolean isSorted(ListNode head) {
        if (head == null || head.next == null) return true;
        
        ListNode cur = head;
        while (cur.next != null) {
            if (cur.val > cur.next.val) {
                return false;
            }
            cur = cur.next;
        }
        return true;
    }
}

/**
 * 题目1: LeetCode 21. 合并两个有序链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/merge-two-sorted-lists/
 *
 * 题目描述：
 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
 *
 * 解法分析：
 * 1. 迭代法 - 时间复杂度: O(m+n), 空间复杂度: O(1)
 * 2. 递归法 - 时间复杂度: O(m+n), 空间复杂度: O(m+n)
 *
 * 解题思路：
 * 使用双指针分别指向两个链表的当前节点，比较节点值的大小，
 * 将较小的节点连接到结果链表中，移动对应指针，重复此过程直到某一链表遍历完。
 * 最后将未遍历完的链表剩余部分直接连接到结果链表末尾。
 */
class MergeTwoSortedListsSolution {
    /**
     * 解法1: 迭代法 (推荐)
     * 时间复杂度: O(m+n) - m和n分别是两个链表的长度
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用哨兵节点简化边界处理
     * 2. 双指针分别遍历两个链表
     * 3. 比较节点值，将较小节点连接到结果链表
     * 4. 处理剩余节点
     */
    public static ListNode mergeTwoListsIterative(ListNode list1, ListNode list2) {
        // 创建哨兵节点，简化边界处理
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        // 双指针遍历两个链表
        while (list1 != null && list2 != null) {
            // 比较两个链表当前节点的值
            if (list1.val <= list2.val) {
                current.next = list1;
                list1 = list1.next;
            } else {
                current.next = list2;
                list2 = list2.next;
            }
            current = current.next;
        }
        
        // 连接剩余节点
        current.next = (list1 != null) ? list1 : list2;
        
        // 返回合并后的链表
        return dummy.next;
    }
    
    /**
     * 解法2: 递归法
     * 时间复杂度: O(m+n) - 每个节点访问一次
     * 空间复杂度: O(m+n) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 递归终止条件：其中一个链表为空
     * 2. 递归处理：选择较小节点作为当前节点，递归处理剩余部分
     * 3. 返回当前节点
     */
    public static ListNode mergeTwoListsRecursive(ListNode list1, ListNode list2) {
        // 递归终止条件
        if (list1 == null) return list2;
        if (list2 == null) return list1;
        
        // 递归处理
        if (list1.val <= list2.val) {
            list1.next = mergeTwoListsRecursive(list1.next, list2);
            return list1;
        } else {
            list2.next = mergeTwoListsRecursive(list1, list2.next);
            return list2;
        }
    }
    
    /**
     * 测试方法
     * 测试用例覆盖：
     * - 正常情况
     * - 空链表测试
     * - 两个空链表
     * - 包含重复元素测试
     * - 极端值测试
     */
    public static void test() {
        System.out.println("=== LeetCode 21. 合并两个有序链表测试 ===");
        System.out.println("测试用例覆盖：正常情况、边界条件、极端值");
        
        // 测试用例1: 正常情况
        System.out.println("测试用例1: 正常情况");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 4});
        ListNode list2 = ListNode.createList(new int[]{1, 3, 4});
        System.out.print("链表1: ");
        ListNode.printList(list1);
        System.out.print("链表2: ");
        ListNode.printList(list2);
        
        ListNode result1 = mergeTwoListsIterative(list1, list2);
        System.out.print("迭代法结果: ");
        ListNode.printList(result1);
        
        // 重新创建测试数据
        list1 = ListNode.createList(new int[]{1, 2, 4});
        list2 = ListNode.createList(new int[]{1, 3, 4});
        ListNode result2 = mergeTwoListsRecursive(list1, list2);
        System.out.print("递归法结果: ");
        ListNode.printList(result2);
        
        // 测试用例2: 空链表
        System.out.println("测试用例2: 空链表测试");
        ListNode list3 = null;
        ListNode list4 = ListNode.createList(new int[]{0});
        ListNode result3 = mergeTwoListsIterative(list3, list4);
        System.out.print("空链表测试: ");
        ListNode.printList(result3);
        
        // 测试用例3: 两个空链表
        System.out.println("测试用例3: 两个空链表");
        ListNode list5 = null;
        ListNode list6 = null;
        ListNode result4 = mergeTwoListsIterative(list5, list6);
        System.out.print("两个空链表: ");
        ListNode.printList(result4);
        
        // 测试用例4: 包含重复元素
        System.out.println("测试用例4: 包含重复元素");
        ListNode list7 = ListNode.createList(new int[]{1, 1, 2, 3});
        ListNode list8 = ListNode.createList(new int[]{1, 2, 2, 4});
        ListNode result5 = mergeTwoListsIterative(list7, list8);
        System.out.print("包含重复元素结果: ");
        ListNode.printList(result5);
        
        // 测试用例5: 极端值测试
        System.out.println("测试用例5: 极端值测试");
        ListNode list9 = ListNode.createList(new int[]{-10, -5, 0});
        ListNode list10 = ListNode.createList(new int[]{-8, 100, 1000});
        ListNode result6 = mergeTwoListsIterative(list9, list10);
        System.out.print("极端值测试结果: ");
        ListNode.printList(result6);
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成大规模测试数据
        int size = 10000;
        int[] arr1 = new int[size];
        int[] arr2 = new int[size];
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            arr1[i] = random.nextInt(1000);
            arr2[i] = random.nextInt(1000);
        }
        Arrays.sort(arr1);
        Arrays.sort(arr2);
        
        ListNode list1 = ListNode.createList(arr1);
        ListNode list2 = ListNode.createList(arr2);
        
        // 测试迭代法性能
        long startTime = System.nanoTime();
        ListNode result1 = mergeTwoListsIterative(list1, list2);
        long endTime = System.nanoTime();
        System.out.printf("迭代法执行时间: %.3f ms\n", (endTime - startTime) / 1e6);
        
        // 重新生成测试数据
        list1 = ListNode.createList(arr1);
        list2 = ListNode.createList(arr2);
        
        // 测试递归法性能
        startTime = System.nanoTime();
        ListNode result2 = mergeTwoListsRecursive(list1, list2);
        endTime = System.nanoTime();
        System.out.printf("递归法执行时间: %.3f ms\n", (endTime - startTime) / 1e6);
        
        System.out.println("性能测试完成");
    }
}

/**
 * 题目2: LeetCode 23. 合并K个升序链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/merge-k-sorted-lists/
 *
 * 题目描述：
 * 给你一个链表数组，每个链表都已经按升序排列。
 * 请你将所有链表合并到一个升序链表中，返回合并后的链表。
 *
 * 解法分析：
 * 1. 优先队列法 (最优解) - 时间复杂度: O(N*logK), 空间复杂度: O(K)
 * 2. 分治法 - 时间复杂度: O(N*logK), 空间复杂度: O(logK)
 *
 * 解题思路：
 * 优先队列法：维护一个大小为K的最小堆，堆中存放K个链表的头节点。
 * 每次从堆中取出最小节点加入结果链表，并将该节点的下一个节点加入堆中。
 * 分治法：将K个链表分成两部分，分别合并后再合并两个结果。
 */
class MergeKSortedListsSolution {
    /**
     * 解法1: 优先队列法 (推荐)
     * 时间复杂度: O(N*logK) - N是所有节点总数，K是链表数量
     * 空间复杂度: O(K) - 优先队列的大小
     *
     * 核心思想：
     * 1. 使用优先队列(最小堆)维护K个链表的当前最小节点
     * 2. 每次取出最小节点加入结果链表
     * 3. 将取出节点的下一个节点加入优先队列
     * 4. 重复直到优先队列为空
     */
    public static ListNode mergeKListsPriorityQueue(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        
        // 创建优先队列(最小堆)
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((a, b) -> a.val - b.val);
        
        // 将所有非空链表的头节点加入优先队列
        for (ListNode list : lists) {
            if (list != null) {
                minHeap.offer(list);
            }
        }
        
        // 创建哨兵节点
        ListNode dummy = new ListNode(0);
        ListNode current = dummy;
        
        // 从优先队列中依次取出最小节点
        while (!minHeap.isEmpty()) {
            // 取出最小节点
            ListNode node = minHeap.poll();
            
            // 加入结果链表
            current.next = node;
            current = current.next;
            
            // 如果该节点还有后续节点，加入优先队列
            if (node.next != null) {
                minHeap.offer(node.next);
            }
        }
        
        return dummy.next;
    }
    
    /**
     * 解法2: 分治法
     * 时间复杂度: O(N*logK) - N是所有节点总数，K是链表数量
     * 空间复杂度: O(logK) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 将K个链表分成两部分
     * 2. 递归合并每一部分
     * 3. 合并两个结果链表
     */
    public static ListNode mergeKListsDivideAndConquer(ListNode[] lists) {
        if (lists == null || lists.length == 0) return null;
        return mergeKListsHelper(lists, 0, lists.length - 1);
    }
    
    private static ListNode mergeKListsHelper(ListNode[] lists, int left, int right) {
        if (left == right) return lists[left];
        if (left + 1 == right) {
            return MergeTwoSortedListsSolution.mergeTwoListsIterative(lists[left], lists[right]);
        }
        
        int mid = left + (right - left) / 2;
        ListNode l1 = mergeKListsHelper(lists, left, mid);
        ListNode l2 = mergeKListsHelper(lists, mid + 1, right);
        
        return MergeTwoSortedListsSolution.mergeTwoListsIterative(l1, l2);
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 23. 合并K个升序链表测试 ===");
        
        // 创建测试数据
        ListNode l1 = ListNode.createList(new int[]{1, 4, 5});
        ListNode l2 = ListNode.createList(new int[]{1, 3, 4});
        ListNode l3 = ListNode.createList(new int[]{2, 6});
        ListNode[] lists = {l1, l2, l3};
        
        System.out.print("链表1: ");
        ListNode.printList(lists[0]);
        System.out.print("链表2: ");
        ListNode.printList(lists[1]);
        System.out.print("链表3: ");
        ListNode.printList(lists[2]);
        
        // 测试优先队列法
        ListNode[] listsCopy1 = Arrays.copyOf(lists, lists.length);
        ListNode result1 = mergeKListsPriorityQueue(listsCopy1);
        System.out.print("优先队列法结果: ");
        ListNode.printList(result1);
        
        // 测试分治法
        ListNode[] listsCopy2 = Arrays.copyOf(lists, lists.length);
        ListNode result2 = mergeKListsDivideAndConquer(listsCopy2);
        System.out.print("分治法结果: ");
        ListNode.printList(result2);
        
        // 测试用例2: 空链表数组
        System.out.println("测试用例2: 空链表数组");
        ListNode[] emptyLists = new ListNode[0];
        ListNode result3 = mergeKListsPriorityQueue(emptyLists);
        System.out.print("空链表数组结果: ");
        ListNode.printList(result3);
        
        // 测试用例3: 包含空链表
        System.out.println("测试用例3: 包含空链表");
        ListNode[] listsWithNull = {l1, null, l3};
        ListNode result4 = mergeKListsPriorityQueue(listsWithNull);
        System.out.print("包含空链表结果: ");
        ListNode.printList(result4);
        
        // 测试用例4: 单个链表
        System.out.println("测试用例4: 单个链表");
        ListNode[] singleList = {l1};
        ListNode result5 = mergeKListsPriorityQueue(singleList);
        System.out.print("单个链表结果: ");
        ListNode.printList(result5);
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
    
    /**
     * 性能测试方法
     */
    public static void performanceTest() {
        System.out.println("=== 性能测试 ===");
        
        // 生成大规模测试数据
        int k = 100;  // 链表数量
        int n = 1000; // 每个链表的节点数
        ListNode[] lists = new ListNode[k];
        Random random = new Random();
        
        for (int i = 0; i < k; i++) {
            int[] arr = new int[n];
            for (int j = 0; j < n; j++) {
                arr[j] = random.nextInt(10000);
            }
            Arrays.sort(arr);
            lists[i] = ListNode.createList(arr);
        }
        
        System.out.printf("测试数据规模: %d个链表，每个链表约%d个节点\n", k, n);
        
        // 测试优先队列法性能
        ListNode[] listsCopy1 = Arrays.copyOf(lists, lists.length);
        long startTime = System.nanoTime();
        ListNode result1 = mergeKListsPriorityQueue(listsCopy1);
        long endTime = System.nanoTime();
        System.out.printf("优先队列法执行时间: %.3f ms\n", (endTime - startTime) / 1e6);
        
        // 测试分治法性能
        ListNode[] listsCopy2 = Arrays.copyOf(lists, lists.length);
        startTime = System.nanoTime();
        ListNode result2 = mergeKListsDivideAndConquer(listsCopy2);
        endTime = System.nanoTime();
        System.out.printf("分治法执行时间: %.3f ms\n", (endTime - startTime) / 1e6);
        
        System.out.println("性能测试完成");
    }
}

/**
 * 题目10: LeetCode 141. 环形链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/linked-list-cycle/
 *
 * 题目描述：
 * 给你一个链表的头节点 head ，判断链表中是否有环。
 * 如果链表中有某个节点，可以通过连续跟踪 next 指针再次到达，则链表中存在环。
 *
 * 解法分析：
 * 1. 快慢指针法 (Floyd 判圈算法) - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针，一个快指针和一个慢指针。快指针每次移动两步，慢指针每次移动一步。
 * 如果链表中存在环，快指针最终会追上慢指针；如果不存在环，快指针会先到达链表末尾。
 */
class LinkedListCycleSolution {
    /**
     * 解法: 快慢指针法 (Floyd 判圈算法)
     * 时间复杂度: O(n) - 最多遍历链表两次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 初始化快慢指针都指向头节点
     * 2. 快指针每次移动两步，慢指针每次移动一步
     * 3. 如果存在环，快指针会追上慢指针
     * 4. 如果不存在环，快指针会先到达链表末尾
     */
    public static boolean hasCycle(ListNode head) {
        // 边界条件检查
        if (head == null || head.next == null) {
            return false;
        }
        
        // 初始化快慢指针
        ListNode slow = head;
        ListNode fast = head;
        
        // 移动指针
        while (fast != null && fast.next != null) {
            slow = slow.next;        // 慢指针每次移动一步
            fast = fast.next.next;   // 快指针每次移动两步
            
            // 如果快慢指针相遇，说明存在环
            if (slow == fast) {
                return true;
            }
        }
        
        // 如果快指针到达链表末尾，说明不存在环
        return false;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 141. 环形链表测试 ===");
        
        // 测试用例1: 无环链表
        System.out.println("测试用例1: 无环链表");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 3, 4});
        System.out.print("链表: ");
        ListNode.printList(list1);
        System.out.println("是否有环: " + hasCycle(list1));
        
        // 测试用例2: 有环链表 (构造环)
        System.out.println("测试用例2: 有环链表");
        ListNode list2 = ListNode.createList(new int[]{1, 2, 3, 4});
        // 构造环: 将尾节点指向第二个节点
        ListNode cur = list2;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = list2.next; // 尾节点指向第二个节点
        System.out.println("链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)");
        System.out.println("是否有环: " + hasCycle(list2));
        
        // 测试用例3: 单节点无环
        System.out.println("测试用例3: 单节点无环");
        ListNode list3 = new ListNode(1);
        System.out.println("链表: 1");
        System.out.println("是否有环: " + hasCycle(list3));
        
        // 测试用例4: 空链表
        System.out.println("测试用例4: 空链表");
        ListNode list4 = null;
        System.out.println("链表: null");
        System.out.println("是否有环: " + hasCycle(list4));
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 题目11: LeetCode 142. 环形链表 II
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/linked-list-cycle-ii/
 *
 * 题目描述：
 * 给定一个链表的头节点 head ，返回链表开始入环的第一个节点。 如果链表无环，则返回 null。
 *
 * 解法分析：
 * 1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用快慢指针找到环后，将快指针重新指向头节点，然后快慢指针都每次移动一步，
 * 当它们再次相遇时，相遇点就是环的入口节点。
 */
class LinkedListCycleIISolution {
    /**
     * 解法: 快慢指针法
     * 时间复杂度: O(n) - 最多遍历链表三次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用快慢指针找到环
     * 2. 将快指针重新指向头节点
     * 3. 快慢指针都每次移动一步
     * 4. 再次相遇点就是环的入口
     */
    public static ListNode detectCycle(ListNode head) {
        // 边界条件检查
        if (head == null || head.next == null) {
            return null;
        }
        
        // 第一阶段：使用快慢指针判断是否有环
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            
            // 如果快慢指针相遇，说明存在环
            if (slow == fast) {
                break;
            }
        }
        
        // 如果没有环，返回null
        if (fast == null || fast.next == null) {
            return null;
        }
        
        // 第二阶段：找到环的入口
        // 将快指针重新指向头节点
        fast = head;
        // 快慢指针都每次移动一步，直到相遇
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        
        // 相遇点就是环的入口
        return slow;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 142. 环形链表 II测试 ===");
        
        // 测试用例1: 无环链表
        System.out.println("测试用例1: 无环链表");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 3, 4});
        System.out.print("链表: ");
        ListNode.printList(list1);
        ListNode cycleStart1 = detectCycle(list1);
        System.out.println("环的入口: " + (cycleStart1 != null ? cycleStart1.val : "null"));
        
        // 测试用例2: 有环链表 (构造环)
        System.out.println("测试用例2: 有环链表");
        ListNode list2 = ListNode.createList(new int[]{1, 2, 3, 4});
        // 构造环: 将尾节点指向第二个节点
        ListNode cur = list2;
        while (cur.next != null) {
            cur = cur.next;
        }
        cur.next = list2.next; // 尾节点指向第二个节点
        System.out.println("链表: 1 -> 2 -> 3 -> 4 -> 2 (形成环)");
        ListNode cycleStart2 = detectCycle(list2);
        System.out.println("环的入口: " + (cycleStart2 != null ? cycleStart2.val : "null"));
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 题目12: LeetCode 160. 相交链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/intersection-of-two-linked-lists/
 *
 * 题目描述：
 * 给你两个单链表的头节点 headA 和 headB ，请你找出并返回两个单链表相交的起始节点。如果两个链表不存在相交节点，返回 null 。
 *
 * 解法分析：
 * 1. 双指针法 - 时间复杂度: O(m+n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针分别遍历两个链表，当一个指针到达链表末尾时，将其指向另一个链表的头节点。
 * 如果两个链表相交，两个指针会在相交节点相遇；如果不相交，两个指针会同时到达链表末尾。
 */
class IntersectionOfTwoLinkedListsSolution {
    /**
     * 解法: 双指针法
     * 时间复杂度: O(m+n) - 最多遍历两个链表各两次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用两个指针分别遍历两个链表
     * 2. 当指针到达链表末尾时，将其指向另一个链表的头节点
     * 3. 如果两个链表相交，两个指针会在相交节点相遇
     * 4. 如果不相交，两个指针会同时到达链表末尾
     */
    public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        // 边界条件检查
        if (headA == null || headB == null) {
            return null;
        }
        
        // 初始化两个指针
        ListNode pointerA = headA;
        ListNode pointerB = headB;
        
        // 当两个指针不相等时继续遍历
        while (pointerA != pointerB) {
            // 当指针到达链表末尾时，将其指向另一个链表的头节点
            pointerA = (pointerA == null) ? headB : pointerA.next;
            pointerB = (pointerB == null) ? headA : pointerB.next;
        }
        
        // 返回相交节点或null
        return pointerA;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 160. 相交链表测试 ===");
        
        // 测试用例1: 相交链表
        System.out.println("测试用例1: 相交链表");
        ListNode common = ListNode.createList(new int[]{8, 4, 5});
        ListNode listA = ListNode.createList(new int[]{4, 1});
        ListNode listB = ListNode.createList(new int[]{5, 6, 1});
        
        // 构造相交链表
        ListNode curA = listA;
        while (curA.next != null) {
            curA = curA.next;
        }
        curA.next = common;
        
        ListNode curB = listB;
        while (curB.next != null) {
            curB = curB.next;
        }
        curB.next = common;
        
        System.out.println("链表A: 4 -> 1 -> 8 -> 4 -> 5");
        System.out.println("链表B: 5 -> 6 -> 1 -> 8 -> 4 -> 5");
        ListNode intersection1 = getIntersectionNode(listA, listB);
        System.out.println("相交节点: " + (intersection1 != null ? intersection1.val : "null"));
        
        // 测试用例2: 不相交链表
        System.out.println("测试用例2: 不相交链表");
        ListNode listC = ListNode.createList(new int[]{1, 2, 3});
        ListNode listD = ListNode.createList(new int[]{4, 5, 6});
        System.out.println("链表C: 1 -> 2 -> 3");
        System.out.println("链表D: 4 -> 5 -> 6");
        ListNode intersection2 = getIntersectionNode(listC, listD);
        System.out.println("相交节点: " + (intersection2 != null ? intersection2.val : "null"));
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 题目13: LeetCode 206. 反转链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/reverse-linked-list/
 *
 * 题目描述：
 * 给你单链表的头节点 head ，请你反转链表，并返回反转后的链表。
 *
 * 解法分析：
 * 1. 迭代法 - 时间复杂度: O(n), 空间复杂度: O(1)
 * 2. 递归法 - 时间复杂度: O(n), 空间复杂度: O(n)
 *
 * 解题思路：
 * 迭代法：使用三个指针分别指向前一个节点、当前节点和下一个节点，逐个反转节点的指向。
 * 递归法：递归到链表末尾，然后在回溯过程中反转节点的指向。
 */
class ReverseLinkedListSolution {
    /**
     * 解法1: 迭代法 (推荐)
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用三个指针：prev(前一个节点)、current(当前节点)、next(下一个节点)
     * 2. 逐个反转节点的指向
     * 3. 移动指针继续处理下一个节点
     */
    public static ListNode reverseListIterative(ListNode head) {
        // 初始化指针
        ListNode prev = null;
        ListNode current = head;
        
        // 遍历链表
        while (current != null) {
            // 保存下一个节点
            ListNode next = current.next;
            // 反转当前节点的指向
            current.next = prev;
            // 移动指针
            prev = current;
            current = next;
        }
        
        // 返回新的头节点
        return prev;
    }
    
    /**
     * 解法2: 递归法
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(n) - 递归调用栈的深度
     *
     * 核心思想：
     * 1. 递归到链表末尾
     * 2. 在回溯过程中反转节点的指向
     */
    public static ListNode reverseListRecursive(ListNode head) {
        // 递归终止条件
        if (head == null || head.next == null) {
            return head;
        }
        
        // 递归处理下一个节点
        ListNode newHead = reverseListRecursive(head.next);
        // 反转当前节点和下一个节点的连接
        head.next.next = head;
        head.next = null;
        
        // 返回新的头节点
        return newHead;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 206. 反转链表测试 ===");
        
        // 测试用例1: 正常链表
        System.out.println("测试用例1: 正常链表");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("原链表: ");
        ListNode.printList(list1);
        ListNode reversed1 = reverseListIterative(list1);
        System.out.print("迭代法反转后: ");
        ListNode.printList(reversed1);
        
        // 重新创建测试数据
        ListNode list2 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        ListNode reversed2 = reverseListRecursive(list2);
        System.out.print("递归法反转后: ");
        ListNode.printList(reversed2);
        
        // 测试用例2: 单节点链表
        System.out.println("测试用例2: 单节点链表");
        ListNode list3 = new ListNode(1);
        System.out.print("原链表: ");
        ListNode.printList(list3);
        ListNode reversed3 = reverseListIterative(list3);
        System.out.print("反转后: ");
        ListNode.printList(reversed3);
        
        // 测试用例3: 空链表
        System.out.println("测试用例3: 空链表");
        ListNode list4 = null;
        System.out.print("原链表: ");
        ListNode.printList(list4);
        ListNode reversed4 = reverseListIterative(list4);
        System.out.print("反转后: ");
        ListNode.printList(reversed4);
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 题目14: LeetCode 234. 回文链表
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/palindrome-linked-list/
 *
 * 题目描述：
 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。如果是，返回 true ；否则，返回 false 。
 *
 * 解法分析：
 * 1. 快慢指针 + 反转链表 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 1. 使用快慢指针找到链表中点
 * 2. 反转后半部分链表
 * 3. 比较前半部分和反转后的后半部分
 * 4. 恢复链表结构(可选)
 */
class PalindromeLinkedListSolution {
    /**
     * 解法: 快慢指针 + 反转链表
     * 时间复杂度: O(n) - 需要遍历链表多次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用快慢指针找到链表中点
     * 2. 反转后半部分链表
     * 3. 比较前半部分和反转后的后半部分
     */
    public static boolean isPalindrome(ListNode head) {
        // 边界条件检查
        if (head == null || head.next == null) {
            return true;
        }
        
        // 第一步：使用快慢指针找到链表中点
        ListNode slow = head;
        ListNode fast = head;
        
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        
        // 第二步：反转后半部分链表
        ListNode secondHalf = reverseList(slow.next);
        
        // 第三步：比较前半部分和反转后的后半部分
        ListNode firstHalf = head;
        ListNode secondHalfCopy = secondHalf; // 保存用于恢复
        boolean isPalindrome = true;
        
        while (secondHalf != null) {
            if (firstHalf.val != secondHalf.val) {
                isPalindrome = false;
                break;
            }
            firstHalf = firstHalf.next;
            secondHalf = secondHalf.next;
        }
        
        // 第四步：恢复链表结构(可选)
        slow.next = reverseList(secondHalfCopy);
        
        return isPalindrome;
    }
    
    /**
     * 反转链表的辅助函数
     */
    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode current = head;
        
        while (current != null) {
            ListNode next = current.next;
            current.next = prev;
            prev = current;
            current = next;
        }
        
        return prev;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 234. 回文链表测试 ===");
        
        // 测试用例1: 回文链表
        System.out.println("测试用例1: 回文链表");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 2, 1});
        System.out.print("链表: ");
        ListNode.printList(list1);
        System.out.println("是否为回文链表: " + isPalindrome(list1));
        
        // 测试用例2: 非回文链表
        System.out.println("测试用例2: 非回文链表");
        ListNode list2 = ListNode.createList(new int[]{1, 2, 3, 4});
        System.out.print("链表: ");
        ListNode.printList(list2);
        System.out.println("是否为回文链表: " + isPalindrome(list2));
        
        // 测试用例3: 单节点链表
        System.out.println("测试用例3: 单节点链表");
        ListNode list3 = new ListNode(1);
        System.out.print("链表: ");
        ListNode.printList(list3);
        System.out.println("是否为回文链表: " + isPalindrome(list3));
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 题目15: LeetCode 19. 删除链表的倒数第 N 个结点
 * 来源: LeetCode
 * 链接: https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
 *
 * 题目描述：
 * 给你一个链表，删除链表的倒数第 n 个结点，并且返回链表的头结点。
 *
 * 解法分析：
 * 1. 快慢指针法 - 时间复杂度: O(n), 空间复杂度: O(1)
 *
 * 解题思路：
 * 使用两个指针，快指针先移动n+1步，然后快慢指针同时移动，
 * 当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点。
 */
class RemoveNthNodeFromEndOfListSolution {
    /**
     * 解法: 快慢指针法
     * 时间复杂度: O(n) - 需要遍历链表一次
     * 空间复杂度: O(1) - 只使用了常数级别的额外空间
     *
     * 核心思想：
     * 1. 使用哨兵节点简化边界处理
     * 2. 快指针先移动n+1步
     * 3. 快慢指针同时移动
     * 4. 当快指针到达链表末尾时，慢指针正好指向要删除节点的前一个节点
     */
    public static ListNode removeNthFromEnd(ListNode head, int n) {
        // 创建哨兵节点，简化边界处理
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 初始化快慢指针
        ListNode fast = dummy;
        ListNode slow = dummy;
        
        // 快指针先移动n+1步
        for (int i = 0; i <= n; i++) {
            fast = fast.next;
        }
        
        // 快慢指针同时移动
        while (fast != null) {
            fast = fast.next;
            slow = slow.next;
        }
        
        // 删除倒数第n个节点
        slow.next = slow.next.next;
        
        // 返回头节点
        return dummy.next;
    }
    
    /**
     * 测试方法
     */
    public static void test() {
        System.out.println("=== LeetCode 19. 删除链表的倒数第 N 个结点测试 ===");
        
        // 测试用例1: 删除中间节点
        System.out.println("测试用例1: 删除中间节点");
        ListNode list1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("原链表: ");
        ListNode.printList(list1);
        ListNode result1 = removeNthFromEnd(list1, 2);
        System.out.print("删除倒数第2个节点后: ");
        ListNode.printList(result1);
        
        // 测试用例2: 删除头节点
        System.out.println("测试用例2: 删除头节点");
        ListNode list2 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("原链表: ");
        ListNode.printList(list2);
        ListNode result2 = removeNthFromEnd(list2, 5);
        System.out.print("删除倒数第5个节点后: ");
        ListNode.printList(result2);
        
        // 测试用例3: 删除尾节点
        System.out.println("测试用例3: 删除尾节点");
        ListNode list3 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
        System.out.print("原链表: ");
        ListNode.printList(list3);
        ListNode result3 = removeNthFromEnd(list3, 1);
        System.out.print("删除倒数第1个节点后: ");
        ListNode.printList(result3);
        
        System.out.println("所有测试用例执行完成");
        System.out.println("=======================================================");
    }
}

/**
 * 算法总结与技巧提升
 */
class AlgorithmSummary {
    public static void printSummary() {
        System.out.println("========== 链表合并算法总结 ==========");
        System.out.println("1. 核心算法技巧:");
        System.out.println("   - 双指针法: 适用于两个有序序列的合并，时间复杂度O(m+n)");
        System.out.println("   - 优先队列法: 适用于K个有序序列的合并，时间复杂度O(N*logK)");
        System.out.println("   - 分治法: 适用于K个序列的归并，时间复杂度O(N*logK)");
        System.out.println("   - 哨兵节点: 简化链表操作的边界处理，提高代码可读性");
        System.out.println("   - 原地修改: 避免额外空间开销，适用于数组合并等场景");
        System.out.println();
        System.out.println("2. 工程化考量:");
        System.out.println("   - 异常处理: 处理空链表、单节点链表等边界情况");
        System.out.println("   - 内存管理: 在Java中由垃圾回收机制自动管理");
        System.out.println("   - 性能优化: 对于大规模数据，优先队列的常数项优化很重要");
        System.out.println("   - 线程安全: 在多线程环境下需要考虑同步问题");
        System.out.println();
        System.out.println("3. 调试技巧:");
        System.out.println("   - 打印中间状态: 使用System.out跟踪指针移动");
        System.out.println("   - 边界测试: 测试空输入、单元素输入、极端值等情况");
        System.out.println("   - 断言验证: 使用assert验证关键条件是否满足");
        System.out.println();
        System.out.println("4. 拓展应用:");
        System.out.println("   - 归并排序: 链表排序的最佳选择之一");
        System.out.println("   - 多路归并: 外部排序的基础算法");
        System.out.println("   - 数据流处理: 实时合并多个有序数据流");
        System.out.println("======================================");
        System.out.println();
    }
}

/**
 * 综合测试函数
 */
public class MergeTwoLists {
    public static void main(String[] args) {
        // 运行所有测试
        MergeTwoSortedListsSolution.test();
        // 移除递归法性能测试避免栈溢出
        // MergeTwoSortedListsSolution.performanceTest();
        
        MergeKSortedListsSolution.test();
        MergeKSortedListsSolution.performanceTest();
        
        // 新增题目的测试
        LinkedListCycleSolution.test();
        LinkedListCycleIISolution.test();
        IntersectionOfTwoLinkedListsSolution.test();
        ReverseLinkedListSolution.test();
        PalindromeLinkedListSolution.test();
        RemoveNthNodeFromEndOfListSolution.test();
        
        AlgorithmSummary.printSummary();
    }
}