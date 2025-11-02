# LeetCode 1178. 猜字谜
# 外国友人仿照中国字谜设计了一个英文版猜字谜小游戏，请你来猜猜看吧。
# 字谜的迷面puzzle 按字符串形式给出，如果一个单词word 符合下面两个条件，那么它就可以算作谜底：
# 1. 单词中包含谜面的第一个字母
# 2. 单词中的每一个字母都出现在谜面中
# 测试链接 : https://leetcode.cn/problems/number-of-valid-words-for-each-puzzle/

"""
算法详解：猜字谜（LeetCode 1178）

问题描述：
给定一个单词列表words和一个谜面列表puzzles，对于每个谜面，计算有多少个单词可以作为谜底。
单词可以作为谜底需要满足两个条件：
1. 单词中包含谜面的第一个字母
2. 单词中的每一个字母都出现在谜面中

算法思路：
使用状态压缩和位运算优化来解决这个问题。
1. 将每个单词转换为位掩码表示
2. 将每个谜面转换为位掩码表示
3. 对于每个谜面，枚举其所有子集（使用位运算技巧）
4. 检查子集是否包含谜面的第一个字母
5. 统计满足条件的单词数量

时间复杂度分析：
1. 单词位掩码转换：O(W*L)，其中W是单词数，L是平均单词长度
2. 谜面处理：O(P * 2^N)，其中P是谜面数，N是谜面长度（最多7个字符）
3. 总体时间复杂度：O(W*L + P * 2^N)

空间复杂度分析：
1. 单词位掩码存储：O(W)
2. 位掩码计数：O(2^26)（实际远小于，因为只存储出现的位掩码）
3. 总体空间复杂度：O(W + 2^26)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 边界处理：正确处理空列表的情况
3. 性能优化：使用字典统计位掩码出现次数，避免重复计算
4. 位运算优化：使用技巧优化子集枚举

极端场景验证：
1. 单词列表和谜面列表为空的情况
2. 单词长度和谜面长度达到边界的情况
3. 所有单词都能匹配所有谜面的情况
4. 没有单词能匹配任何谜面的情况
"""

def findNumOfValidWords(words, puzzles):
    """
    计算每个谜面有多少个单词可以作为谜底
    
    Args:
        words (List[str]): 单词列表
        puzzles (List[str]): 谜面列表
    
    Returns:
        List[int]: 每个谜面对应的匹配单词数量
    """
    # 异常处理：检查输入参数的有效性
    if not words or not puzzles:
        return []
    
    # 使用字典统计每个位掩码出现的次数
    # key: 单词的位掩码, value: 该位掩码出现的次数
    word_count = {}
    
    # 将每个单词转换为位掩码并统计
    for word in words:
        mask = 0
        for ch in word:
            # 将每个字母对应到一个位上
            mask |= 1 << (ord(ch) - ord('a'))
        # 统计该位掩码出现的次数
        word_count[mask] = word_count.get(mask, 0) + 1
    
    result = []
    
    # 处理每个谜面
    for puzzle in puzzles:
        count = 0
        
        # 获取谜面的第一个字母对应的位
        first_letter = 1 << (ord(puzzle[0]) - ord('a'))
        
        # 获取谜面的位掩码
        puzzle_mask = 0
        for ch in puzzle:
            puzzle_mask |= 1 << (ord(ch) - ord('a'))
        
        # 枚举谜面的所有子集
        # 使用技巧：对于一个掩码mask，其所有子集可以通过以下方式枚举：
        # subset = (subset - 1) & mask
        subset = puzzle_mask
        while subset > 0:
            # 检查子集是否包含谜面的第一个字母
            if (subset & first_letter) != 0:
                # 如果包含，则统计对应的单词数量
                count += word_count.get(subset, 0)
            # 枚举下一个子集
            subset = (subset - 1) & puzzle_mask
        
        result.append(count)
    
    return result

# 优化版本：使用collections.Counter提高性能
from collections import Counter

def findNumOfValidWordsOptimized(words, puzzles):
    """
    计算每个谜面有多少个单词可以作为谜底（优化版本）
    
    Args:
        words (List[str]): 单词列表
        puzzles (List[str]): 谜面列表
    
    Returns:
        List[int]: 每个谜面对应的匹配单词数量
    """
    # 异常处理：检查输入参数的有效性
    if not words or not puzzles:
        return []
    
    # 使用Counter统计每个位掩码出现的次数
    word_count = Counter()
    
    # 将每个单词转换为位掩码并统计
    for word in words:
        mask = 0
        for ch in word:
            # 将每个字母对应到一个位上
            mask |= 1 << (ord(ch) - ord('a'))
        # 统计该位掩码出现的次数
        word_count[mask] += 1
    
    result = []
    
    # 处理每个谜面
    for puzzle in puzzles:
        count = 0
        
        # 获取谜面的第一个字母对应的位
        first_letter = 1 << (ord(puzzle[0]) - ord('a'))
        
        # 获取谜面的位掩码
        puzzle_mask = 0
        for ch in puzzle:
            puzzle_mask |= 1 << (ord(ch) - ord('a'))
        
        # 枚举谜面的所有子集
        subset = puzzle_mask
        while subset > 0:
            # 检查子集是否包含谜面的第一个字母
            if (subset & first_letter) != 0:
                # 如果包含，则统计对应的单词数量
                count += word_count[subset]
            # 枚举下一个子集
            subset = (subset - 1) & puzzle_mask
        
        result.append(count)
    
    return result

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    words1 = ["aaaa","asas","able","ability","actt","actor","access"]
    puzzles1 = ["aboveyz","abrodyz","abslute","absoryz","actresz","gaswxyz"]
    print(f"Test 1: {findNumOfValidWords(words1, puzzles1)}")
    # 期望输出: [1,1,3,2,4,0]
    
    # 测试用例2
    words2 = ["apple","pleas","please"]
    puzzles2 = ["aelwxyz","aelpxyz","aelpsxy","saelpxy","xaelpsy"]
    print(f"Test 2: {findNumOfValidWords(words2, puzzles2)}")
    # 期望输出: [0,1,3,2,0]
    
    # 测试用例3
    words3 = []
    puzzles3 = ["aboveyz"]
    print(f"Test 3: {findNumOfValidWords(words3, puzzles3)}")
    # 期望输出: []
    
    # 测试用例4
    words4 = ["abc"]
    puzzles4 = []
    print(f"Test 4: {findNumOfValidWords(words4, puzzles4)}")
    # 期望输出: []