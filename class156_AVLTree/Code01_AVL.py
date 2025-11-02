# AVL树的实现(Python版)
# 实现一种结构，支持如下操作，要求单次调用的时间复杂度O(log n)
# 1，增加x，重复加入算多个词频
# 2，删除x，如果有多个，只删掉一个
# 3，查询x的排名，x的排名为，比x小的数的个数+1
# 4，查询数据中排名为x的数
# 5，查询x的前驱，x的前驱为，小于x的数中最大的数，不存在返回整数最小值
# 6，查询x的后继，x的后继为，大于x的数中最小的数，不存在返回整数最大值
# 所有操作的次数 <= 10^5
# -10^7 <= x <= +10^7
# 测试链接 : https://www.luogu.com.cn/problem/P3369

"""
AVL树补充题目与详细解析

【基础模板题】
1. 洛谷 P3369 【模板】普通平衡树
   链接: https://www.luogu.com.cn/problem/P3369
   题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)
   核心考点: 平衡树基本操作实现

2. 洛谷 P6136 【模板】普通平衡树（数据加强版）
   链接: https://www.luogu.com.cn/problem/P6136
   题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)
   核心考点: 平衡树性能优化、在线处理

【数据结构应用题】
3. LeetCode 406. Queue Reconstruction by Height
   链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
   题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
   时间复杂度: O(n log n)
   空间复杂度: O(n)
   核心考点: 排序+插入策略，可利用AVL树高效插入

4. PAT甲级 1066 Root of AVL Tree
   链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
   题目描述: 给定插入序列，构建AVL树，输出根节点的值
   时间复杂度: O(n log n)
   空间复杂度: O(n)
   核心考点: AVL树构建过程

5. PAT甲级 1123 Is It a Complete AVL Tree
   链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
   题目描述: 判断构建的AVL树是否是完全二叉树
   时间复杂度: O(n log n)
   空间复杂度: O(n)
   核心考点: AVL树与完全二叉树性质结合

【滑动窗口/范围查询题】
6. LeetCode 220. Contains Duplicate III
   链接: https://leetcode.cn/problems/contains-duplicate-iii/
   题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
   时间复杂度: O(n log k)
   空间复杂度: O(k)
   核心考点: 滑动窗口+有序集合，可利用AVL树维护窗口内元素

【计数问题】
7. Codeforces 459D - Pashmak and Parmida's problem
   链接: https://codeforces.com/problems/908D
   题目描述: 计算满足条件的点对数量
   时间复杂度: O(n log n)
   空间复杂度: O(n)
   核心考点: 逆序对变种，可利用AVL树统计

【动态集合维护题】
8. SPOJ Ada and Behives
   链接: https://www.spoj.com/problems/ADAAPHID/
   题目描述: 维护一个动态集合，支持插入和查询操作
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)
   核心考点: 动态集合操作

【新增补充题目】
9. LeetCode 98. 验证二叉搜索树
   链接: https://leetcode.cn/problems/validate-binary-search-tree/
   题目描述: 验证一个二叉树是否是有效的二叉搜索树
   时间复杂度: O(n)
   空间复杂度: O(h)，h为树高
   核心考点: 二叉搜索树性质理解

10. LeetCode 669. 修剪二叉搜索树
    链接: https://leetcode.cn/problems/trim-a-binary-search-tree/
    题目描述: 修剪二叉搜索树，保留值在[low, high]范围内的节点
    时间复杂度: O(n)
    空间复杂度: O(h)
    核心考点: 二叉搜索树删除操作的扩展

11. 牛客网 NC17 最长回文子串
    链接: https://www.nowcoder.com/practice/b4525d1d84934cf280439aeecc36f4af
    题目描述: 寻找最长回文子串，可以利用Manacher算法结合AVL树优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 高级字符串算法结合数据结构

12. 牛客网 NC140 排序数组中出现次数超过一半的数字
    链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
    题目描述: 找出数组中出现次数超过数组长度一半的数字
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 计数问题

13. 洛谷 P1908 逆序对
    链接: https://www.luogu.com.cn/problem/P1908
    题目描述: 求一个序列中的逆序对数量
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 归并排序思想或树状数组/AVL树应用

14. 力扣 230. 二叉搜索树中第K小的元素
    链接: https://leetcode.cn/problems/kth-smallest-element-in-a-bst/
    题目描述: 给定一个二叉搜索树，找出其中第k小的元素
    时间复杂度: O(h + k)
    空间复杂度: O(h)
    核心考点: 中序遍历应用

15. 力扣 538. 把二叉搜索树转换为累加树
    链接: https://leetcode.cn/problems/convert-bst-to-greater-tree/
    题目描述: 将二叉搜索树转换为累加树，每个节点的值变为原树中大于或等于该节点值的所有节点值之和
    时间复杂度: O(n)
    空间复杂度: O(h)
    核心考点: 逆中序遍历应用

16. 力扣 1038. 从二叉搜索树到更大和树
    链接: https://leetcode.cn/problems/binary-search-tree-to-greater-sum-tree/
    题目描述: 与538题类似，但要求节点值变为所有大于该节点值的节点值之和
    时间复杂度: O(n)
    空间复杂度: O(h)
    核心考点: 逆中序遍历应用

17. 力扣 450. 删除二叉搜索树中的节点
    链接: https://leetcode.cn/problems/delete-node-in-a-bst/
    题目描述: 给定一个二叉搜索树和一个键值，删除对应的节点并保持BST性质
    时间复杂度: O(h)
    空间复杂度: O(h)
    核心考点: BST删除操作

18. 力扣 701. 二叉搜索树中的插入操作
    链接: https://leetcode.cn/problems/insert-into-a-binary-search-tree/
    题目描述: 给定一个二叉搜索树和一个值，将值插入到BST中
    时间复杂度: O(h)
    空间复杂度: O(h)
    核心考点: BST插入操作

19. 力扣 1008. 前序遍历构造二叉搜索树
    链接: https://leetcode.cn/problems/construct-binary-search-tree-from-preorder-traversal/
    题目描述: 根据前序遍历结果构造二叉搜索树
    时间复杂度: O(n)
    空间复杂度: O(h)
    核心考点: BST构造

20. 牛客网 NC6 二叉树中的最大路径和
    链接: https://www.nowcoder.com/practice/da785ea0f64b442488c125b441a4ba4a
    题目描述: 找出二叉树中路径和最大的路径
    时间复杂度: O(n)
    空间复杂度: O(h)
    核心考点: 树的后序遍历

21. HackerRank Self-Balancing Tree
    链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
    题目描述: 实现AVL树的插入操作
    时间复杂度: O(log n)
    空间复杂度: O(n)
    核心考点: AVL树节点定义和旋转操作

22. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
    链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
    题目描述: 字符串处理问题，可使用平衡树优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 后缀数组+平衡树

23. CodeChef ORDERSET
    链接: https://www.codechef.com/problems/ORDERSET
    题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
    时间复杂度: O(log n)
    空间复杂度: O(n)
    核心考点: 平衡树基本操作

24. AtCoder ABC134 E - Sequence Decomposing
    链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
    题目描述: 序列分解问题，可使用平衡树优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: LIS变种+平衡树

25. ZOJ 1659 Mobile Phone Coverage
    链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827368277
    题目描述: 计算矩形覆盖面积，可使用平衡树维护
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 扫描线+平衡树

26. POJ 1864 [NOI2009] 二叉查找树
    链接: http://poj.org/problem?id=1864
    题目描述: 二叉查找树的动态规划问题
    时间复杂度: O(n^2)
    空间复杂度: O(n)
    核心考点: 树形DP+平衡树

27. HDU 4589 Special equations
    链接: http://acm.hdu.edu.cn/showproblem.php?pid=4589
    题目描述: 数学问题，可使用平衡树优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    核心考点: 数论+平衡树

算法思路技巧总结：

【适用场景与核心思想】
1. 适用场景：
   - 需要维护有序集合，并支持快速插入、删除、查找（O(log n)时间）
   - 需要查询元素排名或第k小元素
   - 需要频繁查询前驱和后继元素
   - 需要处理强制在线问题
   - 需要保证最坏情况下的性能稳定性

2. 核心思想：
   - 通过旋转操作维持树的平衡性，保证树的高度始终为O(log n)
   - 每个节点维护子树大小和高度信息，支持高效的排名查询
   - 插入和删除操作后通过自底向上的旋转调整恢复平衡
   - 对于重复元素，使用计数方式处理，避免节点过多

【关键操作与实现细节】
3. 四种旋转操作详解：
   - LL旋转（右旋）：当左子树的左子树导致失衡，单右旋一次
   - RR旋转（左旋）：当右子树的右子树导致失衡，单左旋一次
   - LR旋转：先左旋左子树，再右旋根节点
   - RL旋转：先右旋右子树，再左旋根节点

4. 维护信息：
   - 每个节点需要维护：键值、左右子节点指针、高度、计数（重复元素）、子树大小
   - 每次修改树结构后必须更新相关节点的这些信息

【工程化考量】
5. 内存管理：
   - Python中使用对象引用，自动垃圾回收
   - 可考虑使用对象池或数组模拟优化内存访问模式

6. 性能优化技巧：
   - 使用迭代代替递归减少函数调用开销
   - 批量操作时考虑延迟平衡
   - 针对特定场景优化比较操作

7. 边界处理：
   - 空树处理
   - 重复元素处理
   - 前驱后继不存在的情况
   - 极端数据规模（如只有一种元素）

【复杂度分析】
8. 时间和空间复杂度：
   - 插入操作：O(log n)
   - 删除操作：O(log n)
   - 查找操作：O(log n)
   - 查询排名：O(log n)
   - 查询第k小：O(log n)
   - 前驱/后继查询：O(log n)
   - 空间复杂度：O(n)，其中n为元素总数

9. 常数项优化：
   - 旋转操作是常数时间，但实现效率影响整体性能
   - 使用路径压缩技术可能进一步优化某些操作

【语言特性差异】
10. 跨语言实现比较：
    - Java: 对象引用操作直观，自动GC，但可能有GC暂停开销
    - C++: 指针操作更直接，手动内存管理，性能最高，适合竞赛
    - Python: 语法简洁，开发效率高，但性能较低，不适合超大数据规模

【优化方向与拓展】
11. 高级应用场景：
    - 持久化AVL树：支持历史版本查询
    - 并发AVL树：多线程环境下的安全访问
    - 线段树分治结合AVL树处理动态问题

12. 与机器学习/数据挖掘关联：
    - 作为决策树算法的基础结构
    - 用于特征选择中的快速排序和选择操作
    - 在推荐系统中维护有序集合

【调试与测试技巧】
13. 常见错误排查：
    - 旋转后节点关系错误：检查指针更新顺序
    - 平衡因子计算错误：确保每次修改后更新所有相关节点
    - 子树大小维护错误：验证up操作的正确性
    - 边界条件处理不当：针对空树、单节点树等进行专门测试

14. 测试用例设计：
    - 空树测试
    - 重复元素测试
    - 升序/降序插入测试（测试旋转正确性）
    - 随机数据测试（测试性能和正确性）
    - 极端数据规模测试（测试内存使用）
"""

