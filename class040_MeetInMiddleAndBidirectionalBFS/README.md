# Class063: 折半搜索与双向BFS算法专题

## 目录
- [算法概述](#算法概述)
- [核心题目](#核心题目)
- [补充题目](#补充题目)
- [算法模板](#算法模板)
- [复杂度分析](#复杂度分析)
- [工程化考量](#工程化考量)
- [语言特性差异](#语言特性差异)
- [测试用例](#测试用例)
- [扩展应用](#扩展应用)

## 算法概述

折半搜索（Meet in the Middle）和双向BFS是处理大规模状态空间搜索问题的重要技术。

### 折半搜索（Meet in the Middle）
- **适用场景**：状态空间巨大（如2^n级别），但n较大时直接搜索不可行
- **核心思想**：将问题分为两半，分别搜索后合并结果
- **时间复杂度**：O(2^(n/2)) 替代 O(2^n)
- **空间复杂度**：O(2^(n/2))

### 双向BFS（Bidirectional BFS）
- **适用场景**：已知起点和终点的最短路径问题
- **核心思想**：从起点和终点同时开始搜索，在中间相遇
- **时间复杂度**：O(b^(d/2)) 替代 O(b^d)
- **空间复杂度**：O(b^(d/2))

## 核心题目

### 1. 单词接龙（Word Ladder）
**题目来源**：LeetCode 127  
**题目链接**：https://leetcode.cn/problems/word-ladder/

**问题描述**：
给定两个单词（beginWord 和 endWord）和一个字典 wordList，找到从 beginWord 到 endWord 的最短转换序列的长度。

**算法思路**：
- 使用双向BFS从起点和终点同时搜索
- 每次变换一个字母，检查是否在字典中
- 当两个搜索方向相遇时，路径长度即为答案

**时间复杂度**：O(M × N)，其中M是单词长度，N是字典大小  
**空间复杂度**：O(N)

### 2. 打开转盘锁（Open the Lock）
**题目来源**：LeetCode 752  
**题目链接**：https://leetcode.cn/problems/open-the-lock/

**问题描述**：
有一个带有四个圆形拨轮的转盘锁，每个拨轮有10个数字。锁的初始数字为 '0000'，目标数字为 target。每次旋转只能将一个拨轮的数字向上或向下旋转一位。有些数字是死亡数字，如果锁的数字变成这些数字，锁就会被永久锁定。

**算法思路**：
- 双向BFS搜索最短路径
- 避免死亡数字状态
- 每次旋转一个拨轮的一位数字

**时间复杂度**：O(10^4) = O(10000)  
**空间复杂度**：O(10000)

### 3. 分割等和子集（Partition Equal Subset Sum）
**题目来源**：LeetCode 416  
**题目链接**：https://leetcode.cn/problems/partition-equal-subset-sum/

**问题描述**：
给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。

**算法思路**：
- 折半搜索：将数组分为两半
- 分别计算所有可能的子集和
- 使用哈希表查找满足条件的组合

**时间复杂度**：O(2^(n/2))  
**空间复杂度**：O(2^(n/2))

### 4. 目标和（Target Sum）
**题目来源**：LeetCode 494  
**题目链接**：https://leetcode.cn/problems/target-sum/

**问题描述**：
给定一个非负整数数组和一个目标数，给每个数字添加 '+' 或 '-' 符号，使得表达式的结果等于目标数。

**算法思路**：
- 折半搜索：将数组分为两半
- 分别计算所有可能的表达式结果
- 使用哈希表统计结果出现次数

**时间复杂度**：O(2^(n/2))  
**空间复杂度**：O(2^(n/2))

### 5. 最小基因变化（Minimum Genetic Mutation）
**题目来源**：LeetCode 433  
**题目链接**：https://leetcode.cn/problems/minimum-genetic-mutation/

**问题描述**：
基因序列可以用长度为8的字符串表示，包含 'A', 'C', 'G', 'T'。每次变化只能改变一个字符，且变化后的基因必须在基因库中。

**算法思路**：
- 双向BFS搜索最短变化路径
- 类似单词接龙，但字符集更小

**时间复杂度**：O(4^8 × N)  
**空间复杂度**：O(4^8)

## 补充题目

### 6. 数组的均值分割（Array Mean Split）
**题目来源**：LeetCode 805  
**题目链接**：https://leetcode.cn/problems/split-array-with-same-average/

**问题描述**：
给定一个整数数组 nums，判断是否可以将数组分割成两个非空子集，使得两个子集的平均值相等。

**算法思路**：
- 数学推导：如果两个子集平均值相等，则整个数组的平均值等于每个子集的平均值
- 折半搜索：将数组分为两半，分别计算所有可能的和与元素个数组合
- 组合查找：使用哈希表快速查找满足条件的组合

**时间复杂度**：O(2^(n/2) × n)  
**空间复杂度**：O(2^(n/2))

### 7. Beautiful Quadruples
**题目来源**：HackerRank  
**题目链接**：https://www.hackerrank.com/challenges/beautiful-quadruples/problem

**问题描述**：
给定四个数组A, B, C, D，找到四元组(i, j, k, l)的数量，使得：
1. A[i] XOR B[j] XOR C[k] XOR D[l] = 0
2. i < j < k < l（如果数组有重复元素，索引需要严格递增）

**算法思路**：
- XOR性质利用：A XOR B XOR C XOR D = 0 等价于 A XOR B = C XOR D
- 折半搜索：将四个数组分为两组(A,B)和(C,D)
- 组合统计：分别计算两组的所有XOR值及其出现次数，然后进行匹配

**时间复杂度**：O(n^2)  
**空间复杂度**：O(n^2)

### 8. 15-Puzzle Problem
**题目来源**：UVa 10181  
**题目链接**：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=13&page=show_problem&problem=1122

**问题描述**：
15拼图问题，给定一个4x4的棋盘，包含15个数字和一个空格。目标是通过移动空格，将棋盘恢复到目标状态。

**算法思路**：
- 双向BFS：从初始状态和目标状态同时开始搜索
- 状态压缩：使用字符串或位运算表示棋盘状态
- 启发式搜索：使用曼哈顿距离评估状态优先级

**时间复杂度**：难以精确分析，取决于搜索深度和启发式函数  
**空间复杂度**：O(b^d)，其中b是分支因子，d是深度

### 9. Lights Out Puzzle
**题目来源**：多种OJ平台  
**问题描述**：
Lights Out是一个电子游戏，有一个5x5的网格，每个格子有一个灯（开或关）。点击一个格子会切换该格子及其相邻格子的状态。目标是将所有灯关闭。

**算法思路**：
- 折半搜索：将5x5网格分为两部分
- 状态压缩：使用位运算表示灯的状态
- 高斯消元：利用线性代数性质优化

**时间复杂度**：O(2^(n/2))  
**空间复杂度**：O(2^(n/2))

### 10. Anya and Cubes
**题目来源**：Codeforces 525E  
**题目链接**：https://codeforces.com/problemset/problem/525/E

**问题描述**：
Anya有n个立方体，每个立方体上有一个数字。她可以选择一些立方体，对每个选择的立方体，她可以使用一次魔法将其数字变为阶乘。她想知道有多少种方式选择立方体并使用魔法，使得选择的立方体数字之和等于S。

**算法思路**：
- 折半搜索：将立方体分为两组
- 阶乘预处理：预先计算所有可能的阶乘值
- 组合统计：使用哈希表统计各种和的出现次数

**时间复杂度**：O(2^(n/2) × k)  
**空间复杂度**：O(2^(n/2))

### 11. Partition Array Into Two Arrays to Minimize Sum Difference
**题目来源**：LeetCode  
**题目链接**：https://leetcode.com/problems/partition-array-into-two-arrays-to-minimize-sum-difference/

**问题描述**：
给定一个长度为2*n的整数数组nums，你需要将nums分割成两个长度为n的数组，使得两个数组元素和的绝对差值最小。

**算法思路**：
- 折半搜索：将数组分为两半，分别计算所有可能的子集和
- 双指针技术：通过双指针找到最小差值
- 二分查找：在排序后的数组中查找最接近的值

**时间复杂度**：O(n * 2^n)  
**空间复杂度**：O(2^n)

### 12. ABCDEF
**题目来源**：SPOJ  
**题目链接**：https://www.spoj.com/problems/ABCDEF/

**问题描述**：
给定一个集合S，找出不同的三元组(A,B,C)和(D,E,F)使得 (A+B+C) % S = (D+E+F) % S。

**算法思路**：
- 折半搜索：将等式变形后使用折半搜索
- 哈希表：存储中间结果以便快速查找
- 数学变换：将模运算问题转化为普通等式问题

**时间复杂度**：O(N^3)  
**空间复杂度**：O(N^3)

### 13. 4 Values whose Sum is 0
**题目来源**：SPOJ  
**题目链接**：https://www.spoj.com/problems/SUMFOUR/

**问题描述**：
给定4个数组A, B, C, D，每个数组包含n个整数。找出有多少组(i, j, k, l)使得 A[i] + B[j] + C[k] + D[l] = 0。

**算法思路**：
- 折半搜索：将4个数组分为两组
- 哈希表：存储前两组数组的所有可能和
- 组合匹配：在后两组数组中查找匹配的和

**时间复杂度**：O(N^2)  
**空间复杂度**：O(N^2)

### 14. Celebrity Split
**题目来源**：UVa  
**题目链接**：https://onlinejudge.org/index.php?option=onlinejudge&page=show_problem&problem=1895

**问题描述**：
给定一个正整数集合，将其分割成两个子集，使得两个子集元素和的差值最小。

**算法思路**：
- 折半搜索：将集合分为两半
- 子集和计算：分别计算两半的所有可能子集和
- 最优匹配：通过排序和双指针技术找到最小差值

**时间复杂度**：O(2^(n/2) * log(2^(n/2)))  
**空间复杂度**：O(2^(n/2))

### 15. In Search of Truth (Easy Version)
**题目来源**：Codeforces  
**题目链接**：https://codeforces.com/problemset/problem/1840/G1

**问题描述**：
给定一个函数f(x)，需要找到满足特定条件的x值。

**算法思路**：
- 折半搜索：结合其他算法技术解决问题
- 函数分析：分析函数性质以优化搜索过程
- 状态空间优化：减少搜索空间以提高效率

**时间复杂度**：取决于具体问题  
**空间复杂度**：取决于具体问题

## 算法模板

### 折半搜索通用模板

``java
// Java版本
public class MeetInMiddleTemplate {
    public long solve(int[] nums, int target) {
        int n = nums.length;
        int mid = n / 2;
        
        // 计算左半部分的所有可能组合
        Map<Long, Integer> left = new HashMap<>();
        generateSubsets(nums, 0, mid, 0, 0, left);
        
        // 计算右半部分的所有可能组合
        Map<Long, Integer> right = new HashMap<>();
        generateSubsets(nums, mid, n, 0, 0, right);
        
        long count = 0;
        
        // 合并结果
        for (Map.Entry<Long, Integer> leftEntry : left.entrySet()) {
            long needed = target - leftEntry.getKey();
            if (right.containsKey(needed)) {
                count += (long) leftEntry.getValue() * right.get(needed);
            }
        }
        
        return count;
    }
    
    private void generateSubsets(int[] nums, int start, int end, 
                                long currentSum, int currentCount,
                                Map<Long, Integer> result) {
        if (start == end) {
            result.put(currentSum, result.getOrDefault(currentSum, 0) + 1);
            return;
        }
        
        // 不选当前元素
        generateSubsets(nums, start + 1, end, currentSum, currentCount, result);
        
        // 选当前元素
        generateSubsets(nums, start + 1, end, currentSum + nums[start], 
                       currentCount + 1, result);
    }
}
```

### 双向BFS通用模板

``java
// Java版本
public class BidirectionalBFS {
    public int bidirectionalBFS(String start, String end, Set<String> dictionary) {
        if (!dictionary.contains(end)) return -1;
        
        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        Set<String> visited = new HashSet<>();
        
        beginSet.add(start);
        endSet.add(end);
        visited.add(start);
        visited.add(end);
        
        int level = 1;
        
        while (!beginSet.isEmpty() && !endSet.isEmpty()) {
            // 总是从较小的集合开始扩展
            if (beginSet.size() > endSet.size()) {
                Set<String> temp = beginSet;
                beginSet = endSet;
                endSet = temp;
            }
            
            Set<String> nextLevel = new HashSet<>();
            
            for (String current : beginSet) {
                // 生成所有可能的邻居
                for (String neighbor : getNeighbors(current, dictionary)) {
                    if (endSet.contains(neighbor)) {
                        return level + 1;
                    }
                    
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        nextLevel.add(neighbor);
                    }
                }
            }
            
            beginSet = nextLevel;
            level++;
        }
        
        return -1;
    }
}
```

## 复杂度分析

### 时间复杂度对比

| 算法 | 直接搜索 | 折半搜索/双向BFS | 优化倍数 |
|------|----------|------------------|----------|
| 状态空间2^n | O(2^n) | O(2^(n/2)) | 2^(n/2)倍 |
| 图搜索b^d | O(b^d) | O(b^(d/2)) | b^(d/2)倍 |

### 空间复杂度分析

- **折半搜索**：需要存储O(2^(n/2))个状态
- **双向BFS**：需要存储O(b^(d/2))个状态
- **优化关键**：平衡时间复杂度和空间复杂度

## 工程化考量

### 1. 异常处理
```
// 检查输入合法性
if (nums == null || nums.length == 0) {
    throw new IllegalArgumentException("输入数组不能为空");
}

// 检查边界条件
if (target < 0) {
    return false; // 或抛出异常
}
```

### 2. 性能优化
- **提前剪枝**：在搜索过程中尽早排除不可能的分支
- **状态去重**：避免重复搜索相同状态
- **内存优化**：使用压缩表示减少内存占用

### 3. 可测试性
```
// 单元测试覆盖各种边界情况
@Test
public void testEdgeCases() {
    // 空输入测试
    // 单个元素测试
    // 极值测试
    // 重复元素测试
}
```

### 4. 可维护性
- **模块化设计**：将搜索逻辑与业务逻辑分离
- **清晰注释**：说明算法原理和关键步骤
- **配置化参数**：允许调整搜索参数

## 语言特性差异

### Java vs C++ vs Python

| 特性 | Java | C++ | Python |
|------|------|-----|--------|
| 哈希表 | HashMap | unordered_map | dict |
| 优先级队列 | PriorityQueue | priority_queue | heapq |
| 字符串处理 | String | string | str |
| 位运算 | 支持但较慢 | 高效支持 | 支持 |
| 内存管理 | 自动GC | 手动/智能指针 | 自动GC |

### 性能考虑
- **C++**：适合性能要求高的场景，直接内存操作
- **Java**：平衡性能和开发效率，垃圾回收可控
- **Python**：开发效率高，但运行效率较低

## 测试用例

### 综合测试策略

1. **功能测试**
   - 正常功能验证
   - 边界条件测试
   - 特殊输入测试

2. **性能测试**
   - 小规模数据测试
   - 中等规模数据测试
   - 大规模数据压力测试

3. **正确性验证**
   - 与暴力解法对比
   - 多组随机数据测试
   - 已知答案验证

### 测试用例示例

```
// 单词接龙测试用例
@Test
public void testWordLadder() {
    String[] wordList = {"hot","dot","dog","lot","log","cog"};
    int result = ladderLength("hit", "cog", Arrays.asList(wordList));
    assertEquals(5, result);
}

// 分割等和子集测试用例  
@Test
public void testPartitionEqualSubsetSum() {
    int[] nums = {1, 5, 11, 5};
    boolean result = canPartition(nums);
    assertTrue(result);
}
```

## 扩展应用

### 机器学习中的应用

1. **状态空间搜索**
   - 强化学习中的状态探索
   - 游戏AI的决策树搜索
   - 组合优化问题的求解

2. **特征工程**
   - 大规模特征组合的搜索
   - 特征选择的优化算法

### 大数据处理

1. **分布式搜索**
   - MapReduce实现折半搜索
   - 分布式状态空间探索

2. **流式处理**
   - 实时路径规划
   - 动态状态更新

### 实际工程应用

1. **网络路由**
   - 最短路径计算
   - 负载均衡算法

2. **游戏开发**
   - 游戏AI决策
   - 谜题求解引擎

3. **系统优化**
   - 资源配置优化
   - 任务调度算法

## 总结

折半搜索和双向BFS是处理大规模搜索问题的强大工具。通过合理的问题分解和状态管理，可以显著降低算法的时间复杂度和空间复杂度。在实际应用中，需要根据具体问题特点选择合适的算法变种和优化策略。

掌握这些算法不仅有助于解决算法竞赛问题，也为处理实际工程中的复杂优化问题提供了重要思路和方法。