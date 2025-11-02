package class091;

// 种花问题
// 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
// 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
// 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，
// 其中 0 表示没种植花，1 表示种植了花。
// 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？
// 能则返回 true ，不能则返回 false 。
// 测试链接 : https://leetcode.cn/problems/can-place-flowers/
public class Code18_CanPlaceFlowers {

	/**
	 * 种花问题
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 遍历花坛数组
	 * 2. 对于每个位置，检查是否可以种花：
	 *    - 当前位置为0
	 *    - 前一个位置为0或不存在
	 *    - 后一个位置为0或不存在
	 * 3. 如果可以种花，就种下并计数
	 * 4. 最后比较种花数量与n的大小
	 * 
	 * 正确性分析：
	 * 1. 贪心选择：能种就种，这样可以种下最多的花
	 * 2. 局部最优解能够达到全局最优解
	 * 3. 种花后将位置标记为1，避免重复计算
	 * 
	 * 时间复杂度：O(n) - 只需要遍历数组一次
	 * 空间复杂度：O(1) - 只使用常数额外空间
	 * 
	 * @param flowerbed 花坛数组
	 * @param n 需要种的花的数量
	 * @return 是否能种下n朵花
	 */
	public static boolean canPlaceFlowers(int[] flowerbed, int n) {
		int count = 0;  // 已种花的数量
		
		// 遍历花坛数组
		for (int i = 0; i < flowerbed.length; i++) {
			// 检查当前位置是否可以种花
			if (flowerbed[i] == 0) {
				// 检查前一个位置是否为0或不存在
				boolean prevEmpty = (i == 0) || (flowerbed[i - 1] == 0);
				// 检查后一个位置是否为0或不存在
				boolean nextEmpty = (i == flowerbed.length - 1) || (flowerbed[i + 1] == 0);
				
				// 如果前后都为空，可以种花
				if (prevEmpty && nextEmpty) {
					flowerbed[i] = 1;  // 种花
					count++;           // 计数增加
					
					// 如果已经种够了，提前返回
					if (count >= n) {
						return true;
					}
				}
			}
		}
		
		// 返回是否能种下n朵花
		return count >= n;
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: flowerbed = [1,0,0,0,1], n = 1 -> 输出: true
		int[] flowerbed1 = {1, 0, 0, 0, 1};
		int n1 = 1;
		System.out.println("测试用例1:");
		System.out.println("花坛: " + java.util.Arrays.toString(flowerbed1) + ", n = " + n1);
		System.out.println("能否种下: " + canPlaceFlowers(flowerbed1, n1)); // 期望输出: true
		
		// 测试用例2: flowerbed = [1,0,0,0,1], n = 2 -> 输出: false
		int[] flowerbed2 = {1, 0, 0, 0, 1};
		int n2 = 2;
		System.out.println("\n测试用例2:");
		System.out.println("花坛: " + java.util.Arrays.toString(flowerbed2) + ", n = " + n2);
		System.out.println("能否种下: " + canPlaceFlowers(flowerbed2, n2)); // 期望输出: false
		
		// 测试用例3: flowerbed = [0,0,1,0,0], n = 1 -> 输出: true
		int[] flowerbed3 = {0, 0, 1, 0, 0};
		int n3 = 1;
		System.out.println("\n测试用例3:");
		System.out.println("花坛: " + java.util.Arrays.toString(flowerbed3) + ", n = " + n3);
		System.out.println("能否种下: " + canPlaceFlowers(flowerbed3, n3)); // 期望输出: true
		
		// 测试用例4: flowerbed = [0], n = 1 -> 输出: true
		int[] flowerbed4 = {0};
		int n4 = 1;
		System.out.println("\n测试用例4:");
		System.out.println("花坛: " + java.util.Arrays.toString(flowerbed4) + ", n = " + n4);
		System.out.println("能否种下: " + canPlaceFlowers(flowerbed4, n4)); // 期望输出: true
	}
}