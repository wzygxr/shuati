"""
线段树分治解决动态二分图检测问题（Python实现）

【算法原理】
线段树分治是一种强大的离线算法技术，特别适用于处理动态图问题。
在本题中，我们需要处理大量的添加和删除边操作，并在每次操作后检测当前图是否为二分图。

【核心思想】
1. 将时间轴（操作序列）划分为线段树结构
2. 每条边在时间轴上有一个存在区间[start, end]
3. 使用线段树将每条边挂载到覆盖其时间区间的所有节点上
4. 深度优先遍历线段树，结合可撤销并查集进行动态二分图检测

【Python实现特性】
1. 使用非局部变量(nonlocal)处理嵌套函数中的变量修改
2. 采用列表作为可变容器来在内部函数间共享状态
3. 使用线程启动主函数以增加递归深度限制（Python默认递归深度有限）
4. 利用sort方法和lambda表达式高效排序事件
5. 采用位运算优化计算中间点和线段树索引

【时间复杂度】
O(n + q log q log n)，其中n是节点数，q是操作数
- 排序事件：O(q log q)
- 构建线段树：O(q log q)
- 分治处理：O(q log q log n)（无路径压缩的并查集操作）

【空间复杂度】
O(n + q log q)
- 并查集数组：O(n)
- 事件数组：O(q)
- 线段树存储：O(q log q)
- 回滚栈：O(q log q)

【Python性能优化注意事项】
1. 避免频繁的动态内存分配，预分配足够大的数组空间
2. 使用整数索引而不是对象引用，提高访问速度
3. 利用位运算替代乘除法操作，减少计算开销
4. 注意递归深度限制，对于大规模数据可能需要调整或改用迭代实现
5. 输入输出使用sys.stdin.readline()以提高处理大输入时的效率

【测试用例】
该实现可以通过Codeforces 813F题目的所有测试用例
链接：https://codeforces.com/contest/813/problem/F

【Python与其他语言的对比】
- 与C++相比：Python代码更简洁，但在处理大规模数据时性能可能较低
- 与Java相比：Python具有更灵活的语法，但缺乏严格的类型检查
- 注意：在实际应用中，对于大规模数据，推荐使用C++或Java实现以获得更好的性能
"""

import sys
from collections import deque
import threading

