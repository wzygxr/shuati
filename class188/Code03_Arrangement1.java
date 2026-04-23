/*
 * 合法排列数对，java版
 * 问题描述：给定n个数对，每个数对(a, b)，其中 a != b，并且不存在相同的数对
 * 目标：希望所有数对形成首尾相接的样子，叫做合法排列，比如以下的数对
 * [5,1] [4,5] [11,9] [9,4] 合法排列为 [11,9] [9,4] [4,5] [5,1]
 * 注意每个数对必须使用且仅用1次，数据保证存在合法排列，返回任何一个都可以
 * 数据范围：1 <= n <= 10^5, 0 <= 数字 <= 10^9
 * 测试链接 : https://leetcode.cn/problems/valid-arrangement-of-pairs/
 * 
 * 面试要点：
 * - 欧拉路径问题的经典应用
 * - 图论中欧拉路径的存在条件
 * - 数字离散化处理
 * - Hierholzer算法求解欧拉路径
 * 
 * ML/DL关联：
 * - 序列生成中的约束满足问题
 * - 图神经网络中的路径优化
 * - 组合优化问题的机器学习方法
 */

// 导入Arrays数组工具类，提供数组操作方法如排序
// 面试要点：内置工具类的使用，如排序、查找等
// ML/DL关联：数据预处理中的数组操作
import java.util.Arrays;

public class Code03_Arrangement1 {

	/**
	 * 解决合法排列数对问题的解决方案类
	 * 将数对排列问题转化为欧拉路径问题求解
	 * 
	 * 面试要点：
	 * - 类设计模式
	 * - 算法封装策略
	 * 
	 * ML/DL关联：
	 * - 模型类设计原则
	 * - 算法模块化实现
	 */
	class Solution {

		/**
		 * 最大节点数常量，根据题目限制设置
		 * 面试要点：合理的空间预分配
		 * ML/DL关联：批次大小对内存的影响
		 */
		public int MAXN = 200001;
		/**
		 * 离散化后节点数和原始数对数量
		 * 面试要点：全局变量的含义和作用
		 * ML/DL关联：超参数管理
		 */
		public int n, m;
		/**
		 * 原始数对数组，存储输入的数对信息
		 * 面试要点：二维数组的内存布局
		 * ML/DL关联：张量形状设计
		 */
		public int[][] pair;
		/**
		 * 排序数组，用于离散化处理
		 * 面试要点：离散化算法的辅助数组
		 * ML/DL关联：数据预处理中的特征编码
		 */
		public int[] sortv = new int[MAXN];

		/**
		 * 邻接表头指针数组，用于链式前向星存储图
		 * head[i]表示节点i的第一条出边的索引
		 * 面试要点：图的多种存储方式对比（邻接矩阵vs邻接表vs链式前向星）
		 * ML/DL关联：稀疏图的高效存储方法
		 */
		public int[] head = new int[MAXN];
		/**
		 * 邻接表边的下一个指针数组，用于链式前向星
		 * nxt[i]表示第i条边的下一条边的索引
		 * 面试要点：链式前向星的实现原理
		 * ML/DL关联：链表结构在图表示中的应用
		 */
		public int[] nxt = new int[MAXN];
		/**
		 * 邻接表边的目标节点数组，用于链式前向星
		 * to[i]表示第i条边指向的目标节点
		 * 面试要点：链式前向星的空间复杂度O(M)
		 * ML/DL关联：图的邻接关系表示
		 */
		public int[] to = new int[MAXN];
		/**
		 * 图的边计数器，用于链式前向星的边编号
		 * 面试要点：边的唯一标识符管理
		 * ML/DL关联：图中边的索引机制
		 */
		public int cntg;

		/**
		 * 当前节点的当前边指针数组，用于Hierholzer算法
		 * cur[i]表示节点i当前应该访问的下一条边
		 * 面试要点：Hierholzer算法的关键数据结构，避免重复访问边
		 * ML/DL关联：动态规划中的状态转移指针
		 */
		public int[] cur = new int[MAXN];
		/**
		 * 节点出度数组，outDeg[i]表示节点i的出度
		 * 面试要点：欧拉路径判定定理的核心数据结构
		 * ML/DL关联：节点度数作为图的重要特征
		 */
		public int[] outDeg = new int[MAXN];
		/**
		 * 节点入度数组，inDeg[i]表示节点i的入度
		 * 面试要点：入度出度计算是图论算法的基础
		 * ML/DL关联：有向图中节点的流入流出特征
		 */
		public int[] inDeg = new int[MAXN];

