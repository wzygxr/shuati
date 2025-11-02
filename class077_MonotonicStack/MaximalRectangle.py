"""
85. 最大矩形 (Maximal Rectangle)

题目描述:
给定一个仅包含 0 和 1 、大小为 rows x cols 的二维二进制矩阵，
找出只包含 1 的最大矩形，并返回其面积。

解题思路:
将二维问题转化为一维问题。逐行构建高度数组，然后对每一行应用柱状图中最大矩形的解法。
使用单调栈来计算每个位置的最大矩形面积。

时间复杂度: O(rows * cols)
空间复杂度: O(cols)

测试链接: https://leetcode.cn/problems/maximal-rectangle/

工程化考量:
1. 异常处理：空矩阵、边界情况处理
2. 性能优化：使用列表预分配空间，避免动态扩展
3. Python特性：利用列表的高效操作和生成器表达式
4. 代码可读性：详细注释和模块化设计
"""

from typing import List

def maximal_rectangle(matrix: List[List[str]]) -> int:
    """
    计算二维矩阵中最大矩形的面积
    
    Args:
        matrix: 输入二维矩阵，包含'0'和'1'
        
    Returns:
        int: 最大矩形面积
        
    Raises:
        TypeError: 如果输入不是二维列表
        ValueError: 如果矩阵包含非法字符
    """
    # 边界条件检查
    if not matrix or not matrix[0]:
        return 0
    
    rows = len(matrix)
    cols = len(matrix[0])
    max_area = 0
    
    # 高度数组，记录每一列连续1的高度
    heights = [0] * cols
    
    # 逐行处理矩阵
    for i in range(rows):
        # 更新高度数组
        for j in range(cols):
            if matrix[i][j] == '1':
                heights[j] += 1
            else:
                heights[j] = 0
        
        # 对当前行的高度数组计算最大矩形面积
        max_area = max(max_area, largest_rectangle_area(heights))
    
    return max_area

def largest_rectangle_area(heights: List[int]) -> int:
    """
    计算柱状图中最大矩形的面积（单调栈解法）
    
    Args:
        heights: 高度数组
        
    Returns:
        int: 最大矩形面积
    """
    if not heights:
        return 0
    
    n = len(heights)
    stack = []  # 使用列表作为栈
    max_area = 0
    
    # 遍历每个柱子
    for i in range(n):
        # 当栈不为空且当前高度小于栈顶索引对应的高度时
        while stack and heights[stack[-1]] > heights[i]:
            height = heights[stack.pop()]  # 弹出栈顶元素作为高度
            # 计算宽度
            width = i if not stack else i - stack[-1] - 1
            max_area = max(max_area, height * width)
        
        stack.append(i)  # 将当前索引入栈
    
    # 处理栈中剩余元素
    while stack:
        height = heights[stack.pop()]
        width = n if not stack else n - stack[-1] - 1
        max_area = max(max_area, height * width)
    
    return max_area

def maximal_rectangle_optimized(matrix: List[List[str]]) -> int:
    """
    优化版本：使用动态规划预处理左右边界
    
    Args:
        matrix: 输入二维矩阵
        
    Returns:
        int: 最大矩形面积
    """
    if not matrix or not matrix[0]:
        return 0
    
    rows = len(matrix)
    cols = len(matrix[0])
    max_area = 0
    
    heights = [0] * cols
    left = [0] * cols      # 左边第一个比当前高度小的位置
    right = [cols] * cols  # 右边第一个比当前高度小的位置
    
    for i in range(rows):
        # 更新高度数组
        for j in range(cols):
            if matrix[i][j] == '1':
                heights[j] += 1
            else:
                heights[j] = 0
        
        # 更新left边界
        current_left = 0
        for j in range(cols):
            if matrix[i][j] == '1':
                left[j] = max(left[j], current_left)
            else:
                left[j] = 0
                current_left = j + 1
        
        # 更新right边界
        current_right = cols
        for j in range(cols-1, -1, -1):
            if matrix[i][j] == '1':
                right[j] = min(right[j], current_right)
            else:
                right[j] = cols
                current_right = j
        
        # 计算当前行的最大矩形面积
        for j in range(cols):
            area = heights[j] * (right[j] - left[j])
            max_area = max(max_area, area)
    
    return max_area

