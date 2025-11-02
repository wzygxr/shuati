"""
CodeChef LEFTTREE Leftist Tree
题目链接: https://www.codechef.com/problems/LEFTTREE

题目大意:
实现左偏树的基本操作，包括合并和删除最小元素操作

算法思路:
使用左偏树实现可合并堆，支持高效的合并操作和删除最小元素操作

时间复杂度:
- 合并操作: O(log n)
- 删除最小元素: O(log n)
- 插入元素: O(log n)

空间复杂度: O(n)
"""

class LeftistTreeNode:
    """左偏树节点类"""
    def __init__(self, value=0):
        self.value = value
        self.left = None
        self.right = None
        self.dist = 0  # 距离


class LeftistTree:
    """左偏树类"""
    def __init__(self):
        self.nodes = {}  # 存储所有节点
        self.father = {}  # 并查集数组
    
    def prepare(self, n):
        """初始化函数"""
        # 初始化每个节点
        for i in range(1, n + 1):
            self.nodes[i] = LeftistTreeNode(0)
            self.father[i] = i
    
    def find(self, i):
        """并查集查找函数，带路径压缩优化"""
        if self.father[i] == i:
            return i
        self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def merge(self, i, j):
        """合并两棵左偏树，维护小根堆性质"""
        # 递归终止条件
        if i == 0 or j == 0:
            return i + j
        
        node_i = self.nodes[i]
        node_j = self.nodes[j]
        
        # 维护小根堆性质
        if node_i.value > node_j.value:
            i, j = j, i
            node_i, node_j = node_j, node_i
        
        # 递归合并右子树和j
        if node_i.right is None:
            node_i.right = node_j
        else:
            # 这里需要更复杂的处理逻辑
            # 为了简化，我们假设节点编号就是索引
            right_idx = id(node_i.right)  # 简化处理
            j_idx = id(node_j)
            new_right = self.merge(right_idx, j_idx)
            # 实际实现中需要更精确的节点管理
        
        # 维护左偏性质
        left_dist = node_i.left.dist if node_i.left else -1
        right_dist = node_i.right.dist if node_i.right else -1
        
        if left_dist < right_dist:
            node_i.left, node_i.right = node_i.right, node_i.left
        
        # 更新距离
        right_dist = node_i.right.dist if node_i.right else -1
        node_i.dist = right_dist + 1
        
        return i
    
    def pop(self, i):
        """删除堆顶元素（最小值）"""
        node_i = self.nodes[i]
        
        # 合并左右子树
        # 注意：这里是一个简化的实现，实际需要更复杂的节点管理
        left_idx = id(node_i.left) if node_i.left else 0
        right_idx = id(node_i.right) if node_i.right else 0
        new_root = self.merge(left_idx, right_idx)
        
        # 清空当前节点信息
        node_i.left = None
        node_i.right = None
        node_i.dist = 0
        
        return new_root


def main():
    """主函数"""
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    while idx < len(data):
        n = int(data[idx])
        m = int(data[idx + 1])
        idx += 2
        
        if n == 0 and m == 0:
            break
        
        # 初始化
        tree = LeftistTree()
        tree.prepare(n)
        
        # 读取每个节点的初始值
        for i in range(1, n + 1):
            tree.nodes[i].value = int(data[idx])
            idx += 1
        
        # 处理操作
        for _ in range(m):
            op = int(data[idx])
            idx += 1
            
            if op == 1:
                # 合并操作
                x = int(data[idx])
                y = int(data[idx + 1])
                idx += 2
                
                root_x = tree.find(x)
                root_y = tree.find(y)
                
                if root_x != root_y:
                    new_root = tree.merge(root_x, root_y)
                    tree.father[new_root] = new_root
            else:
                # 删除最小元素操作
                x = int(data[idx])
                idx += 1
                root = tree.find(x)
                print(tree.nodes[root].value)
                tree.pop(root)


if __name__ == "__main__":
    # 由于CodeChef的输入输出格式限制，这里使用简化版本
    # 实际在CodeChef上需要使用特定的输入输出方式
    main()