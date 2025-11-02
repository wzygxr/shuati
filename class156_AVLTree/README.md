# Class148: AVL树与平衡二叉搜索树

## 概述

AVL树是一种自平衡二叉搜索树，由Adelson-Velskii和Landis在1962年提出。在AVL树中，任何节点的两个子树的高度差最多为1，这保证了树的操作时间复杂度为O(log n)。

## 核心概念

### 1. 平衡因子
每个节点的平衡因子是其左子树高度减去右子树高度的值，平衡因子只能是-1、0或1。

### 2. 旋转操作
当插入或删除节点导致树失衡时，需要通过旋转操作来重新平衡树：
- **LL旋转**：在左孩子的左子树插入导致失衡
- **RR旋转**：在右孩子的右子树插入导致失衡
- **LR旋转**：在左孩子的右子树插入导致失衡
- **RL旋转**：在右孩子的左子树插入导致失衡

## 支持的操作

1. **插入**：O(log n)
2. **删除**：O(log n)
3. **查找**：O(log n)
4. **查询排名**：O(log n)
5. **查询第k小元素**：O(log n)
6. **查询前驱**：O(log n)
7. **查询后继**：O(log n)

## 实现文件

### Java实现
- [Code01_AVL1.java](Code01_AVL1.java) - 基础AVL树实现
- [Code02_ReconstructionQueue.java](Code02_ReconstructionQueue.java) - 重建队列应用
- [FollowUp1.java](FollowUp1.java) - 数据加强版实现

### C++实现
- [Code01_AVL2.java](Code01_AVL2.java) - 基础AVL树实现（C++版注释）
- [FollowUp2.java](FollowUp2.java) - 数据加强版实现（C++版注释）
- [Code01_AVL.cpp](Code01_AVL.cpp) - 基础AVL树实现（C++简化版）
- [Code02_ReconstructionQueue.cpp](Code02_ReconstructionQueue.cpp) - 重建队列应用（C++简化版）
- [FollowUp1.cpp](FollowUp1.cpp) - 数据加强版实现（C++简化版）

### Python实现
- [Code01_AVL.py](Code01_AVL.py) - 基础AVL树实现
- [Code02_ReconstructionQueue.py](Code02_ReconstructionQueue.py) - 重建队列应用
- [FollowUp1.py](FollowUp1.py) - 数据加强版实现
- [FollowUp2.py](FollowUp2.py) - 数据加强版实现（重复文件，用于说明）

### 文档文件
- [README.md](README.md) - 本文件
- [补充题目.md](补充题目.md) - 题目汇总和分类
- [总结.md](总结.md) - 完整内容总结

## 补充题目列表

### 1. 洛谷 P3369 【模板】普通平衡树
- **链接**: https://www.luogu.com.cn/problem/P3369
- **题目描述**: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 2. 洛谷 P6136 【模板】普通平衡树（数据加强版）
- **链接**: https://www.luogu.com.cn/problem/P6136
- **题目描述**: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 3. LeetCode 406. Queue Reconstruction by Height
- **链接**: https://leetcode.cn/problems/queue-reconstruction-by-height/
- **题目描述**: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 4. PAT甲级 1066 Root of AVL Tree
- **链接**: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
- **题目描述**: 给定插入序列，构建AVL树，输出根节点的值
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 5. PAT甲级 1123 Is It a Complete AVL Tree
- **链接**: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
- **题目描述**: 判断构建的AVL树是否是完全二叉树
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 6. LeetCode 220. Contains Duplicate III
- **链接**: https://leetcode.cn/problems/contains-duplicate-iii/
- **题目描述**: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
- **时间复杂度**: O(n log k)
- **空间复杂度**: O(k)

### 7. Codeforces 459D - Pashmak and Parmida's problem
- **链接**: https://codeforces.com/problemset/problem/459/D
- **题目描述**: 计算满足条件的点对数量
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 8. SPOJ Ada and Behives
- **链接**: https://www.spoj.com/problems/ADAAPHID/
- **题目描述**: 维护一个动态集合，支持插入和查询操作
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 9. HackerRank Self-Balancing Tree
- **链接**: https://www.hackerrank.com/challenges/self-balancing-tree/problem
- **题目描述**: 实现AVL树的插入操作
- **时间复杂度**: O(log n)
- **空间复杂度**: O(n)

### 10. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
- **链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
- **题目描述**: 字符串处理问题，可使用平衡树优化
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 11. CodeChef ORDERSET
- **链接**: https://www.codechef.com/problems/ORDERSET
- **题目描述**: 维护有序集合，支持插入、删除、查询排名、查询第k小
- **时间复杂度**: O(log n)
- **空间复杂度**: O(n)

