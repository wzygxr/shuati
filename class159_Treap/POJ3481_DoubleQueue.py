# POJ 3481 Double Queue
# 维护一个双端队列，支持以下操作：
# 1. 插入元素
# 2. 查询并删除最大值
# 3. 查询并删除最小值
# 测试链接 : http://poj.org/problem?id=3481

import sys
import random

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(1000000)

MAXN = 100001

# 全局变量
head = 0  # 整棵树的头节点编号（根节点）
cnt = 0   # 空间使用计数，记录当前已分配的节点数量

# 节点信息数组
key = [0] * MAXN        # 节点的key值（客户ID）
priority = [0] * MAXN   # 节点的priority值（优先级）
left = [0] * MAXN       # 左孩子节点索引数组
right = [0] * MAXN      # 右孩子节点索引数组
size = [0] * MAXN       # 子树大小数组，记录以每个节点为根的子树中节点总数
randomPriority = [0.0] * MAXN  # 节点随机优先级数组，用于维护Treap的堆性质

# 更新节点信息
def up(i):
    """
    更新节点信息
    计算以节点i为根的子树大小
    :param i: 节点索引
    """
    global size, left, right
    # 子树大小 = 左子树大小 + 右子树大小 + 1（当前节点）
    size[i] = size[left[i]] + size[right[i]] + 1

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
def add_node(i, id_val, pri):
    """
    添加节点的递归实现
    :param i: 当前节点索引
    :param id_val: 客户ID
    :param pri: 优先级
    :return: 插入后的新节点索引
    """
    global cnt, key, priority, left, right, size, randomPriority
    # 如果当前节点为空，创建新节点
    if i == 0:
        cnt += 1
        key[cnt] = id_val
        priority[cnt] = pri
        size[cnt] = 1
        randomPriority[cnt] = random.random()
        return cnt
    # 按优先级插入，优先级小的在左子树，优先级大的在右子树
    if priority[i] < pri:
        right[i] = add_node(right[i], id_val, pri)
    elif priority[i] > pri:
        left[i] = add_node(left[i], id_val, pri)
    else:
        # 如果优先级相等，按客户ID插入
        if key[i] < id_val:
            right[i] = add_node(right[i], id_val, pri)
        else:
            left[i] = add_node(left[i], id_val, pri)
    # 更新当前节点的子树大小信息
    up(i)
    # 检查是否需要旋转以维护堆性质
    # 如果左子节点优先级大于当前节点，执行右旋
    if left[i] != 0 and randomPriority[left[i]] > randomPriority[i]:
        return right_rotate(i)
    # 如果右子节点优先级大于当前节点，执行左旋
    if right[i] != 0 and randomPriority[right[i]] > randomPriority[i]:
        return left_rotate(i)
    # 不需要旋转，返回当前节点
    return i

# 添加元素的公共接口
def add(id_val, pri):
    """
    添加元素的公共接口
    :param id_val: 客户ID
    :param pri: 优先级
    """
    global head
    head = add_node(head, id_val, pri)

# 删除指定优先级的节点
def remove_node(i, pri):
    """
    删除指定优先级的节点
    :param i: 当前节点索引
    :param pri: 要删除节点的优先级
    :return: 删除后的新节点索引
    """
    global left, right, size
    # 如果当前节点为空，返回0
    if i == 0:
        return 0
    # 根据优先级查找要删除的节点
    if priority[i] < pri:
        right[i] = remove_node(right[i], pri)
    elif priority[i] > pri:
        left[i] = remove_node(left[i], pri)
    else:
        # 找到要删除的节点
        # 如果是叶子节点，直接删除
        if left[i] == 0 and right[i] == 0:
            return 0
        # 如果只有右子树，用右子树替代当前节点
        elif left[i] == 0:
            return right[i]
        # 如果只有左子树，用左子树替代当前节点
        elif right[i] == 0:
            return left[i]
        # 如果左右子树都存在，根据随机优先级决定旋转方向
        else:
            # 如果左子节点随机优先级更高，执行右旋
            if randomPriority[left[i]] > randomPriority[right[i]]:
                i = right_rotate(i)
                right[i] = remove_node(right[i], pri)
            # 如果右子节点随机优先级更高，执行左旋
            else:
                i = left_rotate(i)
                left[i] = remove_node(left[i], pri)
    # 更新节点信息
    up(i)
    return i

# 查找并返回最小值节点
def find_min_node(i):
    """
    查找并返回最小值节点
    :param i: 当前节点索引
    :return: 最小值节点的索引
    """
    # 如果左子树为空，说明当前节点就是最小值节点
    if left[i] == 0:
        return i
    # 否则递归查找左子树中的最小值节点
    return find_min_node(left[i])

# 删除最小值并返回其ID
def remove_min():
    """
    删除最小值并返回其ID
    :return: 最小值节点的ID，如果树为空返回-1
    """
    global head
    # 如果树为空，返回-1
    if head == 0:
        return -1
    # 找到最小值节点
    min_node = find_min_node(head)
    # 获取最小值节点的ID
    result = key[min_node]
    # 删除该节点
    head = remove_node(head, priority[min_node])
    return result

# 查找并返回最大值节点
def find_max_node(i):
    """
    查找并返回最大值节点
    :param i: 当前节点索引
    :return: 最大值节点的索引
    """
    # 如果右子树为空，说明当前节点就是最大值节点
    if right[i] == 0:
        return i
    # 否则递归查找右子树中的最大值节点
    return find_max_node(right[i])

# 删除最大值并返回其ID
def remove_max():
    """
    删除最大值并返回其ID
    :return: 最大值节点的ID，如果树为空返回-1
    """
    global head
    # 如果树为空，返回-1
    if head == 0:
        return -1
    # 找到最大值节点
    max_node = find_max_node(head)
    # 获取最大值节点的ID
    result = key[max_node]
    # 删除该节点
    head = remove_node(head, priority[max_node])
    return result

# 清空数据结构
def clear():
    """
    清空数据结构，重置所有数组
    """
    global head, cnt, key, priority, left, right, size, randomPriority
    head = 0
    cnt = 0
    # 重置数组
    key = [0] * MAXN
    priority = [0] * MAXN
    left = [0] * MAXN
    right = [0] * MAXN
    size = [0] * MAXN
    randomPriority = [0.0] * MAXN

# 主函数
def main():
    """
    主函数
    """
    global head
    while True:
        try:
            # 读取命令
            line = input().strip()
            if not line:
                continue
            parts = list(map(int, line.split()))
            command = parts[0]
            
            # 命令0：程序结束
            if command == 0:
                break
            # 命令1：插入元素
            elif command == 1:
                id_val = parts[1]      # 客户ID
                priority_val = parts[2]  # 优先级
                add(id_val, priority_val)
            # 命令2：查询并删除最大值
            elif command == 2:
                max_val = remove_max()
                if max_val != -1:
                    print(max_val)
                else:
                    print(0)
            # 命令3：查询并删除最小值
            elif command == 3:
                min_val = remove_min()
                if min_val != -1:
                    print(min_val)
                else:
                    print(0)
        except EOFError:
            break

if __name__ == "__main__":
    main()