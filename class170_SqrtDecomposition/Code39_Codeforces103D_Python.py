import time
import random

class MoJumpSum:
    """
    Codeforces 103D Time to Raid Cowavans
    题目要求：多次跳跃查询区间和
    核心技巧：分块预处理
    时间复杂度：O(n√n) 预处理，O(√n) 查询
    测试链接：https://codeforces.com/problemset/problem/103/D
    
    该问题的核心思想是：对于每个查询(l, k)，我们需要计算从位置l开始，每隔k步取一个元素的和。
    直接暴力计算的时间复杂度为O(n)，对于大量查询会超时。
    使用分块预处理的方法，我们可以将时间复杂度优化到预处理O(n√n)，查询O(√n)。
    """
    
    def __init__(self, n, a):
        """
        初始化类
        
        Args:
            n (int): 数组长度
            a (list): 输入数组（1-based索引）
        """
        self.n = n
        self.a = a.copy()  # 复制数组，避免修改原数组
        self.BLOCK_SIZE = int(n**0.5) + 1
        self.sum = []
        self.preprocess()
    
    def preprocess(self):
        """
        预处理函数
        对于步长k <= BLOCK_SIZE的情况，预处理每个起始位置的和
        对于步长k > BLOCK_SIZE的情况，查询时暴力计算，因为这种情况下查询次数较少
        """
        # 初始化预处理数组，sum[k][i]表示步长为k时，从位置i开始的和
        self.sum = [[0] * (self.n + 2) for _ in range(self.BLOCK_SIZE + 1)]
        
        # 预处理小步长的情况（k <= BLOCK_SIZE）
        for k in range(1, self.BLOCK_SIZE + 1):
            # 从后往前预处理，避免重复计算
            for i in range(self.n, 0, -1):
                self.sum[k][i] = self.a[i]
                if i + k <= self.n:
                    self.sum[k][i] += self.sum[k][i + k]
    
    def query(self, l, k):
        """
        查询函数
        
        Args:
            l (int): 起始位置（1-based）
            k (int): 步长
            
        Returns:
            long: 从位置l开始，每隔k步取一个元素的和
        """
        # 异常处理
        if k <= 0:
            raise ValueError("步长k必须为正数")
        
        # 如果步长k很小，直接使用预处理的结果
        if k <= self.BLOCK_SIZE:
            return self.sum[k][l]
        
        # 对于大步长，直接暴力计算，因为最多需要计算n/k次，而k > sqrt(n)，所以最多计算sqrt(n)次
        res = 0
        i = l
        while i <= self.n:
            res += self.a[i]
            i += k
        return res

# 优化版本：减少函数调用开销
def optimized_jump_sum(n, a, BLOCK_SIZE=None):
    """
    优化版本的跳跃和查询函数
    使用闭包来减少函数调用开销
    
    Args:
        n (int): 数组长度
        a (list): 输入数组（1-based索引）
        BLOCK_SIZE (int, optional): 块大小，如果为None则自动计算
    
    Returns:
        function: 查询函数
    """
    if BLOCK_SIZE is None:
        BLOCK_SIZE = int(n**0.5) + 1
    
    # 预处理数组
    sum_table = [[0] * (n + 2) for _ in range(BLOCK_SIZE + 1)]
    
    # 预处理
    for k in range(1, BLOCK_SIZE + 1):
        for i in range(n, 0, -1):
            sum_table[k][i] = a[i]
            if i + k <= n:
                sum_table[k][i] += sum_table[k][i + k]
    
    # 查询函数
    def query(l, k):
        if k <= 0:
            return 0
        if k <= BLOCK_SIZE:
            return sum_table[k][l]
        res = 0
        i = l
        while i <= n:
            res += a[i]
            i += k
        return res
    
    return query

# 主函数，处理输入输出
def main():
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    a = [0] * (n + 1)  # 1-based索引
    for i in range(1, n + 1):
        a[i] = int(input[ptr])
        ptr += 1
    
    # 创建对象
    mo = MoJumpSum(n, a)
    
    q = int(input[ptr])
    ptr += 1
    
    for _ in range(q):
        l = int(input[ptr])
        k = int(input[ptr + 1])
        ptr += 2
        print(mo.query(l, k))

