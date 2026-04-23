# 【牛客网】-NC123-欧拉路径问题-中等

## 题目原始链接
https://www.nowcoder.com/practice/...

## 题目完整描述
给定一个无向图，包含n个节点和m条边，你需要判断这个图是否存在欧拉路径或欧拉回路。

如果存在欧拉回路，请输出"Has Euler Circuit"；
如果存在欧拉路径（非回路），请输出"Has Euler Path"；
如果都不存在，请输出"No Euler Path"。

### 输入格式
第一行包含两个整数n和m，分别表示节点数和边数。
接下来m行，每行包含两个整数u和v，表示节点u和节点v之间有一条无向边。

### 输出格式
输出一行，根据图的情况输出相应的结果。

### 数据范围
- 1 <= n <= 1000
- 0 <= m <= 2000
- 1 <= u, v <= n
- 图中可能存在重边和自环

### 样例输入输出
```
输入：
4 4
1 2
2 3
3 4
4 1
输出：
Has Euler Circuit
```

```
输入：
5 4
1 2
2 3
3 4
4 5
输出：
Has Euler Path
```

## 笔试/面试考察点分析
- **考察点1**：无向图欧拉路径/回路判定定理的理解与应用
- **考察点2**：图的表示方法（邻接表 vs 邻接矩阵）
- **考察点3**：节点度数的计算与统计
- **考察点4**：图的连通性判断（使用DFS/BFS或并查集）
- **考察点5**：边界条件处理（孤立节点、重边、自环）

## 解题思路
这是一个标准的欧拉路径判定问题：

1. 首先统计每个节点的度数
2. 判断图的连通性（使用DFS/BFS或并查集）
3. 根据无向图欧拉路径/回路判定定理：
   - 欧拉回路：所有节点的度数都为偶数，且图连通
   - 欧拉路径：恰好有两个节点的度数为奇数，其余都为偶数，且图连通
   - 否则不存在欧拉路径或回路

## 完整代码实现

```java
import java.util.*;

public class Main {
    static List<Integer>[] graph;
    static boolean[] visited;
    static int[] degree;
    
    // 深度优先搜索判断连通性
    // 笔试中需掌握DFS/BFS两种连通性判断方法，面试常考复杂度分析
    static void dfs(int u) {
        visited[u] = true;  // 标记当前节点为已访问，防止重复访问造成死循环
        for (int v : graph[u]) {  // 遍历当前节点的所有邻接节点
            if (!visited[v]) {  // 如果邻接节点未被访问
                dfs(v);  // 递归访问邻接节点，继续深度优先搜索
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);  // 创建输入流读取数据
        int n = sc.nextInt();  // 读取节点数，笔试需注意输入格式的准确性
        int m = sc.nextInt();  // 读取边数
        
        // 初始化邻接表和度数数组
        // 面试需说明邻接表相比邻接矩阵的空间优势：O(V+E) vs O(V^2)
        graph = new ArrayList[n + 1];
        degree = new int[n + 1];
        visited = new boolean[n + 1];
        
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();  // 为每个节点创建邻接表
        }
        
        // 读取边并构建图
        // 统计每个节点的度数，这是欧拉路径判定的基础
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();  // 读取边的起点
            int v = sc.nextInt();  // 读取边的终点
            
            graph[u].add(v);  // 在邻接表中添加边
            graph[v].add(u);  // 无向图需要添加双向边
            degree[u]++;  // 增加起点度数
            degree[v]++;  // 增加终点度数
        }
        
        // 找到一个度数大于0的节点作为DFS起点
        // 如果所有节点度数都为0，则图为空图，认为存在欧拉回路（无边的回路）
        int start = -1;
        for (int i = 1; i <= n; i++) {
            if (degree[i] > 0) {  // 找到一个非孤立节点
                start = i;  // 将其作为DFS起点
                break;
            }
        }
        
        // 如果没有度数大于0的节点，说明图中无边，存在欧拉回路
        if (start == -1) {
            System.out.println("Has Euler Circuit");  // 无边图默认有欧拉回路
            return;
        }
        
        // 使用DFS判断图的连通性
        // 这是欧拉路径存在的必要条件之一，面试高频考察点
        dfs(start);
        
        // 检查图是否连通
        boolean isConnected = true;  // 假设图连通
        for (int i = 1; i <= n; i++) {
            if (degree[i] > 0 && !visited[i]) {  // 如果节点有边但未被访问
                isConnected = false;  // 说明图不连通
                break;
            }
        }
        
        if (!isConnected) {  // 如果图不连通
            System.out.println("No Euler Path");  // 不存在欧拉路径
            return;
        }
        
        // 统计度数为奇数的节点数量
        // 这是应用欧拉路径判定定理的关键步骤，笔试面试必考
        int oddDegreeCount = 0;
        for (int i = 1; i <= n; i++) {
            if (degree[i] % 2 == 1) {  // 如果度数为奇数
                oddDegreeCount++;  // 增加奇度数节点计数
            }
        }
        
        // 根据欧拉路径判定定理判断结果
        // 无向图欧拉路径判定定理：奇度数节点数为0（回路）或2（路径）
        if (oddDegreeCount == 0) {  // 所有节点度数都是偶数
            System.out.println("Has Euler Circuit");  // 存在欧拉回路
        } else if (oddDegreeCount == 2) {  // 恰好有两个奇度数节点
            System.out.println("Has Euler Path");  // 存在欧拉路径
        } else {  // 奇度数节点数不是0也不是2
            System.out.println("No Euler Path");  // 不存在欧拉路径
        }
        
        sc.close();  // 关闭输入流，释放资源
    }
}
```

