import heapq

"""
相关题目6: LeetCode 973. 最接近原点的 K 个点
题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
题目描述: 给定一个数组 points，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
并给定一个整数 k，返回离原点最近的 k 个点
解题思路: 使用最大堆维护k个最近的点
时间复杂度: O(n log k)
空间复杂度: O(k)
是否最优解: 是，这是处理Top K距离问题的经典解法
"""
def kClosest(points, k):
    # 使用最大堆维护k个最近的点
    # Python的heapq是最小堆，所以存储负的距离值来模拟最大堆
    maxHeap = []
    
    for point in points:
        dist = point[0] * point[0] + point[1] * point[1]
        if len(maxHeap) < k:
            # 存储(-距离, 点坐标)来实现最大堆
            heapq.heappush(maxHeap, (-dist, point))
        else:
            # 如果当前点比堆顶点更近，则替换
            if dist < -maxHeap[0][0]:
                heapq.heapreplace(maxHeap, (-dist, point))
    
    # 提取结果
    result = [point for _, point in maxHeap]
    
    return result

# 测试方法
if __name__ == "__main__":
    points1 = [[1, 1], [3, 3], [2, 2]]
    k1 = 2
    result1 = kClosest(points1, k1)
    print("示例1输出:", result1)  # 期望输出: [[1,1],[2,2]] 或 [[2,2],[1,1]]
    
    points2 = [[3, 3], [5, -1], [-2, 4]]
    k2 = 2
    result2 = kClosest(points2, k2)
    print("示例2输出:", result2)  # 期望输出: [[3,3],[-2,4]] 或 [[-2,4],[3,3]]