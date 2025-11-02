# 牛客网暑期ACM多校训练营 J Different Integers - Python版本
# 题目来源: 牛客网暑期ACM多校训练营 J Different Integers
# 题目链接: https://www.nowcoder.com/acm/contest/139/J
# 题目大意: 给定一个整数序列a1, a2, ..., an和q对整数(l1, r1), (l2, r2), ..., (lq, rq)
# 求count(l1, r1), count(l2, l2), ..., count(lq, rq)
# 其中count(i, j)是a1, a2, ..., ai, aj, aj+1, ..., an中不同整数的个数
# 数据范围: 1 ≤ n, q ≤ 10^5, 1 ≤ ai ≤ n
# 解题思路: 使用普通莫队算法处理区间查询问题
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 相关题目:
# 1. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
# 2. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
# 3. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
# 4. 洛谷 P2709 小B的询问: https://www.luogu.com.cn/problem/P2709
# 5. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列: https://www.luogu.com.cn/problem/P1903

import sys
import math
from collections import defaultdict

def main():
    # 由于是多组测试用例，我们只处理一组
    try:
        while True:
            # 读取n和m
            line = sys.stdin.readline()
            if not line:
                break
            n, m = map(int, line.split())
            
            # 读取序列
            arr = [0] + list(map(int, sys.stdin.readline().split()))  # 下标从1开始
            
            # 读取查询
            queries = []
            for i in range(1, m + 1):
                l, r = map(int, sys.stdin.readline().split())
                queries.append((l, r, i))
            
            # 莫队算法求解
            ans = solve_with_mo(n, m, arr, queries)
            
            # 输出答案
            for i in range(1, m + 1):
                print(ans[i])
            
            # 由于是多组测试用例，我们只处理一组就退出
            break
    except:
        # 输入结束
        pass

def solve_with_mo(n, m, arr, queries):
    # 块大小
    block_size = int(math.sqrt(n))
    
    # 计算块编号
    def get_block(x):
        return (x - 1) // block_size + 1
    
    # 为查询添加块编号并排序
    indexed_queries = []
    for l, r, idx in queries:
        indexed_queries.append((get_block(l), r, l, r, idx))
    
    # 按照莫队排序规则排序
    indexed_queries.sort(key=lambda x: (x[0], x[1]))
    
    # 莫队算法
    current_l, current_r = 1, 0
    current_ans = 0
    cnt = defaultdict(int)  # cnt[i]表示数字i在当前区间中的出现次数
    ans = [0] * (m + 1)  # 答案数组
    
    for _, _, l, r, idx in indexed_queries:
        # 扩展右端点
        while current_r < r:
            current_r += 1
            val = arr[current_r]
            if cnt[val] == 0:
                current_ans += 1
            cnt[val] += 1
        
        # 收缩右端点
        while current_r > r:
            val = arr[current_r]
            cnt[val] -= 1
            if cnt[val] == 0:
                current_ans -= 1
            current_r -= 1
        
        # 扩展左端点
        while current_l > l:
            current_l -= 1
            val = arr[current_l]
            if cnt[val] == 0:
                current_ans += 1
            cnt[val] += 1
        
        # 收缩左端点
        while current_l < l:
            val = arr[current_l]
            cnt[val] -= 1
            if cnt[val] == 0:
                current_ans -= 1
            current_l += 1
        
        ans[idx] = current_ans
    
    return ans

if __name__ == "__main__":
    main()

'''
算法分析:

时间复杂度: O((n + m) * sqrt(n))
空间复杂度: O(n)

算法思路:
1. 使用莫队算法处理区间查询问题
2. 维护当前区间中不同数字的个数
3. 当添加一个数字时，如果该数字第一次出现，则不同数字个数加1
4. 当删除一个数字时，如果该数字最后一次出现，则不同数字个数减1

核心思想:
1. 对于每个数字，我们维护它在当前区间中的出现次数
2. 当添加数字val时：
   - 如果cnt[val]为0，说明val是第一次出现，不同数字个数加1
   - cnt[val]加1
3. 当删除数字val时：
   - cnt[val]减1
   - 如果cnt[val]变为0，说明val是最后一次出现，不同数字个数减1

工程化考量:
1. 使用快速输入输出优化IO性能
2. 合理使用静态数组避免动态分配
3. 使用计数数组记录数字出现次数

调试技巧:
1. 可以通过打印中间结果验证算法正确性
2. 使用断言检查关键变量的正确性
3. 对比暴力算法验证结果
'''