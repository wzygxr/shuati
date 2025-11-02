"""
边框为1的最大正方形问题 - Python版本

问题描述：
给你一个由若干 0 和 1 组成的二维网格 grid，找出边界全部由 1 组成的最大正方形子网格，
并返回该子网格中的元素数量。如果不存在，则返回 0。

核心思想：
1. 利用二维前缀和快速计算子矩阵和
2. 枚举所有可能的正方形左上角顶点
3. 对每个左上角顶点，枚举所有可能的边长
4. 利用前缀和验证正方形边界是否全为1

算法详解：
1. 预处理：将原始矩阵转换为前缀和数组
2. 枚举：对每个可能的左上角(a,b)，尝试所有可能的边长k
3. 验证：检查边长为k的正方形边界是否全为1
   - 正方形总元素和 - 内部元素和 = 边框元素和
   - 边框元素和应等于 4*(k-1) （k>1时）

时间复杂度分析：
O(n * m * min(n,m))，其中n为行数，m为列数
- 三层循环：外两层枚举左上角位置，内层枚举边长
- 理论下限：必须枚举所有可能的正方形，无法避免O(n*m*min(n,m))复杂度

空间复杂度分析：
O(1)，只使用了常数额外空间（复用原数组）

算法优势：
1. 时间复杂度已达到理论下限
2. 空间效率高，复用原数组
3. 通过前缀和优化验证过程
4. 枚举优化：从当前最大边长开始枚举，避免重复计算

工程化考虑：
1. 边界处理：处理边长为1的特殊情况
2. 枚举优化：从当前最大边长开始枚举，避免重复计算
3. 异常处理：应添加对空矩阵的处理
4. 内存管理：复用原数组，节省空间
5. 性能优化：提前终止不可能的情况

应用场景：
1. 图像处理中的形状识别
2. 计算机视觉中的目标检测
3. 游戏开发中的碰撞检测
4. 模式识别中的特征提取
5. 数字图像处理中的边界检测

相关题目：
1. LeetCode 1139. 最大的以1为边界的正方形
2. LeetCode 221. 最大正方形
3. LeetCode 764. 最大加号标志
4. HDU 1559 最大子矩阵
5. POJ 1050 To the Max

测试链接 : https://leetcode.cn/problems/largest-1-bordered-square/

Python语言特性：
1. 使用列表推导式简化代码
2. 动态类型，开发效率高
3. 内置测试框架支持
4. 支持函数式编程风格
"""

