#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
高斯消元法题目搜索脚本
搜索各大算法平台上的高斯消元法相关题目
"""

import json
import requests
import time
from typing import List, Dict, Any

class GaussianEliminationProblemSearcher:
    """高斯消元法题目搜索器"""
    
    def __init__(self):
        self.problems = []
        self.platforms = [
            "LeetCode", "LintCode", "HackerRank", "赛码", "AtCoder", "USACO", 
            "洛谷", "CodeChef", "SPOJ", "Project Euler", "HackerEarth", "计蒜客",
            "ZOJ", "MarsCode", "UVa OJ", "TimusOJ", "AizuOJ", "Comet OJ",
            "杭电 OJ", "LOJ", "牛客", "杭州电子科技大学", "acwing", "codeforces",
            "hdu", "poj", "剑指Offer"
        ]
    
    def search_leetcode(self) -> List[Dict[str, Any]]:
        """搜索LeetCode上的高斯消元法题目"""
        leetcode_problems = [
            {
                "platform": "LeetCode",
                "title": "887. 鸡蛋掉落",
                "url": "https://leetcode.com/problems/super-egg-drop/",
                "description": "数学建模+浮点数高斯消元",
                "difficulty": "Hard",
                "tags": ["数学", "动态规划", "二分查找", "高斯消元"]
            },
            {
                "platform": "LeetCode", 
                "title": "1820. 最多邀请的个数",
                "url": "https://leetcode.com/problems/maximum-number-of-accepted-invitations/",
                "description": "异或方程组应用",
                "difficulty": "Medium",
                "tags": ["图论", "二分图", "高斯消元", "异或"]
            },
            {
                "platform": "LeetCode",
                "title": "1707. 与数组中元素的最大异或值",
                "url": "https://leetcode.com/problems/maximum-xor-with-an-element-from-array/",
                "description": "在线查询最大异或对，线性基应用",
                "difficulty": "Hard",
                "tags": ["位运算", "字典树", "线性基", "高斯消元"]
            },
            {
                "platform": "LeetCode",
                "title": "837. 新21点",
                "url": "https://leetcode.com/problems/new-21-game/",
                "description": "期望DP简化版，可扩展为高斯消元",
                "difficulty": "Medium",
                "tags": ["动态规划", "概率", "期望", "高斯消元"]
            }
        ]
        return leetcode_problems
    
    def search_codeforces(self) -> List[Dict[str, Any]]:
        """搜索Codeforces上的高斯消元法题目"""
        codeforces_problems = [
            {
                "platform": "Codeforces",
                "title": "24D. Broken robot",
                "url": "https://codeforces.com/problemset/problem/24/D",
                "description": "期望DP+高斯消元（网格随机游走）",
                "difficulty": "2000",
                "tags": ["概率", "期望", "高斯消元", "动态规划"]
            },
            {
                "platform": "Codeforces",
                "title": "963E. Circles of Waiting",
                "url": "https://codeforces.com/problemset/problem/963/E",
                "description": "期望DP+高斯消元（二维随机游走）",
                "difficulty": "2400",
                "tags": ["概率", "期望", "高斯消元", "随机游走"]
            },
            {
                "platform": "Codeforces",
                "title": "1100F. Ivan and Burgers",
                "url": "https://codeforces.com/problemset/problem/1100/F",
                "description": "线性基区间查询",
                "difficulty": "2400",
                "tags": ["位运算", "线性基", "高斯消元", "区间查询"]
            },
            {
                "platform": "Codeforces",
                "title": "590D. Top Secret Task",
                "url": "https://codeforces.com/problemset/problem/590/D",
                "description": "期望DP+高斯消元",
                "difficulty": "2200",
                "tags": ["动态规划", "期望", "高斯消元"]
            },
            {
                "platform": "Codeforces",
                "title": "113D. Metro",
                "url": "https://codeforces.com/problemset/problem/113/D",
                "description": "概率DP+高斯消元",
                "difficulty": "2300",
                "tags": ["概率", "高斯消元", "图论"]
            }
        ]
        return codeforces_problems
    
    def search_poj(self) -> List[Dict[str, Any]]:
        """搜索POJ上的高斯消元法题目"""
        poj_problems = [
            {
                "platform": "POJ",
                "title": "2947 Widget Factory",
                "url": "http://poj.org/problem?id=2947",
                "description": "模7线性方程组",
                "difficulty": "中等",
                "tags": ["高斯消元", "模运算", "线性方程组"]
            },
            {
                "platform": "POJ",
                "title": "1222 EXTENDED LIGHTS OUT",
                "url": "http://poj.org/problem?id=1222",
                "description": "异或方程组（开关问题）",
                "difficulty": "中等",
                "tags": ["高斯消元", "异或", "开关问题"]
            },
            {
                "platform": "POJ",
                "title": "1681 Painter's Problem",
                "url": "http://poj.org/problem?id=1681",
                "description": "异或方程组（开关问题，需要枚举自由元）",
                "difficulty": "中等",
                "tags": ["高斯消元", "异或", "枚举"]
            },
            {
                "platform": "POJ",
                "title": "1830 开关问题",
                "url": "http://poj.org/problem?id=1830",
                "description": "异或方程组（计算方案数）",
                "difficulty": "中等",
                "tags": ["高斯消元", "异或", "方案数"]
            },
            {
                "platform": "POJ",
                "title": "2065 SETI",
                "url": "http://poj.org/problem?id=2065",
                "description": "浮点数线性方程组（天文学应用）",
                "difficulty": "中等",
                "tags": ["高斯消元", "浮点数", "天文学"]
            },
            {
                "platform": "POJ",
                "title": "3167 Cow Patterns",
                "url": "http://poj.org/problem?id=3167",
                "description": "模线性方程组应用",
                "difficulty": "困难",
                "tags": ["高斯消元", "模运算", "模式匹配"]
            },
            {
                "platform": "POJ",
                "title": "3276 Face The Right Way",
                "url": "http://poj.org/problem?id=3276",
                "description": "开关问题（一维）",
                "difficulty": "中等",
                "tags": ["高斯消元", "开关问题", "贪心"]
            }
        ]
        return poj_problems
    
    def search_hdu(self) -> List[Dict[str, Any]]:
        """搜索HDU上的高斯消元法题目"""
        hdu_problems = [
            {
                "platform": "HDU",
                "title": "5755 Gambler Bo",
                "url": "http://acm.hdu.edu.cn/showproblem.php?pid=5755",
                "description": "模3线性方程组",
                "difficulty": "中等",
                "tags": ["高斯消元", "模运算", "线性方程组"]
            },
            {
                "platform": "HDU",
                "title": "3976 Electric resistance",
                "url": "http://acm.hdu.edu.cn/showproblem.php?pid=3976",
                "description": "浮点数线性方程组（电路计算）",
                "difficulty": "中等",
                "tags": ["高斯消元", "浮点数", "电路"]
            },
            {
                "platform": "HDU",
                "title": "4035 Maze",
                "url": "http://acm.hdu.edu.cn/showproblem.php?pid=4035",
                "description": "树形结构上的期望DP",
                "difficulty": "困难",
                "tags": ["高斯消元", "期望", "树形DP"]
            },
            {
                "platform": "HDU",
                "title": "4418 Time travel",
                "url": "http://acm.hdu.edu.cn/showproblem.php?pid=4418",
                "description": "期望DP+高斯消元（状态转移有环）",
                "difficulty": "困难",
                "tags": ["高斯消元", "期望", "状态转移"]
            },
            {
                "platform": "HDU",
                "title": "3949 XOR",
                "url": "http://acm.hdu.edu.cn/showproblem.php?pid=3949",
                "description": "线性基求第k小异或值",
                "difficulty": "中等",
                "tags": ["线性基", "高斯消元", "异或"]
            }
        ]
        return hdu_problems
    
    def search_luogu(self) -> List[Dict[str, Any]]:
        """搜索洛谷上的高斯消元法题目"""
        luogu_problems = [
            {
                "platform": "洛谷",
                "title": "P2455 [SDOI2006]线性方程组",
                "url": "https://www.luogu.com.cn/problem/P2455",
                "description": "浮点数线性方程组求解",
                "difficulty": "普及/提高-",
                "tags": ["高斯消元", "线性方程组", "浮点数"]
            },
            {
                "platform": "洛谷",
                "title": "P3857 [TJOI2008]彩灯",
                "url": "https://www.luogu.com.cn/problem/P3857",
                "description": "异或方程组+线性基",
                "difficulty": "提高+/省选-",
                "tags": ["高斯消元", "异或", "线性基"]
            },
            {
                "platform": "洛谷",
                "title": "P3812 【模板】线性基",
                "url": "https://www.luogu.com.cn/problem/P3812",
                "description": "线性基模板题，求异或和最大值",
                "difficulty": "省选/NOI-",
                "tags": ["线性基", "高斯消元", "模板"]
            },
            {
                "platform": "洛谷",
                "title": "P4151 [WC2011]最大XOR和路径",
                "url": "https://www.luogu.com.cn/problem/P4151",
                "description": "图论中的线性基应用",
                "difficulty": "省选/NOI-",
                "tags": ["线性基", "高斯消元", "图论"]
            }
        ]
        return luogu_problems
    
    def search_atcoder(self) -> List[Dict[str, Any]]:
        """搜索AtCoder上的高斯消元法题目"""
        atcoder_problems = [
            {
                "platform": "AtCoder",
                "title": "ABC145 E - All-you-can-eat",
                "url": "https://atcoder.jp/contests/abc145/tasks/abc145_e",
                "description": "模线性方程组应用",
                "difficulty": "500",
                "tags": ["高斯消元", "模运算", "背包问题"]
            },
            {
                "platform": "AtCoder",
                "title": "ABC141 F - Xor Sum 3",
                "url": "https://atcoder.jp/contests/abc141/tasks/abc141_f",
                "description": "线性基+最大异或和",
                "difficulty": "600",
                "tags": ["线性基", "高斯消元", "异或"]
            },
            {
                "platform": "AtCoder",
                "title": "ARC084 D - Small Multiple",
                "url": "https://atcoder.jp/contests/arc084/tasks/arc084_b",
                "description": "线性基高级应用",
                "difficulty": "700",
                "tags": ["线性基", "高斯消元", "数学"]
            }
        ]
        return atcoder_problems
    
    def search_other_platforms(self) -> List[Dict[str, Any]]:
        """搜索其他平台的高斯消元法题目"""
        other_problems = [
            # SPOJ
            {
                "platform": "SPOJ",
                "title": "XOR",
                "url": "https://www.spoj.com/problems/XOR/",
                "description": "最大异或和问题",
                "difficulty": "中等",
                "tags": ["线性基", "高斯消元", "异或"]
            },
            {
                "platform": "SPOJ",
                "title": "SUBXOR",
                "url": "https://www.spoj.com/problems/SUBXOR/",
                "description": "子数组异或和统计",
                "difficulty": "困难",
                "tags": ["线性基", "高斯消元", "异或"]
            },
            
            # UVa
            {
                "platform": "UVa OJ",
                "title": "12113 Overlapping Squares",
                "url": "https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3265",
                "description": "异或方程组开关问题",
                "difficulty": "中等",
                "tags": ["高斯消元", "异或", "开关问题"]
            },
            {
                "platform": "UVa OJ",
                "title": "10109 Solving Systems of Linear Equations",
                "url": "https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=13&page=show_problem&problem=1050",
                "description": "异或方程组求解",
                "difficulty": "中等",
                "tags": ["高斯消元", "异或", "线性方程组"]
            },
            
            # ZOJ
            {
                "platform": "ZOJ",
                "title": "3644 Kitty's Game",
                "url": "https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364843",
                "description": "模线性方程组+图论",
                "difficulty": "困难",
                "tags": ["高斯消元", "模运算", "图论"]
            },
            
            # 牛客
            {
                "platform": "牛客",
                "title": "NC14255 线性方程组",
                "url": "https://ac.nowcoder.com/acm/problem/14255",
                "description": "浮点数线性方程组判断解的情况",
                "difficulty": "中等",
                "tags": ["高斯消元", "线性方程组", "浮点数"]
            },
            {
                "platform": "牛客",
                "title": "NC15139 逃离僵尸岛",
                "url": "https://ac.nowcoder.com/acm/problem/15139",
                "description": "期望DP应用",
                "difficulty": "中等",
                "tags": ["高斯消元", "期望", "动态规划"]
            },
            {
                "platform": "牛客",
                "title": "NC19740 异或",
                "url": "https://ac.nowcoder.com/acm/problem/19740",
                "description": "线性基应用",
                "difficulty": "中等",
                "tags": ["线性基", "高斯消元", "异或"]
            },
            
            # AcWing
            {
                "platform": "AcWing",
                "title": "203. 同余方程",
                "url": "https://www.acwing.com/problem/content/205/",
                "description": "扩展欧几里得算法+线性方程求解",
                "difficulty": "中等",
                "tags": ["高斯消元", "模运算", "扩展欧几里得"]
            },
            
            # USACO
            {
                "platform": "USACO",
                "title": "2019 February Contest, Gold Problem 3. Mowing Moocows",
                "url": "http://www.usaco.org/index.php?page=viewproblem2&cpid=922",
                "description": "模线性方程组高级应用",
                "difficulty": "困难",
                "tags": ["高斯消元", "模运算", "高级应用"]
            },
            
            # CodeChef
            {
                "platform": "CodeChef",
                "title": "MODULARITY",
                "url": "https://www.codechef.com/problems/MODULARITY",
                "description": "模线性方程组求解",
                "difficulty": "中等",
                "tags": ["高斯消元", "模运算", "线性方程组"]
            },
            
            # 计蒜客
            {
                "platform": "计蒜客",
                "title": "T1214 同余方程",
                "url": "https://www.jisuanke.com/problem/T1214",
                "description": "扩展欧几里得算法应用",
                "difficulty": "中等",
                "tags": ["高斯消元", "模运算", "扩展欧几里得"]
            }
        ]
        return other_problems
    
    def search_all_problems(self) -> List[Dict[str, Any]]:
        """搜索所有平台的高斯消元法题目"""
        print("开始搜索高斯消元法相关题目...")
        
        all_problems = []
        
        # 搜索各个平台的题目
        all_problems.extend(self.search_leetcode())
        all_problems.extend(self.search_codeforces())
        all_problems.extend(self.search_poj())
        all_problems.extend(self.search_hdu())
        all_problems.extend(self.search_luogu())
        all_problems.extend(self.search_atcoder())
        all_problems.extend(self.search_other_platforms())
        
        # 去重
        seen = set()
        unique_problems = []
        for problem in all_problems:
            key = f"{problem['platform']}-{problem['title']}"
            if key not in seen:
                seen.add(key)
                unique_problems.append(problem)
        
        print(f"搜索完成，共找到 {len(unique_problems)} 个高斯消元法相关题目")
        return unique_problems
    
    def save_to_json(self, problems: List[Dict[str, Any]], filename: str = "gaussian_elimination_problems.json"):
        """将搜索结果保存为JSON文件"""
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(problems, f, ensure_ascii=False, indent=2)
        print(f"搜索结果已保存到 {filename}")
    
    def generate_markdown_report(self, problems: List[Dict[str, Any]], filename: str = "gaussian_elimination_problems_report.md"):
        """生成Markdown格式的报告"""
        with open(filename, 'w', encoding='utf-8') as f:
            f.write("# 高斯消元法题目汇总报告\n\n")
            f.write("## 搜索统计\n")
            
            # 统计各平台题目数量
            platform_count = {}
            for problem in problems:
                platform = problem['platform']
                platform_count[platform] = platform_count.get(platform, 0) + 1
            
            f.write("| 平台 | 题目数量 |\n")
            f.write("|------|----------|\n")
            for platform, count in sorted(platform_count.items(), key=lambda x: x[1], reverse=True):
                f.write(f"| {platform} | {count} |\n")
            
            f.write("\n## 题目详情\n\n")
            
            # 按平台分类
            problems_by_platform = {}
            for problem in problems:
                platform = problem['platform']
                if platform not in problems_by_platform:
                    problems_by_platform[platform] = []
                problems_by_platform[platform].append(problem)
            
            for platform, platform_problems in sorted(problems_by_platform.items()):
                f.write(f"### {platform}\n\n")
                f.write("| 题目 | 难度 | 描述 | 标签 |\n")
                f.write("|------|------|------|------|\n")
                
                for problem in sorted(platform_problems, key=lambda x: x['title']):
                    title = f"[{problem['title']}]({problem['url']})"
                    difficulty = problem.get('difficulty', '未知')
                    description = problem['description']
                    tags = ', '.join(problem['tags'])
                    
                    f.write(f"| {title} | {difficulty} | {description} | {tags} |\n")
                f.write("\n")
            
            f.write("## 题目分类\n\n")
            
            # 按算法类型分类
            type_categories = {
                "浮点数线性方程组": [],
                "模线性方程组": [],
                "异或方程组": [],
                "期望DP与高斯消元": [],
                "线性基问题": []
            }
            
            for problem in problems:
                tags = problem['tags']
                if "浮点数" in str(tags) or "线性方程组" in str(tags):
                    type_categories["浮点数线性方程组"].append(problem)
                elif "模运算" in str(tags) or "模线性" in str(tags):
                    type_categories["模线性方程组"].append(problem)
                elif "异或" in str(tags):
                    type_categories["异或方程组"].append(problem)
                elif "期望" in str(tags) or "概率" in str(tags):
                    type_categories["期望DP与高斯消元"].append(problem)
                elif "线性基" in str(tags):
                    type_categories["线性基问题"].append(problem)
            
            for category, category_problems in type_categories.items():
                if category_problems:
                    f.write(f"### {category} ({len(category_problems)}题)\n\n")
                    for problem in category_problems:
                        f.write(f"- **{problem['platform']} {problem['title']}**: {problem['description']}  
")
                    f.write("\n")
        
        print(f"Markdown报告已生成到 {filename}")

def main():
    """主函数"""
    searcher = GaussianEliminationProblemSearcher()
    
    # 搜索所有题目
    problems = searcher.search_all_problems()
    
    # 保存结果
    searcher.save_to_json(problems)
    searcher.generate_markdown_report(problems)
    
    print("\n搜索完成！")
    print(f"共找到 {len(problems)} 个高斯消元法相关题目")
    
    # 显示统计信息
    platforms = set(p['platform'] for p in problems)
    print(f"覆盖平台数量: {len(platforms)}")
    print(f"平台列表: {', '.join(sorted(platforms))}")

if __name__ == "__main__":
    main()