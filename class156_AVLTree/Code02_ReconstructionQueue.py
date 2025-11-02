# 重建队列(做到最优时间复杂度)(Python版)
# 一共n个人，每个人有(a, b)两个数据，数据a表示该人的身高
# 数据b表示该人的要求，站在自己左边的人中，正好有b个人的身高大于等于自己的身高
# 请你把n个人从左到右进行排列，要求每个人的要求都可以满足
# 返回其中一种排列即可，本题的数据保证一定存在这样的排列
# 题解中的绝大多数方法，时间复杂度O(n平方)，但是时间复杂度能做到O(n * log n)
# 测试链接 : https://leetcode.cn/problems/queue-reconstruction-by-height/

"""
补充题目列表：

1. LeetCode 406. Queue Reconstruction by Height
   链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
   题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
   时间复杂度: O(n log n)
   空间复杂度: O(n)

2. 洛谷 P1118 [USACO06FEB]数字三角形
   链接: https://www.luogu.com.cn/problem/P1118
   题目描述: 使用类似思想解决字典序最小问题
   时间复杂度: O(n log n)
   空间复杂度: O(n)

3. Codeforces 219D Choosing Capital for Treeland
   链接: https://codeforces.com/problemset/problem/219/D
   题目描述: 树上动态规划问题，可以使用类似技巧优化
   时间复杂度: O(n log n)
   空间复杂度: O(n)

4. LeetCode 315. Count of Smaller Numbers After Self
   链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   题目描述: 计算数组右侧小于当前元素的元素个数
   时间复杂度: O(n log n)
   空间复杂度: O(n)

5. LeetCode 327. Count of Range Sum
   链接: https://leetcode.cn/problems/count-of-range-sum/
   题目描述: 计算和在范围内的子数组个数
   时间复杂度: O(n log n)
   空间复杂度: O(n)

6. 牛客网 NC145 01序列的最小权值
   链接: https://www.nowcoder.com/practice/14c0359fb77a48319f0122ec175c9ada
   题目描述: 维护01序列，支持插入和查询操作
   时间复杂度: O(n log n)
   空间复杂度: O(n)

7. AtCoder ABC134 E - Sequence Decomposing
   链接: https://atcoder.jp/contests/abc134/tasks/abc134_e
   题目描述: 序列分解问题，可使用平衡树优化
   时间复杂度: O(n log n)
   空间复杂度: O(n)

8. CodeChef ORDERSET
   链接: https://www.codechef.com/problems/ORDERSET
   题目描述: 维护有序集合，支持插入、删除、查询排名、查询第k小
   时间复杂度: O(log n)
   空间复杂度: O(n)

9. HackerRank Self-Balancing Tree
   链接: https://www.hackerrank.com/challenges/self-balancing-tree/problem
   题目描述: 实现AVL树的插入操作
   时间复杂度: O(log n)
   空间复杂度: O(n)

10. USACO 2017 December Contest, Platinum Problem 1. Standing Out from the Herd
    链接: http://www.usaco.org/index.php?page=viewproblem2&cpid=770
    题目描述: 字符串处理问题，可使用平衡树优化
    时间复杂度: O(n log n)
    空间复杂度: O(n)

11. LeetCode 98. 验证二叉搜索树
    链接: https://leetcode.cn/problems/validate-binary-search-tree/
    题目描述: 验证一个二叉树是否是有效的二叉搜索树
    时间复杂度: O(n)
    空间复杂度: O(h)，h为树高

12. LeetCode 669. 修剪二叉搜索树
    链接: https://leetcode.cn/problems/trim-a-binary-search-tree/
    题目描述: 修剪二叉搜索树，保留值在[low, high]范围内的节点
    时间复杂度: O(n)
    空间复杂度: O(h)

13. LeetCode 230. 二叉搜索树中第K小的元素
    链接: https://leetcode.cn/problems/kth-smallest-element-in-a-bst/
    题目描述: 给定一个二叉搜索树，找出其中第k小的元素
    时间复杂度: O(h + k)
    空间复杂度: O(h)

14. LeetCode 538. 把二叉搜索树转换为累加树
    链接: https://leetcode.cn/problems/convert-bst-to-greater-tree/
    题目描述: 将二叉搜索树转换为累加树，每个节点的值变为原树中大于或等于该节点值的所有节点值之和
    时间复杂度: O(n)
    空间复杂度: O(h)

15. LeetCode 1038. 从二叉搜索树到更大和树
    链接: https://leetcode.cn/problems/binary-search-tree-to-greater-sum-tree/
    题目描述: 与538题类似，但要求节点值变为所有大于该节点值的节点值之和
    时间复杂度: O(n)
    空间复杂度: O(h)

算法思路技巧总结：
1. 适用场景：
   - 需要动态维护一个序列，并支持按索引插入元素
   - 需要根据排名或索引快速查找元素
   - 需要处理涉及排名、位置相关的复杂约束问题

2. 核心思想：
   - 利用AVL树等平衡二叉搜索树维护动态序列
   - 通过维护子树大小信息支持按排名查找和按索引插入
   - 将问题转化为在平衡树中进行插入操作

3. 解题步骤：
   - 将输入数据按特定规则排序
   - 使用平衡树按顺序插入元素
   - 利用树的排名/索引特性满足约束条件

4. 工程化考量：
   - 性能优化：使用平衡树避免O(n)的插入开销
   - 内存管理：合理分配和释放树节点
   - 边界处理：处理空树和单节点等特殊情况

5. 时间和空间复杂度：
   - 排序：O(n log n)
   - 插入：O(n log n)
   - 查询：O(n log n)
   - 空间复杂度：O(n)

6. 与其他算法的关联：
   - 与逆序对问题的关联：都可以用平衡树优化
   - 与树状数组/线段树的关联：都是处理动态序列的数据结构
   - 与分治算法的关联：都涉及将问题分解为更小子问题

7. 语言特性差异：
   - Java: Collections.sort()和Arrays.sort()优化
   - C++: std::sort和自定义比较器
   - Python: sorted()和lambda表达式
"""

