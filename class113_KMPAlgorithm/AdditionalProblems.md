# KMP算法和子树匹配算法扩展题目

## 📋 KMP算法相关题目

### LeetCode题目

1. **28. 找出字符串中第一个匹配项的下标**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
   - 难度: 简单
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)
   - 最优解: 是

2. **459. 重复的子字符串**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/repeated-substring-pattern/
   - 难度: 简单
   - 解法: KMP算法/字符串匹配
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

3. **1392. 最长快乐前缀**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/longest-happy-prefix/
   - 难度: 困难
   - 解法: KMP算法next数组
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

4. **214. 最短回文串**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/shortest-palindrome/
   - 难度: 困难
   - 解法: KMP算法
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

5. **796. 旋转字符串**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/rotate-string/
   - 难度: 简单
   - 解法: 字符串拼接/KMP
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

### 牛客网题目

1. **NC105 二分查找-II**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/5272602925fb4a4898a6506b03f8940d
   - 难度: 简单
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)

2. **NC106 三个数的最大乘积**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/2b345dae74d1491994c911538c583329
   - 难度: 简单
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)

### HackerRank题目

1. **Knuth-Morris-Pratt Algorithm**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/kmp-fp/problem
   - 难度: 中等
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)

2. **Determining DNA Health**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/determining-dna-health/problem
   - 难度: 困难
   - 解法: KMP算法 + 其他优化
   - 时间复杂度: O(n*m + q*(n+m))
   - 空间复杂度: O(n*m)

### Codeforces题目

1. **Password**
   - 来源: Codeforces
   - 链接: https://codeforces.com/contest/126/problem/B
   - 难度: 中等
   - 解法: KMP算法
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

2. **Prefixes and Suffixes**
   - 来源: Codeforces
   - 链接: https://codeforces.com/contest/630/problem/D
   - 难度: 简单
   - 解法: KMP算法
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

### 洛谷题目

1. **P3375 【模板】KMP**
   - 来源: 洛谷
   - 链接: https://www.luogu.com.cn/problem/P3375
   - 难度: 模板
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)

2. **P4391 [BOI2009]Radio Transmission 无线传输**
   - 来源: 洛谷
   - 链接: https://www.luogu.com.cn/problem/P4391
   - 难度: 提高
   - 解法: KMP算法
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

### POJ题目

1. **3461 Oulipo**
   - 来源: POJ
   - 链接: http://poj.org/problem?id=3461
   - 难度: 中等
   - 解法: KMP算法
   - 时间复杂度: O(n + m)
   - 空间复杂度: O(m)
   - 最优解: 是
   - 文件: Code03_Oulipo.java, Code03_Oulipo.cpp, Code03_Oulipo.py

2. **2406 Power Strings**
   - 来源: POJ
   - 链接: http://poj.org/problem?id=2406
   - 难度: 中等
   - 解法: KMP算法next数组性质
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是
   - 文件: Code04_PowerStrings.java, Code04_PowerStrings.cpp, Code04_PowerStrings.py

## 🌲 子树匹配相关题目

### LeetCode题目

1. **572. 另一棵树的子树**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/subtree-of-another-tree/
   - 难度: 简单
   - 解法: 递归/序列化+KMP
   - 时间复杂度: O(n*m) / O(n+m)
   - 空间复杂度: O(max(n,m)) / O(n+m)
   - 最优解: 序列化+KMP

2. **100. 相同的树**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/same-tree/
   - 难度: 简单
   - 解法: 递归比较
   - 时间复杂度: O(min(n,m))
   - 空间复杂度: O(min(n,m))
   - 最优解: 是

3. **101. 对称二叉树**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/symmetric-tree/
   - 难度: 简单
   - 解法: 递归比较
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

4. **1367. 二叉树中的链表**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/linked-list-in-binary-tree/
   - 难度: 中等
   - 解法: 递归/DFS
   - 时间复杂度: O(n*m)
   - 空间复杂度: O(n)
   - 最优解: 是

5. **1145. 二叉树着色游戏**
   - 来源: LeetCode
   - 链接: https://leetcode.cn/problems/binary-tree-coloring-game/
   - 难度: 中等
   - 解法: 子树计数
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)
   - 最优解: 是

### 牛客网题目

1. **NC60 二叉树的最大路径和**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/da785ea0f64b449a938e447b693a91f6
   - 难度: 困难
   - 解法: 递归/子树遍历
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

2. **NC15 二叉树的层序遍历**
   - 来源: 牛客网
   - 链接: https://www.nowcoder.com/practice/7fe2212963db4790b57431d9ed259701
   - 难度: 中等
   - 解法: BFS/DFS
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

### HackerRank题目

1. **Binary Tree Nodes**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/binary-search-tree-1/problem
   - 难度: 中等
   - 解法: 树遍历
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

2. **Tree: Huffman Decoding**
   - 来源: HackerRank
   - 链接: https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
   - 难度: 中等
   - 解法: 树遍历
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

### Codeforces题目

1. **Tree with Maximum Cost**
   - 来源: Codeforces
   - 链接: https://codeforces.com/contest/1092/problem/F
   - 难度: 中等
   - 解法: 树形DP/子树遍历
   - 时间复杂度: O(n)
   - 空间复杂度: O(n)

