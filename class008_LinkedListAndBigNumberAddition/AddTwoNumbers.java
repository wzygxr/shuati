

// 给你两个 非空 的链表，表示两个非负的整数
// 它们每位数字都是按照 逆序 的方式存储的，并且每个节点只能存储 一位 数字
// 请你将两个数相加，并以相同形式返回一个表示和的链表。
// 你可以假设除了数字 0 之外，这两个数都不会以 0 开头
// 测试链接：https://leetcode.cn/problems/add-two-numbers/

/**
 * 链表相加及相关题目扩展
 * 包含LeetCode、LintCode、牛客网、剑指Offer等多个平台的相关题目
 * 每个题目都提供详细的解题思路、复杂度分析、多种解法以及多语言实现
 * 
 * 主要题目：
 * 1. LeetCode 2. 两数相加 (基础题) - 本文件原始题目
 * 2. LeetCode 445. 两数相加 II (进阶题) - 数字最高位位于链表开始位置
 * 3. LeetCode 369. 给单链表加一 (变种题) - 链表表示的数字加一
 * 4. LeetCode 66. 加一 (数组形式) - 数组形式的数字加一
 * 5. LeetCode 989. 数组形式的整数加法 - 数组形式与整数相加
 * 6. LeetCode 415. 字符串相加 - 字符串形式的数字相加
 * 7. LeetCode 67. 二进制求和 - 二进制字符串相加
 * 8. 牛客网 BM86 大数加法 - 字符串形式的大数相加
 * 9. 牛客网 NC40 链表相加（二） - 大数相加的链表实现
 * 10. 剑指Offer 06. 从尾到头打印链表 - 链表遍历的逆序处理
 * 11. LintCode 165. 合并两个排序链表 - 链表操作的变种应用
 * 12. HackerRank BigInteger Addition - 大数加法的通用实现
 * 13. Codeforces 1077C - Good Array - 数组操作与进位思想应用
 * 
 * 解题思路技巧总结：
 * 1. 链表相加：处理进位、对齐不同长度链表、处理最后的进位
 * 2. 链表逆序相加：使用栈或先反转链表
 * 3. 链表加一：递归处理或从后往前处理进位
 * 4. 数组加法：从后往前处理进位
 * 5. 字符串加法：模拟手工加法过程
 * 6. 二进制加法：逢二进一的处理
 * 7. 大数运算通用技巧：避免溢出、逐位处理、进位管理
 * 
 * 时间复杂度分析：
 * 1. 两数相加：O(max(m,n))，m和n分别是两个链表的长度
 * 2. 两数相加 II：O(max(m,n))，使用栈或反转链表
 * 3. 给单链表加一：O(n)，n是链表长度
 * 4. 加一：O(n)，n是数组长度
 * 5. 数组形式的整数加法：O(max(n,log k))，n是数组长度，k是整数
 * 6. 字符串相加：O(max(m,n))，m和n分别是两个字符串的长度
 * 7. 二进制求和：O(max(m,n))，m和n分别是两个字符串的长度
 * 
 * 空间复杂度分析：
 * 1. 两数相加：O(1)，不考虑返回结果的空间
 * 2. 两数相加 II：O(max(m,n))，使用栈的空间
 * 3. 给单链表加一：O(n)，递归调用栈的深度
 * 4. 加一：O(1)，原地修改或O(n)，创建新数组
 * 5. 数组形式的整数加法：O(max(n,log k))
 * 6. 字符串相加：O(1)，不考虑返回结果的空间
 * 7. 二进制求和：O(1)，不考虑返回结果的空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理空输入、边界情况
 * 2. 性能优化：避免重复计算、优化内存使用
 * 3. 代码可读性：清晰的变量命名和注释
 * 4. 可测试性：包含完整的测试用例
 */
public class AddTwoNumbers {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;

		public ListNode(int val) {
			this.val = val;
		}

		public ListNode(int val, ListNode next) {
			this.val = val;
			this.next = next;
		}
		
		/**
		 * 用于测试的链表创建方法
		 */
		public static ListNode createList(int[] arr) {
			if (arr == null || arr.length == 0) return null;
			ListNode head = new ListNode(arr[0]);
			ListNode cur = head;
			for (int i = 1; i < arr.length; i++) {
				cur.next = new ListNode(arr[i]);
				cur = cur.next;
			}
			return head;
		}
		
