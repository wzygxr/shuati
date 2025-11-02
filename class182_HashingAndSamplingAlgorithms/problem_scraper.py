#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
字符串哈希相关算法题爬虫
用于从各大OJ平台爬取字符串哈希相关的算法题
"""

import requests
from bs4 import BeautifulSoup
import json
import time
import random

# 各大OJ平台URL
PLATFORMS = {
    "LeetCode": "https://leetcode.cn",
    "Luogu": "https://www.luogu.com.cn",
    "Codeforces": "https://codeforces.com",
    "AtCoder": "https://atcoder.jp",
    "HDU": "https://acm.hdu.edu.cn",
    "POJ": "http://poj.org",
    "洛谷": "https://www.luogu.com.cn"
}

# 字符串哈希相关关键词
HASH_KEYWORDS = [
    "字符串哈希", "哈希字符串", "string hash", "hash string", 
    "rolling hash", "Rabin-Karp", "哈希匹配", "哈希算法"
]

class ProblemScraper:
    def __init__(self):
        self.problems = []
        self.headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36'
        }
    
    def scrape_leetcode(self):
        """爬取LeetCode相关题目"""
        print("正在爬取LeetCode字符串哈希相关题目...")
        
        # LeetCode相关题目URL
        urls = [
            "https://leetcode.cn/problems/implement-strstr/",
            "https://leetcode.cn/problems/repeated-dna-sequences/",
            "https://leetcode.cn/problems/find-substring-with-given-hash-value/",
            "https://leetcode.cn/problems/longest-duplicate-substring/",
            "https://leetcode.cn/problems/distinct-echo-substrings/"
        ]
        
        for url in urls:
            try:
                response = requests.get(url, headers=self.headers, timeout=10)
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    
                    # 提取题目标题
                    title_elem = soup.find('h4', class_='css-10vk87m')
                    if not title_elem:
                        title_elem = soup.find('h4')
                    
                    title = title_elem.text.strip() if title_elem else "未知题目"
                    
                    # 提取题目描述
                    desc_elem = soup.find('div', class_='css-1eusa4c')
                    if not desc_elem:
                        desc_elem = soup.find('div', class_='question-content')
                    
                    description = desc_elem.text.strip() if desc_elem else "无描述"
                    
                    self.problems.append({
                        "platform": "LeetCode",
                        "title": title,
                        "url": url,
                        "description": description[:500] + "..." if len(description) > 500 else description
                    })
                    
                    print(f"已获取题目: {title}")
                    # 避免请求过于频繁
                    time.sleep(random.uniform(1, 2))
                    
            except Exception as e:
                print(f"爬取 {url} 失败: {str(e)}")
    
    def scrape_luogu(self):
        """爬取洛谷相关题目"""
        print("正在爬取洛谷字符串哈希相关题目...")
        
        # 洛谷相关题目URL
        urls = [
            "https://www.luogu.com.cn/problem/P3370",
            "https://www.luogu.com.cn/problem/P2870",
            "https://www.luogu.com.cn/problem/P2249",
            "https://www.luogu.com.cn/problem/P3975"
        ]
        
        for url in urls:
            try:
                response = requests.get(url, headers=self.headers, timeout=10)
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    
                    # 提取题目标题
                    title_elem = soup.find('h1')
                    title = title_elem.text.strip() if title_elem else "未知题目"
                    
                    # 提取题目描述
                    desc_elem = soup.find('div', class_='problem-content')
                    if not desc_elem:
                        desc_elem = soup.find('div', class_='content')
                    
                    description = desc_elem.text.strip() if desc_elem else "无描述"
                    
                    self.problems.append({
                        "platform": "洛谷",
                        "title": title,
                        "url": url,
                        "description": description[:500] + "..." if len(description) > 500 else description
                    })
                    
                    print(f"已获取题目: {title}")
                    # 避免请求过于频繁
                    time.sleep(random.uniform(1, 2))
                    
            except Exception as e:
                print(f"爬取 {url} 失败: {str(e)}")
    
    def scrape_codeforces(self):
        """爬取Codeforces相关题目"""
        print("正在爬取Codeforces字符串哈希相关题目...")
        
        # Codeforces相关题目URL
        urls = [
            "https://codeforces.com/problemset/problem/1200/D",
            "https://codeforces.com/problemset/problem/113/B",
            "https://codeforces.com/problemset/problem/710/F",
            "https://codeforces.com/problemset/problem/452/F"
        ]
        
        for url in urls:
            try:
                response = requests.get(url, headers=self.headers, timeout=10)
                if response.status_code == 200:
                    soup = BeautifulSoup(response.text, 'html.parser')
                    
                    # 提取题目标题
                    title_elem = soup.find('div', class_='title')
                    title = title_elem.text.strip() if title_elem else "未知题目"
                    
                    # 提取题目描述
                    desc_elem = soup.find('div', class_='problem-statement')
                    description = desc_elem.text.strip() if desc_elem else "无描述"
                    
                    self.problems.append({
                        "platform": "Codeforces",
                        "title": title,
                        "url": url,
                        "description": description[:500] + "..." if len(description) > 500 else description
                    })
                    
                    print(f"已获取题目: {title}")
                    # 避免请求过于频繁
                    time.sleep(random.uniform(1, 2))
                    
            except Exception as e:
                print(f"爬取 {url} 失败: {str(e)}")
    
    def save_problems(self, filename="hashing_problems.json"):
        """保存题目到JSON文件"""
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump(self.problems, f, ensure_ascii=False, indent=2)
        print(f"已保存 {len(self.problems)} 道题目到 {filename}")
    
    def run(self):
        """执行爬取任务"""
        print("开始爬取字符串哈希相关算法题...")
        
        self.scrape_leetcode()
        self.scrape_luogu()
        self.scrape_codeforces()
        
        self.save_problems()
        print("爬取完成!")

def main():
    scraper = ProblemScraper()
    scraper.run()

if __name__ == "__main__":
    main()