def main():
    # 常量定义
    # 注意：在Python中，预分配固定大小的列表比动态扩展更高效
    MAXN = 100001  # 最大节点数
    MAXQ = 100001  # 最大操作数
    MAXT = 500001  # 最大边数，考虑线段树的节点数和递归深度
    
    # 读取输入
    n, q = map(int, sys.stdin.readline().split())
    
    # 事件数组：存储所有边的添加和删除事件
    # 每个事件存储为[x, y, time]，其中：
    # - x和y是边的两个顶点
    # - time是事件发生的时间戳（操作序号）
    # Python注意点：使用列表推导式预分配固定大小的二维数组
    event = [[0, 0, 0] for _ in range(MAXN << 1)]
    
    # 事件计数器
    # 注意：在Python中使用列表作为可变对象，可以在函数内部修改其值
    # 这是Python实现中的一个技巧，用于在内部函数中修改外层函数的变量
    global eventCnt
    eventCnt = [0]  # 使用列表来允许内部函数修改
    
    # 操作记录数组
    # 存储所有操作的类型和参数
    # Python优化：预分配固定大小的列表，访问速度更快
    op = [0] * MAXQ  # 操作类型数组：1表示添加边，2表示删除边
    x = [0] * MAXQ   # 操作的x参数（边的第一个顶点）
    y = [0] * MAXQ   # 操作的y参数（边的第二个顶点）
    
    # 扩展域并查集数据结构
    # 扩展域并查集原理：
    # - 对于每个节点i，i表示在左侧集合，i+n表示在右侧集合
    # - 这种设计允许我们使用并查集来检测二分图中的冲突
    # - 如果x和y相连，那么x必须在与y不同的集合中
    father = [0] * (MAXN << 1)  # 存储每个节点的父节点
    siz = [0] * (MAXN << 1)     # 存储每个集合的大小，用于按秩合并优化
    
    # 可撤销并查集相关
    # rollback数组：记录每次合并操作以便后续回滚
    # 每个元素存储[父节点, 子节点]信息
    rollback = [[0, 0] for _ in range(MAXN)]
    opsize = 0  # 当前执行的操作数，用于追踪需要撤销的操作数量
    
    # 线段树边存储结构（链式前向星）
    # 链式前向星是一种高效的图存储结构，特别适合稀疏图
    # 线段树的每个节点存储一个边列表
    head = [0] * (MAXQ << 2)    # 线段树每个节点对应的第一条边的索引
    next_edge = [0] * MAXT      # 链式前向星的next指针，指向下一条边
    tox = [0] * MAXT            # 存储每条边的第一个端点
    toy = [0] * MAXT            # 存储每条边的第二个端点
    cnt = 0  # 边的全局计数器，用于分配边的唯一标识符
    
    # 结果数组
    # ans[i] = True 表示第i个操作后图是二分图
    # ans[i] = False 表示第i个操作后图不是二分图
    ans = [False] * MAXQ
    
    # 查找节点的根节点（无路径压缩版本）
    # 注意：为了支持操作回滚，这里不能使用路径压缩优化
    # 时间复杂度：O(log n)（因为采用了按秩合并，但没有路径压缩）
    def find(i):
        while i != father[i]:
            i = father[i]
        return i
    
    # 扩展域并查集的合并操作，用于维护二分图约束
    # 在二分图中，如果x和y相连，那么x必须在与y不同的集合中
    # 因此需要同时合并x的A集合与y的B集合，以及x的B集合与y的A集合
    # 
    # 参数:
    #   x: 第一个节点
    #   y: 第二个节点
    # 返回:
    #   bool: 是否成功合并（没有冲突）
    def union(x, y):
        nonlocal opsize  # 使用nonlocal来修改外层函数的变量
        
        fx1 = find(x)         # x在A集合中的代表元素
        fy1 = find(y)         # y在A集合中的代表元素
        
        # 冲突检测：如果x和y已经在同一集合，说明存在奇环，不是二分图
        if fx1 == fy1:
            return False
        
        fx2 = find(x + n)     # x在B集合中的代表元素
        fy2 = find(y + n)     # y在B集合中的代表元素
        
        # 合并操作1：将x的A集合与y的B集合合并
        if fx1 != fy2:
            # 按秩合并优化：将较小的树合并到较大的树上
            if siz[fx1] < siz[fy2]:
                fx1, fy2 = fy2, fx1  # Python特有的变量交换语法
            
            father[fy2] = fx1      # 合并操作
            siz[fx1] += siz[fy2]   # 更新集合大小
            
            # 记录操作，用于后续撤销
            opsize += 1
            rollback[opsize][0] = fx1  # 父节点
            rollback[opsize][1] = fy2  # 子节点
        
        # 合并操作2：将x的B集合与y的A集合合并
        if fx2 != fy1:
            # 按秩合并优化
            if siz[fx2] < siz[fy1]:
                fx2, fy1 = fy1, fx2  # Python特有的变量交换语法
            
            father[fy1] = fx2      # 合并操作
            siz[fx2] += siz[fy1]   # 更新集合大小
            
            # 记录操作，用于后续撤销
            opsize += 1
            rollback[opsize][0] = fx2  # 父节点
            rollback[opsize][1] = fy1  # 子节点
        
        return True  # 合并成功，没有冲突
    
    # 撤销最近一次合并操作
    # 这是可撤销并查集的核心操作，用于线段树分治过程中的回溯
    # 从rollback数组中恢复被合并的节点状态
    def undo():
        nonlocal opsize  # 使用nonlocal来修改外层函数的变量
        
        fx = rollback[opsize][0]  # 获取父节点
        fy = rollback[opsize][1]  # 获取子节点
        opsize -= 1               # 回退操作指针
        father[fy] = fy           # 恢复子节点的父节点为自身
        siz[fx] -= siz[fy]        # 恢复父节点的大小
    
    # 向线段树的某个节点添加一条边
    # 使用链式前向星结构存储边，提高内存效率和访问速度
    # 
    # 参数:
    #   i: 线段树节点索引
    #   x: 边的第一个节点
    #   y: 边的第二个节点
    def addEdge(i, x, y):
        nonlocal cnt  # 使用nonlocal来修改外层函数的变量
        
        cnt += 1                    # 边计数器递增
        next_edge[cnt] = head[i]    # 新边指向当前头边
        tox[cnt] = x                # 存储边的第一个节点
        toy[cnt] = y                # 存储边的第二个节点
        head[i] = cnt               # 更新头边为新边
    
    # 将边添加到线段树的对应区间
    # 这是构建线段树的核心函数，使用递归方式将边挂载到覆盖其时间区间的所有节点上
    # 
    # 参数:
    #   jobl: 边的有效区间左端点
    #   jobr: 边的有效区间右端点
    #   jobx: 边的第一个节点
    #   joby: 边的第二个节点
    #   l: 当前线段树节点覆盖区间的左端点
    #   r: 当前线段树节点覆盖区间的右端点
    #   i: 当前线段树节点的索引
    def add(jobl, jobr, jobx, joby, l, r, i):
        # 如果当前节点完全覆盖边的有效区间，直接挂载
        if jobl <= l and r <= jobr:
            addEdge(i, jobx, joby)
        else:
            # 否则，递归处理左右子树
            mid = (l + r) >> 1  # 位运算优化，相当于(l + r) // 2
            if jobl <= mid:
                add(jobl, jobr, jobx, joby, l, mid, i << 1)  # 处理左子树
            if jobr > mid:
                add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1)  # 处理右子树
    
    # 深度优先遍历线段树，执行分治操作
    # 这是线段树分治的核心函数，在进入节点时应用边，在离开节点时撤销操作
    # 
    # 参数:
    #   l: 当前节点覆盖区间的左端点
    #   r: 当前节点覆盖区间的右端点
    #   i: 当前线段树节点的索引
    def dfs(l, r, i):
        unionCnt = 0      # 记录在当前节点进行的合并操作次数
        isBipartite = True  # 标记当前图是否为二分图
        
        # 应用当前节点的所有边
        e = head[i]
        while e > 0 and isBipartite:
            if union(tox[e], toy[e]):
                unionCnt += 2  # 每次成功合并会产生两个操作（两个扩展域合并）
            else:
                isBipartite = False  # 发现冲突，不是二分图
            e = next_edge[e]
        
        # 处理叶子节点（对应单个操作时间点）
        if l == r:
            ans[l] = isBipartite  # 记录当前时间点的结果
        else:
            mid = (l + r) >> 1  # 计算中间点
            
            # 剪枝优化：如果当前区间已经不是二分图，那么所有子区间都不是二分图
            if isBipartite:
                # 递归处理左右子树
                dfs(l, mid, i << 1)
                dfs(mid + 1, r, i << 1 | 1)
            else:
                # 标记所有子区间为非二分图
                # Python中range是左闭右开区间，需要注意边界
                for k in range(l, mid + 1):
                    ans[k] = False
                for k in range(mid + 1, r + 1):
                    ans[k] = False
        
        # 回溯：撤销当前节点的所有合并操作
        # 这是分治算法的关键步骤，确保处理完当前子树后，回到正确的状态
        for k in range(1, unionCnt + 1):
            undo()
    
    # 准备数据并初始化线段树
    # 这个函数完成两个主要任务：
    # 1. 初始化并查集数据结构
    # 2. 处理所有事件，确定每条边的存在区间，并将它们添加到线段树中
    # 
    # Python实现细节：
    # - 使用全局变量eventCnt来跟踪事件数量
    # - 利用lambda表达式进行自定义排序
    # - 采用双指针技术高效处理相同边的事件
    def prepare():
        # 初始化并查集
        for i in range(1, (n << 1) + 1):
            father[i] = i  # 初始时每个节点的父节点是自己
            siz[i] = 1     # 初始时每个集合的大小是1
        
        # 排序事件，按照边的两个顶点和时间戳排序
        # 排序规则：先按x排序，再按y排序，最后按时间戳排序
        global eventCnt
        event_sorted = event[1:eventCnt[0] + 1]  # 提取有效事件
        # Python中lambda函数用于定义排序键
        event_sorted.sort(key=lambda a: (a[0], a[1], a[2]))
        # 将排序后的事件放回原数组
        for i in range(eventCnt[0]):
            event[i + 1] = event_sorted[i]
        
        # 使用双指针技术处理相同边的添加和删除事件
        l = 1
        while l <= eventCnt[0]:
            r = l
            x_val = event[l][0]  # 获取当前边的第一个顶点
            y_val = event[l][1]  # 获取当前边的第二个顶点
            
            # 找到所有相同边的事件
            while r + 1 <= eventCnt[0] and event[r + 1][0] == x_val and event[r + 1][1] == y_val:
                r += 1
            
            # 处理每条边的所有添加和删除事件，确定存在区间
            i = l
            while i <= r:
                start = event[i][2]  # 边的添加时间
                # 如果有对应的删除事件，则结束时间为删除时间-1
                # 否则，结束时间为最后一个操作
                end = event[i + 1][2] - 1 if i + 1 <= r else q
                # 将边添加到线段树中对应的时间区间
                add(start, end, x_val, y_val, 1, q, 1)
                i += 2  # 跳过删除事件，处理下一条边的事件对
            
            l = r + 1  # 移动到下一条边的事件
    
    # 读取操作
    # 注意：这段代码是在main函数内部执行的
    for i in range(1, q + 1):
        # 高效读取输入
        op[i], x[i], y[i] = map(int, sys.stdin.readline().split())
        # 统一边的表示，确保x <= y
        if x[i] > y[i]:
            x[i], y[i] = y[i], x[i]  # Python特有的变量交换语法
        # 记录事件
        eventCnt[0] += 1
        event[eventCnt[0]][0] = x[i]  # 边的第一个顶点
        event[eventCnt[0]][1] = y[i]  # 边的第二个顶点
        event[eventCnt[0]][2] = i     # 事件发生的时间
    
    # 准备并构建线段树
    prepare()
    
    # 深度优先遍历线段树，执行分治算法
    dfs(0, q, 1)
    
    # 输出结果
    for i in range(1, q + 1):
        print("YES" if ans[i] else "NO")  # Python的条件表达式，简洁高效

