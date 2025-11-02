# 最小覆盖子串（变种滑动窗口）
# 给你一个字符串 s 、一个字符串 t 。返回 s 中涵盖 t 所有字符的最小子串。
# 如果 s 中不存在涵盖 t 所有字符的子串，则返回空字符串 "" 。
# 注意：
# 对于 t 中重复字符，我们寻找的子字符串中该字符数量必须不少于 t 中该字符数量。
# 如果 s 中存在这样的子串，我们保证它是唯一的答案。
# 测试链接：https://leetcode.cn/problems/minimum-window-substring/
#
# 题目解析：
# 这是滑动窗口的经典变种，虽然不是直接使用单调队列，但可以使用单调队列的思想来优化。
# 我们可以用计数方式模拟单调队列的行为。
#
# 算法思路：
# 1. 使用滑动窗口技术
# 2. 用两个指针维护窗口的左右边界
# 3. 用哈希表记录字符频次
# 4. 扩展右边界直到窗口包含所有需要的字符
# 5. 收缩左边界直到不再满足条件，过程中记录最小窗口
#
# 时间复杂度：O(n) - 其中n是字符串s的长度
# 空间复杂度：O(k) - 其中k是字符串t中不同字符的个数

class Solution:
    def minWindow(self, s: str, t: str) -> str:
        """
        寻找最小覆盖子串
        :param s: 源字符串
        :param t: 目标字符串
        :return: s中包含t所有字符的最小子串
        """
        if not s or not t:
            return ""
        
        # 用字典记录需要的字符频次和窗口中的字符频次
        need = {}  # 需要的字符频次
        window = {}  # 窗口中的字符频次
        
        # 统计目标字符串中每个字符的频次
        for c in t:
            need[c] = need.get(c, 0) + 1
        
        left, right = 0, 0  # 滑动窗口的左右边界
        valid = 0  # 窗口中满足need条件的字符个数
        start, length = 0, float('inf')  # 记录最小覆盖子串的起始位置和长度
        
        while right < len(s):
            # c是将要移入窗口的字符
            c = s[right]
            # 右移窗口
            right += 1
            
            # 进行窗口内数据的一系列更新
            if c in need:
                window[c] = window.get(c, 0) + 1
                # 当window[c]等于need[c]时，说明字符c在窗口中的数量已经满足需求
                if window[c] == need[c]:
                    valid += 1
            
            # 判断左侧窗口是否要收缩
            while valid == len(need):
                # 在这里更新最小覆盖子串
                if right - left < length:
                    start = left
                    length = right - left
                
                # d是将要移出窗口的字符
                d = s[left]
                # 左移窗口
                left += 1
                
                # 进行窗口内数据的一系列更新
                if d in need:
                    if window[d] == need[d]:
                        valid -= 1
                    window[d] -= 1
        
        # 返回最小覆盖子串
        return "" if length == float('inf') else s[start:start + length]