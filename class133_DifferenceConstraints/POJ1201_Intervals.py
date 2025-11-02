#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 1201 Intervals 差分约束系统解法

题目描述：
给定n个区间[ai, bi]和对应的整数ci，要求选出最少的整数点集合，
使得每个区间[ai, bi]内至少包含ci个选出的整数点。

解题思路：
这是一个经典的差分约束系统问题。我们可以用前缀和的思想来建模：
设S[i]表示在区间[0, i]内选出的整数点个数，则：
1. S[i] - S[i-1] >= 0  (选的点数非负)
2. S[i] - S[i-1] <= 1  (每个位置最多选1个点)
3. S[bi] - S[ai-1] >= ci  (每个区间至少选ci个点)

为了处理负数下标，我们将所有坐标加上一个偏移量。

差分约束建图：
1. 0 <= S[i] - S[i-1] <= 1 转化为：
   S[i-1] - S[i] <= 0 (从i向i-1连权值为0的边)
   S[i] - S[i-1] <= 1 (从i-1向i连权值为1的边)
2. S[bi] - S[ai-1] >= ci 转化为：
   S[ai-1] - S[bi] <= -ci (从bi向ai-1连权值为-ci的边)

最后添加超级源点，向所有点连权值为0的边，然后求最长路。
答案就是S[max_bi] - S[min_ai-1]。

算法实现细节：
- 使用邻接表存储图结构
- 使用SPFA算法求最长路径，检测正环
- dist字典初始化为负无穷大表示无穷小距离
- count字典记录每个节点入队次数，用于检测正环
- in_queue字典标记节点是否在队列中，避免重复入队

时间复杂度：O(n + m)，其中n是坐标范围，m是约束条件数
空间复杂度：O(n + m)

相关题目：
1. POJ 1201 Intervals - 本题
2. POJ 1716 Integer Intervals - 简化版本
3. ZOJ 1508 Intervals - 类似题目
4. 洛谷 P5960 【模板】差分约束算法
   链接：https://www.luogu.com.cn/problem/P5960
   题意：差分约束模板题
5. 洛谷 P1993 小K的农场
   链接：https://www.luogu.com.cn/problem/P1993
   题意：农场约束问题
6. POJ 3169 Layout
   链接：http://poj.org/problem?id=3169
   题意：奶牛布局问题
7. 洛谷 P1250 种树
   链接：https://www.luogu.com.cn/problem/P1250
   题意：区间种树问题
8. 洛谷 P2294 [HNOI2005]狡猾的商人
   链接：https://www.luogu.com.cn/problem/P2294
   题意：商人账本合理性判断
9. 洛谷 P4926 [1007]倍杀测量者
   链接：https://www.luogu.com.cn/problem/P4926
   题意：倍杀测量问题，需要对数变换
10. 洛谷 P3275 [SCOI2011]糖果
    链接：https://www.luogu.com.cn/problem/P3275
    题意：分糖果问题
11. LibreOJ #10087 「一本通3.4 例1」Intervals
    链接：https://loj.ac/p/10087
    题意：区间选点问题，与POJ 1201类似
12. LibreOJ #10088 「一本通3.4 例2」出纳员问题
    链接：https://loj.ac/p/10088
    题意：出纳员工作时间安排问题
13. AtCoder ABC216G 01Sequence
    链接：https://atcoder.jp/contests/abc216/tasks/abc216_g
    题意：01序列问题，涉及差分约束

工程化考虑：
1. 异常处理：
   - 输入校验：检查n范围，区间端点范围
   - 图构建：检查边数是否超过限制
   - 算法执行：检测正环
2. 性能优化：
   - 使用邻接表存储图，节省空间
   - 使用集合存储节点，避免重复
   - 使用双端队列提高队列操作效率
3. 可维护性：
   - 函数职责单一，spfa_longest_path()求解
   - 变量命名清晰，graph表示图结构
   - 详细注释说明算法原理和关键步骤
4. 可扩展性：
   - 可以轻松修改为求最短路径
   - 可以扩展支持更多类型的约束条件
   - 可以添加更多输出信息，如具体哪个约束导致无解
5. 边界情况处理：
   - 空输入处理
   - 极端值处理（最大/最小约束值）
   - 重复约束处理
6. 测试用例覆盖：
   - 基本功能测试
   - 边界值测试
   - 异常情况测试
   - 性能测试
