package class034;

// 每k个节点一组翻转链表
// 测试链接：https://leetcode.cn/problems/reverse-nodes-in-k-group/
public class Code02_ReverseNodesInkGroup {

	// 链表节点定义
	// val: 节点值
	// next: 指向下一个节点的指针
	public static class ListNode {
		public int val;
		public ListNode next;
		
		// 构造函数
		public ListNode(int val) {
			this.val = val;
		}
	}

	// 每k个节点一组翻转链表
	// 方法：分组处理，每k个节点为一组进行翻转
	// 时间复杂度：O(n) - 每个节点最多被访问两次
	// 空间复杂度：O(1) - 只使用常数额外空间
	// 参数：
	//   head - 链表的头节点
	//   k - 每组翻转的节点数
	// 返回值：翻转后的链表头节点
	public static ListNode reverseKGroup(ListNode head, int k) {
		// 获取第一组的结束节点
		ListNode start = head;
		ListNode end = teamEnd(start, k);
		
		// 如果节点数不足k个，直接返回原链表
		if (end == null) {
			return head;
		}
		
		// 第一组很特殊因为牵扯到换头的问题
		head = end;
		// 翻转第一组
		reverse(start, end);
		
		// 翻转之后start变成了上一组的结尾节点
		ListNode lastTeamEnd = start;
		
		// 处理后续各组
		while (lastTeamEnd.next != null) {
			// 下一组的开始节点
			start = lastTeamEnd.next;
			// 获取当前组的结束节点
			end = teamEnd(start, k);
			
			// 如果节点数不足k个，直接返回结果
			if (end == null) {
				return head;
			}
			
			// 翻转当前组
			reverse(start, end);
			// 连接上一组的结尾和当前组的开始
			lastTeamEnd.next = end;
			// 更新上一组的结尾节点
			lastTeamEnd = start;
		}
		
		// 返回翻转后的链表头节点
		return head;
	}

	// 查找当前组的结束节点
	// 方法：从开始节点s开始，往下数k个节点
	// 时间复杂度：O(k) - 最多遍历k个节点
	// 空间复杂度：O(1) - 只使用常数额外空间
	// 参数：
	//   s - 当前组的开始节点
	//   k - 组大小
	// 返回值：当前组的结束节点，如果节点数不足k个则返回null
	public static ListNode teamEnd(ListNode s, int k) {
		// 循环k-1次，找到第k个节点
		while (--k != 0 && s != null) {
			s = s.next;
		}
		return s;
	}

	// 翻转指定范围内的链表节点
	// 方法：迭代法翻转链表
	// 时间复杂度：O(k) - 翻转k个节点
	// 空间复杂度：O(1) - 只使用常数额外空间
	// 参数：
	//   s - 要翻转的起始节点
	//   e - 要翻转的结束节点
	// 例如：s -> a -> b -> c -> e -> 下一组的开始节点
	// 翻转后：e -> c -> b -> a -> s -> 下一组的开始节点
	public static void reverse(ListNode s, ListNode e) {
		// 保存下一组的开始节点
		e = e.next;
		// 初始化前驱节点、当前节点和后继节点
		ListNode pre = null, cur = s, next = null;
		
		// 翻转链表直到下一组的开始节点
		while (cur != e) {
			// 保存下一个节点
			next = cur.next;
			// 翻转当前节点的指针
			cur.next = pre;
			// 移动指针
			pre = cur;
			cur = next;
		}
		
		// 连接翻转后的链表和下一组
		s.next = e;
	}
	
	/*
	 * 题目扩展：LeetCode 25. K 个一组翻转链表
	 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
	 * 链接：https://leetcode.cn/problems/reverse-nodes-in-k-group/
	 * 
	 * 题目描述：
	 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
	 * k 是一个正整数，它的值小于或等于链表的长度。
	 * 如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
	 * 
	 * 解题思路：
	 * 1. 分组处理：每k个节点为一组
	 * 2. 翻转每组内的节点
	 * 3. 连接各组结果
	 * 
	 * 时间复杂度：O(n) - 每个节点最多被访问两次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是
	 * 
	 * 工程化考量：
	 * 1. 边界情况处理：k为1时无需翻转，节点数不足k时不翻转
	 * 2. 异常处理：输入参数校验
	 * 3. 代码可读性：函数职责单一，命名清晰
	 * 
	 * 与机器学习等领域的联系：
	 * 1. 在序列模型中，有时需要对序列进行分段处理
	 * 2. 类似于时间序列的滑动窗口处理
	 * 
	 * 语言特性差异：
	 * Java: 垃圾回收自动管理内存
	 * C++: 需要注意指针操作避免内存泄漏
	 * Python: 对象引用计数机制
	 * 
	 * 极端输入场景：
	 * 1. k=1时无需翻转
	 * 2. 节点数为k的倍数
	 * 3. 节点数不足k
	 * 4. 空链表
	 * 5. 单节点链表
	 * 
	 * 测试用例示例：
	 * // ListNode head = new ListNode(1);
	 * // head.next = new ListNode(2);
	 * // head.next.next = new ListNode(3);
	 * // head.next.next.next = new ListNode(4);
	 * // head.next.next.next.next = new ListNode(5);
	 * // 
	 * // ListNode result = reverseKGroup(head, 2);
	 * // // 结果应为: 2->1->4->3->5
	 */
}