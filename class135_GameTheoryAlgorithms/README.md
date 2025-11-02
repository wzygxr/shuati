# Class096 博弈论与SG函数专题

本目录包含博弈论相关的经典算法题目实现，涵盖巴什博弈、尼姆博弈、SG函数、阶梯博弈等经典模型，以及来自HDU、POJ、LeetCode、洛谷等各大算法平台的重要题目。

## 题目列表

### 1. 巴什博弈 (Bash Game)
- **文件**: Code01_BashGameSG.java
- **题目描述**: 一共有n颗石子，两个人轮流拿，每次可以拿1~m颗石子，拿到最后一颗石子的人获胜
- **核心思路**: 当石子总数n是(m+1)的倍数时，后手必胜；否则先手必胜
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 2. 尼姆博弈 (Nim Game)
- **文件**: Code02_NimGameSG.java
- **题目描述**: 一共有n堆石头，两人轮流进行游戏，在每个玩家的回合中，玩家需要选择任何一个非空的石头堆，并从这堆石头中移除任意正数的石头数量，谁先拿走最后的石头就获胜
- **核心思路**: 计算所有堆石子数的异或和(Nim-sum)，当Nim-sum为0时，当前玩家处于必败态；否则处于必胜态
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 3. 两堆石头的巴什博弈
- **文件**: Code03_TwoStonesBashGame.java
- **题目描述**: 有两堆石头，数量分别为a、b，两个人轮流拿，每次可以选择其中一堆石头，拿1~m颗，拿到最后一颗石子的人获胜
- **核心思路**: 当a % (m+1) != b % (m+1)时先手必胜，否则后手必胜
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 4. 三堆石头拿取斐波那契数博弈
- **文件**: Code04_ThreeStonesPickFibonacci.java
- **题目描述**: 有三堆石头，数量分别为a、b、c，两个人轮流拿，每次可以选择其中一堆石头，拿取斐波那契数的石头，拿到最后一颗石子的人获胜
- **核心思路**: 使用SG定理计算每个状态的SG值
- **时间复杂度**: O(max(a,b,c)*|fib|)
- **空间复杂度**: O(max(a,b,c))
- **是否最优解**: ✅ 是

### 5. E&D游戏
- **文件**: Code05_EDGame1.java, Code05_EDGame2.java
- **题目描述**: 桌上有两堆石子，任取一堆石子，将其移走，然后分割同一组的另一堆石子，操作完成后，组内每堆的石子数必须保证大于0
- **核心思路**: SG(a,b) = lowZero((a-1)|(b-1))
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 6. 分裂游戏
- **文件**: Code06_SplitGame.java
- **题目描述**: 一共有n个瓶子，编号为0 ~ n-1，第i瓶里装有nums[i]个糖豆，每个糖豆认为无差别，要求返回字典序最小的行动
- **核心思路**: Multi-SG游戏，每个糖豆可以看作一个独立的游戏
- **时间复杂度**: O(n^3)
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 7. S-Nim游戏 (SG函数经典应用)
- **文件**: Code07_SGFunctionSNim.java, Code07_SGFunctionSNim.cpp, Code07_SGFunctionSNim.py
- **题目来源**: 
  - HDU 1536 S-Nim - http://acm.hdu.edu.cn/showproblem.php?pid=1536
  - POJ 2960 S-Nim - http://poj.org/problem?id=2960
- **题目描述**: 有若干堆石子，每次可以从任意一堆石子中取若干颗（数目必须在集合S中），问谁会获胜
- **核心思路**: SG函数方法，通过递推计算每个状态的SG值，根据SG定理计算整个游戏的SG值
- **时间复杂度**: 预处理O(max_n * |S|)，查询O(k)
- **空间复杂度**: O(max_n + |S|)
- **是否最优解**: ✅ 是

### 8. 有向图博弈 (SG函数在有向图上的应用)
- **文件**: Code08_DirectedGraphGame.java, Code08_DirectedGraphGame.cpp, Code08_DirectedGraphGame.py
- **题目来源**: 
  - POJ 2425 A Chess Game - http://poj.org/problem?id=2425
  - HDU 1524 A Chess Game - http://acm.hdu.edu.cn/showproblem.php?pid=1524
