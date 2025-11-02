package class034;

// 判断链表是否是回文结构
// 测试链接 : https://leetcode.cn/problems/palindrome-linked-list/
public class Code04_PalindromeLinkedList {

	// 不要提交这个类
	public static class ListNode {
		public int val;
		public ListNode next;
	}

	// 提交如下的方法
	public static boolean isPalindrome(ListNode head) {
		if (head == null || head.next == null) {
			return true;
		}
		ListNode slow = head, fast = head;
		// 找中点
		while (fast.next != null && fast.next.next != null) {
			slow = slow.next;
			fast = fast.next.next;
		}
		// 现在中点就是slow，从中点开始往后的节点逆序
		ListNode pre = slow;
		ListNode cur = pre.next;
		ListNode next = null;
		pre.next = null;
		while (cur != null) {
			next = cur.next;
			cur.next = pre;
			pre = cur;
			cur = next;
		}
		// 上面的过程已经把链表调整成从左右两侧往中间指
		// head -> ... -> slow <- ... <- pre
		boolean ans = true;
		ListNode left = head;
		ListNode right = pre;
		// left往右、right往左，每一步比对值是否一样，如果某一步不一样答案就是false
		while (left != null && right != null) {
			if (left.val != right.val) {
				ans = false;
				break;
			}
			left = left.next;
			right = right.next;
		}
		// 本着不坑的原则，把链表调整回原来的样子再返回判断结果
		cur = pre.next;
		pre.next = null;
		next = null;
		while (cur != null) {
			next = cur.next;
			cur.next = pre;
			pre = cur;
			cur = next;
		}
		return ans;
	}
	
	/*
	 * 题目扩展：LeetCode 234. 回文链表
	 * 来源：LeetCode、牛客网、剑指Offer等各大算法平台
	 * 
	 * 题目描述：
	 * 给你一个单链表的头节点 head ，请你判断该链表是否为回文链表。
	 * 如果是，返回 true ；否则，返回 false 。
	 * 
	 * 解题思路：
	 * 1. 使用快慢指针找到链表中点
	 * 2. 反转后半部分链表
	 * 3. 比较前半部分和反转后的后半部分
	 * 4. 恢复链表结构（不破坏原链表）
	 * 
	 * 时间复杂度：O(n) - 需要遍历链表多次但仍是线性时间
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 是否最优解：是
	 * 
	 * 工程化考量：
	 * 1. 边界情况处理：空链表、单节点链表
	 * 2. 不破坏原数据结构的原则
	 * 3. 代码可读性和维护性
	 * 
	 * 与机器学习等领域的联系：
	 * 1. 回文检测在自然语言处理中用于识别对称结构
	 * 2. 序列对称性在生物信息学中用于分析DNA/RNA序列
	 * 
	 * 语言特性差异：
	 * Java: boolean类型直接返回
	 * C++: 返回bool类型
	 * Python: 返回True/False
	 * 
	 * 极端输入场景：
	 * 1. 空链表
	 * 2. 单节点链表
	 * 3. 两节点链表
	 * 4. 全部节点值相同
	 * 5. 完全不对称链表
	 */

}