# Morris遍历算法详解与扩展题目

## 什么是Morris遍历

Morris遍历是一种二叉树遍历算法，由Joseph Morris在1979年提出。它能够在不使用递归或栈的情况下，仅使用O(1)的空间复杂度完成二叉树的遍历。

### 核心思想

Morris遍历利用了二叉树中大量存在的空指针（叶子节点的左右指针），通过临时修改这些指针来建立"线索"，从而在遍历过程中能够回溯到父节点，避免使用额外的存储空间。

### 算法步骤

1. 设置当前节点cur指向根节点
2. 当cur不为空时，执行以下操作：
   - 如果cur没有左子树：直接访问cur，然后移动到右子树
   - 如果cur有左子树：
     - 找到cur左子树中的最右节点（前驱节点）
     - 如果前驱节点的右指针为空：建立线索（前驱节点.right = cur），访问cur，移动到左子树
     - 如果前驱节点的右指针指向cur：断开线索（前驱节点.right = null），访问cur，移动到右子树

### 时间和空间复杂度

- 时间复杂度：O(n) - 每个节点最多被访问3次
- 空间复杂度：O(1) - 只使用常数额外空间

## 适用场景

1. 需要节省内存空间的环境
2. 不能修改树结构的场景（遍历后会恢复原状）
3. 面试中展示算法优化能力

## 算法优势

1. **空间效率**：O(1)的空间复杂度，相比递归和栈方法的O(n)更优
2. **结构完整性**：遍历结束后会恢复树的原始结构
3. **实现灵活**：可以实现前序、中序、后序等多种遍历方式
4. **适用性广**：可用于解决多种二叉树相关问题

## 算法劣势

1. **实现复杂**：相比递归实现，代码逻辑更复杂
2. **常数因子较大**：虽然时间复杂度为O(n)，但实际运行可能比递归慢
3. **不适合频繁修改的树**：需要频繁建立和断开线索

## 深入理解Morris遍历

### 如何判断节点的访问次数

在Morris遍历中，一个节点可能被访问一次或两次：
- 没有左子树的节点：只会被访问一次
- 有左子树的节点：会被访问两次
  - 第一次：建立线索时
  - 第二次：断开线索时

### 前序、中序、后序的区别

- **前序遍历**：在第一次访问节点时处理
- **中序遍历**：在第二次访问节点时处理（对于有左子树的节点）或第一次访问时处理（对于没有左子树的节点）
- **后序遍历**：在第二次访问节点时，处理其左子树的右边界，最后处理根节点

## 题目列表