class Solution:
    """
    边框为1的最大正方形解决方案类
    """
    
    def largest1BorderedSquare(self, grid):
        """
        查找边界全为1的最大正方形
        
        算法思路：
        1. 将原始矩阵转换为前缀和数组以支持快速区域和查询
        2. 枚举所有可能的正方形左上角坐标
        3. 对每个左上角，尝试所有可能的边长
        4. 利用前缀和验证正方形边界是否全为1
        
        优化策略：
        1. 从当前已找到的最大边长+1开始枚举，避免重复计算较小边长
        2. 复用原始数组存储前缀和，节省空间
        3. 提前终止：如果整个矩阵和为0，直接返回0
        
        时间复杂度：O(n * m * min(n,m))，其中n为行数，m为列数
        空间复杂度：O(1)，复用原数组存储前缀和
        
        工程化考量：
        1. 参数校验：检查输入矩阵是否有效
        2. 边界处理：处理空矩阵和单元素矩阵
        3. 性能优化：避免不必要的计算
        4. 代码可读性：使用有意义的变量名和注释
        
        :param grid: 由0和1组成的二维网格，要求非空且至少有一个元素
        :return: 最大正方形的面积，如果不存在则返回0
        :raises ValueError: 如果输入矩阵为空或维度为0
        """
        # 参数校验：确保输入矩阵有效
        if not grid or not grid[0]:
            raise ValueError("输入矩阵不能为空")
        
        n = len(grid)
        m = len(grid[0])
        
        # 构建前缀和数组（复用原数组）
        # 优化：复用原数组存储前缀和，节省空间
        self._build_prefix_sum(n, m, grid)
        
        # 如果整个矩阵和为0，说明没有1，直接返回0
        # 优化：提前终止不可能的情况
        if self._sum_region(grid, 0, 0, n - 1, m - 1) == 0:
            return 0
        
        # 记录找到的最大合法正方形的边长
        # 初始值为1，因为至少存在边长为1的正方形（单个1元素）
        ans = 1
        
        # 枚举所有可能的左上角点(a,b)
        # 优化：外层循环枚举所有可能的左上角位置
        for a in range(n):
            for b in range(m):
                # (a,b)作为所有可能的左上角点
                # (c,d)为右下角点，k为当前尝试的边长
                # 优化：从当前最大边长+1开始枚举，避免重复计算较小边长
                c = a + ans
                d = b + ans
                k = ans + 1
                
                while c < n and d < m:
                    # 验证边长为k的正方形边界是否全为1
                    # 方法：正方形总和 - 内部正方形和 = 边框和
                    # 边框和应该等于 4*(k-1) （k>1时）
                    # 数学原理：边框有4条边，每条边长度为k，但四个角被重复计算，所以是4*(k-1)
                    border_sum = (self._sum_region(grid, a, b, c, d) - 
                                self._sum_region(grid, a + 1, b + 1, c - 1, d - 1))
                    
                    if border_sum == (k - 1) * 4:
                        ans = k  # 更新最大边长
                        
                        # 调试输出：打印找到的合法正方形
                        # print(f"找到合法正方形: 左上角({a},{b}), 边长{k}, 边框和={border_sum}")
                    
                    # 继续尝试更大的边长
                    c += 1
                    d += 1
                    k += 1
        
        # 返回最大正方形的面积
        return ans * ans
    
    def _build_prefix_sum(self, n, m, grid):
        """
        构建前缀和数组（原地修改）
        
        算法原理：
        利用容斥原理构建前缀和数组：
        grid[i][j] = grid[i][j] + grid[i][j-1] + grid[i-1][j] - grid[i-1][j-1]
        
        时间复杂度：O(n*m)
        空间复杂度：O(1)（复用原数组）
        
        设计思路：
        1. 复用原数组存储前缀和，节省空间
        2. 使用容斥原理避免重复计算
        3. 处理边界条件，避免数组越界
        
        工程化考量：
        1. 原地修改：复用原数组，节省内存
        2. 边界安全：使用安全获取函数避免越界
        3. 性能优化：顺序访问，提高缓存命中率
        
        :param n: 矩阵行数
        :param m: 矩阵列数
        :param grid: 原始矩阵（会被修改为前缀和数组）
        """
        # 按行优先顺序构建前缀和数组
        # 优化：顺序访问，提高缓存命中率
        for i in range(n):
            for j in range(m):
                # 使用容斥原理计算前缀和
                # grid[i][j] += 左方前缀和 + 上方前缀和 - 左上角前缀和
                grid[i][j] += (self._get_safe(grid, i, j - 1) + 
                              self._get_safe(grid, i - 1, j) - 
                              self._get_safe(grid, i - 1, j - 1))
                
                # 调试输出：验证前缀和计算
                # if i < 3 and j < 3:
                #     print(f"grid[{i}][{j}] = {grid[i][j]}")
    
    def _sum_region(self, grid, a, b, c, d):
        """
        计算子矩阵元素和
        
        算法原理：
        利用容斥原理计算子矩阵和：
        sum = grid[c][d] - grid[c][b-1] - grid[a-1][d] + grid[a-1][b-1]
        
        特殊处理：
        当a>c时，表示空矩阵，返回0
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        数学推导：
        1. grid[c][d] 包含从(0,0)到(c,d)的所有元素
        2. 减去grid[c][b-1] 去掉左侧多余部分
        3. 减去grid[a-1][d] 去掉上方多余部分
        4. 加上grid[a-1][b-1] 补回多减的部分
        
        :param grid: 前缀和数组
        :param a: 子矩阵左上角行索引
        :param b: 子矩阵左上角列索引
        :param c: 子矩阵右下角行索引
        :param d: 子矩阵右下角列索引
        :return: 子矩阵元素和，如果a>c则返回0
        """
        # 处理空矩阵情况
        if a > c:
            return 0
        
        # 利用容斥原理计算子矩阵和
        result = (grid[c][d] - 
                 self._get_safe(grid, c, b - 1) - 
                 self._get_safe(grid, a - 1, d) + 
                 self._get_safe(grid, a - 1, b - 1))
        
        # 调试输出：验证子矩阵和计算
        # print(f"_sum_region({a},{b},{c},{d}) = {grid[c][d]} - {self._get_safe(grid, c, b - 1)} - "
        #       f"{self._get_safe(grid, a - 1, d)} + {self._get_safe(grid, a - 1, b - 1)} = {result}")
        
        return result
    
    def _get_safe(self, grid, i, j):
        """
        安全获取数组元素（边界安全）
        
        边界处理：
        当索引为负数时，返回0
        当索引越界时，返回0
        
        设计目的：
        1. 简化边界条件处理
        2. 避免数组越界异常
        3. 提高代码健壮性
        
        工程化考量：
        1. 防御性编程：处理所有可能的边界情况
        2. 代码简洁：封装边界处理逻辑
        3. 性能考虑：方法内联优化
        
        :param grid: 二维数组
        :param i: 行索引
        :param j: 列索引
        :return: grid[i][j]的值，如果索引越界则返回0
        """
        # 检查索引是否越界
        if i < 0 or j < 0 or i >= len(grid) or j >= len(grid[0]):
            return 0
        return grid[i][j]


