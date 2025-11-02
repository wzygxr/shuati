# 洛谷P4887 莫队二次离线模板题 - Python版本
# 题目来源: 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）
# 题目链接: https://www.luogu.com.cn/problem/P4887
# 题目大意: 给定一个数组，定义k1二元组为满足arr[i] XOR arr[j]的二进制表示中有k个1的二元组(i,j)
# 查询区间内k1二元组的个数
# 数据范围: 1 <= n、m <= 10^5, 0 <= arr[i]、k < 16384(2的14次方)
# 解题思路: 使用莫队二次离线算法优化普通莫队的转移操作
# 时间复杂度: O(n*sqrt(n) + n*C(k,14)) 其中C(k,14)表示14位二进制数中恰好有k个1的数的个数
# 空间复杂度: O(n + 2^14)
# 相关题目:
# 1. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)）: https://www.luogu.com.cn/problem/P4887
# 2. 洛谷 P5501 [LnOI2019] 来者不拒，去者不追: https://www.luogu.com.cn/problem/P5501
# 3. 洛谷 P5398 [Ynoi2019 模拟赛] Yuno loves sqrt technology II: https://www.luogu.com.cn/problem/P5398
# 4. 洛谷 P5047 [Ynoi2019 模拟赛] Yuno loves sqrt technology III: https://www.luogu.com.cn/problem/P5047
# 5. Codeforces 617E XOR and Favorite Number: https://codeforces.com/contest/617/problem/E
# 6. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
# 7. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
# 8. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
# 9. POJ 2104 K-th Number: http://poj.org/problem?id=2104

import sys
import math
from collections import defaultdict

