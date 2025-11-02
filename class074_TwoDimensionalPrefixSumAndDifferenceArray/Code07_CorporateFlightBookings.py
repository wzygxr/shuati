"""
航班预订统计问题

问题描述：
给定 n 个航班，编号从 1 到 n。有一个航班预订表 bookings，其中 bookings[i] = [first, last, seats] 
表示在从 first 到 last（包含 first 和 last）的每个航班上预订了 seats 个座位。
返回一个长度为 n 的数组 answer，其中 answer[i] 是第 (i+1) 个航班预定的座位总数。

核心思想：
1. 利用差分数组处理区间更新操作
2. 对每个预订记录，在差分数组中进行O(1)标记
3. 通过前缀和还原差分数组得到最终结果

算法详解：
1. 差分标记：对区间[first, last]增加seats，在差分数组中标记：
   - diff[first-1] += seats
   - diff[last] -= seats
2. 前缀和还原：通过前缀和将差分数组还原为结果数组

时间复杂度分析：
1. 差分标记：O(k)，k为预订记录数量
2. 前缀和还原：O(n)，n为航班数量
3. 总体复杂度：O(k + n)

空间复杂度分析：
O(n)，用于存储差分数组

算法优势：
1. 区间更新效率高，每次操作O(1)
2. 适合处理大量区间更新操作
3. 空间效率高，复用同一数组

工程化考虑：
1. 边界处理：扩展数组边界避免特殊判断
2. 数据类型选择：使用合适的数据类型防止溢出

应用场景：
1. 资源分配问题
2. 区域统计问题
3. 游戏开发中的区域影响计算

相关题目：
1. LeetCode 1109. Corporate Flight Bookings
2. LeetCode 370. Range Addition
3. HackerRank Array Manipulation

测试链接 : https://leetcode.cn/problems/corporate-flight-bookings/
"""


class Solution:
    """
    航班预订统计问题解决方案
    """

    @staticmethod
    def corp_flight_bookings(bookings, n):
        """
        计算每个航班预定的座位总数

        算法思路：
        1. 使用差分数组处理区间更新
        2. 对每个预订记录进行差分标记
        3. 通过前缀和还原差分数组得到结果

        时间复杂度：O(k + n)，k是预订记录数量
        空间复杂度：O(n)

        :param bookings: 航班预订表，bookings[i] = [first, last, seats]
        :param n: 航班数量
        :return: 每个航班预定的座位总数
        """
        # 创建差分数组
        diff = [0] * (n + 1)

        # 处理每个预订记录
        for booking in bookings:
            first, last, seats = booking[0], booking[1], booking[2]

            # 在差分数组中标记区间更新
            diff[first - 1] += seats
            diff[last] -= seats

        # 通过前缀和还原差分数组得到结果
        result = [0] * n
        result[0] = diff[0]
        for i in range(1, n):
            result[i] = result[i - 1] + diff[i]

        return result


def main():
    """测试用例"""
    solution = Solution()

    # 测试用例1
    bookings1 = [[1, 2, 10], [2, 3, 20], [2, 5, 25]]
    n1 = 5
    result1 = solution.corp_flight_bookings(bookings1, n1)
    # 预期输出: [10, 55, 45, 25, 25]
    print("测试用例1结果:", result1)

    # 测试用例2
    bookings2 = [[1, 2, 10], [2, 2, 15]]
    n2 = 2
    result2 = solution.corp_flight_bookings(bookings2, n2)
    # 预期输出: [10, 25]
    print("测试用例2结果:", result2)


if __name__ == "__main__":
    main()