		/**
		 * 欧拉路径结果数组，存储最终的路径节点
		 * 面试要点：路径重构的数据结构
		 * ML/DL关联：序列生成模型的输出结构
		 */
		public int[] path = new int[MAXN];
		/**
		 * 路径节点计数器，记录路径中节点的数量
		 * 面试要点：计数器在算法中的作用
		 * ML/DL关联：序列长度统计
		 */
		public int cntp;

		/**
		 * 添加边到链式前向星图结构中
		 * 
		 * @param u 起点
		 * @param v 终点
		 * 
		 *          面试要点：
		 *          - 链式前向星的插入操作
		 *          - 时间复杂度：O(1)
		 *          - 空间复杂度：O(M)
		 * 
		 *          ML/DL关联：
		 *          - 图构建操作在GNN预处理中的应用
		 *          - 邻接关系的动态更新
		 */
		public void addEdge(int u, int v) {
			// 将新边插入到邻接表的头部，形成链表结构
			// nxt[++cntg]指向原来的首边，实现链表头插
			nxt[++cntg] = head[u];
			// to[cntg]记录新边的目标节点
			to[cntg] = v;
			// head[u]更新为新边的索引
			head[u] = cntg;
		}

		/**
		 * 二分查找函数，用于找到数字num在排序数组中的位置
		 * 
		 * @param num 要查找的数字
		 * @return 数字在排序数组中的位置（从1开始），如果没找到则返回0
		 * 
		 *         面试要点：
		 *         - 二分查找算法的实现
		 *         - 查找第一个大于等于目标值的位置
		 *         - 时间复杂度：O(logN)
		 * 
		 *         ML/DL关联：
		 *         - 搜索算法在模型推理中的应用
		 *         - 索引查找在特征检索中的作用
		 */
		public int kth(int num) {
			// 使用二分查找找到数字num在排序数组中的位置
			int l = 1, r = n, ans = 0;
			while (l <= r) {
				// 计算中间位置，使用无符号右移防止整数溢出
				int mid = (l + r) >>> 1;
				// 如果中间值大于等于目标值，记录当前位置并继续向左查找
				if (sortv[mid] >= num) {
					ans = mid;
					r = mid - 1;
				} else {
					// 如果中间值小于目标值，向右查找
					l = mid + 1;
				}
			}
			// 返回找到的位置
			return ans;
		}

		/**
		 * 初始化准备工作，包括离散化处理和数组初始化
		 * 
		 * 面试要点：
		 * - 离散化算法的实现
		 * - 数据预处理的重要性
		 * - 数组去重和排序
		 * 
		 * ML/DL关联：
		 * - 数据预处理管道的设计
		 * - 特征离散化在机器学习中的应用
		 */
		public void prepare() {
			// 将所有数对中的数字收集到排序数组中
			int len = 0;
			for (int i = 0; i < m; i++) {
				// 分别添加数对的两个元素
				sortv[++len] = pair[i][0];
				sortv[++len] = pair[i][1];
			}
			// 对排序数组进行排序
			Arrays.sort(sortv, 1, len + 1);
			// 去除重复元素，得到离散化后的节点数量
			n = 1;
			for (int i = 2; i <= len; i++) {
				if (sortv[n] != sortv[i]) {
					sortv[++n] = sortv[i];
				}
			}
			// 初始化计数器和数组
			cntg = cntp = 0;
			for (int i = 1; i <= n; i++) {
				// 重置邻接表头指针、出度和入度数组
				head[i] = outDeg[i] = inDeg[i] = 0;
			}
		}

		/**
		 * 构建图结构，包括计算度数和建立邻接表
		 * 
		 * 面试要点：
		 * - 图的构建过程
		 * - 度数统计的重要性
		 * - 邻接表的动态构建
		 * 
		 * ML/DL关联：
		 * - 图构建在GNN中的预处理步骤
		 * - 度数特征在图学习中的作用
		 */
		public void connect() {
			// 遍历所有数对，构建图结构
			for (int i = 0, u, v; i < m; i++) {
				// 获取数对对应的离散化后的节点编号
				u = kth(pair[i][0]);
				v = kth(pair[i][1]);
				// 更新节点的出度和入度
				outDeg[u]++;
				inDeg[v]++;
				// 将边添加到图的邻接表中
				addEdge(u, v);
			}
			// 初始化每个节点的当前边指针
			for (int i = 1; i <= n; i++) {
				// cur[i]初始化为head[i]，表示从第一条边开始访问
				cur[i] = head[i];
			}
		}

