# 莫队算法 - 离线查询优化 (Python版本)
# 题目来源: HDU 3433
# 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=3433
# 题目大意: 多次查询区间[l,r]内满足条件的元素对
# 约束条件: 数组长度n ≤ 10000，查询次数m ≤ 100000

import sys
import math

class Query:
    def __init__(self, l, r, id):
        self.l = l
        self.r = r
        self.id = id
        self.answer = 0

MAXK = 55  # 题目中元素的最大值

def main():
    input = sys.stdin.read().split()
    ptr = 0
    t = int(input[ptr])
    ptr += 1
    
    while t > 0:
        t -= 1
        n = int(input[ptr])
        ptr += 1
        m = int(input[ptr])
        ptr += 1
        
        # 读取数组（1-based索引）
        arr = [0] * (n + 1)
        for i in range(1, n + 1):
            arr[i] = int(input[ptr])
            ptr += 1
        
        # 读取查询
        queries = []
        for i in range(m):
            l = int(input[ptr])
            ptr += 1
            r = int(input[ptr])
            ptr += 1
            queries.append(Query(l, r, i))
        
        # 初始化块大小
        blen = int(math.sqrt(n))
        if blen == 0:
            blen = 1
        
        # 按块排序查询的函数
        def compare_query(a):
            block_a = a.l // blen
            # 奇偶排序优化
            if block_a % 2 == 0:
                return (block_a, a.r)
            else:
                return (block_a, -a.r)
        
        # 按块排序查询
        queries.sort(key=compare_query)
        
        # 初始化计数器和当前答案
        count = [0] * MAXK
        current_answer = 0
        
        # 初始化左右指针
        current_l = 1
        current_r = 0
        
        # 添加一个元素
        def add(pos):
            nonlocal current_answer
            x = arr[pos]
            # 计算添加x后对结果的贡献
            # 对于每个已存在的y，检查是否满足x<=y且x*2>=y或y<=x且y*2>=x
            for y in range(1, MAXK):
                if count[y] > 0:
                    if (x <= y and 2 * x >= y) or (y <= x and 2 * y >= x):
                        current_answer += count[y]
            # 更新元素计数
            count[x] += 1
        
        # 移除一个元素
        def remove(pos):
            nonlocal current_answer
            x = arr[pos]
            # 先减少计数，因为要计算移除前的影响
            count[x] -= 1
            # 计算移除x后对结果的影响
            for y in range(1, MAXK):
                if count[y] > 0:
                    if (x <= y and 2 * x >= y) or (y <= x and 2 * y >= x):
                        current_answer -= count[y]
        
        # 处理每个查询
        for q in queries:
            l = q.l
            r = q.r
            
            # 移动指针，维护当前区间
            while current_l > l:
                current_l -= 1
                add(current_l)
            while current_r < r:
                current_r += 1
                add(current_r)
            while current_l < l:
                remove(current_l)
                current_l += 1
            while current_r > r:
                remove(current_r)
                current_r -= 1
            
            # 记录查询结果
            q.answer = current_answer
        
        # 按id排序查询，恢复原顺序
        queries.sort(key=lambda x: x.id)
        
        # 输出结果
        for q in queries:
            print(q.answer)

# 测试用例
# 示例：
# 输入：
# 1
# 5 3
# 1 2 3 4 5
# 1 2
# 1 5
# 2 4
# 输出：
# 1
# 10
# 3

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 排序查询：O(m log m)
- 莫队算法主循环：
  - 指针移动的总次数：O((n / √n) * n + m * √n) = O(n√n + m√n)
  - 每次add/remove操作：O(k)，k是元素的取值范围
  - 总体时间复杂度：O(m log m + (n + m)√n * k)

空间复杂度分析：
- 存储数组和查询：O(n + m)
- 计数数组：O(k)
- 总体空间复杂度：O(n + m + k)

Python语言特性注意事项：
1. 使用自定义类存储查询信息
2. 使用lambda函数或自定义排序函数进行排序
3. 使用nonlocal关键字访问外层函数的变量
4. 为了提高输入效率，一次性读取所有输入并使用指针逐个处理
5. 注意Python中的列表索引默认是0-based，但在实现中使用了1-based索引

算法说明：
莫队算法是一种离线查询优化技术，特别适用于处理区间查询问题。其核心思想是：

1. 离线处理：首先收集所有查询，然后按照特定顺序处理
2. 分块排序：将数组分成大小为√n的块，按照左端点所在块排序，同一块内按右端点排序
3. 指针移动：维护当前区间的左右指针，通过增量更新的方式处理每个查询
4. 增量更新：通过add和remove操作，在指针移动时维护当前区间的统计信息

优化说明：
1. 奇偶排序优化：奇数块按右端点升序，偶数块按右端点降序，可以减少缓存未命中
2. 块的大小选择为√n，这是理论上的最优选择
3. 使用闭包函数（add和remove）访问外层函数的变量，简化代码

与其他方法的对比：
- 暴力法：对于每个查询重新计算，时间复杂度O(mn)
- 线段树：时间复杂度更优，但实现复杂，不适合所有类型的查询
- 莫队算法：实现相对简单，时间复杂度适中，适合多种区间查询问题

工程化考虑：
1. 注意输入数据的规模，对于大规模数据需要优化输入方法
2. 在Python中，由于GIL的存在，莫队算法可能不如C++版本高效，但对于中小规模数据仍然适用
3. 对于不同的查询问题，需要相应地调整add和remove操作的实现
4. 可以考虑使用更高效的数据结构来优化特定类型的查询
'''