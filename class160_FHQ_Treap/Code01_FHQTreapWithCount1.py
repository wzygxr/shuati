# FHQ-Treap（无旋Treap）带词频压缩实现 - Python版本
# 
# 【算法原理】
# FHQ-Treap结合了二叉搜索树（BST）和堆（Treap）的特性：
# 1. 满足BST性质：左子树节点值 ≤ 根节点值 ≤ 右子树节点值
# 2. 满足堆性质：父节点优先级 ≥ 子节点优先级（使用随机优先级）
# 
# 【核心操作】
# - split：分裂操作，根据值将树分为两部分
# - merge：合并操作，将两棵满足条件的树合并
# 
# 【词频压缩】
# 对于重复值，只存储一个节点并维护count计数，减少空间占用和操作次数
# 
# 【时间复杂度分析】
# 所有操作的期望时间复杂度：O(log n)，其中n为元素总数
# 
# 【空间复杂度分析】
# O(m)，其中m为不同值的数量（m ≤ n）
# 
# 【适用题目】
# - 洛谷 P3369 普通平衡树
#   题目链接：https://www.luogu.com.cn/problem/P3369
#   题目描述：维护一个有序集合，支持插入、删除、查询排名、查询第k小数、前驱、后继等操作
# - LeetCode 456 132模式（可用于高效查找前驱）
#   题目链接：https://leetcode.cn/problems/132-pattern/
#   题目描述：判断数组中是否存在132模式的子序列
# - LeetCode 2336 无限集中的最小数字（支持动态插入删除）
#   题目链接：https://leetcode.cn/problems/smallest-number-in-infinite-set/
#   题目描述：维护一个包含所有正整数的无限集，支持弹出最小元素和添加元素
# - LeetCode 1845 座位预约管理系统
#   题目链接：https://leetcode.cn/problems/seat-reservation-manager/
#   题目描述：实现一个座位预约管理系统，支持预约和取消预约操作
# - SPOJ ORDERSET：Order statistic set
#   题目链接：https://www.spoj.com/problems/ORDERSET/
#   题目描述：维护一个动态集合，支持插入、删除、查询第k小数、查询某数的排名等操作
# - 各种需要动态维护有序集合的场景
# 
# 【输入输出】
# 操作数：n ≤ 10^5
# 数值范围：-10^7 ≤ x ≤ +10^7
# 操作类型：6种基本操作
# 
# 【语言特性注意】
# Python中递归深度限制默认为1000，对于大数据量可能需要调整递归深度
# 但FHQ-Treap的递归深度为O(log n)，对于n ≤ 10^5，log2(n) ≈ 17，所以不会超过限制
# 提交时请把类名改成"Main"，可以通过所有测试用例

import sys
import random
from io import StringIO

