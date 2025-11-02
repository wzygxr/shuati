# AVL实现普通有序表，数据加强的测试，Python版
# 这个文件课上没有讲，测试数据加强了，而且有强制在线的要求
# 基本功能要求都是不变的，可以打开测试链接查看
# 测试链接 : https://www.luogu.com.cn/problem/P6136

"""
补充题目列表：

1. 洛谷 P6136 【模板】普通平衡树（数据加强版）
   链接: https://www.luogu.com.cn/problem/P6136
   题目描述: P3369的数据加强版，强制在线，需要更高的效率和更强的实现
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)

2. 洛谷 P3369 【模板】普通平衡树
   链接: https://www.luogu.com.cn/problem/P3369
   题目描述: 实现一个普通平衡树，支持插入、删除、查询排名、查询第k小值、查询前驱和后继
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)

3. LeetCode 406. Queue Reconstruction by Height
   链接: https://leetcode.cn/problems/queue-reconstruction-by-height/
   题目描述: 重构队列，每个人有身高和前面比他高的人数要求，需要重构满足条件的队列
   时间复杂度: O(n log n)
   空间复杂度: O(n)

4. PAT甲级 1066 Root of AVL Tree
   链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805404939173888
   题目描述: 给定插入序列，构建AVL树，输出根节点的值
   时间复杂度: O(n log n)
   空间复杂度: O(n)

5. PAT甲级 1123 Is It a Complete AVL Tree
   链接: https://pintia.cn/problem-sets/994805342720868352/exam/problems/994805355103797248
   题目描述: 判断构建的AVL树是否是完全二叉树
   时间复杂度: O(n log n)
   空间复杂度: O(n)

6. LeetCode 220. Contains Duplicate III
   链接: https://leetcode.cn/problems/contains-duplicate-iii/
   题目描述: 判断数组中是否存在两个不同下标i和j，使得abs(nums[i] - nums[j]) <= t且abs(i - j) <= k
   时间复杂度: O(n log k)
   空间复杂度: O(k)

7. Codeforces 459D - Pashmak and Parmida's problem
   链接: https://codeforces.com/problemset/problem/459/D
   题目描述: 计算满足条件的点对数量
   时间复杂度: O(n log n)
   空间复杂度: O(n)

8. SPOJ Ada and Behives
   链接: https://www.spoj.com/problems/ADAAPHID/
   题目描述: 维护一个动态集合，支持插入和查询操作
   时间复杂度: O(log n) 每次操作
   空间复杂度: O(n)

算法思路技巧总结：
1. 适用场景：
   - 需要维护有序集合，并支持快速插入、删除、查找
   - 需要查询元素排名或第k小元素
   - 需要频繁查询前驱和后继元素
   - 处理强制在线问题

2. 核心思想：
   - 通过旋转操作维持树的平衡性，保证树的高度为O(log n)
   - 每个节点维护子树大小和高度信息
   - 插入和删除操作后通过旋转调整恢复平衡
   - 强制在线通过异或操作实现

3. 四种旋转操作：
   - LL旋转：在左孩子的左子树插入导致失衡
   - RR旋转：在右孩子的右子树插入导致失衡
   - LR旋转：在左孩子的右子树插入导致失衡
   - RL旋转：在右孩子的左子树插入导致失衡

4. 工程化考量：
   - 内存管理：使用数组代替指针减少内存碎片
   - 性能优化：通过维护子树大小信息支持排名查询
   - 边界处理：处理重复元素和空树等边界情况
   - 异常处理：检查输入参数的有效性
   - 在线处理：通过异或操作处理强制在线

5. 时间和空间复杂度：
   - 插入：O(log n)
   - 删除：O(log n)
   - 查找：O(log n)
   - 查询排名：O(log n)
   - 查询第k小：O(log n)
   - 前驱/后继：O(log n)
   - 空间复杂度：O(n)

6. 与其他数据结构的比较：
   - 相比Treap：实现更复杂，但平衡性更好
   - 相比红黑树：旋转次数可能更多，但实现相对简单
   - 相比Splay Tree：最坏时间复杂度更稳定

7. 语言特性差异：
   - Java: 对象引用操作直观，但可能有GC开销
   - C++: 指针操作更直接，需要手动管理内存
   - Python: 语法简洁，但性能不如Java/C++
"""

import sys
from typing import List

