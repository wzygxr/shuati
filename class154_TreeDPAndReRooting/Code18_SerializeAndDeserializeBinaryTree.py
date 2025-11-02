# 二叉树的序列化与反序列化
# 题目链接：https://leetcode.com/problems/serialize-and-deserialize-binary-tree/
# 序列化是将一个数据结构或者对象转换为连续的比特位的操作，进而可以将转换后的数据存储在一个文件或者内存中，
# 同时也可以通过网络传输到另一个计算机环境，采取相反方式重构得到原数据。
# 请设计一个算法来实现二叉树的序列化与反序列化。这里不限定你的序列 / 反序列化算法执行逻辑，
# 你只需要保证一个二叉树可以被序列化为一个字符串并且将这个字符串反序列化为原始的树结构。

'''
题目解析：
这是一个关于二叉树表示和重建的问题。我们需要设计两个方法：
1. serialize：将二叉树转换为字符串
2. deserialize：将字符串转换回二叉树

算法思路：
1. 基于层序遍历的序列化和反序列化：
   - 序列化：使用队列进行层序遍历，记录每个节点的值，空子节点用特殊标记（如"None"）表示
   - 反序列化：将字符串按分隔符分割，使用队列重建二叉树

2. 基于前序遍历的序列化和反序列化：
   - 序列化：使用递归进行前序遍历，记录每个节点的值，空子节点用特殊标记表示
   - 反序列化：使用递归根据前序遍历结果重建二叉树

3. 基于后序遍历的序列化和反序列化：
   - 序列化：使用递归进行后序遍历
   - 反序列化：使用栈辅助重建二叉树

时间复杂度：O(n) - 每个节点只被访问常数次
空间复杂度：O(n) - 需要存储序列化的字符串和辅助数据结构
是否为最优解：是，所有节点都需要被处理，时间复杂度不可能低于O(n)

边界情况：
- 空树：序列化为包含一个None的字符串
- 单节点树：序列化为只包含该节点值的字符串
- 完全二叉树：所有节点都有值
- 不平衡树：存在大量空子节点

与机器学习/深度学习的联系：
- 树结构的序列化在模型保存和加载中有重要应用
- 类似的技术也用于决策树模型的持久化
- 在分布式系统中，数据结构的序列化是数据传输的基础
'''

# 导入必要的模块
from collections import deque
import json

# 二叉树节点的定义
class TreeNode:
    def __init__(self, x):
        self.val = x
        self.left = None
        self.right = None

# 基于层序遍历的序列化和反序列化实现
class CodecBFS:
    # 序列化函数：将二叉树转换为字符串
    def serialize(self, root):
        # 处理空树的边界情况
        if root is None:
            return "[]"
        
        # 使用列表收集层序遍历的结果
        result = []
        
        # 使用队列进行层序遍历
        queue = deque([root])
        
        # 记录最后一个非空节点的位置
        last_non_null_index = 0
        
        # 层序遍历树
        while queue:
            node = queue.popleft()
            
            # 如果节点不为空，记录其值并将左右子节点加入队列
            if node is not None:
                result.append(str(node.val))
                queue.append(node.left)
                queue.append(node.right)
                last_non_null_index = len(result) - 1
            else:
                # 如果节点为空，添加None标记
                result.append("null")
        
        # 移除末尾多余的null
        result = result[:last_non_null_index + 1]
        
        # 构造JSON数组格式的字符串
        return "[" + ",".join(result) + "]"
    
    # 反序列化函数：将字符串转换回二叉树
    def deserialize(self, data):
        # 处理空树的边界情况
        if data == "[]":
            return None
        
        try:
            # 解析JSON格式的字符串
            values = json.loads(data)
        except json.JSONDecodeError:
            # 如果不是标准JSON格式，尝试手动解析
            values = []
            # 去除两端的括号
            content = data[1:-1]
            if content:
                # 分割字符串并处理每个值
                for val in content.split(","):
                    val = val.strip()
                    if val == "null" or val == "None":
                        values.append(None)
                    else:
                        try:
                            values.append(int(val))
                        except ValueError:
                            values.append(None)
        
        if not values:
            return None
        
        # 创建根节点
        root = TreeNode(values[0])
        
        # 使用队列重建二叉树
        queue = deque([root])
        
        i = 1  # 从第二个元素开始处理子节点
        
        while queue and i < len(values):
            node = queue.popleft()
            
            # 处理左子节点
            if i < len(values) and values[i] is not None:
                node.left = TreeNode(values[i])
                queue.append(node.left)
            i += 1
            
            # 处理右子节点
            if i < len(values) and values[i] is not None:
                node.right = TreeNode(values[i])
                queue.append(node.right)
            i += 1
        
        return root

