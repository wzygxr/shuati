# 单词接龙
# 字典 wordList 中从单词 beginWord 和 endWord 的 转换序列
# 是一个按下述规格形成的序列 beginWord -> s1 -> s2 -> ... -> sk ：
# 每一对相邻的单词只差一个字母。
# 对于 1 <= i <= k 时，每个 si 都在 wordList 中
# 注意， beginWord 不需要在 wordList 中。sk == endWord
# 给你两个单词 beginWord 和 endWord 和一个字典 wordList
# 返回 从 beginWord 到 endWord 的 最短转换序列 中的 单词数目
# 如果不存在这样的转换序列，返回 0 。
# 测试链接 : https://leetcode.cn/problems/word-ladder/
# 
# 算法思路：
# 使用双向BFS算法，从起点和终点同时开始搜索，一旦两个搜索相遇，就找到了最短路径
# 时间复杂度：O(M^2 * N)，其中M是单词的长度，N是单词列表中的单词数量
# 空间复杂度：O(N * M)
# 
# 工程化考量：
# 1. 异常处理：检查endWord是否在wordList中
# 2. 性能优化：使用双向BFS减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用set进行快速查找，使用list进行队列操作

from typing import List

def ladderLength(beginWord: str, endWord: str, wordList: List[str]) -> int:
    """
    计算从beginWord到endWord的最短转换序列中的单词数目
    
    Args:
        beginWord: 起始单词
        endWord: 目标单词
        wordList: 单词列表
    
    Returns:
        最短转换序列中的单词数目，如果不存在则返回0
    """
    # 将wordList转换为set以提高查找效率
    wordSet = set(wordList)
    
    # 如果目标单词不在词典中，直接返回0
    if endWord not in wordSet:
        return 0
    
    # 初始化双向BFS的集合
    smallLevel = {beginWord}  # 数量较少的一侧
    bigLevel = {endWord}      # 数量较多的一侧
    nextLevel = set()         # 下一层扩展的节点
    
    # len记录路径长度，初始为2（包含begin和end）
    length = 2
    
    while smallLevel:
        # 从小数量的一侧开始扩展
        for word in smallLevel:
            # 尝试改变每个位置的字符
            for i in range(len(word)):
                # 尝试26个字母
                for c in 'abcdefghijklmnopqrstuvwxyz':
                    # 如果不是原字符
                    if c != word[i]:
                        # 生成新单词
                        newWord = word[:i] + c + word[i+1:]
                        # 如果在大侧找到了，说明两路相遇，返回路径长度
                        if newWord in bigLevel:
                            return length
                        # 如果在词典中找到了，加入下一层并从词典中移除
                        if newWord in wordSet:
                            wordSet.remove(newWord)
                            nextLevel.add(newWord)
        
        # 优化：始终从小的一侧开始扩展
        if len(nextLevel) <= len(bigLevel):
            smallLevel, nextLevel = nextLevel, smallLevel
        else:
            smallLevel, bigLevel, nextLevel = bigLevel, nextLevel, smallLevel
        
        # 清空nextLevel，为下一轮扩展做准备
        nextLevel.clear()
        length += 1
    
    return 0

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    wordList1 = ["hot", "dot", "dog", "lot", "log", "cog"]
    print("测试用例1:")
    print("beginWord: hit, endWord: cog")
    print("wordList: [hot, dot, dog, lot, log, cog]")
    print("期望输出: 5")
    print("实际输出:", ladderLength("hit", "cog", wordList1))
    print()
    
    # 测试用例2
    wordList2 = ["hot", "dot", "dog", "lot", "log"]
    print("测试用例2:")
    print("beginWord: hit, endWord: cog")
    print("wordList: [hot, dot, dog, lot, log]")
    print("期望输出: 0")
    print("实际输出:", ladderLength("hit", "cog", wordList2))