# 正确性测试函数
def correctness_test():
    print("=== 正确性测试 ===")
    
    # 测试用例1：小步长查询
    n1 = 10
    a1 = [0] * (n1 + 1)
    for i in range(1, n1 + 1):
        a1[i] = i
    
    mo1 = MoJumpSum(n1, a1)
    print(f"查询 (l=1, k=2): {mo1.query(1, 2)}")  # 应为1+3+5+7+9=25
    print(f"查询 (l=2, k=3): {mo1.query(2, 3)}")  # 应为2+5+8=15
    print(f"查询 (l=1, k=1): {mo1.query(1, 1)}")  # 应为55
    
    # 测试用例2：大步长查询
    n2 = 12
    a2 = [0] * (n2 + 1)
    for i in range(1, n2 + 1):
        a2[i] = i * 10
    
    mo2 = MoJumpSum(n2, a2)
    print(f"查询 (l=3, k=500): {mo2.query(3, 500)}")  # 应为30
    print(f"查询 (l=1, k=4): {mo2.query(1, 4)}")  # 应为10+50+90=150
    
    # 测试边界情况
    print(f"查询 (l=10, k=1): {mo2.query(10, 1)}")  # 应为100+110+120=330
    print(f"查询 (l=12, k=100): {mo2.query(12, 100)}")  # 应为120
    
    # 测试优化版本
    query_func = optimized_jump_sum(n1, a1)
    print(f"\n优化版本测试 (l=1, k=2): {query_func(1, 2)}")  # 应为25
    print(f"优化版本测试 (l=2, k=3): {query_func(2, 3)}")  # 应为15

# 性能测试函数
def performance_test():
    print("\n=== 性能测试 ===")
    
    # 测试大规模数据
    n = 10000
    a = [0] * (n + 1)
    for i in range(1, n + 1):
        a[i] = i
    
    # 测试原始版本
    start_time = time.time()
    mo = MoJumpSum(n, a)
    preprocess_time = (time.time() - start_time) * 1000  # 转换为毫秒
    print(f"预处理1e4数据耗时：{preprocess_time:.2f}ms")
    
    # 测试查询性能
    q = 10000
    random.seed(42)
    total_result = 0
    
    start_time = time.time()
    for _ in range(q):
        l = random.randint(1, n)
        k = random.randint(1, n)
        total_result += mo.query(l, k)
    query_time = (time.time() - start_time) * 1000  # 转换为毫秒
    
    print(f"处理1e4查询耗时：{query_time:.2f}ms")
    print(f"总结果（避免编译器优化）：{total_result}")
    
    # 测试优化版本
    start_time = time.time()
    query_func = optimized_jump_sum(n, a)
    opt_preprocess_time = (time.time() - start_time) * 1000  # 转换为毫秒
    print(f"\n优化版本预处理耗时：{opt_preprocess_time:.2f}ms")
    
    total_result = 0
    start_time = time.time()
    for _ in range(q):
        l = random.randint(1, n)
        k = random.randint(1, n)
        total_result += query_func(l, k)
    opt_query_time = (time.time() - start_time) * 1000  # 转换为毫秒
    
    print(f"优化版本处理1e4查询耗时：{opt_query_time:.2f}ms")
    print(f"总结果：{total_result}")

# 块大小优化分析函数
def block_size_analysis():
    print("\n=== 块大小优化分析 ===")
    
    n = 10000
    a = [0] * (n + 1)
    for i in range(1, n + 1):
        a[i] = i
    
    block_sizes = [50, 100, 150, 200, 250, 300, 320, 400]
    
    for bs in block_sizes:
        # 测试预处理时间
        start_time = time.time()
        query_func = optimized_jump_sum(n, a, bs)
        preprocess_time = (time.time() - start_time) * 1000  # 转换为毫秒
        
        # 测试查询性能
        q = 10000
        random.seed(42)
        total_result = 0
        
        start_time = time.time()
        for _ in range(q):
            l = random.randint(1, n)
            k = random.randint(1, n)
            total_result += query_func(l, k)
        query_time = (time.time() - start_time) * 1000  # 转换为毫秒
        
        print(f"块大小={bs}: 预处理耗时={preprocess_time:.2f}ms, 查询耗时={query_time:.2f}ms")

