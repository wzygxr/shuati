#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
POJ 2983 Is the Information Reliable? 差分约束系统解法

题目描述：
给定n个点和m条信息，信息包含两种类型：
1. P u v w：表示点u在点v的北方w光年处（即u的y坐标比v大w）
2. V u v：表示点u在点v的北方至少1光年处（即u的y坐标比v大至少1）
判断给定信息是否一致，即是否存在矛盾。

解题思路：
这是一个典型的差分约束系统问题，用于判断信息的一致性。
我们可以将每个点的y坐标看作变量，然后根据约束条件建立不等式：
1. P u v w：yu - yv = w => yu - yv <= w 且 yv - yu <= -w
2. V u v：yu - yv >= 1 => yv - yu <= -1

差分约束建图：
1. yu - yv <= w：从v向u连权值为w的边
2. yv - yu <= -w：从u向v连权值为-w的边
3. yv - yu <= -1：从u向v连权值为-1的边

最后添加超级源点，向所有点连权值为0的边，然后使用SPFA判断是否存在负环。
如果存在负环，则信息不一致；否则信息一致。

算法实现细节：
- 使用邻接表存储图结构
- 使用SPFA算法求最短路径，检测负环
- dist字典初始化为无穷大表示无穷大距离
- count字典记录每个节点入队次数，用于检测负环
- in_queue字典标记节点是否在队列中，避免重复入队

时间复杂度：O(n * m)，其中n是点数，m是约束条件数
空间复杂度：O(n + m)

相关题目：
1. POJ 2983 Is the Information Reliable? - 本题
2. POJ 3169 Layout - 类似题目
3. 洛谷 P1993 小K的农场 - 类似题目
4. 洛谷 P5960 【模板】差分约束算法
   链接：https://www.luogu.com.cn/problem/P5960
   题意：差分约束模板题
5. POJ 1201 Intervals
   链接：http://poj.org/problem?id=1201
   题意：区间选点问题
6. POJ 1716 Integer Intervals
   链接：http://poj.org/problem?id=1716
   题意：POJ 1201的简化版本
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
   - 输入校验：检查n、m范围，坐标范围
   - 图构建：检查边数是否超过限制
   - 算法执行：检测负环
2. 性能优化：
   - 使用邻接表存储图，节省空间
   - 使用集合存储节点，避免重复
   - 使用双端队列提高队列操作效率
3. 可维护性：
   - 函数职责单一，spfa_negative_cycle()求解
   - 变量命名清晰，graph表示图结构
   - 详细注释说明算法原理和关键步骤
4. 可扩展性：
   - 可以轻松修改为求最长路径
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
    主函数，处理POJ 2983 Is the Information Reliable?问题
    """
    try:
        # 循环处理多组输入数据
        while True:
            # 读取一行输入并去除首尾空格
            line = input().strip()
            # 如果输入为空，跳出循环
            if not line:
                break
                
            # 分割输入行获取参数
            parts = line.split()
            # 解析节点数
            n = int(parts[0])
            # 解析信息数
            m = int(parts[1])
            
            # 构建图
            # 使用邻接表存储图，键为节点，值为(目标节点, 权重)的列表
            graph = {}
            # 使用集合存储所有节点，避免重复
            nodes = set()
            
            # 标记输入是否有效
            valid = True
            
            # 读入m条信息
            for _ in range(m):
                # 读取一行信息并分割
                info = input().split()
                # 获取信息类型
                type_char = info[0]
                
                # 根据信息类型处理
                if type_char == 'P':
                    # P类型信息：点u在点v的北方w光年处
                    u = int(info[1])
                    v = int(info[2])
                    w = int(info[3])
                    
                    # P u v w: yu - yv = w
                    # 转化为: yu - yv <= w 且 yv - yu <= -w
                    # 从节点v向节点u连一条权值为w的边
                    if v not in graph:
                        graph[v] = []
                    graph[v].append((u, w))
                    nodes.add(v)
                    nodes.add(u)
                    
                    # 从节点u向节点v连一条权值为-w的边
                    if u not in graph:
                        graph[u] = []
                    graph[u].append((v, -w))
                    nodes.add(u)
                    nodes.add(v)
                elif type_char == 'V':
                    # V类型信息：点u在点v的北方至少1光年处
                    u = int(info[1])
                    v = int(info[2])
                    
                    # V u v: yu - yv >= 1
                    # 转化为: yv - yu <= -1
                    # 从节点u向节点v连一条权值为-1的边
                    if u not in graph:
                        graph[u] = []
                    graph[u].append((v, -1))
                    nodes.add(u)
                    nodes.add(v)
                else:
                    # 无效的信息类型
                    valid = False
                    break
            
            # 如果输入无效，输出"Unreliable"并继续处理下一组数据
            if not valid:
                print("Unreliable")
                continue
            
            # 添加超级源点，向所有点连权值为0的边
            # 超级源点的编号为0
            super_source = 0
            # 初始化超级源点的邻接表
            graph[super_source] = []
            # 从超级源点向所有节点连权值为0的边，确保图的连通性
            for i in range(1, n + 1):
                graph[super_source].append((i, 0))
                nodes.add(i)
            nodes.add(super_source)
            
            # SPFA判断负环
            def spfa_negative_cycle(start, node_count):
                """
                SPFA算法实现，用于检测负环并计算最短路径
                时间复杂度：平均O(k*m)，最坏O(n*m)，其中k是常数
                空间复杂度：O(n)
                
                @param start 起始节点（超级源点）
                @param node_count 节点数量
                @return 如果存在负环返回False，否则返回True
                """
                # 初始化距离字典，记录从起始节点到各节点的最短距离
                dist = {}
                # 初始化入队标记字典，记录节点是否在队列中
                in_queue = {}
                # 初始化计数字典，记录节点被更新的次数，用于检测负环
                count = {}
                
                # 初始化所有节点的距离为无穷大
                for node in nodes:
                    dist[node] = sys.maxsize
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
                            # 松弛操作（最短路）
                            # 如果通过节点u可以缩短到节点v的距离
                            if dist[v] > dist[u] + w:
                                # 更新到节点v的最短距离
                                dist[v] = dist[u] + w
                                
                                # 如果节点v不在队列中
                                if not in_queue[v]:
                                    # 将节点v加入队列右侧
                                    queue.append(v)
                                    # 标记节点v已在队列中
                                    in_queue[v] = True
                                    # 增加节点v的更新次数
                                    count[v] += 1
                                    
                                    # 如果入队次数超过节点数，说明存在负环
                                    if count[v] > node_count:
                                        # 返回False，表示存在负环
                                        return False  # 存在负环
                
                # 返回True，表示不存在负环
                return True  # 不存在负环
            
            # 使用SPFA判断是否存在负环
            # 调用SPFA算法判断是否存在负环，节点数量为节点集合的大小
            if spfa_negative_cycle(super_source, len(nodes)):
                # 如果不存在负环，说明信息一致，输出"Reliable"
                print("Reliable")
            else:
                # 如果存在负环，说明信息不一致，输出"Unreliable"
                print("Unreliable")
                
    # 捕获EOFError异常，当输入结束时退出程序
    except EOFError:
        pass

# 程序入口
if __name__ == "__main__":
    main()