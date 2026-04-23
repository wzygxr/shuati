# 强连通分量与缩点问题合集 - 总目录

## 项目概述

本项目系统性收集了强连通分量（SCC）与缩点相关的经典问题，涵盖全球主流OJ平台的典型题目，提供详细解析、代码实现（含逐行中文注释）和机器学习/深度学习关联分析。

**覆盖平台**：
- 国内平台：洛谷、力扣、AcWing、牛客网、PTA、蓝桥杯、CCF CSP、计蒜客、POJ、HDU、ZOJ
- 国际平台：Codeforces、AtCoder、UVA、Kick Start、Hacker Cup、Topcoder、HackerRank

## 目录结构

```
e:\代码\class189\
├── luogu/                 # 洛谷平台题目
├── codeforces/            # Codeforces平台题目
├── atcoder/               # AtCoder平台题目
├── leetcode/              # LeetCode平台题目
├── acwing/                # AcWing平台题目
├── poj/                   # POJ平台题目
├── hdu/                   # HDU平台题目
├── zoj/                   # ZOJ平台题目
├── niuke/                 # 牛客网平台题目
├── pta/                   # PTA平台题目
├── 蓝桥杯/                # 蓝桥杯平台题目
├── ccf_csp/               # CCF CSP认证题目
├── jisuanke/              # 计蒜客平台题目
├── uva/                   # UVA平台题目
├── kickstart/             # Google Kick Start题目
├── hacker_cup/            # Facebook Hacker Cup题目
├── topcoder/              # Topcoder平台题目
├── hackerrank/            # HackerRank平台题目
├── templates/             # 算法模板
│   └── Tarjan算法模板-SCC-缩点-完整版.md
├── tutorial/              # 算法教程
│   ├── 01-强连通分量基础概念与算法对比.md
│   ├── 02-缩点技术与DAG上的动态规划.md
│   └── 03-强连通分量与缩点面试题集锦.md
├── interview_questions_bank.md        # 面试题库
├── 强连通分量算法详解与面试指南.md    # 算法详解
├── 机器学习与深度学习中的图算法应用.md # ML/DL应用
└── README.md              # 项目说明
```

## 已实现题目列表（按平台分类）

### 洛谷（Luogu）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [洛谷-B3609-强连通分量模板](./luogu/洛谷-B3609-强连通分量模板-强连通分量与缩点-入门.md) | 入门 | 强连通分量模板 | Tarjan模板、SCC计数 | ⭐⭐⭐ |
| [洛谷-P2341-受欢迎的牛](./luogu/洛谷-P2341-受欢迎的牛-强连通分量与缩点-普及提高.md) | 普及/提高- | 受欢迎的牛 | 缩点、出度分析 | ⭐⭐⭐⭐ |
| [洛谷-P3387-缩点模板](./luogu/洛谷-P3387-缩点模板-强连通分量与缩点-提高省选.md) | 提高+/省选- | 缩点模板 | 缩点+DAG DP | ⭐⭐⭐⭐⭐ |
| [洛谷-P2746-校园网络](./luogu/洛谷-P2746-校园网络-强连通分量与缩点-提高省选.md) | 提高+/省选- | 校园网络 | 最少加边使图强连通 | ⭐⭐⭐⭐ |

### LeetCode

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [LeetCode-1192-查找集群中的关键连接](./leetcode/LeetCode-1192-查找集群中的关键连接-强连通分量与缩点-困难.md) | 困难 | 查找集群中的关键连接 | 桥边、边双连通 | ⭐⭐⭐ |
| [LeetCode-1462-课程表IV](./leetcode/LeetCode-1462-课程表IV-强连通分量与缩点-中等.md) | 中等 | 课程表 IV | 传递闭包、Floyd算法 | ⭐⭐⭐⭐ |
| [LeetCode-1557-可以到达所有点的最少点数目](./leetcode/LeetCode-1557-可以到达所有点的最少点数目-强连通分量与缩点-中等.md) | 中等 | 可以到达所有点的最少点数目 | 入度分析、SCC缩点 | ⭐⭐⭐ |
| [LeetCode-2360-图中的最长环](./leetcode/LeetCode-2360-图中的最长环-强连通分量与缩点-中等.md) | 中等 | 图中的最长环 | SCC、最长环 | ⭐⭐⭐ |

### AcWing

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [AcWing-1174-受欢迎的牛](./acwing/AcWing-1174-受欢迎的牛-强连通分量与缩点-中等.md) | 中等 | 受欢迎的牛 | 缩点、出度分析 | ⭐⭐⭐⭐ |

