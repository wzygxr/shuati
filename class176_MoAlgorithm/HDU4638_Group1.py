# HDU 4638 Group - Python版本
# 题目来源: HDU 4638 Group
# 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=4638
# 题目大意: 给定一个序列，序列由1-N个元素全排列而成，求任意区间连续的段数
# 例如序列2,3,5,6,9就是三段(2, 3) (5, 6)(9)
# 数据范围: 1 <= n, m <= 100000
# 解题思路: 使用普通莫队算法处理区间查询问题
# 时间复杂度: O((n + m) * sqrt(n))
# 空间复杂度: O(n)
# 相关题目:
# 1. HDU 4638 Group: http://acm.hdu.edu.cn/showproblem.php?pid=4638
# 2. 牛客网暑期ACM多校训练营 J Different Integers: https://www.nowcoder.com/acm/contest/139/J
# 3. SPOJ DQUERY - D-query: https://www.spoj.com/problems/DQUERY/
# 4. 洛谷 P2709 小B的询问: https://www.luogu.com.cn/problem/P2709
# 5. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列: https://www.luogu.com.cn/problem/P1903

import sys
import math
from collections import defaultdict

def main():
    # 读取测试用例数
    T = int(sys.stdin.readline())
    
    for _ in range(T):
        # 读取n和m
        n, m = map(int, sys.stdin.readline().split())
        
        # 读取序列
        arr = [0] + list(map(int, sys.stdin.readline().split()))  # 下标从1开始
        
        # 记录每个数字的位置
        pos = [0] * (n + 1)
        for i in range(1, n + 1):
            pos[arr[i]] = i
        
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
    vis = [False] * (n + 1)  # vis[i]表示数字i是否在当前区间中
    ans = [0] * (m + 1)  # 答案数组
    
    for _, _, l, r, idx in indexed_queries:
        # 扩展右端点
        while current_r < r:
            current_r += 1
            val = arr[current_r]
            vis[val] = True
            
            # 如果val-1在区间中，说明连接了两个段
            if val > 1 and vis[val - 1]:
                current_ans -= 1
            
            # 如果val+1在区间中，说明连接了两个段
            if val < n and vis[val + 1]:
                current_ans -= 1
            
            # 添加一个新的段
            current_ans += 1
        
        # 收缩右端点
        while current_r > r:
            val = arr[current_r]
            vis[val] = False
            
            # 移除一个段
            current_ans -= 1
            
            # 如果val-1在区间中，说明断开了两个段
            if val > 1 and vis[val - 1]:
                current_ans += 1
            
            # 如果val+1在区间中，说明断开了两个段
            if val < n and vis[val + 1]:
                current_ans += 1
            
            current_r -= 1
        
        # 扩展左端点
        while current_l > l:
            current_l -= 1
            val = arr[current_l]
            vis[val] = True
            
            # 如果val-1在区间中，说明连接了两个段
            if val > 1 and vis[val - 1]:
                current_ans -= 1
            
            # 如果val+1在区间中，说明连接了两个段
            if val < n and vis[val + 1]:
                current_ans -= 1
            
            # 添加一个新的段
            current_ans += 1
        
        # 收缩左端点
        while current_l < l:
            val = arr[current_l]
            vis[val] = False
            
            # 移除一个段
            current_ans -= 1
            
            # 如果val-1在区间中，说明断开了两个段
            if val > 1 and vis[val - 1]:
                current_ans += 1
            
            # 如果val+1在区间中，说明断开了两个段
            if val < n and vis[val + 1]:
                current_ans += 1
            
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
2. 维护当前区间中连续数字的段数
3. 当添加一个数字时，检查它是否能与相邻数字连接成段
4. 当删除一个数字时，检查它是否断开了原有的段

核心思想:
1. 对于每个数字，我们维护它是否在当前区间中
2. 当添加数字val时：
   - 如果val-1在区间中，两个段合并，段数减1
   - 如果val+1在区间中，两个段合并，段数减1
   - 添加一个新的段，段数加1
3. 当删除数字val时：
   - 移除一个段，段数减1
   - 如果val-1在区间中，两个段分离，段数加1
   - 如果val+1在区间中，两个段分离，段数加1

工程化考量:
1. 使用快速输入输出优化IO性能
2. 合理使用静态数组避免动态分配
3. 使用布尔数组记录数字是否在区间中

调试技巧:
1. 可以通过打印中间结果验证算法正确性
2. 使用断言检查关键变量的正确性
3. 对比暴力算法验证结果
'''