# KMP算法与子树匹配算法专题 - Class100

## 📚 目录
- [算法概述](#算法概述)
- [核心算法](#核心算法)
- [扩展题目](#扩展题目)
- [工程化考量](#工程化考量)
- [复杂度分析](#复杂度分析)
- [面试技巧](#面试技巧)
- [实战训练](#实战训练)

## 🎯 算法概述

本专题深入探讨两种重要算法：KMP字符串匹配算法和二叉树子树匹配算法。这两种算法在计算机科学中具有重要地位，广泛应用于文本处理、模式识别、数据结构等领域。

### 核心算法
- **KMP算法** - 高效的字符串匹配算法
- **子树匹配算法** - 二叉树结构匹配算法

### 算法应用场景
- **KMP算法**：文本搜索、模式识别、DNA序列分析、自然语言处理
- **子树匹配算法**：树形结构比较、XML/JSON处理、代码结构分析、文档结构匹配

## 🔧 核心算法详解

### 1. KMP算法 (Knuth-Morris-Pratt Algorithm)

**时间复杂度**: O(n + m)  
**空间复杂度**: O(m)  
**适用场景**: 字符串匹配、模式识别、文本搜索

**核心思想**:
KMP算法通过预处理模式串，构建部分匹配表（next数组），在匹配失败时避免文本串指针的回溯，从而提高匹配效率。

**关键特性**:
- 线性时间复杂度
- 无需回溯文本串指针
- 适用于大规模文本搜索

**算法步骤**:
1. 预处理模式串，构建next数组
2. 使用双指针同时遍历文本串和模式串
3. 根据next数组优化匹配过程

### 2. 子树匹配算法

**时间复杂度**: 
- 暴力递归: O(n * m)
- 序列化+KMP: O(n + m)
**空间复杂度**: O(n + m)
**适用场景**: 二叉树结构比较、模式识别、XML/JSON处理

**核心思想**:
通过递归比较或序列化后使用KMP算法，判断一棵树是否包含另一棵树作为子树。

**关键特性**:
- 支持精确匹配
- 可以优化到线性时间复杂度
- 适用于树形结构处理

**算法步骤**:
1. 方法1 - 暴力递归：遍历主树每个节点，递归比较子树
2. 方法2 - 序列化+KMP：将两棵树序列化，使用KMP算法匹配

## 📈 扩展题目

### KMP算法题目

#### LeetCode 平台

#### 题目1: 28. 找出字符串中第一个匹配项的下标
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/find-the-index-of-the-first-occurrence-in-a-string/
**难度**: 简单
**描述**: 给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）。如果 needle 不是 haystack 的一部分，则返回  -1 。

**解法**:
- **KMP算法** (最优解)
  - 时间复杂度: O(n + m)
  - 空间复杂度: O(m)

#### 题目2: 459. 重复的子字符串
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/repeated-substring-pattern/
**难度**: 简单
**描述**: 给定一个非空的字符串 s ，检查是否可以通过由它的一个子串重复多次构成。

**解法**:
- **KMP算法** (最优解)
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目3: 1392. 最长快乐前缀
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/longest-happy-prefix/
**难度**: 困难
**描述**: 「快乐前缀」是在原字符串中既是 非空 前缀也是后缀（不包括原字符串自身）的字符串。给你一个字符串 s，请你返回它的 最长快乐前缀。如果不存在满足题意的前缀，则返回一个空字符串 "" 。

**解法**:
- **KMP算法next数组** (最优解)
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目4: 214. 最短回文串
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/shortest-palindrome/
**难度**: 困难
**描述**: 给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串。

**解法**:
- **KMP算法** (最优解)
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目5: 796. 旋转字符串
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/rotate-string/
**难度**: 简单
**描述**: 给定两个字符串, s 和 goal。如果在若干次旋转操作之后，s 能变成 goal ，那么返回 true 。

**解法**:
- **KMP算法/字符串拼接** (最优解)
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### POJ 平台

#### 题目6: 3461. Oulipo
**来源**: POJ  
**链接**: http://poj.org/problem?id=3461
**难度**: 中等
**描述**: 给定一个模式串 P 和一个文本串 T，计算 P 在 T 中出现的次数。

**解法**:
- **KMP算法** (最优解)
  - 时间复杂度: O(n + m)
  - 空间复杂度: O(m)
  - **文件**: Code03_Oulipo.java, Code03_Oulipo.cpp, Code03_Oulipo.py

#### 题目7: 2406. Power Strings
**来源**: POJ  
**链接**: http://poj.org/problem?id=2406
**难度**: 中等
**描述**: 给定一个字符串，判断它是否可以由某个子串重复多次组成。如果可以，返回最大的重复次数；否则返回1。

**解法**:
- **KMP算法next数组性质** (最优解)
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)
  - **文件**: Code04_PowerStrings.java, Code04_PowerStrings.cpp, Code04_PowerStrings.py

#### 题目8: 1961. Check If String Is a Prefix of Array
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/check-if-string-is-a-prefix-of-array/
**难度**: 简单
**描述**: 给你一个字符串 s 和一个字符串数组 words。请你判断 s 是否为 words 的 前缀字符串 。

**解法**:
- **KMP算法/直接拼接**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### HackerRank 平台

#### 题目9: Determining DNA Health
**来源**: HackerRank  
**链接**: https://www.hackerrank.com/challenges/determining-dna-health/problem
**难度**: 困难
**描述**: 给定一系列基因和它们的健康值，以及一系列DNA片段，计算每个DNA片段的健康值总和。

**解法**:
- **KMP算法 + 优化**
  - 时间复杂度: O(n*m + q*(n+m))
  - 空间复杂度: O(n*m)

#### 题目10: Knuth-Morris-Pratt Algorithm
**来源**: HackerRank  
**链接**: https://www.hackerrank.com/challenges/kmp-fp/problem
**难度**: 中等
**描述**: 实现KMP算法，查找模式串在文本串中的所有出现位置。

**解法**:
- **KMP算法**
  - 时间复杂度: O(n + m)
  - 空间复杂度: O(m)

#### Codeforces 平台

#### 题目11: Password
**来源**: Codeforces  
**链接**: https://codeforces.com/contest/126/problem/B
**难度**: 中等
**描述**: 给定一个字符串 s，找出一个最长的子串，该子串同时作为前缀、后缀和中间子串出现。

**解法**:
- **KMP算法next数组**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目12: Prefixes and Suffixes
**来源**: Codeforces  
**链接**: https://codeforces.com/contest/630/problem/D
**难度**: 简单
**描述**: 给定一个字符串 s，找出所有同时是前缀和后缀的子串的长度，并输出每个长度对应的出现次数。

**解法**:
- **KMP算法next数组**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 洛谷 平台

#### 题目13: P3375 【模板】KMP
**来源**: 洛谷  
**链接**: https://www.luogu.com.cn/problem/P3375
**难度**: 模板
**描述**: KMP算法模板题，输出模式串在文本串中的所有出现位置。

**解法**:
- **KMP算法**
  - 时间复杂度: O(n + m)
  - 空间复杂度: O(m)

#### 题目14: P4391 [BOI2009]Radio Transmission 无线传输
**来源**: 洛谷  
**链接**: https://www.luogu.com.cn/problem/P4391
**难度**: 提高
**描述**: 给定一个由某个子串重复多次组成的字符串，求最短的子串长度。

**解法**:
- **KMP算法next数组性质**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

### 子树匹配题目

#### LeetCode 平台

#### 题目15: 572. 另一棵树的子树
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/subtree-of-another-tree/
**难度**: 简单
**描述**: 给你两棵二叉树 root 和 subRoot 。检验 root 中是否包含和 subRoot 具有相同结构和节点值的子树。如果存在，返回 true ；否则，返回 false 。

**解法**:
- **方法一**: 暴力递归法
  - 时间复杂度: O(n * m)
  - 空间复杂度: O(max(n, m))
- **方法二**: 二叉树序列化 + KMP算法
  - 时间复杂度: O(n + m)
  - 空间复杂度: O(n + m) (最优解)

#### 题目16: 652. 寻找重复的子树
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/find-duplicate-subtrees/
**难度**: 中等
**描述**: 给定一棵二叉树 root ，返回所有重复的子树。对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。

**解法**:
- **序列化 + 哈希表**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目17: 100. 相同的树
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/same-tree/
**难度**: 简单
**描述**: 给你两棵二叉树的根节点 p 和 q ，编写一个函数来检验这两棵树是否相同。

**解法**:
- **递归解法**
  - 时间复杂度: O(min(n, m))
  - 空间复杂度: O(min(h1, h2))

#### 题目18: 1367. 二叉树中的链表
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/linked-list-in-binary-tree/
**难度**: 中等
**描述**: 给你一棵以 root 为根的二叉树和一个 head 为第一个节点的链表。如果在二叉树中，存在一条一直向下的路径，且每个点的数值恰好一一对应以 head 为首的链表中每个节点的值，那么请你返回 True ，否则返回 False 。

**解法**:
- **KMP算法 + DFS**
  - 时间复杂度: O(n*m)
  - 空间复杂度: O(m)

#### 题目19: 951. 翻转等价二叉树
**来源**: LeetCode  
**链接**: https://leetcode.cn/problems/flip-equivalent-binary-trees/
**难度**: 中等
**描述**: 我们可以为二叉树 T 定义一个翻转操作，如下所示：选择任意节点，然后交换它的左子树和右子树。只要经过一定次数的翻转操作后，一棵树可以变成另一棵树，我们就称它们是翻转等价的。

**解法**:
- **递归解法**
  - 时间复杂度: O(min(n, m))
  - 空间复杂度: O(min(h1, h2))

#### 牛客网 平台

#### 题目20: BM37 二叉搜索树的最近公共祖先
**来源**: 牛客网  
**链接**: https://www.nowcoder.com/practice/d9820119321945f588ed6a26f0a6991f
**难度**: 简单
**描述**: 给定一个二叉搜索树, 找到该树中两个指定节点的最近公共祖先。

**解法**:
- **迭代解法**
  - 时间复杂度: O(h)
  - 空间复杂度: O(1)

#### HackerRank 平台

#### 题目21: Tree: Preorder Traversal
**来源**: HackerRank  
**链接**: https://www.hackerrank.com/challenges/tree-preorder-traversal/problem
**难度**: 简单
**描述**: 实现二叉树的前序遍历。

**解法**:
- **递归解法**
  - 时间复杂度: O(n)
  - 空间复杂度: O(h)

#### 题目22: Tree: Compare two binary trees
**来源**: HackerRank  
**链接**: https://www.hackerrank.com/challenges/tree-comparison/problem
**难度**: 简单
**描述**: 比较两棵二叉树是否完全相同。

**解法**:
- **递归解法**
  - 时间复杂度: O(min(n, m))
  - 空间复杂度: O(min(h1, h2))

#### Codeforces 平台

#### 题目23: Tree Matching
**来源**: Codeforces  
**链接**: https://codeforces.com/contest/1182/problem/E
**难度**: 中等
**描述**: 给定一棵树，求最大匹配数（即选择最多的边，使得没有两条边共享一个顶点）。

**解法**:
- **树形DP**
  - 时间复杂度: O(n)
  - 空间复杂度: O(n)

#### 题目24: Tree Requests
**来源**: Codeforces  
**链接**: https://codeforces.com/contest/570/problem/D
**难度**: 困难
**描述**: 给定一棵树，每个节点有一个字符，回答多个查询，判断某个子树中，所有第k层的节点的字符是否可以重新排列成一个回文串。

**解法**:
- **DFS序 + 位运算**
  - 时间复杂度: O(n + q)
  - 空间复杂度: O(n)

## 🏗️ 工程化考量

### 1. 异常处理
```java
// KMP算法输入验证
public static int strStr(String s1, String s2) {
    if (s1 == null || s2 == null) {
        throw new IllegalArgumentException("输入字符串不能为null");
    }
    // ... 算法实现
}

// 子树匹配输入验证
public static boolean isSubtree(TreeNode t1, TreeNode t2) {
    // 处理空树情况
    if (t1 == null && t2 == null) return true;
    if (t1 == null || t2 == null) return false;
    // ... 算法实现
}
```

### 2. 边界条件处理
- 空字符串/空树处理
- 单字符/单节点情况
- 完全匹配/完全不匹配情况
- 模式串长度大于文本串情况

### 3. 性能优化
- KMP算法中next数组的预处理优化
- 子树匹配中避免重复计算
- 内存使用优化

### 4. 可测试性
```java
// 单元测试示例
@Test
public void testKMPSimpleCase() {
    assertEquals(2, Code01_KMP.strStr("hello", "ll"));
    assertEquals(-1, Code01_KMP.strStr("aaaaa", "bba"));
}

@Test
public void testSubtreeBasic() {
    // 构造测试树
    TreeNode t1 = new TreeNode(3);
    t1.left = new TreeNode(4);
    t1.right = new TreeNode(5);
    
    TreeNode t2 = new TreeNode(4);
    
    assertTrue(Code02_SubtreeOfAnotherTree.isSubtree(t1, t2));
}
```

## 📊 复杂度分析

### KMP算法复杂度
| 操作 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| next数组构建 | O(m) | O(m) |
| 匹配过程 | O(n) | O(1) |
| 总体复杂度 | O(n + m) | O(m) |

### 子树匹配复杂度
| 方法 | 时间复杂度 | 空间复杂度 |
|------|------------|------------|
| 暴力递归 | O(n * m) | O(max(n, m)) |
| 序列化+KMP | O(n + m) | O(n + m) |

## 💡 面试技巧

### 1. 算法选择依据
- **字符串匹配**: 优先考虑KMP算法
- **小规模数据**: 可以使用暴力匹配
- **树结构匹配**: 根据数据规模选择暴力递归或序列化+KMP

### 2. 代码实现要点
- 清晰的变量命名
- 关键步骤注释
- 边界条件处理
- 异常情况考虑

### 3. 性能分析能力
- 能够分析时间/空间复杂度
- 理解常数项对实际性能的影响
- 知道如何优化常数项

### 4. 沟通表达能力
- 清晰解释算法思路
- 分析时间/空间复杂度
- 讨论优化可能性
- 展示调试和优化过程

## 🧠 实战训练

### KMP算法推荐练习题目

| 题目名称 | 来源 | 难度 | 相关算法 |
|---------|------|------|--------|
| 找出字符串中第一个匹配项的下标 | LeetCode 28 | 简单 | KMP算法 |
| 重复的子字符串 | LeetCode 459 | 简单 | KMP算法 |
| 旋转字符串 | LeetCode 796 | 简单 | KMP算法 |
| 检查字符串是否是数组前缀 | LeetCode 1961 | 简单 | KMP算法 |
| 最长快乐前缀 | LeetCode 1392 | 困难 | KMP算法 |
| 最短回文串 | LeetCode 214 | 困难 | KMP算法 |
| Oulipo | POJ 3461 | 中等 | KMP算法 |
| Power Strings | POJ 2406 | 中等 | KMP算法 |
| 【模板】KMP | 洛谷 P3375 | 模板 | KMP算法 |
| [BOI2009]Radio Transmission | 洛谷 P4391 | 提高 | KMP算法 |
| KMP字符串匹配 | 牛客网 | 中等 | KMP算法 |
| 字符串匹配 | HackerRank | 中等 | KMP算法 |
| Determining DNA Health | HackerRank | 困难 | KMP算法 |
| Knuth-Morris-Pratt Algorithm | HackerRank | 中等 | KMP算法 |
| Password | Codeforces 126B | 中等 | KMP算法 |
| Prefixes and Suffixes | Codeforces 630D | 简单 | KMP算法 |
| 字符串匹配 | 杭电OJ 1711 | 中等 | KMP算法 |
| 字符串匹配 | 计蒜客 | 简单 | KMP算法 |
| 字符串匹配 | SPOJ NAJPF | 中等 | KMP算法 |

### 子树匹配推荐练习题目

| 题目名称 | 来源 | 难度 | 相关算法 |
|---------|------|------|--------|
| 另一棵树的子树 | LeetCode 572 | 简单 | 递归/KMP |
| 相同的树 | LeetCode 100 | 简单 | 递归 |
| 对称二叉树 | LeetCode 101 | 简单 | 递归 |
| 二叉树的前序遍历 | LeetCode 144 | 简单 | 树遍历 |
| 寻找重复的子树 | LeetCode 652 | 中等 | 序列化/哈希 |
| 二叉树中的链表 | LeetCode 1367 | 中等 | KMP/DFS |
| 翻转等价二叉树 | LeetCode 951 | 中等 | 递归 |
| 二叉搜索树的最近公共祖先 | 牛客网 BM37 | 简单 | 树遍历 |
| Tree: Preorder Traversal | HackerRank | 简单 | 树遍历 |
| Tree: Compare two binary trees | HackerRank | 简单 | 递归 |
| Tree Matching | Codeforces 1182E | 中等 | 树形DP |
| Tree Requests | Codeforces 570D | 困难 | DFS序/位运算 |
| 树的子结构 | 剑指Offer 26 | 中等 | 递归 |
| 树的序列化与反序列化 | 剑指Offer 37 | 困难 | 序列化 |
| Cow Pedigrees | USACO | 中等 | 树形DP |
| QTREE2 | SPOJ | 困难 | 树链剖分 |
| 二叉树遍历 | 杭电OJ | 简单 | 树遍历 |
| 二叉树的中序遍历 | 计蒜客 | 简单 | 树遍历 |

### 进阶题目
1. **多模式匹配** - AC自动机
2. **二维模式匹配** - 二维KMP
3. **动态树匹配** - 支持修改的树匹配
4. **模糊树匹配** - 近似匹配算法

## 🔍 调试技巧

### 1. 打印中间过程
```java
public static int kmp(char[] s1, char[] s2) {
    System.out.println("开始KMP匹配:");
    System.out.println("文本串: " + new String(s1));
    System.out.println("模式串: " + new String(s2));
    
    int n = s1.length, m = s2.length, x = 0, y = 0;
    int[] next = nextArray(s2, m);
    System.out.println("next数组: " + Arrays.toString(next));
    
    while (x < n && y < m) {
        System.out.printf("匹配位置: 文本串[%d]='%c', 模式串[%d]='%c'\n", x, s1[x], y, s2[y]);
        if (s1[x] == s2[y]) {
            x++;
            y++;
        } else if (y == 0) {
            System.out.println("模式串指针为0，文本串指针前移");
            x++;
        } else {
            int oldY = y;
            y = next[y];
            System.out.printf("匹配失败，模式串指针从%d回退到%d\n", oldY, y);
        }
    }
    return y == m ? x - y : -1;
}
```

### 2. 断言验证
```java
public static void main(String[] args) {
    // 基本测试用例
    assert strStr("hello", "ll") == 2 : "基本匹配测试失败";
    assert strStr("aaaaa", "bba") == -1 : "不匹配测试失败";
    assert strStr("", "") == 0 : "空字符串测试失败";
    
    System.out.println("所有断言测试通过!");
}
```

### 3. 性能分析
```java
public static void performanceTest() {
    String text = "a".repeat(100000) + "b";  // 长文本
    String pattern = "ab";  // 模式串
    
    long startTime = System.nanoTime();
    int result = strStr(text, pattern);
    long endTime = System.nanoTime();
    
    System.out.printf("KMP算法性能测试:\n");
    System.out.printf("文本长度: %d, 模式串长度: %d\n", text.length(), pattern.length());
    System.out.printf("匹配结果: %d\n", result);
    System.out.printf("执行时间: %.2f ms\n", (endTime - startTime) / 1_000_000.0);
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

## 🚀 快速开始

### 运行Java代码
```bash
cd class100
javac *.java
java Code01_KMP
java Code02_SubtreeOfAnotherTree
```

### 文件结构
```
class100/
├── Code01_KMP.java              # KMP算法实现
├── Code02_SubtreeOfAnotherTree.java  # 子树匹配算法实现
├── Code03_Oulipo.java           # POJ 3461 Oulipo实现
├── Code03_Oulipo.cpp            # POJ 3461 Oulipo实现
├── Code03_Oulipo.py             # POJ 3461 Oulipo实现
├── Code04_PowerStrings.java     # POJ 2406 Power Strings实现
├── Code04_PowerStrings.cpp      # POJ 2406 Power Strings实现
├── Code04_PowerStrings.py       # POJ 2406 Power Strings实现
├── AdditionalProblems.md        # 扩展题目详解
├── ProblemLinks.md              # 题目链接汇总
├── README.md                    # 项目说明
├── *.class                      # 编译文件
```

## 🎓 学习目标

### 知识目标
- 掌握KMP算法和子树匹配算法的原理和实现
- 理解时间/空间复杂度的计算方法
- 学会根据问题特征选择合适的算法
- 掌握算法优化和调试技巧

### 技能目标
- 能够用Java实现KMP和子树匹配算法
- 能够分析和解决复杂的字符串和树匹配问题
- 能够进行算法性能分析和优化
- 能够应对技术面试中的相关算法问题

### 能力目标
- 算法思维和问题解决能力
- 代码实现和调试能力
- 性能分析和优化能力
- 面试表达和沟通能力

---

**持续更新中... 更多内容将陆续添加**

*最后更新: 2025年10月19日*