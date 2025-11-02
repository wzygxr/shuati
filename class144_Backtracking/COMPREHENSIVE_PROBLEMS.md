# 递归与回溯算法 - 综合题目集锦

## 子序列相关问题

### 1. 字符串的所有子序列 (已实现)
- **题目**: 生成字符串的所有子序列（去重）
- **平台**: NowCoder
- **链接**: https://www.nowcoder.com/practice/92e6247998294f2c933906fdedbc6e6a

### 2. LeetCode 78. 子集
- **题目**: 给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/subsets/
- **解法**: 回溯算法，每个元素选择或不选择

### 3. LeetCode 90. 子集 II (新增)
- **题目**: 给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/subsets-ii/
- **解法**: 回溯算法，需要去重

### 4. LeetCode 115. 不同的子序列
- **题目**: 给定一个字符串 s 和一个字符串 t，计算在 s 的子序列中 t 出现的个数
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/distinct-subsequences/
- **解法**: 动态规划或递归+记忆化

## 组合相关问题

### 1. 数组组合去重 (已实现)
- **题目**: 给你一个整数数组 nums ，其中可能包含重复元素，请你返回该数组所有可能的组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/subsets-ii/

### 2. LeetCode 77. 组合 (新增)
- **题目**: 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/combinations/
- **解法**: 回溯算法

### 3. LeetCode 39. 组合总和 (已实现)
- **题目**: 给定一个无重复元素的数组 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/combination-sum/
- **解法**: 回溯算法，元素可重复使用

### 4. LeetCode 40. 组合总和 II (新增)
- **题目**: 给定一个数组 candidates 和一个目标数 target，找出 candidates 中所有可以使数字和为 target 的组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/combination-sum-ii/
- **解法**: 回溯算法，元素不可重复使用

### 5. LeetCode 216. 组合总和 III (新增)
- **题目**: 找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，且每种组合中不存在重复的数字
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/combination-sum-iii/
- **解法**: 回溯算法

## 排列相关问题

### 1. 无重复数字全排列 (已实现)
- **题目**: 没有重复项数字的全排列
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/permutations/

### 2. 有重复数字全排列 (已实现)
- **题目**: 有重复项数组的去重全排列
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/permutations-ii/

### 3. LeetCode 46. 全排列 (已实现)
- **题目**: 给定一个没有重复数字的序列，返回其所有可能的全排列
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/permutations/
- **解法**: 回溯算法

### 4. LeetCode 47. 全排列 II (新增)
- **题目**: 给定一个可包含重复数字的序列，返回所有不重复的全排列
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/permutations-ii/
- **解法**: 回溯算法，需要去重

### 5. LeetCode 60. 排列序列 (新增)
- **题目**: 给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。按大小顺序列出所有排列情况
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/permutation-sequence/
- **解法**: 数学方法，康托展开

## 栈操作相关问题

### 1. 递归逆序栈 (已实现)
- **题目**: 用递归函数逆序栈
- **平台**: 自定义
- **解法**: 递归

### 2. 递归排序栈 (已实现)
- **题目**: 用递归函数排序栈
- **平台**: 自定义
- **解法**: 递归

### 3. LeetCode 155. 最小栈
- **题目**: 设计一个支持 push，pop，top 操作，并能在常数时间内检索到最小元素的栈
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/min-stack/
- **解法**: 辅助栈

### 4. LeetCode 232. 用栈实现队列
- **题目**: 使用栈实现队列的下列操作：push、pop、peek、empty
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/implement-queue-using-stacks/
- **解法**: 双栈

### 5. LeetCode 225. 用队列实现栈
- **题目**: 使用队列实现栈的下列操作：push、pop、top、empty
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/implement-stack-using-queues/
- **解法**: 单队列或双队列

## 汉诺塔问题

### 1. 汉诺塔移动 (已实现)
- **题目**: 打印n层汉诺塔问题的最优移动轨迹
- **平台**: 自定义
- **解法**: 递归

