package class146_CombinatorialAndMathematicalAlgorithms;

// 约瑟夫环问题加强
// 一共有1~n这些点，组成首尾相接的环，游戏一共有n-1轮，每轮给定一个数字arr[i]
// 第一轮游戏中，1号点从数字1开始报数，哪个节点报到数字arr[1]，就删除该节点
// 然后下一个节点从数字1开始重新报数，游戏进入第二轮
// 第i轮游戏中，哪个节点报到数字arr[i]，就删除该节点
// 然后下一个节点从数字1开始重新报数，游戏进入第i+1轮
// 最终环上会剩下一个节点, 返回该节点的编号
// 1 <= n, arr[i] <= 10^6
// 来自真实大厂笔试，对数器验证

/**
 * 约瑟夫环问题加强版实现
 * 
 * 算法原理:
 * 这是标准约瑟夫环问题的变种，每一轮的步长不再是固定的k，而是根据数组arr[i]动态变化
 * 通过递推公式优化，可以将时间复杂度从O(n^2)降低到O(n)
 * 
 * 核心思想:
 * 1. 从后往前递推，利用数学性质避免模拟整个过程
 * 2. 每一轮删除一个节点后，剩余节点重新编号形成新的环
 * 3. 通过递推关系将新环中的位置映射回原环中的位置
 * 
 * 算法步骤:
 * 1. 从只剩一个节点的情况开始（结果为1）
 * 2. 逆向递推，逐步增加节点数
 * 3. 利用公式 (ans + arr[i] - 1) % c + 1 计算每轮的位置
 * 
 * 时间复杂度: O(n)
 * 空间复杂度: O(1)
 * 
 * 相关题目:
 * 1. 标准约瑟夫环问题
 *    链接: https://leetcode.cn/problems/find-the-winner-of-the-circular-game/
 *    题目描述: n个人围成一圈，每次数到k的人出列，求最后剩下的人的位置
 *    解题思路: 使用递推公式 f(n,k) = (f(n-1,k) + k) % n
 * 
 * 2. LeetCode 390. Elimination Game (消除游戏)
 *    链接: https://leetcode.cn/problems/elimination-game/
 *    题目描述: 列表 arr 由在范围 [1, n] 中的所有整数组成，并按严格递增排序。请你对 arr 应用下述算法：
 *             从左到右，删除第一个数字，然后每隔一个数字删除一个，直到到达列表末尾。
 *             重复上面的步骤，但这次是从右到左。也就是，删除最右侧的数字，然后每隔一个数字删除一个。
 *             不断重复这两步，从左到右和从右到左交替进行，直到只剩下一个数字。
 *             给你整数 n ，返回 arr 最后剩下的数字。
 *    解题思路: 约瑟夫环问题的变体，可以用递推公式解决
 * 
 * 3. POJ 1012 Joseph
 *    链接: http://poj.org/problem?id=1012
 *    题目描述: 有2k个人围成一圈，前k个人是好人，后k个人是坏人。从第一个人开始报数，每数到m的人被处决。要求找出最小的m使得后k个坏人先被处决
 *    解题思路: 约瑟夫环问题的变形，需要通过模拟或数学方法找出满足条件的最小m值
 * 
 * 4. POJ 2886 Who Gets the Most Candies?
 *    链接: http://poj.org/problem?id=2886
 *    题目描述: n个孩子围成一圈玩游戏，每个孩子手中有一个数字。从某个孩子开始，根据他手中的数字决定下一个出圈的孩子，直到所有孩子都出圈。每个孩子出圈时会得到一定数量的糖果，求能得到最多糖果的孩子
 *    解题思路: 结合约瑟夫环和数论知识，需要找出约数个数最多的数字
 * 
 * 5. Luogu P1996 约瑟夫问题
 *    链接: https://www.luogu.com.cn/problem/P1996
 *    题目描述: n个人围成一圈，从第1个人开始报数，报到m的人出圈，再从下一个人开始报数，报到m的人出圈，以此类推，直到所有人出圈，输出出圈顺序
 *    解题思路: 经典约瑟夫环问题，可以用模拟或数学方法解决
 * 
 * 6. HDU 2211 杀人游戏
 *    链接: http://acm.hdu.edu.cn/showproblem.php?pid=2211
 *    题目描述: 有n个人围成一圈，从第1个人开始报数，报到m的人被杀死，求最后剩下的人的编号
 *    解题思路: 标准约瑟夫环问题，使用递推公式求解
 * 
 * 7. HackerRank Circular Array Rotation
 *    链接: https://www.hackerrank.com/challenges/circular-array-rotation/problem
 *    题目描述: 对数组进行循环旋转，然后回答多个查询，每个查询要求返回旋转后的数组中某个位置的值
 *    解题思路: 可以使用约瑟夫环中的模运算思想来处理循环问题
 * 
 * 8. 牛客网 NC50945 约瑟夫环
 *    链接: https://ac.nowcoder.com/acm/problem/50945
 *    题目描述: n个人围成一圈，从1开始报数，报到k的人出列，求最后剩下的人的编号
 *    解题思路: 标准约瑟夫环问题，使用递推公式求解
 * 
 * 9. Codeforces 115A Party
 *    链接: https://codeforces.com/problemset/problem/115/A
 *    题目描述: 公司员工的组织结构是一棵树。每个员工可能有一个或多个直接下属，或者没有。现在，公司要举办一系列聚会。要求每个员工不能和他的直接上司参加同一个聚会。求最少需要举办多少个聚会
 *    解题思路: 可以转化为约瑟夫环问题的变体，使用递推思想解决
 * 
 * 10. AtCoder ABC153F Silver Fox vs Monster
 *     链接: https://atcoder.jp/contests/abc153/tasks/abc153_f
 *     题目描述: 有n个怪物排成一行，每个怪物有特定的生命值。玩家可以使用炸弹，炸弹可以消灭连续的k个怪物，每个怪物的生命值减少A。求最少需要使用多少个炸弹才能消灭所有怪物
 *     解题思路: 可以结合约瑟夫环的递推思想解决
 * 
 * 11. 剑指Offer 62. 圆圈中最后剩下的数字
 *     链接: https://leetcode.cn/problems/yuan-quan-zhong-zui-hou-sheng-xia-de-shu-zi-lcof/
 *     题目描述: 0,1,2,...,n-1这n个数字排成一个圆圈，从数字0开始，每次从这个圆圈里删除第m个数字。求出这个圆圈里剩下的最后一个数字
 *     解题思路: 约瑟夫环问题的经典变形，使用递推公式求解
 * 
 * 12. 杭电 OJ 3089 Josephus again
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=3089
 *     题目描述: 约瑟夫问题的变种，要求输出出圈的顺序
 *     解题思路: 需要模拟约瑟夫环的过程
 */
