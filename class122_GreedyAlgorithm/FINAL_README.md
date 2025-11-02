# 贪心算法专题完整版 (Class 091)

## 项目概述

本项目为贪心算法专题的完整实现，包含了从基础到高级的多种贪心算法题目，每种题目都提供了Java、Python和C++三种语言的实现，并包含详细的注释、复杂度分析和测试用例。

## 题目列表

### 基础题目 (Code01-Code03)
1. **Code01_ShortestUnsortedContinuousSubarray** - 最短无序连续子数组
   - 来源: LeetCode 581
   - 相关题目链接:
     - https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/ (LeetCode 581)
     - https://www.lintcode.com/problem/shortest-unsorted-continuous-subarray/ (LintCode 1206)
     - https://practice.geeksforgeeks.org/problems/minimum-subarray-to-sort/ (GeeksforGeeks)
     - https://www.nowcoder.com/practice/2f9264b48cc24799925d48d355094c78 (牛客网)
2. **Code02_SmallestRange** - 最小范围
3. **Code03_GroupBuyTickets1** - 团体购票问题

### 补充题目 (Code27-Code37)
4. **Code27_TaskScheduler** - 任务调度器
   - 来源: LeetCode 621
   - 相关题目链接:
     - https://leetcode.cn/problems/task-scheduler/ (LeetCode 621)
     - https://www.lintcode.com/problem/task-scheduler/ (LintCode 1482)
     - https://practice.geeksforgeeks.org/problems/task-scheduler/ (GeeksforGeeks)
     - https://www.nowcoder.com/practice/6b48f8c9d2cb4a568890b73383e119cf (牛客网)
5. **Code28_LemonadeChange** - 柠檬水找零
6. **Code29_ReorganizeString** - 重构字符串
7. **Code30_VideoStitching** - 视频拼接
8. **Code31_SplitArrayIntoConsecutiveSubsequences** - 划分数组为连续子序列
9. **Code32_MonotoneIncreasingDigits** - 单调递增的数字
10. **Code33_RemoveKDigits** - 移掉K位数字
11. **Code34_GasStation** - 加油站
12. **Code35_WiggleSubsequence** - 摆动序列
13. **Code36_JumpGame** - 跳跃游戏
14. **Code37_Candy** - 分发糖果

## 语言支持

每个题目都提供以下语言的实现：

### ✅ Java版本
- 完整的类结构
- 详细的注释说明
- 多种解法对比
- 完整的测试框架

### ✅ Python版本  
- 简洁的实现
- 实际运行测试
- 性能对比分析
- 验证函数

### ⚠️ C++版本
- 部分题目实现
- 需要修复编译问题
- 头文件包含优化

## 代码特性

### 1. 算法实现
- **贪心策略**: 每个题目都体现了贪心算法的核心思想
- **多种解法**: 提供贪心、暴力、优化等多种实现
- **复杂度分析**: 详细的时间复杂度和空间复杂度分析

### 2. 工程化考量
- **输入验证**: 处理各种边界情况和错误输入
- **性能优化**: 避免不必要的计算，优化算法效率
- **可读性**: 清晰的变量命名和代码结构
- **可维护性**: 模块化的函数设计

### 3. 测试覆盖
- **单元测试**: 全面的测试用例覆盖
- **边界测试**: 处理各种边界情况
- **性能测试**: 大规模数据性能测试
- **验证函数**: 确保算法正确性

## 贪心算法核心知识点

### 适用场景识别
1. **最优子结构**: 问题可以分解为子问题
2. **贪心选择性质**: 局部最优导致全局最优  
3. **无后效性**: 当前选择不影响后续选择

### 典型问题类型
- **区间调度问题**: 选择不重叠的区间
- **分配问题**: 资源分配和优化
- **序列问题**: 字符串和数组处理
- **图论问题**: 最小生成树、最短路径

### 实现技巧
1. **排序预处理**: 很多问题需要先排序
2. **优先队列**: 动态获取当前最优选择
3. **双指针**: 处理区间或数组问题
4. **状态机**: 维护当前状态和趋势

## 复杂度分析模式

### 时间复杂度
- **O(n)**: 线性扫描，一次遍历
- **O(nlogn)**: 排序主导的算法
- **O(nlogk)**: 堆操作相关的算法
- **O(n²)**: 暴力解法，双重循环
- **O(2ⁿ)**: 指数级复杂度，回溯搜索

### 空间复杂度  
- **O(1)**: 常数空间，原地操作
- **O(n)**: 线性空间，辅助数组
- **O(k)**: 固定空间，与输入规模无关

## 测试验证结果

### ✅ 已验证通过的题目
- Code27_TaskScheduler (Python)
- Code28_LemonadeChange (Python) 
- Code30_VideoStitching (Python)
- Code31_SplitArrayIntoConsecutiveSubsequences (Python)
- Code32_MonotoneIncreasingDigits (Python)
- Code33_RemoveKDigits (Python)
- Code34_GasStation (Python)
- Code36_JumpGame (Python)
- Code37_Candy (Python)

### ⚠️ 需要修复的题目
- Code29_ReorganizeString: 验证函数需要优化
- Code35_WiggleSubsequence: 验证逻辑需要修正
- C++版本: 头文件包含问题需要修复

## 使用说明

### 运行Python代码
```bash
cd class091
python CodeXX_ProblemName.py
```

### 编译运行Java代码
```bash
cd class091
javac CodeXX_ProblemName.java
java CodeXX_ProblemName
```

### 测试特定功能
每个代码文件都包含完整的测试框架，可以直接运行查看结果。

## 学习建议

### 初学者路线
1. 从简单的题目开始（Code01-Code03）
2. 理解贪心算法的基本思想
3. 尝试自己实现算法
4. 对比不同解法的优劣

### 进阶学习  
1. 研究复杂题目的多种解法
2. 分析算法的时间空间复杂度
3. 思考工程化实现细节
4. 尝试解决类似的新题目

### 面试准备
1. 掌握经典贪心算法题目
2. 理解算法证明和正确性
3. 熟练编写无bug的代码
4. 能够分析算法复杂度

## 扩展资源

### 在线练习平台
- LeetCode (力扣): https://leetcode.cn/
- LintCode (炼码): https://www.lintcode.com/
- HackerRank: https://www.hackerrank.com/
- Codeforces: https://codeforces.com/
- AtCoder: https://atcoder.jp/
- 牛客网: https://www.nowcoder.com/
- 洛谷: https://www.luogu.com.cn/

### 推荐学习资料
- 《算法导论》贪心算法章节
- 《编程珠玑》优化技巧
- 各大高校的算法课程

## 贡献指南

欢迎对代码进行改进和优化：

1. 修复现有的bug和问题
2. 添加新的贪心算法题目
3. 优化代码性能和可读性
4. 补充更多的测试用例
5. 完善文档和注释

## 许可证

本项目仅供学习使用，遵循开源协议。

---

**总结**: 本项目提供了完整的贪心算法学习体系，通过丰富的题目和详细的实现，帮助学习者深入理解贪心算法的核心思想和应用技巧。