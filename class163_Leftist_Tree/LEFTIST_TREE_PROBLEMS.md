# 左偏树经典题目汇总

## 1. 模板题

### 1.1 洛谷P3377 【模板】左偏树/可并堆
- **题目来源**: 洛谷
- **题目编号**: P3377
- **难度**: 模板题
- **Java实现**: [Code06_LuoguP3377_LeftistTree.java](Code06_LuoguP3377_LeftistTree.java)
- **C++实现**: [Code06_LuoguP3377_LeftistTree.cpp](Code06_LuoguP3377_LeftistTree.cpp)
- **Python实现**: [Code06_LuoguP3377_LeftistTree.py](Code06_LuoguP3377_LeftistTree.py)

### 1.2 洛谷P2713 罗马游戏
- **题目来源**: 洛谷
- **题目编号**: P2713
- **难度**: 模板题
- **Java实现**: [Code07_LuoguP2713_RomanGame.java](Code07_LuoguP2713_RomanGame.java)
- **C++实现**: [Code07_LuoguP2713_RomanGame.cpp](Code07_LuoguP2713_RomanGame.cpp)
- **Python实现**: [Code07_LuoguP2713_RomanGame.py](Code07_LuoguP2713_RomanGame.py)

## 2. 树形结构+左偏树

### 2.1 APIO2012 派遣
- **题目来源**: APIO2012
- **难度**: 提高+/省选-
- **Java实现**: [Code08_APIO2012Dispatching.java](Code08_APIO2012Dispatching.java)
- **C++实现**: [Code08_APIO2012Dispatching.cpp](Code08_APIO2012Dispatching.cpp)
- **Python实现**: [Code08_APIO2012Dispatching.py](Code08_APIO2012Dispatching.py)

### 2.2 JLOI2015 城池攻占
- **题目来源**: JLOI2015
- **难度**: 省选/NOI-
- **Java实现**: [Code09_JLOI2015CityCapture.java](Code09_JLOI2015CityCapture.java)
- **C++实现**: [Code09_JLOI2015CityCapture.cpp](Code09_JLOI2015CityCapture.cpp)
- **Python实现**: [Code09_JLOI2015CityCapture.py](Code09_JLOI2015CityCapture.py)

## 3. 经典题

### 3.1 HDU 1512 Monkey King（猴王问题）
- **题目来源**: HDU
- **题目编号**: 1512
- **难度**: 提高+/省选-
- **Java实现**: [MonkeyKing_Java.java](MonkeyKing_Java.java)
- **Python实现**: [MonkeyKing_Python.py](MonkeyKing_Python.py)

## 4. 其他数据结构题目

### 4.1 SPOJ RMQSQ（区间最值查询）
- **题目来源**: SPOJ
- **题目编号**: RMQSQ
- **难度**: 提高
- **Java实现**: [RMQSQ_Java.java](RMQSQ_Java.java)
- **Python实现**: [RMQSQ_Python.py](RMQSQ_Python.py)

### 4.2 SPOJ QTREE（树链剖分）
- **题目来源**: SPOJ
- **题目编号**: QTREE
- **难度**: 省选/NOI-
- **Java实现**: [QTREE_Java.java](QTREE_Java.java)
- **Python实现**: [QTREE_Python.py](QTREE_Python.py)

### 4.3 JLOI2015 城池攻占
- **题目来源**: JLOI2015
- **难度**: 省选/NOI-
- **Java实现**: [Code01_CityCapture1.java](Code01_CityCapture1.java)

## 5. 题目分类总结

### 5.1 按算法类型分类

#### 可并堆模板题
- 洛谷P3377 【模板】左偏树/可并堆
- 洛谷P2713 罗马游戏

#### 树形结构+左偏树优化
- APIO2012 派遣
- JLOI2015 城池攻占
- HDU 1512 Monkey King（猴王问题）

#### 其他数据结构
- SPOJ RMQSQ（Sparse Table）
- SPOJ QTREE（树链剖分）

### 5.2 按难度分类

#### 模板题
- 洛谷P3377 【模板】左偏树/可并堆
- 洛谷P2713 罗马游戏

#### 提高+/省选-
- HDU 1512 Monkey King（猴王问题）
- APIO2012 派遣

#### 省选/NOI-
- JLOI2015 城池攻占
- SPOJ QTREE

#### 提高
- SPOJ RMQSQ

## 6. 解题技巧总结

### 6.1 左偏树基本操作
1. **合并操作**: 时间复杂度O(log n)
2. **插入操作**: 通过合并实现，时间复杂度O(log n)
3. **删除根节点**: 通过合并左右子树实现，时间复杂度O(log n)

### 6.2 常见应用场景
1. **维护可合并的堆**: 当需要频繁合并两个堆时
2. **树形结构优化**: 在树形DP中维护子树信息
3. **在线算法**: 支持动态插入和删除操作

### 6.3 优化技巧
1. **延迟标记**: 在需要对整棵树进行操作时使用
2. **并查集维护**: 维护节点所属集合的连通性
3. **标记下传**: 确保操作的正确性

### 6.4 注意事项
1. **路径压缩**: 在并查集中使用路径压缩优化
2. **标记清空**: 操作完成后及时清空延迟标记
3. **边界处理**: 注意空节点和边界情况的处理