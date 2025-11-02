# 线性基算法扩展题目大全

## 一、线性基算法核心题目（已实现）

### 1. 最大异或和问题
- **题目来源**：洛谷P3812 【模板】线性基
- **题目链接**：https://www.luogu.com.cn/problem/P3812
- **算法**：普通消元法
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(log(max_value))

### 2. 第k小异或和问题
- **题目来源**：LOJ #114. 第k小异或和
- **题目链接**：https://loj.ac/p/114
- **算法**：高斯消元法
- **时间复杂度**：O(n * log(max_value) + q * log(max_value))
- **空间复杂度**：O(log(max_value))

### 3. 元素问题（线性基+贪心）
- **题目来源**：洛谷P4570 [BJWC2011] 元素
- **题目链接**：https://www.luogu.com.cn/problem/P4570
- **算法**：线性基 + 贪心
- **时间复杂度**：O(n * log(n) + n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 4. 彩灯问题（线性基应用）
- **题目来源**：洛谷P3857 [TJOI2008] 彩灯
- **题目链接**：https://www.luogu.com.cn/problem/P3857
- **算法**：线性基
- **时间复杂度**：O(m * n)
- **空间复杂度**：O(n)

### 5. HDU 3949 XOR
- **题目来源**：HDU 3949 XOR
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=3949
- **算法**：高斯消元法
- **时间复杂度**：O(n * log(max_value) + q * log(max_value))
- **空间复杂度**：O(log(max_value))

### 6. Codeforces 1101G (Zero XOR Subset)-less
- **题目来源**：Codeforces 1101G
- **题目链接**：https://codeforces.com/problemset/problem/1101/G
- **算法**：普通消元法
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 7. BZOJ 2460 元素
- **题目来源**：BZOJ 2460
- **题目链接**：https://www.lydsy.com/JudgeOnline/problem.php?id=2460
- **算法**：线性基 + 贪心
- **时间复杂度**：O(n * log(n) + n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

## 二、线性基算法扩展题目（新增）

### 8. 最大XOR和路径（图论+线性基）
- **题目来源**：洛谷P4151 [WC2011] 最大XOR和路径
- **题目链接**：https://www.luogu.com.cn/problem/P4151
- **算法**：线性基 + 图论
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 9. 可持久化线性基（区间查询）
- **题目来源**：洛谷P4301 [CQOI2013] 新Nim游戏
- **题目链接**：https://www.luogu.com.cn/problem/P4301
- **算法**：可持久化线性基
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n * log(max_value))

### 10. 幸运数字（线性基+倍增）
- **题目来源**：洛谷P3292 [SCOI2016] 幸运数字
- **题目链接**：https://www.luogu.com.cn/problem/P3292
- **算法**：线性基 + 倍增
- **时间复杂度**：O(n * log(n) * log(max_value))
- **空间复杂度**：O(n * log(n) * log(max_value))

### 11. LeetCode 421. 数组中两个数的最大异或值
- **题目来源**：LeetCode 421
- **题目链接**：https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
- **算法**：线性基 / 字典树
- **时间复杂度**：O(n * 32)
- **空间复杂度**：O(32)

### 12. LeetCode 1738. 找出第 K 大的异或坐标值
- **题目来源**：LeetCode 1738
- **题目链接**：https://leetcode.cn/problems/find-kth-largest-xor-coordinate-value/
- **算法**：二维前缀异或和 + 排序
- **时间复杂度**：O(m*n + m*n*log(m*n))
- **空间复杂度**：O(m*n)

### 13. Codeforces 282E. XOR and Favorite Number
- **题目来源**：Codeforces 282E
- **题目链接**：https://codeforces.com/problemset/problem/282/E
- **算法**：滑动窗口 + 线性基
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 14. AtCoder ABC141 F. Xor Sum 3
- **题目来源**：AtCoder ABC141 F
- **题目链接**：https://atcoder.jp/contests/abc141/tasks/abc141_f
- **算法**：线性基
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(log(max_value))

### 15. Codeforces 938G. Shortest Path Queries
- **题目来源**：Codeforces 938G
- **题目链接**：https://codeforces.com/problemset/problem/938/G
- **算法**：线性基 + 分治
- **时间复杂度**：O(n * log(n) * log(max_value))
- **空间复杂度**：O(n * log(max_value))

### 16. 洛谷P4580 [BJOI2014] 路径
- **题目来源**：洛谷P4580
- **题目链接**：https://www.luogu.com.cn/problem/P4580
- **算法**：线性基 + 图论
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 17. 洛谷P5556 「NWERC2017」Artwork
- **题目来源**：洛谷P5556
- **题目链接**：https://www.luogu.com.cn/problem/P5556
- **算法**：并查集 + 线性基
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 18. 洛谷P4609 [FJOI2016] 建筑师
- **题目来源**：洛谷P4609
- **题目链接**：https://www.luogu.com.cn/problem/P4609
- **算法**：线性基 + 组合数学
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n + log(max_value))

