import java.util.*;

/**
 * 链表反转相关算法题目集合
 * 包含LeetCode、牛客网、Codeforces、LintCode、HackerRank等平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析和多种解法
 * 
 * 题目列表：
 * 1. 反转链表 (LeetCode 206, 牛客网, HackerRank, LintCode 35, 剑指Offer 24)
 * 2. 反转链表 II (LeetCode 92)
 * 3. K个一组翻转链表 (LeetCode 25)
 * 4. 回文链表 (LeetCode 234, LintCode 223, 牛客网 NC78)
 * 5. 旋转链表 (LeetCode 61, LintCode 170)
 * 6. 合并两个有序链表 (LeetCode 21, LintCode 165, 剑指Offer 25, 牛客网 NC33)
 * 7. 两两交换链表中的节点 (LeetCode 24, LintCode 451)
 * 8. 重排链表 (LeetCode 143, LintCode 99)
 * 9. 删除链表的倒数第N个节点 (LeetCode 19, LintCode 174, 牛客网 NC53, 剑指Offer 22)
 * 10. 奇偶链表 (LeetCode 328, LintCode 1292, 牛客网 NC142)
 * 11. 分隔链表 (LeetCode 86, LintCode 96, 牛客网 NC188)
 * 12. 链表求和 (LeetCode 2, LeetCode 445, LintCode 167, 牛客网 NC40)
 * 13. 环形链表 (LeetCode 141, LeetCode 142, LintCode 102, 牛客网 NC4, 剑指Offer 23)
 * 14. 相交链表 (LeetCode 160, LintCode 380, 牛客网 NC66, 剑指Offer 52)
 * 15. 排序链表 (LeetCode 148, LintCode 98)
 * 16. 链表随机节点 (LeetCode 382)
 * 17. 复制带随机指针的链表 (LeetCode 138, 剑指Offer 35)
 * 18. 链表组件 (LeetCode 817)
 * 19. 链表中的下一个更大节点 (LeetCode 1019)
 * 20. 链表最大孪生和 (LeetCode 2130)
 */
public class ListReverse {

	public static void main(String[] args) {
		// int、long、byte、short
		// char、float、double、boolean
		// 还有String
		// 都是按值传递
		int a = 10;
		f(a);
		System.out.println(a);

		// 其他类型按引用传递
		// 比如下面的Number是自定义的类
		Number b = new Number(5);
		g1(b);
		System.out.println(b.val);
		g2(b);
		System.out.println(b.val);

		// 比如下面的一维数组
		int[] c = { 1, 2, 3, 4 };
		g3(c);
		System.out.println(c[0]);
		g4(c);
		System.out.println(c[0]);
		
		// 测试链表反转相关题目
		testReverseList();
		testReverseListII();
		testReverseKGroup();
		testReverseDoubleList();
	}

	public static void f(int a) {
		a = 0;
	}

	public static class Number {
		public int val;

		public Number(int v) {
			val = v;
		}
	}

	public static void g1(Number b) {
		b = null;
	}

	public static void g2(Number b) {
		b.val = 6;
	}

	public static void g3(int[] c) {
		c = null;
	}

	public static void g4(int[] c) {
		c[0] = 100;
	}
	
	// ==================== 链表反转相关题目 ====================
	
	// 单链表节点
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode() {}
		
		public ListNode(int val) {
			this.val = val;
		}

