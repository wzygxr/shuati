package class148;

import java.util.Arrays;

// 重建队列(做到最优时间复杂度)
// 一共n个人，每个人有(a, b)两个数据，数据a表示该人的身高
// 数据b表示该人的要求，站在自己左边的人中，正好有b个人的身高大于等于自己的身高
// 请你把n个人从左到右进行排列，要求每个人的要求都可以满足
// 返回其中一种排列即可，本题的数据保证一定存在这样的排列
// 题解中的绝大多数方法，时间复杂度O(n平方)，但是时间复杂度能做到O(n * log n)
// 测试链接 : https://leetcode.cn/problems/queue-reconstruction-by-height/

/*
 * 补充题目列表：
 * 
 * 1. LeetCode 406. Queue Reconstruction by Height
 *    链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
 *    题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 2. 洛谷 P1118 [USACO06FEB]数字三角形
 *    链接: https://www.luogu.com.cn/problem/P1118
 *    题目描述: 使用类似思想解决字典序最小问题
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 3. Codeforces 219D Choosing Capital for Treeland
 *    链接: https://codeforces.com/problemset/problem/219/D
 *    题目描述: 树上动态规划问题，可以使用类似技巧优化
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 4. LeetCode 315. Count of Smaller Numbers After Self
 *    链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    题目描述: 计算数组右侧小于当前元素的元素个数
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 5. LeetCode 327. Count of Range Sum
 *    链接: https://leetcode.cn/problems/count-of-range-sum/
 *    题目描述: 计算和在范围内的子数组个数
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 6. 牛客网 NC145 01序列的最小权值
 *    链接: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
 *    题目描述: 维护01序列，支持插入和查询操作
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 7. AtCoder ABC134 E - Sequence Decomposing
 *    链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
 *    题目描述: 序列分解问题，可使用平衡树优化
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 8. CodeChef ORDERSET
 *    链接: https://www.codechef.com/problems/ORDERSET
 *    题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 * 
 * 9. HackerRank Self-Balancing Tree
 *    链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现AVL树的插入操作
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 * 
 * 10. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
 *     链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
 *     题目描述: 字符串处理问题，可使用平衡树优化
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 算法思路技巧总结：
 * 1. 适用场景：
 *    - 需要动态维护一个序列，并支持按索引插入元素
 *    - 需要根据排名或索引快速查找元素
 *    - 需要处理涉及排名、位置相关的复杂约束问题
 * 
 * 2. 核心思想：
 *    - 利用AVL树等平衡二叉搜索树维护动态序列
 *    - 通过维护子树大小信息支持按排名查找和按索引插入
 *    - 将问题转化为在平衡树中进行插入操作
 * 
 * 3. 解题步骤：
 *    - 将输入数据按特定规则排序
 *    - 使用平衡树按顺序插入元素
 *    - 利用树的排名/索引特性满足约束条件
 * 
 * 4. 工程化考量：
 *    - 性能优化：使用平衡树避免O(n)的插入开销
 *    - 内存管理：合理分配和释放树节点
 *    - 边界处理：处理空树和单节点等特殊情况
 * 
 * 5. 时间和空间复杂度：
 *    - 排序：O(n log n)
 *    - 插入：O(n log n)
 *    - 查询：O(n log n)
 *    - 空间复杂度：O(n)
 * 
 * 6. 与其他算法的关联：
 *    - 与逆序对问题的关联：都可以用平衡树优化
 *    - 与树状数组/线段树的关联：都是处理动态序列的数据结构
 *    - 与分治算法的关联：都涉及将问题分解为更小子问题
 * 
 * 7. 语言特性差异：
 *    - Java: Collections.sort()和Arrays.sort()优化
 *    - C++: std::sort和自定义比较器
 *    - Python: sorted()和lambda表达式
 */

public class Code02_ReconstructionQueue {

	public static int[][] reconstructQueue(int[][] people) {
		Arrays.sort(people, (a, b) -> a[0] != b[0] ? (b[0] - a[0]) : (a[1] - b[1]));
		for (int[] p : people) {
			add(p[0], p[1]);
		}
		fill(people);
		clear();
		return people;
	}

	public static int MAXN = 2001;

	public static int cnt = 0;

	public static int head = 0;

	public static int[] key = new int[MAXN];

	public static int[] height = new int[MAXN];

	public static int[] left = new int[MAXN];

	public static int[] right = new int[MAXN];
	
	public static int[] value = new int[MAXN];

	public static int[] size = new int[MAXN];

	public static void up(int i) {
		size[i] = size[left[i]] + size[right[i]] + 1;
		height[i] = Math.max(height[left[i]], height[right[i]]) + 1;
	}

	public static int leftRotate(int i) {
		int r = right[i];
		right[i] = left[r];
		left[r] = i;
		up(i);
		up(r);
		return r;
	}

	public static int rightRotate(int i) {
		int l = left[i];
		left[i] = right[l];
		right[l] = i;
		up(i);
		up(l);
		return l;
	}

	public static int maintain(int i) {
		int lh = height[left[i]];
		int rh = height[right[i]];
		if (lh - rh > 1) {
			if (height[left[left[i]]] >= height[right[left[i]]]) {
				i = rightRotate(i);
			} else {
				left[i] = leftRotate(left[i]);
				i = rightRotate(i);
			}
		} else if (rh - lh > 1) {
			if (height[right[right[i]]] >= height[left[right[i]]]) {
				i = leftRotate(i);
			} else {
				right[i] = rightRotate(right[i]);
				i = leftRotate(i);
			}
		}
		return i;
	}

	public static void add(int num, int index) {
		head = add(head, index, num, index);
	}

	public static int add(int i, int rank, int num, int index) {
		if (i == 0) {
			key[++cnt] = num;
			value[cnt] = index;
			size[cnt] = height[cnt] = 1;
			return cnt;
		}
		if (size[left[i]] >= rank) {
			left[i] = add(left[i], rank, num, index);
		} else {
			right[i] = add(right[i], rank - size[left[i]] - 1, num, index);
		}
		up(i);
		return maintain(i);
	}

	public static void fill(int[][] ans) {
		fi = 0;
		inOrder(ans, head);
	}

	public static int fi;

	public static void inOrder(int[][] ans, int i) {
		if (i == 0) {
			return;
		}
		inOrder(ans, left[i]);
		ans[fi][0] = key[i];
		ans[fi++][1] = value[i];
		inOrder(ans, right[i]);
	}

	public static void clear() {
		Arrays.fill(key, 1, cnt + 1, 0);
		Arrays.fill(height, 1, cnt + 1, 0);
		Arrays.fill(left, 1, cnt + 1, 0);
		Arrays.fill(right, 1, cnt + 1, 0);
		Arrays.fill(value, 1, cnt + 1, 0);
		Arrays.fill(size, 1, cnt + 1, 0);
		cnt = 0;
		head = 0;
	}

}