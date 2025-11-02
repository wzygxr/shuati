package class034;

// 返回链表的第一个入环节点
// 测试链接 : https://leetcode.cn/problems/linked-list-cycle-ii/
public class Code05_LinkedListCycleII {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;
	}

	// 提交如下的方法
	public static ListNode detectCycle(ListNode head) {
		if (head == null || head.next == null || head.next.next == null) {
			return null;
		}
		ListNode slow = head.next;
		ListNode fast = head.next.next;
		while (slow != fast) {
			if (fast.next == null || fast.next.next == null) {
				return null;
			}
			slow = slow.next;
			fast = fast.next.next;
		}
		fast = head;
		while (slow != fast) {
			slow = slow.next;
			fast = fast.next;
		}
		return slow;
	}
	
	/*
	 * 题目扩展：LeetCode 142. 环形链表 II
	 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
	 * 
	 * 题目描述：
	 * 给定一个链表的头节点  head ，返回链表开始入环的第一个节点。
	 * 如果链表无环，则返回 null。
	 * 
	 * 解题思路：
	 * 1. 使用快慢指针检测是否有环
	 * 2. 若有环，将快指针重置到头节点
	 * 3. 快慢指针同时以相同速度移动，相遇点即为入环点
	 * 
	 * 时间复杂度：O(n) - 最多遍历链表两次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是
	 * 
	 * 工程化考量：
	 * 1. 边界情况处理：空链表、单节点链表、无环链表
	 * 2. 算法鲁棒性：处理各种异常输入
	 * 3. 代码可读性：注释清晰说明算法原理
	 * 
	 * 与机器学习等领域的联系：
	 * 1. 循环检测在图论中用于检测有向图中的环
	 * 2. 在推荐系统中用于检测用户行为循环模式
	 * 
	 * 语言特性差异：
	 * Java: null值检查
	 * C++: 空指针检查
	 * Python: None值检查
	 * 
	 * 极端输入场景：
	 * 1. 空链表
	 * 2. 单节点自环
	 * 3. 大环链表
	 * 4. 环入口在头部
	 * 5. 无环链表
	 */

}