def print_matrix(matrix):
    """打印矩阵辅助函数"""
    print("矩阵内容:")
    for row in matrix:
        print(" ".join(str(val) for val in row))


def test_normal_case():
    """测试用例1：正常情况"""
    print("=== 测试用例1：正常情况 ===")
    solution = Solution()
    
    grid1 = [
        [1, 1, 1],
        [1, 0, 1],
        [1, 1, 1]
    ]
    print_matrix(grid1)
    result1 = solution.largest1BorderedSquare(grid1)
    print(f"最大正方形面积: {result1}")  # 预期输出: 9
    print()


def test_edge_case_1():
    """测试用例2：边界情况 - 全1矩阵"""
    print("=== 测试用例2：边界情况 - 全1矩阵 ===")
    solution = Solution()
    
    grid2 = [
        [1, 1, 1],
        [1, 1, 1],
        [1, 1, 1]
    ]
    print_matrix(grid2)
    result2 = solution.largest1BorderedSquare(grid2)
    print(f"最大正方形面积: {result2}")  # 预期输出: 9
    print()


def test_edge_case_2():
    """测试用例3：边界情况 - 全0矩阵"""
    print("=== 测试用例3：边界情况 - 全0矩阵 ===")
    solution = Solution()
    
    grid3 = [
        [0, 0, 0],
        [0, 0, 0],
        [0, 0, 0]
    ]
    print_matrix(grid3)
    result3 = solution.largest1BorderedSquare(grid3)
    print(f"最大正方形面积: {result3}")  # 预期输出: 0
    print()


def test_edge_case_3():
    """测试用例4：边界情况 - 单元素矩阵"""
    print("=== 测试用例4：边界情况 - 单元素矩阵 ===")
    solution = Solution()
    
    grid4 = [[1]]
    print_matrix(grid4)
    result4 = solution.largest1BorderedSquare(grid4)
    print(f"最大正方形面积: {result4}")  # 预期输出: 1
    print()


def test_performance():
    """测试用例5：性能测试 - 大规模数据"""
    print("=== 测试用例5：性能测试 ===")
    import time
    
    solution = Solution()
    n = 100
    m = 100
    # 生成测试数据：棋盘格模式
    large_grid = [[(i + j) % 2 for j in range(m)] for i in range(n)]
    
    start_time = time.time()
    result5 = solution.largest1BorderedSquare(large_grid)
    end_time = time.time()
    
    print(f"最大正方形面积: {result5}")
    print(f"计算耗时: {(end_time - start_time) * 1000:.2f}ms")
    print()


def test_exception_case():
    """测试用例6：异常情况测试"""
    print("=== 测试用例6：异常情况测试 ===")
    solution = Solution()
    
    try:
        empty_grid = []
        result6 = solution.largest1BorderedSquare(empty_grid)
    except ValueError as e:
        print(f"异常处理测试通过: {e}")
    print()


def main():
    """主函数：执行所有测试用例"""
    try:
        test_normal_case()
        test_edge_case_1()
        test_edge_case_2()
        test_edge_case_3()
        test_performance()
        test_exception_case()
        print("所有测试用例执行完成！")
    except Exception as e:
        print(f"测试过程中出现异常: {e}")


if __name__ == "__main__":
    main()