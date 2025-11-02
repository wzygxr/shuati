"""
二维前缀和算法实现 - Python版本

核心思想：
1. 利用二维前缀和数组快速计算任意子矩阵的元素和
2. 前缀和数组preSum[i][j]表示从(0,0)到(i-1,j-1)的子矩阵元素和
3. 通过容斥原理计算任意子矩阵和：sumRegion(a,b,c,d) = preSum[c+1][d+1] - preSum[c+1][b] - preSum[a][d+1] + preSum[a][b]

时间复杂度分析：
1. 构造前缀和数组：O(n*m)，其中n为行数，m为列数
2. 查询子矩阵和：O(1)

空间复杂度分析：
O((n+1)*(m+1))，用于存储前缀和数组

算法优势：
1. 查询效率高，一次查询时间复杂度为O(1)
2. 适用于需要多次查询不同子矩阵和的场景
3. 代码实现简单，易于理解和维护

工程化考虑：
1. 边界处理：通过扩展前缀和数组边界避免特殊判断
2. 异常处理：应添加对空矩阵、越界查询的处理
3. 内存管理：使用列表推导式创建二维数组
4. 性能优化：避免不必要的拷贝操作

应用场景：
1. 图像处理中的区域统计
2. 机器学习中的特征提取
3. 游戏开发中的地图区域计算
4. 数据分析中的区域统计

相关题目：
1. LeetCode 304. Range Sum Query 2D - Immutable
2. Codeforces 1371C - A Cookie for You
3. AtCoder ABC106D - AtCoder Express 2
4. HDU 1559 最大子矩阵
5. POJ 1050 To the Max

测试链接 : https://leetcode.cn/problems/range-sum-query-2d-immutable/

Python语言特性：
1. 使用列表嵌套实现二维数组
2. 使用列表推导式简化代码
3. 使用异常处理机制
4. 支持动态类型检查
"""

class NumMatrix:
    """
    NumMatrix类实现了二维前缀和的功能
    
    设计特点：
    1. 在构造函数中预处理前缀和数组，提高查询效率
    2. 使用偏移坐标系统简化边界处理
    3. 支持多次查询，每次查询时间复杂度O(1)
    
    算法详解：
    1. 前缀和构建：preSum[i][j] = matrix[i-1][j-1] + preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1]
    2. 区域和查询：利用容斥原理计算任意子矩阵和
    
    数学原理：
    容斥原理：A∪B = A + B - A∩B
    在二维前缀和中：preSum[i][j] = matrix[i-1][j-1] + preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1]
    
    时间复杂度分析：
    - 构造函数：O(n*m)
    - sumRegion方法：O(1)
    
    空间复杂度分析：
    O((n+1)*(m+1))，用于存储前缀和数组
    """

    def __init__(self, matrix):
        """
        构造函数：构建二维前缀和数组
        
        算法步骤：
        1. 初始化(n+1)*(m+1)的前缀和数组
        2. 将原始矩阵复制到前缀和数组的偏移位置
        3. 按行按列依次计算前缀和
        
        时间复杂度：O(n*m)
        空间复杂度：O((n+1)*(m+1))
        
        工程化考量：
        1. 异常处理：检查输入矩阵是否为空
        2. 边界处理：扩展数组边界避免特殊判断
        3. 内存管理：使用列表推导式创建二维数组
        
        Python特性：
        1. 使用列表推导式创建二维数组
        2. 使用enumerate简化循环
        3. 使用类型注解提高代码可读性
        
        :param matrix: 原始二维矩阵，要求非空且至少有一个元素
        :raises ValueError: 如果输入矩阵为空或维度为0
        """
        # 参数校验：确保输入矩阵有效
        if not matrix or not matrix[0]:
            raise ValueError("输入矩阵不能为空")
        
        n = len(matrix)
        m = len(matrix[0])
        
        # 创建前缀和数组，行列均多申请一个空间用于简化边界处理
        # 使用列表推导式创建(n+1)*(m+1)的二维数组，初始值为0
        self.preSum = [[0] * (m + 1) for _ in range(n + 1)]
        
        # 构建前缀和数组
        # 利用容斥原理：当前点前缀和 = 当前点值 + 上方前缀和 + 左方前缀和 - 左上角前缀和
        # 数学原理：preSum[i][j] = matrix[i-1][j-1] + preSum[i-1][j] + preSum[i][j-1] - preSum[i-1][j-1]
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                self.preSum[i][j] = (matrix[i-1][j-1] + 
                                    self.preSum[i-1][j] + 
                                    self.preSum[i][j-1] - 
                                    self.preSum[i-1][j-1])
                
                # 调试输出：打印每一步的前缀和计算结果
                # print(f"preSum[{i}][{j}] = {matrix[i-1][j-1]} + {self.preSum[i-1][j]} + "
                #       f"{self.preSum[i][j-1]} - {self.preSum[i-1][j-1]} = {self.preSum[i][j]}")

    def sumRegion(self, row1: int, col1: int, row2: int, col2: int) -> int:
        """
        查询指定区域的元素和
        
        算法原理：
        利用容斥原理计算子矩阵和：
        sumRegion(row1,col1,row2,col2) = preSum[row2+1][col2+1] - preSum[row2+1][col1] - preSum[row1][col2+1] + preSum[row1][col1]
        
        数学推导：
        1. preSum[row2+1][col2+1] 包含从(0,0)到(row2,col2)的所有元素
        2. 减去preSum[row2+1][col1] 去掉左侧多余部分
        3. 减去preSum[row1][col2+1] 去掉上方多余部分
        4. 加上preSum[row1][col1] 补回多减的部分
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        边界情况处理：
        1. 输入坐标合法性检查
        2. 坐标越界处理
        3. 空矩阵查询处理
        
        工程化考量：
        1. 参数校验：确保输入坐标有效
        2. 性能优化：避免不必要的计算
        3. 异常处理：提供友好的错误信息
        
        Python特性：
        1. 使用类型注解提高代码可读性
        2. 使用f-string格式化输出
        
        :param row1: 子矩阵左上角行索引（从0开始）
        :param col1: 子矩阵左上角列索引（从0开始）
        :param row2: 子矩阵右下角行索引（从0开始）
        :param col2: 子矩阵右下角列索引（从0开始）
        :return: 子矩阵元素和
        :raises ValueError: 如果坐标越界或无效
        """
        # 参数校验：确保坐标有效
        if (row1 < 0 or col1 < 0 or row2 < row1 or col2 < col1 or 
            row2 >= len(self.preSum) - 1 or col2 >= len(self.preSum[0]) - 1):
            raise ValueError("坐标越界或无效")
        
        # 调整坐标到前缀和数组的对应位置
        # 由于前缀和数组有偏移，需要将原始坐标加1
        row2_adj = row2 + 1
        col2_adj = col2 + 1
        
        # 利用容斥原理计算区域和
        # 公式：preSum[row2_adj][col2_adj] - preSum[row2_adj][col1] - preSum[row1][col2_adj] + preSum[row1][col1]
        result = (self.preSum[row2_adj][col2_adj] - 
                 self.preSum[row2_adj][col1] - 
                 self.preSum[row1][col2_adj] + 
                 self.preSum[row1][col1])
        
        # 调试输出：打印查询结果
        # print(f"sumRegion({row1},{col1},{row2},{col2}) = "
        #       f"{self.preSum[row2_adj][col2_adj]} - {self.preSum[row2_adj][col1]} - "
        #       f"{self.preSum[row1][col2_adj]} + {self.preSum[row1][col1]} = {result}")
        
        return result