### HDU

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [HDU-1269-迷宫城堡](./hdu/HDU-1269-迷宫城堡-强连通分量与缩点-中等.md) | 中等 | 迷宫城堡 | SCC模板 | ⭐⭐⭐ |
| [HDU-2767-证明难题](./hdu/HDU-2767-证明难题-强连通分量与缩点-中等.md) | 中等 | 证明难题 | 最少加边使图强连通 | ⭐⭐⭐⭐ |

### POJ

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [POJ-2186-Popular-Cows](./poj/POJ-2186-Popular-Cows-强连通分量与缩点-中等.md) | 中等 | Popular Cows | 缩点、出度分析 | ⭐⭐⭐⭐ |
| [POJ-2553-The-Bottom-of-a-Graph](./poj/POJ-2553-The-Bottom-of-a-Graph-强连通分量与缩点-中等.md) | 中等 | The Bottom of a Graph | 缩点、出度分析 | ⭐⭐⭐ |

### 牛客网

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [牛客-NC151-判断有向图是否有环](./niuke/牛客-NC151-判断有向图是否有环-强连通分量与缩点-中等.md) | 中等 | 判断有向图是否有环 | SCC/拓扑排序 | ⭐⭐⭐ |
| [牛客-NC200-朋友圈](./niuke/牛客-NC200-朋友圈-强连通分量与缩点-中等.md) | 中等 | 朋友圈 | 并查集、连通分量 | ⭐⭐⭐ |
| [牛客-NC567-CompanyInterview](./niuke/NC567-CompanyInterview-强连通分量与缩点-中等.md) | 中等 | Company Interview | SCC应用 | ⭐⭐⭐ |

### Codeforces

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [Codeforces-427C-Checkposts](./codeforces/CF-427C-Checkposts-强连通分量与缩点-中等.md) | 中等 | Checkposts | SCC+最小权覆盖 | ⭐⭐⭐ |

### AtCoder

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [AtCoder-ABC174F-RangeSetQuery](./atcoder/AtCoder-ABC174F-RangeSetQuery-强连通分量与缩点-中等.md) | 中等 | Range Set Query | SCC与数据结构结合 | ⭐⭐⭐⭐ |

### ZOJ

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [ZOJ-3790-News Spread](./zoj/medium/ZOJ-3790-NewsSpread-Medium.md) | 中等 | News Spread | SCC传播、DAG遍历 | ⭐⭐⭐ |

### PTA（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [PTA-甲级-1013-Battle-Over-Cities](./pta/PTA-甲级-1013-Battle-Over-Cities-强连通分量与缩点-中等.md) | 中等 | Battle Over Cities | 连通分量、割点 | ⭐⭐⭐ |
| [PTA-甲级-1146-Topological-Order](./pta/PTA-甲级-1146-Topological-Order-强连通分量与缩点-中等.md) | 中等 | Topological Order | 拓扑序验证、DAG | ⭐⭐⭐⭐ |

### 蓝桥杯（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [蓝桥杯-省赛-有向图强连通分量](./蓝桥杯/蓝桥杯-省赛-有向图强连通分量-强连通分量与缩点-中等.md) | 中等 | 有向图强连通分量 | Tarjan模板 | ⭐⭐⭐ |

### CCF CSP（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [CCF-CSP-202112-3-邻域均值](./ccf_csp/CCF-CSP-202112-3-邻域均值-强连通分量与缩点-中等.md) | 中等 | 邻域均值 | 二维前缀和、图遍历思维 | ⭐⭐⭐ |

### 计蒜客

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [JSK-123-SCC-Tarjan-Template](./jisuanke/JSK-123-SCC-Tarjan-Template-强连通分量与缩点-入门.md) | 入门 | SCC Tarjan Template | Tarjan模板 | ⭐⭐⭐ |

### UVA

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [UVA-11504-Dominos](./uva/UVA-11504-Dominos-强连通分量与缩点-中等.md) | 中等 | Dominos | SCC+最少起点 | ⭐⭐⭐ |

### Google Kick Start（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [Kick-Start-2020-Round-A-Allocation](./kickstart/Kick-Start-2020-Round-A-Allocation-强连通分量与缩点-入门.md) | 入门 | Allocation | 贪心、排序 | ⭐⭐ |

### Facebook Hacker Cup（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [Hacker-Cup-2020-Qualification-Travel-Restrictions](./hacker_cup/Hacker-Cup-2020-Qualification-Travel-Restrictions-强连通分量与缩点-中等.md) | 中等 | Travel Restrictions | 图建模、可达性 | ⭐⭐⭐⭐ |