### 2. LeetCode 面试题 08.06. 汉诺塔问题
- **题目**: 在经典汉诺塔问题中，有 3 根柱子及 N 个不同大小的穿孔圆盘，盘子可以滑入任意一根柱子。请编写程序，用栈将所有盘子从第一根柱子移到最后一根柱子
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/hanota-lcci/
- **解法**: 递归

## 其他递归与回溯经典问题

### 1. LeetCode 17. 电话号码的字母组合 (已实现)
- **题目**: 给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/letter-combinations-of-a-phone-number/
- **解法**: 回溯算法

### 2. LeetCode 22. 括号生成 (已实现)
- **题目**: 给出 n 代表生成括号的对数，生成所有可能的并且有效的括号组合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/generate-parentheses/
- **解法**: 回溯算法

### 3. LeetCode 37. 解数独 (已实现)
- **题目**: 编写一个程序，通过已填充的空格来解决数独问题
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/sudoku-solver/
- **解法**: 回溯算法

### 4. LeetCode 51. N 皇后 (已实现)
- **题目**: n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/n-queens/
- **解法**: 回溯算法

### 5. LeetCode 93. 复原 IP 地址 (新增)
- **题目**: 有效 IP 地址 正好由四个整数（每个整数位于 0 到 255 之间组成，且不能含有前导 0），整数之间用 '.' 分隔
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/restore-ip-addresses/
- **解法**: 回溯算法

### 6. LeetCode 131. 分割回文串 (已实现)
- **题目**: 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/palindrome-partitioning/
- **解法**: 回溯算法

### 7. LeetCode 140. 单词拆分 II (新增)
- **题目**: 给定一个字符串 s 和一个字符串字典 wordDict，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在词典中
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/word-break-ii/
- **解法**: 回溯算法 + 记忆化搜索

### 8. LeetCode 212. 单词搜索 II (已实现)
- **题目**: 给定一个二维网格 board 和一个字典中的单词列表 words，找出所有同时在二维网格和字典中出现的单词
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/word-search-ii/
- **解法**: 回溯算法 + Trie树

### 9. LeetCode 306. 累加数 (新增)
- **题目**: 累加数是一个字符串，组成它的数字可以形成累加序列。一个有效的累加序列必须至少包含 3 个数
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/additive-number/
- **解法**: 回溯算法

### 10. LeetCode 401. 二进制手表 (新增)
- **题目**: 二进制手表顶部有 4 个 LED 代表小时（0-11），底部的 6 个 LED 代表分钟（0-59）。给你一个整数 turnedOn，表示当前亮着的 LED 的数量，返回二进制手表可以表示的所有可能时间
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/binary-watch/
- **解法**: 回溯算法

### 11. LeetCode 473. 火柴拼正方形 (新增)
- **题目**: 你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。你要用所有的火柴棍拼成一个正方形
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/matchsticks-to-square/
- **解法**: 回溯算法

### 12. LeetCode 494. 目标和 (已实现)
- **题目**: 给定一个非负整数数组，a1, a2, ..., an, 和一个目标数，S。现在你有两个符号 + 和 -。对于数组中的每个数字，你都可以选择一个符号
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/target-sum/
- **解法**: 回溯算法或动态规划

### 13. LeetCode 526. 优美的排列 (新增)
- **题目**: 假设有从 1 到 n 的 n 个整数。用这些整数构造一个数组 perm（下标从 1 开始），只要满足下述条件之一，该数组就是一个优美的排列
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/beautiful-arrangement/
- **解法**: 回溯算法

### 14. LeetCode 698. 划分为k个相等的子集 (新增)
- **题目**: 给定一个整数数组 nums 和一个正整数 k，找出是否有可能把数组分成 k 个非空子集，其总和都相等
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/partition-to-k-equal-sum-subsets/
- **解法**: 回溯算法

