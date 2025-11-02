# 线段树算法全面扩展题目列表

## 一、基础线段树题目（已实现）

### 1. LeetCode 基础题目
- **LeetCode 307. Range Sum Query - Mutable** - 单点更新+区间查询
- **LeetCode 308. Range Sum Query 2D - Mutable** - 二维线段树
- **LeetCode 315. Count of Smaller Numbers After Self** - 线段树+离散化
- **LeetCode 327. Count of Range Sum** - 线段树+前缀和
- **LeetCode 493. Reverse Pairs** - 线段树+离散化
- **LeetCode 218. The Skyline Problem** - 扫描线算法
- **LeetCode 699. Falling Squares** - 线段树+懒惰标记
- **LeetCode 715. Range Module** - 动态开点线段树
- **LeetCode 732. My Calendar III** - 动态开点线段树

### 2. Codeforces 经典题目
- **Codeforces 52C. Circular RMQ** - 线段树+懒惰标记
- **Codeforces 339D. Xenia and Bit Operations** - 线段树+位运算
- **Codeforces 438D. The Child and Sequence** - 吉司机线段树
- **Codeforces 1401F. Reverse and Swap** - 线段树+位运算
- **Codeforces 242E. XOR on Segment** - 线段树+异或操作

### 3. HDU 题目
- **HDU 1166. 敌兵布阵** - 线段树基础
- **HDU 1754. I Hate It** - 线段树基础
- **HDU 1698. Just a Hook** - 线段树+懒惰标记
- **HDU 1199. Color the Ball** - 线段树+离散化
- **HDU 1542. Atlantis** - 扫描线算法

### 4. POJ 题目
- **POJ 3468. A Simple Problem with Integers** - 线段树+懒惰标记
- **POJ 2777. Count Color** - 线段树+位运算
- **POJ 2528. Mayor's posters** - 线段树+离散化

### 5. SPOJ 题目
- **SPOJ GSS1. Can you answer these queries I** - 最大子段和线段树
- **SPOJ GSS2. Can you answer these queries II** - 线段树+扫描线
- **SPOJ GSS3. Can you answer these queries III** - 最大子段和线段树（支持更新）
- **SPOJ GSS4. Can you answer these queries IV** - 线段树+剪枝优化
- **SPOJ GSS5. Can you answer these queries V** - 最大子段和线段树（不连续区间）

### 6. 洛谷题目
- **洛谷 P3372. 【模板】线段树 1** - 线段树+懒惰标记
- **洛谷 P3373. 【模板】线段树 2** - 线段树+多种懒惰标记
- **洛谷 P3919. 【模板】可持久化数组** - 可持久化线段树
- **洛谷 P3834. 【模板】可持久化线段树1** - 主席树（静态区间第K小）
- **洛谷 P4198. 楼房重建** - 线段树维护斜率
- **洛谷 P1198. [JSOI2008] 最大数** - 线段树+动态数组

## 二、新增线段树题目（需要补充实现）

### 1. LeetCode 新增题目

#### LeetCode 1157. Online Majority Element In Subarray
- **题目链接**: https://leetcode.com/problems/online-majority-element-in-subarray/
- **难度**: 困难
- **算法**: 线段树+二分查找
- **题目描述**: 查询指定子数组中出现次数超过阈值的元素

#### LeetCode 1649. Create Sorted Array through Instructions
- **题目链接**: https://leetcode.com/problems/create-sorted-array-through-instructions/
- **难度**: 困难
- **算法**: 线段树+离散化
- **题目描述**: 通过一系列指令创建一个有序数组，计算插入每个元素的代价

#### LeetCode 1505. Minimum Possible Integer After at Most K Adjacent Swaps On Digits
- **题目链接**: https://leetcode.com/problems/minimum-possible-integer-after-at-most-k-adjacent-swaps-on-digits/
- **难度**: 困难
- **算法**: 线段树+贪心
- **题目描述**: 最多进行K次相邻交换，得到最小的整数

