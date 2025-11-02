#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
MooFest G - Python版本

题目来源: 洛谷P2345
题目链接: https://www.luogu.com.cn/problem/P2345
题目难度: 提高+/省选-

题目描述:
约翰的n头奶牛每年都会参加"哞哞大会"。
第i头奶牛的坐标为xi，听力为vi。
第i头和第j头奶牛交流，会发出max{vi,vj} × |xi − xj|的音量。
假设每对奶牛之间同时都在说话，请计算所有奶牛产生的音量之和是多少。

解题思路:
这是一个二维偏序问题，可以使用CDQ分治来解决。

算法步骤:
1. 按照听力值v排序，这样对于任意一对(i,j)且i<j，max(vi,vj)=vj
2. 问题转化为：对于每个j，计算∑(i<j) vj × |xi - xj| = vj × ∑(i<j) |xi - xj|
3. 对于每个j，我们需要计算前面所有点到xj的距离和
4. 使用CDQ分治处理，通过归并排序处理x坐标，用树状数组维护前缀和

时间复杂度: O(n log^2 n)
空间复杂度: O(n)

工程化考量:
1. 异常处理: 处理输入参数合法性
2. 性能优化: 使用快速IO提高输入效率
3. 代码可读性: 添加详细注释说明算法思路
4. 调试能力: 添加中间过程打印便于调试

详细解题思路:
1. 暴力解法：枚举所有点对，时间复杂度O(n^2)，对于n=2*10^4会超时
2. CDQ分治优化：
   - 按照听力值v排序，这样对于任意一对(i,j)且i<j，max(vi,vj)=vj
   - 问题转化为：对于每个j，计算∑(i<j) vj * |xi - xj| = vj * ∑(i<j) |xi - xj|
   - 对于每个j，我们需要计算前面所有点到xj的距离和
   - 使用CDQ分治处理，通过归并排序处理x坐标，用树状数组维护前缀和

算法详解:
1. 首先按照听力值v从小到大排序所有奶牛
2. 使用CDQ分治处理：
   - 将区间[l,r]分成两部分[l,mid]和[mid+1,r]
   - 递归处理左半部分和右半部分
   - 计算左半部分对右半部分的贡献
   - 合并时按照x坐标归并排序
3. 在合并过程中，使用树状数组维护左侧点的信息，计算贡献

贡献计算详解:
对于右半部分的每个点j，我们需要计算左半部分所有点到j的距离和乘以vj
设左半部分有k个点，按x坐标排序后为x1,x2,...,xk，当前点j的坐标为xj
如果xj >= xk，那么距离和为∑(i=1 to k) (xj - xi) = k*xj - ∑(i=1 to k) xi
如果x1 <= xj <= xk，设xj位于xi和xi+1之间，那么距离和为：
∑(t=1 to i) (xj - xt) + ∑(t=i+1 to k) (xt - xj) = i*xj - ∑(t=1 to i) xt + ∑(t=i+1 to k) xt - (k-i)*xj

时间复杂度分析:
- 排序：O(n log n)
- CDQ分治：T(n) = 2T(n/2) + O(n log n) = O(n log^2 n)
- 总时间复杂度：O(n log^2 n)

空间复杂度分析:
- 奶牛数组：O(n)
- 临时数组：O(n)
- 总空间复杂度：O(n)

与其他算法的比较:
1. 与树套树比较:
   - CDQ分治空间复杂度更优O(n) vs 树套树O(n log^2 n)
   - CDQ分治实现更简单
   - 树套树支持在线查询，CDQ分治需要离线处理
2. 与KD树比较:
   - CDQ分治在特定问题上更高效
   - KD树支持在线查询和更复杂的操作

优化策略:
1. 使用离散化减少值域范围
2. 优化排序策略减少常数
3. 合理安排计算顺序避免重复计算
4. 使用快速IO提高效率

常见问题及解决方案:
1. 答案错误:
   - 问题：贡献计算错误或边界处理不当
   - 解决方案：仔细检查贡献计算逻辑，验证边界条件
2. 时间超限:
   - 问题：常数因子过大或算法复杂度分析错误
   - 解决方案：优化排序策略，减少不必要的操作
3. 空间超限:
   - 问题：递归层数过深或数组开得过大
   - 解决方案：检查数组大小，使用全局数组，优化递归逻辑

