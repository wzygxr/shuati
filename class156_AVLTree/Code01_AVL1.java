package class148;

// AVL树的实现(java版)
// 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
// 1，增加x，重复加入算多个词频
// 2，删除x，如果有多个，只删掉一个
// 3，查询x的排名，x的排名为，比x小的数的个数+1
// 4，查询数据中排名为x的数
// 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
// 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
// 所有操作的次数 <= 10^5
// -10^7 <= x <= +10^7
// 测试链接 : https://www.luogu.com.cn/problem/P3369
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有测试用例

/*
 * AVL树实现 (Java版)
 * 时间复杂度: 所有操作均为 O(log n)
 * 空间复杂度: O(n)
 * 
 * 工程化考量：
 * - 使用数组模拟树结构，避免频繁对象创建的开销
 * - 预先分配MAXN空间，提高内存访问效率
 * - 使用快速IO（BufferedReader、StreamTokenizer、PrintWriter）处理大规模数据
 * - 实现clear()方法，支持多次测试用例
 * - 词频计数优化，高效处理重复元素
 */

/*
 * 补充题目列表：
 * 
 * 一、基础模板题（直接应用AVL树实现）：
 * 1. 洛谷 P3369 【模板】普通平衡树
 *    链接: https://www.luogu.com.cn/problem/P3369
 *    题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
 *    核心考点: AVL树基本操作实现，词频处理
 *    适用文件: 当前文件可直接应用，需将类名改为Main
 * 
 * 2. 洛谷 P6136 【模板】普通平衡树（数据加强版）
 *    链接: https://www.luogu.com.cn/problem/P6136
 *    题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
 *    核心考点: AVL树性能优化，大规模数据处理
 *    适用文件: 当前文件需要优化IO效率
 * 
 * 3. PAT甲级 1066 Root of AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
 *    题目描述: 给定插入序列，构建AVL树，输出根节点的值
 *    核心考点: AVL树构建过程，插入操作和旋转维护
 *    适用文件: 基于当前文件修改，专注插入和根节点输出
 * 
 * 二、数据结构应用题（AVL树作为核心组件）：
 * 4. LeetCode 406. Queue Reconstruction by Height
 *    链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
 *    题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
 *    核心考点: AVL树维护有序序列，基于位置插入
 *    适用文件: 可基于当前实现修改，增加位置插入功能
 * 
 * 5. LeetCode 220. Contains Duplicate III
 *    链接: https://leetcode.cn/problems/contains-duplicate-iii/
 *    题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
 *    核心考点: AVL树维护滑动窗口，范围查询
 *    适用文件: 需要扩展支持范围查询功能
 * 
 * 三、算法设计题（需要结合AVL树特性）：
 * 6. PAT甲级 1123 Is It a Complete AVL Tree
 *    链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
 *    题目描述: 判断构建的AVL树是否是完全二叉树
 *    核心考点: AVL树构建 + 完全二叉树判定
 *    适用文件: 需要增加层序遍历和完全二叉树检查
 * 
 * 7. Codeforces 459D - Pashmak and Parmida's problem
 *    链接: https://codeforces.com/problemset/problem/459/D
 *    题目描述: 计算满足条件的点对数量
 *    核心考点: AVL树求逆序对变形
 *    适用文件: 需要基于rank操作进行扩展
 * 
 * 8. SPOJ Ada and Behives
 *    链接: https://www.spoj.com/problems/ADAAPHID/
 *    题目描述: 维护一个动态集合，支持插入和查询操作
 *    核心考点: AVL树基本操作，动态维护
 *    适用文件: 当前文件可直接应用
 * 
 * 四、其他相关题目：
 * 9. LeetCode 98. 验证二叉搜索树
 *    链接: https://leetcode.cn/problems/validate-binary-search-tree/
 *    题目描述: 验证一棵二叉树是否是有效的二叉搜索树
 *    核心考点: 二叉搜索树性质，中序遍历
 *    适用文件: 可扩展当前实现增加验证功能
 * 
 * 10. LeetCode 669. 修剪二叉搜索树
 *     链接: https://leetcode.cn/problems/trim-a-binary-search-tree/
 *     题目描述: 修剪二叉搜索树，使其所有节点值都在[low, high]范围内
 *     核心考点: 二叉搜索树的删除操作扩展
 *     适用文件: 需要扩展删除功能支持范围删除
 * 
 * 11. 洛谷 P1908 逆序对
 *     链接: https://www.luogu.com.cn/problem/P1908
 *     题目描述: 计算一个序列中的逆序对数量
 *     核心考点: 利用AVL树或Fenwick树求逆序对
 *     适用文件: 可基于rank操作实现
 * 
 * 12. 牛客网 NC145 01序列的最小权值
 *     链接: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
 *     题目描述: 维护01序列，支持插入和查询操作
 *     核心考点: 平衡树维护二进制位
 *     适用文件: 需要针对二进制特性进行优化
 * 
 * 13. LeetCode 1382. 将二叉搜索树变平衡
 *     链接: https://leetcode.cn/problems/balance-a-binary-search-tree/
 *     题目描述: 给你一棵二叉搜索树，请你返回一棵平衡后的二叉搜索树
 *     核心考点: BST转AVL树，中序遍历+重构
 *     适用文件: 可基于当前实现扩展
 * 
 * 14. HackerRank Self-Balancing Tree
 *     链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
 *     题目描述: 实现AVL树的插入操作
 *     核心考点: AVL树节点定义和旋转操作
 *     适用文件: 当前文件可直接应用
 * 
 * 15. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
 *     链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
 *     题目描述: 字符串处理问题，可使用平衡树优化
 *     核心考点: 后缀数组+平衡树
 *     适用文件: 需要扩展字符串处理功能
 * 
 * 16. CodeChef ORDERSET
 *     链接: https://www.codechef.com/problems/ORDERSET
 *     题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
 *     核心考点: 平衡树基本操作
 *     适用文件: 当前文件可直接应用
 * 
 * 17. AtCoder ABC134 E - Sequence Decomposing
 *     链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
 *     题目描述: 序列分解问题，可使用平衡树优化
 *     核心考点: LIS变种+平衡树
 *     适用文件: 需要扩展相关功能
 * 
 * 18. ZOJ 1659 Mobile Phone Coverage
 *     链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
 *     题目描述: 计算矩形覆盖面积，可使用平衡树维护
 *     核心考点: 扫描线+平衡树
 *     适用文件: 需要扩展区间处理功能
 * 
 * 19. POJ 1864 [NOI2009] 二叉查找树
 *     链接: http://poj.org/problem?id=1864
 *     题目描述: 二叉查找树的动态规划问题
 *     核心考点: 树形DP+平衡树
 *     适用文件: 需要扩展树形结构处理
 * 
 * 20. HDU 4589 Special equations
 *     链接: http://acm.hdu.edu.cn/showproblem.php?pid=4589
 *     题目描述: 数学问题，可使用平衡树优化
 *     核心考点: 数论+平衡树
 *     适用文件: 需要扩展数学计算功能
 */

