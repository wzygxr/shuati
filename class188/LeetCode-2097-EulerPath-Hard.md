# 【力扣-LeetCode】-2097-合法重新排列数对-欧拉路径-困难

## 题目原始链接
https://leetcode.cn/problems/valid-arrangement-of-pairs/

## 题目完整描述
给你一个下标从 0 开始的二维整数数组 pairs，其中 pairs[i] = [starti, endi]。如果 pairs 的一个重新排列，满足对每个数对 pairs[i] 都有 pairs[i-1] 的 endi-1 == pairs[i] 的 starti，则称该重新排列是合法的。

请你返回任意一个 pairs 的合法重新排列。注意：pairs 中可能会出现相同的数对，这代表在重新排列中也需要保留相同数目的相同数对。

### 输入输出格式
- 输入：pairs = [[5,1],[4,5],[11,9],[9,4]]
- 输出：[[11,9],[9,4],[4,5],[5,1]]

### 数据范围
- 1 <= pairs.length <= 10^5
- pairs[i].length == 2
- 0 <= starti, endi <= 10^9

### 样例输入输出
```
输入：pairs = [[5,1],[4,5],[11,9],[9,4]]
输出：[[11,9],[9,4],[4,5],[5,1]]
解释：(11 -> 9 -> 4 -> 5 -> 1) 是一个合法的重新排列。

输入：pairs = [[1,3],[3,2],[2,1]]
输出：[[1,3],[3,2],[2,1]]
解释：(1 -> 3 -> 2 -> 1) 是一个合法的重新排列，形成了一个欧拉回路。

输入：pairs = [[1,2],[1,3],[2,1]]
输出：[[1,2],[2,1],[1,3]]
解释：(1 -> 2 -> 1 -> 3) 是一个合法的重新排列，从度数不平衡的节点开始（出度比入度多1）。
```

## 笔试/面试考察点分析
- **考察点1**：欧拉路径判定（有向图中每个节点入度=出度，或只有起点出度比入度多1，终点入度比出度多1）
- **考察点2**：Hierholzer算法实现（递归与迭代两种方式，时间复杂度O(E)）
- **考察点3**：图的表示方法（邻接表 vs 邻接矩阵，离散化处理大数值节点）
- **考察点4**：复杂度分析（时间O(E)，空间O(V+E)）
- **考察点5**：连通性检查（确保图是连通的才能形成欧拉路径）

## 解题思路
这是一个经典的欧拉路径问题，需要找到一条经过每条边恰好一次的路径：

1. 将数对看作有向边，start为起点，end为终点
2. 统计每个节点的入度和出度
3. 根据欧拉路径存在条件确定起点
4. 使用Hierholzer算法找到欧拉路径
5. 由于数值较大，需要离散化处理

## 完整代码实现

```java
import java.util.*;

class Solution {
    public int[][] validArrangement(int[][] pairs) {
        // 构建图的邻接表表示
        Map<Integer, Queue<Integer>> graph = new HashMap<>();
        // 统计入度和出度
        Map<Integer, Integer> inDegree = new HashMap<>();
        Map<Integer, Integer> outDegree = new HashMap<>();
        // 统计连通分量
        Set<Integer> nodes = new HashSet<>();
        
        // 遍历所有数对，构建图结构
        for (int[] pair : pairs) {
            int start = pair[0], end = pair[1];
            graph.computeIfAbsent(start, k -> new PriorityQueue<>()).offer(end);  // 使用优先队列保证字典序最小
            outDegree.put(start, outDegree.getOrDefault(start, 0) + 1);  // 更新出度
            inDegree.put(end, inDegree.getOrDefault(end, 0) + 1);  // 更新入度
            nodes.add(start);
            nodes.add(end);
        }
        
        // 找到欧拉路径的起点 - 笔试面试高频考点：欧拉路径起点判定
        int startNode = pairs[0][0];  // 默认从第一个数对的起点开始
        for (int node : nodes) {
            int out = outDegree.getOrDefault(node, 0);
            int in = inDegree.getOrDefault(node, 0);
            if (out - in == 1) {  // 出度比入度多1，是欧拉路径的起点
                startNode = node;
                break;
            }
        }
        
        // 使用Hierholzer算法构造欧拉路径 - 面试重点算法
        List<Integer> path = hierholzer(graph, startNode);
        
        // 构造结果数组
        int[][] result = new int[pairs.length][2];
        for (int i = 0; i < pairs.length; i++) {
            result[i][0] = path.get(i);
            result[i][1] = path.get(i + 1);
        }
        
        return result;
    }
    
    // Hierholzer算法实现 - 面试重点：递归vs迭代实现的选择
    private List<Integer> hierholzer(Map<Integer, Queue<Integer>> graph, int start) {
        Stack<Integer> stack = new Stack<>();
        List<Integer> circuit = new ArrayList<>();
        stack.push(start);
        
        while (!stack.isEmpty()) {
            int node = stack.peek();
            Queue<Integer> neighbors = graph.get(node);
            
            if (neighbors != null && !neighbors.isEmpty()) {
                int next = neighbors.poll();  // 取出下一个节点
                stack.push(next);  // 压入栈继续遍历
            } else {
                circuit.add(stack.pop());  // 没有未访问的边，加入路径并回溯
            }
        }
        
        Collections.reverse(circuit);  // 反转路径得到正确顺序
        return circuit;
    }
}
```

## 代码逐行注释
- `Map<Integer, Queue<Integer>> graph = new HashMap<>()` - 使用哈希表存储图结构，键为起始节点，值为相邻节点的优先队列，面试需说明为何使用优先队列（保证处理边的顺序性）
- `outDegree.put(start, outDegree.getOrDefault(start, 0) + 1)` - 更新出度，欧拉路径判定核心操作之一
- `if (out - in == 1)` - 判断欧拉路径起点的条件，面试高频考察点
- `Stack<Integer> stack = new Stack<>()` - 使用栈实现Hierholzer算法的迭代版本，避免递归深度过大导致栈溢出
- `Collections.reverse(circuit)` - 反转路径获得正确顺序，这是Hierholzer算法的关键步骤，面试需解释原因
- **ML/DL关联**：该算法将图结构转化为有序序列，可用于图神经网络的节点序列化输入

## 时间/空间复杂度分析
- **时间复杂度**：O(E)，其中E为数对的数量。每个边只会被访问一次，Hierholzer算法的时间复杂度为O(E)
- **空间复杂度**：O(V + E)，V为不同节点的数量，E为数对数量，用于存储图结构和栈空间
- **面试高频提问**：为什么时间复杂度是O(E)而不是O(V+E)？因为每条边只访问一次，与节点数无关

## 同类题目拓展
- **类似题目1**：LeetCode 332. 重新安排行程 - 机场路径问题，同样使用欧拉路径思想
- **类似题目2**：LeetCode 753. 破解保险箱 - 德布鲁因序列问题，也是欧拉路径应用
- **变种方向1**：如果要求访问每条边恰好k次，如何修改算法？
- **变种方向2**：如果是无向图的欧拉路径问题，判定条件如何变化？

## ML/DL关联思考
- 该算法本质上是将图结构数据转化为序列特征，适配RNN/Transformer等序列模型输入
- 在图神经网络中，欧拉路径的遍历顺序可以作为一种节点聚合顺序，影响信息传播
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，弥补GNN局部聚合的不足
- 可以将欧拉路径的构造过程视为图嵌入的一种方法，用于下游机器学习任务