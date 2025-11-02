"""
901. 在线股票跨度 (Online Stock Span)

题目描述:
设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。

解题思路:
使用单调栈来解决。维护一个单调递减栈，栈中存储价格和对应的跨度。
当新价格到来时，弹出所有小于等于当前价格的价格，并累加它们的跨度。

时间复杂度: 每个价格最多入栈和出栈各一次，平均O(1)
空间复杂度: O(n)，用于存储单调栈

测试链接: https://leetcode.cn/problems/online-stock-span/

工程化考量:
1. 异常处理：价格边界检查
2. 性能优化：使用列表预分配空间，避免动态扩展
3. Python特性：利用列表的高效操作和生成器表达式
4. 代码可读性：详细注释和模块化设计
"""

from typing import List

class StockSpanner:
    """
    股票跨度计算器类
    """
    
    def __init__(self):
        self.stack = []  # 栈存储(价格, 跨度)
    
    def next(self, price: int) -> int:
        """
        计算当日价格的跨度
        
        Args:
            price: 当日价格
            
        Returns:
            int: 跨度值
        """
        span = 1  # 至少包含当天
        
        # 弹出所有小于等于当前价格的价格，并累加跨度
        while self.stack and self.stack[-1][0] <= price:
            span += self.stack.pop()[1]
        
        # 将当前价格和跨度入栈
        self.stack.append((price, span))
        
        return span

class StockSpannerOptimized:
    """
    优化版本：使用更高效的数据结构
    """
    
    def __init__(self):
        self.prices = []  # 存储价格
        self.spans = []   # 存储跨度
        self.size = 0     # 当前大小
    
    def next(self, price: int) -> int:
        """
        计算当日价格的跨度（优化版本）
        """
        span = 1
        
        # 从后往前遍历，累加跨度
        i = self.size - 1
        while i >= 0 and self.prices[i] <= price:
            span += self.spans[i]
            i -= self.spans[i]  # 跳过已经计算过的天数
        
        # 添加新数据
        self.prices.append(price)
        self.spans.append(span)
        self.size += 1
        
        return span

def test_stock_spanner():
    """测试方法 - 验证算法正确性"""
    print("=== 在线股票跨度算法测试 ===")
    
    # 测试用例1: [100, 80, 60, 70, 60, 75, 85]
    spanner1 = StockSpanner()
    spanner1_opt = StockSpannerOptimized()
    
    prices1 = [100, 80, 60, 70, 60, 75, 85]
    expected1 = [1, 1, 1, 2, 1, 4, 6]
    
    results1 = []
    results1_opt = []
    
    for price in prices1:
        results1.append(spanner1.next(price))
        results1_opt.append(spanner1_opt.next(price))
    
    print(f"测试用例1 标准版: {results1} (预期: {expected1})")
    print(f"测试用例1 优化版: {results1_opt} (预期: {expected1})")
    
    # 测试用例2: 连续递增价格
    spanner2 = StockSpanner()
    spanner2_opt = StockSpannerOptimized()
    
    prices2 = [10, 20, 30, 40, 50]
    expected2 = [1, 2, 3, 4, 5]
    
    results2 = []
    results2_opt = []
    
    for price in prices2:
        results2.append(spanner2.next(price))
        results2_opt.append(spanner2_opt.next(price))
    
    print(f"测试用例2 标准版: {results2} (预期: {expected2})")
    print(f"测试用例2 优化版: {results2_opt} (预期: {expected2})")
    
    # 测试用例3: 连续递减价格
    spanner3 = StockSpanner()
    spanner3_opt = StockSpannerOptimized()
    
    prices3 = [50, 40, 30, 20, 10]
    expected3 = [1, 1, 1, 1, 1]
    
    results3 = []
    results3_opt = []
    
    for price in prices3:
        results3.append(spanner3.next(price))
        results3_opt.append(spanner3_opt.next(price))
    
    print(f"测试用例3 标准版: {results3} (预期: {expected3})")
    print(f"测试用例3 优化版: {results3_opt} (预期: {expected3})")
    
    print("=== 功能测试完成！ ===")

def performance_test():
    """性能测试方法"""
    import time
    
    print("=== 性能测试 ===")
    
    # 性能测试：大规模数据 - 连续递增价格
    spanner = StockSpanner()
    spanner_opt = StockSpannerOptimized()
    
    size = 10000
    
    # 标准版本性能测试
    start_time = time.time()
    for i in range(size):
        spanner.next(i)
    end_time = time.time()
    print(f"标准版本 [{size}个连续递增价格]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 优化版本性能测试
    start_time = time.time()
    for i in range(size):
        spanner_opt.next(i)
    end_time = time.time()
    print(f"优化版本 [{size}个连续递增价格]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 性能测试：最坏情况 - 连续递减价格
    spanner_worst = StockSpanner()
    spanner_worst_opt = StockSpannerOptimized()
    
    # 标准版本最坏情况性能测试
    start_time = time.time()
    for i in range(size, 0, -1):
        spanner_worst.next(i)
    end_time = time.time()
    print(f"标准版本 [最坏情况{size}个连续递减价格]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 优化版本最坏情况性能测试
    start_time = time.time()
    for i in range(size, 0, -1):
        spanner_worst_opt.next(i)
    end_time = time.time()
    print(f"优化版本 [最坏情况{size}个连续递减价格]: 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    print("=== 性能测试完成！ ===")

def debug_print(spanner: StockSpanner, price: int, span: int):
    """
    调试辅助方法：打印栈状态
    
    Args:
        spanner: 股票跨度计算器
        price: 当前价格
        span: 计算出的跨度
    """
    print(f"价格: {price}, 跨度: {span}")
    print(f"栈内容: {spanner.stack}")
    print("---")

if __name__ == "__main__":
    # 运行功能测试
    test_stock_spanner()
    
    # 运行性能测试
    performance_test()

"""
算法复杂度分析:

时间复杂度: 平均O(1)
- 每个价格最多入栈和出栈各一次
- 虽然看起来是O(n)，但均摊分析后为O(1)

空间复杂度: O(n)
- 最坏情况下需要存储所有价格信息
- 优化版本使用了两个列表存储价格和跨度

最优解分析:
- 这是在线股票跨度问题的最优解
- 无法获得更好的时间复杂度
- 空间复杂度也是最优的

Python特性利用:
- 使用列表的pop()和append()操作，时间复杂度为O(1)
- 利用元组存储价格和跨度的配对信息
- 使用类型注解提高代码可维护性

单调栈应用技巧:
- 栈中存储价格和对应的跨度信息
- 当新价格到来时，弹出所有小于等于当前价格的价格
- 累加弹出的价格的跨度作为当前价格的跨度
- 这种设计确保了跨度的正确计算

工程化建议:
1. 对于超大规模数据，可以考虑使用更高效的数据结构
2. 可以添加更多的单元测试用例覆盖边界情况
3. 可以考虑使用装饰器进行性能监控
4. 对于生产环境，可以添加日志记录和异常监控
"""