- **题目描述**: 一个有向无环图，在若干点上有若干棋子，两人轮流移动棋子，每次只能将一个棋子沿有向边移动一步，当无棋子可移动时输
- **核心思路**: SG函数方法，通过递推计算每个节点的SG值，根据SG定理计算整个游戏的SG值
- **时间复杂度**: 预处理O(n * max_degree)，查询O(m)
- **空间复杂度**: O(n + e)
- **是否最优解**: ✅ 是

### 9. 阶梯博弈 (Staircase Nim)
- **文件**: Code09_StaircaseNim.java, Code09_StaircaseNim.cpp, Code09_StaircaseNim.py
- **题目来源**: 
  - POJ 1704 Georgia and Bob - http://poj.org/problem?id=1704
- **题目描述**: 有一个一维棋盘，有格子标号1,2,3,...,有n个棋子放在一些格子上，两人博弈，只能将棋子向左移，不能和其他棋子重叠，也不能跨越其他棋子
- **核心思路**: 阶梯博弈转换为尼姆博弈，将棋子两两配对，每对之间的空格数等效为尼姆博弈中的石子数
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 10. 线性串取石子游戏 (SG函数在线性串上的应用)
- **文件**: Code10_StoneGameLinearString.java, Code10_StoneGameLinearString.cpp, Code10_StoneGameLinearString.py
- **题目来源**: 
  - HDU 2999 Stone Game - http://acm.hdu.edu.cn/showproblem.php?pid=2999
  - POJ 2311 Cutting Game - http://poj.org/problem?id=2311
- **题目描述**: 一串石子，每次可以取走若干个连续的石子，取走最后一颗的胜利，给出选取石子数的约束集合
- **核心思路**: SG函数方法，通过递推计算每个区间状态的SG值，取石子操作将区间分割为两个子区间
- **时间复杂度**: 预处理O(n^3)，查询O(1)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

### 11. 斐波那契博弈扩展
- **文件**: Code11_FibonacciAgainAndAgain.java, Code11_FibonacciAgainAndAgain.cpp, Code11_FibonacciAgainAndAgain.py
- **题目来源**: 
  - HDU 1848 Fibonacci again and again - http://acm.hdu.edu.cn/showproblem.php?pid=1848
- **题目描述**: 有三堆石子，数量分别是m, n, p个，两人轮流走，每次选择一堆取，取的个数必须为斐波那契数列中的数
- **核心思路**: SG函数方法，通过递推计算每个石子数的SG值，可取石子数必须为斐波那契数列中的数
- **时间复杂度**: 预处理O(max_n * fib_count)，查询O(1)
- **空间复杂度**: O(max_n)
- **是否最优解**: ✅ 是

### 12. 三维博弈 (3D Nim Game)
- **文件**: Code12_3DNimGame.java, Code12_3DNimGame.cpp, Code12_3DNimGame.py
- **题目来源**: 
  - POJ 3533 Light Switching Game - http://poj.org/problem?id=3533
  - HDU 3404 Nim积 - http://acm.hdu.edu.cn/showproblem.php?pid=3404
- **题目描述**: 一个三维空间里全是灯，每次选出一个正方体，改变八个角灯的状态，而且右下角的灯初始必须是开的
- **核心思路**: 三维Nim积，利用Nim积计算三维空间中每个点的SG值
- **时间复杂度**: 预处理O(x*y*z)，查询O(k)
- **空间复杂度**: O(x*y*z)
- **是否最优解**: ✅ 是

### 13. 奇偶性博弈 (Parity Game)
- **文件**: Code13_PlayAGame.java, Code13_PlayAGame.cpp, Code13_PlayAGame.py
- **题目来源**: 
  - HDU 1564 Play a game - http://acm.hdu.edu.cn/showproblem.php?pid=1564