#### LeetCode 1520. Maximum Number of Non-Overlapping Substrings
- **题目链接**: https://leetcode.com/problems/maximum-number-of-non-overlapping-substrings/
- **难度**: 困难
- **算法**: 线段树+贪心
- **题目描述**: 找到最多的不重叠子串

### 2. Codeforces 新增题目

#### Codeforces 61E. Enemy is weak
- **题目链接**: https://codeforces.com/problemset/problem/61/E
- **难度**: 中等
- **算法**: 线段树+逆序对
- **题目描述**: 计算三元逆序对数量

#### Codeforces 220B. Little Elephant and Array
- **题目链接**: https://codeforces.com/problemset/problem/220/B
- **难度**: 中等
- **算法**: 莫队算法+线段树
- **题目描述**: 查询区间内出现次数等于数值的元素个数

#### Codeforces 459D. Pashmak and Parmida's problem
- **题目链接**: https://codeforces.com/problemset/problem/459/D
- **难度**: 困难
- **算法**: 线段树+离散化
- **题目描述**: 计算满足条件的(i,j)对数量

#### Codeforces 474E. Pillars
- **题目链接**: https://codeforces.com/problemset/problem/474/E
- **难度**: 困难
- **算法**: 线段树优化DP
- **题目描述**: 找到最长的满足条件的子序列

### 3. HDU 新增题目

#### HDU 4027. Can you answer these queries?
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=4027
- **难度**: 中等
- **算法**: 线段树+开方操作
- **题目描述**: 区间开方和区间求和

#### HDU 4578. Transformation
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=4578
- **难度**: 困难
- **算法**: 线段树+多种操作
- **题目描述**: 支持区间加、乘、赋值、幂次和查询

#### HDU 4614. Vases and Flowers
- **题目链接**: http://acm.hdu.edu.cn/showproblem.php?pid=4614
- **难度**: 中等
- **算法**: 线段树+二分查找
- **题目描述**: 花瓶和花朵问题

### 4. POJ 新增题目

#### POJ 2828. Buy Tickets
- **题目链接**: http://poj.org/problem?id=2828
- **难度**: 中等
- **算法**: 线段树+逆序处理
- **题目描述**: 排队买票问题

#### POJ 2886. Who Gets the Most Candies?
- **题目链接**: http://poj.org/problem?id=2886
- **难度**: 困难
- **算法**: 线段树+约瑟夫问题
- **题目描述**: 糖果分配问题

#### POJ 3321. Apple Tree
- **题目链接**: http://poj.org/problem?id=3321
- **难度**: 中等
- **算法**: 线段树+DFS序
- **题目描述**: 苹果树问题

### 5. SPOJ 新增题目

#### SPOJ KGSS. Maximum Sum
- **题目链接**: https://www.spoj.com/problems/KGSS/
- **难度**: 中等
- **算法**: 线段树维护最大值和次大值
- **题目描述**: 查询区间内两个元素的最大和

#### SPOJ DQUERY. D-query
- **题目链接**: https://www.spoj.com/problems/DQUERY/
- **难度**: 中等
- **算法**: 离线处理+线段树
- **题目描述**: 查询区间内不同元素的个数

#### SPOJ MKTHNUM. K-th Number
- **题目链接**: https://www.spoj.com/problems/MKTHNUM/
- **难度**: 困难
- **算法**: 主席树（可持久化线段树）
- **题目描述**: 查询区间第K小的元素

### 6. 洛谷新增题目

#### 洛谷 P5490. 【模板】扫描线
- **题目链接**: https://www.luogu.com.cn/problem/P5490
- **难度**: 困难
- **算法**: 扫描线算法
- **题目描述**: 矩形面积并

#### 洛谷 P1903. [国家集训队] 数颜色 / 维护队列
- **题目链接**: https://www.luogu.com.cn/problem/P1903
- **难度**: 困难
- **算法**: 带修莫队+线段树
- **题目描述**: 支持修改的区间不同数查询