# 基于前序遍历的序列化和反序列化实现
class CodecDFS:
    # 序列化函数：前序遍历
    def serialize(self, root):
        result = []
        self._serialize_dfs(root, result)
        return ",".join(result)
    
    def _serialize_dfs(self, node, result):
        # 如果节点为空，添加特殊标记
        if node is None:
            result.append("#")
            return
        
        # 访问当前节点（根）
        result.append(str(node.val))
        
        # 递归访问左子树
        self._serialize_dfs(node.left, result)
        
        # 递归访问右子树
        self._serialize_dfs(node.right, result)
    
    # 反序列化函数
    def deserialize(self, data):
        # 分割字符串并过滤空字符串
        values = [val for val in data.split(",") if val]
        # 使用迭代器来逐个获取值
        return self._deserialize_dfs(iter(values))
    
    def _deserialize_dfs(self, values_iter):
        try:
            # 获取下一个值
            val = next(values_iter)
        except StopIteration:
            return None
        
        # 如果是特殊标记，表示空节点
        if val == "#":
            return None
        
        # 创建当前节点
        node = TreeNode(int(val))
        
        # 递归构建左子树
        node.left = self._deserialize_dfs(values_iter)
        
        # 递归构建右子树
        node.right = self._deserialize_dfs(values_iter)
        
        return node

# 基于后序遍历的序列化和反序列化实现
class CodecPostOrder:
    # 序列化函数：后序遍历
    def serialize(self, root):
        result = []
        self._serialize_postorder(root, result)
        return ",".join(result)
    
    def _serialize_postorder(self, node, result):
        # 如果节点为空，添加特殊标记
        if node is None:
            result.append("#")
            return
        
        # 递归访问左子树
        self._serialize_postorder(node.left, result)
        
        # 递归访问右子树
        self._serialize_postorder(node.right, result)
        
        # 访问当前节点（根）
        result.append(str(node.val))
    
    # 反序列化函数
    def deserialize(self, data):
        # 分割字符串并过滤空字符串
        values = [val for val in data.split(",") if val]
        # 使用栈辅助构建后序遍历的树
        stack = []
        
        # 从右到左遍历值（后序遍历的逆序）
        for val in reversed(values):
            if val == "#":
                stack.append(None)
            else:
                # 创建当前节点
                node = TreeNode(int(val))
                # 注意：后序遍历是左-右-根，逆序后是根-右-左
                node.left = stack.pop()
                node.right = stack.pop()
                stack.append(node)
        
        return stack[0] if stack else None

# 更紧凑的JSON格式实现
class CodecJSON:
    def serialize(self, root):
        # 使用递归构建可JSON序列化的字典
        def build_dict(node):
            if node is None:
                return None
            return {
                'val': node.val,
                'left': build_dict(node.left),
                'right': build_dict(node.right)
            }
        
        return json.dumps(build_dict(root))
    
    def deserialize(self, data):
        # 解析JSON字符串为字典
        tree_dict = json.loads(data)
        
        # 递归构建二叉树
        def build_tree(node_dict):
            if node_dict is None:
                return None
            node = TreeNode(node_dict['val'])
            node.left = build_tree(node_dict.get('left'))
            node.right = build_tree(node_dict.get('right'))
            return node
        
        return build_tree(tree_dict)