/*
 * 算法思路技巧总结：
 * 
 * 一、适用场景与核心思想
 * 1. 适用场景：
 *    - 需要维护动态有序集合，支持快速插入、删除、查找
 *    - 需要高效查询元素排名或第k小元素
 *    - 需要频繁查询前驱和后继元素
 *    - 需要稳定的O(log n)时间复杂度保证
 * 
 * 2. 核心思想：
 *    - 通过旋转操作维持树的平衡性，保证树的高度为O(log n)
 *    - 每个节点维护子树大小和高度信息，支持高效的排名查询
 *    - 插入和删除操作后，通过自底向上的旋转调整恢复平衡
 * 
 * 二、关键操作与实现细节
 * 3. 旋转操作（平衡调整的核心）：
 *    - LL旋转：在左孩子的左子树插入导致失衡，右旋一次
 *    - RR旋转：在右孩子的右子树插入导致失衡，左旋一次
 *    - LR旋转：在左孩子的右子树插入导致失衡，先左旋左孩子，再右旋根节点
 *    - RL旋转：在右孩子的左子树插入导致失衡，先右旋右孩子，再左旋根节点
 * 
 * 4. 自平衡维护策略：
 *    - 插入操作：递归插入后，通过up()更新节点信息，再用maintain()检查平衡因子
 *    - 删除操作：递归删除后，同样需要up()和maintain()维护平衡
 *    - 平衡因子计算：通过比较左右子树高度差，超过1时进行旋转调整
 * 
 * 5. 词频优化：
 *    - 使用count数组记录每个key的出现次数，避免重复节点
 *    - 删除时先减少计数，计数为0时才真正删除节点
 * 
 * 三、算法分析与优化
 * 6. 时间复杂度分析：
 *    - 插入：O(log n) - 树高保证
 *    - 删除：O(log n) - 树高保证
 *    - 查找：O(log n) - 二叉搜索特性
 *    - 查询排名：O(log n) - 基于子树大小累加
 *    - 查询第k小：O(log n) - 基于子树大小二分
 *    - 前驱/后继：O(log n) - 二叉搜索树特性
 * 
 * 7. 空间复杂度分析：
 *    - 总空间：O(n) - 存储节点信息的数组
 *    - 递归栈空间：O(log n) - 递归深度受树高限制
 * 
 * 8. 性能优化技巧：
 *    - 使用数组模拟树结构，避免指针/引用的额外开销
 *    - 预先分配足够空间，减少动态扩容
 *    - 使用快速IO处理大规模数据输入输出
 *    - 批量操作时减少重复计算
 * 
 * 四、工程化与实践要点
 * 9. 异常处理与边界情况：
 *    - 处理空树情况（节点编号为0）
 *    - 处理删除不存在元素的情况
 *    - 处理k超出范围的情况
 * 
 * 10. 调试技巧：
 *    - 添加打印中间状态的辅助方法
 *    - 使用断言验证平衡因子和子树大小的正确性
 *    - 针对插入和删除操作设计小规模测试用例
 * 
 * 11. 与标准库对比：
 *    - Java的TreeSet/TreeMap基于红黑树实现，性能相似
 *    - AVL树比红黑树更严格平衡，但旋转次数可能更多
 *    - 对于读操作频繁的场景，AVL树性能可能略优
 * 
 * 五、语言特性与实现差异
 * 12. Java实现特点：
 *    - 使用静态数组模拟树结构，避免频繁创建对象
 *    - 利用StreamTokenizer提高输入效率
 *    - 数组预分配策略适应Java内存模型
 * 
 * 13. 不同语言实现对比：
 *    - Java: 数组模拟，GC管理内存，IO需优化
 *    - C++: 指针或数组模拟，手动内存管理，直接内存访问
 *    - Python: 对象引用，动态类型，递归深度限制
 * 
 * 14. 高级扩展：
 *    - 持久化AVL树：支持版本控制，可回溯历史状态
 *    - 并发AVL树：支持多线程操作，需加锁或无锁设计
 *    - 区间树/线段树：基于AVL树扩展，支持区间查询和更新
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

/**
 * AVL树实现（Java版）
 * 数据结构与算法：自平衡二叉搜索树
 * 主要特点：通过旋转操作维持树的平衡性，保证所有操作的时间复杂度为O(log n)
 * 支持操作：插入、删除、查询排名、查询第k小、查询前驱、查询后继
 * 实现方式：数组模拟指针，提高内存访问效率
 */
