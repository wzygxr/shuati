from typing import List

class Solution:
    """
    Codeforces 296C. Greg and Array
    
    题目描述:
    Greg 有一个长度为 n 的数组 a，初始值都为 0。
    他还有 m 个操作，每个操作是一个三元组 (l, r, d)，表示将区间 [l, r] 中的每个元素加上 d。
    然后他有 k 个指令，每个指令是一个二元组 (x, y)，表示执行操作 x 到操作 y 各一次。
    请输出执行完所有指令后的数组。
    
    示例:
    输入: n = 3, m = 3, k = 3
    操作: 
    操作1: (1, 2, 1)
    操作2: (1, 3, 2)
    操作3: (2, 3, 4)
    指令:
    指令1: (1, 2)
    指令2: (1, 3)
    指令3: (2, 3)
    输出: [9, 18, 17]
    
    题目链接: https://codeforces.com/contest/296/problem/C
    
    解题思路:
    使用两层差分数组技巧来处理多层区间更新操作。
    1. 第一层差分: 统计每个操作被执行多少次
    2. 第二层差分: 根据操作执行次数，计算对原数组的影响
    
    时间复杂度: O(n + m + k) - 需要处理所有操作和指令
    空间复杂度: O(n + m) - 需要存储操作信息和差分数组
    
    这是最优解，因为需要处理多层区间更新。
    """
    
    def gregAndArray(self, n: int, m: int, k: int, operations: List[List[int]], instructions: List[List[int]]) -> List[int]:
        """
        执行Greg的数组操作
        
        Args:
            n: 数组长度
            m: 操作数量
            k: 指令数量
            operations: 操作数组，每个操作包含[l, r, d]
            instructions: 指令数组，每个指令包含[x, y]
            
        Returns:
            执行完所有指令后的数组
        """
        # 边界情况处理
        if n <= 0 or m <= 0 or k <= 0:
            return [0] * n
        
        # 第一层差分: 统计每个操作被执行多少次
        op_count = [0] * (m + 2)  # 操作索引从1开始
        
        # 处理指令，统计每个操作被执行次数
        for instruction in instructions:
            x = instruction[0]  # 起始操作索引
            y = instruction[1]  # 结束操作索引
            
            # 使用差分标记指令区间
            op_count[x] += 1
            if y + 1 <= m:
                op_count[y + 1] -= 1
        
        # 计算每个操作的实际执行次数
        for i in range(1, m + 1):
            op_count[i] += op_count[i - 1]
        
        # 第二层差分: 计算对原数组的影响
        diff = [0] * (n + 2)  # 数组索引从1开始
        
        # 根据操作执行次数，计算对原数组的影响
        for i in range(1, m + 1):
            op = operations[i - 1]  # 操作索引从0开始
            l, r, d = op[0], op[1], op[2]
            count = op_count[i]  # 该操作执行次数
            
            # 应用操作到差分数组
            diff[l] += d * count
            if r + 1 <= n:
                diff[r + 1] -= d * count
        
        # 计算最终结果数组
        result = [0] * n
        current = 0
        for i in range(1, n + 1):
            current += diff[i]
            result[i - 1] = current
        
        return result

def test_greg_and_array():
    """
    测试用例
    """
    solution = Solution()
    
    # 测试用例1: 题目示例
    n1, m1, k1 = 3, 3, 3
    operations1 = [
        [1, 2, 1],  # 操作1
        [1, 3, 2],  # 操作2
        [2, 3, 4]   # 操作3
    ]
    instructions1 = [
        [1, 2],     # 指令1
        [1, 3],     # 指令2
        [2, 3]      # 指令3
    ]
    
    result1 = solution.gregAndArray(n1, m1, k1, operations1, instructions1)
    print(f"测试用例1: {result1}")  # 预期输出: [9, 18, 17]
    
    # 测试用例2: 简单情况
    n2, m2, k2 = 5, 2, 1
    operations2 = [
        [1, 3, 2],  # 操作1
        [2, 4, 3]   # 操作2
    ]
    instructions2 = [
        [1, 2]      # 指令1
    ]
    
    result2 = solution.gregAndArray(n2, m2, k2, operations2, instructions2)
    print(f"测试用例2: {result2}")  # 预期输出: [2, 5, 5, 3, 0]
    
    # 测试用例3: 边界情况
    n3, m3, k3 = 1, 1, 1
    operations3 = [
        [1, 1, 5]   # 操作1
    ]
    instructions3 = [
        [1, 1]      # 指令1
    ]
    
    result3 = solution.gregAndArray(n3, m3, k3, operations3, instructions3)
    print(f"测试用例3: {result3}")  # 预期输出: [5]

if __name__ == "__main__":
    test_greg_and_array()