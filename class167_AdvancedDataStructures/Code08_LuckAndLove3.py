#!/usr/bin/env python3
# -*- coding: utf-8 -*-

'''
线段树套线段树（二维线段树）- Python版本

基础问题：HDU 1823 Luck and Love
题目链接: https://acm.hdu.edu.cn/showproblem.php?pid=1823

问题描述：
人有三种属性，身高、活泼度、缘分值。身高为int类型，活泼度和缘分值为小数点后最多1位的double类型。
实现一种数据结构，支持以下操作：
1. 操作 I a b c   : 加入一个人，身高为a，活泼度为b，缘分值为c
2. 操作 Q a b c d : 查询身高范围[a,b]，活泼度范围[c,d]，所有人中的缘分最大值
注意操作Q中，如果a > b需要交换，如果c > d需要交换。
约束条件：100 <= 身高 <= 200，0.0 <= 活泼度、缘分值 <= 100.0

算法思路：
这是一个二维区间最大值查询问题，采用线段树套线段树（二维线段树）的数据结构来解决。

数据结构设计：
1. 外层线段树用于维护身高维度（x轴）
2. 内层线段树用于维护活泼度维度（y轴）
3. 外层线段树范围：[MINX, MAXX] = [100, 200]，共101个值
4. 内层线段树范围：[MINY, MAXY] = [0, 1000]，共1001个值（活泼度*10转为整数）
5. 每个外层线段树节点对应一个内层线段树，用于存储其覆盖区间内的活泼度-缘分值映射

核心操作：
1. build：构建外层线段树，每个节点构建对应的内层线段树
2. update：更新指定身高和活泼度的缘分值
3. query：查询某个身高区间和活泼度区间内缘分值的最大值

时间复杂度分析：
1. 单点更新：O(log(身高范围) * log(活泼度范围)) = O(log(101) * log(1001)) ≈ O(7 * 10) = O(70)
2. 区间查询：O(log(身高范围) * log(活泼度范围)) = O(70)

空间复杂度分析：
1. 外层线段树节点数：O(身高范围 * 4) = O(404)
2. 内层线段树节点数：O(活泼度范围 * 4) = O(4004)
3. 总空间：O(404 * 4004) = O(1,617,616)

算法优势：
1. 支持动态更新和在线查询
2. 高效处理二维区间最值查询
3. 可以灵活处理各种查询范围

算法劣势：
1. 实现复杂度较高
2. 空间消耗较大
3. 常数因子较大，在大数据量下效率可能受到影响

适用场景：
1. 需要频繁进行二维区间查询操作
2. 数据可以动态更新
3. 查询区域不规则
4. 数据分布较稀疏

更多类似题目：
1. HDU 4911 Inversion (二维线段树)
2. POJ 3468 A Simple Problem with Integers (树状数组套线段树)
3. SPOJ GSS3 Can you answer these queries III (线段树区间查询)
4. Codeforces 1100F Ivan and Burgers (线段树维护线性基)
5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (二维前缀和)
6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树)
7. UVa 11402 Ahoy, Pirates! (线段树区间修改)
8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询)
9. CodeChef CHAOS2 Chaos (二维线段树)
10. HackerEarth Range and Queries (线段树应用)
11. 牛客网 NC14732 区间第k大 (线段树套平衡树)
12. 51Nod 1685 第K大 (线段树套线段树)
13. SGU 398 Tickets (线段树区间处理)
14. Codeforces 609E Minimum spanning tree for each edge (线段树优化)
15. UVA 12538 Version Controlled IDE (线段树维护版本)

Python语言特性注意事项：
1. Python中使用列表的列表来表示二维线段树
2. 初始化时需要预先分配好空间以提高效率
3. 注意浮点数精度问题，特别是在活泼度和缘分值的处理上
4. 由于Python的递归深度限制，对于较大的树需要注意递归深度
5. Python中的整数除法使用//运算符
6. 在Python中，递归可能导致栈溢出，可以考虑迭代实现

工程化考量：
1. 异常处理：处理输入格式错误、非法参数等情况
2. 边界情况：处理查询范围为空、查询结果不存在等情况
3. 性能优化：使用动态开点减少内存分配开销
4. 可读性：添加详细注释，变量命名清晰
5. 可维护性：模块化设计，便于扩展和修改
6. 单元测试：编写测试用例，确保功能正确性

优化技巧：
1. 使用预分配的列表而不是动态扩展列表以提高Python性能
2. 考虑使用迭代方式实现线段树操作以避免递归深度限制
3. 使用numpy等库来优化大规模数组操作
4. 对于频繁调用的函数，可以考虑使用lru_cache装饰器进行缓存
5. 对于大数据量，可以使用动态开点线段树以减少内存占用
'''

