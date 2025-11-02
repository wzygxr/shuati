package class032;

import java.util.HashSet;

// 位图的实现
// Bitset(int size) - 创建一个能容纳size个元素的位集
// void add(int num) - 将数字num添加到位集中
// void remove(int num) - 从位集中移除数字num
// void reverse(int num) - 反转位集中数字num的状态（存在变不存在，不存在变存在）
// boolean contains(int num) - 检查数字num是否在位集中
public class Code01_Bitset {

	// 位图的实现
	// 使用时num不要超过初始化的大小
	public static class Bitset {
		// 使用整数数组来存储位信息，每个整数可以表示32位
		public int[] set;

		// n个数字 : 0~n-1
		// 构造函数，创建一个能容纳n个元素的位集
		// 参数n表示位集能处理的最大数字范围是0到n-1
		public Bitset(int n) {
			// a/b如果结果想向上取整，可以写成 : (a+b-1)/b
			// 前提是a和b都是非负数
			// 计算需要多少个整数来表示n个位
			// 例如：n=100，则需要(100+31)/32 = 4个整数来表示100个位
			set = new int[(n + 31) / 32];
		}

		// 将数字num添加到位集中
		// 通过将对应位设置为1来表示该数字存在
		// 时间复杂度：O(1)
		public void add(int num) {
			// num / 32 确定num在数组中的哪个整数
			// num % 32 确定num在该整数中的哪一位
			// 1 << (num % 32) 创建一个只有第(num % 32)位为1的数
			// 使用按位或操作将该位置为1
			set[num / 32] |= 1 << (num % 32);
		}

		// 从位集中移除数字num
		// 通过将对应位设置为0来表示该数字不存在
		// 时间复杂度：O(1)
		public void remove(int num) {
			// ~(1 << (num % 32)) 创建一个只有第(num % 32)位为0，其他位都为1的数
			// 使用按位与操作将该位置为0
			set[num / 32] &= ~(1 << (num % 32));
		}

		// 反转位集中数字num的状态
		// 如果num存在则移除，如果不存在则添加
		// 时间复杂度：O(1)
		public void reverse(int num) {
			// 1 << (num % 32) 创建一个只有第(num % 32)位为1的数
			// 使用按位异或操作切换该位的状态
			set[num / 32] ^= 1 << (num % 32);
		}

		// 检查数字num是否在位集中
		// 时间复杂度：O(1)
		// 返回值：如果num在位集中返回true，否则返回false
		public boolean contains(int num) {
			// (set[num / 32] >> (num % 32)) 将第(num % 32)位移到最低位
			// & 1 提取最低位
			// == 1 判断该位是否为1
			return ((set[num / 32] >> (num % 32)) & 1) == 1;
		}

	}

	// 对数器测试
	// 使用自定义的Bitset和Java内置的HashSet进行对比测试，验证实现的正确性
	public static void main(String[] args) {
		int n = 1000;         // 测试数据范围：0~999
		int testTimes = 10000; // 测试操作次数
		System.out.println("测试开始");
		// 实现的位图结构
		Bitset bitSet = new Bitset(n);
		// 直接用HashSet做对比测试
		HashSet<Integer> hashSet = new HashSet<>();
		System.out.println("调用阶段开始");
		for (int i = 0; i < testTimes; i++) {
			double decide = Math.random(); // 随机决定执行哪种操作
			// number -> 0 ~ n-1，等概率得到
			int number = (int) (Math.random() * n); // 随机生成测试数字
			if (decide < 0.333) {
				// 33.3%概率执行添加操作
				bitSet.add(number);
				hashSet.add(number);
			} else if (decide < 0.666) {
				// 33.3%概率执行删除操作
				bitSet.remove(number);
				hashSet.remove(number);
			} else {
				// 33.4%概率执行反转操作
				bitSet.reverse(number);
				if (hashSet.contains(number)) {
					hashSet.remove(number);
				} else {
					hashSet.add(number);
				}
			}
		}
		System.out.println("调用阶段结束");
		System.out.println("验证阶段开始");
		// 验证两个数据结构的结果是否一致
		for (int i = 0; i < n; i++) {
			if (bitSet.contains(i) != hashSet.contains(i)) {
				System.out.println("出错了!");
			}
		}
		System.out.println("验证阶段结束");
		System.out.println("测试结束");
	}

}