package class155;

/**
 * 可持久化左偏树的实现
 * 
 * 问题描述：
 * 实现可持久化左偏树数据结构，支持以下操作：
 * 1. 在某个版本的堆中插入一个元素，生成新版本
 * 2. 合并两个版本的堆，生成新版本
 * 3. 弹出某个版本堆的堆顶元素，生成新版本
 * 
 * 解题思路：
 * 可持久化数据结构是一种可以保存历史版本的数据结构，对它进行修改时，
 * 不会破坏之前的版本，而是生成一个新的版本。
 * 
 * 核心思想：
 * 1. 使用函数式编程思想，每次修改只创建需要修改的节点，共享未修改的部分
 * 2. 通过clone操作复制节点，保持历史版本不变
 * 3. 使用merge操作合并两个左偏树
 * 4. 使用pop操作删除堆顶元素
 * 
 * 关键技术：
 * 1. 节点复制：只复制需要修改的节点，其他节点共享
 * 2. 版本管理：通过版本号管理不同的历史版本
 * 3. 左偏树合并：高效的堆合并操作
 * 
 * 时间复杂度分析：
 * - 插入操作: O(log n)
 * - 合并操作: O(log n)
 * - 弹出操作: O(log n)
 * 
 * 空间复杂度分析:
 * - 每次操作最多增加O(log n)个新节点
 * 
 * 相关题目：
 * - Java实现：Code03_PersistentLeftistTree1.java
 * - C++实现：Code03_PersistentLeftistTree2.java
 */

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Code03_PersistentLeftistTree1 {

	public static int MAXN = 10000;   // 最大版本数
	public static int MAXV = 100000;  // 最大值范围
	public static int MAXT = 2000001; // 最大节点数

	// 可持久化左偏树相关数组
	public static int[] rt = new int[MAXN];    // 每个版本的根节点
	public static int[] num = new int[MAXT];   // 节点权值
	public static int[] left = new int[MAXT];  // 左子节点
	public static int[] right = new int[MAXT]; // 右子节点
	public static int[] dist = new int[MAXT];  // 距离（空路径长度）
	public static int[] size = new int[MAXT];  // 子树大小
	public static int cnt = 0;                 // 节点计数器

	/**
	 * 初始化一个新节点
	 * @param v 节点权值
	 * @return 新节点编号
	 */
	public static int init(int v) {
		num[++cnt] = v;
		left[cnt] = right[cnt] = dist[cnt] = 0;
		return cnt;
	}

	/**
	 * 克隆一个节点（可持久化关键操作）
	 * @param i 要克隆的节点编号
	 * @return 新节点编号
	 */
	public static int clone(int i) {
		num[++cnt] = num[i];
		left[cnt] = left[i];
		right[cnt] = right[i];
		dist[cnt] = dist[i];
		return cnt;
	}

	/**
	 * 合并两个左偏树
	 * @param i 第一棵左偏树的根节点
	 * @param j 第二棵左偏树的根节点
	 * @return 合并后的左偏树根节点
	 */
	public static int merge(int i, int j) {
		if (i == 0 || j == 0) {
			return i + j;
		}
		int tmp;
		// 维护小根堆性质
		if (num[i] > num[j]) {
			tmp = i;
			i = j;
			j = tmp;
		}
		// 克隆根节点以保持历史版本不变
		int h = clone(i);
		// 递归合并右子树
		right[h] = merge(right[h], j);
		// 维护左偏性质
		if (dist[left[h]] < dist[right[h]]) {
			tmp = left[h];
			left[h] = right[h];
			right[h] = tmp;
		}
		// 更新距离
		dist[h] = dist[right[h]] + 1;
		return h;
	}

	/**
	 * 弹出堆顶元素
	 * @param i 左偏树根节点
	 * @return 弹出堆顶后的新根节点
	 */
	public static int pop(int i) {
		// 处理边界情况
		if (left[i] == 0 && right[i] == 0) {
			return 0;
		}
		if (left[i] == 0 || right[i] == 0) {
			// 克隆非空子树
			return clone(left[i] + right[i]);
		}
		// 合并非空的左右子树
		return merge(left[i], right[i]);
	}

	/**
	 * 可持久化左偏树，x版本加入数字y，生成最新的i版本
	 * @param x 原版本号
	 * @param y 要插入的数字
	 * @param i 新版本号
	 */
	public static void treeAdd(int x, int y, int i) {
		// 合并原版本的堆与新节点
		rt[i] = merge(rt[x], init(y));
		// 更新新版本堆的大小
		size[rt[i]] = size[rt[x]] + 1;
	}

	/**
	 * 可持久化左偏树，x版本与y版本合并，生成最新的i版本
	 * @param x 第一个版本号
	 * @param y 第二个版本号
	 * @param i 新版本号
	 */
	public static void treeMerge(int x, int y, int i) {
		// 处理边界情况
		if (rt[x] == 0 && rt[y] == 0) {
			rt[i] = 0;
		} else if (rt[x] == 0 || rt[y] == 0) {
			// 克隆非空堆的根节点
			rt[i] = clone(rt[x] + rt[y]);
		} else {
			// 合并两个堆
			rt[i] = merge(rt[x], rt[y]);
		}
		// 更新新版本堆的大小
		size[rt[i]] = size[rt[x]] + size[rt[y]];
	}

	/**
	 * 可持久化左偏树，x版本弹出顶部，生成最新的i版本
	 * @param x 原版本号
	 * @param i 新版本号
	 */
	public static void treePop(int x, int i) {
		// 处理空堆情况
		if (size[rt[x]] == 0) {
			rt[i] = 0;
		} else {
			// 弹出堆顶元素
			rt[i] = pop(rt[x]);
			// 更新新版本堆的大小
			size[rt[i]] = size[rt[x]] - 1;
		}
	}

	// 验证结构
	public static ArrayList<PriorityQueue<Integer>> verify = new ArrayList<>();

	/**
	 * 验证结构，x版本加入数字y，生成最新版本
	 * @param x 原版本号
	 * @param y 要插入的数字
	 */
	public static void verifyAdd(int x, int y) {
		PriorityQueue<Integer> pre = verify.get(x);
		ArrayList<Integer> tmp = new ArrayList<>();
		while (!pre.isEmpty()) {
			tmp.add(pre.poll());
		}
		PriorityQueue<Integer> cur = new PriorityQueue<>();
		for (int number : tmp) {
			pre.add(number);
			cur.add(number);
		}
		cur.add(y);
		verify.add(cur);
	}

	/**
	 * 验证结构，x版本与y版本合并，生成最新版本
	 * @param x 第一个版本号
	 * @param y 第二个版本号
	 */
	public static void verifyMerge(int x, int y) {
		PriorityQueue<Integer> h1 = verify.get(x);
		PriorityQueue<Integer> h2 = verify.get(y);
		ArrayList<Integer> tmp = new ArrayList<>();
		PriorityQueue<Integer> cur = new PriorityQueue<>();
		while (!h1.isEmpty()) {
			int number = h1.poll();
			tmp.add(number);
			cur.add(number);
		}
		for (int number : tmp) {
			h1.add(number);
		}
		tmp.clear();
		while (!h2.isEmpty()) {
			int number = h2.poll();
			tmp.add(number);
			cur.add(number);
		}
		for (int number : tmp) {
			h2.add(number);
		}
		verify.add(cur);
	}

	/**
	 * 验证结构，x版本弹出顶部，生成最新版本
	 * @param x 原版本号
	 */
	public static void verifyPop(int x) {
		PriorityQueue<Integer> pre = verify.get(x);
		PriorityQueue<Integer> cur = new PriorityQueue<>();
		if (pre.size() == 0) {
			verify.add(cur);
		} else {
			int top = pre.poll();
			ArrayList<Integer> tmp = new ArrayList<>();
			while (!pre.isEmpty()) {
				tmp.add(pre.poll());
			}
			for (int number : tmp) {
				pre.add(number);
				cur.add(number);
			}
			pre.add(top);
			verify.add(cur);
		}
	}

	/**
	 * 验证可持久化左偏树i版本的堆是否等于验证结构i版本的堆
	 * @param i 版本号
	 * @return 是否相等
	 */
	public static boolean check(int i) {
		int h1 = rt[i];
		PriorityQueue<Integer> h2 = verify.get(i);
		if (size[h1] != h2.size()) {
			return false;
		}
		boolean ans = true;
		ArrayList<Integer> tmp = new ArrayList<>();
		while (!h2.isEmpty()) {
			int o1 = num[h1];
			h1 = pop(h1);
			int o2 = h2.poll();
			tmp.add(o2);
			if (o1 != o2) {
				ans = false;
				break;
			}
		}
		for (int v : tmp) {
			h2.add(v);
		}
		return ans;
	}

	/**
	 * 主函数，使用对数器验证可持久化左偏树的正确性
	 * 测试操作：
	 * 1. 在某个版本的堆中插入一个元素，生成新版本
	 * 2. 合并两个版本的堆，生成新版本
	 * 3. 弹出某个版本堆的堆顶元素，生成新版本
	 */
	public static void main(String[] args) {
		System.out.println("测试开始");
		dist[0] = -1;
		rt[0] = size[0] = 0; // 可持久化左偏树生成0版本的堆
		verify.add(new PriorityQueue<>()); // 验证结构生成0版本的堆
		for (int i = 1, op, x, y; i < MAXN; i++) {
			// op == 1，x版本的堆里加入数字y，形成i号版本的堆
			// op == 2，x版本的堆和y版本的堆合并，形成i号版本的堆
			// op == 3，x版本的堆弹出堆顶，形成i号版本的堆
			op = i == 1 ? 1 : ((int) (Math.random() * 3) + 1);
			x = (int) (Math.random() * i);
			if (op == 1) {
				y = (int) (Math.random() * MAXV);
				treeAdd(x, y, i);
				verifyAdd(x, y);
			} else if (op == 2) {
				y = x;
				do {
					y = (int) (Math.random() * i);
				} while (y == x);
				// 保证x != y
				treeMerge(x, y, i);
				verifyMerge(x, y);
			} else {
				treePop(x, i);
				verifyPop(x);
			}
			// 检查最新版本的堆是否一样
			if (!check(i)) {
				System.out.println("出错了！");
			}
		}
		// 最后验证是否所有版本的堆都一样
		for (int i = 1; i < MAXN; i++) {
			if (!check(i)) {
				System.out.println("出错了！");
			}
		}
		System.out.println("测试结束");
	}

}