2. **Lomsat gelral**
   - 来源: Codeforces
   - 链接: https://codeforces.com/contest/600/problem/E
   - 难度: 中等
   - 解法: 启发式合并/子树遍历
   - 时间复杂度: O(n log n)
   - 空间复杂度: O(n)

## 🎯 题目分类训练

### KMP算法训练路径

#### 初级（掌握基础）
1. 实现KMP算法核心逻辑
2. 理解next数组的构建过程
3. 解决简单的字符串匹配问题

#### 中级（应用扩展）
1. 使用KMP解决重复子串问题
2. 结合其他算法解决复杂问题
3. 优化KMP算法实现

#### 高级（深入理解）
1. 理解KMP算法的数学原理
2. 解决多模式匹配问题
3. 实现KMP算法的变种

### 子树匹配训练路径

#### 初级（掌握基础）
1. 实现基本的树结构比较
2. 理解递归遍历树的方法
3. 解决简单的子树匹配问题

#### 中级（应用扩展）
1. 使用序列化+KMP解决子树匹配
2. 结合其他树算法解决复杂问题
3. 优化子树匹配算法实现

#### 高级（深入理解）
1. 理解树同构问题
2. 解决动态树匹配问题
3. 实现高效的树匹配算法

## 💡 解题思路总结

### KMP算法常见题型

#### 模式1: 字符串匹配
- 特征：在文本中查找模式串
- 解法：直接使用KMP算法
- 变种：多次匹配、多模式匹配

#### 模式2: 重复模式识别
- 特征：识别字符串中的重复模式
- 解法：利用next数组的性质
- 变种：最小周期、最长公共前后缀

#### 模式3: 字符串构造
- 特征：根据特定规则构造字符串
- 解法：KMP算法 + 贪心/DP
- 变种：最短补全、最优前缀

### 子树匹配常见题型

#### 模式1: 子树存在性
- 特征：判断一棵树是否包含另一棵树作为子树
- 解法：递归比较/序列化+KMP
- 变种：多次查询、动态修改

#### 模式2: 树结构比较
- 特征：比较两棵树是否完全相同或对称
- 解法：递归比较
- 变种：近似匹配、模糊匹配

#### 模式3: 子树属性统计
- 特征：统计子树的某些属性（节点数、和等）
- 解法：DFS一次遍历
- 变种：最大/最小属性子树、满足条件的子树计数

## 🛠️ 工程化实践

### 1. 单元测试设计

```java
// KMP算法测试
@Test
public void testKMP() {
    assertEquals(2, Code01_KMP.strStr("hello", "ll"));
    assertEquals(-1, Code01_KMP.strStr("aaaaa", "bba"));
    assertEquals(0, Code01_KMP.strStr("abc", ""));
    assertEquals(-1, Code01_KMP.strStr("", "a"));
    assertEquals(0, Code01_KMP.strStr("abc", "abc"));
}

// 子树匹配测试
@Test
public void testSubtree() {
    // 构造测试树
    TreeNode t1 = new TreeNode(3);
    t1.left = new TreeNode(4);
    t1.right = new TreeNode(5);
    t1.left.left = new TreeNode(1);
    t1.left.right = new TreeNode(2);
    
    TreeNode t2 = new TreeNode(4);
    t2.left = new TreeNode(1);
    t2.right = new TreeNode(2);
    
    assertTrue(Code02_SubtreeOfAnotherTree.isSubtree(t1, t2));
    assertTrue(Code02_SubtreeOfAnotherTree.isSubtree2(t1, t2));
}
```

### 2. 性能基准测试

```java
public class PerformanceTest {
    public static void benchmarkKMP() {
        String text = generateRandomString(100000);
        String pattern = generateRandomString(100);
        
        long startTime = System.nanoTime();
        int result = Code01_KMP.strStr(text, pattern);
        long endTime = System.nanoTime();
        
        System.out.println("KMP算法耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
    
    public static void benchmarkSubtree() {
        TreeNode t1 = generateLargeTree(10000);
        TreeNode t2 = generateSmallTree(100);
        
        long startTime = System.nanoTime();
        boolean result = Code02_SubtreeOfAnotherTree.isSubtree2(t1, t2);
        long endTime = System.nanoTime();
        
        System.out.println("子树匹配算法耗时: " + (endTime - startTime) / 1000000.0 + " ms");
    }
}
```

### 3. 内存使用监控

```java
public class MemoryTest {
    public static void monitorKMPMemory() {
        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // 执行KMP算法
        String text = generateLargeString(1000000);
        String pattern = "test";
        int result = Code01_KMP.strStr(text, pattern);
        
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("KMP算法内存使用: " + (memoryAfter - memoryBefore) / 1024.0 + " KB");
    }
}
```

## 📚 学习资源

### 推荐书籍
1. 《算法导论》 - 字符串匹配和树算法理论基础
2. 《编程珠玑》 - 字符串算法的实际应用
3. 《算法竞赛入门经典》 - 竞赛中的字符串和树算法

### 在线资源
1. **GeeksforGeeks** - KMP算法详解
2. **Visualgo** - 字符串匹配和树算法可视化
3. **TopCoder** - 算法教程

### 实践平台
1. **LeetCode** - 算法题目练习
2. **HackerRank** - 编程挑战
3. **牛客网** - 国内算法题库
4. **Codeforces** - 竞赛平台

---

**持续更新中... 更多题目和解析将陆续添加**

*最后更新: 2025年10月19日*