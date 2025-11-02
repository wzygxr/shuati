from collections import deque
from typing import Optional

# LeetCode 297. 二叉树的序列化与反序列化
# 题目链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
# 题目大意: 设计一个算法来序列化和反序列化二叉树。
# 序列化是将一个数据结构或者对象转换为连续的比特位，进而可以将转换后的数据存储在一个文件或内存缓冲区中，
# 并且在需要的时候可以恢复成原来的数据结构或对象。

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left: Optional['TreeNode'] = None
        self.right: Optional['TreeNode'] = None

class Codec:
    def serialize(self, root: Optional[TreeNode]) -> str:
        """
        序列化二叉树
        方法: 使用层序遍历进行序列化
        思路: 使用BFS层序遍历，将每个节点的值转换为字符串，空节点用"null"表示
        时间复杂度: O(n) - n是树中节点的数量
        空间复杂度: O(n) - 存储序列化字符串和遍历所需的队列空间
        """
        if not root:
            return "[]"
        
        result = []
        queue = deque([root])
        
        while queue:
            node = queue.popleft()
            if node:
                result.append(str(node.val))
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            else:
                result.append("null")
        
        # 移除末尾的null值
        while result and result[-1] == "null":
            result.pop()
        
        return "[" + ",".join(result) + "]"
    
    def deserialize(self, data: str) -> Optional[TreeNode]:
        """
        反序列化二叉树
        方法: 使用层序遍历进行反序列化
        思路: 将序列化的字符串解析为节点值列表，然后使用BFS方式重建树
        时间复杂度: O(n) - n是树中节点的数量
        空间复杂度: O(n) - 存储节点值列表和重建树所需的队列空间
        """
        if not data or data == "[]":
            return None
        
        # 解析字符串为节点值列表
        values = data[1:-1].split(",")
        if not values or values[0] == "null":
            return None
        
        root = TreeNode(int(values[0]))
        queue = deque([root])
        i = 1
        
        while queue and i < len(values):
            node = queue.popleft()
            
            # 处理左子节点
            if values[i] != "null":
                node.left = TreeNode(int(values[i]))
                queue.append(node.left)
            i += 1
            
            # 处理右子节点
            if i < len(values) and values[i] != "null":
                node.right = TreeNode(int(values[i]))
                queue.append(node.right)
            i += 1
        
        return root

# 测试代码
if __name__ == "__main__":
    # 构建测试二叉树:
    #       1
    #      / \
    #     2   3
    #        / \
    #       4   5
    root = TreeNode(1)
    root.left = TreeNode(2)
    root.right = TreeNode(3)
    root.right.left = TreeNode(4)
    root.right.right = TreeNode(5)
    
    codec = Codec()
    
    # 序列化
    serialized = codec.serialize(root)
    print("序列化结果:", serialized)
    
    # 反序列化
    deserialized = codec.deserialize(serialized)
    print("反序列化结果:", codec.serialize(deserialized))