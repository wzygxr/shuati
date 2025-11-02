package class155;

/**
 * 洛谷 P2409 Y的积木
 * 题目链接：https://www.luogu.com.cn/problem/P2409
 * 
 * 题目描述：
 * 一共有n个正数数组，给定每个数组的大小mi，以及每个数组的数字。
 * 每个数组必须选且只能选一个数字，就可以形成n个数字的挑选方案。
 * 所有这些方案中，有数字累加和第1小的方案、第2小的方案、第3小的方案...
 * 打印，累加和前k小的方案，各自的累加和，要求实现O(k * log k)的解。
 * 
 * 解题思路：
 * 使用可持久化左偏树（Persistent Leftist Tree）结合小根堆来解决K-th Smallest问题。
 * 
 * 核心思想：
 * 1. 首先对每个数组进行排序，确保每个数组内部有序
 * 2. 计算初始方案（每个数组选择最小元素）的累加和
 * 3. 对于每个数组，构建一个左偏树，表示从该数组中选择不同元素的增量
 * 4. 使用小根堆维护所有可能的方案，每次取出累加和最小的方案
 * 5. 对于取出的方案，生成新的可能方案并加入堆中
 * 
 * 算法步骤：
 * 1. 对每个数组排序
 * 2. 计算初始方案累加和（每个数组选最小元素）
 * 3. 为每个数组构建左偏树，表示选择不同元素的增量
 * 4. 将所有左偏树合并成一个大左偏树
 * 5. 使用小根堆维护所有可能方案
 * 6. 重复k次：从堆中取出最小方案，生成新方案并加入堆中
 * 
 * 时间复杂度：O(k * log k)
 * 空间复杂度：O(k)
 * 
 * 相关题目：
 * - Java实现：Code04_Blocks1.java
 * - C++实现：Code04_Blocks2.java
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_Blocks1 {

	public static int MAXN = 101;     // 最大数组数量
	public static int MAXM = 10001;   // 最大元素总数
	public static int MAXK = 10001;   // 最大k值
	public static int MAXT = 1000001; // 最大节点数
	public static int INF = 10000001; // 无穷大

	public static int n, k;

	// 所有数组的所有数字放在arr中
	public static int[] arr = new int[MAXM];
	// start[i] : 第i个数组的第一个数字在arr中的什么位置
	public static int[] start = new int[MAXN];
	// boundary[i] : 第i个数组的越界位置在arr中的什么位置
	public static int[] boundary = new int[MAXN];

	// 左偏树代表基于之前的某个方案，做出行动的可能性
	// 左偏树的头就代表这个最优行动，假设编号为h的节点是头
	// idx[h] : 最优行动来自哪个数组
	public static int[] idx = new int[MAXT];
	// jdx[h] : 最优行动要替换掉idx[h]数组中什么位置的数
	public static int[] jdx = new int[MAXT];
	// cost[h] : 基于之前的某个方案，最优行动会让累加和增加多少
	public static int[] cost = new int[MAXT];
	public static int[] left = new int[MAXT];
	public static int[] right = new int[MAXT];
	public static int[] dist = new int[MAXT];
	// pre[h] : 基于之前的某个方案，这个方案的累加和，标签信息
	public static int[] pre = new int[MAXT];
	public static int cnt = 0;

	// heap是经典的小根堆，放着所有版本左偏树的头
	// 哪个左偏树的头节点，所代表方案的累加和最小，谁就放在heap的顶部
	public static int[] heap = new int[MAXK];
	public static int heapSize = 0;

	// 收集答案
	public static int[] ans = new int[MAXK];

	/**
	 * 初始化一个左偏树节点
	 * @param i 数组编号
	 * @param j 数组中元素的位置
	 * @return 新节点编号
	 */
	public static int init(int i, int j) {
		idx[++cnt] = i;
		jdx[cnt] = j;
		// 计算替换该元素后累加和的增量
		cost[cnt] = j + 1 < boundary[i] ? (arr[j + 1] - arr[j]) : INF;
		left[cnt] = right[cnt] = dist[cnt] = 0;
		return cnt;
	}

	/**
	 * 克隆一个左偏树节点（可持久化关键操作）
	 * @param i 要克隆的节点编号
	 * @return 新节点编号
	 */
	public static int clone(int i) {
		idx[++cnt] = idx[i];
		jdx[cnt] = jdx[i];
		cost[cnt] = cost[i];
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
		// 维护小根堆性质（cost小的优先）
		if (cost[i] > cost[j]) {
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
	 * 弹出左偏树的堆顶元素
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
	 * 比较两个左偏树节点代表的方案的累加和大小
	 * @param i 第一个节点
	 * @param j 第二个节点
	 * @return i代表的方案累加和是否小于j代表的方案
	 */
	public static boolean compare(int i, int j) {
		return pre[i] + cost[i] < pre[j] + cost[j];
	}

	/**
	 * 向小根堆中添加元素
	 * @param i 要添加的元素
	 */
	public static void heapAdd(int i) {
		heap[++heapSize] = i;
		// 上浮操作维护堆性质
		int cur = heapSize, up = cur / 2, tmp;
		while (cur > 1 && compare(heap[cur], heap[up])) {
			tmp = heap[up];
			heap[up] = heap[cur];
			heap[cur] = tmp;
			cur = up;
			up = cur / 2;
		}
	}

	/**
	 * 从小根堆中弹出堆顶元素
	 * @return 堆顶元素
	 */
	public static int heapPop() {
		int ans = heap[1];
		// 将最后一个元素移到堆顶
		heap[1] = heap[heapSize--];
		// 下沉操作维护堆性质
		int cur = 1, l = cur * 2, r = l + 1, best, tmp;
		while (l <= heapSize) {
			// 找到左右子节点中较小的那个
			best = (r <= heapSize && compare(heap[r], heap[l])) ? r : l;
			// 比较父节点与子节点中的最小者
			best = compare(heap[best], heap[cur]) ? best : cur;
			if (best == cur) {
				break;
			}
			// 交换元素
			tmp = heap[best];
			heap[best] = heap[cur];
			heap[cur] = tmp;
			cur = best;
			l = cur * 2;
			r = l + 1;
		}
		return ans;
	}

	/**
	 * 计算前k小的累加和
	 */
	public static void compute() {
		// 计算初始方案的累加和（每个数组选最小元素）
		int first = 0;
		for (int i = 1; i <= n; i++) {
			Arrays.sort(arr, start[i], boundary[i]);
			first += arr[start[i]];
		}
		dist[0] = -1;
		
		// 为每个数组构建左偏树并合并
		int head = 0;
		for (int i = 1; i <= n; i++) {
			head = merge(head, init(i, start[i]));
		}
		
		// 设置初始方案的累加和
		pre[head] = first;
		ans[1] = first;
		heapAdd(head);
		
		// 生成前k小的方案
		for (int ansi = 2, h1, h2; ansi <= k; ansi++) {
			head = heapPop();
			// 当前方案的累加和
			ans[ansi] = pre[head] + cost[head];
			
			// 弹出当前方案的堆顶元素
			h1 = pop(head);
			if (h1 != 0) {
				// 保持前驱累加和不变
				pre[h1] = pre[head];
				heapAdd(h1);
			}
			
			// 生成新方案：在当前数组中选择下一个元素
			if (jdx[head] + 1 < boundary[idx[head]]) {
				h2 = merge(h1, init(idx[head], jdx[head] + 1));
				// 新方案的累加和
				pre[h2] = ans[ansi];
				heapAdd(h2);
			}
		}
	}

	/**
	 * 主函数
	 * 输入格式：
	 * 第一行包含两个整数n和k，分别表示数组数量和要找的前k小方案
	 * 接下来n行，每行首先包含一个整数mi，表示第i个数组的大小
	 * 然后包含mi个整数，表示第i个数组的元素
	 * 输出格式：
	 * 输出k个整数，表示前k小的累加和
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		k = (int) in.nval;
		for (int i = 1, m, ai = 0; i <= n; i++) {
			in.nextToken();
			m = (int) in.nval;
			start[i] = ai + 1;
			for (int j = 1; j <= m; j++) {
				in.nextToken();
				arr[++ai] = (int) in.nval;
			}
			boundary[i] = start[i] + m;
		}
		compute();
		for (int i = 1; i <= k; i++) {
			out.print(ans[i] + " ");
		}
		out.println();
		out.flush();
		out.close();
		br.close();
	}

}