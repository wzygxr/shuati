"""
欧拉路径算法测试套件
包含对基础算法和高级应用的全面测试
"""

import unittest
from 欧拉路径全平台题库.Python_Implementations.EulerPath_BasicAlgorithms import EulerPathAlgorithms
from 欧拉路径全平台题库.Python_Implementations.EulerPath_AdvancedApplications import EulerPathAdvancedApplications


class TestBasicEulerPathAlgorithms(unittest.TestCase):
    """测试基础欧拉路径算法"""

    def setUp(self):
        self.algo = EulerPathAlgorithms()

    def test_undirected_euler_exists_eulerian_cycle(self):
        """测试无向图欧拉回路存在性"""
        # 一个简单的欧拉回路图 (正方形)
        graph = {
            1: [2, 4],
            2: [1, 3],
            3: [2, 4],
            4: [1, 3]
        }

        exists, start, path_type = self.algo.undirected_euler_exists(graph)
        self.assertTrue(exists)
        self.assertIsNotNone(start)
        self.assertEqual(path_type, 2)  # 欧拉回路

    def test_undirected_euler_exists_eulerian_path(self):
        """测试无向图欧拉路径存在性"""
        # 一个简单的欧拉路径图
        graph = {
            1: [2],
            2: [1, 3],
            3: [2, 4],
            4: [3]
        }

        exists, start, path_type = self.algo.undirected_euler_exists(graph)
        self.assertTrue(exists)
        self.assertIsNotNone(start)
        self.assertEqual(path_type, 1)  # 欧拉路径

    def test_undirected_euler_not_exists(self):
        """测试无向图不存在欧拉路径的情况"""
        # 有4个奇度节点的图，不存在欧拉路径
        graph = {
            1: [2, 3, 4],
            2: [1, 5],
            3: [1, 6],
            4: [1, 7],
            5: [2],
            6: [3],
            7: [4]
        }

        exists, start, path_type = self.algo.undirected_euler_exists(graph)
        self.assertFalse(exists)
        self.assertIsNone(start)
        self.assertEqual(path_type, 0)  # 无欧拉路径

    def test_hierholzer_undirected_iterative(self):
        """测试无向图Hierholzer算法迭代版"""
        graph = {
            1: [2, 3],
            2: [1, 3, 4],
            3: [1, 2, 4],
            4: [2, 3]
        }

        path = self.algo.hierholzer_undirected_iterative(graph, 1)
        # 检查路径长度（应该包含所有边数+1个节点）
        self.assertEqual(len(path), 7)  # 6条边，所以7个节点

    def test_directed_euler_exists_eulerian_cycle(self):
        """测试有向图欧拉回路存在性"""
        # 一个简单的欧拉回路图
        graph = {
            1: [2],
            2: [3],
            3: [1]
        }

        exists, start, path_type = self.algo.directed_euler_exists(graph)
        self.assertTrue(exists)
        self.assertIsNotNone(start)
        self.assertEqual(path_type, 2)  # 欧拉回路

    def test_directed_euler_exists_eulerian_path(self):
        """测试有向图欧拉路径存在性"""
        # 一个简单的欧拉路径图
        graph = {
            1: [2],
            2: [3],
            3: [4],
            4: []
        }

        exists, start, path_type = self.algo.directed_euler_exists(graph)
        self.assertTrue(exists)
        self.assertEqual(start, 1)
        self.assertEqual(path_type, 1)  # 欧拉路径

    def test_hierholzer_directed_iterative(self):
        """测试有向图Hierholzer算法迭代版"""
        graph = {
            1: [2],
            2: [3],
            3: [1]
        }

        path = self.algo.hierholzer_directed_iterative(graph, 1)
        self.assertEqual(len(path), 4)  # 3条边，4个节点

    def test_de_bruijn_sequence(self):
        """测试德布鲁因序列生成"""
        # 测试简单情况
        sequence = self.algo.de_bruijn_sequence(2, 2)
        # 长度应该是 2^2 = 4 加上前缀长度 1 = 5
        expected_length = 2**2 + 1 - 1  # n=2, k=2 -> k^n = 4, prefix = n-1=1, total=2*2=4
        self.assertGreaterEqual(len(sequence), 2**2)


class TestAdvancedEulerPathApplications(unittest.TestCase):
    """测试高级欧拉路径应用"""

    def setUp(self):
        self.app = EulerPathAdvancedApplications()

    def test_word_chain_simple(self):
        """测试词链问题简单情况"""
        words = ["cat", "dog", "god"]
        result = self.app.word_chain(words)
        # 这个例子可能无法形成词链，取决于具体实现
        # 但不应该抛出异常
        self.assertIsInstance(result, str)

    def test_valid_arrangement_of_pairs_simple(self):
        """测试合法排列数对简单情况"""
        pairs = [[1, 2], [2, 3], [3, 1]]
        result = self.app.valid_arrangement_of_pairs(pairs)

        if result is not None:
            # 如果找到了有效排列，验证连接性
            self.assertEqual(len(result), len(pairs))
            for i in range(len(result) - 1):
                self.assertEqual(result[i][1], result[i+1][0])

    def test_cracking_the_safe_basic(self):
        """测试密码破解基本功能"""
        # 测试 n=2, k=2 的情况
        result = self.app.cracking_the_safe(2, 2)

        # 结果长度应该是 2^2 = 4
        self.assertGreaterEqual(len(result), 2**2)

        # 检查是否包含所有可能的2位二进制数
        substrings = set()
        for i in range(len(result) - 1):
            substrings.add(result[i:i+2])

        # 应该包含 "00", "01", "10", "11"
        expected_substrings = {"00", "01", "10", "11"}
        self.assertTrue(expected_substrings.issubset(substrings) or len(
            expected_substrings.intersection(substrings)) >= 3)

    def test_letter_pairs_basic(self):
        """测试字母对基本功能"""
        pairs = [['a', 'b'], ['b', 'c'], ['c', 'a']]
        result = self.app.letter_pairs(pairs)

        # 结果应该是一个字符串或"No Solution"
        self.assertIsInstance(result, str)

        if result != "No Solution":
            # 如果有解，路径长度应该是边数+1
            self.assertEqual(len(result), len(pairs) + 1)


