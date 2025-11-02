package class032;

// 位图的实现
// Bitset是一种能以紧凑形式存储位的数据结构
// Bitset(int n) : 初始化n个位，所有位都是0
// void fix(int i) : 将下标i的位上的值更新为1
// void unfix(int i) : 将下标i的位上的值更新为0
// void flip() : 翻转所有位的值
// boolean all() : 是否所有位都是1
// boolean one() : 是否至少有一位是1
// int count() : 返回所有位中1的数量
// String toString() : 返回所有位的状态
public class Code02_DesignBitsetTest {

	// 测试链接 : https://leetcode-cn.com/problems/design-bitset/
	// 实现一个高效的位集数据结构，支持多种位操作
	class Bitset {
		// 使用int数组存储位信息，每个int可以存储32位
		private int[] set;
		// 位的总数
		private final int size;
		// 当前0的个数，用于优化操作
		private int zeros;
		// 当前1的个数，用于优化操作
		private int ones;
		// 是否翻转的标记，用于优化flip操作
		// true表示逻辑状态与实际存储状态相反
		private boolean reverse;

		// 构造函数，初始化n个位，所有位都是0
		// 参数n表示位集的大小
		public Bitset(int n) {
			// 计算需要多少个int来存储n位
			// (n + 31) / 32 是向上取整的写法
			// 例如：n=100，则需要(100+31)/32 = 4个int来存储100位
			set = new int[(n + 31) / 32];
			// 位的总数
			size = n;
			// 初始状态下所有位都是0，所以0的个数为n
			zeros = n;
			// 初始状态下所有位都是0，所以1的个数为0
			ones = 0;
			// 初始状态下没有翻转
			reverse = false;
		}

		// 把i这个数字加入到位图
		// 将下标i的位上的值更新为1
		// 参数i表示要设置为1的位的下标
		public void fix(int i) {
			// 计算i在数组中的位置和位偏移
			// index确定在set数组中的哪个int
			int index = i / 32;
			// bit确定在该int中的哪一位
			int bit = i % 32;
			
			if (!reverse) {
				// 位图所有位的状态，维持原始含义
				// 0 : 不存在
				// 1 : 存在
				// 检查该位是否为0
				if ((set[index] & (1 << bit)) == 0) {
					// 该位实际是0，需要设置为1
					// 0的个数减少1
					zeros--;
					// 1的个数增加1
					ones++;
					// 使用按位或操作将该位设置为1
					set[index] |= (1 << bit);
				}
			} else {
				// 位图所有位的状态，翻转了
				// 0 : 存在
				// 1 : 不存在
				// 检查该位是否为1（在逻辑上是0）
				if ((set[index] & (1 << bit)) != 0) {
					// 该位实际是1，但在逻辑上是0，需要设置为1（即实际设置为0）
					// 0的个数减少1
					zeros--;
					// 1的个数增加1
					ones++;
					// 使用异或操作将该位设置为0
					set[index] ^= (1 << bit);
				}
			}
		}

		// 把i这个数字从位图中移除
		// 将下标i的位上的值更新为0
		// 参数i表示要设置为0的位的下标
		public void unfix(int i) {
			// 计算i在数组中的位置和位偏移
			// index确定在set数组中的哪个int
			int index = i / 32;
			// bit确定在该int中的哪一位
			int bit = i % 32;
			
			if (!reverse) {
				// 位图所有位的状态，维持原始含义
				// 检查该位是否为1
				if ((set[index] & (1 << bit)) != 0) {
					// 该位实际是1，需要设置为0
					// 1的个数减少1
					ones--;
					// 0的个数增加1
					zeros++;
					// 使用异或操作将该位设置为0
					set[index] ^= (1 << bit);
				}
			} else {
				// 位图所有位的状态，翻转了
				// 检查该位是否为0（在逻辑上是1）
				if ((set[index] & (1 << bit)) == 0) {
					// 该位实际是0，但在逻辑上是1，需要设置为0（即实际设置为1）
					// 1的个数减少1
					ones--;
					// 0的个数增加1
					zeros++;
					// 使用按位或操作将该位设置为1
					set[index] |= (1 << bit);
				}
			}
		}

		// 翻转所有位的值
		// 使用懒标记优化，避免每次都实际翻转所有位
		public void flip() {
			// 切换翻转标记
			reverse = !reverse;
			// 翻转后，0和1的个数互换
			// 这是基于数学原理：0变1，1变0
			int tmp = zeros;
			zeros = ones;
			ones = tmp;
		}

		// 检查所有位是否都是1
		// 返回值：如果所有位都是1返回true，否则返回false
		public boolean all() {
			// 所有位都是1当且仅当1的个数等于总位数
			return ones == size;
		}

		// 检查是否至少有一位是1
		// 返回值：如果至少有一位是1返回true，否则返回false
		public boolean one() {
			// 至少有一位是1当且仅当1的个数大于0
			return ones > 0;
		}

		// 返回所有位中1的数量
		// 返回值：1的数量
		public int count() {
			// 直接返回维护的1的个数，避免重新计算
			return ones;
		}

		// 返回所有位的状态
		// 返回值：表示所有位状态的字符串
		public String toString() {
			// 使用StringBuilder提高字符串拼接效率
			StringBuilder builder = new StringBuilder();
			// 遍历每一位
			for (int i = 0, k = 0, number, status; i < size; k++) {
				// 获取第k个int
				number = set[k];
				// 处理该int中的每一位
				for (int j = 0; j < 32 && i < size; j++, i++) {
					// 提取第j位的值
					status = (number >> j) & 1;
					// 根据是否翻转来确定实际的位值
					status ^= reverse ? 1 : 0;
					// 将位值添加到结果字符串中
					builder.append(status);
				}
			}
			return builder.toString();
		}

	}

}