package class092;

import java.util.PriorityQueue;

// 合并果子
// 在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
// 多多决定把所有的果子合成一堆。
// 每一次合并，多多可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
// 可以看出，所有的果子经过n-1次合并之后，就只剩下一堆了。
// 多多在合并果子时总共消耗的体力等于每次合并所耗体力的和。
// 因为还要花大力气把这些果子搬回家，所以多多在合并果子时要尽可能地节省体力。
// 假定每个果子重量都为1，并且已知果子的堆数和每堆果子的数目，
// 你的任务是设计出合并的次序方案，使多多耗费的体力最少，并输出这个最小的体力耗费值。
// 测试链接 : https://www.luogu.com.cn/problem/P1090
public class Code12_MergeFruits {

	/*
	 * 贪心算法解法（使用优先队列/最小堆）
	 * 
	 * 核心思想：
	 * 1. 为了使消耗的体力最小，每次都应该选择当前重量最小的两堆果子进行合并
	 * 2. 这可以通过优先队列（最小堆）来高效实现
	 * 
	 * 算法步骤：
	 * 1. 将所有果子堆的重量放入最小堆中
	 * 2. 每次从堆中取出两个最小的元素，将它们合并（相加）
	 * 3. 将合并后的结果放回堆中
	 * 4. 重复步骤2-3直到堆中只剩一个元素
	 * 5. 累计所有合并操作的体力消耗
	 * 
	 * 时间复杂度：O(n log n) - 每次操作需要O(log n)时间，共n-1次操作
	 * 空间复杂度：O(n) - 需要额外的优先队列存储元素
	 * 
	 * 为什么这是最优解？
	 * 1. 贪心策略保证了每一步都做出了当前看起来最好的选择
	 * 2. 通过哈夫曼编码的理论可以证明这种策略能得到全局最优解
	 * 3. 无法在更少的时间内完成，因为至少需要处理所有元素
	 * 
	 * 工程化考虑：
	 * 1. 边界条件处理：空数组、单元素数组
	 * 2. 异常处理：输入参数验证
	 * 3. 可读性：变量命名清晰，注释详细
	 * 
	 * 算法调试技巧：
	 * 1. 可以通过打印每一步的堆状态来观察合并过程
	 * 2. 用断言验证中间结果是否符合预期
	 * 
	 * 与机器学习的联系：
	 * 1. 这个问题与哈夫曼编码密切相关，哈夫曼编码在数据压缩中有重要应用
	 * 2. 在决策树构建中也有类似的贪心思想
	 */

	public static int mergeFruits(int[] fruits) {
		// 边界条件：如果果子堆数小于等于1，不需要合并
		if (fruits == null || fruits.length <= 1) {
			return 0;
		}

		// 创建最小堆
		PriorityQueue<Integer> minHeap = new PriorityQueue<>();

		// 将所有果子堆的重量放入最小堆中
		for (int fruit : fruits) {
			minHeap.offer(fruit);
		}

		// 记录总消耗的体力
		int totalEnergy = 0;

		// 每次合并两堆果子，直到只剩一堆
		while (minHeap.size() > 1) {
			// 取出两个最小的元素
			int first = minHeap.poll();
			int second = minHeap.poll();

			// 合并两堆果子
			int merged = first + second;

			// 累计消耗的体力
			totalEnergy += merged;

			// 将合并后的结果放回堆中
			minHeap.offer(merged);
		}

		return totalEnergy;
	}

	// 测试方法
	public static void main(String[] args) {
		// 测试用例1: [1,2,9] -> 15
		int[] fruits1 = { 1, 2, 9 };
		System.out.println("测试用例1: " + java.util.Arrays.toString(fruits1));
		System.out.println("预期结果: 15, 实际结果: " + mergeFruits(fruits1));
		System.out.println();

		// 测试用例2: [3,4,5,6,7] -> 62
		int[] fruits2 = { 3, 4, 5, 6, 7 };
		System.out.println("测试用例2: " + java.util.Arrays.toString(fruits2));
		System.out.println("预期结果: 62, 实际结果: " + mergeFruits(fruits2));
		System.out.println();

		// 测试用例3: [1] -> 0
		int[] fruits3 = { 1 };
		System.out.println("测试用例3: " + java.util.Arrays.toString(fruits3));
		System.out.println("预期结果: 0, 实际结果: " + mergeFruits(fruits3));
		System.out.println();

		// 测试用例4: [1,2] -> 3
		int[] fruits4 = { 1, 2 };
		System.out.println("测试用例4: " + java.util.Arrays.toString(fruits4));
		System.out.println("预期结果: 3, 实际结果: " + mergeFruits(fruits4));
		System.out.println();

		// 测试用例5: [5,5,5,5] -> 40
		int[] fruits5 = { 5, 5, 5, 5 };
		System.out.println("测试用例5: " + java.util.Arrays.toString(fruits5));
		System.out.println("预期结果: 40, 实际结果: " + mergeFruits(fruits5));
		System.out.println();
	}
}