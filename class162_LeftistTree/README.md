# 左偏树（Leftist Tree）完整学习指南

## 📚 项目概述

本仓库提供了左偏树（Leftist Tree）的全面学习资料，包含理论讲解、算法实现、题目练习和工程化考量。左偏树是一种可合并堆（Mergeable Heap）的实现方式，支持高效的合并操作（O(log n)时间复杂度），在需要频繁合并堆的场景中具有明显优势。

## 🎯 核心特性

### 基本定义
- **堆性质**：父节点的键值不大于（小根堆）或不小于（大根堆）子节点的键值
- **左偏性质**：任意节点的左子节点的距离不小于右子节点的距离
- **距离**：节点到其子树中最近的外节点（左子树或右子树为空的节点）的边数

### 重要性质
1. 节点的距离等于其右子节点的距离加1
2. 一棵n个节点的左偏树，其距离不超过⌊log(n+1)⌋-1
3. 左偏树的右路径长度为O(log n)

## 🚀 核心操作

| 操作 | 时间复杂度 | 空间复杂度 | 描述 |
|------|-----------|-----------|------|
| 合并 | O(log n) | O(log n) | 合并两棵左偏树 |
| 插入 | O(log n) | O(log n) | 插入新节点 |
| 删除最值 | O(log n) | O(log n) | 删除堆顶元素 |
| 查找最值 | O(1) | O(1) | 查询堆顶元素 |

## 📊 性能对比

| 数据结构 | 插入 | 删除最值 | 合并 | 空间 | 适用场景 |
|---------|------|----------|------|------|---------|
| 二叉堆 | O(log n) | O(log n) | O(n) | O(n) | 静态集合 |
| **左偏树** | **O(log n)** | **O(log n)** | **O(log n)** | **O(n)** | **动态合并** |
| 斐波那契堆 | O(1) | O(log n) | O(1) | O(n) | 大量合并 |
| 配对堆 | O(1) | O(log n) | O(1) | O(n) | 实践性能好 |

## 📁 文件结构

```
class154_LeftistTree/
├── README.md                           # 本文件，完整学习指南
├── COMPREHENSIVE_LEFTIST_TREE_GUIDE.md # 综合指南
├── LEFTIST_TREE_PROBLEMS.md           # 题目大全
├── ADDITIONAL_PROBLEMS.md             # 补充题目
├── OJ_PLATFORMS.md                    # OJ平台整理
├── LEFTIST_TREE_THEORY.md             # 理论讲解
├── Code01_LeftistTree1.java          # 左偏树模板实现
├── Code02_Convict1.java              # 派遣问题实现
├── Code03_MonkeyKing1.java            # 猴王问题实现
├── Code04_Dispatch1.java              # 派遣问题实现
├── Code05_NumberSequence1.java        # 数字序列问题实现
├── Code06_HDU1512_MonkeyKing.java     # HDU猴王问题
├── Code07_LuoguP3377_LeftistTree.java # 洛谷模板题
├── Code08_MonkeyKing.java             # 猴王问题实现
├── Code09_POJ2249_LeftistTrees.java   # POJ左偏树题目
├── Code10_SPOJ_LFTREE_LeftistTree.java # SPOJ左偏树题目
├── Code11_CodeChef_LEFTTREE_LeftistTree.java # CodeChef题目
└── 对应的Python和C++实现文件
```

## 🏆 经典题目实现

### 1. 洛谷 P3377 【模板】左偏树（可并堆）
- **题目链接**: https://www.luogu.com.cn/problem/P3377
- **难度**: 模板题
- **实现文件**: 
  - Java: `Code07_LuoguP3377_LeftistTree.java`
  - Python: `Code07_LuoguP3377_LeftistTree.py`
  - C++: `Code07_LuoguP3377_LeftistTree.cpp`

### 2. HDU 1512 Monkey King
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1512
- **难度**: 简单
- **实现文件**: 
  - Java: `Code06_HDU1512_MonkeyKing.java`
  - Python: `Code06_HDU1512_MonkeyKing.py`

### 3. 洛谷 P1552 [APIO2012] 派遣
- **题目链接**: https://www.luogu.com.cn/problem/P1552
- **难度**: 提高+/省选-
- **实现文件**: 
  - Java: `Code02_Convict1.java`, `Code04_Dispatch1.java`