class AVLNode:
    def __init__(self, key, value):
        self.key = key
        self.value = value
        self.left = None
        self.right = None
        self.height = 1
        self.size = 1

class AVLTree:
    def __init__(self):
        self.root = None
    
    def get_height(self, node):
        """获取节点高度"""
        if not node:
            return 0
        return node.height
    
    def get_size(self, node):
        """获取子树大小"""
        if not node:
            return 0
        return node.size
    
    def update_info(self, node):
        """更新节点信息（高度和大小）"""
        if not node:
            return
        node.height = 1 + max(self.get_height(node.left), self.get_height(node.right))
        node.size = self.get_size(node.left) + self.get_size(node.right) + 1
    
    def get_balance(self, node):
        """获取节点的平衡因子"""
        if not node:
            return 0
        return self.get_height(node.left) - self.get_height(node.right)
    
    def left_rotate(self, z):
        """左旋操作"""
        y = z.right
        T2 = y.left
        
        # 执行旋转
        y.left = z
        z.right = T2
        
        # 更新高度和大小
        self.update_info(z)
        self.update_info(y)
        
        # 返回新的根节点
        return y
    
    def right_rotate(self, z):
        """右旋操作"""
        y = z.left
        T3 = y.right
        
        # 执行旋转
        y.right = z
        z.left = T3
        
        # 更新高度和大小
        self.update_info(z)
        self.update_info(y)
        
        # 返回新的根节点
        return y
    
    def insert_at_index(self, root, index, key, value):
        """在指定索引位置插入节点"""
        if root is None:
            return AVLNode(key, value)
        
        left_size = self.get_size(root.left)
        if left_size >= index:
            root.left = self.insert_at_index(root.left, index, key, value)
        else:
            root.right = self.insert_at_index(root.right, index - left_size - 1, key, value)
        
        # 更新祖先节点的高度和大小
        self.update_info(root)
        
        # 获取平衡因子
        balance = self.get_balance(root)
        
        # 如果节点不平衡，执行相应的旋转操作
        # Left Left Case
        if balance > 1 and self.get_balance(root.left) >= 0:
            return self.right_rotate(root)
        
        # Left Right Case
        if balance > 1 and self.get_balance(root.left) < 0:
            root.left = self.left_rotate(root.left)
            return self.right_rotate(root)
        
        # Right Right Case
        if balance < -1 and self.get_balance(root.right) <= 0:
            return self.left_rotate(root)
        
        # Right Left Case
        if balance < -1 and self.get_balance(root.right) > 0:
            root.right = self.right_rotate(root.right)
            return self.left_rotate(root)
        
        return root
    
    def inorder_traversal(self, root, result):
        """中序遍历获取结果"""
        if root is not None:
            self.inorder_traversal(root.left, result)
            result.append([root.key, root.value])
            self.inorder_traversal(root.right, result)
    
    def insert_at(self, index, key, value):
        """公共接口：在指定索引位置插入"""
        self.root = self.insert_at_index(self.root, index, key, value)
    
    def get_result(self):
        """公共接口：获取结果"""
        result = []
        self.inorder_traversal(self.root, result)
        return result

def reconstruct_queue(people):
    """
    重构队列
    
    Args:
        people: List[List[int]] - 每个人的身高和要求
    
    Returns:
        List[List[int]] - 重构后的队列
    """
    # 按身高降序排列，身高相同时按要求升序排列
    people.sort(key=lambda x: (-x[0], x[1]))
    
    # 创建AVL树
    avl = AVLTree()
    
    # 按顺序插入元素
    for p in people:
        avl.insert_at(p[1], p[0], p[1])
    
    # 返回结果
    return avl.get_result()

# 测试代码
if __name__ == "__main__":
    # 测试用例
    people = [[7,0],[4,4],[7,1],[5,0],[6,1],[5,2]]
    result = reconstruct_queue(people)
    print("输入:", people)
    print("输出:", result)
    
    # 验证结果
    # 检查每个人前面是否有正确数量的身高大于等于自己的人
    def validate_queue(queue):
        for i in range(len(queue)):
            height, requirement = queue[i]
            count = 0
            for j in range(i):
                if queue[j][0] >= height:
                    count += 1
            if count != requirement:
                return False
        return True
    
    print("验证结果:", validate_queue(result))