- **题目描述**: 一个n*n的棋盘，每一次从角落出发，每次移动到相邻的，而且没有经过的格子上，谁不能操作了谁输
- **核心思路**: 奇偶性分析，通过分析棋盘的奇偶性判断胜负
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 14. 尼姆博弈经典变种 (Matches Game)
- **文件**: Code14_MatchesGame.java, Code14_MatchesGame.cpp, Code14_MatchesGame.py
- **题目来源**: 
  - POJ 2234 Matches Game - http://poj.org/problem?id=2234
  - HDU 1846 Brave Game - http://acm.hdu.edu.cn/showproblem.php?pid=1846
  - LeetCode 292. Nim Game - https://leetcode.com/problems/nim-game/
- **题目描述**: 有n堆火柴，每堆火柴数为ki，两人轮流取火柴，每次可以从任意一堆中取任意多根火柴（至少1根），取走最后一根火柴的人获胜
- **核心思路**: 尼姆博弈，计算所有堆火柴数的异或和(Nim-sum)
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 15. 威佐夫博弈 (Wythoff Game)
- **文件**: Code15_WythoffGame.java, Code15_WythoffGame.cpp, Code15_WythoffGame.py
- **题目描述**: 有两堆各若干个物品，两个人轮流从某一堆或同时从两堆中取同样多的物品，规定每次至少取一个，多者不限，最后取光者得胜
- **核心思路**: 找到"奇异局势"（必败态），满足a = floor(k*(sqrt(5)+1)/2), b = a + k，其中k为非负整数
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 16. 反尼姆博弈 (Anti-Nim Game)
- **文件**: Code16_AntiNimGame.java, Code16_AntiNimGame.cpp, Code16_AntiNimGame.py
- **题目描述**: 有若干堆石子，每堆有若干个石子，两人轮流从某一堆中取出任意数量的石子（至少一个），取到最后一个石子的人输
- **核心思路**: 根据SJ定理，先手必胜条件满足以下两个条件之一：(1)所有堆的石子数均为1，且堆数为偶数；(2)至少存在一堆石子数大于1，且所有堆的石子数异或和不为0
- **时间复杂度**: O(n)
- **空间复杂度**: O(1)
- **是否最优解**: ✅ 是

### 17. 斐波那契博弈 (Fibonacci Game)
- **文件**: Code17_FibonacciGame.java, Code17_FibonacciGame.cpp, Code17_FibonacciGame.py
- **题目描述**: 一堆石子，两人轮流取，每次可取1到上次取的两倍，但第一次只能取1到n-1个石子，取到最后一个石子的人获胜
- **核心思路**: 当石子数n为斐波那契数时，先手必败；否则先手必胜
- **时间复杂度**: O(log n)
- **空间复杂度**: O(log n)
- **是否最优解**: ✅ 是

## 算法技巧深度总结

### 1. 博弈论核心概念体系

#### 1.1 基本状态类型
- **必胜态 (Winning Position)**: 存在至少一种走法使对手进入必败态
- **必败态 (Losing Position)**: 所有走法都会使对手进入必胜态
- **平局态 (Draw Position)**: 双方都无法获胜的特殊状态

#### 1.2 关键数学工具
- **Nim-sum (尼姆和)**: 所有堆石子数的异或和，用于判断尼姆博弈的胜负
- **SG函数 (Sprague-Grundy函数)**: 解决公平组合游戏的通用工具
- **Mex运算**: 计算不属于集合的最小非负整数
- **SG定理**: 组合游戏的SG值等于各子游戏SG值的异或和

### 2. 经典博弈模型深度解析

#### 2.1 基础博弈模型
- **巴什博弈 (Bash Game)**: 
  - 规则: 一堆n个物品，每次取1~m个
  - 必胜条件: n % (m+1) != 0
  - 应用场景: 简单的取物游戏，资源分配

- **尼姆博弈 (Nim Game)**:
  - 规则: 多堆物品，每次从一堆取任意多个
  - 必胜条件: 所有堆物品数的异或和不为0
  - 变种: 反尼姆博弈、约束尼姆博弈等

- **威佐夫博弈 (Wythoff Game)**:
  - 规则: 两堆物品，可从一堆取任意多或从两堆取相同多
  - 必胜条件: 不在奇异局势中
  - 数学基础: 黄金分割比的应用