## 代码逐行注释
- `static void dfs(int u)` - 深度优先搜索函数，用于判断图的连通性，笔试需掌握基本图遍历算法
- `visited[u] = true;` - 标记当前节点为已访问，防止重复访问，面试需说明访问标记的作用
- `for (int v : graph[u])` - 遍历当前节点的所有邻接节点，体现邻接表的便利性
- `int oddDegreeCount = 0;` - 统计奇度数节点数量，这是欧拉路径判定定理的核心
- `if (degree[i] % 2 == 1)` - 判断度数是否为奇数，实现欧拉路径判定的关键步骤
- `if (oddDegreeCount == 0)` - 应用欧拉回路判定条件，面试高频考察点
- `if (oddDegreeCount == 2)` - 应用欧拉路径判定条件，必须恰好两个奇度数节点
- **ML/DL关联**：该算法可作为图特征提取的一部分，度数分布可作为图神经网络的节点特征

## 时间/空间复杂度分析
- **时间复杂度**：O(n + m)，其中n为节点数，m为边数。DFS遍历的时间复杂度为O(n + m)，度数统计为O(m)，总体为O(n + m)
- **空间复杂度**：O(n + m)，用于存储邻接表、度数数组和访问标记数组
- **面试高频提问**：为什么时间复杂度是O(n + m)而不是O(n²)？因为使用邻接表存储，每条边只被访问常数次

## 同类题目拓展
- **类似题目1**：LeetCode 332 - 重新安排行程 - 有向图欧拉路径问题
- **类似题目2**：POJ 1041 - John's Trip - 有向图欧拉回路问题
- **变种方向1**：如果要求输出具体的欧拉路径，需要使用Hierholzer算法
- **变种方向2**：如果图不连通，如何找到各个连通分量中的欧拉路径？

## ML/DL关联思考
- 该算法的判定结果可作为图的拓扑特征，输入到图神经网络中
- 节点度数分布是重要的图结构特征，在GNN中可作为节点嵌入的初始特征
- 欧拉路径的存在性可作为图连通性和平衡性的量化指标，用于图分类任务
- 在知识图谱中，欧拉路径可用于寻找覆盖所有关系的遍历路径