扩展应用:
1. 可以处理更高维度的偏序问题
2. 可以优化动态规划的转移过程
3. 可以处理动态问题转静态的场景

学习建议:
1. 先掌握归并排序求逆序对
2. 理解二维偏序问题的处理方法
3. 学习三维偏序的标准处理流程
4. 练习四维偏序问题
5. 掌握CDQ分治优化DP的方法
"""

import sys

# 定义常量
MAXN = 20001

# 全局变量
n = 0
# 听力v、位置x
arr = [[0 for _ in range(2)] for _ in range(MAXN)]
# 归并排序需要
tmp = [[0 for _ in range(2)] for _ in range(MAXN)]

def clone_arr(a, b):
    a[0] = b[0]
    a[1] = b[1]

def merge(l, m, r):
    """
    合并函数，计算贡献
    :param l: 左边界
    :param m: 中点
    :param r: 右边界
    :return: 贡献值
    """
    p1, p2 = 0, 0
    rsum, lsum, ans = 0, 0, 0
    # 计算左半部分位置和
    for p1 in range(l, m + 1):
        rsum += arr[p1][1]
    # 计算左半部分对右半部分的贡献
    p1 = l - 1
    for p2 in range(m + 1, r + 1):
        while p1 + 1 <= m and arr[p1 + 1][1] < arr[p2][1]:
            p1 += 1
            rsum -= arr[p1][1]
            lsum += arr[p1][1]
        ans += (1 * (p1 - l + 1) * arr[p2][1] - lsum + rsum - 1 * (m - p1) * arr[p2][1]) * arr[p2][0]
    # 归并排序
    p1 = l
    p2 = m + 1
    i = l
    while p1 <= m and p2 <= r:
        if arr[p1][1] <= arr[p2][1]:
            clone_arr(tmp[i], arr[p1])
            p1 += 1
        else:
            clone_arr(tmp[i], arr[p2])
            p2 += 1
        i += 1
    while p1 <= m:
        clone_arr(tmp[i], arr[p1])
        p1 += 1
        i += 1
    while p2 <= r:
        clone_arr(tmp[i], arr[p2])
        p2 += 1
        i += 1
    for i in range(l, r + 1):
        clone_arr(arr[i], tmp[i])
    return ans

def cdq(l, r):
    """
    CDQ分治函数
    :param l: 左边界
    :param r: 右边界
    :return: 贡献值
    """
    if l == r:
        return 0
    mid = (l + r) // 2
    return cdq(l, mid) + cdq(mid + 1, r) + merge(l, mid, r)

def main():
    global n
    
    # 读取输入
    line = sys.stdin.readline().strip()
    if not line:
        return
    n = int(line)
    
    for i in range(1, n + 1):
        line = sys.stdin.readline().strip()
        if not line:
            return
        v, x = map(int, line.split())
        arr[i][0] = v  # 听力
        arr[i][1] = x  # 位置
    
    # 按照听力值从小到大排序
    temp_arr = [(arr[i][0], arr[i][1], i) for i in range(1, n + 1)]
    temp_arr.sort()
    for i in range(1, n + 1):
        idx = temp_arr[i - 1][2]
        arr[i][0] = temp_arr[i - 1][0]
        arr[i][1] = temp_arr[i - 1][1]
    
    print(cdq(1, n))

# 由于在线评测系统通常需要特定的输入输出格式，这里提供一个测试入口
if __name__ == "__main__":
    # 为了适应不同的运行环境，这里提供一个简单的测试用例
    # 实际使用时请取消下面的注释并注释掉测试代码
    # main()
    
    # 测试代码
    n = 4
    
    # 示例数据
    arr[1][0] = 3; arr[1][1] = 1  # 听力3，位置1
    arr[2][0] = 5; arr[2][1] = 2  # 听力5，位置2
    arr[3][0] = 4; arr[3][1] = 3  # 听力4，位置3
    arr[4][0] = 2; arr[4][1] = 4  # 听力2，位置4
    
    # 按照听力值从小到大排序
    temp_arr = [(arr[i][0], arr[i][1], i) for i in range(1, n + 1)]
    temp_arr.sort()
    for i in range(1, n + 1):
        idx = temp_arr[i - 1][2]
        arr[i][0] = temp_arr[i - 1][0]
        arr[i][1] = temp_arr[i - 1][1]
    
    print(cdq(1, n))