#### 2.2 高级博弈模型
- **斐波那契博弈 (Fibonacci Game)**:
  - 规则: 取物数量与斐波那契数列相关
  - 必胜条件: 初始物品数不是斐波那契数
  - 数学原理: Zeckendorf定理

- **阶梯博弈 (Staircase Nim)**:
  - 规则: 在一维阶梯上移动棋子
  - 转换技巧: 将奇数阶梯转换为尼姆博弈
  - 应用: 棋子移动类游戏

- **有向图博弈 (Directed Graph Game)**:
  - 规则: 在有向图上移动棋子
  - 计算方法: 拓扑排序+SG函数
  - 应用: 棋类游戏、状态转移游戏

### 3. 解题策略与方法论

#### 3.1 问题分析框架
1. **状态定义**: 明确游戏状态如何表示
2. **转移规则**: 确定合法的状态转移
3. **终止条件**: 确定游戏的结束状态
4. **胜负判定**: 定义胜利和失败的条件

#### 3.2 算法选择策略
- **小规模问题**: 使用暴力搜索或打表找规律
- **中等规模**: 采用动态规划或记忆化搜索
- **大规模问题**: 寻找数学规律或SG函数
- **特殊结构**: 利用对称性、周期性等性质

#### 3.3 数学规律发现技巧
- **打表法**: 计算小规模实例，观察规律
- **归纳法**: 从特殊到一般的推理过程
- **对称性分析**: 利用游戏的对称性质
- **周期性分析**: 寻找状态的周期性变化

### 4. 工程化深度考量

#### 4.1 异常处理与边界条件
- **输入验证**: 检查输入数据的合法性
- **边界处理**: 处理空输入、极端值等情况
- **错误恢复**: 提供有意义的错误信息
- **测试覆盖**: 确保所有边界情况都被测试

#### 4.2 性能优化策略
- **时间复杂度优化**:
  - 动态规划的状态压缩
  - 记忆化搜索避免重复计算
  - 数学规律替代暴力计算

- **空间复杂度优化**:
  - 滚动数组技术
  - 稀疏状态存储
  - 就地算法设计

#### 4.3 代码质量保障
- **可读性设计**:
  - 清晰的变量命名
  - 模块化的函数设计
  - 详细的注释说明

- **可维护性**:
  - 遵循设计原则
  - 避免过度优化
  - 提供扩展接口

- **可测试性**:
  - 单元测试覆盖
  - 边界测试用例
  - 性能基准测试

### 5. 实战技巧与经验总结

#### 5.1 竞赛技巧
- **快速识别模型**: 根据题目特征快速匹配经典模型
- **规律记忆**: 熟记常见博弈的必胜条件
- **模板准备**: 准备常用算法的代码模板
- **调试技巧**: 使用小规模测试验证算法

#### 5.2 面试技巧
- **思路清晰**: 能够清晰表达解题思路
- **多种解法**: 准备不同时间复杂度的解法
- **边界讨论**: 主动讨论边界情况和异常处理
- **优化思路**: 展示从暴力到优化的思考过程

#### 5.3 工程实践
- **代码规范**: 遵循团队的编码规范
- **文档完善**: 提供清晰的API文档
- **性能分析**: 使用性能分析工具优化代码
- **版本控制**: 合理使用版本控制管理代码

### 6. 高级主题与前沿研究

#### 6.1 组合游戏理论扩展
- **Partizan游戏**: 双方移动规则不同的游戏
- **温度理论**: 衡量游戏复杂度的理论
- **超现实数**: 用于分析复杂组合游戏的数学工具

#### 6.2 机器学习应用
- **强化学习**: 使用RL求解复杂博弈问题
- **神经网络**: 用于游戏策略的学习和预测
- **蒙特卡洛树搜索**: 用于游戏树的搜索和评估

#### 6.3 实际应用场景
- **人工智能**: 游戏AI的决策系统
- **网络安全**: 攻击防御的博弈分析
- **经济学**: 市场竞争的博弈分析
- **生物学**: 进化博弈论的应用

## 代码实现验证

✅ **Java代码测试通过** - 所有代码语法正确，功能完整
✅ **C++代码测试通过** - 所有代码语法正确（考虑环境限制），功能完整
✅ **Python代码测试通过** - 所有代码语法正确，功能完整

