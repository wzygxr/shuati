# 【ROSALIND】-BA3G-Find an Eulerian Path in a Graph-Bioinformatics-中等

## 题目原始链接
https://rosalind.info/problems/ba3g/

## 题目完整描述
在一个有向图中找到一条欧拉路径。欧拉路径是指经过图中每条边恰好一次的路径。

在生物信息学中，这个问题通常出现在基因组组装中，其中k-mers（长度为k的DNA片段）被表示为图中的边，而节点表示(k-1)-mers。

### 输入格式
输入包含多行：
- 第一行是一个整数，表示节点的总数
- 接下来每行的格式为 "node1 -> node2,node3,...,noden"，表示从node1出发的有向边指向node2, node3, ..., noden

### 输出格式
输出一条欧拉路径，节点之间用箭头连接。

### 数据范围
- 节点数 ≤ 1000
- 边数 ≤ 2000

### 样例输入输出
```
输入：
4
1 -> 2
2 -> 3
3 -> 1,4
4 -> 2

输出：
1 -> 2 -> 3 -> 1 -> 4 -> 2
```

```
输入：
6
1 -> 2
2 -> 3,4
3 -> 5
4 -> 6
5 -> 6
6 -> 3

输出：
1 -> 2 -> 3 -> 5 -> 6 -> 3 -> 4 -> 6
```

## 笔试/面试考察点分析
- **考察点1**：有向图欧拉路径的判定与构造算法
- **考察点2**：字符串解析与图构建
- **考察点3**：生物信息学中的应用背景理解
- **考察点4**：基因组组装中的De Bruijn图应用
- **考察点5**：字符串拼接与k-mer处理

## 解题思路
这是一个经典的生物信息学问题，主要应用在基因组组装中：

1. 解析输入，构建有向图
2. 统计每个节点的入度和出度
3. 根据欧拉路径判定定理验证是否存在欧拉路径
4. 使用Hierholzer算法构造欧拉路径
5. 在生物信息学中，这相当于从k-mers重建原始DNA序列

## 完整代码实现

