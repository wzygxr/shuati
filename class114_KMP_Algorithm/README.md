# KMP算法完全学习指南 - Class101

## 项目概述

本项目是KMP算法的完整学习资源库，包含从基础到高级的全面内容，涵盖算法原理、多种语言实现、工程化考量、测试用例和扩展题目。

## 文件结构

```
class101/
├── 核心算法实现文件
│   ├── Code01_RepeatMinimumLength.java/.cpp/.py  # 最短循环节长度
│   ├── Code02_DeleteAgainAndAgain.java/.cpp/.py  # 不断删除字符串
│   ├── Code03_LinkedListInBinaryTree.java/.cpp/.py  # 二叉树中的链表
│   ├── Code04_FindAllGoodStrings.java/.cpp/.py  # 找到所有好字符串
│   ├── Code05_Period.java/.cpp/.py  # 周期判断
│   ├── Code06_NeedleInHaystack.java/.cpp/.py  # 干草堆中找针
│   ├── Code07_PeriodsOfWords.java/.cpp/.py  # 单词周期总和
│   ├── Code08_LongestHappyPrefix.java/.cpp/.py  # 最长快乐前缀
│   ├── Code09_LeetCode28_StrStr.java/.cpp/.py  # LeetCode 28. 实现 strStr()
│   ├── Code10_Codeforces126B_Password.java/.cpp/.py  # Codeforces 126B Password
│   ├── Code11_POJ2752_SeekName.java/.cpp/.py  # POJ 2752 Seek the Name, Seek the Fame
│   ├── Code12_HDU2594_SimpsonsTalents.java/.cpp/.py  # HDU 2594 Simpsons' Hidden Talents
│   └── Code13_SPOJ_PERIOD.java/.cpp/.py  # SPOJ PERIOD - Period
│
├── 扩展资源
│   ├── extended/  # 扩展题目实现
│   ├── KMP_Algorithm_Complete_Guide.md  # 完整学习指南
│   ├── Additional_KMP_Problems.md  # 扩展题目集
│   └── KMP_Problems_Comprehensive_List.md  # KMP综合题目列表
│
└── 测试文件
    ├── *.class  # Java编译文件
    ├── *.exe  # C++可执行文件
    └── 测试用例和验证代码
```

## 核心算法实现

### 1. Code01 - 最短循环节长度
- **题目**：洛谷 P4391 [BOI2009]Radio Transmission 无线传输
- **算法**：利用KMP的next数组计算最小周期
- **关键公式**：周期长度 = n - next[n]
- **复杂度**：O(n)时间，O(n)空间

### 2. Code02 - 不断删除字符串
- **题目**：洛谷 P4824 [USACO15FEB]Censoring S
- **算法**：KMP算法 + 栈结构
- **特点**：高效处理字符串删除操作
- **复杂度**：O(n + m)时间，O(n)空间

### 3. Code03 - 二叉树中的链表
- **题目**：LeetCode 1367. 二叉树中的链表
- **算法**：KMP状态机 + 树遍历
- **创新**：将链表匹配问题转化为树路径匹配
- **复杂度**：O(n + m)时间，O(m)空间

### 4. Code04 - 找到所有好字符串
- **题目**：LeetCode 1397. 找到所有好字符串
- **算法**：数位DP + KMP状态机
- **难度**：困难，结合了动态规划和字符串匹配
- **复杂度**：O(n * m)时间，O(n * m)空间

### 5. Code09 - LeetCode 28. 实现 strStr()
- **题目**：LeetCode 28. 实现 strStr()
- **算法**：基础KMP字符串匹配
- **特点**：KMP算法的经典应用
- **复杂度**：O(n + m)时间，O(m)空间

### 6. Code10 - Codeforces 126B Password
- **题目**：Codeforces 126B Password
- **算法**：KMP的next数组应用
- **特点**：查找既是前缀又是后缀且在中间出现的子串
- **复杂度**：O(n)时间，O(n)空间

### 7. Code11 - POJ 2752 Seek the Name, Seek the Fame
- **题目**：POJ 2752 Seek the Name, Seek the Fame
- **算法**：递归应用KMP的next数组
- **特点**：找到所有既是前缀又是后缀的子串
- **复杂度**：O(n)时间，O(n)空间