### 基础遍历题目
1. [LeetCode 144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/) - 实现二叉树的前序遍历，要求空间复杂度O(1)
2. [LeetCode 94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/) - 实现二叉树的中序遍历，要求空间复杂度O(1)
3. [LeetCode 145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/) - 实现二叉树的后序遍历，要求空间复杂度O(1)
4. [HackerRank - Tree: Preorder Traversal](https://www.hackerrank.com/challenges/tree-preorder-traversal/problem) - HackerRank平台的前序遍历题目
5. [HackerRank - Tree: Inorder Traversal](https://www.hackerrank.com/challenges/tree-inorder-traversal/problem) - HackerRank平台的中序遍历题目
6. [HackerRank - Tree: Postorder Traversal](https://www.hackerrank.com/challenges/tree-postorder-traversal/problem) - HackerRank平台的后序遍历题目

### 二叉搜索树验证与操作题目
1. [LeetCode 98. 验证二叉搜索树](https://leetcode.cn/problems/validate-binary-search-tree/) - 使用Morris中序遍历验证二叉搜索树
2. [LeetCode 99. 恢复二叉搜索树](https://leetcode.cn/problems/recover-binary-search-tree/) - 恢复被错误交换两个节点的二叉搜索树
3. [LeetCode 173. 二叉搜索树迭代器](https://leetcode.cn/problems/binary-search-tree-iterator/) - 实现二叉搜索树的迭代器，要求O(1)空间
4. [LintCode 95. 验证二叉查找树](https://www.lintcode.com/problem/95/) - LintCode平台的二叉搜索树验证题目
5. [牛客网 - 验证二叉搜索树](https://www.nowcoder.com/practice/a69242b39baf45dea217815c7dedb52b) - 牛客网平台的二叉搜索树验证题目

### 二叉搜索树统计与转换题目
1. [LeetCode 501. 二叉搜索树中的众数](https://leetcode.cn/problems/find-mode-in-binary-search-tree/) - 找出二叉搜索树中出现次数最多的元素
2. [LeetCode 530. 二叉搜索树的最小绝对差](https://leetcode.cn/problems/minimum-absolute-difference-in-bst/) - 找出二叉搜索树中任意两节点的最小绝对差
3. [LeetCode 538. 把二叉搜索树转换为累加树](https://leetcode.cn/problems/convert-bst-to-greater-tree/) - 将二叉搜索树转换为累加树
4. [LeetCode 230. 二叉搜索树中第K小的元素](https://leetcode.cn/problems/kth-smallest-element-in-a-bst/) - 找到二叉搜索树中第K小的元素
5. [AcWing 44. 二叉搜索树的第k大节点](https://www.acwing.com/problem/content/44/) - AcWing平台的二叉搜索树第K大节点题目

### 路径与和相关题目
1. [LeetCode 129. 求根到叶子节点数字之和](https://leetcode.cn/problems/sum-root-to-leaf-numbers/) - 计算从根到叶子节点生成的所有数字之和
2. [LeetCode 257. 二叉树的所有路径](https://leetcode.cn/problems/binary-tree-paths/) - 输出二叉树中所有从根到叶子的路径
3. [LeetCode 113. 路径总和 II](https://leetcode.cn/problems/path-sum-ii/) - 找出所有从根到叶子节点路径总和等于给定目标和的路径
4. [剑指Offer 34. 二叉树中和为某一值的路径](https://leetcode.cn/problems/er-cha-shu-zhong-he-wei-mou-yi-zhi-de-lu-jing-lcof/) - 剑指Offer中的路径总和问题

### 树的属性与结构题目
1. [LeetCode 111. 二叉树的最小深度](https://leetcode.cn/problems/minimum-depth-of-binary-tree/) - 计算二叉树的最小深度
2. [LeetCode 236. 二叉树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-tree/) - 查找二叉树中两个节点的最近公共祖先
3. [LeetCode 104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/) - 计算二叉树的最大深度
4. [洛谷 P1305 新二叉树](https://www.luogu.com.cn/problem/P1305) - 洛谷平台的二叉树遍历题目
5. [UVa 548 - Tree](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=489) - UVa平台的二叉树相关题目

### 其他平台扩展题目
1. [Codeforces 438D - The Child and Sequence](https://codeforces.com/problemset/problem/438/D) - 可使用Morris遍历思想优化的区间操作问题
2. [AtCoder ABC091 C - 2D Plane 2N Points](https://atcoder.jp/contests/abc091/tasks/arc092_a) - 涉及树结构优化的问题
3. [SPOJ TREEORD - Tree Order](https://www.spoj.com/problems/TREEORD/) - SPOJ平台的树遍历顺序问题
4. [杭电OJ 1026 - Ignatius and the Princess I](https://acm.hdu.edu.cn/showproblem.php?pid=1026) - 可应用Morris遍历思想的搜索问题
5. [计蒜客 - 二叉树遍历](https://nanti.jisuanke.com/t/41093) - 计蒜客平台的二叉树遍历题目
6. [MarsCode - Binary Tree Traversal](https://www.mars.codeforces.com/problemset/problem/102/B) - MarsCode平台的二叉树遍历题目
7. [TimusOJ 1602 - Cabriolet](https://acm.timus.ru/problem.aspx?space=1&num=1602) - 可应用树遍历思想的问题
8. [AizuOJ ALDS1_7_B - Tree](https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_B) - AizuOJ平台的树结构问题
9. [Comet OJ C1013 - 二叉树的遍历](https://cometoj.com/contest/34/problem/C1013?problem_id=473) - Comet OJ平台的二叉树遍历题目
10. [POJ 3667 - Hotel](https://poj.org/problem?id=3667) - 可使用类似Morris遍历思想的线段树问题

### 补充题目列表
1. [LeetCode 100. 相同的树](https://leetcode.cn/problems/same-tree/) - 判断两个二叉树是否相同
2. [LeetCode 101. 对称二叉树](https://leetcode.cn/problems/symmetric-tree/) - 判断二叉树是否对称
3. [LeetCode 102. 二叉树的层序遍历](https://leetcode.cn/problems/binary-tree-level-order-traversal/) - 二叉树的层序遍历
4. [LeetCode 103. 二叉树的锯齿形层序遍历](https://leetcode.cn/problems/binary-tree-zigzag-level-order-traversal/) - 二叉树的锯齿形层序遍历
5. [LeetCode 105. 从前序与中序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-preorder-and-inorder-traversal/) - 根据前序和中序遍历构造二叉树
6. [LeetCode 106. 从中序与后序遍历序列构造二叉树](https://leetcode.cn/problems/construct-binary-tree-from-inorder-and-postorder-traversal/) - 根据中序和后序遍历构造二叉树
7. [LeetCode 107. 二叉树的层序遍历 II](https://leetcode.cn/problems/binary-tree-level-order-traversal-ii/) - 自底向上的层序遍历
8. [LeetCode 108. 将有序数组转换为二叉搜索树](https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/) - 将有序数组转换为高度平衡的二叉搜索树
9. [LeetCode 110. 平衡二叉树](https://leetcode.cn/problems/balanced-binary-tree/) - 判断二叉树是否为平衡二叉树
10. [LeetCode 112. 路径总和](https://leetcode.cn/problems/path-sum/) - 判断二叉树中是否存在根节点到叶子节点的路径总和等于目标值
11. [LeetCode 114. 二叉树展开为链表](https://leetcode.cn/problems/flatten-binary-tree-to-linked-list/) - 将二叉树展开为单链表
12. [LeetCode 116. 填充每个节点的下一个右侧节点指针](https://leetcode.cn/problems/populating-next-right-pointers-in-each-node/) - 填充完美二叉树的next指针
13. [LeetCode 117. 填充每个节点的下一个右侧节点指针 II](https://leetcode.cn/problems/populating-next-right-pointers-in-each-node-ii/) - 填充任意二叉树的next指针
14. [LeetCode 124. 二叉树中的最大路径和](https://leetcode.cn/problems/binary-tree-maximum-path-sum/) - 找到二叉树中的最大路径和
15. [LeetCode 199. 二叉树的右视图](https://leetcode.cn/problems/binary-tree-right-side-view/) - 二叉树的右视图
16. [LeetCode 222. 完全二叉树的节点个数](https://leetcode.cn/problems/count-complete-tree-nodes/) - 计算完全二叉树的节点个数
17. [LeetCode 226. 翻转二叉树](https://leetcode.cn/problems/invert-binary-tree/) - 翻转二叉树
18. [LeetCode 235. 二叉搜索树的最近公共祖先](https://leetcode.cn/problems/lowest-common-ancestor-of-a-binary-search-tree/) - 二叉搜索树的最近公共祖先
19. [LeetCode 297. 二叉树的序列化与反序列化](https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/) - 二叉树的序列化与反序列化
20. [LeetCode 404. 左叶子之和](https://leetcode.cn/problems/sum-of-left-leaves/) - 计算左叶子节点的和
21. [LeetCode 437. 路径总和 III](https://leetcode.cn/problems/path-sum-iii/) - 计算路径总和III
22. [LeetCode 543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/) - 计算二叉树的直径
23. [LeetCode 563. 二叉树的坡度](https://leetcode.cn/problems/binary-tree-tilt/) - 计算二叉树的坡度
24. [LeetCode 572. 另一棵树的子树](https://leetcode.cn/problems/subtree-of-another-tree/) - 判断是否为另一棵树的子树
25. [LeetCode 617. 合并二叉树](https://leetcode.cn/problems/merge-two-binary-trees/) - 合并两棵二叉树
26. [LeetCode 637. 二叉树的层平均值](https://leetcode.cn/problems/average-of-levels-in-binary-tree/) - 计算二叉树每层的平均值
27. [LeetCode 654. 最大二叉树](https://leetcode.cn/problems/maximum-binary-tree/) - 构造最大二叉树
28. [LeetCode 669. 修剪二叉搜索树](https://leetcode.cn/problems/trim-a-binary-search-tree/) - 修剪二叉搜索树
29. [LeetCode 700. 二叉搜索树中的搜索](https://leetcode.cn/problems/search-in-a-binary-search-tree/) - 在二叉搜索树中搜索节点
30. [LeetCode 701. 二叉搜索树中的插入操作](https://leetcode.cn/problems/insert-into-a-binary-search-tree/) - 在二叉搜索树中插入节点

## 更多以Morris遍历为最优解的题目

### 高级应用题目
1. [LeetCode 96. 不同的二叉搜索树](https://leetcode.cn/problems/unique-binary-search-trees/) - 计算不同二叉搜索树的个数（可结合Morris遍历优化）
2. [LeetCode 95. 不同的二叉搜索树 II](https://leetcode.cn/problems/unique-binary-search-trees-ii/) - 生成所有不同的二叉搜索树
3. [LeetCode 173. 二叉搜索树迭代器](https://leetcode.cn/problems/binary-search-tree-iterator/) - 实现BST迭代器（Morris遍历实现空间最优）
4. [LeetCode 285. 二叉搜索树中的中序后继](https://leetcode.cn/problems/inorder-successor-in-bst/) - 找到BST中指定节点的中序后继
5. [LeetCode 510. 二叉搜索树中的中序后继 II](https://leetcode.cn/problems/inorder-successor-in-bst-ii/) - 在有父指针的BST中找中序后继
6. [LeetCode 270. 最接近的二叉搜索树值](https://leetcode.cn/problems/closest-binary-search-tree-value/) - 找到BST中最接近目标值的节点
7. [LeetCode 272. 最接近的二叉搜索树值 II](https://leetcode.cn/problems/closest-binary-search-tree-value-ii/) - 找到BST中k个最接近目标值的节点
8. [LeetCode 333. 最大BST子树](https://leetcode.cn/problems/largest-bst-subtree/) - 找到二叉树中最大的BST子树
9. [LeetCode 450. 删除二叉搜索树中的节点](https://leetcode.cn/problems/delete-node-in-a-bst/) - 删除BST中的节点
10. [LeetCode 703. 数据流中的第K大元素](https://leetcode.cn/problems/kth-largest-element-in-a-stream/) - 数据流中的第K大元素（可结合BST思想）

### 牛客网题目
1. [牛客网 - 二叉树的中序遍历](https://www.nowcoder.com/practice/0bf071c135e64ee2a027783b80bf781d) - 牛客网平台的二叉树中序遍历题目
2. [牛客网 - 二叉树遍历](https://www.nowcoder.com/practice/4b91205483694f449f94c179883c1fef) - 牛客网平台的二叉树遍历题目
3. [牛客网 - 求二叉树的层序遍历](https://www.nowcoder.com/practice/04a5560e43e24e9db4595865dc9c63a3) - 牛客网平台的二叉树层序遍历题目
4. [牛客网 - 二叉搜索树中第K小的元素](https://www.nowcoder.com/practice/ef068f602dde4d28aab4beb7d3c38f79) - 牛客网平台的BST第K小元素题目

### 洛谷题目
1. [洛谷 P1305 新二叉树](https://www.luogu.com.cn/problem/P1305) - 洛谷平台的二叉树遍历题目
2. [洛谷 P1030 [NOIP2001 普及组] 求先序排列](https://www.luogu.com.cn/problem/P1030) - 根据中序和后序求先序排列
3. [洛谷 P1827 [USACO3.4] 美国血统 American Heritage](https://www.luogu.com.cn/problem/P1827) - 根据中序和前序构造二叉树
4. [洛谷 P1304 姐妹省市](https://www.luogu.com.cn/problem/P1304) - 二叉树相关问题

### Codeforces题目
1. [Codeforces 438D - The Child and Sequence](https://codeforces.com/problemset/problem/438/D) - 可使用Morris遍历思想优化的区间操作问题
2. [Codeforces 620E - New Year Tree](https://codeforces.com/problemset/problem/620/E) - 树的染色问题
3. [Codeforces 763A - Timofey and a tree](https://codeforces.com/problemset/problem/763/A) - 树的重构问题

### AtCoder题目
1. [AtCoder ABC091 C - 2D Plane 2N Points](https://atcoder.jp/contests/abc091/tasks/arc092_a) - 涉及树结构优化的问题
2. [AtCoder ABC133 F - Colorful Tree](https://atcoder.jp/contests/abc133/tasks/abc133_f) - 树上路径颜色查询问题

### SPOJ题目
1. [SPOJ TREEORD - Tree Order](https://www.spoj.com/problems/TREEORD/) - SPOJ平台的树遍历顺序问题
2. [SPOJ QTREE - Query on a tree](https://www.spoj.com/problems/QTREE/) - 树上路径查询问题

### 其他平台题目
1. [HackerRank - Is This a Binary Search Tree?](https://www.hackerrank.com/challenges/is-binary-search-tree/problem) - 验证BST
2. [HackerRank - Swap Nodes](https://www.hackerrank.com/challenges/swap-nodes-algo/problem) - 交换节点问题
3. [UVa 122 - Trees on the level](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=58) - 层级树构建问题
4. [POJ 2255 - Tree Recovery](http://poj.org/problem?id=2255) - 根据前序和中序恢复二叉树
5. [ZOJ 1086 - Octal Fractions](https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365585) - 二叉树相关问题
6. [计蒜客 - 二叉树遍历](https://nanti.jisuanke.com/t/41093) - 计蒜客平台的二叉树遍历题目
7. [杭电OJ 1710 - Binary Tree Traversals](https://acm.hdu.edu.cn/showproblem.php?pid=1710) - 根据前序和中序构造二叉树
8. [AizuOJ ALDS1_7_C - Tree Walk](https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_C) - 树的遍历问题

## 如何选择Morris遍历

在解决二叉树问题时，以下情况适合使用Morris遍历：

1. **空间受限环境**：当内存资源有限，无法使用O(n)空间的递归或栈方法时
2. **大规模数据**：处理非常大的树时，Morris遍历的空间优势更加明显
3. **单次遍历**：只需要遍历一次树就能完成的操作
4. **面试展示**：在面试中展示对高级算法的掌握，体现优化意识

## 代码优化建议

1. **边界情况处理**：始终处理空树、单节点树等边界情况
2. **线索恢复**：确保遍历结束后恢复树的原始结构
3. **变量命名**：使用清晰的变量名，如cur、mostRight等，提高代码可读性
4. **模块化**：将Morris遍历的核心逻辑封装成单独的函数，便于复用
5. **注释完善**：添加详细注释，解释算法原理和关键步骤

## 与其他遍历方法的比较

| 遍历方法 | 时间复杂度 | 空间复杂度 | 实现难度 | 适用场景 |
|---------|-----------|-----------|---------|----------|
| 递归遍历 | O(n) | O(h)，h为树高 | 简单 | 大多数场景，代码简洁 |
| 栈遍历 | O(n) | O(h)，h为树高 | 中等 | 不允许递归的场景 |
| Morris遍历 | O(n) | O(1) | 复杂 | 空间受限的场景，面试展示 |

## 总结

Morris遍历是一种巧妙的二叉树遍历算法，通过利用树中的空指针建立线索，实现了O(1)空间复杂度的二叉树遍历。虽然实现较为复杂，但在空间受限的场景下具有显著优势。掌握Morris遍历不仅有助于解决特定的算法问题，也能培养对算法优化的深入理解。

在实际应用中，我们需要根据具体问题的要求，权衡代码复杂度和空间效率，选择最适合的遍历方法。对于大多数日常编程任务，递归或栈遍历可能更为实用；但在面试或特定的性能优化场景中，Morris遍历无疑是展示算法深度的绝佳选择。