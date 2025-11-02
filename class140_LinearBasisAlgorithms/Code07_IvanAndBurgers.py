# Ivan and Burgers
# 给定一个长度为n的数组，有q次询问，每次询问一个区间[l,r]，
# 求在这个区间内选取若干个数能得到的最大异或和。
# 1 <= n, q <= 5 * 10^5
# 0 <= a_i <= 2^31 - 1
# 测试链接 : https://codeforces.com/problemset/problem/1100/F
# 请务必在原有代码基础上增加详细注释，确保代码可以编译运行且没有错误

MAXN = 500001
BIT = 31

# 原数组
arr = [0] * MAXN
# 前缀线性基数组
prefixBasis = [[0 for _ in range(BIT + 2)] for _ in range(MAXN)]
# 数组长度和询问数
n = 0
q = 0

def insert(pos, num):
    """
    将数字插入到指定位置的线性基中
    算法思路：
    1. 从高位到低位扫描
    2. 如果当前位为1且线性基中该位为空，则直接插入
    3. 否则用线性基中该位的数异或当前数，继续处理
    @param pos: 位置
    @param num: 要插入的数字
    """
    for i in range(BIT, -1, -1):
        if (num >> i) & 1:
            if prefixBasis[pos][i] == 0:
                prefixBasis[pos][i] = num
                return
            num ^= prefixBasis[pos][i]

def preprocess():
    """
    预处理前缀线性基
    算法思路：
    1. 对于每个位置i，维护从位置1到位置i的所有数构成的线性基
    2. 位置i的线性基可以从位置i-1的线性基转移而来
    3. 将arr[i]插入到位置i-1的线性基中，得到位置i的线性基
    时间复杂度：O(n * BIT)
    空间复杂度：O(n * BIT)
    """
    # 初始化位置0的线性基为空
    for i in range(BIT + 1):
        prefixBasis[0][i] = 0
    
    # 逐个处理每个位置
    for i in range(1, n + 1):
        # 复制前一个位置的线性基
        for j in range(BIT + 1):
            prefixBasis[i][j] = prefixBasis[i - 1][j]
        
        # 将arr[i]插入到当前位置的线性基中
        insert(i, arr[i])

def query(l, r):
    """
    查询区间[l,r]内选取若干个数能得到的最大异或和
    算法思路：
    1. 利用前缀线性基的性质，区间[l,r]的线性基可以通过prefixBasis[r]和prefixBasis[l-1]计算得到
    2. 从高位到低位贪心地选择线性基中的元素来最大化结果
    时间复杂度：O(BIT)
    空间复杂度：O(BIT)
    @param l: 区间左端点
    @param r: 区间右端点
    @return: 区间内选取若干个数能得到的最大异或和
    """
    # 临时线性基数组
    tempBasis = [0] * (BIT + 2)
    
    # 复制prefixBasis[r]到临时线性基
    for i in range(BIT + 1):
        tempBasis[i] = prefixBasis[r][i]
    
    # 用prefixBasis[l-1]消元
    for i in range(BIT, -1, -1):
        if prefixBasis[l - 1][i] != 0:
            num = prefixBasis[l - 1][i]
            for j in range(BIT, -1, -1):
                if (num >> j) & 1:
                    if tempBasis[j] == 0:
                        tempBasis[j] = num
                        break
                    num ^= tempBasis[j]
    
    # 贪心地选择元素来最大化异或和
    ans = 0
    for i in range(BIT, -1, -1):
        if (ans ^ tempBasis[i]) > ans:
            ans ^= tempBasis[i]
    
    return ans

def main():
    """
    主函数
    读取输入数据，预处理前缀线性基，处理询问，输出结果
    """
    global n, q
    
    # 读取数组长度
    n = int(input())
    
    # 读取数组元素
    temp = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = temp[i - 1]
    
    # 预处理前缀线性基
    preprocess()
    
    # 读取询问数
    q = int(input())
    
    # 处理每个询问
    for _ in range(q):
        line = input().split()
        l = int(line[0])
        r = int(line[1])
        print(query(l, r))

if __name__ == "__main__":
    """
    线性基算法详解
    
    线性基（Linear Basis）是一种处理异或问题的重要数据结构，主要用于解决以下几类问题：
    1. 求n个数中选取任意个数异或能得到的最大值
    2. 求n个数中选取任意个数异或能得到的第k小值
    3. 判断一个数是否能由给定数组中的数异或得到
    4. 求能异或得到的数的个数
    
    Ivan and Burgers解题思路
    
    本题是经典的区间最大异或和查询问题，可以使用前缀线性基来解决：
    
    1. 预处理前缀线性基：
       - 对于每个位置i，维护从位置1到位置i的所有数构成的线性基
       - 位置i的线性基可以从位置i-1的线性基转移而来
       - 将arr[i]插入到位置i-1的线性基中，得到位置i的线性基
    
    2. 区间查询：
       - 利用前缀线性基的性质，区间[l,r]的线性基可以通过prefixBasis[r]和prefixBasis[l-1]计算得到
       - 用prefixBasis[l-1]对prefixBasis[r]进行消元，得到区间[l,r]的线性基
       - 从高位到低位贪心地选择线性基中的元素来最大化结果
    """
    main()