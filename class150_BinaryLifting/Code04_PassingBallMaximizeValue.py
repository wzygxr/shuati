# 在传球游戏中最大化函数值问题
# 问题描述：
# 给定一个长度为n的数组receiver和一个整数k
# 总共有n名玩家，编号0 ~ n-1，这些玩家在玩一个传球游戏
# receiver[i]表示编号为i的玩家会传球给下一个人的编号
# 玩家可以传球给自己，也就是说receiver[i]可能等于i
# 你需要选择一名开始玩家，然后开始传球，球会被传恰好k次
# 如果选择编号为x的玩家作为开始玩家
# 函数f(x)表示从x玩家开始，k次传球内所有接触过球的玩家编号之和
# 如果某位玩家多次触球，则累加多次
# f(x) = x + receiver[x] + receiver[receiver[x]] + ... 
# 你的任务时选择开始玩家x，目的是最大化f(x)，返回函数的最大值
# 测试链接 : https://leetcode.cn/problems/maximize-value-of-function-in-a-ball-passing-game/
# 
# 解题思路：
# 使用树上倍增算法预处理每个节点跳2^i步能到达的位置和路径和
# 然后通过二进制分解计算k步后的结果
# 对于每个起始点，计算k次传球后经过的所有玩家编号之和，找出最大值

import math

class PassingBallMaximizeValue:
    def __init__(self, n):
        """
        初始化传球游戏求解器
        :param n: 玩家数量
        """
        self.n = n
        self.LIMIT = 34  # 最大跳跃级别
        
        # 初始化数据结构
        self.stjump = [[0] * self.LIMIT for _ in range(n)]  # stjump[i][j] 表示从节点i开始跳2^j步能到达的节点
        self.stsum = [[0] * self.LIMIT for _ in range(n)]   # stsum[i][j] 表示从节点i开始跳2^j步经过的节点编号之和
        
    def build(self, k):
        """
        预处理k的二进制表示和相关参数
        :param k: 传球次数
        """
        # 计算k的最高位
        self.power = 0
        temp_k = k
        while (1 << self.power) <= (temp_k >> 1):
            self.power += 1
            
        self.m = 0
        self.kbits = [0] * self.LIMIT
        
        # 收集k的二进制表示中为1的位
        for p in range(self.power, -1, -1):
            if (1 << p) <= temp_k:
                self.kbits[self.m] = p
                self.m += 1
                temp_k -= 1 << p
                
    def get_max_function_value(self, receiver, k):
        """
        使用树上倍增算法计算传球游戏的最大值
        算法思路：
        1. 预处理每个节点跳2^i步能到达的位置和路径和
        2. 对每个起始点，通过二进制分解计算k步后的结果
        3. 找到最大值
        
        时间复杂度：O(n log k + n log k) = O(n log k)
        空间复杂度：O(n log k)
        
        注意：这是树上倍增的解法，虽然时间复杂度不是最优的，但非常好理解和实现
        最优解来自对基环树的分析，后续课程会安排相关内容
        
        :param receiver: 传球规则数组，receiver[i]表示i传给谁
        :param k: 传球次数
        :return: 函数f(x)的最大值
        """
        # 预处理k的二进制表示
        self.build(k)
        n = len(receiver)
        
        # 初始化跳1步的信息
        for i in range(n):
            self.stjump[i][0] = receiver[i]
            self.stsum[i][0] = receiver[i]
            
        # 倍增预处理
        # stjump[i][p] 表示从节点i跳2^p步到达的节点
        # stsum[i][p] 表示从节点i跳2^p步经过的节点编号之和
        for p in range(1, self.power + 1):
            for i in range(n):
                # 跳2^p步 = 跳2^(p-1)步后再跳2^(p-1)步
                self.stjump[i][p] = self.stjump[self.stjump[i][p - 1]][p - 1]
                # 路径和 = 前半段路径和 + 后半段路径和
                self.stsum[i][p] = self.stsum[i][p - 1] + self.stsum[self.stjump[i][p - 1]][p - 1]
                
        ans = 0
        # 枚举每个起始点
        for i in range(n):
            cur = i
            # 起始点自己也算在内
            sum_val = i
            # 通过二进制分解计算k步后的结果
            # 将k分解为2的幂次之和，然后依次跳跃
            for j in range(self.m):
                # 累加路径和
                sum_val += self.stsum[cur][self.kbits[j]]
                # 更新当前位置
                cur = self.stjump[cur][self.kbits[j]]
            # 更新最大值
            ans = max(ans, sum_val)
            
        return ans

def main():
    """
    主函数，用于测试
    """
    # 示例测试
    receiver1 = [2, 0, 1]
    k1 = 4
    
    solver1 = PassingBallMaximizeValue(len(receiver1))
    result1 = solver1.get_max_function_value(receiver1, k1)
    print(f"示例1结果: {result1}")  # 预期输出: 6
    
    # 另一个测试用例
    receiver2 = [1, 1, 1, 2, 3]
    k2 = 3
    
    solver2 = PassingBallMaximizeValue(len(receiver2))
    result2 = solver2.get_max_function_value(receiver2, k2)
    print(f"示例2结果: {result2}")  # 预期输出: 10

if __name__ == "__main__":
    main()