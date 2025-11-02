# 会议室 II
# 给你一个会议时间安排的数组 intervals ，
# 每个会议时间包括开始和结束的时间 intervals[i] = [starti, endi] ，
# 返回所需会议室的最小数量。
# 测试链接 : https://leetcode.cn/problems/meeting-rooms-ii/

import heapq
from typing import List

class Solution:
    def minMeetingRooms(self, intervals: List[List[int]]) -> int:
        """
        计算所需会议室的最小数量
        
        时间复杂度: O(n*logn)
        空间复杂度: O(n)
        
        解题思路:
        1. 将所有会议按照开始时间排序
        2. 使用最小堆维护当前正在使用的会议室的结束时间
        3. 遍历排序后的会议:
           - 如果堆顶的结束时间小于等于当前会议的开始时间，说明有会议室空闲，可以复用
           - 否则需要新的会议室
        4. 堆的大小就是所需的最少会议室数量
        
        Args:
            intervals: 会议时间安排的数组，每个元素为[开始时间, 结束时间]
            
        Returns:
            int: 所需会议室的最小数量
        """
        # 边界条件处理
        if not intervals:
            return 0
        
        # 按照会议开始时间排序
        # 时间复杂度: O(n*logn)
        intervals.sort(key=lambda x: x[0])
        
        # 使用最小堆维护当前正在使用的会议室的结束时间
        # 堆顶是最早结束的会议室
        heap = []
        
        # 遍历所有会议
        # 时间复杂度: O(n*logn)
        for interval in intervals:
            start, end = interval[0], interval[1]
            
            # 如果堆不为空且堆顶的结束时间小于等于当前会议的开始时间
            # 说明有会议室空闲，可以复用
            if heap and heap[0] <= start:
                heapq.heappop(heap)  # 释放会议室
            
            # 当前会议需要占用一个会议室
            heapq.heappush(heap, end)
        
        # 堆的大小就是所需的最少会议室数量
        return len(heap)


# 测试用例
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [[0,30],[5,10],[15,20]]
    # 预期输出: 2
    intervals1 = [[0, 30], [5, 10], [15, 20]]
    print(solution.minMeetingRooms(intervals1))  # 2
    
    # 测试用例2: [[7,10],[2,4]]
    # 预期输出: 1
    intervals2 = [[7, 10], [2, 4]]
    print(solution.minMeetingRooms(intervals2))  # 1
    
    # 测试用例3: [[9,10],[4,9],[4,17]]
    # 预期输出: 2
    intervals3 = [[9, 10], [4, 9], [4, 17]]
    print(solution.minMeetingRooms(intervals3))  # 2
    
    # 测试用例4: []
    # 预期输出: 0
    intervals4 = []
    print(solution.minMeetingRooms(intervals4))  # 0