class FHQTreapWithCount:
    """
    FHQ-Treap数据结构实现类
    支持动态集合的插入、删除、排名查询、第k小查询、前驱查询、后继查询操作
    使用词频压缩优化空间使用
    """
    
    def __init__(self, max_n=100001):
        """
        初始化FHQ-Treap数据结构
        
        Args:
            max_n (int): 最大节点数量，默认为100001（根据题目约束设置）
            
        【数据结构设计】
        - 使用数组模拟树结构，提高缓存命中率
        - 每个节点包含键值、词频、左右子节点、子树大小和优先级信息
        """
        self.MAXN = max_n
        self.head = 0  # 整棵树的头节点编号（根节点）
        self.cnt = 0   # 空间使用计数，记录当前已分配的节点数量
        
        # 节点信息数组 - 使用预分配数组而非链表，提高性能
        self.key = [0] * self.MAXN      # 节点的键值
        self.count = [0] * self.MAXN    # 词频计数：记录每个键值出现的次数
        self.left = [0] * self.MAXN     # 左子节点索引
        self.right = [0] * self.MAXN    # 右子节点索引
        self.size = [0] * self.MAXN     # 子树大小：当前节点及其子树中元素的总数
        self.priority = [0.0] * self.MAXN  # 节点优先级：随机生成，保证树的平衡
        
        # 初始化随机数种子，确保优先级的随机性
        random.seed(42)  # 设置固定种子以保证结果可复现，实际使用时可移除
        
    def up(self, i):
        """
        更新节点的子树大小
        
        Args:
            i (int): 当前节点编号
            
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        【实现说明】
        维护子树大小信息，用于快速计算排名和第k小元素
        子树大小 = 左子树大小 + 右子树大小 + 当前节点的词频
        """
        self.size[i] = self.size[self.left[i]] + self.size[self.right[i]] + self.count[i]
    
    def split(self, l, r, i, num):
        """
        分裂操作：按值分裂
        将以i为根的子树分裂成两棵树，左树包含所有值<=num的节点，右树包含所有值>num的节点
        
        Args:
            l (int): 左树的根节点（作为输出参数）
            r (int): 右树的根节点（作为输出参数）
            i (int): 当前处理的节点
            num (int): 分裂值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【实现细节】
        - 使用递归方式分裂子树
        - 利用right和left数组作为输出参数，存储分裂后的根节点
        - 每次分裂后需要更新节点的子树大小
        
        【算法思想】
        分裂是FHQ-Treap的核心操作，它使得我们可以将树分成两部分进行操作，然后通过merge合并
        这种设计使得FHQ-Treap不需要旋转操作就能维护平衡
        """
        # 边界条件：空树
        if i == 0:
            self.right[l] = self.left[r] = 0
        else:
            # 根据当前节点的值决定分裂方向
            if self.key[i] <= num:
                # 当前节点及其左子树属于左树，继续分裂右子树
                self.right[l] = i
                self.split(i, r, self.right[i], num)
            else:
                # 当前节点及其右子树属于右树，继续分裂左子树
                self.left[r] = i
                self.split(l, i, self.left[i], num)
            # 分裂后更新当前节点的子树大小
            self.up(i)
    
    def merge(self, l, r):
        """
        合并操作：将两棵满足条件的树合并成一棵树
        前提条件：左树中所有节点的值 <= 右树中所有节点的值
        
        Args:
            l (int): 左树的根节点
            r (int): 右树的根节点
            
        Returns:
            int: 合并后的树的根节点
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【实现细节】
        - 根据堆性质（优先级）决定合并方向
        - 优先级高的节点作为根，递归合并子树
        - 合并后更新节点的子树大小
        
        【算法思想】
        合并操作利用优先级来维护树的平衡，确保树的高度保持在O(log n)级别
        这使得所有操作的期望时间复杂度都是O(log n)
        """
        # 边界条件：其中一棵树为空
        if l == 0 or r == 0:
            return l + r  # 返回非空的树
        # 根据堆性质（优先级）决定合并方向
        if self.priority[l] >= self.priority[r]:
            # 左树根节点优先级更高，作为新根，递归合并其右子树与右树
            self.right[l] = self.merge(self.right[l], r)
            self.up(l)
            return l
        else:
            # 右树根节点优先级更高，作为新根，递归合并其左子树与左树
            self.left[r] = self.merge(l, self.left[r])
            self.up(r)
            return r
    
    def find(self, i, num):
        """
        在树中查找指定值的节点
        
        Args:
            i (int): 当前搜索的根节点
            num (int): 要查找的值
            
        Returns:
            int: 找到的节点编号，未找到返回0
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【算法思想】
        利用二叉搜索树的特性进行查找，时间复杂度与树的高度相关
        """
        if i == 0:
            return 0  # 未找到
        if self.key[i] == num:
            return i  # 找到目标节点
        elif self.key[i] > num:
            return self.find(self.left[i], num)  # 在左子树中查找
        else:
            return self.find(self.right[i], num)  # 在右子树中查找
    
    def change_count(self, i, num, change):
        """
        修改指定值的节点的计数
        
        Args:
            i (int): 当前搜索的根节点
            num (int): 要修改的值
            change (int): 计数变化量（+1或-1）
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【注意事项】
        调用此方法前必须确保节点存在
        
        【工程化考量】
        词频压缩是一种空间优化技术，对于存在大量重复元素的场景非常有效
        但需要注意维护词频为0时需要删除节点
        """
        if self.key[i] == num:
            # 找到目标节点，修改计数
            self.count[i] += change
        elif self.key[i] > num:
            self.change_count(self.left[i], num, change)  # 在左子树中查找并修改
        else:
            self.change_count(self.right[i], num, change)  # 在右子树中查找并修改
        # 修改后更新子树大小
        self.up(i)
    
    def add(self, num):
        """
        添加元素操作
        
        Args:
            num (int): 要添加的值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 包含find、change_count或split/merge的递归栈深度
        
        【实现思路】
        1. 先检查值是否已存在
        2. 如果存在，增加词频计数
        3. 如果不存在，创建新节点并插入到树中
        
        【边界场景处理】
        - 空树情况：直接创建新节点
        - 重复值情况：增加词频计数，避免创建新节点
        """
        # 检查值是否已存在
        if self.find(self.head, num) != 0:
            # 值已存在，增加词频计数
            self.change_count(self.head, num, 1)
        else:
            # 值不存在，创建新节点并插入
            self.split(0, 0, self.head, num)
            # 分配新节点
            self.cnt += 1
            self.key[self.cnt] = num
            self.count[self.cnt] = 1
            self.size[self.cnt] = 1
            self.priority[self.cnt] = random.random()  # 随机优先级
            # 合并：<=num的部分 + 新节点 + >num的部分
            self.head = self.merge(self.merge(self.right[0], self.cnt), self.left[0])
    
    def remove(self, num):
        """
        删除元素操作
        
        Args:
            num (int): 要删除的值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 包含find、change_count或两次split/merge的递归栈深度
        
        【实现思路】
        1. 先检查值是否存在
        2. 如果存在且计数>1，减少词频计数
        3. 如果存在且计数=1，将该节点完全删除（通过两次分裂）
        
        【算法细节】
        - 两次分裂操作：第一次分裂出<=num的部分，第二次分裂出<num的部分
        - 这样就排除了=num的节点，然后合并剩下的两部分
        """
        # 查找值对应的节点
        i = self.find(self.head, num)
        if i != 0:
            if self.count[i] > 1:
                # 计数大于1，只减少计数
                self.change_count(self.head, num, -1)
            else:
                # 计数等于1，需要完全删除节点
                # 第一次分裂：分成<=num和>num两部分
                self.split(0, 0, self.head, num)
                lm = self.right[0]  # <=num的部分
                r = self.left[0]   # >num的部分
                # 第二次分裂：将lm分成<num和=num两部分
                self.split(0, 0, lm, num - 1)
                l = self.right[0]   # <num的部分
                # 合并<num和>num的部分，相当于删除=num的部分
                self.head = self.merge(l, r)
        # 如果值不存在，不做任何操作
    
    def small(self, i, num):
        """
        统计小于num的元素个数
        
        Args:
            i (int): 当前搜索的根节点
            num (int): 目标值
            
        Returns:
            int: 小于num的元素个数
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【算法思想】
        利用子树大小信息快速统计比某个值小的元素个数
        这是计算排名的关键操作
        """
        if i == 0:
            return 0  # 空树
        if self.key[i] >= num:
            # 当前节点值>=num，继续在左子树中统计
            return self.small(self.left[i], num)
        else:
            # 当前节点值<num，统计结果包括左子树、当前节点及右子树中<num的部分
            return self.size[self.left[i]] + self.count[i] + self.small(self.right[i], num)
    
    def rank(self, num):
        """
        查询元素的排名（比num小的数的个数+1）
        
        Args:
            num (int): 查询的值
            
        Returns:
            int: num的排名
            
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        【定义】
        排名 = 小于num的元素个数 + 1
        这符合标准的排名定义，即严格小于目标值的元素个数加一
        """
        # 排名 = 小于num的元素个数 + 1
        return self.small(self.head, num) + 1
    
    def index(self, i, x):
        """
        查询第k小的元素（递归实现）
        
        Args:
            i (int): 当前搜索的根节点
            x (int): 排名（第x小）
            
        Returns:
            int: 第x小的元素值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【实现思路】
        1. 如果左子树大小 >= x，说明第x小在左子树中
        2. 如果左子树大小 + 当前节点计数 < x，说明第x小在右子树中
        3. 否则，当前节点就是第x小的元素
        
        【边界条件】
        - x必须是有效的排名（1 <= x <= size[head]）
        """
        if self.size[self.left[i]] >= x:
            # 第x小在左子树
            return self.index(self.left[i], x)
        elif self.size[self.left[i]] + self.count[i] < x:
            # 第x小在右子树，调整查询位置
            return self.index(self.right[i], x - self.size[self.left[i]] - self.count[i])
        # 当前节点就是第x小的元素
        return self.key[i]
    
    def index_by_rank(self, x):
        """
        查询第k小的元素（对外接口）
        
        Args:
            x (int): 排名（第x小）
            
        Returns:
            int: 第x小的元素值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        
        【异常处理】
        注意：此实现假设x是有效的排名（1 <= x <= size[head]）
        在实际应用中应该添加边界检查
        """
        # 工程化优化：添加边界检查
        if x < 1 or x > self.size[self.head]:
            raise ValueError(f"Invalid rank: {x}, must be between 1 and {self.size[self.head]}")
        return self.index(self.head, x)
    
    def pre(self, i, num):
        """
        查询前驱（小于num的最大元素）
        
        Args:
            i (int): 当前搜索的根节点
            num (int): 目标值
            
        Returns:
            int: 前驱元素值，如果不存在返回Integer.MIN_VALUE(-2147483648)
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【实现思路】
        1. 如果当前节点值 >= num，前驱一定在左子树中
        2. 如果当前节点值 < num，前驱可能是当前节点或右子树中的某个节点
        
        【应用场景】
        前驱查询在很多算法问题中都有应用，如132模式、逆序对统计等
        """
        if i == 0:
            return -2147483648  # 空树，无前驱
        if self.key[i] >= num:
            # 当前节点值>=num，前驱在左子树
            return self.pre(self.left[i], num)
        else:
            # 当前节点值<num，比较当前节点和右子树中的前驱
            return max(self.key[i], self.pre(self.right[i], num))
    
    def pre_by_value(self, num):
        """
        查询前驱（对外接口）
        
        Args:
            num (int): 目标值
            
        Returns:
            int: 前驱元素值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        """
        return self.pre(self.head, num)
    
    def post(self, i, num):
        """
        查询后继（大于num的最小元素）
        
        Args:
            i (int): 当前搜索的根节点
            num (int): 目标值
            
        Returns:
            int: 后继元素值，如果不存在返回Integer.MAX_VALUE(2147483647)
            
        时间复杂度：O(log n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【实现思路】
        1. 如果当前节点值 <= num，后继一定在右子树中
        2. 如果当前节点值 > num，后继可能是当前节点或左子树中的某个节点
        
        【应用场景】
        后继查询常用于区间查询、最近公共祖先等问题
        """
        if i == 0:
            return 2147483647  # 空树，无后继
        if self.key[i] <= num:
            # 当前节点值<=num，后继在右子树
            return self.post(self.right[i], num)
        else:
            # 当前节点值>num，比较当前节点和左子树中的后继
            return min(self.key[i], self.post(self.left[i], num))
    
    def post_by_value(self, num):
        """
        查询后继（对外接口）
        
        Args:
            num (int): 目标值
            
        Returns:
            int: 后继元素值
            
        时间复杂度：O(log n)
        空间复杂度：O(log n)
        """
        return self.post(self.head, num)
    
    def inorder(self, i):
        """
        中序遍历函数：用于调试和验证树的正确性
        
        Args:
            i (int): 当前节点
            
        Returns:
            list: 中序遍历结果列表
            
        时间复杂度：O(n)
        空间复杂度：O(log n) - 递归调用栈深度
        
        【工程化工具】
        此方法用于调试，验证树的结构是否正确，所有元素是否按顺序排列
        """
        result = []
        if i == 0:
            return result
        # 访问左子树
        result.extend(self.inorder(self.left[i]))
        # 添加当前节点的所有元素
        result.extend([self.key[i]] * self.count[i])
        # 访问右子树
        result.extend(self.inorder(self.right[i]))
        return result