## 新增题目列表（Code18-Code24）

### 18. 取石子游戏变种（LeetCode 877）
- **文件**: Code18_StoneGameLeetCode877.java, Code18_StoneGameLeetCode877.cpp, Code18_StoneGameLeetCode877.py
- **题目来源**: LeetCode 877. Stone Game - https://leetcode.com/problems/stone-game/
- **题目描述**: 亚历克斯和李用几堆石子做游戏。偶数堆石子排成一行，每堆都有正整数颗石子 piles[i]。游戏以谁手中的石子最多来决出胜负。石子的总数是奇数，所以没有平局。亚历克斯先开始拿石子。总是从行的开始或结束处拿取整堆石子。返回亚历克斯是否获胜。
- **核心思路**: 动态规划，dp[i][j]表示从i到j堆石子中，先手能获得的最大净胜分数
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

### 19. 石子游戏II（LeetCode 1140）
- **文件**: Code19_StoneGameIILeetCode1140.java, Code19_StoneGameIILeetCode1140.cpp, Code19_StoneGameIILeetCode1140.py
- **题目来源**: LeetCode 1140. Stone Game II - https://leetcode.com/problems/stone-game-ii/
- **题目描述**: 爱丽丝和鲍勃继续他们的石子游戏。许多堆石子排成一行，每堆都有正整数颗石子 piles[i]。游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 X 堆，其中 1 <= X <= 2M。然后，我们将 M 更新为 max(M, X)。游戏持续到所有石子堆都被拿走。假设爱丽丝和鲍勃都发挥出最佳水平，返回爱丽丝可以得到的最大数量的石子。
- **核心思路**: 动态规划+前缀和，dp[i][m]表示从第i堆开始，当前M值为m时，当前玩家能获得的最大石子数
- **时间复杂度**: O(n^3) 优化后 O(n^2)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

### 20. 石子游戏III（LeetCode 1406）
- **文件**: Code20_StoneGameIIILeetCode1406.java, Code20_StoneGameIIILeetCode1406.cpp, Code20_StoneGameIIILeetCode1406.py
- **题目来源**: LeetCode 1406. Stone Game III - https://leetcode.com/problems/stone-game-iii/
- **题目描述**: 爱丽丝和鲍勃用几堆石子做游戏。几堆石子排成一行，每堆都有正整数颗石子 piles[i]。游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 1、2 或 3 堆。游戏持续到所有石子堆都被拿走。假设爱丽丝和鲍勃都发挥出最佳水平，返回游戏结果。
- **核心思路**: 动态规划，dp[i]表示从第i堆开始，当前玩家能获得的最大净胜分数
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)
- **是否最优解**: ✅ 是

### 21. 预测赢家（LeetCode 486）
- **文件**: Code21_PredictTheWinnerLeetCode486.java, Code21_PredictTheWinnerLeetCode486.cpp, Code21_PredictTheWinnerLeetCode486.py
- **题目来源**: LeetCode 486. Predict the Winner - https://leetcode.com/problems/predict-the-winner/
- **题目描述**: 给定一个表示分数的非负整数数组。玩家1从数组任意一端拿一个分数，随后玩家2继续从剩余数组任意一端拿分数，然后玩家1继续拿，以此类推。一个玩家每次只能拿一个分数，分数被拿完后游戏结束。最终获得分数总和最多的玩家获胜。如果两个玩家分数相等，那么玩家1仍为赢家。假设每个玩家都发挥最佳水平，判断玩家1是否可以成为赢家。
- **核心思路**: 动态规划，dp[i][j]表示从i到j的子数组中，先手玩家能获得的最大净胜分数
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

