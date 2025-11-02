package class038;

// 打印n层汉诺塔问题的最优移动轨迹
public class Code07_TowerOfHanoi {

	/**
	 * 打印n层汉诺塔问题的最优移动轨迹
	 * 
	 * 算法思路：
	 * 1. 将n-1个盘子从起始柱借助目标柱移动到辅助柱
	 * 2. 将第n个盘子从起始柱移动到目标柱
	 * 3. 将n-1个盘子从辅助柱借助起始柱移动到目标柱
	 * 
	 * 时间复杂度：O(2^n)
	 * 空间复杂度：O(n)
	 * 
	 * @param n 盘子数量
	 */
	public static void hanoi(int n) {
		if (n > 0) {
			f(n, "左", "右", "中");
		}
	}

	/**
	 * 递归移动盘子
	 * 
	 * @param i 盘子数量
	 * @param from 起始柱
	 * @param to 目标柱
	 * @param other 辅助柱
	 */
	public static void f(int i, String from, String to, String other) {
		if (i == 1) {
			System.out.println("移动圆盘 1 从 " + from + " 到 " + to);
		} else {
			f(i - 1, from, other, to);
			System.out.println("移动圆盘 " + i + " 从 " + from + " 到 " + to);
			f(i - 1, other, to, from);
		}
	}

	public static void main(String[] args) {
		int n = 3;
		System.out.println("汉诺塔移动步骤 (n=" + n + "):");
		hanoi(n);
		
		System.out.println("\n汉诺塔移动步骤 (n=2):");
		hanoi(2);
	}

}