def main():
    """
    主函数：处理输入输出和操作调用
    
    【输入格式】
    第一行：操作数n
    接下来n行：每行一个操作和参数
    操作类型：
    1 x：插入x
    2 x：删除x
    3 x：查询x的排名
    4 x：查询第x小的数
    5 x：查询x的前驱
    6 x：查询x的后继
    
    【性能优化】
    - 使用sys.stdin.read()一次性读取所有输入，避免多次IO操作
    - 将输入拆分为列表后通过索引访问，提高处理速度
    
    【工程化考量】
    - 添加异常处理，确保程序在遇到非法输入时不会崩溃
    - 提供清晰的错误提示信息
    """
    # 重定向输入输出用于测试 - 实际提交时应移除
    # input_text = """
    # 10
    # 1 7
    # 1 2
    # 1 2
    # 1 5
    # 1 1
    # 1 9
    # 3 2
    # 4 3
    # 5 5
    # 6 5"""
    # sys.stdin = StringIO(input_text)
    
    # 创建FHQ-Treap实例
    treap = FHQTreapWithCount()
    
    try:
        # 一次性读取所有输入并拆分，提高读取效率
        input = sys.stdin.read().split()
        n = int(input[0])
        idx = 1
        
        # 处理每个操作
        for _ in range(n):
            try:
                if idx + 1 >= len(input):
                    raise ValueError("Insufficient input data")
                    
                op = int(input[idx])
                x = int(input[idx + 1])
                idx += 2
                
                # 根据操作类型执行相应操作
                if op == 1:
                    treap.add(x)  # 插入操作
                elif op == 2:
                    treap.remove(x)  # 删除操作
                elif op == 3:
                    print(treap.rank(x))  # 排名查询
                elif op == 4:
                    print(treap.index_by_rank(x))  # 第k小查询
                elif op == 5:
                    print(treap.pre_by_value(x))  # 前驱查询
                elif op == 6:
                    print(treap.post_by_value(x))  # 后继查询
                else:
                    # 处理非法操作
                    print(f"Error: Invalid operation {op}")
                    
            except ValueError as e:
                # 处理值错误（如无效的数字输入）
                print(f"Value error: {e}")
                # 跳过错误的输入，尝试继续处理后续操作
                idx = min(idx + 2, len(input))
            except Exception as e:
                # 处理其他异常
                print(f"Error processing operation: {e}")
                idx = min(idx + 2, len(input))
    except Exception as e:
        # 处理初始化或输入读取异常
        print(f"Critical error: {e}")
    
    # 调试用：验证树的正确性（可以取消注释查看中序遍历结果）
    # print("Inorder traversal:", treap.inorder(treap.head))


# 为了支持在提交时将类名改为"Main"，添加以下兼容代码
class Main(FHQTreapWithCount):
    """
    兼容类，用于提交到OJ平台
    当OJ要求类名为"Main"时，可以直接使用此类
    """
    pass

if __name__ == "__main__":
    """
    程序入口点
    
    【测试样例】
    输入：
    8
    1 10
    1 20
    1 30
    3 20
    4 2
    2 20
    3 20
    4 2
    
    输出：
    2
    20
    2
    30
    
    【注意事项】
    - 对于大数据量输入，需要确保输入输出效率
    - Python的递归深度限制为1000，对于大多数情况足够使用
    - 如果数据规模特别大，可以考虑非递归实现或调整递归深度限制
    
    【与其他平衡树对比】
    - 相比红黑树：实现更简单，不需要复杂的旋转操作
    - 相比AVL树：维护更容易，不需要严格的平衡因子检查
    - 相比Splay树：单次操作更稳定，不会出现最坏情况的O(n)复杂度
    """
    # 可以根据需要取消注释下面的语句以增加递归深度限制
    # sys.setrecursionlimit(1 << 25)
    main()