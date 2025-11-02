# 最大频率栈
'''
一、题目解析
实现一个类似栈的数据结构，支持以下操作：
1. push(val): 将一个整数val压入栈顶
2. pop(): 删除并返回栈中出现频率最高的元素
   如果出现频率最高的元素不只一个，则移除并返回最接近栈顶的元素

二、算法思路
使用两个字典来维护数据：
1. value_times: 记录每个值的出现频率
2. cnt_values: 记录每个频率对应的值列表（使用list实现）
3. top_times: 记录当前最大频率

push操作：
1. 更新值的频率
2. 将值添加到对应频率的列表中
3. 更新最大频率

pop操作：
1. 从最大频率对应的列表中移除最后一个元素
2. 更新该元素的频率
3. 如果最大频率列表为空，则减少最大频率

三、时间复杂度分析
push操作: O(1) - 字典操作和列表操作都是O(1)
pop操作: O(1) - 字典操作和列表操作都是O(1)

四、空间复杂度分析
O(n) - n为push操作的次数，需要存储所有元素及其频率信息

五、工程化考量
1. 异常处理: 处理空栈的pop操作
2. 边界场景: 空栈、单元素栈等

六、相关题目扩展
1. LeetCode 895. 最大频率栈 (本题)
2. 牛客网: 最大频率栈
3. 剑指Offer相关栈题目
'''

class FreqStack:
    def __init__(self):
        """构造函数"""
        # 出现的最大次数
        self.top_times = 0
        # 每层节点，频率到值列表的映射
        self.cnt_values = {}
        # 每一个数出现了几次，值到频率的映射
        self.value_times = {}

    def push(self, val: int) -> None:
        """
        压入元素到栈中
        :param val: 要压入的元素
        时间复杂度: O(1)
        """
        # 更新值的频率
        self.value_times[val] = self.value_times.get(val, 0) + 1
        cur_top_times = self.value_times[val]
        # 如果该频率对应的列表不存在，则创建新列表
        if cur_top_times not in self.cnt_values:
            self.cnt_values[cur_top_times] = []
        # 将值添加到对应频率的列表中
        self.cnt_values[cur_top_times].append(val)
        # 更新最大频率
        self.top_times = max(self.top_times, cur_top_times)

    def pop(self) -> int:
        """
        弹出频率最高的元素
        :return: 频率最高的元素，如果有多个则返回最接近栈顶的
        时间复杂度: O(1)
        """
        # 检查栈是否为空
        if self.top_times == 0:
            raise Exception("栈为空，无法执行pop操作")
        # 从最大频率对应的列表中移除最后一个元素
        top_time_values = self.cnt_values[self.top_times]
        ans = top_time_values.pop()
        # 如果最大频率列表为空，则减少最大频率
        if not top_time_values:
            del self.cnt_values[self.top_times]
            self.top_times -= 1
        # 更新弹出元素的频率
        times = self.value_times[ans]
        if times == 1:
            del self.value_times[ans]
        else:
            self.value_times[ans] = times - 1
        return ans

# 测试代码
if __name__ == "__main__":
    freqStack = FreqStack()
    
    # 测试用例: ["FreqStack","push","push","push","push","push","push","pop","pop","pop","pop"]
    #           [[],[5],[7],[5],[7],[4],[5],[],[],[],[]]
    
    freqStack.push(5)  # 堆栈为 [5]
    freqStack.push(7)  # 堆栈是 [5,7]
    freqStack.push(5)  # 堆栈是 [5,7,5]
    freqStack.push(7)  # 堆栈是 [5,7,5,7]
    freqStack.push(4)  # 堆栈是 [5,7,5,7,4]
    freqStack.push(5)  # 堆栈是 [5,7,5,7,4,5]
    
    print("pop():", freqStack.pop())  # 返回 5
    print("pop():", freqStack.pop())  # 返回 7
    print("pop():", freqStack.pop())  # 返回 5
    print("pop():", freqStack.pop())  # 返回 4