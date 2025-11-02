"""
最长字符串链

题目来源：LeetCode 1048. 最长字符串链
题目链接：https://leetcode.cn/problems/longest-string-chain/
题目描述：给出一个单词数组 words ，其中每个单词都由小写英文字母组成。
如果我们可以不改变其他字符的顺序，在 wordA 中任意位置添加恰好一个字母使其变成 wordB，
那么我们认为 wordA 是 wordB 的前身。
词链是单词 [word_1, word_2, ..., word_k] 组成的序列，其中 word1 是 word2 的前身，
word2 是 word3 的前身，依此类推。从给定单词列表 words 中选择单词组成词链，
返回词链的最长可能长度。

算法思路：
1. 按字符串长度排序
2. 使用动态规划方法
3. dp[word] 表示以 word 结尾的最长字符串链长度
4. 对于每个单词，尝试删除每个字符，检查是否存在于之前的单词中
5. 如果存在，则更新当前单词的最长链长度

时间复杂度：O(N * L^2) - N是单词数量，L是单词平均长度
空间复杂度：O(N * L) - 需要哈希表存储状态和单词
是否最优解：这是目前较优的解法

示例：
输入：words = ["a","b","ba","bca","bda","bdca"]
输出：4
解释：最长单词链之一为 ["a","ba","bda","bdca"]

输入：words = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
输出：5

输入：words = ["abcd","dbqca"]
输出：1
"""


def longestStrChain(words):
    """
    计算最长字符串链的长度
    
    Args:
        words: 单词数组
        
    Returns:
        最长字符串链的长度
    """
    # 按字符串长度排序
    words.sort(key=lambda x: len(x))
    
    # dp[word] 表示以 word 结尾的最长字符串链长度
    dp = {}
    maxLen = 1
    
    # 遍历每个单词
    for word in words:
        best = 0
        # 尝试删除每个字符，检查是否存在于之前的单词中
        for i in range(len(word)):
            # 删除第i个字符
            prev = word[:i] + word[i+1:]
            # 如果存在，则更新当前单词的最长链长度
            best = max(best, dp.get(prev, 0) + 1)
        dp[word] = best
        maxLen = max(maxLen, best)
    
    return maxLen


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    words1 = ["a","b","ba","bca","bda","bdca"]
    print("输入: [\"a\",\"b\",\"ba\",\"bca\",\"bda\",\"bdca\"]")
    print("输出: ", longestStrChain(words1))
    print("期望: 4")
    print()
    
    # 测试用例2
    words2 = ["xbc","pcxbcf","xb","cxbc","pcxbc"]
    print("输入: [\"xbc\",\"pcxbcf\",\"xb\",\"cxbc\",\"pcxbc\"]")
    print("输出: ", longestStrChain(words2))
    print("期望: 5")
    print()
    
    # 测试用例3
    words3 = ["abcd","dbqca"]
    print("输入: [\"abcd\",\"dbqca\"]")
    print("输出: ", longestStrChain(words3))
    print("期望: 1")