# 线性基补充题目列表

## 经典题目

### 1. 最大异或和类题目
1. **洛谷 P3812 【模板】线性基**
   - 链接：https://www.luogu.com.cn/problem/P3812
   - 类型：模板题
   - 算法：普通消元法

2. **LeetCode 421. 数组中两个数的最大异或值**
   - 链接：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
   - 类型：字典树/线性基
   - 算法：线性基或字典树

3. **洛谷 P4151 [WC2011]最大XOR和路径**
   - 链接：https://www.luogu.com.cn/problem/P4151
   - 类型：图论+线性基
   - 算法：DFS找环+线性基

### 2. 第k小异或和类题目
1. **LOJ #114. 第k小异或和**
   - 链接：https://loj.ac/p/114
   - 类型：模板题
   - 算法：高斯消元法

2. **HDU 3949 XOR**
   - 链接：http://acm.hdu.edu.cn/showproblem.php?pid=3949
   - 类型：经典题
   - 算法：高斯消元法

### 3. 线性基+贪心类题目
1. **洛谷 P4570 [BJWC2011]元素**
   - 链接：https://www.luogu.com.cn/problem/P4570
   - 类型：贪心+线性基
   - 算法：排序+贪心+线性基

2. **BZOJ 2460 元素**
   - 链接：https://www.lydsy.com/JudgeOnline/problem.php?id=2460
   - 类型：贪心+线性基
   - 算法：排序+贪心+线性基

### 4. 线性基应用类题目
1. **洛谷 P3857 [TJOI2008]彩灯**
   - 链接：https://www.luogu.com.cn/problem/P3857
   - 类型：线性基应用
   - 算法：线性基求秩

2. **Codeforces 1101G (Zero XOR Subset)-less**
   - 链接：https://codeforces.com/problemset/problem/1101/G
   - 类型：前缀和+线性基
   - 算法：前缀异或和+线性基

## 进阶题目

### 1. 可持久化线性基
1. **洛谷 P4301 [CQOI2013]最大异或和**
   - 链接：https://www.luogu.com.cn/problem/P4301
   - 类型：可持久化线性基
   - 算法：可持久化线性基

2. **洛谷 P4580 [BJOI2014]路径**
   - 链接：https://www.luogu.com.cn/problem/P4580
   - 类型：可持久化线性基
   - 算法：可持久化线性基

### 2. 线性基+图论
1. **洛谷 P4151 [WC2011]最大XOR和路径**
   - 链接：https://www.luogu.com.cn/problem/P4151
   - 类型：图论+线性基
   - 算法：DFS找环+线性基

2. **Codeforces 938G Shortest Path Queries**
   - 链接：https://codeforces.com/problemset/problem/938/G
   - 类型：图论+线性基+分治
   - 算法：线段树分治+线性基

### 3. 线性基+数据结构
1. **洛谷 P3292 [SCOI2016]幸运数字**
   - 链接：https://www.luogu.com.cn/problem/P3292
   - 类型：线性基+倍增
   - 算法：树上倍增+线性基

2. **洛谷 P5607 [Ynoi2013]无力回天NOI2017**
   - 链接：https://www.luogu.com.cn/problem/P5607
   - 类型：线性基+线段树
   - 算法：线段树套线性基

## AtCoder题目

1. **AtCoder ABC141F Xor Sum 3**
   - 链接：https://atcoder.jp/contests/abc141/tasks/abc141_f
   - 类型：线性基
   - 算法：线性基贪心

## Codeforces题目

1. **Codeforces 282E XOR and Favorite Number**
   - 链接：https://codeforces.com/problemset/problem/282/E
   - 类型：莫队+线性基
   - 算法：莫队算法+线性基

2. **Codeforces 959F Mahmoud and Ehab and yet another xor task**
   - 链接：https://codeforces.com/problemset/problem/959/F
   - 类型：线性基+DP
   - 算法：线性基+动态规划

3. **Codeforces 895C Square Subsets**
   - 链接：https://codeforces.com/problemset/problem/895/C
   - 类型：线性基+状压DP
   - 算法：质因数分解+线性基+状压DP

## 其他平台题目

1. **SPOJ ADABIT - Ada and Behives**
   - 链接：https://www.spoj.com/problems/ADABIT/
   - 类型：线性基
   - 算法：线性基

2. **URAL 1178 - Akbardin's Roads**
   - 链接：https://acm.timus.ru/problem.aspx?space=1&num=1178
   - 类型：线性基
   - 算法：线性基

## 解题技巧总结

### 1. 识别线性基问题的关键特征
- 题目中出现"异或"操作
- 需要从一组数中选择若干个数进行异或运算
- 求最大值、最小值或第k小值
- 判断某个值是否可以被异或得到

### 2. 常用算法选择
- **求最大异或值**：普通消元法
- **求第k小异或值**：高斯消元法
- **判断可达性**：普通消元法
- **带权值问题**：贪心+线性基
- **区间查询**：可持久化线性基或线段树套线性基
- **树上问题**：倍增+线性基

### 3. 常见优化技巧
- **位运算优化**：使用位运算代替乘除法
- **IO优化**：对于大数据量使用快速IO
- **内存优化**：合理使用数组大小，避免浪费
- **时间优化**：预处理常用值，避免重复计算

### 4. 调试技巧
- **打印中间状态**：在线性基构建过程中打印基的变化
- **小数据测试**：用小数据集手动验证算法正确性
- **边界测试**：测试空数组、单元素等边界情况
- **对拍测试**：编写暴力算法进行对拍验证

## 学习资源推荐

### 1. 在线教程
1. **OI Wiki 线性基章节**
   - 链接：https://oi-wiki.org/math/linear-algebra/basis/
   - 特点：详细讲解线性基的数学原理和实现

2. **算法竞赛进阶指南**
   - 特点：系统讲解线性基在竞赛中的应用

### 2. 视频教程
1. **B站相关视频**
   - 搜索关键词："线性基"、"异或线性基"
   - 推荐UP主：算法相关的教学UP主

### 3. 练习平台
1. **洛谷**
   - 链接：https://www.luogu.com.cn/
   - 特点：有大量线性基相关题目

2. **Codeforces**
   - 链接：https://codeforces.com/
   - 特点：高质量的线性基题目

3. **AtCoder**
   - 链接：https://atcoder.jp/
   - 特点：ABC和ARC中有不少线性基题目

## 常见错误及解决方法

### 1. 数据类型溢出
**错误表现**：结果不正确，特别是大数情况
**解决方法**：使用long long或相应的64位整数类型

### 2. 数组越界
**错误表现**：运行时错误或结果不正确
**解决方法**：检查线性基数组大小，确保足够大

### 3. 初始化错误
**错误表现**：多次运行结果不一致
**解决方法**：每次使用前清空线性基数组

### 4. 位运算优先级错误
**错误表现**：位运算结果不符合预期
**解决方法**：适当添加括号明确运算顺序

### 5. 高斯消元实现错误
**错误表现**：第k小查询结果不正确
**解决方法**：仔细检查高斯消元的实现逻辑