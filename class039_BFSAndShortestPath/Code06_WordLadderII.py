# 单词接龙 II
# 按字典 wordList 完成从单词 beginWord 到单词 endWord 转化
# 一个表示此过程的 转换序列 是形式上像 
# beginWord -> s1 -> s2 -> ... -> sk 这样的单词序列，并满足：
# 每对相邻的单词之间仅有单个字母不同
# 转换过程中的每个单词 si（1 <= i <= k）必须是字典 wordList 中的单词
# 注意，beginWord 不必是字典 wordList 中的单词
# sk == endWord
# 给你两个单词 beginWord 和 endWord ，以及一个字典 wordList
# 请你找出并返回所有从 beginWord 到 endWord 的 最短转换序列
# 如果不存在这样的转换序列，返回一个空列表
# 每个序列都应该以单词列表 [beginWord, s1, s2, ..., sk] 的形式返回
# 测试链接 : https://leetcode.cn/problems/word-ladder-ii/
# 
# 算法思路：
# 使用双向BFS构建图，然后使用DFS找到所有最短路径
# 1. 使用BFS从beginWord开始构建反向图（从endWord指向beginWord）
# 2. 在BFS过程中，只扩展能到达endWord的节点
# 3. 使用DFS在构建的图中找到所有从endWord到beginWord的路径
# 4. 将路径反转得到从beginWord到endWord的路径
# 
# 时间复杂度：O(N * M^2 + M * N^2)，其中N是单词数量，M是单词长度
# 空间复杂度：O(N * M^2)，用于存储图和访问状态
# 
# 工程化考量：
# 1. 使用字典存储图结构
# 2. 使用集合快速查找单词是否存在
# 3. 使用双向BFS优化搜索效率

from collections import deque, defaultdict

def findLadders(beginWord, endWord, wordList):
    """
    找出所有从beginWord到endWord的最短转换序列
    
    Args:
        beginWord: str - 起始单词
        endWord: str - 目标单词
        wordList: List[str] - 单词列表
    
    Returns:
        List[List[str]] - 所有最短转换序列
    """
    # 如果目标单词不在词典中，直接返回空列表
    if endWord not in wordList:
        return []
    
    # 将单词列表转换为集合，提高查找效率
    dict_set = set(wordList)
    
    # 反向图
    graph = defaultdict(list)
    
    # 使用BFS构建图
    cur_level = {beginWord}
    found = False
    
    while cur_level and not found:
        # 移除当前层的所有单词，避免在后续层中再次处理
        dict_set -= cur_level
        next_level = set()
        
        # 处理当前层的所有单词
        for word in cur_level:
            # 每个位置，字符a~z，换一遍！检查在词表中是否存在
            for i in range(len(word)):
                for ch in 'abcdefghijklmnopqrstuvwxyz':
                    if ch != word[i]:
                        new_word = word[:i] + ch + word[i+1:]
                        # 如果新单词在词典中
                        if new_word in dict_set:
                            # 如果找到了目标单词
                            if new_word == endWord:
                                found = True
                            # 在反向图中添加边
                            graph[new_word].append(word)
                            # 将新单词加入下一层
                            next_level.add(new_word)
        
        cur_level = next_level
    
    # 如果找到了目标单词，使用DFS搜索所有路径
    if found:
        ans = []
        path = [endWord]
        
        def dfs(word):
            # 如果到达起始单词
            if word == beginWord:
                # 将路径反转后添加到结果中
                ans.append(path[::-1])
            else:
                # 递归处理所有前驱单词
                for next_word in graph[word]:
                    path.append(next_word)
                    dfs(next_word)
                    path.pop()
        
        dfs(endWord)
        return ans
    else:
        return []

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    beginWord1 = "hit"
    endWord1 = "cog"
    wordList1 = ["hot","dot","dog","lot","log","cog"]
    result1 = findLadders(beginWord1, endWord1, wordList1)
    print("测试用例1结果:")
    for path in result1:
        print(path)
    # 预期输出: 
    # ['hit', 'hot', 'dot', 'dog', 'cog']
    # ['hit', 'hot', 'lot', 'log', 'cog']
    
    # 测试用例2
    beginWord2 = "hit"
    endWord2 = "cog"
    wordList2 = ["hot","dot","dog","lot","log"]
    result2 = findLadders(beginWord2, endWord2, wordList2)
    print("\n测试用例2结果:", result2)  # 预期输出: []