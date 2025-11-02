# 数位DP扩展题目完整列表

以下是在学习数位DP过程中可以练习的更多题目，涵盖各大OJ平台，包含完整的题目描述、解题思路和代码实现。

## 一、LeetCode题目

### 1. 233. 数字 1 的个数
- **题目链接**: https://leetcode.cn/problems/number-of-digit-one/
- **题目描述**: 给定一个整数 n，计算所有小于等于 n 的非负整数中数字 1 出现的个数。
- **相关文件**: Code05_CountDigitOne.java, Code05_CountDigitOne.py
- **解题思路**: 数位DP，记录已出现的1的个数
- **时间复杂度**: O(log n)
- **空间复杂度**: O(log n)

### 2. 600. 不含连续1的非负整数
- **题目链接**: https://leetcode.cn/problems/non-negative-integers-without-consecutive-ones/
- **题目描述**: 给定一个正整数 n，返回在 [0, n] 范围内不含连续1的非负整数的个数。
- **相关文件**: Code06_NonNegativeIntegersWithoutConsecutiveOnes.java, Code06_NonNegativeIntegersWithoutConsecutiveOnes.py, Code16_NumberWithoutConsecutiveOnes.java, Code16_NumberWithoutConsecutiveOnes.cpp, Code16_NumberWithoutConsecutiveOnes.py
- **解题思路**: 二进制数位DP，记录前一位是否为1
- **时间复杂度**: O(log n)
- **空间复杂度**: O(log n)

### 3. 902. 最大为 N 的数字组合
- **题目链接**: https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
- **题目描述**: 给定一个按 非递减顺序 排列的数字数组 digits 。你可以用任意次数 digits[i] 来写的数字。返回可以生成的小于或等于给定整数 n 的正整数的个数。
- **相关文件**: Code02_NumbersAtMostGivenDigitSet.java
- **解题思路**: 数位DP，限制数字只能从给定集合中选择
- **时间复杂度**: O(log n * |digits|)
- **空间复杂度**: O(log n)

### 4. 1012. 至少有 1 位重复的数字
- **题目链接**: https://leetcode.cn/problems/numbers-with-repeated-digits/
- **题目描述**: 给定正整数 n，返回在 [1, n] 范围内具有至少 1 位重复数字的正整数的个数。
- **相关文件**: Code04_NumbersWithRepeatedDigits.java
- **解题思路**: 数位DP，用位掩码记录已使用的数字
- **时间复杂度**: O(10 * 2^10 * log n)
- **空间复杂度**: O(2^10 * log n)

### 5. 357. 统计各位数字都不同的数字个数
- **题目链接**: https://leetcode.cn/problems/count-numbers-with-unique-digits/
- **题目描述**: 给你一个整数 n，统计并返回各位数字都不同的数字 x 的个数，其中 0 <= x < 10^n。
- **相关文件**: Code01_CountNumbersWithUniqueDigits.java, Code01_CountNumbersWithUniqueDigits.cpp, Code01_CountNumbersWithUniqueDigits.py
- **解题思路**: 数位DP或数学排列组合
- **时间复杂度**: O(n) 或 O(10 * 2^10 * n)
- **空间复杂度**: O(1) 或 O(2^10 * n)

### 6. 2719. 统计整数数目
- **题目链接**: https://leetcode.cn/problems/count-of-integers/
- **题目描述**: 给你两个数字字符串 num1 和 num2 ，以及两个整数 max_sum 和 min_sum 。如果一个整数 x 满足以下条件，我们称它是一个好整数：num1 <= x <= num2；min_sum <= digit_sum(x) <= max_sum。请你返回好整数的数目。答案可能很大，请你返回答案对 10^9 + 7 取余后的结果。
- **相关文件**: Code03_CountOfIntegers.java
- **解题思路**: 数位DP，记录数位和范围
- **时间复杂度**: O(log n * max_sum)
- **空间复杂度**: O(log n * max_sum)

