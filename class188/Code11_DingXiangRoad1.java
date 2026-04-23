// 丁香之路，java版
// 一共有n个点，点i和点j的距离为|i - j|，所以这是完全图，边是无向边
// 给定m个点对，每个点对(a, b)，保证 a != b，表示a和b之间的无向边做了标记
// 给定起点s，不管终点是什么，每条被标记的无向边，走过至少一次
// 打印起点s，终点i的情况下，走过路程的最小距离是多少
// 终点i可以是1~n中的任意一点，所以一共打印n个数值
// 1 <= n <= 2500
// m <= n个顶点的完全图的边数
// 测试链接 : https://www.luogu.com.cn/problem/P6628
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

public class Code11_DingXiangRoad1 {

	// 定义最大节点数量常量，用于数组初始化
	// 笔试面试要点：根据题目数据范围合理设置数组大小，防止越界
	// ML/DL关联：在图神经网络中，节点数量决定了模型输入维度
	public static int MAXN = 3001;

	// 定义最大边数常量，用于数组初始化
	// 笔试面试要点：根据题目数据范围合理设置数组大小，防止越界
	// ML/DL关联：在图神经网络中，边数量影响了邻接矩阵的稀疏性
	public static int MAXM = 3000001;

	// 全局变量：节点数、边数、起点
	// 笔试面试要点：全局变量便于函数间共享数据
	// ML/DL关联：全局参数类似模型的超参数
	public static int n, m, s;

	// 存储边的起点和终点数组
	// a[i]表示第i条边的起点，b[i]表示第i条边的终点
	// 笔试面试要点：边的存储方式，便于后续处理
	// ML/DL关联：图的边列表表示法
	public static int[] a = new int[MAXM];
	public static int[] b = new int[MAXM];

	// 标记数组，记录某个点是否被固定（即作为起点或在某条被标记的边上）
	// 笔试面试要点：布尔数组用于快速标记和查询
	// ML/DL关联：掩码矩阵在注意力机制中的应用
	public static boolean[] fix = new boolean[MAXN];

	// 使用TreeSet存储需要处理的节点集合，自动排序
	// 笔试面试要点：TreeSet提供O(logn)的插入和查询，且自动排序
	// ML/DL关联：有序数据结构在特征工程中的应用
	public static TreeSet<Integer> nodeSet = new TreeSet<>();

	// 节点数组，存储需要处理的节点
	// 笔试面试要点：将TreeSet中的元素复制到数组便于索引访问
	// ML/DL关联：数据从集合类型转换为张量类型
	public static int[] nodeArr = new int[MAXN];

	// 计数器，记录节点数组中的元素数量
	// 笔试面试要点：用于记录有效元素个数
	// ML/DL关联：批量处理时的有效样本计数
	public static int cnt;

	// 并查集数组fa1，用于初始连通性判断
	// 笔试面试要点：并查集用于动态连通性问题
	// ML/DL关联：聚类算法中的连通组件识别
	public static int[] fa1 = new int[MAXN];

	// 度数数组deg1，记录每个节点的度数
	// 笔试面试要点：度数信息用于欧拉路径/回路的判断
	// ML/DL关联：节点度数作为图神经网络的节点特征
	public static int[] deg1 = new int[MAXN];

	// 并查集数组fa2，用于后续处理
	// 笔试面试要点：双重并查集结构，用于不同阶段的连通性判断
	// ML/DL关联：多层图神经网络中的层次化连接
	public static int[] fa2 = new int[MAXN];

	// 度数数组deg2，用于后续处理
	// 笔试面试要点：修改后的度数信息，用于最终计算
	// ML/DL关联：节点特征在不同层间的传递
	public static int[] deg2 = new int[MAXN];

	// 边数组，存储边的起点、终点和权重信息 [u, v, w]
	// 笔试面试要点：边的三元组表示，便于排序和处理
	// ML/DL关联：图的边特征表示
	public static int[][] edgeArr = new int[MAXN][3];

	// 总距离和，记录所有被标记边的总长度
	// 笔试面试要点：累计值用于最终结果计算
	// ML/DL关联：损失函数的累积计算
	public static long sum;

	// 边比较器类，用于按权重、起点、终点排序
	// 功能：实现Kruskal算法中边的排序
	// 面试考点：自定义比较器的实现
	// 边界条件：考虑权重相同时的处理
	// ML/DL关联：排序机制在注意力权重计算中的应用
	public static class EdgeCmp implements Comparator<int[]> {
		@Override
		// 比较两条边的大小关系
		// 参数：e1和e2是要比较的两条边
		public int compare(int[] e1, int[] e2) {
			// 首先比较边的权重
			if (e1[2] != e2[2]) {
				return e1[2] - e2[2];
			}
			// 如果权重相同，比较起点
			if (e1[0] != e2[0]) {
				return e1[0] - e2[0];
			}
			// 如果起点也相同，比较终点
			return e1[1] - e2[1];
		}
	}

