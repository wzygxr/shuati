package class138;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

/**
 * 01分数规划问题 - 最优比率生成树（Desert King）
 * 题目来源：POJ 2728
 * 题目描述：有n个村庄，每个村庄由(x, y, z)表示，其中(x,y)是二维地图中的位置，z是海拔高度。
 * 任意两个村庄之间的距离是二维地图中的欧式距离，修路花费是海拔差值的绝对值。
 * 要求将所有村庄连通，使得总花费/总距离的比值最小，结果保留小数点后3位。
 * 
 * 数据范围：
 * 2 <= n <= 10^3
 * 0 <= x、y <= 10^4
 * 0 <= z <= 10^7
 * 测试链接：http://poj.org/problem?id=2728
 * 注：提交代码时请将类名改为"Main"，以通过在线评测。
 * 
 * 算法思路：使用二分法求解01分数规划问题，结合Prim算法求最小生成树进行可行性判断
 * 时间复杂度：O(n^2 * log(1/ε))，其中ε是精度要求
 * 空间复杂度：O(n^2)
 * 
 * 01分数规划的数学原理：
 * 我们需要最小化 R = (sum(cost_e)) / (sum(dist_e))，其中e是生成树中的边
 * 对于给定的L，判断是否存在生成树使得 R < L
 * 等价于：sum(cost_e) < L * sum(dist_e)
 * 等价于：sum(cost_e - L * dist_e) < 0
 * 我们通过二分L的值，使用Prim算法计算最小生成树的权值和来判断是否可行
 * 如果最小生成树的权值和 < 0，则说明L可行，可以尝试更小的值
 */
public class Code03_DesertKing {

	/**
	 * 常量定义
	 */
	public static final int MAXN = 1001; // 最大村庄数量
	public static final double PRECISION = 1e-6; // 精度控制，用于二分结束条件

	/**
	 * 数据存储数组
	 */
	public static int[] x = new int[MAXN]; // 村庄的x坐标
	public static int[] y = new int[MAXN]; // 村庄的y坐标
	public static int[] z = new int[MAXN]; // 村庄的海拔高度
	public static double[][] dist = new double[MAXN][MAXN]; // 任意两村庄间的欧式距离
	public static double[][] cost = new double[MAXN][MAXN]; // 任意两村庄间的修路花费（海拔差绝对值）

	/**
	 * Prim算法所需的辅助数组
	 */
	public static boolean[] visit = new boolean[MAXN]; // 标记村庄是否已加入生成树
	public static double[] value = new double[MAXN]; // 存储每个村庄到生成树的最小边权（cost_e - L * dist_e）

	/**
	 * 村庄数量
	 */
	public static int n;

	/**
	 * 邻接矩阵结构下的Prim算法，从节点1出发计算最小生成树的权值和
	 * 边权为 cost_e - L * dist_e，用于01分数规划的可行性判断
	 * 
	 * @param L 当前尝试的比率值
	 * @return 最小生成树的权值和
	 */
	public static double prim(double L) {
		// 初始化visit数组和value数组
		for (int i = 1; i <= n; i++) {
			visit[i] = false;
			value[i] = cost[1][i] - L * dist[1][i]; // 初始化为从节点1出发的边权
		}
		
		visit[1] = true; // 标记节点1已加入生成树
		double totalWeight = 0; // 用于存储最小生成树的权值和
		
		// 最小生成树一定有n-1条边，需要进行n-1轮选择
		for (int i = 1; i <= n - 1; i++) {
			// 在未加入生成树的节点中，找到到生成树点集距离最小的点
			double minEdge = Double.MAX_VALUE;
			int nextNode = 0;
			for (int j = 1; j <= n; j++) {
				if (!visit[j] && value[j] < minEdge) {
					minEdge = value[j];
					nextNode = j;
				}
			}
			
			// 将选中的边加入生成树，更新总和
			totalWeight += minEdge;
			visit[nextNode] = true; // 标记新节点已加入生成树
			
			// 查看新加入的节点能否更新其他未加入节点到生成树的最小距离
			for (int j = 1; j <= n; j++) {
				if (!visit[j]) {
					double newEdgeValue = cost[nextNode][j] - L * dist[nextNode][j];
					if (value[j] > newEdgeValue) {
						value[j] = newEdgeValue;
					}
				}
			}
		}
		
		return totalWeight; // 返回最小生成树的权值和
	}

	/**
	 * 主函数：处理输入输出，执行二分查找
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 输入输出流设置，提高效率
		BufferedReader br = null;
		StreamTokenizer in = null;
		PrintWriter out = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(System.in));
			in = new StreamTokenizer(br);
			out = new PrintWriter(new OutputStreamWriter(System.out));
			
			// 读取村庄数量
			in.nextToken();
			n = (int) in.nval;
			
			// 处理多组测试用例，直到输入0
			while (n != 0) {
				// 读取每个村庄的坐标和海拔
				for (int i = 1; i <= n; i++) {
					in.nextToken();
					x[i] = (int) in.nval;
					in.nextToken();
					y[i] = (int) in.nval;
					in.nextToken();
					z[i] = (int) in.nval;
				}
				
				// 预处理计算任意两个村庄之间的距离和花费
				for (int i = 1; i <= n; i++) {
					for (int j = 1; j <= n; j++) {
						if (i != j) {
							// 计算欧式距离
							double dx = x[i] - x[j];
							double dy = y[i] - y[j];
							dist[i][j] = Math.sqrt(dx * dx + dy * dy);
							
							// 计算海拔差绝对值作为花费
							cost[i][j] = Math.abs(z[i] - z[j]);
						}
					}
				}
				
				// 初始化二分查找的左右边界
				// 左边界为0，右边界可以根据数据范围估算
				double left = 0.0, right = 100.0; // 最大可能的比值，根据数据范围设置
				double result = 0.0;
				
				// 二分查找过程
				// 当左右边界的差大于精度要求时继续循环
				while (left < right && right - left >= PRECISION) {
					double mid = (left + right) / 2.0;
					
					// 调用Prim算法计算当前比率下的最小生成树权值和
					// 如果权值和 <= 0，说明可以找到更小的比值，调整右边界
					if (prim(mid) <= 0) {
						result = mid;
						right = mid - PRECISION;
					} else {
						// 否则调整左边界
						left = mid + PRECISION;
					}
				}
				
				// 输出结果，保留3位小数
				out.printf("%.3f\n", result);
				
				// 读取下一组测试用例的村庄数量
				in.nextToken();
				n = (int) in.nval;
			}
		} finally {
			// 确保资源正确关闭
			if (out != null) {
				out.flush();
				out.close();
			}
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// 忽略关闭时的异常
				}
			}
		}
	}

}
