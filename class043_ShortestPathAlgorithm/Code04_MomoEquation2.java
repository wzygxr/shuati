package class143;

// 墨墨的等式(两次转圈法)
// 一共有n种正数，每种数可以选择任意个，个数不能是负数
// 那么一定有某些数值可以由这些数字累加得到
// 请问在[l...r]范围上，有多少个数能被累加得到
// 0 <= n <= 12
// 0 <= 数值范围 <= 5 * 10^5
// 1 <= l <= r <= 10^12
// 测试链接 : https://www.luogu.com.cn/problem/P2371
// 
// 算法思路：
// 1. 这是一个典型的同余最短路问题，使用"两次转圈法"优化
// 2. 首先对输入的数字进行排序，并去除0值
// 3. 选择最小的正数作为模数x，构建模x意义下的同余类图
// 4. 对于其他数字v[i]，在图中添加边：对于每个余数j，从j向(j+v[i])%x连一条长度为v[i]的边
// 5. 使用最短路算法计算从0到每个余数的最短距离dist[i]
// 6. 对于每个余数i，如果dist[i] <= r，则在该同余类中，能构成的数字个数为max(0, (r-dist[i])/x + 1)
// 7. 使用前缀和思想，通过计算[1,r]和[1,l-1]的答案差值得到[l,r]区间内的答案
//
// 时间复杂度：O(x * Σ(v[i]/gcd(v[i],x)))，其中x是最小的正数
// 空间复杂度：O(x)
//
// 相关题目链接：
// 1. 洛谷 P2371 [国家集训队]墨墨的等式 - https://www.luogu.com.cn/problem/P2371
// 2. 洛谷 P3403 跳楼机 - https://www.luogu.com.cn/problem/P3403
// 3. AtCoder Regular Contest 084 D - Small Multiple - https://atcoder.jp/contests/arc084/tasks/arc084_b
// 4. 洛谷 P2662 牛场围栏 - https://www.luogu.com.cn/problem/P2662
// 5. HDU 6071 Lazy Running - https://acm.hdu.edu.cn/showproblem.php?pid=6071
// 6. LeetCode 743. 网络延迟时间 - https://leetcode.cn/problems/network-delay-time/
// 7. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
// 8. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
// 9. POJ 3403 跳楼机 - http://poj.org/problem?id=3403
// 10. POJ 2662 牛场围栏 - http://poj.org/problem?id=2662
// 11. Codeforces 241E Flights - https://codeforces.com/problemset/problem/241/E
// 12. ZOJ 3403 跳楼机 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367903
// 13. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522
// 14. SPOJ KPEQU - https://www.spoj.com/problems/KPEQU/
// 15. 51Nod 1350 斐波那契表示 - https://www.51nod.com/Challenge/Problem.html#problemId=1350
// 
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code04_MomoEquation2 {

	public static int MAXN = 500001;

	public static long inf = Long.MAX_VALUE;

	public static int[] v = new int[MAXN];

	public static long[] dist = new long[MAXN];

	public static int n, x;

	public static long l, r;

	// 求两个数的最大公约数
	public static int gcd(int a, int b) {
		return b == 0 ? a : gcd(b, a % b);
	}

	// 主计算函数
	public static long compute() {
		// 对输入的数字进行排序
		Arrays.sort(v, 1, n + 1);
		int size = 0;
		// 去除0值，因为0不影响结果
		for (int i = 1; i <= n; i++) {
			if (v[i] != 0) {
				v[++size] = v[i];
			}
		}
		// 如果所有数字都是0，则无法构成任何正数
		if (size == 0) {
			return 0;
		}
		// 选择最小的正数作为模数
		x = v[1];
		// 初始化距离数组为无穷大
		Arrays.fill(dist, 0, x, inf);
		// 从0开始的距离为0
		dist[0] = 0;
		// 对于除最小数外的其他数，更新最短路
		for (int i = 2, d; i <= size; i++) { // 出现基准数之外的其他数，更新最短路
			d = gcd(v[i], x); // 求最大公约数
			// 构建同余类图，每个子环代表一个同余类
			for (int j = 0; j < d; j++) { // j是每个子环的起点
				// 两次转圈法：每个节点访问两次确保最短路正确计算
				for (int cur = j, next, circle = 0; circle < 2; circle += cur == j ? 1 : 0) {
					next = (cur + v[i]) % x;
					// 如果当前节点可达，则更新下一个节点的最短距离
					if (dist[cur] != inf) {
						dist[next] = Math.min(dist[next], dist[cur] + v[i]);
					}
					cur = next;
				}
			}
		}
		long ans = 0;
		// 计算答案：对于每个余数，统计在[l,r]范围内能构成的数字个数
		for (int i = 0; i < x; i++) {
			// 计算[1,r]范围内的答案
			if (r >= dist[i]) {
				ans += Math.max(0, (r - dist[i]) / x + 1);
			}
			// 减去[1,l-1]范围内的答案，得到[l,r]范围内的答案
			if (l >= dist[i]) {
				ans -= Math.max(0, (l - dist[i]) / x + 1);
			}
		}
		return ans;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		l = (long) in.nval - 1;
		in.nextToken();
		r = (long) in.nval;
		for (int i = 1; i <= n; i++) {
			in.nextToken();
			v[i] = (int) in.nval;
		}
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

}