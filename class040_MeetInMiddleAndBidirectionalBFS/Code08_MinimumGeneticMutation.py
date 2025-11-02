# 最小基因变化
# 题目来源：LeetCode 433
# 题目描述：
# 基因序列可以表示为一条由 8 个字符组成的字符串，其中每个字符都是 'A'、'C'、'G' 和 'T' 之一。
# 假设我们需要研究从基因序列 start 变为 end 所发生的基因变化。
# 一次基因变化意味着这个基因序列中的一个字符发生了变化。
# 同时，每次基因变化的结果，必须是在基因库 bank 中存在的有效基因序列。
# 假设 start 和 end 都是有效的基因序列，start 不等于 end。
# 请找出并返回能够使 start 变为 end 的最少变化次数。
# 如果无法完成此任务，则返回 -1。
# 测试链接 : https://leetcode.cn/problems/minimum-genetic-mutation/
# 
# 算法思路：
# 使用双向BFS算法，从起点和终点同时开始搜索，直到两者相遇
# 这样可以大大减少搜索空间，提高算法效率
# 时间复杂度：O(B)，其中B是基因库的大小
# 空间复杂度：O(B)
# 
# 工程化考量：
# 1. 异常处理：检查输入是否合法
# 2. 性能优化：使用双向BFS减少搜索空间
# 3. 可读性：变量命名清晰，注释详细
# 
# 语言特性差异：
# Python中使用集合进行快速查找和集合操作

def minMutation(start: str, end: str, bank: list[str]) -> int:
    """
    计算从start基因序列到end基因序列的最小变化次数
    
    Args:
        start: 起始基因序列
        end: 目标基因序列
        bank: 基因库
    
    Returns:
        最小变化次数，如果无法完成则返回-1
    """
    # 边界条件检查
    if not start or not end or not bank:
        return -1
    
    # 将基因库转换为集合，提高查找效率
    bank_set = set(bank)
    
    # 如果目标基因不在基因库中，直接返回-1
    if end not in bank_set:
        return -1
    
    # 基因字符集
    genes = ['A', 'C', 'G', 'T']
    
    # 使用双向BFS
    # 从起点开始的搜索集合
    start_set = {start}
    # 从终点开始的搜索集合
    end_set = {end}
    # 全局已访问集合，避免重复搜索
    visited = {start, end}
    
    steps = 0
    
    # 当两个集合都不为空时继续搜索
    while start_set and end_set:
        # 优化：总是从较小的集合开始搜索，减少搜索空间
        if len(start_set) > len(end_set):
            start_set, end_set = end_set, start_set
        
        next_level = set()
        
        # 遍历当前层的所有基因序列
        for current in start_set:
            # 尝试改变每个位置的字符
            current_list = list(current)
            for i in range(len(current_list)):
                original = current_list[i]
                
                # 尝试所有可能的字符替换
                for gene in genes:
                    if gene == original:
                        continue  # 跳过相同的字符
                    
                    current_list[i] = gene
                    next_gene = ''.join(current_list)
                    
                    # 检查是否与另一端的搜索集合相遇
                    if next_gene in end_set:
                        return steps + 1
                    
                    # 如果是有效的基因序列且未访问过，则加入下一层
                    if next_gene in bank_set and next_gene not in visited:
                        next_level.add(next_gene)
                        visited.add(next_gene)
                
                # 恢复原字符
                current_list[i] = original
        
        # 进入下一层
        start_set = next_level
        steps += 1
    
    # 无法找到路径
    return -1

# 测试方法
def main():
    # 测试用例1
    print("测试用例1：")
    start1 = "AACCGGTT"
    end1 = "AACCGGTA"
    bank1 = ["AACCGGTA"]
    print(f"start: \"{start1}\")
    print(f"end: \"{end1}\")
    print(f"bank: {bank1}")
    print("期望输出: 1")
    print(f"实际输出: {minMutation(start1, end1, bank1)}")
    print()
    
    # 测试用例2
    print("测试用例2：")
    start2 = "AACCGGTT"
    end2 = "AAACGGTA"
    bank2 = ["AACCGGTA", "AACCGCTA", "AAACGGTA"]
    print(f"start: \"{start2}\")
    print(f"end: \"{end2}\")
    print(f"bank: {bank2}")
    print("期望输出: 2")
    print(f"实际输出: {minMutation(start2, end2, bank2)}")
    print()
    
    # 测试用例3
    print("测试用例3：")
    start3 = "AAAAACCC"
    end3 = "AACCCCCC"
    bank3 = ["AAAACCCC", "AAACCCCC", "AACCCCCC"]
    print(f"start: \"{start3}\")
    print(f"end: \"{end3}\")
    print(f"bank: {bank3}")
    print("期望输出: 3")
    print(f"实际输出: {minMutation(start3, end3, bank3)}")

if __name__ == "__main__":
    main()