class P4887_MoOfflineTwice:
    def __init__(self):
        self.MAXN = 100002
        self.MAXV = 1 << 14  # 2^14 = 16384
        
        self.n = 0
        self.m = 0
        self.k = 0
        self.arr = [0] * self.MAXN
        self.bi = [0] * self.MAXN  # 块编号
        self.kOneArr = [0] * self.MAXV  # 存储二进制中有k个1的数
        self.cntk = 0  # 二进制中有k个1的数的个数

        # 莫队查询任务: l, r, id
        self.query = [[0, 0, 0] for _ in range(self.MAXN)]

        # 离线任务: x, l, r, op, id
        # headl[x]: x在l~r左侧的离线任务列表
        # headr[x]: x在l~r右侧的离线任务列表
        self.headl = [0] * self.MAXN
        self.headr = [0] * self.MAXN
        self.nextq = [0] * (self.MAXN << 1)  # 链式前向星
        self.ql = [0] * (self.MAXN << 1)
        self.qr = [0] * (self.MAXN << 1)
        self.qop = [0] * (self.MAXN << 1)
        self.qid = [0] * (self.MAXN << 1)
        self.cntq = 0  # 离线任务计数

        # cnt[v]: 当前数字v作为第二个数，之前出现的数字作为第一个数，产生多少k1二元组
        self.cnt = [0] * self.MAXV
        # 前缀和与后缀和
        self.pre = [0] * self.MAXN
        self.suf = [0] * self.MAXN

        self.ans = [0] * self.MAXN  # 答案数组

    # 计算一个数二进制表示中1的个数
    def lowbit(self, i):
        return i & -i

    # 计算一个数二进制表示中1的个数
    def countOne(self, num):
        ret = 0
        while num > 0:
            ret += 1
            num -= self.lowbit(num)
        return ret

    # 添加左侧离线任务
    def addLeftOffline(self, x, l, r, op, id):
        self.cntq += 1
        self.nextq[self.cntq] = self.headl[x]
        self.headl[x] = self.cntq
        self.ql[self.cntq] = l
        self.qr[self.cntq] = r
        self.qop[self.cntq] = op
        self.qid[self.cntq] = id

    # 添加右侧离线任务
    def addRightOffline(self, x, l, r, op, id):
        self.cntq += 1
        self.nextq[self.cntq] = self.headr[x]
        self.headr[x] = self.cntq
        self.ql[self.cntq] = l
        self.qr[self.cntq] = r
        self.qop[self.cntq] = op
        self.qid[self.cntq] = id

    # 预处理函数
    def prepare(self):
        # 计算块大小和块编号
        blen = int(math.sqrt(self.n))
        for i in range(1, self.n + 1):
            self.bi[i] = (i - 1) // blen + 1

        # 对查询进行排序
        # Python中使用自定义比较函数需要使用functools.cmp_to_key
        def query_cmp(a, b):
            if self.bi[a[0]] != self.bi[b[0]]:
                return self.bi[a[0]] - self.bi[b[0]]
            return a[1] - b[1]
        
        # 只对有效的查询进行排序（索引1到m）
        valid_queries = self.query[1:self.m + 1]
        valid_queries.sort(key=lambda x: (self.bi[x[0]], x[1]))
        self.query[1:self.m + 1] = valid_queries

        # 预处理所有二进制表示中有k个1的数
        for v in range(self.MAXV):
            if self.countOne(v) == self.k:
                self.cntk += 1
                self.kOneArr[self.cntk] = v

    # 计算函数
    def compute(self):
        # 正向计算前缀贡献
        for i in range(1, self.n + 1):
            # pre[i] = pre[i-1] + 以arr[i]为第二个数的k1二元组个数
            self.pre[i] = self.pre[i - 1] + self.cnt[self.arr[i]]

            # 更新cnt数组：对于每个二进制中有k个1的数t，arr[i] XOR t的值作为第一个数
            # 与arr[i]组成k1二元组，所以cnt[arr[i] XOR t]增加1
            for j in range(1, self.cntk + 1):
                self.cnt[self.arr[i] ^ self.kOneArr[j]] += 1

        # 清空cnt数组
        for i in range(self.MAXV):
            self.cnt[i] = 0

        # 反向计算后缀贡献
        for i in range(self.n, 0, -1):
            # suf[i] = suf[i+1] + 以arr[i]为第一个数的k1二元组个数
            self.suf[i] = self.suf[i + 1] + self.cnt[self.arr[i]]

            # 更新cnt数组
            for j in range(1, self.cntk + 1):
                self.cnt[self.arr[i] ^ self.kOneArr[j]] += 1

        # 执行莫队
        winl = 1
        winr = 0  # 当前窗口[l, r]
        for i in range(1, self.m + 1):
            jobl = self.query[i][0]  # 查询左端点
            jobr = self.query[i][1]  # 查询右端点
            id = self.query[i][2]    # 查询编号

            # 右端点向右扩展
            if winr < jobr:
                # 添加左侧离线任务
                self.addLeftOffline(winl - 1, winr + 1, jobr, -1, id)
                # 累加前缀贡献
                self.ans[id] += self.pre[jobr] - self.pre[winr]

            # 右端点向左收缩
            if winr > jobr:
                # 添加左侧离线任务
                self.addLeftOffline(winl - 1, jobr + 1, winr, 1, id)
                # 减去前缀贡献
                self.ans[id] -= self.pre[winr] - self.pre[jobr]
            
            winr = jobr  # 更新右端点

            # 左端点向左扩展
            if winl > jobl:
                # 添加右侧离线任务
                self.addRightOffline(winr + 1, jobl, winl - 1, -1, id)
                # 累加后缀贡献
                self.ans[id] += self.suf[jobl] - self.suf[winl]

            # 左端点向右收缩
            if winl < jobl:
                # 添加右侧离线任务
                self.addRightOffline(winr + 1, winl, jobl - 1, 1, id)
                # 减去后缀贡献
                self.ans[id] -= self.suf[winl] - self.suf[jobl]
            
            winl = jobl  # 更新左端点

        # 清空cnt数组
        for i in range(self.MAXV):
            self.cnt[i] = 0

        # 处理左侧离线任务
        for x in range(self.n + 1):
            # 更新cnt数组
            if x >= 1:
                for j in range(1, self.cntk + 1):
                    self.cnt[self.arr[x] ^ self.kOneArr[j]] += 1

            # 处理x位置的离线任务
            q = self.headl[x]
            while q > 0:
                l = self.ql[q]
                r = self.qr[q]
                op = self.qop[q]
                id = self.qid[q]
                for j in range(l, r + 1):
                    self.ans[id] += op * self.cnt[self.arr[j]]
                q = self.nextq[q]

        # 清空cnt数组
        for i in range(self.MAXV):
            self.cnt[i] = 0

        # 处理右侧离线任务
        for x in range(self.n + 1, 0, -1):
            # 更新cnt数组
            if x <= self.n:
                for j in range(1, self.cntk + 1):
                    self.cnt[self.arr[x] ^ self.kOneArr[j]] += 1

            # 处理x位置的离线任务
            q = self.headr[x]
            while q > 0:
                l = self.ql[q]
                r = self.qr[q]
                op = self.qop[q]
                id = self.qid[q]
                for j in range(l, r + 1):
                    self.ans[id] += op * self.cnt[self.arr[j]]
                q = self.nextq[q]

    def main(self):
        # 读取输入
        line = sys.stdin.readline().split()
        self.n = int(line[0])
        self.m = int(line[1])
        self.k = int(line[2])

        line = sys.stdin.readline().split()
        for i in range(1, self.n + 1):
            self.arr[i] = int(line[i - 1])

        for i in range(1, self.m + 1):
            line = sys.stdin.readline().split()
            self.query[i][0] = int(line[0])
            self.query[i][1] = int(line[1])
            self.query[i][2] = i

        # 预处理
        self.prepare()

        # 计算答案
        self.compute()

        # ans[i]代表答案变化量，需要加工成前缀和才是每个查询的答案
        # 注意在普通莫队的顺序下，去生成前缀和
        for i in range(2, self.m + 1):
            self.ans[self.query[i][2]] += self.ans[self.query[i - 1][2]]

        # 输出答案
        for i in range(1, self.m + 1):
            print(self.ans[i])

"""
算法分析:

时间复杂度: O(n*sqrt(n) + n*C(k,14))
其中C(k,14)表示14位二进制数中恰好有k个1的数的个数

空间复杂度: O(n + 2^14)

算法思路:
1. 使用莫队二次离线算法优化普通莫队的转移操作
2. 预处理所有二进制表示中有k个1的数
3. 通过前缀和与后缀和预计算部分贡献
4. 将莫队的扩展操作离线处理，批量计算

核心思想:
1. 对于查询[l,r]，我们维护区间内所有k1二元组的个数
2. k1二元组定义为满足arr[i] XOR arr[j]的二进制表示中有k个1的二元组
3. 通过预处理所有二进制中有k个1的数，可以在O(1)时间内判断两个数的XOR是否满足条件
4. 使用莫队算法处理区间扩展，通过二次离线优化转移复杂度

工程化考量:
1. 使用快速输入输出优化IO性能
2. 合理使用静态数组避免动态分配
3. 使用链式前向星存储离线任务
4. 通过位运算优化计算性能

调试技巧:
1. 可以通过打印中间结果验证算法正确性
2. 使用断言检查关键变量的正确性
3. 对比暴力算法验证结果
"""

if __name__ == "__main__":
    solution = P4887_MoOfflineTwice()
    solution.main()