import sys
from typing import List

class AVLNode:
    """
    AVL树节点类
    时间复杂度：属性访问O(1)
    空间复杂度：每个节点O(1)，总树空间O(n)
    """
    def __init__(self, key):
        self.key = key          # 节点键值
        self.left = None        # 左子节点指针
        self.right = None       # 右子节点指针
        self.height = 1         # 节点高度（从叶子到当前节点的最长路径长度+1）
        self.count = 1          # 重复元素计数器，用于高效处理重复插入
        self.size = 1           # 子树大小，包括当前节点和所有子节点

class AVLTree:
    """
    AVL树实现类 - 一种自平衡二叉搜索树
    每个节点的左右子树高度差不超过1，保证所有操作的对数时间复杂度
    支持：插入、删除、查询排名、查询第k小、查询前驱、查询后继
    所有操作时间复杂度：O(log n)
    空间复杂度：O(n)
    """
    def __init__(self):
        """
        初始化空AVL树
        时间复杂度：O(1)
        空间复杂度：O(1)
        """
        self.root = None  # 根节点指针，初始为空
    
    def get_height(self, node):
        """
        获取节点高度，空节点高度为0
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        参数:
            node: 要查询高度的节点
        返回:
            节点高度，空节点返回0
        """
        if not node:
            return 0
        return node.height
    
    def get_size(self, node):
        """
        获取子树大小，空节点子树大小为0
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        参数:
            node: 要查询子树大小的节点
        返回:
            子树大小，包括当前节点和所有子节点
        """
        if not node:
            return 0
        return node.size
    
    def update_info(self, node):
        """
        更新节点信息（高度和子树大小）
        这是维护AVL树平衡性的关键步骤
        时间复杂度：O(1)
        空间复杂度：O(1)
        异常处理：node为None时安全返回
        
        参数:
            node: 需要更新信息的节点
        """
        if not node:
            return
        # 高度计算：左右子树最大高度+1
        node.height = 1 + max(self.get_height(node.left), self.get_height(node.right))
        # 子树大小计算：左子树大小 + 右子树大小 + 当前节点计数
        node.size = self.get_size(node.left) + self.get_size(node.right) + node.count
    
    def get_balance(self, node):
        """
        获取节点的平衡因子（左子树高度 - 右子树高度）
        用于判断节点是否需要旋转调整
        时间复杂度：O(1)
        空间复杂度：O(1)
        异常处理：node为None时返回0
        
        参数:
            node: 要计算平衡因子的节点
        返回:
            平衡因子值
        """
        if not node:
            return 0
        return self.get_height(node.left) - self.get_height(node.right)
    
    def left_rotate(self, z):
        """
        左旋操作 - 处理RR情况
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        参数:
            z: 需要旋转的根节点
        返回:
            旋转后的新根节点y
        操作图解:
            z                 y
           / \               / \
          T1  y    ----->    z  T3
             / \             / \
            T2 T3           T1 T2
        """
        y = z.right  # y成为新的根节点
        T2 = y.left  # T2是y的左子树，旋转后成为z的右子树
        
        # 执行旋转
        y.left = z   # z成为y的左子节点
        z.right = T2 # T2成为z的右子节点
        
        # 更新高度和大小 - 注意顺序：先更新子节点，再更新父节点
        self.update_info(z)
        self.update_info(y)
        
        # 返回新的根节点
        return y
    
    def right_rotate(self, z):
        """
        右旋操作 - 处理LL情况
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        参数:
            z: 需要旋转的根节点
        返回:
            旋转后的新根节点y
        操作图解:
              z                 y
             / \               / \
            y  T3    ----->   T1  z
           / \                   / \
          T1 T2                 T2 T3
        """
        y = z.left   # y成为新的根节点
        T3 = y.right # T3是y的右子树，旋转后成为z的左子树
        
        # 执行旋转
        y.right = z  # z成为y的右子节点
        z.left = T3  # T3成为z的左子树
        
        # 更新高度和大小 - 注意顺序：先更新子节点，再更新父节点
        self.update_info(z)
        self.update_info(y)
        
        # 返回新的根节点
        return y
    
    def insert(self, root, key):
        """
        插入节点的递归实现
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        
        参数:
            root: 当前子树根节点
            key: 要插入的键值
        返回:
            插入操作后更新的子树根节点
        算法步骤:
            1. 标准BST插入
            2. 更新节点信息
            3. 检查平衡因子
            4. 必要时执行旋转恢复平衡
        边界情况处理:
            - 空树: 创建新节点
            - 重复键: 增加计数而不创建新节点
        """
        # 1. 执行标准BST插入
        if not root:
            # 递归终止条件：创建新节点
            return AVLNode(key)
        
        if key < root.key:
            # 在左子树中插入
            root.left = self.insert(root.left, key)
        elif key > root.key:
            # 在右子树中插入
            root.right = self.insert(root.right, key)
        else:
            # 相等的键，增加计数（处理重复元素的优化）
            root.count += 1
            self.update_info(root)
            return root
        
        # 2. 更新祖先节点的高度和大小（自底向上维护信息）
        self.update_info(root)
        
        # 3. 获取平衡因子，判断是否需要旋转
        balance = self.get_balance(root)
        
        # 4. 如果节点不平衡，执行相应的旋转操作
        
        # LL情况：左子树的左子树导致失衡，右旋一次
        if balance > 1 and key < root.left.key:
            return self.right_rotate(root)
        
        # RR情况：右子树的右子树导致失衡，左旋一次
        if balance < -1 and key > root.right.key:
            return self.left_rotate(root)
        
        # LR情况：左子树的右子树导致失衡，先左旋左子树，再右旋根节点
        if balance > 1 and key > root.left.key:
            root.left = self.left_rotate(root.left)
            return self.right_rotate(root)
        
        # RL情况：右子树的左子树导致失衡，先右旋右子树，再左旋根节点
        if balance < -1 and key < root.right.key:
            root.right = self.right_rotate(root.right)
            return self.left_rotate(root)
        
        # 返回未改变的节点指针（如果平衡不需要调整）
        return root
    
    def get_min_value_node(self, root):
        """
        获取以root为根的子树中的最小值节点
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        
        参数:
            root: 子树根节点
        返回:
            最小值节点（最左叶子节点）
        用途:
            用于删除操作中寻找中序后继
        """
        if root is None or root.left is None:
            return root
        return self.get_min_value_node(root.left)
    
    def delete(self, root, key):
        """
        删除节点的递归实现
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        
        参数:
            root: 当前子树根节点
            key: 要删除的键值
        返回:
            删除操作后更新的子树根节点
        算法步骤:
            1. 标准BST删除
            2. 更新节点信息
            3. 检查平衡因子
            4. 必要时执行旋转恢复平衡
        边界情况处理:
            - 空树: 直接返回
            - 重复键: 减少计数而不删除节点
            - 单/无子节点: 直接替换
            - 双子节点: 找到后继并替换
        """
        # 1. 执行标准BST删除
        if not root:
            # 键不存在于树中
            return root
        
        if key < root.key:
            # 在左子树中删除
            root.left = self.delete(root.left, key)
        elif key > root.key:
            # 在右子树中删除
            root.right = self.delete(root.right, key)
        else:
            # 找到要删除的节点
            
            # 情况1：如果有重复元素，减少计数即可
            if root.count > 1:
                root.count -= 1
                self.update_info(root)
                return root
            
            # 情况2：节点有0或1个子节点
            if root.left is None:
                temp = root.right
                root = None  # 帮助垃圾回收
                return temp
            elif root.right is None:
                temp = root.left
                root = None  # 帮助垃圾回收
                return temp
            
            # 情况3：节点有2个子节点
            # 找右子树中的最小值节点（中序后继）
            temp = self.get_min_value_node(root.right)
            
            # 将后继的值和计数复制到当前节点
            root.key = temp.key
            root.count = temp.count
            temp.count = 1  # 重置后继节点的计数，确保删除时正确处理
            
            # 删除后继节点
            root.right = self.delete(root.right, temp.key)
        
        # 如果树只有根节点，则返回
        if root is None:
            return root
        
        # 2. 更新祖先节点的高度和大小（自底向上维护信息）
        self.update_info(root)
        
        # 3. 获取平衡因子，判断是否需要旋转
        balance = self.get_balance(root)
        
        # 4. 如果节点不平衡，执行相应的旋转操作
        
        # LL情况
        if balance > 1 and self.get_balance(root.left) >= 0:
            return self.right_rotate(root)
        
        # LR情况
        if balance > 1 and self.get_balance(root.left) < 0:
            root.left = self.left_rotate(root.left)
            return self.right_rotate(root)
        
        # RR情况
        if balance < -1 and self.get_balance(root.right) <= 0:
            return self.left_rotate(root)
        
        # RL情况
        if balance < -1 and self.get_balance(root.right) > 0:
            root.right = self.right_rotate(root.right)
            return self.left_rotate(root)
        
        return root
    
    def search(self, root, key):
        """
        搜索节点
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        
        参数:
            root: 当前子树根节点
            key: 要搜索的键值
        返回:
            找到的节点，不存在返回None
        """
        if root is None or root.key == key:
            return root
        
        if root.key < key:
            return self.search(root.right, key)
        
        return self.search(root.left, key)
    
    def rank(self, root, key):
        """
        查询key的排名（比key小的数的个数+1）
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        算法核心：利用子树大小进行快速排名计算
        
        参数:
            root: 当前子树根节点
            key: 要查询排名的键值
        返回:
            key的排名
        """
        if root is None:
            return 1
        
        if key <= root.key:
            # key在左子树，递归查询左子树
            return self.rank(root.left, key)
        else:
            # key在右子树，排名 = 左子树大小 + 当前节点计数 + 右子树中的排名
            return self.get_size(root.left) + root.count + self.rank(root.right, key)
    
    def select(self, root, k):
        """
        查询排名为k的数
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        算法核心：利用子树大小进行快速选择
        
        参数:
            root: 当前子树根节点
            k: 要查询的排名
        返回:
            第k小的键值，不存在返回None
        """
        if root is None:
            return None
        
        left_size = self.get_size(root.left)
        if k <= left_size:
            # 第k小的数在左子树
            return self.select(root.left, k)
        elif k > left_size + root.count:
            # 第k小的数在右子树，调整k值
            return self.select(root.right, k - left_size - root.count)
        else:
            # 第k小的数就是当前节点
            return root.key
    
    def predecessor(self, root, key):
        """
        查询key的前驱（小于key的最大数）
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        边界处理：不存在前驱时返回最小整数
        
        参数:
            root: 当前子树根节点
            key: 要查询前驱的键值
        返回:
            key的前驱
        """
        if root is None:
            return -sys.maxsize - 1  # Python中最小整数
        
        if key <= root.key:
            # 前驱一定在左子树中
            return self.predecessor(root.left, key)
        else:
            # 当前节点可能是前驱，或前驱在右子树中
            # 先在右子树中查找，再与当前节点比较取最大值
            return max(root.key, self.predecessor(root.right, key))
    
    def successor(self, root, key):
        """
        查询key的后继（大于key的最小数）
        时间复杂度：O(log n)
        空间复杂度：O(log n)（递归栈空间）
        边界处理：不存在后继时返回最大整数
        
        参数:
            root: 当前子树根节点
            key: 要查询后继的键值
        返回:
            key的后继
        """
        if root is None:
            return sys.maxsize  # Python中最大整数
        
        if key >= root.key:
            # 后继一定在右子树中
            return self.successor(root.right, key)
        else:
            # 当前节点可能是后继，或后继在左子树中
            # 先在左子树中查找，再与当前节点比较取最小值
            return min(root.key, self.successor(root.left, key))
    
    # 公共接口 - 为用户提供简洁易用的API
    def insert_key(self, key):
        """
        公共接口：插入键
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        参数:
            key: 要插入的键值
        """
        self.root = self.insert(self.root, key)
    
    def delete_key(self, key):
        """
        公共接口：删除键
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        优化：只有当key存在时才执行删除操作
        
        参数:
            key: 要删除的键值
        """
        # 只有当key存在时才删除 - 通过比较排名判断
        if self.rank(self.root, key) != self.rank(self.root, key + 1):
            self.root = self.delete(self.root, key)
    
    def get_rank(self, key):
        """
        公共接口：获取排名
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        参数:
            key: 要查询排名的键值
        返回:
            key的排名
        """
        return self.rank(self.root, key)
    
    def get_select(self, k):
        """
        公共接口：获取第k小的数
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        参数:
            k: 要查询的排名
        返回:
            第k小的键值
        """
        return self.select(self.root, k)
    
    def get_predecessor(self, key):
        """
        公共接口：获取前驱
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        参数:
            key: 要查询前驱的键值
        返回:
            key的前驱
        """
        return self.predecessor(self.root, key)
    
    def get_successor(self, key):
        """
        公共接口：获取后继
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        参数:
            key: 要查询后继的键值
        返回:
            key的后继
        """
        return self.successor(self.root, key)