### 12. AtCoder ABC134 E - Sequence Decomposing
- **链接**: https://atcoder.jp/contests/abc134/tasks/abc134_e
- **题目描述**: 序列分解问题，可使用平衡树优化
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 13. 牛客网 NC145 01序列的最小权值
- **链接**: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
- **题目描述**: 维护01序列，支持插入和查询操作
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 14. ZOJ 1659 Mobile Phone Coverage
- **链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
- **题目描述**: 计算矩形覆盖面积，可使用平衡树维护
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 15. POJ 1864 [NOI2009] 二叉查找树
- **链接**: http://poj.org/problem?id=1864
- **题目描述**: 二叉查找树的动态规划问题
- **时间复杂度**: O(n^2)
- **空间复杂度**: O(n)

### 16. LeetCode 98. 验证二叉搜索树
- **链接**: https://leetcode.cn/problems/validate-binary-search-tree/
- **题目描述**: 验证一个二叉树是否是有效的二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)，h为树高

### 17. LeetCode 669. 修剪二叉搜索树
- **链接**: https://leetcode.cn/problems/trim-a-binary-search-tree/
- **题目描述**: 修剪二叉搜索树，保留值在[low, high]范围内的节点
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 18. LeetCode 700. 二叉搜索树中的搜索
- **链接**: https://leetcode.cn/problems/search-in-a-binary-search-tree/
- **题目描述**: 在二叉搜索树中搜索特定值
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 19. LeetCode 701. 二叉搜索树中的插入操作
- **链接**: https://leetcode.cn/problems/insert-into-a-binary-search-tree/
- **题目描述**: 在二叉搜索树中插入新节点
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 20. LeetCode 450. 删除二叉搜索树中的节点
- **链接**: https://leetcode.cn/problems/delete-node-in-a-bst/
- **题目描述**: 在二叉搜索树中删除指定节点
- **时间复杂度**: O(log n)
- **空间复杂度**: O(h)

### 21. LeetCode 230. 二叉搜索树中第K小的元素
- **链接**: https://leetcode.cn/problems/kth-smallest-element-in-a-bst/
- **题目描述**: 在二叉搜索树中查找第k小的元素
- **时间复杂度**: O(k)
- **空间复杂度**: O(h)

### 22. LeetCode 538. 把二叉搜索树转换为累加树
- **链接**: https://leetcode.cn/problems/convert-bst-to-greater-tree/
- **题目描述**: 将二叉搜索树转换为累加树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 23. LeetCode 108. 将有序数组转换为二叉搜索树
- **链接**: https://leetcode.cn/problems/convert-sorted-array-to-binary-search-tree/
- **题目描述**: 将有序数组转换为高度平衡的二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(log n)

### 24. LeetCode 109. 有序链表转换二叉搜索树
- **链接**: https://leetcode.cn/problems/convert-sorted-list-to-binary-search-tree/
- **题目描述**: 将有序链表转换为高度平衡的二叉搜索树
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(log n)

### 25. LeetCode 173. 二叉搜索树迭代器
- **链接**: https://leetcode.cn/problems/binary-search-tree-iterator/
- **题目描述**: 实现二叉搜索树的迭代器
- **时间复杂度**: O(1) 平均每次操作
- **空间复杂度**: O(h)

### 26. LeetCode 285. 二叉搜索树中的中序后继
- **链接**: https://leetcode.cn/problems/inorder-successor-in-bst/
- **题目描述**: 在二叉搜索树中查找指定节点的中序后继
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 27. LeetCode 510. 二叉搜索树中的中序后继 II
- **链接**: https://leetcode.cn/problems/inorder-successor-in-bst-ii/
- **题目描述**: 在带父指针的二叉搜索树中查找中序后继
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 28. LeetCode 272. 最接近的二叉搜索树值 II
- **链接**: https://leetcode.cn/problems/closest-binary-search-tree-value-ii/
- **题目描述**: 在二叉搜索树中查找最接近目标值的k个节点
- **时间复杂度**: O(k + log n)
- **空间复杂度**: O(k + h)

### 29. LeetCode 270. 最接近的二叉搜索树值
- **链接**: https://leetcode.cn/problems/closest-binary-search-tree-value/
- **题目描述**: 在二叉搜索树中查找最接近目标值的节点
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 30. LeetCode 653. 两数之和 IV - 输入二叉搜索树
- **链接**: https://leetcode.cn/problems/two-sum-iv-input-is-a-bst/
- **题目描述**: 在二叉搜索树中查找是否存在两个节点值之和等于目标值
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 31. LeetCode 1305. 两棵二叉搜索树中的所有元素
- **链接**: https://leetcode.cn/problems/all-elements-in-two-binary-search-trees/
- **题目描述**: 合并两棵二叉搜索树中的所有元素
- **时间复杂度**: O(n + m)
- **空间复杂度**: O(h1 + h2)