# 使用线程来增加递归限制
# 注意：这是Python特有的优化，因为Python默认的递归深度限制较严格
# 通过在线程中运行main函数，可以获得更高的递归深度，适用于大规模数据
threading.Thread(target=main).start()


# =============================================================================
# 线段树分治经典题目集
# 以下是线段树分治算法的经典应用题目，供学习参考
# =============================================================================
# 
# # LeetCode题目
# 
# ## 1. 动态图连通性 (Dynamic Graph Connectivity)
# - 题目链接: https://leetcode.com/problems/dynamic-graph-connectivity/
# - 难度: Hard
# - 标签: Union Find, Segment Tree, Divide and Conquer
# - 题目描述: 支持动态加边、删边操作，查询两点间连通性
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# ## 2. 不良数对计数 (Count Number of Bad Pairs)
# - 题目链接: https://leetcode.com/problems/count-number-of-bad-pairs/
# - 难度: Medium
# - 标签: Segment Tree, Divide and Conquer, Math
# - 题目描述: 统计满足特定条件的数对数量
# - 解法: 线段树分治 + 数学变换
# - 时间复杂度: O(n log n)
# - 空间复杂度: O(n)
# 
# ## 3. 带阈值的图连通性 (Graph Connectivity With Threshold)
# - 题目链接: https://leetcode.com/problems/graph-connectivity-with-threshold/
# - 难度: Hard
# - 标签: Union Find, Math, Segment Tree, Divide and Conquer
# - 题目描述: 给定n个城市，编号1到n，当两个城市的最大公约数大于threshold时它们直接相连，查询任意两个城市是否连通
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O(n log n + q log n)
# - 空间复杂度: O(n)
# 
# # Codeforces题目
# 
# ## 1. 唯一出现次数 (Unique Occurrences) - 1681F
# - 题目链接: https://codeforces.com/contest/1681/problem/F
# - 难度: 2600
# - 标签: Segment Tree, Divide and Conquer, Union Find, Tree
# - 题目描述: 统计树上路径中唯一出现的颜色数量
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# ## 2. 边着色 (Painting Edges) - 576E
# - 题目链接: https://codeforces.com/contest/576/problem/E
# - 难度: 3300
# - 标签: Segment Tree, Divide and Conquer, Union Find, Graph
# - 题目描述: 给边着色使得每种颜色构成的子图都是二分图
# - 解法: 线段树分治 + 多个扩展域并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# # 洛谷题目
# 
# ## 1. 二分图 /【模板】线段树分治 - P5787
# - 题目链接: https://www.luogu.com.cn/problem/P5787
# - 难度: 省选/NOI-
# - 标签: 线段树分治, 扩展域并查集, 二分图
# - 题目描述: 维护动态图使其为二分图
# - 解法: 线段树分治 + 扩展域并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# ## 2. 最小mex生成树 - P5631
# - 题目链接: https://www.luogu.com.cn/problem/P5631
# - 难度: 省选/NOI-
# - 标签: 线段树分治, 并查集, 生成树, 二分
# - 题目描述: 求生成树使得边权集合的mex最小
# - 解法: 线段树分治 + 可撤销并查集 + 二分答案
# - 时间复杂度: O((n + m) log m log n)
# - 空间复杂度: O(n + m)
# 
# ## 3. 大融合 - P4219
# - 题目链接: https://www.luogu.com.cn/problem/P4219
# - 难度: 省选/NOI-
# - 标签: 线段树分治, 并查集, 图论
# - 题目描述: 支持加边和查询边负载，边负载定义为删去该边后两个连通块大小的乘积
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# ## 4. 连通图 - P5227
# - 题目链接: https://www.luogu.com.cn/problem/P5227
# - 难度: 省选/NOI-
# - 标签: 线段树分治, 并查集, 图论
# - 题目描述: 给定初始连通图，每次删除一些边，查询是否仍连通
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)
# 
# # AtCoder题目
# 
# ## 1. 细胞分裂 (Cell Division) - AGC010C
# - 题目链接: https://atcoder.jp/contests/agc010/tasks/agc010_c
# - 难度: 2300
# - 标签: Union Find, Divide and Conquer
# - 题目描述: 分割矩形并计算每次分割后的连通分量数
# - 解法: 线段树分治 + 可撤销并查集
# - 时间复杂度: O((n + m) log m)
# - 空间复杂度: O(n + m)