"""
加油站 (Python版本)

题目描述：在一条环路上有 n 个加油站，其中第 i 个加油站有汽油 gas[i] 升。
你有一辆油箱容量无限的汽车，从第 i 个加油站开往第 i+1 个加油站需要消耗汽油 cost[i] 升。
你从其中一个加油站出发，开始时油箱为空。
给定两个整数数组 gas 和 cost，如果你可以按顺序绕环路行驶一周，则返回出发时加油站的编号，否则返回 -1。
如果存在解，则保证它是唯一的。

测试链接: https://leetcode.cn/problems/gas-station/
"""

def can_complete_circuit(gas, cost):
    """
    加油站问题的贪心解法
    
    解题思路：
    1. 如果总的汽油量小于总的消耗量，那么无论从哪里出发，都不可能绕环路一周
    2. 从起点开始遍历，如果当前累计的剩余油量为负数，说明从起点到当前位置的路径不可行
       需要将起点更新为当前位置的下一个位置，并重新计算累计剩余油量
    3. 最终，如果总油量大于等于总消耗，返回找到的起点，否则返回-1
    
    贪心策略的正确性：
    - 如果从站点A出发，在到达站点B之前就没有油了，那么从A和B之间的任何一个站点出发都不可能到达B
    - 因为如果在A和B之间有一个站点C，从C出发能到达B，那么从A出发也能到达B（先到C，再到B）
    - 这与假设矛盾，所以如果从A出发无法到达B，那么从A和B之间的任何站点出发都无法到达B
    
    时间复杂度：O(n)，其中n是加油站的数量，只需要遍历数组一次
    
    空间复杂度：O(1)，只使用了常数个额外变量
    
    Args:
        gas: List[int] - 各加油站的汽油量数组
        cost: List[int] - 各段路程的消耗量数组
    
    Returns:
        int - 如果能绕环路行驶一周，返回出发时加油站的编号；否则返回-1
    """
    # 边界条件处理
    if not gas or not cost or len(gas) != len(cost):
        return -1  # 参数无效

    n = len(gas)
    total_gas = 0      # 总汽油量
    total_cost = 0     # 总消耗量
    current_tank = 0   # 当前油箱中的汽油量
    start_station = 0  # 起始加油站

    # 遍历所有加油站
    for i in range(n):
        # 累加总汽油量和总消耗量
        total_gas += gas[i]
        total_cost += cost[i]

        # 计算当前的剩余油量
        current_tank += gas[i] - cost[i]

        # 如果当前剩余油量为负数，说明从start_station到i的路径不可行
        if current_tank < 0:
            # 将起始站点更新为i+1
            start_station = i + 1
            # 重置当前油箱
            current_tank = 0

    # 如果总汽油量小于总消耗量，无解
    if total_gas < total_cost:
        return -1

    # 否则，返回找到的起始站点
    return start_station


# 测试函数
def test():
    # 测试用例1
    # 输入: gas = [1,2,3,4,5], cost = [3,4,5,1,2]
    # 输出: 3
    gas1 = [1, 2, 3, 4, 5]
    cost1 = [3, 4, 5, 1, 2]
    print("测试用例1结果:", can_complete_circuit(gas1, cost1))  # 期望输出: 3

    # 测试用例2
    # 输入: gas = [2,3,4], cost = [3,4,3]
    # 输出: -1
    gas2 = [2, 3, 4]
    cost2 = [3, 4, 3]
    print("测试用例2结果:", can_complete_circuit(gas2, cost2))  # 期望输出: -1

    # 测试用例3：边界情况 - 只有一个加油站
    # 输入: gas = [5], cost = [4]
    # 输出: 0
    gas3 = [5]
    cost3 = [4]
    print("测试用例3结果:", can_complete_circuit(gas3, cost3))  # 期望输出: 0

    # 测试用例4：边界情况 - 无法到达的情况
    # 输入: gas = [5,1,2,3,4], cost = [4,4,1,5,1]
    # 输出: 4
    gas4 = [5, 1, 2, 3, 4]
    cost4 = [4, 4, 1, 5, 1]
    print("测试用例4结果:", can_complete_circuit(gas4, cost4))  # 期望输出: 4

    # 测试用例5：更复杂的情况
    # 输入: gas = [3,1,1], cost = [1,2,2]
    # 输出: 0
    gas5 = [3, 1, 1]
    cost5 = [1, 2, 2]
    print("测试用例5结果:", can_complete_circuit(gas5, cost5))  # 期望输出: 0


# 执行测试
if __name__ == "__main__":
    test()