	// 计算两点间距离的函数
	// 功能：计算点x和点y之间的距离，即|x-y|
	// 面试考点：绝对值函数的应用
	// 边界条件：无需特殊处理，Math.abs函数处理负数
	// ML/DL关联：距离度量在相似性计算中的应用
	public static int dist(int x, int y) {
		// 返回x和y之间的绝对值距离
		return Math.abs(x - y);
	}

	// 并查集的查找函数，带路径压缩
	// 功能：查找节点x所在的连通分量代表元
	// 面试考点：并查集的实现及路径压缩优化
	// 边界条件：当x本身就是根节点时直接返回
	// ML/DL关联：连通性分析在社区发现中的应用
	public static int find(int[] fa, int x) {
		// 如果x不是根节点，则递归查找并更新父节点（路径压缩）
		if (x != fa[x]) {
			// 递归查找根节点并进行路径压缩
			fa[x] = find(fa, fa[x]);
		}
		// 返回x所在集合的代表元
		return fa[x];
	}

	// 并查集的合并函数
	// 功能：将节点x和节点y所在的集合合并
	// 面试考点：并查集的合并操作
	// 边界条件：若x和y已在同一集合则无需操作
	// ML/DL关联：聚类算法中的合并策略
	public static void union(int[] fa, int x, int y) {
		// 查找x和y所在集合的代表元
		int fx = find(fa, x);
		int fy = find(fa, y);
		// 如果不在同一集合则合并
		if (fx != fy) {
			// 将fx的父节点设为fy，实现两集合合并
			fa[fx] = fy;
		}
	}

	// Kruskal算法求最小生成树
	// 功能：计算最小生成树的权重和
	// 面试考点：最小生成树算法的实现
	// 边界条件：空图或单点图的情况
	// ML/DL关联：图结构学习中的最小连接模式
	public static long kruskal() {
		// 最小生成树的总权重
		long cost = 0;
		// 实际边的数量
		int edgeCnt = cnt - 1;
		// 对边按权重排序
		Arrays.sort(edgeArr, 1, edgeCnt + 1, new EdgeCmp());
		// 遍历所有边
		for (int i = 1; i <= edgeCnt; i++) {
			// 获取边的起点、终点和权重
			int u = edgeArr[i][0];
			int v = edgeArr[i][1];
			int w = edgeArr[i][2];
			// 如果两端点不在同一连通分量则加入MST
			if (find(fa2, u) != find(fa2, v)) {
				// 加上该边的权重（由于是往返，所以乘以2）
				cost += w * 2;
				// 合并两个连通分量
				union(fa2, u, v);
			}
		}
		// 返回最小生成树的总权重
		return cost;
	}

	// 计算从start到end的最小路径长度
	// 功能：计算在给定起点和终点情况下，遍历所有标记边的最短路径
	// 面试考点：图论中路径规划算法的综合应用
	// 边界条件：起点和终点相同的情况
	// ML/DL关联：路径规划在强化学习中的应用
	public static long compute(int start, int end) {
		// 重置节点数组计数器
		cnt = 0;
		// 将TreeSet中的节点复制到数组中
		for (int x : nodeSet) {
			// 将节点x加入数组
			nodeArr[++cnt] = x;
		}
		// 复制初始的连通性和度数信息
		for (int i = 1; i <= n; i++) {
			// 复制并查集信息
			fa2[i] = find(fa1, i);
			// 复制度数信息
			deg2[i] = deg1[i];
		}
		// 初始化结果为所有标记边的总长度
		long ans = sum;
		// 起点度数加1（因为需要额外经过一次）
		deg2[start]++;
		// 终点度数加1（因为需要额外经过一次）
		deg2[end]++;
		// 将起点和终点合并到同一连通分量
		union(fa2, start, end);
		// 处理奇度数节点的配对
		for (int i = 1, p = 0; i <= cnt; i++) {
			// 如果当前节点度数为奇数
			if ((deg2[nodeArr[i]] & 1) == 1) {
				// 如果还没有找到配对的第一个节点
				if (p == 0) {
					// 记录当前节点为第一个奇度数节点
					p = i;
				} else {
					// 将两个奇度数节点配对，加上它们之间的距离
					ans += dist(nodeArr[p], nodeArr[i]);
					// 连接这两个节点以及它们之间所有节点到同一连通分量
					while (p < i) {
						union(fa2, nodeArr[p++], nodeArr[i]);
					}
					// 重置配对指针
					p = 0;
				}
			}
		}
		// 构建边数组，连接相邻节点
		for (int i = 1; i <= cnt - 1; i++) {
			// 设置边的起点
			edgeArr[i][0] = nodeArr[i];
			// 设置边的终点
			edgeArr[i][1] = nodeArr[i + 1];
			// 设置边的权重（相邻节点间的距离）
			edgeArr[i][2] = nodeArr[i + 1] - nodeArr[i];
		}
		// 加上最小生成树的权重
		ans += kruskal();
		// 返回最终结果
		return ans;
	}