### 32. LeetCode 1382. 将二叉搜索树变平衡
- **链接**: https://leetcode.cn/problems/balance-a-binary-search-tree/
- **题目描述**: 将任意二叉搜索树转换为平衡二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 33. LeetCode 99. 恢复二叉搜索树
- **链接**: https://leetcode.cn/problems/recover-binary-search-tree/
- **题目描述**: 恢复被错误交换两个节点的二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 34. LeetCode 333. 最大BST子树
- **链接**: https://leetcode.cn/problems/largest-bst-subtree/
- **题目描述**: 在二叉树中查找最大的二叉搜索子树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 35. LeetCode 426. 将二叉搜索树转化为排序的双向链表
- **链接**: https://leetcode.cn/problems/convert-binary-search-tree-to-sorted-doubly-linked-list/
- **题目描述**: 将二叉搜索树转换为排序的双向循环链表
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 36. LeetCode 449. 序列化和反序列化二叉搜索树
- **链接**: https://leetcode.cn/problems/serialize-and-deserialize-bst/
- **题目描述**: 序列化和反序列化二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 37. LeetCode 501. 二叉搜索树中的众数
- **链接**: https://leetcode.cn/problems/find-mode-in-binary-search-tree/
- **题目描述**: 在二叉搜索树中查找出现次数最多的值
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 38. LeetCode 530. 二叉搜索树的最小绝对差
- **链接**: https://leetcode.cn/problems/minimum-absolute-difference-in-bst/
- **题目描述**: 在二叉搜索树中查找任意两节点值的最小绝对差
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 39. LeetCode 783. 二叉搜索树节点最小距离
- **链接**: https://leetcode.cn/problems/minimum-distance-between-bst-nodes/
- **题目描述**: 在二叉搜索树中查找任意两节点值的最小距离
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 40. LeetCode 897. 递增顺序搜索树
- **链接**: https://leetcode.cn/problems/increasing-order-search-tree/
- **题目描述**: 将二叉搜索树重新排列成只有右节点的递增顺序树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 41. LeetCode 938. 二叉搜索树的范围和
- **链接**: https://leetcode.cn/problems/range-sum-of-bst/
- **题目描述**: 计算二叉搜索树中在给定范围内的所有节点值之和
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 42. LeetCode 965. 单值二叉树
- **链接**: https://leetcode.cn/problems/univalued-binary-tree/
- **题目描述**: 判断二叉树是否所有节点值都相同
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 43. LeetCode 1008. 前序遍历构造二叉搜索树
- **链接**: https://leetcode.cn/problems/construct-binary-search-tree-from-preorder-traversal/
- **题目描述**: 根据前序遍历结果构造二叉搜索树
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 44. LeetCode 1038. 从二叉搜索树到更大和树
- **链接**: https://leetcode.cn/problems/binary-search-tree-to-greater-sum-tree/
- **题目描述**: 将二叉搜索树转换为更大和树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 45. LeetCode 1214. 查找两棵二叉搜索树之和
- **链接**: https://leetcode.cn/problems/two-sum-bsts/
- **题目描述**: 在两棵二叉搜索树中查找是否存在两个节点值之和等于目标值
- **时间复杂度**: O(n + m)
- **空间复杂度**: O(h1 + h2)

### 46. LeetCode 1373. 二叉搜索子树的最大键值和
- **链接**: https://leetcode.cn/problems/maximum-sum-bst-in-binary-tree/
- **题目描述**: 在二叉树中查找键值和最大的二叉搜索子树
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 47. LeetCode 1902. 深度最大的二叉搜索树
- **链接**: https://leetcode.cn/problems/depth-of-bst-given-insertion-order/
- **题目描述**: 根据插入顺序计算二叉搜索树的最大深度
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 48. LeetCode 2096. 从二叉树一个节点到另一个节点每一步的方向
- **链接**: https://leetcode.cn/problems/step-by-step-directions-from-a-binary-tree-node-to-another/
- **题目描述**: 在二叉树中找到从一个节点到另一个节点的路径方向
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 49. LeetCode 2196. 根据描述创建二叉树
- **链接**: https://leetcode.cn/problems/create-binary-tree-from-descriptions/
- **题目描述**: 根据父子关系描述创建二叉树
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 50. LeetCode 2236. 判断根结点是否等于子结点之和
- **链接**: https://leetcode.cn/problems/root-equals-sum-of-children/
- **题目描述**: 判断根节点值是否等于两个子节点值之和
- **时间复杂度**: O(1)
- **空间复杂度**: O(1)