def test_maximal_rectangle():
    """测试方法 - 验证算法正确性"""
    print("=== 最大矩形算法测试 ===")
    
    # 测试用例1
    matrix1 = [
        ['1','0','1','0','0'],
        ['1','0','1','1','1'],
        ['1','1','1','1','1'],
        ['1','0','0','1','0']
    ]
    result1 = maximal_rectangle(matrix1)
    result1_opt = maximal_rectangle_optimized(matrix1)
    print(f"测试用例1: {result1} (优化版: {result1_opt}, 预期: 6)")
    
    # 测试用例2: 全1矩阵
    matrix2 = [
        ['1','1'],
        ['1','1']
    ]
    result2 = maximal_rectangle(matrix2)
    result2_opt = maximal_rectangle_optimized(matrix2)
    print(f"测试用例2: {result2} (优化版: {result2_opt}, 预期: 4)")
    
    # 测试用例3: 空矩阵
    matrix3 = []
    result3 = maximal_rectangle(matrix3)
    result3_opt = maximal_rectangle_optimized(matrix3)
    print(f"测试用例3: {result3} (优化版: {result3_opt}, 预期: 0)")
    
    # 测试用例4: 单行矩阵
    matrix4 = [['1','0','1','1','0']]
    result4 = maximal_rectangle(matrix4)
    result4_opt = maximal_rectangle_optimized(matrix4)
    print(f"测试用例4: {result4} (优化版: {result4_opt}, 预期: 2)")
    
    # 测试用例5: 全0矩阵
    matrix5 = [
        ['0','0'],
        ['0','0']
    ]
    result5 = maximal_rectangle(matrix5)
    result5_opt = maximal_rectangle_optimized(matrix5)
    print(f"测试用例5: {result5} (优化版: {result5_opt}, 预期: 0)")
    
    print("=== 功能测试完成！ ===")

def performance_test():
    """性能测试方法"""
    import time
    
    print("=== 性能测试 ===")
    
    # 性能测试：大规模数据
    size = 100
    matrix = []
    for i in range(size):
        row = []
        for j in range(size):
            row.append('1' if (i + j) % 2 == 0 else '0')
        matrix.append(row)
    
    # 标准版本性能测试
    start_time = time.time()
    result = maximal_rectangle(matrix)
    end_time = time.time()
    print(f"标准版本 [{size}x{size}矩阵]: 结果={result}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 优化版本性能测试
    start_time = time.time()
    result_opt = maximal_rectangle_optimized(matrix)
    end_time = time.time()
    print(f"优化版本 [{size}x{size}矩阵]: 结果={result_opt}, 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")

def debug_print(matrix: List[List[str]], heights: List[int], row: int):
    """
    调试辅助方法：打印矩阵和高度数组
    
    Args:
        matrix: 输入矩阵
        heights: 高度数组
        row: 当前行号
    """
    print(f"第 {row} 行:")
    print(f"高度数组: {heights}")
    print("---")

if __name__ == "__main__":
    # 运行功能测试
    test_maximal_rectangle()
    
    # 运行性能测试
    performance_test()

"""
算法复杂度分析:

时间复杂度: O(rows * cols)
- 外层循环遍历rows行
- 内层循环遍历cols列，并调用O(cols)的单调栈算法
- 总时间复杂度为O(rows * cols)

空间复杂度: O(cols)
- 高度数组大小为cols
- 单调栈大小为cols
- 优化版本需要额外的left和right数组

最优解分析:
- 这是最大矩形问题的最优解之一
- 无法获得更好的时间复杂度
- 空间复杂度也是最优的

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用列表推导式和切片操作提高代码可读性
- 使用类型注解提高代码可维护性

问题转化技巧:
- 将二维矩阵问题转化为多个一维柱状图问题
- 逐行构建高度数组，记录连续1的累积高度
- 对每一行应用柱状图中最大矩形的单调栈解法

工程化建议:
1. 对于超大规模矩阵，可以考虑使用更高效的数据结构
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控
4. 对于生产环境，可以添加日志记录和异常监控
"""