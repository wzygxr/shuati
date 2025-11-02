# Morris遍历算法总结

## 算法核心思想

Morris遍历是一种高效的二叉树遍历算法，其核心思想是利用树中空闲的指针来建立线索，从而避免使用额外的栈或递归空间。

## 算法特点

### 优势
1. **空间复杂度O(1)**：不使用递归或栈，仅使用常数额外空间
2. **时间复杂度O(n)**：每个节点最多访问3次
3. **原地算法**：遍历过程中会临时修改树结构，但最终会恢复

### 劣势
1. **实现复杂**：相比递归和迭代实现更加复杂
2. **不适用于并发环境**：遍历过程中会修改树结构
3. **仅适用于遍历**：对于需要复杂信息聚合的问题不适用

## 三种遍历方式的Morris实现

### 前序遍历
```java
public static void morrisPreorder(TreeNode head, List<Integer> ans) {
    TreeNode cur = head;
    TreeNode mostRight = null;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            while (mostRight.right != null && mostRight.right != cur) {
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) {
                ans.add(cur.val);  // 第一次到达就打印
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else {
                mostRight.right = null;
            }
        } else {
            ans.add(cur.val);  // 没有左子树直接打印
        }
        cur = cur.right;
    }
}
```

### 中序遍历
```java
public static void morrisInorder(TreeNode head, List<Integer> ans) {
    TreeNode cur = head;
    TreeNode mostRight = null;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            while (mostRight.right != null && mostRight.right != cur) {
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) {
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else {
                mostRight.right = null;
            }
        }
        ans.add(cur.val);  // 第二次到达才打印
        cur = cur.right;
    }
}
```

### 后序遍历
```java
public static void morrisPostorder(TreeNode head, List<Integer> ans) {
    TreeNode cur = head;
    TreeNode mostRight = null;
    while (cur != null) {
        mostRight = cur.left;
        if (mostRight != null) {
            while (mostRight.right != null && mostRight.right != cur) {
                mostRight = mostRight.right;
            }
            if (mostRight.right == null) {
                mostRight.right = cur;
                cur = cur.left;
                continue;
            } else {
                mostRight.right = null;
                collect(cur.left, ans);  // 收集左子树右边界逆序
            }
        }
        cur = cur.right;
    }
    collect(head, ans);  // 收集整棵树右边界逆序
}
```

## 适用场景总结

### 适合使用Morris遍历的场景
1. **内存受限环境**：无法使用O(h)空间的递归或栈实现
2. **需要线性遍历**：如验证BST、找最小深度等
3. **面试展示**：展示对算法优化的深入理解

### 不适合使用Morris遍历的场景
1. **需要复杂信息聚合**：如最大路径和、树的直径等
2. **并发环境**：遍历过程中会修改树结构
3. **需要保持树结构不变**：某些场景下不能临时修改树结构

## 语言特性差异

### Java
- 对象引用机制便于线索的建立和断开
- 异常处理机制完善
- 垃圾回收机制无需手动管理内存

### Python
- 动态类型系统，代码更简洁
- 列表推导式便于结果收集
- 无显式指针操作，通过属性访问实现

### C++
- 指针操作更直接
- 需要手动管理内存
- 性能通常优于解释型语言

## 工程化考量

### 异常处理
1. 空树和单节点树的边界情况处理
2. 输入参数的有效性检查
3. 遍历过程中节点指针的正确性维护

### 性能优化
1. 避免重复计算最右节点
2. 合理安排节点访问顺序
3. 及时断开线索避免死循环

### 可维护性
1. 详细注释说明算法每一步的作用
2. 变量命名清晰表达其含义
3. 代码结构模块化，便于理解和修改

## 常见问题和解决方案

### 1. 线索未正确断开导致死循环
**问题**：在遍历过程中，如果线索没有正确断开，可能导致无限循环。
**解决方案**：确保每次建立线索后，在第二次到达时正确断开。

### 2. 遍历顺序错误
**问题**：前序、中序、后序遍历的访问时机容易混淆。
**解决方案**：明确每种遍历方式的访问时机，第一次还是第二次到达节点时访问。

### 3. 结果收集错误（后序遍历）
**问题**：后序遍历需要逆序收集右边界，容易出错。
**解决方案**：使用链表翻转技术，先翻转再收集，最后再翻转恢复。

## 扩展应用

### 1. 验证BST
通过中序遍历检查序列是否有序

### 2. 找最小深度
通过遍历过程计算节点所在层数

### 3. 找最近公共祖先
结合遍历过程中的节点访问顺序

### 4. 恢复BST
通过遍历找到逆序对并修复

### 5. BST迭代器
利用Morris遍历的暂停和恢复特性

### 6. 找BST中的众数
利用BST中序遍历的有序性，统计相同元素的出现次数

### 7. 求根到叶节点数字之和
在Morris前序遍历过程中维护路径数字，到达叶节点时累加

### 8. BST转换为累加树
通过Morris反向中序遍历（右-根-左）维护累加和

