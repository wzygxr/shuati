# Treap（树堆）模板代码
# Treap是一种自平衡二叉搜索树，结合了二叉搜索树和堆的性质
# 每个节点有一个key（用于二叉搜索树的性质）和一个priority（用于堆的性质）
# 操作时间复杂度：O(log n)

import sys
import random

# 增加递归深度限制，防止处理大数据时出现栈溢出
sys.setrecursionlimit(1000000)

# 全局变量定义
MAXN = 100001  # 最大节点数
head = 0  # 整棵树的头节点编号（根节点）
cnt = 0   # 空间使用计数，记录当前已分配的节点数量

# 节点信息数组
key = [0] * (MAXN + 1)      # 节点的key值（存储实际数值）
priority = [0.0] * (MAXN + 1)  # 节点优先级，用于维护Treap的堆性质
left = [0] * (MAXN + 1)     # 左孩子节点索引数组
right = [0] * (MAXN + 1)    # 右孩子节点索引数组
size = [0] * (MAXN + 1)     # 子树大小数组，记录以每个节点为根的子树中节点总数

# 更新节点信息
def up(i):
    """
    更新节点信息
    计算以节点i为根的子树大小
    :param i: 节点索引
    """
    global size, left, right
    # 子树大小 = 左子树大小 + 右子树大小 + 当前节点（词频为1）
    size[i] = size[left[i]] + size[right[i]] + 1

# 左旋转
def left_rotate(i):
    """
    左旋操作
    当右子节点的优先级大于当前节点时执行
    :param i: 当前节点
    :return: 旋转后的新根节点
    """
    global right, left, size
    # 获取右子节点作为新的根节点
    r = right[i]
    # 将右子节点的左子树作为当前节点的右子树
    right[i] = left[r]
    # 将当前节点作为原右子节点的左子树
    left[r] = i
    # 更新节点信息
    up(i)
    up(r)
    # 返回新的根节点
    return r

# 右旋转
def right_rotate(i):
    """
    右旋操作
    当左子节点的优先级大于当前节点时执行
    :param i: 当前节点
    :return: 旋转后的新根节点
    """
    global right, left, size
    # 获取左子节点作为新的根节点
    l = left[i]
    # 将左子节点的右子树作为当前节点的左子树
    left[i] = right[l]
    # 将当前节点作为原左子节点的右子树
    right[l] = i
    # 更新节点信息
    up(i)
    up(l)
    # 返回新的根节点
    return l

# 添加节点
def add_node(i, num):
    """
    添加节点的递归实现
    :param i: 当前节点索引
    :param num: 要插入的数值
    :return: 插入后的新节点索引
    """
    global cnt, key, priority, left, right, size
    # 如果当前节点为空，创建新节点
    if i == 0:
        cnt += 1
        key[cnt] = num
        # 生成随机优先级
        priority[cnt] = random.random()
        # 初始化子树大小
        size[cnt] = 1
        return cnt
    # 如果要插入的值等于当前节点值，这里简化处理（实际应该增加词频）
    if key[i] == num:
        # 如果允许重复，可以在这里增加计数
        pass
    # 如果要插入的值小于当前节点值，递归插入到左子树
    elif key[i] > num:
        left[i] = add_node(left[i], num)
    # 如果要插入的值大于当前节点值，递归插入到右子树
    else:
        right[i] = add_node(right[i], num)
    # 更新当前节点的子树大小信息
    up(i)
    # 检查是否需要旋转以维护堆性质
    # 如果左子节点优先级大于当前节点，执行右旋
    if left[i] != 0 and priority[left[i]] > priority[i]:
        return right_rotate(i)
    # 如果右子节点优先级大于当前节点，执行左旋
    if right[i] != 0 and priority[right[i]] > priority[i]:
        return left_rotate(i)
    # 不需要旋转，返回当前节点
    return i

# 添加元素
def add(num):
    """
    添加元素的公共接口
    :param num: 要添加的数值
    """
    global head
    head = add_node(head, num)

# 删除节点
def remove_node(i, num):
    """
    删除节点的递归实现
    :param i: 当前节点索引
    :param num: 要删除的数值
    :return: 删除后的新节点索引
    """
    global left, right, key
    # 如果当前节点为空，返回0
    if i == 0:
        return 0
    # 如果要删除的值小于当前节点值，递归删除左子树
    if key[i] < num:
        right[i] = remove_node(right[i], num)
    # 如果要删除的值大于当前节点值，递归删除右子树
    elif key[i] > num:
        left[i] = remove_node(left[i], num)
    # 如果要删除的值等于当前节点值
    else:
        # 找到要删除的节点
        # 如果是叶子节点直接删除
        if left[i] == 0 and right[i] == 0:
            return 0
        # 如果只有左子树
        elif left[i] != 0 and right[i] == 0:
            i = left[i]
        # 如果只有右子树
        elif left[i] == 0 and right[i] != 0:
            i = right[i]
        # 如果左右子树都存在，根据优先级选择旋转方向
        else:
            # 如果左子节点优先级更高，执行右旋
            if priority[left[i]] > priority[right[i]]:
                i = right_rotate(i)
                right[i] = remove_node(right[i], num)
            # 如果右子节点优先级更高，执行左旋
            else:
                i = left_rotate(i)
                left[i] = remove_node(left[i], num)
    # 更新节点信息
    up(i)
    return i

# 删除元素
def remove(num):
    """
    删除元素的公共接口
    :param num: 要删除的数值
    """
    global head
    head = remove_node(head, num)