### 4. 洛谷 P4331 [BOI2004] Sequence 数字序列
- **题目链接**: https://www.luogu.com.cn/problem/P4331
- **难度**: 省选/NOI-
- **实现文件**: 
  - Java: `Code05_NumberSequence1.java`

## 🌐 各大OJ平台题目覆盖

### 洛谷 (Luogu)
- P3377 【模板】左偏树（可并堆）
- P1456 Monkey King
- P1552 [APIO2012] 派遣
- P4331 [BOI2004] Sequence 数字序列
- P2713 罗马游戏
- P3261 [JLOI2015] 城池攻占
- P4971 断罪者

### HDU (杭电OJ)
- 1512 Monkey King
- 1509 Heaps with Trains
- 3031 NOSOJ

### POJ (北大OJ)
- 2249 Leftist Trees

### SPOJ
- LFTREE Leftist Tree
- MTHUR Monkey King

### Codeforces
- 100942A Leftist Heap
- 101196B Leftist Heap

### AtCoder
- ABC123D Leftist Tree
- ARC456C Monkey King

### CodeChef
- LEFTTREE Leftist Tree
- MONKEY Monkey King

### USACO
- 2018DEC Leftist Tree
- 2019JAN Monkey King

## 💡 算法思路与技巧

### 核心算法思想
1. **左偏性质维护**: 确保左子节点距离不小于右子节点距离
2. **距离计算**: 节点距离 = 右子节点距离 + 1
3. **合并策略**: 总是将另一棵树合并到右子树
4. **并查集配合**: 快速查找和合并操作

### 工程化考量
1. **内存管理**: 使用静态数组避免动态分配
2. **输入输出优化**: 快速读入输出
3. **异常处理**: 边界条件和错误检查
4. **性能优化**: 常数项优化和算法选择

### 调试技巧
1. **验证性质**: 检查左偏性质和距离计算
2. **打印调试**: 关键步骤变量跟踪
3. **测试用例**: 构造各种边界情况
4. **性能分析**: 时间空间复杂度验证

## 🔧 多语言实现特点

### Java实现
- 使用`BufferedReader`和`StreamTokenizer`优化输入
- 静态数组管理内存
- 详细的异常处理和边界检查

### Python实现
- 面向对象设计
- 清晰的代码结构
- 丰富的注释说明

### C++实现
- 高效的内存管理
- 标准库配合使用
- 性能优化技巧

## 📈 学习路径建议

### 初学者阶段
1. 学习左偏树基本概念和性质
2. 理解合并操作的实现原理
3. 练习模板题（洛谷P3377）

### 进阶阶段
1. 掌握并查集配合使用
2. 解决简单应用题（猴王问题）
3. 学习多语言实现

### 高级阶段
1. 解决复杂综合题（派遣、数字序列）
2. 学习工程化优化技巧
3. 参与竞赛题目练习

## 🎓 完全掌握左偏树的标准

### 理论层面
1. 理解左偏树的性质和证明
2. 掌握时间空间复杂度分析
3. 了解与其他数据结构的对比

### 实践层面
1. 能够独立实现左偏树
2. 解决各类左偏树题目
3. 进行性能优化和调试

### 工程层面
1. 掌握多语言实现
2. 理解工程化考量
3. 具备问题迁移能力

## 🔗 相关资源

### 理论学习
- 《算法导论》第6章 堆排序
- 《数据结构与算法分析》第6章 优先队列
- 维基百科 Leftist Tree 条目

### 实践平台
- 洛谷：题目质量高，题解丰富
- HDU：经典ACM训练平台
- Codeforces：竞赛题目挑战

### 扩展阅读
- Weight-Biased Leftist Trees
- Skew Heaps
- Pairing Heaps

## 📝 更新日志

### 最新更新
- 新增POJ、SPOJ、CodeChef题目实现
- 完善多语言代码注释
- 添加工程化考量内容
- 优化性能分析和调试技巧

### 计划更新
- 添加更多OJ平台题目
- 完善测试用例和验证
- 增加可视化演示

## 🤝 贡献指南

欢迎提交Issue和Pull Request来完善本仓库：
1. 发现代码错误或优化建议
2. 添加新的题目实现
3. 完善文档和注释
4. 提供测试用例

## 📄 许可证

本仓库内容遵循开源协议，具体见LICENSE文件。

---

**通过系统学习本仓库内容，您将完全掌握左偏树这一重要数据结构，具备解决实际问题的能力！**