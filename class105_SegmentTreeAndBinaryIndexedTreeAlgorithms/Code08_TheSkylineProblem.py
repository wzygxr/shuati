# LeetCode 218. 天际线问题
# 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
# 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
# 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
# lefti 是第 i 座建筑物左边缘的 x 坐标。
# righti 是第 i 座建筑物右边缘的 x 坐标。
# heighti 是第 i 座建筑物的高度。
# 你可以假设所有的建筑都是完美的长方形，在高度为 0 的绝对平坦的表面上。
# 天际线应该表示为由“关键点”组成的列表，格式 [[x1,y1],[x2,y2],...] ，并按 x 坐标进行排序。
# 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y 坐标始终为 0，仅用于标记天际线的终点。
# 此外，任何地面或建筑物上都不应有零长度的线段。
# 测试链接: https://leetcode.cn/problems/the-skyline-problem/

from typing import List
import heapq
from collections import defaultdict

def getSkyline(buildings: List[List[int]]) -> List[List[int]]:
    """
    使用扫描线算法解决天际线问题
    
    解题思路:
    1. 将建筑物的左右边界作为事件处理，使用扫描线算法
    2. 对所有事件点进行排序处理
    3. 使用有序数据结构维护当前活跃建筑物的高度
    4. 处理每个事件点，更新高度并确定关键点
    
    时间复杂度分析:
    - 事件排序: O(n log n)
    - 处理事件: O(n log n)
    - 总时间复杂度: O(n log n)
    
    空间复杂度分析:
    - 事件列表: O(n)
    - 高度维护结构: O(n)
    - 结果列表: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 扫描线算法优化
    2. 有序数据结构维护活跃高度
    3. 边界条件处理
    4. 详细注释和变量命名
    """
    # 创建事件列表
    events = []
    
    # 收集所有事件点
    for left, right, height in buildings:
        # 起始事件，用负数表示
        events.append((left, -height))
        # 结束事件
        events.append((right, height))
    
    # 排序事件
    # 如果x坐标相同，进入事件(负高度)优先于离开事件(正高度)
    events.sort()
    
    # 使用字典维护当前活跃建筑物的高度计数
    # 保持高度到计数的映射
    height_count = defaultdict(int)
    height_count[0] = 1  # 初始地面高度
    result = []
    prev_height = 0
    
    for x, h in events:
        if h < 0:
            # 起始事件，添加建筑物高度
            height_count[-h] += 1
        else:
            # 结束事件，移除建筑物高度
            height_count[h] -= 1
            if height_count[h] == 0:
                del height_count[h]
        
        # 获取当前最大高度
        current_height = max(height_count.keys()) if height_count else 0
        
        # 如果高度发生变化，添加关键点
        if current_height != prev_height:
            result.append([x, current_height])
            prev_height = current_height
    
    return result


def getSkylineHeap(buildings: List[List[int]]) -> List[List[int]]:
    """
    使用堆解决天际线问题的另一种实现
    
    解题思路:
    1. 使用最小堆维护建筑物的右边界和高度
    2. 扫描线算法处理建筑物的起始和结束
    3. 通过堆顶元素判断当前有效高度
    """
    # 创建事件点
    events = []
    for left, right, height in buildings:
        events.append((left, -height, right))  # 起始事件
        events.append((right, 0, 0))           # 结束事件占位符
    
    # 按x坐标排序
    events.sort()
    
    # 结果列表
    res = [[0, 0]]
    
    # 最小堆存储 (右边界, 高度)，使用负数实现最大堆效果
    heap = [(0, float('inf'))]  # (高度, 右边界)
    
    for x, neg_h, right in events:
        # 添加起始事件对应的建筑物到堆中
        if neg_h != 0:
            heapq.heappush(heap, (neg_h, right))
        
        # 移除堆中已经结束的建筑物
        while heap[0][1] <= x:
            heapq.heappop(heap)
        
        # 获取当前最大高度
        current_height = -heap[0][0]
        
        # 如果高度发生变化，添加关键点
        if res[-1][1] != current_height:
            res.append([x, current_height])
    
    return res[1:]  # 移除初始占位符


# 测试函数
def test():
    # 测试用例1
    buildings1 = [[2, 9, 10], [3, 7, 15], [5, 12, 12], [15, 20, 10], [19, 24, 8]]
    result1 = getSkyline(buildings1)
    print("Test case 1:")
    print("Input: [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]")
    print("Output:", result1)
    # 期望输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
    
    # 测试用例2
    buildings2 = [[0, 2, 3], [2, 5, 3]]
    result2 = getSkyline(buildings2)
    print("\nTest case 2:")
    print("Input: [[0,2,3],[2,5,3]]")
    print("Output:", result2)
    # 期望输出: [[0,3],[5,0]]
    
    # 使用堆方法测试
    result3 = getSkylineHeap(buildings1)
    print("\nTest case 1 (heap method):")
    print("Output:", result3)


if __name__ == "__main__":
    test()