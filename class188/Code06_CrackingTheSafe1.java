// 破解保险箱，java版
// 给定正数n，表示密码有n位，给定正数k，表示每一位可能的数字是[0..k-1]
// 密码有(k^n)个可能性，构造一个字符串，其中的连续子串包含所有可能的密码
// 先保证字符串的长度最短，作为加强要求，保证字典序尽量的小，返回字符串
// 1 <= n <= 4    1 <= k <= 10
// 测试链接 : https://leetcode.cn/problems/cracking-the-safe/
// 提交以下代码中的Solution类，可以通过所有测试用例

public class Code06_CrackingTheSafe1 {

	class Solution {

		// 定义最大节点数量常量，用于数组初始化
		public int MAXN = 5001;
		// n表示密码位数，k表示每位可能的数字个数，m表示节点总数
		public int n, k, m;
		// cur数组记录每个节点当前访问的边的索引
		public int[] cur = new int[MAXN];
		// path数组存储欧拉路径上的边序列
		public int[] path = new int[MAXN];
		// cntp用于记录路径数组中的元素数量
		public int cntp;

		// 用二维数组模拟栈结构，第一列存储节点，第二列存储边
		public int[][] sta = new int[MAXN][2];
		// u和e分别临时存储栈顶的节点和边
		public int u, e;
		// stacksize记录栈中元素的数量
		public int stacksize;

		// 将节点u和边e压入栈中
		public void push(int u, int e) {
			// 将节点u存储到栈的第0列
			sta[stacksize][0] = u;
			// 将边e存储到栈的第1列
			sta[stacksize][1] = e;
			// 栈大小加1
			stacksize++;
		}

		// 从栈中弹出节点和边
		public void pop() {
			// 栈大小减1
			stacksize--;
			// 获取栈顶的节点
			u = sta[stacksize][0];
			// 获取栈顶的边
			e = sta[stacksize][1];
		}

		// 准备阶段：初始化参数和数据结构
		public void prepare(int len, int num) {
			// 设置密码长度
			n = len;
			// 设置每位可能的数字个数
			k = num;
			// 初始化节点总数为1
			m = 1;
			// 计算节点总数，即k^(n-1)
			for (int i = 1; i <= n - 1; i++) {
				// 每次乘以k，得到k^(n-1)
				m *= k;
			}
			// 初始化cur数组，记录每个节点当前访问的边的索引
			for (int i = 0; i < m; i++) {
				// 将每个节点的边索引初始化为0
				cur[i] = 0;
			}
			// 将路径计数器初始化为0
			cntp = 0;
		}

		// 使用递归实现欧拉路径算法
		// 功能：找到有向图中的欧拉路径，解决破解保险箱问题
		// 面试考点：欧拉路径/回路的存在性判断、Hierholzer算法
		// 边界条件：考虑重边、自环等特殊情况
		// ML/DL关联：图神经网络中路径遍历的应用
		public void euler1(int u, int e) {
			// 当当前节点u还有未访问的边时继续循环
			while (cur[u] < k) {
				// 获取下一条要访问的边，并将cur[u]自增
				int ne = cur[u]++;
				// 递归调用，计算下一个节点并访问该边
				euler1((u * k + ne) % m, ne);
			}
			// 将当前边添加到路径中
			path[++cntp] = e;
		}

		// 使用迭代实现欧拉路径算法，避免递归深度过大
		// 功能：非递归方式实现欧拉路径，适用于大图
		// 面试考点：递归转迭代技巧、显式栈管理
		// 边界条件：栈溢出风险控制
		// ML/DL关联：内存受限环境下的图遍历优化
		public void euler2(int node, int edge) {
			// 将栈大小初始化为0
			stacksize = 0;
			// 将初始节点和边压入栈中
			push(node, edge);
			// 当栈不为空时继续循环
			while (stacksize > 0) {
				// 弹出栈顶元素
				pop();
				// 如果当前节点还有未访问的边
				if (cur[u] < k) {
					// 获取下一条要访问的边，并将cur[u]自增
					int ne = cur[u]++;
					// 将当前节点和边重新压入栈
					push(u, e);
					// 将下一个节点和边压入栈
					push((u * k + ne) % m, ne);
				} else {
					// 将当前边添加到路径中
					path[++cntp] = e;
				}
			}
		}

		// 主函数：破解保险箱，生成最短字符串包含所有可能密码
		// 功能：通过欧拉路径构造de Bruijn序列
		// 面试考点：de Bruijn序列、欧拉路径与哈密顿路径的区别、图论建模
		// 边界条件：n=1时的特殊处理、k=1时的退化情况
		// ML/DL关联：序列建模、状态转移矩阵构建
		public String crackSafe(int len, int num) {
			// 调用准备函数初始化参数和数据结构
			prepare(len, num);
			// 使用递归版欧拉路径算法生成路径
			euler1(0, -1);
			// euler2(0, -1); // 可选：使用迭代版本
			// 创建StringBuilder用于高效字符串拼接
			StringBuilder str = new StringBuilder();
			// 添加前缀：n-1个'0'，对应起始节点
			for (int i = 1; i <= n - 1; i++) {
				// 添加'0'字符
				str.append("0");
			}
			// 逆序添加路径中的边，构成最终结果字符串
			for (int i = cntp - 1; i >= 1; i--) {
				// 将路径中的边添加到结果字符串
				str.append(path[i]);
			}
			// 返回构造完成的字符串
			return str.toString();
		}

	}

}