#### 洛谷 P1972. [SDOI2009] HH的项链
- **题目链接**: https://www.luogu.com.cn/problem/P1972
- **难度**: 困难
- **算法**: 离线处理+线段树
- **题目描述**: 查询区间内不同元素的个数

### 7. 其他平台新增题目

#### AtCoder ABC185F. Range Xor Query
- **题目链接**: https://atcoder.jp/contests/abc185/tasks/abc185_f
- **难度**: 中等
- **算法**: 线段树+异或操作
- **题目描述**: 区间异或查询

#### AtCoder ABC157E. Simple String Queries
- **题目链接**: https://atcoder.jp/contests/abc157/tasks/abc157_e
- **难度**: 中等
- **算法**: 线段树+位运算
- **题目描述**: 字符串区间查询

#### ZOJ 2112. Dynamic Rankings
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
- **难度**: 困难
- **算法**: 树状数组+主席树
- **题目描述**: 动态区间第K小

#### ZOJ 3583. Simple Path
- **题目链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
- **难度**: 困难
- **算法**: 线段树+LCA
- **题目描述**: 树上路径查询

## 三、高级线段树应用

### 1. 可持久化线段树（主席树）
- **静态区间第K小** - 经典应用
- **区间不同数个数** - 离线处理
- **区间众数查询** - 复杂统计
- **历史版本查询** - 版本控制

### 2. 扫描线算法
- **矩形面积并** - 经典应用
- **矩形周长并** - 复杂计算
- **多边形面积** - 几何应用
- **重叠区域统计** - 空间分析

### 3. 李超线段树
- **维护直线** - 动态凸包
- **维护线段** - 几何优化
- **函数最值查询** - 数学应用

### 4. 动态开点线段树
- **值域线段树** - 大值域处理
- **颜色段均摊** - 高效更新
- **区间赋值优化** - 懒标记技巧

### 5. 线段树合并
- **树上路径问题** - 树形结构
- **子树统计** - 深度优先
- **连通块维护** - 图论应用

### 6. 线段树分裂
- **区间分裂操作** - 动态维护
- **平衡树替代** - 高效操作
- **区间复制** - 版本管理

## 四、线段树工程化考量

### 1. 异常处理策略
- **空数组处理** - 边界检查
- **索引越界** - 参数验证
- **数值溢出** - 类型选择
- **内存管理** - 资源释放

### 2. 性能优化技巧
- **位运算优化** - 高效计算
- **内存池技术** - 减少分配
- **缓存友好** - 局部性原理
- **懒标记优化** - 延迟更新

### 3. 可测试性设计
- **单元测试覆盖** - 功能验证
- **边界测试用例** - 极端情况
- **性能测试基准** - 效率评估
- **压力测试验证** - 稳定性检查

### 4. 可维护性考虑
- **清晰的代码结构** - 模块化设计
- **详细的注释说明** - 文档完善
- **统一的编码规范** - 风格一致
- **易于扩展的接口** - 功能增强

## 五、面试要点总结

### 1. 基础概念考察
- **线段树结构原理** - 二叉树理解
- **时间复杂度分析** - 算法效率
- **空间复杂度分析** - 内存使用
- **适用场景判断** - 问题识别

### 2. 实现能力评估
- **建树过程实现** - 递归/迭代
- **查询操作实现** - 区间处理
- **更新操作实现** - 单点/区间
- **懒标记实现** - 优化技巧

### 3. 问题解决能力
- **识别适用特征** - 算法选择
- **设计数据结构** - 模型构建
- **分析复杂度** - 性能评估
- **处理边界情况** - 鲁棒性

### 4. 进阶知识掌握
- **可持久化线段树** - 历史版本
- **扫描线算法** - 几何应用
- **动态开点技术** - 空间优化
- **线段树优化DP** - 动态规划

通过系统学习以上内容，可以全面掌握线段树算法，为算法竞赛和工程实践打下坚实基础。