### 9. BST的最小绝对差
利用BST中序遍历的有序性，计算相邻节点值的最小差值

## 更多相关题目

### 基础遍历题目
1. [LeetCode 144. 二叉树的前序遍历](https://leetcode.cn/problems/binary-tree-preorder-traversal/)
2. [LeetCode 94. 二叉树的中序遍历](https://leetcode.cn/problems/binary-tree-inorder-traversal/)
3. [LeetCode 145. 二叉树的后序遍历](https://leetcode.cn/problems/binary-tree-postorder-traversal/)

### 二叉搜索树相关题目
1. [LeetCode 98. 验证二叉搜索树](https://leetcode.cn/problems/validate-binary-search-tree/)
2. [LeetCode 501. 二叉搜索树中的众数](https://leetcode.cn/problems/find-mode-in-binary-search-tree/)
3. [LeetCode 530. 二叉搜索树的最小绝对差](https://leetcode.cn/problems/minimum-absolute-difference-in-bst/)
4. [LeetCode 538. 把二叉搜索树转换为累加树](https://leetcode.cn/problems/convert-bst-to-greater-tree/)
5. [LeetCode 230. 二叉搜索树中第K小的元素](https://leetcode.cn/problems/kth-smallest-element-in-a-bst/)

### 路径与和相关题目
1. [LeetCode 129. 求根到叶子节点数字之和](https://leetcode.cn/problems/sum-root-to-leaf-numbers/)
2. [LeetCode 257. 二叉树的所有路径](https://leetcode.cn/problems/binary-tree-paths/)
3. [LeetCode 113. 路径总和 II](https://leetcode.cn/problems/path-sum-ii/)
4. [LeetCode 112. 路径总和](https://leetcode.cn/problems/path-sum/)
5. [LeetCode 437. 路径总和 III](https://leetcode.cn/problems/path-sum-iii/)

### 树的属性与结构题目
1. [LeetCode 111. 二叉树的最小深度](https://leetcode.cn/problems/minimum-depth-of-binary-tree/)
2. [LeetCode 104. 二叉树的最大深度](https://leetcode.cn/problems/maximum-depth-of-binary-tree/)
3. [LeetCode 110. 平衡二叉树](https://leetcode.cn/problems/balanced-binary-tree/)
4. [LeetCode 543. 二叉树的直径](https://leetcode.cn/problems/diameter-of-binary-tree/)
5. [LeetCode 222. 完全二叉树的节点个数](https://leetcode.cn/problems/count-complete-tree-nodes/)

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

### 其他平台扩展题目
1. [Codeforces 438D - The Child and Sequence](https://codeforces.com/problemset/problem/438/D)
2. [AtCoder ABC091 C - 2D Plane 2N Points](https://atcoder.jp/contests/abc091/tasks/arc092_a)
3. [SPOJ TREEORD - Tree Order](https://www.spoj.com/problems/TREEORD/)
4. [杭电OJ 1026 - Ignatius and the Princess I](https://acm.hdu.edu.cn/showproblem.php?pid=1026)
5. [计蒜客 - 二叉树遍历](https://nanti.jisuanke.com/t/41093)
6. [MarsCode - Binary Tree Traversal](https://www.mars.codeforces.com/problemset/problem/102/B)
7. [TimusOJ 1602 - Cabriolet](https://acm.timus.ru/problem.aspx?space=1&num=1602)
8. [AizuOJ ALDS1_7_B - Tree](https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_B)
9. [Comet OJ C1013 - 二叉树的遍历](https://cometoj.com/contest/34/problem/C1013?problem_id=473)
10. [POJ 3667 - Hotel](https://poj.org/problem?id=3667)
11. [HackerRank - Is This a Binary Search Tree?](https://www.hackerrank.com/challenges/is-binary-search-tree/problem) - 验证BST
12. [HackerRank - Swap Nodes](https://www.hackerrank.com/challenges/swap-nodes-algo/problem) - 交换节点问题
13. [UVa 122 - Trees on the level](https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=58) - 层级树构建问题
14. [POJ 2255 - Tree Recovery](http://poj.org/problem?id=2255) - 根据前序和中序恢复二叉树
15. [ZOJ 1086 - Octal Fractions](https://zoj.pintia.cn/problem-sets/91827364500/problems/91827365585) - 二叉树相关问题
16. [计蒜客 - 二叉树遍历](https://nanti.jisuanke.com/t/41093) - 计蒜客平台的二叉树遍历题目
17. [杭电OJ 1710 - Binary Tree Traversals](https://acm.hdu.edu.cn/showproblem.php?pid=1710) - 根据前序和中序构造二叉树
18. [AizuOJ ALDS1_7_C - Tree Walk](https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_7_C) - 树的遍历问题

## 总结

Morris遍历是一种非常巧妙的算法，它通过利用树中空闲的指针来实现O(1)空间复杂度的遍历。虽然实现相对复杂，但在特定场景下具有显著优势。掌握Morris遍历不仅有助于解决特定问题，更能加深对二叉树结构和遍历算法的理解。