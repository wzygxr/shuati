"""
天际线问题 (LeetCode 218)
题目链接: https://leetcode.cn/problems/the-skyline-problem/

题目描述:
城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

解题思路:
使用扫描线算法结合系统提供的heapq实现天际线问题的求解。
1. 将建筑物的左右边界作为事件点
2. 使用离散化技术处理坐标值
3. 使用最大堆维护当前活动建筑物的高度
4. 扫描过程中记录高度变化的关键点

时间复杂度: O(n log n) - 排序和堆操作
空间复杂度: O(n) - 存储事件和堆

工程化考量:
1. 异常处理: 检查建筑物数据合法性
2. 边界条件: 处理建筑物边界重叠情况
3. 性能优化: 使用离散化减少坐标范围
4. 可读性: 详细注释和模块化设计
"""

import heapq
import bisect

def get_skyline(arr):
    """
    求解天际线问题
    使用heapq作为最大堆，并结合离散化优化
    
    Args:
        arr: 建筑物数组，每个元素为[left, right, height]
    
    Returns:
        天际线的关键点列表，每个元素为[x, y]
    """
    # 边界条件检查
    if not arr:
        raise ValueError("建筑物数组不能为空")
    
    n = len(arr)
    m, xsort = prepare(arr, n)
    
    # 使用最大堆维护当前活动建筑物的高度
    # 堆中元素: (-高度, 结束位置)
    # 按高度降序排列，高度大的在堆顶
    heap = []
    
    # 扫描线算法处理每个离散化后的坐标点
    height = [0] * (m + 1)
    j = 0
    for i in range(1, m + 1):
        # 将起始位置小于等于当前点的所有建筑物加入堆中
        while j < n and arr[j][0] <= i:
            heapq.heappush(heap, (-arr[j][2], arr[j][1]))
            j += 1
        
        # 移除堆中结束位置小于当前点的建筑物
        while heap and heap[0][1] < i:
            heapq.heappop(heap)
        
        # 当前点的最大高度即为堆顶元素的高度
        if heap:
            height[i] = -heap[0][0]
    
    # 构造结果列表
    ans = []
    pre = 0
    for i in range(1, m + 1):
        # 如果高度发生变化，记录关键点
        if pre != height[i]:
            ans.append([xsort[i], height[i]])
        pre = height[i]
    
    return ans

def prepare(arr, n):
    """
    准备工作：对坐标进行离散化处理
    1) 收集大楼左边界、右边界-1、右边界的值
    2) 收集的所有值排序、去重
    3) 大楼的左边界和右边界，修改成排名值
    4) 大楼根据左边界排序
    5) 返回离散值的个数和离散化数组
    
    Args:
        arr: 建筑物数组
        n: 建筑物数量
    
    Returns:
        离散化后的坐标点数量和离散化数组
    """
    # 收集所有需要离散化的坐标值
    # 包括大楼的左边界、右边界-1、右边界
    xsort = []
    for i in range(n):
        xsort.append(arr[i][0])      # 左边界
        xsort.append(arr[i][1] - 1)  # 右边界-1
        xsort.append(arr[i][1])      # 右边界
    
    # 对收集到的坐标值进行排序
    xsort.sort()
    
    # 排序后去重，得到m个不同的坐标值
    xsort = sorted(list(set(xsort)))
    m = len(xsort)
    
    # 将建筑物的左右边界修改为对应的排名值
    for i in range(n):
        arr[i][0] = bisect.bisect_left(xsort, arr[i][0])        # 左边界
        arr[i][1] = bisect.bisect_left(xsort, arr[i][1] - 1)    # 右边界-1
    
    # 所有建筑物根据左边界排序
    arr.sort(key=lambda x: x[0])
    
    return m, xsort

# 测试函数
def test_get_skyline():
    # 测试用例1
    buildings1 = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
    result1 = get_skyline(buildings1)
    print(f"测试用例1: {result1}")
    # 预期: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
    
    # 测试用例2
    buildings2 = [[0,2,3],[2,5,3]]
    result2 = get_skyline(buildings2)
    print(f"测试用例2: {result2}")
    # 预期: [[0,3],[5,0]]
    
    # 测试用例3: 空数组
    try:
        buildings3 = []
        result3 = get_skyline(buildings3)
        print(f"测试用例3: {result3}")
    except ValueError as e:
        print(f"测试用例3 异常: {e}")

if __name__ == "__main__":
    test_get_skyline()