		/**
		 * 用于测试的链表打印方法
		 */
		public static void printList(ListNode head) {
			ListNode cur = head;
			while (cur != null) {
				System.out.print(cur.val);
				if (cur.next != null) System.out.print(" -> ");
				cur = cur.next;
			}
			System.out.println();
		}
	}

	/**
	 * 题目1: LeetCode 2. 两数相加 (Add Two Numbers)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-two-numbers/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 给你两个 非空 的链表，表示两个非负的整数。它们每位数字都是按照 逆序 的方式存储的，
	 * 并且每个节点只能存储 一位 数字。请你将两个数相加，并以相同形式返回一个表示和的链表。
	 * 你可以假设除了数字 0 之外，这两个数都不会以 0 开头
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(m,n)), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 1. 同时遍历两个链表，逐位相加
	 * 2. 处理进位：使用carry变量记录进位值
	 * 3. 处理不同长度：当一个链表遍历完后，继续处理另一个链表
	 * 4. 处理最后进位：如果最后还有进位，需要添加新节点
	 * 5. 使用哨兵节点简化边界处理
	 */
	static class AddTwoNumbersSolution {
		
		/**
		 * 解法: 模拟加法 (推荐)
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
		 * 空间复杂度: O(1) - 不考虑返回结果的空间
		 * 
		 * 核心思想：
		 * 1. 使用carry变量记录进位
		 * 2. 同时遍历两个链表
		 * 3. 处理不同长度链表
		 * 4. 处理最后的进位
		 */
		public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
			// 创建哨兵节点，简化边界处理
			ListNode dummy = new ListNode(0);
			ListNode current = dummy;
			int carry = 0;  // 进位值
			
			// 同时遍历两个链表
			while (l1 != null || l2 != null) {
				// 获取当前节点的值，如果节点为空则为0
				int x = (l1 != null) ? l1.val : 0;
				int y = (l2 != null) ? l2.val : 0;
				
				// 计算当前位的和
				int sum = x + y + carry;
				
				// 更新进位值
				carry = sum / 10;
				
				// 创建新节点存储当前位的结果
				current.next = new ListNode(sum % 10);
				current = current.next;
				
				// 移动链表指针
				if (l1 != null) l1 = l1.next;
				if (l2 != null) l2 = l2.next;
			}
			
			// 处理最后的进位
			if (carry > 0) {
				current.next = new ListNode(carry);
			}
			
			// 返回结果链表
			return dummy.next;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 两数相加测试 ===");
			
			// 测试用例1: 正常情况
			ListNode l1 = ListNode.createList(new int[]{2, 4, 3});  // 342
			ListNode l2 = ListNode.createList(new int[]{5, 6, 4});  // 465
			System.out.print("链表1 (342): ");
			ListNode.printList(l1);
			System.out.print("链表2 (465): ");
			ListNode.printList(l2);
			
			ListNode result1 = addTwoNumbers(l1, l2);  // 807
			System.out.print("结果 (807): ");
			ListNode.printList(result1);
			
			// 测试用例2: 包含进位
			ListNode l3 = ListNode.createList(new int[]{9, 9, 9, 9, 9, 9, 9});  // 9999999
			ListNode l4 = ListNode.createList(new int[]{9, 9, 9, 9});  // 9999
			System.out.print("链表1 (9999999): ");
			ListNode.printList(l3);
			System.out.print("链表2 (9999): ");
			ListNode.printList(l4);
			
			ListNode result2 = addTwoNumbers(l3, l4);  // 10009998
			System.out.print("结果 (10009998): ");
			ListNode.printList(result2);
			
			// 测试用例3: 不同长度
			ListNode l5 = ListNode.createList(new int[]{0});  // 0
			ListNode l6 = ListNode.createList(new int[]{0});  // 0
			System.out.print("链表1 (0): ");
			ListNode.printList(l5);
			System.out.print("链表2 (0): ");
			ListNode.printList(l6);
			
			ListNode result3 = addTwoNumbers(l5, l6);  // 0
			System.out.print("结果 (0): ");
			ListNode.printList(result3);
			System.out.println();
		}
	}
	
	/**
	 * 题目2: LeetCode 445. 两数相加 II (Add Two Numbers II)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-two-numbers-ii/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 给你两个 非空 链表来代表两个非负整数。数字最高位位于链表开始位置。
	 * 它们的每个节点只存储一位数字。将这两数相加会返回一个新的链表。
	 * 你可以假设除了数字 0 之外，这两个数字都不会以零开头
	 * 
	 * 解法分析：
	 * 1. 使用栈 - 时间复杂度: O(max(m,n)), 空间复杂度: O(m+n)
	 * 2. 反转链表 - 时间复杂度: O(max(m,n)), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 由于数字最高位在链表开始位置，我们需要从链表末尾开始相加，这与我们正常的加法顺序相反
	 * 解决方案：
	 * 1. 使用栈：将两个链表的值分别压入栈中，然后依次弹出相加
	 * 2. 反转链表：先将两个链表反转，然后使用两数相加的方法，最后将结果反转
	 */
	static class AddTwoNumbersIISolution {
		
		/**
		 * 解法1: 使用栈 (推荐)
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
		 * 空间复杂度: O(m+n) - 两个栈的空间
		 * 
		 * 核心思想：
		 * 1. 使用两个栈分别存储两个链表的值
		 * 2. 依次弹出栈顶元素相加
		 * 3. 处理进位
		 * 4. 头插法构建结果链表
		 */
		public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
			// 使用栈存储两个链表的值
			java.util.Stack<Integer> stack1 = new java.util.Stack<>();
			java.util.Stack<Integer> stack2 = new java.util.Stack<>();
			
			// 将链表l1的值压入stack1
			while (l1 != null) {
				stack1.push(l1.val);
				l1 = l1.next;
			}
			
			// 将链表l2的值压入stack2
			while (l2 != null) {
				stack2.push(l2.val);
				l2 = l2.next;
			}
			
			ListNode head = null;  // 结果链表的头节点
			int carry = 0;  // 进位值
			
			// 依次弹出栈顶元素相加
			while (!stack1.isEmpty() || !stack2.isEmpty() || carry != 0) {
				// 获取当前位的值，如果栈为空则为0
				int x = stack1.isEmpty() ? 0 : stack1.pop();
				int y = stack2.isEmpty() ? 0 : stack2.pop();
				
				// 计算当前位的和
				int sum = x + y + carry;
				
				// 更新进位值
				carry = sum / 10;
				
				// 创建新节点，使用头插法插入到结果链表的头部
				ListNode node = new ListNode(sum % 10);
				node.next = head;
				head = node;
			}
			
			return head;
		}
		
		/**
		 * 解法2: 反转链表
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
		 * 空间复杂度: O(1) - 不考虑返回结果的空间
		 * 
		 * 核心思想：
		 * 1. 反转两个链表
		 * 2. 使用两数相加的方法
		 * 3. 反转结果链表
		 */
		public static ListNode addTwoNumbersReverse(ListNode l1, ListNode l2) {
			// 反转两个链表
			l1 = reverseList(l1);
			l2 = reverseList(l2);
			
			// 使用两数相加的方法
			ListNode result = AddTwoNumbersSolution.addTwoNumbers(l1, l2);
			
			// 反转结果链表
			return reverseList(result);
		}
		
		/**
		 * 反转链表的辅助方法
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
			System.out.println("=== 两数相加 II 测试 ===");
			
			// 测试用例1: 正常情况
			ListNode l1 = ListNode.createList(new int[]{7, 2, 4, 3});  // 7243
			ListNode l2 = ListNode.createList(new int[]{5, 6, 4});     // 564
			System.out.print("链表1 (7243): ");
			ListNode.printList(l1);
			System.out.print("链表2 (564): ");
			ListNode.printList(l2);
			
			ListNode result1 = addTwoNumbers(l1, l2);  // 7807
			System.out.print("栈方法结果 (7807): ");
			ListNode.printList(result1);
			
			// 重新创建测试数据
			l1 = ListNode.createList(new int[]{7, 2, 4, 3});  // 7243
			l2 = ListNode.createList(new int[]{5, 6, 4});     // 564
			ListNode result2 = addTwoNumbersReverse(l1, l2);  // 7807
			System.out.print("反转链表方法结果 (7807): ");
			ListNode.printList(result2);
			
			// 测试用例2: 包含进位
			ListNode l3 = ListNode.createList(new int[]{5});  // 5
			ListNode l4 = ListNode.createList(new int[]{5});  // 5
			System.out.print("链表1 (5): ");
			ListNode.printList(l3);
			System.out.print("链表2 (5): ");
			ListNode.printList(l4);
			
			ListNode result3 = addTwoNumbers(l3, l4);  // 10
			System.out.print("结果 (10): ");
			ListNode.printList(result3);
			System.out.println();
		}
	}
	
	/**
	 * 题目3: LeetCode 369. 给单链表加一 (Plus One Linked List)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/plus-one-linked-list/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 用一个 非空 单链表来表示一个非负整数，然后将这个整数加一。
	 * 你可以假设这个整数除了 0 本身，没有任何前导的 0，这个整数的各个数位按照 高位在链表头部、低位在链表尾部 的顺序排列
	 * 
	 * 解法分析：
	 * 1. 递归法 - 时间复杂度: O(n), 空间复杂度: O(n)
	 * 2. 找到最后一个非9节点 - 时间复杂度: O(n), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 由于数字高位在链表头部，我们需要从链表尾部开始加一，这需要处理进位
	 * 解决方案：
	 * 1. 递归法：递归到链表末尾，然后回溯时处理进位
	 * 2. 找到最后一个非9节点：找到最后一个不为9的节点，将其加一，后面所有节点置为0
	 */
	static class PlusOneLinkedListSolution {
		
		/**
		 * 解法1: 递归法
		 * 时间复杂度: O(n) - n是链表长度
		 * 空间复杂度: O(n) - 递归调用栈的深度
		 * 
		 * 核心思想：
		 * 1. 递归到链表末尾
		 * 2. 在回溯过程中处理进位
		 * 3. 如果最高位还有进位，需要添加新节点
		 */
		public static ListNode plusOne(ListNode head) {
			// 递归处理链表，返回进位值
			int carry = helper(head);
			
			// 如果还有进位，添加新节点
			if (carry == 1) {
				ListNode newHead = new ListNode(1);
				newHead.next = head;
				return newHead;
			}
			
			return head;
		}
		
		/**
		 * 递归辅助方法：返回进位值
		 */
		private static int helper(ListNode node) {
			// 递归终止条件：到达链表末尾，返回1表示加1
			if (node == null) return 1;
			
			// 递归处理下一个节点，获取进位
			int carry = helper(node.next);
			
			// 当前节点值加上进位
			int sum = node.val + carry;
			
			// 更新当前节点值
			node.val = sum % 10;
			
			// 返回新的进位
			return sum / 10;
		}
		
		/**
		 * 解法2: 找到最后一个非9节点
		 * 时间复杂度: O(n) - n是链表长度
		 * 空间复杂度: O(1) - 只使用常数级别的额外空间
		 * 
		 * 核心思想：
		 * 1. 使用哨兵节点处理最高位进位的情况
		 * 2. 找到最后一个不为9的节点
		 * 3. 将该节点加一，后面所有节点置为0
		 */
		public static ListNode plusOneOptimized(ListNode head) {
			// 创建哨兵节点，简化最高位进位的处理
			ListNode sentinel = new ListNode(0);
			sentinel.next = head;
			
			// 找到最后一个不为9的节点
			ListNode lastNonNine = sentinel;
			ListNode current = head;
			
			while (current != null) {
				if (current.val != 9) {
					lastNonNine = current;
				}
				current = current.next;
			}
			
			// 将最后一个不为9的节点加一
			lastNonNine.val++;
			
			// 后面所有节点置为0
			current = lastNonNine.next;
			while (current != null) {
				current.val = 0;
				current = current.next;
			}
			
			// 如果哨兵节点的值为1，说明最高位有进位，返回哨兵节点
			// 否则返回原链表头节点
			return (sentinel.val == 1) ? sentinel : sentinel.next;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 给单链表加一测试 ===");
			
			// 测试用例1: 正常情况
			ListNode head1 = ListNode.createList(new int[]{1, 2, 3});  // 123
			System.out.print("链表 (123): ");
			ListNode.printList(head1);
			
			ListNode result1 = plusOne(head1);  // 124
			System.out.print("递归法结果 (124): ");
			ListNode.printList(result1);
			
			// 测试用例2: 包含进位
			ListNode head2 = ListNode.createList(new int[]{1, 2, 9});  // 129
			System.out.print("链表 (129): ");
			ListNode.printList(head2);
			
			ListNode result2 = plusOne(head2);  // 130
			System.out.print("递归法结果 (130): ");
			ListNode.printList(result2);
			
			// 测试用例3: 全为9的情况
			ListNode head3 = ListNode.createList(new int[]{9, 9, 9});  // 999
			System.out.print("链表 (999): ");
			ListNode.printList(head3);
			
			ListNode result3 = plusOne(head3);  // 1000
			System.out.print("递归法结果 (1000): ");
			ListNode.printList(result3);
			
			// 重新创建测试数据
			head3 = ListNode.createList(new int[]{9, 9, 9});  // 999
			ListNode result4 = plusOneOptimized(head3);  // 1000
			System.out.print("优化方法结果 (1000): ");
			ListNode.printList(result4);
			System.out.println();
		}
	}
	
	/**
	 * 题目4: LeetCode 66. 加一 (Plus One)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/plus-one/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给定一个由 整数 组成的 非空 数组所表示的非负整数，在该数的基础上加一。
	 * 最高位数字存放在数组的首位， 数组中每个元素只存储单个数字。
	 * 你可以假设除了整数 0 之外，这个整数不会以零开头
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(n), 空间复杂度: O(1)或O(n)
	 * 
	 * 解题思路：
	 * 从数组末尾开始加一，处理进位情况，如果最高位还有进位需要创建新数组
	 */
	static class PlusOneSolution {
		
		/**
		 * 解法: 模拟加法
		 * 时间复杂度: O(n) - n是数组长度
		 * 空间复杂度: O(1) - 原地修改，最坏情况O(n)需要创建新数组
		 * 
		 * 核心思想：
		 * 1. 从数组末尾开始加一
		 * 2. 处理进位
		 * 3. 如果最高位还有进位，需要创建新数组
		 */
		public static int[] plusOne(int[] digits) {
			// 从数组末尾开始遍历
			for (int i = digits.length - 1; i >= 0; i--) {
				// 当前位加一
				digits[i]++;
				
				// 如果没有进位，直接返回结果
				if (digits[i] < 10) {
					return digits;
				}
				
				// 如果有进位，当前位设为0，继续向高位进位
				digits[i] = 0;
			}
			
			// 如果最高位也需要进位，创建新数组
			int[] newDigits = new int[digits.length + 1];
			newDigits[0] = 1;  // 最高位设为1
			return newDigits;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 加一测试 ===");
			
			// 测试用例1: 正常情况
			int[] digits1 = {1, 2, 3};  // 123
			System.out.println("数组 (123): " + java.util.Arrays.toString(digits1));
			
			int[] result1 = plusOne(digits1);  // 124
			System.out.println("结果 (124): " + java.util.Arrays.toString(result1));
			
			// 测试用例2: 包含进位
			int[] digits2 = {4, 3, 2, 1};  // 4321
			System.out.println("数组 (4321): " + java.util.Arrays.toString(digits2));
			
			int[] result2 = plusOne(digits2);  // 4322
			System.out.println("结果 (4322): " + java.util.Arrays.toString(result2));
			
			// 测试用例3: 全为9的情况
			int[] digits3 = {9};  // 9
			System.out.println("数组 (9): " + java.util.Arrays.toString(digits3));
			
			int[] result3 = plusOne(digits3);  // 10
			System.out.println("结果 (10): " + java.util.Arrays.toString(result3));
			
			// 测试用例4: 多个9的情况
			int[] digits4 = {9, 9, 9};  // 999
			System.out.println("数组 (999): " + java.util.Arrays.toString(digits4));
			
			int[] result4 = plusOne(digits4);  // 1000
			System.out.println("结果 (1000): " + java.util.Arrays.toString(result4));
			System.out.println();
		}
	}
	
	/**
	 * 题目5: LeetCode 989. 数组形式的整数加法 (Add to Array-Form of Integer)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-to-array-form-of-integer/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 整数的 数组形式 num 是按照从左到右的顺序表示其数字的数组。例如，对于 num = 1321 ，数组形式是 [1,3,2,1] 。
	 * 给定 num ，整数的 数组形式 ，和整数 k ，返回 整数 num + k 的 数组形式
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(n,log k)), 空间复杂度: O(max(n,log k))
	 * 
	 * 解题思路：
	 * 从数组末尾和整数k的末尾开始相加，处理进位情况
	 */
	static class AddToArrayFormSolution {
		
		/**
		 * 解法: 模拟加法
		 * 时间复杂度: O(max(n,log k)) - n是数组长度，log k是k的位数
		 * 空间复杂度: O(max(n,log k)) - 结果数组的空间
		 * 
		 * 核心思想：
		 * 1. 从数组末尾和整数k的末尾开始相加
		 * 2. 处理进位
		 * 3. 将结果存储在列表中，最后反转
		 */
		public static java.util.List<Integer> addToArrayForm(int[] num, int k) {
			java.util.List<Integer> result = new java.util.ArrayList<>();
			int carry = 0;  // 进位值
			int i = num.length - 1;  // 数组索引
			
			// 从数组末尾和整数k的末尾开始相加
			while (i >= 0 || k > 0 || carry != 0) {
				// 获取当前位的值
				int x = (i >= 0) ? num[i] : 0;  // 数组当前位的值
				int y = k % 10;  // k的当前位的值
				
				// 计算当前位的和
				int sum = x + y + carry;
				
				// 更新进位值
				carry = sum / 10;
				
				// 将当前位的结果添加到结果列表的开头
				result.add(0, sum % 10);
				
				// 移动索引和k
				i--;
				k /= 10;
			}
			
			return result;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 数组形式的整数加法测试 ===");
			
			// 测试用例1: 正常情况
			int[] num1 = {1, 2, 0, 0};  // 1200
			int k1 = 34;  // 34
			System.out.println("数组 (1200): " + java.util.Arrays.toString(num1) + ", k = " + k1);
			
			java.util.List<Integer> result1 = addToArrayForm(num1, k1);  // 1234
			System.out.println("结果 (1234): " + result1);
			
			// 测试用例2: 包含进位
			int[] num2 = {2, 7, 4};  // 274
			int k2 = 181;  // 181
			System.out.println("数组 (274): " + java.util.Arrays.toString(num2) + ", k = " + k2);
			
			java.util.List<Integer> result2 = addToArrayForm(num2, k2);  // 455
			System.out.println("结果 (455): " + result2);
			
			// 测试用例3: k比数组表示的数大
			int[] num3 = {2, 1, 5};  // 215
			int k3 = 806;  // 806
			System.out.println("数组 (215): " + java.util.Arrays.toString(num3) + ", k = " + k3);
			
			java.util.List<Integer> result3 = addToArrayForm(num3, k3);  // 1021
			System.out.println("结果 (1021): " + result3);
			System.out.println();
		}
	}
	
	/**
	 * 题目6: LeetCode 415. 字符串相加 (Add Strings)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-strings/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给定两个字符串形式的非负整数 num1 和 num2 ，计算它们的和并同样以字符串形式返回。
	 * 你不能使用任何內建的用于处理大整数的库（比如 BigInteger）， 也不能直接将输入的字符串转换为整数形式
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(m,n)), 空间复杂度: O(max(m,n))
	 * 
	 * 解题思路：
	 * 模拟手工加法过程，从字符串末尾开始相加，处理进位情况
	 */
	static class AddStringsSolution {
		
		/**
		 * 解法: 模拟加法
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个字符串的长度
		 * 空间复杂度: O(max(m,n)) - 结果字符串的空间
		 * 
		 * 核心思想：
		 * 1. 从字符串末尾开始相加
		 * 2. 处理进位
		 * 3. 将结果存储在StringBuilder中，最后反转
		 */
		public static String addStrings(String num1, String num2) {
			StringBuilder result = new StringBuilder();
			int carry = 0;  // 进位值
			int i = num1.length() - 1;  // num1的索引
			int j = num2.length() - 1;  // num2的索引
			
			// 从字符串末尾开始相加
			while (i >= 0 || j >= 0 || carry != 0) {
				// 获取当前位的值
				int x = (i >= 0) ? num1.charAt(i) - '0' : 0;  // num1当前位的值
				int y = (j >= 0) ? num2.charAt(j) - '0' : 0;  // num2当前位的值
				
				// 计算当前位的和
				int sum = x + y + carry;
				
				// 更新进位值
				carry = sum / 10;
				
				// 将当前位的结果添加到结果字符串的末尾
				result.append(sum % 10);
				
				// 移动索引
				i--;
				j--;
			}
			
			// 反转结果字符串并返回
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 字符串相加测试 ===");
			
			// 测试用例1: 正常情况
			String num1 = "11";  // 11
			String num2 = "123";  // 123
			System.out.println("字符串1 (11): " + num1 + ", 字符串2 (123): " + num2);
			
			String result1 = addStrings(num1, num2);  // 134
			System.out.println("结果 (134): " + result1);
			
			// 测试用例2: 包含进位
			String num3 = "456";  // 456
			String num4 = "77";  // 77
			System.out.println("字符串1 (456): " + num3 + ", 字符串2 (77): " + num4);
			
			String result2 = addStrings(num3, num4);  // 533
			System.out.println("结果 (533): " + result2);
			
			// 测试用例3: 不同长度
			String num5 = "0";  // 0
			String num6 = "0";  // 0
			System.out.println("字符串1 (0): " + num5 + ", 字符串2 (0): " + num6);
			
			String result3 = addStrings(num5, num6);  // 0
			System.out.println("结果 (0): " + result3);
			System.out.println();
		}
	}
	
	/**
	 * 题目7: LeetCode 67. 二进制求和 (Add Binary)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-binary/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给你两个二进制字符串 a 和 b ，以二进制字符串的形式返回它们的和
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(m,n)), 空间复杂度: O(max(m,n))
	 * 
	 * 解题思路：
	 * 模拟二进制加法过程，从字符串末尾开始相加，处理进位情况（逢二进一）
	 */
	static class AddBinarySolution {
		
		/**
		 * 解法: 模拟加法
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个字符串的长度
		 * 空间复杂度: O(max(m,n)) - 结果字符串的空间
		 * 
		 * 核心思想：
		 * 1. 从字符串末尾开始相加
		 * 2. 处理进位（逢二进一）
		 * 3. 将结果存储在StringBuilder中，最后反转
		 */
		public static String addBinary(String a, String b) {
			StringBuilder result = new StringBuilder();
			int carry = 0;  // 进位值
			int i = a.length() - 1;  // a的索引
			int j = b.length() - 1;  // b的索引
			
			// 从字符串末尾开始相加
			while (i >= 0 || j >= 0 || carry != 0) {
				// 获取当前位的值
				int x = (i >= 0) ? a.charAt(i) - '0' : 0;  // a当前位的值
				int y = (j >= 0) ? b.charAt(j) - '0' : 0;  // b当前位的值
				
				// 计算当前位的和
				int sum = x + y + carry;
				
				// 更新进位值（二进制逢二进一）
				carry = sum / 2;
				
				// 将当前位的结果添加到结果字符串的末尾
				result.append(sum % 2);
				
				// 移动索引
				i--;
				j--;
			}
			
			// 反转结果字符串并返回
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 二进制求和测试 ===");
			
			// 测试用例1: 正常情况
			String a1 = "11";  // 3
			String b1 = "1";   // 1
			System.out.println("二进制1 (11 = 3): " + a1 + ", 二进制2 (1 = 1): " + b1);
			
			String result1 = addBinary(a1, b1);  // 100 = 4
			System.out.println("结果 (100 = 4): " + result1);
			
			// 测试用例2: 包含进位
			String a2 = "1010";  // 10
			String b2 = "1011";  // 11
			System.out.println("二进制1 (1010 = 10): " + a2 + ", 二进制2 (1011 = 11): " + b2);
			
			String result2 = addBinary(a2, b2);  // 10101 = 21
			System.out.println("结果 (10101 = 21): " + result2);
			
			// 测试用例3: 不同长度
			String a3 = "0";  // 0
			String b3 = "0";  // 0
			System.out.println("二进制1 (0 = 0): " + a3 + ", 二进制2 (0 = 0): " + b3);
			
			String result3 = addBinary(a3, b3);  // 0
			System.out.println("结果 (0 = 0): " + result3);
			System.out.println();
		}
	}

	/**
	 * 题目8: 牛客网 BM86 大数加法
	 * 来源: 牛客网
	 * 链接: https://www.nowcoder.com/practice/11ae12e8c6fe48f883cad618c2e81475
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 以字符串的形式读入两个数字，编写一个函数计算它们的和，以字符串形式返回。
	 * 注意：字符串仅由字符'0'-'9'和'-'构成
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(m,n)), 空间复杂度: O(max(m,n))
	 * 
	 * 解题思路：
	 * 1. 处理符号（正数和负数）
	 * 2. 根据符号决定是相加还是相减
	 * 3. 模拟手工计算过程
	 */
	static class BigNumberAdditionSolution {
		
		/**
		 * 解法: 模拟大数加减法
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个字符串的长度
		 * 空间复杂度: O(max(m,n)) - 结果字符串的空间
		 * 
		 * 核心思想：
		 * 1. 处理符号
		 * 2. 根据符号决定计算方式（相加或相减）
		 * 3. 处理绝对值的相加或相减
		 */
		public static String solve(String s, String t) {
			// 处理符号
			boolean signS = true; // true表示正数
			boolean signT = true;
			if (s.charAt(0) == '-') {
				signS = false;
				s = s.substring(1);
			}
			if (t.charAt(0) == '-') {
				signT = false;
				t = t.substring(1);
			}
			
			// 根据符号决定计算方式
			if (signS == signT) {
				// 符号相同，先计算绝对值的和，再加上符号
				String sum = addAbsoluteValues(s, t);
				return signS ? sum : "-" + sum;
			} else {
				// 符号不同，计算绝对值的差，符号由绝对值大的数决定
				int compare = compareAbsoluteValues(s, t);
				String diff;
				boolean resultSign;
				if (compare > 0) {
					diff = subtractAbsoluteValues(s, t);
					resultSign = signS;
				} else if (compare < 0) {
					diff = subtractAbsoluteValues(t, s);
					resultSign = signT;
				} else {
					return "0"; // 两个数绝对值相等，结果为0
				}
				return resultSign ? diff : "-" + diff;
			}
		}
		
		// 计算两个正数字符串的和
		private static String addAbsoluteValues(String a, String b) {
			StringBuilder res = new StringBuilder();
			int i = a.length() - 1;
			int j = b.length() - 1;
			int carry = 0;
			while (i >= 0 || j >= 0 || carry > 0) {
				int digitA = i >= 0 ? a.charAt(i--) - '0' : 0;
				int digitB = j >= 0 ? b.charAt(j--) - '0' : 0;
				int sum = digitA + digitB + carry;
				carry = sum / 10;
				res.append(sum % 10);
			}
			return res.reverse().toString();
		}
		
		// 比较两个正数字符串的绝对值大小
		private static int compareAbsoluteValues(String a, String b) {
			if (a.length() != b.length()) {
				return a.length() > b.length() ? 1 : -1;
			}
			return a.compareTo(b);
		}
		
		// 计算两个正数字符串的差（假设a >= b）
		private static String subtractAbsoluteValues(String a, String b) {
			StringBuilder res = new StringBuilder();
			int i = a.length() - 1;
			int j = b.length() - 1;
			int borrow = 0;
			while (i >= 0) {
				int digitA = a.charAt(i--) - '0';
				int digitB = j >= 0 ? b.charAt(j--) - '0' : 0;
				int diff = digitA - digitB - borrow;
				if (diff < 0) {
					diff += 10;
					borrow = 1;
				} else {
					borrow = 0;
				}
				res.append(diff);
			}
			// 移除前导零
			while (res.length() > 1 && res.charAt(res.length() - 1) == '0') {
				res.deleteCharAt(res.length() - 1);
			}
			return res.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 牛客网 BM86 大数加法测试 ===");
			
			// 测试用例1: 正数相加
			String s1 = "123";
			String t1 = "456";
			System.out.println("数字1 (123): " + s1 + ", 数字2 (456): " + t1);
			String result1 = solve(s1, t1);  // 579
			System.out.println("结果 (579): " + result1);
			
			// 测试用例2: 包含进位
			String s2 = "999";
			String t2 = "1";
			System.out.println("数字1 (999): " + s2 + ", 数字2 (1): " + t2);
			String result2 = solve(s2, t2);  // 1000
			System.out.println("结果 (1000): " + result2);
			
			// 测试用例3: 负数相加
			String s3 = "-123";
			String t3 = "-456";
			System.out.println("数字1 (-123): " + s3 + ", 数字2 (-456): " + t3);
			String result3 = solve(s3, t3);  // -579
			System.out.println("结果 (-579): " + result3);
			
			// 测试用例4: 正负数相减
			String s4 = "123";
			String t4 = "-456";
			System.out.println("数字1 (123): " + s4 + ", 数字2 (-456): " + t4);
			String result4 = solve(s4, t4);  // 579
			System.out.println("结果 (579): " + result4);
			System.out.println();
		}
	}

	/**
	 * 题目9: 牛客网 NC40 链表相加（二）
	 * 来源: 牛客网
	 * 链接: https://www.nowcoder.com/practice/c56f6c70fb3f4849bc56e33ff2a50b6b
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 假设链表中每一个节点的值都在 0-9 之间，那么链表整体就可以代表一个整数。
	 * 例如：链表 1->2->3 代表整数 123。
	 * 给定两个这种链表，请生成代表它们之和的结果链表。
	 * 
	 * 解法分析：
	 * 1. 使用栈 - 时间复杂度: O(max(m,n)), 空间复杂度: O(max(m,n))
	 * 
	 * 解题思路：
	 * 使用栈来存储链表节点的值，这样可以从低位开始相加
	 */
	static class AddTwoNumbersIISolutionNC {
		
		/**
		 * 解法: 使用栈
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个链表的长度
		 * 空间复杂度: O(max(m,n)) - 栈的空间
		 * 
		 * 核心思想：
		 * 1. 使用栈来存储链表节点的值
		 * 2. 从栈顶开始相加，处理进位
		 * 3. 构建新的链表
		 */
		public static ListNode addInList(ListNode head1, ListNode head2) {
			// 方法：使用栈来存储链表节点的值，这样可以从低位开始相加
			java.util.Stack<Integer> stack1 = new java.util.Stack<>();
			java.util.Stack<Integer> stack2 = new java.util.Stack<>();
			
			// 将两个链表的值分别入栈
			while (head1 != null) {
				stack1.push(head1.val);
				head1 = head1.next;
			}
			while (head2 != null) {
				stack2.push(head2.val);
				head2 = head2.next;
			}
			
			int carry = 0;
			ListNode dummy = null; // 用于构建新链表
			
			// 从低位开始相加
			while (!stack1.isEmpty() || !stack2.isEmpty() || carry > 0) {
				int val1 = stack1.isEmpty() ? 0 : stack1.pop();
				int val2 = stack2.isEmpty() ? 0 : stack2.pop();
				int sum = val1 + val2 + carry;
				carry = sum / 10;
				int digit = sum % 10;
				
				// 创建新节点并插入到链表头部
				ListNode newNode = new ListNode(digit);
				newNode.next = dummy;
				dummy = newNode;
			}
			
			return dummy;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 牛客网 NC40 链表相加（二）测试 ===");
			
			// 测试用例1: 正常情况
			int[] arr1 = {1, 2, 3};
			int[] arr2 = {4, 5, 6};
			ListNode head1 = ListNode.createList(arr1);
			ListNode head2 = ListNode.createList(arr2);
			System.out.print("链表1 (123): ");
			ListNode.printList(head1);
			System.out.println();
			System.out.print("链表2 (456): ");
			ListNode.printList(head2);
			System.out.println();
			
			ListNode result1 = addInList(head1, head2); // 579
			System.out.print("结果 (579): ");
			ListNode.printList(result1);
			System.out.println();
			
			// 测试用例2: 包含进位
			int[] arr3 = {9, 9, 9};
			int[] arr4 = {1};
			ListNode head3 = ListNode.createList(arr3);
			ListNode head4 = ListNode.createList(arr4);
			System.out.print("链表1 (999): ");
			ListNode.printList(head3);
			System.out.println();
			System.out.print("链表2 (1): ");
			ListNode.printList(head4);
			System.out.println();
			
			ListNode result2 = addInList(head3, head4); // 1000
			System.out.print("结果 (1000): ");
			ListNode.printList(result2);
			System.out.println();
			System.out.println();
		}
	}

	/**
	 * 题目10: LintCode 165. 合并两个排序链表
	 * 来源: LintCode
	 * 链接: https://www.lintcode.com/problem/165/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 将两个升序链表合并为一个新的升序链表并返回。新链表是通过拼接给定的两个链表的所有节点组成的。
	 * 
	 * 解法分析：
	 * 1. 迭代 - 时间复杂度: O(m+n), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 比较两个链表的当前节点值，选择较小的节点添加到结果链表中
	 */
	static class MergeTwoSortedListsSolution {
		
		/**
		 * 解法: 迭代
		 * 时间复杂度: O(m+n) - m和n分别是两个链表的长度
		 * 空间复杂度: O(1) - 只使用常数额外空间
		 * 
		 * 核心思想：
		 * 1. 使用哑节点简化操作
		 * 2. 比较两个链表的当前节点值
		 * 3. 选择较小的节点添加到结果链表中
		 */
		public static ListNode mergeTwoLists(ListNode l1, ListNode l2) {
			// 使用哑节点简化操作
			ListNode dummy = new ListNode(0);
			ListNode tail = dummy;
			
			// 同时遍历两个链表，比较节点值的大小
			while (l1 != null && l2 != null) {
				if (l1.val <= l2.val) {
					tail.next = l1;
					l1 = l1.next;
				} else {
					tail.next = l2;
					l2 = l2.next;
				}
				tail = tail.next;
			}
			
			// 连接剩余节点
			tail.next = l1 != null ? l1 : l2;
			
			return dummy.next;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LintCode 165. 合并两个排序链表测试 ===");
			
			// 测试用例1: 正常情况
			int[] arr1 = {1, 2, 4};
			int[] arr2 = {1, 3, 4};
			ListNode l1 = ListNode.createList(arr1);
			ListNode l2 = ListNode.createList(arr2);
			System.out.print("链表1 (1->2->4): ");
			ListNode.printList(l1);
			System.out.println();
			System.out.print("链表2 (1->3->4): ");
			ListNode.printList(l2);
			System.out.println();
			
			ListNode result1 = mergeTwoLists(l1, l2); // 1->1->2->3->4->4
			System.out.print("结果 (1->1->2->3->4->4): ");
			ListNode.printList(result1);
			System.out.println();
			
			// 测试用例2: 空链表
			ListNode l3 = null;
			int[] arr4 = {0};
			ListNode l4 = ListNode.createList(arr4);
			System.out.print("链表1 (null): ");
			ListNode.printList(l3);
			System.out.println();
			System.out.print("链表2 (0): ");
			ListNode.printList(l4);
			System.out.println();
			
			ListNode result2 = mergeTwoLists(l3, l4); // 0
			System.out.print("结果 (0): ");
			ListNode.printList(result2);
			System.out.println();
			System.out.println();
		}
	}



	/**
	 * 题目11: 剑指Offer 06. 从尾到头打印链表
	 * 来源: 剑指Offer
	 * 链接: https://leetcode.cn/problems/cong-wei-dao-tou-da-yin-lian-biao-lcof/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 输入一个链表的头节点，从尾到头反过来返回每个节点的值（用数组返回）。
	 * 
	 * 解法分析：
	 * 1. 使用栈 - 时间复杂度: O(n), 空间复杂度: O(n)
	 * 2. 递归 - 时间复杂度: O(n), 空间复杂度: O(n)
	 * 3. 反转链表 - 时间复杂度: O(n), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 使用栈来存储链表节点的值，然后依次弹出得到逆序结果
	 */
	static class ReversePrintSolution {
		
		/**
		 * 解法: 使用栈
		 * 时间复杂度: O(n) - n是链表的长度
		 * 空间复杂度: O(n) - 栈的空间
		 * 
		 * 核心思想：
		 * 1. 遍历链表，将节点值入栈
		 * 2. 从栈中弹出元素，得到逆序结果
		 */
		public static int[] reversePrint(ListNode head) {
			if (head == null) {
				return new int[0];
			}
			
			java.util.Stack<Integer> stack = new java.util.Stack<>();
			ListNode curr = head;
			
			// 将链表节点的值入栈
			while (curr != null) {
				stack.push(curr.val);
				curr = curr.next;
			}
			
			// 从栈中弹出元素，得到逆序结果
			int[] result = new int[stack.size()];
			for (int i = 0; i < result.length; i++) {
				result[i] = stack.pop();
			}
			
			return result;
		}
		
		/**
		 * 解法: 递归
		 * 时间复杂度: O(n) - n是链表的长度
		 * 空间复杂度: O(n) - 递归调用栈的深度
		 * 
		 * 核心思想：
		 * 递归到链表末尾，然后回溯时记录节点值
		 */
		public static int[] reversePrintRecursive(ListNode head) {
			java.util.ArrayList<Integer> list = new java.util.ArrayList<>();
			recursiveHelper(head, list);
			int[] result = new int[list.size()];
			for (int i = 0; i < list.size(); i++) {
				result[i] = list.get(i);
			}
			return result;
		}
		
		private static void recursiveHelper(ListNode head, java.util.ArrayList<Integer> list) {
			if (head == null) {
				return;
			}
			recursiveHelper(head.next, list);
			list.add(head.val);
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 剑指Offer 06. 从尾到头打印链表测试 ===");
			
			// 测试用例1: 正常情况
			int[] arr1 = {1, 3, 2};
			ListNode head1 = ListNode.createList(arr1);
			System.out.print("链表 (1->3->2): ");
			ListNode.printList(head1);
			System.out.println();
			
			int[] result1 = reversePrint(head1); // [2, 3, 1]
			System.out.print("结果 (2, 3, 1): [");
			for (int i = 0; i < result1.length; i++) {
				System.out.print(result1[i]);
				if (i < result1.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("]");
			
			// 测试用例2: 空链表
			ListNode head2 = null;
			System.out.print("链表 (null): ");
			ListNode.printList(head2);
			System.out.println();
			
			int[] result2 = reversePrint(head2); // []
			System.out.print("结果 ([]): [");
			for (int i = 0; i < result2.length; i++) {
				System.out.print(result2[i]);
				if (i < result2.length - 1) {
					System.out.print(", ");
				}
			}
			System.out.println("]");
			System.out.println();
		}
	}

	/**
	 * 题目12: HackerRank BigInteger Addition
	 * 来源: HackerRank
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 实现一个大数加法函数，输入两个非常大的数字（可能超过标准数据类型的范围），返回它们的和。
	 * 
	 * 解法分析：
	 * 1. 模拟加法 - 时间复杂度: O(max(m,n)), 空间复杂度: O(max(m,n))
	 * 
	 * 解题思路：
	 * 从低位开始逐位相加，处理进位
	 */
	static class HackerRankBigIntegerAddition {
		
		/**
		 * 解法: 模拟加法
		 * 时间复杂度: O(max(m,n)) - m和n分别是两个字符串的长度
		 * 空间复杂度: O(max(m,n)) - 结果字符串的空间
		 * 
		 * 核心思想：
		 * 1. 从字符串末尾开始相加
		 * 2. 处理进位
		 * 3. 将结果存储在StringBuilder中，最后反转
		 */
		public static String addBigIntegers(String a, String b) {
			StringBuilder result = new StringBuilder();
			int i = a.length() - 1;
			int j = b.length() - 1;
			int carry = 0;
			
			// 从低位开始逐位相加
			while (i >= 0 || j >= 0 || carry > 0) {
				int digitA = i >= 0 ? a.charAt(i--) - '0' : 0;
				int digitB = j >= 0 ? b.charAt(j--) - '0' : 0;
				int sum = digitA + digitB + carry;
				carry = sum / 10;
				result.append(sum % 10);
			}
			
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== HackerRank BigInteger Addition 测试 ===");
			
			// 测试用例1: 大数相加
			String a1 = "12345678901234567890";
			String b1 = "98765432109876543210";
			System.out.println("大数1: " + a1);
			System.out.println("大数2: " + b1);
			String result1 = addBigIntegers(a1, b1); // 111111111011111111100
			System.out.println("结果: " + result1);
			
			// 测试用例2: 超长数字
			String a2 = "999999999999999999999999999999999999999";
			String b2 = "1";
			System.out.println("大数1: " + a2);
			System.out.println("大数2: " + b2);
			String result2 = addBigIntegers(a2, b2); // 1000000000000000000000000000000000000000
			System.out.println("结果: " + result2);
			System.out.println();
		}
	}

	/**
	 * 题目13: LeetCode 43. 字符串相乘 (Multiply Strings)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/multiply-strings/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 给定两个以字符串形式表示的非负整数 num1 和 num2，返回 num1 和 num2 的乘积，它们的乘积也表示为字符串形式。
	 * 注意：不能使用任何内置的 BigInteger 库或直接将输入转换为整数。
	 * 
	 * 解法分析：
	 * 1. 模拟乘法 - 时间复杂度: O(m*n), 空间复杂度: O(m+n)
	 * 
	 * 解题思路：
	 * 1. 模拟竖式乘法的过程
	 * 2. num1[i] * num2[j] 的结果位于 result[i+j] 和 result[i+j+1]
	 * 3. 处理进位
	 * 4. 去除前导零
	 * 
	 * 与链表相加的联系：
	 * 都是模拟数学运算过程，处理进位是核心思想
	 */
	static class MultiplyStringsSolution {
		
		/**
		 * 解法: 模拟乘法
		 * 时间复杂度: O(m*n) - m和n分别是两个字符串的长度
		 * 空间复杂度: O(m+n) - 结果数组的空间
		 * 
		 * 核心思想：
		 * 1. num1[i] * num2[j] 的结果位于 result[i+j] 和 result[i+j+1]
		 * 2. 从右往左逐位相乘，累加到对应位置
		 * 3. 处理进位
		 * 4. 去除前导零
		 * 
		 * 算法详解：
		 * - 两个数相乘，结果的最大长度为 m + n
		 * - 使用数组存储每一位的结果
		 * - num1[i] * num2[j] 会影响 result[i+j] 和 result[i+j+1] 两个位置
		 */
		public static String multiply(String num1, String num2) {
			// 边界情况：任意一个为 0，结果为 0
			if (num1.equals("0") || num2.equals("0")) {
				return "0";
			}
			
			int m = num1.length();
			int n = num2.length();
			// 结果的最大长度为 m + n
			int[] result = new int[m + n];
			
			// 从右往左遍历 num1 和 num2
			for (int i = m - 1; i >= 0; i--) {
				int digit1 = num1.charAt(i) - '0';
				for (int j = n - 1; j >= 0; j--) {
					int digit2 = num2.charAt(j) - '0';
					// 乘积
					int product = digit1 * digit2;
					// 加上原有的值
					int sum = product + result[i + j + 1];
					// 更新当前位
					result[i + j + 1] = sum % 10;
					// 更新进位
					result[i + j] += sum / 10;
				}
			}
			
			// 构建结果字符串，跳过前导零
			StringBuilder sb = new StringBuilder();
			boolean leadingZero = true;
			for (int digit : result) {
				if (digit != 0) {
					leadingZero = false;
				}
				if (!leadingZero) {
					sb.append(digit);
				}
			}
			
			return sb.toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LeetCode 43. 字符串相乘测试 ===");
			
			// 测试用例1: 正常情况
			String num1 = "2";
			String num2 = "3";
			System.out.println("字符串1: " + num1 + ", 字符串2: " + num2);
			String result1 = multiply(num1, num2); // 6
			System.out.println("结果: " + result1);
			
			// 测试用例2: 多位数相乘
			String num3 = "123";
			String num4 = "456";
			System.out.println("字符串1: " + num3 + ", 字符串2: " + num4);
			String result2 = multiply(num3, num4); // 56088
			System.out.println("结果: " + result2);
			
			// 测试用例3: 包含0的情况
			String num5 = "0";
			String num6 = "123";
			System.out.println("字符串1: " + num5 + ", 字符串2: " + num6);
			String result3 = multiply(num5, num6); // 0
			System.out.println("结果: " + result3);
			System.out.println();
		}
	}

	/**
	 * 题目14: LeetCode 371. 两整数之和 (Sum of Two Integers)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/sum-of-two-integers/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 给你两个整数 a 和 b ，不使用 运算符 + 和 - ​​​​​​​，计算并返回两整数之和。
	 * 
	 * 解法分析：
	 * 1. 位运算 - 时间复杂度: O(1), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 1. 使用异或运算得到不含进位的和
	 * 2. 使用与运算和左移得到进位
	 * 3. 重复上述过程直到进位为0
	 * 
	 * 与链表相加的联系：
	 * 都需要处理进位问题，但这里用位运算实现
	 */
	static class SumOfTwoIntegersSolution {
		
		/**
		 * 解法: 位运算
		 * 时间复杂度: O(1) - 最多迭代32次（整数位数）
		 * 空间复杂度: O(1) - 只使用常数额外空间
		 * 
		 * 核心思想：
		 * 1. a ^ b 得到不含进位的和
		 * 2. (a & b) << 1 得到进位
		 * 3. 重复以上过程直到进位为0
		 * 
		 * 算法详解：
		 * - 异或（XOR）相当于无进位的加法
		 * - 与（AND）+ 左移相当于计算进位
		 * - 例如：5 + 3 = 0101 + 0011
		 *   - 0101 ^ 0011 = 0110 (不含进位的和)
		 *   - (0101 & 0011) << 1 = 0010 (进位)
		 *   - 0110 + 0010 = 1000 = 8
		 */
		public static int getSum(int a, int b) {
			while (b != 0) {
				// 计算不含进位的和
				int sum = a ^ b;
				// 计算进位
				int carry = (a & b) << 1;
				// 更新a和b
				a = sum;
				b = carry;
			}
			return a;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LeetCode 371. 两整数之和测试 ===");
			
			// 测试用例1: 正数相加
			int a1 = 1, b1 = 2;
			System.out.println(a1 + " + " + b1 + " = " + getSum(a1, b1)); // 3
			
			// 测试用例2: 包含进位
			int a2 = 5, b2 = 3;
			System.out.println(a2 + " + " + b2 + " = " + getSum(a2, b2)); // 8
			
			// 测试用例3: 负数相加
			int a3 = -2, b3 = 3;
			System.out.println(a3 + " + " + b3 + " = " + getSum(a3, b3)); // 1
			System.out.println();
		}
	}

	/**
	 * 题目15: LeetCode 258. 各位相加 (Add Digits)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/add-digits/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给定一个非负整数 num，反复将各个位上的数字相加，直到结果为一位数。返回这个结果。
	 * 
	 * 解法分析：
	 * 1. 模拟法 - 时间复杂度: O(log n), 空间复杂度: O(1)
	 * 2. 数学规律（数根） - 时间复杂度: O(1), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 1. 模拟法：不断计算各位数字之和，直到结果为一位数
	 * 2. 数学规律：结果 = (num - 1) % 9 + 1（num > 0 时）
	 * 
	 * 数学原理：
	 * 数根（digital root）的性质：一个数对9取余的结果等于它各位数字之和对9取余的结果
	 */
	static class AddDigitsSolution {
		
		/**
		 * 解法1: 模拟法
		 * 时间复杂度: O(log n) - n是输入数字
		 * 空间复杂度: O(1)
		 */
		public static int addDigits(int num) {
			while (num >= 10) {
				int sum = 0;
				while (num > 0) {
					sum += num % 10;
					num /= 10;
				}
				num = sum;
			}
			return num;
		}
		
		/**
		 * 解法2: 数学规律（数根）- 最优解
		 * 时间复杂度: O(1)
		 * 空间复杂度: O(1)
		 * 
		 * 核心思想：
		 * 数根公式：dr(n) = 1 + ((n - 1) % 9)
		 * 特殊情况：n = 0 时，结果为 0
		 */
		public static int addDigitsOptimal(int num) {
			return num == 0 ? 0 : 1 + (num - 1) % 9;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LeetCode 258. 各位相加测试 ===");
			
			// 测试用例1
			int num1 = 38; // 3 + 8 = 11, 1 + 1 = 2
			System.out.println("模拟法: " + num1 + " -> " + addDigits(num1));
			System.out.println("数学法: " + num1 + " -> " + addDigitsOptimal(num1));
			
			// 测试用例2
			int num2 = 0;
			System.out.println("模拟法: " + num2 + " -> " + addDigits(num2));
			System.out.println("数学法: " + num2 + " -> " + addDigitsOptimal(num2));
			System.out.println();
		}
	}

	/**
	 * 题目16: LeetCode 306. 累加数 (Additive Number)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/additive-number/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 累加数 是一个字符串，组成它的数字可以形成累加序列。
	 * 一个有效的 累加序列 应当 至少 包含 3 个数。除了最开始的两个数以外，
	 * 序列中的每个后续数都必须是它 之前 两个数的和。
	 * 
	 * 解法分析：
	 * 1. 回溯 + 字符串加法 - 时间复杂度: O(n^3), 空间复杂度: O(n)
	 * 
	 * 解题思路：
	 * 1. 枚举前两个数的长度
	 * 2. 使用回溯检查是否能形成累加序列
	 * 3. 使用字符串加法处理大数
	 */
	static class AdditiveNumberSolution {
		
		/**
		 * 解法: 回溯 + 字符串加法
		 * 时间复杂度: O(n^3) - n是字符串长度
		 * 空间复杂度: O(n) - 递归调用栈的深度
		 */
		public static boolean isAdditiveNumber(String num) {
			int n = num.length();
			// 枚举第一个数的长度
			for (int i = 1; i <= n / 2; i++) {
				// 跳过以0开头且长度大于1的数
				if (num.charAt(0) == '0' && i > 1) break;
				// 枚举第二个数的长度
				for (int j = i + 1; n - j >= Math.max(i, j - i); j++) {
					// 跳过以0开头且长度大于1的数
					if (num.charAt(i) == '0' && j - i > 1) break;
					if (isValid(num.substring(0, i), num.substring(i, j), j, num)) {
						return true;
					}
				}
			}
			return false;
		}
		
		private static boolean isValid(String num1, String num2, int start, String num) {
			if (start == num.length()) return true;
			String sum = addStrings(num1, num2);
			if (!num.startsWith(sum, start)) return false;
			return isValid(num2, sum, start + sum.length(), num);
		}
		
		private static String addStrings(String num1, String num2) {
			StringBuilder result = new StringBuilder();
			int carry = 0;
			int i = num1.length() - 1;
			int j = num2.length() - 1;
			
			while (i >= 0 || j >= 0 || carry != 0) {
				int x = (i >= 0) ? num1.charAt(i) - '0' : 0;
				int y = (j >= 0) ? num2.charAt(j) - '0' : 0;
				int sum = x + y + carry;
				carry = sum / 10;
				result.append(sum % 10);
				i--;
				j--;
			}
			
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LeetCode 306. 累加数测试 ===");
			
			String num1 = "112358";
			System.out.println("字符串: " + num1 + " -> " + isAdditiveNumber(num1)); // true (1+1=2, 1+2=3, 2+3=5, 3+5=8)
			
			String num2 = "199100199";
			System.out.println("字符串: " + num2 + " -> " + isAdditiveNumber(num2)); // true (1+99=100, 99+100=199)
			System.out.println();
		}
	}

	/**
	 * 题目17: LeetCode 2816. 翻倍以链表形式表示的数字 (Double a Number Represented as a Linked List)
	 * 来源: LeetCode
	 * 链接: https://leetcode.cn/problems/double-a-number-represented-as-a-linked-list/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 给你一个 非空 链表的头节点 head ，表示一个不含 前导零 的非负数字。
	 * 返回链表 head ，表示将链表 表示的数字 翻倍 后的结果。
	 * 
	 * 解法分析：
	 * 1. 反转链表 - 时间复杂度: O(n), 空间复杂度: O(1)
	 * 2. 递归 - 时间复杂度: O(n), 空间复杂度: O(n)
	 */
	static class DoubleLinkedListNumberSolution {
		
		/**
		 * 解法1: 反转链表
		 * 时间复杂度: O(n)
		 * 空间复杂度: O(1)
		 */
		public static ListNode doubleIt(ListNode head) {
			// 反转链表
			head = reverse(head);
			
			// 翻倍并处理进位
			ListNode cur = head;
			int carry = 0;
			ListNode prev = null;
			
			while (cur != null) {
				int doubled = cur.val * 2 + carry;
				cur.val = doubled % 10;
				carry = doubled / 10;
				prev = cur;
				cur = cur.next;
			}
			
			// 处理最后的进位
			if (carry > 0) {
				prev.next = new ListNode(carry);
			}
			
			// 再次反转链表
			return reverse(head);
		}
		
		private static ListNode reverse(ListNode head) {
			ListNode prev = null;
			ListNode cur = head;
			while (cur != null) {
				ListNode next = cur.next;
				cur.next = prev;
				prev = cur;
				cur = next;
			}
			return prev;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== LeetCode 2816. 翻倍以链表形式表示的数字测试 ===");
			
			ListNode head1 = ListNode.createList(new int[]{1, 8, 9}); // 189
			System.out.print("链表 (189): ");
			ListNode.printList(head1);
			
			ListNode result1 = doubleIt(head1); // 378
			System.out.print("结果 (378): ");
			ListNode.printList(result1);
			System.out.println();
		}
	}

	/**
	 * 题目18: Codeforces 1077C - Good Array
	 * 来源: Codeforces
	 * 链接: https://codeforces.com/problemset/problem/1077/C
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给定一个数组，判断删除一个元素后，剩下的元素中是否存在一个元素等于其他所有元素的和。
	 * 如果存在，输出所有满足条件的元素索引。
	 * 
	 * 解法分析：
	 * 1. 前缀和 + 数学 - 时间复杂度: O(n), 空间复杂度: O(n)
	 * 
	 * 解题思路：
	 * 1. 计算数组的总和
	 * 2. 遍历每个元素，检查删除该元素后，剩余元素中是否存在一个元素等于其他元素的和
	 * 3. 使用数学公式：sum - arr[i] = 2 * max_element
	 * 
	 * 与链表相加的联系：
	 * 都涉及数值运算和条件判断，需要处理数组/链表中的数值关系
	 */
	static class GoodArraySolution {
		
		/**
		 * 解法: 前缀和 + 数学
		 * 时间复杂度: O(n) - n是数组长度
		 * 空间复杂度: O(n) - 存储结果的空间
		 * 
		 * 核心思想：
		 * 1. 计算数组总和
		 * 2. 找到数组中的最大值和次大值
		 * 3. 遍历每个元素，检查条件：sum - arr[i] == 2 * max_element
		 */
		public static java.util.List<Integer> goodArray(int[] arr) {
			java.util.List<Integer> result = new java.util.ArrayList<>();
			if (arr == null || arr.length == 0) return result;
			
			// 计算数组总和
			long sum = 0;
			for (int num : arr) {
				sum += num;
			}
			
			// 找到最大值和次大值
			int max1 = Integer.MIN_VALUE, max2 = Integer.MIN_VALUE;
			for (int num : arr) {
				if (num > max1) {
					max2 = max1;
					max1 = num;
				} else if (num > max2) {
					max2 = num;
				}
			}
			
			// 遍历检查每个元素
			for (int i = 0; i < arr.length; i++) {
				long remainingSum = sum - arr[i];
				int maxElement = (arr[i] == max1) ? max2 : max1;
				
				// 检查条件：剩余元素中是否存在一个元素等于其他元素的和
				if (remainingSum == 2L * maxElement) {
					result.add(i + 1); // 题目要求输出1-based索引
				}
			}
			
			return result;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== Codeforces 1077C - Good Array 测试 ===");
			
			// 测试用例1
			int[] arr1 = {2, 1, 3};
			System.out.println("数组: " + java.util.Arrays.toString(arr1));
			java.util.List<Integer> result1 = goodArray(arr1);
			System.out.println("结果索引: " + result1); // [1, 3]
			
			// 测试用例2
			int[] arr2 = {1, 1};
			System.out.println("数组: " + java.util.Arrays.toString(arr2));
			java.util.List<Integer> result2 = goodArray(arr2);
			System.out.println("结果索引: " + result2); // []
			
			// 测试用例3
			int[] arr3 = {1, 2, 3, 4, 5, 6};
			System.out.println("数组: " + java.util.Arrays.toString(arr3));
			java.util.List<Integer> result3 = goodArray(arr3);
			System.out.println("结果索引: " + result3); // [6]
			System.out.println();
		}
	}

	/**
	 * 题目19: AtCoder ABC176 D - Wizard in Maze
	 * 来源: AtCoder
	 * 链接: https://atcoder.jp/contests/abc176/tasks/abc176_d
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 在一个迷宫中，从起点到终点，可以移动或使用魔法传送。
	 * 移动：上下左右移动一格
	 * 魔法：传送到5×5范围内的任意位置
	 * 求最少使用魔法次数
	 * 
	 * 解法分析：
	 * 1. BFS + 双端队列 - 时间复杂度: O(H*W), 空间复杂度: O(H*W)
	 * 
	 * 解题思路：
	 * 1. 使用双端队列BFS，普通移动代价为0，魔法移动代价为1
	 * 2. 优先处理代价为0的移动
	 * 3. 使用Dijkstra思想，保证最小代价
	 * 
	 * 与链表相加的联系：
	 * 都涉及算法优化和状态转移，需要处理复杂的数据结构
	 */
	static class WizardInMazeSolution {
		
		/**
		 * 解法: BFS + 双端队列 (0-1 BFS)
		 * 时间复杂度: O(H*W) - H和W是迷宫的高度和宽度
		 * 空间复杂度: O(H*W) - 距离数组的空间
		 * 
		 * 核心思想：
		 * 1. 使用双端队列，普通移动代价为0，魔法移动代价为1
		 * 2. 优先处理代价为0的移动
		 * 3. 使用距离数组记录最小代价
		 */
		public static int solve(int H, int W, int startX, int startY, int endX, int endY, char[][] maze) {
			int[][] dist = new int[H][W];
			for (int i = 0; i < H; i++) {
				java.util.Arrays.fill(dist[i], Integer.MAX_VALUE);
			}
			
			// 双端队列存储位置和代价
			java.util.Deque<int[]> deque = new java.util.LinkedList<>();
			deque.addFirst(new int[]{startX, startY, 0});
			dist[startX][startY] = 0;
			
			// 移动方向：上下左右
			int[] dx = {-1, 1, 0, 0};
			int[] dy = {0, 0, -1, 1};
			
			while (!deque.isEmpty()) {
				int[] current = deque.pollFirst();
				int x = current[0], y = current[1], cost = current[2];
				
				// 如果到达终点，返回代价
				if (x == endX && y == endY) {
					return cost;
				}
				
				// 普通移动（代价为0）
				for (int i = 0; i < 4; i++) {
					int nx = x + dx[i];
					int ny = y + dy[i];
					
					if (nx >= 0 && nx < H && ny >= 0 && ny < W && maze[nx][ny] == '.') {
						if (cost < dist[nx][ny]) {
							dist[nx][ny] = cost;
							deque.addFirst(new int[]{nx, ny, cost});
						}
					}
				}
				
				// 魔法移动（代价为1）
				for (int i = -2; i <= 2; i++) {
					for (int j = -2; j <= 2; j++) {
						int nx = x + i;
						int ny = y + j;
						
						if (nx >= 0 && nx < H && ny >= 0 && ny < W && maze[nx][ny] == '.') {
							if (cost + 1 < dist[nx][ny]) {
								dist[nx][ny] = cost + 1;
								deque.addLast(new int[]{nx, ny, cost + 1});
							}
						}
					}
				}
			}
			
			return -1; // 无法到达终点
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== AtCoder ABC176 D - Wizard in Maze 测试 ===");
			
			// 简化测试用例
			int H = 4, W = 4;
			int startX = 1, startY = 1;
			int endX = 3, endY = 3;
			char[][] maze = {
				{'.', '.', '.', '.'},
				{'.', '#', '.', '.'},
				{'.', '.', '#', '.'},
				{'.', '.', '.', '.'}
			};
			
			int result = solve(H, W, startX, startY, endX, endY, maze);
			System.out.println("最少魔法次数: " + result);
			System.out.println();
		}
	}

	/**
	 * 题目20: USACO 2017 December Contest, Silver Problem 1. My Cow Ate My Homework
	 * 来源: USACO
	 * 链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=762
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 给定一个成绩数组，删除前k个成绩后，计算剩余成绩的平均值（去掉最低分）。
	 * 求所有k值中，使得平均分最大的k值。
	 * 
	 * 解法分析：
	 * 1. 前缀和 + 后缀最小值 - 时间复杂度: O(n), 空间复杂度: O(n)
	 * 
	 * 解题思路：
	 * 1. 计算后缀和和后缀最小值
	 * 2. 遍历每个k值，计算平均分
	 * 3. 找到最大平均分对应的k值
	 * 
	 * 与链表相加的联系：
	 * 都涉及数组处理和数值计算，需要优化算法性能
	 */
	static class MyCowAteMyHomeworkSolution {
		
		/**
		 * 解法: 前缀和 + 后缀最小值
		 * 时间复杂度: O(n) - n是数组长度
		 * 空间复杂度: O(n) - 后缀数组的空间
		 * 
		 * 核心思想：
		 * 1. 计算后缀和和后缀最小值
		 * 2. 平均分 = (后缀和 - 后缀最小值) / (剩余元素数 - 1)
		 */
		public static java.util.List<Integer> findBestK(int[] scores) {
			java.util.List<Integer> result = new java.util.ArrayList<>();
			if (scores == null || scores.length < 3) return result;
			
			int n = scores.length;
			long[] suffixSum = new long[n + 1]; // 后缀和
			int[] suffixMin = new int[n + 1];  // 后缀最小值
			
			// 初始化后缀数组
			suffixSum[n] = 0;
			suffixMin[n] = Integer.MAX_VALUE;
			
			// 计算后缀和和后缀最小值
			for (int i = n - 1; i >= 0; i--) {
				suffixSum[i] = suffixSum[i + 1] + scores[i];
				suffixMin[i] = Math.min(suffixMin[i + 1], scores[i]);
			}
			
			double maxAvg = 0;
			
			// 遍历k值（删除前k个成绩）
			for (int k = 1; k <= n - 2; k++) {
				long sum = suffixSum[k] - suffixMin[k];
				int count = n - k - 1;
				
				if (count > 0) {
					double avg = (double) sum / count;
					
					if (avg > maxAvg) {
						maxAvg = avg;
						result.clear();
						result.add(k);
					} else if (Math.abs(avg - maxAvg) < 1e-9) {
						result.add(k);
					}
				}
			}
			
			return result;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== USACO 2017 December Contest, Silver Problem 1 测试 ===");
			
			int[] scores1 = {3, 1, 9, 2, 7};
			System.out.println("成绩数组: " + java.util.Arrays.toString(scores1));
			java.util.List<Integer> result1 = findBestK(scores1);
			System.out.println("最优k值: " + result1);
			
			int[] scores2 = {1, 2, 3, 4, 5, 6};
			System.out.println("成绩数组: " + java.util.Arrays.toString(scores2));
			java.util.List<Integer> result2 = findBestK(scores2);
			System.out.println("最优k值: " + result2);
			System.out.println();
		}
	}

	/**
	 * 题目21: 洛谷 P1001 A+B Problem
	 * 来源: 洛谷
	 * 链接: https://www.luogu.com.cn/problem/P1001
	 * 难度: 入门
	 * 
	 * 题目描述：
	 * 输入两个整数a和b，输出它们的和。
	 * 
	 * 解法分析：
	 * 1. 基础加法 - 时间复杂度: O(1), 空间复杂度: O(1)
	 * 
	 * 解题思路：
	 * 最简单的加法运算，但需要考虑大数情况
	 */
	static class LuoguP1001Solution {
		
		/**
		 * 解法: 基础加法（支持大数）
		 * 时间复杂度: O(1)
		 * 空间复杂度: O(1)
		 */
		public static String add(String a, String b) {
			// 处理大数加法
			StringBuilder result = new StringBuilder();
			int i = a.length() - 1;
			int j = b.length() - 1;
			int carry = 0;
			
			while (i >= 0 || j >= 0 || carry > 0) {
				int digitA = i >= 0 ? a.charAt(i--) - '0' : 0;
				int digitB = j >= 0 ? b.charAt(j--) - '0' : 0;
				int sum = digitA + digitB + carry;
				carry = sum / 10;
				result.append(sum % 10);
			}
			
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== 洛谷 P1001 A+B Problem 测试 ===");
			
			String a1 = "1", b1 = "2";
			System.out.println(a1 + " + " + b1 + " = " + add(a1, b1));
			
			String a2 = "123456789", b2 = "987654321";
			System.out.println(a2 + " + " + b2 + " = " + add(a2, b2));
			System.out.println();
		}
	}

	/**
	 * 题目22: CodeChef FLOW001 - Add Two Numbers
	 * 来源: CodeChef
	 * 链接: https://www.codechef.com/problems/FLOW001
	 * 难度: Beginner
	 * 
	 * 题目描述：
	 * 输入两个整数，输出它们的和。
	 * 
	 * 解法分析：
	 * 1. 基础加法 - 时间复杂度: O(1), 空间复杂度: O(1)
	 */
	static class CodeChefFLOW001Solution {
		
		/**
		 * 解法: 基础加法
		 */
		public static int add(int a, int b) {
			return a + b;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== CodeChef FLOW001 - Add Two Numbers 测试 ===");
			
			System.out.println("1 + 2 = " + add(1, 2));
			System.out.println("100 + 200 = " + add(100, 200));
			System.out.println();
		}
	}

	/**
	 * 题目23: SPOJ ADDREV - Adding Reversed Numbers
	 * 来源: SPOJ
	 * 链接: http://www.spoj.com/problems/ADDREV/
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 输入两个数，将它们反转后相加，再将结果反转输出。
	 * 
	 * 解法分析：
	 * 1. 数字反转 + 加法 - 时间复杂度: O(log n), 空间复杂度: O(1)
	 */
	static class SPOJADDREVSolution {
		
		/**
		 * 解法: 数字反转 + 加法
		 */
		public static int addReversed(int a, int b) {
			int reversedA = reverseNumber(a);
			int reversedB = reverseNumber(b);
			int sum = reversedA + reversedB;
			return reverseNumber(sum);
		}
		
		private static int reverseNumber(int n) {
			int reversed = 0;
			while (n > 0) {
				reversed = reversed * 10 + n % 10;
				n /= 10;
			}
			return reversed;
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== SPOJ ADDREV - Adding Reversed Numbers 测试 ===");
			
			System.out.println("24 + 1 = " + addReversed(24, 1)); // 34 + 1 = 35 -> 53
			System.out.println("4358 + 754 = " + addReversed(4358, 754)); // 8534 + 457 = 8991 -> 1998
			System.out.println();
		}
	}

	/**
	 * 题目24: Project Euler Problem 13: Large sum
	 * 来源: Project Euler
	 * 链接: https://projecteuler.net/problem=13
	 * 难度: Easy
	 * 
	 * 题目描述：
	 * 计算100个50位数字的和的前10位数字。
	 * 
	 * 解法分析：
	 * 1. 大数加法 - 时间复杂度: O(n*m), 空间复杂度: O(m)
	 */
	static class ProjectEulerProblem13Solution {
		
		/**
		 * 解法: 大数加法
		 */
		public static String largeSum(String[] numbers) {
			String result = "0";
			for (String num : numbers) {
				result = addBigNumbers(result, num);
			}
			return result.substring(0, 10); // 返回前10位
		}
		
		private static String addBigNumbers(String a, String b) {
			StringBuilder result = new StringBuilder();
			int i = a.length() - 1;
			int j = b.length() - 1;
			int carry = 0;
			
			while (i >= 0 || j >= 0 || carry > 0) {
				int digitA = i >= 0 ? a.charAt(i--) - '0' : 0;
				int digitB = j >= 0 ? b.charAt(j--) - '0' : 0;
				int sum = digitA + digitB + carry;
				carry = sum / 10;
				result.append(sum % 10);
			}
			
			return result.reverse().toString();
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== Project Euler Problem 13: Large sum 测试 ===");
			
			String[] testNumbers = {
				"37107287533902102798797998220837590246510135740250",
				"46376937677490009712648124896970078050417018260538"
			};
			
			String result = largeSum(testNumbers);
			System.out.println("前10位和: " + result);
			System.out.println();
		}
	}

	/**
	 * 题目25: HackerEarth Monk and Number Queries
	 * 来源: HackerEarth
	 * 链接: https://www.hackerearth.com/practice/data-structures/advanced-data-structures/fenwick-binary-indexed-trees/practice-problems/algorithm/monk-and-number-queries/
	 * 难度: Medium
	 * 
	 * 题目描述：
	 * 支持三种操作：
	 * 1. 添加一个数字
	 * 2. 删除一个数字
	 * 3. 查询所有数字的某种组合
	 * 
	 * 解法分析：
	 * 1. 树状数组/线段树 - 时间复杂度: O(log n), 空间复杂度: O(n)
	 */
	static class MonkAndNumberQueriesSolution {
		
		/**
		 * 简化版解法: 使用列表模拟
		 */
		public static void processQueries(String[] queries) {
			java.util.List<Integer> numbers = new java.util.ArrayList<>();
			
			for (String query : queries) {
				String[] parts = query.split(" ");
				String operation = parts[0];
				
				if (operation.equals("1")) {
					// 添加数字
					int num = Integer.parseInt(parts[1]);
					numbers.add(num);
					System.out.println("添加数字: " + num);
				} else if (operation.equals("2")) {
					// 删除数字
					int num = Integer.parseInt(parts[1]);
					numbers.remove(Integer.valueOf(num));
					System.out.println("删除数字: " + num);
				} else if (operation.equals("3")) {
					// 查询操作（简化）
					if (!numbers.isEmpty()) {
						int sum = 0;
						for (int num : numbers) {
							sum += num;
						}
						System.out.println("当前数字和: " + sum);
					}
				}
			}
		}
		
		/**
		 * 测试方法
		 */
		public static void test() {
			System.out.println("=== HackerEarth Monk and Number Queries 测试 ===");
			
			String[] queries = {
				"1 5",
				"1 3", 
				"3",
				"2 3",
				"3"
			};
			
			processQueries(queries);
			System.out.println();
		}
	}

	/**
	 * 思路技巧题型总结
	 * 
	 * 一、核心思想：模拟数学运算过程
	 * 1. 进位处理：使用carry变量记录进位值
	 * 2. 逼位相加：从低位到高位或从高位到低位
	 * 3. 处理不同长度：短的数字用0补齐
	 * 4. 最后进位处理：需要单独处理最高位的进位
	 * 
	 * 二、常见数据结构选择
	 * 1. 链表：适合从低位开始的相加（如两数相加）
	 * 2. 栈：适合从高位开始的相加（如两数相加II）
	 * 3. 数组/字符串：适合处理大数运算
	 * 4. 位运算：适合不使用算数运算符的场景
	 * 
	 * 三、题型识别
	 * 1. 链表相加类：见到链表表示数字相加 -> 模拟加法 + 进位处理
	 * 2. 字符串相加类：见到大数运算 -> 字符串模拟 + 进位处理
	 * 3. 数组相加类：见到数组加法 -> 从后往前遍历 + 进位处理
	 * 4. 位运算类：见到不使用+-运算符 -> 位运算模拟
	 * 
	 * 四、性能优化技巧
	 * 1. 时间优化：
	 *    - 一次遍历完成所有操作
	 *    - 避免重复计算
	 * 2. 空间优化：
	 *    - 尽量原地修改，减少额外空间
	 *    - 反转链表代替使用栈
	 * 
	 * 五、边界情况处理
	 * 1. 空输入：检查null或空字符串
	 * 2. 极端值：
	 *    - 所有位都是9（需要进位）
	 *    - 单个元素
	 * 3. 重复数据：多个0
	 * 4. 数值范围：溢出处理（大数运算）
	 * 
	 * 六、工程化考量
	 * 1. 异常处理：
	 *    - 输入验证：检柧null、空字符串、非法字符
	 *    - 溢出处理：使用long或BigInteger
	 * 2. 可配置性：
	 *    - 支持不同进制（十进制、二进制等）
	 * 3. 单元测试：
	 *    - 正常情况测试
	 *    - 边界情况测试
	 *    - 异常情况测试
	 * 4. 性能优化：
	 *    - 大数据量测试
	 *    - 并发处理（如果适用）
	 * 
	 * 七、语言特性差异
	 * 1. Java:
	 *    - 使用StringBuilder拼接字符串
	 *    - Integer溢出需要使用long
	 * 2. C++:
	 *    - 注意内存管理（delete释放链表节点）
	 *    - 使用unsigned处理负数左移
	 * 3. Python:
	 *    - 整数无限精度，位运算需要特殊处理
	 *    - 列表操作非常灵活
	 * 
	 * 八、与机器学习/深度学习的联系
	 * 1. 序列处理：RNN/LSTM处理顺序数据，类似链表结构
	 * 2. 注意力机制：处理不同长度序列，类似处理不同长度的数字
	 * 3. 数值计算：深度学习中的梯度计算需要高精度
	 * 4. 大数运算：大语言模型的参数量巨大，需要高效数值计算
	 */
	public static void runAllTests() {
		AddTwoNumbersSolution.test();
		AddTwoNumbersIISolution.test();
		PlusOneLinkedListSolution.test();
		PlusOneSolution.test();
		AddToArrayFormSolution.test();
		AddStringsSolution.test();
		AddBinarySolution.test();
		BigNumberAdditionSolution.test();
		AddTwoNumbersIISolutionNC.test();
		MergeTwoSortedListsSolution.test();
		ReversePrintSolution.test();
		HackerRankBigIntegerAddition.test();
		MultiplyStringsSolution.test();
		SumOfTwoIntegersSolution.test();
		AddDigitsSolution.test();
		AdditiveNumberSolution.test();
		DoubleLinkedListNumberSolution.test();
		GoodArraySolution.test();
		WizardInMazeSolution.test();
		MyCowAteMyHomeworkSolution.test();
		LuoguP1001Solution.test();
		CodeChefFLOW001Solution.test();
		SPOJADDREVSolution.test();
		ProjectEulerProblem13Solution.test();
		MonkAndNumberQueriesSolution.test();
	}

	/**
	 * 主函数 - 运行所有测试
	 */
	public static void main(String[] args) {
		runAllTests();
		
		// 运行新增题目的测试
		System.out.println("=== 新增题目测试 ===");
		GoodArraySolution.test();
		WizardInMazeSolution.test();
		MyCowAteMyHomeworkSolution.test();
		LuoguP1001Solution.test();
		CodeChefFLOW001Solution.test();
		SPOJADDREVSolution.test();
		ProjectEulerProblem13Solution.test();
		MonkAndNumberQueriesSolution.test();
	}

}