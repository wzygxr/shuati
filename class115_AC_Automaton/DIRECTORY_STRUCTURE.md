# AC自动机实现目录结构说明

## 目录组织

本目录按照编程语言对AC自动机的实现进行分类：

- [java/](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class102_AC_Automaton/implementations/java) - Java语言实现
- [python/](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class102_AC_Automaton/implementations/python) - Python语言实现
- [cpp/](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class102_AC_Automaton/implementations/cpp) - C++语言实现

## 文件说明

- [README.md](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class102_AC_Automaton/implementations/README.md) - AC自动机算法详细说明文档
- [DIRECTORY_STRUCTURE.md](file:///d:/Upan/src/algorithm-journey/src/algorithm-journey/src/class102_AC_Automaton/implementations/DIRECTORY_STRUCTURE.md) - 本文件，说明目录结构

## 算法与数据结构分类

AC自动机（Aho-Corasick Automaton）是一种高级字符串匹配算法，属于以下算法与数据结构类别：

1. **字符串匹配算法**
   - 多模式字符串匹配
   - 高效文本搜索

2. **数据结构**
   - Trie树（前缀树）
   - 图论中的有向图（通过fail指针构建）

3. **算法设计思想**
   - 动态规划（构建fail指针的过程）
   - 广度优先搜索（BFS构建fail指针）
   - 状态机思想（在文本中进行状态转移）

## 应用场景

- 关键词过滤系统
- 病毒特征码检测
- 生物信息学中的序列匹配
- 网络入侵检测系统