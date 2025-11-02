"""
洛谷 P4231 三步必杀 (Three Steps Kill)

题目描述:
一开始1~n范围上的数字都是0，一共有m个操作，每次操作为(l,r,s,e,d)
表示在l~r范围上依次加上首项为s、末项为e、公差为d的数列
m个操作做完之后，统计1~n范围上所有数字的最大值和异或和

测试链接 : https://www.luogu.com.cn/problem/P4231

相关题目:
1. 洛谷 P4231 三步必杀 (Three Steps Kill)
   链接: https://www.luogu.com.cn/problem/P4231
   题目描述: 在1~n范围上的数字初始都为0，有m个操作，每次操作为(l,r,s,e,d)，
            表示在l~r范围上依次加上首项为s、末项为e、公差为d的等差数列。
            所有操作完成后，统计1~n范围上所有数字的最大值和异或和。

2. 洛谷 P5026 Lycanthropy
   链接: https://www.luogu.com.cn/problem/P5026
   题目描述: 朋友落水后，会对水面产生影响，形成特定的水位分布。
            需要使用二阶差分来处理这种复杂的区间更新问题。

3. Codeforces 296C Greg and Array
   链接: https://codeforces.com/contest/296/problem/C
   题目描述: 给定一个数组和一系列操作，每个操作是对数组的一个区间进行加法操作。
            然后给定一些指令，每个指令指定执行哪些操作各多少次。
            最后输出执行完所有指令后的数组。

等差数列差分核心思想:
对于等差数列的区间更新，我们需要使用二阶差分。
一阶差分数组 b 定义为: b[i] = a[i] - a[i-1]
二阶差分数组 c 定义为: c[i] = b[i] - b[i-1] = a[i] - 2*a[i-1] + a[i-2]

对于在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列:
原数组变化: a[l] += s, a[l+1] += s+d, ..., a[r] += e
一阶差分变化: b[l] += s, b[l+1] += d, ..., b[r+1] -= (e+d), b[r+2] += e
二阶差分变化: c[l] += s, c[l+1] += d-s, c[r+1] -= d+e, c[r+2] += e

但在这个实现中，我们直接使用一阶差分，通过特定的公式来处理等差数列:
arr[l] += s;
arr[l + 1] += d - s;
arr[r + 1] -= d + e;
arr[r + 2] += e;

时间复杂度分析:
每次操作: O(1)
构造结果数组(两次前缀和): O(n)
总时间复杂度: O(m + n)

空间复杂度分析:
需要额外的数组空间: O(n)

这是最优解，因为:
1. 需要处理所有操作，无法避免O(m)的时间复杂度
2. 使用差分数组将区间更新操作从O(n)优化到O(1)
3. 最终需要计算所有元素，需要O(n)时间
"""


def main():
    """
    主函数，处理输入并输出结果
    
    解题思路:
    1. 使用差分数组技巧处理等差数列的区间更新
    2. 对于每次操作(l,r,s,e,d)，使用特殊的差分标记方式
    3. 通过两次前缀和操作得到最终数组
    4. 计算最大值和异或和
    
    时间复杂度: O(m + n) - 需要处理所有操作和数组两次
    空间复杂度: O(n) - 需要存储差分数组
    
    工程化考量:
    1. 输入处理: 使用高效的输入处理方式
    2. 边界处理: 确保数组索引不越界
    3. 性能优化: 使用差分数组避免重复计算
    4. 输出格式: 按照题目要求输出结果
    """
    try:
        # 读取输入
        line = input().split()
        n = int(line[0])
        m = int(line[1])
        
        # 初始化差分数组
        arr = [0] * (n + 3)  # 多分配一些空间处理边界情况
        
        # 处理m个操作
        for _ in range(m):
            l, r, s, e = map(int, input().split())
            # 计算公差
            d = (e - s) // (r - l)
            # 对区间[l,r]加上首项为s、末项为e、公差为d的等差数列
            set_diff(arr, l, r, s, e, d)
        
        # 通过两次前缀和操作构建最终结果数组
        build(arr, n)
        
        # 计算最大值和异或和
        max_val = 0
        xor_sum = 0
        for i in range(1, n + 1):
            max_val = max(max_val, arr[i])
            xor_sum ^= arr[i]
        
        # 输出结果
        print(xor_sum, max_val)
        
    except EOFError:
        pass


def set_diff(arr, l, r, s, e, d):
    """
    在区间[l,r]上加上首项为s、末项为e、公差为d的等差数列
    这是等差数列差分的核心操作
    
    Args:
        arr: 差分数组
        l: 区间起始位置
        r: 区间结束位置
        s: 首项
        e: 末项
        d: 公差
    """
    # 差分数组更新
    # arr[l] += s 表示从位置l开始增加首项s
    arr[l] += s
    # arr[l + 1] += d - s 表示从位置l+1开始相对于前一个位置增加公差d
    arr[l + 1] += d - s
    # arr[r + 1] -= d + e 表示从位置r+1开始减少(d+e)
    arr[r + 1] -= d + e
    # arr[r + 2] += e 表示从位置r+2开始增加e
    arr[r + 2] += e


def build(arr, n):
    """
    通过两次前缀和操作构建最终结果数组
    
    Args:
        arr: 差分数组
        n: 数组长度
    """
    # 第一次前缀和，得到一阶差分的结果
    for i in range(1, n + 1):
        arr[i] += arr[i - 1]
    
    # 第二次前缀和，得到最终结果
    for i in range(1, n + 1):
        arr[i] += arr[i - 1]


if __name__ == "__main__":
    main()