# Word Ladder (LeetCode 127)
# 题目来源：LeetCode
# 题目描述：
# 给定两个单词（beginWord 和 endWord）和一个字典 wordList，找出从 beginWord 到 endWord 的最短转换序列的长度。
# 转换需遵循如下规则：
# 1. 每次转换只能改变一个字母。
# 2. 转换后的单词必须是字典中的单词。
# 3. 如果不存在这样的转换序列，返回 0。
# 测试链接：https://leetcode.com/problems/word-ladder/
# 
# 算法思路：
# 使用双向BFS算法，从起点和终点同时开始搜索，当两个搜索前沿相遇时，找到最短路径
# 时间复杂度：O(M*N^2)，其中M是单词长度，N是字典大小
# 空间复杂度：O(N)
# 
# 工程化考量：
# 1. 预处理：将单词按模式分组，提高生成邻接节点的效率
# 2. 优化：始终从较小的集合开始扩展，减少搜索空间
# 3. 边界检查：处理特殊情况（如endWord不在字典中）
# 
# 语言特性差异：
# Python中使用set存储访问过的节点，使用dict存储单词模式映射

from typing import List

def ladderLength(beginWord: str, endWord: str, wordList: List[str]) -> int:
    """
    计算从beginWord到endWord的最短转换序列长度
    
    Args:
        beginWord: 起始单词
        endWord: 目标单词
        wordList: 单词列表
    
    Returns:
        最短转换序列的长度，如果不存在返回0
    """
    # 边界条件检查
    if endWord not in wordList:
        return 0  # 如果endWord不在字典中，无法转换
    
    # 将wordList转换为set，提高查找效率
    wordSet = set(wordList)
    
    # 创建双向BFS所需的集合
    beginSet = {beginWord}
    endSet = {endWord}
    visited = set()
    
    # 初始化
    length = 1  # 初始长度为1（包含beginWord）
    
    # 开始双向BFS
    while beginSet and endSet:
        # 优化：始终从较小的集合开始扩展，减少搜索空间
        if len(beginSet) > len(endSet):
            # 交换beginSet和endSet
            beginSet, endSet = endSet, beginSet
        
        # 存储当前层的下一层节点
        nextLevel = set()
        
        # 遍历当前层的所有节点
        for word in beginSet:
            # 生成所有可能的转换
            for i in range(len(word)):
                # 尝试将当前字符替换为其他25个小写字母
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    if c == word[i]:
                        continue
                    
                    newWord = word[:i] + c + word[i+1:]
                    
                    # 如果在另一个集合中找到，则找到了路径
                    if newWord in endSet:
                        return length + 1
                    
                    # 如果单词在字典中且未被访问过，则加入下一层
                    if newWord in wordSet and newWord not in visited:
                        nextLevel.add(newWord)
                        visited.add(newWord)
        
        # 更新当前层
        beginSet = nextLevel
        length += 1
    
    # 如果两个集合不再相交，表示没有找到路径
    return 0

def ladderLengthOptimized(beginWord: str, endWord: str, wordList: List[str]) -> int:
    """
    优化版本：使用单词模式映射进行优化
    
    Args:
        beginWord: 起始单词
        endWord: 目标单词
        wordList: 单词列表
    
    Returns:
        最短转换序列的长度，如果不存在返回0
    """
    # 边界条件检查
    if endWord not in wordList:
        return 0
    
    L = len(beginWord)
    
    # 预处理：将单词按模式分组，例如：h*t -> [hot, hit, hat...]
    from collections import defaultdict
    patternToWords = defaultdict(list)
    for word in wordList:
        for i in range(L):
            pattern = word[:i] + '*' + word[i+1:]
            patternToWords[pattern].append(word)
    
    # 添加beginWord到模式映射
    for i in range(L):
        pattern = beginWord[:i] + '*' + beginWord[i+1:]
        if pattern not in patternToWords:
            patternToWords[pattern] = []
    
    # 双向BFS
    beginSet = {beginWord}
    endSet = {endWord}
    visited = set()
    
    length = 1
    
    while beginSet and endSet:
        if len(beginSet) > len(endSet):
            beginSet, endSet = endSet, beginSet
        
        nextLevel = set()
        
        for word in beginSet:
            for i in range(L):
                pattern = word[:i] + '*' + word[i+1:]
                
                # 获取所有匹配该模式的单词
                for neighbor in patternToWords.get(pattern, []):
                    if neighbor in endSet:
                        return length + 1
                    
                    if neighbor not in visited:
                        nextLevel.add(neighbor)
                        visited.add(neighbor)
        
        beginSet = nextLevel
        length += 1
    
    return 0

# 测试方法
def main():
    # 测试用例1
    beginWord1 = "hit"
    endWord1 = "cog"
    wordList1 = ["hot", "dot", "dog", "lot", "log", "cog"]
    print("测试用例1：")
    print(f"beginWord: {beginWord1}, endWord: {endWord1}, wordList: {wordList1}")
    print("期望输出：5")  # hit -> hot -> dot -> dog -> cog
    print(f"实际输出（普通版）：{ladderLength(beginWord1, endWord1, wordList1)}")
    print(f"实际输出（优化版）：{ladderLengthOptimized(beginWord1, endWord1, wordList1)}")
    
    # 测试用例2
    beginWord2 = "hit"
    endWord2 = "cog"
    wordList2 = ["hot", "dot", "dog", "lot", "log"]
    print("\n测试用例2：")
    print(f"beginWord: {beginWord2}, endWord: {endWord2}, wordList: {wordList2}")
    print("期望输出：0")  # endWord不在wordList中
    print(f"实际输出（普通版）：{ladderLength(beginWord2, endWord2, wordList2)}")
    print(f"实际输出（优化版）：{ladderLengthOptimized(beginWord2, endWord2, wordList2)}")
    
    # 测试用例3
    beginWord3 = "a"
    endWord3 = "c"
    wordList3 = ["a", "b", "c"]
    print("\n测试用例3：")
    print(f"beginWord: {beginWord3}, endWord: {endWord3}, wordList: {wordList3}")
    print("期望输出：2")  # a -> c
    print(f"实际输出（普通版）：{ladderLength(beginWord3, endWord3, wordList3)}")
    print(f"实际输出（优化版）：{ladderLengthOptimized(beginWord3, endWord3, wordList3)}")

if __name__ == "__main__":
    main()