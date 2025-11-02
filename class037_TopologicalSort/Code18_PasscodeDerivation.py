import heapq

def derive_passcode(attempts):
    """
    Passcode derivation - Project Euler Problem 79 - Python实现
    题目解析：通过密码片段推断最短可能密码，使用拓扑排序
    
    算法思路：
    1. 从密码片段中提取数字间的顺序关系
    2. 构建有向图并进行拓扑排序
    3. 输出字典序最小的拓扑序列
    
    时间复杂度：O(n)
    空间复杂度：O(1)，只有10个数字
    
    工程化考虑：
    1. 关系提取：从密码片段中提取数字顺序
    2. 图构建：建立数字间的依赖关系
    3. 拓扑排序：确定数字的排列顺序
    4. 结果验证：确保密码满足所有约束
    """
    # 构建图：10个数字（0-9）
    graph = [[False] * 10 for _ in range(10)]
    indegree = [0] * 10
    exists = [False] * 10
    
    # 从密码片段中提取顺序关系
    for attempt in attempts:
        digits = list(map(int, attempt))
        
        # 标记存在的数字
        for digit in digits:
            exists[digit] = True
        
        # 提取顺序关系：digits[i] 在 digits[j] 之前 (i < j)
        for i in range(len(digits)):
            for j in range(i + 1, len(digits)):
                u = digits[i]
                v = digits[j]
                
                if not graph[u][v]:
                    graph[u][v] = True
                    indegree[v] += 1
    
    # 拓扑排序（使用最小堆实现字典序最小）
    min_heap = []
    for i in range(10):
        if exists[i] and indegree[i] == 0:
            heapq.heappush(min_heap, i)
    
    passcode = []
    while min_heap:
        u = heapq.heappop(min_heap)
        passcode.append(str(u))
        
        for v in range(10):
            if graph[u][v]:
                indegree[v] -= 1
                if indegree[v] == 0:
                    heapq.heappush(min_heap, v)
    
    return ''.join(passcode)

def validate_passcode(passcode, attempts):
    """
    验证密码是否满足所有片段约束
    """
    for attempt in attempts:
        if not is_subsequence(passcode, attempt):
            return False
    return True

def is_subsequence(passcode, attempt):
    """
    检查attempt是否是passcode的子序列（保持相对顺序）
    """
    i, j = 0, 0
    while i < len(passcode) and j < len(attempt):
        if passcode[i] == attempt[j]:
            j += 1
        i += 1
    return j == len(attempt)

# 测试用例
if __name__ == "__main__":
    # Project Euler官方示例
    attempts = [
        "319", "680", "180", "690", "129", "620", "762", "689", "762", "318", 
        "368", "710", "720", "710", "629", "168", "160", "689", "716", "731", 
        "736", "729", "316", "729", "729", "710", "769", "290", "719", "680", 
        "318", "389", "162", "289", "162", "718", "729", "319", "790", "680", 
        "890", "362", "319", "760", "316", "729", "380", "319", "728", "716"
    ]
    
    passcode = derive_passcode(attempts)
    print("推断出的密码:", passcode)
    
    # 验证密码是否满足所有约束
    if validate_passcode(passcode, attempts):
        print("密码验证通过")
    else:
        print("密码验证失败")