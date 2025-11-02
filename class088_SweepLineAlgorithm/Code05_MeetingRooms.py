# 会议室问题
# 给定一个会议时间安排的数组 intervals ，
# 每个会议时间都会包括开始和结束的时间 intervals[i] = [starti, endi] ，
# 请你判断一个人是否能够参加这里面的全部会议。
# 测试链接 : https://leetcode.cn/problems/meeting-rooms/

from typing import List

class Solution:
    def canAttendMeetings(self, intervals: List[List[int]]) -> bool:
        """
        判断一个人是否能够参加所有会议
        
        时间复杂度: O(n*logn)
        空间复杂度: O(1)
        
        解题思路:
        1. 将所有会议按照开始时间排序
        2. 遍历排序后的会议，检查当前会议的开始时间是否早于前一个会议的结束时间
        3. 如果有冲突，返回False；否则返回True
        
        Args:
            intervals: 会议时间安排的数组，每个元素为[开始时间, 结束时间]
            
        Returns:
            bool: 是否能参加所有会议
        """
        # 边界条件处理
        # 如果没有会议，可以参加所有会议
        if not intervals:
            return True
        
        # 按照会议开始时间排序
        # 时间复杂度: O(n*logn)
        intervals.sort(key=lambda x: x[0])
        
        # 遍历所有会议，检查是否有时间冲突
        # 时间复杂度: O(n)
        for i in range(1, len(intervals)):
            # 如果当前会议的开始时间早于前一个会议的结束时间，说明有冲突
            if intervals[i][0] < intervals[i-1][1]:
                return False
        
        # 没有发现时间冲突，可以参加所有会议
        return True


# 测试用例
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: [[0,30],[5,10],[15,20]]
    # 预期输出: False
    intervals1 = [[0, 30], [5, 10], [15, 20]]
    print(solution.canAttendMeetings(intervals1))  # False
    
    # 测试用例2: [[7,10],[2,4]]
    # 预期输出: True
    intervals2 = [[7, 10], [2, 4]]
    print(solution.canAttendMeetings(intervals2))  # True
    
    # 测试用例3: []
    # 预期输出: True
    intervals3 = []
    print(solution.canAttendMeetings(intervals3))  # True
    
    # 测试用例4: [[1,2],[2,3]]
    # 预期输出: True
    intervals4 = [[1, 2], [2, 3]]
    print(solution.canAttendMeetings(intervals4))  # True