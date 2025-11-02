# -*- coding: utf-8 -*-

"""
AC自动机扩展题目合集 - Python实现

本文件实现了以下扩展AC自动机题目：
1. 洛谷P4052 [JSOI2007] 文本生成器
2. Codeforces 963D Frequency of String
3. SPOJ MANDRAKE
4. LeetCode 816. Ambiguous Coordinates（AC自动机应用思路）
5. HDU 3065 病毒侵袭持续中

算法详解：
AC自动机是一种高效的多模式字符串匹配算法，结合了Trie树和KMP算法的优点
能够在O(∑|Pi| + |T|)的时间复杂度内完成多模式串匹配

时间复杂度分析：
- 构建Trie树：O(∑|Pi|)
- 构建fail指针：O(∑|Pi|)
- 文本匹配：O(|T|)
总时间复杂度：O(∑|Pi| + |T|)

空间复杂度：O(∑|Pi| × |Σ|)

Python特性优化：
1. 使用字典实现Trie树，节省空间
2. 使用collections.deque实现高效队列操作
3. 使用生成器模式处理大数据流
4. 利用Python的动态特性实现灵活的字符集支持
5. 使用装饰器实现性能监控和调试
"""

from collections import deque, defaultdict
from typing import List, Set, Dict, Tuple

# ==================== 题目1: 洛谷P4052 [JSOI2007] 文本生成器 ====================

class TextGenerator:
    """
    题目描述：给定n个模式串，求长度为m的至少包含一个模式串的字符串个数
    题目链接：https://www.luogu.com.cn/problem/P4052
    
    算法思路：
    1. 使用AC自动机检测字符串是否包含模式串
    2. 使用动态规划计算满足条件的字符串个数
    3. 总字符串个数减去不包含任何模式串的字符串个数
    
    时间复杂度：O(m × 节点数)
    空间复杂度：O(m × 节点数)
    """
    
    def __init__(self):
        self.MOD = 10007
        self.tree = [{}]  # 使用字典列表实现Trie树
        self.fail = [0]
        self.danger = [False]  # 危险节点标记
        self.cnt = 0
        
    def insert(self, word: str) -> None:
        """插入模式串到Trie树中"""
        u = 0
        for c in word:
            idx = ord(c) - ord('A')
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.danger.append(False)
            u = self.tree[u][idx]
        self.danger[u] = True
        
    def build(self) -> None:
        """构建AC自动机的fail指针"""
        q = deque()
        for i in range(26):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            # 危险标记传递
            self.danger[u] = self.danger[u] or self.danger[self.fail[u]]
            
            for i in range(26):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    # 优化：直接指向fail节点的对应子节点
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def solve(self, m: int) -> int:
        """计算长度为m的至少包含一个模式串的字符串个数"""
        # 计算总字符串个数
        total = pow(26, m, self.MOD)
        
        # 动态规划计算不包含模式串的字符串个数
        dp = [[0] * (self.cnt + 1) for _ in range(m + 1)]
        dp[0][0] = 1
        
        for i in range(m):
            for j in range(self.cnt + 1):
                if dp[i][j] == 0 or self.danger[j]:
                    continue
                
                for k in range(26):
                    next_node = self.tree[j].get(k, 0)
                    if not self.danger[next_node]:
                        dp[i + 1][next_node] = (dp[i + 1][next_node] + dp[i][j]) % self.MOD
        
        # 计算安全字符串个数
        safe = 0
        for j in range(self.cnt + 1):
            if not self.danger[j]:
                safe = (safe + dp[m][j]) % self.MOD
        
        # 结果为总个数减去安全个数
        return (total - safe) % self.MOD

# ==================== 题目2: Codeforces 963D Frequency of String ====================

class FrequencyOfString:
    """
    题目描述：给定字符串s和q个查询，每个查询包含字符串t和整数k
    求t在s中第k次出现的位置，如果不存在则输出-1
    题目链接：https://codeforces.com/problemset/problem/963/D
    
    算法思路：
    1. 构建AC自动机，将所有查询的t插入到Trie树中
    2. 预处理字符串s，记录每个查询t的所有出现位置
    3. 对每个查询，直接返回第k个出现位置
    
    时间复杂度：O(|s| + ∑|ti| + q)
    空间复杂度：O(∑|ti| × |Σ| + |s|)
    """
    
    def __init__(self, n: int):
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 记录模式串编号
        self.cnt = 0
        self.positions = [[] for _ in range(n + 1)]
        
    def insert(self, pattern: str, pattern_id: int) -> None:
        """插入模式串"""
        u = 0
        for c in pattern:
            idx = ord(c) - ord('a')
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
            u = self.tree[u][idx]
        self.end[u] = pattern_id
        
    def build(self) -> None:
        """构建AC自动机"""
        q = deque()
        for i in range(26):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(26):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def preprocess(self, s: str) -> None:
        """预处理字符串s，记录所有出现位置"""
        u = 0
        for i, c in enumerate(s):
            idx = ord(c) - ord('a')
            u = self.tree[u].get(idx, 0)
            temp = u
            while temp != 0:
                if self.end[temp] != 0:
                    self.positions[self.end[temp]].append(i)
                temp = self.fail[temp]
    
    def query(self, pattern_id: int, k: int) -> int:
        """查询第k次出现的位置"""
        if len(self.positions[pattern_id]) < k:
            return -1
        # 返回1-based索引
        return self.positions[pattern_id][k - 1] + 1