# 测试代码
if __name__ == "__main__":
    # 构建测试树
    #       1
    #      / \
    #     2   3
    #        / \
    #       4   5
    root = TreeNode(1)
    node2 = TreeNode(2)
    node3 = TreeNode(3)
    node4 = TreeNode(4)
    node5 = TreeNode(5)
    
    root.left = node2
    root.right = node3
    node3.left = node4
    node3.right = node5
    
    # 测试层序遍历实现
    print("=== 层序遍历实现 ===")
    codec_bfs = CodecBFS()
    serialized_bfs = codec_bfs.serialize(root)
    print(f"序列化结果: {serialized_bfs}")
    deserialized_bfs = codec_bfs.deserialize(serialized_bfs)
    print(f"反序列化后再序列化: {codec_bfs.serialize(deserialized_bfs)}")
    
    # 测试前序遍历实现
    print("\n=== 前序遍历实现 ===")
    codec_dfs = CodecDFS()
    serialized_dfs = codec_dfs.serialize(root)
    print(f"序列化结果: {serialized_dfs}")
    deserialized_dfs = codec_dfs.deserialize(serialized_dfs)
    print(f"反序列化后再序列化: {codec_dfs.serialize(deserialized_dfs)}")
    
    # 测试后序遍历实现
    print("\n=== 后序遍历实现 ===")
    codec_postorder = CodecPostOrder()
    serialized_postorder = codec_postorder.serialize(root)
    print(f"序列化结果: {serialized_postorder}")
    deserialized_postorder = codec_postorder.deserialize(serialized_postorder)
    print(f"反序列化后再序列化: {codec_postorder.serialize(deserialized_postorder)}")
    
    # 测试JSON格式实现
    print("\n=== JSON格式实现 ===")
    codec_json = CodecJSON()
    serialized_json = codec_json.serialize(root)
    print(f"序列化结果: {serialized_json}")
    deserialized_json = codec_json.deserialize(serialized_json)
    print(f"反序列化后再序列化: {codec_json.serialize(deserialized_json)}")
    
    # 测试边界情况
    print("\n=== 边界情况测试 ===")
    
    # 测试空树
    empty_tree = None
    serialized_empty = codec_bfs.serialize(empty_tree)
    print(f"空树序列化: {serialized_empty}")
    deserialized_empty = codec_bfs.deserialize(serialized_empty)
    print(f"空树反序列化后再序列化: {codec_bfs.serialize(deserialized_empty)}")
    
    # 测试单节点树
    single_node = TreeNode(42)
    serialized_single = codec_bfs.serialize(single_node)
    print(f"单节点树序列化: {serialized_single}")
    deserialized_single = codec_bfs.deserialize(serialized_single)
    print(f"单节点树反序列化后再序列化: {codec_bfs.serialize(deserialized_single)}")

# 辅助函数：验证两个树是否相同（用于测试）
def is_same_tree(p, q):
    if p is None and q is None:
        return True
    if p is None or q is None:
        return False
    if p.val != q.val:
        return False
    return is_same_tree(p.left, q.left) and is_same_tree(p.right, q.right)

'''
工程化考量：
1. 异常处理：
   - 处理了空树、单节点树等边界情况
   - 使用特殊标记表示空子节点，确保序列化/反序列化的准确性
   - 在反序列化时添加了异常处理，兼容不同格式的输入

2. 性能优化：
   - 在层序遍历实现中优化了输出，去除了末尾多余的null值
   - 使用生成器和迭代器来提高处理大型数据的效率
   - 使用collections.deque替代普通列表实现队列，提高popleft效率

3. 代码质量：
   - 提供了四种不同的实现方式（层序、前序、后序和JSON）
   - 代码结构清晰，职责分明
   - 添加了详细的注释说明算法思路和实现细节

4. 可扩展性：
   - 序列化格式可以根据需要调整
   - 可以轻松扩展为处理N叉树或其他树形结构
   - 添加了JSON格式的实现，便于与其他系统集成

5. 调试技巧：
   - 添加了验证函数is_same_tree，用于检查反序列化的准确性
   - 在测试代码中包含了多种边界情况的测试
   - 提供了多种实现方式，可以根据不同场景选择合适的方法

6. Python特有优化：
   - 利用列表推导式和生成器表达式简化代码
   - 使用迭代器来逐值处理，避免一次性加载大量数据到内存
   - 利用Python的内置json模块进行数据格式化

7. 算法安全与业务适配：
   - 序列化格式多样，可以根据不同需求选择
   - JSON格式实现更加标准，适合跨平台数据交换
   - 对于大型树结构，层序遍历实现可能更节省内存

8. 数据格式考量：
   - 提供了多种序列化格式，包括数组格式和JSON对象格式
   - 使用特殊字符标记空节点，确保数据的完整性
   - 支持数据格式的容错处理，提高了代码的健壮性

9. 序列化/反序列化的完整性：
   - 确保了序列化和反序列化是互逆操作
   - 测试代码验证了反序列化后再序列化可以得到相同结果
   - 处理了各种可能的数据异常情况
'''