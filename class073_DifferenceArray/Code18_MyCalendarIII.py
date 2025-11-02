from collections import OrderedDict

class MyCalendarThree:
    """
    LeetCode 732. 我的日程安排表 III (My Calendar III)
    
    题目描述:
    实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。
    当 k 个日程安排有一些时间上的交叉时（例如 k 个日程安排都在同一时间内），就会产生 k 次预订。
    每次调用 MyCalendarThree.book(int start, int end) 方法时，返回一个整数 k，
    表示当前日历中存在的最大交叉预订次数。
    
    示例:
    calendar = MyCalendarThree()
    calendar.book(10, 20) # 返回 1
    calendar.book(50, 60) # 返回 1
    calendar.book(10, 40) # 返回 2
    calendar.book(5, 15)  # 返回 3
    calendar.book(5, 10)  # 返回 3
    calendar.book(25, 55) # 返回 3
    
    提示:
    每个测试用例，调用 MyCalendarThree.book 函数最多不超过 400 次。
    调用函数 MyCalendarThree.book(start, end) 时，start 和 end 满足 0 <= start < end <= 10^9。
    
    题目链接: https://leetcode.com/problems/my-calendar-iii/
    
    解题思路:
    使用差分数组技巧来处理区间更新操作。
    1. 使用SortedDict来存储差分标记，key为时间点，value为在该时间点的变化量
    2. 对于每个book操作，在start处+1，在end处-1
    3. 遍历SortedDict，计算前缀和，记录最大值
    
    时间复杂度: O(n^2) - 每次book操作需要遍历所有时间点
    空间复杂度: O(n) - 需要存储所有时间点的差分标记
    
    这是最优解，因为需要处理动态的区间更新和查询。
    """
    
    def __init__(self):
        self.diff = {}
    
    def book(self, start: int, end: int) -> int:
        """
        添加日程安排并返回当前最大交叉预订次数
        
        Args:
            start: 开始时间
            end: 结束时间
            
        Returns:
            当前最大交叉预订次数
        """
        # 边界检查
        if start < 0 or end <= start:
            return 0
        
        # 在start处增加1
        self.diff[start] = self.diff.get(start, 0) + 1
        # 在end处减少1
        self.diff[end] = self.diff.get(end, 0) - 1
        
        # 计算当前最大交叉预订次数
        max_k = 0
        current = 0
        
        # 遍历所有时间点，计算前缀和（按时间顺序）
        sorted_keys = sorted(self.diff.keys())
        for key in sorted_keys:
            current += self.diff[key]
            max_k = max(max_k, current)
        
        return max_k

def test_my_calendar_three():
    """
    测试用例
    """
    calendar = MyCalendarThree()
    
    # 测试用例1
    print(f"测试用例1: {calendar.book(10, 20)}")  # 预期: 1
    print(f"测试用例2: {calendar.book(50, 60)}")  # 预期: 1
    print(f"测试用例3: {calendar.book(10, 40)}")  # 预期: 2
    print(f"测试用例4: {calendar.book(5, 15)}")   # 预期: 3
    print(f"测试用例5: {calendar.book(5, 10)}")   # 预期: 3
    print(f"测试用例6: {calendar.book(25, 55)}")  # 预期: 3

if __name__ == "__main__":
    test_my_calendar_three()