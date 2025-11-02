package class156;

// 关押罪犯
// 一共有n个犯人，编号1 ~ n，一共有两个监狱，你可以决定每个犯人去哪个监狱
// 给定m条记录，每条记录 l r v，表示l号犯人和r号犯人的仇恨值
// 每个监狱的暴力值 = 该监狱中仇恨最深的犯人之间的仇恨值
// 冲突值 = max(第一座监狱的暴力值，第二座监狱的暴力值)
// 犯人的分配方案需要让这个冲突值最小，返回最小能是多少
// 1 <= n <= 20000    1 <= m <= 100000    1 <= 仇恨值 <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P1525
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * 带权并查集+二分解决关押罪犯问题
 * 
 * 问题分析：
 * 将n个犯人分配到两个监狱，使得两个监狱中最大的仇恨值最小
 * 
 * 核心思想：
 * 1. 二分答案，将问题转化为判定性问题
 * 2. 对于给定的最大仇恨值limit，判断是否能将犯人分配到两个监狱使得最大仇恨值不超过limit
 * 3. 对于仇恨值大于limit的犯人对，必须分配到不同监狱
 * 4. 使用扩展域并查集（种类并查集）处理敌对关系：
 *    - 对于每个犯人i，维护两个节点：i（在监狱A）和i+n（在监狱B）
 *    - 如果i和j必须在不同监狱，则合并(i,j+n)和(j,i+n)
 *    - 如果发现冲突（i和i+n在同一集合），说明limit太小
 * 
 * 另一种解法（贪心）：
 * 1. 按仇恨值从大到小排序
 * 2. 对于每对犯人，如果当前在同一监狱则产生冲突，否则尽量分配到不同监狱
 * 3. 使用扩展域并查集维护分配状态
 * 
 * 时间复杂度分析：
 * - 解法1（二分）: O(m * log(max_v) * α(n))
 * - 解法2（贪心）: O(m * log(m) + m * α(n))
 * 
 * 空间复杂度: O(n + m)
 * 
 * 应用场景：
 * - 二分图判定
 * - 敌对关系处理
 * - 最优化问题
 */
public class Code07_DetainCriminals {

	public static int MAXN = 20002;

	public static int MAXM = 100001;

	public static int n, m;

	// father[i] 表示节点i的父节点
	public static int[] father = new int[MAXN];

	// enemy[i] 表示i的敌人所在的节点
	public static int[] enemy = new int[MAXN];

	// arr[i] = {l, r, v} 表示第i条记录：l号犯人和r号犯人的仇恨值为v
	public static int[][] arr = new int[MAXM][3];

	/**
	 * 初始化并查集
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n)
	 */
	public static void prepare() {
		// 初始化每个节点为自己所在集合的代表
		for (int i = 1; i <= n; i++) {
			father[i] = i;
			// 初始时没有已知的敌人
			enemy[i] = 0;
		}
	}

	/**
	 * 查找节点i所在集合的代表
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param i 要查找的节点
	 * @return 节点i所在集合的根节点
	 */
	public static int find(int i) {
		// 路径压缩
		father[i] = father[i] == i ? i : find(father[i]);
		return father[i];
	}

	/**
	 * 合并两个集合
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧节点
	 * @param r 右侧节点
	 */
	public static void union(int l, int r) {
		father[find(l)] = find(r);
	}

	/**
	 * 判断两个节点是否在同一集合中
	 * 时间复杂度: O(α(n)) 近似O(1)
	 * 
	 * @param l 左侧节点
	 * @param r 右侧节点
	 * @return 如果在同一集合中返回true，否则返回false
	 */
	public static boolean same(int l, int r) {
		return find(l) == find(r);
	}

	/**
	 * 计算最小冲突值
	 * 使用贪心策略：按仇恨值从大到小处理，尽量将敌人分配到不同监狱
	 * 时间复杂度: O(m * log(m) + m * α(n))
	 * 
	 * @return 最小冲突值
	 */
	public static int compute() {
		// 按仇恨值从大到小排序
		Arrays.sort(arr, 1, m + 1, (a, b) -> b[2] - a[2]);
		int ans = 0;
		// 从仇恨值最大的开始处理
		for (int i = 1, l, r, v; i <= m; i++) {
			l = arr[i][0];
			r = arr[i][1];
			v = arr[i][2];
			// 如果两个犯人被分到同一监狱
			if (same(l, r)) {
				// 产生冲突，记录当前仇恨值作为答案
				ans = v;
				break;
			} else {
				// 尽量将两个犯人分配到不同监狱
				// 如果l还没有分配敌人
				if (enemy[l] == 0) {
					// 将r作为l的敌人
					enemy[l] = r;
				} else {
					// 将l的敌人和r合并到同一监狱
					union(enemy[l], r);
				}
				// 如果r还没有分配敌人
				if (enemy[r] == 0) {
					// 将l作为r的敌人
					enemy[r] = l;
				} else {
					// 将r的敌人和l合并到同一监狱
					union(l, enemy[r]);
				}
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
		m = (int) in.nval;
		prepare();
		// 读取m条记录
		for (int i = 1; i <= m; i++) {
			in.nextToken();
			arr[i][0] = (int) in.nval;
			in.nextToken();
			arr[i][1] = (int) in.nval;
			in.nextToken();
			arr[i][2] = (int) in.nval;
		}
		// 计算并输出最小冲突值
		out.println(compute());
		out.flush();
		out.close();
		br.close();
	}

}