public class Code01_AVL1 {
	// 最大节点数量
	public static int MAXN = 100001;

	// 空间使用计数 - 记录当前分配的节点编号
	public static int cnt = 0;

	// 整棵树的头节点编号 - 初始为0表示空树
	public static int head = 0;

	// 存储节点的键值
	public static int[] key = new int[MAXN];

	// 存储每个节点为根的子树高度
	public static int[] height = new int[MAXN];

	// 存储每个节点的左孩子编号
	public static int[] left = new int[MAXN];

	// 存储每个节点的右孩子编号
	public static int[] right = new int[MAXN];

	// 存储每个节点键值的出现次数
	public static int[] count = new int[MAXN];

	// 存储每个节点为根的子树节点总数（包括重复计数）
	public static int[] size = new int[MAXN];

	/**
	 * 更新节点的子树大小和高度信息
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 节点编号
	 */
	public static void up(int i) {
		// 子树大小 = 左子树大小 + 右子树大小 + 当前节点的计数
		size[i] = size[left[i]] + size[right[i]] + count[i];
		// 树高 = max(左子树高度, 右子树高度) + 1
		height[i] = Math.max(height[left[i]], height[right[i]]) + 1;
	}

	/**
	 * 左旋操作 - 调整树的平衡
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 需要进行左旋的节点编号
	 * @return 左旋后新的子树根节点编号
	 */
	public static int leftRotate(int i) {
		int r = right[i];  // 右孩子作为新的根
		right[i] = left[r];  // 右孩子的左子树成为当前节点的右子树
		left[r] = i;  // 当前节点成为右孩子的左子树
		// 先更新当前节点的信息，再更新新根节点的信息
		up(i);
		up(r);
		return r;  // 返回新的根节点
	}

