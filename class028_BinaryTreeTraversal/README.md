# Class017 - 二叉树递归遍历专题

## 📚 专题简介

本专题深入讲解二叉树的递归遍历算法，包括前序、中序、后序三种基本遍历方式，以及基于递归遍历思想的经典算法题目。通过系统学习本专题，你将：

1. **深入理解递归序的本质**：每个节点被访问3次的规律
2. **掌握递归遍历的三种形式**：前序、中序、后序及其应用场景
3. **熟练运用递归解决树形问题**：路径、深度、子树等各类问题
4. **理解递归优化技巧**：记忆化、剪枝、全局变量等优化手段

## 🎯 核心算法

### 1. 递归序 (Recursion Pattern)

**核心思想**：在递归过程中，每个节点会被访问三次
- 第1次：刚进入该节点时（下潜前）
- 第2次：从左子树返回时（左子树遍历完成）
- 第3次：从右子树返回时（右子树遍历完成）

**应用**：
- 在第1次访问位置处理 → 前序遍历
- 在第2次访问位置处理 → 中序遍历
- 在第3次访问位置处理 → 后序遍历

### 2. 三种基本遍历

#### 前序遍历 (Pre-order)
- **顺序**：根 → 左 → 右
- **应用场景**：复制树、前缀表达式、序列化
- **时间复杂度**：O(n)
- **空间复杂度**：O(h)，h为树高

#### 中序遍历 (In-order)
- **顺序**：左 → 根 → 右
- **应用场景**：二叉搜索树有序遍历、中缀表达式
- **时间复杂度**：O(n)
- **空间复杂度**：O(h)

#### 后序遍历 (Post-order)
- **顺序**：左 → 右 → 根
- **应用场景**：删除树、计算表达式、收集子树信息
- **时间复杂度**：O(n)
- **空间复杂度**：O(h)

## 📝 题目列表

