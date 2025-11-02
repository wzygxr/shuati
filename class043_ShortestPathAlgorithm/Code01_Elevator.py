"""
跳楼机问题 - 同余最短路算法应用 (Python版本)

问题描述：
一座大楼一共有h层，楼层编号1~h，有如下四种移动方式：
1. 向上移动x层
2. 向上移动y层
3. 向上移动z层
4. 回到1层
假设你正在第1层，请问大楼里有多少楼层你可以到达

输入约束：
1 <= h <= 2^63 - 1
1 <= x、y、z <= 10^5

测试链接：https://www.luogu.com.cn/problem/P3403

核心算法：同余最短路 + Dijkstra算法
算法思想：将问题转化为图论问题，在模x意义下构建最短路图

时间复杂度：O(x * log x)
空间复杂度：O(x)

语言特性差异（Python vs Java/C++）：
1. 动态类型：Python无需声明变量类型，代码更简洁
2. 内置数据结构：使用heapq模块实现优先队列
3. 内存管理：自动垃圾回收，无需手动管理内存
4. 性能特点：解释型语言，运行速度相对较慢但开发效率高

工程化考量：
1. 代码简洁性：利用Python高级特性减少代码量
2. 可读性：清晰的变量命名和注释
3. 异常处理：使用try-except处理可能的异常
4. 模块化设计：函数职责单一，便于测试和维护
"""

'''
算法思路：
这道题可以转化为图论问题，用Dijkstra算法解决。
将楼层按照模x的值进行分类，构建模x意义下的最短路图。
每个点i表示模x余数为i的所有楼层中到达1层需要的最小步数。
通过y和z操作在不同余数之间建立边，权值为y和z。
最后统计所有可达楼层的数量。

时间复杂度：O(x * log x)
空间复杂度：O(x)

题目来源：洛谷P3403 跳楼机 (https://www.luogu.com.cn/problem/P3403)
相关题目：
1. POJ 2387 Til the Cows Come Home - Dijkstra模板题 (http://poj.org/problem?id=2387)
2. Codeforces 20C Dijkstra? - 最短路径模板题 (https://codeforces.com/problemset/problem/20/C)
3. LeetCode 743 Network Delay Time - 网络延迟时间 (https://leetcode.cn/problems/network-delay-time/)
4. 洛谷 P4779 单源最短路径 (https://www.luogu.com.cn/problem/P4779)
5. HDU 2544 最短路 (http://acm.hdu.edu.cn/showproblem.php?pid=2544)
6. AtCoder ABC176_D Wizard in Maze (https://atcoder.jp/contests/abc176/tasks/abc176_d)
7. SPOJ KATHTHI (https://www.spoj.com/problems/KATHTHI/)
8. LeetCode 1368 Minimum Cost to Make at Least One Valid Path in a Grid (https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)
9. Codeforces 590C Three States (https://codeforces.com/contest/590/problem/C)
10. UVA 11573 Ocean Currents (https://vjudge.net/problem/UVA-11573)
11. LeetCode 2290 Minimum Obstacle Removal to Reach Corner (https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/)
12. LeetCode 1824 Minimum Sideway Jumps (https://leetcode.cn/problems/minimum-sideway-jumps/)
13. LeetCode 1631 Path With Minimum Effort (https://leetcode.cn/problems/path-with-minimum-effort/)
14. LeetCode 847 Shortest Path Visiting All Nodes (https://leetcode.cn/problems/shortest-path-visiting-all-nodes/)
15. LeetCode 773 Sliding Puzzle (https://leetcode.cn/problems/sliding-puzzle/)
'''

import heapq  # 优先队列实现模块
import sys     # 系统相关功能，用于输入输出

