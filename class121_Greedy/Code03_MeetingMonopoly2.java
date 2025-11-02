package class090;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 会议必须独占时间段的最大会议数量（大数据量优化版）- 贪心算法
 * 
 * 题目描述：
 * 给定若干会议的开始、结束时间，你参加某个会议的期间，不能参加其他会议。
 * 返回你能参加的最大会议数量。
 * 
 * 特殊说明：
 * 与Code03_MeetingMonopoly1不同，本题的数据量较大：
 * - 会议数量达到10^6
 * - 会议开始、结束时间也是10^6规模
 * - 使用排序会超时，需要采用更优化的方法
 * 
 * 解题思路：
 * 1. 利用时间范围有限的特点，不使用排序
 * 2. 使用数组latest记录每个结束时间对应的最晚开始时间
 * 3. 按时间顺序遍历，贪心选择可参加的会议
 * 
 * 算法原理：
 * - latest[end]记录所有在end时间结束的会议中最晚的开始时间
 * - 按结束时间从小到大遍历，如果当前会议的最晚开始时间不早于上一个选择会议的结束时间，则选择该会议
 * 
 * 时间复杂度：O(n + m) - n为会议数，m为时间范围
 * 空间复杂度：O(m) - m为时间范围
 * 
 * 相关题目：
 * - 洛谷 P1803: https://www.luogu.com.cn/problem/P1803
 * - 大数据量下的贪心优化问题
 */
public class Code03_MeetingMonopoly2 {

	// 既是会议的规模，也是开始、结束时间的规模
	public static int MAXN = 1000001;

	/**
	 * latest[60] == 40
	 * 表示 : 结束时间是60的所有会议中，最晚开始的会议是40的时候开始
	 * 比如会议[1, 60]、[30, 60]、[40, 60]
	 * 这些会议都在60结束，但是最晚开始的会议是40开始
	 * 如果latest[60] == -1
	 * 表示没有任何会议在60结束
	 */
	public static int[] latest = new int[MAXN];

	public static int n;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			// 初始化latest数组
			for (int i = 0; i < MAXN; i++) {
				latest[i] = -1;
			}
			// 读取所有会议信息
			for (int i = 0, start, end; i < n; i++) {
				in.nextToken();
				start = (int) in.nval;
				in.nextToken();
				end = (int) in.nval;
				if (latest[end] == -1) {
					// 如果结束时间在end的会议之前没发现过
					// 现在发现了
					latest[end] = start;
				} else {
					// 如果结束时间在end的会议之前发现过
					// 记录最晚的开始时间
					latest[end] = Math.max(latest[end], start);
				}
			}
			out.println(compute());
		}
		out.flush();
		out.close();
		br.close();
	}

	/**
	 * 计算最大可参加会议数
	 * 
	 * @return 最大可参加会议数
	 */
	public static int compute() {
		int ans = 0;
		// 不排序
		// 根据时间遍历
		for (int cur = 0, end = 0; end < MAXN; end++) {
			// cur : cur之前不能再安排会议，因为安排会议的人来到了cur时刻
			// end是当前的结束时间
			// 所有以end结束的会议，最晚的开始时间是latest[end]
			// 如果cur <= latest[end]，那么说明可以安排当前以end结束的会议
			if (cur <= latest[end]) {
				ans++;
				cur = end; // 安排之后，目前安排会议的人来到end时刻
			}
		}
		return ans;
	}

}