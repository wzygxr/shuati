# Points on Plane问题 - 二维前缀和实现 (Python版本)
# 题目来源: https://codeforces.com/problemset/problem/1181/C
# 题目大意: 给定一个N×N的网格，每个格点上有一些点，多次查询矩形区域内点的数量
# 约束条件: 1 <= N <= 10^3, 1 <= Q <= 10^5

# 定义最大网格大小
MAXN = 1005

# 全局变量
n, q = 0, 0

# arr: 原始网格数据
arr = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

# sum_arr: 二维前缀和数组
sum_arr = [[0 for _ in range(MAXN)] for _ in range(MAXN)]

def build():
    """
    构建二维前缀和数组
    时间复杂度: O(N^2)
    设计思路: 利用二维前缀和公式计算每个位置的前缀和
    sum[i][j] = arr[i][j] + sum[i-1][j] + sum[i][j-1] - sum[i-1][j-1]
    这样可以避免重复计算，提高查询效率
    """
    # 遍历网格的每个位置
    for i in range(1, n + 1):
        for j in range(1, n + 1):
            # 应用二维前缀和公式
            sum_arr[i][j] = arr[i][j] + sum_arr[i - 1][j] + sum_arr[i][j - 1] - sum_arr[i - 1][j - 1]

def query(r1, c1, r2, c2):
    """
    查询矩形区域[r1,c1]到[r2,c2]内点的数量
    时间复杂度: O(1)
    设计思路: 利用二维前缀和数组快速计算矩形区域和
    通过容斥原理计算矩形区域和：
    区域和 = sum[r2][c2] - sum[r1-1][c2] - sum[r2][c1-1] + sum[r1-1][c1-1]
    参数:
        r1: 起始行
        c1: 起始列
        r2: 结束行
        c2: 结束列
    返回:
        矩形区域内点的数量
    """
    # 应用容斥原理计算矩形区域和
    return sum_arr[r2][c2] - sum_arr[r1 - 1][c2] - sum_arr[r2][c1 - 1] + sum_arr[r1 - 1][c1 - 1]

def main():
    global n, q
    
    # 读取网格大小n和查询次数q
    line = input().split()
    n = int(line[0])
    q = int(line[1])
    
    # 读取网格数据
    for i in range(1, n + 1):
        temp = list(map(int, input().split()))
        for j in range(1, n + 1):
            arr[i][j] = temp[j - 1]

    # 构建前缀和数组
    build()

    # 处理q次查询
    for i in range(1, q + 1):
        r1, c1, r2, c2 = map(int, input().split())
        # 输出查询结果
        print(query(r1, c1, r2, c2))

if __name__ == "__main__":
    main()