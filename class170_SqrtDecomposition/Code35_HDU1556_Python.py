import math
import random
import time

"""
HDU 1556 Color the ball
题目要求：区间更新，单点查询
核心技巧：分块标记（懒惰标记）
时间复杂度：O(√n) / 操作
测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1556

算法思想详解：
1. 将数组分成√n大小的块
2. 对于完全覆盖的块，使用懒惰标记记录增量
3. 对于部分覆盖的块，暴力更新每个元素
4. 查询时，累加块标记和元素自身的值

Python优化说明：
- 使用列表作为主要数据结构
- 针对Python循环效率较低的特点，尽量减少不必要的循环
- 优化输入输出以应对大数据量
- 使用边界检查确保算法的正确性
"""

class BlockColor:
    """
    分块算法实现区间更新和单点查询
    用于解决HDU 1556 Color the ball问题
    """
    
    def __init__(self, size):
        """
        初始化分块数据结构
        
        Args:
            size: 数组大小
        """
        self.n = size
        # 计算块的大小，通常取√n
        self.block_size = int(math.isqrt(size)) + 1
        # 题目中的球是1-based编号
        self.arr = [0] * (size + 1)
        # 初始化块的懒惰标记数组
        self.block_add = [0] * ((size + self.block_size - 1) // self.block_size)
    
    def update_range(self, l, r):
        """
        区间更新操作：将区间[l, r]的每个元素加1
        
        Args:
            l: 左边界（1-based）
            r: 右边界（1-based）
        """
        # 处理越界情况
        if l < 1:
            l = 1
        if r > self.n:
            r = self.n
        if l > r:
            return
        
        block_l = (l - 1) // self.block_size
        block_r = (r - 1) // self.block_size
        
        # 同一块内，直接暴力更新
        if block_l == block_r:
            for i in range(l, r + 1):
                self.arr[i] += 1
            return
        
        # 处理左边不完整的块
        for i in range(l, (block_l + 1) * self.block_size + 1):
            self.arr[i] += 1
        
        # 处理中间完整的块，使用懒惰标记
        for i in range(block_l + 1, block_r):
            self.block_add[i] += 1
        
        # 处理右边不完整的块
        for i in range(block_r * self.block_size + 1, r + 1):
            self.arr[i] += 1
    
    def query_point(self, x):
        """
        单点查询操作：查询位置x的值
        
        Args:
            x: 查询位置（1-based）
            
        Returns:
            位置x的值
            
        Raises:
            ValueError: 当查询位置越界时
        """
        # 处理越界情况
        if x < 1 or x > self.n:
            raise ValueError(f"查询位置越界: {x}")
        
        block_index = (x - 1) // self.block_size
        # 元素值 = 原始值 + 所属块的标记值
        return self.arr[x] + self.block_add[block_index]
    
    def clear(self):
        """重置所有数据为初始状态"""
        self.arr = [0] * (self.n + 1)
        self.block_add = [0] * len(self.block_add)
    
    def get_size(self):
        """获取数组长度"""
        return self.n


def run_test():
    """运行标准测试，按题目输入格式处理"""
    import sys
    
    # 优化输入方式以提高效率
    data = sys.stdin.read().split()
    ptr = 0
    
    while True:
        if ptr >= len(data):
            break
        
        n = int(data[ptr])
        ptr += 1
        
        if n == 0:
            break
        
        solution = BlockColor(n)
        
        # 处理n个操作
        for i in range(n):
            l = int(data[ptr])
            r = int(data[ptr + 1])
            ptr += 2
            solution.update_range(l, r)
        
        # 收集结果并一次性输出
        results = []
        for i in range(1, n + 1):
            results.append(str(solution.query_point(i)))
        
        print(' '.join(results))


def performance_test():
    """性能测试函数"""
    print("=== 性能测试 ===")
    
    # 测试不同规模的数据（Python性能有限，测试较小的数据规模）
    test_sizes = [100, 1000, 10000]
    
    for size in test_sizes:
        solution = BlockColor(size)
        
        start_time = time.time()
        
        # 执行size次随机操作
        for _ in range(size):
            l = random.randint(1, size)
            r = random.randint(1, size)
            if l > r:
                l, r = r, l
            solution.update_range(l, r)
        
        # 执行查询
        for i in range(1, size + 1, 100):
            solution.query_point(i)
        
        end_time = time.time()
        elapsed = (end_time - start_time) * 1000  # 转换为毫秒
        print(f"数据规模 {size}, 耗时: {elapsed:.2f} ms")


def correctness_test():
    """测试正确性的函数"""
    print("=== 正确性测试 ===")
    
    # 简单测试案例
    solution = BlockColor(5)
    
    # 执行更新操作
    solution.update_range(1, 3)
    solution.update_range(2, 5)
    solution.update_range(1, 1)
    
    # 检查结果
    expected = [0, 2, 2, 2, 1, 1]  # expected[0]无效，从1开始
    all_correct = True
    
    print("查询结果：")
    for i in range(1, 6):
        actual = solution.query_point(i)
        print(f"位置 {i}: 预期={expected[i]}, 实际={actual}")
        if actual != expected[i]:
            all_correct = False
    
    print(f"测试{'通过' if all_correct else '失败'}")
    
    # 测试边界情况
    print("\n测试边界情况：")
    solution.clear()
    solution.update_range(1, 5)
    solution.update_range(1, 1)
    
    try:
        # 测试越界查询
        solution.query_point(0)
        print("越界检查失败")
    except ValueError as e:
        print(f"越界检查通过: {e}")


def run_demo():
    """运行演示"""
    print("HDU 1556 Color the ball 解决方案")
    print("1. 运行标准测试（按题目输入格式）")
    print("2. 运行正确性测试")
    print("3. 运行性能测试")
    
    try:
        choice = input("请选择测试类型：").strip()
        
        if choice == '1':
            print("请输入测试数据：")
            run_test()
        elif choice == '2':
            correctness_test()
        elif choice == '3':
            performance_test()
        else:
            print("无效选择，运行正确性测试")
            correctness_test()
    except Exception as e:
        print(f"发生错误：{e}")


if __name__ == "__main__":
    run_demo()


"""
Python语言特定优化分析：
1. 使用列表作为底层数据结构，简洁高效
2. 采用math.isqrt()函数（Python 3.8+）计算平方根，比math.sqrt()更高效
3. 输入处理优化：一次性读取所有输入，减少IO操作次数
4. 输出优化：收集所有结果后一次性输出，减少IO操作
5. 错误处理：使用异常机制处理边界情况

时间复杂度分析：
- 区间更新：
  - 对于完整块：O(1)，只更新懒惰标记
  - 对于不完整块：O(√n)，最多处理两个不完整块，每个最多√n个元素
  - 总时间复杂度：O(√n)

- 单点查询：O(1)，直接返回arr[x] + block_add[block_index]

空间复杂度分析：
- 数组arr：O(n)
- 懒惰标记数组block_add：O(√n)
- 总空间复杂度：O(n + √n) = O(n)

Python性能优化建议：
1. 对于非常大的数据集，可以考虑使用PyPy解释器
2. 可以使用NumPy数组来提高数值操作的效率
3. 对于频繁更新的场景，可以尝试不同的块大小，找到性能最优解
4. 考虑使用位操作或其他技巧进一步优化

与其他语言实现对比：
- Python实现的代码更简洁，可读性更好
- 但在性能上，Python版本比C++和Java慢
- 通过优化输入输出和循环结构，可以在一定程度上提高Python版本的性能

最优解分析：
对于这个问题，分块算法已经是最优解之一，时间复杂度为O(√n) per operation
其他可能的解决方案包括线段树和差分数组
- 差分数组在这个问题上时间复杂度为O(1)更新，O(n)查询，对于单点查询不够高效
- 线段树时间复杂度为O(log n) per operation，但常数较大
- 分块算法在实际应用中通常是最优选择
"""