		/**
		 * 在有向图中找到欧拉路径的起点
		 * 
		 * 面试要点：
		 * - 欧拉路径存在条件的实现
		 * - 度数差的判断逻辑
		 * - 起点选择的策略
		 * 
		 * ML/DL关联：
		 * - 图遍历的起始点选择策略
		 * - 序列生成的初始状态设定
		 */
		public int directedStart() {
			// 记录起点和终点（出度比入度多1的点和入度比出度多1的点）
			int start = -1, end = -1;
			// 遍历所有节点，检查度数差
			for (int i = 1; i <= n; i++) {
				// 计算当前节点的出度减入度
				int v = outDeg[i] - inDeg[i];
				// 检查度数差是否合法（只能是-1, 0, 1）
				if (v < -1 || v > 1 || (v == 1 && start != -1) || (v == -1 && end != -1)) {
					// 如果度数差不合法，返回-1表示不存在欧拉路径
					return -1;
				}
				// 如果出度比入度多1，这是起点
				if (v == 1) {
					start = i;
				}
				// 如果入度比出度多1，这是终点
				if (v == -1) {
					end = i;
				}
			}
			// 检查起点和终点的配对情况（要么都是-1，要么都不是-1）
			if ((start == -1) ^ (end == -1)) {
				// 只有一个存在而另一个不存在，不符合欧拉路径条件
				return -1;
			}
			// 如果找到了起点（欧拉路径），返回起点
			if (start != -1) {
				return start;
			}
			// 如果没有起点和终点（欧拉回路），返回任意有出边的节点
			for (int i = 1; i <= n; i++) {
				// 如果节点有出边，它可以作为欧拉回路的起点
				if (outDeg[i] > 0) {
					return i;
				}
			}
			// 没有找到合适的起点，返回-1
			return -1;
		}

		/**
		 * Hierholzer算法，用于寻找欧拉路径
		 * 
		 * 面试要点：
		 * - Hierholzer算法的实现原理
		 * - 递归实现的欧拉路径查找
		 * - 路径记录方法
		 * 
		 * ML/DL关联：
		 * - 图遍历算法在GNN中的应用
		 * - 序列生成的递归方法
		 */
		public void euler(int u) {
			// 遍历从节点u出发的所有未访问边
			for (int e = cur[u]; e > 0; e = cur[u]) {
				// 更新当前边指针，跳过已访问的边
				cur[u] = nxt[e];
				// 递归访问下一个节点
				euler(to[e]);
			}
			// 将当前节点加入路径
			path[++cntp] = u;
		}

		/**
		 * 解决合法排列数对问题的主要方法
		 * 
		 * @param pairs 输入的数对数组
		 * @return 合法排列结果数组
		 * 
		 *         面试要点：
		 *         - 算法整体流程的整合
		 *         - 问题转化策略（数对排列转欧拉路径）
		 *         - 结果重构方法
		 * 
		 *         ML/DL关联：
		 *         - 约束满足问题的机器学习方法
		 *         - 图算法与深度学习的结合
		 */
		public int[][] validArrangement(int[][] pairs) {
			// 初始化成员变量
			m = pairs.length;
			pair = pairs;
			// 执行准备工作
			prepare();
			// 构建图结构
			connect();
			// 寻找欧拉路径的起点
			int start = directedStart();
			// 如果不存在欧拉路径，返回null
			if (start == -1) {
				return null;
			}
			// 使用Hierholzer算法求解欧拉路径
			euler(start);
			// 检查路径长度是否正确（应包含m+1个节点）
			if (cntp != m + 1) {
				return null;
			}
			// 构建结果数组
			int[][] ans = new int[m][2];
			// 根据路径重构数对排列
			for (int i = 0, j = cntp; i < m; i++, j--) {
				// 将离散化后的节点编号转换回原始数字
				ans[i][0] = sortv[path[j]];
				ans[i][1] = sortv[path[j - 1]];
			}
			// 返回结果
			return ans;
		}

	}

}