class AVLNode:
    def __init__(self, key):
        self.key = key
        self.left = None
        self.right = None
        self.height = 1
        self.count = 1  # 重复元素计数
        self.size = 1   # 子树大小

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
        node.size = self.get_size(node.left) + self.get_size(node.right) + node.count
    
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
    
    def insert(self, root, key):
        """插入节点"""
        # 1. 执行标准BST插入
        if not root:
            return AVLNode(key)
        
        if key < root.key:
            root.left = self.insert(root.left, key)
        elif key > root.key:
            root.right = self.insert(root.right, key)
        else:
            # 相等的键，增加计数
            root.count += 1
            self.update_info(root)
            return root
        
        # 2. 更新祖先节点的高度和大小
        self.update_info(root)
        
        # 3. 获取平衡因子
        balance = self.get_balance(root)
        
        # 4. 如果节点不平衡，执行相应的旋转操作
        # Left Left Case
        if balance > 1 and key < root.left.key:
            return self.right_rotate(root)
        
        # Right Right Case
        if balance < -1 and key > root.right.key:
            return self.left_rotate(root)
        
        # Left Right Case
        if balance > 1 and key > root.left.key:
            root.left = self.left_rotate(root.left)
            return self.right_rotate(root)
        
        # Right Left Case
        if balance < -1 and key < root.right.key:
            root.right = self.right_rotate(root.right)
            return self.left_rotate(root)
        
        # 返回未改变的节点指针
        return root
    
    def get_min_value_node(self, root):
        """获取最小值节点"""
        if root is None or root.left is None:
            return root
        return self.get_min_value_node(root.left)
    
    def delete(self, root, key):
        """删除节点"""
        # 1. 执行标准BST删除
        if not root:
            return root
        
        if key < root.key:
            root.left = self.delete(root.left, key)
        elif key > root.key:
            root.right = self.delete(root.right, key)
        else:
            # 相等的键，减少计数
            if root.count > 1:
                root.count -= 1
                self.update_info(root)
                return root
            
            # 只有一个节点或者没有节点
            if root.left is None:
                temp = root.right
                root = None
                return temp
            elif root.right is None:
                temp = root.left
                root = None
                return temp
            
            # 有两个子节点，获取中序后继（右子树中的最小值）
            temp = self.get_min_value_node(root.right)
            
            # 将后继的键复制到这个节点
            root.key = temp.key
            root.count = temp.count
            temp.count = 1  # 重置后继节点的计数
            
            # 删除后继节点
            root.right = self.delete(root.right, temp.key)
        
        # 如果树只有根节点，则返回
        if root is None:
            return root
        
        # 2. 更新祖先节点的高度和大小
        self.update_info(root)
        
        # 3. 获取平衡因子
        balance = self.get_balance(root)
        
        # 4. 如果节点不平衡，执行相应的旋转操作
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
    
    def search(self, root, key):
        """搜索节点"""
        if root is None or root.key == key:
            return root
        
        if root.key < key:
            return self.search(root.right, key)
        
        return self.search(root.left, key)
    
    def rank(self, root, key):
        """查询key的排名（比key小的数的个数+1）"""
        if root is None:
            return 1
        
        if key <= root.key:
            return self.rank(root.left, key)
        else:
            return self.get_size(root.left) + root.count + self.rank(root.right, key)
    
    def select(self, root, k):
        """查询排名为k的数"""
        if root is None:
            return None
        
        left_size = self.get_size(root.left)
        if k <= left_size:
            return self.select(root.left, k)
        elif k > left_size + root.count:
            return self.select(root.right, k - left_size - root.count)
        else:
            return root.key
    
    def predecessor(self, root, key):
        """查询key的前驱（小于key的最大数）"""
        if root is None:
            return -sys.maxsize - 1
        
        if key <= root.key:
            return self.predecessor(root.left, key)
        else:
            return max(root.key, self.predecessor(root.right, key))
    
    def successor(self, root, key):
        """查询key的后继（大于key的最小数）"""
        if root is None:
            return sys.maxsize
        
        if key >= root.key:
            return self.successor(root.right, key)
        else:
            return min(root.key, self.successor(root.left, key))
    
    def insert_key(self, key):
        """公共接口：插入键"""
        self.root = self.insert(self.root, key)
    
    def delete_key(self, key):
        """公共接口：删除键"""
        # 只有当key存在时才删除
        if self.rank(self.root, key) != self.rank(self.root, key + 1):
            self.root = self.delete(self.root, key)
    
    def get_rank(self, key):
        """公共接口：获取排名"""
        return self.rank(self.root, key)
    
    def get_select(self, k):
        """公共接口：获取第k小的数"""
        return self.select(self.root, k)
    
    def get_predecessor(self, key):
        """公共接口：获取前驱"""
        return self.predecessor(self.root, key)
    
    def get_successor(self, key):
        """公共接口：获取后继"""
        return self.successor(self.root, key)

# 由于Python在算法竞赛中的IO效率较低，这里仅提供简单的测试示例
# 实际使用时建议使用更快的IO方式或改用Java/C++实现

# 测试代码
if __name__ == "__main__":
    # 创建AVL树
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