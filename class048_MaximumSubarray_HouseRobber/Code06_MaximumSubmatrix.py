"""
子矩阵最大累加和问题 - Python实现
给定一个整数矩阵matrix
请找出元素和最大的子矩阵
返回这个子矩阵的元素和
测试链接 : https://leetcode.cn/problems/max-submatrix-lcci/

算法核心思想：
1. 将二维问题转化为一维问题
2. 枚举所有可能的上下边界
3. 将上下边界之间的每列元素相加，形成一维数组
4. 对一维数组应用Kadane算法求最大子数组和
5. 记录最大的子矩阵和

时间复杂度分析：
- 最优时间复杂度：O(n² * m) 或 O(m² * n) - 取决于行列数量
- 空间复杂度：O(m) 或 O(n) - 用于存储压缩后的一维数组

工程化考量：
1. 边界处理：处理空矩阵、单行矩阵等特殊情况
2. 性能优化：根据矩阵形状选择最优的枚举方向
3. 内存优化：使用空间压缩技术减少内存使用
4. 可读性：清晰的变量命名和算法步骤说明
"""

from typing import List
import sys

class MaximumSubmatrix:
    """
    子矩阵最大累加和问题的解决方案类
    提供多种实现方式和工具方法
    """
    
    @staticmethod
    def max_submatrix(matrix: List[List[int]]) -> int:
        """
        计算子矩阵最大累加和（基础版本）
        
        算法原理：
        1. 枚举所有可能的上下边界组合
        2. 将多行压缩为一维数组（列方向求和）
        3. 对一维数组应用Kadane算法
        4. 记录过程中的最大值
        
        时间复杂度：O(n² * m) - 其中n是行数，m是列数
        空间复杂度：O(m) - 用于存储压缩后的一维数组
        
        Args:
            matrix: 输入的二维整数矩阵
            
        Returns:
            int: 最大子矩阵的元素和
            
        Raises:
            ValueError: 如果输入矩阵为空
            
        Examples:
            >>> matrix = [[-1, 2, 3], [4, -5, 6], [7, 8, -9]]
            >>> MaximumSubmatrix.max_submatrix(matrix)
            21
        """
        # 边界检查
        if not matrix or not matrix[0]:
            raise ValueError("输入矩阵不能为空")
        
        rows, cols = len(matrix), len(matrix[0])
        max_sum = -sys.maxsize - 1
        
        # 枚举所有可能的上下边界
        for top in range(rows):
            # 压缩数组，存储当前上下边界之间的列和
            compressed = [0] * cols
            
            for bottom in range(top, rows):
                # 更新压缩数组：添加当前行的元素
                for col in range(cols):
                    compressed[col] += matrix[bottom][col]
                
                # 对压缩数组应用Kadane算法
                current_sum = MaximumSubmatrix.kadane(compressed)
                max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    @staticmethod
    def kadane(arr: List[int]) -> int:
        """
        Kadane算法：计算一维数组的最大子数组和
        
        Args:
            arr: 输入的一维整数数组
            
        Returns:
            int: 最大子数组和
        """
        if not arr:
            return 0
        
        max_sum = arr[0]
        current_sum = arr[0]
        
        for i in range(1, len(arr)):
            current_sum = max(arr[i], current_sum + arr[i])
            max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    @staticmethod
    def max_submatrix_with_position(matrix: List[List[int]]) -> tuple:
        """
        计算子矩阵最大累加和并返回位置信息
        
        算法改进：
        - 记录最大子矩阵的边界位置
        - 返回最大和及对应的子矩阵坐标
        
        Returns:
            tuple: (最大和, 上边界, 下边界, 左边界, 右边界)
        """
        if not matrix or not matrix[0]:
            raise ValueError("输入矩阵不能为空")
        
        rows, cols = len(matrix), len(matrix[0])
        max_sum = -sys.maxsize - 1
        positions = (0, 0, 0, 0)  # top, bottom, left, right
        
        for top in range(rows):
            compressed = [0] * cols
            
            for bottom in range(top, rows):
                # 更新压缩数组
                for col in range(cols):
                    compressed[col] += matrix[bottom][col]
                
                # 使用扩展的Kadane算法获取位置信息
                current_sum, left, right = MaximumSubmatrix.kadane_with_position(compressed)
                
                if current_sum > max_sum:
                    max_sum = current_sum
                    positions = (top, bottom, left, right)
        
        return max_sum, positions[0], positions[1], positions[2], positions[3]
    
    @staticmethod
    def kadane_with_position(arr: List[int]) -> tuple:
        """
        Kadane算法扩展：返回最大子数组和及其位置
        
        Args:
            arr: 输入的一维整数数组
            
        Returns:
            tuple: (最大和, 左边界, 右边界)
        """
        if not arr:
            return 0, 0, 0
        
        max_sum = arr[0]
        current_sum = arr[0]
        max_left, max_right = 0, 0
        current_left = 0
        
        for i in range(1, len(arr)):
            if arr[i] > current_sum + arr[i]:
                current_sum = arr[i]
                current_left = i
            else:
                current_sum += arr[i]
            
            if current_sum > max_sum:
                max_sum = current_sum
                max_left = current_left
                max_right = i
        
        return max_sum, max_left, max_right
    
    @staticmethod
    def max_submatrix_optimized(matrix: List[List[int]]) -> int:
        """
        优化版本：根据矩阵形状选择最优枚举方向
        
        算法改进：
        - 如果行数远大于列数，按列枚举
        - 如果列数远大于行数，按行枚举
        - 减少不必要的计算
        
        Returns:
            int: 最大子矩阵和
        """
        if not matrix or not matrix[0]:
            return 0
        
        rows, cols = len(matrix), len(matrix[0])
        
        # 根据矩阵形状选择最优策略
        if rows > cols * 2:
            # 行数远大于列数，按列枚举更高效
            return MaximumSubmatrix._max_submatrix_by_col(matrix)
        elif cols > rows * 2:
            # 列数远大于行数，按行枚举更高效
            return MaximumSubmatrix._max_submatrix_by_row(matrix)
        else:
            # 行列数相近，使用标准方法
            return MaximumSubmatrix.max_submatrix(matrix)
    
    @staticmethod
    def _max_submatrix_by_col(matrix: List[List[int]]) -> int:
        """按列枚举的优化版本"""
        rows, cols = len(matrix), len(matrix[0])
        max_sum = -sys.maxsize - 1
        
        for left in range(cols):
            compressed = [0] * rows
            
            for right in range(left, cols):
                for row in range(rows):
                    compressed[row] += matrix[row][right]
                
                current_sum = MaximumSubmatrix.kadane(compressed)
                max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    @staticmethod
    def _max_submatrix_by_row(matrix: List[List[int]]) -> int:
        """按行枚举的优化版本"""
        rows, cols = len(matrix), len(matrix[0])
        max_sum = -sys.maxsize - 1
        
        for top in range(rows):
            compressed = [0] * cols
            
            for bottom in range(top, rows):
                for col in range(cols):
                    compressed[col] += matrix[bottom][col]
                
                current_sum = MaximumSubmatrix.kadane(compressed)
                max_sum = max(max_sum, current_sum)
        
        return max_sum
    
    @staticmethod
    def test_all_methods():
        """测试所有实现方法的一致性"""
        test_cases = [
            # 标准测试用例
            ([[1, 2, 3], [4, 5, 6], [7, 8, 9]], 45),  # 全正数矩阵
            ([[-1, -2, -3], [-4, -5, -6], [-7, -8, -9]], -1),  # 全负数矩阵
            ([[-1, 2, 3], [4, -5, 6], [7, 8, -9]], 21),  # 混合正负数
            ([[0, 0, 0], [0, 0, 0], [0, 0, 0]], 0),  # 全零矩阵
            ([[1]], 1),  # 单元素矩阵
            ([[1, 2], [3, 4]], 10),  # 2x2矩阵
        ]
        
        print("=== 子矩阵最大累加和算法测试 ===")
        
        for i, (matrix, expected) in enumerate(test_cases, 1):
            try:
                result1 = MaximumSubmatrix.max_submatrix(matrix)
                result2 = MaximumSubmatrix.max_submatrix_optimized(matrix)
                
                print(f"测试用例 {i}:")
                print(f"  输入矩阵: {matrix}")
                print(f"  期望结果: {expected}")
                print(f"  方法1结果: {result1} {'✓' if result1 == expected else '✗'}")
                print(f"  方法2结果: {result2} {'✓' if result2 == expected else '✗'}")
                print(f"  一致性: {'一致' if result1 == result2 else '不一致'}")
                
                # 测试位置信息方法
                if result1 == expected:
                    max_sum, top, bottom, left, right = MaximumSubmatrix.max_submatrix_with_position(matrix)
                    print(f"  位置信息: 和={max_sum}, 区域=[{top}:{bottom}, {left}:{right}]")
                print()
                
            except Exception as e:
                print(f"测试用例 {i} 异常: {e}")
                print()
        
        print("=== 测试完成 ===")


