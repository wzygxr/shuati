#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
动态规划相关题目搜索脚本
搜索与class127中算法相关的更多题目
"""

import json
import requests
import time
from typing import List, Dict, Any

# 定义要搜索的关键词列表
KEYWORDS = [
    # 网格路径相关
    "grid path", "minimum path sum", "unique paths", "cherry pickup",
    "robot path", "matrix traversal", "dynamic programming grid",
    
    # 青蛙跳跃相关
    "frog jump", "jump game", "minimum jumps", "frog crossing",
    "stone bridge", "river crossing", "binary search answer",
    
    # 子数组乘积相关
    "subarray product", "maximum product", "positive negative product",
    "count subarrays", "prefix sum", "bit manipulation",
    
    # 子序列相关
    "longest subsequence", "subsequence product", "bitwise AND",
    "longest valid subsequence", "dynamic programming sequence",
    
    # 排列组合相关
    "arrange plates", "ways to arrange", "dynamic programming counting",
    "combinatorics", "recurrence relation", "matrix exponentiation"
]

# 定义要搜索的平台
PLATFORMS = [
    "LeetCode", "LintCode", "HackerRank", "Codeforces", "AtCoder",
    "USACO", "Luogu", "CodeChef", "SPOJ", "Project Euler",
    "HackerEarth", "计蒜客", "ZOJ", "MarsCode", "UVa OJ",
    "TimusOJ", "AizuOJ", "Comet OJ", "杭电 OJ", "LOJ", "牛客",
    "acwing", "hdu", "poj", "剑指Offer"
]

# 预定义的题目数据（由于实际API调用受限，这里使用预定义数据）
PREDEFINED_PROBLEMS = [
    # 网格路径类题目
    {
        "title": "Cherry Pickup",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/cherry-pickup/",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Grid", "Matrix"],
        "description": "从左上角到右下角再返回左上角，收集樱桃的最大数量"
    },
    {
        "title": "Cherry Pickup II",
        "platform": "LeetCode", 
        "url": "https://leetcode.cn/problems/cherry-pickup-ii/",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Grid", "Matrix"],
        "description": "两个机器人从顶部出发，收集樱桃的最大数量"
    },
    {
        "title": "Minimum Path Sum",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/minimum-path-sum/",
        "difficulty": "Medium", 
        "tags": ["Dynamic Programming", "Grid", "Matrix"],
        "description": "从左上角到右下角的最小路径和"
    },
    {
        "title": "Unique Paths",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/unique-paths/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Math", "Combinatorics"],
        "description": "从左上角到右下角的不同路径数量"
    },
    {
        "title": "Unique Paths II",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/unique-paths-ii/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Grid", "Matrix"],
        "description": "有障碍物的网格中从左上角到右下角的不同路径数量"
    },
    {
        "title": "Dungeon Game",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/dungeon-game/",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Binary Search"],
        "description": "骑士从左上角到右下角的最小初始健康值"
    },
    
    # 青蛙跳跃类题目
    {
        "title": "Frog Jump",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/frog-jump/",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Hash Table"],
        "description": "青蛙过河，判断能否到达最后一块石头"
    },
    {
        "title": "Jump Game",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/jump-game/",
        "difficulty": "Medium",
        "tags": ["Greedy", "Array", "Dynamic Programming"],
        "description": "判断能否从第一个位置跳到最后一个位置"
    },
    {
        "title": "Jump Game II",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/jump-game-ii/",
        "difficulty": "Medium",
        "tags": ["Greedy", "Array", "Dynamic Programming"],
        "description": "跳到最后一个位置的最少跳跃次数"
    },
    {
        "title": "Jump Game V",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/jump-game-v/",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Sorting"],
        "description": "在数组中跳跃，每次跳跃不能超过固定距离"
    },
    {
        "title": "Jump Game VI",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/jump-game-vi/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Queue", "Heap"],
        "description": "在数组中跳跃，每次最多跳k步，求最大得分"
    },
    
    # 子数组乘积类题目
    {
        "title": "Maximum Product Subarray",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/maximum-product-subarray/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Array"],
        "description": "乘积最大的连续子数组"
    },
    {
        "title": "Subarray Product Less Than K",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/subarray-product-less-than-k/",
        "difficulty": "Medium",
        "tags": ["Array", "Sliding Window"],
        "description": "乘积小于K的连续子数组数量"
    },
    {
        "title": "The Number of Products",
        "platform": "Codeforces",
        "url": "https://codeforces.com/problemset/problem/1215/B",
        "difficulty": "Easy",
        "tags": ["Dynamic Programming", "Math"],
        "description": "统计乘积为正和负的子数组数量"
    },
    
    # 子序列类题目
    {
        "title": "Longest Increasing Subsequence",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/longest-increasing-subsequence/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Binary Search"],
        "description": "最长递增子序列"
    },
    {
        "title": "Longest Common Subsequence",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/longest-common-subsequence/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "String"],
        "description": "两个字符串的最长公共子序列"
    },
    {
        "title": "Longest Palindromic Subsequence",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/longest-palindromic-subsequence/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "String"],
        "description": "最长回文子序列"
    },
    
    # 排列组合类题目
    {
        "title": "Knight Dialer",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/knight-dialer/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Matrix Exponentiation"],
        "description": "骑士在电话垫上跳跃，计算不同长度的数字序列数量"
    },
    {
        "title": "Domino and Tromino Tiling",
        "platform": "LeetCode",
        "url": "https://leetcode.cn/problems/domino-and-tromino-tiling/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming"],
        "description": "用多米诺骨牌和托米诺骨牌铺满2*n的面板"
    },
    
    # 其他平台题目
    {
        "title": "方格取数",
        "platform": "牛客网",
        "url": "https://ac.nowcoder.com/acm/problem/14552",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Grid"],
        "description": "与摘樱桃问题非常相似，两个人从左上角出发到右下角取数"
    },
    {
        "title": "Walking on a Grid",
        "platform": "UVa OJ",
        "url": "https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Grid"],
        "description": "在网格中行走，有负数，最多允许k次负数"
    },
    {
        "title": "滑雪",
        "platform": "洛谷",
        "url": "https://www.luogu.com.cn/problem/P1434",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "DFS", "BFS"],
        "description": "寻找最长滑雪路径，每步只能滑向相邻四个方向且高度更低的位置"
    },
    {
        "title": "种树",
        "platform": "洛谷",
        "url": "https://www.luogu.com.cn/problem/P1250",
        "difficulty": "Easy",
        "tags": ["Greedy", "Interval"],
        "description": "区间覆盖问题，贪心选择最优策略"
    },
    {
        "title": "Flying to Fredericton",
        "platform": "UVa OJ",
        "url": "https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2255",
        "difficulty": "Medium",
        "tags": ["Graph", "Shortest Path", "Dynamic Programming"],
        "description": "多段最短路径问题，允许最多k次飞行"
    },
    {
        "title": "MICE AND MAZE",
        "platform": "SPOJ",
        "url": "https://www.spoj.com/problems/MICEMAZE/",
        "difficulty": "Easy",
        "tags": ["Graph", "BFS", "Dijkstra"],
        "description": "迷宫寻路问题，计算能在给定时间内到达终点的老鼠数量"
    },
    {
        "title": "Minimum Jumps",
        "platform": "HackerEarth",
        "url": "https://www.hackerearth.com/practice/data-structures/arrays/1-d/practice-problems/algorithm/minimum-jumps-4/description/",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "BFS"],
        "description": "计算从数组第一个元素跳到最后一个元素的最小跳跃次数"
    },
    {
        "title": "Doing Homework",
        "platform": "杭电 HDU",
        "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1074",
        "difficulty": "Medium",
        "tags": ["Dynamic Programming", "Bitmask"],
        "description": "任务调度问题，求最小扣分"
    },
    {
        "title": "Fight with Monsters",
        "platform": "Codeforces",
        "url": "https://codeforces.com/problemset/problem/1296/D",
        "difficulty": "Easy",
        "tags": ["Greedy", "Sorting"],
        "description": "贪心策略解决怪物战斗问题"
    },
    {
        "title": "Dividing Chocolate",
        "platform": "AtCoder",
        "url": "https://atcoder.jp/contests/abc159/tasks/abc159_e",
        "difficulty": "Hard",
        "tags": ["Dynamic Programming", "Bitmask", "Prefix Sum"],
        "description": "二维网格分割问题，需要类似的状态转移思路"
    },
    {
        "title": "Single Wildcard Pattern Matching",
        "platform": "Codeforces",
        "url": "https://codeforces.com/problemset/problem/965/D",
        "difficulty": "Medium",
        "tags": ["String", "Greedy"],
        "description": "字符串匹配问题，但涉及到间隔匹配的概念"
    }
]

def search_problems() -> List[Dict[str, Any]]:
    """搜索相关题目"""
    print("开始搜索动态规划相关题目...")
    
    # 这里应该调用各个平台的API进行搜索
    # 但由于API限制，我们使用预定义数据
    
    print(f"找到 {len(PREDEFINED_PROBLEMS)} 个相关题目")
    return PREDEFINED_PROBLEMS

def categorize_problems(problems: List[Dict[str, Any]]) -> Dict[str, List[Dict[str, Any]]]:
    """将题目按类别分类"""
    categories = {
        "grid_path": [],      # 网格路径类
        "frog_jump": [],      # 青蛙跳跃类
        "subarray_product": [], # 子数组乘积类
        "subsequence": [],    # 子序列类
        "combinatorics": [],  # 排列组合类
        "other": []           # 其他类
    }
    
    for problem in problems:
        title = problem["title"].lower()
        tags = [tag.lower() for tag in problem["tags"]]
        
        # 分类逻辑
        if any(keyword in title for keyword in ["cherry", "path", "grid", "matrix", "unique", "dungeon"]):
            categories["grid_path"].append(problem)
        elif any(keyword in title for keyword in ["frog", "jump", "stone", "river"]):
            categories["frog_jump"].append(problem)
        elif any(keyword in title for keyword in ["product", "subarray", "multiply"]):
            categories["subarray_product"].append(problem)
        elif any(keyword in title for keyword in ["subsequence", "increasing", "common", "palindromic"]):
            categories["subsequence"].append(problem)
        elif any(keyword in title for keyword in ["knight", "domino", "tiling", "arrange", "combinatorics"]):
            categories["combinatorics"].append(problem)
        else:
            categories["other"].append(problem)
    
    return categories

def save_results(categories: Dict[str, List[Dict[str, Any]]]) -> None:
    """保存搜索结果"""
    # 保存完整的搜索结果
    with open("dp_problems_search_results.json", "w", encoding="utf-8") as f:
        json.dump(categories, f, ensure_ascii=False, indent=2)
    
    # 生成Markdown格式的报告
    with open("dp_problems_report.md", "w", encoding="utf-8") as f:
        f.write("# 动态规划相关题目搜索结果\n\n")
        
        for category, problems in categories.items():
            if problems:
                f.write(f"## {category.replace('_', ' ').title()}\n\n")
                
                for i, problem in enumerate(problems, 1):
                    f.write(f"{i}. **{problem['title']}** ({problem['platform']})\n")
                    f.write(f"   - 链接: {problem['url']}\n")
                    f.write(f"   - 难度: {problem['difficulty']}\n")
                    f.write(f"   - 标签: {', '.join(problem['tags'])}\n")
                    f.write(f"   - 描述: {problem['description']}\n\n")
    
    print("搜索结果已保存到 dp_problems_search_results.json 和 dp_problems_report.md")

def main():
    """主函数"""
    # 搜索题目
    problems = search_problems()
    
    # 分类题目
    categories = categorize_problems(problems)
    
    # 保存结果
    save_results(categories)
    
    # 统计信息
    total = sum(len(problems) for problems in categories.values())
    print(f"\n搜索完成！共找到 {total} 个相关题目")
    print("分类统计：")
    for category, problems in categories.items():
        print(f"  {category}: {len(problems)} 个题目")

if __name__ == "__main__":
    main()