### Topcoder（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [Topcoder-SRM-719-Div2-Hard-Longlongong](./topcoder/Topcoder-SRM-719-Div2-Hard-Longlongong-强连通分量与缩点-困难.md) | 困难 | Longlongong Path | SCC+缩点+DP | ⭐⭐⭐⭐⭐ |

### HackerRank（新增）

| 文件路径 | 难度 | 题目名称 | 考察点 | ML/DL关联度 |
|---------|------|----------|--------|-------------|
| [HackerRank-Connected-Cells-in-a-Grid](./hackerrank/HackerRank-Connected-Cells-in-a-Grid-强连通分量与缩点-中等.md) | 中等 | Connected Cells in a Grid | 网格连通分量 | ⭐⭐⭐⭐ |

### 算法模板

| 文件路径 | 内容 | 重要度 |
|---------|------|--------|
| [Tarjan算法模板-SCC-缩点-完整版](./templates/Tarjan算法模板-SCC-缩点-完整版.md) | 完整Tarjan+缩点+拓扑排序模板 | ⭐⭐⭐⭐⭐ |

### 算法教程

| 文件路径 | 内容 | 重要度 |
|---------|------|--------|
| [01-强连通分量基础概念与算法对比](./tutorial/01-强连通分量基础概念与算法对比.md) | 三大SCC算法对比详解 | ⭐⭐⭐⭐⭐ |
| [02-缩点技术与DAG上的动态规划](./tutorial/02-缩点技术与DAG上的动态规划.md) | 缩点原理与DAG应用 | ⭐⭐⭐⭐⭐ |
| [03-强连通分量与缩点面试题集锦](./tutorial/03-强连通分量与缩点面试题集锦.md) | 16道高频面试题及答案 | ⭐⭐⭐⭐⭐ |

### 面试题库

| 文件路径 | 内容 | 重要度 |
|---------|------|--------|
| [interview_questions_bank.md](./interview_questions_bank.md) | SCC算法面试题库（18道高频题） | ⭐⭐⭐⭐⭐ |

## 各文件内容概要

### 入门题目（适合初学者）

**洛谷-B3609-强连通分量模板**
- 问题描述：给定有向图，求所有强连通分量
- 核心思路：Tarjan算法模板
- 算法复杂度：O(N+M)
- ML关联：图结构数据预处理

**HDU-1269-迷宫城堡**
- 问题描述：判断图强连通
- 核心思路：SCC计数
- 算法复杂度：O(N+M)
- ML关联：网络连通性检测

**JSK-123-SCC-Tarjan-Template**
- 问题描述：强连通分量模板题
- 核心思路：Tarjan算法基础
- 算法复杂度：O(N+M)
- ML关联：图算法基础训练

### 进阶题目（缩点应用）

**洛谷-P2341-受欢迎的牛 / AcWing-1174-受欢迎的牛 / POJ-2186-Popular Cows**
- 问题描述：找出被所有其他节点可达的节点
- 核心思路：SCC缩点 + 出度分析
- 算法复杂度：O(N+M)
- ML关联：社交网络意见领袖发现

**HDU-2767-证明难题 / POJ-1236-Network of Schools**
- 问题描述：最少加多少条边使图强连通
- 核心思路：SCC缩点 + DAG入度出度分析
- 算法复杂度：O(N+M)
- ML关联：网络鲁棒性分析

**洛谷-P2746-校园网络**
- 问题描述：两部分 - 任务A（有向图）和任务B（无向图）
- 核心思路：SCC缩点 + 最少加边
- 算法复杂度：O(N+M)
- ML关联：网络设计优化

### 高级题目（综合应用）

**洛谷-P3387-缩点模板**
- 问题描述：缩点后求DAG最长路
- 核心思路：SCC缩点 + 拓扑排序 + DP
- 算法复杂度：O(N+M)
- ML关联：图特征提取、层次化表示

**Topcoder-SRM-719-Div2-Hard-Longlongong Path**
- 问题描述：带权图最长路径，含正权环检测
- 核心思路：SCC + 缩点 + DAG DP
- 算法复杂度：O(N+M)
- ML关联：图神经网络路径优化

**AtCoder-ABC174F-RangeSetQuery**
- 问题描述：区间查询与图论结合
- 核心思路：SCC + 数据结构
- 算法复杂度：O((N+M) log N)
- ML关联：复杂查询优化

## 技术特色

1. **详细中文注释**：几乎每行代码都有详细注释，解释代码功能、算法原理和实现细节

2. **复杂度分析**：每个题目都提供完整的时间/空间复杂度推导过程