### 15. LeetCode 784. 字母大小写全排列 (新增)
- **题目**: 给定一个字符串S，通过将字符串S中的每个字母转变大小写，我们可以获得一个新的字符串。返回所有可能得到的字符串集合
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/letter-case-permutation/
- **解法**: 回溯算法

### 16. LeetCode 79. 单词搜索 (已实现)
- **题目**: 给定一个二维网格和一个单词，找出该单词是否存在于网格中
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/word-search/
- **解法**: 回溯算法

### 17. LeetCode 996. 正方形数组的数目 (新增)
- **题目**: 给定一个非负整数数组 A，如果该数组任意两个相邻元素的和都可以表示为某个完全平方数，那么这个数组就称为正方形数组。返回 A 的所有可能的排列中，正方形数组的数目
- **平台**: LeetCode
- **链接**: https://leetcode.cn/problems/number-of-squareful-arrays/
- **解法**: 回溯算法 + 去重

### 18. POJ 1011 Sticks (新增)
- **题目**: 给定n根火柴棍，每根火柴棍都有一定的长度。要求将这些火柴棍拼成若干根长度相等的火柴棍，且每根新火柴棍的长度要尽可能大
- **平台**: POJ
- **链接**: http://poj.org/problem?id=1011
- **解法**: 回溯算法 + 剪枝优化

## 新增题目详细说明

### Code18_SubsetsII - 子集 II (LeetCode 90)
- **问题类型**: 子集生成 + 去重
- **关键技巧**: 排序后跳过重复元素
- **时间复杂度**: O(n * 2^n)
- **空间复杂度**: O(n)

### Code19_Combinations - 组合 (LeetCode 77)
- **问题类型**: 组合生成
- **关键技巧**: 控制起始位置避免重复
- **时间复杂度**: O(C(n, k) * k)
- **空间复杂度**: O(k)

### Code20_PermutationsII - 全排列 II (LeetCode 47)
- **问题类型**: 排列生成 + 去重
- **关键技巧**: 排序后确保相同元素的相对顺序
- **时间复杂度**: O(n * n!)
- **空间复杂度**: O(n)

### Code21_CombinationSumII - 组合总和 II (LeetCode 40)
- **问题类型**: 组合求和 + 去重
- **关键技巧**: 排序后跳过重复元素，每个数字只能使用一次
- **时间复杂度**: O(2^n)
- **空间复杂度**: O(n)

### Code22_CombinationSumIII - 组合总和 III (LeetCode 216)
- **问题类型**: 组合求和
- **关键技巧**: 数字范围限制在1-9，不能重复使用
- **时间复杂度**: O(C(9, k))
- **空间复杂度**: O(k)

### Code23_PermutationSequence - 排列序列 (LeetCode 60)
- **问题类型**: 数学排列
- **关键技巧**: 康托展开，直接计算第k个排列
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(n)

### Code24_RestoreIPAddresses - 复原 IP 地址 (LeetCode 93)
- **问题类型**: 字符串分割
- **关键技巧**: 回溯分割，检查IP地址段合法性
- **时间复杂度**: O(3^4) = O(81)
- **空间复杂度**: O(n)

### Code25_WordBreakII - 单词拆分 II (LeetCode 140)
- **问题类型**: 字符串分割 + 字典匹配
- **关键技巧**: 回溯 + 记忆化搜索优化
- **时间复杂度**: O(2^n * n)
- **空间复杂度**: O(n^2)

### Code26_BeautifulArrangement - 优美的排列 (LeetCode 526)
- **问题类型**: 排列生成 + 条件约束
- **关键技巧**: 提前剪枝，只有满足条件的数字才被选择
- **时间复杂度**: O(n!)
- **空间复杂度**: O(n)

### Code27_MatchsticksToSquare - 火柴拼正方形 (LeetCode 473)
- **问题类型**: 分区问题
- **关键技巧**: 回溯分配火柴棒到四条边，剪枝优化
- **时间复杂度**: O(4^n)
- **空间复杂度**: O(n)

