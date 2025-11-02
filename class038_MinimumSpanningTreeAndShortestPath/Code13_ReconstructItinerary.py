# LeetCode 332. Reconstruct Itinerary
# 题目链接: https://leetcode.cn/problems/reconstruct-itinerary/
# 
# 题目描述:
# 给你一份航线列表tickets，其中tickets[i] = [from_i, to_i]表示飞机出发和降落的机场地点。请你对该行程进行重新规划排序。
# 所有这些机票都属于一个从JFK（肯尼迪国际机场）出发的先生，所以该行程必须从JFK开始。假设所有机票至少存在一种合理的行程。
# 如果你有多个有效的行程，请你按字典排序返回最小的行程组合。
# 例如，行程["JFK", "LGA"]与["JFK", "LGB"]相比，按字典排序更小的行程是["JFK", "LGA"]。
# 所有机票必须都用一次且只能用一次。
#
# 解题思路:
# 这是一个经典的欧拉路径问题。我们需要找到一条路径，使得它恰好使用了每条边一次，并且路径字典序最小。
# 我们可以使用Hierholzer算法来求解欧拉路径：
# 1. 构建图，使用邻接表表示，并且对每个节点的邻接列表进行排序以确保字典序最小
# 2. 使用深度优先搜索(DFS)遍历图，递归地访问每个节点的邻居
# 3. 当一个节点没有未访问的邻居时，将其添加到结果列表的开头
# 4. 最终得到的结果列表即为欧拉路径
#
# 时间复杂度: O(E log E)，其中E是边数，排序邻接列表需要O(E log E)的时间
# 空间复杂度: O(V + E)，其中V是顶点数，E是边数
# 是否为最优解: 是，Hierholzer算法是求解欧拉路径的高效算法

from collections import defaultdict

def findItinerary(tickets):
    # 构建图，使用默认字典和列表，用于存储邻接表
    graph = defaultdict(list)
    for start, end in tickets:
        graph[start].append(end)
    
    # 对每个节点的邻接列表进行排序，以确保字典序最小
    for start in graph:
        graph[start].sort(reverse=True)  # 按逆序排序，这样弹出最后一个元素时就是字典序最小的
    
    # 存储结果的列表
    result = []
    
    def dfs(node):
        # 当节点还有邻居时，继续访问
        while graph[node]:
            # 弹出字典序最小的邻居（因为是逆序排序，所以弹出最后一个元素）
            next_node = graph[node].pop()
            dfs(next_node)
        # 当节点没有未访问的邻居时，将其添加到结果列表
        result.append(node)
    
    # 从JFK开始DFS
    dfs("JFK")
    
    # 因为我们是在回溯时将节点添加到结果列表的，所以最终需要反转列表
    return result[::-1]

# 另一种实现方式：迭代版本
def findItinerary_iterative(tickets):
    # 构建图
    graph = defaultdict(list)
    for start, end in tickets:
        graph[start].append(end)
    
    # 对每个节点的邻接列表进行排序
    for start in graph:
        graph[start].sort(reverse=True)
    
    # 使用栈来模拟DFS
    stack = ["JFK"]
    result = []
    
    while stack:
        current = stack[-1]
        
        # 如果当前节点还有邻居，则继续访问
        if graph[current]:
            stack.append(graph[current].pop())
        else:
            # 否则，将当前节点添加到结果列表
            result.append(stack.pop())
    
    # 反转结果列表
    return result[::-1]

# 测试用例
def test():
    # 测试用例1
    tickets1 = [["MUC", "LHR"], ["JFK", "MUC"], ["SFO", "SJC"], ["LHR", "SFO"]]
    print(f"Test 1 (递归): {findItinerary(tickets1)}")
    print(f"Test 1 (迭代): {findItinerary_iterative(tickets1)}")
    # 预期输出: ["JFK", "MUC", "LHR", "SFO", "SJC"]
    
    # 测试用例2
    tickets2 = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
    print(f"Test 2 (递归): {findItinerary(tickets2)}")
    print(f"Test 2 (迭代): {findItinerary_iterative(tickets2)}")
    # 预期输出: ["JFK","ATL","JFK","SFO","ATL","SFO"]

if __name__ == "__main__":
    test()