### 51. HDU 1754 I Hate It
- **链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1754
- **题目描述**: 线段树/平衡树应用，区间最值查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 52. POJ 3468 A Simple Problem with Integers
- **链接**: http://poj.org/problem?id=3468
- **题目描述**: 线段树/平衡树应用，区间修改和查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 53. SPOJ ORDERSET
- **链接**: https://www.spoj.com/problems/ORDERSET/
- **题目描述**: 维护有序集合，支持插入、删除、查询排名、查询第k小
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 54. CodeChef TREEORD
- **链接**: https://www.codechef.com/problems/TREEORD
- **题目描述**: 树排序问题，涉及二叉搜索树操作
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 55. AtCoder ABC174 F - Range Set Query
- **链接**: https://atcoder.jp/contests/abc174/tasks/abc174_f
- **题目描述**: 区间不同元素查询，可使用平衡树优化
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 56. USACO 2018 January Platinum - Lifeguards
- **链接**: http://www.usaco.org/index.php?page=viewproblem2&cpid=793
- **题目描述**: 区间覆盖问题，可使用平衡树维护
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 57. 牛客网 NC145 01序列的最小权值
- **链接**: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
- **题目描述**: 维护01序列，支持插入和查询操作
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 58. 洛谷 P3391 【模板】文艺平衡树
- **链接**: https://www.luogu.com.cn/problem/P3391
- **题目描述**: 平衡树区间翻转操作
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 59. 洛谷 P3835 【模板】可持久化平衡树
- **链接**: https://www.luogu.com.cn/problem/P3835
- **题目描述**: 可持久化平衡树实现
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n log n)

### 60. 洛谷 P5055 【模板】可持久化文艺平衡树
- **链接**: https://www.luogu.com.cn/problem/P5055
- **题目描述**: 可持久化文艺平衡树实现
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n log n)

### 61. Codeforces 455D - Serega and Fun
- **链接**: https://codeforces.com/problemset/problem/455/D
- **题目描述**: 分块+平衡树应用，支持区间循环移位和查询
- **时间复杂度**: O(n√n)
- **空间复杂度**: O(n)

### 62. Codeforces 459D - Pashmak and Parmida's problem
- **链接**: https://codeforces.com/problemset/problem/459/D
- **题目描述**: 树状数组/平衡树应用，计算满足条件的点对数量
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 63. Codeforces 813F - Bipartite Checking
- **链接**: https://codeforces.com/contest/813/problem/F
- **题目描述**: 线段树分治+并查集/平衡树应用
- **时间复杂度**: O(n log² n)
- **空间复杂度**: O(n)

### 64. Codeforces 1681F - Unique Occurrences
- **链接**: https://codeforces.com/contest/1681/problem/F
- **题目描述**: 树上问题，可使用平衡树维护
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 65. Codeforces 576E - Painting Edges
- **链接**: https://codeforces.com/contest/576/problem/E
- **题目描述**: 线段树分治+并查集/平衡树应用
- **时间复杂度**: O(n log² n)
- **空间复杂度**: O(n)

### 66. HackerRank Self-Balancing Tree
- **链接**: https://www.hackerrank.com/challenges/self-balancing-tree/problem
- **题目描述**: 实现AVL树的插入操作
- **时间复杂度**: O(log n)
- **空间复杂度**: O(n)

### 67. HackerRank Binary Search Tree Insertion
- **链接**: https://www.hackerrank.com/challenges/binary-search-tree-insertion/problem
- **题目描述**: 二叉搜索树插入操作
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 68. Project Euler Problem 209 - Circular Logic
- **链接**: https://projecteuler.net/problem=209
- **题目描述**: 组合数学问题，涉及树结构
- **时间复杂度**: O(2^k)
- **空间复杂度**: O(2^k)

### 69. 剑指Offer 33 - 二叉搜索树的后序遍历序列
- **链接**: https://leetcode.cn/problems/er-cha-sou-suo-shu-de-hou-xu-bian-li-xu-lie-lcof/
- **题目描述**: 验证数组是否是二叉搜索树的后序遍历结果
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 70. 剑指Offer 36 - 二叉搜索树与双向链表
- **链接**: https://leetcode.cn/problems/er-cha-sou-suo-shu-yu-shuang-xiang-lian-biao-lcof/
- **题目描述**: 将二叉搜索树转换为排序的双向链表
- **时间复杂度**: O(n)
- **空间复杂度**: O(h)

### 71. 剑指Offer 54 - 二叉搜索树的第k大节点
- **链接**: https://leetcode.cn/problems/er-cha-sou-suo-shu-de-di-kda-jie-dian-lcof/
- **题目描述**: 在二叉搜索树中查找第k大的节点
- **时间复杂度**: O(k)
- **空间复杂度**: O(h)

### 72. 剑指Offer 68-I - 二叉搜索树的最近公共祖先
- **链接**: https://leetcode.cn/problems/er-cha-sou-suo-shu-de-zui-jin-gong-gong-zu-xian-lcof/
- **题目描述**: 在二叉搜索树中查找两个节点的最近公共祖先
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 73. 剑指Offer 68-II - 二叉树的最近公共祖先
- **链接**: https://leetcode.cn/problems/er-cha-shu-de-zui-jin-gong-gong-zu-xian-lcof/
- **题目描述**: 在二叉树中查找两个节点的最近公共祖先
- **时间复杂度**: O(n)
- **空间复杂度**: O(n)