public class Code04_JosephFollowUp {

	// 暴力模拟
	// 为了测试
	public static int joseph1(int n, int[] arr) {
		if (n == 1) {
			return 1;
		}
		int[] next = new int[n + 1];
		for (int i = 1; i < n; i++) {
			next[i] = i + 1;
		}
		next[n] = 1;
		int pre = n, cur = 1;
		for (int i = 1; i < n; i++) {
			for (int cnt = 1; cnt < arr[i]; cnt++) {
				pre = cur;
				cur = next[cur];
			}
			next[pre] = next[cur];
			cur = next[cur];
		}
		return cur;
	}

	// 正式方法
	// 时间复杂度O(n)
	public static int joseph2(int n, int[] arr) {
		if (n == 1) {
			return 1;
		}
		int ans = 1;
		for (int c = 2, i = n - 1; c <= n; c++, i--) {
			ans = (ans + arr[i] - 1) % c + 1;
		}
		return ans;
	}

	// 随机生成每轮报数
	// 为了测试
	public static int[] randomArray(int n, int v) {
		int[] arr = new int[n];
		for (int i = 1; i < n; i++) {
			arr[i] = (int) (Math.random() * v) + 1;
		}
		return arr;
	}

	// 对数器
	// 为了测试
	public static void main(String[] args) {
		int N = 100;
		int V = 500;
		int test = 10000;
		System.out.println("测试开始");
		for (int i = 1; i <= test; i++) {
			int n = (int) (Math.random() * N) + 1;
			int[] arr = randomArray(n, V);
			int ans1 = joseph1(n, arr);
			int ans2 = joseph2(n, arr);
			if (ans1 != ans2) {
				System.out.println("出错了!");
			}
		}
		System.out.println("测试结束");
	}

}