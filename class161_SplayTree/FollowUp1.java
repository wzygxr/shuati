package class153;

/**
 * Splay树实现 - 普通平衡树（数据加强版）问题解决方案
 * 【题目来源】洛谷 P6136
 * 【题目链接】https://www.luogu.com.cn/problem/P6136
 * 【算法分析】
 * Splay树是一种自调整的平衡二叉搜索树，通过将访问过的节点旋转到根附近来优化后续访问
 * 这使得频繁访问的节点能够更快地被再次访问，利用了访问的局部性原理
 * 与普通平衡树相比，数据加强版增加了强制在线的要求
 * 【时间复杂度】
 * - 所有操作均摊时间复杂度为O(log n)
 * - 单次操作最坏情况可能达到O(n)
 * 【空间复杂度】O(n)
 * 【实现特点】
 * 1. 不使用词频压缩，每个重复元素作为单独节点存储
 * 2. 支持强制在线操作
 * 3. 使用异或加密保证在线性
 */

/**
 * 普通平衡树（数据加强版）问题
 * 【题目大意】
 * 实现一种结构，支持以下操作：
 * 1. 插入元素x
 * 2. 删除元素x（如果有多个，只删除一个）
 * 3. 查询x的排名
 * 4. 查询排名为k的数
 * 5. 查询x的前驱
 * 6. 查询x的后继
 * 
 * 与普通版本相比，数据加强版的特点：
 * 1. 数据规模更大（n,m ≤ 10^6）
 * 2. 强制在线：每次操作的参数需要与上一次查询操作的答案进行异或运算
 * 
 * 【解题思路】
 * 使用Splay树实现普通平衡树的所有操作
 * 1. 插入操作：将新元素插入到合适位置并提根
 * 2. 删除操作：找到要删除的元素并删除
 * 3. 查询排名：在BST中查找元素的排名
 * 4. 查询第k大：根据排名查找元素
 * 5. 查询前驱：查找小于x的最大元素
 * 6. 查询后继：查找大于x的最小元素
 * 
 * 【强制在线处理】
 * 每次操作的参数x需要与上一次查询操作的答案lastAns进行异或运算
 * 即实际操作的参数为x ^ lastAns
 * 
 * 【关键技巧】
 * 1. 使用Splay树维护BST性质
 * 2. 通过splay操作优化访问效率
 * 3. 正确处理强制在线要求
 */