### 74. 杭电OJ 2544 - 最短路
- **链接**: http://acm.hdu.edu.cn/showproblem.php?pid=2544
- **题目描述**: 最基础的单源最短路径问题
- **时间复杂度**: O((n+m) log n)
- **空间复杂度**: O(n+m)

### 75. 杭电OJ 1754 - I Hate It
- **链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1754
- **题目描述**: 线段树/平衡树应用，区间最值查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 76. 杭电OJ 4027 - Can you answer these queries?
- **链接**: http://acm.hdu.edu.cn/showproblem.php?pid=4027
- **题目描述**: 线段树/平衡树应用，区间开方和查询
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 77. ZOJ 1659 - Mobile Phone Coverage
- **链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
- **题目描述**: 计算矩形覆盖面积，可使用平衡树维护
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 78. ZOJ 3686 - A Simple Tree Problem
- **链接**: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368846
- **题目描述**: 树结构问题，涉及平衡树操作
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 79. UVa OJ 12192 - Grapevine
- **链接**: https://uva.onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3344
- **题目描述**: 二维矩阵查询，可使用平衡树优化
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 80. Timus OJ 1021 - Sacrament of the Sum
- **链接**: https://acm.timus.ru/problem.aspx?space=1&num=1021
- **题目描述**: 两数之和问题，可使用平衡树优化
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 81. Aizu OJ ALDS1_4_B - Binary Search
- **链接**: https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/4/ALDS1_4_B
- **题目描述**: 二分查找基础题
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 82. Comet OJ - 二分查找
- **链接**: https://cometoj.com/contest/75/problem/A
- **题目描述**: 二分查找应用
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 83. 杭电 OJ 1087 - Super Jumping! Jumping! Jumping!
- **链接**: http://acm.hdu.edu.cn/showproblem.php?pid=1087
- **题目描述**: 动态规划问题，涉及有序序列处理
- **时间复杂度**: O(n²)
- **空间复杂度**: O(n)

### 84. LOJ #10017. 二分查找
- **链接**: https://loj.ac/problem/10017
- **题目描述**: 二分查找基础题
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 85. 计蒜客 T1234 - 二分查找
- **链接**: https://nanti.jisuanke.com/t/T1234
- **题目描述**: 二分查找应用
- **时间复杂度**: O(log n)
- **空间复杂度**: O(1)

### 86. MarsCode - 平衡树专题
- **链接**: https://www.marscode.cn/topic/balanced-tree
- **题目描述**: 平衡树相关题目集合
- **时间复杂度**: 根据具体题目而定
- **空间复杂度**: 根据具体题目而定

### 87. 洛谷 P2408 - 不同子串个数
- **链接**: https://www.luogu.com.cn/problem/P2408
- **题目描述**: 计算字符串中不同子串的个数
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 88. 洛谷 P2870 - [USACO07DEC] Best Cow Line G
- **链接**: https://www.luogu.com.cn/problem/P2870
- **题目描述**: 字符串处理，涉及有序序列维护
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 89. 洛谷 P1090 - 合并果子
- **链接**: https://www.luogu.com.cn/problem/P1090
- **题目描述**: 哈夫曼树/优先队列应用
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 90. 洛谷 P3372 【模板】线段树 1
- **链接**: https://www.luogu.com.cn/problem/P3372
- **题目描述**: 线段树区间修改和查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 91. 洛谷 P3373 【模板】线段树 2
- **链接**: https://www.luogu.com.cn/problem/P3373
- **题目描述**: 线段树区间乘加修改和查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 92. 洛谷 P3374 【模板】树状数组 1
- **链接**: https://www.luogu.com.cn/problem/P3374
- **题目描述**: 树状数组单点修改和区间查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 93. 洛谷 P3368 【模板】树状数组 2
- **链接**: https://www.luogu.com.cn/problem/P3368
- **题目描述**: 树状数组区间修改和单点查询
- **时间复杂度**: O(log n) 每次操作
- **空间复杂度**: O(n)

### 94. 洛谷 P1908 逆序对
- **链接**: https://www.luogu.com.cn/problem/P1908
- **题目描述**: 计算数组中的逆序对数量
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 95. 洛谷 P3810 【模板】三维偏序（陌上花开）
- **链接**: https://www.luogu.com.cn/problem/P3810
- **题目描述**: 三维偏序问题，CDQ分治/树套树应用
- **时间复杂度**: O(n log² n)
- **空间复杂度**: O(n)

### 96. 洛谷 P1972 [SDOI2009] HH的项链
- **链接**: https://www.luogu.com.cn/problem/P1972
- **题目描述**: 区间不同元素查询，离线处理/树状数组应用
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 97. 洛谷 P4113 [HEOI2012] 采花
- **链接**: https://www.luogu.com.cn/problem/P4113
- **题目描述**: 区间出现至少两次的元素查询
- **时间复杂度**: O(n log n)
- **空间复杂度**: O(n)

