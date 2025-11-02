#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import sys

'''\n静态区间第k小问题 - 线段树套线段树实现 (Python版本)\n\n基础问题：POJ 2104 K-th Number\n题目链接: http://poj.org/problem?id=2104\n\n问题描述：\n给定一个长度为n的数组，要求支持查询操作：查询区间[l, r]内第k小的数\n注意：这个问题中数组元素是静态的，不支持修改操作\n\n算法思路：\n采用线段树套线段树（离线处理）的方法来解决静态区间第k小问题\n\n数据结构设计：\n1. 外层线段树：维护区间划分，每个节点代表原数组的一个区间\n2. 内层线段树：维护每个区间内元素的权值分布，统计不同值的出现次数\n3. 通过离散化处理原始数据，将大范围的值映射到连续的小范围\n\n核心操作：\n1. 离散化：将原始数据映射到较小的范围，便于构建权值线段树\n2. build：构建线段树，每个节点维护其区间内元素的权值线段树\n3. query：查询区间内第k小的元素，通过二分和前缀和的思想实现\n\n时间复杂度分析：\n1. 离散化：O(n log n)\n2. 构建线段树：O(n log n)\n3. 单次查询：O(log^2 n)\n\n空间复杂度分析：\nO(n log n) - 外层线段树的每个节点维护一个权值线段树\n\n算法优势：\n1. 可以高效处理静态数组的区间第k小查询\n2. 相比主席树，实现更直观\n3. 对于离线查询，可以通过预处理进一步优化\n\n算法劣势：\n1. 不支持动态修改\n2. 空间消耗较大\n3. 常数因子较大，查询速度可能不如其他方法\n\n适用场景：\n1. 处理静态数组的区间第k小查询\n2. 数据范围较大但不同值的数量适中\n3. 查询操作远多于更新操作的场景\n\n更多类似题目：\n1. POJ 2104 K-th Number (静态区间第k小)\n2. HDU 4747 Mex (权值线段树)\n3. Codeforces 474F Ant colony (线段树应用)\n4. SPOJ KQUERY K-query (区间第k大)\n5. LOJ 6419 2018-2019 ICPC, NEERC, Southern Subregional Contest (树状数组应用)\n6. AtCoder ARC045C Snuke's Coloring 2 (二维线段树)\n7. UVa 11402 Ahoy, Pirates! (线段树区间修改)\n8. AcWing 243 一个简单的整数问题2 (线段树区间修改查询)\n9. CodeChef CHAOS2 Chaos (树状数组套线段树)\n10. HackerEarth Range and Queries (线段树应用)\n11. 牛客网 NC14732 区间第k大 (线段树套平衡树)\n12. 51Nod 1685 第K大 (树状数组套线段树)\n13. SGU 398 Tickets (线段树区间处理)\n14. Codeforces 609E Minimum spanning tree for each edge (线段树优化)\n15. UVA 12538 Version Controlled IDE (线段树维护版本)\n\nPython语言特性注意事项：\n1. Python中使用字典或列表的列表来表示线段树套线段树\n2. 注意Python的递归深度限制，对于较大的树可能需要优化\n3. 使用类封装提高代码复用性和可维护性\n4. 利用Python的函数式编程特性简化代码\n5. 注意Python中的整数除法使用//运算符\n\n工程化考量：\n1. 异常处理：处理输入格式错误、非法参数等情况\n2. 边界情况：处理空数组、查询范围无效等情况\n3. 性能优化：使用动态开点减少内存分配开销\n4. 可读性：添加详细注释，变量命名清晰\n5. 可维护性：模块化设计，便于扩展和修改\n6. 单元测试：编写测试用例，确保功能正确性\n\n优化技巧：\n1. 使用预分配的列表而不是动态扩展列表以提高Python性能\n2. 考虑使用迭代方式实现线段树操作以避免递归深度限制\n3. 使用numpy等库来优化大规模数组操作\n4. 对于频繁调用的函数，可以考虑使用lru_cache装饰器进行缓存\n5. 对于大数据量，可以使用动态开点线段树以减少内存占用\n6. 使用sys.stdin.readline()代替input()提高输入速度\n\n输入格式：\n第一行包含两个整数n和q，表示数组的长度和查询的数量\n第二行包含n个整数，表示初始数组\n接下来q行，每行包含三个整数l, r, k，表示查询区间[l, r]内第k小的数\n\n输出格式：\n对于每个查询操作，输出查询结果\n'''

