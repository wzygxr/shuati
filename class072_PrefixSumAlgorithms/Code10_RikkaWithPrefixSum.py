"""
Rikka with Prefix Sum (牛客网题目)

题目描述:
给定一个长度为n初始全为0的数列A。m次操作，要求支持以下三种操作：
1. 区间加一个数v
2. 全局修改，对于每一个i，把Ai改成原序列前i项的和（前缀和）
3. 区间求和

示例:
输入:
1
100000 7
1 1 3 1
2
3 2333 6666
2
3 2333 6666
2
3 2333 6666

输出:
13002
58489497
12043005

提示:
1 ≤ n,m ≤ 10^5
1 ≤ L ≤ R ≤ n
0 ≤ v ≤ 10^9
查询操作不超过500次

解题思路:
使用差分数组和组合数学来优化操作。
1. 对于操作1（区间加），使用差分数组记录
2. 对于操作2（前缀和），记录前缀和操作的次数
3. 对于操作3（区间求和），使用组合数学公式计算结果

核心思想：如果在(i,j)点+w，那么(x,y)点的系数为C(x-i+y-j-1, x-i-1)

时间复杂度: O(m * 查询次数)
空间复杂度: O(m)

这是一个高级的前缀和应用题目，结合了差分数组和组合数学。
"""


class RikkaWithPrefixSum:
    MOD = 998244353
    N = 200010

    def __init__(self):
        # 预计算阶乘和逆元
        self.fac = [0] * self.N
        self.inv = [0] * self.N

        # 预处理阶乘
        self.fac[0] = 1
        for i in range(1, self.N):
            self.fac[i] = self.fac[i - 1] * i % self.MOD

        # 预处理逆元
        self.inv[self.N - 1] = self.qmod(self.fac[self.N - 1], self.MOD - 2)
        for i in range(self.N - 2, -1, -1):
            self.inv[i] = self.inv[i + 1] * (i + 1) % self.MOD

    def qmod(self, x, y):
        """
        快速幂运算

        :param x: 底数
        :param y: 指数
        :return: (x^y) % MOD
        """
        x %= self.MOD
        ans = 1
        while y > 0:
            if y & 1:
                ans = ans * x % self.MOD
            x = x * x % self.MOD
            y >>= 1
        return ans

    def C(self, a, b):
        """
        计算组合数C(a,b)

        :param a: 组合数上标
        :param b: 组合数下标
        :return: C(a,b) % MOD
        """
        if b > a or b < 0:
            return 0
        return self.fac[a] * self.inv[b] % self.MOD * self.inv[a - b] % self.MOD

    def fun(self, x, y, ops, op_count):
        """
        辅助函数，计算贡献值

        :param x: x坐标
        :param y: y坐标
        :param ops: 操作数组
        :param op_count: 操作数量
        :return: 贡献值
        """
        ans = 0
        for i in range(op_count):
            if ops[i][0] < x and ops[i][1] <= y:
                ans = (ans + self.C(x - ops[i][0] + y - ops[i][1] - 1, x - ops[i][0] - 1) *
                       ops[i][2]) % self.MOD
        return ans

    def solve(self, n, operations):
        """
        主要解法函数

        :param n: 数组长度
        :param operations: 操作数组
        :return: 查询结果数组
        """
        # 记录操作1的信息
        # 每个操作存储：[操作次数, 位置, 值]
        ops = [[0, 0, 0] for _ in range(len(operations) * 2)]
        op_count = 0

        # 当前前缀和操作次数
        prefix_sum_count = 1

        # 结果数组
        results = [0] * len(operations)
        result_count = 0

        for op in operations:
            if op[0] == 1:
                # 操作1：区间加一个数
                l, r, v = op[1], op[2], op[3]

                # 使用差分数组思想记录操作
                ops[op_count][0] = prefix_sum_count - 1  # 记录当前前缀和操作次数
                ops[op_count][1] = l  # 起始位置
                ops[op_count][2] = v % self.MOD  # 值
                op_count += 1

                ops[op_count][0] = prefix_sum_count - 1  # 记录当前前缀和操作次数
                ops[op_count][1] = r + 1  # 结束位置+1
                ops[op_count][2] = -(v % self.MOD)  # 负值
                op_count += 1
            elif op[0] == 2:
                # 操作2：全局前缀和
                prefix_sum_count += 1
            else:
                # 操作3：区间求和
                l, r = op[1], op[2]

                # 计算区间和
                ans = (self.fun(prefix_sum_count + 1, r, ops, op_count) -
                       self.fun(prefix_sum_count + 1, l - 1, ops, op_count)) % self.MOD
                ans = (ans + self.MOD) % self.MOD  # 确保结果为正

                results[result_count] = ans
                result_count += 1

        # 返回实际结果数组
        return results[:result_count]


def main():
    """测试用例"""
    # 创建解法实例
    solver = RikkaWithPrefixSum()

    # 简化测试用例
    n = 5
    operations = [
        [1, 1, 3, 1],  # 区间[1,3]加1
        [2],  # 前缀和操作
        [3, 2, 4]  # 查询区间[2,4]的和
    ]

    results = solver.solve(n, operations)

    print("测试结果:")
    for result in results:
        print(result)


if __name__ == "__main__":
    main()