### 98. 洛谷 P4396 [AHOI2013] 作业
- **链接**: https://www.luogu.com.cn/problem/P4396
- **题目描述**: 区间值域查询，莫队/树状数组应用
- **时间复杂度**: O(n√n)
- **空间复杂度**: O(n)

### 99. 洛谷 P4869 albus就是要第一个出场
- **链接**: https://www.luogu.com.cn/problem/P4869
- **题目描述**: 线性基应用，涉及集合操作
- **时间复杂度**: O(n log maxA)
- **空间复杂度**: O(log maxA)

### 100. 洛谷 P5357 【模板】AC自动机（二次加强版）
- **链接**: https://www.luogu.com.cn/problem/P5357
- **题目描述**: AC自动机模板题
- **时间复杂度**: O(∑|S| + |T|)
- **空间复杂度**: O(∑|S|)

## 算法思路技巧总结

### 1. 适用场景
- **需要维护有序集合**：支持快速插入、删除、查找操作
- **需要查询元素排名或第k小元素**：通过维护子树大小信息实现高效查询
- **需要频繁查询前驱和后继元素**：利用BST性质实现O(log n)查询
- **处理强制在线问题**：通过异或操作实现强制在线处理
- **动态数据维护**：数据频繁变动但需要保持有序性的场景
- **范围查询**：支持区间查询和统计操作

### 2. 核心思想
- **平衡性维护**：通过旋转操作维持树的平衡性，保证树的高度为O(log n)
- **信息维护**：每个节点维护子树大小、高度、平衡因子等关键信息
- **旋转调整**：插入和删除操作后通过四种旋转操作调整恢复平衡
- **在线处理**：强制在线通过异或操作实现，保证安全性
- **性能保证**：最坏情况下所有操作时间复杂度均为O(log n)

### 3. 四种旋转操作详解

#### LL旋转（左左旋转）
**触发条件**：在左孩子的左子树插入导致失衡（平衡因子 > 1）
**操作步骤**：
1. 将失衡节点的左孩子提升为新的根节点
2. 原根节点成为新根节点的右孩子
3. 新根节点原来的右孩子成为原根节点的左孩子
**时间复杂度**：O(1)
**应用场景**：左子树高度大于右子树高度2层

#### RR旋转（右右旋转）
**触发条件**：在右孩子的右子树插入导致失衡（平衡因子 < -1）
**操作步骤**：
1. 将失衡节点的右孩子提升为新的根节点
2. 原根节点成为新根节点的左孩子
3. 新根节点原来的左孩子成为原根节点的右孩子
**时间复杂度**：O(1)
**应用场景**：右子树高度大于左子树高度2层

#### LR旋转（左右旋转）
**触发条件**：在左孩子的右子树插入导致失衡
**操作步骤**：
1. 先对左孩子进行RR旋转
2. 再对根节点进行LL旋转
**时间复杂度**：O(1)
**应用场景**：左子树的右子树高度增加导致失衡

#### RL旋转（右左旋转）
**触发条件**：在右孩子的左子树插入导致失衡
**操作步骤**：
1. 先对右孩子进行LL旋转
2. 再对根节点进行RR旋转
**时间复杂度**：O(1)
**应用场景**：右子树的左子树高度增加导致失衡

### 4. 工程化深度考量

#### 4.1 内存管理优化
- **数组模拟指针**：使用数组代替指针减少内存碎片，提高缓存命中率
- **内存池技术**：预分配内存空间，避免频繁内存分配释放
- **对象复用**：删除节点时标记为可用，后续插入时复用
- **内存对齐**：合理对齐数据结构，提高内存访问效率

#### 4.2 性能优化策略
- **子树大小维护**：通过维护子树大小信息支持O(log n)排名查询
- **路径压缩**：在查找过程中进行路径优化
- **延迟更新**：批量操作时延迟平衡调整
- **缓存友好**：优化数据布局，提高CPU缓存利用率

#### 4.3 边界处理完善
- **空树处理**：正确处理空树的插入、删除、查询操作
- **重复元素**：支持重复元素的插入和词频统计
- **极端输入**：处理极大值、极小值、重复序列等边界情况
- **内存溢出**：检测和处理内存不足的情况

#### 4.4 异常处理机制
- **参数验证**：检查输入参数的有效性和范围
- **状态检查**：验证树结构的完整性和一致性
- **错误恢复**：提供错误恢复机制，保证程序稳定性
- **日志记录**：记录关键操作和异常信息，便于调试

#### 4.5 在线处理安全
- **异或加密**：使用异或操作实现强制在线，保证数据安全
- **输入验证**：验证在线输入的合法性和完整性
- **防攻击机制**：防止恶意输入导致的性能退化
- **数据一致性**：保证在线操作的数据一致性

