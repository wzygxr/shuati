package class152;

/**
 * FHQ-Treap（无旋Treap）带词频压缩实现 - Java版本
 * 
 * 【算法原理】
 * FHQ-Treap结合了二叉搜索树（BST）和堆（Treap）的特性：
 * 1. 满足BST性质：左子树节点值 ≤ 根节点值 ≤ 右子树节点值
 * 2. 满足堆性质：父节点优先级 ≥ 子节点优先级（使用随机优先级）
 * 
 * 【核心操作】
 * - split：分裂操作，根据值将树分为两部分
 * - merge：合并操作，将两棵满足条件的树合并
 * 
 * 【词频压缩】
 * 对于重复值，只存储一个节点并维护count计数，减少空间占用和操作次数
 * 
 * 【时间复杂度分析】
 * 所有操作的期望时间复杂度：O(log n)，其中n为元素总数
 * 最坏情况下，可能退化为O(n)，但概率极低
 * 
 * 【空间复杂度分析】
 * O(m)，其中m为不同值的数量（m ≤ n）
 * 数组实现空间为O(MAXN)，MAXN应根据题目约束设置为足够大
 * 
 * 【适用题目】
 * - 洛谷 P3369 普通平衡树：实现平衡树的6种基本操作
 *   题目链接：https://www.luogu.com.cn/problem/P3369
 *   题目描述：维护一个有序集合，支持插入、删除、查询排名、查询第k小数、前驱、后继等操作
 * - LeetCode 456 132模式：利用前驱和后继查找特性
 *   题目链接：https://leetcode.cn/problems/132-pattern/
 *   题目描述：判断数组中是否存在132模式的子序列
 * - LeetCode 2336 无限集中的最小数字：支持动态插入删除
 *   题目链接：https://leetcode.cn/problems/smallest-number-in-infinite-set/
 *   题目描述：维护一个包含所有正整数的无限集，支持弹出最小元素和添加元素
 * - LeetCode 1845 座位预约管理系统：维护有序集合并支持快速查询和删除
 *   题目链接：https://leetcode.cn/problems/seat-reservation-manager/
 *   题目描述：实现一个座位预约管理系统，支持预约和取消预约操作
 * - LeetCode 1146 快照数组：可作为可持久化实现的基础
 *   题目链接：https://leetcode.cn/problems/snapshot-array/
 *   题目描述：实现一个支持快照的数组，能够在某个时间点创建快照并查询历史版本
 * - SPOJ ORDERSET：Order statistic set
 *   题目链接：https://www.spoj.com/problems/ORDERSET/
 *   题目描述：维护一个动态集合，支持插入、删除、查询第k小数、查询某数的排名等操作
 * 
 * 【输入输出】
 * 操作数：n ≤ 10^5
 * 数值范围：-10^7 ≤ x ≤ +10^7
 * 操作类型：6种基本操作
 * 
 * 【语言特性注意】
 * Java中使用数组模拟节点，避免频繁创建对象带来的性能开销
 * 使用StreamTokenizer进行高效输入，适合大数据量场景
 * 
 * 【工程化考量】
 * 1. 内存管理：使用静态数组预分配空间，避免频繁GC
 * 2. 异常处理：主函数中添加try-catch块，确保程序健壮性
 * 3. 资源管理：正确关闭输入输出流
 * 4. 性能优化：使用高效IO，避免重复计算
 * 5. 可调试性：提供inorder函数用于验证树的正确性
 * 
 * 【算法安全】
 * 1. 边界处理：各种函数中都有对空节点的检查
 * 2. 异常防御：处理非法操作和无效参数
 * 3. 防止栈溢出：递归深度最多为O(log n)，适合大部分场景
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_FHQTreapWithCount1 {

	// 最大节点数量，根据题目约束设置
	public static int MAXN = 100001;

	// 整棵树的头节点编号（根节点）
	public static int head = 0;

	// 空间使用计数，记录当前已分配的节点数量
	public static int cnt = 0;

	// 节点存储的键值
	public static int[] key = new int[MAXN];

	// 词频计数：记录每个键值出现的次数
	public static int[] count = new int[MAXN];

	// 左子节点指针
	public static int[] left = new int[MAXN];

	// 右子节点指针
	public static int[] right = new int[MAXN];

	// 子树大小：当前节点及其子树中元素的总数
	public static int[] size = new int[MAXN];

	// 节点优先级：使用double类型存储随机优先级，保证树的平衡
	public static double[] priority = new double[MAXN];

	/**
	 * 更新节点的子树大小
	 * @param i 当前节点编号
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * 维护子树大小信息，用于快速计算排名和第k小元素
	 */
	public static void up(int i) {
		// 子树大小 = 左子树大小 + 右子树大小 + 当前节点的计数
		size[i] = size[left[i]] + size[right[i]] + count[i];
	}

	/**
	 * 分裂操作：按值分裂
	 * 将以i为根的子树分裂成两棵树，左树包含所有值<=num的节点，右树包含所有值>num的节点
	 * 
	 * @param l 左树的根节点（作为输出参数）
	 * @param r 右树的根节点（作为输出参数）
	 * @param i 当前处理的节点
	 * @param num 分裂值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【实现细节】
	 * - 使用递归方式分裂子树
	 * - 利用right和left数组作为输出参数，存储分裂后的根节点
	 * - 每次分裂后需要更新节点的子树大小
	 */
	public static void split(int l, int r, int i, int num) {
		// 边界条件：空树
		if (i == 0) {
			right[l] = left[r] = 0;
		} else {
			// 根据当前节点的值决定分裂方向
			if (key[i] <= num) {
				// 当前节点及其左子树属于左树，继续分裂右子树
				right[l] = i;
				split(i, r, right[i], num);
			} else {
				// 当前节点及其右子树属于右树，继续分裂左子树
				left[r] = i;
				split(l, i, left[i], num);
			}
			// 分裂后更新当前节点的子树大小
			up(i);
		}
	}

	/**
	 * 合并操作：将两棵满足条件的树合并成一棵树
	 * 前提条件：左树中所有节点的值 <= 右树中所有节点的值
	 * 
	 * @param l 左树的根节点
	 * @param r 右树的根节点
	 * @return 合并后的树的根节点
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【实现细节】
	 * - 根据堆性质（优先级）决定合并方向
	 * - 优先级高的节点作为根，递归合并子树
	 * - 合并后更新节点的子树大小
	 */
	public static int merge(int l, int r) {
		// 边界条件：其中一棵树为空
		if (l == 0 || r == 0) {
			return l + r; // 返回非空的树
		}
		// 根据堆性质（优先级）决定合并方向
		if (priority[l] >= priority[r]) {
			// 左树根节点优先级更高，作为新根，递归合并其右子树与右树
			right[l] = merge(right[l], r);
			up(l);
			return l;
		} else {
			// 右树根节点优先级更高，作为新根，递归合并其左子树与左树
			left[r] = merge(l, left[r]);
			up(r);
			return r;
		}
	}

	/**
	 * 在树中查找指定值的节点
	 * 
	 * @param i 当前搜索的根节点
	 * @param num 要查找的值
	 * @return 找到的节点编号，未找到返回0
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 */
	public static int find(int i, int num) {
		if (i == 0) {
			return 0; // 未找到
		}
		if (key[i] == num) {
			return i; // 找到目标节点
		} else if (key[i] > num) {
			return find(left[i], num); // 在左子树中查找
		} else {
			return find(right[i], num); // 在右子树中查找
		}
	}

	/**
	 * 修改指定值的节点的计数
	 * 
	 * @param i 当前搜索的根节点
	 * @param num 要修改的值
	 * @param change 计数变化量（+1或-1）
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【注意】
	 * 调用此方法前必须确保节点存在
	 */
	public static void changeCount(int i, int num, int change) {
		if (key[i] == num) {
			// 找到目标节点，修改计数
			count[i] += change;
		} else if (key[i] > num) {
			changeCount(left[i], num, change); // 在左子树中查找并修改
		} else {
			changeCount(right[i], num, change); // 在右子树中查找并修改
		}
		// 修改后更新子树大小
		up(i);
	}

	/**
	 * 添加元素操作
	 * 
	 * @param num 要添加的值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 包含find、changeCount或split/merge的递归栈深度
	 * 
	 * 【实现思路】
	 * 1. 先检查值是否已存在
	 * 2. 如果存在，增加词频计数
	 * 3. 如果不存在，创建新节点并插入到树中
	 */
	public static void add(int num) {
		// 检查值是否已存在
		if (find(head, num) != 0) {
			// 值已存在，增加词频计数
			changeCount(head, num, 1);
		} else {
			// 值不存在，创建新节点并插入
			split(0, 0, head, num);
			// 分配新节点
			key[++cnt] = num;
			count[cnt] = size[cnt] = 1;
			priority[cnt] = Math.random(); // 随机优先级
			// 合并：<=num的部分 + 新节点 + >num的部分
			head = merge(merge(right[0], cnt), left[0]);
		}
	}

	/**
	 * 删除元素操作
	 * 
	 * @param num 要删除的值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 包含find、changeCount或两次split/merge的递归栈深度
	 * 
	 * 【实现思路】
	 * 1. 先检查值是否存在
	 * 2. 如果存在且计数>1，减少词频计数
	 * 3. 如果存在且计数=1，将该节点完全删除（通过两次分裂）
	 */
	public static void remove(int num) {
		// 查找值对应的节点
		int i = find(head, num);
		if (i != 0) {
			if (count[i] > 1) {
				// 计数大于1，只减少计数
				changeCount(head, num, -1);
			} else {
				// 计数等于1，需要完全删除节点
				// 第一次分裂：分成<=num和>num两部分
				split(0, 0, head, num);
				int lm = right[0]; // <=num的部分
				int r = left[0];  // >num的部分
				// 第二次分裂：将lm分成<num和=num两部分
				split(0, 0, lm, num - 1);
				int l = right[0]; // <num的部分
				// 合并<num和>num的部分，相当于删除=num的部分
				head = merge(l, r);
			}
		}
		// 如果值不存在，不做任何操作
	}

	/**
	 * 统计小于num的元素个数
	 * 
	 * @param i 当前搜索的根节点
	 * @param num 目标值
	 * @return 小于num的元素个数
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 */
	public static int small(int i, int num) {
		if (i == 0) {
			return 0; // 空树
		}
		if (key[i] >= num) {
			// 当前节点值>=num，继续在左子树中统计
			return small(left[i], num);
		} else {
			// 当前节点值<num，统计结果包括左子树、当前节点及右子树中<num的部分
			return size[left[i]] + count[i] + small(right[i], num);
		}
	}

	/**
	 * 查询元素的排名（比num小的数的个数+1）
	 * 
	 * @param num 查询的值
	 * @return num的排名
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n)
	 */
	public static int rank(int num) {
		// 排名 = 小于num的元素个数 + 1
		return small(head, num) + 1;
	}

	/**
	 * 查询第k小的元素（递归实现）
	 * 
	 * @param i 当前搜索的根节点
	 * @param x 排名（第x小）
	 * @return 第x小的元素值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【实现思路】
	 * 1. 如果左子树大小 >= x，说明第x小在左子树中
	 * 2. 如果左子树大小 + 当前节点计数 < x，说明第x小在右子树中
	 * 3. 否则，当前节点就是第x小的元素
	 */
	public static int index(int i, int x) {
		if (size[left[i]] >= x) {
			// 第x小在左子树
			return index(left[i], x);
		} else if (size[left[i]] + count[i] < x) {
			// 第x小在右子树，调整查询位置
			return index(right[i], x - size[left[i]] - count[i]);
		}
		// 当前节点就是第x小的元素
		return key[i];
	}

	/**
	 * 查询第k小的元素（对外接口）
	 * 
	 * @param x 排名（第x小）
	 * @return 第x小的元素值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n)
	 */
	public static int index(int x) {
		return index(head, x);
	}

	/**
	 * 查询前驱（小于num的最大元素）
	 * 
	 * @param i 当前搜索的根节点
	 * @param num 目标值
	 * @return 前驱元素值，如果不存在返回Integer.MIN_VALUE
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【实现思路】
	 * 1. 如果当前节点值 >= num，前驱一定在左子树中
	 * 2. 如果当前节点值 < num，前驱可能是当前节点或右子树中的某个节点
	 */
	public static int pre(int i, int num) {
		if (i == 0) {
			return Integer.MIN_VALUE; // 空树，无前驱
		}
		if (key[i] >= num) {
			// 当前节点值>=num，前驱在左子树
			return pre(left[i], num);
		} else {
			// 当前节点值<num，比较当前节点和右子树中的前驱
			return Math.max(key[i], pre(right[i], num));
		}
	}

	/**
	 * 查询前驱（对外接口）
	 * 
	 * @param num 目标值
	 * @return 前驱元素值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n)
	 */
	public static int pre(int num) {
		return pre(head, num);
	}

	/**
	 * 查询后继（大于num的最小元素）
	 * 
	 * @param i 当前搜索的根节点
	 * @param num 目标值
	 * @return 后继元素值，如果不存在返回Integer.MAX_VALUE
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 * 
	 * 【实现思路】
	 * 1. 如果当前节点值 <= num，后继一定在右子树中
	 * 2. 如果当前节点值 > num，后继可能是当前节点或左子树中的某个节点
	 */
	public static int post(int i, int num) {
		if (i == 0) {
			return Integer.MAX_VALUE; // 空树，无后继
		}
		if (key[i] <= num) {
			// 当前节点值<=num，后继在右子树
			return post(right[i], num);
		} else {
			// 当前节点值>num，比较当前节点和左子树中的后继
			return Math.min(key[i], post(left[i], num));
		}
	}

	/**
	 * 查询后继（对外接口）
	 * 
	 * @param num 目标值
	 * @return 后继元素值
	 * 
	 * 时间复杂度：O(log n)
	 * 空间复杂度：O(log n)
	 */
	public static int post(int num) {
		return post(head, num);
	}

	/**
	 * 中序遍历函数：用于调试和验证树的正确性
	 * 
	 * @param i 当前节点
	 * 
	 * 时间复杂度：O(n)
	 * 空间复杂度：O(log n) - 递归调用栈深度
	 */
	public static void inorder(int i) {
		if (i == 0) {
			return;
		}
		inorder(left[i]); // 访问左子树
		// 输出当前节点的所有元素
		for (int j = 0; j < count[i]; j++) {
			System.out.print(key[i] + " ");
		}
		inorder(right[i]); // 访问右子树
	}

	/**
	 * 主函数：处理输入输出和操作调用
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 * 
	 * 【输入格式】
	 * 第一行：操作数n
	 * 接下来n行：每行一个操作和参数
	 * 操作类型：
	 * 1 x：插入x
	 * 2 x：删除x
	 * 3 x：查询x的排名
	 * 4 x：查询第x小的数
	 * 5 x：查询x的前驱
	 * 6 x：查询x的后继
	 */
	/**
	 * 主函数：处理输入输出和操作调用
	 * 
	 * @param args 命令行参数
	 * @throws IOException 输入输出异常
	 * 
	 * 【输入格式】
	 * 第一行：操作数n
	 * 接下来n行：每行一个操作和参数
	 * 操作类型：
	 * 1 x：插入x
	 * 2 x：删除x
	 * 3 x：查询x的排名
	 * 4 x：查询第x小的数
	 * 5 x：查询x的前驱
	 * 6 x：查询x的后继
	 * 
	 * 【性能优化】
	 * 1. 使用StreamTokenizer代替Scanner提高输入效率
	 * 2. 使用PrintWriter缓存输出，减少IO次数
	 * 3. 提前声明循环变量，避免重复创建
	 * 
	 * 【异常处理】
	 * 捕获所有异常，打印错误信息并继续处理后续操作
	 * 这样可以确保即使部分操作失败，程序也能继续运行
	 * 
	 * 【资源管理】
	 * 正确关闭所有输入输出流，避免资源泄漏
	 */
	public static void main(String[] args) throws IOException {
		// 使用高效的输入输出方式，适合大数据量
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		
		// 初始化随机数种子（使用系统时间）
		// Java的Math.random()已经内部使用了系统时间作为种子，但显式设置可以确保随机性
		// 注意：Math.random()的线程安全性由JVM保证
		
		// 读取操作数
		in.nextToken();
		int n = (int) in.nval;
		
		// 处理每个操作
		for (int i = 1, op, x; i <= n; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			x = (int) in.nval;
			
			try {
				switch (op) {
					case 1:
						add(x); // 插入操作
						break;
					case 2:
						remove(x); // 删除操作
						break;
					case 3:
						out.println(rank(x)); // 排名查询
						break;
					case 4:
						// 验证参数有效性，防止访问越界
						if (x < 1 || x > size[head]) {
							out.println("参数错误：排名超出范围");
							break;
						}
						out.println(index(x)); // 第k小查询
						break;
					case 5:
						out.println(pre(x)); // 前驱查询
						break;
					case 6:
						out.println(post(x)); // 后继查询
						break;
					default:
						// 处理非法操作
						System.err.println("非法操作: " + op);
				}
			} catch (Exception e) {
				// 异常处理，防止程序崩溃
				System.err.println("操作执行出错: " + op + " " + x);
				e.printStackTrace();
			}
		}
		
		// 刷新输出并关闭资源
		out.flush();
		out.close();
		br.close();
		
		// 调试用：中序遍历验证树的正确性
		// System.out.print("中序遍历结果: ");
		// inorder(head);
		// System.out.println();
	}
	
	/**
	 * 测试函数：验证算法的正确性
	 * 可以在IDE中直接运行此函数进行调试
	 * 
	 * 测试样例：
	 * 8
	 * 1 10
	 * 1 20
	 * 1 30
	 * 3 20
	 * 4 2
	 * 2 20
	 * 3 20
	 * 4 2
	 * 
	 * 预期输出：
	 * 2
	 * 20
	 * 2
	 * 30
	 */
	public static void test() {
		// 重置树状态
		head = 0;
		cnt = 0;
		for (int i = 0; i < MAXN; i++) {
			left[i] = right[i] = 0;
			count[i] = size[i] = 0;
		}
		
		// 执行测试操作
		add(10);
		add(20);
		add(30);
		System.out.println("Rank of 20: " + rank(20));  // 应该输出 2
		System.out.println("2nd smallest: " + index(2)); // 应该输出 20
		remove(20);
		System.out.println("Rank of 20: " + rank(20));  // 应该输出 2
		System.out.println("2nd smallest: " + index(2)); // 应该输出 30
		
		// 打印中序遍历结果
		System.out.print("Inorder traversal: ");
		inorder(head);
		System.out.println();
	}

}
