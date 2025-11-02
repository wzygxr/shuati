# LeetCode 901. 股票价格跨度
# 题目链接: https://leetcode.cn/problems/online-stock-span/
# 题目描述:
# 设计一个算法收集某些股票的每日报价，并返回该股票当日价格的跨度。
# 当日股票价格的跨度被定义为股票价格小于或等于今天价格的最大连续日数（从今天开始往回数，包括今天）。
# 例如，如果未来7天股票的价格是 [100,80,60,70,60,75,85]，那么股票跨度将是 [1,1,1,2,1,4,6]。
#
# 解题思路:
# 这是一个典型的单调栈问题。我们需要找到左边第一个比当前元素大的位置。
# 使用单调递减栈来解决这个问题，栈中存储索引。
# 对于每个新来的价格，我们弹出所有小于等于当前价格的元素，然后计算跨度。
#
# 算法步骤:
# 1. 使用一个栈来存储股票价格的索引
# 2. 对于每个新价格，弹出栈中所有小于等于当前价格的索引
# 3. 如果栈为空，说明当前价格是目前为止最大的，跨度为当前天数+1
# 4. 如果栈不为空，跨度为当前索引减去栈顶索引
# 5. 将当前索引压入栈中
#
# 时间复杂度分析:
# O(n) - 每个元素最多入栈和出栈一次
#
# 空间复杂度分析:
# O(n) - 栈的空间复杂度
#
# 是否为最优解:
# 是，这是解决该问题的最优解，因为每个元素最多被处理两次（一次入栈，一次出栈）
#
# 工程化考量:
# 1. 边界条件处理: 空栈情况
# 2. 异常处理: 输入参数校验
# 3. 可读性: 变量命名清晰，注释详细

class StockSpanner:
    def __init__(self):
        """
        初始化StockSpanner对象
        """
        self.stack = []  # 存储索引的单调栈
        self.prices = []  # 存储价格的数组
        self.index = 0  # 当前索引

    def next(self, price: int) -> int:
        """
        计算股票价格跨度
        
        Args:
            price (int): 当天股票价格
            
        Returns:
            int: 股票价格跨度
        """
        # 将价格添加到列表中
        self.prices.append(price)
        
        # 弹出栈中所有小于等于当前价格的索引
        while self.stack and self.prices[self.stack[-1]] <= price:
            self.stack.pop()
        
        # 计算跨度
        if not self.stack:
            # 如果栈为空，说明当前价格是目前为止最大的
            span = self.index + 1
        else:
            # 跨度为当前索引减去栈顶索引
            span = self.index - self.stack[-1]
        
        # 将当前索引压入栈中
        self.stack.append(self.index)
        self.index += 1
        
        return span


# 测试方法
if __name__ == "__main__":
    stock_spanner = StockSpanner()
    
    # 测试用例: [100, 80, 60, 70, 60, 75, 85]
    prices = [100, 80, 60, 70, 60, 75, 85]
    expected = [1, 1, 1, 2, 1, 4, 6]
    
    print("测试LeetCode 901. 股票价格跨度问题:")
    for i in range(len(prices)):
        result = stock_spanner.next(prices[i])
        print(f"价格: {prices[i]}, 跨度: {result}, 期望: {expected[i]}")
        assert result == expected[i], "测试失败!"
    print("测试通过!")