# 边界情况测试
def boundary_test():
    print("\n=== 边界情况测试 ===")
    
    # 测试n=1的情况
    n1 = 1
    a1 = [0] * (n1 + 1)
    a1[1] = 100
    mo1 = MoJumpSum(n1, a1)
    print(f"n=1, 查询(1,1): {mo1.query(1, 1)}")  # 应为100
    print(f"n=1, 查询(1,100): {mo1.query(1, 100)}")  # 应为100
    
    # 测试l=n的情况
    n2 = 1000
    a2 = [0] * (n2 + 1)
    for i in range(1, n2 + 1):
        a2[i] = i
    mo2 = MoJumpSum(n2, a2)
    print(f"l=n=1000, k=1: {mo2.query(n2, 1)}")  # 应为1000
    print(f"l=n=1000, k=500: {mo2.query(n2, 500)}")  # 应为1000
    
    # 测试k=0的情况（题目中k应该是正数，这里进行健壮性测试）
    try:
        mo2.query(1, 0)
    except ValueError as e:
        print(f"k=0异常处理正常: {e}")

# 运行所有测试的函数
def run_all_tests():
    correctness_test()
    performance_test()
    block_size_analysis()
    boundary_test()

# 算法原理解析
"""
1. 问题分析：
   - 给定一个数组，多次查询从某个位置l开始，每隔k步取一个元素的和
   - 直接暴力解法：对于每个查询，遍历所有符合条件的位置，时间复杂度O(n) per query
   - 当n和q都很大时，暴力解法会超时

2. 分块思想：
   - 将步长k分为两类：小步长(k <= √n)和大步长(k > √n)
   - 对于小步长：预处理所有可能的起始位置的和
   - 对于大步长：由于k > √n，每个查询最多需要访问√n个元素，直接暴力计算

3. Python特定优化：
   - 使用列表而不是其他数据结构来存储预处理结果
   - 创建类版本和函数版本，函数版本可以减少方法调用开销
   - 使用闭包来封装预处理结果，避免全局变量
   - 在Python中，由于性能限制，测试数据规模适当减小
   - 使用生成器或列表推导式代替部分循环

4. 时间复杂度分析：
   - 预处理时间：O(√n * n)
   - 查询时间：
     - 小步长查询：O(1)
     - 大步长查询：O(√n)
   - 总体时间复杂度：O(n√n) 预处理 + O(q√n) 查询

5. 空间复杂度分析：
   - O(n√n) 用于存储预处理的结果
   - 在Python中，列表的内存使用相对较高，需要注意大规模数据的内存使用

6. 优化技巧：
   - 预处理顺序优化：从后往前计算可以避免重复计算
   - 函数调用优化：减少函数调用次数可以提高Python性能
   - 异常处理：添加必要的异常检查
   - 边界检查：确保所有索引操作都在有效范围内

7. Python性能注意事项：
   - Python的循环速度相对较慢，对于大规模数据，可能需要考虑其他语言
   - 可以使用NumPy等库来加速数组操作
   - 对于非常大的n，预处理可能会消耗较多内存，需要调整块大小

8. 最优解分析：
   - 该分块预处理方法是该问题的最优解之一
   - 理论上无法进一步降低时间复杂度
   - 在Python中，由于语言特性，可能需要根据实际情况调整实现方式
   - 对于时间限制较严格的场景，建议使用C++实现
"""

if __name__ == "__main__":
    # 运行主函数或测试
    # main()  # 实际使用时取消注释
    run_all_tests()  # 运行测试时使用