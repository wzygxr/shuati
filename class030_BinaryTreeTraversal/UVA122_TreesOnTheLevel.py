from collections import deque
from typing import List, Optional

# UVA 122. Trees on the Level
# 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=58
# 题目大意: 按照层序遍历的方式构建二叉树并输出节点值。输入格式为(value, path)，其中path是由L和R组成的字符串，
# 表示从根节点到该节点的路径，L表示左子节点，R表示右子节点。

# 二叉树节点定义
class TreeNode:
    def __init__(self, val: int = 0):
        self.val = val
        self.left: Optional['TreeNode'] = None
        self.right: Optional['TreeNode'] = None

# 节点信息类，用于存储节点值和路径
class TreeNodeInfo:
    def __init__(self, val: int, path: str):
        self.val = val
        self.path = path

def level_order_traversal(nodes: List[TreeNodeInfo]) -> List[int]:
    """
    构建二叉树并进行层序遍历
    思路:
    1. 解析输入的节点信息，按照路径构建二叉树
    2. 对构建的二叉树进行层序遍历
    3. 如果构建过程中发现节点重复或缺失，返回空列表
    时间复杂度: O(n) - n是节点数量
    空间复杂度: O(n) - 存储节点和队列
    """
    # 创建根节点
    root = TreeNode(0)  # 临时根节点
    
    # 根据路径信息构建树
    for node_info in nodes:
        if not insert_node(root, node_info.val, node_info.path):
            # 如果插入失败，返回空列表
            return []
    
    # 进行层序遍历
    return bfs(root)

def insert_node(root: TreeNode, val: int, path: str) -> bool:
    """
    根据路径插入节点
    :param root: 根节点
    :param val: 节点值
    :param path: 路径字符串
    :return: 是否插入成功
    """
    current = root
    
    # 根据路径找到要插入的位置
    for direction in path:
        if direction == 'L':
            if current.left is None:
                current.left = TreeNode(0)  # 临时节点
            current = current.left
        elif direction == 'R':
            if current.right is None:
                current.right = TreeNode(0)  # 临时节点
            current = current.right
        else:
            # 无效路径字符
            return False
    
    # 检查节点是否已经被赋值
    if current.val != 0:
        # 节点已经被赋值，说明重复
        return False
    
    # 赋值
    current.val = val
    return True

def bfs(root: TreeNode) -> List[int]:
    """
    层序遍历
    :param root: 根节点
    :return: 遍历结果
    """
    result = []
    queue = deque([root])
    
    while queue:
        current = queue.popleft()
        
        # 如果节点值为0，说明是临时节点，树不完整
        if current.val == 0:
            return []  # 返回空列表表示树不完整
        
        result.append(current.val)
        
        if current.left is not None:
            queue.append(current.left)
        if current.right is not None:
            queue.append(current.right)
    
    return result

# 测试方法
if __name__ == "__main__":
    # 示例测试
    nodes = [
        TreeNodeInfo(5, ""),
        TreeNodeInfo(3, "L"),
        TreeNodeInfo(4, "LL"),
        TreeNodeInfo(7, "LR")
    ]
    
    result = level_order_traversal(nodes)
    if not result:
        print("not complete")
    else:
        print(" ".join(map(str, result)))