def main():
    """
    主函数 - 程序入口点
    
    执行流程：
    1. 读取输入参数：h, x, y, z
    2. 初始化算法数据结构
    3. 构建图结构：添加y和z操作对应的边
    4. 执行Dijkstra算法计算最短距离
    5. 统计并输出可达楼层数量
    
    工程化考量：
    1. 输入验证：确保参数在合理范围内
    2. 异常处理：捕获可能的输入格式错误
    3. 资源管理：Python自动管理内存，无需显式释放
    4. 性能优化：使用heapq模块实现高效优先队列
    """
    
    # 读取输入参数
    # 注意：h需要减1，因为题目中楼层从1开始，但算法中从0开始计算
    try:
        line = sys.stdin.readline().split()
        h = int(line[0]) - 1  # 最大可达楼层高度
        x = int(line[1])      # 第一种移动步长
        y = int(line[2])      # 第二种移动步长  
        z = int(line[3])      # 第三种移动步长
    except (IndexError, ValueError) as e:
        print("输入格式错误，请确保输入四个整数")
        return
    
    # 输入参数验证
    if h < 0 or x <= 0 or y <= 0 or z <= 0:
        print("输入参数不合法")
        return
    
    # 初始化距离数组和访问标记数组
    # 使用float('inf')表示无穷大，符合Python习惯
    distance = [float('inf')] * x  # 距离数组：起点到各节点的最短距离
    visited = [False] * x         # 访问标记数组：记录节点是否已处理
    
    # 构建图的邻接表表示
    # 每个节点i通过y和z操作连接到(i+y)%x和(i+z)%x
    # 这体现了同余最短路的核心思想：在模x意义下构建状态转移图
    graph = [[] for _ in range(x)]  # 邻接表：graph[u] = [(v, weight), ...]
    for i in range(x):
        graph[i].append(((i + y) % x, y))  # 添加y操作边
        graph[i].append(((i + z) % x, z))  # 添加z操作边
    
    # Dijkstra算法实现
    # 算法思想：贪心策略，每次选择距离起点最近的未访问节点进行松弛操作
    # 时间复杂度：O(x * log x)，空间复杂度：O(x)
    distance[0] = 0  # 起点到自身的距离为0
    pq = [(0, 0)]    # 优先队列：存储(距离, 节点)对，按距离升序排列
    
    while pq:
        # 取出距离最小的节点
        d, u = heapq.heappop(pq)
        
        # 惰性删除：如果节点已被访问过，跳过处理
        if visited[u]:
            continue
            
        visited[u] = True  # 标记节点为已访问
        
        # 遍历当前节点的所有邻接边，进行松弛操作
        for v, w in graph[u]:
            # 如果通过u到达v的路径更短，则更新距离
            if not visited[v] and distance[u] + w < distance[v]:
                distance[v] = distance[u] + w  # 更新最短距离
                heapq.heappush(pq, (distance[v], v))  # 新距离入队
    
    # 计算结果 - 统计可达楼层数量
    # 数学原理：对于每个余数i，如果最短距离为d，那么所有满足以下条件的楼层k都可到达：
    # k ≡ i (mod x) 且 k >= d
    # 这样的楼层数量为：floor((h - d) / x) + 1
    ans = 0
    for i in range(x):
        if distance[i] <= h:  # 如果该余数类的最小距离不超过h
            # 计算该余数类中可达楼层的数量
            # 公式：(h - d) // x + 1，表示从d开始，每隔x层有一个可达楼层
            ans += (h - distance[i]) // x + 1
    
    print(ans)  # 输出最终结果

if __name__ == "__main__":
    """
    程序入口点 - 确保代码可以作为模块导入或独立运行
    
    工程化考量：
    1. 模块化设计：main()函数可以独立测试
    2. 可重用性：其他模块可以导入并使用相关函数
    3. 调试支持：可以添加调试代码而不影响主逻辑
    
    测试用例示例：
    - 输入："10 2 3 5" 期望输出：9
    - 输入："100 3 5 7" 期望输出：根据算法计算
    - 边界测试："1 1 1 1" 期望输出：1
    """
    main()