def max_submatrix_simple(matrix: List[List[int]]) -> int:
    """
    简化版本：适合快速实现和面试
    
    Args:
        matrix: 输入的二维整数矩阵
        
    Returns:
        int: 最大子矩阵的元素和
    """
    if not matrix or not matrix[0]:
        return 0
    
    rows, cols = len(matrix), len(matrix[0])
    max_sum = -10**9
    
    for top in range(rows):
        compressed = [0] * cols
        
        for bottom in range(top, rows):
            for col in range(cols):
                compressed[col] += matrix[bottom][col]
            
            # 应用Kadane算法
            current_sum = compressed[0]
            max_ending_here = compressed[0]
            
            for i in range(1, cols):
                max_ending_here = max(compressed[i], max_ending_here + compressed[i])
                current_sum = max(current_sum, max_ending_here)
            
            max_sum = max(max_sum, current_sum)
    
    return max_sum


if __name__ == "__main__":
    # 运行功能测试
    MaximumSubmatrix.test_all_methods()
    
    # 简单使用示例
    test_matrix = [[-1, 2, 3], [4, -5, 6], [7, 8, -9]]
    result = max_submatrix_simple(test_matrix)
    print(f"简单版本测试: {test_matrix} -> {result}")

"""
扩展思考与工程化考量：

1. 算法正确性深度分析：
   - 为什么枚举上下边界的方法是正确的？
     因为任何子矩阵都可以由上下边界和左右边界唯一确定
   - 压缩数组的物理意义是什么？
     表示在固定上下边界的情况下，每列的元素总和
   - Kadane算法为什么适用于压缩数组？
     压缩数组的一维最大和对应原矩阵中相应子矩阵的和

2. 性能优化策略：
   - 预处理前缀和：可以预先计算前缀和数组，将压缩操作优化到O(1)
   - 方向选择：根据矩阵形状选择最优的枚举方向
   - 提前终止：对于某些情况可以提前结束计算

3. 工程实践要点：
   - 边界处理：空矩阵、单行矩阵等特殊情况
   - 数值范围：处理极大值可能导致的溢出问题
   - 内存使用：优化压缩数组的内存分配

4. 多语言对比优势：
   - Python：开发效率高，适合快速原型
   - Java：企业级应用，类型安全
   - C++：性能最优，适合高性能计算

5. 实际应用场景：
   - 图像处理：最大亮度区域检测
   - 数据分析：最大收益区域分析
   - 机器学习：特征选择中的区域优化
"""