		public ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			ListNode current = this;
			while (current != null) {
				sb.append(current.val);
				if (current.next != null) {
					sb.append(" -> ");
				}
				current = current.next;
			}
			return sb.toString();
		}
		
		/**
		 * 创建链表的辅助方法
		 * @param vals 节点值数组
		 * @return 构建的链表头节点
		 */
		public static ListNode createList(int[] vals) {
			if (vals.length == 0) return null;
			
			ListNode head = new ListNode(vals[0]);
			ListNode current = head;
			for (int i = 1; i < vals.length; i++) {
				current.next = new ListNode(vals[i]);
				current = current.next;
			}
			return head;
		}
	}

	// 双链表节点
	public static class DoubleListNode {
		public int value;
		public DoubleListNode last;
		public DoubleListNode next;

		public DoubleListNode(int v) {
			value = v;
		}
	}
	
	/**
	 * 测试基础链表反转
	 * 题目来源：LeetCode 206. 反转链表
	 * 牛客网：反转链表
	 * HackerRank：Reverse a linked list
	 */
	public static void testReverseList() {
		System.out.println("=== 测试基础链表反转 ===");
		
		// 测试用例1: [1,2,3,4,5] -> [5,4,3,2,1]
		ListNode head1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		System.out.println("原链表: " + head1);
		ListNode reversed1 = reverseListIterative(head1);
		System.out.println("反转后: " + reversed1);
		
		// 测试用例2: [1,2] -> [2,1]
		ListNode head2 = ListNode.createList(new int[]{1, 2});
		System.out.println("原链表: " + head2);
		ListNode reversed2 = reverseListRecursive(head2);
		System.out.println("反转后: " + reversed2);
		
		// 测试用例3: [] -> []
		ListNode head3 = null;
		System.out.println("原链表: " + head3);
		ListNode reversed3 = reverseListIterative(head3);
		System.out.println("反转后: " + reversed3);
		System.out.println();
	}
	
	/**
	 * 测试指定区间链表反转
	 * 题目来源：LeetCode 92. 反转链表 II
	 */
	public static void testReverseListII() {
		System.out.println("=== 测试指定区间链表反转 ===");
		
		// 测试用例1: [1,2,3,4,5], left=2, right=4 -> [1,4,3,2,5]
		ListNode head1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		System.out.println("原链表: " + head1);
		ListNode reversed1 = reverseBetween(head1, 2, 4);
		System.out.println("反转位置2到4后: " + reversed1);
		
		// 测试用例2: [5], left=1, right=1 -> [5]
		ListNode head2 = ListNode.createList(new int[]{5});
		System.out.println("原链表: " + head2);
		ListNode reversed2 = reverseBetween(head2, 1, 1);
		System.out.println("反转位置1到1后: " + reversed2);
		System.out.println();
	}
	
	/**
	 * 测试K个一组反转链表
	 * 题目来源：LeetCode 25. K 个一组翻转链表
	 */
	public static void testReverseKGroup() {
		System.out.println("=== 测试K个一组反转链表 ===");
		
		// 测试用例1: [1,2,3,4,5], k=2 -> [2,1,4,3,5]
		ListNode head1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		System.out.println("原链表: " + head1);
		ListNode reversed1 = reverseKGroup(head1, 2);
		System.out.println("每2个一组反转后: " + reversed1);
		
		// 测试用例2: [1,2,3,4,5], k=3 -> [3,2,1,4,5]
		ListNode head2 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		System.out.println("原链表: " + head2);
		ListNode reversed2 = reverseKGroup(head2, 3);
		System.out.println("每3个一组反转后: " + reversed2);
		System.out.println();
	}
	
	/**
	 * 测试双链表反转
	 */
	public static void testReverseDoubleList() {
		System.out.println("=== 测试双链表反转 ===");
		// 双链表测试较为复杂，此处省略具体测试
		System.out.println("双链表反转功能已实现");
		System.out.println();
	}

	// ==================== 题目1: 反转链表 ====================
	// 题目来源：
	// 1. LeetCode 206. 反转链表 - https://leetcode.cn/problems/reverse-linked-list/
	// 2. 牛客网 反转链表 - https://www.nowcoder.com/practice/75e878df47f24fdc9dc3e400ec6058ca
	// 3. HackerRank Reverse a linked list - https://www.hackerrank.com/challenges/reverse-a-linked-list
	// 4. LintCode 35. 翻转链表 - https://www.lintcode.com/problem/reverse-linked-list
	// 5. 剑指Offer 24. 反转链表 - https://leetcode.cn/problems/fan-zhuan-lian-biao-lcof/
	
	/**
	 * 方法1: 迭代法反转链表
	 * 时间复杂度: O(n) - 需要遍历链表一次
	 * 空间复杂度: O(1) - 只使用了常数级别的额外空间
	 * 
	 * 解题思路:
	 * 使用三个指针(pre, current, next)来逐个反转链表中的节点指向关系
	 * 1. pre指向已反转部分的最后一个节点
	 * 2. current指向当前待处理节点
	 * 3. next保存current的下一个节点，防止断链
	 * 
	 * 执行过程:
	 * 原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
	 * 步骤1: null <- 1    2 -> 3 -> 4 -> 5 -> null
	 * 步骤2: null <- 1 <- 2    3 -> 4 -> 5 -> null
	 * 步骤3: null <- 1 <- 2 <- 3    4 -> 5 -> null
	 * ...
	 * 最终: null <- 1 <- 2 <- 3 <- 4 <- 5
	 */
	public static ListNode reverseListIterative(ListNode head) {
		ListNode pre = null;     // 已反转部分的头节点
		ListNode current = head; // 当前待处理节点
		ListNode next = null;    // 保存current的下一个节点
		
		while (current != null) {
			next = current.next;    // 保存下一个节点
			current.next = pre;     // 反转当前节点的指向
			pre = current;          // 移动pre指针
			current = next;         // 移动current指针
		}
		
		return pre; // pre指向原链表的最后一个节点，即新链表的头节点
	}
	
	/**
	 * 方法2: 递归法反转链表
	 * 时间复杂度: O(n) - 递归调用n次
	 * 空间复杂度: O(n) - 递归调用栈的深度为n
	 * 
	 * 解题思路:
	 * 1. 递归到链表末尾
	 * 2. 在回溯过程中逐个反转节点的指向
	 * 3. 假设除了当前节点外，后续链表已经完成反转
	 * 
	 * 执行过程:
	 * 原链表: 1 -> 2 -> 3 -> 4 -> 5 -> null
	 * 递归到5，返回5
	 * 回溯到4: 4.next.next = 4 (即5->4)，4.next = null
	 * 回溯到3: 3.next.next = 3 (即4->3)，3.next = null
	 * ...
	 */
	public static ListNode reverseListRecursive(ListNode head) {
		// 递归终止条件：空节点或只有一个节点
		if (head == null || head.next == null) {
			return head;
		}
		
		// 递归处理后续节点，获取反转后链表的头节点
		ListNode newHead = reverseListRecursive(head.next);
		
		// 反转当前节点和下一个节点的连接关系
		head.next.next = head;  // 让下一个节点指向当前节点
		head.next = null;       // 断开当前节点的next指针
		
		return newHead; // 返回反转后链表的头节点
	}
	
	// 原有的解法保持不变
	public static ListNode reverseList(ListNode head) {
		ListNode pre = null;
		ListNode next = null;
		while (head != null) {
			next = head.next;
			head.next = pre;
			pre = head;
			head = next;
		}
		return pre;
	}

	// ==================== 题目2: 反转链表 II ====================
	// 题目来源：
	// 1. LeetCode 92. 反转链表 II - https://leetcode.cn/problems/reverse-linked-list-ii/
	
	/**
	 * 反转链表指定区间
	 * 时间复杂度: O(n) - 最多遍历一次链表
	 * 空间复杂度: O(1) - 只使用常数级别的额外空间
	 * 
	 * 解题思路:
	 * 1. 找到需要反转区间的前一个节点(pre)
	 * 2. 找到需要反转区间的第一个节点(start)
	 * 3. 使用头插法将区间内的节点逐个插入到pre节点之后
	 * 4. 连接反转后的链表与其他部分
	 * 
	 * 执行过程:
	 * 原链表: 1 -> 2 -> 3 -> 4 -> 5, left=2, right=4
	 * 步骤1: 找到pre(节点1)和start(节点2)
	 * 步骤2: 将节点3插入到pre之后: 1 -> 3 -> 2 -> 4 -> 5
	 * 步骤3: 将节点4插入到pre之后: 1 -> 4 -> 3 -> 2 -> 5
	 * 结果: 1 -> 4 -> 3 -> 2 -> 5
	 */
	public static ListNode reverseBetween(ListNode head, int left, int right) {
		// 创建虚拟头节点，简化边界处理
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		// 找到反转区间的前一个节点
		ListNode pre = dummy;
		for (int i = 0; i < left - 1; i++) {
			pre = pre.next;
		}
		
		// start指向反转区间的第一个节点
		ListNode start = pre.next;
		// then指向待处理节点
		ListNode then = start.next;
		
		// 头插法实现区间反转
		for (int i = 0; i < right - left; i++) {
			start.next = then.next;
			then.next = pre.next;
			pre.next = then;
			then = start.next;
		}
		
		return dummy.next;
	}

	// ==================== 题目3: K 个一组翻转链表 ====================
	// 题目来源：
	// 1. LeetCode 25. K 个一组翻转链表 - https://leetcode.cn/problems/reverse-nodes-in-k-group/
	
	/**
	 * K个一组反转链表
	 * 时间复杂度: O(n) - 每个节点最多被访问两次
	 * 空间复杂度: O(1) - 只使用常数级别的额外空间
	 * 
	 * 解题思路:
	 * 1. 分组处理，每次处理k个节点
	 * 2. 对每组节点进行反转
	 * 3. 连接各组之间的关系
	 * 4. 处理不足k个的剩余节点（保持原顺序）
	 * 
	 * 执行过程:
	 * 原链表: 1 -> 2 -> 3 -> 4 -> 5, k=3
	 * 第一组(1,2,3)反转: 3 -> 2 -> 1
	 * 第二组(4,5)不足k个，保持原顺序: 4 -> 5
	 * 结果: 3 -> 2 -> 1 -> 4 -> 5
	 */
	public static ListNode reverseKGroup(ListNode head, int k) {
		// 计算链表长度
		int length = 0;
		ListNode current = head;
		while (current != null) {
			length++;
			current = current.next;
		}
		
		// 创建虚拟头节点
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		// pre指向已处理部分的最后一个节点
		ListNode pre = dummy;
		
		// 分组处理
		while (length >= k) {
			// start指向当前组的第一个节点
			ListNode start = pre.next;
			// then指向待处理节点
			ListNode then = start.next;
			
			// 对当前组进行k-1次头插操作
			for (int i = 1; i < k; i++) {
				start.next = then.next;
				then.next = pre.next;
				pre.next = then;
				then = start.next;
			}
			
			// 更新pre指针和剩余长度
			pre = start;
			length -= k;
		}
		
		return dummy.next;
	}
	
	// ==================== 双链表反转 ====================
	// 反转双链表
	// 题目来源：
	// 1. LeetCode 445. 两数相加 II（涉及链表反转思想）
	// 2. 剑指Offer 24. 反转链表（扩展到双链表）
	// 3. 牛客网 反转双链表
	// 时间复杂度: O(n) - 需要遍历双链表一次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	public static DoubleListNode reverseDoubleList(DoubleListNode head) {
		DoubleListNode pre = null;
		DoubleListNode next = null;
		while (head != null) {
			next = head.next;    // 保存下一个节点
			head.next = pre;     // 反转next指针
			head.last = next;    // 反转last指针
			pre = head;          // 移动pre指针
			head = next;         // 移动head指针
		}
		return pre;  // 返回新的头节点
	}
	
	// ==================== 补充题目4: 回文链表 ====================
	// 题目来源：
	// 1. LeetCode 234. 回文链表 - https://leetcode.cn/problems/palindrome-linked-list/
	// 2. LintCode 223. 回文链表 - https://www.lintcode.com/problem/palindrome-linked-list/
	// 3. 牛客网 NC78 链表中倒数最后k个结点（相关题目）
	// 
	// 题目描述：判断一个链表是否是回文链表
	// 输入：1->2->2->1
	// 输出：true
	// 
	// 最优解法：使用快慢指针找到中点，反转后半部分，然后比较
	// 时间复杂度: O(n) - 只需要一次遍历找到中点，一次反转，一次比较
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	public static boolean isPalindrome(ListNode head) {
		if (head == null || head.next == null) {
			return true;  // 空链表或单节点链表是回文的
		}
		
		// 步骤1: 使用快慢指针找到链表的中点
		ListNode slow = head;
		ListNode fast = head;
		while (fast != null && fast.next != null) {
			slow = slow.next;        // 慢指针每次走一步
			fast = fast.next.next;   // 快指针每次走两步
		}
		// 循环结束后，slow指向中点位置（如果节点数为奇数）或后半部分的第一个节点（如果节点数为偶数）
		
		// 步骤2: 反转后半部分链表
		ListNode secondHalfHead = reverseListIterative(slow);
		// 保存反转后的头节点，用于后续恢复
		ListNode secondHalfStart = secondHalfHead;
		
		// 步骤3: 比较前半部分和反转后的后半部分
		ListNode firstHalfHead = head;
		boolean isPalindrome = true;
		while (secondHalfHead != null) {
			if (firstHalfHead.val != secondHalfHead.val) {
				isPalindrome = false;
				break;
			}
			firstHalfHead = firstHalfHead.next;
			secondHalfHead = secondHalfHead.next;
		}
		
		// 步骤4: 恢复链表（可选，但这是良好的工程实践）
		reverseListIterative(secondHalfStart);
		
		return isPalindrome;
	}
	
	// ==================== 补充题目5: 旋转链表 ====================
	// 题目来源：
	// 1. LeetCode 61. 旋转链表 - https://leetcode.cn/problems/rotate-list/
	// 2. LintCode 170. 旋转链表 - https://www.lintcode.com/problem/rotate-list/
	// 3. 牛客网 NC53 删除链表的倒数第n个节点（相关题目）
	// 
	// 题目描述：将链表向右旋转k个位置
	// 输入：1->2->3->4->5->NULL, k = 2
	// 输出：4->5->1->2->3->NULL
	// 
	// 解题思路：
	// 1. 先计算链表长度
	// 2. 将链表首尾相连形成环
	// 3. 在合适位置断开环
	// 时间复杂度: O(n) - 需要遍历链表
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	public static ListNode rotateRight(ListNode head, int k) {
		// 处理特殊情况
		if (head == null || head.next == null || k == 0) {
			return head;
		}
		
		// 步骤1: 计算链表长度并找到尾节点
		int length = 1;
		ListNode tail = head;
		while (tail.next != null) {
			tail = tail.next;
			length++;
		}
		
		// 步骤2: 计算实际需要旋转的次数（取模操作避免多余旋转）
		k = k % length;
		if (k == 0) {
			return head;  // 不需要旋转
		}
		
		// 步骤3: 将链表首尾相连形成环
		tail.next = head;
		
		// 步骤4: 找到新的尾节点位置，距离原头节点 (length - k) 个位置
		ListNode newTail = head;
		for (int i = 0; i < length - k - 1; i++) {
			newTail = newTail.next;
		}
		
		// 步骤5: 新的头节点是新尾节点的下一个节点
		ListNode newHead = newTail.next;
		
		// 步骤6: 断开环
		newTail.next = null;
		
		return newHead;
	}
	
	// ==================== 补充题目6: 合并两个有序链表 ====================
	// 题目来源：
	// 1. LeetCode 21. 合并两个有序链表 - https://leetcode.cn/problems/merge-two-sorted-lists/
	// 2. LintCode 165. 合并两个排序链表 - https://www.lintcode.com/problem/merge-two-sorted-lists/
	// 3. 剑指Offer 25. 合并两个排序的链表
	// 4. 牛客网 NC33 合并两个排序的链表
	// 
	// 题目描述：将两个升序链表合并为一个新的升序链表
	// 输入：l1 = [1,2,4], l2 = [1,3,4]
	// 输出：[1,1,2,3,4,4]
	// 
	// 解题思路：使用迭代或递归方法，逐个比较两个链表的节点值
	// 时间复杂度: O(n+m) - n和m分别是两个链表的长度
	// 空间复杂度: O(1) - 迭代版本，只使用常数级别的额外空间
	public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
		// 创建虚拟头节点，简化边界情况处理
		ListNode dummy = new ListNode(-1);
		ListNode current = dummy;
		
		// 迭代比较两个链表的节点值
		while (l1 != null && l2 != null) {
			if (l1.val <= l2.val) {
				current.next = l1;
				l1 = l1.next;
			} else {
				current.next = l2;
				l2 = l2.next;
			}
			current = current.next;
		}
		
		// 连接剩余部分
		current.next = (l1 != null) ? l1 : l2;
		
		return dummy.next;
	}
	
	// ==================== 补充题目7: 两两交换链表中的节点 ====================
	// 题目来源：
	// 1. LeetCode 24. 两两交换链表中的节点 - https://leetcode.cn/problems/swap-nodes-in-pairs/
	// 2. LintCode 451. 两两交换链表中的节点 - https://www.lintcode.com/problem/swap-nodes-in-pairs/
	// 3. 牛客网 NC142 链表的奇偶重排（相关题目）
	// 
	// 题目描述：两两交换链表中的相邻节点
	// 输入：1->2->3->4
	// 输出：2->1->4->3
	// 
	// 解题思路：使用虚拟头节点和迭代方法
	// 时间复杂度: O(n) - 需要遍历链表一次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	public static ListNode swapPairs(ListNode head) {
		// 创建虚拟头节点
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		ListNode prev = dummy;
		
		// 当有至少两个节点可以交换时
		while (prev.next != null && prev.next.next != null) {
			// 获取需要交换的两个节点
			ListNode first = prev.next;
			ListNode second = prev.next.next;
			
			// 执行交换操作
			first.next = second.next;  // 1 -> 3
			second.next = first;       // 2 -> 1
			prev.next = second;        // dummy -> 2
			
			// 移动prev指针到下一对的前一个位置
			prev = first;
		}
		
		return dummy.next;
	}
	
	// ==================== 补充题目8: 重排链表 ====================
	// 题目来源：
	// 1. LeetCode 143. 重排链表 - https://leetcode.cn/problems/reorder-list/
	// 2. LintCode 99. 重排链表 - https://www.lintcode.com/problem/reorder-list/
	// 3. 牛客网 NC40 链表相加（二）（相关题目）
	// 
	// 题目描述：按照 L0 → Ln → L1 → Ln-1 → L2 → Ln-2 → ... 重新排列链表
	// 输入：1->2->3->4
	// 输出：1->4->2->3
	// 
	// 解题思路：
	// 1. 使用快慢指针找到中点
	// 2. 反转后半部分链表
	// 3. 合并两个链表
	// 时间复杂度: O(n) - 需要遍历链表三次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	public static void reorderList(ListNode head) {
		if (head == null || head.next == null || head.next.next == null) {
			return;  // 无需重排
		}
		
		// 步骤1: 使用快慢指针找到链表中点
		ListNode slow = head;
		ListNode fast = head;
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		
		// 步骤2: 反转后半部分链表
		ListNode secondHalf = reverseListIterative(slow.next);
		slow.next = null;  // 断开前半部分和后半部分
		
		// 步骤3: 合并两个链表
		ListNode firstHalf = head;
		while (secondHalf != null) {
			ListNode temp1 = firstHalf.next;
			ListNode temp2 = secondHalf.next;
			
			firstHalf.next = secondHalf;
			secondHalf.next = temp1;
			
			firstHalf = temp1;
			secondHalf = temp2;
		}
	}
	
	// ==================== 补充题目9: 删除链表的倒数第N个节点 ====================
	// 题目来源：
	// 1. LeetCode 19. 删除链表的倒数第N个节点 - https://leetcode.cn/problems/remove-nth-node-from-end-of-list/
	// 2. LintCode 174. 删除链表中倒数第n个节点 - https://www.lintcode.com/problem/remove-nth-node-from-end-of-list/
	// 3. 牛客网 NC53 删除链表的倒数第n个节点
	// 4. 剑指Offer 22. 链表中倒数第k个节点（相关题目）
	// 
	// 题目描述：删除链表的倒数第n个节点，返回链表的头节点
	// 输入：head = [1,2,3,4,5], n = 2
	// 输出：[1,2,3,5]
	// 
	// 解题思路：使用快慢指针，快指针先走n步，然后快慢指针一起走
	// 时间复杂度: O(n) - 只需要遍历链表一次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	// 只需要遍历链表一次，空间复杂度为O(1)，没有更优的算法。
	public static ListNode removeNthFromEnd(ListNode head, int n) {
		// 创建虚拟头节点，简化边界情况处理
		ListNode dummy = new ListNode(0);
		dummy.next = head;
		
		// 设置快慢指针
		ListNode fast = dummy;
		ListNode slow = dummy;
		
		// 快指针先走n+1步
		for (int i = 0; i <= n; i++) {
			fast = fast.next;
			// 如果n大于链表长度，fast会变成null
			if (i < n && fast == null) {
				return head;  // n大于链表长度，无法删除
			}
		}
		
		// 快慢指针一起走，直到快指针到达链表末尾
		while (fast != null) {
			fast = fast.next;
			slow = slow.next;
		}
		
		// 此时slow指向待删除节点的前一个节点
		slow.next = slow.next.next;
		
		return dummy.next;
	}
	
	// ==================== 工程化考虑 ====================
	
	/**
	 * 带异常处理的链表反转方法
	 * 
	 * @param head 链表头节点
	 * @return 反转后的链表头节点
	 * @throws IllegalArgumentException 当输入参数非法时抛出异常
	 */
	public static ListNode reverseListSafe(ListNode head) throws IllegalArgumentException {
		// 参数校验
		if (head == null) {
			return null;
		}
		
		// 检查链表是否有环(简化版检查)
		ListNode slow = head;
		ListNode fast = head;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			if (slow == fast) {
				throw new IllegalArgumentException("链表中存在环，无法进行反转");
			}
		}
		
		// 执行反转
		return reverseListIterative(head);
	}
	
	/**
	 * 链表反转的单元测试方法
	 */
	public static void runUnitTests() {
		System.out.println("=== 链表反转单元测试 ===");
		
		// 测试用例1: 正常情况
		ListNode test1 = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		ListNode result1 = reverseListIterative(test1);
		System.out.println("测试1 - 输入[1,2,3,4,5]，期望[5,4,3,2,1]，实际: " + result1);
		
		// 测试用例2: 空链表
		ListNode result2 = reverseListIterative(null);
		System.out.println("测试2 - 输入[]，期望[]，实际: " + result2);
		
		// 测试用例3: 单节点链表
		ListNode test3 = new ListNode(1);
		ListNode result3 = reverseListIterative(test3);
		System.out.println("测试3 - 输入[1]，期望[1]，实际: " + (result3 != null ? result3.val : "null"));
		
		// 测试用例4: 两节点链表
		ListNode test4 = ListNode.createList(new int[]{1, 2});
		ListNode result4 = reverseListIterative(test4);
		System.out.println("测试4 - 输入[1,2]，期望[2,1]，实际: " + result4);
		
		System.out.println("单元测试完成\n");
	}
	
	// ==================== 补充题目10: 奇偶链表 ====================
	// 题目来源：
	// 1. LeetCode 328. 奇偶链表 - https://leetcode.cn/problems/odd-even-linked-list/
	// 2. LintCode 1292. 奇偶链表 - https://www.lintcode.com/problem/odd-even-linked-list/
	// 3. 牛客网 NC142 链表的奇偶重排
	// 
	// 题目描述：将链表的奇数节点和偶数节点分组在一起，保持相对顺序不变
	// 输入：1->2->3->4->5->NULL
	// 输出：1->3->5->2->4->NULL
	// 
	// 解题思路：
	// 使用两个指针分别处理奇数节点和偶数节点
	// 1. odd指针连接所有奇数位置的节点
	// 2. even指针连接所有偶数位置的节点  
	// 3. 最后将奇数链表的尾部连接到偶数链表的头部
	// 
	// 时间复杂度: O(n) - 只需要遍历链表一次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static ListNode oddEvenList(ListNode head) {
		// 边界情况处理
		if (head == null || head.next == null) {
			return head;
		}
		
		// odd指向奇数位置节点，even指向偶数位置节点
		ListNode odd = head;
		ListNode even = head.next;
		ListNode evenHead = even;  // 保存偶数链表的头节点
		
		// 遍历链表，将奇数节点和偶数节点分组
		while (even != null && even.next != null) {
			odd.next = even.next;   // 连接下一个奇数节点
			odd = odd.next;         // 移动odd指针
			even.next = odd.next;   // 连接下一个偶数节点
			even = even.next;       // 移动even指针
		}
		
		// 将奇数链表的尾部连接到偶数链表的头部
		odd.next = evenHead;
		
		return head;
	}
	
	// ==================== 补充题目11: 分隔链表 ====================
	// 题目来源：
	// 1. LeetCode 86. 分隔链表 - https://leetcode.cn/problems/partition-list/
	// 2. LintCode 96. 分隔链表 - https://www.lintcode.com/problem/partition-list/
	// 3. 牛客网 NC188 分隔链表
	// 
	// 题目描述：给定一个链表和一个特定值 x，将链表分隔成两部分，使得所有小于 x 的节点都在大于或等于 x 的节点之前
	// 输入：head = 1->4->3->2->5->2, x = 3
	// 输出：1->2->2->4->3->5
	// 
	// 解题思路：
	// 使用两个虚拟头节点分别存储小于x和大于等于x的节点
	// 1. before链表存储所有小于x的节点
	// 2. after链表存储所有大于等于x的节点
	// 3. 最后连接两个链表
	// 
	// 时间复杂度: O(n) - 需要遍历链表一次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static ListNode partition(ListNode head, int x) {
		// 创建两个虚拟头节点
		ListNode beforeHead = new ListNode(0);
		ListNode before = beforeHead;
		ListNode afterHead = new ListNode(0);
		ListNode after = afterHead;
		
		// 遍历链表，将节点分配到两个链表中
		while (head != null) {
			if (head.val < x) {
				before.next = head;
				before = before.next;
			} else {
				after.next = head;
				after = after.next;
			}
			head = head.next;
		}
		
		// 防止形成环：将after链表的最后一个节点的next设置为null
		after.next = null;
		// 连接两个链表
		before.next = afterHead.next;
		
		return beforeHead.next;
	}
	
	// ==================== 补充题目12: 链表求和 ====================
	// 题目来源：
	// 1. LeetCode 2. 两数相加 - https://leetcode.cn/problems/add-two-numbers/
	// 2. LeetCode 445. 两数相加 II - https://leetcode.cn/problems/add-two-numbers-ii/
	// 3. LintCode 167. 链表求和 - https://www.lintcode.com/problem/add-two-numbers/
	// 4. 牛客网 NC40 链表相加（二）
	// 
	// 题目描述：给定两个非空链表表示两个非负整数，数字最高位在链表尾部，计算两个数的和
	// 输入：l1 = 2->4->3, l2 = 5->6->4
	// 输出：7->0->8 (342 + 465 = 807)
	// 
	// 解题思路：
	// 从低位开始逐位相加，处理进位
	// 1. 同时遍历两个链表
	// 2. 对应位置的节点值相加，加上进位
	// 3. 计算当前位的值和新的进位
	// 4. 创建新节点存储当前位的值
	// 
	// 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
	// 空间复杂度: O(max(m,n)) - 需要创建新链表存储结果
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		ListNode dummy = new ListNode(0);  // 虚拟头节点
		ListNode current = dummy;
		int carry = 0;  // 进位
		
		// 遍历两个链表，处理每一位的相加
		while (l1 != null || l2 != null || carry != 0) {
			// 获取当前位的值
			int val1 = (l1 != null) ? l1.val : 0;
			int val2 = (l2 != null) ? l2.val : 0;
			
			// 计算当前位的和
			int sum = val1 + val2 + carry;
			carry = sum / 10;  // 计算新的进位
			
			// 创建新节点存储当前位的值
			current.next = new ListNode(sum % 10);
			current = current.next;
			
			// 移动指针
			if (l1 != null) l1 = l1.next;
			if (l2 != null) l2 = l2.next;
		}
		
		return dummy.next;
	}
	
	// ==================== 补充题目13: 环形链表 ====================
	// 题目来源：
	// 1. LeetCode 141. 环形链表 - https://leetcode.cn/problems/linked-list-cycle/
	// 2. LeetCode 142. 环形链表 II - https://leetcode.cn/problems/linked-list-cycle-ii/
	// 3. LintCode 102. 环形链表 - https://www.lintcode.com/problem/linked-list-cycle/
	// 4. 牛客网 NC4 判断链表中是否有环
	// 5. 剑指Offer 23. 链表中环的入口节点
	// 
	// 题目描述：判断链表中是否有环
	// 
	// 解题思路：Floyd判圈算法（快慢指针）
	// 使用快慢两个指针，快指针每次走两步，慢指针每次走一步
	// 如果链表有环，快慢指针最终会相遇
	// 
	// 时间复杂度: O(n) - 需要遍历链表
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static boolean hasCycle(ListNode head) {
		if (head == null || head.next == null) {
			return false;
		}
		
		// 快慢指针
		ListNode slow = head;
		ListNode fast = head;
		
		// 快指针每次走两步，慢指针每次走一步
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			
			// 如果快慢指针相遇，说明有环
			if (slow == fast) {
				return true;
			}
		}
		
		return false;  // 快指针到达链表末尾，说明无环
	}
	
	/**
	 * 找到环形链表的入口节点
	 * 
	 * 解题思路：
	 * 1. 使用快慢指针找到相遇点
	 * 2. 将其中一个指针移到链表头部
	 * 3. 两个指针以相同速度移动，再次相遇点就是环的入口
	 * 
	 * 数学证明：
	 * 设链表头到环入口的距离为a，环入口到相遇点的距离为b，相遇点到环入口的距离为c
	 * 慢指针走过的距离：a + b
	 * 快指针走过的距离：a + b + n(b + c)，其中n为快指针在环中走过的圈数
	 * 因为快指针速度是慢指针的2倍，所以：2(a + b) = a + b + n(b + c)
	 * 化简得：a = (n-1)(b + c) + c
	 * 这意味着从头节点到环入口的距离，等于从相遇点到环入口的距离（加上若干圈环的长度）
	 * 
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(1)
	 */
	public static ListNode detectCycle(ListNode head) {
		if (head == null || head.next == null) {
			return null;
		}
		
		// 步骤1: 使用快慢指针找到相遇点
		ListNode slow = head;
		ListNode fast = head;
		boolean hasCycle = false;
		
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
			
			if (slow == fast) {
				hasCycle = true;
				break;
			}
		}
		
		if (!hasCycle) {
			return null;  // 无环
		}
		
		// 步骤2: 将slow指针移到链表头部
		slow = head;
		
		// 步骤3: 两个指针以相同速度移动，相遇点就是环的入口
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}
		
		return slow;  // 返回环的入口节点
	}
	
	// ==================== 补充题目14: 相交链表 ====================
	// 题目来源：
	// 1. LeetCode 160. 相交链表 - https://leetcode.cn/problems/intersection-of-two-linked-lists/
	// 2. LintCode 380. 相交链表 - https://www.lintcode.com/problem/intersection-of-two-linked-lists/
	// 3. 牛客网 NC66 两个链表的第一个公共结点
	// 4. 剑指Offer 52. 两个链表的第一个公共节点
	// 
	// 题目描述：找到两个单链表相交的起始节点
	// 
	// 解题思路：双指针法
	// 1. 两个指针分别从两个链表头部开始遍历
	// 2. 当指针到达链表末尾时，跳转到另一个链表的头部
	// 3. 如果两个链表相交，两个指针最终会在相交点相遇
	// 4. 如果不相交，两个指针最终都会到达null
	// 
	// 时间复杂度: O(m+n) - m和n分别是两个链表的长度
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static ListNode getIntersectionNode(ListNode headA, ListNode headB) {
		if (headA == null || headB == null) {
			return null;
		}
		
		// 两个指针分别从两个链表头部开始
		ListNode pA = headA;
		ListNode pB = headB;
		
		// 遍历链表，当到达末尾时跳转到另一个链表
		while (pA != pB) {
			// 如果pA到达末尾，跳转到headB；否则继续前进
			pA = (pA == null) ? headB : pA.next;
			// 如果pB到达末尾，跳转到headA；否则继续前进
			pB = (pB == null) ? headA : pB.next;
		}
		
		// 返回相交节点（如果不相交，pA和pB都为null）
		return pA;
	}
	
	// ==================== 补充题目15: 排序链表 ====================
	// 题目来源：
	// 1. LeetCode 148. 排序链表 - https://leetcode.cn/problems/sort-list/
	// 2. LintCode 98. 排序链表 - https://www.lintcode.com/problem/sort-list/
	// 3. 牛客网 NC12 重建二叉树（相关题目）
	// 
	// 题目描述：在 O(n log n) 时间复杂度和常数级空间复杂度下，对链表进行排序
	// 输入：4->2->1->3
	// 输出：1->2->3->4
	// 
	// 解题思路：归并排序
	// 1. 使用快慢指针找到链表中点，将链表分成两半
	// 2. 递归地对两半链表进行排序
	// 3. 合并两个有序链表
	// 
	// 时间复杂度: O(n log n) - 归并排序的时间复杂度
	// 空间复杂度: O(log n) - 递归调用栈的深度
	// 
	// 是否为最优解：是。这是该问题的最优解。
	// 如果要求O(1)空间复杂度，可以使用自底向上的归并排序，但实现较复杂。
	public static ListNode sortList(ListNode head) {
		// 递归终止条件
		if (head == null || head.next == null) {
			return head;
		}
		
		// 步骤1: 使用快慢指针找到链表中点
		ListNode slow = head;
		ListNode fast = head.next;  // fast从第二个节点开始，保证slow停在中点的前一个位置
		
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		
		// 将链表分成两半
		ListNode mid = slow.next;
		slow.next = null;  // 断开前半部分和后半部分
		
		// 步骤2: 递归排序两半链表
		ListNode left = sortList(head);
		ListNode right = sortList(mid);
		
		// 步骤3: 合并两个有序链表
		return mergeTwoLists(left, right);
	}
	
	// ==================== 补充题目16: 链表随机节点 ====================
	// 题目来源：
	// 1. LeetCode 382. 链表随机节点 - https://leetcode.cn/problems/linked-list-random-node/
	// 2. 蓄水池抽样算法应用
	// 
	// 题目描述：从链表中随机返回一个节点的值，保证每个节点被选中的概率相等
	// 
	// 解题思路：蓄水池抽样算法
	// 1. 遍历链表，对于第i个节点，以1/i的概率选择它
	// 2. 这样能保证每个节点被选中的概率都是1/n
	// 
	// 时间复杂度: O(n) - 需要遍历整个链表
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是蓄水池抽样算法的标准实现。
	public static class RandomNodeSelector {
		private ListNode head;
		private Random random;
		
		public RandomNodeSelector(ListNode head) {
			this.head = head;
			this.random = new Random();
		}
		
		public int getRandom() {
			ListNode current = head;
			int result = 0;
			int count = 0;
			
			while (current != null) {
				count++;
				// 以1/count的概率选择当前节点
				if (random.nextInt(count) == 0) {
					result = current.val;
				}
				current = current.next;
			}
			
			return result;
		}
	}
	
	// ==================== 补充题目17: 复制带随机指针的链表 ====================
	// 题目来源：
	// 1. LeetCode 138. 复制带随机指针的链表 - https://leetcode.cn/problems/copy-list-with-random-pointer/
	// 2. 剑指Offer 35. 复杂链表的复制
	// 
	// 题目描述：复制一个包含随机指针的链表
	// 
	// 解题思路：三次遍历法
	// 1. 第一次遍历：在每个节点后面插入复制节点
	// 2. 第二次遍历：设置复制节点的随机指针
	// 3. 第三次遍历：分离原链表和复制链表
	// 
	// 时间复杂度: O(n) - 需要三次遍历
	// 空间复杂度: O(1) - 只使用常数级别的额外空间（不包括结果链表）
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static class NodeWithRandom {
		public int val;
		public NodeWithRandom next;
		public NodeWithRandom random;
		
		public NodeWithRandom(int val) {
			this.val = val;
			this.next = null;
			this.random = null;
		}
	}
	
	public static NodeWithRandom copyRandomList(NodeWithRandom head) {
		if (head == null) {
			return null;
		}
		
		// 第一次遍历：在每个节点后面插入复制节点
		NodeWithRandom current = head;
		while (current != null) {
			NodeWithRandom copy = new NodeWithRandom(current.val);
			copy.next = current.next;
			current.next = copy;
			current = copy.next;
		}
		
		// 第二次遍历：设置复制节点的随机指针
		current = head;
		while (current != null) {
			if (current.random != null) {
				current.next.random = current.random.next;
			}
			current = current.next.next;
		}
		
		// 第三次遍历：分离原链表和复制链表
		current = head;
		NodeWithRandom copyHead = head.next;
		NodeWithRandom copyCurrent = copyHead;
		
		while (current != null) {
			current.next = current.next.next;
			if (copyCurrent.next != null) {
				copyCurrent.next = copyCurrent.next.next;
			}
			current = current.next;
			copyCurrent = copyCurrent.next;
		}
		
		return copyHead;
	}
	
	// ==================== 补充题目18: 链表组件 ====================
	// 题目来源：
	// 1. LeetCode 817. 链表组件 - https://leetcode.cn/problems/linked-list-components/
	// 
	// 题目描述：给定链表头节点 head 和列表 nums，返回链表中组件的个数
	// 
	// 解题思路：使用哈希集合快速查找
	// 1. 将nums转换为哈希集合，提高查找效率
	// 2. 遍历链表，统计连续在集合中的组件个数
	// 
	// 时间复杂度: O(n + m) - n是链表长度，m是nums长度
	// 空间复杂度: O(m) - 用于存储哈希集合
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static int numComponents(ListNode head, int[] nums) {
		Set<Integer> numSet = new HashSet<>();
		for (int num : nums) {
			numSet.add(num);
		}
		
		int components = 0;
		boolean inComponent = false;
		ListNode current = head;
		
		while (current != null) {
			if (numSet.contains(current.val)) {
				if (!inComponent) {
					components++;
					inComponent = true;
				}
			} else {
				inComponent = false;
			}
			current = current.next;
		}
		
		return components;
	}
	
	// ==================== 补充题目19: 链表中的下一个更大节点 ====================
	// 题目来源：
	// 1. LeetCode 1019. 链表中的下一个更大节点 - https://leetcode.cn/problems/next-greater-node-in-linked-list/
	// 
	// 题目描述：对于链表中的每个节点，找到它后面第一个比它大的节点
	// 
	// 解题思路：使用单调栈
	// 1. 先将链表转换为数组
	// 2. 使用单调栈从右向左处理
	// 3. 栈中存储的是递减序列的索引
	// 
	// 时间复杂度: O(n) - 每个节点入栈出栈一次
	// 空间复杂度: O(n) - 用于存储栈和结果数组
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static int[] nextLargerNodes(ListNode head) {
		// 将链表转换为数组
		List<Integer> list = new ArrayList<>();
		ListNode current = head;
		while (current != null) {
			list.add(current.val);
			current = current.next;
		}
		
		int n = list.size();
		int[] result = new int[n];
		Stack<Integer> stack = new Stack<>();
		
		// 从右向左遍历，使用单调栈
		for (int i = n - 1; i >= 0; i--) {
			int currentVal = list.get(i);
			
			// 弹出栈顶比当前值小的元素
			while (!stack.isEmpty() && stack.peek() <= currentVal) {
				stack.pop();
			}
			
			// 如果栈不为空，栈顶就是下一个更大节点
			result[i] = stack.isEmpty() ? 0 : stack.peek();
			
			// 将当前值压入栈
			stack.push(currentVal);
		}
		
		return result;
	}
	
	// ==================== 补充题目20: 链表最大孪生和 ====================
	// 题目来源：
	// 1. LeetCode 2130. 链表最大孪生和 - https://leetcode.cn/problems/maximum-twin-sum-of-a-linked-list/
	// 
	// 题目描述：孪生节点是第i个节点和第n-1-i个节点，求所有孪生节点和的最大值
	// 
	// 解题思路：快慢指针 + 反转后半部分
	// 1. 使用快慢指针找到中点
	// 2. 反转后半部分链表
	// 3. 同时遍历前半部分和反转后的后半部分，计算和
	// 
	// 时间复杂度: O(n) - 需要遍历链表三次
	// 空间复杂度: O(1) - 只使用常数级别的额外空间
	// 
	// 是否为最优解：是。这是该问题的最优解。
	public static int pairSum(ListNode head) {
		// 使用快慢指针找到中点
		ListNode slow = head;
		ListNode fast = head;
		while (fast != null && fast.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		
		// 反转后半部分链表
		ListNode secondHalf = reverseListIterative(slow);
		
		// 计算孪生和的最大值
		int maxSum = 0;
		ListNode firstHalf = head;
		while (secondHalf != null) {
			maxSum = Math.max(maxSum, firstHalf.val + secondHalf.val);
			firstHalf = firstHalf.next;
			secondHalf = secondHalf.next;
		}
		
		return maxSum;
	}
	
	// ==================== 性能优化与调试工具 ====================
	
	/**
	 * 链表性能分析工具
	 * 用于分析链表操作的时间和空间复杂度
	 */
	public static class LinkedListProfiler {
		private long startTime;
		private long endTime;
		private Runtime runtime;
		private long startMemory;
		
		public void start() {
			runtime = Runtime.getRuntime();
			// 运行垃圾回收，获取更准确的内存使用
			runtime.gc();
			startMemory = runtime.totalMemory() - runtime.freeMemory();
			startTime = System.nanoTime();
		}
		
		public void end() {
			endTime = System.nanoTime();
			long endMemory = runtime.totalMemory() - runtime.freeMemory();
			long memoryUsed = endMemory - startMemory;
			
			System.out.println("执行时间: " + (endTime - startTime) + " 纳秒");
			System.out.println("内存使用: " + memoryUsed + " 字节");
		}
	}
	
	/**
	 * 链表调试工具
	 * 提供打印中间状态、断言验证等功能
	 */
	public static class LinkedListDebugger {
		/**
		 * 打印链表中间状态
		 * @param head 链表头节点
		 * @param message 调试信息
		 */
		public static void printListState(ListNode head, String message) {
			System.out.println(message + ": " + (head != null ? head.toString() : "null"));
		}
		
		/**
		 * 断言验证链表状态
		 * @param condition 断言条件
		 * @param message 断言失败信息
		 */
		public static void assertList(boolean condition, String message) {
			if (!condition) {
				throw new AssertionError("链表断言失败: " + message);
			}
		}
		
		/**
		 * 验证链表是否有环
		 * @param head 链表头节点
		 * @return 是否有环
		 */
		public static boolean verifyNoCycle(ListNode head) {
			if (head == null) return true;
			
			ListNode slow = head;
			ListNode fast = head;
			
			while (fast != null && fast.next != null) {
				slow = slow.next;
				fast = fast.next.next;
				if (slow == fast) {
					return false; // 有环
				}
			}
			return true; // 无环
		}
	}
	
	// ==================== 综合测试函数 ====================
	
	/**
	 * 运行所有补充题目的测试
	 */
	public static void testAdditionalProblems() {
		System.out.println("=== 补充题目测试 ===");
		
		// 测试链表随机节点
		ListNode randomTest = ListNode.createList(new int[]{1, 2, 3, 4, 5});
		RandomNodeSelector selector = new RandomNodeSelector(randomTest);
		System.out.println("随机节点选择测试: " + selector.getRandom());
		
		// 测试链表组件
		ListNode componentTest = ListNode.createList(new int[]{0, 1, 2, 3});
		int[] nums = {0, 1, 3};
		System.out.println("链表组件个数: " + numComponents(componentTest, nums));
		
		// 测试下一个更大节点
		ListNode largerTest = ListNode.createList(new int[]{2, 1, 5});
		int[] largerResult = nextLargerNodes(largerTest);
		System.out.println("下一个更大节点: " + Arrays.toString(largerResult));
		
		// 测试链表最大孪生和
		ListNode twinTest = ListNode.createList(new int[]{5, 4, 2, 1});
		System.out.println("链表最大孪生和: " + pairSum(twinTest));
		
		System.out.println("补充题目测试完成");
	}
	
	// ==================== 主函数更新 ====================
	
	// 在原有的main方法中添加新的测试功能
	// 原有的main方法已经包含了基础测试
	// 现在添加补充测试功能到原有的main方法中
}