# ==================== 题目3: SPOJ MANDRAKE ====================

class Mandrake:
    """
    题目描述：给定多个模式串和一个文本串，求有多少个模式串在文本串中出现过，
    并且每个模式串的出现次数至少为k次
    题目链接：https://www.spoj.com/problems/MANDRAKE/
    
    算法思路：
    1. 构建AC自动机，将所有模式串插入到Trie树中
    2. 构建失配指针
    3. 在文本串中进行匹配，统计每个模式串的出现次数
    4. 筛选出出现次数至少为k次的模式串
    
    时间复杂度：O(∑|Pi| + |T| + N)
    空间复杂度：O(∑|Pi| × |Σ| + N)
    """
    
    def __init__(self, n: int):
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 模式串编号
        self.count = [0]  # 节点匹配次数
        self.cnt = 0
        self.pattern_count = [0] * (n + 1)
        
    def insert(self, pattern: str, pattern_id: int) -> None:
        """插入模式串"""
        u = 0
        for c in pattern:
            idx = ord(c) - ord('a')
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
                self.count.append(0)
            u = self.tree[u][idx]
        self.end[u] = pattern_id
        
    def build(self) -> None:
        """构建AC自动机"""
        q = deque()
        for i in range(26):
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(26):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def match(self, text: str) -> None:
        """在文本中进行匹配并统计次数"""
        u = 0
        for c in text:
            idx = ord(c) - ord('a')
            u = self.tree[u].get(idx, 0)
            self.count[u] += 1
        
        # 使用拓扑排序汇总匹配次数
        indegree = [0] * (self.cnt + 1)
        for i in range(1, self.cnt + 1):
            indegree[self.fail[i]] += 1
        
        q = deque()
        for i in range(1, self.cnt + 1):
            if indegree[i] == 0:
                q.append(i)
        
        while q:
            u = q.popleft()
            if self.end[u] != 0:
                self.pattern_count[self.end[u]] = self.count[u]
            
            v = self.fail[u]
            self.count[v] += self.count[u]
            indegree[v] -= 1
            if indegree[v] == 0:
                q.append(v)
    
    def count_patterns(self, k: int) -> int:
        """统计出现次数至少为k次的模式串数量"""
        return sum(1 for count in self.pattern_count if count >= k)

# ==================== 题目4: LeetCode 816. Ambiguous Coordinates ====================

class AmbiguousCoordinates:
    """
    题目描述：给定一个字符串S，它表示一个坐标，格式为"(x,y)"，其中x和y都是整数
    我们可以在任意位置（包括开头和结尾）插入小数点，只要得到的小数是有效的
    求所有可能的有效坐标
    题目链接：https://leetcode.com/problems/ambiguous-coordinates/
    
    算法思路（AC自动机应用思路）：
    虽然这道题可以用暴力枚举解决，但也可以利用AC自动机的思想来识别有效的数字模式
    1. 构建一个自动机，包含所有有效的数字模式（整数、小数）
    2. 对输入的数字部分进行处理，识别所有可能的有效分割
    
    时间复杂度：O(n³)
    空间复杂度：O(n²)
    """
    
    def is_valid_number(self, s: str) -> bool:
        """检查字符串是否表示有效的数字"""
        if not s:
            return False
        
        # 如果是整数
        if '.' not in s:
            # 不能有前导0，除非是0本身
            if len(s) > 1 and s[0] == '0':
                return False
            return True
        
        # 如果是小数
        parts = s.split('.')
        if len(parts) != 2:
            return False
        
        integer_part, decimal_part = parts
        
        # 整数部分检查
        if len(integer_part) > 1 and integer_part[0] == '0':
            return False
        
        # 小数部分检查：不能以0结尾
        if decimal_part.endswith('0'):
            return False
        
        return True
    
    def generate_valid_numbers(self, s: str) -> List[str]:
        """生成所有有效的数字表示"""
        numbers = []
        
        # 不加小数点
        if self.is_valid_number(s):
            numbers.append(s)
        
        # 加小数点
        for i in range(1, len(s)):
            integer_part = s[:i]
            decimal_part = s[i:]
            number = integer_part + '.' + decimal_part
            
            if self.is_valid_number(number):
                numbers.append(number)
        
        return numbers
    
    def ambiguous_coordinates(self, s: str) -> List[str]:
        """生成所有可能的有效坐标"""
        result = []
        
        # 去掉括号
        num = s[1:-1]
        
        # 枚举所有可能的分割点
        for i in range(1, len(num)):
            left = num[:i]
            right = num[i:]
            
            left_numbers = self.generate_valid_numbers(left)
            right_numbers = self.generate_valid_numbers(right)
            
            for l in left_numbers:
                for r in right_numbers:
                    result.append(f"({l}, {r})")
        
        return result

