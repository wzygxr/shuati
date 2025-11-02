# 带权并查集 (Weighted Union-Find)

带权并查集是并查集的一种扩展，它不仅维护元素间的连通关系，还维护元素间的某种权重关系。在合并和查询过程中，需要同时维护这些权重信息。

## 核心思想

1. **基本结构**：
   - `father[i]`: 表示节点i的父节点
   - `dist[i]`: 表示节点i到其根节点的权重（具体含义根据问题而定）

2. **路径压缩**：
   在查找根节点时，同时更新权重信息：
   ```java
   int find(int i) {
       if (i != father[i]) {
           int tmp = father[i];
           father[i] = find(tmp);
           dist[i] += dist[tmp];  // 更新权重
       }
       return father[i];
   }
   ```

3. **集合合并**：
   在合并两个集合时，需要维护权重关系：
   ```java
   void union(int l, int r, int v) {
       int lf = find(l), rf = find(r);
       if (lf != rf) {
           father[lf] = rf;
           dist[lf] = v + dist[r] - dist[l];  // 更新权重关系
       }
   }
   ```

## 常见应用

### 1. 区间和问题
- **问题**：维护区间和关系，支持查询
- **权重含义**：`dist[i]` 表示 `sum[i] - sum[find(i)]`
- **题目**：
  - [洛谷 P8779 推导部分和](https://www.luogu.com.cn/problem/P8779)
  - [HDU 3038 How Many Answers Are Wrong](https://acm.hdu.edu.cn/showproblem.php?pid=3038)

### 2. 数据一致性检测
- **问题**：判断给定的约束条件是否一致
- **权重含义**：`dist[i]` 表示 `sum[i] - sum[find(i)]`
- **题目**：
  - [洛谷 P2294 狡猾的商人](https://www.luogu.com.cn/problem/P2294)

### 3. 队列操作
- **问题**：维护队列合并和查询操作
- **权重含义**：`dist[i]` 表示元素i到队首的距离
- **题目**：
  - [洛谷 P1196 银河英雄传说](https://www.luogu.com.cn/problem/P1196)

### 4. 变量关系推导
- **问题**：维护变量间的倍数关系
- **权重含义**：`dist[x]` 表示变量x是其根节点代表变量的多少倍
- **题目**：
  - [LeetCode 399 除法求值](https://leetcode.cn/problems/evaluate-division/)

### 5. 种类并查集
- **问题**：处理多种类间的关系（如食物链）
- **权重含义**：`dist[i]` 表示元素i与根节点的关系（同类/捕食/被捕食）
- **题目**：
  - [洛谷 P2024 食物链](https://www.luogu.com.cn/problem/P2024)
  - [HDU 1829 A Bug's Life](https://acm.hdu.edu.cn/showproblem.php?pid=1829)

### 6. 敌对关系处理
- **问题**：处理朋友和敌人关系
- **方法**：扩展域并查集（种类并查集）
- **题目**：
  - [洛谷 P1525 关押罪犯](https://www.luogu.com.cn/problem/P1525)
  - [洛谷 P1892 团伙](https://www.luogu.com.cn/problem/P1892)

### 7. 异或关系
- **问题**：维护变量间的异或关系
- **权重含义**：`dist[i]` 表示节点i到根节点的异或值
- **题目**：
  - [HDU 3234 Exclusive-OR](https://acm.hdu.edu.cn/showproblem.php?pid=3234)
  - [UVA 12232 Exclusive OR](https://vjudge.net/problem/UVA-12232)
  - [POJ 1733 Parity game](http://poj.org/problem?id=1733)

### 8. 奇偶性判断
- **问题**：判断区间内元素的奇偶性
- **权重含义**：`dist[i]` 表示节点i与根节点的奇偶关系
- **题目**：
  - [POJ 1733 Parity game](http://poj.org/problem?id=1733)

### 9. 资源追踪
- **问题**：追踪资源的转移和统计
- **权重含义**：`dist[i]` 表示资源i的转移次数
- **题目**：
  - [HDU 3635 Dragon Balls](https://acm.hdu.edu.cn/showproblem.php?pid=3635)

### 10. 逆向处理
- **问题**：处理删除操作和离线查询
- **权重含义**：通过逆向思维转换删除为添加
- **题目**：
  - [ZOJ 3261 Connections in Galaxy War](https://vjudge.net/problem/ZOJ-3261)

### 11. 枚举验证
- **问题**：通过枚举假设验证一致性
- **权重含义**：维护假设下的关系
- **题目**：
  - [POJ 2912 Rochambeau](http://poj.org/problem?id=2912)

## 时间复杂度

- **查找操作**: O(α(n))，其中α是阿克曼函数的反函数，实际上近似O(1)
- **合并操作**: O(α(n))，实际上近似O(1)
- **查询操作**: O(α(n))，实际上近似O(1)

## 空间复杂度

- O(n)，其中n是元素个数

## 典型题目

### 基础题
1. [洛谷 P8779 - 前缀和推导](https://www.luogu.com.cn/problem/P8779) - 区间和问题
2. [洛谷 P2294 - [HNOI2005] 狡猾的商人](https://www.luogu.com.cn/problem/P2294) - 区间和验证
3. [HDU 3038 - How Many Answers Are Wrong](http://acm.hdu.edu.cn/showproblem.php?pid=3038) - 区间和矛盾检测
4. [LeetCode 990 - 等式方程的可满足性](https://leetcode-cn.com/problems/satisfiability-of-equality-equations/) - 等式不等式关系处理

### 进阶题
5. [洛谷 P1196 - [NOI2002] 银河英雄传说](https://www.luogu.com.cn/problem/P1196) - 队列操作问题
6. [LeetCode 399 - 除法求值](https://leetcode-cn.com/problems/evaluate-division/) - 变量关系推导
7. [洛谷 P2024 - [NOI2001] 食物链](https://www.luogu.com.cn/problem/P2024) - 种类并查集
8. [POJ 1182 - 食物链](http://poj.org/problem?id=1182) - 种类并查集
9. [LeetCode 1202 - 交换字符串中的元素](https://leetcode-cn.com/problems/smallest-string-with-swaps/) - 连通分量排序
10. [LeetCode 721 - 账户合并](https://leetcode-cn.com/problems/accounts-merge/) - 字符串连通性
11. [LeetCode 684 - 冗余连接](https://leetcode-cn.com/problems/redundant-connection/) - 图的环检测

### 高级题
12. [洛谷 P1525 - 关押罪犯](https://www.luogu.com.cn/problem/P1525) - 敌对关系处理
13. [洛谷 P1892 - [BOI2003] 团伙](https://www.luogu.com.cn/problem/P1892) - 朋友敌人关系
14. [HDU 1829 - A Bug's Life](http://acm.hdu.edu.cn/showproblem.php?pid=1829) - 性别判断问题
15. [POJ 2492 - A Bug's Life](http://poj.org/problem?id=2492) - 性别判断问题
16. [HDU 3234 - Exclusive-OR](http://acm.hdu.edu.cn/showproblem.php?pid=3234) - 异或关系问题
17. [UVA 12232 - Exclusive-OR](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3384) - 异或关系问题
18. [POJ 1733 - Parity Game](http://poj.org/problem?id=1733) - 奇偶性问题
19. [POJ 1988 - Cube Stacking](http://poj.org/problem?id=1988) - 立方体叠放问题
20. [HDU 3635 - Dragon Balls](http://acm.hdu.edu.cn/showproblem.php?pid=3635) - 带权值的并查集应用
21. [洛谷 P5937 - [CEOI1999] Parity](https://www.luogu.com.cn/problem/P5937) - 奇偶性问题
22. [LeetCode 839 - 相似字符串组](https://leetcode-cn.com/problems/similar-string-groups/) - 字符串相似度分组
23. [LeetCode 947 - 移除最多的同行或同列石头](https://leetcode-cn.com/problems/most-stones-removed-with-same-row-or-column/) - 坐标连通性
24. [LeetCode 1319 - 连通网络的操作次数](https://leetcode-cn.com/problems/number-of-operations-to-make-network-connected/) - 网络连通性
25. [LeetCode 1697 - 检查边长度限制的路径是否存在](https://leetcode-cn.com/problems/checking-existence-of-edge-length-limited-paths/) - 带权路径查询

## 解题技巧

1. **前缀和转换**：区间问题常转换为前缀和问题
2. **扩展域技巧**：敌对关系问题使用扩展域并查集
3. **虚拟节点**：某些问题引入虚拟节点简化处理
4. **权重维护**：在路径压缩和合并时正确维护权重信息
5. **一致性检查**：在合并前检查是否与已有关系矛盾

## 注意事项

1. **权重更新**：在路径压缩和集合合并时要正确更新权重
2. **边界处理**：注意数组边界和特殊输入的处理
3. **精度问题**：浮点数运算时注意精度问题
4. **初始化**：正确初始化father和dist数组

## 本目录文件说明

- `Code01_DerivePartialSums.java/cpp/py` - 推导部分和问题
- `Code02_CunningMerchant.java/py` - 狡猾的商人问题
- `Code03_WrongAnswers.java/py` - 错误答案数量问题
- `Code04_LegendOfHeroes.java/cpp/py` - 银河英雄传说问题
- `Code05_EvaluateDivision.java/py` - 除法求值问题
- `Code06_JudgeFoodChain.java/py` - 甄别食物链问题
- `Code07_DetainCriminals.java/py` - 关押罪犯问题
- `Code08_Gangster.java/py` - 团伙问题
- `Code09_ExclusiveOR.java/py` - 异或关系问题
- `Code10_ParityGame.java/cpp/py` - Parity game问题
- `Code11_DragonBalls.java/cpp/py` - Dragon Balls问题
- `Code12_Rochambeau.java/cpp/py` - Rochambeau问题
- `Code13_ConnectionsInGalaxyWar.java/cpp/py` - Connections in Galaxy War问题
- `Code14_BugsLife.java/cpp/py` - A Bug's Life问题

每道题目都提供了Java、C++（部分）和Python三种语言的实现，并附有详细的注释和复杂度分析。