# MKTHNUM - 第K小数字查询问题 - 莫队算法实现 (Python版本)
# 题目来源: SPOJ
# 题目链接: https://www.spoj.com/problems/MKTHNUM/
# 题目大意: 给定一个数组和多个查询，每个查询要求找出区间[l,r]内第k小的数字
# 约束条件: 数组长度n ≤ 10^5，查询次数q ≤ 5*10^3

import sys
import math
from collections import defaultdict

def main():
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    
    arr = [0] + list(map(int, sys.stdin.readline().split()))  # 1-indexed
    sorted_arr = sorted(set(arr[1:]))  # 去重并排序用于离散化
    unique_count = len(sorted_arr)
    
    # 创建值到索引的映射
    value_to_index = {val: idx for idx, val in enumerate(sorted_arr, 1)}
    
    # 重新映射数组元素到离散化后的值
    for i in range(1, n + 1):
        arr[i] = value_to_index[arr[i]]
    
    # 查询结构
    queries = []
    for i in range(1, q + 1):
        l, r, k = map(int, sys.stdin.readline().split())
        queries.append((l, r, k, i))
    
    # 莫队算法处理
    # 块大小选择: sqrt(n)
    blen = int(math.sqrt(n))
    
    # 按块排序查询
    queries.sort(key=lambda x: (x[0] // blen, x[1]))
    
    # 初始化数据结构
    count = defaultdict(int)  # 计数数组，记录当前窗口中每个元素的出现次数
    ans = [0] * (q + 1)
    
    # 添加元素到当前窗口
    def add(pos):
        count[arr[pos]] += 1
    
    # 从当前窗口移除元素
    def remove(pos):
        count[arr[pos]] -= 1
        if count[arr[pos]] == 0:
            del count[arr[pos]]
    
    # 获取当前窗口第k小的数字
    def get_kth(k):
        cnt = 0
        # 按照离散化后的顺序遍历，找到第k小的数字
        for idx in range(1, unique_count + 1):
            cnt += count.get(idx, 0)
            if cnt >= k:
                return sorted_arr[idx - 1]  # 返回原始值
        return -1  # 不应该到达这里
    
    # 莫队算法主循环
    cur_l, cur_r = 1, 0
    for l, r, k, idx in queries:
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
        ans[idx] = get_kth(k)
    
    # 输出结果
    for i in range(1, q + 1):
        print(ans[i])

if __name__ == "__main__":
    main()

"""
时间复杂度分析：
- 排序查询：O(q log q)
- 离散化：O(n log n)
- 莫队算法主循环：
  - 指针移动的总次数：O((n + q) * sqrt(n))
  - 每次add/remove操作：O(1)
  - 获取第k小数字操作：O(n)（最坏情况）
  - 总体时间复杂度：O(q log q + n log n + (n + q) * sqrt(n) + q * n)

空间复杂度分析：
- 存储数组和查询：O(n + q)
- 计数数组：O(n)
- 总体空间复杂度：O(n + q)

优化说明：
1. 使用离散化技术减少值域大小，提高效率
2. 块大小选择为sqrt(n)，这是经过理论分析得出的最优块大小

算法说明：
MKTHNUM问题要求查询区间第k小数字，可以使用莫队算法解决：
1. 将所有查询按左端点所在的块编号排序，块内按右端点排序
2. 使用莫队算法的指针移动技巧，维护当前窗口的元素计数
3. 通过遍历离散化后的数组来获取第k小数字

与其他方法的对比：
- 暴力法：每次查询O(n log n)，总时间复杂度O(q * n log n)
- 主席树：在线查询，每次查询O(log n)，预处理O(n log n)
- 莫队算法：离线处理，时间复杂度O((n + q) * sqrt(n) + q * n)，适合此类问题

工程化考虑：
1. 使用sys.stdin.readline()提高输入效率
2. 使用defaultdict避免键不存在的检查
3. 使用set去重和排序进行离散化
4. 对于大规模数据，可以考虑使用更快的输入方法
"""