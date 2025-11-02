#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 973. 最接近原点的K个点

问题描述：
给定一个由平面上的点组成的数组 points，其中 points[i] = [xi, yi]，
从中选取 k 个距离原点 (0, 0) 最近的点。可以按任意顺序返回答案。

算法思路：
本题可以使用多种方法解决：
1. 排序法：按照距离原点的距离排序，取前k个点
2. 最小堆法：维护一个大小为k的最大堆
3. 快速选择法：使用快速选择算法找到第k小的元素
4. 最近点对算法的变种

时间复杂度：
- 排序法：O(n log n)
- 最小堆法：O(n log k)
- 快速选择法：O(n) 平均情况
空间复杂度：O(k)

应用场景：
1. 机器学习中的最近邻搜索
2. 地理信息系统中的最近设施查询
3. 推荐系统中的相似用户查找

相关题目：
1. LeetCode 347. 前 K 个高频元素
2. LeetCode 215. 数组中的第K个最大元素
3. LeetCode 719. 找出第 k 小的距离对
"""

import heapq
import random
import time

class LeetCode973KClosestPointsToOrigin:
    """LeetCode 973. 最接近原点的K个点解法实现"""
    
    def k_closest_sort(self, points, k):
        """
        方法1：排序法
        时间复杂度：O(n log n)
        空间复杂度：O(1)
        """
        # 按照距离原点的平方排序（避免开方运算）
        points.sort(key=lambda point: point[0]**2 + point[1]**2)
        # 返回前k个点
        return points[:k]
    
    def k_closest_heap(self, points, k):
        """
        方法2：最小堆法
        时间复杂度：O(n log k)
        空间复杂度：O(k)
        """
        # 使用最大堆，保持堆大小为k
        # Python的heapq是最小堆，所以我们存储负距离来模拟最大堆
        max_heap = []
        
        # 遍历所有点
        for point in points:
            dist = point[0]**2 + point[1]**2
            if len(max_heap) < k:
                # 堆未满，直接添加
                heapq.heappush(max_heap, (-dist, point))
            elif dist < -max_heap[0][0]:
                # 当前点比堆顶更近，替换堆顶
                heapq.heapreplace(max_heap, (-dist, point))
        
        # 提取结果
        return [point for _, point in max_heap]
    
    def k_closest_quick_select(self, points, k):
        """
        方法3：快速选择法
        时间复杂度：O(n) 平均情况
        空间复杂度：O(1)
        """
        def get_distance(point):
            return point[0]**2 + point[1]**2
        
        def quick_select(left, right, k):
            if left >= right:
                return
            
            # 随机选择pivot以避免最坏情况
            pivot_index = random.randint(left, right)
            points[pivot_index], points[right] = points[right], points[pivot_index]
            
            # 分区操作
            pivot_dist = get_distance(points[right])
            i = left
            
            for j in range(left, right):
                if get_distance(points[j]) <= pivot_dist:
                    points[i], points[j] = points[j], points[i]
                    i += 1
            
            points[i], points[right] = points[right], points[i]
            
            # 递归处理
            if i == k - 1:
                return
            elif i < k - 1:
                quick_select(i + 1, right, k)
            else:
                quick_select(left, i - 1, k)
        
        # 复制数组以避免修改原数组
        points_copy = points[:]
        quick_select(0, len(points_copy) - 1, k)
        return points_copy[:k]
    
    @staticmethod
    def test_k_closest_points():
        """测试函数"""
        solution = LeetCode973KClosestPointsToOrigin()
        
        print("=== 测试 LeetCode 973. 最接近原点的K个点 ===")
        
        # 测试用例1
        points1 = [[1,1],[2,2],[3,3]]
        k1 = 1
        print("测试用例1:")
        print("点集:", points1)
        print("k =", k1)
        print("排序法结果:", solution.k_closest_sort([p[:] for p in points1], k1))
        print("堆法结果:", solution.k_closest_heap([p[:] for p in points1], k1))
        print("快速选择法结果:", solution.k_closest_quick_select([p[:] for p in points1], k1))
        
        # 测试用例2
        points2 = [[3,3],[5,-1],[-2,4]]
        k2 = 2
        print("\n测试用例2:")
        print("点集:", points2)
        print("k =", k2)
        print("排序法结果:", solution.k_closest_sort([p[:] for p in points2], k2))
        print("堆法结果:", solution.k_closest_heap([p[:] for p in points2], k2))
        print("快速选择法结果:", solution.k_closest_quick_select([p[:] for p in points2], k2))
        
        # 性能测试
        print("\n=== 性能测试 ===")
        random.seed(42)
        n = 10000
        points3 = [[random.randint(-5000, 5000), random.randint(-5000, 5000)] for _ in range(n)]
        k3 = 100
        
        start_time = time.time()
        solution.k_closest_sort([p[:] for p in points3], k3)
        end_time = time.time()
        print(f"排序法处理{n}个点选取{k3}个最近点时间: {(end_time - start_time) * 1000:.2f} ms")
        
        start_time = time.time()
        solution.k_closest_heap([p[:] for p in points3], k3)
        end_time = time.time()
        print(f"堆法处理{n}个点选取{k3}个最近点时间: {(end_time - start_time) * 1000:.2f} ms")
        
        start_time = time.time()
        solution.k_closest_quick_select([p[:] for p in points3], k3)
        end_time = time.time()
        print(f"快速选择法处理{n}个点选取{k3}个最近点时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    LeetCode973KClosestPointsToOrigin.test_k_closest_points()