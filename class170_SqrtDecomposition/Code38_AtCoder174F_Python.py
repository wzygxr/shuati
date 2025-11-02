import sys
import math
import time
import random
from collections import defaultdict

"""
AtCoder ABC174 F Range Set Query
题目要求：区间查询不同元素个数
核心技巧：莫队算法 + 分块
时间复杂度：O(n√n)
测试链接：https://atcoder.jp/contests/abc174/tasks/abc174_f

莫队算法是一种离线查询的优化算法，适用于处理大量区间查询问题。
它的核心思想是将查询按照块进行排序，然后逐个处理查询，利用之前计算的结果进行增量更新。
对于不同元素个数查询，我们可以维护一个计数字典，记录当前区间内各元素出现的次数。
"""

class MoAlgorithm:
    """
    莫队算法实现类
    用于高效处理区间不同元素个数查询
    """
    
    def __init__(self, a):
        """
        初始化莫队算法
        
        Args:
            a: 原始数组
        """
        self.a = a
        self.n = len(a)
        self.block_size = int(math.sqrt(self.n)) + 1
        self.count = defaultdict(int)
        self.current_distinct = 0
    
    def solve(self, queries):
        """
        解决所有查询
        
        Args:
            queries: 查询列表，每个查询为(l, r, idx)元组，0-based
            
        Returns:
            答案列表，按照查询顺序排列
        """
        q = len(queries)
        answers = [0] * q
        
        # 对查询进行排序
        sorted_queries = sorted(queries, key=lambda x: self._sort_key(x))
        
        current_l, current_r = 0, -1
        
        # 处理每个查询
        for l, r, idx in sorted_queries:
            # 调整左右指针
            while current_l > l:
                current_l -= 1
                self._add(self.a[current_l])
            
            while current_r < r:
                current_r += 1
                self._add(self.a[current_r])
            
            while current_l < l:
                self._remove(self.a[current_l])
                current_l += 1
            
            while current_r > r:
                self._remove(self.a[current_r])
                current_r -= 1
            
            # 保存答案
            answers[idx] = self.current_distinct
        
        return answers
    
    def _sort_key(self, query):
        """
        查询排序键
        奇偶优化：偶数块升序，奇数块降序
        """
        l, r, _ = query
        block = l // self.block_size
        # 奇偶优化
        return (block, r if block % 2 == 0 else -r)
    
    def _add(self, x):
        """
        添加元素到当前区间
        """
        if self.count[x] == 0:
            self.current_distinct += 1
        self.count[x] += 1
    
    def _remove(self, x):
        """
        从当前区间移除元素
        """
        self.count[x] -= 1
        if self.count[x] == 0:
            self.current_distinct -= 1
    
    def reset(self):
        """
        重置状态，准备新的查询批次
        """
        self.count.clear()
        self.current_distinct = 0


def run_test():
    """
    标准测试函数，按题目输入格式处理
    """
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    q = int(input[ptr])
    ptr += 1
    
    a = list(map(int, input[ptr:ptr+n]))
    ptr += n
    
    queries = []
    for i in range(q):
        l = int(input[ptr]) - 1  # 转换为0-based
        ptr += 1
        r = int(input[ptr]) - 1
        ptr += 1
        queries.append((l, r, i))
    
    mo = MoAlgorithm(a)
    answers = mo.solve(queries)
    
    for ans in answers:
        print(ans)