### 8. Code12 - HDU 2594 Simpsons' Hidden Talents
- **题目**：HDU 2594 Simpsons' Hidden Talents
- **算法**：KMP算法的变种应用
- **特点**：找到第一个字符串的后缀和第二个字符串的前缀的最长公共部分
- **复杂度**：O(n + m)时间，O(n + m)空间

## 多语言实现特点

### Java版本
- **优势**：面向对象设计，异常处理完善
- **特点**：内存自动管理，代码结构清晰
- **适用**：企业级应用，教学演示

### C++版本
- **优势**：性能优化，内存控制精细
- **特点**：模板编程支持，系统级控制
- **适用**：竞赛编程，高性能应用

### Python版本
- **优势**：代码简洁，开发效率高
- **特点**：动态类型，生态丰富
- **适用**：快速原型，算法验证

## 工程化考量

### 1. 性能优化
- 使用高效的IO处理
- 预分配数组大小
- 避免动态扩容开销

### 2. 边界条件处理
- 空字符串处理
- 长度关系检查
- 特殊字符支持

### 3. 测试验证
- 单元测试覆盖各种情况
- 性能测试验证大规模数据处理
- 边界测试确保稳定性

## 学习路径建议

### 初级阶段 (1-2周)
1. 理解KMP算法基本原理
2. 实现标准KMP字符串匹配
3. 完成Code01和Code02的基础题目
4. 掌握LeetCode 28的基础应用 (Code09)

### 中级阶段 (2-3周)
1. 掌握周期判断技巧
2. 学习栈与KMP的结合应用
3. 完成Code03和Code05的进阶题目
4. 学习前后缀匹配问题 (Code10, Code11)

### 高级阶段 (3-4周)
1. 研究树结构匹配问题
2. 学习数位DP与KMP的结合
3. 完成Code04和Code07的困难题目
4. 掌握字符串交叉匹配问题 (Code12)

### 专家阶段 (4周+)
1. 参与实际竞赛题目
2. 研究算法优化技巧
3. 扩展到AC自动机等高级算法
4. 完成Codeforces和POJ的挑战题目

## 测试验证结果

### 编译测试
- ✅ Java文件编译成功
- ✅ Python文件运行正常
- ✅ 单元测试全部通过

### 性能测试
- 大规模数据处理能力验证
- 时间复杂度符合理论预期
- 内存使用控制在合理范围

### 功能测试
- 边界条件处理正确
- 特殊输入处理稳定
- 算法逻辑验证准确

## 扩展资源

### 1. 完整学习指南
- `KMP_Algorithm_Complete_Guide.md`
- 包含算法原理、实现细节、调试技巧
- 提供实际应用场景和优化策略

### 2. 扩展题目集
- `Additional_KMP_Problems.md`
- 涵盖各大竞赛平台题目
- 按难度分级，提供解题技巧

### 3. 扩展实现
- `extended/`目录包含更多题目实现
- 覆盖LeetCode、洛谷、Codeforces等平台

## 使用说明

### 快速开始
```bash
# 运行Java版本
cd class101
javac Code01_RepeatMinimumLength.java
java Code01_RepeatMinimumLength

# 运行Python版本
python Code01_RepeatMinimumLength.py

# 运行C++版本（需要编译）
g++ Code01_RepeatMinimumLength.cpp -o Code01
./Code01
```

### 测试验证
```bash
# 运行单元测试
python Code01_RepeatMinimumLength.py

# 运行性能测试
java Code01_RepeatMinimumLength
```

## 贡献指南

欢迎提交改进建议和新的题目实现：

1. 遵循现有的代码风格
2. 添加详细的注释说明
3. 包含完整的测试用例
4. 更新相关文档

## 许可证

本项目采用MIT许可证，允许自由使用和修改。

## 联系方式

如有问题或建议，请通过以下方式联系：
- 项目Issue页面
- 电子邮件联系
- 技术讨论群组

---

**通过系统学习本项目，您将全面掌握KMP算法及其在各种场景下的应用，为算法竞赛和工程开发奠定坚实基础。**