3. **ML/DL关联**：分析算法在机器学习中的应用场景，包括：
   - 图神经网络（GNN）优化
   - 社交网络分析
   - 知识图谱推理
   - 图表示学习
   - 网络鲁棒性分析

4. **同类题目扩展**：提供相关题目推荐，帮助举一反三

5. **面试口述要点**：每个题目都提供面试时如何口述解答的模板

6. **全覆盖平台**：覆盖国内外主流OJ平台，包括：
   - 国内核心平台：洛谷、力扣、AcWing、牛客网、PTA、蓝桥杯、CCF CSP
   - 高校OJ：POJ、HDU、ZOJ
   - 国际平台：Codeforces、AtCoder、UVA、Kick Start、Hacker Cup、Topcoder、HackerRank

## 使用建议

### 笔试准备

1. **基础阶段**
   - 掌握Tarjan算法模板（洛谷-B3609、计蒜客-JSK-123）
   - 理解dfn/low数组的含义
   - 练习HDU-1269迷宫城堡、蓝桥杯省赛题

2. **进阶阶段**
   - 学习缩点技巧（洛谷-P2341、P3387）
   - 掌握最少加边公式（HDU-2767、POJ-1236）
   - 理解入度出度分析
   - 练习PTA甲级题目

3. **冲刺阶段**
   - 刷LeetCode相关题目
   - 练习国际平台题目（Kick Start、Hacker Cup入门题）
   - 练习面试题库中的高频问题
   - 准备ML/DL关联问题的回答

### 面试准备

1. **算法原理**
   - 能够清晰解释Tarjan算法核心思想
   - 说明dfn/low数组的作用和更新逻辑
   - 比较三种SCC算法（Kosaraju、Tarjan、Gabow）的优缺点

2. **手写代码**
   - 5分钟内写出Tarjan模板
   - 正确实现缩点功能
   - 处理边界情况

3. **应用场景**
   - 准备SCC在ML/DL中的应用案例
   - 理解缩点如何简化图问题
   - 了解网络分析中的实际应用

4. **面试题库**
   - 熟记interview_questions_bank.md中的16道高频题
   - 练习tutorial/03-强连通分量与缩点面试题集锦中的标准答案

### 竞赛提升

1. 挑战省选难度题目（洛谷-P2746、P3387）
2. 学习双连通分量（割点、桥边）与SCC的对比
3. 掌握缩点后各种DP技巧
4. 挑战国际竞赛难题（Topcoder Div2 Hard、Codeforces Div1）

## 学习路径推荐

```
入门 → 进阶 → 挑战
│      │       │
│      │       ├─ Topcoder-SRM-719-Hard (困难)
│      │       ├─ 洛谷-P3387 缩点模板 (省选)
│      │       └─ AtCoder-ABC174F (综合)
│      │
│      ├─ 洛谷-P2341 受欢迎的牛
│      ├─ HDU-2767 证明难题
│      ├─ PTA-甲级-1146 拓扑序验证
│      └─ Hacker-Cup-Travel-Restrictions
│
├─ 洛谷-B3609 强连通分量模板
├─ HDU-1269 迷宫城堡
├─ 蓝桥杯-省赛-有向图强连通分量
├─ Kick-Start-2020-Round-A-Allocation
└─ 计蒜客-JSK-123-SCC-Tarjan-Template
```

## 快速导航

### 按难度分类

**入门**：
- 洛谷-B3609
- HDU-1269
- 计蒜客-JSK-123
- 蓝桥杯-省赛-有向图强连通分量
- Kick-Start-2020-Round-A-Allocation

**中等**：
- 洛谷-P2341
- AcWing-1174
- POJ-2186
- HDU-2767
- LeetCode-1462、1557、2360
- Codeforces-427C
- PTA-甲级-1013、1146
- Hacker-Cup-Travel-Restrictions
- HackerRank-Connected-Cells

**困难**：
- 洛谷-P3387
- LeetCode-1192
- Topcoder-SRM-719-Hard
- AtCoder-ABC174F

### 按主题分类

**Tarjan模板**：
- 洛谷-B3609
- HDU-1269
- 计蒜客-JSK-123
- templates/Tarjan算法模板

**缩点应用**：
- 洛谷-P2341、P3387
- AcWing-1174
- POJ-2186、2553
- Topcoder-SRM-719

**DAG上的DP**：
- 洛谷-P3387
- Topcoder-SRM-719

**最少加边问题**：
- 洛谷-P2746
- HDU-2767

---

**最后更新：2026年1月30日**

**覆盖平台**：17个平台，30+道题目

**维护者：算法竞赛与面试准备团队**
