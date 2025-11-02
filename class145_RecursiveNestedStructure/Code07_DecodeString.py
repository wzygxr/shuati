# LeetCode 394. Decode String (字符串解码)
# 测试链接 : https://leetcode.cn/problems/decode-string/
#
# 题目描述:
# 给定一个经过编码的字符串，返回它解码后的字符串。
# 编码规则为: k[encoded_string]，表示其中方括号内部的 encoded_string 正好重复 k 次。
# 注意 k 保证为正整数。
# 你可以认为输入字符串总是有效的；输入字符串中没有额外的空格，
# 且输入的方括号总是符合格式要求的。
# 此外，你可以认为原始数据不包含数字，所有的数字只表示重复的次数 k ，
# 例如不会出现像 3a 或 2[4] 的输入。
#
# 示例:
# 输入：s = "3[a]2[bc]"
# 输出："aaabcbc"
#
# 输入：s = "3[a2[c]]"
# 输出："accaccacc"
#
# 输入：s = "2[abc]3[cd]ef"
# 输出："abcabccdcdcdef"
#
# 输入：s = "abc3[cd]xyz"
# 输出："abccdcdcdxyz"
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
        return self.f(list(s), 0)
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到 ] 停止
    # 返回 : 自己负责的这一段字符串的结果
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: list, i: int) -> str:
        path = []
        cnt = 0
        while i < len(s) and s[i] != ']':
            if s[i].isalpha():
                path.append(s[i])
                i += 1
            elif s[i].isdigit():
                cnt = cnt * 10 + int(s[i])
                i += 1
            else:
                # 遇到 [ 
                # cnt = 7 * ? 
                path.append(self.get(cnt, self.f(s, i + 1)))
                i = self.where + 1
                cnt = 0
        self.where = i
        return ''.join(path)
    
    # 将字符串重复指定次数
    def get(self, cnt: int, s: str) -> str:
        return s * cnt

# 测试用例
def main():
    solution = Solution()
    
    # 测试用例1
    s1 = "3[a]2[bc]"
    print(f"输入: {s1}")
    print(f"输出: {solution.decodeString(s1)}")
    print(f"期望: aaabcbc\n")
    
    # 测试用例2
    s2 = "3[a2[c]]"
    print(f"输入: {s2}")
    print(f"输出: {solution.decodeString(s2)}")
    print(f"期望: accaccacc\n")
    
    # 测试用例3
    s3 = "2[abc]3[cd]ef"
    print(f"输入: {s3}")
    print(f"输出: {solution.decodeString(s3)}")
    print(f"期望: abcabccdcdcdef\n")
    
    # 测试用例4
    s4 = "abc3[cd]xyz"
    print(f"输入: {s4}")
    print(f"输出: {solution.decodeString(s4)}")
    print(f"期望: abccdcdcdxyz\n")

if __name__ == "__main__":
    main()