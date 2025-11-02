# Class069 高级动态规划专题

## 项目概述

本专题全面覆盖高级动态规划算法的核心知识点，包括多维费用背包、概率动态规划、路径计数动态规划、字符串动态规划等高级主题。每个算法都提供Java、C++、Python三种语言的完整实现，包含详细的注释、复杂度分析和测试用例。

## 目录结构

```
class069/
├── Java实现文件/
│   ├── Code01_OnesAndZeroes.java          # 一和零（多维费用背包）
│   ├── Code02_ProfitableSchemes.java       # 盈利计划（多维费用背包）
│   ├── Code03_KnightProbabilityInChessboard.java # 骑士在棋盘上的概率（概率DP）
│   ├── Code04_PathsDivisibleByK.java       # 矩阵中和能被K整除的路径（路径计数DP）
│   ├── Code05_ScrambleString.java         # 扰乱字符串（字符串DP）
│   ├── Code06_Coins.java                   # Coins（概率DP）
│   ├── Code07_KnightDialer.java           # 骑士拨号器（计数DP）
│   ├── Code08_UniquePaths.java            # 不同路径（网格路径计数）
│   ├── Code09_ClimbingStairs.java         # 爬楼梯（线性DP）
│   ├── Code10_HouseRobber.java            # 打家劫舍（线性DP）
│   ├── Code11_LongestIncreasingSubsequence.java # 最长递增子序列（线性DP）
│   ├── TargetSum.java                     # 目标和（背包问题变形）
│   └── LastStoneWeightII.java             # 最后一块石头的重量II（背包问题）
├── C++实现文件/
│   ├── Code01_OnesAndZeroes.cpp
│   ├── Code02_ProfitableSchemes.cpp
│   ├── Code03_KnightProbabilityInChessboard.cpp
│   ├── Code04_PathsDivisibleByK.cpp
│   ├── Code05_ScrambleString.cpp
│   ├── Code06_Coins.cpp
│   ├── Code07_KnightDialer.cpp
│   ├── Code08_UniquePaths.cpp
│   ├── Code09_ClimbingStairs.cpp
│   ├── Code10_HouseRobber.cpp
│   ├── Code11_LongestIncreasingSubsequence.cpp
│   ├── TargetSum.cpp
│   └── LastStoneWeightII.cpp
├── Python实现文件/
│   ├── Code01_OnesAndZeroes.py
│   ├── Code02_ProfitableSchemes.py
│   ├── Code03_KnightProbabilityInChessboard.py
│   ├── Code04_PathsDivisibleByK.py
│   ├── Code05_ScrambleString.py
│   ├── Code06_Coins.py
│   ├── Code07_KnightDialer.py
│   ├── Code08_UniquePaths.py
│   ├── Code09_ClimbingStairs.py
│   ├── Code10_HouseRobber.py
│   ├── Code11_LongestIncreasingSubsequence.py
│   ├── TargetSum.py
│   └── LastStoneWeightII.py
├── 文档文件/
│   ├── SOLUTION_SUMMARY.md                 # 解题思路与技巧总结
│   ├── ADDITIONAL_PROBLEMS.md              # 补充题目清单
│   ├── ADDITIONAL_PROBLEMS_EXTENDED.md     # 扩展题目清单（全面覆盖）
│   ├── COMPREHENSIVE_SUMMARY.md           # 全面总结与扩展
│   └── README.md                          # 项目说明（本文件）
└── 编译文件/
    ├── *.class (Java字节码文件)
    └── *.exe (C++可执行文件)
```

## 算法分类

### 1. 多维费用背包问题
- **一和零 (Ones and Zeroes)**: 每个字符串有两个维度的费用限制
- **盈利计划 (Profitable Schemes)**: 员工数量和利润要求的双重限制
- **目标和 (Target Sum)**: 转化为子集和问题的背包变形
- **最后一块石头的重量 II**: 转化为背包问题的石头粉碎问题

### 2. 概率动态规划
- **骑士在棋盘上的概率**: 计算骑士经过k步移动后留在棋盘的概率
- **Coins**: 计算硬币正面朝上数多于反面的概率
- **骑士拨号器**: 计算骑士在数字键盘上移动形成的号码数量

### 3. 路径计数动态规划
- **矩阵中和能被K整除的路径**: 统计路径和满足模运算条件的路径数
- **不同路径**: 经典的网格路径计数问题

### 4. 字符串动态规划
- **扰乱字符串**: 判断字符串是否可以通过特定操作变成另一个字符串

### 5. 线性动态规划
- **爬楼梯**: 计算爬楼梯的不同方法数
- **打家劫舍**: 计算不触发报警系统能偷窃的最高金额
- **最长递增子序列**: 找到数组中最长严格递增子序列的长度

## 实现特点

### 代码质量保证
1. **多语言实现**: 每个算法提供Java、C++、Python三种语言的完整实现
2. **详细注释**: 包含算法思路、复杂度分析、关键步骤说明
3. **测试用例**: 每个实现都包含完整的测试用例，验证正确性
4. **性能优化**: 提供基础解法和空间优化版本

### 工程化考量
1. **异常处理**: 完善的输入验证和边界条件处理
2. **大数处理**: 使用模运算防止整数溢出
3. **空间优化**: 滚动数组等技术降低空间复杂度
4. **可读性**: 清晰的变量命名和代码结构

## 复杂度分析

每个算法都提供详细的时间复杂度和空间复杂度分析：

- **时间复杂度**: 从暴力解法的指数级到动态规划的多项式级
- **空间复杂度**: 基础实现和优化版本的对比分析
- **优化策略**: 空间优化、剪枝策略等性能提升技巧

## 使用说明

### Java运行
```bash
javac Code01_OnesAndZeroes.java
java Code01_OnesAndZeroes
```

### C++编译运行
```bash
g++ -std=c++11 Code01_OnesAndZeroes.cpp -o OnesAndZeroes
./OnesAndZeroes
```

### Python运行
```bash
python Code01_OnesAndZeroes.py
```

## 学习建议

### 初学者路线
1. 先从"不同路径"开始，理解基本动态规划思想
2. 学习"一和零"，掌握多维费用背包问题
3. 尝试"目标和"，理解问题转化的技巧
4. 挑战"扰乱字符串"，体验复杂字符串DP

### 进阶学习
1. 深入研究每种算法的多种解法
2. 对比不同语言实现的差异和优劣
3. 尝试解决扩展题目清单中的难题
4. 参与在线评测平台的相关题目练习

## 扩展资源

### 在线评测平台
- **LeetCode**: 提供大量动态规划题目和测试用例
- **LintCode**: 中文算法题库，题目分类清晰
- **洛谷**: 国内知名OJ，包含经典题目
- **AtCoder**: 日本编程竞赛平台，题目质量高

### 学习资料
- 《算法导论》: 动态规划章节的理论基础
- 《编程之美》: 实际问题中的动态规划应用
- 各大高校的算法课程讲义

## 贡献指南

欢迎提交改进建议、bug修复或新的算法实现：

1. Fork本项目
2. 创建特性分支
3. 提交更改
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 更新日志

- **2024-01-01**: 项目初始化，完成基础算法实现
- **2024-01-02**: 添加详细注释和复杂度分析
- **2024-01-03**: 完善测试用例和文档说明
- **2024-01-04**: 扩展题目清单和全面总结

通过系统学习本专题，您将能够：
- 掌握高级动态规划的核心思想和技巧
- 熟练解决各类复杂的动态规划问题
- 在算法面试和编程竞赛中游刃有余
- 为后续学习更高级的算法打下坚实基础