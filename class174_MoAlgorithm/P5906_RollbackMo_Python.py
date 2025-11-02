# 回滚莫队模板 (回滚莫队应用 - 最远间隔距离)
# 题目来源: 洛谷P5906 【模板】回滚莫队&不删除莫队
# 题目链接: https://www.luogu.com.cn/problem/P5906
# 题意: 给定一个序列，多次询问一段区间 [l,r]，求区间中相同的数的最远间隔距离。序列中两个元素的间隔距离指的是两个元素下标差的绝对值。
# 算法思路: 使用回滚莫队算法，适用于只能添加不能删除或者只能删除不能添加的区间问题
# 时间复杂度: O((n + q) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间最远间隔距离查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n = int(sys.stdin.readline())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    q = int(sys.stdin.readline())
    queries = []
    for i in range(q):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 回滚莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    last_pos = defaultdict(lambda: -1)  # 记录每个值最后出现的位置
    max_dist = defaultdict(int)  # 记录每个值当前的最大间隔
    global_max = 0  # 全局最大间隔
    results = [0] * q  # 存储结果
    
    # 初始化位置数组
    def init_positions():
        nonlocal global_max
        last_pos.clear()
        max_dist.clear()
        global_max = 0
    
    # 添加元素
    def add(pos):
        nonlocal global_max
        val = arr[pos]
        if last_pos[val] != -1:
            dist = pos - last_pos[val]
            max_dist[val] = max(max_dist[val], dist)
            global_max = max(global_max, max_dist[val])
        last_pos[val] = pos
    
    # 删除元素（不更新答案，用于回滚）
    def remove_without_update(pos):
        # 在回滚莫队中，我们不真正删除元素，而是通过状态恢复来实现
        pass
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 如果左右端点在同一块内，暴力计算
        if (l - 1) // block_size == (r - 1) // block_size:
            init_positions()
            temp_max = 0
            for i in range(l, r + 1):
                val = arr[i]
                if last_pos[val] != -1:
                    dist = i - last_pos[val]
                    temp_max = max(temp_max, dist)
                last_pos[val] = i
            results[idx] = temp_max
            continue
        
        # 初始化状态
        init_positions()
        
        # 扩展右边界到R
        while cur_r < r:
            cur_r += 1
            add(cur_r)
        
        # 保存当前状态
        saved_r = cur_r
        saved_global_max = global_max
        
        # 收缩左边界到L
        while cur_l < l:
            remove_without_update(cur_l)
            cur_l += 1
        
        results[idx] = global_max
        
        # 恢复状态
        init_positions()
        while cur_l > ((l - 1) // block_size) * block_size + 1:
            cur_l -= 1
            add(cur_l)
        
        # 恢复右边界
        while cur_r > saved_r:
            remove_without_update(cur_r)
            cur_r -= 1
        global_max = saved_global_max
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()