	/**
	 * 右旋操作 - 调整树的平衡
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @param i 需要进行右旋的节点编号
	 * @return 右旋后新的子树根节点编号
	 */
	public static int rightRotate(int i) {
		int l = left[i];  // 左孩子作为新的根
		left[i] = right[l];  // 左孩子的右子树成为当前节点的左子树
		right[l] = i;  // 当前节点成为左孩子的右子树
		// 先更新当前节点的信息，再更新新根节点的信息
		up(i);
		up(l);
		return l;  // 返回新的根节点
	}

	/**
	 * 维护树的平衡，根据平衡因子进行相应的旋转操作
	 * 时间复杂度: O(1) - 常数次旋转操作
	 * 空间复杂度: O(1)
	 * @param i 需要维护平衡的子树根节点编号
	 * @return 维护平衡后新的子树根节点编号
	 */
	public static int maintain(int i) {
		int lh = height[left[i]];  // 左子树高度
		int rh = height[right[i]];  // 右子树高度
		
		// 左子树比右子树高超过1，需要右旋调整
		if (lh - rh > 1) {
			// LL情况：左孩子的左子树高度 >= 左孩子的右子树高度
			if (height[left[left[i]]] >= height[right[left[i]]]) {
				i = rightRotate(i);
			} else {
				// LR情况：先左旋左孩子，再右旋根节点
				left[i] = leftRotate(left[i]);
				i = rightRotate(i);
			}
		}
		// 右子树比左子树高超过1，需要左旋调整
		else if (rh - lh > 1) {
			// RR情况：右孩子的右子树高度 >= 右孩子的左子树高度
			if (height[right[right[i]]] >= height[left[right[i]]]) {
				i = leftRotate(i);
			} else {
				// RL情况：先右旋右孩子，再左旋根节点
				right[i] = rightRotate(right[i]);
				i = leftRotate(i);
			}
		}
		return i;  // 返回维护后的根节点
	}

	/**
	 * 公共接口：向树中添加一个数字
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param num 要添加的数字
	 */
	public static void add(int num) {
		head = add(head, num);
	}

	/**
	 * 递归实现：向以i为根的子树中添加num
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param num 要添加的数字
	 * @return 添加后的子树根节点编号
	 */
	public static int add(int i, int num) {
		// 空树情况，创建新节点
		if (i == 0) {
			key[++cnt] = num;  // 分配新节点，存储键值
			count[cnt] = size[cnt] = height[cnt] = 1;  // 初始化计数、大小和高度
			return cnt;  // 返回新节点编号
		}
		
		// 键值已存在，增加计数
		if (key[i] == num) {
			count[i]++;
		}
		// 键值小于当前节点，向左子树添加
		else if (key[i] > num) {
			left[i] = add(left[i], num);
		}
		// 键值大于当前节点，向右子树添加
		else {
			right[i] = add(right[i], num);
		}
		
		// 更新当前节点信息
		up(i);
		// 维护树的平衡
		return maintain(i);
	}

	/**
	 * 公共接口：从树中删除一个数字
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param num 要删除的数字
	 */
	public static void remove(int num) {
		// 检查数字是否存在（通过比较num和num+1的排名）
		if (rank(num) != rank(num + 1)) {
			head = remove(head, num);
		}
	}

	/**
	 * 递归实现：从以i为根的子树中删除num
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param num 要删除的数字
	 * @return 删除后的子树根节点编号
	 */
	public static int remove(int i, int num) {
		// 目标值在右子树
		if (key[i] < num) {
			right[i] = remove(right[i], num);
		}
		// 目标值在左子树
		else if (key[i] > num) {
			left[i] = remove(left[i], num);
		}
		// 找到目标节点
		else {
			// 如果计数大于1，只减少计数
			if (count[i] > 1) {
				count[i]--;
			} else {
				// 叶子节点直接删除
				if (left[i] == 0 && right[i] == 0) {
					return 0;
				}
				// 只有左孩子
				else if (left[i] != 0 && right[i] == 0) {
					i = left[i];
				}
				// 只有右孩子
				else if (left[i] == 0 && right[i] != 0) {
					i = right[i];
				}
				// 有两个孩子，找到右子树的最小节点（后继）
				else {
					int mostLeft = right[i];
					// 找右子树的最左节点
					while (left[mostLeft] != 0) {
						mostLeft = left[mostLeft];
					}
					// 删除右子树中的后继节点
					right[i] = removeMostLeft(right[i], mostLeft);
					// 将后继节点作为新的根，接管左右子树
					left[mostLeft] = left[i];
					right[mostLeft] = right[i];
					i = mostLeft;
				}
			}
		}
		// 更新当前节点信息
		up(i);
		// 维护树的平衡
		return maintain(i);
	}

