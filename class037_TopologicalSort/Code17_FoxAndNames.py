from collections import deque, defaultdict

def alien_order(words):
    """
    Fox and Names - 字典序建图与拓扑排序 - Python实现
    题目解析：通过字符串比较构建字母顺序图，使用拓扑排序判断合法性
    
    算法思路：
    1. 比较相邻字符串，找到第一个不同的字符建立顺序关系
    2. 构建有向图并进行拓扑排序
    3. 检测环的存在，输出结果
    
    时间复杂度：O(n * L)
    空间复杂度：O(1)，固定26个字母
    
    工程化考虑：
    1. 图构建：通过字符串比较建立关系
    2. 环检测：拓扑排序检测非法顺序
    3. 边界处理：前缀关系、空字符串等情况
    """
    # 构建图
    graph = defaultdict(set)
    indegree = {chr(ord('a') + i): 0 for i in range(26)}
    
    # 标记存在的字母
    exists = set()
    for word in words:
        for c in word:
            exists.add(c)
    
    # 比较相邻字符串，构建图
    for i in range(len(words) - 1):
        word1 = words[i]
        word2 = words[i + 1]
        
        # 检查前缀关系
        if len(word1) > len(word2) and word1.startswith(word2):
            return "Impossible"
        
        # 找到第一个不同的字符
        min_len = min(len(word1), len(word2))
        for j in range(min_len):
            c1 = word1[j]
            c2 = word2[j]
            
            if c1 != c2:
                # 建立边 c1 -> c2
                if c2 not in graph[c1]:
                    graph[c1].add(c2)
                    indegree[c2] += 1
                break  # 找到第一个不同字符后停止
    
    # 拓扑排序
    queue = deque()
    for c in exists:
        if indegree[c] == 0:
            queue.append(c)
    
    result = []
    while queue:
        u = queue.popleft()
        result.append(u)
        
        for v in graph[u]:
            indegree[v] -= 1
            if indegree[v] == 0:
                queue.append(v)
    
    # 检查是否有环
    if len(result) != len(exists):
        return "Impossible"
    
    return ''.join(result)

# 测试用例
if __name__ == "__main__":
    # 测试用例1：正常情况
    words1 = ["rivest", "shamir", "adleman"]
    print("测试用例1:", alien_order(words1))
    
    # 测试用例2：存在环
    words2 = ["abc", "ab"]
    print("测试用例2:", alien_order(words2))  # 输出: Impossible
    
    # 测试用例3：正常顺序
    words3 = ["wrt", "wrf", "er", "ett", "rftt"]
    print("测试用例3:", alien_order(words3))
    
    # 测试用例4：前缀关系非法
    words4 = ["apple", "app"]
    print("测试用例4:", alien_order(words4))  # 输出: Impossible