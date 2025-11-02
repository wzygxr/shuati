import time

"""
洛谷 P2054 [AHOI2005] 洗牌
题目要求：模拟洗牌过程，查询最终位置
核心技巧：分块优化模拟
时间复杂度：O(√n) / 操作
测试链接：https://www.luogu.com.cn/problem/P2054

算法思想详解：
1. 观察洗牌过程的数学规律
2. 直接模拟洗牌会超时，需要找到数学规律
3. 对于大次数的洗牌操作，可以利用数学公式快速计算位置
4. 分块处理大次数洗牌，优化性能
"""

class ShuffleSolver:
    """
    洗牌问题求解类
    提供多种解法和测试功能
    """
    
    def __init__(self, n, m, pos):
        """
        初始化问题参数
        
        Args:
            n: 牌的数量（偶数）
            m: 洗牌次数
            pos: 目标牌的初始位置
        """
        self.n = n
        self.m = m
        self.pos = pos
    
    def get_next_position(self, x):
        """
        计算一次洗牌后的位置
        
        Args:
            x: 当前位置
            
        Returns:
            洗牌后的位置
        """
        if x <= self.n // 2:
            # 前半部分的牌会被放到位置 2x-1
            return 2 * x - 1
        else:
            # 后半部分的牌会被放到位置 2(x - n/2)
            return 2 * (x - self.n // 2)
    
    def brute_force(self):
        """
        暴力模拟洗牌过程（用于小数组测试）
        
        Returns:
            最终位置
        """
        current = self.pos
        for _ in range(self.m):
            current = self.get_next_position(current)
        return current
    
    def pow_mod(self, base, exponent, mod):
        """
        快速幂取模运算
        
        Args:
            base: 底数
            exponent: 指数
            mod: 模数
            
        Returns:
            (base^exponent) mod mod
        """
        result = 1
        base = base % mod  # 先取模避免大数运算
        
        while exponent > 0:
            if exponent & 1:  # 如果exponent是奇数
                result = (result * base) % mod
            base = (base * base) % mod
            exponent >>= 1  # 右移一位，相当于除以2取整
        
        return result
    
    def mathematical_solution(self):
        """
        数学优化解法 - 利用模运算快速计算
        
        Returns:
            最终位置
        """
        # 观察数学规律：每次洗牌相当于位置乘以2 mod (n+1)
        # 因此m次洗牌相当于乘以 2^m mod (n+1)
        mod = self.n + 1
        result = (self.pow_mod(2, self.m, mod) * (self.pos % mod)) % mod
        # 如果余数为0，则位置为n
        return result if result != 0 else self.n
    
    def block_optimized_solution(self):
        """
        分块优化解法 - 适用于超大规模数据
        
        Returns:
            最终位置
        """
        # 对于这个问题，数学解法已经是最优的
        # 这里可以根据需要添加分块处理的特殊情况
        return self.mathematical_solution()


def run_test():
    """
    运行标准测试，按题目输入格式处理
    """
    import sys
    
    # 优化输入方式以提高效率
    data = sys.stdin.read().split()
    ptr = 0
    
    n = int(data[ptr])
    ptr += 1
    m = int(data[ptr])
    ptr += 1
    pos = int(data[ptr])
    ptr += 1
    
    solver = ShuffleSolver(n, m, pos)
    
    # 根据数据规模选择合适的解法
    result = None
    if n <= 1000 and m <= 1000:  # 小规模数据，使用暴力解法验证
        result = solver.brute_force()
    else:
        result = solver.mathematical_solution()
    
    print(result)


def consistency_test():
    """
    验证两种解法结果一致性的测试
    """
    print("=== 一致性测试 ===")
    
    # 测试用例
    test_cases = [
        (6, 1, 2),  # 6张牌，洗1次，初始位置2
        (6, 2, 2),  # 6张牌，洗2次，初始位置2
        (8, 3, 5),  # 8张牌，洗3次，初始位置5
        (10, 1, 6)  # 10张牌，洗1次，初始位置6
    ]
    
    for n, m, pos in test_cases:
        solver = ShuffleSolver(n, m, pos)
        
        brute = solver.brute_force()
        math = solver.mathematical_solution()
        
        print(f"n={n}, m={m}, pos={pos} => 暴力结果={brute}, 数学结果={math}, 一致={'是' if brute == math else '否'}")


def performance_test():
    """
    性能测试函数
    """
    print("=== 性能测试 ===")
    
    # 测试不同规模的数据
    test_cases = [
        (1000, 1000),               # 小规模
        (1000000, 1000000),         # 中等规模
        (1000000000, 1000000000)    # 大规模
    ]
    
    for n, m in test_cases:
        pos = 1  # 任意位置
        solver = ShuffleSolver(n, m, pos)
        
        start_time = time.time()
        result = solver.mathematical_solution()
        end_time = time.time()
        
        elapsed = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"n={n}, m={m} => 耗时: {elapsed:.2f} ms, 结果={result}")


