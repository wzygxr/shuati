# Dijkstra算法完整学习指南

## 项目概述

本项目提供了Dijkstra算法及其各种变种的完整实现，涵盖Java、C++、Python三种编程语言。包含从基础应用到高级优化的全方位内容。

## 文件结构

### 基础算法实现
- `Code01_DijkstraLeetcode.java` - LeetCode标准Dijkstra实现
- `code01_dijkstra_leetcode.cpp` - C++版本实现
- `code01_dijkstra_leetcode.py` - Python版本实现

### 算法变种与扩展
- `Code26_MultiSourceShortestPath.java` - 多源最短路径
- `Code27_BellmanFordComparison.java` - Bellman-Ford对比
- `Code28_ShortestPathCount.java` - 最短路径计数
- `Code29_SecondShortestPath.java` - 次短路径问题
- `Code30_ConstrainedShortestPath.java` - 带约束最短路径
- `Code31_AdvancedDijkstraApplications.java` - 高级应用

### 总结文档
- `Dijkstra算法总结.md` - 完整算法总结与题目汇总

## 编译与运行

### Java文件编译
```bash
cd class064
javac *.java
```

### Python文件运行
```bash
cd class064
python code01_dijkstra_leetcode.py
python code31_advanced_dijkstra_applications.py
```

## 各大算法平台题目覆盖

### LeetCode（力扣）
1. **743. 网络延迟时间** - 标准Dijkstra应用
2. **787. K站中转内最便宜的航班** - 带约束最短路径
3. **1514. 概率最大的路径** - 最大概率路径
4. **1631. 最小体力消耗路径** - 瓶颈路径问题

### 洛谷（Luogu）
1. **P4779 单源最短路径（标准版）** - 模板题
2. **P1144 最短路计数** - 路径条数统计
3. **P2865 Roadblocks G** - 严格次短路径

### Codeforces
1. **20C Dijkstra?** - 标准实现与路径重构
2. **449B Jzzhu and Cities** - 多源最短路径

### POJ/HDU/AcWing等
- 覆盖各大OJ平台的经典最短路径题目

## 算法特性对比

| 算法变种 | 时间复杂度 | 空间复杂度 | 适用场景 |
|---------|-----------|-----------|---------|
| 标准Dijkstra | O((V+E)logV) | O(V+E) | 无负权边图 |
| 多源最短路径 | O(K*(V+E)logV) | O(V+E) | 多个源点 |
| 带约束最短路径 | O(K*E*logV) | O(V*K) | 中转次数限制 |
| 次短路径 | O((V+E)logV) | O(V+E) | 备选路线规划 |
| 动态Dijkstra | O(k*logV) | O(V+E) | 权重动态变化 |

## 工程化考量

### 1. 异常处理与边界情况
- 空图、不连通图处理
- 负权边检测与处理
- 大规模数据溢出防护

### 2. 性能优化策略
- 数据结构选择优化
- 内存访问模式优化
- 并行计算支持

### 3. 代码质量保证
- 单元测试覆盖
- 代码审查机制
- 性能基准测试

## 学习路径建议

### 初学者阶段
1. 掌握图的基本概念和Dijkstra原理
2. 实现标准邻接表版本的Dijkstra
3. 完成LeetCode简单难度题目

### 进阶学习阶段
1. 学习各种算法变种和应用场景
2. 深入理解时间空间复杂度分析
3. 对比相关算法（Bellman-Ford、Floyd）

### 高级应用阶段
1. 工程化实现和性能优化
2. 实际场景问题解决
3. 算法创新和改进

## 测试验证

所有Java代码已通过编译测试，Python代码运行正常。关键特性包括：

- ✅ 标准Dijkstra算法实现
- ✅ 多源最短路径支持
- ✅ 带约束条件路径规划
- ✅ 次短路径计算
- ✅ 动态权重更新
- ✅ 多目标优化

## 扩展研究方向

1. **分布式最短路径计算** - 大规模图处理
2. **实时路线规划** - 动态交通状况
3. **多约束优化** - 时间、成本等多目标
4. **机器学习结合** - 预测最优路径

## 贡献指南

欢迎提交改进建议和新的算法实现！请确保：
- 代码符合项目编码规范
- 提供完整的测试用例
- 更新相关文档

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

---

**最后更新**: 2025年10月23日
**维护者**: 算法学习项目组