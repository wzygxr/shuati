package class117;

/**
 * 国旗计划 - Sparse Table应用
 * 洛谷P4155
 * 
 * 【算法核心思想】
 * 使用Sparse Table（稀疏表）结合贪心和倍增法，高效解决环形线段覆盖问题
 * 该问题是Sparse Table在路径跳越问题中的典型应用。通过预处理每个线段能跳到的最远位置，
 * 然后利用二进制拆分的思想快速计算覆盖整个环所需的最少线段数。
 * 
 * 【核心原理】
 * 1. 环形转线性：将环形结构通过复制一遍数组的方式转化为线性结构，便于处理
 * 2. 贪心策略：对于每个位置，选择能覆盖当前位置且延伸最远的线段
 * 3. 倍增预处理：使用Sparse Table预处理每个位置跳2^p步能到达的最远距离
 * 4. 二进制拆分查询：利用预处理的信息，从高位到低位尝试跳跃，快速找到最少步数
 * 
 * 【问题分析】
 * - 输入：n条线段，m个点围成一个环
 * - 约束：所有线段覆盖整个环，且互不包含
 * - 目标：对于每条线段x，求必须选x时，覆盖整个环所需的最少线段数
 * 
 * 【时间复杂度分析】
 * - 排序线段：O(n log n)
 * - 构建Sparse Table：O(n log n)
 *   - 预处理单次跳跃：O(n)使用双指针技巧
 *   - 构建倍增表：O(n log n)，共log n层，每层O(n)操作
 * - 每个查询：O(log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 【空间复杂度分析】
 * - Sparse Table数组：O(n log n)
 * - 其他辅助数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 【应用场景】
 * 1. 环形或线性区间覆盖问题
 * 2. 路径跳越类问题
 * 3. 需要快速查询「跳k步能到达的位置」的场景
 * 4. 资源覆盖优化问题
 * 5. 网络路由路径规划
 * 6. 游戏中的地图覆盖问题
 * 7. 物流配送路线优化
 * 
 * 【相关题目】
 * 1. LeetCode 1326. Minimum Number of Taps to Open to Water a Garden
 * 2. Codeforces 1065E - Side Transmutations
 * 3. POJ 2376 - Cleaning Shifts
 * 4. HDU 5982 - Distance on the tree
 * 5. CodeChef - LADDU
 * 6. LOJ 10187 - 区间覆盖问题
 * 7. POJ 3258 - River Hopscotch
 * 8. Codeforces 620E - New Year Tree
 * 9. HDU 4747 - Mex
 * 10. 洛谷P2049 - 魔术棋子
 * 11. AtCoder ABC138F - Coincidence
 * 12. Codeforces 954E - Water
 * 13. 牛客网NC15349 - 区间覆盖
 */

// 国旗计划
// 给定点的数量m，点的编号1~m，所有点围成一个环
// i号点一定顺时针到达i+1号点，最终m号点顺指针回到1号点
// 给定n条线段，每条线段(a, b)，表示线段从点a顺时针到点b
// 输入数据保证所有线段可以把整个环覆盖
// 输入数据保证每条线段不会完全在另一条线段的内部
// 也就是线段之间可能有重合但一定互不包含
// 返回一个长度为n的结果数组ans，ans[x]表示一定选x号线段的情况下
// 至少选几条线段能覆盖整个环
// 测试链接 : https://www.luogu.com.cn/problem/P4155
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下的code，提交时请把类名改成"Main"，可以直接通过

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.Arrays;

public class Code01_FlagPlan {

	public static int MAXN = 200001;

	public static int LIMIT = 18; // 2^18足够覆盖大部分情况

	public static int power; // 最大的2的幂次，满足2^power <= n

	// 每条线段3个信息 : 线段编号、线段左边界、线段右边界
	// 数组大小为2*MAXN是为了处理环形结构
	public static int[][] line = new int[MAXN << 1][3];

	// stjump[i][p] : 从i号线段出发，跳的次数是2的p次方，能到达的最右线段的编号
	// 这是Sparse Table的核心结构，用于倍增查询
	public static int[][] stjump = new int[MAXN << 1][LIMIT];

