# 加油站（Gas Station）
# 题目来源：LeetCode 134
# 题目链接：https://leetcode.cn/problems/gas-station/
# 
# 问题描述：
# 在一条环路上有N个加油站，每个加油站有汽油gas[i]和消耗cost[i]。
# 从某个加油站出发，按顺序访问每个加油站，判断是否能绕环路行驶一周。
# 如果可以，返回出发时加油站的编号，否则返回-1。
# 
# 算法思路：
# 使用贪心策略，关键观察：如果从加油站i出发无法到达加油站j，那么从i到j之间的任意加油站出发都无法到达j。
# 具体步骤：
# 1. 计算总油量是否足够绕环路一周，如果总油量小于总消耗，直接返回-1
# 2. 遍历所有加油站，维护当前油量和总油量
# 3. 如果当前油量小于0，说明从当前起点无法到达下一个加油站，重置起点为下一个加油站
# 4. 最终返回起点位置
# 
# 时间复杂度：O(n) - 只需遍历数组一次
# 空间复杂度：O(1) - 只使用了常数额外空间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 环路行驶问题
# 2. 资源约束下的可达性判断
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理数组长度不一致情况
# 
# 工程化考量：
# 1. 输入验证：检查数组是否为空，检查数组长度是否一致
# 2. 边界条件：处理单元素数组
# 3. 性能优化：一次遍历完成计算
# 
# 相关题目：
# 1. LeetCode 45. 跳跃游戏 II - 经典跳跃游戏
# 2. LeetCode 55. 跳跃游戏 - 判断是否能到达终点
# 3. LeetCode 871. 最低加油次数 - 加油站问题的变种
# 4. 牛客网 NC48 跳跃游戏 - 与LeetCode 55相同
# 5. LintCode 117. 跳跃游戏 II - 与LeetCode 45相同
# 6. HackerRank - Jumping on the Clouds - 简化版跳跃游戏
# 7. CodeChef - JUMP - 类似跳跃游戏的变种
# 8. AtCoder ABC161D - Lunlun Number - BFS搜索相关
# 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
# 10. POJ 1700 - Crossing River - 经典过河问题

class Solution:
    """
    计算能够绕环路行驶一周的起始加油站
    
    Args:
        gas: List[int] - 每个加油站的汽油量数组
        cost: List[int] - 每个加油站到下一个加油站的消耗数组
    
    Returns:
        int - 起始加油站索引，如果无法完成返回-1
    """
    def canCompleteCircuit(self, gas, cost):
        # 边界条件检查
        if not gas or not cost or len(gas) != len(cost):
            return -1
        
        n = len(gas)
        total_gas = 0      # 总油量
        current_gas = 0    # 当前油量
        start_station = 0   # 起始加油站
        
        for i in range(n):
            total_gas += gas[i] - cost[i]
            current_gas += gas[i] - cost[i]
            
            # 如果当前油量小于0，说明从start_station出发无法到达i+1
            if current_gas < 0:
                # 重置起始加油站为下一个加油站
                start_station = i + 1
                current_gas = 0
        
        # 如果总油量大于等于0，说明存在解，否则返回-1
        return start_station if total_gas >= 0 else -1


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 可以完成
    gas1 = [1, 2, 3, 4, 5]
    cost1 = [3, 4, 5, 1, 2]
    result1 = solution.canCompleteCircuit(gas1, cost1)
    print("测试用例1:")
    print(f"汽油量: {gas1}")
    print(f"消耗量: {cost1}")
    print(f"起始加油站: {result1}")
    print("期望输出: 3")
    print()
    
    # 测试用例2: 基本情况 - 无法完成
    gas2 = [2, 3, 4]
    cost2 = [3, 4, 3]
    result2 = solution.canCompleteCircuit(gas2, cost2)
    print("测试用例2:")
    print(f"汽油量: {gas2}")
    print(f"消耗量: {cost2}")
    print(f"起始加油站: {result2}")
    print("期望输出: -1")
    print()
    
    # 测试用例3: 边界情况 - 单元素数组
    gas3 = [5]
    cost3 = [4]
    result3 = solution.canCompleteCircuit(gas3, cost3)
    print("测试用例3:")
    print(f"汽油量: {gas3}")
    print(f"消耗量: {cost3}")
    print(f"起始加油站: {result3}")
    print("期望输出: 0")
    print()
    
    # 测试用例4: 复杂情况 - 多个可行解
    gas4 = [3, 1, 1]
    cost4 = [1, 2, 2]
    result4 = solution.canCompleteCircuit(gas4, cost4)
    print("测试用例4:")
    print(f"汽油量: {gas4}")
    print(f"消耗量: {cost4}")
    print(f"起始加油站: {result4}")
    print("期望输出: 0")
    print()
    
    # 测试用例5: 边界情况 - 空数组
    gas5 = []
    cost5 = []
    result5 = solution.canCompleteCircuit(gas5, cost5)
    print("测试用例5:")
    print(f"汽油量: {gas5}")
    print(f"消耗量: {cost5}")
    print(f"起始加油站: {result5}")
    print("期望输出: -1")


if __name__ == "__main__":
    main()