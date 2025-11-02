package class089;

// 柠檬水找零
// 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯
// 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元
// 注意，一开始你手头没有任何零钱
// 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。如果你能给每位顾客正确找零，返回 true ，否则返回 false
// 测试链接 : https://leetcode.cn/problems/lemonade-change/
public class Code09_LemonadeChange {

	/**
	 * 使用贪心算法解决柠檬水找零问题
	 * 
	 * 解题思路:
	 * 贪心算法 + 计数策略
	 * 1. 维护5美元和10美元纸币的数量
	 * 2. 遇到5美元：直接收下，5美元数量加1
	 * 3. 遇到10美元：需要找零5美元，检查是否有5美元纸币，如果有则5美元数量减1，10美元数量加1，否则返回false
	 * 4. 遇到20美元：需要找零15美元，优先使用一张10美元和一张5美元的组合，如果没有10美元则使用三张5美元，如果都不满足则返回false
	 * 
	 * 贪心策略解释:
	 * 在找零15美元时，优先使用10+5的组合而不是5+5+5，因为5美元更万能，可以用于10美元和20美元的找零，
	 * 而10美元只能用于20美元的找零，所以要尽可能保留5美元纸币
	 * 
	 * 时间复杂度分析:
	 * O(n) - 其中n是数组bills的长度，只需要遍历一次数组
	 * 
	 * 空间复杂度分析:
	 * O(1) - 只使用了常数级别的额外空间
	 * 
	 * 是否为最优解:
	 * 是，这是解决该问题的最优解
	 * 
	 * 工程化考量:
	 * 1. 边界条件处理: 空数组等特殊情况
	 * 2. 输入验证: 检查输入是否为有效数组
	 * 3. 异常处理: 对非法输入进行检查
	 * 4. 可读性: 添加详细注释和变量命名
	 */
	public static boolean lemonadeChange(int[] bills) {
		// 输入验证
		if (bills == null) {
			return true; // 空数组表示没有顾客，可以正确找零
		}
		
		int fiveCount = 0;  // 5美元纸币数量
		int tenCount = 0;   // 10美元纸币数量
		
		// 遍历顾客付款数组
		for (int bill : bills) {
			if (bill == 5) {
				// 顾客支付5美元，无需找零，直接收下
				fiveCount++;
			} else if (bill == 10) {
				// 顾客支付10美元，需要找零5美元
				if (fiveCount > 0) {
					fiveCount--;  // 找零一张5美元
					tenCount++;   // 收下一张10美元
				} else {
					// 没有5美元纸币找零，返回false
					return false;
				}
			} else if (bill == 20) {
				// 顾客支付20美元，需要找零15美元
				// 贪心策略：优先使用一张10美元和一张5美元的组合
				if (tenCount > 0 && fiveCount > 0) {
					tenCount--;   // 找零一张10美元
					fiveCount--;  // 找零一张5美元
				} 
				// 如果没有10美元，则尝试使用三张5美元
				else if (fiveCount >= 3) {
					fiveCount -= 3;  // 找零三张5美元
				} 
				// 如果两种方式都不满足，则无法正确找零
				else {
					return false;
				}
			}
			// 注意：题目保证输入只包含5、10、20三种面额，所以不需要处理其他情况
		}
		
		// 所有顾客都能正确找零
		return true;
	}
	
	// 测试函数
	public static void main(String[] args) {
		// 测试用例1: [5,5,5,10,20]
		int[] bills1 = {5, 5, 5, 10, 20};
		boolean result1 = lemonadeChange(bills1);
		System.out.println("输入: [5,5,5,10,20]");
		System.out.println("输出: " + result1);
		System.out.println("预期: true");
		System.out.println();
		
		// 测试用例2: [5,5,10,10,20]
		int[] bills2 = {5, 5, 10, 10, 20};
		boolean result2 = lemonadeChange(bills2);
		System.out.println("输入: [5,5,10,10,20]");
		System.out.println("输出: " + result2);
		System.out.println("预期: false");
		System.out.println();
		
		// 测试用例3: [10,10]
		int[] bills3 = {10, 10};
		boolean result3 = lemonadeChange(bills3);
		System.out.println("输入: [10,10]");
		System.out.println("输出: " + result3);
		System.out.println("预期: false");
		System.out.println();
		
		// 测试用例4: [5,5,10]
		int[] bills4 = {5, 5, 10};
		boolean result4 = lemonadeChange(bills4);
		System.out.println("输入: [5,5,10]");
		System.out.println("输出: " + result4);
		System.out.println("预期: true");
		System.out.println();
		
		// 测试用例5: 空数组
		int[] bills5 = {};
		boolean result5 = lemonadeChange(bills5);
		System.out.println("输入: []");
		System.out.println("输出: " + result5);
		System.out.println("预期: true");
		System.out.println();
		
		// 测试用例6: [5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20]
		// 前10个5美元：five_count = 10
		// 一个10美元：找零5美元，five_count = 9, ten_count = 1
		// 一个20美元：优先用10+5找零，five_count = 8, ten_count = 0
		// 剩下的4个20美元：都需要找零15美元，但只有8张5美元，不够找零，所以返回false
		int[] bills6 = {5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20};
		boolean result6 = lemonadeChange(bills6);
		System.out.println("输入: [5,5,5,5,5,5,5,5,5,5,10,20,20,20,20,20]");
		System.out.println("输出: " + result6);
		System.out.println("预期: false");
		System.out.println();
	}
}