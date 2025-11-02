# POJ 2443 Set Operation
# 题目链接: http://poj.org/problem?id=2443
# 题目大意:
# 给定N个集合，第i个集合S(i)有C(i)个元素（集合可以包含两个相同的元素）。
# 集合中的每个元素都用1~10000的正数表示。
# 查询两个给定元素i和j是否同时属于至少一个集合。
# 换句话说，确定是否存在一个数字k(1≤k≤N)，使得元素i和元素j都属于S(k)。
#
# 输入:
# 第一行包含一个整数N(1 <= N <= 1000)，表示集合的数量。
# 接下来N行，每行以数字C(i)(1 <= C(i) <= 10000)开始，然后是C(i)个数字，
# 这些数字用空格分隔，给出集合中的元素（这些C(i)个数字不需要彼此不同）。
# 第N+2行包含一个数字Q(1 <= Q <= 200000)，表示查询的数量。
# 然后是Q行。每行包含一对数字i和j(1 <= i, j <= 10000，i可以等于j)，
# 描述需要回答的元素。
#
# 输出:
# 对于每个查询，在一行中，如果存在这样的数字k，打印"Yes"；否则打印"No"。
#
# 解题思路:
# 使用bitset优化的方法:
# 1. 对于每个元素x，我们用一个bitset记录它在哪些集合中出现过
# 2. 对于查询(x,y)，我们检查vis[x] & vis[y]是否为0
#    如果不为0，说明存在至少一个集合同时包含x和y
# 时间复杂度: O(N*C + Q)  其中C是集合的平均大小
# 空间复杂度: O(10000 * N / 32) = O(312500) bit

# Python中没有内置的bitset，但我们可以使用set来模拟
# 或者使用第三方库bitarray

# 方法1: 使用set模拟bitset
# 利用Python的set数据结构来模拟bitset的功能
def solve_with_set():
    # 读取集合数量
    n = int(input())
    
    # vis[x]表示元素x在哪些集合中出现过
    # 使用set列表来存储每个元素在哪些集合中出现过
    vis = [set() for _ in range(10001)]
    
    # 读取每个集合
    for i in range(1, n + 1):
        # 读取一行输入并转换为整数列表
        line = list(map(int, input().split()))
        # 获取集合中元素的数量
        c = line[0]
        # 处理集合中的每个元素
        for j in range(1, c + 1):
            # 获取元素值
            x = line[j]
            # 将集合编号i添加到元素x的集合中
            # 表示元素x在第i个集合中出现过
            vis[x].add(i)
    
    # 处理查询
    # 读取查询数量
    q = int(input())
    # 存储查询结果
    results = []
    # 处理每个查询
    for _ in range(q):
        # 读取查询的两个元素
        x, y = map(int, input().split())
        
        # 检查是否存在一个集合同时包含x和y
        # 通过求交集检查是否有共同的集合
        # 如果vis[x]和vis[y]的交集不为空，说明存在至少一个集合同时包含x和y
        if vis[x] & vis[y]:  # 如果交集不为空
            results.append("Yes")
        else:
            results.append("No")
    
    # 输出所有查询结果
    for result in results:
        print(result)

# 方法2: 使用位运算模拟bitset
# 利用Python整数的位运算功能来模拟bitset
def solve_with_bitwise():
    # 读取集合数量
    n = int(input())
    
    # vis[x]表示元素x在哪些集合中出现过，使用整数的位来表示
    # 每个整数的第i位为1表示元素x在第i个集合中出现过
    vis = [0 for _ in range(10001)]
    
    # 读取每个集合
    for i in range(1, n + 1):
        # 读取一行输入并转换为整数列表
        line = list(map(int, input().split()))
        # 获取集合中元素的数量
        c = line[0]
        # 处理集合中的每个元素
        for j in range(1, c + 1):
            # 获取元素值
            x = line[j]
            # 使用位运算标记元素x在第i个集合中出现过
            # 1 << i 创建一个只有第i位为1的数
            # |= 按位或操作，将第i位设置为1
            vis[x] |= (1 << i)
    
    # 处理查询
    # 读取查询数量
    q = int(input())
    # 存储查询结果
    results = []
    # 处理每个查询
    for _ in range(q):
        # 读取查询的两个元素
        x, y = map(int, input().split())
        
        # 检查是否存在一个集合同时包含x和y
        # 通过按位与操作检查是否有共同的集合
        # vis[x] & vis[y] 计算两个整数的按位与结果
        # 如果结果不为0，说明存在至少一个集合同时包含x和y
        if vis[x] & vis[y]:  # 如果按位与结果不为0
            results.append("Yes")
        else:
            results.append("No")
    
    # 输出所有查询结果
    for result in results:
        print(result)

# 方法3: 使用bitarray库（如果安装了的话）
# 需要先安装: pip install bitarray
'''
from bitarray import bitarray

def solve_with_bitarray():
    # 读取集合数量
    n = int(input())
    
    # vis[x]表示元素x在哪些集合中出现过
    # 使用bitarray列表来存储每个元素在哪些集合中出现过
    vis = [bitarray(n + 1) for _ in range(10001)]
    # 初始化所有bitarray为全0
    for b in vis:
        b.setall(0)
    
    # 读取每个集合
    for i in range(1, n + 1):
        # 读取一行输入并转换为整数列表
        line = list(map(int, input().split()))
        # 获取集合中元素的数量
        c = line[0]
        # 处理集合中的每个元素
        for j in range(1, c + 1):
            # 获取元素值
            x = line[j]
            # 将第i位设置为1，表示元素x在第i个集合中出现过
            vis[x][i] = 1
    
    # 处理查询
    # 读取查询数量
    q = int(input())
    # 存储查询结果
    results = []
    # 处理每个查询
    for _ in range(q):
        # 读取查询的两个元素
        x, y = map(int, input().split())
        
        # 检查是否存在一个集合同时包含x和y
        # 通过按位与操作检查是否有共同的集合
        # 计算vis[x]和vis[y]的按位与结果
        intersection = vis[x] & vis[y]
        # 检查是否有任何位为1
        if intersection.any():  # 如果有任何位为1
            results.append("Yes")
        else:
            results.append("No")
    
    # 输出所有查询结果
    for result in results:
        print(result)
'''

# 程序入口点
if __name__ == "__main__":
    # 选择其中一种方法来解决问题
    # solve_with_set()      # 使用set模拟
    solve_with_bitwise()  # 使用位运算模拟bitset
    # solve_with_bitarray() # 使用bitarray库