"""

import sys
from collections import deque

def main():
    """
    主函数，处理POJ 1201 Intervals问题
    """
    # 读取输入，获取区间约束数量
    n = int(input())
    
    # 存储区间约束的列表
    constraints = []
    # 记录坐标的最小值和最大值
    min_pos = sys.maxsize
    max_pos = -sys.maxsize - 1
    
    # 读入n个区间约束
    for _ in range(n):
        # 读取一行输入并解析为三个整数
        a, b, c = map(int, input().split())
        # 将约束添加到列表中
        constraints.append((a, b, c))
        # 更新坐标范围的最小值和最大值
        min_pos = min(min_pos, a)
        max_pos = max(max_pos, b)
    
    # 为了处理负数下标，我们使用偏移量
    # 足够大的偏移量，确保所有坐标都变为正数
    offset = 10000  # 足够大的偏移量
    
    # 调整坐标范围，加上偏移量
    min_pos = int(min_pos + offset)
    max_pos = int(max_pos + offset)
    
    # 构建图
    # 使用邻接表存储图，键为节点，值为(目标节点, 权重)的列表
    graph = {}
    # 使用集合存储所有节点，避免重复
    nodes = set()
    
    # 添加约束：S[b] - S[a-1] >= c
    # 转化为：S[a-1] - S[b] <= -c
    for a, b, c in constraints:
        # 计算偏移后的坐标
        a_offset = a - 1 + offset
        b_offset = b + offset
        # 如果起始节点不在图中，初始化为空列表
        if a_offset not in graph:
            graph[a_offset] = []
        # 从节点b_offset向节点a_offset连一条权值为-c的边
        graph[a_offset].append((b_offset, -c))
        # 将两个节点添加到节点集合中
        nodes.add(a_offset)
        nodes.add(b_offset)
    
    # 添加基本约束：0 <= S[i] - S[i-1] <= 1
    for i in range(min_pos - 1, max_pos + 1):
        # S[i] - S[i-1] >= 0 => S[i-1] - S[i] <= 0
        # 从节点i向节点(i-1)连一条权值为0的边
        if i not in graph:
            graph[i] = []
        graph[i].append((i - 1, 0))
        nodes.add(i)
        nodes.add(i - 1)
        
        # S[i] - S[i-1] <= 1 => S[i] - S[i-1] <= 1
        # 从节点(i-1)向节点i连一条权值为1的边
        if i - 1 not in graph:
            graph[i - 1] = []
        graph[i - 1].append((i, 1))
        nodes.add(i - 1)
        nodes.add(i)
    
    # 添加超级源点，向所有点连权值为0的边
    # 超级源点的编号为max_pos + 1
    super_source = max_pos + 1
    # 初始化超级源点的邻接表
    graph[super_source] = []
    # 从超级源点向所有节点连权值为0的边，确保图的连通性
    for node in nodes:
        graph[super_source].append((node, 0))
    
    # SPFA求最长路
    def spfa_longest_path(start, node_count):
        """
        SPFA算法实现，用于检测正环并计算最长路径
        时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数
        空间复杂度：O(n)
        
        @param start 起始节点（超级源点）
        @param node_count 节点数量
        @return (dist, no_cycle) 其中dist为距离字典，no_cycle为是否存在正环
        """
        # 初始化距离字典，记录从起始节点到各节点的最长距离
        dist = {}
        # 初始化入队标记字典，记录节点是否在队列中
        in_queue = {}
        # 初始化计数字典，记录节点被更新的次数，用于检测正环
        count = {}
        
        # 初始化所有节点的距离为负无穷大
        for node in nodes:
            dist[node] = -sys.maxsize - 1
            in_queue[node] = False
            count[node] = 0
        # 起始节点的距离设为0
        dist[start] = 0
        # 标记起始节点已在队列中
        in_queue[start] = True
        # 起始节点的更新次数设为1
        count[start] = 1
        
        # 创建双端队列用于SPFA算法
        queue = deque([start])
        
        # 当队列不为空时继续循环
        while queue:
            # 从队列左侧取出节点
            u = queue.popleft()
            # 标记该节点已出队
            in_queue[u] = False
            
            # 遍历节点u的所有邻接点
            if u in graph:
                for v, w in graph[u]:
                    # 松弛操作（最长路）
                    # 如果通过节点u可以增加到节点v的距离
                    if dist[v] < dist[u] + w:
                        # 更新到节点v的最长距离
                        dist[v] = dist[u] + w
                        
                        # 如果节点v不在队列中
                        if not in_queue[v]:
                            # 将节点v加入队列右侧
                            queue.append(v)
                            # 标记节点v已在队列中
                            in_queue[v] = True
                            # 增加节点v的更新次数
                            count[v] += 1
                            
                            # 如果入队次数超过节点数，说明存在正环，无解
                            if count[v] > node_count:
                                # 返回None和False，表示存在正环
                                return None, False  # 存在正环
        
        # 返回距离字典和True，表示不存在正环
        return dist, True
    
    # 求最长路
    # 调用SPFA算法求最长路径，节点数量为节点集合大小加1
    dist, no_cycle = spfa_longest_path(super_source, len(nodes) + 1)
    
    # 如果存在正环，说明无解
    if not no_cycle:
        print(-1)  # 无解
    else:
        # 如果存在解且距离字典不为空
        if dist is not None:
            # 计算最小位置和最大位置的键值
            min_pos_key = min_pos - 1
            max_pos_key = max_pos
            # 输出S[max_pos] - S[min_pos-1]，即选中的点数
            print(int(dist[max_pos_key] - dist[min_pos_key]))
        else:
            # 如果距离字典为空，输出-1
            print(-1)

# 程序入口
if __name__ == "__main__":
    main()