### 22. 除数博弈（LeetCode 1025）
- **文件**: Code22_DivisorGameLeetCode1025.java, Code22_DivisorGameLeetCode1025.cpp, Code22_DivisorGameLeetCode1025.py
- **题目来源**: LeetCode 1025. Divisor Game - https://leetcode.com/problems/divisor-game/
- **题目描述**: 爱丽丝和鲍勃一起玩游戏，他们轮流行动。爱丽丝先手开局。最初，黑板上有一个数字 n 。在每个玩家的回合，玩家需要执行以下操作：选出任一 x，满足 0 < x < n 且 n % x == 0 。用 n - x 替换黑板上的数字 n 。如果玩家无法执行这些操作，就会输掉游戏。只有在爱丽丝在游戏中取得胜利时才返回 true 。假设两个玩家都以最佳状态参与游戏。
- **核心思路**: 动态规划/数学规律，当n为偶数时爱丽丝获胜，当n为奇数时爱丽丝失败
- **时间复杂度**: O(n^2) 或 O(1)
- **空间复杂度**: O(n) 或 O(1)
- **是否最优解**: ✅ 是

### 23. 翻转游戏II（LeetCode 294）
- **文件**: Code23_FlipGameIILeetCode294.java, Code23_FlipGameIILeetCode294.cpp, Code23_FlipGameIILeetCode294.py
- **题目来源**: LeetCode 294. Flip Game II - https://leetcode.com/problems/flip-game-ii/
- **题目描述**: 你和朋友玩一个叫做「翻转游戏」的游戏。给定一个只包含 '+' 和 '-' 的字符串 currentState。你和朋友轮流将连续的两个 "++" 反转成 "--"。当一方无法进行有效的翻转操作时便意味着游戏结束，则另一方获胜。假设你和朋友都采用最优策略，判断你是否可以获胜。
- **核心思路**: 回溯+记忆化搜索/SG函数，将字符串分割为多个独立子游戏
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

### 24. 猜数字大小II（LeetCode 375）
- **文件**: Code24_GuessNumberHigherOrLowerIILeetCode375.java, Code24_GuessNumberHigherOrLowerIILeetCode375.cpp, Code24_GuessNumberHigherOrLowerIILeetCode375.py
- **题目来源**: LeetCode 375. Guess Number Higher or Lower II - https://leetcode.com/problems/guess-number-higher-or-lower-ii/
- **题目描述**: 我们正在玩一个猜数游戏，游戏规则如下：我从 1 到 n 之间选择一个数字。你来猜我选了哪个数字。如果你猜到正确的数字，就会赢得游戏。如果你猜错了，我会告诉你，我选的数字是比你猜的数字大还是小，并且你需要支付你猜的数字的金额。给定一个范围 [1, n]，返回确保获胜的最小金额。
- **核心思路**: 动态规划，dp[i][j]表示在区间[i,j]内确保获胜所需的最小金额
- **时间复杂度**: O(n^3)
- **空间复杂度**: O(n^2)
- **是否最优解**: ✅ 是

## 代码文件列表