# 查询排名（有多少个元素比num小 + 1）
def rank(num):
    """
    查询x的排名
    :param num: 目标数值
    :return: num的排名（比num小的数的个数+1）
    """
    return small(head, num) + 1

# 计算小于num的元素个数
def small(i, num):
    """
    计算小于num的元素个数
    :param i: 当前节点索引
    :param num: 目标数值
    :return: 小于num的元素个数
    """
    if i == 0:
        return 0
    # 如果当前节点值大于等于目标值，递归查询左子树
    if key[i] >= num:
        return small(left[i], num)
    # 如果当前节点值小于目标值，结果包括：
    # 1. 左子树的所有节点
    # 2. 当前节点
    # 3. 右子树中小于num的节点数
    else:
        return size[left[i]] + 1 + small(right[i], num)

# 查询第k小值
def index_k(i, x):
    """
    查询排名为x的数
    :param i: 当前节点索引
    :param x: 排名
    :return: 排名为x的数值
    """
    # 如果左子树大小大于等于x，说明目标在左子树中
    if size[left[i]] >= x:
        return index_k(left[i], x)
    # 如果左子树大小加上当前节点小于x，说明目标在右子树中
    elif size[left[i]] + 1 < x:
        return index_k(right[i], x - size[left[i]] - 1)
    # 否则当前节点就是目标节点
    return key[i]

# 查询第k小值
def index(x):
    """
    查询排名为x的数的公共接口
    :param x: 排名
    :return: 排名为x的数值
    """
    global head
    # 检查排名是否合法
    if x <= 0 or x > size[head]:
        return float('-inf')  # 非法输入
    return index_k(head, x)

# 查找前驱（比num小的最大元素）
def pre(i, num):
    """
    查询x的前驱
    :param i: 当前节点索引
    :param num: 目标数值
    :return: x的前驱（小于x的最大数）
    """
    if i == 0:
        return float('-inf')
    # 如果当前节点值大于等于目标值，递归查询左子树
    if key[i] >= num:
        return pre(left[i], num)
    # 如果当前节点值小于目标值，前驱可能是当前节点值或右子树中的最大值
    else:
        return max(key[i], pre(right[i], num))

# 查找前驱
def pre_func(num):
    """
    查询x的前驱的公共接口
    :param num: 目标数值
    :return: x的前驱
    """
    return pre(head, num)

# 查找后继（比num大的最小元素）
def post(i, num):
    """
    查询x的后继
    :param i: 当前节点索引
    :param num: 目标数值
    :return: x的后继（大于x的最小数）
    """
    if i == 0:
        return float('inf')
    # 如果当前节点值小于等于目标值，递归查询右子树
    if key[i] <= num:
        return post(right[i], num)
    # 如果当前节点值大于目标值，后继可能是当前节点值或左子树中的最小值
    else:
        return min(key[i], post(left[i], num))

# 查找后继
def post_func(num):
    """
    查询x的后继的公共接口
    :param num: 目标数值
    :return: x的后继
    """
    return post(head, num)

# 中序遍历
def inorder(i):
    """
    中序遍历验证二叉搜索树性质
    :param i: 当前节点索引
    """
    if i == 0:
        return
    inorder(left[i])
    print(key[i], end=' ')
    inorder(right[i])

# 验证二叉搜索树性质
def checkBST(i, min_val, max_val):
    """
    验证二叉搜索树性质
    :param i: 当前节点索引
    :param min_val: 最小值限制
    :param max_val: 最大值限制
    :return: 是否满足BST性质
    """
    if i == 0:
        return True
    # 检查当前节点值是否在合法范围内
    if key[i] <= min_val or key[i] >= max_val:
        return False
    # 递归检查左右子树
    return checkBST(left[i], min_val, key[i]) and checkBST(right[i], key[i], max_val)

# 验证堆性质
def checkHeap(i):
    """
    验证堆性质
    :param i: 当前节点索引
    :return: 是否满足堆性质
    """
    if i == 0:
        return True
    # 检查左子节点优先级是否小于等于当前节点
    if left[i] != 0 and priority[left[i]] > priority[i]:
        return False
    # 检查右子节点优先级是否小于等于当前节点
    if right[i] != 0 and priority[right[i]] > priority[i]:
        return False
    # 递归检查左右子树
    return checkHeap(left[i]) and checkHeap(right[i])

# 清空数据结构
def clear():
    """
    清空数据结构，重置所有数组
    """
    global head, cnt, key, priority, left, right, size
    head = 0
    cnt = 0
    # 重置数组
    key = [0] * (MAXN + 1)
    priority = [0.0] * (MAXN + 1)
    left = [0] * (MAXN + 1)
    right = [0] * (MAXN + 1)
    size = [0] * (MAXN + 1)

# 主函数
def main():
    """
    测试代码
    """
    # 添加测试数据
    add(5)
    add(3)
    add(7)
    add(2)
    add(4)
    add(6)
    add(8)
    
    print("中序遍历结果:")
    inorder(head)
    print()
    
    print(f"查询排名（元素5）: {rank(5)}")
    print(f"查询第3小值: {index(3)}")
    print(f"查询前驱（元素5）: {pre_func(5)}")
    print(f"查询后继（元素5）: {post_func(5)}")
    
    print("\n删除元素5后:")
    remove(5)
    inorder(head)
    print()
    
    print(f"验证二叉搜索树性质: {'通过' if checkBST(head, float('-inf'), float('inf')) else '未通过'}")
    print(f"验证堆性质: {'通过' if checkHeap(head) else '未通过'}")

if __name__ == "__main__":
    main()