def test_normal_case():
    """测试用例1：正常情况"""
    print("=== 测试用例1：正常情况 ===")
    matrix1 = [
        [3, 0, 1, 4, 2],
        [5, 6, 3, 2, 1],
        [1, 2, 0, 1, 5],
        [4, 1, 0, 1, 7],
        [1, 0, 3, 0, 5]
    ]
    
    numMatrix = NumMatrix(matrix1)
    
    # 测试sumRegion(2, 1, 4, 3)
    result1 = numMatrix.sumRegion(2, 1, 4, 3)
    print(f"sumRegion(2, 1, 4, 3) = {result1}")  # 预期输出: 8
    
    # 测试sumRegion(1, 1, 2, 2)
    result2 = numMatrix.sumRegion(1, 1, 2, 2)
    print(f"sumRegion(1, 1, 2, 2) = {result2}")  # 预期输出: 11
    
    # 测试sumRegion(1, 2, 2, 4)
    result3 = numMatrix.sumRegion(1, 2, 2, 4)
    print(f"sumRegion(1, 2, 2, 4) = {result3}")  # 预期输出: 12
    
    print()


def test_edge_case():
    """测试用例2：边界情况 - 单元素矩阵"""
    print("=== 测试用例2：单元素矩阵 ===")
    matrix2 = [[5]]
    numMatrix2 = NumMatrix(matrix2)
    result4 = numMatrix2.sumRegion(0, 0, 0, 0)
    print(f"sumRegion(0, 0, 0, 0) = {result4}")  # 预期输出: 5
    print()


def test_performance():
    """测试用例3：性能测试 - 大规模数据"""
    print("=== 测试用例3：性能测试 ===")
    import time
    
    n = 1000
    m = 1000
    # 使用列表推导式创建大规模矩阵
    large_matrix = [[i + j for j in range(m)] for i in range(n)]
    
    start_time = time.time()
    numMatrix3 = NumMatrix(large_matrix)
    construction_time = (time.time() - start_time) * 1000  # 转换为毫秒
    
    start_time = time.time()
    # 执行多次查询测试性能
    for i in range(1000):
        numMatrix3.sumRegion(0, 0, n-1, m-1)
    query_time = (time.time() - start_time) * 1000  # 转换为毫秒
    
    print(f"构造时间: {construction_time:.2f}ms")
    print(f"1000次查询时间: {query_time:.2f}ms")
    print(f"平均查询时间: {query_time / 1000:.4f}ms")
    print()


def test_exception_case():
    """测试用例4：异常情况测试"""
    print("=== 测试用例4：异常情况测试 ===")
    try:
        empty_matrix = []
        numMatrix4 = NumMatrix(empty_matrix)
    except ValueError as e:
        print(f"异常处理测试通过: {e}")
    print()


def main():
    """主函数：执行所有测试用例"""
    try:
        test_normal_case()
        test_edge_case()
        test_performance()
        test_exception_case()
        print("所有测试用例执行完成！")
    except Exception as e:
        print(f"测试过程中出现异常: {e}")


if __name__ == "__main__":
    main()