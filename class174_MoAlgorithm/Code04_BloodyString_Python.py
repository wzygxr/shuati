# 大爷的字符串题 (普通莫队应用 - 区间众数)
# 题目来源: 洛谷P3709 大爷的字符串题
# 题目链接: https://www.luogu.com.cn/problem/P3709
# 题意: 给定一个长度为n的数组arr，一共有m条查询，查询 l r : arr[l..r]范围上，众数出现了几次，打印次数的相反数
# 算法思路: 使用普通莫队算法，通过分块和双指针技术优化区间查询，维护众数出现次数
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 适用场景: 区间众数出现次数查询问题

import math
import sys
from collections import defaultdict

def main():
    # 读取输入
    n, m = map(int, sys.stdin.readline().split())
    arr = list(map(int, sys.stdin.readline().split()))
    
    # 为了方便处理，将数组下标从1开始
    arr = [0] + arr
    
    queries = []
    for i in range(m):
        l, r = map(int, sys.stdin.readline().split())
        queries.append((l, r, i))
    
    # 莫队算法实现
    block_size = int(math.sqrt(n))
    
    # 为查询排序
    def mo_cmp(query):
        l, r, idx = query
        return (l // block_size, r)
    
    queries.sort(key=mo_cmp)
    
    # 初始化变量
    # cnt1[i] = j，表示窗口内，数字i出现了j次
    # cnt2[i] = j，表示窗口内，出现了i次的数，有j种
    # max_cnt，表示窗口内，众数的次数
    cnt1 = defaultdict(int)
    cnt2 = defaultdict(int)
    max_cnt = [0]  # 使用列表包装以在内部函数中修改
    results = [0] * m  # 存储结果
    
    # 删除元素
    def delete(num):
        if cnt1[num] == max_cnt[0] and cnt2[cnt1[num]] == 1:
            max_cnt[0] -= 1
        cnt2[cnt1[num]] -= 1
        cnt1[num] -= 1
        cnt2[cnt1[num]] += 1
    
    # 添加元素
    def add(num):
        cnt2[cnt1[num]] -= 1
        cnt1[num] += 1
        cnt2[cnt1[num]] += 1
        max_cnt[0] = max(max_cnt[0], cnt1[num])
    
    # 处理查询
    cur_l, cur_r = 1, 0
    
    for l, r, idx in queries:
        # 扩展右边界
        while cur_r < r:
            cur_r += 1
            add(arr[cur_r])
        
        # 收缩右边界
        while cur_r > r:
            delete(arr[cur_r])
            cur_r -= 1
        
        # 收缩左边界
        while cur_l < l:
            delete(arr[cur_l])
            cur_l += 1
        
        # 扩展左边界
        while cur_l > l:
            cur_l -= 1
            add(arr[cur_l])
        
        results[idx] = -max_cnt[0]  # 打印次数的相反数
    
    # 输出结果
    for result in results:
        print(result)

if __name__ == "__main__":
    main()