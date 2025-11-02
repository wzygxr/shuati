# LeetCode 297. Serialize and Deserialize Binary Tree
# 题目链接: https://leetcode.cn/problems/serialize-and-deserialize-binary-tree/
# 题目描述: 设计一个算法来序列化和反序列化二叉树。
# 序列化是将一个数据结构或者对象转换为连续的比特位的过程，进而可以将转换后的数据存储在一个文件或内存中，
# 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
#
# 解题思路:
# 1. 前序遍历序列化：使用特殊字符表示空节点
# 2. 递归反序列化：根据前序遍历顺序重建二叉树
# 3. 使用队列辅助反序列化：更直观的迭代方法
#
# 时间复杂度: 
#   - 序列化: O(n) - 每个节点访问一次
#   - 反序列化: O(n) - 每个节点处理一次
# 空间复杂度: O(n) - 需要存储序列化字符串或使用递归栈
# 是否为最优解: 是，这是序列化二叉树的标准方法

from typing import Optional, List, Deque, Any
from collections import deque

# 二叉树节点定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left: Optional[TreeNode] = None
        self.right: Optional[TreeNode] = None

class Codec:
    # 序列化分隔符
    SEPARATOR = ","
    NULL_NODE = "null"

    def serializePreorder(self, root: Optional[TreeNode]) -> str:
        """
        前序遍历序列化（递归）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            序列化后的字符串
        """
        if root is None:
            return ""
        
        result = []
        self._serialize_helper(root, result)
        return self.SEPARATOR.join(result)
    
    def _serialize_helper(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归序列化辅助函数
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            result.append(self.NULL_NODE)
            return
        
        # 前序遍历：根->左->右
        result.append(str(node.val))
        self._serialize_helper(node.left, result)
        self._serialize_helper(node.right, result)

    def serializeLevelOrder(self, root: Optional[TreeNode]) -> str:
        """
        层序遍历序列化（BFS）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            序列化后的字符串
        """
        if root is None:
            return ""
        
        result = []
        queue: Deque[Optional[TreeNode]] = deque([root])
        
        while queue:
            node = queue.popleft()
            
            if node is None:
                result.append(self.NULL_NODE)
                continue
            
            result.append(str(node.val))
            queue.append(node.left)
            queue.append(node.right)
        
        return self.SEPARATOR.join(result)

    def deserializePreorder(self, data: str) -> Optional[TreeNode]:
        """
        前序遍历反序列化
        
        Args:
            data: 序列化字符串
            
        Returns:
            反序列化后的二叉树根节点
        """
        if not data:
            return None
        
        nodes = deque(data.split(self.SEPARATOR))
        return self._deserialize_helper(nodes)
    
    def _deserialize_helper(self, nodes: Deque[str]) -> Optional[TreeNode]:
        """
        递归反序列化辅助函数
        
        Args:
            nodes: 节点值队列
            
        Returns:
            构建的子树根节点
        """
        if not nodes:
            return None
        
        val = nodes.popleft()
        if val == self.NULL_NODE:
            return None
        
        node = TreeNode(int(val))
        left_node = self._deserialize_helper(nodes)
        right_node = self._deserialize_helper(nodes)
        node.left = left_node
        node.right = right_node
        
        return node

    def deserializeLevelOrder(self, data: str) -> Optional[TreeNode]:
        """
        层序遍历反序列化
        
        Args:
            data: 序列化字符串
            
        Returns:
            反序列化后的二叉树根节点
        """
        if not data:
            return None
        
        nodes = data.split(self.SEPARATOR)
        if not nodes or nodes[0] == self.NULL_NODE:
            return None
        
        root = TreeNode(int(nodes[0]))
        queue: Deque[TreeNode] = deque([root])
        index = 1
        
        while queue and index < len(nodes):
            node = queue.popleft()
            
            # 处理左子节点
            if index < len(nodes) and nodes[index] != self.NULL_NODE:
                left_node = TreeNode(int(nodes[index]))
                node.left = left_node
                queue.append(left_node)
            index += 1
            
            # 处理右子节点
            if index < len(nodes) and nodes[index] != self.NULL_NODE:
                right_node = TreeNode(int(nodes[index]))
                node.right = right_node
                queue.append(right_node)
            index += 1
        
        return root

    def serialize(self, root: Optional[TreeNode]) -> str:
        """
        序列化二叉树（使用层序遍历版本，更直观）
        
        Args:
            root: 二叉树的根节点
            
        Returns:
            序列化后的字符串
        """
        return self.serializeLevelOrder(root)
    
    def deserialize(self, data: str) -> Optional[TreeNode]:
        """
        反序列化二叉树（使用层序遍历版本，更直观）
        
        Args:
            data: 序列化字符串
            
        Returns:
            反序列化后的二叉树根节点
        """
        return self.deserializeLevelOrder(data)

# 测试用例
def main():
    codec = Codec()

    # 测试用例1:
    #       1
    #      / \
    #     2   3
    #        / \
    #       4   5
    root1 = TreeNode(1)
    root1.left = TreeNode(2)
    root1.right = TreeNode(3)
    root1.right.left = TreeNode(4)
    root1.right.right = TreeNode(5)
    
    # 序列化
    serialized = codec.serialize(root1)
    print(f"序列化结果: {serialized}")
    
    # 反序列化
    deserialized = codec.deserialize(serialized)
    print("反序列化验证:")
    print_level_order(deserialized)  # 应该输出与原树相同的结构

    # 测试用例2: 空树
    root2 = None
    serialized2 = codec.serialize(root2)
    print(f"空树序列化: {serialized2}")
    deserialized2 = codec.deserialize(serialized2)
    print(f"空树反序列化验证: {deserialized2 is None}")

def print_level_order(root: Optional[TreeNode]) -> None:
    """
    层序遍历打印二叉树（用于验证）
    
    Args:
        root: 二叉树的根节点
    """
    if root is None:
        print("空树")
        return
    
    queue = deque([root])
    
    while queue:
        level_size = len(queue)
        level = []
        
        for _ in range(level_size):
            node = queue.popleft()
            level.append(str(node.val))
            
            if node.left is not None:
                queue.append(node.left)
            if node.right is not None:
                queue.append(node.right)
        
        print(" ".join(level))

if __name__ == "__main__":
    main()