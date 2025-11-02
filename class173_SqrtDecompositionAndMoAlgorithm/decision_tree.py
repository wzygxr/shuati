#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
决策树（Decision Tree）实现
决策树是一种用于分类和回归的监督学习算法
它通过从数据特征中学习决策规则，构建树状模型进行预测

常见应用场景：
1. 分类问题（如疾病诊断、客户流失预测）
2. 回归问题（如房价预测、销售量预测）
3. 特征重要性分析
4. 规则提取（从树结构中提取业务规则）
5. 数据分析和数据挖掘
6. 风险评估和信用评分

相关算法题目：
- LeetCode 222. 完全二叉树的节点个数 https://leetcode.cn/problems/count-complete-tree-nodes/
- LeetCode 637. 二叉树的层平均值 https://leetcode.cn/problems/average-of-levels-in-binary-tree/
- LeetCode 572. 另一棵树的子树 https://leetcode.cn/problems/subtree-of-another-tree/
- LintCode 95. 验证二叉查找树 https://www.lintcode.com/problem/95/
- 洛谷 P3379 【模板】最近公共祖先 https://www.luogu.com.cn/problem/P3379
- 牛客 NC119 最小的K个数 https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
- HackerRank Tree: Huffman Decoding https://www.hackerrank.com/challenges/tree-huffman-decoding/problem
- CodeChef DECMTREE https://www.codechef.com/problems/DECMTREE
- USACO Section 3.4 Raucous Rockers https://usaco.org/index.php?page=viewproblem2&cpid=348
- AtCoder ABC177 F - I hate Shortest Path Problem https://atcoder.jp/contests/abc177/tasks/abc177_f
- 杭电 OJ 2544 最短路 https://acm.hdu.edu.cn/showproblem.php?pid=2544
- SPOJ PT07Z - Longest path in a tree https://www.spoj.com/problems/PT07Z/
- Codeforces 1327C - Game with Chips https://codeforces.com/problemset/problem/1327/C
"""

from typing import Dict, List, Optional, Any, Union, Tuple
import math
from collections import Counter, deque


class TreeNode:
    """决策树节点类"""
    
    def __init__(self, attribute: Optional[str] = None, 
                 information_gain: float = 0.0,
                 classification: Optional[str] = None,
                 value: Optional[float] = None,
                 depth: int = 0):
        """初始化节点
        
        Args:
            attribute: 用于分裂的属性
            information_gain: 分裂时的信息增益
            classification: 叶节点的分类结果
            value: 回归树的预测值
            depth: 节点深度
        """
        self.attribute = attribute  # 用于分裂的属性
        self.information_gain = information_gain  # 信息增益
        self.classification = classification  # 分类结果（叶节点）
        self.value = value  # 回归值（叶节点）
        self.depth = depth  # 节点深度
        self.children = {}  # 子节点映射 {属性值: 子节点}
    
    @property
    def is_leaf(self) -> bool:
        """判断是否为叶节点
        
        Returns:
            是否为叶节点
        """
        return self.classification is not None or self.value is not None
    
    def add_child(self, attribute_value: Any, child: 'TreeNode') -> None:
        """添加子节点
        
        Args:
            attribute_value: 属性值
            child: 子节点
        """
        self.children[str(attribute_value)] = child
    
    def __str__(self) -> str:
        """字符串表示
        
        Returns:
            节点的字符串表示
        """
        if self.is_leaf:
            if self.classification is not None:
                return f"叶节点(分类={self.classification}, 深度={self.depth})"
            else:
                return f"叶节点(值={self.value:.4f}, 深度={self.depth})"
        else:
            return f"内部节点(属性={self.attribute}, 信息增益={self.information_gain:.4f}, 深度={self.depth})"


class DataRow:
    """数据集行类，用于表示数据集中的一行数据"""
    
    def __init__(self, attributes: Dict[str, Any], 
                 label: Optional[str] = None,
                 value: Optional[float] = None):
        """初始化数据行
        
        Args:
            attributes: 属性及其值的映射
            label: 分类标签
            value: 回归目标值
        """
        self.attributes = attributes.copy()
        self.label = label
        self.value = value
    
    def get_attribute_value(self, attribute: str) -> Any:
        """获取属性值
        
        Args:
            attribute: 属性名
            
        Returns:
            属性值
        """
        return self.attributes.get(attribute)
    
    def get_all_attributes(self) -> Dict[str, Any]:
        """获取所有属性
        
        Returns:
            属性映射
        """
        return self.attributes.copy()
    
    def is_classification(self) -> bool:
        """判断是否为分类数据
        
        Returns:
            是否为分类数据
        """
        return self.label is not None
    
    def __str__(self) -> str:
        """字符串表示
        
        Returns:
            数据行的字符串表示
        """
        if self.is_classification():
            return f"DataRow(attributes={self.attributes}, label='{self.label}')"
        else:
            return f"DataRow(attributes={self.attributes}, value={self.value})"


class DecisionTree:
    """决策树类"""
    
    def __init__(self, max_depth: int = 10, 
                 min_samples_split: int = 2,
                 is_regression: bool = False):
        """构造函数
        
        Args:
            max_depth: 最大深度
            min_samples_split: 最小分裂样本数
            is_regression: 是否为回归树
        """
        self.max_depth = max_depth
        self.min_samples_split = min_samples_split
        self.is_regression = is_regression
        self.root = None
        self.attributes = None
    
    def fit(self, data_set: List[DataRow], attributes: List[str]) -> None:
        """训练决策树
        
        Args:
            data_set: 数据集
            attributes: 属性列表
        """
        self.attributes = attributes.copy()
        self.root = self._build_tree(data_set, attributes.copy(), 0)
    
    def _build_tree(self, data_set: List[DataRow], 
                   available_attributes: List[str],
                   depth: int) -> TreeNode:
        """递归构建决策树
        
        Args:
            data_set: 当前数据集
            available_attributes: 可用属性列表
            depth: 当前深度
            
        Returns:
            构建的节点
        """
        # 检查是否满足终止条件
        if self._should_stop(data_set, available_attributes, depth):
            return self._create_leaf_node(data_set, depth)
        
        # 选择最佳分裂属性
        best_attribute, best_gain = self._select_best_attribute(
            data_set, available_attributes)
        
        # 创建内部节点
        node = TreeNode(
            attribute=best_attribute,
            information_gain=best_gain,
            depth=depth
        )
        
        # 根据最佳属性的值分割数据集
        partitions = self._partition_by_attribute(data_set, best_attribute)
        
        # 为每个属性值创建子树
        for value, subset in partitions.items():
            # 如果子集为空，创建叶节点
            if not subset:
                node.add_child(value, self._create_leaf_node(data_set, depth + 1))
            else:
                # 递归构建子树
                new_attributes = available_attributes.copy()
                new_attributes.remove(best_attribute)
                child_node = self._build_tree(subset, new_attributes, depth + 1)
                node.add_child(value, child_node)
        
        return node
    
    def _should_stop(self, data_set: List[DataRow],
                    available_attributes: List[str],
                    depth: int) -> bool:
        """判断是否应该停止树的生长
        
        Args:
            data_set: 当前数据集
            available_attributes: 可用属性列表
            depth: 当前深度
            
        Returns:
            是否应该停止
        """
        # 1. 数据集为空
        if not data_set:
            return True
        
        # 2. 达到最大深度
        if depth >= self.max_depth:
            return True
        
        # 3. 没有可用属性
        if not available_attributes:
            return True
        
        # 4. 样本数小于最小分裂样本数
        if len(data_set) < self.min_samples_split:
            return True
        
        # 5. 所有样本属于同一类别（分类树）
        if not self.is_regression:
            first_label = data_set[0].label
            all_same = all(row.label == first_label for row in data_set)
            if all_same:
                return True
        
        # 5. 所有样本的目标值相同（回归树）
        else:
            first_value = data_set[0].value
            all_same = all(math.isclose(row.value, first_value) for row in data_set)
            if all_same:
                return True
        
        return False
    
    def _create_leaf_node(self, data_set: List[DataRow], depth: int) -> TreeNode:
        """创建叶节点
        
        Args:
            data_set: 数据集
            depth: 深度
            
        Returns:
            叶节点
        """
        if not data_set:
            return TreeNode(value=0.0, depth=depth) if self.is_regression else \
                   TreeNode(classification="Unknown", depth=depth)
        
        if not self.is_regression:
            # 分类树：返回最常见的类别
            labels = [row.label for row in data_set]
            label_counts = Counter(labels)
            most_common_label = label_counts.most_common(1)[0][0]
            return TreeNode(classification=most_common_label, depth=depth)
        else:
            # 回归树：返回平均值
            values = [row.value for row in data_set]
            average = sum(values) / len(values)
            return TreeNode(value=average, depth=depth)
    
    def _select_best_attribute(self, data_set: List[DataRow],
                              available_attributes: List[str]) -> Tuple[str, float]:
        """选择最佳分裂属性
        
        Args:
            data_set: 数据集
            available_attributes: 可用属性列表
            
        Returns:
            (最佳属性名, 最大增益值)
        """
        best_attribute = None
        best_gain = -float('inf')
        
        for attribute in available_attributes:
            gain = self._calculate_gain(data_set, attribute)
            if gain > best_gain:
                best_gain = gain
                best_attribute = attribute
        
        return best_attribute, best_gain
    
    def _calculate_gain(self, data_set: List[DataRow], attribute: str) -> float:
        """计算信息增益（分类树）或方差减少（回归树）
        
        Args:
            data_set: 数据集
            attribute: 属性名
            
        Returns:
            信息增益或方差减少量
        """
        if not data_set:
            return 0.0
        
        if not self.is_regression:
            # 计算信息增益（使用熵）
            parent_entropy = self._calculate_entropy(data_set)
            partitions = self._partition_by_attribute(data_set, attribute)
            
            weighted_entropy = 0.0
            for subset in partitions.values():
                weight = len(subset) / len(data_set)
                weighted_entropy += weight * self._calculate_entropy(subset)
            
            return parent_entropy - weighted_entropy
        else:
            # 计算方差减少
            parent_variance = self._calculate_variance(data_set)
            partitions = self._partition_by_attribute(data_set, attribute)
            
            weighted_variance = 0.0
            for subset in partitions.values():
                weight = len(subset) / len(data_set)
                weighted_variance += weight * self._calculate_variance(subset)
            
            return parent_variance - weighted_variance
    
    def _calculate_entropy(self, data_set: List[DataRow]) -> float:
        """计算数据集的熵
        
        Args:
            data_set: 数据集
            
        Returns:
            熵值
        """
        if not data_set:
            return 0.0
        
        labels = [row.label for row in data_set]
        label_counts = Counter(labels)
        
        entropy = 0.0
        total = len(data_set)
        
        for count in label_counts.values():
            probability = count / total
            entropy -= probability * math.log(probability, 2)
        
        return entropy
    
    def _calculate_variance(self, data_set: List[DataRow]) -> float:
        """计算数据集的方差
        
        Args:
            data_set: 数据集
            
        Returns:
            方差
        """
        if not data_set:
            return 0.0
        
        values = [row.value for row in data_set]
        mean = sum(values) / len(values)
        
        variance = sum((x - mean) ** 2 for x in values) / len(values)
        return variance
    
    def _partition_by_attribute(self, data_set: List[DataRow], 
                               attribute: str) -> Dict[Any, List[DataRow]]:
        """根据属性值分割数据集
        
        Args:
            data_set: 数据集
            attribute: 属性名
            
        Returns:
            分割后的数据集映射
        """
        partitions = {}
        for row in data_set:
            value = row.get_attribute_value(attribute)
            if value not in partitions:
                partitions[value] = []
            partitions[value].append(row)
        return partitions
    
    def predict(self, attributes: Dict[str, Any]) -> Union[str, float]:
        """预测单个样本
        
        Args:
            attributes: 属性映射
            
        Returns:
            预测结果（分类标签或回归值）
        """
        if self.root is None:
            raise ValueError("决策树尚未训练")
        
        return self._predict_recursive(self.root, attributes)
    
    def _predict_recursive(self, node: TreeNode, 
                          attributes: Dict[str, Any]) -> Union[str, float]:
        """递归预测
        
        Args:
            node: 当前节点
            attributes: 属性映射
            
        Returns:
            预测结果
        """
        # 如果是叶节点，返回分类或回归值
        if node.is_leaf:
            return node.classification if node.classification is not None else node.value
        
        # 获取当前节点用于分裂的属性
        split_attribute = node.attribute
        attribute_value = attributes.get(split_attribute)
        
        # 如果找不到对应属性值的子节点，返回默认预测值
        if attribute_value is None or str(attribute_value) not in node.children:
            return self._get_default_prediction(node)
        
        # 递归到子节点
        child = node.children[str(attribute_value)]
        return self._predict_recursive(child, attributes)
    
    def _get_default_prediction(self, node: TreeNode) -> Union[str, float]:
        """获取默认预测值（当无法找到路径时）
        
        Args:
            node: 当前节点
            
        Returns:
            默认预测值
        """
        # 这里简化处理，实际应该根据子节点情况返回最常见的分类或平均值
        return 0.0 if self.is_regression else "Unknown"
    
    def predict_all(self, data_set: List[DataRow]) -> List[Union[str, float]]:
        """批量预测
        
        Args:
            data_set: 测试数据集
            
        Returns:
            预测结果列表
        """
        return [self.predict(row.get_all_attributes()) for row in data_set]
    
    def calculate_accuracy(self, test_data: List[DataRow]) -> float:
        """计算分类准确率
        
        Args:
            test_data: 测试数据
            
        Returns:
            准确率
        
        Raises:
            ValueError: 如果用于回归树
        """
        if self.is_regression:
            raise ValueError("不能对回归树计算准确率，请使用均方误差等指标")
        
        correct_count = 0
        for row in test_data:
            prediction = self.predict(row.get_all_attributes())
            if prediction == row.label:
                correct_count += 1
        
        return correct_count / len(test_data) if test_data else 0.0
    
    def calculate_mse(self, test_data: List[DataRow]) -> float:
        """计算均方误差（MSE）
        
        Args:
            test_data: 测试数据
            
        Returns:
            均方误差
            
        Raises:
            ValueError: 如果用于分类树
        """
        if not self.is_regression:
            raise ValueError("不能对分类树计算均方误差，请使用准确率等指标")
        
        sum_squared_errors = 0.0
        for row in test_data:
            prediction = self.predict(row.get_all_attributes())
            actual = row.value
            sum_squared_errors += (prediction - actual) ** 2
        
        return sum_squared_errors / len(test_data) if test_data else 0.0
    
    def print_tree(self) -> None:
        """打印决策树"""
        if self.root is None:
            print("决策树为空")
            return
        
        print("决策树结构：")
        self._print_tree_recursive(self.root, "")
    
    def _print_tree_recursive(self, node: TreeNode, prefix: str) -> None:
        """递归打印树结构
        
        Args:
            node: 当前节点
            prefix: 前缀
        """
        print(prefix + str(node))
        
        if not node.is_leaf:
            for value, child in node.children.items():
                print(f"{prefix}  |__ {node.attribute} = {value}")
                self._print_tree_recursive(child, prefix + "      ")
    
    def get_height(self) -> int:
        """获取树的高度
        
        Returns:
            树的高度
        """
        return self._calculate_height(self.root)
    
    def _calculate_height(self, node: TreeNode) -> int:
        """递归计算树高
        
        Args:
            node: 当前节点
            
        Returns:
            树高
        """
        if node is None:
            return 0
        
        if node.is_leaf:
            return 1
        
        max_child_height = 0
        for child in node.children.values():
            child_height = self._calculate_height(child)
            max_child_height = max(max_child_height, child_height)
        
        return max_child_height + 1
    
    def get_node_count(self) -> int:
        """获取节点数量
        
        Returns:
            节点数量
        """
        return self._count_nodes(self.root)
    
    def _count_nodes(self, node: TreeNode) -> int:
        """递归计算节点数量
        
        Args:
            node: 当前节点
            
        Returns:
            节点数量
        """
        if node is None:
            return 0
        
        count = 1  # 当前节点
        for child in node.children.values():
            count += self._count_nodes(child)
        
        return count
    
    def get_leaf_count(self) -> int:
        """获取叶节点数量
        
        Returns:
            叶节点数量
        """
        return self._count_leaves(self.root)
    
    def _count_leaves(self, node: TreeNode) -> int:
        """递归计算叶节点数量
        
        Args:
            node: 当前节点
            
        Returns:
            叶节点数量
        """
        if node is None:
            return 0
        
        if node.is_leaf:
            return 1
        
        leaf_count = 0
        for child in node.children.values():
            leaf_count += self._count_leaves(child)
        
        return leaf_count
    
    def get_root(self) -> Optional[TreeNode]:
        """获取根节点
        
        Returns:
            根节点
        """
        return self.root
    
    def level_order_traversal(self) -> List[List[str]]:
        """层序遍历决策树
        
        Returns:
            层序遍历结果
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
                current_level.append(str(node))
                
                if not node.is_leaf:
                    for child in node.children.values():
                        queue.append(child)
            
            result.append(current_level)
        
        return result