### Code28_PartitionToKEqualSumSubsets - 划分为k个相等的子集 (LeetCode 698)
- **问题类型**: 分区问题
- **关键技巧**: 回溯分配元素到k个子集，剪枝优化
- **时间复杂度**: O(k^n)
- **空间复杂度**: O(n)

### Code29_AdditiveNumber - 累加数 (LeetCode 306)
- **问题类型**: 字符串验证
- **关键技巧**: 回溯验证累加关系，处理大数问题
- **时间复杂度**: O(n^3)
- **空间复杂度**: O(n)

### Code30_BinaryWatch - 二进制手表 (LeetCode 401)
- **问题类型**: 枚举 + 回溯
- **关键技巧**: 使用回溯算法枚举所有可能的LED点亮组合
- **时间复杂度**: O(2^10) = O(1024)
- **空间复杂度**: O(1)

### Code31_LetterCasePermutation - 字母大小写全排列 (LeetCode 784)
- **问题类型**: 字符串变换
- **关键技巧**: 对每个字母字符尝试大小写两种情况
- **时间复杂度**: O(2^n * n)
- **空间复杂度**: O(2^n * n)

### Code32_NumSquarefulPerms - 正方形数组的数目 (LeetCode 996)
- **问题类型**: 排列生成 + 条件验证
- **关键技巧**: 生成所有排列并在过程中验证相邻元素和是否为完全平方数
- **时间复杂度**: O(n! * n)
- **空间复杂度**: O(n)

### Code33_Sticks - 火柴拼接 (POJ 1011)
- **问题类型**: 分区问题
- **关键技巧**: 从大到小尝试可能的长度，使用回溯分配火柴棍
- **时间复杂度**: O(2^n * n)
- **空间复杂度**: O(n)

### Code34_GenerateParenthesesII - 括号生成增强版 (LeetCode 22)
- **问题类型**: 括号生成 + 连续性计算
- **关键技巧**: 在生成括号的同时计算最大连续括号长度
- **时间复杂度**: O(4^n / sqrt(n))
- **空间复杂度**: O(4^n / sqrt(n))

## 算法技巧总结

### 1. 回溯算法通用模板
```java
void backtrack(参数) {
    if (终止条件) {
        存放结果;
        return;
    }
    
    for (选择：本层集合中元素) {
        处理节点;
        backtrack(路径，选择列表); // 递归
        回溯，撤销处理结果
    }
}
```

### 2. 去重技巧
- **排序去重**: 先排序，然后跳过重复元素
- **相对顺序**: 确保相同元素的相对顺序，避免生成重复结果
- **哈希去重**: 使用Set存储已访问的状态

### 3. 剪枝优化
- **提前终止**: 当当前路径不可能得到解时提前返回
- **排序剪枝**: 从大到小排序，便于提前发现不可能的情况
- **状态压缩**: 使用位运算减少空间使用

### 4. 记忆化搜索
- **存储中间结果**: 避免重复计算相同子问题
- **状态表示**: 使用合适的数据结构表示状态

### 5. 工程化考虑
- **异常处理**: 空输入、非法输入检查
- **边界条件**: 极端值、边界值处理
- **性能优化**: 选择合适的算法和数据结构
- **代码可读性**: 清晰的命名和注释

## 适用场景总结

1. **组合优化问题**: 需要找出所有满足条件的组合
2. **排列生成问题**: 需要生成所有可能的排列
3. **分区问题**: 需要将元素分配到多个组中
4. **字符串分割问题**: 需要将字符串分割成多个部分
5. **棋盘问题**: 需要在棋盘上放置棋子
6. **路径搜索问题**: 需要在网格或图中搜索路径

通过掌握这些经典的回溯算法题目和技巧，可以更好地应对各种算法面试和实际开发中的组合优化问题。