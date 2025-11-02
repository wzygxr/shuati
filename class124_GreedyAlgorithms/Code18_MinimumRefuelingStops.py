# 最低加油次数（Minimum Number of Refueling Stops）
# 题目来源：LeetCode 871
# 题目链接：https://leetcode.cn/problems/minimum-number-of-refueling-stops/
# 
# 问题描述：
# 汽车从起点出发驶向目的地，该目的地距离起点target英里。
# 沿途有加油站，每个station[i]代表一个加油站，位于距离起点station[i][0]英里处，有station[i][1]升汽油。
# 假设汽车油箱的容量是无限的，其中最初有startFuel升燃料。
# 它每行驶1英里就会用掉1升汽油。
# 当汽车到达加油站时，它可能停下来加油，将所有汽油从加油站转移到汽车中。
# 为了到达目的地，汽车所必要的最低加油次数是多少？如果无法到达目的地，则返回-1。
# 
# 算法思路：
# 使用贪心策略，结合最大堆：
# 1. 遍历加油站，维护当前能到达的最远位置
# 2. 当油量不足以到达下一个加油站时，从经过的加油站中选择油量最大的进行加油
# 3. 使用最大堆来存储经过的加油站的油量
# 4. 每次加油后更新当前油量和加油次数
# 
# 时间复杂度：O(n log n) - 堆操作的时间复杂度
# 空间复杂度：O(n) - 最大堆的空间
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 路径规划问题
# 2. 资源调度问题
# 
# 异常处理：
# 1. 处理无法到达目的地的情况
# 2. 处理边界条件
# 
# 工程化考量：
# 1. 输入验证：检查参数是否合法
# 2. 边界条件：处理起点就是目的地的情况
# 3. 性能优化：使用堆提高效率
# 
# 相关题目：
# 1. LeetCode 134. 加油站 - 环路加油问题
# 2. LeetCode 45. 跳跃游戏 II - 最少跳跃次数
# 3. LeetCode 55. 跳跃游戏 - 可达性判断
# 4. 牛客网 NC48 跳跃游戏 - 与LeetCode 55相同
# 5. LintCode 117. 跳跃游戏 II - 与LeetCode 45相同
# 6. HackerRank - Jumping on the Clouds - 简化版跳跃游戏
# 7. CodeChef - JUMP - 类似跳跃游戏的变种
# 8. AtCoder ABC161D - Lunlun Number - BFS搜索相关
# 9. Codeforces 1324C - Frog Jumps - 贪心跳跃问题
# 10. POJ 1700 - Crossing River - 经典过河问题

import heapq

class Solution:
    """
    计算最低加油次数
    
    Args:
        target: int - 目的地距离
        startFuel: int - 初始油量
        stations: List[List[int]] - 加油站数组，每个元素是[位置, 油量]
    
    Returns:
        int - 最低加油次数，无法到达返回-1
    """
    def minRefuelStops(self, target, startFuel, stations):
        # 边界条件检查
        if startFuel >= target:
            return 0  # 初始油量足够到达目的地
        
        if not stations:
            return 0 if startFuel >= target else -1  # 没有加油站，检查初始油量是否足够
        
        # 最大堆（使用最小堆的负数来模拟最大堆）
        max_heap = []
        
        current_fuel = startFuel  # 当前油量
        current_position = 0      # 当前位置
        refuel_count = 0          # 加油次数
        station_index = 0         # 加油站索引
        
        while current_position + current_fuel < target:
            next_position = current_position + current_fuel  # 当前能到达的最远位置
            
            # 将能到达的加油站加入最大堆
            while station_index < len(stations) and stations[station_index][0] <= next_position:
                heapq.heappush(max_heap, -stations[station_index][1])  # 使用负数模拟最大堆
                station_index += 1
            
            # 如果没有加油站可加油，且无法到达目的地
            if not max_heap:
                return -1
            
            # 选择油量最大的加油站加油
            max_fuel = -heapq.heappop(max_heap)
            current_fuel = current_fuel - (stations[station_index - 1][0] - current_position) + max_fuel
            current_position = stations[station_index - 1][0]
            refuel_count += 1
            
            # 如果加油后能到达目的地
            if current_position + current_fuel >= target:
                return refuel_count
        
        return refuel_count


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况 - 需要加油
    target1 = 100
    start_fuel1 = 10
    stations1 = [[10, 60], [20, 30], [30, 30], [60, 40]]
    result1 = solution.minRefuelStops(target1, start_fuel1, stations1)
    print("测试用例1:")
    print(f"目的地距离: {target1}")
    print(f"初始油量: {start_fuel1}")
    print(f"加油站: {stations1}")
    print(f"最低加油次数: {result1}")
    print("期望输出: 2")
    print()
    
    # 测试用例2: 基本情况 - 无法到达
    target2 = 100
    start_fuel2 = 10
    stations2 = [[20, 20]]
    result2 = solution.minRefuelStops(target2, start_fuel2, stations2)
    print("测试用例2:")
    print(f"目的地距离: {target2}")
    print(f"初始油量: {start_fuel2}")
    print(f"加油站: {stations2}")
    print(f"最低加油次数: {result2}")
    print("期望输出: -1")
    print()
    
    # 测试用例3: 边界情况 - 不需要加油
    target3 = 50
    start_fuel3 = 60
    stations3 = [[10, 20], [20, 30]]
    result3 = solution.minRefuelStops(target3, start_fuel3, stations3)
    print("测试用例3:")
    print(f"目的地距离: {target3}")
    print(f"初始油量: {start_fuel3}")
    print(f"加油站: {stations3}")
    print(f"最低加油次数: {result3}")
    print("期望输出: 0")
    print()
    
    # 测试用例4: 边界情况 - 没有加油站
    target4 = 50
    start_fuel4 = 40
    stations4 = []
    result4 = solution.minRefuelStops(target4, start_fuel4, stations4)
    print("测试用例4:")
    print(f"目的地距离: {target4}")
    print(f"初始油量: {start_fuel4}")
    print(f"加油站: {stations4}")
    print(f"最低加油次数: {result4}")
    print("期望输出: -1")
    print()
    
    # 测试用例5: 复杂情况 - 多个加油站
    target5 = 100
    start_fuel5 = 20
    stations5 = [[10, 10], [20, 20], [30, 30], [40, 40], [50, 50]]
    result5 = solution.minRefuelStops(target5, start_fuel5, stations5)
    print("测试用例5:")
    print(f"目的地距离: {target5}")
    print(f"初始油量: {start_fuel5}")
    print(f"加油站: {stations5}")
    print(f"最低加油次数: {result5}")
    print("期望输出: 3")


if __name__ == "__main__":
    main()