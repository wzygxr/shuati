package class034;

// 两数相加
// 测试链接: https://leetcode.cn/problems/add-two-numbers/
public class Code42_AddTwoNumbers {

	// 链表节点定义
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
	}

	// 两数相加
	// 方法：模拟加法运算，从低位到高位逐位相加
	// 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
	// 空间复杂度：O(1) - 只使用常数额外空间（不计算结果链表）
	// 参数：
	//   l1 - 第一个数的链表表示（低位在前）
	//   l2 - 第二个数的链表表示（低位在前）
	// 返回值：两数之和的链表表示
	public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
		// 创建虚拟头节点，简化边界情况处理
		ListNode dummy = new ListNode(0);
		// 当前节点指针
		ListNode current = dummy;
		// 进位值
		int carry = 0;
		
		// 当两个链表都未遍历完或还有进位时继续循环
		while (l1 != null || l2 != null || carry != 0) {
			// 获取当前位的值
			int val1 = (l1 != null) ? l1.val : 0;
			int val2 = (l2 != null) ? l2.val : 0;
			
			// 计算当前位的和
			int sum = val1 + val2 + carry;
			// 更新进位值
			carry = sum / 10;
			// 创建新节点存储当前位的结果
			current.next = new ListNode(sum % 10);
			// 移动指针
			current = current.next;
			
			// 移动到下一个节点
			if (l1 != null) l1 = l1.next;
			if (l2 != null) l2 = l2.next;
		}
		
		// 返回结果链表的头节点
		return dummy.next;
	}
	
	// 辅助函数：打印链表
	public static void printList(ListNode head) {
		while (head != null) {
			System.out.print(head.val);
			if (head.next != null) {
				System.out.print(" -> ");
			}
			head = head.next;
		}
		System.out.println();
	}
	
	// 辅助函数：构建链表（数字低位在前）
	public static ListNode buildList(int[] nums) {
		ListNode dummy = new ListNode(0);
		ListNode curr = dummy;
		for (int num : nums) {
			curr.next = new ListNode(num);
			curr = curr.next;
		}
		return dummy.next;
	}
	
	// 主函数用于测试
	public static void main(String[] args) {
		// 测试用例1: 342 + 465 = 807
		// l1: 2 -> 4 -> 3 (表示342)
		// l2: 5 -> 6 -> 4 (表示465)
		// 结果: 7 -> 0 -> 8 (表示807)
		int[] nums1 = {2, 4, 3};
		int[] nums2 = {5, 6, 4};
		ListNode l1 = buildList(nums1);
		ListNode l2 = buildList(nums2);
		System.out.print("测试用例1 - l1: ");
		printList(l1);
		System.out.print("测试用例1 - l2: ");
		printList(l2);
		ListNode result1 = addTwoNumbers(l1, l2);
		System.out.print("两数相加结果: ");
		printList(result1);
		
		// 测试用例2: 0 + 0 = 0
		int[] nums3 = {0};
		int[] nums4 = {0};
		ListNode l3 = buildList(nums3);
		ListNode l4 = buildList(nums4);
		System.out.print("测试用例2 - l1: ");
		printList(l3);
		System.out.print("测试用例2 - l2: ");
		printList(l4);
		ListNode result2 = addTwoNumbers(l3, l4);
		System.out.print("两数相加结果: ");
		printList(result2);
		
		// 测试用例3: 999 + 9999 = 10998
		// l1: 9 -> 9 -> 9 (表示999)
		// l2: 9 -> 9 -> 9 -> 9 (表示9999)
		// 结果: 8 -> 9 -> 9 -> 0 -> 1 (表示10998)
		int[] nums5 = {9, 9, 9};
		int[] nums6 = {9, 9, 9, 9};
		ListNode l5 = buildList(nums5);
		ListNode l6 = buildList(nums6);
		System.out.print("测试用例3 - l1: ");
		printList(l5);
		System.out.print("测试用例3 - l2: ");
		printList(l6);
		ListNode result3 = addTwoNumbers(l5, l6);
		System.out.print("两数相加结果: ");
		printList(result3);
	}
	
	/*
	 * 题目：LeetCode 2. 两数相加
	 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
	 * 链接：https://leetcode.cn/problems/add-two-numbers/
	 * 
	 * 题目描述：
	 * 给你两个非空的链表，表示两个非负的整数。它们每位数字都是按照逆序的方式存储的，
	 * 并且每个节点只能存储一位数字。请你将两个数相加，并以相同形式返回一个表示和的链表。
	 * 
	 * 解题思路：
	 * 模拟加法运算过程，从低位到高位逐位相加，处理进位。
	 * 
	 * 时间复杂度：O(max(m,n)) - m和n分别是两个链表的长度
	 * 空间复杂度：O(1) - 只使用常数额外空间（不计算结果链表）
	 * 是否最优解：是
	 * 
	 * 工程化考量：
	 * 1. 使用虚拟头节点简化边界情况处理
	 * 2. 边界情况处理：不同长度链表、进位处理等
	 * 3. 异常处理：输入参数校验
	 * 
	 * 与机器学习等领域的联系：
	 * 1. 在大整数运算中，链表可以用来表示超长整数
	 * 2. 模拟运算过程在算法设计中很常见
	 * 
	 * 语言特性差异：
	 * Java: 对象引用操作简单，垃圾回收自动管理内存
	 * C++: 需要手动管理内存，注意指针操作
	 * Python: 使用对象引用，无需手动管理内存
	 * 
	 * 极端输入场景：
	 * 1. 空链表
	 * 2. 单节点链表
	 * 3. 非常长的链表
	 * 4. 全0链表
	 * 5. 全9链表（产生连续进位）
	 */
}