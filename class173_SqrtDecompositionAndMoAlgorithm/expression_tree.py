from typing import List, Optional, Tuple, Union, Dict, Set, Any
import math
from collections import deque

"""
表达式树（Expression Tree）实现
表达式树是一种用于表示数学表达式的二叉树
其中内部节点表示运算符，叶节点表示操作数

常见应用场景：
1. 表达式计算与求值
2. 表达式简化
3. 表达式转换（中缀转后缀/前缀）
4. 编译器和解释器中的语法树
5. 数学表达式的可视化
6. 布尔表达式的表示和求值
7. 科学计算和计算器应用

相关算法题目：
- LeetCode 150. 逆波兰表达式求值 https://leetcode.cn/problems/evaluate-reverse-polish-notation/
- LeetCode 224. 基本计算器 https://leetcode.cn/problems/basic-calculator/
- LeetCode 227. 基本计算器 II https://leetcode.cn/problems/basic-calculator-ii/
- LeetCode 772. 基本计算器 III https://leetcode.cn/problems/basic-calculator-iii/
- LintCode 366. 斐波纳契数列 https://www.lintcode.com/problem/366/
- 牛客 NC46 加起来和为目标值的组合 https://www.nowcoder.com/practice/75e6cd5b85ab41c6a7c43359a74e869a
- HackerRank Expression Evaluation https://www.hackerrank.com/challenges/expression-evaluation/problem
- CodeChef SNAKEEAT https://www.codechef.com/problems/SNAKEEAT
- USACO Section 3.4 The Primes https://usaco.org/index.php?page=viewproblem2&cpid=349
- AtCoder ABC182 E - Akari https://atcoder.jp/contests/abc182/tasks/abc182_e
- 杭电 OJ 1237 简单计算器 https://acm.hdu.edu.cn/showproblem.php?pid=1237
- SPOJ ONP - Transform the Expression https://www.spoj.com/problems/ONP/
- Codeforces 1077C - Good Array https://codeforces.com/problemset/problem/1077/C
"""


class TreeNode:
    """
    表达式树节点类
    """
    def __init__(self, value: str):
        """
        初始化节点
        
        Args:
            value: 节点值，可以是运算符或操作数
        """
        self.value = value
        self.left: Optional[TreeNode] = None
        self.right: Optional[TreeNode] = None
        # 判断是否为运算符（+、-、*、/、^）
        self.is_operator = value in '+-*/^'
    
    def is_leaf(self) -> bool:
        """
        判断是否为叶子节点（操作数）
        
        Returns:
            bool: 是否为叶子节点
        """
        return self.left is None and self.right is None
    
    def __str__(self) -> str:
        return self.value