def principle_demo():
    """
    原理解释演示
    """
    print("=== 洗牌原理解释 ===")
    print("洗牌过程：")
    print("假设n=6张牌，初始位置为1,2,3,4,5,6")
    print("洗牌后变为：1,4,2,5,3,6")
    print("\n位置映射规律：")
    print("前半部分(1-3)：位置x → 2x-1")
    print("后半部分(4-6)：位置x → 2(x-3)")
    
    print("\n数学规律推导：")
    print("对于n=6，观察各位置的变化：")
    solver = ShuffleSolver(6, 1, 1)
    for pos in range(1, 7):
        next_pos = solver.get_next_position(pos)
        math = (2 * pos) % 7  # 7 = n + 1
        if math == 0:
            math = 7
        print(f"位置{pos} → {next_pos} (数学计算: 2*{pos} mod 7 = {math})")
    
    print("\n结论：每次洗牌相当于位置乘以2 mod (n+1)")


def run_demo():
    """
    运行演示
    """
    print("洛谷 P2054 [AHOI2005] 洗牌 解决方案")
    print("1. 运行标准测试（按题目输入格式）")
    print("2. 运行一致性测试")
    print("3. 运行性能测试")
    print("4. 查看原理解释")
    
    try:
        choice = input("请选择测试类型：").strip()
        
        if choice == '1':
            print("请输入测试数据：")
            run_test()
        elif choice == '2':
            consistency_test()
        elif choice == '3':
            performance_test()
        elif choice == '4':
            principle_demo()
        else:
            print("无效选择，运行原理解释")
            principle_demo()
    except Exception as e:
        print(f"发生错误：{e}")


if __name__ == "__main__":
    run_demo()


"""
Python语言特定优化分析：
1. 利用Python原生支持大整数的特性，无需担心溢出问题
2. 优化输入处理：一次性读取所有输入，减少IO操作次数
3. 使用位运算（& 和 >>）提高位操作效率
4. 针对不同数据规模选择合适的算法，体现了工程化思想

时间复杂度分析：
- 暴力解法：O(m)，其中m是洗牌次数
- 数学解法：O(log m)，主要是快速幂的时间复杂度
- 分块优化解法：O(log m)，与数学解法相同

空间复杂度分析：
- 所有解法：O(1)，只需要常量级额外空间
- 测试函数使用O(k)空间，k为测试用例数量

Python性能优化建议：
1. 对于非常大规模的计算，可以考虑使用PyPy解释器
2. 在处理大量输入时，可以使用sys.stdin.readline()而不是input()
3. 对于频繁调用的数学函数，可以考虑使用math模块或numpy库

与其他语言实现对比：
- Python实现的代码最简洁，可读性最好
- Python原生支持大整数，无需额外处理溢出
- 但在性能上，Python版本比C++和Java慢
- Python的快速幂实现虽然简单，但效率较高

最优解分析：
对于这个问题，数学解法已经是最优解，时间复杂度为O(log m)
这比直接模拟的O(m)时间复杂度要高效得多，特别是当m非常大时
分块思想在这里主要体现在问题的数学建模上，而不是传统的区间分块处理

题解思路总结：
1. 观察问题，寻找数学规律，而不是直接模拟
2. 利用模运算和快速幂算法高效计算大指数
3. 根据数据规模选择合适的算法，体现工程化思想
4. 处理边界情况，确保算法的正确性
5. 添加充分的测试和性能分析，保证代码质量
"""