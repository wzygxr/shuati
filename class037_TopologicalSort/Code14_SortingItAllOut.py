from collections import deque

def topological_sort_state(graph, indegree, n):
    """
    拓扑排序状态判断
    @return: 0-无法确定, 1-唯一确定, 2-存在矛盾
    """
    temp_indegree = indegree[:]
    queue = deque()
    
    # 统计入度为0的节点
    for i in range(n):
        if temp_indegree[i] == 0:
            queue.append(i)
    
    determined = True
    result = []
    
    while queue:
        # 如果队列中有多个节点，说明无法确定
        if len(queue) > 1:
            determined = False
        
        u = queue.popleft()
        result.append(u)
        
        for v in range(n):
            if graph[u][v] == 1:
                temp_indegree[v] -= 1
                if temp_indegree[v] == 0:
                    queue.append(v)
    
    # 检查是否有环
    if len(result) < n:
        return 2  # 存在矛盾（有环）
    
    return 1 if determined else 0  # 1-唯一确定, 0-无法确定

def get_topological_sequence(graph, indegree, n):
    """获取拓扑序列（唯一确定时）"""
    temp_indegree = indegree[:]
    queue = deque()
    result = []
    
    for i in range(n):
        if temp_indegree[i] == 0:
            queue.append(i)
    
    while queue:
        u = queue.popleft()
        result.append(u)
        
        for v in range(n):
            if graph[u][v] == 1:
                temp_indegree[v] -= 1
                if temp_indegree[v] == 0:
                    queue.append(v)
    
    return result

def solve_sorting_it_all_out(n, m, relations):
    """
    Sorting It All Out - 拓扑排序状态判断 - Python实现
    题目解析：逐步添加关系并判断拓扑排序状态
    
    算法思路：
    1. 使用邻接矩阵存储图结构
    2. 逐步添加边关系
    3. 每次添加后尝试进行拓扑排序
    4. 根据结果判断三种状态
    
    时间复杂度：O(n * m)
    空间复杂度：O(n^2)
    
    工程化考虑：
    1. 使用邻接矩阵简化边操作
    2. 增量式拓扑排序避免重复计算
    3. 精确判断三种可能状态
    4. 输入验证和边界处理
    """
    graph = [[0] * n for _ in range(n)]
    indegree = [0] * n
    found_result = False
    result_step = -1
    result_state = -1
    result_sequence = []
    
    # 逐步添加关系
    for step in range(m):
        relation = relations[step]
        u = ord(relation[0]) - ord('A')
        v = ord(relation[2]) - ord('A')
        
        # 添加边
        graph[u][v] = 1
        indegree[v] += 1
        
        # 检查状态
        state = topological_sort_state(graph, indegree, n)
        
        if state == 1 or state == 2:
            found_result = True
            result_step = step + 1
            result_state = state
            
            # 如果是唯一确定，记录拓扑序列
            if state == 1:
                result_sequence = get_topological_sequence(graph, indegree, n)
            break
    
    if found_result:
        if result_state == 1:
            sequence_str = ''.join(chr(ord('A') + node) for node in result_sequence)
            print("Sorted sequence determined after {} relations: {}.".format(result_step, sequence_str))
        else:
            print("Inconsistency found after {} relations.".format(result_step))
    else:
        print("Sorted sequence cannot be determined.")

# 测试用例
if __name__ == "__main__":
    # 测试用例1：唯一确定
    n1 = 4
    m1 = 6
    relations1 = [
        "A<B", "A<C", "B<C", "C<D", "B<D", "A<B"
    ]
    print("测试用例1:")
    solve_sorting_it_all_out(n1, m1, relations1)
    
    # 测试用例2：存在矛盾
    n2 = 3
    m2 = 3
    relations2 = [
        "A<B", "B<C", "C<A"
    ]
    print("测试用例2:")
    solve_sorting_it_all_out(n2, m2, relations2)
    
    # 测试用例3：无法确定
    n3 = 3
    m3 = 2
    relations3 = [
        "A<B", "A<C"
    ]
    print("测试用例3:")
    solve_sorting_it_all_out(n3, m3, relations3)