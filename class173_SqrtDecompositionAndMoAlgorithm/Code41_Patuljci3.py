# PATULJCI - 众数查询问题 - 莫队算法实现 (Python版本)
# 题目来源: SPOJ
# 题目链接: https://www.spoj.com/problems/PATULJCI/
# 题目大意: 给定一个数组和多个查询，每个查询要求找出区间[l,r]内的众数（出现次数最多的元素）
# 如果存在多个众数，返回任意一个即可
# 约束条件: 数组长度n ≤ 3*10^5，查询次数q ≤ 3*10^5，元素值 ≤ n

import sys
import math
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    c = int(sys.stdin.readline())  # 元素种类数，题目中给出但实际不需要使用
    
    arr = [0] + list(map(int, sys.stdin.readline().split()))  # 1-indexed
    
    q = int(sys.stdin.readline())
    
    # 查询结构
    queries = []
    for i in range(1, q + 1):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 莫队算法处理
    # 块大小选择: sqrt(n)
    blen = int(math.sqrt(n))
    
    # 按块排序查询
    queries.sort(key=lambda x: (x[0] // blen, x[1]))
    
    # 初始化数据结构
    count = defaultdict(int)  # 计数数组，记录当前窗口中每个元素的出现次数
    freq = defaultdict(int)   # 频率数组，记录出现次数为i的元素个数
    max_freq = 0              # 当前窗口中的最大频率
    ans = [0] * (q + 1)
    
    # 添加元素到当前窗口
    def add(pos):
        nonlocal max_freq
        val = arr[pos]
        # 减少旧频率的计数
        freq[count[val]] -= 1
        # 增加元素计数
        count[val] += 1
        # 增加新频率的计数
        freq[count[val]] += 1
        # 更新最大频率
        if count[val] > max_freq:
            max_freq = count[val]
    
    # 从当前窗口移除元素
    def remove(pos):
        nonlocal max_freq
        val = arr[pos]
        # 减少旧频率的计数
        freq[count[val]] -= 1
        # 如果移除的元素是当前最大频率的元素，需要更新最大频率
        if count[val] == max_freq and freq[count[val]] == 0:
            max_freq -= 1
        # 减少元素计数
        count[val] -= 1
        # 增加新频率的计数
        freq[count[val]] += 1
    
    # 获取当前窗口的众数
    def get_mode():
        # 找到出现次数等于最大频率的任意元素
        for val, cnt in count.items():
            if cnt == max_freq:
                return val
        return -1  # 不应该到达这里
    
    # 莫队算法主循环
    cur_l, cur_r = 1, 0
    for l, r, idx in queries:
        # 扩展右边界
        while cur_r < r:
            cur_r += 1
            add(cur_r)
        
        # 收缩左边界
        while cur_l > l:
            cur_l -= 1
            add(cur_l)
        
        # 收缩右边界
        while cur_r > r:
            remove(cur_r)
            cur_r -= 1
        
        # 扩展左边界
        while cur_l < l:
            remove(cur_l)
            cur_l += 1
        
        # 记录答案
        ans[idx] = get_mode()
    
    # 输出结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()

"""
时间复杂度分析：
- 排序查询：O(q log q)
- 莫队算法主循环：
  - 指针移动的总次数：O((n + q) * sqrt(n))
  - 每次add/remove操作：O(1)
  - 获取众数操作：O(n)（最坏情况）
  - 总体时间复杂度：O(q log q + (n + q) * sqrt(n) + q * n)
- 由于q * n项可能较大，我们可以优化获取众数的操作

空间复杂度分析：
- 存储数组和查询：O(n + q)
- 计数数组：O(n)
- 频率数组：O(n)
- 总体空间复杂度：O(n + q)

优化说明：
1. 使用频率数组freq来维护出现次数为i的元素个数，可以快速更新最大频率
2. 在remove操作中，只有当移除的元素是当前最大频率的元素且没有其他相同频率的元素时，才需要减少最大频率
3. 块大小选择为sqrt(n)，这是经过理论分析得出的最优块大小

算法说明：
PATULJCI问题要求查询区间众数，可以使用莫队算法解决：
1. 将所有查询按左端点所在的块编号排序，块内按右端点排序
2. 使用莫队算法的指针移动技巧，维护当前窗口的元素计数
3. 使用计数数组和频率数组来快速获取众数

与其他方法的对比：
- 暴力法：每次查询O(n)，总时间复杂度O(q * n)
- 线段树：实现复杂，且对于众数查询不是最优解
- 莫队算法：离线处理，时间复杂度O((n + q) * sqrt(n))，适合此类问题

工程化考虑：
1. 使用sys.stdin.readline()提高输入效率
2. 使用defaultdict避免键不存在的检查
3. 对于大规模数据，可以考虑使用更快的输入方法
"""