### 19. HDU 6579. Operation
- **题目来源**：HDU 6579
- **题目链接**：http://acm.hdu.edu.cn/showproblem.php?pid=6579
- **算法**：线性基 + 可持久化
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n * log(max_value))

### 20. CodeChef XRQRS. Xor Queries
- **题目来源**：CodeChef XRQRS
- **题目链接**：https://www.codechef.com/problems/XRQRS
- **算法**：可持久化线性基
- **时间复杂度**：O(n * log(max_value))
- **空间复杂度**：O(n * log(max_value))

## 三、线性基算法分类总结

### 按应用场景分类

#### 1. 基础线性基问题
- 最大异或和（P3812）
- 第k小异或和（LOJ #114）
- 判断异或值是否可达

#### 2. 线性基+贪心问题
- 元素问题（P4570, BZOJ 2460）
- 带权值的线性基问题

#### 3. 线性基+图论问题
- 最大XOR和路径（P4151）
- 路径问题（P4580）

#### 4. 可持久化线性基问题
- 区间查询问题（P4301）
- 动态维护问题（HDU 6579）

#### 5. 线性基+其他算法
- 线性基+倍增（P3292）
- 线性基+分治（CF 938G）
- 线性基+并查集（P5556）

### 按难度级别分类

#### 入门级（适合初学者）
- 洛谷P3812 【模板】线性基
- LeetCode 421. 数组中两个数的最大异或值
- HDU 3949 XOR

#### 进阶级（需要理解线性基性质）
- 洛谷P4570 [BJWC2011] 元素
- 洛谷P3857 [TJOI2008] 彩灯
- Codeforces 1101G

#### 高级（需要结合其他算法）
- 洛谷P4151 [WC2011] 最大XOR和路径
- 洛谷P3292 [SCOI2016] 幸运数字
- Codeforces 938G. Shortest Path Queries

## 四、线性基算法工程化考量

### 1. 异常处理与边界条件
- 空数组处理
- 单元素数组处理
- 全0数组处理
- 超大数值溢出处理

### 2. 性能优化策略
- IO优化（大数据量输入）
- 位运算优化
- 内存访问优化
- 缓存友好设计

### 3. 多语言实现差异
- Java：long类型，自动初始化
- C++：long long类型，需要手动初始化
- Python：int类型无限制，但位运算效率较低

### 4. 测试用例设计
- 边界测试：空数组、单元素、全0
- 功能测试：普通情况、线性相关情况
- 性能测试：大数据量、极端值

### 5. 代码可复用性
- 封装线性基类
- 模块化设计
- 清晰的接口定义
- 完善的文档注释

## 五、线性基算法学习路径

### 第一阶段：基础概念
1. 理解线性基的定义和性质
2. 掌握普通消元法和高斯消元法
3. 实现基础的最大异或和问题

### 第二阶段：应用实践
1. 解决线性基+贪心问题
2. 理解线性基在图论中的应用
3. 掌握第k小异或和的计算

### 第三阶段：高级应用
1. 学习可持久化线性基
2. 掌握线性基与其他算法的结合
3. 解决复杂竞赛题目

### 第四阶段：工程化优化
1. 性能优化和异常处理
2. 多语言实现和测试
3. 算法扩展和创新应用

## 六、线性基算法与其他领域的联系

### 1. 与机器学习的联系
- 特征选择：线性基选择最具代表性的向量
- 降维：将n维空间压缩为r维表示
- 线性无关：寻找线性无关的特征子集

### 2. 与线性代数的联系
- 基向量概念
- 线性相关性和线性无关性
- 向量空间理论

### 3. 与信息论的联系
- 信息编码
- 错误检测和纠正
- 数据压缩

通过系统学习线性基算法，不仅可以解决各类算法竞赛中的问题，还能培养对线性代数思想的应用能力，为更复杂算法的学习打下坚实基础。