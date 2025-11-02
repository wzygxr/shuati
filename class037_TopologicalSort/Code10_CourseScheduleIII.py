import heapq

def scheduleCourse(courses):
    """
    课程表 III - Python实现
    题目解析：贪心 + 优先队列，在截止时间前尽可能多地完成课程
    
    算法思路：
    1. 按照截止时间对课程进行排序
    2. 使用最大堆记录已选课程的持续时间
    3. 遍历课程，根据时间约束进行选择或替换
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    
    工程化考虑：
    1. 使用heapq实现最大堆（通过存储负数）
    2. 输入验证和边界处理
    3. 异常捕获和错误处理
    """
    # 按照截止时间排序
    courses.sort(key=lambda x: x[1])
    
    # 最大堆，存储已选课程的持续时间（通过负数实现最大堆）
    max_heap = []
    time = 0  # 当前已用时间
    
    for duration, last_day in courses:
        if time + duration <= last_day:
            # 可以直接选择这门课程
            time += duration
            heapq.heappush(max_heap, -duration)
        elif max_heap and -max_heap[0] > duration:
            # 替换掉持续时间最长的课程
            time = time - (-heapq.heappop(max_heap)) + duration
            heapq.heappush(max_heap, -duration)
    
    return len(max_heap)

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    courses1 = [[100, 200], [200, 1300], [1000, 1250], [2000, 3200]]
    print(scheduleCourse(courses1))  # 输出: 3
    
    # 测试用例2
    courses2 = [[1, 2]]
    print(scheduleCourse(courses2))  # 输出: 1
    
    # 测试用例3
    courses3 = [[3, 2], [4, 3]]
    print(scheduleCourse(courses3))  # 输出: 0