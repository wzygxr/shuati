package class148;

// AVL实现普通有序表，数据加强的测试，java版
// 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
// 基本功能要求都是不变的，可以打开测试链接查看
// 测试链接 : https://www.luogu.com.cn/problem/P6136
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * 补充题目列表：
 * 
 * 1. 洛谷 P6136 【模板】普通平衡树（数据加强版）
 *    链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 2. 洛谷 P3369 【模板】普通平衡树
 *    链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 3. LeetCode 406. Queue Reconstruction by Height
 *    链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
 *    题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 4. PAT甲级 1066 Root of AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
 *    题目描述: 给定插入序列，构建AVL树，输出根节点的值
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 5. PAT甲级 1123 Is It a Complete AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
 *    题目描述: 判断构建的AVL树是否是完全二叉树
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 6. LeetCode 220. Contains Duplicate III
 *    链接: https://leetcode.cn/problems/contains-duplicate-iii/
 *    题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
 *    时间复杂度: O(n log k)
 *    空间复杂度: O(k)
 * 
 * 7. Codeforces 459D - Pashmak and Parmida's problem
 *    链接: https://codeforces.com/problemset/problem/459/D
 *    题目描述: 计算满足条件的点对数量
 *    时间复杂度: O(n log n)
 *    空间复杂度: O(n)
 * 
 * 8. SPOJ Ada and Behives
 *    链接: https://www.spoj.com/problems/ADAAPHID/
 *    题目描述: 维护一个动态集合，支持插入和查询操作
 *    时间复杂度: O(log n) 每次操作
 *    空间复杂度: O(n)
 * 
 * 9. HackerRank Self-Balancing Tree
 *    链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *    题目描述: 实现AVL树的插入操作
 *    时间复杂度: O(log n)
 *    空间复杂度: O(n)
 * 
 * 10. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
 *     链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
 *     题目描述: 字符串处理问题，可使用平衡树优化
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 11. CodeChef ORDERSET
 *     链接: https://www.codechef.com/problems/ORDERSET
 *     题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
 *     时间复杂度: O(log n)
 *     空间复杂度: O(n)
 * 
 * 12. AtCoder ABC134 E - Sequence Decomposing
 *     链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
 *     题目描述: 序列分解问题，可使用平衡树优化
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 13. 牛客网 NC145 01序列的最小权值
 *     链接: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
 *     题目描述: 维护01序列，支持插入和查询操作
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 14. ZOJ 1659 Mobile Phone Coverage
 *     链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
 *     题目描述: 计算矩形覆盖面积，可使用平衡树维护
 *     时间复杂度: O(n log n)
 *     空间复杂度: O(n)
 * 
 * 15. POJ 1864 [NOI2009] 二叉查找树
 *     链接: http://poj.org/problem?id=1864
 *     题目描述: 二叉查找树的动态规划问题
 *     时间复杂度: O(n^2)
 *     空间复杂度: O(n)
 * 
 * 算法思路技巧总结：
 * 1. 适用场景：
 *    - 需要维护有序集合，并支持快速插入、删除、查找
 *    - 需要查询元素排名或第k小元素
 *    - 需要频繁查询前驱和后继元素
 *    - 处理强制在线问题
 * 
 * 2. 核心思想：
 *    - 通过旋转操作维持树的平衡性，保证树的高度为O(log n)
 *    - 每个节点维护子树大小和高度信息
 *    - 插入和删除操作后通过旋转调整恢复平衡
 *    - 强制在线通过异或操作实现
 * 
 * 3. 四种旋转操作：
 *    - LL旋转：在左孩子的左子树插入导致失衡
 *    - RR旋转：在右孩子的右子树插入导致失衡
 *    - LR旋转：在左孩子的右子树插入导致失衡
 *    - RL旋转：在右孩子的左子树插入导致失衡
 * 
 * 4. 工程化考量：
 *    - 内存管理：使用数组代替指针减少内存碎片
 *    - 性能优化：通过维护子树大小信息支持排名查询
 *    - 边界处理：处理重复元素和空树等边界情况
 *    - 异常处理：检查输入参数的有效性
 *    - 在线处理：通过异或操作处理强制在线
 * 
 * 5. 时间和空间复杂度：
 *    - 插入：O(log n)
 *    - 删除：O(log n)
 *    - 查找：O(log n)
 *    - 查询排名：O(log n)
 *    - 查询第k小：O(log n)
 *    - 前驱/后继：O(log n)
 *    - 空间复杂度：O(n)
 * 
 * 6. 与其他数据结构的比较：
 *    - 相比Treap：实现更复杂，但平衡性更好
 *    - 相比红黑树：旋转次数可能更多，但实现相对简单
 *    - 相比Splay Tree：最坏时间复杂度更稳定
 * 
 * 7. 语言特性差异：
 *    - Java: 对象引用操作直观，但可能有GC开销
 *    - C++: 指针操作更直接，需要手动管理内存
 *    - Python: 语法简洁，但性能不如Java/C++
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class FollowUp1 {

	public static int MAXN = 2000001;

	public static int cnt = 0;

	public static int head = 0;

	public static int[] key = new int[MAXN];

	public static int[] height = new int[MAXN];

	public static int[] left = new int[MAXN];

	public static int[] right = new int[MAXN];

	public static int[] count = new int[MAXN];

	public static int[] size = new int[MAXN];

	public static void up(int i) {
		size[i] = size[left[i]] + size[right[i]] + count[i];
		height[i] = Math.max(height[left[i]], height[right[i]]) + 1;
	}

	public static int leftRotate(int i) {
		int r = right[i];
		right[i] = left[r];
		left[r] = i;
		up(i);
		up(r);
		return r;
	}

	public static int rightRotate(int i) {
		int l = left[i];
		left[i] = right[l];
		right[l] = i;
		up(i);
		up(l);
		return l;
	}

	public static int maintain(int i) {
		int lh = height[left[i]];
		int rh = height[right[i]];
		if (lh - rh > 1) {
			if (height[left[left[i]]] >= height[right[left[i]]]) {
				i = rightRotate(i);
			} else {
				left[i] = leftRotate(left[i]);
				i = rightRotate(i);
			}
		} else if (rh - lh > 1) {
			if (height[right[right[i]]] >= height[left[right[i]]]) {
				i = leftRotate(i);
			} else {
				right[i] = rightRotate(right[i]);
				i = leftRotate(i);
			}
		}
		return i;
	}

	public static void add(int num) {
		head = add(head, num);
	}

	public static int add(int i, int num) {
		if (i == 0) {
			key[++cnt] = num;
			count[cnt] = size[cnt] = height[cnt] = 1;
			return cnt;
		}
		if (key[i] == num) {
			count[i]++;
		} else if (key[i] > num) {
			left[i] = add(left[i], num);
		} else {
			right[i] = add(right[i], num);
		}
		up(i);
		return maintain(i);
	}

	public static void remove(int num) {
		if (rank(num) != rank(num + 1)) {
			head = remove(head, num);
		}
	}

	public static int remove(int i, int num) {
		if (key[i] < num) {
			right[i] = remove(right[i], num);
		} else if (key[i] > num) {
			left[i] = remove(left[i], num);
		} else {
			if (count[i] > 1) {
				count[i]--;
			} else {
				if (left[i] == 0 && right[i] == 0) {
					return 0;
				} else if (left[i] != 0 && right[i] == 0) {
					i = left[i];
				} else if (left[i] == 0 && right[i] != 0) {
					i = right[i];
				} else {
					int mostLeft = right[i];
					while (left[mostLeft] != 0) {
						mostLeft = left[mostLeft];
					}
					right[i] = removeMostLeft(right[i], mostLeft);
					left[mostLeft] = left[i];
					right[mostLeft] = right[i];
					i = mostLeft;
				}
			}
		}
		up(i);
		return maintain(i);
	}

	public static int removeMostLeft(int i, int mostLeft) {
		if (i == mostLeft) {
			return right[i];
		} else {
			left[i] = removeMostLeft(left[i], mostLeft);
			up(i);
			return maintain(i);
		}
	}

	public static int rank(int num) {
		return small(head, num) + 1;
	}

	public static int small(int i, int num) {
		if (i == 0) {
			return 0;
		}
		if (key[i] >= num) {
			return small(left[i], num);
		} else {
			return size[left[i]] + count[i] + small(right[i], num);
		}
	}

	public static int index(int x) {
		return index(head, x);
	}

	public static int index(int i, int x) {
		if (size[left[i]] >= x) {
			return index(left[i], x);
		} else if (size[left[i]] + count[i] < x) {
			return index(right[i], x - size[left[i]] - count[i]);
		}
		return key[i];
	}

	public static int pre(int num) {
		return pre(head, num);
	}

	public static int pre(int i, int num) {
		if (i == 0) {
			return Integer.MIN_VALUE;
		}
		if (key[i] >= num) {
			return pre(left[i], num);
		} else {
			return Math.max(key[i], pre(right[i], num));
		}
	}

	public static int post(int num) {
		return post(head, num);
	}

	public static int post(int i, int num) {
		if (i == 0) {
			return Integer.MAX_VALUE;
		}
		if (key[i] <= num) {
			return post(right[i], num);
		} else {
			return Math.min(key[i], post(left[i], num));
		}
	}

	public static void clear() {
		Arrays.fill(key, 1, cnt + 1, 0);
		Arrays.fill(height, 1, cnt + 1, 0);
		Arrays.fill(left, 1, cnt + 1, 0);
		Arrays.fill(right, 1, cnt + 1, 0);
		Arrays.fill(count, 1, cnt + 1, 0);
		Arrays.fill(size, 1, cnt + 1, 0);
		cnt = 0;
		head = 0;
	}

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		in.nextToken();
		int m = (int) in.nval;
		for (int i = 1, num; i <= n; i++) {
			in.nextToken();
			num = (int) in.nval;
			add(num);
		}
		int lastAns = 0;
		int ans = 0;
		for (int i = 1, op, x; i <= m; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			x = (int) in.nval ^ lastAns;
			if (op == 1) {
				add(x);
			} else if (op == 2) {
				remove(x);
			} else if (op == 3) {
				lastAns = rank(x);
				ans ^= lastAns;
			} else if (op == 4) {
				lastAns = index(x);
				ans ^= lastAns;
			} else if (op == 5) {
				lastAns = pre(x);
				ans ^= lastAns;
			} else {
				lastAns = post(x);
				ans ^= lastAns;
			}
		}
		out.println(ans);
		clear();
		out.flush();
		out.close();
		br.close();
	}

}