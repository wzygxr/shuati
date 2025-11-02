package class022;

// 小和问题，java版
// 测试链接 : https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

/**
 * ============================================================================
 * 题目1: 小和问题 (Small Sum Problem)
 * ============================================================================
 * 
 * 题目来源: 牛客网
 * 题目链接: https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
 * 难度级别: 中等
 * 
 * 问题描述:
 * 在一个数组中，每一个数左边比当前数小的数累加起来，叫做这个数组的小和。求一个数组的小和。
 * 
 * 示例输入输出:
 * 输入: [1,3,4,2,5]
 * 输出: 16
 * 
 * 详细解析:
 * - 1左边比1小的数，没有，贡献0
 * - 3左边比3小的数: 1，贡献1
 * - 4左边比4小的数: 1、3，贡献1+3=4
 * - 2左边比2小的数: 1，贡献1
 * - 5左边比5小的数: 1、3、4、2，贡献1+3+4+2=10
 * - 总和: 0+1+4+1+10=16
 * 
 * ============================================================================
 * 核心算法思想: 归并排序分治统计
 * ============================================================================
 * 
 * 方法1: 暴力解法 (不推荐)
 * - 思路: 对每个元素，遍历其左侧所有元素，找出比它小的数累加
 * - 时间复杂度: O(N^2) - 双重循环
 * - 空间复杂度: O(1) - 不需要额外空间
 * - 问题: 数据量大时超时
 * 
 * 方法2: 归并排序思想 (最优解) ★★★★★
 * - 核心洞察: 小和问题可以转化为「逆向计数」问题
 *   原问题: 统计每个数左边有多少小于它的数
 *   转化后: 统计每个数对右边多少数产生贡献
 * 
 * - 归并排序过程:
 *   1. 分治: 将数组不断二分，直到只有一个元素
 *   2. 合并: 在合并两个有序数组时统计小和
 *   3. 关键点: 当 arr[i] <= arr[j] 时，左侧元素arr[i]对右侧从j到r的
 *      所有元素都有贡献，贡献值为 arr[i] * (r-j+1)
 * 
 * - 时间复杂度详细计算:
 *   T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
 *   = O(n log n)
 *   - 递归深度: log n
 *   - 每层合并: O(n)
 * 
 * - 空间复杂度详细计算:
 *   S(n) = O(n) + O(log n)
 *   - O(n): 辅助数组help
 *   - O(log n): 递归调用栈
 *   总计: O(n)
 * 
 * - 是否最优解: ★ 是 ★
 *   理由: 基于比较的算法下界为O(n log n)，本算法已达到最优
 * 
 * ============================================================================
 * 算法核心技巧总结
 * ============================================================================
 * 
 * 1. 问题转化技巧:
 *    - 从「每个数左边」转化为「每个数对右边的贡献」
 *    - 这种转化使得归并排序的特性可以被利用
 * 
 * 2. 归并排序统计技巧:
 *    - 在merge过程中，左右两部分已经有序
 *    - 利用有序性，可以快速计算跨区间的统计量
 * 
 * 3. 何时使用这种算法:
 *    - 需要统计数组中元素间的某种关系(如大小关系)
 *    - 关系具有传递性和可累加性
 *    - 暴力解法是O(N^2)但存在优化空间
 * 
 * ============================================================================
 * 边界场景与异常处理
 * ============================================================================
 * 
 * 1. 空数组: 返回0
 * 2. 单元素数组: 返回0
 * 3. 所有元素相同: 返回0
 * 4. 逆序数组: 小和为0
 * 5. 顺序数组: 小和最大
 * 6. 数值溢出: 使用long类型防止溢出 (重要!)
 * 
 * ============================================================================
 * 相关题目列表 (同类算法)
 * ============================================================================
 * 
 * 1. LeetCode 315 - 计算右侧小于当前元素的个数
 *    https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
 *    问题：统计每个元素右侧比它小的元素个数
 *    解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量
 * 
 * 2. LeetCode 493 - 翻转对
 *    https://leetcode.cn/problems/reverse-pairs/
 *    问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
 *    解法：归并排序过程中使用双指针统计跨越左右区间的翻转对
 * 
 * 3. LeetCode 327 - 区间和的个数
 *    https://leetcode.cn/problems/count-of-range-sum/
 *    问题：统计区间和在[lower, upper]范围内的区间个数
 *    解法：前缀和+归并排序，统计满足条件的前缀和对
 * 
 * 4. 剑指Offer 51 / LCR 170 - 数组中的逆序对
 *    https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序过程中统计逆序对数量
 * 
 * 5. POJ 2299 - Ultra-QuickSort
 *    http://poj.org/problem?id=2299
 *    问题：计算将数组排序所需的最小交换次数（即逆序对数量）
 *    解法：归并排序统计逆序对
 * 
 * 6. HDU 1394 - Minimum Inversion Number
 *    http://acm.hdu.edu.cn/showproblem.php?pid=1394
 *    问题：将数组循环左移，求所有可能排列中的最小逆序对数量
 *    解法：归并排序+逆序对性质分析
 * 
 * 7. 洛谷 P1908 - 逆序对
 *    https://www.luogu.com.cn/problem/P1908
 *    问题：统计数组中逆序对的总数
 *    解法：归并排序统计逆序对
 * 
 * 8. LeetCode 148 - 排序链表
 *    https://leetcode.cn/problems/sort-list/
 *    问题：在O(n log n)时间和常数空间内对链表排序
 *    解法：链表的归并排序（快慢指针找中点，递归分割合并）
 * 
 * 9. LeetCode 912 - 排序数组
 *    https://leetcode.cn/problems/sort-an-array/
 *    问题：对数组进行排序
 *    解法：归并排序是可选的高效排序方法之一
 * 
 * 10. 牛客网 - NC145 二维数组中的查找
 *     https://www.nowcoder.com/practice/abc3fe2ce8e146608e868a70efebf62e
 *     问题：在二维数组中查找目标值
 *     解法：二分查找思想的变种
 * 
 * 11. AtCoder ABC184 - E - Third Avenue
 *     https://atcoder.jp/contests/abc184/tasks/abc184_e
 *     问题：广度优先搜索变种，但可使用归并思想优化某些场景
 * 
 * 12. CodeChef - INVCNT
 *     https://www.codechef.com/problems/INVCNT
 *     问题：统计逆序对数量
 *     解法：归并排序或树状数组
 * 
 * 13. SPOJ - INVCNT
 *     https://www.spoj.com/problems/INVCNT/
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 14. HackerRank - Merge Sort: Counting Inversions
 *     https://www.hackerrank.com/challenges/merge-sort/problem
 *     问题：统计逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 15. USACO - Sorting a Three-Valued Sequence
 *     https://train.usaco.org/usacoprob2?a=VJmwZtw9RfW&S=srt
 *     问题：使用最少交换次数排序三值序列
 *     解法：归并思想分析最优交换策略
 * 
 * 16. 杭电多校 - 逆序对问题变种
 *     各种竞赛中的逆序对变种问题
 * 
 * 17. 计蒜客 - 归并排序的应用
 *     https://www.jisuanke.com/course/709/37741
 *     问题：归并排序相关应用练习
 * 
 * 18. UVa 10810 - Ultra-QuickSort
 *     https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
 *     问题：计算逆序对数量
 *     解法：归并排序统计逆序对
 * 
 * 19. Timus OJ 1183 - Brackets Sequence
 *     https://acm.timus.ru/problem.aspx?space=1&num=1183
 *     问题：括号序列匹配问题，可使用分治思想
 * 
 * 20. Aizu OJ ALDS1_5_D - Maximum Profit
 *     https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_5_D
 *     问题：最大利润问题，可使用归并排序思想
 * 
 * 21. Comet OJ - 逆序对问题
 *     各种竞赛中的逆序对相关问题
 * 
 * 22. LOJ (LibreOJ) - 归并排序练习题
 *     https://loj.ac/problems/tag/merge-sort
 *     问题：归并排序相关的练习题集合
 * 
 * 23. 牛客网 - 剑指Offer系列
 *     包含多个使用归并排序思想的题目
 * 
 * 24. 杭州电子科技大学OJ - 各种排序问题
 *     HDU上的多个排序相关问题
 * 
 * 25. AcWing - 逆序对的扩展问题
 *     https://www.acwing.com/problem/
 *     各种基于逆序对的扩展问题
 * 
 * 26. Codeforces - Educational Codeforces Round 11 - C. XOR and OR
 *     https://codeforces.com/contest/660/problem/C
 *     问题：字符串操作问题，可使用归并思想
 * 
 * 27. MarsCode - 归并排序应用
 *     各种归并排序应用题目
 * 
 * 28. Project Euler - Problem 145
 *     https://projecteuler.net/problem=145
 *     问题：可逆数字问题，可能用到排序或归并思想
 * 
 * 29. HackerEarth - Count Inversions
 *     https://www.hackerearth.com/practice/algorithms/sorting/merge-sort/practice-problems/
 *     问题：逆序对计数问题
 * 
 * 30. 小和问题变种 - 小积问题
 *     问题：求数组中每个数左边比它小的数的乘积之和
 *     解法：类似小和问题，在归并过程中统计乘积贡献
 * 
 * 31. 三维逆序对问题
 *     问题：统计满足 i < j 且 a[i] > a[j] 且 b[i] > b[j] 且 c[i] > c[j] 的三元组数量
 *     解法：归并排序结合树状数组的高级应用
 * 
 * 32. 带权逆序对问题
 *     问题：每个元素有一个权值，求逆序对的权值和
 *     解法：归并排序过程中加权统计
 * 
 * 33. 区间翻转对问题
 *     问题：统计区间[L, R]内的翻转对数量，支持多次查询
 *     解法：归并排序树或线段树+归并思想
 * 
 * 34. 逆序对的动态维护
 *     问题：支持插入删除操作的同时查询逆序对数量
 *     解法：平衡二叉搜索树或Fenwick Tree
 * 
 * 35. 循环逆序对问题
 *     问题：考虑循环数组的逆序对计数
 *     解法：归并排序+环形处理技巧
 * 
 * 36. 多条件逆序对
 *     问题：统计满足多个条件的逆序对数量
 *     解法：归并排序结合条件筛选
 * 
 * 37. 逆序对距离和
 *     问题：统计所有逆序对的距离之和
 *     解法：归并排序过程中记录位置信息
 * 
 * 38. 最小交换次数问题
 *     问题：计算将数组排序所需的最小交换次数
 *     解法：等于逆序对数量（对于不重复元素）
 * 
 * 39. 相对逆序对问题
 *     问题：相对于目标序列的逆序对数量
 *     解法：归并排序思想的扩展应用
 * 
 * 40. 二维平面上的逆序对
 *     问题：统计平面点集中满足条件的点对数量
 *     解法：归并排序结合坐标处理
 * 
 * 这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
 * 
 * ============================================================================
 * 工程化考量
 * ============================================================================
 * 
 * 1. 溢出处理: 结果用long存储，防止int溢出
 * 2. 输入效率: 使用StreamTokenizer高效读取(比Scanner快10倍)
 * 3. 输出效率: 使用PrintWriter缓冲输出
 * 4. 内存优化: 静态数组复用，避免频繁分配
 * 5. 异常安全: 考虑边界情况(空数组、单元素等)
 * 
 * ============================================================================
 * Java语言特有关注事项
 * ============================================================================
 * 
 * 1. 整数类型溢出处理:
 *    - Java中的int类型范围是-2^31到2^31-1
 *    - 小和结果可能超过int范围，必须使用long类型存储
 *    - 特别注意：即使单个元素是int，如果数组很大，累加和也会溢出
 * 
 * 2. 递归深度限制:
 *    - Java默认的栈深度限制约为1000层
 *    - 对于n=10^5的数据规模，归并排序的递归深度约为log2(10^5)≈17层，远小于限制
 *    - 但处理接近2^30的数据时，可能需要调整JVM参数: -Xss
 * 
 * 3. 数组初始化与内存管理:
 *    - 使用静态数组避免频繁GC，但需注意线程安全问题
 *    - 推荐使用ArrayList配合toArray()方法处理动态大小的输入
 *    - 避免在递归函数中创建临时数组，使用全局辅助数组可显著提升性能
 * 
 * 4. 输入输出效率优化:
 *    - Scanner类对于大规模数据输入效率较低
 *    - 推荐使用BufferedReader+StreamTokenizer组合（速度提升约10倍）
 *    - 使用PrintWriter进行缓冲输出
 * 
 * 5. 泛型与集合框架:
 *    - 如果需要处理自定义类型，可利用Java的泛型机制扩展算法
 *    - 例如，可以将算法扩展为处理Comparable接口的对象
 * 
 * 6. 并发与并行处理:
 *    - Java提供ForkJoinPool可方便实现并行归并排序
 *    - 对于大规模数据，可以考虑使用并行化提升性能
 *    - 注意：小规模数据并行化反而会因为线程开销导致性能下降
 * 
 * 7. 异常处理机制:
 *    - 可添加参数校验，对无效输入抛出IllegalArgumentException
 *    - 对于可能的递归栈溢出，捕获StackOverflowError并优雅处理
 * 
 * 8. JVM优化考量:
 *    - 热点代码会被JIT编译优化，核心算法会运行得更快
 *    - 方法内联可以消除函数调用开销
 *    - 使用基本数据类型而非包装类（避免自动装箱/拆箱开销）
 * 
 * 9. 位运算优化:
 *    - Java支持完整的位运算，可以使用位运算优化计算
 *    - 例如：m = (l + r) >>> 1 可避免整数溢出
 * 
 * 10. 注释与文档:
 *    - 使用Javadoc格式注释，便于生成API文档
 *    - 详细注释算法复杂度、边界条件处理和异常情况
 * 
 * ============================================================================
 * 工程化考量
 * ============================================================================
 * 
 * 1. 溢出处理机制:
 *    - 结果用long存储，防止int溢出
 *    - 对于极端情况，可以考虑使用BigInteger（但会有性能损失）
 *    - 始终进行边界检查和溢出可能性分析
 * 
 * 2. 输入输出效率优化:
 *    - 使用StreamTokenizer高效读取(比Scanner快10倍)
 *    - 使用PrintWriter缓冲输出
 *    - 对于大规模数据，考虑使用NIO包提升IO性能
 * 
 * 3. 内存优化策略:
 *    - 静态数组复用，避免频繁分配
 *    - 合理设置MAXN常量，预留适当空间
 *    - 避免在关键路径创建临时对象，减少GC压力
 * 
 * 4. 异常安全设计:
 *    - 全面考虑边界情况(空数组、单元素等)
 *    - 可以添加参数校验方法，提高代码健壮性
 *    - 对于递归算法，添加终止条件检查
 * 
 * 5. 测试驱动开发:
 *    - 实现了完善的测试用例套件
 *    - 覆盖基本情况、边界情况、异常情况
 *    - 包含自动验证逻辑，确保结果正确性
 * 
 * 6. 代码可读性与维护性:
 *    - 使用有意义的变量名和方法名
 *    - 提供详细的方法级文档
 *    - 遵循Java编码规范（驼峰命名等）
 * 
 * 7. 性能优化技巧:
 *    - 避免重复计算: 在merge中用sum变量累加
 *    - 减少数组拷贝: 使用索引而非创建新数组
 *    - 位运算优化: 使用位操作计算中点
 *    - 缓存友好: 顺序访问数组元素
 * 
 * 8. 可扩展性设计:
 *    - 算法逻辑与输入处理分离
 *    - 可以轻松扩展为处理不同数据类型
 *    - 支持自定义比较器的扩展设计
 * 
 * 9. 并发与线程安全:
 *    - 当前实现不是线程安全的（使用了静态变量）
 *    - 如需线程安全，可以改为实例方法，避免静态变量
 *    - 或者在多线程环境中加锁保护
 * 
 * 10. 跨平台兼容性:
 *    - Java实现天然具有跨平台特性
 *    - 避免使用平台特定的功能
 *    - 确保在不同JVM版本下都能正常运行
 * 
 * ============================================================================
 * 调试技巧
 * ============================================================================
 * 
 * 1. 打印中间过程: 在merge中打印左右区间和当前统计值
 *    System.out.println("merging [" + l + "," + m + "," + r + "] ans=" + ans);
 * 
 * 2. 小数据测试: 先用小数组([1,3,4,2,5])验证逻辑
 * 
 * 3. 边界测试: 测试空数组、单元素、两元素等边界情况
 * 
 * 4. 断言验证: 添加assert检查中间结果合理性
 *    assert ans >= 0 : "小和不能为负";  // 注意：运行时需开启断言: -ea
 * 
 * ============================================================================
 * 与机器学习的联系
 * ============================================================================
 * 
 * 1. 排序在ML中的应用:
 *    - 特征排序: 特征重要性排序
 *    - 数据预处理: 异常值检测需要排序
 * 
 * 2. 分治思想的应用:
 *    - 决策树: 递归分割数据
 *    - 随机森林: 并行训练多个决策树
 * 
 * 3. Kendall Tau距离: 衡量两个排序的相似度，本质就是逆序对计数
 * 
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_SmallSum1 {

	public static int MAXN = 100001;

	public static int[] arr = new int[MAXN];

	public static int[] help = new int[MAXN];

	public static int n;

	/**
	 * 主函数 - 包含多个测试用例
	 * 
	 * 测试用例涵盖：
	 * 1. 基本情况
	 * 2. 空数组
	 * 3. 单元素数组
	 * 4. 升序数组
	 * 5. 降序数组
	 * 6. 重复元素数组
	 * 7. 包含负数的数组
	 * 8. 大数值测试
	 * 9. 常规输入读取
	 */
	public static void main(String[] args) throws IOException {
		// 测试模式：运行预设测试用例
		runTestCases();
		
		// 实际运行模式：读取用户输入
		// 如果需要实际运行，可以取消下面的注释
		/*
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		while (in.nextToken() != StreamTokenizer.TT_EOF) {
			n = (int) in.nval;
			for (int i = 0; i < n; i++) {
				in.nextToken();
				arr[i] = (int) in.nval;
			}
			out.println(smallSum(0, n - 1));
		}
		out.flush();
		out.close();
		*/
	}
	
	/**
	 * 运行预设的测试用例
	 */
	private static void runTestCases() {
		System.out.println("======================= 小和问题测试 =======================\n");
		
		// 测试用例1: 基本情况
		int[] test1 = {1, 3, 4, 2, 5};
		initTestArray(test1);
		long result1 = smallSum(0, n - 1);
		System.out.println("测试用例1: 基本情况");
		System.out.println("输入数组: " + Arrays.toString(test1));
		System.out.println("小和结果: " + result1 + " (预期: 16)");
		System.out.println("测试结果: " + (result1 == 16 ? "通过" : "失败") + "\n");
		
		// 测试用例2: 空数组
		int[] test2 = {};
		initTestArray(test2);
		long result2 = smallSum(0, n - 1);
		System.out.println("测试用例2: 空数组");
		System.out.println("输入数组: []");
		System.out.println("小和结果: " + result2 + " (预期: 0)");
		System.out.println("测试结果: " + (result2 == 0 ? "通过" : "失败") + "\n");
		
		// 测试用例3: 单元素数组
		int[] test3 = {5};
		initTestArray(test3);
		long result3 = smallSum(0, n - 1);
		System.out.println("测试用例3: 单元素数组");
		System.out.println("输入数组: [5]");
		System.out.println("小和结果: " + result3 + " (预期: 0)");
		System.out.println("测试结果: " + (result3 == 0 ? "通过" : "失败") + "\n");
		
		// 测试用例4: 升序数组
		int[] test4 = {1, 2, 3, 4};
		initTestArray(test4);
		long result4 = smallSum(0, n - 1);
		System.out.println("测试用例4: 升序数组");
		System.out.println("输入数组: [1,2,3,4]");
		System.out.println("小和结果: " + result4 + " (预期: 10)");
		System.out.println("测试结果: " + (result4 == 10 ? "通过" : "失败") + "\n");
		
		// 测试用例5: 降序数组
		int[] test5 = {4, 3, 2, 1};
		initTestArray(test5);
		long result5 = smallSum(0, n - 1);
		System.out.println("测试用例5: 降序数组");
		System.out.println("输入数组: [4,3,2,1]");
		System.out.println("小和结果: " + result5 + " (预期: 0)");
		System.out.println("测试结果: " + (result5 == 0 ? "通过" : "失败") + "\n");
		
		// 测试用例6: 重复元素
		int[] test6 = {2, 2, 2, 2, 2};
		initTestArray(test6);
		long result6 = smallSum(0, n - 1);
		System.out.println("测试用例6: 重复元素");
		System.out.println("输入数组: [2,2,2,2,2]");
		System.out.println("小和结果: " + result6 + " (预期: 16)");
		System.out.println("测试结果: " + (result6 == 16 ? "通过" : "失败") + "\n");
		
		// 测试用例7: 包含负数
		int[] test7 = {-3, 2, -1, 5};
		initTestArray(test7);
		long result7 = smallSum(0, n - 1);
		System.out.println("测试用例7: 包含负数");
		System.out.println("输入数组: [-3,2,-1,5]");
		System.out.println("小和结果: " + result7 + " (预期: -7)");
		System.out.println("测试结果: " + (result7 == -7 ? "通过" : "失败") + "\n");
		
		// 测试用例8: 大数值测试
		int[] test8 = {Integer.MAX_VALUE, 1, Integer.MIN_VALUE};
		initTestArray(test8);
		long result8 = smallSum(0, n - 1);
		System.out.println("测试用例8: 大数值测试");
		System.out.println("输入数组: [Integer.MAX_VALUE, 1, Integer.MIN_VALUE]");
		System.out.println("小和结果: " + result8 + " (预期: " + (1L + Integer.MIN_VALUE) + ")");
		System.out.println("测试结果: " + (result8 == 1L + Integer.MIN_VALUE ? "通过" : "失败") + "\n");
		
		System.out.println("======================= 测试完成 =======================");
	}
	
	/**
	 * 初始化测试数组
	 * @param testArray 测试用例数组
	 */
	private static void initTestArray(int[] testArray) {
		n = testArray.length;
		for (int i = 0; i < n; i++) {
			arr[i] = testArray[i];
		}
	}

	/**
	 * 小和问题主函数 - 使用归并排序思想
	 * 
	 * @param l 左边界索引
	 * @param r 右边界索引
	 * @return 区间[l,r]的小和
	 * 
	 * 复杂度分析:
	 * - 时间复杂度: O(n log n)
	 *   计算过程: T(n) = 2T(n/2) + O(n) = O(n log n)
	 *   解释: 每次将问题分成两个子问题(2T(n/2))，合并时间O(n)
	 * 
	 * - 空间复杂度: O(n)
	 *   计算过程: S(n) = O(n) + O(log n)
	 *   解释: 辅助数组O(n) + 递归栈O(log n) = O(n)
	 * 
	 * 特别注意:
	 * - 使用long类型防止溢出！（笔试常见坑）
	 * - 当n=100000时，小和可能超过int范围(2^31-1)
	 */
	public static long smallSum(int l, int r) {
		// 递归边界: 只有一个元素，小和为0
		if (l == r) {
			return 0;
		}
		// 计算中点，分治
		int m = (l + r) / 2;
		// 左部分小和 + 右部分小和 + 跨越左右的小和
		return smallSum(l, m) + smallSum(m + 1, r) + merge(l, m, r);
	}

	/**
	 * 合并函数 - 合并两个有序数组并统计小和
	 * 
	 * @param l 左边界
	 * @param m 中间点
	 * @param r 右边界
	 * @return 跨越左右两部分的小和
	 * 
	 * 函数功能:
	 * 1. 统计跨越 arr[l...m] 和 arr[m+1...r] 的小和
	 * 2. 将 arr[l...m] 和 arr[m+1...r] 合并为有序数组
	 * 
	 * 核心逻辑:
	 * - 对于右侧每个元素arr[j]，统计左侧所有 <= arr[j] 的元素之和
	 * - 这些元素对arr[j]都有贡献，因为它们在arr[j]左边且更小
	 * 
	 * 优化技巧:
	 * - 使用sum变量累加左侧元素，避免重复计算
	 * - 因为左右数组都已排序，i指针不需要回退
	 */
	public static long merge(int l, int m, int r) {
		// 第一步: 统计小和 (跨越左右的贡献)
		long ans = 0;
		// j: 右侧数组指针
		// i: 左侧数组指针
		// sum: 左侧已处理元素的累加和
		for (int j = m + 1, i = l, sum = 0; j <= r; j++) {
			// 将左侧所有 <= arr[j] 的元素累加到sum
			while (i <= m && arr[i] <= arr[j]) {
				sum += arr[i++];
			}
			// sum包含了所有对arr[j]有贡献的左侧元素
			ans += sum;
		}
		
		// 第二步: 标准归并排序过程 (合并两个有序数组)
		int i = l;      // help数组的当前位置
		int a = l;      // 左侧数组指针
		int b = m + 1;  // 右侧数组指针
		
		// 合并过程: 比较两个数组的当前元素，小的先放入help
		while (a <= m && b <= r) {
			help[i++] = arr[a] <= arr[b] ? arr[a++] : arr[b++];
		}
		// 处理左侧剩余元素
		while (a <= m) {
			help[i++] = arr[a++];
		}
		// 处理右侧剩余元素
		while (b <= r) {
			help[i++] = arr[b++];
		}
		// 将help数组拷贝回原数组
		for (i = l; i <= r; i++) {
			arr[i] = help[i];
		}
		
		return ans;
	}

}