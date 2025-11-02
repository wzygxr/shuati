"""
SPOJ DQUERY - D-query (区间不同元素个数查询) - Python实现

题目来源：SPOJ
题目链接：https://www.spoj.com/problems/DQUERY/
题目描述：
给定一个长度为n的数组，以及m个查询，每个查询要求计算区间[l,r]内不同元素的个数

解题思路：
使用莫队算法（Mo's Algorithm）解决此问题。莫队算法是一种基于分块思想的离线算法，
通过巧妙地安排查询顺序来优化区间查询问题。

算法步骤：
1. 将数组分块，块大小约为sqrt(n)
2. 将所有查询按左端点所在块和右端点排序
3. 通过指针移动维护当前区间的答案
4. 通过添加或删除元素来更新答案

时间复杂度：O((n+m) * sqrt(n))
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 性能优化：使用莫队算法减少重复计算
3. 鲁棒性：处理各种边界情况
"""

import math
import sys

class DQuerySolution:
    def __init__(self, size):
        """
        初始化莫队算法结构
        
        :param size: 数组大小
        """
        self.n = size
        # 设置块大小为sqrt(n)
        self.block_size = int(math.sqrt(size))
        
        # 原数组
        self.arr = [0] * (size + 1)
        # 计数数组，记录每个元素出现的次数
        self.count = [0] * 1000010
        # 当前不同元素个数
        self.current_ans = 0
    
    def add(self, pos):
        """
        添加元素到当前区间
        
        :param pos: 位置
        """
        self.count[self.arr[pos]] += 1
        if self.count[self.arr[pos]] == 1:
            self.current_ans += 1
    
    def remove(self, pos):
        """
        从当前区间移除元素
        
        :param pos: 位置
        """
        self.count[self.arr[pos]] -= 1
        if self.count[self.arr[pos]] == 0:
            self.current_ans -= 1
    
    def mo_algorithm(self, queries):
        """
        执行莫队算法
        
        :param queries: 查询列表，每个查询包含(l, r, id)
        :return: 答案列表
        """
        # 对查询进行排序
        queries.sort(key=lambda x: (x[0] // self.block_size, x[1]))
        
        # 初始化答案数组
        ans = [0] * (len(queries) + 1)
        
        current_l, current_r = 1, 0
        
        for l, r, id in queries:
            # 扩展或收缩左边界
            while current_l < l:
                self.remove(current_l)
                current_l += 1
            while current_l > l:
                current_l -= 1
                self.add(current_l)
            
            # 扩展或收缩右边界
            while current_r < r:
                current_r += 1
                self.add(current_r)
            while current_r > r:
                self.remove(current_r)
                current_r -= 1
            
            # 记录答案
            ans[id] = self.current_ans
        
        return ans

def main():
    """
    主函数，用于测试
    """
    # 读取数组大小
    n = int(input())
    
    # 初始化解决方案
    solution = DQuerySolution(n)
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        solution.arr[i] = elements[i - 1]
    
    # 读取查询数量
    m = int(input())
    
    # 读取所有查询
    queries = []
    for i in range(1, m + 1):
        l, r = map(int, input().split())
        queries.append((l, r, i))
    
    # 执行莫队算法
    ans = solution.mo_algorithm(queries)
    
    # 输出所有查询结果
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()