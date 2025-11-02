package class038;

import java.util.Stack;

// 用递归函数逆序栈
public class Code05_ReverseStackWithRecursive {

	/**
	 * 仅使用递归函数将栈逆序
	 * 
	 * 算法思路：
	 * 1. 递归地移除栈底元素
	 * 2. 逆序处理剩余元素
	 * 3. 将之前移除的栈底元素压入栈顶
	 * 
	 * 时间复杂度：O(n^2)，其中n为栈的大小
	 * 空间复杂度：O(n)，递归栈深度
	 * 
	 * @param stack 待逆序的栈
	 */
	public static void reverse(Stack<Integer> stack) {
		if (stack.isEmpty()) {
			return;
		}
		int num = bottomOut(stack);
		reverse(stack);
		stack.push(num);
	}

	/**
	 * 移除并返回栈底元素，其他元素向下移动
	 * 
	 * @param stack 栈
	 * @return 栈底元素
	 */
	// 栈底元素移除掉，上面的元素盖下来
	// 返回移除掉的栈底元素
	public static int bottomOut(Stack<Integer> stack) {
		int ans = stack.pop();
		if (stack.isEmpty()) {
			return ans;
		} else {
			int last = bottomOut(stack);
			stack.push(ans);
			return last;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		stack.push(2);
		stack.push(3);
		stack.push(4);
		stack.push(5);
		
		System.out.println("逆序前: " + stack);
		reverse(stack);
		System.out.println("逆序后: " + stack);
		
		// 测试空栈
		Stack<Integer> emptyStack = new Stack<Integer>();
		System.out.println("\n空栈逆序前: " + emptyStack);
		reverse(emptyStack);
		System.out.println("空栈逆序后: " + emptyStack);
	}

}