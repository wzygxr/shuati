import requests
from bs4 import BeautifulSoup
import json

# 读取已有的题目数据
with open('segment_tree_problems.json', 'r', encoding='utf-8') as f:
    problems = json.load(f)

# 添加更多题目
additional_problems = [
    # 更多LeetCode题目
    {
        "platform": "LeetCode",
        "title": "Online Majority Element In Subarray",
        "url": "https://leetcode.com/problems/online-majority-element-in-subarray/",
        "difficulty": "Hard"
    },
    {
        "platform": "LeetCode",
        "title": "Minimum Number of Increments on Subarrays to Form a Target Array",
        "url": "https://leetcode.com/problems/minimum-number-of-increments-on-subarrays-to-form-a-target-array/",
        "difficulty": "Hard"
    },
    {
        "platform": "LeetCode",
        "title": "Create Sorted Array through Instructions",
        "url": "https://leetcode.com/problems/create-sorted-array-through-instructions/",
        "difficulty": "Hard"
    },
    
    # 更多Codeforces题目
    {
        "platform": "Codeforces",
        "title": "XOR on Segment",
        "url": "https://codeforces.com/contest/242/problem/E",
        "difficulty": "Hard"
    },
    {
        "platform": "Codeforces",
        "title": "Drazil and Park",
        "url": "https://codeforces.com/contest/515/problem/E",
        "difficulty": "Hard"
    },
    {
        "platform": "Codeforces",
        "title": "Propagating tree",
        "url": "https://codeforces.com/contest/383/problem/C",
        "difficulty": "Hard"
    },
    
    # 更多HDU题目
    {
        "platform": "HDU",
        "title": "Tunnel Warfare",
        "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1540",
        "difficulty": "Medium"
    },
    {
        "platform": "HDU",
        "title": "Atlantis",
        "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1542",
        "difficulty": "Hard"
    },
    {
        "platform": "HDU",
        "title": "Picture",
        "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1828",
        "difficulty": "Hard"
    },
    {
        "platform": "HDU",
        "title": "To the moon",
        "url": "http://acm.hdu.edu.cn/showproblem.php?pid=4348",
        "difficulty": "Hard"
    },
    
    # 更多POJ题目
    {
        "platform": "POJ",
        "title": "Dynamic Rankings",
        "url": "http://poj.org/problem?id=2104",
        "difficulty": "Hard"
    },
    {
        "platform": "POJ",
        "title": "K-th Number",
        "url": "http://poj.org/problem?id=3468",
        "difficulty": "Hard"
    },
    
    # 更多SPOJ题目
    {
        "platform": "SPOJ",
        "title": "Can you answer these queries V",
        "url": "https://www.spoj.com/problems/GSS5/",
        "difficulty": "Hard"
    },
    {
        "platform": "SPOJ",
        "title": "Can you answer these queries VI",
        "url": "https://www.spoj.com/problems/GSS6/",
        "difficulty": "Hard"
    },
    {
        "platform": "SPOJ",
        "title": "Can you answer these queries VII",
        "url": "https://www.spoj.com/problems/GSS7/",
        "difficulty": "Hard"
    },
    
    # 更多Luogu题目
    {
        "platform": "Luogu",
        "title": "楼房重建",
        "url": "https://www.luogu.com.cn/problem/P4198",
        "difficulty": "Hard"
    },
    {
        "platform": "Luogu",
        "title": "粟粟的书架",
        "url": "https://www.luogu.com.cn/problem/P2468",
        "difficulty": "Hard"
    },
    {
        "platform": "Luogu",
        "title": "Dynamic Rankings",
        "url": "https://www.luogu.com.cn/problem/P2617",
        "difficulty": "Hard"
    },
    
    # 更多LintCode题目
    {
        "platform": "LintCode",
        "title": "Count of Smaller Number before itself",
        "url": "https://www.lintcode.com/problem/249/",
        "difficulty": "Medium"
    },
    {
        "platform": "LintCode",
        "title": "Segment Tree Build II",
        "url": "https://www.lintcode.com/problem/439/",
        "difficulty": "Medium"
    },
    {
        "platform": "LintCode",
        "title": "Segment Tree Query II",
        "url": "https://www.lintcode.com/problem/247/",
        "difficulty": "Medium"
    }
]

# 合并题目列表
problems.extend(additional_problems)

# 保存更新后的题目数据
with open('segment_tree_problems_extended.json', 'w', encoding='utf-8') as f:
    json.dump(problems, f, ensure_ascii=False, indent=2)

print(f"已更新题目列表，共包含 {len(problems)} 道题目")
print("结果已保存到 segment_tree_problems_extended.json 文件中")

