import time
from typing import List, Dict, Optional

"""
LeetCode 652. 寻找重复的子树 (Find Duplicate Subtrees)

题目描述:
给定一棵二叉树，返回所有重复的子树。
对于同一类的重复子树，你只需要返回其中任意一棵的根结点即可。
两棵树重复是指它们具有相同的结构以及相同的结点值。

示例1:
         1
        / \
       2   3
      /   / \
     4   2   4
        /
       4

输出:
[[2,4],[4]]

解释:
上面的二叉树有两个重复子树。
第一个重复子树是4，如蓝色节点所示。
第二个重复子树是2 -> 4，如橙色节点所示。

题目链接: https://leetcode.com/problems/find-duplicate-subtrees/

解题思路:
这道题需要我们找出二叉树中所有重复的子树。解决这个问题的关键在于能够唯一地表示每个子树，并能够快速判断是否已经存在相同的子树。

解法: 递归 + 哈希表
1. 对于每个子树，我们需要生成一个唯一标识符，可以通过序列化的方式实现
2. 使用哈希表来记录每个子树标识符出现的次数
3. 当一个子树标识符出现次数为2时，将该子树的根节点加入结果列表

时间复杂度: O(n^2)，其中 n 是树中的节点数。在最坏情况下，序列化每个节点需要O(n)时间，共有n个节点。
空间复杂度: O(n^2)，存储所有子树的序列化表示。

优化版本：使用ID来代替完整序列化字符串，可以将时间和空间复杂度优化到O(n)。
"""

# 定义二叉树节点
class TreeNode:
    def __init__(self, val=0, left=None, right=None):
        self.val = val
        self.left = left
        self.right = right

class Solution:
    def findDuplicateSubtrees(self, root: Optional[TreeNode]) -> List[Optional[TreeNode]]:
        """
        解法一: 使用序列化 + 哈希表
        将每个子树序列化为字符串，然后使用哈希表记录每个子树出现的次数。
        
        Args:
            root: 二叉树的根节点
        
        Returns:
            包含重复子树根节点的列表
        """
        result = []
        if not root:
            return result
        
        # 哈希表用于记录每个子树序列化字符串出现的次数
        subtree_count = {}
        
        # 递归函数，用于序列化子树并检查重复
        def serialize_and_check_duplicates(node):
            if not node:
                return "#"  # 用#表示空节点
            
            # 序列化当前节点的左子树、当前节点的值、右子树
            key = f"{node.val},{serialize_and_check_duplicates(node.left)},{serialize_and_check_duplicates(node.right)}"
            
            # 获取当前子树出现的次数，如果是第二次出现，则添加到结果中
            subtree_count[key] = subtree_count.get(key, 0) + 1
            if subtree_count[key] == 2:
                result.append(node)  # 只有当子树出现次数为2时添加，避免重复添加
            
            return key
        
        serialize_and_check_duplicates(root)
        return result
    
    def findDuplicateSubtreesOptimized(self, root: Optional[TreeNode]) -> List[Optional[TreeNode]]:
        """
        解法二: 使用ID代替完整序列化字符串（优化版本）
        为每个不同的子树分配一个唯一ID，使用ID来标识子树而不是完整的序列化字符串。
        
        Args:
            root: 二叉树的根节点
        
        Returns:
            包含重复子树根节点的列表
        """
        result = []
        if not root:
            return result
        
        # 哈希表用于将子树的序列化字符串映射到唯一ID
        subtree_id = {}
        # 哈希表用于记录每个ID（子树）出现的次数
        id_count = {}
        # 当前可用的下一个ID
        next_id = [1]  # 使用列表作为可变对象
        
        # 递归函数，使用ID检查重复子树
        def find_duplicates_with_id(node):
            if not node:
                return 0  # 空节点的ID为0
            
            # 构建当前子树的键，使用子树ID而不是完整的序列化字符串
            left_id = find_duplicates_with_id(node.left)
            right_id = find_duplicates_with_id(node.right)
            key = f"{node.val},{left_id},{right_id}"
            
            # 如果当前子树还没有分配ID，则分配一个新ID
            if key not in subtree_id:
                subtree_id[key] = next_id[0]
                next_id[0] += 1
            
            id = subtree_id[key]
            
            # 增加当前ID的计数，并在计数为2时添加到结果中
            id_count[id] = id_count.get(id, 0) + 1
            if id_count[id] == 2:
                result.append(node)
            
            return id
        
        find_duplicates_with_id(root)
        return result

"""
将树转换为字符串表示（用于打印结果）
"""
def tree_to_string(root: Optional[TreeNode]) -> str:
    if not root:
        return "null"
    
    result = str(root.val)
    if root.left or root.right:
        result += f"[{tree_to_string(root.left)},{tree_to_string(root.right)}]"
    
    return result