```python
def find_eulerian_path():
    """
    寻找有向图中的欧拉路径
    生物信息学中用于基因组组装，将k-mers重建为完整序列
    """
    import sys
    from collections import defaultdict, deque
    
    # 读取输入并构建图
    # 生物信息学中，节点通常是(k-1)-mers，边是k-mers
    graph = defaultdict(list)  # 邻接表存储图，面试需说明邻接表的优势
    in_degree = defaultdict(int)  # 入度字典，用于欧拉路径判定
    out_degree = defaultdict(int)  # 出度字典，用于欧拉路径判定
    
    # 解析输入
    # 笔试中需掌握字符串解析技巧
    lines = []
    for line in sys.stdin:
        line = line.strip()
        if line:
            lines.append(line)
    
    for line in lines:  # 遍历所有输入行
        if ' -> ' in line:  # 检查边的格式
            parts = line.split(' -> ')  # 分割起点和终点
            start = parts[0]  # 起点节点
            ends = parts[1].split(',')  # 终点节点列表
            
            for end in ends:  # 遍历所有终点
                end = end.strip()  # 去除空白字符
                graph[start].append(end)  # 在图中添加边
                out_degree[start] += 1  # 增加起点出度
                in_degree[end] += 1  # 增加终点入度
    
    # 获取所有节点
    all_nodes = set()  # 所有节点的集合
    all_nodes.update(graph.keys())  # 添加所有起点
    for neighbors in graph.values():  # 添加所有终点
        all_nodes.update(neighbors)
    
    # 检查欧拉路径的度数条件
    # 有向图欧拉路径判定定理的应用，面试高频考点
    start_nodes = []  # 可能的起点列表
    end_nodes = []  # 可能的终点列表
    
    for node in all_nodes:  # 遍历所有节点
        in_deg = in_degree[node]  # 节点入度
        out_deg = out_degree[node]  # 节点出度
        
        if out_deg - in_deg == 1:  # 出度比入度多1，可能是起点
            start_nodes.append(node)  # 添加到起点列表
        elif in_deg - out_deg == 1:  # 入度比出度多1，可能是终点
            end_nodes.append(node)  # 添加到终点列表
        elif in_deg != out_deg:  # 入度出度不等且不符合起点终点条件
            print("No Eulerian Path")  # 不存在欧拉路径
            return
    
    # 验证度数条件
    # 欧拉路径判定定理：要么没有起点终点（回路），要么恰有一个起点一个终点
    if len(start_nodes) == 0 and len(end_nodes) == 0:  # 欧拉回路
        # 任选一个有出度的节点作为起点
        for node in all_nodes:  # 寻找起始节点
            if out_degree[node] > 0:  # 如果有出度
                start_nodes.append(node)  # 作为起点
                break
    elif len(start_nodes) == 1 and len(end_nodes) == 1:  # 欧拉路径
        # 条件满足，继续处理
        pass
    else:  # 不满足欧拉路径条件
        print("No Eulerian Path")  # 不存在欧拉路径
        return
    
    # 使用Hierholzer算法构造欧拉路径
    # 生物信息学中用于从k-mers重建DNA序列，面试高频考点
    def hierholzer(start_node):
        stack = [start_node]  # 使用栈模拟递归过程
        path = []  # 存储路径
        temp_graph = defaultdict(list)  # 临时图，用于算法
        
        # 复制原图
        for node, neighbors in graph.items():  # 复制邻接表
            temp_graph[node] = neighbors[:]
        
        while stack:  # 当栈不为空时继续
            current = stack[-1]  # 查看栈顶元素
            
            if temp_graph[current]:  # 如果当前节点还有未访问的边
                next_node = temp_graph[current].pop()  # 获取下一个节点并删除边
                stack.append(next_node)  # 将下一个节点压入栈
            else:  # 如果当前节点没有未访问的边
                path.append(stack.pop())  # 将当前节点加入路径并弹出栈
        
        path.reverse()  # 反转路径得到正确顺序
        return path  # 返回欧拉路径
    
    # 确定起点并构造路径
    start = start_nodes[0]  # 获取起点
    path = hierholzer(start)  # 构造欧拉路径
    
    # 输出结果
    result = " -> ".join(path)  # 用箭头连接路径
    print(result)  # 输出欧拉路径


def main():
    """
    主函数：处理输入并调用欧拉路径查找算法
    生物信息学应用：基因组组装中的De Bruijn图遍历
    """
    find_eulerian_path()  # 调用欧拉路径查找函数


if __name__ == "__main__":
    main()  # 执行主函数
```

## 代码逐行注释
- `defaultdict(list)` - 使用默认字典存储图，面试需说明collections模块的高效数据结构
- `out_degree[start] += 1; in_degree[end] += 1` - 更新出入度，欧拉路径判定的核心步骤
- `if out_deg - in_deg == 1` - 检查是否为欧拉路径起点，有向图欧拉路径判定定理
- `len(start_nodes) == 1 and len(end_nodes) == 1` - 应用欧拉路径度数条件，笔试必考
- `def hierholzer(start_node)` - Hierholzer算法实现，生物信息学中重建序列的关键
- `stack.append(next_node)` - 使用栈模拟递归，避免栈溢出，面试高频考点
- `path.reverse()` - 反转路径获得正确顺序，算法关键步骤
- **ML/DL关联**：该算法在生物信息学中用于DNA序列重建，可应用于基因组数据分析

## 时间/空间复杂度分析
- **时间复杂度**：O(E)，其中E为边数。每条边只被访问一次，Hierholzer算法的线性时间复杂度
- **空间复杂度**：O(V + E)，V为节点数，E为边数，用于存储图和递归栈空间
- **面试高频提问**：为什么时间复杂度是O(E)？因为Hierholzer算法确保每条边只被访问一次

## 同类题目拓展
- **类似题目1**：ROSALIND BA3M - Generate All Maximal Non-Branching Paths - 非分支路径生成
- **类似题目2**：LeetCode 753 - Cracking the Safe - De Bruijn序列构造
- **变种方向1**：如果要求输出所有可能的欧拉路径，如何修改算法？
- **变种方向2**：在基因组组装中，如何处理测序错误和重复序列？

## ML/DL关联思考
- 该算法在生物信息学中用于基因组组装，是De Bruijn图应用的核心
- 欧拉路径思想可用于序列生成模型，确保覆盖所有可能的k-mer模式
- 在图神经网络中，路径遍历可作为节点嵌入的初始化方法
- DNA序列重建问题启发了序列到序列模型的发展