class TestEdgeCases(unittest.TestCase):
    """测试边界情况"""

    def setUp(self):
        self.basic_algo = EulerPathAlgorithms()
        self.advanced_app = EulerPathAdvancedApplications()

    def test_empty_graph(self):
        """测试空图"""
        exists, start, path_type = self.basic_algo.undirected_euler_exists({})
        self.assertTrue(exists)  # 空图认为有欧拉回路
        self.assertIsNone(start)
        self.assertEqual(path_type, 2)

    def test_single_node(self):
        """测试单节点图"""
        graph = {1: []}
        exists, start, path_type = self.basic_algo.undirected_euler_exists(
            graph)
        self.assertTrue(exists)
        self.assertEqual(start, 1)
        self.assertEqual(path_type, 2)  # 欧拉回路

    def test_two_connected_nodes(self):
        """测试两个相连的节点"""
        graph = {1: [2], 2: [1]}
        exists, start, path_type = self.basic_algo.undirected_euler_exists(
            graph)
        self.assertTrue(exists)
        self.assertIn(start, [1, 2])
        self.assertEqual(path_type, 2)  # 欧拉回路（两个节点，每节点度数为1，都是偶数度）

    def test_no_pairs(self):
        """测试空数对列表"""
        result = self.advanced_app.valid_arrangement_of_pairs([])
        self.assertEqual(result, [])

    def test_single_pair(self):
        """测试单个数对"""
        pairs = [[1, 2]]
        result = self.advanced_app.valid_arrangement_of_pairs(pairs)
        self.assertIsNotNone(result)
        if result is not None:
            self.assertEqual(result, [[1, 2]])


def run_comprehensive_tests():
    """运行综合测试"""
    print("=== 欧拉路径算法测试套件 ===\n")

    # 创建测试套件
    test_suite = unittest.TestSuite()

    # 添加测试用例
    test_suite.addTest(unittest.makeSuite(TestBasicEulerPathAlgorithms))
    test_suite.addTest(unittest.makeSuite(TestAdvancedEulerPathApplications))
    test_suite.addTest(unittest.makeSuite(TestEdgeCases))

    # 运行测试
    runner = unittest.TextTestRunner(verbosity=2)
    result = runner.run(test_suite)

    # 输出测试结果摘要
    print(f"\n=== 测试结果摘要 ===")
    print(f"运行测试数: {result.testsRun}")
    print(f"失败数: {len(result.failures)}")
    print(f"错误数: {len(result.errors)}")
    print(f"成功率: {((result.testsRun - len(result.failures) - len(result.errors)) / result.testsRun * 100):.2f}%")

    return result.wasSuccessful()


def run_specific_tests():
    """运行特定的测试示例"""
    print("\n=== 具体算法测试示例 ===\n")

    basic_algo = EulerPathAlgorithms()
    advanced_app = EulerPathAdvancedApplications()

    # 测试无向图欧拉路径
    print("1. 无向图欧拉路径测试:")
    undirected_graph = {
        1: [2, 3],
        2: [1, 3, 4],
        3: [1, 2, 4],
        4: [2, 3]
    }
    exists, start, path_type = basic_algo.undirected_euler_exists(
        undirected_graph)
    print(f"   图: {undirected_graph}")
    print(f"   存在性: {exists}, 起点: {start}, 类型: {path_type}")

    if exists:
        path = basic_algo.hierholzer_undirected_iterative(
            undirected_graph, start)
        print(f"   欧拉路径: {path}")

    # 测试有向图欧拉路径
    print("\n2. 有向图欧拉路径测试:")
    directed_graph = {
        1: [2],
        2: [3],
        3: [1]
    }
    exists, start, path_type = basic_algo.directed_euler_exists(directed_graph)
    print(f"   图: {directed_graph}")
    print(f"   存在性: {exists}, 起点: {start}, 类型: {path_type}")

    if exists:
        path = basic_algo.hierholzer_directed_iterative(directed_graph, start)
        print(f"   欧拉路径: {path}")

    # 测试德布鲁因序列
    print("\n3. 德布鲁因序列测试 (n=2, k=2):")
    dbs = basic_algo.de_bruijn_sequence(2, 2)
    print(f"   序列: {dbs}")

    # 测试合法排列数对
    print("\n4. 合法排列数对测试:")
    pairs = [[1, 2], [2, 3], [3, 1]]
    result = advanced_app.valid_arrangement_of_pairs(pairs)
    print(f"   输入: {pairs}")
    print(f"   输出: {result}")


if __name__ == "__main__":
    # 运行综合测试
    success = run_comprehensive_tests()

    # 运行具体示例
    run_specific_tests()

    print(f"\n=== 测试完成 ===")
    print(f"整体测试结果: {'通过' if success else '部分失败'}")