class LuckAndLove:
    def __init__(self):
        # 身高范围内有多少数字
        self.n = 101
        
        # 活泼度范围内有多少数字
        self.m = 1001
        
        # 身高范围对应[MINX, MAXX]，活泼度范围对应[MINY, MAXY]
        self.MINX = 100
        self.MAXX = 200
        self.MINY = 0
        self.MAXY = 1000
        
        # 外层是身高线段树，内层是活泼度线段树
        # 每一个外层线段树的节点，对应着一棵内层线段树
        # 内层线段树收集缘分值
        self.tree = [[-1 for _ in range(self.m << 2)] for _ in range(self.n << 2)]
    
    def innerBuild(self, yl, yr, xi, yi):
        """初始化内层线段树"""
        self.tree[xi][yi] = -1
        if yl < yr:
            mid = (yl + yr) // 2
            self.innerBuild(yl, mid, xi, yi << 1)
            self.innerBuild(mid + 1, yr, xi, yi << 1 | 1)
    
    def innerUpdate(self, jobi, jobv, yl, yr, xi, yi):
        """更新内层线段树"""
        if yl == yr:
            self.tree[xi][yi] = max(self.tree[xi][yi], jobv)
        else:
            mid = (yl + yr) // 2
            if jobi <= mid:
                self.innerUpdate(jobi, jobv, yl, mid, xi, yi << 1)
            else:
                self.innerUpdate(jobi, jobv, mid + 1, yr, xi, yi << 1 | 1)
            self.tree[xi][yi] = max(self.tree[xi][yi << 1], self.tree[xi][yi << 1 | 1])
    
    def innerQuery(self, jobl, jobr, yl, yr, xi, yi):
        """查询内层线段树"""
        if jobl <= yl and yr <= jobr:
            return self.tree[xi][yi]
        mid = (yl + yr) // 2
        ans = -1
        if jobl <= mid:
            ans = self.innerQuery(jobl, jobr, yl, mid, xi, yi << 1)
        if jobr > mid:
            ans = max(ans, self.innerQuery(jobl, jobr, mid + 1, yr, xi, yi << 1 | 1))
        return ans
    
    def outerBuild(self, xl, xr, xi):
        """初始化外层线段树"""
        self.innerBuild(self.MINY, self.MAXY, xi, 1)
        if xl < xr:
            mid = (xl + xr) // 2
            self.outerBuild(xl, mid, xi << 1)
            self.outerBuild(mid + 1, xr, xi << 1 | 1)
    
    def outerUpdate(self, jobx, joby, jobv, xl, xr, xi):
        """更新外层线段树"""
        self.innerUpdate(joby, jobv, self.MINY, self.MAXY, xi, 1)
        if xl < xr:
            mid = (xl + xr) // 2
            if jobx <= mid:
                self.outerUpdate(jobx, joby, jobv, xl, mid, xi << 1)
            else:
                self.outerUpdate(jobx, joby, jobv, mid + 1, xr, xi << 1 | 1)
    
    def outerQuery(self, jobxl, jobxr, jobyl, jobyr, xl, xr, xi):
        """查询外层线段树"""
        if jobxl <= xl and xr <= jobxr:
            return self.innerQuery(jobyl, jobyr, self.MINY, self.MAXY, xi, 1)
        mid = (xl + xr) // 2
        ans = -1
        if jobxl <= mid:
            ans = self.outerQuery(jobxl, jobxr, jobyl, jobyr, xl, mid, xi << 1)
        if jobxr > mid:
            ans = max(ans, self.outerQuery(jobxl, jobxr, jobyl, jobyr, mid + 1, xr, xi << 1 | 1))
        return ans
    
    def process(self, operations):
        """处理操作序列"""
        results = []
        
        # 初始化tree数组
        for i in range(len(self.tree)):
            for j in range(len(self.tree[i])):
                self.tree[i][j] = -1
        
        for op in operations:
            if op[0] == 'I':
                a, b, c = op[1], op[2], op[3]
                joby = int(b * 10)
                jobv = int(c * 10)
                self.outerUpdate(a, joby, jobv, self.MINX, self.MAXX, 1)
            else:  # op[0] == 'Q'
                a, b, c, d = op[1], op[2], op[3], op[4]
                xl = min(a, b)
                xr = max(a, b)
                yl = int(min(c, d) * 10)
                yr = int(max(c, d) * 10)
                ans = self.outerQuery(xl, xr, yl, yr, self.MINX, self.MAXX, 1)
                if ans == -1:
                    results.append(-1)
                else:
                    results.append(ans / 10)
        
        return results

# 由于HDU在线评测系统需要特定的输入输出格式，这里提供核心算法实现
# 实际使用时需要根据具体要求调整输入输出处理

if __name__ == "__main__":
    # 算法核心实现已完成，输入输出部分根据具体环境实现
    pass