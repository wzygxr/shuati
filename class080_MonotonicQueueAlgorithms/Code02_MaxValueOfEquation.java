package class055;

// 满足不等式的最大值
// 给你一个数组 points 和一个整数 k
// 数组中每个元素都表示二维平面上的点的坐标，并按照横坐标 x 的值从小到大排序
// 也就是说 points[i] = [xi, yi]
// 并且在 1 <= i < j <= points.length 的前提下，xi < xj 总成立
// 请你找出 yi + yj + |xi - xj| 的 最大值，
// 其中 |xi - xj| <= k 且 1 <= i < j <= points.length
// 题目测试数据保证至少存在一对能够满足 |xi - xj| <= k 的点。
// 测试链接 : https://leetcode.cn/problems/max-value-of-equation/
public class Code02_MaxValueOfEquation {

	public static int MAXN = 100001;

	// [、i号点[x,y]、]
	//  h、t
	public static int[][] deque = new int[MAXN][2];

	public static int h, t;

	/*
	 * 解题思路：
	 * 由于x是有序的，且xi < xj，所以|xi - xj| = xj - xi
	 * 表达式可以转化为：yi + yj + xj - xi = (yj + xj) + (yi - xi)
	 * 对于每个点j，我们需要找到在范围内的点i，使得(yi - xi)最大
	 * 这可以通过维护一个单调队列来实现，队列中按照(yi - xi)的值单调递减
	 *
	 * 算法步骤：
	 * 1. 遍历每个点
	 * 2. 移除队列中超出距离k的点
	 * 3. 如果队列不为空，用队首元素计算当前点对的值
	 * 4. 维护队列的单调性，移除所有小于当前点(y - x)值的元素
	 * 5. 将当前点加入队列
	 *
	 * 时间复杂度分析：
	 * O(n) - 每个元素最多入队出队一次
	 *
	 * 空间复杂度分析：
	 * O(n) - 存储单调队列
	 *
	 * 是否最优解：
	 * 是，这是处理此类问题的最优解法
	 */
	public static int findMaxValueOfEquation(int[][] points, int k) {
		h = t = 0;
		int n = points.length;
		int ans = Integer.MIN_VALUE;
		for (int i = 0, x, y; i < n; i++) {
			// i号点是此时的点，当前的后面点，看之前哪个点的y-x值最大，x距离又不能超过k
			x = points[i][0];
			y = points[i][1];
			while (h < t && deque[h][0] + k < x) {
				// 单调队列头部的可能性过期了，头部点的x与当前点x的距离超过了k
				h++;
			}
			if (h < t) {
				ans = Math.max(ans, x + y + deque[h][1] - deque[h][0]);
			}
			// i号点的x和y，该从尾部进入单调队列
			// 大 -> 小
			while (h < t && deque[t - 1][1] - deque[t - 1][0] <= y - x) {
				t--;
			}
			deque[t][0] = x;
			deque[t++][1] = y;
		}
		return ans;
	}

	/*
	 * C++版本实现
	 * 
	 * #include <vector>
	 * #include <deque>
	 * #include <climits>
	 * #include <algorithm>
	 * using namespace std;
	 * 
	 * class Solution {
	 * public:
	 *     int findMaxValueOfEquation(vector<vector<int>>& points, int k) {
	 *         deque<pair<int, int>> dq; // 存储 {x, y-x}
	 *         int result = INT_MIN;
	 *         
	 *         for (const auto& point : points) {
	 *             int x = point[0], y = point[1];
	 *             
	 *             // 移除超出距离k的点
	 *             while (!dq.empty() && dq.front().first + k < x) {
	 *                 dq.pop_front();
	 *             }
	 *             
	 *             // 如果队列不为空，计算当前点对的值
	 *             if (!dq.empty()) {
	 *                 result = max(result, y + x + dq.front().second);
	 *             }
	 *             
	 *             // 维护单调性，移除所有小于当前点(y-x)值的元素
	 *             while (!dq.empty() && dq.back().second <= y - x) {
	 *                 dq.pop_back();
	 *             }
	 *             
	 *             // 将当前点加入队列
	 *             dq.push_back({x, y - x});
	 *         }
	 *         
	 *         return result;
	 *     }
	 * };
	 */

	/*
	 * Python版本实现
	 * 
	 * from collections import deque
	 * import sys
	 * 
	 * def findMaxValueOfEquation(points, k):
	 *     dq = deque()  # 存储 (x, y-x)
	 *     result = -sys.maxsize
	 *     
	 *     for x, y in points:
	 *         # 移除超出距离k的点
	 *         while dq and dq[0][0] + k < x:
	 *             dq.popleft()
	 *         
	 *         # 如果队列不为空，计算当前点对的值
	 *         if dq:
	 *             result = max(result, y + x + dq[0][1])
	 *         
	 *         # 维护单调性，移除所有小于当前点(y-x)值的元素
	 *         while dq and dq[-1][1] <= y - x:
	 *             dq.pop()
	 *         
	 *         # 将当前点加入队列
	 *         dq.append((x, y - x))
	 *     
	 *     return result
	 */
}