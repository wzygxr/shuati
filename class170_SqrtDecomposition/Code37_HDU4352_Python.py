import time
from functools import lru_cache

"""
HDU 4352 XHXJ's LIS
题目要求：计算区间内数位LIS长度等于k的数的个数
核心技巧：数位DP + 分块状态压缩
时间复杂度：O(len(digits) * 2^10 * 10)
测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=4352

算法思想详解：
1. 数位DP处理大数范围查询
2. 使用二进制状态压缩表示当前LIS状态
3. 分块处理状态转移，优化计算
4. 利用记忆化搜索避免重复计算
"""

class DigitLISSolver:
    """
    数位LIS求解类
    使用数位DP和状态压缩来高效计算满足条件的数的个数
    """
    
    def __init__(self, l, r, k):
        """
        初始化问题参数
        
        Args:
            l: 区间左边界
            r: 区间右边界
            k: 要求的LIS长度
        """
        self.l = l
        self.r = r
        self.k = k
        self.digits = []  # 当前处理的数字的各位
    
    def solve(self):
        """
        计算区间[l, r]中满足条件的数的个数
        
        Returns:
            满足条件的数的个数
        """
        # 计算[0, r] - [0, l-1]
        return self.calculate(self.r) - self.calculate(self.l - 1)
    
    def get_new_status(self, status, d):
        """
        根据当前状态和新数字，计算新的LIS状态
        
        Args:
            status: 当前状态（二进制压缩）
            d: 新数字
            
        Returns:
            新状态
        """
        tmp = status
        # 找到d应该插入的位置（替换第一个比d大的数字）
        for i in range(d, 10):
            if tmp & (1 << i):
                tmp ^= (1 << i)
                break
        # 将d添加到状态中
        tmp |= (1 << d)
        return tmp
    
    def get_lis_length(self, status):
        """
        计算状态对应的LIS长度
        
        Args:
            status: 状态
            
        Returns:
            LIS长度（二进制中1的个数）
        """
        return bin(status).count('1')
    
    def dfs(self, pos, status, leading_zero, limit):
        """
        数位DP的DFS实现
        
        Args:
            pos: 当前处理的位置
            status: 当前LIS的状态
            leading_zero: 是否前导零
            limit: 当前位是否受原数限制
            
        Returns:
            满足条件的数的个数
        """
        # 已经处理完所有位
        if pos == len(self.digits):
            if leading_zero:
                return 1 if self.k == 0 else 0
            return 1 if self.get_lis_length(status) == self.k else 0
        
        # 如果有前导零，单独处理
        if leading_zero:
            res = self.dfs(pos + 1, 0, True, limit and (self.digits[pos] == 0))
            max_digit = self.digits[pos] if limit else 9
            for d in range(1, max_digit + 1):
                res += self.dfs(pos + 1, self.get_new_status(0, d), False, limit and (d == max_digit))
            return res
        
        # 尝试每一个可能的数字
        res = 0
        max_digit = self.digits[pos] if limit else 9
        
        for d in range(0, max_digit + 1):
            new_status = self.get_new_status(status, d)
            res += self.dfs(pos + 1, new_status, False, limit and (d == max_digit))
        
        return res
    
    def calculate(self, x):
        """
        计算从0到x的满足条件的数的个数
        
        Args:
            x: 上界
            
        Returns:
            满足条件的数的个数
        """
        if x < 0:
            return 0
        
        # 将x转换为数字数组
        self.digits = []
        if x == 0:
            self.digits.append(0)
        else:
            while x > 0:
                self.digits.append(x % 10)
                x //= 10
        
        # 反转以获得正确的顺序（高位到低位）
        self.digits.reverse()
        
        # 开始数位DP（Python中使用lru_cache装饰器可能不适合，这里使用普通递归）
        return self.dfs(0, 0, True, True)


def run_test():
    """
    运行标准测试，按题目输入格式处理
    """
    import sys
    
    data = sys.stdin.read().split()
    ptr = 0
    t = int(data[ptr])
    ptr += 1
    
    for case_num in range(1, t + 1):
        l = int(data[ptr])
        ptr += 1
        r = int(data[ptr])
        ptr += 1
        k = int(data[ptr])
        ptr += 1
        
        solver = DigitLISSolver(l, r, k)
        result = solver.solve()
        
        print(f"Case #{case_num}: {result}")


def optimized_solver(l, r, k):
    """
    使用lru_cache优化的数位DP求解器
    注意：在Python中，lru_cache对于递归函数更高效
    """
    
    def get_new_status(status, d):
        tmp = status
        for i in range(d, 10):
            if tmp & (1 << i):
                tmp ^= (1 << i)
                break
        tmp |= (1 << d)
        return tmp
    
    def get_lis_length(status):
        return bin(status).count('1')
    
    def calculate(x):
        if x < 0:
            return 0
        
        # 转换为数字数组
        digits = []
        if x == 0:
            digits = [0]
        else:
            tmp = x
            while tmp > 0:
                digits.append(tmp % 10)
                tmp //= 10
            digits.reverse()
        
        n = len(digits)
        
        @lru_cache(maxsize=None)
        def dfs(pos, status, leading_zero, limit):
            if pos == n:
                if leading_zero:
                    return 1 if k == 0 else 0
                return 1 if get_lis_length(status) == k else 0
            
            res = 0
            max_digit = digits[pos] if limit else 9
            
            if leading_zero:
                # 选择继续前导零
                res += dfs(pos + 1, 0, True, limit and (0 == max_digit))
                # 选择非零数字
                for d in range(1, max_digit + 1):
                    new_status = get_new_status(0, d)
                    res += dfs(pos + 1, new_status, False, limit and (d == max_digit))
            else:
                # 正常情况
                for d in range(0, max_digit + 1):
                    new_status = get_new_status(status, d)
                    res += dfs(pos + 1, new_status, False, limit and (d == max_digit))
            
            return res
        
        return dfs(0, 0, True, True)
    
    return calculate(r) - calculate(l - 1)