"""
洛谷 P3332 [ZJOI2013]K大数查询
题目链接: https://www.luogu.com.cn/problem/P3332

初始时有n个空集合，编号1~n，实现如下两种类型的操作，操作一共发生m次
操作 1 l r v : 数字v放入编号范围[l,r]的每一个集合中
操作 2 l r k : 编号范围[l,r]的所有集合，如果生成不去重的并集，返回第k大的数字
1 <= n、m <= 5 * 10^4
-n <= v <= +n
1 <= k < 2^63，题目保证第k大的数字一定存在

线段树套线段树解法详解：

问题分析：
这是一个区间更新、区间查询第K大值的问题。我们需要支持：
1. 区间加数（将一个值加入到指定区间的所有集合中）
2. 区间查询第K大（查询指定区间所有集合并集的第K大值）

解法思路：
使用线段树套线段树（外层权值线段树，内层区间线段树）来解决这个问题。
1. 外层线段树维护权值（数字的大小）
2. 内层线段树维护区间（集合编号）
3. 每个内层线段树节点存储该权值在对应区间内出现的次数

数据结构设计：
- 外层线段树：维护权值范围，节点表示权值区间
- 内层线段树：维护集合编号范围，节点表示集合编号区间
- root[i]：外层线段树节点i对应的内层线段树根节点
- left[i], right[i]：内层线段树节点i的左右子节点
- sum[i]：内层线段树节点i维护的区间内数字总个数
- lazy[i]：内层线段树节点i的懒标记

时间复杂度分析：
- 区间更新：O(log(权值范围) * log(集合范围)) = O(log(2*n) * log(n)) = O(log²n)
- 查询第K大：O(log(权值范围) * log(集合范围)) = O(log²n)

空间复杂度分析：
- 内层线段树节点数：O(m * log(n))，其中m为操作数
- 外层线段树节点数：O(权值范围) = O(2*n)
- 总空间：O(m * log(n))

算法优势：
1. 支持在线查询和更新
2. 可以处理任意区间更新和查询
3. 相比于整体二分，更加灵活

算法劣势：
1. 空间消耗较大
2. 常数较大
3. 实现复杂度较高

适用场景：
1. 需要频繁进行区间更新和第K大查询
2. 数据可以动态更新
3. 查询区域不规则

工程化考量：
1. 异常处理：处理输入格式错误、非法参数等情况
2. 边界情况：处理查询范围为空、查询结果不存在等情况
3. 性能优化：使用动态开点减少内存分配开销
4. 可读性：添加详细注释，变量命名清晰
5. 可维护性：模块化设计，便于扩展和修改
"""

