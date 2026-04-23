# 【力扣-LeetCode】-332-重新安排行程-欧拉路径-困难

## 题目原始链接
https://leetcode.cn/problems/reconstruct-itinerary/

## 题目完整描述
给你一份航线列表 tickets ，其中 tickets[i] = [fromi, toi] 表示飞机出发和降落的机场地点。请你对该行程进行重新规划排序。

所有这些机票都属于一个从 JFK（肯尼迪国际机场）出发的先生，所以该行程必须从 JFK 开始。如果存在多种有效的行程，请你按字典排序返回最小的行程组合。

例如，行程 ["JFK", "LGA"] 与 ["JFK", "LGB"] 相比，前者字典序更小。

假定所有机票至少存在一种合理的行程，且所有的机票必须都用一次且只用一次。

### 输入输出格式
- 输入：tickets = [["MUC","LHR"],["JFK","MUC"],["SFO","SJC"],["LHR","SFO"]]
- 输出：["JFK","MUC","LHR","SFO","SJC"]

### 数据范围
- 1 <= tickets.length <= 300
- tickets[i].length == 2
- fromi.length == 3
- toi.length == 3
- fromi 和 toi 由大写英文字母组成
- fromi != toi

### 样例输入输出
```
输入：tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
输出：["JFK","ATL","JFK","SFO","ATL","SFO"]
解释：另一种有效的行程是 ["JFK","SFO","ATL","JFK","ATL","SFO"]，但它字典排序更大更排。
```

## 笔试/面试考察点分析
- **考察点1**：欧拉路径判定（有向图中每个节点入度=出度，或只有起点出度比入度多1，终点入度比出度多1）
- **考察点2**：Hierholzer算法实现（递归与迭代两种方式）
- **考察点3**：字典序优化处理（使用优先队列维护邻接表）
- **考察点4**：图的表示方法（邻接表 vs 邻接矩阵）
- **考察点5**：复杂度分析（时间O(E)，空间O(V+E)）

## 解题思路
这是一个经典的欧拉路径问题，需要找到一条经过每条边恰好一次的路径：

1. 将机票看作有向边，机场看作节点
2. 由于题目保证有解，我们只需要找到欧拉路径
3. 使用Hierholzer算法，从JFK开始搜索
4. 为了保证字典序最小，使用优先队列存储邻接节点
5. 采用后序遍历的方式收集路径（因为要保证能回到当前节点的边最后访问）

## 完整代码实现

```java
import java.util.*;

class Solution {
    // 使用Map存储图，键为出发机场，值为到达机场的优先队列（自动按字典序排序）
    Map<String, PriorityQueue<String>> map = new HashMap<>();
    // 存储最终行程结果
    LinkedList<String> res = new LinkedList<>();

    public List<String> findItinerary(List<List<String>> tickets) {
        // 构建图
        for (List<String> ticket : tickets) {
            String from = ticket.get(0);
            String to = ticket.get(1);
            
            if (!map.containsKey(from)) {
                map.put(from, new PriorityQueue<>());
            }
            map.get(from).offer(to);
        }
        
        // 从JFK开始DFS
        dfs("JFK");
        
        return res;
    }
    
    private void dfs(String curr) {
        // 获取当前机场的所有可达机场
        PriorityQueue<String> pq = map.get(curr);
        
        // 遍历所有可达机场
        while (pq != null && !pq.isEmpty()) {
            // 选择字典序最小的下一个机场
            String next = pq.poll();
            dfs(next);
        }
        
        // 后序遍历，将当前机场加入结果列表头部
        // 这样可以保证路径是按逆序构建的，最终结果是正确的
        res.offerFirst(curr);
    }
}
```

## 代码逐行注释
- `Map<String, PriorityQueue<String>> map = new HashMap<>();` - 使用哈希表存储图结构，键为起始机场，值为按字典序排列的目的地优先队列，面试需说明为何使用优先队列（保证字典序最小）
- `LinkedList<String> res = new LinkedList<>();` - 使用双向链表存储结果，便于在头部插入元素，笔试需注意选择合适的数据结构
- `dfs("JFK");` - 从题目指定的起点JFK开始深度优先搜索，面试中需说明起点的确定性
- `PriorityQueue<String> pq = map.get(curr);` - 获取当前节点的所有邻居，欧拉路径核心操作之一
- `while (pq != null && !pq.isEmpty())` - 遍历当前节点的所有出边，确保每条边只访问一次
- `res.offerFirst(curr);` - 后序遍历将节点加入结果，这是Hierholzer算法的关键，面试需解释为什么这样做
- **ML/DL关联**：该算法将图结构转化为有序序列，可用于图神经网络的节点序列化输入

## 时间/空间复杂度分析
- **时间复杂度**：O(E log E)，其中E为机票数量。每次从优先队列取出元素的时间复杂度为log E，总共E条边
- **空间复杂度**：O(V + E)，V为不同机场的数量，E为机票数量，用于存储图和递归栈空间
- **面试高频提问**：为什么时间复杂度是O(E log E)而不是O(E)？因为需要维护优先队列的有序性

## 同类题目拓展
- **类似题目1**：LeetCode 753. 破解保险箱 - 德布鲁因序列问题，同样使用欧拉路径思想
- **类似题目2**：POJ 1780 - 电话号码 - 类似的序列构造问题
- **变种方向1**：如果允许重复访问某些边，如何修改算法？
- **变种方向2**：如果要求访问每个节点恰好一次（哈密顿路径），复杂度如何变化？

## ML/DL关联思考
- 该算法本质上是将图结构数据转化为序列特征，适配RNN/Transformer等序列模型输入
- 在图神经网络中，欧拉路径的遍历顺序可以作为一种节点聚合顺序，影响信息传播
- 欧拉路径的全局遍历特性有助于捕获图的全局特征，弥补GNN局部聚合的不足
- 可以将欧拉路径的构造过程视为图嵌入的一种方法，用于下游机器学习任务