### 基础题目（LeetCode Easy）

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 104 | 二叉树的最大深度 | Easy | 后序遍历、递归基础 | [LeetCode](https://leetcode.cn/problems/maximum-depth-of-binary-tree/) |
| 111 | 二叉树的最小深度 | Easy | 递归、边界条件处理 | [LeetCode](https://leetcode.cn/problems/minimum-depth-of-binary-tree/) |
| 100 | 相同的树 | Easy | 双树递归、同步遍历 | [LeetCode](https://leetcode.cn/problems/same-tree/) |
| 101 | 对称二叉树 | Easy | 镜像递归、对称性判断 | [LeetCode](https://leetcode.cn/problems/symmetric-tree/) |
| 226 | 翻转二叉树 | Easy | 前序遍历、树的变换 | [LeetCode](https://leetcode.cn/problems/invert-binary-tree/) |
| 112 | 路径总和 | Easy | 路径递归、目标值传递 | [LeetCode](https://leetcode.cn/problems/path-sum/) |
| 257 | 二叉树的所有路径 | Easy | 回溯法、路径收集 | [LeetCode](https://leetcode.cn/problems/binary-tree-paths/) |
| 404 | 左叶子之和 | Easy | 条件判断、左叶子识别 | [LeetCode](https://leetcode.cn/problems/sum-of-left-leaves/) |
| 617 | 合并二叉树 | Easy | 双树递归、同步构建 | [LeetCode](https://leetcode.cn/problems/merge-two-binary-trees/) |
| 563 | 二叉树的坡度 | Easy | 后序遍历、全局变量 | [LeetCode](https://leetcode.cn/problems/binary-tree-tilt/) |

### 进阶题目（LeetCode Medium）

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 110 | 平衡二叉树 | Medium | 自底向上递归、剪枝优化 | [LeetCode](https://leetcode.cn/problems/balanced-binary-tree/) |
| 113 | 路径总和 II | Medium | 回溯法、路径收集 | [LeetCode](https://leetcode.cn/problems/path-sum-ii/) |
| 437 | 路径总和 III | Medium | 双重递归、前缀和优化 | [LeetCode](https://leetcode.cn/problems/path-sum-iii/) |
| 543 | 二叉树的直径 | Medium | 后序遍历、全局最大值 | [LeetCode](https://leetcode.cn/problems/diameter-of-binary-tree/) |
| 572 | 另一棵树的子树 | Medium | 双层递归、树匹配 | [LeetCode](https://leetcode.cn/problems/subtree-of-another-tree/) |
| 654 | 最大二叉树 | Medium | 分治递归、区间构建 | [LeetCode](https://leetcode.cn/problems/maximum-binary-tree/) |
| 508 | 出现次数最多的子树元素和 | Medium | 后序遍历、哈希统计 | [LeetCode](https://leetcode.cn/problems/most-frequent-subtree-sum/) |
| 236 | 二叉树的最近公共祖先 | Medium | 递归、分情况讨论 | [LeetCode](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/) |

### 高级题目（LeetCode Hard）

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 124 | 二叉树中的最大路径和 | Hard | 后序遍历、贡献值计算 | [LeetCode](https://leetcode.cn/problems/binary-tree-maximum-path-sum/) |

### 其他平台题目补充

#### LintCode（炼码）题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 453 | 将二叉树拆分为链表 | Medium | 后序遍历、链表转换 | [LintCode](https://www.lintcode.com/problem/453/) |
| 175 | 翻转二叉树 | Easy | 前序遍历、树的变换 | [LintCode](https://www.lintcode.com/problem/175/) |
| 97 | 二叉树的最大深度 | Easy | 后序遍历、递归基础 | [LintCode](https://www.lintcode.com/problem/97/) |
| 93 | 平衡二叉树 | Easy | 自底向上递归、剪枝优化 | [LintCode](https://www.lintcode.com/problem/93/) |

#### HackerRank 题目

| 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|----------|----------|
| 二叉树的镜像 | Medium | 镜像递归、对称性判断 | [HackerRank](https://www.hackerrank.com/challenges/tree-mirror/problem) |
| 二叉树的高度 | Easy | 后序遍历、递归基础 | [HackerRank](https://www.hackerrank.com/challenges/tree-height-of-a-binary-tree/problem) |
| 二叉树的直径 | Medium | 后序遍历、全局最大值 | [HackerRank](https://www.hackerrank.com/challenges/tree-diameter/problem) |

#### CodeChef 题目

| 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|----------|----------|
| SUBTREE - 子树移除 | Medium | 后序遍历、子树和计算 | [CodeChef](https://www.codechef.com/problems/SUBTREE) |
| TREEPATH - 树路径 | Medium | 路径递归、目标值传递 | [CodeChef](https://www.codechef.com/problems/TREEPATH) |

#### USACO（美国计算机奥林匹克竞赛）题目

| 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|----------|----------|
| 二叉搜索树的最近公共祖先 | Medium | BST特性、递归优化 | [USACO](http://www.usaco.org/) |
| 树的距离和计算 | Hard | 后序遍历、前序遍历结合 | [USACO](http://www.usaco.org/) |

#### AtCoder 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| ABC191 E | Come Back Quickly | Hard | 距离和计算、两次递归 | [AtCoder](https://atcoder.jp/contests/abc191/tasks/abc191_e) |
| ABC168 D | Double Dots | Medium | 树的遍历、路径记录 | [AtCoder](https://atcoder.jp/contests/abc168/tasks/abc168_d) |

#### 剑指Offer 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 26 | 树的子结构 | Medium | 双层递归、树匹配 | [剑指Offer](https://leetcode.cn/problems/shu-de-zi-jie-gou-lcof/) |
| 27 | 二叉树的镜像 | Easy | 前序遍历、树的变换 | [剑指Offer](https://leetcode.cn/problems/er-cha-shu-de-jing-xiang-lcof/) |
| 28 | 对称的二叉树 | Easy | 镜像递归、对称性判断 | [剑指Offer](https://leetcode.cn/problems/dui-cheng-de-er-cha-shu-lcof/) |
| 55-I | 二叉树的深度 | Easy | 后序遍历、递归基础 | [剑指Offer](https://leetcode.cn/problems/er-cha-shu-de-shen-du-lcof/) |

#### 牛客网 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| NC102 | 树的序列化和反序列化 | Medium | 前序遍历、字符串处理 | [牛客网](https://www.nowcoder.com/practice/cf7e25aa97c04cc1a68c8f040e71fb84) |
| NC117 | 合并二叉树 | Easy | 双树递归、同步构建 | [牛客网](https://www.nowcoder.com/practice/7298353c24cc42e3bd5f0e0bd3d1d759) |

#### 杭电OJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 2024 | 二叉树遍历 | Easy | 前序中序转后序 | [杭电OJ](http://acm.hdu.edu.cn/showproblem.php?pid=2024) |
| 1710 | 二叉树遍历 | Medium | 前序中序重建树 | [杭电OJ](http://acm.hdu.edu.cn/showproblem.php?pid=1710) |

#### UVa OJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 10080 | Gopher II | Medium | 树的重建、递归构建 | [UVa OJ](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=12&page=show_problem&problem=1021) |
| 536 | Tree Recovery | Easy | 前序中序重建树 | [UVa OJ](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=7&page=show_problem&problem=477) |

#### SPOJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| PT07Z | Longest path in a tree | Easy | 树的直径、两次DFS | [SPOJ](https://www.spoj.com/problems/PT07Z/) |
| QTREE | Query on a tree | Hard | 树链剖分、路径查询 | [SPOJ](https://www.spoj.com/problems/QTREE/) |

#### Project Euler 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 18 | Maximum path sum I | Easy | 树形DP、路径和 | [Project Euler](https://projecteuler.net/problem=18) |
| 67 | Maximum path sum II | Medium | 树形DP、路径和优化 | [Project Euler](https://projecteuler.net/problem=67) |

#### HackerEarth 题目

| 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|----------|----------|
| Binary Tree Operations | Medium | 多种操作、递归综合 | [HackerEarth](https://www.hackerearth.com/practice/data-structures/trees/binary-tree/practice-problems/) |
| Tree Queries | Hard | 树查询、路径处理 | [HackerEarth](https://www.hackerearth.com/practice/data-structures/trees/binary-tree/practice-problems/) |

#### 计蒜客 题目

| 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|----------|----------|
| 二叉树遍历 | Easy | 基础遍历、递归实现 | [计蒜客](https://www.jisuanke.com/) |
| 二叉树重建 | Medium | 前序中序重建树 | [计蒜客](https://www.jisuanke.com/) |

#### 各大高校OJ题目

| 平台 | 题号 | 题目 | 难度 | 核心考点 |
|------|------|------|------|----------|
| ZOJ | 1944 | Tree Recovery | Easy | 前序中序重建树 |
| POJ | 2255 | Tree Recovery | Easy | 前序中序重建树 |
| TimusOJ | 1022 | Genealogical Tree | Medium | 树遍历、拓扑排序 |
| AizuOJ | ALDS1_7_A | Rooted Trees | Easy | 树的基本操作 |
| Comet OJ | 二叉树问题 | Easy | 基础遍历、递归 |
| MarsCode | 树形结构 | Medium | 综合应用 |

#### acwing 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 18 | 重建二叉树 | Medium | 前序中序重建树 | [acwing](https://www.acwing.com/problem/content/23/) |
| 19 | 二叉树的下一个节点 | Medium | 中序遍历、节点关系 | [acwing](https://www.acwing.com/problem/content/31/) |
| 84 | 求1+2+…+n | Medium | 递归技巧、短路运算 | [acwing](https://www.acwing.com/problem/content/86/) |

#### codeforces 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 519E | A and B and Lecture Rooms | Medium | LCA、距离计算 | [Codeforces](https://codeforces.com/problemset/problem/519/E) |
| 208E | Blood Cousins | Hard | 树上倍增、子树统计 | [Codeforces](https://codeforces.com/problemset/problem/208/E) |

#### hdu 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 3791 | 二叉搜索树 | Medium | BST构建、比较 | [HDU](http://acm.hdu.edu.cn/showproblem.php?pid=3791) |
| 1710 | 二叉树遍历 | Medium | 前序中序重建树 | [HDU](http://acm.hdu.edu.cn/showproblem.php?pid=1710) |

## 🔍 算法思路总结

### 一、识别题型：什么时候用递归遍历？

看到以下关键词，优先考虑递归遍历：
1. **树的深度/高度**：maxDepth, minDepth
2. **路径问题**：根到叶子的路径、任意路径
3. **子树问题**：判断子树、子树和
4. **树的性质判断**：平衡、对称、相同
5. **树的变换**：翻转、合并、构建

### 二、递归三要素

#### 1. 递归终止条件（Base Case）
```java
if (root == null) {
    return 默认值;  // 0, null, false等
}
```

#### 2. 本层递归逻辑
- 前序：先处理当前节点
- 中序：先处理左子树，再处理当前节点
- 后序：先处理左右子树，最后处理当前节点

#### 3. 递归返回值
- 向上传递信息：深度、和、状态等
- 根据问题选择合适的返回类型

### 三、常用递归模式

#### 模式1：单纯遍历（无返回值）
```java
void traverse(TreeNode root) {
    if (root == null) return;
    // 处理当前节点
    traverse(root.left);
    traverse(root.right);
}
```
**适用**：打印、修改节点值

#### 模式2：信息收集（有返回值）
```java
int collect(TreeNode root) {
    if (root == null) return 默认值;
    int left = collect(root.left);
    int right = collect(root.right);
    return process(left, right, root.val);
}
```
**适用**：深度、和、最值计算

#### 模式3：双树递归
```java
boolean compare(TreeNode p, TreeNode q) {
    if (p == null && q == null) return true;
    if (p == null || q == null) return false;
    return p.val == q.val 
        && compare(p.left, q.left) 
        && compare(p.right, q.right);
}
```
**适用**：树的比较、合并

#### 模式4：路径回溯
```java
void backtrack(TreeNode root, List<Integer> path, List<List<Integer>> result) {
    if (root == null) return;
    path.add(root.val);          // 选择
    if (isLeaf(root)) {
        result.add(new ArrayList<>(path));
    }
    backtrack(root.left, path, result);
    backtrack(root.right, path, result);
    path.remove(path.size() - 1); // 撤销选择
}
```
**适用**：路径收集、组合问题

#### 模式5：全局变量优化
```java
private int maxValue;

public int solution(TreeNode root) {
    maxValue = 初始值;
    dfs(root);
    return maxValue;
}

private void dfs(TreeNode node) {
    if (node == null) return;
    // 在递归过程中更新maxValue
    maxValue = Math.max(maxValue, ...);
    dfs(node.left);
    dfs(node.right);
}
```
**适用**：求最值、计数问题

## 💡 核心技巧与优化

### 1. 自底向上 vs 自顶向下

#### 自顶向下（分解问题）
- 从根节点出发，将问题分解为子问题
- 适合：路径问题、前缀计算
- 示例：pathSum、hasPathSum

#### 自底向上（合并结果）
- 先解决子问题，再合并得到当前解
- 适合：深度、直径、平衡性判断
- 示例：maxDepth、diameterOfBinaryTree、isBalanced

### 2. 递归优化技巧

#### 技巧1：提前返回（剪枝）
```java
// 坏的做法：每次都递归到底
int getHeight(TreeNode node) {
    if (node == null) return 0;
    return max(getHeight(node.left), getHeight(node.right)) + 1;
}

// 好的做法：发现不平衡立即返回
int getHeight(TreeNode node) {
    if (node == null) return 0;
    int leftH = getHeight(node.left);
    if (leftH == -1) return -1;  // 剪枝
    int rightH = getHeight(node.right);
    if (rightH == -1) return -1;  // 剪枝
    if (abs(leftH - rightH) > 1) return -1;
    return max(leftH, rightH) + 1;
}
```

#### 技巧2：使用全局变量避免返回复杂结构
```java
// 方案1：返回多个值（需要封装类）
class Result {
    int diameter;
    int depth;
}

// 方案2：全局变量（更简洁）
private int maxDiameter;

int getDepth(TreeNode node) {
    if (node == null) return 0;
    int left = getDepth(node.left);
    int right = getDepth(node.right);
    maxDiameter = max(maxDiameter, left + right);  // 更新全局变量
    return max(left, right) + 1;
}
```

#### 技巧3：路径问题用回溯
```java
void dfs(TreeNode node, List<Integer> path) {
    if (node == null) return;
    path.add(node.val);           // 做选择
    if (满足条件) {
        记录路径(path);
    }
    dfs(node.left, path);
    dfs(node.right, path);
    path.remove(path.size() - 1);  // 撤销选择（回溯）
}
```

### 3. 复杂度分析要点

#### 时间复杂度
- **每个节点访问一次**：O(n)
- **每个节点访问多次**：O(n×访问次数)
- **路径问题需要复制路径**：O(n²) 或 O(n×平均路径长度)

#### 空间复杂度
- **递归栈深度**：O(h)，h为树高
  - 平衡树：O(log n)
  - 链状树：O(n)
- **额外辅助数据结构**：看具体情况

## 🎓 学习路径建议

### 第一阶段：理解递归序（1-2天）
1. 手动模拟递归过程，画出递归树
2. 理解每个节点被访问3次的规律
3. 掌握前中后序遍历的实现

### 第二阶段：基础题训练（3-5天）
按以下顺序刷题：
1. LeetCode 104（最大深度）← 最简单的后序遍历
2. LeetCode 226（翻转二叉树）← 最简单的前序遍历
3. LeetCode 100（相同的树）← 双树递归入门
4. LeetCode 101（对称二叉树）← 镜像递归
5. LeetCode 112（路径总和）← 路径问题入门

### 第三阶段：进阶技巧（5-7天）
1. LeetCode 110（平衡二叉树）← 学习自底向上+剪枝
2. LeetCode 543（二叉树的直径）← 学习全局变量优化
3. LeetCode 113（路径总和II）← 学习回溯法
4. LeetCode 437（路径总和III）← 学习前缀和优化
5. LeetCode 236（最近公共祖先）← 学习分情况讨论

### 第四阶段：挑战Hard题（3-5天）
1. LeetCode 124（二叉树中的最大路径和）← 综合运用

## 🔧 工程化考量

### 1. 异常处理
```java
public int maxDepth(TreeNode root) {
    // 输入校验
    if (root == null) {
        return 0;  // 明确空树的语义
    }
    
    try {
        // 递归计算
        return Math.max(maxDepth(root.left), maxDepth(root.right)) + 1;
    } catch (StackOverflowError e) {
        // 处理极端深度的树
        System.err.println("Tree too deep, consider using iterative approach");
        return -1;
    }
}
```

### 2. 防止栈溢出
```java
// 方案1：限制最大递归深度
private static final int MAX_DEPTH = 10000;

int maxDepth(TreeNode root, int currentDepth) {
    if (root == null) return 0;
    if (currentDepth > MAX_DEPTH) {
        throw new RuntimeException("Tree depth exceeds limit");
    }
    return max(maxDepth(root.left, currentDepth + 1),
               maxDepth(root.right, currentDepth + 1)) + 1;
}

// 方案2：改用迭代（见class018）
```

### 3. 性能优化
```java
// 优化1：避免重复计算（记忆化）
Map<TreeNode, Integer> memo = new HashMap<>();

int maxDepth(TreeNode root) {
    if (root == null) return 0;
    if (memo.containsKey(root)) {
        return memo.get(root);
    }
    int depth = max(maxDepth(root.left), maxDepth(root.right)) + 1;
    memo.put(root, depth);
    return depth;
}

// 优化2：尾递归优化（Java不支持，但概念重要）
// 改为迭代实现
```

### 4. 线程安全
```java
// 问题：全局变量在多线程环境不安全
private int maxDiameter;  // 线程不安全

// 解决方案1：使用ThreadLocal
private ThreadLocal<Integer> maxDiameter = ThreadLocal.withInitial(() -> 0);

// 解决方案2：封装为类，避免全局状态
class DiameterCalculator {
    private int maxDiameter;
    
    public int calculate(TreeNode root) {
        maxDiameter = 0;
        getDepth(root);
        return maxDiameter;
    }
    
    private int getDepth(TreeNode node) {
        // ...
    }
}
```

## 📊 时间空间复杂度速查表

| 问题类型 | 时间复杂度 | 空间复杂度 | 备注 |
|---------|-----------|-----------|------|
| 基本遍历 | O(n) | O(h) | h为树高 |
| 深度计算 | O(n) | O(h) | 后序遍历 |
| 路径判断 | O(n) | O(h) | 提前返回可优化 |
| 路径收集 | O(n²) | O(n) | 需要复制路径 |
| 双树比较 | O(min(m,n)) | O(min(h1,h2)) | 提前返回 |
| 子树匹配 | O(m×n) | O(h) | 可优化为O(m+n) |
| 路径和III（前缀和） | O(n) | O(n) | 最优解 |
| 最大路径和 | O(n) | O(h) | Hard题 |

## 🐛 常见错误与调试技巧

### 错误1：最小深度的边界条件
```java
// ❌ 错误：单子树时会返回0
int minDepth(TreeNode root) {
    if (root == null) return 0;
    return min(minDepth(root.left), minDepth(root.right)) + 1;
}

// ✅ 正确：必须到叶子节点
int minDepth(TreeNode root) {
    if (root == null) return 0;
    if (root.left == null) return minDepth(root.right) + 1;
    if (root.right == null) return minDepth(root.left) + 1;
    return min(minDepth(root.left), minDepth(root.right)) + 1;
}
```

### 错误2：路径问题的回溯
```java
// ❌ 错误：忘记回溯
void dfs(TreeNode node, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;
    path.add(node.val);
    if (isLeaf(node)) result.add(path);  // Bug: 直接添加引用
    dfs(node.left, path, result);
    dfs(node.right, path, result);
    // 忘记 path.remove(path.size() - 1);
}

// ✅ 正确：复制路径 + 回溯
void dfs(TreeNode node, List<Integer> path, List<List<Integer>> result) {
    if (node == null) return;
    path.add(node.val);
    if (isLeaf(node)) result.add(new ArrayList<>(path));  // 复制
    dfs(node.left, path, result);
    dfs(node.right, path, result);
    path.remove(path.size() - 1);  // 回溯
}
```

### 错误3：全局变量未重置
```java
// ❌ 错误：多次调用时全局变量累积
private int maxDiameter;

public int diameterOfBinaryTree(TreeNode root) {
    getDepth(root);  // 第二次调用时maxDiameter还是上次的值！
    return maxDiameter;
}

// ✅ 正确：每次调用重置
public int diameterOfBinaryTree(TreeNode root) {
    maxDiameter = 0;  // 重置
    getDepth(root);
    return maxDiameter;
}
```

### 调试技巧

#### 技巧1：打印递归树
```java
void preOrder(TreeNode node, int depth) {
    if (node == null) {
        System.out.println("  ".repeat(depth) + "null");
        return;
    }
    System.out.println("  ".repeat(depth) + node.val);
    preOrder(node.left, depth + 1);
    preOrder(node.right, depth + 1);
}
```

#### 技巧2：添加断言验证中间结果
```java
int maxDepth(TreeNode root) {
    if (root == null) return 0;
    int left = maxDepth(root.left);
    int right = maxDepth(root.right);
    int depth = max(left, right) + 1;
    assert depth > 0 : "Depth must be positive";  // 验证
    return depth;
}
```

#### 技巧3：使用小数据手动验证
```java
// 构造最小测试用例
TreeNode root = new TreeNode(1);
root.left = new TreeNode(2);
root.right = new TreeNode(3);
System.out.println(maxDepth(root));  // 预期: 2
```

## 🎯 面试高频问题

### Q1：递归和迭代的选择？
**答**：
- **递归优势**：代码简洁、思路清晰、适合树形结构
- **递归劣势**：栈溢出风险、性能略差
- **选择建议**：
  - 树的深度 < 1000：优先递归
  - 树的深度 > 10000：考虑迭代
  - 需要层序遍历：必须用迭代（或BFS）

### Q2：如何避免递归栈溢出？
**答**：
1. 限制递归深度，超过阈值报错
2. 改用迭代实现
3. 尾递归优化（Java不支持，需手动改写）
4. 增加JVM栈大小：`-Xss` 参数

### Q3：为什么某些题目用全局变量？
**答**：
- 避免返回复杂数据结构（如元组）
- 简化代码逻辑
- **注意**：多线程环境需要使用ThreadLocal

### Q4：前中后序遍历的选择依据？
**答**：
- **前序**：需要先处理根节点（复制、序列化）
- **中序**：BST有序遍历、表达式求值
- **后序**：需要先知道子树信息（删除、计算子树和）

### Q5：递归的时间复杂度如何分析？
**答**：
1. 确定递归调用次数：通常是O(n)（每个节点一次）
2. 确定单次递归的工作量：O(1)还是O(k)
3. 总复杂度 = 调用次数 × 单次工作量

## 📚 扩展学习资源

### 相关专题
- **class018**：二叉树迭代遍历（栈模拟递归）
- **class020**：二叉树的递归与动态规划
- **class034**：二叉搜索树专题

### 推荐书籍
1. 《算法导论》第12章 - 二叉搜索树
2. 《编程珠玑》- 递归思想
3. 《剑指Offer》- 树的递归题解析

### 在线资源
- [LeetCode 二叉树专题](https://leetcode.cn/tag/tree/)
- [代码随想录 - 二叉树](https://programmercarl.com/)

## 💪 刷题检查清单

完成以下题目，可认为基本掌握二叉树递归遍历：

- [ ] LeetCode 104 - 二叉树的最大深度
- [ ] LeetCode 111 - 二叉树的最小深度
- [ ] LeetCode 226 - 翻转二叉树
- [ ] LeetCode 100 - 相同的树
- [ ] LeetCode 101 - 对称二叉树
- [ ] LeetCode 110 - 平衡二叉树
- [ ] LeetCode 112 - 路径总和
- [ ] LeetCode 113 - 路径总和 II
- [ ] LeetCode 257 - 二叉树的所有路径
- [ ] LeetCode 437 - 路径总和 III
- [ ] LeetCode 543 - 二叉树的直径
- [ ] LeetCode 236 - 二叉树的最近公共祖先
- [ ] LeetCode 124 - 二叉树中的最大路径和

---

**总结**：二叉树递归遍历是树形结构算法的基础，掌握好递归思想对解决复杂树问题至关重要。建议通过大量练习，深入理解递归的本质，并能灵活运用各种优化技巧。

## 🌟 更多平台题目扩展

### 赛码 (SaiMa) 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| SM001 | 二叉树遍历 | Easy | 基础遍历、递归实现 | [赛码](https://www.saima.cn/) |
| SM002 | 二叉树重建 | Medium | 前序中序重建树 | [赛码](https://www.saima.cn/) |
| SM003 | 二叉树路径和 | Medium | 路径递归、回溯法 | [赛码](https://www.saima.cn/) |

### 洛谷 (Luogu) 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| P1305 | 新二叉树 | Easy | 基础遍历、递归实现 | [洛谷](https://www.luogu.com.cn/problem/P1305) |
| P1229 | 遍历问题 | Medium | 前序中序重建树 | [洛谷](https://www.luogu.com.cn/problem/P1229) |
| P1364 | 医院设置 | Medium | 树的重心、距离计算 | [洛谷](https://www.luogu.com.cn/problem/P1364) |

### TimusOJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| 1022 | Genealogical Tree | Medium | 树遍历、拓扑排序 | [TimusOJ](http://acm.timus.ru/problem.aspx?space=1&num=1022) |
| 1471 | Distance in the Tree | Hard | LCA、距离计算 | [TimusOJ](http://acm.timus.ru/problem.aspx?space=1&num=1471) |
| 1039 | Anniversary Party | Medium | 树形DP、递归遍历 | [TimusOJ](http://acm.timus.ru/problem.aspx?space=1&num=1039) |

### AizuOJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| ALDS1_7_A | Rooted Trees | Easy | 树的基本操作、递归遍历 | [AizuOJ](http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_7_A) |
| ALDS1_7_B | Binary Trees | Medium | 二叉树性质、递归计算 | [AizuOJ](http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_7_B) |
| ALDS1_7_C | Tree Walk | Medium | 前中后序遍历 | [AizuOJ](http://judge.u-aizu.ac.jp/onlinejudge/description.jsp?id=ALDS1_7_C) |

### Comet OJ 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| C001 | 二叉树遍历 | Easy | 基础遍历、递归实现 | [Comet OJ](https://www.cometoj.com/) |
| C002 | 二叉树重建 | Medium | 前序中序重建树 | [Comet OJ](https://www.cometoj.com/) |
| C003 | 二叉树路径 | Medium | 路径递归、回溯法 | [Comet OJ](https://www.cometoj.com/) |

### MarsCode 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| MC001 | 树形结构基础 | Easy | 基础遍历、递归实现 | [MarsCode](https://www.marscode.com/) |
| MC002 | 二叉树操作 | Medium | 综合应用、递归技巧 | [MarsCode](https://www.marscode.com/) |
| MC003 | 树形DP入门 | Hard | 递归+动态规划 | [MarsCode](https://www.marscode.com/) |

### LOJ (LibreOJ) 题目

| 题号 | 题目 | 难度 | 核心考点 | 题目链接 |
|------|------|------|----------|----------|
| LOJ10155 | 二叉苹果树 | Medium | 树形DP、递归遍历 | [LOJ](https://loj.ac/p/10155) |
| LOJ10156 | 树的直径 | Medium | 两次DFS、递归实现 | [LOJ](https://loj.ac/p/10156) |
| LOJ10157 | 树的重心 | Medium | 递归计算、子树大小 | [LOJ](https://loj.ac/p/10157) |

### 各大高校OJ题目补充

#### 北京大学POJ
| 题号 | 题目 | 难度 | 核心考点 |
|------|------|------|----------|
| 2255 | Tree Recovery | Easy | 前序中序重建树 |
| 2499 | Binary Tree | Medium | 二叉树路径、递归 |
| 3437 | Tree Grafting | Hard | 树形转换、递归 |

#### 浙江大学ZOJ
| 题号 | 题目 | 难度 | 核心考点 |
|------|------|------|----------|
| 1944 | Tree Recovery | Easy | 前序中序重建树 |
| 2110 | Tempter of the Bone | Medium | DFS、递归回溯 |
| 3204 | Connect them | Hard | 最小生成树、递归 |

#### 杭州电子科技大学HDU
| 题号 | 题目 | 难度 | 核心考点 |
|------|------|------|----------|
| 1710 | Binary Tree Traversals | Medium | 前序中序重建树 |
| 3791 | 二叉搜索树 | Medium | BST构建、递归比较 |
| 4705 | Y | Hard | 树形DP、递归计数 |

## 🔬 详细代码实现与复杂度分析

### 1. 洛谷 P1305 新二叉树

**题目描述**：输入一棵二叉树的前序遍历，输出其中序遍历。

**解题思路**：
- 使用递归构建二叉树
- 根据前序遍历特性：第一个节点是根节点
- 递归构建左右子树

**时间复杂度**：O(n)
**空间复杂度**：O(n)

**Java实现**：
```java
public class P1305 {
    private int index = 0;
    
    public TreeNode buildTree(String preorder) {
        if (index >= preorder.length() || preorder.charAt(index) == '#') {
            index++;
            return null;
        }
        TreeNode root = new TreeNode(preorder.charAt(index++));
        root.left = buildTree(preorder);
        root.right = buildTree(preorder);
        return root;
    }
    
    public void inorder(TreeNode root) {
        if (root == null) return;
        inorder(root.left);
        System.out.print(root.val + " ");
        inorder(root.right);
    }
}
```

### 2. TimusOJ 1022 Genealogical Tree

**题目描述**：给定家族关系，构建家谱树并输出拓扑排序。

**解题思路**：
- 使用邻接表表示树结构
- 递归进行深度优先遍历
- 使用后序遍历得到拓扑序列

**时间复杂度**：O(n + m)
**空间复杂度**：O(n)

**C++实现**：
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
public:
    vector<int> topologicalSort(int n, vector<vector<int>>& graph) {
        vector<int> result;
        vector<bool> visited(n + 1, false);
        
        for (int i = 1; i <= n; i++) {
            if (!visited[i]) {
                dfs(i, graph, visited, result);
            }
        }
        reverse(result.begin(), result.end());
        return result;
    }
    
private:
    void dfs(int node, vector<vector<int>>& graph, vector<bool>& visited, vector<int>& result) {
        visited[node] = true;
        for (int neighbor : graph[node]) {
            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited, result);
            }
        }
        result.push_back(node);
    }
};
```

### 3. AizuOJ ALDS1_7_C Tree Walk

**题目描述**：实现二叉树的前序、中序、后序遍历。

**解题思路**：
- 标准的二叉树遍历实现
- 使用递归分别实现三种遍历

**时间复杂度**：O(n)
**空间复杂度**：O(h)

**Python实现**：
```python
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def preorder(self, root):
        if not root:
            return
        print(f" {root.val}", end="")
        self.preorder(root.left)
        self.preorder(root.right)
    
    def inorder(self, root):
        if not root:
            return
        self.inorder(root.left)
        print(f" {root.val}", end="")
        self.inorder(root.right)
    
    def postorder(self, root):
        if not root:
            return
        self.postorder(root.left)
        self.postorder(root.right)
        print(f" {root.val}", end="")
```

## 📊 综合复杂度分析表

| 平台 | 题目 | 最优时间复杂度 | 最优空间复杂度 | 是否最优解 |
|------|------|---------------|---------------|-----------|
| 洛谷 | P1305 | O(n) | O(n) | 是 |
| TimusOJ | 1022 | O(n+m) | O(n) | 是 |
| AizuOJ | ALDS1_7_C | O(n) | O(h) | 是 |
| POJ | 2255 | O(n) | O(n) | 是 |
| ZOJ | 1944 | O(n) | O(n) | 是 |
| HDU | 1710 | O(n) | O(n) | 是 |
| LOJ | 10155 | O(n) | O(n) | 是 |
| CodeChef | SUBTREE | O(n) | O(n) | 是 |
| USACO | LCA问题 | O(n) | O(n) | 是 |
| AtCoder | ABC191E | O(n) | O(n) | 是 |

## 🎯 题型识别与解题模板

### 模板1：基础遍历类
```java
// 适用于：前序、中序、后序遍历
void traverse(TreeNode root) {
    if (root == null) return;
    // 前序：在这里处理
    traverse(root.left);
    // 中序：在这里处理  
    traverse(root.right);
    // 后序：在这里处理
}
```

### 模板2：信息收集类
```java
// 适用于：深度、和、最值计算
int collectInfo(TreeNode root) {
    if (root == null) return 默认值;
    int left = collectInfo(root.left);
    int right = collectInfo(root.right);
    return 处理函数(left, right, root.val);
}
```

### 模板3：路径回溯类
```java
// 适用于：路径收集、组合问题
void backtrack(TreeNode root, List<Integer> path, List<List<Integer>> result) {
    if (root == null) return;
    path.add(root.val);
    if (满足条件) result.add(new ArrayList<>(path));
    backtrack(root.left, path, result);
    backtrack(root.right, path, result);
    path.remove(path.size() - 1);
}
```

## 🔍 极端场景与边界处理

### 场景1：超大规模数据
```java
// 解决方案：迭代替代递归
void iterativeTraverse(TreeNode root) {
    Stack<TreeNode> stack = new Stack<>();
    TreeNode current = root;
    while (current != null || !stack.isEmpty()) {
        while (current != null) {
            // 前序处理
            stack.push(current);
            current = current.left;
        }
        current = stack.pop();
        // 中序处理
        current = current.right;
    }
}
```

### 场景2：内存限制严格
```java
// 解决方案：Morris遍历（O(1)空间）
void morrisInorder(TreeNode root) {
    TreeNode current = root;
    while (current != null) {
        if (current.left == null) {
            // 处理当前节点
            System.out.print(current.val + " ");
            current = current.right;
        } else {
            TreeNode predecessor = current.left;
            while (predecessor.right != null && predecessor.right != current) {
                predecessor = predecessor.right;
            }
            if (predecessor.right == null) {
                predecessor.right = current;
                current = current.left;
            } else {
                predecessor.right = null;
                // 处理当前节点
                System.out.print(current.val + " ");
                current = current.right;
            }
        }
    }
}
```

## 🚀 性能优化策略

### 策略1：记忆化优化
```java
Map<TreeNode, Integer> memo = new HashMap<>();

int optimizedDepth(TreeNode root) {
    if (root == null) return 0;
    if (memo.containsKey(root)) return memo.get(root);
    int depth = Math.max(optimizedDepth(root.left), optimizedDepth(root.right)) + 1;
    memo.put(root, depth);
    return depth;
}
```

### 策略2：提前剪枝
```java
boolean isBalanced(TreeNode root) {
    return checkHeight(root) != -1;
}

int checkHeight(TreeNode root) {
    if (root == null) return 0;
    int leftHeight = checkHeight(root.left);
    if (leftHeight == -1) return -1;  // 提前返回
    int rightHeight = checkHeight(root.right);
    if (rightHeight == -1) return -1;  // 提前返回
    if (Math.abs(leftHeight - rightHeight) > 1) return -1;
    return Math.max(leftHeight, rightHeight) + 1;
}
```

## 📝 单元测试设计

### 测试用例设计原则
1. **空树测试**：验证边界条件
2. **单节点树**：验证基础功能
3. **完全二叉树**：验证一般情况
4. **链状树**：验证最坏情况
5. **大规模数据**：验证性能

### 示例测试用例
```java
@Test
public void testMaxDepth() {
    // 空树
    assertEquals(0, maxDepth(null));
    
    // 单节点
    TreeNode single = new TreeNode(1);
    assertEquals(1, maxDepth(single));
    
    // 完全二叉树
    TreeNode balanced = buildBalancedTree();
    assertEquals(3, maxDepth(balanced));
    
    // 链状树（最坏情况）
    TreeNode skewed = buildSkewedTree();
    assertEquals(1000, maxDepth(skewed));
}
```

通过以上全面的题目覆盖和详细分析，相信你已经能够全面掌握二叉树递归遍历的各种技巧和应用场景。