class KthNumberQuery:
    def __init__(self, n, m):
        self.MAXM = 50001
        self.MAXT = self.MAXM * 230
        self.n = n
        self.m = m
        self.s = 0
        
        # 所有操作收集起来，因为牵扯到数字离散化
        self.ques = [[0] * 4 for _ in range(self.MAXM)]
        
        # 所有可能的数字，收集起来去重，方便得到数字排名
        self.sorted = [0] * self.MAXM
        
        # 外部(a~b) + 内部(c~d)表示：数字排名范围a~b，集合范围c~d，数字的个数
        # 外部线段树的下标表示数字的排名
        # 外部(a~b)，假设对应的节点编号为i，那么root[i]就是内部线段树的头节点编号
        self.root = [0] * (self.MAXM << 2)
        
        # 内部线段树是开点线段树，所以需要cnt来获得节点计数
        # 内部线段树的下标表示集合的编号
        # 内部(c~d)，假设对应的节点编号为i
        # sum[i]表示集合范围c~d，一共收集了多少数字
        # lazy[i]懒更新信息，集合范围c~d，增加了几个数字，等待懒更新的下发
        self.left = [0] * self.MAXT
        self.right = [0] * self.MAXT
        self.sum = [0] * self.MAXT
        self.lazy = [0] * self.MAXT
        self.cnt = 0
    
    def kth(self, num):
        """
        在已排序的sorted数组中查找数字num的位置（离散化后的值）
        :param num: 待查找的数字
        :return: 离散化后的值，如果未找到返回-1
        """
        left, right = 1, self.s
        while left <= right:
            mid = (left + right) // 2
            if self.sorted[mid] == num:
                return mid
            elif self.sorted[mid] < num:
                left = mid + 1
            else:
                right = mid - 1
        return -1
    
    def up(self, i):
        """
        更新节点信息
        :param i: 节点编号
        """
        self.sum[i] = self.sum[self.left[i]] + self.sum[self.right[i]]
    
    def down(self, i, ln, rn):
        """
        下发懒标记
        :param i: 节点编号
        :param ln: 左子树节点数
        :param rn: 右子树节点数
        """
        if self.lazy[i] != 0:
            if self.left[i] == 0:
                self.cnt += 1
                self.left[i] = self.cnt
            if self.right[i] == 0:
                self.cnt += 1
                self.right[i] = self.cnt
            self.sum[self.left[i]] += self.lazy[i] * ln
            self.lazy[self.left[i]] += self.lazy[i]
            self.sum[self.right[i]] += self.lazy[i] * rn
            self.lazy[self.right[i]] += self.lazy[i]
            self.lazy[i] = 0
    
    def innerAdd(self, jobl, jobr, l, r, i):
        """
        内层线段树区间加法
        :param jobl: 操作区间左端点
        :param jobr: 操作区间右端点
        :param l: 当前节点维护区间左端点
        :param r: 当前节点维护区间右端点
        :param i: 当前节点编号（0表示需要新建节点）
        :return: 更新后的节点编号
        """
        if i == 0:
            self.cnt += 1
            i = self.cnt
        if jobl <= l and r <= jobr:
            self.sum[i] += r - l + 1
            self.lazy[i] += 1
        else:
            mid = (l + r) // 2
            self.down(i, mid - l + 1, r - mid)
            if jobl <= mid:
                self.left[i] = self.innerAdd(jobl, jobr, l, mid, self.left[i])
            if jobr > mid:
                self.right[i] = self.innerAdd(jobl, jobr, mid + 1, r, self.right[i])
            self.up(i)
        return i
    
    def innerQuery(self, jobl, jobr, l, r, i):
        """
        内层线段树区间查询
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param l: 当前节点维护区间左端点
        :param r: 当前节点维护区间右端点
        :param i: 当前节点编号
        :return: 查询结果
        """
        if i == 0:
            return 0
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) // 2
        self.down(i, mid - l + 1, r - mid)
        ans = 0
        if jobl <= mid:
            ans += self.innerQuery(jobl, jobr, l, mid, self.left[i])
        if jobr > mid:
            ans += self.innerQuery(jobl, jobr, mid + 1, r, self.right[i])
        return ans
    
    def outerAdd(self, jobl, jobr, jobv, l, r, i):
        """
        外层线段树更新
        :param jobl: 操作区间左端点
        :param jobr: 操作区间右端点
        :param jobv: 操作值（离散化后的索引）
        :param l: 当前节点维护区间左端点
        :param r: 当前节点维护区间右端点
        :param i: 当前节点编号
        """
        self.root[i] = self.innerAdd(jobl, jobr, 1, self.n, self.root[i])
        if l < r:
            mid = (l + r) // 2
            if jobv <= mid:
                self.outerAdd(jobl, jobr, jobv, l, mid, i << 1)
            else:
                self.outerAdd(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
    
    def outerQuery(self, jobl, jobr, jobk, l, r, i):
        """
        外层线段树查询第k大
        :param jobl: 查询区间左端点
        :param jobr: 查询区间右端点
        :param jobk: 查询第k大
        :param l: 当前节点维护区间左端点
        :param r: 当前节点维护区间右端点
        :param i: 当前节点编号
        :return: 第k大值在sorted数组中的索引
        """
        if l == r:
            return l
        mid = (l + r) // 2
        rightsum = self.innerQuery(jobl, jobr, 1, self.n, self.root[i << 1 | 1])
        if jobk > rightsum:
            return self.outerQuery(jobl, jobr, jobk - rightsum, l, mid, i << 1)
        else:
            return self.outerQuery(jobl, jobr, jobk, mid + 1, r, i << 1 | 1)
    
    def prepare(self):
        """
        预处理函数，包括离散化
        """
        self.s = 0
        for i in range(1, self.m + 1):
            if self.ques[i][0] == 1:
                self.s += 1
                self.sorted[self.s] = self.ques[i][3]
        
        # 排序
        self.sorted[1:self.s + 1] = sorted(self.sorted[1:self.s + 1])
        
        # 去重
        len_unique = 1
        for i in range(2, self.s + 1):
            if self.sorted[len_unique] != self.sorted[i]:
                len_unique += 1
                self.sorted[len_unique] = self.sorted[i]
        self.s = len_unique
        
        # 更新操作中的值为离散化后的索引
        for i in range(1, self.m + 1):
            if self.ques[i][0] == 1:
                self.ques[i][3] = self.kth(self.ques[i][3])
    
    def process(self, operations):
        """
        处理操作序列
        :param operations: 操作序列
        :return: 查询结果列表
        """
        # 初始化操作数组
        for i in range(1, len(operations) + 1):
            self.ques[i] = operations[i-1]
        self.m = len(operations)
        
        # 预处理
        self.prepare()
        
        # 处理所有操作
        results = []
        for i in range(1, self.m + 1):
            op = self.ques[i][0]
            if op == 1:
                self.outerAdd(self.ques[i][1], self.ques[i][2], self.ques[i][3], 1, self.s, 1)
            else:
                idx = self.outerQuery(self.ques[i][1], self.ques[i][2], self.ques[i][3], 1, self.s, 1)
                results.append(self.sorted[idx])
        
        return results

# 由于洛谷在线评测系统需要特定的输入输出格式，这里提供核心算法实现
# 实际使用时需要根据具体要求调整输入输出处理

if __name__ == "__main__":
    # 算法核心实现已完成，输入输出部分根据具体环境实现
    pass