"""
打印结果列表
"""
def print_result(result: List[Optional[TreeNode]]) -> None:
    strings = [tree_to_string(node) for node in result]
    print(f"[{', '.join(strings)}]")

"""
构建测试用例中的树
"""
def build_example_tree() -> Optional[TreeNode]:
    # 构建示例中的树
    #       1
    #      / \
    #     2   3
    #    /   / \
    #   4   2   4
    #      /
    #     4
    node1 = TreeNode(1)
    node2 = TreeNode(2)
    node3 = TreeNode(3)
    node4 = TreeNode(4)
    node5 = TreeNode(2)
    node6 = TreeNode(4)
    node7 = TreeNode(4)
    
    node1.left = node2
    node1.right = node3
    node2.left = node4
    node3.left = node5
    node3.right = node6
    node5.left = node7
    
    return node1

"""
构建一个带有重复子树的平衡二叉树

Args:
    start: 起始值
    end: 结束值

Returns:
    构建的树的根节点
"""
def build_balanced_tree_with_duplicates(start: int, end: int) -> Optional[TreeNode]:
    if start > end:
        return None
    
    mid = start + (end - start) // 2
    root = TreeNode(mid)
    
    # 为了创建重复子树，我们可以使部分子树的值重复
    if start <= end - 2:
        root.left = build_balanced_tree_with_duplicates(start, mid - 1)
        root.right = build_balanced_tree_with_duplicates(start, mid - 1)  # 重复左子树的结构
    else:
        root.left = build_balanced_tree_with_duplicates(start, mid - 1)
        root.right = build_balanced_tree_with_duplicates(mid + 1, end)
    
    return root

# 测试代码
def main():
    solution = Solution()
    
    # 测试用例1: 示例中的树
    root1 = build_example_tree()
    print("测试用例1:")
    print("解法一（序列化）结果: ")
    print_result(solution.findDuplicateSubtrees(root1))  # 预期输出类似: [2[4,null],4]
    
    # 重新构建树，因为解法一可能修改了树的状态（虽然这里不会，但为了保险起见）
    root1_again = build_example_tree()
    print("解法二（ID优化）结果: ")
    print_result(solution.findDuplicateSubtreesOptimized(root1_again))  # 预期输出类似: [2[4,null],4]
    print()
    
    # 测试用例2: 空树
    root2 = None
    print("测试用例2（空树）:")
    print("解法一（序列化）结果: ")
    print_result(solution.findDuplicateSubtrees(root2))  # 预期输出: []
    print("解法二（ID优化）结果: ")
    print_result(solution.findDuplicateSubtreesOptimized(root2))  # 预期输出: []
    print()
    
    # 测试用例3: 只有一个节点的树
    root3 = TreeNode(1)
    print("测试用例3（单节点树）:")
    print("解法一（序列化）结果: ")
    print_result(solution.findDuplicateSubtrees(root3))  # 预期输出: []
    print("解法二（ID优化）结果: ")
    print_result(solution.findDuplicateSubtreesOptimized(root3))  # 预期输出: []
    print()
    
    # 测试用例4: 所有节点都相同的树
    root4 = TreeNode(0)
    root4.left = TreeNode(0)
    root4.right = TreeNode(0)
    root4.left.left = TreeNode(0)
    root4.right.right = TreeNode(0)
    print("测试用例4（所有节点都相同）:")
    print("解法一（序列化）结果: ")
    print_result(solution.findDuplicateSubtrees(root4))  # 预期输出类似: [0,0]
    
    # 重新构建树
    root4_again = TreeNode(0)
    root4_again.left = TreeNode(0)
    root4_again.right = TreeNode(0)
    root4_again.left.left = TreeNode(0)
    root4_again.right.right = TreeNode(0)
    print("解法二（ID优化）结果: ")
    print_result(solution.findDuplicateSubtreesOptimized(root4_again))  # 预期输出类似: [0,0]
    print()
    
    # 性能测试 - 构建一个较大的树，其中有重复子树
    print("性能测试:")
    # 构建一个平衡树，其中有重复子树
    balanced_tree = build_balanced_tree_with_duplicates(1, 7)
    
    start_time = time.time()
    result1 = solution.findDuplicateSubtrees(balanced_tree)
    end_time = time.time()
    print(f"解法一（序列化）- 找到的重复子树数量: {len(result1)}")
    print(f"解法一（序列化）- 耗时: {(end_time - start_time) * 1000:.2f}ms")
    
    # 重新构建树
    balanced_tree_again = build_balanced_tree_with_duplicates(1, 7)
    start_time = time.time()
    result2 = solution.findDuplicateSubtreesOptimized(balanced_tree_again)
    end_time = time.time()
    print(f"解法二（ID优化）- 找到的重复子树数量: {len(result2)}")
    print(f"解法二（ID优化）- 耗时: {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    main()