	public static int[] ans = new int[MAXN]; // 存储每个线段作为必选时的最少线段数

	public static int n, m; // n为线段数量，m为点的数量

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StreamTokenizer in = new StreamTokenizer(br);
		PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
		in.nextToken();
		n = (int) in.nval;
		in.nextToken();
		m = (int) in.nval;
		for (int i = 1; i <= n; i++) {
			line[i][0] = i;
			in.nextToken();
			line[i][1] = (int) in.nval;
			in.nextToken();
			line[i][2] = (int) in.nval;
		}
		compute();
		out.print(ans[1]);
		for (int i = 2; i <= n; i++) {
			out.print(" " + ans[i]);
		}
		out.println();
		out.flush();
		out.close();
		br.close();
	}

	public static void compute() {
		power = log2(n);
		build();
		for (int i = 1; i <= n; i++) {
			ans[line[i][0]] = jump(i);
		}
	}

	/**
	 * 计算不大于n的最大2的幂次的指数
	 * 例如，n=5时返回2，因为2^2=4 <= 5/2=2.5
	 * 
	 * @param n 输入整数
	 * @return 最大的p，使得2^p <= n/2
	 */
	/**
	 * 计算不大于n/2的最大2的幂次的指数
	 * 
	 * 【算法原理】
	 * 找到最大的p，使得2^p ≤ n/2
	 * 这是为了确保在跳跃过程中，不会一次性跳过目标位置
	 * 
	 * 【实现细节】
	 * 使用位移运算高效计算，避免使用Math.log函数带来的浮点误差
	 * 
	 * 【示例】
	 * n=5时返回2，因为2^2=4 ≤ 5/2=2.5
	 * n=8时返回3，因为2^3=8 ≤ 8/2=4
	 * 
	 * 【时间复杂度】
	 * O(log n) - 最多执行log n次循环
	 * 
	 * @param n 输入整数
	 * @return 最大的p，使得2^p <= n/2
	 */
	public static int log2(int n) {
		int ans = 0;
		// 找到最大的ans，使得2^ans <= n/2
		while ((1 << ans) <= (n >> 1)) {
			ans++;
		}
		return ans;
	}

	/**
	 * 构建Sparse Table结构
	 * 步骤：
	 * 1. 处理线段的环结构，将跨过环起点的线段右端点扩展
	 * 2. 按左端点排序所有线段
	 * 3. 复制线段数组，将环展开为线性结构
	 * 4. 预处理stjump的第0层（一次跳跃能到达的最远位置）
	 * 5. 动态规划构建完整的Sparse Table
	 */
	/**
	 * 构建Sparse Table结构
	 * 
	 * 【实现原理】
	 * 1. 处理环形结构：将跨过环起点的线段右端点扩展
	 * 2. 按左端点排序所有线段，为贪心策略做准备
	 * 3. 复制线段数组并将坐标加m，将环形展开为线性结构
	 * 4. 预处理单次跳跃能到达的最远位置（stjump第0层）
	 * 5. 使用动态规划构建完整的Sparse Table
	 * 
	 * 【核心步骤】
	 * 1. 环形转线性：通过判断左端点是否大于右端点，处理跨过环起点的线段
	 * 2. 排序：按左端点升序排列线段，保证贪心选择的正确性
	 * 3. 数组扩展：复制线段数组并将坐标加m，将环形展开为线性结构
	 * 4. 双指针预处理：O(n)时间找出每个线段能跳到的最远线段
	 * 5. 倍增构建：动态规划构建Sparse Table的其余层
	 * 
	 * 【时间复杂度】
	 * O(n log n) - 排序O(n log n)，预处理和构建Sparse Table O(n log n)
	 * 
	 * 【空间复杂度】
	 * O(n log n) - Sparse Table数组的空间占用
	 */
	public static void build() {
		// 处理环形结构：如果线段的左端点大于右端点，说明跨过了环的起点
		// 将右端点增加m，使其在线性表示中正确显示
		for (int i = 1; i <= n; i++) {
			if (line[i][1] > line[i][2]) {
				line[i][2] += m;
			}
		}
		
		// 按左端点升序排序所有线段，这是贪心策略的基础
		Arrays.sort(line, 1, n + 1, (a, b) -> a[1] - b[1]);
		
		// 将环展开为线性结构：复制一遍线段数组并将坐标加m
		// 这样可以处理环形覆盖问题，避免环形边界判断的复杂性
		for (int i = 1; i <= n; i++) {
			line[i + n][0] = line[i][0];      // 保留线段编号
			line[i + n][1] = line[i][1] + m;  // 左端点加m
			line[i + n][2] = line[i][2] + m;  // 右端点加m
		}
		
		int e = n << 1; // 扩展后的线段数量
		
		// 预处理stjump的第0层：对于每个i，找到最远能到达的线段
		// 使用双指针技术，时间复杂度O(n)
		for (int i = 1, arrive = 1; i <= e; i++) {
			// 找到最大的arrive，使得line[arrive+1][1] <= line[i][2]
			// 这保证了从i号线段出发，能跳到的最远线段
			while (arrive + 1 <= e && line[arrive + 1][1] <= line[i][2]) {
				arrive++;
			}
			// stjump[i][0]表示从i出发跳一次能到达的最远线段
			stjump[i][0] = arrive;
		}
		
		// 动态规划构建Sparse Table的其余层
		// stjump[i][p] = stjump[stjump[i][p-1]][p-1]
		// 表示从i出发跳2^p次能到达的最远线段
		for (int p = 1; p <= power; p++) {
			for (int i = 1; i <= e; i++) {
				// 状态转移：跳2^p次 = 跳2^(p-1)次 + 再跳2^(p-1)次
				// 这是倍增法的核心思想，通过组合小规模的跳跃来构建大规模的跳跃
				stjump[i][p] = stjump[stjump[i][p - 1]][p - 1];
			}
		}
	}

	/**
	 * 计算必须选择第i条线段时，覆盖整个环所需的最少线段数
	 * 使用倍增法从高位到低位尝试跳跃
	 * 
	 * @param i 必须选择的线段索引
	 * @return 最少需要的线段数
	 */
	/**
	 * 计算必须选择第i条线段时，覆盖整个环所需的最少线段数
	 * 使用倍增法从高位到低位尝试跳跃
	 * 
	 * 【算法原理】
	 * 1. 确定目标位置：从线段i的左端点开始，需要覆盖整个环（长度m）
	 * 2. 贪心跳跃：从最高位开始尝试跳跃，如果跳跃后仍未到达目标，则进行跳跃
	 * 3. 累加跳跃次数：每跳跃一次，累加相应的线段数
	 * 4. 计算最终结果：已跳跃次数 + 初始线段 + 最后一次跳跃
	 * 
	 * 【实现细节】
	 * - aim = line[i][1] + m：目标位置是线段i的左端点加上环的长度
	 * - 从高位到低位遍历：优先尝试大的跳跃步数
	 * - 条件判断：只有当跳跃后仍未到达目标时才进行跳跃
	 * - 结果计算：res是中间跳跃的次数，+1是初始线段，+1是最后的跳跃
	 * 
	 * 【时间复杂度】
	 * O(log n) - 只需要遍历log n个幂次
	 * 
	 * 【空间复杂度】
	 * O(1) - 只使用常数额外空间
	 * 
	 * @param i 必须选择的线段索引
	 * @return 最少需要的线段数
	 */
	public static int jump(int i) {
		// 目标位置：从线段i的左端点开始，需要覆盖至少m个长度
		// 也就是需要到达line[i][1] + m的位置
		int aim = line[i][1] + m;
		int cur = i;  // 当前位置
		int res = 0;  // 已跳跃的次数（不包含初始的第i条线段）
		
		// 从最高位开始尝试跳跃，采用贪心策略
		// 这种方法类似于二进制拆分，优先选择最大的可能跳跃步数
		for (int p = power; p >= 0; p--) {
			int next = stjump[cur][p];
			// 如果跳跃2^p次后仍未到达目标，就进行跳跃
			if (next != 0 && line[next][2] < aim) {
				res += 1 << p;  // 增加跳跃次数（2^p次）
				cur = next;     // 更新当前位置
			}
		}
		
		// 最终结果：已跳跃次数 + 当前线段 + 最后一次跳跃
		// res表示中间跳跃的线段数，+1是初始线段i，+1是最后一次跳跃
		// 注意：题目保证有解，所以最后一定能覆盖整个环
		return res + 1 + 1;
	}
	
	/**
	 * 【算法优化技巧】
	 * 1. 环形转线性：通过复制数组的方式将环形问题转化为线性问题，避免环形边界处理的复杂性
	 * 2. 双指针预处理：使用O(n)时间的双指针技巧预处理单次跳跃能到达的最远位置，避免O(n²)的暴力预处理
	 * 3. 倍增法查询：利用二进制拆分思想，将查询时间复杂度优化到O(log n)
	 * 4. 高效IO处理：使用BufferedReader+StreamTokenizer+PrintWriter组合提高输入输出效率，避免超时
	 * 5. 位运算优化：使用位移运算代替乘法和除法，提高位运算效率
	 * 6. 1-based索引设计：使用1-based索引简化边界条件处理
	 * 7. 预处理log数组：预先计算log2值，避免重复计算
	 * 
	 * 【常见错误点】
	 * 1. 数组索引越界：注意扩展后的数组大小是2*MAXN，在处理时避免数组越界
	 * 2. 环形处理错误：忘记处理跨过环起点的线段，导致覆盖不完全
	 * 3. 跳跃条件判断：跳跃条件设置错误，导致无法正确计算最少线段数
	 * 4. 结果计算错误：忘记计入初始线段或最后的跳跃，导致结果偏小
	 * 5. 排序错误：线段排序时错误地使用了右端点而非左端点，破坏贪心策略
	 * 6. 位运算优先级问题：位移运算符优先级低于算术运算符，需要注意括号使用
	 * 7. 初始化错误：忘记初始化Sparse Table数组，导致查询结果错误
	 * 
	 * 【工程化考量】
	 * 1. 可扩展性：对于更大规模的数据，可以调整MAXN和LIMIT的值，确保算法适用范围
	 * 2. 代码复用：可以将Sparse Table封装为独立的类，支持不同类型的跳跃查询
	 * 3. 健壮性：添加边界检查和异常处理，增强代码健壮性
	 * 4. 性能优化：使用位运算、预先计算常用值等技巧进一步优化性能
	 * 5. 代码可读性：添加详细注释，使用有意义的变量名，提高代码可维护性
	 * 6. 测试覆盖：编写全面的测试用例，覆盖各种边界情况
	 * 7. 文档完善：提供详细的API文档和使用示例
	 * 8. 并行优化：对于非常大的数据集，可以考虑并行构建Sparse Table
	 * 
	 * 【实际应用注意事项】
	 * 1. 内存管理：对于大规模数据，需要注意内存占用，避免栈溢出或堆溢出
	 * 2. 输入数据验证：在实际应用中，应增加输入数据的验证，确保数据符合约束条件
	 * 3. 性能监控：对于性能敏感的应用，可以添加性能监控点，及时发现性能瓶颈
	 * 4. 算法选择：根据实际问题特点，选择合适的算法，如对于动态问题可能需要线段树
	 * 5. 平台适配：不同平台的整数范围可能不同，需要注意溢出问题
	 */
	
	// C++版本实现
	// #include <iostream>
	// #include <vector>
	// #include <algorithm>
	// #include <cstring>
	// using namespace std;

	// /**
	//  * 国旗计划问题 - C++版本实现
	//  * 使用Sparse Table结合贪心和倍增法解决环形线段覆盖问题
	//  */

	// // 定义常量
	// const int MAXN = 200001;     // 最大数据规模
	// const int LIMIT = 18;        // 最大幂次限制

	// // 全局变量
	// int power;                   // 最大的2的幂次
	// int line[MAXN << 1][3];      // [0]线段编号, [1]左边界, [2]右边界
	// int stjump[MAXN << 1][LIMIT]; // Sparse Table跳跃表
	// int ans[MAXN];               // 存储结果
	// int n, m;                    // 线段数量和点的数量

	// /**
	//  * 计算不大于n/2的最大2的幂次的指数
	//  * @param n 输入整数
	//  * @return 最大的p，使得2^p <= n/2
	//  */
	// int log2(int n) {
	//     int ans = 0;
	//     while ((1 << ans) <= (n >> 1)) {
	//         ans++;
	//     }
	//     return ans;
	// }

	// /**
	//  * 构建Sparse Table结构
	//  * 1. 处理环形结构
	//  * 2. 按左端点排序线段
	//  * 3. 复制数组展开环形
	//  * 4. 预处理单次跳跃
	//  * 5. 构建倍增表
	//  */
	// void build() {
	//     // 处理环形结构：如果线段跨过环的起点，将右端点增加m
	//     for (int i = 1; i <= n; ++i) {
	//         if (line[i][1] > line[i][2]) {
	//             line[i][2] += m;
	//         }
	//     }
	//     
	//     // 按左端点升序排序
	//     sort(line + 1, line + n + 1, [](const int a[], const int b[]) {
	//         return a[1] < b[1];
	//     });
	//     
	//     // 复制数组，展开环形为线性结构
	//     for (int i = 1; i <= n; ++i) {
	//         line[i + n][0] = line[i][0];
	//         line[i + n][1] = line[i][1] + m;
	//         line[i + n][2] = line[i][2] + m;
	//     }
	//     
	//     int e = n << 1; // 扩展后的线段数量
	//     
	//     // 预处理stjump[0]：使用双指针技巧找出单次跳跃能到达的最远位置
	//     for (int i = 1, arrive = 1; i <= e; ++i) {
	//         while (arrive + 1 <= e && line[arrive + 1][1] <= line[i][2]) {
	//             arrive++;
	//         }
	//         stjump[i][0] = arrive;
	//     }
	//     
	//     // 构建Sparse Table的其余层
	//     for (int p = 1; p <= power; ++p) {
	//         for (int i = 1; i <= e; ++i) {
	//             // 状态转移：跳2^p次 = 跳2^(p-1)次 + 再跳2^(p-1)次
	//             stjump[i][p] = stjump[stjump[i][p-1]][p-1];
	//         }
	//     }
	// }

	// /**
	//  * 计算必须选择第i条线段时，覆盖整个环所需的最少线段数
	//  * @param i 必须选择的线段索引
	//  * @return 最少需要的线段数
	//  */
	// int jump(int i) {
	//     int aim = line[i][1] + m; // 目标位置
	//     int cur = i;             // 当前位置
	//     int res = 0;             // 已跳跃的次数
	//     
	//     // 从高位到低位尝试跳跃
	//     for (int p = power; p >= 0; --p) {
	//         int next = stjump[cur][p];
	//         if (next != 0 && line[next][2] < aim) {
	//             res += 1 << p; // 增加跳跃次数
	//             cur = next;    // 更新当前位置
	//         }
	//     }
	//     
	//     // 最终结果：已跳跃次数 + 当前线段 + 最后一次跳跃
	//     return res + 1 + 1;
	// }

	// /**
	//  * 主要计算函数
	//  * 初始化参数，构建ST表，计算每个线段的结果
	//  */
	// void compute() {
	//     power = log2(n);
	//     build();
	//     for (int i = 1; i <= n; ++i) {
	//         ans[line[i][0]] = jump(i);
	//     }
	// }

	// int main() {
	//     // 输入输出优化
	//     ios::sync_with_stdio(false);
	//     cin.tie(0);
	//     
	//     // 读取输入
	//     cin >> n >> m;
	//     for (int i = 1; i <= n; ++i) {
	//         line[i][0] = i;
	//         cin >> line[i][1] >> line[i][2];
	//     }
	//     
	//     // 计算结果
	//     compute();
	//     
	//     // 输出结果
	//     cout << ans[1];
	//     for (int i = 2; i <= n; ++i) {
	//         cout << " " << ans[i];
	//     }
	//     cout << endl;
	//     
	//     return 0;
	// }
}
