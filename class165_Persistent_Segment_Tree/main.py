#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
持久化线段树（主席树）解法主入口
包含所有题目的测试和示例
"""

import sys
from persistent_segment_tree_solutions import *


def test_mkthnum():
    """测试SPOJ MKTHNUM题目解法"""
    print("=== 测试 SPOJ MKTHNUM - K-th Number ===")
    
    # 测试用例1
    nums = [1, 5, 2, 6, 3, 7, 4]
    queries = [(2, 5, 3), (4, 4, 1), (1, 7, 3)]
    
    solver = MKTHNUM()
    results = solver.solve(nums, queries)
    
    print("输入数组:", nums)
    print("查询结果:")
    for (l, r, k), res in zip(queries, results):
        print(f"区间 [{l}, {r}] 的第 {k} 小元素是: {res}")
    print()


def test_cot():
    """测试SPOJ COT题目解法"""
    print("=== 测试 SPOJ COT - Count on a Tree ===")
    
    # 测试用例1
    n = 5
    m = 3
    values = [1, 2, 3, 4, 5]
    edges = [(1, 2), (1, 3), (2, 4), (2, 5)]
    queries = [(1, 4, 2), (2, 5, 1), (3, 4, 3)]
    
    solver = COT()
    results = solver.solve(n, m, values, edges, queries)
    
    print("树节点值:", values)
    print("查询结果:")
    for (u, v, k), res in zip(queries, results):
        print(f"节点 {u} 到 {v} 路径上的第 {k} 小元素是: {res}")
    print()


def test_count_intervals():
    """测试LeetCode 2276题目解法"""
    print("=== 测试 LeetCode 2276 - Count Integers in Intervals ===")
    
    intervals = CountIntervals()
    intervals.add(2, 3)
    print("添加区间 [2,3] 后的计数:", intervals.count())  # 应输出 2
    
    intervals.add(7, 10)
    print("添加区间 [7,10] 后的计数:", intervals.count())  # 应输出 6
    
    intervals.add(5, 8)
    print("添加区间 [5,8] 后的计数:", intervals.count())  # 应输出 8
    print()


def test_smallest_missing_genetic_value():
    """测试LeetCode 1970题目解法"""
    print("=== 测试 LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree ===")
    
    # 测试用例1
    parents = [-1, 0, 0, 2]
    nums = [1, 2, 3, 4]
    solver = SmallestMissingGeneticValue()
    result = solver.solve(parents, nums)
    print(f"父数组: {parents}")
    print(f"基因值数组: {nums}")
    print(f"结果: {result}")  # 应输出 [5, 1, 1, 1]
    
    # 测试用例2
    parents = [-1, 0, 0, 2]
    nums = [5, 2, 3, 4]
    result = solver.solve(parents, nums)
    print(f"\n父数组: {parents}")
    print(f"基因值数组: {nums}")
    print(f"结果: {result}")  # 应输出 [1, 1, 1, 1]
    print()


def test_dquery():
    """测试SPOJ DQUERY题目解法"""
    print("=== 测试 SPOJ DQUERY - D-query ===")
    
    # 测试用例1
    nums = [1, 1, 2, 1, 3]
    queries = [(1, 5), (2, 4), (1, 3)]
    solver = DQUERY()
    results = solver.solve(nums, queries)
    
    print("输入数组:", nums)
    print("查询结果:")
    for (l, r), res in zip(queries, results):
        print(f"区间 [{l}, {r}] 内不同元素的个数: {res}")
    print()


def test_first_occurrence():
    """测试第一次出现位置序列查询"""
    print("=== 测试 第一次出现位置序列查询 ===")
    
    # 测试用例1
    nums = [1, 2, 3, 2, 5]
    queries = [(1, 5), (2, 4), (1, 3)]
    solver = FirstOccurrence()
    results = solver.solve(nums, queries)
    
    print("输入数组:", nums)
    print("查询结果:")
    for (l, r), res in zip(queries, results):
        print(f"区间 [{l}, {r}] 内元素第一次出现的位置: {res}")
    print()


def test_range_mex():
    """测试区间最小缺失自然数查询"""
    print("=== 测试 区间最小缺失自然数查询 ===")
    
    # 测试用例1
    nums = [0, 1, 2, 3]
    queries = [(0, 3), (1, 2), (0, 1)]
    solver = RangeMex()
    results = solver.solve(nums, queries)
    
    print("输入数组:", nums)
    print("查询结果:")
    for (l, r), res in zip(queries, results):
        print(f"区间 [{l}, {r}] 内最小缺失自然数: {res}")
    print()


def run_all_tests():
    """运行所有测试"""
    print("=" * 50)
    print("持久化线段树（主席树）所有题目测试")
    print("=" * 50)
    
    try:
        test_mkthnum()
        test_cot()
        test_count_intervals()
        test_smallest_missing_genetic_value()
        test_dquery()
        test_first_occurrence()
        test_range_mex()
        
        print("=" * 50)
        print("所有测试运行完成！")
        print("=" * 50)
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        import traceback
        traceback.print_exc()


def interactive_mode():
    """交互式模式，让用户选择要测试的题目"""
    menu = {
        '1': ('SPOJ MKTHNUM - K-th Number', test_mkthnum),
        '2': ('SPOJ COT - Count on a Tree', test_cot),
        '3': ('LeetCode 2276 - Count Integers in Intervals', test_count_intervals),
        '4': ('LeetCode 1970 - Smallest Missing Genetic Value in Each Subtree', test_smallest_missing_genetic_value),
        '5': ('SPOJ DQUERY - D-query', test_dquery),
        '6': ('第一次出现位置序列查询', test_first_occurrence),
        '7': ('区间最小缺失自然数查询', test_range_mex),
        '0': ('运行所有测试', run_all_tests),
        'q': ('退出', lambda: sys.exit(0))
    }
    
    while True:
        print("\n持久化线段树题目测试菜单:")
        print("=" * 60)
        for key, (desc, _) in sorted(menu.items()):
            print(f"{key}. {desc}")
        print("=" * 60)
        
        choice = input("请选择要测试的题目 (输入编号): ")
        
        if choice in menu:
            print()
            menu[choice][1]()
        else:
            print("无效的选择，请重新输入！")


if __name__ == "__main__":
    print("欢迎使用持久化线段树（主席树）解法测试工具")
    
    # 检查是否有命令行参数
    if len(sys.argv) > 1 and sys.argv[1] == "--all":
        run_all_tests()
    else:
        interactive_mode()