# 拓扑排序专题项目结构说明

## 项目概述
本项目是一个完整的拓扑排序算法专题，包含基础算法、高级优化、实际应用和多语言实现。

## 文件结构

### 1. 基础算法文件
```
class059/
├── Code01_CreateGraph.java          # 图结构创建工具类
├── Leetcode207_CourseSchedule.java  # LeetCode 207题实现
├── Leetcode210_CourseScheduleII.java # LeetCode 210题实现
├── Leetcode269_AlienDictionary.java # LeetCode 269题实现
├── Leetcode936_StampingTheSequence.java # LeetCode 936题实现
├── HDU1285_DetermineTheRanking.java # HDU 1285题实现
├── POJ1094_SortingItAllOut.java     # POJ 1094题实现
├── UVA10305_OrderingTasks.java     # UVA 10305题实现
└── SPOJ_TopologicalSorting.java     # SPOJ题目实现
```

### 2. 综合题目集
```
class059/
├── TopologicalSortingComprehensive.java  # Java综合题目集
├── TopologicalSortingComprehensive.cpp   # C++综合题目集
└── TopologicalSortingComprehensive.py    # Python综合题目集
```

### 3. 高级算法与优化
```
class059/
├── AdvancedTopologicalSorting.java       # 高级拓扑排序算法
└── TopologicalSortingApplications.java   # 实际应用案例
```

### 4. 测试与工具
```
class059/
├── TestTopologicalSorting.java           # 综合测试类
├── compile_and_run.sh                    # 编译运行脚本
├── requirements.txt                      # Python依赖管理
└── README.md                             # 项目说明文档
```

## 文件详细说明

### 基础算法文件

#### Code01_CreateGraph.java
- **功能**：图的三种表示方法（邻接矩阵、邻接表、链式前向星）
- **用途**：为其他算法提供图结构支持
- **特点**：支持有向图和无向图，带权图和不带权图

#### Leetcode系列文件
- **Leetcode207_CourseSchedule.java**：课程表环检测问题
- **Leetcode210_CourseScheduleII.java**：课程表顺序生成问题
- **Leetcode269_AlienDictionary.java**：外星字典字符顺序推断
- **Leetcode936_StampingTheSequence.java**：序列生成问题

#### 竞赛题目文件
- **HDU1285_DetermineTheRanking.java**：字典序最小拓扑排序
- **POJ1094_SortingItAllOut.java**：动态拓扑排序检测
- **UVA10305_OrderingTasks.java**：经典拓扑排序模板
- **SPOJ_TopologicalSorting.java**：字典序最小排序

### 综合题目集文件

#### TopologicalSortingComprehensive.java
- **包含题目**：LeetCode 310、Codeforces 510C、AtCoder ABC139-E等
- **工程特性**：异常处理、性能监控、内存优化
- **测试用例**：每个题目都包含完整的测试用例

#### TopologicalSortingComprehensive.cpp
- **语言特性**：模板编程、智能指针、STL库使用
- **优化技术**：内存管理、性能优化、并发支持
- **跨平台**：支持Windows、Linux、macOS

#### TopologicalSortingComprehensive.py
- **Python特性**：类型注解、生成器、装饰器
- **工程实践**：异常处理、性能分析、单元测试
- **依赖管理**：使用requirements.txt管理依赖

### 高级算法文件

#### AdvancedTopologicalSorting.java
- **动态拓扑排序**：支持动态添加和删除边
- **并行拓扑排序**：多线程优化处理
- **增量拓扑排序**：批量操作优化
- **性能优化技巧**：缓存、压缩存储、双向BFS

#### TopologicalSortingApplications.java
- **任务调度系统**：分布式任务依赖管理
- **构建系统**：源代码编译顺序确定
- **包依赖管理**：软件包安装顺序解决
- **数据流水线**：ETL流程依赖处理
- **工作流引擎**：业务流程活动排序
- **课程安排系统**：学习路径规划

### 测试与工具文件

#### TestTopologicalSorting.java
- **测试覆盖**：基本功能、边界情况、异常场景、性能测试
- **测试类型**：单元测试、集成测试、性能测试
- **测试运行器**：TestRunner类支持批量测试

#### compile_and_run.sh
- **功能**：自动化编译和运行脚本
- **支持语言**：Java、C++、Python
- **操作选项**：compile、test、run、clean、help

#### requirements.txt
- **用途**：Python依赖包管理
- **包含包**：numpy、scipy、pytest、性能分析工具等

## 编译与运行指南

### Java代码
```bash
# 编译所有Java文件
javac -d bin *.java

# 运行测试
java -cp bin class059.TestRunner

# 运行特定题目
java -cp bin class059.Leetcode207_CourseSchedule
```

### C++代码
```bash
# 编译C++文件
g++ -std=c++11 -o topological_sort TopologicalSortingComprehensive.cpp

# 运行程序
./topological_sort
```

### Python代码
```bash
# 安装依赖
pip install -r requirements.txt

# 运行程序
python TopologicalSortingComprehensive.py
```

### 使用脚本
```bash
# 编译所有代码
./compile_and_run.sh compile

# 运行所有测试
./compile_and_run.sh test

# 运行特定题目
./compile_and_run.sh run leetcode207

# 清理编译文件
./compile_and_run.sh clean
```

## 学习路径建议

### 初学者路径
1. **基础概念**：阅读README.md中的算法原理
2. **简单实现**：学习Code01_CreateGraph.java和基础题目
3. **题目练习**：完成LeetCode简单题目

### 进阶学习
1. **算法优化**：学习高级算法文件中的优化技巧
2. **实际应用**：研究应用案例文件中的工程实践
3. **多语言实现**：对比不同语言的实现特点

### 高级应用
1. **系统设计**：基于应用案例设计完整系统
2. **性能优化**：实现大规模图的处理优化
3. **分布式处理**：探索分布式拓扑排序算法

## 贡献指南

### 代码规范
- 遵循各语言的编码规范
- 添加详细的注释和文档
- 编写完整的测试用例

### 新增题目
1. 在对应语言的文件中添加实现
2. 更新README.md中的题目列表
3. 添加相应的测试用例

### 问题反馈
- 通过GitHub Issues报告问题
- 提供详细的重现步骤
- 包含环境信息和错误日志

## 许可证说明
本项目采用MIT许可证，允许自由使用、修改和分发。

## 更新日志

### v1.0.0 (2024-01-23)
- 初始版本发布
- 包含基础算法和竞赛题目
- 添加多语言实现
- 完整的测试覆盖

### v1.1.0 (计划中)
- 添加更多实际应用案例
- 优化算法性能
- 增强分布式处理支持
- 完善文档和示例