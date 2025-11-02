"""
子矩阵元素加1问题

问题描述：
给你一个正整数 n ，表示最初有一个 n x n 的整数矩阵 mat ，矩阵中初始值都为 0 。
另给你一个二维整数数组 queries ，其中 queries[i] = [row1, col1, row2, col2] 。
针对每个查询，将子矩阵 mat[row1][col1] 到 mat[row2][col2] 中的每个元素加 1 。
返回执行完所有查询后得到的矩阵 mat 。

核心思想：
1. 利用二维差分数组处理区域更新操作
2. 对每个查询区域，在二维差分数组中进行O(1)标记
3. 通过二维前缀和还原差分数组得到最终结果

算法详解：
1. 差分标记：对区域[(row1,col1),(row2,col2)]增加1，在差分数组中标记：
   - diff[row1][col1] += 1
   - diff[row2+1][col1] -= 1
   - diff[row1][col2+1] -= 1
   - diff[row2+1][col2+1] += 1
2. 前缀和还原：通过二维前缀和将差分数组还原为结果数组

时间复杂度分析：
1. 差分标记：O(q)，q为查询数量
2. 前缀和还原：O(n²)，n为矩阵边长
3. 总体复杂度：O(q + n²)

空间复杂度分析：
O(n²)，用于存储差分数组

算法优势：
1. 区间更新效率高，每次操作O(1)
2. 适合处理大量区间更新操作
3. 空间效率高，复用同一数组

工程化考虑：
1. 边界处理：扩展数组边界避免特殊判断
2. 数据类型选择：使用合适的数据类型防止溢出

应用场景：
1. 图像处理中的区域操作
2. 游戏开发中的区域影响计算
3. 地理信息系统中的区域统计

相关题目：
1. LeetCode 2536. Increment Submatrices by One
2. LeetCode 2132. 用邮票贴满网格图
3. 牛客 226337 二维差分

测试链接 : https://leetcode.cn/problems/increment-submatrices-by-one/
"""


class Solution:
    """
    子矩阵元素加1问题解决方案
    """

    @staticmethod
    def range_add_queries(n, queries):
        """
        执行所有查询后得到的矩阵

        算法思路：
        1. 使用二维差分数组处理区域更新
        2. 对每个查询进行差分标记
        3. 通过二维前缀和还原差分数组得到结果

        时间复杂度：O(q + n²)，q是查询数量
        空间复杂度：O(n²)

        :param n: 矩阵边长
        :param queries: 查询数组，queries[i] = [row1, col1, row2, col2]
        :return: 执行完所有查询后得到的矩阵
        """
        # 创建差分数组
        diff = [[0] * (n + 2) for _ in range(n + 2)]

        # 处理每个查询
        for query in queries:
            row1, col1, row2, col2 = query[0], query[1], query[2], query[3]

            # 在差分数组中标记区域更新
            Solution.add(diff, row1, col1, row2, col2)

        # 通过二维前缀和还原差分数组得到结果
        Solution.build(diff)

        # 构造结果矩阵
        result = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                result[i][j] = diff[i + 1][j + 1]

        return result

    @staticmethod
    def add(diff, a, b, c, d):
        """
        在二维差分数组中标记区域更新

        算法原理：
        对区域[(a,b),(c,d)]增加1，在差分数组中进行标记：
        1. diff[a][b] += 1
        2. diff[c+1][b] -= 1
        3. diff[a][d+1] -= 1
        4. diff[c+1][d+1] += 1

        时间复杂度：O(1)
        空间复杂度：O(1)

        :param diff: 差分数组
        :param a: 区域左上角行索引
        :param b: 区域左上角列索引
        :param c: 区域右下角行索引
        :param d: 区域右下角列索引
        """
        diff[a + 1][b + 1] += 1
        diff[c + 2][b + 1] -= 1
        diff[a + 1][d + 2] -= 1
        diff[c + 2][d + 2] += 1

    @staticmethod
    def build(diff):
        """
        通过二维前缀和还原差分数组

        算法原理：
        利用容斥原理将差分数组还原为结果数组：
        diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]

        时间复杂度：O(n²)
        空间复杂度：O(1)（原地更新）
        """
        for i in range(1, len(diff)):
            for j in range(1, len(diff[0])):
                diff[i][j] += diff[i - 1][j] + diff[i][j - 1] - diff[i - 1][j - 1]


def main():
    """测试用例"""
    # 测试用例1
    n1 = 3
    queries1 = [[1, 1, 2, 2], [0, 0, 1, 1]]
    result1 = Solution.range_add_queries(n1, queries1)
    # 预期输出: [[1,1,0],[1,2,1],[0,1,1]]
    print("测试用例1结果:")
    for row in result1:
        print(" ".join(map(str, row)))

    # 测试用例2
    n2 = 2
    queries2 = [[0, 0, 1, 1]]
    result2 = Solution.range_add_queries(n2, queries2)
    # 预期输出: [[1,1],[1,1]]
    print("测试用例2结果:")
    for row in result2:
        print(" ".join(map(str, row)))


if __name__ == "__main__":
    main()