	/**
	 * 删除以i为根的子树中的最左节点（mostLeft）
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param mostLeft 要删除的最左节点编号
	 * @return 删除后的子树根节点编号
	 */
	public static int removeMostLeft(int i, int mostLeft) {
		// 找到目标节点
		if (i == mostLeft) {
			return right[i];  // 返回右子树作为新的根
		} else {
			// 递归删除左子树中的最左节点
			left[i] = removeMostLeft(left[i], mostLeft);
			// 更新信息并维护平衡
			up(i);
			return maintain(i);
		}
	}

	/**
	 * 公共接口：查询num的排名（比num小的数的个数+1）
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param num 要查询排名的数字
	 * @return num的排名
	 */
	public static int rank(int num) {
		return small(head, num) + 1;  // 比num小的数的个数+1
	}

	/**
	 * 计算以i为根的子树中比num小的数字个数
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param num 比较基准值
	 * @return 比num小的数字个数
	 */
	public static int small(int i, int num) {
		// 空树返回0
		if (i == 0) {
			return 0;
		}
		// 当前节点值大于等于num，继续在左子树查找
		if (key[i] >= num) {
			return small(left[i], num);
		} else {
			// 当前节点值小于num，加上左子树所有节点和当前节点计数，继续在右子树查找
			return size[left[i]] + count[i] + small(right[i], num);
		}
	}

	/**
	 * 公共接口：查询排名为x的数字
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param x 排名（从1开始）
	 * @return 排名为x的数字
	 */
	public static int index(int x) {
		return index(head, x);
	}

	/**
	 * 递归实现：在以i为根的子树中查询排名为x的数字
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param x 排名（从1开始）
	 * @return 排名为x的数字
	 */
	public static int index(int i, int x) {
		// 目标在左子树
		if (size[left[i]] >= x) {
			return index(left[i], x);
		}
		// 目标在右子树
		else if (size[left[i]] + count[i] < x) {
			return index(right[i], x - size[left[i]] - count[i]);
		}
		// 目标就是当前节点
		return key[i];
	}

	/**
	 * 公共接口：查询num的前驱（小于num的最大数）
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param num 基准值
	 * @return 前驱值，如果不存在返回Integer.MIN_VALUE
	 */
	public static int pre(int num) {
		return pre(head, num);
	}

	/**
	 * 递归实现：在以i为根的子树中查询num的前驱
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param num 基准值
	 * @return 前驱值，如果不存在返回Integer.MIN_VALUE
	 */
	public static int pre(int i, int num) {
		// 空树返回最小值
		if (i == 0) {
			return Integer.MIN_VALUE;
		}
		// 当前节点值大于等于num，继续在左子树查找
		if (key[i] >= num) {
			return pre(left[i], num);
		} else {
			// 当前节点值小于num，比较当前节点和右子树的前驱
			return Math.max(key[i], pre(right[i], num));
		}
	}

	/**
	 * 公共接口：查询num的后继（大于num的最小数）
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param num 基准值
	 * @return 后继值，如果不存在返回Integer.MAX_VALUE
	 */
	public static int post(int num) {
		return post(head, num);
	}

	/**
	 * 递归实现：在以i为根的子树中查询num的后继
	 * 时间复杂度: O(log n)
	 * 空间复杂度: O(log n) - 递归栈空间
	 * @param i 当前子树根节点编号
	 * @param num 基准值
	 * @return 后继值，如果不存在返回Integer.MAX_VALUE
	 */
	public static int post(int i, int num) {
		// 空树返回最大值
		if (i == 0) {
			return Integer.MAX_VALUE;
		}
		// 当前节点值小于等于num，继续在右子树查找
		if (key[i] <= num) {
			return post(right[i], num);
		} else {
			// 当前节点值大于num，比较当前节点和左子树的后继
			return Math.min(key[i], post(left[i], num));
		}
	}