class ExpressionTree:
    """
    表达式树类，支持从各种表示法构建并执行表达式计算
    """
    def __init__(self):
        """
        初始化表达式树
        """
        self.root: Optional[TreeNode] = None
    
    def build_from_postfix(self, postfix: str) -> None:
        """
        从后缀表达式构建表达式树
        
        Args:
            postfix: 后缀表达式字符串
        
        Raises:
            ValueError: 如果后缀表达式无效
        """
        # 分词处理
        tokens = self._tokenize_expression(postfix)
        stack = []
        
        for token in tokens:
            node = TreeNode(token)
            
            if node.is_operator:
                # 运算符需要两个操作数，从栈中弹出
                if len(stack) < 2:
                    raise ValueError(f"无效的后缀表达式: {postfix}")
                
                # 注意：先弹出的是右操作数
                node.right = stack.pop()
                node.left = stack.pop()
            
            # 操作数或运算符节点入栈
            stack.append(node)
        
        # 最终栈中应该只有一个节点（根节点）
        if len(stack) != 1:
            raise ValueError(f"无效的后缀表达式: {postfix}")
        
        self.root = stack.pop()
    
    def build_from_prefix(self, prefix: str) -> None:
        """
        从前缀表达式构建表达式树
        
        Args:
            prefix: 前缀表达式字符串
        
        Raises:
            ValueError: 如果前缀表达式无效
        """
        # 分词处理
        tokens = self._tokenize_expression(prefix)
        # 前缀表达式从右往左处理
        stack = []
        
        for token in reversed(tokens):
            node = TreeNode(token)
            
            if node.is_operator:
                # 运算符需要两个操作数，从栈中弹出
                if len(stack) < 2:
                    raise ValueError(f"无效的前缀表达式: {prefix}")
                
                # 注意：前缀表达式先弹出的是左操作数
                node.left = stack.pop()
                node.right = stack.pop()
            
            # 操作数或运算符节点入栈
            stack.append(node)
        
        # 最终栈中应该只有一个节点（根节点）
        if len(stack) != 1:
            raise ValueError(f"无效的前缀表达式: {prefix}")
        
        self.root = stack.pop()
    
    def build_from_infix(self, infix: str) -> None:
        """
        从中缀表达式构建表达式树
        
        Args:
            infix: 中缀表达式字符串
        
        Raises:
            ValueError: 如果中缀表达式无效
        """
        # 中缀表达式转后缀表达式，再构建表达式树
        postfix = self._infix_to_postfix(infix)
        self.build_from_postfix(postfix)
    
    def _tokenize_expression(self, expression: str) -> List[str]:
        """
        表达式分词处理
        
        Args:
            expression: 表达式字符串
        
        Returns:
            List[str]: 分词后的标记列表
        """
        tokens = []
        current_token = []
        
        for char in expression:
            # 跳过空白字符
            if char.isspace():
                if current_token:
                    tokens.append(''.join(current_token))
                    current_token = []
                continue
            
            # 处理运算符和括号
            if char in '+-*/^()':
                if current_token:
                    tokens.append(''.join(current_token))
                    current_token = []
                tokens.append(char)
            else:
                # 处理数字或变量
                current_token.append(char)
        
        # 添加最后一个标记
        if current_token:
            tokens.append(''.join(current_token))
        
        return tokens
    
    def _get_precedence(self, operator: str) -> int:
        """
        获取运算符的优先级
        
        Args:
            operator: 运算符字符串
        
        Returns:
            int: 优先级值（数字越大优先级越高）
        """
        precedence = {
            '+': 1,
            '-': 1,
            '*': 2,
            '/': 2,
            '^': 3
        }
        return precedence.get(operator, 0)
    
    def _infix_to_postfix(self, infix: str) -> str:
        """
        中缀表达式转换为后缀表达式
        
        Args:
            infix: 中缀表达式字符串
        
        Returns:
            str: 后缀表达式字符串
        
        Raises:
            ValueError: 如果表达式中的括号不匹配
        """
        tokens = self._tokenize_expression(infix)
        postfix_tokens = []
        stack = []
        
        for token in tokens:
            # 操作数直接添加到结果
            if token not in '()+-*/^':
                postfix_tokens.append(token)
            # 左括号入栈
            elif token == '(':
                stack.append(token)
            # 处理右括号
            elif token == ')':
                while stack and stack[-1] != '(':
                    postfix_tokens.append(stack.pop())
                if stack and stack[-1] == '(':
                    stack.pop()  # 弹出左括号
                else:
                    raise ValueError(f"括号不匹配的表达式: {infix}")
            # 处理运算符
            else:
                while (stack and stack[-1] != '(' and 
                       self._get_precedence(stack[-1]) >= self._get_precedence(token)):
                    postfix_tokens.append(stack.pop())
                stack.append(token)
        
        # 将栈中剩余的运算符添加到结果
        while stack:
            if stack[-1] == '(':
                raise ValueError(f"括号不匹配的表达式: {infix}")
            postfix_tokens.append(stack.pop())
        
        return ' '.join(postfix_tokens)
    
    def evaluate(self) -> float:
        """
        计算表达式树的值
        
        Returns:
            float: 计算结果
        
        Raises:
            ValueError: 如果节点值无法解析为数值
            ZeroDivisionError: 如果进行除零操作
            TypeError: 如果运算符未知
        """
        return self._evaluate(self.root)
    
    def _evaluate(self, node: Optional[TreeNode]) -> float:
        """
        递归计算表达式树的值
        
        Args:
            node: 当前节点
        
        Returns:
            float: 以该节点为根的子树计算结果
        """
        # 空节点，返回0
        if node is None:
            return 0.0
        
        # 叶节点是操作数，直接转换为数值
        if node.is_leaf():
            try:
                return float(node.value)
            except ValueError:
                raise ValueError(f"无法解析的操作数: {node.value}")
        
        # 递归计算左右子树的值
        left_val = self._evaluate(node.left)
        right_val = self._evaluate(node.right)
        
        # 根据运算符进行计算
        if node.value == '+':
            return left_val + right_val
        elif node.value == '-':
            return left_val - right_val
        elif node.value == '*':
            return left_val * right_val
        elif node.value == '/':
            if abs(right_val) < 1e-10:
                raise ZeroDivisionError("除零错误")
            return left_val / right_val
        elif node.value == '^':
            return math.pow(left_val, right_val)
        else:
            raise TypeError(f"未知的运算符: {node.value}")
    
    def to_infix_notation(self) -> str:
        """
        获取中缀表达式字符串（带括号）
        
        Returns:
            str: 中缀表达式字符串
        """
        result = []
        self._to_infix_notation(self.root, result)
        return ''.join(result)
    
    def _to_infix_notation(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归生成中缀表达式
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        # 对于非叶节点（运算符），需要添加括号
        if not node.is_leaf():
            result.append('(')
        
        # 递归处理左子树
        self._to_infix_notation(node.left, result)
        
        # 添加当前节点值
        result.append(node.value)
        
        # 递归处理右子树
        self._to_infix_notation(node.right, result)
        
        # 对于非叶节点（运算符），需要添加右括号
        if not node.is_leaf():
            result.append(')')
    
    def to_postfix_notation(self) -> str:
        """
        获取后缀表达式字符串
        
        Returns:
            str: 后缀表达式字符串
        """
        result = []
        self._to_postfix_notation(self.root, result)
        return ' '.join(result)
    
    def _to_postfix_notation(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归生成后缀表达式（后续遍历）
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        # 递归处理左右子树
        self._to_postfix_notation(node.left, result)
        self._to_postfix_notation(node.right, result)
        
        # 添加当前节点值
        result.append(node.value)
    
    def to_prefix_notation(self) -> str:
        """
        获取前缀表达式字符串
        
        Returns:
            str: 前缀表达式字符串
        """
        result = []
        self._to_prefix_notation(self.root, result)
        return ' '.join(result)
    
    def _to_prefix_notation(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归生成前缀表达式（前序遍历）
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        # 添加当前节点值
        result.append(node.value)
        
        # 递归处理左右子树
        self._to_prefix_notation(node.left, result)
        self._to_prefix_notation(node.right, result)
    
    def print_tree(self) -> None:
        """
        打印表达式树结构
        """
        print("表达式树结构:")
        self._print_tree(self.root, 0)
    
    def _print_tree(self, node: Optional[TreeNode], level: int) -> None:
        """
        递归打印树结构
        
        Args:
            node: 当前节点
            level: 当前节点深度
        """
        if node is None:
            return
        
        # 先打印右子树（在上层）
        self._print_tree(node.right, level + 1)
        
        # 打印当前节点
        print('    ' * level + str(node.value))
        
        # 打印左子树（在下层）
        self._print_tree(node.left, level + 1)
    
    def get_height(self) -> int:
        """
        获取树的高度
        
        Returns:
            int: 树的高度
        """
        return self._get_height(self.root)
    
    def _get_height(self, node: Optional[TreeNode]) -> int:
        """
        递归计算树的高度
        
        Args:
            node: 当前节点
        
        Returns:
            int: 以该节点为根的子树高度
        """
        if node is None:
            return 0
        
        left_height = self._get_height(node.left)
        right_height = self._get_height(node.right)
        
        return max(left_height, right_height) + 1
    
    def get_node_count(self) -> int:
        """
        获取节点数量
        
        Returns:
            int: 节点数量
        """
        return self._get_node_count(self.root)
    
    def _get_node_count(self, node: Optional[TreeNode]) -> int:
        """
        递归计算节点数量
        
        Args:
            node: 当前节点
        
        Returns:
            int: 以该节点为根的子树节点数量
        """
        if node is None:
            return 0
        
        return (self._get_node_count(node.left) + 
                self._get_node_count(node.right) + 1)
    
    def get_leaf_count(self) -> int:
        """
        获取叶节点数量（操作数数量）
        
        Returns:
            int: 叶节点数量
        """
        return self._get_leaf_count(self.root)
    
    def _get_leaf_count(self, node: Optional[TreeNode]) -> int:
        """
        递归计算叶节点数量
        
        Args:
            node: 当前节点
        
        Returns:
            int: 以该节点为根的子树叶节点数量
        """
        if node is None:
            return 0
        
        if node.is_leaf():
            return 1
        
        return (self._get_leaf_count(node.left) + 
                self._get_leaf_count(node.right))
    
    def get_operator_count(self) -> int:
        """
        获取运算符节点数量
        
        Returns:
            int: 运算符节点数量
        """
        return self._get_operator_count(self.root)
    
    def _get_operator_count(self, node: Optional[TreeNode]) -> int:
        """
        递归计算运算符节点数量
        
        Args:
            node: 当前节点
        
        Returns:
            int: 以该节点为根的子树运算符节点数量
        """
        if node is None:
            return 0
        
        count = 1 if node.is_operator else 0
        return (count + self._get_operator_count(node.left) + 
                self._get_operator_count(node.right))
    
    def preorder_traversal(self) -> List[str]:
        """
        前序遍历表达式树
        
        Returns:
            List[str]: 前序遍历结果列表
        """
        result = []
        self._preorder_traversal(self.root, result)
        return result
    
    def _preorder_traversal(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归进行前序遍历
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        result.append(node.value)
        self._preorder_traversal(node.left, result)
        self._preorder_traversal(node.right, result)
    
    def inorder_traversal(self) -> List[str]:
        """
        中序遍历表达式树
        
        Returns:
            List[str]: 中序遍历结果列表
        """
        result = []
        self._inorder_traversal(self.root, result)
        return result
    
    def _inorder_traversal(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归进行中序遍历
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        self._inorder_traversal(node.left, result)
        result.append(node.value)
        self._inorder_traversal(node.right, result)
    
    def postorder_traversal(self) -> List[str]:
        """
        后序遍历表达式树
        
        Returns:
            List[str]: 后序遍历结果列表
        """
        result = []
        self._postorder_traversal(self.root, result)
        return result
    
    def _postorder_traversal(self, node: Optional[TreeNode], result: List[str]) -> None:
        """
        递归进行后序遍历
        
        Args:
            node: 当前节点
            result: 结果列表
        """
        if node is None:
            return
        
        self._postorder_traversal(node.left, result)
        self._postorder_traversal(node.right, result)
        result.append(node.value)
    
    def level_order_traversal(self) -> List[List[str]]:
        """
        层序遍历表达式树
        
        Returns:
            List[List[str]]: 层序遍历结果列表，每个子列表代表一层
        """
        result = []
        if self.root is None:
            return result
        
        queue = deque([self.root])
        
        while queue:
            level_size = len(queue)
            current_level = []
            
            for _ in range(level_size):
                node = queue.popleft()
                current_level.append(node.value)
                
                if node.left:
                    queue.append(node.left)
                if node.right:
                    queue.append(node.right)
            
            result.append(current_level)
        
        return result
    
    def copy(self) -> 'ExpressionTree':
        """
        复制表达式树
        
        Returns:
            ExpressionTree: 复制的表达式树
        """
        new_tree = ExpressionTree()
        new_tree.root = self._copy_node(self.root)
        return new_tree
    
    def _copy_node(self, node: Optional[TreeNode]) -> Optional[TreeNode]:
        """
        递归复制节点
        
        Args:
            node: 要复制的节点
        
        Returns:
            Optional[TreeNode]: 复制的节点
        """
        if node is None:
            return None
        
        new_node = TreeNode(node.value)
        new_node.left = self._copy_node(node.left)
        new_node.right = self._copy_node(node.right)
        return new_node
    
    def __eq__(self, other: Any) -> bool:
        """
        判断两个表达式树是否相等
        
        Args:
            other: 另一个对象
        
        Returns:
            bool: 如果相等返回True，否则返回False
        """
        if not isinstance(other, ExpressionTree):
            return False
        return self._equals_node(self.root, other.root)
    
    def _equals_node(self, node1: Optional[TreeNode], node2: Optional[TreeNode]) -> bool:
        """
        递归判断两个节点是否相等
        
        Args:
            node1: 第一个节点
            node2: 第二个节点
        
        Returns:
            bool: 如果相等返回True，否则返回False
        """
        if node1 is None and node2 is None:
            return True
        if node1 is None or node2 is None:
            return False
        
        return (node1.value == node2.value and 
                self._equals_node(node1.left, node2.left) and 
                self._equals_node(node1.right, node2.right))


# 测试代码
if __name__ == "__main__":
    try:
        # 创建表达式树实例
        tree = ExpressionTree()
        
        # 测试从中缀表达式构建
        print("===== 从中缀表达式构建 =====")
        infix_expression = "3 + 4 * 2 / ( 1 - 5 ) ^ 2 ^ 3"
        tree.build_from_infix(infix_expression)
        
        # 打印树结构
        tree.print_tree()
        
        # 显示不同形式的表达式
        print(f"中缀表达式: {tree.to_infix_notation()}")
        print(f"后缀表达式: {tree.to_postfix_notation()}")
        print(f"前缀表达式: {tree.to_prefix_notation()}")
        
        # 计算表达式值
        print(f"表达式值: {tree.evaluate()}")
        
        # 显示树的统计信息
        print(f"树高: {tree.get_height()}")
        print(f"节点数: {tree.get_node_count()}")
        print(f"叶节点数(操作数): {tree.get_leaf_count()}")
        print(f"运算符节点数: {tree.get_operator_count()}")
        
        # 遍历结果
        print(f"前序遍历: {' '.join(tree.preorder_traversal())}")
        print(f"中序遍历: {' '.join(tree.inorder_traversal())}")
        print(f"后序遍历: {' '.join(tree.postorder_traversal())}")
        
        # 层序遍历
        print("层序遍历:")
        level_order = tree.level_order_traversal()
        for i, level in enumerate(level_order):
            print(f"层 {i + 1}: {' '.join(level)}")
        
        # 测试从后缀表达式构建
        print("\n===== 从后缀表达式构建 =====")
        tree2 = ExpressionTree()
        postfix_expression = "3 4 2 * 1 5 - 2 3 ^ ^ / +"
        tree2.build_from_postfix(postfix_expression)
        print(f"构建的表达式值: {tree2.evaluate()}")
        print(f"两棵树是否相等: {tree == tree2}")
        
        # 测试从前缀表达式构建
        print("\n===== 从前缀表达式构建 =====")
        tree3 = ExpressionTree()
        prefix_expression = "+ 3 / * 4 2 ^ ^ - 1 5 2 3"
        tree3.build_from_prefix(prefix_expression)
        print(f"构建的表达式值: {tree3.evaluate()}")
        print(f"与原始树是否相等: {tree == tree3}")
        
        # 测试表达式复制
        copy_tree = tree.copy()
        print("\n===== 表达式树复制 =====")
        print(f"复制树的表达式值: {copy_tree.evaluate()}")
        print(f"与原始树是否相等: {tree == copy_tree}")
        
        # 测试更复杂的表达式
        print("\n===== 复杂表达式测试 =====")
        complex_tree = ExpressionTree()
        complex_tree.build_from_infix("((2 + 3) * (5 - 2)) / (1 + 2 * 3)")
        print(f"复杂表达式值: {complex_tree.evaluate()}")
        
    except Exception as e:
        print(f"错误: {e}")
        import traceback
        traceback.print_exc()