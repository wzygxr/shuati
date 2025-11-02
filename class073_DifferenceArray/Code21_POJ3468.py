import sys

class FenwickTree:
    """
    树状数组（Fenwick Tree）实现
    用于高效支持区间更新和区间查询
    """
    
    def __init__(self, size: int):
        self.n = size
        self.tree = [0] * (size + 1)
    
    def update(self, index: int, delta: int) -> None:
        """
        单点更新
        
        Args:
            index: 更新位置
            delta: 增量值
        """
        while index <= self.n:
            self.tree[index] += delta
            index += index & -index
    
    def query(self, index: int) -> int:
        """
        前缀和查询
        
        Args:
            index: 查询位置
            
        Returns:
            前缀和
        """
        total = 0
        while index > 0:
            total += self.tree[index]
            index -= index & -index
        return total
    
    def range_query(self, l: int, r: int) -> int:
        """
        区间和查询
        
        Args:
            l: 左边界
            r: 右边界
            
        Returns:
            区间和
        """
        return self.query(r) - self.query(l - 1)

def main():
    """
    POJ 3468. A Simple Problem with Integers
    
    题目描述:
    给定一个长度为 N 的数列 A，以及 M 条指令，每条指令可能是以下两种之一：
    1. "C a b c"：表示给 [a, b] 区间中的每一个数加上 c。
    2. "Q a b"：表示询问 [a, b] 区间中所有数的和。
    
    示例:
    输入:
    10 5
    1 2 3 4 5 6 7 8 9 10
    Q 4 4
    Q 1 10
    Q 2 4
    C 3 6 3
    Q 2 4
    
    输出:
    4
    55
    9
    15
    
    题目链接: http://poj.org/problem?id=3468
    
    解题思路:
    使用线段树或树状数组来支持区间更新和区间查询。
    这里使用差分数组结合树状数组的方法：
    1. 维护两个树状数组：一个用于记录区间加法的累积影响，另一个用于记录区间加法的次数
    2. 区间更新时，使用差分思想在树状数组上进行标记
    3. 区间查询时，通过两个树状数组的组合计算得到区间和
    
    时间复杂度: O((N+M)logN) - 每次操作的时间复杂度为O(logN)
    空间复杂度: O(N) - 需要存储树状数组
    
    这是最优解之一，线段树和树状数组都是解决此类问题的标准方法。
    """
    data = sys.stdin.read().split()
    if not data:
        return
    
    n = int(data[0])
    m = int(data[1])
    
    arr = [0] * (n + 1)
    bit1 = FenwickTree(n)  # 用于记录区间加法的累积影响
    bit2 = FenwickTree(n)  # 用于记录区间加法的次数
    
    # 读取初始数组
    idx = 2
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    # 构建初始前缀和
    prefix = [0] * (n + 1)
    for i in range(1, n + 1):
        prefix[i] = prefix[i - 1] + arr[i]
    
    # 处理指令
    for _ in range(m):
        op = data[idx]
        idx += 1
        if op == 'C':
            a = int(data[idx]); idx += 1
            b = int(data[idx]); idx += 1
            c = int(data[idx]); idx += 1
            
            # 区间更新: 使用差分思想
            bit1.update(a, c)
            if b + 1 <= n:
                bit1.update(b + 1, -c)
            bit2.update(a, c * (a - 1))
            if b + 1 <= n:
                bit2.update(b + 1, -c * b)
        elif op == 'Q':
            a = int(data[idx]); idx += 1
            b = int(data[idx]); idx += 1
            
            # 区间查询: 使用两个树状数组组合计算
            sum1 = prefix[b] + bit1.query(b) * b - bit2.query(b)
            sum2 = prefix[a - 1] + bit1.query(a - 1) * (a - 1) - bit2.query(a - 1)
            print(sum1 - sum2)

if __name__ == "__main__":
    main()