	/**
	 * 清空树结构，重置所有状态
	 * 时间复杂度: O(n) - n为已分配的节点数量
	 * 空间复杂度: O(1)
	 */
	public static void clear() {
		// 重置所有已使用的节点信息
		Arrays.fill(key, 1, cnt + 1, 0);
		Arrays.fill(height, 1, cnt + 1, 0);
		Arrays.fill(left, 1, cnt + 1, 0);
		Arrays.fill(right, 1, cnt + 1, 0);
		Arrays.fill(count, 1, cnt + 1, 0);
		Arrays.fill(size, 1, cnt + 1, 0);
		// 重置节点计数器和头节点
		cnt = 0;
		head = 0;
	}
	
	/**
	 * 测试用主方法：用于验证AVL树的各项功能
	 * 测试场景：基本插入、删除、查询操作，边界情况，重复元素处理，极端值测试
	 */
	public static void testMain() throws IOException {
		// 测试用例1：基本功能测试
		System.out.println("=== 测试用例1：基本功能测试 ===");
		clear(); // 确保树为空
		
		// 插入元素
		add(10);
		add(5);
		add(15);
		add(3);
		add(7);
		add(13);
		add(18);
		
		// 测试查询操作
		System.out.println("元素10的排名: " + rank(10)); // 应输出4
		System.out.println("排名为4的元素: " + index(4)); // 应输出10
		System.out.println("元素9的前驱: " + pre(9)); // 应输出7
		System.out.println("元素9的后继: " + post(9)); // 应输出10
		
		// 测试删除操作
		remove(10);
		System.out.println("删除10后，元素10的排名: " + rank(10)); // 应大于当前元素数量
		System.out.println("删除10后，排名为4的元素: " + index(4)); // 应输出13
		
		// 测试用例2：重复元素处理
		System.out.println("\n=== 测试用例2：重复元素处理 ===");
		clear();
		
		add(5);
		add(5);
		add(5);
		add(3);
		add(7);
		
		System.out.println("元素5的排名: " + rank(5)); // 应输出2
		remove(5);
		System.out.println("删除一次5后，元素5的排名: " + rank(5)); // 应仍为2
		remove(5);
		System.out.println("再删除一次5后，元素5的排名: " + rank(5)); // 应大于当前元素数量
		
		// 测试用例3：边界情况测试
		System.out.println("\n=== 测试用例3：边界情况测试 ===");
		clear();
		
		add(1);
		System.out.println("空树插入1后，排名为1的元素: " + index(1)); // 应输出1
		remove(1);
		System.out.println("删除唯一元素后，元素1的前驱: " + pre(1)); // 应输出Integer.MIN_VALUE
		System.out.println("删除唯一元素后，元素1的后继: " + post(1)); // 应输出Integer.MAX_VALUE
		
		// 测试用例4：极端值测试
		System.out.println("\n=== 测试用例4：极端值测试 ===");
		clear();
		
		add(Integer.MAX_VALUE);
		add(Integer.MIN_VALUE);
		System.out.println("Integer.MIN_VALUE的排名: " + rank(Integer.MIN_VALUE)); // 应输出1
		System.out.println("Integer.MAX_VALUE的排名: " + rank(Integer.MAX_VALUE)); // 应输出2
		System.out.println("Integer.MIN_VALUE的后继: " + post(Integer.MIN_VALUE)); // 应输出Integer.MAX_VALUE
		
		// 测试用例5：大规模数据性能测试
		System.out.println("
=== 测试用例5：大规模数据性能测试 ===");
		clear();
		
		long startTime = System.currentTimeMillis();
		// 插入10000个随机数
		for (int i = 0; i < 10000; i++) {
			add((int)(Math.random() * 100000));
		}
		long insertTime = System.currentTimeMillis() - startTime;
		System.out.println("插入10000个随机数耗时: " + insertTime + "ms");
		
		startTime = System.currentTimeMillis();
		// 查询1000次
		for (int i = 0; i < 1000; i++) {
			rank((int)(Math.random() * 100000));
		}
		long queryTime = System.currentTimeMillis() - startTime;
		System.out.println("查询1000次耗时: " + queryTime + "ms");
		
		// 测试用例6：有序序列插入测试
		System.out.println("
=== 测试用例6：有序序列插入测试 ===");
		clear();
		
		startTime = System.currentTimeMillis();
		// 插入有序序列（最坏情况测试）
		for (int i = 1; i <= 1000; i++) {
			add(i);
		}
		long sortedInsertTime = System.currentTimeMillis() - startTime;
		System.out.println("有序序列插入1000个数耗时: " + sortedInsertTime + "ms");
		System.out.println("树高度: " + height[head]);
		System.out.println("理论最小高度: " + (int)(Math.log(1000) / Math.log(2)));
	}
	
	/**
	 * 扩展功能：验证二叉搜索树的有效性
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(h) - 递归栈空间
	 * @return 如果是有效的BST返回true，否则返回false
	 */
	public static boolean isValidBST() {
		return isValidBST(head, Long.MIN_VALUE, Long.MAX_VALUE);
	}
	
	/**
	 * 递归验证BST有效性
	 * @param i 当前节点编号
	 * @param min 允许的最小值
	 * @param max 允许的最大值
	 * @return 子树是否是有效的BST
	 */
	private static boolean isValidBST(int i, long min, long max) {
		if (i == 0) return true;
		if (key[i] <= min || key[i] >= max) return false;
		return isValidBST(left[i], min, key[i]) && isValidBST(right[i], key[i], max);
	}
	
	/**
	 * 扩展功能：计算树的高度
	 * 时间复杂度: O(1) - 直接返回存储的高度信息
	 * 空间复杂度: O(1)
	 * @return 树的高度
	 */
	public static int getHeight() {
		return height[head];
	}
	
	/**
	 * 扩展功能：获取树中元素总数（包括重复计数）
	 * 时间复杂度: O(1)
	 * 空间复杂度: O(1)
	 * @return 树中元素总数
	 */
	public static int getTotalSize() {
		return size[head];
	}
	
	/**
	 * 扩展功能：获取树中不同元素的数量
	 * 时间复杂度: O(n) - 需要遍历所有节点
	 * 空间复杂度: O(h) - 递归栈空间
	 * @return 不同元素的数量
	 */
	public static int getDistinctCount() {
		return getDistinctCount(head);
	}
	
	private static int getDistinctCount(int i) {
		if (i == 0) return 0;
		return 1 + getDistinctCount(left[i]) + getDistinctCount(right[i]);
	}
	
	/**
	 * 扩展功能：中序遍历输出所有元素（有序）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(h) - 递归栈空间
	 */
	public static void inorderTraversal() {
		inorderTraversal(head);
		System.out.println();
	}
	
	private static void inorderTraversal(int i) {
		if (i == 0) return;
		inorderTraversal(left[i]);
		for (int j = 0; j < count[i]; j++) {
			System.out.print(key[i] + " ");
		}
		inorderTraversal(right[i]);
	}
	
	/**
	 * 扩展功能：层序遍历输出树结构（用于调试）
	 * 时间复杂度: O(n)
	 * 空间复杂度: O(n) - 队列空间
	 */
	public static void levelOrderTraversal() {
		if (head == 0) {
			System.out.println("空树");
			return;
		}
		
		java.util.Queue<Integer> queue = new java.util.LinkedList<>();
		queue.offer(head);
		
		while (!queue.isEmpty()) {
			int levelSize = queue.size();
			for (int i = 0; i < levelSize; i++) {
				int node = queue.poll();
				System.out.print("(" + key[node] + ",h=" + height[node] + ",s=" + size[node] + ",c=" + count[node] + ") ");
				
				if (left[node] != 0) queue.offer(left[node]);
				if (right[node] != 0) queue.offer(right[node]);
			}
			System.out.println();
		}
	}

	/**
	 * 标准输入输出主方法：用于洛谷题目提交
	 * 支持多组操作，每组操作对应AVL树的基本功能
	 */
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		int n = (int) in.nval;
		for (int i = 1, op, x; i <= n; i++) {
			in.nextToken();
			op = (int) in.nval;
			in.nextToken();
			x = (int) in.nval;
			if (op == 1) {
				add(x);
			} else if (op == 2) {
				remove(x);
			} else if (op == 3) {
				out.println(rank(x));
			} else if (op == 4) {
				out.println(index(x));
			} else if (op == 5) {
				out.println(pre(x));
			} else {
				out.println(post(x));
			}
		}
		clear();
		out.flush();
		out.close();
		br.close();
	}
}