# 测试代码
if __name__ == "__main__":
    # 创建示例数据（天气数据集 - 分类问题）
    weather_data = []
    attributes = ["Outlook", "Temperature", "Humidity", "Windy"]
    
    # 添加训练数据
    attr1 = {"Outlook": "Sunny", "Temperature": "Hot", "Humidity": "High", "Windy": False}
    weather_data.append(DataRow(attr1, label="No"))
    
    attr2 = {"Outlook": "Sunny", "Temperature": "Hot", "Humidity": "High", "Windy": True}
    weather_data.append(DataRow(attr2, label="No"))
    
    attr3 = {"Outlook": "Overcast", "Temperature": "Hot", "Humidity": "High", "Windy": False}
    weather_data.append(DataRow(attr3, label="Yes"))
    
    attr4 = {"Outlook": "Rainy", "Temperature": "Mild", "Humidity": "High", "Windy": False}
    weather_data.append(DataRow(attr4, label="Yes"))
    
    attr5 = {"Outlook": "Rainy", "Temperature": "Cool", "Humidity": "Normal", "Windy": False}
    weather_data.append(DataRow(attr5, label="Yes"))
    
    attr6 = {"Outlook": "Rainy", "Temperature": "Cool", "Humidity": "Normal", "Windy": True}
    weather_data.append(DataRow(attr6, label="No"))
    
    attr7 = {"Outlook": "Overcast", "Temperature": "Cool", "Humidity": "Normal", "Windy": True}
    weather_data.append(DataRow(attr7, label="Yes"))
    
    attr8 = {"Outlook": "Sunny", "Temperature": "Mild", "Humidity": "High", "Windy": False}
    weather_data.append(DataRow(attr8, label="No"))
    
    attr9 = {"Outlook": "Sunny", "Temperature": "Cool", "Humidity": "Normal", "Windy": False}
    weather_data.append(DataRow(attr9, label="Yes"))
    
    attr10 = {"Outlook": "Rainy", "Temperature": "Mild", "Humidity": "Normal", "Windy": False}
    weather_data.append(DataRow(attr10, label="Yes"))
    
    # 训练分类决策树
    classifier = DecisionTree(max_depth=10, min_samples_split=2, is_regression=False)
    classifier.fit(weather_data, attributes)
    
    # 打印决策树
    print("===== 分类决策树 ======")
    classifier.print_tree()
    
    # 测试预测
    test_attr = {"Outlook": "Sunny", "Temperature": "Mild", "Humidity": "Normal", "Windy": True}
    prediction = classifier.predict(test_attr)
    print(f"\n测试样本预测结果: {prediction}")
    
    # 模型统计信息
    print(f"\n树高: {classifier.get_height()}")
    print(f"节点数: {classifier.get_node_count()}")
    print(f"叶节点数: {classifier.get_leaf_count()}")
    
    # 计算训练集准确率
    accuracy = classifier.calculate_accuracy(weather_data)
    print(f"训练集准确率: {accuracy * 100:.2f}%")
    
    # 层序遍历
    print("\n层序遍历：")
    level_order = classifier.level_order_traversal()
    for i, level in enumerate(level_order):
        print(f"层 {i + 1}: {level}")
    
    # 创建回归树测试数据
    regression_data = []
    regression_attrs = ["Size", "Bedrooms", "Bathrooms", "Age"]
    
    # 添加房屋价格预测数据（简化示例）
    house1 = {"Size": 1400, "Bedrooms": 3, "Bathrooms": 2, "Age": 10}
    regression_data.append(DataRow(house1, value=200000.0))
    
    house2 = {"Size": 1600, "Bedrooms": 3, "Bathrooms": 2.5, "Age": 5}
    regression_data.append(DataRow(house2, value=250000.0))
    
    house3 = {"Size": 2000, "Bedrooms": 4, "Bathrooms": 3, "Age": 2}
    regression_data.append(DataRow(house3, value=350000.0))
    
    house4 = {"Size": 1200, "Bedrooms": 2, "Bathrooms": 1, "Age": 20}
    regression_data.append(DataRow(house4, value=150000.0))
    
    # 训练回归决策树
    regressor = DecisionTree(max_depth=5, min_samples_split=2, is_regression=True)
    regressor.fit(regression_data, regression_attrs)
    
    # 打印回归树
    print("\n===== 回归决策树 ======")
    regressor.print_tree()
    
    # 测试预测
    test_house = {"Size": 1800, "Bedrooms": 3, "Bathrooms": 2, "Age": 8}
    price_prediction = regressor.predict(test_house)
    print(f"\n测试房屋价格预测: ${price_prediction:.2f}")
    
    # 计算均方误差
    mse = regressor.calculate_mse(regression_data)
    print(f"均方误差: {mse:.2f}")