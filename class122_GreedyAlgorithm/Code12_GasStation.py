# 加油站
# 在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
# 你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
# 你从其中的一个加油站出发，开始时油箱为空。
# 给定两个整数数组 gas 和 cost ，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1 。
# 如果存在解，则保证它是唯一的。
# 测试链接 : https://leetcode.cn/problems/gas-station/

def canCompleteCircuit(gas, cost):
    """
    加油站问题
    
    算法思路：
    使用贪心策略：
    1. 首先判断总油量是否大于等于总消耗，如果小于则无解
    2. 从编号0开始尝试，维护当前油箱中的油量
    3. 如果油量变为负数，说明从之前的起点无法到达当前位置
    4. 将起点设为当前位置的下一个位置，重置油量
    
    正确性分析：
    1. 如果总油量小于总消耗，肯定无解
    2. 如果从起点start无法到达位置i，那么从start到i之间的任何位置都无法到达i
    3. 因此可以将起点直接跳到i+1位置
    
    时间复杂度：O(n) - 只需要遍历数组一次
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param gas: 汽油数组
    :param cost: 消耗数组
    :return: 起点编号，如果无解返回-1
    """
    totalGas = 0      # 总油量
    totalCost = 0     # 总消耗
    currentGas = 0    # 当前油箱中的油量
    start = 0         # 起点
    
    # 遍历所有加油站
    for i in range(len(gas)):
        totalGas += gas[i]
        totalCost += cost[i]
        currentGas += gas[i] - cost[i]
        
        # 如果当前油量变为负数，说明无法从起点到达位置i
        if currentGas < 0:
            # 将起点设为i+1位置
            start = i + 1
            # 重置油量
            currentGas = 0
    
    # 如果总油量小于总消耗，无解
    if totalGas < totalCost:
        return -1
    
    # 返回起点
    return start

# 测试用例
if __name__ == "__main__":
    # 测试用例1: gas = [1,2,3,4,5], cost = [3,4,5,1,2] -> 输出: 3
    gas1 = [1, 2, 3, 4, 5]
    cost1 = [3, 4, 5, 1, 2]
    print("测试用例1:")
    print("汽油数组:", gas1)
    print("消耗数组:", cost1)
    print("起点编号:", canCompleteCircuit(gas1, cost1))  # 期望输出: 3
    
    # 测试用例2: gas = [2,3,4], cost = [3,4,3] -> 输出: -1
    gas2 = [2, 3, 4]
    cost2 = [3, 4, 3]
    print("\n测试用例2:")
    print("汽油数组:", gas2)
    print("消耗数组:", cost2)
    print("起点编号:", canCompleteCircuit(gas2, cost2))  # 期望输出: -1
    
    # 测试用例3: gas = [5,1,2,3,4], cost = [4,4,1,5,1] -> 输出: 4
    gas3 = [5, 1, 2, 3, 4]
    cost3 = [4, 4, 1, 5, 1]
    print("\n测试用例3:")
    print("汽油数组:", gas3)
    print("消耗数组:", cost3)
    print("起点编号:", canCompleteCircuit(gas3, cost3))  # 期望输出: 4