# 测试代码
if __name__ == "__main__":
    # 由于Python在算法竞赛中的IO效率较低，这里仅提供简单的测试示例
    # 实际使用时建议使用更快的IO方式或改用Java/C++实现
    
    avl = AVLTree()
    
    # 示例操作
    avl.insert_key(10)
    avl.insert_key(20)
    avl.insert_key(30)
    avl.insert_key(40)
    avl.insert_key(50)
    avl.insert_key(25)
    
    print("插入10,20,30,40,50,25后:")
    print("30的排名:", avl.get_rank(30))
    print("第3小的数:", avl.get_select(3))
    print("25的前驱:", avl.get_predecessor(25))
    print("25的后继:", avl.get_successor(25))
    
    avl.delete_key(30)
    print("删除30后:")
    print("30的排名:", avl.get_rank(30))
    print("第3小的数:", avl.get_select(3))

    # 运行完整测试套件
    print("
=== 运行完整测试套件 ===")
    
    # 基本功能测试
    basic_test()
    
    # 重复元素测试
    duplicate_test()
    
    # 边界情况测试
    edge_case_test()
    
    # 性能测试（小规模，避免Python性能问题）
    performance_test(1000)


def basic_test():
    """基本功能测试"""
    print("
=== 基本功能测试 ===")
    avl = AVLTree()
    
    # 插入测试
    avl.insert_key(10)
    avl.insert_key(20)
    avl.insert_key(30)
    avl.insert_key(40)
    avl.insert_key(50)
    avl.insert_key(25)
    
    print("插入10,20,30,40,50,25后:")
    print("30的排名:", avl.get_rank(30))
    print("第3小的数:", avl.get_select(3))
    print("25的前驱:", avl.get_predecessor(25))
    print("25的后继:", avl.get_successor(25))
    
    # 验证BST性质
    if avl.is_valid_bst():
        print("BST性质验证通过")
    else:
        print("BST性质验证失败")
    
    # 验证平衡性
    if avl.is_balanced():
        print("平衡性验证通过")
    else:
        print("平衡性验证失败")


def duplicate_test():
    """重复元素测试"""
    print("
=== 重复元素测试 ===")
    avl = AVLTree()
    
    # 插入重复元素
    avl.insert_key(10)
    avl.insert_key(10)
    avl.insert_key(10)
    avl.insert_key(20)
    avl.insert_key(20)
    
    print("插入3个10和2个20后:")
    print("10的排名:", avl.get_rank(10))
    print("11的排名:", avl.get_rank(11))
    print("第1小的数:", avl.get_select(1))
    print("第3小的数:", avl.get_select(3))
    print("第5小的数:", avl.get_select(5))
    
    # 删除一个10
    avl.delete_key(10)
    print("删除一个10后:")
    print("10的排名:", avl.get_rank(10))
    print("第3小的数:", avl.get_select(3))


def edge_case_test():
    """边界情况测试"""
    print("
=== 边界情况测试 ===")
    avl = AVLTree()
    
    # 空树测试
    print("空树测试:")
    print("1的排名:", avl.get_rank(1))
    print("第1小的数:", avl.get_select(1))
    print("1的前驱:", avl.get_predecessor(1))
    print("1的后继:", avl.get_successor(1))
    
    # 单节点测试
    avl.insert_key(100)
    print("单节点测试:")
    print("50的排名:", avl.get_rank(50))
    print("100的排名:", avl.get_rank(100))
    print("150的排名:", avl.get_rank(150))
    print("100的前驱:", avl.get_predecessor(100))
    print("100的后继:", avl.get_successor(100))
    
    # 极值测试
    avl.insert_key(-1000000)
    avl.insert_key(1000000)
    print("极值测试:")
    print("-1000000的前驱:", avl.get_predecessor(-1000000))
    print("1000000的后继:", avl.get_successor(1000000))


def performance_test(n):
    """性能测试"""
    print(f"
=== 性能测试（插入{n}个随机数据） ===")
    import time
    import random
    
    avl = AVLTree()
    
    start_time = time.time()
    
    # 插入随机数据
    for i in range(n):
        num = random.randint(-1000000, 1000000)
        avl.insert_key(num)
    
    insert_time = time.time() - start_time
    
    # 查询测试
    start_time = time.time()
    for i in range(min(n, 100)):  # 避免查询时间过长
        avl.get_rank(random.randint(-1000000, 1000000))
    
    query_time = time.time() - start_time
    
    print(f"插入时间: {insert_time:.4f}秒")
    print(f"查询时间: {query_time:.4f}秒")
    print(f"树高度: {avl.get_tree_height()}")
    print(f"总元素个数: {avl.get_total_size()}")
    print(f"不同元素个数: {avl.get_distinct_count()}")


# 为AVLTree类添加验证方法
AVLTree.is_valid_bst = lambda self: self._is_valid_bst(self.root, -sys.maxsize - 1, sys.maxsize)
AVLTree._is_valid_bst = lambda self, node, min_val, max_val: (
    node is None or 
    (min_val < node.key < max_val and 
     self._is_valid_bst(node.left, min_val, node.key) and 
     self._is_valid_bst(node.right, node.key, max_val))
)

AVLTree.is_balanced = lambda self: self._is_balanced(self.root)
AVLTree._is_balanced = lambda self, node: (
    node is None or 
    (abs(self.get_height(node.left) - self.get_height(node.right)) <= 1 and 
     self._is_balanced(node.left) and 
     self._is_balanced(node.right))
)

AVLTree.get_tree_height = lambda self: self.get_height(self.root)
AVLTree.get_total_size = lambda self: self.get_size(self.root)

AVLTree.get_distinct_count = lambda self: self._get_distinct_count(self.root)
AVLTree._get_distinct_count = lambda self, node: (
    0 if node is None else 
    1 + self._get_distinct_count(node.left) + self._get_distinct_count(node.right)
)

AVLTree.inorder_traversal = lambda self: self._inorder_traversal(self.root)
AVLTree._inorder_traversal = lambda self, node: (
    [] if node is None else 
    self._inorder_traversal(node.left) + [node.key] * node.count + self._inorder_traversal(node.right)
)