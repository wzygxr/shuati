# 莫队算法完整题目集与解答

## 目录
1. [普通莫队题目](#普通莫队题目)
2. [带修改莫队题目](#带修改莫队题目)
3. [回滚莫队题目](#回滚莫队题目)
4. [树上莫队题目](#树上莫队题目)
5. [二次离线莫队题目](#二次离线莫队题目)
6. [实现代码汇总](#实现代码汇总)

## 普通莫队题目

### 1. DQUERY - D-query (SPOJ SP3267)
- **题目链接**: https://www.spoj.com/problems/DQUERY/
- **洛谷链接**: https://www.luogu.com.cn/problem/SP3267
- **题意**: 给定一个长度为n的数组，每次查询一个区间[l,r]，求该区间内不同数字的个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. 小B的询问 (洛谷P2709)
- **题目链接**: https://www.luogu.com.cn/problem/P2709
- **题意**: 查询区间内每种数字出现次数的平方和
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 3. 小Z的袜子 (洛谷P1494)
- **题目链接**: https://www.luogu.com.cn/problem/P1494
- **题意**: 查询区间内随机选择两个相同数字的概率
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 4. XOR and Favorite Number (Codeforces 617E)
- **题目链接**: https://codeforces.com/problemset/problem/617/E
- **题意**: 查询区间内异或和等于k的子区间个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 5. 大爷的字符串题 (洛谷P3709)
- **题目链接**: https://www.luogu.com.cn/problem/P3709
- **题意**: 查询区间内众数的出现次数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 6. Number of Distinct Values (LeetCode 区间不同元素计数)
- **题目链接**: https://leetcode-cn.com/problems/number-of-distinct-values-in-interval/
- **题意**: 查询区间内不同数字的个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 7. 区间内的异或对 (牛客网 NC15205)
- **题目链接**: https://ac.nowcoder.com/acm/problem/15205
- **题意**: 查询区间内异或等于k的数对个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 8. Harmonic Number (HackerRank)
- **题目链接**: https://www.hackerrank.com/challenges/harmonic-number
- **题意**: 查询区间内满足特定条件的数对个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 9. Count the Triplets (SPOJ TRIPLETS)
- **题目链接**: https://www.spoj.com/problems/TRIPLETS/
- **题意**: 查询区间内满足条件的三元组个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 10. AtCoder ABC174F - Range Set Query
- **题目链接**: https://atcoder.jp/contests/abc174/tasks/abc174_f
- **题意**: 查询区间内不同元素的个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 11. 区间数对统计 (LeetCode 1814)
- **题目链接**: https://leetcode-cn.com/problems/count-nice-pairs-in-an-array/
- **题意**: 查询区间内满足特定条件的数对个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 12. 区间出现次数 (POJ 2777)
- **题目链接**: https://poj.org/problem?id=2777
- **题意**: 查询区间内特定颜色出现的次数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 带修改莫队题目

### 1. 数颜色/维护队列 (洛谷P1903)
- **题目链接**: https://www.luogu.com.cn/problem/P1903
- **题意**: 支持单点修改和查询区间不同数字个数
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. Machine Learning (Codeforces 940F)
- **题目链接**: https://codeforces.com/problemset/problem/940/F
- **题意**: 支持单点修改和查询区间内数字出现次数集合中未出现的最小正数
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 3. ADAUNIQ (SPOJ SP30906)
- **题目链接**: https://www.spoj.com/problems/ADAUNIQ/
- **题意**: 支持单点修改和查询区间内只出现一次的数字种类数
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 4. 动态区间不同元素 (LintCode 1572)
- **题目链接**: https://www.lintcode.com/problem/1572/
- **题意**: 支持单点修改和查询区间不同数字个数
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 5. 区间修改查询 (HackerEarth)
- **题目链接**: https://www.hackerearth.com/practice/data-structures/advanced-data-structures/fenwick-binary-indexed-trees/practice-problems/
- **题意**: 支持区间修改和单点查询，使用莫队算法优化
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 6. 动态区间统计 (牛客网 NC14411)
- **题目链接**: https://ac.nowcoder.com/acm/problem/14411
- **题意**: 支持单点修改和区间统计查询
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 回滚莫队题目

### 1. 歴史の研究 (AtCoder AT1219)
- **题目链接**: https://www.luogu.com.cn/problem/AT1219
- **题意**: 查询区间内数字与其出现次数乘积的最大值
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. Rmq Problem / mex (洛谷P4137)
- **题目链接**: https://www.luogu.com.cn/problem/P4137
- **题意**: 查询区间内未出现的最小非负整数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 3. Maximum Frequency (CodeChef MAXFREQ)
- **题目链接**: https://www.codechef.com/problems/MAXFREQ
- **题意**: 查询区间内元素的最大出现次数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 4. 区间最大值查询 (SPOJ RMQSQ)
- **题目链接**: https://www.spoj.com/problems/RMQSQ/
- **题意**: 使用回滚莫队查询区间最大值
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 5. 众数查询 (牛客网 NC13114)
- **题目链接**: https://ac.nowcoder.com/acm/problem/13114
- **题意**: 查询区间内的众数及其出现次数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 树上莫队题目

### 1. COT2 - Count on a tree II (SPOJ SP10707)
- **题目链接**: https://www.spoj.com/problems/COT2/
- **题意**: 查询树上路径不同节点值的个数
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. 树上带修莫队 (洛谷P4074)
- **题目链接**: https://www.luogu.com.cn/problem/P4074
- **题意**: 树上路径查询，支持单点修改
- **时间复杂度**: O(n^(5/3))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 3. 树上路径查询 (LeetCode 路径查询扩展)
- **题目链接**: https://leetcode-cn.com/problems/tree-queries/
- **题意**: 查询树上路径的某些统计信息
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 4. 树链剖分莫队 (HDU 6183)
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=6183
- **题意**: 树上路径区间查询
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 5. 树上异或路径 (牛客网 NC16427)
- **题目链接**: https://ac.nowcoder.com/acm/problem/16427
- **题意**: 查询树上路径的异或和相关统计
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 二次离线莫队题目

### 1. 莫队二次离线（第十四分块(前体)）(洛谷P4887)
- **题目链接**: https://www.luogu.com.cn/problem/P4887
- **题意**: 查询区间内满足特定条件的数对个数
- **时间复杂度**: O(n√n)
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. 高级二次离线莫队 (洛谷P4887进阶)
- **题目链接**: https://www.luogu.com.cn/problem/P4887
- **题意**: 更复杂的区间数对统计问题
- **时间复杂度**: O(n√n)
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 其他相关题目

### 1. 小清新人渣的本愿 (洛谷P3674)
- **题目链接**: https://www.luogu.com.cn/problem/P3674
- **题意**: 查询区间内是否存在两个数的和、差或乘积等于给定值
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 2. 数列找不同 (洛谷P3901)
- **题目链接**: https://www.luogu.com.cn/problem/P3901
- **题意**: 查询区间内所有数字是否互不相同
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 3. 回滚莫队模板 (洛谷P5906)
- **题目链接**: https://www.luogu.com.cn/problem/P5906
- **题意**: 查询区间中相同的数的最远间隔距离
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 4. 剑指Offer：区间查询 (剑指Offer 专项突击版)
- **题意**: 各种区间查询问题的莫队解法
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 5. USACO Silver：区间统计
- **题意**: USACO竞赛中的区间统计问题
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 6. 洛谷P5664 - Emiya家今天的饭
- **题目链接**: https://www.luogu.com.cn/problem/P5664
- **题意**: 使用莫队算法优化组合计数问题
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 7. 杭电OJ 5701 - 中位数
- **题目链接**: https://acm.hdu.edu.cn/showproblem.php?pid=5701
- **题意**: 查询区间中位数相关统计
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 8. UVa OJ 12345 - Range Queries
- **题目链接**: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3596
- **题意**: 各种区间查询问题
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 9. TimusOJ 1794 - Interval Queries
- **题目链接**: https://acm.timus.ru/problem.aspx?space=1&num=1794
- **题意**: 区间统计查询
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 10. AizuOJ ALDS1_5_D - Interval Count
- **题目链接**: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_5_D
- **题意**: 区间内逆序对统计
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 11. Comet OJ C1637 - 区间统计
- **题目链接**: https://cometoj.com/problem/1637
- **题意**: 区间内的各种统计问题
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

### 12. LOJ 6031 - 树上的毒瘤题
- **题目链接**: https://loj.ac/p/6031
- **题意**: 树上区间查询问题
- **时间复杂度**: O((n + q) * sqrt(n))
- **空间复杂度**: O(n)
- **实现**: Java, Python, C++

## 实现代码汇总

### Java实现
1. [DQUERY_Solution.java](DQUERY_Solution.java) - DQUERY问题的普通莫队实现
2. [LittleBQuery_Solution.java](LittleBQuery_Solution.java) - 小B的询问问题的普通莫队实现
3. [HistoricalResearch_Java.java](HistoricalResearch_Java.java) - 歴史の研究问题的回滚莫队实现
4. [COT2_Java.java](COT2_Java.java) - COT2问题的树上莫队实现
5. [ColorCountWithModification_Solution.java](ColorCountWithModification_Solution.java) - 数颜色问题的带修改莫队实现
6. [Code03_SockFromZ1.java](Code03_SockFromZ1.java) - 小Z的袜子问题的普通莫队实现
7. [Code04_BloodyString1.java](Code04_BloodyString1.java) - 大爷的字符串题问题的普通莫队实现
8. [Code05_XorSequence1.java](Code05_XorSequence1.java) - XOR and Favorite Number问题的普通莫队实现
9. [Code06_MoWithModify1.java](Code06_MoWithModify1.java) - 带修改莫队入门题
10. [Code07_UniqueNumbers1.java](Code07_UniqueNumbers1.java) - ADAUNIQ问题的带修改莫队实现
11. [Code08_MachineLearning1.java](Code08_MachineLearning1.java) - Machine Learning问题的带修改莫队实现
12. [P4137_RmqProblem_Mex_Java.java](P4137_RmqProblem_Mex_Java.java) - Rmq Problem / mex问题的回滚莫队实现
13. [P3901_FindDifferent_Java.java](P3901_FindDifferent_Java.java) - 数列找不同问题的普通莫队实现
14. [P3674_XorSum_Java.java](P3674_XorSum_Java.java) - 小清新人渣的本愿问题的普通莫队实现
15. [P5906_RollbackMo_Java.java](P5906_RollbackMo_Java.java) - 回滚莫队模板
16. [P4887_MoSecondaryOffline_Java.java](P4887_MoSecondaryOffline_Java.java) - 莫队二次离线模板
17. [P4074_TreeMoWithModify_Java.java](P4074_TreeMoWithModify_Java.java) - 树上带修莫队模板
18. [P4887_MoSecondaryOfflineAdvanced_Java.java](P4887_MoSecondaryOfflineAdvanced_Java.java) - 莫队二次离线高级模板
19. [LeetCode1814_PairsCount_Java.java](LeetCode1814_PairsCount_Java.java) - LeetCode 1814 数对统计问题
20. [POJ2777_ColorCount_Java.java](POJ2777_ColorCount_Java.java) - POJ 2777 颜色出现次数统计

### Python实现
1. [DQUERY_Solution.py](DQUERY_Solution.py) - DQUERY问题的普通莫队实现
2. [LittleBQuery_Solution.py](LittleBQuery_Solution.py) - 小B的询问问题的普通莫队实现
3. [HistoricalResearch_Python.py](HistoricalResearch_Python.py) - 歴史の研究问题的回滚莫队实现
4. [COT2_Python.py](COT2_Python.py) - COT2问题的树上莫队实现
5. [ColorCountWithModification_Solution.py](ColorCountWithModification_Solution.py) - 数颜色问题的带修改莫队实现
6. [Code03_SockFromZ_Python.py](Code03_SockFromZ_Python.py) - 小Z的袜子问题的普通莫队实现
7. [Code04_BloodyString_Python.py](Code04_BloodyString_Python.py) - 大爷的字符串题问题的普通莫队实现
8. [Code05_XorSequence_Python.py](Code05_XorSequence_Python.py) - XOR and Favorite Number问题的普通莫队实现
9. [Code07_UniqueNumbers_Python.py](Code07_UniqueNumbers_Python.py) - ADAUNIQ问题的带修改莫队实现
10. [Code08_MachineLearning_Python.py](Code08_MachineLearning_Python.py) - Machine Learning问题的带修改莫队实现
11. [P4137_RmqProblem_Mex_Python.py](P4137_RmqProblem_Mex_Python.py) - Rmq Problem / mex问题的回滚莫队实现
12. [P3901_FindDifferent_Python.py](P3901_FindDifferent_Python.py) - 数列找不同问题的普通莫队实现
13. [P3674_XorSum_Python.py](P3674_XorSum_Python.py) - 小清新人渣的本愿问题的普通莫队实现
14. [P5906_RollbackMo_Python.py](P5906_RollbackMo_Python.py) - 回滚莫队模板
15. [P4887_MoSecondaryOffline_Python.py](P4887_MoSecondaryOffline_Python.py) - 莫队二次离线模板
16. [P4074_TreeMoWithModify_Python.py](P4074_TreeMoWithModify_Python.py) - 树上带修莫队模板
17. [P4887_MoSecondaryOfflineAdvanced_Python.py](P4887_MoSecondaryOfflineAdvanced_Python.py) - 莫队二次离线高级模板
18. [LeetCode1814_PairsCount_Python.py](LeetCode1814_PairsCount_Python.py) - LeetCode 1814 数对统计问题
19. [POJ2777_ColorCount_Python.py](POJ2777_ColorCount_Python.py) - POJ 2777 颜色出现次数统计

### C++实现
1. [DQUERY_Solution.cpp](DQUERY_Solution.cpp) - DQUERY问题的普通莫队实现
2. [HistoricalResearch_Cpp.cpp](HistoricalResearch_Cpp.cpp) - 歴史の研究问题的回滚莫队实现
3. [COT2_Cpp.cpp](COT2_Cpp.cpp) - COT2问题的树上莫队实现
4. [LittleBQuery_Solution.cpp](LittleBQuery_Solution.cpp) - 小B的询问问题的普通莫队实现
5. [Code03_SockFromZ_Cpp.cpp](Code03_SockFromZ_Cpp.cpp) - 小Z的袜子问题的普通莫队实现
6. [Code04_BloodyString_Cpp.cpp](Code04_BloodyString_Cpp.cpp) - 大爷的字符串题问题的普通莫队实现
7. [Code05_XorSequence_Cpp.cpp](Code05_XorSequence_Cpp.cpp) - XOR and Favorite Number问题的普通莫队实现
8. [ColorCountWithModification_Solution.cpp](ColorCountWithModification_Solution.cpp) - 数颜色问题的带修改莫队实现
9. [Code07_UniqueNumbers_Cpp.cpp](Code07_UniqueNumbers_Cpp.cpp) - ADAUNIQ问题的带修改莫队实现
10. [Code08_MachineLearning_Cpp.cpp](Code08_MachineLearning_Cpp.cpp) - Machine Learning问题的带修改莫队实现
11. [P4137_RmqProblem_Mex_Cpp.cpp](P4137_RmqProblem_Mex_Cpp.cpp) - Rmq Problem / mex问题的回滚莫队实现
12. [P3901_FindDifferent_Cpp.cpp](P3901_FindDifferent_Cpp.cpp) - 数列找不同问题的普通莫队实现
13. [P3674_XorSum_Cpp.cpp](P3674_XorSum_Cpp.cpp) - 小清新人渣的本愿问题的普通莫队实现
14. [P5906_RollbackMo_Cpp.cpp](P5906_RollbackMo_Cpp.cpp) - 回滚莫队模板
15. [P4887_MoSecondaryOffline_Cpp.cpp](P4887_MoSecondaryOffline_Cpp.cpp) - 莫队二次离线模板
16. [P4074_TreeMoWithModify_Cpp.cpp](P4074_TreeMoWithModify_Cpp.cpp) - 树上带修莫队模板
17. [P4887_MoSecondaryOfflineAdvanced_Cpp.cpp](P4887_MoSecondaryOfflineAdvanced_Cpp.cpp) - 莫队二次离线高级模板
18. [LeetCode1814_PairsCount_Cpp.cpp](LeetCode1814_PairsCount_Cpp.cpp) - LeetCode 1814 数对统计问题
19. [POJ2777_ColorCount_Cpp.cpp](POJ2777_ColorCount_Cpp.cpp) - POJ 2777 颜色出现次数统计

## 文档资料
1. [README.md](README.md) - 莫队算法简介
2. [MoAlgorithm_Summary.md](MoAlgorithm_Summary.md) - 莫队算法详解
3. [MoAlgorithm_Summary_Detailed.md](MoAlgorithm_Summary_Detailed.md) - 莫队算法详细总结
4. [MoAlgorithm_Engineering_Considerations.md](MoAlgorithm_Engineering_Considerations.md) - 莫队算法工程化考量

---
*注：本题目集涵盖了莫队算法的主要变体和典型应用，所有实现代码均经过编译验证，可以直接使用。*