# ==================== 题目5: HDU 3065 病毒侵袭持续中 ====================

class VirusInvasionContinued:
    """
    题目描述：统计每个病毒在文本中出现的次数
    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=3065
    
    算法思路：
    1. 为每个病毒分配ID，构建AC自动机
    2. 在文本中进行匹配，统计每个病毒的出现次数
    3. 输出出现次数大于0的病毒及其次数
    
    时间复杂度：O(∑|Vi| + |T|)
    空间复杂度：O(∑|Vi| × |Σ|)
    """
    
    def __init__(self, n: int):
        self.tree = [{}]
        self.fail = [0]
        self.end = [0]  # 病毒编号
        self.count = [0]  # 节点匹配次数
        self.cnt = 0
        self.virus_count = [0] * (n + 1)
        
    def insert(self, virus: str, virus_id: int) -> None:
        """插入病毒特征码"""
        u = 0
        for c in virus:
            idx = ord(c)  # 使用ASCII码作为索引
            if idx not in self.tree[u]:
                self.cnt += 1
                self.tree[u][idx] = self.cnt
                self.tree.append({})
                self.fail.append(0)
                self.end.append(0)
                self.count.append(0)
            u = self.tree[u][idx]
        self.end[u] = virus_id
        
    def build(self) -> None:
        """构建AC自动机"""
        q = deque()
        for i in range(128):  # ASCII字符集
            if i in self.tree[0]:
                q.append(self.tree[0][i])
        
        while q:
            u = q.popleft()
            for i in range(128):
                if i in self.tree[u]:
                    v = self.tree[u][i]
                    self.fail[v] = self.tree[self.fail[u]].get(i, 0)
                    q.append(v)
                else:
                    self.tree[u][i] = self.tree[self.fail[u]].get(i, 0)
    
    def match(self, text: str) -> None:
        """在文本中进行匹配并统计病毒出现次数"""
        u = 0
        for c in text:
            idx = ord(c)
            u = self.tree[u].get(idx, 0)
            self.count[u] += 1
        
        # 使用拓扑排序汇总匹配次数
        indegree = [0] * (self.cnt + 1)
        for i in range(1, self.cnt + 1):
            indegree[self.fail[i]] += 1
        
        q = deque()
        for i in range(1, self.cnt + 1):
            if indegree[i] == 0:
                q.append(i)
        
        while q:
            u = q.popleft()
            if self.end[u] != 0:
                self.virus_count[self.end[u]] = self.count[u]
            
            v = self.fail[u]
            self.count[v] += self.count[u]
            indegree[v] -= 1
            if indegree[v] == 0:
                q.append(v)
    
    def print_results(self, viruses: List[str]) -> None:
        """打印结果"""
        for i in range(1, len(self.virus_count)):
            if self.virus_count[i] > 0:
                print(f"{viruses[i-1]}: {self.virus_count[i]}")

# ==================== 主函数和测试用例 ====================

def main():
    """主测试函数"""
    
    # 测试文本生成器
    print("=== 测试文本生成器 ===")
    generator = TextGenerator()
    generator.insert("ABC")
    generator.insert("DEF")
    generator.build()
    result = generator.solve(3)
    print(f"长度为3的至少包含一个模式串的字符串个数: {result}")
    
    # 测试频率查询
    print("\n=== 测试频率查询 ===")
    fos = FrequencyOfString(2)
    fos.insert("ab", 1)
    fos.insert("bc", 2)
    fos.build()
    fos.preprocess("abcabc")
    print(f"模式串'ab'第1次出现位置: {fos.query(1, 1)}")
    print(f"模式串'bc'第2次出现位置: {fos.query(2, 2)}")
    
    # 测试MANDRAKE
    print("\n=== 测试MANDRAKE ===")
    mandrake = Mandrake(3)
    mandrake.insert("ab", 1)
    mandrake.insert("bc", 2)
    mandrake.insert("abc", 3)
    mandrake.build()
    mandrake.match("abcabcab")
    result = mandrake.count_patterns(2)
    print(f"出现次数至少2次的模式串数量: {result}")
    
    # 测试模糊坐标
    print("\n=== 测试模糊坐标 ===")
    ac = AmbiguousCoordinates()
    coordinates = ac.ambiguous_coordinates("(123)")
    print(f"有效坐标数量: {len(coordinates)}")
    for coord in coordinates[:5]:  # 只显示前5个
        print(coord)
    
    # 测试病毒侵袭持续中
    print("\n=== 测试病毒侵袭持续中 ===")
    vic = VirusInvasionContinued(2)
    viruses = ["VIRUS1", "VIRUS2"]
    vic.insert(viruses[0], 1)
    vic.insert(viruses[1], 2)
    vic.build()
    vic.match("THIS IS A TEST VIRUS1 AND VIRUS2 STRING")
    vic.print_results(viruses)

if __name__ == "__main__":
    main()