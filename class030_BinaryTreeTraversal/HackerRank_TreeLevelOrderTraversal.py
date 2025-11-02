from collections import deque
from typing import Optional

# HackerRank Tree: Level Order Traversal
# 题目链接: https://www.hackerrank.com/challenges/tree-level-order-traversal/problem
# 题目大意: 给你一个二叉树的根节点，按照层序遍历的方式打印所有节点的值，从左到右，一层一层地打印。

class Node:
    def __init__(self, info): 
        self.info = info  
        self.left: Optional['Node'] = None  
        self.right: Optional['Node'] = None 
        self.level = None 

    def __str__(self):
        return str(self.info) 

class BinarySearchTree:
    def __init__(self): 
        self.root: Optional[Node] = None

    def create(self, val):  
        if self.root == None:
            self.root = Node(val)
        else:
            current = self.root
         
            while True:
                if val < current.info:
                    if current.left:
                        current = current.left
                    else:
                        current.left = Node(val)
                        break
                elif val > current.info:
                    if current.right:
                        current = current.right
                    else:
                        current.right = Node(val)
                        break
                else:
                    break

def levelOrder(root: Optional[Node]) -> None:
    """
    层序遍历实现
    思路:
    1. 使用队列进行层序遍历
    2. 从根节点开始，将节点加入队列
    3. 当队列不为空时，取出队首节点并打印其值
    4. 将该节点的左右子节点（如果存在）加入队列
    5. 重复步骤3-4直到队列为空
    时间复杂度: O(n) - n是节点数量，每个节点访问一次
    空间复杂度: O(w) - w是树的最大宽度，队列中最多存储一层的节点
    """
    if not root:
        return
    
    # 使用双端队列存储待访问的节点
    queue = deque([root])
    
    # 当队列不为空时继续遍历
    while queue:
        # 取出队首节点
        current = queue.popleft()
        
        # 打印当前节点的值
        print(current.info, end=" ")
        
        # 将左右子节点加入队列（如果存在）
        if current.left:
            queue.append(current.left)
        if current.right:
            queue.append(current.right)

# 以下代码是HackerRank提供的测试框架，无需修改
if __name__ == "__main__":
    tree = BinarySearchTree()
    t = int(input())

    arr = list(map(int, input().split()))

    for i in range(t):
        tree.create(arr[i])

    levelOrder(tree.root)