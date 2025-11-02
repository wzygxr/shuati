from collections import OrderedDict
from typing import Optional

# 含有嵌套的分子式求原子数量
# 测试链接 : https://leetcode.cn/problems/number-of-atoms/
#
# 相关题目:
# 1. LeetCode 394. Decode String (字符串解码)
#    链接: https://leetcode.cn/problems/decode-string/
#    区别: 解码字符串而不是统计原子数量
#
# 2. LeetCode 772. Basic Calculator III (基本计算器 III)
#    链接: https://leetcode.cn/problems/basic-calculator-iii/
#    区别: 计算表达式而不是统计原子数量
#
# 3. LeetCode 856. Score of Parentheses (括号的分数)
#    链接: https://leetcode.cn/problems/score-of-parentheses/
#    区别: 计算括号的分数而不是统计原子数量
#
# 解题思路:
# 使用递归处理嵌套结构，遇到左括号时递归处理括号内的化学式
# 用全局变量where记录当前处理到的位置，用于递归返回时告知上游函数从哪继续处理
# 用OrderedDict存储原子名称和对应的数量，保证输出时按字典序排列
#
# 时间复杂度: O(n)，其中n是字符串的长度
# 空间复杂度: O(n)，递归调用栈的深度和存储原子数量的额外空间

class Solution:
    def __init__(self):
        self.where = 0
    
    def countOfAtoms(self, formula: str) -> str:
        self.where = 0
        atom_map = self.f(formula, 0)
        ans = ""
        for key in atom_map:
            ans += key
            cnt = atom_map[key]
            if cnt > 1:
                ans += str(cnt)
        return ans
    
    # s[i....]开始计算，遇到字符串终止 或者 遇到 ) 停止
    # 返回 : 自己负责的这一段字符串的结果，有序表！
    # 返回之间，更新全局变量where，为了上游函数知道从哪继续！
    def f(self, s: str, i: int) -> OrderedDict:
        # ans是总表，存储原子名称和对应的数量
        ans = OrderedDict()
        # 之前收集到的名字，历史一部分
        name = ""
        # 之前收集到的有序表，历史一部分
        pre: Optional[OrderedDict] = None
        # 历史翻几倍
        cnt = 0
        while i < len(s) and s[i] != ')':
            if ('A' <= s[i] <= 'Z') or s[i] == '(':
                self.fill(ans, name, pre, cnt)
                name = ""
                pre = None
                cnt = 0
                if 'A' <= s[i] <= 'Z':
                    name += s[i]
                    i += 1
                else:
                    # 遇到 (
                    pre = self.f(s, i + 1)
                    i = self.where + 1
            elif 'a' <= s[i] <= 'z':
                name += s[i]
                i += 1
            else:
                cnt = cnt * 10 + int(s[i])
                i += 1
        self.fill(ans, name, pre, cnt)
        self.where = i
        return ans
    
    # 将收集到的原子信息填充到结果中
    def fill(self, ans: OrderedDict, name: str, pre: Optional[OrderedDict], cnt: int) -> None:
        if len(name) > 0 or pre is not None:
            cnt = 1 if cnt == 0 else cnt
            if len(name) > 0:
                if name in ans:
                    ans[name] += cnt
                else:
                    ans[name] = cnt
            else:
                if pre is not None:
                    for key in pre:
                        if key in ans:
                            ans[key] += pre[key] * cnt
                        else:
                            ans[key] = pre[key] * cnt

# 测试函数
"""
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1: "H2O"
    print("Test 1: H2O =", solution.countOfAtoms("H2O"))
    
    # 测试用例2: "Mg(OH)2"
    print("Test 2: Mg(OH)2 =", solution.countOfAtoms("Mg(OH)2"))
    
    # 测试用例3: "K4(ON(SO3)2)2"
    print("Test 3: K4(ON(SO3)2)2 =", solution.countOfAtoms("K4(ON(SO3)2)2"))
"""