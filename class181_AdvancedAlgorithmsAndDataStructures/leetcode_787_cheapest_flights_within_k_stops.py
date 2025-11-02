#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 787 Cheapest Flights Within K Stops

题目描述：
有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，
你的任务是找到一条最多经过 k 站中转的路线，使得从 src 到 dst 的价格最便宜，
并返回该价格。如果不存在这样的路线，则输出 -1。

解题思路：
我们可以使用Dijkstra算法的变种或Bellman-Ford算法来解决这个问题。
由于限制了最多经过k站中转，我们可以使用修改版的Bellman-Ford算法。

时间复杂度：O(k * E)
空间复杂度：O(n)
"""

import sys

class Solution:
    def find_cheapest_price(self, n, flights, src, dst, k):
        """
        找到最多经过k站中转的最便宜航班路线
        
        Args:
            n: 城市数量
            flights: 航班信息列表，每个元素为[from, to, price]
            src: 出发城市
            dst: 目的城市
            k: 最多中转站数
            
        Returns:
            最便宜的价格，如果不存在路线则返回-1
        """
        # 初始化距离数组
        dist = [sys.maxsize] * n
        dist[src] = 0
        
        # 进行k+1轮松弛操作
        for i in range(k + 1):
            # 创建临时距离数组
            temp = dist[:]
            
            # 松弛所有边
            for from_city, to_city, price in flights:
                if dist[from_city] != sys.maxsize:
                    temp[to_city] = min(temp[to_city], dist[from_city] + price)
            
            # 更新距离数组
            dist = temp
        
        return dist[dst] if dist[dst] != sys.maxsize else -1


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    n1 = 3
    flights1 = [[0,1,100],[1,2,100],[0,2,500]]
    src1, dst1, k1 = 0, 2, 1
    print("测试用例1:")
    print("城市数:", n1)
    print("航班:", flights1)
    print("出发城市:", src1, "目的城市:", dst1, "最多中转:", k1)
    print("结果:", solution.find_cheapest_price(n1, flights1, src1, dst1, k1))
    print()
    
    # 测试用例2
    n2 = 3
    flights2 = [[0,1,100],[1,2,100],[0,2,500]]
    src2, dst2, k2 = 0, 2, 0
    print("测试用例2:")
    print("城市数:", n2)
    print("航班:", flights2)
    print("出发城市:", src2, "目的城市:", dst2, "最多中转:", k2)
    print("结果:", solution.find_cheapest_price(n2, flights2, src2, dst2, k2))
    print()
    
    # 测试用例3
    n3 = 4
    flights3 = [[0,1,1],[0,2,5],[1,2,1],[2,3,1]]
    src3, dst3, k3 = 0, 3, 1
    print("测试用例3:")
    print("城市数:", n3)
    print("航班:", flights3)
    print("出发城市:", src3, "目的城市:", dst3, "最多中转:", k3)
    print("结果:", solution.find_cheapest_price(n3, flights3, src3, dst3, k3))


if __name__ == "__main__":
    main()