# 目标网站URL
urls = {
    "LeetCode": "https://leetcode.com/problem-list/segment-tree/",
    "Codeforces": "https://codeforces.com/blog/entry/22616",
    "AtCoder": "https://atcoder.jp/contests/practice2/tasks",
    "HackerRank": "https://www.hackerrank.com/domains/data-structures?filters%5Bsubdomains%5D%5B%5D=segment-trees",
}

# 存储所有题目信息
problems = []

def scrape_leetcode():
    """爬取LeetCode线段树题目"""
    # 从已有的资料中提取LeetCode线段树题目
    leetcode_problems = [
        {
            "platform": "LeetCode",
            "title": "Range Sum Query - Mutable",
            "url": "https://leetcode.com/problems/range-sum-query-mutable/",
            "difficulty": "Medium"
        },
        {
            "platform": "LeetCode",
            "title": "Count of Smaller Numbers After Self",
            "url": "https://leetcode.com/problems/count-of-smaller-numbers-after-self/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "Falling Squares",
            "url": "https://leetcode.com/problems/falling-squares/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "The Skyline Problem",
            "url": "https://leetcode.com/problems/the-skyline-problem/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "Range Sum Query 2D - Mutable",
            "url": "https://leetcode.com/problems/range-sum-query-2d-mutable/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "Reverse Pairs",
            "url": "https://leetcode.com/problems/reverse-pairs/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "Count of Range Sum",
            "url": "https://leetcode.com/problems/count-of-range-sum/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "My Calendar III",
            "url": "https://leetcode.com/problems/my-calendar-iii/",
            "difficulty": "Hard"
        },
        {
            "platform": "LeetCode",
            "title": "Range Module",
            "url": "https://leetcode.com/problems/range-module/",
            "difficulty": "Hard"
        }
    ]
    
    problems.extend(leetcode_problems)
    print(f"LeetCode: 成功获取 {len(leetcode_problems)} 道题目")

def scrape_codeforces():
    """从已有的资料中提取Codeforces线段树题目"""
    codeforces_problems = [
        {
            "platform": "Codeforces",
            "title": "Xenia and Bit Operations",
            "url": "https://codeforces.com/contest/339/problem/D",
            "difficulty": "Medium"
        },
        {
            "platform": "Codeforces",
            "title": "Pashmak and Parmida's problem",
            "url": "https://codeforces.com/contest/459/problem/D",
            "difficulty": "Hard"
        },
        {
            "platform": "Codeforces",
            "title": "Circular RMQ",
            "url": "https://codeforces.com/problemset/problem/52/C",
            "difficulty": "Medium"
        },
        {
            "platform": "Codeforces",
            "title": "The Child and Sequence",
            "url": "https://codeforces.com/problemset/problem/438/D",
            "difficulty": "Hard"
        },
        {
            "platform": "Codeforces",
            "title": "Valera and Queries",
            "url": "https://codeforces.com/contest/369/problem/E",
            "difficulty": "Hard"
        },
        {
            "platform": "Codeforces",
            "title": "Lucky Array",
            "url": "https://codeforces.com/contest/121/problem/E",
            "difficulty": "Hard"
        },
        {
            "platform": "Codeforces",
            "title": "Copying Data",
            "url": "https://codeforces.com/contest/292/problem/E",
            "difficulty": "Hard"
        }
    ]
    
    problems.extend(codeforces_problems)
    print(f"Codeforces: 成功获取 {len(codeforces_problems)} 道题目")

def scrape_atcoder():
    """从已有的资料中提取AtCoder线段树题目"""
    atcoder_problems = [
        {
            "platform": "AtCoder",
            "title": "Range Xor Query",
            "url": "https://atcoder.jp/contests/abc185/tasks/abc185_f",
            "difficulty": "Medium"
        },
        {
            "platform": "AtCoder",
            "title": "Segment Tree",
            "url": "https://atcoder.jp/contests/practice2/tasks/practice2_j",
            "difficulty": "Easy"
        }
    ]
    
    problems.extend(atcoder_problems)
    print(f"AtCoder: 成功获取 {len(atcoder_problems)} 道题目")

