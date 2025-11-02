"""
LeetCode 401. 二进制手表

二进制手表顶部有 4 个 LED 代表小时（0-11），底部的 6 个 LED 代表分钟（0-59）。
每个 LED 代表一个 0 或 1，最低位在右侧。
给你一个整数 turnedOn，表示当前亮着的 LED 的数量，返回二进制手表可以表示的所有可能时间。

算法思路：
使用回溯算法生成所有可能的LED点亮组合，然后验证这些组合是否能构成有效的时间。

时间复杂度：O(2^10) = O(1024)，因为总共有10个LED
空间复杂度：O(1)，不考虑结果数组的空间
"""

class Solution:
    def __init__(self):
        # LED代表的数值，前4个是小时，后6个是分钟
        self.LED_VALUES = [8, 4, 2, 1, 32, 16, 8, 4, 2, 1]
    
    def readBinaryWatch(self, turnedOn):
        """
        返回二进制手表可以表示的所有可能时间
        :param turnedOn: int 当前亮着的 LED 的数量
        :return: List[str] 所有可能的时间列表
        """
        result = []
        self.backtrack(turnedOn, 0, 0, 0, result)
        return result
    
    def backtrack(self, turnedOn, start, hour, minute, result):
        """
        回溯函数
        :param turnedOn: int 剩余需要点亮的LED数量
        :param start: int 当前考虑的LED位置
        :param hour: int 当前小时数
        :param minute: int 当前分钟数
        :param result: List[str] 结果列表
        """
        # 剪枝：如果小时或分钟超出范围，直接返回
        if hour > 11 or minute > 59:
            return
        
        # 终止条件：所有LED都已考虑完
        if turnedOn == 0:
            # 格式化时间字符串
            time = f"{hour}:{minute:02d}"
            result.append(time)
            return
        
        # 遍历剩余的LED
        for i in range(start, len(self.LED_VALUES)):
            if i < 4:
                # 处理小时LED
                self.backtrack(turnedOn - 1, i + 1, hour + self.LED_VALUES[i], minute, result)
            else:
                # 处理分钟LED
                self.backtrack(turnedOn - 1, i + 1, hour, minute + self.LED_VALUES[i], result)

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    print("turnedOn = 1:")
    print(solution.readBinaryWatch(1))
    
    # 测试用例2
    print("\nturnedOn = 9:")
    print(solution.readBinaryWatch(9))