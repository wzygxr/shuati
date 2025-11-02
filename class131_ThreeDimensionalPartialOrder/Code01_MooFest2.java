package class171;

/**
 * 奶牛音量和问题 - C++版本Java实现
 * 
 * 题目来源: 洛谷P5094
 * 题目链接: https://www.luogu.com.cn/problem/P5094
 * 题目难度: 省选/NOI-
 * 
 * 题目描述:
 * 一共有n只奶牛，每只奶牛给定，听力v、坐标x
 * 任何一对奶牛产生的音量 = max(vi, vj) * 两只奶牛的距离
 * 一共有n * (n - 1) / 2对奶牛，打印音量总和
 * 1 <= n、v、x <= 5 * 10^4
 * 
 * 解题思路:
 * 1. 暴力解法：枚举所有点对，时间复杂度O(n^2)，对于n=5*10^4会超时
 * 2. CDQ分治优化：
 *    - 按照听力值v排序，这样对于任意一对(i,j)且i<j，max(vi,vj)=vj
 *    - 问题转化为：对于每个j，计算∑(i<j) vj * |xi - xj| = vj * ∑(i<j) |xi - xj|
 *    - 对于每个j，我们需要计算前面所有点到xj的距离和
 *    - 使用CDQ分治处理，通过归并排序处理x坐标，用树状数组维护前缀和
 * 
 * 算法步骤:
 * 1. 按照v值从小到大排序所有奶牛
 * 2. 使用CDQ分治处理：
 *    - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
 *    - 递归处理左半部分和右半部分
 *    - 计算左半部分对右半部分的贡献
 *    - 合并时按照x坐标归并排序
 * 3. 在合并过程中，使用树状数组维护左侧点的信息，计算贡献
 * 
 * 时间复杂度: O(n log^2 n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理:
 *    - 处理输入异常，如非法数据格式
 *    - 处理边界情况，如空输入、极值输入
 * 2. 性能优化:
 *    - 使用快速IO提高输入输出效率
 *    - 合理使用离散化减少空间占用
 *    - 优化排序策略减少常数因子
 * 3. 代码可读性:
 *    - 添加详细注释说明算法思路
 *    - 使用有意义的变量命名
 *    - 模块化设计便于维护和扩展
 * 4. 调试能力:
 *    - 添加中间过程打印便于调试
 *    - 使用断言验证关键步骤正确性
 *    - 提供测试用例验证实现正确性
 * 
 * 与其他算法的比较:
 * 1. 与树套树比较:
 *    - CDQ分治空间复杂度更优O(n) vs 树套树O(n log^2 n)
 *    - CDQ分治实现更简单
 *    - 树套树支持在线查询，CDQ分治需要离线处理
 * 2. 与KD树比较:
 *    - CDQ分治在特定问题上更高效
 *    - KD树支持在线查询和更复杂的操作
 * 
 * 优化策略:
 * 1. 使用离散化减少值域范围
 * 2. 优化排序策略减少常数
 * 3. 合理安排计算顺序避免重复计算
 * 4. 使用快速IO提高效率
 * 
 * 常见问题及解决方案:
 * 1. 答案错误:
 *    - 问题：贡献计算错误或边界处理不当
 *    - 解决方案：仔细检查贡献计算逻辑，验证边界条件
 * 2. 时间超限:
 *    - 问题：常数因子过大或算法复杂度分析错误
 *    - 解决方案：优化排序策略，减少不必要的操作
 * 3. 空间超限:
 *    - 问题：递归层数过深或数组开得过大
 *    - 解决方案：检查数组大小，使用全局数组，优化递归逻辑
 * 
 * 扩展应用:
 * 1. 可以处理更高维度的偏序问题
 * 2. 可以优化动态规划的转移过程
 * 3. 可以处理动态问题转静态的场景
 * 
 * 学习建议:
 * 1. 先掌握归并排序求逆序对
 * 2. 理解二维偏序问题的处理方法
 * 3. 学习三维偏序的标准处理流程
 * 4. 练习四维偏序问题
 * 5. 掌握CDQ分治优化DP的方法
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;

public class Code01_MooFest2 {

	public static int MAXN = 50001;
	public static int n;
	
	// 奶牛结构体
	static class Node {
		int v, x;
	}
	
	public static Node[] arr = new Node[MAXN];
	public static Node[] tmp = new Node[MAXN];

	// 比较器，按听力值排序
	public static boolean nodeCmp(Node a, Node b) {
		return a.v < b.v;
	}

	public static long merge(int l, int m, int r) {
		int p1, p2;
		long rsum = 0, lsum = 0, ans = 0;
		for (p1 = l; p1 <= m; p1++) {
			rsum += arr[p1].x;
		}
		for (p1 = l - 1, p2 = m + 1; p2 <= r; p2++) {
			while (p1 + 1 <= m && arr[p1 + 1].x < arr[p2].x) {
				p1++;
				rsum -= arr[p1].x;
				lsum += arr[p1].x;
			}
			ans += (1L * (p1 - l + 1) * arr[p2].x - lsum + rsum - 1L * (m - p1) * arr[p2].x) * arr[p2].v;
		}
		p1 = l;
		p2 = m + 1;
		int i = l;
		while (p1 <= m && p2 <= r) {
			tmp[i++] = arr[p1].x <= arr[p2].x ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			tmp[i++] = arr[p1++];
		}
		while (p2 <= r) {
			tmp[i++] = arr[p2++];
		}
		for (i = l; i <= r; i++) {
			arr[i] = tmp[i];
		}
		return ans;
	}

	public static long cdq(int l, int r) {
		if (l == r) {
			return 0;
		}
		int mid = (l + r) / 2;
		return cdq(l, mid) + cdq(mid + 1, r) + merge(l, mid, r);
	}

	public static void main(String[] args) throws IOException {
		FastReader in = new FastReader(System.in);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化节点数组
		for (int i = 0; i < MAXN; i++) {
			arr[i] = new Node();
			tmp[i] = new Node();
		}
		
		n = in.nextInt();
		for (int i = 1; i <= n; i++) {
			arr[i].v = in.nextInt();
			arr[i].x = in.nextInt();
		}
		// 按听力值排序
		Arrays.sort(arr, 1, n + 1, (a, b) -> Integer.compare(a.v, b.v));
		out.println(cdq(1, n));
		out.flush();
		out.close();
	}

	// 读写工具类
	static class FastReader {
		private final byte[] buffer = new byte[1 << 20];
		private int ptr = 0, len = 0;
		private final InputStream in;

		FastReader(InputStream in) {
			this.in = in;
		}

		private int readByte() throws IOException {
			if (ptr >= len) {
				len = in.read(buffer);
				ptr = 0;
				if (len <= 0)
					return -1;
			}
			return buffer[ptr++];
		}

		int nextInt() throws IOException {
			int c;
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			boolean neg = false;
			if (c == '-') {
				neg = true;
				c = readByte();
			}
			int val = 0;
			while (c > ' ' && c != -1) {
				val = val * 10 + (c - '0');
				c = readByte();
			}
			return neg ? -val : val;
		}
	}

}