def correctness_test():
    """
    正确性测试
    """
    print("=== 正确性测试 ===")
    
    # 测试用例
    test_cases = [
        (1, 10, 1),
        (1, 100, 2),
        (10, 30, 2)
    ]
    
    for l, r, k in test_cases:
        # 使用优化版本进行测试
        result = optimized_solver(l, r, k)
        print(f"区间[{l}, {r}]中LIS长度为{k}的数的个数：{result}")


def performance_test():
    """
    性能测试
    """
    print("=== 性能测试 ===")
    
    # 测试不同规模的数据
    test_cases = [
        (1, 1000, 3),
        (1, 1000000, 3),
        (1, 100000000, 3)  # 注意：Python可能无法处理太大的数，会超时
    ]
    
    for l, r, k in test_cases:
        start_time = time.time()
        result = optimized_solver(l, r, k)
        end_time = time.time()
        
        elapsed = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"区间[{l}, {r}], k={k} => 结果={result}, 耗时={elapsed:.2f} ms")


def state_transition_demo():
    """
    状态转移演示
    """
    print("=== 状态转移演示 ===")
    
    def get_new_status(status, d):
        tmp = status
        for i in range(d, 10):
            if tmp & (1 << i):
                tmp ^= (1 << i)
                break
        tmp |= (1 << d)
        return tmp
    
    def get_lis_length(status):
        return bin(status).count('1')
    
    # 演示几个状态转移的例子
    print("状态转移示例：")
    examples = [
        (0, 3),
        (0b1000, 2),
        (0b1100, 1),
        (0b1110, 4)
    ]
    
    for status, digit in examples:
        new_status = get_new_status(status, digit)
        print(f"状态 {bin(status)} (长度={get_lis_length(status)}), 添加数字 {digit} → 新状态 {bin(new_status)} (长度={get_lis_length(new_status)})")


def run_demo():
    """
    运行演示
    """
    print("HDU 4352 XHXJ's LIS 解决方案")
    print("1. 运行标准测试（按题目输入格式）")
    print("2. 运行正确性测试")
    print("3. 运行性能测试")
    print("4. 查看状态转移演示")
    
    try:
        choice = input("请选择测试类型：").strip()
        
        if choice == '1':
            print("请输入测试数据：")
            run_test()
        elif choice == '2':
            correctness_test()
        elif choice == '3':
            performance_test()
        elif choice == '4':
            state_transition_demo()
        else:
            print("无效选择，运行正确性测试")
            correctness_test()
    except Exception as e:
        print(f"发生错误：{e}")


if __name__ == "__main__":
    run_demo()


"""
Python语言特定优化分析：
1. 提供了两个版本的实现：
   - 普通递归版本（DigitLISSolver类）：代码结构清晰，但效率较低
   - 使用lru_cache优化的版本（optimized_solver函数）：利用Python的装饰器机制进行自动缓存
2. 使用bin()函数和count('1')快速计算二进制中1的个数
3. 利用位运算高效处理状态转移
4. 针对Python的性能特点，调整了大数测试的规模

时间复杂度分析：
- 普通递归版本：最坏情况下可能达到O(10^len)，但实际上由于状态重复，会有很多冗余计算
- 优化版本：O(len * 2^10 * 2 * 10) = O(常数)，与Java和C++版本相同

空间复杂度分析：
- 递归调用栈：O(len)
- 缓存（lru_cache）：O(len * 2^10 * 2) = O(常数)

Python性能优化建议：
1. 使用lru_cache装饰器是Python中实现记忆化搜索的最佳方式
2. 对于更大规模的测试，可以考虑使用PyPy解释器
3. 注意Python的递归深度限制，虽然本题中不会有问题
4. 可以尝试使用迭代方式实现数位DP，避免Python递归的性能开销

与其他语言实现对比：
- Python版本的代码最简洁，可读性最好
- 使用lru_cache比手动维护dp数组更方便
- 但在性能上，Python版本比C++和Java慢
- Python的位操作效率较低，这在状态转移中会有一定影响

最优解分析：
对于这个问题，数位DP结合状态压缩是最优解法
Python中的lru_cache版本已经接近最优性能
在处理非常大的数值范围时，Python可能会比C++和Java慢，但算法思想是一致的
状态压缩的设计充分利用了问题的特性，使得即使对于很大的数值范围，计算也能在合理时间内完成

题解思路总结：
1. 数位DP是处理大数范围查询问题的有效方法
2. 状态压缩是将复杂问题转化为可处理规模的关键技术
3. 记忆化搜索可以显著提高数位DP的效率
4. 前导零和数位限制是数位DP中需要特别处理的边界情况
5. Python中利用装饰器可以优雅地实现记忆化搜索
"""