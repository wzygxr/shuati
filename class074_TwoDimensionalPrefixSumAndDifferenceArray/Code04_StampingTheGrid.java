package class048;

/**
 * 用邮票贴满网格图问题
 * 
 * 问题描述：
 * 给你一个 m * n 的二进制矩阵 grid，每个格子要么为 0（空）要么为 1（被占据）。
 * 给你邮票的尺寸为 stampHeight * stampWidth。
 * 我们想将邮票贴进二进制矩阵中，且满足以下限制和要求：
 * 1. 覆盖所有空格子
 * 2. 不覆盖任何被占据的格子
 * 3. 可以放入任意数目的邮票，邮票可以相互有重叠部分
 * 4. 邮票不允许旋转，邮票必须完全在矩阵内
 * 如果在满足上述要求的前提下，可以放入邮票，请返回 true，否则返回 false。
 * 
 * 核心思想：
 * 1. 利用二维前缀和快速判断区域是否可放置邮票
 * 2. 利用二维差分数组记录邮票放置情况
 * 3. 贪心策略：能放就放
 * 
 * 算法详解：
 * 1. 预处理：构建原始矩阵的前缀和数组，用于快速查询区域和
 * 2. 枚举：枚举所有可能的邮票放置位置
 * 3. 验证：利用前缀和验证区域是否全为0（可放置）
 * 4. 标记：利用差分数组标记邮票覆盖区域
 * 5. 检查：通过前缀和还原差分数组，检查是否所有空格子都被覆盖
 * 
 * 时间复杂度分析：
 * O(n*m)，其中n为行数，m为列数
 * - 构建前缀和数组：O(n*m)
 * - 枚举邮票位置并标记：O(n*m)
 * - 还原差分数组：O(n*m)
 * 
 * 空间复杂度分析：
 * O(n*m)，用于存储前缀和数组和差分数组
 * 
 * 算法优势：
 * 1. 时间复杂度已达到理论下限
 * 2. 空间效率高，复用数组
 * 3. 贪心策略简单有效
 * 
 * 工程化考虑：
 * 1. 边界处理：扩展数组边界避免特殊判断
 * 2. 数据结构选择：前缀和数组和差分数组分离，职责明确
 * 3. 极端输入处理：处理大尺寸矩阵情况
 * 
 * 应用场景：
 * 1. 游戏开发中的区域覆盖问题
 * 2. 图像处理中的模板匹配
 * 3. 资源分配问题
 * 
 * 相关题目：
 * 1. LeetCode 2132. 用邮票贴满网格图
 * 2. Codeforces 816B - Karen and Coffee
 * 3. AtCoder ABC106D - AtCoder Express 2
 * 
 * 测试链接 : https://leetcode.cn/problems/stamping-the-grid/
 */
public class Code04_StampingTheGrid {

	/**
	 * 判断是否能用邮票贴满所有空格子
	 * 
	 * 算法思路：
	 * 1. 构建原始矩阵的前缀和数组，用于快速查询区域和
	 * 2. 构建差分数组，用于记录邮票放置情况
	 * 3. 枚举所有可能的邮票放置位置，验证并标记
	 * 4. 通过前缀和还原差分数组，检查覆盖情况
	 * 
	 * 贪心策略：
	 * 能放邮票就放邮票，因为邮票可以重叠，多放不影响结果
	 * 
	 * @param grid 原始二进制矩阵
	 * @param h 邮票高度
	 * @param w 邮票宽度
	 * @return 是否能贴满所有空格子
	 */
	// 时间复杂度O(n*m)，额外空间复杂度O(n*m)
	public static boolean possibleToStamp(int[][] grid, int h, int w) {
		int n = grid.length;
		int m = grid[0].length;
		
		// sum是前缀和数组
		// 查询原始矩阵中的某个范围的累加和很快速
		int[][] sum = new int[n + 1][m + 1];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				sum[i + 1][j + 1] = grid[i][j];
			}
		}
		// 构建前缀和数组
		build(sum);
		
		// 差分矩阵
		// 当贴邮票的时候，不再原始矩阵里贴，在差分矩阵里贴
		// 原始矩阵就用来判断能不能贴邮票，不进行修改
		// 每贴一张邮票都在差分矩阵里修改
		int[][] diff = new int[n + 2][m + 2];
		
		// 枚举所有可能的邮票放置位置
		for (int a = 1, c = a + h - 1; c <= n; a++, c++) {
			for (int b = 1, d = b + w - 1; d <= m; b++, d++) {
				// 原始矩阵中 (a,b)左上角点
				// 根据邮票规格，h、w，算出右下角点(c,d)
				// 这个区域彻底都是0，那么: 
				// sumRegion(sum, a, b, c, d) == 0
				// 那么此时这个区域可以贴邮票
				if (sumRegion(sum, a, b, c, d) == 0) {
					// 在差分数组中标记邮票覆盖区域
					add(diff, a, b, c, d);
				}
			}
		}
		
		// 构建差分数组的前缀和，得到邮票覆盖次数
		build(diff);
		
		// 检查所有的格子！
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				// 原始矩阵里：grid[i][j] == 0，说明是个空格子
				// 差分矩阵里：diff[i + 1][j + 1] == 0，说明空格子上并没有邮票
				// 此时返回false
				if (grid[i][j] == 0 && diff[i + 1][j + 1] == 0) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * 构建前缀和数组
	 * 
	 * 算法原理：
	 * 利用容斥原理构建前缀和数组：
	 * m[i][j] = m[i][j] + m[i-1][j] + m[i][j-1] - m[i-1][j-1]
	 * 
	 * 时间复杂度：O(n*m)
	 * 空间复杂度：O(1)（原地更新）
	 * 
	 * @param m 需要构建前缀和的数组
	 */
	public static void build(int[][] m) {
		for (int i = 1; i < m.length; i++) {
			for (int j = 1; j < m[0].length; j++) {
				m[i][j] += m[i - 1][j] + m[i][j - 1] - m[i - 1][j - 1];
			}
		}
	}

	/**
	 * 计算子矩阵元素和
	 * 
	 * 算法原理：
	 * 利用容斥原理计算子矩阵和：
	 * sum = sum[c][d] - sum[c][b-1] - sum[a-1][d] + sum[a-1][b-1]
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * @param sum 前缀和数组
	 * @param a 子矩阵左上角行索引
	 * @param b 子矩阵左上角列索引
	 * @param c 子矩阵右下角行索引
	 * @param d 子矩阵右下角列索引
	 * @return 子矩阵元素和
	 */
	public static int sumRegion(int[][] sum, int a, int b, int c, int d) {
		return sum[c][d] - sum[c][b - 1] - sum[a - 1][d] + sum[a - 1][b - 1];
	}

	/**
	 * 在二维差分数组中标记区域更新
	 * 
	 * 算法原理：
	 * 对区域[(a,b),(c,d)]增加1，在差分数组中进行标记：
	 * 1. diff[a][b] += 1
	 * 2. diff[c+1][d+1] += 1
	 * 3. diff[c+1][b] -= 1
	 * 4. diff[a][d+1] -= 1
	 * 
	 * 时间复杂度：O(1)
	 * 空间复杂度：O(1)
	 * 
	 * @param diff 差分数组
	 * @param a 区域左上角行索引
	 * @param b 区域左上角列索引
	 * @param c 区域右下角行索引
	 * @param d 区域右下角列索引
	 */
	public static void add(int[][] diff, int a, int b, int c, int d) {
		diff[a][b] += 1;
		diff[c + 1][d + 1] += 1;
		diff[c + 1][b] -= 1;
		diff[a][d + 1] -= 1;
	}

}