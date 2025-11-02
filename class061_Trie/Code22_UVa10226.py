"""
UVa 10226 Hardwood Species

题目描述：
统计森林中各种硬木的数量百分比。输入一系列树名，输出每种树名及其占总数的百分比。

解题思路：
1. 使用字典统计每种树的数量（虽然题目在Trie专题中，但此题更适合用字典）
2. 遍历所有树名，统计每种树的数量
3. 计算每种树的百分比并按字典序输出

时间复杂度：O(N*logN)，其中N是树的数量（主要是排序的时间复杂度）
空间复杂度：O(K)，其中K是不同树的种类数
"""

import sys

def main():
    input_lines = []
    for line in sys.stdin:
        input_lines.append(line)
    
    t = int(input_lines[0].strip())  # 测试用例数量
    idx = 1
    
    for i in range(t):
        if i > 0:
            print()  # 每个测试用例之间输出空行
        
        tree_count = {}  # 统计每种树的数量
        total_count = 0  # 树的总数量
        
        # 读取树名，直到遇到空行或文件结束
        while idx < len(input_lines) and input_lines[idx].strip():
            tree_name = input_lines[idx].strip()
            tree_count[tree_name] = tree_count.get(tree_name, 0) + 1
            total_count += 1
            idx += 1
        
        idx += 1  # 跳过空行
        
        # 按字典序排序
        tree_names = sorted(tree_count.keys())
        
        # 输出每种树的百分比
        for tree_name in tree_names:
            count = tree_count[tree_name]
            percentage = (count / total_count) * 100
            print(f"{tree_name} {percentage:.4f}")

if __name__ == "__main__":
    main()