def scrape_hackerrank():
    """从已有的资料中提取HackerRank线段树题目"""
    hackerrank_problems = [
        {
            "platform": "HackerRank",
            "title": "Array and simple queries",
            "url": "https://www.hackerrank.com/challenges/array-and-simple-queries/problem",
            "difficulty": "Advanced"
        },
        {
            "platform": "HackerRank",
            "title": "Roy and Coin Boxes",
            "url": "https://www.hackerrank.com/challenges/roy-and-coin-boxes/problem",
            "difficulty": "Advanced"
        },
        {
            "platform": "HackerRank",
            "title": "Polynomial Division",
            "url": "https://www.hackerrank.com/challenges/polynomial-divison/problem",
            "difficulty": "Expert"
        },
        {
            "platform": "HackerRank",
            "title": "Range Minimum Query",
            "url": "https://www.hackerrank.com/challenges/range-minimum-query/problem",
            "difficulty": "Easy"
        }
    ]
    
    problems.extend(hackerrank_problems)
    print(f"HackerRank: 成功获取 {len(hackerrank_problems)} 道题目")

def scrape_other_platforms():
    """从已有的资料中提取其他平台线段树题目"""
    other_problems = [
        # HDU
        {
            "platform": "HDU",
            "title": "敌兵布阵",
            "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1166",
            "difficulty": "Easy"
        },
        {
            "platform": "HDU",
            "title": "I Hate It",
            "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1754",
            "difficulty": "Easy"
        },
        {
            "platform": "HDU",
            "title": "Just a Hook",
            "url": "http://acm.hdu.edu.cn/showproblem.php?pid=1698",
            "difficulty": "Medium"
        },
        {
            "platform": "HDU",
            "title": "Billboard",
            "url": "http://acm.hdu.edu.cn/showproblem.php?pid=2795",
            "difficulty": "Medium"
        },
        
        # POJ
        {
            "platform": "POJ",
            "title": "A Simple Problem with Integers",
            "url": "http://poj.org/problem?id=3468",
            "difficulty": "Medium"
        },
        {
            "platform": "POJ",
            "title": "Count Color",
            "url": "http://poj.org/problem?id=2777",
            "difficulty": "Medium"
        },
        {
            "platform": "POJ",
            "title": "Mayor's posters",
            "url": "http://poj.org/problem?id=2528",
            "difficulty": "Medium"
        },
        {
            "platform": "POJ",
            "title": "Balanced Lineup",
            "url": "http://poj.org/problem?id=3264",
            "difficulty": "Easy"
        },
        
        # SPOJ
        {
            "platform": "SPOJ",
            "title": "Can you answer these queries I",
            "url": "https://www.spoj.com/problems/GSS1/",
            "difficulty": "Hard"
        },
        {
            "platform": "SPOJ",
            "title": "Can you answer these queries III",
            "url": "https://www.spoj.com/problems/GSS3/",
            "difficulty": "Hard"
        },
        {
            "platform": "SPOJ",
            "title": "Can you answer these queries IV",
            "url": "https://www.spoj.com/problems/GSS4/",
            "difficulty": "Hard"
        },
        
        # Luogu
        {
            "platform": "Luogu",
            "title": "【模板】线段树 1",
            "url": "https://www.luogu.com.cn/problem/P3372",
            "difficulty": "Medium"
        },
        {
            "platform": "Luogu",
            "title": "【模板】线段树 2",
            "url": "https://www.luogu.com.cn/problem/P3373",
            "difficulty": "Hard"
        },
        
        # LintCode
        {
            "platform": "LintCode",
            "title": "线段树的构造",
            "url": "https://www.lintcode.com/problem/segment-tree-build/description",
            "difficulty": "Medium"
        },
        {
            "platform": "LintCode",
            "title": "线段树查询",
            "url": "https://www.lintcode.com/problem/segment-tree-query/description",
            "difficulty": "Medium"
        },
        {
            "platform": "LintCode",
            "title": "线段树修改",
            "url": "https://www.lintcode.com/problem/segment-tree-modify/description",
            "difficulty": "Medium"
        },
        {
            "platform": "LintCode",
            "title": "区间求和 I",
            "url": "https://www.lintcode.com/problem/interval-sum/description",
            "difficulty": "Medium"
        },
        {
            "platform": "LintCode",
            "title": "区间求和 II",
            "url": "https://www.lintcode.com/problem/interval-sum-ii/description",
            "difficulty": "Hard"
        }
    ]
    
    problems.extend(other_problems)
    print(f"其他平台: 成功获取 {len(other_problems)} 道题目")

def main():
    """主函数"""
    print("开始整理各平台线段树题目...")
    
    # 整理各平台题目
    scrape_leetcode()
    scrape_codeforces()
    scrape_atcoder()
    scrape_hackerrank()
    scrape_other_platforms()
    
    # 保存结果到JSON文件
    with open('segment_tree_problems.json', 'w', encoding='utf-8') as f:
        json.dump(problems, f, ensure_ascii=False, indent=2)
    
    print(f"\n整理完成！共获取 {len(problems)} 道题目")
    print("结果已保存到 segment_tree_problems.json 文件中")

if __name__ == "__main__":
    main()