### 5. 时间和空间复杂度深度分析

#### 5.1 时间复杂度分析
- **插入操作**：O(log n) - 查找插入位置 + 旋转调整
- **删除操作**：O(log n) - 查找目标节点 + 旋转调整
- **查找操作**：O(log n) - 二分查找性质
- **查询排名**：O(log n) - 利用子树大小信息
- **查询第k小**：O(log n) - 基于排名的二分查找
- **前驱/后继**：O(log n) - 利用BST的有序性

#### 5.2 空间复杂度分析
- **节点存储**：O(n) - 每个节点需要存储键值、高度、子树大小等信息
- **递归栈**：O(log n) - 递归实现时的栈空间
- **辅助空间**：O(1) - 迭代实现时的常数空间

#### 5.3 常数因子优化
- **内联函数**：将频繁调用的短函数内联化
- **循环展开**：适当展开循环减少分支预测失败
- **寄存器优化**：合理使用寄存器变量
- **指令优化**：选择高效的机器指令序列

### 6. 与其他数据结构的深度比较

#### 6.1 AVL树 vs Treap
**AVL树优势**：
- 平衡性更严格，查询性能更稳定
- 确定性算法，不依赖随机数
- 最坏情况性能有理论保证

**Treap优势**：
- 实现更简单，代码量少
- 插入删除的平均性能更好
- 支持分裂合并操作

**选择建议**：对查询性能要求高选AVL，对实现简单性要求高选Treap

#### 6.2 AVL树 vs 红黑树
**AVL树优势**：
- 查询性能更好（树更矮）
- 平衡性更严格，性能更可预测
- 实现相对简单直观

**红黑树优势**：
- 插入删除性能更好（旋转次数少）
- 内存开销更小
- 标准库广泛使用，经过充分优化

**选择建议**：查询密集型应用选AVL，插入删除密集型选红黑树

#### 6.3 AVL树 vs Splay Tree
**AVL树优势**：
- 最坏时间复杂度稳定
- 不需要额外的伸展操作
- 适合需要稳定性能的场景

**Splay Tree优势**：
- 局部性原理，访问热点数据更快
- 不需要维护平衡信息
- 实现更简单

**选择建议**：需要稳定性能选AVL，访问模式有局部性选Splay Tree

### 7. 语言特性深度差异分析

#### 7.1 Java语言特性
**优势**：
- 对象引用操作直观易懂
- 自动内存管理（GC）
- 丰富的标准库支持
- 跨平台兼容性好

**劣势**：
- GC可能带来不可预测的停顿
- 对象头开销较大
- 数值类型需要装箱拆箱

**优化策略**：
- 使用基本类型数组减少对象开销
- 合理设置GC参数
- 使用对象池技术

#### 7.2 C++语言特性
**优势**：
- 指针操作直接高效
- 手动内存控制，性能可预测
- 模板元编程优化
- 零开销抽象

**劣势**：
- 内存管理复杂，容易出错
- 需要手动处理资源释放
- 跨平台兼容性需要额外处理

**优化策略**：
- 使用智能指针自动管理内存
- 利用移动语义减少拷贝
- 使用模板优化泛型代码

#### 7.3 Python语言特性
**优势**：
- 语法简洁，开发效率高
- 动态类型，灵活性好
- 丰富的第三方库
- 适合快速原型开发

**劣势**：
- 解释执行，性能较低
- 全局解释器锁（GIL）限制并发
- 内存开销较大

**优化策略**：
- 使用PyPy等JIT编译器
- 关键部分用C扩展实现
- 使用numpy等高效数值库

### 8. 算法调试与问题定位实操能力

#### 8.1 调试技巧
- **打印中间过程**：在关键节点打印变量状态
- **断言验证**：使用断言检查中间结果正确性
- **可视化工具**：使用图形化工具展示树结构
- **单元测试**：编写全面的测试用例

#### 8.2 问题定位方法
- **边界测试**：测试空树、单节点、满树等边界情况
- **压力测试**：大规模数据测试性能表现
- **内存分析**：使用工具分析内存使用情况
- **性能剖析**：使用profiler找出性能瓶颈

#### 8.3 错误排查流程
1. **重现问题**：构造最小可重现测试用例
2. **定位根源**：通过日志和调试信息定位问题代码
3. **分析原因**：分析算法逻辑错误或实现bug
4. **修复验证**：修复问题并验证正确性
5. **预防措施**：添加相应测试用例防止回归

### 9. 笔试面试核心技巧

#### 9.1 笔试解题效率
- **模板准备**：提前准备常用算法模板
- **边界处理**：特别注意各种边界情况
- **时间复杂度**：准确分析算法复杂度
- **代码简洁**：保持代码清晰简洁