### 7. 2827. 范围中美丽整数的数目
- **题目链接**: https://leetcode.cn/problems/number-of-beautiful-integers-in-the-range/
- **题目描述**: 给你两个正整数：low 和 high 。如果一个整数满足以下条件，我们称它为美丽整数：1. 偶数数位的数目与奇数数位的数目相同；2. 这个整数能被 k 整除。请你返回范围 [low, high] 中美丽整数的数目。
- **相关文件**: Code07_NumberOfBeautifulIntegersInTheRange.java, Code07_NumberOfBeautifulIntegersInTheRange.py, Code07_NumberOfBeautifulIntegersInTheRange.cpp
- **解题思路**: 数位DP，记录奇偶数位个数和余数
- **时间复杂度**: O(log n * k * 10^2)
- **空间复杂度**: O(log n * k * 10^2)

### 8. 1397. 找到所有好字符串
- **题目链接**: https://leetcode.cn/problems/find-all-good-strings/
- **题目描述**: 给你两个长度为 n 的字符串 s1 和 s2，以及一个字符串 evil。请你返回好字符串的数目。好字符串的定义是：它的长度为 n，字典序大于等于 s1，字典序小于等于 s2，且不包含 evil 为子字符串。
- **相关文件**: Code08_FindAllGoodStrings.java, Code08_FindAllGoodStrings.py, Code08_FindAllGoodStrings.cpp
- **解题思路**: 数位DP + KMP自动机
- **时间复杂度**: O(n * |evil| * 26)
- **空间复杂度**: O(n * |evil|)

## 二、Codeforces题目

### 1. 55D. Beautiful Numbers
- **题目链接**: https://codeforces.com/problemset/problem/55/D
- **题目描述**: 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。给定区间 [l, r]，求其中美丽数字的个数。
- **相关文件**: Code13_BeautifulNumbersCF.java, Code13_BeautifulNumbersCF.cpp, Code13_BeautifulNumbersCF.py
- **解题思路**: 数位DP，利用LCM性质优化状态
- **时间复杂度**: O(log r * 2520 * 50)
- **空间复杂度**: O(log r * 2520 * 50)

### 2. 628D. Magic Numbers
- **题目链接**: https://codeforces.com/problemset/problem/628/D
- **题目描述**: 定义一个d-magic number为满足特定条件的数字，给定区间 [a, b]，求其中magic number的个数。
- **相关文件**: Code14_MagicNumbersCF.java, Code14_MagicNumbersCF.cpp, Code14_MagicNumbersCF.py
- **解题思路**: 数位DP，根据位置奇偶性应用不同约束
- **时间复杂度**: O(log b * m)
- **空间复杂度**: O(log b * m)

## 三、AtCoder题目

### 1. ABC135 D - Digits Parade
- **题目链接**: https://atcoder.jp/contests/abc135/tasks/abc135_d
- **题目描述**: 给定一个由数字和?组成的字符串，?可以替换成0-9的任意数字，求有多少种替换方案使得结果能被13整除。
- **相关文件**: Code15_DigitsParadeABC.java, Code15_DigitsParadeABC.cpp, Code15_DigitsParadeABC.py
- **解题思路**: 数位DP，处理通配符和模运算
- **时间复杂度**: O(n * 13)
- **空间复杂度**: O(n * 13)

## 四、洛谷题目

### 1. P2602 [ZJOI2010] 数字计数
- **题目链接**: https://www.luogu.com.cn/problem/P2602
- **题目描述**: 给定两个正整数 a 和 b，求在 [a, b] 范围上的所有整数中，每个数码 (digit) 各出现了多少次。
- **相关文件**: Code09_DigitCount.java, Code09_DigitCount.py, Code09_DigitCount.cpp
- **解题思路**: 数位DP，分别统计每个数字的出现次数
- **时间复杂度**: O(log b * 10)
- **空间复杂度**: O(log b * 10)

### 2. P4127 [AHOI2009] 同类分布
- **题目链接**: https://www.luogu.com.cn/problem/P4127
- **题目描述**: 给出两个数 a,b，求出 [a,b] 中各位数字之和能整除原数的数的个数。
- **解题思路**: 数位DP，记录数位和和余数
- **时间复杂度**: O(log b * 162 * 162)
- **空间复杂度**: O(log b * 162 * 162)

### 3. P4124 [CQOI2016] 手机号码
- **题目链接**: https://www.luogu.com.cn/problem/P4124
- **题目描述**: 给定一个区间 [L,R]，统计这个区间内满足特定条件的手机号码个数。
- **解题思路**: 数位DP，多个约束条件组合
- **时间复杂度**: O(log R * 2^3 * 10^2)
- **空间复杂度**: O(log R * 2^3 * 10^2)