	// 预处理函数
	// 功能：初始化数据结构，统计度数和连通性
	// 面试考点：图的预处理步骤
	// 边界条件：空图或单点图的情况
	// ML/DL关联：图数据的预处理步骤
	public static void prepare() {
		// 将起点标记为固定点
		fix[s] = true;
		// 将起点加入节点集合
		nodeSet.add(s);
		// 初始化总距离和
		sum = 0;
		// 初始化并查集，每个节点的父节点是自己
		for (int i = 1; i <= n; i++) {
			fa1[i] = i;
		}
		// 遍历所有标记的边
		for (int i = 1, u, v; i <= m; i++) {
			// 获取边的起点和终点
			u = a[i];
			v = b[i];
			// 起点度数加1
			deg1[u]++;
			// 终点度数加1
			deg1[v]++;
			// 将起点和终点标记为固定点
			fix[u] = true;
			fix[v] = true;
			// 将起点和终点加入节点集合
			nodeSet.add(u);
			nodeSet.add(v);
			// 合并起点和终点所在的连通分量
			union(fa1, u, v);
			// 累加边的长度到总距离和
			sum += dist(u, v);
		}
	}

	// 主函数：程序入口
	// 功能：读取输入、处理数据并输出结果
	// 面试考点：完整的程序框架
	// 边界条件：各种输入规模的处理
	// ML/DL关联：模型推理的完整流程
	public static void main(String[] args) throws Exception {
		// 创建快速读入对象
		FastReader in = new FastReader(System.in);
		// 创建输出流对象
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		// 读取节点数
		n = in.nextInt();
		// 读取边数
		m = in.nextInt();
		// 读取起点
		s = in.nextInt();
		// 读取所有标记的边
		for (int i = 1; i <= m; i++) {
			// 读取边的起点
			a[i] = in.nextInt();
			// 读取边的终点
			b[i] = in.nextInt();
		}
		// 预处理数据
		prepare();
		// 对于每个可能的终点，计算最短路径
		for (int i = 1; i <= n; i++) {
			// 如果当前节点不是固定点，临时加入节点集合
			if (!fix[i]) {
				nodeSet.add(i);
			}
			// 计算从起点s到终点i的最短路径长度
			out.print(compute(s, i) + " ");
			// 如果当前节点不是固定点，从节点集合中移除
			if (!fix[i]) {
				nodeSet.remove(i);
			}
		}
		// 输出换行
		out.println();
		// 刷新输出缓冲区
		out.flush();
		// 关闭输出流
		out.close();
	}

	// 读写工具类
	// 功能：提供快速输入输出功能，提高程序效率
	// 面试考点：IO优化技巧
	// 边界条件：处理各种输入格式
	// ML/DL关联：高效数据加载器的实现
	static class FastReader {
		// 缓冲区大小为65536字节
		private final byte[] buffer = new byte[1 << 16];
		// 当前读取位置和有效数据长度
		private int ptr = 0, len = 0;
		// 输入流
		private final InputStream in;

		// 构造函数
		FastReader(InputStream in) {
			// 初始化输入流
			this.in = in;
		}

		// 读取单个字节
		// 功能：从输入流读取下一个字节
		// 面试考点：底层IO操作的理解
		// 边界条件：缓冲区满或输入结束
		// ML/DL关联：底层数据读取的优化
		private int readByte() throws IOException {
			// 如果当前读取位置超出缓冲区有效长度
			if (ptr >= len) {
				// 从输入流读取新的数据到缓冲区
				len = in.read(buffer);
				// 重置读取位置
				ptr = 0;
				// 如果没有更多数据可读，返回-1
				if (len <= 0)
					return -1;
			}
			// 返回当前字节并移动读取位置
			return buffer[ptr++];
		}

		// 读取整数
		// 功能：从输入流读取下一个整数
		// 面试考点：字符串转整数的实现
		// 边界条件：负数、零、超大数的处理
		// ML/DL关联：数据预处理中的类型转换
		int nextInt() throws IOException {
			int c;
			// 跳过空白字符
			do {
				c = readByte();
			} while (c <= ' ' && c != -1);
			// 判断是否为负数
			boolean neg = false;
			if (c == '-') {
				// 标记为负数
				neg = true;
				// 读取下一个字符
				c = readByte();
			}
			// 初始化数值
			int val = 0;
			// 读取数字字符直到遇到非数字字符
			while (c > ' ' && c != -1) {
				// 将字符转换为数字并累加到结果中
				val = val * 10 + (c - '0');
				// 读取下一个字符
				c = readByte();
			}
			// 根据符号返回正数或负数
			return neg ? -val : val;
		}
	}

}