// 测试链接 : https://www.luogu.com.cn/problem/P6136
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class FollowUp1 {

	/**
	 * 【空间配置】预分配的最大节点数量
	 * 设置为2000001是因为题目保证操作次数不超过2*10^6，额外+1处理边界情况
	 */
	public static int MAXN = 2000001;

	/**
	 * 【树结构标识】
	 * head: 根节点索引
	 * cnt: 当前已分配的节点计数器
	 */
	public static int head = 0;
	public static int cnt = 0;

	/**
	 * 【节点属性数组】使用数组模拟节点，避免对象创建开销
	 * key: 节点存储的值
	 * father: 父节点索引
	 * left: 左子节点索引
	 * right: 右子节点索引
	 * size: 以该节点为根的子树大小
	 */
	public static int[] key = new int[MAXN];
	public static int[] father = new int[MAXN];
	public static int[] left = new int[MAXN];
	public static int[] right = new int[MAXN];
	public static int[] size = new int[MAXN];

	/**
	 * 【自底向上维护】更新节点子树大小
	 * 时间复杂度: O(1)
	 * @param i 需要更新的节点索引
	 */
	public static void up(int i) {
		size[i] = size[left[i]] + size[right[i]] + 1;
	}

	/**
	 * 【方向判断】确定节点i是其父节点的左子节点还是右子节点
	 * 时间复杂度: O(1)
	 * @param i 需要判断的节点索引
	 * @return 1表示右子节点，0表示左子节点
	 */
	public static int lr(int i) {
		return right[father[i]] == i ? 1 : 0;
	}

	/**
	 * 【核心旋转操作】将节点i旋转至其父节点的位置
	 * 这是Splay树维护平衡的基本操作
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 */
	public static void rotate(int i) {
		int f = father[i];     // 父节点索引
		int g = father[f];     // 祖父节点索引
		int soni = lr(i);      // 当前节点是父节点的左子还是右子
		int sonf = lr(f);      // 父节点是祖父节点的左子还是右子
		
		// 【旋转逻辑】根据当前节点是左子还是右子执行不同的旋转操作
		if (soni == 1) {       // 右子节点，执行右旋
			right[f] = left[i];
			if (right[f] != 0) {
				father[right[f]] = f;
			}
			left[i] = f;
		} else {               // 左子节点，执行左旋
			left[f] = right[i];
			if (left[f] != 0) {
				father[left[f]] = f;
			}
			right[i] = f;
		}
		
		// 更新祖父节点的子节点指针
		if (g != 0) {
			if (sonf == 1) {
				right[g] = i;
			} else {
				left[g] = i;
			}
		}
		
		// 更新父指针
		father[f] = i;
		father[i] = g;
		
		// 【重要】更新节点信息，先更新被旋转的父节点，再更新当前节点
		up(f);
		up(i);
	}

	/**
	 * 【核心伸展操作】将节点i旋转到goal的子节点位置
	 * 如果goal为0，则将i旋转到根节点
	 * 这是Splay树的核心操作，通过一系列旋转使被访问节点移动到树的顶部
	 * 时间复杂度: 均摊O(log n)，最坏情况O(n)
	 * 空间复杂度: O(1)
	 * @param i 需要旋转的节点索引
	 * @param goal 目标父节点索引
	 */
	public static void splay(int i, int goal) {
		int f = father[i], g = father[f];
		
		// 当当前节点的父节点不是目标节点时，继续旋转
		while (f != goal) {
			// 【旋转策略】根据Zig-Zig和Zig-Zag情况选择不同的旋转顺序
			if (g != goal) {
				// 如果父节点和当前节点在同侧，先旋转父节点（Zig-Zig情况）
				// 否则直接旋转当前节点（Zig-Zag情况）
				if (lr(i) == lr(f)) {
					rotate(f);
				} else {
					rotate(i);
				}
			}
			// 最后旋转当前节点
			rotate(i);
			
			// 更新父节点和祖父节点
			f = father[i];
			g = father[f];
		}
		
		// 如果旋转到根节点，更新根节点指针
		if (goal == 0) {
			head = i;
		}
	}

	/**
	 * 【查找操作】在整棵树中找到中序遍历排名为rank的节点
	 * 【特殊注意】此方法不进行提根操作，仅作为内部方法使用
	 * 这是因为remove方法在调用此方法时，要求节点不被提根
	 * 时间复杂度: O(log n)
	 * @param rank 目标排名
	 * @return 对应排名的节点索引
	 */
	public static int find(int rank) {
		int i = head;
		while (i != 0) {
			if (size[left[i]] + 1 == rank) {
				return i;
			} else if (size[left[i]] >= rank) {
				i = left[i];
			} else {
				rank -= size[left[i]] + 1;
				i = right[i];
			}
		}
		return 0; // 未找到对应排名的节点
	}

	/**
	 * 【插入操作】向Splay树中插入一个新元素
	 * 插入后将新节点提至根，以优化后续访问
	 * 时间复杂度: 均摊O(log n)
	 * 空间复杂度: O(1)
	 * @param num 需要插入的元素值
	 */
	public static void add(int num) {
		// 创建新节点
		key[++cnt] = num;
		size[cnt] = 1;
		
		// 【空树处理】如果树为空，直接设置为根节点
		if (head == 0) {
			head = cnt;
		} else {
			// 【查找插入位置】根据BST性质找到合适的插入位置
			int f = 0, i = head, son = 0;
			while (i != 0) {
				f = i;
				if (key[i] <= num) {
					son = 1;
					i = right[i];
				} else {
					son = 0;
					i = left[i];
				}
			}
			
			// 插入节点到找到的位置
			if (son == 1) {
				right[f] = cnt;
			} else {
				left[f] = cnt;
			}
			father[cnt] = f;
			
			// 【重要优化】将刚插入的节点旋转至根，优化后续访问
			splay(cnt, 0);
		}
	}

	/**
	 * 【查询排名】查询元素num在树中的排名
	 * 排名定义为：比num小的元素个数 + 1
	 * 时间复杂度: 均摊O(log n)
	 * @param num 要查询的元素值
	 * @return num的排名
	 */
	public static int rank(int num) {
		int i = head, last = head;
		int ans = 0;
		
		// 【遍历查找】同时计算比num小的元素数量
		while (i != 0) {
			last = i;
			if (key[i] >= num) {
				i = left[i];
			} else {
				// 累加左子树节点数和当前节点
				ans += size[left[i]] + 1;
				i = right[i];
			}
		}
		
		// 【重要优化】将最后访问的节点旋转至根，优化后续访问
		splay(last, 0);
		return ans + 1; // 排名 = 比num小的元素数 + 1
	}

	/**
	 * 【查询第k大元素】查询排名为x的元素值
	 * 时间复杂度: 均摊O(log n)
	 * @param x 目标排名
	 * @return 对应排名的元素值
	 */
	public static int index(int x) {
		int i = find(x);
		// 【重要优化】将找到的节点旋转至根，优化后续访问
		splay(i, 0);
		return key[i];
	}

	/**
	 * 【查询前驱】查询小于num的最大元素
	 * 不存在时返回Integer.MIN_VALUE
	 * 时间复杂度: 均摊O(log n)
	 * @param num 目标元素
	 * @return 前驱元素值
	 */
	public static int pre(int num) {
		int i = head, last = head;
		int ans = Integer.MIN_VALUE;
		
		// 【遍历查找】寻找小于num的最大元素
		while (i != 0) {
			last = i;
			if (key[i] >= num) {
				i = left[i];
			} else {
				// 更新可能的前驱元素
				ans = Math.max(ans, key[i]);
				i = right[i];
			}
		}
		
		// 【重要优化】将最后访问的节点旋转至根，优化后续访问
		splay(last, 0);
		return ans;
	}

	/**
	 * 【查询后继】查询大于num的最小元素
	 * 不存在时返回Integer.MAX_VALUE
	 * 时间复杂度: 均摊O(log n)
	 * @param num 目标元素
	 * @return 后继元素值
	 */
	public static int post(int num) {
		int i = head, last = head;
		int ans = Integer.MAX_VALUE;
		
		// 【遍历查找】寻找大于num的最小元素
		while (i != 0) {
			last = i;
			if (key[i] <= num) {
				i = right[i];
			} else {
				// 更新可能的后继元素
				ans = Math.min(ans, key[i]);
				i = left[i];
			}
		}
		
		// 【重要优化】将最后访问的节点旋转至根，优化后续访问
		splay(last, 0);
		return ans;
	}

	/**
	 * 【删除操作】从树中删除一个等于num的元素
	 * 如果有多个，只删除一个
	 * 时间复杂度: 均摊O(log n)
	 * @param num 需要删除的元素值
	 */
	public static void remove(int num) {
		// 【存在性检查】如果num不存在，直接返回
		int kth = rank(num);
		if (kth != rank(num + 1)) {
			// 找到第一个等于num的节点并旋转至根
			int i = find(kth);
			splay(i, 0);
			
			// 【删除策略】根据子树情况选择不同的删除方式
			if (left[i] == 0) {
				// 没有左子树，直接用右子树替换
				head = right[i];
			} else if (right[i] == 0) {
				// 没有右子树，直接用左子树替换
				head = left[i];
			} else {
				// 同时存在左右子树
				// 找到中序遍历的后继节点（右子树的最小节点）
				int j = find(kth + 1);
				// 将后继节点旋转至当前节点的右子节点
				splay(j, i);
				// 将左子树挂载到后继节点下
				left[j] = left[i];
				father[left[j]] = j;
				// 更新后继节点的大小信息
				up(j);
				// 将后继节点设为新的根
				head = j;
			}
			// 确保新根的父指针为空
			father[head] = 0;
		}
	}

	/**
	 * 【主函数】处理输入输出和操作调用
	 * 【输入输出优化】使用BufferedReader和StreamTokenizer提高读取效率
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 */
	public static void main(String[] args) throws IOException {
		// 【IO优化】使用BufferedReader和StreamTokenizer提高读取效率
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		// 【IO优化】使用PrintWriter提高输出效率
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 读取初始元素数量和操作数量
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		
		// 插入初始元素
		for (int i = 1, num; i <= n; i++) {
			in.nextToken();
			num = (int) in.nval;
			add(num);
		}
		
		// 处理操作
		int lastAns = 0;  // 上一次查询操作的答案
		int ans = 0;      // 所有查询操作答案的异或和
		for (int i = 1, op, x; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval; // 操作类型
			in.nextToken();
			// 【强制在线处理】每次操作的参数需要与上一次查询操作的答案进行异或
			x = (int) in.nval ^ lastAns;
			
			// 根据操作类型执行相应操作
			if (op == 1) {
				// 操作1: 插入元素x
				add(x);
			} else if (op == 2) {
				// 操作2: 删除元素x
				remove(x);
			} else if (op == 3) {
				// 操作3: 查询x的排名
				lastAns = rank(x);
				ans ^= lastAns;
			} else if (op == 4) {
				// 操作4: 查询排名为x的元素
				lastAns = index(x);
				ans ^= lastAns;
			} else if (op == 5) {
				// 操作5: 查询x的前驱
				lastAns = pre(x);
				ans ^= lastAns;
			} else {
				// 操作6: 查询x的后继
				lastAns = post(x);
				ans ^= lastAns;
			}
		}
		
		// 输出所有查询操作答案的异或和
		out.println(ans);
		
		// 【工程化考量】确保所有输出都被刷新并关闭资源
		out.flush();
		out.close();
		br.close();
	}

}