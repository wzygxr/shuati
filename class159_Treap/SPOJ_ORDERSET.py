# SPOJ ORDERSET - Order statistic set
# 维护一个可重集合，支持以下操作：
# 1. 插入元素
# 2. 删除元素
# 3. 查询元素排名
# 4. 查询第k小值
# 测试链接 : https://www.spoj.com/problems/ORDERSET/

import sys
import random

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(1000000)

MAXN = 200001

# 全局变量
head = 0  # 整棵树的头节点编号（根节点）
cnt = 0   # 空间使用计数，记录当前已分配的节点数量

# 节点信息数组
key = [0] * MAXN      # 节点的key值（存储实际数值）
count = [0] * MAXN    # 节点key的计数（词频压缩，相同值只存储一次但记录出现次数）
left = [0] * MAXN     # 左孩子节点索引数组
right = [0] * MAXN    # 右孩子节点索引数组
size = [0] * MAXN     # 子树大小数组，记录以每个节点为根的子树中节点总数
priority = [0.0] * MAXN  # 节点优先级数组，用于维护Treap的堆性质

# 更新节点信息
def up(i):
    """
    更新节点信息
    计算以节点i为根的子树大小
    :param i: 节点索引
    """
    global size, left, right, count
    # 子树大小 = 左子树大小 + 右子树大小 + 当前节点的词频
    size[i] = size[left[i]] + size[right[i]] + count[i]

# 左旋转
def left_rotate(i):
    """
    左旋转
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
    右旋转
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
    global cnt, key, count, left, right, size, priority
    # 如果当前节点为空，创建新节点
    if i == 0:
        cnt += 1
        key[cnt] = num
        count[cnt] = size[cnt] = 1
        priority[cnt] = random.random()
        return cnt
    # 如果要插入的值等于当前节点值，增加词频
    if key[i] == num:
        count[i] += 1
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

# 添加元素的公共接口
def add(num):
    """
    添加元素的公共接口
    :param num: 要添加的数值
    """
    global head
    head = add_node(head, num)

# 计算小于num的元素个数
def small(i, num):
    """
    计算小于num的元素个数
    :param i: 当前节点索引
    :param num: 目标数值
    :return: 小于num的元素个数
    """
    global key, left, right, size, count
    # 如果当前节点为空，返回0
    if i == 0:
        return 0
    # 如果当前节点值大于等于目标值，递归查询左子树
    if key[i] >= num:
        return small(left[i], num)
    # 如果当前节点值小于目标值，结果包括：
    # 1. 左子树的所有节点
    # 2. 当前节点的词频
    # 3. 右子树中小于num的节点数
    else:
        return size[left[i]] + count[i] + small(right[i], num)

# 查询排名
def rank(num):
    """
    查询排名
    :param num: 目标数值
    :return: num的排名（比num小的数的个数+1）
    """
    return small(head, num) + 1

# 查询排名为x的数
def index_k(i, x):
    """
    查询排名为x的数
    :param i: 当前节点索引
    :param x: 排名
    :return: 排名为x的数值
    """
    global key, left, right, size, count
    # 如果左子树大小大于等于x，说明目标在左子树中
    if size[left[i]] >= x:
        return index_k(left[i], x)
    # 如果左子树大小加上当前节点词频小于x，说明目标在右子树中
    elif size[left[i]] + count[i] < x:
        return index_k(right[i], x - size[left[i]] - count[i])
    # 否则当前节点就是目标节点
    return key[i]

# 查询排名为x的数的公共接口
def index(x):
    """
    查询排名为x的数的公共接口
    :param x: 排名
    :return: 排名为x的数值
    """
    global head
    # 检查排名是否合法
    if x <= 0 or x > size[head]:
        return float('-inf')
    return index_k(head, x)

# 查找前驱
def pre(i, num):
    """
    查找前驱
    :param i: 当前节点索引
    :param num: 目标数值
    :return: x的前驱（小于x的最大数）
    """
    global key, left, right
    # 如果当前节点为空，返回负无穷
    if i == 0:
        return float('-inf')
    # 如果当前节点值大于等于目标值，递归查询左子树
    if key[i] >= num:
        return pre(left[i], num)
    # 如果当前节点值小于目标值，前驱可能是当前节点值或右子树中的最大值
    else:
        return max(key[i], pre(right[i], num))

# 查找前驱的公共接口
def pre_func(num):
    """
    查找前驱的公共接口
    :param num: 目标数值
    :return: x的前驱
    """
    return pre(head, num)

# 查找后继
def post(i, num):
    """
    查找后继
    :param i: 当前节点索引
    :param num: 目标数值
    :return: x的后继（大于x的最小数）
    """
    global key, left, right
    # 如果当前节点为空，返回正无穷
    if i == 0:
        return float('inf')
    # 如果当前节点值小于等于目标值，递归查询右子树
    if key[i] <= num:
        return post(right[i], num)
    # 如果当前节点值大于目标值，后继可能是当前节点值或左子树中的最小值
    else:
        return min(key[i], post(left[i], num))

# 查找后继的公共接口
def post_func(num):
    """
    查找后继的公共接口
    :param num: 目标数值
    :return: x的后继
    """
    return post(head, num)

# 删除节点
def remove_node(i, num):
    """
    删除节点的递归实现
    :param i: 当前节点索引
    :param num: 要删除的数值
    :return: 删除后的新节点索引
    """
    global key, left, right, count
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
        # 如果词频大于1，只需减少词频
        if count[i] > 1:
            count[i] -= 1
        # 如果词频为1，需要真正删除节点
        else:
            global head
            # 如果是叶子节点，直接删除
            if left[i] == 0 and right[i] == 0:
                return 0
            # 如果只有左子树，用左子树替代当前节点
            elif left[i] != 0 and right[i] == 0:
                i = left[i]
            # 如果只有右子树，用右子树替代当前节点
            elif left[i] == 0 and right[i] != 0:
                i = right[i]
            # 如果左右子树都存在，根据优先级决定旋转方向
            else:
                # 如果左子节点优先级更高，执行右旋
                if priority[left[i]] >= priority[right[i]]:
                    i = right_rotate(i)
                    right[i] = remove_node(right[i], num)
                # 如果右子节点优先级更高，执行左旋
                else:
                    i = left_rotate(i)
                    left[i] = remove_node(left[i], num)
    # 更新节点信息
    up(i)
    return i

# 删除元素的公共接口
def remove(num):
    """
    删除元素的公共接口
    :param num: 要删除的数值
    """
    global head
    # 只有当num存在于树中时才执行删除操作
    if rank(num) != rank(num + 1):
        head = remove_node(head, num)

# 清空数据结构
def clear():
    """
    清空数据结构，重置所有数组
    """
    global head, cnt, key, count, left, right, size, priority
    head = 0
    cnt = 0
    # 重置数组
    key = [0] * MAXN
    count = [0] * MAXN
    left = [0] * MAXN
    right = [0] * MAXN
    size = [0] * MAXN
    priority = [0.0] * MAXN

# 主函数
def main():
    """
    主函数
    """
    n = int(input())
    for _ in range(n):
        # 读取操作指令
        operation = input().strip()
        op, x_str = operation.split()
        x = int(x_str)
        
        if op == 'I':
            # 插入元素
            add(x)
        elif op == 'D':
            # 删除元素
            remove(x)
        elif op == 'K':
            # 查询第k小值
            result = index(x)
            if result == float('-inf'):
                print("invalid")
            else:
                print(int(result))
        elif op == 'C':
            # 查询排名（计算小于x的元素个数）
            print(small(head, x))

if __name__ == "__main__":
    main()