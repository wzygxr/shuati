# LeetCode 2034. 股票价格波动
# 题目链接: https://leetcode.cn/problems/stock-price-fluctuation/
# 题目描述:
# 给你一支股票价格的波动序列，请你实现一个数据结构来处理这些波动。
# 实现 StockPrice 类：
# StockPrice() 初始化对象，当前无股票价格记录。
# void update(int timestamp, int price) 在时间点 timestamp 更新股票价格为 price 。
# int current() 返回股票最新价格。
# int maximum() 返回股票最高价格。
# int minimum() 返回股票最低价格。
#
# 解题思路:
# 这是一个设计类问题，需要高效地维护股票价格数据。
# 使用以下数据结构：
# 1. 字典存储时间戳到价格的映射
# 2. 有序字典或堆维护价格的有序性
# 3. 维护最大时间戳以快速获取最新价格
#
# 算法步骤:
# 1. update操作：更新时间戳-价格映射，更新最大时间戳
# 2. current操作：直接返回最大时间戳对应的价格
# 3. maximum/minimum操作：使用有序数据结构快速获取最值
#
# 时间复杂度分析:
# update: O(log n)
# current: O(1)
# maximum/minimum: O(1)
#
# 空间复杂度分析:
# O(n) - 存储所有时间戳和价格

import heapq
from collections import defaultdict

class StockPrice:
    """
    股票价格波动数据结构
    """
    
    def __init__(self):
        """
        初始化对象
        """
        # 存储时间戳到价格的映射
        self.timestamp_to_price = {}
        
        # 使用两个堆维护最大值和最小值
        self.max_heap = []  # 存储 (-price, timestamp) 以实现最大堆
        self.min_heap = []  # 存储 (price, timestamp) 以实现最小堆
        
        # 记录最大时间戳
        self.max_timestamp = 0
    
    def update(self, timestamp: int, price: int) -> None:
        """
        在时间点 timestamp 更新股票价格为 price
        
        Args:
            timestamp: 时间戳
            price: 价格
        """
        # 更新时间戳到价格的映射
        self.timestamp_to_price[timestamp] = price
        
        # 将价格和时间戳添加到堆中
        heapq.heappush(self.max_heap, (-price, timestamp))
        heapq.heappush(self.min_heap, (price, timestamp))
        
        # 更新最大时间戳
        self.max_timestamp = max(self.max_timestamp, timestamp)
    
    def current(self) -> int:
        """
        返回股票最新价格
        
        Returns:
            最新价格
        """
        return self.timestamp_to_price[self.max_timestamp]
    
    def maximum(self) -> int:
        """
        返回股票最高价格
        
        Returns:
            最高价格
        """
        # 懒删除：检查堆顶元素是否有效
        while self.max_heap and -self.max_heap[0][0] != self.timestamp_to_price[self.max_heap[0][1]]:
            heapq.heappop(self.max_heap)
        
        return -self.max_heap[0][0]
    
    def minimum(self) -> int:
        """
        返回股票最低价格
        
        Returns:
            最低价格
        """
        # 懒删除：检查堆顶元素是否有效
        while self.min_heap and self.min_heap[0][0] != self.timestamp_to_price[self.min_heap[0][1]]:
            heapq.heappop(self.min_heap)
        
        return self.min_heap[0][0]


# 测试方法
if __name__ == "__main__":
    stock_price = StockPrice()
    
    # 测试用例
    stock_price.update(1, 10)
    stock_price.update(2, 5)
    
    assert stock_price.current() == 5, "current()测试失败"
    assert stock_price.maximum() == 10, "maximum()测试失败"
    assert stock_price.minimum() == 5, "minimum()测试失败"
    
    stock_price.update(1, 3)  # 更新时间戳1的价格为3
    
    assert stock_price.maximum() == 5, "maximum()测试失败"
    assert stock_price.minimum() == 3, "minimum()测试失败"
    
    stock_price.update(4, 2)  # 添加时间戳4，价格为2
    
    assert stock_price.minimum() == 2, "minimum()测试失败"
    
    print("所有测试通过!")