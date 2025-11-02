package class091;

import java.util.Arrays;

// 根据身高重建队列
// 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
// 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
// 请你重新构造并返回输入数组 people 所表示的队列。
// 测试链接 : https://leetcode.cn/problems/queue-reconstruction-by-height/
public class Code14_QueueReconstructionByHeight {

	/**
	 * 根据身高重建队列
	 * 
	 * 算法思路：
	 * 使用贪心策略：
	 * 1. 按身高降序排序，身高相同时按k值升序排序
	 * 2. 依次将每个人插入到结果队列的第k个位置
	 * 
	 * 正确性分析：
	 * 1. 身高高的人看不到身高低的人，所以先安排身高高的人
	 * 2. 身高相同时，k值小的应该排在前面
	 * 3. 当处理到某个人时，所有已处理的人都比他高或等高
	 * 4. 将他插入到第k个位置，前面正好有k个身高大于或等于他的人
	 * 
	 * 时间复杂度：O(n^2) - 排序O(n*logn)，插入操作O(n^2)
	 * 空间复杂度：O(logn) - 排序所需的额外空间
	 * 
	 * @param people 人员信息数组
	 * @return 重建后的队列
	 */
	public static int[][] reconstructQueue(int[][] people) {
		// 按身高降序排序，身高相同时按k值升序排序
		Arrays.sort(people, (a, b) -> {
			if (a[0] != b[0]) {
				return b[0] - a[0];  // 身高降序
			} else {
				return a[1] - b[1];  // k值升序
			}
		});
		
		// 使用链表来优化插入操作
		java.util.List<int[]> result = new java.util.ArrayList<>();
		
		// 依次将每个人插入到结果队列的第k个位置
		for (int[] person : people) {
			result.add(person[1], person);
		}
		
		// 转换为数组返回
		return result.toArray(new int[result.size()][]);
	}

	// 测试用例
	public static void main(String[] args) {
		// 测试用例1: people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]] -> 输出: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
		int[][] people1 = {{7, 0}, {4, 4}, {7, 1}, {5, 0}, {6, 1}, {5, 2}};
		System.out.println("测试用例1:");
		System.out.println("人员信息: " + Arrays.deepToString(people1));
		int[][] result1 = reconstructQueue(people1);
		System.out.println("重建队列: " + Arrays.deepToString(result1));
		// 期望输出: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]
		
		// 测试用例2: people = [[6,0],[5,0],[4,0],[3,2],[2,2],[1,4]] -> 输出: [[4,0],[5,0],[2,2],[3,2],[1,4],[6,0]]
		int[][] people2 = {{6, 0}, {5, 0}, {4, 0}, {3, 2}, {2, 2}, {1, 4}};
		System.out.println("\n测试用例2:");
		System.out.println("人员信息: " + Arrays.deepToString(people2));
		int[][] result2 = reconstructQueue(people2);
		System.out.println("重建队列: " + Arrays.deepToString(result2));
		// 期望输出: [[4,0],[5,0],[2,2],[3,2],[1,4],[6,0]]
	}
}