#### 9.2 面试深度表达
- **原理理解**：深入理解算法原理和设计思想
- **优缺点分析**：全面分析各种实现的优缺点
- **应用场景**：清楚说明适用场景和限制
- **优化思路**：提出可能的优化方案

#### 9.3 实战经验分享
- **踩坑经验**：分享实际开发中遇到的问题和解决方案
- **性能调优**：介绍性能优化的具体方法和效果
- **工程实践**：讲述在实际项目中的应用经验
- **学习路径**：分享学习算法的心得和方法

### 10. 极端场景鲁棒性验证

#### 10.1 极端输入测试
- **空输入**：测试空树的各种操作
- **极端值**：测试极大值、极小值、边界值
- **重复数据**：测试大量重复元素的处理
- **有序/逆序**：测试有序和逆序插入的性能

#### 10.2 性能退化排查
- **大数据量**：测试大规模数据的性能表现
- **最坏情况**：构造最坏情况的输入序列
- **内存限制**：测试在内存限制下的表现
- **并发访问**：测试多线程环境下的正确性

#### 10.3 稳定性保障
- **长时间运行**：测试长时间运行的稳定性
- **错误恢复**：测试异常情况下的恢复能力
- **数据一致性**：验证操作前后数据的一致性
- **资源清理**：确保资源正确释放

通过以上全面的分析和实践，可以确保AVL树的实现不仅功能正确，而且具有优秀的性能和鲁棒性，能够满足各种实际应用场景的需求。

## 应用场景

### 1. 排序和选择问题
AVL树可以高效地维护有序数据，支持快速插入、删除和查找操作。

### 2. 范围查询
通过维护子树大小信息，AVL树可以高效地支持范围查询操作。

### 3. 在线算法
AVL树的平衡性质使其适用于需要在线处理的算法问题。

### 4. 动态集合维护
当需要维护一个动态集合并支持各种查询操作时，AVL树是一个很好的选择。

## 性能分析

### 优势
1. **最坏情况下的性能保证**：所有操作的时间复杂度都保证为O(log n)
2. **高度平衡**：树的高度始终保持在最小可能范围内
3. **查询效率高**：由于树的平衡性，查询操作非常高效

### 劣势
1. **实现复杂**：需要处理多种旋转情况，实现相对复杂
2. **常数因子大**：由于需要维护平衡，常数因子比普通二叉搜索树大
3. **插入/删除开销**：为了维持平衡，插入和删除操作可能需要多次旋转

## 与其他平衡树的比较

### AVL树 vs 红黑树
- **平衡性**：AVL树更严格，红黑树相对宽松
- **查询性能**：AVL树查询更快（树更矮）
- **插入/删除性能**：红黑树插入/删除更快（旋转次数少）

### AVL树 vs Treap
- **实现复杂度**：Treap实现更简单
- **随机性**：Treap依赖随机数，AVL树确定性更强
- **性能**：平均情况下两者性能相近

## 实际应用案例

### 1. 数据库索引
某些数据库系统使用AVL树或其变种作为索引结构。

### 2. 编程语言标准库
一些编程语言的标准库中包含AVL树的实现。

### 3. 文件系统
某些文件系统使用平衡树来维护文件和目录的元数据。

## 学习建议

### 1. 理解基础概念
- 掌握二叉搜索树的基本性质
- 理解平衡因子的概念
- 熟悉四种旋转操作

### 2. 实践实现
- 从简单的插入和查找开始实现
- 逐步添加删除操作
- 实现完整的AVL树功能

### 3. 性能调优
- 注意内存管理
- 优化旋转操作的实现
- 考虑缓存友好性

### 4. 扩展学习
- 学习其他平衡树（如红黑树、Treap等）
- 了解B树和B+树
- 探索并发平衡树的实现

## 常见问题和解决方案

### 1. 旋转操作错误
**问题**：旋转后节点关系处理错误
**解决方案**：仔细检查旋转操作中节点指针的更新顺序

### 2. 平衡因子计算错误
**问题**：平衡因子没有及时更新
**解决方案**：在每次修改树结构后更新相关节点的平衡因子

### 3. 内存泄漏
**问题**：删除节点时没有正确释放内存
**解决方案**：确保在删除节点时正确处理内存释放

## 进阶话题

### 1. 并发AVL树
研究如何在多线程环境下安全地使用AVL树。

### 2. 持久化AVL树
实现支持版本控制的AVL树。

### 3. 近似平衡树
研究牺牲一定平衡性以换取更好性能的数据结构。

## 参考资料

1. Adelson-Velskii, G. M., & Landis, E. M. (1962). An algorithm for the organization of information. *Proceedings of the USSR Academy of Sciences*, 146(2), 263-266.
2. Knuth, D. E. (1998). *The Art of Computer Programming, Volume 3: Sorting and Searching* (2nd ed.). Addison-Wesley.
3. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.