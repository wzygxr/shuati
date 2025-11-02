# POJ 3275 Ranking the Cows
# 题目链接: http://poj.org/problem?id=3275
# 题目大意:
# FJ想按照奶牛产奶的能力给她们排序。现在已知有N头奶牛（1 ≤ N ≤ 1,000）。
# FJ通过比较，已经知道了M（1 ≤ M ≤ 10,000）对相对关系。
# 问你最少还要确定多少对牛的关系才能将所有的牛按照一定顺序排序起来。

# 解题思路:
# 1. 这是一个传递闭包问题
# 2. 使用Floyd算法求传递闭包
# 3. 使用整数位运算优化Floyd算法
# 4. 统计已知关系数，用完全图的关系数减去已知关系数就是答案
# 时间复杂度: O(N^3/32)
# 空间复杂度: O(N^2/32)

def main():
    """主函数，处理输入并输出结果"""
    # 读取输入
    # n表示奶牛的数量，m表示已知的关系对数
    n, m = map(int, input().split())
    
    # 使用整数数组模拟bitset优化的邻接矩阵
    # graph[i]表示第i头牛能到达的牛的集合，用整数的位来表示
    # 例如：graph[i]的第j位为1表示第i头牛能到达第j头牛
    graph = [0] * (n + 1)
    
    # 读取已知的M对关系
    # 每一对关系表示a > b，即a到b有一条有向边
    for _ in range(m):
        # 读取一对关系a > b
        a, b = map(int, input().split())
        # a > b，即a到b有一条有向边
        # 在graph[a]中将b的位置为1，表示a能到达b
        # 1 << b 创建一个只有第b位为1的数
        # |= 按位或操作，将第b位设置为1
        graph[a] |= (1 << b)
    
    # Floyd求传递闭包，使用位运算优化
    # 通过Floyd算法计算所有奶牛之间的可达关系
    # 枚举中间节点k
    for k in range(1, n + 1):
        # 枚举起点i
        for i in range(1, n + 1):
            # 如果i到k有路径，则i到k能到达的所有点，i也能到达
            # graph[i] & (1 << k) 检查第k位是否为1，即i是否能到达k
            if graph[i] & (1 << k):
                # graph[i] |= graph[k] 将graph[i]与graph[k]按位或
                # 这表示i能到达k能到达的所有点
                graph[i] |= graph[k]
    
    # 统计已知关系数
    # 计算所有已知的奶牛之间的关系对数
    known = 0
    # 遍历每头牛
    for i in range(1, n + 1):
        # 统计第i头牛能到达的牛的数量
        # bin(graph[i])将整数转换为二进制字符串
        # .count('1')统计字符串中'1'的个数
        known += bin(graph[i]).count('1')
    
    # 完全图的关系数是n*(n-1)/2
    # 答案是还需要确定的关系数
    # 完全图有n*(n-1)/2对关系，减去已知的关系数就是还需要确定的关系数
    result = n * (n - 1) // 2 - known
    # 输出结果
    print(result)

# 测试用例
def test():
    """测试用例"""
    print("POJ 3275 Ranking the Cows 解题测试")
    # 由于这是在线评测题目，测试用例需要按照题目要求构造

# 程序入口点
if __name__ == "__main__":
    # 运行测试用例
    test()
    
    # 如果需要运行主程序，取消下面的注释
    # main()