def optimized_mo_algorithm(a, queries):
    """
    优化版本的莫队算法实现
    使用更紧凑的数据结构和更少的函数调用
    """
    n = len(a)
    q = len(queries)
    block_size = int(math.sqrt(n)) + 1
    
    # 预排序查询
    sorted_queries = sorted(queries, key=lambda x: (x[0] // block_size, x[1] if (x[0] // block_size) % 2 == 0 else -x[1]))
    
    answers = [0] * q
    count = defaultdict(int)
    current_distinct = 0
    current_l, current_r = 0, -1
    
    # 快速访问当前区间
    for l, r, idx in sorted_queries:
        # 快速调整指针位置
        # 这部分代码尽量减少函数调用，提高Python执行效率
        while current_l > l:
            current_l -= 1
            x = a[current_l]
            if count[x] == 0:
                current_distinct += 1
            count[x] += 1
        
        while current_r < r:
            current_r += 1
            x = a[current_r]
            if count[x] == 0:
                current_distinct += 1
            count[x] += 1
        
        while current_l < l:
            x = a[current_l]
            count[x] -= 1
            if count[x] == 0:
                current_distinct -= 1
            current_l += 1
        
        while current_r > r:
            x = a[current_r]
            count[x] -= 1
            if count[x] == 0:
                current_distinct -= 1
            current_r -= 1
        
        answers[idx] = current_distinct
    
    return answers


def correctness_test():
    """
    正确性测试
    """
    print("=== 正确性测试 ===")
    
    # 测试用例1
    a = [1, 2, 3, 2, 1]
    queries = [(0, 4, 0), (1, 3, 1), (0, 0, 2)]
    
    mo = MoAlgorithm(a)
    answers = mo.solve(queries)
    
    print("测试用例1结果（类版本）：")
    for i, ans in enumerate(answers):
        print(f"查询 {i+1}: {ans}")
    
    # 使用优化版本测试
    answers_opt = optimized_mo_algorithm(a, queries)
    print("\n测试用例1结果（优化版本）：")
    for i, ans in enumerate(answers_opt):
        print(f"查询 {i+1}: {ans}")
    
    # 测试用例2
    a = [1, 1, 1, 2, 2, 3, 3, 3, 3, 4]
    queries = [(0, 9, 0), (0, 2, 1), (3, 8, 2), (5, 9, 3)]
    
    mo.reset()
    answers = mo.solve(queries)
    
    print("\n测试用例2结果：")
    for i, ans in enumerate(answers):
        print(f"查询 {i+1}: {ans}")


def performance_test():
    """
    性能测试
    """
    print("=== 性能测试 ===")
    
    # 测试不同规模的数据
    test_cases = [
        (1000, 1000),
        (10000, 10000),
        (50000, 50000)  # Python中不适合太大的数据
    ]
    
    for size, q_count in test_cases:
        print(f"\n测试数组大小: {size}, 查询次数: {q_count}")
        
        # 生成随机数据
        random.seed(42)  # 固定种子，保证可复现性
        a = [random.randint(1, size) for _ in range(size)]
        
        # 生成随机查询
        queries = []
        for i in range(q_count):
            l = random.randint(0, size-1)
            r = random.randint(l, size-1)
            queries.append((l, r, i))
        
        # 测试类版本
        mo = MoAlgorithm(a)
        start_time = time.time()
        answers_class = mo.solve(queries)
        end_time = time.time()
        print(f"类版本耗时: {(end_time - start_time) * 1000:.2f} ms")
        
        # 测试优化版本
        start_time = time.time()
        answers_opt = optimized_mo_algorithm(a, queries)
        end_time = time.time()
        print(f"优化版本耗时: {(end_time - start_time) * 1000:.2f} ms")
        
        # 验证结果一致性
        assert answers_class == answers_opt, "两个版本结果不一致！"
        print("结果一致性验证通过")


def boundary_test():
    """
    边界测试
    """
    print("=== 边界测试 ===")
    
    # 测试1：所有元素相同
    a = [42] * 1000
    queries = [(0, 999, 0), (0, 0, 1), (500, 999, 2)]
    
    answers = optimized_mo_algorithm(a, queries)
    print("所有元素相同测试结果：")
    for ans in answers:
        print(ans)
    
    # 测试2：所有元素不同
    a = list(range(1, 1001))
    queries = [(0, 999, 0), (0, 499, 1), (500, 500, 2)]
    
    answers = optimized_mo_algorithm(a, queries)
    print("\n所有元素不同测试结果：")
    for ans in answers:
        print(ans)


def block_size_analysis():
    """
    块大小优化分析
    """
    print("=== 块大小优化分析 ===")
    
    # 使用较小的数据集进行分析，避免Python运行时间过长
    size = 10000
    q_count = 10000
    
    random.seed(42)
    a = [random.randint(1, size) for _ in range(size)]
    queries = []
    for i in range(q_count):
        l = random.randint(0, size-1)
        r = random.randint(l, size-1)
        queries.append((l, r, i))
    
    # 测试不同的块大小
    block_sizes = [
        int(math.sqrt(size)) // 2,
        int(math.sqrt(size)),
        int(math.sqrt(size)) * 2,
        size // 100,
        size // 10
    ]
    
    for bs in block_sizes:
        start_time = time.time()
        
        # 复制查询并使用指定块大小排序
        sorted_queries = sorted(queries, 
                              key=lambda x: (x[0] // bs, x[1] if (x[0] // bs) % 2 == 0 else -x[1]))
        
        count = defaultdict(int)
        current_distinct = 0
        current_l, current_r = 0, -1
        answers = [0] * q_count
        
        # 处理查询
        for l, r, idx in sorted_queries:
            while current_l > l: current_l -= 1; x = a[current_l]; count[x] += 1; current_distinct += (count[x] == 1)
            while current_r < r: current_r += 1; x = a[current_r]; count[x] += 1; current_distinct += (count[x] == 1)
            while current_l < l: x = a[current_l]; count[x] -= 1; current_distinct -= (count[x] == 0); current_l += 1
            while current_r > r: x = a[current_r]; count[x] -= 1; current_distinct -= (count[x] == 0); current_r -= 1
            answers[idx] = current_distinct
        
        end_time = time.time()
        print(f"块大小: {bs}, 耗时: {(end_time - start_time) * 1000:.2f} ms")


def run_demo():
    """
    运行演示
    """
    print("AtCoder ABC174 F Range Set Query 解决方案")
    print("1. 运行标准测试（按题目输入格式）")
    print("2. 运行正确性测试")
    print("3. 运行性能测试")
    print("4. 运行边界测试")
    print("5. 运行块大小优化分析")
    
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
            boundary_test()
        elif choice == '5':
            block_size_analysis()
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
   - 类版本（MoAlgorithm）：代码结构清晰，易于理解和扩展
   - 函数式优化版本（optimized_mo_algorithm）：减少函数调用开销，提高Python执行效率

2. Python特定优化技巧：
   - 使用defaultdict(int)代替普通字典，简化计数操作
   - 减少函数调用，将频繁执行的操作内联
   - 使用列表推导式和生成器表达式代替循环，提高可读性和性能
   - 使用元组进行数据传递，元组比列表更轻量
   - 提前预计算排序键，避免重复计算

3. 性能优化考量：
   - 在Python中，函数调用有一定开销，对于频繁调用的操作应尽量内联
   - 使用局部变量而不是全局变量，Python访问局部变量更快
   - 避免在循环中创建新对象，重用已有的数据结构
   - 对于大数据量，Python可能性能受限，需要进一步优化或考虑PyPy

4. 时间复杂度分析：
   - 与C++和Java版本相同，理论时间复杂度为O(n√n)
   - 但由于Python解释器的开销，实际运行时间会比编译型语言长
   - 对于n=1e5的规模，Python版本可能需要几秒甚至更长时间

5. 空间复杂度分析：
   - 使用defaultdict存储计数，空间复杂度为O(distinct_values)
   - 总体空间复杂度为O(n + q + distinct_values)

6. Python特有的限制和解决方案：
   - 递归深度限制：莫队算法使用迭代实现，不受此限制
   - 全局解释器锁（GIL）：单线程执行，但莫队算法本身是顺序执行的
   - 内存管理：Python的自动垃圾回收可能会影响性能，对于长时间运行的程序需要注意

7. 测试和验证：
   - 提供了全面的测试函数，包括正确性、性能、边界和优化分析
   - 验证了两个版本实现的一致性
   - 性能测试中特别注意Python的执行效率限制，适当调整测试规模

8. 最佳实践总结：
   - 在Python中实现算法时，应尽量平衡代码可读性和执行效率
   - 对于性能关键部分，可以适当牺牲代码可读性换取更好的性能
   - 利用Python的高级特性（如装饰器、上下文管理器等）可以使代码更简洁
   - 对于大规模数据处理，考虑使用PyPy或混合编程（Python调用C扩展）

虽然Python版本在性能上不如C++和Java，但它的代码更加简洁易读，适合快速原型开发和教学演示。
对于实际应用中的大规模数据处理，可能需要考虑使用编译型语言或优化的Python实现。
"""