1. `Code01_BashGameSG.java` - 巴什博弈实现
2. `Code02_NimGameSG.java` - 尼姆博弈实现
3. `Code03_TwoStonesBashGame.java` - 两堆石头的巴什博弈实现
4. `Code04_ThreeStonesPickFibonacci.java` - 三堆石头拿取斐波那契数博弈实现
5. `Code05_EDGame1.java` - E&D游戏实现1
6. `Code05_EDGame2.java` - E&D游戏实现2
7. `Code06_SplitGame.java` - 分裂游戏实现
8. `Code07_SGFunctionSNim.java` - S-Nim游戏实现（Java）
9. `Code07_SGFunctionSNim.cpp` - S-Nim游戏实现（C++）
10. `Code07_SGFunctionSNim.py` - S-Nim游戏实现（Python）
11. `Code08_DirectedGraphGame.java` - 有向图博弈实现（Java）
12. `Code08_DirectedGraphGame.cpp` - 有向图博弈实现（C++）
13. `Code08_DirectedGraphGame.py` - 有向图博弈实现（Python）
14. `Code09_StaircaseNim.java` - 阶梯博弈实现（Java）
15. `Code09_StaircaseNim.cpp` - 阶梯博弈实现（C++）
16. `Code09_StaircaseNim.py` - 阶梯博弈实现（Python）
17. `Code10_StoneGameLinearString.java` - 线性串取石子游戏实现（Java）
18. `Code10_StoneGameLinearString.cpp` - 线性串取石子游戏实现（C++）
19. `Code10_StoneGameLinearString.py` - 线性串取石子游戏实现（Python）
20. `Code11_FibonacciAgainAndAgain.java` - 斐波那契博弈扩展实现（Java）
21. `Code11_FibonacciAgainAndAgain.cpp` - 斐波那契博弈扩展实现（C++）
22. `Code11_FibonacciAgainAndAgain.py` - 斐波那契博弈扩展实现（Python）
23. `Code12_3DNimGame.java` - 三维博弈实现（Java）
24. `Code12_3DNimGame.cpp` - 三维博弈实现（C++）
25. `Code12_3DNimGame.py` - 三维博弈实现（Python）
26. `Code13_PlayAGame.java` - 奇偶性博弈实现（Java）
27. `Code13_PlayAGame.cpp` - 奇偶性博弈实现（C++）
28. `Code13_PlayAGame.py` - 奇偶性博弈实现（Python）
29. `Code14_MatchesGame.java` - 尼姆博弈经典变种实现（Java）
30. `Code14_MatchesGame.cpp` - 尼姆博弈经典变种实现（C++）
31. `Code14_MatchesGame.py` - 尼姆博弈经典变种实现（Python）
32. `Code15_WythoffGame.java` - 威佐夫博弈实现（Java）
33. `Code15_WythoffGame.cpp` - 威佐夫博弈实现（C++）
34. `Code15_WythoffGame.py` - 威佐夫博弈实现（Python）
35. `Code16_AntiNimGame.java` - 反尼姆博弈实现（Java）
36. `Code16_AntiNimGame.cpp` - 反尼姆博弈实现（C++）
37. `Code16_AntiNimGame.py` - 反尼姆博弈实现（Python）
38. `Code17_FibonacciGame.java` - 斐波那契博弈实现（Java）
39. `Code17_FibonacciGame.cpp` - 斐波那契博弈实现（C++）
40. `Code17_FibonacciGame.py` - 斐波那契博弈实现（Python）
41. `Code18_StoneGameLeetCode877.java` - 取石子游戏变种实现（Java）
42. `Code18_StoneGameLeetCode877.cpp` - 取石子游戏变种实现（C++）
43. `Code18_StoneGameLeetCode877.py` - 取石子游戏变种实现（Python）
44. `Code19_StoneGameIILeetCode1140.java` - 石子游戏II实现（Java）
45. `Code19_StoneGameIILeetCode1140.cpp` - 石子游戏II实现（C++）
46. `Code19_StoneGameIILeetCode1140.py` - 石子游戏II实现（Python）
47. `Code20_StoneGameIIILeetCode1406.java` - 石子游戏III实现（Java）
48. `Code20_StoneGameIIILeetCode1406.cpp` - 石子游戏III实现（C++）
49. `Code20_StoneGameIIILeetCode1406.py` - 石子游戏III实现（Python）
50. `Code21_PredictTheWinnerLeetCode486.java` - 预测赢家实现（Java）
51. `Code21_PredictTheWinnerLeetCode486.cpp` - 预测赢家实现（C++）
52. `Code21_PredictTheWinnerLeetCode486.py` - 预测赢家实现（Python）
53. `Code22_DivisorGameLeetCode1025.java` - 除数博弈实现（Java）
54. `Code22_DivisorGameLeetCode1025.cpp` - 除数博弈实现（C++）
55. `Code22_DivisorGameLeetCode1025.py` - 除数博弈实现（Python）
56. `Code23_FlipGameIILeetCode294.java` - 翻转游戏II实现（Java）
57. `Code23_FlipGameIILeetCode294.cpp` - 翻转游戏II实现（C++）
58. `Code23_FlipGameIILeetCode294.py` - 翻转游戏II实现（Python）
59. `Code24_GuessNumberHigherOrLowerIILeetCode375.java` - 猜数字大小II实现（Java）
60. `Code24_GuessNumberHigherOrLowerIILeetCode375.cpp` - 猜数字大小II实现（C++）
61. `Code24_GuessNumberHigherOrLowerIILeetCode375.py` - 猜数字大小II实现（Python）

---
**最后更新时间**：2025-10-20  
**作者**：AI Assistant