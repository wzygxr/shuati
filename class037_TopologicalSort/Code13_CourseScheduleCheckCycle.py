from collections import deque, defaultdict

def canFinish(numCourses, prerequisites):
    """
    课程表 - 拓扑排序判环 - Python实现
    题目解析：判断课程安排图中是否存在环
    
    算法思路：
    1. 使用邻接表存储课程依赖关系
    2. 计算每个课程的入度
    3. 使用队列进行BFS拓扑排序
    4. 如果完成的课程数等于总课程数，说明无环
    
    时间复杂度：O(V + E)
    空间复杂度：O(V + E)
    
    工程化考虑：
    1. 使用defaultdict简化邻接表操作
    2. 使用deque实现队列
    3. 边界处理：空课程、单课程等情况
    4. 异常捕获和错误处理
    """
    # 特殊情况处理
    if numCourses <= 0:
        return True
    if not prerequisites:
        return True
    
    # 构建邻接表和入度数组
    graph = defaultdict(list)
    indegree = [0] * numCourses
    
    # 构建图并计算入度
    for course, prerequisite in prerequisites:
        graph[prerequisite].append(course)
        indegree[course] += 1
    
    # 使用队列进行拓扑排序
    queue = deque()
    for i in range(numCourses):
        if indegree[i] == 0:
            queue.append(i)
    
    count = 0  # 已完成的课程数
    while queue:
        course = queue.popleft()
        count += 1
        
        # 更新依赖该课程的课程的入度
        for next_course in graph[course]:
            indegree[next_course] -= 1
            if indegree[next_course] == 0:
                queue.append(next_course)
    
    return count == numCourses

# 测试用例
if __name__ == "__main__":
    # 测试用例1：无环，可以完成
    numCourses1 = 2
    prerequisites1 = [[1, 0]]
    print(f"测试用例1: {canFinish(numCourses1, prerequisites1)}")  # 输出: True
    
    # 测试用例2：有环，无法完成
    numCourses2 = 2
    prerequisites2 = [[1, 0], [0, 1]]
    print(f"测试用例2: {canFinish(numCourses2, prerequisites2)}")  # 输出: False
    
    # 测试用例3：空课程
    numCourses3 = 0
    prerequisites3 = []
    print(f"测试用例3: {canFinish(numCourses3, prerequisites3)}")  # 输出: True
    
    # 测试用例4：单课程无依赖
    numCourses4 = 1
    prerequisites4 = []
    print(f"测试用例4: {canFinish(numCourses4, prerequisites4)}")  # 输出: True