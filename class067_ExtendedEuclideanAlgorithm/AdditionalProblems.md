# Class140 补充题目和训练

## 扩展欧几里得算法相关题目

### 1. 线性同余方程
- **题目来源**: 模板题
- **算法**: 扩展欧几里得算法
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

### 2. 乘法逆元
- **题目来源**: 模板题
- **算法**: 扩展欧几里得算法
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

### 3. 青蛙的约会
- **题目来源**: POJ 1061
- **题目链接**: http://poj.org/problem?id=1061
- **算法**: 扩展欧几里得算法
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

### 4. C Looooops
- **题目来源**: POJ 2115
- **题目链接**: http://poj.org/problem?id=2115
- **算法**: 扩展欧几里得算法
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

### 5. The Football Stage
- **题目来源**: Codeforces 1244C
- **题目链接**: https://codeforces.com/problemset/problem/1244/C
- **算法**: 扩展欧几里得算法
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

## 最大公约数相关题目

### 1. How Many Points?
- **题目来源**: LightOJ 1077
- **题目链接**: https://lightoj.com/problem/how-many-points
- **算法**: 最大公约数
- **时间复杂度**: O(log(min(dx,dy)))
- **空间复杂度**: O(log(min(dx,dy)))

### 2. Jewelry
- **题目来源**: HDU 5722
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5722
- **算法**: 最大公约数
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

### 3. Han Solo and Lazer Gun
- **题目来源**: Codeforces 514B
- **题目链接**: https://codeforces.com/problemset/problem/514/B
- **算法**: 最大公约数
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(log(min(a,b)))

## Pick定理相关题目

### 1. Area
- **题目来源**: POJ 1265
- **题目链接**: http://poj.org/problem?id=1265
- **算法**: Pick定理
- **时间复杂度**: O(n*log(max(dx,dy)))
- **空间复杂度**: O(1)

### 2. Trees on My Island
- **题目来源**: UVA 10088
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1029
- **算法**: Pick定理
- **时间复杂度**: O(n*log(max(dx,dy)))
- **空间复杂度**: O(1)

## 赛瓦维斯特定理相关题目

### 1. 小凯的疑惑
- **题目来源**: 洛谷 P3951
- **题目链接**: https://www.luogu.com.cn/problem/P3951
- **算法**: 赛瓦维斯特定理
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(1)

### 2. A New Change Problem
- **题目来源**: HDU 1792
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=1792
- **算法**: 赛瓦维斯特定理
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(1)

## 综合题目

### 1. 检查「好数组」
- **题目来源**: LeetCode 1250
- **题目链接**: https://leetcode.cn/problems/check-if-it-is-a-good-array/
- **算法**: 裴蜀定理
- **时间复杂度**: O(n*log(max(nums)))
- **空间复杂度**: O(1)

### 2. Lunlun Number
- **题目来源**: AtCoder ABC161 D
- **题目链接**: https://atcoder.jp/contests/abc161/tasks/abc161_d
- **算法**: BFS + 数学
- **时间复杂度**: O(K)
- **空间复杂度**: O(K)

### 3. Small Multiple
- **题目来源**: AtCoder ARC084 B
- **题目链接**: https://atcoder.jp/contests/abc077/tasks/arc084_b
- **算法**: 01-BFS
- **时间复杂度**: O(K)
- **空间复杂度**: O(K)

### 4. Pagodas
- **题目来源**: HDU 5512
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5512
- **算法**: 博弈论 + 数学
- **时间复杂度**: O(log(min(a,b)))
- **空间复杂度**: O(1)

## 训练建议

### 初级训练
1. 掌握扩展欧几里得算法的基本实现
2. 理解线性丢番图方程的解法
3. 熟悉最大公约数的计算方法

### 中级训练
1. 学习Pick定理的应用
2. 掌握赛瓦维斯特定理的使用场景
3. 练习将实际问题转化为数学模型

### 高级训练
1. 研究数论在算法竞赛中的高级应用
2. 学习更多数论定理和算法
3. 参与编程竞赛，提升解题能力

## 常见错误和注意事项

### 1. 数据溢出
- **问题**: 在计算大数时可能发生溢出
- **解决方案**: 使用long long类型，注意中间计算过程

### 2. 边界条件处理
- **问题**: 忽略了特殊情况的处理
- **解决方案**: 仔细分析边界条件，添加特殊判断

### 3. 精度问题
- **问题**: 浮点数计算可能导致精度丢失
- **解决方案**: 使用整数运算，避免浮点数计算

### 4. 算法选择错误
- **问题**: 对于不同问题选择了不合适的算法
- **解决方案**: 深入理解各种算法的适用场景

## 性能优化技巧

### 1. 预处理优化
- 对于重复计算的部分，可以预先计算并存储结果

### 2. 数学优化
- 利用数学性质简化计算过程

### 3. 空间换时间
- 在内存允许的情况下，使用缓存减少重复计算

### 4. 算法选择
- 根据数据规模选择合适的算法

## 测试用例设计

### 1. 正常情况
- 设计典型的输入数据，验证算法正确性

### 2. 边界情况
- 测试边界输入，如最小值、最大值等

### 3. 特殊情况
- 测试特殊情况，如无解、唯一解等

### 4. 极端情况
- 测试极端输入，如大数据量、特殊数据分布等

## 学习资源

### 1. 在线平台
- LeetCode: https://leetcode.cn/
- 洛谷: https://www.luogu.com.cn/
- Codeforces: https://codeforces.com/
- POJ: http://poj.org/
- AtCoder: https://atcoder.jp/

### 2. 参考书籍
- 《算法导论》
- 《算法竞赛入门经典》
- 《挑战程序设计竞赛》
- 《数论概论》

### 3. 在线教程
- 各大OJ平台的官方题解
- 算法竞赛相关博客和论坛
- YouTube上的算法讲解视频