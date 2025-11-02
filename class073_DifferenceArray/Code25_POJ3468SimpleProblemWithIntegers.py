"""
POJ 3468 A Simple Problem with Integers

题目描述:
你有N个整数A1, A2, ... , AN。你需要处理两种类型的操作。
一种操作是给定区间的每个数加上一个给定的数。
另一种操作是查询给定区间所有数的和。

输入:
第一行包含两个数字N和Q。1 ≤ N,Q ≤ 100000。
第二行包含N个数字，表示A1, A2, ... , AN的初始值。-1000000000 ≤ Ai ≤ 1000000000。
接下来Q行，每行包含一个操作：
"C a b c" 表示将区间[a,b]中的每个数加上c。-10000 ≤ c ≤ 10000。
"Q a b" 表示查询区间[a,b]中所有数的和。

输出:
对于每个"Q a b"操作，输出一行表示区间和。

题目链接: http://poj.org/problem?id=3468

解题思路:
使用树状数组结合差分数组来处理区间更新和区间查询操作。
1. 使用两个树状数组维护差分信息
2. 对于区间加法操作，使用差分标记更新两个树状数组
3. 对于区间查询操作，使用前缀和公式计算区间和

时间复杂度: O(Q log N) - 每次操作需要O(log N)时间
空间复杂度: O(N) - 需要额外的树状数组空间

这是最优解，因为需要支持动态区间更新和查询操作。
"""


class BIT:
    def __init__(self, size):
        self.n = size
        self.tree = [0] * (size + 1)
    
    def update(self, idx, val):
        while idx <= self.n:
            self.tree[idx] += val
            idx += idx & -idx
    
    def query(self, idx):
        sum_val = 0
        while idx > 0:
            sum_val += self.tree[idx]
            idx -= idx & -idx
        return sum_val


class RangeBIT:
    def __init__(self, size):
        self.n = size
        self.bit1 = BIT(size)
        self.bit2 = BIT(size)
    
    # 区间更新 [l, r] 加上 val
    def range_update(self, l, r, val):
        self.bit1.update(l, val)
        self.bit1.update(r + 1, -val)
        self.bit2.update(l, val * (l - 1))
        self.bit2.update(r + 1, -val * r)
    
    # 查询前缀和 [1, idx]
    def prefix_sum(self, idx):
        return self.bit1.query(idx) * idx - self.bit2.query(idx)
    
    # 查询区间和 [l, r]
    def range_sum(self, l, r):
        return self.prefix_sum(r) - self.prefix_sum(l - 1)


def main():
    """
    主函数，处理输入并输出结果
    
    时间复杂度: O(Q log N) - 每次操作需要O(log N)时间
    空间复杂度: O(N) - 需要额外的树状数组空间
    
    工程化考量:
    1. 输入处理: 使用高效的输入处理方式
    2. 边界处理: 确保数组索引正确
    3. 性能优化: 使用树状数组结合差分处理区间操作
    4. 输出格式: 按照题目要求输出结果
    """
    try:
        line = input().split()
        n = int(line[0])
        q = int(line[1])
        
        range_bit = RangeBIT(n)
        
        values = list(map(int, input().split()))
        for i in range(n):
            range_bit.range_update(i + 1, i + 1, values[i])
        
        for _ in range(q):
            operation = input().split()
            op = operation[0]
            
            if op == "C":
                # 区间更新操作
                a = int(operation[1])
                b = int(operation[2])
                c = int(operation[3])
                range_bit.range_update(a, b, c)
            else:
                # 区间查询操作
                a = int(operation[1])
                b = int(operation[2])
                print(range_bit.range_sum(a, b))
                
    except EOFError:
        pass


if __name__ == "__main__":
    main()