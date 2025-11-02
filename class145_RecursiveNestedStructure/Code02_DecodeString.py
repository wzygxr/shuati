# 含有嵌套的字符串解码
# 测试链接 : https://leetcode.cn/problems/decode-string/
#
# 相关题目:
# 1. LeetCode 726. Number of Atoms (原子的数量)
#    链接: https://leetcode.cn/problems/number-of-atoms/
#    区别: 处理化学式中的原子计数，结构类似但需要统计不同原子的数量
#
# 2. LeetCode 856. Score of Parentheses (括号的分数)
#    链接: https://leetcode.cn/problems/score-of-parentheses/
#    区别: 计算括号的分数，((())())这种结构的计算
#
# 3. LeetCode 385. Mini Parser (迷你语法分析器)
#    链接: https://leetcode.cn/problems/mini-parser/
#    区别: 解析嵌套的整数列表结构
#
# 解题思路:
# 使用递归处理嵌套结构，遇到左括号时递归处理括号内的字符串
# 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
# 数字表示后续字符串的重复次数
#
# 时间复杂度: O(n)，其中n是输出字符串的长度
# 空间复杂度: O(n)，递归调用栈的深度

class Solution:
    def __init__(self):
        self.where = 0
    
    def decodeString(self, s: str) -> str:
        self.where = 0
        return self.f(s, 0)
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到 ] 停止
    # 返回 : 自己负责的这一段字符串的结果
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: str, i: int) -> str:
        path = ""
        cnt = 0
        while i < len(s) and s[i] != ']':
            if ('a' <= s[i] <= 'z') or ('A' <= s[i] <= 'Z'):
                path += s[i]
                i += 1
            elif '0' <= s[i] <= '9':
                cnt = cnt * 10 + int(s[i])
                i += 1
            else:
                # 遇到 [ 
                # cnt = 7 * ? 
                path += self.get(cnt, self.f(s, i + 1))
                i = self.where + 1
                cnt = 0
        self.where = i
        return path
    
    # 将字符串重复指定次数
    def get(self, cnt: int, s: str) -> str:
        builder = ""
        for i in range(cnt):
            builder += s
        return builder

# 测试函数
"""
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: "3[a]2[bc]"
    print("Test 1: 3[a]2[bc] =", solution.decodeString("3[a]2[bc]"))
    
    # 测试用例2: "3[a2[c]]"
    print("Test 2: 3[a2[c]] =", solution.decodeString("3[a2[c]]"))
    
    # 测试用例3: "2[abc]3[cd]ef"
    print("Test 3: 2[abc]3[cd]ef =", solution.decodeString("2[abc]3[cd]ef"))
    
    # 测试用例4: "abc3[cd]xyz"
    print("Test 4: abc3[cd]xyz =", solution.decodeString("abc3[cd]xyz"))
"""