## 五、其他平台题目

### 1. HackerRank - Digit DP
- **题目链接**: https://www.hackerrank.com/challenges/digit-dp
- **题目描述**: 各种数位DP变种题目
- **解题思路**: 标准数位DP框架应用

### 2. 牛客网 - 数位小孩
- **题目链接**: https://ac.nowcoder.com/acm/problem/17023
- **题目描述**: 给出一个区间 [l,r]，求这个区间内有多少个数字满足特定条件。
- **解题思路**: 基础数位DP练习

## 六、数位DP题型分类

### 1. 基础计数问题
- **特征**: 统计满足简单条件的数字个数
- **例题**: 数字1的个数、不含连续1的数字
- **状态设计**: 位置 + 限制标记 + 简单计数

### 2. 数位和问题
- **特征**: 与数字的数位和相关
- **例题**: 数位和在指定范围内、数位和整除原数
- **状态设计**: 位置 + 限制标记 + 数位和 + 余数

### 3. 数字约束问题
- **特征**: 数字必须满足特定模式
- **例题**: 美丽数字、魔法数字
- **状态设计**: 位置 + 限制标记 + 模式状态

### 4. 字符串匹配问题
- **特征**: 结合字符串匹配算法
- **例题**: 不包含特定子串的好字符串
- **状态设计**: 位置 + 限制标记 + KMP状态

### 5. 二进制数位问题
- **特征**: 处理二进制表示
- **例题**: 不含连续1的二进制数
- **状态设计**: 位置 + 限制标记 + 前一位状态

### 6. 通配符问题
- **特征**: 包含通配符的数字
- **例题**: Digits Parade
- **状态设计**: 位置 + 限制标记 + 余数

## 七、解题技巧总结

### 1. 状态设计原则
- **最小化状态数**: 只记录必要的约束信息
- **状态压缩**: 使用位运算压缩多个布尔状态
- **模运算优化**: 利用模运算性质减少状态

### 2. 记忆化优化
- **缓存键设计**: 合理设计记忆化缓存键
- **状态去重**: 识别并合并相同状态
- **提前剪枝**: 在不可能满足条件时提前返回

### 3. 边界处理
- **前导零处理**: 正确处理前导零情况
- **上下界处理**: 正确处理区间边界
- **特殊输入**: 处理n=0, n=1等特殊情况

### 4. 性能优化
- **数学性质利用**: 发现并利用数学规律
- **状态转移优化**: 优化状态转移过程
- **空间优化**: 使用滚动数组等技术优化空间

## 八、代码实现规范

### 1. 文件命名规范
- 使用统一的命名格式：Code{编号}_{题目描述}.{语言}
- 编号从01开始顺序排列
- 题目描述使用驼峰命名法

### 2. 代码结构规范
- 包含完整的题目描述和链接
- 详细的解题思路和时间空间复杂度分析
- 完整的测试用例和性能测试
- 清晰的注释和文档

### 3. 跨语言实现
- 每种题目提供Java、C++、Python三种实现
- 保持算法逻辑的一致性
- 适应各语言特性的优化

## 九、学习路径建议

### 1. 初级阶段
- 先掌握基础数位DP框架
- 练习简单的计数问题
- 理解状态设计和记忆化原理

### 2. 中级阶段
- 学习复杂的状态设计
- 练习数位和、模运算相关问题
- 掌握状态压缩技巧

### 3. 高级阶段
- 学习结合字符串匹配的数位DP
- 练习二进制数位问题
- 掌握性能优化技巧

### 4. 实战阶段
- 参加各大OJ平台的数位DP比赛
- 总结各类题型的解题模式
- 分享解题经验和技巧

## 十、扩展资源

### 1. 在线评测平台
- LeetCode: 丰富的数位DP题目
- Codeforces: 高质量的数位DP比赛题
- AtCoder: 日本编程比赛的数位DP题目
- 洛谷: 中文社区的优秀题目

### 2. 学习资料
- 算法竞赛入门经典：数位DP章节
- 挑战程序设计竞赛：动态规划部分
- 各大博客的技术文章

### 3. 社区讨论
- Stack Overflow: 技术问题解答
- GitHub: 开源代码参考
- 各大技术论坛：经验交流

通过系统学习以上题目和技巧，可以全面掌握数位DP算法，解决各类与数字数位相关的问题。