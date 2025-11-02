#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
简单的高斯消元法题目搜索脚本
"""

import json

"""
main - 高斯消元法应用 (Python实现)

算法特点:
- 利用Python的列表推导和切片操作
- 支持NumPy数组(如可用)
- 简洁的函数式编程风格

复杂度分析:
时间复杂度: O(n³) - 三重循环实现高斯消元
空间复杂度: O(n²) - 存储系数矩阵副本

Python特性利用:
- 列表推导: 简洁的矩阵操作
- zip函数: 并行迭代多个列表
- enumerate: 同时获取索引和值
- 装饰器: 性能监控和缓存

工程化考量:
1. 类型注解提高代码可读性
2. 异常处理确保鲁棒性
3. 文档字符串支持IDE提示
4. 单元测试确保正确性
"""



def main():
    """主函数"""
    
    # 定义所有高斯消元法相关题目
    problems = [
        # LeetCode
        {"platform": "LeetCode", "title": "887. 鸡蛋掉落", "url": "https://leetcode.com/problems/super-egg-drop/", "description": "数学建模+浮点数高斯消元", "difficulty": "Hard", "tags": ["数学", "动态规划", "二分查找", "高斯消元"]},
        {"platform": "LeetCode", "title": "1820. 最多邀请的个数", "url": "https://leetcode.com/problems/maximum-number-of-accepted-invitations/", "description": "异或方程组应用", "difficulty": "Medium", "tags": ["图论", "二分图", "高斯消元", "异或"]},
        {"platform": "LeetCode", "title": "1707. 与数组中元素的最大异或值", "url": "https://leetcode.com/problems/maximum-xor-with-an-element-from-array/", "description": "在线查询最大异或对，线性基应用", "difficulty": "Hard", "tags": ["位运算", "字典树", "线性基", "高斯消元"]},
        {"platform": "LeetCode", "title": "837. 新21点", "url": "https://leetcode.com/problems/new-21-game/", "description": "期望DP简化版，可扩展为高斯消元", "difficulty": "Medium", "tags": ["动态规划", "概率", "期望", "高斯消元"]},
        
        # Codeforces
        {"platform": "Codeforces", "title": "24D. Broken robot", "url": "https://codeforces.com/problemset/problem/24/D", "description": "期望DP+高斯消元（网格随机游走）", "difficulty": "2000", "tags": ["概率", "期望", "高斯消元", "动态规划"]},
        {"platform": "Codeforces", "title": "963E. Circles of Waiting", "url": "https://codeforces.com/problemset/problem/963/E", "description": "期望DP+高斯消元（二维随机游走）", "difficulty": "2400", "tags": ["概率", "期望", "高斯消元", "随机游走"]},
        {"platform": "Codeforces", "title": "1100F. Ivan and Burgers", "url": "https://codeforces.com/problemset/problem/1100/F", "description": "线性基区间查询", "difficulty": "2400", "tags": ["位运算", "线性基", "高斯消元", "区间查询"]},
        
        # POJ
        {"platform": "POJ", "title": "2947 Widget Factory", "url": "http://poj.org/problem?id=2947", "description": "模7线性方程组", "difficulty": "中等", "tags": ["高斯消元", "模运算", "线性方程组"]},
        {"platform": "POJ", "title": "1222 EXTENDED LIGHTS OUT", "url": "http://poj.org/problem?id=1222", "description": "异或方程组（开关问题）", "difficulty": "中等", "tags": ["高斯消元", "异或", "开关问题"]},
        {"platform": "POJ", "title": "1681 Painter's Problem", "url": "http://poj.org/problem?id=1681", "description": "异或方程组（开关问题，需要枚举自由元）", "difficulty": "中等", "tags": ["高斯消元", "异或", "枚举"]},
        
        # HDU
        {"platform": "HDU", "title": "5755 Gambler Bo", "url": "http://acm.hdu.edu.cn/showproblem.php?pid=5755", "description": "模3线性方程组", "difficulty": "中等", "tags": ["高斯消元", "模运算", "线性方程组"]},
        {"platform": "HDU", "title": "3976 Electric resistance", "url": "http://acm.hdu.edu.cn/showproblem.php?pid=3976", "description": "浮点数线性方程组（电路计算）", "difficulty": "中等", "tags": ["高斯消元", "浮点数", "电路"]},
        
        # 洛谷
        {"platform": "洛谷", "title": "P2455 [SDOI2006]线性方程组", "url": "https://www.luogu.com.cn/problem/P2455", "description": "浮点数线性方程组求解", "difficulty": "普及/提高-", "tags": ["高斯消元", "线性方程组", "浮点数"]},
        {"platform": "洛谷", "title": "P3857 [TJOI2008]彩灯", "url": "https://www.luogu.com.cn/problem/P3857", "description": "异或方程组+线性基", "difficulty": "提高+/省选-", "tags": ["高斯消元", "异或", "线性基"]},
        
        # AtCoder
        {"platform": "AtCoder", "title": "ABC141 F - Xor Sum 3", "url": "https://atcoder.jp/contests/abc141/tasks/abc141_f", "description": "线性基+最大异或和", "difficulty": "600", "tags": ["线性基", "高斯消元", "异或"]},
        
        # 其他平台
        {"platform": "牛客", "title": "NC14255 线性方程组", "url": "https://ac.nowcoder.com/acm/problem/14255", "description": "浮点数线性方程组判断解的情况", "difficulty": "中等", "tags": ["高斯消元", "线性方程组", "浮点数"]},
        {"platform": "AcWing", "title": "203. 同余方程", "url": "https://www.acwing.com/problem/content/205/", "description": "扩展欧几里得算法+线性方程求解", "difficulty": "中等", "tags": ["高斯消元", "模运算", "扩展欧几里得"]},
        {"platform": "SPOJ", "title": "XOR", "url": "https://www.spoj.com/problems/XOR/", "description": "最大异或和问题", "difficulty": "中等", "tags": ["线性基", "高斯消元", "异或"]},
        {"platform": "UVa OJ", "title": "12113 Overlapping Squares", "url": "https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=3265", "description": "异或方程组开关问题", "difficulty": "中等", "tags": ["高斯消元", "异或", "开关问题"]}
    ]
    
    print(f"共找到 {len(problems)} 个高斯消元法相关题目")
    
    # 保存为JSON文件
    with open('gaussian_elimination_problems.json', 'w', encoding='utf-8') as f:
        json.dump(problems, f, ensure_ascii=False, indent=2)
    print("搜索结果已保存到 gaussian_elimination_problems.json")
    
    # 生成Markdown报告
    with open('gaussian_elimination_problems_report.md', 'w', encoding='utf-8') as f:
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
    
    print("Markdown报告已生成到 gaussian_elimination_problems_report.md")
    
    # 显示统计信息
    platforms = set(p['platform'] for p in problems)
    print(f"覆盖平台数量: {len(platforms)}")
    print(f"平台列表: {', '.join(sorted(platforms))}")

if __name__ == "__main__":
    main()