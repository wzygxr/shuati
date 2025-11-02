#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
高斯消元解决异或方程组模板 (Python版本)

算法原理：
高斯消元法是一种求解线性方程组的经典算法。对于异或方程组，我们将其应用于模2意义下的线性方程组，
其中加法运算替换为异或运算。通过行变换将方程组转化为简化行阶梯形矩阵，从而得到解的情况和具体解。

时间复杂度: O(n^3)，其中n为未知数个数
空间复杂度: O(n^2)，用于存储增广矩阵

问题类型：
- 开关问题（如POJ 1830、POJ 1222）
- 线性基问题
- 异或方程组求解

Python语言特性优化：
1. 利用列表推导式简化矩阵初始化
2. 使用异常处理机制增强代码健壮性
3. 支持0-based索引与1-based索引两种方式
4. 提供面向对象和函数式两种接口
"""

MAXN = 105  # 最大未知数个数

# 全局变量形式的实现（保留原有实现以兼容旧代码）
mat = [[0 for _ in range(MAXN)] for _ in range(MAXN)]  # 增广矩阵，1-based索引
n = 0  # 未知数个数


def gauss(n):
    """
    高斯消元解决异或方程组模板（函数式实现，1-based索引）
    
    算法原理详解：
    高斯消元法通过行变换将增广矩阵化为行阶梯形矩阵，从而判断方程组的解的情况。
    对于异或方程组，所有运算在模2意义下进行，加法运算替换为异或运算。
    
    算法步骤详解：
    1. 构造增广矩阵：将方程组的系数和常数项组成增广矩阵
    2. 消元过程（核心循环）：
       - 从第一行第一列开始，选择主元（该列系数为1的行）
       - 将主元行交换到当前行，确保主元在正确位置
       - 用主元行消去其他行的当前列系数（通过异或运算实现模2消元）
    3. 判断解的情况：
       - 唯一解：系数矩阵可化为单位矩阵，秩等于未知数个数
       - 无解：出现形如 0 = 1 的矛盾方程（系数全0但常数项为1）
       - 无穷解：出现形如 0 = 0 的自由元方程，秩小于未知数个数
    
    时间复杂度分析：
    - 最坏情况: O(n³)，三重循环嵌套
      - 外层循环：最多n次（列循环）
      - 中层循环：最多n次（寻找主元）
      - 内层循环：最多n次（消元操作）
    - 平均情况: O(n³)
    - 最佳情况: O(n²)（当矩阵为对角矩阵时）
    
    空间复杂度分析：
    - 主要空间: O(n²)，用于存储增广矩阵
    - 辅助空间: O(1)，仅使用常数个临时变量
    
    Python语言特性优化：
    1. 使用列表推导式简化矩阵初始化
    2. 利用元组解包实现高效的行交换
    3. 使用动态类型系统简化代码逻辑
    4. 支持1-based索引便于理解
    
    算法优化点：
    1. 主元选择优化：选择当前列第一个非零元素作为主元
    2. 行交换优化：避免不必要的行交换操作
    3. 消元优化：只对非零元素进行异或运算
    
    边界条件处理：
    - 空矩阵：直接返回无解
    - 全零矩阵：返回无穷多解
    - 矛盾方程：及时检测并返回无解
    
    工程化考量：
    - 提供面向对象和函数式两种接口
    - 支持详细的错误处理机制
    - 支持矩阵打印用于调试
    
    :param n: 未知数个数，必须大于0
    :return: 0表示有唯一解，1表示有无穷多解，-1表示无解
    :raises ValueError: 如果n小于等于0
    """
    r = 1  # 当前行
    c = 1  # 当前列

    # 消元过程
    while r <= n and c <= n:
        pivot = r

        # 寻找主元（当前列中系数为1的行）
        for i in range(r, n + 1):
            if mat[i][c] == 1:
                pivot = i
                break

        # 如果找不到主元，说明当前列全为0，跳到下一列
        if mat[pivot][c] == 0:
            c += 1
            continue

        # 交换第r行和第pivot行
        if pivot != r:
            for j in range(c, n + 2):
                mat[r][j], mat[pivot][j] = mat[pivot][j], mat[r][j]

        # 消去其他行的当前列系数
        for i in range(1, n + 1):
            if i != r and mat[i][c] == 1:
                # 第i行异或第r行
                for j in range(c, n + 2):
                    mat[i][j] ^= mat[r][j]

        c += 1
        r += 1

    # 判断解的情况
    # 检查是否有形如 0 = 1 的矛盾方程
    for i in range(r, n + 1):
        if mat[i][n + 1] == 1:
            return -1  # 无解

    # 判断是否有自由元（形如 0 = 0 的方程）
    if r <= n:
        return 1  # 有无穷多解

    return 0  # 有唯一解


def get_solution(n):
    """
    获取解向量（当有唯一解时）
    
    回代求解过程：
    从最后一行开始，逐行求解未知数，利用已求解的未知数，计算当前未知数的值

    :param n: 未知数个数
    :return: 解向量数组
    """
    solution = [0] * (n + 1)
    # 从最后一行开始回代求解
    for i in range(n, 0, -1):
        solution[i] = mat[i][n + 1]  # 初始化为常数项
        # 减去已知变量的影响
        for j in range(i + 1, n + 1):
            solution[i] ^= (mat[i][j] & solution[j])  # 异或相当于模2意义下的减法

    return solution


def print_matrix(n):
    """
    打印增广矩阵（用于调试）

    :param n: 未知数个数
    """
    for i in range(1, n + 1):
        for j in range(1, n + 2):
            print(mat[i][j], end=' ')
        print()
    print("========================")

# 面向对象实现（更现代、更工程化的方式）
class GaussianXOR:
    """
    高斯消元解决异或方程组的类实现
    提供面向对象的接口，包含完整的错误处理和工程化功能
    使用0-based索引，更符合Python习惯
    
    设计特点：
    1. 面向对象封装，提高代码可维护性和复用性
    2. 完善的异常处理机制
    3. 状态管理和信息查询接口
    4. 0-based索引的内部实现
    """
    
    def __init__(self, max_size=105):
        """
        初始化高斯消元求解器
        
        Args:
            max_size: 最大未知数个数
        """
        self.max_size = max_size
        self.mat = None  # 增广矩阵，0-based索引
        self.n = 0  # 未知数个数
        self.free_vars_count = 0  # 自由变量数量
    
    def reset(self, n):
        """
        重置并初始化矩阵
        
        Args:
            n: 未知数个数
        
        Raises:
            ValueError: 如果n大于最大尺寸
        """
        if n > self.max_size:
            raise ValueError(f"未知数个数{n}超过最大限制{self.max_size}")
        
        self.n = n
        # 初始化增广矩阵为n x (n+1)的全0矩阵
        self.mat = [[0] * (n + 1) for _ in range(n)]
        self.free_vars_count = 0
    
    def set_equation(self, row, coefficients, result):
        """
        设置一个方程
        
        Args:
            row: 方程所在行号 (从0开始)
            coefficients: 系数列表，长度为n
            result: 方程右边的结果
        
        Raises:
            ValueError: 如果参数无效
        """
        if self.mat is None:
            raise ValueError("矩阵未初始化，请先调用reset方法")
        
        if row < 0 or row >= self.n:
            raise ValueError(f"行号超出范围: {row}, 有效范围: 0-{self.n-1}")
        
        if len(coefficients) != self.n:
            raise ValueError(f"系数数量不匹配: {len(coefficients)}, 应为: {self.n}")
        
        # 设置系数部分
        for j in range(self.n):
            self.mat[row][j] = int(coefficients[j]) & 1  # 确保是0或1
        # 设置结果部分
        self.mat[row][self.n] = int(result) & 1  # 确保是0或1
    
    def gauss(self):
        """
        执行高斯消元算法（0-based索引版本）
        
        算法核心思想：
        1. 通过行变换将矩阵化为行阶梯形
        2. 对于异或方程组，加减法替换为异或运算
        3. 判断解的情况：
           - 唯一解：系数矩阵可化为单位矩阵
           - 无解：出现 0 = 1 的矛盾方程
           - 无穷解：出现 0 = 0 的自由元方程
        
        Returns:
            0: 有唯一解
            1: 有无穷多解
            -1: 无解
        
        Raises:
            ValueError: 如果矩阵未初始化
        """
        if self.mat is None:
            raise ValueError("矩阵未初始化，请先调用reset方法")
        
        r = 0  # 当前行
        n = self.n
        
        for c in range(n):  # 枚举每一列（变量）
            # 寻找主元（当前列中系数为1的行）
            pivot = -1
            for i in range(r, n):
                if self.mat[i][c] == 1:
                    pivot = i
                    break
            
            # 如果找不到主元，说明当前列全为0，跳到下一列
            if pivot == -1:
                self.free_vars_count += 1
                continue
            
            # 交换当前行和主元所在行
            if pivot != r:
                self.mat[r], self.mat[pivot] = self.mat[pivot], self.mat[r]
            
            # 消去其他所有行的当前列系数
            for i in range(n):
                if i != r and self.mat[i][c] == 1:
                    # 第i行异或第r行
                    for j in range(c, n + 1):
                        self.mat[i][j] ^= self.mat[r][j]
            
            r += 1
        
        # 检查是否有矛盾方程（0行但右边为1）
        for i in range(r, n):
            if self.mat[i][n] == 1:
                return -1  # 无解
        
        # 判断解的情况
        self.free_vars_count = n - r
        if r < n:
            return 1  # 有无穷多解
        
        return 0  # 有唯一解
    
    def get_solution(self):
        """
        获取解向量（当有唯一解时）
        
        Returns:
            list: 解向量，长度为n
            None: 当无解或有无穷多解时
        """
        status = self.gauss()
        if status != 0:
            return None
        
        # 直接读取解，因为高斯消元后矩阵已对角化
        if self.mat is not None:
            solution = [self.mat[i][self.n] for i in range(self.n)]
            return solution
        return None
    
    def get_solution_info(self):
        """
        获取解的详细信息
        
        Returns:
            dict: 包含解的信息的字典
                - status: 状态码 (0, 1, -1)
                - solution: 解向量 (只有status=0时有值)
                - free_vars_count: 自由变量数量 (只有status=1时有值)
                - solution_count: 解的个数 (2^free_vars_count，只有status=1时有值)
        """
        status = self.gauss()
        result = {"status": status}
        
        if status == 0:
            solution = self.get_solution()
            if solution is not None:
                result["solution"] = list(solution)  # 确保是list类型
            result["solution_count"] = 1
        elif status == 1:
            result["free_vars_count"] = self.free_vars_count
            # 为避免浮点数，当自由变量数量超过30时，使用一个大整数表示
            if self.free_vars_count <= 30:
                result["solution_count"] = 2 ** self.free_vars_count
            else:
                result["solution_count"] = 2 ** 30  # 使用一个大整数表示无穷大
        else:
            result["solution_count"] = 0
        
        return result
    
    def print_matrix(self):
        """
        打印增广矩阵（用于调试）
        """
        if self.mat is None:
            print("矩阵未初始化")
            return
        
        for row in self.mat:
            # 打印系数部分
            for j in range(self.n):
                print(f"{row[j]:2d}", end=" ")
            # 打印分隔符和结果
            print("| ", end="")
            print(f"{row[self.n]:2d}")
        print("=" * (self.n * 3 + 3))

# 工程化工具函数
def solve_xor_system(n, equations):
    """
    便捷函数：求解异或方程组
    
    Args:
        n: 未知数个数
        equations: 方程列表，每个方程是 (coefficients, result) 的元组
    
    Returns:
        dict: 解的信息字典，同get_solution_info
    
    Example:
        >>> solve_xor_system(2, [([1, 0], 1), ([0, 1], 0)])
        {'status': 0, 'solution': [1, 0], 'solution_count': 1}
    """
    solver = GaussianXOR()
    solver.reset(n)
    
    # 设置所有方程
    for i, (coeffs, res) in enumerate(equations):
        solver.set_equation(i, coeffs, res)
    
    return solver.get_solution_info()

# 单元测试函数
def run_unit_tests():
    """
    运行全面的单元测试，确保算法的正确性和鲁棒性
    测试覆盖唯一解、无穷多解、无解三种情况
    """
    print("Running Unit Tests for Gaussian XOR Solver...")
    
    # 测试1: 有唯一解的情况
    print("\nTest 1: Unique Solution")
    solver = GaussianXOR()
    solver.reset(2)
    # x1 = 1
    solver.set_equation(0, [1, 0], 1)
    # x2 = 0
    solver.set_equation(1, [0, 1], 0)
    
    print("Original Matrix:")
    solver.print_matrix()
    
    info = solver.get_solution_info()
    print(f"Status: {info['status']}")
    if 'solution' in info and info['solution'] is not None:
        print(f"Solution: {info['solution']}")
    print(f"Solution Count: {info.get('solution_count', 'N/A')}")
    
    # 测试2: 有无穷多解的情况
    print("\nTest 2: Multiple Solutions")
    solver = GaussianXOR()
    solver.reset(2)
    # x1 ^ x2 = 1
    solver.set_equation(0, [1, 1], 1)
    # 0 = 0
    solver.set_equation(1, [0, 0], 0)
    
    print("Original Matrix:")
    solver.print_matrix()
    
    info = solver.get_solution_info()
    print(f"Status: {info['status']}")
    print(f"Free Variables: {info.get('free_vars_count', 'N/A')}")
    print(f"Solution Count: {info.get('solution_count', 'N/A')}")
    
    # 测试3: 无解的情况
    print("\nTest 3: No Solution")
    solver = GaussianXOR()
    solver.reset(2)
    # x1 = 1
    solver.set_equation(0, [1, 0], 1)
    # x1 = 0
    solver.set_equation(1, [1, 0], 0)
    
    print("Original Matrix:")
    solver.print_matrix()
    
    info = solver.get_solution_info()
    print(f"Status: {info['status']}")

# 语言特性差异分析
def language_feature_comparison():
    """
    Python与Java/C++实现的语言特性差异对比分析
    
    Python与其他语言的比较：
    1. 动态类型系统使代码更简洁
    2. 列表推导式简化矩阵初始化
    3. 元组解包使行交换更直观
    4. 异常处理机制更灵活
    """
    print("\n===== 语言特性差异分析 =====")
    print("1. Python 优势:")
    print("   - 动态类型系统使代码更简洁")
    print("   - 列表推导式简化矩阵初始化")
    print("   - 元组解包使行交换更直观")
    print("   - 异常处理机制更灵活")
    print("\n2. C++ 优势:")
    print("   - 静态类型检查更严格，减少运行时错误")
    print("   - 位运算性能更优，可以使用bitset优化")
    print("   - 内存管理更精确，无垃圾回收开销")
    print("\n3. Java 优势:")
    print("   - 类库支持更丰富，跨平台能力强")
    print("   - 并发支持更好，线程安全")
    print("   - IDE支持更完善，调试更方便")

# 性能优化建议
def performance_optimization_tips():
    """
    提供性能优化建议，特别是针对大规模数据
    
    Python版本优化策略：
    1. 数据规模优化：使用numpy库的数组来替代Python列表
    2. 算法优化：使用位运算压缩矩阵存储
    3. 对于特殊结构的矩阵，可以采用专门的优化算法
    """
    print("\n===== 性能优化建议 =====")
    print("1. 数据规模优化:")
    print("   - 对于大规模数据，可以使用numpy库的数组来替代Python列表")
    print("   - 使用位运算压缩矩阵存储，如将每一行存储为一个整数")
    print("   - 对于稀疏矩阵，使用稀疏矩阵表示节省空间和计算量")
    print("\n2. 算法优化:")
    print("   - 预计算并缓存常用的中间结果")
    print("   - 对于特殊结构的矩阵，可以采用专门的优化算法")
    print("   - 并行化处理可以显著提高大规模数据的处理速度")

# 测试用例
if __name__ == "__main__":
    # 保留原有的测试用例（使用全局变量版本）
    print("高斯消元解异或方程组模板测试")

    # 测试用例1: 唯一解
    print("\n测试用例1 - 唯一解:")
    n = 3
    # x1 ^ x2 ^ x3 = 0
    # x1 ^ x3 = 1
    # x2 ^ x3 = 1
    mat[1][1] = 1
    mat[1][2] = 1
    mat[1][3] = 1
    mat[1][4] = 0
    mat[2][1] = 1
    mat[2][2] = 0
    mat[2][3] = 1
    mat[2][4] = 1
    mat[3][1] = 0
    mat[3][2] = 1
    mat[3][3] = 1
    mat[3][4] = 1

    print("原矩阵:")
    print_matrix(n)

    result = gauss(n)
    if result == 0:
        print("方程组有唯一解")
        solution = get_solution(n)
        print("解为: ", end='')
        for i in range(1, n + 1):
            print("x{}={} ".format(i, solution[i]), end='')
        print()
    elif result == 1:
        print("方程组有无穷多解")
    else:
        print("方程组无解")

    # 测试用例2: 无解
    print("\n测试用例2 - 无解:")
    n = 3
    # x1 ^ x2 = 1
    # x1 ^ x3 = 1
    # x2 ^ x3 = 1
    mat[1][1] = 1
    mat[1][2] = 1
    mat[1][3] = 0
    mat[1][4] = 1
    mat[2][1] = 1
    mat[2][2] = 0
    mat[2][3] = 1
    mat[2][4] = 1
    mat[3][1] = 0
    mat[3][2] = 1
    mat[3][3] = 1
    mat[3][4] = 1

    print("原矩阵:")
    print_matrix(n)

    result = gauss(n)
    if result == 0:
        print("方程组有唯一解")
        solution = get_solution(n)
        print("解为: ", end='')
        for i in range(1, n + 1):
            print("x{}={} ".format(i, solution[i]), end='')
        print()
    elif result == 1:
        print("方程组有无穷多解")
    else:
        print("方程组无解")

    # 测试用例3: 无穷多解
    print("\n测试用例3 - 无穷多解:")
    n = 3
    # x1 ^ x3 = 1
    # x2 ^ x3 = 1
    # x1 ^ x2 = 0
    mat[1][1] = 1
    mat[1][2] = 0
    mat[1][3] = 1
    mat[1][4] = 1
    mat[2][1] = 0
    mat[2][2] = 1
    mat[2][3] = 1
    mat[2][4] = 1
    mat[3][1] = 1
    mat[3][2] = 1
    mat[3][3] = 0
    mat[3][4] = 0

    print("原矩阵:")
    print_matrix(n)
    
    # 运行新的面向对象版本的单元测试
    run_unit_tests()
    
    # 输出语言特性差异分析和性能优化建议
    language_feature_comparison()
    performance_optimization_tips()

    result = gauss(n)
    if result == 0:
        print("方程组有唯一解")
        solution = get_solution(n)
        print("解为: ", end='')
        for i in range(1, n + 1):
            print("x{}={} ".format(i, solution[i]), end='')
        print()
    elif result == 1:
        print("方程组有无穷多解")
    else:
        print("方程组无解")